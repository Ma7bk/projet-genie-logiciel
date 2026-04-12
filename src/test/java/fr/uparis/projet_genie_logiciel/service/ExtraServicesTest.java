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
class ExtraServicesTest {

    @Mock private StudentRepository studentRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private QuestionRepository questionRepository;

    private StudentService studentService;
    private TeacherService teacherService;
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        studentService  = new StudentService(studentRepository);
        teacherService  = new TeacherService(teacherRepository);
        questionService = new QuestionService(questionRepository);
    }

    @Test
    void testStudentServiceGetByClasseNullThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> studentService.getStudentsByClasse(null));
    }

    @Test
    void testStudentServiceGetByClasseMultipleResults() {
        Student s1 = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A", "pwd");
        Student s2 = new Student("S2", "Paul", "Martin", "paul@u-paris.fr", "2A", "pwd");
        when(studentRepository.findByClasse("2A")).thenReturn(Arrays.asList(s1, s2));
        assertEquals(2, studentService.getStudentsByClasse("2A").size());
    }

    @Test
    void testStudentServiceGetByEmailNullThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> studentService.getStudentByEmail(null));
    }

    @Test
    void testTeacherServiceGetBySubjectNullThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> teacherService.getTeachersBySubject(null));
    }

    @Test
    void testTeacherServiceGetByEmailNullThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> teacherService.getTeacherByEmail(null));
    }

    @Test
    void testTeacherServiceGetAll() {
        when(teacherRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(teacherService.getAllTeachers().isEmpty());
    }

    @Test
    void testTeacherServiceCount() {
        when(teacherRepository.count()).thenReturn(7);
        assertEquals(7, teacherService.getTotalTeacherCount());
    }

    @Test
    void testQuestionServiceGetByCourseNullThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> questionService.getQuestionsByCourse(null));
    }

    @Test
    void testQuestionServiceGetByIdNullThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> questionService.getQuestionById(null));
    }

    @Test
    void testQuestionServiceCount() {
        when(questionRepository.count()).thenReturn(10);
        assertEquals(10, questionService.getTotalQuestionCount());
    }

    @Test
    void testQuizWithTrueFalseQuestionCorrect() {
        InMemoryQuizRepository realQuizRepo = new InMemoryQuizRepository();
        InMemoryQuestionRepository realQuestionRepo = new InMemoryQuestionRepository();
        QuizService realService = new QuizService(realQuizRepo, realQuestionRepo);

        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL", "pwd");
        Quiz quiz = realService.createQuizByTeacher(teacher, "Q1", "Sciences Quiz", "Sciences", 20);

        TrueFalseQuestion tf = new TrueFalseQuestion("TF1", "La Terre est ronde ?", "Sciences", true);
        quiz.addQuestion(tf);

        Student student = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A", "pwd");
        Choice bonneReponse = tf.getChoices().get(0);
        Score score = realService.takeQuizByStudent(student, quiz, Arrays.asList(bonneReponse));
        assertEquals(1, score.getValue());
    }

    @Test
    void testQuizWithTrueFalseQuestionWrong() {
        InMemoryQuizRepository realQuizRepo = new InMemoryQuizRepository();
        InMemoryQuestionRepository realQuestionRepo = new InMemoryQuestionRepository();
        QuizService realService = new QuizService(realQuizRepo, realQuestionRepo);

        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL", "pwd");
        Quiz quiz = realService.createQuizByTeacher(teacher, "Q2", "Sciences Quiz", "Sciences", 20);

        TrueFalseQuestion tf = new TrueFalseQuestion("TF2", "La Terre est plate ?", "Sciences", false);
        quiz.addQuestion(tf);

        Student student = new Student("S2", "Alice", "Martin", "alice@u-paris.fr", "2B", "pwd");
        Choice mauvaiseReponse = tf.getChoices().get(0);
        Score score = realService.takeQuizByStudent(student, quiz, Arrays.asList(mauvaiseReponse));
        assertEquals(0, score.getValue());
    }

    @Test
    void testQuizMultipleQuestionsScore() {
        InMemoryQuizRepository realQuizRepo = new InMemoryQuizRepository();
        InMemoryQuestionRepository realQuestionRepo = new InMemoryQuestionRepository();
        QuizService realService = new QuizService(realQuizRepo, realQuestionRepo);

        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL", "pwd");
        Quiz quiz = realService.createQuizByTeacher(teacher, "Q3", "Mix Quiz", "Java", 30);

        QCMQuestion qcm = new QCMQuestion("QU1", "Capitale de la France ?", "Java");
        Choice bon = new Choice("Paris", true);
        Choice mauvais = new Choice("Lyon", false);
        qcm.addChoice(bon);
        qcm.addChoice(mauvais);
        quiz.addQuestion(qcm);

        TrueFalseQuestion tf = new TrueFalseQuestion("TF3", "Java est orienté objet ?", "Java", true);
        quiz.addQuestion(tf);

        Student student = new Student("S3", "Bob", "Dupont", "bob@u-paris.fr", "2A", "pwd");
        Choice bonTF = tf.getChoices().get(0);
        Score score = realService.takeQuizByStudent(student, quiz, Arrays.asList(bon, bonTF));
        assertEquals(2, score.getValue());
    }
}