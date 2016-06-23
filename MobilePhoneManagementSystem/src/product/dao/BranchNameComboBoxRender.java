/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.dao;

import branch.model.Branch;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author tuan
 */
public class BranchNameComboBoxRender extends JLabel implements ListCellRenderer<Branch> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Branch> list, Branch value, int index, boolean isSelected, boolean cellHasFocus) {
        
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(Color.PINK);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        
        setText(value.getBraName() + "");
       
        return this;
    }
    
}
