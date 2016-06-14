package utility;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */
import customer.controller.CustomerLevelComboBoxRenderer;
import customer.controller.CustomerLevelComboBoxModel;
import java.awt.Component;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

/**
 * Implements a cell editor that uses a formatted text field to edit Integer
 * values.
 */
public class ComboBoxCellEditor extends DefaultCellEditor {

    private JComboBox cb;

    public ComboBoxCellEditor(ComboBoxModel comboBoxModel) {
        super(new JComboBox(comboBoxModel));
        cb = (JComboBox) getComponent();
        cb.setRenderer(new CustomerLevelComboBoxRenderer());
    }

    // Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JComboBox cb2 = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ComboBoxModel cbModel = cb2.getModel();

        // Neu model la customer level
        if (cbModel instanceof CustomerLevelComboBoxModel) {
            cb2.setSelectedItem(((CustomerLevelComboBoxModel) cbModel).getCustomerLevelFromValue((int) value));
        }
        return cb2;
    }
}
