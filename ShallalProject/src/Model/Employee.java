/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.List;

/**
 *
 * @author Ryder
 */
public class Employee {

    private int EID; //Employee ID
    private String fname; //FirstName
    private String lname; //LastName
    private int ETID; //Employee Type ID
    private String ETname; //Employee Type name
    private String userName; //ُEmployee userName
    private String password; //ُEmployee password
    private Status status; //Employee Status
    private int salary; //Employee Salary 
    private List<Integer> phones; //Employee phones

    public Employee(String userName, String password) {
        
        if(userName == null || password == null)
        {
            System.out.println("Check the entries please then try again");
        }
        else{
        this.userName = userName;
        this.password = password;
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getETname() {
        return ETname;
    }

    public void setETname(String ETname) {
        this.ETname = ETname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getPhones() {
        return phones;
    }

    public void setPhones(List<Integer> phones) {
        this.phones = phones;
    }

    public String getFullName() {
        return "\'" + this.fname + this.lname + "\'";
    }

}
