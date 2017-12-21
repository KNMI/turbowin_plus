
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Martin
 */
public class DASHBOARD_grafiek_AWS_digital extends JPanel {
   
   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
 public DASHBOARD_grafiek_AWS_digital()
{ 
   color_black              = Color.BLACK;
   color_gray               = Color.GRAY;
   color_dark_red           = new Color(190, 0, 0);         // for main digits
   color_label_text         = Color.GRAY;
   color_instrument_contour = Color.BLACK;
   color_red                = Color.RED;                    // for main digits
   color_instrument_face    = Color.BLACK;
   color_instrument_ring    = Color.DARK_GRAY;
   
   // get the screen size 
   //
   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   double width_screen = screenSize.getWidth();
   double height_screen = screenSize.getHeight();
   System.out.println("--- Screen resolution AWS Dashboard digital: " + width_screen + " x " + height_screen);

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
   
   // width of a digital instrument (6 spaces + 5 instruments = 11 blocks; instument width and space width are equal here)
   // NB calculating instrument_width can't be done in DASHBOARD_grafiek_AWS_digital() because from here (paintComponents) it is only known
   //    see https://stackoverflow.com/questions/12010587/how-to-get-real-jpanel-size
   
   double low_limit;
   double max_limit;
   String label_instrument;
   boolean reading_present;
   double reading; 
   
   
   // dimensions instrument
   //
   instrument_width = DASHBOARD_view_AWS_digital.width_AWS_digital_dashboard / 6;  // NB 6 = 5 instument rows + some space 
   instrument_height = instrument_width / 2;
   instrument_ring_thickness = instrument_width / 10;
   horz_offset = (DASHBOARD_view_AWS_digital.width_AWS_digital_dashboard - (5 * instrument_width)) / 6;  // 6 space blocks in one row of 5 instruments
   
   fontSize_b = (int)(instrument_height - (2 * instrument_ring_thickness) -3);         // measured value  (eg 1002.3)               // -5 for just a slightly better fit
   fontSize_d = instrument_height / 7;                                                 // parameter text (eg "Temp [deg]")
   fontSize_e = (int)(1.5 * instrument_height / 7);                                                      // last updated text in middle screen
  
   try 
   {
      // NB font file LCD: https://www.dafont.com/lcd-lcd-mono.font
      
      //Font font_digital = Font.createFont(Font.TRUETYPE_FONT, new File(main.logs_dir + java.io.File.separator + DIGITAL_FONT_DIR + java.io.File.separator + digital_font));
      Font font_digital = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream(main.ICONS_DIRECTORY + digital_font));
      font_b = font_digital.deriveFont(Font.BOLD, (float)fontSize_b);                   // measured value  (eg 1002.3)  
      font_d = font_digital.deriveFont(Font.BOLD, (float)fontSize_d);                   // parameter text (eg "Temp [deg]")
      font_e = font_digital.deriveFont(Font.BOLD, (float)fontSize_e);                   // last updated text in middle screen
   } 
   catch (FontFormatException | IOException ex) 
   {
      main.log_turbowin_system_message("[DASHBOARD] Couldn't find font: " + digital_font);
      
      // revert to a default logical font (logical fonts are always available in the JRE)
      font_b = new Font("Dialog", Font.BOLD, fontSize_b);                                // measured value  (eg 1002.3)  
      font_d = new Font("Dialog", Font.PLAIN, fontSize_d);                               // parameter text (eg "Temp [deg]")
      font_e = new Font("Dialog", Font.PLAIN, fontSize_e); 
   }
   
   
   // meters top row (x0 and y0 are the centrum of the meter)
   //
   int x0_meter_top_middle = getWidth() / 2;
   int y0_meter_top_middle = getHeight() / 3; 
   
   int x0_meter_top_right = getWidth() / 2 + instrument_width + horz_offset;
   int y0_meter_top_right = getHeight() / 3;     
   
   int x0_meter_top_right2 = getWidth() / 2 + (2 * instrument_width) + (2 * horz_offset);
   int y0_meter_top_right2 = getHeight() / 3;     
   
   int x0_meter_top_left = getWidth() / 2 - instrument_width - horz_offset;
   int y0_meter_top_left = getHeight() / 3;     
   
