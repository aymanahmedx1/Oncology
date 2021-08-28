/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.Address;
import DAO.patient.Patient;
import commons.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class AddressManage {

    private Connection con;

    public AddressManage() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int add(Address adress) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.address(add1,add2)VALUES(?,?) ; ";
            PreparedStatement stmnt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmnt.setString(1, adress.getAdd1());
            stmnt.setString(2, adress.getAdd2());
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

    public ArrayList<Address> alladress() throws SQLException {
        try {
            String sql = "SELECT * FROM address ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ArrayList<Address> allRegion = new ArrayList<>();
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                Address address = new Address();
                address.setId(rs.getInt(1));
                address.setAdd1(rs.getString(2));
                address.setAdd2(rs.getString(3));
                allRegion.add(address);
            }
            return allRegion;
        } catch (SQLException e) {
            throw e;
        }

    }

    public Address getPatadress(Patient pat) throws SQLException {
        try {
            String sql = "SELECT * FROM address  where id = ? ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pat.getAddress());
            ResultSet rs = stmnt.executeQuery();
            Address address = new Address();
            while (rs.next()) {
                address.setId(rs.getInt(1));
                address.setAdd1(rs.getString(2));
                address.setAdd2(rs.getString(3));
            }
            return address;
        } catch (SQLException e) {
            throw e;
        }

    }

    public void updateAdress(Address add) throws SQLException {
        try {
            String sql = "UPDATE oncology.address set add1 = ? ,add2 = ? where id = ? ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, add.getAdd1());
            stmnt.setString(2, add.getAdd2());
            stmnt.setInt(3, add.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }

    }

}
