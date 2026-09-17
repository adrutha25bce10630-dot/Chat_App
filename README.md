# Java Multithreaded Chat App

A terminal-based chat application built with plain Java sockets and threads: one server, multiple clients, real-time broadcast messaging. No external dependencies — just the Java standard library.

## Features

- Multiple clients can connect and chat at the same time
- Join/leave notifications broadcast to everyone
- Each client runs on its own server-side thread (true concurrency, not polling)
- Simple `/quit` command to disconnect cleanly

## Project Structure

```
ChatApp/
├── ChatServer.java     # Accepts connections, spawns a thread per client
├── ClientHandler.java  # Handles one client's messages, broadcasts to all
├── ChatClient.java     # Connects to the server; sends + listens concurrently
└── README.md
```

## Requirements

- Java Development Kit (JDK) 8 or later
- No external libraries needed

## Getting Started

**1. Clone this repo and move into the project folder:**
git clone <your-repo-url>
cd ChatApp

**2. Compile:**
javac *.java

**3. Start the server** (in one terminal):
java ChatServer

Expected output:
```
Starting chat server on port 12345...
Server started. Waiting for clients...
```

**4. Start one or more clients** (each in its own terminal window):
java ChatClient

You'll be prompted for a username. Anything you type is broadcast to every
connected client. Type `/quit` to leave.

> To test locally with multiple "people," just open several terminal tabs and
> run `java ChatClient` in each — they'll all connect to `localhost:12345`.

## How It Works

- **`ServerSocket` / `Socket`** — the server opens a `ServerSocket` and blocks
  on `.accept()` until a client connects, producing a `Socket` to read/write.
- **One thread per client** — the server spawns a new `Thread` running a
  `ClientHandler` for every connection, so all clients are served concurrently
  instead of one at a time.
- **Thread-safe shared state** — every `ClientHandler` needs to reach every
  *other* connected client to broadcast messages. That shared list is a
  `CopyOnWriteArrayList` rather than a plain `ArrayList`, since multiple
  threads read and modify it concurrently.
- **Client-side concurrency** — a client needs to read your keyboard input
  *and* listen for incoming messages at the same time. One blocking call
  would freeze the other, so the client also runs two threads: one for input,
  one for listening.

## Configuration

Both the port and host are hardcoded for simplicity:

| File | Variable | Default |
|-------------------|------------------|-------------|
| `ChatServer.java` |      `PORT`      |   `12345`   |
| `ChatClient.java` | `SERVER_ADDRESS` | `localhost` |
| `ChatClient.java` |   `SERVER_PORT`  |   `12345`   |

Change these constants and recompile if you need a different port, or want
clients to connect to a remote server instead of `localhost`.

## Roadmap / Ideas for Extension

- [ ] Timestamps on messages
- [ ] Private messaging (`/msg <user> <message>`)
- [ ] `/list` command to show who's online
- [ ] Persist chat history to a file
- [ ] Swing or JavaFX GUI instead of the console
- [ ] TLS/SSL encrypted sockets

## Known Limitations

- No authentication — usernames aren't verified or reserved
- No message history for users who join late
- Plaintext over the network (not encrypted)

## License

Feel free to use, modify, and learn from this project.

*results included in Project_report.pdf
