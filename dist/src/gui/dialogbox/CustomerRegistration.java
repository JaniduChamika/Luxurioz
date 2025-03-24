/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui.dialogbox;

import gui.panel.EditCustomer;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import model.CustomButtonUI;
import model.CustomDateChooser;
import model.CustomeMouseAdapter;
import model.MobileValidator;
import model.MySQL;
import model.StaticComponent;

/**
 *
 * @author Janidu
 */
public class CustomerRegistration extends javax.swing.JDialog {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Creates new form CustomerRegistration
     */
    EditCustomer panel;
    String nic;

    public CustomerRegistration(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setNewButtonUI();
        startLisners();
        loadTitle();
        loadcountry();
        loadgender();
        loadmeritalstatus();
        CustomDateChooser.customizeDateChooser(jDateChooser1);

    }

    public CustomerRegistration(java.awt.Frame parent, boolean modal, EditCustomer panel) {
        super(parent, modal);
        initComponents();
        setNewButtonUI();
        startLisners();
        loadTitle();
        loadcountry();
        loadgender();
        loadmeritalstatus();
        CustomDateChooser.customizeDateChooser(jDateChooser1);

        this.panel = panel;
    }

    public CustomerRegistration(java.awt.Frame parent, boolean modal, EditCustomer panel, String nic) {
        super(parent, modal);
        initComponents();
        setNewButtonUI();
        startLisners();
        loadTitle();
        loadcountry();
        loadgender();
        loadmeritalstatus();
        CustomDateChooser.customizeDateChooser(jDateChooser1);
        this.setTitle("Update Customer");
        this.jButton1.setText("Update");
        this.jComboBox1.setEnabled(false);
        this.jComboBox2.setEnabled(false);
        this.jTextField1.setEnabled(false);
        this.jTextField2.setEnabled(false);
        this.jTextField5.setEnabled(false);
        this.jTextField4.setEnabled(false);
        this.jComboBox4.setEnabled(false);
        this.jDateChooser1.setEnabled(false);
        this.panel = panel;
        this.nic = nic;
        loadCusInfo(nic);
    }

    private void setNewButtonUI() {

        jButton1.setUI(new CustomButtonUI(0));

    }

    private void startLisners() {

        // Add a MouseListener to the button
        jButton1.addMouseListener(new CustomeMouseAdapter(jButton1));

    }

