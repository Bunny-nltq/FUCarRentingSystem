package member3.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * HibernateUtil: Singleton pattern
 * Quản lý SessionFactory của Hibernate
 */
public class HibernateUtil {
    
    private static SessionFactory sessionFactory;
    
    static {
        try {
            sessionFactory = buildSessionFactory();
        } catch (Exception e) {
            System.err.println("❌ Lỗi khởi tạo Hibernate: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Xây dựng SessionFactory từ hibernate.cfg.xml
     */
    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            
            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            throw new RuntimeException("❌ Không thể xây dựng SessionFactory: " + e);
        }
    }
    
    /**
     * Lấy SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new RuntimeException("SessionFactory chưa được khởi tạo!");
        }
        return sessionFactory;
    }
    
    /**
     * Đóng SessionFactory
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("✅ SessionFactory đóng thành công");
        }
    }
}
