package com.fucar.repository;

import com.fucar.entity.Review;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ReviewRepository {

    // ==========================
    // CREATE / SAVE
    // ==========================
    public void save(Review review) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(review);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================
    // UPDATE
    // ==========================
    public void update(Review review) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(review); // merge để update
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================
    // DELETE
    // ==========================
    public void delete(Integer reviewId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Review review = session.get(Review.class, reviewId);
            if (review != null) {
                session.remove(review);
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================
    // FIND BY ID
    // ==========================
    public Review findById(Integer reviewId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Review.class, reviewId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ==========================
    // FIND ALL
    // ==========================
    public List<Review> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Review", Review.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // ==========================
    // FIND BY CAR
    // ==========================
    public List<Review> findByCar(int carID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from Review r where r.car.carID = :cid", Review.class
            ).setParameter("cid", carID).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
