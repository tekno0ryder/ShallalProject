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
import Model.Transaction;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Ryder
 */
public class ManagerController implements Initializable {

    @FXML
    private Tab transactionLogTab;
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
    private DatePicker endDate;
    @FXML
    private DatePicker startDate;
    @FXML
    private Label reportName;
    @FXML
    private Label startDateLabel;
    @FXML
    private Label endDateLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private TableView<Item> itemsTable;
    @FXML
    private TableColumn<Item, String> itemsName;
    @FXML
    private TableColumn<Item, Integer> itemsPrice;
    @FXML
    private TableColumn<Item, String> itemsStatus;
    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> categoryName;
    @FXML
    private TableColumn<Category, String> categoryStatus;
    @FXML
    private Label totalSaleLabel;
    @FXML
    private Label fromLabel;
    @FXML
    private Label toLabel;
    @FXML
    private Label isLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /* TRANSACTION TAB */
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(SQLQueries.transactionList());

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

        transactionTable.setItems(transactions);

        /*REPORT TAB */
        ObservableList<Category> categories = FXCollections.observableArrayList(SQLQueries.getCategoryList());

        //Set Category Table 
        categoryName.setCellValueFactory(new PropertyValueFactory("name"));
        categoryStatus.setCellValueFactory(data -> data.getValue().getStatus().descriptionProperty());

        categoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            itemsTable.setItems(FXCollections.observableArrayList(newValue.getItems()));
            generateReport();
        });

        categoryTable.setItems(categories);

        //Set items table
        itemsName.setCellValueFactory(new PropertyValueFactory("name"));
        itemsPrice.setCellValueFactory(new PropertyValueFactory("price"));
        itemsStatus.setCellValueFactory(data -> data.getValue().getStatus().descriptionProperty());

        itemsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            generateReport();
        });

    }

    private void generateReport() {

        Item item = itemsTable.getSelectionModel().getSelectedItem();
        Category category = categoryTable.getSelectionModel().getSelectedItem();

        if (startDate.getValue() == null || endDate.getValue() == null) {
            System.out.println("Make sure the dates aren't empty");
            return;
        }
        if (endDate.getValue().isBefore(startDate.getValue()) || startDate.getValue().isAfter(endDate.getValue())) {
            System.out.println("You have made error in the dates !");
            return;
        }

        Timestamp startDate = Timestamp.valueOf(this.startDate.getValue().atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(this.endDate.getValue().atStartOfDay());

        if (item != null) {
            int report = SQLQueries.itemReport(startDate, endDate, item);

            reportName.setText("item(" + item.getName() + ")");
            this.startDateLabel.setText(this.startDate.getValue().toString());
            this.endDateLabel.setText(this.endDate.getValue().toString());
            this.priceLabel.setText(report + " SAR");

        } else if (category != null) {
            int report = SQLQueries.categoryReport(startDate, endDate, category);
            System.out.println(report);
        } else {
            System.out.println("Please Select a category or item first");
        }

    }

}