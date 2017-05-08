/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Model.Category;
import Model.Item;
import Model.SQLQueries;
import Model.Transaction;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
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
    private TableView<Item> newTansactionTable;
    @FXML
    private TableColumn<Item, String> newTansactionName;
    @FXML
    private TableColumn<Item, Integer> newTansactionQuantity;
    @FXML
    private TableColumn<Item, Integer> newTansactionPrice;
    @FXML
    private TableColumn<Item, Integer> newTansactionTotal;
    @FXML
    private TableColumn<Item, Item> newTansactionAction;
    @FXML
    private TableView<Item> itemsTable;
    @FXML
    private TableColumn<Item, String> itemsName;
    @FXML
    private TableColumn<Item, Integer> itemsPrice;
    @FXML
    private TableColumn<Item, String> itemsStatus;
    @FXML
    private TableColumn<Item, Item> itemsAction;
    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> categoryName;
    @FXML
    private TableColumn<Category, String> categoryStatus;
    @FXML
    private TableView<Item> transactionDetailsTable;
    @FXML
    private TableColumn<Item, String> transactionDetailsName;
    @FXML
    private TableColumn<Item, Integer> transactionDetailsQuantity;
    @FXML
    private TableColumn<Item, Integer> transactionDetailsPrice;
    @FXML
    private TableColumn<Item, Integer> transactionDetailsTotal;
    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, Integer> transactionID;
    @FXML
    private TableColumn<Transaction, Timestamp> transactionDate;
    @FXML
    private TableColumn<Transaction, Integer> transactionPrice;
    @FXML
    private TableColumn<Transaction, String> transactionSoldBy;
    @FXML
    private Tab transactionLogTab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<Category> categories = FXCollections.observableArrayList(SQLQueries.getCategoryList());

        //Set Category Table 
        categoryName.setCellValueFactory(new PropertyValueFactory("name"));
        categoryStatus.setCellValueFactory(data -> data.getValue().getStatus().descriptionProperty());

        categoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("ListView Selection Changed (selected: " + newValue.toString() + ")");
            itemsTable.setItems(FXCollections.observableArrayList(newValue.getItems()));
        });

        categoryTable.setItems(categories);

        //Set items table
        itemsName.setCellValueFactory(new PropertyValueFactory("name"));
        itemsPrice.setCellValueFactory(new PropertyValueFactory("price"));
        itemsStatus.setCellValueFactory(data -> data.getValue().getStatus().descriptionProperty());

        itemsAction.setCellValueFactory(data -> new ReadOnlyObjectWrapper<Item>(data.getValue()));
        itemsAction.setCellFactory(param -> new TableCell<Item, Item>() {

            private final Button addButton = new Button("Add");

            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setGraphic(null);
                    return;
                }
                if (!item.getStatus().getDescription().equalsIgnoreCase("Available")) {
                    return;
                }
                setGraphic(addButton);
                addButton.setOnAction(
                        event -> {
                            boolean exists = false;
                            ObservableList<Item> list = newTansactionTable.getItems();
                            for (Item i : list) {
                                if (i.equals(item)) {
                                    exists = true;
                                    i.setQuantity(i.getQuantity() + 1);
                                }
                            }
                            if (!exists) {
                                list.add(item);
                            }
                            updateTotalPriceField();
                        }
                );
            }
        });

        //Set new Transaction table
        newTansactionName.setCellValueFactory(new PropertyValueFactory("name"));
        newTansactionPrice.setCellValueFactory(new PropertyValueFactory("price"));
        newTansactionQuantity.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());

        newTansactionTotal.setCellValueFactory(data -> data.getValue().totalProperty().asObject());

        newTansactionAction.setCellValueFactory(data -> new ReadOnlyObjectWrapper<Item>(data.getValue()));
        newTansactionAction.setCellFactory(param -> new TableCell<Item, Item>() {

            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> {
                            if (item.getQuantity() == 1) {
                                getTableView().getItems().remove(item);
                            } else {
                                item.setQuantity(item.getQuantity() - 1);
                            }
                            updateTotalPriceField();
                        }
                );
            }
        });

        //Construct Existing Transaction Table
        transactionID.setCellValueFactory(new PropertyValueFactory("TID"));
        transactionDate.setCellValueFactory(new PropertyValueFactory("Date"));
        transactionPrice.setCellValueFactory(new PropertyValueFactory("amount"));
        transactionSoldBy.setCellValueFactory(new PropertyValueFactory("sellBy"));

        //Construct Details of transaction table
        transactionDetailsName.setCellValueFactory(new PropertyValueFactory("name"));
        transactionDetailsPrice.setCellValueFactory(new PropertyValueFactory("price"));
        transactionDetailsQuantity.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        transactionDetailsTotal.setCellValueFactory(data -> data.getValue().totalProperty().asObject());

        transactionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            transactionDetailsTable.setItems(FXCollections.observableArrayList(newValue.getTransactionItems()));
        });

        //When log tab is pressed Query again
        transactionLogTab.setOnSelectionChanged((event) -> {
            ObservableList<Transaction> transactions = FXCollections.observableArrayList(SQLQueries.transactionList());
            transactionTable.setItems(transactions);
        });
    }

    @FXML
    private void onSubmitOrder(ActionEvent event) {

        int amount = Integer.valueOf(totalPriceTextField.getText().replaceAll(" SAR", ""));
        ObservableList items = newTansactionTable.getItems();

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDate(new Timestamp(System.currentTimeMillis()));
        transaction.setSellBy("Ahmad Alsinan");
        transaction.setTransactionItems(items);

        SQLQueries.newTransaction(transaction);

        onClear(event);
    }

    @FXML
    private void onClear(ActionEvent event) {
        ObservableList items = newTansactionTable.getItems();
        items.forEach((item) -> {
            Item t = (Item) item;
            t.setQuantity(1);
        });
        newTansactionTable.getItems().removeAll(items);
        updateTotalPriceField();
    }

    private void updateTotalPriceField() {
        ObservableList<Item> list = newTansactionTable.getItems();
        int total = 0;
        for (Item i : list) {
            total += i.getTotal();
        }
        totalPriceTextField.setText(String.valueOf(total) + " SAR");
    }
}
