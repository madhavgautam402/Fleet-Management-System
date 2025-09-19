package vehicles;

import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;

public abstract class LandVehicle extends Vehicle {
    private int numWheels;

    public LandVehicle(String id, String model, double maxSpeed, double currentMileage, int numWheels)throws InvalidOperationException{
        super(id, model, maxSpeed, currentMileage);
        this.numWheels = numWheels;
    }

    public int getNumWheels() {
        return numWheels;
    }

    @Override
    public double estimateJourneyTime(double distance) throws InvalidOperationException {
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative");
        }
        double baseTime = distance / getMaxSpeed();
        return baseTime * 1.1;
    }
    
    public abstract void move(double distance) 
        throws InvalidOperationException, InsufficientFuelException;
    
    public abstract double calculateFuelEfficiency();
}
