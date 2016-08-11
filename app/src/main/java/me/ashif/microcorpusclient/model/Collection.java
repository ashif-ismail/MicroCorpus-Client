package me.ashif.microcorpusclient.model;

import java.util.Comparator;

/**
 * Created by almukthar on 6/8/16.
 */
public class Collection{
    private String customerID;
    private int collectionAmount;
    private String collectedBy;
    private String dateOfCollection;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public int getCollectionAmount() {
        return collectionAmount;
    }

    public void setCollectionAmount(int collectionAmount) {
        this.collectionAmount = collectionAmount;
    }

    public String getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(String collectedBy) {
        this.collectedBy = collectedBy;
    }

    public String getDateOfCollection() {
        return dateOfCollection;
    }

    public void setDateOfCollection(String dateOfCollection) {
        this.dateOfCollection = dateOfCollection;
    }

}
