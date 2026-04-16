package fr.uparis.projet_genie_logiciel;

import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.persistence.*;
import fr.uparis.projet_genie_logiciel.presentation.CLI;
import fr.uparis.projet_genie_logiciel.presentation.MainMenu;
import fr.uparis.projet_genie_logiciel.repository.*;
import fr.uparis.projet_genie_logiciel.service.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private CLI cli(String input) {
        return new CLI(new Scanner(input));
    }

    private MainMenu buildMenu(String input) {
        InMemoryTeacherRepository tr = new InMemoryTeacherRepository();
        InMemoryStudentRepository sr = new InMemoryStudentRepository();
        InMemoryQuizRepository    qr = new InMemoryQuizRepository();
        InMemoryQuestionRepository qu = new InMemoryQuestionRepository();
        AppContext ctx = new AppContext();
        return new MainMenu(
            cli(input),
            new TeacherService(tr),
            new StudentService(sr),
            new QuizService(qr, qu),
            new QuestionService(qu),
            new AuthService(tr, sr),
            ctx
        );
    }

    @Test
    void testMainMenuQuitter() {
        // Option 0 = quitter immédiatement
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        buildMenu("0\n").run();
        System.setOut(System.out);
        assertTrue(out.toString().contains("Au revoir"));
    }

    @Test
    void testMainMenuOptionInvalide() {
        // Option invalide puis quitter
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        buildMenu("99\n0\n").run();
        System.setOut(System.out);
        assertTrue(out.toString().contains("invalide"));
    }

    @Test
    void testMainMenuEspaceEnseignantRetour() {
        // Espace enseignant → retour → quitter
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        buildMenu("1\n0\n0\n").run();
        System.setOut(System.out);
        assertTrue(out.toString().contains("ESPACE ENSEIGNANT"));
    }

    @Test
    void testMainMenuEspaceEtudiantRetour() {
        // Espace étudiant → retour → quitter
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        buildMenu("2\n0\n0\n").run();
        System.setOut(System.out);
        assertTrue(out.toString().contains("ESPACE ETUDIANT"));
    }

    @Test
    void testMainMenuLoginTeacherWrong() {
        // Espace enseignant → se connecter → mauvais mdp → retour
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        buildMenu("1\n1\nbad@email.com\nwrongpwd\n0\n0\n").run();
        System.setOut(System.out);
        assertTrue(out.toString().contains("incorrect"));
    }

    @Test
    void testMainMenuLoginStudentWrong() {
        // Espace étudiant → se connecter → mauvais mdp → retour
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        buildMenu("2\n1\nbad@email.com\nwrongpwd\n0\n0\n").run();
        System.setOut(System.out);
        assertTrue(out.toString().contains("incorrect"));
    }

    @Test
    void testPersistenceLoadSave() {
        DataStore store = new DataStore();
        AppContext ctx = new AppContext();
        InMemoryTeacherRepository tr = new InMemoryTeacherRepository();
        InMemoryStudentRepository sr = new InMemoryStudentRepository();
        InMemoryQuizRepository qr = new InMemoryQuizRepository();
        InMemoryQuestionRepository qu = new InMemoryQuestionRepository();
        PersistenceManager pm = new PersistenceManager(store, tr, sr, qr, qu, ctx);
        pm.load();
        pm.save();
        new File(store.getTeachersFile()).delete();
        new File(store.getStudentsFile()).delete();
        new File(store.getQuizzesFile()).delete();
        new File(store.getQuestionsFile()).delete();
        new File(store.getScoresFile()).delete();
        new File(store.getCountersFile()).delete();
        assertTrue(true);
    }

    @Test
    void testMainMenuDescription() {
        // Vérifie que MainMenu est dans le bon package
        assertEquals("fr.uparis.projet_genie_logiciel.presentation.MainMenu",
            MainMenu.class.getName());
    }
}
