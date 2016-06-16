package order.controller;

import java.util.Date;
import order.model.Order;
import order.model.OrderDAOImpl;
import utility.CustomizedTableModel;

/**
 *
 * @author Hoang
 */
public class OrderTableModel extends CustomizedTableModel<Order> {

    public OrderTableModel() {
        super(new OrderDAOImpl(), new String[]{"ID", "User Name", "Cus. Name", "Order Date", "Discount (%)", "Status"});
    }
    
    public Order getOrderAtIndex(int index){
        return list.get(index);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, String.class, Date.class, Float.class, String.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getOrdID();
                break;
            case 1:
                result = item.getUserName();
                break;
            case 2:
                result = item.getCusName();
                break;
            case 3:
                result = item.getOrdDate();
                break;
            case 4:
                result = item.getCusDiscount()* 100;
                break;
            case 5:
                result = item.getOrdStatus();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                item.setOrdID((int) aValue);
                break;
            case 1:
                item.setUserName((String) aValue);
                break;
            case 2:
                item.setCusName((String) aValue);
                break;
            case 3:
                item.setOrdDate((Date) aValue);
                break;
            case 4:
                if (aValue instanceof Integer) {
                    item.setCusDiscount(((int) aValue) / 100f);
                } else {
                    item.setCusDiscount((float) aValue / 100);
                }
                break;
            case 5:
                item.setOrdStatus((String) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
