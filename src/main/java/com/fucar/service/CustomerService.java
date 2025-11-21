package com.fucar.service;

import com.fucar.entity.Account;
import com.fucar.entity.Customer;
import com.fucar.repository.CustomerRepository;

import java.util.List;

public class CustomerService {

    private final CustomerRepository repo = new CustomerRepository();

    // =====================================================
    // CRUD CƠ BẢN
    // =====================================================

    public void addCustomer(Customer customer) {
        repo.save(customer);
    }

    public void updateCustomer(Customer customer) {
        repo.update(customer);
    }

    public void deleteCustomer(int customerID) {
        repo.deleteById(customerID);
    }

    public Customer getCustomerById(int id) {
        return repo.findById(id);
    }

    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    // =====================================================
    // CHECK EMAIL — email trong bảng CUSTOMER
    // (chỉ dùng nếu login kiểu cũ, hiện bạn KHÔNG dùng)
    // =====================================================

    public boolean isEmailTaken(String email) {
        return repo.findByEmail(email) != null;
    }

    // =====================================================
    // DÙNG SAU KHI LOGIN ACCOUNT
    // Lấy Customer dựa trên AccountID
    // =====================================================

    public Customer findByAccountId(int accountId) {
        return repo.findByAccountId(accountId);
    }

    // =====================================================
    // HÀM QUAN TRỌNG — TẠO CUSTOMER MẶC ĐỊNH SAU KHI ĐĂNG KÝ ACCOUNT
    // =====================================================

    public Customer createDefaultCustomer(Account account) {

        Customer c = new Customer();

        // Nếu bảng Customer có Email riêng → copy từ Account
        c.setEmail(account.getEmail());

        // Các trường còn lại tạm để trống hoặc “N/A”
        c.setCustomerName(account.getAccountName());   // tên mặc định
        c.setMobile("N/A");
        c.setAddress("N/A");
        c.setBirthday("N/A");
        c.setIdentityCard("N/A");
        c.setLicenceNumber("N/A");
        c.setLicenceDate("N/A");
        c.setPassword("N/A");

        // RẤT QUAN TRỌNG: Gắn Account →
        c.setAccount(account);

        repo.save(c);

        return c;
    }
}
