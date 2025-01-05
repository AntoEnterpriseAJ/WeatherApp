package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class App
{
    public static void main( String[] args ) throws IOException {
        try (
            Socket socket = new Socket("localhost", 9797);
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
        ) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                String messageReceived = bufferedReader.readLine();
                System.out.println("Server: " + messageReceived);

                if (messageToSend.equalsIgnoreCase("exit")) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

