package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.Quiz;
import java.util.List;


public interface QuizRepository {


    void save(Quiz quiz);


    List<Quiz> findAll();


    Quiz findById(String id);


    boolean delete(String id);


    List<Quiz> findByCourse(String course);
    
    List<Quiz> findByTeacherId(String teacherId);


    int count();
}
