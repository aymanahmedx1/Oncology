/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.reception;

import BAO.AddressManage;
import BAO.RegionManage;
import BAO.patient.PatientState;
import DAO.Address;
import DAO.DeathInfo;
import DAO.Region;
import DAO.ReportData;
import DAO.patient.Patient;
import DAO.patient.PatientMovement;
import DAO.user.User;
import com.jfoenix.controls.JFXButton;
import commons.DBConnection;
import commons.Educationstate;
import commons.Helpers;
import static commons.Helpers.MOVEMENT;
import static commons.Helpers.PAT_MANAGE;
import static commons.Helpers.USER;
import commons.JobState;
import commons.Relationstate;
import commons.RunReport;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JREmptyDataSource;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class ReceptionController implements Initializable {
    
    private final Educationstate EDU_STATE = new Educationstate();
    private final Relationstate REL_STATe = new Relationstate();
    private final JobState JOB_State = new JobState();
    private final AddressManage ADDRESS_MANAGE = new AddressManage();
    private Connection con;
    private static final RegionManage REGION = new RegionManage();
    private ArrayList<Region> allRegion;
    private ArrayList<Patient> allPatient;
    private ArrayList<User> allDoctor;
    private HashMap<String, Integer> regionMap = new HashMap<>();
    private HashMap<String, Patient> patentMap = new HashMap<>();
    private HashMap<String, Integer> doctorMap = new HashMap<>();
    private HashMap<String, Integer> genderMap = new HashMap<>();
    private Stage stage;
    @FXML
    private AnchorPane pane;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton editBtn;
    @FXML
    private JFXButton clearBtn;
    @FXML
    private TextField barcodeTxt;
    @FXML
    private JFXButton printBtn;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField idTxt;
    @FXML
    private TextField phoneTxt;
    @FXML
    private DatePicker birthTxt;
    @FXML
    private Button sendDoctorBtn;
    @FXML
    private DatePicker entryTxt;
    @FXML
    private TextField regionTxt;
    @FXML
    private TextField doctorTxt;
    Pattern pattern;
    private ContextMenu pop = new ContextMenu();
    private Region region;
    private User doctor;
    private Patient currentPatient;
    private boolean flage = false;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField txtsurface;
    @FXML
    private TextField txtweight;
    @FXML
    private ComboBox<String> comboGender;
    @FXML
    private TextField txtIcd;
    @FXML
    private JFXButton clearBtn1;
    @FXML
    private TextField txtAdress1;
    @FXML
    private TextField txtAdress2;
    @FXML
    private TextField diagnosisTxt;
    @FXML
    private ComboBox<String> ComboJob;
    @FXML
    private ComboBox<String> ComboRelasionShip;
    @FXML
    private ComboBox<String> ComboEducation;
    private Address currenAddress;
    @FXML
    private TextField txtMotherName;
    @FXML
    private TextField txtHeight;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            try {
                entryTxt.setValue(LocalDate.now());
                getData();
                autotAll();
                setDateConverter();
                con = DBConnection.createConnection();
                barcodeTxt.requestFocus();
                sendDoctorBtn.setDisable(true);
                onOffFileds(true);
                setGenderCombo();
            } catch (SQLException ex) {
                Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @FXML
    private void savePat(ActionEvent event) {
        if (currentPatient != null || flage) {
            return;
        }
        String checkResult = checkData();
        if (checkResult.equals("")) {
            saveNew();
        } else {
            showPop("Information Is Missing", checkResult, "Warinning");
            switch (checkResult) {
                case "Name is required":
                    nameTxt.requestFocus();
                    break;
                case "ID is required":
                    idTxt.requestFocus();
                    break;
                case "Phone is required":
                    phoneTxt.requestFocus();
                    break;
                case "Birth Date is required":
                    birthTxt.requestFocus();
                    break;
                case "Region is required":
                    regionTxt.requestFocus();
                    break;
                case "doctor is required":
                    doctorTxt.requestFocus();
                    break;
                default:
                    break;
            }
        }
        
    }
    
    @FXML
    private void editPat(ActionEvent event) {
        if (currentPatient == null) {
            return;
        }
        if (flage) {
            try {
                String checkResult = checkData();
                if (checkResult.equals("")) {
                    Patient pat = new Patient();
                    pat.setId(Integer.parseInt(barcodeTxt.getText()));
                    pat.setName(nameTxt.getText());
                    pat.setPatId(idTxt.getText());
                    pat.setPhone(phoneTxt.getText());
                    pat.setBirth(Date.valueOf(birthTxt.getValue()));
                    pat.setRegion(region);
                    pat.setEntry(Date.valueOf(entryTxt.getValue()));
                    pat.setDoctor(doctor);
                    pat.setIcd10(txtIcd.getText());
                    pat.setSurface(Float.parseFloat(txtsurface.getText()));
                    pat.setWeight(Float.parseFloat(txtweight.getText()));
                    pat.setGender(genderMap.get(comboGender.getSelectionModel().getSelectedItem()));
                    pat.setJob(JOB_State.getRelationNo(ComboJob.getSelectionModel().getSelectedItem()));
                    pat.setRelation(REL_STATe.getRelationNo(ComboRelasionShip.getSelectionModel().getSelectedItem()));
                    pat.setEducation(EDU_STATE.getRelationNo(ComboEducation.getSelectionModel().getSelectedItem()));
                    if (currenAddress.getId() == 0) {
                        currenAddress = new Address();
                        currenAddress.setAdd1((txtAdress1.getText().isEmpty()) ? "" : txtAdress1.getText());
                        currenAddress.setAdd2((txtAdress2.getText().isEmpty()) ? "" : txtAdress2.getText());
                        pat.setAddress(ADDRESS_MANAGE.add(currenAddress));
                    } else {
                        currenAddress.setAdd1((txtAdress1.getText().isEmpty()) ? "" : txtAdress1.getText());
                        currenAddress.setAdd2((txtAdress2.getText().isEmpty()) ? "" : txtAdress2.getText());
                    }
                    pat.setHeight(Float.parseFloat(txtHeight.getText()));
                    pat.setMotherName(txtMotherName.getText());
                    ADDRESS_MANAGE.updateAdress(currenAddress);
                    currentPatient = checkDuplicate(pat);
                    PAT_MANAGE.update(pat);
                    currentPatient = PAT_MANAGE.findByBarcode(pat.getId());
                    setPatToUi();
                    setMessage("Updated Succesfully");
                } else {
                    showPop("Information Is Missing", checkResult, "Warinning");
                    switch (checkResult) {
                        case "Name is required":
                            nameTxt.requestFocus();
                            break;
                        case "ID is required":
                            idTxt.requestFocus();
                            break;
                        case "Phone is required":
                            phoneTxt.requestFocus();
                            break;
                        case "Birth Date is required":
                            birthTxt.requestFocus();
                            break;
                        case "Region is required":
                            regionTxt.requestFocus();
                            break;
                        case "doctor is required":
                            doctorTxt.requestFocus();
                            break;
                        default:
                            break;
                    }
                }
                
            } catch (NumberFormatException ex) {
                setMessage("Please Check " + ex.getMessage());
            } catch (SQLException ex) {
                Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setFlag();
        
    }
    
    @FXML
    private void clear(ActionEvent event) {
        region = null;
        doctor = null;
        currentPatient = null;
        barcodeTxt.clear();
        nameTxt.clear();
        idTxt.clear();
        phoneTxt.clear();
        regionTxt.clear();
        entryTxt.setValue(LocalDate.now());
        doctorTxt.clear();
        diagnosisTxt.clear();
        birthTxt.getEditor().clear();
        nameTxt.requestFocus();
        txtIcd.clear();
        txtsurface.clear();
        txtweight.clear();
        onOffFileds(true);
        editBtn.setText("Edit");
        currenAddress = null;
        txtAdress1.clear();
        txtAdress2.clear();
        ComboJob.getSelectionModel().clearSelection();
        ComboRelasionShip.getSelectionModel().clearSelection();
        ComboEducation.getSelectionModel().clearSelection();
        txtMotherName.clear();
        txtHeight.clear();
    }
    
    @FXML
    private void printBarcode(ActionEvent event) {
        if (currentPatient != null) {
            HashMap<String, Object> h = new HashMap<>();
            h.put("parameter1", currentPatient.getId());
            h.put("parameter2", currentPatient.getName());
            ReportData report = new ReportData("src/report/barcode.jrxml", "barcode", h, new JREmptyDataSource(), "test");
            RunReport.runReport(report);
        }
    }
    
    @FXML
    private void sendDoctor(ActionEvent event) {
        if (currentPatient != null) {
            if (currentPatient.getBlackList() == Patient.DEATH) {
                setMessage("this patient is dead ! ");
                return;
            }
            if (flage) {
            } else {
                try {
                    if (!checkIfPatHaveVisit() && !Helpers.PRES_MANAGE.isPatHasPrescToday(currentPatient.getId())) {
                        PatientMovement pat = new PatientMovement();
                        pat.setDoctor(currentPatient.getDoctor().getId());
                        pat.setPatient(currentPatient);
                        pat.setServiceType(PatientMovement.SEVRICE_TYPE_CONSULTANT);
                        MOVEMENT.addMovment(pat);
                        clear(event);
                        setMessage("Sent Succesfully");
                        sendDoctorBtn.setDisable(true);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Patient Have Visit Today", ButtonType.OK);
                        Optional<ButtonType> res = alert.showAndWait();
                        if (res.isPresent() && res.get().equals(ButtonType.OK)) {
                            clear(event);
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        } else {
            
            showPop("Patient Is Missing", "Please save data first", "Save first");
        }
    }
    
    @FXML
    private void handelKeyShortcut(KeyEvent event) {
        ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
        switch (event.getCode()) {
            case F10:
                clear(ae);
                break;
            case F12:
                sendDoctor(ae);
                break;
            case F8:
                savePat(ae);
                break;
            default:
                break;
        }
    }
    
    private void getData() {
        try {
            allRegion = REGION.allRegion();
            for (Region rg : allRegion) {
                regionMap.put(rg.getName(), rg.getId());
            }
            allDoctor = USER.allDoctor();
            for (User user : allDoctor) {
                doctorMap.put(user.getName(), user.getId());
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void regionAuto() {
        if (regionTxt.getText().equals("")) {
            if (pop.isShowing()) {
                pop.hide();
            }
            return;
        }
        pop.getItems().clear();
        pattern = Pattern.compile(regionTxt.getText() + ".*", Pattern.CASE_INSENSITIVE);
        for (Region r : allRegion) {
            Matcher matcher = pattern.matcher(r.getName());
            if (matcher.matches()) {
                MenuItem m = new MenuItem(r.getName());
                m.setStyle("-fx-pref-width:" + regionTxt.getWidth() + ";");
                m.setOnAction((event) -> {
                    pop.hide();
                    region = new Region();
                    region.setId(regionMap.get(m.getText()));
                    region.setName(m.getText());
                    regionTxt.setText(m.getText());
                    txtAdress1.requestFocus();
                });
                pop.getItems().add(m);
            }
            
        }
        regionTxt.setContextMenu(pop);
        
        pop.show(regionTxt, Side.BOTTOM, 0, 0);
    }
    
    private void doctorAuto() {
        if (doctorTxt.getText().equals("")) {
            if (pop.isShowing()) {
                pop.hide();
            }
            return;
        }
        pop.getItems().clear();
        pattern = Pattern.compile(doctorTxt.getText() + ".*", Pattern.CASE_INSENSITIVE);
        for (User r : allDoctor) {
            Matcher matcher = pattern.matcher(r.getName());
            if (matcher.matches()) {
                MenuItem m = new MenuItem(r.getName());
                m.setStyle("-fx-pref-width:" + doctorTxt.getWidth() + ";");
                m.setOnAction((event) -> {
                    pop.hide();
                    
                    doctorTxt.setText(m.getText());
                    doctor = new User();
                    doctor.setId(doctorMap.get(m.getText()));
                    doctor.setName(m.getText());
                    doctorTxt.setText(m.getText());
                    txtIcd.requestFocus();
                    
                });
                pop.getItems().add(m);
            }
            
        }
        doctorTxt.setContextMenu(pop);
        
        pop.show(doctorTxt, Side.BOTTOM, 0, 0);
    }
    
    private void nameAuto() {
        if (nameTxt.getText().equals("")) {
            if (pop.isShowing()) {
                pop.hide();
            }
            return;
        }
        getPatient();
        pop.getItems().clear();
        for (Patient r : allPatient) {
            patentMap.put(r.getName(), r);
            MenuItem m = new MenuItem(r.getName());
            m.setStyle("-fx-pref-width:" + nameTxt.getWidth() + ";");
            m.setOnAction((event) -> {
                pop.hide();
                currentPatient = patentMap.get(m.getText());
                setPatToUi();
            });
            pop.getItems().add(m);
            
        }
        nameTxt.setContextMenu(pop);
        
        pop.show(nameTxt, Side.BOTTOM, 0, 0);
    }
    
    private void idAuto() {
        if (idTxt.getText().equals("")) {
            if (pop.isShowing()) {
                pop.hide();
            }
            return;
        }
        getPatientByFileID();
        pop.getItems().clear();
        for (Patient r : allPatient) {
            patentMap.put(r.getPatId(), r);
            MenuItem m = new MenuItem(r.getPatId());
            m.setStyle("-fx-pref-width:" + idTxt.getWidth() + ";");
            m.setOnAction((event) -> {
                pop.hide();
                currentPatient = patentMap.get(m.getText());
                setPatToUi();
            });
            pop.getItems().add(m);
            
        }
        idTxt.setContextMenu(pop);
        
        pop.show(idTxt, Side.BOTTOM, 0, 0);
    }
    
    private void autotAll() {
        idTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            idAuto();
        });
        
        pop.getStyleClass().add("menu");
        regionTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            regionAuto();
        });
        doctorTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            doctorAuto();
        });
        nameTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            nameAuto();
        });
        barcodeTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                pattern = Pattern.compile("\\d*", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(newValue);
                if (matcher.matches()) {
                    try {
                        currentPatient = PAT_MANAGE.findByBarcode(Integer.parseInt(newValue));
                        if (currentPatient != null) {
                            setPatToUi();
                            pop.hide();
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
    }
    
    private void getPatient() {
        try {
            allPatient = PAT_MANAGE.findPatients(nameTxt.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getPatientByFileID() {
        try {
            allPatient = PAT_MANAGE.findPatFileId(idTxt.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String checkData() {
        if (nameTxt.getText().equals("")) {
            return "Name is required";
        } else if (idTxt.getText().equals("")) {
            return "ID is required";
        } else if (regionTxt.getText().equals("")) {
            return "Region is required";
        } else if (doctorTxt.getText().equals("")) {
            return "doctor is required";
        } else if (phoneTxt.getText().equals("")) {
            return "Phone is required";
        } else if (region == null) {
            return "Region is required";
        } else if (ComboJob.getSelectionModel().isEmpty()) {
            return "JOB is required";
        } else if (ComboEducation.getSelectionModel().isEmpty()) {
            return "Education is required";
        } else if (ComboRelasionShip.getSelectionModel().isEmpty()) {
            return "Relation is required";
        } else {
            return "";
        }
    }
    
    public Stage getStage() {
        return stage;
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    private Patient checkDuplicate(Patient pat) {
        try {
            return PAT_MANAGE.checkPatDuplicate(pat);
            
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private void checkOldNew() {
        
    }
    
    private void saveNew() {
        try {
            Patient pat = new Patient();
            pat.setName(nameTxt.getText());
            pat.setPatId(idTxt.getText());
            pat.setPhone(phoneTxt.getText());
            pat.setBirth(Date.valueOf(birthTxt.getValue()));
            pat.setRegion(region);
            pat.setEntry(Date.valueOf(entryTxt.getValue()));
            pat.setDoctor(doctor);
            pat.setIcd10((txtIcd.getText().isEmpty()) ? "" : txtIcd.getText());
            pat.setSurface((txtsurface.getText().isEmpty()) ? 0 : Float.parseFloat(txtsurface.getText()));
            pat.setWeight((txtweight.getText().isEmpty()) ? 0 : Float.parseFloat(txtweight.getText()));
            pat.setGender(genderMap.get(comboGender.getSelectionModel().getSelectedItem()));
            pat.setJob(JOB_State.getRelationNo(ComboJob.getSelectionModel().getSelectedItem()));
            pat.setRelation(REL_STATe.getRelationNo(ComboRelasionShip.getSelectionModel().getSelectedItem()));
            pat.setEducation(EDU_STATE.getRelationNo(ComboEducation.getSelectionModel().getSelectedItem()));
            currentPatient = checkDuplicate(pat);
            if (currentPatient == null) {
                currenAddress = new Address();
                currenAddress.setAdd1((txtAdress1.getText().isEmpty()) ? "" : txtAdress1.getText());
                currenAddress.setAdd2((txtAdress2.getText().isEmpty()) ? "" : txtAdress2.getText());
                pat.setHeight(Float.parseFloat(txtHeight.getText()));
                pat.setMotherName(txtMotherName.getText());
                pat.setAddress(ADDRESS_MANAGE.add(currenAddress));
                pat.setId(PAT_MANAGE.add(pat));
                currentPatient = pat;
                setPatToUi();
                setMessage("Saved Succesfully");
                onOffFileds(false);
                sendDoctorBtn.setDisable(false);
            } else {
                if (idTxt.getText().equals(currentPatient.getPatId())) {
                    String message = " There is another patient have same ID \n"
                            + currentPatient.getName() + "\n"
                            + "Do you want to replace name ??";
                    Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> op = alert.showAndWait();
                    if (op.isPresent() && op.get() == ButtonType.YES) {
                        setPatToUi();
                    } else if (op.isPresent() && op.get() == ButtonType.NO) {
                        currentPatient = null;
                        idTxt.clear();
                        idTxt.requestFocus();
                        onOffFileds(true);
                        sendDoctorBtn.setDisable(true);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            setMessage("Please Check " + ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void saveOld() {
    }
    
    private void setPatToUi() {
        try {
            currenAddress = ADDRESS_MANAGE.getPatadress(currentPatient);
            barcodeTxt.setText(String.valueOf(currentPatient.getId()));
            nameTxt.setText(currentPatient.getName());
            idTxt.setText(currentPatient.getPatId());
            phoneTxt.setText(currentPatient.getPhone());
            birthTxt.setValue(currentPatient.getBirth().toLocalDate());
            regionTxt.setText(currentPatient.getRegion().getName());
            entryTxt.setValue(currentPatient.getEntry().toLocalDate());
            doctorTxt.setText(currentPatient.getDoctor().getName());
            diagnosisTxt.setText(currentPatient.getDiagnosis().getName());
            region = currentPatient.getRegion();
            doctor = currentPatient.getDoctor();
            txtsurface.setText(String.valueOf(currentPatient.getSurface()));
            txtweight.setText(String.valueOf(currentPatient.getWeight()));
            txtIcd.setText(currentPatient.getIcd10());
            comboGender.getSelectionModel().select((currentPatient.getGender() == Patient.MALE) ? "Male" : "Female");
            ComboJob.getSelectionModel().select(JOB_State.getRelationName(currentPatient.getJob()));
            ComboEducation.getSelectionModel().select(EDU_STATE.getRelationName(currentPatient.getEducation()));
            ComboRelasionShip.getSelectionModel().select(REL_STATe.getRelationName(currentPatient.getRelation()));
            txtAdress1.setText(currenAddress.getAdd1());
            txtAdress2.setText(currenAddress.getAdd2());
            txtHeight.setText(currentPatient.getHeight() + "");
            txtMotherName.setText(currentPatient.getMotherName());
            if (currentPatient.getBlackList() == Patient.DEATH) {
                DeathInfo df = new PatientState().getDeathNote(currentPatient);
                sendDoctorBtn.setDisable(true);
                editBtn.setDisable(true);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "this patient is dead ! \n in Date : " + df.getDate(), ButtonType.OK);
                alert.setTitle("Dead Patient");
                alert.show();
            } else {
                sendDoctorBtn.setDisable(false);
                editBtn.setDisable(false);
            }
            onOffFileds(false);
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void showPop(String head, String content, String header) {
        Alert alert = new Alert(Alert.AlertType.WARNING, head, ButtonType.OK);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
    
    private void setDateConverter() {
        birthTxt.setConverter(new StringConverter<LocalDate>() {
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
        entryTxt.setConverter(new StringConverter<LocalDate>() {
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
        birthTxt.setOnAction((event) -> {
            int result = birthTxt.getValue().compareTo(LocalDate.now());
            if (result >= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, " Date Value Is Not Correct", ButtonType.OK);
                alert.setContentText("Please Choose Valid Date Before Today Date ");
                alert.show();
                birthTxt.requestFocus();
                birthTxt.setValue(LocalDate.now());
            }
        });
    }
    
    private void setFlag() {
        if (flage) {
            flage = false;
            editBtn.setText("Edit");
            onOffFileds(false);
            sendDoctorBtn.setDisable(false);
        } else {
            flage = true;
            editBtn.setText(" Update ");
            onOffFileds(true);
            sendDoctorBtn.setDisable(true);
            
        }
    }
    
    private boolean checkIfPatHaveVisit() {
        try {
            return MOVEMENT.checkIfPatHaveVisit(currentPatient);
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private void setMessage(String text) {
        messageLabel.setText(text);
        
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(ev -> {
            messageLabel.setText("");
        });
        delay.play();
    }
    
    private void onOffFileds(boolean on) {
        barcodeTxt.setEditable(on);
        nameTxt.setEditable(on);
        idTxt.setEditable(on);
        phoneTxt.setEditable(on);
        birthTxt.setEditable(on);
        doctorTxt.setEditable(on);
        regionTxt.setEditable(on);
        txtIcd.setEditable(on);
        txtsurface.setEditable(on);
        txtweight.setEditable(on);
        entryTxt.setEditable(on);
        txtAdress1.setEditable(on);
        txtAdress2.setEditable(on);
        txtMotherName.setEditable(on);
        txtHeight.setEditable(on);
    }
    
    private void setGenderCombo() {
        genderMap.put("Male", Patient.MALE);
        genderMap.put("Female", Patient.FEMALE);
        for (String string : genderMap.keySet()) {
            comboGender.getItems().add(string);
        }
        comboGender.getSelectionModel().selectFirst();
        ComboEducation.setItems(FXCollections.observableArrayList(EDU_STATE.getallState()));
        ComboJob.setItems(FXCollections.observableArrayList(JOB_State.getallState()));
        ComboRelasionShip.setItems(FXCollections.observableArrayList(REL_STATe.getallState()));
        
    }
    
    @FXML
    private void printInfo(ActionEvent event) {
        if (currentPatient != null) {
            Thread t = new Thread(() -> {
                RunReport runReport = new RunReport();
                HashMap<String, Object> params = new HashMap<>();
                params.put("pat_id", currentPatient.getId());
                params.put("job", JOB_State.getRelationName(currentPatient.getJob()));
                params.put("education", EDU_STATE.getRelationName(currentPatient.getEducation()));
                params.put("relation", REL_STATe.getRelationName(currentPatient.getRelation()));
                runReport.showReport(RunReport.PATIENT_INFORMATION, params);
            });
            t.start();
        }
    }
    
}
