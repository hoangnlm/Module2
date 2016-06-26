/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.controller;

import com.toedter.calendar.JDateChooser;
import inbound.model.CurrencyCellRenderer;
import inbound.model.Inbound;
import inbound.model.InboundProductTableModel;
import inbound.model.SupplierComboboxModel;
import inbound.model.SupplierComboboxRenderer;
import inbound.model.User;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import org.jdesktop.xswingx.PromptSupport;
import inbound.model.UserComboboxModel;
import inbound.model.UserNameComboboxRenderer;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import inbound.model.Supplier;
import product.controller.ProductPanel;
import utility.SwingUtils;
import utility.TableCellListener;
/**
 *
 * @author tuan
 */
public class InboundPanel extends javax.swing.JPanel {
    private JDateChooser dcFilter;

    // Khai bao 2 cai table model
    private InboundTableModel inboundTableModel;
    private InboundProductTableModel inboundProductTableModel;
    
    //khai bao user combobbox
    private UserComboboxModel userComboBoxModel;
    private UserComboboxModel userComboBoxModel1;
    private UserNameComboboxRenderer userNameComboBoxRenderer;
    
    
    //khai bao supplier combobox
    private SupplierComboboxModel supplierComboBoxModel;
    private SupplierComboboxModel supplierComboBoxModel1;
    private SupplierComboboxRenderer supplierComboBoxRenderer;
    
    
    //row sorter
    private TableRowSorter<InboundTableModel> sorter;
    
    
    // Order dang duoc chon trong table order list
    private Inbound selectedInbound;
    private int selectedRowIndex;
    private User filterUser;
    private Supplier filterSupplier;
    
    
    //format ngay
    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
    JDateChooser jdcFromDate = new JDateChooser();
    JDateChooser jdcToDate = new JDateChooser();
    
    /*Inbound Table*/
    private static final int COL_InID = 0;
    private static final int COL_InDate = 1;
    private static final int COL_SupName = 2;
    private static final int COL_SupInID = 3;
    private static final int COL_UserName = 4;
    /*Product Table*/
    private static final int COL_PROID = 0;
    private static final int COL_PRONAME = 1;
    private static final int COL_PROQTY = 2;
    private static final int COL_PROPRICE = 3;
    public InboundPanel(){
        initComponents();
        // Selecting order in the table
        selectedInbound = new Inbound();
        
        setting();
        // Set data cho combobox user filter
        userComboBoxModel = new UserComboboxModel();
        filterUser = new User(0, "All",true);
        userComboBoxModel.addElement(filterUser);

        // Set data cho column user combobox
        userComboBoxModel1 = new UserComboboxModel();

        // Set data cho combobox user update
        userNameComboBoxRenderer = new UserNameComboboxRenderer();
        cbUserFilter.setModel(userComboBoxModel);
        cbUserFilter.setRenderer(userNameComboBoxRenderer);
        
//         Set data cho combobox supplier filter
        supplierComboBoxModel = new SupplierComboboxModel();
        filterSupplier = new Supplier(0, "All","",true);
        supplierComboBoxModel.addElement(filterSupplier);

        // Set data cho column supplier combobox
        supplierComboBoxModel1 = new SupplierComboboxModel();

        // Set data cho combobox supplier
        supplierComboBoxRenderer = new SupplierComboboxRenderer();
        cbSupFilter.setModel(supplierComboBoxModel);
        cbSupFilter.setRenderer(supplierComboBoxRenderer);
        
        
        // Set sorter cho table
        sorter = new TableRowSorter<>(inboundTableModel);
        tbInboundList.setRowSorter(sorter);
        
        // Select mac dinh cho level filter
        cbUserFilter.setSelectedIndex(cbUserFilter.getItemCount() - 1);
        cbSupFilter.setSelectedIndex(cbSupFilter.getItemCount() - 1);
        formatTable();
        
       
        btnDelete.setEnabled(false);
        btnUpdate.setEnabled(false);
        
    }
    
