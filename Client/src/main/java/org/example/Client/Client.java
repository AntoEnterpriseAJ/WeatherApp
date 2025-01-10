package org.example.Client;

import com.google.gson.*;
import org.example.shared.*;
import org.example.shared.enums.RequestType;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Client {
    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 9797;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public void start() {
        try {
            connectToServer();

            while (true) {
                printMenu();
                handleRequest();

                Response response = (Response) this.objectInputStream.readObject();
                System.out.println("Server: " + response.getMessage());
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void printMenu() {
        System.out.println("1. Get weather");
        System.out.println("2. Upload weather data");
        System.out.println("3. Register");
        System.out.println("4. Login");
        System.out.println("5. Set location");
    }

    private void connectToServer() throws IOException {
        this.socket = new Socket(SERVER_HOST, SERVER_PORT);
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    private void handleRequest() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int requestTypeCode = Integer.parseInt(scanner.nextLine());
        RequestType requestType = RequestType.fromInt(requestTypeCode);

        if (requestType == RequestType.GET_WEATHER) {
            WeatherRequest weatherRequest = new WeatherRequest(requestType);

            this.objectOutputStream.writeObject(weatherRequest);
            this.objectOutputStream.flush();
        } else if (requestType == RequestType.UPDATE_WEATHER) {
            String pathToFile;
            System.out.println("Enter weather data file path: ");
            pathToFile = scanner.nextLine();

            try (FileReader fileReader = new FileReader(pathToFile)) {
                JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();

                // TODO: jsonObject is apparently not serializable by default, fix later
                WeatherRequest weatherRequest = new WeatherRequest(requestType);
                weatherRequest.setWeatherData(jsonObject.toString());

                this.objectOutputStream.writeObject(weatherRequest);
                this.objectOutputStream.flush();
            } catch (IOException e) {
                System.out.println("Error reading file at path: " + pathToFile + ", message: " + e.getMessage());
            } catch (JsonSyntaxException e) {
                System.out.println("Error parsing the JSON data: " + e.getMessage());
            }
        } else if (requestType == RequestType.REGISTER) {
            System.out.println("Enter username:");
            String username = scanner.next();
            System.out.println("Enter password:");
            String password = scanner.next();

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername(username);
            registerRequest.setPassword(password);

            this.objectOutputStream.writeObject(registerRequest);
            this.objectOutputStream.flush();
        } else if (requestType == RequestType.LOGIN) {
            System.out.println("Enter username:");
            String username = scanner.next();
            System.out.println("Enter password:");
            String password = scanner.next();

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(username);
            loginRequest.setPassword(password);

            this.objectOutputStream.writeObject(loginRequest);
            this.objectOutputStream.flush();
        } else if (requestType == RequestType.SET_LOCATION) {
            System.out.println("latitude:");
            double latitude = scanner.nextDouble();
            System.out.println("longitude:");
            double longitude = scanner.nextDouble();

            SetLocationRequest setLocationRequest = new SetLocationRequest(latitude, longitude);

            this.objectOutputStream.writeObject(setLocationRequest);
            this.objectOutputStream.flush();
        }
        else {
            System.out.println("Client: Invalid request type");
        }
    }
}
