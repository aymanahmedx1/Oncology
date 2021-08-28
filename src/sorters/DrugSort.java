/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorters;

import DAO.Drug;
import java.util.Comparator;

/**
 *
 * @author DELL
 */
public class DrugSort implements Comparator<Drug> {

    @Override
    public int compare(Drug t, Drug t1) {
        return t.getName().compareToIgnoreCase(t1.getName());
    }

}
