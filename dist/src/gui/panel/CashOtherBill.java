/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import email.EmailSenderWithAttachment;
import gui.frame.Cashier;
import java.awt.Color;

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
import model.CustomDateChooser;
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

import report.model.OtherBill;
import report.model.OtherInvoice;

/**
 *
 * @author Hiranya
 */
public class CashOtherBill extends javax.swing.JPanel {

    /**
     * Creates new form CashOtherBill
     */
    DecimalFormat df = new DecimalFormat("#0.00");

    SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
    Cashier parent;

    public CashOtherBill(JFrame parent) {
        initComponents();
        this.parent = (Cashier) parent;
        CustomTable custable = new CustomTable();
        custable.modifyLayout(jTable1);
        CustomDateChooser.customizeOnlyCalander(jDateChooser1);
        CustomDateChooser.customizeOnlyCalander(jDateChooser2);
        this.setBackground(new Color(255, 255, 255, 0));

        jPanel1.setBackground(new Color(255, 255, 255, 190));
        jPanel2.setBackground(new Color(255, 255, 255, 190));
        setNewButtonUI();
        startLisners();
        loadBillCategory();
        loadPaymentMethod();
        loadOtherBill();

    }

    private void setNewButtonUI() {

        jButton1.setUI(new CustomButtonUI(0));

    }

    private void startLisners() {

        // Add a MouseListener to the button
        jButton1.addMouseListener(new CustomeMouseAdapter(jButton1));

    }

    Map<String, String> paymentMethod = new HashMap<>();

