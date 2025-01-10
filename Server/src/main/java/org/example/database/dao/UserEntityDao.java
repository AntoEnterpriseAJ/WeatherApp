package org.example.database.dao;

import org.example.database.DatabaseConnection;
import org.example.database.model.UserEntity;

import java.util.List;
import java.util.Optional;

public class UserEntityDao implements Dao<UserEntity> {
    DatabaseConnection databaseConnection = new DatabaseConnection();

    @Override
    public List<UserEntity> getAll() {
        return databaseConnection.executeReturnTransaction(
                entityManager -> entityManager
                        .createQuery("SELECT user FROM UserEntity user", UserEntity.class).getResultList()
        );
    }

    public Optional<UserEntity> findByName (String name) {
        return Optional.ofNullable(databaseConnection.executeReturnTransaction(
                entityManger -> entityManger.createQuery("SELECT user FROM UserEntity user WHERE user.name = :name", UserEntity.class)
                        .setParameter("name", name)
                        .getSingleResult()
        ));
    }

    @Override
    public void create(UserEntity object) {
        databaseConnection.executeTransaction(
                entityManager -> entityManager.persist(object)
        );
    }

    @Override
    public void update(UserEntity object) {
        databaseConnection.executeTransaction(entityManager -> {
            UserEntity existingUser = entityManager.find(UserEntity.class, object.getId());
            if (existingUser == null) {
                throw new IllegalArgumentException(
                        "Entity with ID " + object.getId() + " doesn't exist, therefore can't update it"
                );
            }

            entityManager.merge(object);
        });
    }

    @Override
    public void remove(UserEntity object) {
        databaseConnection.executeTransaction(entityManager -> {
            UserEntity existingUser = entityManager.find(UserEntity.class, object.getId());
            if (existingUser == null) {
                throw new IllegalArgumentException(
                        "Entity with ID " + object.getId() + " doesn't exist, therefore can't delete it"
                );
            }

            entityManager.remove(existingUser);
        });
    }
}
