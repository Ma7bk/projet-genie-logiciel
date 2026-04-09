package fr.uparis.projet_genie_logiciel.persistence;

public class AppContext {
    private int teacherCount = 0;
    private int studentCount = 0;
    private int quizCount = 0;
    private int questionCount = 0;

    public int getTeacherCount() { return teacherCount; }
    public int getStudentCount() { return studentCount; }
    public int getQuizCount() { return quizCount; }
    public int getQuestionCount() { return questionCount; }

    public void setTeacherCount(int n) { this.teacherCount = n; }
    public void setStudentCount(int n) { this.studentCount = n; }
    public void setQuizCount(int n) { this.quizCount = n; }
    public void setQuestionCount(int n) { this.questionCount = n; }

    public String nextTeacherId() { return "T" + (++teacherCount); }
    public void cancelTeacherId() { teacherCount--; }
    public String nextStudentId() { return "S" + (++studentCount); }
    public void cancelStudentId() { studentCount--; }
    public String nextQuizId() { return "Q" + (++quizCount); }
    public void cancelQuizId() { quizCount--; }
    public String nextQuestionId() { return "QU" + (++questionCount); }
    public void cancelQuestionId() { questionCount--; }
}