    public void loadPaymentMethod() {
        try {

            Vector v = new Vector();
            v.add("Select");
            ResultSet rs = MySQL.search("SELECT * FROM pay_method ");
            while (rs.next()) {
                v.add(rs.getString("name"));
                paymentMethod.put(rs.getString("name"), rs.getString("id"));
            }
            jComboBox3.setModel(new DefaultComboBoxModel<>(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Map<String, String> billCategory = new HashMap<>();

    public void loadBillCategory() {
        try {

            Vector v = new Vector();
            v.add("Select Category");
            ResultSet rs = MySQL.search("SELECT * FROM category WHERE name NOT IN ('Rent Fee','Advance Payment','Late Fee','Rental Fee')");
            while (rs.next()) {
                v.add(rs.getString("name"));
                billCategory.put(rs.getString("name"), rs.getString("id"));
            }
            jComboBox2.setModel(new DefaultComboBoxModel<>(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadOtherBill() {
        Date fromDate = jDateChooser1.getDate();
        Date toDate = jDateChooser2.getDate();
        String text = jTextField3.getText();
        String category = jComboBox2.getSelectedItem().toString();
        try {

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM bill_invoice\n"
                    + "INNER JOIN bill_issue ON bill_issue.id=bill_invoice.bill_id\n"
                    + "INNER JOIN pay_method ON bill_invoice.method_id=pay_method.id\n"
                    + "INNER JOIN customer ON bill_issue.customer_cusid=customer.cusid\n"
                    + "INNER JOIN category ON bill_issue.category_id=category.id"
                    + " WHERE category.name NOT IN ('Rent Fee','Advance Payment','Late Fee','Rental Fee') AND (`nic` LIKE '" + text + "%' OR `mobile` LIKE '" + text + "%' OR `fname` LIKE '" + text + "%' OR `lname` LIKE '" + text + "%' OR `invoid` LIKE '" + text + "%' OR `bill_issue`.`uniqueid` LIKE '" + text + "%')";

            if (category != "Select Category") {

                query = query + " AND category.name = '" + category + "' ";

            }
            if (fromDate != null) {
                String date = sdm.format(fromDate);
                query = query + " AND paydate >= '" + date + "'";
            }
            if (toDate != null) {
                String date = sdm.format(toDate);

                query = query + " AND paydate <= '" + date + "' ";

            }
            rs = MySQL.search(query);
            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("invoid"));
                v.add(rs.getString("uniqueid"));
                v.add(rs.getString("category.name"));
                v.add(rs.getString("nic"));
                v.add(rs.getString("pay_method.name"));
                v.add(rs.getString("paydate"));
                v.add(rs.getString("paytime"));
                v.add(df.format(Double.parseDouble(rs.getString("amount"))));
                v.add(df.format(Double.parseDouble(rs.getString("balance"))));
                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    OtherBill bill;

    private void loadBillInfo() {

        String bill = jTextField1.getText();

        try {

            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM bill_issue\n"
                    + "INNER JOIN customer ON bill_issue.customer_cusid=customer.cusid\n"
                    + "INNER JOIN bill_issue_status ON bill_issue.bill_issue_status_id=bill_issue_status.id\n"
                    + "INNER JOIN category ON bill_issue.category_id=category.id\n"
                    + "WHERE bill_issue.uniqueid =  '" + bill + "'";

            rs = MySQL.search(query);
            if (rs.next()) {
                String cusname = rs.getString("customer.fname") + " " + rs.getString("customer.lname");
                String status = rs.getString("bill_issue_status.name");
                if (status.equals("Paid")) {
                    jButton1.setEnabled(false);
                } else {
                    jButton1.setEnabled(true);

                }
                jTextField4.setText(rs.getString("category.name"));
                jTextField5.setText(rs.getString("customer.nic"));
                jTextField6.setText(cusname);
                jTextField7.setText(df.format(Double.parseDouble(rs.getString("bill_issue.amount"))));
                jTextField8.setText(rs.getString("bill_issue.issue_date"));
                jTextField10.setText(status);
                this.bill = new OtherBill(rs.getString("bill_issue.id"), cusname, rs.getString("customer.mobile"), rs.getString("customer.email"), rs.getString("category.name"));
            } else {
                resetInfo();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetInfo() {
        jTextField2.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("0.00");
        jTextField8.setText("");
        jTextField9.setText("0.00");
        jTextField10.setText("");

        jTextField9.setForeground(Color.BLACK);
        jComboBox3.setSelectedIndex(0);
    }

    private void resetFild() {
        resetInfo();
        jTextField1.setText("");

    }

    private void calculateBalance() {
        String pay = jTextField2.getText();
        if (!pay.isEmpty()) {
            double payment = Double.parseDouble(pay);
            double amount = Double.parseDouble(jTextField7.getText());
            double balance = payment - amount;
            jTextField9.setText(df.format(balance));
            if (balance >= 0) {
                jTextField9.setForeground(Color.BLACK);
            } else {
                jTextField9.setForeground(Color.red);
            }
        } else {
            jTextField7.setText("0.00");

        }
    }

    private void addBillInvoice() {
        String id = jTextField1.getText();
        String category = jTextField4.getText();

        String method = jComboBox3.getSelectedItem().toString();

        String payment = jTextField2.getText();
        String balance = jTextField9.getText();
        String total = jTextField7.getText();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please Enter Bill ID", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (category.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Bill not found. Please check bill ID", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (method.equals("Select")) {
            JOptionPane.showMessageDialog(parent, "Please select payment method", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (payment.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter payment", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (Double.parseDouble(balance) < 0) {
            JOptionPane.showMessageDialog(parent, "Low payment", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            Date today = new Date();
            long invoid = System.currentTimeMillis() - 700000000000L;
            MySQL.iud("INSERT INTO bill_invoice (invoid,amount,balance,paydate,paytime,bill_id,method_id) \n"
                    + "VALUES ('" + invoid + "','" + payment + "','" + balance + "','" + sdm.format(today) + "','" + stf.format(today) + "','" + this.bill.getId() + "','" + paymentMethod.get(method) + "')");

            MySQL.iud("UPDATE bill_issue SET `bill_issue_status_id`='2' WHERE `id`='" + this.bill.getId() + "'");
            resetFild();
            loadOtherBill();

            printBillInvoice(new OtherInvoice(String.valueOf(invoid), sdm.format(today), this.bill.getCustomer(), this.bill.getMobile(), bill.getEmail(), category, total, String.valueOf(invoid), df.format(Double.parseDouble(payment)), df.format(Double.parseDouble(balance)), method));
        }
    }

    private void printBillInvoice(OtherInvoice invoice) {

        String pathStream = "src/report/jasper/other_bill_invoice.jasper";

        HashMap parameters = new HashMap();
        parameters.put("invoice", invoice.getId());
        parameters.put("date", invoice.getDate());
        parameters.put("customer", invoice.getCustomer());
        parameters.put("mobile", invoice.getMobile());
        parameters.put("category", invoice.getCategory());
        parameters.put("barcode", invoice.getBarcode());
        parameters.put("balance", invoice.getBalance());
        parameters.put("payment", invoice.getPayment());
        parameters.put("total", invoice.getTotal());
        parameters.put("method", invoice.getMethod());
        String outputPath = "report/otherbill/" + invoice.getId() + ".pdf";
        try {
            JREmptyDataSource dataSource = new JREmptyDataSource();
            JasperPrint jp = JasperFillManager.fillReport(pathStream, parameters, dataSource);
//            JasperViewer.viewReport(jp, false);

            JasperExportManager.exportReportToPdfFile(jp, outputPath);
            openPDF(outputPath);
            String email = invoice.getEmail();

            if (email != null && !email.isEmpty()) {
                String pdfFilePath = outputPath;
                String fileName = invoice.getId() + ".pdf";
                sendEmailAttachment(email, pdfFilePath, fileName, invoice.getCategory());
            }
        } catch (JRRuntimeException e) {
            /*  when file using from other software*/
            openPDF(outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void printBill() {
        String invoid = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

        String nic = jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString();
        String pathStream = "src/report/jasper/other_bill_invoice.jasper";
        String payment = jTable1.getValueAt(jTable1.getSelectedRow(), 7).toString();
        String balance = jTable1.getValueAt(jTable1.getSelectedRow(), 8).toString();
        String total = df.format(Double.parseDouble(payment) - Double.parseDouble(balance));
        String outputPath = "report/otherbill/" + invoid + ".pdf";
        String category = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString();
        try {

            ResultSet rs = MySQL.search("SELECT * FROM customer WHERE `nic` = '" + nic + "'");
            if (rs.next()) {
                String name = rs.getString("customer.fname") + " " + rs.getString("customer.lname");

                HashMap parameters = new HashMap();
                parameters.put("invoice", invoid);
                parameters.put("date", jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString());
                parameters.put("customer", name);
                parameters.put("mobile", rs.getString("customer.mobile"));
                parameters.put("category", category);
                parameters.put("barcode", invoid);
                parameters.put("payment", payment);
                parameters.put("balance", balance);
                parameters.put("total", total);
                parameters.put("method", jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString());

                JREmptyDataSource dataSource = new JREmptyDataSource();
                JasperPrint jp = JasperFillManager.fillReport(pathStream, parameters, dataSource);
                /* JasperViewer.viewReport(jp, false);*/

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
        String sub = "Invoice for " + category + " payment";
        String body = "I hope this message finds you well. Thank you for your recent payment; "
                + "we appreciate your prompt attention to the outstanding charges. "
                + "Below, please find the receipt confirming the payment for the specified charges: ";
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
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jTextField3 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();

        jPopupMenu1.setPreferredSize(new java.awt.Dimension(200, 40));

        jMenuItem1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-print-20.png"))); // NOI18N
        jMenuItem1.setText("Print");
        jMenuItem1.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1380, 940));

        jPanel2.setPreferredSize(new java.awt.Dimension(1380, 940));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel4.setText("Bill History");
        jLabel4.setPreferredSize(null);

        jComboBox2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Category", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.setPreferredSize(new java.awt.Dimension(220, 30));
        jComboBox2.setRequestFocusEnabled(false);
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel5.setText("Search");
        jLabel5.setPreferredSize(null);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Invoice ID", "Bill ID", "Category", "Customer NIC", "Method", "Date", "Time", "Paid", "Balance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel6.setText("From Date");
        jLabel6.setPreferredSize(null);

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel7.setText("To Date");
        jLabel7.setPreferredSize(null);

        jDateChooser1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jDateChooser1.setPreferredSize(new java.awt.Dimension(220, 30));
        jDateChooser1.setRequestFocusEnabled(false);
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jDateChooser2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jDateChooser2.setPreferredSize(new java.awt.Dimension(220, 30));
        jDateChooser2.setRequestFocusEnabled(false);
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jTextField3.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField3.setPreferredSize(new java.awt.Dimension(220, 30));
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-clear-24.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-clear-24.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 172, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jButton2)
                        .addComponent(jButton3))
                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        jPanel1.setPreferredSize(new java.awt.Dimension(1380, 293));

        jLabel19.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(0, 0, 0)));

        jLabel11.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel11.setText("Customer Name");

        jButton1.setBackground(new java.awt.Color(102, 0, 0));
        jButton1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Print");
        jButton1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(), javax.swing.BorderFactory.createCompoundBorder()));
        jButton1.setContentAreaFilled(false);
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel8.setText("Payment");

        jLabel1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel1.setText("Bill ID");

        jTextField1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField1.setPreferredSize(new java.awt.Dimension(400, 45));
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel21.setText("Issues Date");

        jLabel2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel2.setText("Category");

        jComboBox3.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jComboBox3.setPreferredSize(new java.awt.Dimension(400, 45));

        jLabel12.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel12.setText("Customer NIC");
        jLabel12.setFocusable(false);
        jLabel12.setInheritsPopupMenu(false);
        jLabel12.setRequestFocusEnabled(false);

        jTextField2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField2.setPreferredSize(new java.awt.Dimension(400, 45));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel10.setText("Amount (Rs)");

        jLabel9.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel9.setText("Balance (Rs)");

        jLabel3.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel3.setText("Parment method");

        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(255, 255, 255));
        jTextField4.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField4.setForeground(new java.awt.Color(51, 51, 51));
        jTextField4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField4.setPreferredSize(new java.awt.Dimension(400, 45));

        jTextField5.setEditable(false);
        jTextField5.setBackground(new java.awt.Color(255, 255, 255));
        jTextField5.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField5.setForeground(new java.awt.Color(51, 51, 51));
        jTextField5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField5.setPreferredSize(new java.awt.Dimension(400, 45));

        jTextField6.setEditable(false);
        jTextField6.setBackground(new java.awt.Color(255, 255, 255));
        jTextField6.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField6.setForeground(new java.awt.Color(51, 51, 51));
        jTextField6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField6.setPreferredSize(new java.awt.Dimension(400, 45));

        jTextField7.setEditable(false);
        jTextField7.setBackground(new java.awt.Color(255, 255, 255));
        jTextField7.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField7.setText("0.00");
        jTextField7.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField7.setPreferredSize(new java.awt.Dimension(400, 45));

        jTextField8.setEditable(false);
        jTextField8.setBackground(new java.awt.Color(255, 255, 255));
        jTextField8.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField8.setForeground(new java.awt.Color(51, 51, 51));
        jTextField8.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField8.setPreferredSize(new java.awt.Dimension(400, 45));

        jTextField9.setEditable(false);
        jTextField9.setBackground(new java.awt.Color(255, 255, 255));
        jTextField9.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField9.setText("0.00");
        jTextField9.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField9.setPreferredSize(new java.awt.Dimension(400, 45));
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField9KeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel13.setText("Status");

        jTextField10.setEditable(false);
        jTextField10.setBackground(new java.awt.Color(255, 255, 255));
        jTextField10.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField10.setForeground(new java.awt.Color(51, 51, 51));
        jTextField10.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField10.setPreferredSize(new java.awt.Dimension(400, 45));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(71, 71, 71)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9))
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        LoadingIndicator.setWaitCursor(parent);
        addBillInvoice();
        LoadingIndicator.resetCursor(parent);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        String id = jTextField1.getText();
        if (id.length() >= 13) {
            loadBillInfo();
        } else {
            resetInfo();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        String regex = "^[0-9]{0,13}";
        char value = evt.getKeyChar();
        String input = jTextField1.getText() + value;
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        String regex = "^[1-9][0-9]*[.]?([0-9]{1,2})?";

        String input = jTextField2.getText();
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            jTextField2.setText("");
        }
        calculateBalance();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9KeyReleased

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadOtherBill();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        loadOtherBill();
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        loadOtherBill();
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadOtherBill();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jDateChooser1.setDate(null);
        loadOtherBill();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jDateChooser2.setDate(null);
        loadOtherBill();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        LoadingIndicator.setWaitCursor(parent);
        printBill();
        LoadingIndicator.resetCursor(parent);

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.getSelectedRow() != -1 && evt.getButton() == 3) {
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        String regex = "^[1-9][0-9]*[.]?([0-9]{1,2})?";
        char value = evt.getKeyChar();
        String input = jTextField2.getText() + value;
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField2KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
