package fr.uparis.projet_genie_logiciel.repository;
import fr.uparis.projet_genie_logiciel.entity.Quiz;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class InMemoryQuizRepository implements QuizRepository {
    private final List<Quiz> quizzes = new ArrayList<>();
    public void save(Quiz q) {
        if (q == null) { throw new IllegalArgumentException("Quiz null"); }
        delete(q.getId());
        quizzes.add(q);
    }
    public List<Quiz> findAll() { return new ArrayList<>(quizzes); }
    public Quiz findById(String id) {
        if (id == null || id.trim().isEmpty()) { return null; }
        return quizzes.stream().filter(q -> q.getId().equals(id)).findFirst().orElse(null);
    }
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) { return false; }
        return quizzes.removeIf(q -> q.getId().equals(id));
    }
    public List<Quiz> findByCourse(String course) {
        if (course == null || course.trim().isEmpty()) { return new ArrayList<>(); }
        return quizzes.stream().filter(q -> q.getCourse().equalsIgnoreCase(course)).collect(Collectors.toList());
    }
    public List<Quiz> findByTeacherId(String teacherId) {
        if (teacherId == null || teacherId.trim().isEmpty()) { return new ArrayList<>(); }
        return quizzes.stream().filter(q -> q.getTeacherId().equals(teacherId)).collect(Collectors.toList());
    }
    public int count() { return quizzes.size(); }
}
