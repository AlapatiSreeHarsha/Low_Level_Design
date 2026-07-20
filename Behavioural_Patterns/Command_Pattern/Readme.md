# Command Design Pattern

## What Is It?

Command is a **behavioral design pattern** that turns a request or an action into a **standalone object**. Instead of directly calling a method on an object, you wrap that call — along with everything it needs — inside a command object that exposes a uniform `execute()` (and optionally `undo()`) method.

This decouples the object that **triggers** an action from the object that actually **performs** it. The trigger doesn't need to know how the action is implemented — it just calls `execute()` on whatever command it's holding.

In the sample code, pressing a remote control button doesn't directly call `light.on()` — it calls `execute()` on a `Command` object, which internally knows to call `light.on()`. This indirection is what makes undo, queuing, and swappable button assignments possible.

## The Four Components

| Role | In This Code | Responsibility |
|---|---|---|
| **Client** | `Main` | Creates the receivers (`Light`, `AC`), wraps them in concrete commands (`LightOnCommand`, `ACOffCommand`, etc.), and assigns those commands to the invoker's slots. |
| **Invoker** | `RemoteControl` | Holds a reference to a `Command` (in `buttons[]`) and triggers it via `pressButton()`, without knowing what the command actually does. Also keeps a `commandHistory` stack to support `pressUndo()`. |
| **Command** | `Command` interface, `LightOnCommand`, `LightOffCommand`, `ACOnCommand`, `ACOffCommand` | Declares `execute()`/`undo()` and binds a specific receiver + action together. Each concrete command knows exactly which receiver method to call. |
| **Receiver** | `Light`, `AC` | The object that actually performs the real work (`on()`, `off()`). It knows nothing about commands, remotes, or undo — it just exposes simple operations. |

```java
remote.setCommand(0, new LightOnCommand(light)); // Client wires Command to Invoker slot
remote.pressButton(0);                           // Invoker calls execute() — doesn't know it's a Light
remote.pressUndo();                              // Invoker calls undo() on the last executed command
```

## What Happens Without the Command Pattern?

Without wrapping actions in command objects, `RemoteControl` would need to hard-code every possible action itself, typically with conditional logic like:

```java
public void pressButton(int slot) {
    if (slot == 0) light.on();
    else if (slot == 1) light.off();
    else if (slot == 2) ac.on();
    else if (slot == 3) ac.off();
    // ...grows with every new device/action
}
```

This causes several concrete problems:

- **RemoteControl becomes tightly coupled** to every specific device (`Light`, `AC`, and any future device), instead of depending on a single abstraction.
- **Adding a new button or device** means editing `RemoteControl` directly, violating the Open/Closed Principle.
- **Undo becomes very hard to implement cleanly** — you'd need to manually track "what was the opposite action" for every branch, rather than letting each command define its own `undo()`.
- **No easy way to queue, log, or replay actions**, since actions aren't objects — they're just inline method calls buried inside conditionals.
- **Reusability drops** — the same "turn light on" logic can't be handed to a scheduler, a macro button, or a voice assistant without duplicating the conditional logic elsewhere.

## When to Use It

- You need to support **undo/redo** functionality.
- You want to **queue, log, or schedule** operations for later execution.
- You want to **decouple** the object that invokes an action from the object(s) that perform it.
- You need to support **parameterizing objects with actions** — e.g., configurable buttons, menu items, or macros that can be assigned different behaviors at runtime.
- You want to combine several actions into a single **composite command** (e.g., a "goodnight" macro that turns off lights and AC together).

## Pros

- **Decouples invoker from receiver** — `RemoteControl` never needs to know about `Light` or `AC` directly.
- **Supports undo/redo naturally** — each command encapsulates both the action and its inverse.
- **Open/Closed Principle** — new commands (e.g., `TVOnCommand`) can be added without touching `RemoteControl`.
- **Enables queuing, logging, and macros** — since commands are just objects, they can be stored, replayed, or combined.
- **Simplifies the invoker** — no growing conditional chains as more devices/actions are added.

## Cons

- **More classes** — every action needs its own command class, which can feel like overkill for a small number of simple actions.
- **Increased indirection** — tracing what actually happens when a button is pressed requires following the chain from invoker → command → receiver, rather than reading one direct call.
- **Overhead for simple cases** — if undo/queuing/logging are never needed, the pattern adds complexity without a clear payoff.
- **Command explosion** — systems with many fine-grained actions can end up with a large number of near-identical command classes to maintain.