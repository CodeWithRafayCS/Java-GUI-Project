package models;

import java.io.Serializable;
import java.util.ArrayList;

import interfaces.Gradable;
import interfaces.Reportable;

public class Student extends Person implements Serializable,Gradable,Reportable {
    private static final long serialVersionUID = 1L;
    
    private String fullName;
    private int semester;
    private ArrayList<QuizResult> quizHistory;
    private boolean isActive;
    
    // SINGLE CONSTRUCTOR - removed email and hashedPassword
    public Student(String registrationNumber, String fullName, int semester) {
        super(fullName, registrationNumber);
        this.fullName = fullName;
        this.semester = semester;
        this.quizHistory = new ArrayList<>();
        this.isActive = true;
    }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; this.name = fullName; }
    
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    
    public ArrayList<QuizResult> getQuizHistory() { return quizHistory; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public void addQuizResult(QuizResult result) {
        quizHistory.add(result);
    }
    
    @Override
    public String getRole() { return "Student"; }
    
    @Override
    public double calculateScore() {
        if (quizHistory.isEmpty()) return 0;
        double total = 0;
        for (QuizResult r : quizHistory) {
            total += r.getScorePercentage();
        }
        return total / quizHistory.size();
    }

    @Override
    public String getGrade() {
        double avg = calculateScore();
        if (avg >= 90) return "A+";
        if (avg >= 80) return "A";
        if (avg >= 75) return "B+";
        if (avg >= 70) return "B";
        if (avg >= 60) return "C";
        if (avg >= 50) return "D";
        return "F";
    }

    @Override
    public boolean isPassed() { return calculateScore() >= 50; }

    @Override
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== STUDENT QUIZ REPORT ==========\n");
        sb.append("Name: ").append(fullName).append("\n");
        sb.append("Registration: ").append(registrationNumber).append("\n");
        sb.append("Semester: ").append(semester).append("\n");
        sb.append("Total Quizzes: ").append(quizHistory.size()).append("\n");
        sb.append("Average Score: ").append(String.format("%.2f", calculateScore())).append("%\n");
        sb.append("Grade: ").append(getGrade()).append("\n");
        sb.append("Status: ").append(isPassed() ? "PASSING" : "FAILING").append("\n");
        sb.append("----------------------------------------\n");
        for (int i = 0; i < quizHistory.size(); i++) {
            sb.append(i+1).append(". ").append(quizHistory.get(i).toString()).append("\n");
        }
        sb.append("========================================\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return fullName + " (" + registrationNumber + ") - Semester " + semester;
    }
}