package turbowin;


import java.awt.Cursor;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/*
 * about.java
 *
 * Created on 5 november 2008, 9:12
 */



/**
 *
 * @author  Martin
 */
final public class about extends javax.swing.JFrame {

    /** Creates new form about */
    public about() {
        initComponents();
        initComponents2();
        setLocation(main.x_pos_about_frame, main.y_pos_about_frame);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jSeparator1 = new javax.swing.JSeparator();
      jButton1 = new javax.swing.JButton();
      jLabel1 = new javax.swing.JLabel();
      jLabel4 = new javax.swing.JLabel();
      jPanel1 = new javax.swing.JPanel();
      jLabel2 = new javax.swing.JLabel();
      jLabel6 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      jLabel5 = new javax.swing.JLabel();
      jLabel8 = new javax.swing.JLabel();
      jLabel9 = new javax.swing.JLabel();
      jLabel7 = new javax.swing.JLabel();
      jLabel10 = new javax.swing.JLabel();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle("About TurboWin+");
      setMinimumSize(new java.awt.Dimension(500, 600));
      setPreferredSize(new java.awt.Dimension(500, 500));
      setResizable(false);

      jButton1.setText("OK");
      jButton1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            OK_button_actionPerformed(evt);
         }
      });

      jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
      jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel1.setText("Turbo+");

      jLabel4.setForeground(new java.awt.Color(51, 0, 255));
      jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel4.setText("<html><u>Visit the TurboWin Web Site</u></html>");
      jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            Info_about_web_start_link_mouseClicked(evt);
         }
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            Info_about_mouseEntered(evt);
         }
         public void mouseExited(java.awt.event.MouseEvent evt) {
            Info_about_mouseExited(evt);
         }
      });

      jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel2.setText("Endorsed by EUMETNET (European Meteorological Services Network),");

      jLabel6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel6.setText("NOAA (US National Oceanic and Atmospheric Administration)");

      jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel3.setText("and WMO (World Meteorological Organization) for use ");

      jLabel5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel5.setText("onboard voluntary observing ships");

      jLabel8.setForeground(new java.awt.Color(51, 0, 255));
      jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel8.setText("<html><u>TurboWin+ powered by E-SURFMAR</u></html>");
      jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            Info_About_ESURFMAR_link_mouseClicked(evt);
         }
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            Info_About_ESURFMAR_link_mouseEntered(evt);
         }
         public void mouseExited(java.awt.event.MouseEvent evt) {
            Info_About_ESURFMAR_link_mouseExited(evt);
         }
      });

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jLabel8)
               .addGroup(jPanel1Layout.createSequentialGroup()
                  .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                     .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                  .addContainerGap())))
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel6)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel5)
            .addGap(5, 5, 5))
      );

      jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

      jLabel7.setForeground(new java.awt.Color(51, 0, 255));
      jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel7.setText("<html><u>TurboWin+ is free software and open-source software (GPLv3)</u></html>");
      jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            Info_about_GPL_link_mouseClicked(evt);
         }
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            Info_about_GPL_link_mouseEntered(evt);
         }
         public void mouseExited(java.awt.event.MouseEvent evt) {
            Info_about_GPL_link_mouseExited(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                  .addGap(0, 197, Short.MAX_VALUE)
                  .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(0, 200, Short.MAX_VALUE))
               .addGroup(layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                     .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                  .addGap(0, 0, Short.MAX_VALUE))
               .addGroup(layout.createSequentialGroup()
                  .addGap(27, 27, 27)
                  .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(0, 0, Short.MAX_VALUE))
               .addGroup(layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(jLabel7))
               .addGroup(layout.createSequentialGroup()
                  .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
         .addGroup(layout.createSequentialGroup()
            .addGap(173, 173, 173)
            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addComponent(jLabel1)
            .addGap(18, 18, 18)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(15, 15, 15)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(15, 15, 15))
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void initComponents2()
{
   // nb could also be set in design mode but then this will be fixed even if the APPLICATION_NAME was changed
   setTitle("about " + main.APPLICATION_NAME);
   
   // this will overwrite default value "Turbo+" (which is in design mode)
   //jLabel1.setText(main.APPLICATION_NAME + " 2.5.7 (build 22-August-2016)");
   jLabel1.setText(main.APPLICATION_NAME + " " + main.APPLICATION_VERSION);
   
   //jLabel9.setPreferredSize(new Dimension (144, 144));            // so label13 will be 'reused' for the logo !!
   //jLabel9.setVisible(true);
   
   if (main.GUI_logo.equals(main.LOGO_EUMETNET))
   {
      loadImage_about_logo(main.ICONS_DIRECTORY + "logo-eumetnet.png");
   }
   else if (main.GUI_logo.equals(main.LOGO_NOAA))
   {
      loadImage_about_logo(main.ICONS_DIRECTORY + "logo-noaa.png");
   }
   else if (main.GUI_logo.equals(main.LOGO_SOT))
   {
      loadImage_about_logo(main.ICONS_DIRECTORY + "logo-sot.png");
   }
   else // default
   {
      loadImage_about_logo(main.ICONS_DIRECTORY + "logo-eumetnet.png");
   }   
   
   
   // GPLv3 logo
   loadImage_GPL_logo(main.ICONS_DIRECTORY + "gplv3-with-text-136x68.png");
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void loadImage_about_logo(final String imagePath) 
{
   new SwingWorker<ImageIcon, Object>() 
   {
      @Override
      public ImageIcon doInBackground() 
      {
         return createImageIcon(imagePath);
      }

      @Override
      public void done()
      {
         try
         {        
            ImageIcon logo_icon = get();
            jLabel9.setIcon(logo_icon); 
         } // try
         catch (InterruptedException ignore) { }
         catch (java.util.concurrent.ExecutionException e) 
         {
            String why;
            Throwable cause = e.getCause();
            if (cause != null) 
            {
               why = cause.getMessage();
            } 
            else 
            {
               why = e.getMessage();
            }
            //System.err.println("Error retrieving file: " + why);
            JOptionPane.showMessageDialog(null, "Error retrieving file: " + why, main.APPLICATION_NAME, JOptionPane.ERROR_MESSAGE);
         } // catch         
      } //  public void done()
   }.execute();
} // private void loadImage(final String imagePath, final int index)



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void loadImage_GPL_logo(final String imagePath) 
{
   new SwingWorker<ImageIcon, Object>() 
   {
      @Override
      public ImageIcon doInBackground() 
      {
         return createImageIcon(imagePath);
      }

      @Override
      public void done()
      {
         try
         {        
            ImageIcon logo_icon = get();
            jLabel10.setIcon(logo_icon); 
         } // try
         catch (InterruptedException ignore) { }
         catch (java.util.concurrent.ExecutionException e) 
         {
            String why;
            Throwable cause = e.getCause();
            if (cause != null) 
            {
               why = cause.getMessage();
            } 
            else 
            {
               why = e.getMessage();
            }
            //System.err.println("Error retrieving file: " + why);
            JOptionPane.showMessageDialog(null, "Error retrieving file: " + why, main.APPLICATION_NAME, JOptionPane.ERROR_MESSAGE);
         } // catch         
      } //  public void done()
   }.execute();
} // private void loadImage(final String imagePath, final int index)



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public ImageIcon createImageIcon(String path_and_file)
{
   URL url = null;

   try
   {
      url = getClass().getResource(path_and_file);
   }
   catch (Exception e) { /* ... */}

   ImageIcon icon_glyph = new javax.swing.ImageIcon(url);

   return icon_glyph;
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void OK_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_button_actionPerformed
// TODO add your handling code here:
   setVisible(false);
   dispose();
}//GEN-LAST:event_OK_button_actionPerformed



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Info_about_web_start_link_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Info_about_web_start_link_mouseClicked
// TODO add your handling code here:

   link_mouse_clicked(LINK_TURBOWIN);
}//GEN-LAST:event_Info_about_web_start_link_mouseClicked



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Info_about_mouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Info_about_mouseEntered
// TODO add your handling code here:

   link_mouse_entered();
}//GEN-LAST:event_Info_about_mouseEntered



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Info_about_mouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Info_about_mouseExited
// TODO add your handling code here:

   link_mouse_exited();
}//GEN-LAST:event_Info_about_mouseExited



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
   private void Info_About_ESURFMAR_link_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Info_About_ESURFMAR_link_mouseClicked
      // TODO add your handling code here:
      link_mouse_clicked(LINK_ESURFMAR);
   }//GEN-LAST:event_Info_About_ESURFMAR_link_mouseClicked

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/   
   private void Info_About_ESURFMAR_link_mouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Info_About_ESURFMAR_link_mouseEntered
      // TODO add your handling code here:
      
      link_mouse_entered();
   }//GEN-LAST:event_Info_About_ESURFMAR_link_mouseEntered

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/   
   private void Info_About_ESURFMAR_link_mouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Info_About_ESURFMAR_link_mouseExited
      // TODO add your handling code here:
      
      link_mouse_exited();
   }//GEN-LAST:event_Info_About_ESURFMAR_link_mouseExited

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/  
   private void Info_about_GPL_link_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Info_about_GPL_link_mouseClicked
      // TODO add your handling code here:
      link_mouse_clicked(LINK_GITHUB);
   }//GEN-LAST:event_Info_about_GPL_link_mouseClicked

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/  
   private void Info_about_GPL_link_mouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Info_about_GPL_link_mouseEntered
      // TODO add your handling code here:
      link_mouse_entered();
   }//GEN-LAST:event_Info_about_GPL_link_mouseEntered

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/  
   private void Info_about_GPL_link_mouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Info_about_GPL_link_mouseExited
      // TODO add your handling code here:
      link_mouse_exited();
   }//GEN-LAST:event_Info_about_GPL_link_mouseExited


   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/  
   private void link_mouse_entered()   
   {
      setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
   }
   
   
