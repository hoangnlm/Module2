/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

import com.sun.glass.events.KeyEvent;
import inbound.controller.*;
import com.toedter.calendar.JDateChooser;
import inbound.model.Inbound;
import inbound.model.InboundDetail;
import inbound.model.InboundDetailDAOImpl;
import inbound.model.InboundDetailTableModel;
import inbound.model.SupplierComboboxModel;
import inbound.model.SupplierComboboxRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import order.controller.OrderBranchListCellRenderer;
import order.controller.OrderBranchListModel;
import order.controller.OrderProductComboBoxModel;
import order.model.OrderBranch;
import org.jdesktop.xswingx.PromptSupport;

import inbound.model.Supplier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.sql.rowset.CachedRowSet;
import javax.swing.AbstractAction;
import utility.SpinnerCellEditor;
import utility.SwingUtils;
import utility.TableCellListener;

/**
 *
 * @author tuan
 */
public class InboundDialog extends javax.swing.JDialog implements ItemListener {

    private Inbound backup;
    private Inbound inbound;

    ProductTableModel productTableModel;
    InboundDetailTableModel inboundDetailTableModel;
    SupplierComboboxModel supplierComboboxModel;
    InDetailSupplierComboBoxRenderer inDetailSupplierComoboxRenderer;
//    SupplierComboboxRenderer supplierComboboxRenderer;
    private OrderProductComboBoxModel orderProductComboBoxModel;
    JDateChooser jdcDate = new JDateChooser();

    private Product selectedProduct;
    private int selectedRowIndex;
    private int selectedRowIndex1;
    private InboundDetail selectedInDetail;
    //row sorter
    private TableRowSorter<ProductTableModel> sorter;

    //inbound list de add moi vao inbound
    List<InboundDetail> listIn = new ArrayList<>();

    // Two mode: insert va update
    private boolean insertMode;
    
    //flag de cho biet co thay doi noi dung gi k
    private boolean trackChanges;

    public InboundDialog(Inbound inbound) {
        super((JFrame) null, true);
        insertMode = inbound == null;
        initComponents();
        setLocationRelativeTo(null);

        setting();
        btDelete.setEnabled(false);
        if (insertMode) {
            setTitle("New Inbound");
            this.inbound = new Inbound();
            this.inbound.setInID(-1);
            this.inbound.setInDate(new Date());
            this.inbound.setSupID(1);
            this.inbound.setSupInvoiceID("");
            this.inbound.setUserID(1);
            backup = this.inbound.clone();
            setTrackChanges(false);
        } else {
            this.inbound = inbound.clone();
            backup = this.inbound.clone();
            cbSupplier.setSelectedItem(supplierComboboxModel.getSupplierFromValue(this.inbound.getSupName()));//set data cho combobox
            jdcDate.setDate(this.inbound.getInDate());//set data cho ngay
            jdcDate.setEnabled(false);//disable ngay,k cho update
            txtInvoice.setText(this.inbound.getSupInvoiceID());
            setTrackChanges(false);
        }
        
        inboundDetailTableModel.load(this.inbound.getInID());//null neu insert mode
        
        //update lai list
       listIn = inboundDetailTableModel.getList();//null neu insert mode
       
       
    }

    

