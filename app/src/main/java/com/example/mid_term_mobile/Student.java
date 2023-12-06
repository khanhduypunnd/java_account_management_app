package com.example.mid_term_mobile;

public class Student {
    private String age, avatar, st_class, email, name, phone;


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSt_class() {
        return st_class;
    }

    public void setSt_class(String st_class) {
        this.st_class = st_class;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Student(String age, String avatar, String st_class, String email, String name, String phone) {
        this.age = age;
        this.avatar = avatar;
        this.st_class = st_class;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
}