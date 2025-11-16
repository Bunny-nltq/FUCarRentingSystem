package com.fucar.repository;

import com.fucar.entity.Account;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AccountRepository {

    /**
     * Tìm tài khoản theo email
     */
    public Account findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Account WHERE email = :email", Account.class
            ).setParameter("email", email)
             .uniqueResult();
        } catch (Exception e) {
            System.err.println("Error findByEmail: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lưu tài khoản mới
     */
    public void save(Account account) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(account);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error save Account: " + e.getMessage());
        }
    }

    /**
     * Update tài khoản (phòng khi sau này cần đổi mật khẩu)
     */
    public void update(Account account) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(account);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error update Account: " + e.getMessage());
        }
    }
}
