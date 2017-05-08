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
public class Manager extends Employee{

    public Manager(String username, String password){
            super(username,password);
    }
    
    public boolean getItemReport() {
        return true;
    }
    
    public boolean getCategoryReport() {
        return true;
    }
}
