package models;

import java.io.Serializable;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String questionText;
    private String[] options;
    private int correctOption;
    private String difficulty;
    private String subjectCode;
    private String hint;
    
    public Question(String questionText, String[] options, int correctOption, 
                    String difficulty, String subjectCode, String hint) {
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
        this.difficulty = difficulty;
        this.subjectCode = subjectCode;
        this.hint = hint;
    }
    
    public Question(String questionText, String[] options, int correctOption, 
                    String difficulty, String subjectCode) {
        this(questionText, options, correctOption, difficulty, subjectCode, "No hint available");
    }
    
    public String getQuestionText() { 
        return questionText; 
    }
    public void setQuestionText(String questionText) { 
        this.questionText = questionText; 
    }
    
    public String[] getOptions() { 
        return options; 
    }
    public void setOptions(String[] options) { 
        this.options = options; 
    }
    
    public int getCorrectOption() { 
        return correctOption; 
    }
    public void setCorrectOption(int correctOption) { 
        this.correctOption = correctOption; 
    }
    
    public String getDifficulty() { 
        return difficulty; 
    }
    public void setDifficulty(String difficulty) { 
        this.difficulty = difficulty; 
    }
    
    public String getSubjectCode() { 
        return subjectCode; 
    }
    public void setSubjectCode(String subjectCode) { 
        this.subjectCode = subjectCode; 
    }
    
    public String getHint() { 
        return hint; 
    }
    public void setHint(String hint) { 
        this.hint = hint; 
    }
    
    public boolean isCorrect(int selectedOption) {
        return selectedOption == correctOption;
    }
    
    public String getCorrectOptionText() {
        if (options != null && correctOption >= 0 && correctOption < options.length) {
            return options[correctOption];
        }
        return "Unknown";
    }
    
    @Override
    public String toString() {
        return questionText + " [" + difficulty + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Question q = (Question) obj;
        return questionText != null && questionText.equals(q.questionText);
    }
    
    @Override
    public int hashCode() {
        return questionText != null ? questionText.hashCode() : 0;
    }
}