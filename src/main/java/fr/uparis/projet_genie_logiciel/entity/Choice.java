package fr.uparis.projet_genie_logiciel.entity;


public class Choice {
    private final String text;
    private final boolean correct;

    public Choice(String text, boolean correct) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Le texte du choix ne peut pas etre vide");
        }
        this.text = text.trim();
        this.correct = correct;
    }

    public String getText() { return text; }
    public boolean isCorrectAnswer() { return correct; }

    @Override
    public String toString() { return text + (correct ? " (Correct)" : ""); }
}
