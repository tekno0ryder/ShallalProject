/*
 * MyDBConnection.java
 *
 * Created on 2005/01/16, 10:50
 */
package shallalProject;

import java.sql.*;

/**
 *
 * @author noniko
 */
public class DBConnection {

    private Connection myConnection;

    /**
     * Creates a new instance of MyDBConnection
     */
    public DBConnection() {

    }

    public void init() {

        try {

            Class.forName("com.mysql.jdbc.Driver");
            myConnection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/shalalProject",
                    "root", "root"
            );
        } catch (Exception e) {
            System.out.println("Failed to get connection");
            e.printStackTrace();
        }
    }

    public Connection getMyConnection() {
        return myConnection;
    }

    public void close(ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
            }

        }
    }

    public void close(java.sql.Statement stmt) {

        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
            }

        }
    }

    public void destroy() {

        if (myConnection != null) {

            try {
                myConnection.close();
            } catch (Exception e) {
            }

        }
    }

}
