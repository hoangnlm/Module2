/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.controller;

import branch.model.Branch;
import database.DBProvider;
import inbound.controller.FloatEditor;
import inbound.model.CurrencyCellRenderer;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.xswingx.PromptSupport;
import product.dao.BranchNameComboBoxModel;
import product.dao.BranchNameComboBoxRender;
import product.dao.MyQuery;
import product.dao.ProductDAOImpl;
import product.model.Product;
import product.model.ProductTableModel;
import utility.IntegerCellEditor;
import utility.StringCellEditor;
import utility.SwingUtils;
import utility.TableCellListener;

/**
 *
 * @author tuan
 */
public class ProductPanel extends javax.swing.JPanel {
    //chon hinh
   String s;
    
    private ProductTableModel productTableModel;
    private BranchNameComboBoxModel branchNameComboBoxModel;
    
    private final BranchNameComboBoxRender branchNameComboBoxRenderer;
    private final TableRowSorter<ProductTableModel> sorter;
   
   // Branch dang duoc chon trong table
    private Product selectedProduct;
    private int selectedRowIndex;
    private Branch filterBranch;
    
    
    /*Product table*/
    private static int COL_ID = 0;
    private static int COL_BraName = 1;
    private static int COL_ProName = 2;
    private static int COL_ProStock = 3;
    private static int COL_ProPrice = 4;
    private static int COL_ProDescr = 5;
    private static int COL_ProEnable = 6;
    private static int COL_SaleOff = 7;
    private static int COL_Img = 8;
    
    /* gia tri cu cua product name*/
    Object oldValue = null;
    
