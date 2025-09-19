Fleet Management System – README

*********************************************************************************
## Object-Oriented Concepts Illustrated

- Inheritance:
  - LandVehicle, AirVehicle and WaterVehicle inherit from Vehicle to provide specialized attributes.
  - Car, Truck, Bus, Airplane, CargoShip all derive from the top-level abstract class Vehicle.

- Polymorphism:
  - Methods such as estimateJourneyTime() are overridden in subclasses to offer type-specific behavior.
  - FleetManager works with a generic list of Vehicle objects, yet invokes subclass-specific behavior at runtime.

- Abstract Classes:
- Vehicle is abstract, imposing shared characteristics (id, model, maxspeed,mileage etc.) but asking subclasses to provide journey estimation.
- AirVehicle abstracts flight-specific behavior as well.

- Interfaces:
- FuelConsumable guarantees vehicles make fuel consumption available.
- Maintainable guarantees vehicles may report and undertake maintenance.

*********************************************************************************

## Compilation Instructions

1. Navigate to the project root (folder containing fleet, vehicles, and exceptions).
2. Compile all classes:
   javac exceptions/*.java interfaces/*.java vehicles/*.java fleet/*.java
3. Run the CLI:
   java fleet.Main

*********************************************************************************

## Using the CLI

- The CLI provides a menu-driven interface. Example usage:
  1. Add Vehicle
  2. Remove Vehicle
  3. Start Journey
  4. Refuel All
  5. Perform Maintenance
  6. Generate Report
  7. Save Fleet
  8. Load Fleet
  9. Search by Type
  10. List Vehicles Needing Maintenance
  11. Exit

*********************************************************************************

## Persistence Demo

- The fleet is saved to a CSV file (Data.csv).
- To test persistence:
  1. Run the program, add a few vehicles, and select Save Fleet.
  2. Exit the program.
  3. Run the program again and choose Load Fleet.
  4. The previously saved vehicles should appear.

*********************************************************************************
## Demo Walkthrough

Example run (simplified):
1. Add Vehicle → Car (ID: C003, Model: Mahindra-THAR, Fuel: 40)
3. Start All Journeys → Vehicles consume fuel and report journey times
4. Generate Report → Shows fuel consumption and mileage
5. Save Fleet → You can save the data to the previously created Data.csv or to any other file by mentioning the file name
6. Load Fleet → Vehicles appear from the file name you put.
7. Generate Report → A report of all the Vehicles in the fleet appears.
8. Exit
Expected output includes journey times, fuel usage, and maintenance status for each vehicle.

*********************************************************************************
