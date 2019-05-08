/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
public class DBConnection {
    public static Connection Connection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException{
        String dbURL  = "jdbc:derby://localhost:1527/tpsmart;create=true;user=tpsmart;password=tpsmart";
        Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        Connection DBconn = DriverManager.getConnection(dbURL);
        return DBconn;
    } 
}    	