package main.model;

import database.DBProvider;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Hoang
 */
public class Login implements Serializable {
    
    // Some constants in database
    public static final String FG_USER = "User";
    public static final String FG_PERMISSION = "Permission";
    public static final String FG_PRODUCT = "Product";
    public static final String FG_BRANCH = "Branch";
    public static final String FG_INBOUND = "Inbound";
    public static final String FG_OUTBOUND = "Outbound";
    public static final String FG_SUPPLIER = "Supplier";
    public static final String FG_ORDER= "Order";
    public static final String FG_SALESOFF = "SalesOff";
    public static final String FG_CUSTOMER = "Customer";
    public static final String FG_CUSTOMERLEVEL = "CustomerLevel";
    public static final String FG_SERVICE = "Service";
    public static final String FG_EMPLOYEE = "Employee";    //13 features
    public static final String FN_VIEW = "View";
    public static final String FN_UPDATE = "Update";
    
    // Khai bao cac tri default
    public static final String HOST = DBProvider.HOST;
    public static final String PORT = DBProvider.PORT;
    public static final String DBNAME = DBProvider.DBNAME;
    public static final String NAME = DBProvider.NAME;
    public static final String PASSWORD = DBProvider.PASSWORD;
    public static final String USER_NAME = "root";
    public static final String USER_PASSWORD = "1";
    public static final List<UserFunction> USER_FUNCTIONS = Arrays.asList(
            new UserFunction(FG_USER, FN_VIEW),
            new UserFunction(FG_USER, FN_UPDATE),
            new UserFunction(FG_PERMISSION, FN_VIEW),
            new UserFunction(FG_PERMISSION, FN_UPDATE),
            new UserFunction(FG_PRODUCT, FN_VIEW),
            new UserFunction(FG_PRODUCT, FN_UPDATE),
            new UserFunction(FG_BRANCH, FN_VIEW),
            new UserFunction(FG_BRANCH, FN_UPDATE),
            new UserFunction(FG_INBOUND, FN_VIEW),
            new UserFunction(FG_INBOUND, FN_UPDATE),
            new UserFunction(FG_OUTBOUND, FN_VIEW),
            new UserFunction(FG_OUTBOUND, FN_UPDATE),
            new UserFunction(FG_SUPPLIER, FN_VIEW),
            new UserFunction(FG_SUPPLIER, FN_UPDATE),
            new UserFunction(FG_ORDER, FN_VIEW),
            new UserFunction(FG_ORDER, FN_UPDATE),
            new UserFunction(FG_SALESOFF, FN_VIEW),
            new UserFunction(FG_SALESOFF, FN_UPDATE),
            new UserFunction(FG_CUSTOMER, FN_VIEW),
            new UserFunction(FG_CUSTOMER, FN_UPDATE),
            new UserFunction(FG_CUSTOMERLEVEL, FN_VIEW),
            new UserFunction(FG_CUSTOMERLEVEL, FN_UPDATE),
            new UserFunction(FG_SERVICE, FN_VIEW),
            new UserFunction(FG_SERVICE, FN_UPDATE),
            new UserFunction(FG_EMPLOYEE, FN_VIEW),
            new UserFunction(FG_EMPLOYEE, FN_UPDATE)
            );

    public String host;
    public String port;
    public String DBName;
    public String name;
    public String password;
    public String userName;
    public String userPassword;
    public List<UserFunction> userFunctions;

    public Login() {
    }

    public Login(String host, String port, String DBName, String name, String password, String userName, String userPassword, List<UserFunction> userFunctions) {
        this.host = host;
        this.port = port;
        this.DBName = DBName;
        this.name = name;
        this.password = password;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userFunctions = userFunctions;
    }

    @Override
    public String toString() {
        return "Login{" + "host=" + host + ", port=" + port + ", DBName=" + DBName + ", name=" + name + ", password=" + password + ", userName=" + userName + ", userPassword=" + userPassword + ", userFunctions=" + userFunctions + '}';
    }
}
