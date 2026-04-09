package fr.uparis.projet_genie_logiciel.persistence;
import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.repository.*;
import java.util.ArrayList;
import java.util.List;
 
public class PersistenceManager {
    private static final String SEP = "|";
    private static final String SEP_RX = "\\|";
    private static final String CHOICE_SEP = ":";
 
    private final DataStore store;
    private final TeacherRepository teacherRepo;
    private final StudentRepository studentRepo;
    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    private final AppContext ctx;
 
    public PersistenceManager(DataStore store, TeacherRepository teacherRepo,
                               StudentRepository studentRepo, QuizRepository quizRepo,
                               QuestionRepository questionRepo, AppContext ctx) {
        this.store = store;
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;
        this.ctx = ctx;
    }
 
    public void load() {
        loadCounters(); loadTeachers(); loadQuizzes(); loadQuestions();
        loadStudents(); loadScores();
        int nb = teacherRepo.count() + studentRepo.count() + quizRepo.count();
        if (nb > 0) {
            System.out.println("OK Donnees chargees : " + teacherRepo.count()
                + " enseignant(s), " + studentRepo.count()
                + " etudiant(s), " + quizRepo.count() + " quiz");
        }
    }
 
    private void loadCounters() {
        List<String> lines = store.readLines(store.getCountersFile());
        if (lines.isEmpty()) { return; }
        String[] p = lines.get(0).split(SEP_RX);
        if (p.length >= 4) {
            ctx.setTeacherCount(safe(p[0]));
            ctx.setStudentCount(safe(p[1]));
            ctx.setQuizCount(safe(p[2]));
            ctx.setQuestionCount(safe(p[3]));
        }
    }
 
    private void loadTeachers() {
        for (String line : store.readLines(store.getTeachersFile())) {
            String[] p = line.split(SEP_RX, -1);
            if (p.length >= 6) {
                try { teacherRepo.save(new Teacher(p[0], p[1], p[2], p[3], p[4], p[5], true)); }
                catch (Exception ignored) { }
            }
        }
    }
 
    private void loadQuizzes() {
        for (String line : store.readLines(store.getQuizzesFile())) {
            String[] p = line.split(SEP_RX, -1);
            if (p.length >= 5) {
                try { quizRepo.save(new Quiz(p[0], p[1], p[2], safe(p[3]), p[4])); }
                catch (Exception ignored) { }
            }
        }
    }
 
    private void loadQuestions() {
        for (String line : store.readLines(store.getQuestionsFile())) {
            String[] p = line.split(SEP_RX, -1);
            if (p.length < 5) { continue; }
            try {
                String type = p[0]; String id = p[1];
                String text = p[2]; String course = p[3]; String quizId = p[4];
                Quiz quiz = quizRepo.findById(quizId);
                if ("TF".equals(type)) {
                    boolean isTrue = p.length > 5 && "true".equals(p[5]);
                    TrueFalseQuestion q = new TrueFalseQuestion(id, text, course, isTrue);
                    questionRepo.save(q);
                    if (quiz != null) { quiz.addQuestion(q); }
                } else {
                    QCMQuestion q = new QCMQuestion(id, text, course);
                    for (int i = 5; i < p.length; i++) {
                        String[] c = p[i].split(CHOICE_SEP, 2);
                        if (c.length == 2) { q.addChoice(new Choice(c[0], "true".equals(c[1]))); }
                    }
                    questionRepo.save(q);
                    if (quiz != null) { quiz.addQuestion(q); }
                }
            } catch (Exception ignored) { }
        }
    }
 
    private void loadStudents() {
        for (String line : store.readLines(store.getStudentsFile())) {
            String[] p = line.split(SEP_RX, -1);
            if (p.length >= 6) {
                try { studentRepo.save(new Student(p[0], p[1], p[2], p[3], p[4], p[5], true)); }
                catch (Exception ignored) { }
            }
        }
    }
 
    private void loadScores() {
        for (String line : store.readLines(store.getScoresFile())) {
            String[] p = line.split(SEP_RX, -1);
            if (p.length < 3) { continue; }
            try {
                Student student = studentRepo.findById(p[0]);
                Quiz quiz = quizRepo.findById(p[1]);
                if (student != null && quiz != null) {
                    Score score = new Score(quiz);
                    for (int i = 0; i < safe(p[2]); i++) { score.addPoint(); }
                    student.addScoreToHistory(score);
                }
            } catch (Exception ignored) { }
        }
    }
 
    public void save() {
        saveCounters();
        saveTeachers();
        saveQuizzes();
        saveQuestions();
        saveStudents();
        saveScores();
    }
 
    private void saveCounters() {
        List<String> lines = new ArrayList<>();
        lines.add(ctx.getTeacherCount() + SEP + ctx.getStudentCount()
            + SEP + ctx.getQuizCount() + SEP + ctx.getQuestionCount());
        store.writeLines(store.getCountersFile(), lines);
    }
 
    private void saveTeachers() {
        List<String> lines = new ArrayList<>();
        for (Teacher t : teacherRepo.findAll()) {
            lines.add(t.toFileLine(SEP));
        }
        store.writeLines(store.getTeachersFile(), lines);
    }
 
    private void saveQuizzes() {
        List<String> lines = new ArrayList<>();
        for (Quiz q : quizRepo.findAll()) {
            lines.add(q.getId() + SEP + q.getTitle() + SEP + q.getCourse()
                + SEP + q.getDuration() + SEP + q.getTeacherId());
        }
        store.writeLines(store.getQuizzesFile(), lines);
    }
 
    private void saveQuestions() {
        List<String> lines = new ArrayList<>();
        for (Quiz quiz : quizRepo.findAll()) {
            for (Question q : quiz.getQuestions()) {
                lines.add(serializeQuestion(quiz, q));
            }
        }
        store.writeLines(store.getQuestionsFile(), lines);
    }
 
    private String serializeQuestion(Quiz quiz, Question q) {
        StringBuilder sb = new StringBuilder();
        if (q instanceof TrueFalseQuestion) {
            boolean isTrue = !q.getChoices().isEmpty() && q.getChoices().get(0).isCorrectAnswer();
            sb.append("TF").append(SEP).append(q.getId()).append(SEP)
              .append(q.getText()).append(SEP).append(q.getCourse())
              .append(SEP).append(quiz.getId()).append(SEP).append(isTrue);
        } else {
            sb.append("QCM").append(SEP).append(q.getId()).append(SEP)
              .append(q.getText()).append(SEP).append(q.getCourse())
              .append(SEP).append(quiz.getId());
            for (Choice c : q.getChoices()) {
                sb.append(SEP).append(c.getText()).append(CHOICE_SEP).append(c.isCorrectAnswer());
            }
        }
        return sb.toString();
    }
 
    private void saveStudents() {
        List<String> lines = new ArrayList<>();
        for (Student s : studentRepo.findAll()) {
            lines.add(s.toFileLine(SEP));
        }
        store.writeLines(store.getStudentsFile(), lines);
    }
 
    private void saveScores() {
        List<String> lines = new ArrayList<>();
        for (Student s : studentRepo.findAll()) {
            for (Score sc : s.viewScoreHistory()) {
                lines.add(s.getId() + SEP + sc.getQuiz().getId() + SEP + sc.getValue());
            }
        }
        store.writeLines(store.getScoresFile(), lines);
    }
 
    private int safe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }
}
