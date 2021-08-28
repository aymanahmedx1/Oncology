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
public class LabOrder {

    public static final int FINISH = 1;
    public static final int STILL = 0;
    public static final int NOT_CALL = 0;
    public static final int CALL_SAMPLE = 1;
    public static final int CALL_RESULT = 2;
    private int id;
    private int no ; 
    private int doctor;
    private String doctorName;
    private int patId;
    private String patFileId;
    private String patName;
    private int state;
    private Date date;
    private String note;
    private int movementNo;
    private int call;

    public int getMovementNo() {
        return movementNo;
    }

    public void setMovementNo(int movementNo) {
        this.movementNo = movementNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getPatId() {
        return patId;
    }

    public void setPatId(int patId) {
        this.patId = patId;
    }

    public String getPatFileId() {
        return patFileId;
    }

    public void setPatFileId(String patFileId) {
        this.patFileId = patFileId;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getCall() {
        return call;
    }

    public void setCall(int call) {
        this.call = call;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

}
