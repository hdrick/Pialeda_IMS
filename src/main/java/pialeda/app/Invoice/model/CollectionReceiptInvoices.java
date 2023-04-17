package pialeda.app.Invoice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="collection_receipt_list_invoice")
public class CollectionReceiptInvoices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int collectionReceiptNum;
    private String invoiceNo;
    private double invoiceAmount;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCollectionReceiptNum() {
        return collectionReceiptNum;
    }
    public void setCollectionReceiptNum(int collectionReceiptNum) {
        this.collectionReceiptNum = collectionReceiptNum;
    }
    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    public double getInvoiceAmount() {
        return invoiceAmount;
    }
    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    
}
