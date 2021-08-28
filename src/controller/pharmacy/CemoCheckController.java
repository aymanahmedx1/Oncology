/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pharmacy;

import BAO.CallManage;
import BAO.PrescriptionManagement;
import BAO.patient.MovmentManage;
import DAO.CallItem;
import DAO.Drug;
import DAO.Fluid;
import DAO.LabOrder;
import DAO.LabVisit;
import DAO.Prescription;
import DAO.VisitData;
import DAO.patient.Patient;
import DAO.patient.PatientMovement;
import com.jfoenix.controls.JFXButton;
import commons.Helpers;
import static commons.Helpers.LAB_MANAGE;
import static commons.Helpers.VISIT_MANAGE;
import controller.patient.PatFileController;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sorters.PrescriptionSort;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class CemoCheckController implements Initializable {

    private Stage stage;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
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

    ////   CEMO TABLE
    @FXML
    private TableView<Prescription> inStock;
    @FXML
    private TableColumn<Prescription, Prescription> patNameCol;
    @FXML
    private TableColumn<Prescription, Prescription> patIdCol;
    @FXML
    private TableColumn<Prescription, Prescription> doctorCol;
    ////   SUPPORT TABLE

    @FXML
    private TableView<Prescription> outStock;
    @FXML
    private TableColumn<Prescription, Prescription> outNameCol;
    @FXML
    private TableColumn<Prescription, Prescription> outIdCol;
    @FXML
    private TableColumn<Prescription, Prescription> outDoctoCol;

    //// DRUGS TABLE 
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

    ////// LAB TABLE
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
    private TextField txtNameFilter;
    @FXML
    private TextField txtDoctorFilter;
    @FXML
    private Button deleteAll;
    @FXML
    private TextField txtNameFilter1;
    @FXML
    private TextField txtDoctorFilter1;

    @FXML
    private Tab labTab;
    private ObservableList<Prescription> outStockPres;
    private ObservableList<Prescription> inStockPres;
    private ObservableList<Prescription> filterStockOut = FXCollections.observableArrayList();
    private ObservableList<Prescription> filterStockIn = FXCollections.observableArrayList();
    private ObservableList<VisitData> visitDetalis = FXCollections.observableArrayList();
    private ObservableList<LabVisit> labDetalis = FXCollections.observableArrayList();
    private final PrescriptionManagement PRES_MANAGE = new PrescriptionManagement();
    private final MovmentManage MOVE = new MovmentManage();
    @FXML
    private TableColumn<Prescription, Prescription> colNoOut;
    @FXML
    private TableColumn<Prescription, Prescription> colNoIn;
    @FXML
    private TableColumn<Drug, Integer> colNoDrug;
    @FXML
    private Button deleteAll1;
    private ArrayList<CallItem> allCallItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            fromDate.setValue(LocalDate.now());
            toDate.setValue(LocalDate.now());
            setTablUi();
            getData();
            setDateConverter();
            filterNameTxt();
            handelOnClose();
        });
    }

    @FXML
    private void getData(ActionEvent event) {
        getData();
    }

    @FXML
    private void openPatFile(ActionEvent event) {
        try {
            if (inStock.getSelectionModel().getSelectedItem() != null) {
                PatientMovement p = Helpers.MOVEMENT.getPatientFile(inStock.getSelectionModel().getSelectedItem().getPatientId());
                if (null != p) {
                    p.setName(p.getPatient().getName());
                    p.setFile_no(p.getPatient().getPatId());
                    p.setService(PatientMovement.dept.get(p.getServiceType()));
                    openPatFile(p);
                }
            }
            if (outStock.getSelectionModel().getSelectedItem() != null) {
                PatientMovement p = Helpers.MOVEMENT.getPatientFile(outStock.getSelectionModel().getSelectedItem().getPatientId());
                if (null != p) {
                    p.setName(p.getPatient().getName());
                    p.setFile_no(p.getPatient().getPatId());
                    p.setService(PatientMovement.dept.get(p.getServiceType()));
                    openPatFile(p);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void searchBtn(ActionEvent event) {
        getData();
    }

    @FXML
    private void getVisitData(MouseEvent event) {
        if (event.getClickCount() == 2) {
            ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
            acceptPres(ae);
        }
    }

    @FXML
    private void enterToSave(KeyEvent event) {
    }

    @FXML
    private void acceptPres(ActionEvent event) {
        if (outStock.getSelectionModel().getSelectedItem() != null) {
            try {
                Prescription selected = outStock.getSelectionModel().getSelectedItem();
                if (selected.getDate().compareTo(Date.valueOf(LocalDate.now())) != 0) {
                    Helpers.MOVEMENT.updateMoveDate(selected.getPatientId(), Date.valueOf(LocalDate.now()));
                }
                Helpers.PRES_MANAGE.updatePrescriptionState(selected, Prescription.NEW_ADD, Prescription.PHARMACY_TIME);
                getData();
                descTable.getItems().clear();
                labTable.getItems().clear();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
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

    private void setTablUi() {

        ///////// LEFT TABLES          
        rightTable();
        ///////// LEFT TABLES  OUT TABLE
        leftTable();
        outStock.setItems(inStockPres);
        inStock.setItems(outStockPres);
        ////
        /////Details Table
        tDrug.prefWidthProperty().bind(descTable.widthProperty().multiply(0.2));
        tDose.prefWidthProperty().bind(descTable.widthProperty().multiply(0.1));
        tFluid.prefWidthProperty().bind(descTable.widthProperty().multiply(0.2));
        tVolume.prefWidthProperty().bind(descTable.widthProperty().multiply(0.1));
        tNote.prefWidthProperty().bind(descTable.widthProperty().multiply(0.4));
        tDrug.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        tDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        tFluid.setCellValueFactory(new PropertyValueFactory<>("fluidName"));
        tVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        tNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colNoDrug.setCellValueFactory(new PropertyValueFactory<>("no"));
        descTable.setItems(visitDetalis);
        inStock.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                outStock.getSelectionModel().clearSelection();
                handelCemoData();
            }
        });
        outStock.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                inStock.getSelectionModel().clearSelection();
                handelOutData();
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

    private void getData() {
        outStockPres = FXCollections.observableArrayList();
        inStockPres = FXCollections.observableArrayList();
        try {
            allCallItem = new CallManage().allDoctorsCall(CallItem.GO_To_CEMO);
            Date from = Date.valueOf(fromDate.getValue());
            Date to = Date.valueOf(toDate.getValue());
            ArrayList<Prescription> outTemp = Helpers.PRES_MANAGE.allPresForDrugAndPharmacy(Prescription.DRUG_OUT_STOCK, from, to, Prescription.ID);
            ArrayList<Prescription> allOtherTemp = Helpers.PRES_MANAGE.allPresForDrugAndPharmacy(from, to, Prescription.PHARMACY_TIME);
            int index = 1;
            for (Prescription prescription : allOtherTemp) {
                if (prescription.getAcceptState() != Prescription.DRUG_OUT_STOCK) {
                    List<Integer> li = Helpers.PRES_MANAGE.getPresCategories(prescription);
                    if (li.contains(Drug.CHEMOTHERAPY)) {
                        prescription.setNo(index++);
                        inStockPres.add(prescription);
                    }
                }
            }
            int index2 = 1;
            for (Prescription prescription : outTemp) {
                prescription.setNo(index2++);
                outStockPres.add(prescription);
            }
            outStockPres.sorted(new PrescriptionSort());
            inStockPres.sorted(new PrescriptionSort());
            inStock.setItems(inStockPres);
            outStock.setItems(outStockPres);
            descTable.getItems().clear();
//            drugCatChange();
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

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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
        toDate.setConverter(new StringConverter<LocalDate>() {
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

    private void handelCemoData() {
        try {
            if (inStock.getSelectionModel().getSelectedItem() != null) {
                Prescription selected = inStock.getSelectionModel().getSelectedItem();
                visitDetalis = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(selected));
                descTable.setItems(visitDetalis);
                setInfoFildes(selected);
                getLabDetails(selected);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handelOutData() {
        try {
            if (outStock.getSelectionModel().getSelectedItem() != null) {
                Prescription selected = outStock.getSelectionModel().getSelectedItem();
                visitDetalis = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(selected));
                descTable.setItems(visitDetalis);
                setInfoFildes(selected);
                getLabDetails(selected);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
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

    private void filterNameTxt() {
        txtNameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterStockOut.clear();
            for (Prescription pres : outStockPres) {
                if (pres.getPatientName().startsWith(newValue)) {
                    filterStockOut.add(pres);
                }
            }
            outStock.setItems(filterStockOut);
        });
        txtDoctorFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterStockOut.clear();
            for (Prescription pres : outStockPres) {
                if (pres.getDoctorName().startsWith(newValue)) {
                    filterStockOut.add(pres);
                }
            }
            outStock.setItems(filterStockOut);
        });
        txtNameFilter1.textProperty().addListener((observable, oldValue, newValue) -> {
            filterStockIn.clear();
            for (Prescription pres : inStockPres) {
                if (pres.getPatientName().startsWith(newValue)) {
                    filterStockIn.add(pres);
                }
            }
            inStock.setItems(filterStockIn);
        });
        txtDoctorFilter1.textProperty().addListener((observable, oldValue, newValue) -> {
            filterStockIn.clear();
            for (Prescription pres : inStockPres) {
                if (pres.getDoctorName().startsWith(newValue)) {
                    filterStockIn.add(pres);
                }
            }
            inStock.setItems(filterStockIn);
        });
    }

    @FXML
    private void refusePres(ActionEvent event) {
        try {
            if (outStock.getSelectionModel().getSelectedItem() != null) {
                Prescription selected = outStock.getSelectionModel().getSelectedItem();
                PRES_MANAGE.deletePatientVisit(selected);
                MOVE.updateMovementByDate(selected.getPatientId(), selected.getDate(), PatientMovement.FINISH, PatientMovement.SEVRICE_TYPE_FINISH);
                getData();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CemoCheckController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openPatFile(PatientMovement item) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patient/PatFile.fxml"));
            root = loader.load();
            PatFileController manage = loader.getController();
            manage.setCurrentPatient(item);
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
    private void callPat(ActionEvent event) {
        Prescription selected = getTableSelection();
        if (selected != null) {
            try {
                if (isPresCalled(selected)) {
                    CallItem c = new CallItem();
                    c.setPatId(selected.getPatientId());
                    new CallManage().deleteCemoCall(c);
                } else {
                    CallItem item = new CallItem();
                    item.setPatId(selected.getPatientId());
                    item.setDoctorId(selected.getUser());
                    item.setState(CallItem.GO_To_CEMO);
                    new CallManage().addDoctorCall(item);
                }
                getData();
            } catch (SQLException ex) {
                Logger.getLogger(CemoCheckController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Prescription getTableSelection() {
        Prescription selected = null;
        if (inStock.getSelectionModel().getSelectedItem() != null) {
            selected = inStock.getSelectionModel().getSelectedItem();
        }
        if (outStock.getSelectionModel().getSelectedItem() != null) {
            selected = outStock.getSelectionModel().getSelectedItem();
        }
        return selected;
    }

    private boolean isPresCalled(Prescription pres) {
        for (CallItem call : allCallItem) {
            if (call.getPatId() == pres.getPatientId()) {
                return true;
            }
        }
        return false;
    }

    private void rightTable() {
        colNoOut.prefWidthProperty().bind(outStock.widthProperty().multiply(0.1));
        patNameCol.prefWidthProperty().bind(inStock.widthProperty().multiply(0.4));
        patIdCol.prefWidthProperty().bind(inStock.widthProperty().multiply(0.2));
        colNoIn.prefWidthProperty().bind(inStock.widthProperty().multiply(0.3));
        colNoIn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        colNoIn.setCellFactory(param -> new TableCell<Prescription, Prescription>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(String.valueOf(item.getNo()));
                    if (isPresCalled(item)) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }
        });
        colNoOut.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        colNoOut.setCellFactory(param -> new TableCell<Prescription, Prescription>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(String.valueOf(item.getNo()));
                    if (isPresCalled(item)) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }
        });

//        patNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
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
                    if (isPresCalled(item)) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });
//        patIdCol.setCellValueFactory(new PropertyValueFactory<>("patientNumber"));
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
                    if (isPresCalled(item)) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });
//        doctorCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
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
                    if (isPresCalled(item)) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });

    }

    private void leftTable() {
        colNoIn.prefWidthProperty().bind(outStock.widthProperty().multiply(0.1));
        outNameCol.prefWidthProperty().bind(outStock.widthProperty().multiply(0.4));
        outIdCol.prefWidthProperty().bind(outStock.widthProperty().multiply(0.2));
        outDoctoCol.prefWidthProperty().bind(outStock.widthProperty().multiply(0.3));
//        colNoOut.setCellValueFactory(new PropertyValueFactory<>("no"));

//        outNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        outNameCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        outNameCol.setCellFactory(param -> new TableCell<Prescription, Prescription>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(item.getPatientName());
                    if (isPresCalled(item)) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });
//        outIdCol.setCellValueFactory(new PropertyValueFactory<>("patientNumber"));
        outIdCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        outIdCol.setCellFactory(param -> new TableCell<Prescription, Prescription>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                } else {
                    setText(item.getPatientNumber());
                    if (isPresCalled(item)) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });
//        outDoctoCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        outDoctoCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        outDoctoCol.setCellFactory(param -> new TableCell<Prescription, Prescription>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText("");
                } else {
                    setText(item.getDoctorName());
                    if (isPresCalled(item)) {
                        this.setTextFill(Color.RED);
                    } else {
                        this.setTextFill(Color.BLACK);

                    }
                }

            }

        });
    }

    private void handelOnClose() {
        stage.setOnHidden((event) -> {
            try {
                new CallManage().deleteAllCemoCall();
            } catch (SQLException ex) {
                Logger.getLogger(CemoCheckController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
