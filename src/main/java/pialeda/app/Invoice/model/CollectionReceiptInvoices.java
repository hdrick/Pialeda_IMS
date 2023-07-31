package pialeda.app.Invoice.model;

import javax.persistence.*;

@Entity
@Table(name="collection_receipt_list_invoice")
public class CollectionReceiptInvoices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long collectionReceiptNum;
    // @OneToOne
    // @JoinColumn(name = "invoiceNum", referencedColumnName = "supplier_invoice_number")
    // private Invoice invoice;
    private String invoice;
    private double invoiceAmount;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getCollectionReceiptNum() {
        return collectionReceiptNum;
    }
    public void setCollectionReceiptNum(long collectionReceiptNum) {
        this.collectionReceiptNum = collectionReceiptNum;
    }
    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
    public double getInvoiceAmount() {
        return invoiceAmount;
    }
    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    
}
