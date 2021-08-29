/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO.patient;

import BAO.RegionManage;
import DAO.Diagnosis;
import DAO.Region;
import DAO.patient.Patient;
import DAO.user.User;
import commons.DBConnection;
import commons.DbStatments;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ayman
 */
public class PatientManage {

    private Connection con;

    public PatientManage() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int add(Patient pat) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.patient"
                    + "(pat_id,name,phone,birth,region,entry,doctor,icd10,surface,weight,gender"
                    + ",job,relation,education,patient.add,height,motherName)"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ; ";
            PreparedStatement stmnt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmnt.setString(1, pat.getPatId());
            stmnt.setString(2, pat.getName());
            stmnt.setString(3, pat.getPhone());
            stmnt.setDate(4, pat.getBirth());
            stmnt.setInt(5, pat.getRegion().getId());
            stmnt.setDate(6, pat.getEntry());
            stmnt.setInt(7, pat.getDoctor().getId());
            stmnt.setString(8, pat.getIcd10());
            stmnt.setFloat(9, pat.getSurface());
            stmnt.setFloat(10, pat.getWeight());
            stmnt.setInt(11, pat.getGender());
            stmnt.setInt(12, pat.getJob());
            stmnt.setInt(13, pat.getRelation());
            stmnt.setInt(14, pat.getEducation());
            stmnt.setInt(15, pat.getAddress());
            stmnt.setFloat(16, pat.getHeight());
            stmnt.setString(17, pat.getMotherName());
            int queryResult = stmnt.executeUpdate();
            ResultSet resultID;
            int result;
            resultID = stmnt.getGeneratedKeys();
            while (resultID.next()) {
                result = Integer.parseInt(resultID.getString(1));
                return result;
            }
            return -1;
        } catch (SQLException e) {
            throw e;
        }

    }

    public void update(Patient pat) throws SQLException {
        try {
            String sql = "update oncology.patient set pat_id = ?,name = ?,phone = ?,birth = ?,region = ?,doctor = ? "
                    + ",icd10 = ?,surface = ?,weight = ?,gender = ? , entry = ?"
                    + ",job= ?,relation= ?,education= ? , patient.add = ?"
                    + ",height=?,motherName=?  "
                    + " where id = ? ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, pat.getPatId());
            stmnt.setString(2, pat.getName());
            stmnt.setString(3, pat.getPhone());
            stmnt.setDate(4, pat.getBirth());
            stmnt.setInt(5, pat.getRegion().getId());
            stmnt.setInt(6, pat.getDoctor().getId());
            stmnt.setString(7, pat.getIcd10());
            stmnt.setFloat(8, pat.getSurface());
            stmnt.setFloat(9, pat.getWeight());
            stmnt.setInt(10, pat.getGender());
            stmnt.setDate(11, pat.getEntry());
            stmnt.setInt(12, pat.getJob());
            stmnt.setInt(13, pat.getRelation());
            stmnt.setInt(14, pat.getEducation());
            stmnt.setInt(15, pat.getAddress());
            stmnt.setFloat(16, pat.getHeight());
            stmnt.setString(17, pat.getMotherName());
            stmnt.setInt(18, pat.getId());
            stmnt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }

    }

    public void delete(Patient pat) throws SQLException {
        try {
            String sql = "DELETE FROM oncology.patient where id = ? ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pat.getId());
            stmnt.executeUpdate();

        } catch (Exception e) {
            throw new SQLException("Error Add Region");
        }

    }

    public ArrayList<Patient> allPatient() throws SQLException {
        try {
            String sql = "select * from patient ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<Patient> allPatient = new ArrayList<>();
            while (rs.next()) {
                Patient pat = new Patient();
                pat.setId(rs.getInt(1));
                pat.setPatId(rs.getString(2));
                pat.setName(rs.getString(3));
                pat.setPhone(rs.getString(4));
                pat.setBirth(rs.getDate(5));
                Region rg = new Region();
                rg.setId(rs.getInt(6));
                pat.setRegion(rg);
                pat.setEntry(rs.getDate(7));
                User doctor = new User();
                doctor.setId(rs.getInt(8));
                pat.setDoctor(doctor);
                Diagnosis diagnosis = new Diagnosis();
                diagnosis.setId(rs.getInt(9));
                pat.setDiagnosis(diagnosis);
                pat.setBlackList(rs.getInt(10));
                pat.setIcd10(rs.getString(11));
                pat.setSurface(rs.getFloat(12));
                pat.setWeight(rs.getFloat(13));
                pat.setGender(rs.getInt(14));
                pat.setJob(rs.getInt(15));
                pat.setRelation(rs.getInt(16));
                pat.setEducation(rs.getInt(17));
                pat.setAddress(rs.getInt(18));
                pat.setHeight(rs.getFloat(19));
                pat.setMotherName(rs.getString(20));
                allPatient.add(pat);
            }
            return allPatient;
        } catch (Exception e) {
            throw new SQLException("Error gte Patient Data");
        }
    }

    public ArrayList<Patient> findPatients(String keyWord) throws SQLException {
        try {
            String sql = DbStatments.FIND_PAT_BY_KEY_WORD;
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, keyWord + "%");
            ResultSet rs = stmnt.executeQuery();
            ArrayList<Patient> allPatient = new ArrayList<>();
            while (rs.next()) {
                Patient pat = new Patient();
                pat.setId(rs.getInt(1));
                pat.setPatId(rs.getString(2));
                pat.setName(rs.getString(3));
                pat.setPhone(rs.getString(4));
                pat.setBirth(rs.getDate(5));
                Region rg = new Region();
                rg.setId(rs.getInt(6));
                rg.setName(rs.getString(7));
                pat.setRegion(rg);
                pat.setEntry(rs.getDate(8));
                User doctor = new User();
                doctor.setId(rs.getInt(9));
                doctor.setName(rs.getString(10));
                pat.setDoctor(doctor);
                Diagnosis diagnosis = new Diagnosis();
                diagnosis.setId(rs.getInt(11));
                diagnosis.setName(rs.getString(12));
                pat.setDiagnosis(diagnosis);
                pat.setBlackList(rs.getInt(13));
                pat.setIcd10(rs.getString(14));
                pat.setSurface(rs.getFloat(15));
                pat.setWeight(rs.getFloat(16));
                pat.setGender(rs.getInt(17));
                pat.setJob(rs.getInt(18));
                pat.setRelation(rs.getInt(19));
                pat.setEducation(rs.getInt(20));
                pat.setAddress(rs.getInt(21));
                pat.setHeight(rs.getFloat(22));
                pat.setMotherName(rs.getString(23));
                allPatient.add(pat);
            }
            return allPatient;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error gte Patient Data");
        }
    }

    public Patient findByBarcode(int keyWord) throws SQLException {
        try {
            String sql = "select pat.id,pat.pat_id,pat.name,pat.phone,pat.birth,pat.region,rego.name,pat.entry,pat.doctor,us.name,pat.diagnosis ,\n"
                    + "dia.name,pat.black_list ,pat.icd10,pat.surface,pat.weight,pat.gender "
                    + ", pat.job , pat.relation,pat.education,pat.add ,pat.height,pat.motherName \n"
                    + "from patient as pat \n"
                    + "LEFT join region as rego on pat.region = rego.id \n"
                    + "LEFT join users as us on pat.doctor = us.id \n"
                    + "LEFT join diagnosis as dia on pat.diagnosis = dia.id "
                    + "where pat.id = ?  ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, keyWord);
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                Patient pat = new Patient();
                pat.setId(rs.getInt(1));
                pat.setPatId(rs.getString(2));
                pat.setName(rs.getString(3));
                pat.setPhone(rs.getString(4));
                pat.setBirth(rs.getDate(5));
                Region rg = new Region();
                rg.setId(rs.getInt(6));
                rg.setName(rs.getString(7));
                pat.setRegion(rg);
                pat.setEntry(rs.getDate(8));
                User doctor = new User();
                doctor.setId(rs.getInt(9));
                doctor.setName(rs.getString(10));
                pat.setDoctor(doctor);
                Diagnosis diagnosis = new Diagnosis();
                diagnosis.setId(rs.getInt(11));
                diagnosis.setName(rs.getString(12));
                pat.setDiagnosis(diagnosis);
                pat.setBlackList(rs.getInt(13));
                pat.setIcd10(rs.getString(14));
                pat.setSurface(rs.getFloat(15));
                pat.setWeight(rs.getFloat(16));
                pat.setGender(rs.getInt(17));
                pat.setJob(rs.getInt(18));
                pat.setRelation(rs.getInt(19));
                pat.setEducation(rs.getInt(20));
                pat.setAddress(rs.getInt(21));
                pat.setHeight(rs.getFloat(22));
                pat.setMotherName(rs.getString(23));
                return pat;
            }
            return null;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    public ArrayList<Patient> findPatID(String keyWord) throws SQLException {
        try {
            String sql = DbStatments.FIND_PAT_BY_PAT_ID;
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, keyWord + "%");
            ResultSet rs = stmnt.executeQuery();
            ArrayList<Patient> allPatient = new ArrayList<>();
            while (rs.next()) {
                Patient pat = new Patient();
                pat.setId(rs.getInt(1));
                pat.setPatId(rs.getString(2));
                pat.setName(rs.getString(3));
                pat.setPhone(rs.getString(4));
                pat.setBirth(rs.getDate(5));
                Region rg = new Region();
                rg.setId(rs.getInt(6));
                rg.setName(rs.getString(7));
                pat.setRegion(rg);
                pat.setEntry(rs.getDate(8));
                User doctor = new User();
                doctor.setId(rs.getInt(9));
                doctor.setName(rs.getString(10));
                pat.setDoctor(doctor);
                Diagnosis diagnosis = new Diagnosis();
                diagnosis.setId(rs.getInt(11));
                diagnosis.setName(rs.getString(12));
                pat.setDiagnosis(diagnosis);
                pat.setBlackList(rs.getInt(13));
                pat.setIcd10(rs.getString(14));
                pat.setSurface(rs.getFloat(15));
                pat.setWeight(rs.getFloat(16));
                pat.setGender(rs.getInt(17));
                pat.setJob(rs.getInt(18));
                pat.setRelation(rs.getInt(19));
                pat.setEducation(rs.getInt(20));
                pat.setAddress(rs.getInt(21));
                allPatient.add(pat);
            }
            return allPatient;
        } catch (SQLException e) {
            throw e;
        }
    }

    public Patient checkPatDuplicate(Patient keyWord) throws SQLException {
        try {
            String sql = "select p.id,p.pat_id,p.name,p.phone,p.birth,p.region,r.name,p.entry,p.doctor,d.name,"
                    + "p.diagnosis ,dia.name,p.black_list,p.icd10,p.surface,p.weight,p.gender "
                    + ", p.job , p.relation,p.education,p.add,p.height,p.motherName \n "
                    + "from patient as p \n"
                    + "LEFT join region as r on p.region = r.id \n"
                    + "LEFT join users as d on p.doctor = d.id \n"
                    + "LEFT join diagnosis as dia on p.diagnosis = dia.id\n"
                    + "where p.name = ?  OR p.pat_id = ?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, keyWord.getName());
            stmnt.setString(2, keyWord.getPatId());
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                Patient pat = new Patient();
                pat.setId(rs.getInt(1));
                pat.setPatId(rs.getString(2));
                pat.setName(rs.getString(3));
                pat.setPhone(rs.getString(4));
                pat.setBirth(rs.getDate(5));
                Region rg = new Region();
                rg.setId(rs.getInt(6));
                rg.setName(rs.getString(7));
                pat.setRegion(rg);
                pat.setEntry(rs.getDate(8));
                User doctor = new User();
                doctor.setId(rs.getInt(9));
                doctor.setName(rs.getString(10));
                pat.setDoctor(doctor);
                Diagnosis diagnosis = new Diagnosis();
                diagnosis.setId(rs.getInt(11));
                diagnosis.setName(rs.getString(12));
                pat.setDiagnosis(diagnosis);
                pat.setBlackList(rs.getInt(13));
                pat.setIcd10(rs.getString(14));
                pat.setSurface(rs.getFloat(15));
                pat.setWeight(rs.getFloat(16));
                pat.setGender(rs.getInt(17));
                pat.setJob(rs.getInt(18));
                pat.setRelation(rs.getInt(19));
                pat.setEducation(rs.getInt(20));
                pat.setAddress(rs.getInt(21));
                return pat;
            }
            return null;
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updatediagnosis(Patient pat) throws SQLException {
        try {
            String sql = "update oncology.patient set diagnosis = ? where id = ? ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pat.getDiagnosis().getId());
            stmnt.setInt(2, pat.getId());
            stmnt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    public ArrayList<Date> getPatVisits(int pat) {
        try {
            String sql = " select prescription_no.date from prescription_no where patient_id = ? order by id desc";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pat);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<Date> allDate = new ArrayList<>();
            while (rs.next()) {
                allDate.add(rs.getDate(1));
            }
            return allDate;
        } catch (SQLException e) {
            return null;
        }
    }
}
