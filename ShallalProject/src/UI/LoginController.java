/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Model.Employee;
import Model.SQLQueries;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author test
 */
public class LoginController implements Initializable {

    private List<Employee> EL = new ArrayList<>();

    private String Username, Passwrd;

    @FXML
    private Button loginButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onLoginClicked(ActionEvent event) {
        // cheack if the list is there or not. 
        initializeI();
        ListIterator it = EL.listIterator();
        try {
            Username = userName.getText().trim();
            Passwrd = password.getText().trim();
            //System.out.println(Username + Passwrd);
            int counter = 0, eid = -1;
            while (it.hasNext()) {
                if (EL.get(counter).getUserName().equals(Username)) {
                    //System.out.println("UserName is here");
                    eid = counter;
                    break;
                }
                counter++;
                it.next();
            }
            if (eid != -1) {
                if (checkPass(eid, Passwrd)) {
                    System.out.println(EL.get(eid));
                    goToNextPage(EL.get(eid), EL.get(eid).getETname(), event);
                } else {
                    Dialogs.errorDialog("Username and password does not match");
                }

            } else {
                Dialogs.errorDialog("Username and password does not match");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void ClearUserandPass(ActionEvent event) {
        userName.clear();
        password.clear();
    }

    private void initializeI() {
        if (SQLQueries.isALLEmpoyeeHere(EL)) {
            System.out.println("It is already initilized");
        } else {
            EL = SQLQueries.getUserInfo();
            System.out.println("Initillize is done");
        }
    }

    private boolean checkPass(int Eid, String Password) {
        return EL.get(Eid).getPassword().equals(Password);
    }

    private void goToNextPage(Employee get, String eTname, ActionEvent event) throws IOException {

        String page = "";

        switch (eTname) {
            case "Admin":
                page = "Admin.fxml";
                break;
            case "Manager":
                page = "Manager.fxml";
                break;
            case "Cashier":
                page = "Cashier.fxml";
                CashierController.cName = get.getFullName();
                break;
            default:
                System.out.println("null user");
                Dialogs.errorDialog("null user");
                return;
        }

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent pane = FXMLLoader.load(getClass().getResource(page));
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
