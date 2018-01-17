/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import library.assistant.database.Database;

/**
 *
 * @author E5-473G
 */
public class BookDAO {

    public void saveBook(Book book) throws SQLException {
        Connection conn = Database.getInstance().getConnect();
        String insertSql = "insert into lbdb.books (id,title,author,publisher,is_available) values (?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(insertSql);
        stmt.setString(1, book.getId());
        stmt.setString(2, book.getTitle());
        stmt.setString(3, book.getAuthor());
        stmt.setString(4, book.getPublisher());
        stmt.setBoolean(5, book.isAvailable());
        stmt.execute();
    }

    public Book getBook(String id) throws SQLException {
        Connection conn = Database.getInstance().getConnect();
        String SelectSql = "select * from lbdb.books where id=?";

        PreparedStatement stmt = conn.prepareStatement(SelectSql);
        stmt.setString(1, id);
        ResultSet result = stmt.executeQuery();
        Book book = null;

        if (result.next()) {
            String title = result.getString("title");
            String author = result.getString("author");
            String publisher = result.getString("publisher");
            boolean available = result.getBoolean("is_available");

            book = new Book(id, title, author, publisher, available);

        }
        return book;
    }

    public List<Book> getBooks() throws SQLException {
        Connection conn = Database.getInstance().getConnect();
        List<Book> books = new ArrayList<>();
        String sql = "select * from lbdb.books";
        ResultSet result = null;

        Statement stmt = conn.createStatement();
        result = stmt.executeQuery(sql);

        while (result.next()) {
            String id = result.getString("id");
            String title = result.getString("title");
            String author = result.getString("author");
            String publisher = result.getString("publisher");
            boolean available = result.getBoolean("is_available");

            books.add(new Book(id, title, author, publisher, available));

        }
        return books;
    }

    public void updateBook(String id, boolean is_available) throws SQLException {
        Connection conn = Database.getInstance().getConnect();
       // System.out.println("ID:"+id);
        String updateSql = "update lbdb.books set is_available=? where id=?";
        PreparedStatement prestmt = conn.prepareStatement(updateSql);
        prestmt.setBoolean(1, is_available);
        prestmt.setString(2, id);
        prestmt.execute();

    }

    public void deleteBook(String id) throws SQLException {
        Connection conn = Database.getInstance().getConnect();

        String deleteSql = "delete from lbdb.books where id=?";
        PreparedStatement prestmt = conn.prepareStatement(deleteSql);
        prestmt.setString(1, id);
        prestmt.execute();

    }
}
