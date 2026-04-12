package fr.uparis.projet_genie_logiciel.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WelcomeView {
    private final VBox root;

    public WelcomeView(MainApp app) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        root.setStyle("-fx-background-color:#2c3e50;");
        Label title = new Label("APPLICATION QUIZ");
        title.setStyle("-fx-font-size:36px;-fx-font-weight:bold;-fx-text-fill:white;");
        Label subtitle = new Label("Genie Logiciel - EIDD 2A SIE");
        subtitle.setStyle("-fx-font-size:14px;-fx-text-fill:#bdc3c7;");
        Label question = new Label("Qui etes-vous ?");
        question.setStyle("-fx-font-size:18px;-fx-text-fill:#ecf0f1;-fx-padding:20 0 5 0;");
        javafx.scene.control.Button teacherBtn = MainApp.primaryBtn("Espace Enseignant", "#3498db");
        teacherBtn.setOnAction(e -> app.showTeacherSpace());
        javafx.scene.control.Button studentBtn = MainApp.primaryBtn("Espace Etudiant", "#27ae60");
        studentBtn.setOnAction(e -> app.showStudentSpace());
        root.getChildren().addAll(title, subtitle, question, teacherBtn, studentBtn);
    }

    public VBox getRoot() { return root; }
}
