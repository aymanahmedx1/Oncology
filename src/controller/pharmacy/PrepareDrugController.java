/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pharmacy;

import BAO.VisitManagement;
import DAO.Drug;
import DAO.LabOrder;
import DAO.LabVisit;
import DAO.Prescription;
import DAO.VisitData;
import DAO.patient.Patient;
import DAO.patient.PatientMovement;
import DAO.user.User;
import com.jfoenix.controls.JFXButton;
import commons.ExcelExport;
import commons.Helpers;
import static commons.Helpers.LAB_MANAGE;
import static commons.Helpers.VISIT_MANAGE;
import commons.NewExcelExport;
import controller.actions.AddDrugController;
import controller.patient.PatFileController;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class PrepareDrugController implements Initializable {

    private User current;
    private Stage stage;
    private ObservableList<Prescription> allPrescriptions = FXCollections.observableArrayList();
    private ObservableList<Prescription> filterdPresTable = FXCollections.observableArrayList();
    private ObservableList<Prescription> filterdPresTableByDrugName = FXCollections.observableArrayList();
    private ObservableList<VisitData> visitDetalis = FXCollections.observableArrayList();
    private ObservableList<VisitData> filterdVisitDetalis = FXCollections.observableArrayList();
    private ObservableList<LabVisit> labDetalis = FXCollections.observableArrayList();
    private HashMap<String, Integer> getCategory = new HashMap<>();
    private Prescription currentPrescription;
    private Timeline timeline;
    private DateTimeFormatter df;
    @FXML
    private Label todayDateLbl;
    @FXML
    private TableView<Prescription> presTable;
    @FXML
    private TableColumn<Prescription, Prescription> noCol;
    @FXML
    private TableColumn<Prescription, Prescription> patNameCol;
    @FXML
    private TableColumn<Prescription, Prescription> patIdCol;
    @FXML
    private TableColumn<Prescription, Prescription> doctorCol;
    @FXML
    private TableColumn<Prescription, Prescription> colDate;
    @FXML
    private TableView<VisitData> descTable;
    @FXML
    private TableColumn<VisitData, VisitData> tDrug;
    @FXML
    private TableColumn<VisitData, VisitData> tDose;
    @FXML
    private TableColumn<VisitData, VisitData> tFluid;
    @FXML
    private TableColumn<VisitData, VisitData> tVolume;
    @FXML
    private TableColumn<VisitData, VisitData> tNote;
    @FXML
    private Label lblTime;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toate;
    @FXML
    private JFXButton search;
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
    private Tab labTab;
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
    private ComboBox<String> comboDrugCat;
    @FXML
    private ComboBox<String> comboDetailCat;
    @FXML
    private TextField txtNameFilter;
    @FXML
    private TextField txtDoctorFilter;
    @FXML
    private TextField txtDrugFilter;
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
            setCategoryComboData();
            getData();
            updateTimeLabel();
            setDateConverter();
            setTableUi();
            tableSelectionChange();
            textNameFilter();
            presTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            textDrugFilter();
        });
    }

    @FXML
    private void getData(ActionEvent event) {
        getData();
    }

    @FXML
    private void getVisitData(MouseEvent event) {
        try {
            if (presTable.getSelectionModel().getSelectedItem() != null) {
                Prescription selected = presTable.getSelectionModel().getSelectedItem();
                visitDetalis = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(selected));
//                descTable.setItems(visitDetalis);
                visitDetailFilter();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (event.getClickCount() == 2) {
            Prescription selected = presTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Do you want to send back the patient to clinical pharmacy ? ", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Return patient");
            alert.setHeaderText("CONFIRMATION".toLowerCase());
            Optional<ButtonType> res = alert.showAndWait();
            if (res.isPresent() && res.get().equals(ButtonType.YES)) {
                try {
                    Prescription pres = presTable.getSelectionModel().getSelectedItem();
                    Helpers.PRES_MANAGE.updatePrescriptionState(selected, Prescription.NEW_ADD, Prescription.PREPARE_TIME);
                    for (VisitData drug : visitDetalis) {
                        drug.setCheck(VisitData.NOT_CHECED);
                        Helpers.VISIT_MANAGE.updateVisitItemState(drug);
                    }
                    pres.setChecked(VisitData.NOT_CHECED);
                    Helpers.PRES_MANAGE.updatePresItemState(pres, Prescription.PREPARE_TIME);
                    getData();
                } catch (SQLException ex) {
                    Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void editPres(ActionEvent event) {
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
            Scene scene = new Scene(root);
            Stage st = new Stage();
            st.setTitle("Edit Drug ");
            st.setScene(scene);
            st.initOwner(stage);
            st.initModality(Modality.WINDOW_MODAL);
            st.showAndWait();
            getData();
            currentPrescription = null;
            descTable.getItems().clear();

        } catch (IOException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getData() {
        try {
            Date from = Date.valueOf(fromDate.getValue());
            Date to = Date.valueOf(toate.getValue());
            allPrescriptions = FXCollections.observableArrayList(Helpers.PRES_MANAGE.allPresForDrugAndPharmacy(Prescription.ACCEPTED, from, to, Prescription.PREPARE_TIME));
//            presTable.setItems(allPrescriptions);
            descTable.getItems().clear();
            prescriptionFilter();
        } catch (SQLException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setTableUi() {

//        noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
//        patIdCol.setCellValueFactory(new PropertyValueFactory<>("patientNumber"));
//        doctorCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
//        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colNoDrug.setCellValueFactory(new PropertyValueFactory<>("no"));

        patNameCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        patNameCol.setCellFactory(param -> new TableCell<Prescription, Prescription>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(item.getPatientName());
                    if (item.getChecked() == VisitData.CHECED) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });
        ////
        doctorCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        doctorCol.setCellFactory(param -> new TableCell<Prescription, Prescription>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(item.getDoctorName());
                    if (item.getChecked() == VisitData.CHECED) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });
        ///
        patIdCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        patIdCol.setCellFactory(param -> new TableCell<Prescription, Prescription>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(item.getPatientNumber());
                    if (item.getChecked() == VisitData.CHECED) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });
        ///////////
        colDate.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        colDate.setCellFactory(param -> new TableCell<Prescription, Prescription>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(String.valueOf(item.getDate()));
                    if (item.getChecked() == VisitData.CHECED) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });
        ///////////
        noCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        noCol.setCellFactory(param -> new TableCell<Prescription, Prescription>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(String.valueOf(item.getNo()));
                    if (item.getChecked() == VisitData.CHECED) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });

        presTable.setItems(allPrescriptions);

        ////
        /////Details Table
//        tDrug.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        tDrug.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        tDrug.setCellFactory(param -> new TableCell<VisitData, VisitData>() {
            @Override
            protected void updateItem(VisitData item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(String.valueOf(item.getDrugName()));
                    if (item.getCheck() == VisitData.CHECED) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });
//        tDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        tDose.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        tDose.setCellFactory(param -> new TableCell<VisitData, VisitData>() {
            @Override
            protected void updateItem(VisitData item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(String.valueOf(item.getDose()));
                    if (item.getCheck() == VisitData.CHECED) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });
//        tFluid.setCellValueFactory(new PropertyValueFactory<>("fluidName"));
        tFluid.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        tFluid.setCellFactory(param -> new TableCell<VisitData, VisitData>() {
            @Override
            protected void updateItem(VisitData item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    if (item.getFluidName() != null) {
                        setText(String.valueOf(item.getFluidName()));
                        if (item.getCheck() == VisitData.CHECED) {
                            this.setTextFill(Color.RED);
                        } else {
                            this.setTextFill(Color.BLACK);
                        }
                    } else {
                        setText("");
                    }
                }

            }

        });
