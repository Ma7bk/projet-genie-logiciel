package fr.uparis.projet_genie_logiciel.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Quiz {
    private final String id;
    private final String title;
    private final String course;
    private final List<Question> questions;
    private final int duration;

    public Quiz(String id, String title, String course, int duration) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du quiz ne peut pas être vide");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre du quiz ne peut pas être vide");
        }
        if (course == null || course.trim().isEmpty()) {
            throw new IllegalArgumentException("Le cours associé est obligatoire");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("La durée doit être positive");
        }

        this.id = id.trim();
        this.title = title.trim();
        this.course = course.trim();
        this.duration = duration;
        this.questions = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return new ArrayList<>(questions);
    }

    public void addQuestion(Question question) {
        if (question != null) {
            this.questions.add(question);
        }
    }

    public void start() {
        System.out.println("Démarrage du quiz : " + title);
    }

    public Question getFirstQuestion() {
        if (questions.isEmpty()) {
            return null;
        }
        return questions.get(0);
    }

    public String getId() {
        return id;
    }

    public String getCourse() {
        return course;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quiz quiz = (Quiz) o;
        return Objects.equals(id, quiz.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
