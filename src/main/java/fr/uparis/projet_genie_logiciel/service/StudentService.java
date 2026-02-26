package fr.uparis.projet_genie_logiciel.service;

import fr.uparis.projet_genie_logiciel.entity.Student;
import fr.uparis.projet_genie_logiciel.repository.StudentRepository;
import java.util.List;

/**
 * Service métier pour la gestion des étudiants
 */
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    public StudentService(StudentRepository studentRepository) {
        if (studentRepository == null) {
            throw new IllegalArgumentException("Le repository ne peut pas être null");
        }
        this.studentRepository = studentRepository;
    }
    
    
    public void createStudent(String id, String firstName, String lastName, 
                             String email, String classe) {

        if (studentRepository.findById(id) != null) {
            throw new IllegalStateException("Un étudiant avec l'ID '" + id + "' existe déjà");
        }
        

        if (studentRepository.findByEmail(email) != null) {
            throw new IllegalStateException("Un étudiant avec l'email '" + email + "' existe déjà");
        }
        
        Student student = new Student(id, firstName, lastName, email, classe);
        studentRepository.save(student);
    }
    
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    
    public Student getStudentById(String id) {
        Student student = studentRepository.findById(id);
        if (student == null) {
            throw new IllegalArgumentException("Étudiant non trouvé avec l'ID : " + id);
        }
        return student;
    }
    
    
    public Student getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            throw new IllegalArgumentException("Étudiant non trouvé avec l'email : " + email);
        }
        return student;
    }
    
    
    public List<Student> getStudentsByClasse(String classe) {
        if (classe == null || classe.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la classe ne peut pas être vide");
        }
        return studentRepository.findByClasse(classe);
    }
    
    
    public void deleteStudent(String id) {
        if (studentRepository.findById(id) == null) {
            throw new IllegalArgumentException("Impossible de supprimer : étudiant non trouvé avec l'ID " + id);
        }
        studentRepository.delete(id);
    }
    
    
    public int getTotalStudentCount() {
        return studentRepository.count();
    }
    
    
    public int countStudentsByClasse(String classe) {
        return getStudentsByClasse(classe).size();
    }
}
