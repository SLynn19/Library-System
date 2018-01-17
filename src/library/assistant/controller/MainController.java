/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.model.Book;
import library.assistant.model.BookDAO;
import library.assistant.model.IssueDAO;
import library.assistant.model.IssueInfo;
import library.assistant.model.Member;
import library.assistant.model.MemberDAO;

/**
 *
 * @author E5-473G
 */
public class MainController implements Initializable {

    @FXML
    private Button newBookBtn;
    @FXML
    private Button newMemberBtn;
    @FXML
    private Button booklistBtn;
    @FXML
    private Button memberlistBtn1;
    @FXML
    private JFXTextField bookIDSearchField;
    @FXML
    private Text titleText;
    @FXML
    private Text authorText;

    @FXML
    private JFXTextField memberIDSearchField;

    private BookDAO bookDAO;
    private MemberDAO memberDAO;

    @FXML
    private Text nameText;
    @FXML
    private Text mobileText;
    @FXML
    private Text addressText;
    @FXML
    private JFXButton issueBtn;
    @FXML
    private Text availableText;

    private IssueDAO issueDAO;
    @FXML
    private JFXTextField bookIdField;
    @FXML
    private Text mNameText;
    @FXML
    private Text mMobileText;
    @FXML
    private Text mAddressText;
    @FXML
    private Text bTitleText;
    @FXML
    private Text bAuthorText;
    @FXML
    private Text bPublisherText;
    @FXML
    private Text issueDateText;
    @FXML
    private Text renewCountText;
    @FXML
    private JFXButton submissionBtn;
    @FXML
    private JFXButton renewBtn;
    @FXML
    private MenuItem closeBtn;
    @FXML
    private MenuItem dbPreferences;

    private void handleButtonAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();
        issueDAO = new IssueDAO();
    }

    @FXML
    private void loadNewBookWindow(ActionEvent event) throws IOException {
        loadWindow("New Book", "/library/assistant/view/newbook.fxml");
    }

    @FXML
    private void loadNewMemberWindow(ActionEvent event) throws IOException {
        loadWindow("New Member", "/library/assistant/view/newmember.fxml");
    }

    @FXML
    private void loadBookListWindow(ActionEvent event) throws IOException {
        loadWindow("Book List", "/library/assistant/view/booklist.fxml");
    }

    private void loadWindow(String title, String url) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initOwner(newBookBtn.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void loadMemberListWindow(ActionEvent event) throws IOException {
        loadWindow("Member List", "/library/assistant/view/memberlist.fxml");
    }
     @FXML
    private void startPreferencesConfiWindow(ActionEvent event) throws IOException {
         loadWindow("Database Preferences", "/library/assistant/view/server.fxml");
    }

    @FXML
    private void searchBookInfo(ActionEvent event) throws SQLException {

        clearBookCache();

        String bookID = bookIDSearchField.getText();

        if (bookID.isEmpty()) {
            System.out.println("Plz enter book ID first.");
        }
        try {
            Book book = bookDAO.getBook(bookID);
            if (book != null) {
                titleText.setText(book.getTitle());
                authorText.setText(book.getAuthor());
                String availableStr = (book.isAvailable() ? "Available" : "Not Available");
                availableText.setText(availableStr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void clearBookCache() {

        titleText.setText("-");
        authorText.setText("-");
        availableText.setText("-");
    }

    @FXML
    private void searchMemberInfo(ActionEvent event) {

        clearMemberCache();

        String memberID = memberIDSearchField.getText();

        if (memberID.isEmpty()) {
            System.out.println("Plz enter member ID first.");
        }
        try {
            Member member = memberDAO.getMember(memberID);
            if (member != null) {
                nameText.setText(member.getName());
                mobileText.setText(member.getMobile());
                addressText.setText(member.getAddress());

            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void clearMemberCache() {

        nameText.setText("-");
        mobileText.setText("-");
        addressText.setText("-");
    }

    @FXML
    private void issueBook(ActionEvent event) {
        String memberId = memberIDSearchField.getText();
        String bookId = bookIDSearchField.getText();
        if (memberId.isEmpty() || bookId.isEmpty()) {
            System.out.println("Plz enter member id and book id first.");
            return;
        }

        try {
            if (issueDAO.checkBookAvailable(bookId)) {
                System.out.println("This book was already issued.");
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            bookDAO.updateBook(bookId, false);
            issueDAO.saveIssueInfo(memberId, bookId);
            System.out.println("Book was successfully issued.");
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void bookIssuedInfo(ActionEvent event) {
        ClearIssuedInfo();
        String bookId = bookIdField.getText();

        if (bookId.isEmpty()) {
            System.out.println("Plz enter book ID first.");
        }
        try {

            IssueInfo issueInfo = issueDAO.searchIssueInfo(bookId);
            if (issueInfo != null) {

                Book book = bookDAO.getBook(issueInfo.getBookId());
                bTitleText.setText(book.getTitle());
                bAuthorText.setText(book.getAuthor());
                bPublisherText.setText(book.getPublisher());

                Member member = memberDAO.getMember(issueInfo.getMemberId());
                mNameText.setText(member.getName());
                mMobileText.setText(member.getMobile());
                mAddressText.setText(member.getAddress());

                SimpleDateFormat dateFormate = new SimpleDateFormat("dd/MM/YYYY , E");
                String dateStr = dateFormate.format(issueInfo.getIssueDate());
                issueDateText.setText("Issue Date:" + dateStr);
                renewCountText.setText("Renew Count:" + issueInfo.getRenewCount());
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ClearIssuedInfo() {
        bTitleText.setText("-");
        bAuthorText.setText("-");
        bPublisherText.setText("-");
        mNameText.setText("-");
        mMobileText.setText("-");
        mAddressText.setText("-");
        issueDateText.setText("-");
        renewCountText.setText("-");

    }

    @FXML
    private void startSubmissionBook(ActionEvent event) {

        String bookId = bookIdField.getText();
        try {
            issueDAO.deleteIssuedBook(bookId);
            bookDAO.updateBook(bookId, true);
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void renewBook(ActionEvent event) {
        String bookId = bookIdField.getText();
        try {
            issueDAO.updateRenewBook(bookId);
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void closeMenuItem(ActionEvent event) throws IOException {

        Stage stage = (Stage) newBookBtn.getScene().getWindow();
        stage.close();

    }

   
}
