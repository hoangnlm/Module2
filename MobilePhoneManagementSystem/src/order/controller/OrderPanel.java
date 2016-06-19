/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package order.controller;

import com.toedter.calendar.JDateChooser;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;
import order.model.Order;
import order.model.OrderStatus;
import salesoff.controller.SalesOffDialog;
import utility.CurrencyCellRenderer;
import utility.PercentCellRenderer;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class OrderPanel extends javax.swing.JPanel {

    private JDateChooser dcFilter;

    // Khai bao 2 cai table model
    private OrderTableModel orderTableModel;
    private OrderProductTableModel orderProductTableModel;
    private OrderStatusComboBoxModel orderStatusComboBoxModel;
    private OrderStatusComboBoxRenderer orderStatusComboBoxRenderer;
    private TableRowSorter<OrderTableModel> sorter;

    // Order dang duoc chon trong table order list
    private Order selectedOrder;
    private int selectedRowIndex;
    private OrderStatus filterStatus;

    private static final int COL_ORDID = 0;
    private static final int COL_USERNAME = 1;
    private static final int COL_CUSNAME = 2;
    private static final int COL_ORDDATE = 3;
    private static final int COL_DISCOUNT = 4;
    private static final int COL_STATUS = 5;

    private static final int COL_PROID = 0;
    private static final int COL_PRONAME = 1;
    private static final int COL_PROQTY = 2;
    private static final int COL_PROPRICE1 = 3;
    private static final int COL_SALEAMOUNT = 4;
    private static final int COL_PROPRICE2 = 5;

//<editor-fold defaultstate="collapsed" desc="constructor">
    public OrderPanel() {
        initComponents();

        // Set date picker len giao dien
        dcFilter = new JDateChooser();
        dcFilter.setBounds(0, 0, 130, 20);
        dcFilter.setDateFormatString("dd/MM/yyyy");
        dcFilter.setDate(null);
        pnDateChooser.add(dcFilter);

        //Disable button khi moi khoi dong len
        setButtonEnabled(false);

        // Selecting order in the table
        selectedOrder = new Order();

        // Set data cho combobox level filter
        orderStatusComboBoxModel = new OrderStatusComboBoxModel();
        filterStatus = new OrderStatus(0, "All", "Order");
        orderStatusComboBoxModel.addElement(filterStatus);
        orderStatusComboBoxRenderer = new OrderStatusComboBoxRenderer();
        cbStatusFilter.setModel(orderStatusComboBoxModel);
        cbStatusFilter.setRenderer(orderStatusComboBoxRenderer);

        // Set data cho 2 table
        orderTableModel = new OrderTableModel();
        orderProductTableModel = new OrderProductTableModel();
        tbOrderList.setModel(orderTableModel);
        tbProductList.setModel(orderProductTableModel);

        // Set sorter cho table
        sorter = new TableRowSorter<>(orderTableModel);
        tbOrderList.setRowSorter(sorter);

        // Select mac dinh cho level filter
        cbStatusFilter.setSelectedIndex(cbStatusFilter.getItemCount() - 1);

        // Set auto define column from model to false to stop create column again
        tbOrderList.setAutoCreateColumnsFromModel(false);
        tbProductList.setAutoCreateColumnsFromModel(false);

        // Set height cho table header
        tbOrderList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        tbProductList.getTableHeader().setPreferredSize(new Dimension(300, 30));

        // Col order ID
        tbOrderList.getColumnModel().getColumn(COL_ORDID).setMinWidth(40);
        tbOrderList.getColumnModel().getColumn(COL_ORDID).setMaxWidth(40);

        // Col user name
        tbOrderList.getColumnModel().getColumn(COL_USERNAME).setMinWidth(120);
        tbOrderList.getColumnModel().getColumn(COL_USERNAME).setMaxWidth(120);

        // Col cus name
        tbOrderList.getColumnModel().getColumn(COL_CUSNAME).setMinWidth(150);

        // Col order date
        tbOrderList.getColumnModel().getColumn(COL_ORDDATE).setMinWidth(120);
        tbOrderList.getColumnModel().getColumn(COL_ORDDATE).setMaxWidth(120);

        // Col discount
        tbOrderList.getColumnModel().getColumn(COL_DISCOUNT).setMinWidth(120);
        tbOrderList.getColumnModel().getColumn(COL_DISCOUNT).setMaxWidth(120);

        // Col status
        tbOrderList.getColumnModel().getColumn(COL_STATUS).setMinWidth(120);

        // Col pro ID
        tbProductList.getColumnModel().getColumn(COL_PROID).setMinWidth(40);
        tbProductList.getColumnModel().getColumn(COL_PROID).setMaxWidth(40);

        // Col pro name
        tbProductList.getColumnModel().getColumn(COL_PRONAME).setMinWidth(150);

        // Col quantity
        tbProductList.getColumnModel().getColumn(COL_PROQTY).setMinWidth(120);
        tbProductList.getColumnModel().getColumn(COL_PROQTY).setMaxWidth(120);

        // Col price 1
        tbProductList.getColumnModel().getColumn(COL_PROPRICE1).setMinWidth(125);
        tbProductList.getColumnModel().getColumn(COL_PROPRICE1).setCellRenderer(new CurrencyCellRenderer());

        // Col salesoff
        tbProductList.getColumnModel().getColumn(COL_SALEAMOUNT).setMinWidth(125);
        tbProductList.getColumnModel().getColumn(COL_SALEAMOUNT).setCellRenderer(new PercentCellRenderer());

        // Col price 2
        tbProductList.getColumnModel().getColumn(COL_PROPRICE2).setMinWidth(125);
        tbProductList.getColumnModel().getColumn(COL_PROPRICE2).setCellRenderer(new CurrencyCellRenderer());
        
        // Bat su kien select row tren table sales off
        tbOrderList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                setButtonEnabled(true);
            } else {
                setButtonEnabled(false);
            }
        });

        //<editor-fold defaultstate="collapsed" desc="Bat su kien cho vung filter">
        tfIdFilter.getDocument().addDocumentListener(new DocumentListener() {
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
        tfUserFilter.getDocument().addDocumentListener(new DocumentListener() {
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
        tfCusFilter.getDocument().addDocumentListener(
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
        dcFilter.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                doFilter();
            }
        });
        cbStatusFilter.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                doFilter();
            }
        });
