package com.fucar.Service;

import com.fucar.entity.Account;
import com.fucar.repository.AccountRepository;

import java.security.MessageDigest;

public class AccountService {

    private final AccountRepository accountRepo = new AccountRepository();

    // Hash mật khẩu SHA-256
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hash) sb.append(String.format("%02x", b));

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Account login(String email, String password) {
        Account acc = accountRepo.findByEmail(email);
        if (acc == null) return null;

        String hash = hashPassword(password);

        if (acc.getPasswordHash().equals(hash))
            return acc;

        return null;
    }

    public boolean register(String email, String password, String role) {
        if (accountRepo.findByEmail(email) != null)
            return false;

        Account newAcc = new Account(email, hashPassword(password), role);
        accountRepo.save(newAcc);
        return true;
    }
}
