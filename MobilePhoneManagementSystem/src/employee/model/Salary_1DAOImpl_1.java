/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Hoang
 */
public class Salary_1DAOImpl_1 implements IDAO<Salary_1> {

    private CachedRowSet crs;  //CRS to update table
    private int selectingIndex;
    private Salary_1 currentOrder;

    /**
     * Dung load du lieu theo yeu cau, khong tu dong load
     *
     * @param ordID
     */
    public void load(int empID) {
        crs = getCRS("select Salary_1ID,EmpID,PayDay,WorkDays,OffDays from Salaries WHERE EmpID=? ", empID);
    }

    @Override
    public List<Salary_1> getList() {
        List<Salary_1> list = new ArrayList<>();
        try {
            if (crs != null && crs.first()) {
                do {
                    list.add(new Salary_1(
                            crs.getInt(Salary_1.COL_SALID),
                            crs.getInt(Salary_1.COL_EMPID),
                            (crs.getDate(Salary_1.COL_PAYDAY).getMonth() + 1),
                            crs.getDate(Salary_1.COL_PAYDAY),
                            crs.getInt(Salary_1.COL_WORKDAYS),
                            crs.getInt(Salary_1.COL_OFFDAYS)
                    ));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Salary_1DAOImpl_1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(Salary_1 salary) {
        boolean result = false;
        try {

            runPS("insert into Salaries values(?,?,?,?)", salary.getEmpID(), salary.getPayDay(), salary.getWorkDays(), salary.getOffDays());

            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(Salary_1DAOImpl_1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(Salary_1 salary) {
        boolean result = false;
        try {

            runPS("update Salaries set PayDay=?,WorkDays=?,OffDays=? WHERE Salary_1ID=?", salary.getPayDay(), salary.getWorkDays(), salary.getOffDays(), salary.getSalID());

            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(Salary_1DAOImpl_1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean update(List<Salary_1> list) {
        boolean result = false;
        if (currentOrder == null) { // Chua set current order cho DAO
            return false;
        }

        return result;
    }

    @Override
    public boolean delete(Salary_1 salary) {
        boolean result = false;
        try {
            runPS("delete Salaries  WHERE Salary_1ID=?", salary.getSalID());

            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(Salary_1DAOImpl_1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public Vector getTotalItemSale(int empID, int month) {
        Vector list = new Vector<>();
        boolean result = false;
        int soluong = 0;
        float total = 0;
        float discount = 0;
        float price = 0;
        int monthSale;
        try {

            CachedRowSet crs1, crs2, crs3;
            crs1 = getCRS("select UserID from Users WHERE EmpID=?", empID);
            if (crs1.first()) {
                int ordID = crs1.getInt("UserID");
                crs2 = getCRS("select OrdID,OrdCusDiscount,OrdDate from Orders where UserID=?", ordID);
                discount = crs2.getFloat("OrdCusDiscount");
                monthSale = crs2.getDate("OrdDate").getMonth() + 1;
                if (crs2.first()) {
                    do {
                        if (month == monthSale) {
                            crs3 = getCRS("select OrdProQty from OrderDetails where OrdID=?", crs2.getInt("OrdID"));
                            if (crs3.first()) {

                                do {
                                    int x = crs3.getInt("OrdProQty");
                                    soluong += x;
                                    float i = (float) x * discount;
                                    total += i;
                                } while (crs2.next());
                                result = true;
                            }
                        }
                    } while (crs2.first());
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Salary_1DAOImpl_1.class.getName()).log(Level.SEVERE, null, ex);
        }
        list.add(result);
        list.add(soluong);
        list.add(total);
        return list;
    }

    @Override
    public int getSelectingIndex(int idx) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx) {
        return;
    }

}
