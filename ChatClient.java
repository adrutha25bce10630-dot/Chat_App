import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * ChatClient
 * ----------
 * Connects to the ChatServer and lets the user send/receive messages.
 *
 * Two threads are needed here because network I/O and console I/O
 * both block:
 *   - The main thread reads from the keyboard (Scanner) and sends to the server.
 *   - A background "listener" thread reads incoming messages from the
 *     server and prints them, so you can receive messages from others
 *     even while you're not actively typing.
 */
public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            // Background thread: continuously listen for incoming messages
            Thread listenerThread = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            listenerThread.setDaemon(true); // dies automatically when main thread exits
            listenerThread.start();

            System.out.println("Connected to chat server. Type /quit to exit.");
            String userInput;
            while (true) {
                userInput = scanner.nextLine();
                out.println(userInput);
                if (userInput.equalsIgnoreCase("/quit")) {
                    break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}
