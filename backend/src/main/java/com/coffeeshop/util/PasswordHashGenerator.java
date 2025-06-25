package com.coffeeshop.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility to generate password hashes for database initialization
 */
public class PasswordHashGenerator {
    public static void main(String[] args) {
        String password = "admin123";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
        
        // Verify the hash works
        boolean isValid = BCrypt.checkpw(password, hash);
        System.out.println("Hash verification: " + isValid);
        
        // Test the current hash from database
        String currentHash = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
        boolean currentHashValid = BCrypt.checkpw(password, currentHash);
        System.out.println("Current database hash valid for 'admin123': " + currentHashValid);
    }
}
