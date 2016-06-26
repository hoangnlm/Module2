/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.model;


import database.DBProvider;
import database.IDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import utility.SwingUtils;

/**
 *
 * @author tuan
 */
public class OutboundDAOImpl implements IDAO<Outbound> {
    private CachedRowSet crs;

    public OutboundDAOImpl() {
        this.crs = getCRS("SELECT OutID,OutDate,OutContent,UserName,u.UserID from Outbounds o join Users u on u.UserID=o.UserID");
    }
    
    
    @Override
    public List<Outbound> getList() {
        List<Outbound> inboundList = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    inboundList.add(new Outbound(
                            crs.getInt(Outbound.COL_OutID),
                            crs.getDate(Outbound.COL_OutDate),
                            crs.getString(Outbound.COL_OutContent),
                            crs.getString(Outbound.COL_UserName),
                            crs.getInt(Outbound.COL_UserID)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(OutboundDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inboundList;
    }

    
    @Override
    public boolean update(Outbound outbound) {
       boolean result = false;
//        try {
//            
//                runPS("update Inbounds set InDate=?, SupID=(Select SupID from Suppliers where supName=?),SupInvoiceID=?,UserID=(SELECT UserID from users where userName=?) where InID=?",
//                        inbound.getInDate(),
//                        inbound.getSupName(),
//                        inbound.getSupInvoiceID(),
//                        inbound.getUserName(),
//                        inbound.getInID()
//                );
//
//                // Refresh lai cachedrowset hien thi table
//                crs.execute();
//                result = true;
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(OutboundDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return result;
    }

    @Override
    public boolean delete(Outbound outbound) {
        boolean result = false;
        try {
            runPS("delete from OutDetails where OutID=?",outbound.getOutID());
            runPS("delete from Outbounds where OutID=?",outbound.getOutID() );
            crs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(OutboundDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean insert(Outbound model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
