/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdfwriter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Janidu
 */
public class LeaseDataModel {
    
    SimpleDateFormat day = new SimpleDateFormat("dd");
    SimpleDateFormat month = new SimpleDateFormat("MM");
    SimpleDateFormat year = new SimpleDateFormat("yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("dd / MM / yyyy");
    DecimalFormat df = new DecimalFormat("#,###.00");
    private Date regdate;
    private String customername;
    private String cusnic;
    private String apartid;
    private String startdate;
    private String enddate;
    private double totrent;
    private double secupayment;
    private String regday;
    private String regmonth;
    private String regyear;
    
    public LeaseDataModel(Date regdate, String customername, String cusnic, String apartid, Date startdate, Date enddate, double totrent, double secupayment) {
        this.regdate = regdate;
        this.customername = customername;
        this.cusnic = cusnic;
        this.apartid = apartid;
        this.startdate = sdf.format(startdate);
        this.enddate = sdf.format(enddate);
        this.totrent = totrent;
        this.secupayment = secupayment;
        this.regday = day.format(regdate);
        this.regmonth = month.format(regdate);
        this.regyear = year.format(regdate);
    }
    
    public Date getRegdate() {
        return regdate;
    }
    
    public String getCustomername() {
        return customername;
    }
    
    public String getCusnic() {
        return cusnic;
    }
    
    public String getApartid() {
        return apartid;
    }
    
    public String getStartdate() {
        return startdate;
    }
    
    public String getEnddate() {
        return enddate;
    }
    
    public String getTotrent() {
        return df.format(totrent);
    }
    
    public String getSecupayment() {
        return df.format(secupayment);
    }
    
    public String getRegday() {
        return regday;
    }
    
    public String getRegmonth() {
        return regmonth;
    }
    
    public String getRegyear() {
        return regyear;
    }
    
}
