/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.spark_pets.database.tables;

import mainClasses.PetOwner;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import mainClasses.PetOwner;
import com.mycompany.spark_laptops.database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mainClasses.Booking;

/**
 *
 * @author Mike
 */
public class EditPetOwnersTable {

    public void addPetOwnerFromJSON(String json) throws ClassNotFoundException {
        PetOwner user = jsonToPetOwner(json);
        addNewPetOwner(user);
    }

    public PetOwner jsonToPetOwner(String json) {
        Gson gson = new Gson();

        PetOwner user = gson.fromJson(json, PetOwner.class);
        return user;
    }

    public String petOwnerToJSON(PetOwner user) {
        Gson gson = new Gson();

        String json = gson.toJson(user, PetOwner.class);
        return json;
    }

    public void updatePetOwner(String json) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        PetOwner user = jsonToPetOwner(json);

        String updateQuery = "UPDATE petowners SET "
                + "email = '" + user.getEmail() + "', "
                + "password = '" + user.getPassword() + "', "
                + "firstname = '" + user.getFirstname() + "', "
                + "lastname = '" + user.getLastname() + "', "
                + "birthdate = '" + user.getBirthdate() + "', "
                + "gender = '" + user.getGender() + "', "
                + "country = '" + user.getCountry() + "', "
                + "city = '" + user.getCity() + "', "
                + "address = '" + user.getAddress() + "', "
                + "personalpage = '" + user.getPersonalpage() + "', "
                + "job = '" + user.getJob() + "', "
                + "telephone = '" + user.getTelephone() + "', "
                + "lat = '" + user.getLat() + "', "
                + "lon = '" + user.getLon() + "' "
                + "WHERE username = '" + user.getUsername() + "'";

        stmt.executeUpdate(updateQuery);
    }

    public PetOwner databaseToPetOwners(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            String query;
            if (password.equals("")) {
                query = "SELECT * FROM petowners WHERE username = '" + username + "'";
            } else {
                query = "SELECT * FROM petowners WHERE username = '" + username + "' AND password='" + password + "'";
            }
            rs = stmt.executeQuery(query);
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            PetOwner user = gson.fromJson(json, PetOwner.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public PetOwner databaseToPetOwnersEmail(String email) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            String query = "SELECT * FROM petowners WHERE email = '" + email + "'";
            rs = stmt.executeQuery(query);
            PetOwner user = null;
            if (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                user = gson.fromJson(json, PetOwner.class);
            }
            return user;
        } catch (JsonSyntaxException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public PetOwner databaseToPetOwnersUsername(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            String query = "SELECT * FROM petowners WHERE username = '" + username + "'";
            rs = stmt.executeQuery(query);
            PetOwner user = null;
            if (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                user = gson.fromJson(json, PetOwner.class);
            }
            return user;
        } catch (JsonSyntaxException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public String databasePetOwnerToJSON(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petowners WHERE username = '" + username + "' AND password='" + password + "'");
            String json = null;
            if (rs.next()) {
                json = DB_Connection.getResultsToJSON(rs);
            }
            return json;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public boolean databasePetOwnerExists(String ownerId) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM petowners WHERE owner_id = '" + ownerId + "'");
            return rs.next();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void createPetOwnersTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE petowners "
                + "(owner_id INTEGER not NULL AUTO_INCREMENT, "
                + "    username VARCHAR(30) not null unique,"
                + "    email VARCHAR(50) not null unique,	"
                + "    password VARCHAR(32) not null,"
                + "    firstname VARCHAR(30) not null,"
                + "    lastname VARCHAR(30) not null,"
                + "    birthdate DATE not null,"
                + "    gender  VARCHAR (7) not null,"
                + "    country VARCHAR(30) not null,"
                + "    city VARCHAR(50) not null,"
                + "    address VARCHAR(50) not null,"
                + "    personalpage VARCHAR(200) not null,"
                + "    job VARCHAR(200) not null,"
                + "    telephone VARCHAR(14),"
                + "    lat DOUBLE,"
                + "    lon DOUBLE,"
                + " PRIMARY KEY (owner_id))";
        stmt.execute(query);
        stmt.close();
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewPetOwner(PetOwner user) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " petowners (username,email,password,firstname,lastname,birthdate,gender,country,city,address,personalpage,"
                    + "job,telephone,lat,lon)"
                    + " VALUES ("
                    + "'" + user.getUsername() + "',"
                    + "'" + user.getEmail() + "',"
                    + "'" + user.getPassword() + "',"
                    + "'" + user.getFirstname() + "',"
                    + "'" + user.getLastname() + "',"
                    + "'" + user.getBirthdate() + "',"
                    + "'" + user.getGender() + "',"
                    + "'" + user.getCountry() + "',"
                    + "'" + user.getCity() + "',"
                    + "'" + user.getAddress() + "',"
                    + "'" + user.getPersonalpage() + "',"
                    + "'" + user.getJob() + "',"
                    + "'" + user.getTelephone() + "',"
                    + "'" + user.getLat() + "',"
                    + "'" + user.getLon() + "'"
                    + ")";
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The pet owner was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditPetOwnersTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
