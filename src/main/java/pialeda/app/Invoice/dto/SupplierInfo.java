package pialeda.app.Invoice.dto;

public class SupplierInfo {
    private String name;
    private String address;
    private String cityAddress;
    private String tin;
    private String secNum;
    private String secYear;
    private String atp;
    private String corNum;
    private String corDate;

    private double limit;

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityAddress() {
        return cityAddress;
    }

    public void setCityAddress(String cityAddress) {
        this.cityAddress = cityAddress;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getSecNum() {
        return secNum;
    }

    public void setSecNum(String secNum) {
        this.secNum = secNum;
    }

    public String getSecYear() {
        return secYear;
    }

    public void setSecYear(String secYear) {
        this.secYear = secYear;
    }

    public String getAtp() {
        return atp;
    }

    public void setAtp(String atp) {
        this.atp = atp;
    }

    public String getCorNum() {
        return corNum;
    }

    public void setCorNum(String corNum) {
        this.corNum = corNum;
    }

    public String getCorDate() {
        return corDate;
    }

    public void setCorDate(String corDate) {
        this.corDate = corDate;
    }
}
