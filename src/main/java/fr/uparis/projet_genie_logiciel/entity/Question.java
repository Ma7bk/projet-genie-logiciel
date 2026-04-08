package fr.uparis.projet_genie_logiciel.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class Question {
    private final String id;
    private final String text;
    private final String course;
    protected final List<Choice> choices;

    public Question(String id, String text, String course) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de la question ne peut pas etre vide");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Le texte ne peut pas etre vide");
        }
        if (course == null || course.trim().isEmpty()) {
            throw new IllegalArgumentException("Le cours ne peut pas etre vide");
        }
        this.id = id.trim();
        this.text = text.trim();
        this.course = course.trim();
        this.choices = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getText() { return text; }
    public String getCourse() { return course; }
    public List<Choice> getChoices() { return new ArrayList<>(choices); }

    public boolean checkAnswer(Choice choice) {
        if (choice == null) { return false; }
        return choice.isCorrectAnswer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        return Objects.equals(id, ((Question) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
