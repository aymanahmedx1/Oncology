/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.drug;

import BAO.DrugManagment;
import DAO.Drug;
import DAO.user.User;
import com.jfoenix.controls.JFXTextField;
import commons.RunReport;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class DrugDataController implements Initializable {

    private Stage stage;
    private User current;
    private ObservableList<Drug> allDrug;
    private ObservableList<Drug> filterd = FXCollections.observableArrayList();
    private static final DrugManagment DRUG_MANAGE = new DrugManagment();
    private ArrayList<Drug> allFluid;
    private static final String SELECT = " -- NONE -- ";
    private HashMap<String, Integer> getFluidId = new HashMap<>();
    private HashMap<String, Integer> getCategory = new HashMap<>();
    private HashMap<Integer, String> getFluidName = new HashMap<>();
    private Drug currentDrug;
    @FXML
    private TextField strengthTxt;
    @FXML
    private ComboBox<String> categoryCombo;
    @FXML
    private TextField drugTxt;
    @FXML
    private ComboBox<String> fluidCombo;
    @FXML
    private TextField volumeTxt;
    @FXML
    private JFXTextField filterTxt;
    @FXML
    private TableView<Drug> table;
    @FXML
    private TableColumn<Drug, String> categoryCol;
    @FXML
    private TableColumn<Drug, String> drugCol;
    @FXML
    private TableColumn<Drug, Integer> strenghtCol;
    @FXML
    private TableColumn<Drug, Float> stockCol;
    @FXML
    private TableColumn<Drug, Drug> vialsCol;
    @FXML
    private Button clearBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField strengthTxt2;
    @FXML
    private TableColumn<Drug, Integer> strenghtCol1;
    @FXML
    private TextField strengthTxt3;
    @FXML
    private TableColumn<Drug, Integer> strenghtCol3;
    @FXML
    private TextField txtStock;
    @FXML
    private TextArea txtNote;
    @FXML
    private ComboBox<String> filterCombo;
    @FXML
    private Button btnPrint;
    private int cat = 0;
    @FXML
    private TableColumn<?, ?> colNo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            getData();
            setTableUi();
            setComboData();
            textFilter();
        });
    }

    @FXML
    private void selectItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            currentDrug = table.getSelectionModel().getSelectedItem();
            setUi();

        }
    }

    @FXML
    private void clear(ActionEvent event) {
        drugTxt.clear();
        fluidCombo.getSelectionModel().select(SELECT);
        volumeTxt.clear();
        strengthTxt.clear();
        strengthTxt3.clear();
        strengthTxt2.clear();
        txtStock.clear();
        txtNote.clear();
        currentDrug = null;
    }

    @FXML
    private void save(ActionEvent event) {

        if (checkData()) {
            if (currentDrug == null) {
                if (checkDiplicated()) {
                    saveNew();
                }
            } else {
                updateDrug();
            }
            getData();
            clear(event);
        }
    }

    private void setTableUi() {
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        drugCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        strenghtCol.setCellValueFactory(new PropertyValueFactory<>("strength"));
        strenghtCol1.setCellValueFactory(new PropertyValueFactory<>("strength2"));
        strenghtCol3.setCellValueFactory(new PropertyValueFactory<>("strength3"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        vialsCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );

        vialsCol.setCellFactory(param -> new TableCell<Drug, Drug>() {
            @Override
            protected void updateItem(Drug item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                    return;
                }
                if (item.getStrength() == 0) {
                    setText(String.valueOf(item.getStock()));
                    return;
                }
                int s1 = item.getStrength();
                float stock = item.getStock();
                float result = stock / s1;
                int res = (int) Math.floor(result);
                setText(String.valueOf(res));
            }

        });
        table.setItems(allDrug);
    }

    private void getData() {
        table.getItems().clear();

        try {
            allDrug = FXCollections.observableArrayList(DRUG_MANAGE.allDrug());
            int index = 1;
            for (Drug drug : allDrug) {
                drug.setNo(index++);
            }
            table.setItems(allDrug);
        } catch (SQLException ex) {
            Logger.getLogger(DrugDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setUi() {
        categoryCombo.getSelectionModel().select(currentDrug.getCategoryName());
        drugTxt.setText(currentDrug.getName());
        fluidCombo.getSelectionModel().select(
                (getFluidName.get(currentDrug.getDefFluid()) == null) ? SELECT : getFluidName.get(currentDrug.getDefFluid()));
        volumeTxt.setText(String.valueOf(currentDrug.getDefVolume()));
        strengthTxt.setText(String.valueOf(currentDrug.getStrength()));
        strengthTxt2.setText(String.valueOf(currentDrug.getStrength2()));
        strengthTxt3.setText(String.valueOf(currentDrug.getStrength3()));
        txtNote.setText(currentDrug.getNote());

    }

    private void setComboData() {

        try {
            fluidCombo.getItems().add(SELECT);
            allFluid = DRUG_MANAGE.allFluid();
            for (Drug fl : allFluid) {
                fluidCombo.getItems().add(fl.getName());
                getFluidId.put(fl.getName(), fl.getId());
                getFluidName.put(fl.getId(), fl.getName());
            }
            categoryCombo.getItems().add("CHEMOTHERAPY".toLowerCase());
            categoryCombo.getItems().add("SUPPORTIVE".toLowerCase());
            categoryCombo.getItems().add("FLUID".toLowerCase());
            filterCombo.getItems().add("All");
            filterCombo.getItems().add("CHEMOTHERAPY".toLowerCase());
            filterCombo.getItems().add("SUPPORTIVE".toLowerCase());
            filterCombo.getItems().add("FLUID".toLowerCase());
            getCategory.put("All", 0);
            getCategory.put("CHEMOTHERAPY".toLowerCase(), 1);
            getCategory.put("SUPPORTIVE".toLowerCase(), 2);
            getCategory.put("FLUID".toLowerCase(), 3);
            categoryCombo.getSelectionModel().selectFirst();
            filterCombo.getSelectionModel().selectFirst();
        } catch (SQLException ex) {
            Logger.getLogger(DrugDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private boolean checkData() {
        if (drugTxt.getText().trim().equals("")) {
            alert("Drug Name Is Required !");
            drugTxt.requestFocus();
            return false;
        }
        if (strengthTxt.getText().trim().equals("")) {
            alert("strength Is Required !");
            strengthTxt.requestFocus();
            return false;
        }
        if (strengthTxt2.getText().isEmpty() || strengthTxt2.getText().trim().equals("")) {
            strengthTxt2.setText("0");
        }
        if (strengthTxt2.getText().isEmpty() || strengthTxt3.getText().trim().equals("")) {
            strengthTxt3.setText("0");
        }
        return true;
    }

    private void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.show();
    }

    private void saveNew() {
        try {
            Drug drug = new Drug();
            drug.setCategory(getCategory.get(categoryCombo.getSelectionModel().getSelectedItem().trim()));
            drug.setName(drugTxt.getText());
            drug.setStrength(Integer.parseInt(strengthTxt.getText()));
            if (drug.getCategory() != Drug.FLUID) {
                if (fluidCombo.getSelectionModel().getSelectedItem() != null && !fluidCombo.getSelectionModel().getSelectedItem().equals(SELECT)) {
                    drug.setDefFluid(getFluidId.get(fluidCombo.getSelectionModel().getSelectedItem()));
                    drug.setDefVolume(Integer.parseInt(volumeTxt.getText()));
                }
            }
            drug.setStrength2(Integer.parseInt(strengthTxt2.getText()));
            drug.setStrength3(Integer.parseInt(strengthTxt3.getText()));
            drug.setStock(Float.parseFloat(txtStock.getText()));
            drug.setNote(txtNote.getText());
            drug.setId(DRUG_MANAGE.addDrug(drug));
            currentDrug = drug;
            addFluidToCombo(currentDrug);
        } catch (SQLException ex) {
            Logger.getLogger(DrugDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateDrug() {
        try {
            currentDrug.setCategory(getCategory.get(categoryCombo.getSelectionModel().getSelectedItem().trim()));
            currentDrug.setName(drugTxt.getText());
            currentDrug.setStrength(Integer.parseInt(strengthTxt.getText()));
            if (currentDrug.getCategory() != Drug.FLUID) {
                if (!fluidCombo.getSelectionModel().getSelectedItem().equals(SELECT)) {
                    currentDrug.setDefFluid(getFluidId.get(fluidCombo.getSelectionModel().getSelectedItem()));
                    currentDrug.setDefVolume(Integer.parseInt(volumeTxt.getText()));
                } else {
                    currentDrug.setDefFluid(0);
                    currentDrug.setDefVolume(0);
                }
            }
            currentDrug.setStrength2(Integer.parseInt(strengthTxt2.getText()));
            currentDrug.setStrength3(Integer.parseInt(strengthTxt3.getText()));
            currentDrug.setNote(txtNote.getText());
            DRUG_MANAGE.update(currentDrug);
            getData();
            addFluidToCombo(currentDrug);
        } catch (SQLException ex) {
            Logger.getLogger(DrugDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean checkDiplicated() {
        for (Drug drug : allDrug) {
            if (drugTxt.getText().equals(drug.getName())) {
                drugTxt.requestFocus();
                alert("Drug Name Is Duplicated !");
                return false;
            }
        }
        return true;
    }

    private void textFilter() {
        filterTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filterd.clear();
            for (Drug drug : allDrug) {
                if (drug.getName().startsWith(newValue, 0)) {
                    filterd.add(drug);
                }
            }
            table.setItems(filterd);
        });

    }

    @FXML
    private void addStock(ActionEvent event) {
        try {
            if (!txtStock.getText().isEmpty()) {
                if (currentDrug != null) {
                    float oldValue = currentDrug.getStock();
                    float newValue = Float.parseFloat(txtStock.getText());
                    currentDrug.setStock(oldValue + newValue);
                    DRUG_MANAGE.updateDrugStock(currentDrug);
                    getData();
                    clear(event);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DrugDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void filterDrug(ActionEvent event) {
        cat = getCategory.get(filterCombo.getSelectionModel().getSelectedItem());
        if (cat == 0) {
            int index = 1;
            for (Drug drug : allDrug) {
                drug.setNo(index++);
            }
            table.setItems(allDrug);
            return;
        }
        filterd.clear();
        int in = 1;
        for (Drug drug : allDrug) {
            if (drug.getCategory() == cat) {
                drug.setNo(in++);
                filterd.add(drug);
            }
        }
        table.setItems(filterd);
    }

    @FXML
    private void printPage(ActionEvent event) {

        RunReport r = new RunReport();
        try {
            if (cat != 0) {
                HashMap<String, Object> params = new HashMap();
                params.put("category", cat);
                params.put("catName", filterCombo.getSelectionModel().getSelectedItem());
                r.showReport(RunReport.CATEGORY_DRUG, params);
            } else {
                r.showReport(RunReport.ALL_DRUG_REPORT, null);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    private void addFluidToCombo(Drug drug) {
        if (drug.getCategory() == Drug.FLUID) {
            allFluid.add(drug);
            fluidCombo.getItems().add(drug.getName());
            getFluidId.put(drug.getName(), drug.getId());
            getFluidName.put(drug.getId(), drug.getName());

        }
    }
}
