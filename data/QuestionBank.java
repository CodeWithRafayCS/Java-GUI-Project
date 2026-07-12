package data;

import java.util.ArrayList;
import java.util.HashMap;
import models.Question;
import models.Subject;
import utils.DataPersistence;

public class QuestionBank {
    private static QuestionBank instance;
    private HashMap<String, ArrayList<Question>> questionsBySubject;
    private HashMap<String, HashMap<String, ArrayList<Question>>> questionsBySubjectAndDifficulty;
    private ArrayList<Subject> subjects;
    
    private QuestionBank() {
        questionsBySubject = new HashMap<>();
        questionsBySubjectAndDifficulty = new HashMap<>();
        subjects = new ArrayList<>();
        initializeSubjects();
        loadFromFile();
    }
    
    public static QuestionBank getInstance() {
        if (instance == null) {
            instance = new QuestionBank();
        }
        return instance;
    }
    
    private void loadFromFile() {
        ArrayList<Question> loaded = DataPersistence.loadQuestions();
        for (Question q : loaded) {
            addQuestion(q, q.getSubjectCode());
        }
        System.out.println("QuestionBank loaded: " + getTotalQuestionCount() + " questions");
    }
    
    private void saveToFile() {
        DataPersistence.saveQuestions(getAllQuestions());
        System.out.println("QuestionBank saved: " + getTotalQuestionCount() + " questions");
    }
    
    private void initializeSubjects() {
        // Semester 1
        subjects.add(new Subject("CS101", "Programming Fundamental (Theory)", "1", "Theory"));
        subjects.add(new Subject("CS101L", "Programming Fundamental (Lab)", "1", "Lab"));
        subjects.add(new Subject("MATH101", "Calculus & Analytical Geometry", "1", "Theory"));
        subjects.add(new Subject("CS102", "Discrete Structures", "1", "Theory"));
        subjects.add(new Subject("CS103", "Information & Communication Technologies (Theory)", "1", "Theory"));
        subjects.add(new Subject("CS103L", "Information & Communication Technologies (Lab)", "1", "Lab"));
        subjects.add(new Subject("ENG101", "Functional English", "1", "Theory"));
        
        // Semester 2
        subjects.add(new Subject("CS201", "Object Oriented Programming (Theory)", "2", "Theory"));
        subjects.add(new Subject("CS201L", "Object Oriented Programming (Lab)", "2", "Lab"));
        subjects.add(new Subject("MATH201", "Linear Algebra", "2", "Theory"));
        subjects.add(new Subject("MATH202", "Multivariable Calculus", "2", "Theory"));
        subjects.add(new Subject("CS202", "Database System (Theory)", "2", "Theory"));
        subjects.add(new Subject("CS202L", "Database System (Lab)", "2", "Lab"));
        subjects.add(new Subject("CS203", "Digital Logic Design (Theory)", "2", "Theory"));
        subjects.add(new Subject("CS203L", "Digital Logic Design (Lab)", "2", "Lab"));
    }
    
    public void addQuestion(Question question, String subjectCode) {
        // Add to subject map
        if (!questionsBySubject.containsKey(subjectCode)) {
            questionsBySubject.put(subjectCode, new ArrayList<>());
        }
        questionsBySubject.get(subjectCode).add(question);
        
        // Add to difficulty map
        if (!questionsBySubjectAndDifficulty.containsKey(subjectCode)) {
            questionsBySubjectAndDifficulty.put(subjectCode, new HashMap<>());
            questionsBySubjectAndDifficulty.get(subjectCode).put("Easy", new ArrayList<>());
            questionsBySubjectAndDifficulty.get(subjectCode).put("Medium", new ArrayList<>());
            questionsBySubjectAndDifficulty.get(subjectCode).put("Hard", new ArrayList<>());
        }
        
        String difficulty = question.getDifficulty();
        questionsBySubjectAndDifficulty.get(subjectCode).get(difficulty).add(question);
        saveToFile();
    }
    
    
    public ArrayList<Question> getQuestionsBySubjectAndDifficulty(String subjectCode, String difficulty) {
        if (questionsBySubjectAndDifficulty.containsKey(subjectCode)) {
            return questionsBySubjectAndDifficulty.get(subjectCode).getOrDefault(difficulty, new ArrayList<>());
        }
        return new ArrayList<>();
    }
    
    public ArrayList<Subject> getAllSubjects() {
        return new ArrayList<>(subjects);
    }
    
    public ArrayList<Subject> getSubjectsBySemester(String semester) {
        ArrayList<Subject> result = new ArrayList<>();
        for (Subject s : subjects) {
            if (s.getSemester().equals(semester)) {
                result.add(s);
            }
        }
        return result;
    }
    
    
    public int getQuestionCount(String subjectCode, String difficulty) {
        return getQuestionsBySubjectAndDifficulty(subjectCode, difficulty).size();
    }
    
    
    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> all = new ArrayList<>();
        for (ArrayList<Question> list : questionsBySubject.values()) {
            all.addAll(list);
        }
        return all;
    }
    
    public int getTotalQuestionCount() {
        int total = 0;
        for (ArrayList<Question> list : questionsBySubject.values()) {
            total += list.size();
        }
        return total;
    }
    
}