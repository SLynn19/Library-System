/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import library.assistant.model.Book;
import library.assistant.model.Member;

/**
 *
 * @author E5-473G
 */
public class Database {

    private Connection conn;

    private static Database db;
    private static String host;
    private static int port;
    private static String username;
    private static String pass;

    private Database() throws SQLException {
        connect();
        createDatabase();
        createTables();

    }

    public static Database getInstance() throws SQLException {
        if (db == null) {
            db = new Database();
        }
        return db;
    }

    public void connect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found.");
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadDatabaseConfig();
        
        conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/", username, pass);

    }

    public void createDatabase() throws SQLException {
        String createsql = "create database if not exists lbdb";

        Statement stmt = conn.createStatement();
        stmt.execute(createsql);

    }

    public void createTables() throws SQLException {
        String createbooksql = "create table if not exists lbdb.books (id varchar(255) primary key,title varchar(255),author varchar(255),publisher varchar(255),is_available boolean default true)";
        Statement stmt1 = conn.createStatement();
        stmt1.execute(createbooksql);

        String createmembersql = "create table if not exists lbdb.members( id varchar(255) primary key,name varchar(255),mobile varchar(255),address varchar(255))";
        Statement stmt2 = conn.createStatement();
        stmt2.execute(createmembersql);

        String creatIssueTableSql = "create table if not exists lbdb.issue(member_id varchar(255),book_id varchar(255),issue_date date,renew_count int,foreign key (member_id) references members(id),foreign key (book_id) references books(id)) ";
        Statement stmt3 = conn.createStatement();
        stmt3.execute(creatIssueTableSql);

    }

    public Connection getConnect() {
        return conn;
    }

    private void loadDatabaseConfig() {
        Preferences prefs = Preferences.userRoot().node("lb2db");
        host = prefs.get("host", "localhost");
        username = prefs.get("user", "root");
        pass = prefs.get("pass", "");
        port = prefs.getInt("port", 3306);
    }

}
