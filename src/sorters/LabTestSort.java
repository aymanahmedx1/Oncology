/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorters;

import DAO.LabTest;
import java.util.Comparator;

/**
 *
 * @author DELL
 */
public class LabTestSort implements Comparator<LabTest> {

    @Override
    public int compare(LabTest t, LabTest t1) {
        return t.getTestName().compareToIgnoreCase(t1.getTestName());
    }

}
