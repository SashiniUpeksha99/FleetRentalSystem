package lk.ijse.fleet;

import java.util.ArrayList;
import java.util.List;

public class FleetManager {
    private List<Vehicle> vehicles;
    private List<Customer> customers;
    private List<RentalAgreement> agreements;
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
        List<Vehicle> availableVehicles = new ArrayList<Vehicle>();
        for (Vehicle vehicle : vehicles){
            if (vehicle.getStatus().equals(RentalStatus.AVAILABLE)){
                availableVehicles.add(vehicle);
            }
        }
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

    }

    public List<Vehicle> getVehiclesByFuelType(FuelType type){

        return null;
    }

    public List<Customer> getCustomersSortedByName(){
        return null;
    }

    public double generateRevenueReport(){
        return 0;
    }

    public void printAllReportables(){

    }

    public List<Customer> getAllCustomers(){
        return null;
    }

    public List<RentalAgreement> getAllAgreements(){
        return null;
    }


    public class FleetSnapshot{
        private int availableCount;
        private int rentedCount;
        private double totalRevenue;

        public FleetSnapshot() {

        }

        public void printSummary(){

        }
    }

}
