package fr.uparis.projet_genie_logiciel.service;
import fr.uparis.projet_genie_logiciel.entity.Choice;
import fr.uparis.projet_genie_logiciel.entity.Question;
import fr.uparis.projet_genie_logiciel.entity.Quiz;
import fr.uparis.projet_genie_logiciel.entity.Score;
import fr.uparis.projet_genie_logiciel.entity.Student;
import fr.uparis.projet_genie_logiciel.entity.Teacher;
import fr.uparis.projet_genie_logiciel.repository.QuestionRepository;
import fr.uparis.projet_genie_logiciel.repository.QuizRepository;
import java.util.List;
public class QuizService {
    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    public QuizService(QuizRepository quizRepo, QuestionRepository questionRepo) {
        if (quizRepo == null || questionRepo == null) {
            throw new IllegalArgumentException("Repositories null");
        }
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;
    }
    public Quiz createQuizByTeacher(Teacher teacher, String id, String title, String course, int duration) {
        if (teacher == null) { throw new IllegalArgumentException("Enseignant null"); }
        if (quizRepo.findById(id) != null) {
            throw new IllegalStateException("Quiz avec ID '" + id + "' existe deja");
        }
        Quiz quiz = teacher.createQuiz(id, title, course, duration);
        quizRepo.save(quiz);
        return quiz;
    }
    public Score takeQuizByStudent(Student student, Quiz quiz, List<Choice> answers) {
        if (student == null || quiz == null || answers == null) {
            throw new IllegalArgumentException("Parametres null");
        }
        student.startQuiz(quiz);
        Score score = new Score(quiz);
        List<Question> questions = quiz.getQuestions();
        for (int i = 0; i < Math.min(questions.size(), answers.size()); i++) {
            Question q = questions.get(i);
            Choice c = answers.get(i);
            student.submitAnswer(q, c);
            if (q.checkAnswer(c)) { score.addPoint(); }
        }
        student.addScoreToHistory(score);
        return score;
    }
    public List<Quiz> getAllQuizzes() { return quizRepo.findAll(); }
    public List<Quiz> getQuizzesByTeacher(String teacherId) { return quizRepo.findByTeacherId(teacherId); }
    public Quiz getQuizById(String id) {
        Quiz q = quizRepo.findById(id);
        if (q == null) { throw new IllegalArgumentException("Quiz non trouve : " + id); }
        return q;
    }
    public void deleteQuiz(String id) {
        if (quizRepo.findById(id) == null) {
            throw new IllegalArgumentException("Quiz non trouve : " + id);
        }
        quizRepo.delete(id);
    }
    public List<Quiz> getQuizzesByCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            throw new IllegalArgumentException("Le cours ne peut pas etre vide");
        }
        return quizRepo.findByCourse(course);
    }
    public int getTotalQuizCount() { return quizRepo.count(); }
}
