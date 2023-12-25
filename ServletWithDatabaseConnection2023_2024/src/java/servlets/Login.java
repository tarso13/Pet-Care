/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import database.tables.EditPetKeepersTable;
import database.tables.EditPetOwnersTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mainClasses.PetKeeper;
import mainClasses.PetOwner;

/**
 *
 * @author kelet
 */
public class Login extends HttpServlet {

    private String serializeToJson(String value) {
        // Here, we use a simple JSON serialization as an example
        return "\"" + value + "\"";
    }

    private void createCookies(HttpServletResponse response, Map<String, String> map) {

    }

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
            out.println("<title>Servlet Login</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Login at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            StringBuilder requestData = new StringBuilder();
            InputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                requestData.append(line);
            }

            EditPetKeepersTable eut = new EditPetKeepersTable();
            PetKeeper user = eut.jsonToPetKeeper(requestData.toString());
            PetKeeper su;

            if (user != null) {
                su = eut.databaseToPetKeepers(user.getUsername(), user.getPassword());
                if (su != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().print(eut.databasePetKeeperToJSON(user.getUsername(), user.getPassword()));
                    Cookie cookie = new Cookie("username", su.getUsername());
                    response.addCookie(cookie);
                    cookie = new Cookie("password", su.getPassword());
                    response.addCookie(cookie);
                    cookie = new Cookie("keeper_id", String.valueOf(su.getKeeperId()));
                    response.addCookie(cookie);
                    cookie = new Cookie("email", su.getEmail());
                    response.addCookie(cookie);
                    cookie = new Cookie("firstname", su.getFirstname());
                    response.addCookie(cookie);
                    cookie = new Cookie("lastname", su.getLastname());
                    response.addCookie(cookie);
                    cookie = new Cookie("birthdate", su.getBirthdate());
                    response.addCookie(cookie);
                    cookie = new Cookie("gender", su.getGender());
                    response.addCookie(cookie);
                    cookie = new Cookie("job", su.getJob());
                    response.addCookie(cookie);
                    cookie = new Cookie("lat", String.valueOf(su.getLat()));
                    response.addCookie(cookie);
                    cookie = new Cookie("lon", String.valueOf(su.getLon()));
                    response.addCookie(cookie);
                    cookie = new Cookie("country", su.getCountry());
                    response.addCookie(cookie);
                    cookie = new Cookie("city", su.getCity());
                    response.addCookie(cookie);
                    cookie = new Cookie("address", URLEncoder.encode(su.getAddress(), "UTF-8"));
                    response.addCookie(cookie);
                    cookie = new Cookie("telephone", su.getTelephone());
                    response.addCookie(cookie);
                    cookie = new Cookie("personalpage", su.getPersonalpage());
                    response.addCookie(cookie);
                    cookie = new Cookie("property", su.getProperty());
                    response.addCookie(cookie);
                    cookie = new Cookie("propertydescription", URLEncoder.encode(su.getPropertydescription(), "UTF-8"));
                    response.addCookie(cookie);
                    cookie = new Cookie("personalpage", su.getPersonalpage());
                    response.addCookie(cookie);
                    cookie = new Cookie("catkeeper", su.getCatkeeper());
                    response.addCookie(cookie);
                    cookie = new Cookie("dogkeeper", su.getDogkeeper());
                    response.addCookie(cookie);
                    cookie = new Cookie("catprice", String.valueOf(su.getCatprice()));
                    response.addCookie(cookie);
                    cookie = new Cookie("dogprice", String.valueOf(su.getDogprice()));
                    response.addCookie(cookie);
                    return;
                }
            }

            EditPetOwnersTable eutt = new EditPetOwnersTable();
            PetOwner user1 = eutt.jsonToPetOwner(requestData.toString());
            PetOwner suu;

            if (user1 != null) {
                suu = eutt.databaseToPetOwners(user1.getUsername(), user1.getPassword());
                if (suu != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().print(eutt.databasePetOwnerToJSON(user1.getUsername(), user1.getPassword()));
                    Cookie cookie = new Cookie("username", suu.getUsername());
                    response.addCookie(cookie);
                    cookie = new Cookie("password", suu.getPassword());
                    response.addCookie(cookie);
                    cookie = new Cookie("owner_id", String.valueOf(suu.getOwnerId()));
                    response.addCookie(cookie);
                    cookie = new Cookie("email", suu.getEmail());
                    response.addCookie(cookie);
                    cookie = new Cookie("firstname", suu.getFirstname());
                    response.addCookie(cookie);
                    cookie = new Cookie("lastname", suu.getLastname());
                    response.addCookie(cookie);
                    cookie = new Cookie("birthdate", suu.getBirthdate());
                    response.addCookie(cookie);
                    cookie = new Cookie("gender", suu.getGender());
                    response.addCookie(cookie);
                    cookie = new Cookie("job", suu.getJob());
                    response.addCookie(cookie);
                    cookie = new Cookie("lat", String.valueOf(suu.getLat()));
                    response.addCookie(cookie);
                    cookie = new Cookie("lon", String.valueOf(suu.getLon()));
                    response.addCookie(cookie);
                    cookie = new Cookie("country", suu.getCountry());
                    response.addCookie(cookie);
                    cookie = new Cookie("city", suu.getCity());
                    response.addCookie(cookie);
                    cookie = new Cookie("address", URLEncoder.encode(suu.getAddress(), "UTF-8"));
                    response.addCookie(cookie);
                    cookie = new Cookie("telephone", suu.getTelephone());
                    response.addCookie(cookie);
                    cookie = new Cookie("personalpage", suu.getPersonalpage());
                    response.addCookie(cookie);
                    return;
                }
            }

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().print("No such user exists");
        } catch (SQLException | ClassNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("Internal Server Error");
            Logger.getLogger(checkUsername.class.getName()).log(Level.SEVERE, null, ex);
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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
