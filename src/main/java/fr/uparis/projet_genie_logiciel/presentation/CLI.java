package fr.uparis.projet_genie_logiciel.presentation;
import java.io.PrintStream;
import java.util.Scanner;

public class CLI {
    private final Scanner scanner;
    private final PrintStream out;


    public CLI(Scanner scanner) {
        this(scanner, System.out); 
    }


    public CLI(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out     = out;
    }

    public void print(String msg) { out.println(msg); }
    public void ok(String msg)    { out.println("OK " + msg); }
    public void error(String msg) { out.println("ERREUR " + msg); }
    public void sep()             { out.println("------------------------------------------"); }
    public void title(String t)   { sep(); out.println("  " + t); sep(); }
    public void banner() {
        out.println("==========================================");
        out.println("        APPLICATION QUIZ");
        out.println("     Groupe 2 - EIDD 2A SIE");
        out.println("==========================================");
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
            if (v.isEmpty())          { error("Ce champ ne peut pas etre vide."); v = ""; }
            else if (!v.contains("@")){ error("Email invalide. Doit contenir '@'."); v = ""; }
        }
        return v;
    }

    public boolean readBoolean(String prompt) {
        if (!prompt.isEmpty()) { print(prompt); }
        String s = scanner.nextLine().trim().toLowerCase();
        return s.equals("o") || s.equals("oui") || s.equals("y") || s.equals("yes");
    }
}
