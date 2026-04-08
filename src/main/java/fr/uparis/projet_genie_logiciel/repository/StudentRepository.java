package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.Student;
import java.util.List;


public interface StudentRepository {
    
    void save(Student student);
    
    List<Student> findAll();
    
    Student findById(String id);
    
    boolean delete(String id);
    
    Student findByEmail(String email);
    
    List<Student> findByClasse(String classe);
    
    int count();
}
