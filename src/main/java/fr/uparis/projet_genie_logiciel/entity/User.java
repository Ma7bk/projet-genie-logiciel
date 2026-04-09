package fr.uparis.projet_genie_logiciel.entity;


public abstract class User {
    private final String id;
    private final String hashedPassword;

    public User(String id, String password) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID ne peut pas etre vide");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas etre vide");
        }
        this.id = id.trim();
        this.hashedPassword = PasswordUtil.hash(password);
    }

    protected User(String id, String hashedPassword, boolean alreadyHashed) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID ne peut pas etre vide");
        }
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le hash ne peut pas etre vide");
        }
        this.id = id.trim();
        this.hashedPassword = hashedPassword.trim();
    }  
    
    public String getId() { return id; }


    public boolean checkPassword(String plainPassword) {
        return PasswordUtil.verify(plainPassword, hashedPassword);
    }


    String getHashedPassword() { return hashedPassword; }
    
}
