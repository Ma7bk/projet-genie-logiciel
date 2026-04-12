package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExtraRepositoriesTest {

    private InMemoryStudentRepository studentRepo;
    private InMemoryTeacherRepository teacherRepo;
    private InMemoryQuizRepository quizRepo;
    private InMemoryQuestionRepository questionRepo;

    @BeforeEach
    void setUp() {
        studentRepo  = new InMemoryStudentRepository();
        teacherRepo  = new InMemoryTeacherRepository();
        quizRepo     = new InMemoryQuizRepository();
        questionRepo = new InMemoryQuestionRepository();
    }

    // ── Student ──────────────────────────────────────────────────────────────

    @Test
    void testStudentCountEmpty() {
        assertEquals(0, studentRepo.count());
    }

    @Test
    void testStudentCountAfterSave() {
        studentRepo.save(new Student("S1", "A", "B", "a@b.com", "2A", "pwd"));
        studentRepo.save(new Student("S2", "C", "D", "c@d.com", "2B", "pwd"));
        assertEquals(2, studentRepo.count());
    }

    @Test
    void testStudentFindAll() {
        studentRepo.save(new Student("S1", "A", "B", "a@b.com", "2A", "pwd"));
        assertEquals(1, studentRepo.findAll().size());
    }

    @Test
    void testStudentFindByIdNull() {
        assertNull(studentRepo.findById(null));
    }

    @Test
    void testStudentFindByIdEmpty() {
        assertNull(studentRepo.findById(""));
    }

    @Test
    void testStudentDeleteNonExistent() {
        assertFalse(studentRepo.delete("INEXISTANT"));
    }

    // ── Teacher ──────────────────────────────────────────────────────────────

    @Test
    void testTeacherCountEmpty() {
        assertEquals(0, teacherRepo.count());
    }

    @Test
    void testTeacherCountAfterSave() {
        teacherRepo.save(new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd"));
        teacherRepo.save(new Teacher("T2", "C", "D", "c@d.com", "Math", "pwd"));
        assertEquals(2, teacherRepo.count());
    }

    @Test
    void testTeacherFindAll() {
        teacherRepo.save(new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd"));
        assertEquals(1, teacherRepo.findAll().size());
    }

    @Test
    void testTeacherFindByIdNull() {
        assertNull(teacherRepo.findById(null));
    }

    @Test
    void testTeacherDeleteNonExistent() {
        assertFalse(teacherRepo.delete("INEXISTANT"));
    }

    // ── Quiz ─────────────────────────────────────────────────────────────────

    @Test
    void testQuizCountEmpty() {
        assertEquals(0, quizRepo.count());
    }

    @Test
    void testQuizFindAll() {
    	quizRepo.save(new Quiz("Q1", "Test", "Java", 30, "T1"));
        assertEquals(1, quizRepo.findAll().size());
    }

    @Test
    void testQuizFindByIdNull() {
        assertNull(quizRepo.findById(null));
    }

    @Test
    void testQuizDeleteNonExistent() {
        assertFalse(quizRepo.delete("INEXISTANT"));
    }

    // ── Question ─────────────────────────────────────────────────────────────

    @Test
    void testQuestionCountEmpty() {
        assertEquals(0, questionRepo.count());
    }

    @Test
    void testQuestionFindAll() {
        questionRepo.save(new QCMQuestion("QU1", "Test?", "Java"));
        assertEquals(1, questionRepo.findAll().size());
    }

    @Test
    void testQuestionFindByIdNull() {
        assertNull(questionRepo.findById(null));
    }

    @Test
    void testQuestionDeleteNonExistent() {
        assertFalse(questionRepo.delete("INEXISTANT"));
    }
}