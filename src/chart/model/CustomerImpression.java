/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chart.model;

/**
 *
 * @author Janidu
 */
public class CustomerImpression {
    private String Month;
    private Integer customerNum;
  

    public CustomerImpression(String Month, Integer customerNum) {
        this.Month = Month;
        this.customerNum = customerNum;
       
       
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String Month) {
        this.Month = Month;
    }

    public Integer getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(Integer customerNum) {
        this.customerNum = customerNum;
    }

  
  
  

   
}
