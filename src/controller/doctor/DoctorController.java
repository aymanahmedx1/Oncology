/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.doctor;

import BAO.CallManage;
import BAO.patient.MovmentManage;
import DAO.CallItem;
import DAO.LabOrder;
import DAO.patient.PatientMovement;
import DAO.user.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;
import static commons.Helpers.LAB_MANAGE;
import controller.actions.AddDrugController;
import controller.actions.AddLabTestController;
import controller.patient.PatFileController;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class DoctorController implements Initializable {

    private Stage currentStage;
    private User current;
    private static final MovmentManage MOVE = new MovmentManage();
    private ObservableList<PatientMovement> allPat;
    private ObservableList<PatientMovement> filterdPat = FXCollections.observableArrayList();
    private PatientMovement callSelected;
    private PatientMovement currentCallPat = null;
    private final CallManage CALL = new CallManage();
    @FXML
    private TextField doctorNameTxt;
    @FXML
    private Label currentPatientLbl;
    @FXML
    private Label finishedPatientLbl;
    @FXML
    private Label currentNameLbl;
    @FXML
    private TableView<PatientMovement> table;
    @FXML
    private TableColumn<PatientMovement, String> serviceTypeCOL;
    @FXML
    private TableColumn<PatientMovement, String> nameCOL;
    @FXML
    private TableColumn<PatientMovement, JFXToggleNode> idCOL;
    @FXML
    private TableColumn<PatientMovement, Integer> noCol;
    @FXML
    private TextField txtNameFilter;
    @FXML
    private JFXButton btnCall;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toate;
    @FXML
    private JFXButton search;
    @FXML
    private JFXButton search1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            doctorNameTxt.setText(current.getName());
            fromDate.setValue(LocalDate.now());
            toate.setValue(LocalDate.now());
            setDateConverter();
            setTableUi();
            getData();
            filterNameTxt();
            handelOnClose();
        });
    }

    @FXML
    private void refreshUi(ActionEvent event) {
        getData();
    }

    @FXML
    private void finishedPat(ActionEvent event) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/doctor/DoctorFinished.fxml"));
            root = loader.load();
            DoctorFinishedController manage = loader.getController();
            manage.setCurrent(current);
            Scene scene = new Scene(root);
            Stage st = new Stage();
            manage.setStage(st);
            st.setResizable(true);
            st.setTitle("Doctor Finished ");
            st.setScene(scene);
            st.initOwner(currentStage);
            st.initModality(Modality.APPLICATION_MODAL);
            st.showAndWait();
            getData();
        } catch (IOException e) {
        }

    }

    private void getData() {
        try {
            Date from = Date.valueOf(fromDate.getValue());
            Date to = Date.valueOf(toate.getValue());
            ArrayList<PatientMovement> temp = MOVE.getDoctorPatient(current, from, to);
            allPat = FXCollections.observableArrayList();
            for (PatientMovement patMove : temp) {
                if (patMove.getFinish() == PatientMovement.ADD_NEW || patMove.getFinish() == PatientMovement.LAB_ADD) {
                    allPat.add(patMove);
                }
            }
            int no = 1;
            for (PatientMovement p : allPat) {
                p.setName(p.getPatient().getName());
                p.setFile_no(p.getPatient().getPatId());
                p.setService(PatientMovement.dept.get(p.getServiceType()));
                p.setNo(no++);
            }
            table.setItems(allPat);
            int finished = MOVE.getDoctorFinishCount(current, PatientMovement.FINISH);
            int drugAdd = MOVE.getDoctorFinishCount(current, PatientMovement.DRUG_ADD);
            finishedPatientLbl.setText(String.valueOf(finished + drugAdd));
            currentPatientLbl.setText(String.valueOf(allPat.size()));

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public User getCurrent() {
        return current;
    }

    public void setCurrent(User current) {
        this.current = current;
    }

    public Stage getStage() {
        return currentStage;
    }

    public void setStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    private void setTableUi() {

        serviceTypeCOL.setCellValueFactory(new PropertyValueFactory<>("service"));
        nameCOL.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCOL.setCellValueFactory(new PropertyValueFactory<>("file_no"));
        noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
        table.setItems(allPat);
        table.setRowFactory((param) -> {
            return new TableRow<PatientMovement>() {
                @Override
                protected void updateItem(PatientMovement item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        switch (item.getServiceType()) {
                            case PatientMovement.SEVRICE_TYPE_LAB:
                                setStyle("-fx-background-color: #99ff99 ; ");
                                break;
                            case PatientMovement.SEVRICE_TYPE_ADD_LAB:
                                setStyle("-fx-background-color: pink ; ");
                                break;
                            default:
                                setStyle("");
                                break;
                        }
                    } else {
                        setStyle("");
                    }
                }

            }; //To change body of generated lambdas, choose Tools | Templates.
        });
    }

    private void addDrug(PatientMovement pat) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/actions/AddDrug.fxml"));
            root = loader.load();
            AddDrugController manage = loader.getController();
            manage.setCurrentPat(pat);
            manage.setCurrentUser(current);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(true);
            stage.setTitle("Add Drug ");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
            getData();
        } catch (IOException e) {
        }
    }

    private void callPat(PatientMovement pat) {

    }

    private void addLab(PatientMovement pat) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/actions/AddLabTest.fxml"));
            root = loader.load();
            AddLabTestController manage = loader.getController();
            manage.setCurrentPat(pat);
            manage.setCurrentUser(current);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Add Lab Test ");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            if (pat.getServiceType() == PatientMovement.SEVRICE_TYPE_LAB || pat.getServiceType() == PatientMovement.SEVRICE_TYPE_ADD_LAB) {
                LabOrder labOrder = LAB_MANAGE.getTodayLabOrder(pat);
                manage.setCurrentOrder(labOrder);
                manage.setUpdate(true);
            }
            stage.showAndWait();
            getData();
        } catch (IOException e) {
        } catch (SQLException ex) {
            Logger.getLogger(DoctorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void finish(PatientMovement pat) {
        try {
            MOVE.updateMovement(pat, PatientMovement.FINISH, PatientMovement.SEVRICE_TYPE_FINISH);
            getData();
        } catch (SQLException ex) {
            Logger.getLogger(DoctorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openFile(PatientMovement item, boolean lab) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patient/PatFile.fxml"));
            root = loader.load();
            PatFileController manage = loader.getController();
            manage.setCurrentPatient(item);
            manage.setCurrentUser(current);
            manage.setStage(currentStage);
            manage.setOpenLabTab(lab);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
//            stage.setResizable(false);
            stage.setTitle("Patient File ");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
            getData();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void openFile(ActionEvent event) {
        if (getSelected() != null) {
            openFile(getSelected(), false);
        }
    }

    @FXML
    private void openDrug(ActionEvent event) {
        if (getSelected() != null) {
            addDrug(getSelected());
        }
    }

    @FXML
    private void openLab(ActionEvent event) {
        if (getSelected() != null) {
            addLab(getSelected());
        }
    }

    @FXML
    private void callPat(ActionEvent event) {
        try {
            if (getSelected() == null) {
                deleteCall();
                currentCallPat = null;
                currentNameLbl.setText("");
                btnCall.setStyle("-fx-background-color:green;");
            } else {
                deleteCall();
                currentCallPat = getSelected();
                currentNameLbl.setText(currentCallPat.getPatient().getName());
                btnCall.setStyle("-fx-background-color:RED;");
                CallItem item = new CallItem();
                item.setPatId(currentCallPat.getPatient().getId());
                item.setDoctorId(current.getId());
                item.setState(CallItem.GO_To_DOCTOR);
                CALL.addDoctorCall(item);
            }
            table.getSelectionModel().clearSelection();
        } catch (SQLException ex) {
            Logger.getLogger(DoctorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void dischargePat(ActionEvent event) {
        if (getSelected() != null) {
            finish(getSelected());
        }
    }

    private PatientMovement getSelected() {
        return (table.getSelectionModel().getSelectedItem() != null) ? table.getSelectionModel().getSelectedItem() : null;
    }

    private void filterNameTxt() {
        txtNameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterdPat.clear();
            for (PatientMovement pat : allPat) {
                if (pat.getPatient().getName().startsWith(newValue)) {
                    filterdPat.add(pat);
                }
            }
            table.setItems(filterdPat);
        });
    }

    private void handelOnClose() {
        currentStage.setOnHidden((event) -> {
            if (currentCallPat != null) {
                deleteCall();
            }
        });
    }

    private void deleteCall() {
        try {
            CallItem item = new CallItem();
            item.setDoctorId(current.getId());
            CALL.deleteDoctorCall(item);
        } catch (SQLException ex) {
            Logger.getLogger(DoctorController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void openLabDetails(MouseEvent event) {
        if (event.getClickCount() == 2) {
            PatientMovement selected = table.getSelectionModel().getSelectedItem();
            if (selected.getServiceType() == PatientMovement.SEVRICE_TYPE_LAB) {
                openFile(selected, true);
            } else {
                openFile(selected, false);
            }
        }
    }

    @FXML
    private void searchBtn(ActionEvent event) {
        getData();
    }

    private void setDateConverter() {
        fromDate.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d-M-yyyy");

            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return dateFormatter.format(object);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }

        });
        toate.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d-M-yyyy");

            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return dateFormatter.format(object);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }

        });

    }

    @FXML
    private void toDay(ActionEvent event) {
        fromDate.setValue(LocalDate.now());
        toate.setValue(LocalDate.now());
        getData();
    }
}
