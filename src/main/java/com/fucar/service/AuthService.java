package com.fucar.service;

import com.fucar.entity.Account;
import com.fucar.repository.AccountRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {

    private final AccountRepository accountRepo = new AccountRepository();

    // ======================= HASH PASSWORD =======================
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash password error", e);
        }
    }

    // ======================= LOGIN =======================
    public Account login(String email, String password) {
        Account acc = accountRepo.findByEmail(email);
        if (acc == null) return null;

        String hashed = hashPassword(password);
        return acc.getPasswordHash().equals(hashed) ? acc : null;
    }

    // ======================= REGISTER =======================
    public boolean register(String email, String password, String role, String accountName) {

        // 1. Check email tồn tại
        if (accountRepo.findByEmail(email) != null) {
            return false;
        }

        // 2. Tạo Account mới
        Account newAcc = new Account();
        newAcc.setEmail(email);
        newAcc.setPasswordHash(hashPassword(password));
        newAcc.setRole(role.toUpperCase());
        newAcc.setAccountName(accountName); // ✅ Bắt buộc

        // 3. Lưu vào DB
        accountRepo.save(newAcc);

        return true;
    }
}
