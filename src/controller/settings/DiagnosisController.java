/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.settings;

import BAO.DiagnosisManage;
import DAO.Diagnosis;
import DAO.Region;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class DiagnosisController implements Initializable {

    private ObservableList<Diagnosis> allDia = FXCollections.observableArrayList();
    private ObservableList<Diagnosis> filterdDia = FXCollections.observableArrayList();
    private final DiagnosisManage DIA = new DiagnosisManage();
    private Stage stage;
    @FXML
    private TextField txtDiagnosis;
    @FXML
    private TableView<Diagnosis> table;
    @FXML
    private TableColumn<Diagnosis, String> colName;
    @FXML
    private TextField txtFilter;
    private Diagnosis cureentDia;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setTableUi();
            getData();
            txtFilter();
        });
    }

    @FXML
    private void save(ActionEvent event) {
        try {
            if (!txtDiagnosis.getText().isEmpty()) {
                if (cureentDia != null) {
                    cureentDia.setName(txtDiagnosis.getText());
                    DIA.update(cureentDia);
                } else {
                    if (checkDuplicate()) {
                        Diagnosis dia = new Diagnosis();
                        dia.setName(txtDiagnosis.getText());
                        DIA.add(dia);
                    }
                }
                clear(event);
                getData();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        txtDiagnosis.clear();
    }

    @FXML
    private void updateItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            cureentDia = table.getSelectionModel().getSelectedItem();
            txtDiagnosis.setText(cureentDia.getName());
        }
    }

    private void setTableUi() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.setItems(allDia);
    }

    private void getData() {
        try {
            allDia = FXCollections.observableArrayList(DIA.allDiagnosis());
            txtDiagnosis.clear();
            table.setItems(allDia);
        } catch (SQLException ex) {
            Logger.getLogger(RegionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void txtFilter() {
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterdDia.clear();
            for (Diagnosis dia : allDia) {
                if (dia.getName().toLowerCase().startsWith(newValue.toLowerCase())) {
                    filterdDia.add(dia);
                }
            }
            table.setItems(filterdDia);
        });
    }

    private boolean checkDuplicate() {
        for (Diagnosis dia : allDia) {
            if (txtDiagnosis.getText().equals(dia.getName())) {
                return false;
            }
        }
        return true;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
