/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbowin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author marti
 */
public class DASHBOARD_grafiek_AWS_radar extends JPanel 
{

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public DASHBOARD_grafiek_AWS_radar()
   { 
      color_black                   = Color.BLACK;
      color_gray                    = Color.GRAY;
      color_inserted_data           = Color.BLUE;
      color_wind_rose_additional    = Color.YELLOW;  //Color.BLACK;             // BF circles + labels; NORTH,WEST,EAST,SOUTH chars
      color_wind_rose_background    = new Color(0, 191, 255, 255);              // light blue
      color_wind_rose_ring          = Color.DARK_GRAY;
      color_contour_block           = Color.BLACK;
      color_fill_block              = new Color(0, 191, 255);//new Color(0, 160, 255);//new Color(0, 191, 255);
      block_contour_thickness       = 2.0f;
      color_true_wind_arrow_max     = new Color(224, 224, 224, 120); 
      color_rel_wind_arrow_max      = new Color(224, 224, 224, 120); 
      color_true_wind_arrow_actual  = Color.YELLOW;
      color_rel_wind_arrow_actual   = new Color(255, 110, 0);//Color.ORANGE;
      
      // get the screen size 
      //
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      double width_screen = screenSize.getWidth();
      double height_screen = screenSize.getHeight();
      System.out.println("--- Screen resolution AWS Dashboard wind radar: " + width_screen + " x " + height_screen);   
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void setAllRenderingHints(Graphics2D g2d) 
   {
      g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
      g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      
      //RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
              
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
   }
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   @Override
   public void paintComponent(Graphics g) 
   {
      // eg: https://www.google.nl/search?q=barometer&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjJ78PZg8_UAhWEJMAKHZA3AsAQ_AUICigB&biw=1920&bih=950#imgrc=hE2asDauQrhr6M:
   
      // NB calculating instrument_width can't be done in DASHBOARD_grafiek_AWS_hybrid() because from here (paintComponents) it is only known
      //    see https://stackoverflow.com/questions/12010587/how-to-get-real-jpanel-size
  
      boolean draw_info_blocks = false;
      double x1_block          = 0;
      double y1_block          = 0;
      double x5_block          = 0;
      double y5_block          = 0;
      double block_width       = 0;
      double block_height      = 0;
      double wind_rose_radius  = 0;
      int beaufort_class       = Integer.MAX_VALUE;                                                 // for the correct background photo of the radar screen
      
      
      // always call the super's method to clean "dirty" pixels
      super.paintComponent(g);
   
      // general
      final Graphics2D g2d = (Graphics2D) g;
      setAllRenderingHints(g2d);
  
      // set new origin to middle screen
      g2d.translate(getWidth() / 2, getHeight() / 2);                                     
      

      int fontSize_a = 8;                                                                   // units (eg 1100) wind rose
      int fontSize_c = 12;                                                                  // Bf labels
      int fontSize_d = 18;
      int fontSize_h = 14;                                                                  // Bf descriptions
      int fontSize_e = DASHBOARD_view_AWS_radar.width_AWS_radar_dashboard / 70; 
      int dist_radar_blocks = DASHBOARD_view_AWS_radar.width_AWS_radar_dashboard / 70;      // distance between the info blocks and the radar screen
   
   
      font_a = new Font("SansSerif", Font.PLAIN, fontSize_a);                              // units (eg 1100)
      font_b = new Font("Monospaced", Font.BOLD, fontSize_d);                              // N, E, S, W
      font_c = new Font("SansSerif", Font.PLAIN, fontSize_c);                              // units (eg 1100)
      font_g = new Font("Monospaced", Font.BOLD, (int)(fontSize_e / 1.2));                    // labels and inserted data of the measured parameters
      //font_h = new Font("Monospaced", Font.PLAIN, fontSize_c);                             // Bf description (right info block)
      font_h = new Font("Monospaced", Font.BOLD, fontSize_h);                             // Bf description (right info block)


   /////////////////////////////////////////////////////////////////////////////////////////////////////////////
   // background image(not in night vision mode)
   //
   
   //if (DASHBOARD_view_AWS_radar.night_vision == false)
   //{
   //   //Image img1 = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + main.DASHBOARD_LOGO)).getImage();
   //   Image img1 = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "waves_desktop.png")).getImage();
   //   
   //   //scale the image to cover a the complete area of the drawing surface
   //   //g2d.drawImage(img1, 0, 0,getWidth(), getHeight(), 0, 0, img1.getWidth(null), img1.getHeight(null), null);
   //   int width = getWidth();  
   //   int height = getHeight();  
   //   for (int x = 0; x < width; x += img1.getWidth(null)) 
   //   {  
   //      for (int y = 0; y < height; y += img1.getHeight(null)) 
   //      {  
   //         g2d.drawImage(img1, x, y, this);  
   //      }  
   //   }       
   //} // if (DASHBOARD_view_AWS.night_vision == false)
  
   //setBackground(Color.LIGHT_GRAY);
   
      // colors depending night or day vision
      if (DASHBOARD_view_AWS_radar.night_vision == true)
      {
         //color_block_face = color_red;
         //color_contour_block = color_black;
         color_fill_block              = Color.RED;
         color_digits = color_black;
         color_last_update = color_black; //color_dark_red;   // " measured_at" in case hybrid / Meteo France dashboard
         color_update_message_south_panel = color_gray;
      }
      else // day vision
      {
         //color_block_face = new Color(179, 242, 255); 
         //color_contour_block = color_inserted_data;
         color_fill_block              = new Color(0, 191, 255);
         color_digits = color_inserted_data;
         color_last_update = color_black; //color_red;
         color_update_message_south_panel = color_black;
      }
   
      // obsolete data (no cummunication for roughly > 2 minutes with the AWS)
      if (main.displayed_aws_data_obsolate == true)   // valid for analog and digital dashboard
      {
         color_digits = main.obsolate_color_data_from_aws;                 // inserted data e.g. 12.3 C
         color_last_update = main.obsolate_color_data_from_aws;
         
         main_RS232_RS422.RS422_initialise_AWS_Sensor_Data_For_Display();  // NB see also: main_RS232_RS422.MAX_AGE_AWS_DATA
      }
      
      
      
   
      
      //////// measured: ///////
      //
      String measured_at_line = "";
      String MEASURED_AT = "Measured  :";
      String short_date = "";
      if (main.date_from_AWS_present && main.time_from_AWS_present)
      {
         if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("January"))
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("January", "Jan"); 
         }
         else if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("February"))
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("February", "Feb");
         }
         else if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("March"))
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("March", "Mar");
         }
         else if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("April"))
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("April", "Apr");        
         }     
         else if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("May"))
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("May", "May");
         }
         else if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("June"))
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("June", "Jun"); 
         }
         else if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("July"))
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("July", "Jul"); 
         }            
         else if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("August"))        
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("August", "Aug"); 
         }   
         else if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("September"))
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("September", "Sep");     
         }
         else if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("October"))
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("October", "Oct");     
         }
         else if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("November"))
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("November", "Nov");
         } 
         else if (main_RS232_RS422.dashboard_string_last_update_record_date.contains("December"))
         {
            short_date = main_RS232_RS422.dashboard_string_last_update_record_date.replace("December", "Dec");
         }
            
         measured_at_line = /*MEASURED_AT  + " " +*/short_date + "  " + main_RS232_RS422.dashboard_string_last_update_record_time + " UTC";
      }
      else
      {
         measured_at_line = MEASURED_AT  + " -";
      }   
       
      
      
      //////// Heading /////
      //
      String heading_line = "Heading   : -";
      int reading_int_heading = Integer.MAX_VALUE;
      boolean reading_heading_ok = false;                                                     // for alligning of the ship in the wind rose
      if (main.true_heading_from_AWS_present)
      {
         String reading_heading = main_RS232_RS422.dashboard_string_last_update_record_heading;
      
         try
         {
            reading_int_heading = Integer.parseInt(reading_heading);   
         }
         catch (NumberFormatException ex)
         {
            reading_int_heading = Integer.MAX_VALUE;        
         }
        
         if (reading_int_heading >= 1 && reading_int_heading <= 360) 
         {
            heading_line = "Heading   : "  + reading_heading + "°";
            reading_heading_ok = true;
         }   
      } // if (main.true_heading_from_AWS_present)

      
      //// COG //////
      //
      String COG_line = "COG       : -";
      int reading_int_COG = Integer.MAX_VALUE;
      boolean reading_COG_ok = false;                                                     // for alligning of the ship in the wind rose
      if (main.COG_from_AWS_present)
      {
         String reading_COG = main_RS232_RS422.dashboard_string_last_update_record_COG;
      
         try
         {
            reading_int_COG = Integer.parseInt(reading_COG);   
         }
         catch (NumberFormatException ex)
         {
            reading_int_COG = Integer.MAX_VALUE;        
         }
        
         if (reading_int_COG >= 1 && reading_int_COG <= 360) 
         {
            COG_line = "COG       : "  + reading_COG + "°";
            reading_COG_ok = true;
         }   
      } // 
      
      
      //////// SOG /////
      //
      String SOG_line = "SOG       : -";
      double reading_double_SOG = Double.MAX_VALUE;
      if (main.SOG_from_AWS_present)
      {
         String reading_SOG = main_RS232_RS422.dashboard_string_last_update_record_SOG;
      
         try
         {
            reading_double_SOG = Double.parseDouble(reading_SOG);
         }
         catch (NumberFormatException ex)
         {
            reading_double_SOG = Integer.MAX_VALUE;        
         }
        
         if (reading_double_SOG >= 0.0 && reading_double_SOG <= 50.0) 
         {
            SOG_line = "SOG       : " + reading_SOG + " kts";
         }   
      } // if (main.SOG_from_AWS_present)

      
      //////// Lat /////
      //
      String lat_line = "Lat       : -";
      if (main.latitude_from_AWS_present)
      {
         String reading_latitude = main_RS232_RS422.dashboard_string_last_update_record_latitude;   
      
         int pos = reading_latitude.indexOf("°", 0);
      
         int reading_int_latitude_degrees = Integer.MAX_VALUE;
         try
         {
            reading_int_latitude_degrees = Integer.parseInt(reading_latitude.substring(0, pos));   // eg 42°12'N ->  reading_latitude_degrees = 42
         }
         catch (NumberFormatException ex)
         {
            reading_int_latitude_degrees = Integer.MAX_VALUE;        
         }
      
         if (reading_int_latitude_degrees >= 0 && reading_int_latitude_degrees <= 90)
         {
            reading_latitude = reading_latitude.replaceAll("\\s","");            // skip all the spaces in the latitude string
            lat_line = "Lat       : "  + reading_latitude + "°";
         }
      }       
      
      
      //////// Lon /////
      //
      String lon_line = "Lon       : -";
      if (main.longitude_from_AWS_present)
      {
         String reading_longitude = main_RS232_RS422.dashboard_string_last_update_record_longitude;   
      
         int pos = reading_longitude.indexOf("°", 0);
      
         int reading_int_longitude_degrees = Integer.MAX_VALUE;
         try
         {
            reading_int_longitude_degrees = Integer.parseInt(reading_longitude.substring(0, pos));   // eg 42°12'N ->  reading_latitude_degrees = 42
         }
         catch (NumberFormatException ex)
         {
            reading_int_longitude_degrees = Integer.MAX_VALUE;        
         }
      
         if (reading_int_longitude_degrees >= 0 && reading_int_longitude_degrees <= 180)
         {
            reading_longitude = reading_longitude.replaceAll("\\s","");            // skip all the spaces in the latitude string
            lon_line = "Lon       : "  + reading_longitude + "°";
         }
      } // if (main.longitude_from_AWS_present)       
      
      
      ////////// MSLP //////
      //
      String mslp_line = "MSLP      : -";
      if (main.pressure_MSL_from_AWS_present)
      {
         double reading_mslp = main_RS232_RS422.dashboard_double_last_update_record_MSL_pressure_ic;         // see: RS422_update_AWS_dasboard_values()
         if (reading_mslp > 900.0 && reading_mslp < 1100.0)
         {
            mslp_line = "MSLP      : " + Double.toString(reading_mslp) + " hPa";
         }
      }
      
      
      //////// air pressure tendency (and charactristic) ////////
      //
      String tendency_line = "Tendency  : -";
      if (main.pressure_tendency_from_AWS_present)
      {
         double tendency_reading = main_RS232_RS422.dashboard_double_last_update_record_pressure_tendency; // see: RS422_update_AWS_dasboard_values()
         if (tendency_reading >= -99.9 && tendency_reading <= 99.9)
         {
            tendency_line = "Tendency  : " + Double.toString(tendency_reading) + " hPa / 3 hrs";
         }
      } 
      
      
      //////// air pressire at sensor height ('read') /////
      //
      String pressure_height_line = "Press read: -";
      if (main.pressure_sensor_level_from_AWS_present)
      {
         double reading_pressure_at_height = main_RS232_RS422.dashboard_double_last_update_record_sensor_level_pressure_ic; // see: main_RS232_RS422.RS422_update_AWS_dasboard_values()
         if (reading_pressure_at_height > 900.0 && reading_pressure_at_height < 1100.0)
         {
            pressure_height_line = "Press read: " + Double.toString(reading_pressure_at_height) + " hPa";
         }
      }
      
      
      ///////// air temp ///////
      //
      String air_temp_line = "Air temp  : -";
      if (main.air_temp_from_AWS_present)
      {
         double air_temp_reading = main_RS232_RS422.dashboard_double_last_update_record_air_temp;      // see: RS422_update_AWS_dasboard_values()
         if (air_temp_reading > -60.0 && air_temp_reading < 60.0) 
         {
            air_temp_line = "Air temp  : " + Double.toString(air_temp_reading) + " °C";
         }   
      } // if (main.air_temp_from_AWS_present)
      
      
      /////// relative humidity (RH) /////////
      //
      String rh_line = "RH        : -";
      if (main.rh_from_AWS_present)
      {
         double rh_reading = main_RS232_RS422.dashboard_double_last_update_record_humidity;
         if (rh_reading >= 0.0 && rh_reading <= 105.0)
         {
            rh_line = "RH        : " + Double.toString(rh_reading) + " %";
         }
      } 
      
      
      /////////// SST /////////
      //
      String sst_line = "SST       : -";
      if (main.SST_from_AWS_present)
      {
         double sst_reading = main_RS232_RS422.dashboard_double_last_update_record_sst;
         if (sst_reading > -60.0 && sst_reading < 60.0) 
         {
            sst_line = "SST       : " + Double.toString(sst_reading) + " °C";
         }   
      } // if (main.SST_from_AWS_present)
   
   
      //////////  visual obs message based on VOT ////////
      //
      String visual_obs_line = "Visual obs: -";
      if (main.VOT_from_AWS_present)
      {
         if (main_RS232_RS422.dashboard_int_last_update_record_VOT >= 0 && main_RS232_RS422.dashboard_int_last_update_record_VOT < 100)
         {
            visual_obs_line = "Visual obs: now possible";
            DASHBOARD_view_AWS_radar.jButton1.setEnabled(true);          // button: "make an visual observation"
         }
         else if (main_RS232_RS422.dashboard_int_last_update_record_VOT < 0)
         {
            visual_obs_line = "Visual obs: in " + Integer.toString(Math.abs(main_RS232_RS422.dashboard_int_last_update_record_VOT)) + " minutes";
            DASHBOARD_view_AWS_radar.jButton1.setEnabled(false);
         }
         else // VOT >= 100 (eg harbour mode)
         {
            DASHBOARD_view_AWS_radar.jButton1.setEnabled(false);
         }
      } // if (main.VOT_from_AWS_present)
      else
      {
         DASHBOARD_view_AWS_radar.jButton1.setEnabled(false);
      }  
      
       ///////// height anemometer as text //////////////////////////////
       //
      String txt_height_anemometer = "";
      try
      {
         int int_height_anemometer = Integer.parseInt(main.height_anemometer);
         if (int_height_anemometer >= 0 && int_height_anemometer <= 200)
         {
            txt_height_anemometer = main.height_anemometer;
         }
         else
         {
            txt_height_anemometer = "??";
         }
      } // try
      catch (NumberFormatException e)
      {
         txt_height_anemometer = "??";
      } // catch
      
      
      ///////// wind relative at sensor height (e.g. "Wind rel at ??m: 100° / 12 kts") ///////
      //
      String wind_rel_sub_speed_line = "-";
      if (main.relative_wind_speed_from_AWS_present)
      {
         int relative_wind_speed_reading = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_speed;
         if (relative_wind_speed_reading >= 0.0 && relative_wind_speed_reading <= 400.0) 
         {
            // so the digit indication is up to 400 kts/m/s and the analogue up to 110 kts/m/s
            if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)               // units is knots
            {
               wind_rel_sub_speed_line = Integer.toString(relative_wind_speed_reading) + " kts";
            }
            else                                                                   // units is m/s                                                     
            {
               wind_rel_sub_speed_line = Integer.toString(relative_wind_speed_reading) + " m/s";
            }
         }
      } //if (main.relative_wind_speed_from_AWS_present)
      
      String wind_rel_sub_dir_line = "-";
      if (main.relative_wind_dir_from_AWS_present)
      {
         int relative_wind_dir_reading = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_dir;
         if (relative_wind_dir_reading >= 1 && relative_wind_dir_reading <= 360)       // dir = 0 should be impossible 
         {
            String hulp_dir = Integer.toString(relative_wind_dir_reading);
            
            // always 3 char for wind direction
            if (hulp_dir.length() == 1)
            {
               hulp_dir = "00" + hulp_dir;
            }
            else if (hulp_dir.length() == 2)
            {
               hulp_dir = "0" + hulp_dir;
            }
            
            //wind_rel_sub_dir_line = Integer.toString(relative_wind_dir_reading) + "°";
            wind_rel_sub_dir_line = hulp_dir + "°";
         }
      } //if (main.relative_wind_speed_from_AWS_present)
       
      //String wind_rel_line_sensor_height = "REL at ??m*  : " + wind_rel_sub_dir_line + " / " + wind_rel_sub_speed_line;
      String wind_rel_line_sensor_height = "REL  at " + txt_height_anemometer + "m* : " + wind_rel_sub_dir_line + " / " + wind_rel_sub_speed_line;
      
 
      ///////// wind true at sensor height (e.g. "Wind true at ??m: 100° / 12 kts") ///////
      //
      String wind_true_sub_speed_line = "-";
      if (main.true_wind_speed_from_AWS_present)
      {
         int true_wind_speed_reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_speed;
         if (true_wind_speed_reading >= 0.0 && true_wind_speed_reading <= 400.0) 
         {
            // so the digit indication is up to 400 kts/m/s and the analogue up to 110 kts/m/s
            if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)               // units is knots
            {
               wind_true_sub_speed_line = Integer.toString(true_wind_speed_reading) + " kts";
               //beaufort_class = convert_knots_to_bf(true_wind_speed_reading);
            }
            else                                                                   // units is m/s                                                     
            {
               wind_true_sub_speed_line = Integer.toString(true_wind_speed_reading) + " m/s";
               //beaufort_class = convert_ms_to_bf(true_wind_speed_reading);
            }
         }
      } //if (main.true_wind_speed_from_AWS_present)
      
      String wind_true_sub_dir_line = "-";
      if (main.true_wind_dir_from_AWS_present)
      {
         int true_wind_dir_reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_dir;
         if (true_wind_dir_reading >= 1 && true_wind_dir_reading <= 360)       // dir = 0 should be impossible 
         {
            String hulp_dir = Integer.toString(true_wind_dir_reading);
            
            // always 3 char for wind direction
            if (hulp_dir.length() == 1)
            {
               hulp_dir = "00" + hulp_dir;
            }
            else if (hulp_dir.length() == 2)
            {
               hulp_dir = "0" + hulp_dir;
            }
            
            //wind_true_sub_dir_line = Integer.toString(true_wind_dir_reading) + "°";
            wind_true_sub_dir_line = hulp_dir + "°";
         }
      } //if (main.true_wind_speed_from_AWS_present)
       
      //String wind_true_line_sensor_height = "True at ??m* : " + wind_true_sub_dir_line + " / " + wind_true_sub_speed_line;
      String wind_true_line_sensor_height = "True at " + txt_height_anemometer + "m* : " + wind_true_sub_dir_line + " / " + wind_true_sub_speed_line;
      
      
      ///////// wind gust true at sensor height ////// e.g. "Wind gust at??m: 100° / 12 kts"
      //
      String wind_gust_sub_speed_line = "-";
      if (main.true_wind_gust_from_AWS_present)
      {
         int wind_gust_speed_reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_gust;
         if (wind_gust_speed_reading >= 0.0 && wind_gust_speed_reading <= 400.0) 
         {
            // so the digit indication is up to 400 kts/m/s and the analogue up to 110 kts/m/s
            if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)               // units is knots
            {
               wind_gust_sub_speed_line = Integer.toString(wind_gust_speed_reading) + " kts";
            }
            else                                                                   // units is m/s                                                     
            {
               wind_gust_sub_speed_line = Integer.toString(wind_gust_speed_reading) + " m/s";
            }
         }
      } //if (main.true_wind_speed_from_AWS_present)
      
      String wind_gust_sub_dir_line = "-";
      if (main.true_wind_gust_dir_from_AWS_present)
      {
         int wind_gust_dir_reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_gust_dir;
         if (wind_gust_dir_reading >= 1 && wind_gust_dir_reading <= 360)       // dir = 0 should be impossible 
         {
            String hulp_dir = Integer.toString(wind_gust_dir_reading);
            
            // always 3 char for wind direction
            if (hulp_dir.length() == 1)
            {
               hulp_dir = "00" + hulp_dir;
            }
            else if (hulp_dir.length() == 2)
            {
               hulp_dir = "0" + hulp_dir;
            }
            
            //wind_gust_sub_dir_line = Integer.toString(wind_gust_dir_reading) + "°";
            wind_gust_sub_dir_line = hulp_dir + "°";
         }
      } // if (main.true_wind_gust_dir_from_AWS_present)
       
      //String wind_gust_line_sensor_height = "Gust at ??m* : " + wind_gust_sub_dir_line + " / " + wind_gust_sub_speed_line;
      String wind_gust_line_sensor_height = "Gust at " + txt_height_anemometer + "m* : " + wind_gust_sub_dir_line + " / " + wind_gust_sub_speed_line;
      
      
      ///////// wind relative at 10 m (e.g. "Wind rel at 10m: 100° / 12 kts") ///////
      //
      String wind_rel_sub_speed_line_10m = "-";
      if (main.relative_wind_speed_from_AWS_present)
      {
         int relative_wind_speed_reading_10m = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_speed_10m;
         if (relative_wind_speed_reading_10m >= 0.0 && relative_wind_speed_reading_10m <= 400.0) 
         {
            // so the digit indication is up to 400 kts/m/s and the analogue up to 110 kts/m/s
            if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)               // units is knots
            {
               wind_rel_sub_speed_line_10m = Integer.toString(relative_wind_speed_reading_10m) + " kts";
            }
            else                                                                   // units is m/s                                                     
            {
               wind_rel_sub_speed_line_10m = Integer.toString(relative_wind_speed_reading_10m) + " m/s";
            }
         }
      } 
      
      String wind_rel_sub_dir_line_10m = "-";
      if (main.relative_wind_dir_from_AWS_present)
      {
         // NB wind dir at 10m height is excactly the same as at sensor height !!
         int relative_wind_dir_reading_10m = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_dir;
         if (relative_wind_dir_reading_10m >= 1 && relative_wind_dir_reading_10m <= 360)       // dir = 0 should be impossible 
         {
            String hulp_dir = Integer.toString(relative_wind_dir_reading_10m);
            
            // always 3 char for wind direction
            if (hulp_dir.length() == 1)
            {
               hulp_dir = "00" + hulp_dir;
            }
            else if (hulp_dir.length() == 2)
            {
               hulp_dir = "0" + hulp_dir;
            }
            
            //wind_rel_sub_dir_line_10m = Integer.toString(relative_wind_dir_reading_10m) + "°";
            wind_rel_sub_dir_line_10m = hulp_dir + "°";
         }
      } //if (main.relative_wind_speed_from_AWS_present)
       
      String wind_relative_line_10m = "REL  at 10m**: " + wind_rel_sub_dir_line_10m + " / " + wind_rel_sub_speed_line_10m;      
      
      
      
      ///////// wind true at 10 m (e.g. "Wind true at 10m: 100° / 12 kts") ///////
      //
      String wind_true_sub_speed_line_10m = "-";
      if (main.true_wind_speed_from_AWS_present)
      {
         int true_wind_speed_reading_10m = main_RS232_RS422.dashboard_int_last_update_record_true_wind_speed_10m;
         if (true_wind_speed_reading_10m >= 0.0 && true_wind_speed_reading_10m <= 400.0) 
         {
            // so the digit indication is up to 400 kts/m/s and the analogue up to 110 kts/m/s
            if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)               // units is knots
            {
               wind_true_sub_speed_line_10m = Integer.toString(true_wind_speed_reading_10m) + " kts";
               beaufort_class = convert_knots_to_bf(true_wind_speed_reading_10m);
            }
            else                                                                   // units is m/s                                                     
            {
               wind_true_sub_speed_line_10m = Integer.toString(true_wind_speed_reading_10m) + " m/s";
               beaufort_class = convert_ms_to_bf(true_wind_speed_reading_10m);
            }
         }
      } //if (main.true_wind_speed_from_AWS_present)
      
      String wind_true_sub_dir_line_10m = "-";
      if (main.true_wind_dir_from_AWS_present)
      {
         // NB wind dir at 10m height is excactly the same as at sensor height !!
         int true_wind_dir_reading_10m = main_RS232_RS422.dashboard_int_last_update_record_true_wind_dir;
         if (true_wind_dir_reading_10m >= 1 && true_wind_dir_reading_10m <= 360)       // dir = 0 should be impossible 
         {
            String hulp_dir = Integer.toString(true_wind_dir_reading_10m);
            
            // always 3 char for wind direction
            if (hulp_dir.length() == 1)
            {
               hulp_dir = "00" + hulp_dir;
            }
            else if (hulp_dir.length() == 2)
            {
               hulp_dir = "0" + hulp_dir;
            }
            
            wind_true_sub_dir_line_10m = hulp_dir + "°";
            //wind_true_sub_dir_line_10m = Integer.toString(true_wind_dir_reading_10m) + "°";
         }
      } //if (main.true_wind_speed_from_AWS_present)
       
      String wind_true_line_10m = "True at 10m**: " + wind_true_sub_dir_line_10m + " / " + wind_true_sub_speed_line_10m;      
      
      
      
      ///////// wind gust true at 10m ////// e.g. "Wind gust at 10m: 100° / 12 kts"
      //
      String wind_gust_sub_speed_line_10m = "-";
      if (main.true_wind_gust_from_AWS_present)
      {
         int true_wind_gust_reading_10m = main_RS232_RS422.dashboard_int_last_update_record_true_wind_gust_10m;
         if (true_wind_gust_reading_10m >= 0.0 && true_wind_gust_reading_10m <= 400.0) 
         {
            // so the digit indication is up to 400 kts/m/s and the analogue up to 110 kts/m/s
            if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)               // units is knots
            {
               wind_gust_sub_speed_line_10m = Integer.toString(true_wind_gust_reading_10m) + " kts";
            }
            else                                                                   // units is m/s                                                     
            {
               wind_gust_sub_speed_line_10m = Integer.toString(true_wind_gust_reading_10m) + " m/s";
            }
         }
      } //if (main.true_wind_gust_from_AWS_present)
      
      String wind_gust_sub_dir_line_10m = "-";
      if (main.true_wind_gust_dir_from_AWS_present)
      {
         // NB wind dir at 10m height is excactly the same as at sensor height !!
         int true_wind_gust_dir_reading_10m = main_RS232_RS422.dashboard_int_last_update_record_true_wind_gust_dir;
         if (true_wind_gust_dir_reading_10m >= 1 && true_wind_gust_dir_reading_10m <= 360)       // dir = 0 should be impossible 
         {
            String hulp_dir = Integer.toString(true_wind_gust_dir_reading_10m);
            
            // always 3 char for wind direction
            if (hulp_dir.length() == 1)
            {
               hulp_dir = "00" + hulp_dir;
            }
            else if (hulp_dir.length() == 2)
            {
               hulp_dir = "0" + hulp_dir;
            }
            
            //wind_gust_sub_dir_line_10m = Integer.toString(true_wind_gust_dir_reading_10m) + "°";
            wind_gust_sub_dir_line_10m = hulp_dir + "°";
         }
      } // if (main.true_wind_gust_dir_from_AWS_present)
       
      String wind_gust_line_10m = "Gust at 10m**: " + wind_gust_sub_dir_line_10m + " / " + wind_gust_sub_speed_line_10m;            
      
      
      
      
      //////////// wind rose /////////
      //
      double wind_rose_diameter = 0.0;
      if (DASHBOARD_view_AWS_radar.width_AWS_radar_dashboard < DASHBOARD_view_AWS_radar.height_AWS_radar_dashboard)
      {
         wind_rose_diameter = DASHBOARD_view_AWS_radar.width_AWS_radar_dashboard / 1.1;   // nb width from JPanel1 (center panel)
      }
      else
      {
         wind_rose_diameter = DASHBOARD_view_AWS_radar.height_AWS_radar_dashboard / 1.1;  // nb height from JPanel1 (center panel)
      }
   
      // for wind rose and wind arrow
      double marker_circle_diameter_1   = wind_rose_diameter - 26;                  // eg outside marker circle: 220 - 26 = 194; [most 'outside' outside marker circle]
      double marker_circle_diameter_2   = marker_circle_diameter_1 - 10;            // eg inside marker circle: 194 - 10 = 184; [most 'inside' outside marker circle]
      double bf_per_class_radius = (marker_circle_diameter_2 / 2) / 12;
   
      // wind rose itself
      draw_wind_rose_radar(g2d, wind_rose_diameter, marker_circle_diameter_1, marker_circle_diameter_2, bf_per_class_radius, beaufort_class);      
      
      
      /////// ship in wind rose //////
      //
      int reading_int_course = Integer.MAX_VALUE;
      boolean course_ok = false;
   
      if (reading_heading_ok)
      {
         reading_int_course = reading_int_heading;
         course_ok = true;
      }
      else // heading not ok/present so now try COG
      {
         if (reading_COG_ok)
         {
            reading_int_course = reading_int_COG;
            course_ok = true;
         }
      } // else     
     
      
      
