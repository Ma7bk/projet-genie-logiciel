package fr.uparis.projet_genie_logiciel.presentation.commands;
import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.persistence.AppContext;
import fr.uparis.projet_genie_logiciel.presentation.CLI;
import fr.uparis.projet_genie_logiciel.presentation.Command;
import fr.uparis.projet_genie_logiciel.service.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
 
public final class StudentCommands {
    private StudentCommands() { }
 
    public static Command register(CLI cli, StudentService svc, AppContext ctx) {
        return new Command() {
            @Override
            public void execute() {
                cli.title("S'INSCRIRE COMME ETUDIANT");
                boolean done = false;
                while (!done) {
                    String prenom = cli.readString("Prenom : ");
                    String nom = cli.readString("Nom : ");
                    String email = cli.readEmail("Email : ");
                    String classe = cli.readString("Classe : ");
                    String pwd = cli.readString("Mot de passe : ");
                    try {
                        String id = ctx.nextStudentId();
                        svc.createStudent(id, prenom, nom, email, classe, pwd);
                        cli.ok("Compte cree ! (ID : " + id + ") Vous pouvez maintenant vous connecter.");
                        done = true;
                    } catch (Exception e) {
                        ctx.cancelStudentId();
                        cli.error(e.getMessage());
                        done = !cli.readBoolean("Reessayer ? (o/n) : ");
                    }
                }
            }
            @Override
            public String getDescription() { return "S'inscrire (creer un compte)"; }
        };
    }
 
    public static Command takeQuiz(CLI cli, Student student, QuizService svc) {
        return new Command() {
            @Override
            public void execute() {
                cli.title("PASSER UN QUIZ");
                List<Quiz> quizzes = svc.getAllQuizzes();
                if (quizzes.isEmpty()) { cli.error("Aucun quiz disponible."); return; }
                Quiz quiz = selectQuiz(cli, quizzes);
                List<Question> questions = quiz.getQuestions();
                if (questions.isEmpty()) { cli.error("Ce quiz n'a pas de questions."); return; }
                if (cli.readBoolean("Melanger les questions ? (o/n) : ")) {
                    Collections.shuffle(questions);
                    cli.ok("Questions melangees !");
                }
                printQuizHeader(cli, quiz, student);
                List<Choice> reponses = collectAnswers(cli, questions);
                printResult(cli, svc.takeQuizByStudent(student, quiz, reponses), quiz);
            }
            @Override
            public String getDescription() { return "Passer un quiz"; }
        };
    }
 
    private static Quiz selectQuiz(CLI cli, List<Quiz> quizzes) {
        cli.print("Quiz disponibles :");
        for (int i = 0; i < quizzes.size(); i++) {
            cli.print("  " + (i + 1) + ". " + quizzes.get(i).getTitle()
                + " [" + quizzes.get(i).getCourse() + "]"
                + " | " + quizzes.get(i).getQuestions().size() + " question(s)");
        }
        int idx = cli.readIntBetween("Choisissez un quiz : ", 1, quizzes.size()) - 1;
        return quizzes.get(idx);
    }
 
    private static void printQuizHeader(CLI cli, Quiz quiz, Student student) {
        cli.sep();
        cli.print("  QUIZ : " + quiz.getTitle().toUpperCase());
        cli.print("  Etudiant : " + student.getFullName());
        cli.print("  Questions : " + quiz.getQuestions().size());
        cli.sep();
    }
 
    private static List<Choice> collectAnswers(CLI cli, List<Question> questions) {
        List<Choice> reponses = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            cli.print("\nQuestion " + (i + 1) + "/" + questions.size() + " : " + q.getText());
            List<Choice> choices = q.getChoices();
            for (int j = 0; j < choices.size(); j++) {
                cli.print("  " + (j + 1) + ". " + choices.get(j).getText());
            }
            int rep = cli.readIntBetween("Votre reponse : ", 1, choices.size()) - 1;
            reponses.add(choices.get(rep));
        }
        return reponses;
    }
 
    private static void printResult(CLI cli, Score score, Quiz quiz) {
        int total = quiz.getQuestions().size();
        int pct = total > 0 ? (score.getValue() * 100) / total : 0;
        cli.sep();
        cli.ok("QUIZ TERMINE !");
        cli.print("  " + score.display());
        cli.print("  Pourcentage : " + pct + "%");
        if (pct >= 80) { cli.print("  Excellent travail !"); }
        else if (pct >= 50) { cli.print("  Bien, continuez !"); }
        else { cli.print("  Continuez a reviser !"); }
        cli.sep();
    }
 
    public static Command viewHistory(CLI cli, Student student) {
        return new Command() {
            @Override
            public void execute() {
                cli.title("MON HISTORIQUE DE SCORES");
                cli.print("  Etudiant : " + student.getFullName());
                List<Score> hist = student.viewScoreHistory();
                if (hist.isEmpty()) {
                    cli.print("  Vous n'avez pas encore passe de quiz.");
                } else {
                    for (int i = 0; i < hist.size(); i++) {
                        cli.print("  " + (i + 1) + ". " + hist.get(i).display());
                    }
                }
            }
            @Override
            public String getDescription() { return "Voir mon historique de scores"; }
        };
    }
}
 
