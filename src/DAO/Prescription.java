/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Date;

/**
 *
 * @author Ayman
 */
public class Prescription {

    public static final int NEW_ADD = 0;
    public static final int ACCEPTED = 1;
    public static final int FINISHED = 2;
    public static final int REFUSED = 3;
    public static final int DELETE = 4;
    public static final int DRUG_OUT_STOCK = 5;
    public static final String PREPARE_TIME = "prepare_time";
    public static final String SCREEN_TIME = "screen_time";
    public static final String PHARMACY_TIME = "pharmacy_time";
    public static final String ID = "id";

    private int id;
    private int patientId;
    private String patientName;
    private String patientNumber;
    private int age ;
    private Date date;
    private int no;
    private int user;
    private String doctorName;
    private int checked ;
    private int acceptState ;
    public Prescription() {
    }

    public Prescription(int id, int patientId, String patientName, Date date, int no, int user, String userName) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.date = date;
        this.no = no;
        this.user = user;
        this.doctorName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

  

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getAcceptState() {
        return acceptState;
    }

    public void setAcceptState(int acceptState) {
        this.acceptState = acceptState;
    }

  

}
