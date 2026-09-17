import java.io.*;
import java.net.*;
import java.util.List;

/**
 * ClientHandler
 * -------------
 * Represents one connected client, running on its own thread.
 * Responsible for:
 *   1. Reading the username on connect.
 *   2. Reading chat messages from that client and broadcasting them.
 *   3. Sending messages (from other clients) back to this client.
 *   4. Cleaning up when the client disconnects.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final List<ClientHandler> clients;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter your username:");
            username = in.readLine();

            if (username == null || username.trim().isEmpty()) {
                username = "Anonymous" + socket.getPort();
            }

            broadcast("SERVER: " + username + " has joined the chat!");
            System.out.println(username + " connected.");

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("/quit")) {
                    break;
                }
                broadcast(username + ": " + message);
            }

        } catch (IOException e) {
            System.err.println("Connection error with "
                    + (username != null ? username : "unknown client") + ": " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    /** Sends a message to every connected client. */
    private void broadcast(String message) {
        System.out.println(message);
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    /** Sends a message to just this client. */
    public void sendMessage(String message) {
        out.println(message);
    }

    /** Removes this client from the shared list and closes its socket. */
    private void disconnect() {
        clients.remove(this);
        broadcast("SERVER: " + username + " has left the chat.");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
        System.out.println(username + " disconnected.");
    }
}
