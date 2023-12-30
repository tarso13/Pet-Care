/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.tables.EditPetOwnersTable;
import database.tables.EditPetsTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Pet;

/**
 *
 * @author kelet
 */
public class addPet extends HttpServlet {

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
            out.println("<title>Servlet addPet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addPet at " + request.getContextPath() + "</h1>");
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
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        EditPetOwnersTable eut = new EditPetOwnersTable();
        EditPetsTable ept = new EditPetsTable();
        response.setContentType("text/html;charset=UTF-8");
        StringBuilder requestData = new StringBuilder();
        InputStream inputStream = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            requestData.append(line);
        }
        String json = requestData.toString();
        System.out.println(json);
        try (PrintWriter out = response.getWriter()) {
            Pet pet = ept.jsonToPet(json);
            Boolean ownerExistsOwnersTable = eut.databasePetOwnerExists(String.valueOf(pet.getOwnerId()));

            if (!ownerExistsOwnersTable) {
                response.setStatus(406);
                response.getWriter().print("Owner does not exist");
                return;
            }

//            Pet ownerExistsPetsTable = ept.petOfOwner(String.valueOf(pet.getOwnerId()));
//            if (ownerExistsPetsTable != null) {
//                response.setStatus(406);
//                response.getWriter().print("Owner already has a pet");
//                return;
//            }

            String petId = String.valueOf(pet.getPetId());
            Boolean petExists = ept.databaseToPetId(petId);

            if (petExists) {
                response.setStatus(406);
                response.getWriter().print("Pet already exists");
                return;
            }

            if (petId.length() != 10) {
                response.setStatus(406);
                response.getWriter().print("Invalid id for pet");
                return;
            }

            if (!pet.getPhoto().startsWith("http")) {
                response.setStatus(406);
                response.getWriter().print("Invalid photo format");
            }

            if (pet.getWeight() < 0) {
                response.setStatus(406);
                response.getWriter().print("Invalid weight");
                return;
            }

            if (pet.getBirthyear() < 2000) {
                response.setStatus(406);
                response.getWriter().print("Invalid birthyear");
                return;
            }

            ept.addPetFromJSON(json);
            response.setStatus(200);
        } catch (SQLException ex) {
            Logger.getLogger(addPet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(addPet.class.getName()).log(Level.SEVERE, null, ex);
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
