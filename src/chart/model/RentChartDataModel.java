/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chart.model;

/**
 *
 * @author Janidu
 */
public class RentChartDataModel {
    private String Month;
    private double rentEarning;
  

    public RentChartDataModel(String Month, double rentEarning) {
        this.Month = Month;
        this.rentEarning = rentEarning;
       
       
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String Month) {
        this.Month = Month;
    }

    public double getRentEarning() {
        return rentEarning;
    }

    public void setRentEarning(double rentEarning) {
        this.rentEarning = rentEarning;
    }

  
  

   
}
