
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.Arc2D;
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
public class DASHBOARD_grafiek_AWS  extends JPanel {
   
   
   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
 public DASHBOARD_grafiek_AWS()
{ 
   color_gray                 = Color.GRAY;
   color_black                = Color.BLACK;
   color_light_golden_rod     = new Color(255, 236, 139);
   color_brown                = new Color(139, 69, 19);
   color_dark_red             = new Color(210, 0, 0);
   thickness_outside_ring     = 2.0f;
   updated_text               = "";                                 //updated";  // unfortunately due to a lack of space no room for writing "updated"
   
   // get the screen size 
   //
   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
   double width_screen = screenSize.getWidth();
   double height_screen = screenSize.getHeight();
   System.out.println("--- Screen resolution AWS Dashboard analog: " + width_screen + " x " + height_screen);
   
   // Full HD or above (1920 x 1080)
   if ((width_screen > 1900) && (height_screen > 1000))
   {
      instrument_diameter = 180;  
      vert_offset         = 30;
      fontSize_a          = 7;
      fontSize_b          = 8;
      fontSize_c          = 5;
      fontSize_d          = 12;
      color_digits        = color_black;                            // digits value on the dial
   }
   else
   {
      // eg 1600 x 900 or 1366 x 768
      instrument_diameter = 240; 
      vert_offset         = -20;
      fontSize_a          = 7;
      fontSize_b          = 15;
      fontSize_c          = 10;
      fontSize_d          = 18;
      color_digits        = Color.BLUE;
   }
   
   // font (logical fonts, will always be available on every system with Java)
   font_a = new Font("SansSerif", Font.PLAIN, fontSize_a);          // units (eg 1100)
   font_b = new Font("SansSerif", Font.PLAIN, fontSize_b);          // digits sensor value
   font_c = new Font("SansSerif", Font.PLAIN, fontSize_c);          // last update on dial
   font_d = new Font("Monospaced", Font.ITALIC, fontSize_d);        // text's like "PRESSURE MSL", "TEMPERATURE" etc
   
   // default stroke for main hand (= pointer/arrow air pressure value); will be overwritten (by a dashed line) if the communucation to the sensor is lost
   stroke_main_hand = new BasicStroke(1.0f);
   
   horz_offset                = instrument_diameter - 0;            // -20 orig
   marker_circle_diameter_1   = instrument_diameter - 26;           // eg outside marker circle: 220 - 26 = 194;
   marker_circle_diameter_2   = marker_circle_diameter_1 - 10;      // eg outside marker circle: 194 - 10 = 184;
   units_start                = instrument_diameter / 2 - 18;       // eg 220 / 2 - 18 = 92   // not the 5 and 10 units markers
   units_end                  = instrument_diameter / 2 - 14;       // eg 220 / 2 - 14 = 96   // all markers end (5, 10 and intermediate) 
   units_5_start              = instrument_diameter / 2 - 21;       // eg 220 / 2 - 21 = 89   //the 5 units markers
   units_10_start             = instrument_diameter / 2 - 22;       // eg 220 / 2 - 22 = 88   //the 10 units markers
   text_base_units_values     = instrument_diameter / 2 - 10;       // eg 220 / 2 - 10 = 100
   text_base_comments         = text_base_units_values / 2 + 5;     // eg 100 / 2 + 5 = 55
   hand_opposite              = 25;                                 // length hand/arrow from the centre in opposite dir
   arrow_n                    = units_start - 5;                    // eg 89 - 5 = 84
   arrow_l                    = arrow_n - 14;                       // eg 84 - 14 = 70
   arrow_r                    = arrow_l + 3;                        // eg 70 + 3 = 73
   text_base_digits           = units_5_start;                      // eg 89

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
   
   int wind_mode = 0;                  // 1 = true wind 2 = relative wind 3 = wind gust
   boolean pressure_mode_msl;        // if true; pressure MSL; if false: pressure read (sensor height)
   
   // meters top row
   //
   int x0_meter_top_middle = getWidth() / 2;
   int y0_meter_top_middle = getHeight() / 3 + vert_offset;     
   
   int x0_meter_top_right = getWidth() / 2 + horz_offset;
   int y0_meter_top_right = getHeight() / 3 + vert_offset;     
   
   int x0_meter_top_right2 = getWidth() / 2 + (2 * horz_offset);
   int y0_meter_top_right2 = getHeight() / 3 + vert_offset;     
   
   int x0_meter_top_left = getWidth() / 2 - horz_offset;
   int y0_meter_top_left = getHeight() / 3 + vert_offset;     
   
   int x0_meter_top_left2 = getWidth() / 2 - (2 * horz_offset);
   int y0_meter_top_left2 = getHeight() / 3 + vert_offset;     
   
   
   // meters bottom row
   //
   int x0_meter_bottom_middle = getWidth() / 2;
   int y0_meter_bottom_middle = getHeight() / 3 * 2 - vert_offset;     
   
   int x0_meter_bottom_right = getWidth() / 2 +  horz_offset;
   int y0_meter_bottom_right = getHeight() / 3 * 2 - vert_offset;     
   
   int x0_meter_bottom_right2 = getWidth() / 2 + (2 * horz_offset);
   int y0_meter_bottom_right2 = getHeight() / 3  * 2 - vert_offset;     
   
   int x0_meter_bottom_left = getWidth() / 2 -  horz_offset;
   int y0_meter_bottom_left = getHeight() / 3 * 2 - vert_offset;     
  
   int x0_meter_bottom_left2 = getWidth() / 2 - (2 * horz_offset);
   int y0_meter_bottom_left2 = getHeight() / 3 * 2 - vert_offset;     
   
  
   // clean the dirty pixels
   super.paintComponent(g);
   
   // general
   Graphics2D g2d = (Graphics2D) g;
   
  
   // background image(not in night vision mode)
   //
   if (DASHBOARD_view_AWS.night_vision == false)
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
   }

   // set the new origin
   g2d.translate(getWidth() / 2, getHeight() / 2);
   
   // scaling
   int side = getWidth() > getHeight() ? getHeight() : getWidth();
   g2d.scale((side / 2) / 200, (side / 2) / 200);
   
   // rendering hints
   setAllRenderingHints(g2d);
   
   // colors depending night or day vision
   if (DASHBOARD_view_AWS.night_vision == true)
   {
      color_instrument_face = color_dark_red;
      color_instrument_ring = color_black;
      //color_set_hand = color_gray;
      color_update_message_south_panel = color_gray;
   }
   else
   {
      color_instrument_face = color_light_golden_rod;
      color_instrument_ring = color_brown;
      //color_set_hand = color_white;
      color_update_message_south_panel = color_black;
   }

   // obsolete data (no cummunication for roughly > 5 minutes with the AWS)
   if (main.displayed_aws_data_obsolate == true)
   {
      color_digits = main.obsolate_color_data_from_aws;                      // // will not be visible antmore on the same color instrument face
      color_instrument_face = main.obsolate_color_data_from_aws;
      color_instrument_ring = color_black;
      
      float dash1[] = {5.0f};     // where the values alternate between the dash length and the gap length. a transparent dash for 5 units, another opaque dash for 5 units
      stroke_main_hand = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
   }
   
   
   //
   ///////////////////// "last updated date and time" bottom left screen [South panel]
   //
   String updated_text_bottom_screen = "dials updated every minute. last updated: ";
   String update_message = "";
   
   if (main_RS232_RS422.dashboard_string_last_update_record_date_time.equals(""))
   {
      update_message =  updated_text_bottom_screen + " unknown";
   }
   else
   {
      //update_message = updated_text_bottom_screen + " " + main_RS232_RS422.dashboard_string_last_update_record_date_time.replace("-", " ");
      
      // remove the seconds from the date-time update string "12-Dec-2017 08:12:24 UTC" -> "12-Dec-2017 08:12 UTC"
      String updated_message_short = main_RS232_RS422.dashboard_string_last_update_record_date_time.substring(0, main_RS232_RS422.dashboard_string_last_update_record_date_time.length() - 7);
      update_message = updated_text_bottom_screen + " " + updated_message_short + " UTC";
   }

   DASHBOARD_view_AWS.jLabel3.setForeground(color_update_message_south_panel);
   DASHBOARD_view_AWS.jLabel3.setText(update_message);
   
   
   
   //
   // top row dials   : 1 - 2 - 3 - 4 - 5
   // bottom row dials: 6 - 7 - 8 - 9 - 10
   //
   
   // 
   ///////////////// top middle meter (3) [TEMPERATURE] ///////////////////
   //
   g2d.translate(-getWidth() / 2, -getHeight() / 2);
   g2d.translate(x0_meter_top_middle, y0_meter_top_middle);
   
   // draw dial barometer
   draw_AWS_instrument_face_thermometer(g2d);
   draw_AWS_thermometer_hand(g2d);
   draw_AWS_instrument_face_central_knob(g2d);
   
   
   //
   ///////////////////// top meter (4) [RELATIVE HUMIDITY] //////////////////
   //
   g2d.translate(-x0_meter_top_middle, -y0_meter_top_middle);
   g2d.translate(x0_meter_top_right, y0_meter_top_right);
   
   // draw dial meter
   draw_AWS_instrument_face_rh(g2d);
   draw_AWS_hygrometer_hand(g2d);
   draw_AWS_instrument_face_central_knob(g2d);
  
   
   //
   ///////////////////// top meter (5) [SST] //////////////////
   //
   g2d.translate(-x0_meter_top_right, -y0_meter_top_right);
   g2d.translate(x0_meter_top_right2, y0_meter_top_right2);
   
