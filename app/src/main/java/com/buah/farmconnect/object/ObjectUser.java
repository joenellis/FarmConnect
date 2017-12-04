package com.buah.farmconnect.object;

public class ObjectUser {

    private String user_id;
    private String fullname;
    private String email;
    private String password;
    private String contact;

    public ObjectUser() {
    }

    public ObjectUser(String user_id, String fullname, String email, String password, String contact) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.contact = contact;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getContact() {
        return contact;
    }

}