/***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/  
   private void link_mouse_exited()   
   {
      setCursor(Cursor.getDefaultCursor());
   }   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/  
   private void link_mouse_clicked(String link_subject)
   {
      
      new SwingWorker<Integer, Void>()
      {
         
         @Override
         protected Integer doInBackground() throws Exception
         {
            String link_url = "";
      
            if (link_subject.equals(LINK_ESURFMAR))
            {
               link_url = "http://eumetnet.eu/activities/observations-programme/current-activities/e-surfmar/"; 
            }
            else if (link_subject.equals(LINK_GITHUB))
            {
               link_url = "https://github.com/KNMI/turbowin_plus/"; 
            }
            else if (link_subject.equals(LINK_TURBOWIN))
            {
               link_url = "https://projects.knmi.nl/turbowin/"; 
            }
            
            Integer code = 0;                              // ok
            Desktop desktop = null;

            // Before more Desktop API is used, first check
            // whether the API is supported by this particular
            // virtual machine (VM) on this particular host.
            if (Desktop.isDesktopSupported())
            {
               desktop = Desktop.getDesktop();
               URI uri = null;
               try
               {
                  String http_adres = link_url;  
                  uri = new URI(http_adres);
                  desktop.browse(uri);
               }
               catch (IOException | URISyntaxException ioe) 
               { 
                  code = -2;
               }
            } // if (Desktop.isDesktopSupported())
            else
            {
               //JOptionPane.showMessageDialog(null, "Error invoking default web browser (-Desktop-method not supported on this computer system)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               code = -1;
            } // else

            return code;

         } // protected Integer doInBackground() throws Exception
      
         @Override
         protected void done()
         {
            try
            {
               Integer response_code = get();

               if (response_code == -1)
               {
                  String message = "[GENERAL] Error invoking default web browser (-Desktop-method not supported on this computer system)";
                  JOptionPane.showMessageDialog(null, message, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                  main.log_turbowin_system_message(message);
               }   
               else if (response_code == -2)
               {
                  String message = "[GENERAL] Error invoking URL";
                  JOptionPane.showMessageDialog(null, message, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                  main.log_turbowin_system_message(message);
               }   
            } // try
            catch (InterruptedException | ExecutionException ex) 
            {   
               String message = "[GENERAL] Error invoking default web browser; " + ex.toString(); 
               main.log_turbowin_system_message(message);
               //main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message);
            } // catch
         } // protected void done()      
      }.execute(); // new SwingWorker<Void, Void>()
   }
   


    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
         @Override
            public void run() {
                new about().setVisible(true);
            }
        });
    }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton jButton1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel10;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JLabel jLabel6;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JLabel jLabel8;
   private javax.swing.JLabel jLabel9;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JSeparator jSeparator1;
   // End of variables declaration//GEN-END:variables

   
   private final String LINK_ESURFMAR  =    "link_esurfmar";
   private final String LINK_GITHUB    =    "link_github";
   private final String LINK_TURBOWIN  =    "link_turbowin";
}
