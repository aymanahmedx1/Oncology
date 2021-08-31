/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO.user;

/**
 *
 * @author Ayman
 */
public class User {

    public static final int DOCTOR = 1;
    private int no;
    private int id;
    private String name;
    private String password;
    private int dept;
    private Permisions permision;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDept() {
        return dept;
    }

    public void setDept(int dept) {
        this.dept = dept;
    }

    public Permisions getPermision() {
        return permision;
    }

    public void setPermision(Permisions permision) {
        this.permision = permision;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    
}
