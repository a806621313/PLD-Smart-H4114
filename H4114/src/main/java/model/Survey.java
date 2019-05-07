/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package model;

import model.Block;
import java.io.PrintStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.persistence.Id;

/**
 *
 * @author avianey
 */
public class Survey {

    
    
    @Id
    private Integer id;
    Assembly assembly;
    private final String question;
    private ArrayList<String> choices;
    Map<String, Integer> responses;
    PrivateKey privateKey;
    PublicKey publicKey;
    int contestation;
    long timeMillis;
    String creator;
    int stat;
    
    private ArrayList<Block> blockchain; 

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }
     public Integer getId() {
        return id;
    }
     
     public void setId(Integer id) {
       this.id = id;
    }
    
    
    public Survey(String question, String timeMillis)
    {
        this.id = id;
        this.question = question;
        this.stat = 0;
        this.timeMillis = Long.parseLong(timeMillis);
        this.choices = new ArrayList<>();
        
         try { 
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            this.publicKey = kp.getPublic();
            this.privateKey = kp.getPrivate();
            this.blockchain = new ArrayList<>();
            this.responses = new HashMap<> ();
            this.publicKey = kp.getPublic();
            
           
        }
        catch(NoSuchAlgorithmException e) 
        {
            throw new RuntimeException(e);
        }
    }
    public void start()
    {
        System.out.println("START");
        this.stat = 1;
        final Survey s = this;
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized(s)
                {
                    try
                    {
                        s.wait(timeMillis);
                        s.stat = 2;
                        s.notifyAll();
                        System.out.println("END");
                        
                    } catch (Exception e) {}
                }
            }
        });

        
        

    }
    
    public String getTimeMillis()
   {
       return Long.toString(this.timeMillis);
   }
    
   public String getChoices()
   {
       String choices = "";
       for (int i = 0; i < this.choices.size(); i++)
       {
           choices+=this.choices.get(i);
           
           if (i!=this.choices.size()-1)
           {
               choices+=";";
           }
       }
       return choices;
   }
    
    public void addResponseChoice (String response)
    {
        if (!response.isEmpty())
        {
            this.responses.put(response, 0);
            this.choices.add(response);
        }
    }
    
    public String addAnswerVote(String data)
    {
        String lastHash = "0";
        if (this.blockchain.size() > 0)
        {
            lastHash = this.blockchain.get(this.blockchain.size()-1).getHash();
        }
            
        if (this.stat == 1)
        {
            Block block = new Block(data, lastHash, this.publicKey);
            this.blockchain.add(block);
            return block.getHash();
                       
        }
        
        return lastHash;

    }
    
    public void showResults (PrintStream stream)
    {
        stream.println(this.question+ ":");
        
        for(int i=0; i < this.blockchain.size(); i++) 
        {
            stream.println("Vote " + i + " : ");
            Block block = this.blockchain.get(i);
            block.display(stream);        
        }
    }
    
    public void addContestation()
    {
        this.contestation++;
    }

    @Override
    public String toString() {
        return "Sondage{" + "question=" + question + 
                ", responses=" + responses +
                ", publicKey=" + publicKey + '}';
    }
    
    
    
    
    
    public void showClearResults (PrintStream stream)
    {
        System.out.println(this.question  + ": \n");
        
        for(int i=0; i < this.blockchain.size(); i++) 
        {
           stream.println("Vote " + i + " : ");
            Block block = this.blockchain.get(i);
            String choice =  block.decrypt(this.privateKey);
            
            if (responses.containsKey(choice))
            {
                Integer nb = responses.get(choice) + 1;
                responses.put(choice, nb);
            }
            else
            {
                Integer nb = responses.getOrDefault("", 0) + 1;
                responses.put("", nb);
                
                System.out.println("White vote");
            }
            
            stream.println("Address : " + block.getHash());
            stream.println("Vote : " + choice);
           
        } 
        
        stream.println("Bilan :" + responses.toString());
    }
    
    public void getVote(PrintStream stream, String address)
    {
        for (int i = 0; i < this.blockchain.size(); i++)
        {
           if (this.blockchain.get(i).getHash().equals(address))
           {
               stream.println(this.blockchain.get(i).decrypt(privateKey));
           }
        }
    }
    
    public void display(PrintStream stream) {
        for(int i=0; i < this.blockchain.size(); i++) 
        {
            stream.println("Hash for block " + i + " : " + this.blockchain.get(i).getHash());
            stream.println("Data for block " + i + " : " + this.blockchain.get(i).getData());
        }
    }

    public String getQuestion() {
        return question;
    }

    public Map<String, Integer> getResponses() {
        return responses;
    }

    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
    }
    
    public String getPrivateKey() {
        return Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
    }
    
    public Boolean isChainValid() {
        
	Block currentBlock; 
	Block previousBlock;
	
            for(int i=1; i < this.blockchain.size(); i++) 
            {
                currentBlock = this.blockchain.get(i);
                previousBlock = this.blockchain.get(i-1);
               
                if(!currentBlock.getHash().equals(currentBlock.calculateHash()) )
                {
                        System.out.println("Current Hashes not equal");			
                        return false;
                }

                
                if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) 
                {
                        System.out.println("Previous Hashes not equal");
                        return false;
                }
            }
            return true;
    }
    
    public static Survey getSurvey(Connection conn, String idSurvey) throws SQLException 
    {
        String sql="select * from surveys where id_survey = ? ";
        PreparedStatement stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);    
        stmt.setInt(1, Integer.parseInt(idSurvey)); 
        ResultSet rs = stmt.executeQuery();

        if (rs != null) 
        {
            rs.last();    
            Survey survey = new Survey( 
                  rs.getString("question"),
                  rs.getString("time")
            );
            
            survey.setTimeMillis(Long.parseLong(rs.getString("time")));
            
           
            String [] choices = rs.getString("choices").split(";");
            for (int i = 0; i < choices.length; i++)
            {
                survey.addResponseChoice(choices[i]);
            }

            return survey;  
        } 
        
        return null;
    }
    
    public static boolean Insert(Connection conn, Survey survey) throws SQLException{
            //String value="'"+email+"','"+pseudo+"','"+password+"'";
            String sql = "insert into surveys(question,choices,time, publicKey, privateKey) values(?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);   
            preparedStatement.setString(1, survey.getQuestion()); 
            preparedStatement.setString(2, survey.getChoices());
            preparedStatement.setString(3, survey.getTimeMillis());
            preparedStatement.setString(4, survey.getPublicKey()); 
            preparedStatement.setString(3, survey.getPrivateKey());
            
            int flag=preparedStatement.executeUpdate();
            
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                survey.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
            System.out.println("flag="+flag);
            if (flag!=-1){return true;}
            else{return false;}
        }  
     
    
    
}
