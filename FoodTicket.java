import java.util.List; 
/**
 * The FoodTicket class represents a food ticket with a unique ticket ID, a list of items,
 * and the status of payment and preparation. It provides methods to retrieve the ticket ID,
 * items, and status, as well as methods to mark the ticket as paid or prepared.
 */
public class FoodTicket {
    int ticketID;
    List<String> items;
    boolean paymentStatus;
    boolean preparationStatus;
/**
     * Constructs a FoodTicket object with the specified ticket ID and list of items.
     * The payment and preparation statuses are initialized to false.
     *
     * @param ticketID the unique identifier for the food ticket
     * @param items the list of items associated with the food ticket
     */
    public FoodTicket(int ticketID, List<String> items) {
        this.ticketID = ticketID;
        this.items = items;
        this.paymentStatus = false;
        this.preparationStatus = false;
    }
/**
     * Returns the unique identifier of the food ticket.
     *
     * @return the ticket ID of the food ticket
     */
    public int getTicketID() {
        return ticketID;
    }/**
     * Sets the unique identifier of the food ticket.
     *
     * @param ticketID the new ticket ID for the food ticket
     */
    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }
/**
     * Returns the list of items associated with the food ticket.
     *
     * @return the list of items in the food ticket
     */
    public List<String> getItems() {
        return items;
    }
/**
     * Returns the payment status of the food ticket.
     *
     * @return true if the food ticket is paid, false otherwise
     */
    public boolean isPaid() {
        return paymentStatus;
    }
/**
     * Returns the preparation status of the food ticket.
     *
     * @return true if the food ticket is prepared, false otherwise
     */
    public boolean isPrepared() {
        return preparationStatus;
    }
/**
     * Marks the food ticket as paid by setting the payment status to true.
     */
    public void markAsPaid() {
        this.paymentStatus = true;
    }
/**
     * Marks the food ticket as prepared by setting the preparation status to true.
     */
    public void markAsPrepared() {
        this.preparationStatus = true;
    }
}
