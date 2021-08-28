/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO.patient;

import DAO.Diagnosis;
import DAO.patient.Patient;
import DAO.patient.PatientMovement;
import DAO.user.User;
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
 * @author Ayman
 */
public class MovmentManage {

    private Connection connection;

    public MovmentManage() {
        try {
            connection = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(MovmentManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addMovment(PatientMovement pat) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.movments(service_type,pat_id,doctor,date)VALUES(?,?,?,?) ; ";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, pat.getServiceType());
            stmnt.setInt(2, pat.getPatient().getId());
            stmnt.setInt(3, pat.getDoctor());
            stmnt.setDate(4, Date.valueOf(LocalDate.now()));
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

//    public ArrayList<PatientMovement> getDoctorPatient(User user, int finish) throws SQLException {
//        try {
//            String sql = " SELECT m.id,m.service_type,m.pat_id,p.name,p.pat_id,d.id,d.name\n"
//                    + "FROM oncology.movments as m\n"
//                    + "LEFT join patient as p on m.pat_id = p.id\n"
//                    + "LEFT join diagnosis as  d on p.diagnosis = d.id\n"
//                    + "where m.doctor = ? and date = ? and finish = ?  ";
//            PreparedStatement stmnt = connection.prepareStatement(sql);
//            stmnt.setInt(1, user.getId());
//            stmnt.setDate(2, Date.valueOf(LocalDate.now()));
//            stmnt.setInt(3, finish);
//            ResultSet rs = stmnt.executeQuery();
//            ArrayList<PatientMovement> allMovments = new ArrayList<>();
//            while (rs.next()) {
//                PatientMovement pm = new PatientMovement();
//                pm.setId(rs.getInt(1));
//                Patient patient = new Patient();
//                patient.setId(rs.getInt(3));
//                patient.setName(rs.getString(4));
//                patient.setPatId(rs.getString(5));
//                Diagnosis diagnosis = new Diagnosis();
//                diagnosis.setId(rs.getInt(6));
//                diagnosis.setName(rs.getString(7));
//                patient.setDiagnosis(diagnosis);
//                pm.setPatient(patient);
//                pm.setServiceType(rs.getInt(2));
//                allMovments.add(pm);
//            }
//            return allMovments;
//        } catch (SQLException e) {
//            throw new SQLException("Error get Data");
//        }
//
//    }
    public ArrayList<PatientMovement> getDoctorPatient(User user, Date from, Date to) throws SQLException {
        try {
            String sql = " SELECT m.id,m.service_type,m.pat_id,p.name,p.pat_id,d.id,d.name ,"
                    + " m.finish , p.surface,p.weight ,p.birth , m.date\n"
                    + "FROM oncology.movments as m\n"
                    + "LEFT join patient as p on m.pat_id = p.id\n"
                    + "LEFT join diagnosis as  d on p.diagnosis = d.id\n"
                    + "where m.doctor = ? and date between ? and ? ";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, user.getId());
            stmnt.setDate(2, from);
            stmnt.setDate(3, to);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<PatientMovement> allMovments = new ArrayList<>();
            while (rs.next()) {
                PatientMovement pm = new PatientMovement();
                pm.setId(rs.getInt(1));
                Patient patient = new Patient();
                patient.setId(rs.getInt(3));
                patient.setName(rs.getString(4));
                patient.setPatId(rs.getString(5));
                Diagnosis diagnosis = new Diagnosis();
                diagnosis.setId(rs.getInt(6));
                diagnosis.setName(rs.getString(7));
                patient.setDiagnosis(diagnosis);
                patient.setSurface(rs.getFloat(9));
                patient.setWeight(rs.getFloat(10));
                patient.setBirth(rs.getDate(11));
                pm.setPatient(patient);
                pm.setServiceType(rs.getInt(2));
                pm.setFinish(rs.getInt(8));
                pm.setDate(rs.getDate(12));
                allMovments.add(pm);
            }
            return allMovments;
        } catch (SQLException e) {
            throw new SQLException("Error get Data");
        }

    }

    public void updateMovement(PatientMovement pat, int state, int serviceType) throws SQLException {
        try {
            String sql = "UPDATE movments set  finish = ?  , service_type = ? where id = ?; ";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, state);
            stmnt.setInt(2, serviceType);
            stmnt.setInt(3, pat.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public int getDoctorFinishCount(User user, int state) throws SQLException {
        try {
            String sql = " SELECT count(*) FROM oncology.movments \n"
                    + "where doctor = ? and date = ? and finish = ?  ";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, user.getId());
            stmnt.setDate(2, Date.valueOf(LocalDate.now()));
            stmnt.setInt(3, state);
            ResultSet rs = stmnt.executeQuery();
            int result = -1;
            while (rs.next()) {
                result = rs.getInt(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException("Error get Data");
        }
    }

    public ArrayList<PatientMovement> getDoctorFinish(User user, Date from, Date to, int status) throws SQLException {
        try {
            String sql = " SELECT m.id,m.service_type,m.pat_id,p.name,p.pat_id,d.id,d.name,m.date, m.finish,p.surface,p.weight ,p.birth \n"
                    + "FROM oncology.movments as m\n"
                    + "LEFT join patient as p on m.pat_id = p.id\n"
                    + "LEFT join diagnosis as  d on p.diagnosis = d.id\n"
                    + "where m.doctor = ? and finish = ? and date between ? and ? order by p.name ";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, user.getId());
            stmnt.setInt(2, status);
            stmnt.setDate(3, from);
            stmnt.setDate(4, to);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<PatientMovement> allMovments = new ArrayList<>();
            while (rs.next()) {
                PatientMovement pm = new PatientMovement();
                pm.setId(rs.getInt(1));
                Patient patient = new Patient();
                patient.setId(rs.getInt(3));
                patient.setName(rs.getString(4));
                patient.setPatId(rs.getString(5));
                Diagnosis diagnosis = new Diagnosis();
                diagnosis.setId(rs.getInt(6));
                diagnosis.setName(rs.getString(7));
                patient.setDiagnosis(diagnosis);
                patient.setSurface(rs.getFloat(10));
                patient.setWeight(rs.getFloat(11));
                patient.setBirth(rs.getDate(12));
                pm.setPatient(patient);
                pm.setServiceType(rs.getInt(2));
                pm.setDate(rs.getDate(8));
                pm.setFinish(rs.getInt(9));
                allMovments.add(pm);
            }
            return allMovments;
        } catch (SQLException e) {
            throw new SQLException("Error get Data");
        }
    }

    public boolean checkIfPatHaveVisit(Patient pat) throws SQLException {
        try {
            String sql = " SELECT count(*) FROM oncology.movments where pat_id = ? and date = ?; ";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, pat.getId());
            stmnt.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmnt.executeQuery();
            boolean result = false;
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    result = true;
                    break;
                } else {
                    result = false;
                    break;
                }
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException("Error Add Data");
        }
    }

    public ArrayList<PatientMovement> getPatientMovement(PatientMovement pat, User user) throws SQLException {
        try {
            String sql = " SELECT m.id,m.service_type,m.pat_id,p.name,p.pat_id,d.id,d.name,m.date , p.surface,p.weight\n"
                    + "FROM oncology.movments as m\n"
                    + "LEFT join patient as p on m.pat_id = p.id\n"
                    + "LEFT join diagnosis as  d on p.diagnosis = d.id\n"
                    + "where m.pat_id =?  and m.doctor=? \n"
                    + " group by date order by date asc";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, pat.getPatient().getId());
            stmnt.setInt(2, user.getId());
            ResultSet rs = stmnt.executeQuery();
            ArrayList<PatientMovement> allMovments = new ArrayList<>();
            while (rs.next()) {
                PatientMovement pm = new PatientMovement();
                pm.setId(rs.getInt(1));
                Patient patient = new Patient();
                patient.setId(rs.getInt(3));
                patient.setName(rs.getString(4));
                patient.setPatId(rs.getString(5));
                Diagnosis diagnosis = new Diagnosis();
                diagnosis.setId(rs.getInt(6));
                diagnosis.setName(rs.getString(7));
                patient.setDiagnosis(diagnosis);
                pm.setPatient(patient);
                pm.setServiceType(rs.getInt(2));
                pm.setDate(rs.getDate(8));
                patient.setSurface(rs.getFloat(9));
                patient.setWeight(rs.getFloat(10));
                allMovments.add(pm);
            }
            return allMovments;
        } catch (SQLException e) {
            throw new SQLException("Error get Data");
        }
    }

    public ArrayList<PatientMovement> getPatientMovement(PatientMovement pat) throws SQLException {
        try {
            String sql = " SELECT m.id,m.service_type,m.pat_id,p.name,p.pat_id,d.id,d.name,m.date , p.surface,p.weight\n"
                    + "FROM oncology.movments as m\n"
                    + "LEFT join patient as p on m.pat_id = p.id\n"
                    + "LEFT join diagnosis as  d on p.diagnosis = d.id\n"
                    + "where m.pat_id =? \n"
                    + " group by date order by date asc";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, pat.getPatient().getId());
            ResultSet rs = stmnt.executeQuery();
            ArrayList<PatientMovement> allMovments = new ArrayList<>();
            while (rs.next()) {
                PatientMovement pm = new PatientMovement();
                pm.setId(rs.getInt(1));
                Patient patient = new Patient();
                patient.setId(rs.getInt(3));
                patient.setName(rs.getString(4));
                patient.setPatId(rs.getString(5));
                Diagnosis diagnosis = new Diagnosis();
                diagnosis.setId(rs.getInt(6));
                diagnosis.setName(rs.getString(7));
                patient.setDiagnosis(diagnosis);
                pm.setPatient(patient);
                pm.setServiceType(rs.getInt(2));
                pm.setDate(rs.getDate(8));
                patient.setSurface(rs.getFloat(9));
                patient.setWeight(rs.getFloat(10));
                allMovments.add(pm);
            }
            return allMovments;
        } catch (SQLException e) {
            throw new SQLException("Error get Data");
        }
    }

