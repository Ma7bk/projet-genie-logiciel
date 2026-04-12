![CI](https://github.com/Ma7bk/projet-genie-logiciel/actions/workflows/ci.yml/badge.svg)

# Projet Génie Logiciel — Application Quiz

![Java](https://img.shields.io/badge/Java-11-orange)
![Maven](https://img.shields.io/badge/Maven-3.x-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-17.0.2-green)
![JUnit](https://img.shields.io/badge/JUnit-5.10.0-red)
![Coverage](https://img.shields.io/badge/Coverage-88%25-brightgreen)

## Équipe — Groupe 2 — Filière SIE

| Nom | Prénom |
|-----|--------|
| BEN KHALIFA | Mahmoud Aziz |
| ARGAB | Mohammed Amine |
| ZEGGUIR | Anouar |

**Formation :** EIDD 2ème année — SIE  
**Matière :** Génie Logiciel  
**Université :** Université Paris Cité

---

## Description

Application de gestion de quiz développée en Java avec Maven. Elle permet à des enseignants de créer des quiz avec des questions QCM ou Vrai/Faux, et à des étudiants de les passer et consulter leur historique de scores.

L'application propose deux interfaces :
- **CLI** — interface en ligne de commande
- **GUI** — interface graphique JavaFX (bonus)

---

## Fonctionnalités

### Enseignant
- Créer un compte et se connecter
- Créer des quiz (titre, cours, durée)
- Ajouter des questions QCM (2 à 6 choix, une seule bonne réponse)
- Ajouter des questions Vrai/Faux
- Voir uniquement ses propres quiz

### Étudiant
- Créer un compte et se connecter
- Passer un quiz (questions dans un ordre aléatoire — bonus)
- Voir son score à la fin du quiz
- Consulter son historique de scores

### Sécurité
- Les mots de passe sont **hachés avec SHA-256** avant stockage
- Aucun mot de passe n'est jamais stocké en clair
- Authentification par email + mot de passe

---

## Architecture

```
src/main/java/fr/uparis/projet_genie_logiciel/
│
├── App.java                          Point d'entrée CLI
│
├── entity/                           Entités métier
│   ├── User.java                     Classe abstraite (mot de passe hashé SHA-256)
│   ├── Teacher.java                  Enseignant
│   ├── Student.java                  Étudiant
│   ├── Quiz.java                     Quiz (lié à un enseignant)
│   ├── Question.java                 Classe abstraite question
│   ├── QCMQuestion.java              Question à choix multiples
│   ├── TrueFalseQuestion.java        Question Vrai/Faux (bonus)
│   ├── Choice.java                   Choix de réponse
│   ├── Score.java                    Score d'un étudiant
│   └── PasswordUtil.java             Hachage SHA-256
│
├── repository/                       Pattern Repository
│   ├── TeacherRepository.java        Interface
│   ├── StudentRepository.java        Interface
│   ├── QuizRepository.java           Interface
│   ├── QuestionRepository.java       Interface
│   ├── InMemoryTeacherRepository.java
│   ├── InMemoryStudentRepository.java
│   ├── InMemoryQuizRepository.java
│   └── InMemoryQuestionRepository.java
│
├── service/                          Logique métier
│   ├── TeacherService.java
│   ├── StudentService.java
│   ├── QuizService.java
│   ├── QuestionService.java
│   └── AuthService.java              Authentification
│
├── persistence/                      Persistance fichiers .txt
│   ├── DataStore.java                Lecture/écriture fichiers
│   ├── AppContext.java               Compteurs d'IDs
│   └── PersistenceManager.java       Sauvegarde/chargement
│
├── presentation/                     CLI — Pattern Command
│   ├── Command.java                  Interface Command
│   ├── CLI.java                      Affichage et saisie
│   ├── Menu.java                     Gestion des menus
│   └── commands/
│       ├── TeacherCommands.java
│       └── StudentCommands.java
│
└── gui/                              Interface graphique JavaFX (bonus)
    ├── MainApp.java                  Point d'entrée JavaFX
    ├── WelcomeView.java              Accueil : Espace Enseignant | Espace Étudiant
    ├── TeacherSpaceView.java         Connexion | Inscription enseignant
    ├── TeacherDashboardView.java     Dashboard enseignant connecté
    ├── StudentSpaceView.java         Connexion | Inscription étudiant
    └── StudentDashboardView.java     Dashboard étudiant connecté
```

---

## Design Patterns

| Pattern | Où | Description |
|---------|-----|-------------|
| **Repository** | `repository/` | Séparation persistance / logique métier. Obligatoire. |
| **Command** | `presentation/commands/` | Chaque action CLI est une commande encapsulée. |
| **Strategy** | `entity/` | `QCMQuestion` et `TrueFalseQuestion` implémentent différemment `checkAnswer()`. |

---

## Persistance

Les données sont sauvegardées dans des fichiers texte dans le répertoire courant :

| Fichier | Contenu |
|---------|---------|
| `teachers.txt` | `id\|prenom\|nom\|email\|matiere\|HASH_SHA256` |
| `students.txt` | `id\|prenom\|nom\|email\|classe\|HASH_SHA256` |
| `quizzes.txt` | `id\|titre\|cours\|duree\|teacherId` |
| `questions.txt` | `type\|id\|texte\|cours\|quizId\|choix:correct\|...` |
| `scores.txt` | `studentId\|quizId\|valeur` |
| `counters.txt` | `teacherCount\|studentCount\|quizCount\|questionCount` |

Les mots de passe sont stockés sous forme de **hash SHA-256** (64 caractères hexadécimaux), jamais en clair.

---

## Technologies

| Technologie | Version | Usage |
|-------------|---------|-------|
| Java | 11 | Langage principal |
| Maven | 3.x | Build et gestion des dépendances |
| JavaFX | 17.0.2 | Interface graphique (bonus) |
| JUnit Jupiter | 5.10.0 | Tests unitaires |
| Mockito | 5.11.0 | Mocks pour les tests |
| JaCoCo | 0.8.11 | Couverture de code |
| Checkstyle | 3.3.1 | Qualité du code |
| SonarQube Cloud | — | Analyse statique du code |

---

## Lancer l'application

### Prérequis
- Java 11+
- Maven 3.x

### CLI
```bash
mvn compile exec:java
```

### Interface graphique (bonus)
```bash
mvn javafx:run
```



## Sécurité

- **SHA-256** : tous les mots de passe sont hachés avec `PasswordUtil.hash()`
- **Pas d'exposition** : aucune méthode `getPassword()` publique — seul `checkPassword()` est accessible
- **Persistance sécurisée** : les fichiers `.txt` contiennent uniquement les hashes
- **Validation** : tous les constructeurs valident leurs paramètres avec `IllegalArgumentException`
- **Copies défensives** : `getChoices()`, `getQuestions()`, `viewScoreHistory()` retournent des copies

---

## Navigation GUI

```
WelcomeView
├── Espace Enseignant → TeacherSpaceView
│     ├── Se connecter  → TeacherDashboardView
│     │     ├── Créer un quiz
│     │     ├── Ajouter une question QCM
│     │     ├── Ajouter une question Vrai/Faux
│     │     └── Voir mes quiz
│     └── S'inscrire
└── Espace Étudiant → StudentSpaceView
      ├── Se connecter  → StudentDashboardView
      │     ├── Passer un quiz
      │     └── Voir mon historique de scores
      └── S'inscrire
```

---

## CI/CD

Le projet utilise GitHub Actions pour :
- Compilation automatique à chaque push
- Exécution des tests
- Analyse SonarQube Cloud

Configuration dans `.github/workflows/ci.yml`.
