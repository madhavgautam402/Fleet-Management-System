package vehicles;

import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import exceptions.InsufficientFuelException;
import interfaces.CargoCarrier;
import interfaces.FuelConsumable;
import interfaces.Maintainable;

public class CargoShip extends WaterVehicle implements FuelConsumable, CargoCarrier, Maintainable {
    
    private double fuelLevel = 0;
    private double cargoCapacity = 50000;
    private double currentCargo = 0;
    private boolean maintenanceNeeded = false;

    public CargoShip(String id, String model, double maxSpeed, double currentMileage, boolean hasSail) 
            throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage, hasSail);
    }

    @Override
    public double calculateFuelEfficiency() {
        return getHasSail() ? 0.0 : 4.0;
    }
    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) throw new InvalidOperationException("Distance cannot be negative.");
        if (!getHasSail()) {
            double fuelRequired = distance / calculateFuelEfficiency();
            if (fuelRequired > fuelLevel) throw new InsufficientFuelException("Not enough fuel.");
            fuelLevel -= fuelRequired;
        }

        updateMileage(distance);
        System.out.println("Sailing with cargo for " + distance + " km.");
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (getHasSail()) throw new InvalidOperationException("This ship has a sail and does not use fuel.");
        if (amount <= 0) throw new InvalidOperationException("Fuel amount must be positive.");
        fuelLevel += amount;
        System.out.println("Refueled " + amount + " liters in the fuel tank.");
    }

    @Override
    public double getFuelLevel() {
        return getHasSail() ? 0.0 : fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {
        if (getHasSail()) return 0.0;
        double fuelNeeded = distance / calculateFuelEfficiency();
        if (fuelNeeded > fuelLevel) throw new InsufficientFuelException("Not enough fuel.");
        fuelLevel -= fuelNeeded;
        return fuelNeeded;
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {
        if (weight <= 0) {
            throw new OverloadException("Cargo weight must be positive.");
        }
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Exceeds cargo capacity! Cannot load " + weight + " kg.");
        }
        currentCargo += weight;
        System.out.println("Loaded " + weight + " kg. Current cargo: " + currentCargo + " / " + cargoCapacity);
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {
        if (weight <= 0) {
            throw new InvalidOperationException("Cargo weight must be positive.");
        }
        if (weight > currentCargo) {
            throw new InvalidOperationException("Cannot unload more cargo than currently loaded.");
        }
        currentCargo -= weight;
        System.out.println("Unloaded " + weight + " kg. Current cargo: " + currentCargo + " / " + cargoCapacity);
    }


    @Override
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public double getCurrentCargo() {
        return currentCargo;
    }

    @Override
    public boolean needsMaintenance() {
        return maintenanceNeeded;
    }

    @Override
    public void scheduleMaintenance() {
        maintenanceNeeded = true;
        System.out.println("Maintenance scheduled.");
    }
    @Override
    public void performMaintenance() {
        maintenanceNeeded = false; 
        System.out.println("Maintenance completed. Vehicle is now ready for use.");
    }

    
}
