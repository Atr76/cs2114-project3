import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void addCar(Car car) {
        carQueue.enqueue(car); 
     }

    public FoodTicket createOrder(String carId, List<String> items) {
        FoodTicket foodTicket = new FoodTicket(nextTicketId, items);
        foodTicket.setTicketID(nextTicketId);
        foodTicketQueue.enqueue(foodTicket);
        nextTicketId++;
        return foodTicket;
}

    public Set<String> getMenu(){
        Set<String> menu = new HashSet<>();
        menu.add("Burger");
        menu.add("Fries");
        menu.add("Soda");
        return menu;
    }

    public void markPaid(int ticketId){
        for (FoodTicket foodTicket : foodTicketQueue.toList()) {
            if (foodTicket.getTicketID() == ticketId) {
                foodTicket.markAsPaid();;
                break;
            }
        }
    }
}
