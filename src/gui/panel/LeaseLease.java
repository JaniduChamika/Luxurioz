/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import gui.dialogbox.LeaseRegistration;
import java.awt.Color;
import java.io.File;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import model.CustomButtonUI;
import model.CustomTable;
import model.CustomeMouseAdapter;
import model.LoadingIndicator;
import model.MySQL;
import model.StaticComponent;
import pdfwriter.LeaseDataModel;
import pdfwriter.PdfWriter;

/**
 *
 * @author Hiranya
 */
public class LeaseLease extends javax.swing.JPanel {

    /**
     * Creates new form LeaseLease
     */
    JFrame parent;
    DecimalFormat df = new DecimalFormat("#,##0.00");

    public LeaseLease(JFrame parent) {
        initComponents();
        this.parent = parent;
        this.setBackground(new Color(255, 255, 255, 0));
        jPanel4.setBackground(new Color(255, 255, 255, 190));

        CustomTable custable = new CustomTable();
        custable.modifyLayout(jTable1);

        startLisners();
        setNewButtonUI();
        loadStatus();
        loadLease();
    }

    private void setNewButtonUI() {

        jButton6.setUI(new CustomButtonUI(0));

    }

    private void startLisners() {

        // Add a MouseListener to the button
        jButton6.addMouseListener(new CustomeMouseAdapter(jButton6));

    }

    public void viewLeaseReg() {
        LeaseRegistration lr = new LeaseRegistration(parent, true, this);
        lr.setVisible(true);
    }

    public void viewLeaseUgrade() {
        String leaseId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

        LeaseRegistration lr = new LeaseRegistration(parent, true, this, leaseId);
        lr.setTitle(leaseId + " Lease Upgrade");
        lr.jButton1.setText("Upgrade");
        lr.setVisible(true);
    }

    public void viewLeaseDowngrade() {
        String leaseId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

        LeaseRegistration lr = new LeaseRegistration(parent, true, this, leaseId);
        lr.setTitle(leaseId + " Lease Downgrade");
        lr.jButton1.setText("Downgrade");
        lr.setVisible(true);
    }

