package fr.uparis.projet_genie_logiciel.entity;


public class QCMQuestion extends Question {

    public QCMQuestion(String id, String text, String course) {
        super(id, text, course);
    }

    
    public void addChoice(Choice choice) {
        if (choice == null) {
            throw new IllegalArgumentException("Le choix ne peut pas être null");
        }
        if (choices.size() >= 6) {
            throw new IllegalStateException("Une question QCM ne peut pas avoir plus de 6 choix");
        }
        choices.add(choice);
    }
}
