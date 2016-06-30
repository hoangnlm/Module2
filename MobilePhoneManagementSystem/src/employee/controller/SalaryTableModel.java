package employee.controller;

import employee.model.Salary;
import employee.model.SalaryDAOImpl;
import java.util.Date;
import service.model.ServiceDetails;
import service.model.ServiceDetailsDAOImpl;
import utility.CustomizedTableModel;

/**
 *
 * @author BonBon
 */
public class SalaryTableModel extends CustomizedTableModel<Salary> {

    public SalaryTableModel() {
        super(new SalaryDAOImpl(), new String[]{"ID", "EmpID", "Month", "PayDay", "WorkDays", "OffDays"});
    }

    public Salary getSalaryFromIndex(int index) {
        return list.get(index);
    }

    public void load(int empID) {
        ((SalaryDAOImpl) super.daoImpl).load(empID);
        list = daoImpl.getList();
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex==3||columnIndex==4||columnIndex==5;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, Integer.class, Integer.class, Date.class, Integer.class, Integer.class};
        return columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getSalID();
                break;
            case 1:
                result = item.getEmpID();
                break;
            case 2:
                result = item.getMonth();
                break;
            case 3:
                result = item.getPayDay();
                break;
            case 4:
                result = item.getWorkDays();
                break;
            case 5:
                result = item.getOffDays();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                item.setSalID((int) aValue);
                break;
            case 1:
                item.setEmpID((int) aValue);
                break;
            case 2:
                item.setMonth((int) aValue);
                break;
            case 3:
                item.setPayDay((Date) aValue);
                break;
            case 4:
                item.setWorkDays((int) aValue);
                break;
            case 5:
                item.setOffDays((int) aValue);
                break;

        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
