package com.fucar.service;

import com.fucar.entity.Account;
import com.fucar.entity.Customer;
import com.fucar.repository.AccountRepository;
import com.fucar.repository.CustomerRepository;

import java.security.MessageDigest;

public class AccountService {

    private final AccountRepository accountRepo = new AccountRepository();
    private final CustomerRepository customerRepo = new CustomerRepository();

    // ================================
    // HASH PASSWORD (SHA-256)
    // ================================
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // ================================
    // LOGIN
    // ================================
    public Account login(String email, String password) {

        Account acc = accountRepo.findByEmail(email);
        if (acc == null) return null; // Email không tồn tại

        String hash = hashPassword(password);

        if (!acc.getPasswordHash().equals(hash)) {
            return null; // Sai mật khẩu
        }

        return acc; // Đăng nhập thành công
    }

    // ================================
    // REGISTER (TẠO ACCOUNT + CUSTOMER)
    // ================================
    public boolean register(String email, String password, String role, String accountName) {

        // Check tồn tại email
        if (accountRepo.findByEmail(email) != null)
            return false;

        // Tạo Account
        Account newAcc = new Account(
                email,
                hashPassword(password),
                role.toUpperCase(),
                accountName
        );

        accountRepo.save(newAcc);

        // Nếu tạo CUSTOMER → phải tạo Customer tương ứng
        if (role.equalsIgnoreCase("CUSTOMER")) {
            Customer c = new Customer();
            c.setAccount(newAcc);      // Gắn AccountID
            c.setEmail(email);         // Đồng bộ email
            customerRepo.save(c);
        }

        return true;
    }
}
