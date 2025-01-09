package org.example;

import org.example.database.dao.LocationEntityDao;

public class App
{
    public static void main( String[] args )
    {
//        new Server().run();
        try {
            LocationEntityDao locationEntityDao = new LocationEntityDao();

            locationEntityDao.getAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
