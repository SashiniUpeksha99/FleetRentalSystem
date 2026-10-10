package lk.ijse.fleet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FleetManager {
    private List<Vehicle> vehicles = new ArrayList<Vehicle>();
    private List<Customer> customers = new ArrayList<Customer>();
    private List<RentalAgreement> agreements = new ArrayList<RentalAgreement>();
    private RentalEventListener rentalEventListener;


    public void addVehicle(Vehicle c){
        vehicles.add(c);
    }

    public void addCustomer(Customer c){
        customers.add(c);
    }

    public Vehicle findVehicleById(String id){
        try{
            for(Vehicle v :vehicles){
                if(v.getVehicleId().equalsIgnoreCase(id)){
                    return v;
                }
            }
        }catch(NullPointerException e){
            //System.out.println("No vehicles have been added yet");
        }
        return null;
    }

    public Customer findCustomerById(String id){
        try{
            for(Customer c :customers){
                if(c.getCustomerId().equalsIgnoreCase(id)){
                    return c;
                }
            }
        }catch (NullPointerException e){
            //System.out.println("No customers have been added yet");
        }
        return null;
    }

    public void setRentalEventListener(RentalEventListener rentalEventListener){
        this.rentalEventListener = rentalEventListener;
    }

    public List<Vehicle> getAvailableVehicles(){

        List<Vehicle> availableVehicles;

        availableVehicles = vehicles.stream()
                .filter(v -> v.getStatus() == RentalStatus.AVAILABLE)
                .collect(Collectors.toList());

        return availableVehicles;
    }

    public RentalAgreement createRentalAgreement(String vehicleId, String customerId, int rentalDays) throws VehicleNotAvailableException,InsufficientCreditException{

        Vehicle vehicleById = findVehicleById(vehicleId);

        if(vehicleById == null){
            System.out.println("No vehicle found with this ID");
            return null;
        }

        if(!vehicleById.getStatus().equals(RentalStatus.AVAILABLE)){
            throw new VehicleNotAvailableException("...");
        }

        Customer customerById = findCustomerById(customerId);
        if(customerById == null){
            System.out.println("No Customer found with this ID");
            return null;
        }
        double subtotal = vehicleById.calculateRentalCost(rentalDays);
        double totalCost = customerById.applyDiscount(subtotal);

        if(customerById instanceof CorporateCustomer &&
                !((CorporateCustomer) customerById).hasSufficientCredit(totalCost)){
            throw new InsufficientCreditException("...");

        }

        RentalAgreement agreement = new RentalAgreement(vehicleById, customerById, rentalDays);
        vehicleById.setStatus(RentalStatus.RENTED);

        agreements.add(agreement);

        if(rentalEventListener != null){
            rentalEventListener.onRentalCreated(agreement);
        }
        return agreement;
    }

    public void returnVehicle(String agreementId){
        for (RentalAgreement agreement :  agreements ){
            if (agreement.getAgreementId().equalsIgnoreCase(agreementId)) {
                agreement.getVehicle().setStatus(RentalStatus.AVAILABLE);

                System.out.println("Rental agreement "+agreementId+"completed. Vehicle is now Available.");
                break;
            }
        }
        System.out.println("No rental agreement found with id : "+agreementId);
    }

    public FleetSnapshot generateSnapshot(){
        return new FleetSnapshot();
    }

    public synchronized void printFleetStatus(){
        generateSnapshot().printSummary();
    }

    public synchronized void  checkOverdueAgreements(){
        for (RentalAgreement agreement : agreements){

            agreement.setDaysElapsed(agreement.getDaysElapsed()+1);

            if(agreement.getDaysElapsed() > agreement.getRentalDays()){
                System.out.println("[OVERDUE ALERT] Agreement "+agreement.getAgreementId()+
                        " has exceeded it's "+agreement.getRentalDays()+"-day rental period.");
            }
        }
    }

    public List<Vehicle> getVehiclesByFuelType(FuelType type){

        return vehicles.stream()
                .filter(v -> v.getFuelType() == type)
                .collect(Collectors.toList());
    }

    public List<Customer> getCustomersSortedByName(){

        return customers.stream()
                .sorted(Comparator.comparing(c -> c.getFullName()))
                .collect(Collectors.toList());

    }

    public double generateRevenueReport(){
        return agreements.stream()
                .mapToDouble(a -> a.getTotalCost())
                .sum();
    }

    public void printAllReportables(){

        List<Reportable> reportables = new ArrayList<>();
        reportables.addAll(vehicles);
        reportables.addAll(customers);

        for (Reportable item : reportables){
            System.out.println(item.getReportCategory() +" : "+item.getSummary());
        }
    }

    public List<Customer> getAllCustomers(){
        return customers;
    }

    public List<RentalAgreement> getAllAgreements(){
        return agreements;
    }


    public class FleetSnapshot{
        private int availableCount;
        private int rentedCount;
        private double totalRevenue;

        public FleetSnapshot() {
            try {
                for (Vehicle vehicle : vehicles) {
                    if (vehicle.getStatus() == RentalStatus.AVAILABLE) {
                        availableCount++;
                    } else {
                        rentedCount++;
                    }
                }
            }catch (NullPointerException e){

            }

            for (RentalAgreement agreement : agreements){
                totalRevenue += agreement.getTotalCost();
            }
        }

        public void printSummary(){
            System.out.println("\n[FLEET STATUS] Available:"+availableCount+" Rented:"+rentedCount+" Total Revenue So Far:"+totalRevenue);
        }
    }

}
