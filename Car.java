/**
 * The Car class represents a car with a unique identifier and an associated ticket ID.
 * It provides methods to retrieve and set the car's ID and ticket ID assocaited with
 * the order, as well as a string representation of the car.
 */
public class Car{
    String carID;
    Integer ticketID;
/**
     * Constructs a Car object with the specified car ID.
     * The ticket ID is initialized to null.
     *
     * @param carID the unique identifier for the car
     */
    public Car(String carID){
        if(carID == null || carID.isEmpty()) {
            throw new IllegalArgumentException("Car ID cannot be null or empty");
        }
        this.carID = carID;
        this.ticketID = null;
    }
/**
 * Returns the unique identifier of the car.
 * @return the unique identifier of the car
 */
    public String getCarId(){
            return carID;
        }

/**
 * Returns the ticket ID associated with the car.
 * @return the ticket ID of the order attached to the car
 */
    public Integer getTicketId(){
            return ticketID;
        }
/**
 * Sets the ticket ID associated with the car.
 * @param ticketID the ticket ID to set for the car 
 */
    public void setTicketId(Integer ticketID){
            if(this.ticketID != null){
                throw new IllegalArgumentException("Car already has Ticket Id");
            } 
            this.ticketID = ticketID;
        }
/**(non-Javadoc)
 * Returns a string representation of the car, including its ID and ticket ID.
 * @return a string representation of the car
 * @see java.lang.Object#toString()
 */
    public String toString(){
            return "Car ID: " + carID + ", Ticket ID: " + ticketID;
        }
 }