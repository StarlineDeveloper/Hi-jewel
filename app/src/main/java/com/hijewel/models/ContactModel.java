package com.hijewel.models;

import org.json.JSONObject;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class ContactModel {

    private String company1, company2, company3, company4, company5, person1, person2, contact11,
            contact12, contact21, contact22, email1, email2, address1, address2,marquee,sociallink,Whatsapp;

    public ContactModel() {
    }

    public ContactModel(JSONObject j) {
        company1 = j.optString("company1");
        company2 = j.optString("company2");
        company3 = j.optString("company3");
        company4 = j.optString("company4");
        company5 = j.optString("company5");
        person1 = j.optString("person1");
        person2 = j.optString("person2");
        contact11 = j.optString("contact11");
        contact12 = j.optString("contact12");
        contact21 = j.optString("contact21");
        contact22 = j.optString("contact22");
        email1 = j.optString("email1");
        email2 = j.optString("email2");
        address1 = j.optString("address1");
        address2 = j.optString("address2");
        marquee = j.optString("marquee");
        sociallink = j.optString("sociallink");
        Whatsapp = j.optString("Whatsapp");
    }

    public String getCompany1() {
        return company1;
    }

    public void setCompany1(String company1) {
        this.company1 = company1;
    }

    public String getCompany2() {
        return company2;
    }

    public void setCompany2(String company2) {
        this.company2 = company2;
    }

    public String getCompany3() {
        return company3;
    }

    public void setCompany3(String company3) {
        this.company3 = company3;
    }

    public String getCompany4() {
        return company4;
    }

    public void setCompany4(String company4) {
        this.company4 = company4;
    }

    public String getCompany5() {
        return company5;
    }

    public void setCompany5(String company5) {
        this.company5 = company5;
    }

    public String getPerson1() {
        return person1;
    }

    public void setPerson1(String person1) {
        this.person1 = person1;
    }

    public String getPerson2() {
        return person2;
    }

    public void setPerson2(String person2) {
        this.person2 = person2;
    }

    public String getContact11() {
        return contact11;
    }

    public void setContact11(String contact11) {
        this.contact11 = contact11;
    }

    public String getContact12() {
        return contact12;
    }

    public void setContact12(String contact12) {
        this.contact12 = contact12;
    }

    public String getContact21() {
        return contact21;
    }

    public void setContact21(String contact21) {
        this.contact21 = contact21;
    }

    public String getContact22() {
        return contact22;
    }

    public void setContact22(String contact22) {
        this.contact22 = contact22;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getAddress1() {
        return address1;
    }

    public String getmarquee() {
        return marquee;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String sociallink() {
        return sociallink;
    }
    public String Whatsapp() {
        return Whatsapp;
    }



    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    public void setmarquee(String marquee) {
        this.marquee = marquee;
    }

    public void setWhatsapp(String Whatsapp) {
        this.Whatsapp = Whatsapp;
    }

}
