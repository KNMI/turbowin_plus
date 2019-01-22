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
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author marti
 */
public class DASHBOARD_grafiek_AWS_hybrid extends JPanel {

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public DASHBOARD_grafiek_AWS_hybrid()
{ 
   //color_white                 = Color.WHITE;
   color_black                 = Color.BLACK;
   color_gray                  = Color.GRAY;
   //color_dark_red              = new Color(190, 0, 0);                     // for main digits
   //color_label_text            = Color.GRAY;
   color_red                   = Color.RED;                                // for main digits
   color_inserted_data         = Color.BLUE;
   //color_contour_block        = color_inserted_data;                      //Color.DARK_GRAY;
   block_contour_thickness     = 1.0f;
   color_block_face            = new Color(179, 242, 255);                 // Celeste
   color_wind_rose_ring        = Color.DARK_GRAY;
   color_wind_arrow_actual     = Color.YELLOW;
   color_wind_arrow_max        = new Color(224, 224, 224, 120);            //Color.LIGHT_GRAY;
   color_wind_rose_additional  = Color.BLACK;//Color.YELLOW;               // BF circles + labels; N,W,E,S chars
   color_wind_rose_background  = new Color(0, 191, 255, 255);              // light blue
           
   // https://www.rapidtables.com/web/color/RGB_Color.html
   //color_deck = new Color(176,196,222, 255);                                  // light steel blue (alpha 255 = 100% opaque)
   //color_bridge_container_ship = new Color(245,255,250);                                     // mint cream
   //int alpha_container = 128;
   //color_container_1           = new Color(255, 51, 51, alpha_container);              // red      
   //color_container_2           = new Color(178, 34, 34, alpha_container);              // firebrick red
   //color_container_3           = new Color(240,128,128, alpha_container);              // light coral red
   //color_container_4           = new Color(70,130,180, alpha_container);               // steel blue
   //color_container_5           = new Color(30,144,255, alpha_container);               // dodger blue
   //color_container_6           = new Color(65,105,225, alpha_container);               // royal blue
   //color_container_7           = new Color(255,245,238, alpha_container);              // sea shell white
   //color_container_8           = new Color(245,255,250, alpha_container);              // mint crean white
   //color_container_9           = new Color(244,164,96, alpha_container);               // sandy brown
   //color_container_10          = new Color(255,127,80, alpha_container);               // coral red
   //color_bulk_carrier          = new Color(204, 69, 50, 255);                 // red-brown
   //color_bridge_oil_tanker     = Color.GRAY;                           
   //color_deck_oil_tanker       = new Color(0, 100, 70);                       // green-blue
   //color_pipes_oil_tanker      = Color.LIGHT_GRAY;   
   //color_lng_tanks             = Color.RED;
   //color_deck_lng_tanker       = Color.LIGHT_GRAY;  
   //color_life_boat             = new Color(255, 69, 0);                       // orange-red
   //color_deck_passenger_ship   = new Color(205,133,63);  
   //color_acc_passenger_ship    = new Color(255, 218, 185);
   //color_pool_1                = new Color(100, 149, 237);                    // sarboard and aft swimming pool edge; color between inner swimming pool and outer pool; for depth effect
   //color_pool_2                = new Color(0, 0, 205);                        // inner swimming pool (pool bottom)
   //color_pool_3                = new Color(0, 255, 255, 100);                 // outer swimming pool (covering pool bottom and edges (semi transparent)
   //color_funnel_passenger_ship = Color.LIGHT_GRAY;
   
   
   
   
   // get the screen size 
   //
   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   double width_screen = screenSize.getWidth();
   double height_screen = screenSize.getHeight();
   System.out.println("--- Screen resolution AWS Dashboard hybrid: " + width_screen + " x " + height_screen);

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
  
   boolean display_icon = false;
   double wind_rose_block_margin = DASHBOARD_view_AWS_hybrid.width_AWS_hybrid_dashboard / 30.0;        // 20// left central block
   double temp_block_margin = wind_rose_block_margin;                                                  // right central block
   
   
   // always call the super's method to clean "dirty" pixels
   super.paintComponent(g);
   
   // general
   final Graphics2D g2d = (Graphics2D) g;
   setAllRenderingHints(g2d);
   
   
   // font size depending the dimensions of the top block (last updated block/label/text)
   //
   //int hulp_font_block_last_update_width = DASHBOARD_view_AWS_hybrid.width_AWS_hybrid_dashboard / 6;  // NB 
   //int hulp_font_block_last_update_height = hulp_font_block_last_update_width / 3;
   
   //int hulp_font = DASHBOARD_view_AWS_hybrid.width_AWS_hybrid_dashboard / 15;  // NB 
   
   
   
   //fontSize_b = (int)(block_last_update_height - (2 * instrument_ring_thickness) -3);         // measured value  (eg 1002.3)               // -5 for just a slightly better fit
   //fontSize_b = (int)(hulp_font);
   //fontSize_d = hulp_font / 7;                                                         // parameter text (eg "Temp [deg]")
   //fontSize_e = (int)(1.5 * hulp_font / 7);                                            // last updated text 

   int fontSize_a = 8;                                                                   // units (eg 1100) wind rose
   int fontSize_c = 10;                                                                  // Bf labels
   int fontSize_d = 18;
   int fontSize_e = DASHBOARD_view_AWS_hybrid.width_AWS_hybrid_dashboard / 70; 
   
   
   font_a = new Font("SansSerif", Font.PLAIN, fontSize_a);                              // units (eg 1100)
   font_b = new Font("Monospaced", Font.BOLD, fontSize_d);                               // N, E, S, W
   font_c = new Font("SansSerif", Font.PLAIN, fontSize_c);                              // Bf labels
   //font_d = new Font("Monospaced", Font.ITALIC, fontSize_d);                            // text's like "PRESSURE MSL", "TEMPERATURE" etc
   font_e = new Font("Dialog", Font.PLAIN, fontSize_e);                                 // test top block ("Measured at: ") and bottom block ("visual observation possible in 32 minutes")
   //font_f = new Font("Dialog", Font.PLAIN, (int)(fontSize_e / 1.5));                    // labels and inserted data of the measured parameters
   font_f = new Font("Dialog", Font.PLAIN, (int)(fontSize_e / 1.2));                    // labels and inserted data of the measured parameters
   font_c = new Font("SansSerif", Font.PLAIN, fontSize_c);                              // units (eg 1100)
  
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////
   // background image(not in night vision mode)
   //
/*   
   if (DASHBOARD_view_AWS_hybrid.night_vision == false)
   {
      //Image img1 = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + main.DASHBOARD_LOGO)).getImage();
      Image img1 = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "waves_desktop.png")).getImage();
      
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
*/  
   //setBackground(Color.LIGHT_GRAY);
   
   // colors depending night or day vision
   if (DASHBOARD_view_AWS_hybrid.night_vision == true)
   {
      color_block_face = color_red;
      color_contour_block = color_black;
      color_digits = color_black;
      color_last_update = color_black; //color_dark_red;   // " measured_at" in case hybrid / Meteo France dashboard
      color_update_message_south_panel = color_gray;
   }
   else // day vision
   {
      color_block_face = new Color(179, 242, 255); 
      color_contour_block = color_inserted_data;
      color_digits = color_inserted_data;
      color_last_update = color_black; //color_red;
      color_update_message_south_panel = color_black;
   }
   
   // obsolete data (no cummunication for roughly > 2 minutes with the AWS)
   if (main.displayed_aws_data_obsolate == true)   // valid for analog and digital dashboard
   {
      color_digits = main.obsolate_color_data_from_aws;                 // inserted data e.g. 12.3 C
      //color_last_update = main.obsolate_color_data_from_aws;
      color_last_update = color_black;
      
      main_RS232_RS422.RS422_initialise_AWS_Sensor_Data_For_Display();  // NB see also: main_RS232_RS422.MAX_AGE_AWS_DATA
   }
   
   
   //////////////////////////////////////////////// TOP BLOCK (LAST UPDATED) ///////////////////////////////////////////////////////////////
   
   // set new origin
   g2d.translate(getWidth() / 2, getHeight() / 12);                       // getHeight()/12 -> top screen
   
   //
   ///////////////////// "measured at" label in top screen
   //
   String updated_text = "Measured at: ";
   String update_message = "";
   final double height_measured_at;
   final double width_measured_at;
   
   Image icon_date_time = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "date_time.png")).getImage();
   int icon_width = icon_date_time.getWidth(this);
   int icon_height = icon_date_time.getHeight(this);
   int total_horiz_space_arround_icon = icon_width;
   
   
   g2d.setFont(font_e);
      
   
   if ((main.date_from_AWS_present == false) || (main.time_from_AWS_present == false))
   {
      update_message =  updated_text + " -";
      FontRenderContext context_datum_tijd_parameter = g2d.getFontRenderContext();
      Rectangle2D bounds_datum_tijd_updated = font_e.getStringBounds(update_message, context_datum_tijd_parameter);
      height_measured_at = -bounds_datum_tijd_updated.getY();     // height char's, this is now a positve value e.g. 33.4 
      width_measured_at = bounds_datum_tijd_updated.getWidth() + icon_width + total_horiz_space_arround_icon;   // length string
   }
   else if (main_RS232_RS422.dashboard_string_last_update_record_date.equals("") || main_RS232_RS422.dashboard_string_last_update_record_time.equals(""))
   {
      update_message =  updated_text + " unknown";
      FontRenderContext context_datum_tijd_parameter = g2d.getFontRenderContext();
      Rectangle2D bounds_datum_tijd_updated = font_e.getStringBounds(update_message, context_datum_tijd_parameter);
      height_measured_at = -bounds_datum_tijd_updated.getY();     // height char's, this is now a positve value e.g. 33.4 
      width_measured_at = bounds_datum_tijd_updated.getWidth() + icon_width + total_horiz_space_arround_icon;   // length string
   }
   else
   {
      //update_message = updated_text + " " + main_RS232_RS422.dashboard_string_last_update_record_date_time.replace("-", " ");
      update_message = updated_text + " " + main_RS232_RS422.dashboard_string_last_update_record_date + "  " + main_RS232_RS422.dashboard_string_last_update_record_time + " UTC";
      FontRenderContext context_datum_tijd_parameter = g2d.getFontRenderContext();
      Rectangle2D bounds_datum_tijd_updated = font_e.getStringBounds(update_message, context_datum_tijd_parameter);
      height_measured_at = -bounds_datum_tijd_updated.getY();     // height char's                     
      width_measured_at = bounds_datum_tijd_updated.getWidth() + icon_width + total_horiz_space_arround_icon;   // length string
   }
   
   if (height_measured_at > icon_height / 2)
   {
      display_icon = true;
   }
      
   
   double block_height_measured_at = 4 * height_measured_at;  // also be used by other blocks!!
   
   //System.out.println("+++ y_top_label = " + y_top_label);        // eg -66.4
   //System.out.println("+++ y_bottom_label = " + y_bottom_label);  // eg 66.4
   //System.out.println("+++ height_measured_at = " + height_measured_at);  
   //System.out.println("+++ icon-height = " + icon_height);        // eg 24
   
   // NB drawing of the TOP block after drawing of the left and right central blocks (because in that case after smaller scaling the top block will be still visible over the central blocks)
   






    
   
   /////////////////////////////////////////////// LEFT BLOCK (WIND ROSE) ///////////////////////////////////////////////////////////////
   //
   g2d.setFont(font_f);
   
   // set new origin
   g2d.translate(-getWidth() / 2, -getHeight() / 12);                            // return to original origin
   g2d.translate(getWidth() / 4, getHeight() / 2);                               // origin is now center left block               
   //System.out.println("+++ left block: screen getWidth = " + getWidth()); 
   //System.out.println("+++ left block: screen getHeight = " + getHeight()); 
   
   //System.out.println("+++ left block: DASHBOARD_view_AWS_hybrid.width_AWS_hybrid_dashboard = " + DASHBOARD_view_AWS_hybrid.width_AWS_hybrid_dashboard); 
   //System.out.println("+++ left block: DASHBOARD_view_AWS_hybrid.height_AWS_hybrid_dashboard = " + DASHBOARD_view_AWS_hybrid.height_AWS_hybrid_dashboard); 
   //System.out.println("+++ left block: block_height_last_updated = " + block_height_last_updated);
   
   // paint left block (relative to new origin !!)
   double block_wind_rose_width = (DASHBOARD_view_AWS_hybrid.width_AWS_hybrid_dashboard / 2 - (2 * wind_rose_block_margin));  
   double block_wind_rose_height = (DASHBOARD_view_AWS_hybrid.height_AWS_hybrid_dashboard - block_height_measured_at * 2);
   double x_left_block_wind_rose = -block_wind_rose_width / 2;
   double y_top_block_wind_rose = -block_wind_rose_height / 2;
           
   //System.out.println("+++ left block: x_left_block_wind_rose = " + x_left_block_wind_rose); 
   //System.out.println("+++ left block: block_wind_rose_width = " + block_wind_rose_width); 
   //System.out.println("+++ left block: y_top_block_wind_rose = " + y_top_block_wind_rose); 
   //System.out.println("+++ left block: block_wind_rose_height = " + block_wind_rose_height); 
   
   // fill block
   g2d.setPaint(color_block_face);
   g2d.fill(new RoundRectangle2D.Double(x_left_block_wind_rose, y_top_block_wind_rose, block_wind_rose_width, block_wind_rose_height, 20, 20));   
   
   // contour block
   g2d.setPaint(color_contour_block);
   g2d.setStroke(new BasicStroke(block_contour_thickness));
   g2d.draw(new RoundRectangle2D.Double(x_left_block_wind_rose, y_top_block_wind_rose, block_wind_rose_width, block_wind_rose_height, 20, 20));
   
   // Latitude, logitude speed, Course, Heading text
   double sub_block_wind_rose_width = block_wind_rose_width / 8.0;
   double x_latitude_text = - (2 * sub_block_wind_rose_width);
   double x_longitude_text = - (1 * sub_block_wind_rose_width);
   double x_speed_text = 0;
   double x_course_text = + (1 * sub_block_wind_rose_width);
   double x_heading_text = + (2 * sub_block_wind_rose_width);
   
   double y_position_text = -(block_wind_rose_height / 2.0) + (height_measured_at);      // bottom of the text string itself
   
   g2d.setColor(color_black);
   g2d.drawString("Lat", (int)x_latitude_text, (int)y_position_text);   
   g2d.drawString("Long", (int)x_longitude_text, (int)y_position_text); 
   g2d.drawString("Speed", (int)x_speed_text, (int)y_position_text); 
   g2d.drawString("Course", (int)x_course_text, (int)y_position_text); 
   g2d.drawString("Heading", (int)x_heading_text, (int)y_position_text); 
   
   // measured data (updated every minute)
   g2d.setColor(color_digits);
   double y_position_data = -(block_wind_rose_height / 2.0) + (2 * height_measured_at);      // bottom of the text string itself
   
   // position icon
   if (display_icon)
   {
      Image icon_position = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "position.png")).getImage();  
      
      // NB double y_icon = 0;               -> top icon is bottom label !!!!
      // NB double y_icon = y_top_label;     -> top icon is top label !!!!
      // NB double y_icon = y_text_label;    -> top icon is bottom text string
      double y_icon_pos = y_position_data - icon_height;      // eg -22.1 - 24 // NB icon height was already defined in the begin of this function
      double x_icon_pos = - (3 * sub_block_wind_rose_width);
      
      g2d.drawImage(icon_position, (int)x_icon_pos + total_horiz_space_arround_icon / 2, (int)y_icon_pos, this); // NB The x,y location specifies the position for the top-left of the image.
   } // if (display_icon)
   
   
   // insert latitude
   //
   g2d.drawString("", (int)x_latitude_text, (int)y_position_data);
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
         g2d.drawString(reading_latitude, (int)x_latitude_text, (int)y_position_data);
      }
   }
   else
   {
      g2d.drawString("-", (int)x_latitude_text, (int)y_position_data);
   }
   
   // insert longitude
   //
   g2d.drawString("", (int)x_longitude_text, (int)y_position_data);
   if (main.longitude_from_AWS_present)
   {
      String reading_longitude = main_RS232_RS422.dashboard_string_last_update_record_longitude;         
      
      int pos = reading_longitude.indexOf("°", 0);
      
      int reading_int_longitude_degrees = Integer.MAX_VALUE;
      try
      {
         reading_int_longitude_degrees = Integer.parseInt(reading_longitude.substring(0, pos));   // eg 142°12'N ->  reading_longitude_degrees = 142
      }
      catch (NumberFormatException ex)
      {
         reading_int_longitude_degrees = Integer.MAX_VALUE;        
      }
      
      if (reading_int_longitude_degrees >= 0 && reading_int_longitude_degrees <= 180)
      {
         reading_longitude = reading_longitude.replaceAll("\\s","");            // skip all the spaces in the latitude string
         g2d.drawString(reading_longitude, (int)x_longitude_text, (int)y_position_data);
      }
   }
   else
   {
      g2d.drawString("-", (int)x_longitude_text, (int)y_position_data);
   }   
   
   // insert speed (SOG)
   //
   g2d.drawString("", (int)x_speed_text, (int)y_position_data);                       // clear SOG insert field
   if (main.SOG_from_AWS_present)
   {
      String reading_SOG = main_RS232_RS422.dashboard_string_last_update_record_SOG;
      
      double reading_double_speed = Double.MAX_VALUE;
      try
      {
         reading_double_speed = Double.parseDouble(reading_SOG);   
      }
      catch (NumberFormatException ex)
      {
         reading_double_speed = Double.MAX_VALUE;        
      }
        
      if (reading_double_speed >= 0.0 && reading_double_speed <= 50.0) 
      {
         String digits = reading_SOG + " kts";
         g2d.drawString(digits, (int)x_speed_text, (int)y_position_data); 
      }   
   } // if (main.SOG_from_AWS_present)
   else
   {
      g2d.drawString("-", (int)x_speed_text, (int)y_position_data); 
   }
   
   
   // NB main_RS232_RS422.dashboard_string_last_update_record_COG;    // [already roundend  - 360]
   // NB main_RS232_RS422.dashboard_string_last_update_record_heading // [already roundend  - 360]
   
 
   // insert course (COG)
   //
   int reading_int_COG = Integer.MAX_VALUE;
   boolean reading_COG_ok = false;                                                        // for alligning of the ship in the wind rose
   g2d.drawString("", (int)x_course_text, (int)y_position_data);                          // clear SOG insert field
   if (main.COG_from_AWS_present)
   {
      String reading_COG = main_RS232_RS422.dashboard_string_last_update_record_COG;
      
      //int reading_int_COG = Integer.MAX_VALUE;
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
         String digits = reading_COG + "°";
         g2d.drawString(digits, (int)x_course_text, (int)y_position_data); 
         reading_COG_ok = true;
      }   
   } // if (main.COG_from_AWS_present)
   else
   {
      g2d.drawString("-", (int)x_course_text, (int)y_position_data); 
   }
   
   // insert Heading
   //
   int reading_int_heading = Integer.MAX_VALUE;
   boolean reading_heading_ok = false;                                                     // for alligning of the ship in the wind rose
   g2d.drawString("", (int)x_heading_text, (int)y_position_data);                          // clear SOG insert field
   if (main.true_heading_from_AWS_present)
   {
      String reading_heading = main_RS232_RS422.dashboard_string_last_update_record_heading;
      
      //int reading_int_heading = Integer.MAX_VALUE;
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
         String digits = reading_heading + "°";
         g2d.drawString(digits, (int)x_heading_text, (int)y_position_data); 
         reading_heading_ok = true;
      }   
   } // if (main.true_heading_from_AWS_present)
   else
   {
      g2d.drawString("-", (int)x_heading_text, (int)y_position_data);
   }
   
   
   // wind rose
   //
   double wind_rose_diameter = 0.0;
   if (block_wind_rose_width < block_wind_rose_height)
   {
      wind_rose_diameter = block_wind_rose_width / 1.5;
   }
   else
   {
      wind_rose_diameter = block_wind_rose_height / 1.5; 
   }
   
   // for wind rose and wind arrow
   double marker_circle_diameter_1   = wind_rose_diameter - 26;                  // eg outside marker circle: 220 - 26 = 194; [most 'outside' outside marker circle]
   double marker_circle_diameter_2   = marker_circle_diameter_1 - 10;            // eg inside marker circle: 194 - 10 = 184; [most 'inside' outside marker circle]
   double bf_per_class_radius = (marker_circle_diameter_2 / 2) / 12;
   
   // wind rose itself
   //
   draw_wind_rose(g2d, wind_rose_diameter, marker_circle_diameter_1, marker_circle_diameter_2, bf_per_class_radius);
   
   // ship in wind rose
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
   //reading_int_course = 360;
   //course_ok = true;
   //main.ship_type_dashboard = main.NEUTRAL_SHIP;
   //main.ship_type_dashboard = main.PASSENGER_SHIP;
   //main.ship_type_dashboard = main.OIL_TANKER;
   //main.ship_type_dashboard = main.CONTAINER_SHIP;
   //main.ship_type_dashboard = main.LNG_TANKER;
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
         case main.GENERAL_CARGO_SHIP : main.myship.draw_general_cargo_ship(g2d, wind_rose_diameter, DASHBOARD_view_AWS_hybrid.night_vision); break;
         case main.CONTAINER_SHIP : main.myship.draw_container_ship(g2d, wind_rose_diameter, DASHBOARD_view_AWS_hybrid.night_vision); break;
         case main.BULK_CARRIER   : main.myship.draw_bulk_carrier(g2d, wind_rose_diameter); break;
         case main.OIL_TANKER     : main.myship.draw_oil_tanker(g2d, wind_rose_diameter); break;
         case main.LNG_TANKER     : main.myship.draw_lng_tanker(g2d, wind_rose_diameter); break;
         case main.PASSENGER_SHIP : main.myship.draw_passenger_ship(g2d, wind_rose_diameter); break;
         case main.NEUTRAL_SHIP   : main.myship.draw_neutral_ship(g2d, wind_rose_diameter, DASHBOARD_view_AWS_hybrid.night_vision); break;
         default                  : main.myship.draw_container_ship(g2d, wind_rose_diameter, DASHBOARD_view_AWS_hybrid.night_vision); break;
      } // switch (main.ship_type_dashboard)
      
      // reset drawing orientation
      if (reading_int_course >= 1 && reading_int_course <= 360)    
      {
         g2d.rotate(Math.toRadians(-reading_int_course));  // what follows will be rotated -xxx degrees
      }
      
   } // if (course_ok)
   
   
   // True wind 'arrow'
   //
   draw_wind_arrow(g2d, marker_circle_diameter_2, bf_per_class_radius);
   
   
   
   
   
   
   
   /////////////////////////////////////////////// RIGHT BLOCK (PRESSURE, AIR TEMP ETC.) ///////////////////////////////////////////////////////////////
   g2d.setFont(font_f);
   
   g2d.translate(-getWidth() / 4, -getHeight() / 2);                          // return to original origin
   g2d.translate(3 * (getWidth() / 4), getHeight() / 2);                      // origin is now center right block
   
   // paint left block (relative to new origin !!)
   //
   double block_temp_width = (int)(DASHBOARD_view_AWS_hybrid.width_AWS_hybrid_dashboard / 2 - (2 * temp_block_margin));  
   double block_temp_height = (int)(DASHBOARD_view_AWS_hybrid.height_AWS_hybrid_dashboard - block_height_measured_at * 2);
   double x_left_block_temp = -block_temp_width / 2;
   double y_top_block_temp = -block_temp_height / 2;
   
   // fill block
   g2d.setPaint(color_block_face);
   g2d.fill(new RoundRectangle2D.Double(x_left_block_temp, y_top_block_temp, block_temp_width, block_temp_height, 20, 20));       
   
   // contour block
   g2d.setPaint(color_contour_block);
   g2d.setStroke(new BasicStroke(block_contour_thickness));
   g2d.draw(new RoundRectangle2D.Double(x_left_block_temp, y_top_block_temp, block_temp_width, block_temp_height, 20, 20));

   
   //
   ////////////// Sea level pressure, Barometric tendency ////////////////////
   //
   
   // Sea level pressure, Barometric tendency text label
   double sub_block_temp_width = block_temp_width / 4.0;
   double x_mslp_text = - (1 * sub_block_temp_width);
   double x_tendency_text = - (0 * sub_block_temp_width);
  
   double y_pressure_text = -(block_temp_height / 2.0) + (1 * height_measured_at);      // bottom of the text string itself
   
   g2d.setColor(color_black);
   g2d.drawString("Sea level pressure", (int)x_mslp_text, (int)y_pressure_text);   
   g2d.drawString("Barometric tendency", (int)x_tendency_text, (int)y_pressure_text); 
   
   // measured pressure data updated every minute
   g2d.setColor(color_digits);
   double y_pressure_data = -(block_temp_height / 2.0) + (2 * height_measured_at);      // bottom of the text string itself
   
   // icon
   //
   if (display_icon)
   {
      Image icon_pressure = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "barometer.png")).getImage();  
      
      // NB double y_icon = 0;               -> top icon is bottom label !!!!
      // NB double y_icon = y_top_label;     -> top icon is top label !!!!
      // NB double y_icon = y_text_label;    -> top icon is bottom text string
      double y_icon_pressure = y_pressure_data - icon_height;      // eg -22.1 - 24 // NB icon height was already defined in the begin of this function
      double x_icon_pressure = - (2 * sub_block_temp_width);
      
      g2d.drawImage(icon_pressure, (int)x_icon_pressure + total_horiz_space_arround_icon / 2 + (int)(sub_block_temp_width / 2), (int)y_icon_pressure, this); // NB The x,y location specifies the position for the top-left of the image.
   } // if (display_icon)

   
   // insert air pressure reading mslp
   //
   g2d.drawString("", (int)x_mslp_text, (int)y_pressure_data);
   if (main.pressure_MSL_from_AWS_present)
   {
      double reading_mslp = main_RS232_RS422.dashboard_double_last_update_record_MSL_pressure_ic;         // see: RS422_update_AWS_dasboard_values()
      if (reading_mslp > 900.0 && reading_mslp < 1100.0)
      {
         String digits = Double.toString(reading_mslp) + " hPa";
         g2d.drawString(digits, (int)x_mslp_text, (int)y_pressure_data);
      }
   }
   else
   {
      g2d.drawString("-", (int)x_mslp_text, (int)y_pressure_data);
   }
   
   
   // insert air pressure tendency (and charactristic)
   //
   g2d.drawString("", (int)x_tendency_text, (int)y_pressure_data);
   if (main.pressure_tendency_from_AWS_present)
   {
        
      double tendency_reading = main_RS232_RS422.dashboard_double_last_update_record_pressure_tendency; // see: RS422_update_AWS_dasboard_values()
      if (tendency_reading >= -99.9 && tendency_reading <= 99.9)
      {
         String digits = Double.toString(tendency_reading) + " hPa / 3 hrs";
         g2d.drawString(digits, (int)x_tendency_text, (int)y_pressure_data);  
      }
   } 
   else
   {
      g2d.drawString("-", (int)x_tendency_text, (int)y_pressure_data);
   }
   
   
   //
   ////////// Temperature and SST /////////////
   //
   
   // Temperature text label
   double x_air_temp_text = - (1 * sub_block_temp_width);
   double x_sst_text = - (0 * sub_block_temp_width);
  
   double y_temp_text = -(block_temp_height / 2.0) + (5 * height_measured_at);      // bottom of the text string itself
   
   g2d.setColor(color_black);
   g2d.drawString("Air temperature", (int)x_air_temp_text, (int)y_temp_text);   
   g2d.drawString("Sea surface temperature", (int)x_sst_text, (int)y_temp_text); 

   // measured temp data updated every minute
   g2d.setColor(color_digits);
   double y_temp_data = -(block_temp_height / 2.0) + (6 * height_measured_at);      // bottom of the text string itself
   
   // icon
   //
   if (display_icon)
   {
      Image icon_temp = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "temperatures.png")).getImage();  
      
      // NB double y_icon = 0;               -> top icon is bottom label !!!!
      // NB double y_icon = y_top_label;     -> top icon is top label !!!!
      // NB double y_icon = y_text_label;    -> top icon is bottom text string
      double y_icon_temp = y_temp_data - icon_height;      // eg -22.1 - 24 // NB icon height was already defined in the begin of this function
      double x_icon_temp = - (2 * sub_block_temp_width);
      
      g2d.drawImage(icon_temp, (int)x_icon_temp + total_horiz_space_arround_icon / 2 + (int)(sub_block_temp_width / 2), (int)y_icon_temp, this); // NB The x,y location specifies the position for the top-left of the image.
   } // if (display_icon)
   
   
   // insert sst
   //
   g2d.drawString("", (int)x_sst_text, (int)y_temp_data); 
   if (main.SST_from_AWS_present)
   {
      double sst_reading = main_RS232_RS422.dashboard_double_last_update_record_sst;
      if (sst_reading > -60.0 && sst_reading < 60.0) 
      {
         String digits = Double.toString(sst_reading) + " °C";
         g2d.drawString(digits, (int)x_sst_text, (int)y_temp_data);   
      }   
   } // if (main.SST_from_AWS_present)
   else
   {
      g2d.drawString("-", (int)x_sst_text, (int)y_temp_data);
   }
   
   
   // insert air temp
   //
   g2d.drawString("", (int)x_air_temp_text, (int)y_temp_data);
   if (main.air_temp_from_AWS_present)
   {
      double air_temp_reading = main_RS232_RS422.dashboard_double_last_update_record_air_temp;      // see: RS422_update_AWS_dasboard_values()
      if (air_temp_reading > -60.0 && air_temp_reading < 60.0) 
      {
         String digits = Double.toString(air_temp_reading) + " °C";
         g2d.drawString(digits, (int)x_air_temp_text, (int)y_temp_data);   
      }   
   } // if (main.air_temp_from_AWS_present)
   else
   {
      g2d.drawString("-", (int)x_air_temp_text, (int)y_temp_data);
   }
  
   //
   ////////////////// Humidity /////////////////
   //
   
   // Humidity text label
   //double sub_block_temp_width = block_temp_width / 4.0;
   double x_humidity_text = - (1 * sub_block_temp_width);
  
   double y_humidity_text = -(block_temp_height / 2.0) + (9 * height_measured_at);      // bottom of the text string itself
   
   g2d.setColor(color_black);
   g2d.drawString("Humidity", (int)x_humidity_text, (int)y_humidity_text);   
   
   // measured temp data updated every minute
   g2d.setColor(color_digits);
   double y_humidity_data = -(block_temp_height / 2.0) + (10 * height_measured_at);      
   
   // icon
   //
   if (display_icon)
   {
      Image icon_humidity = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "humidity.png")).getImage();  
      
      // NB double y_icon = 0;               -> top icon is bottom label !!!!
      // NB double y_icon = y_top_label;     -> top icon is top label !!!!
      // NB double y_icon = y_text_label;    -> top icon is bottom text string
      double y_icon_humidity = y_humidity_data - icon_height;      // eg -22.1 - 24 // NB icon height was already defined in the begin of this function
      double x_icon_humidity = - (2 * sub_block_temp_width);
      
      g2d.drawImage(icon_humidity, (int)x_icon_humidity + total_horiz_space_arround_icon / 2 + (int)(sub_block_temp_width / 2), (int)y_icon_humidity, this); // NB The x,y location specifies the position for the top-left of the image.
   } // if (display_icon)
   
   
   // insert relative humidity
   //
   g2d.drawString("", (int)x_humidity_text, (int)y_humidity_data);
   if (main.rh_from_AWS_present)
   {
        
      double rh_reading = main_RS232_RS422.dashboard_double_last_update_record_humidity;
      if (rh_reading >= 0.0 && rh_reading <= 105.0)
      {
         String digits = Double.toString(rh_reading) + " %";
         g2d.drawString(digits, (int)x_humidity_text, (int)y_humidity_data);  
      }
   } 
   else
   {
      g2d.drawString("-", (int)x_humidity_text, (int)y_humidity_data);  
   }
   
   
   //
   ////////////////// Relative wind ////////////////
   //
   
   // relative wind text label
   double x_relative_wind_speed_text = - (1 * sub_block_temp_width);
   double x_relative_wind_dir_text = - (0 * sub_block_temp_width);
  
   double y_relative_wind_text = -(block_temp_height / 2.0) + (13 * height_measured_at);      // bottom of the text string itself
   
   g2d.setColor(color_black);
   g2d.drawString("Relative wind spd", (int)x_relative_wind_speed_text, (int)y_relative_wind_text);   
   g2d.drawString("Relative wind direction", (int)x_relative_wind_dir_text, (int)y_relative_wind_text); 
   
   // measured relative wind data updated every minute
   g2d.setColor(color_digits);
   double y_relative_wind_data = -(block_temp_height / 2.0) + (14 * height_measured_at);      // bottom of the text string itself
   
   // icon
   //
   if (display_icon)
   {
      Image icon_relative_wind = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "wind.png")).getImage();  
      
      // NB double y_icon = 0;               -> top icon is bottom label !!!!
      // NB double y_icon = y_top_label;     -> top icon is top label !!!!
      // NB double y_icon = y_text_label;    -> top icon is bottom text string
      double y_icon_relative_wind = y_relative_wind_data - icon_height;      // eg -22.1 - 24 // NB icon height was already defined in the begin of this function
      double x_icon_relative_wind = - (2 * sub_block_temp_width);
      
      g2d.drawImage(icon_relative_wind, (int)x_icon_relative_wind + total_horiz_space_arround_icon / 2 + (int)(sub_block_temp_width / 2), (int)y_icon_relative_wind, this); // NB The x,y location specifies the position for the top-left of the image.
   } // if (display_icon)
   
   
   // insert relative wind speed
   //
   g2d.drawString("", (int)x_relative_wind_speed_text, (int)y_relative_wind_data); 
   if (main.relative_wind_speed_from_AWS_present)
   {
      int relative_wind_speed_reading = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_speed;
      if (relative_wind_speed_reading >= 0.0 && relative_wind_speed_reading <= 400.0) 
      {
         // so the digit indication is up to 400 kts/m/s and the analogue up to 110 kts/m/s
         String digits = "";
         if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)               // units is knots
         {
            digits = Integer.toString(relative_wind_speed_reading) + " kts";
         }
         else                                                                   // units is m/s                                                     
         {
            digits = Integer.toString(relative_wind_speed_reading) + " m/s";
         }
         
         g2d.drawString(digits, (int)x_relative_wind_speed_text, (int)y_relative_wind_data); 
      }
   } //if (main.relative_wind_speed_from_AWS_present)
   else
   {
      g2d.drawString("-", (int)x_relative_wind_speed_text, (int)y_relative_wind_data);         
   }

   
   // insert relative wind direction
   //
   g2d.drawString("", (int)x_relative_wind_dir_text, (int)y_relative_wind_data); 
   if (main.relative_wind_dir_from_AWS_present)
   {
      int relative_wind_dir_reading = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_dir;
      if (relative_wind_dir_reading >= 1 && relative_wind_dir_reading <= 360)       // dir = 0 should be impossible 
      {
         String digits = Integer.toString(relative_wind_dir_reading) + "°";
         g2d.drawString(digits, (int)x_relative_wind_dir_text, (int)y_relative_wind_data); 
      }
   } //if (main.relative_wind_dir_from_AWS_present)
   else
   {
      g2d.drawString("-", (int)x_relative_wind_dir_text, (int)y_relative_wind_data); 
   }
   

   //
   ///////////////////// True wind ///////////////
   //
   double x_true_wind_speed_text = - (1 * sub_block_temp_width);
   double x_true_wind_dir_text = - (0 * sub_block_temp_width);
  
   double y_true_wind_text = -(block_temp_height / 2.0) + (17 * height_measured_at);      // bottom of the text string itself
   
   g2d.setColor(color_black);
   g2d.drawString("True wind speed", (int)x_true_wind_speed_text, (int)y_true_wind_text);   
   g2d.drawString("True wind direction", (int)x_true_wind_dir_text, (int)y_true_wind_text); 
   
   // measured relative wind data updated every minute
   g2d.setColor(color_digits);
   double y_true_wind_data = -(block_temp_height / 2.0) + (18 * height_measured_at);      // bottom of the text string itself
   
   // icon
   //
   if (display_icon)
   {
      Image icon_true_wind = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "wind.png")).getImage();  
      
      // NB double y_icon = 0;               -> top icon is bottom label !!!!
      // NB double y_icon = y_top_label;     -> top icon is top label !!!!
      // NB double y_icon = y_text_label;    -> top icon is bottom text string
      double y_icon_true_wind = y_true_wind_data - icon_height;      // eg -22.1 - 24 // NB icon height was already defined in the begin of this function
      double x_icon_true_wind = - (2 * sub_block_temp_width);
      
      g2d.drawImage(icon_true_wind, (int)x_icon_true_wind + total_horiz_space_arround_icon / 2 + (int)(sub_block_temp_width / 2), (int)y_icon_true_wind, this); // NB The x,y location specifies the position for the top-left of the image.
   } // if (display_icon)
   
   
   // insert truee wind speed
   //
   g2d.drawString("", (int)x_true_wind_speed_text, (int)y_true_wind_data); 
   if (main.true_wind_speed_from_AWS_present)
   {
      int true_wind_speed_reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_speed; // NB could by kts or m/s
      if (true_wind_speed_reading >= 0.0 && true_wind_speed_reading <= 400.0) 
      {
         // so the digit indication is up to 400 kts/m/s and the analogue up to 110 kts/m/s
         String digits = "";
         if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)               // units is knots
         {
            digits = Integer.toString(true_wind_speed_reading) + " kts";
         }
         else                                                                   // units is m/s                                                     
         {
            digits = Integer.toString(true_wind_speed_reading) + " m/s";
         }
         
         g2d.drawString(digits, (int)x_true_wind_speed_text, (int)y_true_wind_data); 
      }
   } //if (main.true_wind_speed_from_AWS_present)
   else
   {
      g2d.drawString("-", (int)x_true_wind_speed_text, (int)y_true_wind_data);
   }
   
   
   // insert true wind direction
   //
   g2d.drawString("", (int)x_true_wind_dir_text, (int)y_true_wind_data); 
   if (main.true_wind_dir_from_AWS_present)
   {
      int true_wind_dir_reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_dir;
      if (true_wind_dir_reading >= 1 && true_wind_dir_reading <= 360)       // dir = 0 should be impossible 
      {
         String digits = Integer.toString(true_wind_dir_reading) + "°";
         g2d.drawString(digits, (int)x_true_wind_dir_text, (int)y_true_wind_data); 
      }
   } //if (main.true_wind_dir_from_AWS_present)
   else
   {
      g2d.drawString("-", (int)x_true_wind_dir_text, (int)y_true_wind_data);
   }
   
   
   //
   //////////////// True wind gust (max true wind) //////////////////
   //
   double x_true_gust_speed_text = - (1 * sub_block_temp_width);
   double x_true_gust_dir_text = - (0 * sub_block_temp_width);
  
   double y_true_gust_text = -(block_temp_height / 2.0) + (21 * height_measured_at);      // bottom of the text string itself
   
   g2d.setColor(color_black);
   g2d.drawString("Max true wind spd", (int)x_true_gust_speed_text, (int)y_true_gust_text);   
   g2d.drawString("Max true wind direction", (int)x_true_gust_dir_text, (int)y_true_gust_text); 
   
   // measured relative wind data updated every minute
   g2d.setColor(color_digits);
   double y_true_gust_data = -(block_temp_height / 2.0) + (22 * height_measured_at);      // bottom of the text string itself
   
   // icon
   //
   if (display_icon)
   {
      Image icon_true_gust = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "wind.png")).getImage();  
      
      // NB double y_icon = 0;               -> top icon is bottom label !!!!
      // NB double y_icon = y_top_label;     -> top icon is top label !!!!
      // NB double y_icon = y_text_label;    -> top icon is bottom text string
      double y_icon_true_gust = y_true_gust_data - icon_height;      // eg -22.1 - 24 // NB icon height was already defined in the begin of this function
      double x_icon_true_gust = - (2 * sub_block_temp_width);
      
      g2d.drawImage(icon_true_gust, (int)x_icon_true_gust + total_horiz_space_arround_icon / 2 + (int)(sub_block_temp_width / 2), (int)y_icon_true_gust, this); // NB The x,y location specifies the position for the top-left of the image.
   } // if (display_icon)
   
   
   // insert true wind gust speed
   //
   g2d.drawString("", (int)x_true_gust_speed_text, (int)y_true_gust_data);    // clear field
   if (main.true_wind_gust_from_AWS_present)
   {
      int true_wind_gust_speed_reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_gust; // NB could by kts or m/s
      if (true_wind_gust_speed_reading >= 0.0 && true_wind_gust_speed_reading <= 400.0) 
      {
         // so the digit indication is up to 400 kts/m/s and the analogue up to 110 kts/m/s
         String digits = "";
         if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)               // units is knots
         {
            digits = Integer.toString(true_wind_gust_speed_reading) + " kts";
         }
         else                                                                   // units is m/s                                                     
         {
            digits = Integer.toString(true_wind_gust_speed_reading) + " m/s";
         }
         
         g2d.drawString(digits, (int)x_true_gust_speed_text, (int)y_true_gust_data); 
      }
   } //if (main.true_wind_gust_speed_from_AWS_present)
   else
   {
      g2d.drawString("-", (int)x_true_gust_speed_text, (int)y_true_gust_data); 
   }
   
   
   // insert true wind gust dir
   //
   g2d.drawString("", (int)x_true_gust_dir_text, (int)y_true_gust_data);        // clear field
   if (main.true_wind_gust_dir_from_AWS_present)
   {
      int true_wind_gust_dir_reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_gust_dir; // NB could by kts or m/s
      if (true_wind_gust_dir_reading >= 1 && true_wind_gust_dir_reading <= 360)       // dir = 0 should be impossible 
      {
         String digits = Integer.toString(true_wind_gust_dir_reading) + "°";
         g2d.drawString(digits, (int)x_true_gust_dir_text, (int)y_true_gust_data); 
      }
   } //if (main.true_wind_gust_speed_from_AWS_present)
   else
   {
      g2d.drawString("-", (int)x_true_gust_dir_text, (int)y_true_gust_data); 
   }
   
   
   
   
   
   
   
   /////////////////////////////////////////////// BOTTOM BLOCK (VISUAL OBS POSSIBLE IN ?? MINUTES) ///////////////////////////////////////////////////////////////
   g2d.setFont(font_e);
   
   g2d.translate(-3 * (getWidth() / 4), -getHeight() / 2);                     // return to original origin
   g2d.translate(getWidth() / 2, getHeight() - (getHeight() / 12) + block_height_measured_at / 2); 
   
   // visual obs message based on VOT
   String visual_obs_message = "-";
   if (main.VOT_from_AWS_present)
   {
      if (main_RS232_RS422.dashboard_int_last_update_record_VOT >= 0 && main_RS232_RS422.dashboard_int_last_update_record_VOT < 100)
      {
         visual_obs_message = "Visual observation now possible";
         DASHBOARD_view_AWS_hybrid.jButton1.setEnabled(true);          // button: "make an visual observation"
      }
      else if (main_RS232_RS422.dashboard_int_last_update_record_VOT < 0)
      {
         visual_obs_message = "Visual observation possible in " + Integer.toString(Math.abs(main_RS232_RS422.dashboard_int_last_update_record_VOT)) + " minutes";
         DASHBOARD_view_AWS_hybrid.jButton1.setEnabled(false);          // button: "make an visual observation"
      }
      else // VOT >= 100 (eg harbour mode)
      {
         DASHBOARD_view_AWS_hybrid.jButton1.setEnabled(false);
      }
   } // if (main.VOT_from_AWS_present)
   else
   {
      DASHBOARD_view_AWS_hybrid.jButton1.setEnabled(false);          // button: "make an visual observation"
   }
   
   //String visual_obs_message = "Visual observation possible in ?? minutes";
   FontRenderContext context_visual_obs_parameter = g2d.getFontRenderContext();
   Rectangle2D bounds_visual_obs = font_e.getStringBounds(visual_obs_message, context_visual_obs_parameter);
   final double height_visual_obs = -bounds_visual_obs.getY();     // height char's                     
   final double width_visual_obs = bounds_visual_obs.getWidth() + icon_width + total_horiz_space_arround_icon;   // length string

   // "visual observation possible"- label NB EVERYTHING RELATIVE TO NEW ORIGIN !!!  
   double x_left_label2 = -width_visual_obs / 2 - 10;
   double width_label2 = width_visual_obs + 20;
   
   double y_top_label2 = -(2 * height_visual_obs);
   double height_label2 = 2 * height_visual_obs;
      
   double x_text_label2 = -(width_visual_obs / 2.0);
   double y_text_label2 = -(height_visual_obs / 1.5);      // bottom of the text string itself
   
   // paint face "visual observation possible"-label
   g2d.setPaint(color_block_face);
   g2d.fill(new RoundRectangle2D.Double(x_left_label2, y_top_label2, width_label2, height_label2, 20, 20));       
   
   // "visual observation possible"-label contour
   g2d.setPaint(color_contour_block);
   g2d.setStroke(new BasicStroke(block_contour_thickness));
   g2d.draw(new RoundRectangle2D.Double(x_left_label2, y_top_label2, width_label2, height_label2, 20, 20));  
  
   // text label (visual observation possible)
   g2d.setColor(color_last_update);
   g2d.drawString(visual_obs_message, (int)x_text_label2 + icon_width + total_horiz_space_arround_icon / 2, (int)y_text_label2);   
   
   // visibilty icon
   if (display_icon)
   {
       Image icon_visibilty = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "visibility.png")).getImage();  
      
      // NB double y_icon = 0;               -> top icon is bottom label !!!!
      // NB double y_icon = y_top_label;     -> top icon is top label !!!!
      // NB double y_icon = y_text_label;    -> top icon is bottom text string
      //double y_icon_vis = y_text_label2 - icon_height;      // eg -22.1 - 24 // NB icon height was already defined in the begin of this function
      double y_icon_vis = y_text_label2 - icon_height;
   
      g2d.drawImage(icon_visibilty, (int)x_left_label2 + total_horiz_space_arround_icon / 2, (int)y_icon_vis, this); // NB The x,y location specifies the position for the top-left of the image.
   } // if (display_icon)

   
   
   /////////////////////////////////////////////// TOP BLOCK ////////////////////////////////////// 
   //
   // "update date-time"- label NB EVERYTHING RELATIVE TO NEW ORIGIN !!!  
   
   //System.out.println("+++ y_text_label = " + y_text_label);        // eg -22.1
   
   
   g2d.translate(-(getWidth() / 2), -(getHeight() - (getHeight() / 12) + block_height_measured_at / 2));   // return to original origin
   
   // set new origin
   g2d.translate(getWidth() / 2, getHeight() / 12); 
   
    // "update date-time"- label NB EVERYTHING RELATIVE TO NEW ORIGIN !!!  
   double x_left_label = -width_measured_at / 2 - 10;
   double width_label = width_measured_at + 20;
   
   double y_top_label = -(2 * height_measured_at);
   double height_label = 2 * height_measured_at;
   
   double x_text_label = -(width_measured_at / 2.0);
   double y_text_label = -(height_measured_at / 1.5);      // bottom of the text string itself
  
   // paint face "date-time/update"-label
   g2d.setPaint(color_block_face);
   g2d.fill(new RoundRectangle2D.Double(x_left_label, y_top_label, width_label, height_label, 20, 20));       
   
   // "update date-time"-label contour
   g2d.setPaint(color_contour_block);
   g2d.setStroke(new BasicStroke(block_contour_thickness));
   g2d.draw(new RoundRectangle2D.Double(x_left_label, y_top_label, width_label, height_label, 20, 20));  
  
   // text (last updated) label
   g2d.setColor(color_last_update);
   g2d.drawString(update_message, (int)x_text_label + icon_width + total_horiz_space_arround_icon / 2, (int)y_text_label);   
    
   // last updated (clock)icon
   if (display_icon)
   {
      // NB double y_icon = 0;               -> top icon is bottom label !!!!
      // NB double y_icon = y_top_label;     -> top icon is top label !!!!
      // NB double y_icon = y_text_label;    -> top icon is bottom text string
      //double y_icon_date = y_text_label - icon_height;               // eg -22.1 - (24 )
      double y_icon_date = y_text_label - icon_height;
      
      g2d.drawImage(icon_date_time, (int)x_left_label + total_horiz_space_arround_icon / 2, (int)y_icon_date, this); // NB The x,y location specifies the position for the top-left of the image.
   } // if (display_icon)
   
   
   
   
   
   ///////////////////////////////////////////////////// ADDITIONAL (NOT MAIN PANEL) ///////////////////////////////////////////////////////////
   //
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

   DASHBOARD_view_AWS_hybrid.jLabel3.setForeground(color_update_message_south_panel);
   DASHBOARD_view_AWS_hybrid.jLabel3.setText(update_message_bottom_screen);

    
}   
   


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_wind_rose(Graphics2D g2d, double wind_rose_diameter, double marker_circle_diameter_1, double marker_circle_diameter_2, double bf_per_class_radius)
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
 

   int horz_offset                = (int)(wind_rose_diameter - 0);            // -20 orig
   int units_start                = (int)(wind_rose_diameter / 2 - 18);       // eg 220 / 2 - 18 = 92   // not the 5 and 10 units markers
   int units_end                  = (int)(wind_rose_diameter / 2 - 14);       // eg 220 / 2 - 14 = 96   // all markers end (5, 10 and intermediate) 
   int units_5_start              = (int)(wind_rose_diameter / 2 - 21);       // eg 220 / 2 - 21 = 89   //the 5 units markers
   int units_10_start             = (int)(wind_rose_diameter / 2 - 22);       // eg 220 / 2 - 22 = 88   //the 10 units markers
   int text_base_units_values     = (int)(wind_rose_diameter / 2 - 10);       // eg 220 / 2 - 10 = 100
   //int text_base_comments         = text_base_units_values / 2 + 5;           // eg 100 / 2 + 5 = 55
   int text_base_comments         = (int)(text_base_units_values - (22 * 1.5));        
   int width_a1                   = 0;
   int width_a2                   = 0;
   
 
   if (DASHBOARD_view_AWS_hybrid.night_vision == false)
   {
      // main wind/compass rose area   
      //g2d.setPaint(Color.LIGHT_GRAY);
      //g2d.setPaint(new Color(0,191,255, 255));
      g2d.setPaint(color_wind_rose_background);      
      g2d.fill(new Arc2D.Double(-wind_rose_diameter / 2, -wind_rose_diameter / 2, wind_rose_diameter, wind_rose_diameter, 0, 360, Arc2D.CHORD));
/*     
      // NB see also: https://docstore.mik.ua/orelly/java-ent/jfc/ch04_04.htm for obtaining area difference (substracting)
   
      // save current clipping area
      Rectangle rec = g2d.getClipBounds();
   
      // inside the inside marker ring area
      Shape circle = new Ellipse2D.Float((float)-marker_circle_diameter_2 / 2, (float)-marker_circle_diameter_2 / 2, (float)marker_circle_diameter_2, (float)marker_circle_diameter_2);
   
      // set clipping area to wind rose
      g2d.setClip(circle);
   
      Image img1 = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + "waves_desktop_sepia.png")).getImage();
      
      // NB scale the image to cover a the complete area of the drawing surface
      // g2d.drawImage(img1, 0, 0,getWidth(), getHeight(), 0, 0, img1.getWidth(null), img1.getHeight(null), null);
      int width = getWidth();  
      int height = getHeight();  
      for (int xi = -width; xi < width; xi += img1.getWidth(null))    
      {  
         for (int yi = -height; yi < height; yi += img1.getHeight(null)) 
         {  
            g2d.drawImage(img1, xi, yi, this);  
         }  
      }         
   
      // reset to 'original' clipping area
      g2d.setClip(rec);
*/      
      
   } // if (DASHBOARD_view_AWS_hybrid.night_vision == false)
   else
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
      if (i == start)   
      {  
         //if (i == 360 - 15)
         //{
         //   letter = "E";
         //}   
         //else if (i == 360 - 5)
         //{
         //   letter = "A";
         //}
         //else if (i == start + 5)
         //{
         //   letter = "S";
         //}
         //else if (i == start + 15)
         //{
         //   letter = "T";
         //}
         letter = "E";
            
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
      //if (i == start - 20 || i == start - 10 || i == start || i == start + 10 || i == start + 20)
      if (i == start)
      {  
         //if (i == start - 20)
         //{
         //   letter = "S";
         //}   
         //else if (i == start - 10)
         //{
         //   letter = "O";
         //}
         //else if (i == start)
         //{
         //   letter = "U";
         //}
         //else if (i == start + 10)
         //{
         //   letter = "T";
         //}
         //else if (i == start + 20)
         //{
         //   letter = "H";
         //}
         letter = "S";
           
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
      //if (i == start - 15 || i == start - 5 || i == start + 5 || i == start + 15)
      if (i == start)
      {  
         //if (i == start - 15)
         //{
         //   letter = "W";
         //}   
         //else if (i == start - 5)
         //{
         //   letter = "E";
         //}
         //else if (i == start + 5)
         //{
         //   letter = "S";
         //}
         //else if (i == start + 15)
         //{
         //   letter = "T";
         //}
         letter = "W";
         
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
      //if (i == start - 20 || i == start - 10 || i == start || i == start + 10 || i == start + 20)
      if (i == start)
      {  
         //if (i == start - 20)
         //{
         //   letter = "N";
         //}   
         //else if (i == start - 10)
         //{
         //   letter = "O";
         //}
         //else if (i == start)
         //{
         //   letter = "R";
         //}
         //else if (i == start + 10)
         //{
         //  letter = "T";
         //}
         //else if (i == start + 20)
         //{
         //  letter = "H";
         //}
         letter = "N";   
           
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
   // marker circles
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
   

   // True wind label (left below wind rose)
   //
   g2d.setColor(color_black);
   g2d.setFont(font_f);
   g2d.drawString("True wind", (int)(-wind_rose_diameter / 2), (int)(wind_rose_diameter / 2));
   
   // Course (COG) label (right below wind rose)
   //int width_COG_string = g2d.getFontMetrics().stringWidth("Course");
   //g2d.drawString("Course", (int)(wind_rose_diameter / 2 - width_COG_string), (int)(wind_rose_diameter / 2));
   
}   
   


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void draw_oil_tanker(Graphics2D g2d, double wind_rose_diameter)
{
   //
   //  (x,y)  .
   //             /  \
   //            /     \
   //           /       \
   //  (x1,y1) |         | (x2,y2)                                    y - ^
   //          |         |                                                |
   //          |         |                                                |
   //          |    *    |   * = origin (0,0)                             |
   //          |         |                                     x-  <--------------> x+
   //          |         |                                                |
   //          |         |                                                |
   //  (x3,y3) ----------- (x4,y4)                                        |
   //          \         /                                                v
   //   (x6,y6) --------- (x5,y5)                                      y+
   //
   //
   //
   
   
   
   // ship_breadth is leading for scaling
   //
   double ship_breadth = wind_rose_diameter / 8.0;
   
   
   ////////////// course line ///////////////
   //
   g2d.setColor(color_black);
   float[] dash = { 2f, 0f, 2f };
   g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));
   g2d.draw(new Line2D.Double(0, wind_rose_diameter / 2 + ship_breadth, 0, -wind_rose_diameter / 2 - ship_breadth));
   
   double x = -ship_breadth / 2.0;                                       // relative to origin (centre of wind rose)
   double y = -(wind_rose_diameter / 2.0 - ship_breadth);                // relative to origin (centre of wind rose)

   
   ////////////// Bow (ellipse shape) //////////////
   //
   double bow_width = ship_breadth;
   double bow_height = ship_breadth * 1.7;                                  // total height of the ellipse, bur only the upper half will be visible as the bow        
   Shape shape_bow = new Ellipse2D.Double(x, y, bow_width, bow_height);

   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(shape_bow);   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   
   g2d.setPaint(color_deck_oil_tanker);   
   g2d.fill(shape_bow);
   
   
   ///////////// main ship section (rectangle shape) ///////////
   //
   double x1 = x;
   double y1 = y + bow_height / 2.0;
   double ship_length = Math.abs(y1) * 2;                  // so the length of the main mid section
   Shape shape_midships = new Rectangle2D.Double(x1, y1, ship_breadth, ship_length);
   
   g2d.setColor(color_black);
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(shape_midships);  
   
   //g2d.setPaint(color_deck);  
   g2d.setPaint(color_deck_oil_tanker); 
   g2d.fill(shape_midships);
    
   double bridge_height = 7;                      // for bridge and containers
   double acc_height = 17;                        // for bridge and containers
   double bridge_indention = ship_breadth / 4;
   
   ///////////// aft ship (trapezium shape) ///////////
   //
   double x3 = x1;
   double x4 = x3 + ship_breadth;
   double x5 = x4 - (ship_breadth / 5); 
   double x6 = x3 + (ship_breadth / 5);
   double y3 = y1 + ship_length;
   double y4 = y3;
   double y5 = y3 + (ship_breadth / 1.5);
   double y6 = y5;
       
   double xPoints[] = {x3, x4, x5, x6};         // -values for x goes/points to port (relative to origin)
   double yPoints[] = {y3, y4, y5, y6};         // -values for y goes/points to the bow( relative to origin)
   
   GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,  xPoints.length);
   polygon.moveTo(xPoints[0], yPoints[0]);
   for (int index = 1; index < xPoints.length; index++ ) 
   {
      polygon.lineTo(xPoints[index], yPoints[index]);
   }
   polygon.closePath();
   
   // aft ship fill
   g2d.setPaint(color_deck_oil_tanker);
   g2d.fill(polygon);
   
   // aft ship contour
   g2d.setColor(color_black);
   g2d.draw(polygon);
   
 
   /////////////// ship bridge /////////
   //
   double dist_origin_bridge = y3 - bridge_height;
   draw_ship_bridge(g2d, ship_breadth, bridge_height, acc_height, dist_origin_bridge, bridge_indention);
   
   
   ////////////// pipes  ////////////
   //
   double between_pipes = 3;
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.setPaint(color_pipes_oil_tanker);
   
   // from bow to bridge (starboard side from center line)
   double x_pipe = 0;
   double y_pipe = y1;                          // at the bow
   for (int i = 0; i < 3; i++) 
   {
      Shape shape_pipe = new Line2D.Double(x_pipe, y_pipe, x_pipe, y_pipe + ship_length - bridge_height - between_pipes);
      g2d.draw(shape_pipe);
      x_pipe += between_pipes;
   }
   
   // from manifold to bridge (port side from center line)
   x_pipe = - between_pipes;
   y_pipe = 0;                                  // center ship (manifold)                    
   for (int i = 0; i < 3; i++) 
   {
      Shape shape_pipe = new Line2D.Double(x_pipe, y_pipe, x_pipe, y_pipe + (ship_length / 2) - bridge_height - between_pipes);
      g2d.draw(shape_pipe);
      x_pipe -= between_pipes;
   }
   
   // manifold pipes
   x_pipe = -(ship_breadth / 2) + between_pipes;
   y_pipe = 0; 
   for (int i = 0; i < 6; i++)
   {
      Shape shape_pipe = new Line2D.Double(x_pipe, y_pipe, x_pipe + ship_breadth - (2 * between_pipes), y_pipe); 
      g2d.draw(shape_pipe);
      y_pipe += between_pipes;
   }    
   
   
   ///////////////  deck lines ///////////////
   //
 

   //       
   //     | (x0, y0)
   //     |
   //     |  (x1, y1)
   //     \ 
   //       \
   //        |  (x2,y2)
   //        |
   //        |
   //        |  )x3, y3)  
   //
   //    
   //
   double x0_deck_line = 0;
   double x1_deck_line = 0;
   double x2_deck_line = 0;
   double x3_deck_line = 0;
   
   double y0_deck_line = 0;             // center ship
   double y1_deck_line = y_pipe;
   double y2_deck_line = y1_deck_line + between_pipes;
   double y3_deck_line = y1 + ship_length - bridge_height - between_pipes;
   
   for (int i = 0; i < 2; i++)
   {
      if (i == 0)      // port deck line
      {
         x0_deck_line = x1;
         x1_deck_line = x1;
         x2_deck_line = -(ship_breadth / 2) + (2 * between_pipes);
         x3_deck_line = x2_deck_line;
      }   
      else              // starboard deck line
      {
         x0_deck_line = x1 + ship_breadth;
         x1_deck_line = x1 + ship_breadth;
         x2_deck_line = (ship_breadth / 2) - (2 * between_pipes);
         x3_deck_line = x2_deck_line;
      }
 
      double xPoints_deck_line[] = {x0_deck_line, x1_deck_line,  x2_deck_line,  x3_deck_line};         // -values for x goes/points to port (relative to origin)
      double yPoints_deck_line[] = {y0_deck_line, y1_deck_line,  y2_deck_line,  y3_deck_line};         // -values for y goes/points to the bow( relative to origin)
   
      GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD,  xPoints_deck_line.length);
      polyline.moveTo(xPoints_deck_line[0], yPoints_deck_line[0]);
      for (int index = 1; index < xPoints_deck_line.length; index++) 
      {
         polyline.lineTo(xPoints_deck_line[index], yPoints_deck_line[index]);
      }
      
      g2d.setColor(Color.RED);
      g2d.draw(polyline);
   
   } // for (int i = 0; i < 2; i++)
   
   
   
   ////////////// Helicopter deck ///////////
   //
   double heli_circle_offset = 2;              // do not touch the railing or pipes
   double heli_circle_thickness = 3.0f;
   double x_heli = x1 + heli_circle_offset;   // do not touch the railing
   double y_heli = y1 + ship_length / 6;  
   double width_heli = (ship_breadth / 2) - heli_circle_thickness - heli_circle_offset;
   double height_heli = width_heli;
   
   Shape shape_heli = new Ellipse2D.Double(x_heli, y_heli, width_heli, height_heli);

   g2d.setColor(Color.YELLOW);
   g2d.setStroke(new BasicStroke((float)heli_circle_thickness));
   g2d.draw(shape_heli);  
   
   g2d.setPaint(Color.LIGHT_GRAY);   
   g2d.fill(shape_heli);        

}
*/       
        

