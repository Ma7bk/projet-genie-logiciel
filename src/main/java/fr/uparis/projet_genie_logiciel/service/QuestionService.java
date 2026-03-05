package fr.uparis.projet_genie_logiciel.service;

import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.repository.QuestionRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        if (questionRepository == null) {
            throw new IllegalArgumentException("Le repository ne peut pas être null");
        }
        this.questionRepository = questionRepository;
    }

    
    public QCMQuestion createQCMQuestion(String id, String text, String course, List<Choice> choices) {
        if (questionRepository.findById(id) != null) {
            throw new IllegalStateException("Une question avec l'ID '" + id + "' existe déjà");
        }
        QCMQuestion question = new QCMQuestion(id, text, course);
        for (Choice choice : choices) {
            question.addChoice(choice);
        }
        questionRepository.save(question);
        return question;
    }

    
    public TrueFalseQuestion createTrueFalseQuestion(String id, String text, String course, boolean isTrue) {
        if (questionRepository.findById(id) != null) {
            throw new IllegalStateException("Une question avec l'ID '" + id + "' existe déjà");
        }
        TrueFalseQuestion question = new TrueFalseQuestion(id, text, course, isTrue);
        questionRepository.save(question);
        return question;
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

    public int getTotalQuestionCount() {
        return questionRepository.count();
    }
}
