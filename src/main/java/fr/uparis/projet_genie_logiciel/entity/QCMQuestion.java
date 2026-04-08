package fr.uparis.projet_genie_logiciel.entity;


public class QCMQuestion extends Question {

    public QCMQuestion(String id, String text, String course) {
        super(id, text, course);
    }

    public void addChoice(Choice choice) {
        if (choice == null) {
            throw new IllegalArgumentException("Le choix ne peut pas etre null");
        }
        if (choices.size() >= 6) {
            throw new IllegalStateException("Maximum 6 choix par question QCM");
        }
        choices.add(choice);
    }

    public void validateChoices() {
        if (choices.size() < 2) {
            throw new IllegalStateException("Une question QCM doit avoir au moins 2 choix");
        }
    }
}
