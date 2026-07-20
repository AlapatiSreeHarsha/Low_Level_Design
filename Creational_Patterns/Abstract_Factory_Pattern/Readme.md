# Abstract Factory Design Pattern

## Definition
Abstract Factory is a creational design pattern that provides an interface for creating **families of related objects** without specifying their concrete classes. Unlike a Simple Factory (which returns one product), an Abstract Factory groups several related factory methods together, so a single factory produces a *consistent set* of products that belong together.

## Why do we need it?
- Some objects naturally come in **related groups** that must stay consistent with each other — e.g. a US checkout should always pair a US-compatible payment gateway with a US invoice, never with a GST invoice.
- Prevents mixing incompatible products (accidentally using an India payment gateway with a US invoice format).
- Client code depends only on abstract interfaces (`PaymentGateway`, `InvoiceGenerator`, `RegionalPaymentGatewayFactory`) — it never touches concrete classes like `StripePaymentGateway` or `GSTInvoice` directly.
- Adding a whole new "family" (e.g. a new region) means adding one new factory class, without touching existing client code.

## Prime Examples
- `USPaymentGateway` / `IndiaPaymentGateway` here — each produces a matching payment gateway + invoice generator
- Cross-platform UI toolkits: a `WindowsFactory` producing `WindowsButton` + `WindowsCheckbox`, vs a `MacFactory` producing `MacButton` + `MacCheckbox`
- Database driver families: a factory producing a matching `Connection`, `Command`, and `Adapter` for MySQL vs PostgreSQL
- Theme/skin systems producing a matched set of fonts, colors, and icons

---

## How it works in `Checkout_Service.java`

**Product families (two of them):**
- `PaymentGateway` interface → `PayPalPaymentGateway`, `StripePaymentGateway`, `RazorpayPaymentGateway`
- `InvoiceGenerator` interface → `USInvoice`, `GSTInvoice`

**Abstract factory:**
```java
interface RegionalPaymentGatewayFactory {
    PaymentGateway createPaymentGateway();
    InvoiceGenerator createInvoiceGenerator();
}
```
This declares creation methods for *both* products in the family — the factory is responsible for keeping them consistent.

**Concrete factories:**
- `USPaymentGateway` — builds a `PayPalPaymentGateway`/`StripePaymentGateway` **paired with** a `USInvoice`.
- `IndiaPaymentGateway` — builds a `PayPalPaymentGateway`/`RazorpayPaymentGateway` **paired with** a `GSTInvoice`.

Each concrete factory guarantees its two products always match the correct region.

**Client:**
```java
class Checkout_Service {
    public Checkout_Service(RegionalPaymentGatewayFactory factory) {
        this.paymentGateway = factory.createPaymentGateway();
        this.invoiceGenerator = factory.createInvoiceGenerator();
    }
    public void checkout(double amount) {
        paymentGateway.processPayment(amount);
        invoiceGenerator.generateInvoice(amount);
    }
}
```
`Checkout_Service` never instantiates `Stripe...`, `Razorpay...`, `GSTInvoice`, etc. directly — it only knows about the `RegionalPaymentGatewayFactory` abstraction, so it works identically regardless of which region's factory is injected.

---

## Pros
- Guarantees related products stay consistent (no mismatched payment gateway/invoice combos).
- Client code is fully decoupled from concrete product classes — depends only on interfaces.
- Adding a new region/family (e.g. `EUPaymentGateway`) requires no changes to `Checkout_Service`.
- Follows the Open/Closed Principle at the family level — extend by adding factories, not by editing existing ones.

## Cons
- More classes/interfaces than a Simple Factory — higher upfront complexity.
- Adding a **new product type** to the family (e.g. a `TaxCalculator` alongside `PaymentGateway`/`InvoiceGenerator`) requires changing the `RegionalPaymentGatewayFactory` interface **and every concrete factory** that implements it.
- In this specific code, `USPaymentGateway`/`IndiaPaymentGateway` still use `if-else` on a `mode` string internally to pick the payment gateway — a Simple Factory nested inside an Abstract Factory. If `mode` doesn't match any case, `paymentGateway` stays `null`, which would cause a `NullPointerException` in `checkout()`. Worth adding a `default`/`else` branch that throws an exception.

---

## Simple Factory vs Abstract Factory (quick contrast)
| | Simple Factory | Abstract Factory |
|---|---|---|
| Produces | One product | A **family** of related products |
| Example here | `LogisticsFactory.getLogistics("Truck")` | `USPaymentGateway` → matching gateway + invoice |
| Goal | Hide instantiation of one type | Keep multiple related types consistent with each other |