package pialeda.app.Invoice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="current_invoice")
public class CurrentInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String invoiceNum;
    public CurrentInvoice() { }
    public CurrentInvoice(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getInvoiceNum() {
        return invoiceNum;
    }
    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }
    public static void findById(int i) {
    }
}
