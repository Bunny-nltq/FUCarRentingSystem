package com.fucar.repository;

import com.fucar.entity.Customer;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CustomerRepository {

    // ================================
    // LẤY TẤT CẢ CUSTOMER
    // ================================
    public List<Customer> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Customer", Customer.class).list();
        }
    }

    // ================================
    // TÌM CUSTOMER THEO PK CustomerID
    // ================================
    public Customer findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Customer.class, id);
        }
    }

    // ================================
    // TÌM CUSTOMER THEO AccountID
    // ================================
    public Customer findByAccountId(int accountId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Customer c WHERE c.account.accountId = :accId",
                    Customer.class
            )
            .setParameter("accId", accountId)
            .uniqueResult();
        }
    }

    // ================================
    // TÌM CUSTOMER THEO EMAIL (CỘT Email CỦA CUSTOMER)
    // ================================
    public Customer findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Customer c WHERE c.email = :email",
                    Customer.class
            )
            .setParameter("email", email)
            .uniqueResult();
        }
    }

    // ================================
    // SAVE CUSTOMER
    // ================================
    public void save(Customer c) {
        executeTransaction(session -> session.persist(c));
    }

    // ================================
    // UPDATE CUSTOMER
    // ================================
    public void update(Customer c) {
        executeTransaction(session -> session.merge(c));
    }

    // ================================
    // DELETE BY OBJECT
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
    // TRANSACTION WRAPPER
    // ================================
    private void executeTransaction(RepositoryAction action) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            action.execute(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    private interface RepositoryAction {
        void execute(Session session);
    }
}
