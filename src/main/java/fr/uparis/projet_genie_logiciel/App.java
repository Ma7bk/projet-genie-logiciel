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
import java.util.Scanner;

/**
 * Point d'entrée de l'application.
 * Rôle unique : Composition Root — instancier et câbler tous les composants,
 * puis déléguer le flux à MainMenu (couche présentation).
 */
public class App {

    public static void main(String[] args) {

        // ── Repositories ─────────────────────────────────────────────────
        InMemoryTeacherRepository teacherRepo   = new InMemoryTeacherRepository();
        InMemoryStudentRepository studentRepo   = new InMemoryStudentRepository();
        InMemoryQuizRepository    quizRepo      = new InMemoryQuizRepository();
        InMemoryQuestionRepository questionRepo = new InMemoryQuestionRepository();

        // ── Services ─────────────────────────────────────────────────────
        TeacherService  teacherService  = new TeacherService(teacherRepo);
        StudentService  studentService  = new StudentService(studentRepo);
        QuizService     quizService     = new QuizService(quizRepo, questionRepo);
        QuestionService questionService = new QuestionService(questionRepo);
        AuthService     authService     = new AuthService(teacherRepo, studentRepo);

        // ── Persistance ───────────────────────────────────────────────────
        AppContext ctx = new AppContext();
        PersistenceManager pm = new PersistenceManager(
            new DataStore(), teacherRepo, studentRepo,
            quizRepo, questionRepo, ctx);
        pm.load();

        // ── Présentation ─────────────────────────────────────────────────
        Scanner scanner = new Scanner(System.in);
        CLI cli = new CLI(scanner);

        // ── Délégation au menu principal ──────────────────────────────────
        MainMenu menu = new MainMenu(cli, teacherService, studentService,
            quizService, questionService, authService, ctx);
        menu.run();

        // ── Sauvegarde finale ────────────────────────────────────────────
        pm.save();
        scanner.close();
    }
}