/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void draw_lng_tanker(Graphics2D g2d, double wind_rose_diameter)
{
   //
   //  (x,y)  .
   //             /  \
   //            /     \
   //           /       \
   //  (x1,y1) |         | (x2,y2)                                    y - ^
   //          |         |                                                |
   //          |         |                                                |
   //          |    *    |   * = origin (0,0)                             |
   //          |         |                                     x-  <--------------> x+
   //          |         |                                                |
   //          |         |                                                |
   //  (x3,y3) ----------- (x4,y4)                                        |
   //          \         /                                                v
   //   (x6,y6) --------- (x5,y5)                                      y+
   //
   //
   //
   
   
   
   // ship_breadth is leading for scaling
   //
   double ship_breadth = wind_rose_diameter / 8.0;
   
   
   ////////////// course line ///////////////
   //
   g2d.setColor(color_black);
   float[] dash = { 2f, 0f, 2f };
   g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));
   g2d.draw(new Line2D.Double(0, wind_rose_diameter / 2 + ship_breadth, 0, -wind_rose_diameter / 2 - ship_breadth));
   
   double x = -ship_breadth / 2.0;                                       // relative to origin (centre of wind rose)
   double y = -(wind_rose_diameter / 2.0 - ship_breadth);                // relative to origin (centre of wind rose)

   
   ////////////// Bow (ellipse shape) //////////////
   //
   double bow_width = ship_breadth;
   double bow_height = ship_breadth * 2.0;                                // total height of the ellipse, bur only the upper half will be visible as the bow        
   Shape shape_bow = new Ellipse2D.Double(x, y, bow_width, bow_height);

   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(shape_bow);   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   
   g2d.setPaint(color_deck_lng_tanker);   
   g2d.fill(shape_bow);
   
   
   ///////////// main ship section (rectangle shape) ///////////
   //
   double x1 = x;
   double y1 = y + bow_height / 2.0;
   double ship_length = Math.abs(y1) * 2;                  // so the length of the main mid section
   Shape shape_midships = new Rectangle2D.Double(x1, y1, ship_breadth, ship_length);
   
   g2d.setColor(color_black);
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(shape_midships);  
   
   //g2d.setPaint(color_deck);  
   g2d.setPaint(color_deck_lng_tanker); 
   g2d.fill(shape_midships);
    
   double bridge_height = 7;                      // for bridge and containers
   double acc_height = 17;                        // for bridge and containers
   double bridge_indention = ship_breadth / 4;
   
   ///////////// aft ship (trapezium shape) ///////////
   //
   double x3 = x1;
   double x4 = x3 + ship_breadth;
   double x5 = x4 - (ship_breadth / 5); 
   double x6 = x3 + (ship_breadth / 5);
   double y3 = y1 + ship_length;
   double y4 = y3;
   double y5 = y3 + (ship_breadth / 1.5);
   double y6 = y5;
       
   double xPoints[] = {x3, x4, x5, x6};         // -values for x goes/points to port (relative to origin)
   double yPoints[] = {y3, y4, y5, y6};         // -values for y goes/points to the bow( relative to origin)
   
   GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,  xPoints.length);
   polygon.moveTo(xPoints[0], yPoints[0]);
   for (int index = 1; index < xPoints.length; index++ ) 
   {
      polygon.lineTo(xPoints[index], yPoints[index]);
   }
   polygon.closePath();
   
   // aft ship fill
   g2d.setPaint(color_deck_lng_tanker);
   g2d.fill(polygon);
   
   // aft ship contour
   g2d.setColor(color_black);
   g2d.draw(polygon);
   
 
   /////////////// ship bridge /////////
   //
   double dist_origin_bridge = y3 - bridge_height;
   draw_ship_bridge(g2d, ship_breadth, bridge_height, acc_height, dist_origin_bridge, bridge_indention);
   
   
   ///////////// LNG tanks //////////
   //
   double tank_intermediate = ship_breadth / 10;   // distances between 2 tanks and between tank and railing
   double tank_width = ship_breadth - (2 * tank_intermediate);
   double tank_height = tank_width;                                        
   
   
   double y_tank = y1;
   double x_tank = -tank_width / 2;
   
   for (int i = 0; i < 20; i++)                    // max 20 hatches
   {
      if (y_tank + (tank_height + tank_intermediate / 2) < (ship_length / 2  - bridge_height))
      {
         Shape shape_tank = new Ellipse2D.Double(x_tank, y_tank, tank_width, tank_height);

         // fill tank
         g2d.setPaint(color_lng_tanks);   
         g2d.fill(shape_tank);  
         
         // contour tank
         g2d.setStroke(new BasicStroke(1.0f));
         g2d.setPaint(color_black);  
         g2d.draw(shape_tank);   

         y_tank += tank_height + tank_intermediate;
      }
      else
      {
         break;
      }
   } // for (int i = 0; i < 20; i++)
   
   
   ////////////// pipes  ////////////
   //
   double between_pipes = 3;
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.setPaint(color_lng_tanks);
   
   // from bow to bridge (starboard side from center line)
   double x_pipe = 0;
   double y_pipe = y1;                          // at the bow
   for (int i = 0; i < 3; i++) 
   {
      Shape shape_pipe = new Line2D.Double(x_pipe, y_pipe, x_pipe, y_pipe + ship_length - bridge_height - between_pipes);
      g2d.draw(shape_pipe);
      x_pipe += between_pipes;
   }
    
}
*/       


  

   


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void draw_bulk_carrier(Graphics2D g2d, double wind_rose_diameter)
{
   //
   //  (x,y)  .
   //              / \
   //             /   \
   //            /     \
   //           /       \
   //  (x1,y1) |         | (x2,y2)                                    y - ^
   //          |         |                                                |
   //          |         |                                                |
   //          |    *    |   * = origin (0,0)                             |
   //          |         |                                     x-  <--------------> x+
   //          |         |                                                |
   //          |         |                                                |
   //  (x3,y3) ----------- (x4,y4)                                        |
   //          \         /                                                v
   //   (x6,y6) --------- (x5,y5)                                      y+
   //
   //
   //
   
   
   
   // ship_breadth is leading for scaling
   //
   double ship_breadth = wind_rose_diameter / 7.0;
   
   
   ////////////// course line ///////////////
   //
   g2d.setColor(color_black);
   float[] dash = { 2f, 0f, 2f };
   g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));
   g2d.draw(new Line2D.Double(0, wind_rose_diameter / 2 + ship_breadth, 0, -wind_rose_diameter / 2 - ship_breadth));
   
   
   ///////////// general /////
   //
   double x = -ship_breadth / 2.0;                                       // relative to origin (centre of wind rose)
   double y = -(wind_rose_diameter / 2.0 - ship_breadth);                // relative to origin (centre of wind rose)

   
   ////////////// Bow (ellipse shape) //////////////
   //
   double bow_width = ship_breadth;
   double bow_height = ship_breadth * 1.2;                                  // total height of the ellipse, bur only the upper half will be visible as the bow        
   Shape shape_bow = new Ellipse2D.Double(x, y, bow_width, bow_height);

   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(shape_bow);   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   
   g2d.setPaint(color_bulk_carrier);   
   g2d.fill(shape_bow);
   
   
   ///////////// main ship section (rectangle shape) ///////////
   //
   double x1 = x;
   double y1 = y + bow_height / 2.0;
   double ship_length = Math.abs(y1) * 2;                  // so the length of the main mid section
   Shape shape_midships = new Rectangle2D.Double(x1, y1, ship_breadth, ship_length);
   
   g2d.setColor(color_black);
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(shape_midships);  
   
   //g2d.setPaint(color_deck);  
   g2d.setPaint(color_bulk_carrier); 
   g2d.fill(shape_midships);
    
   double bridge_height = 7;                      // for bridge and containers
   double acc_height = 17;                        // for bridge and containers
   double bridge_indention = ship_breadth / 4;
   
   ///////////// aft ship (trapezium shape) ///////////
   //
   double x3 = x1;
   double x4 = x3 + ship_breadth;
   double x5 = x4 - (ship_breadth / 5); 
   double x6 = x3 + (ship_breadth / 5);
   double y3 = y1 + ship_length;
   double y4 = y3;
   double y5 = y3 + (ship_breadth / 1.5);
   double y6 = y5;
       
   double xPoints[] = {x3, x4, x5, x6};         // -values for x goes/points to port (relative to origin)
   double yPoints[] = {y3, y4, y5, y6};         // -values for y goes/points to the bow( relative to origin)
   
   GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,  xPoints.length);
   polygon.moveTo(xPoints[0], yPoints[0]);
   for (int index = 1; index < xPoints.length; index++ ) 
   {
      polygon.lineTo(xPoints[index], yPoints[index]);
   }
   polygon.closePath();
   
   // aft ship fill
   g2d.setPaint(color_bulk_carrier);
   g2d.fill(polygon);
   
   // aft ship contour
   g2d.setColor(color_black);
   g2d.draw(polygon);
   
 
   /////////////// ship bridge /////////
   //
   double dist_origin_bridge = y3 - bridge_height;
   draw_ship_bridge(g2d, ship_breadth, bridge_height, acc_height, dist_origin_bridge, bridge_indention);
   
   
   ////////////// hatches ////////////
   //
   double hatch_breath = ship_breadth / 2;
   double hatch_length = hatch_breath;
   double hatch_intermediate = hatch_breath / 2;   // distances between 2 hatches
   double y_hatch = y1;
   double x_hatch = -hatch_breath / 2;
   
   for (int i = 0; i < 20; i++)                    // max 20 hatches
   {
      
      //if (y_hatch + (hatch_length + hatch_intermediate) > (ship_length - bridge_height))
      //if (y_hatch + (hatch_length + hatch_intermediate) < 0)  
      if (y_hatch + (hatch_length + hatch_intermediate / 2) < (ship_length / 2  - bridge_height))
      {
         
         //Shape shape_hatch = new Rectangle2D.Double(x_hatch, y_hatch, hatch_breath, hatch_length);
         
         g2d.setPaint(color_bulk_carrier);
         //g2d.fill(shape_hatch);
         g2d.fill3DRect((int)x_hatch, (int)y_hatch, (int)hatch_breath, (int)hatch_length, true);   // raised hatches
      
         // hatch contour
         //g2d.setColor(color_black);
         //g2d.setStroke(new BasicStroke(1.0f));
         //g2d.draw(shape_hatch); 
         
         y_hatch += hatch_length + hatch_intermediate;
      }
      else
      {
         break;
      }   
      
   } // for (int i = 0; i < 20; i++)
         
}
*/       
        
        
        
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void draw_container_ship(Graphics2D g2d, double wind_rose_diameter)
{
   // ship_breadth is leading for scaling
   //
   double ship_breadth = wind_rose_diameter / 8.0;
   
   
   ////////////// course line ///////////////
   //
   g2d.setColor(color_black);
   float[] dash = { 2f, 0f, 2f };
   g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));
   g2d.draw(new Line2D.Double(0, wind_rose_diameter / 2 + ship_breadth, 0, -wind_rose_diameter / 2 - ship_breadth));
   
   double x = -ship_breadth / 2.0;                                       // relative to origin (centre of wind rose)
   double y = -(wind_rose_diameter / 2.0 - ship_breadth);                // relative to origin (centre of wwind rose)

   
   ////////////// Bow (ellipse shape) //////////////
   //
   double bow_width = ship_breadth;
   double bow_height = ship_breadth * 2;                                  // total height of the ellipse, bur only the upper half will be visible as the bow        
   Shape shape_bow = new Ellipse2D.Double(x, y, bow_width, bow_height);

   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(shape_bow);   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   
   g2d.setPaint(color_deck);   
   g2d.fill(shape_bow);
   
   
   ///////////// main ship section (rectangle shape) ///////////
   //
   double x1 = x;
   double y1 = y + bow_height / 2.0;
   double stern = bow_height / 2;
   double ship_length = (Math.abs(y1) * 2) + stern;    // length without the bow!
   Shape shape_midships = new Rectangle2D.Double(x1, y1, ship_breadth, ship_length);
   
   g2d.setColor(color_black);
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(shape_midships);  
   
   g2d.setPaint(color_deck);   
   g2d.fill(shape_midships);
    
   double bridge_height = 5;                      // for bridge and containers
   double acc_height = 10;                        // for bridge and containers
   double bridge_indention = ship_breadth / 5; //8;   
      
   ////////////// containers ////////////
   //
   int max_number_containers_rows = 10;
   int number_containers_in_row = 8;
   double container_breadth = ship_breadth / number_containers_in_row;
   double container_length = 0;
   double container_length_40 = container_breadth * 5;                           // fixed relation (1:5) for 40ft container
   double container_length_20 = container_length_40 / 2;
   double x_container;
   double y_container;
   boolean doorgaan = true;
   double offset_y_bridge = 0;                                               // for cheking space between bridge and first container row for and aft the bridge
   double y_container_offset;                                                // painting on aft ship start with x,y as top left and not bottom left
   
   for (int m = 0; m < 2; m++)                                               // two loops (fore and aft ship)
   {
      // NB m = 0: fore ship
      // NB m =1: aft ship
   
      if (m == 0)  // fore ship
      {
         y_container = y1;                                                   // now y_container is negative value
      }
      else        // aft ship
      {
         y_container = Math.abs(y1) + stern;//ship_length / 2;                              // now y_container is positive value (-values for y goes to the bow from the origin
      }
      
      //System.out.println("+++ m = " + m + " y_container= " + y_container); 
     
      for (int i = 0; i < max_number_containers_rows; i++)
      {
         if (m == 0) // fore ship
         {
            offset_y_bridge = 0;
         }
         else        // aft ship
         {
            offset_y_bridge = bridge_height + acc_height;
         }
      
      
         if (Math.abs(y_container) - offset_y_bridge > container_length_40)        
         {
            container_length = container_length_40;
            doorgaan = true;
         }
         else if (Math.abs(y_container - offset_y_bridge) > container_length_20)
         {
            container_length = container_length_20;                             // 20 ft container
            doorgaan = true;
         }
         else
         {
            doorgaan = false;
         }
      
         if (doorgaan)   
         {
            if (m == 0)    // fore ship 
            {
               y_container_offset = 0;                                             // because of the starting point (x,y) of the drawing of a rectangle
            }   
            else           // aft ship
            {
               y_container_offset = container_length;                             // because of the starting point (x,y) of the drawing of a rectangle
            }
         
            int start_in_row = 0;
            int end_in_row = 0;
            if (i == 0 && m == 0)                                                 // first row fore ship (20ft row on the bow)
            {
               container_length = container_length_20;
               x_container = x1 + container_breadth;
               y_container = y1 - container_length;
               start_in_row = 1;                                                  // because less container space available on the bow
               end_in_row = number_containers_in_row -1;                          // because less container space available on the bow
            }   
            else  
            {
               start_in_row = 0;                                                  // full space for containers available 
               end_in_row = number_containers_in_row;                             // full space for containers available
               x_container = x1;
            }   
            
             //x_container = x1;
            //for (int k = 0; k < number_containers_in_row; k++)
            for (int k = start_in_row; k < end_in_row; k++)   
            {
               Shape shape_container = new Rectangle2D.Double(x_container, y_container - y_container_offset, container_breadth, container_length);
           
               int random = (int )(Math.random() * 10 + 1);    // 1 - 10 return value
      
               switch (random)
               {
                  case 1  : g2d.setPaint(color_container_1);    
                            break;
                  case 2  : g2d.setPaint(color_container_2);    
                            break;
                  case 3  : g2d.setPaint(color_container_3);    
                            break;
                  case 4  : g2d.setPaint(color_container_4);    
                            break;
                  case 5  : g2d.setPaint(color_container_5);    
                            break;
                  case 6  : g2d.setPaint(color_container_6);    
                            break;
                  case 7  : g2d.setPaint(color_container_7);    
                            break;
                  case 8  : g2d.setPaint(color_container_8);    
                            break;
                  case 9  : g2d.setPaint(color_container_9);    
                            break;
                  case 10 : g2d.setPaint(color_container_10);    
                            break;
                  default : g2d.setPaint(color_container_1);   
                            break;
               }
      
               g2d.fill(shape_container);
      
               g2d.setColor(color_black);
               g2d.setStroke(new BasicStroke(1.0f));
               g2d.draw(shape_container);  
      
               x_container = x_container + container_breadth;
      
               //System.out.println("+++ i = " + i + " ;x_container: = " + x_container); 
      
            } // for (int k = 0; k < number_containers_row; k++)
   
            if (m == 0)    // fore ship
            {
               y_container = y_container + container_length;
            }
            else           // aft ship
            {
               y_container = y_container - container_length;
            }   
         }
         else
         {
            break;
         }
      } // for (int i = 0; i < max_number_containers_rows; i++)   
   } //for (int m = 0; m < 2; m++)   
   
   
   //////// ship bridge ////////
   double dist_origin_bridge = 0;
   draw_ship_bridge(g2d, ship_breadth, bridge_height, acc_height, dist_origin_bridge, bridge_indention);

}
*/


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void draw_neutral_ship(Graphics2D g2d, double wind_rose_diameter)
{
   //
   //          (x,y).
   //               
   //
   //               ^
   //             /   \
   //            /     \
   //           /       \
   //  (x1,y1) |         | (x2,y2)                                    y - ^
   //          |         |                                                |
   //          |         |                                                |
   //          |    *    |   * = origin (0,0)                             |
   //          |         |                                     x-  <--------------> x+
   //          |         |                                                |
   //          |         |                                                |
   //  (x3,y3) ----------- (x4,y4)                                        |
   //          \         /                                                v
   //   (x6,y6) --------- (x5,y5)                                      y+
   //
   //
   //
   // eg see http://what-when-how.com/introduction-to-computer-graphics-using-java-2d-and-3d/basic-principles-of-two-dimensional-graphics-introduction-to-computer-graphics-using-java-2d-and-3d-part-2/
   
   double x = 0;
   double y = 0;
   double x1 = 0;
   double y1 = 0;
   double x2 = 0;
   double y2 = 0;        
   double x3 = 0;
   double y3 = 0; 
   double x4 = 0;
   double y4 = 0;
   double x5 = 0;
   double y5 = 0;
   double x6 = 0;
   double y6 = 0;
   
   
   // ship_breadth is leading for scaling
   //
   double ship_breadth = wind_rose_diameter / 8.0;
  
   
   ////////////// course line ///////////////
   //
   g2d.setColor(color_black);
   float[] dash = { 2f, 0f, 2f };
   g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));
   g2d.draw(new Line2D.Double(0, wind_rose_diameter / 2 + ship_breadth, 0, -wind_rose_diameter / 2 - ship_breadth));
   
   
   //////////// bow ////////
   //
   x = 0;                                                             // control point quadTo; relative to origin (centre of wind rose)
   y = -(wind_rose_diameter / 2.0);                                   // control point quadTo; relative to origin (centre of wind rose)
   double bow_height_virtual = ship_breadth * 2.5;                    // quadTo; vertical distance from control point to start end end point       
   
   ///////////// main ship section (rectangle shape) ///////////
   //
   x1 = -ship_breadth / 2.0; //x;
   y1 = y + bow_height_virtual;
   x2 = ship_breadth / 2.0;
   y2 = y1;
   double ship_length = Math.abs(y1) * 2;                              // so the length of the main mid section

   ///////////// aft ship (trapezium shape) ///////////
   //
   x3 = x1;
   x4 = x3 + ship_breadth;
   x5 = x4 - (ship_breadth / 7); 
   x6 = x3 + (ship_breadth / 7);
   y3 = y1 + ship_length;
   y4 = y3;
   y5 = y3 + (ship_breadth * 1.2);
   y6 = y5;
   
   ////////////// total ship ///////////////
   //
   GeneralPath ship = new GeneralPath();
   ship.moveTo(x1, y1);
   ship.quadTo(x, y, x2, y2);
   ship.lineTo(x4, y4);
   ship.lineTo(x5, y5);
   ship.lineTo(x6, y6);
   ship.lineTo(x3, y3);
   ship.closePath();
   
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.setPaint(color_black);
   g2d.draw(ship);
   g2d.setPaint(color_deck);
   g2d.fill(ship);
   
   //System.out.println("x = "  + x);
   //System.out.println("x1 = "  + x1);
   //System.out.println("x2 = "  + x2);
   //System.out.println("y = "  + y);
   //System.out.println("y1 = "  + y1);
   //System.out.println("y2 = "  + y2);
}
*/

