package fr.uparis.projet_genie_logiciel.presentation;

import fr.uparis.projet_genie_logiciel.entity.Student;
import fr.uparis.projet_genie_logiciel.entity.Teacher;
import fr.uparis.projet_genie_logiciel.persistence.AppContext;
import fr.uparis.projet_genie_logiciel.presentation.commands.StudentCommands;
import fr.uparis.projet_genie_logiciel.presentation.commands.TeacherCommands;
import fr.uparis.projet_genie_logiciel.service.AuthService;
import fr.uparis.projet_genie_logiciel.service.QuestionService;
import fr.uparis.projet_genie_logiciel.service.QuizService;
import fr.uparis.projet_genie_logiciel.service.StudentService;
import fr.uparis.projet_genie_logiciel.service.TeacherService;


public class MainMenu {

    private final CLI cli;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final QuizService quizService;
    private final QuestionService questionService;
    private final AuthService authService;
    private final AppContext ctx;

    public MainMenu(CLI cli,
                    TeacherService teacherService,
                    StudentService studentService,
                    QuizService quizService,
                    QuestionService questionService,
                    AuthService authService,
                    AppContext ctx) {
        this.cli             = cli;
        this.teacherService  = teacherService;
        this.studentService  = studentService;
        this.quizService     = quizService;
        this.questionService = questionService;
        this.authService     = authService;
        this.ctx             = ctx;
    }

    /** Lance la boucle principale du menu. */
    public void run() {
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
                case 1: runTeacherSpace(); break;
                case 2: runStudentSpace(); break;
                case 0: running = false; break;
                default: cli.error("Option invalide."); break;
            }
        }
        cli.print("\nAu revoir ! Donnees sauvegardees.");
    }



    private void runTeacherSpace() {
        Menu spaceMenu = new Menu("ESPACE ENSEIGNANT", cli);
        spaceMenu.addCommand(makeTeacherLoginCommand());
        spaceMenu.addCommand(TeacherCommands.register(cli, teacherService, ctx));
        spaceMenu.show();
    }

    private Command makeTeacherLoginCommand() {
        return new Command() {
            @Override
            public void execute() {
                cli.title("CONNEXION ENSEIGNANT");
                String email = cli.readEmail("Email : ");
                String pwd   = cli.readString("Mot de passe : ");
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



    private void runStudentSpace() {
        Menu spaceMenu = new Menu("ESPACE ETUDIANT", cli);
        spaceMenu.addCommand(makeStudentLoginCommand());
        spaceMenu.addCommand(StudentCommands.register(cli, studentService, ctx));
        spaceMenu.show();
    }

    private Command makeStudentLoginCommand() {
        return new Command() {
            @Override
            public void execute() {
                cli.title("CONNEXION ETUDIANT");
                String email = cli.readEmail("Email : ");
                String pwd   = cli.readString("Mot de passe : ");
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
