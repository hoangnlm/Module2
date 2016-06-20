package order.controller;

import java.util.Date;
import order.model.Order;
import order.model.OrderDAOImpl;
import utility.CustomizedTableModel;
import utility.SwingUtils;
import utility.SwingUtils.FormatType;

/**
 *
 * @author Hoang
 */
public class OrderTableModel extends CustomizedTableModel<Order> {

    public OrderTableModel() {
        super(new OrderDAOImpl(), new String[]{"ID", "Cus. Name", "Date", "Value", "Status", "Discount", "User Name"});
    }

    public Order getOrderAtIndex(int index) {
        return list.get(index);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, Date.class, Float.class, String.class, Float.class, String.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case OrderPanel.COL_ORDID:
                result = item.getOrdID();
                break;
            case OrderPanel.COL_ORDDATE:
                result = item.getOrdDate();
                break;
            case OrderPanel.COL_ORDVALUE:
                result = item.getOrdValue();
                break;
            case OrderPanel.COL_STATUS:
                result = item.getOrdStatus();
                break;
            case OrderPanel.COL_DISCOUNT:
                result = item.getCusDiscount();
                break;
            case OrderPanel.COL_CUSNAME:
                result = item.getCusName();
                break;
            case OrderPanel.COL_USERNAME:
                result = item.getUserName();
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
                item.setOrdDate((Date) aValue);
                break;
            case 2:
                item.setOrdValue((float) SwingUtils.unFormatString((String) aValue, FormatType.CURRENCY));
                break;
            case 3:
                item.setOrdStatus((String) aValue);
                break;
            case 4:
                item.setCusDiscount((float) SwingUtils.unFormatString((String) aValue, FormatType.PERCENT));
                break;
            case 5:
                item.setCusName((String) aValue);
                break;
            case 6:
                item.setUserName((String) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
