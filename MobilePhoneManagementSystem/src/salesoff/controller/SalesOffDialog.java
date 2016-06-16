package salesoff.controller;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import salesoff.model.SalesOff;
import salesoff.model.SalesOffProduct;
import utility.DateCellEditor;
import utility.SpinnerCellEditor;
import utility.StringCellEditor;
import utility.SwingUtils;
import utility.TableCellListener;

/**
 *
 * @author Hoang
 */
public class SalesOffDialog extends javax.swing.JDialog {

    // Khai bao 2 cai table model
    private SalesOffTableModel salesOffTableModel;
    private SalesOffProductTableModel salesOffProductTableModel;

    // Sales off dang duoc chon trong table
    private SalesOff selectedSale;
    private int selectedSaleRowIndex;

    private static final int COL_SALEID = 0;
    private static final int COL_SALENAME = 1;
    private static final int COL_SALESTART = 2;
    private static final int COL_SALEEND = 3;
    private static final int COL_SALEAMOUNT = 4;

    // Product dang duoc chon trong table
    private SalesOffProduct selectedPro;
    private int selectedProRowIndex;

    private static final int COL_PROID = 0;
    private static final int COL_PRONAME = 1;
    private static final int COL_BRANAME = 2;
    private static final int COL_PROSALE = 3;

    /**
     * Creates new form CustomerDialog
     */
    public SalesOffDialog() {
        initComponents();
        setLocationRelativeTo(null);

        //Disable button khi moi khoi dong len
        setButtonEnabled(false);

        // Selecting sales off in the table
        selectedSale = new SalesOff();
        selectedPro = new SalesOffProduct();

        // Set data cho table
        salesOffTableModel = new SalesOffTableModel();
        salesOffProductTableModel = new SalesOffProductTableModel();
        tbSaleList.setModel(salesOffTableModel);
        tbProList.setModel(salesOffProductTableModel);

        // Set auto define column from model to false to stop create column again
        tbSaleList.setAutoCreateColumnsFromModel(false);
        tbProList.setAutoCreateColumnsFromModel(false);
        tbSaleList.setAutoCreateRowSorter(true);
        tbProList.setAutoCreateRowSorter(true);

        // Set height cho table header
        tbSaleList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        tbProList.getTableHeader().setPreferredSize(new Dimension(300, 30));

        // Col sale ID
        tbSaleList.getColumnModel().getColumn(COL_SALEID).setMinWidth(20);
        tbSaleList.getColumnModel().getColumn(COL_SALEID).setMaxWidth(40);

        // Col sale name
        tbSaleList.getColumnModel().getColumn(COL_SALENAME).setCellEditor(new StringCellEditor(1, 50, SwingUtils.PATTERN_CUSADDRESS));
        tbSaleList.getColumnModel().getColumn(COL_SALENAME).setMinWidth(150);

        // Col sale start date
        tbSaleList.getColumnModel().getColumn(COL_SALESTART).setCellEditor(new DateCellEditor());
        tbSaleList.getColumnModel().getColumn(COL_SALESTART).setMinWidth(120);
        tbSaleList.getColumnModel().getColumn(COL_SALESTART).setMaxWidth(120);

        // Col sale end date
        tbSaleList.getColumnModel().getColumn(COL_SALEEND).setCellEditor(new DateCellEditor());
        tbSaleList.getColumnModel().getColumn(COL_SALEEND).setMinWidth(120);
        tbSaleList.getColumnModel().getColumn(COL_SALEEND).setMaxWidth(120);

        // Col sale amount
        tbSaleList.getColumnModel().getColumn(COL_SALEAMOUNT).setCellEditor(new SpinnerCellEditor(SalesOff.MIN_SALE, SalesOff.MAX_SALE));
        tbSaleList.getColumnModel().getColumn(COL_SALEAMOUNT).setMinWidth(90);
        tbSaleList.getColumnModel().getColumn(COL_SALEAMOUNT).setMaxWidth(90);

        // Col pro ID
        tbProList.getColumnModel().getColumn(COL_PROID).setMinWidth(20);
        tbProList.getColumnModel().getColumn(COL_PROID).setMaxWidth(40);

        // Col pro name
        tbProList.getColumnModel().getColumn(COL_PRONAME).setMinWidth(150);

        // Col branch
        tbProList.getColumnModel().getColumn(COL_BRANAME).setMinWidth(120);
        tbProList.getColumnModel().getColumn(COL_BRANAME).setMaxWidth(120);

        // Col sales off enable
        tbProList.getColumnModel().getColumn(COL_PROSALE).setMinWidth(90);
        tbProList.getColumnModel().getColumn(COL_PROSALE).setMaxWidth(90);

        // Bat su kien select row tren table sales off
        tbSaleList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchSaleAction();
                setButtonEnabled(true);
            } else {
                setButtonEnabled(false);
            }
        });

        // Bat su kien select row tren table product
        tbProList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchProAction();
            }
        });

        // Set table cell listener to update table sales off
        TableCellListener tcl1 = new TableCellListener(tbSaleList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                switch (tcl.getColumn()) {
                    case COL_SALENAME:
                        selectedSale.setSaleName((String) tcl.getNewValue());
                        break;
                    case COL_SALESTART:
                        selectedSale.setSaleStartDate((Date) tcl.getNewValue());
                        break;
                    case COL_SALEEND:
                        selectedSale.setSaleEndDate((Date) tcl.getNewValue());
                        break;
                    case COL_SALEAMOUNT:
                        selectedSale.setSaleAmount((float) tcl.getNewValue() / 100);
                        break;
                }
                updateSaleAction();
            }
        });

        // Set table cell listener to update table product
        TableCellListener tcl2 = new TableCellListener(tbProList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
//                scrollToRow(tbProList, tcl.getRow());

                switch (tcl.getColumn()) {
                    case COL_PROSALE:
                        selectedPro.setSalesOff((boolean) tcl.getNewValue());
                        selectedPro.setSaleID(selectedPro.isSalesOff() ? selectedSale.getSaleID() : 0);
                        break;
                }
                updateProAction();
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
        tbSaleList = new javax.swing.JTable();
        btAddSale = new javax.swing.JButton();
        btRemoveSale = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        btClose = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProList = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SalesOff Management");
        setMinimumSize(new java.awt.Dimension(712, 600));
        setModal(true);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "SalesOff List"));

        tbSaleList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Hoang", "3", "0936031044", "10"},
                {"2", "Tri", "2", "123123123", "5"},
                {"3", "Son", "0", "3434343434", "1"},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Start", "End", "Amount(%)"
            }
        ));
        tbSaleList.setRowHeight(25);
        tbSaleList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbSaleList);

        btAddSale.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btAddSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Add3.png"))); // NOI18N
        btAddSale.setText("Add");
        btAddSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddSaleActionPerformed(evt);
            }
        });

        btRemoveSale.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRemoveSale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete2.png"))); // NOI18N
        btRemoveSale.setText("Remove");
        btRemoveSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveSaleActionPerformed(evt);
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

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "SalesOff Details"));

        tbProList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Nokia Lumia 920", "Nokia", "true"},
                {"2", "Samsung Note 3", "Samsung", "false"},
                {"3", "LG One", "LG", "true"},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Product Name", "Branch", "Sales Off"
            }
        ));
        tbProList.setRowHeight(25);
        tbProList.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbProList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAddSale, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btRemoveSale, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btClose, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAddSale, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRemoveSale, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btClose, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
