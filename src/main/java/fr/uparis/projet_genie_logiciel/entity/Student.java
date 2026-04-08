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
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prenom ne peut pas etre vide");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas etre vide");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email invalide");
        }
        if (classe == null || classe.trim().isEmpty()) {
            throw new IllegalArgumentException("La classe ne peut pas etre vide");
        }
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim().toLowerCase();
        this.classe = classe.trim();
        this.scoreHistory = new ArrayList<>();
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
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getClasse() { return classe; }
    public String getFullName() { return firstName + " " + lastName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        return Objects.equals(getId(), ((Student) o).getId());
    }

    @Override
    public int hashCode() { return Objects.hash(getId()); }
}
