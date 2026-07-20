# Visitor Design Pattern

## What Is It?

Visitor is a **behavioral design pattern** that lets you add new operations to a group of related classes **without modifying those classes**. Instead of putting every possible operation (invoicing, shipping cost, tax calculation, etc.) directly inside each element class, the operation is pulled out into a separate **visitor** object, and each element simply "accepts" a visitor and lets it perform the operation.

This works through a technique called **double dispatch**: the element calls `accept(visitor)`, and inside that method it calls `visitor.visit(this)` — passing its own concrete type. Because `this` is typed exactly as `PhysicalProduct`, `DigitalProduct`, or `GiftCard`, the compiler resolves the correct overloaded `visit()` method on the visitor, without any `instanceof` checks or casting.

## Key Components

| Role | In This Code | Responsibility |
|---|---|---|
| **Element Interface** | `Item` | Declares `accept(ItemVisitor visitor)` — every element type must support being visited. |
| **Concrete Elements** | `PhysicalProduct`, `DigitalProduct`, `GiftCard` | Each implements `accept()` by calling `visitor.visit(this)`, triggering the correct overload. |
| **Visitor Interface** | `ItemVisitor` | Declares one overloaded `visit()` method per concrete element type. |
| **Concrete Visitors** | `InvoiceVisitor`, `ShippingCostVisitor` | Each implements a distinct operation across all element types — one generates invoice text, the other computes shipping cost — without either operation living inside the element classes. |

```java
public void accept(ItemVisitor visitor) {
    visitor.visit(this);   // double dispatch — resolves to visit(PhysicalProduct)
}
```

```java
for (Item item : items) {
    item.accept(invoiceVisitor);   // same loop, different operation just by swapping the visitor
}
for (Item item : items) {
    item.accept(shippingVisitor);
}
```

Notice that `PhysicalProduct`, `DigitalProduct`, and `GiftCard` never contain any invoicing or shipping-cost logic themselves — all of that lives in the visitors, completely separate from the element classes.

## Real-World Example

**E-commerce checkout systems** (what this code models) commonly separate "what a product is" from "what can be done with it." A shopping cart holding physical products, digital downloads, and gift cards can be run through an `InvoiceVisitor` to generate a receipt, a `ShippingCostVisitor` to calculate delivery fees, or later a `TaxVisitor` or `DiscountVisitor` — all without ever touching the `PhysicalProduct`, `DigitalProduct`, or `GiftCard` classes again.

Other common real-world uses:
- **Compilers/interpreters** — an Abstract Syntax Tree (AST) with node types like `NumberNode`, `BinaryOpNode`, `VariableNode`, visited by different passes: a type-checker, an optimizer, a code generator — each a separate visitor over the same tree.
- **Document object models** — visiting different node types (paragraphs, tables, images) in a document to export to PDF, HTML, or plain text, each export format as its own visitor.
- **File system operations** — visiting different file/directory types to compute total size, search for matches, or generate a report, without changing the `File`/`Directory` classes themselves.

## When to Use It

- You need to perform **several unrelated operations** across a fixed set of classes, and don't want to scatter that logic across each class.
- The set of **element classes rarely changes**, but the set of **operations performed on them changes or grows often**.
- You want to keep an element class **focused on its own data**, moving cross-cutting operations (reporting, exporting, calculating) elsewhere.
- You want to avoid `instanceof` checks or type-casting when handling a collection of different but related types.

## When to Avoid It

- The set of **element classes changes frequently** — every new element type requires updating the visitor interface *and* every existing concrete visitor, which can ripple widely.
- There's only **one or two operations** ever needed — the extra visitor/element machinery may not be worth it over simply adding a method to each class.
- Elements need to expose **private internal state** to visitors that shouldn't otherwise be public, which can weaken encapsulation.

## Pros

- **Open/Closed for operations** — new visitors (e.g., `TaxVisitor`) can be added without touching any existing element class.
- **Keeps related behavior together** — all the logic for one operation (e.g., invoicing across all product types) lives in a single visitor class, instead of being scattered across every element.
- **Avoids type-checking/casting** — double dispatch naturally routes to the correct method based on the element's real type.
- **Clean separation of data and operations** — element classes stay focused on representing data; visitors own the behavior.

## Cons

- **Fragile when elements change** — adding a new element type (e.g., `SubscriptionProduct`) means updating the `ItemVisitor` interface and every concrete visitor to handle it.
- **Breaks encapsulation risk** — visitors sometimes need access to element internals that would otherwise stay private, forcing extra getters to be exposed.
- **More classes and indirection** — the accept/visit double-dispatch mechanism is less intuitive to read than a straightforward method call, especially for developers unfamiliar with the pattern.
- **Overkill for a stable, small set of operations** — if operations rarely change, putting them directly on the element classes is simpler.