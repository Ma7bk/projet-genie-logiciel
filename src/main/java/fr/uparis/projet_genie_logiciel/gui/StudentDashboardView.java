package fr.uparis.projet_genie_logiciel.gui;

import fr.uparis.projet_genie_logiciel.entity.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentDashboardView {
    private final BorderPane root;
    private final Student student;

    public StudentDashboardView(MainApp app, Student student) {
        this.student = student;
        root = MainApp.pageLayout("Mon Espace",
            student.getFullName() + " [" + student.getClasse() + "]",
            app::showStudentSpace);
        reset();
    }

    private void reset() { root.setCenter(new StackPane(buildMenu())); }

    private VBox buildMenu() {
        VBox box = new VBox(14); box.setPadding(new Insets(30)); box.setAlignment(Pos.TOP_CENTER);
        Button b1 = MainApp.primaryBtn("Passer un quiz", "#27ae60");
        Button b2 = MainApp.secondaryBtn("Voir mon historique"); b2.setPrefWidth(300);
        b1.setOnAction(e -> showTakeQuiz());
        b2.setOnAction(e -> showHistory());
        box.getChildren().addAll(MainApp.titleLabel("Que souhaitez-vous faire ?"),
            new Separator(), b1, b2);
        return box;
    }

    private void showTakeQuiz() {
        List<Quiz> quizzes = MainApp.quizService.getAllQuizzes();
        if (quizzes.isEmpty()) { alert("Aucun quiz disponible."); return; }
        VBox form = new VBox(10); MainApp.pad(form);
        ComboBox<String> combo = new ComboBox<>();
        for (Quiz q : quizzes) {
            combo.getItems().add(q.getId() + " - " + q.getTitle()
                + " (" + q.getQuestions().size() + " questions)");
        }
        combo.setPromptText("Choisissez un quiz"); combo.setPrefWidth(350);
        Label fb = new Label();
        Button start = MainApp.primaryBtn("Demarrer", "#27ae60"); start.setPrefWidth(200);
        Button cancel = MainApp.secondaryBtn("Annuler");
        start.setOnAction(e -> {
            int idx = combo.getSelectionModel().getSelectedIndex();
            if (idx < 0) { err(fb, "Choisissez un quiz."); return; }
            Quiz quiz = quizzes.get(idx);
            if (quiz.getQuestions().isEmpty()) { alert("Ce quiz n'a pas de questions."); return; }
            runSession(quiz);
        });
        cancel.setOnAction(e -> reset());
        form.getChildren().addAll(MainApp.titleLabel("Passer un quiz"), combo,
            new HBox(10, start, cancel), fb);
        root.setCenter(new ScrollPane(form));
    }

    private void runSession(Quiz quiz) {
        List<Question> questions = new ArrayList<>(quiz.getQuestions());
        Collections.shuffle(questions);
        askQuestion(quiz, questions, 0, new ArrayList<>());
    }

    private void askQuestion(Quiz quiz, List<Question> questions, int idx, List<Choice> answers) {
        if (idx >= questions.size()) { showResult(quiz, answers); return; }
        Question q = questions.get(idx);
        VBox view = new VBox(12); view.setPadding(new Insets(20));
        Label prog = new Label("Question " + (idx + 1) + " / " + questions.size());
        prog.setStyle("-fx-text-fill:#7f8c8d;");
        Label qt = new Label(q.getText());
        qt.setStyle("-fx-font-size:16px;-fx-font-weight:bold;"); qt.setWrapText(true);
        ToggleGroup group = new ToggleGroup();
        List<RadioButton> rbs = new ArrayList<>();
        VBox cBox = new VBox(8);
        for (Choice c : q.getChoices()) {
            RadioButton rb = new RadioButton(c.getText()); rb.setToggleGroup(group);
            rbs.add(rb); cBox.getChildren().add(rb);
        }
        Label errLabel = new Label();
        String btnTxt = idx < questions.size() - 1 ? "Suivant ->" : "Terminer";
        Button next = MainApp.primaryBtn(btnTxt, "#27ae60"); next.setPrefWidth(180);
        next.setOnAction(e -> {
            int sel = group.getToggles().indexOf(group.getSelectedToggle());
            if (sel < 0) { errLabel.setText("Choisissez une reponse.");
                errLabel.setStyle("-fx-text-fill:red;"); return; }
            answers.add(q.getChoices().get(sel));
            askQuestion(quiz, questions, idx + 1, answers);
        });
        view.getChildren().addAll(prog, new Label(quiz.getTitle()),
            new Separator(), qt, cBox, next, errLabel);
        root.setCenter(new ScrollPane(view));
    }

    private void showResult(Quiz quiz, List<Choice> answers) {
        Score score = MainApp.quizService.takeQuizByStudent(student, quiz, answers);
        MainApp.save();
        int total = quiz.getQuestions().size();
        int pct = total > 0 ? (score.getValue() * 100) / total : 0;
        String mention = pct >= 80 ? "Excellent !" : pct >= 50 ? "Bien !" : "Continuez !";
        String color = pct >= 80 ? "#27ae60" : pct >= 50 ? "#f39c12" : "#e74c3c";
        VBox view = new VBox(16); view.setPadding(new Insets(30)); view.setAlignment(Pos.CENTER);
        Label sc = new Label(score.getValue() + " / " + total);
        sc.setStyle("-fx-font-size:52px;-fx-font-weight:bold;-fx-text-fill:#27ae60;");
        Label ml = new Label(mention);
        ml.setStyle("-fx-font-size:18px;-fx-font-weight:bold;-fx-text-fill:" + color + ";");
        Button back = MainApp.primaryBtn("Retour au menu", "#27ae60"); back.setPrefWidth(200);
        back.setOnAction(e -> reset());
        view.getChildren().addAll(new Label("Quiz Termine !"), new Label(student.getFullName()),
            new Separator(), sc, new Label(pct + "%"), ml, new Separator(), back);
        root.setCenter(new StackPane(view));
    }

    private void showHistory() {
        VBox content = new VBox(10); MainApp.pad(content);
        List<Score> hist = student.viewScoreHistory();
        List<String> items = new ArrayList<>();
        if (hist.isEmpty()) {
            items.add("Vous n'avez pas encore passe de quiz.");
        } else {
            for (int i = 0; i < hist.size(); i++) {
                items.add((i + 1) + ". " + hist.get(i).display());
            }
        }
        ListView<String> lv = new ListView<>(FXCollections.observableArrayList(items));
        lv.setPrefHeight(300);
        Button back = MainApp.secondaryBtn("Retour"); back.setOnAction(e -> reset());
        content.getChildren().addAll(MainApp.titleLabel("Mon historique"), lv, back);
        root.setCenter(new ScrollPane(content));
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING); a.setHeaderText(null);
        a.setContentText(msg); a.showAndWait();
    }
    private void err(Label l, String m) { l.setText(m); l.setStyle("-fx-text-fill:red;"); }
    public BorderPane getRoot() { return root; }
}
