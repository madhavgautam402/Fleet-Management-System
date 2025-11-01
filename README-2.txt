Fleet Management System – README (Assignment 2)
************************************************************************************

1. Compilation and Execution

To Compile:

javac exceptions/*.java interfaces/*.java vehicles/*.java fleet/*.java


To Run:

java fleet.Main

************************************************************************************

2. Use of Collections and Justification

ArrayList<Vehicle> fleet
Using an ArrayList as a dynamic storage option for the fleet to provide quick random access, straightforward iteration, and resizing ability when vehicles are added or removed.

HashSet<String> distinctModels
Using a HashSet to make sure the model names are unique, thus avoiding duplicates and demonstrating  set-based collection manipulation.

Comparator with Collections.sort()
Used to sort vehicles by maximum speed, model name, and fuel efficiency without requiring a TreeSet.
This fulfills the ordering and sorting requirement while maintaining flexibility.

************************************************************************************


3. File I/O Implementation

UseD Java’s java.io handle I/O operations.
The methods saveToFile and loadFromFile implement persistence.

Fleet data is saved in CSV format where each line represents a vehicle and its attributes.
For Example:-
Car,C001,Honda City,180.00,0.00,4,50.00,5,0,false

************************************************************************************

4. Program Features Sample Run


Fleet Manager CLI
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
11. Show Fastest and Slowest Vehicle
12. Sort Fleet by Maximum Speed
13. Sort Fleet by Model Name
14. Sort Fleet by Efficiency
15. Exit

for input 11, the fleets fastest and slowest vehicles along with thier Maximum speeds are displayed
for input 12, the fleet sorted in the ascending order of the vehicles Maximum speed is displayed.
for input 13, the fleet sorted in the alphabetical order of the vehicles Model  is displayed.
for input 14, the fleet sorted in the ascending order of the vehicles Efficiency is displayed.

************************************************************************************
