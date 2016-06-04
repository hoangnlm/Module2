/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.xswingx.PromptSupport;
import utility.DBUtils;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class ConfigPanel extends javax.swing.JPanel {

    private LoginFrame loginFrame;
    private JPanel parent;
    private LoginConfig config;

    /**
     * Creates new form LoginPanel
     */
    public ConfigPanel(JPanel parent) {
        this.parent = parent;
        this.loginFrame = (LoginFrame) parent.getParent().getParent().getParent().getParent();
        this.config = loginFrame.config;

        initComponents();
        setBackground(new Color(0, 255, 0, 0));
        tfHost.setBackground(new Color(255, 255, 255, 150));
        tfHost.setHorizontalAlignment(JTextField.CENTER);
        tfPort.setBackground(new Color(255, 255, 255, 150));
        tfPort.setHorizontalAlignment(JTextField.CENTER);
        tfDBName.setBackground(new Color(255, 255, 255, 150));
        tfDBName.setHorizontalAlignment(JTextField.CENTER);
        tfName.setBackground(new Color(255, 255, 255, 150));
        tfName.setHorizontalAlignment(JTextField.CENTER);
        tfPassword.setBackground(new Color(255, 255, 255, 150));
        tfPassword.setHorizontalAlignment(JTextField.CENTER);

        // Set place holder
        PromptSupport.setForeground(Color.RED, tfHost);
        PromptSupport.setForeground(Color.RED, tfPort);
        PromptSupport.setForeground(Color.RED, tfDBName);
        PromptSupport.setForeground(Color.RED, tfName);
        PromptSupport.setForeground(Color.RED, tfPassword);
        PromptSupport.setPrompt("Host...", tfHost);
        PromptSupport.setPrompt("Port...", tfPort);
        PromptSupport.setPrompt("Database...", tfDBName);
        PromptSupport.setPrompt("Name...", tfName);
        PromptSupport.setPrompt("Password...", tfPassword);

        // Khoi tao tri mac dinh
        tfHost.setText(config.host);
        tfPort.setText(config.port);
        tfDBName.setText(config.DBName);
        tfName.setText(config.name);
        tfPassword.setText(config.password);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfHost = new javax.swing.JTextField();
        tfPort = new javax.swing.JTextField();
        tfDBName = new javax.swing.JTextField();
        tfName = new javax.swing.JTextField();
        tfPassword = new javax.swing.JPasswordField();
        btOK = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();
        lbBg = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(270, 554));
        setLayout(null);

        tfHost.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfHostFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfHostFocusLost(evt);
            }
        });
        add(tfHost);
        tfHost.setBounds(60, 120, 150, 40);

        tfPort.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfPortFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfPortFocusLost(evt);
            }
        });
        add(tfPort);
        tfPort.setBounds(60, 170, 150, 40);

        tfDBName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfDBNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfDBNameFocusLost(evt);
            }
        });
        add(tfDBName);
        tfDBName.setBounds(60, 220, 150, 40);

        tfName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfNameFocusLost(evt);
            }
        });
        add(tfName);
        tfName.setBounds(60, 270, 150, 40);

        tfPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfPasswordFocusLost(evt);
            }
        });
        add(tfPassword);
        tfPassword.setBounds(60, 320, 150, 40);

        btOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/OK.png"))); // NOI18N
        btOK.setBorderPainted(false);
        btOK.setContentAreaFilled(false);
        btOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOKActionPerformed(evt);
            }
        });
        add(btOK);
        btOK.setBounds(30, 420, 50, 50);

        btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Cancel.png"))); // NOI18N
        btCancel.setBorderPainted(false);
        btCancel.setContentAreaFilled(false);
        btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelActionPerformed(evt);
            }
        });
        add(btCancel);
        btCancel.setBounds(190, 420, 50, 50);

        lbBg.setBackground(new java.awt.Color(255, 255, 255));
        lbBg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Login.png"))); // NOI18N
        add(lbBg);
        lbBg.setBounds(0, 0, 269, 554);
    }// </editor-fold>//GEN-END:initComponents

    private void tfHostFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfHostFocusGained
        tfHost.setBackground(new Color(255, 255, 255, 255));
    }//GEN-LAST:event_tfHostFocusGained

    private void tfHostFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfHostFocusLost
        tfHost.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfHostFocusLost

    private void tfPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPasswordFocusGained
        tfPassword.setBackground(new Color(255, 255, 255, 255));
    }//GEN-LAST:event_tfPasswordFocusGained

    private void tfPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPasswordFocusLost
        tfPassword.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfPasswordFocusLost

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        parent.removeAll();
        parent.add(new LoginPanel(parent));
        parent.validate();
        parent.repaint();
    }//GEN-LAST:event_btCancelActionPerformed

    private void tfPortFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPortFocusGained
        tfPort.setBackground(new Color(255, 255, 255, 255));
    }//GEN-LAST:event_tfPortFocusGained

    private void tfPortFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPortFocusLost
        tfPort.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfPortFocusLost

    private void tfDBNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDBNameFocusGained
        tfDBName.setBackground(new Color(255, 255, 255, 255));
    }//GEN-LAST:event_tfDBNameFocusGained

    private void tfDBNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDBNameFocusLost
        tfDBName.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfDBNameFocusLost

    private void tfNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfNameFocusGained
        tfName.setBackground(new Color(255, 255, 255, 255));
    }//GEN-LAST:event_tfNameFocusGained

    private void tfNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfNameFocusLost
        tfName.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfNameFocusLost

    private void btOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOKActionPerformed
        checkConnection();
    }//GEN-LAST:event_btOKActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btOK;
    private javax.swing.JLabel lbBg;
    public javax.swing.JTextField tfDBName;
    public javax.swing.JTextField tfHost;
    public javax.swing.JTextField tfName;
    public javax.swing.JPasswordField tfPassword;
    public javax.swing.JTextField tfPort;
    // End of variables declaration//GEN-END:variables

    private void checkConnection() {
        config.host = tfHost.getText();
        config.port = tfPort.getText();
        config.DBName = tfDBName.getText();
        config.name = tfName.getText();
        config.password = new String(tfPassword.getPassword());
        DBUtils db = new DBUtils(config.host, config.port, config.DBName, config.name, config.password);
        if(!db.start()){
            SwingUtils.showErrorDialog("Error: cannot connect database!");
        }else{
            SwingUtils.showMessageDialog("Connected database successfully!");
            db.stop();
        }
    }
}