    public ProductPanel() {
        initComponents();
        PromptSupport.setPrompt("Find ID", tfIdFilter);
        PromptSupport.setPrompt("Find Name", tfNameFilter);
        PromptSupport.setPrompt("Find Price", tfPriceFilter);
        PromptSupport.setPrompt("Stock", tfStockFilter);
        
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfIdFilter);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfNameFilter);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfPriceFilter);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfStockFilter);
        //disable nut browse
        jBrowse.setEnabled(false);
        
        selectedProduct = new Product();
        
        // Set data cho combobox level filter
        branchNameComboBoxModel = new BranchNameComboBoxModel();
        filterBranch = new Branch(0,"All",true,"default",0);
        branchNameComboBoxModel.addElement(filterBranch);
        
        branchNameComboBoxRenderer = new BranchNameComboBoxRender();
        cbBranchName.setModel(branchNameComboBoxModel);
        cbBranchName.setRenderer(branchNameComboBoxRenderer);
        
        
        //set data cho table
        productTableModel = new ProductTableModel();
        tbProductList.setModel(productTableModel);
        tbProductList.setRowSelectionAllowed(true);
        
        
        // Set sorter cho table
        sorter = new TableRowSorter<>(productTableModel);
        tbProductList.setRowSorter(sorter);
        
        //set default option cho cb
        cbBranchName.setSelectedIndex(cbBranchName.getItemCount() - 1);
        formatTable();
        tbProductList.setRowHeight(120);
        
      
        
        // Bat su kien select row tren table
        tbProductList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchProductDetails();
                jButton1.setEnabled(true);
                tbProductList.setSurrendersFocusOnKeystroke(false);
            } 
        });
        

        TableCellListener tcl = new TableCellListener(tbProductList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
//                System.out.println("Row   : " + tcl.getRow());
//                System.out.println("Column: " + tcl.getColumn());
//                System.out.println("Old   : " + tcl.getOldValue());
//                System.out.println("New   : " + tcl.getNewValue());

                switch (tcl.getColumn()) {
                    case 0:
                        selectedProduct.setProName((String) tcl.getNewValue());
                        oldValue = tcl.getNewValue();//de tra ve gia tri cu neu sai
                        break;
                    case 1:
                        selectedProduct.setBraname((String) tcl.getNewValue());
                        break;
                    case 2:
                        selectedProduct.setProName((String) tcl.getNewValue());
                        break;
                    case 4:
                        selectedProduct.setProPrice((float) tcl.getNewValue());
                        break;
                    case 3:
                        selectedProduct.setProStock((int) tcl.getNewValue());
                        break;
                    case 5:
                        selectedProduct.setProDesc((String)tcl.getNewValue());
                        break;
                    case 7:
                        selectedProduct.setSaleofid((int)tcl.getNewValue());
                        break;
                    case 6:
                        selectedProduct.setProEnabled((boolean)tcl.getNewValue());
                        break;
                }

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
        tfPriceFilter.getDocument().addDocumentListener(
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
        tfStockFilter.getDocument().addDocumentListener(
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
        
        
       
        jButton1.setEnabled(false);
        

    }
    
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
    
    private void updateAction() {
        if (productTableModel.update(selectedProduct)) {
            formatTable();
            tbProductList.setRowHeight(120);
            JOptionPane.showMessageDialog(this, "Update successfully");
        } else {
            //refresh
            refreshAction(true);
            productTableModel.refresh();
            formatTable();
            
        }
        
    }
    
    private void refreshAction(){
        
        //refresh filter
        tfIdFilter.setText("");
        tfNameFilter.setText("");
        tfPriceFilter.setText("");
        tfStockFilter.setText("");
        cbBranchName.setSelectedIndex(cbBranchName.getItemCount() - 1);
        cbStatusFilter.setSelectedIndex(0);
        tbProductList.getSelectionModel().clearSelection();
        
        
        
        
    }
    public void formatTable() {
        //alignment component
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        //id
        tbProductList.getColumnModel().getColumn(COL_ID).setMinWidth(30);
        tbProductList.getColumnModel().getColumn(COL_ID).setMaxWidth(50);
        tbProductList.getColumnModel().getColumn(COL_ID).setCellRenderer(centerRenderer);
        //branch
        tbProductList.getColumnModel().getColumn(COL_BraName).setMinWidth(80);
        tbProductList.getColumnModel().getColumn(COL_BraName).setMaxWidth(80);
        tbProductList.getColumnModel().getColumn(COL_BraName).setCellRenderer(centerRenderer);
        tbProductList.getColumnModel().getColumn(COL_BraName).setCellEditor(new product.dao.ComboBoxCellEditor(new BranchNameComboBoxModel()));
        
        //name
        tbProductList.getColumnModel().getColumn(COL_ProName).setMinWidth(80);
        tbProductList.getColumnModel().getColumn(COL_ProName).setCellRenderer(centerRenderer);
        tbProductList.getColumnModel().getColumn(COL_ProName).setCellEditor(new StringCellEditor(1, 70,"[a-zA-Z ]+"));
        //stock
        tbProductList.getColumnModel().getColumn(COL_ProStock).setMinWidth(35);
        tbProductList.getColumnModel().getColumn(COL_ProStock).setMaxWidth(35);
        tbProductList.getColumnModel().getColumn(COL_ProStock).setCellRenderer(centerRenderer);

        //price
        tbProductList.getColumnModel().getColumn(COL_ProPrice).setMinWidth(30);
        tbProductList.getColumnModel().getColumn(COL_ProPrice).setCellRenderer(new CurrencyCellRenderer());

        //desc
        tbProductList.getColumnModel().getColumn(COL_ProDescr).setMinWidth(150);
        tbProductList.getColumnModel().getColumn(COL_ProDescr).setCellRenderer(centerRenderer);
        tbProductList.getColumnModel().getColumn(COL_ProDescr).setCellEditor(new StringCellEditor(1, 100,"[a-zA-Z ]+"));
        
        //enable
        tbProductList.getColumnModel().getColumn(COL_ProEnable).setMinWidth(40);
        tbProductList.getColumnModel().getColumn(COL_ProEnable).setMaxWidth(40);
        
        
        //saleoff
        tbProductList.getColumnModel().getColumn(COL_SaleOff).setMinWidth(100);
        tbProductList.getColumnModel().getColumn(COL_SaleOff).setMaxWidth(100);
        tbProductList.getColumnModel().getColumn(COL_SaleOff).setCellRenderer(centerRenderer);
        
        //image
        tbProductList.getColumnModel().getColumn(COL_Img).setMinWidth(100);
//        tbProductList.getColumnModel().getColumn(COL_Img).setMaxWidth(100);
        

        
        tbProductList.setDefaultEditor(Float.class,new FloatEditor(1000000,100000000));
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton10 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jBrowse = new javax.swing.JButton();
        jInsert = new javax.swing.JButton();
        jBranch = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tfNameFilter = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        tfStockFilter = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cbStatusFilter = new javax.swing.JComboBox<>();
        tfPriceFilter = new javax.swing.JTextField();
        tfIdFilter = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cbBranchName = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jrefreshFilter = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbProductList = new javax.swing.JTable();

        jTextField2.setText("jTextField2");

        jLabel8.setText("jLabel8");

        jButton10.setText("jButton10");

        setPreferredSize(new java.awt.Dimension(810, 680));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Update", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

        jLabel9.setText("Image");

        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jBrowse.setText("Browse");
        jBrowse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBrowseMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jInsert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Add.png"))); // NOI18N
        jInsert.setText("Insert");
        jInsert.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jInsertMousePressed(evt);
            }
        });
        jInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jInsertActionPerformed(evt);
            }
        });

        jBranch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/folder-open.png"))); // NOI18N
        jBranch.setText("Branch");
        jBranch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBranchMouseClicked(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

        jLabel3.setText("ID:");

        jLabel12.setText("Name:");

        jLabel13.setText("Stock:");

        jLabel14.setText("Price:");

        jLabel15.setText("Status");

        cbStatusFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Enabled", "Disabled" }));
        cbStatusFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStatusFilterItemStateChanged(evt);
            }
        });

        jLabel11.setText("Branch:");

        cbBranchName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbBranchNameItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfStockFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(tfIdFilter))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfNameFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(tfPriceFilter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbBranchName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbStatusFilter, 0, 145, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel12)
                    .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(cbStatusFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(tfStockFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(tfPriceFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cbBranchName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Delete.png"))); // NOI18N
        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Product.png"))); // NOI18N
        jLabel1.setText("<html><u><i><font color='red'>P</font>roduct <font color='red'>M</font>anagement</i></u></html>");

        jrefreshFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Refresh.png"))); // NOI18N
        jrefreshFilter.setText("Refresh");
        jrefreshFilter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrefreshFilterMouseClicked(evt);
            }
        });

        tbProductList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbProductList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProductListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbProductList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jInsert)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jrefreshFilter)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jBranch))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 789, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBranch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jrefreshFilter)
                            .addComponent(jButton1)
                            .addComponent(jInsert))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbBranchNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbBranchNameItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbBranchNameItemStateChanged

    private void cbStatusFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStatusFilterItemStateChanged
    doFilter();
    }//GEN-LAST:event_cbStatusFilterItemStateChanged

    private void jrefreshFilterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrefreshFilterMouseClicked
        refreshAction();
        
        refreshAction(false);
        formatTable();
        scrollToRow(tbProductList.getRowCount() - 1);
    }//GEN-LAST:event_jrefreshFilterMouseClicked

    private void jBrowseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBrowseMouseClicked

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.name")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg", "gif", "png");
        fileChooser.addChoosableFileFilter(filter);
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            ImageIcon MyImage = new ImageIcon(path);
            Image img = MyImage.getImage();
            Image newImage = img.getScaledInstance(jLabel10.getWidth(), jLabel10.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon image = new ImageIcon(newImage);
            jLabel10.setIcon(image);
            s = path;
            
            
            try {
                InputStream is = new FileInputStream(new File(s));
                new ProductDAOImpl().updateImage(selectedProduct,is);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ProductPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            refreshAction(true);
            return;
        } else if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("No data");
            return;
        }

    }//GEN-LAST:event_jBrowseMouseClicked

    private void jInsertMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jInsertMousePressed
       
       
    }//GEN-LAST:event_jInsertMousePressed

    private void jInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jInsertActionPerformed
        insertAction();
        refreshAction(true);
        productTableModel.refresh();
        formatTable();
     
        // Select row vua insert vao
        selectedRowIndex = 0;
        scrollToRow(selectedRowIndex);
        tbProductList.editCellAt(tbProductList.getSelectedRow(), 2);
        tbProductList.getEditorComponent().requestFocus();
    }//GEN-LAST:event_jInsertActionPerformed

    private void jBranchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBranchMouseClicked
        new branch.controller.BranchDialog().setVisible(true);
        refreshAction(true);
    }//GEN-LAST:event_jBranchMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        deleteAction();
        refreshAction(true);
        productTableModel.refresh();
        formatTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tbProductListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProductListMouseClicked

        jBrowse.setEnabled(true);//enable khi bam chon vao table

        ImageIcon image1;
        image1 = (ImageIcon) tbProductList.getValueAt(selectedRowIndex, 8);

        Image image2 = image1.getImage().getScaledInstance(jLabel10.getWidth(),jLabel10.getHeight(), Image.SCALE_SMOOTH);

        ImageIcon image3 = new ImageIcon(image2);

        jLabel10.setIcon(image3);
    }//GEN-LAST:event_tbProductListMouseClicked

   private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Refresh table
            productTableModel.refresh();

            // Refresh combobox filter
            branchNameComboBoxModel.refresh();
            branchNameComboBoxModel.addElement(filterBranch);

            // Refresh combobox column table
