/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.user;

import BAO.DatabaseChanges;
import BAO.DeleteData;
import DAO.user.User;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class ClearProgramDataController implements Initializable {
    private Stage stage ;
    private User current ;
    private final DeleteData delete = new DeleteData();
    private final Alert waitAlert =new Alert(Alert.AlertType.INFORMATION, "Please Wait ", ButtonType.OK);
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void delDrug(ActionEvent event) {
        Optional<ButtonType> res = showMessage("drug");
        if (res.isPresent() && res.get().equals(ButtonType.YES)) {
            try {
                waitAlert.show();
                delete.deleteDrug();
                waitAlert.hide();
            } catch (SQLException ex) {
                resultMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void delLabTest(ActionEvent event) {
        Optional<ButtonType> res = showMessage("Lab Test");
        if (res.isPresent() && res.get().equals(ButtonType.YES)) {
            try {
                delete.deleteLabTest();
            } catch (SQLException ex) {
                resultMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void delRegion(ActionEvent event) {
        Optional<ButtonType> res = showMessage("Region");
        if (res.isPresent() && res.get().equals(ButtonType.YES)) {
            try {
                delete.deleteRegion();
            } catch (SQLException ex) {
                resultMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void delPres(ActionEvent event) {
        Optional<ButtonType> res = showMessage("Prescriptions");
        if (res.isPresent() && res.get().equals(ButtonType.YES)) {
            try {
                delete.deletPrescription();
            } catch (SQLException ex) {
                resultMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void delLab(ActionEvent event) {
        Optional<ButtonType> res = showMessage("Lab Orders");
        if (res.isPresent() && res.get().equals(ButtonType.YES)) {
            try {
                delete.deleteLabOrder();
            } catch (SQLException ex) {
                resultMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void delMove(ActionEvent event) {
        Optional<ButtonType> res = showMessage("Patient Visits");
        if (res.isPresent() && res.get().equals(ButtonType.YES)) {
            try {
                delete.deleteMovement();
            } catch (SQLException ex) {
                resultMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void delPat(ActionEvent event) {
        Optional<ButtonType> res = showMessage("Patient ");
        if (res.isPresent() && res.get().equals(ButtonType.YES)) {
            try {
                delete.deletePatient();
            } catch (SQLException ex) {
                resultMessage(ex.getMessage());
            }
        }
    }

    @FXML
    private void delAll(ActionEvent event) {
        Optional<ButtonType> res = showMessage("All Program Data !! ");
        if (res.isPresent() && res.get().equals(ButtonType.YES)) {
            delLab(event);
            delPres(event);
            delMove(event);
            delRegion(event);
            delLabTest(event);
            delDrug(event);
            delPat(event);
        }
    }

    private Optional<ButtonType> showMessage(String Message) {
        Alert aler = new Alert(Alert.AlertType.ERROR, "Are You Sure \n you will delete \t" + Message + "\n FOR EVER", ButtonType.YES, ButtonType.NO);
               return aler.showAndWait();
    }

    private void resultMessage(String Message) {
        Alert aler = new Alert(Alert.AlertType.ERROR, Message, ButtonType.NO);
        aler.showAndWait();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public User getCurrent() {
        return current;
    }

    public void setCurrent(User current) {
        this.current = current;
    }

}
