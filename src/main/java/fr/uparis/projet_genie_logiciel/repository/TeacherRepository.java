package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.Teacher;
import java.util.List;


public interface TeacherRepository {
    
    void save(Teacher teacher);
    
    List<Teacher> findAll();
    
    Teacher findById(String id);
    
    boolean delete(String id);
    
    Teacher findByEmail(String email);
    
    List<Teacher> findBySubject(String subject);
    
    int count();
}
