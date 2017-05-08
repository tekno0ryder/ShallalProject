/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Ryder
 */
public class Item {

    private int iID;
    private String name;
    private int price;
    private Timestamp startDate;
    private int createdBy;
    private Status status;
    private SimpleIntegerProperty quantity = new SimpleIntegerProperty(1);
    private SimpleIntegerProperty total = new SimpleIntegerProperty();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getiID() {
        return iID;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }

    public int getQuantity() {
        return this.quantity.getValue();
    }

    public void setQuantity(int quantity) {
        this.quantity.setValue(quantity);
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public int getTotal() {
        updateTotal();
        return this.total.getValue();
    }

    public SimpleIntegerProperty totalProperty() {
        updateTotal();
        return total;
    }

    public void setTotal(int total) {
        this.total.setValue(total);
    }

    public void updateTotal() {
        setTotal(price * getQuantity());
    }

    @Override
    public String toString() {
        return "Item{" + "iID=" + iID + ", name=" + name + ", price=" + price + ", startDate=" + startDate + ", createdBy=" + createdBy + ", status=" + status + '}';
    }
}
