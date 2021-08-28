/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.settings;

import BAO.RegionManage;
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
public class RegionController implements Initializable {

    private ObservableList<Region> allRegion = FXCollections.observableArrayList();
    private ObservableList<Region> filterdRegion = FXCollections.observableArrayList();
    private final RegionManage REG = new RegionManage();
    private Stage stage;
    @FXML
    private TextField txtRegion;
    @FXML
    private TableView<Region> table;
    @FXML
    private TableColumn<Region, String> colRegName;
    @FXML
    private TextField txtRegFilter;
    private Region currentRegion;

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
    private void updateItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            currentRegion = table.getSelectionModel().getSelectedItem();
            txtRegion.setText(currentRegion.getName());
        }
    }

    private void setTableUi() {
        colRegName.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.setItems(allRegion);
    }

    private void getData() {
        try {
            allRegion = FXCollections.observableArrayList(REG.allRegion());
            txtRegion.clear();
            table.setItems(allRegion);
        } catch (SQLException ex) {
            Logger.getLogger(RegionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void save(ActionEvent event) {
        try {
            if (!txtRegion.getText().isEmpty()) {
                if (currentRegion != null) {
                    currentRegion.setName(txtRegion.getText());
                    REG.update(currentRegion);
                } else {
                    if (checkDuplicate()) {
                        Region r = new Region();
                        r.setName(txtRegion.getText());
                        REG.add(r);
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
        currentRegion = null;
        txtRegion.clear();
    }

    private boolean checkDuplicate() {
        for (Region reg : allRegion) {
            if (txtRegion.getText().equals(reg.getName())) {
                return false;
            }

        }
        return true;
    }

    private void txtFilter() {
        txtRegFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterdRegion.clear();
            for (Region region : allRegion) {
                if (region.getName().toLowerCase().startsWith(newValue.toLowerCase())) {
                    filterdRegion.add(region);
                }
            }
            table.setItems(filterdRegion);
        });
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
