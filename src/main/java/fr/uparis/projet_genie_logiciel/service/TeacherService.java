package fr.uparis.projet_genie_logiciel.service;

import fr.uparis.projet_genie_logiciel.entity.Teacher;
import fr.uparis.projet_genie_logiciel.repository.TeacherRepository;
import java.util.List;


public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        if (teacherRepository == null) {
            throw new IllegalArgumentException("Le repository ne peut pas être null");
        }
        this.teacherRepository = teacherRepository;
    }

    
    public void createTeacher(String id, String firstName, String lastName, String email, String subject) {
        if (teacherRepository.findById(id) != null) {
            throw new IllegalStateException("Un enseignant avec l'ID '" + id + "' existe déjà");
        }
        if (teacherRepository.findByEmail(email) != null) {
            throw new IllegalStateException("Un enseignant avec l'email '" + email + "' existe déjà");
        }
        Teacher teacher = new Teacher(id, firstName, lastName, email, subject);
        teacherRepository.save(teacher);
    }


    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(String id) {
        Teacher teacher = teacherRepository.findById(id);
        if (teacher == null) {
            throw new IllegalArgumentException("Enseignant non trouvé avec l'ID : " + id);
        }
        return teacher;
    }

    public Teacher getTeacherByEmail(String email) {
        Teacher teacher = teacherRepository.findByEmail(email);
        if (teacher == null) {
            throw new IllegalArgumentException("Enseignant non trouvé avec l'email : " + email);
        }
        return teacher;
    }

    public List<Teacher> getTeachersBySubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("La matière ne peut pas être vide");
        }
        return teacherRepository.findBySubject(subject);
    }

    public void deleteTeacher(String id) {
        if (teacherRepository.findById(id) == null) {
            throw new IllegalArgumentException("Impossible de supprimer : enseignant non trouvé avec l'ID " + id);
        }
        teacherRepository.delete(id);
    }

    public int getTotalTeacherCount() {
        return teacherRepository.count();
    }
}
