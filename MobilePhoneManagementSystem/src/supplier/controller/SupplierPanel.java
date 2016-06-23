/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supplier.controller;

import database.DBProvider;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;
import supplier.model.Supplier;
import supplier.model.SupplierTableModel;
import utility.SwingUtils;

/**
 *
 * @author tuan
 */
public class SupplierPanel extends javax.swing.JPanel {
    
     private SupplierTableModel supplierTableModel;
   private final TableRowSorter<SupplierTableModel> sorter;
   
   // Branch dang duoc chon trong table
    private Supplier selectedSupplier;
    private int selectedRowIndex;
    public SupplierPanel() {
        initComponents();
        selectedSupplier = new Supplier();
        
        //set data cho table
        supplierTableModel = new SupplierTableModel();
        tbSupplierList.setModel(supplierTableModel);
        
        // Set sorter cho table
        sorter = new TableRowSorter<>(supplierTableModel);
        tbSupplierList.setRowSorter(sorter);
        
        // Bat su kien select row tren table
        tbSupplierList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchSupplierDetails();
                tbSupplierList.setSurrendersFocusOnKeystroke(false);
            } 
        });
        
        //bat su kien update cho table
        tbSupplierList.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                fetchSupplierDetails();

                updateAction();
             
            }
    });
        
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
        tfNameFilter.getDocument().addDocumentListener(
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
        
        tfAddressFilter.getDocument().addDocumentListener(
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
        RowFilter<SupplierTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<SupplierTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            filters.add(RowFilter.regexFilter("^" + tfNameFilter.getText(), 1));
            filters.add(RowFilter.regexFilter("^" + tfAddressFilter.getText(), 2));
            
            // Neu status khac "Choose" thi moi filter
            String statusFilter = cbStatusFilter.getSelectedItem().toString();
            if (!statusFilter.equals("--Choose--")) {
                filters.add(RowFilter.regexFilter(
                        statusFilter.equals("Enabled") ? "t" : "f", 3));
            }

            rf = RowFilter.andFilter(filters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    private void fetchSupplierDetails() {
        selectedRowIndex = tbSupplierList.getSelectedRow();
        selectedSupplier.setSupID((int) tbSupplierList.getValueAt(selectedRowIndex, 0));
        selectedSupplier.setSupName((String) tbSupplierList.getValueAt(selectedRowIndex, 1));
        selectedSupplier.setSupAddress((String) tbSupplierList.getValueAt(selectedRowIndex, 2));
        selectedSupplier.setSupStatus((boolean) tbSupplierList.getValueAt(selectedRowIndex, 3));
    }
    private void updateAction() {
        if (supplierTableModel.update(selectedSupplier)) {
            JOptionPane.showMessageDialog(this, "Update successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Update failed");
        }

        
    }
    
    private void insertAction() {
        Supplier supplier = new Supplier();
        Random rn = new Random();
        int result = rn.nextInt(1000 - 1 + 1) + 1;
        supplier.setSupName("Default Name"+ result);
        supplier.setSupAddress("Default address");
        supplier.setSupStatus(false);
        if (supplierTableModel.insert(supplier)) {
            JOptionPane.showMessageDialog(this, "Insert successfully");

            // Select row vua insert vao
            moveScrollToRow(tbSupplierList.getRowCount() - 1);
            tbSupplierList.editCellAt(tbSupplierList.getSelectedRow(), 1);
            tbSupplierList.setSurrendersFocusOnKeystroke(true);
            tbSupplierList.getEditorComponent().requestFocus();
        } else {
            JOptionPane.showMessageDialog(this, "Insert failed");
        }
    }
    
    private void moveScrollToRow(int row) {
        tbSupplierList.getSelectionModel().setSelectionInterval(row, row);
        tbSupplierList.scrollRectToVisible(new Rectangle(tbSupplierList.getCellRect(row, 0, true)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfIdFilter = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfNameFilter = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbStatusFilter = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        tfAddressFilter = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbSupplierList = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

        jLabel1.setText("Supplier ID:");

        jLabel2.setText("Supplier Name:");

        jLabel3.setText("Supplier Address:");

        jLabel4.setText("Status:");

        cbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Choose--", "Enabled", "Disabled" }));
        cbStatusFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStatusFilterItemStateChanged(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Refresh.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        tfAddressFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfAddressFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(15, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(tfIdFilter)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfNameFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                    .addComponent(tfAddressFilter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 36, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfAddressFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Add.png"))); // NOI18N
        jButton1.setText("Add New Supplier");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Suppliers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

        tbSupplierList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Supplier ID", "Supplier Name", "Supplier Address", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbSupplierList);

        jLabel5.setFont(new java.awt.Font("Lucida Sans", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Supplier.png"))); // NOI18N
        jLabel5.setText("<html><u><i><font color='red'>S</font>upplier <font color='red'>M</font>anagement</i></u></html>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbStatusFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStatusFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbStatusFilterItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        insertAction();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        tfIdFilter.setText("");
        tfAddressFilter.setText("");
        tfNameFilter.setText("");
        cbStatusFilter.setSelectedIndex(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tfAddressFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfAddressFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfAddressFilterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbStatusFilter;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tbSupplierList;
    private javax.swing.JTextField tfAddressFilter;
    private javax.swing.JTextField tfIdFilter;
    private javax.swing.JTextField tfNameFilter;
    // End of variables declaration//GEN-END:variables
}
