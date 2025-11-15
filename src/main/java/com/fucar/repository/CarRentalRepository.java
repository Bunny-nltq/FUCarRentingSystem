package com.fucar.repository;

import com.fucar.entity.CarRental;
import com.fucar.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class CarRentalRepository {

    public void save(CarRental rental) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(rental);
        tx.commit();
        session.close();
    }

    public void update(CarRental rental) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.merge(rental);
        tx.commit();
        session.close();
    }

    public void delete(Integer rentalID) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        CarRental rental = session.get(CarRental.class, rentalID);
        if (rental != null) session.remove(rental);
        tx.commit();
        session.close();
    }

    public CarRental findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CarRental rental = session.get(CarRental.class, id);
        session.close();
        return rental;
    }

    public List<CarRental> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CarRental> list = session.createQuery("from CarRental", CarRental.class).list();
        session.close();
        return list;
    }

    // Tìm theo ngày thuê
    public List<CarRental> filterByDate(LocalDate start, LocalDate end) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CarRental> list = session.createQuery(
                "from CarRental c where c.pickupDate >= :start and c.returnDate <= :end",
                CarRental.class
        )
        .setParameter("start", start)
        .setParameter("end", end)
        .list();
        session.close();
        return list;
    }

    // Sắp xếp
    public List<CarRental> sortByPriceDesc() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CarRental> list = session.createQuery(
                "from CarRental c order by c.rentPrice desc",
                CarRental.class
        ).list();
        session.close();
        return list;
    }

    public List<CarRental> sortByPickupDateDesc() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CarRental> list = session.createQuery(
                "from CarRental c order by c.pickupDate desc",
                CarRental.class
        ).list();
        session.close();
        return list;
    }
}
