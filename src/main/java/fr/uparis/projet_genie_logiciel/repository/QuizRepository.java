package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.Quiz;
import java.util.List;

/*
  cet interface définit les opérations pour gérer les quiz.
  Elle sert d'intermédiaire entre l'application et le stockage des données.
 */
public interface QuizRepository {

    // Enregistre un quiz dans le système
    void save(Quiz quiz);

    // Retourne la liste de tous les quiz
    List<Quiz> findAll();

    // Cherche un quiz à partir de son identifiant
    Quiz findById(String id);

    // Supprime un quiz grâce à son identifiant
    boolean delete(String id);

    // Récupère les quiz associés à un cours donné
    List<Quiz> findByCourse(String course);

    // Retourne le nombre total de quiz
    int count();
}
