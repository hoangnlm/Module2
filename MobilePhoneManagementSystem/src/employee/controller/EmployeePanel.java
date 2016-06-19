/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee.controller;

//import utility.ComboBoxCellEditor;
import com.toedter.calendar.JDateChooser;
import employee.model.Employee;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;

import order.controller.OrderTableModel;
import order.model.Order;
import user.model.User;
import utility.DateCellEditor;
import utility.IntegerCellEditor;
import utility.StringCellEditor;
import utility.TableCellListener;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class EmployeePanel extends javax.swing.JPanel {

    private JDateChooser dcFilter;
    private JDateChooser dcFilter1;
    private EmployeeTableModel employeeTableModel;
    private UserNameComboBoxModel usernameLevelComboBoxModel1;
    private UserNameComboBoxModel usernameLevelComboBoxModel2;
    private UserNameComboBoxRenderer usernameComboBoxRenderer;
    private TableRowSorter<EmployeeTableModel> sorter;

    // Employee dang duoc chon trong table
    private Employee selectedEmployee;
    private int selectedRowIndex;
    private User filterLevel;
    private Employee filterDes;
    // Define some column constants
    private static final int COL_EMPID = 0;
     private static final int COL_USERNAME = 1;
    private static final int COL_EMPNAME = 2;   
    private static final int COL_EMPPHONE = 3;
    private static final int COL_EMPBIRTH = 4;
    private static final int COL_SALARY = 5;
    private static final int COL_EMPDES = 6;
    private static final int COL_WORKSTART = 7;
    private static final int COL_BONUS = 8;
    private static final int COL_STATUS = 9;

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Creates new form OrderPanel
     */
    public EmployeePanel() {
        initComponents();
// Set date picker len giao dien
        dcFilter = new JDateChooser();
        dcFilter.setBounds(0, 0, 110, 20);
        dcFilter.setDateFormatString("dd/MM/yyyy");
        dcFilter.setDate(null);
        pnBirthday.add(dcFilter);
        dcFilter1 = new JDateChooser();
        dcFilter1.setBounds(0, 0, 110, 20);
        dcFilter1.setDateFormatString("dd/MM/yyyy");
        dcFilter1.setDate(null);
        pnStartDate.add(dcFilter1);
        //Disable button khi moi khoi dong len
        setButtonEnabled(false);

        // Selecting customer in the table
        selectedEmployee = new Employee();

        // Set data cho combobox Username filter
        usernameLevelComboBoxModel1 = new UserNameComboBoxModel();
        filterLevel = new User(0, "All", true);
        usernameLevelComboBoxModel1.addElement(filterLevel);

        // Set data cho column employee Username combobox
        usernameLevelComboBoxModel2 = new UserNameComboBoxModel();

        // Set data cho combobox Username update
        usernameComboBoxRenderer = new UserNameComboBoxRenderer();
        cbUserNameFilter.setModel(usernameLevelComboBoxModel1);
        cbUserNameFilter.setRenderer(usernameComboBoxRenderer);

        // Set data cho table
        employeeTableModel = new EmployeeTableModel();
        tbEmpployeeList.setModel(employeeTableModel);

        // Set sorter cho table
        sorter = new TableRowSorter<>(employeeTableModel);
        tbEmpployeeList.setRowSorter(sorter);

        // Select mac dinh cho username filter
        cbUserNameFilter.setSelectedIndex(cbUserNameFilter.getItemCount() - 1);

         //Set auto define column from model to false to stop create column again
        tbEmpployeeList.setAutoCreateColumnsFromModel(false);

        // Set height cho table header
        tbEmpployeeList.getTableHeader().setPreferredSize(new Dimension(100, 30));
        //Set CellEditor cho table
        //col empid
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPID).setMaxWidth(30);
        tbEmpployeeList.getColumnModel().getColumn(COL_BONUS).setMaxWidth(50);
        tbEmpployeeList.getColumnModel().getColumn(COL_STATUS).setMaxWidth(50);
        tbEmpployeeList.getColumnModel().getColumn(COL_BONUS).setMinWidth(50);
        tbEmpployeeList.getColumnModel().getColumn(COL_STATUS).setMinWidth(30);
//        tbEmpployeeList.getColumnModel().getColumn(COL_EMPID).
        // Col emp name
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPNAME).setCellEditor(new StringCellEditor(1, 50, SwingUtils.PATTERN_CUSNAME));
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPNAME).setMinWidth(150);
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPNAME).setMaxWidth(300);
        //Col username
        tbEmpployeeList.getColumnModel().getColumn(COL_USERNAME).setCellEditor(new UserNameComboBoxCellEditor(usernameLevelComboBoxModel2));
        // Col emp phone
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPPHONE).setCellEditor(new StringCellEditor(1, 30, SwingUtils.PATTERN_CUSPHONE));
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPPHONE).setMinWidth(60);
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPPHONE).setMinWidth(60);
        //col salary
