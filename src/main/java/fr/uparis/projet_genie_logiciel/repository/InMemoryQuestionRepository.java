package fr.uparis.projet_genie_logiciel.repository;

package fr.uparis.projet_genie_logiciel.entity.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class InMemoryQuestionRepository implements QuestionRepository {
    

    private final List<Question> questions;
    
  
    public InMemoryQuestionRepository() {
        this.questions = new ArrayList<>();
    }
    
    @Override
    public void save(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("La question ne peut pas être null");
        }
        

        Question existing = findById(question.getId());
        if (existing != null) {

            delete(question.getId());
        }
        
        questions.add(question);
    }
    
    @Override
    public List<Question> findAll() {

        return new ArrayList<>(questions);
    }
    
    @Override
    public Question findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        return questions.stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        return questions.removeIf(q -> q.getId().equals(id));
    }
    
    @Override
    public List<Question> findByCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return questions.stream()
                .filter(q -> q.getCourse().equalsIgnoreCase(course))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Question> findByType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return questions.stream()
                .filter(q -> q.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }
    
    @Override
    public int count() {
        return questions.size();
    }
}
