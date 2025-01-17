package org.example.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.database.dao.LocationEntityDao;
import org.example.database.dao.UserEntityDao;
import org.example.database.model.LocationEntity;
import org.example.database.model.UserEntity;
import org.example.database.model.enums.Role;
import org.example.shared.*;
import org.example.shared.enums.RequestType;
import org.example.shared.enums.ResponseStatus;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class ClientThread extends Thread implements Closeable {

    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Client client = new Client();

    public ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                handleRequest();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void handleRequest() throws IOException, ClassNotFoundException {
        Request request = (Request) inputStream.readObject();
        System.out.println("Received request: " + request.getRequestType());

        Response response = processRequest(request);
        outputStream.writeObject(response);
        outputStream.flush();
    }

    private Response processRequest(Request request) {
        if (request instanceof WeatherRequest weatherRequest) {
            return handleWeatherRequest(weatherRequest);
        } else if (request instanceof RegisterRequest registerRequest) {
            return handleRegisterRequest(registerRequest);
        } else if (request instanceof LoginRequest loginRequest) {
            return handleLoginRequest(loginRequest);
        } else if (request instanceof SetLocationRequest setLocationRequest) {
            return handleSetLocationRequest(setLocationRequest);
        }
        return new Response(ResponseStatus.FAILED, "Invalid request type");
    }

    private Response handleWeatherRequest(WeatherRequest request) {
        if (!client.isLoggedIn()) {
            return new Response(ResponseStatus.FAILED, "You need to be logged in to get weather data");
        }

        LocationEntityDao locationDao = new LocationEntityDao();
        if (request.getRequestType() == RequestType.GET_WEATHER) {
            JsonObject weatherData = locationDao.getWeatherDataAt(client.getLocationId());
            return new Response(ResponseStatus.SUCCESS, weatherData.toString());
        } else if (request.getRequestType() == RequestType.UPDATE_WEATHER) {
            if (client.getRole() != Role.ADMIN) {
                return new Response(ResponseStatus.FAILED, STR."No permissions: your current role is \{client.getRole()}");
            }

            Optional<LocationEntity> optionalLocationEntity = locationDao.getAtId(client.getLocationId());
            if (optionalLocationEntity.isEmpty()) {
                return new Response(ResponseStatus.FAILED, "Cannot update weather at non-existing location");
            }

            LocationEntity currentLocation = optionalLocationEntity.get();

            JsonObject weatherData = JsonParser.parseString(request.getWeatherData()).getAsJsonObject();
            currentLocation.setWeatherData(weatherData);

            locationDao.update(currentLocation);
            return new Response(ResponseStatus.SUCCESS, "Successfully updated the weather data at your current location");
        }

        return new Response(ResponseStatus.FAILED, "Invalid weather request type");
    }

    private Response handleRegisterRequest(RegisterRequest request) {
        if (client.isLoggedIn()) {
            return new Response(ResponseStatus.FAILED, "Already logged in");
        }

        UserEntityDao userDao = new UserEntityDao();
        LocationEntityDao locationDao = new LocationEntityDao();

        if (userDao.findByName(request.getUsername()).isPresent()) {
            return new Response(ResponseStatus.FAILED, "User already exists, try logging in");
        }

        UserEntity newUser = new UserEntity();
        newUser.setName(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setRoleId(Role.USER.getValue());
        newUser.setLocation(locationDao.getDefaultLocation());

        userDao.create(newUser);
        initializeClientSession(newUser);

        return new Response(ResponseStatus.SUCCESS,
                String.format("User %s registered successfully. Currently at location: %s",
                        newUser.getName(), newUser.getLocation().getName()));
    }

    private Response handleLoginRequest(LoginRequest request) {
        if (client.isLoggedIn()) {
            return new Response(ResponseStatus.FAILED, "Already logged in");
        }

        UserEntityDao userDao = new UserEntityDao();
        Optional<UserEntity> userOptional = userDao.findByName(request.getUsername());

        if (userOptional.isEmpty() ||
                !userOptional.get().getPassword().equals(request.getPassword())) {
            return new Response(ResponseStatus.FAILED, "Invalid username or password");
        }

        initializeClientSession(userOptional.get());

        return new Response(ResponseStatus.SUCCESS,
                String.format("User %s logged in successfully. Currently at location: %s",
                        client.getUsername(), userOptional.get().getLocation().getName()));
    }

    private Response handleSetLocationRequest(SetLocationRequest request) {
        if (!client.isLoggedIn()) {
            return new Response(ResponseStatus.FAILED, "You need to be logged in to set location");
        }

        UserEntityDao userDao = new UserEntityDao();
        LocationEntityDao locationDao = new LocationEntityDao();

        LocationEntity closestLocation = locationDao.getClosestTo(request.getLatitude(), request.getLongitude());

        UserEntity user = userDao.findByName(client.getUsername()).orElseThrow(
                () -> new IllegalStateException("User not found")
        );

        user.setLocation(closestLocation);
        userDao.update(user);

        client.setLocationId(closestLocation.getId());
        return new Response(ResponseStatus.SUCCESS, "Location set successfully");
    }

    private void initializeClientSession(UserEntity user) {
        client.setLoggedIn(true);
        client.setUsername(user.getName());
        client.setRole(Role.fromInt(user.getRoleId()));
        client.setLocationId(user.getLocation().getId());
    }

    @Override
    public void close() {
        try {
            if (!socket.isClosed()) socket.close();
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
