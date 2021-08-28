/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO.patient;

import com.jfoenix.controls.JFXToggleNode;
import java.sql.Date;
import java.util.HashMap;

/**
 *
 * @author Ayman
 */
public class PatientMovement {
// SERVICE TYPE

    public static final int SEVRICE_TYPE_CONSULTANT = 1;
    public static final int SEVRICE_TYPE_DRUG = 2;
    public static final int SEVRICE_TYPE_LAB = 3;
    public static final int SEVRICE_TYPE_ADD_LAB = 4;
    public static final int SEVRICE_TYPE_FINISH = 5;
    
// FINISH STATE
    public static final int ADD_NEW = 0;
    public static final int DRUG_ADD = 1;
    public static final int LAB_ADD = 2;
    public static final int FINISH = 9;

    public static final HashMap<Integer, String> dept = new HashMap<>();
    private int no;
    private int id;
    private Patient patient;
    private int pat_id;
    private String file_no;
    private String name;
    private int serviceType;
    private String service;
    private int doctor;
    private int finish;
    private Date date;
    private JFXToggleNode call;

    public PatientMovement() {
        dept.put(1, "Consultant");
        dept.put(2, "Drug");
        dept.put(3, " back from lab");
        dept.put(4, "sent to lab");
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getPat_id() {
        return pat_id;
    }

    public void setPat_id(int pat_id) {
        this.pat_id = pat_id;
    }

    public String getFile_no() {
        return file_no;
    }

    public void setFile_no(String file_no) {
        this.file_no = file_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public JFXToggleNode getCall() {
        return call;
    }

    public void setCall(JFXToggleNode call) {
        this.call = call;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

}
