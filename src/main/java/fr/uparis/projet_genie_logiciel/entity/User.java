package fr.uparis.projet_genie_logiciel.entity;


public abstract class User {
    private final String id;
    private final String password;

    public User(String id, String password) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID ne peut pas etre vide");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas etre vide");
        }
        this.id = id.trim();
        this.password = password.trim();
    }

    public String getId() { return id; }
    public String getPassword() { return password; }

    public boolean checkPassword(String pwd) {
        return pwd != null && this.password.equals(pwd.trim());
    }
}