//        tbEmpployeeList.getColumnModel().getColumn(COL_SALARY).setCellEditor(new IntegerCellEditor(4000000, 2000000));
        tbEmpployeeList.getColumnModel().getColumn(COL_SALARY).setMinWidth(60);
        tbEmpployeeList.getColumnModel().getColumn(COL_SALARY).setMinWidth(60);
        // Col birth date
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPBIRTH).setCellEditor(new DateCellEditor());
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPBIRTH).setMinWidth(90);
        tbEmpployeeList.getColumnModel().getColumn(COL_EMPBIRTH).setMaxWidth(90);
        // Col work start date
        tbEmpployeeList.getColumnModel().getColumn(COL_WORKSTART).setCellEditor(new DateCellEditor());
        tbEmpployeeList.getColumnModel().getColumn(COL_WORKSTART).setMinWidth(90);
        tbEmpployeeList.getColumnModel().getColumn(COL_WORKSTART).setMaxWidth(90);

        // Bat su kien select row tren table
        tbEmpployeeList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                setButtonEnabled(true);
            } else {
                setButtonEnabled(false);
            }
        });
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Set cell listener cho updating">
        TableCellListener tcl = new TableCellListener(tbEmpployeeList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
//                System.out.println("Row   : " + tcl.getRow());
//                System.out.println("Column: " + tcl.getColumn());
//                System.out.println("Old   : " + tcl.getOldValue());
//                System.out.println("New   : " + tcl.getNewValue());
            
                switch (tcl.getColumn()) {
                    case COL_EMPNAME:
                        selectedEmployee.setEmpName((String) tcl.getNewValue());
                        break;
                    case COL_USERNAME:
                        selectedEmployee.setUserName((String) tcl.getNewValue());
                        break;
                    case COL_EMPPHONE:
                        selectedEmployee.setEmpPhone((String) tcl.getNewValue());
                        break;
                    case COL_EMPBIRTH:
                        selectedEmployee.setEmpBirthday((Date) tcl.getNewValue());
                        break;
                    case COL_WORKSTART:
                        selectedEmployee.setEmpStartDate((Date) tcl.getNewValue());
                        break;    
//                    case COL_SALARY:
//                        selectedEmployee.setEmpSalary((Float) tcl.getNewValue());
//                        break;
//                     case COL_BONUS:
//                        selectedEmployee.setEmpBonus((Float) tcl.getNewValue());
//                        break;
                    case COL_STATUS:
                        selectedEmployee.setEmpEnabled((boolean) tcl.getNewValue());
                        break;
                }
//                System.out.println("Listener: "+selectedEmployee.toString());
                updateAction();
            }
        });
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Bat su kien cho vung filter">
        tfIdFilter.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doFilter();
            }
        });
        tfCusNameFilter.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doFilter();
            }
        });
        tfCusPhoneFilter.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doFilter();
            }
        });
        tfEmpDes.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                doFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                doFilter();
            }
        });
        dcFilter.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                doFilter();
            }
        });
        dcFilter1.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                doFilter();
            }
        });
