/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui.frame;

import com.formdev.flatlaf.IntelliJTheme;
import gui.component.Header;
import gui.panel.CashLateFee;
import gui.panel.CashLeaseBill;
import gui.panel.CashOtherBill;
import gui.panel.CashSecurityBill;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import javax.swing.JFrame;

import model.CustomButtonUI;
import model.CustomeMouseAdapter;

import model.StaticComponent;

/**
 *
 * @author Pramudi Hirunika
 */
public class Cashier extends javax.swing.JFrame implements model.FrameAdditional {

    CashLeaseBill lesebill;
    CashLateFee leatfee;
    CashOtherBill otherbill;
    CashSecurityBill securityBill;
    String un = "Username";
    String role = "Cashier";

    public Cashier() {
        initComponents();
        setLogo();
        loadHeader();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        initailSetup();
        startLisners();
        viewLeaseBill();
        jButton4.setVisible(false);

    }

    public Cashier(String un) {
        initComponents();
        this.un = un;
        setLogo();
        loadHeader();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        initailSetup();
        startLisners();
        viewLeaseBill();
        jButton4.setVisible(false);

    }

    private void loadHeader() {
        Header h = new Header(this.un, this.role);
        h.setSize(jLayeredPane2.getSize());
        jLayeredPane2.removeAll();
        jLayeredPane2.add(h);
        jLayeredPane2.revalidate();
        jLayeredPane2.repaint();
    }

    private void setLogo() {

        this.setIconImage(StaticComponent.titlebarIcon.getImage());
    }

    private void initailSetup() {
        jPanel1.setBackground(new Color(255, 255, 255, 120));
        jPanel2.setBackground(new Color(119, 13, 16, 170));

        jButton1.setUI(new CustomButtonUI(20));
        jButton5.setUI(new CustomButtonUI(20));
        jButton6.setUI(new CustomButtonUI(20));

        jButton4.setUI(new CustomButtonUI(20));
        jButton2.setUI(new CustomButtonUI(20));

    }

    private void startLisners() {

        // Add a MouseListener to the button
        jButton1.addMouseListener(new CustomeMouseAdapter(jButton1));
        jButton2.addMouseListener(new CustomeMouseAdapter(jButton2));

        jButton4.addMouseListener(new CustomeMouseAdapter(jButton4));
        jButton5.addMouseListener(new CustomeMouseAdapter(jButton5));
        jButton6.addMouseListener(new CustomeMouseAdapter(jButton6));

    }

    @Override
    public void setActive(JButton btn) {

        if (btn.equals(jButton1)) {
            this.setInactive(jButton4);
            this.setInactive(jButton5);
            this.setInactive(jButton6);
        } else if (btn.equals(jButton4)) {
            this.setInactive(jButton1);
            this.setInactive(jButton5);
            this.setInactive(jButton6);
        } else if (btn.equals(jButton5)) {
            this.setInactive(jButton1);
            this.setInactive(jButton4);
            this.setInactive(jButton6);

        } else if (btn.equals(jButton6)) {
            this.setInactive(jButton1);
            this.setInactive(jButton4);
            this.setInactive(jButton5);

        }
        MouseListener[] listeners = btn.getMouseListeners();
        for (MouseListener listener : listeners) {
            if (listener instanceof CustomeMouseAdapter) {

                ((CustomeMouseAdapter) listener).setState(false);

            }
        }
        btn.setBackground(new Color(166, 0, 0));
    }

    @Override
    public void setInactive(JButton btn) {
        MouseListener[] listeners = btn.getMouseListeners();
        for (MouseListener listener : listeners) {
            if (listener instanceof CustomeMouseAdapter) {

                ((CustomeMouseAdapter) listener).setState(true);
                btn.setBackground(new Color(102, 0, 0));
            }
        }
    }

    private void viewLeaseBill() {
        lesebill = new CashLeaseBill(this);
        lesebill.setSize(jLayeredPane1.getSize());

        jLayeredPane1.removeAll();

        jLayeredPane1.add(lesebill);
        jLayeredPane1.revalidate();
        jLayeredPane1.repaint();

        this.setActive(jButton1);
    }

    private void viewOtherBill() {
        otherbill = new CashOtherBill(this);
        otherbill.setSize(jLayeredPane1.getSize());

        jLayeredPane1.removeAll();

        jLayeredPane1.add(otherbill);
        jLayeredPane1.revalidate();
        jLayeredPane1.repaint();

        this.setActive(jButton6);
    }

    private void viewLateFee() {
        leatfee = new CashLateFee();
        leatfee.setSize(jLayeredPane1.getSize());

        jLayeredPane1.removeAll();

        jLayeredPane1.add(leatfee);
        jLayeredPane1.revalidate();
        jLayeredPane1.repaint();

        this.setActive(jButton4);
    }

    private void viewSecurityBill() {
        securityBill = new CashSecurityBill(this);
        securityBill.setSize(jLayeredPane1.getSize());

        jLayeredPane1.removeAll();

        jLayeredPane1.add(securityBill);
        jLayeredPane1.revalidate();
        jLayeredPane1.repaint();

        this.setActive(jButton5);
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
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cashier");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setLayout(new java.awt.GridLayout());

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1920, Short.MAX_VALUE)
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );

        jPanel1.add(jLayeredPane2);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 80));

        jPanel2.setBackground(new java.awt.Color(102, 0, 0));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(new java.awt.Color(102, 0, 0));
        jButton1.setFont(new java.awt.Font("Gadugi", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-rent-48.png"))); // NOI18N
        jButton1.setText("Rent Payment");
        jButton1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(150, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 40, 1, 1)));
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.setPreferredSize(new java.awt.Dimension(176, 60));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(102, 0, 0));
        jButton2.setFont(new java.awt.Font("Gadugi", 1, 24)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-logout-48.png"))); // NOI18N
        jButton2.setText("Logout");
        jButton2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(150, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1)));
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(102, 0, 0));
        jButton4.setFont(new java.awt.Font("Gadugi", 1, 24)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-time-to-pay-48.png"))); // NOI18N
        jButton4.setText("Late Fee");
        jButton4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(150, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 40, 1, 1)));
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(102, 0, 0));
        jButton5.setFont(new java.awt.Font("Gadugi", 1, 24)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-card-security-48.png"))); // NOI18N
        jButton5.setText("Security Payment");
        jButton5.setToolTipText("");
        jButton5.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(150, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 40, 1, 1)));
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(102, 0, 0));
        jButton6.setFont(new java.awt.Font("Gadugi", 1, 24)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-bill-48.png"))); // NOI18N
        jButton6.setText("Other Bill");
        jButton6.setToolTipText("");
        jButton6.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(150, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(1, 40, 1, 1)));
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logo/fram-logo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 250, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 490, 1000));

        jLayeredPane1.setPreferredSize(new java.awt.Dimension(1380, 940));

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1380, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 940, Short.MAX_VALUE)
        );

        getContentPane().add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, 1380, 940));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/bac.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 1080));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        viewSecurityBill();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        viewLeaseBill();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Login frame = new Login();
        frame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        viewLateFee();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        viewOtherBill();
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Windows".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Cashier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Cashier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Cashier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Cashier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
        InputStream is = Cashier.class.getResourceAsStream("/theam/arc-theme-orange.theme.json");
        IntelliJTheme.setup(is);

//        Log4jInitialization.initializeLog4j();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cashier().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

}
