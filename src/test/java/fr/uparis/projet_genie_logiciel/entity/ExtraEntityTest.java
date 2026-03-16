package fr.uparis.projet_genie_logiciel.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExtraEntityTest {

    @Test
    void testStudentEqualsHashCode() {
        Student s1 = new Student("S1", "A", "B", "a@b.com", "2A");
        Student s2 = new Student("S1", "C", "D", "c@d.com", "2B");
        Student s3 = new Student("S2", "A", "B", "a@b.com", "2A");

        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1.hashCode(), s3.hashCode());
    }

    @Test
    void testQuizEqualsHashCode() {
        Quiz q1 = new Quiz("Q1", "T1", "C1", 10);
        Quiz q2 = new Quiz("Q1", "T2", "C2", 20);
        Quiz q3 = new Quiz("Q2", "T1", "C1", 10);

        assertEquals(q1, q2);
        assertNotEquals(q1, q3);
        assertEquals(q1.hashCode(), q2.hashCode());
        assertNotEquals(q1.hashCode(), q3.hashCode());
    }

    @Test
    void testTeacherEqualsHashCode() {
        Teacher t1 = new Teacher("T1", "A", "B", "a@b.com", "GL");
        Teacher t2 = new Teacher("T1", "C", "D", "c@d.com", "Maths");
        Teacher t3 = new Teacher("T2", "A", "B", "a@b.com", "GL");

        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1.hashCode(), t3.hashCode());
    }

    @Test
    void testQuestionEqualsHashCode() {
        QCMQuestion q1 = new QCMQuestion("QU1", "T1", "C1");
        QCMQuestion q2 = new QCMQuestion("QU1", "T2", "C2");
        QCMQuestion q3 = new QCMQuestion("QU2", "T1", "C1");

        assertEquals(q1, q2);
        assertNotEquals(q1, q3);
        assertEquals(q1.hashCode(), q2.hashCode());
        assertNotEquals(q1.hashCode(), q3.hashCode());
    }

    @Test
    void testStudentCreationErrors() {
        assertThrows(IllegalArgumentException.class, () -> new Student("S1", "", "B", "a@b.com", "2A"));
        assertThrows(IllegalArgumentException.class, () -> new Student("S1", "A", "", "a@b.com", "2A"));
        assertThrows(IllegalArgumentException.class, () -> new Student("S1", "A", "B", "invalid", "2A"));
        assertThrows(IllegalArgumentException.class, () -> new Student("S1", "A", "B", "a@b.com", ""));
    }

    @Test
    void testQuizCreationErrors() {
        assertThrows(IllegalArgumentException.class, () -> new Quiz("", "T", "C", 10));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "", "C", 10));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "T", "", 10));
        assertThrows(IllegalArgumentException.class, () -> new Quiz("Q", "T", "C", 0));
    }
}
