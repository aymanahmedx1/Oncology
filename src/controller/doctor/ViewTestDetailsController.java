/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.doctor;

import BAO.LabGroupBAO;
import BAO.LabOrderManage;
import BAO.patient.PatientManage;
import DAO.LabGroup;
import DAO.LabOrder;
import DAO.LabVisit;
import DAO.patient.Patient;
import DAO.patient.PatientMovement;
import com.jfoenix.controls.JFXButton;
import commons.Helpers;
import commons.RunReport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.apache.commons.compress.utils.FileNameUtils;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class ViewTestDetailsController implements Initializable {

    private Stage stage;
    private final LabOrderManage LAB_MANAGE = new LabOrderManage();
    private HashMap<Integer, TextField> getTextFiled = new HashMap<>();
    private ArrayList<LabGroup> orderLabGroups = new ArrayList<>();
    private final PatientManage PAT = new PatientManage();
    private LabOrder currentOrder;
    private ArrayList<LabVisit> orderDetails;
    private ArrayList<LabGroup> allLabGroup;
    @FXML
    private TextField patNameTxt;
    @FXML
    private TextField patIdTxt;
    @FXML
    private TextField orderNotesTxt;
    @FXML
    private TabPane mainTapPan;
    private boolean resultAdd = false;
    @FXML
    private Label nameLbl1111;
    @FXML
    private TextField txtAge;
    @FXML
    private Label phoneLbl111111;
    @FXML
    private TextField txtDia;
    private Patient currentPat;
    private boolean print = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            getLabGroup();
            getData();
        });
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.hide();
    }

    @FXML
    private void saveByEnter(KeyEvent event) {
    }

    @FXML
    private void save(ActionEvent event) {
        Tab selected = mainTapPan.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Send group To Doctor !\n Dor you want to procced ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent() && res.get().equals(ButtonType.YES)) {
            if (!checkallResultAdd(selected)) {
                Alert al = new Alert(Alert.AlertType.ERROR, "Please Add All Result ", ButtonType.OK);
                al.show();
                return;
            }
            updateGroupState(selected);
            sentToDoctor(currentOrder);
            mainTapPan.getTabs().remove(selected);
            printGroupResult(selected);
            if (mainTapPan.getTabs().isEmpty()) {
                finishOrder();
                currentOrder.setCall(LabOrder.NOT_CALL);
                LAB_MANAGE.updateCallState(currentOrder);
                stage.hide();
            }
        }
    }

    public LabOrder getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(LabOrder currentOrder) {
        this.currentOrder = currentOrder;
    }

    private void getData() {
        try {
            currentPat = PAT.findByBarcode(currentOrder.getPatId());
            patNameTxt.setText(currentOrder.getPatName());
            patIdTxt.setText(String.valueOf(currentOrder.getPatFileId()));
            orderNotesTxt.setText(currentOrder.getNote());
            txtAge.setText(String.valueOf(Helpers.calculateAge(currentPat.getBirth().toLocalDate())));
            txtDia.setText(currentPat.getDiagnosis().getName());
            orderDetails = LAB_MANAGE.getOrderDetails(currentOrder, LabOrder.STILL);
            ArrayList<Integer> group = new ArrayList<>();
            for (LabVisit order : orderDetails) {
                if (!group.contains(order.getGroupId())) {
                    group.add(order.getGroupId());
                }
            }
            for (Integer integer : group) {
                for (LabGroup labGroup : allLabGroup) {
                    if (labGroup.getId() == integer) {
                        orderLabGroups.add(labGroup);
                    }
                }
            }
            bulidGroups();
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    private VBox bulidTestItems(LabVisit visit) {
        VBox vbox = new VBox();
        HBox v = new HBox();
        v.getStyleClass().add("main");
        v.setAlignment(Pos.CENTER_LEFT);
        v.setSpacing(5);
        TextField textF = new TextField(visit.getResult());
        textF.textProperty().addListener((observable, oldValue, newValue) -> {
            visit.setResult(newValue);
        });
        Label lb = new Label(visit.getTestName());
        Label or = new Label("Or");
        lb.getStyleClass().add("testName");
        textF.getStyleClass().add("tf");
        Button btn = new Button(" Choose File");
        JFXButton clear = new JFXButton("X");
        clear.setButtonType(JFXButton.ButtonType.RAISED);
        btn.setStyle((visit.getResultFile() == null) ? "" : " -fx-background-color:green ;\n"
                + "    -fx-text-fill: whitesmoke;\n");
        btn.setOnAction((event) -> {
            File file = fileChooser();
            if (file != null) {
                btn.setStyle(" -fx-background-color:green ;\n"
                        + "    -fx-text-fill: whitesmoke;\n"
                );
                visit.setResultFile(file.getPath());
                clear.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
            }
        });
        clear.setOnAction((event) -> {
            if (visit.getResultFile().equals("") || visit.getResultFile() != null) {
                visit.setResultFile("");
                btn.setStyle("");
                clear.setBackground(Background.EMPTY);
            }
        });
        v.getChildren().add(lb);
        v.getChildren().add(textF);
        v.getChildren().add(or);
        v.getChildren().add(btn);
        v.getChildren().add(clear);
        vbox.getChildren().add(v);
        return vbox;
    }

    private File fileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Result File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "desktop"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }

    private File SaveFile(String choosedFile, LabVisit visit) {
        FileOutputStream fbs = null;
        try {
            File dir = new File(Helpers.SERVER_FILE_PATH + currentOrder.getPatId());
            if (!dir.exists()) {
                dir.mkdir();
            }
            File f = new File(Helpers.SERVER_FILE_PATH + currentOrder.getPatId() + System.getProperty("file.separator") + visit.getId() + visit.getDate() + "." + FileNameUtils.getExtension(choosedFile));
            fbs = new FileOutputStream(f);
            Files.copy(Paths.get(choosedFile), fbs);
            return f;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ViewTestDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ViewTestDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fbs.close();
            } catch (IOException ex) {
                Logger.getLogger(ViewTestDetailsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void getLabGroup() {
        try {
            allLabGroup = new LabGroupBAO().AllLabGroup();
        } catch (SQLException ex) {
            Logger.getLogger(ViewTestDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void bulidGroups() {
        for (LabGroup group : orderLabGroups) {
            Tab tab = new Tab(group.getName());
            FlowPane pane = new FlowPane();
            pane.setAlignment(Pos.TOP_CENTER);
            pane.setHgap(10);
            pane.setVgap(10);
            pane.setPadding(new Insets(15, 0, 0, 0));
            for (LabVisit orderDetail : orderDetails) {
                if (orderDetail.getGroupId() == group.getId()) {
                    VBox vbox = bulidTestItems(orderDetail);
                    pane.getChildren().add(vbox);
                }
            }
            tab.setContent(pane);
            tab.setClosable(true);
            tab.setId(String.valueOf(group.getId()));
            tab.setOnCloseRequest((event) -> {
            });
            tab.setOnClosed((event) -> {

            });
            mainTapPan.getTabs().add(tab);
        }
    }

    private void updateGroupState(Tab tab) {
        try {
            for (LabVisit visit : orderDetails) {
                if (visit.getGroupId() == Integer.parseInt(tab.getId())) {
                    if (visit.getResultFile() != null) {
                        File f = SaveFile(visit.getResultFile(), visit);
                        visit.setResultFile(f.getPath());
                    }
                    LAB_MANAGE.addTestResult(visit);
                    LAB_MANAGE.updateLabOrderDetailsState(visit);
                    resultAdd = true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ViewTestDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sentToDoctor(LabOrder item) {
        try {
            PatientMovement pat = new PatientMovement();
            pat.setId(item.getMovementNo());
            if (Helpers.PRES_MANAGE.isPatHasPrescToday(item.getPatId())) {
                Helpers.MOVEMENT.updateMovement(pat, PatientMovement.DRUG_ADD, PatientMovement.SEVRICE_TYPE_DRUG);
            } else {
                Helpers.MOVEMENT.updateMovement(pat, PatientMovement.LAB_ADD, PatientMovement.SEVRICE_TYPE_LAB);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void finishOrder() {
        try {
            LAB_MANAGE.updateTestState(currentOrder, PatientMovement.FINISH);
            stage.hide();
        } catch (SQLException ex) {
            Logger.getLogger(ViewTestDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isResultAdd() {
        return resultAdd;
    }

    public void setResultAdd(boolean resultAdd) {
        this.resultAdd = resultAdd;
    }

    private void printGroupResult(Tab selected) {
        Thread t = new Thread(() -> {
            RunReport runReport = new RunReport();
            HashMap<String, Object> params = new HashMap<>();
            params.put("name", currentPat.getName());
            params.put("id", currentPat.getId());
            params.put("barcode", currentPat.getPatId());
            params.put("as", currentPat.getSurface());
            params.put("wt", currentPat.getWeight());
            Date d = (currentPat.getBirth() == null) ? Date.valueOf(LocalDate.now()) : currentPat.getBirth();
            params.put("age", Helpers.calculateAge(currentPat.getBirth().toLocalDate()));
            params.put("dia", currentPat.getDiagnosis().getName());
            params.put("grpName", selected.getText());
            params.put("grpId", selected.getId());
            params.put("orderId", currentOrder.getId());
            params.put("reg_par", currentPat.getRegion().getName());
            runReport.showReport(RunReport.LAB_CATEGORY_RESULT, params);
        });
        t.setDaemon(true);
        t.start();
    }

    private boolean checkallResultAdd(Tab selected) {
        for (LabVisit order : orderDetails) {
            if (order.getGroupId() == Integer.parseInt(selected.getId())) {
                if (order.getResult() == null && order.getResultFile() == null) {
                    return false;
                } else {
                    if (order.getResult() != null) {
                        if (order.getResult().equals("")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                    if (order.getResultFile() != null) {
                        if (order.getResultFile().equals("")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }

}

// for (LabVisit visit : orderDetails) {
//            try {
//                if (visit.getResultFile() != null) {
//                    File f = SaveFile(visit.getResultFile(), visit);
//                    visit.setResultFile(f.getPath());
//                }
//                LAB_MANAGE.addTestResult(visit);
//                LAB_MANAGE.updateLabOrderDetailsState(visit);
//                sentToDoctor(currentOrder);
//                finishOrder();
//            } catch (SQLException ex) {
//                Logger.getLogger(ViewTestDetailsController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        closeWindow(event);
