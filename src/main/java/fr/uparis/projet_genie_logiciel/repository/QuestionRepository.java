package fr.uparis.projet_genie_logiciel.repository;

import fr.uparis.projet_genie_logiciel.entity.Question;
import java.util.List;

/**
 * Interface du repository pour les questions
 * Définit les opérations CRUD de base 
 */
public interface QuestionRepository {
    
    /**
     * Sauvegarde une question
     * @param question la question à sauvegarder
     */
    void save(Question question);
    
    /**
     * Récupère toutes les questions
     * @return liste de toutes les questions
     */
    List<Question> findAll();
    
    /**
     * Recherche une question par son ID
     * @param id l'ID de la question
     * @return la question trouvée ou null si non trouvée
     */
    Question findById(String id);
    
    /**
     * Supprime une question par son ID
     * @param id l'ID de la question à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    boolean delete(String id);
    
    /**
     * Recherche des questions par cours
     * @param course le nom du cours
     * @return liste des questions du cours
     */
    List<Question> findByCourse(String course);
    
    /**
     * Recherche des questions par type
     * @param type le type de question (QCM, VRAI_FAUX, TEXTE)
     * @return liste des questions du type spécifié
     */
    List<Question> findByType(String type);
    
    /**
     * Compte le nombre total de questions
     * @return le nombre de questions
     */
    int count();
}
