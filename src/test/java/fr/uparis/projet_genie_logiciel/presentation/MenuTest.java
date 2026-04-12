package fr.uparis.projet_genie_logiciel.presentation;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private CLI cli(String input) {
        return new CLI(new Scanner(input));
    }

    @Test
    void testMenuExitImmediately() {
        CLI c = cli("0\n");
        Menu menu = new Menu("TEST", c);
        assertDoesNotThrow(menu::show);
    }

    @Test
    void testMenuExecutesCommand() {
        boolean[] executed = {false};
        CLI c = cli("1\n0\n");
        Menu menu = new Menu("TEST", c);
        menu.addCommand(new Command() {
            @Override public void execute() { executed[0] = true; }
            @Override public String getDescription() { return "Test cmd"; }
        });
        menu.show();
        assertTrue(executed[0]);
    }

    @Test
    void testMenuInvalidOptionThenExit() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        CLI c = cli("99\n0\n");
        Menu menu = new Menu("TEST", c);
        menu.show();
        assertTrue(out.toString().contains("invalide") || out.toString().contains("ERREUR"));
        System.setOut(System.out);
    }

    @Test
    void testMenuMultipleCommands() {
        int[] count = {0};
        CLI c = cli("1\n2\n1\n0\n");
        Menu menu = new Menu("TEST", c);
        menu.addCommand(new Command() {
            @Override public void execute() { count[0]++; }
            @Override public String getDescription() { return "Cmd 1"; }
        });
        menu.addCommand(new Command() {
            @Override public void execute() { count[0] += 10; }
            @Override public String getDescription() { return "Cmd 2"; }
        });
        menu.show();
        assertEquals(12, count[0]);
    }

    @Test
    void testMenuShowsTitle() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        CLI c = cli("0\n");
        Menu menu = new Menu("MON MENU", c);
        menu.show();
        assertTrue(out.toString().contains("MON MENU"));
        System.setOut(System.out);
    }

    @Test
    void testMenuShowsCommandDescription() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        CLI c = cli("0\n");
        Menu menu = new Menu("TEST", c);
        menu.addCommand(new Command() {
            @Override public void execute() { }
            @Override public String getDescription() { return "Ma super commande"; }
        });
        menu.show();
        assertTrue(out.toString().contains("Ma super commande"));
        System.setOut(System.out);
    }

    @Test
    void testMenuShowsRetourOption() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        CLI c = cli("0\n");
        Menu menu = new Menu("TEST", c);
        menu.show();
        assertTrue(out.toString().contains("0"));
        System.setOut(System.out);
    }
    @Test
    void testMenuWithNullTitleDoesNotThrow() {
        CLI c = cli("0\n");
        Menu menu = new Menu(null, c);
        assertDoesNotThrow(menu::show);
    }

    @Test
    void testMenuWithEmptyTitle() {
        CLI c = cli("0\n");
        Menu menu = new Menu("", c);
        assertDoesNotThrow(menu::show);
    }

    @Test
    void testMenuNoCommandsExitsCleanly() {
        CLI c = cli("0\n");
        Menu menu = new Menu("VIDE", c);
        assertDoesNotThrow(menu::show);
    }

    @Test
    void testMenuInvalidChoiceThenExit() {
        CLI c = cli("99\n0\n");
        Menu menu = new Menu("TEST", c);
        assertDoesNotThrow(menu::show);
    }
}
