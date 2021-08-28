/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.CallItem;
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
public class CallManage {

    private Connection con;

    public CallManage() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addDoctorCall(CallItem item) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.doctor_screen (pat_id, doctor_id, status , date ) VALUES (?, ?, ?, ?);";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, item.getPatId());
            stmnt.setInt(2, item.getDoctorId());
            stmnt.setInt(3, item.getState());
            stmnt.setDate(4, Date.valueOf(LocalDate.now()));
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void deleteDoctorCall(CallItem item) throws SQLException {
        try {
            String sql = "DELETE FROM oncology.doctor_screen where doctor_id = ? and status =? ;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, item.getDoctorId());
            stmnt.setInt(2, CallItem.GO_To_DOCTOR);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void deleteCemoCall(CallItem item) throws SQLException {
        try {
            String sql = "DELETE FROM oncology.doctor_screen where pat_id = ? and status =?;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, item.getPatId());
            stmnt.setInt(2, CallItem.GO_To_CEMO);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
    public void deleteAllCemoCall() throws SQLException {
        try {
            String sql = "DELETE FROM oncology.doctor_screen where status =?;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, CallItem.GO_To_CEMO);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public ArrayList<CallItem> allDoctorsCall() throws SQLException {
        try {
            String sql = "SELECT screen.id ,patient.name,patient.pat_id,doctor.id,doctor.name ,screen.status , "
                    + "patient.id FROM oncology.doctor_screen as screen\n"
                    + "join patient on patient.id = screen.pat_id\n"
                    + "join oncology.users as doctor on doctor.id = screen.doctor_id "
                    + " where screen.date = ? ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmnt.executeQuery();
            ArrayList<CallItem> found = new ArrayList<>();
            while (rs.next()) {
                CallItem item = new CallItem();
                item.setId(rs.getInt(1));
                item.setPatName(rs.getString(2));
                item.setPatFile(rs.getString(3));
                item.setDoctorId(rs.getInt(4));
                item.setDoctorName(rs.getString(5));
                item.setState(rs.getInt(6));
                item.setPatId(rs.getInt(7));
                found.add(item);
            }
            return found;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public ArrayList<CallItem> allDoctorsCall(int state) throws SQLException {
        try {
            String sql = "SELECT screen.id ,patient.name,patient.pat_id,doctor.id,doctor.name ,screen.status , "
                    + "patient.id FROM oncology.doctor_screen as screen\n"
                    + "join patient on patient.id = screen.pat_id\n"
                    + "join oncology.users as doctor on doctor.id = screen.doctor_id "
                    + " where screen.date = ? and screen.status=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, Date.valueOf(LocalDate.now()));
            stmnt.setInt(2, state);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<CallItem> found = new ArrayList<>();
            while (rs.next()) {
                CallItem item = new CallItem();
                item.setId(rs.getInt(1));
                item.setPatName(rs.getString(2));
                item.setPatFile(rs.getString(3));
                item.setDoctorId(rs.getInt(4));
                item.setDoctorName(rs.getString(5));
                item.setState(rs.getInt(6));
                item.setPatId(rs.getInt(7));
                found.add(item);
            }
            return found;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public ArrayList<CallItem> getLabCall() {
        try {
            String sql = "SELECT screen.id ,patient.name,patient.pat_id,screen.call \n"
                    + "  FROM oncology.lab_order as screen\n"
                    + " join patient on patient.id = screen.pat_id\n"
                    + "WHERE screen.date = ?"
                    + "  order by screen.calledTime ;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, Date.valueOf(LocalDate.now()));
            ArrayList<CallItem> allItems = new ArrayList<>();
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                CallItem rg = new CallItem();
                rg.setId(rs.getInt(1));
                rg.setPatName(rs.getString(2));
                rg.setPatFile(rs.getString(3));
                rg.setState(rs.getInt(4));
                allItems.add(rg);
            }
            return allItems;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
