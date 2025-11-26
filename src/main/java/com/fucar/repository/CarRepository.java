package com.fucar.repository;

import com.fucar.entity.Car;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class CarRepository {

    public Car save(Car car) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(car); // or session.saveOrUpdate(car)
            tx.commit();
            return car;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public Car update(Car car) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            car = (Car) session.merge(car);
            tx.commit();
            return car;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void delete(Car car) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(session.contains(car) ? car : session.merge(car));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public Optional<Car> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Car.class, id));
        }
    }

    public List<Car> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Car", Car.class).list();
        }
    }

    /**
     * Kiểm tra car có giao dịch (rental) hay không.
     * Assumes there is a Rental entity with a field 'car' mapping to Car.
     */
    public boolean hasRentals(Integer carId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // nếu bạn có entity Rental -> dùng query thực tế
            String hql = "select count(r.id) from Rental r where r.car.carID = :carId";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("carId", carId);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            // nếu không có entity Rental, trả về false (hoặc ném exception tuỳ bạn)
            e.printStackTrace();
            return false;
        }
    }
}
