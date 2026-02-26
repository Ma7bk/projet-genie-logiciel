package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class InMemoryStudentRepository implements StudentRepository {
    
    private final List<Student> students;
    
    public InMemoryStudentRepository() {
        this.students = new ArrayList<>();
    }
    
    @Override
    public void save(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("L'étudiant ne peut pas être null");
        }
        
        Student existing = findById(student.getId());
        if (existing != null) {
            delete(student.getId());
        }
        
        students.add(student);
    }
    
    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students);
    }
    
    @Override
    public Student findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        return students.removeIf(s -> s.getId().equals(id));
    }
    
    @Override
    public Student findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        
        return students.stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Student> findByClasse(String classe) {
        if (classe == null || classe.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return students.stream()
                .filter(s -> s.getClasse().equalsIgnoreCase(classe))
                .collect(Collectors.toList());
    }
    
    @Override
    public int count() {
        return students.size();
    }
}