   // draw dial meter
   draw_AWS_instrument_face_sst(g2d);
   draw_AWS_sst_hand(g2d);
   draw_AWS_instrument_face_central_knob(g2d);
   
   
   //
   ///////////////////// top meter (2) [AIR PRESSURE READ/SENSOR HEIGHT] //////////////////
   //
   g2d.translate(-x0_meter_top_right2, -y0_meter_top_right2);
   g2d.translate(x0_meter_top_left, y0_meter_top_left);
   
   // draw dial meter
   pressure_mode_msl = false;
   draw_AWS_instrument_face_barometer(g2d, pressure_mode_msl);
   draw_AWS_barometer_hands(g2d, pressure_mode_msl);
   draw_AWS_instrument_face_central_knob(g2d);
  
   
   //
   ///////////////////// top meter (1) [AIR PRESSURE MSL] //////////////////
   //
   g2d.translate(-x0_meter_top_left, -y0_meter_top_left);
   g2d.translate(x0_meter_top_left2, y0_meter_top_left2);
   
   // draw dial meter
   pressure_mode_msl = true;
   draw_AWS_instrument_face_barometer(g2d, pressure_mode_msl);
   draw_AWS_barometer_hands(g2d, pressure_mode_msl);
   draw_AWS_instrument_face_central_knob(g2d);
   
   
   
   //
   ///////////////////// bottom middle meter (8) [wind gust]  //////////////////
   //
   
   g2d.translate(-x0_meter_top_left2, -y0_meter_top_left2);
   g2d.translate(x0_meter_bottom_middle, y0_meter_bottom_middle);
   
   // draw dial meter
   wind_mode = 3;                   // wind gust
   draw_AWS_instrument_face_wind_speed(g2d, wind_mode);
   draw_AWS_anemometer_hand(g2d, wind_mode);
   draw_AWS_instrument_face_central_knob(g2d);
   
   
   //
   ///////////////////// bottom meter 9 [relative wind speed] //////////////////
   //
   g2d.translate(-x0_meter_bottom_middle, -y0_meter_bottom_middle);
   g2d.translate(x0_meter_bottom_right, y0_meter_bottom_right);
   
   // draw dial meter
   wind_mode = 2;                   // relative wind
   draw_AWS_instrument_face_wind_speed(g2d, wind_mode);
   draw_AWS_anemometer_hand(g2d, wind_mode);
   draw_AWS_instrument_face_central_knob(g2d);  
   
   
   //
   ///////////////////// bottom meter 10 [relative wind dir] //////////////////
   //
   g2d.translate(-x0_meter_bottom_right, -y0_meter_bottom_right);
   g2d.translate(x0_meter_bottom_right2, y0_meter_bottom_right2);
   
   // draw dial meter
   wind_mode = 2;                  // relative wind
   draw_AWS_instrument_face_wind_dir(g2d, wind_mode);
   draw_AWS_wind_dir_hand(g2d, wind_mode);
   draw_AWS_instrument_face_central_knob(g2d);  
   
   
   //
   ///////////////////// bottom meter 7 [true wind dir] //////////////////
   //
   g2d.translate(-x0_meter_bottom_right2, -y0_meter_bottom_right2);
   g2d.translate(x0_meter_bottom_left, y0_meter_bottom_left);
   
   // draw dial meter
   wind_mode = 1;              // true wind
   draw_AWS_instrument_face_wind_dir(g2d, wind_mode);
   draw_AWS_wind_dir_hand(g2d, wind_mode);
   draw_AWS_instrument_face_central_knob(g2d);
  
   
   //
   ///////////////////// bottom meter 6 [true wind speed]] //////////////////
   //
   g2d.translate(-x0_meter_bottom_left, -y0_meter_bottom_left);
   g2d.translate(x0_meter_bottom_left2, y0_meter_bottom_left2);
   
