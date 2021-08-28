/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.doctor;

import BAO.LabGroupBAO;
import BAO.LabOrderManage;
import DAO.LabGroup;
import DAO.LabOrder;
import DAO.LabVisit;
import DAO.patient.PatientMovement;
import com.jfoenix.controls.JFXButton;
import commons.Helpers;
import static commons.Helpers.LAB_MANAGE;
import commons.NewExcelExport;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class LabFinishedController implements Initializable {

    private Stage stage;
    private ObservableList<LabVisit> labDetalis = FXCollections.observableArrayList();
    private ObservableList<LabVisit> FilterlabDetalis = FXCollections.observableArrayList();
    private ObservableList<LabOrder> allPrescriptions;
    private ObservableList<LabOrder> FilterdPres = FXCollections.observableArrayList();
    private ArrayList<LabGroup> allGroups;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private JFXButton search;
    @FXML
    private TextField txtNameFilter;
    @FXML
    private TextField txtDoctorFilter;
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
    private TableView<LabOrder> table;
    @FXML
    private TableColumn<LabOrder, Integer> colNo;
    @FXML
    private TableColumn<LabOrder, String> colPatName;
    @FXML
    private TableColumn<LabOrder, String> colPatId;
    @FXML
    private TableColumn<LabOrder, String> colPatDoctor;
    @FXML
    private TableColumn<LabOrder, Date> colPatDAte;
    @FXML
    private TableColumn<LabVisit, LabVisit> colFile;
    @FXML
    private ComboBox<String> comboLabGroup;
    private final String SELECT = "All Tests";
    private LabGroup group = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setDateConvert();
            setTableUi();
            getData();
            tableOnSelectionChange();
            textFilter();
            setCombo();
            table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        });
    }

    private void getData(ActionEvent event) {
        getData();
    }

    @FXML
    private void openPatFile(ActionEvent event) {
        if (table.getSelectionModel().isEmpty()) {
            return;
        }
        try {
            PatientMovement p = Helpers.MOVEMENT.getPatientFile(table.getSelectionModel().getSelectedItem().getPatId());
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

    @FXML
    private void prinrReport(ActionEvent event) {
        try {
            NewExcelExport ex = new NewExcelExport();
            LocalDate from = fromDate.getValue();
            LocalDate to = toDate.getValue();
            String selected = comboLabGroup.getSelectionModel().getSelectedItem();
            String catSelected = (selected.equals(SELECT) ? "All " : selected);
            int catSelectedId = (group == null) ? 0 : group.getId();
            if (table.getSelectionModel().isEmpty() && table.getItems().size() > 0) {
                ex.LabReportToExcel(stage, new ArrayList<>(table.getItems()), from, to, catSelected, catSelectedId);
            } else if (!table.getSelectionModel().isEmpty() &&table.getItems().size() > 0) {
                ArrayList<LabOrder> orders = new ArrayList<>(table.getSelectionModel().getSelectedItems());
                ex.LabReportToExcel(stage, orders, from, to, catSelected, catSelectedId);
            }

        } catch (SQLException ex1) {
            Logger.getLogger(LabFinishedController.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    @FXML
    private void searchBtn(ActionEvent event) {
        getData();
    }

    private void setDateConvert() {
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

    private void getData() {
        try {
            Date from = Date.valueOf(fromDate.getValue());
            Date to = Date.valueOf(toDate.getValue());
            allPrescriptions = FXCollections.observableArrayList(new LabOrderManage().getLabOrders(PatientMovement.FINISH, from, to));
            table.setItems(allPrescriptions);
            allGroups = new LabGroupBAO().AllLabGroup();
            labTable.getItems().clear();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setTableUi() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colPatName.setCellValueFactory(new PropertyValueFactory<>("patName"));
        colPatId.setCellValueFactory(new PropertyValueFactory<>("patFileId"));
        colPatDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colPatDAte.setCellValueFactory(new PropertyValueFactory<>("date"));
        table.setItems(allPrescriptions);
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

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void tableOnSelectionChange() {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            getVisitDataHandel();
        });
    }

    private void getVisitDataHandel() {
        if (!table.getSelectionModel().isEmpty()) {
            try {
                LabOrder selected = table.getSelectionModel().getSelectedItem();
                labDetalis = FXCollections.observableArrayList(LAB_MANAGE.getOrderDetails(selected));
                handelLabGroupChange();
            } catch (SQLException ex) {
                Logger.getLogger(LabFinishedController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void textFilter() {
        txtNameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            FilterdPres.clear();
            for (LabOrder pres : allPrescriptions) {
                if (pres.getPatName().startsWith(newValue)) {
                    FilterdPres.add(pres);
                }
            }
            table.setItems(FilterdPres);
        });
        txtDoctorFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            FilterdPres.clear();
            for (LabOrder pres : allPrescriptions) {
                if (pres.getDoctorName().startsWith(newValue)) {
                    FilterdPres.add(pres);
                }
            }
            table.setItems(FilterdPres);
        });
    }

    private void openFile(PatientMovement item) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patient/PatFile.fxml"));
            root = loader.load();
            PatFileController manage = loader.getController();
            manage.setCurrentPatient(item);
            manage.setStage(stage);
            manage.disableUpdate();
            manage.hideDrugTab(true);
            manage.setOpenLabTab(true);
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
    private void openResultFile(MouseEvent event) {
        if (event.getClickCount() == 2) {
            LabVisit visit = labTable.getSelectionModel().getSelectedItem();
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

    @FXML
    private void labGroupChange(ActionEvent event) {
        handelLabGroupChange();
    }

    private void setCombo() {
        comboLabGroup.getItems().add(SELECT);
        for (LabGroup group : allGroups) {
            comboLabGroup.getItems().add(group.getName());
        }
        comboLabGroup.getSelectionModel().selectFirst();

    }

    private void setLabFilter(LabGroup group) {
        FilterlabDetalis.clear();
        for (LabVisit labDetali : labDetalis) {
            if (labDetali.getGroupId() == group.getId()) {
                FilterlabDetalis.add(labDetali);
            }
        }
        labTable.setItems(FilterlabDetalis);
    }

    private void handelLabGroupChange() {
        if (comboLabGroup.getSelectionModel().getSelectedItem().equals(SELECT)) {
            labTable.setItems(labDetalis);
        } else {

            for (LabGroup gr : allGroups) {
                if (gr.getName().equals(comboLabGroup.getSelectionModel().getSelectedItem())) {
                    group = gr;
                    break;
                }
            }
            if (group != null) {
                setLabFilter(group);
            }
        }
    }

    @FXML
    private void refresh(ActionEvent event) {
        getData();
    }

}
