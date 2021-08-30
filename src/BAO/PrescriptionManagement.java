/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.Prescription;
import DAO.VisitData;
import DAO.patient.Patient;
import DAO.patient.PatientMovement;
import commons.DBConnection;
import commons.Helpers;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ayman
 */
public class PrescriptionManagement {

    private static Connection con;

    public PrescriptionManagement() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int addPrescription(Prescription pres, int AcceptState) throws SQLException {
        try {
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new java.util.Date());
            String sql = "INSERT INTO prescription_no(patient_id,date,user_id,accept_state,pharmacy_time)VALUES(?,?,?,?,?)";
            PreparedStatement stmnt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmnt.setInt(1, pres.getPatientId());
            stmnt.setDate(2, Date.valueOf(LocalDate.now()));
            stmnt.setInt(3, pres.getUser());
            stmnt.setInt(4, AcceptState);
            stmnt.setString(5, currentTime);
            int queryResult = stmnt.executeUpdate();
            ResultSet resultID;
            int result;
            resultID = stmnt.getGeneratedKeys();
            while (resultID.next()) {
                result = Integer.parseInt(resultID.getString(1));
                return result;
            }
        } catch (SQLException e) {
            throw new SQLException("Error Save Prescription");
        }
        throw new SQLException("Error Save Prescription");
    }

    public ArrayList<Prescription> patientVisits(Patient pat) throws SQLException {
        int no = 1;
        try {
            String sql = "SELECT * from prescription_no WHERE patient_id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pat.getId());
            ResultSet resultSet = stmnt.executeQuery();
            ArrayList<Prescription> patVisits = new ArrayList<>();
            while (resultSet.next()) {
                Prescription pres = new Prescription();
                pres.setId(resultSet.getInt(1));
                pres.setPatientId(pat.getId());
                pres.setDate(resultSet.getDate(3));
                pres.setUser(resultSet.getInt(4));
                pres.setNo(no++);
                patVisits.add(pres);
            }
            return patVisits;
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    public int getTodayPatient() throws SQLException {
        try {
            String sql = "SELECT count(*) FROM hospital.prescription_no where date=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet result = stmnt.executeQuery();
            int count = -1;
            while (result.next()) {
                count = result.getInt(1);
            }
            return count;
        } catch (SQLException e) {
            throw new SQLException("Error Get Patients");
        }
    }

    public boolean deletePrescription(Prescription pres) throws SQLException {
        try {
            String sql = " delete from prescription_no where id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pres.getId());
            int queryResult = stmnt.executeUpdate();
            if (queryResult > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new SQLException("Error Save Prescription");
        }
        return false;
    }

    public boolean deletePatientPrescription(Patient pat) throws SQLException {
        try {
            String sql = " delete from prescription_no where patient_id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pat.getId());
            int queryResult = stmnt.executeUpdate();
            if (queryResult > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new SQLException("Error Save Prescription");
        }
        return false;
    }

    public void updateVisitItem(VisitData visit) throws SQLException {
        try {
            String sql = " update prescription_detail set is_checked = ? where id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, visit.getCheck());
            stmnt.setInt(2, visit.getId());
            stmnt.executeUpdate();
        } catch (Exception e) {
        }

    }

    public ArrayList<Prescription> patientVisits(PatientMovement pat, Date date) throws SQLException {
        int no = 1;
        try {
            String sql = "SELECT * from prescription_no WHERE patient_id=? and date = ?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pat.getPatient().getId());
            stmnt.setDate(2, date);
            ResultSet resultSet = stmnt.executeQuery();
            ArrayList<Prescription> patVisits = new ArrayList<>();
            while (resultSet.next()) {
                Prescription pres = new Prescription();
                pres.setId(resultSet.getInt(1));
                pres.setPatientId(pat.getId());
                pres.setDate(resultSet.getDate(3));
                pres.setUser(resultSet.getInt(4));
                pres.setChecked(resultSet.getInt(8));
                pres.setNo(no++);
                patVisits.add(pres);
            }
            return patVisits;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SQLException();
        }
    }

    public ArrayList<Prescription> allPresForDrugAndPharmacy(int acceptState, Date from, Date to, String orderColName) throws SQLException {
        int no = 1;
        try {
            String sql = "SELECT pres.id , pat.id,pat.pat_id,pat.name,pat.birth"
                    + ",pres.date,pres.user_id , u.name , pres.is_check \n"
                    + "from prescription_no as pres\n"
                    + "JOIN patient as pat on pres.patient_id = pat.id\n"
                    + "JOIN users as u on u.id = pres.user_id WHERE pres.accept_state = ? and pres.date between ? and ?  order by pres." + orderColName;
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, acceptState);
            stmnt.setDate(2, from);
            stmnt.setDate(3, to);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<Prescription> patVisits = new ArrayList<>();
            while (rs.next()) {
                Prescription pres = new Prescription();
                pres.setId(rs.getInt(1));
                pres.setPatientId(rs.getInt(2));
                pres.setPatientNumber(rs.getString(3));
                pres.setPatientName(rs.getString(4));
                int age = Helpers.calculateAge(rs.getDate(5).toLocalDate());
                if (age != 0) {
                    pres.setAge(age);
                }
                pres.setDate(rs.getDate(6));
                pres.setUser(rs.getInt(7));
                pres.setDoctorName(rs.getString(8));
                pres.setNo(no++);
                pres.setChecked(rs.getInt(9));
                patVisits.add(pres);
            }
            return patVisits;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SQLException();
        }
    }

    public ArrayList<Prescription> allPresForDrugAndPharmacy(Date from, Date to, String orderColName) throws SQLException {
        int no = 1;
        try {
            String sql = "SELECT pres.id , pat.id,pat.pat_id,pat.name,pat.birth"
                    + ",pres.date,pres.user_id , u.name , pres.is_check , pres.accept_state\n"
                    + "from prescription_no as pres\n"
                    + "JOIN patient as pat on pres.patient_id = pat.id\n"
                    + "JOIN users as u on u.id = pres.user_id WHERE pres.date between ? and ?  order by pres." + orderColName;
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, from);
            stmnt.setDate(2, to);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<Prescription> patVisits = new ArrayList<>();
            while (rs.next()) {
                Prescription pres = new Prescription();
                pres.setId(rs.getInt(1));
                pres.setPatientId(rs.getInt(2));
                pres.setPatientNumber(rs.getString(3));
                pres.setPatientName(rs.getString(4));
                int age = Helpers.calculateAge(rs.getDate(5).toLocalDate());
                if (age != 0) {
                    pres.setAge(age);
                }
                pres.setDate(rs.getDate(6));
                pres.setUser(rs.getInt(7));
                pres.setDoctorName(rs.getString(8));
                pres.setNo(no++);
                pres.setChecked(rs.getInt(9));
                pres.setAcceptState(rs.getInt(10));
                patVisits.add(pres);
            }
            return patVisits;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SQLException();
        }
    }

    public void updatePrescriptionState(Prescription pres, int PresState, String orderColName) throws SQLException {
        try {
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new java.util.Date());
            String sql = "UPDATE prescription_no set accept_state = ? , date = ? , " + orderColName + " =? WHERE id = ?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, PresState);
            stmnt.setDate(2, Date.valueOf(LocalDate.now()));
            stmnt.setString(3, currentTime);
            stmnt.setInt(4, pres.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public void updatePrescriptionState(int presID, int PresState) throws SQLException {
        try {
            String sql = "UPDATE prescription_no set black = ? WHERE id = ?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, PresState);
            stmnt.setInt(2, presID);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean isPatHasPrescToday(int patId) {
        try {
            String sql = "SELECT * FROM oncology.prescription_no where patient_id = ? and date = ?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, patId);
            stmnt.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int getPresStateCount(int state) {
        try {
            String sql = "SELECT count(*) FROM oncology.prescription_no where accept_state = ? and prescription_no.date = ?;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, state);
            stmnt.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmnt.executeQuery();
            int result;
            while (rs.next()) {
                result = rs.getInt(1);
                return result;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public boolean deletePatientVisit(Prescription pres) throws SQLException {
        try {
            String sql = " delete from prescription_detail where prescription_no =?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pres.getId());
            stmnt.executeUpdate();
            String sql2 = " delete from prescription_no where id =?";
            PreparedStatement stmnt2 = con.prepareStatement(sql2);
            stmnt2.setInt(1, pres.getId());
            stmnt2.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Save Prescription");
        }
        return false;
    }

    public List<Integer> getPresCategories(Prescription pres) throws SQLException {
        try {
            List<Integer> li = new ArrayList<>();
            String sql = " SELECT  distinct drugs.main_category FROM oncology.prescription_detail as pres\n"
                    + "join drugs on drugs.id = pres.drug\n"
                    + "join drug_cat on drug_cat.id = drugs.main_category\n"
                    + " where pres.prescription_no = ? ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pres.getId());
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                li.add(rs.getInt(1));
            }
            return li;
        } catch (SQLException e) {
            throw new SQLException("Error Save Prescription");
        }
    }

    public void updatePresItemState(Prescription visit, String colName) throws SQLException {
        try (Connection conn = DBConnection.createConnection();) {
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new java.util.Date());
            String sql = "UPDATE prescription_no SET is_check = ? , " + colName + " =? WHERE id = ?;";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, visit.getChecked());
            stmnt.setString(2, currentTime);
            stmnt.setInt(3, visit.getId());
            stmnt.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
    }

    public ArrayList<Prescription> allPresFromDateToDate(Date from, Date to) throws SQLException {
        try {
            String sql = "SELECT pres.id ,pres.date, pres.is_check \n"
                    + "from prescription_no as pres\n"
                    + "JOIN patient as pat on pres.patient_id = pat.id\n"
                    + "JOIN users as u on u.id = pres.user_id WHERE pres.date between ? and ?  ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, from);
            stmnt.setDate(2, to);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<Prescription> patVisits = new ArrayList<>();
            while (rs.next()) {
                Prescription pres = new Prescription();
                pres.setId(rs.getInt(1));
                pres.setDate(rs.getDate(2));
                pres.setChecked(rs.getInt(3));
                patVisits.add(pres);
            }
            return patVisits;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SQLException();
        }
    }

    public ArrayList<Prescription> AllPrescription(Date from, Date to) throws SQLException {
        int no = 1;
        try {
            String sql = "SELECT pres.id , pat.id,pat.pat_id,pat.name,pat.birth"
                    + ",pres.date,pres.user_id , u.name , pres.is_check \n"
                    + "from prescription_no as pres\n"
                    + "JOIN patient as pat on pres.patient_id = pat.id\n"
                    + "JOIN users as u on u.id = pres.user_id where  pres.date between ? and ? and pres.black = ? ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, from);
            stmnt.setDate(2, to);
            stmnt.setInt(3, Patient.BLACK_LIST);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<Prescription> patVisits = new ArrayList<>();
            while (rs.next()) {
                Prescription pres = new Prescription();
                pres.setId(rs.getInt(1));
                pres.setPatientId(rs.getInt(2));
                pres.setPatientNumber(rs.getString(3));
                pres.setPatientName(rs.getString(4));
                int age = Helpers.calculateAge(rs.getDate(5).toLocalDate());
                if (age != 0) {
                    pres.setAge(age);
                }
                pres.setDate(rs.getDate(6));
                pres.setUser(rs.getInt(7));
                pres.setDoctorName(rs.getString(8));
                pres.setNo(no++);
                pres.setChecked(rs.getInt(9));
                patVisits.add(pres);
            }
            return patVisits;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SQLException();
        }
    }

    public int getPatientLastPrescriptionID(Patient pat) throws SQLException {
        try {
            String sql = "SELECT max(id) from prescription_no WHERE patient_id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pat.getId());
            ResultSet resultSet = stmnt.executeQuery();
            int result = -1;
            while (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            return result;
        } catch (SQLException e) {
            throw e;
        }

    }
}
