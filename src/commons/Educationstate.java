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
public class Educationstate {

    private final HashMap<String, Integer> EDU_MAP = new HashMap<>();

    public Educationstate() {
        EDU_MAP.put("ابتدائي", 1);
        EDU_MAP.put("متوسطة", 2);
        EDU_MAP.put("اعدادية", 3);
        EDU_MAP.put("دبلوم", 4);
        EDU_MAP.put("بكلوريوس", 5);
        EDU_MAP.put("ماجستير", 6);
        EDU_MAP.put("دكتوراه", 7);
        EDU_MAP.put("اميه", 8);
    }

    public  int getRelationNo(String relname) {
        return EDU_MAP.get(relname);
    }

    public String getRelationName(int relNo) {
        for (Map.Entry<String, Integer> entry : EDU_MAP.entrySet()) {
            if (entry.getValue() == relNo) {
                return entry.getKey();
            }
        }
        return "";
    }

    public ArrayList<String> getallState() {
        return new ArrayList<>(EDU_MAP.keySet());
    }
}
