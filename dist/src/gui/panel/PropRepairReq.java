/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import gui.dialogbox.RepairReqComplete;
import java.awt.Color;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import model.CustomButtonUI;
import model.CustomTable;
import model.CustomeMouseAdapter;
import model.MySQL;

/**
 *
 * @author Hiranya
 */
public class PropRepairReq extends javax.swing.JPanel {

    /**
     * Creates new form RepairReq
     */
    private JFrame parent;
    DecimalFormat df = new DecimalFormat("#.00");

    public PropRepairReq(JFrame parent) {
        initComponents();
        this.setBackground(new Color(255, 255, 255, 0));
        jPanel4.setBackground(new Color(255, 255, 255, 190));
        CustomTable custable = new CustomTable();
        custable.modifyLayout(jTable1);
        loadStatus();
        loadRapirReq();
    }

    public void loadStatus() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `repair_status` ");
            Vector v = new Vector();
            v.add("Select Status");
            while (rs.next()) {
                String abc = rs.getString("name");
                v.add(abc);
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            jComboBox2.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRapirReq() {

        String status = jComboBox2.getSelectedItem().toString();
        String text = jTextField3.getText();

        try {

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM repair_req\n"
                    + "INNER JOIN customer ON repair_req.customer_id=customer.cusid\n"
                    + "INNER JOIN repair_status ON repair_req.repair_status_id=repair_status.id\n"
                    + "WHERE (customer.nic LIKE '" + text + "%' OR customer.fname LIKE '" + text + "%' OR customer.lname LIKE '" + text + "%' OR customer.mobile LIKE '" + text + "%' OR repair_req.title LIKE '" + text + "%' OR description LIKE '" + text + "%' ) ";

            if (status != "Select Status") {

                query = query + "AND repair_status.name='" + status + "'";
            }

            rs = MySQL.search(query);
            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("repair_req.id"));
                v.add(rs.getString("repair_req.title"));
                v.add(rs.getString("repair_req.report_date"));
                v.add(rs.getString("description"));
                v.add(rs.getString("customer.mobile"));
                if (rs.getString("repair_req.price") == null) {
                    v.add("");

                } else {
                    v.add(df.format(Double.parseDouble(rs.getString("repair_req.price"))));
                }
                v.add(rs.getString("repair_req.fixed_date"));
                v.add(rs.getString("repair_status.name"));

                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAction() {

        int selectedRow = jTable1.getSelectedRow();
        if (jTable1.getValueAt(selectedRow, 7).equals("Pending")) {
            jMenuItem1.setText("Action Taken");
            jMenuItem1.setIcon(new ImageIcon("src/icon/icons8-click-20.png"));
        } else if (jTable1.getValueAt(selectedRow, 7).equals("Action Taken")) {
            jMenuItem1.setText("Repair Prosessing");
            jMenuItem1.setIcon(new ImageIcon("src/icon/icons8-process-20.png"));

        } else if (jTable1.getValueAt(selectedRow, 7).equals("Processing")) {
            jMenuItem1.setText("Completed");
            jMenuItem1.setIcon(new ImageIcon("src/icon/icons8-complete-20.png"));

        }
    }

    private void changeStatus() {
        String id = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

        String text = jMenuItem1.getText();
        if (text.equals("Action Taken")) {
            MySQL.iud("UPDATE  repair_req SET `repair_status_id`= '2'\n"
                    + "WHERE id='" + id + "'");
            loadRapirReq();
        } else if (text.equals("Repair Prosessing")) {
            MySQL.iud("UPDATE  repair_req SET `repair_status_id`= '3'\n"
                    + "WHERE id='" + id + "'");
            loadRapirReq();
        } else if (text.equals("Completed")) {
            RepairReqComplete rrc = new RepairReqComplete(parent, true, this);
            rrc.jLabel4.setText(id);
            rrc.setVisible(true);
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
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPopupMenu1.setPreferredSize(new java.awt.Dimension(200, 40));

        jMenuItem1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-click-20.png"))); // NOI18N
        jMenuItem1.setText("Action Taken");
        jMenuItem1.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setPreferredSize(new java.awt.Dimension(1380, 940));

        jPanel4.setPreferredSize(new java.awt.Dimension(1380, 940));

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
                "Req ID", "Title", "Request Date", "Description", "Customer Mobile", "Cost", "Fixed Date", "Status"
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
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 765, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 847, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1380, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 940, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.getSelectedRow() != -1 && evt.getButton() == 3) {
            String text = jTable1.getValueAt(jTable1.getSelectedRow(), 7).toString();
            if (!text.equals("Completed")) {
                setAction();
                jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
            }

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        changeStatus();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadRapirReq();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadRapirReq();
    }//GEN-LAST:event_jTextField3KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
