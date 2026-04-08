package fr.uparis.projet_genie_logiciel.entity;

import java.util.Objects;


public class Teacher extends User {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String subject;

    public Teacher(String id, String firstName, String lastName,
                   String email, String subject, String password) {
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
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("La matiere ne peut pas etre vide");
        }
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim().toLowerCase();
        this.subject = subject.trim();
    }

    public Quiz createQuiz(String id, String title, String course, int duration) {
        return new Quiz(id, title, course, duration, getId());
    }

    public void addQuestion(Quiz quiz, Question question) {
        if (quiz != null && question != null) { quiz.addQuestion(question); }
    }

    public boolean defineCorrectAnswer(Choice choice) {
        if (choice == null) { return false; }
        return choice.isCorrectAnswer();
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getSubject() { return subject; }
    public String getFullName() { return firstName + " " + lastName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        return Objects.equals(getId(), ((Teacher) o).getId());
    }

    @Override
    public int hashCode() { return Objects.hash(getId()); }
}
