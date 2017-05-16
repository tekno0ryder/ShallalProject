/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Model.About;
import Model.Category;
import Model.Item;
import Model.SQLQueries;
import Model.Transaction;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ryder
 */
public class CashierController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
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

    public static String cName;

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
    private void onSubmitOrder(ActionEvent event) throws IOException {

        ObservableList items = newTansactionTable.getItems();

        if (items.isEmpty()) {
            Dialogs.errorDialog("Add items to the transaction first!");
            return;
        }
        int amount = Integer.valueOf(totalPriceTextField.getText().replaceAll(" SAR", ""));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDate(new Timestamp(System.currentTimeMillis()));
        transaction.setSellBy(cName);
        transaction.setTransactionItems(items);

        printReceipt(transaction);

       // SQLQueries.newTransaction(transaction);
        Dialogs.sucessDialog("The order is succefully submitted!");
        onClear(event);

    }

    private void printReceipt(Transaction transaction) throws IOException {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String fileName = now.format(formatter);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String date = now.format(formatter2);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName + ".txt"), "utf-8"))) {

            writer.write("\t\t\t\tShallal Restuarant");
            writer.write(System.getProperty("line.separator"));
            writer.write("\t\t10th Street (Riyadh Street), Khobar, Saudi Arabia");
            writer.write(System.getProperty("line.separator"));

            writer.write(System.getProperty("line.separator"));
            writer.write("Date: " + date);
            writer.write(System.getProperty("line.separator"));
            writer.write("Sold by : " + cName);
            writer.write(System.getProperty("line.separator"));

            writer.write(System.getProperty("line.separator"));
            writer.write("Quantity" + "\t" + "Item" + "\t\t\t\t\t\t" + "Price");
            writer.write(System.getProperty("line.separator"));
            writer.write("--------------------------------------------------------------------");
            writer.write(System.getProperty("line.separator"));

            int max = 25;

            transaction.getTransactionItems().forEach((t) -> {
                try {
                    //calculate the extra spaces to allign the price
                    if (t.getName().length() < max) {
                        String spaces = "";
                        int spacesLength = max - t.getName().length();
                        for (int i = 0; i < spacesLength; i++) {
                            spaces += " ";
                        }
                        t.setName(t.getName() + spaces);
                    }

                    writer.write(String.valueOf(t.getQuantity()));
                    writer.write("\t\t");
                    writer.write(String.valueOf(t.getName()));
                    writer.write("\t\t\t");
                    writer.write(String.valueOf(t.getPrice()));
                    writer.write(System.getProperty("line.separator"));
                } catch (IOException ex) {
                    Logger.getLogger(CashierController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            writer.write(System.getProperty("line.separator"));
            writer.write("\t" + "Amount: " + "\t" + transaction.getAmount() + " SAR");
        }

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

    @FXML
    private void onLogout(ActionEvent event) throws IOException {

        Stage primaryStage = (Stage) anchorPane.getScene().getWindow();
        Parent pane = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void onAbout(ActionEvent event) throws IOException {
        new About();
    }
}
