package com.example.deniz.plaxo2.model;


import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Contact extends SugarRecord {
    private int contactId;
    private String contactName;
    private String phoneNumber;
    private String nameTitle;
    private String companyName;

    // Default constructor is important!
    public Contact() {

    }

    public Contact(String contactName, String contactSurname) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle = nameTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
