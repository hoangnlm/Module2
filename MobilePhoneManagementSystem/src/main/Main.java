/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

/**
 *
 * @author Hoang
 */
public final class Main extends javax.swing.JFrame {

    private final ButtonGroup group = new ButtonGroup();
    private final int sidebarTotal = 10;
    private final JRadioButton[] rb = new JRadioButton[sidebarTotal];
    private final JLabel[] lb = new JLabel[sidebarTotal];
    private final JPanel[] pn = new JPanel[sidebarTotal];

    private final Color normalState = new Color(51, 51, 51); // light black
    private final Color hoverState = Color.CYAN;
    private final Color seletedState = Color.ORANGE;

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();

        setSidebar();
        setSelected(0);
    }

    private void setSidebar() {
        lb[0] = lbHome;
        lb[1] = lbProduct;
        lb[2] = lbInbound;
        lb[3] = lbOutbound;
        lb[4] = lbOrder;
        lb[5] = lbCustomer;
        lb[6] = lbSupplier;
        lb[7] = lbService;
        lb[8] = lbUser;
        lb[9] = lbLogout;

        pn[0] = new JPanel();
        pn[1] = new JPanel();
        pn[2] = new JPanel();
        pn[3] = new JPanel();
        pn[4] = new JPanel();
        pn[5] = new JPanel();
        pn[6] = new JPanel();
        pn[7] = new JPanel();
        pn[8] = new JPanel();
        pn[9] = new JPanel();

        for (int i = 0; i < sidebarTotal; i++) {
            setSidebarItem(lb[i]);
            rb[i] = new JRadioButton();
            group.add(rb[i]);
        }
    }

    private void setSidebarItem(JLabel label) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                 System.out.println("Mouse pressed inside the component");
                if (e.getComponent() instanceof JLabel) {
                    for (int i = 0; i < rb.length; i++) {
                        if (lb[i] == (JLabel) e.getComponent()) {
                            setSelected(i);
                            return;
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
//                 System.out.println("Mouse Entered inside the component");
                if (e.getComponent() instanceof JLabel) {
                    for (int i = 0; i < rb.length; i++) {
                        if (lb[i] == (JLabel) e.getComponent() && rb[i].isSelected() == false) {
                            setHover((JLabel) e.getComponent());
                        }
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
//                 System.out.println("Mouse Exitted inside the component");
                if (e.getComponent() instanceof JLabel) {
                    for (int i = 0; i < rb.length; i++) {
                        if (lb[i] == (JLabel) e.getComponent() && rb[i].isSelected() == false) {
                            setNormal((JLabel) e.getComponent());
                        }
                    }
                }
            }
        });
        setNormal(label);
    }

    public void setNormal(JLabel label) {
        label.setBackground(normalState);
        label.setForeground(Color.WHITE);
    }

    public void setHover(JLabel label) {
        label.setBackground(hoverState);
        label.setForeground(Color.BLACK);
    }

    public void setSelected(int index) {
        // If user pressed "Sign out"
        if (lb[index].getText() == "Log out" || lb[index].getName() == "Log out") {
            logOut();
            return;
        }
        // If not, set selected item color & radio button
        for (int i = 0; i < rb.length; i++) {
            if (i == index) {
                rb[i].setSelected(true);
                lb[i].setBackground(seletedState);
                lb[i].setForeground(Color.BLACK);
                setPanel(pn[i]);
            } else {
                setNormal(lb[i]);
            }
        }
    }

    public void setPanel(JPanel pnChild) {
        pnMain.removeAll();
        pnMain.add(pnChild);
        pnMain.validate();
        pnMain.repaint();
    }

    private void logOut() {
        int ret = JOptionPane.showConfirmDialog(this, "Are you sure to sign out ?", "Confirm sign out", JOptionPane.YES_NO_OPTION);
        if (ret == JOptionPane.YES_OPTION) {
//            new Login().setVisible(true);
            // Reset userID
            // Close the main form
            dispose();
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

        pnSidebar = new javax.swing.JPanel();
        lbHome = new javax.swing.JLabel();
        lbProduct = new javax.swing.JLabel();
        lbInbound = new javax.swing.JLabel();
        lbOrder = new javax.swing.JLabel();
        lbOutbound = new javax.swing.JLabel();
        lbCustomer = new javax.swing.JLabel();
        lbSupplier = new javax.swing.JLabel();
        lbUser = new javax.swing.JLabel();
        lbService = new javax.swing.JLabel();
        lbLogout = new javax.swing.JLabel();
        pnMain = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mobile Phone Management System");
        setSize(new java.awt.Dimension(900, 650));

        pnSidebar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbHome.setBackground(new java.awt.Color(51, 51, 51));
        lbHome.setFont(new java.awt.Font("Lucida Calligraphy", 0, 14)); // NOI18N
        lbHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Home.png"))); // NOI18N
        lbHome.setText("HOME");
        lbHome.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        lbHome.setOpaque(true);
        lbHome.setPreferredSize(new java.awt.Dimension(170, 60));

        lbProduct.setBackground(new java.awt.Color(51, 51, 51));
        lbProduct.setFont(new java.awt.Font("Lucida Calligraphy", 0, 14)); // NOI18N
        lbProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Product.png"))); // NOI18N
        lbProduct.setText("PRODUCT");
        lbProduct.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        lbProduct.setOpaque(true);
        lbProduct.setPreferredSize(new java.awt.Dimension(170, 60));

        lbInbound.setBackground(new java.awt.Color(51, 51, 51));
        lbInbound.setFont(new java.awt.Font("Lucida Calligraphy", 0, 14)); // NOI18N
        lbInbound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Inbound.png"))); // NOI18N
        lbInbound.setText("INBOUND");
        lbInbound.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        lbInbound.setOpaque(true);
        lbInbound.setPreferredSize(new java.awt.Dimension(170, 60));

        lbOrder.setBackground(new java.awt.Color(51, 51, 51));
        lbOrder.setFont(new java.awt.Font("Lucida Calligraphy", 0, 14)); // NOI18N
        lbOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Order.png"))); // NOI18N
        lbOrder.setText("ORDER");
        lbOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        lbOrder.setOpaque(true);
        lbOrder.setPreferredSize(new java.awt.Dimension(170, 60));

        lbOutbound.setBackground(new java.awt.Color(51, 51, 51));
        lbOutbound.setFont(new java.awt.Font("Lucida Calligraphy", 0, 14)); // NOI18N
        lbOutbound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Outbound.png"))); // NOI18N
        lbOutbound.setText("OUTBOUND");
        lbOutbound.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        lbOutbound.setOpaque(true);
        lbOutbound.setPreferredSize(new java.awt.Dimension(170, 60));

        lbCustomer.setBackground(new java.awt.Color(51, 51, 51));
        lbCustomer.setFont(new java.awt.Font("Lucida Calligraphy", 0, 14)); // NOI18N
        lbCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Customer.png"))); // NOI18N
        lbCustomer.setText("CUSTOMER");
        lbCustomer.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        lbCustomer.setOpaque(true);
        lbCustomer.setPreferredSize(new java.awt.Dimension(170, 60));

        lbSupplier.setBackground(new java.awt.Color(51, 51, 51));
        lbSupplier.setFont(new java.awt.Font("Lucida Calligraphy", 0, 14)); // NOI18N
        lbSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Supplier.png"))); // NOI18N
        lbSupplier.setText("SUPPLIER");
        lbSupplier.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        lbSupplier.setOpaque(true);
        lbSupplier.setPreferredSize(new java.awt.Dimension(170, 60));

        lbUser.setBackground(new java.awt.Color(51, 51, 51));
        lbUser.setFont(new java.awt.Font("Lucida Calligraphy", 0, 14)); // NOI18N
        lbUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/User.png"))); // NOI18N
        lbUser.setText("USER");
        lbUser.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        lbUser.setOpaque(true);
        lbUser.setPreferredSize(new java.awt.Dimension(170, 60));

        lbService.setBackground(new java.awt.Color(51, 51, 51));
        lbService.setFont(new java.awt.Font("Lucida Calligraphy", 0, 14)); // NOI18N
        lbService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Service.png"))); // NOI18N
        lbService.setText("SERVICE");
        lbService.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        lbService.setOpaque(true);
        lbService.setPreferredSize(new java.awt.Dimension(170, 60));

        lbLogout.setBackground(new java.awt.Color(51, 51, 51));
        lbLogout.setFont(new java.awt.Font("Lucida Calligraphy", 0, 14)); // NOI18N
        lbLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Logout.png"))); // NOI18N
        lbLogout.setText("LOG OUT");
        lbLogout.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        lbLogout.setName("Log out"); // NOI18N
        lbLogout.setOpaque(true);
        lbLogout.setPreferredSize(new java.awt.Dimension(170, 60));

        javax.swing.GroupLayout pnSidebarLayout = new javax.swing.GroupLayout(pnSidebar);
        pnSidebar.setLayout(pnSidebarLayout);
        pnSidebarLayout.setHorizontalGroup(
            pnSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSidebarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbHome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbInbound, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbOutbound, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbCustomer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbOrder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbSupplier, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbService, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbLogout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
            .addGroup(pnSidebarLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lbProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnSidebarLayout.setVerticalGroup(
            pnSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSidebarLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lbHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lbProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lbInbound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lbOutbound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lbOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lbCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lbService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lbUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lbLogout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnMainLayout = new javax.swing.GroupLayout(pnMain);
        pnMain.setLayout(pnMainLayout);
        pnMainLayout.setHorizontalGroup(
            pnMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );
        pnMainLayout.setVerticalGroup(
            pnMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnSidebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new HiFiLookAndFeel());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbCustomer;
    private javax.swing.JLabel lbHome;
    private javax.swing.JLabel lbInbound;
    private javax.swing.JLabel lbLogout;
    private javax.swing.JLabel lbOrder;
    private javax.swing.JLabel lbOutbound;
    private javax.swing.JLabel lbProduct;
    private javax.swing.JLabel lbService;
    private javax.swing.JLabel lbSupplier;
    private javax.swing.JLabel lbUser;
    private javax.swing.JPanel pnMain;
    private javax.swing.JPanel pnSidebar;
    // End of variables declaration//GEN-END:variables
}
