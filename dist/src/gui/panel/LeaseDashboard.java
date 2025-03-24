/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import chart.barchart.ModelBarChart;
import chart.model.CustomerImpression;
import chart.model.LeaseChart;
import chart.raven.chart.ModelChart;
import chart.model.RentChartDataModel;
import chart.piechart.ModelPieChart;
import java.awt.Color;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import model.ModelCard;
import model.MySQL;
import model.StaticComponent;

/**
 *
 * @author Janidu
 */
public class LeaseDashboard extends javax.swing.JPanel {

    /**
     * Creates new form AdminDashboard
     */
    SimpleDateFormat year = new SimpleDateFormat("YYYY");
    SimpleDateFormat month = new SimpleDateFormat("MM");
    Date today = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("#,##0.00");

    public LeaseDashboard() {
        initComponents();
        chart.addLegend("Lease", new Color(7, 215, 255), new Color(7, 215, 255));
        customerChart.addLegend("Impression", new Color(255, 137, 0), new Color(255, 137, 0));
        setDataLeaseChart(year.format(today));
        setDataCusImpressChart(year.format(today));
        GetNumcustomer();

        loadPiechart();

    }

    public void setDataLeaseChart(String year) {
        chart.clear();
        chart.setTitle(year + " Lease Registration");

        Map<Integer, Integer> monthlyLease = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            monthlyLease.put((i + 1), 0);
        }

