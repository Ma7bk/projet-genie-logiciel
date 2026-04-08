package fr.uparis.projet_genie_logiciel.repository;
import fr.uparis.projet_genie_logiciel.entity.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class InMemoryQuestionRepository implements QuestionRepository {
    private final List<Question> questions = new ArrayList<>();
    public void save(Question q) {
        if (q == null) { throw new IllegalArgumentException("Question null"); }
        delete(q.getId());
        questions.add(q);
    }
    public List<Question> findAll() { return new ArrayList<>(questions); }
    public Question findById(String id) {
        if (id == null || id.trim().isEmpty()) { return null; }
        return questions.stream().filter(q -> q.getId().equals(id)).findFirst().orElse(null);
    }
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) { return false; }
        return questions.removeIf(q -> q.getId().equals(id));
    }
    public List<Question> findByCourse(String course) {
        if (course == null || course.trim().isEmpty()) { return new ArrayList<>(); }
        return questions.stream().filter(q -> q.getCourse().equalsIgnoreCase(course)).collect(Collectors.toList());
    }
    public int count() { return questions.size(); }
}
