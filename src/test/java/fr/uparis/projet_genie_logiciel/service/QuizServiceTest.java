package fr.uparis.projet_genie_logiciel.service;

import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.repository.QuizRepository;
import fr.uparis.projet_genie_logiciel.repository.QuestionRepository;
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
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    private QuizService quizService;

    @BeforeEach
    void setUp() {
        quizService = new QuizService(quizRepository, questionRepository);
    }

    @Test
    void testCreateQuizByTeacher() {
        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie.dubois@u-paris.fr", "Génie Logiciel");
        when(quizRepository.findById("Q1")).thenReturn(null);

        Quiz quiz = quizService.createQuizByTeacher(teacher, "Q1", "Quiz Architecture", "Génie Logiciel", 30);

        assertNotNull(quiz);
        assertEquals("Quiz Architecture", quiz.getTitle());
        verify(quizRepository).save(any(Quiz.class));
    }

    @Test
    void testCreateQuizByTeacherAlreadyExists() {
        Teacher teacher = new Teacher("T1", "Marie", "Dubois", "marie.dubois@u-paris.fr", "Génie Logiciel");
        Quiz existingQuiz = new Quiz("Q1", "Existing", "Java", 30);
        when(quizRepository.findById("Q1")).thenReturn(existingQuiz);

        assertThrows(IllegalStateException.class, () -> {
            quizService.createQuizByTeacher(teacher, "Q1", "New Quiz", "Java", 30);
        });
    }

    @Test
    void testTakeQuizByStudent() {
        Student student = new Student("S1", "Jean", "Dupont", "jean.dupont@u-paris.fr", "2A SIE");
        Quiz quiz = new Quiz("Q1", "Java Quiz", "Java", 30);
        
        QCMQuestion q1 = new QCMQuestion("QU1", "Question 1", "Java");
        Choice c1 = new Choice("Correct", true);

        q1.addChoice(c1);

        quiz.addQuestion(q1);

        List<Choice> answers = Arrays.asList(c1);

        Score score = quizService.takeQuizByStudent(student, quiz, answers);

        assertNotNull(score);
        assertEquals(1, score.getValue());
        assertEquals(1, student.viewScoreHistory().size());
    }
}
