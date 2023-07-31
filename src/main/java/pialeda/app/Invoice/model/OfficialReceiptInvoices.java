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
    private long id;

    private long officialReceiptNum;
    private String invoiceNo;
    private double invoiceAmount;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getOfficialReceiptNum() {
        return officialReceiptNum;
    }
    public void setOfficialReceiptNum(long officialReceiptNum) {
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
