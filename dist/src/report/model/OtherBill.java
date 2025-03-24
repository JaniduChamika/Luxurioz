/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report.model;

/**
 *
 * @author Janidu
 */
public class OtherBill {

    public OtherBill(String id, String date, String customer, String mobile, String email, String reason, String category, String barcode, String total) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.mobile = mobile;
        this.reason = reason;
        this.category = category;
        this.barcode = barcode;
        this.total = total;
        this.email = email;
    }

    public OtherBill(String id, String customer, String mobile,String email, String category) {
        this.id = id;
        this.customer = customer;
        this.mobile = mobile;
        this.category = category;
        this.email = email;
    }
    private String id;
    private String date;
    private String customer;
    private String reason;
    private String category;
    private String mobile;
    private String barcode;
    private String total;
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

    public String getReason() {
        return reason;
    }

    public String getCategory() {
        return category;
    }

    public String getMobile() {
        return mobile;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getTotal() {
        return total;
    }

    public String getEmail() {
        return email;
    }
}