    public void loadStatus() {
        try {

            String query = "SELECT * FROM `lease_status`";

            ResultSet rs = MySQL.search(query);

            Vector v = new Vector();
            v.add("Select Status");
            while (rs.next()) {
                v.add(rs.getString("name"));

            }

            jComboBox4.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadLease() {

        String status = jComboBox4.getSelectedItem().toString();
        String text = jTextField3.getText();

        try {

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM lease\n"
                    + "INNER JOIN customer ON lease.customer_id=customer.cusid\n"
                    + "INNER JOIN lease_has_apartment ON lease.uniqeid=lease_has_apartment.lease_uniqeid \n"
                    + "INNER JOIN apartment ON lease_has_apartment.apartment_id=apartment.id\n"
                    + "INNER JOIN `lease_status` ON lease.lease_status_id=`lease_status`.id "
                    + "WHERE (customer.nic LIKE '" + text + "%' OR customer.fname LIKE '" + text + "%' "
                    + "OR customer.lname LIKE '" + text + "%' "
                    + "OR customer.mobile LIKE '" + text + "%' OR `lease`.`uniqeid` LIKE '" + text + "%') ";

            if (status != "Select Status") {

                query = query + "AND `lease_status`.name='" + status + "'";
            }
//            System.out.println(query);
            rs = MySQL.search(query);
            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("lease.uniqeid"));
                v.add(rs.getString("apartment.id"));
                v.add(rs.getString("nic"));
                v.add(df.format(rs.getDouble("total_rate")));
                v.add(rs.getString("start_date"));
                v.add(rs.getString("end_date"));
                v.add(df.format(rs.getDouble("lease.advance_rate")));
                v.add(rs.getString("lease_status.name"));

                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeLease() {
        String leaseId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
        String apartment = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();

        String nic = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString();
        MySQL.iud("UPDATE `lease` SET `lease_status_id`='3' WHERE `uniqeid`='" + leaseId + "'");
        MySQL.iud("UPDATE `apartment` SET `apartment_status_id`='1' WHERE `id`='" + apartment + "'");

        try {
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM lease\n"
                    + "INNER JOIN customer ON lease.customer_id=customer.cusid\n"
                    + "WHERE `nic`='" + nic + "' AND lease.lease_status_id IN ('1','4')";

            rs = MySQL.search(query);
            if (!rs.next()) {
                MySQL.iud("UPDATE `customer` SET `cus_status_id`='2' WHERE `nic`='" + nic + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadLease();
    }

    private void completeLease() {
        String leaseId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
        String apartment = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
        String nic = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString();

        MySQL.iud("UPDATE `lease` SET `lease_status_id`='2' WHERE `uniqeid`='" + leaseId + "'");
        MySQL.iud("UPDATE `apartment` SET `apartment_status_id`='1' WHERE `id`='" + apartment + "'");

        try {
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM lease\n"
                    + "INNER JOIN customer ON lease.customer_id=customer.cusid\n"
                    + "WHERE `nic`='" + nic + "' AND lease.lease_status_id IN ('1','4')";

            rs = MySQL.search(query);
            if (!rs.next()) {
                MySQL.iud("UPDATE `customer` SET `cus_status_id`='2' WHERE `nic`='" + nic + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadLease();

    }

    private void leaseDocGen() {
        String leasId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

        try {
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM lease\n"
                    + "INNER JOIN customer ON lease.customer_id=customer.cusid\n"
                    + "INNER JOIN lease_has_apartment ON lease.uniqeid=lease_has_apartment.lease_uniqeid \n"
                    + "INNER JOIN apartment ON lease_has_apartment.apartment_id=apartment.id\n"
                    + "INNER JOIN `lease_status` ON lease.lease_status_id=`lease_status`.id "
                    + "INNER JOIN quality_level ON lease_has_apartment.quality_level_id=quality_level.id\n"
                    + "INNER JOIN `condition` ON lease_has_apartment.condition_id=`condition`.id\n"
                    + "INNER JOIN apartment_type ON lease_has_apartment.apartment_type_id=apartment_type.id\n"
                    + "INNER JOIN apartment_status ON apartment.apartment_status_id=apartment_status.id "
                    + "WHERE lease.uniqeid = '" + leasId + "' ";
            rs = MySQL.search(query);
            if (rs.next()) {

                String intial = rs.getString("customer.initial");
                String fname = rs.getString("customer.fname");
                String lname = rs.getString("customer.lname");

                String nic = rs.getString("customer.nic");

                String name = intial + " " + fname + " " + lname;

                Date regdate = rs.getDate("reg_date");
                String rentRate = rs.getString("rent_rate");
                double advance = rs.getDouble("advance_rate");
                String qualityAdditionRate = rs.getString("quality_level.addion_rate");
                String conditionAdditionRate = rs.getString("condition.addion_rate");
                String typeAdditionRate = rs.getString("apartment_type.addition_rate");
                String apartmentId = rs.getString("apartment.id");
                Date startdate = rs.getDate("lease.start_date");
                Date enddate = rs.getDate("lease.end_date");

                double totalRentRate = Double.parseDouble(rentRate) + Double.parseDouble(qualityAdditionRate) + Double.parseDouble(conditionAdditionRate) + Double.parseDouble(typeAdditionRate);
                LeaseDataModel ldm = new LeaseDataModel(regdate, name, nic, apartmentId, startdate, enddate, totalRentRate, advance);
                PdfWriter pw = new PdfWriter(ldm);
                pw.generateDoc(String.valueOf(leasId));
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jComboBox4 = new javax.swing.JComboBox<>();

        jMenuItem1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-add-new-20.png"))); // NOI18N
        jMenuItem1.setText("New");
        jMenuItem1.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-upgrade-20.png"))); // NOI18N
        jMenuItem2.setText("Upgrade");
        jMenuItem2.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jMenuItem3.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-downgrade20.png"))); // NOI18N
        jMenuItem3.setText("Downgrade");
        jMenuItem3.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem3);

        jMenuItem4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-view-20.png"))); // NOI18N
        jMenuItem4.setText("Info");
        jMenuItem4.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem4);

        jMenu1.setText("Change Status");
        jMenu1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N

        jMenuItem5.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-lease-20.png"))); // NOI18N
        jMenuItem5.setText("Close");
        jMenuItem5.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem6.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-complete-20.png"))); // NOI18N
        jMenuItem6.setText("Complete");
        jMenuItem6.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jPopupMenu1.add(jMenu1);

        setPreferredSize(new java.awt.Dimension(1380, 940));

        jPanel4.setPreferredSize(new java.awt.Dimension(1380, 940));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel4.setText("Lease Info");

        jLabel5.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel5.setText("Search");

        jTextField3.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Lease ID", "Apartment", "Customer", "Rate", "Start Date", "End Date", "Advance", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton6.setBackground(new java.awt.Color(102, 0, 0));
        jButton6.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("New");
        jButton6.setContentAreaFilled(false);
        jButton6.setOpaque(true);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jComboBox4.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Status", "Item 2", "Item 3", "Item 4" }));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 802, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        viewLeaseReg();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        viewLeaseReg();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            viewLeaseUgrade();
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.getSelectedRow() != -1 && evt.getButton() == 3) {
            jMenuItem2.setVisible(true);
            jMenuItem3.setVisible(true);
            jMenuItem4.setVisible(true);
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        } else if (evt.getButton() == 3) {
            jMenuItem2.setVisible(false);
            jMenuItem3.setVisible(false);
            jMenuItem4.setVisible(false);
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        viewLeaseDowngrade();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        loadLease();
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadLease();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        if (jTable1.getSelectedRow() != -1)  {
            closeLease();
        }


    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            completeLease();
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            LoadingIndicator.setWaitCursor(this);

            String leaseId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
            String outputPath = "doc/lease-doc/" + leaseId + ".docx";
            File file = new File(outputPath);
            if (file.exists()) {
                StaticComponent.openPDF(outputPath);

            } else {
                leaseDocGen();
            }
            LoadingIndicator.resetCursor(this);

        }

    }//GEN-LAST:event_jMenuItem4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
