package org.example.database;

import jakarta.persistence.*;

import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseConnection {
    private final String DEFAULT_PERSISTENCE = "default";
    private final EntityManagerFactory entityManagerFactory;

    public DatabaseConnection() {
        try {
            this.entityManagerFactory = Persistence.createEntityManagerFactory(DEFAULT_PERSISTENCE);
        } catch (PersistenceException e) {
            System.err.println("Failed to initialize EntityManagerFactory: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public DatabaseConnection(String persistenceUnitName) {
        try {
            this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        } catch (PersistenceException e) {
            System.err.println("Failed to initialize EntityManagerFactory: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    // Constructor that accepts a persistence unit name with additional properties
    public DatabaseConnection(String persistenceUnitName, Map<String, String> properties) {
        try {
            this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
        } catch (PersistenceException e) {
            System.err.println("Failed to initialize EntityManagerFactory: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public void executeTransaction(Consumer<EntityManager> consumer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            consumer.accept(entityManager);
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Execute transaction error: " + e.getMessage());
            e.printStackTrace();
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public <R> R executeReturnTransaction(Function<EntityManager, R> function) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        R result = null;

        try {
            entityTransaction.begin();
            result = function.apply(entityManager);
            entityTransaction.commit();
        } catch (Exception e) {
            System.err.println("Execute returning transaction error: " + e.getMessage());
            e.printStackTrace();
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }

        return result;
    }

    public void closeFactory() {
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
