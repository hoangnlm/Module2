/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.controller;

import utility.ComboBoxCellEditor;
import customer.model.Customer;
import customer.model.CustomerLevel;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;
import order.controller.AddOrderDialog;
import utility.StringCellEditor;
import utility.TableCellListener;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class CustomerPanel extends javax.swing.JPanel {

    private CustomerTableModel customerTableModel;
    private CustomerLevelComboBoxModel customerLevelComboBoxModel1;
    private CustomerLevelComboBoxModel customerLevelComboBoxModel2;
    private CustomerLevelComboBoxRenderer customerLevelComboBoxRenderer;
    private TableRowSorter<CustomerTableModel> sorter;

    // Customer dang duoc chon trong table
    private Customer selectedCustomer;
    private int selectedRowIndex;
    private CustomerLevel filterLevel;

    // Define some column constants
    private static final int COL_CUSID = 0;
    private static final int COL_CUSNAME = 1;
    private static final int COL_CUSLEVEL = 2;
    private static final int COL_CUSPHONE = 3;
    private static final int COL_CUSADDRESS = 4;
    private static final int COL_STATUS = 5;

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Creates new form OrderPanel
     */
    public CustomerPanel() {
        initComponents();

        // Selecting customer in the table
        selectedCustomer = new Customer();

        // Set data cho combobox level filter
        customerLevelComboBoxModel1 = new CustomerLevelComboBoxModel();
        filterLevel = new CustomerLevel(0, 0, "", 0);
        customerLevelComboBoxModel1.addElement(filterLevel);

        // Set data cho column customer level combobox
        customerLevelComboBoxModel2 = new CustomerLevelComboBoxModel();

        // Set data cho combobox level update
        customerLevelComboBoxRenderer = new CustomerLevelComboBoxRenderer();
        cbLevelFilter.setModel(customerLevelComboBoxModel1);
        cbLevelFilter.setRenderer(customerLevelComboBoxRenderer);

        // Set data cho table
        customerTableModel = new CustomerTableModel();
        tbCustomerList.setModel(customerTableModel);

        // Set auto define column from model to false to stop create column again
        tbCustomerList.setAutoCreateColumnsFromModel(false);

        // Set sorter cho table
        sorter = new TableRowSorter<>(customerTableModel);
        tbCustomerList.setRowSorter(sorter);

        // Select mac dinh cho level filter
        cbLevelFilter.setSelectedIndex(cbLevelFilter.getItemCount() - 1);

        // Col cus name
        tbCustomerList.getColumnModel().getColumn(COL_CUSNAME).setCellEditor(new StringCellEditor(1, 50, SwingUtils.PATTERN_CUSNAME));

        // Col cus level
        tbCustomerList.getColumnModel().getColumn(COL_CUSLEVEL).setCellEditor(new ComboBoxCellEditor(customerLevelComboBoxModel2));

        // Col cus phone
        tbCustomerList.getColumnModel().getColumn(COL_CUSPHONE).setCellEditor(new StringCellEditor(1, 15, SwingUtils.PATTERN_CUSPHONE));

        // Col cus address
        tbCustomerList.getColumnModel().getColumn(COL_CUSADDRESS).setCellEditor(new StringCellEditor(1, 200, SwingUtils.PATTERN_CUSADDRESS));

        // Bat su kien select row tren table
        tbCustomerList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                setButtonEnabled(true);
            } else {
                setButtonEnabled(false, btRefresh);
            }
        });

        // Set height cho table header
        tbCustomerList.getTableHeader().setPreferredSize(new Dimension(100, 30));
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Set cell listener cho updating">
        TableCellListener tcl = new TableCellListener(tbCustomerList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
//                System.out.println("Row   : " + tcl.getRow());
//                System.out.println("Column: " + tcl.getColumn());
//                System.out.println("Old   : " + tcl.getOldValue());
//                System.out.println("New   : " + tcl.getNewValue());

                switch (tcl.getColumn()) {
                    case COL_CUSNAME:
                        selectedCustomer.setCusName((String) tcl.getNewValue());
                        break;
                    case COL_CUSLEVEL:
                        selectedCustomer.setCusLevel((int) tcl.getNewValue());
                        break;
                    case COL_CUSPHONE:
                        selectedCustomer.setCusPhone((String) tcl.getNewValue());
                        break;
                    case COL_CUSADDRESS:
                        selectedCustomer.setCusAddress((String) tcl.getNewValue());
                        break;
                    case COL_STATUS:
                        selectedCustomer.setCusEnabled((boolean) tcl.getNewValue());
                        break;
                }

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
        cbLevelFilter = new javax.swing.JComboBox<>();
        tfCusPhoneFilter = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tfCusAddressFilter = new javax.swing.JTextField();
        btRemove = new javax.swing.JButton();
        btAdd = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        pnTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btCusLevel = new javax.swing.JButton();
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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
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

    private void btCusLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCusLevelActionPerformed
        new CustomerLevelDialog().setVisible(true);
    }//GEN-LAST:event_btCusLevelActionPerformed

    private void btNewOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNewOrderActionPerformed
        new AddOrderDialog().setVisible(true);
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

    private void cbLevelFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbLevelFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbLevelFilterItemStateChanged
    //// </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="khai bao component">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btCusLevel;
    private javax.swing.JButton btNewOrder;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemove;
    private javax.swing.JComboBox<CustomerLevel> cbLevelFilter;
    private javax.swing.JComboBox<String> cbStatusFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JTable tbCustomerList;
    private javax.swing.JTextField tfCusAddressFilter;
    private javax.swing.JTextField tfCusNameFilter;
    private javax.swing.JTextField tfCusPhoneFilter;
    private javax.swing.JTextField tfIdFilter;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="doFilter">
    private void doFilter() {
        RowFilter<CustomerTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<CustomerTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            filters.add(RowFilter.regexFilter("^" + tfCusNameFilter.getText(), 1));

            // Neu co chon cus level thi moi filter level
            if (cbLevelFilter.getSelectedIndex() != cbLevelFilter.getItemCount() - 1) {
                filters.add(RowFilter.regexFilter("^" + ((CustomerLevel) cbLevelFilter.getSelectedItem()).getCusLevel(), 2));
            }

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
//</editor-fold>

    private void fetchAction() {
        selectedRowIndex = tbCustomerList.getSelectedRow();
        selectedCustomer.setCusID((int) tbCustomerList.getValueAt(selectedRowIndex, 0));
        selectedCustomer.setCusName((String) tbCustomerList.getValueAt(selectedRowIndex, 1));
        selectedCustomer.setCusLevel((int) tbCustomerList.getValueAt(selectedRowIndex, 2));
        selectedCustomer.setCusPhone((String) tbCustomerList.getValueAt(selectedRowIndex, 3));
        selectedCustomer.setCusAddress((String) tbCustomerList.getValueAt(selectedRowIndex, 4));
        selectedCustomer.setCusEnabled((boolean) tbCustomerList.getValueAt(selectedRowIndex, 5));
    }

    private void refreshAction(boolean mustInfo) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // Refresh table
        customerTableModel.refresh();

        // Refresh combobox filter
        customerLevelComboBoxModel1.refresh();
        customerLevelComboBoxModel1.addElement(filterLevel);

        // Refresh combobox column table
        customerLevelComboBoxModel2.refresh();
        setCursor(null);
        if (mustInfo) {
            SwingUtils.showInfoDialog(SwingUtils.DB_REFRESH);
        }
        scrollToRow(selectedRowIndex);
    }

    private void insertAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = customerTableModel.insert(new Customer());
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.INSERT_SUCCESS : SwingUtils.INSERT_FAIL);
        // Select row vua insert vao
        selectedRowIndex = tbCustomerList.getRowCount() - 1;
        scrollToRow(selectedRowIndex);
        tbCustomerList.editCellAt(tbCustomerList.getSelectedRow(), 1);
        tbCustomerList.getEditorComponent().requestFocus();
    }

    private void updateAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = customerTableModel.update(selectedCustomer);
        refreshAction(false);
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.UPDATE_SUCCESS : SwingUtils.UPDATE_FAIL);
        scrollToRow(selectedRowIndex);
    }

    private void deleteAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = customerTableModel.delete(selectedCustomer);
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.DELETE_SUCCESS : SwingUtils.DELETE_FAIL);

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbCustomerList.getRowCount() ? tbCustomerList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
    }

    private void scrollToRow(int row) {
        tbCustomerList.getSelectionModel().setSelectionInterval(row, row);
        tbCustomerList.scrollRectToVisible(new Rectangle(tbCustomerList.getCellRect(row, 0, true)));
    }

    private void setButtonEnabled(boolean enabled, JButton... exclude) {
        btRefresh.setEnabled(enabled);
        btRemove.setEnabled(enabled);
        btAdd.setEnabled(enabled);
        btNewOrder.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }
}
