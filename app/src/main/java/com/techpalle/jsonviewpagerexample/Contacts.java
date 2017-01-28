package com.techpalle.jsonviewpagerexample;

/**
 * Created by DELL on 23-Jan-17.
 */
public class Contacts {
    private int sno;
    private String name, email, mobile;

    public Contacts() {
    }

    public Contacts(int sno, String name, String email, String mobile) {
        this.sno = sno;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
