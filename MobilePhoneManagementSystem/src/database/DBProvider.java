package database;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;

public class DBProvider {

    private final String dbType = "jdbc:sqlserver://";
    private String dbHost, dbPort, dbName, dbUsername, dbPassword;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private CallableStatement callableStatement;
    private ResultSet resultSet;
    
    // Define some string sentences
    public static final String INSERT_SUCCESS = "Added new successfully!";
    public static final String INSERT_FAIL = "Cannot add new due to some reasons!";
    public static final String UPDATE_SUCCESS = "Updated successfully!";
    public static final String UPDATE_FAIL = "Cannot update due to some reasons!";
    public static final String DELETE_SUCCESS = "Deleted successfully!";
    public static final String DELETE_FAIL = "Cannot delete due to some reasons!";
    public static final String DB_REFRESH = "Data have been refreshed!";

    public static void main(String[] args) throws SQLException {
        // Dung de test ket noi db
        // Buoc 1: Khoi tao DBProvider instance va set DB URL, DB Name,
        // DB Username, DB Password (neu co thay doi) va start db
        DBProvider db = new DBProvider();
        db.start();

        // Buoc 2: Get ket qua (result set hoac prepared st hoac callable st
        // hoac cached rowset) va xu ly ket qua
//        ResultSet rs = db.getResultSet("select * from CustomerLevels");
//        rs.next();
        // Xu ly ket qua ...
        // Buoc 3: Stop db util
        // db.stop();
//        System.out.println(rs.getString(3));
//		PreparedStatement ps = db.getPreparedStatement("update class set class_name=? where class_id=?");
//		ps.setString(1, "ten class moi sua");
//		ps.setString(2, "3343422");
//		ps.executeUpdate();
//
//		CallableStatement cs = db.getCallableStatement("{?=call sp_CheckExist(?)}");
//		cs.registerOutParameter(1, Types.INTEGER);
//		cs.setString(2, "F51508S1");
//		cs.execute();
//		System.out.println(cs.getInt(1));
//		
//		db.stop();
//		
//		CachedRowSet crs = DBProvider.getCRS("select student_id from student where student_id=?");
//		crs.setString(1, "SV004");
//		crs.execute();
//		crs.first();
//		System.out.println(crs.getString(1));
    }

    public DBProvider() {
        dbHost = "10.211.55.7";
        dbPort = "1433";
        dbName = "Mobile";
        dbUsername = "sa";
        dbPassword = "111";
    }

    public DBProvider(String dbHost, String dbPort, String dbName, String dbUsername, String dbPassword) {
        this.dbHost = dbHost;
        this.dbPort = (dbPort == null || dbPort == "") ? "1433" : dbPort;
        this.dbName = dbName;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    public boolean start() {
        boolean result = false;
        try {
//            System.out.println(getDbUrl());
            connection = DriverManager.getConnection(getDbUrl(), dbUsername, dbPassword);
//            connection = DriverManager.getConnection("jdbc:sqlserver://10.211.55.7:1433;DatabaseName=", "sa", "111");
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void stop() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (callableStatement != null) {
                callableStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDbUrl() {
        return dbType + dbHost + ":" + dbPort + ";DatabaseName=" + dbName;
    }
    
    public String getFullDbUrl() {
        return dbType + dbHost + ":" + dbPort + ";DatabaseName=" + dbName + ";UID=" + dbUsername + ";Password=" + dbPassword;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet getResultSet(String query) {
        try {
            resultSet = connection.createStatement().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public PreparedStatement getPreparedStatement(String query) {
        try {
            preparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preparedStatement;
    }

    public CallableStatement getCallableStatement(String query) {
        try {
            callableStatement = connection.prepareCall(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return callableStatement;
    }

    public CachedRowSet getCRS(String query) {
        CachedRowSet cachedRowSet = null;
        try {
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet.setUrl(getDbUrl());
            cachedRowSet.setUsername(dbUsername);
            cachedRowSet.setPassword(dbPassword);
            cachedRowSet.setCommand(query);
            if (!query.matches(".*\\?.*")) {
                cachedRowSet.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cachedRowSet;
    }
}
