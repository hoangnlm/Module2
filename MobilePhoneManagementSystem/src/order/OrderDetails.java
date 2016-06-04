/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order;

/**
 * Define order details class for model
 *
 * @author Hoang
 */
public class OrderDetails {

    private int ordID;
    private int ordDetailsID;
    private int proID;
    private String proName;
    private int proQty;
    private float proPrice;

    public OrderDetails() {
    }
    
    public OrderDetails(int ordID, int ordDetailsID, int proID, String proName, int proQty, float proPrice) {
        this.ordID = ordID;
        this.ordDetailsID = ordDetailsID;
        this.proID = proID;
        this.proName = proName;
        this.proQty = proQty;
        this.proPrice = proPrice;
    }
    
    // Getters
    public int getOrdID() {
        return ordID;
    }

    public int getOrdDetailsID() {
        return ordDetailsID;
    }

    public int getProID() {
        return proID;
    }

    public String getProName() {
        return proName;
    }

    public int getProQty() {
        return proQty;
    }

    public float getProPrice() {
        return proPrice;
    }

    // Setters
    public void setOrdID(int ordID) {
        this.ordID = ordID;
    }

    public void setOrdDetailsID(int ordDetailsID) {
        this.ordDetailsID = ordDetailsID;
    }

    public void setProID(int proID) {
        this.proID = proID;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public void setProQty(int proQty) {
        this.proQty = proQty;
    }

    public void setProPrice(float proPrice) {
        this.proPrice = proPrice;
    }
}
