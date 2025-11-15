package member3.service;

import java.time.LocalDate;
import java.util.List;

import member3.entity.CarProducer;
import member3.repository.CarProducerRepository;

/**
 * Service: CarProducerService
 * Xử lý logic nghiệp vụ cho CarProducer
 * - Validate dữ liệu hãng sản xuất
 * - Kiểm tra điều kiện trước khi xóa
 */
public class CarProducerService {
    
    private final CarProducerRepository producerRepository = new CarProducerRepository();
    
    // ===== VALIDATION METHODS =====
    
    /**
     * Validate năm thành lập (1800 - năm hiện tại)
     */
    private boolean isValidFoundedYear(int year) {
        int currentYear = LocalDate.now().getYear();
        if (year < 1800 || year > currentYear) {
            System.err.println("❌ Năm thành lập phải từ 1800 đến " + currentYear);
            return false;
        }
        return true;
    }
    
    /**
     * Validate tên hãng (không được trống)
     */
    private boolean isValidProducerName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("❌ Tên hãng sản xuất không được trống");
            return false;
        }
        return true;
    }
    
    /**
     * Kiểm tra tên hãng không bị trùng
     */
    private boolean isUniqueProducerName(String name, int excludeProducerId) {
        CarProducer existing = producerRepository.findByName(name);
        if (existing != null && existing.getProducerId() != excludeProducerId) {
            System.err.println("❌ Tên hãng '" + name + "' đã tồn tại!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate toàn bộ dữ liệu hãng
     */
    private boolean validateProducer(CarProducer producer) {
        if (!isValidProducerName(producer.getProducerName())) return false;
        if (producer.getFoundedYear() > 0 && !isValidFoundedYear(producer.getFoundedYear())) {
            return false;
        }
        return true;
    }
    
    // ===== CREATE =====
    
    /**
     * Thêm hãng sản xuất mới
     */
    public boolean addProducer(CarProducer producer) {
        if (producer == null) {
            System.err.println("❌ CarProducer không được null");
            return false;
        }
        
        // Validate
        if (!validateProducer(producer)) {
            return false;
        }
        
        // Kiểm tra tên không trùng
        if (!isUniqueProducerName(producer.getProducerName(), -1)) {
            return false;
        }
        
        return producerRepository.save(producer);
    }
    
    // ===== READ =====
    
    /**
     * Lấy hãng theo ID
     */
    public CarProducer getProducerById(int producerId) {
        return producerRepository.findById(producerId);
    }
    
    /**
     * Lấy tất cả hãng sản xuất
     */
    public List<CarProducer> getAllProducers() {
        return producerRepository.findAll();
    }
    
    /**
     * Tìm hãng theo tên
     */
    public CarProducer getProducerByName(String name) {
        return producerRepository.findByName(name);
    }
    
    /**
     * Lấy hãng theo đất nước
     */
    public List<CarProducer> getProducersByCountry(String country) {
        return producerRepository.findByCountry(country);
    }
    
    /**
     * Lấy hãng theo năm thành lập
     */
    public List<CarProducer> getProducersByFoundedYear(int year) {
        return producerRepository.findByFoundedYear(year);
    }
    
    // ===== UPDATE =====
    
    /**
     * Cập nhật thông tin hãng sản xuất
     */
    public boolean updateProducer(CarProducer producer) {
        if (producer == null || producer.getProducerId() <= 0) {
            System.err.println("❌ Producer ID không hợp lệ");
            return false;
        }
        
        // Validate
        if (!validateProducer(producer)) {
            return false;
        }
        
        // Kiểm tra tên không trùng
        if (!isUniqueProducerName(producer.getProducerName(), producer.getProducerId())) {
            return false;
        }
        
        return producerRepository.update(producer);
    }
    
    // ===== DELETE (WITH BUSINESS RULES) =====
    
    /**
     * ⚠️ XÓA HÃN SẢN XUẤT - KIỂM TRA CÓ XE LIÊN QUAN
     * 
     * Quy tắc:
     * - KHÔNG xóa hãng nếu có xe liên quan
     * - CHỈ xóa được khi hãng không có xe nào
     */
    public boolean deleteProducer(int producerId) {
        CarProducer producer = producerRepository.findById(producerId);
        
        if (producer == null) {
            System.err.println("❌ Hãng sản xuất không tồn tại");
            return false;
        }
        
        // Kiểm tra xem có xe liên quan không
        if (!producer.getCars().isEmpty()) {
            System.err.println("❌ KHÔNG THỂ XÓA! Hãng còn có " + producer.getCars().size() + " chiếc xe liên quan");
            System.out.println("   Danh sách xe:");
            producer.getCars().forEach(car -> 
                System.out.println("     • " + car.getCarName() + " (" + car.getLicensePlate() + ")")
            );
            return false;
        }
        
        return producerRepository.delete(producerId);
    }
    
    // ===== STATISTICS =====
    
    /**
     * Đếm tổng số hãng
     */
    public long getTotalProducers() {
        return producerRepository.count();
    }
}
