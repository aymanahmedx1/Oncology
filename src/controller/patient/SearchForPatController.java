/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.patient;

import DAO.VisitData;
import DAO.patient.Patient;
import com.jfoenix.controls.JFXButton;
import commons.Helpers;
import static commons.Helpers.PAT_MANAGE;
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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class SearchForPatController implements Initializable {

    private Patient currentPatient;
    private ArrayList<Patient> allPatient;
    private HashMap<String, Patient> patentMap = new HashMap<>();
    private ObservableList<VisitData> visitTable = FXCollections.observableArrayList();
    private ObservableList<VisitData> filterdVisitTable = FXCollections.observableArrayList();
    private HashMap<String, Integer> getCategory = new HashMap<>();
    private final String ALL = "All";
    private final String CEMO = "Chemotherapy";
    private final String SUPPORT = "Supportive";
    private final String FLUID = "Fluid";
    private Stage stage;
    @FXML
    private JFXButton btnclear;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtId;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private JFXButton search;
    @FXML
    private Label nameLbl1111;
    @FXML
    private TextField txtAge;
    @FXML
    private Label phoneLbl112;
    @FXML
    private TextField txtRegoin;
    @FXML
    private Label phoneLbl111111;
    @FXML
    private TextField txtDia;
    @FXML
    private TableView<VisitData> table;
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
    @FXML
    private TableColumn<VisitData, Date> tDate;
    private ContextMenu pop = new ContextMenu();
    @FXML
    private ComboBox<String> comboDrugCat;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setDateConverter();
            autoAll();
            setTableUI();
            setCategoryComboData();
        });
    }

    @FXML
    private void printPage(ActionEvent event) {
        try {
            int selected = getCategory.get(comboDrugCat.getSelectionModel().getSelectedItem());
            RunReport runReport = new RunReport();
            HashMap<String, Object> params = new HashMap<>();
            params.put("date_from", fromDate.getValue().toString());
            params.put("date_to", toDate.getValue().toString());
            params.put("patient_id", currentPatient.getId());
            params.put("pat_name", currentPatient.getName());
            params.put("pat_id", currentPatient.getPatId());
            params.put("pat_age", Helpers.calculateAge(currentPatient.getBirth().toLocalDate()));
            params.put("pat_reg", currentPatient.getRegion().getName());
            params.put("pat_dia", currentPatient.getDiagnosis().getName());
            if (selected == 0) {
                runReport.showReport(RunReport.SERACH_FOR_PAT_ALL, params);
            } else {
                params.put("cat_id", selected);
                params.put("cat_name", comboDrugCat.getSelectionModel().getSelectedItem());
                runReport.showReport(RunReport.SERACH_FOR_PAT, params);

            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Some thing went wrong " + e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        fromDate.setValue(LocalDate.now());
        toDate.setValue(LocalDate.now());
        currentPatient = null;
        txtName.clear();
        txtId.clear();
        txtRegoin.clear();
        txtDia.clear();
        txtAge.clear();
        table.getItems().clear();
    }

    @FXML
    private void searchBtn(ActionEvent event) {
        if (currentPatient != null) {
            getPatVisits();
        }
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

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void nameAuto() {
        if (txtName.getText().equals("")) {
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
            m.setStyle("-fx-pref-width:" + txtName.getWidth() + ";");
            m.setOnAction((event) -> {
                pop.hide();
                currentPatient = patentMap.get(m.getText());
                setPatToUi();
            });
            pop.getItems().add(m);

        }
        txtName.setContextMenu(pop);

        pop.show(txtName, Side.BOTTOM, 0, 0);
    }

    private void getPatient() {
        try {
            allPatient = PAT_MANAGE.findPatients(txtName.getText());

        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void autoAll() {
        txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            nameAuto();
        });
    }

    private void setPatToUi() {
        txtName.setText(currentPatient.getName());
        txtId.setText(currentPatient.getPatId());
        txtRegoin.setText(currentPatient.getRegion().getName());
        txtDia.setText(currentPatient.getDiagnosis().getName());
        txtAge.setText(String.valueOf(Helpers.calculateAge(currentPatient.getBirth().toLocalDate())));
    }

    private void getPatVisits() {
        try {
            Date from = Date.valueOf(fromDate.getValue());
            Date to = Date.valueOf(toDate.getValue());
            visitTable = FXCollections.observableArrayList(Helpers.VISIT_MANAGE.VisitsByDate(currentPatient, from, to));
            if (visitTable.size() > 0) {
                Date d = visitTable.get(0).getDate();
                for (VisitData visitData : visitTable) {
                    if (visitData.getDate().equals(d)) {
                        visitData.setColor(false);
                    } else {
                        d = visitData.getDate();
                        visitData.setColor(true);
                    }
                }
                table.setItems(visitTable);
                handelComboChange();
            }

        } catch (SQLException ex) {
            Logger.getLogger(SearchForPatController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setTableUI() {
        tDrug.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        tDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        tFluid.setCellValueFactory(new PropertyValueFactory<>("fluidName"));
        tVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        tNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        tDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        table.setRowFactory(tv -> new TableRow<VisitData>() {
            @Override
            protected void updateItem(VisitData item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.isColor()) {
                    setStyle("-fx-border-width: 1 0 0 0; -fx-border-color: red ;");
                } else {
                    setStyle("");
                }
            }
        });

        table.setItems(visitTable);
    }

    @FXML
    private void drugCategoryChange(ActionEvent event) {
        handelComboChange();

    }

    private void setCategoryComboData() {

        comboDrugCat.getItems().add(ALL);
        comboDrugCat.getItems().add(CEMO);
        comboDrugCat.getItems().add(SUPPORT);
        comboDrugCat.getItems().add(FLUID);
        getCategory.put(ALL, 0);
        getCategory.put(CEMO, 1);
        getCategory.put(SUPPORT, 2);
        getCategory.put(FLUID, 3);
        comboDrugCat.getSelectionModel().selectFirst();
    }

    private void handelComboChange() {
        if (!comboDrugCat.getSelectionModel().isEmpty() && comboDrugCat.getSelectionModel().getSelectedItem() != null) {
            int selected = getCategory.get(comboDrugCat.getSelectionModel().getSelectedItem());
            if (selected == 0) {
                table.setItems(visitTable);

            } else {
                filterdVisitTable.clear();
                for (VisitData visit : visitTable) {
                    if (visit.getCategory() == selected) {
                        filterdVisitTable.add(visit);
                    }
                }
                table.setItems(filterdVisitTable);
            }
        }
    }
}
