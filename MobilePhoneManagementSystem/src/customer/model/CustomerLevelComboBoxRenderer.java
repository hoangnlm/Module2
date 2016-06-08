/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.model;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;

/**
 *
 * @author Hoang
 */
public class CustomerLevelComboBoxRenderer extends JLabel implements ListCellRenderer<CustomerLevel> {

    public CustomerLevelComboBoxRenderer() {
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends CustomerLevel> list, CustomerLevel value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(Color.RED);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (value != null) {
            if (value.getCusLevel() == -1) {
                setText("--");
            } else {
                setText(value.getCusLevel() + "");
            }
        }
        return this;
    }
}
