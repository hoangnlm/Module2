package utility;

import java.awt.Toolkit;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class SwingUtils {

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

    public static void validateNumericInput(JTextField tf, String regex) {
        AbstractDocument abstractDocument = (AbstractDocument) tf.getDocument();
        abstractDocument.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches(regex)) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    SwingUtils.showInfoDialog("Invalid input!");
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches(regex)) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    SwingUtils.showInfoDialog("Invalid input!");
                }
            }
        });
    }

    public static void validateStringInput(JTextField tf, int maxLength, String regex) {
        AbstractDocument abstractDocument = (AbstractDocument) tf.getDocument();
        abstractDocument.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                int totalLength = fb.getDocument().getLength() + string.length();
                if (totalLength <= maxLength && string.matches(regex)) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    SwingUtils.showInfoDialog("Invalid input!");
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                int totalLength = fb.getDocument().getLength() + text.length();
                if (totalLength <= maxLength && text.matches(regex)) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    SwingUtils.showInfoDialog("Invalid input!");
                }
            }
        });
    }

    public static void validateStringValue(JTextField tf, String regex) {
        System.out.println("verifier");
        tf.setInputVerifier(new InputVerifier() {
            @Override
            public boolean shouldYieldFocus(JComponent input) {
                System.out.println("should");
                return false;
            }

            @Override
            public boolean verify(JComponent input) {
                System.out.println("verify");
                return true;
            }
        });
    }
}
