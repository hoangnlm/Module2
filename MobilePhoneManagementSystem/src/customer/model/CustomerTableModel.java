/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.model;

import customer.dao.CustomerDAOImpl;
import database.DBProvider;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import utility.SwingUtils;

/**
 * Data model for table Customer List
 *
 * @author Hoang
 */
public class CustomerTableModel extends AbstractTableModel {

    private List<Customer> customerList;
    private Customer customer;
    private CustomerDAOImpl customerDAOImpl;
    private String[] columnNames;

    public CustomerTableModel() {
        customerDAOImpl = new CustomerDAOImpl();
        customerList = customerDAOImpl.getList();
        columnNames = new String[]{"ID", "Cus. Name", "Cus. Level", "Cus. Phone", "Cus. Address", "Status"};
    }

    public void insert(Customer customer) {
        if (customerDAOImpl.insert(customer)) {
            customerList = customerDAOImpl.getList();
            fireTableRowsInserted(customerList.indexOf(customer), customerList.indexOf(customer));
            SwingUtils.showMessageDialog(DBProvider.INSERT_SUCCESS);
        } else {
            SwingUtils.showMessageDialog(DBProvider.INSERT_FAIL);
        }
    }

    public void update(Customer customer) {
        if (customerDAOImpl.update(customer)) {
            customerList = customerDAOImpl.getList();
            fireTableRowsUpdated(customerList.indexOf(customer), customerList.indexOf(customer));
            SwingUtils.showMessageDialog(DBProvider.UPDATE_SUCCESS);
        } else {
            SwingUtils.showMessageDialog(DBProvider.UPDATE_FAIL);
        }
    }

    public void delete(Customer customer) {
        if (customerDAOImpl.delete(customer)) {
            customerList = customerDAOImpl.getList();
            fireTableRowsDeleted(customerList.indexOf(customer), customerList.indexOf(customer));
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
        Class[] columnClasses = {Integer.class, String.class, Integer.class, String.class, String.class, Boolean.class};
        return columnClasses[column];
    }

    @Override
    public int getRowCount() {
        return customerList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        customer = customerList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return customer.getCusID();
            case 1:
                return customer.getCusName();
            case 2:
                return customer.getCusLevel();
            case 3:
                return customer.getCusPhone();
            case 4:
                return customer.getCusAddress();
            case 5:
                return customer.isCusEnabled();
        }
        return null;
    }
}
