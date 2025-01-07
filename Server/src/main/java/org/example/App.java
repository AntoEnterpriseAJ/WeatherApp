package org.example;

import org.example.database.DatabaseConnection;
import org.example.database.model.UserEntity;

import javax.xml.crypto.Data;
import java.util.List;

public class App
{
    public static void main( String[] args )
    {
//        new Server().run();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        List<UserEntity> allUsers = databaseConnection.getAllUsers();
        allUsers.forEach(System.out::println);
    }
}
