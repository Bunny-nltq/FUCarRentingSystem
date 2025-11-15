package com.fucar.repository;

import com.fucar.entity.Review;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ReviewRepository {

    public void save(Review review) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.persist(review);
        tx.commit();
        s.close();
    }

    public List<Review> findByCar(int carID) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Review> list = s.createQuery(
                "from Review r where r.car.carID = :cid", Review.class
        ).setParameter("cid", carID).list();
        s.close();
        return list;
    }
}
