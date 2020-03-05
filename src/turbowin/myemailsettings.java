package turbowin;


import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
//import javax.xml.bind.DatatypeConverter;                           // needed for Java 6 but doesn't exist in Java 12

import java.util.Base64;                                            // Java 8 and higher
//import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/*
 * myemailsettings.java
 *
 * Created on 12 november 2008, 9:59
 */



/**
 *
 * @author  Martin
 */
final public class myemailsettings extends javax.swing.JFrame {

   /* inner class popupListener (Gmail app password) */
   class PopupListener extends MouseAdapter 
   {
      @Override
      public void mousePressed(MouseEvent e) 
      {
         ShowPopup(e);
         //System.out.println("Popup menu will be visible!");
      }

      @Override
      public void mouseReleased(MouseEvent e) 
      {
         ShowPopup(e);
      }

      private void ShowPopup(MouseEvent e) 
      {
         if (e.isPopupTrigger()) 
         {
            popup.show(e.getComponent(), e.getX(), e.getY());
         }
      }
   }      
   
   /* inner class popupListener2 (Yahoo app password) */
   class PopupListener2 extends MouseAdapter 
   {
      @Override
      public void mousePressed(MouseEvent e) 
      {
         ShowPopup(e);
         //System.out.println("Popup menu will be visible!");
      }

      @Override
      public void mouseReleased(MouseEvent e) 
      {
         ShowPopup(e);
      }

      private void ShowPopup(MouseEvent e) 
      {
         if (e.isPopupTrigger()) 
         {
            popup2.show(e.getComponent(), e.getX(), e.getY());
         }
      }
   }   
   
