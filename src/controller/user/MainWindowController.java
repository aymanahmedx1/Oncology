/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.user;

import BAO.DatabaseChanges;
import BAO.user.Authmanage;
import DAO.user.Permisions;
import DAO.user.User;
import controller.doctor.CallScreenController;
import controller.doctor.DoctorController;
import controller.doctor.LabController;
import controller.doctor.LabScreenController;
import controller.drug.DrugDataController;
import controller.drug.LabDataController;
import controller.patient.BlackListController;
import controller.patient.PatientCostController;
import controller.patient.PatientManagementController;
import controller.patient.SearchForPatController;
import controller.pharmacy.CemoCheckController;
import controller.pharmacy.PHCallScreenController;
import controller.pharmacy.PharmacyController;
import controller.pharmacy.PrepareDrugController;
import controller.reception.DeathNoteController;
import controller.reception.ReceptionController;
import controller.report.DoseController;
import controller.report.SearchForDrugController;
import controller.settings.DiagnosisController;
import controller.settings.RegionController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class MainWindowController implements Initializable {

    private final Authmanage AUTH = new Authmanage();

    private User currentUser;
    @FXML
    private Label userlbl;
    @FXML
    private Label username;
    @FXML
    private Button logOutBtn;
    private Accordion accordion;
    private TitledPane patientTab;
    @FXML
    private Button databaseChanges;
    @FXML
    private Tab tabReception;
    @FXML
    private Button reciption;
    @FXML
    private Tab tabDoctor;
    @FXML
    private Button doctor;
    @FXML
    private Button deathNote;
    @FXML
    private Tab tabChemo;
    @FXML
    private Button chemoCheck;
    @FXML
    private Tab tabPH;
    @FXML
    private Button clinicalPH;
    @FXML
    private Button prepareDrug;
    @FXML
    private Button blackList;
    @FXML
    private Button drugDose;
    @FXML
    private Button patManage;
    @FXML
    private Button patCost;
    @FXML
    private Tab tabLab;
    @FXML
    private Button lab;
    @FXML
    private Tab tabScreen;
    @FXML
    private Button doctorScreen;
    @FXML
    private Button labScreen;
    @FXML
    private Button PHScreen;
    @FXML
    private Tab tabReport;
    @FXML
    private Button searchForPat;
    @FXML
    private Button serachForDrug;
    @FXML
    private Tab tabSettint;
    @FXML
    private Button userManage;
    @FXML
    private Button drugManage;
    @FXML
    private Button labTest;
    @FXML
    private Button diaManage;
    @FXML
    private Button rigManage;
    @FXML
    private Button pref;
    @FXML
    private Button deleteData;
    private Permisions per;
    @FXML
    private TabPane mainTabPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setUserPermission();
        });
    }

    @FXML
    private void closeProgram(ActionEvent event) {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/view/LogIn.fxml"));
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.setTitle("Log In");
//            stage.setResizable(false);
//            stage.show();
//            ((Node) (event.getSource())).getScene().getWindow().hide();
//        } catch (IOException ex) {
//            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @FXML
    private void addPatient(ActionEvent event) {

        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reception/reception.fxml"));
            root = loader.load();
            ReceptionController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(true);
            stage.setTitle(" Reception ");
//            stage.getIcons().add(new Image("/com/zsalse/icons/LogInIcon.jpg"));
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        username.setText(currentUser.getName());
    }

    public void mouseClicked(MouseEvent event) {

    }

    @FXML
    private void doctor(ActionEvent event) {
        if (currentUser.getDept() != User.DOCTOR) {
            return;
        }
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/doctor/Doctor.fxml"));
            root = loader.load();
            DoctorController manage = loader.getController();
            manage.setCurrent(currentUser);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            manage.setStage(stage);
//            stage.setResizable(false);
            stage.setTitle(" Doctor ");
//            stage.getIcons().add(new Image("/com/zsalse/icons/LogInIcon.jpg"));
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void drug(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/drug/DrugData.fxml"));
            root = loader.load();
            DrugDataController manage = loader.getController();
            manage.setStage(currentStage);
            manage.setCurrent(currentUser);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
//            stage.setResizable(false);
            stage.setTitle(" Drug Management ");
//            stage.getIcons().add(new Image("/com/zsalse/icons/LogInIcon.jpg"));
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void lab(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/doctor/Lab.fxml"));
            root = loader.load();
            LabController manage = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
//            stage.setResizable(false);
            stage.setTitle(" Lab ");
//            stage.getIcons().add(new Image("/com/zsalse/icons/LogInIcon.jpg"));
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void labTests(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/drug/LabData.fxml"));
            root = loader.load();
            LabDataController manage = loader.getController();
            manage.setStage(currentStage);
            manage.setCurrent(currentUser);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
//            stage.setResizable(false);
            stage.setTitle(" Lab Management ");
//            stage.getIcons().add(new Image("/com/zsalse/icons/LogInIcon.jpg"));
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void prepareDrug(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Pharmacy/PrepareDrug.fxml"));
            root = loader.load();
            PrepareDrugController manage = loader.getController();
            manage.setStage(currentStage);
            manage.setCurrent(currentUser);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
//            stage.setResizable(false);
            stage.setTitle(" Prepare Drug ");
//            stage.getIcons().add(new Image("/com/zsalse/icons/LogInIcon.jpg"));
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void pharmacy(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Pharmacy/Pharmacy.fxml"));
            root = loader.load();
            PharmacyController manage = loader.getController();
            manage.setStage(currentStage);
            manage.setCurrent(currentUser);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Clinical Pharmacy ");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void peformDatabaseChanges(ActionEvent event) {
        try {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Please wait !! ");
            alert2.show();
            DatabaseChanges db = new DatabaseChanges();
            db.databaseChanges();
            alert2.hide();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Database set Success !", ButtonType.OK);
            alert.show();
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error " + ex.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    private void drugDose(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/report/Dose.fxml"));
            root = loader.load();
            DoseController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Drug Dose ");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void searchForDrug(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/report/SearchForDrug.fxml"));
            root = loader.load();
            SearchForDrugController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Search For Drug");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void searchForPat(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patient/SearchForPat.fxml"));
            root = loader.load();
            SearchForPatController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Search For Patient");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void patCost(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patient/PatientCost.fxml"));
            root = loader.load();
            PatientCostController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Patient Cost");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void ppatManage(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patient/PatientManagement.fxml"));
            root = loader.load();
            PatientManagementController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Patient Manage");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void doctorScreen(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/doctor/CallScreen.fxml"));
            root = loader.load();
            CallScreenController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(" Doctor and chemo check screen");
