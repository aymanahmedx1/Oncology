/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.doctor;

import BAO.CallManage;
import DAO.CallItem;
import DAO.LabOrder;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
public class LabScreenController implements Initializable {

    private Stage stage;
    private Timeline timeline;
    private ObservableList<CallItem> result = FXCollections.observableArrayList();
    private ObservableList<CallItem> sample = FXCollections.observableArrayList();
    private final CallManage CALL = new CallManage();
    @FXML
    private TableView<CallItem> resultTable;
    @FXML
    private TableColumn<CallItem, String> colNameResult;
    @FXML
    private TableColumn<CallItem, String> colIDResult;
    @FXML
    private TableColumn<CallItem, String> colNameSample;
    @FXML
    private TableColumn<CallItem, String> colIDSample;
    @FXML
    private TableView<CallItem> sampleTable;
    @FXML
    private TableColumn<CallItem, CallItem> colNoResult;
    @FXML
    private TableColumn<CallItem, CallItem> colNoSample;

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

    private void getData() {
        result.clear();
        sample.clear();
        ArrayList<CallItem> temp = CALL.getLabCall();
        int index = 1 ;
        int index2 = 1 ;
        for (CallItem callItem : temp) {
            if (callItem.getState() == LabOrder.CALL_RESULT) {
                callItem.setNo(index++);
                result.add(callItem);
            } else if (callItem.getState() == LabOrder.CALL_SAMPLE) {
                callItem.setNo(index2++);
                sample.add(callItem);
            }
        }
        resultTable.setItems(result);
        sampleTable.setItems(sample);
    }

    private void setTableUi() {
        colNoSample.setCellValueFactory(new PropertyValueFactory<>("no"));
        colNoResult.setCellValueFactory(new PropertyValueFactory<>("no"));
        colNameResult.setCellValueFactory(new PropertyValueFactory<>("patName"));
        colIDResult.setCellValueFactory(new PropertyValueFactory<>("patFile"));
        colNameSample.setCellValueFactory(new PropertyValueFactory<>("patName"));
        colIDSample.setCellValueFactory(new PropertyValueFactory<>("patFile"));
        resultTable.setItems(result);
        sampleTable.setItems(sample);
    }

    private void setTimeLine() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(5), new EventHandler() {
                    // KeyFrame event handler
                    @Override
                    public void handle(Event event) {
                        getData();
                    }
                }));
        timeline.playFromStart();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
