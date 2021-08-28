/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pharmacy;

import DAO.Prescription;
import DAO.VisitData;
import commons.RunReport;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class PrintLabelController implements Initializable {

    private Stage stage;
    private Prescription presc;
    private VisitData visit;
    @FXML
    private Button btnprint;
    @FXML
    private Label lblPatNme;
    @FXML
    private Label lblDrugName;
    @FXML
    private Label lblDate;
    @FXML
    private TextField txtFree;
    @FXML
    private TextField txtNote;
    @FXML
    private TextField txtDose;
    @FXML
    private Label lblId;
    @FXML
    private Label lblNo;
    @FXML
    private Label lblNo1;
    private boolean finish = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setData();
        });
    }

    private void setData() {
        lblPatNme.setText(presc.getPatientName());
        lblId.setText(presc.getPatientNumber());
        lblDate.setText(String.valueOf(presc.getDate()));
        lblDrugName.setText(visit.getDrugName());
        txtFree.setText("");
        txtNote.setText(visit.getNote());
        lblNo.setText(String.valueOf(presc.getNo()));
        txtDose.setText(visit.getDose());
    }

    @FXML
    private void print(ActionEvent event) {
        try {
            float test = Float.parseFloat(txtDose.getText());
            String time = DateTimeFormatter.ofPattern("hh:mm a yyyy/MM/dd ").format(LocalDateTime.now());
            RunReport runReport = new RunReport();
            HashMap<String, Object> params = new HashMap<>();
            params.put("pr_name", lblPatNme.getText());
            params.put("pr_no", lblNo.getText());
            params.put("pr_date", time);
            params.put("pr_note", txtNote.getText());
            params.put("pr_dose", txtDose.getText());
            params.put("pr_exnote", txtFree.getText());
            params.put("pr_id", lblId.getText());
            params.put("prescription", visit.getId());
            runReport.showReport(RunReport.DRUG_LABEL, params);
            finish = true;
            stage.hide();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Some thing went wrong " + e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    public VisitData getVisit() {
        return visit;
    }

    public void setVisit(VisitData visit) {
        this.visit = visit;
    }

    public Prescription getPresc() {
        return presc;
    }

    public void setPresc(Prescription presc) {
        this.presc = presc;
    }

    public boolean finish() {
        return finish;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
