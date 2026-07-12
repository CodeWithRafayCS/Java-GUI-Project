package models;

import java.io.Serializable;

public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String subjectCode;
    private String subjectName;
    private String semester;
    private String type;
    
    public Subject(String subjectCode, String subjectName, String semester, String type) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.semester = semester;
        this.type = type;
    }
    
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    

    
    @Override
    public String toString() {
        return subjectName + " (" + type + ")";
    }
}