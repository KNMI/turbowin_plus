/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbowin;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.Timer;

/**
 *
 * @author marti
 */
public class DASHBOARD_view_AWS_radar extends javax.swing.JFrame 
{

   /**
    * Creates new form DASHBOARD_view_AWS_radar
    */
   
   /* inner class popupListener */
   class PopupListener extends MouseAdapter 
   {
      @Override
      public void mousePressed(MouseEvent e) 
      {
         maybeShowPopup(e);
         //System.out.println("Popup menu will be visible!");
      }
      @Override
      public void mouseReleased(MouseEvent e) 
      {
         maybeShowPopup(e);
      }

      private void maybeShowPopup(MouseEvent e) 
      {
         if (e.isPopupTrigger()) 
         {
            popup.show(e.getComponent(), e.getX(), e.getY());
         }
      }
   }      
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/   
   private void initComponents1()
   {
      /* background color main panel (set by main menu theme option) */
      if (main.theme_mode.equals(main.THEME_NIMBUS_NIGHT))
      {
         night_vision = true;
         
         background_color_panel1 = jPanel1.getBackground();
         //background_color_panel1 = Color.LIGHT_GRAY;
         background_color_panel2 = Color.LIGHT_GRAY;
         background_color_panel3 = Color.LIGHT_GRAY;
         background_color_panel4 = Color.LIGHT_GRAY;
         background_color_panel5 = Color.LIGHT_GRAY;
              
         jPanel1.setBackground(Color.DARK_GRAY);
         jPanel2.setBackground(Color.BLACK);
         jPanel3.setBackground(Color.BLACK);
         jPanel4.setBackground(Color.BLACK);
         jPanel5.setBackground(Color.BLACK); 
      }
      else
      {
         night_vision = false;
         
         //background_color_panel1 = jPanel1.getBackground();
          background_color_panel1 = jPanel4.getBackground();    // jPanel4 = left panel (but could also be another (site)panel)
         jPanel1.setBackground(background_color_panel1); 
         
         background_color_panel2 = jPanel2.getBackground();
         background_color_panel3 = jPanel3.getBackground();
         background_color_panel4 = jPanel4.getBackground();
         background_color_panel5 = jPanel5.getBackground();
      } 
   
      
      /* background color main panel (set by popup menu option) */
      popup = new JPopupMenu();
      
      JMenuItem menuItem = new JMenuItem("night colours");
      menuItem.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            night_vision = true;
       
            jPanel1.setBackground(Color.DARK_GRAY); 
            jPanel2.setBackground(Color.BLACK);
            jPanel3.setBackground(Color.BLACK);
            jPanel4.setBackground(Color.BLACK);
            jPanel5.setBackground(Color.BLACK); 
         }
      });
      popup.add(menuItem);
      
      JMenuItem menuItem2 = new JMenuItem("day colours");
      menuItem2.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            night_vision = false;
            
            jPanel1.setBackground(background_color_panel1); 
            jPanel2.setBackground(background_color_panel2);
            jPanel3.setBackground(background_color_panel3);
            jPanel4.setBackground(background_color_panel4);
            jPanel5.setBackground(background_color_panel5); 
         }
      });
      popup.add(menuItem2);  

      
      popup.add(new JSeparator()); // SEPARATOR
      
 
      JMenuItem menuItem3 = new JMenuItem("container ship");
      menuItem3.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            main.ship_type_dashboard = main.CONTAINER_SHIP;
            repaint();
            
            // write meta data to muffins or configuration files
            if (main.offline_mode_via_cmd == true)                          // also if the turbowin_launcher is present (JPMS)
            {
                main.schrijf_configuratie_regels();          
            }
            else // so offline_via_jnlp mode or online (webstart) mode
            {
               main.set_muffin();
               main.schrijf_configuratie_regels();
            }  
         }
      });
      popup.add(menuItem3);  
      
      JMenuItem menuItem4 = new JMenuItem("bulk carrier");
      menuItem4.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            main.ship_type_dashboard = main.BULK_CARRIER;
            repaint();
            
            // write meta data to muffins or configuration files
            if (main.offline_mode_via_cmd == true)                          // also if the turbowin_launcher is present (JPMS)
            {
                main.schrijf_configuratie_regels();          
            }
            else // so offline_via_jnlp mode or online (webstart) mode
            {
               main.set_muffin();
               main.schrijf_configuratie_regels();
            }  
         }
      });
      popup.add(menuItem4);  
      
      
      JMenuItem menuItem5 = new JMenuItem("oil tanker");
      menuItem5.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            main.ship_type_dashboard = main.OIL_TANKER;
            repaint();
            
            // write meta data to muffins or configuration files
            if (main.offline_mode_via_cmd == true)                          // also if the turbowin_launcher is present (JPMS)
            {
                main.schrijf_configuratie_regels();          
            }
            else // so offline_via_jnlp mode or online (webstart) mode
            {
               main.set_muffin();
               main.schrijf_configuratie_regels();
            }  
         }
      });
      popup.add(menuItem5);  
 
      JMenuItem menuItem6 = new JMenuItem("LNG tanker");
      menuItem6.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            main.ship_type_dashboard = main.LNG_TANKER;
            repaint();
            
            // write meta data to muffins or configuration files
            if (main.offline_mode_via_cmd == true)                          // also if the turbowin_launcher is present (JPMS)
            {
                main.schrijf_configuratie_regels();          
            }
            else // so offline_via_jnlp mode or online (webstart) mode
            {
               main.set_muffin();
               main.schrijf_configuratie_regels();
            }  
         }
      });
      popup.add(menuItem6);  
      
      
      JMenuItem menuItem7 = new JMenuItem("passenger ship");
      menuItem7.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            main.ship_type_dashboard = main.PASSENGER_SHIP;
            repaint();
            
            // write meta data to muffins or configuration files
            if (main.offline_mode_via_cmd == true)                          // also if the turbowin_launcher is present (JPMS)
            {
                main.schrijf_configuratie_regels();          
            }
            else // so offline_via_jnlp mode or online (webstart) mode
            {
               main.set_muffin();
               main.schrijf_configuratie_regels();
            }  
         }
      });
      popup.add(menuItem7); 
      
      
      JMenuItem menuItem8 = new JMenuItem("neutral ship");
      menuItem8.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            main.ship_type_dashboard = main.NEUTRAL_SHIP;
            repaint();
            
            // write meta data to muffins or configuration files
            if (main.offline_mode_via_cmd == true)                          // also if the turbowin_launcher is present (JPMS)
            {
                main.schrijf_configuratie_regels();          
            }
            else // so offline_via_jnlp mode or online (webstart) mode
            {
               main.set_muffin();
               main.schrijf_configuratie_regels();
            }  
         }
      });
      popup.add(menuItem8);  
      
      
      JMenuItem menuItem9 = new JMenuItem("general cargo ship");
      menuItem9.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            main.ship_type_dashboard = main.GENERAL_CARGO_SHIP;
            repaint();
            
            // write meta data to muffins or configuration files
            if (main.offline_mode_via_cmd == true)                          // also if the turbowin_launcher is present (JPMS)
            {
                main.schrijf_configuratie_regels();          
            }
            else // so offline_via_jnlp mode or online (webstart) mode
            {
               main.set_muffin();
               main.schrijf_configuratie_regels();
            }  
         }
      });
      popup.add(menuItem9);  
      
      
      JMenuItem menuItem10 = new JMenuItem("research vessel");
      menuItem10.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            main.ship_type_dashboard = main.RESEARCH_VESSEL;
            repaint();
            
            // write meta data to muffins or configuration files
            if (main.offline_mode_via_cmd == true)                          // also if the turbowin_launcher is present (JPMS)
            {
                main.schrijf_configuratie_regels();          
            }
            else // so offline_via_jnlp mode or online (webstart) mode
            {
               main.set_muffin();
               main.schrijf_configuratie_regels();
            }  
         }
      });
      popup.add(menuItem10);  
      

      JMenuItem menuItem11 = new JMenuItem("Ro-Ro ship");
      menuItem11.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            main.ship_type_dashboard = main.RO_RO_SHIP;
            repaint();
            
            // write meta data to muffins or configuration files
            if (main.offline_mode_via_cmd == true)                          // also if the turbowin_launcher is present (JPMS)
            {
                main.schrijf_configuratie_regels();          
            }
            else // so offline_via_jnlp mode or online (webstart) mode
            {
               main.set_muffin();
               main.schrijf_configuratie_regels();
            }  
         }
      });
      popup.add(menuItem11);  
      
      JMenuItem menuItem12 = new JMenuItem("ferry");
      menuItem12.addActionListener(new java.awt.event.ActionListener() 
      {
         @Override
         public void actionPerformed(ActionEvent e) 
         {
            main.ship_type_dashboard = main.FERRY;
            repaint();
            
            // write meta data to muffins or configuration files
            if (main.offline_mode_via_cmd == true)                          // also if the turbowin_launcher is present (JPMS)
            {
                main.schrijf_configuratie_regels();          
            }
            else // so offline_via_jnlp mode or online (webstart) mode
            {
               main.set_muffin();
               main.schrijf_configuratie_regels();
            }  
         }
      });
      popup.add(menuItem12);  
      
      
      MouseListener popupListener = new DASHBOARD_view_AWS_radar.PopupListener();
      addMouseListener(popupListener);
      
      
      // title
      setTitle(main.APPLICATION_NAME + " Automatic Weather Station Dashboard [wind radar]");
   
      // NB see below, otherwise if you select Dashboard -> AWS for the second, third or xth time it wil first display the situation 
      //    of the moment that the dasboard was closed, and after approx 1 minute it will be updated. 
      //    NOW it will update the dashboard immediately
      jPanel1.repaint();     
      
   } // private void initComponents1()
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void init_dashboard_AWS_radar_timer()
   {
      // updating/displaying received AWS sensor data (not from file), timer scheduled
      //
      // called from: DASHBOARD_view_AWS_radar() [DASHBOARD_view_AWS_radar.java]
   
      ActionListener update_dashboard_AWS_radar_action = new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            jPanel1.repaint();                                                        // main panel
         } 
      };  

      // main loop for updating AWS hybrid dashboard
      dashboard_update_timer_AWS_radar = new Timer(DELAY_UPDATE_AWS_RADAR_SENSOR_LOOP, update_dashboard_AWS_radar_action);
      dashboard_update_timer_AWS_radar.setRepeats(true);                                               // false = only one action
      dashboard_update_timer_AWS_radar.setInitialDelay(INITIAL_DELAY_UPDATE_AWS_RADAR_SENSOR_LOOP);    // time in millisec to wait after timer is started to fire first event
      dashboard_update_timer_AWS_radar.setCoalesce(true);                                              // by default true, but to be certain
      dashboard_update_timer_AWS_radar.restart();
      dashboard_update_timer_AWS_radar_is_gecreeerd = true;
   }   
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public DASHBOARD_view_AWS_radar() 
   {
      initComponents();
      initComponents1();
      init_dashboard_AWS_radar_timer();
      
      if (main.theme_mode.equals(main.THEME_TRANSPARENT))   
      {
         setOpacity(0.75f);
      } // else if (theme_mode.equals(THEME_TRANSPARENT))  
   }

   
   /**
    * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form
    * Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents()
   {

      jPanel5 = new javax.swing.JPanel();
      jPanel2 = new javax.swing.JPanel();
      jPanel3 = new javax.swing.JPanel();
      jLabel2 = new javax.swing.JLabel();
      jPanel4 = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      jButton1 = new javax.swing.JButton();
      jPanel1 = new DASHBOARD_grafiek_AWS_radar();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      addWindowListener(new java.awt.event.WindowAdapter()
      {
         public void windowClosed(java.awt.event.WindowEvent evt)
         {
            Dashboard_view_AWS_radar_windowClosed(evt);
         }
         public void windowDeiconified(java.awt.event.WindowEvent evt)
         {
            Dashboard_AWS_radar_windowDeiconified(evt);
         }
      });

      jPanel5.setPreferredSize(new java.awt.Dimension(10, 551));

      javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
      jPanel5.setLayout(jPanel5Layout);
      jPanel5Layout.setHorizontalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 10, Short.MAX_VALUE)
      );
      jPanel5Layout.setVerticalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 600, Short.MAX_VALUE)
      );

      getContentPane().add(jPanel5, java.awt.BorderLayout.WEST);

      jPanel2.setPreferredSize(new java.awt.Dimension(10, 551));

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 10, Short.MAX_VALUE)
      );
      jPanel2Layout.setVerticalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 600, Short.MAX_VALUE)
      );

      getContentPane().add(jPanel2, java.awt.BorderLayout.EAST);

      jPanel3.setPreferredSize(new java.awt.Dimension(1209, 40));

      jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
      jLabel2.setText("--- Wind speed and dir = 10 min average. Wind gust = max wind in last 10 min. MSLP = 1 min median. Press read, Air temp, RH, SST = 1 min median at sensor ht. SOG, COG, Heading = 10 min average ---");

      javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
      jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1189, Short.MAX_VALUE)
            .addContainerGap())
      );
      jPanel3Layout.setVerticalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
      );

      getContentPane().add(jPanel3, java.awt.BorderLayout.NORTH);

      jPanel4.setPreferredSize(new java.awt.Dimension(1209, 40));

      jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
      jLabel1.setText("--- right click for night colors and ship type ---");

      jButton1.setText("make visual observation");
      jButton1.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            make_obs_actionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
      jPanel4.setLayout(jPanel4Layout);
      jPanel4Layout.setHorizontalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(260, 260, 260)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
            .addGap(260, 260, 260)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
      );
      jPanel4Layout.setVerticalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jButton1)))
            .addContainerGap())
      );

      getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);

      jPanel1.setBackground(java.awt.Color.lightGray);
      jPanel1.setPreferredSize(new java.awt.Dimension(800, 600));
      jPanel1.addComponentListener(new java.awt.event.ComponentAdapter()
      {
         public void componentResized(java.awt.event.ComponentEvent evt)
         {
            DASHBOARD_view_radar_componentResizedHandler(evt);
         }
      });

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 1189, Short.MAX_VALUE)
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 600, Short.MAX_VALUE)
      );

      getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

      pack();
   }// </editor-fold>//GEN-END:initComponents

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/      
   private void DASHBOARD_view_radar_componentResizedHandler(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_DASHBOARD_view_radar_componentResizedHandler
      // TODO add your handling code here:
      System.out.println("--- AWS Dashboard wind radar (jPanel1) size = " + DASHBOARD_view_AWS_radar.jPanel1.getSize());
      
      width_AWS_radar_dashboard = jPanel1.getWidth();
      height_AWS_radar_dashboard = jPanel1.getHeight();
      
      jPanel1.repaint();  
   }//GEN-LAST:event_DASHBOARD_view_radar_componentResizedHandler

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/      
   private void make_obs_actionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_make_obs_actionPerformed
   {//GEN-HEADEREND:event_make_obs_actionPerformed
      // TODO add your handling code here:
      
      if (main.mainClass != null)
      {   
         if ((turbowin.main.ICONIFIED & main.mainClass.getExtendedState()) == turbowin.main.ICONIFIED)
         {
            if (turbowin.main.trayIcon != null)
            {
               main.mainClass.tray.remove(turbowin.main.trayIcon) ; 
            }
         }
         
         main.mainClass.setExtendedState(NORMAL);
         main.mainClass.setVisible(true); 
         
      } // if (main.mainClass != null)
   }//GEN-LAST:event_make_obs_actionPerformed

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/    
   private void Dashboard_AWS_radar_windowDeiconified(java.awt.event.WindowEvent evt)//GEN-FIRST:event_Dashboard_AWS_radar_windowDeiconified
   {//GEN-HEADEREND:event_Dashboard_AWS_radar_windowDeiconified
      // TODO add your handling code here:
      
      System.out.println("--- AWS Dashboard wind radar (jPanel1) deiconified");
      jPanel1.repaint();  
   }//GEN-LAST:event_Dashboard_AWS_radar_windowDeiconified

   
   
/***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/    
   private void Dashboard_view_AWS_radar_windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_Dashboard_view_AWS_radar_windowClosed
      // TODO add your handling code here:
      
      if (dashboard_update_timer_AWS_radar_is_gecreeerd == true)  
      {
         if (dashboard_update_timer_AWS_radar.isRunning())
         {
            dashboard_update_timer_AWS_radar.stop();
         }
      }
      
      dashboard_update_timer_AWS_radar = null;
      
      dashboard_update_timer_AWS_radar_is_gecreeerd = false;  
   }//GEN-LAST:event_Dashboard_view_AWS_radar_windowClosed

   
   
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
            if ("Nimbus".equals(info.getName())) {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (ClassNotFoundException ex) {
         java.util.logging.Logger.getLogger(DASHBOARD_view_AWS_radar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (InstantiationException ex) {
         java.util.logging.Logger.getLogger(DASHBOARD_view_AWS_radar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (IllegalAccessException ex) {
         java.util.logging.Logger.getLogger(DASHBOARD_view_AWS_radar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (javax.swing.UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(DASHBOARD_view_AWS_radar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      //</editor-fold>

      /* Create and display the form */
      java.awt.EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            new DASHBOARD_view_AWS_radar().setVisible(true);
         }
      });
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   public static javax.swing.JButton jButton1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   public static javax.swing.JLabel jLabel3;
   /*
   private javax.swing.JPanel jPanel1;
   */private static DASHBOARD_grafiek_AWS_radar jPanel1;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JPanel jPanel3;
   public static javax.swing.JPanel jPanel4;
   private javax.swing.JPanel jPanel5;
   // End of variables declaration//GEN-END:variables

   public static int width_AWS_radar_dashboard;
   public static int height_AWS_radar_dashboard;
   public static Timer dashboard_update_timer_AWS_radar;
   public static boolean dashboard_update_timer_AWS_radar_is_gecreeerd;
   public static boolean night_vision;
   private final int DELAY_UPDATE_AWS_RADAR_SENSOR_LOOP                 = 60000; // 1 min                 // time in millisec to wait after timer is started to fire first event (10 min = 10 * 1000 * 60 * 10 = 600000)
   private final int INITIAL_DELAY_UPDATE_AWS_RADAR_SENSOR_LOOP         = 0; // 1000 = 1 sec              // time in millisec to wait after timer is started to fire first event
   //public static Timer dashboard_update_AWS_radar_timer;
   //public static boolean dashboard_update_AWS_radar_timer_is_gecreeerd;
   private JPopupMenu popup;
   private static Color background_color_panel1;
   private static Color background_color_panel2;
   private static Color background_color_panel3;
   private static Color background_color_panel4;
   private static Color background_color_panel5;


}