   /** Creates new form myemailsettings */
   public myemailsettings() 
   {
      initComponents();
      initEmailComponents();
      setLocation(main.x_pos_main_frame, main.y_pos_main_frame);

   }

    
    
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
private void initEmailComponents()
{        
   ////////////////////// initialise /////////////////////////
   
   // [ALL]
   jTextField1.setText("");                                       // obs by email recipient
   jTextField2.setText("");                                       // obs by email subject
   jTextField4.setText("");                                       // obs by email CC
   
   // [FOTRMAT 101]
   jRadioButton5.setSelected(false);                              // format 101 obs in body
   jRadioButton6.setSelected(false);                              // format 101 obs in attachment
   
   // [SMTP HOST]                                                 // NB sometimes also called LOCAL HOST
   jTextField5.setText("");                                       // SMTP host email server
   jTextField10.setText("");                                      // SMTP host ship email address
   jTextField11.setText("");                                      // SMTP host password
   jTextField12.setText("");                                      // SMTP host port
   
   // [GMAIL]
   jTextField6.setText("");
   jTextField7.setText("");
   jRadioButton1.setSelected(false); 
   jRadioButton2.setSelected(false); 
   
   // [YAHOO]
   jTextField8.setText("");
   jTextField9.setText("");
   jRadioButton3.setSelected(false); 
   jRadioButton4.setSelected(false); 
   
   // [LOGS]
   jTextField3.setText("");                                       // logs email recipient
   
   //jCheckBox1.setSelected(false); 
   
   
   /////////////////////// pop up menu /////////////////////////
   
   // NB only for the Gmail and Yahoo app password field
   
   
   // Gmail app password
   
   // pop up menu 
   popup = new JPopupMenu();
      
   JMenuItem menuItem_a = new JMenuItem("copy");
   menuItem_a.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         jTextField7.copy();
      }
   });
   popup.add(menuItem_a);    
      
   JMenuItem menuItem_b = new JMenuItem("paste");
   menuItem_b.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         jTextField7.paste();
      }
   });
   popup.add(menuItem_b);    
      
   MouseListener popupListener = new myemailsettings.PopupListener();
   jTextField7.addMouseListener(popupListener);
      
   // tool tip text
   jTextField7.setToolTipText("right click for copy or paste");
   
   
   // Yahoo app password
   popup2 = new JPopupMenu();
   
   JMenuItem menuItem_c = new JMenuItem("copy");
   menuItem_c.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         jTextField9.copy();
      }
   });
   popup2.add(menuItem_c);    
      
   JMenuItem menuItem_d = new JMenuItem("paste");
   menuItem_d.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         jTextField9.paste();
      }
   });
   popup2.add(menuItem_d);    
      
   MouseListener popupListener2 = new myemailsettings.PopupListener2();
   jTextField9.addMouseListener(popupListener2);
   
   // tool tip text
   jTextField9.setToolTipText("right click for copy or paste");
   
   
   //////////////// put back earlier inserted values (if applicable) /////////////////
   
   // [ALL]
   jTextField1.setText(main.obs_email_recipient);                 // Obs E-mail address recipient
   jTextField4.setText(main.obs_email_cc);                        // Obs E-mail address cc
   jTextField2.setText(main.obs_email_subject);                   // Obs E-mail subject
   
   
   // [FORMAT 101]
   if (main.obs_101_email.equals(main.FORMAT_101_BODY))
   {
      jRadioButton5.setSelected(true);   
      jRadioButton6.setSelected(false); 
   }
   else if (main.obs_101_email.equals(main.FORMAT_101_ATTACHEMENT))
   {
      jRadioButton5.setSelected(false);   
      jRadioButton6.setSelected(true); 
   }
   else // default = 'attachment'
   {
      jRadioButton5.setSelected(false);                           // format 101 obs in body 
      jRadioButton6.setSelected(true);                            // format 101 obs in attachment
   }
   
   
   // [SMTP HOST]
   jTextField5.setText(main.local_email_server);
   jTextField10.setText(main.your_ship_address);
    
   if (main.smtp_host_password.equals("") == false)
   {
      jTextField11.setText(password_stars);
   }
    
   jTextField12.setText(main.smtp_host_port); 
    
   
   // [GMAIL]
   jTextField6.setText(main.your_gmail_address);
   //jTextField7.setText(main.gmail_app_password);
   if (main.gmail_app_password.equals("") == false)
   {
      jTextField7.setText(password_stars);
   }
   
   if (main.gmail_security.equals(main.GMAIL_TLS))
   {
      jRadioButton1.setSelected(true); 
      jRadioButton2.setSelected(false); 
   }
   else if (main.gmail_security.equals(main.GMAIL_SSL))
   {
      jRadioButton1.setSelected(false); 
      jRadioButton2.setSelected(true); 
   }
   else // default = TLS
   {
      jRadioButton1.setSelected(true); 
      jRadioButton2.setSelected(false);
   }   
   
   
   // [YAHOO]
   jTextField8.setText(main.your_yahoo_address);
   //jTextField9.setText(main.yahoo_app_password);
   if (main.yahoo_app_password.equals("") == false)
   {
      jTextField9.setText(password_stars);
   }
   
   if (main.yahoo_security.equals(main.YAHOO_TLS))
   {
      jRadioButton3.setSelected(true); 
      jRadioButton4.setSelected(false); 
   }
   else if (main.yahoo_security.equals(main.YAHOO_SSL))
   {
      jRadioButton3.setSelected(false); 
      jRadioButton4.setSelected(true); 
   }
   else // default = TLS
   {
      jRadioButton3.setSelected(true); 
      jRadioButton4.setSelected(false);
   }   
   
   // [LOGS]
   jTextField3.setText(main.logs_email_recipient);                 // logs (immt etc) adress recipient
   
   
   // default E-mail program on this computer is AMOS Mail
   //if (main.amos_mail == true) 
   //{
   //   jCheckBox1.setSelected(true);
   //} 

}   
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      buttonGroup1 = new javax.swing.ButtonGroup();
      buttonGroup2 = new javax.swing.ButtonGroup();
      buttonGroup3 = new javax.swing.ButtonGroup();
      jLabel3 = new javax.swing.JLabel();
      jPanel1 = new javax.swing.JPanel();
      jPanel3 = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      jTextField1 = new javax.swing.JTextField();
      jLabel8 = new javax.swing.JLabel();
      jTextField4 = new javax.swing.JTextField();
      jLabel2 = new javax.swing.JLabel();
      jTextField2 = new javax.swing.JTextField();
      jPanel4 = new javax.swing.JPanel();
      jLabel9 = new javax.swing.JLabel();
      jRadioButton5 = new javax.swing.JRadioButton();
      jRadioButton6 = new javax.swing.JRadioButton();
      jPanel5 = new javax.swing.JPanel();
      jLabel10 = new javax.swing.JLabel();
      jTextField5 = new javax.swing.JTextField();
      jLabel17 = new javax.swing.JLabel();
      jTextField10 = new javax.swing.JTextField();
      jLabel13 = new javax.swing.JLabel();
      jTextField11 = new javax.swing.JTextField();
      jLabel18 = new javax.swing.JLabel();
      jTextField12 = new javax.swing.JTextField();
      jPanel6 = new javax.swing.JPanel();
      jLabel11 = new javax.swing.JLabel();
      jTextField6 = new javax.swing.JTextField();
      jLabel14 = new javax.swing.JLabel();
      jTextField7 = new javax.swing.JTextField();
      jRadioButton1 = new javax.swing.JRadioButton();
      jRadioButton2 = new javax.swing.JRadioButton();
      jPanel7 = new javax.swing.JPanel();
      jLabel12 = new javax.swing.JLabel();
      jTextField8 = new javax.swing.JTextField();
      jLabel15 = new javax.swing.JLabel();
      jTextField9 = new javax.swing.JTextField();
      jRadioButton3 = new javax.swing.JRadioButton();
      jRadioButton4 = new javax.swing.JRadioButton();
      jLabel16 = new javax.swing.JLabel();
      jLabel4 = new javax.swing.JLabel();
      jPanel2 = new javax.swing.JPanel();
      jTextField3 = new javax.swing.JTextField();
      jLabel5 = new javax.swing.JLabel();
      jLabel6 = new javax.swing.JLabel();
      jLabel7 = new javax.swing.JLabel();
      jSeparator1 = new javax.swing.JSeparator();
      jButton1 = new javax.swing.JButton();
      jButton2 = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle("E-mail settings");
      setMinimumSize(new java.awt.Dimension(800, 600));
      setResizable(false);

      jLabel3.setText("required for: 'Output --> Obs by Email (default / SMTP host / Gmail / Yahool)' and optional for AP[&T]R and AWSR");

      jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

      jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

      jLabel1.setText("[ALL] address recipient");

      jLabel8.setText("cc*");

      jLabel2.setText("Email subject");

      javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
      jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(39, 39, 39)
            .addComponent(jLabel8)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(46, 46, 46)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextField2)
            .addContainerGap())
      );
      jPanel3Layout.setVerticalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel1)
               .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel2)
               .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel8)
               .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
      jPanel4.setPreferredSize(new java.awt.Dimension(438, 46));

      jLabel9.setText("[FORMAT 101]***");

      buttonGroup3.add(jRadioButton5);
      jRadioButton5.setText("obs in body");

      buttonGroup3.add(jRadioButton6);
      jRadioButton6.setText("obs in attachment");

      javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
      jPanel4.setLayout(jPanel4Layout);
      jPanel4Layout.setHorizontalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jRadioButton5)
            .addGap(43, 43, 43)
            .addComponent(jRadioButton6)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      jPanel4Layout.setVerticalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addContainerGap(9, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel9)
               .addComponent(jRadioButton5)
               .addComponent(jRadioButton6))
            .addContainerGap(10, Short.MAX_VALUE))
      );

      jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

      jLabel10.setText("[SMTP HOST]* ship email addr.");

      jLabel17.setText("server");

      jLabel13.setText("password**");

      jLabel18.setText("port**");

      javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
      jPanel5.setLayout(jPanel5Layout);
      jPanel5Layout.setHorizontalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(37, 37, 37)
            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(34, 34, 34)
            .addComponent(jLabel13)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
            .addComponent(jLabel18)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(25, 25, 25))
      );
      jPanel5Layout.setVerticalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel10)
               .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel17)
               .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel13)
               .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel18)
               .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

      jLabel11.setText("[GMAIL]* your Gmail address");

      jLabel14.setText("Gmail app password");

      buttonGroup1.add(jRadioButton1);
      jRadioButton1.setText("TLS");

      buttonGroup1.add(jRadioButton2);
      jRadioButton2.setText("SSL");

      javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
      jPanel6.setLayout(jPanel6Layout);
      jPanel6Layout.setHorizontalGroup(
         jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(38, 38, 38)
            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jRadioButton1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jRadioButton2)
            .addGap(25, 25, 25))
      );
      jPanel6Layout.setVerticalGroup(
         jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel11)
               .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel14)
               .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jRadioButton1)
               .addComponent(jRadioButton2))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

      jLabel12.setText("[YAHOO]* your Yahoo address");

      jLabel15.setText("Yahoo app password");

      buttonGroup2.add(jRadioButton3);
      jRadioButton3.setText("TLS");

      buttonGroup2.add(jRadioButton4);
      jRadioButton4.setText("SSL");

      javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
      jPanel7.setLayout(jPanel7Layout);
      jPanel7Layout.setHorizontalGroup(
         jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(39, 39, 39)
            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jRadioButton3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jRadioButton4)
            .addGap(25, 25, 25))
      );
      jPanel7Layout.setVerticalGroup(
         jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel12)
               .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel15)
               .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jRadioButton3)
               .addComponent(jRadioButton4))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
               .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
               .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(15, 15, 15)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
      );

      jLabel16.setText("* optional                                       ** often not required                               ***  consult your PMO ");

      jLabel4.setText("required for: 'Maintenance -> Move log files by Email'");

      jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

      jLabel5.setText("logs email recipient");

      jLabel6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel6.setForeground(new java.awt.Color(0, 0, 255));
      jLabel6.setText("These log files include important data which is of particular value for climate studies");

      jLabel7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel7.setForeground(new java.awt.Color(0, 0, 255));
      jLabel7.setText("Downloading of the log files should be done at routine intervals (ideally not exceeding six months)");

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel2Layout.createSequentialGroup()
                  .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(0, 0, Short.MAX_VALUE))
               .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
      );
      jPanel2Layout.setVerticalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel5)
               .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(26, 26, 26)
            .addComponent(jLabel6)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel7)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jButton1.setText("OK");
      jButton1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            OK_button_actionPerformed(evt);
         }
      });

      jButton2.setText("Cancel");
      jButton2.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Cancel_button_actionPerformed(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(jSeparator1))
               .addGroup(layout.createSequentialGroup()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(layout.createSequentialGroup()
                        .addGap(400, 400, 400)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                     .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                           .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 726, javax.swing.GroupLayout.PREFERRED_SIZE)
                           .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                           .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                           .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                           .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 741, javax.swing.GroupLayout.PREFERRED_SIZE))))
                  .addGap(0, 39, Short.MAX_VALUE)))
            .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGap(30, 30, 30)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel16)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
            .addComponent(jLabel4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(31, 31, 31)
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(15, 15, 15)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(15, 15, 15))
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

    
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
private void OK_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_button_actionPerformed
// TODO add your handling code here:
   
   /* read meta data components */
   
   // [ALL]
   main.obs_email_recipient         = jTextField1.getText().trim();
   main.obs_email_subject           = jTextField2.getText().trim();
   //main.amos_mail = jCheckBox1.isSelected() == true;
   main.obs_email_cc                = jTextField4.getText().trim();
        
   
   // [FORMAT 101]
   if (jRadioButton5.isSelected() == true)                      // email 101 format in body
   {
      main.obs_101_email =  main.FORMAT_101_BODY;
   }
   else if (jRadioButton6.isSelected() == true)                 // email 101 format in attachement
   {
      main.obs_101_email =  main.FORMAT_101_ATTACHEMENT;
   }
       
   
   // [SMTP HOST]        
   main.local_email_server          = jTextField5.getText().trim();
   main.your_ship_address           = jTextField10.getText().trim();
   
   smtp_host_password_plain         = jTextField11.getText().trim();
   
   // save password (in encrypted mode) only if something was changed
   if ((smtp_host_password_plain.equals(password_stars) == false) && (smtp_host_password_plain.equals("") == false) && (smtp_host_password_plain.length() >= 4))
   {
      main.smtp_host_password       = encrypt(smtp_host_password_plain);
   }
   if (smtp_host_password_plain.equals("") == true)
   {
      main.smtp_host_password       = "";
   }
    
   main.smtp_host_port              = jTextField12.getText().trim();
   
   
   // {GMAIL]        
   main.your_gmail_address          = jTextField6.getText().trim();
   gmail_app_password_plain         = jTextField7.getText().trim();
   
   // save password (in encrypted mode) only if something was changed
   if ((gmail_app_password_plain.equals(password_stars) == false) && (gmail_app_password_plain.equals("") == false) && (gmail_app_password_plain.length() >= 8))
   {
      main.gmail_app_password       = encrypt(gmail_app_password_plain);
   }
   if (gmail_app_password_plain.equals(""))
   {
      main.gmail_app_password       = "";
   }
   
   if (jRadioButton1.isSelected() == true)  
   {
      main.gmail_security           = main.GMAIL_TLS;
   }
   else if (jRadioButton2.isSelected() == true)  
   {
      main.gmail_security           = main.GMAIL_SSL;
   }   
   
   
   // [YAHOO]
   main.your_yahoo_address          = jTextField8.getText().trim();
   yahoo_app_password_plain         = jTextField9.getText().trim();
   
   // save password (in encrypted mode) only if something was changed
   if ((yahoo_app_password_plain.equals(password_stars) == false) && (yahoo_app_password_plain.equals("") == false) && (yahoo_app_password_plain.length() >= 8))
   {
      main.yahoo_app_password       = encrypt(yahoo_app_password_plain);
   }
   if (yahoo_app_password_plain.equals(""))
   {
      main.yahoo_app_password       = "";
   }
   
   if (jRadioButton3.isSelected() == true)  
   {
      main.yahoo_security           = main.YAHOO_TLS;
   }
   else if (jRadioButton4.isSelected() == true)  
   {
      main.yahoo_security           = main.YAHOO_SSL;
   } 


   // [LOGS]
   main.logs_email_recipient        = jTextField3.getText().trim();
   
           
   
   if (main.offline_mode_via_cmd == true)
   {
      main.schrijf_configuratie_regels();          
   }
   else // so offline_via_jnlp mode or online (webstart) mode
   {
      main.set_muffin();
      main.schrijf_configuratie_regels(); 
   }   

   /* message */
   //String info = "Changes will take effect inmediatelly. Not necessary to restart";
   String info = "Changes will take full effect after a " + main.APPLICATION_NAME + " restart";
   JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " info", JOptionPane.INFORMATION_MESSAGE);

   /* close this E-mail settings input page */
   setVisible(false);
   dispose();
}//GEN-LAST:event_OK_button_actionPerformed



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
private void Cancel_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_button_actionPerformed
// TODO add your handling code here:

   /* close input page */
   setVisible(false);
   dispose();
   
}//GEN-LAST:event_Cancel_button_actionPerformed



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static String encrypt(String plainText) 
   {
      String encryptedText = "";
      try 
      {
         Cipher cipher = Cipher.getInstance(cipherTransformation);
         byte[] key = encryptionKey.getBytes(characterEncoding);
         SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
         IvParameterSpec ivparameterspec = new IvParameterSpec(key);
         cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
         byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF8"));
            
         Base64.Encoder encoder = Base64.getEncoder();                                // Java 8 and higher
         encryptedText = encoder.encodeToString(cipherText);
            
         //encryptedText = DatatypeConverter.printBase64Binary(cipherText);           // Java 6 and 7 but not possible Java 12 and higher.
      } 
      catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException E) 
      {
         //System.err.println("[EMAIL] Encrypt Exception : " + E.getMessage());
         
         String info = "encrypt exception: " + E.getMessage();
         main.log_turbowin_system_message("[EMAIL] " + info);
         
         //JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " info", JOptionPane.INFORMATION_MESSAGE);
      }
      
      
      return encryptedText;
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static String decrypt(String encryptedText) 
   {
      String decryptedText = "";
      try 
      {
         Cipher cipher = Cipher.getInstance(cipherTransformation);
         byte[] key = encryptionKey.getBytes(characterEncoding);
         SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
         IvParameterSpec ivparameterspec = new IvParameterSpec(key);
         cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
          
         Base64.Decoder decoder = Base64.getDecoder();                                 // Java 8 and higher
         byte[] cipherText = decoder.decode(encryptedText.getBytes("UTF8"));
         //byte[] cipherText = DatatypeConverter.parseBase64Binary(encryptedText);     // Java 6/7 but not availble in Java 12 etc.
            
         decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");
      } 
      catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException E) 
      {
         //System.err.println("decrypt Exception : "+E.getMessage());
         String info = "decrypt exception: " + E.getMessage();
         main.log_turbowin_system_message("[EMAIL] " + info);
      }
      
      
      return decryptedText;
   }
   
   



   /**
   * @param args the command line arguments
   */
   public static void main(String args[]) {
      java.awt.EventQueue.invokeLater(new Runnable() {
         @Override
            public void run() {
               new myemailsettings().setVisible(true);
            }
      });
   }
   
   

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.ButtonGroup buttonGroup1;
   private javax.swing.ButtonGroup buttonGroup2;
   private javax.swing.ButtonGroup buttonGroup3;
   private javax.swing.JButton jButton1;
   private javax.swing.JButton jButton2;
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
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JLabel jLabel6;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JLabel jLabel8;
   private javax.swing.JLabel jLabel9;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JPanel jPanel3;
   private javax.swing.JPanel jPanel4;
   private javax.swing.JPanel jPanel5;
   private javax.swing.JPanel jPanel6;
   private javax.swing.JPanel jPanel7;
   private javax.swing.JRadioButton jRadioButton1;
   private javax.swing.JRadioButton jRadioButton2;
   private javax.swing.JRadioButton jRadioButton3;
   private javax.swing.JRadioButton jRadioButton4;
   private javax.swing.JRadioButton jRadioButton5;
   private javax.swing.JRadioButton jRadioButton6;
   private javax.swing.JSeparator jSeparator1;
   private javax.swing.JTextField jTextField1;
   private javax.swing.JTextField jTextField10;
   private javax.swing.JTextField jTextField11;
   private javax.swing.JTextField jTextField12;
   private javax.swing.JTextField jTextField2;
   private javax.swing.JTextField jTextField3;
   private javax.swing.JTextField jTextField4;
   private javax.swing.JTextField jTextField5;
   private javax.swing.JTextField jTextField6;
   private javax.swing.JTextField jTextField7;
   private javax.swing.JTextField jTextField8;
   private javax.swing.JTextField jTextField9;
   // End of variables declaration//GEN-END:variables

   
   private static final String encryptionKey                 = "????????????????";     //"ABCDEFGHIJKLMNOP";
   private static final String characterEncoding             = "UTF-8";
   private static final String cipherTransformation          = "AES/CBC/PKCS5PADDING";
   private static final String aesEncryptionAlgorithem       = "AES";
   
   private String gmail_app_password_plain                   = "";
   private String yahoo_app_password_plain                   = "";
   private String smtp_host_password_plain                   = "";
   private final String password_stars                       = "******";
   
   JPopupMenu popup;
   JPopupMenu popup2;
}