//        tVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        tVolume.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        tVolume.setCellFactory(param -> new TableCell<VisitData, VisitData>() {
            @Override
            protected void updateItem(VisitData item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    if (item.getFluidName() != null) {
                        setText(String.valueOf(item.getVolume()));
                        if (item.getCheck() == VisitData.CHECED) {
                            this.setTextFill(Color.RED);
                        } else {
                            this.setTextFill(Color.BLACK);

                        }
                    } else {
                        setText("");
                    }
                }

            }

        });
//        tNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        tNote.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        tNote.setCellFactory(param -> new TableCell<VisitData, VisitData>() {
            @Override
            protected void updateItem(VisitData item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    if (item.getNote() != null) {
                        setText(String.valueOf(item.getNote()));
                        if (item.getCheck() == VisitData.CHECED) {
                            this.setTextFill(Color.RED);
                        } else {
                            this.setTextFill(Color.BLACK);
                        }

                    } else {
                        setText("");
                    }
                }

            }

        });

        descTable.setItems(visitDetalis);

        ////////// LAB TABLE 
        labNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        labResult.setCellValueFactory(new PropertyValueFactory<>("result"));
        labTest.setCellValueFactory(new PropertyValueFactory<>("testName"));
        labDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        labTable.setItems(labDetalis);
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

    private void tableSelectionChange() {
        presTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            getVisitDataHandel();
        });
        presTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        });

    }

    private void getVisitDataHandel() {
        try {
            if (presTable.getSelectionModel().getSelectedItem() != null) {
                Prescription selected = presTable.getSelectionModel().getSelectedItem();
                visitDetalis = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(selected));
                visitDetailFilter();
                setInfoFildes(selected);
                getLabDetails(selected);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    @FXML
    private void prinrReport(ActionEvent event) {
        try {
            NewExcelExport ex = new NewExcelExport();
            int drug = getCategory.get(comboDetailCat.getSelectionModel().getSelectedItem());
            int category = getCategory.get(comboDrugCat.getSelectionModel().getSelectedItem());
            String catName = "";
            if (category == 0) {
                catName = "Checked all patients ";
            } else {
                catName = comboDrugCat.getSelectionModel().getSelectedItem();
            }
            if (presTable.getSelectionModel() != null && presTable.getSelectionModel().getSelectedItems().size() > 0) {
                ArrayList<Prescription> selected = new ArrayList<>(presTable.getSelectionModel().getSelectedItems());
                ex.prepareDrugToExcel(stage, selected, fromDate.getValue(), toate.getValue(), drug, catName);
            } else {
                ex.prepareDrugToExcel(stage, new ArrayList<>(presTable.getItems()), fromDate.getValue(), toate.getValue(), drug, catName);
            }
        } catch (SQLException ex1) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    private void setCategoryComboData() {
        comboDrugCat.getItems().add("All patients".toLowerCase());
        comboDrugCat.getItems().add("Chemotherapy patients".toLowerCase());
        comboDrugCat.getItems().add("Supportive patients".toLowerCase());

        getCategory.put("All Drug".toLowerCase(), 0);
        getCategory.put("All patients".toLowerCase(), 0);
        getCategory.put("Chemotherapy patients".toLowerCase(), 1);
        getCategory.put("Supportive patients".toLowerCase(), 2);
        getCategory.put("Chemotherapy and fluid".toLowerCase(), 1);
        getCategory.put("Supportive and fluid".toLowerCase(), 2);
        comboDrugCat.getSelectionModel().selectFirst();

        comboDetailCat.getItems().add("All Drug".toLowerCase());
        comboDetailCat.getItems().add("Chemotherapy and fluid".toLowerCase());
        comboDetailCat.getItems().add("Supportive and fluid".toLowerCase());
        comboDetailCat.getSelectionModel().selectFirst();
    }

    private void prescriptionFilter() {
        if (comboDrugCat.getSelectionModel() != null || null == allPrescriptions) {
            txtDrugFilter.clear();
            int selected = getCategory.get(comboDrugCat.getSelectionModel().getSelectedItem());
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
                    } else if (selected == 0) {
                        prescription.setNo(index++);
                        filterdPresTable.add(prescription);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(PharmacyController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            presTable.setItems(filterdPresTable);
            descTable.getItems().clear();
        }
    }

    private void visitDetailFilter() {
        if (!comboDetailCat.getSelectionModel().isEmpty() && comboDetailCat.getSelectionModel() != null) {
            int selected = getCategory.get(comboDetailCat.getSelectionModel().getSelectedItem());
            filterdVisitDetalis.clear();
            if (selected == 0) {
                descTable.setItems(visitDetalis);
            } else {
                for (VisitData visit : visitDetalis) {
                    if (selected == visit.getCategory()) {
                        filterdVisitDetalis.add(visit);
                    } else if (selected == Drug.CHEMOTHERAPY && visit.getCategory() == Drug.FLUID) {
                        filterdVisitDetalis.add(visit);
                    } else if (selected == Drug.SUPPORTIVE && visit.getCategory() == Drug.FLUID) {
                        filterdVisitDetalis.add(visit);
                    }
                }
                descTable.setItems(filterdVisitDetalis);
            }
        }
    }

    private void textNameFilter() {
        txtNameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                prescriptionFilter();
                return;
            }
            ObservableList<Prescription> temp = FXCollections.observableArrayList(filterdPresTable);
            filterdPresTable.clear();
            for (Prescription pres : temp) {
                if (pres.getPatientName().startsWith(newValue)) {
                    filterdPresTable.add(pres);
                }
            }
            presTable.setItems(filterdPresTable);
        });
        txtDoctorFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                prescriptionFilter();
                return;
            }
            ObservableList<Prescription> temp = FXCollections.observableArrayList(filterdPresTable);
            filterdPresTable.clear();
            for (Prescription pres : temp) {
                if (pres.getDoctorName().startsWith(newValue)) {
                    filterdPresTable.add(pres);
                }
            }
            presTable.setItems(filterdPresTable);
        });

    }

    @FXML
    private void drugDetialsClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            VisitData drug = descTable.getSelectionModel().getSelectedItem();
            Prescription pres = presTable.getSelectionModel().getSelectedItem();

            if (drug == null || pres == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Some thing went wrong !!", ButtonType.OK);
                alert.show();
                return;
            }
            try {
                Parent root;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Pharmacy/PrintLabel.fxml"));
                root = loader.load();
                PrintLabelController manage = loader.getController();
                manage.setVisit(drug);
                manage.setPresc(pres);
                Scene scene = new Scene(root);
                Stage inStage = new Stage();
                inStage.setResizable(false);
                inStage.setTitle("Print Label");
                inStage.setScene(scene);
                inStage.initOwner(stage);
                inStage.initModality(Modality.WINDOW_MODAL);
                manage.setStage(inStage);
                inStage.showAndWait();
                boolean res = manage.finish();
                if (res) {
                    drug.setCheck(VisitData.CHECED);
                    pres.setChecked(VisitData.CHECED);
                    Helpers.VISIT_MANAGE.updateVisitItemState(drug);
                    Helpers.PRES_MANAGE.updatePresItemState(pres, Prescription.SCREEN_TIME);
                    getData();
                    visitDetalis = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(pres));
                    descTable.setItems(visitDetalis);
                    presTable.getSelectionModel().select(pres);
                }
            } catch (IOException e) {
            } catch (SQLException ex) {
                Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void textDrugFilter() {
        txtDrugFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!txtDrugFilter.getText().isEmpty() || txtDrugFilter.getText().equals("")) {
                filterdPresTableByDrugName.clear();
                int selected = getCategory.get(comboDrugCat.getSelectionModel().getSelectedItem());
                try {
                    for (Prescription prescription : filterdPresTable) {
                        ObservableList<VisitData> temp = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(prescription));
                        for (VisitData visit : temp) {
                            if (visit.getDrugName().startsWith(newValue)) {
                                filterdPresTableByDrugName.add(prescription);
                                break;
                            }
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(PharmacyController.class.getName()).log(Level.SEVERE, null, ex);
                }

                presTable.setItems(filterdPresTableByDrugName);
                descTable.getItems().clear();
            } else {
                presTable.setItems(filterdPresTable);
            }
        });
    }

    @FXML
    private void combPrescription(ActionEvent event) {
        prescriptionFilter();
    }

    @FXML
    private void comboDetai(ActionEvent event) {
        visitDetailFilter();
    }
}
