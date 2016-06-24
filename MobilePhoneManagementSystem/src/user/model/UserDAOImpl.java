/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tuserate file, choose Tools | Tuserates
 * and open the tuserate in the editor.
 */
package user.model;

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
 * @author BonBon
 */
public class UserDAOImpl implements IDAO<User> {

    private CachedRowSet crs;   //CRS to update table

    public UserDAOImpl() {
        crs = getCRS("select u.UserID,u.UserName,u.UserPassword,e.EmpName,u.EmpID,u.UserEnabled from Users u join Employees e on u.EmpID=e.EmpID");
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
                            crs.getString(User.COL_PASSWORD),
                            crs.getString(User.COL_EMPNAME),
                            crs.getInt(User.COL_EMPID),
                            crs.getBoolean(User.COL_USERENABLE)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userList;
    }

    @Override
    public boolean insert(User user) {
        boolean result = false;
        //Lay EmpID va EmpName chua co trong user
        CachedRowSet crs1 = getCRS("select EmpID,EmpName from Employees WHERE  EmpID NOT IN (select u.EmpID from  Users u)");
        List<UserEmployee> list = new ArrayList<>();
//       

        try {
            if (!crs1.first()) {
                SwingUtils.showErrorDialog("All employees have account, can't insert user more ! Please insert more employee before");
            } else {
//                System.out.println("Co gia tri: ");
                do {
                    list.add(new UserEmployee(
                            crs1.getInt(UserEmployee.COL_ID),
                            crs1.getString(UserEmployee.COL_NAME)));
                } while (crs1.next());
                System.out.println("name:" + list.get(0).getEmpName());
                // Khoi tao tri default de insert vao db
                user.setUserName(System.currentTimeMillis() + "");
                user.setPassword("1");
                user.setEmpID(list.get(0).getEmpID());
                user.setEmpName(list.get(0).getEmpName());
                user.setUserEnable(true);
                try {
                    runPS("insert into Users(UserName,UserPassword,EmpID,UserEnabled) values(?,?,?,?)",
                            user.getUserName(),
                            user.getPassword(),
                            user.getEmpID(),
                            user.isUserEnable()
                    );

                    // Refresh lai cachedrowset hien thi table
                    crs.execute();
                    result = true;
                } catch (SQLException ex) {
                    Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(User user
    ) {
        boolean result = false;
        try {
            // Check cus phone khong duoc trung            
            CachedRowSet crs1 = getCRS("select * from Users "
                    + "where UserName = ? AND UserID <>?", user.getUserName(), user.getUserID());
            CachedRowSet crs2 = getCRS("select * from Users "
                    + "where EmpID = ? AND UserID <>?", user.getEmpID(), user.getUserID());
            if (crs1.first()) {
                SwingUtils.showErrorDialog("UserName cannot be duplicated !");
            } else if (crs2.first()) {
                SwingUtils.showErrorDialog("EmpID cannot be duplicated !");
            } else {
                System.out.println(user.toString());
                runPS("update Users set UserName=?, "
                        + " EmpID=?, UserEnabled=? where UserID=?",
                        user.getUserName(),
//                        user.getPassword(),
                        user.getEmpID(),
                        user.isUserEnable(),
                        user.getUserID()
                );

                // Refresh lai cachedrowset hien thi table                
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean delete(User user
    ) {
        boolean result = false;
        try {
            //Check emp co salary khong, neu co thi khong cho delete
            CachedRowSet crs1 = getCRS("select * from Orders  where UserID=?", user.getUserID());
            if (crs1.first()) {
                SwingUtils.showErrorDialog("Orders have user !");
            } else {
                runPS("delete from Users where UserID=?", user.getUserID());

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }

    @Override
    public int getSelectingIndex(int idx
    ) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx
    ) {

    }

}
