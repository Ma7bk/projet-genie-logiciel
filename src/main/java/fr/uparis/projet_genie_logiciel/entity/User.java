package fr.uparis.projet_genie_logiciel.entity;


public abstract class User {
    private final String id;

    public User(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de l'utilisateur ne peut pas être vide");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
