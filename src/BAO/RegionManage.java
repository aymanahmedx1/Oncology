/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.Region;
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
 * @author Ayman
 */
public class RegionManage {

    private static Connection con;

    public RegionManage() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void add(Region region) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.region(name)VALUES(?) ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, region.getName());
            stmnt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error Add Region");
        }

    }

    public ArrayList<Region> allRegion() throws SQLException {
        try {
            String sql = "SELECT * FROM region ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ArrayList<Region> allRegion = new ArrayList<>();
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                Region rg = new Region();
                rg.setId(rs.getInt(1));
                rg.setName(rs.getString(2));
                allRegion.add(rg);
            }
            return allRegion;
        } catch (Exception e) {
            throw new SQLException("Error Add Region");
        }
    }

    public void update(Region region) throws SQLException {
        try {
            String sql = "update oncology.region set name = ? where id = ?  ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, region.getName());
            stmnt.setInt(2, region.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }

    }
}
