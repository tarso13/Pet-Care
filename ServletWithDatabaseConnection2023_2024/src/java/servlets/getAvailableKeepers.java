/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.google.gson.Gson;
import database.tables.EditBookingsTable;
import database.tables.EditPetKeepersTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.PetKeeper;

/**
 *
 * @author kelet
 */
public class getAvailableKeepers extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet getAvailableKeepers</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet getAvailableKeepers at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        processRequest(request, response);
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
        response.setContentType("text/html;charset=UTF-8");
        StringBuilder requestData = new StringBuilder();
        InputStream inputStream = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            requestData.append(line);
        }
        String json = requestData.toString();
        Gson gson = new Gson();
        Map<String, String> jsonObject = gson.fromJson(json, Map.class);
        try {
            EditBookingsTable eut = new EditBookingsTable();
            ArrayList<Integer> reservedKeeperIds = eut.getReservedKeeperIds();
            EditPetKeepersTable emt = new EditPetKeepersTable();
            ArrayList<PetKeeper> keepers = emt.getKeepersByPetType(jsonObject.get("type"));
            System.out.print(jsonObject);
            ArrayList<PetKeeper> availableKeepers = new ArrayList();
            for (PetKeeper keeper : keepers) {
                if (reservedKeeperIds.contains(keeper.getKeeperId())) {

                } else {
                    availableKeepers.add(keeper);
                }
            }
            Gson gsonMessages = new Gson();
            String keepersJson = gsonMessages.toJson(availableKeepers);
            response.getWriter().print(keepersJson);
            System.out.println(keepersJson);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(getKeepers.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(getAvailableKeepers.class.getName()).log(Level.SEVERE, null, ex);
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
