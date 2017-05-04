/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Model.Category;
import Model.Item;
import Model.SQLQueries;
import Model.Status;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    private TableView<Item> itemsTable;
    @FXML
    private TableColumn<Item, String> itemsName;
    @FXML
    private TableColumn<Item, Integer> itemsPrice;
    @FXML
    private TableColumn<Item, String> itemsStatus;
    @FXML
    private TableColumn<Item, Button> itemsAction;
    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> categoryName;
    @FXML
    private TableColumn<Category, String> categoryStatus;
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

        //Set Category Table 
        categoryName.setCellValueFactory(new PropertyValueFactory("name"));
        categoryStatus.setCellValueFactory(data -> data.getValue().getStatus().descriptionProperty());

        ObservableList categories = FXCollections.observableArrayList(SQLQueries.getCategoryList());
        categoryTable.setItems(categories);

        categoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("ListView Selection Changed (selected: " + newValue.toString() + ")");
            itemsTable.setItems(FXCollections.observableArrayList(newValue.getItems()));
        });

        itemsName.setCellValueFactory(new PropertyValueFactory("name"));
        itemsPrice.setCellValueFactory(new PropertyValueFactory("price"));
        itemsStatus.setCellValueFactory(data -> data.getValue().getStatus().descriptionProperty());
        //itemsAction

        categoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("ListView Selection Changed (selected: " + newValue.toString() + ")");
        });
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
