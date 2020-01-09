package my.edu.tarc.fyp;

import java.util.ArrayList;

public class Appointment {

    private String appID,appStatus, appLocation, appDate, startTime, endTime, serviceID, custEmail, serviceImg;

    public Appointment() {
    }

    public Appointment(String appID, String appStatus, String appLocation, String appDate, String startTime, String endTime, String serviceID, String custEmail, String serviceImg) {
        this.appID = appID;
        this.appStatus = appStatus;
        this.appLocation = appLocation;
        this.appDate = appDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.serviceID = serviceID;
        this.custEmail = custEmail;
        this.serviceImg = serviceImg;
    }

    public Appointment(String appID, String appLocation, String appDate, String startTime, String endTime, String serviceID, String custEmail) {
        this.appID = appID;
        this.appLocation = appLocation;
        this.appDate = appDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.serviceID = serviceID;
        this.custEmail = custEmail;
    }

    public Appointment(String appID, String appStatus) {
        this.appID = appID;
        this.appStatus = appStatus;
    }

    public String getServiceImg() {
        return serviceImg;
    }

    public void setServiceImg(String serviceImg) {
        this.serviceImg = serviceImg;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getAppLocation() {
        return appLocation;
    }

    public void setAppLocation(String appLocation) {
        this.appLocation = appLocation;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public Appointment(AppointmentAdapter.ViewHolder viewHolder) {

    }


}
