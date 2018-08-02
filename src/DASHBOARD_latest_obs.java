
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Martin
 */
public class DASHBOARD_latest_obs extends javax.swing.JFrame {

   /**
    * Creates new form DASHBOARD_latest_obs
    */
   public DASHBOARD_latest_obs() {
      initComponents();
      initComponents2();
      setLocation(main.x_pos_small_frame, main.y_pos_small_frame);
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void initComponents2()
   {
      Boolean doorgaan = true;
      
      
      if (main.logs_dir.trim().equals("") == true || main.logs_dir.trim().length() < 2)
      {
         doorgaan = false;
         String info = "logs folder unknown, select: Maintenance -> Log files settings";
         JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
      }
      
      
      if (doorgaan)
      {
         new SwingWorker<String, Void>()
         {
            @Override
            protected String doInBackground() throws Exception
            {
               String latest_immt_record = "";  
               String record = "";
      
               // first check if there is an immt log source file present (and not empty) 
               String volledig_path_immt = main.logs_dir + java.io.File.separator + main.IMMT_LOG;

               File immt_file = new File(volledig_path_immt);
               if (immt_file.exists() && immt_file.length() > 0)     // length() in bytes
               {
                  BufferedReader in = null;

                  try
                  {
                     in = new BufferedReader(new FileReader(volledig_path_immt));

                     while ((record = in.readLine()) != null)
                     {
                        latest_immt_record = record; 
                     }
                     in.close();
                  }
                  catch (IOException ex) { }
               } // if (immt_file.exists() && immt_file.length() > 0)            
            
            
               return latest_immt_record;

            } // protected Void doInBackground() throws Exception
         
            @Override
            protected void done()
            {
               try
               {
                  String latest_dashboard_obs = get();
                  set_latest_obs_values(latest_dashboard_obs);
                  
               } // try
               catch (InterruptedException | ExecutionException ex) 
               { 
                  System.out.println("+++ Error in Function: initComponents2() [DASHBOARD_latest_obs.java] " + ex);
               }
            } // protected void done()
         
         }.execute(); // new SwingWorker<Void, Void>()      
      } //  if (doorgaan)
      
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void set_latest_obs_values(String latest_obs)
   {
      // called from: initComponents2() [DASHBOARD_latest_obs.java]
      
      String month_full           = "";
      String latitude_hemisphere  = "";
      String longitude_hemisphere = "";
      String wind_units           = "";
      String true_wind_dir_hulp   = "";
      
      
      if (latest_obs.length() > 70)
      {
         // see IMMT description
         String year              = latest_obs.substring(1, 5);              
         String month             = latest_obs.substring(5, 7);              
         String day               = latest_obs.substring(7, 9);              
         String hour              = latest_obs.substring(9, 11);            
      
         String quadrant          = latest_obs.substring(11, 12);            // WMO code table 3333
         String latitude          = latest_obs.substring(12, 15);            // tenths of degrees
         String longitude         = latest_obs.substring(15, 19);            // tenths of degrees
         
         String pressure_MSL      = latest_obs.substring(37, 41);
         String air_temp_sign     = latest_obs.substring(29, 30);   
         String air_temp          = latest_obs.substring(30, 33);
         String sst_sign          = latest_obs.substring(49, 50);           // WMO code table 3845
         String sst               = latest_obs.substring(50, 53);
         String wind_units_ind    = latest_obs.substring(26, 27);           // WMO code table 1855
         String true_wind_dir     = latest_obs.substring(24, 26);           // WMO code table 0877
         String true_wind_speed   = latest_obs.substring(27, 29);
         String wind_waves_period = latest_obs.substring(55, 57);
         String wind_waves_height = latest_obs.substring(57, 59);
         String swell_1_dir       = latest_obs.substring(59, 61);
         String swell_1_period    = latest_obs.substring(61, 63);
         String swell_1_height    = latest_obs.substring(63, 65);
         
         //////////// date time
         //
         switch (month)
         {
            case "01" : month_full = "Jan"; break; 
            case "02" : month_full = "Feb"; break; 
            case "03" : month_full = "Mar"; break;  
            case "04" : month_full = "Apr"; break;     
            case "05" : month_full = "May"; break; 
            case "06" : month_full = "Jun"; break; 
            case "07" : month_full = "Jul"; break;  
            case "08" : month_full = "Aug"; break;     
            case "09" : month_full = "Sep"; break; 
            case "10" : month_full = "Oct"; break; 
            case "11" : month_full = "nov"; break;  
            case "12" : month_full = "Dec"; break;     
            default:  month_full = month; break;   
         } // switch (month)
      
         jLabel10.setText(day + "-" + month_full + "-" + year + " " + hour + ":00 UTC"); 
         
         switch (quadrant)
         {
            case "1" : latitude_hemisphere = "N";
                       longitude_hemisphere = "E";
                       break;
            case "3" : latitude_hemisphere = "S";
                       longitude_hemisphere = "E";
                       break;
            case "5" : latitude_hemisphere = "S";
                       longitude_hemisphere = "W";
                       break;
            case "7" : latitude_hemisphere = "N";
                       longitude_hemisphere = "W";
                       break;
            default :  latitude_hemisphere = "?";
                       longitude_hemisphere = "?";
                       break;
         } // switch (quadrant)
      
         
         ///////// position
         //
         String lat = latitude.substring(0, 2) + "." + latitude.substring(2, latitude.length()) + " " + latitude_hemisphere;
         String lon = longitude.substring(0, 3) + "." + longitude.substring(3, longitude.length()) + " " + longitude_hemisphere;
         
         jLabel11.setText(lat + " / " + lon);
         
         
         //////// pressure MSL
         //
         if (!" ".equals(pressure_MSL.substring(0, 1)))
         {
            // IMMT: 991.7 hPa -> 9917
            //       1007.8 hPa -> 0078 
            try
            {
               int int_pressure_MSL = Integer.parseInt(pressure_MSL);
               if (int_pressure_MSL > 8000)
               {
                  jLabel12.setText(pressure_MSL.substring(0, 3) + "." + pressure_MSL.substring(3, pressure_MSL.length()) + " hPa");
               }
               else
               {
                  jLabel12.setText("1" + pressure_MSL.substring(0, 3) + "." + pressure_MSL.substring(3, pressure_MSL.length()) + " hPa");
               }
            }
            catch (NumberFormatException e)
            {
               jLabel12.setText("-");        
            }
         }
         
         
         ///////// air temp
         //
         if ( (!" ".equals(air_temp_sign)) && (!" ".equals(air_temp.substring(0, 1))) )
         {
            String air_temp_string = "";
            
            // Sn 0 = positive or zero
            //    1 = negative
            if (air_temp_sign.equals("1"))
            {
               air_temp_string = "-";
            }       
               
            if (air_temp.substring(0, 1).equals("0"))
            {
               air_temp_string += air_temp.substring(1, 2) + "." + air_temp.substring(2, air_temp.length());// + " °C";
            }
            else
            {
               air_temp_string += air_temp.substring(0, 2) + "." + air_temp.substring(2, air_temp.length());// + " °C";
            }
            
            // only in case of US add the air temp in F
            if (main.recruiting_country.contains("UNITED STATES US"))
            {
               DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
               double double_celcius = Double.parseDouble(air_temp_string);
               double double_fahrenheit = (double_celcius * 1.8) + 32.0;
               String string_fahrenheit = df.format(double_fahrenheit);
               string_fahrenheit = string_fahrenheit.replace(",", ".");   // if necessary replace "," by "." (e.g. if country is Netherlands see Windows settings
                  
               air_temp_string += " °C" + " / " +  string_fahrenheit +  " °F";
            } // if (main.recruiting_country.contains("UNITED STATES US"))
            else
            {
               air_temp_string += " °C";   
            }
            
            jLabel13.setText(air_temp_string);
            
         } // if ( (!" ".equals(air_temp_sign)) && (!" ".equals(air_temp.substring(0, 1))) )
         
         
         /////////// SST
         //
         if ( (!" ".equals(sst_sign)) && (!" ".equals(sst.substring(0, 1))) )
         {
            String sst_string = "";
            
            // sst sign 0 = positive or zero
            //          1 = neative
            if (sst_sign.equals("1"))
            {
               sst_string = "-";
            }
            
            if (sst.substring(0, 1).equals("0"))
            {
               // not the extra leading zero
               sst_string += sst.substring(1, 2) + "." + sst.substring(2, sst.length());
            }
            else
            {
               sst_string += sst.substring(0, 2) + "." + sst.substring(2, sst.length()); 
            }
            
            // only in the case of US add the sst in F
            if (main.recruiting_country.contains("UNITED STATES US"))
            {
               DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
               double double_celcius = Double.parseDouble(sst_string);
               double double_fahrenheit = (double_celcius * 1.8) + 32.0;
               String string_fahrenheit = df.format(double_fahrenheit);
               string_fahrenheit = string_fahrenheit.replace(",", ".");   // if necessary replace "," by "." (e.g. if country is Netherlands see Windows settings
                  
               sst_string += " °C" + " / " +  string_fahrenheit +  " °F";
            } // if (main.recruiting_country.contains("UNITED STATES US"))
            else
            {
               sst_string += " °C";   
            }
            
            jLabel14.setText(sst_string);
            
         } // if ( (!" ".equals(sst_sign)) && (!" ".equals(sst.substring(0, 1))) )  
            
          
         ////////// True Wind
         //
         if ( (!" ".equals(wind_units_ind)) && (!" ".equals(true_wind_dir.substring(0, 1))) && (!" ".equals(true_wind_speed.substring(0, 1))))
         {
            // wind_units_ind:
            // 0 = estimated m/s
            // 1 = measured m/s
            // 3 = estimated knots
            // 4 = measured knots
            switch (wind_units_ind)
            {
               case "0" : wind_units = "m/s"; break;
               case "1" : wind_units = "m/s"; break;   
               case "3" : wind_units = "kts"; break; 
               case "4" : wind_units = "kts"; break;   
               default :  wind_units = "?"; break;  
            }
                 
            if (true_wind_dir.equals("99"))
            {
               true_wind_dir_hulp = "var";
            }
            else
            {
               true_wind_dir_hulp = true_wind_dir + "0";           // eg 34 immt-code -> 340°
            }
            
            jLabel15.setText(true_wind_dir_hulp + "°" + " / " + true_wind_speed + " " + wind_units);
            
         } // if ( (!" ".equals(wind_units_ind)) && (!" ".equals(true_wind_dir)) && (!" ".equals(true_wind_speed)))
         
         
         ////////// Wind Waves
         //
         if ( (!" ".equals(wind_waves_period.substring(0, 1))) && (!" ".equals(wind_waves_height.substring(0, 1))) )
         {
            if (wind_waves_period.equals("99"))
            {
               wind_waves_period = "confused";
            }
             
            try
            {
               double double_wind_waves_height = Double.parseDouble(wind_waves_height); 
               wind_waves_height = String.format("%.1f", double_wind_waves_height / 2.0);  // one digit behind the decimal point
            } 
            catch (NumberFormatException e)
            {
               wind_waves_height = "-";  
            }
             
            jLabel16.setText(wind_waves_period + " sec / " + wind_waves_height + " m");
             
         } // if ( (!" ".equals(wind_waves_period)) && (!" ".equals(wind_waves_height)) )
            
         
         ////////// predominant Swell
         //
         if ( (!" ".equals(swell_1_dir.substring(0, 1))) && (!" ".equals(swell_1_period.substring(0, 1))) && (!" ".equals(swell_1_height.substring(0, 1))))
         {
            if (swell_1_dir.equals("99"))
            {
               swell_1_dir = "-";
            }
            else
            {
               swell_1_dir = swell_1_dir + "0";           // eg 34 immt-code -> 340°
            }
            
            try
            {
               double double_swell_1_height = Double.parseDouble(swell_1_height); 
               swell_1_height = String.format("%.1f", double_swell_1_height / 2.0);  // one digit behind the decimal point
            } 
            catch (NumberFormatException e)
            {
               swell_1_height = "-";  
            }
            
            jLabel17.setText(swell_1_dir + "°" + " / " + swell_1_period + " sec" + " / " + swell_1_height + " m");
         } // if ( (!" ".equals(swell_1_dir)) && (!" ".equals(swell_1_period)) && (!" ".equals(swell_1_height)))
         
         
         
         ////////// screen color
         //
         
         // Date-time of invoking of this latest obs dashboard
         //jLabel18.setText(main.sdf_tsl_2.format(new Date()) + " UTC");   //  new Date() -> always in UTC
         
         // date time latest obs stored in immt log
         sdf_lo = new SimpleDateFormat("yyyyMMddHHmm");                  // HH hour in day (0-23) note there is also hh (then also with am, pm)
         sdf_lo.setTimeZone(TimeZone.getTimeZone("UTC"));

         Date record_date;
         try 
         {
            record_date = sdf_lo.parse(year + month + day + hour + "00");
            
            long system_sec = System.currentTimeMillis(); // the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
            long timeDiff = Math.abs(record_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes
            
            if (timeDiff <= 12 * 60)  // < 12 hours
            {
               jPanel1.setBackground(new Color(0, 255, 127));      // spring green (https://web.njit.edu/~kevin/rgb.txt.html)
            }
            else if ((timeDiff > 12 * 60) && (timeDiff < 24 * 60)) // 12 - 24 hours
            {
               jPanel1.setBackground(new Color(255, 255, 0));      // yellow1 (https://web.njit.edu/~kevin/rgb.txt.html)
            }
            else if (timeDiff >= 24 * 60) //>= 24 hours
            {
               jPanel1.setBackground(new Color(255, 99, 71));      // tomato (https://web.njit.edu/~kevin/rgb.txt.html)
            }
         } // try
         catch (ParseException ex) 
         {
            // do nothing
         } // catch
      } // if (latest_obs.length() > 10)
      
      
      // Date-time of invoking of this latest obs dashboard
      jLabel18.setText(main.sdf_tsl_2.format(new Date()) + " UTC");   //  new Date() -> always in UTC
   }

   
   
   /**
    * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form
    * Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents()
   {

      jPanel1 = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      jLabel2 = new javax.swing.JLabel();
      jLabel10 = new javax.swing.JLabel();
      jLabel11 = new javax.swing.JLabel();
      jLabel12 = new javax.swing.JLabel();
      jLabel13 = new javax.swing.JLabel();
      jLabel14 = new javax.swing.JLabel();
      jLabel15 = new javax.swing.JLabel();
      jLabel16 = new javax.swing.JLabel();
      jLabel17 = new javax.swing.JLabel();
      jLabel4 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      jLabel5 = new javax.swing.JLabel();
      jLabel6 = new javax.swing.JLabel();
      jLabel7 = new javax.swing.JLabel();
      jLabel8 = new javax.swing.JLabel();
      jButton1 = new javax.swing.JButton();
      jLabel9 = new javax.swing.JLabel();
      jLabel18 = new javax.swing.JLabel();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle("Latest Observation");
      setMaximumSize(new java.awt.Dimension(400, 300));
      setMinimumSize(new java.awt.Dimension(400, 300));
      setPreferredSize(new java.awt.Dimension(400, 300));
      setResizable(false);

      jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

      jLabel1.setText("Date/Time: ");

      jLabel2.setText("Position:");

      jLabel10.setText("no data");

      jLabel11.setText("no data");

      jLabel12.setText("no data");

      jLabel13.setText("no data");

      jLabel14.setText("no data");

      jLabel15.setText("no data");

      jLabel16.setText("no data");

      jLabel17.setText("no data");

      jLabel4.setText("Air temp:");

      jLabel3.setText("Pressure MSL:");

      jLabel5.setText("SST:");

      jLabel6.setText("True wind:");

      jLabel7.setText("Wind waves:");

      jLabel8.setText("Swell predominant:");

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
               .addGroup(jPanel1Layout.createSequentialGroup()
                  .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGap(18, 18, 18)
                  .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                     .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                     .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel1)
               .addComponent(jLabel10))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel2)
               .addComponent(jLabel11))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel3)
               .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel4)
               .addComponent(jLabel13))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel5)
               .addComponent(jLabel14))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel6)
               .addComponent(jLabel15))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel7)
               .addComponent(jLabel16))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel8)
               .addComponent(jLabel17))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jButton1.setText("OK");
      jButton1.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            OK_button_actionPerformed(evt);
         }
      });

      jLabel9.setText("Date/Time select: ");

      jLabel18.setText("no data");

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap(24, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                  .addGap(10, 10, 10)
                  .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap(22, Short.MAX_VALUE))
         .addGroup(layout.createSequentialGroup()
            .addGap(160, 160, 160)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel9)
               .addComponent(jLabel18))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(12, 12, 12))
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
      setVisible(false);
      dispose();
   }//GEN-LAST:event_OK_button_actionPerformed

   
   
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
         java.util.logging.Logger.getLogger(DASHBOARD_latest_obs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (InstantiationException ex) {
         java.util.logging.Logger.getLogger(DASHBOARD_latest_obs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (IllegalAccessException ex) {
         java.util.logging.Logger.getLogger(DASHBOARD_latest_obs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      } catch (javax.swing.UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(DASHBOARD_latest_obs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
        //</editor-fold>
        //</editor-fold>

      /* Create and display the form */
      java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
            new DASHBOARD_latest_obs().setVisible(true);
         }
      });
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton jButton1;
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
   // End of variables declaration//GEN-END:variables


  public static SimpleDateFormat sdf_lo;

}
