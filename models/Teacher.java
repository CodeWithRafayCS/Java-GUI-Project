package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Teacher extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String teacherId;
    private String fullName;
    private String hashedPassword;
    private ArrayList<Student> managedStudents;
    private HashMap<String, ArrayList<Question>> questionBankByDifficulty;
    
    // Removed email and department
    public Teacher(String teacherId, String fullName) {
        super(fullName, teacherId);
        this.teacherId = teacherId;
        this.fullName = fullName;
        this.managedStudents = new ArrayList<>();
        this.questionBankByDifficulty = new HashMap<>();
        
        questionBankByDifficulty.put("Easy", new ArrayList<>());
        questionBankByDifficulty.put("Medium", new ArrayList<>());
        questionBankByDifficulty.put("Hard", new ArrayList<>());
    }
    
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; this.registrationNumber = teacherId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; this.name = fullName; }
    
    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }
    


    public ArrayList<Student> getManagedStudents() { return managedStudents; }
    public HashMap<String, ArrayList<Question>> getQuestionBankByDifficulty() { return questionBankByDifficulty; }
    
    public void addStudent(Student student) {
        if (findStudentByRegNumber(student.getRegistrationNumber()) == null) {
            managedStudents.add(student);
        }
    }
    
    public void removeStudent(String registrationNumber) {
        managedStudents.removeIf(s -> s.getRegistrationNumber().equals(registrationNumber));
    }
    
    public Student findStudentByRegNumber(String registrationNumber) {
        for (Student s : managedStudents) {
            if (s.getRegistrationNumber().equals(registrationNumber)) {
                return s;
            }
        }
        return null;
    }
    
    public ArrayList<Student> getStudentsBySemester(int semester) {
        ArrayList<Student> result = new ArrayList<>();
        for (Student s : managedStudents) {
            if (s.getSemester() == semester) {
                result.add(s);
            }
        }
        return result;
    }
    
    public void addQuestionManually(Question question) {
        String difficulty = question.getDifficulty();
        if (questionBankByDifficulty.containsKey(difficulty)) {
            questionBankByDifficulty.get(difficulty).add(question);
        }
    }
    
    public ArrayList<Question> getQuestionsByDifficulty(String difficulty) {
        return questionBankByDifficulty.getOrDefault(difficulty, new ArrayList<>());
    }
    
    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> all = new ArrayList<>();
        for (ArrayList<Question> list : questionBankByDifficulty.values()) {
            all.addAll(list);
        }
        return all;
    }
    
    public int getQuestionCountByDifficulty(String difficulty) {
        return questionBankByDifficulty.getOrDefault(difficulty, new ArrayList<>()).size();
    }
    
    public int getTotalQuestionCount() {
        return getQuestionCountByDifficulty("Easy") + 
               getQuestionCountByDifficulty("Medium") + 
               getQuestionCountByDifficulty("Hard");
    }
    
    
    @Override
    public String getRole() { return "Teacher"; }

    
    @Override
    public String toString() {
        return fullName + " (" + teacherId + ")";
    }
}