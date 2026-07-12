package models;

import java.io.Serializable;

public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String name;
    protected String registrationNumber;
    
    public Person(String name, String registrationNumber) {
        this.name = name;
        this.registrationNumber = registrationNumber;
    }
    
    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }
    public String getRegistrationNumber() { 
        return registrationNumber; 
    }
    public void setRegistrationNumber(String registrationNumber) { 
        this.registrationNumber = registrationNumber; 
    }

    public abstract String getRole();

}