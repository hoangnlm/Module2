package employee.model;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * Implements a cell editor that uses a JDateChooser to edit Date values.
 */
public class DateCellBirthDayEditor extends DefaultCellEditor {

    private JDateChooser dc;

    public DateCellBirthDayEditor() {
        super(new JTextField());
        dc = new JDateChooser();
        Calendar c = Calendar.getInstance();
        c.set(1945, 11, 31);
        dc.getJCalendar().setMinSelectableDate(c.getTime());
        c.set(1999, 11, 31);
        dc.getJCalendar().setMaxSelectableDate(c.getTime());
        dc.getDateEditor().setEnabled(false);
    }

// Override to ensure that the value remains an Integer.
    @Override
    public Object getCellEditorValue() {
        return dc.getDate();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        dc.setDate((Date) value);
        return dc;
    }
}
