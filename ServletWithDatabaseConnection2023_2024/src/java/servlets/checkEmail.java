package servlets;

import database.tables.EditPetKeepersTable;
import database.tables.EditPetOwnersTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.PetKeeper;
import mainClasses.PetOwner;

/**
 *
 * @author kelet
 */
public class checkEmail extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
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
            PetKeeper su = null;
            if (user != null) {
                su = eut.databaseToPetKeepersEmail(user.getEmail());
            }
            EditPetOwnersTable eutt = new EditPetOwnersTable();
            PetOwner user1 = eutt.jsonToPetOwner(requestData.toString());
            PetOwner suu = null;
            if (user1 != null) {
                suu = eutt.databaseToPetOwnersEmail(user1.getEmail());
            }
            if (su == null && suu == null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().print("Email available");
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().print("Email already exists");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(checkEmail.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("Internal Server Error");
        }
    }
}
