/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author avianey
 */
public class Util {
    
    public static Date StringToDate(String dateS) 
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return format.parse(dateS);
        }
        catch(ParseException e)
        {
            return new Date();
        }
    }
    
    public static String DateToString (Date date)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");  
        return dateFormat.format(date);  
    }
    
}
