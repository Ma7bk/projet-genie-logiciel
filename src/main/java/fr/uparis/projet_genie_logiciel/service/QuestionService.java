package fr.uparis.projet_genie_logiciel.service;
import fr.uparis.projet_genie_logiciel.entity.Choice;
import fr.uparis.projet_genie_logiciel.entity.QCMQuestion;
import fr.uparis.projet_genie_logiciel.entity.Question;
import fr.uparis.projet_genie_logiciel.entity.TrueFalseQuestion;
import fr.uparis.projet_genie_logiciel.repository.QuestionRepository;
import java.util.List;
public class QuestionService {
    private final QuestionRepository repo;
    public QuestionService(QuestionRepository repo) {
        if (repo == null) { throw new IllegalArgumentException("Repository null"); }
        this.repo = repo;
    }
    public QCMQuestion createQCMQuestion(String id, String text, String course, List<Choice> choices) {
        if (repo.findById(id) != null) {
            throw new IllegalStateException("Question avec ID '" + id + "' existe deja");
        }
        QCMQuestion q = new QCMQuestion(id, text, course);
        for (Choice c : choices) { q.addChoice(c); }
        q.validateChoices(); 
        repo.save(q);
        return q;
    }
    public TrueFalseQuestion createTrueFalseQuestion(String id, String text, String course, boolean isTrue) {
        if (repo.findById(id) != null) {
            throw new IllegalStateException("Question avec ID '" + id + "' existe deja");
        }
        TrueFalseQuestion q = new TrueFalseQuestion(id, text, course, isTrue);
        repo.save(q);
        return q;
    }
    public List<Question> getAllQuestions() { return repo.findAll(); }
    public Question getQuestionById(String id) {
        Question q = repo.findById(id);
        if (q == null) { throw new IllegalArgumentException("Question non trouvee : " + id); }
        return q;
    }
    public void deleteQuestion(String id) {
        if (repo.findById(id) == null) {
            throw new IllegalArgumentException("Question non trouvee : " + id);
        }
        repo.delete(id);
    }
    public List<Question> getQuestionsByCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            throw new IllegalArgumentException("Le cours ne peut pas etre vide");
        }
        return repo.findByCourse(course);
    }
    public int getTotalQuestionCount() { return repo.count(); }
}
