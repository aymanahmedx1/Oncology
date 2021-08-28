/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.actions;

import BAO.DiagnosisManage;
import BAO.DrugManagment;
import BAO.PrescriptionManagement;
import BAO.VisitManagement;
import BAO.patient.MovmentManage;
import BAO.patient.PatientManage;
import DAO.Diagnosis;
import DAO.Drug;
import DAO.Fluid;
import DAO.Prescription;
import DAO.VisitData;
import DAO.patient.PatientMovement;
import DAO.user.User;
import commons.Helpers;
import commons.RunReport;
import controller.reception.ReceptionController;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class AddDrugController implements Initializable {

    private Stage stage;
    private static final PrescriptionManagement PRES_MANAGE = new PrescriptionManagement();
    private static final VisitManagement ADD_VISIT = new VisitManagement();
    private static final MovmentManage MOVE = new MovmentManage();
    private final String SELECT = " -- NONE -- ";
    private static final DrugManagment DRUG_MANAGE = new DrugManagment();
    private static final PatientManage PAT_MANAGE = new PatientManage();
    private boolean update = false;
    private Drug currentDrug;
    private Drug currentFluid;
    private PatientMovement currentPat;
    private User currentUser;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField idTxt;
    @FXML
    private TextField drugTxt;
    @FXML
    private TextField doseTxt;
    @FXML
    private ComboBox<String> fluidCombo;
    @FXML
    private TextField volumeTxt;
    @FXML
    private TextField noteTxt;
    @FXML
    private TableView<VisitData> table;
    @FXML
    private TableColumn<VisitData, String> tNumber;
    @FXML
    private TableColumn<VisitData, String> tDrug;
    @FXML
    private TableColumn<VisitData, String> tDose;
    @FXML
    private TableColumn<VisitData, String> tFluid;
    @FXML
    private TableColumn<VisitData, Integer> tVolume;
    @FXML
    private TableColumn<VisitData, String> tNote;
    private ContextMenu pop = new ContextMenu();
    private Pattern pattern;
    private ArrayList<Drug> foundDrug;
    private ArrayList<Drug> allFluid;
    private ArrayList<Diagnosis> allDiagnosis;
    private ArrayList<Diagnosis> filterdDiagnosis = new ArrayList<>();
    private HashMap<String, Drug> drugMap = new HashMap<>();
    private HashMap<String, Drug> fluidMap = new HashMap<>();
    private HashMap<String, Diagnosis> diaMap = new HashMap<>();
    private ArrayList<String> comboBoxStrings = new ArrayList<>();
    private ObservableList<VisitData> visitTable = FXCollections.observableArrayList();
    private Prescription currentPres = new Prescription();
    @FXML
    private ComboBox<Date> comboLastVisit;
    @FXML
    private TextField txtSurface;
    @FXML
    private TextField txtWeight;
    @FXML
    private ComboBox<String> comboDia;
    @FXML
    private TextField diaTxtFilter;
    private boolean drugStockProceed = false;
    private boolean drugOutStock = false;
    private boolean editFlage = false;
    private boolean setOldDose = false;
    private int editDose = 0;
    @FXML
    private TextField txtAge;

    /**
     * Initializes the controller class.
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setPat();
            autoDrug();
            drugTxt.requestFocus();
            setComboboxData();
            setTableData();
            getDiagnosis();
            checkDose();
            txtFilterDia();
        });

    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.hide();
    }

    @FXML
    private void addByEnter(KeyEvent event) {
    }

    @FXML
    private void addDrugToTable(ActionEvent event) {
        if (validateDrugToAddToTable()) {
            VisitData visit = new VisitData();
            visit.setDrugId(currentDrug.getId());
            visit.setDrugName(currentDrug.getName());
            visit.setDose((setOldDose) ? String.valueOf(editDose) : doseTxt.getText());
            visit.setNote(noteTxt.getText());
            visit.setCategory(currentDrug.getCategory());
            if (!fluidCombo.getSelectionModel().getSelectedItem().equals(SELECT)) {
                visit.setFluidId(currentFluid.getId());
                visit.setFluidName(currentFluid.getName());
                visit.setVolume(Integer.parseInt(volumeTxt.getText()));
            }
            visit.setIsChecked(false);

            if (validateDuplicate(visit)) {
                visitTable.add(visit);
                drugStockProceed = false;
                editFlage = false;
                clearDrugArea();
                drugTxt.requestFocus();
                setTablenumbers();
            }
        }
    }

    @FXML
    private void editTableItem(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            editFlage = true;
            VisitData editVisit = table.getSelectionModel().getSelectedItem();
            currentDrug = new Drug();
            currentDrug.setId(editVisit.getDrugId());
            currentDrug.setName(editVisit.getDrugName());
            try {
                currentFluid = new Fluid();
                currentFluid.setId(editVisit.getFluidId());
                currentFluid.setName(editVisit.getFluidName());
                fluidCombo.getSelectionModel().select((currentFluid.getName() == null) ? SELECT : currentFluid.getName());
                noteTxt.setText(editVisit.getNote());
            } catch (NullPointerException ex) {
                fluidCombo.getSelectionModel().select(SELECT);
            }
            drugTxt.setText(currentDrug.getName());
            currentDrug.setStock(foundDrug.get(0).getStock());
            doseTxt.setText(editVisit.getDose());
            visitTable.remove(editVisit);
            setTablenumbers();
            pop.hide();
            editDose = Integer.parseInt(editVisit.getDose());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please Select Drug !!", ButtonType.OK);
            alert.show();
        }

    }

    @FXML
    private void deleteTableItem(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            visitTable.remove(table.getSelectionModel().getSelectedItem());
            setTablenumbers();
        }

    }

    @FXML
    private void savePrescription(ActionEvent event) {
        if (visitTable.size() > 0) {
            try {
                if (update) {
                    ArrayList<VisitData> save = new ArrayList<>();
                    for (VisitData visitData : visitTable) {
                        visitData.setPrescription(currentPres.getId());
                        save.add(visitData);
                    }
                    ADD_VISIT.deleteVisitDetails(currentPres);
                    ADD_VISIT.addNewVisitDetails(save);

                } else {
                    currentPres.setPatientId(currentPat.getPatient().getId());
                    currentPres.setUser(currentUser.getId());
                    if (isPrescHaveCemo() && checkTableDose()) {
                        currentPres.setId(PRES_MANAGE.addPrescription(currentPres, Prescription.DRUG_OUT_STOCK));
                    } else {
                        currentPres.setId(PRES_MANAGE.addPrescription(currentPres, Prescription.NEW_ADD));
                    }
                    MOVE.updateMovement(currentPat, PatientMovement.DRUG_ADD, PatientMovement.SEVRICE_TYPE_DRUG);
                    ArrayList<VisitData> save = new ArrayList<>();
                    for (VisitData visitData : visitTable) {
                        visitData.setPrescription(currentPres.getId());
                        save.add(visitData);
                    }
                    ADD_VISIT.addNewVisitDetails(save);
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Patient sent to pharmacy ", ButtonType.OK);
                printPrescription();
                closeWindow(event);
//                Optional<ButtonType> op = alert.showAndWait();
//                if (op.isPresent() && op.get().equals(ButtonType.OK)) {
//                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void handelKeyShortcut(KeyEvent event) {
        ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
        if (event.getCode().equals(KeyCode.F11)) {
            savePrescription(ae);
        } else if (event.getCode().equals(KeyCode.F9)) {
            addDrugToTable(ae);
        }

    }

    private void setPat() {
        try {
            nameTxt.setText(currentPat.getPatient().getName());
            idTxt.setText(currentPat.getPatient().getPatId());
            comboDia.getSelectionModel().select(currentPat.getPatient().getDiagnosis().getName());
            txtSurface.setText(String.valueOf(currentPat.getPatient().getSurface()));
            txtWeight.setText(String.valueOf(currentPat.getPatient().getWeight()));
            txtAge.setText(String.valueOf(Helpers.calculateAge(currentPat.getPatient().getBirth().toLocalDate())));
            if (!update) {
                ArrayList<Date> allVisits = PAT_MANAGE.getPatVisits(currentPat.getPatient().getId());
                if (!allVisits.isEmpty()) {
                    for (Date visit : allVisits) {
                        comboLastVisit.getItems().add(visit);
                    }
                }
                if (comboLastVisit.getItems().size() > 0) {
                    comboLastVisit.getSelectionModel().selectFirst();
                }

            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.show();
        }

    }

    public PatientMovement getCurrentPat() {
        return currentPat;
    }

    public void setCurrentPat(PatientMovement currentPat) {
        this.currentPat = currentPat;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private void drugAutoCompleat() {
        try {
            if (drugTxt.getText().equals("")) {
                if (pop.isShowing()) {
                    pop.hide();
                }
                return;
            }
            getDrugs();
            pop.getItems().clear();
            pattern = Pattern.compile(drugTxt.getText() + ".*", Pattern.CASE_INSENSITIVE);
            for (Drug drug : foundDrug) {
                drugMap.put(drug.getName(), drug);
                Matcher matcher = pattern.matcher(drug.getName());
                if (matcher.matches()) {
                    MenuItem m = new MenuItem(drug.getName());
                    m.setStyle("-fx-pref-width:" + drugTxt.getWidth() + ";");
                    m.setOnAction((event) -> {
                        currentDrug = drugMap.get(m.getText());
                        drugTxt.setText(currentDrug.getName());
                        noteTxt.setText(currentDrug.getNote());
                        pop.hide();
                        doseTxt.requestFocus();
                        boolean flage = true;
                        for (Drug fl : allFluid) {
                            if (fl.getId() == currentDrug.getDefFluid()) {
                                fluidCombo.getSelectionModel().select(fl.getName());
                                volumeTxt.setText(String.valueOf(currentDrug.getDefVolume()));
                                flage = false;
                                break;
                            }
                        }
                        if (flage) {
                            fluidCombo.getSelectionModel().select(SELECT);
                        }
                    });
                    pop.getItems().add(m);
                }

            }
            pop.show(drugTxt, Side.BOTTOM, 0, 0);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    private void autoDrug() {
        drugTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            drugAutoCompleat();
        });
    }

    private void getDrugs() {
        try {
            foundDrug = DRUG_MANAGE.findDrug(drugTxt.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setComboboxData() {
        try {
            allFluid = DRUG_MANAGE.allFluid();
            fluidCombo.getItems().add(SELECT);
            for (Drug fluid : allFluid) {
                fluidMap.put(fluid.getName(), fluid);
                fluidCombo.getItems().add(fluid.getName());
            }

        } catch (SQLException ex) {
            Logger.getLogger(AddDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean validateDrugToAddToTable() {
        if (currentDrug == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please Choose Drug First !!", ButtonType.OK);
            alert.show();
            drugTxt.requestFocus();
            return false;
        }
        if (doseTxt.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please Enter Dose First  !!", ButtonType.OK);
            alert.show();
            doseTxt.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateDuplicate(VisitData newVisit) {
        for (VisitData visitData : visitTable) {
            if (newVisit.getDrugId() == visitData.getDrugId()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Drug already exists", ButtonType.OK);
                alert.show();
                drugTxt.requestFocus();
                return false;
            }
        }
        return true;
    }

    @FXML
    private void comboChange(ActionEvent event) {
        try {
            String selectedItem = fluidCombo.getSelectionModel().getSelectedItem();
            for (Drug fluid : allFluid) {
                if (fluid.getName().equals(selectedItem)) {
                    currentFluid = fluid;
                }
            }
            if (fluidCombo.getSelectionModel().getSelectedItem().equals(SELECT)) {
                volumeTxt.setText("0");
            } else {
                volumeTxt.setText("1");
            }
        } catch (Exception e) {
            fluidCombo.getSelectionModel().select(SELECT);
        }
    }

    private void setTableData() {
        tNumber.setCellValueFactory(new PropertyValueFactory<>("no"));
        tDrug.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        tDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        tFluid.setCellValueFactory(new PropertyValueFactory<>("fluidName"));
        tVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        tNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        table.setItems(visitTable);
        table.setRowFactory(tv -> new TableRow<VisitData>() {
            @Override
            protected void updateItem(VisitData item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getNo() % 2 == 0) {
                    setStyle("-fx-background-color: #ff9999;  -fx-font: 19px sans-serif ;");
                } else {
                    setStyle("-fx-background-color: #cc0066; -fx-font: 19px sans-serif ;");

                }
            }

        });

    }

    private void setTablenumbers() {
        int no = 1;
        for (VisitData visitData : visitTable) {
            visitData.setNo(no++);
        }
    }

    private void clearDrugArea() {
        currentDrug = null;
        currentFluid = null;
        doseTxt.setText("");
        fluidCombo.getSelectionModel().select(SELECT);
        volumeTxt.setText("");
        drugTxt.setText("");
        noteTxt.clear();

    }

    private void autoDiagnosis() {
//
//        if (diaTxt.getText().equals("")) {
//            if (pop.isShowing()) {
//                pop.hide();
//            }
//            return;
//        }
//        pop.getItems().clear();
//        pattern = Pattern.compile(diaTxt.getText() + ".*", Pattern.CASE_INSENSITIVE);
//        for (Diagnosis r : allDiagnosis) {
//            Matcher matcher = pattern.matcher(r.getName());
//            if (matcher.matches()) {
//                MenuItem m = new MenuItem(r.getName());
//                m.setStyle("-fx-pref-width:" + diaTxt.getWidth() + ";");
//                m.setOnAction((event) -> {
//                    try {
//                        pop.hide();
//                        Diagnosis dia = new Diagnosis();
//                        dia.setId(diaMap.get(m.getText()).getId());
//                        dia.setName(m.getText());
//                        diaTxt.setText(m.getText());
//                        drugTxt.requestFocus();
//                        currentDiagnosis = dia;
//                        Patient pat = new Patient();
//                        pat.setId(currentPat.getPatient().getId());
//                        pat.setDiagnosis(currentDiagnosis);
//                        new PatientManage().updatediagnosis(pat);
//                    } catch (SQLException ex) {
//                        Logger.getLogger(AddDrugController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                });
//                pop.getItems().add(m);
//            }
//
//        }
//        diaTxt.setContextMenu(pop);
//
//        pop.show(diaTxt, Side.BOTTOM, 0, 0);
    }

    private void getDiagnosis() {
        try {
            allDiagnosis = new DiagnosisManage().allDiagnosis();
            for (Diagnosis dia : allDiagnosis) {
                diaMap.put(dia.getName(), dia);
                comboBoxStrings.add(dia.getName());
            }
            comboDia.setItems(FXCollections.observableArrayList(comboBoxStrings));

        } catch (SQLException ex) {
            Logger.getLogger(AddDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setOldVisit() {
        update = true;
        try {
            ArrayList<Prescription> allPres = PRES_MANAGE.patientVisits(currentPat, currentPat.getDate());
            for (Prescription allPre : allPres) {
                currentPres = allPre;
            }
            visitTable = FXCollections.observableArrayList(ADD_VISIT.patientVisits(currentPres));
            table.setItems(visitTable);
        } catch (SQLException ex) {
            Logger.getLogger(AddDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setOldVisit(Prescription pres) {
        update = true;
        try {
            currentPres = pres;
            visitTable = FXCollections.observableArrayList(ADD_VISIT.patientVisits(pres));
            table.setItems(visitTable);
        } catch (SQLException ex) {
        }
    }

    @FXML
    private void clearFileds(ActionEvent event) {
        if (currentDrug != null) {
            if (editFlage) {
                setOldDose = true;
                addDrugToTable(event);
                setOldDose = false;
                editDose = 0;
            }
        }
        drugTxt.clear();
        currentDrug = null;
        doseTxt.clear();
        volumeTxt.clear();
        noteTxt.clear();

    }

    @FXML
    private void clearTableItems(ActionEvent event) {
        visitTable.clear();
        table.getItems().clear();
        drugOutStock = false;
        drugStockProceed = false;

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void visitSelectionChange(ActionEvent event) {
        try {
            if (!comboLastVisit.getSelectionModel().isEmpty()) {
                Date selected = comboLastVisit.getSelectionModel().getSelectedItem();
                ArrayList<Prescription> allPres = PRES_MANAGE.patientVisits(currentPat, selected);
                Prescription tempPres = null;
                for (Prescription allPre : allPres) {
                    tempPres = allPre;
                }
                if (null != tempPres) {
                    visitTable = FXCollections.observableArrayList(ADD_VISIT.patientVisits(tempPres));
                    checkTableDose();
                    table.setItems(visitTable);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void checkDose() {
        doseTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    float dose = Float.parseFloat(doseTxt.getText());
                    if (dose > currentDrug.getStock()) {
                        if (drugStockProceed) {
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "Drug dose is larger than Stock \n do you want to procced ?", ButtonType.YES, ButtonType.NO);
                            alert.setHeaderText("Dose Alert");
                            Optional<ButtonType> res = alert.showAndWait();
                            if (res.isPresent() && res.get().equals(ButtonType.YES)) {
                                drugStockProceed = true;
                                drugOutStock = true;
                            }else{
                                clearDrugArea();
                                drugTxt.requestFocus();
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                }
            }
        });
    }

    private void colseUi(ActionEvent event) {
        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
        current.hide();
    }

    private void printPrescription() {
        Thread t = new Thread(() -> {
            RunReport runReport = new RunReport();
            HashMap<String, Object> params = new HashMap<>();
            params.put("presNo", currentPres.getId());
            params.put("name", currentPat.getPatient().getName());
            params.put("id", currentPat.getPatient().getId());
            params.put("barcode", currentPat.getPatient().getPatId());
            params.put("as", currentPat.getPatient().getSurface());
            params.put("wt", currentPat.getPatient().getWeight());
            params.put("doctor", currentUser.getName());
            Date d = (currentPat.getPatient().getBirth() == null) ? Date.valueOf(LocalDate.now()) : currentPat.getPatient().getBirth();
            params.put("age", Helpers.calculateAge(currentPat.getPatient().getBirth().toLocalDate()));
            params.put("dia", currentPat.getPatient().getDiagnosis().getName());
            runReport.showReport(RunReport.PAT_DRUG_REPORT, params);
        });
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void handeldiagnosisChange(ActionEvent event) {
        if (!comboDia.getSelectionModel().isEmpty()) {
            Diagnosis selected = diaMap.get(comboDia.getSelectionModel().getSelectedItem());
            currentPat.getPatient().setDiagnosis(selected);
            try {
                PAT_MANAGE.updatediagnosis(currentPat.getPatient());
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(AddDrugController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void txtFilterDia() {
        diaTxtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                comboBoxStrings.clear();
                comboDia.getItems().clear();
                for (Diagnosis dia : allDiagnosis) {
                    if (dia.getName().startsWith(newValue)) {
                        comboBoxStrings.add(dia.getName());
                    }
                }
                comboDia.setItems(FXCollections.observableArrayList(comboBoxStrings));
                comboDia.getSelectionModel().selectFirst();
            } else {
                comboDia.setItems(FXCollections.observableArrayList(diaMap.keySet()));
            }
        });

    }

    public void setEditFildes() {
        comboDia.setDisable(true);
        diaTxtFilter.setDisable(true);
    }

    private boolean checkTableDose() {
        try {
            for (VisitData visit : visitTable) {
                if (Float.parseFloat(visit.getDose()) > DRUG_MANAGE.getDrugStock(visit.getDrugId())) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    private boolean isPrescHaveCemo() {
        for (VisitData visitData : visitTable) {
            if (visitData.getCategory() == Drug.CHEMOTHERAPY) {
                return true;
            }
        }
        return false;
    }
}
