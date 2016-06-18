package order.controller;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import main.controller.LoginConfig;
import order.model.Order;
import order.model.OrderBranch;
import order.model.OrderCustomer;
import order.model.OrderProduct;
import utility.CheckBoxListCellRenderer;
import utility.CurrencyCellRenderer;
import utility.PercentCellRenderer;
import utility.SpinnerCellEditor;
import utility.SwingUtils;
import utility.TableCellListener;

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
    private int selectedRowIndex = -1;

    private static final int COL_PRONO = 0;
    private static final int COL_PRONAME = 1;
    private static final int COL_PROQTY = 2;
    private static final int COL_PROPRICE1 = 3;
    private static final int COL_SALEAMOUNT = 4;
    private static final int COL_PROPRICE2 = 5;

    // Two mode: insert va update
    private boolean insertMode;

    // Flag de theo doi co thay doi noi dung gi khong
    private boolean trackChanges;

    // List de save vao database
    private List<OrderProduct> proList;

    public OrderDialog(Order order) {
        super((JFrame) null, true);
        initComponents();
        setLocationRelativeTo(null);
        insertMode = order == null;

        // Disable button khi moi khoi dong len
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
        cbCustomer.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateDiscountLabel();
                }
            }
        });

        // Set data cho combobox product name
        orderProductComboBoxModel = new OrderProductComboBoxModel();

        // Set data cho table
        orderProductTableModelDialog = new OrderProductTableModelDialog();
        tbProduct.setModel(orderProductTableModelDialog);

        // Set height cho table header
        tbProduct.getTableHeader().setPreferredSize(new Dimension(300, 30));

        // Col pro No
        tbProduct.getColumnModel().getColumn(COL_PRONO).setMinWidth(40);
        tbProduct.getColumnModel().getColumn(COL_PRONO).setMaxWidth(50);

        // Col pro name
        tbProduct.getColumnModel().getColumn(COL_PRONAME).setMinWidth(450);
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
        tbProduct.getColumnModel().getColumn(COL_PROPRICE2).setMinWidth(110);
        tbProduct.getColumnModel().getColumn(COL_PROPRICE2).setCellRenderer(new CurrencyCellRenderer());

        // Bat su kien select row tren table product
        tbProduct.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                if (orderProductTableModelDialog.getRowCount() > 1) {
                    btRemove.setEnabled(true);
                }
            } else {
                btRemove.setEnabled(false);
            }
        });

        //<editor-fold defaultstate="collapsed" desc="Set cell listener cho updating">
        TableCellListener tcl = new TableCellListener(tbProduct, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
//                System.out.println("Row   : " + tcl.getRow());
//                System.out.println("Column: " + tcl.getColumn());
//                System.out.println("Old   : " + tcl.getOldValue());
//                System.out.println("New   : " + tcl.getNewValue());

                switch (tcl.getColumn()) {
                    case COL_PRONAME:
                        // Check duplicate product
                        if (checkDuplicate((String) tcl.getNewValue())) {
                            SwingUtils.showErrorDialog("Duplicated item is not allowed in an order !");
                            tbProduct.setValueAt(tcl.getOldValue(), tbProduct.getSelectedRow(), tbProduct.getSelectedColumn());
                        } else {

                            // Lay product moi tu combo box gan cho product
                            // hien tai
                            selectedProduct = orderProductComboBoxModel.getOrderProductFromName((String) tcl.getNewValue());
                            tbProduct.setValueAt(selectedProduct.getProPrice1(), tbProduct.getSelectedRow(), COL_PROPRICE1);
                            tbProduct.setValueAt(selectedProduct.getSalesOffAmount(), tbProduct.getSelectedRow(), COL_SALEAMOUNT);
                            tbProduct.setValueAt(selectedProduct.getProPrice1() * (1 - selectedProduct.getSalesOffAmount()), tbProduct.getSelectedRow(), COL_PROPRICE2);

                            // Gan quantity moi cho product hien tai
                            selectedProduct.setProQty((int) tbProduct.getValueAt(tbProduct.getSelectedRow(), COL_PROQTY));

                            // Update label
                            updateTotalLabel();
                        }
                        break;
                }

                trackChanges = true;
                btSave.setEnabled(true);
            }
        });
