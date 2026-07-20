# Composite Design Pattern

## Definition
Composite is a structural design pattern that lets you compose objects into **tree structures** to represent part-whole hierarchies. It lets clients treat **individual objects** (leaves) and **compositions of objects** (containers/composites) **uniformly**, through a common interface.

## Why do we need it?
- Real-world data is often hierarchical — a cart item might be a single product, or a bundle containing other products *and* other bundles, nested to any depth.
- Without Composite, client code would need `instanceof` checks and separate handling for "single item" vs "group of items" everywhere.
- Composite lets the client call the same method (`getPrice()`, `displayItem()`) on a single product or an entire nested bundle, without caring which one it is — the object handles it correctly either way.
- Makes it trivial to add new nesting levels (bundles of bundles of bundles) without changing any client code.

## Prime Examples
- `Product` and `ProductBundle` here — a bundle can contain products *and* other bundles
- File system: files (leaves) and folders (composites containing files/other folders)
- UI component trees: a `Button` (leaf) vs a `Panel` (composite containing buttons/other panels)
- Organization charts: individual employees vs departments containing employees/sub-departments
- XML/HTML DOM trees: text nodes (leaves) vs element nodes (composites containing child nodes)

---

## How it works in `Cart.java`

**Component interface** (common contract for both leaf and composite):
```java
interface CartItem {
    double getPrice();
    void displayItem();
}
```

**Leaf** (a simple, indivisible object — has no children):
```java
class Product implements CartItem {
    private String name;
    private double price;
    public double getPrice() { return price; }
    public void displayItem() { System.out.println("Product: " + name + ", Price: " + price); }
}
```

**Composite** (a container that holds a list of `CartItem` — which can be more `Product`s *or* more `ProductBundle`s):
```java
class ProductBundle implements CartItem {
    private List<CartItem> items;

    public void addItem(CartItem item) { items.add(item); }

    public double getPrice() {
        double totalPrice = 0;
        for (CartItem item : items) {
            totalPrice += item.getPrice();   // works for Product OR nested ProductBundle
        }
        return totalPrice;
    }

    public void displayItem() {
        System.out.println("Product Bundle: " + bundleName);
        for (CartItem item : items) {
            item.displayItem();   // recurses automatically into nested bundles
        }
    }
}
```
The key detail: `ProductBundle.getPrice()`/`displayItem()` call `item.getPrice()`/`item.displayItem()` on each child **without checking whether that child is a `Product` or another `ProductBundle`**. Because both implement `CartItem`, the call works either way — and if the child happens to be a `ProductBundle`, its own loop recurses further down the tree automatically.

**Client (building a nested tree):**
```java
ProductBundle bundle1 = new ProductBundle("Office Setup");   // Laptop, Mouse, Keyboard
ProductBundle bundle2 = new ProductBundle("Gaming Setup");
bundle2.addItem(bundle1);      // a bundle inside a bundle!
bundle2.addItem(product4);     // a plain product too

System.out.println("Total Price of Gaming Setup: " + bundle2.getPrice());
bundle2.displayItem();
```
`bundle2.getPrice()` transparently sums `bundle1`'s total (which itself sums its 3 products) plus `product4`'s price — the client never needs to know or care how deep the nesting goes.

---

## Pros
- Uniform treatment of individual objects and groups — client code doesn't need `instanceof`/type-checking branches.
- Naturally supports arbitrarily deep nesting (bundles of bundles) with zero extra code.
- Open/Closed Principle: new `CartItem` types can be added without changing `ProductBundle`'s traversal logic.
- Simplifies client code significantly — one method call (`getPrice()`) works no matter how complex the tree underneath is.

## Cons
- Can make the design overly general — it may become hard to restrict what kinds of children a composite is allowed to contain (e.g. nothing stops a bundle from containing itself, causing infinite recursion).
- Leaf classes (`Product`) are forced to implement the full `CartItem` interface even though some methods might not conceptually apply to leaves in more complex use cases (e.g. an `addItem()` method on the interface would be meaningless for a `Product`) — here it's avoided by keeping `addItem()` only on `ProductBundle`, which is good practice.
- Debugging deeply nested trees can be harder, since a single `getPrice()` call may fan out into many recursive calls.

---

## Composite vs Decorator (quick contrast)
Both wrap a common interface and use recursive delegation, but for different goals:

| | Composite | Decorator |
|---|---|---|
| Purpose | Represent part-whole **hierarchies** (tree of many children) | Add **behavior/responsibilities** to one object at a time |
| Structure | One node can have **many** children | One wrapper has exactly **one** wrapped object |
| Example | `ProductBundle` containing many `CartItem`s | `ExtraCheese` wrapping one `Pizza` |