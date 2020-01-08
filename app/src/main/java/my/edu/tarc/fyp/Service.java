package my.edu.tarc.fyp;

import java.util.ArrayList;

public class Service {

    private String sID, sName, sDesc;
    private double sPrice;
    private double sDuration;

    public Service() {
    }

    public Service(String sID, String sName, String sDesc, double sPrice, double sDuration) {
        this.sID = sID;
        this.sName = sName;
        this.sDesc = sDesc;
        this.sPrice = sPrice;
        this.sDuration = sDuration;
    }

    public Service(String sName, String sDesc) {
        this.sName = sName;
        this.sDesc = sDesc;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsDesc() {
        return sDesc;
    }

    public void setsDesc(String sDesc) {
        this.sDesc = sDesc;
    }

    public double getsPrice() {
        return sPrice;
    }

    public void setsPrice(double sPrice) {
        this.sPrice = sPrice;
    }

    public double getsDuration() {
        return sDuration;
    }

    public void setsDuration(int sDuration) {
        this.sDuration = sDuration;
    }

    public Service(ServiceAdapter.ViewHolder viewHolder) {

    }

    public static ArrayList<Service> getData(int num) {
        ArrayList<Service> testData = new ArrayList<Service>();

        for (int i = 1; i <= num; i++) {
            testData.add(new Service("cutID","cut","cut cut desc",12.00,30));
        }

        return testData;
    }

}
