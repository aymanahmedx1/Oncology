/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.user;

import BAO.DepartmentManage;
import BAO.user.Authmanage;
import BAO.user.UserManagment;
import DAO.Department;
import DAO.user.Permisions;
import DAO.user.User;
import com.jfoenix.controls.JFXCheckBox;
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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class UserManageController implements Initializable {

    private ArrayList<Department> allDep;
    private ObservableList<User> allUser = FXCollections.observableArrayList();
    private final UserManagment USER_MANAGE = new UserManagment();
    private final Authmanage AUTH = new Authmanage();
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
    private Button saveBtn;
    private User currentUser;
    @FXML
    private Label messageLable;
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, Integer> colNo;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, User> colDept;
    private boolean edit;
    @FXML
    private JFXCheckBox chReception;
    @FXML
    private JFXCheckBox chBlack;
    @FXML
    private JFXCheckBox chDrugDose;
    @FXML
    private JFXCheckBox chPatManage;
    @FXML
    private JFXCheckBox chPatCost;
    @FXML
    private JFXCheckBox chSrearchForPat;
    @FXML
    private JFXCheckBox chSearchForDrug;
    @FXML
    private JFXCheckBox chDoctorScreen;
    @FXML
    private JFXCheckBox chLabScreen;
    @FXML
    private JFXCheckBox chPharmacyScreen;
    @FXML
    private JFXCheckBox scDooctor;
    @FXML
    private JFXCheckBox chDeathNote;
    @FXML
    private JFXCheckBox chLab;
    @FXML
    private JFXCheckBox chDrugManage;
    @FXML
    private JFXCheckBox chDiaManage;
    @FXML
    private JFXCheckBox chRegmanage;
    @FXML
    private JFXCheckBox chLabTest;
    @FXML
    private JFXCheckBox chClinicPH;
    @FXML
    private JFXCheckBox chCemoCheck;
    @FXML
    private JFXCheckBox chPrepareDrug;
    @FXML
    private JFXCheckBox chUserManage;
    @FXML
    private JFXCheckBox chPrefrances;
    @FXML
    private JFXCheckBox chIsAdmin;
    @FXML
    private JFXCheckBox chDeleteData;
    private Permisions currentAuthData;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            getData();
            setTableUi();
            setTextAuto();
            setCompoData();
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
    private void save(ActionEvent event) {
        try {
            if (edit && CheckFileds() && currentUser != null) {
                currentUser.setName(txtUserName.getText());
                currentUser.setPassword(txtPassword.getText());
                currentUser.setDept(getDeptId());
                USER_MANAGE.update(currentUser);
                setMessage("Updated Success");
                fillAuthData();
                edit = false;
                getData();

            } else if (CheckDuplicate() && CheckFileds()) {
                User user = new User();
                user.setName(txtUserName.getText());
                user.setPassword(txtPassword.getText());
                user.setDept(getDeptId());
                if (user.getDept() != -1) {
                    user.setId(USER_MANAGE.addUser(user));
                    setMessage("Saved Success");
                    getData();
                    AUTH.addPermissionForNewUser(user);
                    fillAuthData();
                    currentUser = user;
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getData() {
        try {
            allUser = FXCollections.observableArrayList(USER_MANAGE.allUser());
            table.setItems(allUser);
            allDep = new DepartmentManage().getAllDepartMent();
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

        for (Department department : allDep) {
            if (department.getId() == currentUser.getDept()) {
                comboDepartmet.getSelectionModel().select(department.getName());
                break;
            }
        }
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

    @FXML
    private void userEdit(MouseEvent event) {
        if (event.getClickCount() == 2) {
            edit = true;
            currentUser = table.getSelectionModel().getSelectedItem();
            setUserToUi();
            pop.hide();
        }
    }

    private void setTableUi() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDept.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        colDept.setCellFactory(param -> new TableCell<User, User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText("");
                    return;
                }
                setText(deptname(item.getDept()));

            }

        });
        table.setItems(allUser);
    }

    private String deptname(int id) {
        for (Department department : allDep) {
            if (department.getId() == id) {
                return department.getName();
            }
        }
        return "";
    }

    private void fillAuthData() {

    }
}
