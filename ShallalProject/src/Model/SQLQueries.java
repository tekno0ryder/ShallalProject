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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Ryder
 */
public class SQLQueries {

    //Get category list with all their associated items
    public static List<Category> getCategoryList() {

        List<Category> categoryList = new ArrayList<>();

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            //Query categories and insert them to categoryList
            try (Statement categoryStatement = connection.createStatement();
                    ResultSet categoryResult = categoryStatement.executeQuery("SELECT * FROM foodcategory;")) {
                while (categoryResult.next()) {
                    Category c = new Category();
                    c.setCID(categoryResult.getInt("CID"));
                    c.setName(categoryResult.getString("name"));
                    c.setDescription(categoryResult.getString("description"));
                    c.setStartDate(categoryResult.getTimestamp("startdate"));

                    Status status = new Status(categoryResult.getInt("statusID"));
                    c.setStatus(getStatusWithDescription(status));

                    categoryList.add(c);
                }
            }
            //Query items and insert them to categoryList
            try (Statement itemStatement = connection.createStatement()) {
                for (Category c : categoryList) {
                    ResultSet itemResult = itemStatement.executeQuery("SELECT * FROM fooditem "
                            + "WHERE fooditem.foodcategory = " + c.getCID());
                    // System.out.println(c);
                    while (itemResult.next()) {
                        Item item = new Item();
                        item.setiID(itemResult.getInt("FIid"));
                        item.setName(itemResult.getString("name"));
                        item.setPrice(itemResult.getInt("price"));
                        item.setStartDate(itemResult.getTimestamp("startdate"));

                        Status status = new Status(itemResult.getInt("statusID"));
                        item.setStatus(getStatusWithDescription(status));

                        c.getItems().add(item);
                        // System.out.println("\t" + item);
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

    //Submit new transaction
    public static boolean newTransaction(Transaction transaction) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            //Insert Transaction to Transaction Table
            try (Statement transactionStatement = connection.createStatement()) {
                String transactionQuery = "'" + transaction.getDate() + "'" + ","
                        + "'" + transaction.getAmount() + "'" + ","
                        + "'" + transaction.getSellBy() + "'";

                transactionStatement.executeUpdate("INSERT INTO transaction"
                        + " (TRANSACTIONDATE,AMOUNT,SELLBY)\n"
                        + " VALUES(" + transactionQuery + ")");
            }

            try (Statement tidStatement = connection.createStatement()) {
                //Take the TID so we can store the items in their table
                ResultSet tidResult = tidStatement.executeQuery("SELECT tid FROM transaction;");
                if (!tidResult.next()) {
                    transaction.setTID(1);
                } else {
                    do {
                        transaction.setTID(tidResult.getInt("TID"));
                    } while (tidResult.next());
                }
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

    //Get Transaction list with all their items 
    public static List<Transaction> transactionList() {
        List<Transaction> transactionList = new ArrayList<>();

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            //Query categories and insert them to categoryList
            try (Statement transactionStatement = connection.createStatement()) {
                ResultSet transactionResult = transactionStatement.executeQuery("SELECT * FROM transaction;");
                while (transactionResult.next()) {
                    Transaction t = new Transaction();
                    t.setTID(transactionResult.getInt("TID"));
                    t.setSellBy(transactionResult.getString("sellby"));
                    t.setDate(transactionResult.getTimestamp("transactiondate"));
                    t.setAmount(transactionResult.getInt("amount"));

                    /*Status status = new Status(transactionResult.getInt("transactionStatus"), true);
                    t.setStatus(getStatusWithDescription(status));*/
                    transactionList.add(t);
                }

                try (Statement itemStatement = connection.createStatement()) {
                    for (Transaction t : transactionList) {
                        ResultSet itemResult = itemStatement.executeQuery("SELECT * FROM transactionitems\n"
                                + " NATURAL JOIN fooditem");
                        while (itemResult.next()) {
                            if (itemResult.getInt("TID") == t.getTID()) {
                                Item item = new Item();
                                item.setiID(itemResult.getInt("FIid"));
                                item.setName(itemResult.getString("name"));
                                item.setPrice(itemResult.getInt("price"));
                                item.setStartDate(itemResult.getTimestamp("startdate"));
                                item.setQuantity(itemResult.getInt("quantity"));

                                /*Status status = new Status(transactionResult.getInt("transactionStatus"), false);
                                item.setStatus(getStatusWithDescription(status));*/
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

    //Get status description from the correct table
    public static Status getStatusWithDescription(Status status) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection();
                Statement statusStatement = connection.createStatement()) {
            //Query the status description
            String query = "SELECT description FROM status"
                    + " WHERE SID =" + status.getStatusID();
            ResultSet statusResult = statusStatement.executeQuery(query);

            if (statusResult.next()) {
                status.setDescription(statusResult.getString("description"));
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return status;
    }

    //Return List of status in DB
    public static List<Status> getStatusList() {

        List<Status> list = new ArrayList<>();

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection();
                Statement statusStatement = connection.createStatement()) {
            //Query the status description
            String query = "SELECT * FROM Status";
            ResultSet statusResult = statusStatement.executeQuery(query);

            while (statusResult.next()) {
                int id = statusResult.getInt("sid");
                String description = statusResult.getString("description");
                list.add(new Status(id, description));
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return list;
    }

    public static boolean updateCategory(Category category) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            try (Statement categoryStatement = connection.createStatement()) {
                String query = "UPDATE foodcategory "
                        + "SET name = '" + category.getName() + "', "
                        + "statusID = " + category.getStatus().getStatusID()
                        + " WHERE CID = " + category.getCID();
                categoryStatement.executeUpdate(query);
            }
            try (Statement itemStatement = connection.createStatement()) {
                for (Item item : category.getItems()) {
                    String query = "UPDATE fooditem "
                            + "SET statusID = " + category.getStatus().getStatusID()
                            + " WHERE foodCategory = " + category.getCID()
                            + " AND FIiD = " + item.getiID();
                    itemStatement.executeUpdate(query);
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

    public static boolean addCategory(Category category) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            try (Statement categoryStatement = connection.createStatement()) {
                String query = "INSERT INTO foodcategory (name,startdate,statusid) "
                        + "VALUES('" + category.getName() + "','"
                        + category.getStartDate() + "','"
                        + category.getStatus().getStatusID() + "')";
                categoryStatement.executeUpdate(query);
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    public static boolean deleteCategory(Category category, String reason) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            try (Statement categoryStatement = connection.createStatement()) {
                String query = "DELETE FROM foodcategory "
                        + "WHERE CID=" + category.getCID();
                categoryStatement.executeUpdate(query);
            }
            try (Statement historyStatement = connection.createStatement()) {
                String query = "INSERT INTO foodcategoryhistory (name,startdate,enddate,reason) "
                        + "VALUES('" + category.getName() + "','"
                        + category.getStartDate() + "','"
                        + new Timestamp(System.currentTimeMillis()) + "','"
                        + reason + "')";
                historyStatement.executeUpdate(query);
            }
            //delete items belong to the category
            category.getItems().forEach((t) -> deleteItem(t, category, category.getName() + " category is deleted"));

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    public static boolean addItem(Item item, Category category) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            try (Statement itemStatement = connection.createStatement()) {
                String query = "INSERT INTO fooditem (name,startdate,statusid,price,foodCategory) "
                        + "VALUES('" + item.getName() + "','"
                        + item.getStartDate() + "','"
                        + item.getStatus().getStatusID() + "','"
                        + item.getPrice() + "','"
                        + category.getCID() + "')";
                itemStatement.executeUpdate(query);
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    public static boolean updateItem(Item item) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            try (Statement itemStatement = connection.createStatement()) {
                String query = "UPDATE fooditem "
                        + "SET name = '" + item.getName() + "', "
                        + "statusID = " + item.getStatus().getStatusID() + ", "
                        + "price = " + item.getPrice()
                        + " WHERE FIiD = " + item.getiID();
                itemStatement.executeUpdate(query);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    public static boolean deleteItem(Item item, Category category, String reason) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            try (Statement deleteStatement = connection.createStatement()) {
                String query = "DELETE FROM fooditem "
                        + "WHERE FIiD=" + item.getiID();
                deleteStatement.executeUpdate(query);
            }
            try (Statement historyStatement = connection.createStatement()) {
                String query = "INSERT INTO fooditemhistory (name,startdate,enddate,category,price,reason) "
                        + "VALUES('" + item.getName() + "','"
                        + item.getStartDate() + "','"
                        + new Timestamp(System.currentTimeMillis()) + "','"
                        + category.getName() + "','"
                        + item.getPrice() + "','"
                        + reason + "')";
                historyStatement.executeUpdate(query);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    public static int itemReport(Timestamp startTimeStamp, Timestamp endTimeStamp, Item item) {

        int totalMoney = 0;

        List<Transaction> transaction = transactionList();
        List<Item> transactionItems;

        int quantity = 0;
        for (int i = 0; i < transaction.size(); i++) {
            if (transaction.get(i).getDate().compareTo(startTimeStamp) >= 0
                    && (transaction.get(i).getDate().compareTo(endTimeStamp) <= 0)) {

                transactionItems = transaction.get(i).getTransactionItems();
                for (int j = 0; j < transactionItems.size(); j++) {
                    Item temp = transactionItems.get(j);
                    if (temp.equals(item)) {
                        totalMoney += temp.getQuantity() * temp.getPrice();
                    }
                }
            }
        }

        return totalMoney;
    }

    public static int categoryReport(Timestamp startTimeStamp, Timestamp endTimeStamp, Category category) {
        int totalSum = 0;

        if (category == null) {
            System.out.println("No item is provided, please provide certain item.");
        } else {
            List<Item> itemList = category.getItems();
            Item itemElement;
            int totalMoney = 0;

            for (int i = 0; i < itemList.size(); i++) {
                itemElement = itemList.get(i);
                totalMoney = itemReport(startTimeStamp, endTimeStamp, itemElement);

                totalSum = totalSum + totalMoney;
            }
        }

        return totalSum;
    }

    public static List<Employee> getUserInfo() {
        List<Employee> empList = new ArrayList<>();

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection();
                Statement empStatement = connection.createStatement();
                Statement statusStatement = connection.createStatement()) {
            //Query the status description
            String query = "SELECT* From employee";
            String StatusQuery = "Select * From Status where sid =";
            String EmpTypeNameQuery = "SELECT * FROM employeetype where ETID = ";
            String stp, Etnp;

            ResultSet empResult = empStatement.executeQuery(query);
            ResultSet Statusresult;
            ResultSet EMPtypeResult;

            while (empResult.next()) {
                Employee empt = new Employee(empResult.getString("username"), empResult.getString("pword"));
                empt.setEID(empResult.getInt("EID"));
                empt.setETID(empResult.getInt("EmployeeTypeId"));
                empt.setFname(empResult.getString("fname"));
                empt.setLname(empResult.getString("lname"));
                empt.setPhone1(empResult.getString("phone1"));
                empt.setPhone2(empResult.getString("phone2"));
                empt.setSalary(empResult.getInt("salary"));

                stp = StatusQuery + empResult.getInt("StatusID");
                Statusresult = statusStatement.executeQuery(stp);
                while (Statusresult.next()) {
                    empt.setStatus(new Status(empResult.getInt("StatusID"), Statusresult.getString("description")));
                }

                Etnp = EmpTypeNameQuery + empResult.getInt("EmployeeTypeId");
                EMPtypeResult = statusStatement.executeQuery(Etnp);
                while (EMPtypeResult.next()) {
                    empt.setETname(EMPtypeResult.getString("name"));
                }
                empList.add(empt);
                System.out.println(empt);
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.exit(0);
        }
        db.destroy();
        return empList;
    }

    public static boolean isALLEmpoyeeHere(List<Employee> EL) {
        DBConnection db = new DBConnection();
        try (Connection connection = db.getMyConnection()) {
            Statement empStatement = connection.createStatement();
            String SQuery = "Select count(*) from Employee";
            ResultSet empResult = empStatement.executeQuery(SQuery);
            while (empResult.next()) {
                int ersul = empResult.getInt("count(*)");
                if (EL.isEmpty() && ersul > 0) {
                    return false;
                }

                //System.out.println(ersul);
                //System.out.println(EL.size());
                return ersul == EL.size();
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.exit(0);
        }
        return false;
    }

    public static boolean addEmployee(Employee e) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            try (Statement statement = connection.createStatement()) {
                String query = "INSERT INTO employee (fname,lname,employeeTypeId,userName,pword,statusID,phone1,phone2) "
                        + "VALUES('" + e.getFname() + "','"
                        + e.getLname() + "','"
                        + e.getETID() + "','"
                        + e.getUserName() + "','"
                        + e.getPassword() + "','"
                        + e.getStatus().getStatusID() + "','"
                        + e.getPhone1() + "','"
                        + e.getPhone2() + "')";

                statement.executeUpdate(query);
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    public static boolean updateEmployee(Employee e) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            try (Statement statement = connection.createStatement()) {
                String query = "UPDATE employee "
                        + "SET fname = '" + e.getFname() + "', "
                        + "lname = '" + e.getLname() + "', "
                        + "username = '" + e.getUserName() + "', "
                        + "pword = '" + e.getPassword() + "', "
                        + "statusID = " + e.getStatus().getStatusID() + ", "
                        + "phone1 = " + e.getPhone1() + ", "
                        + "phone2 = " + e.getPhone2() + ", "
                        + "employeeTypeId = " + e.getETID()
                        + " WHERE EID = " + e.getEID();
                statement.executeUpdate(query);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    public static boolean deleteEmployee(Employee e) {

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            try (Statement deleteStatement = connection.createStatement()) {
                String query = "DELETE FROM Employee "
                        + "WHERE EID=" + e.getEID();
                deleteStatement.executeUpdate(query);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    public static List<HistoryCategory> getHistoryCategoryList() {

        List<HistoryCategory> categoryHistoryList = new ArrayList<>();

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            //Query categories and insert them to categoryList
            try (Statement categoryHistoryStatement = connection.createStatement();
                    ResultSet categoryResult = categoryHistoryStatement.executeQuery("SELECT * FROM foodcategoryhistory;")) {
                while (categoryResult.next()) {
                    HistoryCategory c = new HistoryCategory();
                    c.setCID(categoryResult.getInt("FCHiD"));
                    c.setName(categoryResult.getString("name"));
                    c.setStartDate(categoryResult.getTimestamp("startdate"));
                    c.setEndDate(categoryResult.getTimestamp("enddate"));
                    c.setReason(categoryResult.getString("reason"));

                    categoryHistoryList.add(c);
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return categoryHistoryList;
    }

    public static List<HistoryItem> getHistoryItemList() {

        List<HistoryItem> itemHistoryList = new ArrayList<>();

        DBConnection db = new DBConnection();

        try (Connection connection = db.getMyConnection()) {
            //Query categories and insert them to categoryList
            try (Statement HistoryItemStatement = connection.createStatement();
                    ResultSet itemResult = HistoryItemStatement.executeQuery("SELECT * FROM fooditemHistory;")) {
                while (itemResult.next()) {
                    HistoryItem c = new HistoryItem();
                    c.setiID(itemResult.getInt("FIHiD"));
                    c.setName(itemResult.getString("name"));
                    c.setStartDate(itemResult.getTimestamp("startdate"));
                    c.setEndDate(itemResult.getTimestamp("enddate"));
                    c.setReason(itemResult.getString("reason"));
                    c.setCategory(itemResult.getString("category"));
                    c.setPrice(itemResult.getInt("price"));

                    itemHistoryList.add(c);
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return itemHistoryList;
    }
}
