/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import email.EmailSender;
import gui.dialogbox.Role;
import java.awt.Color;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
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

import model.MobileValidator;
import model.MySQL;
import model.StaticComponent;

/**
 *
 * @author Hiranya
 */
public class AdminUsers extends javax.swing.JPanel {

    /**
     * Creates new form AdminP1
     */
    JFrame parent;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Map<String, String> userRole = new HashMap<>();

    public AdminUsers(JFrame parent) {
        initComponents();
        CustomDateChooser.customizeDateChooser(jdob);
        this.setBackground(new Color(0, 0, 0, 0));
        this.parent = parent;
        startLisners();
        setNewButtonUI();
        jPanel1.setBackground(new Color(255, 255, 255, 190));
        jPanel4.setBackground(new Color(255, 255, 255, 190));
        jPanel2.setBackground(new Color(255, 255, 255, 190));
        CustomTable custable = new CustomTable();
        custable.modifyLayout(jTable1);
//        jMenuItem1.addMouseListener(new HoverMouseListener(jMenuItem1

        loadRole();
        loadUser();
    }

    private void startLisners() {

        // Add a MouseListener to the button
        jButton1.addMouseListener(new CustomeMouseAdapter(jButton1));
        jButton3.addMouseListener(new CustomeMouseAdapter(jButton3));
        jButton2.addMouseListener(new CustomeMouseAdapter(jButton2));

    }

    private void setNewButtonUI() {
        jButton1.setUI(new CustomButtonUI(0));
        jButton3.setUI(new CustomButtonUI(0));
        jButton2.setUI(new CustomButtonUI(0));

    }

    public void setForEdit() {
        jButton1.setText("Update");
        juname.setEnabled(false);

    }

