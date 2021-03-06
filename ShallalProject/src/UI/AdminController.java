/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Model.About;
import Model.Category;
import Model.Employee;
import Model.HistoryCategory;
import Model.HistoryItem;
import Model.Item;
import Model.SQLQueries;
import Model.Status;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ryder
 */
public class AdminController implements Initializable {
    
    @FXML
    private AnchorPane anchorPane;
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
    private TableColumn<HistoryCategory, String> categoryHistoryName;
    @FXML
    private TableColumn<HistoryCategory, Timestamp> categoryHistoryStartDate;
    @FXML
    private TableColumn<HistoryCategory, Timestamp> categoryHistoryEndDate;
    @FXML
    private TableColumn<HistoryCategory, String> categoryHistoryReason;
    @FXML
    private TableColumn<HistoryItem, String> itemHistoryName;
    @FXML
    private TableColumn<HistoryItem, String> itemHistoryCategory;
    @FXML
    private TableColumn<HistoryItem, String> itemHistoryReason;
    @FXML
    private TableColumn<HistoryItem, Integer> itemHistoryPrice;
    @FXML
    private TableColumn<HistoryItem, Timestamp> itemHistoryStartDate;
    @FXML
    private TableColumn<HistoryItem, Timestamp> itemHistoryEndDate;
    @FXML
    private Button addUpdateEmployeeButton;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField phone1TextField;
    @FXML
    private TextField phone2TextField;
    @FXML
    private ComboBox<Status> employeeStatusComboBox;
    @FXML
    private ComboBox<String> employeeTypeComboBox;
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
    @FXML
    private TableView<HistoryCategory> categoryHistoryTable;
    @FXML
    private TableView<HistoryItem> itemsHistoryTable;
    
    ObservableList<Category> categories = FXCollections.observableArrayList(SQLQueries.getCategoryList());
    ObservableList<Status> statuses = FXCollections.observableArrayList(SQLQueries.getStatusList());
    ObservableList<Employee> employees;

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

        /**
         ****************History categories and items screen*******************
         */
        //Initilaize History Table
        categoryHistoryName.setCellValueFactory(new PropertyValueFactory("name"));
        categoryHistoryStartDate.setCellValueFactory(new PropertyValueFactory("startDate"));
        categoryHistoryEndDate.setCellValueFactory(new PropertyValueFactory("endDate"));
        categoryHistoryReason.setCellValueFactory(new PropertyValueFactory("reason"));

        //Initilaize Item Table
        itemHistoryName.setCellValueFactory(new PropertyValueFactory("name"));
        itemHistoryStartDate.setCellValueFactory(new PropertyValueFactory("startDate"));
        itemHistoryEndDate.setCellValueFactory(new PropertyValueFactory("endDate"));
        itemHistoryReason.setCellValueFactory(new PropertyValueFactory("reason"));
        itemHistoryPrice.setCellValueFactory(new PropertyValueFactory("price"));
        itemHistoryCategory.setCellValueFactory(new PropertyValueFactory("category"));

        //Fetch history items when history tap is pressed
        historyLogTap.setOnSelectionChanged((event) -> {
            
            ObservableList<HistoryCategory> historyCategory
                    = FXCollections.observableArrayList(SQLQueries.getHistoryCategoryList());
            categoryHistoryTable.setItems(historyCategory);
            
            ObservableList<HistoryItem> historyItems
                    = FXCollections.observableArrayList(SQLQueries.getHistoryItemList());
            itemsHistoryTable.setItems(historyItems);
        });

        /**
         ****************Employees screen*******************
         */
        employees = FXCollections.observableArrayList(SQLQueries.getUserInfo());

        //Add Types to Type comboBox
        employeeTypeComboBox.getItems().addAll("Cashier", "Admin", "Manager");
        employeeTypeComboBox.getSelectionModel().select("Cashier");

        //Add status to Status comboBox
        employeeStatusComboBox.getItems().addAll(statuses);
        employeeStatusComboBox.getSelectionModel().select(0);

        //Add NEW placeholder at first index
        employees.add(0, new Employee());
        
        employeeComboBox.setItems(employees);
        employeeComboBox.getSelectionModel().select(0);
        
