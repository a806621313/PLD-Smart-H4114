/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.insalyon.votewithbchain;

import model.Survey;
import model.Block;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintStream;
import java.util.ArrayList;


/**
 *
 * @author avianey
 */
public class VoteWithBChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>(); 
    
    /*public static void main(String[] args) {
      Survey survey=null;
        //Survey survey = new Survey("Où manger ce midi?");
        survey.addResponseChoice("RU");
        survey.addResponseChoice("RI");
        survey.addResponseChoice("Olivier");
        survey.addResponseChoice("Prevert");
        survey.addResponseChoice("Grillon");
        
        survey.addAnswerVote("RU");
        
        startSurvey(survey);
        
        survey.addAnswerVote("RU");
        
        long t = 0;
        while (t<50)
        {
            survey.addAnswerVote("RI");
            survey.addAnswerVote("RI");
            survey.addAnswerVote("Olivier");
            survey.addAnswerVote("Olivier");
            survey.addAnswerVote("Prevert");
            survey.addAnswerVote("Prevert");
            survey.addAnswerVote("Grillon");
            survey.addAnswerVote("Grillon");
            t++;
        }
        
     // sondage.showResults (System.out);
        
     survey.showClearResults (System.out);
        
        
    
        
    }*/
    
    public static void startSurvey(Survey survey)
    {
        survey.start();
    }
    
    public static void sendQuestionAndAnswers(PrintStream stream, Survey survey)
    {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        final String json = gson.toJson(survey);
        
    }
    
    public static void setVote(PrintStream stream, Survey survey, String response)
    {
        stream.println(survey.addAnswerVote(response));
        
    }
    
    public static void recapVote(PrintStream stream, Survey survey, String address)
    {
        survey.getVote(stream, address);
       
    }
    
    public static void sendClearResult(PrintStream stream, Survey survey)
    {
         survey.showClearResults(stream);
    }
    
    public static void addContestation(Survey survey)
    {
         survey.addContestation();
    }
    
    
   
    
   
    
    
    
}
