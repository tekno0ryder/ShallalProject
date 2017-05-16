/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Ryder
 */
public class Dialogs {

    public static void errorDialog(String Error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ooops, there was an error!");
        alert.setHeaderText(Error);
        alert.setContentText("Please try again");

        alert.showAndWait();
    }

    public static void sucessDialog(String sucess) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(sucess);

        alert.showAndWait();
    }
}
