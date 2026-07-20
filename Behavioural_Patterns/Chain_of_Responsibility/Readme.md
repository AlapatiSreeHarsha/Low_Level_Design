# Chain of Responsibility Design Pattern

## What Is It?

Chain of Responsibility is a **behavioral design pattern** that lets you pass a request along a **chain of handler objects**, where each handler decides either to process the request or pass it on to the next handler in the chain. The sender of the request doesn't need to know which handler will actually end up processing it — it just hands the request to the first handler and the chain takes care of the rest.

Each handler holds a reference to the **next handler** in the chain (in this code, `nextHandler`). If a handler can't deal with the request, it forwards it along; if no handler in the chain can process it, the request simply falls through unhandled.

In the sample code:

- `SupportHandler` is the **abstract handler**, holding a `nextHandler` reference and declaring `handleRequest()`.
- `TechnicalSupport`, `BillingSupport`, `GeneralSupport`, and `DeliverySupport` are **concrete handlers** — each checks whether the request type matches its specialty; if not, it forwards the request to `nextHandler`.
- The chain is wired up externally: `technical → billing → general → delivery`.

```java
technical.setNextHandler(billing);
billing.setNextHandler(general);
general.setNextHandler(delivery);

technical.handleRequest("Delivery");
// Technical can't handle it -> passes to Billing
// Billing can't handle it  -> passes to General
// General can't handle it  -> passes to Delivery
// Delivery handles it
```

The caller only ever talks to `technical`, the first handler — it has no idea (and doesn't need to know) that the request actually gets resolved four handlers down the chain.

## When to Use It

- More than one object **might handle a request**, and the actual handler isn't known in advance — it should be determined at runtime by walking the chain.
- You want to **issue a request without specifying the receiver explicitly**, decoupling the sender from the specific object that processes it.
- The set of handlers and their order should be **configurable independently** of the code that sends requests.
- You want to avoid a large `if/else if` block that checks a request's type against every possible handler in one place.
- Common scenarios: support/ticket routing systems, middleware pipelines, event bubbling in UI frameworks, approval workflows, logging/filter chains.

## Pros

- **Decouples sender from receiver** — the code issuing a request doesn't need to know which handler will actually deal with it.
- **Flexible, reconfigurable chains** — the order and composition of handlers can be changed at runtime (e.g., `setNextHandler()`), without touching the handler classes themselves.
- **Single Responsibility per handler** — each concrete handler only worries about its own specialty and forwarding logic.
- **Open/Closed Principle** — new handler types (e.g., `RefundSupport`) can be added without modifying existing handlers.

## Cons

- **No guarantee a request gets handled** — as seen with `"Refund"` in the example, if no handler in the chain matches, the request silently falls through with "No handler available."
- **Debugging can be harder** — tracing which handler actually processed a request means following the chain, rather than seeing it in one place.
- **Chain configuration is external and fragile** — if handlers are wired in the wrong order (e.g., forgetting a `setNextHandler()` call), requests can fail to reach a handler that could have processed them.
- **Performance cost for long chains** — a request may need to pass through many handlers before finding (or failing to find) the right one.

## Real-Life Example

**A user signup flow** is a natural fit for this pattern: `SignupValidationHandler → EmailVerificationHandler → OTPVerificationHandler → ProfileSetupHandler`. Each step in the signup process only knows how to do its own job — validate input, send a verification email, check the OTP, finish profile setup — and passes the request to the next step once its own job is done, without any single class needing to know the entire signup pipeline.

Other common real-life uses:
- **Customer support ticket routing** (what this code models) — a ticket bounces from Level 1 support, to Level 2, to a specialist, until someone can resolve it.
- **Web server middleware** — an HTTP request passes through authentication, logging, rate-limiting, and compression middleware, each deciding whether to act and/or forward the request.
- **Expense approval workflows** — a reimbursement request goes to a manager, then a director, then finance, depending on the amount, each handler either approving it or passing it up the chain.
- **Event handling in UI frameworks** — a click event bubbles up from a button, to its container, to the window, until something handles it or it's ignored.