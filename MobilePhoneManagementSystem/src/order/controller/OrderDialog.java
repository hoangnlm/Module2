package order.controller;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import main.controller.LoginConfig;
import order.model.Order;
import order.model.OrderCustomer;
import order.model.OrderProduct;
import utility.CurrencyCellRenderer;
import utility.PercentCellRenderer;
import utility.SpinnerCellEditor;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class OrderDialog extends javax.swing.JDialog {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OrderDialog(null).setVisible(true);
            }
        });
    }

    private Order order;

    // Khai bao model
    private OrderProductTableModelDialog orderProductTableModelDialog;
    private OrderStatusComboBoxModel orderStatusComboBoxModel;
    private OrderStatusComboBoxRenderer orderStatusComboBoxRenderer;
    private OrderCustomerComboBoxModel orderCustomerComboBoxModel;
    private OrderCustomerComboBoxRenderer orderCustomerComboBoxRenderer;
    private OrderProductComboBoxModel orderProductComboBoxModel;

    // Product dang duoc chon trong table product list
    private OrderProduct selectedProduct;
    private int selectedRowIndex;

    private static final int COL_PRONO = 0;
    private static final int COL_PRONAME = 1;
    private static final int COL_PROQTY = 2;
    private static final int COL_PROPRICE1 = 3;
    private static final int COL_SALEAMOUNT = 4;
    private static final int COL_PROPRICE2 = 5;

    // Two mode: insert va update
    private boolean insertMode;

    public OrderDialog(Order order) {
        super((JFrame) null, true);
        initComponents();
        setLocationRelativeTo(null);
        insertMode = order == null;

        //Disable button khi moi khoi dong len
        btRemove.setEnabled(false);
        btSave.setEnabled(false);

        // Set data cho combobox status
        orderStatusComboBoxModel = new OrderStatusComboBoxModel();
        orderStatusComboBoxRenderer = new OrderStatusComboBoxRenderer();
        cbStatus.setModel(orderStatusComboBoxModel);
        cbStatus.setRenderer(orderStatusComboBoxRenderer);

        // Set data cho combobox customer
        orderCustomerComboBoxModel = new OrderCustomerComboBoxModel();
        orderCustomerComboBoxRenderer = new OrderCustomerComboBoxRenderer();
        cbCustomer.setModel(orderCustomerComboBoxModel);
        cbCustomer.setRenderer(orderCustomerComboBoxRenderer);

        // Set data cho combobox product name
        orderProductComboBoxModel = new OrderProductComboBoxModel();

        // Set data cho table
        orderProductTableModelDialog = new OrderProductTableModelDialog();
        tbProduct.setModel(orderProductTableModelDialog);

        // Set auto define column from model to false to stop create column again
        tbProduct.setAutoCreateColumnsFromModel(false);
        tbProduct.setAutoCreateRowSorter(true);

        // Set height cho table header
        tbProduct.getTableHeader().setPreferredSize(new Dimension(300, 30));

        // Col pro No
        tbProduct.getColumnModel().getColumn(COL_PRONO).setMinWidth(40);
        tbProduct.getColumnModel().getColumn(COL_PRONO).setMaxWidth(50);

        // Col pro name
        tbProduct.getColumnModel().getColumn(COL_PRONAME).setMinWidth(500);
        tbProduct.getColumnModel().getColumn(COL_PRONAME).setCellEditor(new OrderProductComboBoxCellEditor(orderProductComboBoxModel));

        // Col quantity
        tbProduct.getColumnModel().getColumn(COL_PROQTY).setMinWidth(50);
        tbProduct.getColumnModel().getColumn(COL_PROQTY).setCellEditor(new SpinnerCellEditor(1, 10));

        // Col price 1
        tbProduct.getColumnModel().getColumn(COL_PROPRICE1).setMinWidth(100);
        tbProduct.getColumnModel().getColumn(COL_PROPRICE1).setCellRenderer(new CurrencyCellRenderer());

        // Col salesoff
        tbProduct.getColumnModel().getColumn(COL_SALEAMOUNT).setMinWidth(100);
        tbProduct.getColumnModel().getColumn(COL_SALEAMOUNT).setCellRenderer(new PercentCellRenderer());

        // Col price 2
        tbProduct.getColumnModel().getColumn(COL_PROPRICE2).setMinWidth(100);
        tbProduct.getColumnModel().getColumn(COL_PROPRICE2).setCellRenderer(new CurrencyCellRenderer());

        // Bat su kien select row tren table product
        tbProduct.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                btRemove.setEnabled(true);
            } else {
                btRemove.setEnabled(false);
            }
        });

        // Xu ly mode
        if (insertMode) { // Mode insert
            setTitle("New Order");
            this.order = new Order();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tfDate.setText(dateFormat.format(new Date()));
            tfUser.setText(LoginConfig.USER_NAME);
            cbStatus.setSelectedIndex(0);
            cbCustomer.setSelectedIndex(cbCustomer.getItemCount() - 1);
            orderProductTableModelDialog.load(-1);    //Emply list

            // Load data cho combobox col product name trong table
//            orderProductComboBoxModel.load(order.getOrdID());
        } else { // Mode update
            setTitle("Update Order");
            this.order = order;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tfDate.setText(dateFormat.format(order.getOrdDate()));
            tfUser.setText(order.getUserName());
            cbStatus.setSelectedItem(orderStatusComboBoxModel.getStatusFromValue(order.getOrdStatus()));
//            System.out.println("cusID: "+order.getCusID());
//            System.out.println("cus: "+orderCustomerComboBoxModel.getCustomerFromID(order.getCusID()));
            cbCustomer.setSelectedItem(orderCustomerComboBoxModel.getCustomerFromID(order.getCusID()));
            orderProductTableModelDialog.load(order.getOrdID());
        }
    }

