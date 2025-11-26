package com.fucar.repository;

import com.fucar.entity.CarRental;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
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
        if (rental == null || rental.getCar() == null) return;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            CarRental managedRental = session.get(CarRental.class, rental.getCar());
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
            return session.get(CarRental.class, id);
        }
    }

    // ===================== LẤY TẤT CẢ =====================
    public List<CarRental> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from CarRental", CarRental.class).list();
        }
    }

    // ===================== LỌC THEO NGÀY =====================
    public List<CarRental> filterByDate(LocalDate start, LocalDate end) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from CarRental c where c.pickupDate >= :start and c.returnDate <= :end",
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
                    "from CarRental c order by c.rentPrice desc",
                    CarRental.class
            ).list();
        }
    }

    public List<CarRental> sortByPickupDateDesc() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from CarRental c order by c.pickupDate desc",
                    CarRental.class
            ).list();
        }
    }

    // ===================== LẤY THEO ACCOUNT =====================
    public List<CarRental> findByCustomerAccountId(Integer CustomerID) {
        if (CustomerID == null) return List.of();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from CarRental c where c.customer.customerId = :CustomerID",
                    CarRental.class
            )
            .setParameter("CustomerID", CustomerID)
            .list();
        }
    }
    
    
    
}
