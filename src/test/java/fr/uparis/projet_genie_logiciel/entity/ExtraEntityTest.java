package fr.uparis.projet_genie_logiciel.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExtraEntityTest {

    // ── Choice ──────────────────────────────────────────────────────────────

    @Test
    void testChoiceIsCorrectAnswer() {
        Choice c1 = new Choice("Vrai", true);
        Choice c2 = new Choice("Faux", false);
        assertTrue(c1.isCorrectAnswer());
        assertFalse(c2.isCorrectAnswer());
    }

    @Test
    void testChoiceTextIsPreserved() {
        Choice c = new Choice("Option X", false);
        assertEquals("Option X", c.getText());
    }

    // ── QCMQuestion ─────────────────────────────────────────────────────────

    @Test
    void testQCMQuestionGetCourse() {
        QCMQuestion q = new QCMQuestion("Q1", "Question ?", "Maths");
        assertEquals("Maths", q.getCourse());
    }

    @Test
    void testQCMQuestionMultipleChoicesAdded() {
        QCMQuestion q = new QCMQuestion("Q1", "Question ?", "Java");
        q.addChoice(new Choice("A", true));
        q.addChoice(new Choice("B", false));
        q.addChoice(new Choice("C", false));
        assertEquals(3, q.getChoices().size());
    }

    @Test
    void testQCMCheckAnswerCorrect() {
        QCMQuestion q = new QCMQuestion("Q1", "Question ?", "Java");
        Choice bon = new Choice("A", true);
        q.addChoice(bon);
        assertTrue(q.checkAnswer(bon));
    }

    @Test
    void testQCMCheckAnswerWrong() {
        QCMQuestion q = new QCMQuestion("Q1", "Question ?", "Java");
        Choice mauvais = new Choice("B", false);
        q.addChoice(mauvais);
        assertFalse(q.checkAnswer(mauvais));
    }

    // ── TrueFalseQuestion ────────────────────────────────────────────────────

    @Test
    void testTrueFalseHasTwoChoices() {
        TrueFalseQuestion tf = new TrueFalseQuestion("TF1", "Test ?", "Sciences", true);
        assertEquals(2, tf.getChoices().size());
    }

    @Test
    void testTrueFalseCorrectAnswerIsTrue() {
        TrueFalseQuestion tf = new TrueFalseQuestion("TF1", "Test ?", "Sciences", true);
        Choice vrai = tf.getChoices().get(0);
        assertTrue(tf.checkAnswer(vrai));
    }

    @Test
    void testTrueFalseCorrectAnswerIsFalse() {
        TrueFalseQuestion tf = new TrueFalseQuestion("TF2", "Test ?", "Sciences", false);
        Choice faux = tf.getChoices().get(1);
        assertTrue(tf.checkAnswer(faux));
    }

    // ── Quiz ─────────────────────────────────────────────────────────────────

    @Test
    void testQuizAddMultipleQuestions() {
        Quiz quiz = new Quiz("Q1", "Mon Quiz", "Java", 30);
        quiz.addQuestion(new QCMQuestion("QU1", "Q1 ?", "Java"));
        quiz.addQuestion(new QCMQuestion("QU2", "Q2 ?", "Java"));
        quiz.addQuestion(new TrueFalseQuestion("TF1", "Q3 ?", "Java", true));
        assertEquals(3, quiz.getQuestions().size());
    }

    @Test
    void testQuizGetFirstQuestionAfterAdd() {
        Quiz quiz = new Quiz("Q1", "Mon Quiz", "Java", 30);
        QCMQuestion q = new QCMQuestion("QU1", "Première ?", "Java");
        quiz.addQuestion(q);
        assertEquals(q, quiz.getFirstQuestion());
    }

    // ── Score ────────────────────────────────────────────────────────────────

    @Test
    void testScoreInitialValueIsZero() {
        Quiz quiz = new Quiz("Q1", "Test", "Java", 10);
        Score score = new Score(quiz);
        assertEquals(0, score.getValue());
    }

    @Test
    void testScoreAddMultiplePoints() {
        Quiz quiz = new Quiz("Q1", "Test", "Java", 10);
        Score score = new Score(quiz);
        score.addPoint();
        score.addPoint();
        score.addPoint();
        assertEquals(3, score.getValue());
    }

    // ── Student ──────────────────────────────────────────────────────────────

    @Test
    void testStudentFullName() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        assertEquals("Jean Dupont", s.getFullName());
    }

    @Test
    void testStudentScoreHistoryInitiallyEmpty() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        assertTrue(s.viewScoreHistory().isEmpty());
    }

    @Test
    void testStudentAddScoreToHistory() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        Quiz quiz = new Quiz("Q1", "Test", "Java", 10);
        Score score = new Score(quiz);
        s.addScoreToHistory(score);
        assertEquals(1, s.viewScoreHistory().size());
    }

    // ── Teacher ──────────────────────────────────────────────────────────────

    @Test
    void testTeacherFullName() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        assertEquals("Marie Dubois", t.getFullName());
    }

    @Test
    void testTeacherCreateQuizNotNull() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        Quiz quiz = t.createQuiz("Q1", "Quiz GL", "GL", 20);
        assertNotNull(quiz);
    }
}