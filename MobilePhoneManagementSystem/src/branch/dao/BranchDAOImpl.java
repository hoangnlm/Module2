/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branch.dao;

import branch.model.Branch;
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
 * @author tuan
 */
public class BranchDAOImpl implements IDAO<Branch> {
    private CachedRowSet dbCrs;           //CRS for update database
    private final CachedRowSet tableCrs;  //CRS for update table
    public BranchDAOImpl(){
        tableCrs = getCRS(Branch.Query_Show);
    }

    @Override
    public List<Branch> getList() {
        List<Branch> branchList = new ArrayList<>();
        try {
            
            if(tableCrs.first()){
                do{
                branchList.add(new Branch(
                tableCrs.getInt(Branch.COL_ID),
                tableCrs.getString(Branch.COL_Name),
                tableCrs.getBoolean(Branch.COL_Status)
                ));
                }while(tableCrs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(BranchDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return branchList;
    }

   

    @Override
    public boolean insert(Branch branch) {
        boolean result = false;
        dbCrs = getCRS(Branch.Query_Insert);
        try {
        
            dbCrs.setString(1,branch.getBraName());
            dbCrs.setBoolean(2, branch.getBraStatus());
            dbCrs.execute();
            
            // Refresh lai cachedrowset hien thi table
            tableCrs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(BranchDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(Branch branch) {
        boolean result = false;
        
        try {
            CachedRowSet crs1 = getCRS("SELECT * from Branches where braName = "+"'"+branch.getBraName()+"'");
            if (crs1.first()) {
                SwingUtils.showErrorDialog("Branch name cannot be duplicated !");
            } 
            dbCrs = getCRS("Update Branches set BraName = ?,BraEnabled = ? Where BraId = "+branch.getBraID());
            dbCrs.setString(1,branch.getBraName());
            dbCrs.setBoolean(2, branch.getBraStatus());
 
            dbCrs.execute();
            tableCrs.execute();
            result = true;
            } catch (SQLException ex) {
            Logger.getLogger(BranchDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return result;
    }
    

    @Override
    public boolean delete(Branch branch) {
        boolean result = false;
        dbCrs = getCRS(Branch.Query_Delete);
        try {
            if (!dbCrs.first()) {
                tableCrs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BranchDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
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
