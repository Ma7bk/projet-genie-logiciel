package fr.uparis.projet_genie_logiciel.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public final class PasswordUtil {

    private PasswordUtil() { }

    
    public static String hash(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas etre vide");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plainPassword.trim().getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 non disponible", e);
        }
    }

    
    public static boolean verify(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) { return false; }
        return hash(plainPassword).equals(storedHash);
    }
}
