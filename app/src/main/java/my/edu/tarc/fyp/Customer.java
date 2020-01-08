package my.edu.tarc.fyp;

public class Customer {

    private String custEmail, custPassword, custName, custAddress, custPhoneNum, custImg, custGender;
    private double custRating;

    public Customer() {
    }

    public Customer(String custEmail, String custPassword, String custName, String custAddress, String custPhoneNum, String custImg, String custGender, double custRating) {
        this.custEmail = custEmail;
        this.custPassword = custPassword;
        this.custName = custName;
        this.custAddress = custAddress;
        this.custPhoneNum = custPhoneNum;
        this.custImg = custImg;
        this.custGender = custGender;
        this.custRating = custRating;
    }

    public Customer(String custEmail, String custName) {
        this.custEmail = custEmail;
        this.custName = custName;
    }

    public Customer(String custEmail, String custName,String custImg) {
        this.custEmail = custEmail;
        this.custName = custName;
        this.custImg = custImg;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustPassword() {
        return custPassword;
    }

    public void setCustPassword(String custPassword) {
        this.custPassword = custPassword;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustPhoneNum() {
        return custPhoneNum;
    }

    public void setCustPhoneNum(String custPhoneNum) {
        this.custPhoneNum = custPhoneNum;
    }

    public String getCustImg() {
        return custImg;
    }

    public void setCustImg(String custImg) {
        this.custImg = custImg;
    }

    public String getCustGender() {
        return custGender;
    }

    public void setCustGender(String custGender) {
        this.custGender = custGender;
    }

    public double getCustRating() {
        return custRating;
    }

    public void setCustRating(double custRating) {
        this.custRating = custRating;
    }
}
