package org.example.database;

import jakarta.persistence.*;
import org.example.database.model.UserEntity;

import java.util.List;

public class DatabaseConnection {
    private EntityManagerFactory entityManagerFactory;

    public DatabaseConnection() {
        initEntityManagerFactory();
    }

    private void initEntityManagerFactory() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("default");
        } catch (Exception e) {
            System.err.println("Error initializing EntityManagerFactory: " + e.getMessage());
        }
    }

    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    public List<UserEntity> getAllUsers() {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            TypedQuery<UserEntity> query = entityManager.createQuery(
                    "SELECT userEntity FROM UserEntity userEntity", UserEntity.class
            );
            List<UserEntity> allUsers = query.getResultList();

            transaction.commit();
            return allUsers;

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Transaction error: " + e.getMessage());
            throw e;

        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
