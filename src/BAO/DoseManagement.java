/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.Drug;
import DAO.Prescription;
import DAO.patient.Patient;
import commons.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class DoseManagement {

    private static Connection con;

    public DoseManagement() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<Drug> getDrugDose(Prescription pres) throws SQLException {
        try {
            String sql = "SELECT drugs.id,drugs.drug_name , sum(pres.dose) ,drugs.main_category ,drugs.Strength ,CEILING((sum(pres.dose)/drugs.Strength ))\n"
                    + "from oncology.prescription_detail as pres \n"
                    + "join drugs on pres.drug = drugs.id\n"
                    + "where drugs.main_category != 3  and pres.prescription_no = ?\n"
                    + "group by drugs.id\n"
                    + "union\n"
                    + "SELECT drugs.id,drugs.drug_name , sum(pres.volume) ,drugs.main_category ,drugs.Strength ,CEILING((sum(pres.volume)/drugs.Strength ))\n"
                    + "from oncology.prescription_detail as pres \n"
                    + "join drugs on pres.fluid = drugs.id\n"
                    + "where pres.prescription_no = ?\n"
                    + "group by drugs.id ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pres.getId());
            stmnt.setInt(2, pres.getId());
            ResultSet result = stmnt.executeQuery();
            ArrayList<Drug> allDrugs = new ArrayList<>();
            while (result.next()) {
                Drug drug = new Drug();
                drug.setId(result.getInt(1));
                drug.setName(result.getString(2));
                drug.setCategory(result.getInt(4));
                drug.setStock(result.getFloat(3));
                drug.setStrength(result.getInt(5));
                drug.setNoOfVials(result.getInt(6));
                allDrugs.add(drug);
            }
            return allDrugs;
        } catch (SQLException e) {
            throw e;
        }

    }

    public ArrayList<Drug> getSearchForDrug(Date from, Date to, Drug d) throws SQLException {
        try {
            String sql = "SELECT drugs.id,drugs.drug_name , sum(pres.dose) ,drugs.main_category ,drugs.Strength ,(sum(pres.dose)/drugs.Strength )\n"
                    + "from oncology.prescription_detail as pres \n"
                    + "join drugs on pres.drug = drugs.id\n"
                    + "where pres.date between ? and ?\n"
                    + "and drugs.id = ?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, from);
            stmnt.setDate(2, to);
            stmnt.setInt(3, d.getId());
            ResultSet result = stmnt.executeQuery();
            ArrayList<Drug> allDrugs = new ArrayList<>();
            while (result.next()) {
                Drug drug = new Drug();
                drug.setId(result.getInt(1));
                drug.setName(result.getString(2));
                drug.setCategory(result.getInt(4));
                drug.setStock(result.getFloat(3));
                drug.setStrength(result.getInt(5));
                drug.setFloatOfVials(result.getFloat(6));
                allDrugs.add(drug);
            }
            return allDrugs;
        } catch (SQLException e) {
            throw e;
        }

    }

    public ArrayList<Drug> getDrugDoseForPat(Patient pat, Date from, Date to) throws SQLException {
        try {
            String sql = "SELECT drugs.id,drugs.drug_name , sum(pres_det.dose) ,drugs.main_category ,drugs.Strength ,(sum(pres_det.dose)/drugs.Strength )\n"
                    + "from prescription_detail as pres_det \n"
                    + "join drugs on pres_det.drug = drugs.id\n"
                    + "join prescription_no as pres_no on pres_no.id = pres_det.prescription_no\n"
                    + "where pres_det.date between ? and ? and drugs.main_category != 3 and pres_no.patient_id =? \n"
                    + "group by drugs.id\n"
                    + "union\n"
                    + "SELECT drugs.id,drugs.drug_name , sum(pres_det.volume) ,drugs.main_category ,drugs.Strength ,(sum(pres_det.volume)/drugs.Strength )\n"
                    + "from  prescription_detail as pres_det \n"
                    + "join drugs on pres_det.fluid = drugs.id\n"
                    + "join prescription_no as pres_no on pres_no.id = pres_det.prescription_no\n"
                    + "where pres_det.date between  ? and ? and pres_no.patient_id =?  \n"
                    + "group by drugs.id ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, from);
            stmnt.setDate(2, to);
            stmnt.setInt(3, pat.getId());
            stmnt.setDate(4, from);
            stmnt.setDate(5, to);
            stmnt.setInt(6, pat.getId());
            ResultSet result = stmnt.executeQuery();
            ArrayList<Drug> allDrugs = new ArrayList<>();
            while (result.next()) {
                Drug drug = new Drug();
                drug.setId(result.getInt(1));
                drug.setName(result.getString(2));
                drug.setCategory(result.getInt(4));
                drug.setStock(result.getFloat(3));
                drug.setStrength(result.getInt(5));
                drug.setNoOfVials(result.getInt(6));
                drug.setFloatOfVials(result.getFloat(6));
                allDrugs.add(drug);
            }
            return allDrugs;
        } catch (SQLException e) {
            throw e;
        }

    }

}
