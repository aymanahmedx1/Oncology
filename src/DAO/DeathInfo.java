/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Date;

/**
 *
 * @author DELL
 */
public class DeathInfo {

    public DeathInfo(int id, String note, Date date) {
        this.id = id;
        this.note = note;
        this.date = date;
    }

    public DeathInfo() {
    }

    
    
    
    
    private int id;
    private String note;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
