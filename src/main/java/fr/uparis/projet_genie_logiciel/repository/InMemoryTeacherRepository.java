package fr.uparis.projet_genie_logiciel.repository;
import fr.uparis.projet_genie_logiciel.entity.Teacher;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class InMemoryTeacherRepository implements TeacherRepository {
    private final List<Teacher> teachers = new ArrayList<>();
    public void save(Teacher t) {
        if (t == null) { throw new IllegalArgumentException("Teacher null"); }
        delete(t.getId());
        teachers.add(t);
    }
    public List<Teacher> findAll() { return new ArrayList<>(teachers); }
    public Teacher findById(String id) {
        if (id == null || id.trim().isEmpty()) { return null; }
        return teachers.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) { return false; }
        return teachers.removeIf(t -> t.getId().equals(id));
    }
    public Teacher findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) { return null; }
        return teachers.stream().filter(t -> t.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
    }
    public List<Teacher> findBySubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) { return new ArrayList<>(); }
        return teachers.stream().filter(t -> t.getSubject().equalsIgnoreCase(subject)).collect(Collectors.toList());
    }
    public int count() { return teachers.size(); }
}
