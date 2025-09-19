package vehicles;

import exceptions.InvalidOperationException;
import exceptions.OverloadException;
import exceptions.InsufficientFuelException;
import interfaces.CargoCarrier;
import interfaces.FuelConsumable;
import interfaces.Maintainable;
import interfaces.PassengerCarrier;

public class Bus extends LandVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {
    
    private double fuelLevel = 0;
    private int passengerCapacity = 50;
    private int currentPassengers = 0;
    private double cargoCapacity = 500;
    private double currentCargo = 0;
    private boolean maintenanceNeeded = false;

    public Bus(String id, String model, double maxSpeed, double currentMileage, int numWheels) 
            throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage, numWheels);
    }

    @Override
    public double calculateFuelEfficiency() {
        return 10.0;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) throw new InvalidOperationException("Distance cannot be negative.");
        double fuel_reqired = distance / calculateFuelEfficiency();
        if (fuel_reqired > fuelLevel) throw new InsufficientFuelException("Not enough fuel.");
        fuelLevel -= fuel_reqired;
        updateMileage(distance);
        System.out.println("Drove on road for "+distance+" km");
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount <= 0) throw new InvalidOperationException("Fuel amount must be positive.");
        fuelLevel += amount;
        System.out.println("Refueled " + amount + " liters in the fuel tank.");
    }

    @Override
    public double getFuelLevel() {
        return fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {
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
    public int getCurrentPassengers() {
        return currentPassengers;
    }

    @Override
    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    @Override
    public void boardPassengers(int n) throws InvalidOperationException {
        if (n <= 0) throw new InvalidOperationException("Passenger count must be positive.");
        if (currentPassengers + n > passengerCapacity)
            throw new InvalidOperationException("Exceeds passenger capacity.");
        currentPassengers += n;
        System.out.println(n + " passengers boarded. Current passengers: " + currentPassengers);
    }

    @Override
    public void disembarkPassengers(int n) throws InvalidOperationException {
        if (currentPassengers - n < 0)
            throw new InvalidOperationException("Cannot have negative passengers.");
        currentPassengers -= n;
        System.out.println(n + " passengers disembarked. Current passengers: " + currentPassengers);
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