//</editor-fold>

        // Xu ly mode
        if (insertMode) { // Mode insert
            setTitle("New Order");
            this.order = new Order();
            this.order.setOrdID(-1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tfDate.setText(dateFormat.format(new Date()));
            tfUser.setText(LoginConfig.USER_NAME);
            cbStatus.setSelectedIndex(0);
            cbCustomer.setSelectedIndex(cbCustomer.getItemCount() - 1);
        } else { // Mode update
            setTitle("Update Order");
            this.order = order;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tfDate.setText(dateFormat.format(order.getOrdDate()));
            tfUser.setText(order.getUserName());
            cbStatus.setSelectedItem(orderStatusComboBoxModel.getStatusFromValue(order.getOrdStatus()));
            cbCustomer.setSelectedItem(orderCustomerComboBoxModel.getCustomerFromID(order.getCusID()));
        }

        // Set data cho table chinh
        orderProductTableModelDialog.load(this.order.getOrdID());    //Emply list

        // Set data cho cac label
        updateItemsLabel();
        updateDiscountLabel();

//<editor-fold defaultstate="collapsed" desc="xu ly cho vung filter">
// Set data cho list filter
        list.setModel(new OrderBranchListModel());
        list.setCellRenderer(new OrderBranchListCellRenderer());
        list.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                } else {
                    super.addSelectionInterval(index0, index1);
                }
            }
        });
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                orderProductComboBoxModel.setBraList((List<OrderBranch>) list.getSelectedValuesList());
                orderProductComboBoxModel.filter();
            }
        });

// Su kien cho filter name
        tfNameFilter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                orderProductComboBoxModel.setProName(tfNameFilter.getText().trim());
                orderProductComboBoxModel.filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                orderProductComboBoxModel.setProName(tfNameFilter.getText().trim());
                orderProductComboBoxModel.filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                orderProductComboBoxModel.setProName(tfNameFilter.getText().trim());
                orderProductComboBoxModel.filter();
            }
        });
