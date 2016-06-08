/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesoff.model;

/**
 *
 * @author Hoang
 */
public class ProductOfSales {
    int proID;
    String braName;
    String proName;
    boolean salesOff;

    public ProductOfSales() {
    }

    public ProductOfSales(int proID, String braName, String proName, boolean salesOff) {
        this.proID = proID;
        this.braName = braName;
        this.proName = proName;
        this.salesOff = salesOff;
    }
    
    // Getters
    public int getProID() {
        return proID;
    }

    public String getBraName() {
        return braName;
    }

    public String getProName() {
        return proName;
    }

    public boolean isSalesOff() {
        return salesOff;
    }
    
    // Setters
    public void setProID(int proID) {
        this.proID = proID;
    }

    public void setBraName(String braName) {
        this.braName = braName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public void setSalesOff(boolean salesOff) {
        this.salesOff = salesOff;
    }
}
