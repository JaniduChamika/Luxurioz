/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui.dialogbox;

import gui.panel.LeaseLease;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import model.CustomButtonUI;
import model.CustomDateChooser;
import model.CustomeMouseAdapter;
import model.LoadingIndicator;
import model.MySQL;
import model.StaticComponent;
import pdfwriter.LeaseDataModel;
import pdfwriter.PdfWriter;

/**
 *
 * @author Janidu
 */
public class LeaseRegistration extends javax.swing.JDialog {

    /**
     * Creates new form LeaseRegistration
     */
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("#.00");
    LeaseLease panel;
    String leasid;

    public LeaseRegistration(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        startLisners();
        setNewButtonUI();
        CustomDateChooser.customizeDateChooser(jDateChooser1);
        CustomDateChooser.customizeDateChooser(jDateChooser2);

    }

    public LeaseRegistration(java.awt.Frame parent, boolean modal, LeaseLease panel) {
        super(parent, modal);
        initComponents();
        startLisners();
        setNewButtonUI();
        CustomDateChooser.customizeDateChooser(jDateChooser1);
        CustomDateChooser.customizeDateChooser(jDateChooser2);
        this.panel = panel;
    }

    public LeaseRegistration(java.awt.Frame parent, boolean modal, LeaseLease panel, String leasId) {
        super(parent, modal);
        initComponents();
        startLisners();
        setNewButtonUI();
        CustomDateChooser.customizeDateChooser(jDateChooser1);
        CustomDateChooser.customizeDateChooser(jDateChooser2);
        this.panel = panel;

        jTextField11.setEnabled(false);
        jTextField5.setEnabled(false);
        jDateChooser1.setEnabled(false);

        loadFiled(leasId);
        this.leasid = leasId;

    }

    private void setNewButtonUI() {

        jButton1.setUI(new CustomButtonUI(0));

    }

    private void startLisners() {

        // Add a MouseListener to the button
        jButton1.addMouseListener(new CustomeMouseAdapter(jButton1));

    }
    Date enddate;

