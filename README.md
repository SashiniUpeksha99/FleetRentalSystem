**#Fleet Rental Management System (FRMS)**
---
#### Module: ITS1033 – Object-Oriented Programming
#### Programme: Higher Diploma in Software Engineering (HDSE 77)
#### Institution: Institute of Software Engineering (IJSE)
#### Application type: Java console application

---
### **Project Overview**
``
Fleet Rental Management System (FRMS) is a terminal-based Java application developed for the ITS1033 Object-Oriented Programming coursework. 
It manages vehicle records, customer records, rental agreements, returns, and basic reports for a small vehicle rental company. 
Registered users can log in and access the system's operations.
The implementation follows the coursework's four missions: OOP foundations, core rental operations, background monitoring, and Streams-based reports.
``
---
### **Features**

* Register users, log in, and log out.
* Add Car, Van, and Truck vehicles.
* Add Individual and Corporate customers.
* View available vehicles and search vehicles by fuel type.
* Create rental agreements with vehicle-specific costs and customer discounts.
* Check vehicle availability and corporate credit limits using custom checked exceptions.
* Return a rented vehicle to the available fleet.
* Display revenue, customer, rental-agreement, and combined fleet/customer reports.
* Monitor fleet status and overdue rental agreements using two background daemon threads.
* Print a notification when a new rental agreement is created through a lambda-based event listener.

---
### **Technologies and Requirements**

* Language: Java
* Required JDK: Java 25 or later (as specified by the coursework)
* IDE: IntelliJ IDEA or another Java-compatible IDE
* User interface: Command-line / terminal
* Storage: In-memory Java collections; no database or file persistence
* Package: `lk.ijse.fleet`
---
### **Object-Oriented Programming Concepts**

* Encapsulation	: Private fields with constructors, getters and setters
* Inheritance	: `Car`, `Van`, `Truck` extend `Vehicle`; customer subclasses extend `Customer`
* Abstraction	: Abstract `Vehicle` and `Customer` classes
* Polymorphism	: Overridden `calculateRentalCost()` and `applyDiscount()` methods
* Composition / association	: `RentalAgreement` references a `Vehicle` and a `Customer`
* Enumerations	: `FuelType` (with cost multiplier) and `RentalStatus`
* Interfaces	: `Reportable` implemented by `Vehicle` and `Customer`
* Functional interface / lambda	: `RentalEventListener` callback
* Custom checked exceptions	: `VehicleNotAvailableException`, `InsufficientCreditException`
* Inner class	: `FleetManager.FleetSnapshot`
* Collections and Streams	: Lists, filtering, sorting and revenue aggregation
* Multithreading	: `VehicleStatusMonitor` and `OverdueReminderWorker` daemon threads
* Synchronization	: `FleetManager.printFleetStatus()` and `checkOverdueAgreements()`
* Static members	: Counters for automatically generated IDs

---
### **Project Structure**

* All Java source files are in the `lk.ijse.fleet` package.

```text
src/
└── lk/
    └── ijse/
        └── fleet/
            ├── Main.java
            ├── FleetManager.java
            ├── Vehicle.java
            ├── Car.java
            ├── Van.java
            ├── Truck.java
            ├── Customer.java
            ├── IndividualCustomer.java
            ├── CorporateCustomer.java
            ├── RentalAgreement.java
            ├── SystemUser.java
            ├── UserManager.java
            ├── FuelType.java
            ├── RentalStatus.java
            ├── Reportable.java
            ├── RentalEventListener.java
            ├── VehicleNotAvailableException.java
            ├── InsufficientCreditException.java
            ├── VehicleStatusMonitor.java
            └── OverdueReminderWorker.java
```