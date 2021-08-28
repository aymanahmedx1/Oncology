/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.user;

import BAO.user.UserManagment;
import DAO.user.User;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import commons.NewExcelExport;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class LogInController implements Initializable {

    private UserManagment um = new UserManagment();
    private ArrayList<User> allUsers = new ArrayList<>();

    private User currentuser;
    @FXML
    private AnchorPane pane;
    @FXML
    private JFXTextField userTxt;
    @FXML
    private JFXPasswordField passwordTxt;
    @FXML
    private Label messageLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAllUser();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userTxt.requestFocus();
            }

        });
    }

    @FXML
    private void logIn(ActionEvent event) {

        if (validateData()) {
            String userName = userTxt.getText();
            String password = passwordTxt.getText();
            if (!allUsers.isEmpty()) {
                for (User user : allUsers) {
                    if (user.getName().equals(userName) && user.getPassword().equals(password)) {
                        currentuser = user;
                        doLogIn(event);
                        return;
                    }
                }
                Alert alert = new Alert(Alert.AlertType.ERROR, "User or password not correct", ButtonType.OK);
                alert.show();
                userTxt.requestFocus();
                userTxt.clear();
                passwordTxt.clear();
            }
        } else {
            if (userTxt.getText().trim().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please Enter User Name", ButtonType.OK);
                alert.show();
                userTxt.requestFocus();
            } else if (passwordTxt.getText().trim().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please Enter Password", ButtonType.OK);
                alert.show();
                passwordTxt.requestFocus();
            }
        }
    }

    @FXML
    private void keyShort(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
        if (event.getCode() == KeyCode.ENTER) {
            ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
            logIn(ae);
            event.consume();
        }

    }

    private void doLogIn(ActionEvent event) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/MainWindow.fxml"));
            root = loader.load();
            MainWindowController manage = loader.getController();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            manage.setCurrentUser(currentuser);
            stage.setTitle("Oncology Pharmacy");
            stage.setMaximized(true);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void getAllUser() {
        try {
            allUsers = um.allUser();
        } catch (SQLException ex) {
            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean validateData() {
        if (userTxt.getText().trim().equals("")) {
            return false;
        }
        return !passwordTxt.getText().trim().equals("");
    }

}
