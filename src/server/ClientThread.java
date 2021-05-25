package server;

import gameplay.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Locale;
import java.util.Random;

class ClientThread extends Thread implements Runnable {

    private final Socket socket;
    private final ServerSocket serverSocket;

    private Player currentPlayer = null;

    public ClientThread(Socket socket, ServerSocket serverSocket, List<ClientThread> clients) {
        this.socket = socket ;
        this.serverSocket = serverSocket;
    }

    public void run () {

        String clientDetails = "IP: "
                + socket.getInetAddress().getCanonicalHostName()
                + "\nPORT: "
                + socket.getPort()
                + "\n";

        System.out.println("Hello from:\n"
                + clientDetails);

        String welcomeAndCommands = """
                Welcome!
                 Available commands are the following:
                register name: adds a new person to the social network;
                login name: establishes a connection between the server and the client;
                logout: logs out the current user
                friend name1 name2 ... namek: adds friendship relations between the person that sends the command and other persons;
                send message: sends a message to all friends.
                read: reads the messages from the server.help: prints the command list again.
                """;

        try (
            DataInputStream in = new DataInputStream(
                    socket.getInputStream());
            DataOutputStream out = new DataOutputStream(
                    socket.getOutputStream())   ) {

            out.writeUTF("Hello! ...");

            String request;
            // Get the request from the input stream: client → server

            while (currentPlayer == null) {

                out.writeUTF("Tell me your name, please ...");

                request = in.readUTF();

                String name;
                if (request.trim().contains(" ")) {
                     name = request.trim().substring(0, request.trim().indexOf(" "));
                } else
                    name = request;

                out.writeUTF("Is your name '" + name + "' ? ( confirm with 'ok' )");
                String confirm =  in.readUTF();
                if (confirm.equals("ok")) {

                    Color color = null;

                    while (color == null) {
                        out.writeUTF("What color do you prefer, " + name + "? ( white/black/any )");
                        request = in.readUTF();
                        switch (request.trim().toUpperCase(Locale.ROOT)) {
                            case "WHITE" -> color = Color.WHITE;
                            case "BLACK" -> color = Color.BLACK;
                            case "ANY", "" -> color = (new Random().nextInt(2) == 1 ? Color.WHITE : Color.BLACK);
                        }
                    }

                    this.currentPlayer = new Player(name, false, color);

                    String mode = "";
                    while (!(mode.equals("PVP") || mode.equals("PVE") || mode.equals("PVM") )) {
                        out.writeUTF("'Player vs Player'(pvp) mode or 'Player vs Environment'(pve/pvm) mode ?...");
                        mode = in.readUTF().trim().toUpperCase(Locale.ROOT);
                        switch (mode) {
                            case "PVP" ->{
                                currentPlayer.setMode(Mode.PVP);
                                out.writeUTF("Okay," + currentPlayer.getName() + ", checking for opponents ...");
                            }
                            case "PVM", "PVE" ->{
                                Player computerOpponent = new Player("AI", true, (currentPlayer.getColor() == Color.BLACK ? Color.WHITE : Color.BLACK));
                                computerOpponent.setMode(Mode.PVE);
                                out.writeUTF("Okay, " + currentPlayer.getName() + ", game will start in moments ... ");
                            }
                        }
                    }


                }

            }

//            while (!request.equals("exit")) {
//
//                String response;
//                int i = request.indexOf(" ");
//
//                if ( i >= 1 ) {
//
//                    String command = request.substring(0, i);
//                    String arguments = request.substring(i);
//
//                    arguments = arguments.trim();
//
//                    response = "\nServer received the request ... " + "\ncommand: " + command + "\narguments: " + arguments;
//
//                    System.out.println(response);
//
//                    response = "Command '" + command + "' is not valid !!! ";
//
//                } else {
//
//                    response = switch (request.trim()) {
//                        case "help" -> helpCommand(welcomeAndCommands);
//                        default -> "Command '" + request + "' is not valid !!! ";
//                    };
//                }
//
//
//                System.out.println(response);
//
//                // Send the response to the output stream: server → client
//                out.writeUTF(response + "\n");
//                if (currentPlayer != null) {
//                    System.out.println("User is logged as '" + currentPlayer.getName() + "'. Waiting for commands ...");
//                } else {
//                    System.out.println("User is not logged in...");
//                }
//
//                out.flush();
//
//                request = in.readUTF();
//            }

            String response = "Server stopped !";
            if (currentPlayer != null)
                clientDetails = clientDetails + "logged as: " + currentPlayer.getName() + "\n";
            System.out.println(clientDetails + "stopped me !!!");
            out.writeUTF(response);
            out.flush();
            serverSocket.close();

        } catch (IOException e) {
            System.err.println("\nCommunication error for client: \n" + clientDetails + "\n" + e);
        }
    }

    private String helpCommand(String welcomeAndCommands) {

        return welcomeAndCommands;
    }

}