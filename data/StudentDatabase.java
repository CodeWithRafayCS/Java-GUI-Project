package data;

import java.util.ArrayList;
import java.util.HashMap;
import models.Student;
import models.QuizResult;
import utils.DataPersistence;

public class StudentDatabase {
    private static StudentDatabase instance;
    private HashMap<String, Student> studentsByRegNumber;
    private ArrayList<Student> allStudents;
    
    private StudentDatabase() {
        studentsByRegNumber = new HashMap<>();
        allStudents = new ArrayList<>();
        loadFromFile();
    }
    
    public static StudentDatabase getInstance() {
        if (instance == null) {
            instance = new StudentDatabase();
        }
        return instance;
    }
    
    private void loadFromFile() {
        ArrayList<Student> loaded = DataPersistence.loadStudents();
        for (Student s : loaded) {
            if (!studentsByRegNumber.containsKey(s.getRegistrationNumber())) {
                studentsByRegNumber.put(s.getRegistrationNumber(), s);
                allStudents.add(s);
            }
        }
        System.out.println("StudentDatabase loaded: " + allStudents.size() + " students");
    }
    
    private void saveToFile() {
        DataPersistence.saveStudents(allStudents);
        System.out.println("StudentDatabase saved: " + allStudents.size() + " students");
    }
    
    public boolean addStudent(Student student) {
        if (student == null || student.getRegistrationNumber() == null) {
            return false;
        }
        
        if (!studentsByRegNumber.containsKey(student.getRegistrationNumber())) {
            studentsByRegNumber.put(student.getRegistrationNumber(), student);
            allStudents.add(student);
            saveToFile();
            return true;
        }
        return false;
    }
    
    public boolean removeStudent(String registrationNumber) {
        Student student = studentsByRegNumber.remove(registrationNumber);
        if (student != null) {
            allStudents.remove(student);
            saveToFile();
            return true;
        }
        return false;
    }
    
    public Student findStudentByRegNumber(String registrationNumber) {
        return studentsByRegNumber.get(registrationNumber);
    }
    
    
    public ArrayList<Student> getAllStudents() {
        return new ArrayList<>(allStudents);
    }
    
    
    public boolean studentExists(String registrationNumber) {
        return studentsByRegNumber.containsKey(registrationNumber);
    }
    
    public boolean validateStudentLogin(String registrationNumber, String fullName) {
        Student s = findStudentByRegNumber(registrationNumber);
        return s != null && s.isActive() && s.getFullName().equalsIgnoreCase(fullName);
    }
    
    // StudentDatabase.java
public boolean addQuizResult(String registrationNumber, QuizResult result) {
    Student s = findStudentByRegNumber(registrationNumber);
    if (s != null) {
        // Check if this specific result object was already added
        if (!s.getQuizHistory().contains(result)) {
            s.addQuizResult(result);
            saveToFile();
            return true;
        }
    }
    return false;
}

}