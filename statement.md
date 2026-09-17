# Problem Statement: Multithreaded Java Chat Application

## Problem Statement
In networked applications and distributed environments, facilitating concurrent, low-latency, real-time communication between multiple clients remains a foundational challenge. Standard single-threaded network implementations block execution during synchronous I/O operations, rendering them incapable of serving multiple concurrent users simultaneously. Without proper thread synchronization and non-blocking state management, multi-user applications risk race conditions, message delivery delays, data corruption, and connection drops.

This project addresses the requirement for a robust, multi-user real-time communication framework built using core Java socket networking and multithreading primitives.

---

## Scope of the Project
The project encompasses the end-to-end design, implementation, and verification of a multi-client client-server network architecture built using Java standard libraries.

### In Scope
* **Server-Side Architecture:** Implementation of a central server using `ServerSocket` capable of listening on a dedicated port and accepting concurrent incoming TCP connections.
* **Concurrency Management:** Dynamic creation of dedicated worker threads (or thread pool tasks) implementing `Runnable` to manage individual client sessions independently.
* **Synchronous Thread Dispatching:** Safe thread synchronization across shared collections to facilitate real-time message broadcasting to all active participants without race conditions.
* **Client-Side Interface:** Text-based command-line interface (CLI) enabling users to connect, transmit streams of text messages, and receive updates in real time.
* **Fault Tolerance & Connection Handling:** Graceful socket termination, resource cleanup (`BufferedReader`, `PrintWriter`, `Socket`), and exception handling during unexpected disconnections.

### Out of Scope
* Graphical User Interfaces (GUI) built with Swing, JavaFX, or external web frameworks.
* Persistent database storage (messages are kept transiently in memory during active runtime).
* End-to-end payload encryption (TLS/SSL configurations).

---

## Target Users
1. **Computer Science & Software Engineering Students:** Developers studying networking fundamentals, TCP/IP socket mechanics, stream I/O, and multi-core concurrency in Java.
2. **Systems Developers:** Engineers looking for a lightweight, modular prototype or template for building custom application-layer protocol servers.
3. **Internal Lab & Enterprise Teams:** Teams requiring a minimalistic, zero-dependency, local network messaging tool for internal text communication.

---

## High-Level Features
* **Multi-Client Concurrent Connection:** Supports simultaneous active connections without blocking thread execution or dropping inbound requests.
* **Real-Time Message Broadcasting:** Dispatches inbound client messages across all active client handlers asynchronously.
* **Thread-Safe Session Tracking:** Utilizes synchronized collections (e.g., `Collections.synchronizedSet` or `CopyOnWriteArrayList`) to safely add, track, and remove connected clients.
* **Graceful Disconnection & Cleanup:** Automatic detection of connection loss, notifying active users upon departure while reclaiming memory and socket resources.
* **Configurable Network Settings:** Simple configuration options for host address binding and target port allocation.