    public void setting() {

        selectedProduct = new Product();
        selectedInDetail = new InboundDetail();
        //set data cho table product
        productTableModel = new ProductTableModel();
        tbProductList.setModel(productTableModel);

        // Set sorter cho table
        sorter = new TableRowSorter<>(productTableModel);
        tbProductList.setRowSorter(sorter);

        //hide column hinh
        TableColumnModel tcm = tbProductList.getColumnModel();
        tcm.removeColumn(tcm.getColumn(8));

        //hide column hinh
        tcm.removeColumn(tcm.getColumn(7));

        // Set data cho combobox product name
        orderProductComboBoxModel = new OrderProductComboBoxModel();

        //set data cho table InDetail
        inboundDetailTableModel = new InboundDetailTableModel();
        tbInDetail.setModel(inboundDetailTableModel);
        tbInDetail.setRowSelectionAllowed(true);

        // Set data cho combobox supplier
        supplierComboboxModel = new SupplierComboboxModel();
        inDetailSupplierComoboxRenderer = new InDetailSupplierComboBoxRenderer();
        cbSupplier.setModel(supplierComboboxModel);
        cbSupplier.setRenderer(inDetailSupplierComoboxRenderer);
        cbSupplier.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                setTrackChanges(true);
            }
        });

        //get current user name
        txtUser.setText(main.model.Login.USER_NAME);

        // Set promt text
        PromptSupport.setPrompt("Input Invoice", txtInvoice);

        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, txtInvoice);

        // ---------- Settup Jdatechooser
        jdcDate.setBounds(0, 0, 120, 35);
        jdcDate.setDateFormatString("MMM dd, yyyy");
        jdcDate.getDateEditor().setEnabled(false);
        pnlDate.add(jdcDate);
        //Lay ngay hien tai
        java.util.Date today = new java.util.Date();
        jdcDate.setDate(today);
        
        //bat su kien sua thong tin table InDetail
        TableCellListener tcl = new TableCellListener(tbInDetail, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                switch (tcl.getColumn()){
                    case 2: setTrackChanges(true);break;//co sua cot cost
                    case 3: setTrackChanges(true);break;//co sua cot quantity
                }
                }
        });
        //bat su kien click vao table product
        tbProductList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchProductDetails();
                
                tbProductList.setSurrendersFocusOnKeystroke(false);
            }
        });

        // Bat su kien select row tren table Indetail
        tbInDetail.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                btDelete.setEnabled(true);
            }
        });

        tbInDetail.setDefaultEditor(Float.class, new FloatEditor(1000000, 30000000));

        tbInDetail.getColumnModel().getColumn(3).setCellEditor(new SpinnerCellEditor(1, 10));

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
                if (list.isSelectionEmpty()) {

                    refreshAction(false);
                    productTableModel.refresh();
                    doFilter();
                    return;
                } else {
                    doFilter();
                }

            }
        });
        
       

    }
    private void resetAction(boolean mustInfo) {
        // Load lai vung supplier
        cbSupplier.setSelectedItem(supplierComboboxModel.getSupplierFromValue(backup.getSupName()));
        

        
        // Load lai table product
        inboundDetailTableModel.load(backup.getInID());
        // Load lai Supplier invoice id
        txtInvoice.setText(backup.getSupInvoiceID());

        if (mustInfo) {
            SwingUtils.showInfoDialog(SwingUtils.DB_RESET);
        }
        setTrackChanges(false);
    }
    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Refresh table
            productTableModel.refresh();

            // Refresh combobox column table
