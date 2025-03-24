
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import gui.dialogbox.CompliantCategory;
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
public class CSComplaint extends javax.swing.JPanel {

    /**
     * Creates new form CSComplaint
     */
    JFrame parent;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    public CSComplaint(JFrame parent) {
        initComponents();
        CustomTable custable = new CustomTable();
        custable.modifyLayout(jTable1);
        
        this.setBackground(new Color(255, 255, 255, 0));
        jPanel1.setBackground(new Color(255, 255, 255, 0));
        jPanel8.setBackground(new Color(255, 255, 255, 190));
        jPanel2.setBackground(new Color(255, 255, 255, 190));
        CustomDateChooser.customizeDateChooser(jDateChooser1);
        
        setNewButtonUI();
        startLisners();
        loadComplaintCategpry();
        loadstatus();
        loadComplaint();
        
        Date today = new Date();
        jDateChooser1.setDate(today);
        
    }
    
    private void setNewButtonUI() {
        
        jButton6.setUI(new CustomButtonUI(0));
        
    }
    
    private void startLisners() {

        // Add a MouseListener to the button
        jButton6.addMouseListener(new CustomeMouseAdapter(jButton6));
        
    }
    Map<String, String> comCaegory = new HashMap<>();
    
    public void loadComplaintCategpry() {
        try {
            
            Vector v = new Vector();
            v.add("Select Category");
            ResultSet rs = MySQL.search("SELECT * FROM com_category");
            while (rs.next()) {
                v.add(rs.getString("name"));
                comCaegory.put(rs.getString("name"), rs.getString("id"));
            }
            jComboBox2.setModel(new DefaultComboBoxModel<>(v));
            jComboBox7.setModel(new DefaultComboBoxModel<>(v));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Map<String, String> status = new HashMap<>();
    
    public void loadstatus() {
        try {
            
            Vector v = new Vector();
            v.add("Select Status");
            ResultSet rs = MySQL.search("SELECT * FROM complaint_status ");
            while (rs.next()) {
                v.add(rs.getString("name"));
                status.put(rs.getString("name"), rs.getString("id"));
            }
            jComboBox3.setModel(new DefaultComboBoxModel<>(v));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadComplaint() {
        String status = jComboBox3.getSelectedItem().toString();
        String category = jComboBox2.getSelectedItem().toString();
        String text = jTextField3.getText();
        
        try {
            
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM complaint\n"
                    + "INNER JOIN `complaint_status` ON complaint.complaint_status_id=`complaint_status`.id\n"
                    + "INNER JOIN customer ON complaint.customer_id=customer.cusid\n"
                    + "INNER JOIN com_category ON complaint.com_category_id=com_category.id "
                    + "WHERE  (`description` LIKE '%" + text + "%' OR `mobile` LIKE '" + text + "%' OR `report_date` LIKE '%" + text + "%' OR `nic` LIKE '" + text + "%')";
            
            if (status != "Select Status") {
                query = query + " AND complaint_status.name='" + status + "'";
            }
            if (category != "Select Category") {
                query = query + " AND `com_category`.`name` = '" + category + "%' ";
            }
            rs = MySQL.search(query);
            while (rs.next()) {
                Vector v = new Vector();
                
                v.add(rs.getString("com_category.name"));
                v.add(rs.getString("description"));
                v.add(rs.getString("report_date"));
                v.add(rs.getString("nic"));
                v.add(rs.getString("mobile"));
                v.add(rs.getString("complaint_status.name"));
                dtm.addRow(v);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    String customerId = "";
    
    private void loadCusName() {
        String nic = jTextField24.getText();
        
        try {
            if (!nic.isEmpty()) {
                ResultSet rs = MySQL.search("SELECT * FROM customer WHERE `nic` = '" + nic + "'");
                if (rs.next()) {
                    String name = rs.getString("customer.fname") + " " + rs.getString("customer.lname");
                    
                    this.customerId = rs.getString("customer.cusid");
                    
                    jTextField23.setText(name);
                } else {
                    jTextField23.setText("");
                }
                
            } else {
                jTextField23.setText("");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void resetFild() {
        jComboBox7.setSelectedIndex(0);
        jTextField23.setText("");
        jTextField24.setText("");
        jDateChooser1.setDate(null);
        jTextArea6.setText("");
    }
    
    public void addComplaint() {
        
        String category = jComboBox7.getSelectedItem().toString();
        String nic = jTextField24.getText();
        Date rdate = jDateChooser1.getDate();
        String des = jTextArea6.getText();
        
        if (category.equals("Select Category")) {
            JOptionPane.showMessageDialog(parent, "Please select category", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter NIC", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (customerId.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Invalid NIC", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (des.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter description", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (rdate == null) {
            JOptionPane.showMessageDialog(parent, "Please enter reported date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            MySQL.iud("INSERT INTO complaint (`com_category_id`,`description`,`report_date`,`complaint_status_id`,`customer_id`) \n"
                    + "VALUES ('" + comCaegory.get(category) + "','" + des.replace("'", "\\'") + "','" + sdf.format(rdate) + "','1','" + customerId + "')");
            resetFild();
            loadComplaint();
            JOptionPane.showMessageDialog(parent, "Complaint Added Succes", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        jButton6 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();

        setPreferredSize(new java.awt.Dimension(1380, 930));

        jPanel1.setPreferredSize(new java.awt.Dimension(1380, 930));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel4.setText("Complaints");

        jComboBox2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Category", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

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
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Category", "Description", "Reported Date", "Customer NIC", "Customer Mobile", "Status"
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

        jComboBox3.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Status", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1340, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel8.setPreferredSize(new java.awt.Dimension(1380, 345));

        jComboBox7.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox7.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox7.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox7.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jComboBox7.setLightWeightPopupEnabled(false);
        jComboBox7.setMinimumSize(new java.awt.Dimension(72, 20));
        jComboBox7.setPreferredSize(new java.awt.Dimension(360, 45));
        jComboBox7.setVerifyInputWhenFocusTarget(false);

        jButton3.setBackground(new java.awt.Color(102, 0, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-plus-38.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel28.setText("Category");
        jLabel28.setMaximumSize(new java.awt.Dimension(37, 20));

        jLabel29.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel29.setText("Customer NIC");
        jLabel29.setMaximumSize(new java.awt.Dimension(37, 20));

        jTextField23.setEditable(false);
        jTextField23.setBackground(java.awt.Color.white);
        jTextField23.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField23.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField23.setMinimumSize(new java.awt.Dimension(360, 45));
        jTextField23.setPreferredSize(new java.awt.Dimension(420, 45));
        jTextField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField23ActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel30.setText("Customer Name");
        jLabel30.setMaximumSize(new java.awt.Dimension(37, 20));

        jTextField24.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField24.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField24.setMinimumSize(new java.awt.Dimension(360, 45));
        jTextField24.setPreferredSize(new java.awt.Dimension(420, 45));
        jTextField24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField24KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField24KeyTyped(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel31.setText("Reported Date");
        jLabel31.setMaximumSize(new java.awt.Dimension(37, 20));

        jLabel32.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel32.setText("Description");
        jLabel32.setMaximumSize(new java.awt.Dimension(37, 20));

        jTextArea6.setColumns(20);
        jTextArea6.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextArea6.setLineWrap(true);
        jTextArea6.setRows(5);
        jTextArea6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextArea6.setMinimumSize(new java.awt.Dimension(360, 45));
        jScrollPane7.setViewportView(jTextArea6);

        jButton6.setBackground(new java.awt.Color(102, 0, 0));
        jButton6.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Add");
        jButton6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(), null), javax.swing.BorderFactory.createCompoundBorder()));
        jButton6.setContentAreaFilled(false);
        jButton6.setOpaque(true);
        jButton6.setPreferredSize(new java.awt.Dimension(420, 45));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jDateChooser1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(255, 255, 255))));
        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jDateChooser1.setPreferredSize(new java.awt.Dimension(420, 45));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane7)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(42, 42, 42)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        CompliantCategory category = new CompliantCategory(parent, this, true);
        category.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        addComplaint();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField24KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField24KeyReleased
        loadCusName();
    }//GEN-LAST:event_jTextField24KeyReleased

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadComplaint();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        loadComplaint();

    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadComplaint();

    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField24KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField24KeyTyped
        String regex = "^[0-9]{0,13}(v)?";
        char value = evt.getKeyChar();
        String input = jTextField24.getText() + value;
        Pattern pattern = Pattern.compile(regex);
        
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField24KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox7;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea6;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
