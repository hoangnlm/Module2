package service.controller;

import order.controller.*;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import main.model.Login;
import order.model.OrderBranch;
import order.model.OrderProduct;
import service.model.Service;
import service.model.ServiceDetails;
import service.model.ServiceStatus;
import service.model.ServiceType;
import utility.SpinnerCellEditor;
import utility.SwingUtils;
import utility.SwingUtils.FormatType;
import utility.TableCellListener;

/**
 *
 * @author BonBon
 */
public class ServiceDialog extends javax.swing.JDialog implements ItemListener {

    private Service backup;
    private Service service;

    // Khai bao model
    private ServiceDetailsTableModelDialog serviceDetailsTableModelDialog;
    private ServiceStatusComboBoxModel serviceStatusComboBoxModel;
    private ServiceStatusComboBoxRenderer serviceStatusComboBoxRenderer;
    private ServiceTypeComboBoxModel serviceTypeComboBoxModel;
    private ServiceTypeComboBoxRenderer serviceTypeComboBoxRenderer;
    private ServiceDetailsComboBoxModel serviceDetailsComboBoxModel;

    // Product dang duoc chon trong table product list
    private ServiceDetails selectedDetails;
    private int selectedRowIndex = -1;

    public static final int COL_SERID = 0;
    public static final int COL_PRONAME = 1;
    public static final int COL_BRANAME = 2;
    public static final int COL_CONTENT = 3;

    public static final int COL_PROQTY = 4;
    public static final int COL_ODERID = 5;

    // Two mode: insert va update
    private boolean insertMode;

    // Flag de theo doi co thay doi noi dung gi khong
    private boolean trackChanges;

    // List de save vao database
    private List<ServiceDetails> serviceDetails;

