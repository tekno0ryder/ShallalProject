/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ryder
 */
public class SQLQueries {

    public static List<Category> geCategoryList() {

        List<Category> categoryList = new ArrayList<>();

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            //Query categories and insert them to categoryList
            try (Statement categoryStatement = connection.createStatement();
                    ResultSet categoryResult = categoryStatement.executeQuery("SELECT * FROM foodcategory")) {
                while (categoryResult.next()) {
                    Category c = new Category();
                    c.setCID(categoryResult.getInt("CID"));
                    c.setName(categoryResult.getString("name"));
                    c.setDescription(categoryResult.getString("description"));
                    c.setStartDate(categoryResult.getTimestamp("startdate"));
                    c.setStatus(new Status(categoryResult.getInt("statusID")));
                    categoryList.add(c);
                }
            }
            //Query items and insert them to categoryList
            try (Statement itemStatement = connection.createStatement()) {
                for (Category c : categoryList) {
                    ResultSet itemResult = itemStatement.executeQuery("SELECT * FROM fooditem "
                            + "WHERE fooditem.foodcategory = " + c.getCID());
                    System.out.println(c);
                    while (itemResult.next()) {
                        Item item = new Item();
                        item.setiID(itemResult.getInt("FIid"));
                        item.setName(itemResult.getString("name"));
                        item.setPrice(itemResult.getInt("price"));
                        item.setStartDate(itemResult.getTimestamp("startdate"));
                        item.setStatus(new Status(itemResult.getInt("statusID")));
                        c.getItems().add(item);
                        System.out.println(item);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return categoryList;
    }

    public boolean submitOrder() {

        return true;
    }
}
