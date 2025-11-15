package com.fucar.repository;

import com.fucar.entity.Customer;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
}
