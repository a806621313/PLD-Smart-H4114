/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.persistence.Id;

/**
 *
 * @author avianey
 */
public class Participant {

    @Id
    private Integer id;
    User user;
    Assembly assembly;
    int status;
    double longitude;
    double latitude;

    public Participant(User user, Assembly assembly, double latitude, double longitude, int status) {
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.assembly = assembly;
        this.user = user;
        

    }

    public User getUser() {
        return user;
    }

    public Assembly getAssembly() {
        return assembly;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    
    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.addProperty("id", this.id);
        json.addProperty("status", this.status);
        json.addProperty("pseudo", this.user.getPseudo());
        json.addProperty("assembly", this.assembly.getId());
        json.addProperty("title", this.assembly.getTitle());
        json.addProperty("latitude", this.latitude);
        json.addProperty("longitude", this.longitude);
        return json;
    }
    
    

    public static boolean Insert(Connection conn, Participant participant) throws SQLException {
        System.out.println(participant.getUser().getId());
        //String value="'"+email+"','"+pseudo+"','"+password+"'";
        //String sql = "insert into participants(idUser,idAssembly,title,description,adresse,date, time)) values(?,?,?,?,?,?,?)";
        String sql = "insert into participants(id_user,id_assembly,status,latitude,longitude) values(?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
        preparedStatement.setInt(1, participant.getUser().getId());
        preparedStatement.setInt(2, participant.getAssembly().getId());
        preparedStatement.setInt(3, participant.getStatus());
        preparedStatement.setDouble(4, participant.getLatitude());
        preparedStatement.setDouble(5, participant.getLongitude());

        int flag = preparedStatement.executeUpdate();

        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                participant.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

        if (flag != -1) {
            return true;
        } else {
            return false;
        }
    }
    
    public static ArrayList<Participant> GetParticipants(Connection conn) throws SQLException {
        //String value="'"+email+"','"+pseudo+"','"+password+"'";
        //String sql = "insert into participants(idUser,idAssembly,title,description,adresse,date, time)) values(?,?,?,?,?,?,?)";
        ArrayList<Participant> participants = new ArrayList<>();
        String sql="select * from participants";
        PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);            
        ResultSet rs = stmt.executeQuery();

        while(rs.next())
        {
            Integer idP = rs.getInt("id_participant");
            Integer idU =  rs.getInt("id_user");
            User user = User.getUser(conn, idU);
            Integer idA = rs.getInt("id_assembly");
            Assembly assembly = Assembly.getAssembly(conn, idU);
            int status =  rs.getInt("status");
            double latitude =  rs.getDouble("latitude");
            double longitude =  rs.getDouble("longitude");
            
            Participant participant = new Participant(user, assembly, latitude, longitude, status);
            participants.add(participant);
        }

        return participants;
    }

    public static boolean Remove(Connection conn, String id) throws SQLException {
        return true;
    }

}
