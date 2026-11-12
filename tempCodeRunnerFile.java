import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Represents a drive-thru system that manages a queue of customers.
 */
public class DriveThruSystem {
    private DriveThruQueue<Car> carQueue;
    private DriveThruQueue<FoodTicket> foodTicketQueue;
    private int nextTicketId;
    /**
     * Constructs a DriveThruSystem object with an empty car queue, an empty food ticket queue,
     * and initializes the next ticket ID to 1.
     */
    public DriveThruSystem() {
        carQueue = new DriveThruQueue<>();
        foodTicketQueue = new DriveThruQueue<>();
        nextTicketId =1;
    }

    public void addCar(Car car) {
        if(car == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        if (carQueue.contains(car)) {
            throw new IllegalArgumentException("Car with ID " + car.getCarId() + " already exists");
        }
        carQueue.enqueue(car);
    }

    public FoodTicket createOrder(String carId, List<String> items) {
        FoodTicket foodTicket = new FoodTicket(nextTicketId, items);
        foodTicket.setTicketId(nextTicketId);
        Car car = findCar(carId);
        car.setTicketId(nextTicketId);
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
        FoodTicket foodTicket = findFoodTicket(ticketId);
        if (foodTicket != null) {
            foodTicket.markAsPaid();
        }
    }

    public void markPrepared(int ticketId){
        FoodTicket foodTicket = findFoodTicket(ticketId);
        if (foodTicket != null) {
            foodTicket.markAsPrepared();
        }
    }

    public Car deliverOrder(){
        if(foodTicketQueue.peek().isPaid() && foodTicketQueue.peek().isPrepared()){
            FoodTicket foodTicket = foodTicketQueue.dequeue();
            Car car = carQueue.dequeue();
            return car ;
        }
        return null;
    }
    
    public List<Car> getCarSnapshot(){
        return carQueue.toList();
    }

    public List<FoodTicket> getTicketSnapshot(){
        return foodTicketQueue.toList();
    }

    private Car findCar(String carId){
        for (Car car : carQueue.toList()) {
            if (car.getCarId().equals(carId)) {
                return car;
            }
        }
        throw new NoSuchElementException("Car with ID " + carId + " not found");
    }

    private FoodTicket findFoodTicket(int ticketId){
        for (FoodTicket foodTicket : foodTicketQueue.toList()) {
            if (foodTicket.getTicketId() == ticketId) {
                return foodTicket;
            }
        }
        throw new NoSuchElementException("FoodTicket with ID " + ticketId + " not found");
    }

    public void options(){
        System.out.println("Available options:");
        System.out.println("add");
        System.out.println("order");
        System.out.println("pay");
        System.out.println("prep");
        System.out.println("deliver");
    }
    public static void main(String[] args) {
        DriveThruSystem system = new DriveThruSystem();
        DriveThruView view = new DriveThruView();
        DriveThruController controller = new DriveThruController(system, view);
        system.options();
        InputHandler inputHandler = new InputHandler(controller);
        inputHandler.readInput();
    }
}