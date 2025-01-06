package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 9797;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public void start() {
        try {
            this.socket = new Socket(SERVER_HOST, SERVER_PORT);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String messageToSend = scanner.nextLine();
                Response response = new Response();
                response.setMessage(messageToSend);

                this.objectOutputStream.writeObject(response);

                Response messageResponse = (Response) this.objectInputStream.readObject();
                System.out.println("Server: " + messageResponse);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
