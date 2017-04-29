/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Ryder
 */
public abstract class Employee {

    private int EID; //Employee ID
    private String fname; //FirstName
    private String lname; //LastName
    private int ETID; //Employee Type ID
    private String ETname; //Employee Type name
    private String userName; //ُEmployee userName
    private String password; //ُEmployee password
    private int statusID; //Employee Status
    private float salary; //Employee Salary 

    public Employee(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public int getEID() {
        return EID;
    }

    public void setEID(int EID) {
        this.EID = EID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getETID() {
        return ETID;
    }

    public void setETID(int ETID) {
        this.ETID = ETID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

}
