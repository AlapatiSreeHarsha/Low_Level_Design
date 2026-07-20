# State Design Pattern

## What Is It?

State is a **behavioral design pattern** that lets an object **change its behavior when its internal state changes**, making it appear as if the object changed its class. Instead of scattering `if/else` or `switch` statements everywhere to check "what state am I in and what should I do," the behavior for each state is extracted into its own class, and the object simply **delegates** to whichever state object it currently holds.

The object doing the delegating (the **context**) doesn't implement the state-specific logic itself — it just forwards calls to the current state object, which knows exactly how to behave and, crucially, **which state to transition to next**.

## Product Example (from your code)

An order-tracking system needs to behave differently depending on where the order is in its lifecycle:

- `OrderState` is the **state interface**, defining `next()`, `cancel()`, and `getStateName()`.
- `OrderPlacedState`, `PreparingState`, `OutForDeliveryState`, `DeliveredState`, and `CancelledState` are **concrete states** — each implements the same interface but produces different behavior and decides the next valid state.
- `OrderContext` is the **context**. It holds a `currentState` reference and simply delegates `next()`/`cancel()` calls to it, without containing any state-specific logic itself.

```java
public void next() {
    currentState.next(this);   // delegate — OrderContext doesn't know what state it's in
}
```

Each concrete state controls its own transitions — e.g., `OrderPlacedState.next()` switches the context to `PreparingState`, while `DeliveredState.next()` simply prints that nothing more can happen. `OrderContext` never needs an `if (state == "Delivered")` check anywhere.

## When to Use It

- An object's behavior **depends on its state**, and that behavior must change at runtime as the state changes.
- You have **large conditional blocks** (`if/else` or `switch`) that branch on a state variable throughout multiple methods.
- States have their **own transition rules** — some states allow certain operations, others block them (e.g., a `DeliveredState` refusing to be cancelled).
- You want to make **adding a new state** easy without modifying the context or the other states' logic.
- Common scenarios: order/workflow lifecycles, traffic light systems, media player controls (playing/paused/stopped), connection/session states, game character states (idle/running/jumping).

## State vs Strategy: What's the Actual Difference?

State and Strategy are structurally almost identical — both use a context holding a reference to an interface, with concrete implementations swapped in. The difference is in **intent and behavior**, not structure.

| Basis | **State** | **Strategy** |
|---|---|---|
| **Intent** | Represents an object's **internal condition changing over time**; behavior varies because the object has moved to a different state. | Represents **choosing among interchangeable algorithms** to do the same job; behavior varies because a different approach was selected. |
| **Dependency** | State objects typically **know about each other** (or at least know which state comes next) and actively **change the context's state** for it. | Strategy objects are usually **independent of one another** — a strategy doesn't know or care about other strategies, and doesn't switch itself out. |
| **Final Result** | The context's **behavior changes over time as it moves through states**, driven internally by the state objects themselves. | The context's **behavior is fixed for as long as a given strategy is assigned** — it doesn't change unless the client explicitly picks a different strategy. |
| **Usage / Who Changes It** | The **state objects themselves** trigger transitions (`context.setState(new PreparingState())` happens inside `OrderPlacedState`). | The **client** explicitly chooses and assigns the strategy (`service.setStrategy(new SurgePriorityStrategy())` happens in `Main`), not the strategy itself. |

In short: **State** models "this object behaves differently because of what state it's in, and it manages its own transitions." **Strategy** models "this object can do the same task in different ways, and someone external picks which way."

## Pros

- **Eliminates large conditional logic** — no more `if/else` chains checking the current state throughout the codebase.
- **Open/Closed Principle** — new states can be added without touching the context or existing states.
- **Encapsulates state-specific behavior and transition rules** in one place per state, making each state easy to reason about independently.
- **Makes invalid transitions explicit** — e.g., `DeliveredState.cancel()` clearly states delivered orders can't be cancelled, rather than relying on a scattered condition somewhere else.

## Cons

- **More classes** — every state needs its own class, which can be a lot of boilerplate for simple state machines with just two or three states.
- **Transition logic is spread out** — since each state decides its own next state, understanding the full state machine means reading through every state class rather than seeing it in one place.
- **Tight coupling between states** — concrete states often need to know about other concrete states to transition to them (e.g., `OrderPlacedState` creates a `PreparingState`), which can make states harder to reuse independently.
- **Overkill for simple flags** — if there are only two states and minimal behavior difference, a simple boolean or enum check is often clearer.

## Real-Life Examples

- **E-commerce order tracking** (what this code models) — Placed → Preparing → Out for Delivery → Delivered, with cancellation rules that differ at each stage.
- **Media players** — Playing, Paused, and Stopped states each respond differently to the same "press play" button.
- **Traffic lights** — Red, Yellow, and Green states each define their own duration and next-state transition.
- **TCP connections** — a network connection object behaves differently in states like Listening, Established, and Closed.
- **Vending machines** — behavior for inserting coins, selecting a product, and dispensing differs based on whether the machine is idle, has received payment, or is out of stock.