//</editor-fold>
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnFilter = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tfIdFilter = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfCusNameFilter = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbStatusFilter = new javax.swing.JComboBox<>();
        cbUserNameFilter = new javax.swing.JComboBox<>();
        tfCusPhoneFilter = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        pnBirthday = new javax.swing.JPanel();
        pnStartDate = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        tfEmpDes = new javax.swing.JTextField();
        btRemove = new javax.swing.JButton();
        btAdd = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        pnTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btSalary = new javax.swing.JButton();
        btNewOrder = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbEmpployeeList = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(790, 640));

        pnFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        jLabel2.setText("ID:");

        jLabel3.setText("Name:");
        jLabel3.setPreferredSize(new java.awt.Dimension(55, 16));

        jLabel4.setText("UserName");
        jLabel4.setPreferredSize(new java.awt.Dimension(43, 16));

        jLabel6.setText("Status:");

        cbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Enabled", "Disabled" }));
        cbStatusFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStatusFilterItemStateChanged(evt);
            }
        });

        cbUserNameFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbUserNameFilterItemStateChanged(evt);
            }
        });

        jLabel5.setText("Phone:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Refresh2.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setText("Birthday");
        jLabel7.setPreferredSize(new java.awt.Dimension(55, 16));

        jLabel8.setText("WorkStartDate");

        pnBirthday.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnBirthdayLayout = new javax.swing.GroupLayout(pnBirthday);
        pnBirthday.setLayout(pnBirthdayLayout);
        pnBirthdayLayout.setHorizontalGroup(
            pnBirthdayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 106, Short.MAX_VALUE)
        );
        pnBirthdayLayout.setVerticalGroup(
            pnBirthdayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pnStartDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnStartDateLayout = new javax.swing.GroupLayout(pnStartDate);
        pnStartDate.setLayout(pnStartDateLayout);
        pnStartDateLayout.setHorizontalGroup(
            pnStartDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnStartDateLayout.setVerticalGroup(
            pnStartDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jLabel9.setText("Designation");
        jLabel9.setPreferredSize(new java.awt.Dimension(43, 16));

        javax.swing.GroupLayout pnFilterLayout = new javax.swing.GroupLayout(pnFilter);
        pnFilter.setLayout(pnFilterLayout);
        pnFilterLayout.setHorizontalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfIdFilter)
                    .addComponent(cbUserNameFilter, 0, 89, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfCusPhoneFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(tfCusNameFilter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnBirthday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbStatusFilter, 0, 80, Short.MAX_VALUE)
                    .addComponent(tfEmpDes))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        pnFilterLayout.setVerticalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton1))
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbStatusFilter))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfEmpDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfCusNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pnBirthday, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(tfCusPhoneFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbUserNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8))))))
        );

        btRemove.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        btAdd.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Add.png"))); // NOI18N
        btAdd.setText("Add New");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btRefresh.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Refresh.png"))); // NOI18N
        btRefresh.setText("Refresh");
        btRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRefreshActionPerformed(evt);
            }
        });

        pnTitle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Employee.png"))); // NOI18N
        jLabel1.setText("<html><u><i><font color='red'>E</font>mployee <font color='red'>M</font>anagement</i></u></html>");

        btSalary.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        btSalary.setForeground(new java.awt.Color(255, 153, 0));
        btSalary.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/user/rsz_salary32.png"))); // NOI18N
        btSalary.setText("<html><u>Salary</u></html>");
        btSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalaryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnTitleLayout = new javax.swing.GroupLayout(pnTitle);
        pnTitle.setLayout(pnTitleLayout);
        pnTitleLayout.setHorizontalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
                .addComponent(btSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnTitleLayout.setVerticalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addComponent(btSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btNewOrder.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        btNewOrder.setForeground(new java.awt.Color(0, 255, 255));
        btNewOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/NewOrder.png"))); // NOI18N
        btNewOrder.setText("New User");
        btNewOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNewOrderActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Order Details"));

        tbEmpployeeList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "User Name", "Name", "Phone", "Birthday", "BasicSalary", "EmpDes", "StartDate", "Bonus", "Enabled"
            }
        ));
        tbEmpployeeList.setFillsViewportHeight(true);
        tbEmpployeeList.setRowHeight(25);
        tbEmpployeeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbEmpployeeList.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbEmpployeeList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btNewOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pnFilter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btNewOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Khai bao event">    
    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        insertAction();
    }//GEN-LAST:event_btAddActionPerformed

    private void btSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalaryActionPerformed
