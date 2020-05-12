package org.example;

import java.sql.*;

public class App
{
    private static String connectionString = "jdbc:mysql://localhost:3306/?&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Berlin";
    private static String userName = "root";
    private static String userPassword = "root";

    public static void main(String[] args) {
        //executeCommandsToDatabase("USE world;");
        //executeCommandsToDatabase("SELECT * FROM world.city WHERE CountryCode = 'SWE';");
        //executeCommandsToDatabase("UPDATE world.city SET Name = 'Göteborg' WHERE ID = 3049;");
    }

    static void executeCommandsToDatabase(String commandsString) {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(connectionString, userName, userPassword);
            statement = connection.createStatement();

            connection.setAutoCommit(false);

            boolean isResult = statement.execute(commandsString);

            connection.commit();

            System.out.println("First command: " + (isResult ? "ResultSet" : "Update"));

            boolean isDone = false;
            while (!isDone) {
                if (isResult) {
                    rs = statement.getResultSet();

                    ResultSetMetaData metaData = rs.getMetaData();
                    int colCount = metaData.getColumnCount();

                    if (colCount > 0) {
                        for (int i = 1; i <= colCount; i++) {
                            System.out.print(metaData.getColumnLabel(i) + "\t\t");
                        }
                        System.out.println();
                        while (rs.next()) {
                            for (int i = 1; i <= colCount ; i++) {
                                System.out.print(rs.getString(i) + "\t\t");
                            }
                            System.out.println();
                        }
                    }

                }
                else {
                    int updateCount = statement.getUpdateCount();
                    if (updateCount >= 0) {
                        System.out.println("Update counter: " + updateCount);
                    }
                    else {
                        isDone = true;
                    }
                }

                if (!isDone) {
                    isResult = statement.getMoreResults();
                }
            }


        }
        catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {
            try {
                if (commandsString.equals("UPDATE world.city SET Name = 'Göteborg' WHERE ID = 3049;")) {
                    connection.rollback();// will not work if you haven't changed Auto-commit to false
                }
            } catch (SQLException throwables) {
                System.out.println("Rollback failed.");
                throwables.printStackTrace();
            }

            try {
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
