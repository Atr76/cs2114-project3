/**
 * The Car class represents a car with a unique identifier and an associated ticket ID.
 * It provides methods to retrieve and set the car's ID and ticket ID assocaited with
 * the order, as well as a string representation of the car.
 */
public class Car{
    String carID;
    int ticketID;
/**
     * Constructs a Car object with the specified car ID.
     * The ticket ID is initialized to 0.
     *
     * @param carID the unique identifier for the car
     */
    public Car(String carID){
        this.carID = carID;
        this.ticketID = 0;
    }
/**
 * Returns the unique identifier of the car.
 * @return
 */
    public String getCarId(){
            return carID;
        }
/**
 * Sets the unique identifier of the car.
 * @param carID the unique identifier to set for the car
 * @return
 */
    public void setCarId(String carID){
            this.carID = carID;
        }
/**
 * Returns the ticket ID associated with the car.
 * @return the ticket ID of the order attached to the car
 */
    public int getTicketId(){
            return ticketID;
        }
/**
 * Sets the ticket ID associated with the car.
 * @param ticketID the ticket ID to set for the car 
 */
    public void setTicketId(int ticketID){
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