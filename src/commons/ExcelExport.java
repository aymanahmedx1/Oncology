/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

import DAO.Drug;
import DAO.Prescription;
import DAO.VisitData;
import static commons.Helpers.VISIT_MANAGE;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Border;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author DELL
 */
public class ExcelExport {

    public void exportDrugFromPrepare(Stage stage, ArrayList<Prescription> pres, LocalDate from, LocalDate to, int drugSelected, String catName) throws SQLException {
        try {
            File se = fileName(stage);
            if (se == null) {
                return;
            }
            WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, true);
            WritableCellFormat titleformat = new WritableCellFormat(titleFont);
            titleformat.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat.setAlignment(Alignment.CENTRE);
            titleformat.setBackground(Colour.YELLOW2);
            titleformat.setBorder(Border.ALL, BorderLineStyle.DOUBLE);
            WritableFont contentFont = new WritableFont(WritableFont.ARIAL, 14, WritableFont.NO_BOLD, false);
            WritableCellFormat contentFormat = new WritableCellFormat(contentFont);
            contentFormat.setAlignment(Alignment.CENTRE);
            contentFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            contentFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            WritableFont sepratorRow = new WritableFont(WritableFont.ARIAL, 14, WritableFont.NO_BOLD, false);
            WritableCellFormat sepratorFormat = new WritableCellFormat(sepratorRow);
            sepratorFormat.setBackground(Colour.GREY_25_PERCENT);
            WritableWorkbook workbook = Workbook.createWorkbook(new File(se.getPath() + ".xls"));
            WritableSheet sheet = workbook.createSheet("Forward lookups", 0);
            sheet.setColumnView(1, 30);
            sheet.setColumnView(2, 15);
            sheet.setColumnView(3, 30);
            sheet.setColumnView(7, 50);

            sheet.addCell(new Label(0, 0, catName, titleformat));
            sheet.mergeCells(0, 0, 7, 0);
           
            sheet.addCell(new Label(0, 1, " From Date  ", contentFormat));
            sheet.addCell(new Label(2, 1, from.toString(), contentFormat));
            sheet.addCell(new Label(4, 1, "  To Date ", contentFormat));
            sheet.addCell(new Label(6, 1, to.toString(), contentFormat));
            sheet.mergeCells(0, 1, 1, 1);//
            sheet.mergeCells(2, 1, 3, 1);//
            sheet.mergeCells(4, 1, 5, 1);
            sheet.mergeCells(6, 1, 7, 1);
            sheet.addCell(new Label(0, 2, " No", titleformat));
            sheet.addCell(new Label(1, 2, " Patient Name", titleformat));
            sheet.addCell(new Label(2, 2, " ID", titleformat));
            sheet.addCell(new Label(3, 2, " Drug", titleformat));
            sheet.addCell(new Label(4, 2, " Dose", titleformat));
            sheet.addCell(new Label(5, 2, " Fluid", titleformat));
            sheet.addCell(new Label(6, 2, " Vol", titleformat));
            sheet.addCell(new Label(7, 2, " Note", titleformat));
            int nextRow = 3;
            int startRowNo;
            int endRowrNo;
            for (Prescription res : pres) {
                startRowNo = nextRow;
                sheet.addCell(new Label(0, nextRow, String.valueOf(res.getNo()), contentFormat));
                sheet.addCell(new Label(1, nextRow, res.getPatientName(), contentFormat));
                sheet.addCell(new Label(2, nextRow, res.getPatientNumber(), contentFormat));
                ObservableList<VisitData> temp = FXCollections.observableArrayList(VISIT_MANAGE.patientVisits(res));
                ObservableList<VisitData> visitDetalis = getVisitData(temp, drugSelected);
                for (VisitData detail : visitDetalis) {
                    sheet.addCell(new Label(3, nextRow, detail.getDrugName(), contentFormat));
                    sheet.addCell(new Label(4, nextRow, detail.getDose(), contentFormat));
                    sheet.addCell(new Label(5, nextRow, detail.getFluidName(), contentFormat));
                    sheet.addCell(new Label(6, nextRow, String.valueOf(detail.getVolume()), contentFormat));
                    sheet.addCell(new Label(7, nextRow, detail.getNote(), contentFormat));
                    nextRow++;
                }
                endRowrNo = nextRow - 1;
                sheet.mergeCells(0, startRowNo, 0, endRowrNo);
                sheet.mergeCells(1, startRowNo, 1, endRowrNo);
                sheet.mergeCells(2, startRowNo, 2, endRowrNo);
                sheet.addCell(new Label(0, nextRow, null, sepratorFormat));
                sheet.addCell(new Label(1, nextRow, null, sepratorFormat));
                sheet.addCell(new Label(2, nextRow, null, sepratorFormat));
                sheet.addCell(new Label(3, nextRow, null, sepratorFormat));
                sheet.addCell(new Label(4, nextRow, null, sepratorFormat));
                sheet.addCell(new Label(5, nextRow, null, sepratorFormat));
                sheet.addCell(new Label(6, nextRow, null, sepratorFormat));
                sheet.addCell(new Label(7, nextRow, null, sepratorFormat));
                nextRow++;
            }
            workbook.write();
            workbook.close();
        } catch (WriteException ex) {
            Logger.getLogger("resultExport.ExportForwardLookupsToXLS").log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private File fileName(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "desktop"));
//        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XLS files", "xls"));
        File file = fileChooser.showSaveDialog(stage);
        return file;
    }

    public void ExportDrugDose(Stage stage, Drug drug, LocalDate from, LocalDate to) {
        try {
            File se = fileName(stage);
            if (se == null) {
                return;
            }
            WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, true);
            WritableCellFormat titleformat = new WritableCellFormat(titleFont);
            titleformat.setVerticalAlignment(VerticalAlignment.CENTRE);
            titleformat.setAlignment(Alignment.CENTRE);
            titleformat.setBackground(Colour.YELLOW2);
            titleformat.setBorder(Border.ALL, BorderLineStyle.DOUBLE);
            WritableFont contentFont = new WritableFont(WritableFont.ARIAL, 14, WritableFont.NO_BOLD, false);
            WritableCellFormat contentFormat = new WritableCellFormat(contentFont);
            contentFormat.setAlignment(Alignment.CENTRE);
            contentFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            contentFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            WritableWorkbook workbook = Workbook.createWorkbook(new File(se.getPath() + ".xls"));
            WritableSheet sheet = workbook.createSheet("Forward lookups", 0);
            sheet.setColumnView(0, 40);
            sheet.setColumnView(1, 20);
            sheet.setColumnView(2, 20);
            sheet.addCell(new Label(0, 0, " Serach For Drug ", titleformat));
            sheet.mergeCells(0, 0, 3, 0);
            sheet.addCell(new Label(0, 1, " From Date  ", contentFormat));
            sheet.addCell(new Label(1, 1, from.toString(), contentFormat));
            sheet.addCell(new Label(2, 1, "  To Date ", contentFormat));
            sheet.addCell(new Label(3, 1, to.toString(), contentFormat));
            sheet.addCell(new Label(0, 2, " Drug name ", titleformat));
            sheet.addCell(new Label(1, 2, " Total dose (in mg)", titleformat));
            sheet.addCell(new Label(2, 2, " No.of Vials", titleformat));

            sheet.addCell(new Label(0, 3, drug.getName(), contentFormat));
            sheet.addCell(new Label(1, 3, String.valueOf(drug.getStock()), contentFormat));
            sheet.addCell(new Label(2, 3, String.valueOf(drug.getFloatOfVials()), contentFormat));

            workbook.write();
            workbook.close();
        } catch (WriteException ex) {
            Logger.getLogger("resultExport.ExportForwardLookupsToXLS").log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelExport.class.getName()).log(Level.SEVERE, null, ex);
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
}
