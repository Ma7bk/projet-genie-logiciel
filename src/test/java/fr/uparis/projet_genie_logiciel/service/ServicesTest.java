package fr.uparis.projet_genie_logiciel.service;
import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicesTest {

    @Mock private TeacherRepository teacherRepo;
    @Mock private StudentRepository studentRepo;
    @Mock private QuizRepository quizRepo;
    @Mock private QuestionRepository questionRepo;

    private TeacherService teacherService;
    private StudentService studentService;
    private QuestionService questionService;
    private QuizService quizService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        teacherService  = new TeacherService(teacherRepo);
        studentService  = new StudentService(studentRepo);
        questionService = new QuestionService(questionRepo);
        quizService     = new QuizService(quizRepo, questionRepo);
        authService     = new AuthService(teacherRepo, studentRepo);
    }

    // ── TeacherService ────────────────────────────────────────────────────────
    @Test void testTeacherServiceNullRepo() {
        assertThrows(IllegalArgumentException.class, () -> new TeacherService(null));
    }
    @Test void testTeacherServiceCreate() {
        when(teacherRepo.findById("T1")).thenReturn(null);
        when(teacherRepo.findByEmail("marie@u.fr")).thenReturn(null);
        teacherService.createTeacher("T1", "Marie", "Dubois", "marie@u.fr", "GL", "pwd");
        verify(teacherRepo).save(any(Teacher.class));
    }
    @Test void testTeacherServiceCreateDuplicateId() {
        when(teacherRepo.findById("T1")).thenReturn(new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd"));
        assertThrows(IllegalStateException.class,
            () -> teacherService.createTeacher("T1", "A", "B", "a@b.com", "GL", "pwd"));
    }
    @Test void testTeacherServiceCreateDuplicateEmail() {
        when(teacherRepo.findById("T1")).thenReturn(null);
        when(teacherRepo.findByEmail("marie@u.fr")).thenReturn(new Teacher("T2", "A", "B", "marie@u.fr", "GL", "pwd"));
        assertThrows(IllegalStateException.class,
            () -> teacherService.createTeacher("T1", "Marie", "D", "marie@u.fr", "GL", "pwd"));
    }
    @Test void testTeacherServiceGetById() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u.fr", "GL", "pwd");
        when(teacherRepo.findById("T1")).thenReturn(t);
        assertEquals(t, teacherService.getTeacherById("T1"));
    }
    @Test void testTeacherServiceGetByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> teacherService.getTeacherById("X"));
    }
    @Test void testTeacherServiceDelete() {
        Teacher t = new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd");
        when(teacherRepo.findById("T1")).thenReturn(t);
        teacherService.deleteTeacher("T1");
        verify(teacherRepo).delete("T1");
    }
    @Test void testTeacherServiceDeleteNotFound() {
        assertThrows(IllegalArgumentException.class, () -> teacherService.deleteTeacher("X"));
    }
    @Test void testTeacherServiceCount() {
        when(teacherRepo.count()).thenReturn(3);
        assertEquals(3, teacherService.getTotalTeacherCount());
    }
    @Test void testTeacherServiceGetBySubject() {
        when(teacherRepo.findBySubject("GL")).thenReturn(Collections.singletonList(
            new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd")));
        assertEquals(1, teacherService.getTeachersBySubject("GL").size());
        assertThrows(IllegalArgumentException.class, () -> teacherService.getTeachersBySubject(""));
    }

    // ── StudentService ────────────────────────────────────────────────────────
    @Test void testStudentServiceNullRepo() {
        assertThrows(IllegalArgumentException.class, () -> new StudentService(null));
    }
    @Test void testStudentServiceCreate() {
        when(studentRepo.findById("S1")).thenReturn(null);
        when(studentRepo.findByEmail("jean@u.fr")).thenReturn(null);
        studentService.createStudent("S1", "Jean", "Dupont", "jean@u.fr", "2A", "pwd");
        verify(studentRepo).save(any(Student.class));
    }
    @Test void testStudentServiceCreateDuplicate() {
        when(studentRepo.findById("S1")).thenReturn(new Student("S1", "A", "B", "a@b.com", "2A", "pwd"));
        assertThrows(IllegalStateException.class,
            () -> studentService.createStudent("S1", "A", "B", "a@b.com", "2A", "pwd"));
    }
    @Test void testStudentServiceGetById() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u.fr", "2A", "pwd");
        when(studentRepo.findById("S1")).thenReturn(s);
        assertEquals(s, studentService.getStudentById("S1"));
    }
    @Test void testStudentServiceGetByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentById("X"));
    }
    @Test void testStudentServiceCount() {
        when(studentRepo.count()).thenReturn(5);
        assertEquals(5, studentService.getTotalStudentCount());
    }

    // ── QuestionService ───────────────────────────────────────────────────────
    @Test void testQuestionServiceNullRepo() {
        assertThrows(IllegalArgumentException.class, () -> new QuestionService(null));
    }
    @Test void testCreateQCM() {
        when(questionRepo.findById("QU1")).thenReturn(null);
        QCMQuestion q = questionService.createQCMQuestion("QU1", "T?", "Java",
            Arrays.asList(new Choice("A", true), new Choice("B", false)));
        assertNotNull(q);
        verify(questionRepo).save(any(QCMQuestion.class));
    }
    @Test void testCreateQCMDuplicate() {
        when(questionRepo.findById("QU1")).thenReturn(new QCMQuestion("QU1", "T", "C"));
        assertThrows(IllegalStateException.class,
            () -> questionService.createQCMQuestion("QU1", "T", "C", Collections.emptyList()));
    }
    @Test void testCreateTrueFalse() {
        when(questionRepo.findById("QU1")).thenReturn(null);
        TrueFalseQuestion q = questionService.createTrueFalseQuestion("QU1", "T?", "Java", true);
        assertNotNull(q);
    }
    @Test void testQuestionServiceGetById() {
        QCMQuestion q = new QCMQuestion("QU1", "T?", "Java");
        when(questionRepo.findById("QU1")).thenReturn(q);
        assertEquals(q, questionService.getQuestionById("QU1"));
    }
    @Test void testQuestionServiceGetByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestionById("X"));
    }
    @Test void testQuestionServiceCount() {
        when(questionRepo.count()).thenReturn(7);
        assertEquals(7, questionService.getTotalQuestionCount());
    }
    @Test void testQuestionServiceGetByCourseEmpty() {
        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestionsByCourse(""));
    }

    // ── QuizService ───────────────────────────────────────────────────────────
    @Test void testQuizServiceNullRepo() {
        assertThrows(IllegalArgumentException.class, () -> new QuizService(null, questionRepo));
        assertThrows(IllegalArgumentException.class, () -> new QuizService(quizRepo, null));
    }
    @Test void testQuizServiceCreate() {
        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie@u.fr", "GL", "pwd");
        when(quizRepo.findById("Q1")).thenReturn(null);
        Quiz quiz = quizService.createQuizByTeacher(teacher, "Q1", "Java Quiz", "Java", 30);
        assertNotNull(quiz); assertEquals("T1", quiz.getTeacherId());
        verify(quizRepo).save(any(Quiz.class));
    }
    @Test void testQuizServiceCreateNullTeacher() {
        assertThrows(IllegalArgumentException.class,
            () -> quizService.createQuizByTeacher(null, "Q1", "T", "C", 10));
    }
    @Test void testQuizServiceCreateDuplicate() {
        Teacher t = new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd");
        when(quizRepo.findById("Q1")).thenReturn(new Quiz("Q1", "X", "Java", 10, "T1"));
        assertThrows(IllegalStateException.class,
            () -> quizService.createQuizByTeacher(t, "Q1", "T", "C", 10));
    }
    @Test void testQuizServiceTakeQuiz() {
        InMemoryQuizRepository rq = new InMemoryQuizRepository();
        InMemoryQuestionRepository rqu = new InMemoryQuestionRepository();
        QuizService svc = new QuizService(rq, rqu);
        Teacher t = new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd");
        Quiz quiz = svc.createQuizByTeacher(t, "Q1", "Java", "Java", 30);
        Choice bon = new Choice("Paris", true);
        Choice mauvais = new Choice("Lyon", false);
        QCMQuestion q = new QCMQuestion("QU1", "T?", "Java");
        q.addChoice(bon); q.addChoice(mauvais);
        quiz.addQuestion(q);
        Student student = new Student("S1", "Jean", "Dupont", "jean@u.fr", "2A", "pwd");
        Score score = svc.takeQuizByStudent(student, quiz, Arrays.asList(bon));
        assertEquals(1, score.getValue());
        assertEquals(1, student.viewScoreHistory().size());
    }
    @Test void testQuizServiceTakeQuizNullParams() {
        Quiz quiz = new Quiz("Q1", "T", "C", 10, "T1");
        Student student = new Student("S1", "A", "B", "a@u.fr", "2A", "pwd");
        assertThrows(IllegalArgumentException.class,
            () -> quizService.takeQuizByStudent(null, quiz, Collections.emptyList()));
        assertThrows(IllegalArgumentException.class,
            () -> quizService.takeQuizByStudent(student, null, Collections.emptyList()));
        assertThrows(IllegalArgumentException.class,
            () -> quizService.takeQuizByStudent(student, quiz, null));
    }
    @Test void testQuizServiceGetById() {
        Quiz quiz = new Quiz("Q1", "T", "C", 10, "T1");
        when(quizRepo.findById("Q1")).thenReturn(quiz);
        assertEquals(quiz, quizService.getQuizById("Q1"));
    }
    @Test void testQuizServiceGetByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> quizService.getQuizById("X"));
    }
    @Test void testQuizServiceCount() {
        when(quizRepo.count()).thenReturn(4);
        assertEquals(4, quizService.getTotalQuizCount());
    }
    @Test void testQuizServiceGetByCourseEmpty() {
        assertThrows(IllegalArgumentException.class, () -> quizService.getQuizzesByCourse(""));
    }

    // ── AuthService ───────────────────────────────────────────────────────────
    @Test void testAuthLoginTeacherOk() {
        Teacher t = new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd123");
        when(teacherRepo.findByEmail("a@b.com")).thenReturn(t);
        assertEquals(t, authService.loginTeacher("a@b.com", "pwd123"));
    }
    @Test void testAuthLoginTeacherWrongPwd() {
        Teacher t = new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd123");
        when(teacherRepo.findByEmail("a@b.com")).thenReturn(t);
        assertNull(authService.loginTeacher("a@b.com", "wrong"));
    }
    @Test void testAuthLoginTeacherNotFound() {
        assertNull(authService.loginTeacher("unknown@b.com", "pwd"));
    }
    @Test void testAuthLoginStudentOk() {
        Student s = new Student("S1", "A", "B", "a@b.com", "2A", "pwd123");
        when(studentRepo.findByEmail("a@b.com")).thenReturn(s);
        assertEquals(s, authService.loginStudent("a@b.com", "pwd123"));
    }
    @Test void testAuthLoginStudentWrongPwd() {
        Student s = new Student("S1", "A", "B", "a@b.com", "2A", "pwd123");
        when(studentRepo.findByEmail("a@b.com")).thenReturn(s);
        assertNull(authService.loginStudent("a@b.com", "wrong"));
    }
    @Test void testAuthNullParams() {
        assertNull(authService.loginTeacher(null, "pwd"));
        assertNull(authService.loginTeacher("a@b.com", null));
        assertNull(authService.loginStudent(null, "pwd"));
        assertNull(authService.loginStudent("a@b.com", null));
    }
}
