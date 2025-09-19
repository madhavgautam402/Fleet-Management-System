package fleet;

import vehicles.*;
import exceptions.InvalidOperationException;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final FleetManager manager = new FleetManager();
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            manager.addVehicle(new Car("C001","Honda City",180.0,0.0,4));
            manager.addVehicle(new Truck("T001","Mahindra",90.0,0.0,6));
            manager.addVehicle(new Bus("B001","Mercedes-Benz",100.0,0.0,6));
            manager.addVehicle(new Airplane("A001","Boeing",950.0,0.0,10000.0));
            manager.addVehicle(new CargoShip("S001","Titanic",40.0,0.0,false));
        } catch (InvalidOperationException e) {
            System.err.println("Error building demo fleet: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            printMenu();
            String choice = in.nextLine().trim();
            switch (choice) {
                case "1": addVehicleCLI(); break;
                case "2": removeVehicleCLI(); break;
                case "3": startJourneyCLI(); break;
                case "4": refuelAllCLI(); break;
                case "5": performMaintenanceCLI(); break;
                case "6": System.out.println(manager.generateReport()); break;
                case "7": saveFleetCLI(); break;
                case "8": loadFleetCLI(); break;
                case "9": searchByTypeCLI(); break;
                case "10": listMaintenanceCLI(); break;
                case "11": running = false; break;
                default: System.out.println("Invalid Command. Please try again.");
            }
        }
        System.out.println("Exiting. Thank You :)");
    }

    private static void printMenu() {
        System.out.println("\nFleet Manager CLI");
        System.out.println("1. Add Vehicle");
        System.out.println("2. Remove Vehicle");
        System.out.println("3. Start Journey");
        System.out.println("4. Refuel All ");
        System.out.println("5. Perform Maintenance");
        System.out.println("6. Generate Report");
        System.out.println("7. Save Fleet");
        System.out.println("8. Load Fleet");
        System.out.println("9. Search by Type");
        System.out.println("10. List Vehicles Needing Maintenance");
        System.out.println("11. Exit");
        System.out.print("Choose: ");
    }

    private static void addVehicleCLI() {
        try {
            System.out.print("Type (Car,Truck,Bus,Airplane,CargoShip): ");
            String type = in.nextLine().trim();
            System.out.print("ID: "); String id = in.nextLine().trim();
            System.out.print("Model: "); String model = in.nextLine().trim();
            System.out.print("Max speed: "); double maxSpeed = Double.parseDouble(in.nextLine().trim());
            System.out.print("Current mileage: "); double curr = Double.parseDouble(in.nextLine().trim());
            switch (type) {
                case "Car": {
                    System.out.print("Num wheels: "); int nw = Integer.parseInt(in.nextLine().trim());
                    manager.addVehicle(new Car(id, model, maxSpeed, curr, nw));
                    break;
                }
                case "Truck": {
                    System.out.print("Num wheels: "); int nw = Integer.parseInt(in.nextLine().trim());
                    manager.addVehicle(new Truck(id, model, maxSpeed, curr, nw));
                    break;
                }
                case "Bus": {
                    System.out.print("Num wheels: "); int nw = Integer.parseInt(in.nextLine().trim());
                    manager.addVehicle(new Bus(id, model, maxSpeed, curr, nw));
                    break;
                }
                case "Airplane": {
                    System.out.print("Max altitude: "); double alt = Double.parseDouble(in.nextLine().trim());
                    manager.addVehicle(new Airplane(id, model, maxSpeed, curr, alt));
                    break;
                }
                case "CargoShip": {
                    System.out.print("Has sail (true/false): "); boolean hs = Boolean.parseBoolean(in.nextLine().trim());
                    manager.addVehicle(new CargoShip(id, model, maxSpeed, curr, hs));
                    break;
                }
                default: System.out.println("Unknown type.");
            }
        } catch (Exception e) {
            System.err.println("Error adding vehicle: " + e.getMessage());
        }
    }

    private static void removeVehicleCLI() {
        System.out.print("ID to remove: ");
        String id = in.nextLine().trim();
        try {
            manager.removeVehicle(id);
            System.out.println("Removed " + id);
        } catch (InvalidOperationException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void startJourneyCLI() {
        try {
            System.out.print("Distance (km): ");
            double d = Double.parseDouble(in.nextLine().trim());
            manager.startAllJourneys(d);
        } catch (Exception e) {
            System.err.println("Invalid distance.");
        }
    }

    private static void refuelAllCLI() {
        System.out.print("Refuel amount to add to each fuel-capable vehicle (liters): ");
        try {
            double amt = Double.parseDouble(in.nextLine().trim());
            for (Vehicle v : manager.listAll()) {
                if (v instanceof interfaces.FuelConsumable) {
                    try {
                        ((interfaces.FuelConsumable) v).refuel(amt);
                    } catch (Exception e) {
                        System.err.println("Refuel failed for " + v.getId() + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid amount.");
        }
    }

    private static void performMaintenanceCLI() {
        manager.maintainAll();
        System.out.println("Performed maintenance on vehicles that needed it.");
    }

    private static void saveFleetCLI() {
        System.out.print("Filename to save: ");
        String fn = in.nextLine().trim();
        manager.saveToFile(fn);
    }

    private static void loadFleetCLI() {
        System.out.print("Filename to load: ");
        String fn = in.nextLine().trim();
        manager.loadFromFile(fn);
    }

    private static void searchByTypeCLI() {
        System.out.print("Type name to search (Car, Truck, Bus, Airplane, CargoShip, FuelConsumable): ");
        String type = in.nextLine().trim();
        try {
            Class<?> cls = switch (type) {
                case "Car" -> vehicles.Car.class;
                case "Truck" -> vehicles.Truck.class;
                case "Bus" -> vehicles.Bus.class;
                case "Airplane" -> vehicles.Airplane.class;
                case "CargoShip" -> vehicles.CargoShip.class;
                case "FuelConsumable" -> interfaces.FuelConsumable.class;
                default -> null;
            };
            if (cls == null) { System.out.println("Unknown type."); return; }
            List<Vehicle> results = manager.searchByType(cls);
            System.out.println("Found " + results.size() + " vehicles:");
            for (Vehicle v : results) System.out.println("  " + v.getId() + " (" + v.getClass().getSimpleName() + ")");
        } catch (Exception e) {
            System.err.println("Search failed: " + e.getMessage());
        }
    }

    private static void listMaintenanceCLI() {
        List<Vehicle> needs = manager.getVehiclesNeedingMaintenance();
        if (needs.isEmpty()) System.out.println("No vehicles need maintenance.");
        else {
            System.out.println("Vehicles needing maintenance:");
            needs.forEach(v -> System.out.println("  " + v.getId() + " (" + v.getClass().getSimpleName() + ")"));
        }
    }
}
