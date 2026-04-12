package fr.uparis.projet_genie_logiciel.gui;

import fr.uparis.projet_genie_logiciel.entity.Student;
import fr.uparis.projet_genie_logiciel.entity.Teacher;
import fr.uparis.projet_genie_logiciel.persistence.AppContext;
import fr.uparis.projet_genie_logiciel.persistence.DataStore;
import fr.uparis.projet_genie_logiciel.persistence.PersistenceManager;
import fr.uparis.projet_genie_logiciel.repository.InMemoryQuestionRepository;
import fr.uparis.projet_genie_logiciel.repository.InMemoryQuizRepository;
import fr.uparis.projet_genie_logiciel.repository.InMemoryStudentRepository;
import fr.uparis.projet_genie_logiciel.repository.InMemoryTeacherRepository;
import fr.uparis.projet_genie_logiciel.service.AuthService;
import fr.uparis.projet_genie_logiciel.service.QuestionService;
import fr.uparis.projet_genie_logiciel.service.QuizService;
import fr.uparis.projet_genie_logiciel.service.StudentService;
import fr.uparis.projet_genie_logiciel.service.TeacherService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class MainApp extends Application {

    static InMemoryTeacherRepository teacherRepo;
    static InMemoryStudentRepository studentRepo;
    static InMemoryQuizRepository quizRepo;
    static InMemoryQuestionRepository questionRepo;
    static TeacherService teacherService;
    static StudentService studentService;
    static QuizService quizService;
    static QuestionService questionService;
    static AuthService authService;
    static AppContext ctx;
    static PersistenceManager pm;

    private Stage primaryStage;

    public static void main(String[] args) {
        teacherRepo  = new InMemoryTeacherRepository();
        studentRepo  = new InMemoryStudentRepository();
        quizRepo     = new InMemoryQuizRepository();
        questionRepo = new InMemoryQuestionRepository();
        teacherService  = new TeacherService(teacherRepo);
        studentService  = new StudentService(studentRepo);
        quizService     = new QuizService(quizRepo, questionRepo);
        questionService = new QuestionService(questionRepo);
        authService     = new AuthService(teacherRepo, studentRepo);
        ctx = new AppContext();
        pm  = new PersistenceManager(new DataStore(), teacherRepo, studentRepo,
                  quizRepo, questionRepo, ctx);
        pm.load();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Quiz - Genie Logiciel EIDD 2A SIE");
        stage.setMinWidth(720);
        stage.setMinHeight(560);
        stage.setOnCloseRequest(e -> { pm.save(); Platform.exit(); });
        showWelcome();
        stage.show();
    }

    public void showWelcome() {
        primaryStage.setScene(new javafx.scene.Scene(new WelcomeView(this).getRoot(), 720, 560));
    }

    public void showTeacherSpace() {
        primaryStage.setScene(new javafx.scene.Scene(new TeacherSpaceView(this).getRoot(), 720, 560));
    }

    public void showStudentSpace() {
        primaryStage.setScene(new javafx.scene.Scene(new StudentSpaceView(this).getRoot(), 720, 560));
    }

    public void showTeacherDashboard(Teacher teacher) {
        primaryStage.setScene(new javafx.scene.Scene(
            new TeacherDashboardView(this, teacher).getRoot(), 760, 640));
    }

    public void showStudentDashboard(Student student) {
        primaryStage.setScene(new javafx.scene.Scene(
            new StudentDashboardView(this, student).getRoot(), 760, 640));
    }

    public static void save() { pm.save(); }

    public static String nextTeacherId()  { return ctx.nextTeacherId(); }
    public static String nextStudentId()  { return ctx.nextStudentId(); }
    public static String nextQuizId()     { return ctx.nextQuizId(); }
    public static String nextQuestionId() { return ctx.nextQuestionId(); }
    public static void cancelTeacherId()  { ctx.cancelTeacherId(); }
    public static void cancelStudentId()  { ctx.cancelStudentId(); }
    public static void cancelQuizId()     { ctx.cancelQuizId(); }
    public static void cancelQuestionId() { ctx.cancelQuestionId(); }

    public static Button primaryBtn(String text, String color) {
        Button b = new Button(text);
        b.setPrefWidth(300); b.setPrefHeight(46);
        b.setStyle("-fx-background-color:" + color + ";-fx-text-fill:white;"
            + "-fx-font-size:14px;-fx-font-weight:bold;-fx-cursor:hand;"
            + "-fx-background-radius:8;");
        return b;
    }

    public static Button secondaryBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:#ecf0f1;-fx-text-fill:#2c3e50;"
            + "-fx-font-size:13px;-fx-cursor:hand;-fx-background-radius:6;"
            + "-fx-padding:8 16;");
        return b;
    }

    public static Label titleLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size:20px;-fx-font-weight:bold;-fx-text-fill:#2c3e50;");
        return l;
    }

    public static BorderPane pageLayout(String title, String sub, Runnable onBack) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f5f6fa;");
        HBox hdr = new HBox(12);
        hdr.setStyle("-fx-background-color:#2c3e50;-fx-padding:14 20;");
        hdr.setAlignment(Pos.CENTER_LEFT);
        Button back = new Button("<- Retour");
        back.setStyle("-fx-background-color:transparent;-fx-text-fill:#bdc3c7;"
            + "-fx-cursor:hand;-fx-font-size:13px;");
        back.setOnAction(e -> { save(); onBack.run(); });
        Label t = new Label(title);
        t.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:white;");
        Label s = new Label(sub);
        s.setStyle("-fx-font-size:12px;-fx-text-fill:#bdc3c7;");
        hdr.getChildren().addAll(back, t, s);
        root.setTop(hdr);
        return root;
    }

    public static void pad(javafx.scene.layout.VBox box) {
        box.setPadding(new Insets(20));
    }
}
