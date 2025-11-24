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
    // HASH PASSWORD
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
    // SAVE ACCOUNT
    // ================================
    public void save(Account acc) {
        accountRepo.save(acc);
    }

    public void update(Account acc) {
        accountRepo.update(acc);
    }

    // ================================
    // LOGIN
    // ================================
    public Account login(String email, String password) {

        Account acc = accountRepo.findByEmail(email);
        if (acc == null) return null;

        String hash = hashPassword(password);

        if (!acc.getPasswordHash().equals(hash)) return null;

        return acc;
    }

    // ================================
    // REGISTER
    // ================================
    public boolean register(String email, String password, String role, String accountName) {

        if (accountRepo.findByEmail(email) != null)
            return false;

        Account newAcc = new Account(email, hashPassword(password), role, accountName);

        accountRepo.save(newAcc);

        if (role.equalsIgnoreCase("CUSTOMER")) {

            Customer c = new Customer();

            c.setAccount(newAcc);
            c.setEmail(email);

            c.setCustomerName(accountName);
            c.setMobile("N/A");
            c.setPassword("123456");

            c.setIdentityCard("N/A");
            c.setLicenceNumber("N/A");
            c.setBirthday(null);
            c.setLicenceDate(null);

            customerRepo.save(c);
        }

        return true;
    }
}
