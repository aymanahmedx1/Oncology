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
public class JobState {

    private final HashMap<String, Integer> JOB_MAP = new HashMap<>();

    public JobState () {
        JOB_MAP.put("موظف", 1);
        JOB_MAP.put("كاسب", 2);
        JOB_MAP.put("متقاعد", 3);
        JOB_MAP.put("عاجز", 4);
        JOB_MAP.put("طالب", 5);
        JOB_MAP.put("ربة بيت", 6);
    }

    public int getRelationNo(String relname) {
        return JOB_MAP.get(relname);
    }

    public  String getRelationName(int relNo) {
        for (Map.Entry<String, Integer> entry : JOB_MAP.entrySet()) {
            if (entry.getValue() == relNo) {
                return entry.getKey();
            }
        }
        return "";
    }

    public ArrayList<String> getallState() {
        return new ArrayList<>(JOB_MAP.keySet());
    }
}
