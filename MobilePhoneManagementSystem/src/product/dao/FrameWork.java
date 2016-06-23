/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.dao;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.FilteredRowSetImpl;
import com.sun.rowset.JoinRowSetImpl;
import java.awt.Component;
import java.awt.Image;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.JoinRowSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

/**
 *
 * @author tuan
 */
public class FrameWork {
    static JoinRowSet jrs = null;
    public static Connection getConnection(String databaseName) throws SQLException{
        return DriverManager.getConnection("jdbc:sqlserver://localhost;DatabaseName="+databaseName, "sa", "26101996");
    }
    public static CachedRowSet cachedRowSet_connect(CachedRowSet crs,String sql,String databaseName) throws SQLException{
            crs = new CachedRowSetImpl();
            crs.setUrl("jdbc:sqlserver://localhost;DatabaseName="+databaseName);
            crs.setUsername("sa");
            crs.setPassword("26101996");
            crs.setCommand(sql);
            crs.execute();
            return crs;
    }
    
    public static JoinRowSet createJoinRowSetFrom_2CachedRowSet(CachedRowSet crs,CachedRowSet crs1, String foreignKey){
        try {
            jrs = new JoinRowSetImpl();
            jrs.addRowSet(crs, foreignKey);
            jrs.addRowSet(crs1, foreignKey);
            
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
          return jrs;  
    }
    public static DefaultTableModel createStringAndDefaultTableModel(String... title) {
        //declare variable index
        int index = 0;

        //To know how many parameters
        for (String t : title) {
            index++;
        }

        //create String array and defaultTableModel
        String header[] = new String[index];

        //declare temporary i variable
        int i = 0;
        for (String s : title) {
            for (; i < index;) {
                header[i] = s;
                break;
            }
            i++;
        }
        DefaultTableModel tblModel = new DefaultTableModel(header, 0);
        return tblModel;
    }

    public static JComboBox createComboBox(Connection conn, PreparedStatement st, ResultSet rs, DefaultComboBoxModel cmbModel, JComboBox cmbCondition, String sql, String itemToDisplay) {
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            Vector data = new Vector();

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
    
    public static JList createJList(Connection conn, PreparedStatement st, ResultSet rs, DefaultListModel cmbModel, JList cmbCondition, String sql, String itemToDisplay) {
        cmbModel = new DefaultListModel();
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            

            while (rs.next()) {
                String name = rs.getString(itemToDisplay);
                cmbModel.addElement(name);
            }

            
            cmbCondition.setModel(cmbModel);

        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cmbCondition;
    }
    
    public static JList createJList(CachedRowSet crs1,String stringToDisplay,DefaultListModel dlModel,JList jList1){
        try {
            Vector data3 = null;
            crs1.beforeFirst();
            while (crs1.next()) //go through each row that your query returns
            {
                data3 = new Vector();
                data3.add(crs1.getString(stringToDisplay));
                dlModel.addElement(crs1.getString(stringToDisplay));
            }

            jList1.setModel(dlModel);
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jList1;
    }
    

    public static JTable cachedRowSet_createJTableWith_2Column(CachedRowSet crs1,DefaultTableModel tblModel1,JTable jTable1,String string1,String string2) throws SQLException{
            Vector data = null;
            crs1.beforeFirst();
            while (crs1.next()) {
                data = new Vector();
                data.add(crs1.getString(string1));
                data.add(crs1.getString(string2));
                
                tblModel1.addRow(data);
            }
            
            jTable1.setModel(tblModel1);
            
            return jTable1;
    }
    
    public static JTable cachedRowSet_createJTableWith_3Column(CachedRowSet crs1,DefaultTableModel tblModel1,JTable jTable1,String string1,String string2,String string3) throws SQLException{
            Vector data = null;
            crs1.beforeFirst();
            while (crs1.next()) {
                data = new Vector();
                data.add(crs1.getString(string1));
                data.add(crs1.getString(string2));
                data.add(crs1.getString(string3));
                tblModel1.addRow(data);
            }
            
            jTable1.setModel(tblModel1);
            
            return jTable1;
    }
    
    public static JTable cachedRowSet_createJTableWith_4Column(CachedRowSet crs1,DefaultTableModel tblModel1,JTable jTable1,String string1,String string2,String string3,String string4) throws SQLException{
            Vector data = null;
            crs1.beforeFirst();
            while (crs1.next()) {
                data = new Vector();
                data.add(crs1.getString(string1));
                data.add(crs1.getString(string2));
                data.add(crs1.getString(string3));
                data.add(crs1.getString(string4));
                tblModel1.addRow(data);
            }
            
            jTable1.setModel(tblModel1);
            
            return jTable1;
    }
    public static JTable cachedRowSet_createJTableWith_5Column(CachedRowSet crs1,DefaultTableModel tblModel1,JTable jTable1,String string1,String string2,String string3,String string4,String string5) throws SQLException{
            Vector data = null;
            crs1.beforeFirst();
            while (crs1.next()) {
                data = new Vector();
                data.add(crs1.getString(string1));
                data.add(crs1.getString(string2));
                data.add(crs1.getString(string3));
                data.add(crs1.getString(string4));
                data.add(crs1.getString(string5));
                tblModel1.addRow(data);
            }
            
            jTable1.setModel(tblModel1);
            
            return jTable1;
    }
    public static JTable cachedRowSet_createJTableWith_8Column(CachedRowSet crs1,DefaultTableModel tblModel1,JTable jTable1,String string1,String string2,String string3,String string4,String string5,String string6,String string7,String string8) throws SQLException{
            Vector data = null;
            crs1.beforeFirst();
            while (crs1.next()) {
                data = new Vector();
                data.add(crs1.getInt(string1));
                data.add(crs1.getInt(string2));
                data.add(crs1.getString(string3));
                data.add(crs1.getInt(string4));
                data.add(crs1.getFloat(string5));
                data.add(crs1.getString(string6));
                data.add(crs1.getBytes(string7));
                data.add(crs1.getBoolean(string8));
                tblModel1.addRow(data);
            }
            
            jTable1.setModel(tblModel1);
            
            return jTable1;
    }
    public static JTable selectQueryWithWhere(String selectSql, int title, String sql, Connection conn, PreparedStatement pst, ResultSet rs, DefaultTableModel tblModel, JFrame parent, JTable jTable1, JTextField txt) {

        try {
            // Tạo đối tượng thực thi câu lệnh Select
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            Vector data = null;
            tblModel.setRowCount(0);

            // Nếu item không tồn tại
            if (rs.isBeforeFirst() == false) {
                JOptionPane.showMessageDialog(parent, "The book is not available!");
                txt.setText("");
                txt.requestFocus();
            }
            String[] tableTitle = returnColumnName(title, conn, selectSql);

            // Trong khi chưa hết dữ liệu
            while (rs.next()) {
                data = new Vector();
                for (int i = 0; i < title; i++) {
                    data.add(rs.getString(tableTitle[i]));

                }
                // Thêm một dòng vào table model
                tblModel.addRow(data);
            }

            jTable1.setModel(tblModel); // Thêm dữ liệu vào table
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jTable1;

    }

    public static JTable selectQuery(String sql, int title, Connection conn, PreparedStatement pst, ResultSet rs, DefaultTableModel tblModel, JFrame parent, JTable jTable1) {

        try {
            // Tạo đối tượng thực thi câu lệnh Select
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            Vector data = null;
            tblModel.setRowCount(0);

            String tableTitle[] = returnColumnName(title, conn, sql);

            // Trong khi chưa hết dữ liệu
            //int num = 1;
            while (rs.next()) {
                data = new Vector();
                //data.add(num);
                for (int i = 0; i < title; i++) {
                    
                    data.add(rs.getString(tableTitle[i]));
                    

                }
                // Thêm một dòng vào table model
                tblModel.addRow(data);
                //num++;
            }

            jTable1.setModel(tblModel); // Thêm dữ liệu vào table
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jTable1;

    }
    
    public static String selectCount_execute(PreparedStatement pst,Connection conn,String sql,ResultSet rs) throws SQLException{
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
            rs.next();
            return rs.getString(1);
    }

    public static void deleteQueryByPrimaryKey(Connection c, PreparedStatement ps, String sql, JFrame parent) {
        int ret = JOptionPane.showConfirmDialog(parent, "Do you want to delete?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ret != JOptionPane.YES_OPTION) {
            return;
        }
        try {

            ps = c.prepareStatement(sql);

            ret = ps.executeUpdate();
            if (ret != -1) {
                JOptionPane.showMessageDialog(parent, "This book has been deleted");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void eventForJTableOrJComboBoxThatChange_1TextField(Connection conn, PreparedStatement ps, ResultSet rs, String sqlex, String sql, int title, JTextField txtTitle) {
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            String[] tableTitle = returnColumnName(title, conn, sqlex);
            if (rs.next()) {
                //Hiển thị dữ liệu vào các textfield
                txtTitle.setText(rs.getString(tableTitle[1]));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void eventForJTableOrJComboBoxThatChange_2TextField(Connection conn, PreparedStatement ps, ResultSet rs, String sqlex, String sql, int title, JTextField txtTitle1, JTextField txtTitle2) {
        try {
            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            String[] tableTitle = returnColumnName(title, conn, sqlex);
            if (rs.next()) {
                //Hiển thị dữ liệu vào các textfield
                txtTitle1.setText(rs.getString(tableTitle[1]));
                txtTitle2.setText(rs.getString(tableTitle[2]));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateQueryWith_1TextField(Connection conn, PreparedStatement ps, String updatesql, JFrame parent) {
        try {
            int ret = JOptionPane.showConfirmDialog(parent, "Do you want to update?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ret != JOptionPane.YES_OPTION) {
                return;
            }
            ps = conn.prepareStatement(updatesql);

            ret = ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void insertQuery(JFrame parent, String sql, Connection conn, PreparedStatement ps) {
        try {
            int ret = JOptionPane.showConfirmDialog(parent, "Do you want to save?", "Confirm", JOptionPane.YES_NO_OPTION);

            // Trường hợp không lưu
            if (ret != JOptionPane.YES_OPTION) {
                return; // Thoát khỏi phương thức
            }

            // Câu lệnh insert
            String insert = sql;
            ps = conn.prepareStatement(insert);
            ret = ps.executeUpdate();

            if (ret != -1) {
                JOptionPane.showMessageDialog(parent, "The book has been inserted");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String CallableStateMentWith_1In_1IntergerOut(CallableStatement stmt, Connection conn, String input, String procedure) {
        int number = 0;
        try {

            //Execute a query
            String sql = "{call " + procedure + " (?, ?)}";
            stmt = conn.prepareCall(sql);
            stmt.setString(1, input);

            // Because second parameter is OUT so register it
            stmt.registerOutParameter(2, java.sql.Types.INTEGER);

            //Use execute method to run stored procedure.
            stmt.execute();
            number = stmt.getInt(2);

        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new Integer(number).toString();
    }

    public static String[] returnColumnName(int title, Connection conn, String selectSql) {
        String tableTitle[] = new String[title];
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

    public static void cacheRowSet_Update4Field(CachedRowSet crs, String string1, String string2, String string3, String string4) {
        try {
            crs.updateString(1, string1);
            crs.updateString(2, string2);
            crs.updateString(3, string3);
            crs.updateString(4, string4);

            crs.updateRow();
            crs.acceptChanges(); //submit
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cacheRowSet_Update3Field(CachedRowSet crs, String string1, String string2, String string3) {
        try {
            crs.updateString(1, string1);
            crs.updateString(2, string2);
            crs.updateString(3, string3);

            crs.updateRow();
            crs.acceptChanges(); //submit
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cacheRowSet_Update2Field(CachedRowSet crs, String string1, String string2) {
        try {
            crs.updateString(1, string1);
            crs.updateString(2, string2);

            crs.updateRow();
            crs.acceptChanges(); //submit
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cacheRowSet_Update1Field(CachedRowSet crs, String string1) {
        try {
            crs.updateString(1, string1);

            crs.updateRow();
            crs.acceptChanges(); //submit
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cacheRowSet_Insert4Field(CachedRowSet crs,String string1,String string2,String string3,String string4) {
        try {
            crs.moveToInsertRow();
            crs.updateString(1, string1);
            crs.updateString(2, string2);
            crs.updateString(3, string3);
            crs.updateString(4, string4);
            
            crs.insertRow();
            crs.moveToCurrentRow();
            crs.acceptChanges(); //submit
        } catch (SQLException ex) {
//            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void cacheRowSet_Insert3Field(CachedRowSet crs,String string1,String string2,String string3) {
        try {
            crs.moveToInsertRow();
            crs.updateString(1, string1);
            crs.updateString(2, string2);
            crs.updateString(3, string3);
            
            
            crs.insertRow();
            crs.moveToCurrentRow();
            crs.acceptChanges(); //submit
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void cacheRowSet_Insert2Field(CachedRowSet crs,String string1,String string2) {
        try {
            crs.moveToInsertRow();
            crs.updateString(1, string1);
            crs.updateString(2, string2);
            
            
            crs.insertRow();
            crs.moveToCurrentRow();
            crs.acceptChanges(); //submit
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void cacheRowSet_Insert1Field(CachedRowSet crs,String string1) {
        try {
            crs.moveToInsertRow();
            crs.updateString(1, string1);
            
            
            crs.insertRow();
            crs.moveToCurrentRow();
            crs.acceptChanges(); //submit
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void cacheRowSet_DeleteRow(CachedRowSet crs){
        try {
            crs.deleteRow();
            crs.acceptChanges();
            
        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void nextButton(JButton jFirst,JButton jNext,JButton jPrevious,JButton jLast,CachedRowSet crs) throws SQLException{
            jFirst.setEnabled(true);
            jNext.setEnabled(crs.getRow() == crs.size() ? false : true);
            jPrevious.setEnabled(true);
            jLast.setEnabled(crs.getRow() == crs.size() ? false : true);
    }
    
    public static void previousButton(JButton jFirst,JButton jNext,JButton jPrevious,JButton jLast,CachedRowSet crs) throws SQLException{
            jFirst.setEnabled(crs.getRow() == 1 ? false : true);
            jNext.setEnabled(true);
            jPrevious.setEnabled(crs.getRow() == 1 ? false : true);
            jLast.setEnabled(true);
    }
    
    public static void lastButton(JButton jFirst,JButton jNext,JButton jPrevious,JButton jLast,CachedRowSet crs) throws SQLException{
            jFirst.setEnabled(true);
            jNext.setEnabled(false);
            jPrevious.setEnabled(true);
            jLast.setEnabled(false);
    }
    
    public static void firstButton(JButton jFirst,JButton jNext,JButton jPrevious,JButton jLast,CachedRowSet crs) throws SQLException{
            jFirst.setEnabled(false);
            jNext.setEnabled(true);
            jPrevious.setEnabled(false);
            jLast.setEnabled(true);
			
    }
    
    public static boolean checkBlankTextField(JTextComponent...com){
        
        for(JTextComponent t : com){
        if(t.getText().isEmpty()){
          t.requestFocus();
        return true;
        }
    }
        return false;
    }
    
    public static boolean checkBlankRadioButton(JRadioButton...r){
        int count = 0;
        
        //To know how many Jradiobutton
        for(JRadioButton jr : r){
            count++;
        }
        for(JRadioButton jr : r){
            if(!jr.isSelected())
                count--;
        }
        if(count==0) return true;
        return false;
    }
    
    public static void enableComponent(Component...com){
        for (Component t : com) {
            t.setEnabled(true);
        }
        
    }
    
    public static void disableComponent(Component...com){
        for (Component t : com) {
            t.setEnabled(false);
        }
    }
    
    public static void editableTextComponent(JTextComponent...com){
        for (JTextComponent t : com) {
           t.setEditable(true);
        }
    }
    public static void non_editableTextComponent(JTextComponent...com){
        for (JTextComponent t : com) {
           t.setEditable(false);
        }
    }
    
    public static void resetTextComponent(JTextComponent...com){
        for (JTextComponent t : com) {
            t.setText("");
        }
    }
    
    

    public static String returnStringWhenClicked_1RowInJTable(JTable jTable1) {
        int row = jTable1.getSelectedRow();
        String id = (String) jTable1.getValueAt(row, 0);
        return id;
    }

    public static boolean checkIfRecordExists(String sql, Connection conn, PreparedStatement pst, ResultSet rs) {
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == 0) {
                    return false;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(FrameWork.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public static void main(String[] args) {
    }

}
