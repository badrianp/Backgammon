
package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServer {
    private static final int PORT = 8100;
    private static List<ClientThread> clients;
    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    public SimpleServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!serverSocket.isClosed()) {
                while (clients.size() < 2) {
                    System.out.println((clients.size() == 0 ? "Waiting for players ... " : "Waiting for opponent  ..."));
                    Socket socket = serverSocket.accept();
                    // Execute the client's request in a new thread
                    ClientThread clientThread = new ClientThread(socket, serverSocket, clients);
                    clients.add(clientThread);

                    pool.execute(clientThread);
                }
            }
        } catch (IOException e) {
            System.err.println("Ops... " + e);
        }
    }


    public static void main(String[] args) {

        SimpleServer simpleServer = new SimpleServer();
    }

}

