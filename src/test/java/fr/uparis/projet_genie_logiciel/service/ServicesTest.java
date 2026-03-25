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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicesTest {

    @Mock private StudentRepository studentRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private QuestionRepository questionRepository;
    @Mock private QuizRepository quizRepository;

    private StudentService studentService;
    private TeacherService teacherService;
    private QuestionService questionService;
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        studentService  = new StudentService(studentRepository);
        teacherService  = new TeacherService(teacherRepository);
        questionService = new QuestionService(questionRepository);
        quizService     = new QuizService(quizRepository, questionRepository);
    }

   
    @Test
    void testStudentServiceNullRepoThrows() {
        assertThrows(IllegalArgumentException.class, () -> new StudentService(null));
    }

    @Test
    void testStudentServiceCreateOk() {
        when(studentRepository.findById("S1")).thenReturn(null);
        when(studentRepository.findByEmail("jean@u-paris.fr")).thenReturn(null);

        studentService.createStudent("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void testStudentServiceCreateDuplicateIdThrows() {
        when(studentRepository.findById("S1"))
            .thenReturn(new Student("S1", "A", "B", "a@b.com", "2A"));
        assertThrows(IllegalStateException.class,
            () -> studentService.createStudent("S1", "A", "B", "a@b.com", "2A"));
    }

    @Test
    void testStudentServiceCreateDuplicateEmailThrows() {
        when(studentRepository.findById("S1")).thenReturn(null);
        when(studentRepository.findByEmail("a@b.com"))
            .thenReturn(new Student("S2", "A", "B", "a@b.com", "2A"));
        assertThrows(IllegalStateException.class,
            () -> studentService.createStudent("S1", "A", "B", "a@b.com", "2A"));
    }

    @Test
    void testStudentServiceGetById() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        when(studentRepository.findById("S1")).thenReturn(s);
        assertEquals(s, studentService.getStudentById("S1"));
    }

    @Test
    void testStudentServiceGetByIdNotFoundThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> studentService.getStudentById("INCONNU"));
    }

    @Test
    void testStudentServiceGetByEmail() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        when(studentRepository.findByEmail("jean@u-paris.fr")).thenReturn(s);
        assertEquals(s, studentService.getStudentByEmail("jean@u-paris.fr"));
    }

    @Test
    void testStudentServiceGetByEmailNotFoundThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> studentService.getStudentByEmail("inconnu@x.com"));
    }

    @Test
    void testStudentServiceGetByClasseEmptyThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> studentService.getStudentsByClasse(""));
    }

    @Test
    void testStudentServiceGetByClasse() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        when(studentRepository.findByClasse("2A")).thenReturn(Arrays.asList(s));
        assertEquals(1, studentService.getStudentsByClasse("2A").size());
    }

    @Test
    void testStudentServiceGetAll() {
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(studentService.getAllStudents().isEmpty());
    }

    @Test
    void testStudentServiceCount() {
        when(studentRepository.count()).thenReturn(3);
        assertEquals(3, studentService.getTotalStudentCount());
    }

    @Test
    void testStudentServiceDeleteOk() {
        Student s = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        when(studentRepository.findById("S1")).thenReturn(s);
        assertDoesNotThrow(() -> studentService.deleteStudent("S1"));
        verify(studentRepository).delete("S1");
    }

    @Test
    void testStudentServiceDeleteNotFoundThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> studentService.deleteStudent("INCONNU"));
    }

    
    @Test
    void testTeacherServiceNullRepoThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TeacherService(null));
    }

    @Test
    void testTeacherServiceCreateOk() {
        when(teacherRepository.findById("T1")).thenReturn(null);
        when(teacherRepository.findByEmail("marie@u-paris.fr")).thenReturn(null);

        teacherService.createTeacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void testTeacherServiceCreateDuplicateIdThrows() {
        when(teacherRepository.findById("T1"))
            .thenReturn(new Teacher("T1", "A", "B", "a@b.com", "GL"));
        assertThrows(IllegalStateException.class,
            () -> teacherService.createTeacher("T1", "A", "B", "a@b.com", "GL"));
    }

    @Test
    void testTeacherServiceCreateDuplicateEmailThrows() {
        when(teacherRepository.findById("T1")).thenReturn(null);
        when(teacherRepository.findByEmail("marie@u-paris.fr"))
            .thenReturn(new Teacher("T2", "A", "B", "marie@u-paris.fr", "GL"));
        assertThrows(IllegalStateException.class,
            () -> teacherService.createTeacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL"));
    }

    @Test
    void testTeacherServiceGetById() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        when(teacherRepository.findById("T1")).thenReturn(t);
        assertEquals(t, teacherService.getTeacherById("T1"));
    }

    @Test
    void testTeacherServiceGetByIdNotFoundThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> teacherService.getTeacherById("INCONNU"));
    }

    @Test
    void testTeacherServiceGetByEmail() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        when(teacherRepository.findByEmail("marie@u-paris.fr")).thenReturn(t);
        assertEquals(t, teacherService.getTeacherByEmail("marie@u-paris.fr"));
    }

    @Test
    void testTeacherServiceGetByEmailNotFoundThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> teacherService.getTeacherByEmail("inconnu@x.com"));
    }

    @Test
    void testTeacherServiceGetBySubjectEmptyThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> teacherService.getTeachersBySubject(""));
    }

    @Test
    void testTeacherServiceGetBySubject() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        when(teacherRepository.findBySubject("GL")).thenReturn(Arrays.asList(t));
        assertEquals(1, teacherService.getTeachersBySubject("GL").size());
    }

    @Test
    void testTeacherServiceGetAll() {
        when(teacherRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(teacherService.getAllTeachers().isEmpty());
    }

    @Test
    void testTeacherServiceCount() {
        when(teacherRepository.count()).thenReturn(2);
        assertEquals(2, teacherService.getTotalTeacherCount());
    }

    @Test
    void testTeacherServiceDeleteOk() {
        Teacher t = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        when(teacherRepository.findById("T1")).thenReturn(t);
        assertDoesNotThrow(() -> teacherService.deleteTeacher("T1"));
        verify(teacherRepository).delete("T1");
    }

    @Test
    void testTeacherServiceDeleteNotFoundThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> teacherService.deleteTeacher("INCONNU"));
    }



    @Test
    void testQuestionServiceNullRepoThrows() {
        assertThrows(IllegalArgumentException.class, () -> new QuestionService(null));
    }

    @Test
    void testQuestionServiceCreateQCMOk() {
        when(questionRepository.findById("Q1")).thenReturn(null);
        List<Choice> choices = Arrays.asList(new Choice("A", true), new Choice("B", false));
        QCMQuestion q = questionService.createQCMQuestion("Q1", "Test ?", "Java", choices);
        assertNotNull(q);
        verify(questionRepository).save(any(QCMQuestion.class));
    }

    @Test
    void testQuestionServiceCreateQCMDuplicateThrows() {
        when(questionRepository.findById("Q1"))
            .thenReturn(new QCMQuestion("Q1", "A", "B"));
        assertThrows(IllegalStateException.class,
            () -> questionService.createQCMQuestion("Q1", "A", "B", Collections.emptyList()));
    }

    @Test
    void testQuestionServiceCreateTrueFalseOk() {
        when(questionRepository.findById("Q1")).thenReturn(null);
        TrueFalseQuestion q = questionService.createTrueFalseQuestion("Q1", "Test ?", "Java", true);
        assertNotNull(q);
        verify(questionRepository).save(any(TrueFalseQuestion.class));
    }

    @Test
    void testQuestionServiceCreateTrueFalseDuplicateThrows() {
        when(questionRepository.findById("Q1"))
            .thenReturn(new QCMQuestion("Q1", "A", "B"));
        assertThrows(IllegalStateException.class,
            () -> questionService.createTrueFalseQuestion("Q1", "A", "B", true));
    }

    @Test
    void testQuestionServiceGetById() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        when(questionRepository.findById("Q1")).thenReturn(q);
        assertEquals(q, questionService.getQuestionById("Q1"));
    }

    @Test
    void testQuestionServiceGetByIdNotFoundThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> questionService.getQuestionById("INCONNU"));
    }

    @Test
    void testQuestionServiceGetByCourseEmptyThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> questionService.getQuestionsByCourse(""));
    }

    @Test
    void testQuestionServiceGetByCourse() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        when(questionRepository.findByCourse("Java")).thenReturn(Arrays.asList(q));
        assertEquals(1, questionService.getQuestionsByCourse("Java").size());
    }

    @Test
    void testQuestionServiceGetAll() {
        when(questionRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(questionService.getAllQuestions().isEmpty());
    }

    @Test
    void testQuestionServiceCount() {
        when(questionRepository.count()).thenReturn(5);
        assertEquals(5, questionService.getTotalQuestionCount());
    }

    @Test
    void testQuestionServiceDeleteOk() {
        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        when(questionRepository.findById("Q1")).thenReturn(q);
        assertDoesNotThrow(() -> questionService.deleteQuestion("Q1"));
        verify(questionRepository).delete("Q1");
    }

    @Test
    void testQuestionServiceDeleteNotFoundThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> questionService.deleteQuestion("INCONNU"));
    }

  
    @Test
    void testQuizServiceNullRepoThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new QuizService(null, questionRepository));
        assertThrows(IllegalArgumentException.class,
            () -> new QuizService(quizRepository, null));
    }

    @Test
    void testQuizServiceCreateOk() {
        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        when(quizRepository.findById("Q1")).thenReturn(null);

        Quiz quiz = quizService.createQuizByTeacher(teacher, "Q1", "Java Quiz", "Java", 30);
        assertNotNull(quiz);
        verify(quizRepository).save(any(Quiz.class));
    }

    @Test
    void testQuizServiceCreateNullTeacherThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> quizService.createQuizByTeacher(null, "Q1", "Java Quiz", "Java", 30));
    }

    @Test
    void testQuizServiceCreateDuplicateThrows() {
        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        when(quizRepository.findById("Q1"))
            .thenReturn(new Quiz("Q1", "Existing", "Java", 10));
        assertThrows(IllegalStateException.class,
            () -> quizService.createQuizByTeacher(teacher, "Q1", "Java Quiz", "Java", 30));
    }

    @Test
    void testQuizServiceTakeQuizOk() {
        // Utiliser des repos réels pour ce test d'intégration
        InMemoryQuizRepository realQuizRepo = new InMemoryQuizRepository();
        InMemoryQuestionRepository realQuestionRepo = new InMemoryQuestionRepository();
        QuizService realService = new QuizService(realQuizRepo, realQuestionRepo);

        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        Quiz quiz = realService.createQuizByTeacher(teacher, "Q1", "Java Quiz", "Java", 30);

        QCMQuestion q = new QCMQuestion("QU1", "Test ?", "Java");
        Choice bon = new Choice("Paris", true);
        Choice mauvais = new Choice("Lyon", false);
        q.addChoice(bon);
        q.addChoice(mauvais);
        quiz.addQuestion(q);

        Student student = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        Score score = realService.takeQuizByStudent(student, quiz, Arrays.asList(bon));
        assertEquals(1, score.getValue());
    }

    @Test
    void testQuizServiceTakeQuizWrongAnswer() {
        InMemoryQuizRepository realQuizRepo = new InMemoryQuizRepository();
        InMemoryQuestionRepository realQuestionRepo = new InMemoryQuestionRepository();
        QuizService realService = new QuizService(realQuizRepo, realQuestionRepo);

        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        Quiz quiz = realService.createQuizByTeacher(teacher, "Q1", "Java Quiz", "Java", 30);

        QCMQuestion q = new QCMQuestion("QU1", "Test ?", "Java");
        Choice bon = new Choice("Paris", true);
        Choice mauvais = new Choice("Lyon", false);
        q.addChoice(bon);
        q.addChoice(mauvais);
        quiz.addQuestion(q);

        Student student = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        Score score = realService.takeQuizByStudent(student, quiz, Arrays.asList(mauvais));
        assertEquals(0, score.getValue());
    }

    @Test
    void testQuizServiceTakeQuizNullParamsThrows() {
        Quiz quiz = new Quiz("Q1", "Test", "Java", 10);
        Student student = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");

        assertThrows(IllegalArgumentException.class,
            () -> quizService.takeQuizByStudent(null, quiz, Collections.emptyList()));
        assertThrows(IllegalArgumentException.class,
            () -> quizService.takeQuizByStudent(student, null, Collections.emptyList()));
        assertThrows(IllegalArgumentException.class,
            () -> quizService.takeQuizByStudent(student, quiz, null));
    }

    @Test
    void testQuizServiceGetById() {
        Quiz quiz = new Quiz("Q1", "Test", "Java", 10);
        when(quizRepository.findById("Q1")).thenReturn(quiz);
        assertEquals(quiz, quizService.getQuizById("Q1"));
    }

    @Test
    void testQuizServiceGetByIdNotFoundThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> quizService.getQuizById("INCONNU"));
    }

    @Test
    void testQuizServiceGetByCourseEmptyThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> quizService.getQuizzesByCourse(""));
    }

    @Test
    void testQuizServiceGetByCourse() {
        Quiz quiz = new Quiz("Q1", "Test", "Java", 10);
        when(quizRepository.findByCourse("Java")).thenReturn(Arrays.asList(quiz));
        assertEquals(1, quizService.getQuizzesByCourse("Java").size());
    }

    @Test
    void testQuizServiceGetAll() {
        when(quizRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(quizService.getAllQuizzes().isEmpty());
    }

    @Test
    void testQuizServiceCount() {
        when(quizRepository.count()).thenReturn(4);
        assertEquals(4, quizService.getTotalQuizCount());
    }

    @Test
    void testQuizServiceDeleteOk() {
        Quiz quiz = new Quiz("Q1", "Test", "Java", 10);
        when(quizRepository.findById("Q1")).thenReturn(quiz);
        assertDoesNotThrow(() -> quizService.deleteQuiz("Q1"));
        verify(quizRepository).delete("Q1");
    }

    @Test
    void testQuizServiceDeleteNotFoundThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> quizService.deleteQuiz("INCONNU"));
    }
}
