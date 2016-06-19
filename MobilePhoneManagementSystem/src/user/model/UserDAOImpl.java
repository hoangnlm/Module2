/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author BonBon
 */
public class UserDAOImpl implements IDAO<User>{
private CachedRowSet crs;   //CRS to update table

    public UserDAOImpl() {
        crs = getCRS("select UserID, UserName, UserEnabled from Users");
    }
    @Override
    public List<User> getList() {
        List<User> userList = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    userList.add(new User(
                            crs.getInt(User.COL_USERID),
                            crs.getString(User.COL_USERNAME),                            
                            crs.getBoolean(User.COL_USERENABLE)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userList;
    }

    @Override
    public boolean insert(User model) {
        return false;
    }

    @Override
    public boolean update(User model) {
      return false;
    }

    @Override
    public boolean delete(User model) {
       return false;
    }

    @Override
    public int getSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
