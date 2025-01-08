package org.example.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Thread;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run()
    {
        try {
            while (true) {
                Response messageReceived = (Response) objectInputStream.readObject();
                System.out.println("Client: " + messageReceived);

                String responseMessage = respond(messageReceived.getMessage());
                Response response = new Response();
                response.setMessage(responseMessage);

                objectOutputStream.writeObject(response);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.err.println("Error during running: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void close() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
            if (objectOutputStream != null) objectOutputStream.close();
            if (objectInputStream != null) objectInputStream.close();
        } catch (IOException e) {
            System.err.println("Error while closing resources: " + e.getMessage());
        }
    }

    private static String respond(String message) {
        switch (message.toLowerCase()) {
            case "hello": return "world";
            case "bye" : return "bye";
            case "george" : return "droyd";
            default: return "i didn't catch that";
        }
    }
}
