/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branch.controller;

import branch.dao.BranchDAOImpl;
import branch.dao.CheckBoxCellEditor;
import branch.dao.ComboBoxCellEditor;
import branch.model.Branch;
import branch.model.BranchTableModel;
import branch.model.Supplier;
import branch.model.SupplierComboboxModel;
import branch.model.SupplierComboboxRenderer;
import database.DBProvider;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import utility.StringCellEditor;
import utility.SwingUtils;
import utility.TableCellListener;

/**
 *
 * @author tuan
 */
public class BranchDialog extends javax.swing.JDialog {

   private BranchTableModel branchTableModel;
   private final TableRowSorter<BranchTableModel> sorter;
   private final SupplierComboboxModel supplierComboboxModel;
   private final SupplierComboboxRenderer supplierComboboxRenderer;
   // Branch dang duoc chon trong table
    private Branch selectedBranch;
    private int selectedRowIndex;
    private Supplier filterSupplier;
   
    private static int COL_ID = 0;
    private static int COL_BraName = 1;
    private static int COL_Status = 2;
    private static int COL_Supplier = 3;
   
   
    public BranchDialog() {
        super((JFrame)null, true);
        initComponents();
        setLocationRelativeTo(null);
        
        
        selectedBranch = new Branch();
        
        //combobox
        // Set data cho combobox level filter
        supplierComboboxModel = new SupplierComboboxModel();
        filterSupplier = new Supplier(0, "All", "223a", true);
        supplierComboboxModel.addElement(filterSupplier);
        
        supplierComboboxRenderer = new SupplierComboboxRenderer();
        cbSupplier.setModel(supplierComboboxModel);
        cbSupplier.setRenderer(supplierComboboxRenderer);
        
        
        //set data cho table
        branchTableModel = new BranchTableModel();
        tbBranchList.setModel(branchTableModel);
        
        // Set sorter cho table
        sorter = new TableRowSorter<>(branchTableModel);
        tbBranchList.setRowSorter(sorter);
        
        // Bat su kien select row tren table
        tbBranchList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchBranchDetails();
                btnDelete.setEnabled(true);
                tbBranchList.setSurrendersFocusOnKeystroke(false);
            } 
        });
        
        //bat su kien update cho table
        TableCellListener tcl = new TableCellListener(tbBranchList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
//                System.out.println("Row   : " + tcl.getRow());
//                System.out.println("Column: " + tcl.getColumn());
//                System.out.println("Old   : " + tcl.getOldValue());
//                System.out.println("New   : " + tcl.getNewValue());

                switch (tcl.getColumn()) {
                    case 1:
                        selectedBranch.setBraName((String) tcl.getNewValue());
                        
                        break;
                    case 2:
                        selectedBranch.setBraStatus((boolean) tcl.getNewValue());
                        break;
                    case 3:
                        selectedBranch.setSupName((String) tcl.getNewValue());
                        break;
                    }

                updateAction();
                formatTable();
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
         //customize combobox status
        cbStatusFilter.setRenderer(new DefaultListCellRenderer() {
            JTextField jtf = new JTextField();

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (isSelected) {
                    jtf.setBackground(Color.ORANGE);
                    jtf.setForeground(Color.BLACK);

                } else {
                    jtf.setBackground(new java.awt.Color(51, 51, 51));
                    jtf.setForeground(list.getForeground());
                }
                jtf.setText((String) value);
                jtf.setBorder(null);
                jtf.setHorizontalAlignment(CENTER);
                return jtf;
            }

        });
        //set gia tri mac dinh
        cbSupplier.setSelectedIndex(cbSupplier.getItemCount() - 1);
        formatTable();
        
        //button delete
        btnDelete.setEnabled(false);
    }
    
    public void formatTable(){
        tbBranchList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        
         //alignment component
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        //id
        tbBranchList.getColumnModel().getColumn(COL_ID).setMinWidth(30);
        tbBranchList.getColumnModel().getColumn(COL_ID).setMaxWidth(50);
        tbBranchList.getColumnModel().getColumn(COL_ID).setCellRenderer(centerRenderer);
        //branch
        tbBranchList.getColumnModel().getColumn(COL_BraName).setMinWidth(100);
        tbBranchList.getColumnModel().getColumn(COL_BraName).setMaxWidth(100);
        tbBranchList.getColumnModel().getColumn(COL_BraName).setCellRenderer(centerRenderer);
        tbBranchList.getColumnModel().getColumn(COL_BraName).setCellEditor(new StringCellEditor(1, 50, "[a-zA-Z ]+"));
        //status
        tbBranchList.getColumnModel().getColumn(COL_Status).setMinWidth(73);
        tbBranchList.getColumnModel().getColumn(COL_Status).setMaxWidth(73);
        
        //supplier
        tbBranchList.getColumnModel().getColumn(COL_Supplier).setMinWidth(200);
        tbBranchList.getColumnModel().getColumn(COL_Supplier).setMaxWidth(200);
        tbBranchList.getColumnModel().getColumn(COL_Supplier).setCellRenderer(centerRenderer);
        tbBranchList.getColumnModel().getColumn(COL_Supplier).setCellEditor(new ComboBoxCellEditor(new SupplierComboboxModel()));
    
    }
    private void doFilter() {
        RowFilter<BranchTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<BranchTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            filters.add(RowFilter.regexFilter("^" + tfNameFilter.getText(), 1));

            // Neu status khac "All" thi moi filter
            String statusFilter = cbStatusFilter.getSelectedItem().toString();
            if (!statusFilter.equals("All")) {
                filters.add(RowFilter.regexFilter(
                        statusFilter.equals("Enabled") ? "t" : "f", 2));
            }

            
            if(cbSupplier.getSelectedIndex() != cbSupplier.getItemCount() - 1){
                filters.add(RowFilter.regexFilter("^"+((Supplier) cbSupplier.getSelectedItem()).getSupName(), 3));
            }
            rf = RowFilter.andFilter(filters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    private void fetchBranchDetails() {
        selectedRowIndex = tbBranchList.getSelectedRow();
        selectedBranch.setBraID((int) tbBranchList.getValueAt(selectedRowIndex, 0));
        selectedBranch.setBraName((String) tbBranchList.getValueAt(selectedRowIndex, 1));
        selectedBranch.setBraStatus((boolean) tbBranchList.getValueAt(selectedRowIndex, 2));
        selectedBranch.setSupName((String) tbBranchList.getValueAt(selectedRowIndex, 3));
    }
    
    private void updateAction() {
        if (branchTableModel.update(selectedBranch)) {
            JOptionPane.showMessageDialog(this, "Update successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Update failed");
        }
        branchTableModel.refresh();
    }
    private void insertAction() {
        Branch branch = new Branch();
        
        branch.setBraName("Default Branch "+System.currentTimeMillis());
        branch.setBraStatus(false);
        
        if (branchTableModel.insert(branch)) {
            JOptionPane.showMessageDialog(this, "insert successfully");
            
            //refresh table
            branchTableModel.refresh();
            
            
            
            // Select row vua insert vao
            selectedRowIndex = 0;
            moveScrollToRow(selectedRowIndex);
            tbBranchList.editCellAt(tbBranchList.getSelectedRow(), 1);
            tbBranchList.getEditorComponent().requestFocus();
        } else {
            JOptionPane.showMessageDialog(this, "insert failed");
        }
        formatTable();
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
        cbStatusFilter = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbBranchList = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cbSupplier = new javax.swing.JComboBox<>();
        btnDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("ID:");

        jLabel2.setText("Name:");

        jLabel3.setText("Status:");

        cbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Enabled", "Disabled" }));
        cbStatusFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStatusFilterItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbStatusFilter, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbBranchList.setAutoCreateRowSorter(true);
        tbBranchList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Branch ID", "Branch Name", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbBranchList.setFillsViewportHeight(true);
        tbBranchList.setRowHeight(25);
        tbBranchList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbBranchListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbBranchList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Add.png"))); // NOI18N
        jButton1.setText("Insert");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/folder-open.png"))); // NOI18N
        jButton2.setText("Product");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/refresh_1.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel4.setText("Supplier:");

        cbSupplier.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbSupplierItemStateChanged(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Delete.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton1)
                        .addComponent(jButton2))
                    .addComponent(btnDelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        insertAction();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        refreshAction();
        tfIdFilter.setText("");
        tfNameFilter.setText("");
        cbStatusFilter.setSelectedIndex(0);
        cbSupplier.setSelectedIndex(cbSupplier.getItemCount() - 1);
        branchTableModel.refresh();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cbStatusFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStatusFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbStatusFilterItemStateChanged

    private void tbBranchListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbBranchListMouseClicked
//        int Selectedrow = tbBranchList.getSelectedRow();
//           
//          
//           if(tbBranchList.getValueAt(Selectedrow, 1).toString().equals("")){
//               return;
//           }
//           
//            int col = tbBranchList.columnAtPoint(evt.getPoint());
//            if(col==1){
//                
//                tbBranchList.setCellEditor(new StringCellEditor(1,50, "[A-Za-z]"));
//            }
//            
    }//GEN-LAST:event_tbBranchListMouseClicked

    private void cbSupplierItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSupplierItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbSupplierItemStateChanged

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
       deleteAction();
    }//GEN-LAST:event_btnDeleteActionPerformed

    public void deleteAction(){
        int ans = SwingUtils.showConfirmDialog("Are you sure to delete?");
        if(ans==JOptionPane.YES_OPTION){
        if (branchTableModel.delete(selectedBranch)) {
            JOptionPane.showMessageDialog(this, "Delete successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed");
        }
        branchTableModel.refresh();
        formatTable();
        btnDelete.setEnabled(false);
        }
    }
   public void refreshAction(){
       setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
       branchTableModel = new BranchTableModel();
       tbBranchList.getSelectionModel().clearSelection();
       setCursor(null);
   }
   private void moveScrollToRow(int row) {
        tbBranchList.getSelectionModel().setSelectionInterval(row, row);
        tbBranchList.scrollRectToVisible(new Rectangle(tbBranchList.getCellRect(row, 0, true)));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JComboBox<String> cbStatusFilter;
    private javax.swing.JComboBox<Supplier> cbSupplier;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbBranchList;
    private javax.swing.JTextField tfIdFilter;
    private javax.swing.JTextField tfNameFilter;
    // End of variables declaration//GEN-END:variables
}
