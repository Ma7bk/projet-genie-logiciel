package fr.uparis.projet_genie_logiciel.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DataStoreTest {

    private DataStore store;
    private static final String TEST_FILE = "test_datastore_tmp.txt";

    @BeforeEach
    void setUp() { store = new DataStore(); }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
        new File(store.getTeachersFile()).delete();
        new File(store.getStudentsFile()).delete();
        new File(store.getQuizzesFile()).delete();
        new File(store.getQuestionsFile()).delete();
        new File(store.getScoresFile()).delete();
        new File(store.getCountersFile()).delete();
    }

    @Test
    void testWriteAndReadLines() {
        List<String> lines = Arrays.asList("ligne1", "ligne2", "ligne3");
        store.writeLines(TEST_FILE, lines);
        List<String> result = store.readLines(TEST_FILE);
        assertEquals(3, result.size());
        assertEquals("ligne1", result.get(0));
        assertEquals("ligne2", result.get(1));
        assertEquals("ligne3", result.get(2));
    }

    @Test
    void testReadLinesFileNotExists() {
        List<String> result = store.readLines("fichier_inexistant_xyz.txt");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testWriteAndReadEmptyLines() {
        store.writeLines(TEST_FILE, Arrays.asList("  ", "", "valide"));
        List<String> result = store.readLines(TEST_FILE);
        assertEquals(1, result.size());
        assertEquals("valide", result.get(0));
    }

    @Test
    void testWriteLinesOverwrite() {
        store.writeLines(TEST_FILE, Arrays.asList("ancienne"));
        store.writeLines(TEST_FILE, Arrays.asList("nouvelle1", "nouvelle2"));
        List<String> result = store.readLines(TEST_FILE);
        assertEquals(2, result.size());
        assertEquals("nouvelle1", result.get(0));
    }

    @Test
    void testGetFilenames() {
        assertEquals("teachers.txt",  store.getTeachersFile());
        assertEquals("students.txt",  store.getStudentsFile());
        assertEquals("quizzes.txt",   store.getQuizzesFile());
        assertEquals("questions.txt", store.getQuestionsFile());
        assertEquals("scores.txt",    store.getScoresFile());
        assertEquals("counters.txt",  store.getCountersFile());
    }

    @Test
    void testWriteLinesWithPipes() {
        List<String> lines = Arrays.asList("T1|Marie|Dupont|marie@u.fr|GL|pwd123");
        store.writeLines(TEST_FILE, lines);
        List<String> result = store.readLines(TEST_FILE);
        assertEquals(1, result.size());
        String[] parts = result.get(0).split("\\|");
        assertEquals(6, parts.length);
        assertEquals("T1", parts[0]);
        assertEquals("Marie", parts[1]);
    }

    @Test
    void testWriteMultipleLinesWithPipes() {
        List<String> lines = Arrays.asList(
            "T1|Marie|Dupont|marie@u.fr|GL|pwd",
            "T2|Paul|Martin|paul@u.fr|Math|pwd2"
        );
        store.writeLines(TEST_FILE, lines);
        List<String> result = store.readLines(TEST_FILE);
        assertEquals(2, result.size());
    }

    @Test
    void testReadLinesTrimmed() {
        store.writeLines(TEST_FILE, Arrays.asList("  valeur avec espaces  "));
        List<String> result = store.readLines(TEST_FILE);
        assertEquals(1, result.size());
        assertEquals("valeur avec espaces", result.get(0));
    }
}
