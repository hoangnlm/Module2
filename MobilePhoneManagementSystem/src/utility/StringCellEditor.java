package utility;

/*
 * IntegerEditor is used by TableFTFEditDemo.java.
 */
import java.awt.Component;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.InputVerifier;
import javax.swing.JTable;
import utility.SwingUtils;

/**
 * Implements a cell editor that uses a formatted text field to edit Integer
 * values.
 */
public class StringCellEditor extends DefaultCellEditor{

    private JFormattedTextField ftf;
    private int maxLength;
        InputVerifier verifier = null;


    public StringCellEditor(int maxLength, String regex) {
        super(new JFormattedTextField(new StringFormatter(regex)));
        ftf = (JFormattedTextField) getComponent();
        this.maxLength = maxLength;

        ftf.setHorizontalAlignment(JTextField.LEFT);
        ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);
        SwingUtils.validateStringInput(ftf, maxLength, "[A-Za-z0-9 ]+");

        //React when the user presses Enter while the editor is
        //active.  (Tab is handled as specified by
        //JFormattedTextField's focusLostBehavior property.)
        ftf.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, 0),
                "check");
        ftf.getActionMap().put("check", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ftf.isEditValid()) { //The text is invalid.
                    if (userSaysRevert()) { //reverted
                        ftf.postActionEvent(); //inform the editor
                    }
                } else {
                    try {              //The text is valid,
                        ftf.commitEdit();     //so use it.
                        ftf.postActionEvent(); //stop editing
                    } catch (java.text.ParseException exc) {
                    }
                }
            }
        });
    }

    // Override to invoke setValue on the formatted text field.
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JFormattedTextField ftf2 = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
//        ftf2.setValue(value);

        // Validate input
        SwingUtils.validateStringInput(ftf2, maxLength, "[A-Za-z0-9 ]+");
        return ftf2;
    }

    //Override to check whether the edit is valid,
    //setting the value if it is and complaining if
    //it isn't.  If it's OK for the editor to go
    //away, we need to invoke the superclass's version 
    //of this method so that everything gets cleaned up.
    @Override
    public boolean stopCellEditing() {
        JFormattedTextField ftf2 = (JFormattedTextField) getComponent();
        if (ftf2.isEditValid()) {
            try {
                ftf2.commitEdit();
            } catch (java.text.ParseException exc) {
            }

        } else //text is invalid
        {
            if (!userSaysRevert()) { //user wants to edit
                return false; //don't let the editor go away
            }
        }
        return super.stopCellEditing();
    }

    /**
     * Lets the user know that the text they entered is bad. Returns true if the
     * user elects to revert to the last good value. Otherwise, returns false,
     * indicating that the user wants to continue editing.
     *
     * @return boolean
     */
    protected boolean userSaysRevert() {
        Toolkit.getDefaultToolkit().beep();
        if (SwingUtils.showInputValidationDialog(
                "Invalid value entered.\n"
                + "You can either reinput or "
                + "revert to the last valid value.") == JOptionPane.NO_OPTION) { //Revert!
            ftf.setValue(ftf.getValue());
            return true;
        }
        ftf.selectAll();
        return false;
    }

}
