package fr.uparis.projet_genie_logiciel.service;

import fr.uparis.projet_genie_logiciel.entity.*;
import fr.uparis.projet_genie_logiciel.repository.QuizRepository;
import fr.uparis.projet_genie_logiciel.repository.QuestionRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


public class QuizService {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository) {
        if (quizRepository == null || questionRepository == null) {
            throw new IllegalArgumentException("Les repositories ne peuvent pas être null");
        }
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    
    public Quiz createQuizByTeacher(Teacher teacher, String id, String title, String course, int duration) {
        if (teacher == null) throw new IllegalArgumentException("L'enseignant ne peut pas être null");
        if (quizRepository.findById(id) != null) {
            throw new IllegalStateException("Un quiz avec l'ID '" + id + "' existe déjà");
        }
        Quiz quiz = teacher.createQuiz(id, title, course, duration);
        quizRepository.save(quiz);
        return quiz;
    }

    
    public Score takeQuizByStudent(Student student, Quiz quiz, List<Choice> answers) {
        if (student == null || quiz == null || answers == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }

        student.startQuiz(quiz);
        Score score = new Score(quiz);
        List<Question> questions = quiz.getQuestions();

        for (int i = 0; i < Math.min(questions.size(), answers.size()); i++) {
            Question q = questions.get(i);
            Choice c = answers.get(i);
            student.submitAnswer(q, c);
            if (q.checkAnswer(c)) {
                score.addPoint();
            }
        }

        student.addScoreToHistory(score);
        student.viewScore(score);
        return score;
    }

    // Méthodes TP5
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

    public int getTotalQuizCount() {
        return quizRepository.count();
    }
}
