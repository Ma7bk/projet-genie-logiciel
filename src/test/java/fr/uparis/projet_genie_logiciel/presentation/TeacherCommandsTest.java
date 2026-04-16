package fr.uparis.projet_genie_logiciel.presentation;

import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.persistence.AppContext;
import fr.uparis.projet_genie_logiciel.presentation.commands.TeacherCommands;
import fr.uparis.projet_genie_logiciel.repository.*;
import fr.uparis.projet_genie_logiciel.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class TeacherCommandsTest {

    private InMemoryTeacherRepository  teacherRepo;
    private InMemoryQuizRepository     quizRepo;
    private InMemoryQuestionRepository questionRepo;
    private TeacherService             teacherService;
    private QuizService                quizService;
    private QuestionService            questionService;
    private AppContext                 ctx;
    private Teacher                    teacher;

    @BeforeEach
    void setUp() {
        teacherRepo     = new InMemoryTeacherRepository();
        quizRepo        = new InMemoryQuizRepository();
        questionRepo    = new InMemoryQuestionRepository();
        teacherService  = new TeacherService(teacherRepo);
        quizService     = new QuizService(quizRepo, questionRepo);
        questionService = new QuestionService(questionRepo);
        ctx             = new AppContext();
        teacher         = new Teacher("T1", "Marie", "Dupont", "marie@u.fr", "GL", "pwd123");
        teacherRepo.save(teacher);
        ctx.setTeacherCount(1);
    }

    /** Exécute une commande et retourne la sortie capturée — sans System.setOut */
    private String exec(Command cmd) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            cmd.execute();
        }
        return buf.toString();
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
                TeacherCommands.register(cli("", ps), teacherService, ctx).getDescription());
        }
    }

    @Test
    void testRegisterSuccess() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            TeacherCommands.register(
                cli("Jean\nMartin\njean@u.fr\nMath\npwd456\n", ps),
                teacherService, ctx).execute();
        }
        assertEquals(2, teacherRepo.count());
    }

    @Test
    void testRegisterDuplicateEmailAbort() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            TeacherCommands.register(
                cli("Jean\nMartin\nmarie@u.fr\nMath\npwd456\nn\n", ps),
                teacherService, ctx).execute();
        }
        assertEquals(1, teacherRepo.count());
    }

    @Test
    void testCreateQuizDescription() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            assertEquals("Creer un quiz",
                TeacherCommands.createQuiz(cli("", ps), teacher, quizService, ctx).getDescription());
        }
    }

    @Test
    void testCreateQuizSuccess() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            TeacherCommands.createQuiz(
                cli("Quiz Java\nJava\n30\n", ps),
                teacher, quizService, ctx).execute();
        }
        assertEquals(1, quizRepo.count());
    }

    @Test
    void testAddQCMDescription() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            assertEquals("Ajouter une question QCM",
                TeacherCommands.addQCM(cli("", ps), teacher, quizService, questionService, ctx)
                    .getDescription());
        }
    }

    @Test
    void testAddQCMNoQuiz() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            TeacherCommands.addQCM(cli("", ps), teacher, quizService, questionService, ctx)
                .execute();
        }
        assertTrue(buf.toString().contains("ERREUR") || buf.toString().contains("aucun"));
    }

    @Test
    void testAddQCMWithQuiz() {
        Quiz quiz = new Quiz("Q1", "Java", "Java", 30, "T1");
        quizRepo.save(quiz);
        ctx.setQuizCount(1);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            TeacherCommands.addQCM(
                cli("1\nCapitale ?\n3\nParis\nLyon\nMarseille\n1\n", ps),
                teacher, quizService, questionService, ctx).execute();
        }
        assertEquals(1, quiz.getQuestions().size());
    }

    @Test
    void testAddTrueFalseDescription() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            assertEquals("Ajouter une question Vrai/Faux",
                TeacherCommands.addTrueFalse(cli("", ps), teacher, quizService, questionService, ctx)
                    .getDescription());
        }
    }

    @Test
    void testAddTrueFalseNoQuiz() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            TeacherCommands.addTrueFalse(cli("", ps), teacher, quizService, questionService, ctx)
                .execute();
        }
        assertTrue(buf.toString().contains("ERREUR") || buf.toString().contains("aucun"));
    }

    @Test
    void testAddTrueFalseWithQuiz() {
        Quiz quiz = new Quiz("Q1", "Sci", "Sciences", 20, "T1");
        quizRepo.save(quiz);
        ctx.setQuizCount(1);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            TeacherCommands.addTrueFalse(
                cli("1\nLa Terre est ronde ?\no\n", ps),
                teacher, quizService, questionService, ctx).execute();
        }
        assertEquals(1, quiz.getQuestions().size());
    }

    @Test
    void testListMyQuizzesDescription() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            assertEquals("Voir mes quiz",
                TeacherCommands.listMyQuizzes(cli("", ps), teacher, quizService).getDescription());
        }
    }

    @Test
    void testListMyQuizzesEmpty() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            TeacherCommands.listMyQuizzes(cli("", ps), teacher, quizService).execute();
        }
        assertTrue(buf.toString().contains("pas encore"));
    }

    @Test
    void testListMyQuizzesWithData() {
        quizRepo.save(new Quiz("Q1", "Java Quiz", "Java", 30, "T1"));
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            TeacherCommands.listMyQuizzes(cli("", ps), teacher, quizService).execute();
        }
        assertTrue(buf.toString().contains("Java Quiz"));
    }
}
