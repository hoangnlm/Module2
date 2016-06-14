package utility;

import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class SwingUtils {

    // Define some string sentences
    public static final String INSERT_SUCCESS = "Inserted successfully !";
    public static final String INSERT_FAIL = "Insertion has failed !";
    public static final String UPDATE_SUCCESS = "Updated successfully !";
    public static final String UPDATE_FAIL = "Updation has failed !";
    public static final String DELETE_SUCCESS = "Deleted successfully !";
    public static final String DELETE_FAIL = "Deletion has failed !";
    public static final String DB_REFRESH = "Data have been refreshed !";
    
    // Declare some regex constants
    public static final String PATTERN_CUSNAME = "[A-Za-z0-9 ]+";
    public static final String PATTERN_CUSPHONE = "\\d+";
    public static final String PATTERN_CUSADDRESS = "[A-Za-z0-9 .\\/-]+";

    public static int showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Confirm:", JOptionPane.YES_NO_OPTION);
    }

    public static void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Info:", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error:", JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorDialog(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Error:", JOptionPane.ERROR_MESSAGE);
    }

    public static int showInputValidationDialog(String message) {
        String[] options = {"Reinput", "Revert"};
        return JOptionPane.showOptionDialog(
                null,
                message,
                "Error:",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[1]);    //Mac dinh la NO (or Revert)
    }

    public static void validateIntegerInput(JTextField tf) {
        AbstractDocument doc = new PlainDocument() {

            private static final long serialVersionUID = 1L;

            @Override
            public void setDocumentFilter(DocumentFilter filter) {

                if (filter instanceof IntegerDocumentFilter) {
                    super.setDocumentFilter(filter);
                }
            }
        };
        doc.setDocumentFilter(new IntegerDocumentFilter());
        tf.setDocument(doc);
    }

    public static void validateStringInput(JTextField tf, int maxLength, String regex) {
        AbstractDocument abstractDocument = (AbstractDocument) tf.getDocument();
        abstractDocument.setDocumentFilter(new StringDocumentFilter(maxLength, regex));
    }

    public static class IntegerDocumentFilter extends DocumentFilter {

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches(PATTERN_CUSPHONE)) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                SwingUtils.showErrorDialog("Invalid input!");
            }
        }
    }

    public static class StringDocumentFilter extends DocumentFilter {

        private int maxLength;
        private String regex;

        public StringDocumentFilter(int maxLength, String regex) {
            this.maxLength = maxLength;
            this.regex = regex;
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches(regex)) {
                int totalLength = fb.getDocument().getLength() + text.length();
                if (totalLength <= maxLength) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    SwingUtils.showErrorDialog("Maximum length: " + maxLength + " characters!");
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
                SwingUtils.showErrorDialog("Invalid input!");
            }
        }
    }
}
