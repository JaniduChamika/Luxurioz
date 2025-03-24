/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import gui.dialogbox.AccessKey;
import gui.dialogbox.ApartmentDescription;
import gui.dialogbox.ApartmentInfo;
import gui.dialogbox.ApartmentType;
import gui.dialogbox.Condition;
import gui.dialogbox.Quality;
import java.awt.Color;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import model.MySQL;
import model.StaticComponent;

/**
 *
 * @author Hiranya
 */
public class PropApartment extends javax.swing.JPanel {

    /**
     * Creates new form ApartmentInfo
     */
    DecimalFormat df = new DecimalFormat("#.00");

    private JFrame parent;
//    private JComboBox apartmentType;

    public PropApartment(JFrame parent) {
        initComponents();
        this.parent = parent;
        this.setBackground(new Color(255, 255, 255, 0));
        jPanel5.setBackground(new Color(255, 255, 255, 190));
        jPanel1.setBackground(new Color(255, 255, 255, 190));
        jButton2.setOpaque(true);
        CustomTable custable = new CustomTable();
        custable.modifyLayout(jTable1);

        setNewButtonUI();
        startLisners();

        loadQuality();
        loadType();
        loadCondition();
        loadStatus();
        loadApartment();

    }

    private void startLisners() {

        // Add a MouseListener to the button
        jButton1.addMouseListener(new CustomeMouseAdapter(jButton1));
        jButton2.addMouseListener(new CustomeMouseAdapter(jButton2));
        jButton3.addMouseListener(new CustomeMouseAdapter(jButton3));
        jButton4.addMouseListener(new CustomeMouseAdapter(jButton4));
        jButton5.addMouseListener(new CustomeMouseAdapter(jButton5));

        jButton7.addMouseListener(new CustomeMouseAdapter(jButton7));

    }

    private void setNewButtonUI() {
        jButton1.setUI(new CustomButtonUI(0));
        jButton2.setUI(new CustomButtonUI(0));
        jButton3.setUI(new CustomButtonUI(0));
        jButton4.setUI(new CustomButtonUI(0));
        jButton5.setUI(new CustomButtonUI(0));

        jButton7.setUI(new CustomButtonUI(0));

    }

    private void viewApartmentType() {
        ApartmentType dbox = new ApartmentType(parent, true, this);
        dbox.setVisible(true);

    }

    private void viewCondition() {
        Condition dbox = new Condition(parent, true, this);
        dbox.setVisible(true);

    }

    private void viewQuality() {
        Quality dbox = new Quality(parent, true, this);
        dbox.setVisible(true);

    }
    Map<String, String> quality = new HashMap<>();

