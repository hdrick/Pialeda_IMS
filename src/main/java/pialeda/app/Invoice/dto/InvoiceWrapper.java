package pialeda.app.Invoice.dto;

public class InvoiceWrapper {
    private InvoiceInfo invoiceInfo;
    private InvoiceProdInfo invoiceProdInfo;

    public InvoiceInfo getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(InvoiceInfo invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public InvoiceProdInfo getInvoiceProdInfo() {
        return invoiceProdInfo;
    }

    public void setInvoiceProdInfo(InvoiceProdInfo invoiceProdInfo) {
        this.invoiceProdInfo = invoiceProdInfo;
    }
}
