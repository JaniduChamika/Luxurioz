/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.panel;

import gui.dialogbox.ItemCategory;
import gui.dialogbox.TransferItem;
import java.awt.Color;
import java.sql.ResultSet;
import java.text.DateFormat;
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
import model.MySQL;
import model.StaticComponent;

/**
 *
 * @author Hiranya
 */
public class PropItem extends javax.swing.JPanel {
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("#.00");

    /**
     * Creates new form ItemInfo
     */
    private JFrame parent;
    
    public PropItem(JFrame parent) {
        initComponents();
        this.parent = parent;
        this.setBackground(new Color(255, 255, 255, 0));
        jPanel5.setBackground(new Color(255, 255, 255, 190));
        jPanel1.setBackground(new Color(255, 255, 255, 190));
        CustomDateChooser.customizeOnlyCalander(jDateChooser1);
        CustomDateChooser.customizeDateChooser(jDateChooser2);
        
        startLisners();
        setNewButtonUI();
        CustomTable custable = new CustomTable();
        custable.modifyLayout(jTable1);
        loadCategory();
        loadItem();
    }
    
    private void startLisners() {

        // Add a MouseListener to the button
        jButton1.addMouseListener(new CustomeMouseAdapter(jButton1));
        jButton3.addMouseListener(new CustomeMouseAdapter(jButton3));
        
    }
    
    private void setNewButtonUI() {
        jButton1.setUI(new CustomButtonUI(0));
        jButton3.setUI(new CustomButtonUI(0));
        
    }
    Map<String, String> itemCategory = new HashMap<>();
    
