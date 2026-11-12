# cs2114-project3
## Reflection

### What Changed from Our Specification
* **Classes & Method Signatures:**
  * **`Car`:** Changed `setTicketId(int ticketId)` to `setTicketId(Integer ticketId)` to align with `ticketID` being stored as an `Integer` wrapper object. Also threw `IllegalArgumentException` instead of `IllegalStateException` when a car already has a ticket ID
  * **`DriveThruQueue<T>`:** Renamed `size()` to `getSize()` and added a `contains(T data)` helper method. Additionally, `toList()` returns a standard `ArrayList` rather than an unmodifiable copy.
  * **`FoodTicket`:** Renamed `markPaid()` and `markPrepared()` to `markAsPaid()` and `markAsPrepared()` (as called in `DriveThruSystem`), and included a `setTicketId(int)` setter.
  * **`DriveThruSystem`:** Renamed `findTicket(int)` to `findFoodTicket(int)`. In `getMenu()`, we returned a `HashSet` containing `"Burger"`, `"Fries"`, and `"Soda"` rather than an unmodifiable set.
  * **`InputHandler`:** Added an overloaded constructor `InputHandler(DriveThruController controller, InputStream in)` alongside `InputHandler(DriveThruController controller)` so CLI commands can be injected via `ByteArrayInputStream` during unit tests without blocking on `System.in`.
* **Where Validation Lives:**
  * While the spec called for `DriveThruSystem.createOrder(...)` to validate menu items, the 7-item order cap, and whether a car is next in line, the current `DriveThruSystem.createOrder` implementation only checks if `carId` exists.
  * Similarly, `DriveThruSystem.addCar(...)` currently relies on `carQueue.contains(car)`, which checks reference equality (`Object.equals`) rather than duplicate `carId` strings unless `Car.equals` is overridden.
  * In `DriveThruSystem.deliverOrder()`, the method currently returns `null` if the front ticket is not both paid and prepared (and throws `NoSuchElementException` from `peek()` if empty) instead of throwing `IllegalStateException` as expected by `DriveThruController.deliver()`.
* **Test Cases:**
  * Corrected specification typos in our test suite (e.g., `Car` constructor throwing `IllegalArgumentException` instead of `NoSuchElementException` on blank input, and `isPrepared()` returning `false` before preparation).

### Did the Model Really Stand Alone?
* **Yes.** None of the Model classes (`Car`, `FoodTicket`, `DriveThruQueue<T>`, `DriveThruSystem`) import or reference `DriveThruView`, `InputHandler`, or `DriveThruController`.
* The boundary holds cleanly: `DriveThruController.refresh()` pulls `List<Car>` and `List<FoodTicket>` snapshots from `DriveThruSystem` and passes them into `DriveThruView.render(List<Car>, List<FoodTicket>)`, ensuring the View never holds a direct reference to the Model.

### What Happened to Our Unknowns
* **Figured Out:**
  * **Dual-Queue Synchronization:** Linking `Car` and `FoodTicket` via `ticketId` allowed both FIFO queues to operate independently while keeping Window 1 (next car with `ticketId == null`) and Window 2 (front car in `carQueue` + front ticket in `foodTicketQueue`) synchronized in `DriveThruView`.
  * **Headless CLI Testing:** Decoupling `Scanner` from `System.in` via an `InputStream` constructor in `InputHandler` solved automated command testing.
* **Still Open:**
  * **Multi-Word Menu Items:** Splitting CLI commands on whitespace in `InputHandler` works for single-word items (`Burger`, `Fries`, `Soda`), but will require delimiter rules (such as commas or quotes) if multi-word items are added to the menu.

### What Doesn't Work Yet
* **`DriveThruSystem.deliverOrder()` Exception Mismatch:** `DriveThruController.deliver()` catches `IllegalStateException` when an order isn't ready or the queue is empty[cite: 19], but `DriveThruSystem.deliverOrder()` currently returns `null` when unpaid/unprepared (causing a `NullPointerException` on `car.getCarId()` in the controller) and throws `NoSuchElementException` when empty.
* **Missing Domain Validations in `DriveThruSystem`:**
  * `createOrder` does not yet validate items against `getMenu()`, enforce the 7-item maximum, or verify that the ordering car is the first car without a ticket.
  * `addCar` checks `carQueue.contains(car)` without `Car` overriding `equals()`, allowing duplicate `carId`s to be enqueued.
  * `Car.setTicketId(Integer ticketId)` checks `if (ticketId != null)` (the parameter) instead of `if (this.ticketID != null)` (the field), preventing any valid ticket ID from being assigned.
  * `Car` constructor checks `carID.isEmpty()` rather than `carID.isBlank()`, allowing whitespace-only IDs.

---

## Next Steps

