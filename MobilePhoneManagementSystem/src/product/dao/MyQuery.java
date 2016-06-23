/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.dao;

import product.model.Product;
import static com.oracle.jrockit.jfr.ContentType.Bytes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.Query;
import javax.rmi.CORBA.Util;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author tuan
 */
public class MyQuery {

    public static ArrayList<String> byteArray = new ArrayList<String>();
    private static int availableRecord;

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlserver://localhost;DatabaseName=Mobile", "sa", "26101996");
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    public ArrayList<Product> BindTable() {

        ArrayList<Product> list = new ArrayList<Product>();
        Connection con = getConnection();
        Statement st;
        ResultSet rs;

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT [ProID]\n"
                    + "      ,[BraID]\n"
                    + "      ,[ProName]\n"
                    + "      ,[ProStock]\n"
                    + "      ,[ProPrice]\n"
                    + "      ,[ProDescr]\n"
                    + "      ,[ProEnabled]\n"
                    + "      ,[SalesOffID]\n"
                    + "      ,[ProImage]\n"
                    + "  FROM [Mobile].[dbo].[Products] ORDER BY ProID DESC");

            Product p;
//            while (rs.next()) {
//                p = new Product(
//                        rs.getInt("ProID"),
//                        rs.getInt("BraID"),
//                        rs.getString("ProName"),
//                        rs.getInt("ProStock"),
//                        rs.getFloat("ProPrice"),
//                        rs.getString("ProDescr"),
//                        rs.getBoolean("ProEnabled"),
//                        rs.getInt("SalesOffID"),
//                        rs.getBytes("ProImage")
//                );
//                list.add(p);
//            }

        } catch (SQLException ex) {
            Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static void displayToTextField(PreparedStatement ps, ResultSet rs, String sqlgetColumnName, String sql, int title, JTextField txtID, JTextField txtName, JTextField txtStock, JTextField price, JTextArea desc, ButtonGroup btnGr, JRadioButton enabled, JRadioButton disabled) {

        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            String[] tableTitle = returnColumnName(title, sqlgetColumnName);

            if (rs.next()) {
                txtID.setText(new Integer(rs.getInt(tableTitle[0])).toString());
                txtName.setText(rs.getString(tableTitle[1]));
                txtStock.setText(new Integer(rs.getInt(tableTitle[2])).toString());

                price.setText(new java.math.BigDecimal(rs.getFloat(tableTitle[3])).toString());
                desc.setText(rs.getString(tableTitle[4]));

                btnGr.setSelected(new Boolean(rs.getBoolean(tableTitle[5])).toString().equals("true") ? enabled.getModel() : disabled.getModel(), true);

            }
        } catch (SQLException ex) {
            Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String[] returnColumnName(int title, String selectSql) {
        String tableTitle[] = new String[title];
        Connection conn = getConnection();
        try {

            PreparedStatement pst1 = conn.prepareStatement(selectSql);
            ResultSet rs1 = pst1.executeQuery();
            ResultSetMetaData rsmd = rs1.getMetaData();

            for (int i = 0; i < title; i++) {
                tableTitle[i] = rsmd.getColumnName(i + 1);

            }
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tableTitle;
    }

    public static JComboBox createComboBox(PreparedStatement st, ResultSet rs, DefaultComboBoxModel cmbModel, JComboBox cmbCondition, String sql, String itemToDisplay) {
        try {

            Connection conn = getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            Vector data = new Vector();
            data.add("-Choose-");
            while (rs.next()) {
                data.add(rs.getString(itemToDisplay));
            }

            cmbModel = new DefaultComboBoxModel(data);
            cmbCondition.setModel(cmbModel);

        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cmbCondition;
    }

    public static String displayBranchNameByID(PreparedStatement st, ResultSet rs, String sql) {
        String braName = null;
        try {

            Connection conn = getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                braName = rs.getString("BraName");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return braName;
    }

    public static int displayIDByBranchName(PreparedStatement st, ResultSet rs, String sql) {
        int braID = 0;
        try {

            Connection conn = getConnection();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                braID = rs.getInt("BraID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return braID;
    }

    public static boolean isNumeric(String str) {
        if (str.trim().length() == 0) {
            return false;
        }
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public static int countRecords() {
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            Connection conn = getConnection();

            ps = conn.prepareStatement("SELECT count(*) from products");
            rs = ps.executeQuery();
            while (rs.next()) {
                availableRecord = rs.getInt(1);
            }

        } catch (SQLException ex) {
            System.err.println("cho sai");;
        }
        return availableRecord;
    }

    public static boolean checkBlankTextField(JTextComponent... com) {

        for (JTextComponent t : com) {
            if (t.getText().isEmpty()) {
                t.requestFocus();
                return true;
            }
        }
        return false;
    }
}
