package pialeda.app.Invoice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="collection_receipt")
public class CollectionReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String supplierName;
    private String supplierAddress;
    private int collectionReceiptNum;
    private String supplierTin;

    private double amountDue;
    private double ewt;
    private double total;

    private String cash;
    private String checkNo;
    private double amount;

    private String collectionReceiptDate;
    private String recvFrom;
    private String clientAddress;
    private String clientTin;
    private String clientBus;
    private String clientSumOf;
    private double clientPayment;
    private String partialPaymentFor;

    private String cashierName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCollectionReceiptNum() {
        return collectionReceiptNum;
    }

    public void setCollectionReceiptNum(int collectionReceiptNum) {
        this.collectionReceiptNum = collectionReceiptNum;
    }

    public String getSupplierTin() {
        return supplierTin;
    }

    public void setSupplierTin(String supplierTin) {
        this.supplierTin = supplierTin;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public double getEwt() {
        return ewt;
    }

    public void setEwt(double ewt) {
        this.ewt = ewt;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCollectionReceiptDate() {
        return collectionReceiptDate;
    }

    public void setCollectionReceiptDate(String collectionReceiptDate) {
        this.collectionReceiptDate = collectionReceiptDate;
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

    public double getClientPayment() {
        return clientPayment;
    }

    public void setClientPayment(double clientPayment) {
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
