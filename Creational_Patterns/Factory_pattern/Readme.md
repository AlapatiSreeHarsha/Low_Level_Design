# Factory Design Pattern

## Definition
Factory is a creational design pattern that provides an interface (or static method) for creating objects, **without exposing the object-creation logic** to the client and **without specifying the exact class** of object being created. The client asks for an object by type/key, and the factory decides which concrete class to instantiate.

## Why do we need it?
- Decouples object creation from object usage — client code depends on an interface/abstraction, not concrete classes.
- Centralizes creation logic in one place, making it easier to maintain and extend.
- Adding a new product type only requires changes in the factory, not in every place objects are created (Open/Closed Principle).
- Hides complex instantiation logic (e.g. choosing between multiple implementations) from the caller.

## Prime Examples
- `LogisticsFactory` choosing between `Truck`, `Ship`, `Airplane` (as in this code)
- `java.util.Calendar.getInstance()`
- `java.text.NumberFormat.getInstance()`
- Shape factories (`Circle`, `Square`, `Rectangle`)
- UI toolkit widget factories (creating OS-specific buttons, checkboxes)
- Document parsers/readers chosen by file extension (`.csv`, `.json`, `.xml`)

---

## How it works in `LogisticsCompany.java`

```java
interface Logistics {
    void send();
}
class Truck implements Logistics { ... }
class Ship implements Logistics { ... }
class Airplane implements Logistics { ... }

class LogisticsFactory {
    public static Logistics getLogistics(String type) {
        if(type.equalsIgnoreCase("Truck")) return new Truck();
        else if(type.equalsIgnoreCase("Ship")) return new Ship();
        else if(type.equalsIgnoreCase("Airplane")) return new Airplane();
        return null;
    }
}
```

- `Logistics` is the common interface (product abstraction).
- `Truck`, `Ship`, `Airplane` are concrete products implementing that interface.
- `LogisticsFactory` is the factory — it decides which concrete product to return based on the input `type`.
- The client (`main`) only interacts with the `Logistics` interface, staying unaware of the concrete class.

---

## Pros
- **Loose coupling**: client code depends only on the `Logistics` interface, not concrete classes.
- **Single Responsibility**: object-creation logic is isolated in the factory class.
- **Easy to extend**: adding a new transport type (e.g. `Train`) means adding a new class + one `else if` — no change needed in client code.
- Improves testability — factory can be mocked/stubbed.

## Cons
- Every new product type requires modifying the factory method (`if-else`/`switch` chain keeps growing) — violates Open/Closed Principle at the factory level unless combined with reflection or a registry map.
- Adds an extra layer of abstraction/indirection, which can be overkill for simple object creation.
- Returning `null` for an unrecognized type (as in this code) is risky — better to throw an exception (e.g. `IllegalArgumentException`) to fail fast.
- If overused, can lead to a large number of small factory classes, adding complexity.

---

## Simple Factory vs Factory Method vs Abstract Factory
| Variant | Description |
|---|---|
| **Simple Factory** (this example) | One factory class with a static/instance method that returns a product based on input — not a formal GoF pattern, but the most common starting point. |
| **Factory Method** | Subclasses override a factory method to decide which product to instantiate — creation logic lives in the class hierarchy itself. |
| **Abstract Factory** | A factory of factories — produces families of related objects without specifying their concrete classes. |

`LogisticsFactory` here is a **Simple Factory** implementation.