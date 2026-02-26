package fr.uparis.projet_genie_logiciel.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Question {
    private final String id;
    private final String text;
    private final String type; 
    private final String course; 
    private final List<String> options;
    private final String correctAnswer;

  
    public Question(String id, String text, String type, String course, 
                    List<String> options, String correctAnswer) {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de la question ne peut pas être vide");
        }
        

        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Le texte de la question ne peut pas être vide");
        }
        

        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type de question est obligatoire");
        }
        if (!type.equals("QCM") && !type.equals("VRAI_FAUX") && !type.equals("TEXTE")) {
            throw new IllegalArgumentException("Type de question invalide. Types acceptés : QCM, VRAI_FAUX, TEXTE");
        }
        

        if (course == null || course.trim().isEmpty()) {
            throw new IllegalArgumentException("Le cours associé est obligatoire");
        }
        

        if (type.equals("QCM")) {
            if (options == null || options.size() < 2) {
                throw new IllegalArgumentException("Un QCM doit avoir au moins 2 options");
            }
            if (options.size() > 4) {
                throw new IllegalArgumentException("Un QCM ne peut pas avoir plus de 4 options");
            }
        } else if (type.equals("VRAI_FAUX")) {
            if (options == null || options.size() != 2) {
                throw new IllegalArgumentException("Une question VRAI/FAUX doit avoir exactement 2 options");
            }
        }
        

        if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
            throw new IllegalArgumentException("La réponse correcte est obligatoire");
        }
        if (options != null && !options.contains(correctAnswer)) {
            throw new IllegalArgumentException("La réponse correcte doit faire partie des options proposées");
        }
        

        this.id = id.trim();
        this.text = text.trim();
        this.type = type.trim();
        this.course = course.trim();
        this.options = options != null ? new ArrayList<>(options) : new ArrayList<>();
        this.correctAnswer = correctAnswer.trim();
    }


    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public String getCourse() {
        return course;
    }

    public List<String> getOptions() {
        return new ArrayList<>(options); // Copie défensive
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }


    public boolean isCorrectAnswer(String answer) {
        return correctAnswer.equalsIgnoreCase(answer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", course='" + course + '\'' +
                ", options=" + options +
                '}';
    }
}
