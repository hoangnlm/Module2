/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.model;

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
public class ServiceDetailsDAOImpl implements IDAO<ServiceDetails>{
    private CachedRowSet crs;  //CRS to update table
    /**
     * Dung load du lieu theo yeu cau, khong tu dong load
     *
     * @param serID
     */
    public void load(int serID) {
//    public ServiceDetailsDAOImpl(){
        crs = getCRS("select a.ServiceID,p.ProName,b.BraName,a.ServiceContent,a.ProQty,a.OrdID,a.ProID,p.BraID from ServiceDetails a join Products p on a.ProID=p.ProID left join Branches b on p.BraID=b.BraID");
    
    }
    
    @Override
    public List<ServiceDetails> getList() {
        List<ServiceDetails> list = new ArrayList<>();
        try {
            if (crs != null && crs.first()) {
                do {
                    list.add(new ServiceDetails(
                            crs.getInt(ServiceDetails.COL_ID),
                            crs.getString(ServiceDetails.COL_PRONAME),
                            crs.getString(ServiceDetails.COL_BRANCH),
                            crs.getString(ServiceDetails.COL_CONTENT),
                            crs.getInt(ServiceDetails.COL_QUANTITY),
                            crs.getInt(ServiceDetails.COL_ORDERID)!=0?crs.getInt(ServiceDetails.COL_ORDERID):0,
                            crs.getInt(ServiceDetails.COL_PROID),
                            crs.getInt(ServiceDetails.COL_BRAID)
                    ));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDetailsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(ServiceDetails model) {
        return false;
    }

    @Override
    public boolean update(ServiceDetails model) {
        return false;
    }

    @Override
    public boolean delete(ServiceDetails model) {
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
