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
    private float cusDiscount;

    public static final String COL_CUSLEVELID = "CusLevelID";
    public static final String COL_CUSLEVEL = "CusLevel";
    public static final String COL_CUSLEVELNAME = "CusLevelName";
    public static final String COL_CUSDISCOUNT = "CusDiscount";
    public static final String QUERY_SHOW = "select * from CustomerLevels";
    public static final String QUERY_INSERT = "insert into CustomerLevels values(?,?,?)";
    public static final String QUERY_UPDATE = "update CustomerLevels set "
            + CustomerLevel.COL_CUSLEVEL + "=?, "
            + CustomerLevel.COL_CUSLEVELNAME + "=?, "
            + CustomerLevel.COL_CUSDISCOUNT + "=? "
            + "where " + CustomerLevel.COL_CUSLEVELID + "=?";
    public static final String QUERY_DELETE = "delete from CustomerLevels where CusLevelID=?";
    public static final String QUERY_CHECK_INSERT = "select * from CustomerLevels where "
            + CustomerLevel.COL_CUSLEVEL + "=? OR "
            + CustomerLevel.COL_CUSLEVELNAME + " like ? OR "
            + CustomerLevel.COL_CUSDISCOUNT + "=?";
    public static final String QUERY_CHECK_UPDATE = "select * from CustomerLevels where "
            + "(" + CustomerLevel.COL_CUSLEVEL + "=? OR "
            + CustomerLevel.COL_CUSLEVELNAME + " like ? OR "
            + CustomerLevel.COL_CUSDISCOUNT + "=?) AND "
            + CustomerLevel.COL_CUSLEVELID + "!=?";
    public static final String QUERY_CHECK_DELETE = "select * from Customers where CusLevelID=?";

    public CustomerLevel() {
    }

    public CustomerLevel(int cusLevelID, int cusLevel, String cusLevelName, float cusDiscount) {
        this.cusLevelID = cusLevelID;
        this.cusLevel = cusLevel;
        this.cusLevelName = cusLevelName;
        this.cusDiscount = cusDiscount;
    }

    public Object getCustomerLevelFromValue(int customerLevel){
        CustomerLevel cl = null;
        
        return cl;
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

    public float getCusDiscount() {
        return cusDiscount;
    }

    public void setCusDiscount(float cusDiscount) {
        this.cusDiscount = cusDiscount;
    }

    @Override
    public String toString() {
        return "CusLevelID: "+cusLevelID+", CusLevel: "+cusLevel;
    }
}
