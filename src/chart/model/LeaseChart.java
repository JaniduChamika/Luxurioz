/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chart.model;

/**
 *
 * @author Janidu
 */
public class LeaseChart {

    private String Month;
    private Integer monthNumLease;

    public LeaseChart(String Month, Integer monthNumLease) {
        this.Month = Month;
        this.monthNumLease = monthNumLease;
    }

   
  
    public String getMonth() {
        return Month;
    }

    public void setMonth(String Month) {
        this.Month = Month;
    }

    public Integer getMonthNumLease() {
        return monthNumLease;
    }

    public void setMonthNumLease(Integer monthNumLease) {
        this.monthNumLease = monthNumLease;
    }
    
}
