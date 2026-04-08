package fr.uparis.projet_genie_logiciel.repository;
import fr.uparis.projet_genie_logiciel.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RepositoriesTest {
    private InMemoryTeacherRepository teacherRepo;
    private InMemoryStudentRepository studentRepo;
    private InMemoryQuizRepository quizRepo;
    private InMemoryQuestionRepository questionRepo;

    @BeforeEach
    void setUp() {
        teacherRepo  = new InMemoryTeacherRepository();
        studentRepo  = new InMemoryStudentRepository();
        quizRepo     = new InMemoryQuizRepository();
        questionRepo = new InMemoryQuestionRepository();
    }

    // ── Teacher ───────────────────────────────────────────────────────────────
    @Test void testTeacherSaveFind() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u.fr", "GL", "pwd");
        teacherRepo.save(t);
        assertEquals(t, teacherRepo.findById("T1"));
        assertEquals(1, teacherRepo.count());
    }
    @Test void testTeacherSaveUpdates() {
        teacherRepo.save(new Teacher("T1", "A", "B", "a@u.fr", "GL", "pwd"));
        teacherRepo.save(new Teacher("T1", "C", "D", "c@u.fr", "M", "pwd2"));
        assertEquals(1, teacherRepo.count());
    }
    @Test void testTeacherSaveNull() {
        assertThrows(IllegalArgumentException.class, () -> teacherRepo.save(null));
    }
    @Test void testTeacherDelete() {
        teacherRepo.save(new Teacher("T1", "A", "B", "a@u.fr", "GL", "pwd"));
        assertTrue(teacherRepo.delete("T1"));
        assertNull(teacherRepo.findById("T1"));
        assertFalse(teacherRepo.delete(null));
        assertFalse(teacherRepo.delete(""));
    }
    @Test void testTeacherFindByEmail() {
        Teacher t = new Teacher("T1", "A", "B", "marie@u.fr", "GL", "pwd");
        teacherRepo.save(t);
        assertEquals(t, teacherRepo.findByEmail("marie@u.fr"));
        assertNull(teacherRepo.findByEmail("other@u.fr"));
        assertNull(teacherRepo.findByEmail(null));
    }
    @Test void testTeacherFindBySubject() {
        teacherRepo.save(new Teacher("T1", "A", "B", "a@u.fr", "GL", "pwd"));
        teacherRepo.save(new Teacher("T2", "C", "D", "c@u.fr", "Math", "pwd"));
        assertEquals(1, teacherRepo.findBySubject("GL").size());
        assertTrue(teacherRepo.findBySubject(null).isEmpty());
        assertTrue(teacherRepo.findBySubject("").isEmpty());
    }

    // ── Student ───────────────────────────────────────────────────────────────
    @Test void testStudentSaveFind() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u.fr", "2A", "pwd");
        studentRepo.save(s);
        assertEquals(s, studentRepo.findById("S1"));
        assertEquals(1, studentRepo.count());
    }
    @Test void testStudentSaveNull() {
        assertThrows(IllegalArgumentException.class, () -> studentRepo.save(null));
    }
    @Test void testStudentDelete() {
        studentRepo.save(new Student("S1", "A", "B", "a@u.fr", "2A", "pwd"));
        assertTrue(studentRepo.delete("S1"));
        assertNull(studentRepo.findById("S1"));
        assertFalse(studentRepo.delete(null));
    }
    @Test void testStudentFindByEmail() {
        Student s = new Student("S1", "A", "B", "jean@u.fr", "2A", "pwd");
        studentRepo.save(s);
        assertEquals(s, studentRepo.findByEmail("jean@u.fr"));
        assertNull(studentRepo.findByEmail("other@u.fr"));
    }
    @Test void testStudentFindByClasse() {
        studentRepo.save(new Student("S1", "A", "B", "a@u.fr", "2A", "pwd"));
        studentRepo.save(new Student("S2", "C", "D", "c@u.fr", "2B", "pwd"));
        assertEquals(1, studentRepo.findByClasse("2A").size());
        assertTrue(studentRepo.findByClasse(null).isEmpty());
    }

    // ── Quiz ──────────────────────────────────────────────────────────────────
    @Test void testQuizSaveFind() {
        Quiz q = new Quiz("Q1", "Java Quiz", "Java", 30, "T1");
        quizRepo.save(q);
        assertEquals(q, quizRepo.findById("Q1"));
        assertEquals(1, quizRepo.count());
    }
    @Test void testQuizSaveNull() {
        assertThrows(IllegalArgumentException.class, () -> quizRepo.save(null));
    }
    @Test void testQuizDelete() {
        quizRepo.save(new Quiz("Q1", "T", "C", 10, "T1"));
        assertTrue(quizRepo.delete("Q1"));
        assertNull(quizRepo.findById("Q1"));
        assertFalse(quizRepo.delete(null));
    }
    @Test void testQuizFindByTeacherId() {
        quizRepo.save(new Quiz("Q1", "T", "C", 10, "T1"));
        quizRepo.save(new Quiz("Q2", "T", "C", 10, "T2"));
        assertEquals(1, quizRepo.findByTeacherId("T1").size());
        assertTrue(quizRepo.findByTeacherId(null).isEmpty());
    }
    @Test void testQuizFindByCourse() {
        quizRepo.save(new Quiz("Q1", "T", "Java", 10, "T1"));
        quizRepo.save(new Quiz("Q2", "T", "Python", 10, "T1"));
        assertEquals(1, quizRepo.findByCourse("Java").size());
        assertTrue(quizRepo.findByCourse(null).isEmpty());
    }

    // ── Question ──────────────────────────────────────────────────────────────
    @Test void testQuestionSaveFind() {
        QCMQuestion q = new QCMQuestion("QU1", "T?", "Java");
        questionRepo.save(q);
        assertEquals(q, questionRepo.findById("QU1"));
        assertEquals(1, questionRepo.count());
    }
    @Test void testQuestionSaveNull() {
        assertThrows(IllegalArgumentException.class, () -> questionRepo.save(null));
    }
    @Test void testQuestionDelete() {
        questionRepo.save(new QCMQuestion("QU1", "T?", "Java"));
        assertTrue(questionRepo.delete("QU1"));
        assertEquals(0, questionRepo.count());
        assertFalse(questionRepo.delete(null));
    }
    @Test void testQuestionFindByCourse() {
        questionRepo.save(new QCMQuestion("QU1", "T?", "Java"));
        questionRepo.save(new QCMQuestion("QU2", "T?", "Python"));
        assertEquals(1, questionRepo.findByCourse("Java").size());
        assertTrue(questionRepo.findByCourse(null).isEmpty());
    }
}
