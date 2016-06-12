/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.controller;

import utility.ComboBoxCellEditor;
import customer.model.Customer;
import customer.model.CustomerLevel;
import customer.model.CustomerLevelComboBoxModel;
import customer.model.CustomerTableModel;
import database.DBProvider;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import order.controller.AddOrderDialog;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class CustomerPanel_bk extends javax.swing.JPanel {

    private CustomerTableModel customerTableModel;
    private CustomerLevelComboBoxModel customerLevelComboBoxModel1;
    private CustomerLevelComboBoxModel customerLevelComboBoxModel2;
    private final CustomerLevelComboBoxRenderer customerLevelComboBoxRenderer;
    private final TableRowSorter<CustomerTableModel> sorter;

    // Text cua button thay doi khi update du lieu
    private String buttonText;

    // Flag de biet insert, update action da finish chua
    private boolean isFinished;

    // Customer dang duoc chon trong table
    private Customer selectedCustomer;
    private int selectedRowIndex;
    private CustomerLevel filterLevel;

    /**
     * Creates new form OrderPanel
     */
    public CustomerPanel_bk() {
        initComponents();

        // Selecting customer in the table
        selectedCustomer = new Customer();

        // Set data cho combobox level filter
        customerLevelComboBoxModel1 = new CustomerLevelComboBoxModel();
        filterLevel = new CustomerLevel(0, 0, "", 0);
        customerLevelComboBoxModel1.addElement(filterLevel);

        // Set data cho combobox level update
        customerLevelComboBoxModel2 = new CustomerLevelComboBoxModel();
        customerLevelComboBoxRenderer = new CustomerLevelComboBoxRenderer();
        cbLevelFilter.setModel(customerLevelComboBoxModel1);
        cbLevelFilter.setRenderer(customerLevelComboBoxRenderer);

        cbCusLevel.setModel(customerLevelComboBoxModel2);
        cbCusLevel.setRenderer(customerLevelComboBoxRenderer);

        // Set data cho table
        customerTableModel = new CustomerTableModel();
        tbCustomerList.setModel(customerTableModel);

        // Set sorter cho table
        sorter = new TableRowSorter<>(customerTableModel);
        tbCustomerList.setRowSorter(sorter);
        
        // Select mac dinh cho level filter
        cbLevelFilter.setSelectedIndex(cbLevelFilter.getItemCount()-1);

        // Col cus level
        tbCustomerList.getColumnModel().getColumn(2).setCellEditor(new ComboBoxCellEditor(new CustomerLevelComboBoxModel()));

        // Set focus and cursor to table cell
//        tbCustomerList.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusGained(FocusEvent e) {
//
//                tbCustomerList.editCellAt(tbCustomerList.getSelectedRow(), tbCustomerList.getSelectedColumn());
//                tbCustomerList.setSurrendersFocusOnKeystroke(true);
//                tbCustomerList.getEditorComponent().requestFocus();
//            }
//
//        });
        // Bat su kien select row tren table
        tbCustomerList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchCustomerDetails();
                setButtonEnabled(true);
            } else {
                resetCustomerDetails();
                setButtonEnabled(false, btRefresh);
            }
        });

        // Set height cho table header
        tbCustomerList.getTableHeader().setPreferredSize(new Dimension(100, 30));

        // Bat su kien cho vung filter
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
        tfCusAddressFilter.getDocument().addDocumentListener(
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
    }

    private void doFilter() {
        RowFilter<CustomerTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<CustomerTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            filters.add(RowFilter.regexFilter("^" + tfCusNameFilter.getText(), 1));

            // Neu co chon cus level thi moi filter level
//            if (cbLevelFilter.getSelectedIndex() != cbLevelFilter.getItemCount()-1) {
//                filters.add(RowFilter.regexFilter("^" + ((CustomerLevel) cbLevelFilter.getSelectedItem()).getCusLevel(), 2));
//            }

            filters.add(RowFilter.regexFilter("^" + tfCusPhoneFilter.getText(), 3));
            filters.add(RowFilter.regexFilter("^" + tfCusAddressFilter.getText(), 4));

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

    private void fetchCustomerDetails() {
        selectedRowIndex = tbCustomerList.getSelectedRow();
        selectedCustomer.setCusID((int) tbCustomerList.getValueAt(selectedRowIndex, 0));
        selectedCustomer.setCusName((String) tbCustomerList.getValueAt(selectedRowIndex, 1));
        selectedCustomer.setCusLevel((int) tbCustomerList.getValueAt(selectedRowIndex, 2));
        selectedCustomer.setCusPhone((String) tbCustomerList.getValueAt(selectedRowIndex, 3));
        selectedCustomer.setCusAddress((String) tbCustomerList.getValueAt(selectedRowIndex, 4));
        selectedCustomer.setCusEnabled((boolean) tbCustomerList.getValueAt(selectedRowIndex, 5));

        tfCusName.setText(selectedCustomer.getCusName());
        tfCusPhone.setText(selectedCustomer.getCusPhone());
        tfCusAddress.setText(selectedCustomer.getCusAddress());
        cbCusLevel.setSelectedIndex(customerLevelComboBoxModel2.getIndexOfCustomerLevel(selectedCustomer.getCusLevel()));
        cbCusEnabled.setSelected(selectedCustomer.isCusEnabled());
    }

    private void resetCustomerDetails() {
        tfCusName.setText(null);
        tfCusPhone.setText(null);
        tfCusAddress.setText(null);
        cbCusLevel.setSelectedIndex(0);
        cbCusEnabled.setSelected(true);
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
        cbLevelFilter = new javax.swing.JComboBox<>();
        tfCusPhoneFilter = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tfCusAddressFilter = new javax.swing.JTextField();
        btRemove = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();
        btAdd = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        pnTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btCusLevel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        tfCusName = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tfCusPhone = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tfCusAddress = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cbCusLevel = new javax.swing.JComboBox<>();
        cbCusEnabled = new javax.swing.JCheckBox();
        btNewOrder = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCustomerList = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(790, 640));

        pnFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));

        jLabel2.setText("ID:");

        jLabel3.setText("Name:");

        jLabel4.setText("Level:");

        jLabel6.setText("Status:");

        cbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Enabled", "Disabled" }));
        cbStatusFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStatusFilterItemStateChanged(evt);
            }
        });

        cbLevelFilter.setRenderer(customerLevelComboBoxRenderer);
        cbLevelFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbLevelFilterItemStateChanged(evt);
            }
        });

        jLabel5.setText("Phone:");

        jLabel11.setText("Address:");

        javax.swing.GroupLayout pnFilterLayout = new javax.swing.GroupLayout(pnFilter);
        pnFilter.setLayout(pnFilterLayout);
        pnFilterLayout.setHorizontalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfCusNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbLevelFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfCusPhoneFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfCusAddressFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbStatusFilter, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnFilterLayout.setVerticalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(tfCusNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(cbLevelFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(tfCusPhoneFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(tfCusAddressFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        btRemove.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        btUpdate.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Save.png"))); // NOI18N
        btUpdate.setText("Update");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
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
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Customer.png"))); // NOI18N
        jLabel1.setText("<html><u><i><font color='red'>C</font>ustomer <font color='red'>M</font>anagement</i></u></html>");

        btCusLevel.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        btCusLevel.setForeground(new java.awt.Color(255, 153, 0));
        btCusLevel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Customer2.png"))); // NOI18N
        btCusLevel.setText("<html><u>Customer Level...</u></html>");
        btCusLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCusLevelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnTitleLayout = new javax.swing.GroupLayout(pnTitle);
        pnTitle.setLayout(pnTitleLayout);
        pnTitleLayout.setHorizontalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btCusLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnTitleLayout.setVerticalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addComponent(btCusLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Customer Details"));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 103));

        jLabel7.setText("Customer Name:");

        tfCusName.setEnabled(false);

        jLabel8.setText("Customer Phone:");

        tfCusPhone.setEnabled(false);

        jLabel9.setText("Customer Address:");

        tfCusAddress.setEnabled(false);

        jLabel10.setText("Customer Level:");

        cbCusLevel.setEnabled(false);
        cbCusLevel.setRenderer(customerLevelComboBoxRenderer);

        cbCusEnabled.setSelected(true);
        cbCusEnabled.setText("Enabled");
        cbCusEnabled.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfCusPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfCusName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfCusAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbCusLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbCusEnabled)))
                .addGap(3, 3, 3))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(tfCusAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfCusName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cbCusLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbCusEnabled, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfCusPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btNewOrder.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        btNewOrder.setForeground(new java.awt.Color(0, 255, 255));
        btNewOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/NewOrder.png"))); // NOI18N
        btNewOrder.setText("New Order...");
        btNewOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNewOrderActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Order Details"));

        tbCustomerList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Cus. Name", "Cus. Level", "Cus. Phone", "Cus. Address", "Status"
            }
        ));
        tbCustomerList.setFillsViewportHeight(true);
        tbCustomerList.setRowHeight(25);
        tbCustomerList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbCustomerList.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbCustomerList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btNewOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btNewOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        insertAction();
    }//GEN-LAST:event_btAddActionPerformed

    private void btCusLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCusLevelActionPerformed
        new CustomerLevelDialog().setVisible(true);
    }//GEN-LAST:event_btCusLevelActionPerformed

    private void btNewOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNewOrderActionPerformed
        new AddOrderDialog().setVisible(true);
    }//GEN-LAST:event_btNewOrderActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        refreshAction();
    }//GEN-LAST:event_btRefreshActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        updateAction();
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        deleteAction();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void cbStatusFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStatusFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbStatusFilterItemStateChanged

    private void cbLevelFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbLevelFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbLevelFilterItemStateChanged

    //<editor-fold defaultstate="collapsed" desc="khai bao component">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btCusLevel;
    private javax.swing.JButton btNewOrder;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btUpdate;
    private javax.swing.JCheckBox cbCusEnabled;
    private javax.swing.JComboBox<CustomerLevel> cbCusLevel;
    private javax.swing.JComboBox<CustomerLevel> cbLevelFilter;
    private javax.swing.JComboBox<String> cbStatusFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JTable tbCustomerList;
    private javax.swing.JTextField tfCusAddress;
    private javax.swing.JTextField tfCusAddressFilter;
    private javax.swing.JTextField tfCusName;
    private javax.swing.JTextField tfCusNameFilter;
    private javax.swing.JTextField tfCusPhone;
    private javax.swing.JTextField tfCusPhoneFilter;
    private javax.swing.JTextField tfIdFilter;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    private void refreshAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        tbCustomerList.getSelectionModel().clearSelection();
        customerTableModel = new CustomerTableModel();
        customerLevelComboBoxModel1 = new CustomerLevelComboBoxModel();
        customerLevelComboBoxModel2 = new CustomerLevelComboBoxModel();
        setCursor(null);
        SwingUtils.showInfoDialog(DBProvider.DB_REFRESH);
    }

    private void insertAction() {
        if (!isFinished) {
            resetCustomerDetails();
            setUpdatable(true, btAdd);
            tfCusName.requestFocus();
            cbCusLevel.setSelectedIndex(1);
            isFinished = true;
        } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            setUpdatable(false, btAdd);
            Customer customer = new Customer();
            customer.setCusName(tfCusName.getText());
            customer.setCusAddress(tfCusAddress.getText());
            customer.setCusPhone(tfCusPhone.getText());
            customer.setCusLevelID(((CustomerLevel) cbCusLevel.getSelectedItem()).getCusLevelID());
            customer.setCusEnabled(cbCusEnabled.isSelected());

            if (customerTableModel.insert(customer)) {
                SwingUtils.showInfoDialog(DBProvider.INSERT_SUCCESS);

                // Select row vua insert vao
                moveScrollToRow(tbCustomerList.getRowCount() - 1);
            } else {
                SwingUtils.showErrorDialog(DBProvider.INSERT_FAIL);
            }

            isFinished = false;
            setCursor(null);
        }
    }

    private void updateAction() {
        if (!isFinished) {
            setUpdatable(true, btUpdate);
            tfCusName.requestFocus();
            isFinished = true;
        } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            setUpdatable(false, btUpdate);
            selectedCustomer.setCusName(tfCusName.getText());
            selectedCustomer.setCusAddress(tfCusAddress.getText());
            selectedCustomer.setCusPhone(tfCusPhone.getText());
            selectedCustomer.setCusLevelID(((CustomerLevel) cbCusLevel.getSelectedItem()).getCusLevelID());
            selectedCustomer.setCusEnabled(cbCusEnabled.isSelected());

            if (customerTableModel.update(selectedCustomer)) {
                SwingUtils.showInfoDialog(DBProvider.UPDATE_SUCCESS);
            } else {
                SwingUtils.showErrorDialog(DBProvider.UPDATE_FAIL);
            }

            moveScrollToRow(selectedRowIndex);
            isFinished = false;
            setCursor(null);
        }
    }

    private void deleteAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (customerTableModel.delete(selectedCustomer)) {
            SwingUtils.showInfoDialog(DBProvider.DELETE_SUCCESS);
        } else {
            SwingUtils.showErrorDialog(DBProvider.DELETE_FAIL);
        }
        setCursor(null);
    }

    private void moveScrollToRow(int row) {
        tbCustomerList.getSelectionModel().setSelectionInterval(row, row);
        tbCustomerList.scrollRectToVisible(new Rectangle(tbCustomerList.getCellRect(row, 0, true)));
    }

    private void setUpdatable(boolean updatable, JButton... exclude) {
        // Set enable cho vung nhap lieu
        tfCusName.setEnabled(updatable);
        tfCusPhone.setEnabled(updatable);
        tfCusAddress.setEnabled(updatable);
        cbCusLevel.setEnabled(updatable);
        cbCusEnabled.setEnabled(updatable);

        // Set disable cho may button update
        setButtonEnabled(!updatable, exclude);

        // Ngoai tru may component nay luon luon enable
        if (exclude.length != 0) {
            exclude[0].setEnabled(true);
            if (updatable) {
                // Doi font color mau do khi dang update
                exclude[0].setForeground(Color.RED);
                buttonText = exclude[0].getText();
                exclude[0].setText("Save");
            } else {
                // Doi font color ve mac dinh khi update xong
                exclude[0].setForeground(null);
                exclude[0].setText(buttonText);
            }
        }
    }

    private void setButtonEnabled(boolean enabled, JButton... exclude) {
        btRefresh.setEnabled(enabled);
        btRemove.setEnabled(enabled);
        btUpdate.setEnabled(enabled);
        btAdd.setEnabled(enabled);
        btNewOrder.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }
    
}