    public ServiceDialog(Service service) {
        super((JFrame) null, true);
        initComponents();
        setLocationRelativeTo(null);
        insertMode = service == null;

        // Set data cho combobox status
        serviceStatusComboBoxModel = new ServiceStatusComboBoxModel();
        serviceStatusComboBoxRenderer = new ServiceStatusComboBoxRenderer();
        cbStatus.setModel(serviceStatusComboBoxModel);
        cbStatus.setRenderer(serviceStatusComboBoxRenderer);
        cbStatus.addItemListener(this);

        // Set data cho combobox customer
        serviceTypeComboBoxModel = new ServiceTypeComboBoxModel();
        serviceTypeComboBoxRenderer = new ServiceTypeComboBoxRenderer();
        cbType.setModel(serviceTypeComboBoxModel);
        cbType.setRenderer(serviceTypeComboBoxRenderer);
        cbType.addItemListener(this);

        // Set data cho combobox product name
        serviceDetailsComboBoxModel = new ServiceDetailsComboBoxModel();

        // Set data cho table
        serviceDetailsTableModelDialog = new ServiceDetailsTableModelDialog();
        tbProduct.setModel(serviceDetailsTableModelDialog);

        // Set height cho table header
        tbProduct.getTableHeader().setPreferredSize(new Dimension(300, 30));

        // Col Ser ID (HIDDEN)
        tbProduct.getColumnModel().getColumn(COL_SERID).setMinWidth(0);
        tbProduct.getColumnModel().getColumn(COL_SERID).setMaxWidth(0);
        // Col pro name
        tbProduct.getColumnModel().getColumn(COL_PRONAME).setMinWidth(300);
        tbProduct.getColumnModel().getColumn(COL_PRONAME).setCellEditor(new ServiceDetailsComboBoxCellEditor(serviceDetailsComboBoxModel));
        // Col braname
        tbProduct.getColumnModel().getColumn(COL_BRANAME).setMinWidth(100);

        // Col content
        tbProduct.getColumnModel().getColumn(COL_CONTENT).setMinWidth(150);
        tbProduct.getColumnModel().getColumn(COL_CONTENT).setCellRenderer(new DefaultTableCellRenderer());

        // Col quantity
        tbProduct.getColumnModel().getColumn(COL_PROQTY).setMinWidth(50);
        tbProduct.getColumnModel().getColumn(COL_PROQTY).setCellEditor(new SpinnerCellEditor(1, 10));

        // Col oderid
        tbProduct.getColumnModel().getColumn(COL_ODERID).setMinWidth(50);
        tbProduct.getColumnModel().getColumn(COL_ODERID).setCellEditor(new SpinnerCellEditor(1, 10000));

        // Bat su kien select row tren table product
        tbProduct.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                if (serviceDetailsTableModelDialog.getRowCount() > 1) {
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
                switch (tcl.getColumn()) {
                    case COL_PRONAME:
                        String oldValue = (String) tcl.getOldValue();
                        String newValue = (String) tcl.getNewValue();
                        if (!newValue.equals(ServiceDetails.DEFAULT_PRONAME)) {
                            // Check duplicate product
                            if (checkDuplicate((String) tcl.getNewValue())) {
                                SwingUtils.showErrorDialog("Duplicated item is not allowed in an order !");
                                tbProduct.setValueAt(oldValue, tbProduct.getSelectedRow(), COL_PRONAME);
//                            } else if (checkStockZero((String) tcl.getNewValue())) {
//                                SwingUtils.showErrorDialog("This product is out of stock.\nPlease choose another !");
//                                tbProduct.setValueAt(oldValue, tbProduct.getSelectedRow(), COL_PRONAME);
                            } else {
                                // Lay product moi tu combo box gan cho product
                                // trong row cua table
                                selectedDetails = serviceDetailsComboBoxModel.getProductFromName((String) tcl.getNewValue());
//                                /// Gan quantity moi cho product hien tai
                                selectedDetails.setProQty((int) tbProduct.getValueAt(tbProduct.getSelectedRow(), COL_PROQTY));
                                // Update cac thuoc tinh cua product moi vao table
                                tbProduct.setValueAt(selectedDetails.getBraName(), tbProduct.getSelectedRow(), COL_BRANAME);
                                tbProduct.setValueAt(selectedDetails.getSerContent(), tbProduct.getSelectedRow(), COL_CONTENT);
                                tbProduct.setValueAt(selectedDetails.getOrdID(), tbProduct.getSelectedRow(), COL_ODERID);
                                tbProduct.setValueAt(selectedDetails.getSerID(), tbProduct.getSelectedRow(), COL_SERID);
                                // Update label
//                                updateTotalLabel();
                                setTrackChanges(true);
                            }
                        } else if (!newValue.equals(oldValue)) {
                            tbProduct.setValueAt(oldValue, tbProduct.getSelectedRow(), COL_PRONAME);
                        }
                        break;
                    case COL_PROQTY:
                        // Gan quantity moi cho product tren row
                        selectedDetails.setProQty((int) tbProduct.getValueAt(tbProduct.getSelectedRow(), COL_PROQTY));
                        // Update label
                        updateItemsLabel();
                        setTrackChanges(true);
                        break;
                    case COL_ODERID:
                        // Gan quantity moi cho product tren row
                        selectedDetails.setOrdID((int) tbProduct.getValueAt(tbProduct.getSelectedRow(), COL_ODERID));
                        // Update label
                        updateItemsLabel();
                        setTrackChanges(true);
                        break;
                    case COL_CONTENT:
                        // Gan quantity moi cho product tren row
                        selectedDetails.setSerContent((String) tbProduct.getValueAt(tbProduct.getSelectedRow(), COL_CONTENT));
                        // Update label
                        updateItemsLabel();
                        setTrackChanges(true);
                        break;
                }
            }
        });

