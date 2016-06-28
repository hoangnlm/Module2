/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import utility.SwingUtils;

/**
 *
 * @author BonBon
 */
public class EmployeeDAOImpl implements IDAO<Employee> {

    private CachedRowSet crs;   //CRS to update table

    public EmployeeDAOImpl() {
        crs = getCRS("select EmpID, EmpName, EmpPhone, Birthday, BasicSalary,Designation, WorkingStartDate,Bonus, EmpEnabled from Employees");

    }

    @Override
    public List<Employee> getList() {
        List<Employee> employeeList = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
//                    new Employee(0, userName, empName, empPhone, empBirthday, 0, empDes, empStartDate, 0, true)
                    employeeList.add(new Employee(
                            crs.getInt(Employee.COL_EMPID),                            
                            crs.getString(Employee.COL_EMPNAME),
                            crs.getString(Employee.COL_EMPPHONE),
                            crs.getDate(Employee.COL_EMPBIRTHDAY),
                            crs.getFloat(Employee.COL_EMPSALARY),
                            crs.getString(Employee.COL_EMPDESIGNATION),
                            crs.getDate(Employee.COL_EMPWORKSTARTDATE),
                            crs.getFloat(Employee.COL_EMPBONUS),
                            crs.getBoolean(Employee.COL_EMPENABLED)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return employeeList;
    }

    @Override
    public boolean insert(Employee employee) {
        boolean result = false;

        // Khoi tao tri default de insert vao db
        employee.setEmpName("New Employee");        
        employee.setEmpPhone(System.currentTimeMillis() + "");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        employee.setEmpBirthday(calendar.getTime());
        employee.setEmpSalary(0);
        employee.setEmpDes("New Employee");
        employee.setEmpStartDate(calendar.getTime());
        employee.setEmpBonus(0);
        employee.setEmpEnabled(true);
        try {
            runPS("insert into Employees(EmpName, EmpPhone, Birthday,BasicSalary,Designation,WorkingStartDate,"
                    + "Bonus, EmpEnabled) values(?,?,?,?,?,?,?,?)",
                    employee.getEmpName(),
                    employee.getEmpPhone(),
                    employee.getEmpBirthday(),
                    employee.getEmpSalary(),
                    employee.getEmpDes(),
                    employee.getEmpStartDate(),
                    employee.getEmpBonus(),
                    employee.isEmpEnabled()
            );

            // Refresh lai cachedrowset hien thi table
            crs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(Employee empl) {
//        System.out.println("Deo VO: "+empl.toString());
        boolean result = false;
        try {
            // Check cus phone khong duoc trung            
            CachedRowSet crs3 = getCRS("select * from Employees "
                    + "where EmpPhone = ? AND EmpID <>?", empl.getEmpPhone(),empl.getEmpID());
            
            if (crs3.first()) {
                SwingUtils.showErrorDialog("Phone cannot be duplicated !");
            } else {
                runPS("update Employees set EmpName=?, EmpPhone=?,"
                        + " Birthday=?,BasicSalary=?,Designation=?,WorkingStartDate = ?,Bonus=?, EmpEnabled=? where EmpID=?",
                        empl.getEmpName(),                        
                        empl.getEmpPhone(),
                        empl.getEmpBirthday(),
                        empl.getEmpSalary(),
                        empl.getEmpDes(),
                        empl.getEmpStartDate(),
                        empl.getEmpBonus(),
                        empl.isEmpEnabled(),
                        empl.getEmpID()
                );

                // Refresh lai cachedrowset hien thi table                
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean delete(Employee employee
    ) {
        boolean result = false;
        try {
            //Check emp co salary khong, neu co thi khong cho delete
            CachedRowSet crs1 = getCRS("select * from Salaries  where EmpID=?", employee.getEmpID());
            if (crs1.first()) {
                SwingUtils.showErrorDialog("Empployee have salary !");
            } else {
                runPS("delete from Employees where EmpID=?", employee.getEmpID());

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getSelectingIndex(int idx
    ) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx
    ) {

    }

}
