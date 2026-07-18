# Builder Design Pattern

## Definition
Builder is a creational design pattern that lets you construct complex objects **step by step**. It separates the construction of an object from its representation, so the same construction process can produce different configurations of the object — without a giant constructor or many overloaded constructors.

## Why do we need it?
- Some objects have many fields — some required, many optional (e.g. a meal with bun, patty, toppings, cheese, side, drink).
- Building such objects with plain constructors either forces callers to pass `null`/defaults for every unused field, or forces the class author to write dozens of overloaded constructors.
- Builder gives a readable, fluent, step-by-step way to construct the object, setting only the fields that matter.
- Keeps the constructed object **immutable** (all fields `final`) while still allowing flexible, optional configuration.

## Prime Examples
- `BurgerMeal` here — required `bunType` + `pattyType`, optional toppings/cheese/side/drink
- `StringBuilder` / `StringBuffer` in Java
- `AlertDialog.Builder` in Android
- `Lombok`'s `@Builder` annotation
- HTTP request builders (`OkHttpClient.Builder`, `HttpRequest.Builder`)
- SQL query builders

---

## The Telescoping Constructor Anti-Pattern
Before Builder, a common (bad) approach is the **telescoping constructor** — a chain of overloaded constructors, each adding one more parameter:

```java
public BurgerMeal(String bunType, String pattyType) { ... }
public BurgerMeal(String bunType, String pattyType, String cheese) { ... }
public BurgerMeal(String bunType, String pattyType, String cheese, String side) { ... }
public BurgerMeal(String bunType, String pattyType, String cheese, String side, String drink) { ... }
public BurgerMeal(String bunType, String pattyType, String cheese, String side, String drink, ArrayList<String> toppings) { ... }
```

**Problems with telescoping constructors:**
- Becomes unreadable fast — a call like `new BurgerMeal("Sesame Bun", "Chicken Patty", null, "Fries", null, toppings)` gives no clue what each argument means.
- Easy to accidentally swap two `String` arguments of the same type (e.g. `side` and `drink`) — compiler won't catch it.
- Doesn't scale: N optional fields can require up to 2^N constructor combinations to cover every case.
- Adding a new optional field means adding yet another constructor overload, or breaking existing ones.

**Builder solves this** by replacing the constructor explosion with named, chainable methods:

```java
BurgerMeal meal = new BurgerMeal.Builder("Sesame Bun", "Chicken Patty")
        .addTopping("Lettuce")
        .addTopping("Tomato")
        .addCheese("Cheddar")
        .addSide("Fries")
        .addDrink("Coke")
        .build();
```

Every call is self-documenting — `addCheese("Cheddar")` can't be confused with `addDrink("Coke")` even though both take a `String`.

---

## How it works in `BurgerMeal.java`
- `BurgerMeal` has a **private constructor** that only accepts a `Builder` — objects can only be created through the builder, never directly.
- `Builder` holds the same fields as the outer class:
  - Required fields (`bunType`, `pattyType`) are passed into the `Builder`'s own constructor and marked `final`, so they must be supplied up front.
  - Optional fields (`toppings`, `cheese`, `side`, `drink`) have dedicated `addX()` methods.
- Each `addX()` method sets the field and **returns `this`**, enabling the fluent/chained style.
- `build()` finally constructs the immutable `BurgerMeal` from the accumulated builder state.

---

## Pros
- Readable, self-documenting object construction (fluent API).
- Handles optional parameters cleanly without constructor overload explosion.
- Produces immutable objects — `BurgerMeal`'s fields are all `final`, set once via the builder.
- Construction logic is isolated in `Builder`, keeping `BurgerMeal` itself simple.
- Required fields can still be enforced (via the `Builder`'s own constructor), while optional ones stay flexible.

## Cons
- More boilerplate — an extra `Builder` class duplicating the fields of the outer class.
- Slightly more verbose for simple objects with few fields, where a plain constructor would be sufficient.
- Builder object itself is mutable during construction, so care is needed in concurrent contexts (though the final built object is immutable).
- Overusing Builder for classes with only 1–2 fields adds unnecessary indirection.