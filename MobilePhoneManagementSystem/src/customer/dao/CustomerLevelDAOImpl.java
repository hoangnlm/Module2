/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.dao;

import customer.model.CustomerLevel;
import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Hoang
 */
public class CustomerLevelDAOImpl implements IDAO<CustomerLevel> {

    private CachedRowSet dbCrs;           //CRS to update database
    private CachedRowSet tableCrs;  //CRS to show table

    public static void main(String[] args) {
        CustomerLevelDAOImpl customerLevelDAOImpl = new CustomerLevelDAOImpl();
//        customerLevelDAOImpl.insert(new CustomerLevel(10, "hoang2", 3, "1232323", "ly te xuyen", true, 1));
    }

    public CustomerLevelDAOImpl() {
        tableCrs = getCRS(CustomerLevel.QUERY_SHOW);
    }

    @Override
    public List<CustomerLevel> getList() {
        List<CustomerLevel> customerLevelList = new ArrayList<>();
        try {
            if (tableCrs.first()) {
                do {
                    customerLevelList.add(new CustomerLevel(
                            tableCrs.getInt(CustomerLevel.COL_CUSLEVELID),
                            tableCrs.getInt(CustomerLevel.COL_CUSLEVEL),
                            tableCrs.getString(CustomerLevel.COL_CUSLEVELNAME),
                            tableCrs.getFloat(CustomerLevel.COL_CUSDISCOUNT)));
                } while (tableCrs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerLevelDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerLevelList;
    }

    @Override
    public CustomerLevel get(int position) {

        return null;
    }

    @Override
    public boolean insert(CustomerLevel customerLevel) {
        boolean result = false;

        try {
            // Check duplicate truoc khi insert
            dbCrs = getCRS(CustomerLevel.QUERY_CHECK_INSERT);
            dbCrs.setInt(1, customerLevel.getCusLevel());
            dbCrs.setString(2, customerLevel.getCusLevelName());
            dbCrs.setFloat(3, customerLevel.getCusDiscount());
            dbCrs.execute();
            if (!dbCrs.first()) {   //Khong co record nao
                dbCrs = getCRS(CustomerLevel.QUERY_INSERT);
                dbCrs.setInt(1, customerLevel.getCusLevel());
                dbCrs.setString(2, customerLevel.getCusLevelName());
                dbCrs.setFloat(3, customerLevel.getCusDiscount());
//                dbCrs.execute();

                // Refresh lai cachedrowset hien thi table
                tableCrs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerLevelDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(CustomerLevel customerLevel) {
        boolean result = false;

        try {
            // Check duplicate truoc khi update
            dbCrs = getCRS(CustomerLevel.QUERY_CHECK_UPDATE);
            dbCrs.setInt(1, customerLevel.getCusLevel());
            dbCrs.setString(2, customerLevel.getCusLevelName());
            dbCrs.setFloat(3, customerLevel.getCusDiscount());
            dbCrs.setInt(4, customerLevel.getCusLevelID());
            dbCrs.execute();
            if (!dbCrs.first()) {   //Khong co record nao
                dbCrs = getCRS(CustomerLevel.QUERY_UPDATE);
                dbCrs.setInt(1, customerLevel.getCusLevel());
                dbCrs.setString(2, customerLevel.getCusLevelName());
                dbCrs.setFloat(3, customerLevel.getCusDiscount());
                dbCrs.setInt(4, customerLevel.getCusLevelID());
//                dbCrs.execute();

                // Refresh lai cachedrowset hien thi table
                tableCrs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerLevelDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean delete(CustomerLevel customerLevel) {
        boolean result = false;

        try {
            //Check customerLevel co customer khong, neu khong thi delete
            dbCrs = getCRS(CustomerLevel.QUERY_CHECK_DELETE);
            dbCrs.setInt(1, customerLevel.getCusLevelID());
//            dbCrs.execute();
            if (!dbCrs.first()) {   //Khong co record nao
//                dbCrs = getCRS(CustomerLevel.QUERY_DELETE);

                // Refresh lai cachedrowset hien thi table
                tableCrs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerLevelDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
