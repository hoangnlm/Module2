/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.model;
import inbound.model.*;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import static javax.swing.SwingConstants.CENTER;
/**
 *
 * @author tuan
 */
public class UserNameComboboxRenderer extends JLabel implements ListCellRenderer<User> {
    
    public UserNameComboboxRenderer(){
        setHorizontalAlignment(CENTER);
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends User> list, User value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value!=null){
        if (isSelected) {
            setBackground(Color.ORANGE);
            setForeground(Color.ORANGE);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        setText(value.getUsername()+ "");
        
        }
        return this;
    }
    
    
}
