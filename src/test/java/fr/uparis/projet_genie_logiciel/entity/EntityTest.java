package fr.uparis.projet_genie_logiciel.entity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    // ── Choice ────────────────────────────────────────────────────────────────
    @Test void testChoiceCorrect() {
        Choice c = new Choice("Paris", true);
        assertEquals("Paris", c.getText());
        assertTrue(c.isCorrectAnswer());
        assertTrue(c.toString().contains("Correct"));
    }
    @Test void testChoiceWrong() {
        Choice c = new Choice("Lyon", false);
        assertFalse(c.isCorrectAnswer());
        assertFalse(c.toString().contains("Correct"));
    }
    @Test void testChoiceInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Choice("", true));
        assertThrows(IllegalArgumentException.class, () -> new Choice(null, true));
    }

    // ── QCMQuestion ───────────────────────────────────────────────────────────
    @Test void testQCMCreation() {
        QCMQuestion q = new QCMQuestion("Q1", "Capitale ?", "Geo");
        assertEquals("Q1", q.getId());
        assertEquals("Capitale ?", q.getText());
        assertEquals("Geo", q.getCourse());
        assertTrue(q.getChoices().isEmpty());
    }
    @Test void testQCMCheckAnswer() {
        QCMQuestion q = new QCMQuestion("Q1", "T?", "C");
        Choice bon = new Choice("Paris", true);
        Choice mauvais = new Choice("Lyon", false);
        q.addChoice(bon); q.addChoice(mauvais);
        assertTrue(q.checkAnswer(bon));
        assertFalse(q.checkAnswer(mauvais));
        assertFalse(q.checkAnswer(null));
    }
    @Test void testQCMMaxChoices() {
        QCMQuestion q = new QCMQuestion("Q1", "T?", "C");
        for (int i = 0; i < 6; i++) { q.addChoice(new Choice("O" + i, false)); }
        assertThrows(IllegalStateException.class, () -> q.addChoice(new Choice("X", false)));
    }
    @Test void testQCMValidate() {
        QCMQuestion q = new QCMQuestion("Q1", "T?", "C");
        assertThrows(IllegalStateException.class, q::validateChoices);
        q.addChoice(new Choice("A", true)); q.addChoice(new Choice("B", false));
        assertDoesNotThrow(q::validateChoices);
    }
    @Test void testQCMNullChoice() {
        assertThrows(IllegalArgumentException.class,
            () -> new QCMQuestion("Q1", "T?", "C").addChoice(null));
    }
    @Test void testQuestionInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new QCMQuestion("", "T", "C"));
        assertThrows(IllegalArgumentException.class, () -> new QCMQuestion(null, "T", "C"));
        assertThrows(IllegalArgumentException.class, () -> new QCMQuestion("Q1", "", "C"));
        assertThrows(IllegalArgumentException.class, () -> new QCMQuestion("Q1", "T", ""));
    }
    @Test void testQuestionEqualsHashCode() {
        QCMQuestion q1 = new QCMQuestion("Q1", "T1", "C1");
        QCMQuestion q2 = new QCMQuestion("Q1", "T2", "C2");
        QCMQuestion q3 = new QCMQuestion("Q2", "T1", "C1");
        assertEquals(q1, q2); assertNotEquals(q1, q3);
        assertEquals(q1.hashCode(), q2.hashCode());
        assertEquals(q1, q1); assertNotEquals(q1, null); assertNotEquals(q1, "str");
    }

    // ── TrueFalseQuestion ─────────────────────────────────────────────────────
    @Test void testTrueFalseTrue() {
        TrueFalseQuestion tf = new TrueFalseQuestion("Q1", "La Terre est ronde ?", "Sci", true);
        assertEquals(2, tf.getChoices().size());
        assertTrue(tf.checkAnswer(tf.getChoices().get(0)));
        assertFalse(tf.checkAnswer(tf.getChoices().get(1)));
    }
    @Test void testTrueFalseFalse() {
        TrueFalseQuestion tf = new TrueFalseQuestion("Q1", "T?", "C", false);
        assertFalse(tf.checkAnswer(tf.getChoices().get(0)));
        assertTrue(tf.checkAnswer(tf.getChoices().get(1)));
    }
    @Test void testTrueFalseGenerate() {
        TrueFalseQuestion tf = new TrueFalseQuestion("Q1", "T?", "C", true);
        tf.generateChoices(false);
        assertFalse(tf.getChoices().get(0).isCorrectAnswer());
        assertTrue(tf.getChoices().get(1).isCorrectAnswer());
    }

    // ── Quiz ──────────────────────────────────────────────────────────────────
    @Test void testQuizCreation() {
        Quiz q = new Quiz("Q1", "Java Quiz", "Java", 30, "T1");
        assertEquals("Q1", q.getId()); assertEquals("Java Quiz", q.getTitle());
        assertEquals("Java", q.getCourse()); assertEquals(30, q.getDuration());
        assertEquals("T1", q.getTeacherId()); assertTrue(q.getQuestions().isEmpty());
    }
    @Test void testQuizInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Quiz("", "T", "C", 10, "T1"));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "", "C", 10, "T1"));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "T", "", 10, "T1"));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "T", "C", 0, "T1"));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "T", "C", 10, ""));
    }
    @Test void testQuizAddQuestion() {
        Quiz quiz = new Quiz("Q1", "T", "C", 10, "T1");
        QCMQuestion q = new QCMQuestion("QU1", "T?", "C");
        quiz.addQuestion(q); quiz.addQuestion(null);
        assertEquals(1, quiz.getQuestions().size());
        assertTrue(quiz.start().contains("T"));
    }
    @Test void testQuizEqualsHashCode() {
        Quiz q1 = new Quiz("Q1", "T1", "C1", 10, "T1");
        Quiz q2 = new Quiz("Q1", "T2", "C2", 20, "T2");
        Quiz q3 = new Quiz("Q2", "T1", "C1", 10, "T1");
        assertEquals(q1, q2); assertNotEquals(q1, q3);
        assertEquals(q1, q1); assertNotEquals(q1, null); assertNotEquals(q1, "str");
    }

    // ── Score ─────────────────────────────────────────────────────────────────
    @Test void testScore() {
        Quiz quiz = new Quiz("Q1", "T", "C", 10, "T1");
        Score sc = new Score(quiz);
        assertEquals(0, sc.getValue());
        sc.addPoint(); sc.addPoint();
        assertEquals(2, sc.getValue());
        assertEquals(quiz, sc.getQuiz());
        assertTrue(sc.display().contains("T"));
    }
    @Test void testScoreNullQuiz() {
        assertThrows(IllegalArgumentException.class, () -> new Score(null));
    }

    // ── Teacher ───────────────────────────────────────────────────────────────
    @Test void testTeacherCreation() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u.fr", "GL", "pwd123");
        assertEquals("T1", t.getId()); assertEquals("Marie Dubois", t.getFullName());
        assertEquals("marie@u.fr", t.getEmail()); assertEquals("GL", t.getSubject());
        assertTrue(t.checkPassword("pwd123")); assertFalse(t.checkPassword("wrong"));
    }
    @Test void testTeacherInvalid() {
        assertThrows(IllegalArgumentException.class,
            () -> new Teacher("T1", "", "D", "a@b.com", "GL", "pwd"));
        assertThrows(IllegalArgumentException.class,
            () -> new Teacher("T1", "A", "", "a@b.com", "GL", "pwd"));
        assertThrows(IllegalArgumentException.class,
            () -> new Teacher("T1", "A", "B", "invalid", "GL", "pwd"));
        assertThrows(IllegalArgumentException.class,
            () -> new Teacher("T1", "A", "B", "a@b.com", "", "pwd"));
        assertThrows(IllegalArgumentException.class,
            () -> new Teacher("T1", "A", "B", "a@b.com", "GL", ""));
    }
    @Test void testTeacherMethods() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u.fr", "GL", "pwd");
        Quiz quiz = t.createQuiz("Q1", "Java", "Java", 30);
        assertNotNull(quiz); assertEquals("T1", quiz.getTeacherId());
        QCMQuestion q = new QCMQuestion("QU1", "T?", "C");
        t.addQuestion(quiz, q); assertEquals(1, quiz.getQuestions().size());
        t.addQuestion(null, q); t.addQuestion(quiz, null);
        assertTrue(t.defineCorrectAnswer(new Choice("A", true)));
        assertFalse(t.defineCorrectAnswer(null));
    }
    @Test void testTeacherEqualsHashCode() {
        Teacher t1 = new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd");
        Teacher t2 = new Teacher("T1", "C", "D", "c@d.com", "M", "pwd2");
        Teacher t3 = new Teacher("T2", "A", "B", "a@b.com", "GL", "pwd");
        assertEquals(t1, t2); assertNotEquals(t1, t3);
        assertEquals(t1, t1); assertNotEquals(t1, null); assertNotEquals(t1, "str");
    }

    // ── Student ───────────────────────────────────────────────────────────────
    @Test void testStudentCreation() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u.fr", "2A", "pwd123");
        assertEquals("S1", s.getId()); assertEquals("Jean Dupont", s.getFullName());
        assertEquals("jean@u.fr", s.getEmail()); assertEquals("2A", s.getClasse());
        assertTrue(s.checkPassword("pwd123"));
    }
    @Test void testStudentInvalid() {
        assertThrows(IllegalArgumentException.class,
            () -> new Student("S1", "", "B", "a@b.com", "2A", "pwd"));
        assertThrows(IllegalArgumentException.class,
            () -> new Student("S1", "A", "", "a@b.com", "2A", "pwd"));
        assertThrows(IllegalArgumentException.class,
            () -> new Student("S1", "A", "B", "invalid", "2A", "pwd"));
        assertThrows(IllegalArgumentException.class,
            () -> new Student("S1", "A", "B", "a@b.com", "", "pwd"));
    }
    @Test void testStudentScoreHistory() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u.fr", "2A", "pwd");
        assertTrue(s.viewScoreHistory().isEmpty());
        Quiz quiz = new Quiz("Q1", "T", "C", 10, "T1");
        Score sc = new Score(quiz);
        s.addScoreToHistory(sc); s.addScoreToHistory(null);
        assertEquals(1, s.viewScoreHistory().size());
    }
    @Test void testStudentMethods() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u.fr", "2A", "pwd");
        Quiz quiz = new Quiz("Q1", "T", "C", 10, "T1");
        assertDoesNotThrow(() -> s.startQuiz(quiz));
        assertDoesNotThrow(() -> s.startQuiz(null));
        QCMQuestion q = new QCMQuestion("QU1", "T?", "C");
        Choice c = new Choice("A", true);
        assertDoesNotThrow(() -> s.submitAnswer(q, c));
        assertDoesNotThrow(() -> s.submitAnswer(null, c));
        assertDoesNotThrow(() -> s.submitAnswer(q, null));
    }
    @Test void testStudentEqualsHashCode() {
        Student s1 = new Student("S1", "A", "B", "a@b.com", "2A", "pwd");
        Student s2 = new Student("S1", "C", "D", "c@d.com", "2B", "pwd2");
        Student s3 = new Student("S2", "A", "B", "a@b.com", "2A", "pwd");
        assertEquals(s1, s2); assertNotEquals(s1, s3);
        assertEquals(s1, s1); assertNotEquals(s1, null); assertNotEquals(s1, "str");
    }
    @Test void testUserInvalidId() {
        assertThrows(IllegalArgumentException.class,
            () -> new Student(null, "A", "B", "a@b.com", "2A", "pwd"));
        assertThrows(IllegalArgumentException.class,
            () -> new Student("", "A", "B", "a@b.com", "2A", "pwd"));
    }
}
