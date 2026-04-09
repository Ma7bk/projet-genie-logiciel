package fr.uparis.projet_genie_logiciel;
import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.persistence.*;
import fr.uparis.projet_genie_logiciel.presentation.*;
import fr.uparis.projet_genie_logiciel.presentation.commands.*;
import fr.uparis.projet_genie_logiciel.repository.*;
import fr.uparis.projet_genie_logiciel.service.*;
import java.util.Scanner;


public class App {
    public static void main(String[] args) {

        TeacherRepository teacherRepo   = new InMemoryTeacherRepository();
        StudentRepository studentRepo   = new InMemoryStudentRepository();
        QuizRepository quizRepo         = new InMemoryQuizRepository();
        QuestionRepository questionRepo = new InMemoryQuestionRepository();


        TeacherService teacherService   = new TeacherService(teacherRepo);
        StudentService studentService   = new StudentService(studentRepo);
        QuizService quizService         = new QuizService(quizRepo, questionRepo);
        QuestionService questionService = new QuestionService(questionRepo);
        AuthService authService         = new AuthService(teacherRepo, studentRepo);


        AppContext ctx = new AppContext();
        PersistenceManager pm = new PersistenceManager(
            new DataStore(), teacherRepo, studentRepo, quizRepo, questionRepo, ctx);
        pm.load();

        Scanner scanner = new Scanner(System.in);
        CLI cli = new CLI(scanner);

        cli.banner();
        boolean running = true;
        while (running) {
            cli.title("MENU PRINCIPAL");
            cli.print("  1. Espace Enseignant");
            cli.print("  2. Espace Etudiant");
            cli.print("  0. Quitter");
            cli.sep();
            cli.print("Votre choix : ");
            int choice = cli.readInt();
            switch (choice) {
                case 1: runTeacherSpace(cli, teacherService, quizService,
                            questionService, authService, ctx); break;
                case 2: runStudentSpace(cli, studentService, quizService,
                            authService, ctx); break;
                case 0: running = false; break;
                default: cli.error("Option invalide."); break;
            }
            pm.save();
        }
        pm.save();
        cli.print("\nAu revoir ! Donnees sauvegardees.");
        scanner.close();
    }

    private static void runTeacherSpace(CLI cli, TeacherService teacherService,
                                         QuizService quizService, QuestionService questionService,
                                         AuthService authService, AppContext ctx) {
        Menu spaceMenu = new Menu("ESPACE ENSEIGNANT", cli);
        spaceMenu.addCommand(makeTeacherLogin(cli, teacherService, quizService, questionService, authService, ctx));
        spaceMenu.addCommand(TeacherCommands.register(cli, teacherService, ctx));
        spaceMenu.show();
    }

    private static Command makeTeacherLogin(CLI cli, TeacherService teacherService,
                                             QuizService quizService, QuestionService questionService,
                                             AuthService authService, AppContext ctx) {
        return new Command() {
            @Override
            public void execute() {
                cli.title("CONNEXION ENSEIGNANT");
                String email = cli.readEmail("Email : ");
                String pwd = cli.readString("Mot de passe : ");
                Teacher teacher = authService.loginTeacher(email, pwd);
                if (teacher == null) {
                    cli.error("Email ou mot de passe incorrect.");
                    return;
                }
                cli.ok("Bienvenue, " + teacher.getFullName() + " !");
                Menu menu = new Menu("MON ESPACE - " + teacher.getFullName(), cli);
                menu.addCommand(TeacherCommands.createQuiz(cli, teacher, quizService, ctx));
                menu.addCommand(TeacherCommands.addQCM(cli, teacher, quizService, questionService, ctx));
                menu.addCommand(TeacherCommands.addTrueFalse(cli, teacher, quizService, questionService, ctx));
                menu.addCommand(TeacherCommands.listMyQuizzes(cli, teacher, quizService));
                menu.show();
            }
            @Override
            public String getDescription() { return "Se connecter"; }
        };
    }

    private static void runStudentSpace(CLI cli, StudentService studentService,
                                         QuizService quizService,
                                         AuthService authService, AppContext ctx) {
        Menu spaceMenu = new Menu("ESPACE ETUDIANT", cli);
        spaceMenu.addCommand(makeStudentLogin(cli, studentService, quizService, authService));
        spaceMenu.addCommand(StudentCommands.register(cli, studentService, ctx));
        spaceMenu.show();
    }

    private static Command makeStudentLogin(CLI cli, StudentService studentService,
                                             QuizService quizService, AuthService authService) {
        return new Command() {
            @Override
            public void execute() {
                cli.title("CONNEXION ETUDIANT");
                String email = cli.readEmail("Email : ");
                String pwd = cli.readString("Mot de passe : ");
                Student student = authService.loginStudent(email, pwd);
                if (student == null) {
                    cli.error("Email ou mot de passe incorrect.");
                    return;
                }
                cli.ok("Bienvenue, " + student.getFullName() + " !");
                Menu menu = new Menu("MON ESPACE - " + student.getFullName(), cli);
                menu.addCommand(StudentCommands.takeQuiz(cli, student, quizService));
                menu.addCommand(StudentCommands.viewHistory(cli, student));
                menu.show();
            }
            @Override
            public String getDescription() { return "Se connecter"; }
        };
    }
}
