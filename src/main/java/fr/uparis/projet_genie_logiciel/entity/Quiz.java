package fr.uparis.projet_genie_logiciel.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Quiz {
    private final String id;
    private final String title;
    private final String course;
    private final List<String> questionIds; // IDs des questions associées
    private final int duration; // Durée en minutes

 
    public Quiz(String id, String title, String course, List<String> questionIds, int duration) {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du quiz ne peut pas être vide");
        }
        

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre du quiz ne peut pas être vide");
        }
        if (title.length() < 3) {
            throw new IllegalArgumentException("Le titre du quiz doit contenir au moins 3 caractères");
        }
        

        if (course == null || course.trim().isEmpty()) {
            throw new IllegalArgumentException("Le cours associé est obligatoire");
        }
        

        if (questionIds == null || questionIds.isEmpty()) {
            throw new IllegalArgumentException("Un quiz doit contenir au moins une question");
        }
        

        if (duration <= 0) {
            throw new IllegalArgumentException("La durée du quiz doit être positive (en minutes)");
        }
        if (duration > 180) {
            throw new IllegalArgumentException("La durée du quiz ne peut pas dépasser 180 minutes (3 heures)");
        }
        

        this.id = id.trim();
        this.title = title.trim();
        this.course = course.trim();
        this.questionIds = new ArrayList<>(questionIds);
        this.duration = duration;
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCourse() {
        return course;
    }

    public List<String> getQuestionIds() {
        return new ArrayList<>(questionIds); // Copie défensive
    }

    public int getDuration() {
        return duration;
    }

    public int getQuestionCount() {
        return questionIds.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equals(id, quiz.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", course='" + course + '\'' +
                ", questionCount=" + questionIds.size() +
                ", duration=" + duration + " min" +
                '}';
    }
}
