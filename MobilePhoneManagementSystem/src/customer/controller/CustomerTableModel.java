package customer.controller;

import customer.model.CustomerDAOImpl;
import customer.model.Customer;
import customer.model.CustomerLevel;
import utility.CustomizedTableModel;

/**
 * Data model for table Customer List
 *
 * @author Hoang
 */
public class CustomerTableModel extends CustomizedTableModel<Customer> {

    public CustomerTableModel() {
        super(new CustomerDAOImpl(), new String[]{"ID", "Cus. Name", "Cus. Level", "Cus. Phone", "Cus. Address", "Status"});
    }


    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, Integer.class, String.class, String.class, Boolean.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getCusID();
                break;
            case 1:
                result = item.getCusName();
                break;
            case 2:
                result = item.getCusLevel();
                break;
            case 3:
                result = item.getCusPhone();
                break;
            case 4:
                result = item.getCusAddress();
                break;
            case 5:
                result = item.isCusEnabled();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Customer customer = list.get(rowIndex);
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
