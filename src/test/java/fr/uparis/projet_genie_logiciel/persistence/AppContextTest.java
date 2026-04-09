package fr.uparis.projet_genie_logiciel.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppContextTest {

    private AppContext ctx;

    @BeforeEach
    void setUp() { ctx = new AppContext(); }

    @Test
    void testInitialCountersAreZero() {
        assertEquals(0, ctx.getTeacherCount());
        assertEquals(0, ctx.getStudentCount());
        assertEquals(0, ctx.getQuizCount());
        assertEquals(0, ctx.getQuestionCount());
    }

    @Test
    void testNextTeacherId() {
        assertEquals("T1", ctx.nextTeacherId());
        assertEquals("T2", ctx.nextTeacherId());
        assertEquals(2, ctx.getTeacherCount());
    }

    @Test
    void testCancelTeacherId() {
        ctx.nextTeacherId();
        ctx.cancelTeacherId();
        assertEquals(0, ctx.getTeacherCount());
    }

    @Test
    void testNextStudentId() {
        assertEquals("S1", ctx.nextStudentId());
        assertEquals("S2", ctx.nextStudentId());
        assertEquals(2, ctx.getStudentCount());
    }

    @Test
    void testCancelStudentId() {
        ctx.nextStudentId();
        ctx.cancelStudentId();
        assertEquals(0, ctx.getStudentCount());
    }

    @Test
    void testNextQuizId() {
        assertEquals("Q1", ctx.nextQuizId());
        assertEquals("Q2", ctx.nextQuizId());
        assertEquals(2, ctx.getQuizCount());
    }

    @Test
    void testCancelQuizId() {
        ctx.nextQuizId();
        ctx.cancelQuizId();
        assertEquals(0, ctx.getQuizCount());
    }

    @Test
    void testNextQuestionId() {
        assertEquals("QU1", ctx.nextQuestionId());
        assertEquals("QU2", ctx.nextQuestionId());
        assertEquals(2, ctx.getQuestionCount());
    }

    @Test
    void testCancelQuestionId() {
        ctx.nextQuestionId();
        ctx.cancelQuestionId();
        assertEquals(0, ctx.getQuestionCount());
    }

    @Test
    void testSetters() {
        ctx.setTeacherCount(5);
        ctx.setStudentCount(10);
        ctx.setQuizCount(3);
        ctx.setQuestionCount(7);
        assertEquals(5, ctx.getTeacherCount());
        assertEquals(10, ctx.getStudentCount());
        assertEquals(3, ctx.getQuizCount());
        assertEquals(7, ctx.getQuestionCount());
    }

    @Test
    void testSettersThenNext() {
        ctx.setTeacherCount(3);
        assertEquals("T4", ctx.nextTeacherId());
        ctx.setStudentCount(2);
        assertEquals("S3", ctx.nextStudentId());
        ctx.setQuizCount(1);
        assertEquals("Q2", ctx.nextQuizId());
        ctx.setQuestionCount(5);
        assertEquals("QU6", ctx.nextQuestionId());
    }
}
