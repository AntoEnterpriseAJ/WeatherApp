package org.example.database.dao;

import org.example.database.DatabaseConnection;
import org.example.database.model.RoleEntity;

import java.util.List;

public class RoleEntityDao implements Dao<RoleEntity> {
    DatabaseConnection databaseConnection;

    public RoleEntityDao() {
        databaseConnection = new DatabaseConnection();
    }

    public RoleEntityDao(String persistence) {
        databaseConnection = new DatabaseConnection(persistence);
    }

    @Override
    public List<RoleEntity> getAll() {
        return databaseConnection.executeReturnTransaction(
                entityManager -> entityManager
                        .createQuery("SELECT role FROM RoleEntity role", RoleEntity.class).getResultList()
        );
    }

    @Override
    public void create(RoleEntity object) {
        databaseConnection.executeTransaction(
                entityManager -> entityManager.persist(object)
        );
    }

    @Override
    public void update(RoleEntity object) {
        databaseConnection.executeTransaction(entityManager -> {
            RoleEntity existingRole = entityManager.find(RoleEntity.class, object.getId());
            if (existingRole == null) {
                throw new IllegalArgumentException(
                        "Entity with ID " + object.getId() + " doesn't exist, therefore can't update it"
                );
            }

            entityManager.merge(object);
        });
    }

    @Override
    public void remove(RoleEntity object) {
        databaseConnection.executeTransaction(entityManager -> {
            RoleEntity existingRole = entityManager.find(RoleEntity.class, object.getId());
            if (existingRole == null) {
                throw new IllegalArgumentException(
                        "Entity with ID " + object.getId() + " doesn't exist, therefore can't delete it"
                );
            }

            entityManager.remove(existingRole);
        });
    }
}
