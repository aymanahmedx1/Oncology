/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.CallItem;
import DAO.Prescription;
import commons.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class PhCallManage {

    private Connection con;

    public PhCallManage() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<CallItem> allPatientCall() throws SQLException {
        try {
            String sql = "SELECT screen.id , patient.name,patient.pat_id FROM prescription_no as screen\n"
                    + "join patient on patient.id = screen.patient_id\n"
                    + "where screen.is_check = 1 and screen.date =? order by "+ Prescription.SCREEN_TIME;
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmnt.executeQuery();
            ArrayList<CallItem> found = new ArrayList<>();
            while (rs.next()) {
                CallItem item = new CallItem();
                item.setId(rs.getInt(1));
                item.setPatName(rs.getString(2));
                item.setPatFile(rs.getString(3));
                found.add(item);
            }
            return found;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

}
