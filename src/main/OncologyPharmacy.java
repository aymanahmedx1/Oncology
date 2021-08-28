/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.user.DBConnectionController;
import controller.user.LogInController;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Ayman
 */
public class OncologyPharmacy extends Application {

    private String fileName = "jdbc.properties";
    private Properties prop = new Properties();

    @Override
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            if (checkDbConnection(stage)) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/view/user/LogIn.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.getIcons().add(new Image("/images/logo.jpg"));
                    stage.setTitle("Log In");
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(OncologyPharmacy.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {

            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private boolean checkDbConnection(Stage stage) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        } else {
            setDatabaseData(stage);
        }
        return false;
    }

    private void setDatabaseData(Stage stage) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/DBConnection.fxml"));
            root = loader.load();
            DBConnectionController manage = loader.getController();
            manage.setStage(stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getIcons().add(new Image("/images/logo.jpg"));
            stage.setTitle("Database Connection");
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(OncologyPharmacy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
