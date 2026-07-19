# Template Method Design Pattern

## What Is It?

Template Method is a **behavioral design pattern** that defines the **skeleton of an algorithm** in a base class, while letting subclasses override specific steps of that algorithm **without changing its overall structure**.

The base class controls the sequence of steps — subclasses only fill in the parts that vary. This is the inverse of a subclass calling into a parent: here, the **parent calls into the subclass** at well-defined points (a principle sometimes called the "Hollywood Principle" — "don't call us, we'll call you").

In the sample code, `NotificationSender` defines the fixed sequence for sending any notification. `EmailNotification` and `SMSNotification` only supply the two steps that differ between an email and an SMS — everything else (rate limiting, validation, formatting, audit logging, post-send analysis) is shared and unchangeable.

## Key Steps

| Concept | In This Code | Description |
|---|---|---|
| **Template Method** | `send(recipient)` | The method that defines the algorithm's fixed skeleton. Declared `final` so subclasses can't change the order or skip steps — they can only plug into the defined slots. |
| **Primitive Operations** | `composeMessage()`, `sendMessage()` | Abstract methods with **no default implementation**. Every subclass *must* provide its own version — this is where the real variation between Email and SMS lives. |
| **Concrete Operations** | `rateLimitCheck()`, `validateRecipient()`, `formatMessage()`, `preSendAuditLog()`, `postSendAnalysis()` | Fully implemented in the base class and **shared by all subclasses as-is**. Neither `EmailNotification` nor `SMSNotification` overrides these — the behavior is identical for every notification type. |
| **Hook** | *(not present in this code)* | An optional step with a **default (often empty) implementation** that a subclass *may* override if it needs to, but isn't required to. None of the current methods serve as a hook since every concrete method here is meant to run identically for all subclasses — but you could turn `postSendAnalysis()` into a hook by giving it an empty default body and letting specific notification types optionally add extra analytics. |

```java
public final void send(String recipient) {
    rateLimitCheck(recipient);        // concrete operation
    validateRecipient(recipient);     // concrete operation
    String message = composeMessage(recipient);  // primitive operation (subclass-defined)
    formatMessage(message);           // concrete operation
    preSendAuditLog(recipient, message);          // concrete operation
    sendMessage(recipient, message);  // primitive operation (subclass-defined)
    postSendAnalysis(recipient);      // concrete operation (could become a hook)
}
```

## When to Use It

- Multiple classes implement **the same overall algorithm** but differ in a few specific steps.
- You want to **enforce a fixed sequence of operations** and prevent subclasses from reordering or skipping steps.
- You want to **avoid duplicating** the shared parts of an algorithm across multiple classes.
- You want to give subclasses **controlled extension points** (via abstract methods or hooks) instead of letting them override the entire process.
- Common scenarios: data processing pipelines, report generation, game turn sequences, request-handling workflows, notification/messaging systems like this one.

## Pros

- **Eliminates code duplication** — shared steps (`rateLimitCheck`, `validateRecipient`, etc.) are written once in the base class.
- **Enforces structure** — marking `send()` as `final` guarantees every subclass follows the exact same sequence of steps.
- **Easy to extend** — adding a new notification type (e.g., `PushNotification`) only requires implementing the two abstract methods, not rewriting the whole flow.
- **Inversion of control** — the base class drives execution and calls into subclasses at defined points, keeping the overall algorithm centralized and easy to reason about.

## Cons

- **Rigid structure** — because the template method is `final`, subclasses can't change the sequence even if a particular case genuinely needs a different order.
- **Inheritance-based coupling** — subclasses are tightly bound to the base class's structure; changes to the base algorithm can ripple into every subclass.
- **Liskov Substitution risks** — if a subclass's primitive operation does something unexpected for one of the fixed steps, it can violate the assumptions the base algorithm makes.
- **Can encourage a deep or fragile inheritance hierarchy** if overused, especially compared to composition-based alternatives like the Strategy pattern.