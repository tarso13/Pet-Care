/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import com.google.gson.Gson;
import mainClasses.Booking;
import database.DB_Connection;
import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mike
 */
public class EditBookingsTable {

    public void addBookingFromJSON(String json) throws ClassNotFoundException {
        Booking r = jsonToBooking(json);
        createNewBooking(r);
    }

    public Booking databaseToBooking(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE booking_id= '" + id + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Booking bt = gson.fromJson(json, Booking.class);
            return bt;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Booking jsonToBooking(String json) {
        Gson gson = new Gson();
        Booking r = gson.fromJson(json, Booking.class);
        return r;
    }

    public ArrayList<Integer> getReservedKeeperIds() throws SQLException, ClassNotFoundException, ParseException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            ArrayList<Integer> ids_future_dates = new ArrayList();
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE status='requested' OR status='accepted'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Booking booking = gson.fromJson(json, Booking.class);
                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date bookingToDate = sdf.parse(booking.getToDate());
                if (bookingToDate.after(currentDate)) {
                    ids_future_dates.add(booking.getKeeper_id());
                }
            }

            return ids_future_dates;
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

        return null;
    }

    public String bookingToJSON(Booking r) {
        Gson gson = new Gson();
        String json = gson.toJson(r, Booking.class
        );
        return json;
    }

    public void updateBooking(int bookingID, String status) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE bookings SET status='" + status + "' WHERE booking_id= '" + bookingID + "'";
        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

    public void createBookingTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE bookings "
                + "(booking_id INTEGER not NULL AUTO_INCREMENT, "
                + " owner_id INTEGER not NULL, "
                + "  pet_id VARCHAR(10) not NULL, "
                + " keeper_id INTEGER not NULL, "
                + " fromdate DATE not NULL, "
                + " todate DATE not NULL, "
                + " status VARCHAR(15) not NULL, "
                + " price INTEGER not NULL, "
                + "FOREIGN KEY (owner_id) REFERENCES petowners(owner_id), "
                + "FOREIGN KEY (pet_id) REFERENCES pets(pet_id), "
                + "FOREIGN KEY (keeper_id) REFERENCES petkeepers(keeper_id), "
                + " PRIMARY KEY (booking_id))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    public HashMap<String, Integer> getMoneyEarned() throws SQLException, ClassNotFoundException {
        HashMap<String, Integer> earnings = new HashMap<>();
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT keeper_id, price FROM bookings WHERE status='finished'");
            while (rs.next()) {
                // find keeper username
                int keeper_id = rs.getInt("keeper_id");
                int price = rs.getInt("price");
                ResultSet rs2 = stmt.executeQuery("SELECT username FROM petkeepers WHERE keeper_id='" + keeper_id + "'");
                if (rs2.next()) {
                    String username = rs2.getString("username");
                    earnings.put(username, price);
                }
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return earnings;
    }

    public ArrayList getKeeperBookings(String keeper_id) throws SQLException, ClassNotFoundException {
        ArrayList<Booking> bookings = new ArrayList<>();
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE keeper_id='" + keeper_id + "'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Booking booking = gson.fromJson(json, Booking.class
                );
                bookings.add(booking);
            }
            return bookings;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList getOwnerBookings(String owner_id) throws SQLException, ClassNotFoundException {
        ArrayList<Booking> bookings = new ArrayList<>();
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE owner_id='" + owner_id + "'");
            System.out.println("SELECT * FROM bookings WHERE owner_id='" + owner_id + "'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Booking booking = gson.fromJson(json, Booking.class
                );
                bookings.add(booking);
            }
            return bookings;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void deleteBookingByKeeperId(String keeper_id) {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String deleteQuery = "DELETE FROM bookings WHERE keeper_id='" + keeper_id + "'";
            stmt.executeUpdate(deleteQuery);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            try {
                ArrayList<Booking> bookings = getKeeperBookings(keeper_id);
                for (Booking b : bookings) {
                    EditMessagesTable emt = new EditMessagesTable();
                    emt.deleteMessageByBookingId(String.valueOf(b.getBorrowing_id()));
                }
//            Logger.getLogger(EditBookingsTable.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(EditBookingsTable.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (ClassNotFoundException ex1) {
                Logger.getLogger(EditBookingsTable.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditBookingsTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBookingByOwnerId(String owner_id) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String deleteQuery = "DELETE FROM bookings WHERE owner_id='" + owner_id + "'";
            stmt.executeUpdate(deleteQuery);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            try {
                ArrayList<Booking> bookings = getOwnerBookings(owner_id);
                for (Booking b : bookings) {
                    EditMessagesTable emt = new EditMessagesTable();
                    emt.deleteMessageByBookingId(String.valueOf(b.getBorrowing_id()));
                }
            } catch (SQLException ex1) {
                Logger.getLogger(EditBookingsTable.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (ClassNotFoundException ex1) {
                Logger.getLogger(EditBookingsTable.class.getName()).log(Level.SEVERE, null, ex1);
            }
            } catch (ClassNotFoundException e) {
            Logger.getLogger(EditBookingsTable.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public Booking getOwnerBookingId(String owner_id) throws SQLException, ClassNotFoundException {
        ArrayList<Booking> bookings = new ArrayList<>();
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE owner_id='" + owner_id + "'");
            if (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Booking booking = gson.fromJson(json, Booking.class
                );
                return booking;
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList getKeeperAcceptedBookings(String keeper_id) throws SQLException, ClassNotFoundException {
        ArrayList<Booking> bookings = new ArrayList<>();
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE keeper_id='" + keeper_id + "' AND status='accepted' OR status='finished'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Booking booking = gson.fromJson(json, Booking.class
                );
                bookings.add(booking);
            }
            return bookings;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList getKeeperFinishedBookings(String keeper_id) throws SQLException, ClassNotFoundException {
        ArrayList<Booking> bookings = new ArrayList<>();
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE keeper_id='" + keeper_id + "' AND status='finished'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Booking booking = gson.fromJson(json, Booking.class
                );
                bookings.add(booking);
            }
            return bookings;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList getOwnerAcceptedBookings(String owner_id) throws SQLException, ClassNotFoundException {
        ArrayList<Booking> bookings = new ArrayList<>();
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE owner_id='" + owner_id + "' AND status='accepted' OR status='finished'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Booking booking = gson.fromJson(json, Booking.class
                );
                bookings.add(booking);
            }
            return bookings;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewBooking(Booking bor) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " bookings (owner_id,pet_id,keeper_id,fromDate,toDate,status,price)"
                    + " VALUES ("
                    + "'" + bor.getOwner_id() + "',"
                    + "'" + bor.getPet_id() + "',"
                    + "'" + bor.getKeeper_id() + "',"
                    + "'" + bor.getFromDate() + "',"
                    + "'" + bor.getToDate() + "',"
                    + "'" + bor.getStatus() + "',"
                    + "'" + bor.getPrice() + "'"
                    + ")";
            //stmt.execute(table);

            stmt.executeUpdate(insertQuery);
            System.out.println("# The booking was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditBookingsTable.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Booking> getAllBookings() throws SQLException, ClassNotFoundException {
        ArrayList<Booking> bookings = new ArrayList<>();
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM bookings WHERE status='accepted' OR status='finished'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Booking booking = gson.fromJson(json, Booking.class
                );
                bookings.add(booking);
            }
            return bookings;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
}
