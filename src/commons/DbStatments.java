/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

/**
 *
 * @author DELL
 */
public class DbStatments {

    public static final String FIND_PAT_BY_KEY_WORD
            = "select p.id,p.pat_id,p.name,p.phone,p.birth,p.region,rego.name,p.entry,p.doctor,d.name,p.diagnosis ,dia.name,p.black_list ,p.icd10,p.surface,p.weight,p.gender ,"
            + "p.job , p.relation,p.education,p.add,p.height,p.motherName"
            + " from patient as p \n"
            + "LEFT join region as rego on p.region = rego.id\n"
            + " LEFT join users as d on p.doctor = d.id\n"
            + " LEFT join diagnosis as dia on p.diagnosis = dia.id "
            + "where p.name like ?  ";
    public static final String FIND_PAT_BY_PAT_ID
            = "select p.id,p.pat_id,p.name,p.phone,p.birth,p.region,rego.name,p.entry,p.doctor,d.name,p.diagnosis ,dia.name,p.black_list ,"
            + "p.icd10,p.surface,p.weight,p.gender,p.job , p.relation,p.education,p.add,p.height,p.motherName "
            + "from patient as p \n"
            + "LEFT join region as rego on p.region = rego.id\n"
            + " LEFT join users as d on p.doctor = d.id\n"
            + " LEFT join diagnosis as dia on p.diagnosis = dia.id "
            + "where p.pat_id like ?  ";
    public static final String FIND_PAT_BY_BARCODE
            = "select p.id,p.pat_id,p.name,p.phone,p.birth,p.region,rego.name,p.entry,p.doctor,d.name,p.diagnosis ,dia.name,p.black_list ,"
            + "p.icd10,p.surface,p.weight,p.gender,p.job , p.relation,p.education,p.add,p.height,p.motherName "
            + "from patient as p \n"
            + "LEFT join region as rego on p.region = rego.id\n"
            + " LEFT join users as d on p.doctor = d.id\n"
            + " LEFT join diagnosis as dia on p.diagnosis = dia.id "
            + "where p.id like ?  ";
}
