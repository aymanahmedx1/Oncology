/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.drug;

import BAO.LabGroupBAO;
import DAO.LabGroup;
import DAO.LabTest;
import DAO.user.User;
import com.jfoenix.controls.JFXTextField;
import commons.Helpers;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import sorters.LabTestSort;

/**
 * FXML Controller class
 *
 * @author Ayman
 */
public class LabDataController implements Initializable {

    private HashMap< String, LabGroup> CombpMap = new HashMap<>();
    private HashMap<Integer, String> tableMap = new HashMap<>();
    private Stage stage;
    private User current;
    private LabTest currentLabTest;
    private ObservableList<LabTest> allTest;
    private ObservableList<LabTest> filterd = FXCollections.observableArrayList();
    private final LabGroupBAO LAB_MANAGE = new LabGroupBAO();
    private ArrayList<LabGroup> allLabGroups;
    private ArrayList<LabGroup> filterdLabGroup = new ArrayList<>();
    private ObservableList<LabGroup> labGroupTable;
    @FXML
    private ComboBox<String> categoryCombo;
    @FXML
    private TextField testTxt;
    @FXML
    private JFXTextField filterTxt;
    @FXML
    private TableView<LabTest> table;
    @FXML
    private TableColumn<LabTest, String> testCol;
    @FXML
    private TableColumn<LabTest, LabTest> categoryCol;
    @FXML
    private TableColumn<LabTest, Integer> colTestNo;
    @FXML
    private Button clearBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField groupName;
    @FXML
    private Button btnClearGroup;
    @FXML
    private Button btnSaveGroup;
    @FXML
    private TableView<LabGroup> groupTable;
    @FXML
    private TableColumn<LabGroup, String> colGroupName;
    @FXML
    private TableColumn<LabGroup, Integer> colCatNo;
    private boolean updateGroup = false;
    private LabGroup updateLabGroup = null;
    @FXML
    private Label messageLabel;
    @FXML
    private JFXTextField txtGroupFilter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            getData();
            setTableUi();
            setComboBoxData();
            textFilter();
        });
    }

    @FXML
    private void selectItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            currentLabTest = table.getSelectionModel().getSelectedItem();
            setUi();

        }
    }

    @FXML
    private void clear(ActionEvent event) {
        testTxt.clear();
        currentLabTest = null;

    }

    @FXML
    private void save(ActionEvent event) {
        LabTest labTest = new LabTest();
        labTest.setTestName(testTxt.getText());
        labTest.setGroup(CombpMap.get(categoryCombo.getSelectionModel().getSelectedItem()).getId());
        if (!testTxt.getText().trim().equals("")) {
            try {
                if (currentLabTest == null) {
                    Helpers.LAB_TEST_MANAGE.add(labTest);
                    setMessage("Test Added");
                } else {
                    labTest.setTestId(currentLabTest.getTestId());
                    Helpers.LAB_TEST_MANAGE.update(labTest);
                    setMessage("Test Updated Success");
                }
                clear(event);
                getData();
            } catch (SQLException ex) {
                Logger.getLogger(LabDataController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void getData() {
        table.getItems().clear();
        try {
            allLabGroups = LAB_MANAGE.AllLabGroup();
            allTest = FXCollections.observableArrayList(Helpers.LAB_TEST_MANAGE.allLabTest());
            allTest.sort(new LabTestSort());
            int index = 1;
            for (LabTest labTest : allTest) {
                labTest.setNo(index++);
            }
            table.setItems(allTest);
            filterdLabGroup = new ArrayList<>(allLabGroups);
        } catch (SQLException ex) {
            Logger.getLogger(DrugDataController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setTableUi() {
        testCol.setCellValueFactory(new PropertyValueFactory<>("testName"));
        colCatNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colTestNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colGroupName.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        categoryCol.setCellFactory(param -> new TableCell<LabTest, LabTest>() {
            @Override
            protected void updateItem(LabTest item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setText("");
                    return;
                }

                setText(tableMap.get(item.getGroup()));
            }

        });
        table.setItems(allTest);

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

    private void setUi() {
        testTxt.setText(currentLabTest.getTestName());
        for (Map.Entry<String, LabGroup> entry : CombpMap.entrySet()) {
            if (entry.getValue().getId() == currentLabTest.getGroup()) {
                categoryCombo.getSelectionModel().select(entry.getKey());
                break;
            }
        }

    }

    private void setComboBoxData() {
        for (LabGroup gr : allLabGroups) {
            CombpMap.put(gr.getName(), gr);
            categoryCombo.getItems().add(gr.getName());
            tableMap.put(gr.getId(), gr.getName());
        }
        categoryCombo.getSelectionModel().selectFirst();
        labGroupTable = FXCollections.observableArrayList(allLabGroups);
        int index = 1;
        for (LabGroup labGroup : labGroupTable) {
            labGroup.setNo(index++);
        }
        groupTable.setItems(labGroupTable);
    }

    private void textFilter() {
        filterTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTestName();
        });
        txtGroupFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterGroup();
        });

    }

    @FXML
    private void saveGroup(ActionEvent event) {
        if (!groupName.getText().isEmpty()) {
            try {
                if (CheckDuplicated(groupName.getText())) {
                    if (updateGroup) {
                        updateLabGroup.setName(groupName.getText());
                        LAB_MANAGE.update(updateLabGroup);
                        setMessage("Group Updated");
                        clearGroup(event);
                    } else {
                        LabGroup group = new LabGroup();
                        group.setName(groupName.getText());
                        LAB_MANAGE.add(group);
                        setMessage("Group Added");
                    }
                    allLabGroups.clear();
                    categoryCombo.getItems().clear();
                    setComboBoxData();
                    getData();
                    setComboBoxData();
                    groupName.clear();
                } else {
                    setMessage("Group Duplicated");
                }
            } catch (SQLException ex) {
                Logger.getLogger(LabDataController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean CheckDuplicated(String groupName) {
        for (LabGroup group : allLabGroups) {
            if (group.getName().equals(groupName)) {
                return false;
            }
        }
        return true;
    }

    @FXML
    private void clearGroup(ActionEvent event) {
        updateGroup = false;
        updateLabGroup = null;
        groupName.clear();
    }

    @FXML
    private void groupClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            updateLabGroup = groupTable.getSelectionModel().getSelectedItem();
            groupName.setText(updateLabGroup.getName());
            updateGroup = true;
        }

    }

    private void setMessage(String text) {
        messageLabel.setText(text);
        messageLabel.setStyle("-fx-text-fill: red;");
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(ev -> {
            messageLabel.setText("");
        });
        delay.play();
    }

    private void filterGroup() {
        if (!txtGroupFilter.getText().isEmpty() && !txtGroupFilter.getText().equals("")) {
            filterdLabGroup.clear();
            filterd.clear();
            for (LabGroup group : allLabGroups) {
                if (group.getName().toLowerCase().startsWith(txtGroupFilter.getText().toLowerCase())) {
                    filterdLabGroup.add(group);
                }
            }
            int index = 1;
            for (LabTest labTest : allTest) {
                for (LabGroup labGroup : filterdLabGroup) {
                    if (labTest.getGroup() == labGroup.getId()) {
                        labTest.setNo(index++);
                        filterd.add(labTest);
                    }
                }
            }
            filterd.sorted(new LabTestSort());
            table.setItems(filterd);
        } else {
            filterd.sorted(new LabTestSort());
            table.setItems(allTest);
        }
    }

    private void filterTestName() {
        filterd.clear();
        if (!filterTxt.getText().isEmpty() && !filterTxt.getText().equals("")) {
            ArrayList<LabTest> temp = new ArrayList<>();
//            if (filterdLabGroup.size() > 0) {
//                int index = 1;
//                for (LabTest test : allTest) {
//                    {
//                        if (test.getTestName().toLowerCase().startsWith(filterTxt.getText().toLowerCase())) {
//                            temp.add(test);
//                        }
//                    }
//                }
//                for (LabTest labTest : temp) {
//                    for (LabGroup labGroup : filterdLabGroup) {
//                        if (labTest.getGroup() == labGroup.getId()) {
//                            labTest.setNo(index++);
//                            filterd.add(labTest);
//                        }
//                    }
//                }
//            } else {
            int index = 1;
            for (LabTest labTest : allTest) {
                if (labTest.getTestName().toLowerCase().startsWith(filterTxt.getText().toLowerCase())) {
                    labTest.setNo(index++);
                    filterd.add(labTest);
                }
            }
//            }
            filterd.sorted(new LabTestSort());
            table.setItems(filterd);
        } else {
            filterd.sorted(new LabTestSort());
            table.setItems(allTest);
        }
    }
}
