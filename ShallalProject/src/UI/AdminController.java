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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableColumn<?, ?> categoryHistoryStatus;
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
    private TableColumn<?, ?> itemHistoryStatus;
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
    private TableColumn<?, ?> categoryAction;
    @FXML
    private TableColumn<?, ?> itemsAction;
    @FXML
    private TextField updateCategoryTextField;
    @FXML
    private TextField updatItemNameTextField;
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

        categoryTable.setItems(categories);

        //Set items table
        itemsName.setCellValueFactory(new PropertyValueFactory("name"));
        itemsPrice.setCellValueFactory(new PropertyValueFactory("price"));
        itemsStatus.setCellValueFactory(data -> data.getValue().getStatus().descriptionProperty());

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
    }

    @FXML
    private void onAddCategoryClicked(ActionEvent event) {

        String newName = addCategoryTextField.getText();
        Status newStatus = addCategoryStatusComboBox.valueProperty().getValue();

        if (categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(newName))) {
            System.out.println("Sorry this category is already used");
            return;
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
        }
    }
}