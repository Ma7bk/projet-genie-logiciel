package fr.uparis.projet_genie_logiciel.persistence;

import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PersistenceManagerTest {

    private DataStore store;
    private InMemoryTeacherRepository teacherRepo;
    private InMemoryStudentRepository studentRepo;
    private InMemoryQuizRepository quizRepo;
    private InMemoryQuestionRepository questionRepo;
    private AppContext ctx;
    private PersistenceManager pm;

    @BeforeEach
    void setUp() {
        store = new DataStore();
        teacherRepo  = new InMemoryTeacherRepository();
        studentRepo  = new InMemoryStudentRepository();
        quizRepo     = new InMemoryQuizRepository();
        questionRepo = new InMemoryQuestionRepository();
        ctx = new AppContext();
        pm = new PersistenceManager(store, teacherRepo, studentRepo, quizRepo, questionRepo, ctx);
    }

    @AfterEach
    void tearDown() {
        new File(store.getTeachersFile()).delete();
        new File(store.getStudentsFile()).delete();
        new File(store.getQuizzesFile()).delete();
        new File(store.getQuestionsFile()).delete();
        new File(store.getScoresFile()).delete();
        new File(store.getCountersFile()).delete();
    }



    @Test
    void testSaveAndLoadTeacher() {
        teacherRepo.save(new Teacher("T1", "Marie", "Dubois", "marie@u.fr", "GL", "pwd"));
        ctx.setTeacherCount(1);
        pm.save();

        InMemoryTeacherRepository newTeacherRepo = new InMemoryTeacherRepository();
        AppContext newCtx = new AppContext();
        PersistenceManager pm2 = new PersistenceManager(store, newTeacherRepo,
            new InMemoryStudentRepository(), new InMemoryQuizRepository(),
            new InMemoryQuestionRepository(), newCtx);
        pm2.load();

        assertEquals(1, newTeacherRepo.count());
        Teacher loaded = newTeacherRepo.findById("T1");
        assertNotNull(loaded);
        assertEquals("Marie", loaded.getFirstName());
        assertEquals("Dubois", loaded.getLastName());
        assertEquals("marie@u.fr", loaded.getEmail());
        assertEquals("GL", loaded.getSubject());
        assertTrue(loaded.checkPassword("pwd"));
        assertEquals(1, newCtx.getTeacherCount());
    }



    @Test
    void testSaveAndLoadStudent() {
        studentRepo.save(new Student("S1", "Jean", "Dupont", "jean@u.fr", "2A", "pwd"));
        ctx.setStudentCount(1);
        pm.save();

        InMemoryStudentRepository newStudentRepo = new InMemoryStudentRepository();
        AppContext newCtx = new AppContext();
        PersistenceManager pm2 = new PersistenceManager(store, new InMemoryTeacherRepository(),
            newStudentRepo, new InMemoryQuizRepository(), new InMemoryQuestionRepository(), newCtx);
        pm2.load();

        assertEquals(1, newStudentRepo.count());
        Student loaded = newStudentRepo.findById("S1");
        assertNotNull(loaded);
        assertEquals("Jean", loaded.getFirstName());
        assertEquals("2A", loaded.getClasse());
        assertTrue(loaded.checkPassword("pwd"));
        assertEquals(1, newCtx.getStudentCount());
    }



    @Test
    void testSaveAndLoadQuiz() {
        quizRepo.save(new Quiz("Q1", "Java Quiz", "Java", 30, "T1"));
        ctx.setQuizCount(1);
        pm.save();

        InMemoryQuizRepository newQuizRepo = new InMemoryQuizRepository();
        AppContext newCtx = new AppContext();
        PersistenceManager pm2 = new PersistenceManager(store, new InMemoryTeacherRepository(),
            new InMemoryStudentRepository(), newQuizRepo, new InMemoryQuestionRepository(), newCtx);
        pm2.load();

        assertEquals(1, newQuizRepo.count());
        Quiz loaded = newQuizRepo.findById("Q1");
        assertNotNull(loaded);
        assertEquals("Java Quiz", loaded.getTitle());
        assertEquals("Java", loaded.getCourse());
        assertEquals(30, loaded.getDuration());
        assertEquals("T1", loaded.getTeacherId());
        assertEquals(1, newCtx.getQuizCount());
    }



    @Test
    void testSaveAndLoadQCMQuestion() {
        Quiz quiz = new Quiz("Q1", "Java", "Java", 30, "T1");
        quizRepo.save(quiz);
        QCMQuestion q = new QCMQuestion("QU1", "Capitale ?", "Java");
        q.addChoice(new Choice("Paris", true));
        q.addChoice(new Choice("Lyon", false));
        quiz.addQuestion(q);
        questionRepo.save(q);
        ctx.setQuizCount(1); ctx.setQuestionCount(1);
        pm.save();

        InMemoryQuizRepository newQuizRepo = new InMemoryQuizRepository();
        InMemoryQuestionRepository newQRepo = new InMemoryQuestionRepository();
        PersistenceManager pm2 = new PersistenceManager(store, new InMemoryTeacherRepository(),
            new InMemoryStudentRepository(), newQuizRepo, newQRepo, new AppContext());
        pm2.load();

        Quiz loadedQuiz = newQuizRepo.findById("Q1");
        assertNotNull(loadedQuiz);
        assertEquals(1, loadedQuiz.getQuestions().size());
        Question loadedQ = loadedQuiz.getQuestions().get(0);
        assertEquals("Capitale ?", loadedQ.getText());
        assertEquals(2, loadedQ.getChoices().size());
        assertTrue(loadedQ.getChoices().get(0).isCorrectAnswer());
    }



    @Test
    void testSaveAndLoadTrueFalseQuestion() {
        Quiz quiz = new Quiz("Q1", "Sci", "Sciences", 20, "T1");
        quizRepo.save(quiz);
        TrueFalseQuestion tf = new TrueFalseQuestion("QU1", "La Terre est ronde ?", "Sciences", true);
        quiz.addQuestion(tf);
        questionRepo.save(tf);
        pm.save();

        InMemoryQuizRepository newQuizRepo = new InMemoryQuizRepository();
        InMemoryQuestionRepository newQRepo = new InMemoryQuestionRepository();
        PersistenceManager pm2 = new PersistenceManager(store, new InMemoryTeacherRepository(),
            new InMemoryStudentRepository(), newQuizRepo, newQRepo, new AppContext());
        pm2.load();

        Quiz loadedQuiz = newQuizRepo.findById("Q1");
        assertNotNull(loadedQuiz);
        assertEquals(1, loadedQuiz.getQuestions().size());
        Question loadedQ = loadedQuiz.getQuestions().get(0);
        assertTrue(loadedQ instanceof TrueFalseQuestion);
        assertTrue(loadedQ.checkAnswer(loadedQ.getChoices().get(0)));
    }



    @Test
    void testSaveAndLoadTrueFalseFalse() {
        Quiz quiz = new Quiz("Q1", "Sci", "Sciences", 20, "T1");
        quizRepo.save(quiz);
        TrueFalseQuestion tf = new TrueFalseQuestion("QU1", "La Terre est plate ?", "Sciences", false);
        quiz.addQuestion(tf);
        questionRepo.save(tf);
        pm.save();

        InMemoryQuizRepository newQuizRepo = new InMemoryQuizRepository();
        PersistenceManager pm2 = new PersistenceManager(store, new InMemoryTeacherRepository(),
            new InMemoryStudentRepository(), newQuizRepo, new InMemoryQuestionRepository(), new AppContext());
        pm2.load();

        Question loadedQ = newQuizRepo.findById("Q1").getQuestions().get(0);
        assertFalse(loadedQ.checkAnswer(loadedQ.getChoices().get(0)));
        assertTrue(loadedQ.checkAnswer(loadedQ.getChoices().get(1)));
    }



    @Test
    void testSaveAndLoadScores() {
        Quiz quiz = new Quiz("Q1", "Java", "Java", 30, "T1");
        quizRepo.save(quiz);
        Student student = new Student("S1", "Jean", "Dupont", "jean@u.fr", "2A", "pwd");
        Score score = new Score(quiz);
        score.addPoint(); score.addPoint();
        student.addScoreToHistory(score);
        studentRepo.save(student);
        pm.save();

        InMemoryQuizRepository newQuizRepo = new InMemoryQuizRepository();
        InMemoryStudentRepository newStudentRepo = new InMemoryStudentRepository();
        PersistenceManager pm2 = new PersistenceManager(store, new InMemoryTeacherRepository(),
            newStudentRepo, newQuizRepo, new InMemoryQuestionRepository(), new AppContext());
        pm2.load();

        Student loadedStudent = newStudentRepo.findById("S1");
        assertNotNull(loadedStudent);
        assertEquals(1, loadedStudent.viewScoreHistory().size());
        assertEquals(2, loadedStudent.viewScoreHistory().get(0).getValue());
    }



    @Test
    void testLoadFromEmptyFiles() {
        pm.save(); // sauvegarder vide
        pm.load(); // recharger - ne doit pas planter
        assertEquals(0, teacherRepo.count());
        assertEquals(0, studentRepo.count());
        assertEquals(0, quizRepo.count());
    }



    @Test
    void testSaveAndLoadCounters() {
        ctx.setTeacherCount(3);
        ctx.setStudentCount(5);
        ctx.setQuizCount(2);
        ctx.setQuestionCount(8);
        pm.save();

        AppContext newCtx = new AppContext();
        PersistenceManager pm2 = new PersistenceManager(store, new InMemoryTeacherRepository(),
            new InMemoryStudentRepository(), new InMemoryQuizRepository(),
            new InMemoryQuestionRepository(), newCtx);
        pm2.load();

        assertEquals(3, newCtx.getTeacherCount());
        assertEquals(5, newCtx.getStudentCount());
        assertEquals(2, newCtx.getQuizCount());
        assertEquals(8, newCtx.getQuestionCount());
    }



    @Test
    void testLoadInvalidLinesIgnored() {
        store.writeLines(store.getTeachersFile(), java.util.Arrays.asList("invalide|trop|court"));
        store.writeLines(store.getStudentsFile(), java.util.Arrays.asList("bad"));
        store.writeLines(store.getQuizzesFile(), java.util.Arrays.asList("Q1|titre|cours"));
        store.writeLines(store.getQuestionsFile(), java.util.Arrays.asList("QCM|id|txt"));
        store.writeLines(store.getScoresFile(), java.util.Arrays.asList("S1"));
        pm.load(); // ne doit pas planter
        assertEquals(0, teacherRepo.count());
        assertEquals(0, studentRepo.count());
        assertEquals(0, quizRepo.count());
    }



    @Test
    void testLoadQuestionWithoutQuiz() {
        store.writeLines(store.getQuestionsFile(),
            java.util.Arrays.asList("QCM|QU1|Question?|Java|Q_INEXISTANT|Choix1:true|Choix2:false"));
        pm.load(); // ne doit pas planter
        assertEquals(0, quizRepo.count());
    }



    @Test
    void testSaveAndLoadMultipleEntities() {
        teacherRepo.save(new Teacher("T1", "A", "B", "a@u.fr", "GL", "pwd"));
        teacherRepo.save(new Teacher("T2", "C", "D", "c@u.fr", "Math", "pwd"));
        studentRepo.save(new Student("S1", "E", "F", "e@u.fr", "2A", "pwd"));
        studentRepo.save(new Student("S2", "G", "H", "g@u.fr", "2B", "pwd"));
        quizRepo.save(new Quiz("Q1", "Quiz1", "GL", 30, "T1"));
        quizRepo.save(new Quiz("Q2", "Quiz2", "Math", 20, "T2"));
        ctx.setTeacherCount(2); ctx.setStudentCount(2); ctx.setQuizCount(2);
        pm.save();

        InMemoryTeacherRepository newTR = new InMemoryTeacherRepository();
        InMemoryStudentRepository newSR = new InMemoryStudentRepository();
        InMemoryQuizRepository newQR = new InMemoryQuizRepository();
        PersistenceManager pm2 = new PersistenceManager(store, newTR, newSR, newQR,
            new InMemoryQuestionRepository(), new AppContext());
        pm2.load();

        assertEquals(2, newTR.count());
        assertEquals(2, newSR.count());
        assertEquals(2, newQR.count());
    }
}
