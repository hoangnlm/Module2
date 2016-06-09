/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.model;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.swing.DefaultComboBoxModel;
import database.DBProvider;

/**
 *
 * @author Hoang
 */
public class CustomerLevelComboBoxModel extends DefaultComboBoxModel<CustomerLevel> {
    private CachedRowSet crs;

    public CustomerLevelComboBoxModel() {
        super();

        // Them hien thi khi selectedIndex = -1
        addElement(new CustomerLevel(-1,-1,""));
        
        // Them vao danh sach customer level
        crs = new DBProvider().getCRS("select * from CustomerLevels");
        try {
            if (crs.first()) {
                do {
                    addElement(new CustomerLevel(
                            crs.getInt(CustomerLevel.COL_CUSLEVELID), 
                            crs.getInt(CustomerLevel.COL_CUSLEVEL), 
                            crs.getString(CustomerLevel.COL_CUSLEVELNAME)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerLevelComboBoxModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Tim chi so cua doi tuong customer level trong list
     * 
     * @param cusLevel
     * @return index in list
     */
    public int getIndexOfCustomerLevel(int cusLevel){
        int result = -1;
        
        // Search thi bo qua phan tu dau tien
        for (int i=1; i<= crs.size(); i++) {
            if(getElementAt(i).getCusLevel()==cusLevel){
                result = i;
                break;
            }
        }
        return result;
    }
}
