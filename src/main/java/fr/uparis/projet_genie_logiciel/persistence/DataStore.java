package fr.uparis.projet_genie_logiciel.persistence;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static final String TEACHERS_FILE  = "teachers.txt";
    private static final String STUDENTS_FILE  = "students.txt";
    private static final String QUIZZES_FILE   = "quizzes.txt";
    private static final String QUESTIONS_FILE = "questions.txt";
    private static final String SCORES_FILE    = "scores.txt";
    private static final String COUNTERS_FILE  = "counters.txt";

    public void writeLines(String filename, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) { bw.write(line); bw.newLine(); }
        } catch (IOException e) {
            System.out.println("AVERTISSEMENT sauvegarde " + filename + " : " + e.getMessage());
        }
    }

    public List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) { return lines; }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) { lines.add(line.trim()); }
            }
        } catch (IOException e) {
            System.out.println("AVERTISSEMENT chargement " + filename + " : " + e.getMessage());
        }
        return lines;
    }

    public String getTeachersFile()  { return TEACHERS_FILE; }
    public String getStudentsFile()  { return STUDENTS_FILE; }
    public String getQuizzesFile()   { return QUIZZES_FILE; }
    public String getQuestionsFile() { return QUESTIONS_FILE; }
    public String getScoresFile()    { return SCORES_FILE; }
    public String getCountersFile()  { return COUNTERS_FILE; }
}
