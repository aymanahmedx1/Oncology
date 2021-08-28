/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.report;

import BAO.DoseManagement;
import BAO.DrugManagment;
import DAO.Drug;
import com.jfoenix.controls.JFXButton;
import commons.ExcelExport;
import commons.RunReport;
import controller.reception.ReceptionController;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class SearchForDrugController implements Initializable {

    private Stage stage;
    private ContextMenu pop = new ContextMenu();
    private Pattern pattern;
    private static final DrugManagment DRUG_MANAGE = new DrugManagment();
    private ArrayList<Drug> foundDrug;
    private Drug currentDrug;
    private HashMap<String, Drug> drugMap = new HashMap<>();
    private final DoseManagement DOSE_MANAGE = new DoseManagement();
    private ObservableList<Drug> allDrugs = FXCollections.observableArrayList();
    private ObservableList<Drug> filterdDrug = FXCollections.observableArrayList();
    @FXML
    private JFXButton btnclear;
    @FXML
    private TextField txtDrugNameFilter;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private JFXButton search;
    @FXML
    private TableView<Drug> table;
    @FXML
    private TableColumn<Drug, String> colName;
    @FXML
    private TableColumn<Drug, Float> colDose;
    @FXML
    private TableColumn<Drug, Float> colVials;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDateConverter();
        setTableUi();
        drugAutoCompleat();
        autuDrug();
    }

    @FXML
    private void printPage(ActionEvent event) {
        if (currentDrug != null) {
            RunReport runReport = new RunReport();
            HashMap<String, Object> params = new HashMap<>();
            params.put("dfrom", fromDate.getValue().toString());
            params.put("dto", toDate.getValue().toString());
            params.put("drug_id", currentDrug.getId());
            runReport.showReport(RunReport.SERACH_FOR_DRUG, params);
        }

    }

    @FXML
    private void toExcel(ActionEvent event) {
        if (currentDrug != null) {
            ExcelExport ex = new ExcelExport();
            ex.ExportDrugDose(stage, table.getItems().get(0), fromDate.getValue(), toDate.getValue());
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        currentDrug = null;
        txtDrugNameFilter.clear();
        fromDate.setValue(LocalDate.now());
        toDate.setValue(LocalDate.now());
        table.getItems().clear();
    }

    @FXML
    private void searchBtn(ActionEvent event) {
        if (currentDrug != null) {
            try {
                table.getItems().clear();
                Date from = Date.valueOf(fromDate.getValue());
                Date to = Date.valueOf(toDate.getValue());
                allDrugs = FXCollections.observableArrayList(DOSE_MANAGE.getSearchForDrug(from, to, currentDrug));
                table.setItems(allDrugs);
            } catch (SQLException ex) {
                Logger.getLogger(SearchForDrugController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void getVisitData(MouseEvent event) {
    }

    private void setDateConverter() {
        fromDate.setValue(LocalDate.now());
        toDate.setValue(LocalDate.now());
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

    private void setTableUi() {
        table.setId("my-table");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDose.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colVials.setCellValueFactory(new PropertyValueFactory<>("floatOfVials"));
        table.setItems(allDrugs);

    }

    private void drugAutoCompleat() {
        try {
            if (txtDrugNameFilter.getText().equals("")) {
                if (pop.isShowing()) {
                    pop.hide();
                }
                return;
            }
            getDrugs();
            pop.getItems().clear();
            pattern = Pattern.compile(txtDrugNameFilter.getText() + ".*", Pattern.CASE_INSENSITIVE);
            for (Drug drug : foundDrug) {
                drugMap.put(drug.getName(), drug);
                Matcher matcher = pattern.matcher(drug.getName());
                if (matcher.matches()) {
                    MenuItem m = new MenuItem(drug.getName());
                    m.setStyle("-fx-pref-width:" + txtDrugNameFilter.getWidth() + ";");
                    m.setOnAction((event) -> {
                        currentDrug = drugMap.get(m.getText());
                        txtDrugNameFilter.setText(currentDrug.getName());
                        pop.hide();

                    });
                    pop.getItems().add(m);
                }

            }
            pop.show(txtDrugNameFilter, Side.BOTTOM, 0, 0);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    private void getDrugs() {
        try {
            foundDrug = DRUG_MANAGE.findDrug(txtDrugNameFilter.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void autuDrug() {
        txtDrugNameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            drugAutoCompleat();
        });
    }
}
