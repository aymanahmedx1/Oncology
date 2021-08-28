/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

/**
 *
 * @author Ayman
 */
public class SelectStatment {

    public static final String SELECT_STATMENT
            = " SELECT p.id ,pat.name,num.patient_id,drug.drug_name,p.dose  ,fluid.drug_name  ,p.volume  ,p.note   ,num.date ,p.is_checked , drug.main_category,num.id"
            + " FROM prescription_detail as p"
            + " LEFT JOIN prescription_no as num  ON  p.prescription_no =  num.id"
            + " LEFT JOIN patient as pat on pat.id=num.patient_id "
            + " left join drugs as drug on p.drug=drug.id "
            + " LEFT join drugs as fluid on p.fluid=fluid.id "
            + " WHERE p.date BETWEEN ? AND ? "
            + "order by p.id desc";

}
