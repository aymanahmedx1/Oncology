/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.doctor;

import BAO.patient.MovmentManage;
import DAO.Prescription;
import DAO.patient.PatientMovement;
import DAO.user.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controller.actions.AddDrugController;
import controller.actions.AddLabTestController;
import controller.patient.PatFileController;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
public class DoctorFinishedController implements Initializable {

    private int currentStatus = PatientMovement.DRUG_ADD;
    private Stage stage;
    private User current;
    private ObservableList<PatientMovement> allPat;
    private ObservableList<PatientMovement> filterdPat = FXCollections.observableArrayList();
    private static final MovmentManage MOVEMENT = new MovmentManage();
    final ToggleGroup group = new ToggleGroup();
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toate;
    @FXML
    private JFXButton search;
    @FXML
    private JFXTextField filterTxt;
    @FXML
    private Label countLabel;
    @FXML
    private JFXButton printBtn;
    @FXML
    private TableView<PatientMovement> table;
    @FXML
    private TableColumn<PatientMovement, String> nameCol;
    @FXML
    private TableColumn<PatientMovement, String> idCol;
    @FXML
    private TableColumn<PatientMovement, Date> dateCol;
    @FXML
    private TableColumn<PatientMovement, Integer> noCol;
    @FXML
    private RadioButton finished;
    @FXML
    private RadioButton drug;
    @FXML
    private JFXButton btnOpenDrug;
    @FXML
    private JFXButton btnOpenLab;
    @FXML
    private TextField textNameFilter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setTableUi();
            fromDate.setValue(LocalDate.now());
            toate.setValue(LocalDate.now());
            setDateConverter();
            getData();
            textFilter();
            filterTxt.requestFocus();
            drug.setToggleGroup(group);
            finished.setToggleGroup(group);
            drug.setSelected(true);
            btnOpenDrug.setDisable(true);
            btnOpenLab.setDisable(true);
        });
    }

    private void setTableUi() {

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("file_no"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
        table.setItems(filterdPat);
    }

    private void getData() {
        filterdPat.clear();
        Date from = Date.valueOf(fromDate.getValue());
        Date to = Date.valueOf(toate.getValue());
        try {
            int no = 1;
            allPat = FXCollections.observableArrayList(MOVEMENT.getDoctorFinish(current, from, to, currentStatus));
            for (PatientMovement p : allPat) {
                p.setName(p.getPatient().getName());
                p.setFile_no(p.getPatient().getPatId());
                p.setService(PatientMovement.dept.get(p.getServiceType()));
                p.setNo(no++);
                if (p.getFinish() == PatientMovement.FINISH || p.getFinish() == PatientMovement.DRUG_ADD) {
                    filterdPat.add(p);
                }
            }
            table.setItems(allPat);
            countLabel.setText(String.valueOf(allPat.size()));
        } catch (SQLException ex) {
            Logger.getLogger(DoctorController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public User getCurrent() {
        return current;
    }

    public void setCurrent(User current) {
        this.current = current;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void searchBtn(ActionEvent event) {
        getData();
    }

    @FXML
    private void printBtn(ActionEvent event) {
    }

    private void textFilter() {
        filterTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filterdPat.clear();
            for (PatientMovement pat : allPat) {
                if (pat.getName().startsWith(newValue, 0)) {
                    filterdPat.add(pat);
                }
            }
            table.setItems(filterdPat);
        });
        textNameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterdPat.clear();
            for (PatientMovement pat : allPat) {
                if (pat.getName().startsWith(newValue, 0)) {
                    filterdPat.add(pat);
                }
            }
            table.setItems(filterdPat);
        });

    }

    private void openFile(PatientMovement item) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patient/PatFile.fxml"));
            root = loader.load();
            PatFileController manage = loader.getController();
            manage.setCurrentPatient(item);
            manage.setCurrentUser(current);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
//            stage.setResizable(false);
            stage.setTitle("Patient File ");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            getData();
        } catch (IOException ex) {
            Logger.getLogger(DoctorFinishedController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void radioChange(ActionEvent event) {
        if (finished.isSelected()) {
            currentStatus = PatientMovement.FINISH;
        } else if (drug.isSelected()) {
            currentStatus = PatientMovement.DRUG_ADD;
        }
        getData();
    }

    @FXML
    private void openLab(ActionEvent event) {
        if (getSelected() != null) {
            addLab(getSelected());
        }
    }

    @FXML
    private void openDrug(ActionEvent event) {
        if (getSelected() != null) {
            addDrug(getSelected());
        }
    }

    @FXML
    private void openFile(ActionEvent event) {
        if (getSelected() != null) {
            openFile(getSelected());
        }
    }

    private PatientMovement getSelected() {
        return (table.getSelectionModel().getSelectedItem() != null) ? table.getSelectionModel().getSelectedItem() : null;
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
            Stage inStage = new Stage();
            inStage.setTitle("Add Lab Test ");
            inStage.setScene(scene);
            inStage.initModality(Modality.WINDOW_MODAL);
            inStage.showAndWait();
        } catch (IOException e) {
        }
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
            Stage inStage = new Stage();
            manage.setStage(inStage);
            inStage.setResizable(true);
            inStage.setTitle("Add Drug ");
            inStage.setScene(scene);
            inStage.initModality(Modality.WINDOW_MODAL);
            inStage.showAndWait();
        } catch (IOException e) {
        }
    }

    @FXML
    private void tableSelectionChange(MouseEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            PatientMovement currentSelect = table.getSelectionModel().getSelectedItem();
            if (currentSelect.getDate().equals(Date.valueOf(LocalDate.now()))) {
                if (finished.isSelected()) {
                    btnOpenDrug.setDisable(false);
                    btnOpenLab.setDisable(false);
                } else if (drug.isSelected() && !MOVEMENT.checkPatLab(currentSelect)) {
                    btnOpenLab.setDisable(false);
                } else {
                    btnOpenDrug.setDisable(true);
                    btnOpenLab.setDisable(true);
                }
            }

        }

    }


}
