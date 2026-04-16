package fr.uparis.projet_genie_logiciel.presentation;

import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.persistence.AppContext;
import fr.uparis.projet_genie_logiciel.presentation.commands.StudentCommands;
import fr.uparis.projet_genie_logiciel.repository.*;
import fr.uparis.projet_genie_logiciel.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class StudentCommandsTest {

    private InMemoryStudentRepository  studentRepo;
    private InMemoryQuizRepository     quizRepo;
    private InMemoryQuestionRepository questionRepo;
    private StudentService             studentService;
    private QuizService                quizService;
    private AppContext                 ctx;
    private Student                    student;

    @BeforeEach
    void setUp() {
        studentRepo    = new InMemoryStudentRepository();
        quizRepo       = new InMemoryQuizRepository();
        questionRepo   = new InMemoryQuestionRepository();
        studentService = new StudentService(studentRepo);
        quizService    = new QuizService(quizRepo, questionRepo);
        ctx            = new AppContext();
        student        = new Student("S1", "Jean", "Dupont", "jean@u.fr", "2A", "pwd123");
        studentRepo.save(student);
        ctx.setStudentCount(1);
    }

    /** Construit un CLI avec PrintStream injecté */
    private CLI cli(String input, PrintStream ps) {
        return new CLI(new Scanner(input), ps);
    }

    @Test
    void testRegisterDescription() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            assertEquals("S'inscrire (creer un compte)",
                StudentCommands.register(cli("", ps), studentService, ctx).getDescription());
        }
    }

    @Test
    void testRegisterSuccess() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            StudentCommands.register(
                cli("Alice\nDupont\nalice@u.fr\n2B\npwd456\n", ps),
                studentService, ctx).execute();
        }
        assertEquals(2, studentRepo.count());
    }

    @Test
    void testRegisterDuplicateAbort() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            StudentCommands.register(
                cli("Jean\nDupont\njean@u.fr\n2A\npwd123\nn\n", ps),
                studentService, ctx).execute();
        }
        assertEquals(1, studentRepo.count());
    }

    @Test
    void testTakeQuizDescription() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            assertEquals("Passer un quiz",
                StudentCommands.takeQuiz(cli("", ps), student, quizService).getDescription());
        }
    }

    @Test
    void testTakeQuizNoQuiz() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            StudentCommands.takeQuiz(cli("", ps), student, quizService).execute();
        }
        assertTrue(buf.toString().contains("Aucun quiz") || buf.toString().contains("ERREUR"));
    }

    @Test
    void testTakeQuizNoQuestions() {
        quizRepo.save(new Quiz("Q1", "Quiz vide", "Java", 30, "T1"));
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            StudentCommands.takeQuiz(cli("1\n", ps), student, quizService).execute();
        }
        assertTrue(buf.toString().contains("ERREUR") || buf.toString().contains("questions"));
    }

    @Test
    void testTakeQuizComplete() {
        Quiz quiz = new Quiz("Q1", "Java", "Java", 30, "T1");
        QCMQuestion q = new QCMQuestion("QU1", "Capitale ?", "Java");
        q.addChoice(new Choice("Paris", true));
        q.addChoice(new Choice("Lyon", false));
        quiz.addQuestion(q);
        quizRepo.save(quiz);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            StudentCommands.takeQuiz(cli("1\nn\n1\n", ps), student, quizService).execute();
        }
        assertEquals(1, student.viewScoreHistory().size());
    }

    @Test
    void testTakeQuizWithShuffle() {
        Quiz quiz = new Quiz("Q1", "Java", "Java", 30, "T1");
        QCMQuestion q = new QCMQuestion("QU1", "Q?", "Java");
        q.addChoice(new Choice("A", true));
        q.addChoice(new Choice("B", false));
        quiz.addQuestion(q);
        quizRepo.save(quiz);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            StudentCommands.takeQuiz(cli("1\no\n1\n", ps), student, quizService).execute();
        }
        assertFalse(student.viewScoreHistory().isEmpty());
    }

    @Test
    void testViewHistoryDescription() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            assertEquals("Voir mon historique de scores",
                StudentCommands.viewHistory(cli("", ps), student).getDescription());
        }
    }

    @Test
    void testViewHistoryEmpty() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            StudentCommands.viewHistory(cli("", ps), student).execute();
        }
        assertTrue(buf.toString().contains("pas encore"));
    }

    @Test
    void testViewHistoryWithScores() {
        Quiz quiz = new Quiz("Q1", "Java", "Java", 30, "T1");
        quizRepo.save(quiz);
        Score sc = new Score(quiz);
        sc.addPoint();
        student.addScoreToHistory(sc);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            StudentCommands.viewHistory(cli("", ps), student).execute();
        }
        assertTrue(buf.toString().contains("Java"));
    }
}
