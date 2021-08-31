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
public class Permisions {

    public static final int ALLOW = 1;
    public static final int DENY = 0;
    private int userId;

    private int reception;
    private int doctor;
    private int death_note;
    private int chemo_check;
    private int clinical_pharmacy;
    private int prepare_drug;
    private int black_list;
    private int drug_dose;
    private int pat_management;
    private int pat_cost;
    private int lab;
    private int doc_screen;
    private int lab_screen;
    private int pharmacy_screen;
    private int search_for_pat;
    private int serach_for_drug;
    private int user_manage;

    public Permisions() {
    }

    public Permisions(int userId, int reception, int doctor, int death_note, int chemo_check, int clinical_pharmacy, int prepare_drug, int black_list, int drug_dose, int pat_management, int pat_cost, int lab, int doc_screen, int lab_screen, int pharmacy_screen, int search_for_pat, int serach_for_drug, int user_manage, int drug, int lab_test, int dia_manage, int region_manage, int pref_manage, int DelData, int is_admin) {
        this.userId = userId;
        this.reception = reception;
        this.doctor = doctor;
        this.death_note = death_note;
        this.chemo_check = chemo_check;
        this.clinical_pharmacy = clinical_pharmacy;
        this.prepare_drug = prepare_drug;
        this.black_list = black_list;
        this.drug_dose = drug_dose;
        this.pat_management = pat_management;
        this.pat_cost = pat_cost;
        this.lab = lab;
        this.doc_screen = doc_screen;
        this.lab_screen = lab_screen;
        this.pharmacy_screen = pharmacy_screen;
        this.search_for_pat = search_for_pat;
        this.serach_for_drug = serach_for_drug;
        this.user_manage = user_manage;
        this.drug = drug;
        this.lab_test = lab_test;
        this.dia_manage = dia_manage;
        this.region_manage = region_manage;
        this.pref_manage = pref_manage;
        this.DelData = DelData;
        this.is_admin = is_admin;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReception() {
        return reception;
    }

    public void setReception(int reception) {
        this.reception = reception;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    public int getDeath_note() {
        return death_note;
    }

    public void setDeath_note(int death_note) {
        this.death_note = death_note;
    }

    public int getChemo_check() {
        return chemo_check;
    }

    public void setChemo_check(int chemo_check) {
        this.chemo_check = chemo_check;
    }

    public int getClinical_pharmacy() {
        return clinical_pharmacy;
    }

    public void setClinical_pharmacy(int clinical_pharmacy) {
        this.clinical_pharmacy = clinical_pharmacy;
    }

    public int getPrepare_drug() {
        return prepare_drug;
    }

    public void setPrepare_drug(int prepare_drug) {
        this.prepare_drug = prepare_drug;
    }

    public int getBlack_list() {
        return black_list;
    }

    public void setBlack_list(int black_list) {
        this.black_list = black_list;
    }

    public int getDrug_dose() {
        return drug_dose;
    }

    public void setDrug_dose(int drug_dose) {
        this.drug_dose = drug_dose;
    }

    public int getPat_management() {
        return pat_management;
    }

    public void setPat_management(int pat_management) {
        this.pat_management = pat_management;
    }

    public int getPat_cost() {
        return pat_cost;
    }

    public void setPat_cost(int pat_cost) {
        this.pat_cost = pat_cost;
    }

    public int getLab() {
        return lab;
    }

    public void setLab(int lab) {
        this.lab = lab;
    }

    public int getDoc_screen() {
        return doc_screen;
    }

    public void setDoc_screen(int doc_screen) {
        this.doc_screen = doc_screen;
    }

    public int getLab_screen() {
        return lab_screen;
    }

    public void setLab_screen(int lab_screen) {
        this.lab_screen = lab_screen;
    }

    public int getPharmacy_screen() {
        return pharmacy_screen;
    }

    public void setPharmacy_screen(int pharmacy_screen) {
        this.pharmacy_screen = pharmacy_screen;
    }

    public int getSearch_for_pat() {
        return search_for_pat;
    }

    public void setSearch_for_pat(int search_for_pat) {
        this.search_for_pat = search_for_pat;
    }

    public int getSerach_for_drug() {
        return serach_for_drug;
    }

    public void setSerach_for_drug(int serach_for_drug) {
        this.serach_for_drug = serach_for_drug;
    }

    public int getUser_manage() {
        return user_manage;
    }

    public void setUser_manage(int user_manage) {
        this.user_manage = user_manage;
    }

    public int getDrug() {
        return drug;
    }

    public void setDrug(int drug) {
        this.drug = drug;
    }

    public int getLab_test() {
        return lab_test;
    }

    public void setLab_test(int lab_test) {
        this.lab_test = lab_test;
    }

    public int getDia_manage() {
        return dia_manage;
    }

    public void setDia_manage(int dia_manage) {
        this.dia_manage = dia_manage;
    }

    public int getRegion_manage() {
        return region_manage;
    }

    public void setRegion_manage(int region_manage) {
        this.region_manage = region_manage;
    }

    public int getPref_manage() {
        return pref_manage;
    }

    public void setPref_manage(int pref_manage) {
        this.pref_manage = pref_manage;
    }

    public int getDelData() {
        return DelData;
    }

    public void setDelData(int DelData) {
        this.DelData = DelData;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }
    private int drug;
    private int lab_test;
    private int dia_manage;
    private int region_manage;
    private int pref_manage;
    private int DelData;
    private int is_admin;

}
