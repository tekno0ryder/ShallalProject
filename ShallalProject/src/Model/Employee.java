/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
    private Status status; //Employee Status
    private float salary; //Employee Salary 
    private List<String> phones; //Employee phones

    public Employee(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public List<Category> geCategoryList() {
        List<Category> list = new ArrayList<>();
        try {
            DBConnection db = new DBConnection();
            Connection connection = db.getMyConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM foodcategory");
            while (result.next()) {
                Category c = new Category();
                c.setCID(result.getInt("CID"));
                c.setName(result.getString("name"));
                c.setDescription(result.getString("description"));
                c.setStartDate(result.getDate("startdate"));
                System.out.println(result.getTime("startdate"));
                c.setStatus(new Status(result.getInt("statusID")));
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return list;
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

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
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

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

}
