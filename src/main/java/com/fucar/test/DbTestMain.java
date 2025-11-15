package com.fucar.test;

import org.hibernate.Session;
import com.fucar.util.HibernateUtil;

public class DbTestMain {
    public static void main(String[] args) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("✔ Hibernate connected OK!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