//            branchNameComboBoxModel1.refresh();
            setCursor(null);

        } else {
            // Refresh table
            productTableModel.refresh();

        }

    }
    public void setTrackChanges(boolean trackChanges) {
        this.trackChanges = trackChanges;
        btnSave.setEnabled(trackChanges);
    }
    private void refreshAction2(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Refresh table
            inboundDetailTableModel.refresh();

            // Refresh combobox column table
//            branchNameComboBoxModel1.refresh();
            setCursor(null);

        } else {
            // Refresh table
            inboundDetailTableModel.refresh();

        }

    }

    private void scrollToRow(int row) {
        tbProductList.getSelectionModel().setSelectionInterval(row, row);
        tbProductList.scrollRectToVisible(new Rectangle(tbProductList.getCellRect(row, 0, true)));
    }

    private void doFilter() {
        RowFilter<ProductTableModel, Object> rf = null;

        List<RowFilter<ProductTableModel, Object>> filters = new ArrayList<>();

        // Get the index of all the selected items
        int[] selectedIx = list.getSelectedIndices();

        // Get all the selected items using the indices
        for (int i = 0; i < selectedIx.length; i++) {
            Object sel = list.getModel().getElementAt(selectedIx[i]).getBraName();
            System.err.println(sel);
            filters.add(RowFilter.regexFilter("^" + sel, 1));
            rf = RowFilter.orFilter(filters);
        }

        sorter.setRowFilter(rf);
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
        pnlDate = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cbSupplier = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtInvoice = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbInDetail = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProductList = new javax.swing.JTable();
        btnSave = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btDelete = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        list = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(730, 627));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inbound", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13), new java.awt.Color(255, 153, 0))); // NOI18N

        pnlDate.setEnabled(false);

        javax.swing.GroupLayout pnlDateLayout = new javax.swing.GroupLayout(pnlDate);
        pnlDate.setLayout(pnlDateLayout);
        pnlDateLayout.setHorizontalGroup(
            pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
        );
        pnlDateLayout.setVerticalGroup(
            pnlDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
        );

        jLabel3.setText("Supplier:");

        jLabel4.setText("Suppplier Invoice:");

        txtInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceKeyPressed(evt);
            }
        });

        jLabel5.setText("User:");

        txtUser.setEnabled(false);
        txtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });

        jLabel2.setText("Date:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pnlDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtUser))
                    .addComponent(cbSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inbound Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13), new java.awt.Color(255, 153, 0))); // NOI18N

        tbInDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbInDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbInDetailMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbInDetail);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Product", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13), new java.awt.Color(255, 153, 0))); // NOI18N

        tbProductList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbProductList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProductListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbProductList);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/apply.png"))); // NOI18N
        btnSave.setText("Save all");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Add.png"))); // NOI18N
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Delete.png"))); // NOI18N
        btDelete.setText("Delete");
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });

        list.setBackground(new java.awt.Color(51, 51, 51));
        list.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        jScrollPane3.setViewportView(list);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/refresh_1.png"))); // NOI18N

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/banned.png"))); // NOI18N
        jButton5.setText("Discard");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Refresh.png"))); // NOI18N
        jButton3.setText("Reset Data");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(jButton2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btDelete)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(jButton5)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        InboundDetail inbound = new InboundDetail();
        inbound.setInID(0);
        inbound.setProID(selectedProduct.getProId());
        inbound.setProName(selectedProduct.getProName());

        inbound.setProCost(1000000);
        inbound.setProQty(1);
        inboundDetailTableModel.insert(inbound);
        listIn = inboundDetailTableModel.getList();
        setTrackChanges(true);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (insertMode)//insert mode
        {
            insertAction();
        } else {
            
            updateAction();
        }
        
        
    }//GEN-LAST:event_btnSaveActionPerformed

    private void tbInDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbInDetailMouseClicked

    }//GEN-LAST:event_tbInDetailMouseClicked

    private void tbProductListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProductListMouseClicked

        tbProductList.setRowSelectionAllowed(true);
    }//GEN-LAST:event_tbProductListMouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        cancelAction();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        deleteAction();
    }//GEN-LAST:event_btDeleteActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        resetAction(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtInvoiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceKeyPressed
        int key = evt.getKeyCode();
        if(key==evt.VK_0){
            txtInvoice.setEditable(true);
            txtInvoice.setBackground(Color.red);
        }
        else{
            txtInvoice.setBackground(Color.orange);
        }
        
    }//GEN-LAST:event_txtInvoiceKeyPressed
    public void deleteAction() {

        inboundDetailTableModel.delete(selectedInDetail);
        selectedRowIndex = (selectedRowIndex == tbInDetail.getRowCount() ? tbInDetail.getRowCount() - 1 : selectedRowIndex++);
        // scrollToRow(selectedRowIndex);
        //refresh table
        //refreshAction2(false);
        //inboundDetailTableModel.refresh();
        setTrackChanges(true);
    }

    public void cancelAction() {
        if (trackChanges) {
            if (SwingUtils.showConfirmDialog("Discard change(s) and quit ?") == JOptionPane.YES_OPTION) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    public void insertAction() {
        
        if (txtInvoice.getText().equals("")) {
            SwingUtils.showInfoDialog("You have not input invoice id");
            txtInvoice.requestFocus();
            return;
        }
       
        String temp = txtInvoice.getText();//check dupplicate sup invoice id
        if (new InboundDetailDAOImpl().insert(listIn,temp)) { //insert inbound moi voi gia tri mac dinh

            //update lai nhung gia tri duoc sua moi
            Inbound ib = new Inbound();
            ib.setInDate(jdcDate.getDate());
            System.err.println(jdcDate.getDate());
            if (cbSupplier.getSelectedIndex() == -1) {
                ib.setSupName(supplierComboboxModel.getElementAt(0).getSupName());
            } else {
                ib.setSupName(supplierComboboxModel.getSelectedItem().getSupName());
            }
            ib.setSupInvoiceID(txtInvoice.getText());

            //update lai database
            if (new InboundDetailDAOImpl().update(ib)) {
                SwingUtils.showInfoDialog(SwingUtils.INSERT_SUCCESS);
                //tat dialog
                dispose();
            } else {//neu invoice id bi trung
                System.err.println("nonon");
                //delete inbound moi them vao
                new InboundDetailDAOImpl().delete(ib);
                txtInvoice.requestFocus();
                txtInvoice.selectAll();
            }

        } else {
            SwingUtils.showInfoDialog(SwingUtils.INSERT_FAIL);
            txtInvoice.requestFocus();
            txtInvoice.selectAll();
            return;
        }

    }
    
    

    public void updateAction() {
        
        if (txtInvoice.getText().equals("")) {
            SwingUtils.showInfoDialog("You have not input invoice id");
            txtInvoice.requestFocus();
            return;
        } 
        else if(!txtInvoice.getText().matches("[a-zA-Z]")){
            SwingUtils.showInfoDialog("Invalid input!");
            txtInvoice.requestFocus();
            return;
        }
        else{

            //update lai nhung gia tri moi
            Inbound ib = new Inbound();
            ib.setInID(this.inbound.getInID());
            
            if (cbSupplier.getSelectedIndex() == -1) {
                ib.setSupName(supplierComboboxModel.getElementAt(0).getSupName());
            } else {
                ib.setSupName(supplierComboboxModel.getSelectedItem().getSupName());
            }
            
            ib.setSupInvoiceID(txtInvoice.getText());
            
            
             if (new InboundDetailDAOImpl().update(listIn,ib,inbound)){
                 SwingUtils.showInfoDialog(SwingUtils.UPDATE_SUCCESS);//update thanh cong
                 //tat dialog
                 dispose();
             }
             else{
                 SwingUtils.showInfoDialog(SwingUtils.UPDATE_FAIL);//update that bai
             }
        }
    }

    /**
     * @param args the command line arguments
     */
    private void fetchProductDetails() {

        selectedRowIndex = tbProductList.getSelectedRow();

        selectedProduct.setProId((int) tbProductList.getValueAt(selectedRowIndex, 0));

        selectedProduct.setBraname((String) tbProductList.getValueAt(selectedRowIndex, 1));
        selectedProduct.setProName((String) tbProductList.getValueAt(selectedRowIndex, 2));
        selectedProduct.setProStock((int) tbProductList.getValueAt(selectedRowIndex, 3));
        selectedProduct.setProPrice((float) tbProductList.getValueAt(selectedRowIndex, 4));
        selectedProduct.setProDesc((String) tbProductList.getValueAt(selectedRowIndex, 5));
        selectedProduct.setProEnabled((boolean) tbProductList.getValueAt(selectedRowIndex, 6));
        //cot sale off k cho update

        //cot image
//        //cot braid bi an di
//        selectedProduct.setBraId((int) tbProductList.getModel().getValueAt(selectedRowIndex, 9));
    }

    private void fetchAction() {

//        selectedRowIndex = tbInDetail.getSelectedRow();
//       selectedInDetail.setProID((int) tbInDetail.getValueAt(selectedRowIndex, 0));
//       selectedInDetail.setProName((String) tbInDetail.getValueAt(selectedRowIndex, 1));
//       selectedInDetail.setProCost((float) tbInDetail.getValueAt(selectedRowIndex, 2));
//       selectedInDetail.setProQty((int) tbInDetail.getValueAt(selectedRowIndex, 3));
//       System.err.println("indetail selected "+selectedInDetail);
        selectedRowIndex1 = tbInDetail.getSelectedRow();
        System.err.println(selectedRowIndex1);
        if (selectedRowIndex1 >= 0) {
            int idx = tbInDetail.convertRowIndexToModel(selectedRowIndex1);
            selectedInDetail = inboundDetailTableModel.getInboundDetailFromIndex(idx);
            inboundDetailTableModel.setSelectingIndex(idx);
        } else {
            selectedProduct = null;
            inboundDetailTableModel.setSelectingIndex(-1);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<Supplier> cbSupplier;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<OrderBranch> list;
    private javax.swing.JPanel pnlDate;
    private javax.swing.JTable tbInDetail;
    private javax.swing.JTable tbProductList;
    private javax.swing.JTextField txtInvoice;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == cbSupplier) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setTrackChanges(true);
            }
    }
    }
}
