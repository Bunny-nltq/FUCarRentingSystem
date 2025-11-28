package com.fucar.repository;

import com.fucar.entity.Customer;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CustomerRepository {

    public List<Customer> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Customer", Customer.class).list();
        }
    }

    public void save(Customer c) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.persist(c);
            tx.commit();
        }
    }

    public void delete(Customer c) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.remove(s.contains(c) ? c : s.merge(c));
            tx.commit();
        }
    }
    
    public Customer findByAccountId(int accountId) {
        Transaction tx = null;
        Customer result = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Query<Customer> query = session.createQuery(
                "FROM Customer c WHERE c.account.accountId = :accountId",
                Customer.class
            );

            query.setParameter("accountId", accountId);
            result = query.uniqueResult();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return result;
    }

    
}
