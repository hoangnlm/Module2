/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branch.model;

/**
 *
 * @author tuan
 */
public class Branch {
    private int braID;
    private String braName;
    private boolean braStatus;
    
    public static final String COL_ID = "BraID";
    public static final String COL_Name = "BraName";
    public static final String COL_Status = "BraEnabled";
    
    public static final String Query_Show = "select braid,braname,braEnabled from branches";
    public static final String Query_Insert = "insert into branches values(?,?)";
    public static final String Query_Update = "update branches set "+COL_Name+"=?"+COL_Status+"=?"+"where "+COL_ID+"=?";
    public static final String Query_Delete = "delete branches where "+COL_ID+"=?";
    
    public Branch(){
        
    }
    public Branch(int braID, String braName, boolean braStatus) {
        this.braID = braID;
        this.braName = braName;
        this.braStatus = braStatus;
    }

    public int getBraID() {
        return braID;
    }

    public String getBraName() {
        return braName;
    }
    
    public boolean getBraStatus(){
        return braStatus;
    }

    public void setBraID(int braID) {
        this.braID = braID;
    }

    public void setBraName(String braName) {
        this.braName = braName;
    }

    public void setBraStatus(boolean braStatus) {
        this.braStatus = braStatus;
    }
    
}
