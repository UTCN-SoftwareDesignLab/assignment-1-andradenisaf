package model;

import java.io.Serializable;

public class Client implements IModel, Serializable {
    private int id;
    private String fullname;
    private String CNP;
    private int identityCardNo;
    private String address;
    private String email;

    public Client(String fullname, String CNP, int identityCardNo, String address, String email) {
        this.fullname = fullname;
        this.CNP = CNP;
        this.identityCardNo = identityCardNo;
        this.address = address;
        this.email = email;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public int getIdentityCardNo() {
        return identityCardNo;
    }

    public void setIdentityCardNo(int identityCardNo) {
        this.identityCardNo = identityCardNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client{" +
                "fullname='" + fullname + '\'' +
                ", CNP='" + CNP + '\'' +
                ", identityCardNo=" + identityCardNo +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
