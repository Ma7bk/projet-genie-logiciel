package fr.uparis.projet_genie_logiciel.presentation;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class CLITest {


    private static final class CliWithOutput {
        final CLI cli;
        final ByteArrayOutputStream buf;

        CliWithOutput(String input) {
            buf = new ByteArrayOutputStream();
            cli = new CLI(new Scanner(input), new PrintStream(buf));
        }

        String output() { return buf.toString(); }
    }

    // CLI sans capture (pour les tests de lecture seule)
    private CLI cli(String input) {
        return new CLI(new Scanner(input), new PrintStream(new ByteArrayOutputStream()));
    }

    @Test
    void testPrint() {
        CliWithOutput c = new CliWithOutput("");
        c.cli.print("bonjour");
        assertTrue(c.output().contains("bonjour"));
    }

    @Test
    void testOk() {
        CliWithOutput c = new CliWithOutput("");
        c.cli.ok("succes");
        assertTrue(c.output().contains("OK succes"));
    }

    @Test
    void testError() {
        CliWithOutput c = new CliWithOutput("");
        c.cli.error("probleme");
        assertTrue(c.output().contains("ERREUR probleme"));
    }

    @Test
    void testSep() {
        CliWithOutput c = new CliWithOutput("");
        c.cli.sep();
        assertTrue(c.output().contains("---"));
    }

    @Test
    void testTitle() {
        CliWithOutput c = new CliWithOutput("");
        c.cli.title("MON TITRE");
        assertTrue(c.output().contains("MON TITRE"));
    }

    @Test
    void testBanner() {
        CliWithOutput c = new CliWithOutput("");
        c.cli.banner();
        assertTrue(c.output().contains("QUIZ"));
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
