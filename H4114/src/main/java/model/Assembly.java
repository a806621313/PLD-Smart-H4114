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
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Id;

/**
 *
 * @author avianey
 */
public class Assembly {

    @Id
    private Integer id;
    private Survey currentSurvey;
    private String colour;
    private String title;
    private String place;
    private Date date;
    private Time time;
    private String description;
    private int radio;
    private long latitiude;
    private long longitude;

    public Assembly(Integer id, String title, String description, String place, String date, String radio) throws ParseException {
        this.id = id;
        this.currentSurvey = null;
        this.colour = "red";
        this.radio = Integer.parseInt(radio);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        this.date = format.parse(date);

        this.title = title;
        this.description = description;

        this.place = place;
        // this.latitiude = getlat

    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }

    public long getLatitiude() {
        return latitiude;
    }

    public long getLongitude() {
        return longitude;
    }

    public Survey getCurrentSurvey() {
        return currentSurvey;
    }

    public void addSurvey(Survey survey) {
        if (currentSurvey != null || currentSurvey.stat == 2) {
            currentSurvey = survey;
        }
    }

    public void startSurvey() {
        if (currentSurvey != null && currentSurvey.stat == 1) {
            currentSurvey.start();
        }
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCurrentSurvey(Survey currentSurvey) {
        this.currentSurvey = currentSurvey;
    }

    public static Assembly getAssembly(Connection conn, String idAssembly) throws SQLException, ParseException {
        String sql = "select * from assemblies where id_assembly = ? ";
        PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt.setInt(1, Integer.parseInt(idAssembly));
        ResultSet rs = stmt.executeQuery();

        if (rs != null) {
            rs.last();
            Assembly assembly = new Assembly(
                    Integer.parseInt(idAssembly),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("place"),
                    rs.getString("date"),
                    rs.getString("radio")
            );

            String idSurvey = rs.getString("id_survey");
            Survey survey = Survey.getSurvey(conn, idSurvey);
            assembly.setCurrentSurvey(survey);

            return assembly;
        }

        return null;
    }

}
