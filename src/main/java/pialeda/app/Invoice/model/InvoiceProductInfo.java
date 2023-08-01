package pialeda.app.Invoice.model;

import javax.persistence.*;

@Entity
@Table(name="invoice_Product_Info")
public class InvoiceProductInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String invoiceNumber;
    private String purchaseOrderNumber;

    private int qty;
    private String unit;
    private String articles;
    private double unitPrice;
    private double amount;

    public InvoiceProductInfo(){}

//    public InvoiceProductInfo(String invoiceNumber, String purchaseOrderNumber, int qty, String unit, String articles, double unitPrice, double amount) {
//        this.invoiceNumber = invoiceNumber;
//        this.purchaseOrderNumber = purchaseOrderNumber;
//        this.qty = qty;
//        this.unit = unit;
//        this.articles = articles;
//        this.unitPrice = unitPrice;
//        this.amount = amount;
//    }


    public InvoiceProductInfo(int id, String invoiceNumber, String purchaseOrderNumber, int qty, String unit, String articles, double unitPrice, double amount) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.qty = qty;
        this.unit = unit;
        this.articles = articles;
        this.unitPrice = unitPrice;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getArticles() {
        return articles;
    }

    public void setArticles(String articles) {
        this.articles = articles;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
