/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorters;

import DAO.Prescription;
import java.util.Comparator;

/**
 *
 * @author DELL
 */
public class PrescriptionSort implements Comparator<Prescription> {

    @Override
    public int compare(Prescription t, Prescription t1) {
        return t.getPatientName().compareToIgnoreCase(t1.getPatientName());
    }

}