    public void setting(){
        // Add prompt/hint to textfield
        PromptSupport.setPrompt("Find InID", tfIdFilter);
        PromptSupport.setPrompt("Find Invoice", tfSupInID);

        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfIdFilter);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT, tfSupInID);
        
        
        // Setup button state
       
        
        // ---------- Settup Jdatechooser
        jdcFromDate.setBounds(0, 0, 120,35);
        jdcFromDate.setDateFormatString("MMM dd, yyyy");
        jdcToDate.setBounds(0, 0, 120, 35);
        jdcToDate.setDateFormatString("MMM dd, yyyy");
        jdcFromDate.getDateEditor().setEnabled(false);
        jdcToDate.getDateEditor().setEnabled(false);
        pnlFrom.add(jdcFromDate);
        pnlTo.add(jdcToDate);
        
        

        // Add listener for date selection change
        jdcFromDate.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                doFilter();
            }
        });
        jdcToDate.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                doFilter();
            }
        });

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
        tfSupInID.getDocument().addDocumentListener(new DocumentListener() {
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
        // Set auto-select all when FocusGained
        FrameWork.setAutoSelectAll(pnlSearch);

        // Start editing
        tfIdFilter.requestFocus();
        
        // Set data cho table
        inboundTableModel = new InboundTableModel();
        inboundProductTableModel = new InboundProductTableModel();
        tbInboundList.setModel(inboundTableModel);
        tbProductList.setModel(inboundProductTableModel);
        
        // Bat su kien select row tren table sales off
        tbInboundList.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            if (!model.isSelectionEmpty()) {
                fetchAction();
                btnDelete.setEnabled(true);
                btnUpdate.setEnabled(true);
               
            }
        });
        
        // Set table cell listener to update table sales off
        TableCellListener tcl1 = new TableCellListener(tbInboundList, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                switch (tcl.getColumn()) {
                    case COL_InDate:
                        selectedInbound.setInDate((Date) tcl.getNewValue());
                        break;
                    case COL_SupName:
                        selectedInbound.setSupName((String) tcl.getNewValue());
                        break;
                    case COL_SupInID:
                        selectedInbound.setSupInvoiceID((String) tcl.getNewValue());
                        break;
                    case COL_UserName:
                        selectedInbound.setUserName((String) tcl.getNewValue());
                        break;
                }
                updateAction();
            }
        });
    }
    
    private void doFilter() {
        RowFilter<InboundTableModel, Object> rf = null;

        //Filter theo regex, neu parse bi loi thi khong filter
        try {
            List<RowFilter<InboundTableModel, Object>> filters = new ArrayList<>();
            filters.add(RowFilter.regexFilter("^" + tfIdFilter.getText(), 0));
            filters.add(RowFilter.regexFilter("^" + tfSupInID.getText(), 3));
            
            if(jdcFromDate.getDate()!=null){
                
                
            if(jdcFromDate.getDate()!=null||jdcToDate.getDate()!=null){
                System.err.println("bbbbb");
                if(jdcFromDate.getDate()!=null)
                filters.add( RowFilter.dateFilter(ComparisonType.AFTER, jdcFromDate.getDate(),1) );
                if(jdcToDate.getDate()!=null)
                filters.add( RowFilter.dateFilter(ComparisonType.BEFORE, jdcToDate.getDate(),1) );
                
            }
            }
            if(jdcFromDate.getDate()==null&&jdcToDate.getDate()==null){
                System.err.println("aaa");
                
            }
            // Neu co chon cus level thi moi filter level
            if (cbUserFilter.getSelectedIndex() != cbUserFilter.getItemCount() - 1) {
                filters.add(RowFilter.regexFilter("^" + ((User) cbUserFilter.getSelectedItem()).getUsername(), 4));
            }
            if(cbSupFilter.getSelectedItem()!=null){
            if (cbSupFilter.getSelectedIndex() != cbSupFilter.getItemCount() - 1) {
                
                filters.add(RowFilter.regexFilter("^" + ((Supplier) cbSupFilter.getSelectedItem()).getSupName(), 2));
            }
            }

            rf = RowFilter.andFilter(filters);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
    
    private void fetchAction() {
        selectedRowIndex = tbInboundList.getSelectedRow();
        selectedInbound.setInID((int) tbInboundList.getValueAt(selectedRowIndex, 0));
        selectedInbound.setInDate((Date) tbInboundList.getValueAt(selectedRowIndex, 1));
        selectedInbound.setSupName(((String) tbInboundList.getValueAt(selectedRowIndex, 2)));
        selectedInbound.setSupInvoiceID((String) tbInboundList.getValueAt(selectedRowIndex, 3));
        selectedInbound.setUserName((String) tbInboundList.getValueAt(selectedRowIndex, 4));
        // Reload table product list voi Order moi chon
        inboundProductTableModel.load(selectedInbound.getInID());
    }
    

    
    

    

    
    
    public void formatTable(){
        tbInboundList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        tbProductList.getTableHeader().setPreferredSize(new Dimension(300, 30));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        //id
        tbInboundList.getColumnModel().getColumn(COL_InID).setMinWidth(70);
        tbInboundList.getColumnModel().getColumn(COL_InID).setMaxWidth(70);
        tbInboundList.getColumnModel().getColumn(COL_InID).setCellRenderer(centerRenderer);
        
        tbInboundList.getColumnModel().getColumn(COL_InDate).setMinWidth(150);
        tbInboundList.getColumnModel().getColumn(COL_InDate).setMaxWidth(150);
        tbInboundList.getColumnModel().getColumn(COL_InDate).setCellRenderer(centerRenderer);
        
        
        tbInboundList.getColumnModel().getColumn(COL_SupName).setMinWidth(250);
        tbInboundList.getColumnModel().getColumn(COL_SupName).setMaxWidth(250);
        tbInboundList.getColumnModel().getColumn(COL_SupName).setCellRenderer(centerRenderer);
        
        tbInboundList.getColumnModel().getColumn(COL_SupInID).setMinWidth(150);
        tbInboundList.getColumnModel().getColumn(COL_SupInID).setMaxWidth(150);
        tbInboundList.getColumnModel().getColumn(COL_SupInID).setCellRenderer(centerRenderer); 
        
        
        tbInboundList.getColumnModel().getColumn(COL_UserName).setMinWidth(175);
        tbInboundList.getColumnModel().getColumn(COL_UserName).setMaxWidth(175);
        tbInboundList.getColumnModel().getColumn(COL_UserName).setCellRenderer(centerRenderer);
        
        
        
        
         // Col pro ID
        tbProductList.getColumnModel().getColumn(COL_PROID).setMinWidth(70);
        tbProductList.getColumnModel().getColumn(COL_PROID).setMaxWidth(70);
        tbProductList.getColumnModel().getColumn(COL_PROID).setCellRenderer(centerRenderer);
        // Col pro name
        tbProductList.getColumnModel().getColumn(COL_PRONAME).setMinWidth(150);
        tbProductList.getColumnModel().getColumn(COL_PRONAME).setCellRenderer(centerRenderer);
        // Col quantity
        tbProductList.getColumnModel().getColumn(COL_PROQTY).setMinWidth(120);
        tbProductList.getColumnModel().getColumn(COL_PROQTY).setMaxWidth(120);
        tbProductList.getColumnModel().getColumn(COL_PROQTY).setCellRenderer(centerRenderer);
        // Col price
        tbProductList.getColumnModel().getColumn(COL_PROPRICE).setMinWidth(125);
        tbProductList.getColumnModel().getColumn(COL_PROPRICE).setCellRenderer(new CurrencyCellRenderer());
    }
   
    

    // Handler for list selection changes  
   

    
    
    public void resetFilter(){
        tfIdFilter.setText(null);
        jdcFromDate.setCalendar(null);
        
        jdcToDate.setCalendar(null);
        
        cbSupFilter.setSelectedIndex(cbSupFilter.getItemCount() - 1);
        cbUserFilter.setSelectedIndex(cbUserFilter.getItemCount() - 1);
        
        tfSupInID.setText(null);
        
    }
    
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlSearch = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfIdFilter = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfSupInID = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        cbSupFilter = new javax.swing.JComboBox<>();
        cbUserFilter = new javax.swing.JComboBox<>();
        pnlFrom = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pnlTo = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        tableScrollPane = new javax.swing.JScrollPane();
        tbInboundList = new javax.swing.JTable();
        jScroll = new javax.swing.JScrollPane();
        tbProductList = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(810, 680));

        pnlSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

        jLabel1.setText("Inbound ID");

        tfIdFilter.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tfIdFilterCaretUpdate(evt);
            }
        });
        tfIdFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfIdFilterActionPerformed(evt);
            }
        });

        jLabel2.setText("Supplier Name:");

        jLabel4.setText("To Date:");

        jLabel5.setText("Supplier Invoice ID:");

        jLabel6.setText("User ID:");

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/refresh_1.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        cbSupFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbSupFilterItemStateChanged(evt);
            }
        });
        cbSupFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSupFilterActionPerformed(evt);
            }
        });

        cbUserFilter.setPreferredSize(new java.awt.Dimension(31, 32));
        cbUserFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbUserFilterItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout pnlFromLayout = new javax.swing.GroupLayout(pnlFrom);
        pnlFrom.setLayout(pnlFromLayout);
        pnlFromLayout.setHorizontalGroup(
            pnlFromLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 132, Short.MAX_VALUE)
        );
        pnlFromLayout.setVerticalGroup(
            pnlFromLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        jLabel3.setText("From Date:");

        javax.swing.GroupLayout pnlToLayout = new javax.swing.GroupLayout(pnlTo);
        pnlTo.setLayout(pnlToLayout);
        pnlToLayout.setHorizontalGroup(
            pnlToLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlToLayout.setVerticalGroup(
            pnlToLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 39, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlSearchLayout = new javax.swing.GroupLayout(pnlSearch);
        pnlSearch.setLayout(pnlSearchLayout);
        pnlSearchLayout.setHorizontalGroup(
            pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSearchLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(cbSupFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSearchLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(pnlSearchLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbUserFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5))
                    .addGroup(pnlSearchLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfSupInID, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 14, Short.MAX_VALUE))
        );
        pnlSearchLayout.setVerticalGroup(
            pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchLayout.createSequentialGroup()
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(tfSupInID, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5)
                    .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(cbUserFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlSearchLayout.createSequentialGroup()
                .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlSearchLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(pnlTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSearchLayout.createSequentialGroup()
                        .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(tfIdFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbSupFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tableScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inbound Note", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N
        tableScrollPane.setAutoscrolls(true);

        tbInboundList.setModel(new javax.swing.table.DefaultTableModel(
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
        tbInboundList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbInboundListMouseClicked(evt);
            }
        });
        tableScrollPane.setViewportView(tbInboundList);

        jScroll.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inbound Note Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(255, 153, 0))); // NOI18N

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
        jScroll.setViewportView(tbProductList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableScrollPane)
            .addComponent(jScroll)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(tableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Lucida Sans", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Inbound.png"))); // NOI18N
        jLabel7.setText("<html><u><i><font color='red'>I</font>nbound <font color='red'>M</font>anagement</i></u></html>");

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/banned.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Save.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product/Add.png"))); // NOI18N
        jButton7.setText("Insert");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addComponent(btnUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7)
                    .addComponent(pnlSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(pnlSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
            
            new InboundDialog(null).setVisible(true);
            refreshAction(false);

            scrollToRow(tbInboundList.getRowCount() - 1);
       
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tbInboundListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbInboundListMouseClicked
        
    }//GEN-LAST:event_tbInboundListMouseClicked

    private void cbUserFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbUserFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbUserFilterItemStateChanged

    private void cbSupFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSupFilterItemStateChanged
        doFilter();
    }//GEN-LAST:event_cbSupFilterItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        resetFilter();
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void tfIdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfIdFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfIdFilterActionPerformed

    private void tfIdFilterCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_tfIdFilterCaretUpdate
        //        search();        // TODO add your handling code here:
    }//GEN-LAST:event_tfIdFilterCaretUpdate

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
      
      //neu khong co row nao duoc chon, k cho xoa  
      int selectedRowIndex = tbInboundList.getSelectedRow();
      if(selectedRowIndex<0){
          JOptionPane.showMessageDialog(this, "Choose inbound to delete!");
          return;
      }
      int ans =  JOptionPane.showConfirmDialog(this, "Are you sure to delete?", "Confirm", JOptionPane.YES_NO_OPTION);
      if(ans==JOptionPane.YES_OPTION){
        deleteAction();
        refreshAction(true);
        formatTable();
      }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void cbSupFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSupFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSupFilterActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        new InboundDialog(selectedInbound).setVisible(true);
        refreshAction(false);
       new ProductPanel().getProductTableModel().fireTableDataChanged();
    }//GEN-LAST:event_btnUpdateActionPerformed
    public void setTableColumnType(JTable tbl, Integer colID, Class type) {
        TableColumn col = tbl.getColumnModel().getColumn(colID);
        col.setCellEditor(tbl.getDefaultEditor(type));
        col.setCellRenderer(tbl.getDefaultRenderer(type));
    }
    private void refreshAction(boolean mustInfo) {
        if (mustInfo) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Refresh table
            inboundTableModel.refresh();

            // Refresh combobox filter
            supplierComboBoxModel.refresh();
            supplierComboBoxModel.addElement(filterSupplier);

            // Refresh combobox column table
//            branchNameComboBoxModel1.refresh();
            setCursor(null);
            
        } else {
            // Refresh table
            inboundTableModel.refresh();

            // Refresh combobox filter
            supplierComboBoxModel.refresh();
            supplierComboBoxModel.addElement(filterSupplier);

            // Refresh combobox column table
            supplierComboBoxModel1.refresh();
        }
        scrollToRow(selectedRowIndex);
    }
    
    private void scrollToRow(int row) {
        tbProductList.getSelectionModel().setSelectionInterval(row, row);
        tbProductList.scrollRectToVisible(new Rectangle(tbProductList.getCellRect(row, 0, true)));
    }
   private void deleteAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = inboundTableModel.delete(selectedInbound);
        setCursor(null);
        

        // Neu row xoa la row cuoi thi lui cursor ve
        // Neu row xoa la row khac cuoi thi tien cursor ve truoc
        selectedRowIndex = (selectedRowIndex == tbProductList.getRowCount() ? tbProductList.getRowCount() - 1 : selectedRowIndex++);
        scrollToRow(selectedRowIndex);
        JOptionPane.showMessageDialog(this, SwingUtils.DELETE_SUCCESS);
    }
   
   private void updateAction() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        boolean result = inboundTableModel.update(selectedInbound);
        refreshAction(false);
        setCursor(null);
        formatTable();
        SwingUtils.showInfoDialog(result ? SwingUtils.UPDATE_SUCCESS : SwingUtils.UPDATE_FAIL);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<Supplier> cbSupFilter;
    private javax.swing.JComboBox<User> cbUserFilter;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScroll;
    private javax.swing.JPanel pnlFrom;
    private javax.swing.JPanel pnlSearch;
    private javax.swing.JPanel pnlTo;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTable tbInboundList;
    private javax.swing.JTable tbProductList;
    private javax.swing.JTextField tfIdFilter;
    private javax.swing.JTextField tfSupInID;
    // End of variables declaration//GEN-END:variables
}
