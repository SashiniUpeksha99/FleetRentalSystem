package lk.ijse.fleet;

public class RentalAgreement {
    private static int counter = 1001;

    private String agreementId;
    private Vehicle vehicle;
    private Customer customer;
    private int rentalDays;
    private double totalCost;
    private int daysElapsed = 0 ;

    public RentalAgreement(Vehicle vehicle, Customer customer, int rentalDays) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentalDays = rentalDays;

        double subtotal = vehicle.calculateRentalCost(rentalDays);
        totalCost = customer.applyDiscount(subtotal);

        agreementId = String.format("AGR-%03d", counter);
        counter++;

        daysElapsed = 0;

    }

    public static int getCounter() {
        return counter;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public int getDaysElapsed() {
        return daysElapsed;
    }

    public void setDaysElapsed(int daysElapsed) {
        this.daysElapsed = daysElapsed;
    }

    @Override
    public String toString() {
        return "RentalAgreement{" +
                "agreementId='" + agreementId + '\'' +
                ", vehicle=" + vehicle +
                ", customer=" + customer +
                ", rentalDays=" + rentalDays +
                ", totalCost=" + totalCost +
                ", daysElapsed=" + daysElapsed +
                '}';
    }
}
