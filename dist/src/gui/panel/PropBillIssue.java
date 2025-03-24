/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import email.EmailSenderWithAttachment;
import gui.dialogbox.BillCategory;
import gui.dialogbox.Loading;
import gui.frame.Login;
import gui.frame.PropertyManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.DecimalFormat;
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
import model.CustomTable;
import model.CustomeMouseAdapter;
import model.LoadingIndicator;
import model.MySQL;
import model.StaticComponent;
import static model.StaticComponent.openPDF;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import report.model.Customer;
import report.model.OtherBill;

/**
 *
 * @author Hiranya
 */
public class PropBillIssue extends javax.swing.JPanel {

    /**
     * Creates new form BillIssue
     */
    PropertyManager parent;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("#0.00");

    public PropBillIssue(JFrame parent) {
        initComponents();
        this.parent = (PropertyManager) parent;
        this.setBackground(new Color(255, 255, 255, 0));
        jPanel5.setBackground(new Color(255, 255, 255, 190));
        jPanel1.setBackground(new Color(255, 255, 255, 190));
        CustomTable custable = new CustomTable();
        custable.modifyLayout(jTable1);
        setNewButtonUI();
        startLisners();
        loadCategory();

        loadStatus();
        loadBill();
    }

    private void startLisners() {

        // Add a MouseListener to the button
        jButton1.addMouseListener(new CustomeMouseAdapter(jButton1));
        jButton2.addMouseListener(new CustomeMouseAdapter(jButton2));

    }

    private void setNewButtonUI() {
        jButton1.setUI(new CustomButtonUI(0));
        jButton2.setUI(new CustomButtonUI(0));

    }

    public void viewBillVategory() {
        BillCategory bc = new BillCategory(parent, true, this);
        bc.setVisible(true);
    }

    Map<String, String> category = new HashMap<>();

