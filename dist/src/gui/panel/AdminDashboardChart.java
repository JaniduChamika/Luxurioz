/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import chart.barchart.ModelBarChart;
import chart.model.CustomerImpression;
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
public class AdminDashboardChart extends javax.swing.JPanel {

    /**
     * Creates new form AdminDashboard
     */
    SimpleDateFormat year = new SimpleDateFormat("YYYY");
    SimpleDateFormat month = new SimpleDateFormat("MM");
    Date today = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("#,##0.00");

    public AdminDashboardChart() {
        initComponents();
        chart.addLegend("Revenue", new Color(7, 215, 255), new Color(7, 215, 255));
        customerChart.addLegend("Impression", new Color(255, 137, 0), new Color(255, 137, 0));
        barchart.addLegend("Complaint", new Color(135, 189, 245));
        barchart.addLegend("Repair Request", new Color(245, 189, 135));
        setDataRentChart(year.format(today));
        setDataCusImpressChart(year.format(today));
        GetNumcustomer();
        loadTotalRent();
        loadoutstandin();
        loadPiechart();
        loadBarChat(year.format(today));
//        loadBarChat("2023");
        loadTotalEpenditure();
    }

    private void loadTotalEpenditure() {
        double totalRent = 0.0;
        try {
            String query2 = "SELECT SUM(price) AS `total` FROM `item`";
            ResultSet sum = MySQL.search(query2);
            if (sum.next()) {
                totalRent = sum.getDouble("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jLabel11.setText("Rs " + df.format(totalRent));
    }

    public void setDataRentChart(String year) {
        chart.clear();
        chart.setTitle(year + " Rent Revenue");
        jLabel3.setText(year + " Rent Revenue");

        Map<Integer, Double> monthlyEarn = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            monthlyEarn.put((i + 1), 0.0);
        }

        try {
            String query = "SELECT * FROM `rent_invoice` WHERE `paydate` LIKE '" + year + "%'";
            ResultSet rs = MySQL.search(query);

            while (rs.next()) {
                Date paydate = rs.getDate("paydate");
                for (int i = 0; i < 12; i++) {
                    if (Integer.parseInt(month.format(paydate)) == (i + 1)) {

                        monthlyEarn.replace((i + 1), monthlyEarn.get((i + 1)) + rs.getDouble("amount"));

                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Month[] monthsArray = Month.values();

        ArrayList<RentChartDataModel> dm = new ArrayList<>();
        double totalYearErn = 0.0;
        for (int i = 0; i < 12; i++) {
//            if (monthlyEarn.get((i + 1)) != 0.0) {
            dm.add(new RentChartDataModel(StaticComponent.capitalizeFirstLetter(String.valueOf(monthsArray[i])), monthlyEarn.get((i + 1))));
            totalYearErn = totalYearErn + monthlyEarn.get((i + 1));
//            }
        }

        jLabel1.setText("Rs " + df.format(totalYearErn));

//        if (dm.size() > 1) {
        for (int i = 0; i < dm.size(); i++) {
            RentChartDataModel d = dm.get(i);
            chart.addData(new ModelChart(d.getMonth(), new double[]{d.getRentEarning()}));

        }
//        }
        chart.start();

        buttonEnable((Integer.parseInt(year) - 1), (Integer.parseInt(year) + 1));
    }

    private void loadTotalRent() {
        double totalRent = 0.0;
        try {
            String query2 = "SELECT SUM(amount) AS `total` FROM `rent_invoice`";
            ResultSet sum = MySQL.search(query2);
            if (sum.next()) {
                totalRent = sum.getDouble("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jLabel4.setText("Rs " + df.format(totalRent));
    }

    private void loadoutstandin() {
        try {
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM lease\n"
                    + "INNER JOIN lease_has_apartment \n"
                    + "ON lease.uniqeid= lease_has_apartment.lease_uniqeid\n"
                    + "INNER JOIN customer ON lease.customer_id=customer.cusid \n"
                    + "INNER JOIN lease_account ON lease_account.lease_uniqeid=lease.uniqeid\n"
                    + "INNER JOIN lease_status ON lease.lease_status_id=lease_status.id "
                    + "WHERE lease.lease_status_id='1'";

            rs = MySQL.search(query);
            double oustanding = 0.0;
            while (rs.next()) {
                LocalDate startDate = LocalDate.parse(rs.getString("lease.start_date"));
                LocalDate endDate = LocalDate.parse(sdf.format(new Date()));
                Period period = Period.between(startDate, endDate);

                int totalMonthDiffer = period.getYears() * 12 + period.getMonths();
                double leaseAccountAmount = Double.parseDouble(rs.getString("lease_account.current_balance"));
                double totalMonthRent = Double.parseDouble(rs.getString("lease.total_rate"));
                oustanding = oustanding + ((totalMonthRent * totalMonthDiffer) - leaseAccountAmount);
            }
            jLabel6.setText("Rs " + df.format(oustanding));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buttonEnable(int prev, int next) {
        try {
            ResultSet rs1 = MySQL.search("SELECT COUNT(paydate) as `count` FROM `rent_invoice` WHERE `paydate` LIKE '" + String.valueOf(prev) + "%'");
            rs1.next();
            if (Integer.parseInt(rs1.getString("count")) > 1) {
                jButton1.setEnabled(true);
            } else {
                jButton1.setEnabled(false);

            }
            ResultSet rs2 = MySQL.search("SELECT COUNT(paydate) as `count` FROM `rent_invoice` WHERE `paydate` LIKE '" + String.valueOf(next) + "%'");
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
            String query = "SELECT * FROM `apartment` ";
            ResultSet rs = MySQL.search(query);
            int vacant = 0;
            int occupied = 0;
            int maintain = 0;
            int sold = 0;
            while (rs.next()) {
                switch (rs.getInt("apartment_status_id")) {
                    case 1:
                        vacant++;
                        break;
                    case 2:
                        occupied++;
                        break;
                    case 3:
                        maintain++;
                        break;
                    case 4:
                        sold++;
                        break;
                    default:
                        System.out.println("error");
                }
            }
            pieChart1.addData(new ModelPieChart("Vacant", vacant, new Color(0, 239, 255)));
            pieChart1.addData(new ModelPieChart("Occupied ", occupied, new Color(0, 255, 10)));
            pieChart1.addData(new ModelPieChart("Maintain ", maintain, new Color(255, 128, 0)));
            pieChart1.addData(new ModelPieChart("Sold ", sold, new Color(128, 128, 128)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBarChat(String year) {
        barchart.clearData();
        jLabel9.setText(year + " Maintenance and complaint");

        try {
            Map<Integer, Double> complaint = new HashMap<>();
            for (int i = 0; i < 12; i++) {
                complaint.put((i + 1), 0.0);
            }
            String query = "SELECT * FROM `complaint` WHERE `report_date` LIKE '" + year + "%' ";
            ResultSet rs = MySQL.search(query);
            while (rs.next()) {
                Date reportdate = rs.getDate("report_date");
                for (int i = 0; i < 12; i++) {
                    if (Integer.parseInt(month.format(reportdate)) == (i + 1)) {

                        complaint.replace((i + 1), complaint.get((i + 1)) + 1);

                    }

                }

            }
            Map<Integer, Double> repairReq = new HashMap<>();
            for (int i = 0; i < 12; i++) {
                repairReq.put((i + 1), 0.0);
            }
            String query2 = "SELECT * FROM `repair_req` WHERE `report_date` LIKE '" + year + "%' ";
            ResultSet rs2 = MySQL.search(query2);
            while (rs2.next()) {
                Date reportdate = rs2.getDate("report_date");
                for (int i = 0; i < 12; i++) {
                    if (Integer.parseInt(month.format(reportdate)) == (i + 1)) {

                        repairReq.replace((i + 1), complaint.get((i + 1)) + 1);

                    }

                }

            }
            Month[] monthsArray = Month.values();
            for (int i = 0; i < 12; i++) {

                barchart.addData(new ModelBarChart(String.valueOf(monthsArray[i]), new double[]{repairReq.get((i + 1)), complaint.get((i + 1))}));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        buttonEnableBarChart((Integer.parseInt(year) - 1), (Integer.parseInt(year) + 1));
        barchart.revalidate();
        barchart.repaint();
    }

    private void buttonEnableBarChart(int prev, int next) {
        try {
            ResultSet rs1 = MySQL.search("SELECT COUNT(report_date) as `count` FROM `repair_req` WHERE `report_date` LIKE '" + String.valueOf(prev) + "%'");
            rs1.next();
            if (Integer.parseInt(rs1.getString("count")) > 1) {
                jButton6.setEnabled(true);
            } else {
                jButton6.setEnabled(false);

            }
            ResultSet rs2 = MySQL.search("SELECT COUNT(report_date) as `count` FROM `repair_req` WHERE `report_date` LIKE '" + String.valueOf(next) + "%'");
            rs2.next();

            if (Integer.parseInt(rs2.getString("count")) > 1) {
                jButton5.setEnabled(true);

            } else {
                jButton5.setEnabled(false);

            }
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
        jPanel4 = new gui.custom.RoundedCornerPanel(20);
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel5 = new gui.custom.RoundedCornerPanel(20);
        chart = new chart.raven.chart.CurveLineChart();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel6 = new gui.custom.RoundedCornerPanel(20);
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        card1 = new gui.component.Card();
        card2 = new gui.component.Card();
        jLabel25 = new javax.swing.JLabel();
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
        jPanel8 = new gui.custom.RoundedCornerPanel(20);
        barchart = new chart.barchart.Chart();
        jLabel9 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setOpaque(false);

        jPanel14.setOpaque(false);
        jPanel14.setPreferredSize(new java.awt.Dimension(1380, 930));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255,0));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1380, 930));

        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(1380, 1230));

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(customerChart, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
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
        jPanel3.setPreferredSize(new java.awt.Dimension(1340, 392));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel2.setText("Total Rent Revenue");

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel4.setText("Rs 0.00");

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel3.setText("2023 Rent Revenue");

        jLabel1.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel1.setText("Rs 0.00");

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel5.setText("Total Oustanding");

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel6.setText("Rs 0.00");

        jLabel10.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel10.setText("Total Expenditure");

        jLabel11.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel11.setText("Rs 0.00");

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-revenue-60.png"))); // NOI18N

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-receipt-60.png"))); // NOI18N

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-get-revenue-60.png"))); // NOI18N

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-money-bag-65.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                        .addGap(35, 35, 35))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                        .addGap(51, 51, 51))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(29, 29, 29))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4))
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1))
                    .addComponent(jLabel23))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6))
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11))
                    .addComponent(jLabel22))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                        .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 887, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
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
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel13.setText("Properties");

        jLabel14.setBackground(new java.awt.Color(128, 128, 128));
        jLabel14.setOpaque(true);

        jLabel15.setBackground(new java.awt.Color(0, 239, 255));
        jLabel15.setOpaque(true);

        jLabel16.setBackground(new java.awt.Color(0, 255, 10));
        jLabel16.setOpaque(true);

        jLabel17.setBackground(new java.awt.Color(255, 128, 0));
        jLabel17.setOpaque(true);

        jLabel18.setText("Sold");

        jLabel19.setText("Vacant");

        jLabel20.setText("Occupied");

        jLabel21.setText("Maintain");

        javax.swing.GroupLayout pieChart1Layout = new javax.swing.GroupLayout(pieChart1);
        pieChart1.setLayout(pieChart1Layout);
        pieChart1Layout.setHorizontalGroup(
            pieChart1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pieChart1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel19)
                .addGap(34, 34, 34)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel20)
                .addGap(24, 24, 24)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel21)
                .addGap(29, 29, 29)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel18)
                .addContainerGap(25, Short.MAX_VALUE))
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
                .addGap(18, 18, 18)
                .addComponent(jLabel13))
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

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel9.setText("Maintenance and complaint");

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-arrow-20 right.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-arrow-20 left.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(408, 408, 408)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(barchart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barchart, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(58, Short.MAX_VALUE))
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1252, Short.MAX_VALUE)
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
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 1252, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String title = chart.getTitle();
        String[] part = title.split(" ");
        String next = String.valueOf((Integer.parseInt(part[0]) + 1));
        setDataRentChart(next);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String title = chart.getTitle();
        String[] part = title.split(" ");
        String prev = String.valueOf((Integer.parseInt(part[0]) - 1));
        setDataRentChart(prev);
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

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String title = jLabel9.getText();
        String[] part = title.split(" ");
        String next = String.valueOf((Integer.parseInt(part[0]) + 1));

        loadBarChat(next);


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String title = jLabel9.getText();
        String[] part = title.split(" ");
        String prev = String.valueOf((Integer.parseInt(part[0]) - 1));

        loadBarChat(prev);

    }//GEN-LAST:event_jButton6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private chart.barchart.Chart barchart;
    private gui.component.Card card1;
    private gui.component.Card card2;
    private chart.raven.chart.CurveLineChart chart;
    private chart.raven.chart.CurveLineChart customerChart;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private chart.piechart.PieChart pieChart1;
    // End of variables declaration//GEN-END:variables
}
