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

    public static List<Category> getCategoryList() {

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

    public static boolean newTransaction(Transaction transaction, Employee employee) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            try (Statement tidStatement = connection.createStatement()) {
                //Take latest TID and increament it , else start from 1
                ResultSet tidResult = tidStatement.executeQuery("SELECT tid FROM transaction");
                if (!tidResult.next()) {
                    transaction.setTID(1);
                } else {
                    tidResult.last();
                    int latestTID = tidResult.getInt("TID");
                    transaction.setTID(latestTID + 1);
                }
            }
            //Insert Transaction to Transaction Table
            try (Statement transactionStatement = connection.createStatement()) {
                String transactionQuery = transaction.getTID() + "," + transaction.getDate() + ","
                        + 1 + "," + transaction.getAmount() + "," + employee.getFullName();
                transactionStatement.executeUpdate("INSERT INTO transaction"
                        + "(TID,TRANSACTIONDATE,TRANSACTIONSTATUS,AMOUNT,SELLBY)\n"
                        + "VALUES (" + transactionQuery + ")");
            }
            //Insert Items in TransactionItems table
            List<Item> transactionItems = transaction.getTransactionItems();

            try (Statement itemStatement = connection.createStatement()) {
                for (Item item : transactionItems) {
                    String itemQuery = transaction.getTID() + "," + item.getiID() + "," + item.getQuantity();
                    itemStatement.executeUpdate("INSERT INTO transactionitems (TID,FIiD,Quantity)\n"
                            + "VALUES (" + itemQuery + ")");
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    public static List<Transaction> transactionList() {
        List<Transaction> transactionList = new ArrayList<>();

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            //Query categories and insert them to categoryList
            try (Statement transactionStatement = connection.createStatement()) {
                ResultSet transactionResult = transactionStatement.executeQuery("SELECT * FROM transaction");
                while (transactionResult.next()) {
                    Transaction t = new Transaction();
                    t.setTID(transactionResult.getInt("TID"));
                    t.setSellBy(transactionResult.getString("sellby"));
                    t.setDate(transactionResult.getTimestamp("transactiondate"));
                    t.setAmount(transactionResult.getInt("amount"));
                    t.setStatus(new Status(transactionResult.getInt("transactionStatus")));
                    transactionList.add(t);
                }

                try (Statement itemStatement = connection.createStatement()) {
                    for (Transaction t : transactionList) {
                        ResultSet itemResult = itemStatement.executeQuery("SELECT * FROM transactionitems\n"
                                + "NATURAL JOIN fooditem");
                        while (itemResult.next()) {
                            if (itemResult.getInt("TID") == t.getTID()) {
                                Item item = new Item();
                                item.setiID(itemResult.getInt("FIid"));
                                item.setName(itemResult.getString("name"));
                                item.setPrice(itemResult.getInt("price"));
                                item.setStartDate(itemResult.getTimestamp("startdate"));
                                item.setQuantity(itemResult.getInt("quantity"));
                                item.setStatus(new Status(itemResult.getInt("statusID")));
                                t.getTransactionItems().add(item);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return transactionList;
    }

}
