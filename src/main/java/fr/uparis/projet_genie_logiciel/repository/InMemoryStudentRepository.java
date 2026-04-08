package fr.uparis.projet_genie_logiciel.repository;
import fr.uparis.projet_genie_logiciel.entity.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class InMemoryStudentRepository implements StudentRepository {
    private final List<Student> students = new ArrayList<>();
    public void save(Student s) {
        if (s == null) { throw new IllegalArgumentException("Student null"); }
        delete(s.getId());
        students.add(s);
    }
    public List<Student> findAll() { return new ArrayList<>(students); }
    public Student findById(String id) {
        if (id == null || id.trim().isEmpty()) { return null; }
        return students.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) { return false; }
        return students.removeIf(s -> s.getId().equals(id));
    }
    public Student findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) { return null; }
        return students.stream().filter(s -> s.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
    }
    public List<Student> findByClasse(String classe) {
        if (classe == null || classe.trim().isEmpty()) { return new ArrayList<>(); }
        return students.stream().filter(s -> s.getClasse().equalsIgnoreCase(classe)).collect(Collectors.toList());
    }
    public int count() { return students.size(); }
}
