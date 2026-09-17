import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ChatServer
 * ----------
 * Listens on a TCP port. For every client that connects, it spins up
 * a dedicated thread (ClientHandler) so multiple clients can chat at
 * the same time without blocking each other.
 *
 * The `clients` list is shared across all handler threads, so it must
 * be thread-safe. CopyOnWriteArrayList is a simple, safe choice here
 * because reads (broadcasting) happen far more often than writes
 * (a client joining/leaving).
 */
public class ChatServer {
    private static final int PORT = 12345;

    // Thread-safe list of all currently connected client handlers
    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        System.out.println("Starting chat server on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                // accept() blocks until a client connects
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection from: "
                        + clientSocket.getInetAddress().getHostAddress());

                ClientHandler handler = new ClientHandler(clientSocket, clients);
                clients.add(handler);

                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
