/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Define order class for model
 * 
 * @author Hoang
 */
public class Order {
    private int ordID;
    private Date ordDate;
    private String ordStatusID;
    private String ordStatus;
    
    private int cusID;
    private String cusName;
    private float cusDiscount;
    
    private int userID;
    private String userName;
    
    private List<OrderDetails> orderDetailsList;

    public Order() {
        orderDetailsList = new ArrayList<>();
    }
    
    // Getters
    public int getOrdID() {
        return ordID;
    }

    public Date getOrdDate() {
        return ordDate;
    }

    public String getOrdStatusID() {
        return ordStatusID;
    }

    public String getOrdStatus() {
        return ordStatus;
    }

    public int getCusID() {
        return cusID;
    }

    public String getCusName() {
        return cusName;
    }

    public float getCusDiscount() {
        return cusDiscount;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public List<OrderDetails> getOrderDetailsList() {
        return orderDetailsList;
    }
    
    // Setters
    public void setOrdID(int ordID) {
        this.ordID = ordID;
    }

    public void setOrdDate(Date ordDate) {
        this.ordDate = ordDate;
    }

    public void setOrdStatusID(String ordStatusID) {
        this.ordStatusID = ordStatusID;
    }

    public void setOrdStatus(String ordStatus) {
        this.ordStatus = ordStatus;
    }

    public void setCusID(int cusID) {
        this.cusID = cusID;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public void setCusDiscount(float cusDiscount) {
        this.cusDiscount = cusDiscount;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setOrderDetailsList(List<OrderDetails> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }
}
