/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.dao;

import customer.model.Customer;
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
public class CustomerDAOImpl implements IDAO<Customer> {

    private CachedRowSet dbCrs;           //CRS for update database
    private final CachedRowSet tableCrs;  //CRS for update table

    public static void main(String[] args) {
        CustomerDAOImpl customerDAOImpl = new CustomerDAOImpl();
        customerDAOImpl.insert(new Customer(10, "hoang2", 3, "1232323", "ly te xuyen", true, 1));
    }

    public CustomerDAOImpl() {
        tableCrs = getCRS(Customer.QUERY_SHOW);
    }

    @Override
    public List<Customer> getList() {
        List<Customer> customerList = new ArrayList<>();
        try {
            if (tableCrs.first()) {
                do {
                    customerList.add(new Customer(
                            tableCrs.getInt(Customer.COL_CUSID),
                            tableCrs.getString(Customer.COL_CUSNAME),
                            tableCrs.getInt(Customer.COL_CUSLEVEL),
                            tableCrs.getString(Customer.COL_CUSPHONE),
                            tableCrs.getString(Customer.COL_CUSADDRESS),
                            tableCrs.getBoolean(Customer.COL_CUSENABLED),
                            tableCrs.getInt(Customer.COL_CUSLEVELID)));
                } while (tableCrs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerList;
    }

    @Override
    public Customer get(int position) {

        return null;
    }

    @Override
    public boolean insert(Customer customer) {
        boolean result = false;
        dbCrs = getCRS("insert into Customers values(?,?,?,?,?)");
        try {
            dbCrs.setString(1, customer.getCusName());
            dbCrs.setString(2, customer.getCusAddress());
            dbCrs.setString(3, customer.getCusPhone());
            dbCrs.setInt(4, customer.getCusLevelID());
            dbCrs.setBoolean(5, customer.isCusEnabled());
            dbCrs.execute();

            // Refresh lai cachedrowset hien thi table
            tableCrs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Customer customer) {
        boolean result = false;

        dbCrs = getCRS("update Customers set "
                + Customer.COL_CUSNAME + "=?, "
                + Customer.COL_CUSADDRESS + "=?, "
                + Customer.COL_CUSPHONE + "=?, "
                + Customer.COL_CUSLEVELID + "=?, "
                + Customer.COL_CUSENABLED + "=? "
                + "where " + Customer.COL_CUSID + "=" + customer.getCusID());

        try {
            dbCrs.setString(1, customer.getCusName());
            dbCrs.setString(2, customer.getCusAddress());
            dbCrs.setString(3, customer.getCusPhone());
            dbCrs.setInt(4, customer.getCusLevelID());
            dbCrs.setBoolean(5, customer.isCusEnabled());
            dbCrs.execute();
            tableCrs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean delete(Customer customer) {
        boolean result = false;
        //Check customer co order khong, neu co thi khong cho delete
        dbCrs = getCRS("select * from Orders where CusID=" + customer.getCusID());
        try {
            if (!dbCrs.first()) {
                dbCrs = getCRS("delete from Customers where CusID=" + customer.getCusID());
                tableCrs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