    public void filedInput() {
        int row = jTable1.getSelectedRow();

        String id = jTable1.getValueAt(row, 0).toString();

        try {
            ResultSet rs = MySQL.search("SELECT * FROM `user` WHERE `userId`='" + id + "'");
            if (rs.next()) {
                jfname.setText(rs.getString("fname"));
                jlname.setText(rs.getString("lname"));

                jdob.setDate(sdf.parse(rs.getString("dob")));
                juname.setText(rs.getString("username"));
                jemail.setText(rs.getString("email"));
                jmobile.setText(rs.getString("mobile"));

                for (Map.Entry entry : userRole.entrySet()) {
                    if (rs.getString("user_role_id").equals(entry.getValue())) {
                        jComboBox7.setSelectedItem(entry.getKey());
                    }
                }

            } else {
                JOptionPane.showMessageDialog(parent, "User not found! try again later", "Warning", JOptionPane.WARNING_MESSAGE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setForAdd() {
        jButton1.setText("Add");
        juname.setEnabled(true);
    }

    public void loadRole() {
        try {

            Vector v = new Vector();
            v.add("Select");
            ResultSet rs = MySQL.search("SELECT * FROM user_role");
            while (rs.next()) {
                v.add(rs.getString("name"));
                userRole.put(rs.getString("name"), rs.getString("id"));
            }
            jComboBox1.setModel(new DefaultComboBoxModel<>(v));
            jComboBox7.setModel(new DefaultComboBoxModel<>(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUser() {
        String role = jComboBox1.getSelectedItem().toString();
        String text = jTextField2.getText();

        try {

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            String query = "SELECT * FROM user INNER JOIN user_role ON user.user_role_id=user_role.id ";
            if (role == "Select") {
                query = query + " WHERE `fname` LIKE '" + text + "%' OR `lname` LIKE '" + text + "%' OR `mobile` LIKE '" + text + "%' OR `email` LIKE '" + text + "%' OR `username` LIKE '" + text + "%'";
            } else {
                query = query + " WHERE `name`='" + role + "' AND (`fname` LIKE '" + text + "%' OR `lname` LIKE '" + text + "%' OR `mobile` LIKE '" + text + "%' OR `email` LIKE '" + text + "%' OR `username` LIKE '" + text + "%')";

            }
            ResultSet rs = MySQL.search(query);
//            System.out.println(query);
            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("userId"));
                v.add(rs.getString("fname") + " " + rs.getString("lname"));
                v.add(rs.getString("email"));
                v.add(rs.getString("mobile"));
                v.add(rs.getString("dob"));
                v.add(rs.getString("username"));
                v.add(rs.getString("name"));
                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetFeild() {
        jfname.setText("");
        jlname.setText("");
        jdob.setDate(null);
        juname.setText("");
        jemail.setText("");
        jmobile.setText("");
        jComboBox7.setSelectedIndex(0);
        jpw.setText("");
        setForAdd();
    }

    private void addUser() {
        String fn = jfname.getText();
        String ln = jlname.getText();
        Date dob = jdob.getDate();
        String un = juname.getText();
        String email = jemail.getText();
        String mobile = jmobile.getText();
        String role = jComboBox7.getSelectedItem().toString();
        char[] pw = jpw.getPassword();
        String password = String.valueOf(pw);

        if (fn.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter first name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (ln.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter last name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter email address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$").matcher(email).matches()) {
            JOptionPane.showMessageDialog(parent, "Invalid email address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (dob == null) {
            JOptionPane.showMessageDialog(parent, "Invaild Date of birth", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("07[1,2,4,5,6,7,8][0-9]{7}").matcher(mobile).matches()) {
            JOptionPane.showMessageDialog(parent, "Invalid phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (un.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter username", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter password", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (password.length() < 5) {
            JOptionPane.showMessageDialog(parent, "Password length must be at least 5 character", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (role.equals("Select")) {
            JOptionPane.showMessageDialog(parent, "Please select user role", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {

                ResultSet rs2 = MySQL.search("SELECT * FROM `user` WHERE `username`='" + un + "'");
                if (rs2.next()) {
                    JOptionPane.showMessageDialog(parent, "Already exist user with this username", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    MySQL.iud("INSERT INTO `user` (`fname`,`lname`,`mobile`,`email`,`dob`,`username`,`password`,`user_role_id`,`user_status_id`) "
                            + "VALUES ('" + fn + "','" + ln + "','" + mobile + "','" + email + "','" + sdf.format(dob) + "','" + un + "','" + password + "','" + userRole.get(role) + "','1')");

                    loadUser();
                    resetFeild();
                    sendEmail(email, role, un);
                    JOptionPane.showMessageDialog(parent, "User registerd success", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void updateUser() {
        String fn = jfname.getText();
        String ln = jlname.getText();
        Date dob = jdob.getDate();
        String un = juname.getText();
        String email = jemail.getText();
        String mobile = jmobile.getText();
        String role = jComboBox7.getSelectedItem().toString();
        char[] pw = jpw.getPassword();
        String password = String.valueOf(pw);

        if (fn.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter first name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (ln.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter last name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter email address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$").matcher(email).matches()) {
            JOptionPane.showMessageDialog(parent, "Invalid email address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (dob == null) {
            JOptionPane.showMessageDialog(parent, "Invaild Date of birth", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("07[1,2,4,5,6,7,8][0-9]{7}").matcher(mobile).matches()) {
            JOptionPane.showMessageDialog(parent, "Invalid phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (un.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter username", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (role.equals("Select")) {
            JOptionPane.showMessageDialog(parent, "Please select user role", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                if (password.isEmpty()) {
                    MySQL.iud("UPDATE `user` SET `fname`='" + fn + "',`lname`='" + ln + "',`mobile`='" + mobile + "',`email`='" + email + "',`dob`='" + sdf.format(dob) + "',`user_role_id`='" + userRole.get(role) + "'"
                            + "WHERE `username`='" + un + "' ");

                } else if (password.length() < 5) {
                    JOptionPane.showMessageDialog(parent, "Password length must be at least 5 character", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    MySQL.iud("UPDATE `user` SET `fname`='" + fn + "',`lname`='" + ln + "',`mobile`='" + mobile + "',`email`='" + email + "',`dob`='" + sdf.format(dob) + "',`password`='" + password + "',`user_role_id`='" + userRole.get(role) + "'"
                            + "WHERE `username`='" + un + "' ");

                }

                loadUser();
                resetFeild();
                setForAdd();

                JOptionPane.showMessageDialog(parent, "User updated success", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void sendEmail(String email, String userRole, String un) {
        String sub = "Registration";
        String body = "Welcome to the Luxurioz Apartment Ltd. You were registered in Luxurioz Apartment Management System as " + userRole + ". Your username is " + un;

        EmailSender es = new EmailSender(email, sub, body);
        String status = es.sendMail();
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
                String status2 = es.sendMail();
                if (status2.equals("failed")) {
                    JOptionPane.showMessageDialog(parent, "Email Sending Failed", "Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(parent, "Email Sent Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
                }
            }
        } else {
//            JOptionPane.showMessageDialog(parent, "Email Sent Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
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
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jfname = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jemail = new javax.swing.JTextField();
        jmobile = new javax.swing.JTextField();
        jdob = new com.toedter.calendar.JDateChooser();
        jlname = new javax.swing.JTextField();
        juname = new javax.swing.JTextField();
        jpw = new javax.swing.JPasswordField();
        jComboBox7 = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jPopupMenu1.setPreferredSize(new java.awt.Dimension(200, 80));

        jMenuItem1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-edit-20.png"))); // NOI18N
        jMenuItem1.setText("Edit");
        jMenuItem1.setMargin(new java.awt.Insets(5, 6, 5, 6));
        jMenuItem1.setPreferredSize(new java.awt.Dimension(200, 90));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-lock-20.png"))); // NOI18N
        jMenuItem2.setText("Block");
        jMenuItem2.setMargin(new java.awt.Insets(5, 6, 5, 6));
        jMenuItem2.setPreferredSize(new java.awt.Dimension(200, 90));
        jPopupMenu1.add(jMenuItem2);

        setPreferredSize(new java.awt.Dimension(1380, 920));

        jPanel4.setPreferredSize(new java.awt.Dimension(1380, 554));

        jLabel2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel2.setText("User Info");

        jComboBox1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Role", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setPreferredSize(new java.awt.Dimension(129, 40));
        jComboBox1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox1PropertyChange(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel3.setText("Search");

        jTextField2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField2.setMinimumSize(new java.awt.Dimension(64, 30));
        jTextField2.setPreferredSize(new java.awt.Dimension(200, 28));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "User ID ", "Name", "Email", "Phone No", "DOB", "Username", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setEditingColumn(0);
        jTable1.setEditingRow(0);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setShowGrid(true);
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
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 814, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        jPanel1.setPreferredSize(new java.awt.Dimension(1380, 357));

        jPanel2.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel1.setText("First Name");

        jfname.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jfname.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jfname.setPreferredSize(new java.awt.Dimension(400, 45));
        jfname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jfnameActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel4.setText("Last Name");

        jLabel5.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel5.setText("Username");

        jLabel8.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel8.setText("Password");

        jLabel9.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel9.setText("Role");

        jLabel10.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel10.setText("Phone No");

        jLabel11.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel11.setText("Date of Birth");

        jLabel12.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel12.setText("Email");

        jLabel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jemail.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jemail.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jemail.setPreferredSize(new java.awt.Dimension(400, 45));

        jmobile.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jmobile.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jmobile.setPreferredSize(new java.awt.Dimension(400, 45));
        jmobile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jmobileKeyTyped(evt);
            }
        });

        jdob.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(255, 255, 255))));
        jdob.setDateFormatString("yyyy-MM-dd");
        jdob.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N

        jlname.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jlname.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jlname.setPreferredSize(new java.awt.Dimension(400, 45));

        juname.setBackground(new java.awt.Color(255, 255, 255));
        juname.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        juname.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        juname.setPreferredSize(new java.awt.Dimension(400, 45));

        jpw.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jpw.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));

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
        jButton3.setContentAreaFilled(false);
        jButton3.setOpaque(true);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(102, 0, 0));
        jButton1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add");
        jButton1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(), javax.swing.BorderFactory.createCompoundBorder()));
        jButton1.setContentAreaFilled(false);
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(102, 0, 0));
        jButton2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Clear");
        jButton2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(), javax.swing.BorderFactory.createCompoundBorder()));
        jButton2.setContentAreaFilled(false);
        jButton2.setOpaque(true);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jfname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jmobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdob, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addComponent(jLabel19)))
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jComboBox7, 0, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(juname, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpw, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(juname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpw, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                    .addComponent(jfname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jlname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jdob, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jmobile, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        LoadingIndicator.setWaitCursor(this);

        if (jButton1.getText().equals("Add")) {
            addUser();
        } else {
            updateUser();
        }
        LoadingIndicator.resetCursor(this);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Role role = new Role(parent, this, true);
        role.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jfnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jfnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jfnameActionPerformed
    MobileValidator mobilevalid = new MobileValidator();
    private void jmobileKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jmobileKeyTyped

        String t = String.valueOf(evt.getKeyChar());
        String old = jmobile.getText();
        String newtext = old + "" + t;

        if (mobilevalid.mobileValidate(newtext)) {
            evt.consume();
        }

    }//GEN-LAST:event_jmobileKeyTyped

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        if (jTable1.getSelectedRow() != -1 && evt.getButton() == 3) {
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        setForEdit();
        filedInput();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jComboBox1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox1PropertyChange
        loadUser();
    }//GEN-LAST:event_jComboBox1PropertyChange

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased

        loadUser();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        resetFeild();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    public javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    private com.toedter.calendar.JDateChooser jdob;
    private javax.swing.JTextField jemail;
    private javax.swing.JTextField jfname;
    private javax.swing.JTextField jlname;
    public javax.swing.JTextField jmobile;
    private javax.swing.JPasswordField jpw;
    private javax.swing.JTextField juname;
    // End of variables declaration//GEN-END:variables
}
