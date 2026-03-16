package fr.uparis.projet_genie_logiciel.service;

import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
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
        studentService = new StudentService(studentRepository);
        teacherService = new TeacherService(teacherRepository);
        questionService = new QuestionService(questionRepository);
    }

    @Test
    void testStudentServiceErrors() {
        when(studentRepository.findById("S1")).thenReturn(new Student("S1", "A", "B", "a@b.com", "2A"));
        assertThrows(IllegalStateException.class, () -> studentService.createStudent("S1", "A", "B", "a@b.com", "2A"));
        
        when(studentRepository.findById("S1")).thenReturn(null);
        when(studentRepository.findByEmail("a@b.com")).thenReturn(new Student("S2", "A", "B", "a@b.com", "2A"));
        assertThrows(IllegalStateException.class, () -> studentService.createStudent("S1", "A", "B", "a@b.com", "2A"));

        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentById("NON_EXISTENT"));
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentByEmail("NON_EXISTENT"));
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentsByClasse(""));
        assertThrows(IllegalArgumentException.class, () -> studentService.deleteStudent("NON_EXISTENT"));
    }

    @Test
    void testTeacherServiceErrors() {
        when(teacherRepository.findById("T1")).thenReturn(new Teacher("T1", "A", "B", "a@b.com", "GL"));
        assertThrows(IllegalStateException.class, () -> teacherService.createTeacher("T1", "A", "B", "a@b.com", "GL"));

        assertThrows(IllegalArgumentException.class, () -> teacherService.getTeacherById("NON_EXISTENT"));
        assertThrows(IllegalArgumentException.class, () -> teacherService.getTeachersBySubject(""));
        assertThrows(IllegalArgumentException.class, () -> teacherService.deleteTeacher("NON_EXISTENT"));
    }

    @Test
    void testQuestionServiceErrors() {
        when(questionRepository.findById("Q1")).thenReturn(new QCMQuestion("Q1", "A", "B"));
        assertThrows(IllegalStateException.class, () -> questionService.createQCMQuestion("Q1", "A", "B", Arrays.asList()));
        assertThrows(IllegalStateException.class, () -> questionService.createTrueFalseQuestion("Q1", "A", "B", true));

        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestionById("NON_EXISTENT"));
        assertThrows(IllegalArgumentException.class, () -> questionService.getQuestionsByCourse(""));
        assertThrows(IllegalArgumentException.class, () -> questionService.deleteQuestion("NON_EXISTENT"));
    }
}