//<editor-fold defaultstate="collapsed" desc="Bat su kien">
    private void btCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btCloseActionPerformed

    private void btAddSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddSaleActionPerformed
        insertSaleAction();
    }//GEN-LAST:event_btAddSaleActionPerformed

    private void btRemoveSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveSaleActionPerformed
        deleteSaleAction();
    }//GEN-LAST:event_btRemoveSaleActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        refreshAction(true);
    }//GEN-LAST:event_btRefreshActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddSale;
    private javax.swing.JButton btClose;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemoveSale;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tbProList;
    private javax.swing.JTable tbSaleList;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="xu ly cho table salesoff">
    private void fetchSaleAction() {
        selectedSaleRowIndex = tbSaleList.getSelectedRow();
        selectedSale.setSaleID((int) tbSaleList.getValueAt(selectedSaleRowIndex, 0));
        selectedSale.setSaleName(((String) tbSaleList.getValueAt(selectedSaleRowIndex, 1)).trim());
        selectedSale.setSaleStartDate((Date) tbSaleList.getValueAt(selectedSaleRowIndex, 2));
        selectedSale.setSaleEndDate((Date) tbSaleList.getValueAt(selectedSaleRowIndex, 3));
        selectedSale.setSaleAmount((float) tbSaleList.getValueAt(selectedSaleRowIndex, 4) / 100);

        // Reload table product list voi SalesOff moi chon
        salesOffProductTableModel.load(selectedSale.getSaleID());
    }

    private void insertSaleAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = salesOffTableModel.insert(new SalesOff());
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.INSERT_SUCCESS : SwingUtils.INSERT_FAIL);
        // Select row vua insert vao
        selectedSaleRowIndex = tbSaleList.getRowCount() - 1;
        scrollToRow(tbSaleList, selectedSaleRowIndex);
    }

    private void updateSaleAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = salesOffTableModel.update(selectedSale);
        refreshAction(false);
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.UPDATE_SUCCESS : SwingUtils.UPDATE_FAIL);
    }

    private void deleteSaleAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = salesOffTableModel.delete(selectedSale);
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.DELETE_SUCCESS : SwingUtils.DELETE_FAIL);

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedSaleRowIndex = (selectedSaleRowIndex == tbSaleList.getRowCount() ? tbSaleList.getRowCount() - 1 : selectedSaleRowIndex++);
        scrollToRow(tbSaleList, selectedSaleRowIndex);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="xu ly cho table product list">
    private void fetchProAction() {
        selectedProRowIndex = tbProList.getSelectedRow();
        selectedPro.setProID((int) tbProList.getValueAt(selectedProRowIndex, 0));
        selectedPro.setProName(((String) tbProList.getValueAt(selectedProRowIndex, 1)).trim());
        selectedPro.setBraName((String) tbProList.getValueAt(selectedProRowIndex, 2));
        selectedPro.setSalesOff((boolean) tbProList.getValueAt(selectedProRowIndex, 3));
    }
    
    private void updateProAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = salesOffProductTableModel.update(selectedPro);
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.UPDATE_SUCCESS : SwingUtils.UPDATE_FAIL);
//        scrollToRow(tbSaleList, selectedSaleRowIndex);
    }
//</editor-fold>
    
    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            salesOffTableModel.refresh();
            setCursor(null);
            SwingUtils.showInfoDialog(SwingUtils.DB_REFRESH);
        } else {
            salesOffTableModel.refresh();
        }
        scrollToRow(tbSaleList, selectedSaleRowIndex);
    }
    
    private void scrollToRow(JTable table, int row) {
        table.getSelectionModel().setSelectionInterval(row, row);
        table.scrollRectToVisible(new Rectangle(table.getCellRect(row, 0, true)));
    }
//</editor-fold>

    private void setButtonEnabled(boolean enabled, JButton... exclude) {
        btAddSale.setEnabled(enabled);
        btRemoveSale.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }
}
