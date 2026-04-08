package fr.uparis.projet_genie_logiciel.service;
import fr.uparis.projet_genie_logiciel.entity.Student;
import fr.uparis.projet_genie_logiciel.entity.Teacher;
import fr.uparis.projet_genie_logiciel.repository.StudentRepository;
import fr.uparis.projet_genie_logiciel.repository.TeacherRepository;

public class AuthService {
    private final TeacherRepository teacherRepo;
    private final StudentRepository studentRepo;
    public AuthService(TeacherRepository teacherRepo, StudentRepository studentRepo) {
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
    }
    public Teacher loginTeacher(String email, String password) {
        if (email == null || password == null) { return null; }
        Teacher t = teacherRepo.findByEmail(email.trim().toLowerCase());
        if (t != null && t.checkPassword(password)) { return t; }
        return null;
    }
    public Student loginStudent(String email, String password) {
        if (email == null || password == null) { return null; }
        Student s = studentRepo.findByEmail(email.trim().toLowerCase());
        if (s != null && s.checkPassword(password)) { return s; }
        return null;
    }
}
