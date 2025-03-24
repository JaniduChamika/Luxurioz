/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report.model;

/**
 *
 * @author Janidu
 */
public class OtherInvoice {

    public OtherInvoice(String id, String date, String customer, String mobile, String email, String category, String total, String barcode, String payment, String balance, String method) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.category = category;
        this.mobile = mobile;
        this.total = total;
        this.barcode = barcode;
        this.payment = payment;
        this.balance = balance;
        this.method = method;
        this.email = email;
    }

    private String id;
    private String date;
    private String customer;
    private String category;
    private String mobile;
    private String total;
    private String barcode;
    private String payment;
    private String balance;
    private String method;
    private String email;

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getCustomer() {
        return customer;
    }

    public String getCategory() {
        return category;
    }

    public String getMobile() {
        return mobile;
    }

    public String getTotal() {
        return total;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getPayment() {
        return payment;
    }

    public String getBalance() {
        return balance;
    }

    public String getMethod() {
        return method;
    }

    public String getEmail() {
        return email;
    }
}
