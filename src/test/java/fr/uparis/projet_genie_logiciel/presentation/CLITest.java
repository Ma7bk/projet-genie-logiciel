package fr.uparis.projet_genie_logiciel.presentation;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class CLITest {

    private CLI cli(String input) {
        return new CLI(new Scanner(input));
    }



    @Test
    void testPrint() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        cli("").print("bonjour");
        assertTrue(out.toString().contains("bonjour"));
        System.setOut(System.out);
    }

    @Test
    void testOk() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        cli("").ok("succes");
        assertTrue(out.toString().contains("OK succes"));
        System.setOut(System.out);
    }

    @Test
    void testError() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        cli("").error("probleme");
        assertTrue(out.toString().contains("ERREUR probleme"));
        System.setOut(System.out);
    }

    @Test
    void testSep() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        cli("").sep();
        assertTrue(out.toString().contains("---"));
        System.setOut(System.out);
    }

    @Test
    void testTitle() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        cli("").title("MON TITRE");
        assertTrue(out.toString().contains("MON TITRE"));
        System.setOut(System.out);
    }

    @Test
    void testBanner() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        cli("").banner();
        assertTrue(out.toString().contains("QUIZ"));
        System.setOut(System.out);
    }



    @Test
    void testReadIntValid() {
        assertEquals(3, cli("3\n").readInt());
    }

    @Test
    void testReadIntInvalid() {
        assertEquals(-1, cli("abc\n").readInt());
    }

    @Test
    void testReadIntNegative() {
        assertEquals(-5, cli("-5\n").readInt());
    }



    @Test
    void testReadIntBetweenValid() {
        assertEquals(2, cli("2\n").readIntBetween("Choix : ", 1, 3));
    }

    @Test
    void testReadIntBetweenRetryThenValid() {
        // Premier essai invalide (0), deuxième valide (2)
        assertEquals(2, cli("0\n2\n").readIntBetween("", 1, 3));
    }

    @Test
    void testReadIntBetweenRetryText() {
        assertEquals(1, cli("abc\n1\n").readIntBetween("", 1, 2));
    }



    @Test
    void testReadStringValid() {
        assertEquals("hello", cli("hello\n").readString("Entrez : "));
    }

    @Test
    void testReadStringEmptyThenValid() {
        assertEquals("ok", cli("\nok\n").readString(""));
    }

    @Test
    void testReadStringWithEmptyPrompt() {
        assertEquals("test", cli("test\n").readString(""));
    }

    // ── readEmail ─────────────────────────────────────────────────────────────

    @Test
    void testReadEmailValid() {
        assertEquals("a@b.com", cli("a@b.com\n").readEmail("Email : "));
    }

    @Test
    void testReadEmailInvalidThenValid() {
        assertEquals("a@b.com", cli("invalide\na@b.com\n").readEmail("Email : "));
    }

    @Test
    void testReadEmailEmptyThenValid() {
        assertEquals("a@b.com", cli("\na@b.com\n").readEmail("Email : "));
    }

    // ── readBoolean ───────────────────────────────────────────────────────────

    @Test
    void testReadBooleanOui() {
        assertTrue(cli("o\n").readBoolean("Oui ? "));
    }

    @Test
    void testReadBooleanOuiLong() {
        assertTrue(cli("oui\n").readBoolean(""));
    }

    @Test
    void testReadBooleanY() {
        assertTrue(cli("y\n").readBoolean(""));
    }

    @Test
    void testReadBooleanYes() {
        assertTrue(cli("yes\n").readBoolean(""));
    }

    @Test
    void testReadBooleanNon() {
        assertFalse(cli("n\n").readBoolean(""));
    }

    @Test
    void testReadBooleanOther() {
        assertFalse(cli("maybe\n").readBoolean(""));
    }

    @Test
    void testReadBooleanEmptyPrompt() {
        assertFalse(cli("n\n").readBoolean(""));
    }
}
