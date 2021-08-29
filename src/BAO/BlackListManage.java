/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.Address;
import DAO.BlackList;
import commons.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class BlackListManage {
     private Connection con;

    public BlackListManage() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(BlackListManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int add(BlackList black) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.black_list(date,pat_ID,pres,note)VALUES(?,?,?,?) ; ";
            PreparedStatement stmnt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmnt.setDate(1,black.getDate());
            stmnt.setInt(2,black.getPatId());
            stmnt.setInt(3,black.getPrescription());
            stmnt.setString(4,black.getNote());
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

}
