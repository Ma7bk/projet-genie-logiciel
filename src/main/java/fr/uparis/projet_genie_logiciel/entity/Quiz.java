package fr.uparis.projet_genie_logiciel.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Quiz {
    private final String id;
    private final String title;
    private final String course;
    private final int duration;
    private final String teacherId;
    private final List<Question> questions;

    public Quiz(String id, String title, String course, int duration, String teacherId) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du quiz ne peut pas etre vide");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas etre vide");
        }
        if (course == null || course.trim().isEmpty()) {
            throw new IllegalArgumentException("Le cours ne peut pas etre vide");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("La duree doit etre positive");
        }
        if (teacherId == null || teacherId.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID enseignant ne peut pas etre vide");
        }
        this.id = id.trim();
        this.title = title.trim();
        this.course = course.trim();
        this.duration = duration;
        this.teacherId = teacherId.trim();
        this.questions = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getCourse() { return course; }
    public int getDuration() { return duration; }
    public String getTeacherId() { return teacherId; }
    public List<Question> getQuestions() { return new ArrayList<>(questions); }

    public void addQuestion(Question question) {
        if (question != null) { this.questions.add(question); }
    }

    public String start() { return "Demarrage du quiz : " + title; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        return Objects.equals(id, ((Quiz) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