////////////////////////////////////////////////// BEGIN TESTING //////////////////////////////
      //reading_int_course = 33;
      //course_ok = true;
      //main.ship_type_dashboard = main.NEUTRAL_SHIP;
      //main.ship_type_dashboard = main.PASSENGER_SHIP;
      //main.ship_type_dashboard = main.OIL_TANKER;
      //main.ship_type_dashboard = main.CONTAINER_SHIP;
      //main.ship_type_dashboard = main.LNG_TANKER;
      //main.ship_type_dashboard = main.RESEARCH_VESSEL;
      //main.ship_type_dashboard = main.RO_RO_SHIP;
      //main.ship_type_dashboard = main.FERRY;
 ////////////////////////////////////////////////// END TESTING ////////////////////////////////////////
 
 
  
      // if not yet done create object ship
      if (main.myship == null)
      {                                
         main.myship = new ship();
      }
   
      // if no (or not yet) ship type defined start with a container ship
      if (main.ship_type_dashboard.equals(""))
      {
         main.ship_type_dashboard = main.CONTAINER_SHIP;
      }
   
      if (course_ok)
      {
         // set new drawing orientation (rotated to heading or COG)
         if (reading_int_course >= 1 && reading_int_course <= 360) 
         {
            g2d.rotate(Math.toRadians(reading_int_course));  // what follows will be rotated xxx(reading_int_course) degrees
         }   
      
         switch (main.ship_type_dashboard)
         {
            case main.GENERAL_CARGO_SHIP : main.myship.draw_general_cargo_ship(g2d, wind_rose_diameter, DASHBOARD_view_AWS_radar.night_vision); break;
            case main.CONTAINER_SHIP     : main.myship.draw_container_ship(g2d, wind_rose_diameter, DASHBOARD_view_AWS_radar.night_vision); break;
            case main.BULK_CARRIER       : main.myship.draw_bulk_carrier(g2d, wind_rose_diameter, DASHBOARD_view_AWS_radar.night_vision); break;
            case main.OIL_TANKER         : main.myship.draw_oil_tanker(g2d, wind_rose_diameter, DASHBOARD_view_AWS_radar.night_vision); break;
            case main.LNG_TANKER         : main.myship.draw_lng_tanker(g2d, wind_rose_diameter, DASHBOARD_view_AWS_radar.night_vision); break;
            case main.PASSENGER_SHIP     : main.myship.draw_passenger_ship(g2d, wind_rose_diameter, DASHBOARD_view_AWS_radar.night_vision); break;
            case main.RESEARCH_VESSEL    : main.myship.draw_research_vessel(g2d, wind_rose_diameter, DASHBOARD_view_AWS_radar.night_vision); break;
            case main.NEUTRAL_SHIP       : main.myship.draw_neutral_ship(g2d, wind_rose_diameter, DASHBOARD_view_AWS_radar.night_vision); break;
            case main.RO_RO_SHIP         : main.myship.draw_ro_ro_ship(g2d, wind_rose_diameter, DASHBOARD_view_AWS_radar.night_vision); break;
            case main.FERRY              : main.myship.draw_ferry(g2d, wind_rose_diameter, DASHBOARD_view_AWS_radar.night_vision); break;
            default                      : main.myship.draw_container_ship(g2d, wind_rose_diameter, DASHBOARD_view_AWS_radar.night_vision); break;
         } // switch (main.ship_type_dashboard)
      
         // reset drawing orientation
         if (reading_int_course >= 1 && reading_int_course <= 360)    
         {
            g2d.rotate(Math.toRadians(-reading_int_course));  // what follows will be rotated -xxx degrees
         }
      
      } // if (course_ok)
      
      
      
      // declaration + initialisation for wind arrow
      String wind_type           = "";
      boolean wind_dir_present   = false;
      boolean wind_speed_present = false;
      int wind_dir               = 0;
      int wind_speed             = 0; 
      
      
      // NB most of the time if relative and true direction are almost the same (on the bow) then relative speed will be higher then true speed
      //   therfore first drawing the relative arrow and then the true arrow. Furtheremore the relative wind arrow is a little bit widther than the true wind arrow
      //
      //
      
      ////// relative wind arrows (before true wind arrow!) /////
      //
      wind_type           = RELATIVE_WIND;
      wind_dir_present   = main.relative_wind_dir_from_AWS_present;
      wind_speed_present = main.relative_wind_speed_from_AWS_present;
      wind_dir           = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_dir;        // NB wind dir at 10m height is excactly the same as at sensor height !!
      wind_speed         = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_speed_10m;  
      draw_wind_arrow_radar(g2d, marker_circle_diameter_2, bf_per_class_radius, wind_type, wind_dir_present, wind_speed_present, wind_dir, wind_speed);
      
      
      ////// True wind arrows (after relative wind arrow!) /////
      //
      wind_type           = TRUE_WIND;
      wind_dir_present   = main.true_wind_dir_from_AWS_present;
      wind_speed_present = main.true_wind_speed_from_AWS_present;
      wind_dir           = main_RS232_RS422.dashboard_int_last_update_record_true_wind_dir;            //  NB wind dir at 10m height is excactly the same as at sensor height !!
      wind_speed         = main_RS232_RS422.dashboard_int_last_update_record_true_wind_speed_10m;  
      draw_wind_arrow_radar(g2d, marker_circle_diameter_2, bf_per_class_radius, wind_type, wind_dir_present, wind_speed_present, wind_dir, wind_speed);
      
      
      
      //////////////// left block, radar circle, right block ///////////
      //
      //
      //
      //             (x1,y1)           (x2,y2                                   (x5,y5)           (x6,y6)
      //                    ------------                                               ------------
      //                   |           |                                               |          |
      //                   |           |                                               |          |
      //                   |           |                                               |          |
      //                   |           |                  radar circle                 |          |
      //                   |           |                                               |          |
      //                   |           |                                               |          |
      //                   |           |                                               |          |
      //                    -----------                                                -----------|
      //             (x3,y3)           (x4,y4)                                   (x7,y7)           (x8,y8)
      //
      //
      //System.out.println("+++ wind_rose_diameter * 2 = " + wind_rose_diameter * 2);
      //System.out.println("+++ DASHBOARD_view_AWS_radar.width_AWS_radar_dashboard = " + DASHBOARD_view_AWS_radar.width_AWS_radar_dashboard);
      draw_info_blocks = ((wind_rose_diameter * 2) + (dist_radar_blocks * 2)) < DASHBOARD_view_AWS_radar.width_AWS_radar_dashboard; // all of the center drawing panel (JPanel1)
         
      if (draw_info_blocks)   
      {
         wind_rose_radius = wind_rose_diameter / 2;
         x1_block = -wind_rose_radius - (wind_rose_radius) - dist_radar_blocks;
         y1_block = -wind_rose_radius;
         
         x5_block = wind_rose_radius + dist_radar_blocks;
         y5_block = -wind_rose_radius;
         
         block_width  = wind_rose_radius;        //Math.abs(x1_block) - Math.abs(x2_block);
         block_height = wind_rose_diameter;      // origin lies between y7 and y5
         
         // fill left and right block
         g2d.setPaint(color_fill_block);
         //g2d.setStroke(new BasicStroke(block_contour_thickness));
         g2d.fill(new RoundRectangle2D.Double(x1_block, y1_block, block_width, block_height, 20, 20));
         g2d.fill(new RoundRectangle2D.Double(x5_block, y5_block, block_width, block_height, 20, 20));
         
         // contour left and right block
         g2d.setPaint(color_contour_block);
         g2d.setStroke(new BasicStroke(block_contour_thickness));
         g2d.draw(new RoundRectangle2D.Double(x1_block, y1_block, block_width, block_height, 20, 20));
         g2d.draw(new RoundRectangle2D.Double(x5_block, y5_block, block_width, block_height, 20, 20));
      } // if (draw_info_blocks) 
      
     
    
      if (draw_info_blocks)
      {
         //g2d.setFont(font_f);
         g2d.setFont(font_g);
         g2d.setPaint(color_black);
         
         int height_letter = g2d.getFontMetrics().stringWidth("M");
         int x_line = 0;
         int y_line = 0;
         
         
         ////// left: --- AWS DATA --- //////
         //
         g2d.setPaint(color_black);
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 2;
         g2d.drawString("--- AWS DATA ---", x_line, y_line); 
         
         ////// left: measured: //////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 8;
         g2d.drawString(measured_at_line, x_line, y_line); 
    
         
         //////// left: Heading /////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 14;
         g2d.drawString(heading_line, x_line, y_line); 


         //////// left: COG /////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 16;
         g2d.drawString(COG_line, x_line, y_line); 
         
         
         ///////// left: SOG //////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 18;
         g2d.drawString(SOG_line, x_line, y_line); 
         
         
         ////////// left: Lat ////////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 20;
         g2d.drawString(lat_line, x_line, y_line); 
         
        
         ////////// left: Lon ////////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 22;
         g2d.drawString(lon_line, x_line, y_line); 
         
         
         ////////// left: MSLP ////////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 26;
         g2d.drawString(mslp_line, x_line, y_line); 
         
         
         ////////// left: tendency ////////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 28;
         g2d.drawString(tendency_line, x_line, y_line); 
         
         
         ////////// left: presure at sensor height ////////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 30;
         g2d.drawString(pressure_height_line, x_line, y_line); 
         
         
         ////////// left: Air temp ////////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 34;
         g2d.drawString(air_temp_line, x_line, y_line); 
         
         
         ////////// left: RH ////////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 36;
         g2d.drawString(rh_line, x_line, y_line); 
         
         
         ////////// left: SST ////////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 38;
         g2d.drawString(sst_line, x_line, y_line); 
         
         
         ////////// left: Visual obs ////////
         //
         x_line = (int)x1_block + height_letter;
         y_line = (int)y1_block + height_letter * 44;
         g2d.drawString(visual_obs_line, x_line, y_line);
         
         
         
         
         ////// right: --- WIND DATA --- //////
         //
         g2d.setPaint(color_black);
         x_line = (int)x5_block + height_letter;
         y_line = (int)y5_block + height_letter * 2;
         g2d.drawString("--- AWS WIND DATA ---", x_line, y_line); 
         
         ////// right: relative wind at sensor height: //////
         //
         g2d.setPaint(color_black);
         x_line = (int)x5_block + height_letter;
         y_line = (int)y5_block + height_letter * 8;
         g2d.drawString(wind_rel_line_sensor_height, x_line, y_line); 
         
         
         ////// right: true wind at sensor height: //////
         //
         g2d.setPaint(color_black);
         x_line = (int)x5_block + height_letter;
         y_line = (int)y5_block + height_letter * 10;
         g2d.drawString(wind_true_line_sensor_height, x_line, y_line); 
         
         
         ////// right: true wind at sensor height: //////
         //
         g2d.setPaint(color_black);
         x_line = (int)x5_block + height_letter;
         y_line = (int)y5_block + height_letter * 12;
         g2d.drawString(wind_gust_line_sensor_height, x_line, y_line); 
         
         
         ////// right: wind at sensor height note: //////
         //
         g2d.setFont(font_h);
         g2d.setPaint(color_black);
         x_line = (int)x5_block + height_letter;
         y_line = (int)y5_block + height_letter * 14;
         g2d.drawString("* anemometer height above water", x_line, y_line); 
         g2d.setFont(font_g);

         
         ////// right: relative wind at 10 m height: //////
         //
         g2d.setPaint(color_rel_wind_arrow_actual);
         x_line = (int)x5_block + height_letter;
         y_line = (int)y5_block + height_letter * 18;
         g2d.drawString(wind_relative_line_10m, x_line, y_line); 
         
         
         ////// right: true wind at 10m height: //////
         //
         g2d.setPaint(color_true_wind_arrow_actual);
         x_line = (int)x5_block + height_letter;
         y_line = (int)y5_block + height_letter * 20;
         g2d.drawString(wind_true_line_10m, x_line, y_line); 
         
         
         ////// right: true gust at 10m height: //////
         //
         g2d.setPaint(color_black);
         x_line = (int)x5_block + height_letter;
         y_line = (int)y5_block + height_letter * 22;
         g2d.drawString(wind_gust_line_10m, x_line, y_line); 
        
        
         ////// right: wind at 10m note: //////
         //
         g2d.setFont(font_h);
         g2d.setPaint(color_black);
         x_line = (int)x5_block + height_letter;
         y_line = (int)y5_block + height_letter * 24;
         g2d.drawString("** the reference level", x_line, y_line); 
         g2d.setFont(font_g);
         
         
         /////// Bf description (covers a number of lines) ///////
         if (beaufort_class <= 12)
         {
            g2d.setFont(font_h);
            g2d.setPaint(color_black);
            x_line = (int)x5_block + height_letter;
            
            String line0 = "Bf " + Integer.toString(beaufort_class) + ":";
            String line1 = "";
            String line2 = "";
            String line3 = "";
            String line4 = "";
            String line5 = "";
            String line6 = "";
            String line7 = "";
            String line8 = "";
            String line9 = "";
            String line10 = "";
            String line11 = "";
            String line12 = "";
            String line13 = "";
            
            switch (beaufort_class)
            {
               case 0 : line1 = "Sea like a mirror"; 
                        line2 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line3 = "image © N.C. Horner";
                        }
                        break;
               case 1 : line1 = "Ripples with the appearance of";                   
                        line2 = "scales are formed, but without";  
                        line3 = "foam crests";
                        line4 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line5 = "image © G.J. Simpson";
                        }
                        break; 
               case 2 : line1 = "Small wavelets, still short";
                        line2 = "but more pronounced: crests"; 
                        line3 = "have a glassy appearance and";
                        line4 = "do not break";
                        line5 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line6 = "image © G.J. Simpson";
                        }
                        break;
               case 3 : line1 = "Large wavelets; crests begin ";
                        line2 = "to break; foam of glassy";  
                        line3 = "appearance; perhaps scattered";
                        line4 = "white horses";
                        line5 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line6 = "image © I.G. McNeil";
                        }
                        break;
               case 4 : line1 = "Small waves, becoming longer;";
                        line2 = "fairly frequent white horses"; 
                        line3 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line4 = "image © I.G. McNeil";
                        }
                        break;
               case 5 : line1 = "Moderate waves, taking a more";
                        line2 = "pronounced long form; many";
                        line3 = "white horses are formed (chance";
                        line4 = "of somespray)";
                        line5 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line6 = "image © Crown";
                        }
                        break;
               case 6 : line1 = "Large waves begin to form;";
                        line2 = "the white foam crests are";
                        line3 = "more extensive everywhere"; 
                        line4 = "(probably some spray)";
                        line5 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line6 = "image © I.G. McNeil";
                        }
                        break;
               case 7 : line1 = "Sea heaps up and white foam";
                        line2 = "from breaking waves begins to";
                        line3 = "be blown in streaks along the";
                        line4 = "direction of the wind";  
                        line5 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line6 = "image © Crown";
                        }
                        break;
               case 8 : line1 = "Moderately high waves of";
                        line2 = "greater length; edges of crests";
                        line3 = "begin to break into the";
                        line4 = "spindrift; the foam is blown in";
                        line5 = "well-marked streaks along the"; 
                        line6 = "direction of the wind";
                        line7 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line8 = "image © W.A.E. Smith";
                        }
                        break;
               case 9 : line1 = "High waves; dense streaks of";
                        line2 = "foam along the direction of";
                        line3 = "the wind; crests of waves begin";
                        line4 = "to topple, tumble and roll ";
                        line5 = "over; spray mayaffect"; 
                        line6 = "visibility";
                        line7 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line8 = "image © J.P. Lacock";
                        }
                        break;
               case 10: line1 = "Very high waves with long";
                        line2 = "overhanging crests; the ";
                        line3 = "resulting foam, in greater";
                        line4 = "patches, is blown in dense";
                        line5 = "white streaks along the";
                        line6 = "direction of the wind; on the";
                        line7 = "whole, the surface of the sea";
                        line8 = "takes awhite appearance; the";
                        line9 = "tumbling of the sea becomes";
                        line10 = "heavy and shock-like;";
                        line11 = "visibility affected";
                        line12 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line13 = "image © G. Allen";
                        }
                        break;
               case 11: line1 = "Exceptionally high waves";
                        line2 = "(small and medium-sized ships";
                        line3 = "might be for a time lost to";
                        line4 = "view behind the waves); the";
                        line5 = "sea is completely covered with";
                        line6 = "long white patches of foam";
                        line7 = "lying along the direction of";
                        line8 = "the wind; everywhere the"; 
                        line9 = "edges of the wave crests are";
                        line10 = "blown into froth; visibility";
                        line11 = "affected";
                        line12 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line13 = "image © Crown";
                        }
                        break;
               case 12: line1 = "The air is filled with foam";
                        line2 = "and spray; sea completely white";
                        line3 = "with driving spray; visibility"; 
                        line4 = "very seriously affected";
                        line5 = "";
                        if (!DASHBOARD_view_AWS_radar.night_vision == true)
                        {
                           line6 = "image © J.F. Thompson";
                        }
                        break;
               default: break;
            } // switch (beaufort_class)
            
            String bf_line = "";
            for (int i = 0; i <= 13; i++)
            {
               bf_line = "";
               
               if (i == 0)
               {
                  bf_line = line0;
               }
               else if (i == 1)
               {
                  bf_line = line1;
               }
               else if (i == 2)
               {
                  bf_line = line2;
               }
               else if (i == 3)
               {
                  bf_line = line3;
               }
               else if (i == 4)
               {
                  bf_line = line4;
               }
               else if (i == 5)
               {
                  bf_line = line5;
               }
               else if (i == 6)
               {
                  bf_line = line6;
               }
               else if (i == 7)
               {
                  bf_line = line7;
               }
               else if (i == 8)
               {
                  bf_line = line8;
               }
               else if (i == 9)
               {
                  bf_line = line9;
               }
               else if (i == 10)
               {
                  bf_line = line10;
               }
               else if (i == 11)
               {
                  bf_line = line11;
               }
               else if (i == 12)
               {
                  bf_line = line12;
               }
               else if (i == 13)
               {
                  bf_line = line13;
               }
               
               y_line = (int)y5_block + height_letter * (34 + i * 2);
               g2d.drawString(bf_line, x_line, y_line); 
               
            } // for (int i = 0; i <= 5; i++)
         } // if (beaufort_class <= 12)
      } // if (draw_info_blocks)
      
      
      ///////////////////// "last updated date and time" bottom left screen [South panel]
      //
      String updated_text_bottom_screen = "last updated: ";
      String update_message_bottom_screen = "";
   
      if (main_RS232_RS422.dashboard_string_last_update_record_date_time.equals(""))
      {
         update_message_bottom_screen =  updated_text_bottom_screen + " unknown";
      }
      else
      {
         //update_message_bottom_screen = updated_text_bottom_screen + " " + main_RS232_RS422.dashboard_string_last_update_record_date_time.replace("-", " ");
         update_message_bottom_screen = updated_text_bottom_screen + " " + main_RS232_RS422.dashboard_string_last_update_record_date_time;
      }

      DASHBOARD_view_AWS_radar.jLabel3.setForeground(color_update_message_south_panel);
      DASHBOARD_view_AWS_radar.jLabel3.setText(update_message_bottom_screen);
   
   } // public void paintComponent(Graphics g) 
   
   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
