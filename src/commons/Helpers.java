/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

import BAO.LabGroupBAO;
import BAO.LabOrderManage;
import BAO.PrescriptionManagement;
import BAO.VisitManagement;
import BAO.labTestManage;
import BAO.patient.MovmentManage;
import BAO.patient.PatientManage;
import BAO.user.UserManagment;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Ayman
 */
public class Helpers {

    public static final String SERVER_FILE_PATH = "d:\\";
    public static final UserManagment USER = new UserManagment();
    public static final PatientManage PAT_MANAGE = new PatientManage();
    public static final MovmentManage MOVEMENT = new MovmentManage();
    public static final LabOrderManage LAB_MANAGE = new LabOrderManage();
    public static final labTestManage LAB_TEST_MANAGE = new labTestManage();
    public static final PrescriptionManagement PRES_MANAGE = new PrescriptionManagement();
    public static final VisitManagement VISIT_MANAGE = new VisitManagement();
    public static final LabGroupBAO LAB_GROUP_MANAGE = new LabGroupBAO();

    public static int calculateAge(LocalDate birthDate) {
        if ((birthDate != null)) {
            return Period.between(birthDate, LocalDate.now()).getYears();
        } else {
            return 0;
        }
    }

    public static void execRaport() {
        try {
            Connection con = DBConnection.createConnection();

            JasperReport jasperReport
                    = (JasperReport) JRLoader.loadObjectFromFile("E:\\15-6-2021-ayman-java\\Oncology\\src\\report\\AllDrugReport.jasper");

            Map parameters = new HashMap();
            // Fill the Jasper Report
            JasperPrint jasperPrint
                    = JasperFillManager.fillReport(jasperReport, parameters, con);

            // Creation of the HTML Jasper Reports
            JasperExportManager.exportReportToPdfFile(jasperPrint, "MyJasperReport.pdf");

        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(Helpers.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
