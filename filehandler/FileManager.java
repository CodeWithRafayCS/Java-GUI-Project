package filehandler;

import java.io.*;
import java.util.ArrayList;
import models.Question;

public class FileManager {
    private static FileManager instance;
    
    private FileManager() {}
    
    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }
    
    public ArrayList<Question> importQuestionsFromCSV(String filePath) throws IOException {
        ArrayList<Question> questions = new ArrayList<>();
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine && line.toLowerCase().contains("question")) {
                    isFirstLine = false;
                    continue;
                }
                isFirstLine = false;
                
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String questionText = parts[0].trim();
                    String[] options = new String[4];
                    for (int i = 0; i < 4; i++) {
                        options[i] = parts[i + 1].trim();
                    }
                    int correctOption = Integer.parseInt(parts[5].trim())-1;
                    System.out.println("CSV correctOption value: " + parts[5].trim() + " → converted to: " + correctOption);
                    String difficulty = parts[6].trim();
                    String subjectCode = parts[7].trim();
                    String hint = parts.length > 8 ? parts[8].trim() : "No hint available";
                    
                    Question q = new Question(questionText, options, correctOption, difficulty, subjectCode, hint);
                    questions.add(q);
                }
            }
        } finally {
            if (reader != null) reader.close();
        }
        return questions;
    }
    
    public ArrayList<Question> importQuestionsFromTXT(String filePath) throws IOException {
        ArrayList<Question> questions = new ArrayList<>();
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            String questionText = null;
            String[] options = new String[4];
            int correctOption = -1;
            String difficulty = null;
            String subjectCode = null;
            String hint = "No hint available";
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("---")) {
                    if (questionText != null && correctOption != -1 && difficulty != null && subjectCode != null) {
                        questions.add(new Question(questionText, options, correctOption, difficulty, subjectCode, hint));
                    }
                    questionText = null;
                    options = new String[4];
                    correctOption = -1;
                    difficulty = null;
                    subjectCode = null;
                    hint = "No hint available";
                    continue;
                }
                
                if (line.startsWith("Question:")) {
                    questionText = line.substring(9).trim();
                } else if (line.startsWith("Option1:")) {
                    options[0] = line.substring(8).trim();
                } else if (line.startsWith("Option2:")) {
                    options[1] = line.substring(8).trim();
                } else if (line.startsWith("Option3:")) {
                    options[2] = line.substring(8).trim();
                } else if (line.startsWith("Option4:")) {
                    options[3] = line.substring(8).trim();
                } else if (line.startsWith("Answer:")) {
                    correctOption = Integer.parseInt(line.substring(7).trim());
                } else if (line.startsWith("Difficulty:")) {
                    difficulty = line.substring(11).trim();
                } else if (line.startsWith("SubjectCode:")) {
                    subjectCode = line.substring(12).trim();
                } else if (line.startsWith("Hint:")) {
                    hint = line.substring(5).trim();
                }
            }
            
            if (questionText != null && correctOption != -1 && difficulty != null && subjectCode != null) {
                questions.add(new Question(questionText, options, correctOption, difficulty, subjectCode, hint));
            }
        } finally {
            if (reader != null) reader.close();
        }
        return questions;
    }
    
    
    public void exportStudentReport(String reportData, String filePath) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(reportData);
        } finally {
            if (writer != null) writer.close();
        }
    }
}