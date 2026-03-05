package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.Question;
import java.util.List;

/**
 * Interface du repository pour les questions 
 */
public interface QuestionRepository {
    
    void save(Question question);
    
    List<Question> findAll();
    
    Question findById(String id);
    
    boolean delete(String id);
    
    List<Question> findByCourse(String course);
    
    int count();
}
