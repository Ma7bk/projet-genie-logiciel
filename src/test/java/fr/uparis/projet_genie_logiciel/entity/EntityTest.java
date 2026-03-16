package fr.uparis.projet_genie_logiciel.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

class EntityTest {

    @Test
    void testTeacherCreation() {
        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie.dubois@u-paris.fr", "Génie Logiciel");
        assertEquals("T1", teacher.getId());
        assertEquals("Marie Dubois", teacher.getFullName());
        assertEquals("Génie Logiciel", teacher.getSubject());
    }

    @Test
    void testQuizCreation() {
        Quiz quiz = new Quiz("Q1", "Java Quiz", "Java", 30);
        assertEquals("Q1", quiz.getId());
        assertEquals("Java Quiz", quiz.getTitle());
        assertEquals("Java", quiz.getCourse());
        assertEquals(30, quiz.getDuration());
    }

    @Test
    void testQCMQuestion() {
        QCMQuestion qcm = new QCMQuestion("QU1", "Quelle est la capitale de la France ?", "Géographie");
        Choice c1 = new Choice("Paris", true);
        Choice c2 = new Choice("Lyon", false);
        qcm.addChoice(c1);
        qcm.addChoice(c2);

        assertEquals("Paris", qcm.getChoices().get(0).getText());
        assertTrue(qcm.checkAnswer(c1));
        assertFalse(qcm.checkAnswer(c2));
    }

    @Test
    void testTrueFalseQuestion() {
        TrueFalseQuestion tf = new TrueFalseQuestion("QU2", "La Terre est plate ?", "Sciences", false);
        Choice cFalse = tf.getChoices().get(1); 
        assertTrue(tf.checkAnswer(cFalse));
    }

    @Test
    void testScoreCalculation() {
        Quiz quiz = new Quiz("Q1", "Test", "Test", 10);
        Score score = new Score(quiz);
        score.addPoint();
        score.addPoint();
        
        assertEquals(2, score.getValue());
        assertTrue(score.display().contains("2/0"));
    }

    @Test
    void testChoice() {
        Choice choice = new Choice("Option A", true);
        assertEquals("Option A", choice.getText());
        assertTrue(choice.isCorrectAnswer());
    }
}
