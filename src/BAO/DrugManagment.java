/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.Drug;
import commons.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Ayman
 */
public class DrugManagment {

    public int addDrug(Drug drug) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "INSERT INTO drugs(drug_name,main_category,Strength,def_fluid,def_volume,Strength2,Strength3 , note , stock)VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmnt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmnt.setString(1, drug.getName());
            stmnt.setInt(2, drug.getCategory());
            stmnt.setInt(3, drug.getStrength());
            stmnt.setInt(4, drug.getDefFluid());
            stmnt.setInt(5, drug.getDefVolume());
            stmnt.setInt(6, drug.getStrength2());
            stmnt.setInt(7, drug.getStrength3());
            stmnt.setString(8, drug.getNote());
            stmnt.setFloat(9, drug.getStock());
            int queryResult = stmnt.executeUpdate();
            ResultSet resultID;
            int result;
            resultID = stmnt.getGeneratedKeys();
            while (resultID.next()) {
                result = Integer.parseInt(resultID.getString(1));
                return result;
            }
        } catch (SQLException e) {
            throw new SQLException("Error Save Patient");
        }
        throw new SQLException("Error Save Drug");
    }

    public boolean update(Drug drug) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "update drugs set drug_name=?,main_category=? ,Strength=? ,def_fluid=? ,def_volume=? ,Strength2=?,Strength3=? ,note=?  WHERE id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, drug.getName());
            stmnt.setInt(2, drug.getCategory());
            stmnt.setInt(3, drug.getStrength());
            stmnt.setInt(4, drug.getDefFluid());
            stmnt.setInt(5, drug.getDefVolume());
            stmnt.setInt(6, drug.getStrength2());
            stmnt.setInt(7, drug.getStrength3());
            stmnt.setString(8, drug.getNote());
            stmnt.setInt(9, drug.getId());

            int result = stmnt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

    }

    public boolean Delete(Drug drug) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "DLETE FROM drugs WHERE id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, drug.getId());
            int result = stmnt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new SQLException("Error DELETE Drug");
        }

    }

    public ArrayList<Drug> allDrug() throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "SELECT * FROM drugs order by drug_name";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ResultSet result = stmnt.executeQuery();
            ArrayList<Drug> allDrugs = new ArrayList<>();
            while (result.next()) {
                Drug drug = new Drug();
                drug.setId(result.getInt(1));
                drug.setName(result.getString(2));
                drug.setCategory(result.getInt(3));
                drug.setCategoryName(getCategoryName(drug.getCategory()));
                drug.setStock(result.getFloat(4));
                drug.setStrength(result.getInt(5));
                drug.setDefFluid(result.getInt(6));
                drug.setDefVolume(result.getInt(7));
                drug.setStrength2(result.getInt(8));
                drug.setStrength3(result.getInt(9));
                drug.setNote(result.getString(10));
                allDrugs.add(drug);
            }
            return allDrugs;
        } catch (SQLException e) {
            throw new SQLException("Error Get drugs");
        }

    }

    public Drug getDrugDose(Drug drug, Date from, Date to) throws SQLException {
        int no = 1;
        try (Connection conn = DBConnection.createConnection()) {
            String sql = "SELECT d.drug_name ,SUM(p.dose)  FROM drugs as d LEFT JOIN prescription_detail as p ON p.drug = d.id WHERE d.id =? AND date BETWEEN ? AND ? ;";
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.setInt(1, drug.getId());
            stmnt.setDate(2, from);
            stmnt.setDate(3, to);
            ResultSet resultSet = stmnt.executeQuery();
            Drug foundDrug = new Drug();
            while (resultSet.next()) {
                foundDrug.setId(drug.getId());
                foundDrug.setName(resultSet.getString(1));
                foundDrug.setStock(resultSet.getFloat(2));
                foundDrug.setNo(no++);
            }
            return foundDrug;
        } catch (Exception e) {
            throw new SQLException();
        }
    }

    public ArrayList<Drug> findDrug(String keyWord) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "SELECT * FROM drugs WHERE drug_name LIKE ?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, keyWord + "%");
            ResultSet result = stmnt.executeQuery();
            ArrayList<Drug> allDrugs = new ArrayList<>();
            while (result.next()) {
                Drug drug = new Drug();
                drug.setId(result.getInt(1));
                drug.setName(result.getString(2));
                drug.setCategory(result.getInt(3));
                drug.setStock(result.getFloat(4));
                drug.setStrength(result.getInt(5));
                drug.setDefFluid(result.getInt(6));
                drug.setDefVolume(result.getInt(7));
                drug.setStrength2(result.getInt(8));
                drug.setStrength3(result.getInt(9));
                drug.setNote(result.getString(10));
                allDrugs.add(drug);
            }
            return allDrugs;
        } catch (SQLException e) {
            throw new SQLException("Error Get drugs");
        }

    }

    public ArrayList<Drug> allFluid() throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "SELECT * FROM drugs where main_category = ?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, Drug.FLUID);
            ResultSet result = stmnt.executeQuery();
            ArrayList<Drug> allDrugs = new ArrayList<>();
            while (result.next()) {
                Drug drug = new Drug();
                drug.setId(result.getInt(1));
                drug.setName(result.getString(2));
                drug.setCategory(result.getInt(3));
                allDrugs.add(drug);
            }
            return allDrugs;
        } catch (SQLException e) {
            throw new SQLException("Error Get drugs");
        }

    }

    private String getCategoryName(int no) {
        switch (no) {
            case 1:
                return "CHEMOTHERAPY".toLowerCase();
            case 2:
                return "SUPPORTIVE".toLowerCase();
            case 3:
                return "FLUID".toLowerCase();
            default:
                return "";
        }

    }

    public void updateDrugStock(Drug drug) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "update drugs set stock=?  WHERE id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setFloat(1, drug.getStock());
            stmnt.setInt(2, drug.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public int getDrugStock(int drug) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "select stock from drugs where id = ? ;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, drug);
            ResultSet rs = stmnt.executeQuery();
            int result = -1;
            while (rs.next()) {
                result = rs.getInt(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

}
