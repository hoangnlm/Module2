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
public class StatusDAOImpl implements IDAO<Status> {

    private CachedRowSet crs;  //CRS to update table

    public StatusDAOImpl() {
        crs = getCRS("select * from Status where SttType like 'Order'");
    }

    @Override
    public List<Status> getList() {
        List<Status> list = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    list.add(new Status(
                            crs.getInt(Status.COL_ID),
                            crs.getString(Status.COL_NAME),
                            crs.getString(Status.COL_TYPE)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatusDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(Status model) {
        return false;
    }

    @Override
    public boolean update(Status model) {
        return false;
    }

    @Override
    public boolean delete(Status model) {
        return false;
    }

}
