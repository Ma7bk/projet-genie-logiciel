package fr.uparis.projet_genie_logiciel.entity;


public class Choice {
    private final String text;
    private final boolean isCorrect;

    public Choice(String text, boolean isCorrect) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Le texte du choix ne peut pas être vide");
        }
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrectAnswer() {
        return isCorrect;
    }

    @Override
    public String toString() {
        return text + (isCorrect ? " (Correct)" : "");
    }
}
