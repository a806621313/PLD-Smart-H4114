/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
public class User {
    public static boolean Insert(Connection conn,String email,String pseudo,String password) throws SQLException{
        //String value="'"+email+"','"+pseudo+"','"+password+"'";
        String sql = "insert into utilisateurs(email,pseudo,password) values(?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);   
        preparedStatement.setString(1, email); 
        preparedStatement.setString(2, pseudo); 
        preparedStatement.setString(3, password);
        int flag=preparedStatement.executeUpdate();
        System.out.println("flag="+flag);
        if (flag!=-1){return true;}
        else{return false;}
    }  
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