package fr.uparis.projet_genie_logiciel.entity;

import java.util.Objects;


public class Student {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String classe; // Classe de l'étudiant

    public Student(String id, String firstName, String lastName, String email, String classe) {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de l'étudiant ne peut pas être vide");
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
        

        if (classe == null || classe.trim().isEmpty()) {
            throw new IllegalArgumentException("La classe ne peut pas être vide");
        }
        

        this.id = id.trim();
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim().toLowerCase();
        this.classe = classe.trim();
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

    public String getClasse() {
        return classe;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", classe='" + classe + '\'' +
                '}';
    }
}
