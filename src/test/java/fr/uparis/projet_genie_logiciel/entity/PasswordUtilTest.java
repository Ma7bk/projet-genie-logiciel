package fr.uparis.projet_genie_logiciel.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void testHashNotNull() {
        assertNotNull(PasswordUtil.hash("monMotDePasse"));
    }

    @Test
    void testHashLength() {
        // SHA-256 produit toujours 64 caracteres hexa
        assertEquals(64, PasswordUtil.hash("test").length());
    }

    @Test
    void testHashDeterministe() {
        // Le meme mot de passe doit toujours donner le meme hash
        assertEquals(PasswordUtil.hash("abc123"), PasswordUtil.hash("abc123"));
    }

    @Test
    void testHashDifferent() {
        // Deux mots de passe differents donnent des hash differents
        assertNotEquals(PasswordUtil.hash("abc"), PasswordUtil.hash("ABC"));
    }

    @Test
    void testHashNotReversible() {
        // Le hash ne doit pas contenir le mot de passe en clair
        String pwd = "monSecret";
        assertFalse(PasswordUtil.hash(pwd).contains(pwd));
    }

    @Test
    void testVerifyCorrect() {
        String hash = PasswordUtil.hash("password123");
        assertTrue(PasswordUtil.verify("password123", hash));
    }

    @Test
    void testVerifyWrong() {
        String hash = PasswordUtil.hash("password123");
        assertFalse(PasswordUtil.verify("mauvaisMotDePasse", hash));
    }

    @Test
    void testVerifyNullPassword() {
        String hash = PasswordUtil.hash("pwd");
        assertFalse(PasswordUtil.verify(null, hash));
    }

    @Test
    void testVerifyNullHash() {
        assertFalse(PasswordUtil.verify("pwd", null));
    }

    @Test
    void testVerifyBothNull() {
        assertFalse(PasswordUtil.verify(null, null));
    }

    @Test
    void testHashNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hash(null));
    }

    @Test
    void testHashEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hash(""));
    }

    @Test
    void testVerifyWithSpaces() {
        // Les espaces sont trimmes avant hachage
        String hash = PasswordUtil.hash("  pwd123  ");
        assertTrue(PasswordUtil.verify("pwd123", hash));
    }
}
