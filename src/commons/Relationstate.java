/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author DELL
 */
public class Relationstate {

    private final HashMap<String, Integer> RELMAP = new HashMap<>();

    public Relationstate() {
        RELMAP.put("اعزب/ باكر", 1);
        RELMAP.put("متزوج", 2);
        RELMAP.put("مطلق", 3);
        RELMAP.put("ارملة", 4);
        RELMAP.put("طفل", 5);
    }

    public int getRelationNo(String relname) {
        return RELMAP.get(relname);
    }

    public String getRelationName(int relNo) {
        for (Map.Entry<String, Integer> entry : RELMAP.entrySet()) {
            if (entry.getValue() == relNo) {
                return entry.getKey();
            }
        }
        return "";
    }

    public ArrayList<String> getallState() {
        return new ArrayList<>(RELMAP.keySet());
    }
}
