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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    private CLI cliFromString(String input) {
        return new CLI(new Scanner(input));
    }

    private MainMenu buildMenu(String input) {
        InMemoryTeacherRepository  tr  = new InMemoryTeacherRepository();
        InMemoryStudentRepository  sr  = new InMemoryStudentRepository();
        InMemoryQuizRepository     qr  = new InMemoryQuizRepository();
        InMemoryQuestionRepository qu  = new InMemoryQuestionRepository();
        AppContext ctx = new AppContext();
        return new MainMenu(
            cliFromString(input),
            new TeacherService(tr),
            new StudentService(sr),
            new QuizService(qr, qu),
            new QuestionService(qu),
            new AuthService(tr, sr),
            ctx
        );
    }

    /**
     * Capture la sortie console en passant un PrintStream dédié au CLI.
     * On ne touche jamais à System.out — on injecte directement le stream
     * dans un CLI séparé qui écrit dans notre buffer.
     */
    private String runAndCapture(String input) {
        java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            InMemoryTeacherRepository  tr  = new InMemoryTeacherRepository();
            InMemoryStudentRepository  sr  = new InMemoryStudentRepository();
            InMemoryQuizRepository     qr  = new InMemoryQuizRepository();
            InMemoryQuestionRepository qu  = new InMemoryQuestionRepository();
            AppContext ctx = new AppContext();
            CLI cli = new CLI(new Scanner(input), ps);
            MainMenu menu = new MainMenu(
                cli,
                new TeacherService(tr),
                new StudentService(sr),
                new QuizService(qr, qu),
                new QuestionService(qu),
                new AuthService(tr, sr),
                ctx
            );
            menu.run();
        }
        return buf.toString();
    }

    private void deleteIfExists(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }

    @Test
    void testMainMenuQuitter() {
        assertTrue(runAndCapture("0\n").contains("Au revoir"));
    }

    @Test
    void testMainMenuOptionInvalide() {
        assertTrue(runAndCapture("99\n0\n").toLowerCase().contains("invalide"));
    }

    @Test
    void testMainMenuEspaceEnseignantRetour() {
        assertTrue(runAndCapture("1\n0\n0\n").contains("ESPACE ENSEIGNANT"));
    }

    @Test
    void testMainMenuEspaceEtudiantRetour() {
        assertTrue(runAndCapture("2\n0\n0\n").contains("ESPACE ETUDIANT"));
    }

    @Test
    void testMainMenuLoginTeacherWrong() {
        assertTrue(runAndCapture("1\n1\nbad@email.com\nwrongpwd\n0\n0\n")
            .toLowerCase().contains("incorrect"));
    }

    @Test
    void testMainMenuLoginStudentWrong() {
        assertTrue(runAndCapture("2\n1\nbad@email.com\nwrongpwd\n0\n0\n")
            .toLowerCase().contains("incorrect"));
    }

    @Test
    void testPersistenceLoadSave() throws IOException {
        DataStore store = new DataStore();
        AppContext ctx = new AppContext();
        InMemoryTeacherRepository  tr = new InMemoryTeacherRepository();
        InMemoryStudentRepository  sr = new InMemoryStudentRepository();
        InMemoryQuizRepository     qr = new InMemoryQuizRepository();
        InMemoryQuestionRepository qu = new InMemoryQuestionRepository();
        PersistenceManager pm = new PersistenceManager(store, tr, sr, qr, qu, ctx);
        pm.load();
        pm.save();
        deleteIfExists(store.getTeachersFile());
        deleteIfExists(store.getStudentsFile());
        deleteIfExists(store.getQuizzesFile());
        deleteIfExists(store.getQuestionsFile());
        deleteIfExists(store.getScoresFile());
        deleteIfExists(store.getCountersFile());
    }

    @Test
    void testMainMenuDescription() {
        assertEquals(
            "fr.uparis.projet_genie_logiciel.presentation.MainMenu",
            MainMenu.class.getName()
        );
    }
}
