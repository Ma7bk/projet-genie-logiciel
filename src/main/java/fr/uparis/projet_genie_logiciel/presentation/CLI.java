package fr.uparis.projet_genie_logiciel.presentation;
import java.util.Scanner;

public class CLI {
    private final Scanner scanner;
    public CLI(Scanner scanner) { this.scanner = scanner; }

    public void print(String msg) { System.out.println(msg); }
    public void ok(String msg) { System.out.println("OK " + msg); }
    public void error(String msg) { System.out.println("ERREUR " + msg); }
    public void sep() { System.out.println("------------------------------------------"); }
    public void title(String t) { sep(); System.out.println("  " + t); sep(); }
    public void banner() {
        System.out.println("==========================================");
        System.out.println("        APPLICATION QUIZ");
        System.out.println("     Groupe 2 - EIDD 2A SIE");
        System.out.println("==========================================");
    }

    public int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    public int readIntBetween(String prompt, int min, int max) {
        int v = -1;
        while (v < min || v > max) {
            print(prompt);
            v = readInt();
            if (v < min || v > max) { error("Entrez un nombre entre " + min + " et " + max); }
        }
        return v;
    }

    public String readString(String prompt) {
        String v = "";
        while (v.isEmpty()) {
            if (!prompt.isEmpty()) { print(prompt); }
            v = scanner.nextLine().trim();
            if (v.isEmpty()) { error("Ce champ ne peut pas etre vide."); }
        }
        return v;
    }

    public String readEmail(String prompt) {
        String v = "";
        while (v.isEmpty()) {
            print(prompt);
            v = scanner.nextLine().trim();
            if (v.isEmpty()) { error("Ce champ ne peut pas etre vide."); v = ""; }
            else if (!v.contains("@")) { error("Email invalide. Doit contenir '@'."); v = ""; }
        }
        return v;
    }

    public boolean readBoolean(String prompt) {
        if (!prompt.isEmpty()) { print(prompt); }
        String s = scanner.nextLine().trim().toLowerCase();
        return s.equals("o") || s.equals("oui") || s.equals("y") || s.equals("yes");
    }
}
