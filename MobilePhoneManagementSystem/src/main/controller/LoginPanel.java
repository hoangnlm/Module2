/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.controller;

import main.model.Login;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.jdesktop.xswingx.PromptSupport;
import database.DBProvider;
import java.awt.Container;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;
import main.model.LoginDAOImpl;
import main.model.UserFunction;
import utility.IOUtils;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class LoginPanel extends javax.swing.JPanel {

    private LoginFrame parent;
    private Login config;
    private List<UserFunction> functions;

    /**
     * Creates new form LoginPanel
     * @param parent
     */
    public LoginPanel(Container parent) {
        this.parent = (LoginFrame) parent;
        this.config = LoginFrame.config;
        this.functions = new ArrayList<>();

        // Set thuoc tinh cho GUI
        initComponents();
        setBackground(new Color(0, 255, 0, 0));
        tfUserName.setBackground(new Color(255, 255, 255, 150));
        tfUserName.setHorizontalAlignment(JTextField.CENTER);
        tfPassword.setBackground(new Color(255, 255, 255, 150));
        tfPassword.setHorizontalAlignment(JTextField.CENTER);

        //Set place holder
        PromptSupport.setForeground(Color.RED, tfUserName);
        PromptSupport.setForeground(Color.RED, tfPassword);
        PromptSupport.setPrompt("Login name...", tfUserName);
        PromptSupport.setPrompt("Login password...", tfPassword);

        // Khoi tao tri mac dinh
        tfUserName.setText(config.userName);
        tfPassword.setText(config.userPassword);

        // Set focus luc khoi tao
        tfUserName.requestFocus();

        // Set validate cho input
        SwingUtils.validateStringInput(tfUserName, 1, 30, SwingUtils.PATTERN_NAMENOSPACE);
        SwingUtils.validateStringInput(tfPassword, 1, 30, SwingUtils.PATTERN_NAMENOSPACE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfUserName = new javax.swing.JTextField();
        tfPassword = new javax.swing.JPasswordField();
        btOK = new javax.swing.JButton();
        btConnect = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();
        lbBg = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(270, 554));
        setLayout(null);

        tfUserName.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        tfUserName.setForeground(new java.awt.Color(153, 0, 153));
        tfUserName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfUserNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfUserNameFocusLost(evt);
            }
        });
        tfUserName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfUserNameActionPerformed(evt);
            }
        });
        add(tfUserName);
        tfUserName.setBounds(60, 120, 150, 40);

        tfPassword.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        tfPassword.setForeground(new java.awt.Color(153, 0, 153));
        tfPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfPasswordFocusLost(evt);
            }
        });
        tfPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPasswordActionPerformed(evt);
            }
        });
        add(tfPassword);
        tfPassword.setBounds(60, 170, 150, 40);

        btOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/OK.png"))); // NOI18N
        btOK.setBorderPainted(false);
        btOK.setContentAreaFilled(false);
        btOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOKActionPerformed(evt);
            }
        });
        add(btOK);
        btOK.setBounds(30, 470, 50, 50);

        btConnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Connect.png"))); // NOI18N
        btConnect.setBorderPainted(false);
        btConnect.setContentAreaFilled(false);
        btConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConnectActionPerformed(evt);
            }
        });
        add(btConnect);
        btConnect.setBounds(110, 470, 50, 50);

        btCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Cancel.png"))); // NOI18N
        btCancel.setBorderPainted(false);
        btCancel.setContentAreaFilled(false);
        btCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelActionPerformed(evt);
            }
        });
        add(btCancel);
        btCancel.setBounds(190, 470, 50, 50);

        lbBg.setBackground(new java.awt.Color(255, 255, 255));
        lbBg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/main/Login3.jpg"))); // NOI18N
        add(lbBg);
        lbBg.setBounds(0, 0, 324, 556);
    }// </editor-fold>//GEN-END:initComponents

