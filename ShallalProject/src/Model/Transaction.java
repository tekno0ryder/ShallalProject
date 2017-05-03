/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ryder
 */
public class Transaction {

    private int TID;
    private Timestamp date;
    private Status status;
    private int amount;
    private Employee sellBy;
    private List<Item> transactionItems = new ArrayList<>();

    public List<Item> getTransactionItems() {
        return transactionItems;
    }

    public void setTransactionItems(List<Item> transactionItems) {
        this.transactionItems = transactionItems;
    }
    
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Employee getSellBy() {
        return sellBy;
    }

    public void setSellBy(Employee sellBy) {
        this.sellBy = sellBy;
    }

    public int getTID() {
        return TID;
    }

    public void setTID(int TID) {
        this.TID = TID;
    }
    
    
}
