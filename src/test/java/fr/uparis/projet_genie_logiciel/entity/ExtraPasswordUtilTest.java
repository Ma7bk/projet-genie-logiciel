package fr.uparis.projet_genie_logiciel.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExtraPasswordUtilTest {

    private static final String SAMPLE = "sampleInput";
    private static final String OTHER  = "otherInput";

    @Test
    void testHashLongInput() {
        String longInput = "a".repeat(200);
        assertEquals(64, PasswordUtil.hash(longInput).length());
    }

    @Test
    void testHashSpecialCharacters() {
        String special = "p@$$w0rd!#%^&*()";
        assertEquals(64, PasswordUtil.hash(special).length());
    }

    @Test
    void testHashIsHexadecimal() {
        assertTrue(PasswordUtil.hash(SAMPLE).matches("[0-9a-f]{64}"));
    }

    @Test
    void testHashIsDeterministic() {
        String hash1 = PasswordUtil.hash(SAMPLE);
        String hash2 = PasswordUtil.hash(SAMPLE);
        assertEquals(hash1, hash2);
        assertTrue(PasswordUtil.verify(SAMPLE, hash1));
    }

    @Test
    void testVerifyEmptyHash() {
        assertFalse(PasswordUtil.verify(SAMPLE, ""));
    }

    @Test
    void testVerifyCaseSensitive() {
        String hash = PasswordUtil.hash(SAMPLE);
        assertFalse(PasswordUtil.verify(SAMPLE.toLowerCase(), hash));
        assertFalse(PasswordUtil.verify(SAMPLE.toUpperCase(), hash));
    }

    @Test
    void testHashNumericInput() {
        String numeric = "987654321";
        assertEquals(64, PasswordUtil.hash(numeric).length());
        assertTrue(PasswordUtil.verify(numeric, PasswordUtil.hash(numeric)));
    }

    @Test
    void testHashSingleChar() {
        assertEquals(64, PasswordUtil.hash("a").length());
    }

    @Test
    void testHashDifferentInputs() {
        assertNotEquals(PasswordUtil.hash(SAMPLE), PasswordUtil.hash(OTHER));
    }
}