/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import customer.model.Customer;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import database.DBProvider;

/**
 *
 * @author Hoang
 */
public interface IDAO<T> {
    List<T> getList();
    T get(int position);
    boolean insert(Customer customer);
    boolean update(Customer customer);
    boolean delete(Customer customer);
    
    default CachedRowSet getCRS(String query){
        return new DBProvider().getCRS(query);
    }
}
