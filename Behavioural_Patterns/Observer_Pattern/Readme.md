# Observer Design Pattern

## What Is It?

Observer is a **behavioral design pattern** that defines a **one-to-many dependency** between objects, so that when one object (the **subject**) changes state, all the objects that depend on it (the **observers**) are notified and updated automatically.

The subject doesn't need to know any details about its observers — only that they all follow a common interface. This decouples the object that produces an event from the objects that react to it.

In the sample code:

- `Subscriber` is the **observer interface**, defining `update(videoTitle)`.
- `EmailSubscriber` and `MobileAppSubscriber` are **concrete observers**, each reacting to a notification differently.
- `Channel` is the **subject interface**, defining `subscribe()`, `unsubscribe()`, and `notifySubscribers()`.
- `YouTubeChannel` is the **concrete subject**, maintaining a list of subscribers and notifying all of them whenever `uploadVideo()` is called.

```java
channel.subscribe(s1);
channel.subscribe(s2);
channel.uploadVideo("Observer Design Pattern"); // notifies every subscriber automatically
```

`YouTubeChannel` never needs to know whether a subscriber is an email address or a mobile app — it just loops through its `List<Subscriber>` and calls `update()` on each one.

## Real-World Example

**YouTube's own subscription/notification system** (which is literally what this code models) is a textbook Observer pattern: when a channel uploads a new video, every subscriber gets notified through whatever channel they signed up for (email, push notification, app badge) — without the channel needing to know how each notification is delivered.

Other common real-world uses:
- **GUI event listeners** — a button click (subject) notifies all registered listeners (observers) like click handlers.
- **Stock market tickers** — a stock price (subject) notifies all subscribed trading dashboards/apps when the price changes.
- **Model-View architectures (MVC/MVVM)** — the data model notifies views to re-render whenever underlying data changes.
- **Pub/Sub messaging systems** — message brokers (e.g., Kafka topics) notify all consumers subscribed to a topic.
- **Spreadsheet formulas** — a cell change (subject) automatically recalculates and updates all dependent cells (observers).

## When to Use It

- Multiple objects need to react to a **state change or event** in another object, and the set of reacting objects can vary at runtime.
- You want to **decouple** the object generating events from the objects handling them.
- The number of observers isn't known in advance, or needs to change dynamically (subscribe/unsubscribe at runtime).
- You want to support **broadcast communication** — one change, many independent reactions.

## When to Avoid It

- There's only ever **one** dependent object — a direct method call is simpler and clearer than the overhead of an observer setup.
- **Notification order matters** and needs to be strictly guaranteed — Observer doesn't inherently guarantee ordering, which can cause subtle bugs if consumers assume one.
- Observers **modify the subject's state** during notification — this can trigger cascading or recursive updates that are hard to trace and debug.
- You need **tight performance guarantees** — notifying a large number of observers synchronously can become a bottleneck; an event bus or async queue may fit better.
- The relationship between subject and observers is **static and simple** — introducing the pattern adds indirection that isn't earning its keep.

## Pros

- **Loose coupling** — the subject only depends on the observer interface, not on concrete observer classes.
- **Open/Closed Principle** — new observer types (e.g., a future `SmsSubscriber`) can be added without changing `YouTubeChannel` at all.
- **Dynamic relationships** — observers can subscribe and unsubscribe at runtime, as shown when `s3` unsubscribes mid-program.
- **Broadcast communication** — one event can trigger many independent reactions with a single method call (`notifySubscribers`).

## Cons

- **Unexpected update chains** — if an observer's `update()` triggers further changes, it can cause cascading notifications that are hard to trace.
- **No guaranteed order** — subscribers are typically notified in an unspecified or insertion order, which can be a problem if execution order matters.
- **Memory leaks risk** — forgetting to `unsubscribe()` observers that are no longer needed keeps them alive and referenced by the subject.
- **Harder debugging** — because the coupling is indirect (through an interface), tracing "who reacts to what" isn't as obvious as with direct method calls.