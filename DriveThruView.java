import java.util.List;

public class DriveThruView {

    public void render(List<Car> cars, List<FoodTicket> tickets) {
        if (cars == null) {
            throw new IllegalArgumentException("cars is null");
        }
        if (tickets == null) {
            throw new IllegalArgumentException("tickets is null");
        }
        Car window1 = null;
        for (Car car : cars) {
            if (car.getTicketId() == null) {
                window1 = car;
                break;
            }
        }
        Car window2 = null;
        if (!cars.isEmpty()) {
            window2 = cars.get(0);
        }
        if (window1 == null) {
            System.out.println("Window 1: No cars waiting to order");
        }
        else {
            System.out.println("Window 1: car " + window1.getCarId() + " is ordering");
        }
        if (window2 == null) {
            System.out.println("Window 2: No cars waiting");
        }
        else {
            System.out.println("Window 2: car " + window2.getCarId());
            if (window2.getTicketId() == null) {
                System.out.println("No ticket");
            }
            else {
                for (FoodTicket ticket : tickets) {
                    if (ticket.getTicketId() == window2.getTicketId())
                        System.out.println("paid: " + ticket.isPaid() + " prepared: " + ticket.isPrepared());
                        break;
                    }
                }
        }
    }

    public void showError(String message) {
            System.out.println("Error: " + message);
    }
    public void showMessage(String message) {
            System.out.println(message);
    }
}
