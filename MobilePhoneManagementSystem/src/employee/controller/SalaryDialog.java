/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee.controller;

import com.toedter.calendar.JDateChooser;
import employee.model.Employee;
import employee.model.Salary;
import employee.model.SalaryDAOImpl;
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
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import main.controller.LoginFrame;
import main.model.UserFunction;
import static order.controller.OrderDialog.COL_PROQTY;
import utility.DateCellEditor;
import utility.IntegerCellEditor;
import utility.SpinnerCellEditor;
import utility.StringCellEditor;
import utility.SwingUtils;
import utility.TableCellListener;

/**
 *
 * @author Hoang
 */
public class SalaryDialog extends javax.swing.JDialog {

    private JDateChooser dcFilter;
    private SalaryTableModel salaryTableModel;
    private TableRowSorter<SalaryTableModel> sorter;

    // Salary dang duoc chon trong table
    private Salary selectedSalary;
    private int selectedRowIndex;
    Employee employee;
// Define some column constants
    private static final int COL_SALID = 0;
    private static final int COL_EMPID = 1;
    private static final int COL_MONTH = 2;
    private static final int COL_PAYDAY = 3;
    private static final int COL_WORKDAYS = 4;
    private static final int COL_OFFDAYS = 5;
    private List<Salary> salary;

    public SalaryDialog(Employee employee) {
        super((JFrame) null, true);
        initComponents();
        this.employee = employee;
        dcFilter = new JDateChooser();
        dcFilter.setBounds(0, 0, 130, 30);
        dcFilter.setDateFormatString("MMMM dd,yyyy");
//        pnPayday.add(dcFilter);
        setLocationRelativeTo(null);

        // Set data cho table
        salaryTableModel = new SalaryTableModel();
        tbSalaryList.setModel(salaryTableModel);

        // Set height cho table header
        tbSalaryList.getTableHeader().setPreferredSize(new Dimension(300, 30));

        // Col Ser ID (HIDDEN)
        tbSalaryList.getColumnModel().getColumn(COL_SALID).setMinWidth(70);
//        tbSalaryList.getColumnModel().getColumn(COL_SALID).setMaxWidth(100);
        tbSalaryList.getColumnModel().getColumn(COL_EMPID).setMinWidth(0);
        tbSalaryList.getColumnModel().getColumn(COL_EMPID).setMaxWidth(0);
        // Col pro ID (HIDDEN)
        tbSalaryList.getColumnModel().getColumn(COL_MONTH).setMinWidth(70);
//        tbSalaryList.getColumnModel().getColumn(COL_MONTH).setMaxWidth(100);
        // Col quantity
        tbSalaryList.getColumnModel().getColumn(COL_PAYDAY).setMinWidth(160);
        tbSalaryList.getColumnModel().getColumn(COL_PAYDAY).setMaxWidth(200);
        tbSalaryList.getColumnModel().getColumn(COL_PAYDAY).setCellEditor(new DateCellEditor());

        // Col oderid
        tbSalaryList.getColumnModel().getColumn(COL_WORKDAYS).setMinWidth(100);
//        tbSalaryList.getColumnModel().getColumn(COL_WORKDAYS).setMaxWidth(100);
        tbSalaryList.getColumnModel().getColumn(COL_WORKDAYS).setCellEditor(new IntegerCellEditor(0, 30));
        // Col oderid
        tbSalaryList.getColumnModel().getColumn(COL_OFFDAYS).setMinWidth(100);
//        tbSalaryList.getColumnModel().getColumn(COL_OFFDAYS).setMaxWidth(100);
        tbSalaryList.getColumnModel().getColumn(COL_OFFDAYS).setCellEditor(new IntegerCellEditor(0, 22));

        // Bat su kien select row tren table product
        tbSalaryList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                if (salaryTableModel.getRowCount() > 1) {
                    btRemove.setEnabled(true);
                    setButtonEnabled(true);
                }
            } else {
                setButtonEnabled(false);
                btRemove.setEnabled(false);
            }
        });

//</editor-fold>
        // Set data cho table chinh
        salaryTableModel.load(employee.getEmpID());    //Emply list neu o mode insert

        //<editor-fold defaultstate="collapsed" desc="Set cell listener cho updating">
        TableCellListener tcl = new TableCellListener(tbSalaryList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();

                switch (tcl.getColumn()) {
                    case COL_PAYDAY:
//                        selectedProduct.setProQty((int) tbProduct.getValueAt(tbProduct.getSelectedRow(), COL_PROQTY));
                        selectedSalary.setPayDay((Date) tcl.getNewValue());
                        break;
                    case COL_WORKDAYS:
                        selectedSalary.setWorkDays((int) tcl.getNewValue());
                        break;
                    case COL_OFFDAYS:
                        selectedSalary.setOffDays((int) tcl.getNewValue());
                        break;
                }
                if (SwingUtils.showConfirmDialog("Are you sure to update ?") == JOptionPane.NO_OPTION) {
                    return;
                } else {
                    updateAction();
                }
            }
        });
