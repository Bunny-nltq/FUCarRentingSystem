package com.fucar.repository;

import java.util.List;

import com.fucar.entity.CarProducer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CarProducerRepository {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void save(CarProducer producer) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(producer);
        em.getTransaction().commit();
        em.close();
    }

    public void update(CarProducer producer) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(producer);
        em.getTransaction().commit();
        em.close();
    }

    public CarProducer findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        CarProducer producer = em.find(CarProducer.class, id);
        em.close();
        return producer;
    }

    public List<CarProducer> findAll() {
        EntityManager em = emf.createEntityManager();
        List<CarProducer> list = em.createQuery("SELECT p FROM CarProducer p", CarProducer.class).getResultList();
        em.close();
        return list;
    }

    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        CarProducer producer = em.find(CarProducer.class, id);
        if (producer != null) {
            em.getTransaction().begin();
            em.remove(producer);
            em.getTransaction().commit();
        }
        em.close();
    }
}
