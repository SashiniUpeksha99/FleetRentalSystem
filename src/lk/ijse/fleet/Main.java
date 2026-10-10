package lk.ijse.fleet;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static FleetManager fleetManager;
    private static UserManager userManager;
    private static Scanner scanner;
    private static SystemUser loggedInUser;

    public static void main(String[] args) {
        run();
    }

    public static void run(){

        fleetManager = new FleetManager();

        fleetManager.setRentalEventListener(agreement ->
                System.out.println("[EVENT] New rental created : "+agreement.getAgreementId())

        );

        Thread statusThread = new Thread(new VehicleStatusMonitor(fleetManager));
        statusThread.setDaemon(true);
        statusThread.start();

        Thread overdueThread = new Thread(new OverdueReminderWorker(fleetManager));
        overdueThread.setDaemon(true);
        overdueThread.start();

        showAuthMenu();
    }

    public static void showAuthMenu(){
        loop1:
        while(true){
            scanner = new Scanner(System.in);
            System.out.print("===================================================================================================\n" +
                             "                               FLEET RENTAL MANAGEMENT SYSTEM (FRMS)                               \n" +
                             "===================================================================================================\n" +
                    "[1] Login\n[2] Register New User\n[0] Exit\n");
            System.out.print("===================================================================================================\n");

            int choice ;
            while(true){
                try{
                    System.out.print("Enter choice > ");
                    choice = Integer.parseInt(scanner.nextLine().trim());

                    if(choice < 0 || choice >2){
                        System.out.println("Please enter a valid choice.");
                        continue;
                    }
                }catch (NumberFormatException e){
                    System.out.println("Please enter a valid choice.");
                    continue;
                }
                break;
            }
            String userName;
            String password;
            String fullName;
            userManager = new UserManager();
            SystemUser obj;

            switch (choice){
                case 1 :
                    System.out.print("User Name : ");
                    userName = scanner.nextLine().trim();

                    System.out.print("Password : ");
                    password = scanner.nextLine().trim();

                    obj = userManager.login(userName, password);

                    if (obj == null){
                        System.out.println("Invalid username or password!");
                        continue loop1;
                    }
                    loggedInUser = obj;
                    showMainMenu();
                    break;

                case 2 :
                    loop2:
                    while(true) {
                        System.out.print("User Name : ");
                        userName = scanner.nextLine().trim();

                        if (userName.matches(".*[A-Za-z].*") && userName.matches("[A-Za-z0-9_]+")) {

                            loop3:
                            while(true){
                                System.out.print("Password : ");
                                password = scanner.nextLine().trim();

                                if (password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*")
                                        && !password.contains(" ")) {

                                    loop4:
                                    while(true){
                                        System.out.print("Full Name : ");
                                        fullName = scanner.nextLine().trim();

                                        if (fullName.matches("[A-Za-z '-]+")) {

                                            SystemUser user = userManager.registerUser(userName, password, fullName);
                                            continue loop1;
                                        }
                                        System.out.println("Full Name must contain only letters and spaces.");
                                        continue loop4;
                                    }
                                }
                                System.out.println("Password must include letters and numbers.");
                                continue loop3;
                            }
                        }
                        System.out.println("Username must contain letters and can include numbers or underscore.");
                        continue loop2;
                    }
                case 0 :
                    System.out.println("Exiting system...");
                    return;
            }
        }

    }

    public static void showMainMenu(){

        while(true){

            System.out.print("\nWelcome," +loggedInUser.getFullName()+"! Loading fleet dashboard...\n"+
                    "===================================================================================================\n" +
                    "FRMS MAIN MENU | Logged in as : "+loggedInUser.getFullName()+"\n"+
                    "===================================================================================================\n");

            System.out.println("[1] Add Vehicle\n" +
                    "[2] Add Customer\n" +
                    "[3] View Available Vehicles\n" +
                    "[4] Create Rental Agreement\n" +
                    "[5] Return Vehicle\n" +
                    "[6] View Revenue Report\n" +
                    "[7] View Vehicles by Fuel Type\n" +
                    "[8] View Combined Fleet & Customer Report\n" +
                    "[9] View All Customers\n" +
                    "[10] View All Rental Agreements\n" +
                    "[0] Logout\n" +
                    "===================================================================================================\n");

            int choice = 0 ;
            while(true){
                try{
                    System.out.print("Enter choice > ");
                    choice = Integer.parseInt(scanner.nextLine().trim());

                    if(choice < 0 || choice >10){
                        System.out.println("Please enter a valid choice.");
                        continue;
                    }
                }catch (NumberFormatException e){
                    System.out.println("Please enter a valid choice.");
                    continue;
                }
                break;
            }

            if (choice == 0) {
                System.out.println("Logging Out.");
                loggedInUser = null;
                return;
            }

            do {
                switch (choice) {
                    case 1 -> addVehicle();
                    case 2 -> addCustomer();
                    case 3 -> viewAvailableVehicles();
                    case 4 -> createRentalAgreement();
                    case 5 -> returnVehicle();
                    case 6 -> fleetRevenueReport();
                    case 7 -> searchVehiclesByFuelType();
                    case 8 -> viewCombinedFleetCustomerReport();
                    case 9 -> viewAllCustomers();
                    case 10 -> viewAllRentalAgreements();
                }
            } while (continueOperation());
        }
    }

    public static void addVehicle(){
        System.out.print("===================================================================================================\n" +
                         "                                            ADD VEHICLE                                            \n" +
                         "===================================================================================================\n");

        System.out.println("Select Vehicle Type :\n[1] Car\n[2] Van\n[3] Truck");

        int vehicleType;
        while(true){
            try{
                System.out.print("Enter choice > ");
                vehicleType = Integer.parseInt(scanner.nextLine().trim());

                if(vehicleType < 1 || vehicleType >3){
                    System.out.println("Please enter a valid choice.");
                    continue;
                }
            }catch (NumberFormatException e){
                System.out.println("Please enter a valid choice.");
                continue;
            }
            break;
        }

        System.out.print("\nRegistration Number : ");
        String registrationNumber = scanner.nextLine().trim();

        System.out.print("Brand : ");
        String brand = scanner.nextLine().trim();
        System.out.print("Model : ");
        String model = scanner.nextLine().trim();

        double dailyRate;
        while(true){
            try{
                System.out.print("Daily Rate : ");
                dailyRate = Double.parseDouble(scanner.nextLine().trim());

            }catch (NumberFormatException e){
                System.out.println("Invalid Daily Rate!");
                continue;
            }
            break;
        }

        System.out.print("\nSelect Fuel Type :\n[1] PETROL\n[2] DIESEL\n[3] ELECTRIC\n[4] HYBRID\n");

        int selectFuelType1;
        while(true){
            try{
                System.out.print("Enter choice > ");
                selectFuelType1 = Integer.parseInt(scanner.nextLine().trim());

                if(selectFuelType1 < 1 || selectFuelType1 >4){
                    System.out.println("Please enter a valid choice.");
                    continue;
                }
            }catch (NumberFormatException e){
                System.out.println("Please enter a valid choice.");
                continue;
            }
            break;
        }

        FuelType selectFUelType = null;

        if(selectFuelType1 == 1 ){
            selectFUelType = FuelType.PETROL;
        }else if(selectFuelType1 == 2 ){
            selectFUelType = FuelType.DIESEL;
        }else if(selectFuelType1 == 3 ){
            selectFUelType = FuelType.ELECTRIC;
        }else if(selectFuelType1 == 4 ){
            selectFUelType = FuelType.HYBRID;
        }

        Vehicle newvehicle = null ;

        switch (vehicleType){
            case 1:
                newvehicle = new Car(registrationNumber,brand,model,dailyRate,selectFUelType);
                break;

            case 2 :

                int passengerCapacity;
                while(true){
                    try{
                        System.out.print("\nPassenger Capacity : ");
                        passengerCapacity = Integer.parseInt(scanner.nextLine().trim());

                    }catch (NumberFormatException e){
                        System.out.println("Invalid Passenger Capacity!");
                        continue;
                    }
                    break;
                }

                newvehicle = new Van(registrationNumber,brand,model,dailyRate,selectFUelType,passengerCapacity);

                break;

            case 3 :

                double cargoCapacityTons;
                while(true){
                    try{
                        System.out.print("\nCargo Capacity Tons : ");
                        cargoCapacityTons = Double.parseDouble(scanner.nextLine().trim());

                    }catch (NumberFormatException e){
                        System.out.println("Invalid Cargo Capacity!");
                        continue;
                    }
                    break;
                }
                newvehicle = new Truck(registrationNumber,brand,model,dailyRate,selectFUelType,cargoCapacityTons);
                break;
        }

        fleetManager.addVehicle(newvehicle);
        System.out.println("\nVehicle added successfully!");
    }

    public static void addCustomer(){
        System.out.print("===================================================================================================\n" +
                         "                                           ADD CUSTOMER                                            \n" +
                "===================================================================================================\n");

        System.out.println("Select Customer Type :\n[1] Individual Customer\n[2] Corporate Customer");

        int customerType;
        while(true){
            try{
                System.out.print("Enter choice > ");
                customerType = Integer.parseInt(scanner.nextLine().trim());

                if(customerType < 1 || customerType >2){
                    System.out.println("Please enter a valid choice.");
                    continue;
                }
            }catch (NumberFormatException e){
                System.out.println("Please enter a valid choice.");
                continue;
            }
            break;
        }

        String fullName;
        while(true){
            System.out.print("\nFull Name : ");
            fullName = scanner.nextLine().trim();

            if(!fullName.matches("[A-Za-z '-]+")){
                System.out.println("Please enter a valid Full Name!");
                continue;
            }
            break;
        }

        String contactNumber;
        while(true){
            System.out.print("Contact Number : ");
            contactNumber = scanner.nextLine().trim();

            if (!contactNumber.matches("07[0-9]{8}")) {

                System.out.println("Invalid contact number!");
                continue;
            }
            break;
        }

        Customer newCustomer = null ;

        switch (customerType){

            case 1:

                int loyaltyPoints;
                while (true) {
                    try {
                        System.out.print("Loyalty Points : ");
                        loyaltyPoints = Integer.parseInt(scanner.nextLine().trim());

                        if (loyaltyPoints < 0) {
                            System.out.println("Invalid loyalty points! Value cannot be negative.");
                            continue;
                        }
                        break;

                    }catch(NumberFormatException e) {
                        System.out.println("Invalid loyalty points!");
                    }
                }
                newCustomer = new IndividualCustomer(fullName,contactNumber,loyaltyPoints);
                break;

            case 2 :

                double creditLimit;
                while (true) {
                    try {
                        System.out.print("Credit Limit : ");
                        creditLimit = Double.parseDouble(scanner.nextLine().trim());

                        if (creditLimit <= 0) {
                            System.out.println("Invalid credit limit!");
                            continue;
                        }
                        break;

                    }catch(NumberFormatException e) {
                        System.out.println("Invalid loyalty points!");
                    }
                }
                newCustomer = new CorporateCustomer(fullName,contactNumber,creditLimit);
        }

        fleetManager.addCustomer(newCustomer);
        System.out.println("\nCustomer added successfully!");

    }

    public static void viewAvailableVehicles(){

        System.out.print("===================================================================================================\n" +
                           "                                      VIEW AVAILABLE VEHICLES                                      \n" +
                           "===================================================================================================\n");

        List<Vehicle> availableVehicles = fleetManager.getAvailableVehicles();

        if(availableVehicles.isEmpty()){
            System.out.println("No Available Vehicles Found!");
            return;
        }

        System.out.println("+------------+--------------+------------------+------------+------------+------------+-----------+");
        System.out.printf("| %-10s | %-12s | %-16s | %-10s | %-10s | %-10s | %-9s |%n",
                "Vehicle ID",
                "Vehicle Type",
                "Registration No.",
                "Brand",
                "Model",
                "Daily Rate",
                "Fuel Type");
        System.out.println("+------------+--------------+------------------+------------+------------+------------+-----------+");

        for (Vehicle vehicle : availableVehicles) {
            System.out.printf("| %-10s | %-12s | %-16s | %-10s | %-10s | %10.2f | %-9s |%n",
                    vehicle.getVehicleId(),
                    vehicle.getClass().getSimpleName().toUpperCase(),
                    vehicle.getRegistrationNumber(),
                    vehicle.getBrand(),
                    vehicle.getModel(),
                    vehicle.getDailyRate(),
                    vehicle.getFuelType());
        }
        System.out.println("+------------+--------------+------------------+------------+------------+------------+-----------+");

        System.out.println("\nTotal Available Vehicles : " + availableVehicles.size());

    }

    public static void createRentalAgreement() {
        System.out.print("===================================================================================================\n" +
                         "                                      CREATE RENTAL AGREEMENT                                      \n" +
                         "===================================================================================================\n");

        System.out.print("Vehicle ID : ");
        String vehicleId = scanner.nextLine().trim();
        System.out.print("Customer ID : ");
        String customerId = scanner.nextLine().trim();

        int rentalDays;
        loop1:
        while(true) {
            try {
                System.out.print("Rental Days : ");
                rentalDays = Integer.parseInt(scanner.nextLine().trim());

                if (rentalDays <= 0) {
                    System.out.println("Please enter a valid day count.!");
                    continue loop1;
                }
                break;

            }catch (NumberFormatException e) {
                System.out.println("Please enter a valid day count.");
                continue loop1;
            }
        }

        try{
            RentalAgreement agreement = fleetManager.createRentalAgreement(vehicleId, customerId, rentalDays);

            if (agreement != null){
                System.out.println(agreement);
            }

        } catch (VehicleNotAvailableException | InsufficientCreditException e) {
            System.out.println("Rental failed: "+ e.getMessage());
        }
    }

    public static void returnVehicle (){
        System.out.print("===================================================================================================\n" +
                         "                                          RETURN VEHICLE                                           \n" +
                "===================================================================================================\n");

        System.out.print("Enter Agreement ID : ");
        String agreementId = scanner.nextLine().trim();

        fleetManager.returnVehicle(agreementId);

    }

    public static void fleetRevenueReport (){
        System.out.print("===================================================================================================\n" +
                         "                                        FLEET REVENUE REPORT                                       \n" +
                "===================================================================================================\n"+
                "Total Revenue : "+fleetManager.generateRevenueReport()+
                "\n===================================================================================================\n");
    }

    public static void searchVehiclesByFuelType(){

        scanner = new Scanner(System.in);
        System.out.print("===================================================================================================\n" +
                         "                                   SEARCH VEHICLES BY FUEL TYPE                                    \n" +
                         "===================================================================================================\n");

        FuelType fuelType;

        while(true){
            System.out.print("Enter Fuel Type : ");
            String inputFuelTYpe = scanner.nextLine().trim().toUpperCase();

            try{
                fuelType = FuelType.valueOf(inputFuelTYpe);
                break;
            }catch(IllegalArgumentException e){
                System.out.println("Invalid Fuel Type. Try Again!");
                continue;
            }
        }

        List<Vehicle> selectVehiclesByFuelType = fleetManager.getVehiclesByFuelType(fuelType);

        System.out.println("+-------------+---------------------+---------------+---------------+---------------+---------------+");
        System.out.printf("| %-11s | %-19s | %-13s | %-13s | %-13s | %-13s |%n",
                "Vehicle ID",
                "Registration Number",
                "Brand",
                "Model",
                "Daily Rate",
                "Rental Status");
        System.out.println("+-------------+---------------------+---------------+---------------+---------------+---------------+");

        for (Vehicle vehicle : selectVehiclesByFuelType) {
            System.out.printf("| %-11s | %-19s | %-13s | %-13s | %-13.2f | %-13s |%n",
                    vehicle.getVehicleId(),
                    vehicle.getRegistrationNumber(),
                    vehicle.getBrand(),
                    vehicle.getModel(),
                    vehicle.getDailyRate(),
                    vehicle.getStatus());
        }
        System.out.println("+-------------+---------------------+---------------+---------------+---------------+---------------+");

    }

    public static void viewCombinedFleetCustomerReport (){
        System.out.print("===================================================================================================\n" +
                         "                               VIEW COMBINED FLEET & CUSTOMER REPORT                               \n" +
                "===================================================================================================\n");

        fleetManager.printAllReportables();
    }

    public static void viewAllCustomers(){
        System.out.print("===========================================================================================================================\n" +
                         "                                                    VIEW ALL CUSTOMERS                                                     \n" +
                         "===========================================================================================================================\n");

        List<Customer> allCustomers = fleetManager.getAllCustomers();

        if(allCustomers.isEmpty()){
            System.out.println("No Customers Found!");
            return;
        }

        System.out.println("+-------------+----------------------+--------------+----------------------+----------------+---------------+---------------+");
        System.out.printf("| %-10s |     %-16s | %-12s |     %-13s    | %-11s | %-13s | %-13s |%n",
                "Customer ID",
                "Customer Name",
                "Contact No.",
                "Customer Type",
                "Loyalty Points",
                "Credit Limit",
                "Outstanding");
        System.out.println("+-------------+----------------------+--------------+----------------------+----------------+---------------+---------------+");

        for (Customer customer : allCustomers) {

            String loyaltyPoints = " - ";
            String creditLimit = " - ";
            String outstanding = " - ";

            if (customer instanceof IndividualCustomer c) {

                loyaltyPoints = String.valueOf(c.getLoyaltyPoints());

            } else if (customer instanceof CorporateCustomer c) {

                creditLimit = String.format("%,.2f", c.getCreditLimit());
                outstanding = String.format("%,.2f",c.getOutstandingBalance());
            }

            System.out.printf("| %-10s  |    %-17s | %-12s | %-19s  | %-14s | %-13s | %-13s |%n",
                    customer.getCustomerId(),
                    customer.getFullName(),
                    customer.getContactNumber(),
                    customer.getClass().getSimpleName(),
                    loyaltyPoints,
                    creditLimit,
                    outstanding);
        }

        System.out.println("+-------------+----------------------+--------------+----------------------+----------------+---------------+---------------+");


        System.out.println("\nTotal Customers : " + allCustomers.size());
    }

    public static void viewAllRentalAgreements(){
        System.out.print("===================================================================================================\n" +
                         "                                    VIEW ALL RENTAL AGREEMENTS                                     \n" +
                         "===================================================================================================\n");

        System.out.println("+--------------+------------+-------------+-------------+---------------+--------------+");
        System.out.printf("| %-12s | %-10s | %-11s | %-11s | %-13s | %-10s |%n",
                "Agreement ID",
                "Vehicle ID",
                "Customer ID",
                "Rental Days",
                "Total Cost",
                "Days Elapsed");
        System.out.println("+--------------+------------+-------------+-------------+---------------+--------------+");

        List<RentalAgreement> allAgreements = fleetManager.getAllAgreements();

        for (RentalAgreement agreement : allAgreements) {
            System.out.printf("| %-12s | %-10s | %-11s | %11d | %,13.2f | %-10s |%n",
                    agreement.getAgreementId(),
                    agreement.getVehicle().getVehicleId(),
                    agreement.getCustomer().getCustomerId(),
                    agreement.getRentalDays(),
                    agreement.getTotalCost(),
                    agreement.getDaysElapsed());
        }
        System.out.println("+--------------+------------+-------------+-------------+---------------+---------9" +
                "-----+");

        System.out.println("\nTotal Rental Agreements : " + allAgreements.size());
    }

    public static boolean continueOperation() {

        while (true) {
            System.out.print("\nDo You Want to Continue? (Y/N): ");
            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("Y")) {

                return true;

            } else if (choice.equalsIgnoreCase("N")) {

                System.out.println("Returning to Main Menu...");
                return false;

            } else {
                System.out.println("Invalid Choice! Please Enter (Y/N)?");
            }
        }
    }
}
