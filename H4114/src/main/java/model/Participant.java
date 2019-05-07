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
    String status;
    long longitude;
    long latitude;

    public Participant(User user, Assembly assembly, String latitude, String longitude, String status) {
        this.status = status;
        this.latitude = Long.parseLong(latitude);
        this.longitude = Long.parseLong(longitude);
        this.assembly = assembly;
        this.user = user;

    }

    public User getUser() {
        return user;
    }

    public Assembly getAssembly() {
        return assembly;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static boolean Insert(Connection conn, Participant participant) throws SQLException {
        //String value="'"+email+"','"+pseudo+"','"+password+"'";
        //String sql = "insert into participants(idUser,idAssembly,title,description,adresse,date, time)) values(?,?,?,?,?,?,?)";
        String sql = "insert into participants(idUser,idAssembly,status,lat,long)) values(?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, participant.getUser().getId().toString());
        preparedStatement.setString(2, participant.getAssembly().getId().toString());
        preparedStatement.setString(3, participant.getStatus());
        preparedStatement.setString(4, Long.toString(participant.getLatitude()));
        preparedStatement.setString(5, Long.toString(participant.getLongitude()));

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

    public static boolean Remove(Connection conn, String id) throws SQLException {
        return true;
    }

}
