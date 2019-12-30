package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection conn;

    private Database() {
        String url      = "jdbc:mariadb://localhost:3306/";
        String dbName   = "ModSimProject";
        String driver   = "org.mariadb.jdbc.Driver";
        String userName = "root";
        String password = "123";

        try {
            Class.forName( driver ).newInstance();
            this.conn = DriverManager.getConnection( url + dbName, userName, password );
        } catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }

    public static Database getInstance() {
        return Inner.connection;
    }

    public Connection getConn() {
        return conn;
    }

    private static class Inner {
        private static Database connection = new Database();
    }
}
