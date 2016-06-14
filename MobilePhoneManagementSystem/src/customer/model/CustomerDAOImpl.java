/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class CustomerDAOImpl implements IDAO<Customer> {

    private CachedRowSet crs;   //CRS to update table

    public CustomerDAOImpl() {
        crs = getCRS("select CusID, CusName, CusLevel, CusPhone, CusAddress, CusEnabled, a.CusLevelID from Customers a join CustomerLevels b on a.CusLevelID=b.CusLevelID");
    }

    @Override
    public List<Customer> getList() {
        List<Customer> customerList = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    customerList.add(new Customer(
                            crs.getInt(Customer.COL_CUSID),
                            crs.getString(Customer.COL_CUSNAME),
                            crs.getInt(Customer.COL_CUSLEVEL),
                            crs.getString(Customer.COL_CUSPHONE),
                            crs.getString(Customer.COL_CUSADDRESS),
                            crs.getBoolean(Customer.COL_CUSENABLED),
                            crs.getInt(Customer.COL_CUSLEVELID)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerList;
    }

    @Override
    public boolean insert(Customer customer) {
        boolean result = false;

        // Khoi tao tri default de insert vao db
        customer.setCusName("New Customer");
        customer.setCusAddress("New Address");
        customer.setCusPhone(System.currentTimeMillis() + "");
        customer.setCusLevel(1);
        customer.setCusEnabled(true);
        try {
            runPS("insert into Customers(CusName, CusLevelID, CusPhone, CusAddress, CusEnabled) values(?,(select CusLevelID from CustomerLevels where CusLevel=?),?,?,?)",
                    customer.getCusName(),
                    customer.getCusLevel(),
                    customer.getCusPhone(),
                    customer.getCusAddress(),
                    customer.isCusEnabled()
            );

            // Refresh lai cachedrowset hien thi table
            crs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(Customer customer) {
        boolean result = false;
        try {
            // Check cus phone khong duoc trung
            CachedRowSet crs2 = getCRS("select * from Customers where CusPhone=? AND CusID!=?", customer.getCusPhone(), customer.getCusID());
            if (crs2.first()) {
                SwingUtils.showErrorDialog("Customer phone cannot be duplicated !");
            } else {
                runPS("update Customers set CusName=?, CusLevelID=(select CusLevelID from CustomerLevels where CusLevel=?), CusPhone=?, CusAddress=?, CusEnabled=? where CusID=?",
                        customer.getCusName(),
                        customer.getCusLevel(),
                        customer.getCusPhone(),
                        customer.getCusAddress(),
                        customer.isCusEnabled(),
                        customer.getCusID()
                );

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean delete(Customer customer) {
        boolean result = false;
        try {
            //Check customer co order hoac service khong, neu co thi khong cho delete
            CachedRowSet crs1 = getCRS("select * from Orders where CusID=?", customer.getCusID());
            CachedRowSet crs2 = getCRS("select * from Service where CusID=?", customer.getCusID());
            if (crs1.first()) {
                SwingUtils.showErrorDialog("Customer is now in ORDER !");
            } else if (crs2.first()) {
                SwingUtils.showErrorDialog("Customer is now in SERVICE !");
            } else {
                runPS("delete from Customers where CusID=?", customer.getCusID());

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
