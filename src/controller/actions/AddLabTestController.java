/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.actions;

import BAO.LabGroupBAO;
import BAO.labTestManage;
import DAO.LabGroup;
import DAO.LabOrder;
import DAO.LabOrderDetail;
import DAO.LabTest;
import DAO.LabVisit;
import DAO.patient.PatientMovement;
import DAO.user.User;
import com.jfoenix.controls.JFXListView;
import commons.Helpers;
import static commons.Helpers.LAB_MANAGE;
import controller.doctor.ViewTestDetailsController;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class AddLabTestController implements Initializable {

//    private static final LabOrderManage LAB_MANAGE = new LabOrderManage();
//    private static final MovmentManage MOVE = new MovmentManage();
    private final LabGroupBAO LAB_GROUP_MANAGE = new LabGroupBAO();
    private ArrayList<LabTest> allTest;
    private PatientMovement currentPat;
    private ArrayList<LabVisit> orderDetails;
    private ArrayList<LabGroup> AllGroups;
    private ObservableList<LabGroup> labGroupTable;
    private ArrayList<LabTest> choosedTest = new ArrayList<>();
    private ObservableList<LabTest> tableData = FXCollections.observableArrayList();
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField idTxt;
    @FXML
    private TextField diaTxt;
    @FXML
    private TextField noteTxt;
    @FXML
    private TableView<LabTest> table;
    @FXML
    private TableColumn<LabTest, String> tName;
    @FXML
    private TableView<LabGroup> catTable;
    @FXML
    private TableColumn<LabGroup, String> colCatName;
    @FXML
    private TextField txtCategoryFilter;
    @FXML
    private FlowPane mainFlowPane;
    private LabOrder currentOrder;
    private boolean update = false;
    private User currentUser;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setTableUI();
            getData();
            setPat();
            categoryTxtFilter();
            if (update) {
                setOldVisit();
            }
        });
    }

    private void getData() {
        try {
            AllGroups = LAB_GROUP_MANAGE.AllLabGroup();
            labGroupTable = FXCollections.observableArrayList(AllGroups);
            allTest = getCheckBoxes(new labTestManage().allLabTest());
            catTable.setItems(labGroupTable);
        } catch (SQLException ex) {
            Logger.getLogger(AddLabTestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.hide();
    }

    @FXML
    private void saveByEnter(KeyEvent event) {
    }

    @FXML
    private void sendToLab(ActionEvent event) {
        try {
            if (update) {
                Helpers.LAB_MANAGE.deleteOrderItems(currentOrder, LabOrder.STILL);
                ArrayList<LabOrderDetail> labOrderDetails = new ArrayList<>();
                for (LabTest test : tableData) {
                    LabOrderDetail lb = new LabOrderDetail();
                    lb.setOrder(currentOrder);
                    lb.setTest(test.getTestId());
                    labOrderDetails.add(lb);
                }
                for (LabOrderDetail lbDetail : labOrderDetails) {
                    Helpers.LAB_MANAGE.addOrderDetails(lbDetail);
                }
                LAB_MANAGE.updateTestState(currentOrder, PatientMovement.ADD_NEW);
            } else {
                LabOrder order = new LabOrder();
                order.setPatId(currentPat.getPatient().getId());
                order.setDoctor(currentUser.getId());
                order.setNote(noteTxt.getText());
                order.setMovementNo(currentPat.getId());
                order = Helpers.LAB_MANAGE.addOrder(order);
                ArrayList<LabOrderDetail> labOrderDetails = new ArrayList<>();
                for (LabTest test : tableData) {
                    LabOrderDetail lb = new LabOrderDetail();
                    lb.setOrder(order);
                    lb.setTest(test.getTestId());
                    labOrderDetails.add(lb);
                }
                for (LabOrderDetail lbDetail : labOrderDetails) {
                    Helpers.LAB_MANAGE.addOrderDetails(lbDetail);
                }
                if (Helpers.PRES_MANAGE.isPatHasPrescToday(currentPat.getPatient().getId())) {
                    Helpers.MOVEMENT.updateMovement(currentPat, PatientMovement.DRUG_ADD, PatientMovement.SEVRICE_TYPE_ADD_LAB);
                } else {
                    Helpers.MOVEMENT.updateMovement(currentPat, PatientMovement.LAB_ADD, PatientMovement.SEVRICE_TYPE_ADD_LAB);
                }
            }
            closeWindow(event);
        } catch (SQLException ex) {
            Logger.getLogger(AddLabTestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PatientMovement getCurrentPat() {
        return currentPat;
    }

    public void setCurrentPat(PatientMovement currentPat) {
        this.currentPat = currentPat;
    }

    private void setPat() {
        nameTxt.setText(currentPat.getPatient().getName());
        idTxt.setText(currentPat.getPatient().getPatId());
        diaTxt.setText(currentPat.getPatient().getDiagnosis().getName());
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private void addTestToList(LabTest test) {
        tableData.add(test);
    }

    private void removeTestFromList(LabTest test) {
        tableData.remove(test);
    }

    private void setTableUI() {
        tName.setCellValueFactory(new PropertyValueFactory<>("testName"));
        colCatName.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.setPlaceholder(new Label(" Not Thing Choosed Yet !"));
        table.setItems(tableData);
        catTable.setItems(labGroupTable);
    }

    @FXML
    private void reomoveItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            LabTest selected = table.getSelectionModel().getSelectedItem();
            tableData.remove(selected);
            selected.setSelected(false);
        }
    }

    public void setOldVisit() {
        try {
            orderDetails = Helpers.LAB_MANAGE.getOrderDetails(currentOrder, LabOrder.STILL);
            for (LabVisit order : orderDetails) {
                for (LabTest labTest : allTest) {
                    if (labTest.getTestName().equals(order.getTestName())) {
                        labTest.setSelected(true);
                        addTestToList(labTest);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ViewTestDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public LabOrder getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(LabOrder currentOrder) {
        this.currentOrder = currentOrder;
    }

    private ArrayList<LabTest> getCheckBoxes(ArrayList<LabTest> allTest) {
        ArrayList<LabTest> checkBoxes = new ArrayList<>();
        for (LabTest test : allTest) {
            test.setText(test.getTestName());
            test.getStyleClass().add("radio");
            test.setSelectedColor(Color.RED);
            test.setUnSelectedColor(Color.BLUE);
            test.setOnAction((event) -> {
                if (test.isSelected()) {
                    addTestToList(test);
                } else {
                    removeTestFromList(test);
                }
            });
            checkBoxes.add(test);
        }
        return checkBoxes;
    }

    @FXML
    private void categoryChange(MouseEvent event) {
        if (catTable.getSelectionModel() != null) {
            LabGroup selected = catTable.getSelectionModel().getSelectedItem();
            mainFlowPane.getChildren().clear();
            for (LabTest labTest : allTest) {
                if (labTest.getGroup() == selected.getId()) {
                    mainFlowPane.getChildren().add(labTest);
                }
            }
        }
    }

    private void categoryTxtFilter() {
        txtCategoryFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                labGroupTable.clear();
                for (LabGroup group : AllGroups) {
                    if (group.getName().toLowerCase().startsWith(newValue.toLowerCase())) {
                        labGroupTable.add(group);
                    }
                }
                catTable.setItems(labGroupTable);
            } else {
                labGroupTable = FXCollections.observableArrayList(AllGroups);
                catTable.setItems(labGroupTable);
            }
        });
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

}