    public void loadCategory() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `category` WHERE `id` NOT IN (1,2,3,5)");
            Vector u = new Vector();
            u.add("Select Category");
            while (rs.next()) {
                String abc = rs.getString("name");

                u.add(abc);
                category.put(rs.getString("name"), rs.getString("id"));
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(u);
            DefaultComboBoxModel dcm1 = new DefaultComboBoxModel(u);
            jComboBox1.setModel(dcm);
            jComboBox2.setModel(dcm1);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void loadStatus() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `bill_issue_status`");
            Vector v = new Vector();
            v.add("Select Status");
            while (rs.next()) {
                String abc = rs.getString("name");
                v.add(abc);
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            jComboBox3.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadBill() {

        String category = jComboBox2.getSelectedItem().toString();
        String status = jComboBox3.getSelectedItem().toString();
        String text = jTextField3.getText();

        try {

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM bill_issue\n"
                    + "INNER JOIN customer ON bill_issue.customer_cusid=customer.cusid\n"
                    + "INNER JOIN bill_issue_status ON bill_issue.bill_issue_status_id=bill_issue_status.id\n"
                    + "INNER JOIN category ON bill_issue.category_id=category.id\n"
                    + "WHERE (customer.nic LIKE '" + text + "%' OR customer.fname LIKE '" + text + "%' OR customer.lname LIKE '" + text + "%' OR customer.mobile LIKE '" + text + "%' OR uniqueid LIKE '" + text + "%' OR reason LIKE '%" + text + "%') ";

            if (category != "Select Category") {

                query = query + " AND category.name='" + category + "'";
            }
            if (status != "Select Status") {

                query = query + " AND bill_issue_status.name='" + status + "'";
            } else {
                query = query + " AND bill_issue_status.name!='Deleted'";
            }

            rs = MySQL.search(query);
            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("uniqueid"));
                v.add(rs.getString("category.name"));
                v.add(rs.getString("customer.nic"));
                v.add(rs.getString("issue_date"));
                v.add(df.format(Double.parseDouble(rs.getString("bill_issue.amount"))));
                v.add(rs.getString("bill_issue.reason"));
                v.add(rs.getString("bill_issue_status.name"));

                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Customer cus = new Customer();

    private void loadCusName() {
        String nic = jTextField5.getText();

        try {
            if (!nic.isEmpty() && nic.length() > 8) {
                ResultSet rs = MySQL.search("SELECT * FROM customer WHERE `nic` = '" + nic + "'");
                if (rs.next()) {
                    String name = rs.getString("customer.fname") + " " + rs.getString("customer.lname");
                    cus.setName(name);
                    cus.setNic(nic);
                    cus.setMobile(rs.getString("customer.mobile"));
                    cus.setCusid(rs.getString("customer.cusid"));
                    cus.setEmail(rs.getString("customer.email"));

                    jTextField7.setText(name);
                } else {
                    jTextField7.setText("");
                }

            } else {
                jTextField7.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetFiled() {
        jTextField5.setText("");
        jTextField7.setText("");
        jComboBox1.setSelectedIndex(0);
        jTextArea1.setText("");
        jTextField8.setText("");
    }

    private void addBill() {
        String nic = jTextField5.getText();

        String category = jComboBox1.getSelectedItem().toString();
        String reason = jTextArea1.getText();
        String amount = jTextField8.getText();

        if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please Enter customer NIC", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (cus.getNic() == null) {
            JOptionPane.showMessageDialog(parent, "Customer NIC not found", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (category.equals("Select Category")) {
            JOptionPane.showMessageDialog(parent, "Please select category", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter reason", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (amount.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter amount", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            Date today = new Date();
            long uniqid = System.currentTimeMillis();
            MySQL.iud("INSERT INTO bill_issue (uniqueid,issue_date,amount,reason,customer_cusid,category_id,bill_issue_status_id) \n"
                    + "VALUES ('" + uniqid + "','" + sdf.format(today) + "','" + amount + "','" + reason.replace("'", "\\'") + "','" + cus.getCusid() + "','" + this.category.get(category) + "','1')");
            resetFiled();
            loadBill();
            printBill(new OtherBill(String.valueOf(uniqid), sdf.format(today), cus.getName(), cus.getMobile(), cus.getEmail(), reason, category, String.valueOf(uniqid), df.format(Double.parseDouble(amount))));
        }
    }

    private void printBill(OtherBill bill) {

        String pathStream = "src/report/jasper/other_bill_issue.jasper";
        String outputPath = "report/bill_issue/" + bill.getId() + ".pdf";

        HashMap parameters = new HashMap();
        parameters.put("id", bill.getId());
        parameters.put("date", bill.getDate());
        parameters.put("customer", bill.getCustomer());
        parameters.put("reason", bill.getReason());
        parameters.put("category", bill.getCategory());
        parameters.put("mobile", bill.getMobile());
        parameters.put("barcode", bill.getBarcode());
        parameters.put("total", bill.getTotal());
        try {

            JREmptyDataSource dataSource = new JREmptyDataSource();
            JasperPrint jp = JasperFillManager.fillReport(pathStream, parameters, dataSource);
//            JasperViewer.viewReport(jp, false);
            JasperExportManager.exportReportToPdfFile(jp, outputPath);
            openPDF(outputPath);

            String email = bill.getEmail();

            if (email != null && !email.isEmpty()) {
                String pdfFilePath = outputPath;
                String fileName = bill.getId() + ".pdf";
                sendEmailAttachment(email, pdfFilePath, fileName, bill.getCategory());
            }
        } catch (JRRuntimeException e) {
            /*  when file using from other software*/
            openPDF(outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void printBill() {

        String uniquID = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

        String nic = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString();
        String pathStream = "src/report/jasper/other_bill_issue.jasper";
        String outputPath = "report/bill_issue/" + uniquID + ".pdf";

        try {

            ResultSet rs = MySQL.search("SELECT * FROM customer WHERE `nic` = '" + nic + "'");
            if (rs.next()) {
                String name = rs.getString("customer.fname") + " " + rs.getString("customer.lname");

                HashMap parameters = new HashMap();
                parameters.put("id", uniquID);
                parameters.put("date", jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString());
                parameters.put("customer", name);
                parameters.put("reason", jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString());
                parameters.put("category", jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString());
                parameters.put("mobile", rs.getString("customer.mobile"));
                parameters.put("barcode", uniquID);
                parameters.put("total", jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString());
                JREmptyDataSource dataSource = new JREmptyDataSource();
                JasperPrint jp = JasperFillManager.fillReport(pathStream, parameters, dataSource);
//                JasperViewer.viewReport(jp, false);
                JasperExportManager.exportReportToPdfFile(jp, outputPath);
                openPDF(outputPath);

            }

        } catch (JRRuntimeException e) {
            /*  when file using from other software*/
            openPDF(outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendEmailAttachment(String email, String pdfFilePath, String fileName, String category) {
        String sub = "Bill receipt for " + category;
        String body = "I trust this message finds you well. We appreciate your continued association with us.\n"
                + "We have processed the recent charges related to your account, and I am pleased to inform you that everything fits. "
                + "Attached, please find the Bill receipt detailing the " + category + ".\n"
                + "Kindly review the invoice at your earliest convenience. If everything is in order, "
                + "we kindly request that you process the payment within a month ";
        EmailSenderWithAttachment esw = new EmailSenderWithAttachment(email, sub, body, pdfFilePath, fileName);
        String status = esw.sendMail();
        if (status.equals("failed")) {
            String[] options = {"Close", "Retry"};

            int result = JOptionPane.showOptionDialog(
                    parent,
                    "Email sending failed. Please check your internet connection!.\nClick retry button if connection is Fine",
                    "Connection lost",
                    JOptionPane.ERROR_MESSAGE,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1]
            );

            if (result == 0) {
                JOptionPane.showMessageDialog(parent, "Email Sending is Canceled", "Info", JOptionPane.INFORMATION_MESSAGE);

            } else if (result == 1) {
                String status2 = esw.sendMail();
                if (status2.equals("failed")) {
                    JOptionPane.showMessageDialog(parent, "Email Sending Failed", "Error", JOptionPane.WARNING_MESSAGE);
                } else if (status2.equals("Success")) {
                    JOptionPane.showMessageDialog(parent, "Email Sent Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
                } else if (status2.equals("timeout")) {
                    JOptionPane.showMessageDialog(parent, "Time out! Check your connection", "Error", JOptionPane.WARNING_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(parent, "Something Wrong! Try again later", "Error", JOptionPane.WARNING_MESSAGE);

                }
            }
        } else if (status.equals("Success")) {
//            JOptionPane.showMessageDialog(this, "Email Sent Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
        } else {
            JOptionPane.showMessageDialog(parent, "Time out! Email Sending Failed", "Error", JOptionPane.WARNING_MESSAGE);

        }
    }

    private void deleteBill() {
        String uniquID = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
        MySQL.iud("UPDATE  `bill_issue` SET `bill_issue_status_id`='3'"
                + "WHERE `uniqueid`='" + uniquID + "'");
        loadBill();
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
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jPopupMenu1.setPreferredSize(new java.awt.Dimension(200, 80));

        jMenuItem1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-delete-20.png"))); // NOI18N
        jMenuItem1.setText("Move Trash");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-print-20.png"))); // NOI18N
        jMenuItem2.setText("Print");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setPreferredSize(new java.awt.Dimension(1380, 940));

        jPanel5.setPreferredSize(new java.awt.Dimension(1380, 940));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel4.setText("Bill Issued");

        jComboBox2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Category", "Item 2", "Item 3", "Item 4" }));
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
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Bill ID", "Category", "Customer NIC", "Date", "Amount", "Reason", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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

        jComboBox3.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Status", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel1.setMinimumSize(new java.awt.Dimension(1420, 300));

        jLabel2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel2.setText("Customer NIC");
        jLabel2.setMinimumSize(new java.awt.Dimension(37, 20));

        jTextField5.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField5.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        jTextField5.setPreferredSize(new java.awt.Dimension(360, 45));
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel3.setText("Description");
        jLabel3.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel6.setText("Name");
        jLabel6.setMinimumSize(new java.awt.Dimension(37, 20));

        jTextField7.setEditable(false);
        jTextField7.setBackground(new java.awt.Color(255, 255, 255));
        jTextField7.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField7.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField7.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        jTextField7.setPreferredSize(new java.awt.Dimension(360, 45));

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel7.setText("Category");
        jLabel7.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel8.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel8.setText("Amount");
        jLabel8.setMinimumSize(new java.awt.Dimension(37, 20));

        jComboBox1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jComboBox1.setPreferredSize(new java.awt.Dimension(360, 45));

        jButton1.setBackground(new java.awt.Color(102, 0, 0));
        jButton1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Print");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0), 2));
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField8.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField8.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField8.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        jTextField8.setPreferredSize(new java.awt.Dimension(360, 45));
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField8KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField8KeyTyped(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(102, 0, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-plus-38.png"))); // NOI18N
        jButton2.setContentAreaFilled(false);
        jButton2.setOpaque(true);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(98, 98, 98)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2))
                .addGap(134, 134, 134)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBox1, 0, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        LoadingIndicator.setWaitCursor(parent);
        addBill();
        LoadingIndicator.resetCursor(parent);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        viewBillVategory();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField8KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyTyped
        String regex = "^[1-9][0-9]*[.]?([0-9]{1,2})?";
        char value = evt.getKeyChar();
        String input = jTextField8.getText() + value;
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField8KeyTyped

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        loadCusName();
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadBill();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        loadBill();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadBill();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        if (jTable1.getSelectedRow() != -1 && evt.getButton() == 3) {
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        deleteBill();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        String regex = "^[0-9]{0,13}(v)?";
        char value = evt.getKeyChar();
        String input = jTextField5.getText() + value;
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed

        // Simulate file loading
        LoadingIndicator.setWaitCursor(parent);
        printBill();
        LoadingIndicator.resetCursor(parent);

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyReleased
        String regex = "^[1-9][0-9]*[.]?([0-9]{1,2})?";

        String input = jTextField8.getText();
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            jTextField8.setText("");
        }
    }//GEN-LAST:event_jTextField8KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