    public void loadCategory() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `item_category`");
            Vector v = new Vector();
            v.add("Select Category");
            while (rs.next()) {
                String abc = rs.getString("name");
                v.add(abc);
                itemCategory.put(rs.getString("name"), rs.getString("id"));
                
            }
            DefaultComboBoxModel dcm = new DefaultComboBoxModel(v);
            DefaultComboBoxModel dcm1 = new DefaultComboBoxModel(v);
            jComboBox1.setModel(dcm);
            jComboBox2.setModel(dcm1);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadItem() {
        
        Date date = jDateChooser1.getDate();
        String text = jTextField3.getText();
        String category = jComboBox2.getSelectedItem().toString();
        
        try {
            
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs;
            String query = "SELECT * FROM item "
                    + " INNER JOIN apartment ON item.apartment_id=apartment.id\n"
                    + "INNER JOIN item_category ON item.item_category_id=item_category.id\n"
                    + " WHERE (apartment.id LIKE '" + text + "%' OR reference_no LIKE '" + text + "%' OR model LIKE '" + text + "%' OR uniqueid LIKE '" + text + "%' OR purchase_address LIKE '" + text + "%') ";
            
            if (date != null) {
                String pdate = sdf.format(date);
                query = query + "AND purchase_date='" + pdate + "'";
            }
            if (!category.equals("Select Category")) {
                query = query + "AND item_category.name='" + category + "'";
            }
//            System.out.println(query);
            rs = MySQL.search(query);
            while (rs.next()) {
                Vector v = new Vector();
                
                v.add(rs.getString("uniqueid"));
                v.add(rs.getString("item_category.name"));
                v.add(rs.getString("item.model"));
                v.add(rs.getString("item.purchase_date"));
                v.add(df.format(Double.parseDouble(rs.getString("item.price"))));
                v.add(rs.getString("item.purchase_address"));
                v.add(rs.getString("item.reference_no"));
                v.add(rs.getString("item.apartment_id"));
                
                dtm.addRow(v);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void addItem() {
        String apartmentId = jTextField1.getText();
        String category = jComboBox1.getSelectedItem().toString();
        String model = jTextField5.getText();
        String price = jTextField4.getText();
        Date buydate = jDateChooser2.getDate();
        String purchaseAddress = jTextField6.getText();
        
        String reference = jTextField2.getText();
        String itemId = jTextField7.getText();
        
        String categoryId = itemCategory.get(category);
        
        if (apartmentId.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter apartment id", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (jTextField1.getForeground().equals(Color.RED)) {
            JOptionPane.showMessageDialog(parent, apartmentId + " apartment doesn't exist", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (categoryId == null) {
            JOptionPane.showMessageDialog(parent, "Please select category", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (model.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter model name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (price.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (buydate == null) {
            JOptionPane.showMessageDialog(parent, "Please enter purchase date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (purchaseAddress.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter purchase address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (reference.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter reference no (invoice/bill no)", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (itemId.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter item id", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                
                MySQL.iud("INSERT INTO `item` (`uniqueid`,`model`,`purchase_date`,`price`,`purchase_address`,`reference_no`,`apartment_id`,`item_category_id`) "
                        + "VALUES ('" + itemId + "','" + model + "','" + sdf.format(buydate) + "','" + price + "','" + purchaseAddress + "','" + reference + "','" + apartmentId + "','" + categoryId + "')");
                resetInput();
                loadItem();
                
                JOptionPane.showMessageDialog(parent, "Item registerd success", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void resetInput() {
        jTextField1.setForeground(Color.BLACK);
        jButton1.setText("Add");
        
        jTextField1.setText("");
        jComboBox1.setSelectedIndex(0);
        jTextField5.setText("");
        jTextField4.setText("");
        jDateChooser2.setDate(null);
        jTextField6.setText("");
        
        jTextField2.setText("");
        jTextField7.setText("");
        jTextField1.setEnabled(true);
        jComboBox1.setEnabled(true);
        jTextField7.setEnabled(true);
    }
    
    private void viewItemCategory() {
        ItemCategory ic = new ItemCategory(parent, true, this);
        ic.setVisible(true);
    }
    
    private void searchApartment() {
        String apartmentId = jTextField1.getText();
        ResultSet rs;
        String query = "SELECT *\n"
                + "FROM apartment\n"
                + "WHERE apartment.id = '" + apartmentId + "'";
        try {
            rs = MySQL.search(query);
            if (rs.next()) {
                jTextField1.setForeground(new Color(0, 109, 12));
            } else {
                jTextField1.setForeground(Color.RED);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        
    }
    
    private String genrateCategoryID() {
        String category = jComboBox1.getSelectedItem().toString();
        String[] part = category.split(" ");
        String categoryId = "";
        if (part.length >= 2) {
            for (int i = 0; i < part.length && i < 3; i++) {
                String[] subpart = part[i].split("");
                for (int j = 0; j < subpart.length; j++) {
                    if (j == 0) {
                        categoryId = categoryId + subpart[0];
                    }
                }
            }
        } else {
            String[] subpart = category.split("");
            for (int j = 0; j < subpart.length && j < 3; j++) {
                categoryId = categoryId + subpart[j];
            }
        }
        
        return categoryId.toUpperCase();
    }
    
    private void generateItemId() {
        String category = jComboBox1.getSelectedItem().toString();
        String apartmentId = jTextField1.getText();
        if (category.equals("Select Category")) {
            
            jTextField7.setText(apartmentId);
        } else {
            String itemNo = "001";
            if (!jTextField1.getForeground().equals(Color.RED)) {
                try {
                    ResultSet rs;
                    String query = "SELECT COUNT(item.id) AS item_count\n"
                            + "FROM item\n"
                            + "INNER JOIN item_category ON item.item_category_id=item_category.id\n"
                            + "WHERE item.apartment_id='" + apartmentId + "' AND item_category.name='" + category + "' ";
                    rs = MySQL.search(query);
                    if (rs.next()) {
                        String itemCount = rs.getString("item_count");
                        itemNo = String.valueOf(String.format("%03d", (Integer.parseInt(itemCount) + 1)));
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String categoryId = genrateCategoryID();
            jTextField7.setText(apartmentId + "/" + categoryId + "/" + itemNo);
        }
    }
    
    private void tranferItem() {
        
    }
    
    private void fillInput() {
        String itemId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
        jTextField1.setEnabled(false);
        jComboBox1.setEnabled(false);
        jTextField7.setEnabled(false);
        jButton1.setText("Update");
        try {
            
            ResultSet rs;
            String query = "SELECT * FROM item "
                    + " INNER JOIN apartment ON item.apartment_id=apartment.id\n"
                    + "INNER JOIN item_category ON item.item_category_id=item_category.id\n"
                    + " WHERE  uniqueid = '" + itemId + "'";
            
            rs = MySQL.search(query);
            if (rs.next()) {
                
                jTextField1.setText(rs.getString("item.apartment_id"));
                jComboBox1.setSelectedItem(rs.getString("item_category.name"));
                jTextField5.setText(rs.getString("item.model"));
                jTextField4.setText(df.format(Double.parseDouble(rs.getString("item.price"))));
                jDateChooser2.setDate(sdf.parse(rs.getString("item.purchase_date")));
                jTextField6.setText(rs.getString("item.purchase_address"));
                
                jTextField2.setText(rs.getString("item.reference_no"));
                jTextField7.setText(rs.getString("uniqueid"));
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void editItem() {
        
        String model = jTextField5.getText();
        String price = jTextField4.getText();
        Date buydate = jDateChooser2.getDate();
        String purchaseAddress = jTextField6.getText();
        
        String reference = jTextField2.getText();
        String itemId = jTextField7.getText();
        
        if (model.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter model name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (price.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter price", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (buydate == null) {
            JOptionPane.showMessageDialog(parent, "Please enter purchase date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (purchaseAddress.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter purchase address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (reference.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter reference no (invoice/bill no)", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                
                MySQL.iud("UPDATE `item` SET `model`='" + model + "',`purchase_date`='" + sdf.format(buydate) + "',`price`='" + price + "',`purchase_address`='" + purchaseAddress + "',`reference_no`='" + reference + "' "
                        + "WHERE `uniqueid`='" + itemId + "'");
                resetInput();
                loadItem();
                
                JOptionPane.showMessageDialog(parent, "Item updated success", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
                
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        jPopupMenu1.setPreferredSize(new java.awt.Dimension(200, 80));

        jMenuItem1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-transfer-20.png"))); // NOI18N
        jMenuItem1.setText("Transfer");
        jMenuItem1.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-edit-20.png"))); // NOI18N
        jMenuItem2.setText("Edit");
        jMenuItem2.setPreferredSize(new java.awt.Dimension(200, 40));
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setPreferredSize(new java.awt.Dimension(1380, 940));

        jPanel5.setPreferredSize(new java.awt.Dimension(1380, 940));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel4.setText("Item Info");

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

            },
            new String [] {
                "Item ID", "Category", "Model", "Purchase Date", "Price", "Address", "Reference No", "Apartment ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, false, false, false
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

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel11.setText("Purchase date");

        jComboBox2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Type", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-clear-24.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(50, 50, 50)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1342, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel1.setToolTipText("");
        jPanel1.setMinimumSize(new java.awt.Dimension(1380, 360));

        jTextField5.setBackground(new java.awt.Color(255, 255, 255));
        jTextField5.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField5.setPreferredSize(new java.awt.Dimension(400, 45));

        jLabel1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel1.setText("Apertment ID");
        jLabel1.setMinimumSize(new java.awt.Dimension(37, 20));

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
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

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel6.setText("Purchase Address");
        jLabel6.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel2.setText("Price");
        jLabel2.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel3.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel3.setText("Referance No");
        jLabel3.setMinimumSize(new java.awt.Dimension(37, 20));

        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField2.setPreferredSize(new java.awt.Dimension(400, 45));

        jTextField4.setBackground(new java.awt.Color(255, 255, 255));
        jTextField4.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField4.setPreferredSize(new java.awt.Dimension(400, 45));
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel7.setText("Model");
        jLabel7.setMinimumSize(new java.awt.Dimension(37, 20));

        jLabel8.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel8.setText("Item ID");
        jLabel8.setMinimumSize(new java.awt.Dimension(37, 20));

        jTextField6.setBackground(new java.awt.Color(255, 255, 255));
        jTextField6.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField6.setPreferredSize(new java.awt.Dimension(400, 45));

        jTextField7.setBackground(new java.awt.Color(255, 255, 255));
        jTextField7.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField7.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jTextField7.setEnabled(false);
        jTextField7.setPreferredSize(new java.awt.Dimension(400, 45));

        jLabel9.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel9.setText("Purchase Data");
        jLabel9.setMinimumSize(new java.awt.Dimension(37, 20));

        jButton1.setBackground(new java.awt.Color(102, 0, 0));
        jButton1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0), 2));
        jButton1.setContentAreaFilled(false);
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jDateChooser2.setBackground(new java.awt.Color(255, 255, 255));
        jDateChooser2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0)));
        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jDateChooser2.setPreferredSize(new java.awt.Dimension(99, 45));

        jComboBox1.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1)));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel10.setText("Item Category");
        jLabel10.setMinimumSize(new java.awt.Dimension(37, 20));

        jButton3.setBackground(new java.awt.Color(102, 0, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-plus-38.png"))); // NOI18N
        jButton3.setContentAreaFilled(false);
        jButton3.setOpaque(true);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(66, 66, 66)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox1)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jButton1.getText().equals("Add")) {
            addItem();
        } else {
            editItem();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        viewItemCategory();
    }//GEN-LAST:event_jButton3ActionPerformed

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

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        String regex = "[0-9]";
        char value = evt.getKeyChar();
        
        Pattern pattern = Pattern.compile(regex);
        
        Matcher matcher = pattern.matcher(String.valueOf(value));
        if (!matcher.matches()) {
            evt.consume();
        }
        

    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        searchApartment();
        generateItemId();

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        
        generateItemId();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        fillInput();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.getSelectedRow() != -1 && evt.getButton() == 3) {
            jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            String itemId = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
            TransferItem ti = new TransferItem(parent, true, this);
            ti.jLabel4.setText(itemId);
            ti.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadItem();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        if (jDateChooser1.getDate() != null) {
            loadItem();
        }

    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jDateChooser1.setDate(null);
        loadItem();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        loadItem();
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        String regex = "^[1-9][0-9]*[.]?([0-9]{1,2})?";
        
        String input = jTextField4.getText();
        Pattern pattern = Pattern.compile(regex);
        
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            jTextField4.setText("");
        }
    }//GEN-LAST:event_jTextField4KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
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
    // End of variables declaration//GEN-END:variables
}
