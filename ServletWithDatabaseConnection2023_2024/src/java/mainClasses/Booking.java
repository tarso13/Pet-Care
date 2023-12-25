/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainClasses;

/**
 *
 * @author Mike
 */
public class Booking {

    int booking_id, owner_id, pet_id, keeper_id;
    String fromdate, todate, status;
    int price;

    public int getBorrowing_id() {
        return booking_id;
    }

    public void setBorrowing_id(int borrowing_id) {
        this.booking_id = borrowing_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getPet_id() {
        return pet_id;
    }

    public void setPet_id(int pet_id) {
        this.pet_id = pet_id;
    }

    public int getKeeper_id() {
        return keeper_id;
    }

    public void setKeeper_id(int keeper_id) {
        this.keeper_id = keeper_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFromDate() {
        return fromdate;
    }

    public void setFromDate(String fromDate) {
        this.fromdate = fromDate;
    }

    public String getToDate() {
        return todate;
    }

    public void setToDate(String toDate) {
        this.todate = toDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
