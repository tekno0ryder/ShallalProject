/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Model.Category;
import Model.Status;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Ryder
 */
public class CashierController implements Initializable {

    @FXML
    private TextField totalPriceTextField;
    @FXML
    private TableView<?> newTansactionTable;
    @FXML
    private TableColumn<?, ?> newTansactionName;
    @FXML
    private TableColumn<?, ?> newTansactionQuantity;
    @FXML
    private TableColumn<?, ?> newTansactionPrice;
    @FXML
    private TableColumn<?, ?> newTansactionTotal;
    @FXML
    private TableColumn<?, ?> newTansactionAction;
    @FXML
    private TableView<?> itemsTable;
    @FXML
    private TableColumn<?, ?> itemsName;
    @FXML
    private TableColumn<?, ?> itemsPrice;
    @FXML
    private TableColumn<?, ?> itemsStatus;
    @FXML
    private TableColumn<?, ?> itemsAction;
    @FXML
    private TableView<?> categoryTable;
    @FXML
    private TableColumn<?, ?> categoryName;
    @FXML
    private TableColumn<?, ?> categoryStatus;
    @FXML
    private TableView<?> transactionDetailsTable;
    @FXML
    private TableColumn<?, ?> transactionDetailsName;
    @FXML
    private TableColumn<?, ?> transactionDetailsQuantity;
    @FXML
    private TableColumn<?, ?> transactionDetailsPrice;
    @FXML
    private TableColumn<?, ?> transactionDetailsTotal;
    @FXML
    private TableColumn<?, ?> transactionDetailsAction;
    @FXML
    private TextField newPriceTextField;
    @FXML
    private TableView<?> transactionTable;
    @FXML
    private TableColumn<?, ?> transactionID;
    @FXML
    private TableColumn<?, ?> transactionDate;
    @FXML
    private TableColumn<?, ?> transactionPrice;
    @FXML
    private TableColumn<?, ?> transactionSoldBy;
    @FXML
    private TableColumn<?, ?> transactionStatus;
    @FXML
    private MenuButton addNewItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoryName.setCellValueFactory(
                new PropertyValueFactory("CID"));
        categoryStatus.setCellValueFactory(
                new PropertyValueFactory("status"));

        Category c = new Category();
        c.setCID(1);
        c.setStatus(new Status(1));
        ObservableList data = FXCollections.observableArrayList(c);
        categoryTable.setItems(data);
    }

    @FXML
    private void onSubmitOrder(ActionEvent event) {
    }

    @FXML
    private void onClear(ActionEvent event) {
    }

    @FXML
    private void submitChangesButton(ActionEvent event) {
    }

    @FXML
    private void onAddNewItem(ActionEvent event) {
    }

}
