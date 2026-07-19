# Flyweight Design Pattern

## What Is It?

Flyweight is a **structural design pattern** that reduces memory usage when a program needs to create a huge number of similar objects. It works by splitting an object's state into two parts:

- **Intrinsic state** – the part that is shared across many objects and stored once.
- **Extrinsic state** – the part that is unique per object and supplied from outside at runtime.

Instead of creating a new object every time, a **Flyweight Factory** caches and reuses shared objects, handing out the same instance whenever the same intrinsic state is requested.

In the sample code, `TreeType` is the flyweight (shared, intrinsic data), `Tree` is the context object (holds the extrinsic data), and `TreeFactory` is the factory that caches `TreeType` instances.

## Intrinsic vs Extrinsic Attributes

| | Intrinsic (shared) | Extrinsic (unique) |
|---|---|---|
| **Definition** | State that is identical across many objects; stored inside the flyweight and never changes based on context | State that differs per object instance; stored outside the flyweight and passed in when needed |
| **Stored in** | The flyweight object itself (`TreeType`) | The context object (`Tree`) |
| **In this example** | `name`, `color`, `texture` | `x`, `y` (position on the map) |
| **Created** | Once per unique combination, cached and reused | Once per object, never shared |

```java
class TreeType {              // Flyweight — intrinsic state
    private String name;
    private String color;
    private String texture;
}

class Tree {                  // Context — extrinsic state
    private int x;
    private int y;
    private TreeType type;    // reference to shared flyweight
}
```

## Product Example (from your code)

A forest-rendering system needs to draw thousands of trees. Each tree has a position (`x, y`) but many trees share the same species data (`name`, `color`, `texture`).

- `TreeFactory` keeps a `Map<String, TreeType>` keyed by `name-color-texture`.
- When `plantTree()` is called, the factory returns an **existing** `TreeType` if one with that key already exists, otherwise it creates and caches a new one.
- Every `Tree` object only stores its coordinates plus a **reference** to the shared `TreeType` — not a full copy of the species data.

Result: planting 3 Oak trees creates only **one** `TreeType("Oak", "Green", "Rough")` object, reused by all 3 `Tree` instances.

## When to Use It

- You need to create a **very large number** of similar objects (thousands to millions).
- Object storage cost is high because of the amount of state each instance would otherwise carry.
- Most of an object's state can be made **extrinsic** and passed in from the caller.
- Object identity doesn't matter — many contexts can safely point to the same shared instance.
- Typical scenarios: rendering engines (trees, particles, tiles, characters), text editors (glyph/character formatting), game development (bullets, enemies), caching of immutable configuration objects.

## Pros

- **Reduces memory footprint** significantly when object count is large.
- **Centralizes shared state**, making it easier to manage and update common data.
- Works well with the **Factory pattern** to control object creation and caching.
- Can improve performance by avoiding repeated construction of identical objects.

## Cons

- **Adds complexity** — state must be split carefully between intrinsic and extrinsic, which isn't always natural.
- Extrinsic state has to be **recomputed or passed in** by the client every time, which can add CPU overhead in exchange for memory savings.
- Flyweight objects should be **immutable**; if shared state needs to change, it can unintentionally affect every context using that flyweight.
- Harder to debug/reason about since many contexts reference the same shared object rather than owning their own copy.

## Real-World Example

**Text editors / word processors:** Every character glyph on a page could theoretically be its own object carrying font, size, color, and style. Instead, editors like those used in word-processing software treat each *character style* (font family, size, weight, color) as a flyweight — shared and cached — while each individual character's *position* on the page (row, column) is extrinsic state supplied by the document layout. This is why documents with tens of thousands of characters don't require tens of thousands of fully independent style objects.

Other common real-world uses:
- **Game maps/tilesets** — reusing the same tile texture object across thousands of map cells, only varying the (x, y) grid position.
- **Browser rendering (DOM/CSS)** — shared CSS style objects applied to many DOM nodes.
- **Icon/font rendering libraries** — caching glyph shapes and reusing them at different screen positions and sizes.