//</editor-fold>
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
        btAdd = new javax.swing.JButton();
        btRemove = new javax.swing.JButton();
        btReset = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProduct = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btClear = new javax.swing.JButton();
        tfNameFilter = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        list = new javax.swing.JList<>();
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
        setMinimumSize(new java.awt.Dimension(900, 700));
        setPreferredSize(new java.awt.Dimension(900, 700));
        setSize(new java.awt.Dimension(900, 700));

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

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Order Details"));

        tbProduct.setAutoCreateRowSorter(true);
        tbProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Product Name", "Quantity", "Price 1", "SalesOff", "Price 2"
            }
        ));
        tbProduct.setFillsViewportHeight(true);
        tbProduct.setRowHeight(25);
        tbProduct.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbProduct.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbProduct);

        jLabel1.setText("Name:");
        jLabel1.setPreferredSize(new java.awt.Dimension(95, 20));

        btClear.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        btClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/trash_full.png"))); // NOI18N
        btClear.setText("Clear Filter");
        btClear.setPreferredSize(new java.awt.Dimension(100, 20));
        btClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearActionPerformed(evt);
            }
        });

        tfNameFilter.setPreferredSize(new java.awt.Dimension(250, 20));

        jScrollPane3.setMinimumSize(new java.awt.Dimension(200, 30));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(604, 50));
        jScrollPane3.setViewportView(list);

        list.setBackground(new java.awt.Color(51, 51, 51));
        list.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        list.setMaximumSize(new java.awt.Dimension(99999, 9999));
        list.setMinimumSize(new java.awt.Dimension(600, 30));
        list.setPreferredSize(new java.awt.Dimension(600, 80));
        list.setSize(new java.awt.Dimension(600, 30));
        list.setVisibleRowCount(-1);
        jScrollPane3.setViewportView(list);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btReset, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btReset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))
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
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbItems, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lbTotal)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(lbItems)))
                .addGap(4, 4, 4)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(lbDiscount))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(lbFinal)))
                .addContainerGap())
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        resetAction(true);
    }//GEN-LAST:event_btResetActionPerformed

    private void btSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveActionPerformed
        updateAction();
    }//GEN-LAST:event_btSaveActionPerformed

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        clearFilter();
    }//GEN-LAST:event_btClearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btClear;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btReset;
    private javax.swing.JButton btSave;
    private javax.swing.JComboBox<OrderCustomer> cbCustomer;
    private javax.swing.JComboBox<order.model.OrderStatus> cbStatus;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbDiscount;
    private javax.swing.JLabel lbFinal;
    private javax.swing.JLabel lbItems;
    private javax.swing.JLabel lbTotal;
    private javax.swing.JList<OrderBranch> list;
    private javax.swing.JTable tbProduct;
    private javax.swing.JTextField tfDate;
    private javax.swing.JTextField tfNameFilter;
    private javax.swing.JTextField tfUser;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    private void insertAction() {
        // Toi da 20 items
        if (orderProductTableModelDialog.getRowCount() == 19) {
            SwingUtils.showInfoDialog("Maximum 20 item in 01 order !");
            btAdd.setEnabled(false);
        }
        OrderProduct product = new OrderProduct();
        product.setProNo(orderProductTableModelDialog.getRowCount() + 1);
        product.setProQty(1);
        product.setProName(OrderProduct.DEFAULT_PRONAME);
        orderProductTableModelDialog.insert(product);
        scrollToRow(tbProduct.getRowCount() - 1);
        trackChanges = true;
        btSave.setEnabled(true);
        updateItemsLabel();
    }

    private void deleteAction() {
        btAdd.setEnabled(true);

        // Toi thieu 01 item
        if (orderProductTableModelDialog.getRowCount() == 2) {
            SwingUtils.showInfoDialog("At least 01 item in 01 order !");
            btRemove.setEnabled(false);
        }

        orderProductTableModelDialog.delete(selectedProduct);
        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbProduct.getRowCount() ? tbProduct.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
        trackChanges = true;
        btSave.setEnabled(true);
        updateItemsLabel();
    }

    private void updateAction() {
        if (checkValidate()) {
            System.out.println("list size: " + orderProductTableModelDialog.getList().size());
            System.out.println("list: " + orderProductTableModelDialog.getList());

        } else {
            SwingUtils.showInfoDialog("Save dang xu ly!");
        }
    }

    private void cancelAction() {
        if (trackChanges) {
            if (SwingUtils.showConfirmDialog("Discard change(s) and quit ?") == JOptionPane.YES_OPTION) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    private void fetchAction() {
        selectedRowIndex = tbProduct.getSelectedRow();
        if (selectedRowIndex >= 0) {
            int idx = tbProduct.convertRowIndexToModel(selectedRowIndex);
            selectedProduct = orderProductTableModelDialog.getOrderProductFromIndex(idx);
            orderProductTableModelDialog.setSelectingIndex(idx);
        } else {
            selectedProduct = null;
            orderProductTableModelDialog.setSelectingIndex(-1);
        }
    }

    private void resetAction(boolean mustInfo) {
        orderProductTableModelDialog.load(order.getOrdID());
        if (mustInfo) {
            SwingUtils.showInfoDialog(SwingUtils.DB_RESET);
        }
        if (insertMode) {
            btSave.setEnabled(false);
        }
    }

    private boolean checkValidate() {

        return true;
    }

    private boolean checkDuplicate(String proName) {
        
        return false;
    }

    private void updateItemsLabel() {
        if (tbProduct.getRowCount() > 0) {
            lbItems.setText(String.format("%02d", tbProduct.getRowCount()));
        } else {
            lbItems.setText("0");
        }
        updateTotalLabel();
    }

    private void updateDiscountLabel() {
        OrderCustomer oc = (OrderCustomer) cbCustomer.getSelectedItem();
        NumberFormat format = NumberFormat.getPercentInstance();
        lbDiscount.setText(format.format(oc.getCusDiscount()));
        updateFinalLabel();
    }

    private void updateTotalLabel() {
        float sum = 0;
        if (tbProduct.getRowCount() > 0) {
            for (int i = 0; i < tbProduct.getRowCount(); i++) {
                sum += (float) tbProduct.getValueAt(i, COL_PROPRICE2);
            }
        }
        lbTotal.setText(String.format("%,.0f Đ", (float) sum));
        updateFinalLabel();
    }

    private void updateFinalLabel() {
        float sum = 0;
        if (tbProduct.getRowCount() > 0) {
            for (int i = 0; i < tbProduct.getRowCount(); i++) {
                sum += (float) tbProduct.getValueAt(i, COL_PROPRICE2);
            }
            OrderCustomer oc = (OrderCustomer) cbCustomer.getSelectedItem();
            sum = sum * (1 - oc.getCusDiscount());
        }
        lbFinal.setText(String.format("%,.0f Đ", (float) sum));
    }

    private void clearFilter() {
        list.getSelectionModel().clearSelection();
        tfNameFilter.setText(null);
    }

    private void scrollToRow(int row) {
        tbProduct.getSelectionModel().setSelectionInterval(row, row);
        tbProduct.scrollRectToVisible(new Rectangle(tbProduct.getCellRect(row, 0, true)));
    }
}
