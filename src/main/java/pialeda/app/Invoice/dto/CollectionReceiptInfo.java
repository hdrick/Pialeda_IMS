package pialeda.app.Invoice.dto;

public class CollectionReceiptInfo {
    private String supplierName;
    private String supplierAddress;
    private String supplierTin;

    private String officialReceiptDate;
    private String recvFrom;
    private String clientAddress;
    private String clientTin;
    private String clientBus;
    private String clientSumOf;
    private String clientPayment;
    private String partialPaymentFor;

    private String cashierName;

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierTin() {
        return supplierTin;
    }

    public void setSupplierTin(String supplierTin) {
        this.supplierTin = supplierTin;
    }

    public String getOfficialReceiptDate() {
        return officialReceiptDate;
    }

    public void setOfficialReceiptDate(String officialReceiptDate) {
        this.officialReceiptDate = officialReceiptDate;
    }

    public String getRecvFrom() {
        return recvFrom;
    }

    public void setRecvFrom(String recvFrom) {
        this.recvFrom = recvFrom;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientTin() {
        return clientTin;
    }

    public void setClientTin(String clientTin) {
        this.clientTin = clientTin;
    }

    public String getClientBus() {
        return clientBus;
    }

    public void setClientBus(String clientBus) {
        this.clientBus = clientBus;
    }

    public String getClientSumOf() {
        return clientSumOf;
    }

    public void setClientSumOf(String clientSumOf) {
        this.clientSumOf = clientSumOf;
    }

    public String getClientPayment() {
        return clientPayment;
    }

    public void setClientPayment(String clientPayment) {
        this.clientPayment = clientPayment;
    }

    public String getPartialPaymentFor() {
        return partialPaymentFor;
    }

    public void setPartialPaymentFor(String partialPaymentFor) {
        this.partialPaymentFor = partialPaymentFor;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    
}
