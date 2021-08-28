/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.doctor;

import BAO.CallManage;
import DAO.CallItem;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class CallScreenController implements Initializable {

    private Timeline timeline;
    private final CallManage CALL = new CallManage();
    private ObservableList<CallItem> doctorCallItem = FXCollections.observableArrayList();
    private ObservableList<CallItem> CemoCallItem = FXCollections.observableArrayList();
    private HashMap<Integer, String> stateMap = new HashMap<>();

    private Stage stage;

    @FXML
    private TableColumn<CallItem, Integer> colNo;
    @FXML
    private TableColumn<CallItem, String> colName;
    @FXML
    private TableColumn<CallItem, String> colId;
    @FXML
    private TableColumn<CallItem, String> ColDoctor;
    @FXML
    private TableColumn<CallItem, String> colState;
    @FXML
    private TableView<CallItem> table;
    @FXML
    private TableView<CallItem> cemoTable;
    @FXML
    private TableColumn<CallItem, Integer> cemoNoCol;
    @FXML
    private TableColumn<CallItem, String> cemoNameCol;
    @FXML
    private TableColumn<CallItem, String> cemoIdCol;
    @FXML
    private TableColumn<CallItem, String> cemoStatusCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setTableUi();
            stateMap.put(CallItem.GO_To_DOCTOR, "الرجاء الذهاب الى الطبيب ");
            stateMap.put(CallItem.GO_To_CEMO, "الرجاء الحضور الى غرفة الختم الكيميائي");
            getData();
            setTimeLine();

        });
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

    private void getData() {
        try {
            doctorCallItem.clear();
            CemoCallItem.clear();
            ArrayList<CallItem> temp = CALL.allDoctorsCall();
            int docNo = 1;
            int cemoNo = 1;
            for (CallItem call : temp) {
                call.setStateName(stateMap.get(call.getState()));
                if (call.getState() == CallItem.GO_To_DOCTOR) {
                    call.setNo(docNo++);
                    doctorCallItem.add(call);
                } else {
                    call.setNo(cemoNo++);
                    CemoCallItem.add(call);
                }
            }
            table.setItems(doctorCallItem);
            cemoTable.setItems(CemoCallItem);
        } catch (SQLException ex) {
            Logger.getLogger(CallScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTableUi() {
        colNo.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        colName.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        colId.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        ColDoctor.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        colState.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colName.setCellValueFactory(new PropertyValueFactory<>("patName"));
        colId.setCellValueFactory(new PropertyValueFactory<>("patFile"));
        ColDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colState.setCellValueFactory(new PropertyValueFactory<>("stateName"));
        table.setItems(doctorCallItem);

        cemoNoCol.prefWidthProperty().bind(cemoTable.widthProperty().multiply(0.1));
        cemoNameCol.prefWidthProperty().bind(cemoTable.widthProperty().multiply(0.3));
        cemoIdCol.prefWidthProperty().bind(cemoTable.widthProperty().multiply(0.2));
        cemoStatusCol.prefWidthProperty().bind(cemoTable.widthProperty().multiply(0.4));
        cemoNoCol.setCellValueFactory(new PropertyValueFactory<>("no"));
        cemoNameCol.setCellValueFactory(new PropertyValueFactory<>("patName"));
        cemoIdCol.setCellValueFactory(new PropertyValueFactory<>("patFile"));
        cemoStatusCol.setCellValueFactory(new PropertyValueFactory<>("stateName"));
        cemoTable.setItems(doctorCallItem);
    }

}
