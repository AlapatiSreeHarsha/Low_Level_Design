# Prototype Design Pattern

## Definition
Prototype is a creational design pattern that creates new objects by **copying (cloning) an existing object** — called the prototype — instead of instantiating a class from scratch via `new`. The object being cloned defines its own copying logic (usually via a `clone()` method).

## Why do we need it?
- Some objects are expensive to create (heavy initialization, DB/network calls, complex setup) — cloning an already-built prototype is cheaper than rebuilding from zero.
- Lets you produce many similar-but-slightly-different objects from a common base without repeating the setup logic.
- Decouples the client from concrete classes — the client just asks the prototype/registry for a copy, without knowing (or calling `new`) on the concrete class.
- Useful when the set of possible object states is only known at runtime (e.g. pre-registered templates), rather than fixed at compile time.

## Prime Examples
- `EmailRegistry` here — clones a pre-registered `WelcomeEmailTemplate` for each new recipient instead of rebuilding it
- `Object.clone()` in Java / `Cloneable` interface
- Copying game entities/characters that share a base configuration
- Document/config templates (start from a template object, then customize the copy)
- `git clone` — conceptually, copying an existing repo as a starting point

---

## How it works in `EmailTemplateRegistry.java`

```java
interface EmailTemplate {
    void setContent(String content);
    void sendEmail();
    EmailTemplate clone();
}
```
`EmailTemplate` declares `clone()` — every concrete template knows how to copy itself.

```java
class WelcomeEmailTemplate implements EmailTemplate {
    private String userName;
    ...
    public EmailTemplate clone() {
        WelcomeEmailTemplate clone = new WelcomeEmailTemplate();
        clone.setContent(this.userName);
        return clone;
    }
}
```
`clone()` manually builds a new `WelcomeEmailTemplate` and copies over `userName`.

```java
class EmailRegistry {
    private final HashMap<String,EmailTemplate> emailTemplates = new HashMap<>();
    public void registerTemplate(String templateName, EmailTemplate template) {
        emailTemplates.put(templateName, template);
    }
    public EmailTemplate getTemplate(String templateName) {
        EmailTemplate template = emailTemplates.get(templateName);
        return template != null ? template.clone() : null;
    }
}
```
`EmailRegistry` is a **prototype registry** — it stores one prototype per template name, and `getTemplate()` always hands back a *clone*, never the original. That's why `email1` and `email2` in `main()` can each be customized (`"Harsha"`, `"Sam"`) independently without one overwriting the other.

---

## Shallow Copy vs Deep Copy
This is the core detail that makes or breaks a Prototype implementation.

### Shallow Copy
- Copies the object's own fields directly.
- If a field is a **reference type** (another object, array, list, map), the copy gets a reference to the **same underlying object** as the original — not a new one.
- Changing a mutable field through the clone will also affect the original (and vice versa).

```java
// Shallow copy example
class Order {
    List<String> items;
    Order clone() {
        Order copy = new Order();
        copy.items = this.items;  // same List reference shared by both!
        return copy;
    }
}
```
If `copy.items.add(...)` is called, `original.items` changes too — because both point to the same `List` object.

### Deep Copy
- Copies the object's own fields **and** recursively copies every mutable referenced object too.
- Original and clone become fully independent — no shared mutable state.

```java
// Deep copy example
class Order {
    List<String> items;
    Order clone() {
        Order copy = new Order();
        copy.items = new ArrayList<>(this.items);  // new List, same contents
        return copy;
    }
}
```

### Where does `WelcomeEmailTemplate.clone()` fall?
`userName` is a `String`, which is **immutable** in Java — so copying the reference (as `setContent(this.userName)` effectively does) behaves safely like a deep copy in practice; there's no shared mutable state to worry about. This is technically a shallow copy of an immutable field, so it causes no aliasing bugs.

**However**, if `WelcomeEmailTemplate` later gained a mutable field — e.g. a `List<String> attachments` — a shallow `clone()` would make `email1` and `email2` share the same `attachments` list. Modifying one clone's attachments would silently corrupt the other. In that case `clone()` would need to deep-copy that field:
```java
clone.attachments = new ArrayList<>(this.attachments);
```

---

## Pros
- Avoids repeating expensive/complex object construction.
- Adds new "kinds" of objects at runtime by registering new prototypes, without touching client code.
- Client stays decoupled from concrete classes — works purely through the `EmailTemplate` interface.
- Registry pattern (as used here) makes management of multiple templates simple and centralized.

## Cons
- Cloning logic must be implemented carefully for every class — easy to introduce shallow-copy bugs when mutable fields are added later.
- Deep-copying complex object graphs (circular references, nested objects) can get tricky and expensive.
- Java's built-in `Object.clone()`/`Cloneable` is notoriously awkward and error-prone; many teams prefer manual copy constructors or copy methods (as done here) instead.
- Every class in the hierarchy needs to correctly implement/override `clone()` — a missed override can silently produce shallow copies where deep copies were needed.