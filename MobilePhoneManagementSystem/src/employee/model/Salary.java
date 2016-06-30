/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee.model;

import java.util.Date;

/**
 *
 * @author BonBon
 */
public class Salary {
    
    private int salID;
    private int empID;
    private int month;
    private Date payDay;
    private int workDays;
    private int offDays;
    
    
    public static final String COL_SALID = "SalaryID";
      public static final String COL_EMPID = "EmpID";
    public static final String COL_PAYDAY = "PayDay";
    public static final String COL_WORKDAYS = "WorkDays";
    public static final String COL_OFFDAYS = "OffDays";

    public Salary() {
    }

    public Salary(int salID, int empID, int month, Date payDay, int workDays, int offDays) {
        this.salID = salID;
        this.empID = empID;
        this.month = month;
        this.payDay = payDay;
        this.workDays = workDays;
        this.offDays = offDays;
    }

    

    

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    

    public int getSalID() {
        return salID;
    }

    public void setSalID(int salID) {
        this.salID = salID;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    

    public Date getPayDay() {
        return payDay;
    }

    public void setPayDay(Date payDay) {
        this.payDay = payDay;
    }

    public int getWorkDays() {
        return workDays;
    }

    public void setWorkDays(int workDays) {
        this.workDays = workDays;
    }

    public int getOffDays() {
        return offDays;
    }

    public void setOffDays(int offDays) {
        this.offDays = offDays;
    }

    @Override
    public String toString() {
        return "Salary{" + "salID=" + salID + ", empID=" + empID + ", month=" + month + ", payDay=" + payDay + ", workDays=" + workDays + ", offDays=" + offDays + '}';
    }

    

    
    
}