//</editor-fold>
        // Xu ly mode
        if (insertMode) { // Mode insert
            setTitle("New Service");
            this.service = new Service();
            this.service.setSerID(-1);
            this.service.setSerStatusID(serviceStatusComboBoxModel.getElementAt(0).getSttID());
            this.service.setSerStatus(serviceStatusComboBoxModel.getElementAt(0).getSttName());
            this.service.setSerTypeID(serviceTypeComboBoxModel.getElementAt(serviceTypeComboBoxModel.getSize() - 1).getSerTypeID());
            this.service.setSerTypeName(serviceTypeComboBoxModel.getElementAt(serviceTypeComboBoxModel.getSize() - 1).getSerTypeName());
            this.service.setUserID(1);
            this.service.setReceiveDate(new Date());
            this.service.setReturnDate(new Date());
            backup = this.service.clone(); // Backup

            tfID.setText("New");
            tfReceiveDate.setText(SwingUtils.formatString(new Date(), FormatType.DATE));
            tfReturnDate.setText(SwingUtils.formatString(new Date(), FormatType.DATE));
            tfUser.setText(Login.USER_NAME);
            cbStatus.setSelectedIndex(0);
            cbType.setSelectedIndex(cbType.getItemCount() - 1);
            setTrackChanges(false);
        } else { // Mode update
            setTitle("Update Service");
            this.service = service.clone();
            backup = this.service.clone(); // Backup
            tfID.setText(this.service.getSerID() + "");
            tfReceiveDate.setText(SwingUtils.formatString(this.service.getReceiveDate(), FormatType.DATE));
            tfReturnDate.setText(SwingUtils.formatString(this.service.getReturnDate(), FormatType.DATE));
            tfUser.setText(this.service.getUserName());
            cbStatus.setSelectedItem(serviceStatusComboBoxModel.getStatusFromValue(this.service.getSerStatus()));
            cbType.setSelectedItem(serviceTypeComboBoxModel.getServiceTypeNameFromValue(this.service.getSerTypeName()));
            setTrackChanges(false);

//            // Xac nhan update discount khi discount cua customer bi thay doi sau order
//            if (backup.getSerTypeID() != serviceTypeComboBoxModel.getServiceTypeNameFromValue(this.service.getSerID()).getSerTypeID()) {
//                if (SwingUtils.showConfirmDialog("Discount of this order has changed !\nWould you like to update ?") == JOptionPane.NO_OPTION) {
//                    this.service.setSerTypeID(backup.getSerTypeID());
//                } else {
//                    setTrackChanges(true);
//                }
//            }
        }

        // Set data cho table chinh
        serviceDetailsTableModelDialog.load(this.service.getSerID());    //Emply list neu o mode insert

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
                serviceDetailsComboBoxModel.setBraList((List<OrderBranch>) list.getSelectedValuesList());
                serviceDetailsComboBoxModel.filter();
            }
        });

