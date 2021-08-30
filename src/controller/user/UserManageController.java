/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.user;

import BAO.DepartmentManage;
import BAO.user.UserManagment;
import DAO.Department;
import DAO.patient.Patient;
import DAO.user.User;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class UserManageController implements Initializable {

    private ArrayList<Department> allDep;
    private ArrayList<User> allUser = new ArrayList<>();
    private final UserManagment USER_MANAGE = new UserManagment();
    private ContextMenu pop = new ContextMenu();
    private HashMap<String, User> UserMap = new HashMap<>();
    @FXML
    private Button clearBtn1;
    @FXML
    private JFXComboBox<String> comboDepartmet;
    @FXML
    private JFXTextField txtPassword;
    @FXML
    private JFXTextField txtUserName;
    @FXML
    private JFXTextField txtCPassword;
    @FXML
    private ListView<Label> AllPerList;
    @FXML
    private ListView<Label> UserPerList;
    @FXML
    private Button saveBtn;
    private User currentUser;
    @FXML
    private Label messageLable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            getData();
            setTextAuto();
        });
    }

    @FXML
    private void clear(ActionEvent event) {
        txtPassword.clear();
        txtCPassword.clear();
        txtUserName.clear();
        currentUser = null;
    }

    @FXML
    private void addPerToList(ActionEvent event) {
    }

    @FXML
    private void removePerFromList(ActionEvent event) {
    }

    @FXML
    private void save(ActionEvent event) {
        if (CheckDuplicate() && CheckFileds()) {
            try {
                User user = new User();
                user.setName(txtUserName.getText());
                user.setPassword(txtPassword.getText());
                user.setDept(getDeptId());
                if (user.getDept() != -1) {
                    user.setId(USER_MANAGE.addUser(user));
                    setMessage("Saved Success");
                }

            } catch (SQLException ex) {
                Logger.getLogger(UserManageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void getData() {
        try {
            allUser = USER_MANAGE.allUser();
            allDep = new DepartmentManage().getAllDepartMent();
            setCompoData();
        } catch (SQLException ex) {
            Logger.getLogger(UserManageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTextAuto() {
        txtUserName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (txtUserName.getText().equals("")) {
                if (pop.isShowing()) {
                    pop.hide();
                }
                return;
            }

            pop.getItems().clear();
            for (User r : allUser) {
                if (r.getName().startsWith(newValue, 0)) {

                    UserMap.put(r.getName(), r);
                    MenuItem m = new MenuItem(r.getName());
                    m.setStyle("-fx-pref-width:" + txtUserName.getWidth() + ";");
                    m.setOnAction((event) -> {
                        pop.hide();
                        currentUser = UserMap.get(m.getText());
                        setUserToUi();
                    });
                    pop.getItems().add(m);
                }

            }
            txtUserName.setContextMenu(pop);

            pop.show(txtUserName, Side.BOTTOM, 0, 0);
        });
    }

    private void setUserToUi() {
        txtUserName.setText(currentUser.getName());
    }

    private void setCompoData() {
        for (Department department : allDep) {
            comboDepartmet.getItems().add(department.getName());
        }
    }

    private boolean CheckDuplicate() {
        for (User user : allUser) {
            if (user.getName().equals(txtUserName.getText())) {
                setMessage("User is Duplicated ");
                return false;
            }
        }
        return true;
    }

    private boolean CheckFileds() {
        if (txtUserName.getText().isEmpty() || txtUserName.getText().equals("")) {
            setMessage("User Name is reqiured");
            return false;

        } else if (txtPassword.getText().isEmpty() || txtPassword.getText().equals("")) {
            setMessage("Password is reqiured");
            return false;

        } else if (txtCPassword.getText().isEmpty() || txtCPassword.getText().equals("")) {
            setMessage("Confirm Password is reqiured");
            return false;
        } else if (!txtPassword.getText().equals(txtCPassword.getText())) {
            setMessage("Password Did not match ");
            return false;
        }
        return true;
    }

    private int getDeptId() {
        for (Department dp : allDep) {
            if (dp.getName().equals(comboDepartmet.getSelectionModel().getSelectedItem())) {
                return dp.getId();
            }
        }
        setMessage("Please Select Department ! ");
        return -1;
    }

    private void setMessage(String text) {
        messageLable.setText(text);
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(ev -> {
            messageLable.setText("");
        });
        delay.play();
    }

}
