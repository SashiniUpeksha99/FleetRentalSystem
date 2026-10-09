package lk.ijse.fleet;

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
            System.out.print("===========================================================\n" +
                    "           FLEET RENTAL MANAGEMENT SYSTEM (FRMS)           \n" +
                    "===========================================================\n" +
                    "[1] Login\n[2] Register New User\n[0] Exit\n");
            System.out.print("===========================================================\n");

            int choice = 0 ;
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
                    "===========================================================\n" +
                    "FRMS MAIN MENU | Logged in as : "+loggedInUser.getFullName()+"\n"+
                    "===========================================================\n");

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
                    //"Enter choice > _\n" +
                    "===========================================================\n");

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

            switch (choice){
                case 1 :
                    break;
                case 2 :
                    break;
                case 3 :
                    break;

                case 4 :
                    String vehicleId = "V001";
                    String customerId = "V002";
                    int rentalDays = 10;

                    try{
                        RentalAgreement agreement = fleetManager.createRentalAgreement(vehicleId, customerId, rentalDays);

                        if (agreement != null){
                            System.out.println(agreement);
                        }

                    } catch (VehicleNotAvailableException | InsufficientCreditException e) {
                        System.out.println("Rental failed: "+ e.getMessage());
                    }
                    break;
                case 5 :

                    break;
                case 6 :
                    System.out.println("Coming soon..");
                    break;
                case 7 :
                    System.out.println("Coming soon...");
                    break;
                case 8 :
                    System.out.println("Coming soon....");
                    break;
                case 9 :
                    break;
                case 10 :
                    break;
                case 0 :
                    loggedInUser = null;
                    return;
            }
        }
    }
}
