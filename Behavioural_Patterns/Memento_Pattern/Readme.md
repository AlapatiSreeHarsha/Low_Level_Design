# Memento Design Pattern

## What Is It?

Memento is a **behavioral design pattern** that lets you **capture and externalize an object's internal state** so it can be restored later, all **without violating encapsulation**. The object's private fields are never exposed directly to the outside world — instead, a snapshot object (the memento) is handed out, opaque to everyone except the object that created it.

This is the classic mechanism behind **undo/redo** functionality: save a snapshot before a change, and if needed, roll back to it later.

In the sample code:

- `Memento` is the **memento** — an immutable snapshot holding a copy of a resume's `name`, `education`, `experience`, and `skills`.
- `ResumeEditor` is the **originator** — the object whose state is being tracked. It knows how to create a `Memento` of itself (`save()`) and how to restore its state from one (`restore()`).
- `ResumeHistory` is the **caretaker** — it stores mementos (in a `Stack<Memento>`) but never looks inside them or modifies their contents; it just keeps them safe and hands them back to the originator on request.

```java
public Memento save() {
    return new Memento(name, education, experience, skills);  // snapshot, defensively copied
}

public void restore(Memento memento) {
    name = memento.getName();
    education = memento.getEducation();
    experience = memento.getExperience();
    skills = memento.getSkills();
}
```

```java
history.save(editor);          // caretaker asks the originator for a snapshot and stores it
editor.setExperience("2 Years");
history.undo(editor);          // caretaker hands the last snapshot back for the originator to restore
```

`ResumeHistory` never reads `memento.getName()` or `getSkills()` itself — it just pushes and pops `Memento` objects. All the meaningful state is handled entirely by `ResumeEditor`, keeping `Memento`'s internals fully encapsulated from the caretaker.

## Real-World Example

**Resume/document editors with version history** (what this code models) — writing tools that let you save checkpoints of a document and roll back to an earlier version without exposing the document's internal representation to the history-tracking mechanism.

Other common real-world uses:
- **Undo/redo in text editors and IDEs** — every keystroke or edit can be checkpointed as a memento, letting Ctrl+Z step backward through history.
- **Video game save states** — a game snapshots player position, inventory, and progress into a save file (memento) that can be reloaded later.
- **Database transactions** — a transaction can be rolled back to a savepoint, restoring the prior state if something goes wrong mid-transaction.
- **Form wizards** — a multi-step form can snapshot the user's inputs at each step so they can navigate "back" without losing previously entered data.

## When to Use It

- You need to implement **undo/redo** or "restore to a previous state" functionality.
- You want to save snapshots of an object's state **without exposing its internal structure** to the code that manages those snapshots.
- The object's state is **complex enough** that manually tracking and reversing individual changes would be error-prone.
- You want a clean separation between **who owns the state** (originator) and **who manages the history** (caretaker).

## Pros

- **Preserves encapsulation** — `ResumeHistory` never accesses `Memento`'s internal fields directly; only `ResumeEditor` (the originator) can interpret it.
- **Simplifies undo/redo** — restoring state is as simple as handing a stored snapshot back to the originator.
- **Clean separation of responsibilities** — the originator owns state logic, the caretaker owns storage/history logic, and neither needs to know the other's internals.
- **Snapshots are self-contained** — since `Memento` stores defensive copies (like `new ArrayList<>(skills)`), later changes to the originator don't corrupt saved history.

## Cons

- **Memory overhead** — storing a full snapshot for every save can be expensive if the state is large or saves happen frequently.
- **Caretaker can grow unbounded** — without a cap on `history`, a `Stack<Memento>` (or similar) can keep accumulating snapshots indefinitely.
- **Snapshot creation cost** — deep-copying state (e.g., copying the `skills` list) on every `save()` adds overhead, especially for large or nested objects.
- **Not ideal for streaming/very frequent changes** — capturing a full snapshot on every tiny modification (e.g., every keystroke) can become impractical compared to tracking incremental diffs.