/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void draw_passenger_ship(Graphics2D g2d, double wind_rose_diameter)
{
   double x_control = 0;
   double y_control = 0;
   double x1 = 0;
   double y1 = 0;
   double x2 = 0;
   double y2 = 0;        
   double bow_height_virtual = 0;

   
   // ship_breadth is leading for scaling
   //
   double ship_breadth = wind_rose_diameter / 8.5;
   
   
   ////////////// course line ///////////////
   //
   g2d.setColor(color_black);
   float[] dash = { 2f, 0f, 2f };
   g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));
   g2d.draw(new Line2D.Double(0, wind_rose_diameter / 2 + ship_breadth, 0, -wind_rose_diameter / 2 - ship_breadth));
   

   
   ////////////// Bow  //////////////
   //
        
   double bow_height = ship_breadth * 3.0;  
   
   x_control = 0;                                                             // control point quadTo; relative to origin (centre of wind rose)
   y_control = -(wind_rose_diameter / 2.0);                                   // control point quadTo; relative to origin (centre of wind rose)
   bow_height_virtual = ship_breadth * 2.5;                    // quadTo; vertical distance from control point to start end end point       
   x1 = -ship_breadth / 2.0; 
   y1 = y_control + bow_height_virtual;
   x2 = ship_breadth / 2.0;
   y2 = y1;

   
   GeneralPath bow = new GeneralPath();
           
   bow.moveTo(x1, y1);
   bow.quadTo(x_control, y_control, x2, y2);
   bow.closePath();
   
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.setPaint(color_black);
   g2d.draw(bow);
   g2d.setPaint(color_deck_passenger_ship);
   g2d.fill(bow);
   
   
   ///////////// main ship section (rectangle shape) ///////////
   //
   double stern = bow_height / 2;
   double ship_length = (Math.abs(y1) * 2) + stern;    // length without the bow!
   Shape shape_midships = new Rectangle2D.Double(x1, y1, ship_breadth, ship_length);
   
   g2d.setColor(color_black);
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(shape_midships);  
   
   g2d.setPaint(color_deck_passenger_ship);   
   g2d.fill(shape_midships);
   
  
    
   ////////// ship bridge ////////////
   //
   double x1_curve = x1;
   double y1_curve = y1;
   double ctrx_curve = 0;
   double ctry_curve = y_control + bow_height_virtual / 1.1; 
   double x2_curve = x1_curve + ship_breadth;
   double y2_curve = y1_curve;
   double between_curves = ship_breadth / 8;
   
   for (int i = 0; i < 4; i++)
   {  
      QuadCurve2D quad = new QuadCurve2D.Double(x1_curve, y1_curve, ctrx_curve, ctry_curve, x2_curve, y2_curve);
      g2d.setColor(color_black);
      g2d.draw(quad);
      
      y1_curve -= between_curves;
      y2_curve = y1_curve;
      ctry_curve -= between_curves;
      x1_curve += 1.5;
      x2_curve -= 1.5;
   } // for (int i = 0; i < 3; i++)

   
   ////////// swimming pool /////////////
   //
   //
   //               
   //  y = - (to fore ship) < -------   Y    ------> y = + (to aft ship)
   //                                   |
   //                                   |
   //                                   |
   //                 (x2,y2)           |              (x3,y3)
   //                       ____________|____________                 ^ x = + (to starboard)
   //                      | \ ____________________/ |                |
   //                      |  |         |          | |                |
   //                      |  |         |          | |                |
   //  <--- fore ship      |  |         |          | |                |                             ----> aft ship    
   //                      |  | ________|__________| |                |
   //                      |_/_____________________\_|                |
   //                 (x2,y1)           |              (x4,y4)        v x = - (to port)
   //                                   |
   //                                   |                   
   //                                   |   
   //
   //
   //
   //
   double pool_width = ship_breadth * 0.8;
   double pool_length = pool_width * 1.5;
   double pool_edge = 4;
   double pool_width_inner = pool_width - (2 * pool_edge);
   double pool_length_inner = pool_length - (2 * pool_edge);
  
   // outer swimming pool coordinates
   double x1_pool = -pool_width / 2;                    //  relative to origin; middle of the pool
   double y1_pool = -pool_length / 2;                   //  relative to origin; middle of the pool
   double x2_pool = x1_pool + pool_width;   
   double y2_pool = y1_pool;
   double x3_pool = x2_pool;
   double y3_pool = y2_pool + pool_length;       
   double x4_pool = x1_pool;
   double y4_pool = y3_pool;
   
   // inner swimming pool coordinates
   double x1_sub_pool = x1_pool + pool_edge;
   double y1_sub_pool = y1_pool + pool_edge;
   double x2_sub_pool = x2_pool - pool_edge;
   double y2_sub_pool = y1_sub_pool;
   double x3_sub_pool = x2_sub_pool;
   double y3_sub_pool = y3_pool - pool_edge;    
   double x4_sub_pool = x1_sub_pool;
   double y4_sub_pool = y3_sub_pool;
   
   // fill inner swimming pool (pool bottom)
   g2d.setPaint(color_pool_2);
   g2d.fill(new Rectangle2D.Double(x1_sub_pool, y1_sub_pool, pool_width_inner, pool_length_inner));
   
   
   // fore pool edge (trapezium shape)
   GeneralPath fpe = new GeneralPath();
   fpe.moveTo(x1_pool, y1_pool);
   fpe.lineTo(x2_pool, y2_pool);
   fpe.lineTo(x2_sub_pool, y2_sub_pool);
   fpe.lineTo(x1_sub_pool, y1_sub_pool);
   fpe.closePath();
   g2d.setPaint(color_black);
   g2d.draw(fpe);
   g2d.setPaint(color_white);
   g2d.fill(fpe);
   
   // starboard pool edge (trapezium shape)
   GeneralPath spe = new GeneralPath();
   spe.moveTo(x2_pool, y2_pool);
   spe.lineTo(x3_pool, y3_pool);
   spe.lineTo(x3_sub_pool, y3_sub_pool);
   spe.lineTo(x2_sub_pool, y2_sub_pool);
   spe.closePath();
   g2d.setPaint(color_black);
   g2d.draw(spe);
   g2d.setPaint(color_pool_1);
   g2d.fill(spe);
   
   // aft pool edge (trapezium shape)
   GeneralPath ape = new GeneralPath();
   ape.moveTo(x3_pool, y3_pool);
   ape.lineTo(x4_pool, y4_pool);
   ape.lineTo(x4_sub_pool, y4_sub_pool);
   ape.lineTo(x3_sub_pool, y3_sub_pool);
   ape.closePath();
   g2d.setPaint(color_black);
   g2d.draw(ape);
   g2d.setPaint(color_pool_1);
   g2d.fill(ape);
   
   // port pool edge (trapezium shape)
   GeneralPath ppe = new GeneralPath();
   ppe.moveTo(x4_pool, y4_pool);
   ppe.lineTo(x1_pool, y1_pool);
   ppe.lineTo(x1_sub_pool, y1_sub_pool);
   ppe.lineTo(x4_sub_pool, y4_sub_pool);
   ppe.closePath();
   g2d.setPaint(color_black);
   g2d.draw(ppe);
   g2d.setPaint(color_white);
   g2d.fill(ppe);
   
   // complete swimming pool (outer pool)
   g2d.setPaint(color_pool_3);
   g2d.fill(new Rectangle2D.Double(x1_pool, y1_pool, pool_width, pool_length));
   

   /////////// diving board /////
   //
   double dive_platform_width = 10;
   double dive_platform_height = 4;
   double dive_board_width = 2;
   double dive_board_length = 12;
   double dive_platform_indention = (dive_platform_width - dive_board_width) / 2;
   
   if (dive_platform_width < pool_width - (2 * pool_edge))
   {
      double x1_dive = 0;
      double y1_dive = y1_pool;
      double x2_dive = x1_dive + dive_platform_width;
      double y2_dive = y1_dive;
      double x3_dive = x2_dive;
      double y3_dive = y2_dive + dive_platform_height;          // platform 'height' 
      double x4_dive = x3_dive - dive_platform_indention;       // platform indention
      double y4_dive = y3_dive; 
      double x5_dive = x4_dive;
      double y5_dive = y4_dive + dive_board_length;             // dive board length;
      double x6_dive = x5_dive - dive_board_width;              // dive board width;
      double y6_dive = y5_dive;
      double x7_dive = x6_dive;
      double y7_dive = y3_dive;
      double x8_dive = x1_dive;
      double y8_dive = y7_dive;
           
      double xPoints[] = {x1_dive, x2_dive, x3_dive, x4_dive, x5_dive, x6_dive, x7_dive, x8_dive};
      double yPoints[] = {y1_dive, y2_dive, y3_dive, y4_dive, y5_dive, y6_dive, y7_dive, y8_dive};         // -values for y goes/points to the bow

      GeneralPath polygon_dive = new GeneralPath(GeneralPath.WIND_EVEN_ODD,  xPoints.length);
      polygon_dive.moveTo(xPoints[0], yPoints[0]);
      for (int index = 1; index < xPoints.length; index++) 
      {
         polygon_dive.lineTo(xPoints[index], yPoints[index]);
      }
      polygon_dive.closePath();
           
      g2d.setPaint(Color.LIGHT_GRAY);
      g2d.fill(polygon_dive);
   } // if (dive_platform_width < pool_width - (2 * pool_edge))


   // accomodation fore and aft
   double between_acc = 4;

   /////////// fore accomodation /////////////
   //
   double length_fore_acc = Math.abs(y1) - (pool_length / 2) - between_curves;
   double x1_gp;
   double y1_gp = 0;
   double ctrx_gp;
   double ctry_gp;
   double x2_gp;
   double y2_gp;
   double x3_gp;
   double y3_gp;
   double x4_gp;
   double y4_gp = 0;     
   
   for (int k = 0; k < 3; k++)
   {
      GeneralPath gp = new GeneralPath();
      
      x1_gp = x1 + (k * between_acc);
      y1_gp = y1 + (k * between_acc);
      ctrx_gp = 0;
      ctry_gp = y_control + bow_height_virtual / 1.1 + (k * between_acc);
      x2_gp = x1_gp + ship_breadth - (2 * k * between_acc);
      y2_gp = y1_gp;
      x3_gp = x2_gp;
      y3_gp = y2_gp + length_fore_acc - (2 * k * between_acc);
      x4_gp = x1_gp;
      y4_gp = y3_gp;        
      
      gp.moveTo(x1_gp, y1_gp);
      gp.quadTo(ctrx_gp, ctry_gp, x2_gp, y2_gp);
      gp.lineTo(x3_gp, y3_gp);
      gp.lineTo(x4_gp, y4_gp);
      gp.closePath();

      // contour acc
      g2d.setStroke(new BasicStroke(1.0f));
      g2d.setColor(color_black);
      g2d.draw(gp);
      
      // fill acc
      if (k == 0)
      {
         g2d.setPaint(color_deck_passenger_ship);       // to cover the fore midship section line)
         g2d.fill(gp);
      }
      
      if (k == 2)
      {
         g2d.setPaint(color_acc_passenger_ship); 
         g2d.fill(gp);
      }
   } // for (int k = 0; k < 3; k++)
   
   
   //////// satellite dome (after building "fore accomodation"!) ///////
   //
   double dome_diameter = ship_breadth / 4;
   double x1_dome = 0 - dome_diameter / 2;            // middle of the fore acc
   double y1_dome = (y1_gp + y4_gp) / 2 - (dome_diameter / 2);              
   Shape shape_dome= new Ellipse2D.Double(x1_dome, y1_dome, dome_diameter, dome_diameter);
   g2d.setPaint(color_white); 
   g2d.fill(shape_dome);
   g2d.setPaint(color_black); 
   g2d.draw(shape_dome);
   
   
   /////////// aft accomodation //////////////
   //
   double length_aft_acc = ship_length / 2 - (pool_length / 2) - between_curves;
   double x1_gp2 = 0;
   double y1_gp2;
   double ctrx_gp2;
   double ctry_gp2;
   double x2_gp2;
   double y2_gp2;
   double x3_gp2;
   double y3_gp2 = 0;
   double x4_gp2;
   double y4_gp2;    
   
   for (int k = 0; k < 3; k++)
   {
      GeneralPath gp2 = new GeneralPath();
      
      x1_gp2 = x1 + (k * between_acc);
      y1_gp2 = (pool_length / 2) + between_curves + (k * between_acc);
      ctrx_gp2 = 0;
      ctry_gp2 = y1_gp2 - between_curves - (k * between_acc);
      x2_gp2 = x1_gp2 + ship_breadth - (2 * k * between_acc);
      y2_gp2 = y1_gp2;
      x3_gp2 = x2_gp2;
      y3_gp2 = y2_gp2 + length_aft_acc - (2 * k * between_acc);
      x4_gp2 = x1_gp2;
      y4_gp2 = y3_gp2;        
      
      gp2.moveTo(x1_gp2, y1_gp2);
      gp2.quadTo(ctrx_gp2, ctry_gp2, x2_gp2, y2_gp2);
      gp2.lineTo(x3_gp2, y3_gp2);
      gp2.lineTo(x4_gp2, y4_gp2);
      gp2.closePath();

      // contour acc
      g2d.setStroke(new BasicStroke(1.0f));
      g2d.setColor(color_black);
      g2d.draw(gp2);
      
      // fill acc
      if (k == 0)
      {
         g2d.setPaint(color_deck_passenger_ship);       // to cover the fore midship section line)
         g2d.fill(gp2);
      }
      
      if (k == 2)
      {
         g2d.setPaint(color_acc_passenger_ship); 
         g2d.fill(gp2);
      }
   } // for (int k = 0; k < 3; k++)
  
   
   ////////// funnel /////////////
   //
   double x1_funnel = x1_gp2 / 1.5;
   double width_funnel = Math.abs(x1_funnel) * 2;
   double length_funnel = width_funnel * 1.5;   
   double y1_funnel = y3_gp2 - length_funnel;
         
   g2d.setPaint(color_funnel_passenger_ship); 
   g2d.fill(new RoundRectangle2D.Double(x1_funnel, y1_funnel, width_funnel, length_funnel, 10, 10));  
   
   
   /////////// pipes in funnel //////////////
   //
   g2d.setPaint(color_black); 
   double diameter_pipe = width_funnel / 4;
   double x1_pipe;
   double y1_pipe = 0;
   
   for (int p = 0; p < 10; p++)
   {
      x1_pipe = -(diameter_pipe / 2);
      y1_pipe = (y1_funnel + diameter_pipe) + (p * diameter_pipe);
      
      if ((diameter_pipe * (p + 1)) < (length_funnel - diameter_pipe))
      {
         Shape shape_pipe= new Ellipse2D.Double(x1_pipe, y1_pipe, diameter_pipe, diameter_pipe);
         g2d.fill(shape_pipe);
      }
      else
      {
         break;
      }   
   } // for (int p = 0; p < 10; p++)
   
   
   ////////// life boats (after building "pipes in funnel" !) ////////////
   //
   g2d.setPaint(color_life_boat); 
   
   double life_boat_breadth = 3.0;
   double life_boat_length = 15;
   
   g2d.setStroke(new BasicStroke((float)life_boat_breadth));
   double x1_life_boat = 0;
   double y1_life_boat = 0;
   double x2_life_boat = 0; 
   double y2_life_boat;
   
   for (int h = 0; h < 2; h++)    // h = 0: port side; h = 1 : starboard side
   {
      if (h == 0)                 // port side
      {
         x1_life_boat = x1;
         y1_life_boat = y1 + life_boat_length;
         x2_life_boat = x1_life_boat;
      }
      else if (h == 1)            // starboard side
      {
         x1_life_boat = x1 + ship_breadth;
         y1_life_boat = y1 + life_boat_length;
         x2_life_boat = x1_life_boat;
      }
      
      for (int b = 0; b < 20; b++)
      { 
         if (y1_life_boat + (2 * life_boat_length) < y1_pipe)     // from "bow" to the "pipes" 
         {
            y2_life_boat = y1_life_boat + life_boat_length;
            g2d.draw(new Line2D.Double(x1_life_boat, y1_life_boat, x2_life_boat, y2_life_boat)); 
         
            y1_life_boat += (2 * life_boat_length);
         }
         else
         {
           break;
         }   
      } //  for (int b = 0; b < 20; b++)
   } // for (int h = 0; h < 2; h++)
   
}
*/



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void draw_ship_bridge(Graphics2D g2d, double ship_breadth, double bridge_height, double acc_height, double dist_origin_bridge, double bridge_indention)
{
   ///////////// ship bridge ////////////
   //
   //
   //
   //  (x1,y1)----------------------------- (x2,y2)
   //         |                            |
   //         |   (x7,y7)        (x4,y4)   |
   //  (x8,y8)--------               -------(x3,y3)
   //                |               |
   //                |               |
   //          (x6,y6)----------------(x5,y5)
   //
   //
   // NB
   //    bridge_height = distance y2 -> y3
   //    acc_height = distance y3 -> y5
   //    bridge_indention = x3 -> x4
   //
   // NB
   //    all relative to origin (0, 0)
   //
   
   double bridge_overhang = 3;
   //double bridge_indention = 8;
   double x1_bridge = -ship_breadth / 2 - bridge_overhang;
   double x2_bridge = ship_breadth / 2 + bridge_overhang;       
   double x3_bridge = x2_bridge;    
   double x4_bridge = ship_breadth / 2 - bridge_indention;
   double x5_bridge = x4_bridge;       
   double x6_bridge = -ship_breadth / 2 + bridge_indention;      
   double x7_bridge = x6_bridge;
   double x8_bridge = x1_bridge;
           
   double y1_bridge = dist_origin_bridge; //0;
   double y2_bridge = y1_bridge;       
   double y3_bridge = y1_bridge + bridge_height;      
   double y4_bridge = y3_bridge;
   double y5_bridge = y1_bridge + bridge_height + acc_height;      
   double y6_bridge = y5_bridge;     
   double y7_bridge = y4_bridge;
   double y8_bridge = y3_bridge;    
           
   double xPoints[] = {x1_bridge, x2_bridge, x3_bridge, x4_bridge, x5_bridge, x6_bridge, x7_bridge, x8_bridge};
   double yPoints[] = {y1_bridge, y2_bridge, y3_bridge, y4_bridge, y5_bridge, y6_bridge, y7_bridge, y8_bridge};         // -values for y goes/points to the bow

   
   GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,  xPoints.length);
   polygon.moveTo(xPoints[0], yPoints[0]);
   for (int index = 1; index < xPoints.length; index++ ) 
   {
      polygon.lineTo(xPoints[index], yPoints[index]);
   }
   polygon.closePath();
   
   // bridge fill
   if (main.ship_type_dashboard.equals(main.CONTAINER_SHIP))
   {
      g2d.setPaint(color_bridge_container_ship); 
   }
   else if (main.ship_type_dashboard.equals(main.OIL_TANKER))
   {
      g2d.setPaint(color_bridge_oil_tanker);                          
   }
   else
   {
      g2d.setPaint(Color.white);
   }
   g2d.fill(polygon);
   
   // bridge contour
   if (main.ship_type_dashboard.equals(main.OIL_TANKER))
   {
      g2d.setColor(Color.white);
   }
   else
   {
      g2d.setColor(color_black);
   }
   g2d.draw(polygon);
   
}
*/

