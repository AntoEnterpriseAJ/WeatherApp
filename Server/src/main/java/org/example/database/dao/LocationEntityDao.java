package org.example.database.dao;

import org.example.database.DatabaseConnection;
import org.example.database.model.LocationEntity;

import java.util.List;

public class LocationEntityDao implements Dao<LocationEntity> {
    DatabaseConnection databaseConnection = new DatabaseConnection();

    @Override
    public List<LocationEntity> getAll() {
        return databaseConnection.executeReturnTransaction(
                entityManager -> entityManager
                        .createQuery("SELECT location FROM LocationEntity location", LocationEntity.class)
                        .getResultList()
        );
    }

    @Override
    public void create(LocationEntity object) {
        databaseConnection.executeTransaction(
                entityManager -> entityManager.persist(object)
        );
    }

    @Override
    public void update(LocationEntity object) {
        databaseConnection.executeTransaction(entityManager -> {
            LocationEntity locationEntity = entityManager.find(LocationEntity.class, object.getId());
            if (locationEntity == null) {
                throw new IllegalArgumentException(
                        "Entity with ID " + object.getId() + " doesn't exist, therefore can't update it"
                );
            }

            entityManager.merge(object);
        });
    }

    @Override
    public void remove(LocationEntity object) {
        databaseConnection.executeTransaction(entityManager -> {
            LocationEntity existingLocation = entityManager.find(LocationEntity.class, object.getId());
            if (existingLocation == null) {
                throw new IllegalArgumentException(
                        "Entity with ID " + object.getId() + " doesn't exist, therefore can't delete it"
                );
            }

            entityManager.remove(existingLocation);
        });
    }
}
