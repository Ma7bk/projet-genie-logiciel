package fr.uparis.projet_genie_logiciel.service;

import fr.uparis.projet_genie_logiciel.entity.Question;
import fr.uparis.projet_genie_logiciel.entity.Quiz;
import fr.uparis.projet_genie_logiciel.repository.QuestionRepository;
import fr.uparis.projet_genie_logiciel.repository.QuizRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des quiz

 */
public class QuizService {
    
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    
   
    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository) {
        if (quizRepository == null) {
            throw new IllegalArgumentException("Le quizRepository ne peut pas être null");
        }
        if (questionRepository == null) {
            throw new IllegalArgumentException("Le questionRepository ne peut pas être null");
        }
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }
    
    
    public void createQuiz(String id, String title, String course, 
                          List<String> questionIds, int duration) {

        if (quizRepository.findById(id) != null) {
            throw new IllegalStateException("Un quiz avec l'ID '" + id + "' existe déjà");
        }
        

        for (String questionId : questionIds) {
            if (questionRepository.findById(questionId) == null) {
                throw new IllegalArgumentException("La question avec l'ID '" + questionId + "' n'existe pas");
            }
        }
        

        List<Question> questions = getQuestionsForQuiz(questionIds);
        boolean allSameCourse = questions.stream()
                .allMatch(q -> q.getCourse().equalsIgnoreCase(course));
        
        if (!allSameCourse) {
            throw new IllegalArgumentException("Toutes les questions doivent appartenir au cours : " + course);
        }
        

        Quiz quiz = new Quiz(id, title, course, questionIds, duration);
        quizRepository.save(quiz);
    }
    
   
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }
    
    
    public Quiz getQuizById(String id) {
        Quiz quiz = quizRepository.findById(id);
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz non trouvé avec l'ID : " + id);
        }
        return quiz;
    }
    
    
    public void deleteQuiz(String id) {
        if (quizRepository.findById(id) == null) {
            throw new IllegalArgumentException("Impossible de supprimer : quiz non trouvé avec l'ID " + id);
        }
        quizRepository.delete(id);
    }
    
    
    public List<Quiz> getQuizzesByCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du cours ne peut pas être vide");
        }
        return quizRepository.findByCourse(course);
    }
    
    
    public List<Question> getQuestionsForQuiz(String quizId) {
        Quiz quiz = getQuizById(quizId);
        return getQuestionsForQuiz(quiz.getQuestionIds());
    }
    
    
    private List<Question> getQuestionsForQuiz(List<String> questionIds) {
        List<Question> questions = new ArrayList<>();
        for (String questionId : questionIds) {
            Question question = questionRepository.findById(questionId);
            if (question != null) {
                questions.add(question);
            }
        }
        return questions;
    }
    
    
    public double calculateScore(String quizId, java.util.Map<String, String> answers) {
        List<Question> questions = getQuestionsForQuiz(quizId);
        
        if (questions.isEmpty()) {
            return 0.0;
        }
        
        int correctAnswers = 0;
        for (Question question : questions) {
            String studentAnswer = answers.get(question.getId());
            if (studentAnswer != null && question.isCorrectAnswer(studentAnswer)) {
                correctAnswers++;
            }
        }
        
        return (correctAnswers * 100.0) / questions.size();
    }
    
    
    public void addQuestionToQuiz(String quizId, String questionId) {
        Quiz quiz = getQuizById(quizId);
        

        Question question = questionRepository.findById(questionId);
        if (question == null) {
            throw new IllegalArgumentException("La question avec l'ID '" + questionId + "' n'existe pas");
        }
        

        if (!question.getCourse().equalsIgnoreCase(quiz.getCourse())) {
            throw new IllegalArgumentException("La question doit appartenir au cours : " + quiz.getCourse());
        }
        

        if (quiz.getQuestionIds().contains(questionId)) {
            throw new IllegalStateException("La question est déjà dans ce quiz");
        }
        

        List<String> newQuestionIds = new ArrayList<>(quiz.getQuestionIds());
        newQuestionIds.add(questionId);
        
        Quiz updatedQuiz = new Quiz(quiz.getId(), quiz.getTitle(), quiz.getCourse(), 
                                    newQuestionIds, quiz.getDuration());
        quizRepository.save(updatedQuiz);
    }
    
    
    public int getTotalQuizCount() {
        return quizRepository.count();
    }
}
