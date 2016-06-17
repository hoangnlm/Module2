package order.controller;

import customer.controller.*;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import order.model.OrderProduct;

public class OrderProductComboBoxCellEditor extends DefaultCellEditor {

    private JComboBox cb;

    public OrderProductComboBoxCellEditor(OrderProductComboBoxModel orderProductComboBoxModel) {
        super(new JComboBox(orderProductComboBoxModel));
        cb = (JComboBox) getComponent();
        cb.setRenderer(new CustomerLevelComboBoxRenderer());
    }

    // Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox cb2 = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        cb2.setSelectedItem(((OrderProductComboBoxModel) cb2.getModel()).getProductFromID(Integer.parseInt((String) value)));
//        cb2.setSelectedItem((OrderProduct) value);
        return cb2;
    }
}
