package fr.uparis.projet_genie_logiciel.presentation;

import fr.uparis.projet_genie_logiciel.entity.Student;
import fr.uparis.projet_genie_logiciel.entity.Teacher;
import fr.uparis.projet_genie_logiciel.persistence.AppContext;
import fr.uparis.projet_genie_logiciel.repository.*;
import fr.uparis.projet_genie_logiciel.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuTest {

    private InMemoryTeacherRepository teacherRepo;
    private InMemoryStudentRepository studentRepo;
    private InMemoryQuizRepository    quizRepo;
    private InMemoryQuestionRepository questionRepo;
    private AppContext ctx;

    @BeforeEach
    void setUp() {
        teacherRepo  = new InMemoryTeacherRepository();
        studentRepo  = new InMemoryStudentRepository();
        quizRepo     = new InMemoryQuizRepository();
        questionRepo = new InMemoryQuestionRepository();
        ctx          = new AppContext();
    }

    private String run(String input) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(buf));
        try {
            new MainMenu(
                new CLI(new Scanner(input)),
                new TeacherService(teacherRepo),
                new StudentService(studentRepo),
                new QuizService(quizRepo, questionRepo),
                new QuestionService(questionRepo),
                new AuthService(teacherRepo, studentRepo),
                ctx
            ).run();
        } finally {
            System.setOut(old);
        }
        return buf.toString();
    }

    @Test
    void testQuitter() {
        assertTrue(run("0\n").contains("Au revoir"));
    }

    @Test
    void testOptionInvalide() {
        assertTrue(run("99\n0\n").toLowerCase().contains("invalide"));
    }

    @Test
    void testEspaceEnseignantRetour() {
        assertTrue(run("1\n0\n0\n").contains("ESPACE ENSEIGNANT"));
    }

    @Test
    void testEspaceEtudiantRetour() {
        assertTrue(run("2\n0\n0\n").contains("ESPACE ETUDIANT"));
    }

    @Test
    void testLoginEnseignantIncorrect() {
        assertTrue(run("1\n1\nbad@u.fr\nwrongpwd\n0\n0\n")
            .toLowerCase().contains("incorrect"));
    }

    @Test
    void testLoginEtudiantIncorrect() {
        assertTrue(run("2\n1\nbad@u.fr\nwrongpwd\n0\n0\n")
            .toLowerCase().contains("incorrect"));
    }

    @Test
    void testLoginEnseignantCorrect() {
        teacherRepo.save(new Teacher("T1", "Marie", "D", "marie@u.fr", "GL", "pwd123"));
        assertTrue(run("1\n1\nmarie@u.fr\npwd123\n0\n0\n0\n").contains("Bienvenue"));
    }

    @Test
    void testLoginEtudiantCorrect() {
        studentRepo.save(new Student("S1", "Jean", "D", "jean@u.fr", "2A", "pwd456"));
        assertTrue(run("2\n1\njean@u.fr\npwd456\n0\n0\n0\n").contains("Bienvenue"));
    }
}
