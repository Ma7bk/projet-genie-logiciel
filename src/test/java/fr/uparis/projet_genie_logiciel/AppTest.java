package fr.uparis.projet_genie_logiciel;

import fr.uparis.projet_genie_logiciel.persistence.AppContext;
import fr.uparis.projet_genie_logiciel.persistence.DataStore;
import fr.uparis.projet_genie_logiciel.persistence.PersistenceManager;
import fr.uparis.projet_genie_logiciel.presentation.CLI;
import fr.uparis.projet_genie_logiciel.presentation.MainMenu;
import fr.uparis.projet_genie_logiciel.repository.InMemoryQuestionRepository;
import fr.uparis.projet_genie_logiciel.repository.InMemoryQuizRepository;
import fr.uparis.projet_genie_logiciel.repository.InMemoryStudentRepository;
import fr.uparis.projet_genie_logiciel.repository.InMemoryTeacherRepository;
import fr.uparis.projet_genie_logiciel.service.AuthService;
import fr.uparis.projet_genie_logiciel.service.QuestionService;
import fr.uparis.projet_genie_logiciel.service.QuizService;
import fr.uparis.projet_genie_logiciel.service.StudentService;
import fr.uparis.projet_genie_logiciel.service.TeacherService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    private CLI cli(String input) {
        return new CLI(new Scanner(input));
    }

    private MainMenu buildMenu(String input) {
        InMemoryTeacherRepository teacherRepository = new InMemoryTeacherRepository();
        InMemoryStudentRepository studentRepository = new InMemoryStudentRepository();
        InMemoryQuizRepository quizRepository = new InMemoryQuizRepository();
        InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
        AppContext ctx = new AppContext();

        return new MainMenu(
            cli(input),
            new TeacherService(teacherRepository),
            new StudentService(studentRepository),
            new QuizService(quizRepository, questionRepository),
            new QuestionService(questionRepository),
            new AuthService(teacherRepository, studentRepository),
            ctx
        );
    }

    private String captureOutput(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (PrintStream tempOut = new PrintStream(output)) {
            System.setOut(tempOut);
            action.run();
        } finally {
            System.setOut(originalOut);
        }

        return output.toString();
    }

    private void deleteFile(String path) throws Exception {
        Files.deleteIfExists(Path.of(path));
    }

    @Test
    void testMainMenuQuitter() {
        String output = captureOutput(() -> buildMenu("0\n").run());
        assertTrue(output.contains("Au revoir"));
    }

    @Test
    void testMainMenuOptionInvalide() {
        String output = captureOutput(() -> buildMenu("99\n0\n").run());
        assertTrue(output.toLowerCase().contains("invalide"));
    }

    @Test
    void testMainMenuEspaceEnseignantRetour() {
        String output = captureOutput(() -> buildMenu("1\n0\n0\n").run());
        assertTrue(output.contains("ESPACE ENSEIGNANT"));
    }

    @Test
    void testMainMenuEspaceEtudiantRetour() {
        String output = captureOutput(() -> buildMenu("2\n0\n0\n").run());
        assertTrue(output.contains("ESPACE ETUDIANT"));
    }

    @Test
    void testMainMenuLoginTeacherWrong() {
        String output = captureOutput(() -> buildMenu("1\n1\nbad@email.com\nwrongpwd\n0\n0\n").run());
        assertTrue(output.toLowerCase().contains("incorrect"));
    }

    @Test
    void testMainMenuLoginStudentWrong() {
        String output = captureOutput(() -> buildMenu("2\n1\nbad@email.com\nwrongpwd\n0\n0\n").run());
        assertTrue(output.toLowerCase().contains("incorrect"));
    }

    @Test
    void testPersistenceLoadSave() throws Exception {
        DataStore store = new DataStore();
        AppContext ctx = new AppContext();
        InMemoryTeacherRepository teacherRepository = new InMemoryTeacherRepository();
        InMemoryStudentRepository studentRepository = new InMemoryStudentRepository();
        InMemoryQuizRepository quizRepository = new InMemoryQuizRepository();
        InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
        PersistenceManager persistenceManager = new PersistenceManager(
            store,
            teacherRepository,
            studentRepository,
            quizRepository,
            questionRepository,
            ctx
        );

        persistenceManager.load();
        persistenceManager.save();

        deleteFile(store.getTeachersFile());
        deleteFile(store.getStudentsFile());
        deleteFile(store.getQuizzesFile());
        deleteFile(store.getQuestionsFile());
        deleteFile(store.getScoresFile());
        deleteFile(store.getCountersFile());
    }

    @Test
    void testMainMenuDescription() {
        assertEquals(
            "fr.uparis.projet_genie_logiciel.presentation.MainMenu",
            MainMenu.class.getName()
        );
    }
}
