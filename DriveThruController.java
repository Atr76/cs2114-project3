package drivethru;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * The DriveThruController mediates between the DriveThruView
 * and DriveThruSystem. It receives user requests, translates
 * them into model operations, handles errors, and refreshes
 * the view after successful state changes.
 */
public class DriveThruController {

    private DriveThruView view;
    private DriveThruSystem system;

    /**
     * Creates a controller with the given system and view.
     * @param system the drive-thru system
     * @param view the drive-thru view
     */
    public DriveThruController(DriveThruSystem system,
                               DriveThruView view) {
        if (system == null || view == null) {
            throw new IllegalArgumentException(
                "System and view cannot be null."
            );
        }

        this.system = system;
        this.view = view;
    }

    /**
     * Creates a car and adds it to the drive-thru system.
     * @param carId the ID of the car
     */
    public void addCar(String carId) {
        try {
            Car car = new Car(carId);
            system.addCar(car);
            refresh();

        } catch (IllegalArgumentException e) {
            view.showError("Invalid or duplicate car ID.");
        }
    }

    /**
     * Creates an order for the specified car.
     * @param carId the ID of the car placing the order
     * @param items the ordered food items
     */
    public void enterOrder(String carId, List<String> items) {
        try {
            FoodTicket ticket = system.createOrder(carId, items);

            view.showMessage(
                "Order created successfully. Ticket ID: "
                + ticket.getTicketId()
            );

            refresh();

        } catch (IllegalStateException | IllegalArgumentException
                 | NoSuchElementException e) {

            view.showError("No ticket is created.");
            view.showMessage("Available menu: " + system.getMenu());
        }
    }

    /**
     * Marks the specified ticket as paid.
     * @param ticketId the ID of the ticket
     */
    public void markPaid(int ticketId) {
        try {
            system.markPaid(ticketId);
            refresh();

        } catch (NoSuchElementException e) {
            view.showError("Ticket not found.");
        }
    }

    /**
     * Marks the specified ticket as prepared.
     * @param ticketId the ID of the ticket
     */
    public void markPrepared(int ticketId) {
        try {
            system.markPrepared(ticketId);
            refresh();

        } catch (NoSuchElementException e) {
            view.showError("Ticket not found.");
        }
    }

    /**
     * Delivers the front car if its ticket is paid and prepared.
     */
    public void deliver() {
        try {
            Car car = system.deliverOrder();

            view.showMessage("Delivered car #" + car.getCarId());
            refresh();

        } catch (IllegalStateException e) {
            view.showError(
                "There are no orders or the order is not ready. "
                + "It must be paid and prepared."
            );
        }
    }

    /**
     * Refreshes the view with the latest model information.
     */
    private void refresh() {
        view.render(
            system.getCarSnapshot(),
            system.getTicketSnapshot()
        );
    }

    /**
     * Displays an error message through the view.
     * @param message the error message
     */
    public void showError(String message) {
        view.showError(message);
    }

    /**
     * Main method to initialize and start the program.
     */
    public static void main(String[] args) {
        DriveThruSystem system = new DriveThruSystem();
        DriveThruView view = new DriveThruView();

        DriveThruController controller =
            new DriveThruController(system, view);

        InputHandler inputHandler = new InputHandler(controller);
        inputHandler.readInput();
    }
}