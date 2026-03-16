package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class RepositoriesTest {

    private InMemoryStudentRepository studentRepo;
    private InMemoryTeacherRepository teacherRepo;
    private InMemoryQuestionRepository questionRepo;

    @BeforeEach
    void setUp() {
        studentRepo = new InMemoryStudentRepository();
        teacherRepo = new InMemoryTeacherRepository();
        questionRepo = new InMemoryQuestionRepository();
    }

    @Test
    void testStudentRepository() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        studentRepo.save(s);
        assertEquals(s, studentRepo.findById("S1"));
        assertEquals(1, studentRepo.findAll().size());
        assertTrue(studentRepo.delete("S1"));
        assertNull(studentRepo.findById("S1"));
    }

    @Test
    void testTeacherRepository() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        teacherRepo.save(t);
        assertEquals(t, teacherRepo.findById("T1"));
        assertEquals(1, teacherRepo.findAll().size());
        assertTrue(teacherRepo.delete("T1"));
        assertNull(teacherRepo.findById("T1"));
    }

    @Test
    void testQuestionRepository() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        questionRepo.save(q);
        assertEquals(q, questionRepo.findById("Q1"));
        assertEquals(1, questionRepo.findByCourse("Java").size());
        assertEquals(1, questionRepo.count());
        assertTrue(questionRepo.delete("Q1"));
        assertEquals(0, questionRepo.count());
    }
}
