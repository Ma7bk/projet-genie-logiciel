package fr.uparis.projet_genie_logiciel.entity;

import java.util.Objects;


public class Teacher {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String subject; // Matière enseignée

  
    public Teacher(String id, String firstName, String lastName, String email, String subject) {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de l'enseignant ne peut pas être vide");
        }
        

        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        if (firstName.length() < 2) {
            throw new IllegalArgumentException("Le prénom doit contenir au moins 2 caractères");
        }
        

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (lastName.length() < 2) {
            throw new IllegalArgumentException("Le nom doit contenir au moins 2 caractères");
        }
        

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
        

        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("La matière enseignée ne peut pas être vide");
        }
        

        this.id = id.trim();
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim().toLowerCase();
        this.subject = subject.trim();
    }


    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + id + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
