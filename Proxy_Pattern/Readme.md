# Proxy Design Pattern

## Definition
Proxy is a structural design pattern that provides a **surrogate/placeholder object** which controls access to another (real) object. The proxy implements the same interface as the real object, so the client interacts with the proxy exactly as it would with the real thing — while the proxy adds extra logic (caching, access control, lazy creation, logging, etc.) around calls it forwards to the real object.

## Why do we need it?
- Sometimes you want to add behavior **around** access to an object — without changing that object's own code or the client's code.
- Real object creation/access might be expensive (network calls, large files, remote services) — a proxy can defer or optimize that work.
- Client code stays unaware that a proxy is even involved, since it depends only on the shared interface.
- Useful for cross-cutting concerns: caching, permission checks, lazy loading, logging, or bridging to a remote resource.

## Prime Examples
- `CacheVideoDownloader` here — proxies `RealVideoDownloader`, adding a caching layer
- Java's `java.rmi` proxies for Remote Method Invocation
- Hibernate/JPA lazy-loaded entity proxies
- Spring AOP proxies (transaction management, logging around bean method calls)
- `CDN` caching a website's real server responses
- Credit card as a "proxy" for a bank account (real-world analogy) — controls/mediates access to your actual funds

---

## How it works in `Main.java`

**Subject interface** (shared by both the real object and the proxy):
```java
interface VideoDownloader {
    void downloadVideo(String videoUrl);
}
```

**Real Subject** (the actual object doing real work):
```java
class RealVideoDownloader implements VideoDownloader {
    public void downloadVideo(String videoUrl) {
        System.out.println("Downloading video from: " + videoUrl);
    }
}
```

**Proxy** (implements the same interface, wraps the real object, adds caching):
```java
class CacheVideoDownloader implements VideoDownloader {
    private RealVideoDownloader realVideoDownloader;
    private HashMap<String, String> cache = new HashMap<>();

    public void downloadVideo(String videoUrl) {
        if (cache.containsKey(videoUrl)) {
            System.out.println("Video already downloaded. Fetching from cache: " + videoUrl);
        } else {
            realVideoDownloader.downloadVideo(videoUrl);
            cache.put(videoUrl, videoUrl);
        }
    }
}
```
`CacheVideoDownloader` checks its cache first. On a cache miss, it delegates to `RealVideoDownloader.downloadVideo()` and stores the result for next time. On a cache hit, the real object is never even called.

**Client:**
```java
CacheVideoDownloader cacheVideoDownloader = new CacheVideoDownloader();
cacheVideoDownloader.downloadVideo("http://example.com/video1");
```
The client calls `downloadVideo()` exactly as it would on `RealVideoDownloader` directly — it has no idea caching logic is happening in between.

---

## Types of Proxy

### 1. Virtual Proxy
- Controls access to an object that is **expensive to create**, delaying its creation until it's actually needed (lazy initialization).
- Example: a proxy for a large image that only loads the actual image file from disk the first time it needs to be displayed.

### 2. Protection Proxy
- Controls **access rights** to the real object — checks permissions/roles before forwarding a request.
- Example: a proxy in front of a document editor that only allows `save()` calls from users with "editor" role, blocking others.

### 3. Remote Proxy
- Represents an object that lives in a **different address space** (another process/machine), hiding the networking details from the client.
- Example: RMI stubs, gRPC client stubs — calling a method on the proxy transparently makes a network call to a remote server.

### 4. Smart (Reference) Proxy
- Adds extra housekeeping actions around access to the real object — reference counting, logging, caching, locking, etc.
- `CacheVideoDownloader` in this code is a **Smart Proxy** — it adds caching behavior around the real downloader without changing the client's interaction at all.

| Type | Adds | Example use case |
|---|---|---|
| Virtual | Lazy/deferred creation | Loading a large image only when displayed |
| Protection | Access control | Role-based permission checks |
| Remote | Network transparency | RMI/gRPC stubs |
| Smart | Extra bookkeeping (caching, logging, counting) | `CacheVideoDownloader` here |

---

## Pros
- Adds behavior (caching, access control, lazy loading) transparently, without modifying the real subject or the client.
- Client code stays decoupled — it only depends on the shared interface.
- Can improve performance significantly (e.g. avoiding repeated expensive downloads, as in this example).
- Follows Single Responsibility — caching logic lives in the proxy, not mixed into `RealVideoDownloader`.

## Cons
- Adds an extra layer of indirection — every call passes through the proxy first, which can add latency for logic-heavy proxies.
- Can make debugging harder, since the object the client "sees" isn't the one actually doing the work.
- If not carefully designed, keeping the proxy and real subject's interfaces in sync as the interface evolves adds maintenance overhead.
- This example's cache grows unbounded (`HashMap` with no eviction) — a real implementation would need a cache-invalidation/size-limiting strategy.