    private void loadCusInfo(String nic) {
        try {
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM customer\n"
                    + "INNER JOIN country ON customer.country_id=country.id\n"
                    + "INNER JOIN gender ON customer.gender_id=gender.id\n"
                    + "INNER JOIN `cus_status` ON customer.cus_status_id=`cus_status`.id "
                    + "INNER JOIN marital_status ON customer.marital_id=marital_status.id "
                    + "INNER JOIN title ON customer.title_id=title.id "
                    + "WHERE  `nic` = '" + nic + "' ";

            rs = MySQL.search(query);
            if (rs.next()) {

                jTextField1.setText(nic);
                jTextField2.setText(rs.getString("initial"));
                jTextField5.setText(rs.getString("fname"));
                jTextField4.setText(rs.getString("lname"));
                jTextField9.setText(rs.getString("mobile"));
                jTextField10.setText(rs.getString("email"));
                jTextField11.setText(rs.getString("job_title"));
                jTextField13.setText(rs.getString("income"));
                jComboBox4.setSelectedItem(rs.getString("title.name"));
                jComboBox2.setSelectedItem(rs.getString("gender.name"));
                jComboBox3.setSelectedItem(rs.getString("marital_status.name"));
                jComboBox1.setSelectedItem(rs.getString("country.name"));
                jDateChooser1.setDate(rs.getDate("dob"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Map<String, String> title = new HashMap<>();

    public void loadTitle() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `title` ");
            Vector v = new Vector();
            v.add("Select Title");
            while (rs.next()) {
                String abc = rs.getString("name");
                v.add(abc);
                title.put(rs.getString("name"), rs.getString("id"));

            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            jComboBox4.setModel(dcm);
        } catch (Exception e) {

        }
    }
    Map<String, String> gender = new HashMap<>();

    public void loadgender() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `gender` ");
            Vector v = new Vector();
            v.add("Select Gender");
            while (rs.next()) {
                String abc = rs.getString("name");
                v.add(abc);
                gender.put(rs.getString("name"), rs.getString("id"));

            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            jComboBox2.setModel(dcm);
        } catch (Exception e) {

        }
    }
    Map<String, String> meritalstatus = new HashMap<>();

    public void loadmeritalstatus() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `marital_status` ");
            Vector v = new Vector();
            v.add("Select Status");
            while (rs.next()) {
                String abc = rs.getString("name");
                v.add(abc);
                meritalstatus.put(rs.getString("name"), rs.getString("id"));

            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            jComboBox3.setModel(dcm);
        } catch (Exception e) {

        }
    }
    Map<String, String> country = new HashMap<>();

    public void loadcountry() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `country` ");
            Vector v = new Vector();
            v.add("Select country");
            while (rs.next()) {
                String abc = rs.getString("name");
                v.add(abc);
                country.put(rs.getString("name"), rs.getString("id"));

            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            jComboBox1.setModel(dcm);
            jComboBox1.setSelectedItem("Sri Lanka");
        } catch (Exception e) {

        }
    }

    private void resetfeild() {
        jTextField1.setText("");
        jComboBox4.setSelectedIndex(0);
        jTextField2.setText("");
        jTextField5.setText("");
        jTextField4.setText("");
        jComboBox2.setSelectedIndex(0);
        jDateChooser1.setDate(null);
        jComboBox3.setSelectedIndex(0);
        jTextField9.setText("");
        jTextField10.setText("");
        jTextField11.setText("");
        jTextField13.setText("");
        jComboBox1.setSelectedItem("Sri Lanka");
    }

    private void cusreg() {
        String nic = jTextField1.getText();
        String title = jComboBox4.getSelectedItem().toString();
        String intial = jTextField2.getText();
        String firstname = jTextField5.getText();
        String lastname = jTextField4.getText();
        String gender = jComboBox2.getSelectedItem().toString();
        Date dob = jDateChooser1.getDate();
        String meritalstatus = jComboBox3.getSelectedItem().toString();
        String mobile = jTextField9.getText();
        String email = jTextField10.getText();
        String jobtitle = jTextField11.getText();
        String income = jTextField13.getText();
        String country = jComboBox1.getSelectedItem().toString();
        if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter nic", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (nic.length()<9) {
            JOptionPane.showMessageDialog(this, "Invalid NIC", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (title.equals("Select Title")) {
            JOptionPane.showMessageDialog(this, "Please select Title", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (intial.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter intial", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (firstname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select first name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (lastname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter last name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (gender.equals("Select Gender")) {
            JOptionPane.showMessageDialog(this, "Please select gender", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (dob == null) {
            JOptionPane.showMessageDialog(this, "Please select Date of Birth", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (meritalstatus.equals("Select Status")) {
            JOptionPane.showMessageDialog(this, "Please select merital status", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter mobile", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("07[1,2,4,5,6,7,8][0-9]{7}").matcher(mobile).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!email.isEmpty() && !Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$").matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jobtitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter job title", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (income.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer monthly income", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (country.equals("Select country")) {
            JOptionPane.showMessageDialog(this, "Please select country", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                String quary = "INSERT INTO `customer` (`nic`,`initial`,`fname`,`lname`,`mobile`,"
                        + "`email`,`dob`,`reg_date`,`job_title`,`income`,`gender_id`"
                        + ",`title_id`,`country_id`,`marital_id`,`cus_status_id`) "
                        + "VALUES ('" + nic + "','" + intial + "','" + firstname + "','" + lastname + "','" + mobile + "',"
                        + "'" + email + "','" + sdf.format(dob) + "','" + sdf.format(new Date()) + "','" + jobtitle + "'"
                        + ",'" + income + "' ,'" + this.gender.get(gender) + "','" + this.title.get(title) + "'"
                        + ",'" + this.country.get(country) + "','" + this.meritalstatus.get(meritalstatus) + "','2')";
//                System.out.println(quary);
                MySQL.iud(quary);
                this.panel.loadCustomer();
                resetfeild();
                JOptionPane.showMessageDialog(this, "Customer registerd success", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cusedit() {
        String nic = jTextField1.getText();

        String mobile = jTextField9.getText();
        String email = jTextField10.getText();
        String jobtitle = jTextField11.getText();
        String income = jTextField13.getText();
        String meritalstatus = jComboBox3.getSelectedItem().toString();

        if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter nic", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (meritalstatus.equals("Select Status")) {
            JOptionPane.showMessageDialog(this, "Please select merital status", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter mobile", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("07[1,2,4,5,6,7,8][0-9]{7}").matcher(mobile).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!email.isEmpty() && !Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$").matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jobtitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter job title", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (income.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer monthly income", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {
                String quary = "UPDATE `customer` SET `mobile`='" + mobile + "',"
                        + "`email`='" + email + "',`job_title`='" + jobtitle + "',`income`='" + income + "'"
                        + ",`marital_id`='" + this.meritalstatus.get(meritalstatus) + "' "
                        + "WHERE `nic`='" + nic + "'";
                System.out.println(quary);
                MySQL.iud(quary);
                this.panel.loadCustomer();
                JOptionPane.showMessageDialog(this, "Customer updated success", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
                this.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Customer Registration");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(1380, 500));
        jPanel1.setPreferredSize(new java.awt.Dimension(1180, 564));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel1.setText("NIC");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 18, 188, -1));

        jTextField1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 48, 360, 45));

        jTextField2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(411, 129, 142, 45));

        jTextField4.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(881, 129, 274, 45));

        jTextField5.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(579, 129, 270, 45));

        jLabel2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel2.setText("Last Name");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(881, 99, 156, -1));

        jLabel3.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel3.setText("Title");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 99, 128, -1));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel4.setText("Initial");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(411, 99, 142, -1));

        jLabel5.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel5.setText("First Name");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(579, 99, 142, -1));

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel6.setText("Gender");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 128, -1));

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel7.setText("DOB");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(411, 180, 128, -1));

        jLabel8.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel8.setText("Merital Status");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 180, 128, -1));

        jLabel9.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel9.setText("Mobile");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 128, -1));

        jTextField9.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField9.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField9KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 360, 45));

        jLabel10.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel10.setText("E-mail");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 290, 128, -1));

        jTextField10.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField10.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 320, 744, 45));

        jLabel11.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel11.setText("Income (monthly)");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 400, 150, -1));

        jLabel12.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel12.setText("Job Title");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 128, -1));

        jLabel13.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel13.setText("Country");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 400, 128, -1));

        jTextField11.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField11.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField11KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 360, 45));

        jTextField13.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField13.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField13KeyTyped(evt);
            }
        });
        jPanel1.add(jTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 430, 356, 45));

        jButton1.setBackground(new java.awt.Color(153, 0, 0));
        jButton1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Register");
        jButton1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(), null), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(), javax.swing.BorderFactory.createCompoundBorder())));
        jButton1.setContentAreaFilled(false);
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 490, 360, 45));

        jLabel14.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 1120, 10));

        jLabel15.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 1120, 10));

        jComboBox1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 430, 360, 45));

        jComboBox2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jPanel1.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 360, 45));

        jDateChooser1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), null));
        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jPanel1.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 210, 350, 45));

        jComboBox3.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jPanel1.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 210, 360, 45));

        jComboBox4.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 360, 45));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jButton1.getText().equals("Register")) {
            cusreg();
        } else {
            cusedit();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        String regex = "^[0-9]{0,13}(v)?";
        char value = evt.getKeyChar();
        String input = jTextField1.getText() + value;
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        char value = evt.getKeyChar();
        String regex = "^[0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value + "");

        if (matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        char value = evt.getKeyChar();
        String regex = "^[0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value + "");

        if (matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTextField11KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyTyped
        char value = evt.getKeyChar();
        String regex = "^[0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value + "");

        if (matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField11KeyTyped

    private void jTextField13KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13KeyTyped
        char value = evt.getKeyChar();
        String regex = "^[0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value + "");

        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField13KeyTyped
    MobileValidator mobilevalid = new MobileValidator();
    private void jTextField9KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyTyped
        String t = String.valueOf(evt.getKeyChar());
        String old = jTextField9.getText();
        String newtext = old + "" + t;

        if (mobilevalid.mobileValidate(newtext)) {
            evt.consume();
        }

    }//GEN-LAST:event_jTextField9KeyTyped

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        char value = evt.getKeyChar();
        String regex = "^[0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value + "");

        if (matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField2KeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CustomerRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CustomerRegistration dialog = new CustomerRegistration(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButton1;
    public javax.swing.JComboBox<String> jComboBox1;
    public javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    public javax.swing.JComboBox<String> jComboBox4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