//               stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void drugScreen(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Pharmacy/PHCallScreen.fxml"));
            root = loader.load();
            PHCallScreenController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(" Doctor Screen");
//               stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void cemoCheck(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Pharmacy/CemoCheck.fxml"));
            root = loader.load();
            CemoCheckController manage = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(" Chemo Check  ");
            stage.setScene(scene);
            manage.setStage(stage);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void labScreen(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/doctor/LabScreen.fxml"));
            root = loader.load();
            LabScreenController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(" Doctor Screen");
//               stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void regionManage(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/settings/Region.fxml"));
            root = loader.load();
            RegionController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(" Region Management");
//               stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void diagnosisManage(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/settings/Diagnosis.fxml"));
            root = loader.load();
            DiagnosisController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(" Region Management");
//               stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void death(ActionEvent event) {

        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reception/DeathNote.fxml"));
            root = loader.load();
            DeathNoteController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(true);
            stage.setTitle(" Death Note ");
//            stage.getIcons().add(new Image("/com/zsalse/icons/LogInIcon.jpg"));
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void blackList(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/patient/BlackList.fxml"));
            root = loader.load();
            BlackListController manage = loader.getController();
            manage.setStage(currentStage);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(" Black List");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void UserManagement(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/UserManage.fxml"));
            root = loader.load();
            UserManageController manage = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("User ManageMent ");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void manageDelete(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/ClearProgramData.fxml"));
            root = loader.load();
            ClearProgramDataController manage = loader.getController();
            manage.setStage(currentStage);
            manage.setCurrent(currentUser);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Clear Program Data ");
            stage.setScene(scene);
            stage.initOwner(currentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setPermission() {
        reciption.setVisible(toBool(per.getReception()));
        doctor.setVisible(toBool(per.getDoctor()));
        deathNote.setVisible(toBool(per.getDeath_note()));
        chemoCheck.setVisible(toBool(per.getChemo_check()));
        clinicalPH.setVisible(toBool(per.getClinical_pharmacy()));
        prepareDrug.setVisible(toBool(per.getPrepare_drug()));
        blackList.setVisible(toBool(per.getBlack_list()));
        drugDose.setVisible(toBool(per.getDrug_dose()));
        patManage.setVisible(toBool(per.getPat_management()));
        patCost.setVisible(toBool(per.getPat_cost()));
        lab.setVisible(toBool(per.getLab()));
        doctorScreen.setVisible(toBool(per.getDoc_screen()));
        labScreen.setVisible(toBool(per.getLab_screen()));
        PHScreen.setVisible(toBool(per.getPharmacy_screen()));
        searchForPat.setVisible(toBool(per.getSearch_for_pat()));
        serachForDrug.setVisible(toBool(per.getSerach_for_drug()));
        userManage.setVisible(toBool(per.getUser_manage()));
        drugManage.setVisible(toBool(per.getDrug()));
        labTest.setVisible(toBool(per.getLab_test()));
        diaManage.setVisible(toBool(per.getDia_manage()));
        rigManage.setVisible(toBool(per.getRegion_manage()));
        pref.setVisible(toBool(per.getPref_manage()));
        deleteData.setVisible(toBool(per.getDelData()));

    }

    private boolean toBool(int i) {
        return i == 1;
    }

    private void setUserPermission() {
        try {
            per = AUTH.getUserAuth(currentUser);
            if (per.getIs_admin() != 1) {
                setPermission();
            }
            setTabPane();
        } catch (SQLException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTabPane() {
        ArrayList<Tab> alltabs = new ArrayList<>(mainTabPane.getTabs());
        for (Tab tab : alltabs) {
            FlowPane p = (FlowPane) tab.getContent();
            ArrayList<Node> no = new ArrayList<>(p.getChildren());
            ArrayList<Integer> i = new ArrayList<>();
            for (Node node : no) {
                if (node.isVisible()) {
                    i.add(1);
                } else {
                    p.getChildren().remove(node);
                }
            }
            if (i.size() <= 0) {
                mainTabPane.getTabs().remove(tab);
            }
        }
    }
}
