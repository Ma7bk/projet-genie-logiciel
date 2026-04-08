package fr.uparis.projet_genie_logiciel.service;
import fr.uparis.projet_genie_logiciel.entity.Student;
import fr.uparis.projet_genie_logiciel.repository.StudentRepository;
import java.util.List;
public class StudentService {
    private final StudentRepository repo;
    public StudentService(StudentRepository repo) {
        if (repo == null) { throw new IllegalArgumentException("Repository null"); }
        this.repo = repo;
    }
    public void createStudent(String id, String firstName, String lastName,
                               String email, String classe, String password) {
        if (repo.findById(id) != null) {
            throw new IllegalStateException("Etudiant avec ID '" + id + "' existe deja");
        }
        if (repo.findByEmail(email) != null) {
            throw new IllegalStateException("Etudiant avec email '" + email + "' existe deja");
        }
        repo.save(new Student(id, firstName, lastName, email, classe, password));
    }
    public List<Student> getAllStudents() { return repo.findAll(); }
    public Student getStudentById(String id) {
        Student s = repo.findById(id);
        if (s == null) { throw new IllegalArgumentException("Etudiant non trouve : " + id); }
        return s;
    }
    public Student getStudentByEmail(String email) {
        Student s = repo.findByEmail(email);
        if (s == null) { throw new IllegalArgumentException("Etudiant non trouve : " + email); }
        return s;
    }
    public List<Student> getStudentsByClasse(String classe) {
        if (classe == null || classe.trim().isEmpty()) {
            throw new IllegalArgumentException("La classe ne peut pas etre vide");
        }
        return repo.findByClasse(classe);
    }
    public void deleteStudent(String id) {
        if (repo.findById(id) == null) {
            throw new IllegalArgumentException("Etudiant non trouve : " + id);
        }
        repo.delete(id);
    }
    public int getTotalStudentCount() { return repo.count(); }
}
