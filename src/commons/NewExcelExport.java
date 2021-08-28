/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

import DAO.Drug;
import DAO.LabOrder;
import DAO.LabVisit;
import DAO.Prescription;
import DAO.VisitData;
import static commons.Helpers.LAB_MANAGE;
import static commons.Helpers.VISIT_MANAGE;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author DELL
 */
public class NewExcelExport {

    public void LabReportToExcel(Stage stage, ArrayList<LabOrder> orders, LocalDate from, LocalDate to, String CatName, int catSelectedId) throws SQLException {
        File file = fileName(stage);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Finished Lab Test Results");
        CellStyle cs = contentStyle(workbook, sheet);
        CellStyle header = headerStyle(workbook, sheet);
        Row pageHead = sheet.createRow(0);
        Cell head = pageHead.createCell(0, CellType.STRING);
        head.setCellValue("Finished Lab Test");
        head.setCellStyle(header);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        Row catName = sheet.createRow(1);
        Cell catNameCell = catName.createCell(0, CellType.STRING);
        catNameCell.setCellValue(CatName);
        catNameCell.setCellStyle(cs);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
        Row pageDate = sheet.createRow(2);
        Cell headDate = pageDate.createCell(0, CellType.STRING);
        headDate.setCellValue(" From Date  " + from.toString() + "  To Date  " + to.toString());
        headDate.setCellStyle(cs);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 6));
        Row tablHead = sheet.createRow(3);
        Cell patNo = tablHead.createCell(0, CellType.STRING);
        patNo.setCellValue("NO");
        patNo.setCellStyle(header);
        Cell patName = tablHead.createCell(1, CellType.STRING);
        patName.setCellValue("Patient Name");
        patName.setCellStyle(header);
        Cell patId = tablHead.createCell(2, CellType.STRING);
        patId.setCellValue(" ID ");
        patId.setCellStyle(header);
        Cell doctorname = tablHead.createCell(3, CellType.STRING);
        doctorname.setCellValue("Doctor Name");
        doctorname.setCellStyle(header);
        Cell testname = tablHead.createCell(4, CellType.STRING);
        testname.setCellValue("Test Name");
        testname.setCellStyle(header);
        Cell resuCell = tablHead.createCell(5, CellType.STRING);
        resuCell.setCellValue(" Result ");
        resuCell.setCellStyle(header);
        Cell date = tablHead.createCell(6, CellType.STRING);
        date.setCellValue("Date");
        date.setCellStyle(header);
        int rowCount = 4;
        int columnCount = 0;
        for (LabOrder order : orders) {
            Row row = sheet.createRow(rowCount++);
            ArrayList<LabVisit> labDetalis = LAB_MANAGE.getOrderDetails(order);
            if (isHaveSelectedGroup(labDetalis, catSelectedId) || catSelectedId == 0) {
                Cell c = row.createCell(columnCount++, CellType.STRING);
                Cell c1 = row.createCell(columnCount++, CellType.STRING);
                Cell c2 = row.createCell(columnCount++, CellType.STRING);
                Cell c3 = row.createCell(columnCount++, CellType.STRING);
                c.setCellStyle(cs);
                c1.setCellStyle(cs);
                c2.setCellStyle(cs);
                c3.setCellStyle(cs);
                c.setCellValue((String) String.valueOf(order.getNo()));
                c1.setCellValue((String) order.getPatName());
                c2.setCellValue((String) order.getPatFileId());
                c3.setCellValue((String) order.getDoctorName());
                for (LabVisit field : labDetalis) {
                    if (field.getGroupId() == catSelectedId || catSelectedId == 0) {
                        Cell cell = row.createCell(columnCount++, CellType.STRING);
                        Cell cell2 = row.createCell(columnCount++, CellType.STRING);
                        Cell c4 = row.createCell(columnCount++, CellType.STRING);
                        c4.setCellStyle(cs);
                        cell.setCellValue((String) field.getTestName());
                        cell2.setCellValue((String) field.getResult());
                        c4.setCellValue((String) order.getDate().toString());
                        cell.setCellStyle(cs);
                        cell2.setCellStyle(cs);
                        row = sheet.createRow(rowCount++);
                        columnCount = 4;
                    }
                }
                columnCount = 0;
            }
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        try (FileOutputStream outputStream = new FileOutputStream(file.getPath() + ".xlsx")) {
            workbook.write(outputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewExcelExport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NewExcelExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepareDrugToExcel(Stage stage, ArrayList<Prescription> pres, LocalDate from, LocalDate to, int drugSelected, String catName) throws SQLException {
        File se = fileName(stage);
        if (se == null) {
            return;
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Prepare Drug Report ");
                sheet.autoSizeColumn(0);
                System.out.println(sheet.getColumnWidth(0));
        sheet.setColumnWidth(1,8000);
        sheet.setColumnWidth(2,4000);
        sheet.setColumnWidth(3,5000);
        sheet.setColumnWidth(4,3000);
        sheet.setColumnWidth(5,5000);
        sheet.setColumnWidth(6,3000);
        sheet.setColumnWidth(7,8000);
        sheet.setColumnWidth(8,255*255);
        CellStyle cs = contentStyle(workbook, sheet);
        CellStyle header = headerStyle(workbook, sheet);
        Row pageHead = sheet.createRow(0);
        Cell head = pageHead.createCell(0, CellType.STRING);
        head.setCellValue(catName);
        head.setCellStyle(header);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        Row pageDate = sheet.createRow(1);
        Cell date = pageDate.createCell(0, CellType.STRING);
        date.setCellValue(" From Date  " + from.toString() + "  To Date  " + to.toString());
        date.setCellStyle(cs);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
        Row tablHead = sheet.createRow(2);
        Cell no = tablHead.createCell(0, CellType.STRING);
        no.setCellValue("NO");
        no.setCellStyle(header);
        Cell patName = tablHead.createCell(1, CellType.STRING);
        patName.setCellValue("Patient Name");
        patName.setCellStyle(header);
        Cell patId = tablHead.createCell(2, CellType.STRING);
        patId.setCellValue(" ID ");
        patId.setCellStyle(header);
        Cell druName = tablHead.createCell(3, CellType.STRING);
        druName.setCellValue("Drug");
        druName.setCellStyle(header);
        Cell drugDose = tablHead.createCell(4, CellType.STRING);
        drugDose.setCellValue("Dose");
        drugDose.setCellStyle(header);
        Cell fluidName = tablHead.createCell(5, CellType.STRING);
        fluidName.setCellValue("Fluid");
        fluidName.setCellStyle(header);
        Cell volume = tablHead.createCell(6, CellType.STRING);
        volume.setCellValue(" Vol ");
        volume.setCellStyle(header);
        Cell notes = tablHead.createCell(7, CellType.STRING);
        notes.setCellValue(" Notes ");
        notes.setCellStyle(header);
        int rowCount = 3;
        int columnCount = 0;
        for (Prescription res : pres) {
            Row row = sheet.createRow(rowCount++);
            Cell c1 = row.createCell(columnCount++, CellType.STRING);
            Cell c2 = row.createCell(columnCount++, CellType.STRING);
            Cell c3 = row.createCell(columnCount++, CellType.STRING);
            c1.setCellStyle(cs);
            c2.setCellStyle(cs);
            c3.setCellStyle(cs);
            c1.setCellValue((String) String.valueOf(res.getNo()));
            c2.setCellValue((String) res.getPatientName());
            c3.setCellValue((String) res.getPatientNumber());
            ObservableList<VisitData> temp = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(res));
            ObservableList<VisitData> visitDetalis = getVisitData(temp, drugSelected);
            for (VisitData detail : visitDetalis) {
                Cell cell = row.createCell(columnCount++, CellType.STRING);
                Cell cell2 = row.createCell(columnCount++, CellType.STRING);
                Cell cell3 = row.createCell(columnCount++, CellType.STRING);
                Cell cell4 = row.createCell(columnCount++, CellType.STRING);
                Cell cell5 = row.createCell(columnCount++, CellType.STRING);
                cell.setCellValue((String) detail.getDrugName());
                cell2.setCellValue((String) detail.getDose());
                cell3.setCellValue((String) detail.getFluidName());
                String vol = String.valueOf((detail.getVolume() == 0 ) ?"" : detail.getVolume()) ;
                cell4.setCellValue((String) vol);
                cell5.setCellValue((String) detail.getNote());
                cell.setCellStyle(cs);
                cell2.setCellStyle(cs);
                cell3.setCellStyle(cs);
                cell4.setCellStyle(cs);
                cell5.setCellStyle(cs);
                row = sheet.createRow(rowCount++);
                columnCount = 3;
            }
            columnCount = 0;
        }


        try (FileOutputStream outputStream = new FileOutputStream(se.getPath() + ".xlsx")) {
            workbook.write(outputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewExcelExport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NewExcelExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private File fileName(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "desktop"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All files", "*.*"));
        File file = fileChooser.showSaveDialog(stage);
        return file;
    }

    private CellStyle contentStyle(XSSFWorkbook workbook, XSSFSheet sheet) {
        CellStyle cs = workbook.createCellStyle();
        XSSFFont newFont = sheet.getWorkbook().createFont();
        newFont.setBold(false);
        newFont.setFontHeightInPoints(Short.valueOf("12"));
        newFont.setItalic(false);
        cs.setFont(newFont);
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        return cs;
    }

    private CellStyle headerStyle(XSSFWorkbook workbook, XSSFSheet sheet) {
        CellStyle cs = workbook.createCellStyle();
        XSSFFont newFont = sheet.getWorkbook().createFont();
        newFont.setBold(true);
        newFont.setFontHeightInPoints(Short.valueOf("14"));
        newFont.setItalic(false);
        cs.setFont(newFont);
        cs.setFillForegroundColor(IndexedColors.YELLOW1.getIndex());
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setFillBackgroundColor(Short.valueOf("5"));
        return cs;
    }

    public void DrugDoseExcelReport(Stage stage, ArrayList<Drug> drugs, String cat, String dateFromTo) {
        File file = fileName(stage);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Java Books");
        CellStyle cs = contentStyle(workbook, sheet);
        CellStyle header = headerStyle(workbook, sheet);
        // SHEET HEAD
        Row pageHead = sheet.createRow(0);
        Cell pageTitle = pageHead.createCell(0, CellType.STRING);
        pageTitle.setCellValue(" Drug Dose ");
        pageTitle.setCellStyle(header);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        ////// CATEGORY ROW 
        Row catRow = sheet.createRow(1);
        Cell catCell = catRow.createCell(0, CellType.STRING);
        catCell.setCellValue(cat);
        catCell.setCellStyle(cs);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
        /// Date Row
        Row dateRow = sheet.createRow(2);
        Cell dateCell = dateRow.createCell(0, CellType.STRING);
        dateCell.setCellValue(dateFromTo);
        dateCell.setCellStyle(cs);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));
        ///// TABLE HEAD 
        Row tablHead = sheet.createRow(3);
        Cell patName = tablHead.createCell(0, CellType.STRING);
        patName.setCellValue("No");
        patName.setCellStyle(header);
        Cell patId = tablHead.createCell(1, CellType.STRING);
        patId.setCellValue(" Drug Name ");
        patId.setCellStyle(header);
        Cell doctorname = tablHead.createCell(2, CellType.STRING);
        doctorname.setCellValue(" Total dose in(mg)");
        doctorname.setCellStyle(header);
        Cell date = tablHead.createCell(3, CellType.STRING);
        date.setCellValue("No of vials");
        date.setCellStyle(header);
        int rowCount = 4;
        int columnCount = 0;
        for (Drug drug : drugs) {
            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(columnCount++, CellType.STRING);
            Cell cel2 = row.createCell(columnCount++, CellType.STRING);
            Cell cel3 = row.createCell(columnCount++, CellType.STRING);
            Cell cel4 = row.createCell(columnCount++, CellType.STRING);
            cell.setCellValue(String.valueOf(drug.getNo()));
            cel2.setCellValue((String) drug.getName());
            cel3.setCellValue(String.valueOf(drug.getStock()));
            cel4.setCellValue(String.valueOf(drug.getNoOfVials()));
            cell.setCellStyle(cs);
            cel2.setCellStyle(cs);
            cel3.setCellStyle(cs);
            cel4.setCellStyle(cs);
            columnCount = 0;
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);

        try (FileOutputStream outputStream = new FileOutputStream(file.getPath() + ".xlsx")) {
            workbook.write(outputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewExcelExport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NewExcelExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ObservableList<VisitData> getVisitData(ObservableList<VisitData> data, int selected) {
        ObservableList<VisitData> res = FXCollections.observableArrayList();
        if (selected == 0) {
            res = FXCollections.observableArrayList(data);
        } else {
            for (VisitData visit : data) {
                if (selected == visit.getCategory()) {
                    res.add(visit);
                } else if (selected == Drug.CHEMOTHERAPY && visit.getCategory() == Drug.FLUID) {
                    res.add(visit);
                } else if (selected == Drug.SUPPORTIVE && visit.getCategory() == Drug.FLUID) {
                    res.add(visit);
                }
            }
        }
        return res;
    }

    private boolean isHaveSelectedGroup(ArrayList<LabVisit> labDetalis, int selected) {
        for (LabVisit labDetali : labDetalis) {
            if (labDetali.getGroupId() == selected) {
                return true;
            }
        }
        return false;

    }
}
