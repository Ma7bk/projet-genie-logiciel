package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.Quiz;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class InMemoryQuizRepository implements QuizRepository {
    
    private final List<Quiz> quizzes;
    
    public InMemoryQuizRepository() {
        this.quizzes = new ArrayList<>();
    }
    
    @Override
    public void save(Quiz quiz) {
        if (quiz == null) {
            throw new IllegalArgumentException("Le quiz ne peut pas être null");
        }
        
        Quiz existing = findById(quiz.getId());
        if (existing != null) {
            delete(quiz.getId());
        }
        
        quizzes.add(quiz);
    }
    
    @Override
    public List<Quiz> findAll() {
        return new ArrayList<>(quizzes);
    }
    
    @Override
    public Quiz findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        return quizzes.stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        return quizzes.removeIf(q -> q.getId().equals(id));
    }
    
    @Override
    public List<Quiz> findByCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return quizzes.stream()
                .filter(q -> q.getCourse().equalsIgnoreCase(course))
                .collect(Collectors.toList());
    }
    
    @Override
    public int count() {
        return quizzes.size();
    }
}