/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_wind_arrow(Graphics2D g2d, double marker_circle_diameter_2, double bf_per_class_radius)
{
   boolean true_wind_dir_ok = false;
   boolean true_wind_speed_ok = false;
   int Bf_class = Integer.MAX_VALUE;
   int true_wind_dir_reading = Integer.MAX_VALUE;
   
   
   // true wind dir
   if (main.true_wind_dir_from_AWS_present)
   {
      true_wind_dir_reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_dir;
      if (true_wind_dir_reading >= 1 && true_wind_dir_reading <= 360)       // dir = 0 should be impossible 
      {
         true_wind_dir_ok = true;
      }
   } //if (main.true_wind_dir_from_AWS_present)
   
   
   // insert true wind speed as Bf
   if (main.true_wind_speed_from_AWS_present)
   {
      int true_wind_speed_reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_speed; // NB could by kts or m/s
      if (true_wind_speed_reading >= 0.0 && true_wind_speed_reading <= 400.0) 
      {
         // so the digit indication is up to 400 kts/m/s and the analogue up to 110 kts/m/s
         //String digits = "";
         
         if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)               // units is knots
         {
            if (true_wind_speed_reading >= 64 && true_wind_speed_reading < 400)
            {
               Bf_class = 12; 
               true_wind_speed_ok = true; 
            }
            else
            {   
               switch (true_wind_speed_reading)
               {
                  case 0:                                                                 Bf_class = 0; true_wind_speed_ok = true; break;
                  case 1: case 2: case 3:                                                 Bf_class = 1; true_wind_speed_ok = true; break;                  
                  case 4: case 5: case 6:                                                 Bf_class = 2; true_wind_speed_ok = true; break;    
                  case 7: case 8: case 9: case 10:                                        Bf_class = 3; true_wind_speed_ok = true; break;  
                  case 11: case 12: case 13: case 14: case 15: case 16:                   Bf_class = 4; true_wind_speed_ok = true; break;
                  case 17: case 18: case 19: case 20: case 21:                            Bf_class = 5; true_wind_speed_ok = true; break;
                  case 22: case 23: case 24: case 25: case 26: case 27:                   Bf_class = 6; true_wind_speed_ok = true; break;
                  case 28: case 29: case 30: case 31: case 32: case 33:                   Bf_class = 7; true_wind_speed_ok = true; break;
                  case 34: case 35: case 36: case 37: case 38: case 39: case 40:          Bf_class = 8; true_wind_speed_ok = true; break;
                  case 41: case 42: case 43: case 44: case 45: case 46: case 47:          Bf_class = 9; true_wind_speed_ok = true; break;
                  case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55: Bf_class = 10; true_wind_speed_ok = true; break;
                  case 56: case 57: case 58: case 59: case 60: case 61: case 62: case 63: Bf_class = 11; true_wind_speed_ok = true; break;
                  default :                                                               Bf_class = Integer.MAX_VALUE; break;
               } // switch
            } // else
         } // if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)  
         
         else if (main.wind_units_dashboard.indexOf(main.M_S) != -1)               // units is m/s
         {
            if (true_wind_speed_reading >= 33 && true_wind_speed_reading < 400)
            {
               Bf_class = 12; 
               true_wind_speed_ok = true; 
            }
            else
            {   
               switch (true_wind_speed_reading)
               {
                  case 0:                                                                 Bf_class = 0; true_wind_speed_ok = true; break;
                  case 1:                                                                 Bf_class = 1; true_wind_speed_ok = true; break;    
                  case 2: case 3:                                                         Bf_class = 2; true_wind_speed_ok = true; break;    
                  case 4: case 5:                                                         Bf_class = 3; true_wind_speed_ok = true; break;  
                  case 6: case 7:                                                         Bf_class = 4; true_wind_speed_ok = true; break;
                  case 8: case 9: case 10:                                                Bf_class = 5; true_wind_speed_ok = true; break;
                  case 11: case 12: case 13:                                              Bf_class = 6; true_wind_speed_ok = true; break;
                  case 14: case 15: case 16: case 17:                                     Bf_class = 7; true_wind_speed_ok = true; break;
                  case 18: case 19: case 20:                                              Bf_class = 8; true_wind_speed_ok = true; break;
                  case 21: case 22: case 23: case 24:                                     Bf_class = 9; true_wind_speed_ok = true; break;
                  case 25: case 26: case 27: case 28:                                     Bf_class = 10; true_wind_speed_ok = true; break;
                  case 29: case 30: case 31: case 32:                                     Bf_class = 11; true_wind_speed_ok = true; break;
                  default :                                                               Bf_class = Integer.MAX_VALUE; break;
               } // switch
            } // else
         } // else if (main.wind_units_dashboard.indexOf(main.M_S) != -1)
        
      }
   } //if (main.true_wind_speed_from_AWS_present)
   
   
   if (true_wind_speed_ok && true_wind_dir_ok)
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
   
   
      g2d.setStroke(new BasicStroke(1.0f));
      
      double max_arrow_width = 10;
   
      // polygon of the max theoretical wind arrow (12 Bf)
      //
      double x1_arrow = Math.cos(Math.toRadians(true_wind_dir_reading - (max_arrow_width / 2) - 90)) * (radius_wind_arrow_max);                   // -90 because the drawing starts at EAST (90 degr)
      double x2_arrow = Math.cos(Math.toRadians(true_wind_dir_reading + (max_arrow_width / 2) - 90)) * (radius_wind_arrow_max); 
      double x3_arrow = Math.cos(Math.toRadians(true_wind_dir_reading + 0 - 90)) * (radius_wind_arrow_max);
      
      double y1_arrow = Math.sin(Math.toRadians(true_wind_dir_reading - (max_arrow_width / 2) - 90)) * (radius_wind_arrow_max);
      double y2_arrow = Math.sin(Math.toRadians(true_wind_dir_reading + (max_arrow_width / 2) - 90)) * (radius_wind_arrow_max);        
      double y3_arrow = Math.sin(Math.toRadians(true_wind_dir_reading + 0 - 90)) * (radius_wind_arrow_max);        
            
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
      double x1_arrow_actual = Math.cos(Math.toRadians(true_wind_dir_reading - (max_arrow_width / 2) - 90)) * (radius_wind_arrow_actual);                   // -90 because the drawing starts at EAST (90 degr)
      double x2_arrow_actual = Math.cos(Math.toRadians(true_wind_dir_reading + (max_arrow_width / 2) - 90)) * (radius_wind_arrow_actual); 
      double x3_arrow_actual = Math.cos(Math.toRadians(true_wind_dir_reading + 0 - 90)) * (radius_wind_arrow_actual);
      
      double y1_arrow_actual = Math.sin(Math.toRadians(true_wind_dir_reading - (max_arrow_width / 2) - 90)) * (radius_wind_arrow_actual);
      double y2_arrow_actual = Math.sin(Math.toRadians(true_wind_dir_reading + (max_arrow_width / 2) - 90)) * (radius_wind_arrow_actual);        
      double y3_arrow_actual = Math.sin(Math.toRadians(true_wind_dir_reading + 0 - 90)) * (radius_wind_arrow_actual);        
      
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

   //private final Color color_white;
   private final Color color_black;
   private final Color color_red;
   private final Color color_gray;
   //private final Color color_dark_red;
   private final Color color_wind_arrow_actual;
   private final Color color_wind_arrow_max;
   //private final Color color_label_text;
   private Color color_last_update;                    // not final
   private Color color_update_message_south_panel;     // not final
   private Color color_digits;                         // not final
   private final Color color_inserted_data;   
   private final Color color_wind_rose_additional;
   
   private Font font_a; 
   private Font font_b;
   private Font font_c;
   //private Font font_d;
   private Font font_e;
   private Font font_f;
   //private int horz_offset;
   //private int fontSize_b;
   //private int fontSize_d;
   //private int fontSize_e;
   //private int block_last_update_width;
   //private int block_last_update_height;
   
   //private double block_height_last_updated;
   //private int block_wind_rose_width;
   //private int block_wind_rose_left_height;
   
   //private float instrument_ring_thickness;
   //public static final String digital_font = "LCD-N___.TTF";
   //private final String text = "test";
   private final float block_contour_thickness;
   
   private Color color_contour_block;                     // not final (night/day colors)
   private Color color_block_face;                        // not final (night/day colors)
   //private final Color color_wind_rose_face;
   private final Color color_wind_rose_ring;
   private final Color color_wind_rose_background;
   //private final Color color_deck;
   //private final Color color_bridge_container_ship;
   //private final Color color_container_1;
   //private final Color color_container_2;
   //private final Color color_container_3;
   //private final Color color_container_4;
   //private final Color color_container_5;
   //private final Color color_container_6;
   //private final Color color_container_7;
   //private final Color color_container_8;
   //private final Color color_container_9;
   //private final Color color_container_10;
   
   //private final Color color_bulk_carrier;
   //private final Color color_bridge_oil_tanker;
   //private final Color color_deck_oil_tanker;
   //private final Color color_pipes_oil_tanker;
   //private final Color color_deck_lng_tanker;
   //private final Color color_lng_tanks;
   //private final Color color_deck_passenger_ship;
   //private final Color color_pool_1;
   //private final Color color_pool_2;
   //private final Color color_pool_3;
   //private final Color color_acc_passenger_ship;
   //private final Color color_life_boat;
   //private final Color color_funnel_passenger_ship;
 
}
