package com.rakpak.pak_parak_admin.Model;

public class UsersList {
    private String MYID, Number, Username, profileimage, Date, Time;

    public UsersList(){

    }

    public UsersList(String MYID, String number, String username, String profileimage, String date, String time) {
        this.MYID = MYID;
        Number = number;
        Username = username;
        this.profileimage = profileimage;
        Date = date;
        Time = time;
    }

    public String getMYID() {
        return MYID;
    }

    public void setMYID(String MYID) {
        this.MYID = MYID;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
