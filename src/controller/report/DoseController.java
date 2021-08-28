/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.report;

import BAO.DoseManagement;
import BAO.PrescriptionManagement;
import DAO.Drug;
import DAO.Prescription;
import com.jfoenix.controls.JFXButton;
import sorters.DrugSort;
import commons.Helpers;
import commons.NewExcelExport;
import commons.RunReport;
import controller.pharmacy.PharmacyController;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class DoseController implements Initializable {

    private final PrescriptionManagement PRES_MANAGE = new PrescriptionManagement();
    private final DoseManagement DOSE_MANAGE = new DoseManagement();
    private ObservableList<Drug> allDrugs = FXCollections.observableArrayList();
    private ObservableList<Drug> filterdDrug = FXCollections.observableArrayList();
    private HashMap<String, Integer> getCategory = new HashMap<>();
    private ArrayList<Prescription> allPrescription = new ArrayList<>();
    private ArrayList<Prescription> filtersPrescription = new ArrayList<>();
    private final String ALL = "All";
    private final String CEMO = "Chemotherapy";
    private final String SUPPORT = "Supportive";
    private final String FLUID = "Fluid";
    private final String S_PAT = "Supportive patients";
    private final String C_PAT = "Chemotherapy patients";
    private Stage stage;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private ComboBox<String> comboDrugCat;
    @FXML
    private JFXButton search;
    @FXML
    private TableView<Drug> presTable;
    @FXML
    private TableColumn<Drug, String> colName;
    @FXML
    private TableColumn<Drug, Float> colDose;
    @FXML
    private TableColumn<Drug, Integer> colVials;
    @FXML
    private TableColumn<Drug, Integer> colNo;
    @FXML
    private ComboBox<String> comboPatType;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setTableUi();
            setDateConverter();
            setCategoryComboData();
            getData();
        });
    }

    private void getData() {
        try {
            Date from = Date.valueOf(fromDate.getValue());
            Date to = Date.valueOf(toDate.getValue());
            allPrescription = PRES_MANAGE.allPresFromDateToDate(from, to);
            handelTypeChange();
        } catch (SQLException ex) {
            Logger.getLogger(DoseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTableUi() {
        presTable.setId("my-table");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDose.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colVials.setCellValueFactory(new PropertyValueFactory<>("noOfVials"));
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        presTable.setItems(allDrugs);

    }

    @FXML
    private void printPage(ActionEvent event) {
        NewExcelExport ex = new NewExcelExport();
        String dateFromTo = "Date From  : " + fromDate.getValue().toString() + "  To : " + toDate.getValue().toString();
        ex.DrugDoseExcelReport(stage, new ArrayList<>(presTable.getItems()), catGetString(), dateFromTo);

//        try {
//            int selected = getCategory.get(comboDrugCat.getSelectionModel().getSelectedItem());
//            RunReport runReport = new RunReport();
//            HashMap<String, Object> params = new HashMap<>();
//            params.put("dfrom", fromDate.getValue().toString());
//            params.put("dto", toDate.getValue().toString());
//            switch (selected) {
//                case 0:
//                    runReport.showReport(RunReport.DRUG_DOSE, params);
//                    break;
//                case 1:
//                case 2:
//                    params.put("paracat", selected);
//                    params.put("cat_name", comboDrugCat.getSelectionModel().getSelectedItem());
//                    runReport.showReport(RunReport.DRUG_DOSE_CEMO, params);
//                    break;
//                case 3:
//                    params.put("cat_name", comboDrugCat.getSelectionModel().getSelectedItem());
//                    runReport.showReport(RunReport.DRUG_DOSE_FLUID, params);
//                    break;
//
//            }
//        } catch (NumberFormatException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, "Some thing went wrong " + e.getMessage(), ButtonType.OK);
//            alert.show();
//        }
    }

    @FXML
    private void drugCategoryChange(ActionEvent event) {
        ComboChange();
    }

    @FXML
    private void searchBtn(ActionEvent event) {
        getData();
    }

    @FXML
    private void getVisitData(MouseEvent event) {
    }

    private void setDateConverter() {
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

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setCategoryComboData() {

        comboDrugCat.getItems().add(ALL);
        comboDrugCat.getItems().add(CEMO);
        comboDrugCat.getItems().add(SUPPORT);
        comboDrugCat.getItems().add(FLUID);
        getCategory.put(ALL, 0);
        getCategory.put(CEMO, 1);
        getCategory.put(SUPPORT, 2);
        getCategory.put(FLUID, 3);
        getCategory.put(C_PAT, 1);
        getCategory.put(S_PAT, 2);
        comboDrugCat.getSelectionModel().selectFirst();
        comboPatType.getItems().add(ALL);
        comboPatType.getItems().add(C_PAT);
        comboPatType.getItems().add(S_PAT);
        comboPatType.getSelectionModel().selectFirst();
    }

    private void ComboChange() {
        if (!comboDrugCat.getSelectionModel().isEmpty() && comboDrugCat.getSelectionModel().getSelectedItem() != null) {
            filterdDrug.clear();
            int selected = getCategory.get(comboDrugCat.getSelectionModel().getSelectedItem());
            if (selected == 0) {
                int index = 1;
                for (Drug drug : allDrugs) {
                    drug.setNo(index++);
                }
                presTable.setItems(allDrugs);
            } else {
                for (Drug drug : allDrugs) {
                    if (drug.getCategory() == selected) {
                        filterdDrug.add(drug);
                    }
                }
                int index = 1;
                for (Drug drug : filterdDrug) {
                    drug.setNo(index++);
                }
                presTable.setItems(filterdDrug);
            }
        }
    }

    @FXML
    private void patTypeChange(ActionEvent event) {
        handelTypeChange();
    }

    private void handelTypeChange() {
        if (!comboPatType.getSelectionModel().isEmpty()) {
            int selected = getCategory.get(comboPatType.getSelectionModel().getSelectedItem());
            filtersPrescription.clear();
            for (Prescription pres : allPrescription) {
                try {
                    List<Integer> li = Helpers.PRES_MANAGE.getPresCategories(pres);
                    if (li.contains(selected) && selected == Drug.CHEMOTHERAPY) {
                        filtersPrescription.add(pres);
                    } else if (!li.contains(Drug.CHEMOTHERAPY) && selected == Drug.SUPPORTIVE) {
                        filtersPrescription.add(pres);
                    } else if (selected == 0) {
                        filtersPrescription.add(pres);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(PharmacyController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            getPrescriptionsDrugs();
        }
    }

    private void getPrescriptionsDrugs() {
        allDrugs.clear();
        presTable.getItems().clear();
        try {
            for (Prescription pres : filtersPrescription) {
                ArrayList<Drug> presDrugs = DOSE_MANAGE.getDrugDose(pres);
                for (Drug drug : presDrugs) {
                    if (allDrugs.contains(drug)) {
                        float stock = allDrugs.get(allDrugs.indexOf(drug)).getStock();
                        allDrugs.get(allDrugs.indexOf(drug)).setStock(stock + drug.getStock());
                    } else {
                        allDrugs.add(drug);
                    }
                }
            }
            for (Drug drug : allDrugs) {
                double stock = drug.getStock();
                if (drug.getStrength() != 0) {
                    double temp = Math.ceil(stock / drug.getStrength());
                    int result = (int) temp;
                    allDrugs.get(allDrugs.indexOf(drug)).setNoOfVials(result);
                }
            }
            allDrugs.sort(new DrugSort());
            ComboChange();
        } catch (SQLException ex) {
            Logger.getLogger(DoseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String catGetString() {
        int patCompo = getCategory.get(comboPatType.getSelectionModel().getSelectedItem());
        int drugCompo = getCategory.get(comboDrugCat.getSelectionModel().getSelectedItem());
        if (patCompo == Drug.CHEMOTHERAPY && drugCompo == Drug.SUPPORTIVE) {
            return "chemo patients-premedications";
        } else if (patCompo == Drug.CHEMOTHERAPY && drugCompo == Drug.FLUID) {
            return "chemo patients-fluids";
        } else if (patCompo == Drug.CHEMOTHERAPY && drugCompo == Drug.CHEMOTHERAPY) {
            return "chemo patients-chemotherapy";
        } else if (patCompo == Drug.SUPPORTIVE && drugCompo == 0) {
            return "supportive patients-All drugs";
        } else if (patCompo == Drug.SUPPORTIVE && drugCompo == Drug.SUPPORTIVE) {
            return "supportive patients-supportive drugs";
        } else if (patCompo == Drug.SUPPORTIVE && drugCompo == Drug.FLUID) {
            return "supportive patients-fluids";
        } else if (patCompo == 0 && drugCompo == 0) {
            return "all drugs";
        } else {
            return (comboPatType.getSelectionModel().getSelectedItem() + " " + comboDrugCat.getSelectionModel().getSelectedItem());
        }
    }
}
