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
class ExtraServicesTest2 {

    @Mock private TeacherRepository teacherRepo;
    @Mock private StudentRepository studentRepo;
    @Mock private QuestionRepository questionRepo;

    private TeacherService teacherService;
    private StudentService studentService;
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        teacherService  = new TeacherService(teacherRepo);
        studentService  = new StudentService(studentRepo);
        questionService = new QuestionService(questionRepo);
    }

    @Test
    void testTeacherGetByEmailFound() {
        Teacher t = new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd");
        when(teacherRepo.findByEmail("a@b.com")).thenReturn(t);
        assertEquals(t, teacherService.getTeacherByEmail("a@b.com"));
    }

    @Test
    void testTeacherGetByEmailNotFound() {
        when(teacherRepo.findByEmail("x@b.com")).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
            () -> teacherService.getTeacherByEmail("x@b.com"));
    }

    @Test
    void testTeacherGetAll() {
        Teacher t = new Teacher("T1", "A", "B", "a@b.com", "GL", "pwd");
        when(teacherRepo.findAll()).thenReturn(Arrays.asList(t));
        assertEquals(1, teacherService.getAllTeachers().size());
    }

    @Test
    void testStudentGetByEmailFound() {
        Student s = new Student("S1", "A", "B", "a@b.com", "2A", "pwd");
        when(studentRepo.findByEmail("a@b.com")).thenReturn(s);
        assertEquals(s, studentService.getStudentByEmail("a@b.com"));
    }

    @Test
    void testStudentGetByEmailNotFound() {
        when(studentRepo.findByEmail("x@b.com")).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
            () -> studentService.getStudentByEmail("x@b.com"));
    }

    @Test
    void testStudentGetAll() {
        Student s = new Student("S1", "A", "B", "a@b.com", "2A", "pwd");
        when(studentRepo.findAll()).thenReturn(Arrays.asList(s));
        assertEquals(1, studentService.getAllStudents().size());
    }

    @Test
    void testStudentDelete() {
        Student s = new Student("S1", "A", "B", "a@b.com", "2A", "pwd");
        when(studentRepo.findById("S1")).thenReturn(s);
        studentService.deleteStudent("S1");
        verify(studentRepo).delete("S1");
    }

    @Test
    void testStudentDeleteNotFound() {
        when(studentRepo.findById("X")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> studentService.deleteStudent("X"));
    }

    @Test
    void testStudentCount() {
        when(studentRepo.count()).thenReturn(3);
        assertEquals(3, studentService.getTotalStudentCount());
    }

    @Test
    void testQuestionDeleteFound() {
        QCMQuestion q = new QCMQuestion("QU1", "T?", "Java");
        when(questionRepo.findById("QU1")).thenReturn(q);
        questionService.deleteQuestion("QU1");
        verify(questionRepo).delete("QU1");
    }

    @Test
    void testQuestionDeleteNotFound() {
        when(questionRepo.findById("X")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> questionService.deleteQuestion("X"));
    }

    @Test
    void testQuestionGetAll() {
        QCMQuestion q = new QCMQuestion("QU1", "T?", "Java");
        when(questionRepo.findAll()).thenReturn(Arrays.asList(q));
        assertEquals(1, questionService.getAllQuestions().size());
    }

    @Test
    void testQuestionGetByCourse() {
        QCMQuestion q = new QCMQuestion("QU1", "T?", "Java");
        when(questionRepo.findByCourse("Java")).thenReturn(Arrays.asList(q));
        assertEquals(1, questionService.getQuestionsByCourse("Java").size());
    }
}
