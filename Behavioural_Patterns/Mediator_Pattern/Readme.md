# Mediator Design Pattern

## What Is It?

Mediator is a **behavioral design pattern** that reduces chaotic, direct communication between a group of objects by forcing them to communicate **only through a central mediator object**. Instead of every object holding references to every other object it needs to talk to, each object only knows about the mediator — and the mediator handles routing messages between them.

This turns a tangled **many-to-many** web of dependencies into a simpler **hub-and-spoke** structure: each object (a "colleague") is only coupled to the mediator, never to its peers directly.

In the sample code:

- `DocumentSessionMediator` is the **mediator interface**, defining `join()` and `broadcastChange()`.
- `CollaborativeDocument` is the **concrete mediator** — it keeps track of every connected `User` and is responsible for relaying changes between them.
- `User` is the **colleague** — it holds a reference to the mediator (not to other users) and sends/receives changes exclusively through it.

```java
public void makeChange(String change) {
    System.out.println(name + " made change: " + change);
    mediator.broadcastChange(change, this);   // User never talks to other Users directly
}
```

```java
public void broadcastChange(String change, User sender) {
    for (User user : users) {
        if (user != sender) {
            user.receiveChange(change, sender);   // Mediator handles all routing
        }
    }
}
```

`Harsha`, `Rahul`, and `Anjali` never hold references to each other — when Harsha makes a change, `CollaborativeDocument` is the one that decides who else needs to hear about it.

## Real-World Example

**Real-time collaborative document editors** (what this code models — think Google Docs) are a textbook use of Mediator: when one person edits a doc, they don't send that edit directly to every other open browser tab. Instead, a central session/server object receives the change and broadcasts it to everyone else connected to that document.

Other common real-world uses:
- **Air traffic control** — pilots don't communicate directly with each other; every plane talks only to the control tower, which coordinates all traffic.
- **Chat room applications** — a chat room server receives a message from one user and relays it to every other participant, so users never message each other directly.
- **GUI dialog boxes** — a dialog with multiple interdependent form fields (e.g., enabling a submit button only when other fields are valid) is often coordinated by a mediator that all the widgets report to, instead of widgets referencing each other.
- **Ride-hailing dispatch systems** — drivers and riders don't coordinate directly; a central dispatch service matches and routes information between them.

## When to Use It

- A set of objects communicate in **complex, many-to-many ways**, making the web of direct references hard to follow or maintain.
- You want to **reduce coupling** between a group of interacting objects so they can be reused or modified independently.
- The communication/coordination logic between objects is **substantial enough to deserve its own class**, rather than being spread across every participant.
- You want to be able to add or remove participants (like a new `User` joining a session) **without changing the other participants**.

## Pros

- **Reduces coupling** — colleagues (`User`) only depend on the mediator interface, not on each other.
- **Centralizes communication logic** — routing/coordination rules live in one place (`CollaborativeDocument`), making them easier to understand and modify.
- **Simplifies adding new participants** — a new `User` just needs to join the mediator; existing users don't need to know about it.
- **Improves reusability** — colleague classes become easier to reuse elsewhere since they aren't hard-wired to specific peers.

## Cons

- **Mediator can become a "God object"** — as more interaction logic gets added, the mediator risks growing into a large, complex class that's hard to maintain.
- **Single point of failure/bottleneck** — since all communication funnels through one object, a bug or performance issue there affects every colleague.
- **Less transparent at a glance** — understanding how two users' changes reach each other means tracing through the mediator, rather than seeing a direct call between them.
- **Overkill for simple interactions** — if only two or three objects interact in a straightforward way, introducing a mediator may add unnecessary indirection.