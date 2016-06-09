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
    boolean insert(T model);
    boolean update(T model);
    boolean delete(T model);
    
    default CachedRowSet getCRS(String query){
        return new DBProvider().getCRS(query);
    }
}
