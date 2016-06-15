/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package salesoff.model;

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
public class SalesOffDAOImpl implements IDAO<SalesOff> {

    private CachedRowSet crs;  //CRS to update table
    private int filter;

    public SalesOffDAOImpl() {
        crs = getCRS("select * from SalesOff");
    }

    @Override
    public List<SalesOff> getList() {
        List<SalesOff> list = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    list.add(new SalesOff(
                            crs.getInt(SalesOff.COL_SALEID),
                            crs.getString(SalesOff.COL_SALENAME),
                            crs.getDate(SalesOff.COL_SALESTART),
                            crs.getDate(SalesOff.COL_SALEEND),
                            crs.getFloat(SalesOff.COL_SALEAMOUNT)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesOffDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(SalesOff salesOff) {
        boolean result = false;
        List<SalesOff> list = getList();

        // Check duplicate level
//        int tmpLevel = 0;   // Toi thieu bang 1
//        if (list.size() > 0) {  // Neu table khong rong
//            for (int i = CustomerLevel.MIN_LEVEL; i <= CustomerLevel.MAX_LEVEL; i++) {
//                boolean found = false;
//                // Tim phan tu i trong mang list, neu tim thay thi thoat
//                for (SalesOff cl : list) {
//                    int level = cl.getCusLevel();
//                    if (i == level) {
//                        found = true;
//                        break;
//                    }
//                }
//                if (!found) { //Co nghia la khong duplicate
//                    tmpLevel = i;
//                    break;
//                }
//            }
//        } else {
//            tmpLevel = 1;
//        }
//
//        // Check duplicate discount
//        int tmpDiscount = -1;   // Toi thieu bang 0
//        if (list.size() > 0) {
//            for (int i = CustomerLevel.MIN_DISCOUNT; i <= CustomerLevel.MAX_DISCOUNT; i++) {
//                boolean found = false;
//                // Tim phan tu i trong mang list, neu tim thay thi thoat
//                for (CustomerLevel cl : list) {
//                    int discount = (int) (cl.getCusDiscount() * 100);
//                    if (i == discount) {
//                        found = true;
//                        break;
//                    }
//                }
//                if (!found) { //Co nghia la khong duplicate
//                    tmpDiscount = i;
//                    break;
//                }
//            }
//        } else {
//            tmpDiscount = 0;
//        }
//
//        if (tmpLevel == 0) { // Toan bo level da dung het
//            SwingUtils.showErrorDialog("Customer level has reached maximum level !");
//        } else if (tmpDiscount == -1) { // Toan bo discount da dung het
//            SwingUtils.showErrorDialog("Customer level has reached maximum discount !");
//        } else {
//            // Khoi tao tri default de insert vao db
//            customerLevel.setCusLevel(tmpLevel);
//            customerLevel.setCusLevelName("Level " + System.currentTimeMillis());
//            customerLevel.setCusDiscount((float) tmpDiscount / 100);
//        }
//
//        try {
//            runPS("insert CustomerLevels values(?,?,?)", customerLevel.getCusLevel(), customerLevel.getCusLevelName(), customerLevel.getCusDiscount());
//
//            // Refresh lai cachedrowset hien thi table
//            crs.execute();
//            result = true;
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerLevelDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return result;
    }

    @Override
    public boolean update(SalesOff salesOff) {
        boolean result = false;

        try {
            // Check duplicate name
            CachedRowSet crs1 = getCRS("select * from SalesOff where SalesOffName like ? AND SalesOffID!=?", salesOff.getSaleName(), salesOff.getSaleID());
            // Check duplicate amount
            CachedRowSet crs2 = getCRS("select * from SalesOff where SalesOffAmount=? AND SalesOffID!=?", salesOff.getSaleAmount(), salesOff.getSaleID());
            if (crs1.first()) {
                SwingUtils.showErrorDialog("SalesOff name cannot be duplicated !");
            } else if (crs2.first()) {
                SwingUtils.showErrorDialog("SalesOff amount cannot be duplicated !");
            } else if (salesOff.getSaleEndDate().compareTo(salesOff.getSaleStartDate()) < 0) {
                SwingUtils.showErrorDialog("End date must >= end date !");
            } else {
                runPS("update SalesOff set SalesOffName=?, SalesOffStartDate=?, SalesOffEndDate=?, SalesOffAmount=? where SalesOffID=?", salesOff.getSaleName(), salesOff.getSaleStartDate(), salesOff.getSaleEndDate(), salesOff.getSaleAmount(), salesOff.getSaleID());

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesOffDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean delete(SalesOff salesOff) {
        boolean result = false;

//        try {
//            //Check customerLevel co customer khong, neu khong thi delete
//            CachedRowSet crs1 = getCRS("select * from Customers where CusLevelID=?", customerLevel.getCusLevelID());
//            if (crs1.first()) {
//                SwingUtils.showErrorDialog("Customer level is now in use with some customer(s) !");
//            } else {
//                runPS("delete CustomerLevels where CusLevelID=?", customerLevel.getCusLevelID());
//
//                // Refresh lai cachedrowset hien thi table
//                crs.execute();
//                result = true;
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(CustomerLevelDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return result;
    }

}
