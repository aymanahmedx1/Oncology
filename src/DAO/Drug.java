/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.Objects;

/**
 *
 * @author Ayman
 */
public class Drug {

    public static final int CHEMOTHERAPY = 1;
    public static final int SUPPORTIVE = 2;
    public static final int FLUID = 3;

    private int id;
    private String name;
    private int category;
    private String categoryName;
    private float stock;
    private int no;
    private int strength;
    private int strength2;
    private int strength3;
    private int defFluid;
    private int defVolume;
    private String note;
    private int noOfVials ;
    private float floatOfVials ;
    
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDefFluid() {
        return defFluid;
    }

    public void setDefFluid(int defFluid) {
        this.defFluid = defFluid;
    }

    public int getDefVolume() {
        return defVolume;
    }

    public void setDefVolume(int defVolume) {
        this.defVolume = defVolume;
    }

    public Drug() {
    }

    public Drug(int id, String name, int category, float stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.stock = stock;
    }

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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public float getStock() {
        return stock;
    }

    public void setStock(float stock) {
        this.stock = stock;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + this.id;
        hash = 19 * hash + Objects.hashCode(this.name);
        hash = 19 * hash + Objects.hashCode(this.category);
        hash = 19 * hash + Float.floatToIntBits(this.stock);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Drug other = (Drug) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        return true;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getStrength2() {
        return strength2;
    }

    public void setStrength2(int strength2) {
        this.strength2 = strength2;
    }

    public int getStrength3() {
        return strength3;
    }

    public void setStrength3(int strength3) {
        this.strength3 = strength3;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getNoOfVials() {
        return noOfVials;
    }

    public void setNoOfVials(int noOfVials) {
        this.noOfVials = noOfVials;
    }

    public float getFloatOfVials() {
        return floatOfVials;
    }

    public void setFloatOfVials(float floatOfVials) {
        this.floatOfVials = floatOfVials;
    }

}
