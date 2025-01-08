package org.example;

import org.example.database.dao.RoleEntityDao;
import org.example.database.model.RoleEntity;
import org.example.database.model.enums.Role;

public class App
{
    public static void main( String[] args )
    {
//        new Server().run();
        RoleEntityDao roleEntityDao = new RoleEntityDao();


        System.out.println("Roles:");
        roleEntityDao.getAll().forEach(System.out::println);
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole(Role.ADMIN);
        roleEntityDao.create(roleEntity);

        roleEntityDao.getAll().forEach(System.out::println);
    }
}
