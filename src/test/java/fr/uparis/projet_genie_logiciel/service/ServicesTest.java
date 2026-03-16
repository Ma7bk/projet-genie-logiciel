package fr.uparis.projet_genie_logiciel.service;

import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
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

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository);
        teacherService = new TeacherService(teacherRepository);
        questionService = new QuestionService(questionRepository);
    }

    @Test
    void testStudentService() {
        when(studentRepository.findById("S1")).thenReturn(null);
        when(studentRepository.findByEmail("jean@u-paris.fr")).thenReturn(null);
        
        studentService.createStudent("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        verify(studentRepository).save(any(Student.class));

        Student student = new Student("S1", "Jean", "Dupont", "jean@u-paris.fr", "2A");
        when(studentRepository.findById("S1")).thenReturn(student);
        assertEquals(student, studentService.getStudentById("S1"));
    }

    @Test
    void testTeacherService() {
        when(teacherRepository.findById("T1")).thenReturn(null);
        when(teacherRepository.findByEmail("marie@u-paris.fr")).thenReturn(null);
        
        teacherService.createTeacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        verify(teacherRepository).save(any(Teacher.class));

        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie@u-paris.fr", "GL");
        when(teacherRepository.findById("T1")).thenReturn(teacher);
        assertEquals(teacher, teacherService.getTeacherById("T1"));
    }

    @Test
    void testQuestionService() {
        when(questionRepository.findById("Q1")).thenReturn(null);
        
        questionService.createQCMQuestion("Q1", "Test ?", "Java", Arrays.asList(new Choice("A", true)));
        verify(questionRepository).save(any(QCMQuestion.class));

        QCMQuestion q = new QCMQuestion("Q1", "Test ?", "Java");
        when(questionRepository.findByCourse("Java")).thenReturn(Arrays.asList(q));
        assertEquals(1, questionService.getQuestionsByCourse("Java").size());
    }
}
