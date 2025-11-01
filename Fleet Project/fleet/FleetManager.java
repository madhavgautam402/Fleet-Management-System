package fleet;

import vehicles.*;
import exceptions.InvalidOperationException;
import exceptions.InsufficientFuelException;

import interfaces.FuelConsumable;
import interfaces.Maintainable;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FleetManager {

    // Using ArrayList for dynamic fleet storage
    private List<Vehicle> fleet = new ArrayList<Vehicle>();

    // Using HashSet to ensure unique model names
    private final Set<String> distinctModels = new HashSet<>();

    public void addVehicle(Vehicle v) throws InvalidOperationException {
        Objects.requireNonNull(v, "Vehicle cannot be null");
        Optional<Vehicle> dup = fleet.stream().filter(x -> x.getId().equals(v.getId())).findAny();
        if (dup.isPresent()) throw new InvalidOperationException("Duplicate vehicle ID: " + v.getId());
        fleet.add(v);
        distinctModels.add(v.getModel());
    }

    public void removeVehicle(String id) throws InvalidOperationException {
        boolean removed = fleet.removeIf(v -> v.getId().equals(id));
        if (!removed) throw new InvalidOperationException("Vehicle with ID " + id + " not found.");
    }

    public void startAllJourneys(double distance) {
        for (Vehicle v : new ArrayList<>(fleet)) {
            try {
                v.move(distance);
            } catch (InvalidOperationException e) {
                System.err.println("Invalid operation for " + v.getId() + ": " + e.getMessage());
            } catch (InsufficientFuelException e) {
                System.err.println("Fuel problem for " + v.getId() + ": " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error for " + v.getId() + ": " + e.getMessage());
            }
        }
    }

    public double getTotalFuelConsumption(double distance) {
        double total = 0.0;
        for (Vehicle v : fleet) {
            if (v instanceof FuelConsumable) {
                FuelConsumable fc = (FuelConsumable) v;
                try {
                    total += fc.consumeFuel(distance);
                } catch (InsufficientFuelException e) {
                    System.err.println("Insufficient fuel while computing consumption for " + v.getId() + ": " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error consuming fuel for " + v.getId() + ": " + e.getMessage());
                }
            }
        }
        return total;
    }

    public void maintainAll() {
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable) {
                Maintainable m = (Maintainable) v;
                if (m.needsMaintenance()) m.performMaintenance();
            }
        }
    }

    public List<Vehicle> searchByType(Class<?> type) {
        return fleet.stream()
                .filter(v -> type.isAssignableFrom(v.getClass()))
                .collect(Collectors.toList());
    }

    public void sortFleetByEfficiency() {
        Collections.sort(fleet);
        System.out.println("Fleet sorted by fuel efficiency (ascending order).");
    }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fleet Report\n");
        sb.append("**********************************************\n");
        sb.append("Total vehicles: ").append(fleet.size()).append("\n");
        Map<String, Long> byType = fleet.stream()
                .collect(Collectors.groupingBy(v -> v.getClass().getSimpleName(), Collectors.counting()));
        sb.append("Vehicle Count by type:\n");
        for (Map.Entry<String, Long> e : byType.entrySet()) {
            sb.append("  ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }
        double avgEff = fleet.stream().mapToDouble(Vehicle::calculateFuelEfficiency).average().orElse(0.0);
        sb.append(String.format("Average fuel efficiency: %.2f km/l\n", avgEff));
        double totalMileage = fleet.stream().mapToDouble(FleetManager::getMileageSafe).sum();
        sb.append(String.format("Total mileage: %.2f km\n", totalMileage));
        List<String> needs = fleet.stream()
                .filter(v -> v instanceof Maintainable && ((Maintainable) v).needsMaintenance())
                .map(Vehicle::getId).collect(Collectors.toList());
        sb.append("Vehicles needing maintenance: ").append(needs.size()).append("\n");
        if (!needs.isEmpty()) sb.append("  ").append(String.join(", ", needs)).append("\n");
        return sb.toString();
    }

    public List<Vehicle> getVehiclesNeedingMaintenance() {
        return fleet.stream()
                .filter(v -> v instanceof Maintainable && ((Maintainable) v).needsMaintenance())
                .collect(Collectors.toList());
    }

    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Vehicle v : fleet) {
                String type = v.getClass().getSimpleName();
                String id = v.getId();
                String model = safeInvokeString(v, "getModel");
                double maxSpeed = safeInvokeDouble(v, "getMaxSpeed");
                double currMileage = getMileageSafe(v);

                if (v instanceof vehicles.Car) {
                    vehicles.Car c = (vehicles.Car) v;
                    int numWheels = safeInvokeInt(v, "getNumWheels", 4);
                    double fuelLevel = safeInvokeDouble(v, "getFuelLevel");
                    int pCap = safeInvokeInt(v, "getPassengerCapacity", 5);
                    int currP = safeInvokeInt(v, "getCurrentPassengers", 0);
                    boolean mNeeded = (c instanceof Maintainable) && ((Maintainable) c).needsMaintenance();
                    pw.printf("Car,%s,%s,%.2f,%.2f,%d,%.2f,%d,%d,%b%n",
                            id, model, maxSpeed, currMileage, numWheels, fuelLevel, pCap, currP, mNeeded);
                } else if (v instanceof vehicles.Truck) {
                    vehicles.Truck t = (vehicles.Truck) v;
                    int numWheels = safeInvokeInt(v, "getNumWheels", 6);
                    double fuelLevel = safeInvokeDouble(v, "getFuelLevel");
                    double cargoCap = safeInvokeDouble(v, "getCargoCapacity");
                    double currCargo = safeInvokeDouble(v, "getCurrentCargo");
                    boolean mNeeded = (t instanceof Maintainable) && ((Maintainable) t).needsMaintenance();
                    pw.printf("Truck,%s,%s,%.2f,%.2f,%d,%.2f,%.2f,%.2f,%b%n",
                            id, model, maxSpeed, currMileage, numWheels, fuelLevel, cargoCap, currCargo, mNeeded);
                } else if (v instanceof vehicles.Bus) {
                    vehicles.Bus b = (vehicles.Bus) v;
                    int numWheels = safeInvokeInt(v, "getNumWheels", 6);
                    double fuelLevel = safeInvokeDouble(v, "getFuelLevel");
                    int pCap = safeInvokeInt(v, "getPassengerCapacity", 50);
                    int currP = safeInvokeInt(v, "getCurrentPassengers", 0);
                    double cargoCap = safeInvokeDouble(v, "getCargoCapacity");
                    double currCargo = safeInvokeDouble(v, "getCurrentCargo");
                    boolean mNeeded = (b instanceof Maintainable) && ((Maintainable) b).needsMaintenance();
                    pw.printf("Bus,%s,%s,%.2f,%.2f,%d,%.2f,%d,%d,%.2f,%.2f,%b%n",
                            id, model, maxSpeed, currMileage, numWheels, fuelLevel, pCap, currP, cargoCap, currCargo, mNeeded);
                } else if (v instanceof vehicles.Airplane) {
                    vehicles.Airplane a = (vehicles.Airplane) v;
                    double maxAlt = safeInvokeDouble(v, "getMaxAltitude");
                    double fuelLevel = safeInvokeDouble(v, "getFuelLevel");
                    int pCap = safeInvokeInt(v, "getPassengerCapacity", 200);
                    int currP = safeInvokeInt(v, "getCurrentPassengers", 0);
                    double cargoCap = safeInvokeDouble(v, "getCargoCapacity");
                    double currCargo = safeInvokeDouble(v, "getCurrentCargo");
                    boolean mNeeded = (a instanceof Maintainable) && ((Maintainable) a).needsMaintenance();
                    pw.printf("Airplane,%s,%s,%.2f,%.2f,%.2f,%.2f,%d,%d,%.2f,%.2f,%b%n",
                            id, model, maxSpeed, currMileage, maxAlt, fuelLevel, pCap, currP, cargoCap, currCargo, mNeeded);
                } else if (v instanceof vehicles.CargoShip) {
                    vehicles.CargoShip s = (vehicles.CargoShip) v;
                    boolean hasSail = safeInvokeBoolean(v, "getHasSail", false);
                    double fuelLevel = safeInvokeDouble(v, "getFuelLevel");
                    double cargoCap = safeInvokeDouble(v, "getCargoCapacity");
                    double currCargo = safeInvokeDouble(v, "getCurrentCargo");
                    boolean mNeeded = (s instanceof Maintainable) && ((Maintainable) s).needsMaintenance();
                    pw.printf("CargoShip,%s,%s,%.2f,%.2f,%b,%.2f,%.2f,%.2f,%b%n",
                            id, model, maxSpeed, currMileage, hasSail, fuelLevel, cargoCap, currCargo, mNeeded);
                } else {
                    pw.printf("%s,%s,%s,%.2f,%.2f%n", type, id, model, maxSpeed, currMileage);
                }
            }
            System.out.println("Saved Fleet to file: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving fleet: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<Vehicle> loaded = new ArrayList<>();
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", -1);
                try {
                    Vehicle v = VehicleFactory.createVehicle(parts);
                    if (v != null) {
                        loaded.add(v);
                        distinctModels.add(v.getModel());
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNo + ": " + e.getMessage());
                }
            }
            fleet.clear();
            fleet.addAll(loaded);
            System.out.println("Loaded " + loaded.size() + " vehicles from " + filename);
        } catch (IOException e) {
            System.err.println("Error loading fleet: " + e.getMessage());
        }
    }

    public List<Vehicle> listAll() {
        return new ArrayList<>(fleet);
    }

    // new methods for data analysis

    public List<Vehicle> getFleetSortedBySpeed() {
        List<Vehicle> sorted = new ArrayList<>(fleet);
        sorted.sort(Comparator.comparingDouble(this::safeGetMaxSpeed));
        return sorted;
    }

    public List<Vehicle> getFleetSortedByModel() {
        List<Vehicle> sorted = new ArrayList<>(fleet);
        sorted.sort(Comparator.comparing(Vehicle::getModel, String.CASE_INSENSITIVE_ORDER));
        return sorted;
    }

    public List<Vehicle> getFleetSortedByEfficiency() {
        List<Vehicle> sorted = new ArrayList<>(fleet);
        sorted.sort(Comparator.comparingDouble(Vehicle::calculateFuelEfficiency));
        return sorted;
    }

    public String getFastestAndSlowestSummary() {
        if (fleet.isEmpty()) return "Fleet is empty.";
        Vehicle fastest = Collections.max(fleet, Comparator.comparingDouble(this::safeGetMaxSpeed));
        Vehicle slowest = Collections.min(fleet, Comparator.comparingDouble(this::safeGetMaxSpeed));
        return String.format("""
                Fastest Vehicle:
                  ID: %s | Model: %s | Speed: %.1f km/h
                Slowest Vehicle:
                  ID: %s | Model: %s | Speed: %.1f km/h
                """,
                fastest.getId(), fastest.getModel(), safeGetMaxSpeed(fastest),
                slowest.getId(), slowest.getModel(), safeGetMaxSpeed(slowest));
    }

    private double safeGetMaxSpeed(Vehicle v) {
        try {
            return (double) v.getClass().getMethod("getMaxSpeed").invoke(v);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static double getMileageSafe(Vehicle v) {
        String[] attempts = {"getCurrMileage", "getCurr_mileage", "getCurrentMileage", "getMileage"};
        for (String m : attempts) {
            try {
                java.lang.reflect.Method method = v.getClass().getMethod(m);
                Object out = method.invoke(v);
                if (out instanceof Number) return ((Number) out).doubleValue();
            } catch (Exception ignored) {}
        }
        return 0.0;
    }

    private static String safeInvokeString(Object obj, String methodName) {
        try {
            java.lang.reflect.Method m = obj.getClass().getMethod(methodName);
            Object r = m.invoke(obj);
            return r == null ? "" : r.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static double safeInvokeDouble(Object obj, String methodName) {
        try {
            java.lang.reflect.Method m = obj.getClass().getMethod(methodName);
            Object r = m.invoke(obj);
            if (r instanceof Number) return ((Number) r).doubleValue();
            return Double.parseDouble(r.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static int safeInvokeInt(Object obj, String methodName, int def) {
        try {
            java.lang.reflect.Method m = obj.getClass().getMethod(methodName);
            Object r = m.invoke(obj);
            if (r instanceof Number) return ((Number) r).intValue();
            return Integer.parseInt(r.toString());
        } catch (Exception e) {
            return def;
        }
    }

    private static boolean safeInvokeBoolean(Object obj, String methodName, boolean def) {
        try {
            java.lang.reflect.Method m = obj.getClass().getMethod(methodName);
            Object r = m.invoke(obj);
            if (r instanceof Boolean) return (Boolean) r;
            return Boolean.parseBoolean(r.toString());
        } catch (Exception e) {
            return def;
        }
    }
}
