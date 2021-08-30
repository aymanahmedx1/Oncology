/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.patient;

import BAO.PrescriptionManagement;
import BAO.VisitManagement;
import DAO.CallItem;
import DAO.Prescription;
import DAO.VisitData;
import DAO.patient.Patient;
import com.jfoenix.controls.JFXButton;
import commons.Helpers;
import static commons.Helpers.PAT_MANAGE;
import static commons.Helpers.VISIT_MANAGE;
import controller.pharmacy.PrepareDrugController;
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
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class BlackListController implements Initializable {

    private final PrescriptionManagement PRES = new PrescriptionManagement();
    private Stage stage;
    private ObservableList<Prescription> allPrescriptions;
    private ObservableList<VisitData> visitDetalis = FXCollections.observableArrayList();
    private ContextMenu pop = new ContextMenu();
    Pattern pattern;
    private Patient currentPatient;
    private HashMap<String, Patient> patentMap = new HashMap<>();
    private ArrayList<Patient> allPatient;
    private final VisitManagement VISIT = new VisitManagement();

    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private JFXButton search;
    @FXML
    private TextField txtBarcode;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtAge;
    @FXML
    private TextField txtRegion;
    @FXML
    private TextField txtDia;
    @FXML
    private TextField txtDoctor;
    @FXML
    private TextField txtEntry;
    @FXML
    private TableView<Prescription> presTable;
    @FXML
    private TableColumn<Prescription, Integer> noCol;
    @FXML
    private TableColumn<Prescription, String> patNameCol;
    @FXML
    private TableColumn<Prescription, Date> doctorCol;
    @FXML
    private TextField txtNameFilter;
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
    private TableColumn<VisitData, VisitData> colNoDrug;
    @FXML
    private TableColumn<VisitData, VisitData> tCeck;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setDateConverter();
            fromDate.setValue(LocalDate.now());
            toDate.setValue(LocalDate.now());
            setTableUI();
            autotAll();
            getData();
            tableSelectionChange();
        });
    }

    @FXML
    private void Add(ActionEvent event) {
        try {
            int lastPresNo = PRES.getPatientLastPrescriptionID(currentPatient);
            if (lastPresNo != -1) {
                PRES.updatePrescriptionState(lastPresNo, Patient.BLACK_LIST);
            }
            getData();
        } catch (SQLException ex) {
            Logger.getLogger(BlackListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void searchBtn(ActionEvent event) {
        getData();
    }

    @FXML
    private void getVisitData(MouseEvent event) {
    }

    private void setTableUI() {
        noCol.prefWidthProperty().bind(presTable.widthProperty().multiply(0.1));
        patNameCol.prefWidthProperty().bind(presTable.widthProperty().multiply(0.6));
        doctorCol.prefWidthProperty().bind(presTable.widthProperty().multiply(0.3));
        noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
        patNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        doctorCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        presTable.setItems(allPrescriptions);
        /////Details Table
        colNoDrug.prefWidthProperty().bind(descTable.widthProperty().multiply(0.1));
        tDrug.prefWidthProperty().bind(descTable.widthProperty().multiply(0.2));
        tDose.prefWidthProperty().bind(descTable.widthProperty().multiply(0.1));
        tFluid.prefWidthProperty().bind(descTable.widthProperty().multiply(0.1));
        tVolume.prefWidthProperty().bind(descTable.widthProperty().multiply(0.1));
        tNote.prefWidthProperty().bind(descTable.widthProperty().multiply(0.3));
        tCeck.prefWidthProperty().bind(descTable.widthProperty().multiply(0.1));
        colNoDrug.setCellValueFactory(new PropertyValueFactory<>("no"));
        tDrug.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        tDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        tFluid.setCellValueFactory(new PropertyValueFactory<>("fluidName"));
        tVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        tNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        tCeck.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        tCeck.setCellFactory(param -> new TableCell<VisitData, VisitData>() {
            @Override
            protected void updateItem(VisitData item, boolean empty) {
                super.updateItem(item, empty);
                CheckBox check = new CheckBox();
                if (item == null) {
                    setGraphic(null);
                } else {
                    if (item.getBlack() == Patient.BLACK_LIST) {
                        check.setSelected(true);
                        setGraphic(check);
                    } else {
                        check.setSelected(false);
                        setGraphic(check);
                    }
                }
                check.setOnAction((event) -> {
                    try {
                        if (check.isSelected()) {
                            VISIT.updateVisitItemBlack(item, Patient.BLACK_LIST);
                        } else {
                            VISIT.updateVisitItemBlack(item, 0);

                        }
                        getData();
                    } catch (SQLException ex) {
                        Logger.getLogger(BlackListController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

            }
        ;

        }

            );
            descTable.setItems(visitDetalis);
        descTable.setRowFactory(tv -> new TableRow<VisitData>() {
            @Override
            protected void updateItem(VisitData item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else {
                    if (item.getBlack() == Patient.BLACK_LIST) {
                        setStyle(" -fx-background-color:red ;");
                    } else {
                        setStyle("");
                    }
                }

            }

        });
    }

    private void autotAll() {
        pop.getStyleClass().add("menu");
        txtId.textProperty().addListener((observable, oldValue, newValue) -> {
            idAuto();
        });

        txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            nameAuto();
        });
        txtBarcode.textProperty().addListener((observable, oldValue, newValue) -> {
            bracodeAuto();
        });
    }

    private void setPatToUi() {
//        try {
        txtBarcode.setText(String.valueOf(currentPatient.getId()));
        txtName.setText(currentPatient.getName());
        txtId.setText(currentPatient.getPatId());
        txtPhone.setText(currentPatient.getPhone());
        txtAge.setText(String.valueOf(Helpers.calculateAge(currentPatient.getBirth().toLocalDate())));
        txtEntry.setText(currentPatient.getEntry().toString());
        txtDoctor.setText(currentPatient.getDoctor().getName());
        txtDia.setText(currentPatient.getDiagnosis().getName());
        txtRegion.setText(currentPatient.getRegion().getName());

        if (currentPatient.getBlackList() == Patient.BLACK_LIST) {

        } else {
        }
//        } catch (SQLException ex) {
//            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
//        }
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

    private void bracodeAuto() {
        if (txtBarcode.getText().equals("")) {
            if (pop.isShowing()) {
                pop.hide();
            }
            return;
        }
        getPatientByID();
        pop.getItems().clear();
        for (Patient r : allPatient) {
            patentMap.put(String.valueOf(r.getId()), r);
            MenuItem m = new MenuItem(String.valueOf(r.getId()));
            m.setStyle("-fx-pref-width:" + txtBarcode.getWidth() + ";");
            m.setOnAction((event) -> {
                pop.hide();
                currentPatient = patentMap.get(m.getText());
                setPatToUi();
            });
            pop.getItems().add(m);

        }
        txtBarcode.setContextMenu(pop);

        pop.show(txtBarcode, Side.BOTTOM, 0, 0);
    }

    private void idAuto() {
        if (txtId.getText().equals("")) {
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
            m.setStyle("-fx-pref-width:" + txtId.getWidth() + ";");
            m.setOnAction((event) -> {
                pop.hide();
                currentPatient = patentMap.get(m.getText());
                setPatToUi();
            });
            pop.getItems().add(m);

        }
        txtId.setContextMenu(pop);

        pop.show(txtId, Side.BOTTOM, 0, 0);
    }

    private void getPatient() {
        try {
            allPatient = PAT_MANAGE.findPatients(txtName.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getPatientByFileID() {
        try {
            allPatient = PAT_MANAGE.findPatFileId(txtId.getText());
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

    @FXML
    private void clear(ActionEvent event) {
        clear();
    }

    private void clear() {
        txtAge.clear();
        txtBarcode.clear();
        txtDia.clear();
        txtDoctor.clear();
        txtEntry.clear();
        txtId.clear();
        txtName.clear();
        txtPhone.clear();
        txtPhone.clear();
        txtRegion.clear();
        getData();
    }

    private void getData() {
        try {
            Date from = Date.valueOf(fromDate.getValue());
            Date to = Date.valueOf(toDate.getValue());
            allPrescriptions = FXCollections.observableArrayList(new PrescriptionManagement().AllPrescription(from, to));
            presTable.setItems(allPrescriptions);
            descTable.getItems().clear();
        } catch (SQLException ex) {
            Logger.getLogger(BlackListController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrepareDrugController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getPatientByID() {
        try {
            allPatient = PAT_MANAGE.findPatID(txtBarcode.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
