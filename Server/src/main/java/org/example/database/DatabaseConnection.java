package org.example.database;

import jakarta.persistence.*;

import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseConnection {
    private static final EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("default");
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
            entityTransaction.rollback();
            System.err.println("Execute transaction error: " + e.getMessage());
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
            entityTransaction.rollback();
            System.err.println("Execute returning transaction error: " + e.getMessage());
        } finally {
            entityManager.close();
        }

        return result;
    }

    public static void closeFactory() {
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
