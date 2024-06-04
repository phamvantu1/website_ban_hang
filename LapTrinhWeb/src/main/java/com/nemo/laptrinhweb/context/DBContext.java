package com.nemo.laptrinhweb.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {

    private static Connection conn;
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyBanGiay1;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "1234567";

    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//            System.out.println("Success");
        } catch (Exception e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
        return conn;
    }

//    public Connection getConnection()throws Exception {
//        String url = "jdbc:sqlserver://"+serverName+":"+portNumber + "\\" + instance +";databaseName="+dbName+";integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
//        if(instance == null || instance.trim().isEmpty())
//            url = "jdbc:sqlserver://"+serverName+":"+portNumber +";databaseName="+dbName+";integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        return DriverManager.getConnection(url, userID, password);
//    }   
//    /*Insert your other code right after this comment*/
//    /*Change/update information of your database connection, DO NOT change name of instance variables in this class*/
//    private final String serverName = "NEMO";
//    private final String dbName = "QuanLyBanGiay";
//    private final String portNumber = "1433";
//    private final String instance="";//LEAVE THIS ONE EMPTY IF YOUR SQL IS A SINGLE INSTANCE
//    private final String userID = "sa";
//    private final String password = "22012003";
}
