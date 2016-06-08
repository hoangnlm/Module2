package utility;

import javax.swing.JOptionPane;

public class SwingUtils {
    public static final String INSERT_SUCCESS = "Added new successfully!";
    public static final String INSERT_FAIL = "Added new failed!";
    public static final String UPDATE_SUCCESS = "Updated successfully!";
    public static final String UPDATE_FAIL = "Updated failed!";
    public static final String DELETE_SUCCESS = "Deleted successfully!";
    public static final String DELETE_FAIL = "Deleted failed!";
    
	public static int showConfirmDialog(String message) {
		return JOptionPane.showConfirmDialog(null, message, "Confirm:", JOptionPane.YES_NO_OPTION);
	}
	
	public static void showMessageDialog(String message){
		JOptionPane.showMessageDialog(null, message, "Info:", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error:", JOptionPane.ERROR_MESSAGE);
	}

	public static void showErrorDialog(Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "Error:", JOptionPane.ERROR_MESSAGE);
	}
	
}
