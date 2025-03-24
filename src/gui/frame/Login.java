/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.frame;

import com.formdev.flatlaf.IntelliJTheme;
import email.EmailSender;
import java.awt.Color;
import java.io.InputStream;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.CustomButtonUI;
import model.CustomeMouseAdapter;
import model.LoadingIndicator;
import model.MySQL;
import model.StaticComponent;

/**
 *
 * @author Pramudi Hirunika
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    public Login() {

        initComponents();
        setLogo();
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        initailSetup();
        startLisners();
        jPanel2.setBackground(new Color(255, 255, 255));
        this.setBackground(new Color(0, 0, 0));
    }

    private void setLogo() {

        this.setIconImage(StaticComponent.titlebarIcon.getImage());
    }

    private void initailSetup() {
        jPanel2.setBackground(new Color(255, 255, 255, 0));
        jButton1.setUI(new CustomButtonUI(0));
//        this.setUndecorated(true);

    }

    private void startLisners() {

        // Add a MouseListener to the button
        jButton1.addMouseListener(new CustomeMouseAdapter(jButton1));
        jButton2.addMouseListener(new CustomeMouseAdapter(jButton2, new Color(0, 120, 255), new Color(0, 0, 204), true));

    }

    private void login() {
        String un = jTextField1.getText();
        char[] pw = jPasswordField1.getPassword();
        String password = "";
        int i = 0;
        for (char c : pw) {
            password = password + pw[i];
            i++;
        }
        if (un.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Username", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Password", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            try {
                ResultSet rs = MySQL.search("SELECT * FROM user "
                        + "INNER JOIN user_role ON user.user_role_id=user_role.id"
                        + " WHERE `username`='" + un + "' AND `password`='" + password + "' ");
                if (rs.next()) {
                    int status = rs.getInt("user_status_id");
                    if (status == 1) {
                        switch (rs.getString("user_role.name")) {
                            case "Admin":
                                JFrame frame = new Admin(un);
                                frame.setVisible(true);
                                this.dispose();
                                break;
                            case "Receptionist":
                                JFrame resipt = new Receptionist(un);
                                resipt.setVisible(true);
                                this.dispose();
                                break;
                            case "Property Manager":
                                JFrame pm = new PropertyManager(un);
                                pm.setVisible(true);
                                this.dispose();
                                break;
                            case "Lease Agent":
                                JFrame lm = new LeaseManagement(un);
                                lm.setVisible(true);
                                this.dispose();
                                break;
                            case "CS Representative":
                                JFrame csr = new CsRepresentative(un);
                                csr.setVisible(true);
                                this.dispose();
                                break;
                            case "Cashier":
                                JFrame ch = new Cashier(un);
                                ch.setVisible(true);
                                this.dispose();
                                break;
                            default:
                                System.out.println("Error");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Your account is blocked! Please contact administrator", "Warning", JOptionPane.WARNING_MESSAGE);

                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect Login details", "Warning", JOptionPane.WARNING_MESSAGE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void forgotPassword() {
        String un = jTextField1.getText();
        if (un.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Username", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {
            try {
                ResultSet rs = MySQL.search("SELECT * FROM user "
                        + "INNER JOIN user_role ON user.user_role_id=user_role.id"
                        + " WHERE `username`='" + un + "' ");
                if (rs.next()) {
                    String email = rs.getString("email");
                    String pw = rs.getString("password");

                    sendEmail(email, pw);

                } else {
                    JOptionPane.showMessageDialog(this, "Not registered username", "Warning", JOptionPane.WARNING_MESSAGE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendEmail(String email, String pw) {
        String sub = "Password retrieve";
        String body = "Your password is " + pw;

        EmailSender es = new EmailSender(email, sub, body);
        String status = es.sendMail();
        if (status.equals("failed")) {
            String[] options = {"Close", "Retry"};

            int result = JOptionPane.showOptionDialog(
                    this,
                    "Email sending failed. Please check your internet connection!.\nClick retry button if connection is Fine",
                    "Connection lost",
                    JOptionPane.ERROR_MESSAGE,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1]
            );

            if (result == 0 || result == -1) {
                JOptionPane.showMessageDialog(this, "Email Sending is Canceled", "Info", JOptionPane.INFORMATION_MESSAGE);

            } else if (result == 1) {
                String status2 = es.sendMail();
                if (status2.equals("failed")) {
                    JOptionPane.showMessageDialog(this, "Email Sending Failed", "Error", JOptionPane.WARNING_MESSAGE);
                } else if (status2.equals("Success")) {
                    JOptionPane.showMessageDialog(this, "Email Sent Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
                } else if (status2.equals("timeout")) {
                    JOptionPane.showMessageDialog(this, "Time out! Check your connection", "Error", JOptionPane.WARNING_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(this, "Something Wrong! Try again later", "Error", JOptionPane.WARNING_MESSAGE);

                }
            }
        } else if (status.equals("Success")) {
            JOptionPane.showMessageDialog(this, "Email Sent Successfully", "Success", JOptionPane.INFORMATION_MESSAGE, StaticComponent.successIcon);
        } else {
            JOptionPane.showMessageDialog(this, "Time out! Check your connection", "Error", JOptionPane.WARNING_MESSAGE);

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
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1930, 1040));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setForeground(new java.awt.Color(0, 0, 204));
        jPanel2.setToolTipText("");
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel2.setText("Username");

        jTextField1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jTextField1.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1)));
        jTextField1.setPreferredSize(new java.awt.Dimension(389, 45));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel3.setText("Password");

        jButton1.setBackground(new java.awt.Color(102, 0, 0));
        jButton1.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Login");
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Source Sans Pro SemiBold", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 51, 204));
        jButton2.setText("Forgot Password?");
        jButton2.setBorder(null);
        jButton2.setContentAreaFilled(false);
        jButton2.setFocusPainted(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Lucida Bright", 1, 36)); // NOI18N
        jLabel4.setText("Luxurioz");

        jPasswordField1.setFont(new java.awt.Font("Nirmala UI", 0, 18)); // NOI18N
        jPasswordField1.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1)));
        jPasswordField1.setPreferredSize(new java.awt.Dimension(389, 45));
        jPasswordField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPasswordField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(176, 176, 176)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(60, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(177, 177, 177))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel1, new java.awt.GridBagConstraints());

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1930, 1080));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/bac.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//        Admin frame = new Admin();
//        frame.setVisible(true);
//        this.dispose();
        login();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        LoadingIndicator.setWaitCursor(this);
        forgotPassword();
        LoadingIndicator.resetCursor(this);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jPasswordField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField1KeyReleased
        LoadingIndicator.setWaitCursor(this);
        if (evt.getKeyCode() == 10) {

            login();

        }
        LoadingIndicator.resetCursor(this);
    }//GEN-LAST:event_jPasswordField1KeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        InputStream is = Login.class.getResourceAsStream("/theam/arc-theme-orange.theme.json");
        IntelliJTheme.setup(is);
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
