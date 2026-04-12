package fr.uparis.projet_genie_logiciel.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExtraPasswordUtilTest {

    @Test
    void testHashLongPassword() {
        String longPwd = "a".repeat(200);
        assertEquals(64, PasswordUtil.hash(longPwd).length());
    }

    @Test
    void testHashSpecialCharacters() {
        String special = "p@$$w0rd!#%^&*()";
        assertEquals(64, PasswordUtil.hash(special).length());
    }

    @Test
    void testHashIsHexadecimal() {
        assertTrue(PasswordUtil.hash("testPassword").matches("[0-9a-f]{64}"));
    }

    @Test
    void testVerifyAfterMultipleHashes() {
        String pwd = "multipleTest";
        String hash1 = PasswordUtil.hash(pwd);
        String hash2 = PasswordUtil.hash(pwd);
        assertEquals(hash1, hash2);
        assertTrue(PasswordUtil.verify(pwd, hash1));
    }

    @Test
    void testVerifyEmptyHash() {
        assertFalse(PasswordUtil.verify("pwd", ""));
    }

    @Test
    void testVerifyCaseSensitive() {
        String hash = PasswordUtil.hash("Password");
        assertFalse(PasswordUtil.verify("password", hash));
        assertFalse(PasswordUtil.verify("PASSWORD", hash));
    }

    @Test
    void testHashNumericPassword() {
        String numPwd = "123456789";
        assertEquals(64, PasswordUtil.hash(numPwd).length());
        assertTrue(PasswordUtil.verify(numPwd, PasswordUtil.hash(numPwd)));
    }

    @Test
    void testHashSingleChar() {
        assertEquals(64, PasswordUtil.hash("a").length());
    }
}