//</editor-fold>

    }
//</editor-fold>

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
        tfUserFilter = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfCusFilter = new javax.swing.JTextField();
        lbOrderDate = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbStatusFilter = new javax.swing.JComboBox<>();
        pnDateChooser = new javax.swing.JPanel();
        btClear = new javax.swing.JButton();
        lbOrderDate1 = new javax.swing.JLabel();
        cbValueFilter = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbOrderList = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProductList = new javax.swing.JTable();
        btRemove = new javax.swing.JButton();
        btUpdate = new javax.swing.JButton();
        btAdd = new javax.swing.JButton();
        btRefresh = new javax.swing.JButton();
        pnTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btSalesOff = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(790, 640));

        pnFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filter"));
        pnFilter.setPreferredSize(new java.awt.Dimension(770, 66));

        jLabel2.setText("ID:");

        jLabel3.setText("Sell. User:");

        jLabel4.setText("Cus. Name:");
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 16));

        tfCusFilter.setPreferredSize(new java.awt.Dimension(160, 26));

        lbOrderDate.setText("Order Date:");

        jLabel6.setText("Status:");
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 16));

        cbStatusFilter.setPreferredSize(new java.awt.Dimension(160, 27));

        pnDateChooser.setPreferredSize(new java.awt.Dimension(130, 20));

        javax.swing.GroupLayout pnDateChooserLayout = new javax.swing.GroupLayout(pnDateChooser);
        pnDateChooser.setLayout(pnDateChooserLayout);
        pnDateChooserLayout.setHorizontalGroup(
            pnDateChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        pnDateChooserLayout.setVerticalGroup(
            pnDateChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        btClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete2.png"))); // NOI18N
        btClear.setBorderPainted(false);
        btClear.setContentAreaFilled(false);
        btClear.setFocusPainted(false);
        btClear.setPreferredSize(new java.awt.Dimension(20, 28));
        btClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearActionPerformed(evt);
            }
        });

        lbOrderDate1.setText("Order Value:");

        cbValueFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "~1.000.000", "1.000.000-5.000.000", "5.000.000-10.000.000", "10.000.000~", "All" }));
        cbValueFilter.setSelectedItem("All");

        javax.swing.GroupLayout pnFilterLayout = new javax.swing.GroupLayout(pnFilter);
        pnFilter.setLayout(pnFilterLayout);
        pnFilterLayout.setHorizontalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbOrderDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbOrderDate1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbValueFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfUserFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfCusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        pnFilterLayout.setVerticalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(tfUserFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfCusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbOrderDate)
                            .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbOrderDate1)
                                .addComponent(cbValueFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(btClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Order List"));

        tbOrderList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "User Name", "Cus. Name", "Order Date", "Discount", "Value", "Status"
            }
        ));
        tbOrderList.setFillsViewportHeight(true);
        tbOrderList.setRowHeight(25);
        tbOrderList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbOrderList);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Order Details"));

        tbProductList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product Name", "Quantity", "Sold Price"
            }
        ));
        tbProductList.setFillsViewportHeight(true);
        tbProductList.setRowHeight(25);
        tbProductList.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbProductList);

        btRemove.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Delete.png"))); // NOI18N
        btRemove.setText("Remove");
        btRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRemoveActionPerformed(evt);
            }
        });

        btUpdate.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Save.png"))); // NOI18N
        btUpdate.setText("Update...");
        btUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btUpdateActionPerformed(evt);
            }
        });

        btAdd.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/Add.png"))); // NOI18N
        btAdd.setText("Add New...");
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
        pnTitle.setPreferredSize(new java.awt.Dimension(790, 65));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Order.png"))); // NOI18N
        jLabel1.setText("<html><u><i><font color='red'>O</font>rder <font color='red'>M</font>anagement</i></u></html>");

        btSalesOff.setFont(new java.awt.Font("Lucida Grande", 3, 14)); // NOI18N
        btSalesOff.setForeground(new java.awt.Color(255, 153, 0));
        btSalesOff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/order/SalesOff2.png"))); // NOI18N
        btSalesOff.setText("<html><u>SalesOff...</u></html>");
        btSalesOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalesOffActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnTitleLayout = new javax.swing.GroupLayout(pnTitle);
        pnTitle.setLayout(pnTitleLayout);
        pnTitleLayout.setHorizontalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btSalesOff, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnTitleLayout.setVerticalGroup(
            pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addComponent(btSalesOff, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 790, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
//<editor-fold defaultstate="collapsed" desc="Bat su kien">
    private void btAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddActionPerformed
        new OrderDialog(null).setVisible(true);
    }//GEN-LAST:event_btAddActionPerformed

    private void btSalesOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalesOffActionPerformed
        new SalesOffDialog().setVisible(true);
    }//GEN-LAST:event_btSalesOffActionPerformed

    private void btUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btUpdateActionPerformed
        new OrderDialog(selectedOrder).setVisible(true);
    }//GEN-LAST:event_btUpdateActionPerformed

    private void btRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRemoveActionPerformed
        deleteAction();
    }//GEN-LAST:event_btRemoveActionPerformed

    private void btRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRefreshActionPerformed
        refreshAction(true);
    }//GEN-LAST:event_btRefreshActionPerformed

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        clearFilter();
    }//GEN-LAST:event_btClearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdd;
    private javax.swing.JButton btClear;
    private javax.swing.JButton btRefresh;
    private javax.swing.JButton btRemove;
    private javax.swing.JButton btSalesOff;
    private javax.swing.JButton btUpdate;
    private javax.swing.JComboBox<order.model.OrderStatus> cbStatusFilter;
    private javax.swing.JComboBox<String> cbValueFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbOrderDate;
    private javax.swing.JLabel lbOrderDate1;
    private javax.swing.JPanel pnDateChooser;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JPanel pnTitle;
    private javax.swing.JTable tbOrderList;
    private javax.swing.JTable tbProductList;
    private javax.swing.JTextField tfCusFilter;
    private javax.swing.JTextField tfIdFilter;
    private javax.swing.JTextField tfUserFilter;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="doFilter">
    private void doFilter() {
        RowFilter<OrderTableModel, Object> rf = null;
        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<OrderTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            filters.add(RowFilter.regexFilter("^" + tfUserFilter.getText(), 1));
            filters.add(RowFilter.regexFilter("^" + tfCusFilter.getText(), 1));

            // Chi filter date khi date khac null
            if (dcFilter.getDate() != null) {
                // dcFilter khac voi date cua table o GIO, PHUT, GIAY nen phai dung Calendar de so sanh chi nam, thang, ngay.... oh vai~.
                // Magic here....
                RowFilter<OrderTableModel, Object> dateFilter = new RowFilter<OrderTableModel, Object>() {
                    @Override
                    public boolean include(RowFilter.Entry<? extends OrderTableModel, ? extends Object> entry) {
                        OrderTableModel model = entry.getModel();
                        Order o = model.getOrderAtIndex((Integer) entry.getIdentifier());

                        Calendar origin = Calendar.getInstance();
                        origin.setTime(o.getOrdDate());

                        Calendar filter = Calendar.getInstance();
                        filter.setTime(dcFilter.getDate());

                        if (origin.get(Calendar.YEAR) == filter.get(Calendar.YEAR) && origin.get(Calendar.MONTH) == filter.get(Calendar.MONTH) && origin.get(Calendar.DATE) == filter.get(Calendar.DATE)) {
                            return true;
                        }

                        return false;
                    }
                };

                filters.add(dateFilter);
            }

            // Chi filter status khi status khac "All"
            String stt = ((OrderStatus) cbStatusFilter.getSelectedItem()).getSttName();
            if (!stt.equals("All")) {
                filters.add(RowFilter.regexFilter("^" + stt, 5));
            }

            rf = RowFilter.andFilter(filters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
//</editor-fold>

    private void clearFilter(){
        tfIdFilter.setText(null);
        dcFilter.setDate(null);
        tfUserFilter.setText(null);
        cbValueFilter.setSelectedIndex(cbValueFilter.getItemCount()-1);
        tfCusFilter.setText(null);
        cbStatusFilter.setSelectedIndex(cbStatusFilter.getItemCount()-1);
    }
    
    //<editor-fold defaultstate="collapsed" desc="xu ly cho table order">
    private void fetchAction() {
        selectedRowIndex = tbOrderList.convertRowIndexToModel(tbOrderList.getSelectedRow());
        selectedOrder = orderTableModel.getOrderAtIndex(selectedRowIndex);
        // Reload table product list voi Order moi chon
        orderProductTableModel.load(selectedOrder.getOrdID());
    }

    private void deleteAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = orderTableModel.delete(selectedOrder);
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.DELETE_SUCCESS : SwingUtils.DELETE_FAIL);

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbOrderList.getRowCount() ? tbOrderList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
    }
//</editor-fold>

    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            orderTableModel.refresh();
            setCursor(null);
            SwingUtils.showInfoDialog(SwingUtils.DB_REFRESH);
        } else {
            orderTableModel.refresh();
        }
        scrollToRow(selectedRowIndex);
    }

    private void scrollToRow(int row) {
        tbOrderList.getSelectionModel().setSelectionInterval(row, row);
        tbOrderList.scrollRectToVisible(new Rectangle(tbOrderList.getCellRect(row, 0, true)));
    }

    private void setButtonEnabled(boolean enabled, JButton... exclude) {
        btUpdate.setEnabled(enabled);
        btRemove.setEnabled(enabled);

        // Ngoai tru may button nay luon luon enable
        if (exclude.length != 0) {
            Arrays.stream(exclude).forEach(b -> b.setEnabled(true));
        }
    }
}
