package fr.uparis.projet_genie_logiciel.service;
import fr.uparis.projet_genie_logiciel.entity.Teacher;
import fr.uparis.projet_genie_logiciel.repository.TeacherRepository;
import java.util.List;
public class TeacherService {
    private final TeacherRepository repo;
    public TeacherService(TeacherRepository repo) {
        if (repo == null) { throw new IllegalArgumentException("Repository null"); }
        this.repo = repo;
    }
    public void createTeacher(String id, String firstName, String lastName,
                               String email, String subject, String password) {
        if (repo.findById(id) != null) {
            throw new IllegalStateException("Enseignant avec ID '" + id + "' existe deja");
        }
        if (repo.findByEmail(email) != null) {
            throw new IllegalStateException("Enseignant avec email '" + email + "' existe deja");
        }
        repo.save(new Teacher(id, firstName, lastName, email, subject, password));
    }
    public List<Teacher> getAllTeachers() { return repo.findAll(); }
    public Teacher getTeacherById(String id) {
        Teacher t = repo.findById(id);
        if (t == null) { throw new IllegalArgumentException("Enseignant non trouve : " + id); }
        return t;
    }
    public Teacher getTeacherByEmail(String email) {
        Teacher t = repo.findByEmail(email);
        if (t == null) { throw new IllegalArgumentException("Enseignant non trouve : " + email); }
        return t;
    }
    public List<Teacher> getTeachersBySubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("La matiere ne peut pas etre vide");
        }
        return repo.findBySubject(subject);
    }
    public void deleteTeacher(String id) {
        if (repo.findById(id) == null) {
            throw new IllegalArgumentException("Enseignant non trouve : " + id);
        }
        repo.delete(id);
    }
    public int getTotalTeacherCount() { return repo.count(); }
}
