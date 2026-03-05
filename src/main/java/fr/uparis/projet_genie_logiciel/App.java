package fr.uparis.projet_genie_logiciel;

import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.repository.*;
import fr.uparis.projet_genie_logiciel.service.*;

import java.util.ArrayList;
import java.util.List;


public class App {
    public static void main(String[] args) {
        System.out.println("DÉMARRAGE DES TESTS DU SYSTÈMEn");


        QuestionRepository questionRepository = new InMemoryQuestionRepository();
        QuizRepository quizRepository = new InMemoryQuizRepository();
        StudentRepository studentRepository = new InMemoryStudentRepository();
        TeacherRepository teacherRepository = new InMemoryTeacherRepository();

        QuestionService questionService = new QuestionService(questionRepository);
        QuizService quizService = new QuizService(quizRepository, questionRepository);
        StudentService studentService = new StudentService(studentRepository);
        TeacherService teacherService = new TeacherService(teacherRepository);



        teacherService.createTeacher("T1", "Marie", "Dubois", "marie.dubois@u-paris.fr", "Génie Logiciel");
        studentService.createStudent("S1", "Jean", "Dupont", "jean.dupont@u-paris.fr", "2A SIE");
        
        Teacher teacher = teacherService.getTeacherById("T1");
        Student student = studentService.getStudentById("S1");
        System.out.println("✓ Utilisateurs créés : Teacher Marie Dubois et Student Jean Dupont\n");


        System.out.println("--- Action du Teacher : Création d'un quiz ---");
        Quiz quiz = quizService.createQuizByTeacher(teacher, "Q1", "Quiz Architecture", "Génie Logiciel", 30);
        System.out.println("✓ Quiz créé : " + quiz.getTitle());


        System.out.println("\n--- Action du Teacher : Ajout de questions ---");
        

        List<Choice> choices = new ArrayList<>();
        Choice correctChoice = new Choice("Couche présentation", true);
        choices.add(new Choice("Couche métier", false));
        choices.add(correctChoice);
        choices.add(new Choice("Couche persistance", false));
        
        QCMQuestion q1 = questionService.createQCMQuestion("QU1", "Quelle couche gère l'affichage ?", "Génie Logiciel", choices);
        teacher.addQuestion(quiz, q1);
        teacher.defineCorrectAnswer(correctChoice);
        System.out.println("✓ Question QCM ajoutée");


        TrueFalseQuestion q2 = questionService.createTrueFalseQuestion("QU2", "La loi de Déméter est un principe de conception", "Génie Logiciel", true);
        teacher.addQuestion(quiz, q2);
        System.out.println("✓ Question Vrai/Faux ajoutée");


        System.out.println("\n--- Action du Student : Passage du quiz ---");
        List<Choice> studentAnswers = new ArrayList<>();
        studentAnswers.add(correctChoice); 
        studentAnswers.add(q2.getChoices().get(0));
        Score finalScore = quizService.takeQuizByStudent(student, quiz, studentAnswers);
        
        // 6. Vérification de l'historique (UML)
        System.out.println("\n--- Vérification de l'historique des scores ---");
        List<Score> history = student.viewScoreHistory();
        for (Score s : history) {
            System.out.println("Historique : " + s.display());
        }

        
    }
}
