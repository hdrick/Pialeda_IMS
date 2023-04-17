package pialeda.app.Invoice.dto;

public class ClientInfo {
    private String name;
    private String address;
    private String cityAddress;
    private String tin;
    private String agent;
    private String busStyle;
    private String terms;

    
    public String getBusStyle() {
        return busStyle;
    }

    public void setBusStyle(String busStyle) {
        this.busStyle = busStyle;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
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

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}