   int x0_meter_top_left2 = getWidth() / 2 - (2 * instrument_width) - (2 * horz_offset);
   int y0_meter_top_left2 = getHeight() / 3;     
   
   
   // meters bottom row
   //
   int x0_meter_bottom_middle = getWidth() / 2;
   int y0_meter_bottom_middle = getHeight() - (getHeight() / 3) + instrument_height;  
   
   int x0_meter_bottom_right = getWidth() / 2 + instrument_width + horz_offset;
   int y0_meter_bottom_right = getHeight() - (getHeight() / 3) + instrument_height;  
   
   int x0_meter_bottom_right2 = getWidth() / 2 + (2 * instrument_width) + (2 * horz_offset);
   int y0_meter_bottom_right2 = getHeight() - (getHeight() / 3) + instrument_height;   
   
   int x0_meter_bottom_left = getWidth() / 2 - instrument_width - horz_offset;
   int y0_meter_bottom_left = getHeight() - (getHeight() / 3) + instrument_height;  
  
   int x0_meter_bottom_left2 = getWidth() / 2 - (2 * instrument_width) - (2 * horz_offset);
   int y0_meter_bottom_left2 = getHeight() - (getHeight() / 3) + instrument_height;  
   
   
   // always call the super's method to clean "dirty" pixels
   super.paintComponent(g);
   
   
   // general
   final Graphics2D g2d = (Graphics2D) g;
   
  
   // background image(not in night vision mode)
   //
   if (DASHBOARD_view_AWS_digital.night_vision == false)
   {
      Image img1 = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + main.DASHBOARD_LOGO)).getImage();
      //scale the image to cover a the complete area of the drawing surface
      //g2d.drawImage(img1, 0, 0,getWidth(), getHeight(), 0, 0, img1.getWidth(null), img1.getHeight(null), null);
      int width = getWidth();  
      int height = getHeight();  
      for (int x = 0; x < width; x += img1.getWidth(null)) 
      {  
         for (int y = 0; y < height; y += img1.getHeight(null)) 
         {  
            g2d.drawImage(img1, x, y, this);  
         }  
      }       
   } // if (DASHBOARD_view_AWS.night_vision == false)
   
   
   //int side = getWidth() > getHeight() ? getHeight() : getWidth();
   //g2d.scale((side / 2) / 200, (side / 2) / 200);
   
   setAllRenderingHints(g2d);
   
   // colors depending night or day vision
   if (DASHBOARD_view_AWS_digital.night_vision == true)
   {
      color_digits = color_dark_red;
      color_last_update = color_dark_red;
      color_update_message_south_panel = color_gray;
   }
   else
   {
      color_digits = color_red;
      color_last_update = color_red;
      color_update_message_south_panel = color_black;
   }
   
   // obsolete data (no cummunication for roughly > 5 minutes with the AWS)
   if (main.displayed_aws_data_obsolate == true)   // valid for analog and digital dashboard
   {
      color_digits = main.obsolate_color_data_from_aws;
      color_last_update = main.obsolate_color_data_from_aws;
   }
   
   
   // set new origin
   g2d.translate(getWidth() / 2, getHeight() / 2);
   
   
   //
   ///////////////////// "last updated date and time" label in middle screen
   //
   String updated_text = "last updated";
   String update_message = "";
   final double height_datum_tijd_updated;
   final double width_datum_tijd_updated;
   
   g2d.setFont(font_e);
      
   if (main_RS232_RS422.dashboard_string_last_update_record_date_time.equals(""))
   {
      update_message =  updated_text + " unknown";
      FontRenderContext context_datum_tijd_parameter = g2d.getFontRenderContext();
      Rectangle2D bounds_datum_tijd_updated = font_e.getStringBounds(update_message, context_datum_tijd_parameter);
      height_datum_tijd_updated = -bounds_datum_tijd_updated.getY();     // hoogte char's                     
      width_datum_tijd_updated = bounds_datum_tijd_updated.getWidth();   // lengte string
   }
   else
   {
      update_message = updated_text + " " + main_RS232_RS422.dashboard_string_last_update_record_date_time.replace("-", " ");
      FontRenderContext context_datum_tijd_parameter = g2d.getFontRenderContext();
      Rectangle2D bounds_datum_tijd_updated = font_e.getStringBounds(update_message, context_datum_tijd_parameter);
      height_datum_tijd_updated = -bounds_datum_tijd_updated.getY();     // hoogte char's                     
      width_datum_tijd_updated = bounds_datum_tijd_updated.getWidth();   // lengte string
   }
      
   // "update date-time"- label face   
   g2d.setPaint(color_instrument_face);
   double x_left_label = -width_datum_tijd_updated / 2 - 10;
   double x_right_label = width_datum_tijd_updated + 20;
   double y_top_label = - (2 * height_datum_tijd_updated);
   double y_bottom_label = 2 * height_datum_tijd_updated;
   double x_text_label = -(width_datum_tijd_updated / 2.0);
   double y_text_label = -(height_datum_tijd_updated / 2.0);
   
   g2d.fill(new RoundRectangle2D.Double(x_left_label, y_top_label, x_right_label, y_bottom_label, 20, 20));       
      
   // "update date-time"-label contour
   g2d.setPaint(color_instrument_ring);
   g2d.setStroke(new BasicStroke(5.0f));
   g2d.draw(new RoundRectangle2D.Double(x_left_label, y_top_label, x_right_label, y_bottom_label, 20, 20));       
   
   g2d.setColor(color_last_update);
   g2d.drawString(update_message, (int)x_text_label, (int)y_text_label);   
      
   //      width_AWS_digital_dashboard = jPanel1.getWidth();
   //height_AWS_digital_dashboard = jPanel1.getHeight();

   
   //
   ///////////////////// "last updated date and time" bottom left screen [South panel]
   //
   String updated_text_bottom_screen = "meters updated every minute. last updated: ";
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

   DASHBOARD_view_AWS_digital.jLabel3.setForeground(color_update_message_south_panel);
   DASHBOARD_view_AWS_digital.jLabel3.setText(update_message_bottom_screen);


   
   //
   // top row meters   : 1 - 2 - 3 - 4 - 5
   // bottom row meters: 6 - 7 - 8 - 9 - 10
   //
   
   // 
   ///////////////// top middle meter (3) [TEMPERATURE] ///////////////////
   //
   g2d.translate(-getWidth() / 2, -getHeight() / 2);
   g2d.translate(x0_meter_top_middle, y0_meter_top_middle);
   
   low_limit = -60;
   max_limit = 60;
   label_instrument = "Temp [C]";//"Temp [°C]";
   reading_present = main.air_temp_from_AWS_present;
   reading =   main_RS232_RS422.dashboard_double_last_update_record_air_temp;
   draw_AWS_digital_instrument(g2d, low_limit, max_limit, label_instrument, reading_present, reading);

   
   //
   ///////////////////// top meter (4) [RELATIVE HUMIDITY] //////////////////
   //
   g2d.translate(-x0_meter_top_middle, -y0_meter_top_middle);
   g2d.translate(x0_meter_top_right, y0_meter_top_right);
   
   low_limit = 0;
   max_limit = 105;
   label_instrument = "RH [pct]";  // "RH [%]";
   reading_present = main.rh_from_AWS_present;
   reading =   main_RS232_RS422.dashboard_double_last_update_record_humidity;
   draw_AWS_digital_instrument(g2d, low_limit, max_limit, label_instrument, reading_present, reading);
  
   
   //
   ///////////////////// top meter (5) [SST] //////////////////
   //
   g2d.translate(-x0_meter_top_right, -y0_meter_top_right);
   g2d.translate(x0_meter_top_right2, y0_meter_top_right2);
   
   low_limit = -10;
   max_limit = 40;
   label_instrument = "SST [C]";//"SST [°C]";
   reading_present = main.SST_from_AWS_present;
   reading = main_RS232_RS422.dashboard_double_last_update_record_sst; 
   draw_AWS_digital_instrument(g2d, low_limit, max_limit, label_instrument, reading_present, reading);
   
   //
   ///////////////////// top meter (2) [AIR PRESSURE READ/SENSOR HEIGHT] //////////////////
   //
   g2d.translate(-x0_meter_top_right2, -y0_meter_top_right2);
   g2d.translate(x0_meter_top_left, y0_meter_top_left);
   
   low_limit = 900;
   max_limit = 1100;
   label_instrument = "Pressure read [hPa]";
   reading_present = main.pressure_sensor_level_from_AWS_present;
   reading =  main_RS232_RS422.dashboard_double_last_update_record_sensor_level_pressure_ic;
   draw_AWS_digital_instrument(g2d, low_limit, max_limit, label_instrument, reading_present, reading);
  
   
   //
   ///////////////////// top meter (1) [AIR PRESSURE MSL] //////////////////
   //
   g2d.translate(-x0_meter_top_left, -y0_meter_top_left);
   g2d.translate(x0_meter_top_left2, y0_meter_top_left2);
   
   low_limit = 900;
   max_limit = 1100;
   label_instrument = "Pressure MSL [hPa]";
   reading_present = main.pressure_MSL_from_AWS_present;
   reading =  main_RS232_RS422.dashboard_double_last_update_record_MSL_pressure_ic;
   draw_AWS_digital_instrument(g2d, low_limit, max_limit, label_instrument, reading_present, reading);
   
   
   //
   ///////////////////// bottom middle meter (8) [wind gust]  //////////////////
   //
   g2d.translate(-x0_meter_top_left2, -y0_meter_top_left2);
   g2d.translate(x0_meter_bottom_middle, y0_meter_bottom_middle);
   
   low_limit = 0;
   max_limit = 800;
   label_instrument = "Wind gust true [kts]";
   reading_present = main.true_wind_gust_from_AWS_present;
   reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_gust; 
   draw_AWS_digital_instrument(g2d, low_limit, max_limit, label_instrument, reading_present, reading);
   
   
   //
   ///////////////////// bottom meter 9 [relative wind speed] //////////////////
   //
   g2d.translate(-x0_meter_bottom_middle, -y0_meter_bottom_middle);
   g2d.translate(x0_meter_bottom_right, y0_meter_bottom_right);
   
   low_limit = 0;
   max_limit = 400;
   label_instrument = "Wind speed rel [kts]";
   reading_present = main.relative_wind_speed_from_AWS_present;
   reading = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_speed;
   draw_AWS_digital_instrument(g2d, low_limit, max_limit, label_instrument, reading_present, reading);
   
   
   //
   ///////////////////// bottom meter 10 [relative wind dir] //////////////////
   //
   g2d.translate(-x0_meter_bottom_right, -y0_meter_bottom_right);
   g2d.translate(x0_meter_bottom_right2, y0_meter_bottom_right2);
   
   low_limit = 0.1;
   max_limit = 360;
   label_instrument = "Wind dir rel [deg]"; //"Wind dir rel [°]";
   reading_present = main.relative_wind_dir_from_AWS_present;
   reading = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_dir;
   draw_AWS_digital_instrument(g2d, low_limit, max_limit, label_instrument, reading_present, reading);
   
   
   //
   ///////////////////// bottom meter 7 [true wind dir] //////////////////
   //
   g2d.translate(-x0_meter_bottom_right2, -y0_meter_bottom_right2);
   g2d.translate(x0_meter_bottom_left, y0_meter_bottom_left);
   
   low_limit = 0.1;
   max_limit = 360;
   label_instrument = "Wind dir true [deg]";//"Wind dir true [°]";
   reading_present = main.true_wind_dir_from_AWS_present;
   reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_dir;
   draw_AWS_digital_instrument(g2d, low_limit, max_limit, label_instrument, reading_present, reading);
  
   
   //
   ///////////////////// bottom meter 6 [true wind speed]] //////////////////
   //
   g2d.translate(-x0_meter_bottom_left, -y0_meter_bottom_left);
   g2d.translate(x0_meter_bottom_left2, y0_meter_bottom_left2);
   
   low_limit = 0;
   max_limit = 400;
   label_instrument = "Wind speed true [kts]";
   reading_present = main.true_wind_speed_from_AWS_present;
   reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_speed;
   draw_AWS_digital_instrument(g2d, low_limit, max_limit, label_instrument, reading_present, reading);

    
}   
   



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_AWS_digital_instrument(Graphics2D g2d, double low_limit, double max_limit, String label_instrument, boolean reading_present, double reading)
{
   // eg: https://www.google.nl/search?q=barometer&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjJ78PZg8_UAhWEJMAKHZA3AsAQ_AUICigB&biw=1920&bih=950#imgrc=hE2asDauQrhr6M:
   double reading_abs;                                      // needed for displaying negative values (the "-" sign is not part of the lcd font set)
   
   
   // main instument area   
   //
   g2d.setPaint(color_instrument_face);
   g2d.fill(new RoundRectangle2D.Double(-instrument_width / 2, -instrument_height, instrument_width, instrument_height, 10, 10));   
   
   // ring of the instrument face
   //
   g2d.setPaint(color_instrument_ring);
   g2d.setStroke(new BasicStroke(instrument_ring_thickness));
   g2d.draw(new RoundRectangle2D.Double(-instrument_width / 2, -instrument_height, instrument_width, instrument_height, 10, 10));   
   
   // contour line of the instrument 
   //
   g2d.setPaint(color_instrument_contour);
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(new RoundRectangle2D.Double(-instrument_width / 2, -instrument_height, instrument_width, instrument_height, 10, 10));   

   
   // name of the meter
   //
   g2d.setFont(font_d);
   g2d.setColor(color_label_text);
   int width_d1 = g2d.getFontMetrics().stringWidth(label_instrument);
   int height_d1 = g2d.getFontMetrics().getHeight();
   int x_text =  - (width_d1 / 2);   // from the middle of the instrument 
   //int y_text = height_d1 / 4;
   int y_text = -height_d1 + 3;// +3 for a better fit (experimental defined)
   g2d.drawString(label_instrument, x_text , y_text);
   
   if (reading_present)        
   {
      //reading = main_RS232_RS422.dashboard_double_last_update_record_sst;    
      ////// TEST ////  
      //reading = 6.5;
      //// TEST //////
      
      g2d.setFont(font_b);
      g2d.setColor(color_digits);
      
      if (reading >= low_limit && reading <= max_limit)
      {
         reading_abs = Math.abs(reading);              // the "-" sign is not correct displayed in de LCD font set (need a trick)
         
         
         String digits = "";
         
         // wind dir and speed displayed as eg 350 and not 350.0 (wind dir and wind speed and gust were already rounded)
         if (label_instrument.contains("Wind"))
         {
            digits = Integer.toString((int)reading_abs);
         }
         else
         {
            digits = Double.toString(reading_abs);
         }
         
         // wind dir always with 3 chars
         if (label_instrument.contains("Wind dir"))
         {
            if (digits.length() == 1)
            {
               digits = "00" + digits;
            }
            else if (digits.length() == 2)
            {
               digits = "0" + digits;
            }
         }
         
         if (reading < 0.0)
         {
            digits = "_" + digits;                        // to add the "-" sign with a trick
         }
         
         
         //String digits = Double.toString(reading);
         int width_b1 = g2d.getFontMetrics().stringWidth(digits);
         int height_b1 = g2d.getFontMetrics().getHeight();
         
         int x_digits = - (width_b1/ 2);
         //int y_digits = -(int)instrument_height / 2 + (height_b1 / 2) - (int)instrument_ring_thickness;
         int y_digits = - (height_b1 / 2);               

         g2d.drawString(digits, x_digits, y_digits);
      }   
   } // if (main.air_temp_from_AWS_present)   

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
}


   private final Color color_black;
   private final Color color_red;
   private final Color color_gray;
   private final Color color_dark_red;
   private final Color color_instrument_face;
   private final Color color_instrument_ring;
   private final Color color_instrument_contour;
   private final Color color_label_text;
   private Color color_last_update;
   private Color color_update_message_south_panel;
   private Color color_digits;
   private Font font_b;
   private Font font_d;   
   private Font font_e;
   private int horz_offset;
   private int fontSize_b;
   private int fontSize_d;
   private int fontSize_e;
   private int instrument_width;
   private int instrument_height;
   private float instrument_ring_thickness;
   public static final String digital_font = "LCD-N___.TTF";
   private String text = "test";
}
