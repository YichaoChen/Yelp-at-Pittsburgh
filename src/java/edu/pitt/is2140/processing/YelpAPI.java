/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pitt.is2140.processing;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;

import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Main entry for sample Yelp API requests.
 * <p>
 * After entering your OAuth credentials, execute <tt><b>run.sh</b></tt> to run
 * this example.
 */
@WebServlet(name = "YelpAPI", urlPatterns = {"/YelpAPI"})
public class YelpAPI extends HttpServlet {

    private static final String API_HOST = "api.yelp.com";
    private static final String DEFAULT_TERM = "dinner";
    private static final String DEFAULT_LOCATION = "Pittsburgh";
    private static final int SEARCH_LIMIT = 3;
    private static final String SEARCH_PATH = "/v2/search";
    private static final String BUSINESS_PATH = "/v2/business";

    /*
   * Update OAuth credentials below from the Yelp Developers API site:
   * http://www.yelp.com/developers/getting_started/api_access
     */
    private static final String CONSUMER_KEY = "cuPeYGni21ePbZeHLtPfUQ";
    private static final String CONSUMER_SECRET = "r7L6z5Y3LknkvMKPwbjePj1lybY";
    private static final String TOKEN = "lf1gYlt574YfQgZHhqudj69-Bn5M9meH";
    private static final String TOKEN_SECRET = "Jyd6G9Sze1DapGjwADrVRv5akvQ";

    OAuthService service;
    Token accessToken;

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
            String business_id = request.getParameter("apiID");
            
            try{
                this.service
                    = new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(CONSUMER_KEY)
                    .apiSecret(CONSUMER_SECRET).build();
                this.accessToken = new Token(TOKEN, TOKEN_SECRET);

                // YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
                String path = BUSINESS_PATH + "/" + business_id;
                OAuthRequest requestAPI = new OAuthRequest(Verb.GET, "https://" + API_HOST + path);
                // sendRequestAndGetResponse();

                this.service.signRequest(this.accessToken, requestAPI);

                Response responseAPI = requestAPI.send();

                String searchResponseJSON = responseAPI.getBody();

                JSONObject json_object = new JSONObject(searchResponseJSON);

                out.println(json_object.get("image_url").toString());
            }catch(Exception e){
                business_id = business_id + "-2";
                this.service
                    = new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(CONSUMER_KEY)
                    .apiSecret(CONSUMER_SECRET).build();
                this.accessToken = new Token(TOKEN, TOKEN_SECRET);

                // YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
                String path = BUSINESS_PATH + "/" + business_id;
                OAuthRequest requestAPI = new OAuthRequest(Verb.GET, "https://" + API_HOST + path);
                // sendRequestAndGetResponse();

                this.service.signRequest(this.accessToken, requestAPI);

                Response responseAPI = requestAPI.send();

                String searchResponseJSON = responseAPI.getBody();

                JSONObject json_object = new JSONObject(searchResponseJSON);

                out.println(json_object.get("image_url").toString());
            }
            
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
            Logger.getLogger(YelpAPI.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(YelpAPI.class.getName()).log(Level.SEVERE, null, ex);
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
