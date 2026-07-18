# Decorator Design Pattern

## Definition
Decorator is a structural design pattern that lets you attach new behavior/responsibilities to an object **dynamically at runtime**, by wrapping it in one or more "decorator" objects — without modifying the original class or using subclassing for every combination of features.

## Why do we need it?
- Adding features via subclassing doesn't scale: if a `Pizza` needs optional extra cheese, olives, and stuffed crust, subclassing would require a separate class for every *combination* (`ExtraCheeseOlivesStuffedCrustPizza`, etc.) — combinatorial explosion.
- Decorator lets you compose behavior at runtime by stacking wrappers, choosing only the combination needed for that instance.
- Keeps each add-on's logic isolated in its own small class (Single Responsibility), instead of bloating one giant class with flags/conditionals.
- Follows the Open/Closed Principle — new toppings/add-ons can be added as new decorator classes without touching existing ones.

## Prime Examples
- `ExtraCheese`, `Olives`, `StuffedCrust` wrapping a `Pizza` here
- `java.io` streams — `BufferedReader(new InputStreamReader(new FileInputStream(...)))` — each layer adds behavior around the previous stream
- Coffee shop ordering systems (base coffee + milk + sugar + whipped cream add-ons)
- UI component wrapping — adding scrollbars, borders, or shadows around a base widget
- Middleware chains in web frameworks (each middleware wraps/decorates the request handler)

---

## How it works in `Main.java`

**Component interface** (common contract for both base object and decorators):
```java
interface Pizza {
    String getDescription();
    double getCost();
}
```

**Concrete component** (the base object being decorated):
```java
class MargheritaPizza implements Pizza {
    public String getDescription() { return "Margherita Pizza"; }
    public double getCost() { return 200.0; }
}
```

**Base decorator** (holds a reference to the wrapped `Pizza`, implements the same interface):
```java
abstract class PizzaDecorator implements Pizza {
    protected Pizza pizza;
    public PizzaDecorator(Pizza pizza) {
        this.pizza = pizza;
    }
}
```

**Concrete decorators** (each adds its own behavior, then delegates to the wrapped object):
```java
class ExtraCheese extends PizzaDecorator {
    public ExtraCheese(Pizza pizza) { super(pizza); }
    public String getDescription() { return pizza.getDescription() + ", Extra Cheese"; }
    public double getCost() { return pizza.getCost() + 50.0; }
}
```
`Olives` and `StuffedCrust` follow the exact same pattern — call through to the wrapped `pizza`, then add their own description text and cost.

**Client (stacking decorators at runtime):**
```java
Pizza pizza = new MargheritaPizza();
pizza = new ExtraCheese(pizza);
pizza = new Olives(pizza);
pizza = new StuffedCrust(pizza);
```
Each line wraps the previous `pizza` in one more layer. When `pizza.getCost()` is finally called, it cascades: `StuffedCrust` → `Olives` → `ExtraCheese` → `MargheritaPizza`, each adding its own cost on top of the one before — giving `200 + 50 + 40 + 80 = 370`. Since every decorator implements the same `Pizza` interface as the base object, the client can keep stacking or swapping wrappers without caring how many layers deep it is.

---

## Pros
- Add/remove behavior at runtime by composing objects, instead of being locked into a fixed class hierarchy.
- Avoids combinatorial subclass explosion — N optional features need only N decorator classes, not 2^N subclasses.
- Each decorator has a single, focused responsibility.
- Open/Closed Principle: new decorators can be introduced without modifying `Pizza`, `MargheritaPizza`, or existing decorators.

## Cons
- Many small wrapper objects/classes can make the system harder to understand — stack traces and debugging get deeper with each layer.
- Order of wrapping can matter and may be easy to get wrong (though not an issue for simple additive cost/description logic like this example).
- Identity of the wrapped object changes — `pizza` after decoration is no longer `instanceof MargheritaPizza` at the outer level, which can complicate type-checking code that relies on concrete types.
- Slightly more upfront design effort compared to just adding fields/flags to one class for a small, fixed number of options.

---

## Decorator vs Inheritance (quick contrast)
| | Subclassing | Decorator |
|---|---|---|
| When behavior is fixed | Compile-time, static | Runtime, dynamic |
| Combining features | Needs a subclass per combination | Just stack decorators in any combination |
| Extending further | Requires editing/extending the class hierarchy | Add a new decorator class, nothing else changes |