    private void loadFiled(String leasId) {
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

                String fname = rs.getString("customer.fname");
                String lname = rs.getString("customer.lname");
                String mobile = rs.getString("customer.mobile");

                String name = fname + " " + lname;
                jTextField11.setText(rs.getString("customer.nic"));
                jLabel22.setText(name);
                jLabel21.setText(mobile);

                String floor = rs.getString("apartment.floor");
                String no_room = rs.getString("apartment.num_room");
                String no_bathroom = rs.getString("apartment.num_bathroom");
                String quality = rs.getString("quality_level.name");
                String type = rs.getString("apartment_type.name");
                String condition = rs.getString("condition.name");
                String rentRate = rs.getString("rent_rate");
                String advance = rs.getString("advance_rate");
                String qualityAdditionRate = rs.getString("quality_level.addion_rate");
                String conditionAdditionRate = rs.getString("condition.addion_rate");
                String typeAdditionRate = rs.getString("apartment_type.addition_rate");
                String apartmentId = rs.getString("apartment.id");
                Date startdate = rs.getDate("lease.start_date");
                this.enddate = rs.getDate("lease.end_date");
                String status = rs.getString("apartment_status.name");
                double totalRentRate = Double.parseDouble(rentRate) + Double.parseDouble(qualityAdditionRate) + Double.parseDouble(conditionAdditionRate) + Double.parseDouble(typeAdditionRate);

                jLabel14.setText(floor);
                jLabel15.setText(no_room);
                jLabel16.setText(no_bathroom);
                jLabel17.setText(quality);
                jLabel18.setText(type);
                jLabel19.setText(condition);
                jLabel23.setText(df.format(totalRentRate));
                jLabel25.setText(advance);
                jTextField5.setText(apartmentId);
                jDateChooser1.setDate(startdate);
                jDateChooser2.setDate(this.enddate);
                jLabel28.setText(status);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upgrateDate() {
        Date enddate = jDateChooser2.getDate();
        if (this.enddate.after(enddate) && jButton1.getText().equals("Upgrade")) {
            JOptionPane.showMessageDialog(this, "New end date should be future date from previous end date", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (this.enddate.before(enddate) && jButton1.getText().equals("Downgrade")) {
            JOptionPane.showMessageDialog(this, "New end date should be before date from previous end date", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {
            MySQL.iud("UPDATE `lease` SET `end_date`='" + sdf.format(enddate) + "' WHERE `uniqeid`='" + this.leasid + "'");
            panel.loadLease();
            JOptionPane.showMessageDialog(this, this.leasid + " Lease upgraded success", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
            this.dispose();
        }

    }

    private void resetfiled() {
        jLabel22.setText("N/A");
        jLabel21.setText("N/A");
        jLabel14.setText("N/A");
        jLabel15.setText("N/A");
        jLabel16.setText("N/A");
        jLabel17.setText("N/A");
        jLabel18.setText("N/A");
        jLabel19.setText("N/A");
        jLabel23.setText("N/A");
        jLabel25.setText("N/A");
        jLabel28.setText("N/A");

        jTextField11.setText("");
        jTextField5.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
    }

    private void loadcustomer() {
        String nic = jTextField11.getText();

        try {
            if (!nic.isEmpty() && nic.length() > 8) {
                ResultSet rs;
                String query = "SELECT * FROM `customer`"
                        + "WHERE (`customer`.`nic` = '" + nic + "')";

                rs = MySQL.search(query);
                if (rs.next()) {

                    String initial = rs.getString("customer.initial");
                    String fname = rs.getString("customer.fname");
                    String lname = rs.getString("customer.lname");
                    String mobile = rs.getString("customer.mobile");

                    String name = initial+" "+fname + " " + lname;
                    jLabel22.setText(name);
                    jLabel21.setText(mobile);
                } else {
                    jLabel22.setText("N/A");
                    jLabel21.setText("N/A");
                }
            } else {
                jLabel22.setText("N/A");
                jLabel21.setText("N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadapartment() {
        String apartment_id = jTextField5.getText();

        try {

            ResultSet rs;
            String query = "SELECT * FROM apartment\n"
                    + "INNER JOIN `condition` ON apartment.condition_id=`condition`.id\n"
                    + "INNER JOIN `apartment_type` ON apartment.type_id=`apartment_type`.id\n"
                    + "INNER JOIN `quality_level` ON apartment.quality_id=`quality_level`.id\n"
                    + "INNER JOIN apartment_status ON apartment.apartment_status_id=apartment_status.id "
                    + "WHERE (`apartment`.`id` = '" + apartment_id + "')";

            rs = MySQL.search(query);
            if (rs.next()) {

                String floor = rs.getString("apartment.floor");
                String no_room = rs.getString("apartment.num_room");
                String no_bathroom = rs.getString("apartment.num_bathroom");
                String quality = rs.getString("quality_level.name");
                String type = rs.getString("apartment_type.name");
                String condition = rs.getString("condition.name");
                String rentRate = rs.getString("rent_rate");
                String advance = rs.getString("advance_rate");
                String qualityAdditionRate = rs.getString("quality_level.addion_rate");
                String conditionAdditionRate = rs.getString("condition.addion_rate");
                String typeAdditionRate = rs.getString("apartment_type.addition_rate");
                String status = rs.getString("apartment_status.name");
                double totalRentRate = Double.parseDouble(rentRate) + Double.parseDouble(qualityAdditionRate) + Double.parseDouble(conditionAdditionRate) + Double.parseDouble(typeAdditionRate);

                jLabel14.setText(floor);
                jLabel15.setText(no_room);
                jLabel16.setText(no_bathroom);
                jLabel17.setText(quality);
                jLabel18.setText(type);
                jLabel19.setText(condition);
                jLabel23.setText(df.format(totalRentRate));
                jLabel25.setText(advance);
                jLabel28.setText(status);
            } else {
                jLabel14.setText("N/A");
                jLabel15.setText("N/A");
                jLabel16.setText("N/A");
                jLabel17.setText("N/A");
                jLabel18.setText("N/A");
                jLabel19.setText("N/A");
                jLabel23.setText("N/A");
                jLabel25.setText("N/A");
                jLabel28.setText("N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AddLease() {
        String nic = jTextField11.getText();
        String apartment_id = jTextField5.getText();
        Date start_date = jDateChooser1.getDate();
        Date end_date = jDateChooser2.getDate();
        String name = jLabel22.getText();
        String floor = jLabel14.getText();
        String status = jLabel28.getText();

        // Calculating the difference in days
        if (nic.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter NIC", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (name.equals("N/A")) {
            JOptionPane.showMessageDialog(this, "Customer Not Found", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (apartment_id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Apartment ID", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (floor.equals("N/A")) {
            JOptionPane.showMessageDialog(this, "Apartment Not Found", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!status.equals("Vacant")) {
            JOptionPane.showMessageDialog(this, "Apartment Not Available for new lease", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (start_date == null) {
            JOptionPane.showMessageDialog(this, "Please Enter Start date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (start_date.before(new Date())) {
            JOptionPane.showMessageDialog(this, "Start date should be a future date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (end_date == null) {
            JOptionPane.showMessageDialog(this, "Please Enter End date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (end_date.before(start_date)) {
            JOptionPane.showMessageDialog(this, "End date should be a future date from start date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            LocalDate date1 = start_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();  // Current date
            LocalDate date2 = end_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();  //... initialize with another date
            if (ChronoUnit.DAYS.between(date1, date2) < 90) {
                JOptionPane.showMessageDialog(this, "Date Different should be more than 90 Days", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                try {

                    ResultSet rs1;
                    String query1 = "SELECT * FROM apartment\n"
                            + "INNER JOIN `condition` ON apartment.condition_id=`condition`.id\n"
                            + "INNER JOIN `apartment_type` ON apartment.type_id=`apartment_type`.id\n"
                            + "INNER JOIN `quality_level` ON apartment.quality_id=`quality_level`.id\n"
                            + "WHERE (`apartment`.`id` = '" + apartment_id + "')";

                    rs1 = MySQL.search(query1);
                    if (rs1.next()) {

                        String rentRate = rs1.getString("rent_rate");
                        String qualityId = rs1.getString("quality_level.id");
                        String qualityAdditionRate = rs1.getString("quality_level.addion_rate");
                        String conditionId = rs1.getString("condition.id");
                        String conditionAdditionRate = rs1.getString("condition.addion_rate");
                        String typeid = rs1.getString("apartment_type.id");
                        String typeAdditionRate = rs1.getString("apartment_type.addition_rate");
                        double advance_rate = rs1.getDouble("apartment.advance_rate");
                        double totalRentRate = Double.parseDouble(rentRate) + Double.parseDouble(qualityAdditionRate) + Double.parseDouble(conditionAdditionRate) + Double.parseDouble(typeAdditionRate);
                        long uniqeid = System.currentTimeMillis() - 700700000000L;

                        ResultSet rs2;
                        String query2 = "SELECT * FROM customer\n"
                                + "WHERE (`customer`.`nic` = '" + nic + "')";

                        rs2 = MySQL.search(query2);
                        if (rs2.next()) {

                            String cus_id = rs2.getString("cusid");
                            MySQL.iud("INSERT INTO lease (`uniqeid`,`total_rate`,`reg_date`,`customer_id`,`start_date`,`end_date`,`advance_rate`,`lease_status_id`) \n"
                                    + "VALUES ('" + uniqeid + "','" + totalRentRate + "','" + sdf.format(new Date()) + "','" + cus_id + "','" + sdf.format(start_date) + "','" + sdf.format(end_date) + "','" + advance_rate + "','4')");
                            MySQL.iud("INSERT INTO lease_has_apartment (`lease_uniqeid`,`apartment_id`,`apartment_type_id`,`type_addition`,`quality_level_id`,`quality_addition`,`condition_id`,`condition_addition`) \n"
                                    + "VALUES ('" + uniqeid + "','" + apartment_id + "','" + typeid + "','" + typeAdditionRate + "','" + qualityId + "','" + qualityAdditionRate + "','" + conditionId + "','" + conditionAdditionRate + "')");
                            MySQL.iud("INSERT INTO lease_account (`lease_uniqeid`,`current_balance`,`update_at`) \n"
                                    + "VALUES ('" + uniqeid + "','0.0','" + sdf.format(new Date()) + "')");
                            MySQL.iud("UPDATE `customer` SET `cus_status_id`='1' WHERE `cusid`='" + cus_id + "'");
                            MySQL.iud("UPDATE `apartment` SET `apartment_status_id`='2' WHERE `id`='" + apartment_id + "'");

                            resetfiled();
                            panel.loadLease();
                            LeaseDataModel ldm = new LeaseDataModel(new Date(), name, nic, apartment_id, start_date, end_date, totalRentRate, advance_rate);
                            PdfWriter pw = new PdfWriter(ldm);
                            pw.generateDoc(String.valueOf(uniqeid));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lease Registration");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(1380, 550));
        jPanel1.setPreferredSize(new java.awt.Dimension(1003, 540));

        jLabel1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel1.setText("Customer NIC");
        jLabel1.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel1.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel2.setText("Phone No");
        jLabel2.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel2.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel3.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel3.setText("Name");
        jLabel3.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel3.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel4.setText("Apertment ID");
        jLabel4.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel4.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel5.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel5.setText("Foor");
        jLabel5.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel5.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel6.setText("No of barthroom");
        jLabel6.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel6.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel7.setText("No of Room");
        jLabel7.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel7.setMinimumSize(new java.awt.Dimension(37, 20));

        jTextField5.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField5.setForeground(new java.awt.Color(0, 0, 0));
        jTextField5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField5.setPreferredSize(new java.awt.Dimension(0, 45));
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel8.setText("Quality");
        jLabel8.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel8.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel9.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel9.setText("Type");
        jLabel9.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel9.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel10.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel10.setText("Condition");
        jLabel10.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel10.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel11.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel11.setText("Start Date");
        jLabel11.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel11.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel12.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel12.setText("Total Rent Rate");
        jLabel12.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel12.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel13.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel13.setText("End Date");
        jLabel13.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel13.setMinimumSize(new java.awt.Dimension(37, 20));

        jTextField11.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField11.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField11.setPreferredSize(new java.awt.Dimension(0, 45));
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField11KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField11KeyTyped(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(153, 0, 0));
        jButton1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Print");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0), 2));
        jButton1.setContentAreaFilled(false);
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setText("N/A");
        jLabel14.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel14.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel15.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("N/A");
        jLabel15.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel15.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel16.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("N/A");
        jLabel16.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel16.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel17.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("N/A");
        jLabel17.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel17.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel18.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("N/A");
        jLabel18.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel18.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel19.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("N/A");
        jLabel19.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel19.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel20.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(102, 102, 102));
        jLabel20.setText("Rs.");
        jLabel20.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel20.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel21.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("N/A");
        jLabel21.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel21.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel22.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setText("N/A");
        jLabel22.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel22.setMinimumSize(new java.awt.Dimension(37, 20));

        jDateChooser1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jDateChooser1.setPreferredSize(new java.awt.Dimension(360, 45));

        jDateChooser2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jDateChooser2.setPreferredSize(new java.awt.Dimension(360, 45));

        jLabel23.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(102, 102, 102));
        jLabel23.setText("N/A");
        jLabel23.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel23.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel24.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setText("Rs.");
        jLabel24.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel24.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel25.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("N/A");
        jLabel25.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel25.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel26.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel26.setText("Security Payment");
        jLabel26.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel26.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel27.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel27.setText("Status");
        jLabel27.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel27.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel28.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(102, 102, 102));
        jLabel28.setText("N/A");
        jLabel28.setMaximumSize(new java.awt.Dimension(37, 20));
        jLabel28.setMinimumSize(new java.awt.Dimension(37, 20));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(96, 96, 96)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(145, 145, 145)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(36, 36, 36)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(266, 266, 266)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(96, 96, 96)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(145, 145, 145)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 764, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 764, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        LoadingIndicator.setWaitCursor(this);

        if (jButton1.getText().equals("Print")) {
            AddLease();
        } else {
            upgrateDate();
        }
        LoadingIndicator.resetCursor(this);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField11KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyTyped
        String regex = "^[0-9]{0,13}(v)?";
        char value = evt.getKeyChar();
        String input = jTextField11.getText() + value;
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField11KeyTyped

    private void jTextField11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyReleased

        loadcustomer();


    }//GEN-LAST:event_jTextField11KeyReleased

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        loadapartment();
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        String regex = "[0-9]";
        char value = evt.getKeyChar();

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(String.valueOf(value));
        String input = jTextField5.getText() + value;
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField5KeyTyped

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
            java.util.logging.Logger.getLogger(LeaseRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeaseRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeaseRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeaseRegistration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LeaseRegistration dialog = new LeaseRegistration(new javax.swing.JFrame(), true);
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
    public com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    public javax.swing.JTextField jTextField11;
    public javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
