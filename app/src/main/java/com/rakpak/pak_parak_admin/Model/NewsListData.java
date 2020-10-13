package com.rakpak.pak_parak_admin.Model;

public class NewsListData {
    private String CurrentDate, CurrentTime, jobimage, message;

    public NewsListData(){

    }

    public NewsListData(String currentDate, String currentTime, String jobimage, String message) {
        CurrentDate = currentDate;
        CurrentTime = currentTime;
        this.jobimage = jobimage;
        this.message = message;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        CurrentTime = currentTime;
    }

    public String getJobimage() {
        return jobimage;
    }

    public void setJobimage(String jobimage) {
        this.jobimage = jobimage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
