package user.controller;


import user.model.User;
import user.model.UserDAOImpl;
import utility.CustomizedTableModel;

/**
 * Data model for table Customer List
 *
 * @author Hoang
 */
public class UserTableModel extends CustomizedTableModel<User> {

    public UserTableModel() {
        super(new UserDAOImpl(), new String[]{"ID", "User Name", "Status"});
    }


    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, Boolean.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getUserID();
                break;
            case 1:
                result = item.getUserName();
                break;            
            case 2:
                result = item.isUserEnable();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        User user = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                user.setUserID((int) aValue);
                break;
            case 1:
                user.setUserName((String) aValue);
                break;
            case 2:                
                user.setUserEnable((boolean) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
