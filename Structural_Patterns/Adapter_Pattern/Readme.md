# Adapter Design Pattern

## Definition
Adapter is a structural design pattern that lets two **incompatible interfaces work together**. It wraps an existing class (with an interface the client can't use directly) inside a new class that implements the interface the client *expects* — translating calls from one to the other.

## Why do we need it?
- Third-party libraries/APIs rarely match the interface your application already codes against.
- Rewriting the client (or the third-party code) to make interfaces match is often impossible or impractical.
- Adapter lets you integrate new/external/legacy code **without modifying either side** — client keeps using its familiar interface, and the incompatible class keeps its own API untouched.
- Keeps the client decoupled from vendor-specific APIs — swapping providers later only means writing a new adapter.

## Prime Examples
- `RazorpayAdapter` here — makes `RazorpayAPI` (with its own `makePayment` method) usable wherever a `PaymentGateway` is expected
- Power plug adapters (real-world analogy: different socket "interface" per country)
- `Arrays.asList()` — adapts an array to the `List` interface
- `InputStreamReader` — adapts a byte `InputStream` to a character `Reader`
- Integrating a legacy XML-based system with a new JSON-based client

---

## How it works in `Main.java`

**Target interface** (what the client expects):
```java
interface PaymentGateway {
    void pay(int orderId, double amount);
}
```

**Adaptee** (existing/incompatible class with a different method signature):
```java
class RazorpayAPI {
    public void makePayment(int orderId, double amount) { ... }
}
```
`RazorpayAPI` doesn't implement `PaymentGateway` and can't be used directly wherever a `PaymentGateway` is expected — its method is called `makePayment`, not `pay`.

**Adapter** (bridges the two):
```java
class RazorpayAdapter implements PaymentGateway {
    private RazorpayAPI razorpayAPI;
    public RazorpayAdapter() {
        razorpayAPI = new RazorpayAPI();
    }
    @Override
    public void pay(int orderId, double amount) {
        razorpayAPI.makePayment(orderId, amount);
    }
}
```
`RazorpayAdapter` implements `PaymentGateway` (the interface the client wants) and internally delegates to `RazorpayAPI.makePayment()` (the interface that actually exists). It **translates** the call.

**Client:**
```java
class Checkout {
    private PaymentGateway paymentGateway;
    public Checkout(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
    public void processPayment(int orderId, double amount) {
        paymentGateway.pay(orderId, amount);
    }
}
```
`Checkout` only ever calls `pay(...)` on a `PaymentGateway`. It works identically whether it's handed a native `PayUGateway` or a `RazorpayAdapter` wrapping `RazorpayAPI` — `Checkout` has no idea `RazorpayAPI` even exists.

---

## Pros
- Integrates incompatible/third-party/legacy classes without modifying their source code.
- Client code stays fully decoupled from the adaptee's actual API.
- Single Responsibility: translation logic lives only in the adapter, not scattered through the client.
- Open/Closed Principle: new providers (e.g. a `StripeAdapter`) can be added without touching `Checkout`.

## Cons
- Adds an extra class/layer of indirection for every incompatible API being wrapped.
- If the adaptee's interface is very different from the target (not just a renamed method, but different parameter shapes, return types, or paradigms), the adapter's translation logic can get complex.
- Overusing adapters everywhere can obscure the real API being used underneath, making debugging slightly harder.

---

## Object Adapter vs Class Adapter
| | Object Adapter (used here) | Class Adapter |
|---|---|---|
| Mechanism | Composition — adapter **holds a reference** to the adaptee (`RazorpayAdapter` has-a `RazorpayAPI`) | Inheritance — adapter **extends** the adaptee class |
| Java support | Always possible | Only possible if adaptee isn't `final` and Java allowed multiple inheritance of classes (it doesn't) — so class adapters are rare in Java |
| Flexibility | Can adapt subclasses of the adaptee too, easier to combine with other patterns | Tightly coupled to one specific adaptee class |

`RazorpayAdapter` is an **Object Adapter** — the standard approach in Java, since it only supports single class inheritance.