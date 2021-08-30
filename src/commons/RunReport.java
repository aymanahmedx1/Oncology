/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

import DAO.ReportData;
import controller.reception.ReceptionController;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Ayman
 */
public class RunReport {

    public RunReport() {
    }

    public static final String ALL_DRUG_REPORT = "/report/AllDrugReport.jrxml";
    public static final String CATEGORY_DRUG = "/report/CategoryDrug.jrxml";
    public static final String PAT_DRUG_REPORT = "/report/PatientDrugReport.jrxml";
    public static final String DRUG_LABEL = "/report/drugLabel.jrxml";
    public static final String DRUG_DOSE = "/report/DrugDose.jrxml";
    public static final String DRUG_DOSE_CEMO = "/report/DrugDoseCemo.jrxml";
    public static final String DRUG_DOSE_FLUID = "/report/DrugDoseFluid.jrxml";
    public static final String SERACH_FOR_DRUG = "/report/SearchForDrug.jrxml";
    public static final String SERACH_FOR_PAT_ALL = "/report/SearchPatDrugAll.jrxml";
    public static final String SERACH_FOR_PAT = "/report/SearchPatDrug.jrxml";
    public static final String PATIENT_COST = "/report/PatientCost.jrxml";
    public static final String PATIENT_COST_CEMO = "/report/PatientCostCemo.jrxml";
    public static final String PATIENT_COST_FLUID = "/report/PatientCostFulid.jrxml";
    public static final String LAB_CATEGORY_RESULT = "/report/LabCategoryResult.jrxml";
    public static final String PATIENT_INFORMATION = "/report/InformationPatient.jrxml";
    public static final String DEATH_NOTE = "/report/DeathPatient.jrxml";

    public static void runReport(ReportData report) {
        try {
            PrintService printer = PrintServiceLookup.lookupDefaultPrintService();
            PrinterJob printerJob = PrinterJob.getPrinterJob();

            if (printer != null) {
                printerJob.setPrintService(printer);
                JasperReport jr = JasperCompileManager.compileReport(report.getLocation());
//                JasperReport jr = JasperCompileManager.compileReport("src/report/barcode.jrxml");
                JasperPrint jp = JasperFillManager.fillReport(jr, report.getParam(), report.getDataSource());
                jp.setName(report.getReportName());
                PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                printRequestAttributeSet.add(new Copies(1));
                JRPrintServiceExporter exporter = new JRPrintServiceExporter();
                exporter.setExporterInput(new SimpleExporterInput(jp));
                exporter.exportReport();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
                alert.setTitle(" Printer Not Found");
                alert.setContentText(" Please make sure the printer Is connected \n And printer name is  " + report.getPrinter());
                alert.show();
            }
        } catch (PrinterException | JRException ex) {
            Logger.getLogger(ReceptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showReport(String reportPath, HashMap<String, Object> params) {

        try {
            try (Connection con = DBConnection.createConnection()) {
                JasperReport jr = JasperCompileManager.compileReport(this.getClass().getResourceAsStream(reportPath));
                JasperPrint jp = JasperFillManager.fillReport(jr, params, con);
                jp.setOrientation(OrientationEnum.PORTRAIT);
                JasperViewer.viewReport(jp, false);
            }
        } catch (SQLException | JRException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace());
        }
    }

    public void ExportToExcel(String reportPath, HashMap<String, Object> params) {
        try (Connection con = DBConnection.createConnection()) {
            JasperReport jr = JasperCompileManager.compileReport(this.getClass().getResourceAsStream(reportPath));
            JasperPrint xlsPrint = JasperFillManager.fillReport(jr, params, con);
            JRXlsExporter xlsExporter = new JRXlsExporter();
            xlsExporter.setExporterInput(new SimpleExporterInput(xlsPrint));
            xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput("c:\\name"));
            SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
            xlsReportConfiguration.setOnePagePerSheet(false);
            xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
            xlsReportConfiguration.setDetectCellType(false);
            xlsReportConfiguration.setWhitePageBackground(false);
            xlsExporter.setConfiguration(xlsReportConfiguration);
            xlsExporter.exportReport();
        } catch (JRException | SQLException ex) {
            Logger.getLogger(RunReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
