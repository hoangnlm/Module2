/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order;

/**
 *
 * @author Hoang
 */
public class Customer {

    private int cusID;
    private String cusName;
    private String cusAddress;
    private String cusPhone;
    private String cusLevelID;
    private float cusDiscount;
    private boolean cusEnabled;

    public Customer() {
    }

    public Customer(int cusID, String cusName, String cusAddress, String cusPhone, String cusLevelID, float cusDiscount, boolean cusEnabled) {
        this.cusID = cusID;
        this.cusName = cusName;
        this.cusAddress = cusAddress;
        this.cusPhone = cusPhone;
        this.cusLevelID = cusLevelID;
        this.cusDiscount = cusDiscount;
        this.cusEnabled = cusEnabled;
    }

    // Getters
    public int getCusID() {
        return cusID;
    }

    public String getCusName() {
        return cusName;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public String getCusLevelID() {
        return cusLevelID;
    }

    public float getCusDiscount() {
        return cusDiscount;
    }

    public boolean isCusEnabled() {
        return cusEnabled;
    }

    // Setters
    public void setCusID(int cusID) {
        this.cusID = cusID;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public void setCusLevelID(String cusLevelID) {
        this.cusLevelID = cusLevelID;
    }

    public void setCusDiscount(float cusDiscount) {
        this.cusDiscount = cusDiscount;
    }

    public void setCusEnabled(boolean cusEnabled) {
        this.cusEnabled = cusEnabled;
    }
}
