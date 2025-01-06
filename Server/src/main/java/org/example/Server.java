package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int SERVER_PORT = 9797;

    void run() {
        try (
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        ){
            while (true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("Another client connected");
                ClientThread clientThread = new ClientThread(socket);
                clientThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
