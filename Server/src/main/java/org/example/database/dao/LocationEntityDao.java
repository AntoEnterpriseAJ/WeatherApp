package org.example.database.dao;

import com.google.gson.JsonObject;
import org.example.database.DatabaseConnection;
import org.example.database.model.LocationEntity;

import java.util.List;
import java.util.Optional;

public class LocationEntityDao implements Dao<LocationEntity> {
    DatabaseConnection databaseConnection;

    public LocationEntityDao() {
        databaseConnection = new DatabaseConnection();
    }

    public LocationEntityDao(String persistence) {
        databaseConnection = new DatabaseConnection(persistence);
    }

    public Optional<LocationEntity> getAtId(long id) {
        return Optional.ofNullable(databaseConnection.executeReturnTransaction(
                entityManager -> entityManager
                        .createQuery("SELECT location FROM LocationEntity location WHERE location.id=:id", LocationEntity.class)
                        .setParameter("id", id)
                        .getSingleResult()
        ));
    }

    public LocationEntity getClosestTo(double latitude, double longitude) {
        return databaseConnection.executeReturnTransaction(entityManager ->
                (LocationEntity) entityManager.createNativeQuery(
                                "SELECT * FROM location " +
                                        "ORDER BY ST_Distance(geom, ST_SetSRID(ST_MakePoint(?, ?), 4326)) " +
                                        "LIMIT 1",
                                LocationEntity.class
                        )
                        .setParameter(1, longitude)
                        .setParameter(2, latitude)
                        .getSingleResult()
        );
    }

    public JsonObject getWeatherDataAt(long locationId) {
        return databaseConnection.executeReturnTransaction(
                entityManager -> entityManager
                        .createQuery("SELECT location.weatherData FROM LocationEntity location WHERE location.id=:locationId", JsonObject.class)
                        .setParameter("locationId", locationId)
                        .getSingleResult()
        );
    }

    public LocationEntity getDefaultLocation() {
        return databaseConnection.executeReturnTransaction(
                entityManager -> entityManager
                        .createQuery("SELECT location FROM LocationEntity location WHERE location.id=1", LocationEntity.class).getSingleResult()
        );
    }

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
