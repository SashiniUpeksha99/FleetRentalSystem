package lk.ijse.fleet;

public class OverdueReminderWorker implements Runnable{
    private FleetManager fleetManager;


    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(5000);
                fleetManager.checkOverdueAgreements();

            } catch (Exception e) {
                break;
            }
        }
    }
}
