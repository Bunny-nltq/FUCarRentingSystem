package com.fucar.repository;

import com.fucar.entity.Car;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class CarRepository {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void save(Car car) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(car);
        em.getTransaction().commit();
        em.close();
    }

    public void update(Car car) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(car);
        em.getTransaction().commit();
        em.close();
    }

    public Car findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        Car car = em.find(Car.class, id);
        em.close();
        return car;
    }

    public List<Car> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Car> list = em.createQuery("SELECT c FROM Car c", Car.class).getResultList();
        em.close();
        return list;
    }

    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        Car car = em.find(Car.class, id);
        if (car != null) {
            em.getTransaction().begin();
            em.remove(car);
            em.getTransaction().commit();
        }
        em.close();
    }
}