private int convert_knots_to_bf(int true_wind_speed_reading)
{
   int bf_class = Integer.MAX_VALUE;
   
   
   if (true_wind_speed_reading >= 64 && true_wind_speed_reading < 400)
   {
      bf_class = 12; 
   }
   else
   {   
      switch (true_wind_speed_reading)
      {
         case 0:                                                                 bf_class = 0; break;
         case 1: case 2: case 3:                                                 bf_class = 1; break;                  
         case 4: case 5: case 6:                                                 bf_class = 2; break;    
         case 7: case 8: case 9: case 10:                                        bf_class = 3; break;  
         case 11: case 12: case 13: case 14: case 15: case 16:                   bf_class = 4; break;
         case 17: case 18: case 19: case 20: case 21:                            bf_class = 5; break;
         case 22: case 23: case 24: case 25: case 26: case 27:                   bf_class = 6; break;
         case 28: case 29: case 30: case 31: case 32: case 33:                   bf_class = 7; break;
         case 34: case 35: case 36: case 37: case 38: case 39: case 40:          bf_class = 8; break;
         case 41: case 42: case 43: case 44: case 45: case 46: case 47:          bf_class = 9; break;
         case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55: bf_class = 10;break;
         case 56: case 57: case 58: case 59: case 60: case 61: case 62: case 63: bf_class = 11;break;
         default :                                                               bf_class = Integer.MAX_VALUE; break;
      } // switch
   } // else  
   
   
   return bf_class;
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
private int convert_ms_to_bf(int true_wind_speed_reading)
{
   int bf_class = Integer.MAX_VALUE;
   
   
   if (true_wind_speed_reading >= 33 && true_wind_speed_reading < 400)
   {
      bf_class = 12; 
   }
   else
   {   
      switch (true_wind_speed_reading)
      {
         case 0:                                                                 bf_class = 0; break;
         case 1:                                                                 bf_class = 1; break;    
         case 2: case 3:                                                         bf_class = 2; break;    
         case 4: case 5:                                                         bf_class = 3; break;  
         case 6: case 7:                                                         bf_class = 4; break;
         case 8: case 9: case 10:                                                bf_class = 5; break;
         case 11: case 12: case 13:                                              bf_class = 6; break;
         case 14: case 15: case 16: case 17:                                     bf_class = 7; break;
         case 18: case 19: case 20:                                              bf_class = 8; break;
         case 21: case 22: case 23: case 24:                                     bf_class = 9; break;
         case 25: case 26: case 27: case 28:                                     bf_class = 10;break;
         case 29: case 30: case 31: case 32:                                     bf_class = 11;break;
         default :                                                               bf_class = Integer.MAX_VALUE; break;
      } // switch
   } // else   
   
   
   return bf_class;
}  
   

   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_wind_arrow_radar(Graphics2D g2d, double marker_circle_diameter_2, double bf_per_class_radius, String wind_type, boolean wind_dir_present, boolean wind_speed_present, int wind_dir, int wind_speed)
{
   boolean wind_dir_ok             = false;
   boolean wind_speed_ok           = false;
   int Bf_class                    = Integer.MAX_VALUE;
   int wind_dir_reading            = Integer.MAX_VALUE;
   double max_arrow_width          = 0;
   float thickness_wind_rose_arrow = 2.0f;
   
   
   if (wind_type.equals(TRUE_WIND))
   {
      color_wind_arrow_max    = color_true_wind_arrow_max;
      color_wind_arrow_actual = color_true_wind_arrow_actual;     
      max_arrow_width = 8;
   }
   else if (wind_type.equals(RELATIVE_WIND))
   {
      color_wind_arrow_max    = color_rel_wind_arrow_max;
      color_wind_arrow_actual = color_rel_wind_arrow_actual;
      max_arrow_width = 10;
   }
   
   // wind dir
   if (wind_dir_present)
   {
      wind_dir_reading = wind_dir;
      if (wind_dir_reading >= 1 && wind_dir_reading <= 360)       // dir = 0 should be impossible 
      {
         wind_dir_ok = true;
      }
   } //if (main.true_wind_dir_from_AWS_present)
   
   
   // insert wind speed as Bf
   if (wind_speed_present)
   {
      int wind_speed_reading = wind_speed; // NB could by kts or m/s
      if (wind_speed_reading >= 0.0 && wind_speed_reading <= 400.0) 
      {
         // so the digit indication is up to 400 kts/m/s and the analogue up to 110 kts/m/s
         if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)                        // units is knots
         {
/*            
            if (wind_speed_reading >= 64 && wind_speed_reading < 400)
            {
               Bf_class = 12; 
               wind_speed_ok = true; 
            }
            else
            {   
               switch (wind_speed_reading)
               {
                  case 0:                                                                 Bf_class = 0; wind_speed_ok = true; break;
                  case 1: case 2: case 3:                                                 Bf_class = 1; wind_speed_ok = true; break;                  
                  case 4: case 5: case 6:                                                 Bf_class = 2; wind_speed_ok = true; break;    
                  case 7: case 8: case 9: case 10:                                        Bf_class = 3; wind_speed_ok = true; break;  
                  case 11: case 12: case 13: case 14: case 15: case 16:                   Bf_class = 4; wind_speed_ok = true; break;
                  case 17: case 18: case 19: case 20: case 21:                            Bf_class = 5; wind_speed_ok = true; break;
                  case 22: case 23: case 24: case 25: case 26: case 27:                   Bf_class = 6; wind_speed_ok = true; break;
                  case 28: case 29: case 30: case 31: case 32: case 33:                   Bf_class = 7; wind_speed_ok = true; break;
                  case 34: case 35: case 36: case 37: case 38: case 39: case 40:          Bf_class = 8; wind_speed_ok = true; break;
                  case 41: case 42: case 43: case 44: case 45: case 46: case 47:          Bf_class = 9; wind_speed_ok = true; break;
                  case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55: Bf_class = 10; wind_speed_ok = true; break;
                  case 56: case 57: case 58: case 59: case 60: case 61: case 62: case 63: Bf_class = 11; wind_speed_ok = true; break;
                  default :                                                               Bf_class = Integer.MAX_VALUE; break;
               } // switch
            } // else
*/            
            Bf_class = convert_knots_to_bf(wind_speed_reading);
            
            
            
         } //  if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1) 
         
         else if (main.wind_units_dashboard.indexOf(main.M_S) != -1)                      // units is m/s
         {
/*            
            if (wind_speed_reading >= 33 && wind_speed_reading < 400)
            {
               Bf_class = 12; 
               wind_speed_ok = true; 
            }
            else
            {   
               switch (wind_speed_reading)
               {
                  case 0:                                                                 Bf_class = 0; wind_speed_ok = true; break;
                  case 1:                                                                 Bf_class = 1; wind_speed_ok = true; break;    
                  case 2: case 3:                                                         Bf_class = 2; wind_speed_ok = true; break;    
                  case 4: case 5:                                                         Bf_class = 3; wind_speed_ok = true; break;  
                  case 6: case 7:                                                         Bf_class = 4; wind_speed_ok = true; break;
                  case 8: case 9: case 10:                                                Bf_class = 5; wind_speed_ok = true; break;
                  case 11: case 12: case 13:                                              Bf_class = 6; wind_speed_ok = true; break;
                  case 14: case 15: case 16: case 17:                                     Bf_class = 7; wind_speed_ok = true; break;
                  case 18: case 19: case 20:                                              Bf_class = 8; wind_speed_ok = true; break;
                  case 21: case 22: case 23: case 24:                                     Bf_class = 9; wind_speed_ok = true; break;
                  case 25: case 26: case 27: case 28:                                     Bf_class = 10; wind_speed_ok = true; break;
                  case 29: case 30: case 31: case 32:                                     Bf_class = 11; wind_speed_ok = true; break;
                  default :                                                               Bf_class = Integer.MAX_VALUE; break;
               } // switch
            } // else
*/
            Bf_class = convert_ms_to_bf(wind_speed_reading);


         } // else if (main.wind_units_dashboard.indexOf(main.M_S) != -1)
        
         
         
         wind_speed_ok = Bf_class <= 12;
         
        
      } // if (wind_speed_reading >= 0.0 && wind_speed_reading <= 400.0)
   } //if (main.true_wind_speed_from_AWS_present)
   
   
   if (wind_speed_ok && wind_dir_ok)
   {
      double radius_wind_arrow_max = marker_circle_diameter_2 / 2;
    
      // NB "wind_rose_diameter = block_wind_rose_width / 1.5"  (if block_wind_rose_width < block_wind_rose_heightz) else "wind_rose_diameter = block_wind_rose_height / 1.5" ;
      //
      // point of the arrow is origin (0, 0)
      //
      //       (x1,y1)____________ (x2,y2)
      //              \          /
      //               \        /
      //                \      /
      //                 \    /  
      //                  \  /
      //                    .   (0,0)
      //
      //
      //
   
   
      double radius_wind_arrow_actual = bf_per_class_radius * Bf_class;
   
   
      g2d.setStroke(new BasicStroke(thickness_wind_rose_arrow));
   
      
      //double max_arrow_width = 10;
   
      // polygon of the max theoretical wind arrow (12 Bf)
      //
      double x1_arrow = Math.cos(Math.toRadians(wind_dir_reading - (max_arrow_width / 2) - 90)) * (radius_wind_arrow_max);                   // -90 because the drawing starts at EAST (90 degr)
      double x2_arrow = Math.cos(Math.toRadians(wind_dir_reading + (max_arrow_width / 2) - 90)) * (radius_wind_arrow_max); 
      double x3_arrow = Math.cos(Math.toRadians(wind_dir_reading + 0 - 90)) * (radius_wind_arrow_max);
      
      double y1_arrow = Math.sin(Math.toRadians(wind_dir_reading - (max_arrow_width / 2) - 90)) * (radius_wind_arrow_max);
      double y2_arrow = Math.sin(Math.toRadians(wind_dir_reading + (max_arrow_width / 2) - 90)) * (radius_wind_arrow_max);        
      double y3_arrow = Math.sin(Math.toRadians(wind_dir_reading + 0 - 90)) * (radius_wind_arrow_max);        
            
      double xPoints[] = {0, x1_arrow, x3_arrow, x2_arrow};
      double yPoints[] = {0, y1_arrow, y3_arrow, y2_arrow};       
   
      GeneralPath polygon_max = new GeneralPath(GeneralPath.WIND_EVEN_ODD,  xPoints.length);
      polygon_max.moveTo(xPoints[0], yPoints[0]);
      for (int index = 1; index < xPoints.length; index++ ) 
      {
         polygon_max.lineTo(xPoints[index], yPoints[index]);
      }
      polygon_max.closePath();
   
      // polygon of the actual wind arrow (e.g 4 Bf)
      //
      double x1_arrow_actual = Math.cos(Math.toRadians(wind_dir_reading - (max_arrow_width / 2) - 90)) * (radius_wind_arrow_actual);                   // -90 because the drawing starts at EAST (90 degr)
      double x2_arrow_actual = Math.cos(Math.toRadians(wind_dir_reading + (max_arrow_width / 2) - 90)) * (radius_wind_arrow_actual); 
      double x3_arrow_actual = Math.cos(Math.toRadians(wind_dir_reading + 0 - 90)) * (radius_wind_arrow_actual);
      
      double y1_arrow_actual = Math.sin(Math.toRadians(wind_dir_reading - (max_arrow_width / 2) - 90)) * (radius_wind_arrow_actual);
      double y2_arrow_actual = Math.sin(Math.toRadians(wind_dir_reading + (max_arrow_width / 2) - 90)) * (radius_wind_arrow_actual);        
      double y3_arrow_actual = Math.sin(Math.toRadians(wind_dir_reading + 0 - 90)) * (radius_wind_arrow_actual);        
      
      double xPoints_actual[] = {0, x1_arrow_actual, x3_arrow_actual, x2_arrow_actual};
      double yPoints_actual[] = {0, y1_arrow_actual, y3_arrow_actual, y2_arrow_actual};       
   
      GeneralPath polygon_actual = new GeneralPath(GeneralPath.WIND_EVEN_ODD,  xPoints.length);
      polygon_actual.moveTo(xPoints[0], yPoints[0]);
      for (int index = 1; index < xPoints_actual.length; index++ ) 
      {
         polygon_actual.lineTo(xPoints_actual[index], yPoints_actual[index]);
      }
      polygon_actual.closePath();
   
   
      // wind arrow fill (full theoretical possible max Bf12)
      g2d.setPaint(color_wind_arrow_max);   
      g2d.fill(polygon_max);
   
      // wind arrow fill (only the actual measured Bf part)
      g2d.setPaint(color_wind_arrow_actual);   
      g2d.fill(polygon_actual);
   
      // wind arrow contour (full theoretical possible max Bf12)
      //g2d.setColor(color_black);
      g2d.setColor(color_wind_arrow_actual);
      g2d.draw(polygon_max); 
   } // if (true_wind_speed_ok && true_wind_dir_ok)
   
}


   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_wind_rose_radar(Graphics2D g2d, double wind_rose_diameter, double marker_circle_diameter_1, double marker_circle_diameter_2, double bf_per_class_radius, int beaufort_class)
{
   // eg: https://www.google.nl/search?q=barometer&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjJ78PZg8_UAhWEJMAKHZA3AsAQ_AUICigB&biw=1920&bih=950#imgrc=hE2asDauQrhr6M:
   
   String letter = null;
   String text;
   int angle;
   int start;
   int x;
   int y;
   int width_letter;
   float thickness_wind_rose_ring = 1.0f;
 

   //int horz_offset                = (int)(wind_rose_diameter - 0);            // -20 orig
   //int units_start                = (int)(wind_rose_diameter / 2 - 18);       // eg 220 / 2 - 18 = 92   // not the 5 and 10 units markers
   int units_end                  = (int)(wind_rose_diameter / 2 - 14);       // eg 220 / 2 - 14 = 96   // all markers end (5, 10 and intermediate) 
   int units_5_start              = (int)(wind_rose_diameter / 2 - 21);       // eg 220 / 2 - 21 = 89   //the 5 units markers
   int units_10_start             = (int)(wind_rose_diameter / 2 - 22);       // eg 220 / 2 - 22 = 88   //the 10 units markers
   int text_base_units_values     = (int)(wind_rose_diameter / 2 - 10);       // eg 220 / 2 - 10 = 100
   //int text_base_comments         = text_base_units_values / 2 + 5;           // eg 100 / 2 + 5 = 55
   int text_base_comments         = (int)(text_base_units_values - (22 * 1.5));        
   int width_a1                   = 0;
   int width_a2                   = 0;
   

   if (DASHBOARD_view_AWS_radar.night_vision == false)
   {
      // main wind/compass rose area   
      //g2d.setPaint(Color.LIGHT_GRAY);
      //g2d.setPaint(new Color(0,191,255, 255));
      g2d.setPaint(color_wind_rose_background);      
      g2d.fill(new Arc2D.Double(-wind_rose_diameter / 2, -wind_rose_diameter / 2, wind_rose_diameter, wind_rose_diameter, 0, 360, Arc2D.CHORD));
     
      // NB see also: https://docstore.mik.ua/orelly/java-ent/jfc/ch04_04.htm for obtaining area difference (substracting)
   
      // save current clipping area
      Rectangle rec = g2d.getClipBounds();
   
      // inside the inside marker ring area
      Shape circle = new Ellipse2D.Float((float)-marker_circle_diameter_2 / 2, (float)-marker_circle_diameter_2 / 2, (float)marker_circle_diameter_2, (float)marker_circle_diameter_2);
   
      
      if (beaufort_class <= 12)
      {
          // set clipping area to wind rose
         g2d.setClip(circle);
        
         String image = "bf" + Integer.toString(beaufort_class) + "_wind_radar.jpg";
         
/*      
         //Image img1 = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "waves_desktop.png")).getImage();
         Image img1 = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + image)).getImage();
      
         // set clipping area to wind rose
         g2d.setClip(circle);
      
         // NB scale the image to cover a the complete area of the drawing surface
         int width = getWidth();  
         int height = getHeight();  
         for (int xi = -width; xi < width; xi += img1.getWidth(null))    
         {  
            for (int yi = -height; yi < height; yi += img1.getHeight(null)) 
            {  
               g2d.drawImage(img1, xi, yi, this);  
            }  
         }      
*/         

         BufferedImage img1 = null;
         try 
         {
            img1 = ImageIO.read(this.getClass().getResource(main.ICONS_DIRECTORY + image));
            int wind_rose_radius = (int)wind_rose_diameter / 2;
            g2d.drawImage(img1, -wind_rose_radius, -wind_rose_radius, (int)wind_rose_diameter, (int)wind_rose_diameter, this);
         } 
         catch (IOException ex) 
         {
            System.out.println("+++ Dashboard wind radar background image error");
         }
     
      
         // reset clipping area
         g2d.setClip(rec);
         
   
      } // if (beaufort_class <= 12)
      
   } // if (DASHBOARD_view_AWS_radar.night_vision == false)
   else // night
   {
      Shape circle = new Ellipse2D.Float((float)-marker_circle_diameter_2 / 2, (float)-marker_circle_diameter_2 / 2, (float)marker_circle_diameter_2, (float)marker_circle_diameter_2);
      g2d.setPaint(color_black);   
      g2d.fill(circle);
   } // else
 
   
   
   // outside ring
   g2d.setColor(color_wind_rose_ring);
   g2d.setStroke(new BasicStroke(thickness_wind_rose_ring));
   g2d.draw(new Arc2D.Double(-wind_rose_diameter / 2, -wind_rose_diameter / 2, wind_rose_diameter, wind_rose_diameter, 0, 360, Arc2D.CHORD));

   
   for (int i = 0; i < 360; i++)     
   {
      // NB first i = <never mind> -> paint starting point = always East -> clockwise
      g2d.setColor(color_black);
      
      g2d.setFont(font_a);
      //g2d.setFont(font_b);
      
      if ((i % 5 == 0) && (i % 10 != 0))          
      {
         g2d.setStroke(new BasicStroke(1.0f));
         g2d.drawLine(units_5_start, 0, units_end, 0);
      }
      else if (i % 10 == 0) // tens marks                            // 10 units marks   
      {
         g2d.setStroke(new BasicStroke(2.0f));
         g2d.drawLine(units_10_start, 0, units_end, 0);
            
         // eg text = "90"
         if (i >= 0 && i <= 270)
         {  
            text = String.valueOf(i + 90);        // i = 90 = start of the scale (East dir)
         }
         else
         {
            text = String.valueOf(i - 270);        // i = 270 = North dir
         }
            
               
         // text rotated
         //
         // Font metrics
         width_a1 = g2d.getFontMetrics().stringWidth("90");
         width_a2 = g2d.getFontMetrics().stringWidth("100");
            

         x = text_base_units_values;
         if ( i > 270 && i <= 360)                 // 0 - 90 deg
         {
            y = 0 - width_a1 / 2;
         }
         else                                      // 100 - 360 deg
         {
            y = 0 - width_a2 / 2;
         }
            
         // unit numbers e.g. 90
         angle = 90;
         g2d.translate((float)x,(float)y);
         g2d.rotate(Math.toRadians(angle));
         g2d.drawString(text,0,0);
            
         g2d.rotate(-Math.toRadians(angle));    // rotate back
         g2d.translate(-(float)x,-(float)y);    // translate back
         
      } // else (tens)
         
         
      g2d.setColor(color_wind_rose_additional);
     
      // text decoration 'EAST'
      start = 0;
      //if (i == 360 - 15 || i == 360 - 5 || i == start + 5 || i == start + 15)
      if (i == 360 - 6 || i == 360 - 2 || i == start + 2 || i == start + 6)   
      //if (i == start)   
      {  
         if (i == 360 - 6)
         {
            letter = "E";
         }   
         else if (i == 360 - 2)
         {
            letter = "A";
         }
         else if (i == start + 2)
         {
            letter = "S";
         }
         else if (i == start + 6)
         {
            letter = "T";
         }
         //letter = "E";
            
         g2d.setFont(font_b);
         width_letter = g2d.getFontMetrics().stringWidth("E");
         y = 0 - width_letter / 2;
            
         x = text_base_comments;
         angle = 90;
         g2d.translate((float)x,(float)y);
         g2d.rotate(Math.toRadians(angle));
         g2d.drawString(letter,0,0);
            
         g2d.rotate(-Math.toRadians(angle));    // rotate back
         g2d.translate(-(float)x,-(float)y);    // translate back
      }         
      
      // text decoration 'SOUTH'
      start = 90;
      if (i == start - 8 || i == start - 4 || i == start || i == start + 4 || i == start + 8)
      //if (i == start)
      {  
         if (i == start - 8)
         {
            letter = "S";
         }   
         else if (i == start - 4)
         {
            letter = "O";
         }
         else if (i == start)
         {
            letter = "U";
         }
         else if (i == start + 4)
         {
            letter = "T";
         }
         else if (i == start + 8)
         {
            letter = "H";
         }
         //letter = "S";
           
         g2d.setFont(font_b);
         width_letter = g2d.getFontMetrics().stringWidth("S");
         y = 0 - width_letter / 2;
            
         x = text_base_comments;
         angle = 90;
         g2d.translate((float)x,(float)y);
         g2d.rotate(Math.toRadians(angle));
         g2d.drawString(letter,0,0);
            
         g2d.rotate(-Math.toRadians(angle));    // rotate back
         g2d.translate(-(float)x,-(float)y);    // translate back
      }      
         
      // text decoration 'WEST'
      start = 180;
      if (i == start - 6 || i == start - 2 || i == start + 2 || i == start + 6)
      //if (i == start)
      {  
         if (i == start - 6)
         {
            letter = "W";
         }   
         else if (i == start - 2)
         {
            letter = "E";
         }
         else if (i == start + 2)
         {
            letter = "S";
         }
         else if (i == start + 6)
         {
            letter = "T";
         }
         //letter = "W";
         
         g2d.setFont(font_b);
         width_letter = g2d.getFontMetrics().stringWidth("W");
         y = 0 - width_letter / 2;
            
         x = text_base_comments;
         angle = 90;
         g2d.translate((float)x,(float)y);
         g2d.rotate(Math.toRadians(angle));
         g2d.drawString(letter,0,0);
            
         g2d.rotate(-Math.toRadians(angle));    // rotate back
         g2d.translate(-(float)x,-(float)y);    // translate back
      }

      
      // text decoration 'NORTH'
      start = 270;
      if (i == start - 8 || i == start - 4 || i == start || i == start + 4 || i == start + 8)
      //if (i == start)
      {  
         if (i == start - 8)
         {
            letter = "N";
         }   
         else if (i == start - 4)
         {
            letter = "O";
         }
         else if (i == start)
         {
            letter = "R";
         }
         else if (i == start + 4)
         {
           letter = "T";
         }
         else if (i == start + 8)
         {
           letter = "H";
         }
         //letter = "N";   
           
         g2d.setFont(font_b);
         width_letter = g2d.getFontMetrics().stringWidth("N");
         y = 0 - width_letter / 2;
            
         x = text_base_comments;
         angle = 90;
         g2d.translate((float)x,(float)y);
         g2d.rotate(Math.toRadians(angle));
         g2d.drawString(letter,0,0);
            
         g2d.rotate(-Math.toRadians(angle));    // rotate back
         g2d.translate(-(float)x,-(float)y);    // translate back
      }      
      
      // Because the rotate function of Java Graphics takes a radian value as a parameter, we convert 3 degree to radians by the formula (?/180 x 3.0).
      //g2d.rotate((Math.PI / 180.0) * 3.0);  // 360 / 120 = 3.0
      g2d.rotate((Math.PI / 180.0) * 1.0);  // 360 / 360 = 1.0
      
   } // for (int i = 0; i < 360; i++)
   
   
   // NB Arc2D.Double(double x, double y, double w, double h, double start, double extent, int type)
   //    Constructs a new arc, initialized to the specified location, size, angular extents, and closure type.
   //
   // marker (outer) circles (this are not the Bf circles)
   //
   g2d.setColor(color_black);
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_1 / 2, -marker_circle_diameter_1 / 2, marker_circle_diameter_1, marker_circle_diameter_1, 360, 360, Arc2D.OPEN));   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_2 / 2, -marker_circle_diameter_2 / 2, marker_circle_diameter_2, marker_circle_diameter_2, 360, 360, Arc2D.OPEN));   // point East = 0 degrees -> to the left

   
   // Bf circles
   //
   g2d.setColor(color_wind_rose_additional);
   //float[] dash = {2f, 2f, 2f};   // length, space
   float[] dash = {2f, 4f};   // length, space
   g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));

   // every 3Bf a circle + label
   for (int i = 3; i <= 9; i+=3)
   {   
      // NB i = Bf class
      g2d.draw(new Arc2D.Double(-bf_per_class_radius * i, -bf_per_class_radius * i, 2 * bf_per_class_radius * i, 2* bf_per_class_radius * i, 360, 360, Arc2D.OPEN));   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
      
      // Bf circles labels
      //g2d.setColor(color_black);
      g2d.setFont(font_c);
      String bf_text = "Bf " + i;
      
      // label along 360 degrees direction
      int x_bf_label = 0;
      int y_bf_label = (int)(-bf_per_class_radius * i);
      g2d.drawString(bf_text, x_bf_label, y_bf_label);
      
      // labels along 90 degrees direction
      int x_bf_label2 = (int)(bf_per_class_radius * i);
      int y_bf_label2 = 0;
      g2d.translate((float)x_bf_label2, (float)y_bf_label2);
      g2d.rotate(Math.toRadians(90));
      g2d.drawString(bf_text, 0, 0);
      g2d.rotate(-Math.toRadians(90));
      g2d.translate(-(float)x_bf_label2, -(float)y_bf_label2);
      
   } // for (int i = 3; i <= 9; i+=3)   
   
