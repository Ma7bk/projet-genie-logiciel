package fr.uparis.projet_genie_logiciel.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Student extends User {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String classe;
    private final List<Score> scoreHistory;


    public Student(String id, String firstName, String lastName,
                   String email, String classe, String password) {
        super(id != null ? id.trim() : null, password);
        this.firstName    = validate(firstName, "Le prenom");
        this.lastName     = validate(lastName, "Le nom");
        this.email        = validateEmail(email);
        this.classe       = validate(classe, "La classe");
        this.scoreHistory = new ArrayList<>();
    }


    public Student(String id, String firstName, String lastName,
                   String email, String classe, String hashedPassword, boolean alreadyHashed) {
        super(id != null ? id.trim() : null, hashedPassword, true);
        this.firstName    = validate(firstName, "Le prenom");
        this.lastName     = validate(lastName, "Le nom");
        this.email        = validateEmail(email);
        this.classe       = validate(classe, "La classe");
        this.scoreHistory = new ArrayList<>();
    }

    private static String validate(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " ne peut pas etre vide");
        }
        return value.trim();
    }

    private static String validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email invalide");
        }
        return email.trim().toLowerCase();
    }

    public void startQuiz(Quiz quiz) {
        if (quiz != null) { quiz.start(); }
    }

    public void submitAnswer(Question question, Choice choice) {
        if (question != null && choice != null) { question.checkAnswer(choice); }
    }


    public List<Score> viewScoreHistory() { return new ArrayList<>(scoreHistory); }

    public void addScoreToHistory(Score score) {
        if (score != null) { this.scoreHistory.add(score); }
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getEmail()     { return email; }
    public String getClasse()    { return classe; }
    public String getFullName()  { return firstName + " " + lastName; }


    public String toFileLine(String sep) {
        return getId() + sep + firstName + sep + lastName
            + sep + email + sep + classe + sep + getHashedPassword();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        return Objects.equals(getId(), ((Student) o).getId());
    }

    @Override
    public int hashCode() { return Objects.hash(getId()); }
}
