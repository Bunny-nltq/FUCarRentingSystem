package com.fucar.repository;

import com.fucar.entity.CarRental;
import com.fucar.util.HibernateUtil;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaBuilder;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class CarRentalRepository {

    // ===================== THÊM RENTAL =====================
    public void save(CarRental rental) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(rental);
            tx.commit();
        }
    }

    // ===================== CẬP NHẬT RENTAL =====================
    public void update(CarRental rental) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(rental);
            tx.commit();
        }
    }

    // ===================== XÓA RENTAL =====================
    public void delete(CarRental rental) {
        if (rental == null || rental.getRentalId() == null) return;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            CarRental managedRental = session.get(CarRental.class, rental.getRentalId());
            if (managedRental != null) {
                session.remove(managedRental);
            }
            tx.commit();
        }
    }

    // ===================== TÌM THEO ID =====================
    public CarRental findById(Integer id) {
        if (id == null) return null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT cr FROM CarRental cr " +
                "LEFT JOIN FETCH cr.customer " +
                "LEFT JOIN FETCH cr.car " +
                "WHERE cr.rentalId = :id", 
                CarRental.class)
                .setParameter("id", id)
                .uniqueResult();
        }
    }

    // ===================== LẤY TẤT CẢ =====================
    public List<CarRental> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT cr FROM CarRental cr " +
                "LEFT JOIN FETCH cr.customer " +
                "LEFT JOIN FETCH cr.car", 
                CarRental.class)
                .list();
        }
    }

    // ===================== LỌC THEO NGÀY =====================
    public List<CarRental> filterByDate(LocalDate start, LocalDate end) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT cr FROM CarRental cr " +
                    "LEFT JOIN FETCH cr.customer " +
                    "LEFT JOIN FETCH cr.car " +
                    "WHERE cr.pickupDate >= :start AND cr.returnDate <= :end",
                    CarRental.class
            )
            .setParameter("start", start)
            .setParameter("end", end)
            .list();
        }
    }

    // ===================== SẮP XẾP =====================
    public List<CarRental> sortByPriceDesc() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT cr FROM CarRental cr " +
                    "LEFT JOIN FETCH cr.customer " +
                    "LEFT JOIN FETCH cr.car " +
                    "ORDER BY cr.rentPrice DESC",
                    CarRental.class
            ).list();
        }
    }

    public List<CarRental> sortByPickupDateDesc() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT cr FROM CarRental cr " +
                    "LEFT JOIN FETCH cr.customer " +
                    "LEFT JOIN FETCH cr.car " +
                    "ORDER BY cr.pickupDate DESC",
                    CarRental.class
            ).list();
        }
    }

    // ===================== LẤY THEO ACCOUNT =====================
    public List<CarRental> findByCustomerAccountId(Integer CustomerID) {
        if (CustomerID == null) return List.of();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT cr FROM CarRental cr " +
                    "LEFT JOIN FETCH cr.customer " +
                    "LEFT JOIN FETCH cr.car " +
                    "WHERE cr.customer.customerID = :CustomerID",
                    CarRental.class
            )
            .setParameter("CustomerID", CustomerID)
            .list();
        }
    }
    
    
    public List<CarRental> findByCustomerId(Integer customerId) {
        if (customerId == null) {
            return Collections.emptyList();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT cr FROM CarRental cr " +
                    "LEFT JOIN FETCH cr.customer " +
                    "LEFT JOIN FETCH cr.car " +
                    "WHERE cr.customer.customerID = :customerId",
                    CarRental.class
            )
            .setParameter("customerId", customerId)
            .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