// Su kien cho filter name
        tfNameFilter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                serviceDetailsComboBoxModel.setProName(tfNameFilter.getText().trim());
                serviceDetailsComboBoxModel.filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                serviceDetailsComboBoxModel.setProName(tfNameFilter.getText().trim());
                serviceDetailsComboBoxModel.filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                serviceDetailsComboBoxModel.setProName(tfNameFilter.getText().trim());
                serviceDetailsComboBoxModel.filter();
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
        tfReceiveDate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        tfUser = new javax.swing.JTextField();
        tfID = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tfReturnDate = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbType = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Order");
        setMinimumSize(new java.awt.Dimension(900, 700));
        setSize(new java.awt.Dimension(900, 700));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Service Details"));

        jLabel3.setText("ReceiveDate:");

        tfReceiveDate.setEditable(false);
        tfReceiveDate.setEnabled(false);
        tfReceiveDate.setFocusable(false);

        jLabel6.setText("Status:");

        jLabel7.setText("Technician:");

        tfUser.setEditable(false);
        tfUser.setEnabled(false);
        tfUser.setFocusable(false);

        tfID.setEditable(false);
        tfID.setEnabled(false);
        tfID.setFocusable(false);

        jLabel8.setText("ID:");

        tfReturnDate.setEditable(false);
        tfReturnDate.setEnabled(false);
        tfReturnDate.setFocusable(false);

        jLabel9.setText("ReturnDate:");

        jLabel11.setText("Type:");

        jButton1.setText("Check Warranty");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfUser, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(tfID))
                .addGap(75, 75, 75)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfReturnDate, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(tfReceiveDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbStatus, 0, 176, Short.MAX_VALUE)
                    .addComponent(cbType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(tfReceiveDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfReturnDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Product Details"));

        btAdd.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Add3.png"))); // NOI18N
        btAdd.setText("Add");
        btAdd.setFocusPainted(false);
        btAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddActionPerformed(evt);
            }
        });

        btRemove.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete2.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.setEnabled(false);
        btRemove.setFocusPainted(false);
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        btReset.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Refresh2.png"))); // NOI18N
        btReset.setText("Reset");
        btReset.setFocusPainted(false);
        btReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResetActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Service Details"));

        tbProduct.setAutoCreateRowSorter(true);
        tbProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Name", "Content", "Quantity", "OrderID"
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
        btClear.setFocusPainted(false);
        btClear.setPreferredSize(new java.awt.Dimension(100, 20));
        btClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearActionPerformed(evt);
            }
        });

        tfNameFilter.setPreferredSize(new java.awt.Dimension(250, 20));

        jScrollPane3.setMinimumSize(new java.awt.Dimension(200, 30));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(604, 82));

        list.setBackground(new java.awt.Color(51, 51, 51));
        list.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        list.setMaximumSize(new java.awt.Dimension(99999, 9999));
        list.setMinimumSize(new java.awt.Dimension(600, 30));
        list.setPreferredSize(new java.awt.Dimension(600, 600));
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 211, Short.MAX_VALUE)
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btReset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btSave.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/OK2.png"))); // NOI18N
        btSave.setText("Save");
        btSave.setEnabled(false);
        btSave.setFocusPainted(false);
        btSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveActionPerformed(evt);
            }
        });

        btCancel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Cancel2.png"))); // NOI18N
        btCancel.setText("Cancel");
        btCancel.setFocusPainted(false);
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbItems, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(lbItems))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(lbDiscount))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(lbTotal)))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
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
    private javax.swing.JComboBox<service.model.ServiceStatus> cbStatus;
    private javax.swing.JComboBox<service.model.ServiceType> cbType;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbDiscount;
    private javax.swing.JLabel lbItems;
    private javax.swing.JLabel lbTotal;
    private javax.swing.JList<OrderBranch> list;
    private javax.swing.JTable tbProduct;
    private javax.swing.JTextField tfID;
    private javax.swing.JTextField tfNameFilter;
    private javax.swing.JTextField tfReceiveDate;
    private javax.swing.JTextField tfReturnDate;
    private javax.swing.JTextField tfUser;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    private void insertAction() {
        // Toi da 20 items
        if (serviceDetailsTableModelDialog.getRowCount() == 4) {
            SwingUtils.showInfoDialog("Maximum 5 item in 01 service  !");
            btAdd.setEnabled(false);
        }
        ServiceDetails details = new ServiceDetails();
//        details.setOrdID(-1);
//        details.setBraName("Choose product!");
//        details.setSerContent("New service");        
//        details.setProQty(1);
        details.setProName(ServiceDetails.DEFAULT_PRONAME);
//        details.setSerID(selectedDetails.getSerID());
        serviceDetailsTableModelDialog.insert(details);
        scrollToRow(tbProduct.getRowCount() - 1);
        updateItemsLabel();
        setTrackChanges(true);
    }

    private void deleteAction() {
        btAdd.setEnabled(true);

        // Toi thieu 01 item
        if (serviceDetailsTableModelDialog.getRowCount() == 2) {
            SwingUtils.showInfoDialog("At least 01 item in 01 order !");
            btRemove.setEnabled(false);
        }

        serviceDetailsTableModelDialog.delete(selectedDetails);
        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbProduct.getRowCount() ? tbProduct.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
        updateItemsLabel();
        setTrackChanges(true);
    }

    // Ham goi khi bam nut Save
    private void updateAction() {
        // Check da co product chua va product da chon het chua
        if (checkProductEmpty()) {
            SwingUtils.showInfoDialog("Please choose product name !");
            return;
        }

        if (service.getSerID() == -1) { // Insert mode
            if (serviceDetailsTableModelDialog.insert(service)) {
                SwingUtils.showInfoDialog(SwingUtils.INSERT_SUCCESS);
                dispose(); //Tat dialog sau khi da update db
            } else {
                SwingUtils.showInfoDialog(SwingUtils.INSERT_FAIL);
            }
        } else if (serviceDetailsTableModelDialog.update(service)) { // Update mode
            SwingUtils.showInfoDialog(SwingUtils.UPDATE_SUCCESS);
            dispose(); //Tat dialog sau khi da update db
        } else {
            SwingUtils.showInfoDialog(SwingUtils.UPDATE_FAIL);
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
            selectedDetails = serviceDetailsTableModelDialog.getOrderProductFromIndex(idx);
            serviceDetailsTableModelDialog.setSelectingIndex(idx);
        } else {
            selectedDetails = null;
            serviceDetailsTableModelDialog.setSelectingIndex(-1);
        }
    }

    private void resetAction(boolean mustInfo) {
        // Load lai vung info
        cbStatus.setSelectedItem(serviceStatusComboBoxModel.getStatusFromValue(backup.getSerStatus()));
        cbType.setSelectedItem(serviceTypeComboBoxModel.getServiceTypeNameFromValue(backup.getSerTypeName()));

//       
        // Load lai table product
        serviceDetailsTableModelDialog.load(service.getSerID());
        // Load lai may cai label
        updateItemsLabel();
        updateDiscountLabel();

        if (mustInfo) {
            SwingUtils.showInfoDialog(SwingUtils.DB_RESET);
        }
        setTrackChanges(false);
    }

    private void updateItemsLabel() {
        int sum = 0;
        if (serviceDetailsTableModelDialog.getRowCount() > 0) {
            for (int i = 0; i < serviceDetailsTableModelDialog.getRowCount(); i++) {
                sum += (int) serviceDetailsTableModelDialog.getValueAt(i, COL_PROQTY);
            }
        }
        lbItems.setText(sum > 0 ? String.format("%02d", sum) : "0");
//        updateTotalLabel();
    }

    private void updateDiscountLabel() {
        NumberFormat format = NumberFormat.getPercentInstance();
//        lbDiscount.setText(format.format(order.getCusDiscount()));
//        updateTotalLabel();
    }
