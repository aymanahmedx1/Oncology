/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import commons.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class DatabaseChanges {

    private Connection con;

    public DatabaseChanges() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void databaseChanges() throws SQLException {
        try {
            String sql = "ALTER TABLE `oncology`.`patient` \n"
                    + "ADD COLUMN `job` INT NULL AFTER `gender`,\n"
                    + "ADD COLUMN `relation` INT NULL AFTER `job`,\n"
                    + "ADD COLUMN `education` INT NULL AFTER `relation`,\n"
                    + "ADD COLUMN `add` INT NULL AFTER `education` \n";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e ; 
        }
    }

}
