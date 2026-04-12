package fr.uparis.projet_genie_logiciel.gui;

import fr.uparis.projet_genie_logiciel.entity.Student;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class StudentSpaceView {
    private final BorderPane root;

    public StudentSpaceView(MainApp app) {
        root = MainApp.pageLayout("Espace Etudiant", "", app::showWelcome);
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
        Button btn = MainApp.primaryBtn("Se connecter", "#27ae60"); btn.setPrefWidth(260);
        btn.setOnAction(e -> {
            Student s = MainApp.authService.loginStudent(email.getText().trim(), pwd.getText());
            if (s != null) { MainApp.save(); app.showStudentDashboard(s); }
            else { fb.setText("Email ou mot de passe incorrect."); fb.setStyle("-fx-text-fill:red;"); }
        });
        form.getChildren().addAll(MainApp.titleLabel("Connexion Etudiant"),
            new Label("Email :"), email, new Label("Mot de passe :"), pwd, btn, fb);
        return new ScrollPane(form);
    }

    private ScrollPane buildRegister() {
        VBox form = new VBox(8); MainApp.pad(form);
        TextField prenom = f("Prenom"), nom = f("Nom"), email = f("Email"), classe = f("Classe");
        PasswordField pwd = pf("Mot de passe"), pwdC = pf("Confirmer");
        Label fb = new Label();
        Button btn = MainApp.primaryBtn("Creer mon compte", "#27ae60"); btn.setPrefWidth(260);
        btn.setOnAction(e -> {
            if (!email.getText().contains("@")) { err(fb, "Email invalide."); return; }
            if (pwd.getText().trim().isEmpty())  { err(fb, "Mot de passe vide."); return; }
            if (!pwd.getText().equals(pwdC.getText())) { err(fb, "Mots de passe differents."); return; }
            try {
                String id = MainApp.nextStudentId();
                MainApp.studentService.createStudent(id, prenom.getText().trim(),
                    nom.getText().trim(), email.getText().trim(),
                    classe.getText().trim(), pwd.getText().trim());
                MainApp.save();
                ok(fb, "Compte cree (ID : " + id + ") ! Connectez-vous.");
                prenom.clear(); nom.clear(); email.clear(); classe.clear(); pwd.clear(); pwdC.clear();
            } catch (Exception ex) { MainApp.cancelStudentId(); err(fb, ex.getMessage()); }
        });
        form.getChildren().addAll(MainApp.titleLabel("Inscription Etudiant"),
            new Label("Prenom :"), prenom, new Label("Nom :"), nom,
            new Label("Email :"), email, new Label("Classe :"), classe,
            new Label("Mot de passe :"), pwd, new Label("Confirmer :"), pwdC, btn, fb);
        return new ScrollPane(form);
    }

    private TextField f(String p) { TextField t = new TextField(); t.setPromptText(p); return t; }
    private PasswordField pf(String p) { PasswordField f = new PasswordField(); f.setPromptText(p); return f; }
    private void ok(Label l, String m)  { l.setText(m); l.setStyle("-fx-text-fill:green;"); }
    private void err(Label l, String m) { l.setText(m); l.setStyle("-fx-text-fill:red;"); }
    public BorderPane getRoot() { return root; }
}
