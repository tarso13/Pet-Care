package servlets;

import database.tables.EditPetKeepersTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.PetKeeper;

/**
 *
 * @author kelet
 */
public class InsertPetKeeper extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

           EditPetKeepersTable eut = new EditPetKeepersTable();
           PetKeeper user = eut.jsonToPetKeeper(requestData.toString());
           String json = eut.petKeeperToJSON(user);
           eut.addPetKeeperFromJSON(json);
           response.setStatus(HttpServletResponse.SC_OK); 
        }catch (ClassNotFoundException ex) {
                Logger.getLogger(InsertPetOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
