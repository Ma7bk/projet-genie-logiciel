package fr.uparis.projet_genie_logiciel.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ExtraDataStoreTest {

    private DataStore store;
    private static final String TEST_FILE = "test_extra_datastore_tmp.txt";

    @BeforeEach
    void setUp() { store = new DataStore(); }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void testWriteNullLinesDoesNotThrow() {
        assertDoesNotThrow(() -> store.writeLines(TEST_FILE, Arrays.asList()));
    }

    @Test
    void testReadLinesReturnsEmptyListForEmptyFile() {
        store.writeLines(TEST_FILE, Arrays.asList());
        List<String> result = store.readLines(TEST_FILE);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testWriteLinesMultipleOverwrites() {
        store.writeLines(TEST_FILE, Arrays.asList("v1"));
        store.writeLines(TEST_FILE, Arrays.asList("v2"));
        store.writeLines(TEST_FILE, Arrays.asList("v3"));
        List<String> result = store.readLines(TEST_FILE);
        assertEquals(1, result.size());
        assertEquals("v3", result.get(0));
    }

    @Test
    void testReadLinesOrderPreserved() {
        List<String> lines = Arrays.asList("alpha", "beta", "gamma", "delta");
        store.writeLines(TEST_FILE, lines);
        List<String> result = store.readLines(TEST_FILE);
        assertEquals("alpha", result.get(0));
        assertEquals("beta", result.get(1));
        assertEquals("gamma", result.get(2));
        assertEquals("delta", result.get(3));
    }

    @Test
    void testWriteAndReadSingleLine() {
        store.writeLines(TEST_FILE, Arrays.asList("unique"));
        List<String> result = store.readLines(TEST_FILE);
        assertEquals(1, result.size());
        assertEquals("unique", result.get(0));
    }

    @Test
    void testWriteLinesOnlyBlanksResultsInEmptyFile() {
        store.writeLines(TEST_FILE, Arrays.asList("  ", "   ", ""));
        List<String> result = store.readLines(TEST_FILE);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFilenamesAreNotNull() {
        assertNotNull(store.getTeachersFile());
        assertNotNull(store.getStudentsFile());
        assertNotNull(store.getQuizzesFile());
        assertNotNull(store.getQuestionsFile());
        assertNotNull(store.getScoresFile());
        assertNotNull(store.getCountersFile());
    }

    @Test
    void testWriteLinesWithSpecialChars() {
        store.writeLines(TEST_FILE, Arrays.asList("val|eur#1", "val|eur#2"));
        List<String> result = store.readLines(TEST_FILE);
        assertEquals(2, result.size());
        assertEquals("val|eur#1", result.get(0));
    }
}