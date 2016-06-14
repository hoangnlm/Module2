package utility;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */
import java.awt.Component;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.DefaultCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import utility.SwingUtils;

/**
 * Implements a cell editor that uses a formatted text field to edit Integer
 * values.
 */
public class SpinnerCellEditor extends DefaultCellEditor {

    private JSpinner spinner;

    public SpinnerCellEditor(int min, int max) {
        super(new JTextField());
        spinner = new JSpinner(new SpinnerNumberModel(min, min, max, 1));
        
        // Validate input
        JSpinner.NumberEditor ne = (JSpinner.NumberEditor) spinner.getEditor();
        SwingUtils.validateIntegerInput(ne.getTextField());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        spinner.setValue(value);
        return spinner;
    }

    public Object getCellEditorValue() {
        return spinner.getValue();
    }
}
