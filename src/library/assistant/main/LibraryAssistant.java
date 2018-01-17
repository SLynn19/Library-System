/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.main;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import library.assistant.database.Database;
import library.assistant.util.MessageBox;

/**
 *
 * @author E5-473G
 */
public class LibraryAssistant extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Database db = Database.getInstance();
        } catch (SQLException e) {
            MessageBox.showAndWaitMessage("Failed", "Can't connect to database,plz check the server Configuration");

        }

        Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/view/main.fxml"));
        System.out.println("hey!");
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