//<editor-fold defaultstate="collapsed" desc="Bat su kien">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tfDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbCustomer = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        tfUser = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbProduct = new javax.swing.JTable();
        btAdd = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        btReset = new javax.swing.JButton();
        btSave = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lbItems = new javax.swing.JLabel();
        lbTotal = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbDiscount = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lbFinal = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Order");
        setMaximumSize(new java.awt.Dimension(9999, 9999));
        setMinimumSize(new java.awt.Dimension(800, 607));
        setPreferredSize(new java.awt.Dimension(900, 607));
        setSize(new java.awt.Dimension(800, 607));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Order Details"));

        jLabel3.setText("Date:");

        tfDate.setEditable(false);
        tfDate.setFocusable(false);

        jLabel4.setText("Customer:");

        cbCustomer.setPreferredSize(new java.awt.Dimension(800, 30));

        jLabel6.setText("Status:");

        jLabel7.setText("Seller:");

        tfUser.setEditable(false);
        tfUser.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(tfDate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbCustomer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7)
                    .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Product Details"));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(250, 404));

        tbProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Name", "Qty", "Price1", "Sales (%)", "Price2"
            }
        ));
        tbProduct.setPreferredSize(new java.awt.Dimension(200, 100));
        tbProduct.setRowHeight(25);
        tbProduct.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbProduct.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbProduct);

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

        btReset.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Refresh2.png"))); // NOI18N
        btReset.setText("Reset");
        btReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btReset, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btReset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );

        btSave.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/OK2.png"))); // NOI18N
        btSave.setText("Save");
        btSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveActionPerformed(evt);
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

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Summary"));

        jLabel5.setText("Total items:");

        lbItems.setText("10");

        lbTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTotal.setText("10,000,000 VND");

        jLabel10.setText("Total money:");

        lbDiscount.setText("2%");

        jLabel12.setText("Discount:");

        lbFinal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbFinal.setText("10,000,000 VND");
        lbFinal.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel14.setText("Final money:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbItems, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(lbFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lbItems)
                    .addComponent(jLabel10)
                    .addComponent(lbTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(lbDiscount))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(lbFinal)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        cancelAction();
    }//GEN-LAST:event_btCancelActionPerformed

    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        insertAction();
    }//GEN-LAST:event_btAddActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        deleteAction();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void btResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResetActionPerformed
        refreshAction(true);
    }//GEN-LAST:event_btResetActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveActionPerformed
        saveAction();
    }//GEN-LAST:event_btSaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btReset;
    private javax.swing.JButton btSave;
    private javax.swing.JComboBox<OrderCustomer> cbCustomer;
    private javax.swing.JComboBox<order.model.OrderStatus> cbStatus;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbDiscount;
    private javax.swing.JLabel lbFinal;
    private javax.swing.JLabel lbItems;
    private javax.swing.JLabel lbTotal;
    private javax.swing.JTable tbProduct;
    private javax.swing.JTextField tfDate;
    private javax.swing.JTextField tfUser;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    private void insertAction() {

    }

    private void deleteAction() {

    }

    private void saveAction() {

    }

    private void cancelAction() {
        dispose();
    }

    private void fetchAction() {
        if (tbProduct.getSelectedRow() >= 0) {
            selectedRowIndex = tbProduct.convertRowIndexToModel(tbProduct.getSelectedRow());
            selectedProduct = orderProductTableModelDialog.getOrderProductFromIndex(selectedRowIndex);
            orderProductTableModelDialog.setSelectingIndex(selectedRowIndex);
        }
    }

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            orderProductTableModelDialog.refresh();
            setCursor(null);
            SwingUtils.showInfoDialog(SwingUtils.DB_REFRESH);
        } else {
            orderProductTableModelDialog.refresh();
        }

        scrollToRow(selectedRowIndex);
    }

    private void scrollToRow(int row) {
        tbProduct.getSelectionModel().setSelectionInterval(row, row);
        tbProduct.scrollRectToVisible(new Rectangle(tbProduct.getCellRect(row, 0, true)));
    }
}