//        new UserNameDialog().setVisible(true);
    }//GEN-LAST:event_btSalaryActionPerformed

    private void btNewOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNewOrderActionPerformed
        
    }//GEN-LAST:event_btNewOrderActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        refreshAction(true);
    }//GEN-LAST:event_btRefreshActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        deleteAction();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void cbStatusFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStatusFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbStatusFilterItemStateChanged

    private void cbUserNameFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbUserNameFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbUserNameFilterItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        clearFilter();
    }//GEN-LAST:event_jButton1ActionPerformed
    //// </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="khai bao component">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btNewOrder;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btSalary;
    private javax.swing.JComboBox<String> cbStatusFilter;
    private javax.swing.JComboBox<User> cbUserNameFilter;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnBirthday;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JPanel pnStartDate;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JTable tbEmpployeeList;
    private javax.swing.JTextField tfCusNameFilter;
    private javax.swing.JTextField tfCusPhoneFilter;
    private javax.swing.JTextField tfEmpDes;
    private javax.swing.JTextField tfIdFilter;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="doFilter">
    private void doFilter() {
        RowFilter<EmployeeTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<EmployeeTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            filters.add(RowFilter.regexFilter("^" + tfCusNameFilter.getText(), 3));
            // Chi filter date khi date khac null
            if (dcFilter.getDate() != null) {
                // dcFilter khac voi date cua table o GIO, PHUT, GIAY nen phai dung Calendar de so sanh chi nam, thang, ngay.... oh vai~.
                // Magic here....
                RowFilter<EmployeeTableModel, Object> dateFilter = new RowFilter<EmployeeTableModel, Object>() {
                    @Override
                    public boolean include(RowFilter.Entry<? extends EmployeeTableModel, ? extends Object> entry) {
                        EmployeeTableModel model = entry.getModel();
                        Employee o = model.getEmpAtIndex((Integer) entry.getIdentifier());

                        Calendar origin = Calendar.getInstance();
                        origin.setTime(o.getEmpBirthday());

                        Calendar filter = Calendar.getInstance();
                        filter.setTime(dcFilter.getDate());

                        if (origin.get(Calendar.YEAR) == filter.get(Calendar.YEAR) && origin.get(Calendar.MONTH) == filter.get(Calendar.MONTH) && origin.get(Calendar.DATE) == filter.get(Calendar.DATE)) {
                            return true;
                        }

                        return false;
                    }
                };

                filters.add(dateFilter);
            }
            //filter workdate
            if (dcFilter1.getDate() != null) {
                // dcFilter khac voi date cua table o GIO, PHUT, GIAY nen phai dung Calendar de so sanh chi nam, thang, ngay.... oh vai~.
                // Magic here....
                RowFilter<EmployeeTableModel, Object> dateFilter = new RowFilter<EmployeeTableModel, Object>() {
                    @Override
                    public boolean include(RowFilter.Entry<? extends EmployeeTableModel, ? extends Object> entry) {
                        EmployeeTableModel model = entry.getModel();
                        Employee o = model.getEmpAtIndex((Integer) entry.getIdentifier());

                        Calendar origin = Calendar.getInstance();
                        origin.setTime(o.getEmpBirthday());

                        Calendar filter = Calendar.getInstance();
                        filter.setTime(dcFilter1.getDate());

                        if (origin.get(Calendar.YEAR) == filter.get(Calendar.YEAR) && origin.get(Calendar.MONTH) == filter.get(Calendar.MONTH) && origin.get(Calendar.DATE) == filter.get(Calendar.DATE)) {
                            return true;
                        }

                        return false;
                    }
                };

                filters.add(dateFilter);
            }
            // Neu co chon user name thi moi filter username
            if (cbUserNameFilter.getSelectedIndex() != cbUserNameFilter.getItemCount() - 1) {
                filters.add(RowFilter.regexFilter("^" + ((User) cbUserNameFilter.getSelectedItem()).getUserName(), 2));
            }

            filters.add(RowFilter.regexFilter("^" + tfCusPhoneFilter.getText(), 3));
            filters.add(RowFilter.regexFilter("^" + tfEmpDes.getText(), 3));

            // Neu status khac "All" thi moi filter
            String statusFilter = cbStatusFilter.getSelectedItem().toString();
            if (!statusFilter.equals("All")) {
                filters.add(RowFilter.regexFilter(
                        statusFilter.equals("Enabled") ? "t" : "f", 5));
            }

            rf = RowFilter.andFilter(filters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
//</editor-fold>

    private void clearFilter() {
        tfIdFilter.setText(null);
        tfCusPhoneFilter.setText(null);
        tfCusNameFilter.setText(null);
        tfEmpDes.setText(null);
        dcFilter.setDate(null);
        dcFilter1.setDate(null);
        cbUserNameFilter.setSelectedIndex(cbUserNameFilter.getItemCount() - 1);
        cbStatusFilter.setSelectedIndex(0);
    }

    private void fetchAction() {
        selectedRowIndex = tbEmpployeeList.getSelectedRow();
        selectedEmployee.setEmpID((int) tbEmpployeeList.getValueAt(selectedRowIndex, 0));
        selectedEmployee.setUserName((String) tbEmpployeeList.getValueAt(selectedRowIndex, 1));
        selectedEmployee.setEmpName((String) tbEmpployeeList.getValueAt(selectedRowIndex, 2));
        selectedEmployee.setEmpPhone((String) tbEmpployeeList.getValueAt(selectedRowIndex, 3));
        selectedEmployee.setEmpBirthday((Date) tbEmpployeeList.getValueAt(selectedRowIndex, 4));
        selectedEmployee.setEmpSalary((Float) tbEmpployeeList.getValueAt(selectedRowIndex, 5));
        selectedEmployee.setEmpDes((String) tbEmpployeeList.getValueAt(selectedRowIndex, 6));
        selectedEmployee.setEmpStartDate((Date) tbEmpployeeList.getValueAt(selectedRowIndex, 7));
        selectedEmployee.setEmpBonus((Float) tbEmpployeeList.getValueAt(selectedRowIndex, 8));
        selectedEmployee.setEmpEnabled((boolean) tbEmpployeeList.getValueAt(selectedRowIndex, 9));
//        System.out.println("fetch: "+selectedEmployee.toString());
    }

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Refresh table
            employeeTableModel.refresh();

            // Refresh combobox filter
            usernameLevelComboBoxModel1.refresh();
            usernameLevelComboBoxModel1.addElement(filterLevel);

            // Refresh combobox column table
            usernameLevelComboBoxModel2.refresh();
            setCursor(null);
            SwingUtils.showInfoDialog(SwingUtils.DB_REFRESH);
        } else {
            // Refresh table
            employeeTableModel.refresh();

            // Refresh combobox filter
            usernameLevelComboBoxModel1.refresh();
            usernameLevelComboBoxModel1.addElement(filterLevel);

            // Refresh combobox column table
            usernameLevelComboBoxModel2.refresh();
        }
        scrollToRow(selectedRowIndex);
    }

    private void insertAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = employeeTableModel.insert(new Employee());
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.INSERT_SUCCESS : SwingUtils.INSERT_FAIL);
        // Select row vua insert vao
        selectedRowIndex = tbEmpployeeList.getRowCount() - 1;
        scrollToRow(selectedRowIndex);
        tbEmpployeeList.editCellAt(tbEmpployeeList.getSelectedRow(), 1);
        tbEmpployeeList.getEditorComponent().requestFocus();
    }

    private void updateAction() {
//        System.out.println("Updateaction: "+selectedEmployee.toString());
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
        boolean result = employeeTableModel.update(selectedEmployee);
        refreshAction(false);
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.UPDATE_SUCCESS : SwingUtils.UPDATE_FAIL);
        scrollToRow(selectedRowIndex);
    }

    private void deleteAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = employeeTableModel.delete(selectedEmployee);
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.DELETE_SUCCESS : SwingUtils.DELETE_FAIL);

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbEmpployeeList.getRowCount() ? tbEmpployeeList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
    }

    private void scrollToRow(int row) {
        tbEmpployeeList.getSelectionModel().setSelectionInterval(row, row);
        tbEmpployeeList.scrollRectToVisible(new Rectangle(tbEmpployeeList.getCellRect(row, 0, true)));
    }

    private void setButtonEnabled(boolean enabled, JButton... exclude) {
        btRemove.setEnabled(enabled);
        btAdd.setEnabled(enabled);
        btNewOrder.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }
}
