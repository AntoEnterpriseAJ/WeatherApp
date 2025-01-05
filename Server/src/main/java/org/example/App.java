package org.example;

import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class App
{
    public static String respond(String message) {
        switch (message) {
            case "Hello": return "World";
            case "Bye": return "Bye";
            default: return "Didn't catch that";
        }
    }

    public static void main( String[] args )
    {
        System.out.println("Server started waiting for client...");
        try (
            ServerSocket serverSocket = new ServerSocket(9797);
            Socket socket = serverSocket.accept();

            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        ) {
            System.out.println("Established connection.");

            while (true) {
                String receivedMessage = bufferedReader.readLine();
                System.out.println("Client: " + receivedMessage);

                String response = respond(receivedMessage);
                bufferedWriter.write(response);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