### Remaining Work to Finish the MVP (In Order)
1. **Fix Critical Model Bugs (`Car` & `DriveThruSystem`):**
   * Fix `Car.setTicketId` to check `if (this.ticketID != null)` and throw `IllegalStateException`, and update the `Car` constructor to reject `carID.isBlank()`.
   * Update `DriveThruSystem.addCar` to check for duplicate `carId` strings rather than object reference equality.
   * Update `DriveThruSystem.deliverOrder()` to throw `IllegalStateException` when `carQueue` is empty or the front ticket is not paid and prepared.
2. **Implement Missing Order Rules (`DriveThruSystem.createOrder`):**
   * Add validation for null/empty item lists, menu membership (`getMenu().containsAll(items)`), the 7-item limit, and enforcing that only the first car in `carQueue` with `ticketId == null` can place an order.
3. **End-to-End Integration & Test Verification:**
   * Run full CLI scenarios through `DriveThruController.main()` (`add` -> `order` -> `pay` -> `prepare` -> `deliver` -> `quit`) and verify all normal and bad-input JUnit tests pass.

### Deliverable 4 Change Feature
* **Feature Name:** **Order Cancellation / Drive-Off (`cancel <carId>`)**
* **Architectural Impact Prediction:**
  * **Classes That Will Change:**
    * **Model (`DriveThruQueue<T>` & `DriveThruSystem`):** `DriveThruQueue<T>` will need a `remove(T item)` method to unlink a node from the middle or front of the linked list while maintaining `front`, `rear`, and `size`. `DriveThruSystem` will add `cancelOrder(String carId)` to locate the car, remove it from `carQueue`, and remove its linked `FoodTicket` (if one exists and is not yet `PREPARED`) from `foodTicketQueue`.
    * **Controller (`DriveThruController`):** Will add `cancelOrder(String carId)` to call `system.cancelOrder(carId)`, display a confirmation message, refresh the view, and catch `NoSuchElementException` or `IllegalStateException` to display errors.
    * **View (`InputHandler`):** Will add a `cancel <carId>` command case in `readInput()` to parse the car ID and call `controller.cancelOrder(carId)`.
  * **Classes That Should Not Need to Change:**
    * **`Car` and `FoodTicket`:** Already store `carId`, `ticketId`, and preparation status needed to validate whether cancellation is allowed.
    * **`DriveThruView`:** Renders queue snapshots dynamically via `render(List<Car>, List<FoodTicket>)`, so removed cars and tickets will automatically disappear from the display without view changes.

### Stretch Goals
* **Committing To:** Expanding `DriveThruView.render(...)` to display the full ordered list of cars and tickets in both queues alongside Window 1 and Window 2 statuses (currently `DriveThruView` computes `waitingCount` in a loop but only prints the cars actively at Window 1 and Window 2).
* **Dropping:** Building a graphical UI (GUI) — keeping the application strictly CLI-based to prioritize model validation, the Deliverable 4 change feature, and thorough JUnit coverage.

### New Tests Needed
* **Model Tests (`DriveThruQueueTest` & `DriveThruSystemTest`):**
  * Normal cases: Removing an element from the front, middle, and rear of `DriveThruQueue<T>` updates `getSize()` and `toList()` accurately; calling `cancelOrder("C102")` removes both `C102` and its linked unpaid/in-progress `FoodTicket` while preserving queue order for other cars.
  * Bad-input cases: Calling `cancelOrder` on a nonexistent `carId` throws `NoSuchElementException`; calling `cancelOrder` when the linked `FoodTicket` is already `PREPARED` throws `IllegalStateException`.
* **Controller & Input Tests (`DriveThruControllerTest` & `InputHandlerTest`):**
  * Normal cases: Typing `cancel C102` calls `controller.cancelOrder("C102")` and triggers `view.render(...)` and `view.showMessage(...)`.
  * Bad-input cases: Typing `cancel` without a `carId` triggers `controller.showError("Unknown command.")`; canceling a prepared order triggers `view.showError(...)` without terminating the input loop.

### Division of Responsibilities
* **Julia (`InputHandler` & Integration):** Add `cancel <carId>` command parsing to `InputHandler`, update CLI delimiter handling if needed, and co-lead end-to-end integration testing.
* **Aaron (`Model` — `Car`, `DriveThruQueue`, `DriveThruSystem`):** Fix the `Car.setTicketId` / `deliverOrder` / `createOrder` MVP bugs, implement `DriveThruQueue.remove` and `DriveThruSystem.cancelOrder`, and add model unit tests.
* **Cassidy (`View` — `DriveThruView`):** Enhance `DriveThruView.render` to print the full queue waiting lists below the active Window 1 / Window 2 statuses and verify error formatting.
* **Golden (`Controller` — `DriveThruController` & Test Suite):** Wire `cancelOrder` through `DriveThruController`, align exception handling with the model fixes, and update the JUnit test suite[cite: 14, 15].

## Attribution

| Class| Name | Contribution Details |
|---|---|
| DriveThruView | Cassidy | Drafted the functions on my own. Then I used GenAI to find any inconsistencies with the draft. Additionally, I had GenAI show me how I should start to implement window 1 status, and then I used what I had learned from impletmenting window 1 status to implement window 2 status on my own. Then I had GenAI check over my code. |
