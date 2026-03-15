package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.Quiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryQuizRepositoryTest {

    private InMemoryQuizRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryQuizRepository();
    }

    @Test
    void testRoundTripBasique() {
        Quiz quiz = new Quiz("Q1", "Java", "Java", 30);
        repository.save(quiz);
        
        Quiz found = repository.findById("Q1");
        assertNotNull(found);
        assertEquals("Java", found.getTitle());
    }

    @Test
    void testFindByIdInexistant() {
        Quiz found = repository.findById("NON_EXISTENT");
        assertNull(found);
    }

    @Test
    void testFindAllEmpty() {
        List<Quiz> all = repository.findAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void testDelete() {
        Quiz quiz = new Quiz("Q1", "Java", "Java", 30);
        repository.save(quiz);
        assertTrue(repository.delete("Q1"));
        assertNull(repository.findById("Q1"));
    }
}
