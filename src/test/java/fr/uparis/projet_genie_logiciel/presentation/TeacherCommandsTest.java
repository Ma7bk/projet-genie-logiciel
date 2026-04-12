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

    private InMemoryTeacherRepository teacherRepo;
    private InMemoryQuizRepository quizRepo;
    private InMemoryQuestionRepository questionRepo;
    private TeacherService teacherService;
    private QuizService quizService;
    private QuestionService questionService;
    private AppContext ctx;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacherRepo  = new InMemoryTeacherRepository();
        quizRepo     = new InMemoryQuizRepository();
        questionRepo = new InMemoryQuestionRepository();
        teacherService  = new TeacherService(teacherRepo);
        quizService     = new QuizService(quizRepo, questionRepo);
        questionService = new QuestionService(questionRepo);
        ctx = new AppContext();
        teacher = new Teacher("T1", "Marie", "Dupont", "marie@u.fr", "GL", "pwd123");
        teacherRepo.save(teacher);
        ctx.setTeacherCount(1);
    }

    private CLI cli(String input) { return new CLI(new Scanner(input)); }

    @Test
    void testRegisterDescription() {
        assertEquals("S'inscrire (creer un compte)",
            TeacherCommands.register(cli(""), teacherService, ctx).getDescription());
    }

    @Test
    void testRegisterSuccess() {
        CLI c = cli("Jean\nMartin\njean@u.fr\nMath\npwd456\n");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        TeacherCommands.register(c, teacherService, ctx).execute();
        System.setOut(System.out);
        assertEquals(2, teacherRepo.count());
    }

    @Test
    void testRegisterDuplicateEmailAbort() {
        CLI c = cli("Jean\nMartin\nmarie@u.fr\nMath\npwd456\nn\n");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        TeacherCommands.register(c, teacherService, ctx).execute();
        System.setOut(System.out);
        assertEquals(1, teacherRepo.count());
    }

    @Test
    void testCreateQuizDescription() {
        assertEquals("Creer un quiz",
            TeacherCommands.createQuiz(cli(""), teacher, quizService, ctx).getDescription());
    }

    @Test
    void testCreateQuizSuccess() {
        CLI c = cli("Quiz Java\nJava\n30\n");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        TeacherCommands.createQuiz(c, teacher, quizService, ctx).execute();
        System.setOut(System.out);
        assertEquals(1, quizRepo.count());
    }

    @Test
    void testAddQCMDescription() {
        assertEquals("Ajouter une question QCM",
            TeacherCommands.addQCM(cli(""), teacher, quizService, questionService, ctx)
                .getDescription());
    }

    @Test
    void testAddQCMNoQuiz() {
        CLI c = cli("");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        TeacherCommands.addQCM(c, teacher, quizService, questionService, ctx).execute();
        System.setOut(System.out);
        assertTrue(out.toString().contains("ERREUR") || out.toString().contains("aucun"));
    }

    @Test
    void testAddQCMWithQuiz() {
        Quiz quiz = new Quiz("Q1", "Java", "Java", 30, "T1");
        quizRepo.save(quiz); ctx.setQuizCount(1);
        CLI c = cli("1\nCapitale ?\n3\nParis\nLyon\nMarseille\n1\n");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        TeacherCommands.addQCM(c, teacher, quizService, questionService, ctx).execute();
        System.setOut(System.out);
        assertEquals(1, quiz.getQuestions().size());
    }

    @Test
    void testAddTrueFalseDescription() {
        assertEquals("Ajouter une question Vrai/Faux",
            TeacherCommands.addTrueFalse(cli(""), teacher, quizService, questionService, ctx)
                .getDescription());
    }

    @Test
    void testAddTrueFalseNoQuiz() {
        CLI c = cli("");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        TeacherCommands.addTrueFalse(c, teacher, quizService, questionService, ctx).execute();
        System.setOut(System.out);
        assertTrue(out.toString().contains("ERREUR") || out.toString().contains("aucun"));
    }

    @Test
    void testAddTrueFalseWithQuiz() {
        Quiz quiz = new Quiz("Q1", "Sci", "Sciences", 20, "T1");
        quizRepo.save(quiz); ctx.setQuizCount(1);
        CLI c = cli("1\nLa Terre est ronde ?\no\n");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        TeacherCommands.addTrueFalse(c, teacher, quizService, questionService, ctx).execute();
        System.setOut(System.out);
        assertEquals(1, quiz.getQuestions().size());
    }

    @Test
    void testListMyQuizzesDescription() {
        assertEquals("Voir mes quiz",
            TeacherCommands.listMyQuizzes(cli(""), teacher, quizService).getDescription());
    }

    @Test
    void testListMyQuizzesEmpty() {
        CLI c = cli("");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        TeacherCommands.listMyQuizzes(c, teacher, quizService).execute();
        System.setOut(System.out);
        assertTrue(out.toString().contains("pas encore"));
    }

    @Test
    void testListMyQuizzesWithData() {
        quizRepo.save(new Quiz("Q1", "Java Quiz", "Java", 30, "T1"));
        CLI c = cli("");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        TeacherCommands.listMyQuizzes(c, teacher, quizService).execute();
        System.setOut(System.out);
        assertTrue(out.toString().contains("Java Quiz"));
    }
}
