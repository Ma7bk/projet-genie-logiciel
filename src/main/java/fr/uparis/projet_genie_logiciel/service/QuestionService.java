package fr.uparis.projet_genie_logiciel.service;

import fr.uparis.projet_genie_logiciel.entity.Question;
import fr.uparis.projet_genie_logiciel.repository.QuestionRepository;
import java.util.List;
import java.util.Arrays;

/**
 * Service métier pour la gestion des questions
 */
public class QuestionService {
    
    private final QuestionRepository questionRepository;
    
    
    public QuestionService(QuestionRepository questionRepository) {
        if (questionRepository == null) {
            throw new IllegalArgumentException("Le repository ne peut pas être null");
        }
        this.questionRepository = questionRepository;
    }
    
    
    public void createQuestion(String id, String text, String type, String course,
                               List<String> options, String correctAnswer) {

        if (questionRepository.findById(id) != null) {
            throw new IllegalStateException("Une question avec l'ID '" + id + "' existe déjà");
        }
        

        Question question = new Question(id, text, type, course, options, correctAnswer);
        

        questionRepository.save(question);
    }
    
   
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
    
    
    public Question getQuestionById(String id) {
        Question question = questionRepository.findById(id);
        if (question == null) {
            throw new IllegalArgumentException("Question non trouvée avec l'ID : " + id);
        }
        return question;
    }
    
    
    public void deleteQuestion(String id) {
        if (questionRepository.findById(id) == null) {
            throw new IllegalArgumentException("Impossible de supprimer : question non trouvée avec l'ID " + id);
        }
        
        questionRepository.delete(id);
    }
    
    
    public List<Question> getQuestionsByCourse(String course) {
        if (course == null || course.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du cours ne peut pas être vide");
        }
        return questionRepository.findByCourse(course);
    }
    
    
    public List<Question> getQuestionsByType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type ne peut pas être vide");
        }
        return questionRepository.findByType(type);
    }
    
    
    public int getTotalQuestionCount() {
        return questionRepository.count();
    }
    
    
    public void createQCMQuestion(String id, String text, String course,
                                  String optionA, String optionB, String optionC, String optionD,
                                  String correctAnswer) {
        List<String> options = Arrays.asList(optionA, optionB, optionC, optionD);
        createQuestion(id, text, "QCM", course, options, correctAnswer);
    }
    
    
    public void createVraiFauxQuestion(String id, String text, String course, boolean isTrue) {
        List<String> options = Arrays.asList("Vrai", "Faux");
        String correctAnswer = isTrue ? "Vrai" : "Faux";
        createQuestion(id, text, "VRAI_FAUX", course, options, correctAnswer);
    }
    
    
    public boolean checkAnswer(String questionId, String answer) {
        Question question = getQuestionById(questionId);
        return question.isCorrectAnswer(answer);
    }
    
    
    public int countQuestionsByCourse(String course) {
        return getQuestionsByCourse(course).size();
    }
}
