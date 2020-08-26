package com.abhi.vedanam;

import java.io.Serializable;

public class Realtimedata implements Serializable {

    String teacher_uid, teacher_name, coaching, subject , teacher_house, teacher_street, teacher_area,
            teacher_landmark, teacher_pincode, teacher_email, teacher_number;

    public Realtimedata() {
    }

    public String getTeacher_uid() {
        return teacher_uid;
    }

    public void setTeacher_uid(String teacher_uid) {
        this.teacher_uid = teacher_uid;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getCoaching() {
        return coaching;
    }

    public void setCoaching(String coaching) {
        this.coaching = coaching;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher_house() {
        return teacher_house;
    }

    public void setTeacher_house(String teacher_house) {
        this.teacher_house = teacher_house;
    }

    public String getTeacher_street() {
        return teacher_street;
    }

    public void setTeacher_street(String teacher_street) {
        this.teacher_street = teacher_street;
    }

    public String getTeacher_area() {
        return teacher_area;
    }

    public void setTeacher_area(String teacher_area) {
        this.teacher_area = teacher_area;
    }

    public String getTeacher_landmark() {
        return teacher_landmark;
    }

    public void setTeacher_landmark(String teacher_landmark) {
        this.teacher_landmark = teacher_landmark;
    }

    public String getTeacher_pincode() {
        return teacher_pincode;
    }

    public void setTeacher_pincode(String teacher_pincode) {
        this.teacher_pincode = teacher_pincode;
    }

    public String getTeacher_email() {
        return teacher_email;
    }

    public void setTeacher_email(String teacher_email) {
        this.teacher_email = teacher_email;
    }

    public String getTeacher_number() {
        return teacher_number;
    }

    public void setTeacher_number(String teacher_number) {
        this.teacher_number = teacher_number;
    }
}
