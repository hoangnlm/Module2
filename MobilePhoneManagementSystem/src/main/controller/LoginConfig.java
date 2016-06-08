/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.controller;

import java.io.Serializable;

/**
 *
 * @author Hoang
 */
public class LoginConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    // Khai bao cac tri default
    public static final String HOST = "10.211.55.7";
    public static final String PORT = "1433";
    public static final String DBNAME = "Mobile";
    public static final String NAME = "sa";
    public static final String PASSWORD = "111";
    public static final String USER_NAME = "admin";
    public static final String USER_PASSWORD = "admin";

    public String host;
    public String port;
    public String DBName;
    public String name;
    public String password;
    public String userName;
    public String userPassword;

    public LoginConfig() {
    }

    public LoginConfig(String host, String port, String DBName, String name, String password, String userName, String userPassword) {
        this.host = host;
        this.port = port;
        this.DBName = DBName;
        this.name = name;
        this.password = password;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "[" + host + ", " + port + ", " + DBName + ", " + name + ", " + password + ", " + userName + ", " + userPassword + "]";
    }

}
