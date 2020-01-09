package my.edu.tarc.fyp;

public class CalendarJob {

    private String appStatus, appLocation, appDate, startTime, custName, serviceName, appID, custImage;
    private double servicePrice;

    public CalendarJob(String appStatus, String appLocation, String appDate, String startTime, String custName, String serviceName, Double servicePrice, String appID, String custImage) {
        this.appStatus = appStatus;
        this.appLocation = appLocation;
        this.appDate = appDate;
        this.startTime = startTime;
        this.custName = custName;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.appID = appID;
        this.custImage = custImage;
    }

    public String getCustImage() {
        return custImage;
    }

    public void setCustImage(String custImage) {
        this.custImage = custImage;
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

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }
}
