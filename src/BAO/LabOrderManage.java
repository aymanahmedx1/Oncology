/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.LabGroup;
import DAO.LabOrder;
import DAO.LabOrderDetail;
import DAO.LabVisit;
import DAO.patient.Patient;
import DAO.patient.PatientMovement;
import commons.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ayman
 */
public class LabOrderManage {

    private static Connection con;

    public LabOrderManage() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public LabOrder addOrder(LabOrder order) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.lab_order(pat_id,doctor,DATE,note,movementNo)VALUES(?,?,?,?,?) ; ";
            PreparedStatement stmnt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmnt.setInt(1, order.getPatId());
            stmnt.setInt(2, order.getDoctor());
            stmnt.setDate(3, Date.valueOf(LocalDate.now()));
            stmnt.setString(4, order.getNote());
            stmnt.setInt(5, order.getMovementNo());
            stmnt.executeUpdate();
            ResultSet resultID;
            int result = -1;
            resultID = stmnt.getGeneratedKeys();
            while (resultID.next()) {
                result = Integer.parseInt(resultID.getString(1));
            }
            order.setDate(Date.valueOf(LocalDate.now()));
            order.setId(result);
            return order;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error Add LabTest");
        }

    }

    public void addOrderDetails(LabOrderDetail order) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.lab_order_detail(lab_order_detail.order,test)VALUES(?,?) ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, order.getOrder().getId());
            stmnt.setInt(2, order.getTest());
            stmnt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error Add LabTest");
        }

    }

    public ArrayList<LabVisit> getOrderDetails(LabOrder order, int finish) throws SQLException {
        try {
            String sql = " SELECT d.id,test.name , users.name , l_order.date , d.result ,test.group , d.finish , d.resultFile ,d.seen \n"
                    + "FROM oncology.lab_order_detail d\n"
                    + "JOIN lab_order as l_order on d.order = l_order.id\n"
                    + "join users as users on l_order.doctor = users.id\n"
                    + "join lab_test as test on d.test = test.id\n"
                    + "where d.order=? and d.finish = ?; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, order.getId());
            stmnt.setInt(2, finish);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<LabVisit> allLabVisit = new ArrayList<>();
            int no = 1;
            while (rs.next()) {
                LabVisit lb = new LabVisit();
                lb.setId(rs.getInt(1));
                lb.setTestName(rs.getString(2));
                lb.setDroctorName(rs.getString(3));
                lb.setDate(rs.getDate(4));
                lb.setResult(rs.getString(5));
                lb.setGroupId(rs.getInt(6));
                lb.setFinish((rs.getInt(7) != 0));
                lb.setResultFile(rs.getString(8));
                lb.setSeen(rs.getInt(9));
                lb.setNo(no++);
                allLabVisit.add(lb);
            }
            return allLabVisit;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }

    }

    public ArrayList<LabVisit> getOrderDetails(LabOrder order) throws SQLException {
        try {
            String sql = " SELECT d.id,test.name , users.name , l_order.date , d.result ,test.group , d.finish , d.resultFile ,d.seen \n"
                    + "FROM oncology.lab_order_detail d\n"
                    + "JOIN lab_order as l_order on d.order = l_order.id\n"
                    + "join users as users on l_order.doctor = users.id\n"
                    + "join lab_test as test on d.test = test.id\n"
                    + "where d.order=? ;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, order.getId());
            ResultSet rs = stmnt.executeQuery();
            ArrayList<LabVisit> allLabVisit = new ArrayList<>();
            int no = 1;
            while (rs.next()) {
                LabVisit lb = new LabVisit();
                lb.setId(rs.getInt(1));
                lb.setTestName(rs.getString(2));
                lb.setDroctorName(rs.getString(3));
                lb.setDate(rs.getDate(4));
                lb.setResult(rs.getString(5));
                lb.setGroupId(rs.getInt(6));
                lb.setFinish((rs.getInt(7) != 0));
                lb.setResultFile(rs.getString(8));
                lb.setSeen(rs.getInt(9));
                lb.setNo(no++);
                allLabVisit.add(lb);
            }
            return allLabVisit;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }

    }

    public LabOrder getTodayLabOrder(PatientMovement pat) throws SQLException {
        try {
            String sql = "SELECT * FROM oncology.lab_order where pat_id =? and DATE =?; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pat.getPatient().getId());
            stmnt.setDate(2, pat.getDate());
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                LabOrder lb = new LabOrder();
                lb.setId(rs.getInt(1));
                lb.setPatId(rs.getInt(2));
                lb.setDoctor(rs.getInt(3));
                lb.setDate(rs.getDate(4));
                lb.setState(rs.getInt(5));
                lb.setMovementNo(rs.getInt(6));
                return lb;
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException("Error Add LabTest");
        }

    }

    public ArrayList<LabOrder> getLabOrders(int state) throws SQLException {
        try {
            String sql = "SELECT ord.id,pat.id,pat.pat_id,pat.name , ord.doctor , users.name ,ord.date,ord.state , ord.note ,ord.movementNo ,ord.call\n"
                    + "FROM oncology.lab_order as ord\n"
                    + "join users as users on users.id = ord.doctor\n"
                    + "join patient as pat on pat.id = ord.pat_id\n"
                    + " WHERE DATE = ? AND state = ? order by ord.id ;";

            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, Date.valueOf(LocalDate.now()));
            stmnt.setInt(2, state);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<LabOrder> allLabOrder = new ArrayList<>();
            int index = 1;
            while (rs.next()) {
                LabOrder lb = new LabOrder();
                lb.setNo(index++);
                lb.setId(rs.getInt(1));
                lb.setPatId(rs.getInt(2));
                lb.setPatFileId(rs.getString(3));
                lb.setPatName(rs.getString(4));
                lb.setDoctor(rs.getInt(5));
                lb.setDoctorName(rs.getString(6));
                lb.setDate(rs.getDate(7));
                lb.setState(rs.getInt(8));
                lb.setNote(rs.getString(9));
                lb.setMovementNo(rs.getInt(10));
                lb.setCall(rs.getInt(11));
                allLabOrder.add(lb);
            }
            return allLabOrder;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

    }

    public ArrayList<LabOrder> getLabOrders(int state, Date from, Date to) throws SQLException {
        try {
            String sql = "SELECT ord.id,pat.id,pat.pat_id,pat.name , ord.doctor , users.name ,ord.date,ord.state , ord.note ,ord.movementNo ,ord.call\n"
                    + "FROM oncology.lab_order as ord\n"
                    + "join users as users on users.id = ord.doctor\n"
                    + "join patient as pat on pat.id = ord.pat_id\n"
                    + " WHERE DATE between ? and ? AND state = ? "
                    + "order by ord.id ;";

            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setDate(1, from);
            stmnt.setDate(2, to);
            stmnt.setInt(3, state);
            ResultSet rs = stmnt.executeQuery();
            ArrayList<LabOrder> allLabOrder = new ArrayList<>();
            int index = 1;
            while (rs.next()) {
                LabOrder lb = new LabOrder();
                lb.setNo(index++);
                lb.setId(rs.getInt(1));
                lb.setPatId(rs.getInt(2));
                lb.setPatFileId(rs.getString(3));
                lb.setPatName(rs.getString(4));
                lb.setDoctor(rs.getInt(5));
                lb.setDoctorName(rs.getString(6));
                lb.setDate(rs.getDate(7));
                lb.setState(rs.getInt(8));
                lb.setNote(rs.getString(9));
                lb.setMovementNo(rs.getInt(10));
                lb.setCall(rs.getInt(11));
                allLabOrder.add(lb);
            }
            return allLabOrder;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

    }

    public void addTestResult(LabVisit visit) throws SQLException {
        try {
            String sql = "update lab_order_detail set result = ? , resultFile = ? where id = ?; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, visit.getResult());
            stmnt.setString(2, visit.getResultFile());
            stmnt.setInt(3, visit.getId());
            stmnt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error Add LabTest");
        }

    }

    public void updateTestState(LabOrder order, int state) throws SQLException {
        try {
            String sql = "UPDATE lab_order set  state = ? where id = ?; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, state);
            stmnt.setInt(2, order.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public int getFinishedLabOrder() {
        int result = 0;
        try {
            String sql = "SELECT COUNT(*) FROM oncology.lab_order where state = ? and date = ? ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, PatientMovement.FINISH);
            stmnt.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public void deleteOrderItems(LabOrder order, int finish) throws SQLException {
        try {
            String sql = "DELETE FROM oncology.lab_order_detail WHERE lab_order_detail.order = ? and finish = ?; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, order.getId());
            stmnt.setInt(2, finish);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public ArrayList<LabGroup> getLabOrderGroups(LabOrder order) throws SQLException {
        try {
            String sql = "SELECT  distinct test.group ,lab_group.name\n"
                    + "FROM oncology.lab_order_detail d\n"
                    + "join lab_test as test on d.test = test.id\n"
                    + "join lab_group on lab_group.id = test.group\n"
                    + "where d.order=?  ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, order.getId());
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

    public void updateLabOrderDetailsState(LabVisit visit) throws SQLException {
        try {
            String sql = "update lab_order_detail set finish = ? where lab_order_detail.id = ?  ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, 1);
            stmnt.setInt(2, visit.getId());
            stmnt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateLabOrderDetailsState(LabOrder visit) throws SQLException {
        try {
            String sql = "update lab_order_detail set finish = ? where lab_order_detail.order = ?  ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, 1);
            stmnt.setInt(2, visit.getId());
            stmnt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    public void deletePatientLab(LabOrder order) throws SQLException {
        try {
            String sql = "DELETE FROM oncology.lab_order_detail WHERE lab_order_detail.order = ? ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, order.getId());
            stmnt.executeUpdate();
            String sql2 = "DELETE FROM oncology.lab_order WHERE id = ? ; ";
            PreparedStatement stmnt2 = con.prepareStatement(sql2);
            stmnt2.setInt(1, order.getId());
            stmnt2.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void deleteAllPatientLab(Patient pat) throws SQLException {
        try {
            String sql2 = "DELETE FROM oncology.lab_order WHERE pat_id = ? ; ";
            PreparedStatement stmnt2 = con.prepareStatement(sql2);
            stmnt2.setInt(1, pat.getId());
            stmnt2.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public void updateLabItemOpenState(LabVisit visit) throws SQLException {
        try {
            String sql = "update lab_order_detail set seen = ?  where id = ?; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, visit.getSeen());
            stmnt.setInt(2, visit.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateCallState(LabOrder order) {
        try {
            String sql = " update oncology.lab_order set lab_order.call = ? ,calledTime=? where id  = ? ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, order.getCall());
            stmnt.setTime(2, Time.valueOf(LocalTime.now()));
            stmnt.setInt(3, order.getId());
            stmnt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
