/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pharmacy;

import BAO.PhCallManage;
import DAO.CallItem;
import DAO.Drug;
import DAO.Prescription;
import commons.Helpers;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class PHCallScreenController implements Initializable {

    private Timeline timeline;

    private Stage stage;
/// TABLE ONE

    private final PhCallManage CALL = new PhCallManage();
    private final ObservableList<CallItem> items1 = FXCollections.observableArrayList();
    private ObservableList<CallItem> items2 = FXCollections.observableArrayList();
    private ObservableList<CallItem> items3 = FXCollections.observableArrayList();
    private ObservableList<CallItem> items4 = FXCollections.observableArrayList();

    @FXML
    private TableView<CallItem> tableOne;
    @FXML
    private TableColumn<CallItem, Integer> oneNo;
    @FXML
    private TableColumn<CallItem, String> oneName;
    @FXML
    private TableColumn<CallItem, String> oneId;
    /// TABLE TOW

    @FXML
    private TableView<CallItem> tableTow;
    @FXML
    private TableColumn<CallItem, Integer> towNo;
    @FXML
    private TableColumn<CallItem, String> towName;
    @FXML
    private TableColumn<CallItem, String> towId;
    /// TABLE THREE

    @FXML
    private TableView<CallItem> tableThree;
    @FXML
    private TableColumn<CallItem, Integer> threeNo;
    @FXML
    private TableColumn<CallItem, String> threeName;
    @FXML
    private TableColumn<CallItem, String> threeId;
    /// TABLE FOUR

    @FXML
    private TableView<CallItem> tableFour;
    @FXML
    private TableColumn<CallItem, Integer> fourNo;
    @FXML
    private TableColumn<CallItem, String> fourName;
    @FXML
    private TableColumn<CallItem, String> fourId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setTableUi();
            getData();
            setTimeLine();
        });
    }

    private void setTableUi() {
        String cemoColor = "-fx-background-color: #ffffcc ;";
        String other = "-fx-background-color: #ccffcc;";
        ////TBALE ONE
        oneNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        oneName.setCellValueFactory(new PropertyValueFactory<>("patName"));
        oneId.setCellValueFactory(new PropertyValueFactory<>("patFile"));
        oneNo.prefWidthProperty().bind(tableOne.widthProperty().multiply(0.2));
        oneName.prefWidthProperty().bind(tableOne.widthProperty().multiply(0.5));
        oneId.prefWidthProperty().bind(tableOne.widthProperty().multiply(0.3));

        ////TBALE TOW
        towNo.prefWidthProperty().bind(tableTow.widthProperty().multiply(0.2));
        towName.prefWidthProperty().bind(tableTow.widthProperty().multiply(0.5));
        towId.prefWidthProperty().bind(tableTow.widthProperty().multiply(0.3));
        towNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        towName.setCellValueFactory(new PropertyValueFactory<>("patName"));
        towId.setCellValueFactory(new PropertyValueFactory<>("patFile"));
        ////TBALE THREE
        threeNo.prefWidthProperty().bind(tableThree.widthProperty().multiply(0.2));
        threeName.prefWidthProperty().bind(tableThree.widthProperty().multiply(0.5));
        threeId.prefWidthProperty().bind(tableThree.widthProperty().multiply(0.3));
        threeNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        threeName.setCellValueFactory(new PropertyValueFactory<>("patName"));
        threeId.setCellValueFactory(new PropertyValueFactory<>("patFile"));
        ////TBALE FOUR
        fourNo.prefWidthProperty().bind(tableFour.widthProperty().multiply(0.2));
        fourName.prefWidthProperty().bind(tableFour.widthProperty().multiply(0.5));
        fourId.prefWidthProperty().bind(tableFour.widthProperty().multiply(0.3));
        fourNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        fourName.setCellValueFactory(new PropertyValueFactory<>("patName"));
        fourId.setCellValueFactory(new PropertyValueFactory<>("patFile"));
        tableOne.setItems(items1);
        tableOne.setRowFactory(tv -> new TableRow<CallItem>() {
            @Override
            protected void updateItem(CallItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else {
                    Prescription pres = new Prescription();
                    pres.setId(item.getId());
                    if (checkCemo(pres)) {
                        setStyle(cemoColor);
                    } else {
                        setStyle(other);
                    }
                }

            }

        });
        tableTow.setItems(items2);
        tableTow.setRowFactory(tv -> new TableRow<CallItem>() {
            @Override
            protected void updateItem(CallItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else {
                    Prescription pres = new Prescription();
                    pres.setId(item.getId());
                    if (checkCemo(pres)) {
                        setStyle(cemoColor);
                    } else {
                        setStyle(other);
                    }
                }

            }

        });
        tableThree.setItems(items3);
        tableThree.setRowFactory(tv -> new TableRow<CallItem>() {
            @Override
            protected void updateItem(CallItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else {
                    Prescription pres = new Prescription();
                    pres.setId(item.getId());
                    if (checkCemo(pres)) {
                        setStyle(cemoColor);
                    } else {
                        setStyle(other);
                    }
                }

            }

        });
        tableFour.setItems(items4);
        tableFour.setRowFactory(tv -> new TableRow<CallItem>() {
            @Override
            protected void updateItem(CallItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else {
                    Prescription pres = new Prescription();
                    pres.setId(item.getId());
                    if (checkCemo(pres)) {
                        setStyle(cemoColor);
                    } else {
                        setStyle(other);
                    }
                }

            }

        });

    }

    private void getData() {
        try {
            tableOne.getItems().clear();
            tableTow.getItems().clear();
            tableThree.getItems().clear();
            tableFour.getItems().clear();
            ObservableList<CallItem> temp = FXCollections.observableArrayList(CALL.allPatientCall());
            int index = 1;
            for (CallItem callItem : temp) {
                if (index <= 62) {
                    callItem.setNo(index++);
                    items1.add(callItem);
                } else if (index > 62 && index <= 124) {
                    callItem.setNo(index++);
                    items2.add(callItem);
                } else if (index > 124 && index <= 186) {
                    callItem.setNo(index++);
                    items3.add(callItem);
                } else if (index > 186) {
                    callItem.setNo(index++);
                    items4.add(callItem);
                }

            }
            tableOne.setItems(items1);
            tableTow.setItems(items2);
            tableThree.setItems(items3);
            tableFour.setItems(items4);

        } catch (SQLException ex) {
            Logger.getLogger(PHCallScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setTimeLine() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(3), new EventHandler() {
                    // KeyFrame event handler
                    @Override
                    public void handle(Event event) {
                        getData();
                    }
                }));
        timeline.playFromStart();
    }

    private boolean checkCemo(Prescription pres) {
        try {
            List<Integer> li = Helpers.PRES_MANAGE.getPresCategories(pres);
            if (li.contains(Drug.CHEMOTHERAPY)) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PHCallScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
