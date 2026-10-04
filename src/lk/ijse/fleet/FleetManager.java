package lk.ijse.fleet;

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
        for(Vehicle v :vehicles){
            if(v.getVehicleId().equalsIgnoreCase(id)){
                return v;
            }
        }
        return null;
    }

    public Customer findCustomerById(String id){
        for(Customer c :customers){
            if(c.getCustomerId().equalsIgnoreCase(id)){
                return c;
            }
        }
        return null;
    }

    public void setRentalEventListener(RentalEventListener rentalEventListener){
        this.rentalEventListener = rentalEventListener;
    }

    public List<Vehicle> getAvailableVehicles(){

        return null;
    }

    public RentalAgreement createRentalAgreement(String vehicleId, String customerId, int rentalDays) throws VehicleNotAvailableException,InsufficientCreditException{

        return null;
    }

    public void returnVehicle(String agreementId){

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
