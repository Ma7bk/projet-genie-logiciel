package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.Quiz;
import java.util.List;

/**
 * Interface du repository pour les quiz
 * Définit les opérations CRUD de base 
 */
public interface QuizRepository {
    
    /**
     * Sauvegarde un quiz
     * @param quiz le quiz à sauvegarder
     */
    void save(Quiz quiz);
    
    /**
     * Récupère tous les quiz
     * @return liste de tous les quiz
     */
    List<Quiz> findAll();
    
    /**
     * Recherche un quiz par son ID
     * @param id l'ID du quiz
     * @return le quiz trouvé ou null si non trouvé
     */
    Quiz findById(String id);
    
    /**
     * Supprime un quiz par son ID
     * @param id l'ID du quiz à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    boolean delete(String id);
    
    /**
     * Recherche des quiz par cours
     * @param course le nom du cours
     * @return liste des quiz du cours
     */
    List<Quiz> findByCourse(String course);
    
    /**
     * Compte le nombre total de quiz
     * @return le nombre de quiz
     */
    int count();
}