//
//    private void updateTotalLabel() {
//        float sum = 0;
//        if (serviceDetailsTableModelDialog.getRowCount() > 0) {
//            for (int i = 0; i < serviceDetailsTableModelDialog.getRowCount(); i++) {
//                sum += (float) serviceDetailsTableModelDialog.getValueAt(i, COL_ODERID) * (int) serviceDetailsTableModelDialog.getValueAt(i, COL_PROQTY);
//            }
//            String dis = lbDiscount.getText().split("%")[0];
//            float discount = Float.parseFloat(dis) / 100;
//            sum = sum * (1 - discount);
//        }
//        lbTotal.setText(String.format("%,.0f Đ", (float) sum));
//    }

    /**
     * Kiem tra da chon product name day du het chua
     *
     * @return true neu da chon product name day du
     */
    private boolean checkProductEmpty() {
        List<ServiceDetails> tmp = new ArrayList();
        serviceDetailsTableModelDialog.getList().stream().filter(op -> op.getProName().equals(OrderProduct.DEFAULT_PRONAME)).forEach(op -> tmp.add(op));
        System.out.println(tmp.size());
        return tmp.size() > 0; //Da chon product day du
    }


    /**
     * Kiem tra duplicate product trong order
     *
     * @param proName
     * @return true if duplicated
     */
    private boolean checkDuplicate(String proName) {
        List<ServiceDetails> tmp = new ArrayList();
        serviceDetailsTableModelDialog.getList().stream().filter(op -> op.getProName().equals(proName)).forEach(op -> tmp.add(op));
        return tmp.size() > 1; //Mang co 2 phan tu tuc la duplicate
    }

    public void setTrackChanges(boolean trackChanges) {
        this.trackChanges = trackChanges;
        btSave.setEnabled(trackChanges);
    }

    private void clearFilter() {
        list.getSelectionModel().clearSelection();
        tfNameFilter.setText(null);
    }

    private void scrollToRow(int row) {
        tbProduct.getSelectionModel().setSelectionInterval(row, row);
        tbProduct.scrollRectToVisible(new Rectangle(tbProduct.getCellRect(row, 0, true)));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cbType) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ServiceType oc = (ServiceType) e.getItem();
                service.setSerTypeID(oc.getSerTypeID());
                service.setSerTypeName(oc.getSerTypeName());
                updateDiscountLabel();
                setTrackChanges(true);
            }
        }

        if (e.getSource() == cbStatus) {
            ServiceStatus os = (ServiceStatus) e.getItem();
            service.setSerStatusID(os.getSttID());
            service.setSerStatus(os.getSttName());
            setTrackChanges(true);
        }
    }
}
