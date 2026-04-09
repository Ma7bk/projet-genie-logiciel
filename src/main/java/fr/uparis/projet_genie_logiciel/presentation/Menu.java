package fr.uparis.projet_genie_logiciel.presentation;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final String title;
    private final CLI cli;
    private final List<Command> commands = new ArrayList<>();

    public Menu(String title, CLI cli) { this.title = title; this.cli = cli; }

    public void addCommand(Command command) { commands.add(command); }

    public void show() {
        boolean running = true;
        while (running) {
            cli.title(title);
            for (int i = 0; i < commands.size(); i++) {
                cli.print("  " + (i + 1) + ". " + commands.get(i).getDescription());
            }
            cli.print("  0. Retour");
            cli.sep();
            cli.print("Votre choix : ");
            int choice = cli.readInt();
            if (choice == 0) { running = false; }
            else if (choice >= 1 && choice <= commands.size()) { commands.get(choice - 1).execute(); }
            else { cli.error("Option invalide."); }
        }
    }
}
