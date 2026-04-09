package fr.uparis.projet_genie_logiciel.presentation.commands;
import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.persistence.AppContext;
import fr.uparis.projet_genie_logiciel.presentation.CLI;
import fr.uparis.projet_genie_logiciel.presentation.Command;
import fr.uparis.projet_genie_logiciel.service.*;
import java.util.ArrayList;
import java.util.List;

public final class TeacherCommands {
    private TeacherCommands() { }

    public static Command register(CLI cli, TeacherService svc, AppContext ctx) {
        return new Command() {
            @Override
            public void execute() {
                cli.title("S'INSCRIRE COMME ENSEIGNANT");
                boolean done = false;
                while (!done) {
                    String prenom = cli.readString("Prenom : ");
                    String nom = cli.readString("Nom : ");
                    String email = cli.readEmail("Email : ");
                    String matiere = cli.readString("Matiere : ");
                    String pwd = cli.readString("Mot de passe : ");
                    try {
                        String id = ctx.nextTeacherId();
                        svc.createTeacher(id, prenom, nom, email, matiere, pwd);
                        cli.ok("Compte cree ! (ID : " + id + ") Vous pouvez maintenant vous connecter.");
                        done = true;
                    } catch (Exception e) {
                        ctx.cancelTeacherId();
                        cli.error(e.getMessage());
                        done = !cli.readBoolean("Reessayer ? (o/n) : ");
                    }
                }
            }
            @Override
            public String getDescription() { return "S'inscrire (creer un compte)"; }
        };
    }

    public static Command createQuiz(CLI cli, Teacher teacher, QuizService svc, AppContext ctx) {
        return new Command() {
            @Override
            public void execute() {
                cli.title("CREER UN QUIZ");
                String titre = cli.readString("Titre du quiz : ");
                String cours = cli.readString("Cours associe : ");
                int duree = cli.readIntBetween("Duree (minutes) : ", 1, 300);
                try {
                    String id = ctx.nextQuizId();
                    Quiz quiz = svc.createQuizByTeacher(teacher, id, titre, cours, duree);
                    cli.ok("Quiz cree ! ID=" + quiz.getId() + " | " + quiz.getTitle());
                } catch (Exception e) {
                    ctx.cancelQuizId();
                    cli.error(e.getMessage());
                }
            }
            @Override
            public String getDescription() { return "Creer un quiz"; }
        };
    }

    public static Command addQCM(CLI cli, Teacher teacher, QuizService quizSvc,
                                  QuestionService qSvc, AppContext ctx) {
        return new Command() {
            @Override
            public void execute() {
                cli.title("AJOUTER UNE QUESTION QCM");
                List<Quiz> mesQuiz = quizSvc.getQuizzesByTeacher(teacher.getId());
                if (mesQuiz.isEmpty()) {
                    cli.error("Vous n'avez aucun quiz. Creez d'abord un quiz.");
                    return;
                }
                cli.print("Vos quiz :");
                for (int i = 0; i < mesQuiz.size(); i++) {
                    cli.print("  " + (i + 1) + ". " + mesQuiz.get(i).getTitle()
                        + " (" + mesQuiz.get(i).getQuestions().size() + " question(s))");
                }
                int idx = cli.readIntBetween("Choisissez : ", 1, mesQuiz.size()) - 1;
                Quiz quiz = mesQuiz.get(idx);
                String texte = cli.readString("Texte de la question : ");
                int nb = cli.readIntBetween("Nombre de choix (2 a 6) : ", 2, 6);
                List<String> textes = new ArrayList<>();
                for (int i = 0; i < nb; i++) {
                    textes.add(cli.readString("  Choix " + (i + 1) + " : "));
                }
                int correct = cli.readIntBetween("Bonne reponse (1 a " + nb + ") : ", 1, nb) - 1;
                List<Choice> choices = new ArrayList<>();
                for (int i = 0; i < textes.size(); i++) {
                    choices.add(new Choice(textes.get(i), i == correct));
                }
                try {
                    String id = ctx.nextQuestionId();
                    QCMQuestion q = qSvc.createQCMQuestion(id, texte, quiz.getCourse(), choices);
                    quiz.addQuestion(q);
                    cli.ok("Question QCM ajoutee ! Total : " + quiz.getQuestions().size());
                } catch (Exception e) {
                    ctx.cancelQuestionId();
                    cli.error(e.getMessage());
                }
            }
            @Override
            public String getDescription() { return "Ajouter une question QCM"; }
        };
    }

    public static Command addTrueFalse(CLI cli, Teacher teacher, QuizService quizSvc,
                                        QuestionService qSvc, AppContext ctx) {
        return new Command() {
            @Override
            public void execute() {
                cli.title("AJOUTER UNE QUESTION VRAI/FAUX");
                List<Quiz> mesQuiz = quizSvc.getQuizzesByTeacher(teacher.getId());
                if (mesQuiz.isEmpty()) {
                    cli.error("Vous n'avez aucun quiz. Creez d'abord un quiz.");
                    return;
                }
                cli.print("Vos quiz :");
                for (int i = 0; i < mesQuiz.size(); i++) {
                    cli.print("  " + (i + 1) + ". " + mesQuiz.get(i).getTitle());
                }
                int idx = cli.readIntBetween("Choisissez : ", 1, mesQuiz.size()) - 1;
                Quiz quiz = mesQuiz.get(idx);
                String texte = cli.readString("Texte de la question : ");
                boolean rep = cli.readBoolean("La bonne reponse est VRAI ? (o/n) : ");
                try {
                    String id = ctx.nextQuestionId();
                    TrueFalseQuestion q = qSvc.createTrueFalseQuestion(id, texte, quiz.getCourse(), rep);
                    quiz.addQuestion(q);
                    cli.ok("Question Vrai/Faux ajoutee ! Total : " + quiz.getQuestions().size());
                } catch (Exception e) {
                    ctx.cancelQuestionId();
                    cli.error(e.getMessage());
                }
            }
            @Override
            public String getDescription() { return "Ajouter une question Vrai/Faux"; }
        };
    }

    public static Command listMyQuizzes(CLI cli, Teacher teacher, QuizService svc) {
        return new Command() {
            @Override
            public void execute() {
                cli.title("MES QUIZ");
                List<Quiz> mesQuiz = svc.getQuizzesByTeacher(teacher.getId());
                if (mesQuiz.isEmpty()) {
                    cli.print("  Vous n'avez pas encore cree de quiz.");
                    return;
                }
                for (Quiz q : mesQuiz) {
                    cli.print("  [" + q.getId() + "] " + q.getTitle()
                        + " | " + q.getCourse() + " | " + q.getDuration() + " min"
                        + " | " + q.getQuestions().size() + " question(s)");
                }
            }
            @Override
            public String getDescription() { return "Voir mes quiz"; }
        };
    }
}
