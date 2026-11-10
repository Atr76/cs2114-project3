/**
 * Represents a drive-thru system that manages a queue of customers.
 */
public class DriveThruSystem {
    private DriveThruQueue<Car> carQueue;
    private DriveThruQueue<FoodTicket> foodTicketQueue;
    private int nextTicketId;

    public DriveThruSystem() {
        carQueue = new DriveThruQueue<>();
        foodTicketQueue = new DriveThruQueue<>();
        nextTicketId =1;
    }
}
