package order.controller;

import order.model.OrderProduct;
import order.model.OrderProductDAOImpl;
import utility.CustomizedTableModel;

/**
 *
 * @author Hoang
 */
public class OrderProductTableModel2 extends CustomizedTableModel<OrderProduct> {

    public OrderProductTableModel2() {
        super(new OrderProductDAOImpl(), new String[]{"No.", "Product Name", "Quantity", "Price 1", "SalesOff", "Price 2"});
    }
    
    public OrderProduct getOrderProductFromIndex(int index){
        return list.get(index);
    }

    public void load(int ordID) {
        ((OrderProductDAOImpl) super.daoImpl).load(ordID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex==1 || columnIndex==2; //Chi sua name va qty
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, Integer.class, Float.class, Float.class, Float.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getProNo();
                break;
            case 1:
                result = item.getProName();
                break;
            case 2:
                result = item.getProQty();
                break;
            case 3:
                result = item.getProPrice1();
                break;
            case 4:
                result = item.getSalesOffAmount();
                break;
            case 5:
                result = item.getProPrice2();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                item.setProNo((int) aValue);
                break;
            case 1:
                item.setProName((String) aValue);
                break;
            case 2:
                item.setProQty((int) aValue);
                break;
            case 3:
                item.setProPrice1((float) aValue);
                break;
            case 4:
                item.setSalesOffAmount((float) aValue);
                break;
            case 5:
                item.setProPrice2((float) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
