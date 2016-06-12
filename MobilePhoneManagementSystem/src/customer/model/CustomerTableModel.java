/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.model;

import customer.dao.CustomerDAOImpl;
import java.util.List;
import javax.swing.table.AbstractTableModel;

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

    public boolean insert(Customer customer) {
        boolean result = false;
        if (customerDAOImpl.insert(customer)) {
            customerList = customerDAOImpl.getList();
            fireTableRowsInserted(customerList.indexOf(customer), customerList.indexOf(customer));
            result = true;
        }
        return result;
    }

    public boolean update(Customer customer) {
        boolean result = false;
        if (customerDAOImpl.update(customer)) {
            customerList = customerDAOImpl.getList();
            fireTableRowsUpdated(customerList.indexOf(customer), customerList.indexOf(customer));
            result = true;
        }
        return result;
    }

    public boolean delete(Customer customer) {
        boolean result = false;
        if (customerDAOImpl.delete(customer)) {
            customerList = customerDAOImpl.getList();
            fireTableRowsDeleted(customerList.indexOf(customer), customerList.indexOf(customer));
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

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Customer customer = customerList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                customer.setCusID((int) aValue);
                break;
            case 1:
                customer.setCusName((String) aValue);
                break;
            case 2:
                customer.setCusLevel(((CustomerLevel) aValue).getCusLevel());
                break;
            case 3:
                customer.setCusPhone((String) aValue);
                break;
            case 4:
                customer.setCusAddress((String) aValue);
                break;
            case 5:
                customer.setCusEnabled((boolean) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