/*
   // Labels (left below wind rose)
   //
   g2d.setColor(color_black);
   //g2d.setColor(color_wind_rose_additional);
   g2d.setFont(font_f);
   //g2d.drawString("True wind", (int)(-wind_rose_diameter / 2), (int)(wind_rose_diameter / 2));
   
   
   
   //
   int height_letter = g2d.getFontMetrics().stringWidth("M");
   
   
   // insert MSLP
   //
   
   
   //y = 0 - width_letter / 2
   
   int x_mslp = (int)((-wind_rose_diameter / 2) - (DASHBOARD_view_AWS_radar.width_AWS_radar_dashboard * 0.1 / 2));
   int y_mslp = (int)(wind_rose_diameter / 2) - (height_letter * 2);
   
   g2d.drawString("", x_mslp, y_mslp);
   if (main.pressure_MSL_from_AWS_present)
   {
      double reading_mslp = main_RS232_RS422.dashboard_double_last_update_record_MSL_pressure_ic;         // see: RS422_update_AWS_dasboard_values()
      if (reading_mslp > 900.0 && reading_mslp < 1100.0)
      {
         String digits = Double.toString(reading_mslp) + " hPa";
         g2d.drawString("MSLP: " + digits, x_mslp, y_mslp);
      }
   }
   else
   {
      g2d.drawString("MSLP: -", x_mslp, y_mslp);
   }
   
   // insert barometric tendency
   //
   int x_tendency = (int)((-wind_rose_diameter / 2) - (DASHBOARD_view_AWS_radar.width_AWS_radar_dashboard * 0.1 / 2));
   int y_tendency = (int)(wind_rose_diameter / 2);
   //
   g2d.drawString("", x_tendency, y_tendency);
   if (main.pressure_tendency_from_AWS_present)
   {
   //     
      double tendency_reading = main_RS232_RS422.dashboard_double_last_update_record_pressure_tendency; // see: RS422_update_AWS_dasboard_values()
      if (tendency_reading >= -99.9 && tendency_reading <= 99.9)
      {
         String digits = Double.toString(tendency_reading) + " hPa / 3 hrs";
         g2d.drawString("tendency: " + digits, x_tendency, y_tendency);  
      }
   } 
   else
   {
      g2d.drawString("tendency: -", x_tendency, y_tendency);
   }
*/   
   
   
}   
   private final String RELATIVE_WIND  = "relative_wind";
   private final String TRUE_WIND       = "true_wind";
   
   
   
   
   
   private final Color color_wind_rose_additional;
   private final Color color_wind_rose_ring;
   private final Color color_wind_rose_background;
   private final Color color_contour_block;
  
   
   private final Color color_true_wind_arrow_max; 
   private final Color color_rel_wind_arrow_max; 
   private final Color color_true_wind_arrow_actual;
   private final Color color_rel_wind_arrow_actual;
   
   private final Color color_inserted_data;   
   private final Color color_black;
   private final Color color_gray;
   private final float block_contour_thickness;
   
    private Color color_fill_block;                    // not final  // parameter value text blocks left and right of the main wind radar circle
   private Color color_last_update;                    // not final
   private Color color_digits;                         // not final
   private Color color_update_message_south_panel;

   private Color color_wind_arrow_max;                 // not final
   private Color color_wind_arrow_actual;              // not final
   
   private Font font_a; 
   private Font font_b;
   private Font font_c;
   //private Font font_f;
   private Font font_g;
   private Font font_h;
   
}
