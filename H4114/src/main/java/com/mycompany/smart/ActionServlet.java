/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.smart;

import com.google.gson.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.DBConnection;

/**
 *
 * @author Arthur
 */
@WebServlet(name = "ActionServlet", urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action=request.getParameter("action"); 
        String lastName,firstName,age,address,pwd,email,sex;
        Connection conn=null;
        switch(action){
            case "connect":
                email=request.getParameter("email");
                pwd=request.getParameter("pwd");
                JsonObject connection=new JsonObject();
                JsonObject connect=new JsonObject();
                try {
                    conn = DBConnection.Connection();
                    String existance=DBConnection.ExistanceCompte(email,conn);
                    if(existance.equals("not exist")){
                        try (PrintWriter out = response.getWriter()) {
                            Gson gson=new GsonBuilder().setPrettyPrinting().create();
                            connect.addProperty("connect", "account not exist");
                            connection.add("connect", connect);
                            out.println(gson.toJson(connection));
                        }
                    }
                    else{
                        boolean flag=DBConnection.Connect(email, pwd, conn);
                        try (PrintWriter out = response.getWriter()) {
                            Gson gson=new GsonBuilder().setPrettyPrinting().create();
                            if(flag){
                                connect.addProperty("connect", "successful");
                                request.getSession().setAttribute("email", email);
                            }
                            else{
                                connect.addProperty("connect", "failed");
                            }
                            connection.add("connect", connect);
                            out.println(gson.toJson(connection));
                        }
                    }
                }
                catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "inscription":
                email=request.getParameter("email");
                sex=request.getParameter("sex");
                lastName=request.getParameter("lastName");
                firstName=request.getParameter("firstName");
                age=request.getParameter("age");
                address=request.getParameter("address");
                pwd=request.getParameter("pwd");
                JsonObject inscription=new JsonObject();
                try {
                    conn = DBConnection.Connection();
                    String existance=DBConnection.ExistanceCompte(email, conn);
                    if(existance.equals("not exist")){
                        int resultInsert=DBConnection.Insert(conn,email,sex,lastName,firstName,age,address,pwd);
                        if(resultInsert!=-1){
                            try (PrintWriter out = response.getWriter()) {
                                Gson gson=new GsonBuilder().setPrettyPrinting().create();
                                JsonObject inscrit=new JsonObject();
                                inscrit.addProperty("inscrit", "true");
                                inscription.add("inscrit", inscrit);
                                out.println(gson.toJson(inscription));
                            }
                        }
                    }
                    else{
                        try (PrintWriter out = response.getWriter()) {
                            Gson gson=new GsonBuilder().setPrettyPrinting().create();
                            JsonObject inscrit=new JsonObject();
                            inscrit.addProperty("inscrit", "false");
                            inscription.add("inscrit", inscrit);
                            out.println(gson.toJson(inscription));
                        }
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "profil":
                String id_email=(String)request.getSession().getAttribute("email");
                PrintWriter out = response.getWriter();
                Gson gson=new GsonBuilder().setPrettyPrinting().create();
                ResultSet rs;
                try {
                    conn = DBConnection.Connection();
                    rs = DBConnection.SearchCompteWithEmail(id_email, conn);
                    JsonObject jsonCompte=new JsonObject();
                    while (rs.next()){
                        jsonCompte.addProperty("email", rs.getString(1));
                        jsonCompte.addProperty("sex", rs.getString(2));
                        jsonCompte.addProperty("lastName", rs.getString(3));
                        jsonCompte.addProperty("firstName", rs.getString(4));
                        jsonCompte.addProperty("age", rs.getString(5));
                        jsonCompte.addProperty("address", rs.getString(6));
                        jsonCompte.addProperty("pwd", rs.getString(7));
                    }
                    rs.beforeFirst();
                    JsonObject container=new JsonObject();
                    container.add("profil", jsonCompte);
                    out.println(gson.toJson(container));
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default:
            }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        service(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        service(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
