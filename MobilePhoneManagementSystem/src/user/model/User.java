/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.model;

/**
 *
 * @author BonBon
 */
public class User {
    private int userID;
    private String userName;
    private String password;
    private boolean userEnable;
    
    public static final String COL_USERID = "UserID";
    public static final String COL_USERNAME = "UserName";
    public static final String COL_USERENABLE = "UserEnabled";

    public User() {
    }

    public User(int userID, String userName, String password, boolean userEnable) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.userEnable = userEnable;
    }

    public User(int userID, String userName, boolean userEnable) {
        this.userID = userID;
        this.userName = userName;
        this.userEnable = userEnable;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isUserEnable() {
        return userEnable;
    }

    public void setUserEnable(boolean userEnable) {
        this.userEnable = userEnable;
    }
    
    
}
