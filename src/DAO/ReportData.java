/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.HashMap;
import net.sf.jasperreports.engine.JRDataSource;

/**
 *
 * @author Ayman
 */
public class ReportData {

    private String location;
    private String printer;
    private HashMap<String, Object> param;
    private JRDataSource dataSource;
    private String reportName;

    public ReportData(String location, String printer, HashMap<String, Object> param, JRDataSource dataSource, String reportName) {
        this.location = location;
        this.printer = printer;
        this.param = param;
        this.dataSource = dataSource;
        this.reportName = reportName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public HashMap<String, Object> getParam() {
        return param;
    }

    public void setParam(HashMap<String, Object> param) {
        this.param = param;
    }

    public JRDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(JRDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

}
