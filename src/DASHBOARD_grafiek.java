
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
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
public class DASHBOARD_grafiek extends JPanel {
   
   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public DASHBOARD_grafiek()
{ 
   // colors
   color_black             = Color.BLACK;
   color_light_golden_rod  = new Color(255, 236, 139);
   color_brown             = new Color(139, 69, 19);
   color_white             = Color.WHITE;
   color_dark_red          = new Color(210, 0, 0);
   color_gray              = Color.GRAY;
   color_digits            = Color.BLACK;
   
   // font
   font_a = new Font("SansSerif", Font.PLAIN, 8);
   font_b = new Font("SansSerif", Font.PLAIN, 10);
   font_c = new Font("SansSerif", Font.PLAIN, 5);
   font_d = new Font("Monospaced", Font.ITALIC, 12);
   
   thickness_outside_ring   = 2.0f;
   
   // default stroke for main hand (= pointer/arrow air pressure value); will be overwritten (by a dashed line) if the communucation to the sensor is lost
   stroke_main_hand = new BasicStroke(1.0f);
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
   
   // clear dirty pixels
   super.paintComponent(g);
   
   Graphics2D g2d = (Graphics2D) g;
   
   // background photo (not in night vision mode)
   //
   if (DASHBOARD_view.night_vision == false)
   {
      Image img1 = new ImageIcon(this.getClass().getResource(main.ICONS_DIRECTORY + main.DASHBOARD_LOGO)).getImage();
      // scale the image to cover a the complete area of the drawing surface
      //g2d.drawImage(img1, 0, 0, getWidth(), getHeight(), 0, 0, img1.getWidth(null), img1.getHeight(null), null);   
      
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
   
   
   g2d.translate(getWidth() / 2, getHeight() / 2);
   int side = getWidth() > getHeight() ? getHeight() : getWidth();
   g2d.scale(side / 250, side / 250);
   setAllRenderingHints(g2d);
   
   // colors depending night or day vision
   if (DASHBOARD_view.night_vision == true)
   {
      color_instrument_face = color_dark_red;
      color_instrument_ring = color_black;
      color_set_hand = color_gray;
      color_digits = color_black;
      color_update_message_north_panel = color_gray;
   }
   else
   {
      color_instrument_face = color_light_golden_rod;
      color_instrument_ring = color_brown;
      color_set_hand = color_white;
      color_digits = color_black;
      color_update_message_north_panel = color_black;
   }
   
   // obsolete data (no cummunication for roughly > 5 minutes with the AWS)
   if (main.displayed_barometer_data_obsolate == true)
   {
      color_digits = main.obsolate_color_data_from_aws;             // will not be visible antmore on the same color instrument face
      color_instrument_face = main.obsolate_color_data_from_aws;
      color_instrument_ring = color_black;
      color_set_hand = main.obsolate_color_data_from_aws;
      
      //overwrite the stroke of the main hand (and set the stroke of the copy, not the original) 
      float dash1[] = {5.0f};     // where the values alternate between the dash length and the gap length. a transparent dash for 5 units, another opaque dash for 5 units
      stroke_main_hand = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
   }

   
   
   
   //
   ///////////////////// "last updated date and time" top right screen [North panel]
   //
   String updated_text_bottom_screen = "barometer updated every minute. last updated: ";
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

   DASHBOARD_view.jLabel4.setForeground(color_update_message_north_panel);
   DASHBOARD_view.jLabel4.setText(update_message);   
   
   
   
   // 
   //////////////////// draw barometer
   //
   draw_instrument_face(g2d);
   draw_barometer_hands(g2d);
   draw_instrument_face_central_knob(g2d);
   
}   
   


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void draw_instrument_face_central_knob(Graphics2D g2d)
{
   // centre knob
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
private void draw_instrument_face(Graphics2D g2d)
{
   // eg: https://www.google.nl/search?q=barometer&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjJ78PZg8_UAhWEJMAKHZA3AsAQ_AUICigB&biw=1920&bih=950#imgrc=hE2asDauQrhr6M:
   
   int text_base = 55;                              // from the cente basis of "STORMY, CHANGE and VERYDRY"
   
   
   // main instument area   
   //g2d.setPaint(Color.white);
   g2d.setPaint(color_instrument_face);
   g2d.fill(new Arc2D.Double(-110, -110, 220, 220, 0, 360, Arc2D.CHORD));
      
   // outside ring
   g2d.setColor(color_instrument_ring);
   g2d.setStroke(new BasicStroke(thickness_outside_ring));
   g2d.draw(new Arc2D.Double(-110, -110, 220, 220, 0, 360, Arc2D.CHORD));

   
   
   
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
            g2d.drawLine(92, 0, 96, 0);
         } 
         else if ((i % 5 == 0) && (i % 10 != 0))       // 5 units marks
         {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.drawLine(89, 0, 96, 0);
         }
         else // tens marks                            // 10 units marks
         {
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawLine(88, 0, 96, 0);
            
            // eg text = "970"
            String text;
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

            //int x = 88 - 8;                         // 8 = 96 - 88 (length small hPa mark)
            int x = 100;                         // 8 = 96 - 88 (length small hPa indivator line)
            int y;
            if ( i > 40 && i < 90)                  // 950 - 1000 hPa
            {
               y = 0 - width_a2 / 2;
            }
            else                                    // >= 1000 hPa
            {
               y = 0 - width_a1 / 2;
            }
            
            // unit numbers e.g. 1010
            int angle = 90;
            g2d.translate((float)x,(float)y);
            g2d.rotate(Math.toRadians(angle));
            g2d.drawString(text,0,0);
            
            g2d.rotate(-Math.toRadians(angle));    // rotate back
            g2d.translate(-(float)x,-(float)y);    // translate back
            
         } // else (tens)
         

         // text decoration 'STORMY'
         int start = 40;
         if (i == start || i == start + 3 || i == start + 6 || i == start + 9 || i == start + 12 || i == start + 15)
         {  
            String letter = null;
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
            int width_letter = g2d.getFontMetrics().stringWidth("S");
            int y = 0 - width_letter / 2;
            //int y = 0;
            
            int x = text_base;
            int angle = 90;
            g2d.translate((float)x,(float)y);
            g2d.rotate(Math.toRadians(angle));
            g2d.drawString(letter,0,0);
            
            g2d.rotate(-Math.toRadians(angle));    // rotate back
            g2d.translate(-(float)x,-(float)y);    // translate back
         }         
         
       
         // text decoration 'CHANGE'
         start = 83;
         if (i == start || i == start + 3 || i == start + 6 || i == start + 9 || i == start + 12 || i == start + 15)
         {  
            String letter = null;
            if (i == start)
            {
               letter = "C";
            }   
            else if (i == start + 3)
            {
               letter = "H";
            }
            else if (i == start + 6)
            {
               letter = "A";
            }
            else if (i == start + 9)
            {
               letter = "N";
            }
            else if (i == start + 12)
            {
               letter = "G";
            }
            else if (i == start + 15)
            {
               letter = "E";
            }
            
            g2d.setFont(font_d);
            int width_letter = g2d.getFontMetrics().stringWidth("S");
            int y = 0 - width_letter / 2;
            //int y = 0;
            
            int x = text_base;
            int angle = 90;
            g2d.translate((float)x,(float)y);
            g2d.rotate(Math.toRadians(angle));
            g2d.drawString(letter,0,0);
            
            g2d.rotate(-Math.toRadians(angle));    // rotate back
            g2d.translate(-(float)x,-(float)y);    // translate back
         }         
                  
          
          // text decoration 'VERY DRY'
         start = 0;
         if (i == start || i == start + 3 || i == start + 6 || i == start + 9 || i == start + 12 || i == start + 15 || i == start + 18 || i == start + 21)
         {  
            String letter = null;
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
            int width_letter = g2d.getFontMetrics().stringWidth("S");
            int y = 0 - width_letter / 2;
            //int y = 0;
            
            int x = text_base;
            int angle = 90;
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
   g2d.draw(new Arc2D.Double(-97, -97, 194, 194, 300, 300, Arc2D.OPEN));   // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left
   g2d.draw(new Arc2D.Double(-92, -92, 184, 184, 300, 300, Arc2D.OPEN));   // point East = 0 degrees -> to the left
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
 private void draw_barometer_hands(Graphics2D g2d) 
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
   double reading_ppp;
   String update_message;
   String pressure_digits;
   int width_b1; 
   int width_c1;  
    
      
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
   double xh_a;
   double yh_a;
   double xh_b;
   double yh_b;
   double m;                          // main hand
   double h;                          // second hand
            
            // test values
            //double reading = 940.5;
            //double reading = 1000.0;
            //double reading = 1015.0;
            //double reading = 1020.5;
            //double reading = 1040.5;
            //double reading = 1050.5; 
            //double reading = 948.8;
            //double reading = 960.8;
            //double reading = 985.5;
            
   
   
   ///////// main hand, actual pressure at sensor height (ic applied) ////////////         
   //
   reading = main_RS232_RS422.dashboard_double_last_update_record_pressure_ic;     // see: function: main_RS232_RS422.RS232_compute_dasboard_values()
   //reading = 1033.8;
   
   if (reading >= 950 && reading <= 1050)         
   {
      if (reading >= 1000 && reading < 1060)
      {
         m = (reading - 1000.0) / 2;                                       // m = 0 if pointer precisly pointing North; m = 15 if East; m = 30 = South; m = 45 = West
      }
      else // reading < 1000
      {
         m = (1020.0 + ((reading - 950.0) * 2) - reading) / 2;             // eg 970 hPa: (1020 + ((970 - 950) * 2) - 970)  / 2= 45
      }
   }
   else // outside range
   {
      m = 30; // South (hand pointing to nowhere)
   }
   
   xm_a = Math.cos(m * Math.PI / 30 - Math.PI / 2) * 92;                // 92 = length hand
   ym_a = Math.sin(m * Math.PI / 30 - Math.PI / 2) * 92;

   // hand (pointer) part at the opposite side
   xm_b = Math.cos(m * Math.PI / 30 - Math.PI / 2) * -30;               // 30 = length hand opposite side
   ym_b = Math.sin(m * Math.PI / 30 - Math.PI / 2) * -30;
      
   //g2d.setStroke(new BasicStroke(1.0f));
   g2d.setStroke(stroke_main_hand);
   g2d.drawLine((int)xm_b, (int)ym_b, (int)xm_a, (int)ym_a);
   
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
   
   //
   int arrow_base = 8;
   xm_n = Math.cos(m * Math.PI / 30 - Math.PI / 2) * 84;                 // N = top of arrow point
   ym_n = Math.sin(m * Math.PI / 30 - Math.PI / 2) * 84;                 // N = top of arrow point
   
   xm_l = Math.cos(m * Math.PI / 30 - Math.PI / 2) * 70;                 // L = middle of the arrow point base     
   ym_l = Math.sin(m * Math.PI / 30 - Math.PI / 2) * 70;                 // L = middle of the arrow point base
   
   xm_m = xm_l +  Math.sin(Math.toRadians(90 - m * 6)) * arrow_base / 2; 
   ym_m = ym_l +  Math.cos(Math.toRadians(90 - m * 6)) * arrow_base / 2;
  
   xm_k = xm_l - (Math.sin(Math.toRadians(90 - m * 6)) * arrow_base / 2); 
   ym_k = ym_l - (Math.cos(Math.toRadians(90 - m * 6)) * arrow_base / 2);

   xm_r = Math.cos(m * Math.PI / 30 - Math.PI / 2) * 73 + 0;                 // notch, makes the arrow point more dynamic     
   ym_r = Math.sin(m * Math.PI / 30 - Math.PI / 2) * 73 + 0;                 // notch, makes the arrow point more dynamic
   
   int xpoints[] = {(int)xm_n, (int)xm_m, (int)xm_r, (int)xm_k};
   int ypoints[] = {(int)ym_n, (int)ym_m, (int)ym_r, (int)ym_k};
   int npoints = 4;
    
   g2d.fillPolygon(xpoints, ypoints, npoints);
   
   
   ///////////  pressure in digits //////////
   //
   m = 30;
   xm_a = (int) (Math.cos(m * Math.PI / 30 - Math.PI / 2) * 90);
   ym_a = (int) (Math.sin(m * Math.PI / 30 - Math.PI / 2) * 90);

   g2d.setColor(color_digits);
   g2d.setFont(font_b);
   
   if (reading > 900.0 && reading < 1100.0)
   {
      pressure_digits = Double.toString(reading) + " hPa";
      width_b1 = g2d.getFontMetrics().stringWidth(pressure_digits);
      g2d.drawString(pressure_digits, (int)xm_a - width_b1 / 2, (int)ym_a);
   }
   
   
   ///////////  last update date and time ////////////
   //
   g2d.setColor(color_black);
   g2d.setFont(font_c);
   if (main_RS232_RS422.dashboard_string_last_update_record_date_time.equals(""))
   {
      update_message =  "last update unknown";
   }
   else
   {
      //update_message = "last update " + main_RS232_RS422.dashboard_string_last_update_record_date_time;
      // remove the seconds from the update string "12-Dec-2017 08:12:24 UTC" -> "12-Dec-2017 08:12 UTC"
      String updated_message_short = main_RS232_RS422.dashboard_string_last_update_record_date_time.substring(0, main_RS232_RS422.dashboard_string_last_update_record_date_time.length() - 7);
      update_message = "last update" + " " + updated_message_short + " UTC";
   }
   
   width_c1 = g2d.getFontMetrics().stringWidth(update_message);
   g2d.drawString(update_message, (int)xm_a - width_c1 / 2, (int)ym_a + 10);
   
   
   ///////////  barometer type text label ////////////
   //
   String barometer_type = "";
   g2d.setFont(font_c);
   
   if  (main.RS232_connection_mode == 1)
   {
      barometer_type =  "Vaisala PTB220";
   }
   else if (main.RS232_connection_mode == 2)
   {
      barometer_type =  "Vaisala PTB330";
   }
   else if (main.RS232_connection_mode == 4)
   {
      barometer_type =  "Mintaka Duo";
   }
   else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)
   {
      barometer_type =  "Mintaka Star";
   }
      
   width_c1 = g2d.getFontMetrics().stringWidth(barometer_type);
   g2d.drawString(barometer_type, (int)xm_a - width_c1 / 2, (int)ym_a + 15);
   
   
   ///////////// pressure at sensor height (ic corrected) 3 hours before last update
   //
   // (second pointer/hand)
   //
   if (reading > 900.0 && reading < 1100.0)        // reading must be a valid value
   {
      reading_ppp = reading - main_RS232_RS422.dashboard_double_last_update_record_ppp; // eg reading = 1020.0 and ppp = 0.7 -> reading_ppp (reading 3 hours before) = 1019.3
     
      if (reading_ppp >= 950 && reading_ppp <= 1050)         
      {
         if (reading_ppp >= 1000 && reading_ppp < 1060)
         {
            h = (reading_ppp - 1000.0) / 2;                               // h = 0 if pointer precisly pointing North; m = 15 if East; m = 30 = South; m = 45 = West
         }
         else // reading < 1000
         {
            h = (1020.0 + ((reading_ppp - 950.0) * 2) - reading_ppp) / 2;  // eg 970 hPa: (1020 + ((970 - 950) * 2) - 970)  / 2= 45
         }
         
         // hand (pointer)
         xh_a = Math.cos(h * Math.PI / 30 - Math.PI / 2) * 84;         // 84 = length set hand
         yh_a = Math.sin(h * Math.PI / 30 - Math.PI / 2) * 84;
         
         // hand (pointer) part at the opposite side
         xh_b = Math.cos(h * Math.PI / 30 - Math.PI / 2) * -20;        // 10 = length hand opposite side
         yh_b = Math.sin(h * Math.PI / 30 - Math.PI / 2) * -20;

         g2d.setColor(color_set_hand);   
         g2d.setStroke(new BasicStroke(1.0f));
         g2d.drawLine((int)xh_b, (int)yh_b, (int)xh_a, (int)yh_a);
      }
      else // outside range
      {
         //h = 30; // South (hand pointing to nowhere)
         // do nothing!! (so in that case the white set hand will not be visible)
      }
   } // if (reading > 900.0 && reading < 1100.0) 
   
   
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






   private final float thickness_outside_ring;
   private final Color color_light_golden_rod;
   private final Color color_black;
   private final Color color_brown;
   private final Color color_white;
   //private final Color color_red;
   private final Color color_dark_red;
   private final Color color_gray;
   private Color color_instrument_face;
   private Color color_instrument_ring;
   private Color color_set_hand;
   private Color color_digits;
   private Color color_update_message_north_panel;
   private final Font font_a;
   private final Font font_b;
   private final Font font_c;
   private final Font font_d;
   private int width_a1;
   private int width_a2;
   private Stroke stroke_main_hand;

}
