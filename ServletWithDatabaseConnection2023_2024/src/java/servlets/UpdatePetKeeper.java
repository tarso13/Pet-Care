/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import database.tables.EditPetKeepersTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.PetKeeper;

/**
 *
 * @author kelet
 */
public class UpdatePetKeeper extends HttpServlet {

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
            out.println("<title>Servlet UpdatePetKeeper</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdatePetKeeper at " + request.getContextPath() + "</h1>");
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
        try {
            response.setContentType("text/html;charset=UTF-8");
            StringBuilder requestData = new StringBuilder();
            InputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                requestData.append(line);
            }
            String json  = requestData.toString();
            EditPetKeepersTable eut = new EditPetKeepersTable();
            PetKeeper user = eut.jsonToPetKeeper(json);
            eut.updatePetKeeper(json);
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setMaxAge(0); 
                    response.addCookie(cookie); 
                }
            }

            Cookie cookie = new Cookie("username", user.getUsername());
            response.addCookie(cookie);
            cookie = new Cookie("password", user.getPassword());
            response.addCookie(cookie);
            cookie = new Cookie("email", user.getEmail());
            response.addCookie(cookie);
            cookie = new Cookie("firstname", user.getFirstname());
            response.addCookie(cookie);
            cookie = new Cookie("lastname", user.getLastname());
            response.addCookie(cookie);
            cookie = new Cookie("birthdate", user.getBirthdate());
            response.addCookie(cookie);
            cookie = new Cookie("gender", user.getGender());
            response.addCookie(cookie);
            cookie = new Cookie("job", user.getJob());
            response.addCookie(cookie);
            cookie = new Cookie("lat", String.valueOf(user.getLat()));
            response.addCookie(cookie);
            cookie = new Cookie("lon", String.valueOf(user.getLon()));
            response.addCookie(cookie);
            cookie = new Cookie("country", user.getCountry());
            response.addCookie(cookie);
            cookie = new Cookie("city", user.getCity());
            response.addCookie(cookie);
            cookie = new Cookie("address", URLEncoder.encode(user.getAddress(), "UTF-8"));
            response.addCookie(cookie);
            cookie = new Cookie("telephone", user.getTelephone());
            response.addCookie(cookie);
            cookie = new Cookie("personalpage", user.getPersonalpage());
            response.addCookie(cookie);
            cookie = new Cookie("property", user.getProperty());
            response.addCookie(cookie);
            cookie = new Cookie("propertydescription", URLEncoder.encode(user.getPropertydescription(), "UTF-8"));
            response.addCookie(cookie);
            cookie = new Cookie("personalpage", user.getPersonalpage());
            response.addCookie(cookie);
            cookie = new Cookie("catkeeper", user.getCatkeeper());
            response.addCookie(cookie);
            cookie = new Cookie("dogkeeper", user.getDogkeeper());
            response.addCookie(cookie);
            cookie = new Cookie("catprice", String.valueOf(user.getCatprice()));
            response.addCookie(cookie);
            cookie = new Cookie("dogprice", String.valueOf(user.getDogprice()));
            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UpdatePetKeeper.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("Internal Server Error");
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
