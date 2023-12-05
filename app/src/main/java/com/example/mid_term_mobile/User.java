package com.example.mid_term_mobile;

public class User {
    private String avatar, name, mail, pass, role, status, phone;
    private int age;
    public User(){};

    public User(String avatar, String name, String mail, String pass, String role, String status, int age, String phone) {
        this.avatar = avatar;
        this.name = name;
        this.mail = mail;
        this.pass = pass;
        this.role = role;
        this.status = status;
        this.age = age;
        this.phone = phone;
    }



    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
