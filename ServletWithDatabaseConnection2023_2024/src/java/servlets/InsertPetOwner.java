package servlets;

import database.tables.EditPetOwnersTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.PetOwner;

/**
 *
 * @author kelet
 */

public class InsertPetOwner extends HttpServlet{
    
        protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserPutAndDelete</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserPutAndDelete at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
       try {
           response.setContentType("text/html;charset=UTF-8");
           StringBuilder requestData = new StringBuilder();
           InputStream inputStream = request.getInputStream();
           BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
               
           String line;
           while ((line = reader.readLine()) != null) {
                requestData.append(line);
           }

           EditPetOwnersTable eut = new EditPetOwnersTable();
           PetOwner user = eut.jsonToPetOwner(requestData.toString());
           String json = eut.petOwnerToJSON(user);
           eut.addPetOwnerFromJSON(json);
           response.setStatus(HttpServletResponse.SC_OK);
       }catch (ClassNotFoundException ex) {
                Logger.getLogger(InsertPetOwner.class.getName()).log(Level.SEVERE, null, ex);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
                response.getWriter().print("Internal Server Error");
        }
    }
}
