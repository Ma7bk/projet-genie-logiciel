package fr.uparis.projet_genie_logiciel.entity;


public class TrueFalseQuestion extends Question {

    public TrueFalseQuestion(String id, String text, String course, boolean isTrue) {
        super(id, text, course);
        generateChoices(isTrue);
    }

    
    public void generateChoices(boolean isTrue) {
        choices.clear();
        choices.add(new Choice("Vrai", isTrue));
        choices.add(new Choice("Faux", !isTrue));
    }
}
