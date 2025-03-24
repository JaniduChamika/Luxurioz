/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import chart.barchart.ModelBarChart;
import chart.model.CustomerImpression;
import chart.raven.chart.ModelChart;

import chart.piechart.ModelPieChart;
import java.awt.Color;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.time.Month;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import model.CustomTable;

import model.ModelCard;
import model.MySQL;
import model.StaticComponent;

/**
 *
 * @author Janidu
 */
public class ReciptDashboard extends javax.swing.JPanel {

    /**
     * Creates new form AdminDashboard
     */
    SimpleDateFormat year = new SimpleDateFormat("YYYY");
    SimpleDateFormat month = new SimpleDateFormat("MM");
    Date today = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("#,##0.00");

    public ReciptDashboard() {
        initComponents();
        customerChart.addLegend("Impression", new Color(255, 137, 0), new Color(255, 137, 0));
        barchart.addLegend("Repair Request", new Color(245, 189, 135));
        barchart.addLegend("Complaint", new Color(135, 189, 245));
        setDataCusImpressChart(year.format(today));
        GetNumcustomer();

        loadBarChat(year.format(today));
        CustomTable custable = new CustomTable();
        custable.modifyLayout(jTable1);
        loadAlert();
        loader();
    }
    SimpleDateFormat minite = new SimpleDateFormat("mm");

    private void loader() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    loadAlert();

                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();
    }

    private void loadAlert() {
        try {
            int count = 0;
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs;
            String query = "SELECT DISTINCT(apartment_id) AS app_id ,lock_status,`time`,`attempts` FROM door_lock \n"
                    + "INNER JOIN apartment_door_lock ON door_lock.app_id=apartment_door_lock.apartment_id "
                    + "WHERE (time = (SELECT MAX(time) FROM door_lock))";

            rs = MySQL.search(query);

            while (rs.next()) {

                int attempts = rs.getInt("attempts");
                if (attempts > 3) {
                    Vector v = new Vector();
                    v.add(rs.getString("app_id"));
                    v.add(attempts);
                    count++;
                    dtm.addRow(v);
                }

            }
            if (count > 0) {
                jLabel10.setForeground(Color.red);
                jLabel11.setIcon(new ImageIcon(getClass().getResource("/icon/animate/icons8-high-risk.gif")));
            } else {
                jLabel10.setForeground(Color.black);
                jLabel11.setIcon(new ImageIcon(getClass().getResource("/icon/animate/icons8-correct.gif")));

            }
            jLabel10.setText(String.valueOf(count));
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

            dm.add(new CustomerImpression(StaticComponent.capitalizeFirstLetter(String.valueOf(monthsArray[i])), cutomerNum.get((i + 1))));

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
        jPanel6 = new gui.custom.RoundedCornerPanel(20);
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        card1 = new gui.component.Card();
        card2 = new gui.component.Card();
        jLabel22 = new javax.swing.JLabel();
        jPanel7 = new gui.custom.RoundedCornerPanel(20);
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
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
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
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
                .addContainerGap(463, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(customerChart, javax.swing.GroupLayout.PREFERRED_SIZE, 906, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel7.setText("Total  Customers");

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel8.setText("0");

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-crowd-64.png"))); // NOI18N

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
                        .addComponent(jLabel22))
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
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel13.setText("Security Alert");

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Apartment ID", "Attempt"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jLabel10.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel10.setText("0");

        jLabel11.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/animate/icons8-correct.gif"))); // NOI18N
        jLabel11.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(16, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
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
                .addComponent(barchart, javax.swing.GroupLayout.PREFERRED_SIZE, 936, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel9))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(396, 396, 396)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(467, Short.MAX_VALUE))
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
    private chart.raven.chart.CurveLineChart customerChart;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
