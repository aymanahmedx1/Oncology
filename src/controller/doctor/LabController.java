/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.doctor;

import DAO.LabOrder;
import DAO.patient.PatientMovement;
import com.jfoenix.controls.JFXToggleNode;
import commons.Helpers;
import static commons.Helpers.LAB_MANAGE;
import controller.user.MainWindowController;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class LabController implements Initializable {

    private ObservableList<LabOrder> allLabOrders;
    private ObservableList<LabOrder> filterdOrder = FXCollections.observableArrayList();
//    private ToggleGroup sampleGroup;
//    private ToggleGroup resultGroup;

    @FXML
    private Label todayDateLbl;
    @FXML
    private Label currentPatCount;
    @FXML
    private Label finishPatCount;
    @FXML
    private TableView<LabOrder> table;
    @FXML
    private TableColumn<LabOrder, String> nameCol;
    @FXML
    private TableColumn<LabOrder, String> idCol;
    @FXML
    private TableColumn<LabOrder, String> doctorCol;
    @FXML
    private TableColumn<LabOrder, LabOrder> addResultCol;
    @FXML
    private TableColumn<LabOrder, LabOrder> callCol;
    @FXML
    private TableColumn<LabOrder, LabOrder> resultCall;
    @FXML
    private Label messageLabel1;
    @FXML
    private TextField filter;
    @FXML
    private TableColumn<LabOrder, Integer> colNo;
    @FXML
    private TextField txtDoctorFilter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setTableUi();
            getData();
            setDate();
            textFilter();
        });
    }

    @FXML
    private void refresh(ActionEvent event) {
    }

    @FXML
    private void patFinish(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/doctor/LabFinished.fxml"));
            root = loader.load();
            LabFinishedController manage = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            manage.setStage(stage);
            stage.setTitle("Lab Finish ");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getData() {
        try {
            allLabOrders = FXCollections.observableArrayList(LAB_MANAGE.getLabOrders(PatientMovement.ADD_NEW));
            table.setItems(allLabOrders);
            finishPatCount.setText(String.valueOf(LAB_MANAGE.getFinishedLabOrder()));
            currentPatCount.setText(String.valueOf(allLabOrders.size()));
        } catch (SQLException ex) {
            Logger.getLogger(LabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTableUi() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("patName"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("patFileId"));
        doctorCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));

        addResultCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );

        addResultCol.setCellFactory(param -> new TableCell<LabOrder, LabOrder>() {
            Button btn = new Button("Add Result");

            @Override
            protected void updateItem(LabOrder item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btn);
                btn.getStyleClass().add("AddDrugBtn");
                btn.setPrefWidth(Double.MAX_VALUE);
                btn.setPrefHeight(40);
                btn.setOnAction(
                        event -> {
                            openVisitDetials(event, item);
                        });
            }

        });

        callCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );

        callCol.setCellFactory(param -> new TableCell<LabOrder, LabOrder>() {
            JFXToggleNode btn = new JFXToggleNode("Call");

            @Override
            protected void updateItem(LabOrder item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setGraphic(null);
                    return;
                }
                if (item.getCall() == LabOrder.CALL_SAMPLE) {
                    btn.setSelected(true);
                } else {
                    btn.setSelected(false);
                }
                btn.getStyleClass().add("callBtn");
                btn.setPrefWidth(Double.MAX_VALUE);
                btn.setPrefHeight(40);
                btn.setSelectedColor(Color.RED);
                btn.setUnSelectedColor(Color.GREEN);
                setGraphic(btn);
                btn.getStyleClass().add("AddDrugBtn");
                btn.setPrefWidth(Double.MAX_VALUE);
                btn.setPrefHeight(40);
                btn.setOnAction(event -> {
                    if (btn.isSelected()) {
                        item.setCall(LabOrder.CALL_SAMPLE);
                        LAB_MANAGE.updateCallState(item);
                    } else {
                        item.setCall(LabOrder.NOT_CALL);
                        LAB_MANAGE.updateCallState(item);
                    }
                    getData();
                });
            }
        });
        resultCall.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );

        resultCall.setCellFactory(param -> new TableCell<LabOrder, LabOrder>() {
            JFXToggleNode btn = new JFXToggleNode("Call");

            @Override
            protected void updateItem(LabOrder item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setGraphic(null);
                    return;
                }
                if (item.getCall() == LabOrder.CALL_RESULT) {
                    btn.setSelected(true);
                } else {
                    btn.setSelected(false);
                }
                btn.getStyleClass().add("callBtn");
                btn.setPrefWidth(Double.MAX_VALUE);
                btn.setPrefHeight(40);
                btn.setSelectedColor(Color.RED);
                btn.setUnSelectedColor(Color.GREEN);
//                if (!sampleGroup.getToggles().contains(btn)) {
//                    resultGroup.getToggles().add(btn);
//                }
                setGraphic(btn);
                btn.getStyleClass().add("AddDrugBtn");
                btn.setPrefWidth(Double.MAX_VALUE);
                btn.setPrefHeight(40);
                btn.setOnAction(event -> {
                    if (btn.isSelected()) {
                        item.setCall(LabOrder.CALL_RESULT);
                        LAB_MANAGE.updateCallState(item);
                    } else {
                        item.setCall(LabOrder.NOT_CALL);
                        LAB_MANAGE.updateCallState(item);
                    }
                    getData();
                });
            }
        });

        table.setItems(allLabOrders);
    }

    private void setDate() {
        todayDateLbl.setText(Date.valueOf(LocalDate.now()) + "");
    }

    private void openVisitDetials(ActionEvent event, LabOrder order) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/doctor/ViewTestDetails.fxml"));
            root = loader.load();
            ViewTestDetailsController manage = loader.getController();
            manage.setCurrentOrder(order);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            manage.setStage(stage);
            stage.setTitle(" Order Details ");
//            stage.getIcons().add(new Image("/com/zsalse/icons/LogInIcon.jpg"));
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
            if (manage.isResultAdd()) {
                setMessage(" Result Added ");
                getData();
            }
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sentToDoctor(LabOrder item) {
        try {
            PatientMovement pat = new PatientMovement();
            pat.setId(item.getMovementNo());

            if (Helpers.PRES_MANAGE.isPatHasPrescToday(item.getPatId())) {
                Helpers.MOVEMENT.updateMovement(pat, PatientMovement.DRUG_ADD, PatientMovement.SEVRICE_TYPE_DRUG);
            } else {
                Helpers.MOVEMENT.updateMovement(pat, PatientMovement.LAB_ADD, PatientMovement.SEVRICE_TYPE_LAB);
            }
            LAB_MANAGE.updateTestState(item, PatientMovement.FINISH);
            LAB_MANAGE.updateLabOrderDetailsState(item);
            getData();
            setMessage("Sent To Doctor Success");

        } catch (SQLException ex) {
            Logger.getLogger(LabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setMessage(String text) {
        messageLabel1.setText(text);
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(ev -> {
            messageLabel1.setText("");
        });
        delay.play();
    }

    private void textFilter() {
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterdOrder.clear();
            for (LabOrder pat : allLabOrders) {
                if (pat.getPatName().startsWith(newValue, 0)) {
                    filterdOrder.add(pat);
                }
            }
            table.setItems(filterdOrder);
        });
        txtDoctorFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterdOrder.clear();
            for (LabOrder pat : allLabOrders) {
                if (pat.getDoctorName().startsWith(newValue, 0)) {
                    filterdOrder.add(pat);
                }
            }
            table.setItems(filterdOrder);
        });

    }
}
