package utility;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */
import customer.controller.CustomerLevelComboBoxRenderer;
import javax.swing.DefaultCellEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

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
}
