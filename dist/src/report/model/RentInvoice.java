/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report.model;

import java.time.LocalDate;

/**
 *
 * @author Janidu
 */
public class RentInvoice {

    public RentInvoice() {

    }

    private String invoid;
    private String leaseId;
    private String date;
    private String customerName;
    private String mobile;
    private String barcode;
    private double outstanding;

    private double payment;
    private double balanceCarried;
    private String method;
    private String email;
    private double leaseAccountAmount;

    public String getInvoid() {
        return invoid;
    }

    public String getLeaseId() {
        return leaseId;
    }

    public String getDate() {
        return date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getBarcode() {
        return barcode;
    }

    public double getOutstanding() {
        return outstanding;
    }

//    public double getMonthRent() {
//        return monthRent;
//    }
//    public double getBillTotal() {
//        return Billtotal;
//    }
    public double getPayment() {
        return payment;
    }

    public double getBalanceCarried() {
        return balanceCarried;
    }

    public String getMethod() {
        return method;
    }

//    public String getPeriod() {
//        double paidNoMonth = getLeaseAccountAmount() / monthRent;
//        double payNomonth = payment / monthRent;
//
//        LocalDate startDate = LocalDate.parse(LeasStartDate);
//
//        // Adding 3 months to the startDate
//        LocalDate newDate = startDate.plusMonths((int) paidNoMonth);
//        LocalDate forwardDate = newDate.plusMonths((int) payNomonth);
//
//        return newDate + " to " + forwardDate;
//      
//    }
    public void setInvoid(String invoid) {
        this.invoid = invoid;
    }

    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setOutstanding(double outstanding) {
        this.outstanding = outstanding;
    }

//    public void setMonthRent(double monthRent) {
//        this.monthRent = monthRent;
//    }
//    public void setBillTotal(double total) {
//        this.Billtotal = total;
//    }
    public void setPayment(double payment) {
        this.payment = payment;
    }

    public void setBalanceCarried(double balanceCarried) {
        this.balanceCarried = balanceCarried;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setLeaseAccountAmount(double leaseAccountAmount) {
        this.leaseAccountAmount = leaseAccountAmount;
    }

//    public void setLeasStartDate(String LeasStartDate) {
//        this.LeasStartDate = LeasStartDate;
//    }
//    public void setPeriod(String period) {
//        this.period = period;
//    }
    public double getLeaseAccountAmount() {
        return leaseAccountAmount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
