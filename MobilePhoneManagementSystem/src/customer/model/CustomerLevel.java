/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.model;

/**
 *
 * @author Hoang
 */
public class CustomerLevel {
    private int cusLevelID;
    private int cusLevel;
    private String cusLevelName;
    
    public static final String COL_CUSLEVELID = "CusLevelID";
    public static final String COL_CUSLEVEL = "CusLevel";
    public static final String COL_CUSLEVELNAME = "CusLevelName";

    public CustomerLevel() {
    }

    public CustomerLevel(int CusLevelID, int CusLevel, String CusLevelName) {
        this.cusLevelID = CusLevelID;
        this.cusLevel = CusLevel;
        this.cusLevelName = CusLevelName;
    }

    public int getCusLevelID() {
        return cusLevelID;
    }

    public void setCusLevelID(int CusLevelID) {
        this.cusLevelID = CusLevelID;
    }

    public int getCusLevel() {
        return cusLevel;
    }

    public void setCusLevel(int CusLevel) {
        this.cusLevel = CusLevel;
    }

    public String getCusLevelName() {
        return cusLevelName;
    }

    public void setCusLevelName(String CusLevelName) {
        this.cusLevelName = CusLevelName;
    }

}
