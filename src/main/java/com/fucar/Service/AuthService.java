package com.fucar.service;

import com.fucar.entity.Account;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AuthService {

    // Phương thức cũ
    public boolean register(String email, String accountName, String password, String role) {
        // Kiểm tra email đã tồn tại chưa
        if (getAccountByEmail(email) != null) {
            return false;
        }
        Account acc = new Account();
        acc.setAccountName(accountName);
        acc.setEmail(email);
        acc.setPasswordHash(password); // nên hash password ở thực tế
        acc.setRole(role);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(acc);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức mới: trả về Account vừa tạo
    public Account registerAndReturnAccount(String email, String accountName, String password, String role) {
        // Kiểm tra email đã tồn tại chưa
        if (getAccountByEmail(email) != null) {
            return null;
        }
        Account acc = new Account();
        acc.setAccountName(accountName);
        acc.setEmail(email);
        acc.setPasswordHash(password);
        acc.setRole(role);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(acc);
            tx.commit();
            return acc; // trả về đối tượng Account vừa tạo
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy account theo email
    public Account getAccountByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Account WHERE email = :email", Account.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
