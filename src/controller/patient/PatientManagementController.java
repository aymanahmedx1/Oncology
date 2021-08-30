/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.patient;

import DAO.Drug;
import DAO.Fluid;
import DAO.LabOrder;
import DAO.LabVisit;
import DAO.Prescription;
import DAO.VisitData;
import DAO.patient.Patient;
import DAO.patient.PatientMovement;
import commons.Helpers;
import static commons.Helpers.LAB_MANAGE;
import static commons.Helpers.MOVEMENT;
import static commons.Helpers.PAT_MANAGE;
import static commons.Helpers.PRES_MANAGE;
import static commons.Helpers.VISIT_MANAGE;
import controller.reception.ReceptionController;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class PatientManagementController implements Initializable {

    private ArrayList<Patient> allPatient;
    private ObservableList<VisitData> visitDetalis = FXCollections.observableArrayList();
    private ObservableList<LabVisit> labDetalis = FXCollections.observableArrayList();
    private ObservableList<PatientMovement> allVisit;
    private PatientMovement current;
    private Patient currentPatient;
    private ContextMenu pop = new ContextMenu();
    private HashMap<String, Patient> patentMap = new HashMap<>();

    private Stage stage;
    @FXML
    private Label phoneLbl111111;
    @FXML
    private TextField diaTxt;
    @FXML
    private Label nameLbl1111;
    @FXML
    private TextField dateOfBirthTxt;
    @FXML
    private Label nameLbl;
    @FXML
    private TextField nameTxt;
    @FXML
    private Label idLbl;
    @FXML
    private TextField idTxt;
    @FXML
    private Label phoneLbl112;
    @FXML
    private TextField regionTxt;
    @FXML
    private Label phoneLbl111;
    @FXML
    private TextField entryDateTxt;
    //// PRES TABLE
    @FXML
    private TableView<PatientMovement> visitTable;
    @FXML
    private TableColumn<PatientMovement, Integer> tNo;
    @FXML
    private TableColumn<PatientMovement, Date> tD;
    @FXML
    /////// DESC TABLE
    private TableView<VisitData> descTable;
    @FXML
    private TableColumn<VisitData, Drug> tDrug;
    @FXML
    private TableColumn<VisitData, String> tDose;
    @FXML
    private TableColumn<VisitData, Fluid> tFluid;
    @FXML
    private TableColumn<VisitData, Integer> tVolume;
    @FXML
    private TableColumn<VisitData, String> tNote;
    @FXML
    private TableColumn<VisitData, Integer> tNumber;
    ///// LAB TABLE

    @FXML
    private TableView<LabVisit> labTable;
    @FXML
    private TableColumn<LabVisit, Integer> labNo;
    @FXML
    private TableColumn<LabVisit, String> labTest;
    @FXML
    private TableColumn<LabVisit, Date> labDate;
    @FXML
    private TableColumn<LabVisit, String> labResult;
    @FXML
    private TabPane tabPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setTableUi();
            autoAll();
        });
    }

    @FXML
    private void clear(ActionEvent event) {
        nameTxt.clear();
        idTxt.clear();
        dateOfBirthTxt.clear();
        regionTxt.clear();
        entryDateTxt.clear();
        diaTxt.clear();
        visitTable.getItems().clear();
        descTable.getItems().clear();
        labTable.getItems().clear();
        current = null;
        currentPatient = null;

    }

    @FXML
    private void getVisitData(MouseEvent event) {
        handelVisitData();
    }

    @FXML
    private void deletVisit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are You Sure ", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent() && res.get().equals(ButtonType.YES)) {
            try {
                PatientMovement item = visitTable.getSelectionModel().getSelectedItem();
                ArrayList<Prescription> allPres = PRES_MANAGE.patientVisits(item, item.getDate());
                if (allPres.isEmpty()) {
                    visitDetalis.clear();
                    descTable.getItems().clear();
                } else {
                    Helpers.PRES_MANAGE.deletePatientVisit(allPres.get(0));
                }
                LabOrder alllab = LAB_MANAGE.getTodayLabOrder(item);
                if (alllab != null) {
                    Helpers.LAB_MANAGE.deletePatientLab(alllab);
                }
                Helpers.MOVEMENT.deletePatientMoveMent(item);
                getData();
            } catch (SQLException ex) {
                Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void delAllPatVisit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are You Sure ", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent() && res.get().equals(ButtonType.YES)) {
            try {
                Helpers.PRES_MANAGE.deletePatientPrescription(currentPatient);
                Helpers.LAB_MANAGE.deleteAllPatientLab(currentPatient);
                Helpers.MOVEMENT.deletAllPatientMovements(currentPatient);
                Helpers.PAT_MANAGE.delete(currentPatient);
                clear(event);
            } catch (SQLException ex) {
                Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setTableUi() {
        //// VISIT TABLE
        tNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        tD.setCellValueFactory(new PropertyValueFactory<>("date"));
        visitTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handelVisitData();
            }
        });
        visitTable.setItems(allVisit);
        //// DETEAILS TABLE 
        tNumber.setCellValueFactory(new PropertyValueFactory<>("no"));
        tDrug.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        tDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        tFluid.setCellValueFactory(new PropertyValueFactory<>("fluidName"));
        tVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        tNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        descTable.setItems(visitDetalis);

        //// LAB TABLE 
        labNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        labResult.setCellValueFactory(new PropertyValueFactory<>("result"));
        labTest.setCellValueFactory(new PropertyValueFactory<>("testName"));
        labDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        labTable.setItems(labDetalis);

    }

    private void setPatToUi() {
        nameTxt.setText(currentPatient.getName());
        idTxt.setText(currentPatient.getPatId());
        int yearsBetween = Period.between(currentPatient.getBirth().toLocalDate(), LocalDate.now()).getYears();
        dateOfBirthTxt.setText(String.valueOf(yearsBetween));
        regionTxt.setText(currentPatient.getRegion().getName());
        entryDateTxt.setText(String.valueOf(currentPatient.getEntry()));
        diaTxt.setText(currentPatient.getDiagnosis().getName());
    }

    private void getData() {
        try {
            current = new PatientMovement();
            current.setPatient(currentPatient);
            allVisit = FXCollections.observableArrayList();
            ObservableList<PatientMovement> temp = null;
            temp = FXCollections.observableArrayList(MOVEMENT.getPatientMovement(current));
            int no = 1;
            for (PatientMovement visit : temp) {
                if (MOVEMENT.checkPatDrug(visit) || MOVEMENT.checkPatLab(visit)) {
                    visit.setNo(no++);
                    visit.setPatient(currentPatient);
                    allVisit.add(visit);
                }
            }
            visitTable.setItems(allVisit);
            if (visitTable.getItems().size() > 0) {
                visitTable.getSelectionModel().selectFirst();
                handelVisitData();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handelVisitData() {
        try {
            PatientMovement item = visitTable.getSelectionModel().getSelectedItem();
            ArrayList<Prescription> allPres = PRES_MANAGE.patientVisits(item, item.getDate());
            if (allPres.isEmpty()) {
                visitDetalis.clear();
                descTable.getItems().clear();
            } else {
                visitDetalis = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(allPres.get(0)));
                descTable.setItems(visitDetalis);
            }
            LabOrder alllab = LAB_MANAGE.getTodayLabOrder(item);
            if (alllab != null) {
                labDetalis = FXCollections.observableArrayList(LAB_MANAGE.getOrderDetails(alllab));
                labTable.setItems(labDetalis);
            } else {
                labDetalis.clear();
                labTable.getItems().clear();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void nameAuto() {
        if (nameTxt.getText().equals("")) {
            if (pop.isShowing()) {
                pop.hide();
            }
            return;
        }
        getPatientByName();
        pop.getItems().clear();
        for (Patient r : allPatient) {
            patentMap.put(r.getName(), r);
            MenuItem m = new MenuItem(r.getName());
            m.setStyle("-fx-pref-width:" + nameTxt.getWidth() + ";");
            m.setOnAction((event) -> {
                pop.hide();
                currentPatient = patentMap.get(m.getText());
                setPatToUi();
                getData();
                pop.hide();

            });
            pop.getItems().add(m);

        }
        nameTxt.setContextMenu(pop);

        pop.show(nameTxt, Side.BOTTOM, 0, 0);
    }

    private void getPatientByName() {
        try {
            allPatient = PAT_MANAGE.findPatients(nameTxt.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getPatientById() {
        try {
            allPatient = PAT_MANAGE.findPatFileId(idTxt.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void autoAll() {
        nameTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            nameAuto();
        });
        idTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            idAuto();
        });
    }

    private void idAuto() {
        if (idTxt.getText().equals("")) {
            if (pop.isShowing()) {
                pop.hide();
            }
            return;
        }
        getPatientById();
        pop.getItems().clear();
        for (Patient r : allPatient) {
            patentMap.put(r.getPatId(), r);
            MenuItem m = new MenuItem(r.getPatId());
            m.setStyle("-fx-pref-width:" + idTxt.getWidth() + ";");
            m.setOnAction((event) -> {
                pop.hide();
                currentPatient = patentMap.get(m.getText());
                setPatToUi();
                getData();
                pop.hide();
            });
            pop.getItems().add(m);
        }
        idTxt.setContextMenu(pop);

        pop.show(idTxt, Side.BOTTOM, 0, 0);
    }

    @FXML
    private void openResultFile(MouseEvent event) {
        LabVisit visit = labTable.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2) {
            if (visit.getResultFile() == null) {
                return;
            }
            try {
                File f = new File(visit.getResultFile());
                Desktop.getDesktop().open(f);
                visit.setSeen(1);
                Helpers.LAB_MANAGE.updateLabItemOpenState(visit);
                PatientMovement item = visitTable.getSelectionModel().getSelectedItem();
                LabOrder alllab = LAB_MANAGE.getTodayLabOrder(item);
                labDetalis = FXCollections.observableArrayList(LAB_MANAGE.getOrderDetails(alllab, LabOrder.FINISH));
                labTable.setItems(labDetalis);
            } catch (IOException ex) {
                Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
