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
public class Cashier extends Employee {

    public Cashier(String userName, String password) {
        super(userName, password);
    }

    public boolean submitOrder() {
        return true;
    }

    public boolean updateOrder() {
        return true;
    }
    
}