    public void loadQuality() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `quality_level` ");
            Vector v = new Vector();
            v.add("Select Quality");
            while (rs.next()) {
                String abc = rs.getString("name");
                v.add(abc);
                quality.put(rs.getString("name"), rs.getString("id"));

            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            jComboBox6.setModel(dcm);
        } catch (Exception e) {

        }
    }
    Map<String, String> type = new HashMap<>();

    public void loadType() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `apartment_type`");
            Vector v = new Vector();
            v.add("Select Facilities");
            while (rs.next()) {
                String abc = rs.getString("name");
                v.add(abc);
                type.put(rs.getString("name"), rs.getString("id"));

            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            DefaultComboBoxModel dcm1 = new DefaultComboBoxModel(v);
            jComboBox2.setModel(dcm);
            jComboBox7.setModel(dcm1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Map<String, String> condition = new HashMap<>();

    public void loadCondition() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `condition`");
            Vector v = new Vector();
            v.add("Select Condition");
            while (rs.next()) {
                String abc = rs.getString("name");
                v.add(abc);
                condition.put(rs.getString("name"), rs.getString("id"));

            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            DefaultComboBoxModel dcm1 = new DefaultComboBoxModel(v);
            jComboBox3.setModel(dcm);
            jComboBox5.setModel(dcm1);

        } catch (Exception e) {

        }
    }

    public void loadStatus() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `apartment_status` ");
            Vector v = new Vector();
            v.add("Select Status");
            while (rs.next()) {
                String abc = rs.getString("name");
                v.add(abc);
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            jComboBox4.setModel(dcm);
        } catch (Exception e) {

        }
    }

    public void loadApartment() {

        String type = jComboBox2.getSelectedItem().toString();
        String condition = jComboBox3.getSelectedItem().toString();
        String status = jComboBox4.getSelectedItem().toString();
        String text = jTextField3.getText();

        try {

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs;
            String query = "SELECT *\n"
                    + "FROM apartment\n"
                    + "INNER JOIN quality_level ON apartment.quality_id=quality_level.id\n"
                    + "INNER JOIN apartment_type ON apartment.type_id=apartment_type.id\n"
                    + "INNER JOIN `condition` ON apartment.condition_id=`condition`.id\n"
                    + "INNER JOIN apartment_status ON apartment.apartment_status_id=apartment_status.id\n"
                    + "WHERE (apartment.id LIKE '" + text + "%' OR apartment.description LIKE '%" + text + "%' OR floor LIKE '" + text + "%' ) ";

            if (!type.equals("Select Facilities")) {

                query = query + " AND apartment_type.name='" + type + "'";
            }

            if (!condition.equals("Select Condition")) {

                query = query + " AND `condition`.name='" + condition + "'";
            }

            if (!status.equals("Select Status")) {

                query = query + " AND apartment_status.name='" + status + "' ";
            }
            query = query + " ORDER BY `apartment`.`id` ASC";
            rs = MySQL.search(query);

            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("apartment.id"));
                v.add(rs.getString("floor"));
                v.add(rs.getString("apartment_type.name"));
                v.add(rs.getString("num_room"));
                v.add(rs.getString("num_bathroom"));
                v.add(rs.getString("quality_level.name"));
                v.add(rs.getString("condition.name"));
                v.add(rs.getString("apartment.description"));
                v.add(rs.getString("apartment_status.name"));

                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetInput() {
        jButton2.setText("Add");

        jTextField1.setText("");
        jTextField5.setText("");
        jComboBox7.setSelectedIndex(0);
        jTextField2.setText("");
        jTextField6.setText("");
        jComboBox6.setSelectedIndex(0);
        jTextField4.setText("");
        jTextField7.setText("");
        jComboBox5.setSelectedIndex(0);
        jTextField8.setText("");
        jTextField5.setEnabled(true);
        jTextField1.setEnabled(true);
    }

    private void addApartment() {
        String floor = jTextField1.getText();
        String apartmentId = jTextField5.getText();
        String apartmentType = jComboBox7.getSelectedItem().toString();
        String noOfRoom = jTextField2.getText();
        String noOfBathroom = jTextField6.getText();
        String Quality = jComboBox6.getSelectedItem().toString();
        String rentRate = jTextField4.getText();
        String advanceRate = jTextField7.getText();
        String conditionS = jComboBox5.getSelectedItem().toString();
        String description = jTextField8.getText();

        String apartTypeId = type.get(apartmentType);
        String qulityId = quality.get(Quality);
        String conditionId = this.condition.get(conditionS);

        if (floor.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter foor number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (apartmentId.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter apartment Id", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (apartTypeId == null) {
            JOptionPane.showMessageDialog(parent, "Please select apartment type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (noOfRoom.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter number of room", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (noOfBathroom.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter number of bathroom", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (qulityId == null) {
            JOptionPane.showMessageDialog(parent, "Please select qulity", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (rentRate.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter rent rate", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (advanceRate.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter advance rate", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (conditionId == null) {
            JOptionPane.showMessageDialog(parent, "Please select condition", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {

                MySQL.iud("INSERT INTO `apartment` (`id`,`floor`,`num_room`,`num_bathroom`,`description`,`quality_id`,`type_id`,`condition_id`,`apartment_status_id`,`advance_rate`,`rent_rate`) "
                        + "VALUES ('" + apartmentId + "','" + floor + "','" + noOfRoom + "','" + noOfBathroom + "','" + description.replace("'", "\\'") + "','" + qulityId + "','" + apartTypeId + "','" + conditionId + "','1','" + advanceRate + "','" + rentRate + "')");
                resetInput();
                loadApartment();

                JOptionPane.showMessageDialog(parent, "Apartment registerd success", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void filInput() {

        String apartmentId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
        ResultSet rs;
        String query = "SELECT *\n"
                + "FROM apartment\n"
                + "INNER JOIN quality_level ON apartment.quality_id=quality_level.id\n"
                + "INNER JOIN apartment_type ON apartment.type_id=apartment_type.id\n"
                + "INNER JOIN `condition` ON apartment.condition_id=`condition`.id\n"
                + "INNER JOIN apartment_status ON apartment.apartment_status_id=apartment_status.id\n"
                + "WHERE apartment.id = '" + apartmentId + "'";
        try {
            rs = MySQL.search(query);
            if (rs.next()) {
                if (rs.getString("apartment_status.name").equals("Reserved") || rs.getString("apartment_status.name").equals("Sold")) {
                    JOptionPane.showMessageDialog(parent, "Update option not available for Reserved or Sold Apartments", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    jTextField5.setEnabled(false);
                    jTextField1.setEnabled(false);
                    jButton2.setText("Update");
                    jTextField1.setText(rs.getString("floor"));
                    jTextField5.setText(rs.getString("apartment.id"));
                    jComboBox7.setSelectedItem(rs.getString("apartment_type.name"));
                    jTextField2.setText(rs.getString("apartment.num_room"));
                    jTextField6.setText(rs.getString("apartment.num_bathroom"));
                    jComboBox6.setSelectedItem(rs.getString("quality_level.name"));
                    jTextField4.setText(df.format(Double.parseDouble(rs.getString("apartment.rent_rate"))));
                    jTextField7.setText(df.format(Double.parseDouble(rs.getString("apartment.advance_rate"))));
                    jComboBox5.setSelectedItem(rs.getString("condition.name"));
                    jTextField8.setText(rs.getString("apartment.description"));
                }

            } else {
                JOptionPane.showMessageDialog(parent, "Record not found. Please try agin later", "Warning", JOptionPane.WARNING_MESSAGE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateApartment() {

        String apartmentId = jTextField5.getText();
        String apartmentType = jComboBox7.getSelectedItem().toString();
        String noOfRoom = jTextField2.getText();
        String noOfBathroom = jTextField6.getText();
        String Quality = jComboBox6.getSelectedItem().toString();
        String rentRate = jTextField4.getText();
        String advanceRate = jTextField7.getText();
        String conditionS = jComboBox5.getSelectedItem().toString();
        String description = jTextField8.getText();

        String apartTypeId = type.get(apartmentType);
        String qulityId = quality.get(Quality);
        String conditionId = this.condition.get(conditionS);

        if (apartTypeId.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Missing Infomation", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (apartTypeId.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please select apartment type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (noOfRoom.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter number of room", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (noOfBathroom.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter number of bathroom", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (qulityId.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please select qulity", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (rentRate.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter rent rate", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (advanceRate.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter advance rate", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (conditionId.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please select condition", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {

                MySQL.iud("UPDATE  `apartment` SET `num_room`='" + noOfRoom + "',`num_bathroom`='" + noOfBathroom + "',`description`='" + description + "',`quality_id`='" + qulityId + "',`type_id`='" + apartTypeId + "',`condition_id`='" + conditionId + "',`advance_rate`='" + advanceRate + "',`rent_rate`='" + rentRate + "' "
                        + "WHERE `id`='" + apartmentId + "'");
                resetInput();
                loadApartment();

                JOptionPane.showMessageDialog(parent, "Apartment update success", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteApartment() {
        String aprtmentId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
        ResultSet rs;
        String query = "SELECT 'table1' AS `table_name`, COUNT(0) AS `record_count`\n"
                + "FROM `lease_has_apartment`\n"
                + "WHERE (`lease_has_apartment`.`apartment_id` = '" + aprtmentId + "') UNION\n"
                + "SELECT 'table2' AS `table_name`, COUNT(0) AS `record_count`\n"
                + "FROM `apartment_door_lock`\n"
                + "WHERE (`apartment_door_lock`.`apartment_id` = '" + aprtmentId + "') UNION\n"
                + "SELECT 'table3' AS `table_name`, COUNT(0) AS `record_count`\n"
                + "FROM `door_lock`\n"
                + "WHERE (`door_lock`.`app_id` = '" + aprtmentId + "') UNION\n"
                + "SELECT 'table3' AS `table_name`, COUNT(0) AS `record_count`\n"
                + "FROM `item`\n"
                + "WHERE (`item`.`apartment_id` = '" + aprtmentId + "')";
        try {
            rs = MySQL.search(query);
            boolean haveRecord = false;
            while (rs.next()) {
                if (Integer.parseInt(rs.getString("record_count")) > 0) {
                    haveRecord = true;
                }
            }

            if (haveRecord) {
                JOptionPane.showMessageDialog(parent, "Sorry! " + aprtmentId + " apartment is not able to delete ", "Warning", JOptionPane.WARNING_MESSAGE);

            } else {
                String[] options = {"Yes", "No"};

                int result = JOptionPane.showOptionDialog(
                        parent,
                        "Are you sure want to delete this apartment?",
                        "Confirmation",
                        JOptionPane.INFORMATION_MESSAGE,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[1]
                );
                if (result == 0) {
                    MySQL.iud("DELETE FROM apartment WHERE apartment.id='" + aprtmentId + "'");
                    if (jTextField5.getText().equals(aprtmentId)) {
                        resetInput();
                    }
                    loadApartment();
                    JOptionPane.showMessageDialog(parent, aprtmentId + " apartment deleted success", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeToMaintain() {
        String apartment = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
        MySQL.iud("UPDATE `apartment` SET `apartment_status_id`='3' WHERE `id`='" + apartment + "'");
        loadApartment();
    }

    private void changeToVacant() {
        String apartment = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
        MySQL.iud("UPDATE `apartment` SET `apartment_status_id`='1' WHERE `id`='" + apartment + "'");
        loadApartment();

    }

    private void changeToSold() {
        String apartment = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
        MySQL.iud("UPDATE `apartment` SET `apartment_status_id`='4' WHERE `id`='" + apartment + "'");
        loadApartment();

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
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();
        jComboBox7 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        jMenuItem1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-edit-20.png"))); // NOI18N
        jMenuItem1.setText("Edit");
        jMenuItem1.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-delete-20.png"))); // NOI18N
        jMenuItem2.setText("Delete");
        jMenuItem2.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jMenuItem3.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-view-20 (1).png"))); // NOI18N
        jMenuItem3.setText("Info");
        jMenuItem3.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem3);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-status-20.png"))); // NOI18N
        jMenu1.setText("Change Status");
        jMenu1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenu1.setPreferredSize(new java.awt.Dimension(200, 40));

        jMenuItem4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-process-20.png"))); // NOI18N
        jMenuItem4.setText("Maintain");
        jMenuItem4.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-sold-20.png"))); // NOI18N
        jMenuItem5.setText("Sold");
        jMenuItem5.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem6.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-ok-20.png"))); // NOI18N
        jMenuItem6.setText("Vacant");
        jMenuItem6.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jPopupMenu1.add(jMenu1);

        jMenuItem7.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-key-20.png"))); // NOI18N
        jMenuItem7.setText("Access Card Key");
        jMenuItem7.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem7);

        setPreferredSize(new java.awt.Dimension(1380, 940));

        jPanel5.setPreferredSize(new java.awt.Dimension(1380, 940));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel4.setText("Apartment Info");

        jComboBox2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Type", "Item 2", "Item 3", "Item 4" }));
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
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Apartment ID", "Floor", "Type", "Room", "Bathroom", "Quality", "Condition", "Description", "Status"
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

        jComboBox3.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Condition", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jComboBox4.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Status", "Item 2", "Item 3", "Item 4" }));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1341, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jPanel1.setPreferredSize(new java.awt.Dimension(500, 920));

        jLabel1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel1.setText("Foor");

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(0, 0, 0));
        jTextField1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField1.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        jTextField1.setMinimumSize(new java.awt.Dimension(64, 20));
        jTextField1.setPreferredSize(new java.awt.Dimension(360, 45));
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel2.setText("Rent Rate (Rs)");
        jLabel2.setPreferredSize(new java.awt.Dimension(37, 20));

        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(0, 0, 0));
        jTextField2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField2.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        jTextField2.setPreferredSize(new java.awt.Dimension(360, 45));
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel3.setText("No of Room");
        jLabel3.setMaximumSize(new java.awt.Dimension(37, 20));

        jTextField4.setBackground(new java.awt.Color(255, 255, 255));
        jTextField4.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField4.setForeground(new java.awt.Color(0, 0, 0));
        jTextField4.setToolTipText("Monthly rent rate");
        jTextField4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField4.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        jTextField4.setMinimumSize(new java.awt.Dimension(64, 20));
        jTextField4.setPreferredSize(new java.awt.Dimension(360, 45));
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel6.setText("Description");
        jLabel6.setMaximumSize(new java.awt.Dimension(60, 20));

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel7.setText("Apartment ID");
        jLabel7.setMaximumSize(new java.awt.Dimension(37, 20));

        jTextField5.setBackground(new java.awt.Color(255, 255, 255));
        jTextField5.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField5.setForeground(new java.awt.Color(0, 0, 0));
        jTextField5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField5.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        jTextField5.setPreferredSize(new java.awt.Dimension(360, 45));
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel8.setText("No of Barthroom");
        jLabel8.setMaximumSize(new java.awt.Dimension(37, 20));

        jTextField6.setBackground(new java.awt.Color(255, 255, 255));
        jTextField6.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField6.setForeground(new java.awt.Color(0, 0, 0));
        jTextField6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField6.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        jTextField6.setMinimumSize(new java.awt.Dimension(64, 20));
        jTextField6.setPreferredSize(new java.awt.Dimension(360, 45));
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField6KeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel9.setText("Security payment (Rs)");
        jLabel9.setPreferredSize(new java.awt.Dimension(37, 20));

        jTextField7.setBackground(new java.awt.Color(255, 255, 255));
        jTextField7.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField7.setForeground(new java.awt.Color(0, 0, 0));
        jTextField7.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField7.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        jTextField7.setPreferredSize(new java.awt.Dimension(360, 45));
        jTextField7.setSelectionColor(new java.awt.Color(153, 0, 0));
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField7KeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel10.setText("Facilities");
        jLabel10.setMaximumSize(new java.awt.Dimension(37, 20));

        jLabel11.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel11.setText("Quality");
        jLabel11.setMaximumSize(new java.awt.Dimension(37, 20));

        jLabel12.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel12.setText("Condition");
        jLabel12.setMaximumSize(new java.awt.Dimension(43, 20));

        jComboBox5.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox5.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox5.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jComboBox5.setLightWeightPopupEnabled(false);
        jComboBox5.setMinimumSize(new java.awt.Dimension(72, 20));
        jComboBox5.setPreferredSize(new java.awt.Dimension(360, 45));
        jComboBox5.setVerifyInputWhenFocusTarget(false);

        jComboBox6.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox6.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox6.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jComboBox6.setFocusable(false);
        jComboBox6.setLightWeightPopupEnabled(false);
        jComboBox6.setPreferredSize(new java.awt.Dimension(360, 45));
        jComboBox6.setVerifyInputWhenFocusTarget(false);

        jComboBox7.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox7.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox7.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox7.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jComboBox7.setLightWeightPopupEnabled(false);
        jComboBox7.setMinimumSize(new java.awt.Dimension(72, 20));
        jComboBox7.setPreferredSize(new java.awt.Dimension(360, 45));
        jComboBox7.setVerifyInputWhenFocusTarget(false);

        jButton2.setBackground(new java.awt.Color(102, 0, 0));
        jButton2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Add");
        jButton2.setPreferredSize(new java.awt.Dimension(250, 45));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField8.setBackground(new java.awt.Color(255, 255, 255));
        jTextField8.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField8.setForeground(new java.awt.Color(0, 0, 0));
        jTextField8.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField8.setPreferredSize(new java.awt.Dimension(250, 45));

        jButton1.setBackground(new java.awt.Color(102, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-arrow-48.png"))); // NOI18N
        jButton1.setContentAreaFilled(false);
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(102, 0, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-plus-38.png"))); // NOI18N
        jButton3.setContentAreaFilled(false);
        jButton3.setOpaque(true);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(102, 0, 0));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-plus-38.png"))); // NOI18N
        jButton4.setContentAreaFilled(false);
        jButton4.setOpaque(true);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(102, 0, 0));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-plus-38.png"))); // NOI18N
        jButton5.setContentAreaFilled(false);
        jButton5.setOpaque(true);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(102, 0, 0));
        jButton7.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Reset");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(1253, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(136, 136, 136)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 779, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(110, 110, 110)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addGap(27, 27, 27))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jComboBox5, javax.swing.GroupLayout.Alignment.LEADING, 0, 292, Short.MAX_VALUE)
                                            .addComponent(jComboBox6, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                                            .addComponent(jComboBox7, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(33, 33, 33))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1380, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        viewApartmentType();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        viewCondition();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        viewQuality();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ApartmentDescription box = new ApartmentDescription(parent, true, jTextField8);
        box.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jButton2.getText().equals("Add")) {
            addApartment();
        } else {
            updateApartment();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        String regex = "[0-9]";
        char value = evt.getKeyChar();

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(String.valueOf(value));
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped
        String regex = "[0-9]";
        char value = evt.getKeyChar();

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(String.valueOf(value));
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField6KeyTyped

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        String regex = "[0-9]";
        char value = evt.getKeyChar();

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(String.valueOf(value));
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped

        String regex = "[0-9]";
        char value = evt.getKeyChar();

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(String.valueOf(value));
        String input = jTextField1.getText() + value;
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        String regex = "^[1-9][0-9]*[.]?([0-9]{1,2})?";
        char value = evt.getKeyChar();
        String input = jTextField4.getText() + value;
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyTyped
        String regex = "^[1-9][0-9]*[.]?([0-9]{1,2})?";
        char value = evt.getKeyChar();
        String input = jTextField7.getText() + value;
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField7KeyTyped

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        String input = jTextField1.getText();

        if (!input.isEmpty()) {
            ResultSet rs;
            try {

                String query = "SELECT MAX(id) AS `id` FROM apartment WHERE apartment.`floor`='" + input + "'";

                rs = MySQL.search(query);
                rs.next();
                String id = rs.getString("id");

                if (id != null) {

                    int newid = Integer.parseInt(id);
                    newid++;
                    jTextField5.setText(String.valueOf(newid));
                } else {
                    int newid = Integer.parseInt(input);
                    newid = newid * 100 + 1;
                    jTextField5.setText(String.valueOf(newid));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            jTextField5.setText("");
        }
    }//GEN-LAST:event_jTextField1KeyReleased


    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadApartment();

    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.getSelectedRow() != -1 && evt.getButton() == 3) {
            String status = jTable1.getValueAt(jTable1.getSelectedRow(), 8).toString();
            if (status.equals("Vacant")) {
                jMenu1.setVisible(true);
                jMenuItem4.setVisible(true);
                jMenuItem5.setVisible(true);
                jMenuItem6.setVisible(false);
            } else if (status.equals("Sold")) {
                jMenu1.setVisible(true);
                jMenuItem4.setVisible(false);
                jMenuItem5.setVisible(false);
                jMenuItem6.setVisible(true);
            } else if (status.equals("Maintain")) {
                jMenu1.setVisible(true);
                jMenuItem4.setVisible(false);
                jMenuItem5.setVisible(false);
                jMenuItem6.setVisible(true);
            } else if (status.equals("Occupied")) {
                jMenuItem4.setVisible(false);
                jMenuItem5.setVisible(false);
                jMenuItem6.setVisible(false);
                jMenu1.setVisible(false);
            }
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        filInput();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        resetInput();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        deleteApartment();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadApartment();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        loadApartment();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        loadApartment();
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            String aprtmentId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

            ApartmentInfo api = new ApartmentInfo(parent, true, this, aprtmentId);
            api.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        changeToMaintain();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        changeToSold();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        changeToVacant();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        if (jTable1.getSelectedRow() != -1) {

            String aprtmentId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
            AccessKey ak = new AccessKey(parent, true, aprtmentId);
            ak.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        String regex = "^[1-9][0-9]*[.]?([0-9]{1,2})?";

        String input = jTextField4.getText();
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            jTextField4.setText("");
        }
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        String regex = "^[1-9][0-9]*[.]?([0-9]{1,2})?";

        String input = jTextField4.getText();
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            jTextField4.setText("");
        }
    }//GEN-LAST:event_jTextField7KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
