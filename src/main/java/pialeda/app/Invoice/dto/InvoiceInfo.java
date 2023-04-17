package pialeda.app.Invoice.dto;

import java.time.LocalDate;

public class InvoiceInfo {
    private String invoiceNum;
    private String poNum;
    private LocalDate dateCreated;
    private String clientContactPerson;
    private String supplierName;
    private String supplierAddress;
    private String supplierTin;

    private String clientName;
    private String clientTin;
    private String clientAddress;
    private String clientBusStyle;
    private String clientTerms;

    private double grandTotal;
    private double addVat;
    private double amountNetOfVat;
    private double totalSalesVatInc;
    private String cashier;

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getPoNum() {
        return poNum;
    }

    public void setPoNum(String poNum) {
        this.poNum = poNum;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getClientContactPerson() {
        return clientContactPerson;
    }

    public void setClientContactPerson(String clientContactPerson) {
        this.clientContactPerson = clientContactPerson;
    }

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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientTin() {
        return clientTin;
    }

    public void setClientTin(String clientTin) {
        this.clientTin = clientTin;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientBusStyle() {
        return clientBusStyle;
    }

    public void setClientBusStyle(String clientBusStyle) {
        this.clientBusStyle = clientBusStyle;
    }

    public String getClientTerms() {
        return clientTerms;
    }

    public void setClientTerms(String clientTerms) {
        this.clientTerms = clientTerms;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public double getAddVat() {
        return addVat;
    }

    public void setAddVat(double addVat) {
        this.addVat = addVat;
    }

    public double getAmountNetOfVat() {
        return amountNetOfVat;
    }

    public void setAmountNetOfVat(double amountNetOfVat) {
        this.amountNetOfVat = amountNetOfVat;
    }

    public double getTotalSalesVatInc() {
        return totalSalesVatInc;
    }

    public void setTotalSalesVatInc(double totalSalesVatInc) {
        this.totalSalesVatInc = totalSalesVatInc;
    }
}