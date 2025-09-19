package fleet;

import vehicles.*;

public final class VehicleFactory {
    private VehicleFactory() {}

    public static Vehicle createVehicle(String[] parts) throws Exception {
        if (parts.length == 0) return null;
        String type = parts[0].trim();
        switch (type) {
            case "Car": {
                String id = parts[1];
                String model = parts[2];
                double maxSpeed = parseDouble(parts, 3, 100.0);
                double currMileage = parseDouble(parts, 4, 0.0);
                int numWheels = parseInt(parts, 5, 4);
                Car c = new Car(id, model, maxSpeed, currMileage, numWheels);
                if (parts.length > 6) {
                    double fuel = parseDouble(parts, 6, 0.0);
                    if (fuel > 0) c.refuel(fuel);
                }
                if (parts.length > 8) {
                    int currP = parseInt(parts, 8, 0);
                    if (currP > 0) c.boardPassengers(currP);
                }
                return c;
            }
            case "Truck": {
                String id = parts[1], model = parts[2];
                double maxSpeed = parseDouble(parts, 3, 80.0);
                double currMileage = parseDouble(parts, 4, 0.0);
                int numWheels = parseInt(parts, 5, 6);
                Truck t = new Truck(id, model, maxSpeed, currMileage, numWheels);
                if (parts.length > 6) {
                    double fuel = parseDouble(parts, 6, 0.0);
                    if (fuel > 0) t.refuel(fuel);
                }
                if (parts.length > 8) {
                    double currCargo = parseDouble(parts, 8, 0.0);
                    if (currCargo > 0) t.loadCargo(currCargo);
                }
                return t;
            }
            case "Bus": {
                String id = parts[1], model = parts[2];
                double maxSpeed = parseDouble(parts, 3, 100.0);
                double currMileage = parseDouble(parts, 4, 0.0);
                int numWheels = parseInt(parts, 5, 6);
                Bus b = new Bus(id, model, maxSpeed, currMileage, numWheels);
                if (parts.length > 6) {
                    double fuel = parseDouble(parts, 6, 0.0);
                    if (fuel > 0) b.refuel(fuel);
                }
                if (parts.length > 8) {
                    int currP = parseInt(parts, 8, 0);
                    if (currP > 0) b.boardPassengers(currP);
                }
                if (parts.length > 10) {
                    double currCargo = parseDouble(parts, 10, 0.0);
                    if (currCargo > 0) b.loadCargo(currCargo);
                }
                return b;
            }
            case "Airplane": {
                String id = parts[1], model = parts[2];
                double maxSpeed = parseDouble(parts, 3, 700.0);
                double currMileage = parseDouble(parts, 4, 0.0);
                double maxAlt = parseDouble(parts, 5, 10000.0);
                Airplane a = new Airplane(id, model, maxSpeed, currMileage, maxAlt);
                if (parts.length > 6) {
                    double fuel = parseDouble(parts, 6, 0.0);
                    if (fuel > 0) a.refuel(fuel);
                }
                if (parts.length > 8) {
                    int currP = parseInt(parts, 8, 0);
                    if (currP > 0) a.boardPassengers(currP);
                }
                if (parts.length > 10) {
                    double currCargo = parseDouble(parts, 10, 0.0);
                    if (currCargo > 0) a.loadCargo(currCargo);
                }
                return a;
            }
            case "CargoShip": {
                String id = parts[1], model = parts[2];
                double maxSpeed = parseDouble(parts, 3, 30.0);
                double currMileage = parseDouble(parts, 4, 0.0);
                boolean hasSail = parseBoolean(parts, 5, true);
                CargoShip s = new CargoShip(id, model, maxSpeed, currMileage, hasSail);
                if (!hasSail && parts.length > 6) {
                    double fuel = parseDouble(parts, 6, 0.0);
                    if (fuel > 0) s.refuel(fuel);
                }
                if (parts.length > 8) {
                    double currCargo = parseDouble(parts, 8, 0.0);
                    if (currCargo > 0) s.loadCargo(currCargo);
                }
                return s;
            }
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }

    private static double parseDouble(String[] parts, int idx, double def) {
        try { return Double.parseDouble(parts[idx]); } catch (Exception e) { return def; }
    }
    private static int parseInt(String[] parts, int idx, int def) {
        try { return Integer.parseInt(parts[idx]); } catch (Exception e) { return def; }
    }
    private static boolean parseBoolean(String[] parts, int idx, boolean def) {
        try { return Boolean.parseBoolean(parts[idx]); } catch (Exception e) { return def; }
    }
}
