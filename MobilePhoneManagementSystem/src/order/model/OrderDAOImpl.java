/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order.model;

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
public class OrderDAOImpl implements IDAO<Order> {

    private CachedRowSet crs;  //CRS to update table

    public OrderDAOImpl() {
        crs = getCRS("select OrdID, u.UserName, c.CusName, OrdDate, OrdCusDiscount, s.SttName, o.UserID, o.CusID, o.SttID from Orders o join Users u on o.UserID=u.UserID join Customers c on o.CusID=c.CusID join Status s on o.SttID=s.SttID");
    }

    @Override
    public List<Order> getList() {
        List<Order> list = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    list.add(new Order(
                            crs.getInt(Order.COL_ORDID),
                            crs.getString(Order.COL_USERNAME),
                            crs.getString(Order.COL_CUSNAME),
                            crs.getDate(Order.COL_ORDDATE),
                            crs.getFloat(Order.COL_CUSDISCOUNT),
                            crs.getString(Order.COL_ORDSTATUS),
                            crs.getInt(Order.COL_USERID),
                            crs.getInt(Order.COL_CUSID),
                            crs.getInt(Order.COL_ORDSTATUSID)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderStatusDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(Order model) {
        return false;
    }

    @Override
    public boolean update(Order model) {
        return false;

    }

    @Override
    public boolean delete(Order model) {
        // Neu order da thanh toan (DONE) thi khong cho xoa
        return false;

    }

    @Override
    public int getSelectingIndex(int idx) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx) {
    }

}
