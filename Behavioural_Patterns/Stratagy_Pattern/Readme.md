# Strategy Design Pattern

## What Is It?

Strategy is a **behavioral design pattern** that defines a **family of interchangeable algorithms**, encapsulates each one in its own class, and lets the client swap between them at runtime — without changing the code that uses them.

Instead of writing a big `if/else` or `switch` block inside a class to decide which algorithm to run, the algorithm is extracted into its own object that implements a common interface. The class that needs the algorithm (the **context**) just holds a reference to that interface and delegates the work to whichever concrete strategy is currently plugged in.

In the sample code:

- `MatchingStrategy` is the **strategy interface**, defining `match(location)`.
- `NearestDriverStrategy`, `SurgePriorityStrategy`, and `AirportQueueStrategy` are **concrete strategies** — each a different driver-matching algorithm.
- `MatchingService` is the **context**. It holds a `MatchingStrategy` reference and delegates to it in `matchRide()`, without knowing (or caring) which concrete strategy it's using.

```java
MatchingService service = new MatchingService(new NearestDriverStrategy());
service.matchRide("Hyderabad");          // uses NearestDriverStrategy

service.setStrategy(new SurgePriorityStrategy());
service.matchRide("Bangalore");          // now uses SurgePriorityStrategy, same matchRide() call
```

`MatchingService` never changes — only the strategy object plugged into it changes, and that changes its behavior.

## Real-World Example

**Ride-hailing apps** (which is literally what this code models — think Uber/Ola) use exactly this pattern: the same "match a rider to a driver" operation behaves differently depending on conditions — nearest available driver under normal demand, high-priority matching during surge pricing, or a queue-based match at an airport. The app swaps the matching algorithm without changing the rest of the booking flow.

Other common real-world uses:
- **Payment processing** — an e-commerce checkout swapping between credit card, PayPal, or wallet payment strategies behind a single `processPayment()` call.
- **Sorting/compression libraries** — choosing between different sorting or compression algorithms at runtime based on data size or type.
- **Navigation apps** — switching between "fastest route," "shortest route," and "avoid tolls" routing strategies.
- **Validation frameworks** — plugging in different validation rules/strategies for different form types.

## When to Use It

- You have **multiple variants of an algorithm** for doing the same task, and want to switch between them at runtime.
- You want to **eliminate conditional logic** (large `if/else` or `switch` statements) that selects behavior based on type or mode.
- Different clients or contexts need **different behavior variations** of an operation, and new variations are expected to be added over time.
- You want to isolate an algorithm's implementation details from the code that uses it, so each can change independently.

## When to Avoid It

- There's only **one algorithm** and no realistic plan to add alternatives — the extra interface and classes are unnecessary overhead.
- The algorithms **rarely or never change** at runtime — a simple method or a static utility function is more direct.
- The number of strategies is **small and unlikely to grow**, and a straightforward `if/else` is actually more readable than several tiny classes.
- The overhead of managing strategy objects (instantiating, injecting, wiring them) outweighs the benefit for a **simple, short-lived** piece of logic.

## Pros

- **Open/Closed Principle** — new strategies (e.g., a future `ScheduledRideStrategy`) can be added without modifying `MatchingService`.
- **Eliminates conditional complexity** — no more branching logic scattered through the context class to pick behavior.
- **Runtime flexibility** — the algorithm can be swapped on the fly via `setStrategy()`, as shown when the service switches strategies between rides.
- **Testability** — each strategy can be unit-tested in isolation, independent of the context that uses it.

## Cons

- **More classes** — every algorithm variant needs its own class, which can add clutter for a small number of simple algorithms.
- **Client must know the strategies** — the calling code needs to understand the differences between strategies to choose the right one.
- **Communication overhead** — passing all the data a strategy might need through a common interface can sometimes force a broader interface than any single strategy actually uses.
- **Increased indirection** — following the logic requires jumping between the context and the strategy implementation, which can make small codebases harder to trace.