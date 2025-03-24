/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import java.awt.Color;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.table.DefaultTableModel;
import model.CustomTable;
import model.MySQL;
import model.StaticComponent;

/**
 *
 * @author Hiranya
 */
public class CSCust extends javax.swing.JPanel {

    /**
     * Creates new form CSCust
     */
    public CSCust() {
        initComponents();
        this.setBackground(new Color(255, 255, 255, 0));
        jPanel4.setBackground(new Color(255, 255, 255, 190));
        CustomTable custable = new CustomTable();
        custable.modifyLayout(jTable1);
        loadCounty();
        loadGender();
        loadstatus();
        loadCustomer();
    }

    Map<String, String> status = new HashMap<>();

    public void loadstatus() {
        try {

            Vector v = new Vector();
            v.add("Select Status");
            ResultSet rs = MySQL.search("SELECT * FROM cus_status");
            while (rs.next()) {
                v.add(rs.getString("name"));
                status.put(rs.getString("name"), rs.getString("id"));
            }
            jComboBox5.setModel(new DefaultComboBoxModel<>(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Map<String, String> gender = new HashMap<>();

    public void loadGender() {
        try {

            Vector v = new Vector();
            v.add("Select Gender");
            ResultSet rs = MySQL.search("SELECT * FROM gender ");
            while (rs.next()) {
                v.add(rs.getString("name"));
                gender.put(rs.getString("name"), rs.getString("id"));
            }
            jComboBox3.setModel(new DefaultComboBoxModel<>(v));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    Map<String, String> country = new HashMap<>();

    public void loadCounty() {
        try {

            Vector v = new Vector();
            v.add("Select Country");
            ResultSet rs = MySQL.search("SELECT * FROM country ");
            while (rs.next()) {
                v.add(rs.getString("name"));
                country.put(rs.getString("name"), rs.getString("id"));
            }
            jComboBox4.setModel(new DefaultComboBoxModel<>(v));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void loadCustomer() {
        String gender = jComboBox3.getSelectedItem().toString();
        String country = jComboBox4.getSelectedItem().toString();
        String status = jComboBox5.getSelectedItem().toString();
        String text = jTextField3.getText();

        try {

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM customer\n"
                    + "INNER JOIN country ON customer.country_id=country.id\n"
                    + "INNER JOIN gender ON customer.gender_id=gender.id\n"
                    + "INNER JOIN `cus_status` ON customer.cus_status_id=`cus_status`.id "
                    + "INNER JOIN marital_status ON customer.marital_id=marital_status.id "
                    + "WHERE  (`fname` LIKE '" + text + "%' OR `nic` LIKE '" + text + "%' OR `mobile` LIKE '%" + text + "%' OR `email` LIKE '" + text + "%' OR `lname` LIKE '" + text + "%')";

            if (status != "Select Status") {
                query = query + " AND cus_status.name='" + status + "'";
            }
            if (gender != "Select Gender") {
                query = query + " AND `gender`.`name` = '" + gender + "' ";

            }
            if (country != "Select Country") {
                query = query + " AND `country`.`name` = '" + country + "' ";

            }
            rs = MySQL.search(query);
            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("nic"));
                v.add(rs.getString("fname") + " " + rs.getString("lname"));
                v.add(rs.getString("mobile"));
                v.add(rs.getString("email"));
                v.add(rs.getString("dob"));
                v.add(rs.getString("job_title"));
                v.add(rs.getString("gender.name"));
                v.add(rs.getString("country.name"));
                v.add(rs.getString("marital_status.name"));
                v.add(rs.getString("cus_status.name"));
                dtm.addRow(v);
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

        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();

        jPanel4.setPreferredSize(new java.awt.Dimension(1380, 930));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel4.setText("Customer Info");

        jLabel5.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel5.setText("Search");

        jTextField3.setBackground(new java.awt.Color(255, 255, 255));
        jTextField3.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NIC", "Name", "Mobile", "Email", "DOB", "Job Title", "Gender", "Country", "Marital", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jComboBox3.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Gender", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jComboBox4.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Country", "Item 2", "Item 3", "Item 4" }));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        jComboBox5.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Status", "Item 2", "Item 3", "Item 4" }));
        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(407, 407, 407)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1340, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 821, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        loadCustomer();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        loadCustomer();

    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
        loadCustomer();

    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadCustomer();

    }//GEN-LAST:event_jTextField3KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
