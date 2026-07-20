# Singleton Design Pattern

## Definition
Singleton is a creational design pattern that restricts a class to a **single instance** and provides a **global point of access** to it.

## Why do we need it?
- Ensures only one object manages shared resources (config, connection pool, cache, logger).
- Saves memory/overhead of repeated object creation.
- Provides a controlled, consistent access point across the application.
- Prevents inconsistent state that could arise from multiple instances mutating shared data.

## Prime Examples
- Database connection pool manager
- Application configuration/settings manager
- Logger
- Thread pool
- Cache manager
- Runtime / driver managers (e.g. `java.lang.Runtime.getRuntime()`)

## Quick Gist — Eager vs Lazy vs Synchronized
| Aspect | Eager Loading | Lazy Loading | Synchronized |
|---|---|---|---|
| Instance created | At class loading | On first `getInstance()` call | On first `getInstance()` call |
| Thread safe | Yes | No | Yes |
| Performance | No locking overhead | No locking overhead (but unsafe) | Locking overhead on every call |
| Memory | Used even if never needed | Used only when needed | Used only when needed |

---

## 1. Eager Loading (`EagerLoading.java`)
Instance is created at class-loading time via a `static` field initializer.

```java
private static JudgeAnalysis instance = new JudgeAnalysis();
```

**Pros**
- Simple to implement.
- Thread-safe by default (JVM guarantees class loading is thread-safe).

**Cons**
- Instance is created even if it's never used → wastes memory/resources.
- No control over exception handling during instantiation.
- Not lazy — can slow down application startup if construction is expensive.

---

## 2. Lazy Loading (`LazyLoading.java`)
Instance is created only when `getInstance()` is first called.

```java
if(instance == null) {
    instance = new JudgeAnalysis();
}
```

**Pros**
- Instance created only when needed → saves resources.
- Simple, easy to read.

**Cons**
- **Not thread-safe**: two threads can simultaneously pass the `null` check and create two separate instances, breaking the singleton guarantee.
- Unsuitable for multithreaded applications as-is.

---

## 3. Synchronized Singleton (`SynchronizedSingleton.java`)
Lazy loading + `synchronized` keyword on the `getInstance()` method to fix the thread-safety issue.

```java
public static synchronized JudgeAnalysis getInstance() {
    if(instance == null) {
        instance = new JudgeAnalysis();
    }
    return instance;
}
```

**Pros**
- Thread-safe.
- Lazy — instance created only on demand.

**Cons**
- Every call to `getInstance()` acquires a lock, even after the instance already exists → unnecessary performance overhead in high-concurrency scenarios.

---

## 4. Double-Checked Locking Singleton (`DoubleCheckedLockingSingleton.java`)
Improves on synchronized singleton by only synchronizing the first time the instance is created, checking `null` twice, and using `volatile` to prevent instruction reordering issues.

```java
private static volatile JudgeAnalysis instance;

public static JudgeAnalysis getInstance() {
    if(instance == null) {
        synchronized (JudgeAnalysis.class) {
            if(instance == null) {
                instance = new JudgeAnalysis();
            }
        }
    }
    return instance;
}
```

**Pros**
- Thread-safe.
- Lazy loading.
- Better performance than plain synchronized version — locking only happens during first-time initialization.

**Cons**
- More complex, easy to get wrong (must use `volatile`, or risk subtle bugs from reordering/partially constructed objects).
- Slightly harder to read/maintain than other approaches.

---

## 5. Bill Pugh Singleton (`BillParghSingleton.java`)
Uses a static inner "Holder" class. The `INSTANCE` field is only initialized when `Holder` is loaded, which only happens when `getInstance()` is called — relying on the JVM's class-loading guarantees instead of explicit locks.

```java
public static class Holder{
    private static final JudgeAnalysis INSTANCE = new JudgeAnalysis();
}
public static JudgeAnalysis getInstance() {
    return Holder.INSTANCE;
}
```

**Pros**
- Thread-safe without any explicit synchronization (JVM handles it via class loading).
- Lazy — `Holder` class loads only on first `getInstance()` call.
- No locking overhead on repeated calls → best performance among lazy approaches.
- Clean, simple code.

**Cons**
- Slightly less intuitive for beginners — relies on understanding JVM class-loading semantics.

---

## Overall Comparison

| Approach | Thread-safe | Lazy | Performance | Complexity |
|---|---|---|---|---|
| Eager Loading | ✅ | ❌ | High (no locks) | Low |
| Lazy Loading | ❌ | ✅ | High (no locks, but unsafe) | Low |
| Synchronized | ✅ | ✅ | Low (locks every call) | Low |
| Double-Checked Locking | ✅ | ✅ | High (locks only once) | Medium/High |
| Bill Pugh (Holder) | ✅ | ✅ | High (no locks) | Low/Medium |

**Recommended approach:** Bill Pugh Singleton — it's thread-safe, lazy, has no synchronization overhead, and is straightforward to implement.