package vehicles;

import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;

public abstract class WaterVehicle extends Vehicle {
    private boolean hasSail;

    public WaterVehicle(String id, String model, double maxSpeed, double currentMileage, boolean hasSail)throws InvalidOperationException{
        super(id, model, maxSpeed, currentMileage);
        this.hasSail = hasSail;
    }

    public Boolean getHasSail() {
        return hasSail;
    }

    @Override
    public double estimateJourneyTime(double distance) throws InvalidOperationException {
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative");
        }
        double baseTime = distance / getMaxSpeed();
        return baseTime * 1.15;
    }
    
    public abstract void move(double distance) 
        throws InvalidOperationException, InsufficientFuelException;
    
    public abstract double calculateFuelEfficiency();
}