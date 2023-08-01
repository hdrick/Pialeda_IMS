package pialeda.app.Invoice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;

@Entity
@Table(name="client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name="client_name")
    private String name;

    @Column(nullable = false, name="client_address")
    private String address;

    @Column(nullable = false, name="client_cityAddress")
    private String cityAddress;

    @Column(nullable = false, name="client_tin")
    private String tin;

    @Column(nullable = false, name="client_agent")
    private String agent;

    private String busStyle;
    private String terms;

    public Client() {

    }
    public Client(String name, String address, String cityAddress, String tin, String agent) {
        this.name = name;
        this.address = address;
        this.cityAddress = cityAddress;
        this.tin = tin;
        this.agent = agent;
    }
    

    public Client(String name, String address, String cityAddress, String tin, String agent, String busStyle,
            String terms) {
        this.name = name;
        this.address = address;
        this.cityAddress = cityAddress;
        this.tin = tin;
        this.agent = agent;
        this.busStyle = busStyle;
        this.terms = terms;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

}
