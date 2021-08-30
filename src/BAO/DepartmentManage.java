/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.Address;
import DAO.Department;
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
public class DepartmentManage {

    private Connection con;

    public DepartmentManage() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int add(Department dept) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.dept(name)VALUES(?) ; ";
            PreparedStatement stmnt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmnt.setString(1, dept.getName());
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

    public ArrayList<Department> getAllDepartMent() throws SQLException {
        try {
            String sql = "select * from  oncology.dept ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<Department> allDept = new ArrayList<>();
            while (rs.next()) {
                Department dp = new Department(rs.getInt(1), rs.getString(2));
                allDept.add(dp);
            }
            return allDept;
        } catch (SQLException e) {
            throw e;
        }
    }
}
