package fr.uparis.projet_genie_logiciel.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testStudentCreationNominal() {
        Student student = new Student("S1", "Jean", "Dupont", "jean.dupont@u-paris.fr", "2A SIE");
        assertEquals("S1", student.getId());
        assertEquals("Jean Dupont", student.getFullName());
        assertEquals("2A SIE", student.getClasse().toUpperCase());
    }

    @Test
    void testStudentCreationInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Student("", "Jean", "Dupont", "jean.dupont@u-paris.fr", "2A SIE");
        });
    }

    @Test
    void testStudentCreationInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Student("S1", "Jean", "Dupont", "invalid-email", "2A SIE");
        });
    }

    @Test
    void testScoreHistory() {
        Student student = new Student("S1", "Jean", "Dupont", "jean.dupont@u-paris.fr", "2A SIE");
        Quiz quiz = new Quiz("Q1", "Java Quiz", "Java", 30);
        Score score = new Score(quiz);
        score.addPoint();
        
        student.addScoreToHistory(score);
        assertEquals(1, student.viewScoreHistory().size());
        assertEquals(1, student.viewScoreHistory().get(0).getValue());
    }
}
