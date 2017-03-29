/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pitt.is2140.processing;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zigchg
 */
@WebServlet(name = "Scoring", urlPatterns = {"/Scoring"})
public class Scoring extends HttpServlet {
    
    public int count = 0;

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
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String query = "";
            if (request.getParameter("searchbutton") != null) {
                //out.println(request.getParameter("index_searchbox"));
                if (request.getParameter("index_searchbox") != null) {
                    if (!request.getParameter("index_searchbox").equals("")) {
                        
                        query = request.getParameter("index_searchbox");
                    }
                }
            }
            
            if(count != 0){
                Cookie[] cookies = request.getCookies();


                for(Cookie cookie : cookies){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
                        
            //request.getSession().setAttribute("tag_num", null);
            
            //out.println(query);
            
            String extendedQuery;
            String originalQuery;

            Query aQuery = new Query(query);
            extendedQuery = aQuery.getExtendedBeforeStem();
            originalQuery = aQuery.getOriginalQuery();
            
            //out.println(aQuery.GetQueryContent());
            
            String[] synonyms = extendedQuery.split(" ");
            
            MyIndexReader ixreader = new MyIndexReader("trectext");
            
            
            PseudoRFRetrievalModel PRFSearchModel = new PseudoRFRetrievalModel(ixreader);
            
            Cookie originalQ = new Cookie("originalQuery", originalQuery);
            response.addCookie(originalQ);
            
            //out.println(aQuery.GetQueryContent());

            
            out.println(aQuery.GetQueryContent());
            
            List<Document> results = PRFSearchModel.RetrieveQuery(aQuery, 20, 100, 0.4);
            
            out.println(PRFSearchModel.postLen);
            
            String isReviewEmpty;
            Cookie reviewEmpty;
            
            if(results == null){
                isReviewEmpty = "true";
                reviewEmpty = new Cookie("isReviewEmpty", isReviewEmpty);
                response.addCookie(reviewEmpty);
                response.sendRedirect("result.jsp");
            }else{
                isReviewEmpty = "false";
                reviewEmpty = new Cookie("isReviewEmpty", isReviewEmpty);
                response.addCookie(reviewEmpty);
            }
            
                        //out.println(aQuery.GetQueryContent());

            
            
            Cookie [] business_id = new Cookie[results.size()];
            Cookie[] tag_num = new Cookie[synonyms.length];
            
//            ArrayList<String> tag_num = new ArrayList<>();
            
            int i = -1;
            int j = -1;
            
            for(String tags : synonyms) {
                j++;
                tag_num[j] = new Cookie("tag_num" + j, tags);
                //tag_num[j].setPath(request.getServletPath());
                tag_num[j].setMaxAge(1*60*60);
                response.addCookie(tag_num[j]);
            }
            
//            for(String tags : tag_num) {
//                tag_num.add(tags);
//            }
//            
//            request.getSession().setAttribute("tag_num", tag_num);

            
            for (Document result : results){
                i++;
                business_id[i] = new Cookie("business_id" + i, result.docno());
                //business_id[i].setPath(request.getServletPath());
                business_id[i].setMaxAge(1*60*60);
                response.addCookie(business_id[i]);
//                business_id[i].getName()
            }
//            Cookie[] cookies = ...get;
//            for(Cookie cookie: cookies){
//                
//            }
//            request.getSession().setAttribute("business", business);
            count++;
            response.sendRedirect("result.jsp");
            
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
        } catch (Exception ex) {
            Logger.getLogger(Scoring.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception ex) {
            Logger.getLogger(Scoring.class.getName()).log(Level.SEVERE, null, ex);
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
