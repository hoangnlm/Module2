/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.controller;

import com.toedter.calendar.JDateChooser;

/**
 *
 * @author Hoang
 */
public class HomePanel extends javax.swing.JPanel {

    private JDateChooser date;

    /**
     * Creates new form OrderPanel
     */
    public HomePanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btStatistics = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setPreferredSize(new java.awt.Dimension(810, 680));
        setLayout(null);

        btStatistics.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        btStatistics.setForeground(new java.awt.Color(255, 153, 0));
        btStatistics.setText("Mobile Phone Shop Management System...");
        btStatistics.setFocusPainted(false);
        add(btStatistics);
        btStatistics.setBounds(10, 640, 290, 30);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/home/HomeBG.jpg"))); // NOI18N
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        add(jLabel5);
        jLabel5.setBounds(0, -30, 810, 730);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btStatistics;
    private javax.swing.JLabel jLabel5;
    // End of variables declaration//GEN-END:variables
}
