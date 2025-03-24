/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import java.awt.Color;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.CustomButtonUI;
import model.CustomDateChooser;
import model.CustomTable;
import model.CustomeMouseAdapter;
import model.MySQL;
import model.StaticComponent;

/**
 *
 * @author Hiranya
 */
public class CSRepairReq extends javax.swing.JPanel {

    /**
     * Creates new form CSRepairReq
     */
    JFrame parent;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public CSRepairReq() {
        initComponents();
        this.setBackground(new Color(255, 255, 255, 0));
        jPanel1.setBackground(new Color(255, 255, 255, 190));
        jPanel2.setBackground(new Color(255, 255, 255, 190));
        CustomDateChooser.customizeDateChooser(jDateChooser5);
        CustomTable custable = new CustomTable();
        custable.modifyLayout(jTable1);
        setNewButtonUI();
        startLisners();
        loadstatus();
        loadRepairReq();

        Date today = new Date();
        jDateChooser5.setDate(today);
    }

    private void setNewButtonUI() {

        jButton10.setUI(new CustomButtonUI(0));

    }

    private void startLisners() {

        // Add a MouseListener to the button
        jButton10.addMouseListener(new CustomeMouseAdapter(jButton10));

    }

    Map<String, String> status = new HashMap<>();

    public void loadstatus() {
        try {

            Vector v = new Vector();
            v.add("Select Status");
            ResultSet rs = MySQL.search("SELECT * FROM repair_status ");
            while (rs.next()) {
                v.add(rs.getString("name"));
                status.put(rs.getString("name"), rs.getString("id"));
            }
            jComboBox2.setModel(new DefaultComboBoxModel<>(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRepairReq() {
        String status = jComboBox2.getSelectedItem().toString();
        String text = jTextField3.getText();

        try {

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs;
            String query = "SELECT * FROM `repair_req` INNER JOIN `repair_status` ON `repair_req`.`repair_status_id`=`repair_status`.`id`"
                    + "INNER JOIN `customer` ON `repair_req`.`customer_id`=`customer`.`cusid` "
                    + "WHERE  (`title` LIKE '" + text + "%' OR `description` LIKE '%" + text + "%' OR `mobile` LIKE '" + text + "%' OR `report_date` LIKE '%" + text + "%')";
            if (status != "Select Status") {
                query = query
                        + "AND repair_status.name='" + status + "' ";
            }
            rs = MySQL.search(query);

            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("repair_req.id"));
                v.add(rs.getString("title"));
                v.add(rs.getString("report_date"));
                v.add(rs.getString("description"));
                v.add(rs.getString("mobile"));
                v.add(rs.getString("repair_status.name"));
                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    String customerId = "";

    private void loadCusName() {
        String nic = jTextField36.getText();

        try {
            if (!nic.isEmpty()) {
                ResultSet rs = MySQL.search("SELECT * FROM customer WHERE `nic` = '" + nic + "'");
                if (rs.next()) {
                    String name = rs.getString("customer.fname") + " " + rs.getString("customer.lname");

                    this.customerId = rs.getString("customer.cusid");

                    jTextField35.setText(name);
                } else {
                    jTextField35.setText("");
                }

            } else {
                jTextField35.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRepairReq() {

        String title = jTextField34.getText();
        String nic = jTextField36.getText();
        Date rdate = jDateChooser5.getDate();
        String des = jTextArea10.getText();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter title", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter NIC", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (customerId.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Invalid NIC", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (des.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter description", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (rdate == null) {
            JOptionPane.showMessageDialog(parent, "Please enter reported date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            MySQL.iud("INSERT INTO repair_req (`title`,`description`,`report_date`,`repair_status_id`,`customer_id`) \n"
                    + "VALUES ('" + title + "','" + des.replace("'", "\\'") + "','" + sdf.format(rdate) + "','1','" + customerId + "')");

            loadRepairReq();
            JOptionPane.showMessageDialog(parent, "Repair request Added Succes", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
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

        jPanel2 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jTextField36 = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextArea10 = new javax.swing.JTextArea();
        jButton10 = new javax.swing.JButton();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1380, 930));

        jPanel2.setPreferredSize(new java.awt.Dimension(1380, 345));

        jLabel48.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel48.setText("Title");
        jLabel48.setMaximumSize(new java.awt.Dimension(37, 20));

        jTextField34.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField34.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField34.setMinimumSize(new java.awt.Dimension(360, 45));
        jTextField34.setPreferredSize(new java.awt.Dimension(420, 45));

        jLabel49.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel49.setText("Customer NIC");
        jLabel49.setMaximumSize(new java.awt.Dimension(37, 20));

        jTextField35.setEditable(false);
        jTextField35.setBackground(new java.awt.Color(255, 255, 255));
        jTextField35.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField35.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField35.setMinimumSize(new java.awt.Dimension(360, 45));
        jTextField35.setPreferredSize(new java.awt.Dimension(420, 45));

        jLabel50.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel50.setText("Customer Name");
        jLabel50.setMaximumSize(new java.awt.Dimension(37, 20));

        jTextField36.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField36.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField36.setMinimumSize(new java.awt.Dimension(360, 45));
        jTextField36.setPreferredSize(new java.awt.Dimension(420, 45));
        jTextField36.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField36KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField36KeyTyped(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel51.setText("Request Date");
        jLabel51.setMaximumSize(new java.awt.Dimension(37, 20));

        jLabel52.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel52.setText("Discription");
        jLabel52.setMaximumSize(new java.awt.Dimension(37, 20));

        jTextArea10.setColumns(20);
        jTextArea10.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextArea10.setLineWrap(true);
        jTextArea10.setRows(5);
        jTextArea10.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextArea10.setMinimumSize(new java.awt.Dimension(360, 45));
        jScrollPane11.setViewportView(jTextArea10);

        jButton10.setBackground(new java.awt.Color(102, 0, 0));
        jButton10.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Add");
        jButton10.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(), null), javax.swing.BorderFactory.createCompoundBorder()));
        jButton10.setContentAreaFilled(false);
        jButton10.setOpaque(true);
        jButton10.setPreferredSize(new java.awt.Dimension(420, 45));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jDateChooser5.setBackground(new java.awt.Color(255, 255, 255));
        jDateChooser5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jDateChooser5.setDateFormatString("yyyy-MM-dd");
        jDateChooser5.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jDateChooser5.setPreferredSize(new java.awt.Dimension(420, 45));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane11)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(42, 42, 42)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel1.setPreferredSize(new java.awt.Dimension(1380, 930));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel4.setText("Repair Request");

        jComboBox2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select State ID", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel5.setText("Search");

        jTextField3.setBackground(new java.awt.Color(255, 255, 255));
        jTextField3.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Req ID", "Title", "Request Date", "Description", "Customer Mobile", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 765, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1340, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField36KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField36KeyReleased
        loadCusName();
    }//GEN-LAST:event_jTextField36KeyReleased

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        addRepairReq();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadRepairReq();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadRepairReq();

    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField36KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField36KeyTyped
        String regex = "^[0-9]{0,13}(v)?";
        char value = evt.getKeyChar();
        String input = jTextField3.getText() + value;
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField36KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea10;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    // End of variables declaration//GEN-END:variables
}
