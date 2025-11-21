package com.fucar.repository;

import com.fucar.entity.Account;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AccountRepository {

    // Tìm account theo email
    public Account findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Account WHERE email = :email",
                    Account.class
            )
            .setParameter("email", email)
            .uniqueResult();
        } catch (Exception e) {
            System.err.println("Error findByEmail: " + e.getMessage());
            return null;
        }
    }

    // Lưu Account mới
    public void save(Account acc) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(acc);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error save Account: " + e.getMessage());
        }
    }

    // Update Account
    public void update(Account acc) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(acc);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error update Account: " + e.getMessage());
        }
    }
}
