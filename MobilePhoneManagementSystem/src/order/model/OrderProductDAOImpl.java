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
public class OrderProductDAOImpl implements IDAO<OrderProduct> {

    private CachedRowSet crs;  //CRS to update table
    private int selectingIndex;

    /**
     * Dung load du lieu theo yeu cau, khong tu dong load
     *
     * @param ordID
     */
    public void load(int ordID) {
        // Chi thao tac voi product dang enable
        crs = getCRS("select p.ProID, ProName, OrdProQty, ProPrice, SalesOffAmount, OrdProPrice, s.SalesOffID, RANK() OVER(ORDER BY OrdDetailsID) ProNo, BraName, p.BraID from OrderDetails o join Products p on o.ProID=p.ProID left join SalesOff s on p.SalesOffID=s.SalesOffID join Branches b on p.BraID=b.BraID where OrdID=?", ordID);
    }

    @Override
    public List<OrderProduct> getList() {
        List<OrderProduct> list = new ArrayList<>();
        try {
            if (crs != null && crs.first()) {
                do {
                    list.add(new OrderProduct(
                            crs.getInt(OrderProduct.COL_PROID),
                            crs.getString(OrderProduct.COL_PRONAME),
                            crs.getInt(OrderProduct.COL_PROQTY),
                            crs.getFloat(OrderProduct.COL_PROPRICE1),
                            crs.getFloat(OrderProduct.COL_SALEAMOUNT),
                            crs.getFloat(OrderProduct.COL_PROPRICE2),
                            crs.getInt(OrderProduct.COL_SALEID),
                            crs.getInt(OrderProduct.COL_PRONO),
                            crs.getString(OrderProduct.COL_BRANAME),
                            crs.getInt(OrderProduct.COL_BRAID)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderProductDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(OrderProduct model) {
        return false;

    }

    @Override
    public boolean update(OrderProduct model) {
        return false;

    }

    @Override
    public boolean delete(OrderProduct model) {
        return false;
    }

    @Override
    public int getSelectingIndex(int idx) {
        return selectingIndex;
    }

    @Override
    public void setSelectingIndex(int idx) {
        selectingIndex = idx;
    }

}
