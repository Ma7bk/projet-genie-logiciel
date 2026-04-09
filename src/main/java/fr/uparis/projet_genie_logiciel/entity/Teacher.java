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
        this.firstName = validate(firstName, "Le prenom");
        this.lastName  = validate(lastName, "Le nom");
        this.email     = validateEmail(email);
        this.subject   = validate(subject, "La matiere");
    }


    public Teacher(String id, String firstName, String lastName,
                   String email, String subject, String hashedPassword, boolean alreadyHashed) {
        super(id != null ? id.trim() : null, hashedPassword, true);
        this.firstName = validate(firstName, "Le prenom");
        this.lastName  = validate(lastName, "Le nom");
        this.email     = validateEmail(email);
        this.subject   = validate(subject, "La matiere");
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
    public String getLastName()  { return lastName; }
    public String getEmail()     { return email; }
    public String getSubject()   { return subject; }
    public String getFullName()  { return firstName + " " + lastName; }


    public String toFileLine(String sep) {
        return getId() + sep + firstName + sep + lastName
            + sep + email + sep + subject + sep + getHashedPassword();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        return Objects.equals(getId(), ((Teacher) o).getId());
    }

    @Override
    public int hashCode() { return Objects.hash(getId()); }
}
