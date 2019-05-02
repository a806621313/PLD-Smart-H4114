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
    public static Connection Connection() throws ClassNotFoundException, SQLException{
        String url = "jdbc:mysql://localhost:3306/tpsmart?"
                + "user=tpsmart&password=tpsmart&useUnicode=true&characterEncoding=UTF8";
         Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
         Connection conn = DriverManager.getConnection(url);
         return conn;
    }
    public static int CreateTable(Connection conn) throws SQLException{
        Statement stmt = conn.createStatement();
        String sql = "create table account(lastName varchar(50),firstName varchar(50),Age int(100),Address varchar(50),Pwd varchar(50))";
        int result = stmt.executeUpdate(sql);
        return result;
    }
    public static int Insert(Connection conn,String email,String sex,String lastName,String firstName,String age, String address,String pwd) throws SQLException{
        Statement stmt = conn.createStatement();
        String value="'"+email+"','"+sex+"','"+lastName+"','"+firstName+"','"+age+"','"+address+"','"+pwd+"'";
        String sql = "insert into account(Email,Sex,lastName,firstName,Age,Address,Pwd) values("+value+")";
        int result = stmt.executeUpdate(sql);
        return result;
    } 
    public static void Delete(Connection conn,String email,String pwd) throws SQLException{
        String sql = "delete * from account where Email = ? and Pwd= ?";
        PreparedStatement stmt = conn.prepareStatement(sql);    
        stmt.setString(1, email); 
        stmt.setString(2, pwd); 
        ResultSet rs = stmt.executeQuery();
    } 
    public static boolean Connect(String email,String pwd,Connection conn) throws SQLException{
        String sql="select * from account where Email = ? and Pwd= ? ";
        PreparedStatement stmt = conn.prepareStatement(sql);    
        stmt.setString(1, email); 
        stmt.setString(2, pwd); 
        ResultSet rs = stmt.executeQuery();
        rs.last();    
        int size = rs.getRow(); 
        if (size==0){
            return false;
        }
        else{
            rs.beforeFirst();
            //Display(rs);
            return true;   
        }
    }
    public static String ExistanceCompte(String email,Connection conn) throws SQLException{
        ResultSet rs=SearchCompteWithEmail(email,conn);
        rs.last();    
        int size = rs.getRow(); 
        rs.beforeFirst();
        if (size==0){
            return "not exist";
        }
        else{
            return "exist";   
        } 
    }
    public static ResultSet SearchCompteWithEmail(String email,Connection conn) throws SQLException{
        String sql="select * from account where Email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);    
        stmt.setString(1, email); 
        ResultSet rs = stmt.executeQuery();
        //Display(rs);
        return rs;
    }
    public static void DisplayAll(Connection conn) throws SQLException{
        Statement stmt = conn.createStatement();
        String sql = "select * from account";
        ResultSet rs = stmt.executeQuery(sql);
        //Display(rs);
    }
    public static void Display(ResultSet rs) throws SQLException{
        System.out.println("Email\tSex\tlastName\tfirstName\tAge\tAddress\tPwd");
        while (rs.next()) {
            System.out.println(rs.getString(1) + "\t" + rs.getString(2)+"\t" + rs.getString(3)+"\t" + rs.getString(4)+"\t" + rs.getString(5)+"\t" + rs.getString(6)+"\t" + rs.getString(7));
        }
        rs.beforeFirst();
    } 
}	