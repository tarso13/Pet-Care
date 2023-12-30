/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.google.gson.Gson;
import database.tables.EditBookingsTable;
import database.tables.EditMessagesTable;
import database.tables.EditReviewsTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Booking;
import mainClasses.Message;
import mainClasses.Review;

/**
 *
 * @author kelet
 */
public class runGiveaway extends HttpServlet {

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
            out.println("<title>Servlet runGiveaway</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet runGiveaway at " + request.getContextPath() + "</h1>");
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
        EditMessagesTable emt = new EditMessagesTable();
        try {
            ArrayList<Message> messages_admin = emt.getMessagesAdmin();
            if (messages_admin.isEmpty()) {
            } else {
                Date currentDate = new Date();
                int currentMonth = currentDate.getMonth() + 1;
                for (Message m : messages_admin) {
                    String datetime = m.getDatetime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = df.parse(datetime);
                    int messageMonth = date.getMonth() + 1;
                    if (currentMonth == messageMonth) {
                        response.setStatus(403);
                        return;
                    }
                }
                EditBookingsTable ebt = new EditBookingsTable();
                ArrayList<Booking> bookings = ebt.getAllBookings();
                HashMap<Integer, Integer> ownerPoints = new HashMap<>();
                for (Booking b : bookings) {
                    int owner_id = b.getOwner_id();
                    LocalDate startDate = LocalDate.parse(b.getFromDate());
                    LocalDate endDate = LocalDate.parse(b.getToDate());
                    long total_days = ChronoUnit.DAYS.between(startDate, endDate);;
                    int pointsToAdd = (int) total_days;
                    if (ownerPoints.containsKey(owner_id)) {
                        int currentPoints = ownerPoints.get(owner_id);
                        ownerPoints.put(owner_id, currentPoints + pointsToAdd);
                    } else {
                        ownerPoints.put(owner_id, pointsToAdd);
                    }
                }

                EditReviewsTable ert = new EditReviewsTable();
                ArrayList<Review> reviews = ert.getAllReviews();
                for (Review r : reviews) {
                    int owner_id = r.getOwner_id();
                    if (ownerPoints.containsKey(owner_id)) {
                        int currentPoints = ownerPoints.get(owner_id);
                        ownerPoints.put(owner_id, currentPoints + 2);
                    } else {
                        ownerPoints.put(owner_id, 2);
                    }
                }
                ArrayList<Integer> ownerIds = new ArrayList();
                for (Integer key : ownerPoints.keySet()) {
                    int value = ownerPoints.get(key);
                    for (int i = 0; i < value; i++) {
                        ownerIds.add(key);
                    }
                }

                Collections.shuffle(ownerIds);
                Random random = new Random();
                int winnerIndex = random.nextInt(ownerIds.size() - 0);
                int winner1Id = ownerIds.get(winnerIndex);
                winnerIndex = random.nextInt(ownerIds.size() - 0);
                int winner2Id = ownerIds.get(winnerIndex);
                while (winner2Id == winner1Id) {
                    winnerIndex = random.nextInt(ownerIds.size() - 0);
                    winner2Id = ownerIds.get(winnerIndex);
                }
                ArrayList<Booking> winnerBookings = new ArrayList();
                for (Booking b : bookings) {
                    if (b.getOwner_id() == winner1Id) {
                        winnerBookings.add(b);
                        break;
                    }
                }
                for (Booking b : bookings) {
                    if (b.getOwner_id() == winner2Id) {
                        winnerBookings.add(b);
                        break;
                    }
                }
                Gson gson = new Gson();
                String bookingsJson = gson.toJson(winnerBookings);
                response.setStatus(200);
                response.getWriter().print(bookingsJson);
            }
        } catch (SQLException ex) {
            Logger.getLogger(runGiveaway.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(500);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(runGiveaway.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(500);
        } catch (ParseException ex) {
            Logger.getLogger(runGiveaway.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(500);
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
