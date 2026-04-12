package fr.uparis.projet_genie_logiciel.gui;

import fr.uparis.projet_genie_logiciel.entity.Teacher;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TeacherSpaceView {
    private final BorderPane root;

    public TeacherSpaceView(MainApp app) {
        root = MainApp.pageLayout("Espace Enseignant", "", app::showWelcome);
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(
            new Tab("Se connecter", buildLogin(app)),
            new Tab("S'inscrire", buildRegister()));
        StackPane center = new StackPane(tabs);
        center.setPadding(new Insets(20));
        root.setCenter(center);
    }

    private ScrollPane buildLogin(MainApp app) {
        VBox form = new VBox(10); MainApp.pad(form);
        TextField email = new TextField(); email.setPromptText("Email");
        PasswordField pwd = new PasswordField(); pwd.setPromptText("Mot de passe");
        Label fb = new Label();
        Button btn = MainApp.primaryBtn("Se connecter", "#3498db"); btn.setPrefWidth(260);
        btn.setOnAction(e -> {
            Teacher t = MainApp.authService.loginTeacher(email.getText().trim(), pwd.getText());
            if (t != null) { MainApp.save(); app.showTeacherDashboard(t); }
            else { fb.setText("Email ou mot de passe incorrect."); fb.setStyle("-fx-text-fill:red;"); }
        });
        form.getChildren().addAll(MainApp.titleLabel("Connexion Enseignant"),
            new Label("Email :"), email, new Label("Mot de passe :"), pwd, btn, fb);
        return new ScrollPane(form);
    }

    private ScrollPane buildRegister() {
        VBox form = new VBox(8); MainApp.pad(form);
        TextField prenom = f("Prenom"), nom = f("Nom"), email = f("Email"), matiere = f("Matiere");
        PasswordField pwd = pf("Mot de passe"), pwdC = pf("Confirmer");
        Label fb = new Label();
        Button btn = MainApp.primaryBtn("Creer mon compte", "#3498db"); btn.setPrefWidth(260);
        btn.setOnAction(e -> {
            if (!email.getText().contains("@")) { err(fb, "Email invalide."); return; }
            if (pwd.getText().trim().isEmpty())  { err(fb, "Mot de passe vide."); return; }
            if (!pwd.getText().equals(pwdC.getText())) { err(fb, "Mots de passe differents."); return; }
            try {
                String id = MainApp.nextTeacherId();
                MainApp.teacherService.createTeacher(id, prenom.getText().trim(),
                    nom.getText().trim(), email.getText().trim(),
                    matiere.getText().trim(), pwd.getText().trim());
                MainApp.save();
                ok(fb, "Compte cree (ID : " + id + ") ! Connectez-vous.");
                prenom.clear(); nom.clear(); email.clear(); matiere.clear(); pwd.clear(); pwdC.clear();
            } catch (Exception ex) { MainApp.cancelTeacherId(); err(fb, ex.getMessage()); }
        });
        form.getChildren().addAll(MainApp.titleLabel("Inscription Enseignant"),
            new Label("Prenom :"), prenom, new Label("Nom :"), nom,
            new Label("Email :"), email, new Label("Matiere :"), matiere,
            new Label("Mot de passe :"), pwd, new Label("Confirmer :"), pwdC, btn, fb);
        return new ScrollPane(form);
    }

    private TextField f(String p) { TextField t = new TextField(); t.setPromptText(p); return t; }
    private PasswordField pf(String p) { PasswordField f = new PasswordField(); f.setPromptText(p); return f; }
    private void ok(Label l, String m)  { l.setText(m); l.setStyle("-fx-text-fill:green;"); }
    private void err(Label l, String m) { l.setText(m); l.setStyle("-fx-text-fill:red;"); }
    public BorderPane getRoot() { return root; }
}
