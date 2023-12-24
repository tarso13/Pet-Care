/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.spark_pets;

/**
 *
 * @author kelet
 */
public class Pet {
    private int pet_id;
    private int owner_id;
    private String name;
    private String type;
    private String breed;
    private String gender;
    private int birthyear;
    private double weight;
    private String description;
    private String photo;
    
    public int getPetId() {
        return pet_id;
    }

    public int getOwnerId() {
        return owner_id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getBreed() {
        return breed;
    }

    public String getGender() {
        return gender;
    }

    public int getBirthyear() {
        return birthyear;
    }

    public double getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPetId(int pet_id) {
        this.pet_id = pet_id;
    }

    public void setOwnerId(int owner_id) {
        this.owner_id = owner_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
