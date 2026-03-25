package fr.uparis.projet_genie_logiciel.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

class EntityTest {
    @Test
    void testChoiceCreation() {
        Choice choice = new Choice("Option A", true);
        assertEquals("Option A", choice.getText());
        assertTrue(choice.isCorrectAnswer());
    }

    @Test
    void testChoiceToStringCorrect() {
        Choice correct = new Choice("Paris", true);
        assertTrue(correct.toString().contains("Correct"));
    }

    @Test
    void testChoiceToStringWrong() {
        Choice wrong = new Choice("Lyon", false);
        assertFalse(wrong.toString().contains("Correct"));
    }

    @Test
    void testChoiceEmptyTextThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Choice("", true));
    }

    @Test
    void testChoiceNullTextThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Choice(null, true));
    }

    @Test
    void testChoiceBlankTextThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Choice("   ", false));
    }


    @Test
    void testQCMQuestionCreation() {
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
    void testQCMAddNullChoiceThrows() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        assertThrows(IllegalArgumentException.class, () -> q.addChoice(null));
    }

    @Test
    void testQCMMaxSixChoices() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        for (int i = 0; i < 6; i++) {
            q.addChoice(new Choice("Option " + i, false));
        }
        assertThrows(IllegalStateException.class, () -> q.addChoice(new Choice("Extra", false)));
    }

    @Test
    void testQCMValidateChoicesNotEnough() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        assertThrows(IllegalStateException.class, () -> q.validateChoices());
    }

    @Test
    void testQCMValidateChoicesOk() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        q.addChoice(new Choice("A", true));
        q.addChoice(new Choice("B", false));
        assertDoesNotThrow(() -> q.validateChoices());
    }

    @Test
    void testCheckAnswerNull() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        assertFalse(q.checkAnswer(null));
    }

    @Test
    void testQuestionInvalidIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> new QCMQuestion("", "Test ?", "Java"));
        assertThrows(IllegalArgumentException.class, () -> new QCMQuestion(null, "Test ?", "Java"));
    }

    @Test
    void testQuestionInvalidTextThrows() {
        assertThrows(IllegalArgumentException.class, () -> new QCMQuestion("Q1", "", "Java"));
        assertThrows(IllegalArgumentException.class, () -> new QCMQuestion("Q1", null, "Java"));
    }

    @Test
    void testQuestionInvalidCourseThrows() {
        assertThrows(IllegalArgumentException.class, () -> new QCMQuestion("Q1", "Test ?", ""));
        assertThrows(IllegalArgumentException.class, () -> new QCMQuestion("Q1", "Test ?", null));
    }

    @Test
    void testQuestionGetters() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        assertEquals("Q1", q.getId());
        assertEquals("Test ?", q.getText());
        assertEquals("Java", q.getCourse());
        assertTrue(q.getChoices().isEmpty());
    }

    @Test
    void testQuestionEqualsAndHashCode() {
        QCMQuestion q1 = new QCMQuestion("QU1", "T1", "C1");
        QCMQuestion q2 = new QCMQuestion("QU1", "T2", "C2");
        QCMQuestion q3 = new QCMQuestion("QU2", "T1", "C1");

        assertEquals(q1, q2);
        assertNotEquals(q1, q3);
        assertEquals(q1.hashCode(), q2.hashCode());
        assertNotEquals(q1.hashCode(), q3.hashCode());
        assertEquals(q1, q1);
        assertNotEquals(q1, null);
        assertNotEquals(q1, "autre objet");
    }


    @Test
    void testTrueFalseQuestionFalse() {
        TrueFalseQuestion tf = new TrueFalseQuestion("QU2", "La Terre est plate ?", "Sciences", false);
        Choice cFaux = tf.getChoices().get(1); 
        assertTrue(tf.checkAnswer(cFaux));
    }

    @Test
    void testTrueFalseQuestionTrue() {
        TrueFalseQuestion tf = new TrueFalseQuestion("QU3", "La Terre est ronde ?", "Sciences", true);
        Choice cVrai = tf.getChoices().get(0);
        assertTrue(tf.checkAnswer(cVrai));
        assertFalse(tf.checkAnswer(tf.getChoices().get(1)));
    }

    @Test
    void testTrueFalseGenerateChoicesRegenerates() {
        TrueFalseQuestion tf = new TrueFalseQuestion("QU4", "Test ?", "Sciences", true);
        tf.generateChoices(false); 
        assertEquals(2, tf.getChoices().size());
        assertTrue(tf.getChoices().get(1).isCorrectAnswer()); // "Faux" est correct
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
    void testQuizCreationErrors() {
        assertThrows(IllegalArgumentException.class, () -> new Quiz("", "T", "C", 10));
        assertThrows(IllegalArgumentException.class, () -> new Quiz(null, "T", "C", 10));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "", "C", 10));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", null, "C", 10));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "T", "", 10));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "T", null, 10));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "T", "C", 0));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "T", "C", -5));
    }

    @Test
    void testQuizGetFirstQuestionEmpty() {
        Quiz quiz = new Quiz("Q1", "Java Quiz", "Java", 30);
        assertNull(quiz.getFirstQuestion());
    }

    @Test
    void testQuizGetFirstQuestionWithQuestions() {
        Quiz quiz = new Quiz("Q1", "Java Quiz", "Java", 30);
        QCMQuestion q = new QCMQuestion("QU1", "Test ?", "Java");
        quiz.addQuestion(q);
        assertEquals(q, quiz.getFirstQuestion());
    }

    @Test
    void testQuizAddNullQuestionIgnored() {
        Quiz quiz = new Quiz("Q1", "Java Quiz", "Java", 30);
        quiz.addQuestion(null); 
        assertTrue(quiz.getQuestions().isEmpty());
    }

    @Test
    void testQuizStart() {
        Quiz quiz = new Quiz("Q1", "Java Quiz", "Java", 30);
        assertDoesNotThrow(() -> quiz.start());
    }

    @Test
    void testQuizEqualsAndHashCode() {
        Quiz q1 = new Quiz("Q1", "T1", "C1", 10);
        Quiz q2 = new Quiz("Q1", "T2", "C2", 20);
        Quiz q3 = new Quiz("Q2", "T1", "C1", 10);

        assertEquals(q1, q2);
        assertNotEquals(q1, q3);
        assertEquals(q1.hashCode(), q2.hashCode());
        assertNotEquals(q1.hashCode(), q3.hashCode());
        assertEquals(q1, q1);
        assertNotEquals(q1, null);
        assertNotEquals(q1, "string");
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
    void testScoreGetQuiz() {
        Quiz quiz = new Quiz("Q1", "Test", "Test", 10);
        Score score = new Score(quiz);
        assertEquals(quiz, score.getQuiz());
    }

    @Test
    void testScoreNullQuizThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Score(null));
    }

    @Test
    void testScoreDisplay() {
        Quiz quiz = new Quiz("Q1", "Mon Quiz", "Java", 10);
        Score score = new Score(quiz);
        score.addPoint();
        assertTrue(score.display().contains("Mon Quiz"));
        assertTrue(score.display().contains("1"));
    }


    @Test
    void testTeacherCreation() {
        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie.dubois@u-paris.fr", "Génie Logiciel");
        assertEquals("T1", teacher.getId());
        assertEquals("Marie Dubois", teacher.getFullName());
        assertEquals("Génie Logiciel", teacher.getSubject());
        assertEquals("marie.dubois@u-paris.fr", teacher.getEmail());
        assertEquals("Marie", teacher.getFirstName());
        assertEquals("Dubois", teacher.getLastName());
    }

    @Test
    void testTeacherCreationErrors() {
        assertThrows(IllegalArgumentException.class,
            () -> new Teacher("T1", "", "Dubois", "marie@u-paris.fr", "GL"));
        assertThrows(IllegalArgumentException.class,
            () -> new Teacher("T1", "Marie", "", "marie@u-paris.fr", "GL"));
        assertThrows(IllegalArgumentException.class,
            () -> new Teacher("T1", "Marie", "Dubois", "invalidemail", "GL"));
        assertThrows(IllegalArgumentException.class,
            () -> new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", ""));
    }

    @Test
    void testTeacherCreateQuiz() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        Quiz quiz = t.createQuiz("Q1", "Java Quiz", "Java", 30);
        assertNotNull(quiz);
        assertEquals("Q1", quiz.getId());
    }

    @Test
    void testTeacherAddQuestion() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        Quiz quiz = t.createQuiz("Q1", "Java Quiz", "Java", 30);
        QCMQuestion q = new QCMQuestion("QU1", "Test ?", "Java");
        t.addQuestion(quiz, q);
        assertEquals(1, quiz.getQuestions().size());
    }

    @Test
    void testTeacherAddQuestionNullIgnored() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        Quiz quiz = t.createQuiz("Q1", "Java Quiz", "Java", 30);
        assertDoesNotThrow(() -> t.addQuestion(null, new QCMQuestion("QU1", "Test ?", "Java")));
        assertDoesNotThrow(() -> t.addQuestion(quiz, null));
    }

    @Test
    void testTeacherDefineCorrectAnswer() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        Choice c = new Choice("Paris", true);
        assertDoesNotThrow(() -> t.defineCorrectAnswer(c));
        assertDoesNotThrow(() -> t.defineCorrectAnswer(null)); // ne doit pas planter
    }

    @Test
    void testTeacherEqualsAndHashCode() {
        Teacher t1 = new Teacher("T1", "A", "B", "a@b.com", "GL");
        Teacher t2 = new Teacher("T1", "C", "D", "c@d.com", "Maths");
        Teacher t3 = new Teacher("T2", "A", "B", "a@b.com", "GL");

        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1.hashCode(), t3.hashCode());
        assertEquals(t1, t1);
        assertNotEquals(t1, null);
        assertNotEquals(t1, "string");
    }

 

    @Test
    void testStudentCreation() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        assertEquals("S1", s.getId());
        assertEquals("Jean", s.getFirstName());
        assertEquals("Dupont", s.getLastName());
        assertEquals("jean@u-paris.fr", s.getEmail());
        assertEquals("2A", s.getClasse());
        assertEquals("Jean Dupont", s.getFullName());
    }

    @Test
    void testStudentCreationErrors() {
        assertThrows(IllegalArgumentException.class, () -> new Student("S1", "", "B", "a@b.com", "2A"));
        assertThrows(IllegalArgumentException.class, () -> new Student("S1", "A", "", "a@b.com", "2A"));
        assertThrows(IllegalArgumentException.class, () -> new Student("S1", "A", "B", "invalid", "2A"));
        assertThrows(IllegalArgumentException.class, () -> new Student("S1", "A", "B", "a@b.com", ""));
    }

    @Test
    void testStudentStartQuiz() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        Quiz quiz = new Quiz("Q1", "Test", "Java", 30);
        assertDoesNotThrow(() -> s.startQuiz(quiz));
        assertDoesNotThrow(() -> s.startQuiz(null)); 
    }

    @Test
    void testStudentSubmitAnswer() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        QCMQuestion q = new QCMQuestion("QU1", "Test ?", "Java");
        Choice c = new Choice("A", true);
        assertDoesNotThrow(() -> s.submitAnswer(q, c));
        assertDoesNotThrow(() -> s.submitAnswer(null, c));   
        assertDoesNotThrow(() -> s.submitAnswer(q, null));   
    }

    @Test
    void testStudentViewScore() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        Quiz quiz = new Quiz("Q1", "Test", "Java", 30);
        Score score = new Score(quiz);
        assertDoesNotThrow(() -> s.viewScore(score));
        assertDoesNotThrow(() -> s.viewScore(null)); 
    }

    @Test
    void testStudentScoreHistory() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        Quiz quiz = new Quiz("Q1", "Test", "Java", 30);
        Score score = new Score(quiz);

        assertTrue(s.viewScoreHistory().isEmpty());
        s.addScoreToHistory(score);
        assertEquals(1, s.viewScoreHistory().size());
        s.addScoreToHistory(null);
        assertEquals(1, s.viewScoreHistory().size());
    }

    @Test
    void testStudentEqualsAndHashCode() {
        Student s1 = new Student("S1", "A", "B", "a@b.com", "2A");
        Student s2 = new Student("S1", "C", "D", "c@d.com", "2B");
        Student s3 = new Student("S2", "A", "B", "a@b.com", "2A");

        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1.hashCode(), s3.hashCode());
        assertEquals(s1, s1);
        assertNotEquals(s1, null);
        assertNotEquals(s1, "string");
    }
}
