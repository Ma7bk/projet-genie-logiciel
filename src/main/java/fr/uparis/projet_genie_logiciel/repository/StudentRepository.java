package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.quiz.entity.Student;
import java.util.List;

/**
 * Interface du repository pour les étudiants
 */
public interface StudentRepository {
    
    void save(Student student);
    
    List<Student> findAll();
    
    Student findById(String id);
    
    boolean delete(String id);
    
    Student findByEmail(String email);
    
    List<Student> findByClasse(String classe);
    
    int count();
}
