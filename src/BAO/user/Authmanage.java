/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO.user;

import DAO.user.Permisions;
import DAO.user.User;
import commons.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ayman
 */
public class Authmanage {

    public void addPermissionForNewUser(User user) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "INSERT INTO oncology.permision(id) VALUES (?);";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, user.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }

    }

    public Permisions getUserAuth(User user) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "SELECT * FROM permision WHERE id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, user.getId());
            ResultSet r = stmnt.executeQuery();
            Permisions auth = null;
            int no = 1;
            while (r.next()) {
                auth = new Permisions(r.getInt(no++), r.getInt(no++), r.getInt(no++), r.getInt(no++), r.getInt(no++),
                        r.getInt(no++), r.getInt(no++), r.getInt(no++), r.getInt(no++), r.getInt(no++),
                        r.getInt(no++), r.getInt(no++), r.getInt(no++), r.getInt(no++), r.getInt(no++),
                        r.getInt(no++), r.getInt(no++), r.getInt(no++), r.getInt(no++), r.getInt(no++),
                        r.getInt(no++), r.getInt(no++), r.getInt(no++), r.getInt(no++), r.getInt(no++));
            }
            return auth;
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateAuth(Permisions per) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "UPDATE oncology.permision SET reception = ?,doctor = ?,death_note = ?,chemo_check = ?,clinical_pharmacy = ?,prepare_drug =?,\n"
                    + "black_list = ? ,drug_dose = ?,pat_management = ?,pat_cost =?,lab = ?,doc_screen =?,lab_screen = ?,pharmacy_screen = ?,\n"
                    + "search_for_pat = ?,serach_for_drug = ?,user_manage = ?,drug = ?,lab_test =?,dia_manage =?,region_manage =?,pref_manage =?,\n"
                    + "DelData = ? ,is_admin=? WHERE id =?;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            int no = 1 ; 
            stmnt.setInt(no++, per.getReception());
            stmnt.setInt(no++, per.getDoctor());
            stmnt.setInt(no++, per.getDeath_note());
            stmnt.setInt(no++, per.getChemo_check());
            stmnt.setInt(no++, per.getClinical_pharmacy());
            stmnt.setInt(no++, per.getPrepare_drug());
            stmnt.setInt(no++, per.getBlack_list());
            stmnt.setInt(no++, per.getDrug_dose());
            stmnt.setInt(no++, per.getPat_management());
            stmnt.setInt(no++, per.getPat_cost());
            stmnt.setInt(no++, per.getLab());
            stmnt.setInt(no++, per.getDoc_screen());
            stmnt.setInt(no++, per.getLab_screen());
            stmnt.setInt(no++, per.getPharmacy_screen());
            stmnt.setInt(no++, per.getSearch_for_pat());
            stmnt.setInt(no++, per.getSerach_for_drug());
            stmnt.setInt(no++, per.getUser_manage());
            stmnt.setInt(no++, per.getDrug());
            stmnt.setInt(no++, per.getLab_test());
            stmnt.setInt(no++, per.getDia_manage());
            stmnt.setInt(no++, per.getRegion_manage());
            stmnt.setInt(no++, per.getPref_manage());
            stmnt.setInt(no++, per.getDelData());
            stmnt.setInt(no++, per.getIs_admin());
            stmnt.setInt(no++, per.getUserId());
            stmnt.executeUpdate();
           
        } catch (SQLException e) {
            throw e;
        }
    }

}
