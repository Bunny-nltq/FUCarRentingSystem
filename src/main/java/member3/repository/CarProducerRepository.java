package member3.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import member3.entity.CarProducer;
import member3.util.HibernateUtil;

/**
 * Repository: CarProducerRepository
 * Xử lý tất cả các hoạt động CRUD với đối tượng CarProducer
 */
public class CarProducerRepository {
    
    // ===== CREATE =====
    
    /**
     * Thêm một hãng sản xuất mới
     */
    public boolean save(CarProducer producer) {
        if (producer == null || !producer.isValid()) {
            System.err.println("❌ CarProducer không hợp lệ!");
            return false;
        }
        
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.save(producer);
            transaction.commit();
            
            System.out.println("✅ Thêm hãng sản xuất thành công: " + producer.getProducerName());
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ Lỗi thêm hãng: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    // ===== READ =====
    
    /**
     * Lấy hãng theo ID
     */
    public CarProducer findById(int producerId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(CarProducer.class, producerId);
        } catch (Exception e) {
            System.err.println("❌ Lỗi lấy hãng: " + e.getMessage());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Lấy tất cả hãng sản xuất
     */
    public List<CarProducer> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<CarProducer> query = session.createQuery(
                "FROM CarProducer ORDER BY producerId DESC", CarProducer.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Lỗi lấy danh sách hãng: " + e.getMessage());
            return List.of();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Lấy hãng theo tên
     */
    public CarProducer findByName(String name) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<CarProducer> query = session.createQuery(
                "FROM CarProducer WHERE producerName = :name", CarProducer.class);
            query.setParameter("name", name);
            return query.getResultList().stream().findFirst().orElse(null);
        } catch (Exception e) {
            System.err.println("❌ Lỗi tìm hãng theo tên: " + e.getMessage());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Lấy hãng theo đất nước
     */
    public List<CarProducer> findByCountry(String country) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<CarProducer> query = session.createQuery(
                "FROM CarProducer WHERE country = :country", CarProducer.class);
            query.setParameter("country", country);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Lỗi tìm hãng theo đất nước: " + e.getMessage());
            return List.of();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    // ===== UPDATE =====
    
    /**
     * Cập nhật thông tin hãng sản xuất
     */
    public boolean update(CarProducer producer) {
        if (producer == null || !producer.isValid()) {
            System.err.println("❌ CarProducer không hợp lệ!");
            return false;
        }
        
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(producer);
            transaction.commit();
            
            System.out.println("✅ Cập nhật hãng sản xuất thành công!");
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ Lỗi cập nhật hãng: " + e.getMessage());
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    // ===== DELETE =====
    
    /**
     * Xóa hãng sản xuất theo ID
     */
    public boolean delete(int producerId) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            CarProducer producer = session.get(CarProducer.class, producerId);
            if (producer == null) {
                System.err.println("❌ Không tìm thấy hãng sản xuất!");
                return false;
            }
            
            // Kiểm tra xem có xe liên quan không
            if (!producer.getCars().isEmpty()) {
                System.err.println("❌ Không thể xóa hãng khi còn xe liên quan! (" + producer.getCars().size() + " xe)");
                return false;
            }
            
            session.delete(producer);
            transaction.commit();
            
            System.out.println("✅ Xóa hãng sản xuất thành công!");
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ Lỗi xóa hãng: " + e.getMessage());
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    // ===== UTILITY METHODS =====
    
    /**
     * Đếm tổng số hãng sản xuất
     */
    public long count() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM CarProducer", Long.class);
            return query.getResultList().get(0);
        } catch (Exception e) {
            System.err.println("❌ Lỗi đếm hãng: " + e.getMessage());
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Lấy tất cả hãng theo năm thành lập
     */
    public List<CarProducer> findByFoundedYear(int year) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<CarProducer> query = session.createQuery(
                "FROM CarProducer WHERE foundedYear = :year", CarProducer.class);
            query.setParameter("year", year);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Lỗi tìm hãng theo năm thành lập: " + e.getMessage());
            return List.of();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
