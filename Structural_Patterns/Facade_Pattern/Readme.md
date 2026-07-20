# Facade Design Pattern

## Definition
Facade is a structural design pattern that provides a **single, simplified interface** to a complex subsystem made up of many classes. The client interacts only with the facade, which internally coordinates calls to the underlying subsystem classes on the client's behalf.

## Why do we need it?
- Complex workflows often involve coordinating several subsystems in a specific order (payment, then seat reservation, then ticketing, then loyalty points, then notification).
- Without a facade, every client would need to know *all* these subsystem classes and the correct order to call them — tightly coupling client code to internal implementation details.
- A facade hides that complexity behind one simple method call, making the subsystem easier to use correctly.
- Reduces coupling between client code and subsystem classes — subsystems can change internally as long as the facade's interface stays stable.

## Prime Examples
- `MovieBookingFacade` here — wraps payment, seat reservation, ticket booking, loyalty points, and notifications behind one `bookMovieTicket()` call
- `javax.faces.context.FacesContext` in JSF
- Compiler facades that hide lexer, parser, optimizer, and code-generator subsystems behind one `compile()` call
- Home theater "watch movie" button that internally turns on projector, sound system, lights, and streaming device
- ORM libraries providing a simple `save()`/`find()` API over complex SQL generation, connection pooling, and transaction management

---

## How it works in `Client.java`

**Subsystem classes** (each with its own narrow responsibility):
```java
PaymentService       -> pay(paymentType, amount)
SeatReservation       -> reserveSeat(seatNumber)
NotificationService    -> sendNotification(message)
LoyaltyPoints          -> addPoints(points)
TicketBooking          -> bookTicket(ticketId)
```
None of these know about each other — each does exactly one job.

**Facade** (coordinates the subsystems in the right order):
```java
class MovieBookingFacade {
    private PaymentService paymentService;
    private SeatReservation seatReservation;
    private NotificationService notificationService;
    private LoyaltyPoints loyaltyPoints;
    private TicketBooking ticketBooking;

    public void bookMovieTicket(String paymentType, double amount, String seatNumber, String ticketId) {
        paymentService.pay(paymentType, amount);
        seatReservation.reserveSeat(seatNumber);
        ticketBooking.bookTicket(ticketId);
        loyaltyPoints.addPoints(10);
        notificationService.sendNotification("Your movie ticket has been booked successfully!");
    }
}
```
`MovieBookingFacade` owns instances of all five subsystem classes and calls them in the correct sequence inside a single method.

**Client:**
```java
MovieBookingFacade facade = new MovieBookingFacade();
facade.bookMovieTicket("Credit Card", 15.99, "A1", "T001");
```
The client makes **one call** and gets the entire booking workflow — payment, seat reservation, ticketing, loyalty points, and notification — without ever touching the five subsystem classes directly or needing to know their call order.

---

## Pros
- Drastically simplifies the client's interaction with a complex subsystem — one method instead of five coordinated calls.
- Reduces coupling: client only depends on the facade, not on every subsystem class.
- Encapsulates the "correct" order of operations in one place, preventing misuse (e.g. accidentally reserving a seat before payment succeeds).
- Subsystem internals can evolve (e.g. swap `NotificationService` implementation) without breaking client code, as long as the facade's public method signature stays the same.

## Cons
- The facade itself can become a "god object" if too much unrelated logic gets piled into it over time.
- Doesn't restrict access to subsystem classes — clients can still bypass the facade and call `PaymentService`/`SeatReservation` etc. directly if they're not made private/hidden, which can lead to inconsistent usage.
- Adds an extra layer — for very simple subsystems with one or two classes, a facade may be unnecessary overhead.
- Error handling can get tricky: `bookMovieTicket()` in this example doesn't roll back earlier steps (e.g. payment) if a later step (e.g. ticket booking) fails — real systems would need to handle partial-failure scenarios explicitly.

---

## Facade vs Adapter (quick contrast)
| | Facade | Adapter |
|---|---|---|
| Purpose | Simplify access to a **complex subsystem** with multiple classes | Make **one incompatible interface** match what the client expects |
| Number of classes wrapped | Many | Usually one (the adaptee) |
| Changes the interface? | Provides a *new, simpler* interface | Translates an *existing* interface into another existing one |