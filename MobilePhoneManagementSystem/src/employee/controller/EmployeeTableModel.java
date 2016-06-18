package employee.controller;

import employee.model.Employee;
import employee.model.EmployeeDAOImpl;
import java.util.Date;
import user.model.User;

import utility.CustomizedTableModel;

/**
 * Data model for table Customer List
 *
 * @author Hoang
 */
public class EmployeeTableModel extends CustomizedTableModel<Employee> {

    public EmployeeTableModel() {
        super(new EmployeeDAOImpl(), new String[]{"ID", "UserName", "Name", "Phone", "Birthday", "BasicSalary", "EmpDes",
            "WorkStart", "Bonus", "Status"});
    }

    public Employee getEmpAtIndex(int index) {
        return list.get(index);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, String.class, String.class, Date.class,
            Float.class, String.class, Date.class, Float.class, Boolean.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getEmpID();
                break;
            case 1:
                result = item.getUserName();
                break;
            case 2:
                result = item.getEmpName();
                break;
            case 3:
                result = item.getEmpPhone();
                break;
            case 4:
                result = item.getEmpBirthday();
                break;
            case 5:
                result = item.getEmpSalary();
                break;
            case 6:
                result = item.getEmpDes();
                break;
            case 7:
                result = item.getEmpStartDate();
                break;
            case 8:
                result = item.getEmpBonus();
                break;
            case 9:
                result = item.isEmpEnabled();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Employee employee = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                employee.setEmpID((int) aValue);
                break;
            case 1:
                employee.setUserName(((User) aValue).getUserName());
                break;
            case 2:
                employee.setEmpName((String) aValue);
                break;
            case 3:
                employee.setEmpPhone((String) aValue);
                break;
            case 4:
                employee.setEmpBirthday((Date) aValue);
                break;
            case 5:
                employee.setEmpSalary((Float) aValue);
                break;
            case 6:
                employee.setEmpDes((String) aValue);
                break;
            case 7:
                employee.setEmpStartDate((Date) aValue);
                break;
            case 8:
                employee.setEmpBonus((Float) aValue);
                break;
            case 9:
                employee.setEmpEnabled((boolean) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
