package pialeda.app.Invoice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="official_receipt_list_invoice")
public class OfficialReceiptInvoices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int officialReceiptNum;
    private String invoiceNo;
    private double invoiceAmount;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getOfficialReceiptNum() {
        return officialReceiptNum;
    }
    public void setOfficialReceiptNum(int officialReceiptNum) {
        this.officialReceiptNum = officialReceiptNum;
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
