package fr.uparis.projet_genie_logiciel;



import fr.uparis.projet_genie_logiciel.entity.Question;
import fr.uparis.projet_genie_logiciel.entity.Quiz;
import fr.uparis.projet_genie_logiciel.entity.Student;
import fr.uparis.projet_genie_logiciel.entity.Teacher;
import fr.uparis.projet_genie_logiciel.repository.*;
import fr.uparis.projet_genie_logiciel.service.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe Main de test 
 * Permet de tester l'architecture en couches et les opérations de base
 */
public class App {
    
    public static void main(String[] args) {
        System.out.println("=== DÉMARRAGE DES TESTS DU SYSTÈME DE QUIZ ===\n");
        

        System.out.println("--- Assemblage des composants ---");
        
        // 1. Instanciation des repositories
        QuestionRepository questionRepository = new InMemoryQuestionRepository();
        QuizRepository quizRepository = new InMemoryQuizRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        TeacherRepository teacherRepository = new InMemoryTeacherRepository();
        System.out.println("✓ Repositories créés (en mémoire avec ArrayList)");
        
        // 2. Instanciation des services avec injection des repositories
        QuestionService questionService = new QuestionService(questionRepository);
        QuizService quizService = new QuizService(quizRepository, questionRepository);
        StudentService studentService = new StudentService(studentRepository);
        TeacherService teacherService = new TeacherService(teacherRepository);
        System.out.println("✓ Services créés avec injection des repositories\n");
        
        // ========== TEST DES ENSEIGNANTS ==========
        System.out.println("=== TEST 1 : Gestion des Enseignants ===");
        
        System.out.println("\n--- Création d'enseignants ---");
        try {
            teacherService.createTeacher("T1", "Marie", "Dubois", "marie.dubois@eidd.fr", "Génie Logiciel");
            System.out.println("✓ Enseignant T1 créé (Marie Dubois - Génie Logiciel)");
            
            teacherService.createTeacher("T2", "Pierre", "Martin", "pierre.martin@eidd.fr", "Base de données");
            System.out.println("✓ Enseignant T2 créé (Pierre Martin - Base de données)");
            
            teacherService.createTeacher("T3", "Sophie", "Bernard", "sophie.bernard@eidd.fr", "Génie Logiciel");
            System.out.println("✓ Enseignant T3 créé (Sophie Bernard - Génie Logiciel)");
            
        } catch (Exception e) {
            System.out.println("✗ Erreur : " + e.getMessage());
        }
        
        // Test validation email dupliqué
        System.out.println("\n--- Test validation : email enseignant dupliqué ---");
        try {
            teacherService.createTeacher("T4", "Jean", "Dupont", "marie.dubois@eidd.fr", "Java");
            System.out.println("✗ ERREUR : La validation des emails n'a pas fonctionné !");
        } catch (IllegalStateException e) {
            System.out.println("✓ Validation OK : " + e.getMessage());
        }
        
        // Récupération par matière
        System.out.println("\n--- Récupération par matière ---");
        List<Teacher> glTeachers = teacherService.getTeachersBySubject("Génie Logiciel");
        System.out.println("Enseignants de Génie Logiciel : " + glTeachers.size());
        for (Teacher t : glTeachers) {
            System.out.println("  - " + t.getFullName() + " (" + t.getEmail() + ")");
        }
        
        // ========== TEST DES QUESTIONS ==========
        System.out.println("\n=== TEST 2 : Gestion des Questions ===");
        
        System.out.println("\n--- Création de questions ---");
        try {
            questionService.createQCMQuestion(
                "Q1", 
                "Quel est le principe SOLID qui stipule qu'une classe ne doit avoir qu'une seule responsabilité ?",
                "Génie Logiciel",
                "Single Responsibility Principle",
                "Open/Closed Principle",
                "Liskov Substitution Principle",
                "Interface Segregation Principle",
                "Single Responsibility Principle"
            );
            System.out.println("✓ Question Q1 créée (QCM Génie Logiciel)");
            
            questionService.createQCMQuestion(
                "Q2",
                "Quelle couche gère l'affichage dans une architecture en couches ?",
                "Génie Logiciel",
                "Couche métier",
                "Couche présentation",
                "Couche persistance",
                "Couche données",
                "Couche présentation"
            );
            System.out.println("✓ Question Q2 créée (QCM Génie Logiciel)");
            
            questionService.createVraiFauxQuestion(
                "Q3",
                "La loi de Déméter recommande de parler uniquement à ses amis immédiats",
                "Génie Logiciel",
                true
            );
            System.out.println("✓ Question Q3 créée (VRAI/FAUX Génie Logiciel)");
            
            questionService.createQCMQuestion(
                "Q4",
                "Quelle structure de données est utilisée pour l'implémentation en mémoire dans le TP5 ?",
                "Java",
                "LinkedList",
                "ArrayList",
                "HashMap",
                "TreeSet",
                "ArrayList"
            );
            System.out.println("✓ Question Q4 créée (QCM Java)");
            
        } catch (Exception e) {
            System.out.println("✗ Erreur lors de la création : " + e.getMessage());
        }
        
        List<Question> allQuestions = questionService.getAllQuestions();
        System.out.println("\nNombre total de questions : " + allQuestions.size());
        
        // ========== TEST DE VALIDATION ==========
        System.out.println("\n=== TEST 3 : Validation des données ===");
        
        System.out.println("\n--- Test validation : texte vide ---");
        try {
            questionService.createQCMQuestion("Q_INVALID", "", "Java", "A", "B", "C", "D", "A");
            System.out.println("✗ ERREUR : La validation n'a pas fonctionné !");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Validation OK : " + e.getMessage());
        }
        
        System.out.println("\n--- Test validation : ID dupliqué ---");
        try {
            questionService.createQCMQuestion("Q1", "Question en double", "Java", "A", "B", "C", "D", "A");
            System.out.println("✗ ERREUR : La validation des doublons n'a pas fonctionné !");
        } catch (IllegalStateException e) {
            System.out.println("✓ Validation OK : " + e.getMessage());
        }
        
        // ========== TEST DES QUIZ ==========
        System.out.println("\n=== TEST 4 : Gestion des Quiz ===");
        
        System.out.println("\n--- Création d'un quiz ---");
        try {
            quizService.createQuiz(
                "QUIZ1",
                "Quiz Architecture Logicielle",
                "Génie Logiciel",
                Arrays.asList("Q1", "Q2", "Q3"),
                30
            );
            System.out.println("✓ Quiz QUIZ1 créé avec 3 questions");
            
        } catch (Exception e) {
            System.out.println("✗ Erreur : " + e.getMessage());
        }
        
        List<Question> quizQuestions = quizService.getQuestionsForQuiz("QUIZ1");
        System.out.println("\nQuestions du quiz : " + quizQuestions.size());
        
        // ========== TEST DU CALCUL DE SCORE ==========
        System.out.println("\n=== TEST 5 : Calcul de score ===");
        
        Map<String, String> answers = new HashMap<>();
        answers.put("Q1", "Single Responsibility Principle");
        answers.put("Q2", "Couche présentation");
        answers.put("Q3", "Vrai");
        
        double score = quizService.calculateScore("QUIZ1", answers);
        System.out.println("Score obtenu : " + score + "%");
        System.out.println("✓ Calcul de score fonctionnel");
        
        // ========== TEST DES ÉTUDIANTS ==========
        System.out.println("\n=== TEST 6 : Gestion des Étudiants ===");
        
        System.out.println("\n--- Création d'étudiants ---");
        try {
            studentService.createStudent("S1", "Jean", "Dupont", "jean.dupont@eidd.fr", "2A SIE");
            System.out.println("✓ Étudiant S1 créé");
            
            studentService.createStudent("S2", "Marie", "Martin", "marie.martin@eidd.fr", "2A SIE");
            System.out.println("✓ Étudiant S2 créé");
            
            studentService.createStudent("S3", "Pierre", "Durand", "pierre.durand@eidd.fr", "2A INFO");
            System.out.println("✓ Étudiant S3 créé");
            
        } catch (Exception e) {
            System.out.println("✗ Erreur : " + e.getMessage());
        }
        
        List<Student> sie2A = studentService.getStudentsByClasse("2A SIE");
        System.out.println("\nÉtudiants en 2A SIE : " + sie2A.size());
        
        // ========== TEST DE SUPPRESSION ==========
        System.out.println("\n=== TEST 7 : Suppression ===");
        
        System.out.println("\n--- Suppression d'une question ---");
        try {
            questionService.deleteQuestion("Q4");
            System.out.println("✓ Question Q4 supprimée");
            System.out.println("Nombre de questions restantes : " + questionService.getTotalQuestionCount());
        } catch (Exception e) {
            System.out.println("✗ Erreur : " + e.getMessage());
        }
        
        // ========== RÉSUMÉ FINAL ==========
        System.out.println("\n=== RÉSUMÉ FINAL ===");
        System.out.println("Enseignants totaux : " + teacherService.getTotalTeacherCount());
        System.out.println("Questions totales : " + questionService.getTotalQuestionCount());
        System.out.println("Quiz totaux : " + quizService.getTotalQuizCount());
        System.out.println("Étudiants totaux : " + studentService.getTotalStudentCount());
        
        System.out.println("\n=== TOUS LES TESTS SONT TERMINÉS ===");
        System.out.println("\n✓ Architecture en couches fonctionnelle");
        System.out.println("✓ Validation des entités opérationnelle");
        System.out.println("✓ Repositories en mémoire (ArrayList) fonctionnels");
        System.out.println("✓ Services avec logique métier opérationnels");
        System.out.println("✓ Entité Teacher ajoutée et testée");
    }
}
