/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.user;

import com.jfoenix.controls.JFXButton;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class DBConnectionController implements Initializable {

    private Stage stage;
    private String fileName = "commons/jdbc.properties";
    private Properties prop = new Properties();

    @FXML
    private JFXButton btnCheckConn;
    @FXML
    private TextField txtDbName;
    @FXML
    private TextField txPassword;
    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtServer;
    boolean isOk = false;
    @FXML
    private JFXButton btnSave;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            txtDbName.requestFocus();
            txtDbName.setText("oncology");
            txtServer.setText("localhost");
            txtUserName.setText("root");
            txPassword.setText("toor");
        });
    }

    @FXML
    private void save(ActionEvent event) {

        if (isOk) {
            try {
                String s = "jdbc:mysql://" + txtServer.getText() + ":3306/" + txtDbName.getText();
                prop.setProperty("url", s);
                prop.setProperty("password", txPassword.getText());
                prop.setProperty("username", txtUserName.getText());
                prop.store(new FileOutputStream("jdbc.properties"), null);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Database Information Saved Success ! \n Please Restart Application ", ButtonType.OK);
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get().equals(ButtonType.OK)) {
                    stage.close();
                }
            } catch (FileNotFoundException ex) {
                showAlert(ex.getMessage() );
            } catch (IOException ex) {
                showAlert(ex.getMessage()+ "Error");
            }
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        stage.hide();
    }

    @FXML
    private void checkConn(ActionEvent event) {
        try {
            FileOutputStream fr = null;
            Connection conn = null;
            conn = DriverManager.getConnection("jdbc:mysql://" + txtServer.getText() + ":3306/" + txtDbName.getText(), txtUserName.getText(), txPassword.getText());
            if (conn != null) {
                isOk = true;
                btnSave.setDisable(false);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Connection is Ok ", ButtonType.OK);
                alert.show();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnectionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void allListn() {
        txtDbName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                isOk = false;
                btnSave.setDisable(true);
            }
        });
        txtServer.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                isOk = false;
                btnSave.setDisable(true);
            }
        });
        txtUserName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                isOk = false;
                btnSave.setDisable(true);
            }
        });
        txPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                isOk = false;
                btnSave.setDisable(true);
            }
        });
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void showAlert(String mess) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mess, ButtonType.OK);
        alert.show();
    }
}
