/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Model.Category;
import Model.Employee;
import Model.Item;
import Model.SQLQueries;
import Model.Status;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Ryder
 */
public class AdminController implements Initializable {

    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> categoryName;
    @FXML
    private TableColumn<Category, String> categoryStatus;
    @FXML
    private TableView<Item> itemsTable;
    @FXML
    private TableColumn<Item, String> itemsName;
    @FXML
    private TableColumn<Item, Integer> itemsPrice;
    @FXML
    private TableColumn<Item, String> itemsStatus;
    @FXML
    private Tab historyLogTap;
    @FXML
    private TableColumn<?, ?> categoryHistoryName;
    @FXML
    private TableColumn<?, ?> categoryHistoryStartDate;
    @FXML
    private TableColumn<?, ?> categoryHistoryEndDate;
    @FXML
    private TableColumn<?, ?> itemHistoryName;
    @FXML
    private TableColumn<?, ?> itemHistoryCategory;
    @FXML
    private TableColumn<?, ?> itemHistoryPrice;
    @FXML
    private TableColumn<?, ?> itemHistoryStartDate;
    @FXML
    private TableColumn<?, ?> itemHistoryEndDate;
    @FXML
    private Button addUpdateEmployeeButton;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField phone1TextField;
    @FXML
    private TextField phone2TextField;
    @FXML
    private ComboBox<Status> employeeStatusComboBox;
    @FXML
    private ComboBox<?> employeeTypeComboBox;
    @FXML
    private Button removeEmployeeBtton;
    @FXML
    private ComboBox<Employee> employeeComboBox;
    @FXML
    private TextField addItemNameTextField;
    @FXML
    private TextField addItemPriceTextField;
    @FXML
    private ComboBox<Status> addItemStatusComboBox;
    @FXML
    private TextField addCategoryTextField;
    @FXML
    private TableColumn<Category, Category> categoryAction;
    @FXML
    private TableColumn<Item, Item> itemsAction;
    @FXML
    private TextField updateCategoryTextField;
    @FXML
    private TextField updateItemNameTextField;
    @FXML
    private TextField updateItemPriceTextField;
    @FXML
    private ComboBox<Status> updateItemStatusComboBox;
    @FXML
    private ComboBox<Status> addCategoryStatusComboBox;
    @FXML
    private ComboBox<Status> updateCategoryStatusComboBox;

    ObservableList<Category> categories = FXCollections.observableArrayList(SQLQueries.getCategoryList());
    ObservableList<Status> statuses = FXCollections.observableArrayList(SQLQueries.getStatusList());

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /**
         ****************Edit categories and items screen*******************
         */
        //Set Category Table 
        categoryName.setCellValueFactory(new PropertyValueFactory("name"));
        categoryStatus.setCellValueFactory(data -> data.getValue().getStatus().descriptionProperty());

        categoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (categoryTable.getSelectionModel().getSelectedItem() != null) {
                itemsTable.setItems(FXCollections.observableArrayList(newValue.getItems()));
                updateCategoryTextField.setText(newValue.getName());
                updateCategoryStatusComboBox.getSelectionModel().select(newValue.getStatus());
            }
        });

        categoryAction.setCellValueFactory(data -> new ReadOnlyObjectWrapper<Category>(data.getValue()));
        categoryAction.setCellFactory(param -> new TableCell<Category, Category>() {

            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);

                if (category == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> {
                            TextInputDialog dialog = new TextInputDialog();
                            dialog.setTitle("Warning");
                            dialog.setHeaderText("You are about to delete this category: " + category.getName());
                            dialog.setContentText("Enter the reason:");
                            Optional<String> reason = dialog.showAndWait();
                            if (reason.isPresent()) {
                                deleteCategory(category, reason.get());
                            }
                        }
                );
            }
        });
        categoryTable.setItems(categories);
        //Set items table
        itemsName.setCellValueFactory(new PropertyValueFactory("name"));
        itemsPrice.setCellValueFactory(new PropertyValueFactory("price"));
        itemsStatus.setCellValueFactory(data -> data.getValue().getStatus().descriptionProperty());

        itemsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (itemsTable.getSelectionModel().getSelectedItem() != null) {
                updateItemNameTextField.setText(newValue.getName());
                updateItemStatusComboBox.getSelectionModel().select(newValue.getStatus());
                updateItemPriceTextField.setText(String.valueOf(newValue.getPrice()));
            }
        });

        itemsAction.setCellValueFactory(data -> new ReadOnlyObjectWrapper<Item>(data.getValue()));
        itemsAction.setCellFactory(param -> new TableCell<Item, Item>() {

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
                            TextInputDialog dialog = new TextInputDialog();
                            dialog.setTitle("Warning");
                            dialog.setHeaderText("You are about to delete this item: " + item.getName());
                            dialog.setContentText("Enter the reason:");
                            Optional<String> reason = dialog.showAndWait();
                            if (reason.isPresent()) {
                                deleteItem(item, categoryTable.getSelectionModel().getSelectedItem(), reason.get());
                            }
                        }
                );
            }
        });
        //Set statues to the combo boxes and to "Available"
        addCategoryStatusComboBox.getItems().setAll(statuses);
        addItemStatusComboBox.getItems().setAll(statuses);
        updateCategoryStatusComboBox.getItems().setAll(statuses);
        updateItemStatusComboBox.getItems().setAll(statuses);
        addCategoryStatusComboBox.getSelectionModel().selectFirst();
        addItemStatusComboBox.getSelectionModel().selectFirst();
        updateCategoryStatusComboBox.getSelectionModel().selectFirst();
        updateItemStatusComboBox.getSelectionModel().selectFirst();

    }

    @FXML
    private void onAddItemClicked(ActionEvent event) {

        try {
            String newName = addItemNameTextField.getText();
            Status newStatus = addItemStatusComboBox.valueProperty().getValue();

            int newPrice = Integer.valueOf(addItemPriceTextField.getText());

            if (newName.equals("") || newPrice <= 0) {
                System.out.println("Please enter the name and valid value");
                return;
            }
            if (categoryTable.getSelectionModel().getSelectedItem() == null) {
                System.out.println("Select category First");
                return;
            }
            if (categories.stream().anyMatch(c -> c.getItems().stream().anyMatch(t
                    -> t.getName().equalsIgnoreCase(newName)))) {
                System.out.println("Sorry this item is already used");
                return;
            }
            Item item = new Item();
            item.setName(newName);
            item.setStatus(newStatus);
            item.setStartDate(new Timestamp(System.currentTimeMillis()));
            item.setPrice(newPrice);

            Category itemCategory = categoryTable.getSelectionModel().getSelectedItem();
            boolean temp = SQLQueries.addItem(item, itemCategory);

            if (temp) {
                System.out.println("Successed !!");
                categories.setAll(SQLQueries.getCategoryList());
                itemsTable.getItems().setAll();

                //reset fields
                addItemNameTextField.setText("");
                addItemStatusComboBox.getSelectionModel().selectFirst();
            }
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Something went wrong");
            alert.setContentText("Oops!  Please revise your input");

            alert.showAndWait();
        }
    }

    @FXML
    private void onUpdateItemClicked(ActionEvent event) {
        try {

            Item oldItem = itemsTable.getSelectionModel().getSelectedItem();

            if (oldItem == null) {
                System.out.println("Please select an item first");
                return;
            }

            String newName = updateItemNameTextField.getText();
            Status newStatus = updateItemStatusComboBox.valueProperty().getValue();
            int newPrice = Integer.valueOf(updateItemPriceTextField.getText());

            if (newName.equals("") || newPrice <= 0) {
                System.out.println("Please enter the name and valid value");
                return;
            }
            if (categories.stream().anyMatch(c -> c.getItems().stream().anyMatch(t
                    -> t.getName().equalsIgnoreCase(newName) && t.getStatus().equals(newStatus)
                    && t.getPrice() == newPrice))) {
                System.out.println("Sorry you didn't make any change");
                return;
            }

            Item newItem = oldItem;
            newItem.setName(newName);
            newItem.setStatus(newStatus);
            newItem.setPrice(newPrice);

            boolean temp = SQLQueries.updateItem(newItem);

            if (temp) {
                System.out.println("Successed !!");
                categories.setAll(SQLQueries.getCategoryList());
                itemsTable.getItems().setAll();

                //reset fields
                updateItemNameTextField.setText("");
                updateItemStatusComboBox.getSelectionModel().selectFirst();
            }
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Something went wrong");
            alert.setContentText("Oops!  Please revise your input");

            alert.showAndWait();
        }
    }

    @FXML
    private void onAddCategoryClicked(ActionEvent event) {

        String newName = addCategoryTextField.getText();
        Status newStatus = addCategoryStatusComboBox.valueProperty().getValue();

        if (newName.equals("")) {
            System.out.println("Please enter the name first");
            return;
        }
        if (categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(newName)
                && c.getStatus().equals(newStatus))) {
            System.out.println("Sorry this category is already used");
            return;
        }

        Category category = new Category();
        category.setName(newName);
        category.setStatus(newStatus);
        category.setStartDate(new Timestamp(System.currentTimeMillis()));

        boolean temp = SQLQueries.addCategory(category);

        if (temp) {
            System.out.println("Successed !!");
            categories.setAll(SQLQueries.getCategoryList());
            itemsTable.getItems().setAll();

            //reset fields
            addCategoryTextField.setText("");
            addCategoryStatusComboBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void onUpdateCategoryClicked(ActionEvent event) {

        String newName = updateCategoryTextField.getText();
        Status newStatus = updateCategoryStatusComboBox.valueProperty().getValue();
        Category category = categoryTable.getSelectionModel().getSelectedItem();

        if (category == null) {
            System.out.println("Please select a category from the left table first");
            return;
        }
        if (category.getName().equals(newName) && category.getStatus().equals(newStatus)) {
            System.out.println("you didn't make any change");
            return;
        }
        category.setName(newName);
        category.setStatus(newStatus);
        boolean temp = SQLQueries.updateCategory(category);
        if (temp) {
            System.out.println("Successed !!");
            categories.setAll(SQLQueries.getCategoryList());
            itemsTable.getItems().setAll();

            //reset fields
            updateCategoryTextField.setText("");
            updateCategoryStatusComboBox.getSelectionModel().selectFirst();
        }
    }

    private void deleteItem(Item item, Category category, String reason) {
        SQLQueries.deleteItem(item, category, reason);
        categories.setAll(SQLQueries.getCategoryList());
        itemsTable.getItems().setAll();
    }

    private void deleteCategory(Category category, String reason) {
        SQLQueries.deleteCategory(category, reason);
        categories.setAll(SQLQueries.getCategoryList());
        itemsTable.getItems().setAll();
    }
}
