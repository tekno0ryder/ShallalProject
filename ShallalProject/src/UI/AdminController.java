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
    private TextField itemNameTextField;
    @FXML
    private TextField itemPriceTextField;
    @FXML
    private ComboBox<Status> itemStatusComboBox;
    @FXML
    private Button addUpdateItemButton;
    @FXML
    private Button deleteItemButton;
    @FXML
    private TextField categoryNameTextField;
    @FXML
    private ComboBox<Status> categoryStatusComboBox;
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
    private ComboBox<?> statusComboBox;
    @FXML
    private ComboBox<?> typeComboBox;
    @FXML
    private Button removeEmployeeBtton;
    @FXML
    private ComboBox<?> employeeComboBox;

    ObservableList<Category> categories = FXCollections.observableArrayList(SQLQueries.getCategoryList());
    ObservableList<Status> statuses = FXCollections.observableArrayList(SQLQueries.getStatusList());

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /**
         * ******Edit categories and items screen*******
         */
        //Set Category Table 
        categoryName.setCellValueFactory(new PropertyValueFactory("name"));
        categoryStatus.setCellValueFactory(data -> data.getValue().getStatus().descriptionProperty());

        categoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            itemsTable.setItems(FXCollections.observableArrayList(newValue.getItems()));
        });

        categoryTable.setItems(categories);

        //Set items table
        itemsName.setCellValueFactory(new PropertyValueFactory("name"));
        itemsPrice.setCellValueFactory(new PropertyValueFactory("price"));
        itemsStatus.setCellValueFactory(data -> data.getValue().getStatus().descriptionProperty());

        //Set statues to the combo boxes
        categoryStatusComboBox.getItems().setAll(statuses);
        itemStatusComboBox.getItems().setAll(statuses);
        categoryStatusComboBox.getSelectionModel().selectFirst();
        itemStatusComboBox.getSelectionModel().selectFirst();

    }

    @FXML
    private void onRemoveCategory(ActionEvent event) {
    }

    @FXML
    private void onAddUpdateCategory(ActionEvent event) {
        String name = categoryNameTextField.getText();
        Status status = categoryStatusComboBox.valueProperty().getValue();
        Category category = categoryTable.getSelectionModel().getSelectedItem();

        //if  means Update , else add
        if (categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(name))) {
            category = categories.filtered(c -> c.getName().equalsIgnoreCase(name)).get(0);
            category.setStatus(status);
            
            boolean temp = SQLQueries.updateCategory(category);
            
            
        } else {
        }
    }

}