//</editor-fold>

        // Set data cho cac label
        updateItemsLabel();

        if (!LoginFrame.checkPermission(new UserFunction(UserFunction.FG_SALARY, UserFunction.FN_UPDATE))) {
            tbSalaryList.setEnabled(false);
            btAdd.setEnabled(false);
            btRemove.setEnabled(false);
        }

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
        tbSalaryList = new javax.swing.JTable();
        btAdd = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblBasicSalary = new javax.swing.JLabel();
        lblBonusDes = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblSumary = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Customer");
        setMinimumSize(new java.awt.Dimension(712, 500));
        setModal(true);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Salary List"));

        tbSalaryList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SalID", "Month", "Payday", "WorkDays", "OffDays"
            }
        ));
        tbSalaryList.setRowHeight(20);
        tbSalaryList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbSalaryList);

        btAdd.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Add3.png"))); // NOI18N
        btAdd.setText("Add");
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btRemove.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete2.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
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

        btCancel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Cancel2.png"))); // NOI18N
        btCancel.setText("Cancel");
        btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Salary Info"));

        jLabel5.setText("Bonus on Designation:");

        jLabel7.setText("Basic Salary:");

        lblBasicSalary.setText("10000000 đ");

        lblBonusDes.setText("5000000 đ");

        jLabel16.setText("Sumary:");

        lblSumary.setText("500000000");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(lblBasicSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(lblBonusDes, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(132, 132, 132)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblSumary, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSumary, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBasicSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBonusDes, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btRemove)
                .addGap(13, 13, 13)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btCancelActionPerformed

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        insertAction();
    }//GEN-LAST:event_btAddActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        deleteAction();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        refreshAction(false);
    }//GEN-LAST:event_btRefreshActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemove;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBasicSalary;
    private javax.swing.JLabel lblBonusDes;
    private javax.swing.JLabel lblSumary;
    private javax.swing.JTable tbSalaryList;
    // End of variables declaration//GEN-END:variables

    private void insertAction() {

        Salary details = new Salary();
        details.setWorkDays(22);
        details.setOffDays(0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        details.setPayDay(calendar.getTime());
        details.setMonth(calendar.getTime().getMonth() + 1);
        details.setEmpID(employee.getEmpID());
//        System.out.println("zzzzzzz:   " + details);
        salaryTableModel.insert(details);
//        salaryTableModel.refresh();

        scrollToRow(tbSalaryList.getRowCount() - 1);
        updateItemsLabel();
    }

    private void deleteAction() {
        btAdd.setEnabled(true);
        if (SwingUtils.showConfirmDialog("Are you sure to delete ?") == JOptionPane.NO_OPTION) {
            return;
        } else if (salaryTableModel.delete(selectedSalary)) {
            SwingUtils.showInfoDialog(SwingUtils.DELETE_SUCCESS);

        } else {
            SwingUtils.showInfoDialog(SwingUtils.DELETE_FAIL);
        }
        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbSalaryList.getRowCount() ? tbSalaryList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
        updateItemsLabel();
    }

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            // Refresh table
            salaryTableModel.load(employee.getEmpID());
//            salaryTableModel.refresh();

            setCursor(null);
            SwingUtils.showInfoDialog(SwingUtils.DB_REFRESH);
        } else {
            // Refresh table
            salaryTableModel.load(employee.getEmpID());
//            salaryTableModel.refresh();
        }
        scrollToRow(selectedRowIndex);
    }

    // Ham goi khi bam nut Save
    private void updateAction() {

        if (salaryTableModel.update(selectedSalary)) { // Update mode
            SwingUtils.showInfoDialog(SwingUtils.UPDATE_SUCCESS);
            
        } else {
            SwingUtils.showInfoDialog(SwingUtils.UPDATE_FAIL);
        }
    }

    private void cancelAction() {

        if (SwingUtils.showConfirmDialog("Discard change(s) and quit ?") == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void scrollToRow(int row) {
        tbSalaryList.getSelectionModel().setSelectionInterval(row, row);
        tbSalaryList.scrollRectToVisible(new Rectangle(tbSalaryList.getCellRect(row, 0, true)));
    }

    private void fetchAction() {
        selectedRowIndex = tbSalaryList.getSelectedRow();
        if (selectedRowIndex >= 0) {
            int idx = tbSalaryList.convertRowIndexToModel(selectedRowIndex);
            selectedSalary = salaryTableModel.getSalaryFromIndex(idx);
            salaryTableModel.setSelectingIndex(idx);
        } else {
            selectedSalary = null;
            salaryTableModel.setSelectingIndex(-1);
        }
    }

    private void updateItemsLabel() {
//        int sum = tbProduct.getRowCount();
//        int sale = 0;float bonusSale=0;
//        int service=0;
//        SalaryDAOImpl sa = new SalaryDAOImpl();
//        Vector arr = sa.getTotalItemSale(employee.getEmpID(),selectedSalary.getMonth());
//        
//        sale=(int) arr.get(1);
//        bonusSale=(float) arr.get(2);
//        lblTotalItemSale.setText(sale+"");
//        lblBonusSale.setText(String.format("%12.2lf", bonusSale));
        lblBasicSalary.setText(String.format("%d", employee.getEmpSalary()));
        lblBonusDes.setText(String.format("%d", employee.getEmpBonus()));
        lblSumary.setText(String.format("%d", employee.getEmpBonus() + employee.getEmpSalary()));
        updateTotalLabel();
    }

    private void updateTotalLabel() {
        int sum = 0;
//        if (serviceDetailsTableModelDialog.getRowCount() > 0) {
//            for (int i = 0; i < serviceDetailsTableModelDialog.getRowCount(); i++) {
//                sum += (int) serviceDetailsTableModelDialog.getValueAt(i, COL_COST) * (int) serviceDetailsTableModelDialog.getValueAt(i, COL_PROQTY);
//            }
//        }
//        lbTotal.setText(String.format("%,.0f Đ", (float) sum));
    }

    private void setButtonEnabled(boolean enabled, JButton... exclude) {
        btRemove.setEnabled(enabled);
        btAdd.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }

}
