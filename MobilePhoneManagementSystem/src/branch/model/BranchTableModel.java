/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branch.model;

import branch.dao.BranchDAOImpl;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author tuan
 */
public class BranchTableModel extends AbstractTableModel {
    private List<Branch> branchList;
    private Branch branch;
    private BranchDAOImpl branchDAOImpl;
    private String[] columnNames;
    
    public BranchTableModel(){
        branchDAOImpl = new BranchDAOImpl();
        branchList = branchDAOImpl.getList();
        columnNames = new String[]{"Branch ID","Branch Name","Status"};
    }
    
    public boolean insert(Branch branch) {
        boolean result = false;
        if (branchDAOImpl.insert(branch)) {
            branchList = branchDAOImpl.getList();
            fireTableRowsInserted(branchList.indexOf(branch), branchList.indexOf(branch));
            result = true;
        }
        return result;
    }
    
     public boolean update(Branch branch) {
        boolean result = false;
        if (branchDAOImpl.update(branch)) {
            branchList = branchDAOImpl.getList();
            
            result = true;
        }
        return result;
    }
    
    public boolean delete(Branch branch) {
        boolean result = false;
        if (branchDAOImpl.delete(branch)) {
            branchList = branchDAOImpl.getList();
            fireTableRowsDeleted(branchList.indexOf(branch), branchList.indexOf(branch));
            result = true;
        }
        return result;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
         if (columnIndex == 0) { //Khong cho sua column ID
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class[] columnClasses = {Integer.class, String.class, Boolean.class};
        return columnClasses[columnIndex];
    }
    @Override
    public int getRowCount() {
        return branchList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         branch = branchList.get(rowIndex);
         switch(columnIndex){
             case 0 : return branch.getBraID();
             case 1 : return branch.getBraName();
             case 2 : return branch.getBraStatus();
         }
         return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
         Branch branch = branchList.get(rowIndex);
         switch(columnIndex){
             case 0 : branch.setBraID((int) aValue); break;
             case 1 : branch.setBraName((String) aValue); break;
             case 2 : branch.setBraStatus((boolean) aValue); break;
         }
         fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    
    
}
