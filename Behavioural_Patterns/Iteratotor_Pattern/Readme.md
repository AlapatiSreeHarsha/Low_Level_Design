# Iterator Design Pattern

## What Is It?

Iterator is a **behavioral design pattern** that provides a way to access the elements of a collection sequentially **without exposing its underlying representation** (array, list, linked list, etc.).

Instead of the client reaching into a collection's internals to loop over it, the collection hands out an **iterator object** that knows how to walk through the elements one at a time. The client only ever talks to the iterator through a small, standard interface: "is there a next element?" and "give me the next element."

In the sample code:

- `Video` is the **element** being iterated over.
- `PlaylistIterator` is the **iterator interface** (`hasNext()`, `next()`).
- `YouTubePlaylistIterator` is the **concrete iterator** that tracks position and walks the video list.
- `Playlist` is the **aggregate interface**, exposing `createIterator()`.
- `YouTubePlaylist` is the **concrete aggregate** that stores the videos and produces an iterator on request.

```java
PlaylistIterator iterator = playlist.createIterator();

while (iterator.hasNext()) {
    Video video = iterator.next();
    System.out.println(video.getTitle());
}
```

The client (`Main`) never touches `List<Video>` directly — it only knows about `hasNext()` and `next()`. This means `YouTubePlaylist` could swap its internal storage from an `ArrayList` to a `LinkedList` or a database cursor without breaking any client code.

## When to Use It

- You need to traverse a collection **without exposing** its internal structure (array, list, tree, etc.).
- You want to support **multiple simultaneous traversals** of the same collection (each iterator keeps its own position).
- You want a **uniform way to iterate** over different collection types through a common interface.
- You want to decouple traversal logic from the collection itself, so the collection can change its storage mechanism freely.
- Common scenarios: custom collection classes, traversing tree/graph structures, paginated API results, streaming large datasets where loading everything into memory isn't practical.

## Pros

- **Encapsulation** — the collection's internal data structure stays hidden from the client.
- **Single Responsibility** — traversal logic is separated from the collection's storage/management logic.
- **Supports multiple traversals** — several iterators can operate independently on the same collection at once.
- **Uniform interface** — different collection types can be iterated the same way, making client code reusable.
- Easy to add **new traversal strategies** (e.g., reverse iterator, filtered iterator) without modifying the collection class.

## Cons

- **Extra classes/overhead** — for simple collections, adding an iterator interface and concrete iterator can feel like unnecessary boilerplate compared to just looping over an array.
- **Slight performance cost** — going through an abstraction layer (`hasNext()`/`next()` calls) is typically a bit slower than direct index access.
- If the underlying collection is **modified during iteration** (elements added/removed), the iterator can become inconsistent or throw errors unless specifically designed to handle it.

## Real-World Example

**Java's own Collections Framework** is the most direct real-world example. Every `List`, `Set`, and `Map` in Java implements `Iterable`, exposing an `iterator()` method that returns an `Iterator<T>` with `hasNext()` and `next()` — exactly the shape used in this playlist example. This is also what powers Java's enhanced for-loop (`for (Video v : videos)`) under the hood.

Other common real-world uses:
- **Streaming services** — a playlist or queue (Spotify, YouTube) iterating through tracks/videos one at a time without loading the entire catalog into memory.
- **Database cursors** — SQL query results are iterated row-by-row through a cursor rather than loading the entire result set at once.
- **File readers** — reading a large file line-by-line (e.g., `BufferedReader`) without loading the whole file into memory.
- **UI component trees** — traversing a tree of UI widgets (e.g., DOM tree traversal) via an iterator instead of directly manipulating the tree structure.