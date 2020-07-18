package com.ruswives.rwadmin.model;

public class FsUser {


    private String name;
    private String age;
    private String country;
    private String image;
    private String status;
    private String thumb_image;
    private String uid;
    private String gender;
    private String relationship_status;
    private String body_type;
    private String ethnicity;
    private String faith;
    private String smoking_status;
    private String number_of_kids;
    private String want_children_or_not;
    private String hobby;
    private String drink_status;
    private int rating;

    private String must_info;
    private String full_profile;
    private String id_soc;

    public String getFull_profile() {
        return full_profile;
    }

    public void setFull_profile(String full_profile) {
        this.full_profile = full_profile;
    }

    public String getMust_info() {
        return must_info;
    }

    public void setMust_info(String must_info) {
        this.must_info = must_info;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDrink_status() {
        return drink_status;
    }

    public void setDrink_status(String drink_status) {
        this.drink_status = drink_status;
    }

    public FsUser(String name, String age, String country, String drink_status,
                  String image, String status, String thumb_image, String uid,
                  String gender, String relationship_status, String body_type,
                  String ethnicity, String faith, String smoking_status,
                  String number_of_kids, String want_children_or_not,
                  String hobby, int rating, String id_soc) {
        this.name = name;
        this.age = age;
        this.country = country;
        this.drink_status = drink_status;
        this.image = image;
        this.status = status;
        this.thumb_image = thumb_image;
        this.uid = uid;
        this.gender = gender;
        this.relationship_status = relationship_status;
        this.body_type = body_type;
        this.ethnicity = ethnicity;
        this.faith = faith;
        this.smoking_status = smoking_status;
        this.number_of_kids = number_of_kids;
        this.want_children_or_not = want_children_or_not;
        this.hobby = hobby;
        this.rating = rating;
        this.id_soc = id_soc;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationship_status() {
        return relationship_status;
    }

    public void setRelationship_status(String relationship_status) {
        this.relationship_status = relationship_status;
    }

    public String getBody_type() {
        return body_type;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getFaith() {
        return faith;
    }

    public void setFaith(String faith) {
        this.faith = faith;
    }

    public String getSmoking_status() {
        return smoking_status;
    }

    public void setSmoking_status(String smoking_status) {
        this.smoking_status = smoking_status;
    }

    public String getNumber_of_kids() {
        return number_of_kids;
    }

    public void setNumber_of_kids(String number_of_kids) {
        this.number_of_kids = number_of_kids;
    }

    public String getWant_children_or_not() {
        return want_children_or_not;
    }

    public void setWant_children_or_not(String want_children_or_not) {
        this.want_children_or_not = want_children_or_not;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public FsUser(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getId_soc() {
        return id_soc;
    }

    public void setId_soc(String id_soc) {
        this.id_soc = id_soc;
    }

    @Override
    public String toString() {
        return "name=" + name  +
                ", age=" + age +
                ", country=" + country+
                ", image=" + image +
                ", status=" + status +
                ", thumb_image=" + thumb_image +
                ", uid=" + uid +
                ", gender=" + gender +
                ", relationship_status=" + relationship_status+
                ", body_type=" + body_type +
                ", ethnicity=" + ethnicity +
                ", faith=" + faith +
                ", smoking_status=" + smoking_status +
                ", number_of_kids=" + number_of_kids  +
                ", want_children_or_not=" + want_children_or_not  +
                ", hobby=" + hobby  +
                ", drink_status=" + drink_status +
                ", rating=" + rating +
                ", must_info=" + must_info  +
                ", full_profile=" + full_profile;
    }
}
