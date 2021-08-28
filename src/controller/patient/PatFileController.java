/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.patient;

import DAO.Drug;
import DAO.Fluid;
import DAO.LabOrder;
import DAO.LabVisit;
import DAO.Prescription;
import DAO.VisitData;
import DAO.patient.Patient;
import DAO.patient.PatientMovement;
import DAO.user.User;
import commons.Helpers;
import static commons.Helpers.LAB_MANAGE;
import static commons.Helpers.MOVEMENT;
import static commons.Helpers.PAT_MANAGE;
import static commons.Helpers.PRES_MANAGE;
import static commons.Helpers.VISIT_MANAGE;
import controller.actions.AddDrugController;
import controller.actions.AddLabTestController;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class PatFileController implements Initializable {

    private ObservableList<VisitData> visitDetalis = FXCollections.observableArrayList();
    private ObservableList<LabVisit> labDetalis = FXCollections.observableArrayList();
    private ObservableList<PatientMovement> allVisit;
    private PatientMovement current;
    private Patient currentPatient;
    private User currentUser;
    private Stage currentStage;
    private boolean disableUpdate = false;
    @FXML
    private Label nameLbl;
    @FXML
    private TextField nameTxt;
    @FXML
    private Label idLbl;
    @FXML
    private TextField idTxt;
    @FXML
    private Label nameLbl1111;
    @FXML
    private TextField dateOfBirthTxt;
    @FXML
    private Label phoneLbl112;
    @FXML
    private TextField regionTxt;
    @FXML
    private Label phoneLbl111;
    @FXML
    private TextField entryDateTxt;
    @FXML
    private Label phoneLbl111111;
    @FXML
    private TextField diaTxt;

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
    @FXML
    private TableColumn<VisitData, Integer> tNumber;
    @FXML
    private TableView<PatientMovement> visitTable;
    @FXML
    private TableColumn<PatientMovement, Integer> tNo;
    @FXML
    private TableColumn<PatientMovement, Date> tD;
    @FXML
    private TableColumn<PatientMovement, PatientMovement> tE;
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
    private TableColumn<LabVisit, LabVisit> colLabState;
    @FXML
    private TabPane tabPane;
    private boolean openLabTab;
    @FXML
    private Tab tabDrug;
    @FXML
    private Tab tabLab;
    private boolean hideDrugTab = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setTableUi();
            getData();
            setPatToUi();
            if (hideDrugTab) {
                tabPane.getTabs().remove(tabDrug);
            }
            if (openLabTab) {
                tabPane.getSelectionModel().select(tabLab);
            }
        });
    }

    @FXML
    private void getVisitData(MouseEvent event) {
        handelVisitData();
    }

    public PatientMovement getCurrentPatient() {
        return current;
    }

    public void setCurrentPatient(PatientMovement currentPatient) {
        this.current = currentPatient;
    }

    private void setPatToUi() {
        nameTxt.setText(currentPatient.getName());
        idTxt.setText(currentPatient.getPatId());
        int yearsBetween = Period.between(currentPatient.getBirth().toLocalDate(), LocalDate.now()).getYears();
        dateOfBirthTxt.setText(String.valueOf(yearsBetween));
        regionTxt.setText(currentPatient.getRegion().getName());
        entryDateTxt.setText(String.valueOf(currentPatient.getEntry()));
        diaTxt.setText(currentPatient.getDiagnosis().getName());
    }

    private void setTableUi() {
        //// VISIT TABLE
        tNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        tD.setCellValueFactory(new PropertyValueFactory<>("date"));
        tE.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        tE.setCellFactory(param -> new TableCell<PatientMovement, PatientMovement>() {
            Button btn = new Button("Edit");

            @Override
            protected void updateItem(PatientMovement item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setGraphic(null);
                    return;
                }
                if (item.getDate().equals(Date.valueOf(LocalDate.now())) && !disableUpdate) {
                    setGraphic(btn);
                    btn.getStyleClass().add("Edit");
                    btn.setPrefWidth(Double.MAX_VALUE);
                    btn.setPrefHeight(40);
                    btn.setOnAction(
                            event -> {
                                if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Drugs")) {
                                    editDrug(item);

                                } else {
                                    editLab(item);
                                }
                            });
                }
            }

        });
        visitTable.setItems(allVisit);
        //// DETEAILS TABLE 
        tNumber.setCellValueFactory(new PropertyValueFactory<>("no"));
        tDrug.setCellValueFactory(new PropertyValueFactory<>("drugName"));
        tDose.setCellValueFactory(new PropertyValueFactory<>("dose"));
        tFluid.setCellValueFactory(new PropertyValueFactory<>("fluidName"));
        tVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
        tNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colLabState.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        colLabState.setCellFactory(param -> new TableCell<LabVisit, LabVisit>() {
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
        descTable.setItems(visitDetalis);

        //// LAB TABLE 
        labNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        labResult.setCellValueFactory(new PropertyValueFactory<>("result"));
        labTest.setCellValueFactory(new PropertyValueFactory<>("testName"));
        labDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        labTable.setItems(labDetalis);

    }

    private void getData() {
        try {
            currentPatient = PAT_MANAGE.findByBarcode(current.getPatient().getId());
            allVisit = FXCollections.observableArrayList();
            ObservableList<PatientMovement> temp = null;
            if (currentUser == null) {
                temp = FXCollections.observableArrayList(MOVEMENT.getPatientMovement(current));
            } else {
                temp = FXCollections.observableArrayList(MOVEMENT.getPatientMovement(current, currentUser));
            }
            int no = 1;
            for (PatientMovement visit : temp) {
                if (MOVEMENT.checkPatDrug(visit) || MOVEMENT.checkPatLab(visit)) {
                    visit.setNo(no++);
                    visit.setPatient(currentPatient);
                    allVisit.add(visit);

                }
            }
            visitTable.setItems(allVisit);
            if (visitTable.getItems().size() > 0) {
                visitTable.getSelectionModel().selectLast();
                handelVisitData();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private void editDrug(PatientMovement item) {
        try {
            ArrayList<Prescription> allPres = PRES_MANAGE.patientVisits(item, item.getDate());
            if (allPres.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "There Is no Drugs Today", ButtonType.OK);
                alert.show();
            } else if (allPres.get(0).getChecked() == VisitData.CHECED) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sorry, you can't change medication \n"
                        + "Medicines are being prepared for this patient ", ButtonType.OK);
                alert.show();
            } else {
                Parent root;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/actions/AddDrug.fxml"));
                root = loader.load();
                AddDrugController manage = loader.getController();
                manage.setCurrentPat(item);
                manage.setCurrentUser(currentUser);
                manage.setOldVisit();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Edit Drug ");
                stage.setScene(scene);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.showAndWait();
                getData();
            }
        } catch (IOException e) {
        } catch (SQLException ex) {
            Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void editLab(PatientMovement item) {
        if (!labTable.getItems().isEmpty()) {
            try {
                LabOrder labOrder = LAB_MANAGE.getTodayLabOrder(item);
                Parent root;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/actions/AddLabTest.fxml"));
                root = loader.load();
                AddLabTestController manage = loader.getController();
                manage.setCurrentPat(item);
                manage.setCurrentUser(currentUser);
                manage.setCurrentOrder(labOrder);
                manage.setUpdate(true);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Edit Lab ");
                stage.setScene(scene);
//            stage.initOwner(stage);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.showAndWait();
                getData();
            } catch (SQLException | IOException ex) {
                Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Stage getStage() {
        return currentStage;
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    private void handelVisitData() {
        try {
            PatientMovement item = visitTable.getSelectionModel().getSelectedItem();
            ArrayList<Prescription> allPres = PRES_MANAGE.patientVisits(item, item.getDate());
            if (allPres.isEmpty()) {
                visitDetalis.clear();
                descTable.getItems().clear();
            } else {
                visitDetalis = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(allPres.get(0)));
                descTable.setItems(visitDetalis);
            }
            LabOrder alllab = LAB_MANAGE.getTodayLabOrder(item);
            if (alllab != null) {
                labDetalis = FXCollections.observableArrayList(LAB_MANAGE.getOrderDetails(alllab, LabOrder.FINISH));
                labTable.setItems(labDetalis);
            } else {
                labDetalis.clear();
                labTable.getItems().clear();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disableUpdate() {
        disableUpdate = true;
    }

    @FXML
    private void openResultFile(MouseEvent event) {
        LabVisit visit = labTable.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2) {
            if (visit.getResultFile() == null) {
                return;
            }
            try {
                File f = new File(visit.getResultFile());
                Desktop.getDesktop().open(f);
                visit.setSeen(1);
                Helpers.LAB_MANAGE.updateLabItemOpenState(visit);
                PatientMovement item = visitTable.getSelectionModel().getSelectedItem();
                LabOrder alllab = LAB_MANAGE.getTodayLabOrder(item);
                labDetalis = FXCollections.observableArrayList(LAB_MANAGE.getOrderDetails(alllab, LabOrder.FINISH));
                labTable.setItems(labDetalis);
            } catch (IOException ex) {
                Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(PatFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setOpenLabTab(boolean openLabTab) {
        this.openLabTab = openLabTab;
    }

    public void hideDrugTab(boolean f) {
        hideDrugTab = f;
    }
}