        try {
            String query = "SELECT * FROM `lease` WHERE `reg_date` LIKE '" + year + "%'";
            ResultSet rs = MySQL.search(query);

            while (rs.next()) {
                Date paydate = rs.getDate("reg_date");
                for (int i = 0; i < 12; i++) {
                    if (Integer.parseInt(month.format(paydate)) == (i + 1)) {

                        monthlyLease.replace((i + 1), monthlyLease.get((i + 1)) + 1);

                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Month[] monthsArray = Month.values();

        ArrayList<LeaseChart> dm = new ArrayList<>();

        for (int i = 0; i < 12; i++) {

            dm.add(new LeaseChart(StaticComponent.capitalizeFirstLetter(String.valueOf(monthsArray[i])), monthlyLease.get((i + 1))));

        }

        for (int i = 0; i < dm.size(); i++) {
            LeaseChart d = dm.get(i);
            chart.addData(new ModelChart(d.getMonth(), new double[]{d.getMonthNumLease()}));

        }

        chart.start();

        buttonEnable((Integer.parseInt(year) - 1), (Integer.parseInt(year) + 1));
    }

    private void buttonEnable(int prev, int next) {
        try {
            ResultSet rs1 = MySQL.search("SELECT COUNT(reg_date) as `count` FROM `lease` WHERE `reg_date` LIKE '" + String.valueOf(prev) + "%'");
            rs1.next();
            if (Integer.parseInt(rs1.getString("count")) > 1) {
                jButton1.setEnabled(true);
            } else {
                jButton1.setEnabled(false);

            }
            ResultSet rs2 = MySQL.search("SELECT COUNT(reg_date) as `count` FROM `lease` WHERE `reg_date` LIKE '" + String.valueOf(next) + "%'");
            rs2.next();

            if (Integer.parseInt(rs2.getString("count")) > 1) {
                jButton2.setEnabled(true);

            } else {
                jButton2.setEnabled(false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDataCusImpressChart(String year) {
        customerChart.clear();
        customerChart.setTitle(year + " Customer Impression");

        Map<Integer, Integer> cutomerNum = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            cutomerNum.put((i + 1), 0);
        }
        try {
            String query = "SELECT * FROM `customer` WHERE `reg_date` LIKE '" + year + "%'";
            ResultSet rs = MySQL.search(query);

            while (rs.next()) {
                Date regDate = rs.getDate("reg_date");
                for (int i = 0; i < 12; i++) {
                    if (Integer.parseInt(month.format(regDate)) == (i + 1)) {

                        cutomerNum.replace((i + 1), cutomerNum.get((i + 1)) + 1);

                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Month[] monthsArray = Month.values();

        ArrayList<CustomerImpression> dm = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
//            if (monthlyEarn.get((i + 1)) != 0.0) {
            dm.add(new CustomerImpression(StaticComponent.capitalizeFirstLetter(String.valueOf(monthsArray[i])), cutomerNum.get((i + 1))));
//            }
        }

        if (dm.size() > 1) {
            for (int i = 0; i < dm.size(); i++) {
                CustomerImpression d = dm.get(i);
                customerChart.addData(new ModelChart(d.getMonth(), new double[]{d.getCustomerNum()}));

            }
        }
        customerChart.start();

        buttonEnableCustomer((Integer.parseInt(year) - 1), (Integer.parseInt(year) + 1));
    }

    private void buttonEnableCustomer(int prev, int next) {
        try {
            ResultSet rs1 = MySQL.search("SELECT COUNT(cusid) as `count` FROM `customer` WHERE `reg_date` LIKE '" + String.valueOf(prev) + "%'");
            rs1.next();
            if (Integer.parseInt(rs1.getString("count")) > 1) {
                jButton3.setEnabled(true);
            } else {
                jButton3.setEnabled(false);

            }
            ResultSet rs2 = MySQL.search("SELECT COUNT(cusid) as `count` FROM `customer` WHERE `reg_date` LIKE '" + String.valueOf(next) + "%'");
            rs2.next();

            if (Integer.parseInt(rs2.getString("count")) > 1) {
                jButton4.setEnabled(true);

            } else {
                jButton4.setEnabled(false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetNumcustomer() {
        try {
            String query = "SELECT COUNT(cusid) AS `total` FROM `customer` ";
            String query2 = "SELECT COUNT( DISTINCT(customer_id) ) AS `active_cus` FROM `lease` WHERE `lease_status_id` IN ('4','1') ";
            String query3 = "SELECT COUNT(id) AS `vacant` FROM `apartment`  ";
            String query4 = "SELECT COUNT(id) AS `vacant` FROM `apartment` WHERE `apartment_status_id`='1' ";
            ResultSet rs = MySQL.search(query);
            int activeCus = 0;
            int apartment = 0;
            int vacantapartment = 0;
            if (rs.next()) {
                jLabel8.setText(rs.getString("total"));
            }
            ResultSet rs2 = MySQL.search(query2);
            if (rs2.next()) {
                activeCus = rs2.getInt("active_cus");

            }
            ResultSet rs3 = MySQL.search(query3);

            if (rs3.next()) {
                apartment = rs3.getInt("vacant");

            }
            ResultSet rs4 = MySQL.search(query4);

            if (rs4.next()) {
                vacantapartment = rs4.getInt("vacant");

            }
            int percentage = (activeCus * 100) / apartment;
            Icon ap = new ImageIcon((getClass().getResource("/icon/dash-apartment48.png")));
            Icon cus = new ImageIcon((getClass().getResource("/icon/dash-cus-48.png")));
            card1.setData(new ModelCard("Active Customers", activeCus, percentage, cus));
            card2.setData(new ModelCard("Vacant Apartment", vacantapartment, (vacantapartment * 100) / apartment, ap));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPiechart() {
        try {
            String query = "SELECT * FROM `lease` ";
            ResultSet rs = MySQL.search(query);
            int active = 0;
            int complete = 0;
            int close = 0;
            int pending = 0;
            while (rs.next()) {
                switch (rs.getInt("lease_status_id")) {
                    case 1:
                        active++;
                        break;
                    case 2:
                        complete++;
                        break;
                    case 3:
                        close++;
                        break;
                    case 4:
                        pending++;
                        break;
                    default:
                        System.out.println("error");
                }
            }
            jLabel13.setText("Total Lease :- " + (active + complete + close + pending));
            pieChart1.addData(new ModelPieChart("Active", active, new Color(0, 239, 255)));
            pieChart1.addData(new ModelPieChart("Complete ", complete, new Color(0, 255, 10)));
            pieChart1.addData(new ModelPieChart("Close ", close, new Color(255, 128, 0)));
            pieChart1.addData(new ModelPieChart("Pending ", pending, new Color(128, 128, 128)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel14 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new gui.custom.RoundedCornerPanel(20);
        customerChart = new chart.raven.chart.CurveLineChart();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new gui.custom.RoundedCornerPanel(20);
        chart = new chart.raven.chart.CurveLineChart();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel7 = new gui.custom.RoundedCornerPanel(20);
        jLabel13 = new javax.swing.JLabel();
        pieChart1 = new chart.piechart.PieChart();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel6 = new gui.custom.RoundedCornerPanel(20);
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        card1 = new gui.component.Card();
        card2 = new gui.component.Card();
        jLabel25 = new javax.swing.JLabel();

        setOpaque(false);

        jPanel14.setOpaque(false);
        jPanel14.setPreferredSize(new java.awt.Dimension(1380, 930));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255,0));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1380, 822));

        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(1380, 821));

        customerChart.setBackground(new java.awt.Color(0, 153, 153));
        customerChart.setForeground(new java.awt.Color(0, 0, 0));
        customerChart.setColor1(new java.awt.Color(102, 102, 102));
        customerChart.setColor2(new java.awt.Color(51, 51, 51));
        customerChart.setFillColor(true);
        customerChart.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        customerChart.setTitleFont(new java.awt.Font("Roboto", 1, 15)); // NOI18N

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-arrow-20 left.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-arrow-20 right.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(408, 408, 408)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addContainerGap(443, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(customerChart, javax.swing.GroupLayout.PREFERRED_SIZE, 883, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(customerChart, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(1340, 395));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        chart.setBackground(new java.awt.Color(0, 153, 153));
        chart.setForeground(new java.awt.Color(0, 0, 0));
        chart.setColor1(new java.awt.Color(102, 102, 102));
        chart.setColor2(new java.awt.Color(51, 51, 51));
        chart.setFillColor(true);
        chart.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        chart.setTitleFont(new java.awt.Font("Roboto", 1, 15)); // NOI18N

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-arrow-20 left.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-arrow-20 right.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addGap(405, 405, 405))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel13.setText("Lease");

        jLabel14.setBackground(new java.awt.Color(128, 128, 128));
        jLabel14.setOpaque(true);

        jLabel15.setBackground(new java.awt.Color(0, 239, 255));
        jLabel15.setOpaque(true);

        jLabel16.setBackground(new java.awt.Color(0, 255, 10));
        jLabel16.setOpaque(true);

        jLabel17.setBackground(new java.awt.Color(255, 128, 0));
        jLabel17.setOpaque(true);

        jLabel18.setText("Pending");

        jLabel19.setText("Active");

        jLabel20.setText("Complete");

        jLabel21.setText("Close");

        javax.swing.GroupLayout pieChart1Layout = new javax.swing.GroupLayout(pieChart1);
        pieChart1.setLayout(pieChart1Layout);
        pieChart1Layout.setHorizontalGroup(
            pieChart1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pieChart1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel20)
                .addGap(30, 30, 30)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );
        pieChart1Layout.setVerticalGroup(
            pieChart1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pieChart1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pieChart1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pieChart1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(pieChart1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel7.setText("Total  Customers");

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel8.setText("0");

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-crowd-64.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addGap(93, 93, 93)
                        .addComponent(jLabel25))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 43, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8))
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        jScrollPane1.getViewport().setOpaque(false);
        jScrollPane1.setOpaque(false);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 821, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        //jScrollPane1.setBorder(BorderFactory.createEmptyBorder());

        jScrollPane1.setViewportBorder(null);
        jScrollPane1.setBorder(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 1382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 821, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String title = chart.getTitle();
        String[] part = title.split(" ");
        String next = String.valueOf((Integer.parseInt(part[0]) + 1));
        setDataLeaseChart(next);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String title = chart.getTitle();
        String[] part = title.split(" ");
        String prev = String.valueOf((Integer.parseInt(part[0]) - 1));
        setDataLeaseChart(prev);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String title = customerChart.getTitle();
        String[] part = title.split(" ");
        String prev = String.valueOf((Integer.parseInt(part[0]) - 1));
        setDataCusImpressChart(prev);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String title = customerChart.getTitle();
        String[] part = title.split(" ");
        String next = String.valueOf((Integer.parseInt(part[0]) + 1));
        setDataCusImpressChart(next);
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.component.Card card1;
    private gui.component.Card card2;
    private chart.raven.chart.CurveLineChart chart;
    private chart.raven.chart.CurveLineChart customerChart;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private chart.piechart.PieChart pieChart1;
    // End of variables declaration//GEN-END:variables
}