//            branchNameComboBoxModel1.refresh();
            setCursor(null);
            
        } else {
            // Refresh table
            productTableModel.refresh();
        }
        scrollToRow(selectedRowIndex);
    }
   
   
   private void scrollToRow(int row) {
        tbProductList.getSelectionModel().setSelectionInterval(row, row);
        tbProductList.scrollRectToVisible(new Rectangle(tbProductList.getCellRect(row, 0, true)));
    }
   private void deleteAction() {
        int ans = SwingUtils.showConfirmDialog("Are you sure to delete?");
        if(ans==JOptionPane.YES_OPTION){
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = productTableModel.delete(selectedProduct);
        setCursor(null);
        if(result)
            SwingUtils.showInfoDialog(SwingUtils.DELETE_SUCCESS);
        

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbProductList.getRowCount() ? tbProductList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
        
        }
    }
    private void insertAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = productTableModel.insert(new Product());
        setCursor(null);
        SwingUtils.showInfoDialog(result ? SwingUtils.INSERT_SUCCESS : SwingUtils.INSERT_FAIL);
        
    }
private void doFilter() {
        RowFilter<ProductTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<ProductTableModel, Object>> filters = new ArrayList<>();
            
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            // Neu co chon cus level thi moi filter level
            if (cbBranchName.getSelectedIndex() != cbBranchName.getItemCount() - 1) {
                filters.add(RowFilter.regexFilter("^" + ((Branch) cbBranchName.getSelectedItem()).getBraName(), 1));
            }
            filters.add(RowFilter.regexFilter("^" + tfNameFilter.getText(), 2));

            filters.add(RowFilter.regexFilter("^" + tfStockFilter.getText(), 3));
            filters.add(RowFilter.regexFilter("^" + tfPriceFilter.getText(), 4));
            
            
            // Neu status khac "All" thi moi filter
            String statusFilter = cbStatusFilter.getSelectedItem().toString();
            if (!statusFilter.equals("All")) {
                filters.add(RowFilter.regexFilter(
                        statusFilter.equals("Enabled") ? "t" : "f", 6));
            }
            
            
            rf = RowFilter.andFilter(filters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    //de refresh table tu panel khac
    public ProductTableModel getProductTableModel() {
        return productTableModel;
    }

    public void setProductTableModel(ProductTableModel productTableModel) {
        this.productTableModel = productTableModel;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<Branch> cbBranchName;
    private javax.swing.JComboBox<String> cbStatusFilter;
    private javax.swing.JButton jBranch;
    private javax.swing.JButton jBrowse;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jInsert;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton jrefreshFilter;
    private javax.swing.JTable tbProductList;
    private javax.swing.JTextField tfIdFilter;
    private javax.swing.JTextField tfNameFilter;
    private javax.swing.JTextField tfPriceFilter;
    private javax.swing.JTextField tfStockFilter;
    // End of variables declaration//GEN-END:variables
}
