/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web;

import com.google.api.translate.Language;
import db.PMF;
import db.entities.WaveDbo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.Transactional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lamache
 */
public class ConfigBot extends HttpServlet {

    private PersistenceManager pm;
    private WaveDbo w;
    private WaveDbo waveObj = null;
    private static final Logger log = Logger.getLogger(ConfigBot.class.getName());
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConfigBot</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<b>Config Your Bot</b>");
            out.println("<form id='io' method='post' action='/ConfigBot?wid=" + request.getParameter("wid") + "'>");
            out.println("<select name='lang'>");
            out.println("<option>Choose a language</option>");
            if (waveObj != null) {
                out.println("<option value='" + waveObj.getLanguage() + "' selected='selected'>" + Language.fromString(waveObj.getLanguage()).name() + "</option>");
            }
            for (Language l : Language.values()) {
                if (!l.toString().isEmpty()) {
                    out.println("<option value='"+ l.toString() +"'>"+ l.name() +"</option>");
                }
            }
            out.println("</select>");
            out.println("<input type='hidden' name='wid' value='" + request.getParameter("wid") + "' />");
            out.println("<input type='submit'>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
            
        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        if (!request.getParameter("wid").isEmpty()) {
            if (pm == null || pm.isClosed()) {
                pm = PMF.get().getPersistenceManager();
            }
            Query query = pm.newQuery(WaveDbo.class);
            query.setFilter("waveId == waveidParam");
            query.declareParameters("String waveidParam");

            List<WaveDbo> results = (List<WaveDbo>) query.execute(request.getParameter("wid").replace("+", " "));
            if (results.size() > 0) {
                waveObj = results.get(0);
            }
        }

        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    @Transactional
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        if (!request.getParameter("wid").isEmpty() && !request.getParameter("lang").isEmpty()) {
            
            if (pm == null || pm.isClosed()) {
                pm = PMF.get().getPersistenceManager();
            }

            try {
                Query query = pm.newQuery(WaveDbo.class);
                query.setFilter("waveId == waveidParam");
                query.declareParameters("String waveidParam");

                List<WaveDbo> results = (List<WaveDbo>) query.execute(request.getParameter("wid").replace("+", " "));

                log.info("Already Exist ? " + (results.size() > 0 ? "Oui" : "Non"));

                if (results.size() > 0) {
                    waveObj = results.get(0);
                    waveObj.setLanguage(request.getParameter("lang"));
                } else {
                    w = new WaveDbo(request.getParameter("wid"), request.getParameter("lang"));
                    pm.makePersistent(w);
                }
            } finally {
                pm.close();
            }

            
        }
        
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
