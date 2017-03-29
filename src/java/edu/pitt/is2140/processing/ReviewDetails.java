/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pitt.is2140.processing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author zigchg
 */
@WebServlet(name = "ReviewDetails", urlPatterns = {"/ReviewDetails"})
public class ReviewDetails extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String business_id = request.getParameter("businessID");
            
            InputStream inputStream = getServletContext().getResourceAsStream("/WEB-INF/classes/business/pittsburgh_review_oneline.txt");
            InputStreamReader streamReader = new InputStreamReader(inputStream);

            BufferedReader reader = new BufferedReader(streamReader);
            
            String line = "";
            StringBuilder reviews = new StringBuilder();
            
            // out.print(business_id);
            
            
            int count = 0;
            while((line=reader.readLine())!=null){
                // out.print(line);
                
                try{
                    JSONObject parsed_json = new JSONObject(line);
                    // out.println(parsed_json.get("business_id"));
                    
                    // out.print(reviews.toString());
                    if(parsed_json.get("business_id").equals(business_id)){
                        // out.println(parsed_json.get("text"));
                        count++;
                        reviews.append("<div class='review'>");
                        reviews.append("<div class='review_head'><h2>Review "+count+"</h2></div><br /><div class='review_body'><p style='font-size:20px'>");
                        reviews.append(parsed_json.get("text"));
                        reviews.append("</p></div><br />");
                        reviews.append("</div>");
                        // break;
                    }
                }catch(Exception e){
                    
                }
                
            }
            
            
            
            // out.print("end of loop");
            
            reader.close();
            // out.print(reviews.toString());
            
            
            
            try{
                
                String results = reviews.toString();
                // out.print(business_id);
                out.println(reviews.toString());
                
            }catch(Exception e){
                out.print(e);
            }
            
//            Cookie bizID = new Cookie("bizID", business_id);
//            response.addCookie(bizID);
//            response.sendRedirect("reviews.jsp");
            
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
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(ReviewDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(ReviewDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
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
