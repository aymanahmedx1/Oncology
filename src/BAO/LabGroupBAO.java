/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.LabGroup;
import commons.DBConnection;
import java.sql.Connection;
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
public class LabGroupBAO {
      private Connection con;

    public LabGroupBAO() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

 
    public void add(LabGroup group) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.lab_group(name)VALUES(?) ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, group.getName());
            stmnt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error Add Group");
        }

    }
    public void update(LabGroup group) throws SQLException {
        try {
            String sql = "update lab_group set name= ? WHERE id = ?  ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, group.getName());
            stmnt.setInt(2, group.getId());
            stmnt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error Add Group");
        }

    }

    public ArrayList<LabGroup> AllLabGroup() throws SQLException {
        try {
            String sql = "SELECT * FROM lab_group ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ArrayList<LabGroup> AllGroup = new ArrayList<>();
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                LabGroup rg = new LabGroup();
                rg.setId(rs.getInt(1));
                rg.setName(rs.getString(2));
                AllGroup.add(rg);
            }
            return AllGroup;
        } catch (Exception e) {
            throw new SQLException("Error Add lab_group");
        }
    }
}
