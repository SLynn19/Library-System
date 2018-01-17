/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import library.assistant.database.Database;
import library.assistant.model.Book;
import library.assistant.model.BookDAO;

/**
 * FXML Controller class
 *
 * @author E5-473G
 */
public class BooklistController implements Initializable {

    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> idColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    private BookDAO bookDAO;
    @FXML
    private MenuItem deleteItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bookDAO = new BookDAO();
        intitColumn();
        loadData();

    }

    private void intitColumn() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
    }

    private void loadData() {

        try {
            List<Book> books = bookDAO.getBooks();
            bookTable.getItems().setAll(books);
        } catch (SQLException ex) {
            Logger.getLogger(BooklistController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void deletebook(ActionEvent event) {
        Book book = bookTable.getSelectionModel().getSelectedItem();
        if (book == null) {
            System.out.println("Plz select the book that want to delete.");
            return;
        }
        try {
            bookDAO.deleteBook(book.getId());
            bookTable.getItems().remove(book);
        } catch (SQLException ex) {
            System.out.println("Book wasn't successfully deleted.");
            Logger.getLogger(BooklistController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
