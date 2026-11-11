import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Handles terminal/CLI input parsing for DriveThruDash (Team 42).
 * Captures user actions, validates syntactical structure and data types,
 * and delegates commands to the DriveThruController.
 */
public class InputHandler {

    private final DriveThruController controller;
    private final Scanner scanner;

    /**
     * Constructs an InputHandler reading from standard System.in.
     *
     * @param controller 
     * @throws IllegalArgumentException
     */
    public InputHandler(DriveThruController controller) {
        this(controller, System.in);
    }

    /**
     * Overloaded constructor supporting custom InputStreams (useful for unit testing).
     *
     * @param controller 
     * @param in 
     * @throws IllegalArgumentException
     */
    public InputHandler(DriveThruController controller, InputStream in) {
        if (controller == null) {
            throw new IllegalArgumentException("Controller cannot be null.");
        }
        if (in == null) {
            throw new IllegalArgumentException("InputStream cannot be null.");
        }
        this.controller = controller;
        this.scanner = new Scanner(in);
    }

    /**
     * Continuously reads, tokenizes, and delegates user commands until the user inputs 'quit'.
     * Malformed commands or non-numeric IDs are caught here and reported via controller.showError.
     */
    public void readInput() {
        boolean running = true;

        while (running && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            // Exit condition
            if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                running = false;
                break;
            }

            // Split command by whitespace
            String[] tokens = line.split("\\s+");
            String command = tokens[0].toLowerCase();

            try {
                switch (command) {
                    case "add":
                    case "addcar":
                    case "newcar":
                    case "car":
                        handleAddCar(tokens);
                        break;

                    case "order":
                    case "enterorder":
                    case "makeorder":
                    case "neworder":
                        handleEnterOrder(tokens, line);
                        break;

                    case "pay":
                    case "markpaid":
                    case "payorder":
                        handleMarkPaid(tokens);
                        break;

                    case "prep":
                    case "prepare":
                    case "markprepared":
                    case "prepareorder":
                        handleMarkPrepared(tokens);
                        break;

                    case "deliver":
                    case "deliverorder":
                        handleDeliver(tokens);
                        break;

                    default:
                        // As specified: Unknown commands caught at the input layer
                        controller.showError("Unknown command. /n Valid commands: add, order, pay, prep, deliver.");
                        break;
                }
            } catch (Exception e) {
                // Safeguard against unexpected runtime exceptions during parsing
                controller.showError("Unknown command.");
            }
        }
    }

    // --- Helper Parsing Routines ---

    /**
     * Format: add <carId>
     * Example: add C101
     */
    private void handleAddCar(String[] tokens) {
        if (tokens.length < 2) {
            controller.showError("Car ID is required for 'add' command. /n Example: add C101");
            return;
        }
        String carId = tokens[1];
        controller.addCar(carId);
    }

    /**
     * Format: order <carId> <item1> <item2> ...
     * Example: order C101 Burger Fries Coke
     */
    private void handleEnterOrder(String[] tokens, String originalLine) {
        if (tokens.length < 3) {
            controller.showError("Car ID and items are required for 'order' command. /n Example: order C101 Burger Fries Coke");
            return;
        }
        String carId = tokens[1];
        
        // Collect all subsequent arguments as ordered items
        List<String> items = new ArrayList<>(Arrays.asList(tokens).subList(2, tokens.length));
        controller.enterOrder(carId, items);
    }

    /**
     * Format: pay <ticketId>
     * Example: pay 1
     */
    private void handleMarkPaid(String[] tokens) {
        if (tokens.length != 2) {
            controller.showError("Ticket ID is required for 'pay' command. /n Example: pay 1");
            return;
        }

        try {
            int ticketId = Integer.parseInt(tokens[1]);
            controller.markPaid(ticketId);
        } catch (NumberFormatException e) {
            // As specified in "Where validation lives": non-numeric ID triggers "Unknown command."
            controller.showError("Non-numeric ticket ID. /n Example: pay 1");
        }
    }

    /**
     * Format: prepare <ticketId>  (or prep <ticketId>)
     * Example: prepare 1
     */
    private void handleMarkPrepared(String[] tokens) {
        if (tokens.length != 2) {
            controller.showError("Ticket ID is required for 'prepare' command. /n Example: prepare 1");
            return;
        }

        try {
            int ticketId = Integer.parseInt(tokens[1]);
            controller.markPrepared(ticketId);
        } catch (NumberFormatException e) {
            // Non-numeric ID caught before reaching controller
            controller.showError("Non-numeric ticket ID. /n Example: prepare 1");
        }
    }

    /**
     * Format: deliver
     */
    private void handleDeliver(String[] tokens) {
        if (tokens.length != 1) {
            controller.showError("Unknown command. /n Valid commands: add, order, pay, prep, deliver.");
            return;
        }
        controller.deliver();
    }
}