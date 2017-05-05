/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Model.Category;
import Model.Item;
import Model.SQLQueries;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
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

    ObservableList categories = FXCollections.observableArrayList(SQLQueries.getCategoryList());

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
                        //event -> getTableView().getItems().remove(item)
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
                        }
                );
            }
        });

        //Set new Transaction table
        newTansactionName.setCellValueFactory(new PropertyValueFactory("name"));
        newTansactionPrice.setCellValueFactory(new PropertyValueFactory("price"));
        newTansactionQuantity.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());

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
                        //event -> getTableView().getItems().remove(item)
                        event -> {
                            if (item.getQuantity() == 1) {
                                getTableView().getItems().remove(item);
                            } else {
                                item.setQuantity(item.getQuantity() - 1);
                            }
                        }
                );
            }
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
