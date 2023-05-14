package pialeda.app.Invoice.model;

import javax.persistence.*;

@Entity
@Table(name="collection_receipt_list_invoice")
public class CollectionReceiptInvoices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int collectionReceiptNum;
    // @OneToOne
    // @JoinColumn(name = "invoiceNum", referencedColumnName = "supplier_invoice_number")
    // private Invoice invoice;
    private String invoice;
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