//<editor-fold defaultstate="collapsed" desc="Bat su kien">
    private void tfUserNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfUserNameFocusGained
        tfUserName.setBackground(new Color(255, 255, 255, 255));
        tfUserName.selectAll();
    }//GEN-LAST:event_tfUserNameFocusGained

    private void tfUserNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfUserNameFocusLost
        tfUserName.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfUserNameFocusLost

    private void tfPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPasswordFocusGained
        tfPassword.setBackground(new Color(255, 255, 255, 255));
        tfPassword.selectAll();
    }//GEN-LAST:event_tfPasswordFocusGained

    private void tfPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPasswordFocusLost
        tfPassword.setBackground(new Color(255, 255, 255, 150));
    }//GEN-LAST:event_tfPasswordFocusLost

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelActionPerformed
        parent.exit();
    }//GEN-LAST:event_btCancelActionPerformed

    private void btConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConnectActionPerformed
        config.userName = tfUserName.getText();
        config.userPassword = new String(tfPassword.getPassword());
        parent.reloadContentPanel(LoginFrame.CONFIG_PANEL);
    }//GEN-LAST:event_btConnectActionPerformed

    private void btOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOKActionPerformed
        login();
    }//GEN-LAST:event_btOKActionPerformed

    private void tfUserNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfUserNameActionPerformed
        login();
    }//GEN-LAST:event_tfUserNameActionPerformed

    private void tfPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPasswordActionPerformed
        login();
    }//GEN-LAST:event_tfPasswordActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancel;
    private javax.swing.JButton btConnect;
    private javax.swing.JButton btOK;
    private javax.swing.JLabel lbBg;
    public javax.swing.JPasswordField tfPassword;
    public javax.swing.JTextField tfUserName;
    // End of variables declaration//GEN-END:variables
//</editor-fold>

    private void login() {
        // Check connection truoc xem co duoc ko, neu khong yeu cau user setting config
        if (!checkConnection()) {
            return;
        }

        // Neu username va password dung thi ghi data xuong file roi chuyen sang main frame
        if (checkLogin()) { // True khi thong tin hop le
            // Ghi du lieu dang nhap vao file
            config.userName = tfUserName.getText().trim();
            config.userPassword = new String(tfPassword.getPassword()).trim();
            config.userFunctions = functions;

            IOUtils.writeObject(LoginFrame.CONFIG_FILENAME, config);

            // Chuyen sang main frame
            SwingUtils.createLookAndFeel();
            new MainFrame().setVisible(true);

            // Dong login frame
            parent.dispose();
        }
    }

    private boolean checkConnection() {
        boolean result = false;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DBProvider db = new DBProvider(config.host, config.port, config.DBName, config.name, config.password);
        if (!db.start()) { //Ket noi database that bai
            setCursor(null);
            SwingUtils.showErrorDialog("Error: cannot connect database!");
            if (SwingUtils.showConfirmDialog("Would you like to setting configs ?") == JOptionPane.YES_OPTION) {
                parent.reloadContentPanel(LoginFrame.CONFIG_PANEL);
            }
        } else { // Ket noi database thanh cong
            db.stop();
            result = true;
            setCursor(null);
        }

        return result;
    }

    private boolean checkLogin() {
        boolean result = false;
        // Moc database len check
        List<Login> list = new LoginDAOImpl(tfUserName.getText().trim()).getList();
        SwingUtils.showInfoDialog("List login: "+list);
        if (list.isEmpty()) {
            SwingUtils.showInfoDialog("Username not correct ! Please try again !");
            tfUserName.requestFocus();
        } else if (!String.valueOf(tfPassword.getPassword()).trim().equals(list.get(0).userPassword)) {
            SwingUtils.showInfoDialog("Password not correct ! Please try again !");
            tfPassword.requestFocus();
        } else {
            functions = list.get(0).userFunctions;
            result = true;
        }
        return result;
    }
}
