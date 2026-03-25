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
    private InMemoryQuizRepository quizRepo;

    @BeforeEach
    void setUp() {
        studentRepo  = new InMemoryStudentRepository();
        teacherRepo  = new InMemoryTeacherRepository();
        questionRepo = new InMemoryQuestionRepository();
        quizRepo     = new InMemoryQuizRepository();
    }

  
    @Test
    void testStudentSaveAndFind() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        studentRepo.save(s);
        assertEquals(s, studentRepo.findById("S1"));
        assertEquals(1, studentRepo.findAll().size());
        assertEquals(1, studentRepo.count());
    }

    @Test
    void testStudentSaveUpdatesExisting() {
        Student s1 = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        Student s2 = new Student("S1", "Jean", "Martin", "jean2@u-paris.fr", "2B");
        studentRepo.save(s1);
        studentRepo.save(s2); 
        assertEquals(1, studentRepo.count());
        assertEquals(s2, studentRepo.findById("S1"));
    }

    @Test
    void testStudentSaveNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> studentRepo.save(null));
    }

    @Test
    void testStudentDelete() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        studentRepo.save(s);
        assertTrue(studentRepo.delete("S1"));
        assertNull(studentRepo.findById("S1"));
    }

    @Test
    void testStudentDeleteNullOrEmpty() {
        assertFalse(studentRepo.delete(null));
        assertFalse(studentRepo.delete(""));
    }

    @Test
    void testStudentFindByIdNullOrEmpty() {
        assertNull(studentRepo.findById(null));
        assertNull(studentRepo.findById(""));
    }

    @Test
    void testStudentFindByEmail() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        studentRepo.save(s);
        assertEquals(s, studentRepo.findByEmail("jean@u-paris.fr"));
        assertNull(studentRepo.findByEmail("inconnu@x.com"));
        assertNull(studentRepo.findByEmail(null));
        assertNull(studentRepo.findByEmail(""));
    }

    @Test
    void testStudentFindByClasse() {
        Student s1 = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        Student s2 = new Student("S2", "Paul", "Martin", "paul@u-paris.fr", "2B");
        studentRepo.save(s1);
        studentRepo.save(s2);

        List<Student> result = studentRepo.findByClasse("2A");
        assertEquals(1, result.size());
        assertEquals(s1, result.get(0));

        assertTrue(studentRepo.findByClasse(null).isEmpty());
        assertTrue(studentRepo.findByClasse("").isEmpty());
    }

    @Test
    void testTeacherSaveAndFind() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        teacherRepo.save(t);
        assertEquals(t, teacherRepo.findById("T1"));
        assertEquals(1, teacherRepo.findAll().size());
        assertEquals(1, teacherRepo.count());
    }

    @Test
    void testTeacherSaveUpdatesExisting() {
        Teacher t1 = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        Teacher t2 = new Teacher("T1", "Marie", "Curie", "marie2@u-paris.fr", "Physique");
        teacherRepo.save(t1);
        teacherRepo.save(t2);
        assertEquals(1, teacherRepo.count());
    }

    @Test
    void testTeacherSaveNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> teacherRepo.save(null));
    }

    @Test
    void testTeacherDelete() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        teacherRepo.save(t);
        assertTrue(teacherRepo.delete("T1"));
        assertNull(teacherRepo.findById("T1"));
    }

    @Test
    void testTeacherDeleteNullOrEmpty() {
        assertFalse(teacherRepo.delete(null));
        assertFalse(teacherRepo.delete(""));
    }

    @Test
    void testTeacherFindByIdNullOrEmpty() {
        assertNull(teacherRepo.findById(null));
        assertNull(teacherRepo.findById(""));
    }

    @Test
    void testTeacherFindByEmail() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        teacherRepo.save(t);
        assertEquals(t, teacherRepo.findByEmail("marie@u-paris.fr"));
        assertNull(teacherRepo.findByEmail("inconnu@x.com"));
        assertNull(teacherRepo.findByEmail(null));
        assertNull(teacherRepo.findByEmail(""));
    }

    @Test
    void testTeacherFindBySubject() {
        Teacher t1 = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        Teacher t2 = new Teacher("T2", "Paul", "Martin", "paul@u-paris.fr", "Maths");
        teacherRepo.save(t1);
        teacherRepo.save(t2);

        List<Teacher> result = teacherRepo.findBySubject("GL");
        assertEquals(1, result.size());

        assertTrue(teacherRepo.findBySubject(null).isEmpty());
        assertTrue(teacherRepo.findBySubject("").isEmpty());
    }


    @Test
    void testQuestionSaveAndFind() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        questionRepo.save(q);
        assertEquals(q, questionRepo.findById("Q1"));
        assertEquals(1, questionRepo.findByCourse("Java").size());
        assertEquals(1, questionRepo.count());
    }

    @Test
    void testQuestionSaveUpdatesExisting() {
        QCMQuestion q1 = new QCMQuestion("Q1", "Test ?", "Java");
        QCMQuestion q2 = new QCMQuestion("Q1", "Autre ?", "Java");
        questionRepo.save(q1);
        questionRepo.save(q2);
        assertEquals(1, questionRepo.count());
    }

    @Test
    void testQuestionSaveNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> questionRepo.save(null));
    }

    @Test
    void testQuestionDelete() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        questionRepo.save(q);
        assertTrue(questionRepo.delete("Q1"));
        assertEquals(0, questionRepo.count());
    }

    @Test
    void testQuestionDeleteNullOrEmpty() {
        assertFalse(questionRepo.delete(null));
        assertFalse(questionRepo.delete(""));
    }

    @Test
    void testQuestionFindByIdNullOrEmpty() {
        assertNull(questionRepo.findById(null));
        assertNull(questionRepo.findById(""));
    }

    @Test
    void testQuestionFindByCourseNullOrEmpty() {
        assertTrue(questionRepo.findByCourse(null).isEmpty());
        assertTrue(questionRepo.findByCourse("").isEmpty());
    }


    @Test
    void testQuizSaveAndFind() {
        Quiz q = new Quiz("Q1", "Java Quiz", "Java", 30);
        quizRepo.save(q);
        assertEquals(q, quizRepo.findById("Q1"));
        assertEquals(1, quizRepo.findAll().size());
        assertEquals(1, quizRepo.count());
    }

    @Test
    void testQuizSaveUpdatesExisting() {
        Quiz q1 = new Quiz("Q1", "Java Quiz", "Java", 30);
        Quiz q2 = new Quiz("Q1", "Updated Quiz", "Java", 45);
        quizRepo.save(q1);
        quizRepo.save(q2); // même ID → mise à jour
        assertEquals(1, quizRepo.count());
    }

    @Test
    void testQuizSaveNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> quizRepo.save(null));
    }

    @Test
    void testQuizDelete() {
        Quiz q = new Quiz("Q1", "Java Quiz", "Java", 30);
        quizRepo.save(q);
        assertTrue(quizRepo.delete("Q1"));
        assertNull(quizRepo.findById("Q1"));
    }

    @Test
    void testQuizDeleteNullOrEmpty() {
        assertFalse(quizRepo.delete(null));
        assertFalse(quizRepo.delete(""));
    }

    @Test
    void testQuizFindByIdNullOrEmpty() {
        assertNull(quizRepo.findById(null));
        assertNull(quizRepo.findById(""));
    }

    @Test
    void testQuizFindByCourse() {
        Quiz q1 = new Quiz("Q1", "Java Quiz", "Java", 30);
        Quiz q2 = new Quiz("Q2", "Python Quiz", "Python", 20);
        quizRepo.save(q1);
        quizRepo.save(q2);

        List<Quiz> result = quizRepo.findByCourse("Java");
        assertEquals(1, result.size());
        assertEquals(q1, result.get(0));

        assertTrue(quizRepo.findByCourse(null).isEmpty());
        assertTrue(quizRepo.findByCourse("").isEmpty());
    }
}
