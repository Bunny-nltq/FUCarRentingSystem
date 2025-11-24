package com.fucar.repository;

import com.fucar.entity.Customer;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CustomerRepository {

    // ================================
    // FIND ALL
    // ================================
    public List<Customer> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Customer", Customer.class).list();
        } finally {
            session.close();
        }
    }

    // ================================
    // FIND BY ID
    // ================================
    public Customer findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Customer.class, id);
        } finally {
            session.close();
        }
    }

    // ================================
    // FIND BY ACCOUNT ID
    // ================================
    public Customer findByAccountId(int accountId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                    "FROM Customer c WHERE c.account.accountId = :accId",
                    Customer.class
            )
            .setParameter("accId", accountId)
            .uniqueResult();
        } finally {
            session.close();
        }
    }

    // ================================
    // FIND BY EMAIL
    // ================================
    public Customer findByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                    "FROM Customer c WHERE c.email = :email",
                    Customer.class
            )
            .setParameter("email", email)
            .uniqueResult();
        } finally {
            session.close();
        }
    }

    // ================================
    // SAVE
    // ================================
    public void save(Customer c) {
        executeTransaction(session -> session.persist(c));
    }

    // ================================
    // UPDATE
    // ================================
    public void update(Customer c) {
        executeTransaction(session -> session.merge(c));
    }

    // ================================
    // DELETE BY ENTITY
    // ================================
    public void delete(Customer c) {
        executeTransaction(session -> {
            Customer attached = session.contains(c) ? c : session.merge(c);
            session.remove(attached);
        });
    }

    // ================================
    // DELETE BY ID
    // ================================
    public void deleteById(int id) {
        executeTransaction(session -> {
            Customer c = session.get(Customer.class, id);
            if (c != null) session.remove(c);
        });
    }

    // ================================
    // TRANSACTION WRAPPER FIXED - SAFE
    // ================================
    private void executeTransaction(RepositoryAction action) {

        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            action.execute(session);

            tx.commit();

        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @FunctionalInterface
    private interface RepositoryAction {
        void execute(Session session);
    }
}