        employeeComboBox.setOnAction(event -> {
            Employee e = employeeComboBox.getSelectionModel().getSelectedItem();
            if (!e.getFname().equals("New Employee")) {
                firstNameTextField.setText(e.getFname());
                lastNameTextField.setText(e.getLname());
                phone1TextField.setText(e.getPhone1());
                phone2TextField.setText(e.getPhone2());
                userNameTextField.setText(e.getUserName());
                passwordTextField.setText(e.getPassword());
                employeeStatusComboBox.getSelectionModel().select(e.getStatus());
                employeeTypeComboBox.getSelectionModel().select(e.getETname());
            } else {
                firstNameTextField.setText("");
                lastNameTextField.setText("");
                phone1TextField.setText("");
                phone2TextField.setText("");
                userNameTextField.setText("");
                passwordTextField.setText("");
                employeeStatusComboBox.getSelectionModel().select(0);
                employeeTypeComboBox.getSelectionModel().select(0);
            }
        });
    }
    
    @FXML
    private void onAddItemClicked(ActionEvent event) {
        
        try {
            String newName = addItemNameTextField.getText();
            Status newStatus = addItemStatusComboBox.valueProperty().getValue();
            
            int newPrice = Integer.valueOf(addItemPriceTextField.getText());
            
            if (newName.equals("") || newPrice <= 0) {
                Dialogs.errorDialog("Please enter the name and valid value");
                return;
            }
            if (categoryTable.getSelectionModel().getSelectedItem() == null) {
                Dialogs.errorDialog("Select category First");
                return;
            }
            if (categories.stream().anyMatch(c -> c.getItems().stream().anyMatch(t
                    -> t.getName().equalsIgnoreCase(newName)))) {
                Dialogs.errorDialog("Sorry this item is already used");
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
                Dialogs.sucessDialog("The item has been added successfully!");
                categories.setAll(SQLQueries.getCategoryList());
                itemsTable.getItems().setAll();

                //reset fields
                addItemNameTextField.setText("");
                addItemStatusComboBox.getSelectionModel().selectFirst();
            }
        } catch (Exception ex) {
            Dialogs.errorDialog("Something went wrong :(");
        }
    }
    
    @FXML
    private void onUpdateItemClicked(ActionEvent event) {
        try {
            
            Item oldItem = itemsTable.getSelectionModel().getSelectedItem();
            
            if (oldItem == null) {
                Dialogs.errorDialog("Please select an item first");
                return;
            }
            
            String newName = updateItemNameTextField.getText();
            Status newStatus = updateItemStatusComboBox.valueProperty().getValue();
            int newPrice = Integer.valueOf(updateItemPriceTextField.getText());
            
            if (newName.equals("") || newPrice <= 0) {
                Dialogs.errorDialog("Please enter the name and valid value");
                return;
            }
            if (categories.stream().anyMatch(c -> c.getItems().stream().anyMatch(t
                    -> t.getName().equalsIgnoreCase(newName) && t.getStatus().equals(newStatus)
                    && t.getPrice() == newPrice))) {
                Dialogs.errorDialog("Sorry you didn't make any change");
                return;
            }
            
            Item newItem = oldItem;
            newItem.setName(newName);
            newItem.setStatus(newStatus);
            newItem.setPrice(newPrice);
            
            boolean temp = SQLQueries.updateItem(newItem);
            
            if (temp) {
                Dialogs.sucessDialog("The item has been updated successfully!");
                categories.setAll(SQLQueries.getCategoryList());
                itemsTable.getItems().setAll();

                //reset fields
                updateItemNameTextField.setText("");
                updateItemStatusComboBox.getSelectionModel().selectFirst();
            }
        } catch (Exception ex) {
            Dialogs.errorDialog("Something went wrong");
        }
    }
    
    @FXML
    private void onAddCategoryClicked(ActionEvent event) {
        
        String newName = addCategoryTextField.getText();
        Status newStatus = addCategoryStatusComboBox.valueProperty().getValue();
        
        if (newName.equals("")) {
            Dialogs.errorDialog("Please enter the name first");
            return;
        }
        if (categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(newName)
                && c.getStatus().equals(newStatus))) {
            Dialogs.errorDialog("Sorry this category is already used");
            return;
        }
        
        Category category = new Category();
        category.setName(newName);
        category.setStatus(newStatus);
        category.setStartDate(new Timestamp(System.currentTimeMillis()));
        
        boolean temp = SQLQueries.addCategory(category);
        
        if (temp) {
            Dialogs.sucessDialog("The category has been added successfully!");
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
            Dialogs.errorDialog("Please select a category from the left table first");
            return;
        }
        if (category.getName().equals(newName) && category.getStatus().equals(newStatus)) {
            Dialogs.errorDialog("you didn't make any change");
            return;
        }
        category.setName(newName);
        category.setStatus(newStatus);
        boolean temp = SQLQueries.updateCategory(category);
        if (temp) {
            Dialogs.sucessDialog("The category has been updated!");
            categories.setAll(SQLQueries.getCategoryList());
            itemsTable.getItems().setAll();

            //reset fields
            updateCategoryTextField.setText("");
            updateCategoryStatusComboBox.getSelectionModel().selectFirst();
        }
    }
    
    @FXML
    private void addUpdateButton(ActionEvent event) {
        
        if (userNameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
            Dialogs.errorDialog("Username or password is empty!");
            return;
        }
        
        Employee e = employeeComboBox.getSelectionModel().getSelectedItem();
        e.setFname(firstNameTextField.getText());
        e.setLname(lastNameTextField.getText());
        e.setPhone1(phone1TextField.getText());
        e.setPhone2(phone2TextField.getText());
        e.setUserName(userNameTextField.getText());
        e.setPassword(passwordTextField.getText());
        e.setStatus(employeeStatusComboBox.getSelectionModel().getSelectedItem());
        
        switch (employeeTypeComboBox.getSelectionModel().getSelectedItem()) {
            case "Cashier":
                e.setETID(3);
                e.setETname("Cashier");
                break;
            case "Admin":
                e.setETID(1);
                e.setETname("Admin");
                break;
            case "Manager":
                e.setETID(2);
                e.setETname("Manager");
                break;
        }
        
        if (e.getEID() == 0) {
            if (SQLQueries.addEmployee(e)) {
                employees.add(e);
            } else {
                Dialogs.errorDialog("Error happened in adding new employee");
            }
        } else {
            if (SQLQueries.updateEmployee(e)) {
                employees.remove(employeeComboBox.getSelectionModel().getSelectedItem());
                employees.add(e);
                employeeComboBox.getSelectionModel().select(e);
            } else {
                Dialogs.errorDialog("Error happened in updating an employee");
            }
            
        }
    }
    
    @FXML
    private void onRemoveEmployee(ActionEvent event) {
        
        Employee e = employeeComboBox.getSelectionModel().getSelectedItem();
        
        if (e.getEID() == 0) {
            Dialogs.errorDialog("Please select an employee");
        } else {
            if (SQLQueries.deleteEmployee(e)) {
                employees.remove(e);
                employeeComboBox.getSelectionModel().select(0);
                Dialogs.sucessDialog(e.getFullName() + " " + "has been removed");
            }
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