    public boolean checkPatDrug(PatientMovement visit) {
        try {
            String sql = "SELECT * FROM oncology.prescription_no where patient_id = ? and date = ?";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, visit.getPatient().getId());
            stmnt.setDate(2, visit.getDate());
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

    public boolean checkPatLab(PatientMovement visit) {
        try {
            String sql2 = "SELECT * FROM oncology.lab_order where pat_id = ? and date = ?";
            PreparedStatement stmnt2 = connection.prepareStatement(sql2);
            stmnt2.setInt(1, visit.getPatient().getId());
            stmnt2.setDate(2, visit.getDate());
            ResultSet rs2 = stmnt2.executeQuery();
            while (rs2.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public PatientMovement getPatientFile(int user) throws SQLException {
        PatientMovement pm = new PatientMovement();
        try {
            String sql = " SELECT m.id,m.service_type,m.pat_id,p.name,p.pat_id,d.id,d.name ,"
                    + " m.finish , p.surface,p.weight ,p.birth\n"
                    + "FROM oncology.movments as m\n"
                    + "LEFT join patient as p on m.pat_id = p.id\n"
                    + "LEFT join diagnosis as  d on p.diagnosis = d.id\n"
                    + "where m.pat_id = ? limit 1";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, user);
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                pm.setId(rs.getInt(1));
                Patient patient = new Patient();
                patient.setId(rs.getInt(3));
                patient.setName(rs.getString(4));
                patient.setPatId(rs.getString(5));
                Diagnosis diagnosis = new Diagnosis();
                diagnosis.setId(rs.getInt(6));
                diagnosis.setName(rs.getString(7));
                patient.setDiagnosis(diagnosis);
                patient.setSurface(rs.getFloat(9));
                patient.setWeight(rs.getFloat(10));
                patient.setBirth(rs.getDate(11));
                pm.setPatient(patient);
                pm.setServiceType(rs.getInt(2));
                pm.setFinish(rs.getInt(8));
                return pm;
            }
        } catch (SQLException e) {
            throw e;
        }
        System.out.println("before throw");
        return null;
    }

    public void updateMovementByDate(int patId, Date date, int state, int serviceType) throws SQLException {
        try {
            String sql = "UPDATE movments set  finish = ?  , service_type = ? "
                    + " where pat_id = ? and date = ? ; ";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, state);
            stmnt.setInt(2, serviceType);
            stmnt.setInt(3, patId);
            stmnt.setDate(4, date);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void updateMoveDate(int patId, Date date) throws SQLException {
        try {
            String sql = "UPDATE movments set date = ? "
                    + " where pat_id = ? ; ";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setDate(1, date);
            stmnt.setInt(2, patId);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void deletAllPatientMovements(Patient pat) throws SQLException {
        try {
            String sql = "DELETE FROM movments where pat_id = ?";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, pat.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public void deletePatientMoveMent(PatientMovement move) {
        try {
            String sql = "DELETE FROM movments where id = ?";
            PreparedStatement stmnt = connection.prepareStatement(sql);
            stmnt.setInt(1, move.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
        }
    }
}
