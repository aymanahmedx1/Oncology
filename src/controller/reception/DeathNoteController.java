/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.reception;

import BAO.AddressManage;
import BAO.patient.PatientState;
import DAO.Address;
import DAO.patient.Patient;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import commons.Educationstate;
import static commons.Helpers.PAT_MANAGE;
import commons.JobState;
import commons.Relationstate;
import java.net.URL;
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
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class DeathNoteController implements Initializable {

    private Stage stage;
    private final Educationstate EDU_STATE = new Educationstate();
    private final Relationstate REL_STATe = new Relationstate();
    private final JobState JOB_State = new JobState();
    private ContextMenu pop = new ContextMenu();
    Pattern pattern;
    private ArrayList<Patient> allPatient;
    private HashMap<String, Patient> patientMap = new HashMap<>();
    private final PatientState PAT_STATE = new PatientState();
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
    private TextField txtMotherName;
    @FXML
    private TextField regionTxt;
    @FXML
    private TextField txtAdress1;
    @FXML
    private TextField txtAdress2;
    @FXML
    private TextField doctorTxt;
    @FXML
    private TextField diagnosisTxt;
    @FXML
    private TextField txtIcd;
    @FXML
    private TextField txtsurface;
    @FXML
    private TextField txtweight;
    @FXML
    private TextField txtHeight;
    @FXML
    private JFXButton clearBtn;
    @FXML
    private Button sendDoctorBtn;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField txtDOB;
    @FXML
    private TextField txtDOE;
    @FXML
    private TextField txtGender;
    @FXML
    private TextField txtJob;
    @FXML
    private TextField txtRelation;
    @FXML
    private TextField txtEducation;
    @FXML
    private JFXTextArea txtDeathNote;
    private Patient currentPatient;
    private Address currenAddress;
    private final AddressManage ADDRESS_MANAGE = new AddressManage();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            AutoAll();
        });
    }

    @FXML
    private void printBarcode(ActionEvent event) {
    }

    @FXML
    private void clear(ActionEvent event) {
        clear();
    }

    @FXML
    private void save(ActionEvent event) {
        if (currentPatient == null) {
            return;
        }
        try {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The patient will be marked as dead ", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> res = alert.showAndWait();
            if (res.isPresent() && res.get().equals(ButtonType.YES)) {
                PAT_STATE.UpdatePatientState(Patient.DEATH, currentPatient);
                PAT_STATE.addDeathNote(currentPatient, txtDeathNote.getText());
                setMessage("Saved Success");
                clear();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DeathNoteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void AutoAll() {
        idTxt.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            idAuto();
        });

        pop.getStyleClass().add("menu");
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
            patientMap.put(r.getPatId(), r);
            MenuItem m = new MenuItem(r.getPatId());
            m.setStyle("-fx-pref-width:" + idTxt.getWidth() + ";");
            m.setOnAction((event) -> {
                pop.hide();
                currentPatient = patientMap.get(m.getText());
                setPatToUi();
            });
            pop.getItems().add(m);

        }
        idTxt.setContextMenu(pop);

        pop.show(idTxt, Side.BOTTOM, 0, 0);
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
            patientMap.put(r.getName(), r);
            MenuItem m = new MenuItem(r.getName());
            m.setStyle("-fx-pref-width:" + nameTxt.getWidth() + ";");
            m.setOnAction((event) -> {
                pop.hide();
                currentPatient = patientMap.get(m.getText());
                setPatToUi();
            });
            pop.getItems().add(m);

        }
        nameTxt.setContextMenu(pop);

        pop.show(nameTxt, Side.BOTTOM, 0, 0);
    }

    private void setPatToUi() {
        try {
            currenAddress = ADDRESS_MANAGE.getPatadress(currentPatient);
            barcodeTxt.setText(String.valueOf(currentPatient.getId()));
            nameTxt.setText(currentPatient.getName());
            idTxt.setText(currentPatient.getPatId());
            phoneTxt.setText(currentPatient.getPhone());
            txtDOB.setText(currentPatient.getBirth().toString());
            regionTxt.setText(currentPatient.getRegion().getName());
            txtDOE.setText(currentPatient.getEntry().toString());
            doctorTxt.setText(currentPatient.getDoctor().getName());
            diagnosisTxt.setText(currentPatient.getDiagnosis().getName());
            txtsurface.setText(String.valueOf(currentPatient.getSurface()));
            txtweight.setText(String.valueOf(currentPatient.getWeight()));
            txtIcd.setText(currentPatient.getIcd10());
            txtGender.setText((currentPatient.getGender() == Patient.MALE) ? "Male" : "Female");
            txtJob.setText(JOB_State.getRelationName(currentPatient.getJob()));
            txtEducation.setText(EDU_STATE.getRelationName(currentPatient.getEducation()));
            txtRelation.setText(REL_STATe.getRelationName(currentPatient.getRelation()));
            txtAdress1.setText(currenAddress.getAdd1());
            txtAdress2.setText(currenAddress.getAdd2());
            txtHeight.setText(currentPatient.getHeight() + "");
            txtMotherName.setText(currentPatient.getMotherName());
            if (currentPatient.getBlackList() == Patient.DEATH) {
                txtDeathNote.setText(PAT_STATE.getDeathNote(currentPatient).getNote());
                sendDoctorBtn.setDisable(true);
            } else {
                sendDoctorBtn.setDisable(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getPatient() {
        try {
            allPatient = PAT_MANAGE.findPatients(nameTxt.getText());
        } catch (SQLException ex) {
        }
    }

    private void getPatientByFileID() {
        try {
            allPatient = PAT_MANAGE.findPatID(idTxt.getText());
        } catch (SQLException ex) {
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void clear() {
        currentPatient = null;
        barcodeTxt.clear();
        nameTxt.clear();
        idTxt.clear();
        phoneTxt.clear();
        regionTxt.clear();
        txtDOE.clear();
        doctorTxt.clear();
        diagnosisTxt.clear();
        txtDOB.clear();
        nameTxt.requestFocus();
        txtIcd.clear();
        txtsurface.clear();
        txtweight.clear();
        currenAddress = null;
        txtAdress1.clear();
        txtAdress2.clear();
        txtJob.clear();
        txtRelation.clear();
        txtEducation.clear();
        txtMotherName.clear();
        txtHeight.clear();
    }

    @FXML
    private void handelF10(KeyEvent event) {
        if (event.getCode().equals(KeyCode.F10)) {
            clear();
        } else if (event.getCode().equals(KeyCode.F12)) {
            ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
            save(ae);
        }
    }

    private void setMessage(String text) {
        messageLabel.setText(text);

        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(ev -> {
            messageLabel.setText("");
        });
        delay.play();
    }
}
