package com.sis.model;

public class Student {
    private int id;
    private String rollNo;
    private String name;
    private String email;
    private String password;
    private String branch;
    private int semester;

    public Student() {}

    public Student(int id, String rollNo, String name, String email, String branch, int semester) {
        this.id = id;
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.branch = branch;
        this.semester = semester;
    }

    // Getters
    public int getId()          { return id; }
    public String getRollNo()   { return rollNo; }
    public String getName()     { return name; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public String getBranch()   { return branch; }
    public int getSemester()    { return semester; }

    // Setters
    public void setId(int id)             { this.id = id; }
    public void setRollNo(String rollNo)  { this.rollNo = rollNo; }
    public void setName(String name)      { this.name = name; }
    public void setEmail(String email)    { this.email = email; }
    public void setPassword(String p)     { this.password = p; }
    public void setBranch(String branch)  { this.branch = branch; }
    public void setSemester(int semester) { this.semester = semester; }
}
