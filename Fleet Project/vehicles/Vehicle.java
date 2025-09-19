package vehicles;

import exceptions.InvalidOperationException;
import exceptions.InsufficientFuelException;

public abstract class Vehicle implements Comparable<Vehicle>{
    private String id;
    private String model;
    private double maxSpeed;     
    private double currentMileage;

    public Vehicle(String id, String model, double maxSpeed,double currentMileage) throws InvalidOperationException {
        if (id == null || id.trim().isEmpty()) {
            throw  new InvalidOperationException("Vehicle ID cannot be empty");
        }
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = currentMileage;
    }

    public abstract void move(double distance) 
        throws InvalidOperationException, InsufficientFuelException;

    public abstract double calculateFuelEfficiency();

    public abstract double estimateJourneyTime(double distance) 
            throws InvalidOperationException;


    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public double getCurrentMileage() {
        return currentMileage;
    }

    public double getMaxSpeed() {   
        return maxSpeed;
    }

    public void displayInfo() {
        System.out.println(this.toString());
    }

    protected void updateMileage(double distance) {
        this.currentMileage += distance;
    }

    @Override
    public String toString() {
        return id + " | Model: " + model + " | Speed: " + maxSpeed 
               + " km/h | Mileage: " + currentMileage;
    }
    @Override
    public int compareTo(Vehicle other) {
        return Double.compare(this.calculateFuelEfficiency(), other.calculateFuelEfficiency());
    }
}
