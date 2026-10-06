package lk.ijse.fleet;

import java.util.InputMismatchException;
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
                    choice = scanner.nextInt();

                    if(choice < 0 || choice >2){
                        System.out.println("Please enter a valid choice.");
                        continue;
                    }
                }catch (InputMismatchException e){
                    System.out.println("Please enter a valid choice.");
                    scanner.nextLine();
                    continue;
                }
                scanner.nextLine();
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
        System.out.print("\nWelcome," +loggedInUser.getFullName()+"! Loading fleet dashboard...\n"+
                         "===========================================================\n" +
                         "FRMS MAIN MENU | Logged in as : "+loggedInUser.getFullName()+"\n"+
                         "===========================================================\n");

    }
}
