# Bridge Design Pattern

## Definition
Bridge is a structural design pattern that **decouples an abstraction from its implementation**, so the two can vary and evolve independently. Instead of one deep inheritance hierarchy covering every combination of "what" and "how", Bridge splits it into two separate hierarchies connected by composition — the abstraction *holds a reference* to an implementation interface, rather than inheriting from a concrete implementation.

## Why do we need it?
- Without Bridge, combining two independent dimensions (e.g. player type × video quality) via inheritance alone requires a subclass for **every combination** — `SmartTvUltraHDPlayer`, `SmartTvHDPlayer`, `MobileSDPlayer`, etc. — a combinatorial explosion.
- Bridge lets each dimension (players, qualities) grow its own hierarchy independently — adding a new quality doesn't require touching player classes, and adding a new player doesn't require touching quality classes.
- Keeps abstraction (`VideoPlayer`) and implementation (`VideoQuality`) loosely coupled — either side can change without breaking the other.
- Enables runtime flexibility — the same player can be composed with different quality implementations, and vice versa.

## Prime Examples
- `VideoPlayer` (abstraction) + `VideoQuality` (implementation) here — any player can be combined with any quality
- JDBC drivers — the `java.sql` API (abstraction) is bridged to vendor-specific driver implementations (MySQL, PostgreSQL, etc.)
- Cross-platform GUI toolkits — a `Window` abstraction bridged to OS-specific rendering implementations (Windows, macOS, Linux)
- Remote controls (abstraction) bridged to different devices they control (TV, radio, projector — implementation)
- Shape + rendering engine combos (vector renderer vs raster renderer, applied to any shape)

---

## How it works in `StreamingApp.java`

**Implementation interface** (the "how" — video quality handling):
```java
interface VideoQuality {
    void load();
    void play();
}
```
Concrete implementations: `UltraHDQuality`, `HDQuality`, `SDQuality` — each knows how to load/play at its own quality level.

**Abstraction** (the "what" — a video player, holding a reference to a `VideoQuality`):
```java
abstract class VideoPlayer {
    protected VideoQuality quality;
    public VideoPlayer(VideoQuality quality) {
        this.quality = quality;
    }
    public abstract void load();
    public abstract void play();
}
```
This is the **bridge** — `VideoPlayer` doesn't implement `VideoQuality` or inherit from a concrete quality class; it simply **holds a reference** to whichever `VideoQuality` it was given.

**Refined abstractions** (different player types, each delegating to their held `quality`):
```java
class SmartTvPlayer extends VideoPlayer {
    public void load() { System.out.print("[Smart TV] "); quality.load(); }
    public void play() { System.out.print("[Smart TV] "); quality.play(); }
}
```
`MobilePlayer` and `WebPlayer` follow the same shape — each prefixes its own label, then delegates the actual load/play work to `quality`.

**Client (mixing and matching abstraction + implementation freely):**
```java
VideoPlayer tvPlayer = new SmartTvPlayer(new UltraHDQuality());
VideoPlayer mobilePlayer = new MobilePlayer(new SDQuality());
VideoPlayer webPlayer = new WebPlayer(new HDQuality());
```
Any of the 3 player types can be paired with any of the 3 quality types — **9 combinations from just 6 classes**, instead of needing 9 separate hardcoded subclasses.

---

## Pros
- Avoids combinatorial subclass explosion — M players × N qualities need only M+N classes, not M×N.
- Abstraction and implementation vary independently — add a new `VideoQuality` (e.g. `4KQuality`) without touching any player class, or add a new player (e.g. `ConsolePlayer`) without touching any quality class.
- Improves testability — implementations can be mocked/swapped independently of the abstraction.
- Runtime flexibility — the quality used by a player can even be swapped after construction if the abstraction exposes a setter.

## Cons
- Adds upfront design complexity — introducing two parallel hierarchies plus the bridge between them is more work than a single inheritance chain for simple cases.
- Can be harder to understand for developers unfamiliar with the pattern, since behavior is split across two classes joined by composition rather than living in one place.
- Overkill when there's only one dimension of variation (if quality never changes independently of player type, plain inheritance would suffice).

---

## Bridge vs Adapter (quick contrast)
| | Bridge | Adapter |
|---|---|---|
| Intent | Designed **upfront** to let two hierarchies evolve independently | Applied **after the fact** to make an existing incompatible class fit an interface |
| Relationship | Abstraction and implementation are both meant to vary | One side (the adaptee) is fixed/external and can't be changed |
| Example here | `VideoPlayer` × `VideoQuality`, both designed together | `RazorpayAdapter` retrofitted onto an existing `RazorpayAPI` |