package order.controller;

import java.util.ArrayList;
import java.util.List;
import order.model.Order;
import order.model.OrderProduct;
import order.model.OrderProductDAOImpl;
import utility.CustomizedTableModel;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class OrderProductTableModelDialog extends CustomizedTableModel<OrderProduct> {

    public OrderProductTableModelDialog() {
        super(new OrderProductDAOImpl(), new String[]{"No.", "Product Name", "Qty", "Price 1", "SalesOff", "Price 2"});
    }

    public List<OrderProduct> getList() {
        return list;
    }

    public OrderProduct getOrderProductFromIndex(int index) {
        return list.get(index);
    }

    public void load(int ordID) {
        ((OrderProductDAOImpl) super.daoImpl).load(ordID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }

    public boolean insert(Order order) {
        boolean result = false;
        if (checkOrderProduct()) { //Da chon product day du
            ((OrderProductDAOImpl) super.daoImpl).setCurrentOrder(order);
            if (((OrderProductDAOImpl) super.daoImpl).insert(list)) {
                result = true;
            }
        } else {
            SwingUtils.showErrorDialog("Please choose all product items !");
        }
        return result;
    }

    @Override
    public boolean insert(OrderProduct item) {
        list.add(item);
        fireTableRowsInserted(list.size() - 1, list.size() - 1);
        return true;
    }

    public boolean update(Order order) {
        boolean result = false;
        if (checkOrderProduct()) { //Da chon product day du
            ((OrderProductDAOImpl) super.daoImpl).setCurrentOrder(order);
            if (((OrderProductDAOImpl) super.daoImpl).update(list)) {
                result = true;
            }
        } else {
            SwingUtils.showErrorDialog("Please choose all product items !");
        }
        return result;
    }

    @Override
    public boolean update(OrderProduct item) {
        return false;
    }

    @Override
    public boolean delete(OrderProduct item) {
        list.remove(selectingIndex);
        fireTableRowsDeleted(selectingIndex, selectingIndex);
        return true;
    }

    public boolean checkOrderProduct() {
        // Check product name co khac empty
        // (co chon product het khong)
        List<OrderProduct> tmp = new ArrayList();
        list.stream().filter(op -> op.getProName().equals(OrderProduct.DEFAULT_PRONAME)).forEach(op -> tmp.add(op));
        return tmp.size() == 0; //Da chon product day du
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 || columnIndex == 2; //Chi sua name va qty
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
        if (item != null) {
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
                if (aValue != null) {
                    if (aValue instanceof OrderProduct) {
                        item.setProName(((OrderProduct) aValue).getProName());
                    } else {
                        item.setProName((String) aValue);
                    }
                } else {
                    item.setProName(OrderProduct.DEFAULT_PRONAME);
                }
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
