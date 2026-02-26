package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.Teacher;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class InMemoryTeacherRepository implements TeacherRepository {
    
    private final List<Teacher> teachers;
    
    public InMemoryTeacherRepository() {
        this.teachers = new ArrayList<>();
    }
    
    @Override
    public void save(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("L'enseignant ne peut pas être null");
        }
        
        Teacher existing = findById(teacher.getId());
        if (existing != null) {
            delete(teacher.getId());
        }
        
        teachers.add(teacher);
    }
    
    @Override
    public List<Teacher> findAll() {
        return new ArrayList<>(teachers);
    }
    
    @Override
    public Teacher findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        return teachers.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        return teachers.removeIf(t -> t.getId().equals(id));
    }
    
    @Override
    public Teacher findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        
        return teachers.stream()
                .filter(t -> t.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Teacher> findBySubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return teachers.stream()
                .filter(t -> t.getSubject().equalsIgnoreCase(subject))
                .collect(Collectors.toList());
    }
    
    @Override
    public int count() {
        return teachers.size();
    }
}
