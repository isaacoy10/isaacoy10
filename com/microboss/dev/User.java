package com.microboss.dev;


public class User {

    public String title;
    public String surname;
    public String firstName;
    public String email;
    public String phone;
    public String company;
    public String jobTitle;
    public String sexGender;
    public String address;
    public String city;
    public String country;
    public String password;
    public String userType;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userTitle, String surname, String firstName, String email,
                String phone, String company, String jobTitle, String sexGender,
                String address, String city, String country, String password, String userType) {
        this.title = userTitle;
        this.surname = surname;
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.jobTitle = jobTitle;
        this.sexGender = sexGender;
        this.address = address;
        this.city = city;
        this.country = country;
        this.password = password;
        this.userType = userType;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getSexGender() {
        return sexGender;
    }

    public void setSexGender(String sexGender) {
        this.sexGender = sexGender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
