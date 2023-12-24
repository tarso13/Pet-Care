/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.spark_pets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.spark_pets.database.tables.EditPetOwnersTable;
import com.mycompany.spark_pets.database.tables.EditPetsTable;
import java.util.ArrayList;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

/**
 *
 * @author kelet
 */
public class Spark_Pets {

    static String apiPath = "PetsAPI/";

    public static void main(String[] args) {
        post(apiPath + "/pet", (request, response) -> {
            response.type("application/json");
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            EditPetOwnersTable eut = new EditPetOwnersTable();
            EditPetsTable ept = new EditPetsTable();
            Pet pet = ept.jsonToPet(request.body());
            Boolean ownerExistsOwnersTable = eut.databasePetOwnerExists(String.valueOf(pet.getOwnerId()));

            if (!ownerExistsOwnersTable) {
                response.status(406);
                return gson.toJson(new StandardResponse("Error: Owner does not exist"));
            }

            Pet ownerExistsPetsTable = ept.petOfOwner(String.valueOf(pet.getOwnerId()));
            if (ownerExistsPetsTable != null) {
                response.status(406);
                return gson.toJson(new StandardResponse("Error: Owner already has a pet"));
            }

            String petId = String.valueOf(pet.getPetId());
            Boolean petExists = ept.databaseToPetId(petId);

            if (petExists) {
                response.status(406);
                return gson.toJson(new StandardResponse("Error: Pet already exists"));
            }
            if (petId.length() != 10) {
                response.status(406);
                return gson.toJson(new StandardResponse("Error: Invalid id for pet"));

            }

            if (!pet.getPhoto().startsWith("http")) {
                response.status(406);
                return gson.toJson(new StandardResponse("Error: Invalid photo format"));
            }

            if (pet.getWeight() < 0) {
                response.status(406);
                return gson.toJson(new StandardResponse("Error: Invalid weight"));
            }
            
             if (pet.getBirthyear() < 2000) {
                response.status(406);
                return gson.toJson(new StandardResponse("Error: Invalid birthyear"));
            }

            ept.addPetFromJSON(request.body());
            response.status(200);
            return gson.toJson(new StandardResponse("Success: Pet added to database"));

        });

        get(apiPath + "/pets/:type/:breed", (request, response)
                -> {
            response.type("application/json");
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String type = request.params(":type");
            String breed = request.params(":breed");
            String fromWeight = request.queryParams("fromWeight");
            String toWeight = request.queryParams("toWeight");
            double fromValue;
            double toValue;

            if (fromWeight != null && toWeight != null) {
                fromValue = Double.parseDouble(fromWeight);
                toValue = Double.parseDouble(toWeight);

                if (fromValue > toValue) {
                    response.status(406);
                    return gson.toJson(new StandardResponse("Error: Invalid parameters for weight range [fromWeight, toWeight]"));
                }

            }

            if (fromWeight != null) {
                fromValue = Double.parseDouble(fromWeight);
                if (fromValue < 0.0) {
                    response.status(406);
                    return gson.toJson(new StandardResponse("Error: Invalid parameters for weight range [fromWeight]"));
                }

            }
            if (toWeight != null) {
                toValue = Double.parseDouble(toWeight);
                if (toValue < 0.0) {
                    response.status(406);
                    return gson.toJson(new StandardResponse("Error: Invalid parameters for weight range [toWeight]"));
                }
            }

            EditPetsTable Ept = new EditPetsTable();
            ArrayList<Pet> pets = Ept.databaseToPetsTypeBreed(breed, type, fromWeight, toWeight);
            response.status(200);
            return gson.toJson(new StandardResponse(gson.toJson(pets)));

        });

        put(apiPath + "/petWeight/:pet_id/:weight", (request, response)
                -> {
            response.type("application/json");
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String pet_id = request.params(":pet_id");
            String weight = request.params(":weight");
            if (Double.parseDouble(weight) < 0.0) {
                response.status(406);
                return gson.toJson(new StandardResponse("Error: Invalid parameter for weight"));
            }

            EditPetsTable ept = new EditPetsTable();
            Boolean petExists = ept.databaseToPetId(pet_id);

            if (!petExists) {
                response.status(406);
                return gson.toJson(new StandardResponse("Error: Pet does not exist"));
            }
            ept.updatePetWeight(pet_id, weight);
            response.status(200);
            return gson.toJson(new StandardResponse("Success: Pet updated"));

        });

        delete(apiPath + "/petDeletion/:pet_id", (request, response)
                -> {
            response.type("application/json");
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String pet_id = request.params(":pet_id");
            EditPetsTable ept = new EditPetsTable();
            Boolean petExists = ept.databaseToPetId(pet_id);

            if (!petExists) {
                response.status(406);
                return gson.toJson(new StandardResponse("Error: Pet does not exist"));
            }
            ept.deletePet(pet_id);
            response.status(200);
            return gson.toJson(new StandardResponse("Success: Pet deleted"));

        });
    }
}
