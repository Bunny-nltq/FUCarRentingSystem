package member3.repository;

import member3.entity.Car;
import member3.entity.CarProducer;
import member3.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Repository: CarRepository
 * Xử lý tất cả các hoạt động CRUD với đối tượng Car
 */
public class CarRepository {
    
    // ===== CREATE =====
    
    /**
     * Thêm một chiếc xe mới
     */
    public boolean save(Car car) {
        if (car == null || !car.isValid()) {
            System.err.println("❌ Car không hợp lệ!");
            return false;
        }
        
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.save(car);
            transaction.commit();
            
            System.out.println("✅ Thêm xe thành công: " + car.getCarName());
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ Lỗi thêm xe: " + e.getMessage());
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
     * Lấy xe theo ID
     */
    public Car findById(int carId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Car.class, carId);
        } catch (Exception e) {
            System.err.println("❌ Lỗi lấy xe: " + e.getMessage());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Lấy tất cả xe
     */
    public List<Car> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Car> query = session.createQuery("FROM Car ORDER BY carId DESC", Car.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Lỗi lấy danh sách xe: " + e.getMessage());
            return List.of();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Lấy xe theo biển số
     */
    public Car findByLicensePlate(String licensePlate) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Car> query = session.createQuery(
                "FROM Car WHERE licensePlate = :plate", Car.class);
            query.setParameter("plate", licensePlate);
            return query.getResultList().stream().findFirst().orElse(null);
        } catch (Exception e) {
            System.err.println("❌ Lỗi tìm xe theo biển số: " + e.getMessage());
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Lấy xe theo tên
     */
    public List<Car> findByName(String name) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Car> query = session.createQuery(
                "FROM Car WHERE carName LIKE :name", Car.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Lỗi tìm xe theo tên: " + e.getMessage());
            return List.of();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Lấy xe theo trạng thái
     */
    public List<Car> findByStatus(Car.CarStatus status) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Car> query = session.createQuery(
                "FROM Car WHERE carStatus = :status", Car.class);
            query.setParameter("status", status);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Lỗi tìm xe theo trạng thái: " + e.getMessage());
            return List.of();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Lấy xe có sẵn (AVAILABLE)
     */
    public List<Car> getAvailableCars() {
        return findByStatus(Car.CarStatus.AVAILABLE);
    }
    
    /**
     * Lấy xe theo hãng sản xuất
     */
    public List<Car> findByProducer(CarProducer producer) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Car> query = session.createQuery(
                "FROM Car WHERE producer = :producer", Car.class);
            query.setParameter("producer", producer);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Lỗi tìm xe theo hãng: " + e.getMessage());
            return List.of();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Tìm xe theo khoảng giá
     */
    public List<Car> findByPriceRange(double minPrice, double maxPrice) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Car> query = session.createQuery(
                "FROM Car WHERE rentalPrice BETWEEN :min AND :max", Car.class);
            query.setParameter("min", minPrice);
            query.setParameter("max", maxPrice);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("❌ Lỗi tìm xe theo giá: " + e.getMessage());
            return List.of();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    // ===== UPDATE =====
    
    /**
     * Cập nhật thông tin xe
     */
    public boolean update(Car car) {
        if (car == null || !car.isValid()) {
            System.err.println("❌ Car không hợp lệ!");
            return false;
        }
        
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            car.setUpdatedDate(java.time.LocalDateTime.now());
            session.update(car);
            transaction.commit();
            
            System.out.println("✅ Cập nhật xe thành công!");
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ Lỗi cập nhật xe: " + e.getMessage());
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Thay đổi trạng thái xe
     */
    public boolean changeStatus(int carId, Car.CarStatus newStatus) {
        Car car = findById(carId);
        if (car == null) {
            System.err.println("❌ Không tìm thấy xe!");
            return false;
        }
        
        car.setCarStatus(newStatus);
        return update(car);
    }
    
    // ===== DELETE =====
    
    /**
     * Xóa xe theo ID
     */
    public boolean delete(int carId) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Car car = session.get(Car.class, carId);
            if (car == null) {
                System.err.println("❌ Không tìm thấy xe!");
                return false;
            }
            
            // Kiểm tra trạng thái trước khi xóa
            if (car.getCarStatus() != Car.CarStatus.AVAILABLE) {
                System.err.println("❌ Không thể xóa xe đang " + car.getCarStatus().displayName);
                return false;
            }
            
            session.delete(car);
            transaction.commit();
            
            System.out.println("✅ Xóa xe thành công!");
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("❌ Lỗi xóa xe: " + e.getMessage());
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    // ===== UTILITY METHODS =====
    
    /**
     * Đếm tổng số xe
     */
    public long count() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Car", Long.class);
            return query.getResultList().get(0);
        } catch (Exception e) {
            System.err.println("❌ Lỗi đếm xe: " + e.getMessage());
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Đếm xe theo trạng thái
     */
    public long countByStatus(Car.CarStatus status) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM Car WHERE carStatus = :status", Long.class);
            query.setParameter("status", status);
            return query.getResultList().get(0);
        } catch (Exception e) {
            System.err.println("❌ Lỗi đếm xe theo trạng thái: " + e.getMessage());
            return 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
