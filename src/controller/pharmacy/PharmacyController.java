/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pharmacy;

import DAO.Drug;
import DAO.Fluid;
import DAO.LabOrder;
import DAO.LabVisit;
import DAO.Prescription;
import DAO.VisitData;
import DAO.patient.Patient;
import DAO.patient.PatientMovement;
import DAO.user.User;
import com.jfoenix.controls.JFXButton;
import commons.Helpers;
import static commons.Helpers.LAB_MANAGE;
import static commons.Helpers.VISIT_MANAGE;
import controller.actions.AddDrugController;
import controller.patient.PatFileController;
import controller.user.MainWindowController;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class PharmacyController implements Initializable {

    private ObservableList<Prescription> filterdPresTable = FXCollections.observableArrayList();
    private ObservableList<Prescription> allPrescriptions;
    private ObservableList<VisitData> visitDetalis = FXCollections.observableArrayList();
    private ObservableList<LabVisit> labDetalis = FXCollections.observableArrayList();
    private HashMap<String, Integer> getCategory = new HashMap<>();
    private Stage stage;
    private User current;
    private Prescription currentPrescription;
    private Timeline timeline;
    private DateTimeFormatter df;
    @FXML
    private Label todayDateLbl;
    @FXML
    private Label acceptedPatLbl;
    @FXML
    private TableView<Prescription> presTable;
    @FXML
    private TableColumn<Prescription, Integer> noCol;
    @FXML
    private TableColumn<Prescription, String> patNameCol;
    @FXML
    private TableColumn<Prescription, String> patIdCol;
    @FXML
    private TableColumn<Prescription, String> doctorCol;
    @FXML
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
    private Button deleteAll;
    @FXML
    private TextField txtNameFilter;
    @FXML
    private TextField txtDoctorFilter;
    @FXML
    private Label lblTime;
    @FXML
    private TextField txtSA;
    @FXML
    private TextField txtWt;
    @FXML
    private TextField txtAge;
    @FXML
    private TextField txtRegion;
    @FXML
    private TextField txtDia;
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
    private TableColumn<LabVisit, LabVisit> colFile;
    @FXML
    private Tab labTab;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toate;
    @FXML
    private JFXButton search;
    @FXML
    private ComboBox<String> comboDrugCat;
    @FXML
    private TableColumn<Drug, Integer> colNoDrug;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            fromDate.setValue(LocalDate.now());
            toate.setValue(LocalDate.now());
            setDateConverter();
            setTableUi();
            getData();
            filterNameTxt();
            tableSelectionChange();
            updateTimeLabel();
            setCategoryComboData();
        });
    }

    @FXML
    private void getData(ActionEvent event) {
        fromDate.setValue(LocalDate.now());
        toate.setValue(LocalDate.now());
        getData();

    }

    @FXML
    private void getVisitData(MouseEvent event) {
        getVisitDataHandel();
        if (event.getClickCount() == 2) {
            ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
            acceptPres(ae);
        }
    }

    @FXML
    private void enterToSave(KeyEvent event) {
    }

    @FXML
    private void refusePres(ActionEvent event) {
        currentPrescription = presTable.getSelectionModel().getSelectedItem();
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/actions/AddDrug.fxml"));
            root = loader.load();
            AddDrugController manage = loader.getController();
            PatientMovement move = new PatientMovement();
            Patient patient = Helpers.PAT_MANAGE.findByBarcode(currentPrescription.getPatientId());
            move.setPatient(patient);
            manage.setCurrentPat(move);
            manage.setCurrentUser(current);
            manage.setOldVisit(currentPrescription);
            manage.setEditFildes();
            Scene scene = new Scene(root);
            Stage st = new Stage();
            st.setTitle("Edit Drug ");
            st.setScene(scene);
            st.initOwner(stage);
            st.initModality(Modality.WINDOW_MODAL);
            st.showAndWait();
            currentPrescription = null;
            descTable.getItems().clear();

        } catch (IOException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void acceptPres(ActionEvent event) {
        if (presTable.getSelectionModel().getSelectedItem() != null) {
            try {
                Prescription selected = presTable.getSelectionModel().getSelectedItem();
                if (selected.getDate().compareTo(Date.valueOf(LocalDate.now())) != 0) {
                    Helpers.MOVEMENT.updateMoveDate(selected.getPatientId(), Date.valueOf(LocalDate.now()));
                }
                Helpers.PRES_MANAGE.updatePrescriptionState(selected, Prescription.ACCEPTED, Prescription.PREPARE_TIME);
                getData();
                descTable.getItems().clear();
                labTable.getItems().clear();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void getData() {
        clearpatInfoFileds();
        try {
            Date from = Date.valueOf(fromDate.getValue());
            Date to = Date.valueOf(toate.getValue());
            allPrescriptions = FXCollections.observableArrayList(Helpers.PRES_MANAGE.allPresForDrugAndPharmacy(Prescription.NEW_ADD, from, to, Prescription.PHARMACY_TIME));
            int index = 1;
            for (Prescription pres : allPrescriptions) {
                pres.setNo(index++);
            }
            presTable.setItems(allPrescriptions);
            descTable.getItems().clear();
            acceptedPatLbl.setText(String.valueOf(Helpers.PRES_MANAGE.getPresStateCount(Prescription.ACCEPTED)));
            drugCatChange();
        } catch (SQLException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setTableUi() {
        noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
        patNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patIdCol.setCellValueFactory(new PropertyValueFactory<>("patientNumber"));
        doctorCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        presTable.setItems(allPrescriptions);

        ////
        /////Details Table
        colNoDrug.setCellValueFactory(new PropertyValueFactory<>("no"));

        tDrug.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        tDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        tFluid.setCellValueFactory(new PropertyValueFactory<>("fluidName"));
        tVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        tNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        descTable.setItems(visitDetalis);

        presTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    Prescription selected = presTable.getSelectionModel().getSelectedItem();
                    visitDetalis = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(selected));
                    descTable.setItems(visitDetalis);
                } catch (SQLException ex) {
                    Logger.getLogger(PharmacyController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        ////////// LAB TABLE 
        labNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        labResult.setCellValueFactory(new PropertyValueFactory<>("result"));
        labTest.setCellValueFactory(new PropertyValueFactory<>("testName"));
        labDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colFile.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        colFile.setCellFactory(param -> new TableCell<LabVisit, LabVisit>() {
            @Override
            protected void updateItem(LabVisit item, boolean empty) {
                super.updateItem(item, empty);
                Image imgNew = new Image(this.getClass().getResourceAsStream("/images/new.png"));
                Image imgOld = new Image(this.getClass().getResourceAsStream("/images/old.png"));
                ImageView imgNewView = new ImageView(imgNew);
                ImageView imgOldView = new ImageView(imgOld);
                if (item == null) {
                    setGraphic(null);
                } else {
                    if (item.getResultFile() == null) {
                    } else {
                        setGraphic((item.getSeen() == 0) ? imgNewView : imgOldView);
                    }
                }
            }

        });
        labTable.setItems(labDetalis);

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public User getCurrent() {
        return current;
    }

    public void setCurrent(User user) {
        this.current = user;
    }

    private void filterNameTxt() {
        txtNameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterdPresTable.clear();
            for (Prescription pres : allPrescriptions) {
                if (pres.getPatientName().startsWith(newValue)) {
                    filterdPresTable.add(pres);
                }
            }
            presTable.setItems(filterdPresTable);
        });
        txtDoctorFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterdPresTable.clear();
            for (Prescription pres : allPrescriptions) {
                if (pres.getDoctorName().startsWith(newValue)) {
                    filterdPresTable.add(pres);
                }
            }
            presTable.setItems(filterdPresTable);
        });
    }

    private void clearpatInfoFileds() {
        txtSA.clear();
        txtWt.clear();
        txtAge.clear();
        txtRegion.clear();
        txtDia.clear();

    }

    private void setInfoFildes(Prescription selected) {
        try {
            Patient pat = Helpers.PAT_MANAGE.findByBarcode(selected.getPatientId());
            txtSA.setText(String.valueOf(pat.getSurface()));
            txtWt.setText(String.valueOf(pat.getWeight()));
            int age = Helpers.calculateAge(pat.getBirth().toLocalDate());
            txtAge.setText(String.valueOf(age));
            txtRegion.setText(String.valueOf(pat.getRegion().getName()));
            txtDia.setText((pat.getDiagnosis().getName() == null) ? "" : pat.getDiagnosis().getName());
        } catch (SQLException ex) {
            Logger.getLogger(PharmacyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getLabDetails(Prescription item) {
        PatientMovement mo = new PatientMovement();
        Patient p = new Patient();
        p.setId(item.getPatientId());
        mo.setPatient(p);
        mo.setDate(item.getDate());
        try {
            LabOrder alllab = LAB_MANAGE.getTodayLabOrder(mo);
            if (alllab != null) {
                labTab.setDisable(false);
                labDetalis = FXCollections.observableArrayList(LAB_MANAGE.getOrderDetails(alllab, LabOrder.FINISH));
                labTable.setItems(labDetalis);
            } else {
                labTab.setDisable(true);
                labDetalis.clear();
                labTable.getItems().clear();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PharmacyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void tableSelectionChange() {
        presTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            getVisitDataHandel();
        });

    }

    private void getVisitDataHandel() {
        try {
            if (presTable.getSelectionModel().getSelectedItem() != null) {
                Prescription selected = presTable.getSelectionModel().getSelectedItem();
                visitDetalis = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(selected));
                descTable.setItems(visitDetalis);
                setInfoFildes(selected);
                getLabDetails(selected);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openPatFile(ActionEvent event) {
        if (presTable.getSelectionModel().getSelectedItem() != null) {
            try {
                PatientMovement p = Helpers.MOVEMENT.getPatientFile(presTable.getSelectionModel().getSelectedItem().getPatientId());
                if (null != p) {
                    p.setName(p.getPatient().getName());
                    p.setFile_no(p.getPatient().getPatId());
                    p.setService(PatientMovement.dept.get(p.getServiceType()));
                    openFile(p);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    private void openFile(PatientMovement item) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patient/PatFile.fxml"));
            root = loader.load();
            PatFileController manage = loader.getController();
            manage.setCurrentPatient(item);
            manage.setCurrentUser(current);
            manage.setStage(stage);
            manage.disableUpdate();
            Scene scene = new Scene(root);
            Stage inStage = new Stage();
//            stage.setResizable(false);
            inStage.setTitle("Patient File ");
            inStage.setScene(scene);
            inStage.initOwner(stage);
            inStage.initModality(Modality.WINDOW_MODAL);
            inStage.showAndWait();
            getData();
        } catch (IOException e) {
        }
    }

    @FXML
    private void openPreDrug(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Pharmacy/PrepareDrug.fxml"));
            root = loader.load();
            PrepareDrugController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage inStage = new Stage();
            inStage.setTitle(" Prepare Drug ");
            inStage.setScene(scene);
            inStage.initOwner(currentStage);
            inStage.initModality(Modality.WINDOW_MODAL);
            inStage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateTimeLabel() {
        todayDateLbl.setText(String.valueOf(LocalDate.now()));
        df = DateTimeFormatter.ofPattern("hh:mm:ss a");
        lblTime.setText(String.valueOf(LocalTime.now().format(df)));
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1), new EventHandler() {
                    // KeyFrame event handler
                    @Override
                    public void handle(Event event) {
                        lblTime.setText(String.valueOf(LocalTime.now().format(df)));
                    }
                }));
        timeline.playFromStart();
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
    private void rejectPres(ActionEvent event) {
        if (presTable.getSelectionModel().getSelectedItem() != null) {
            Prescription pres = presTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you will delete all patient drug \n"
                    + "Do you want to procced ", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> res = alert.showAndWait();
            if (res.isPresent() && res.get().equals(ButtonType.YES)) {
                try {
                    Helpers.PRES_MANAGE.deletePatientVisit(pres);
                    Helpers.MOVEMENT.updateMovementByDate(pres.getPatientId(), pres.getDate(), PatientMovement.FINISH, PatientMovement.SEVRICE_TYPE_FINISH);
                    getData();
                } catch (SQLException ex) {
                    Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    @FXML
    private void drugCategoryChange(ActionEvent event) {
        drugCatChange();
    }

    private void drugCatChange() {
        if (!comboDrugCat.getSelectionModel().isEmpty() && comboDrugCat.getSelectionModel() != null) {
            int selected = getCategory.get(comboDrugCat.getSelectionModel().getSelectedItem());
            if (selected != 0) {
                filterdPresTable.clear();
                int index = 1;
                for (Prescription prescription : allPrescriptions) {
                    try {
                        List<Integer> li = Helpers.PRES_MANAGE.getPresCategories(prescription);
                        if (li.contains(selected) && selected == Drug.CHEMOTHERAPY) {
                            prescription.setNo(index++);
                            filterdPresTable.add(prescription);
                        } else if (!li.contains(Drug.CHEMOTHERAPY) && selected == Drug.SUPPORTIVE) {
                            prescription.setNo(index++);
                            filterdPresTable.add(prescription);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(PharmacyController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                presTable.setItems(filterdPresTable);
                descTable.getItems().clear();

            } else {
                presTable.setItems(allPrescriptions);
            }
        }
    }

    private void setCategoryComboData() {
        comboDrugCat.getItems().add("All patients".toLowerCase());
        comboDrugCat.getItems().add("Chemotherapy patients".toLowerCase());
        comboDrugCat.getItems().add("Supportive patients".toLowerCase());
        getCategory.put("All patients".toLowerCase(), 0);
        getCategory.put("Chemotherapy patients".toLowerCase(), 1);
        getCategory.put("Supportive patients".toLowerCase(), 2);
        comboDrugCat.getSelectionModel().selectFirst();
    }

    @FXML
    private void openFile(MouseEvent event) {
        LabVisit visit = labTable.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2) {
            if (visit.getResultFile() == null) {
                return;
            }
            try {
                File f = new File(visit.getResultFile());
                Desktop.getDesktop().open(f);
            } catch (IOException ex) {
                Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
