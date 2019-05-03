/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
public class DBConnection {
    public static Connection Connection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException{
        String dbURL  = "jdbc:derby://localhost:1527/tpsmart;create=true;user=tpsmart;password=tpsmart";
        Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
        Connection DBconn = DriverManager.getConnection(dbURL);
        return DBconn;
    }
    /*public static int CreateTableUser(Connection conn) throws SQLException{
        Statement stmt = conn.createStatement();
        String sql = "create table user(id_user int(255),firstName varchar(50),Age int(100),Address varchar(50),Pwd varchar(50))";
        int result = stmt.executeUpdate(sql);
        return result;
    }*/
    public static int Insert(Connection conn,String email,String pseudo,String password) throws SQLException{
        String value="'"+email+"','"+pseudo+"','"+password+"'";
        String sql = "insert into utilisateurs(email,pseudo,password) values("+value+")";
        Statement stmt = conn.createStatement();
        int result = stmt.executeUpdate(sql);
        return result;
    } 
    /*public static void Delete(Connection conn,String email,String pwd) throws SQLException{
        String sql = "delete * from account where Email = ? and Pwd= ?";
        PreparedStatement stmt = conn.prepareStatement(sql);    
        stmt.setString(1, email); 
        stmt.setString(2, pwd); 
        ResultSet rs = stmt.executeQuery();
    }*/ 
    public static boolean Connect(String email,String password,Connection conn) throws SQLException{
        String sql="select * from utilisateurs where email = ? and password= ? ";
        PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);    
        stmt.setString(1, email); 
        stmt.setString(2, password); 
        ResultSet rs = stmt.executeQuery();
        int size =0;
        if (rs != null) 
        {
          rs.last();    // moves cursor to the last row
          size = rs.getRow(); // get row id 
        } 
        if (size==0){
            return false;
        }
        else{
            return true;   
        }
    }
    
    public static ResultSet FindUserWithPseudo(String pseudo,Connection conn) throws SQLException{
        String sql="select * from utilisateurs where pseudo = ?";
        PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt.setString(1, pseudo);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
    public static ResultSet FindUserWithEmail(String email,Connection conn) throws SQLException{
        String sql="select * from utilisateurs where email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
    public static boolean UserExist(String email,String pseudo,Connection conn) throws SQLException{
        ResultSet rs1=FindUserWithEmail(email,conn);
        ResultSet rs2=FindUserWithPseudo(pseudo,conn);
        int size1=0;
        int size2=0;
        if (rs1 != null) 
        {
          rs1.last();    // moves cursor to the last row
          size1 = rs1.getRow(); // get row id 
        } 
        if (rs2 != null) 
        {
          rs2.last();    // moves cursor to the last row
          size2 = rs2.getRow(); // get row id 
        } 
        return size1+size2 != 0;
    } 
}    	