/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.model;

import customer.dao.CustomerLevelDAOImpl;
import database.DBProvider;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import utility.SwingUtils;

/**
 * Data model for table CustomerLevel List
 *
 * @author Hoang
 */
public class CustomerLevelTableModel extends AbstractTableModel {

    private List<CustomerLevel> customerLevelList;
    private CustomerLevel customerLevel;
    private final CustomerLevelDAOImpl customerLevelDAOImpl;
    private String[] columnNames;

    public CustomerLevelTableModel() {
        customerLevelDAOImpl = new CustomerLevelDAOImpl();
        customerLevelList = customerLevelDAOImpl.getList();
        columnNames = new String[]{"ID", "Level", "Level Name", "Discount (%)"};
    }

    public void insert(CustomerLevel customerLevel) {
        if (customerLevelDAOImpl.insert(customerLevel)) {
            customerLevelList = customerLevelDAOImpl.getList();
            fireTableRowsInserted(customerLevelList.indexOf(customerLevel), customerLevelList.indexOf(customerLevel));
            SwingUtils.showMessageDialog(DBProvider.INSERT_SUCCESS);
        } else {
            SwingUtils.showMessageDialog(DBProvider.INSERT_FAIL);
        }
    }

    public void update(CustomerLevel customerLevel) {
        if (customerLevelDAOImpl.update(customerLevel)) {
            customerLevelList = customerLevelDAOImpl.getList();
            fireTableRowsUpdated(customerLevelList.indexOf(customerLevel), customerLevelList.indexOf(customerLevel));
            SwingUtils.showMessageDialog(DBProvider.UPDATE_SUCCESS);
        } else {
            SwingUtils.showMessageDialog(DBProvider.UPDATE_FAIL);
        }
    }

    public void delete(CustomerLevel customerLevel) {
        if (customerLevelDAOImpl.delete(customerLevel)) {
            customerLevelList = customerLevelDAOImpl.getList();
            fireTableRowsDeleted(customerLevelList.indexOf(customerLevel), customerLevelList.indexOf(customerLevel));
            SwingUtils.showMessageDialog(DBProvider.DELETE_SUCCESS);
        } else {
            SwingUtils.showMessageDialog(DBProvider.DELETE_FAIL);
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, Integer.class, String.class, Float.class};
        return columnClasses[column];
    }

    @Override
    public int getRowCount() {
        return customerLevelList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        customerLevel = customerLevelList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return customerLevel.getCusLevelID();
            case 1:
                return customerLevel.getCusLevel();
            case 2:
                return customerLevel.getCusLevelName();
            case 3:
                return customerLevel.getCusDiscount()*100;
        }
        return null;
    }
}
