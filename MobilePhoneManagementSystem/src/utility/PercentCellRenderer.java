package utility;

import java.awt.Component;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Hoang
 */
public class PercentCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        NumberFormat percentFormatter = NumberFormat.getPercentInstance();
        label.setHorizontalAlignment(CENTER);
        label.setText(percentFormatter.format((float) value));
        return label;
    }
}
