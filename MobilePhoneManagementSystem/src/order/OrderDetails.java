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

    private int ordDetailsID;
    private int proID;
    private String proName;
    private int proQty;
    private float proPrice;

    // Getters
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
