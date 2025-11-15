package com.fucar.repository;

import com.fucar.entity.Account;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AccountRepository {

    public Account findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Account WHERE email = :email", Account.class
            ).setParameter("email", email).uniqueResult();
        }
    }

    public void save(Account account) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(account);
            tx.commit();
        }
    }
}