   // draw dial meter
   wind_mode = 1;              // true wind
   draw_AWS_instrument_face_wind_speed(g2d, wind_mode);
   draw_AWS_anemometer_hand(g2d, wind_mode);
   draw_AWS_instrument_face_central_knob(g2d);
   
}   
   


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_AWS_instrument_face_central_knob(Graphics2D g2d)
{
   // centre knob
   //g2d.draw(new Arc2D.Double(-5, -5, 10, 10, 360, 360, Arc2D.CHORD));   // East = 0 degrees -> to the left
   g2d.setColor(color_black);
   int radius_centre_knob = 5;
   int diameter_centre_knob = radius_centre_knob * 2;
   g2d.fillOval(0 - radius_centre_knob, 0 - radius_centre_knob, diameter_centre_knob, diameter_centre_knob); // shift x and y (0, 0) by the radius of the circle in order to correctly center it
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_AWS_instrument_face_wind_dir(Graphics2D g2d, int wind_mode)
{
   // eg: https://www.google.nl/search?q=barometer&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjJ78PZg8_UAhWEJMAKHZA3AsAQ_AUICigB&biw=1920&bih=950#imgrc=hE2asDauQrhr6M:
   
   String letter = null;
   String text;
   int angle;
   int start;
   int x;
   int y;
   int width_letter;
   
   
   // main instument area   
   g2d.setPaint(color_instrument_face);
   g2d.fill(new Arc2D.Double(-instrument_diameter / 2, -instrument_diameter / 2, instrument_diameter, instrument_diameter, 0, 360, Arc2D.CHORD));
      
   // outside ring
   g2d.setColor(color_instrument_ring);
   g2d.setStroke(new BasicStroke(thickness_outside_ring));
   g2d.draw(new Arc2D.Double(-instrument_diameter /2, -instrument_diameter / 2, instrument_diameter, instrument_diameter, 0, 360, Arc2D.CHORD));

   
   g2d.setColor(color_black);
   
   for (int i = 0; i < 360; i++)     
   {
      // NB first i = <never mind> -> paint starting point = always East -> clockwise
      
      g2d.setFont(font_a);
      
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
         
         
      // text decoration 'EAST'
      if (wind_mode == 1)                       // true wind
      {   
         start = 0;
         if (i == 360 - 15 || i == 360 - 5 || i == start + 5 || i == start + 15)
         {  
            if (i == 360 - 15)
            {
               letter = "E";
            }   
            else if (i == 360 - 5)
            {
               letter = "A";
            }
            else if (i == start + 5)
            {
               letter = "S";
            }
            else if (i == start + 15)
            {
               letter = "T";
            }
            
            g2d.setFont(font_d);
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
      } //  if (wind_mode == 1) 
      
      // text decoration 'SOUTH'
      if (wind_mode == 1) 
      {   
         start = 90;
         if (i == start - 20 || i == start - 10 || i == start || i == start + 10 || i == start + 20)
         {  
            if (i == start - 20)
            {
               letter = "S";
            }   
            else if (i == start - 10)
            {
               letter = "O";
            }
            else if (i == start)
            {
               letter = "U";
            }
            else if (i == start + 10)
            {
               letter = "T";
            }
            else if (i == start + 20)
            {
               letter = "H";
            }
           
            g2d.setFont(font_d);
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
      } // if (wind_mode == 1) 
         
      // text decoration 'WEST'
      if (wind_mode == 1)                       // true wind
      {   
         start = 180;
         if (i == start - 15 || i == start - 5 || i == start + 5 || i == start + 15)
         {  
            if (i == start - 15)
            {
               letter = "W";
            }   
            else if (i == start - 5)
            {
               letter = "E";
            }
            else if (i == start + 5)
            {
               letter = "S";
            }
            else if (i == start + 15)
            {
               letter = "T";
            }
            
            g2d.setFont(font_d);
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
      } // if (wind_mode == 1) 

      
      // text decoration 'WIND TRUE'
      if (wind_mode == 1)                       // true wind
      {
         start = 270;
         if (i == start - 40 || i == start - 30 || i == start - 20 || i == start - 10 || i == start || i == start + 10 || i == start + 20 || i == start + 30  || i == start + 40)
         {  
            if (i == start - 40)
            {
               letter = "W";
            }  
            else if (i == start - 30)
            {
               letter = "I";
            }  
            else if (i == start - 20)
            {
               letter = "N";
            }   
            else if (i == start - 10)
            {
               letter = "D";
            }
            else if (i == start)
            {
               letter = " ";
            }
            else if (i == start + 10)
            {
               letter = "T";
            }
            else if (i == start + 20)
            {
               letter = "R";
            }
            else if (i == start + 30)
            {
               letter = "U";
            }
            else if (i == start + 40)
            {
               letter = "E";
            }
           
            g2d.setFont(font_d);
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
      } // if (wind_mode == 1) 
      
      // text decoration 'WIND DIR REL'
      if (wind_mode == 2)                       // relative wind
      {
         //System.out.println("+++ i = "+ i);
         start = 270;
         if (i == start - 50 || i == start - 40 || i == start - 30 || i == start - 20 || i == start - 10 || i == start || i == start + 10 || i == start + 20 || i == start + 30 || i == start + 40 || i == start + 50 || i == start + 60)
         {  
            if (i == start - 50)
            {
               letter = "W";
            }  
            else if (i == start - 40)
            {
               letter = "I";
            }  
            else if (i == start - 30)
            {
               letter = "N";
            }   
            else if (i == start - 20)
            {
               letter = "D";
            }
            else if (i == start - 10)
            {
               letter = " ";
            }
            else if (i == start)
            {
               letter = "D";
            }
            else if (i == start + 10)
            {
               letter = "I";
            }
            else if (i == start + 20)
            {
               letter = "R";
            }
            else if (i == start + 30)
            {
               letter = " ";
            }
            else if (i == start + 40)
            {
               letter = "R";
            }
            else if (i == start + 50)
            {
               letter = "E";
            }
            else if (i == start + 60)
            {
               letter = "L";
            }
            
            g2d.setFont(font_d);
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
      } //   if (wind_mode == 2)
     
      
      // Because the rotate function of Java Graphics takes a radian value as a parameter, we convert 3 degree to radians by the formula (?/180 x 3.0).
      //g2d.rotate((Math.PI / 180.0) * 3.0);  // 360 / 120 = 3.0
      g2d.rotate((Math.PI / 180.0) * 1.0);  // 360 / 360 = 1.0
      
   } // for (int i = 0; i < 360; i++)
   
   // NB Arc2D.Double(double x, double y, double w, double h, double start, double extent, int type)
   //    Constructs a new arc, initialized to the specified location, size, angular extents, and closure type.
   //
   // marker circles
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_1 / 2, -marker_circle_diameter_1 / 2, marker_circle_diameter_1, marker_circle_diameter_1, 360, 360, Arc2D.OPEN));   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_2 / 2, -marker_circle_diameter_2 / 2, marker_circle_diameter_2, marker_circle_diameter_2, 360, 360, Arc2D.OPEN));   // point East = 0 degrees -> to the left
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_AWS_instrument_face_barometer(Graphics2D g2d, boolean pressure_mode_msl)
{
   // eg: https://www.google.nl/search?q=barometer&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjJ78PZg8_UAhWEJMAKHZA3AsAQ_AUICigB&biw=1920&bih=950#imgrc=hE2asDauQrhr6M:
   
   String letter = null;
   String text;
   int angle;
   int start;
   int x;
   int y;
   int width_letter;
   
   
   // main instument area   
   g2d.setPaint(color_instrument_face);
   g2d.fill(new Arc2D.Double(-instrument_diameter / 2, -instrument_diameter / 2, instrument_diameter, instrument_diameter, 0, 360, Arc2D.CHORD));
      
   // outside ring
   g2d.setColor(color_instrument_ring);
   g2d.setStroke(new BasicStroke(thickness_outside_ring));
   g2d.draw(new Arc2D.Double(-instrument_diameter / 2, -instrument_diameter / 2, instrument_diameter, instrument_diameter, 0, 360, Arc2D.CHORD));

   
   g2d.setColor(color_black);
   for (int i = 0; i < 120; i++)    
   {
      // NB first i = <never mind> -> paint starting point = always East -> clockwise
      
      g2d.setFont(font_a);
      
      if (i <= 20 || i >= 40)                          // not the bottom sector
      {
         if (((i % 10) != 0) && ((i % 5) != 0))        // not the 5 and 10 units marks
         {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(units_start, 0, units_end, 0);
         } 
         else if ((i % 5 == 0) && (i % 10 != 0))       // 5 units marks
         {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(units_5_start, 0, units_end, 0);
         }
         else // tens marks                            // 10 units marks
         {
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawLine(units_10_start, 0, units_end, 0);
            
            // eg text = "970"
            
            if (i >= 40)
            {   
               text = String.valueOf(910 + i);        // 910 = start scale
            }
            else
            {
               text = String.valueOf(1030 + i);       // 1030 East position
            }
               
            // text rotated
            //
            // Font metrics
            width_a1 = g2d.getFontMetrics().stringWidth("1000");
            width_a2 = g2d.getFontMetrics().stringWidth("900");

            x = text_base_units_values;
            if ( i > 40 && i < 90)                  // 950 - 1000 hPa
            {
               y = 0 - width_a2 / 2;
            }
            else                                    // >= 1000 hPa
            {
               y = 0 - width_a1 / 2;
            }
            
            // unit numbers e.g. 1010
            angle = 90;
            g2d.translate((float)x,(float)y);
            g2d.rotate(Math.toRadians(angle));
            g2d.drawString(text,0,0);
            
            g2d.rotate(-Math.toRadians(angle));    // rotate back
            g2d.translate(-(float)x,-(float)y);    // translate back
            
         } // else (tens)
         

         // text decoration 'STORMY'
         //
         if (pressure_mode_msl)
         {
            start = 40;
            if (i == 40 || i == start + 3 || i == start + 6 || i == start + 9 || i == start + 12 || i == start + 15)
            {  
               if (i == start)
               {
                  letter = "S";
               }   
               else if (i == start + 3)
               {
                  letter = "T";
               }
               else if (i == start + 6)
               {
                  letter = "O";
               }
               else if (i == start + 9)
               {
                  letter = "R";
               }
               else if (i == start + 12)
               {
                  letter = "M";
               }
               else if (i == start + 15)
               {
                  letter = "Y";
               }
            
               g2d.setFont(font_d);
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
         } //  if (pressure_mode_msl)
         
         // text decoration 'PRESSURE MSL' or 'PRESSURE READ'
         //
         if (pressure_mode_msl)
         {   
            // text decoration 'PRESSURE MSL'
            start = 90;
            if (i == start - 15 || i == start - 12 || i == start - 9 || i == start - 6 || i == start - 3 || i == start || i == start + 3 || i == start + 6 || i == start + 9 || i == start + 12 || i == start + 15 || i == start + 18)
            {  
               if (i == start - 15)
               {
                  letter = "P";
               }  
               else if (i == start - 12)
               {
                  letter = "R";
               }  
               else if (i == start - 9)
               {
                  letter = "E";
               }   
               else if (i == start - 6)
               {
                  letter = "S";
               }
               else if (i == start - 3)
               {
                  letter = "S";
               }
               else if (i == start)
               {
                  letter = "U";
               }
               else if (i == start + 3)
               {
                  letter = "R";
               }
               else if (i == start + 6)
               {
                  letter = "E";
               }
               else if (i == start + 9)
               {
                  letter = " ";
               }
               else if (i == start + 12)
               {
                  letter = "M";
               }
               else if (i == start + 15)
               {
                  letter = "S";
               }
               else if (i == start + 18)
               {
                  letter = "L";
               }
               
               g2d.setFont(font_d);
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
         } // if (pressure_mode_msl)
         else // no pressure MSL (so pressure read) 
         {
            // text decoration 'PRESSURE READ'
            start = 90;
            if (i == start - 18 || i == start - 15 || i == start - 12 || i == start - 9 || i == start - 6 || i == start - 3 || i == start || i == start + 3 || i == start + 6 || i == start + 9 || i == start + 12 || i == start + 15 || i == start + 18)
            {  
               if (i == start - 18)
               {
                  letter = "P";
               }  
               else if (i == start - 15)
               {
                  letter = "R";
               }  
               else if (i == start - 12)
               {
                  letter = "E";
               }  
               else if (i == start - 9)
               {
                  letter = "S";
               }   
               else if (i == start - 6)
               {
                  letter = "S";
               }
               else if (i == start - 3)
               {
                  letter = "U";
               }
               else if (i == start)
               {
                  letter = "R";
               }
               else if (i == start + 3)
               {
                  letter = "E";
               }
               else if (i == start + 6)
               {
                  letter = " ";
               }
               else if (i == start + 9)
               {
                  letter = "R";
               }
               else if (i == start + 12)
               {
                  letter = "E";
               }
               else if (i == start + 15)
               {
                  letter = "A";
               }
               else if (i == start + 18)
               {
                  letter = "D";
               }
               
               g2d.setFont(font_d);
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
         } // else (pressure mode not msl = sensor height)
         
         // text decoration 'VERY DRY'    
         //
         if (pressure_mode_msl)
         {   
            start = 0;
            letter = null;
            if (i == start || i == start + 3 || i == start + 6 || i == start + 9 || i == start + 12 || i == start + 15 || i == start + 18 || i == start + 21)
            {  
               if (i == start)
               {
                  letter = "V";
               }   
               else if (i == start + 3)
               {
                  letter = "E";
               }
               else if (i == start + 6)
               {
                  letter = "R";
               }
               else if (i == start + 9)
               {
                  letter = "Y";
               }
               else if (i == start + 12)
               {
                  letter = "D";
               }
               else if (i == start + 15)
               {
                  letter = "R";
               }
               else if (i == start + 18)
               {
                  letter = "Y";
               }
            
               g2d.setFont(font_d);
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
         } // if (pressure_mode_msl)
         
      } // if (i <= 20 || i >= 40) 
      
      // Because the rotate function of Java Graphics takes a radian value as a parameter, we convert 3 degree to radians by the formula (?/180 x 3.0).
      g2d.rotate((Math.PI / 180.0) * 3.0);  // 360 / 120 = 3.0
      
   }
   
   // NB Arc2D.Double(double x, double y, double w, double h, double start, double extent, int type)
   //    Constructs a new arc, initialized to the specified location, size, angular extents, and closure type.
   //
   // marker circles
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_1 / 2, -marker_circle_diameter_1 / 2, marker_circle_diameter_1, marker_circle_diameter_1, 300, 300, Arc2D.OPEN));   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_2 / 2, -marker_circle_diameter_2 / 2, marker_circle_diameter_2, marker_circle_diameter_2, 300, 300, Arc2D.OPEN));   // point East = 0 degrees -> to the left
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_AWS_instrument_face_thermometer(Graphics2D g2d)
{
   // eg: https://www.google.nl/search?q=barometer&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjJ78PZg8_UAhWEJMAKHZA3AsAQ_AUICigB&biw=1920&bih=950#imgrc=hE2asDauQrhr6M:
   
   String letter = null;
   String text;
   int angle;
   int start;
   int x;
   int y;
   int width_letter;
   
   
   // main instument area   
   g2d.setPaint(color_instrument_face);
   g2d.fill(new Arc2D.Double(-instrument_diameter / 2, -instrument_diameter / 2, instrument_diameter, instrument_diameter, 0, 360, Arc2D.CHORD));
      
   // outside ring
   g2d.setColor(color_instrument_ring);
   g2d.setStroke(new BasicStroke(thickness_outside_ring));
   g2d.draw(new Arc2D.Double(-instrument_diameter / 2, -instrument_diameter / 2, instrument_diameter, instrument_diameter, 0, 360, Arc2D.CHORD));
   
   
   
   g2d.setColor(color_black);
   for (int i = 0; i < 120; i++)    
   {
      // NB first i = <never mind> -> paint starting point = always East -> clockwise
      
      g2d.setFont(font_a);
      
      if (i <= 20 || i >= 40)                          // not the bottom sector
      {
         if (((i % 10) != 0) && ((i % 5) != 0))        // not the 5 and 10 units marks
         {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(units_start, 0, units_end, 0);
         } 
         else if ((i % 5 == 0) && (i % 10 != 0))       // 5 units marks
         {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(units_5_start, 0, units_end, 0);
         }
         else // tens marks                            // 10 units marks
         {
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawLine(units_10_start, 0, units_end, 0);
            
            // eg text = "970"
            if (i >= 40)
            {   
               text = String.valueOf(-90 + i);         // -50 deg = start scale
            }
            else
            {
               text = String.valueOf(30 + i);          // 30 def East position
            }
               
            // text rotated
            //
            // Font metrics
            width_a1 = g2d.getFontMetrics().stringWidth("30");
            width_a2 = g2d.getFontMetrics().stringWidth("-30");

            x = text_base_units_values;
            if ( i > 40 && i < 90)                     // -50 deg - 0 deg
            {
               y = 0 - width_a2 / 2;
            }
            else                                       // >= 0 deg
            {
               y = 0 - width_a1 / 2;
            }
            
            // unit numbers e.g. 30
            angle = 90;
            g2d.translate((float)x,(float)y);
            g2d.rotate(Math.toRadians(angle));
            g2d.drawString(text,0,0);
            
            g2d.rotate(-Math.toRadians(angle));    // rotate back
            g2d.translate(-(float)x,-(float)y);    // translate back
            
         } // else (tens)
         
         // text decoration 'TEMPERATURE'
         start = 90;                           // i = 90 top middle of the dial meter
         if (i == start -15 || i == start -12 || i == start -9 || i == start -6 || i == start -3 || i == start || i == start + 3 || i == start + 6 || i == start + 9 || i == start + 12 || i == start + 15)
         {  
            if (i == start - 15)
            {
               letter = "T";
            }   
            else if (i == start - 12)
            {
               letter = "E";
            }            
            if (i == start - 9)
            {
               letter = "M";
            }   
            else if (i == start - 6)
            {
               letter = "P";
            }
            else if (i == start - 3)
            {
               letter = "E";
            }
            else if (i == start)
            {
               letter = "R";
            }
            else if (i == start + 3)
            {
               letter = "A";
            }
            else if (i == start + 6)
            {
               letter = "T";
            }
            else if (i == start + 9)
            {
               letter = "U";
            }
            else if (i == start + 12)
            {
               letter = "R";
            }
            else if (i == start + 15)
            {
               letter = "E";
            }
            
            
            g2d.setFont(font_d);
            width_letter = g2d.getFontMetrics().stringWidth("S");
            y = 0 - width_letter / 2;
            //int y = 0;
            
            x = text_base_comments;
            angle = 90;
            g2d.translate((float)x,(float)y);
            g2d.rotate(Math.toRadians(angle));
            g2d.drawString(letter,0,0);
            
            g2d.rotate(-Math.toRadians(angle));    // rotate back
            g2d.translate(-(float)x,-(float)y);    // translate back
         }         
            
      } // if (i <= 20 || i >= 40) 
      
      // Because the rotate function of Java Graphics takes a radian value as a parameter, we convert 3 degree to radians by the formula (?/180 x 3.0).
      g2d.rotate((Math.PI / 180.0) * 3.0);  // 360 / 120 = 3.0
      
   }
   
   // NB Arc2D.Double(double x, double y, double w, double h, double start, double extent, int type)
   //    Constructs a new arc, initialized to the specified location, size, angular extents, and closure type.
   //
   // marker circles
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_1 / 2, -marker_circle_diameter_1 / 2, marker_circle_diameter_1, marker_circle_diameter_1, 300, 300, Arc2D.OPEN));   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_2 / 2, -marker_circle_diameter_2 / 2, marker_circle_diameter_2, marker_circle_diameter_2, 300, 300, Arc2D.OPEN));   // point East = 0 degrees -> to the left
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_AWS_instrument_face_rh(Graphics2D g2d)
{
   // eg: https://www.google.nl/search?q=barometer&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjJ78PZg8_UAhWEJMAKHZA3AsAQ_AUICigB&biw=1920&bih=950#imgrc=hE2asDauQrhr6M:
   
   String letter = null;
   String text;
   int angle;
   int start;
   int x;
   int y;
   int width_letter;
   
   
   // main instument area   
   g2d.setPaint(color_instrument_face);
   g2d.fill(new Arc2D.Double(-instrument_diameter / 2, -instrument_diameter / 2, instrument_diameter, instrument_diameter, 0, 360, Arc2D.CHORD));
      
   // outside ring
   g2d.setColor(color_instrument_ring);
   g2d.setStroke(new BasicStroke(thickness_outside_ring));
   g2d.draw(new Arc2D.Double(-instrument_diameter / 2, -instrument_diameter / 2, instrument_diameter, instrument_diameter, 0, 360, Arc2D.CHORD));
   
   
   
   g2d.setColor(color_black);
   for (int i = 0; i < 120; i++)    
   {
      // NB first i = <never mind> -> paint starting point = always East -> clockwise
      
      g2d.setFont(font_a);
      
      if (i <= 20 || i >= 40)                          // not the bottom sector
      {
         if (((i % 10) != 0) && ((i % 5) != 0))        // not the 5 and 10 units marks
         {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(units_start, 0, units_end, 0);
         } 
         else if ((i % 5 == 0) && (i % 10 != 0))       // 5 units marks
         {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(units_5_start, 0, units_end, 0);
         }
         else // tens marks                            // 10 units marks
         {
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawLine(units_10_start, 0, units_end, 0);
            
            // eg text = "50"
            if (i >= 40)
            {   
               text = String.valueOf(-40 + i);         // 0 % = start scale
            }
            else
            {
               text = String.valueOf(80 + i);          // 80 % = East position
            }
               
            // text rotated
            //
            // Font metrics
            width_a1 = g2d.getFontMetrics().stringWidth("50");
            width_a2 = g2d.getFontMetrics().stringWidth("100");

            x = text_base_units_values;
            if (i != 20)                               // not 100%
            {
               y = 0 - width_a1 / 2;
            }
            else                                       // 
            {
               y = 0 - width_a2/ 2;
            }
            
            // unit numbers e.g. 30
            angle = 90;
            g2d.translate((float)x,(float)y);
            g2d.rotate(Math.toRadians(angle));
            g2d.drawString(text,0,0);
            
            g2d.rotate(-Math.toRadians(angle));    // rotate back
            g2d.translate(-(float)x,-(float)y);    // translate back
            
         } // else (tens)
         
         // text decoration 'HUMIDITY'
         start = 90;                           // i = 90 top middle of the dial meter
         if (i == start -9 || i == start -6 || i == start -3 || i == start || i == start + 3 || i == start + 6 || i == start + 9 || i == start + 12)
         {  
            if (i == start - 9)
            {
               letter = "H";
            }   
            else if (i == start - 6)
            {
               letter = "U";
            }
            else if (i == start - 3)
            {
               letter = "M";
            }
            else if (i == start)
            {
               letter = "I";
            }
            else if (i == start + 3)
            {
               letter = "D";
            }
            else if (i == start + 6)
            {
               letter = "I";
            }
            else if (i == start + 9)
            {
               letter = "T";
            }
            else if (i == start + 12)
            {
               letter = "Y";
            }
            
            
            g2d.setFont(font_d);
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
         
      } // if (i <= 20 || i >= 40) 
      
      // Because the rotate function of Java Graphics takes a radian value as a parameter, we convert 3 degree to radians by the formula (?/180 x 3.0).
      g2d.rotate((Math.PI / 180.0) * 3.0);  // 360 / 120 = 3.0
      
   }
   
   // NB Arc2D.Double(double x, double y, double w, double h, double start, double extent, int type)
   //    Constructs a new arc, initialized to the specified location, size, angular extents, and closure type.
   //
   // marker circles
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_1 / 2, -marker_circle_diameter_1 / 2, marker_circle_diameter_1, marker_circle_diameter_1, 300, 300, Arc2D.OPEN));   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_2 / 2, -marker_circle_diameter_2 / 2, marker_circle_diameter_2, marker_circle_diameter_2, 300, 300, Arc2D.OPEN));   // point East = 0 degrees -> to the left
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_AWS_instrument_face_wind_speed(Graphics2D g2d, int wind_mode)
{
   // eg: https://www.google.nl/search?q=barometer&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjJ78PZg8_UAhWEJMAKHZA3AsAQ_AUICigB&biw=1920&bih=950#imgrc=hE2asDauQrhr6M:
   
   String letter = null;
   String text;
   int angle;
   int start;
   int x;
   int y;
   int width_letter;
   
   
   // main instument area   
   g2d.setPaint(color_instrument_face);
   g2d.fill(new Arc2D.Double(-instrument_diameter / 2, -instrument_diameter / 2, instrument_diameter, instrument_diameter, 0, 360, Arc2D.CHORD));
      
   // outside ring
   g2d.setColor(color_instrument_ring);
   g2d.setStroke(new BasicStroke(thickness_outside_ring));
   g2d.draw(new Arc2D.Double(-instrument_diameter / 2, -instrument_diameter / 2, instrument_diameter , instrument_diameter, 0, 360, Arc2D.CHORD));
   
   
   
   g2d.setColor(color_black);
   for (int i = 0; i < 120; i++)    
   {
      // NB first i = <never mind> -> paint starting point = always East -> clockwise
      
      g2d.setFont(font_a);
      
      if (i <= 20 || i >= 40)                          // not the bottom sector
      {
         if (((i % 10) != 0) && ((i % 5) != 0))        // not the 5 and 10 units marks
         {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(units_start, 0, units_end, 0);
         } 
         else if ((i % 5 == 0) && (i % 10 != 0))       // 5 units marks
         {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(units_5_start, 0, units_end, 0);
         }
         else // tens marks                            // 10 units marks
         {
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawLine(units_10_start, 0, units_end, 0);
            
            // eg text = "50"
            if (i >= 40)
            {   
               text = String.valueOf(-40 + i);         // 0 knots = start scale
            }
            else
            {
               text = String.valueOf(80 + i);          // 80 knots = East position
            }
               
            // text rotated
            //
            // Font metrics
            width_a1 = g2d.getFontMetrics().stringWidth("50");
            width_a2 = g2d.getFontMetrics().stringWidth("100");

            x = text_base_units_values;
            if (i != 20)                               // not 100
            {
               y = 0 - width_a1 / 2;
            }
            else                                       // 
            {
               y = 0 - width_a2/ 2;
            }
            
            // unit numbers e.g. 30
            angle = 90;
            g2d.translate((float)x,(float)y);
            g2d.rotate(Math.toRadians(angle));
            g2d.drawString(text,0,0);
            
            g2d.rotate(-Math.toRadians(angle));    // rotate back
            g2d.translate(-(float)x,-(float)y);    // translate back
            
         } // else (tens)
         
         // text decoration 
         // WIND SPEED TRUE (wind_mode 1) or
         // WIND SPEED REL (wind_mode 2) or 
         // WIND GUST TRUE' (wind mode 3)
         //
         start = 90;                           // i = 90 top middle of the dial meter
         if (wind_mode == 3)                   // wind gust true
         {   
            if (i == start -18 || i == start -15 || i == start -12 || i == start -9 || i == start -6 || i == start -3 || i == start || i == start + 3 || i == start + 6 || i == start + 9 || i == start + 12 || i == start + 15  || i == start + 18  || i == start + 21)
            {  
               if (i == start - 18)
               {
                  letter = "W";
               }   
               else if (i == start - 15)
               {
                  letter = "I";
               }   
               if (i == start - 12)
               {
                  letter = "N";
               }   
               else if (i == start - 9)
               {
                  letter = "D";
               }            
               if (i == start - 6)
               {
                  letter = " ";
               }   
               else if (i == start - 3)
               {
                  letter = "G";
               }
               else if (i == start)
               {
                  letter = "U";
               }
               else if (i == start + 3)
               {
                  letter = "S";
               }
               else if (i == start + 6)
               {
                  letter = "T";
               }
               else if (i == start + 9)
               {
                  letter = " ";
               }
               else if (i == start + 12)
               {
                  letter = "T";
               }
                else if (i == start + 15)
               {
                  letter = "R";
               }
               else if (i == start + 18)
               {
                  letter = "U";
               }
               else if (i == start + 21)
               {
                  letter = "E";
               }
             
               g2d.setFont(font_d);
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
         } //if (wind_mode == 3)
         else if (wind_mode == 1 || wind_mode == 2)   // wind true or wind relative
         {
            if (i == start -21 || i == start -18 || i == start -15 || i == start -12 || i == start -9 || i == start -6 || i == start -3 || i == start || i == start + 3 || i == start + 6 || i == start + 9 || i == start + 12 || i == start + 15 || i == start + 18 || i == start + 21)
            {  
               if (i == start - 21)
               {
                  letter = "W";
               }  
               else if (i == start - 18)
               {
                  letter = "I";
               }  
               else if (i == start - 15)
               {
                  letter = "N";
               }  
               else if (i == start - 12)
               {
                  letter = "D";
               }   
               else if (i == start - 9)
               {
                  letter = " ";
               }            
               else if (i == start - 6)
               {
                  letter = "S";
               }   
               else if (i == start - 3)
               {
                  letter = "P";
               }
               else if (i == start)
               {
                  letter = "E ";
               }
               else if (i == start + 3)
               {
                  letter = "E";
               }
               else if (i == start + 6)
               {
                  letter = "D";
               }
               else if (i == start + 9)
               {
                  letter = " ";
               }
               
               
               if (wind_mode == 1)                         // true wind
               {
                  if (i == start + 12)
                  {
                     letter = "T";
                  }
                  else if (i == start + 15)
                  {
                     letter = "R";
                  }
                  else if (i == start + 18)
                  {
                     letter = "U";
                  }
                  else if (i == start + 21)
                  {
                     letter = "E";
                  }
               }
               else if (wind_mode == 2)                 // relative wind
               {
                  if (i == start + 12)
                  {
                     letter = "R";
                  }
                  else if (i == start + 15)
                  {
                    letter = "E";
                  }
                  else if (i == start + 18)
                  {
                     letter = "L";
                  }
                  else if (i == start + 21)
                  {
                     letter = " ";
                  }
               } // else if (wind_mode == 2)
               
               g2d.setFont(font_d);
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
         } // else if (wind_mode == 1 || wind_mode == 2)
      } // if (i <= 20 || i >= 40) 
      
      // Because the rotate function of Java Graphics takes a radian value as a parameter, we convert 3 degree to radians by the formula (?/180 x 3.0).
      g2d.rotate((Math.PI / 180.0) * 3.0);  // 360 / 120 = 3.0
      
   }
   
   // NB Arc2D.Double(double x, double y, double w, double h, double start, double extent, int type)
   //    Constructs a new arc, initialized to the specified location, size, angular extents, and closure type.
   //
   // marker circles
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_1 / 2, -marker_circle_diameter_1 / 2, marker_circle_diameter_1, marker_circle_diameter_1, 300, 300, Arc2D.OPEN));   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_2 / 2, -marker_circle_diameter_2 / 2, marker_circle_diameter_2, marker_circle_diameter_2, 300, 300, Arc2D.OPEN));   // point East = 0 degrees -> to the left
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_AWS_instrument_face_sst(Graphics2D g2d)
{
   // eg: https://www.google.nl/search?q=barometer&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjJ78PZg8_UAhWEJMAKHZA3AsAQ_AUICigB&biw=1920&bih=950#imgrc=hE2asDauQrhr6M:
   
   String letter = null;
   String text;
   int angle;
   int start;
   int x;
   int y;
   int width_letter;
   
   
   // main instument area   
   g2d.setPaint(color_instrument_face);
   g2d.fill(new Arc2D.Double(-instrument_diameter / 2, -instrument_diameter / 2, instrument_diameter, instrument_diameter, 0, 360, Arc2D.CHORD));
      
   // outside ring
   g2d.setColor(color_instrument_ring);
   g2d.setStroke(new BasicStroke(thickness_outside_ring));
   g2d.draw(new Arc2D.Double(-instrument_diameter / 2, -instrument_diameter / 2, instrument_diameter, instrument_diameter, 0, 360, Arc2D.CHORD));
   
   
   
   g2d.setColor(color_black);
   for (int i = 0; i < 60; i++)    
   {
      // NB first i = <never mind> -> paint starting point = always East -> clockwise
      
      g2d.setFont(font_a);
      
      if (i <= 10 || i >= 20)                          // not the bottom sector
      {
         if (((i % 10) != 0) && ((i % 5) != 0))        // not the 5 and 10 units marks
         {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(units_start, 0, units_end, 0);
         } 
         else if ((i % 5 == 0) && (i % 10 != 0))       // 5 units marks
         {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(units_5_start, 0, units_end, 0);
         }
         else // tens marks                            // 10 units marks
         {
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawLine(units_10_start, 0, units_end, 0);
            
            // eg text = "20"
            if (i >= 0 && i <=10)
            {   
               text = String.valueOf(i + 30);         // -50 deg = start scale
            }
            else
            {
               text = String.valueOf(i - 30);          // 30 def East position
            }
               
            // text rotated
            //
            // Font metrics
            width_a1 = g2d.getFontMetrics().stringWidth("-10");
            width_a2 = g2d.getFontMetrics().stringWidth("10");

            x = text_base_units_values;
            if (i == 20)                              // -10 deg sst
            {
               y = 0 - width_a1 / 2;
            }
            else                                       // >= -10 deg
            {
               y = 0 - width_a2 / 2;
            }
            
            // unit numbers e.g. 30
            angle = 90;
            g2d.translate((float)x,(float)y);
            g2d.rotate(Math.toRadians(angle));
            g2d.drawString(text,0,0);
            
            g2d.rotate(-Math.toRadians(angle));    // rotate back
            g2d.translate(-(float)x,-(float)y);    // translate back
            
         } // else (tens)
         
         // text decoration 'SST'
         start = 45;                           // i = 45 top middle of the dial meter
         if (i == start -2 || i == start || i == start + 2)
         {  
            if (i == start - 2)
            {
               letter = "S";
            }
            else if (i == start)
            {
               letter = "S";
            }
            else if (i == start + 2)
            {
               letter = "T";
            }
            
            g2d.setFont(font_d);
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
         
      } // if (i <= 20 || i >= 40) 
      
      // Because the rotate function of Java Graphics takes a radian value as a parameter, we convert 3 degree to radians by the formula (?/180 x 3.0).
      g2d.rotate((Math.PI / 180.0) * 6.0);  // 360 / 60 = 6.0
      
   }
   
   // NB Arc2D.Double(double x, double y, double w, double h, double start, double extent, int type)
   //    Constructs a new arc, initialized to the specified location, size, angular extents, and closure type.
   //
   // marker circles
   g2d.setStroke(new BasicStroke(1.0f));
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_1 / 2, -marker_circle_diameter_1 / 2, marker_circle_diameter_1, marker_circle_diameter_1, 300, 300, Arc2D.OPEN));   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   g2d.draw(new Arc2D.Double(-marker_circle_diameter_2 / 2, -marker_circle_diameter_2 / 2, marker_circle_diameter_2, marker_circle_diameter_2, 300, 300, Arc2D.OPEN));   // point East = 0 degrees -> to the left
   
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



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
 private void draw_AWS_barometer_hands(Graphics2D g2d, boolean pressure_mode_msl) 
 {
      
   //  http://www.developer.com/java/data/how-to-manipulate-complex-graphical-elements-in-java-with-the-2d-api.html
   //
   //
   // NB m x 6 = number of degrees clockwise, start from North
   //
   // m = 0 -> 0 degrees
   // m = 15 -> 90 degrees
   // m = 30 - > 180 degrees
   // m = 45 -> 270 degrees
   //
   //
   double reading;
   //double reading_ppp;
   String update_message;
   String digits;
   int width_b1; 
   int width_c1;  
      
   double xm_a;   
   double ym_a;
 
   double m;                          // main hand
   //double h;                          // second hand
   boolean present_test;
   
   
   ////// TEST ////      
   // main.pressure_MSL_from_AWS_present = true;
   //// TEST //////

   
   if (pressure_mode_msl)            
   {
      present_test = main.pressure_MSL_from_AWS_present;
   }    
   else // relative 
   {
      present_test = main.pressure_sensor_level_from_AWS_present; 
   }    
 
   
   
   ///////// actual pressure at MSL (ic corrected) ////////////         
   //
   if (present_test)   
   {
      if (pressure_mode_msl)
      {
         reading = main_RS232_RS422.dashboard_double_last_update_record_MSL_pressure_ic;       // see: function: main_RS232_RS422.RS232_compute_dasboard_values()
      }  
      else
      {
         reading = main_RS232_RS422.dashboard_double_last_update_record_sensor_level_pressure_ic; 
      }
      
      
      ////// TEST ////      
      //reading = 1033.8;
      //// TEST //////
   
      if (reading >= 950 && reading <= 1050)         
      {
         if (reading >= 1000 && reading < 1060)
         {
            m = (reading - 1000.0) / 2;                  // m = 0 if pointer precisly pointing North; m = 15 if East; m = 30 = South; m = 45 = West
         }
         else // reading < 1000
         {
            m = (1020.0 + ((reading - 950.0) * 2) - reading) / 2;  // eg 970 hPa: (1020 + ((970 - 950) * 2) - 970)  / 2= 45
         }
      }
      else // outside range
      {
         m = 30; // South (hand pointing to nowhere)
      }

   
      // make the arrow(hand) + point
      make_arrow(g2d, m);
      
      ///////////  pressure in digits //////////
      //
      m = 30;
      xm_a = (int) (Math.cos(m * Math.PI / 30 - Math.PI / 2) * text_base_digits);
      ym_a = (int) (Math.sin(m * Math.PI / 30 - Math.PI / 2) * text_base_digits);

      g2d.setFont(font_b);
      g2d.setColor(color_digits);
   
      if (reading > 900.0 && reading < 1100.0)
      {
         digits = Double.toString(reading) + " hPa";
         width_b1 = g2d.getFontMetrics().stringWidth(digits);
         g2d.drawString(digits, (int)xm_a - width_b1 / 2, (int)ym_a);
      }
      
      g2d.setColor(color_black);
   
   
      ///////////  last update date and time ////////////
      //
      // NB dashboard_string_last_update_record_date_time set in Function: update_AWS_dasboard_values()
      //
      g2d.setFont(font_c);
      if (main_RS232_RS422.dashboard_string_last_update_record_date_time.equals(""))
      {
         update_message =  updated_text + ": unknown";
      }
      else
      {
         // remove the seconds from the update string "12-Dec-2017 08:12:24 UTC" -> "12-Dec-2017 08:12 UTC"
         String updated_message_short = main_RS232_RS422.dashboard_string_last_update_record_date_time.substring(0, main_RS232_RS422.dashboard_string_last_update_record_date_time.length() - 7);
         update_message = updated_text + " " + updated_message_short + " UTC";
      }
   
      width_c1 = g2d.getFontMetrics().stringWidth(update_message);
      g2d.drawString(update_message, (int)xm_a - width_c1 / 2, (int)ym_a + 10);
   
   } // if (main.pressure_MSL_from_AWS_present)
   
   
/*   
   ///////////// pressure at sensor height (ic corrected) 3 hours before last update
   //
   // (second pointer/hand)
   //
   if (reading > 900.0 && reading < 1100.0)                               // reading must be a valid value
   {
      reading_ppp = reading - main_RS232_RS422.dashboard_double_last_update_record_ppp; // eg reading = 1020.0 and ppp = 0.7 -> reading_ppp (reading 3 hours before) = 1019.3
      //reading_ppp = reading - main_RS232_RS422.dashboard_AWS_double_last_update_record_ppp; // eg reading = 1020.0 and ppp = 0.7 -> reading_ppp (reading 3 hours before) = 1019.3
     
      if (reading_ppp >= 950 && reading_ppp <= 1050)         
      {
         if (reading_ppp >= 1000 && reading_ppp < 1060)
         {
            h = (reading_ppp - 1000.0) / 2;                                // h = 0 if pointer precisly pointing North; m = 15 if East; m = 30 = South; m = 45 = West
         }
         else // reading < 1000
         {
            h = (1020.0 + ((reading_ppp - 950.0) * 2) - reading_ppp) / 2;  // eg 970 hPa: (1020 + ((970 - 950) * 2) - 970)  / 2= 45
         }
         
         xh_a = Math.cos(h * Math.PI / 30 - Math.PI / 2) * 84 + 0;         // 90 = length hand
         yh_a = Math.sin(h * Math.PI / 30 - Math.PI / 2) * 84 + 0;

         g2d.setColor(color_set_hand);   
         g2d.setStroke(new BasicStroke(1.0f));
         g2d.drawLine(0, 0, (int)xh_a, (int)yh_a);
      }
      else // outside range
      {
         //h = 30; // South (hand pointing to nowhere)
         // do nothing!! (so in that case the white set hand will not be visible)
      }
   } // if (reading > 900.0 && reading < 1100.0) 
*/   
   
 }



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
 private void draw_AWS_thermometer_hand(Graphics2D g2d) 
 {
      
   //  http://www.developer.com/java/data/how-to-manipulate-complex-graphical-elements-in-java-with-the-2d-api.html
   //
   //
   // NB m x 6 = number of degrees clockwise, start from North
   //
   // m = 0  -> 0 degrees
   // m = 15 -> 90 degrees
   // m = 30 -> 180 degrees
   // m = 45 -> 270 degrees
   //
   //
   double reading;
   String update_message;
   String digits;
   int width_b1; 
   int width_c1;  
      
   double xm_a;   
   double ym_a;
   double m;                          // main hand
            
   
   
   ///////// actual air temp at sensor height ////////////         
   //
   if (main.air_temp_from_AWS_present)
   {
      reading = main_RS232_RS422.dashboard_double_last_update_record_air_temp;    
      ////// TEST ////  
      //reading = 6.5;
      //// TEST //////
   
      if (reading >= -60 && reading <= 60)         
      {
         if (reading >= 0 && reading < 60)
         {
            m = (reading + 0.0) / 2;                  // m = 0 if pointer precisly pointing North; m = 15 if East; m = 30 = South; m = 45 = West
         }
         else // reading < 0
         {
            m = (20.0 + ((reading + 50.0) * 2) - reading) / 2;                    // eg -30 C: (20 + ((-30 + 50) * 2) - -30)  / 2 = 45
         }
      }
      else // outside range
      {
         m = 30; // South (hand pointing to nowhere)
      }
      // make the arrow(hand) + point
      make_arrow(g2d, m);
   
      ///////////  temp in digits //////////
      //
      m = 30;
      xm_a = (int) (Math.cos(m * Math.PI / 30 - Math.PI / 2) * text_base_digits);
      ym_a = (int) (Math.sin(m * Math.PI / 30 - Math.PI / 2) * text_base_digits);

      g2d.setFont(font_b);
      g2d.setColor(color_digits);
   
      if (reading > -60.0 && reading < 60.0)
      {
         digits = Double.toString(reading) + " °C";
         width_b1 = g2d.getFontMetrics().stringWidth(digits);
         g2d.drawString(digits, (int) xm_a - width_b1 / 2, (int) ym_a);
      }
      g2d.setColor(color_black);
   
      ///////////  last update date and time ////////////
      //
      g2d.setFont(font_c);
      if (main_RS232_RS422.dashboard_string_last_update_record_date_time.equals(""))
      {
         update_message =  updated_text + ": unknown";
      }
      else
      {
         // remove the seconds from the update string "12-Dec-2017 08:12:24 UTC" -> "12-Dec-2017 08:12 UTC"
         String updated_message_short = main_RS232_RS422.dashboard_string_last_update_record_date_time.substring(0, main_RS232_RS422.dashboard_string_last_update_record_date_time.length() - 7);
         update_message = updated_text + " " + updated_message_short + " UTC";
      }
   
      width_c1 = g2d.getFontMetrics().stringWidth(update_message);
      g2d.drawString(update_message, (int)xm_a - width_c1 / 2, (int)ym_a + 10); 
   } // if (main.air_temp_from_AWS_present)
 }



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
 private void draw_AWS_hygrometer_hand(Graphics2D g2d) 
 {
      
   //  http://www.developer.com/java/data/how-to-manipulate-complex-graphical-elements-in-java-with-the-2d-api.html
   //
   //
   // NB m x 6 = number of degrees clockwise, start from North
   //
   // m = 0  -> 0 degrees
   // m = 15 -> 90 degrees
   // m = 30 -> 180 degrees
   // m = 45 -> 270 degrees
   //
   //
   double reading;
   String update_message;
   String digits;
   int width_b1; 
   int width_c1;  
   double xm_a;   
   double ym_a;
   double m;                          // main hand
            
   
   
   ///////// actual humidity at sensor height ////////////         
   //
   if (main.rh_from_AWS_present)
   {
        
      reading = main_RS232_RS422.dashboard_double_last_update_record_humidity;
      ////// TEST ////   
      //reading = 33.5;
      //// TEST //////
   
      if (reading >= 0 && reading <= 105)         
      {
         if (reading >= 50 && reading <= 105)
         {
            m = (reading - 50.0) / 2;                  // m = 0 if pointer precisly pointing North; m = 15 if East; m = 30 = South; m = 45 = West
         }
         else // reading < 50
         {
            m = (70 + reading) / 2;                                   // eg 20%: (70 + 20) / 2 = 45 (hand pointing West)
         }
      }
      else // outside range
      {
         m = 30; // South (hand pointing to nowhere)
      }
      // make the arrow(hand) + point
      make_arrow(g2d, m);      
   
      ///////////  humidity in digits //////////
      //
      m = 30;
      xm_a = (int) (Math.cos(m * Math.PI / 30 - Math.PI / 2) * text_base_digits);
      ym_a = (int) (Math.sin(m * Math.PI / 30 - Math.PI / 2) * text_base_digits);

      g2d.setFont(font_b);
      g2d.setColor(color_digits);
   
      if (reading >= 0.0 && reading <= 105.0)
      {
         digits = Double.toString(reading) + " %";
         width_b1 = g2d.getFontMetrics().stringWidth(digits);
         g2d.drawString(digits, (int)xm_a - width_b1 / 2, (int)ym_a);
      }
      g2d.setColor(color_black);
   
      ///////////  last update date and time ////////////
      //
      g2d.setFont(font_c);
      if (main_RS232_RS422.dashboard_string_last_update_record_date_time.equals(""))
      {
         update_message =  updated_text + ": unknown";
      }
      else
      {
         // remove the seconds from the update string "12-Dec-2017 08:12:24 UTC" -> "12-Dec-2017 08:12 UTC"
         String updated_message_short = main_RS232_RS422.dashboard_string_last_update_record_date_time.substring(0, main_RS232_RS422.dashboard_string_last_update_record_date_time.length() - 7);
         update_message = updated_text + " " + updated_message_short + " UTC";
      }
   
      width_c1 = g2d.getFontMetrics().stringWidth(update_message);
      g2d.drawString(update_message, (int)xm_a - width_c1 / 2, (int)ym_a + 10); 
   } // if (main.rh_from_AWS_present)
 }



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
 private void draw_AWS_anemometer_hand(Graphics2D g2d, int wind_mode) 
 {
      
   //  http://www.developer.com/java/data/how-to-manipulate-complex-graphical-elements-in-java-with-the-2d-api.html
   //
   //
   // NB m x 6 = number of degrees clockwise, start from North
   //
   // m = 0  -> 0 degrees
   // m = 15 -> 90 degrees
   // m = 30 -> 180 degrees
   // m = 45 -> 270 degrees
   //
   //
   int reading = Integer.MAX_VALUE;
   String update_message;
   String digits;
   int width_b1; 
   int width_c1;  
   boolean present_test = false;
   double xm_a;   
   double ym_a;
   double m;                          // main hand
            
   
   
   if (wind_mode == 1)                                   // true wind
   {
      present_test = main.true_wind_speed_from_AWS_present;
   }    
   else if (wind_mode == 2)                              // relative wind
   {
      present_test = main.relative_wind_speed_from_AWS_present; 
   }    
   else if (wind_mode == 3)                              // true wind gust
   {
      present_test = main.true_wind_gust_from_AWS_present;
   }     
   
   ///////// wind speed ////////////         
   //
   if (present_test)   
   {
      if (wind_mode == 1)                              // true wind speed
      {   
         reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_speed;
      }
      else if (wind_mode == 2)                         // relative wind
      {
         reading = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_speed;
      }
      else if (wind_mode == 3)                         // true wind gust
      {
         reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_gust;
      }
       
      ////// TEST //// 
      //reading = 104.1;
      //// TEST //////
   
      if (reading >= 0 && reading <= 110)         
      {
         if (reading >= 50 && reading <= 110)
         {
            m = ((double)reading - 50.0) / 2;                                // m = 0 if pointer precisly pointing North; m = 15 if East; m = 30 = South; m = 45 = West
         }
         else // reading < 0
         {
            m = (70 + (double)reading) / 2;                                   // eg 20 kn: (70 + 20)  / 2= 45 (hand pointing West)
         }
      }
      else // outside range
      {
         m = 30; // South (hand pointing to nowhere)
      }

      // make the arrow(hand) + point
      make_arrow(g2d, m);      
   
      ///////////  measured value in digits //////////
      //
      m = 30;
      xm_a = (int) (Math.cos(m * Math.PI / 30 - Math.PI / 2) * text_base_digits);
      ym_a = (int) (Math.sin(m * Math.PI / 30 - Math.PI / 2) * text_base_digits);

      g2d.setFont(font_b);
      g2d.setColor(color_digits);

      if (reading >= 0.0 && reading <= 400.0) 
      {
         // so the digit indication is up to 400 kts and the analogue up to 110 kts
         digits = Integer.toString(reading) + " kts";
         width_b1 = g2d.getFontMetrics().stringWidth(digits);
         g2d.drawString(digits, (int) xm_a - width_b1 / 2, (int) ym_a);
      }
      g2d.setColor(color_black);
   
      ///////////  last update date and time ////////////
      //
      g2d.setFont(font_c);
      if (main_RS232_RS422.dashboard_string_last_update_record_date_time.equals("")) 
      {
         update_message = updated_text + ": unknown";
      } 
      else 
      {
         // remove the seconds from the update string "12-Dec-2017 08:12:24 UTC" -> "12-Dec-2017 08:12 UTC"
         String updated_message_short = main_RS232_RS422.dashboard_string_last_update_record_date_time.substring(0, main_RS232_RS422.dashboard_string_last_update_record_date_time.length() - 7);
         update_message = updated_text + " " + updated_message_short + " UTC";
      }

      width_c1 = g2d.getFontMetrics().stringWidth(update_message);
      g2d.drawString(update_message, (int)xm_a - width_c1 / 2, (int)ym_a + 10); 
   } // if (main.true_wind_dir_from_AWS_present)
 }


 
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
 private void draw_AWS_wind_dir_hand(Graphics2D g2d, int wind_mode) 
 {
      
   //  http://www.developer.com/java/data/how-to-manipulate-complex-graphical-elements-in-java-with-the-2d-api.html
   //
   //
   // NB m x 6 = number of degrees clockwise, start from North
   //
   // m = 0  -> 0 degrees
   // m = 15 -> 90 degrees
   // m = 30 -> 180 degrees
   // m = 45 -> 270 degrees
   //
   //
   boolean present_test;
   int reading = Integer.MAX_VALUE;
   String update_message;
   String digits;
   int width_b1; 
   int width_c1;  
   boolean doorgaan;   
   double xm_a;   
   double ym_a;
   double m;                          // main hand
            
   
   if (wind_mode == 1)                // true wind
   {
      present_test = main.true_wind_dir_from_AWS_present;
   }    
   else
   {
      present_test = main.relative_wind_dir_from_AWS_present; 
   }    
   
   
   ///////// actual wind dir (true or relative) ////////////         
   //
   if (present_test)
   {
      if (wind_mode == 1)             // true wind
      {   
         reading = main_RS232_RS422.dashboard_int_last_update_record_true_wind_dir;
      }
      else                           // relative wind
      {
         reading = main_RS232_RS422.dashboard_int_last_update_record_relative_wind_dir;
      }   
      ////// TEST //// 
      //reading = 90;
      //// TEST //////
   
      if (reading >= 0.1 && reading <= 360)                                         // dir = 0 should be impossible     
      {
         //if (reading >= 0 && reading < 60)
         //{
         //   m = (reading + 0.0) / 2;                  // m = 0 if pointer precisly pointing North; m = 15 if East; m = 30 = South; m = 45 = West
         //}
         //else // reading < 0
         //{
         //   m = (20.0 + ((reading + 50.0) * 2) - reading) / 2;                    // eg -30 C: (20 + ((-30 + 50) * 2) - -30)  / 2 = 45
         //}
      
         m = (double)reading / 6;
         doorgaan = true;
      }
      else // outside range
      {
         m = 30; // South (hand pointing to nowhere)
         doorgaan = false;
      }
   
      // only if valid wind dir show the hand
      if (doorgaan)
      {

        // make the arrow(hand) + point
        make_arrow(g2d, m);
   
         ///////////  wind dir in digits //////////
         //
         m = 30;
         xm_a = (int) (Math.cos(m * Math.PI / 30 - Math.PI / 2) * 20 + 0);
         ym_a = (int) (Math.sin(m * Math.PI / 30 - Math.PI / 2) * 20 + 0);

         g2d.setFont(font_b);
         g2d.setColor(color_digits);

         if (reading >= 0.1 && reading <= 360) 
         {
            digits = Integer.toString(reading) + " °";
            width_b1 = g2d.getFontMetrics().stringWidth(digits);
            g2d.drawString(digits, (int) xm_a - width_b1 / 2, (int) ym_a);
         }
         g2d.setColor(color_black);
   
   
         ///////////  last update date and time ////////////
         //
         g2d.setFont(font_c);
         if (main_RS232_RS422.dashboard_string_last_update_record_date_time.equals(""))
         {
            update_message =  updated_text + ": unknown";
         }
         else
         {
            // remove the seconds from the update string "12-Dec-2017 08:12:24 UTC" -> "12-Dec-2017 08:12 UTC"
            String updated_message_short = main_RS232_RS422.dashboard_string_last_update_record_date_time.substring(0, main_RS232_RS422.dashboard_string_last_update_record_date_time.length() - 7);
            update_message = updated_text + " " + updated_message_short + " UTC";
         }
   
         width_c1 = g2d.getFontMetrics().stringWidth(update_message);
         g2d.drawString(update_message, (int)xm_a - width_c1 / 2, (int)ym_a + 10);        
   
      } // if (doorgaan)
   } //  if (main.true_wind_dir_from_AWS_present)
   
 }

 
 
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
 private void draw_AWS_sst_hand(Graphics2D g2d) 
 {
      
   //  http://www.developer.com/java/data/how-to-manipulate-complex-graphical-elements-in-java-with-the-2d-api.html
   //
   //
   // NB m x 6 = number of degrees clockwise, start from North
   //
   // m = 0  -> 0 degrees
   // m = 15 -> 90 degrees
   // m = 30 -> 180 degrees
   // m = 45 -> 270 degrees
   //
   //
   double reading;
   String update_message;
   String digits;
   int width_b1; 
   int width_c1;  
      
   double xm_a;   
   double ym_a;
   double m;                          // main hand
            
   
   
   ///////// actual pressure at sensor height (ic corrected) ////////////         
   //
   if (main.SST_from_AWS_present)
   {
      reading = main_RS232_RS422.dashboard_double_last_update_record_sst;
      ////// TEST ////         
      //reading = 22.7;
      //// TEST //////
   
      if (reading >= -10 && reading <= 40)         
      {
         if (reading >= 15 && reading <= 40)
         {
            m = (reading - 15.0);                  // m = 0 if pointer precisly pointing North; m = 15 if East; m = 30 = South; m = 45 = West
         }
         else // reading < -15
         {
            m = reading + 45.0;                    // eg -30 C: (20 + ((-30 + 50) * 2) - -30)  / 2 = 45
         }
      }
      else // outside range
      {
         m = 30; // South (hand pointing to nowhere)
      }

      // make the arrow(hand) + point
      make_arrow(g2d, m);      
   
      ///////////  temp in digits //////////
      //
      m = 30;
      xm_a = (int) (Math.cos(m * Math.PI / 30 - Math.PI / 2) * text_base_digits);
      ym_a = (int) (Math.sin(m * Math.PI / 30 - Math.PI / 2) * text_base_digits);

      g2d.setFont(font_b);
      g2d.setColor(color_digits);

      if (reading > -60.0 && reading < 60.0) 
      {
         digits = Double.toString(reading) + " °C";
         width_b1 = g2d.getFontMetrics().stringWidth(digits);
         g2d.drawString(digits, (int) xm_a - width_b1 / 2, (int) ym_a);
      }
      g2d.setColor(color_black);
   
      ///////////  last update date and time ////////////
      //
      g2d.setFont(font_c);
      if (main_RS232_RS422.dashboard_string_last_update_record_date_time.equals(""))
      {
         update_message =  updated_text + ": unknown";
      }
      else
      {
         // remove the seconds from the update string "12-Dec-2017 08:12:24 UTC" -> "12-Dec-2017 08:12 UTC"
         String updated_message_short = main_RS232_RS422.dashboard_string_last_update_record_date_time.substring(0, main_RS232_RS422.dashboard_string_last_update_record_date_time.length() - 7);
         update_message = updated_text + " " + updated_message_short + " UTC";
      }
   
      width_c1 = g2d.getFontMetrics().stringWidth(update_message);
      g2d.drawString(update_message, (int)xm_a - width_c1 / 2, (int)ym_a + 10); 
   } // if (main.SST_from_AWS_present)
 }


 
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void make_arrow(Graphics2D g2d, double m)
{       
   double xm_a;   
   double ym_a;
   double xm_b;
   double ym_b;
   double xm_n;
   double ym_n;    
   double xm_m;
   double xm_r;
   double ym_m;    
   double xm_l;
   double ym_l;    
   double xm_k;
   double ym_r;
   double ym_k; 
    
    
   xm_a = Math.cos(m * Math.PI / 30 - Math.PI / 2) * units_start;       
   ym_a = Math.sin(m * Math.PI / 30 - Math.PI / 2) * units_start;

   // hand (pointer) part at the opposite side
   xm_b = Math.cos(m * Math.PI / 30 - Math.PI / 2) * -hand_opposite;      
   ym_b = Math.sin(m * Math.PI / 30 - Math.PI / 2) * -hand_opposite;

   //g2d.setStroke(new BasicStroke(1.0f));
   g2d.setStroke(stroke_main_hand);
   g2d.drawLine((int) xm_b, (int) ym_b, (int) xm_a, (int) ym_a);

      // arrow point (trangle)
      //
      //
      //             N
      //    k     /
      //     \  R/ 
      //      \/
      //     L \
      //        \
      //         M
      //
     
   int arrow_base = 8;
   xm_n = Math.cos(m * Math.PI / 30 - Math.PI / 2) * arrow_n;                 // N = top of arrow point
   ym_n = Math.sin(m * Math.PI / 30 - Math.PI / 2) * arrow_n;                 // N = top of arrow point

   xm_l = Math.cos(m * Math.PI / 30 - Math.PI / 2) * arrow_l;                 // L = middle of the arrow point base     
   ym_l = Math.sin(m * Math.PI / 30 - Math.PI / 2) * arrow_l;                 // L = middle of the arrow point base

   xm_m = xm_l + Math.sin(Math.toRadians(90 - m * 6)) * arrow_base / 2;
   ym_m = ym_l + Math.cos(Math.toRadians(90 - m * 6)) * arrow_base / 2;

   xm_k = xm_l - (Math.sin(Math.toRadians(90 - m * 6)) * arrow_base / 2);
   ym_k = ym_l - (Math.cos(Math.toRadians(90 - m * 6)) * arrow_base / 2);

   xm_r = Math.cos(m * Math.PI / 30 - Math.PI / 2) * arrow_r;                 // notch, makes the arrow point more dynamic     
   ym_r = Math.sin(m * Math.PI / 30 - Math.PI / 2) * arrow_r;                 // notch, makes the arrow point more dynamic

   int xpoints[] = {(int) xm_n, (int) xm_m, (int) xm_r, (int) xm_k};
   int ypoints[] = {(int) ym_n, (int) ym_m, (int) ym_r, (int) ym_k};
   int npoints = 4;

   g2d.fillPolygon(xpoints, ypoints, npoints);
} 
 
 
 
 
   private final String updated_text;
   private final float thickness_outside_ring;
   private final Color color_light_golden_rod;
   private final Color color_black;
   private final Color color_brown;
   private final Color color_dark_red;
   private final Color color_gray;
   private Color color_digits;
   private Color color_instrument_face;
   private Color color_instrument_ring;
   private Color color_update_message_south_panel;
   private final Font font_a;
   private final Font font_b;
   private final Font font_c;
   private final Font font_d;  
   private int width_a1;
   private int width_a2;
   private Stroke stroke_main_hand;
   private final int instrument_diameter;
   private final int vert_offset;
   private final int horz_offset;
   private final int marker_circle_diameter_1;
   private final int marker_circle_diameter_2;
   private final int units_start;
   private final int units_end;
   private final int units_5_start;
   private final int units_10_start;
   private final int text_base_units_values;
   private final int text_base_comments;
   private final int text_base_digits;
   private final int hand_opposite;
   private final int arrow_n;
   private final int arrow_l;
   private final int arrow_r;
   private final int fontSize_a;
   private final int fontSize_b;
   private final int fontSize_c;
   private final int fontSize_d;
}
