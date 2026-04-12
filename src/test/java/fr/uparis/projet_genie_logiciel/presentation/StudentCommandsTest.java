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

    private InMemoryStudentRepository studentRepo;
    private InMemoryQuizRepository quizRepo;
    private InMemoryQuestionRepository questionRepo;
    private StudentService studentService;
    private QuizService quizService;
    private AppContext ctx;
    private Student student;

    @BeforeEach
    void setUp() {
        studentRepo  = new InMemoryStudentRepository();
        quizRepo     = new InMemoryQuizRepository();
        questionRepo = new InMemoryQuestionRepository();
        studentService = new StudentService(studentRepo);
        quizService    = new QuizService(quizRepo, questionRepo);
        ctx = new AppContext();
        student = new Student("S1", "Jean", "Dupont", "jean@u.fr", "2A", "pwd123");
        studentRepo.save(student);
        ctx.setStudentCount(1);
    }

    private CLI cli(String input) { return new CLI(new Scanner(input)); }

    @Test
    void testRegisterDescription() {
        assertEquals("S'inscrire (creer un compte)",
            StudentCommands.register(cli(""), studentService, ctx).getDescription());
    }

    @Test
    void testRegisterSuccess() {
        CLI c = cli("Alice\nDupont\nalice@u.fr\n2B\npwd456\n");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        StudentCommands.register(c, studentService, ctx).execute();
        System.setOut(System.out);
        assertEquals(2, studentRepo.count());
    }

    @Test
    void testRegisterDuplicateAbort() {
        CLI c = cli("Jean\nDupont\njean@u.fr\n2A\npwd123\nn\n");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        StudentCommands.register(c, studentService, ctx).execute();
        System.setOut(System.out);
        assertEquals(1, studentRepo.count());
    }

    @Test
    void testTakeQuizDescription() {
        assertEquals("Passer un quiz",
            StudentCommands.takeQuiz(cli(""), student, quizService).getDescription());
    }

    @Test
    void testTakeQuizNoQuiz() {
        CLI c = cli("");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        StudentCommands.takeQuiz(c, student, quizService).execute();
        System.setOut(System.out);
        assertTrue(out.toString().contains("Aucun quiz") || out.toString().contains("ERREUR"));
    }

    @Test
    void testTakeQuizNoQuestions() {
        quizRepo.save(new Quiz("Q1", "Quiz vide", "Java", 30, "T1"));
        CLI c = cli("1\n");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        StudentCommands.takeQuiz(c, student, quizService).execute();
        System.setOut(System.out);
        assertTrue(out.toString().contains("ERREUR") || out.toString().contains("questions"));
    }

    @Test
    void testTakeQuizComplete() {
        Quiz quiz = new Quiz("Q1", "Java", "Java", 30, "T1");
        QCMQuestion q = new QCMQuestion("QU1", "Capitale ?", "Java");
        q.addChoice(new Choice("Paris", true));
        q.addChoice(new Choice("Lyon", false));
        quiz.addQuestion(q);
        quizRepo.save(quiz);
        CLI c = cli("1\nn\n1\n");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        StudentCommands.takeQuiz(c, student, quizService).execute();
        System.setOut(System.out);
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
        CLI c = cli("1\no\n1\n");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        StudentCommands.takeQuiz(c, student, quizService).execute();
        System.setOut(System.out);
        assertFalse(student.viewScoreHistory().isEmpty());
    }

    @Test
    void testViewHistoryDescription() {
        assertEquals("Voir mon historique de scores",
            StudentCommands.viewHistory(cli(""), student).getDescription());
    }

    @Test
    void testViewHistoryEmpty() {
        CLI c = cli("");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        StudentCommands.viewHistory(c, student).execute();
        System.setOut(System.out);
        assertTrue(out.toString().contains("pas encore"));
    }

    @Test
    void testViewHistoryWithScores() {
        Quiz quiz = new Quiz("Q1", "Java", "Java", 30, "T1");
        quizRepo.save(quiz);
        Score sc = new Score(quiz); sc.addPoint();
        student.addScoreToHistory(sc);
        CLI c = cli("");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        StudentCommands.viewHistory(c, student).execute();
        System.setOut(System.out);
        assertTrue(out.toString().contains("Java"));
    }
}
