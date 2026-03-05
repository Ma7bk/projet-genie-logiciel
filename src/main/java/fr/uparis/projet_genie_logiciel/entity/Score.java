package fr.uparis.projet_genie_logiciel.entity;


public class Score {
    private int value;
    private final Quiz quiz;

    public Score(Quiz quiz) {
        if (quiz == null) {
            throw new IllegalArgumentException("Le quiz ne peut pas être null");
        }
        this.quiz = quiz;
        this.value = 0;
    }

    
    public void addPoint() {
        this.value++;
    }

    
    public int getValue() {
        return value;
    }

    
    public String display() {
        return "Score pour le quiz '" + quiz.getTitle() + "' : " + value + "/" + quiz.getQuestions().size();
    }

    public Quiz getQuiz() {
        return quiz;
    }
}
