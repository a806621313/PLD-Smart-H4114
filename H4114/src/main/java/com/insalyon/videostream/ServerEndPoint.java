/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.insalyon.videostream;
import java.util.logging.Logger;
 
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.server.PathParam;
import org.json.JSONObject;
import model.Participant;
/**
 *
 * @author scheah
 */

@ServerEndpoint(value = "/video/{room}/{usertype}/{username}")
public class ServerEndPoint {
    private Participant participant;
    private Session session;
    private String roomNumber;
    private String name;
    private String userType;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private static HashMap<String, Set<ServerEndPoint> > serverEndPoints = new HashMap<String, Set<ServerEndPoint> >();
    private static HashMap<String, ServerEndPoint> serverEndPointStart = new HashMap<String, ServerEndPoint>();
    private static HashMap<String, Boolean> serverEndPointState = new HashMap<String, Boolean>();
    private static HashMap<String, String> serverEndPointRoomKey = new HashMap<String, String>();
    private static int roomIndex = 1;
    
    public static HashMap<String, Boolean> getServerEndPointState(){
        return serverEndPointState;
    }
    
    public static HashMap<String, String> getServerEndPointRoomKey(){
        return serverEndPointRoomKey;
    }
    
    public static HashMap<String, Set<ServerEndPoint> > getServerEndPoints(){
        return serverEndPoints;
    }
    
    public static HashMap<String, ServerEndPoint> getServerEndPointStart(){
        return serverEndPointStart;
    }
    
    public String getName(){
        return name;
    }
    
    public static int getNumber(){
        return roomIndex++;
    }
 
    @OnOpen
    public void onOpen(Session session, @PathParam("usertype") String usertype, @PathParam("room") String roomNumber, @PathParam("username") String name) throws IOException {
        System.out.println("Connected ... " + session.getId());
        logger.info("Connected ... " + session.getId());
        this.session = session;
        this.roomNumber = roomNumber;
        this.name = name;
        this.userType = usertype;
        if (usertype.equals("start")) {
            serverEndPoints.put(roomNumber, new CopyOnWriteArraySet<ServerEndPoint> ());
            serverEndPointStart.put(roomNumber, this);
            serverEndPointState.put(roomNumber, Boolean.FALSE);
        }
        else {
            if(serverEndPoints.get(roomNumber).size() < 10){
                serverEndPoints.get(roomNumber).add(this);
                if(serverEndPoints.get(roomNumber).size() == 9){
                    serverEndPointState.replace(roomNumber, Boolean.TRUE);
                }
                JSONObject json  = new JSONObject();
                json.put("type", "join");
                json.put("name", this.name);
                sendToStart(json.toString());
            }
            
        }
    }
 
    @OnMessage
    public String onMessage(String message, Session session) throws IOException {
        JSONObject json = new JSONObject(message);
        if (json != null) {
            String userType = json.getString("user");
            String type = json.getString("type");
            if ("bye".equals(type)) {
                System.out.println("user :" + session.getId() + " " + userType + " exit..");
                if(userType.equals("start")){
                    serverEndPoints.remove(roomNumber);
                    serverEndPointState.remove(roomNumber);
                    serverEndPoints.remove(roomNumber);
                }else{
                    serverEndPoints.get(roomNumber).remove(this);
                    if(Objects.equals(serverEndPointState.get(roomNumber), Boolean.TRUE)){
                        serverEndPointState.replace(roomNumber, Boolean.FALSE);
                    }
                }
            }else if ("offer".equals(type)){
                if(userType.equals("start")){
                    String dest = json.getString("name");
                    sendToDest(message,dest);
                    //broadcast(message);
                }else{
                    System.err.println("Wrong userType sent offer ");
                }
            }else if ("answer".equals(type)){
                if(userType.equals("listen")){
                    sendToStart(message);
                }else{
                    System.err.println("Wrong userType sent answer ");
                }
            }else if ("candidate".equals(type)){
                System.out.println(json);
                if(userType.equals("start")){
                    String dest = json.getString("name");
                    sendToDest(message,dest);
                    //broadcast(message);
                }else{
                    sendToStart(message);
                }
            }else if ("alive".equals(type)){
                System.out.println(session.getId()+" is alive.");
            }
        } 
        return message;
    }
 
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
        if (this.userType.equals("start")) {
            serverEndPoints.remove(roomNumber);
            serverEndPointState.remove(roomNumber);
            serverEndPoints.remove(roomNumber);
        }else{
            serverEndPoints.get(roomNumber).remove(this);
            if(Objects.equals(serverEndPointState.get(roomNumber), Boolean.TRUE)){
                serverEndPointState.replace(roomNumber, Boolean.FALSE);
            }
        }
    }
    
    private void broadcast(String message) throws IOException {
        for(ServerEndPoint endpoint : serverEndPoints.get(this.roomNumber)) {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void sendToDest(String message, String dest) throws IOException {
        for(ServerEndPoint endpoint : serverEndPoints.get(this.roomNumber)) {
            synchronized (endpoint) {
                try {
                    if(dest.equals(endpoint.name)){
                        endpoint.session.getBasicRemote().sendText(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void sendToStart(String message) throws IOException {
        ServerEndPoint endpoint = serverEndPointStart.get(this.roomNumber);
        try {
            endpoint.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}