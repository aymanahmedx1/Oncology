/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.Prescription;
import DAO.VisitData;
import DAO.patient.Patient;
import commons.DBConnection;
import commons.SelectStatment;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Ayman
 */
public class VisitManagement {

    public boolean addNewVisitDetails(ArrayList<VisitData> items) throws SQLException {
        try (Connection conn = DBConnection.createConnection();) {
            for (VisitData visitItem : items) {
                String sql = "INSERT INTO prescription_detail (drug,dose,fluid,volume,note,prescription_no,date,is_checked) VALUES (?,?,?,?,?,?,?,?);";
                PreparedStatement stmnt = conn.prepareStatement(sql);
                stmnt.setInt(1, visitItem.getDrugId());
                stmnt.setString(2, visitItem.getDose());
                stmnt.setInt(3, visitItem.getFluidId());
                stmnt.setInt(4, visitItem.getVolume());
                stmnt.setString(5, visitItem.getNote());
                stmnt.setInt(6, visitItem.getPrescription());
                stmnt.setDate(7, Date.valueOf(LocalDate.now()));
                stmnt.setInt(8, VisitData.bollToInt(visitItem.isIsChecked()));
                int result = stmnt.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
    }

    public boolean UpdateVisitDetails(ArrayList<VisitData> items) throws SQLException {
        try (Connection conn = DBConnection.createConnection();) {
            for (VisitData visitItem : items) {
                String sql = "update prescription_detail set drug=?,dose=?,fluid=?,volume=?,note=?,date=?,is_checked=? VALUES (?,?,?,?,?,?,?);";
                PreparedStatement stmnt = conn.prepareStatement(sql);
                stmnt.setInt(1, visitItem.getDrugId());
                stmnt.setString(2, visitItem.getDose());
                stmnt.setInt(3, visitItem.getFluidId());
                stmnt.setInt(4, visitItem.getVolume());
                stmnt.setString(5, visitItem.getNote());
                stmnt.setInt(6, visitItem.getPrescription());
                stmnt.setDate(7, Date.valueOf(LocalDate.now()));
                stmnt.setInt(8, VisitData.bollToInt(visitItem.isIsChecked()));
                int result = stmnt.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            throw new SQLException("Error ADD Order Item ");
        }
    }

    public ArrayList<VisitData> patientVisits(Prescription pres) throws SQLException {
        int no = 1;
        try (Connection conn = DBConnection.createConnection()) {
            String sql = "SELECT p.id,p.drug,d.drug_name,p.dose,p.fluid,f.drug_name,"
                    + "p.volume,p.note,p.is_checked , d.main_category ,p.black "
                    + "FROM prescription_detail as p "
                    + "join drugs as d on p.drug=d.id left  "
                    + "join drugs as f on p.fluid=f.id "
                    + "WHERE prescription_no =?";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, pres.getId());
            ResultSet resultSet = stmnt.executeQuery();
            ArrayList<VisitData> patVisits = new ArrayList<>();
            while (resultSet.next()) {
                VisitData visit = new VisitData();
                visit.setId(resultSet.getInt(1));
                visit.setDrugId(resultSet.getInt(2));
                visit.setDrugName(resultSet.getString(3));
                visit.setDose(resultSet.getString(4));
                visit.setFluidId(resultSet.getInt(5));
                visit.setFluidName(resultSet.getString(6));
                visit.setVolume(resultSet.getInt(7));
                visit.setNote(resultSet.getString(8));
                visit.setCheck(resultSet.getInt(9));
                visit.setCategory(resultSet.getInt(10));
                visit.setNo(no++);
                visit.setBlack(resultSet.getInt(11));
                patVisits.add(visit);
            }

            return patVisits;
        } catch (Exception e) {
            throw e;
        }
    }

    public ArrayList<VisitData> VisitsByDate(Patient pat, Date from, Date to) throws SQLException {
        int no = 1;
        try (Connection conn = DBConnection.createConnection()) {
            String colNames = "SELECT p.id,drug.drug_name,p.dose,fluid.drug_name,p.volume,p.note ,num.date , drug.main_category"
                    + " FROM prescription_detail as p ";
            String joins = "LEFT JOIN prescription_no as num  ON  p.prescription_no =  num.id "
                    + "left join drugs as drug on p.drug=drug.id"
                    + " LEFT join drugs as fluid on p.fluid=fluid.id ";
            String whereCloses = "WHERE num.patient_id = ?  AND p.date BETWEEN ? AND ?  order by num.date ;";
            String sql = colNames + joins + whereCloses;
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, pat.getId());
            stmnt.setDate(2, from);
            stmnt.setDate(3, to);
            ResultSet resultSet = stmnt.executeQuery();
            ArrayList<VisitData> patVisits = new ArrayList<>();
            while (resultSet.next()) {
                VisitData visit = new VisitData();
                visit.setId(resultSet.getInt(1));
//                visit.setDrugId(resultSet.getInt(2));
                visit.setDrugName(resultSet.getString(2));
                visit.setDose(resultSet.getString(3));
//                visit.setFluidId(resultSet.getInt(5));
                visit.setFluidName(resultSet.getString(4));
                visit.setVolume(resultSet.getInt(5));
                visit.setNote(resultSet.getString(6));
                visit.setDate(resultSet.getDate(7));
                visit.setNo(no++);
                visit.setCategory(resultSet.getInt(8));
                patVisits.add(visit);
            }

            return patVisits;
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    public ArrayList<VisitData> visitToPremed(Date from, Date to) throws SQLException {
        int no = 1;
        try (Connection conn = DBConnection.createConnection()) {
            String sql = SelectStatment.SELECT_STATMENT;
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setDate(1, from);
            stmnt.setDate(2, to);
            ResultSet resultSet = stmnt.executeQuery();
            ArrayList<VisitData> patVisits = new ArrayList<>();
            while (resultSet.next()) {
                VisitData vwp = new VisitData();
                vwp.setId(resultSet.getInt(1));
                vwp.setPatName(resultSet.getString(2));
                vwp.setPatId(resultSet.getInt(3));
                vwp.setDrugName(resultSet.getString(4));
                vwp.setDose(resultSet.getString(5));
                vwp.setFluidName(resultSet.getString(6));
                vwp.setVolume(resultSet.getInt(7));
                vwp.setNo(no++);
                vwp.setNote(resultSet.getString(8));
                vwp.setDate(resultSet.getDate(9));
                vwp.setCheck(resultSet.getInt(10));
                vwp.setCategory(resultSet.getInt(11));
                vwp.setPrescription(resultSet.getInt(12));
                patVisits.add(vwp);
            }

            return patVisits;
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    public void deleteVisitDetails(Prescription pres) throws SQLException {
        try (Connection conn = DBConnection.createConnection();) {
            String sql = "delete from  prescription_detail where prescription_no =?;";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, pres.getId());
            stmnt.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }

    }

    public void updateVisitItemState(VisitData visit) throws SQLException {
        try (Connection conn = DBConnection.createConnection();) {
            String sql = "UPDATE prescription_detail SET is_checked = ? WHERE id = ?;";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, visit.getCheck());
            stmnt.setInt(2, visit.getId());
            stmnt.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
    }

    public void updateVisitItemBlack(VisitData visit, int black) throws SQLException {
        try (Connection conn = DBConnection.createConnection();) {
            String sql = "UPDATE prescription_detail SET black = ? WHERE id = ?;";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, black);
            stmnt.setInt(2, visit.getId());
            stmnt.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
    }

}
