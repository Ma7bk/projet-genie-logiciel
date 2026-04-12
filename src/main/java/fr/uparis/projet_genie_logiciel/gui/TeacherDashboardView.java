package fr.uparis.projet_genie_logiciel.gui;

import fr.uparis.projet_genie_logiciel.entity.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDashboardView {
    private final BorderPane root;
    private final Teacher teacher;

    public TeacherDashboardView(MainApp app, Teacher teacher) {
        this.teacher = teacher;
        root = MainApp.pageLayout("Mon Espace",
            teacher.getFullName() + " [" + teacher.getSubject() + "]",
            app::showTeacherSpace);
        reset();
    }

    private void reset() { root.setCenter(new StackPane(buildMenu())); }

    private VBox buildMenu() {
        VBox box = new VBox(14); box.setPadding(new Insets(30)); box.setAlignment(Pos.TOP_CENTER);
        Button b1 = MainApp.primaryBtn("Creer un quiz", "#3498db");
        Button b2 = MainApp.primaryBtn("Ajouter une question QCM", "#3498db");
        Button b3 = MainApp.primaryBtn("Ajouter une question Vrai/Faux", "#3498db");
        Button b4 = MainApp.secondaryBtn("Voir mes quiz"); b4.setPrefWidth(300);
        b1.setOnAction(e -> showCreateQuiz());
        b2.setOnAction(e -> showAddQCM());
        b3.setOnAction(e -> showAddTrueFalse());
        b4.setOnAction(e -> showMyQuizzes());
        box.getChildren().addAll(MainApp.titleLabel("Que souhaitez-vous faire ?"),
            new Separator(), b1, b2, b3, b4);
        return box;
    }

    private void showCreateQuiz() {
        VBox form = new VBox(10); MainApp.pad(form);
        TextField titre = f("Titre"), cours = f("Cours"), duree = f("Duree (minutes)");
        Label fb = new Label();
        Button ok = MainApp.primaryBtn("Creer le quiz", "#3498db"); ok.setPrefWidth(260);
        Button cancel = MainApp.secondaryBtn("Annuler");
        ok.setOnAction(e -> {
            try {
                int d = Integer.parseInt(duree.getText().trim());
                String id = MainApp.nextQuizId();
                Quiz quiz = MainApp.quizService.createQuizByTeacher(
                    teacher, id, titre.getText().trim(), cours.getText().trim(), d);
                MainApp.save();
                ok(fb, "Quiz cree ! " + quiz.getId() + " | " + quiz.getTitle());
                titre.clear(); cours.clear(); duree.clear();
            } catch (Exception ex) { MainApp.cancelQuizId(); err(fb, ex.getMessage()); }
        });
        cancel.setOnAction(e -> reset());
        form.getChildren().addAll(MainApp.titleLabel("Creer un quiz"),
            new Label("Titre :"), titre, new Label("Cours :"), cours,
            new Label("Duree (min) :"), duree, new HBox(10, ok, cancel), fb);
        root.setCenter(new ScrollPane(form));
    }

    private void showAddQCM() {
        List<Quiz> mesQuiz = MainApp.quizService.getQuizzesByTeacher(teacher.getId());
        if (mesQuiz.isEmpty()) { alert("Creez d'abord un quiz."); return; }
        VBox form = new VBox(10); MainApp.pad(form);
        ComboBox<String> combo = quizCombo(mesQuiz);
        TextField texte = f("Texte de la question");
        ToggleGroup group = new ToggleGroup();
        List<TextField> cFields = new ArrayList<>();
        List<RadioButton> radios = new ArrayList<>();
        VBox cBox = new VBox(6);
        for (int i = 0; i < 4; i++) {
            TextField tf = f("Choix " + (i + 1));
            RadioButton rb = new RadioButton("Bonne reponse"); rb.setToggleGroup(group);
            cFields.add(tf); radios.add(rb);
            cBox.getChildren().add(new HBox(10, tf, rb));
        }
        radios.get(0).setSelected(true);
        Label fb = new Label();
        Button ok = MainApp.primaryBtn("Ajouter", "#3498db"); ok.setPrefWidth(260);
        Button cancel = MainApp.secondaryBtn("Annuler");
        ok.setOnAction(e -> {
            int qi = combo.getSelectionModel().getSelectedIndex();
            if (qi < 0) { err(fb, "Choisissez un quiz."); return; }
            List<Choice> choices = new ArrayList<>();
            for (int i = 0; i < cFields.size(); i++) {
                String t = cFields.get(i).getText().trim();
                if (!t.isEmpty()) { choices.add(new Choice(t, radios.get(i).isSelected())); }
            }
            if (choices.size() < 2) { err(fb, "Au moins 2 choix."); return; }
            try {
                String id = MainApp.nextQuestionId();
                QCMQuestion q = MainApp.questionService.createQCMQuestion(
                    id, texte.getText().trim(), mesQuiz.get(qi).getCourse(), choices);
                mesQuiz.get(qi).addQuestion(q);
                MainApp.save();
                ok(fb, "Question ajoutee !");
                texte.clear(); cFields.forEach(TextField::clear); radios.get(0).setSelected(true);
            } catch (Exception ex) { MainApp.cancelQuestionId(); err(fb, ex.getMessage()); }
        });
        cancel.setOnAction(e -> reset());
        form.getChildren().addAll(MainApp.titleLabel("Question QCM"), combo,
            new Label("Question :"), texte, new Label("Choix :"), cBox,
            new HBox(10, ok, cancel), fb);
        root.setCenter(new ScrollPane(form));
    }

    private void showAddTrueFalse() {
        List<Quiz> mesQuiz = MainApp.quizService.getQuizzesByTeacher(teacher.getId());
        if (mesQuiz.isEmpty()) { alert("Creez d'abord un quiz."); return; }
        VBox form = new VBox(10); MainApp.pad(form);
        ComboBox<String> combo = quizCombo(mesQuiz);
        TextField texte = f("Texte de la question");
        ToggleGroup tg = new ToggleGroup();
        RadioButton rbV = new RadioButton("VRAI"); rbV.setToggleGroup(tg); rbV.setSelected(true);
        RadioButton rbF = new RadioButton("FAUX"); rbF.setToggleGroup(tg);
        Label fb = new Label();
        Button ok = MainApp.primaryBtn("Ajouter", "#3498db"); ok.setPrefWidth(260);
        Button cancel = MainApp.secondaryBtn("Annuler");
        ok.setOnAction(e -> {
            int qi = combo.getSelectionModel().getSelectedIndex();
            if (qi < 0) { err(fb, "Choisissez un quiz."); return; }
            try {
                String id = MainApp.nextQuestionId();
                TrueFalseQuestion q = MainApp.questionService.createTrueFalseQuestion(
                    id, texte.getText().trim(), mesQuiz.get(qi).getCourse(), rbV.isSelected());
                mesQuiz.get(qi).addQuestion(q);
                MainApp.save();
                ok(fb, "Question Vrai/Faux ajoutee !");
                texte.clear();
            } catch (Exception ex) { MainApp.cancelQuestionId(); err(fb, ex.getMessage()); }
        });
        cancel.setOnAction(e -> reset());
        form.getChildren().addAll(MainApp.titleLabel("Question Vrai/Faux"), combo,
            new Label("Question :"), texte,
            new HBox(20, new Label("Bonne reponse :"), rbV, rbF),
            new HBox(10, ok, cancel), fb);
        root.setCenter(new ScrollPane(form));
    }

    private void showMyQuizzes() {
        VBox content = new VBox(10); MainApp.pad(content);
        List<Quiz> mesQuiz = MainApp.quizService.getQuizzesByTeacher(teacher.getId());
        List<String> items = new ArrayList<>();
        if (mesQuiz.isEmpty()) {
            items.add("Vous n'avez pas encore cree de quiz.");
        } else {
            for (Quiz q : mesQuiz) {
                items.add("[" + q.getId() + "] " + q.getTitle() + " | "
                    + q.getCourse() + " | " + q.getDuration() + " min | "
                    + q.getQuestions().size() + " question(s)");
            }
        }
        ListView<String> lv = new ListView<>(FXCollections.observableArrayList(items));
        lv.setPrefHeight(300);
        Button back = MainApp.secondaryBtn("Retour"); back.setOnAction(e -> reset());
        content.getChildren().addAll(MainApp.titleLabel("Mes quiz"), lv, back);
        root.setCenter(new ScrollPane(content));
    }

    private ComboBox<String> quizCombo(List<Quiz> quizzes) {
        ComboBox<String> c = new ComboBox<>();
        for (Quiz q : quizzes) { c.getItems().add(q.getId() + " - " + q.getTitle()); }
        c.setPromptText("Choisissez un quiz"); c.setPrefWidth(350);
        return c;
    }

    private TextField f(String p) { TextField t = new TextField(); t.setPromptText(p); return t; }
    private void ok(Label l, String m)  { l.setText(m); l.setStyle("-fx-text-fill:green;"); }
    private void err(Label l, String m) { l.setText(m); l.setStyle("-fx-text-fill:red;"); }
    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING); a.setHeaderText(null);
        a.setContentText(msg); a.showAndWait();
    }
    public BorderPane getRoot() { return root; }
}
