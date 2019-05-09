/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.persistence.Id;

/**
 *
 * @author avianey
 */
public class User {

    
    @Id
    private Integer id;
    private String email;
    private String password;
    private String pseudo;
    
    public User(String email,String password,String pseudo)
    {
        this.email = email;
        this.password = password;
        this.pseudo = pseudo;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public static boolean Insert(Connection conn, User user) throws SQLException{
            //String value="'"+email+"','"+pseudo+"','"+password+"'";
            String sql = "insert into users(email,pseudo,password) values(?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS);   
            preparedStatement.setString(1, user.getEmail()); 
            preparedStatement.setString(2, user.getPseudo()); 
            preparedStatement.setString(3, user.getPassword());
            int flag=preparedStatement.executeUpdate();
            System.out.println("flag="+flag);
            
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
            
            if (flag!=-1){return true;}
            else{return false;}
        }  
    public static User Connect(Connection conn, String email,String password) throws SQLException{
        String sql="select * from users where email = ? and password= ? ";
        PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);    
        stmt.setString(1, email); 
        stmt.setString(2, password); 
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) 
        {   
            User user = new User( 
                  rs.getString("email"),
                  rs.getString("password"),
                  rs.getString("pseudo")
          );
            user.setId(rs.getInt("id_user"));
            
            return user;  
        } 
        
        return null;
    }
    
    public static User getUser(Connection conn, Integer idU) throws SQLException 
    {
            String sql="select * from users where id_user = ? ";
            PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);    
            stmt.setInt(1, idU); 
            ResultSet rs = stmt.executeQuery();

            if (rs != null) 
            {
                rs.last();    
                User user = new User( 
                      rs.getString("email"),
                      rs.getString("password"),
                      rs.getString("pseudo") 
                );
                
                user.setId(idU);

                return user;  
            } 
        return null;
    }
    
    
    public static boolean IsPartAssembly(Connection conn, User user) throws SQLException{
        String sql="select * from participants where id_user = ?";
        PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt.setInt(1, user.getId());
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    } 
    
    public static ResultSet FindUserWithPseudo(Connection conn, String pseudo) throws SQLException{
        String sql="select * from users where pseudo = ?";
        PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt.setString(1, pseudo);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
    
    public static ResultSet FindUserWithEmail(Connection conn, String email) throws SQLException{
        String sql="select * from users where email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
    
    public static boolean UserExist(Connection conn, String email,String pseudo) throws SQLException{
        ResultSet rs1=FindUserWithEmail(conn, email);
        ResultSet rs2=FindUserWithPseudo(conn, pseudo);
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
