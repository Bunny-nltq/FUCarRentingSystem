package com.fucar.test;

import com.fucar.util.HibernateUtil;
import org.hibernate.Session;

public class DbTestMain {
    public static void main(String[] args) {
        System.out.println("Starting DB test...");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object result = session.createNativeQuery("SELECT 1").getSingleResult();
            System.out.println("✅ MySQL connected successfully! Result: " + result);
        } catch (Exception e) {
            System.err.println("❌ DB connection test failed:");
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
