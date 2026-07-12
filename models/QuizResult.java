package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QuizResult implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String studentRegNumber;
    private String studentName;
    private String subjectCode;
    private String subjectName;
    private String examMode;
    private String difficulty;
    private int totalQuestions;
    private int correctAnswers;
    private double scorePercentage;
    private LocalDateTime attemptDate;
    private boolean passed;
    private int timeTakenSeconds;
    private int hintsUsed;
    
    public QuizResult(String studentRegNumber, String studentName, String subjectCode,
                      String subjectName, String examMode, String difficulty,
                      int totalQuestions, int correctAnswers, int timeTakenSeconds) {
        this.studentRegNumber = studentRegNumber;
        this.studentName = studentName;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.examMode = examMode;
        this.difficulty = difficulty;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.timeTakenSeconds = timeTakenSeconds;
        this.attemptDate = LocalDateTime.now();
        this.hintsUsed = 0;
        this.scorePercentage = totalQuestions > 0 ? (correctAnswers * 100.0) / totalQuestions : 0;
        this.passed = this.scorePercentage >= 50;
    }
    
    // Getters
    public String getStudentRegNumber() { return studentRegNumber; }
    public String getStudentName() { return studentName; }
    public String getSubjectCode() { return subjectCode; }
    public String getSubjectName() { return subjectName; }
    public String getExamMode() { return examMode; }
    public String getDifficulty() { return difficulty; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectAnswers(){ return correctAnswers;}
    public int getWrongAnswers() { return totalQuestions - correctAnswers; }
    public double getScorePercentage() { return scorePercentage; }
    public boolean isPassed() { return passed; }
    public int getTimeTakenSeconds() { return timeTakenSeconds; }
    public int getHintsUsed() { return hintsUsed; }
    public void setHintsUsed(int hintsUsed) { this.hintsUsed = hintsUsed; }
    
    public String getFormattedDate() {
        return attemptDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    public String getFormattedTime() {
        return String.format("%02d:%02d", timeTakenSeconds / 60, timeTakenSeconds % 60);
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s | %.1f%% | %s", getFormattedDate(), subjectName, scorePercentage, passed ? "PASS" : "FAIL");
    }
}