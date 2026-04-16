package fr.uparis.projet_genie_logiciel.presentation;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class CLITest {


    private CLI cliOut(String input, PrintStream ps) {
        return new CLI(new Scanner(input), ps);
    }


    private CLI cli(String input) {
        return new CLI(new Scanner(input),
            new PrintStream(new ByteArrayOutputStream()));
    }

    @Test
    void testPrint() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            cliOut("", ps).print("bonjour");
        }
        assertTrue(buf.toString().contains("bonjour"));
    }

    @Test
    void testOk() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            cliOut("", ps).ok("succes");
        }
        assertTrue(buf.toString().contains("OK succes"));
    }

    @Test
    void testError() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            cliOut("", ps).error("probleme");
        }
        assertTrue(buf.toString().contains("ERREUR probleme"));
    }

    @Test
    void testSep() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            cliOut("", ps).sep();
        }
        assertTrue(buf.toString().contains("---"));
    }

    @Test
    void testTitle() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            cliOut("", ps).title("MON TITRE");
        }
        assertTrue(buf.toString().contains("MON TITRE"));
    }

    @Test
    void testBanner() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(buf)) {
            cliOut("", ps).banner();
        }
        assertTrue(buf.toString().contains("QUIZ"));
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
