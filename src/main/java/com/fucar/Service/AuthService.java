package com.fucar.Service;

import com.fucar.entity.Account;
import com.fucar.repository.AccountRepository;

import java.security.MessageDigest;

public class AuthService {

    private final AccountRepository accountRepo = new AccountRepository();

    /**
     * Hash mật khẩu bằng SHA-256
     */
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hash password error", e);
        }
    }

    /**
     * Login bằng email + password
     */
    public Account login(String email, String password) {
        Account acc = accountRepo.findByEmail(email);
        if (acc == null) return null;

        String hashed = hashPassword(password);
        if (acc.getPasswordHash().equals(hashed)) {
            return acc;
        }
        return null;
    }

    /**
     * Register tài khoản mới
     */
    public boolean register(String email, String password, String role) {
        if (accountRepo.findByEmail(email) != null) {
            return false; // Email đã tồn tại
        }

        Account newAcc = new Account();
        newAcc.setEmail(email);
        newAcc.setPasswordHash(hashPassword(password));
        newAcc.setRole(role.toUpperCase());

        accountRepo.save(newAcc);
        return true;
    }
}
