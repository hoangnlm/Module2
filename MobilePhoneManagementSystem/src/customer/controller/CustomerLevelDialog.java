/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.controller;

import customer.model.CustomerLevel;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import utility.SpinnerCellEditor;
import utility.StringCellEditor;
import utility.SwingUtils;
import utility.TableCellListener;

/**
 *
 * @author Hoang
 */
public class CustomerLevelDialog extends javax.swing.JDialog {

    private CustomerLevelTableModel customerLevelTableModel;

    // Customer level dang duoc chon trong table
    private CustomerLevel selectedCustomerLevel;
    private int selectedRowIndex;

    private static final int COL_CUSLEVELID = 0;
    private static final int COL_CUSLEVEL = 1;
    private static final int COL_CUSLEVELNAME = 2;
    private static final int COL_CUSDISCOUNT = 3;

    /**
     * Creates new form CustomerDialog
     */
    public CustomerLevelDialog() {
        initComponents();
        setLocationRelativeTo(null);
        
        // Disable button khi moi khoi dong len
        setButtonEnabled(false);

        // Selecting customer level in the table
        selectedCustomerLevel = new CustomerLevel();

        // Set data cho table
        customerLevelTableModel = new CustomerLevelTableModel();
        tbLevelList.setModel(customerLevelTableModel);

        // Set auto define column from model to false to stop create column again
        tbLevelList.setAutoCreateColumnsFromModel(false);
        tbLevelList.setAutoCreateRowSorter(true);

        // Col cus level ID
        tbLevelList.getColumnModel().getColumn(COL_CUSLEVELID).setMaxWidth(40);

        // Col cus level
        tbLevelList.getColumnModel().getColumn(COL_CUSLEVEL).setCellEditor(new SpinnerCellEditor(CustomerLevel.MIN_LEVEL, CustomerLevel.MAX_LEVEL));
        tbLevelList.getColumnModel().getColumn(COL_CUSLEVEL).setMaxWidth(50);

        // Col cus level name
        tbLevelList.getColumnModel().getColumn(COL_CUSLEVELNAME).setCellEditor(new StringCellEditor(1, 50, SwingUtils.PATTERN_NAMEWITHSPACE));

        // Col cus discount
        tbLevelList.getColumnModel().getColumn(COL_CUSDISCOUNT).setCellEditor(new SpinnerCellEditor(CustomerLevel.MIN_DISCOUNT, CustomerLevel.MAX_DISCOUNT));
        tbLevelList.getColumnModel().getColumn(COL_CUSDISCOUNT).setMinWidth(100);
        tbLevelList.getColumnModel().getColumn(COL_CUSDISCOUNT).setMaxWidth(150);

        // Bat su kien select row tren table
        tbLevelList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                setButtonEnabled(true);
            } else {
                setButtonEnabled(false);
            }
        });

        // Set height cho table header
        tbLevelList.getTableHeader().setPreferredSize(new Dimension(300, 30));

        // Set table cell listener to update
        TableCellListener tcl = new TableCellListener(tbLevelList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                switch (tcl.getColumn()) {
                    case COL_CUSLEVEL:
                        selectedCustomerLevel.setCusLevel((int) tcl.getNewValue());
                        break;
                    case COL_CUSLEVELNAME:
                        selectedCustomerLevel.setCusLevelName((String) tcl.getNewValue());
                        break;
                    case COL_CUSDISCOUNT:
                        selectedCustomerLevel.setCusDiscount((float) tcl.getNewValue() / 100);
                        break;
                }
                updateAction();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbLevelList = new javax.swing.JTable();
        btAdd = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        btClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Customer Level Management");
        setMinimumSize(null);
        setModal(true);
        setResizable(false);
        setSize(new java.awt.Dimension(560, 400));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Level List"));

        tbLevelList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Level", "Name", "Discount (%)"
            }
        ));
        tbLevelList.setFillsViewportHeight(true);
        tbLevelList.setPreferredSize(new java.awt.Dimension(560, 80));
        tbLevelList.setRowHeight(20);
        tbLevelList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbLevelList);

        btAdd.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Add3.png"))); // NOI18N
        btAdd.setText("Add");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btDelete.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete2.png"))); // NOI18N
        btDelete.setText("Remove");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        btRefresh.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Refresh2.png"))); // NOI18N
        btRefresh.setText("Refresh");
        btRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRefreshActionPerformed(evt);
            }
        });

        btClose.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Cancel2.png"))); // NOI18N
        btClose.setText("Close");
        btClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btClose, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btClose, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
//<editor-fold defaultstate="collapsed" desc="bat su kien">
    private void btCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btCloseActionPerformed

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        insertAction();
    }//GEN-LAST:event_btAddActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        deleteAction();
    }//GEN-LAST:event_btDeleteActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        refreshAction(true);
    }//GEN-LAST:event_btRefreshActionPerformed
//</editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btClose;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbLevelList;
    // End of variables declaration//GEN-END:variables

    private void fetchAction() {
        selectedRowIndex = tbLevelList.getSelectedRow();
        selectedCustomerLevel.setCusLevelID((int) tbLevelList.getValueAt(selectedRowIndex, 0));
        selectedCustomerLevel.setCusLevel((int) tbLevelList.getValueAt(selectedRowIndex, 1));
        selectedCustomerLevel.setCusLevelName(((String) tbLevelList.getValueAt(selectedRowIndex, 2)).trim());
        selectedCustomerLevel.setCusDiscount((float) tbLevelList.getValueAt(selectedRowIndex, 3) / 100);
    }

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            customerLevelTableModel.refresh();
            setCursor(null);
            SwingUtils.showInfoDialog(SwingUtils.DB_REFRESH);
        } else {
            customerLevelTableModel.refresh();
        }
        scrollToRow(selectedRowIndex);
    }

    private void insertAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = customerLevelTableModel.insert(new CustomerLevel());
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.INSERT_SUCCESS : SwingUtils.INSERT_FAIL);
        // Select row vua insert vao
        selectedRowIndex = tbLevelList.getRowCount() - 1;
        scrollToRow(selectedRowIndex);
    }

    private void updateAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = customerLevelTableModel.update(selectedCustomerLevel);
        refreshAction(false);
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.UPDATE_SUCCESS : SwingUtils.UPDATE_FAIL);
    }

    private void deleteAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = customerLevelTableModel.delete(selectedCustomerLevel);
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.DELETE_SUCCESS : SwingUtils.DELETE_FAIL);

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbLevelList.getRowCount() ? tbLevelList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
    }

    private void scrollToRow(int row) {
        tbLevelList.getSelectionModel().setSelectionInterval(row, row);
        tbLevelList.scrollRectToVisible(new Rectangle(tbLevelList.getCellRect(row, 0, true)));
    }

    private void setButtonEnabled(boolean enabled, JButton... exclude) {
        btDelete.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }
}
