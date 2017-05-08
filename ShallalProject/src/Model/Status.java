/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Ryder
 */
public class Status {

    private int statusID;
    private StringProperty description = new SimpleStringProperty();

    public Status(int statusID, String description) {
        this.statusID = statusID;
        setDescription(description);
    }

    public Status(int statusID) {
        this.statusID = statusID;
    }

    Status() {
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public String toString() {
        return getDescription();
    }

}
