package com.fucar.repository;

import com.fucar.entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class AccountRepository {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("FUCarPU");

    public Account findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Account a WHERE a.email = :email", Account.class)
                     .setParameter("email", email)
                     .getResultStream()
                     .findFirst()
                     .orElse(null);
        } finally {
            em.close();
        }
    }

    public void save(Account account) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (account.getAccountID() == null) {
                em.persist(account);
            } else {
                em.merge(account);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
