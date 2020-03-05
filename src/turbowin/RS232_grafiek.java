package turbowin;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hometrainer
 */
public class RS232_grafiek extends JPanel{
    
    
    
/***********************************************************************************************/
/*                                                                                             */
/*                                RS232_AWS_grafiek()                                          */
/*                                                                                             */
/***********************************************************************************************/
public RS232_grafiek()
{
   // initialisation
   for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
   {
      RS232_view.points[i] = new Point2D.Double(0, 0);
      RS232_view.sensor_waarde_array[i]    = "";             // 1st meteo instrument
      RS232_view.sensor_waarde_array_2[i]  = "";             // 1st meteo instrument
		RS232_view.sensor_waarde_array_c[i]  = "";             // 1st meteo instrument
		RS232_view.sensor_waarde_array_d[i]  = "";             // 1st meteo instrument
      RS232_view.sensor_waarde_array_II[i] = "";             // 2nd meteo instrument
   }

   //JOptionPane.showMessageDialog(null, "in RS232_AWS_grafiek", "test", JOptionPane.WARNING_MESSAGE);
   //init_sensor_data_uit_file_ophalen_timer();

}
    
    
 
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
@Override
public void paintComponent(Graphics g)
{
   super.paintComponent(g);

   double lengte_y_as_mark               = 5;
   double afstand_aanduiding_naar_marker = 3;  // distance between end of indication and begin of marker
   double lengte_x_as_mark               = 5;
   double linker_kantlijn_margin         = 80;//60; // x left top point
   double boven_kantlijn_margin          = 40; // y left top point
   double rechter_kantlijn_margin        = 80;//60;
   double onder_kantlijn_margin          = 60;
   int aantal_time_markers_x_as          = 0;
   int aantal_secondary_lines;                 // number of secondary (help/intermidiate) lines between two markers (e.g. 10 help lines between 1000 hPa and 1010 hPa)

   Point2D links_boven_grafiek = new Point2D.Double(linker_kantlijn_margin, boven_kantlijn_margin);
   Point2D links_onder_grafiek = new Point2D.Double(linker_kantlijn_margin, getHeight() - onder_kantlijn_margin);

   Point2D rechts_boven_grafiek = new Point2D.Double(getWidth() - rechter_kantlijn_margin, boven_kantlijn_margin);
   Point2D rechts_onder_grafiek = new Point2D.Double(getWidth() - rechter_kantlijn_margin, getHeight() - onder_kantlijn_margin);


	// graph top left (a)
	Point2D a_links_boven_grafiek  = links_boven_grafiek;
	Point2D a_links_onder_grafiek  = new Point2D.Double(linker_kantlijn_margin, getHeight() / 2 - onder_kantlijn_margin);
	Point2D a_rechts_boven_grafiek = new Point2D.Double(getWidth() / 2 - rechter_kantlijn_margin, boven_kantlijn_margin);
	Point2D a_rechts_onder_grafiek = new Point2D.Double(getWidth() / 2 - rechter_kantlijn_margin, getHeight() / 2 - onder_kantlijn_margin);
	
	// graph top right (b)
	Point2D b_links_boven_grafiek  = new Point2D.Double(getWidth() / 2 + linker_kantlijn_margin, boven_kantlijn_margin);
	Point2D b_links_onder_grafiek  = new Point2D.Double(getWidth() / 2 + linker_kantlijn_margin, getHeight() / 2 - onder_kantlijn_margin);
	Point2D b_rechts_boven_grafiek = new Point2D.Double(getWidth() - rechter_kantlijn_margin, boven_kantlijn_margin);
	Point2D b_rechts_onder_grafiek = new Point2D.Double(getWidth() - rechter_kantlijn_margin, getHeight() / 2 - onder_kantlijn_margin);
	
	// graph bottom left (c)
	Point2D c_links_boven_grafiek  = new Point2D.Double(linker_kantlijn_margin, getHeight() / 2 + boven_kantlijn_margin);
	Point2D c_links_onder_grafiek  = new Point2D.Double(linker_kantlijn_margin, getHeight() - onder_kantlijn_margin);
	Point2D c_rechts_boven_grafiek = new Point2D.Double(getWidth() / 2 - rechter_kantlijn_margin, getHeight() / 2 + boven_kantlijn_margin);
	Point2D c_rechts_onder_grafiek = new Point2D.Double(getWidth() / 2 - rechter_kantlijn_margin, getHeight() - onder_kantlijn_margin);
	
	// graph bottom right (d)
	Point2D d_links_boven_grafiek  = new Point2D.Double(getWidth() / 2 + linker_kantlijn_margin, getHeight() / 2 + boven_kantlijn_margin);
	Point2D d_links_onder_grafiek  = new Point2D.Double(getWidth() / 2 + linker_kantlijn_margin, getHeight() - onder_kantlijn_margin);
	Point2D d_rechts_boven_grafiek = new Point2D.Double(getWidth() - rechter_kantlijn_margin, getHeight() / 2 + boven_kantlijn_margin);
	//Point2D d_rechts_onder_grafiek = new Point2D.Double(getWidth() - rechter_kantlijn_margin, getHeight() - onder_kantlijn_margin);
	Point2D d_rechts_onder_grafiek = rechts_onder_grafiek;
	

   Graphics2D g2 = (Graphics2D)g;      // device

   //Font font_xx = new Font("Verdana", Font.BOLD, 12);
	//g2.setFont(font_xx);	
	
	
   Color color_raster        = null;
   Color color_pen           = null;
   Color color_date_time     = new Color(0, 0, 128);
	
	Color color_raster_orange   = new Color(255, 153, 51);
   Color color_raster_blue     = new Color(205, 104, 137);          // palevioletred 3 
   Color color_raster_blue_air = new Color(114, 160, 193);          //  Air Superiority Blue
   Color color_raster_blue_sst = new Color(0, 153, 153);            //  sea water color
   Color color_pen_black     = Color.BLACK;
   
   
   
   // in night mode theme than truck for oposite pen color
   //if (main.theme_mode .equals(main.THEME_NIGHT))
   //{
   //   Color color_very_light_gray  = new Color(204, 204, 204);      // RGB 204,204,204 same as used in high contrat theme (secondary2 in class ContrastTheme)
   //   color_pen_black = color_very_light_gray;
   //}
   //if (main.theme_mode.equals(main.THEME_NIGHT))
   if (RS232_view.night_vision == true)    
   {
      // overwrite default raster color (see also initComponents1()[RS232_view.java])
      color_raster_orange  = new Color(255, 0, 0);      // RGB 204,204,204 same as used in high contrat theme (secondary2 in class ContrastTheme)
      color_raster = color_raster_orange;
   }
   
   
   if ((main.mode_grafiek).equals(main.MODE_ALL_PARAMETERS))   // 4 graphs view
   {
		Point2D lb_grafiek = null;
		Point2D lo_grafiek = null;
		Point2D rb_grafiek = null;
		Point2D ro_grafiek = null;
		Point2D marker_begin_x_as = null;
		Point2D marker_eind_x_as = null;
	   Point2D verticaal_hulp_lijn_begin = null;
		Point2D verticaal_hulp_lijn_eind = null;
		Point2D marker_begin_linker_y_as = null;
		Point2D marker_eind_linker_y_as = null;
		Point2D marker_begin_rechter_y_as = null;
		Point2D marker_eind_rechter_y_as = null;
		Point2D horizontaal_hulp_lijn_begin = null;
		Point2D horizontaal_hulp_lijn_eind = null;
		Line2D line_marker_linker_y_as = null;
		Line2D line_marker_rechter_y_as = null;
		Line2D hulp_lijn = null;
		String uur_aanduiding = null;
		String string_aanduiding = "";
	   double x_as_lengte = 0;
		double schaling_x_as = 0;
		double y_as_lengte = 0;
		double schaling = 0;
		double x_pos_marker =  0;
		double y_pos_marker = 0;
		//double y_pos_sub_marker = 0;
		double x_waarde = 0;
      double y_waarde = 0;
		

		
		for (int k = 0; k < 4; k++)
		{
			// NB
			// --- k=0; graph a; top left; pressure
			// --- k=1; graph b; top right; wind dir
			// --- k=2; graph c; bottom left; air temp
			// --- k=3; graph d; bottom right; wind speed
			
			if (k == 0)       // graph top left (a) pressure
			{
				lb_grafiek = a_links_boven_grafiek;		 
				lo_grafiek = a_links_onder_grafiek;
				ro_grafiek = a_rechts_onder_grafiek;
				rb_grafiek = a_rechts_boven_grafiek;
				rb_grafiek = a_rechts_boven_grafiek;
				
			   parameter_start_waarde        = 950;           // lowest parameter value in graph
			   aantal_parameter_markers_y_as = 10;            // actually 11 markers (if source included)
			   aantal_units_tussen_2_markers = 10;            // nb units = hPa or degrees or wind speed
				
			   color_raster = color_raster_orange;
				color_pen = color_pen_black;
			}
			else if (k == 1)   // graph top right (b) wind dir
			{
				lb_grafiek = b_links_boven_grafiek;	
				lo_grafiek = b_links_onder_grafiek;
				ro_grafiek = b_rechts_onder_grafiek;	
            rb_grafiek = b_rechts_boven_grafiek;		
				
            parameter_start_waarde        = 0;              // lowest parameter value in graph
            aantal_parameter_markers_y_as = 36;             // actually 37 markers (if source included)
            aantal_units_tussen_2_markers = 10;             // nb units = hPa or degrees or wind speed
				
				color_raster = color_raster_orange;
            color_pen = color_pen_black;
			}			
			else if (k == 2)   // graph bottom left (c); air temp 
			{
				lb_grafiek = c_links_boven_grafiek;		  
				lo_grafiek = c_links_onder_grafiek;
				ro_grafiek = c_rechts_onder_grafiek;
				rb_grafiek = c_rechts_boven_grafiek;
				
            parameter_start_waarde        = -50;             // lowest parameter value in graph
            aantal_parameter_markers_y_as = 10;              // actually 11 markers (if source included)
            aantal_units_tussen_2_markers = 10;              // nb units = hPa or degrees or wind speed
				
				color_raster = color_raster_orange;
				color_pen = color_pen_black;
			}			
			else if (k == 3)   // graph bottom right (d) wind speed
			{
				lb_grafiek = d_links_boven_grafiek;		  
				lo_grafiek = d_links_onder_grafiek;
				ro_grafiek = d_rechts_onder_grafiek;
            rb_grafiek = d_rechts_boven_grafiek;		
				
            parameter_start_waarde        = 0;                // lowest parameter value in graph
            aantal_parameter_markers_y_as = 10;               // actually 11 markers (if source included)
            aantal_units_tussen_2_markers = 10;               // nb units = hPa or degrees or wind speed
				
				color_raster = color_raster_orange;
            color_pen = color_pen_black;
			}			
			
		
			// safe method for constructing a square
			g2.setPaint(color_raster);
			g2.setStroke(new BasicStroke(2f));

			Rectangle2D rect_k = new Rectangle2D.Double();
			rect_k.setFrameFromDiagonal(lb_grafiek, ro_grafiek);
			g2.draw(rect_k);

		   if (lo_grafiek != null && lb_grafiek != null)
		   {
		      y_as_lengte  = lo_grafiek.getY() - lb_grafiek.getY();
		   }
         schaling = y_as_lengte / aantal_parameter_markers_y_as;

         //
         // x-axis values (date-time)
         //
         if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
         {
            aantal_time_markers_x_as                     = 25; // 25 hours, actually 26 markers if source also included
         }
         else if((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
         {
            aantal_time_markers_x_as                     = 29; // 7 days every 6 hours (so 4 per dag), actually  30 markers if source also included
         }


         int aantal_5_minuten_perioden_tussen_2_markers = 0;
         if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
         {
            aantal_5_minuten_perioden_tussen_2_markers = 12;
         }
         else if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
         {
            aantal_5_minuten_perioden_tussen_2_markers = 72;              // 12 * 6 (12 blokjes -van 5 minuten per uur- en dan per 6 uur)
         }

		   if (ro_grafiek != null && lo_grafiek != null)
		   {
            x_as_lengte   = ro_grafiek.getX() - lo_grafiek.getX();
            schaling_x_as = x_as_lengte / aantal_time_markers_x_as;
	   	}
		
		
         //
			// markers and help lines in graph
			//
			g2.setPaint(color_raster);
			g2.setStroke(new BasicStroke(1f));
		
         //
         // x-axis
         //
         for (int i = 0; i <= aantal_time_markers_x_as; i++)
         {
			   if (lo_grafiek != null)
			   {	
               x_pos_marker = lo_grafiek.getX() + (i * schaling_x_as);
			   }	

            // markers (small lines) below at x-as
            //
            g2.setStroke(new BasicStroke(1f));

			   if (lo_grafiek != null)
			   {	
               marker_begin_x_as  = new Point2D.Double(x_pos_marker, lo_grafiek.getY()+ lengte_x_as_mark);
				   marker_eind_x_as   = new Point2D.Double(x_pos_marker, lo_grafiek.getY());
			   }

            Line2D line_marker_x_as = new Line2D.Double(marker_begin_x_as, marker_eind_x_as);
            g2.draw(line_marker_x_as);

            // numbers (hour indication) determine for below x-as
				//                          (see page 212, 213 Core Java Volume I)
				Font font_x = new Font("SansSerif", Font.BOLD, 11);
				g2.setFont(font_x);
				String test_aanduiding_x = "12";
				FontRenderContext context_x = g2.getFontRenderContext();
				Rectangle2D bounds_x = font_x.getStringBounds(test_aanduiding_x, context_x);
				double stringWidth_x = bounds_x.getWidth();
				double ascent_x = -bounds_x.getY();                           // ascent = helling/steilte (hoogte characters)

            // NB drawString kan niet met een Point2D argument

            cal_grafiek_datum_tijd = new GregorianCalendar();
            if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
            {
               cal_grafiek_datum_tijd.add(Calendar.HOUR_OF_DAY, -aantal_time_markers_x_as + 1 + i);
            }
            else if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
            {
               cal_grafiek_datum_tijd.add(Calendar.HOUR_OF_DAY, -(aantal_time_markers_x_as * 6) + 6 + (i * 6));
            }

            // hour indications x-axis
            //
			   if (lo_grafiek != null)
			   {	
               uur_aanduiding = RS232_view.sdf5.format(cal_grafiek_datum_tijd.getTime());
               g2.drawString(uur_aanduiding, (int)(x_pos_marker - stringWidth_x / 2), (int)(lo_grafiek.getY() + lengte_x_as_mark + ascent_x));
			   }
			
            // date indication x-axis
            //
            int int_uur_aanduiding = Integer.parseInt(uur_aanduiding);

            if ( ( (RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY) && (int_uur_aanduiding == 0 || int_uur_aanduiding == 12)) ||
                 ( (RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK)) && (int_uur_aanduiding == 0 || int_uur_aanduiding == 1 || int_uur_aanduiding == 2 || int_uur_aanduiding == 3 || int_uur_aanduiding == 4 || int_uur_aanduiding == 5) )
            {
               g2.setPaint(color_date_time);

					//Font font_datum = new Font("Serif", Font.BOLD, 14);
					Font font_datum = new Font("Verdana", Font.BOLD, 11);
					g2.setFont(font_datum);
					String test_aanduiding_datum = "02-12-2016";
					FontRenderContext context_datum = g2.getFontRenderContext();
					Rectangle2D bounds_datum = font_datum.getStringBounds(test_aanduiding_datum, context_datum);
					double ascent_datum = -bounds_datum.getY();                           // ascent = helling/steilte (hoogte characters)

               String dag_van_de_week_aanduiding = RS232_view.sdf7.format(cal_grafiek_datum_tijd.getTime());
               //String datum_aanduiding = RS232_view.sdf6.format(cal_grafiek_datum_tijd.getTime()) + " UTC";
               String datum_aanduiding = RS232_view.sdf6.format(cal_grafiek_datum_tijd.getTime());
            
               if (i != aantal_time_markers_x_as)  // niet bij de allerlaaste x-as marker (komt dan voorbij de rechter y-as)
               {
					   if (lo_grafiek != null)
					   {	
                     // eg Thuesday
                     //g2.drawString(dag_van_de_week_aanduiding, (int)(x_pos_marker), (int)(lo_grafiek.getY() + lengte_x_as_mark + (2 * ascent_datum)));
                     g2.drawString(dag_van_de_week_aanduiding, (int)(x_pos_marker), (int)(lo_grafiek.getY() + lengte_x_as_mark + (2.5 * ascent_datum)));

                     // eg 12-05-2016
                     //g2.drawString(datum_aanduiding, (int)(x_pos_marker), (int)(lo_grafiek.getY() + lengte_x_as_mark + (3 * ascent_datum)));
                     g2.drawString(datum_aanduiding, (int)(x_pos_marker), (int)(lo_grafiek.getY() + lengte_x_as_mark + (3.5 * ascent_datum)));
							
					   }
               } // if (i != aantal_time_markers_x_as)

               g2.setPaint(color_raster);                      // reset
            
            } // if (int_uur_aanduiding == 0 || etc.


            // vertical secondary lines
            //
            if (i != 0) // the y-axis (x = 0) itself do not over write
            {
               g2.setStroke(new BasicStroke(1f));

               //g2.setPaint(Color.LIGHT_GRAY);
               if (lo_grafiek != null && lb_grafiek != null)
				   {	
				      verticaal_hulp_lijn_begin  = new Point2D.Double(x_pos_marker, lo_grafiek.getY());
                  verticaal_hulp_lijn_eind   = new Point2D.Double(x_pos_marker, lb_grafiek.getY());
				   }
				
               hulp_lijn = new Line2D.Double(verticaal_hulp_lijn_begin, verticaal_hulp_lijn_eind);
               g2.draw(hulp_lijn);
            } // if (i != 0)
         } // for (int i = 0; i <= aantal_time_markers_x_as; i++)
			
         //
         // y-axis
         //
         Font font_y = new Font("SansSerif", Font.BOLD, 11);
         g2.setFont(font_y);
      
         String test_aanduiding_y = "";
         if (k == 0)                             // graph a top left; (pressure)
         {
            test_aanduiding_y = "1000 hPa";
         }
         else if (k == 1)                        // graph b top right; (wind dir)
         {
            test_aanduiding_y = "120 °";
         }
         else if (k == 2)                        // graph c bottom left; (air temp)
         {
            test_aanduiding_y = "-10 °C";
         }
         else if (k == 3)                        // bottom right; graph d (wind speed)
         {
            test_aanduiding_y = "100 kts";
         }
      
			FontRenderContext context_y = g2.getFontRenderContext();
			Rectangle2D bounds_y = font_y.getStringBounds(test_aanduiding_y, context_y);
			double stringWidth_y = bounds_y.getWidth();
			double ascent_y = -bounds_y.getY();

         for (int i = 0; i <= aantal_parameter_markers_y_as; i++)
         {	
				if ((k != 1) || (k == 1 && i % 3 == 0))  // if wind dir plot only every 30 degrees
				{	

				   if (lo_grafiek != null)
				   {	
				      y_pos_marker = lo_grafiek.getY() - (i * (schaling));
				   }	

               // markers at left x-axis
				   //
				   g2.setStroke(new BasicStroke(1f));

				   if (lo_grafiek != null)
				   {
				      marker_begin_linker_y_as = new Point2D.Double(lo_grafiek.getX() - lengte_y_as_mark, y_pos_marker);
				      marker_eind_linker_y_as = new Point2D.Double(lo_grafiek.getX(), y_pos_marker);
				   }
				
				   line_marker_linker_y_as = new Line2D.Double(marker_begin_linker_y_as, marker_eind_linker_y_as);
				   g2.draw(line_marker_linker_y_as);

				   // NB drawString doesn't work with a Point2D argument
				   int int_aanduiding = parameter_start_waarde + i * aantal_units_tussen_2_markers;

				   String aanduiding = "";
				   if (k == 0)        // top left; graph a; pressure graph         
				   {
                  aanduiding = Integer.toString(int_aanduiding) + " hPa";
               }
               else if (k == 1)   // top right; graph b; wind dir
               {
                  aanduiding = Integer.toString(int_aanduiding) + " °";                 // e.g. 13 -> 130°
               }
               else if (k == 2)   // bottom left; graph c; air temp
               {
                  aanduiding = Integer.toString(int_aanduiding) + " °C";   
               }
               else if (k == 3)   // bottom right; graph d; wind speed
               {
                  if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)
                  {
                     aanduiding = Integer.toString(int_aanduiding) + " kts";
                  }
                  else
                  {
                     aanduiding = Integer.toString(int_aanduiding) + " m/s";
                  }
               }
         
               // indications at left y-axis
               //
				   if (marker_begin_linker_y_as != null)
				   {	
                  g2.drawString(aanduiding, (int)(marker_begin_linker_y_as.getX() - stringWidth_y - afstand_aanduiding_naar_marker), (int)(y_pos_marker + ascent_y / 2));
				   }

               // markers at right y-axis
               //
               g2.setStroke(new BasicStroke(1f));

				   if (ro_grafiek != null)
				   {	
                  marker_begin_rechter_y_as  = new Point2D.Double(ro_grafiek.getX(), y_pos_marker);
				   }
				
				   if (ro_grafiek != null)
				   {
				      marker_eind_rechter_y_as   = new Point2D.Double(ro_grafiek.getX() + lengte_y_as_mark, y_pos_marker);
				   }
				
				   if (marker_begin_rechter_y_as != null)
				   {
                  line_marker_rechter_y_as = new Line2D.Double(marker_begin_rechter_y_as, marker_eind_rechter_y_as);
				   }
				   g2.draw(line_marker_rechter_y_as);
				

               // indications at right y-as
               //
				   if (marker_eind_rechter_y_as != null)
				   {	
                  g2.drawString(aanduiding, (int)(marker_eind_rechter_y_as.getX() + afstand_aanduiding_naar_marker), (int)(y_pos_marker + /*(stringHeight - leading) / 2*/ ascent_y / 2));
				   }
				} // if ((k != 1) || (k == 1 && i % 3 == 0))
				
				
            // horizontal main lines
            //
				if ((k != 1 && i != 0) || (k == 1 && i % 3 == 0 && i != 0)) // the x-axis (y = 0) itself do not over write and if wind dir plot lines only every 30 degrees	
            {
               g2.setStroke(new BasicStroke(2f));

               if (lo_grafiek != null && ro_grafiek != null)
					{	
					   horizontaal_hulp_lijn_begin  = new Point2D.Double(lo_grafiek.getX(), y_pos_marker);
					   horizontaal_hulp_lijn_eind   = new Point2D.Double(ro_grafiek.getX(), y_pos_marker);
					} 
					
               hulp_lijn = new Line2D.Double(horizontaal_hulp_lijn_begin, horizontaal_hulp_lijn_eind);
               g2.draw(hulp_lijn);
            } // if (i != 0)

/*				
				// horizontal secondary lines
				//
            g2.setStroke(new BasicStroke(1f));

            if (i != aantal_parameter_markers_y_as)
            {
               // horizontal sec. lines (eg 10 secondary help lines between 2 lines)
               if (k == 1)                           // graph b; top right; wind dir
               {
                  aantal_secondary_lines = 0;              // otherwise to crowdy
               }
               else
               {
                  aantal_secondary_lines = aantal_units_tussen_2_markers;
               }
            
               for (int j = 0; j < aantal_secondary_lines; j++)    
               {
                  if (lo_grafiek != null)
						{	
						   y_pos_sub_marker = (lo_grafiek.getY() - (i * schaling)) - (j * (schaling / aantal_units_tussen_2_markers));
						}
						
						if (lo_grafiek != null && ro_grafiek != null)
						{	
						   horizontaal_hulp_lijn_begin  = new Point2D.Double(lo_grafiek.getX(), y_pos_sub_marker);
						   horizontaal_hulp_lijn_eind   = new Point2D.Double(ro_grafiek.getX(), y_pos_sub_marker);
						}
						
                  hulp_lijn = new Line2D.Double(horizontaal_hulp_lijn_begin, horizontaal_hulp_lijn_eind);
                  g2.draw(hulp_lijn);

               } // for (int j = 0; j < aantal_units_tussen_2_markers; j++)
            } // if (i != aantal_parameter_markers_y_as)
*/
         } // for (int i = 0; i <= aantal_markers_y_as; i++)
			
			
         ////// meta informatie
         //
         if (k == 0)                  // graph a; top left; pressure     
         {
            string_aanduiding = "Air pressure at MSL";
         }   
         else if (k == 1)             // graph b; top righ; wind dir
         {
            string_aanduiding = "True wind dir at sensor ht";
         } 			
         else if (k == 2)             // graph c; bottom left; air temp   
         {
            string_aanduiding = "Air temp at sensor height";
         }  			
         else if (k == 3)             // graph d; bottom right; wind speed  
         {
            string_aanduiding = "True wind speed at sensor ht";
         }  
      
         if (string_aanduiding.equals("") == false)
         {
            // NB color is the color of the raster 
            //Font font_sensor_level_text = new Font("Serif", Font.BOLD, 14);
				Font font_sensor_level_text = new Font("Verdana", Font.BOLD, 11);
            g2.setFont(font_sensor_level_text);
                  
            FontRenderContext context_sensor_level_font = g2.getFontRenderContext();
            Rectangle2D bounds_string_aanduiding = font_sensor_level_text.getStringBounds(string_aanduiding, context_sensor_level_font);
            double ascent_sensor_level_text = -bounds_string_aanduiding.getY();     // hoogte char's                      // ascent = helling/steilte (hoogte characters)
            
				if (lb_grafiek != null)
				{	
				   //g2.drawString(string_aanduiding, (int)(lb_grafiek.getX()), (int)(2 * ascent_sensor_level_text));
					g2.drawString(string_aanduiding, (int)(lb_grafiek.getX()), (int)(lb_grafiek.getY() - (0.5 * ascent_sensor_level_text)));
				}
         } // if (string_aanduiding.equals("") == false) 

			
			
			
      
         /////////////////////////// START INPUT DATA /////////////////////////////

         g2.setStroke(new BasicStroke(2.5f));
         g2.setPaint(color_pen);


         // initialisation
         for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
         {
            RS232_view.points[i] = new Point2D.Double(0, 0);
         }

			if (k == 0)                  // graph a; top left; air pressure
			{	
            for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
            {
               //System.out.println("--- " + "sensor_waarde_array[" + i + "] = " + sensor_waarde_array[i]);
					//if (i == 0)
					//{		  
               //System.out.println("--- " + "RS232_view.sensor_waarde_array_2[" + i + "] = " + RS232_view.sensor_waarde_array_2[i]);
					//}

               if (!RS232_view.sensor_waarde_array[i].equals(""))
               {
                  try
                  {
                     double sensor_waarde;
                     sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim());
               
                     if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
                     {
                        if (lo_grafiek != null)
							   {	
							      x_waarde = lo_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
							   }	
                     }
                     else if((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
                     {
							   if (lo_grafiek != null)
							   {	
                           x_waarde = lo_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
							   }
                     }

                     if (lo_grafiek != null)
						   {	
						      y_waarde = lo_grafiek.getY() - ((sensor_waarde - parameter_start_waarde) / (double)aantal_units_tussen_2_markers * schaling);
						   } 
						
						   RS232_view.points[i] = new Point2D.Double(x_waarde, y_waarde);
                  }
                  catch (NumberFormatException e) 
                  { 
                     //System.out.println("--- " + "Function paintComponent() " + e);
                  }
               } // if (!RS232_view.sensor_waarde_array[i].equals(""))
            } // for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
			} // (if k == 0)
			

			else if (k == 1)                  // graph b; top right; wind dir
			{	
            for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
            {
               //System.out.println("--- " + "sensor_waarde_array[" + i + "] = " + sensor_waarde_array[i]);

               if (!RS232_view.sensor_waarde_array_2[i].equals(""))
               {
                  try
                  {
                     double sensor_waarde;
                     sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_2[i].trim());

							// NB North is 0 degr. from the sensors, this must be plotted as 360 degr for consistency with the wind observations
                     if (sensor_waarde < 0.5)
                     {
                        sensor_waarde = 360;
                     }
               
                     if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
                     {
                        if (lo_grafiek != null)
							   {	
							      x_waarde = lo_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
							   }	
                     }
                     else if((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
                     {
							   if (lo_grafiek != null)
							   {	
                           x_waarde = lo_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
							   }
                     }

                     if (lo_grafiek != null)
						   {	
						      y_waarde = lo_grafiek.getY() - ((sensor_waarde - parameter_start_waarde) / (double)aantal_units_tussen_2_markers * schaling);
						   } 
						
						   RS232_view.points[i] = new Point2D.Double(x_waarde, y_waarde);
                  }
                  catch (NumberFormatException e) 
                  { 
                     //System.out.println("--- " + "Function paintComponent() " + e);
                  }
               } // if (!RS232_view.sensor_waarde_array[i].equals(""))
            } // for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
			} // (if k == 1)
			

			else if (k == 2)                  // graph c; bottom left; air temp
			{	
            for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
            {
					//if (i == 0)
					//{		  
               //System.out.println("--- " + "RS232_view.sensor_waarde_array_c[" + i + "] = " + RS232_view.sensor_waarde_array_c[i]);
					//}
					
               if (!RS232_view.sensor_waarde_array_c[i].equals(""))
					//if (RS232_view.sensor_waarde_array_c[i] != null)	
               {
                  try
                  {
                     double sensor_waarde;
                     sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_c[i].trim());
               
                     if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
                     {
                        if (lo_grafiek != null)
							   {	
							      x_waarde = lo_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
							   }	
                     }
                     else if((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
                     {
							   if (lo_grafiek != null)
							   {	
                           x_waarde = lo_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
							   }
                     }

                     if (lo_grafiek != null)
						   {	
						      y_waarde = lo_grafiek.getY() - ((sensor_waarde - parameter_start_waarde) / (double)aantal_units_tussen_2_markers * schaling);
						   } 
						
						   RS232_view.points[i] = new Point2D.Double(x_waarde, y_waarde);
                  }
                  catch (NumberFormatException e) 
                  { 
                     //System.out.println("--- " + "Function paintComponent() " + e);
                  }
               } // if (!RS232_view.sensor_waarde_array[i].equals(""))
            } // for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
			} // (if k == 2)
			

			else if (k == 3)                  // graph d; bottom right; wind speed
			{	
            for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
            {
               //System.out.println("--- " + "sensor_waarde_array[" + i + "] = " + sensor_waarde_array[i]);

               if (!RS232_view.sensor_waarde_array_d[i].equals(""))
					//if (RS232_view.sensor_waarde_array_d[i] != null)	
               {
                  try
                  {
                     double sensor_waarde = Double.MAX_VALUE;
                     
                     // m/s -> knots
                     //sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_d[i].trim()) * main.M_S_KNOT_CONVERSION;
                    
                     // NB 4 options:
                     // 1) measured/observed in m/s   -> dashboard and graph in knots
                     // 2) measured/observed in m/s   -> dashboard and graph in m/s
                     // 3) measured/observed in knots -> dashboard and graph in knots
                     // 4) measured/observed in knots -> dashboard and graph in m/s
                     
                     if (main.wind_units.indexOf(main.M_S) != -1)                   // wind_units 'as measured' set to m/s by observer in Maintenance -> Station data
                     {
                        if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // measured in m/s and dashboard in knots
                        {
                           sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_d[i].trim()) * main.M_S_KNOT_CONVERSION;
                        }
                        else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // measured in m/s and dashboard in m/s
                        {
                           sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_d[i].trim()); 
                        }
                     }
                     else if (main.wind_units.indexOf(main.KNOTS) != -1)            // wind_units 'as measured' set to knots by observer in Maintenance -> Station data
                     {
                        if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // measured in knots and dashboard in knots
                        {
                           sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_d[i].trim()); 
                        }
                        else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // measured in knots and dashboard in m/s
                        {
                           sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_d[i].trim()) * main.KNOT_M_S_CONVERSION; 
                        }   
                     }
                     
               
                     if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
                     {
                        if (lo_grafiek != null)
							   {	
							      x_waarde = lo_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
							   }	
                     }
                     else if((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
                     {
							   if (lo_grafiek != null)
							   {	
                           x_waarde = lo_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
							   }
                     }

                     if (lo_grafiek != null)
						   {	
						      y_waarde = lo_grafiek.getY() - ((sensor_waarde - parameter_start_waarde) / (double)aantal_units_tussen_2_markers * schaling);
						   } 
						
						   RS232_view.points[i] = new Point2D.Double(x_waarde, y_waarde);
                  }
                  catch (NumberFormatException e) 
                  { 
                     //System.out.println("--- " + "Function paintComponent() " + e);
                  }
               } // if (!RS232_view.sensor_waarde_array[i].equals(""))
            } // for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
			} // (if k == 1)
		
			
         // plot
			//
         GeneralPath path = new GeneralPath();
         for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
         {
            if (RS232_view.points[i].getX() != 0)
            {
               path.moveTo((float)RS232_view.points[i].getX(), (float)RS232_view.points[i].getY());
               if (i + 1 < RS232_view.AANTAL_PLOT_POINTS)
               {
                  if (RS232_view.points[i + 1].getX() != 0)
                  {
                     path.lineTo((float)RS232_view.points[i + 1].getX(), (float)RS232_view.points[i + 1].getY());  // Resets the path to empty. The append position is set back to the beginning of the path and all coordinates and point types are forgotten
                  }
                  else
                  {
                     g2.draw(path);
                     path.reset();
                  }
               } // if (i + 1 < RS232_view.AANTAL_PLOT_POINTS)
               else
               {
                  g2.draw(path);
               } // else
            } // if (points[i].getX() != 0)
         } // for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)

      
         // write as a string the last parameter value (update every 5 minutes); top right corner above graph
         //
			double digitale_sensor_waarde = 99999;
			String jaar   = "";
			String maand  = "";
			String dag    = "";
			String uur    = "";
			String minuut = "";
			
			if (k == 0)                          // graph a; top left; pressure
			{
            for (int i = RS232_view.AANTAL_PLOT_POINTS - 1; i >= 0; i--) 
		      {				
               if (!RS232_view.sensor_waarde_array[i].equals("")) 
			      {
				      try
						{
					      digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim());
							
							jaar   = RS232_view.datum_tijd_array[i].substring(0, 4);
						   maand  = RS232_view.datum_tijd_array[i].substring(4, 4 + 2);
						   dag    = RS232_view.datum_tijd_array[i].substring(6, 6 + 2);
						   uur    = RS232_view.datum_tijd_array[i].substring(8, 8 + 2);
						   minuut = RS232_view.datum_tijd_array[i].substring(10, 10 + 2);
						}
						catch (NumberFormatException e)
						{
							digitale_sensor_waarde = 99999;		  
						}
						
						break;
					} // if (!RS232_view.sensor_waarde_array[i].equals(""))
			   } // for (int i = RS232_view.AANTAL_PLOT_POINTS - 1; i >= 0; i--)
			} // if (k == 0)
			
			else if (k == 1)                     // graph b; top right; wind dir
			{
            for (int i = RS232_view.AANTAL_PLOT_POINTS - 1; i >= 0; i--) 
		      {				
               if (!RS232_view.sensor_waarde_array_2[i].equals("")) 
			      {
				      try
						{
					      digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_2[i].trim());
							
							jaar   = RS232_view.datum_tijd_array_b[i].substring(0, 4);
						   maand  = RS232_view.datum_tijd_array_b[i].substring(4, 4 + 2);
						   dag    = RS232_view.datum_tijd_array_b[i].substring(6, 6 + 2);
						   uur    = RS232_view.datum_tijd_array_b[i].substring(8, 8 + 2);
						   minuut = RS232_view.datum_tijd_array_b[i].substring(10, 10 + 2);
						}
						catch (NumberFormatException e)
						{
							digitale_sensor_waarde = 99999;		  
						}
						
						break;
					} // if (!RS232_view.sensor_waarde_array[i].equals(""))
			   } // for (int i = RS232_view.AANTAL_PLOT_POINTS - 1; i >= 0; i--)
			} // else if (k == 1)
				
			else if (k == 2)                       // graph c; bottom left; air temp
			{
            for (int i = RS232_view.AANTAL_PLOT_POINTS - 1; i >= 0; i--) 
		      {				
               if (!RS232_view.sensor_waarde_array_c[i].equals("")) 
			      {
				      try
						{
					      digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_c[i].trim());
							
							jaar   = RS232_view.datum_tijd_array_c[i].substring(0, 4);
						   maand  = RS232_view.datum_tijd_array_c[i].substring(4, 4 + 2);
						   dag    = RS232_view.datum_tijd_array_c[i].substring(6, 6 + 2);
						   uur    = RS232_view.datum_tijd_array_c[i].substring(8, 8 + 2);
						   minuut = RS232_view.datum_tijd_array_c[i].substring(10, 10 + 2);
						}
						catch (NumberFormatException e)
						{
							digitale_sensor_waarde = 99999;		  
						}
						
						break;
					} // if (!RS232_view.sensor_waarde_array[i].equals(""))
			   } // for (int i = RS232_view.AANTAL_PLOT_POINTS - 1; i >= 0; i--)
			} // else if (k == 2)
			
			else if (k == 3)                  // graph d; bottom right; wind speed
			{
            for (int i = RS232_view.AANTAL_PLOT_POINTS - 1; i >= 0; i--) 
		      {				
               if (!RS232_view.sensor_waarde_array_d[i].equals("")) 
			      {
				      try
						{
					      digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_d[i].trim());
							
							jaar   = RS232_view.datum_tijd_array_d[i].substring(0, 4);
						   maand  = RS232_view.datum_tijd_array_d[i].substring(4, 4 + 2);
						   dag    = RS232_view.datum_tijd_array_d[i].substring(6, 6 + 2);
						   uur    = RS232_view.datum_tijd_array_d[i].substring(8, 8 + 2);
						   minuut = RS232_view.datum_tijd_array_d[i].substring(10, 10 + 2);
						}
						catch (NumberFormatException e)
						{
							digitale_sensor_waarde = 99999;		  
						}
						
						break;
					} // if (!RS232_view.sensor_waarde_array[i].equals(""))
			   } // for (int i = RS232_view.AANTAL_PLOT_POINTS - 1; i >= 0; i--)
			} // else if (k == 3)
			
			
			// converse and rounding
			if (k == 3)                        // graph d; bottom right; wind speed 
			{
			   // m/s -> knots
			   //digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10 * main.M_S_KNOT_CONVERSION) / 10.0d;
            
            if (main.wind_units.indexOf(main.M_S) != -1)                   // wind_units 'as measured' set to m/s by observer in Maintenance -> Station data
            {
               if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // measured in m/s and dashboard in knots
               {
                  digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10 * main.M_S_KNOT_CONVERSION) / 10.0d;
               }
               else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // measured in m/s and dashboard in m/s
               {
                  digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d; 
               }
            }
            else if (main.wind_units.indexOf(main.KNOTS) != -1)            // wind_units 'as measured' set to knots by observer in Maintenance -> Station data
            {
               if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // measured in knots and dashboard in knots
               {
                  digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;
               }
               else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // measured in knots and dashboard in m/s
               {
                  digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10 * main.KNOT_M_S_CONVERSION) / 10.0d;
               }   
            }           
			} 
			else 
			{
			   digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;  // eg 998.19 -> 998.2
			}
			
			// write the string top right
			if ((digitale_sensor_waarde > -50) && (digitale_sensor_waarde < 1100)) // max/min of max limits of the parameters pressure, air temp, sst and wind speed
			{
				String datum_tijd_parameter_string = "";
				String test_aanduiding_datum_tijd_parameter = "";
				
				if (k == 0)                   // graph a; top left; pressure 
				{
				   datum_tijd_parameter_string = dag + "-" + maand + "-" + jaar + " " + uur + ":" + minuut + " UTC  " + digitale_sensor_waarde + " hPa";
			      test_aanduiding_datum_tijd_parameter = "17-04-2016 09:35 UTC 1020.7 hPa";
				} 
				else if (k == 1)              // graph b; top right; wind dir 
				{
				   datum_tijd_parameter_string = dag + "-" + maand + "-" + jaar + " " + uur + ":" + minuut + " UTC  " + digitale_sensor_waarde + " °";
					test_aanduiding_datum_tijd_parameter = "17-04-2016 09:35 UTC 120 °";
				}
				else if (k == 2)              // graph c; bottom left; air temp 
				{
				   datum_tijd_parameter_string = dag + "-" + maand + "-" + jaar + " " + uur + ":" + minuut + " UTC  " + digitale_sensor_waarde + " °C";
					test_aanduiding_datum_tijd_parameter = "17-04-2016 09:35 UTC 14.8 °C";
				} 
				else if (k == 3)              // graph d; bottom right; wind speed
				{
               if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)       // on graph (and dashboard) in knots
               {
				      datum_tijd_parameter_string = dag + "-" + maand + "-" + jaar + " " + uur + ":" + minuut + " UTC  " + digitale_sensor_waarde + " kts";
               }
               else // m/s
               {
                  datum_tijd_parameter_string = dag + "-" + maand + "-" + jaar + " " + uur + ":" + minuut + " UTC  " + digitale_sensor_waarde + " m/s";
               }
               
					test_aanduiding_datum_tijd_parameter = "17-04-2016 09:35 UTC 23 kts";
				} 

				//Font font_datum_tijd_parameter = new Font("Serif", Font.BOLD, 14);
				Font font_datum_tijd_parameter = new Font("Verdana", Font.BOLD, 11);
				g2.setFont(font_datum_tijd_parameter);
							
				FontRenderContext context_datum_tijd_parameter = g2.getFontRenderContext();
				Rectangle2D bounds_datum_tijd_pressure = font_datum_tijd_parameter.getStringBounds(test_aanduiding_datum_tijd_parameter, context_datum_tijd_parameter);
			   //double stringWidth_datum = bounds_datum.getWidth();
			   double ascent_datum_tijd_pressure = -bounds_datum_tijd_pressure.getY();     // hoogte char's                      // ascent = helling/steilte (hoogte characters)
			   double length_datum_tijd_pressure = bounds_datum_tijd_pressure.getWidth();     // lengte string

			   //g2.drawString(datum_tijd_parameter_string, (int) (rechts_boven_grafiek.getX() - length_datum_tijd_pressure), (int) (2 * ascent_datum_tijd_pressure));
			   if (rb_grafiek != null)
				{	
				   g2.drawString(datum_tijd_parameter_string, (int) (rb_grafiek.getX() - length_datum_tijd_pressure), (int)(rb_grafiek.getY() - (0.5 * ascent_datum_tijd_pressure)));
				}
			} //if ((digitale_sensor_waarde > -50) && (digitale_sensor_waarde < 1100)) // max/min of max limits of the parameters pressure, air temp, sst and wind speed
			

			
		
			////////////////////////// START INPUT DATA /////////////////////////////	
			
		} // for (int k = 0; k < 4; k++)
	} // if ((main.mode_grafiek).equals(MODE_ALL_PARAMETERS)) 
	
	
	else // 1 graph view
	{
      //Color color_raster        = null;
      //Color color_pen           = null;
      //Color color_date_time     = new Color(0, 0, 128);

      color_raster = color_raster_orange;
      color_pen    = color_pen_black;
      
      if (main.mode_grafiek.equals(main.MODE_PRESSURE))
      {
         //Color color_raster_green  = new Color(97, 192, 97);
         //Color color_raster_orange = new Color(255, 140, 0);
         //Color color_pen_red       = Color.RED;
         //Color color_pen_black     = Color.BLACK;
         
         //color_raster = color_raster_orange;
         //color_pen    = color_pen_black;
      }
      else if (main.mode_grafiek.equals(main.MODE_AIRTEMP) || main.mode_grafiek.equals(main.MODE_AIRTEMP_II))
      {
         //Color color_raster_blue_air = new Color(114, 160, 193);          //  Air Superiority Blue
         //Color color_pen_red         = Color.RED;
         
         //color_raster = color_raster_blue_air;
         //color_pen    = color_pen_red;
      }
      else if (main.mode_grafiek.equals(main.MODE_SST))
      {
         //Color color_raster_blue_sst = new Color(0, 153, 153);           //  sea water color
         //Color color_pen_blue      = Color.BLUE;
         
         //color_raster = color_raster_blue_sst;
         //color_pen    = color_pen_blue;
      }
      else if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))
      {
         //Color color_raster_blue   = new Color(205, 104, 137);          // palevioletred 3 
         //Color color_pen_dark_gray = Color.DARK_GRAY;
         
         //color_raster = color_raster_blue;
         //color_pen    = color_pen_dark_gray;
      }
      else if (main.mode_grafiek.equals(main.MODE_WIND_DIR))
      {
         //Color color_raster_blue   = new Color(205, 104, 137);          // palevioletred 3 
         //Color color_pen_dark_gray = Color.DARK_GRAY;
         
         //color_raster = color_raster_blue;
         //color_pen    = color_pen_dark_gray;
      }
      else
      {
         JOptionPane.showMessageDialog(null, "internal error 'mode_grafiek' in Function RS232_grafiek() unknown", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      }
      

      // NB getWidth() en getHeight() zijn van jPanel1 (centrum panel)
      // NB onderstaande werkt ook, maar minder veilig (ivm het vierkant mooi te sluiten)
      //    Rectangle2D recta = new Rectangle2D.Double(linker_kantlijn_margin, boven_kantlijn_margin, (getWidth() - linker_kantlijn_margin) - rechter_kantlijn_margin, (getHeight() - boven_kantlijn_margin) - onder_kantlijn_margin);
      //    g2.draw(recta);

      //
      // safe method for constructing a square
      //
      g2.setPaint(color_raster);
      g2.setStroke(new BasicStroke(2f));

      Rectangle2D rect = new Rectangle2D.Double();
      rect.setFrameFromDiagonal(links_boven_grafiek, rechts_onder_grafiek);
      g2.draw(rect);

      // NB De hele achtergrond (bepaald door jPanel in design mode is nu floral white (255, 250, 240))
      // NB onderstaande zou in deze jPanel de getekende rechthoek (kleiner dan jPanel) weer zwart maken
      // NB g2.setPaint(Color.BLACK);
      // NB g2.fill(rect);


      //
      // y-as values (eg air pressure values)
      //
      if (main.mode_grafiek.equals(main.MODE_PRESSURE))
      {
         parameter_start_waarde                       = 950; // lowest parameter value in graph
         aantal_parameter_markers_y_as                = 10;  // actually 11 markers (if source included)
         aantal_units_tussen_2_markers                = 10;  // nb units = hPa or degrees or wind speed
      }
      else if (main.mode_grafiek.equals(main.MODE_AIRTEMP) || main.mode_grafiek.equals(main.MODE_AIRTEMP_II))
      {
         parameter_start_waarde                       = -50; // lowest parameter value in graph
         aantal_parameter_markers_y_as                = 10;  // actually 11 markers (if source included)
         aantal_units_tussen_2_markers                = 10;  // nb units = hPa or degrees or wind speed
      }
      else if (main.mode_grafiek.equals(main.MODE_SST))
      {
         parameter_start_waarde                       = -10; // lowest parameter value in graph
         aantal_parameter_markers_y_as                = 10;  // actually 11 markers (if source included)
         aantal_units_tussen_2_markers                = 5;   // nb units = hPa or degrees or wind speed
      }
      else if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))  // valid for true wind speed 10 min average and maximum true wind gust from 10min
      {
         parameter_start_waarde                       = 0;   // lowest parameter value in graph
         aantal_parameter_markers_y_as                = 10;  // actually 11 markers (if source included)
         aantal_units_tussen_2_markers                = 10;  // nb units = hPa or degrees or wind speed
      }
      else if (main.mode_grafiek.equals(main.MODE_WIND_DIR))   
      {
         parameter_start_waarde                       = 0;   // lowest parameter value in graph
         aantal_parameter_markers_y_as                = 36;  // actually 37 markers (if source included)
         aantal_units_tussen_2_markers                = 10;  // nb units = hPa or degrees or wind speed
      }
      
      double y_as_lengte                              = links_onder_grafiek.getY() - links_boven_grafiek.getY();
      double schaling                                 = y_as_lengte / aantal_parameter_markers_y_as;
      // NB dus om bv 990.6 hPa in grafiek te zetten:
      //   links_onder_grafiek.getY() - {(990.6 - pressure_start_waarde) / aantal_hpa_tussen_2_markers * schaling}


      //
      // x-axis values (date-time)
      //
      if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
      {
         aantal_time_markers_x_as                     = 25; // 25 hours, actually 26 markers if source also included
      }
      else if((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
      {
         aantal_time_markers_x_as                     = 29; // 7 days every 6 hours (so 4 per dag), actually  30 markers if source also included
      }


      int aantal_5_minuten_perioden_tussen_2_markers = 0;
      if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
      {
         aantal_5_minuten_perioden_tussen_2_markers = 12;
      }
      else if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
      {
         aantal_5_minuten_perioden_tussen_2_markers = 72;              // 12 * 6 (12 blokjes -van 5 minuten per uur- en dan per 6 uur)
      }

      double x_as_lengte                              = rechts_onder_grafiek.getX() - links_onder_grafiek.getX();
      double schaling_x_as                            = x_as_lengte / aantal_time_markers_x_as;


      //
      // markers and help lines in graph
      //
      g2.setPaint(color_raster);
      g2.setStroke(new BasicStroke(1f));


      //
      // x-axis
      //
      for (int i = 0; i <= aantal_time_markers_x_as; i++)
      {
         double x_pos_marker = links_onder_grafiek.getX() + (i * schaling_x_as);

         // markers (small lines) below at x-as
         //
         g2.setStroke(new BasicStroke(1f));

         Point2D marker_begin_x_as  = new Point2D.Double(x_pos_marker, links_onder_grafiek.getY()+ lengte_x_as_mark);
         Point2D marker_eind_x_as   = new Point2D.Double(x_pos_marker, links_onder_grafiek.getY());

         Line2D line_marker_x_as = new Line2D.Double(marker_begin_x_as, marker_eind_x_as);
         g2.draw(line_marker_x_as);

         // numbers (hour indication) determine for below x-as
         //                          (see page 212, 213 Core Java Volume I)
         Font font_x = new Font("SansSerif", Font.BOLD, 11);
         g2.setFont(font_x);
         String test_aanduiding_x = "12";
         FontRenderContext context_x = g2.getFontRenderContext();
         Rectangle2D bounds_x = font_x.getStringBounds(test_aanduiding_x, context_x);
         double stringWidth_x = bounds_x.getWidth();
         double ascent_x = -bounds_x.getY();                           // ascent = helling/steilte (hoogte characters)

         // NB drawString kan niet met een Point2D argument

         cal_grafiek_datum_tijd = new GregorianCalendar();
         if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
         {
            cal_grafiek_datum_tijd.add(Calendar.HOUR_OF_DAY, -aantal_time_markers_x_as + 1 + i);
         }
         else if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
         {
            cal_grafiek_datum_tijd.add(Calendar.HOUR_OF_DAY, -(aantal_time_markers_x_as * 6) + 6 + (i * 6));
         }

         // hour indications x-axis
         //
         String uur_aanduiding = RS232_view.sdf5.format(cal_grafiek_datum_tijd.getTime());
         g2.drawString(uur_aanduiding, (int)(x_pos_marker - stringWidth_x / 2), (int)(links_onder_grafiek.getY() + lengte_x_as_mark + ascent_x));

         // date indication x-axis
         //
         int int_uur_aanduiding = Integer.parseInt(uur_aanduiding);

         if ( ( (RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY) && (int_uur_aanduiding == 0 || int_uur_aanduiding == 12)) ||
              ( (RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK)) && (int_uur_aanduiding == 0 || int_uur_aanduiding == 1 || int_uur_aanduiding == 2 || int_uur_aanduiding == 3 || int_uur_aanduiding == 4 || int_uur_aanduiding == 5) )
         {
            g2.setPaint(color_date_time);

            Font font_datum = new Font("Serif", Font.BOLD, 14);
            g2.setFont(font_datum);
            String test_aanduiding_datum = "02-12-2016";
            FontRenderContext context_datum = g2.getFontRenderContext();
            Rectangle2D bounds_datum = font_datum.getStringBounds(test_aanduiding_datum, context_datum);
            double ascent_datum = -bounds_datum.getY();                           // ascent = helling/steilte (hoogte characters)

            String dag_van_de_week_aanduiding = RS232_view.sdf7.format(cal_grafiek_datum_tijd.getTime());
            //String datum_aanduiding = RS232_view.sdf6.format(cal_grafiek_datum_tijd.getTime()) + " UTC";
            String datum_aanduiding = RS232_view.sdf6.format(cal_grafiek_datum_tijd.getTime());
            
            if (i != aantal_time_markers_x_as)  // niet bij de allerlaaste x-as marker (komt dan voorbij de rechter y-as)
            {
               // eg Thuesday
               g2.drawString(dag_van_de_week_aanduiding, (int)(x_pos_marker), (int)(links_onder_grafiek.getY() + lengte_x_as_mark + (2 * ascent_datum)));

               // eg 12-05-2015
               g2.drawString(datum_aanduiding, (int)(x_pos_marker), (int)(links_onder_grafiek.getY() + lengte_x_as_mark + (3 * ascent_datum)));
            }

            g2.setPaint(color_raster);                      // reset
            
         } // if (int_uur_aanduiding == 0 || etc.


         // vertical secondary lines
         //
         if (i != 0) // the y-axis (x = 0) itself do not over write
         {
            g2.setStroke(new BasicStroke(1f));

            //g2.setPaint(Color.LIGHT_GRAY);
            Point2D verticaal_hulp_lijn_begin  = new Point2D.Double(x_pos_marker, links_onder_grafiek.getY());
            Point2D verticaal_hulp_lijn_eind   = new Point2D.Double(x_pos_marker, links_boven_grafiek.getY());

            Line2D hulp_lijn = new Line2D.Double(verticaal_hulp_lijn_begin, verticaal_hulp_lijn_eind);
            g2.draw(hulp_lijn);
         } // if (i != 0)

      } // for (int i = 0; i <= aantal_time_markers_x_as; i++)

      

      //
      // y-axis
      //
      Font font_y = new Font("SansSerif", Font.BOLD, 11);
      g2.setFont(font_y);
      
      String test_aanduiding_y = "";
      if (main.mode_grafiek.equals(main.MODE_PRESSURE))
      {
         test_aanduiding_y = "1000 hPa";
      }
      else if ( (main.mode_grafiek.equals(main.MODE_AIRTEMP)) || (main.mode_grafiek.equals(main.MODE_SST)) || (main.mode_grafiek.equals(main.MODE_AIRTEMP_II)) )
      {
         test_aanduiding_y = "-10 °C";
      }
      else if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))
      {
         test_aanduiding_y = "100 kts";
      }
      else if (main.mode_grafiek.equals(main.MODE_WIND_DIR))
      {
         test_aanduiding_y = "120 °";
      }
      
      FontRenderContext context_y = g2.getFontRenderContext();
      Rectangle2D bounds_y = font_y.getStringBounds(test_aanduiding_y, context_y);
      double stringWidth_y = bounds_y.getWidth();
      double ascent_y = -bounds_y.getY();

      for (int i = 0; i <= aantal_parameter_markers_y_as; i++)
      {
         double y_pos_marker = links_onder_grafiek.getY() - (i * (schaling));

         // markers at left x-axis
         //
         g2.setStroke(new BasicStroke(1f));

         Point2D marker_begin_linker_y_as  = new Point2D.Double(links_onder_grafiek.getX() - lengte_y_as_mark, y_pos_marker);
         Point2D marker_eind_linker_y_as   = new Point2D.Double(links_onder_grafiek.getX(), y_pos_marker);

         Line2D line_marker_linker_y_as = new Line2D.Double(marker_begin_linker_y_as, marker_eind_linker_y_as);
         g2.draw(line_marker_linker_y_as);


         // NB drawString doesn't work with a Point2D argument
         int int_aanduiding = parameter_start_waarde + i * aantal_units_tussen_2_markers;
         
         String aanduiding = "";
         if (main.mode_grafiek.equals(main.MODE_PRESSURE))
         {
            aanduiding = Integer.toString(int_aanduiding) + " hPa";
         }
         else if ((main.mode_grafiek.equals(main.MODE_AIRTEMP)) || (main.mode_grafiek.equals(main.MODE_SST)) || (main.mode_grafiek.equals(main.MODE_AIRTEMP_II)))
         {
            aanduiding = Integer.toString(int_aanduiding) + " °C";   
         }
         else if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))
         {
            if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)
            {
               aanduiding = Integer.toString(int_aanduiding) + " kts";
            }
            else
            {
              aanduiding = Integer.toString(int_aanduiding) + " m/s"; 
            }
         }
         else if (main.mode_grafiek.equals(main.MODE_WIND_DIR))
         {
            aanduiding = Integer.toString(int_aanduiding) + " °";                 // e.g. 13 -> 130°
         }
         
         // indications at left y-axis
         //
         g2.drawString(aanduiding, (int)(marker_begin_linker_y_as.getX() - stringWidth_y - afstand_aanduiding_naar_marker), (int)(y_pos_marker + ascent_y / 2));


         // markers at right y-axis
         //
         g2.setStroke(new BasicStroke(1f));

         Point2D marker_begin_rechter_y_as  = new Point2D.Double(rechts_onder_grafiek.getX(), y_pos_marker);
         Point2D marker_eind_rechter_y_as   = new Point2D.Double(rechts_onder_grafiek.getX() + lengte_y_as_mark, y_pos_marker);

         Line2D line_marker_rechter_y_as = new Line2D.Double(marker_begin_rechter_y_as, marker_eind_rechter_y_as);
         g2.draw(line_marker_rechter_y_as);

         // indications at right y-as
         //
         g2.drawString(aanduiding, (int)(marker_eind_rechter_y_as.getX() + afstand_aanduiding_naar_marker), (int)(y_pos_marker + /*(stringHeight - leading) / 2*/ ascent_y / 2));


         // horizontal main lines
         //
         if (i != 0) // the x-axis (y = 0) itself do not over write
         {
            g2.setStroke(new BasicStroke(2f));

            Point2D horizontaal_hulp_lijn_begin  = new Point2D.Double(links_onder_grafiek.getX(), y_pos_marker);
            Point2D horizontaal_hulp_lijn_eind   = new Point2D.Double(rechts_onder_grafiek.getX(), y_pos_marker);

            Line2D hulp_lijn = new Line2D.Double(horizontaal_hulp_lijn_begin, horizontaal_hulp_lijn_eind);
            g2.draw(hulp_lijn);
         } // if (i != 0)

			
         // horizontal secondary lines
         //
         g2.setStroke(new BasicStroke(1f));

         if (i != aantal_parameter_markers_y_as)
         {
            // horizontal sec. lines (eg 10 secondary help lines between 2 lines)
            if (main.mode_grafiek.equals(main.MODE_WIND_DIR))
            {
               aantal_secondary_lines = 0;              // otherwise to crowdy
            }
            else
            {
               aantal_secondary_lines = aantal_units_tussen_2_markers;
            }
            
            //for (int j = 0; j < aantal_units_tussen_2_markers; j++)  
            for (int j = 0; j < aantal_secondary_lines; j++)    
            {
               double y_pos_sub_marker = (links_onder_grafiek.getY() - (i * schaling)) - (j * (schaling / aantal_units_tussen_2_markers));
               Point2D horizontaal_hulp_lijn_begin  = new Point2D.Double(links_onder_grafiek.getX(), y_pos_sub_marker);
               Point2D horizontaal_hulp_lijn_eind   = new Point2D.Double(rechts_onder_grafiek.getX(), y_pos_sub_marker);

               Line2D hulp_lijn = new Line2D.Double(horizontaal_hulp_lijn_begin, horizontaal_hulp_lijn_eind);
               g2.draw(hulp_lijn);

            } // for (int j = 0; j < aantal_units_tussen_2_markers; j++)
         } // if (i != aantal_parameter_markers_y_as)

      } // for (int i = 0; i <= aantal_markers_y_as; i++)
      
      
      // meta text left above graph
      //
      String string_aanduiding = "";
      
      if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2 || main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)   // PTB220 or PTB330 or MintakaDuo or Mintaka Star (USB) Mintaka Star WiFi 
      {
         if (main.mode_grafiek.equals(main.MODE_PRESSURE))
         {
            string_aanduiding = "All values at barometer height and no ic applied";
         }
         // NB color is the color of the raster 
         //Font font_sensor_level_text = new Font("Serif", Font.BOLD, 14);
         //g2.setFont(font_sensor_level_text);
                  
         //String string_aanduiding = "All values at barometer height";
         //FontRenderContext context_sensor_level_font = g2.getFontRenderContext();
         //Rectangle2D bounds_string_aanduiding = font_sensor_level_text.getStringBounds(string_aanduiding, context_sensor_level_font);
         //double ascent_sensor_level_text = -bounds_string_aanduiding.getY();     // hoogte char's                      // ascent = helling/steilte (hoogte characters)
         //g2.drawString(string_aanduiding, (int)(links_boven_grafiek.getX()), (int)(2 * ascent_sensor_level_text));
      } 
      else if (main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8)      // StarX USB or StarX LAN connected 
      {
         if (main.mode_grafiek.equals(main.MODE_PRESSURE))      
         {
            //string_aanduiding = "All air pressure values at Mean Sea Level";          // till 22-06-2018
            string_aanduiding = "All values at barometer height and no ic applied";     // from 22-06-2018
         }  
         else if (main.mode_grafiek.equals(main.MODE_AIRTEMP))   
         {
            string_aanduiding = "All air temperature values at sensor height";
         }  
      }
      else if (main.RS232_connection_mode == 3 || main.RS232_connection_mode == 9 || main.RS232_connection_mode == 10) // AWS connected
      {
         if (main.mode_grafiek.equals(main.MODE_PRESSURE))      
         {
            string_aanduiding = "All air pressure values at Mean Sea Level";
         }   
         else if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))   
         {
            string_aanduiding = "All true wind speed values at sensor height";
         }  
         else if (main.mode_grafiek.equals(main.MODE_WIND_DIR))   
         {
            string_aanduiding = "All true wind direction values at sensor height";
         } 
         else if (main.mode_grafiek.equals(main.MODE_AIRTEMP))   
         {
            string_aanduiding = "All air temperature values at sensor height";
         }  
         else if (main.mode_grafiek.equals(main.MODE_SST))   
         {
            string_aanduiding = "All sea surface temperature values at sensor depth";
         }  
      } // else if (main.RS232_connection_mode == 3 || main.RS232_connection_mode == 9 || main.RS232_connection_mode == 10)
      
      
      if (main.RS232_connection_mode_II == 1)
      {
         if (main.mode_grafiek.equals(main.MODE_AIRTEMP_II))   
         {
            string_aanduiding = "All air temperature values at sensor height";
         }     
      }
      
      
      if (string_aanduiding.equals("") == false)
      {
         // NB color is the color of the raster 
         Font font_sensor_level_text = new Font("Serif", Font.BOLD, 14);
         g2.setFont(font_sensor_level_text);
                  
         FontRenderContext context_sensor_level_font = g2.getFontRenderContext();
         Rectangle2D bounds_string_aanduiding = font_sensor_level_text.getStringBounds(string_aanduiding, context_sensor_level_font);
         double ascent_sensor_level_text = -bounds_string_aanduiding.getY();     // hoogte char's                      // ascent = helling/steilte (hoogte characters)
         g2.drawString(string_aanduiding, (int)(links_boven_grafiek.getX()), (int)(2 * ascent_sensor_level_text));
      }   
      
      

      /////////////////////////// START INPUT DATA /////////////////////////////

      g2.setStroke(new BasicStroke(2.5f));
      g2.setPaint(color_pen);

      double x_waarde = 0;
      double y_waarde = 0;
      
      // initialisation
      for (int k = 0; k <  RS232_view.AANTAL_PLOT_POINTS; k++)
      {
         // 2nd meteo instrument
         if (main.mode_grafiek.equals(main.MODE_AIRTEMP_II)) 
         {
            hulp_sensor_waarde_array[k] =  RS232_view.sensor_waarde_array_II[k];
            hulp_datum_tijd_array[k]    =  RS232_view.datum_tijd_array_II[k];
         }
         else // 1st meteo instrument
         {
            hulp_sensor_waarde_array[k] =  RS232_view.sensor_waarde_array[k];
            hulp_datum_tijd_array[k]    =  RS232_view.datum_tijd_array[k];
         }
      } // for (int k = 0; k <  RS232_view.AANTAL_PLOT_POINTS; k++)

      // initialisation
      for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
      {
         RS232_view.points[i] = new Point2D.Double(0, 0);
      }

      for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
      {
         //System.out.println("--- " + "sensor_waarde_array[" + i + "] = " + sensor_waarde_array[i]);

         if (!hulp_sensor_waarde_array[i].equals(""))
         {
            try
            {
               double sensor_waarde = Double.MAX_VALUE;
               if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))
               {
                  // m/s -> knots
                  //sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim()) * main.M_S_KNOT_CONVERSION;
                 
                  // NB 4 options:
                  // 1) measured/observed in m/s   -> dashboard and graph in knots
                  // 2) measured/observed in m/s   -> dashboard and graph in m/s
                  // 3) measured/observed in knots -> dashboard and graph in knots
                  // 4) measured/observed in knots -> dashboard and graph in m/s
                     
                  if (main.wind_units.indexOf(main.M_S) != -1)                   // wind_units 'as measured' set to m/s by observer in Maintenance -> Station data
                  {
                     if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // measured in m/s and dashboard in knots
                     {
                        sensor_waarde = Double.parseDouble(hulp_sensor_waarde_array[i].trim()) * main.M_S_KNOT_CONVERSION;
                     }
                     else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // measured in m/s and dashboard in m/s
                     {
                        sensor_waarde = Double.parseDouble(hulp_sensor_waarde_array[i].trim()); 
                     }
                  }
                  else if (main.wind_units.indexOf(main.KNOTS) != -1)            // wind_units 'as measured' set to knots by observer in Maintenance -> Station data
                  {
                     if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // measured in knots and dashboard in knots
                     {
                        sensor_waarde = Double.parseDouble(hulp_sensor_waarde_array[i].trim()); 
                     }
                     else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // measured in knots and dashboard in m/s
                     {
                        sensor_waarde = Double.parseDouble(hulp_sensor_waarde_array[i].trim()) * main.KNOT_M_S_CONVERSION; 
                     }   
                  }          
               } // if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))
               else
               {
                  sensor_waarde = Double.parseDouble(hulp_sensor_waarde_array[i].trim());
               }
               
               if (main.mode_grafiek.equals(main.MODE_WIND_DIR))
               {
                  // NB North is 0 degr. from the sensors, this must be plotted as 360 degr for consistency with the wind observations
                  if (sensor_waarde < 0.5)
                  {
                     sensor_waarde = 360;
                  }
               } // if (main.mode_grafiek.equals(main.MODE_WIND_DIR))
               
               if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
               {
                  x_waarde = links_onder_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
               }
               else if((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
               {
                  x_waarde = links_onder_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
               }

               y_waarde = links_onder_grafiek.getY() - ((sensor_waarde - parameter_start_waarde) / (double)aantal_units_tussen_2_markers * schaling);
               RS232_view.points[i] = new Point2D.Double(x_waarde, y_waarde);
            }
            catch (NumberFormatException e) 
            { 
               //System.out.println("--- " + "Function paintComponent() " + e);
            }
         } // if (!RS232_view.sensor_waarde_array[i].equals(""))
      } // for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)


      GeneralPath path = new GeneralPath();
      for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
      {
         if (RS232_view.points[i].getX() != 0)
         {
            path.moveTo((float)RS232_view.points[i].getX(), (float)RS232_view.points[i].getY());
            if (i + 1 < RS232_view.AANTAL_PLOT_POINTS)
            {
               if (RS232_view.points[i + 1].getX() != 0)
               {
                  path.lineTo((float)RS232_view.points[i + 1].getX(), (float)RS232_view.points[i + 1].getY());  // Resets the path to empty. The append position is set back to the beginning of the path and all coordinates and point types are forgotten
               }
               else
               {
                  g2.draw(path);
                  path.reset();
               }
            } // if (i + 1 < RS232_view.AANTAL_PLOT_POINTS)
            else
            {
               g2.draw(path);
            } // else
         } // if (points[i].getX() != 0)
      } // for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)

      
      // write as a string the last parameter value (update every 5 minutes); top right corner above graph
      //
      for (int i = RS232_view.AANTAL_PLOT_POINTS -1; i >= 0; i--)   
      {
         if (!hulp_sensor_waarde_array[i].equals(""))
         {
            try        
            {
               double digitale_sensor_waarde = Double.parseDouble(hulp_sensor_waarde_array[i].trim());
               
               if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))
               {
                  // m/s -> knots
                  //digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10 * main.M_S_KNOT_CONVERSION) / 10.0d; 
                 
                  if (main.wind_units.indexOf(main.M_S) != -1)                   // wind_units 'as measured' set to m/s by observer in Maintenance -> Station data
                  {
                     if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // measured in m/s and dashboard in knots
                     {
                        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10 * main.M_S_KNOT_CONVERSION) / 10.0d;
                     }
                     else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // measured in m/s and dashboard in m/s
                     {
                        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d; 
                     }
                  } 
                  else if (main.wind_units.indexOf(main.KNOTS) != -1)            // wind_units 'as measured' set to knots by observer in Maintenance -> Station data
                  {
                     if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // measured in knots and dashboard in knots
                     {
                        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;
                     }
                     else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // measured in knots and dashboard in m/s
                     {
                        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10 * main.KNOT_M_S_CONVERSION) / 10.0d;
                     }   
                  }        
                    
               } //  if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))
               else
               {
                  digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;  // eg 998.19 -> 998.2
               }
               
               if ((digitale_sensor_waarde > -50) && (digitale_sensor_waarde < 1100))   // max/min of max limits of the parameters pressure, air temp, sst and wind speed
               {
                  String jaar   = hulp_datum_tijd_array[i].substring(0, 4);
                  String maand  = hulp_datum_tijd_array[i].substring(4, 4 + 2);
                  String dag    = hulp_datum_tijd_array[i].substring(6, 6 + 2);
                  String uur    = hulp_datum_tijd_array[i].substring(8, 8 + 2);
                  String minuut = hulp_datum_tijd_array[i].substring(10, 10 + 2);

                  String datum_tijd_parameter_string = "";
						String test_aanduiding_datum_tijd_parameter = "";
                  if (main.mode_grafiek.equals(main.MODE_PRESSURE))
                  {
                     datum_tijd_parameter_string = dag + "-" + maand + "-" + jaar + " " + uur + ":" + minuut + " UTC  " + digitale_sensor_waarde + " hPa";
							test_aanduiding_datum_tijd_parameter = "17-04-2016 09:35 UTC 1020.7 hPa";
                  }
                  else if ((main.mode_grafiek.equals(main.MODE_AIRTEMP)) || (main.mode_grafiek.equals(main.MODE_SST)) || (main.mode_grafiek.equals(main.MODE_AIRTEMP_II)))
                  {
                     datum_tijd_parameter_string = dag + "-" + maand + "-" + jaar + " " + uur + ":" + minuut + " UTC  " + digitale_sensor_waarde + " °C";
							test_aanduiding_datum_tijd_parameter = "17-04-2016 09:35 UTC 14.8 °C";
                  }
                  else if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))
                  {
                     if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)
                     {
                        datum_tijd_parameter_string = dag + "-" + maand + "-" + jaar + " " + uur + ":" + minuut + " UTC  " + digitale_sensor_waarde + " kts";
                     }
                     else
                     {
                        datum_tijd_parameter_string = dag + "-" + maand + "-" + jaar + " " + uur + ":" + minuut + " UTC  " + digitale_sensor_waarde + " m/s";
                     }
                     
                     test_aanduiding_datum_tijd_parameter = "17-04-2016 09:35 UTC 23 kts";							
                  }
                  else if (main.mode_grafiek.equals(main.MODE_WIND_DIR))
                  {
                     datum_tijd_parameter_string = dag + "-" + maand + "-" + jaar + " " + uur + ":" + minuut + " UTC  " + digitale_sensor_waarde + " °";
							test_aanduiding_datum_tijd_parameter = "17-04-2016 09:35 UTC 120 °";
                  }
                  
                  Font font_datum_tijd_parameter = new Font("Serif", Font.BOLD, 14);
                  g2.setFont(font_datum_tijd_parameter);
                  //String test_aanduiding_datum_tijd_parameter = "17-04-2016 09:35 UTC 1020.7 hPa";
                  FontRenderContext context_datum_tijd_parameter = g2.getFontRenderContext();
                  Rectangle2D bounds_datum_tijd_pressure = font_datum_tijd_parameter.getStringBounds(test_aanduiding_datum_tijd_parameter, context_datum_tijd_parameter);
                  //double stringWidth_datum = bounds_datum.getWidth();
                  double ascent_datum_tijd_pressure = -bounds_datum_tijd_pressure.getY();     // hoogte char's                      // ascent = helling/steilte (hoogte characters)
                  double length_datum_tijd_pressure = bounds_datum_tijd_pressure.getWidth();     // lengte string
	
                  g2.drawString(datum_tijd_parameter_string, (int)(rechts_boven_grafiek.getX() - length_datum_tijd_pressure), (int)(2 * ascent_datum_tijd_pressure));
               }
            } // try
            catch (NumberFormatException e) { }        

            break;
                    
         } // if (!RS232_view.sensor_waarde_array[i].equals(""))
      } // for (int i = RS232_view.AANTAL_PLOT_POINTS -1; i >= 0; i--)


      //
      // in wind speed mode draw a second line (wind gust)
      // 
      if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))
      {
         // NB (see: http://docstore.mik.ua/orelly/java-ent/jfc/ch04_05.htm)
         // 
         // public BasicStroke(float width, int cap, int join, float miterlimit, float[] dash, float dash_phase)
         //
         // The dash pattern of a line is actually controlled by two attributes: the dash array and the dash phase. 
         // The dash array is a float[] that specifies the number of units to be drawn followed by the number of units 
         // to be skipped. For example, to draw a dashed line in which both the dashes and spaces are 25 units long, 
         // you use an array like:
         //
         //    new float[] { 25.0f, 25.0f }
         //
         // To draw a dot-dash pattern consisting of 21 on, 9 off, 3 on, and 9 off, you use this array:
         //
         //    new float[] { 21.0f, 9.0f, 3.0f, 9.0f }
         //
         //
         // If, for some reason, you want to draw a dashed line but do not want your line to begin at the 
         // beginning of the dash pattern, you can specify the dash-phase attribute. The value of this 
         // attribute specifies how far into the dash pattern the line should begin. Note, however, 
         // that this value is not an integer index into the dash pattern array. Instead, it is a floating-point 
         // value that specifies a linear distance.          
         
         
         //g2.setStroke(new BasicStroke(2.5f));
         //float[] dash = {2.0f, 0.0f, 2.0f};
         //float dash[] = {5.0f, 5.0f, 10.0f, 7.0f};
         //float[] dash = {2.0f, 2.0f, 2.0f};
         
         //BasicStroke bs_dashed = new BasicStroke(1.25f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, dash, 2.0f);
       
         //BasicStroke bs_dashed = new BasicStroke(1.25f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[]{100f,200f}, 2.0f);
         //g2.setStroke(bs_dashed);
         
         g2.setStroke(new BasicStroke(1.25f));
         g2.setPaint(color_pen);

         double x_waarde_2 = 0;
         double y_waarde_2 = 0;

         // initialisation
         for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
         {
            RS232_view.points[i] = new Point2D.Double(0, 0);
         }

         for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
         {
            //System.out.println("--- " + "gepasseerd");

            if (!RS232_view.sensor_waarde_array_2[i].equals(""))
            {
               //System.out.println("--- " + "gepasseerd2 " + RS232_view.sensor_waarde_array_2[i]);
               
               try
               {
                  // m/s -> knots
                  //double sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_2[i].trim()) * main.M_S_KNOT_CONVERSION;
                  
                  double sensor_waarde = Double.MAX_VALUE;
                  // NB 4 options:
                  // 1) measured/observed in m/s   -> dashboard and graph in knots
                  // 2) measured/observed in m/s   -> dashboard and graph in m/s
                  // 3) measured/observed in knots -> dashboard and graph in knots
                  // 4) measured/observed in knots -> dashboard and graph in m/s
                     
                  if (main.wind_units.indexOf(main.M_S) != -1)                   // wind_units 'as measured' set to m/s by observer in Maintenance -> Station data
                  {
                     if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // measured in m/s and dashboard in knots
                     {
                        sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_2[i].trim()) * main.M_S_KNOT_CONVERSION;
                     }
                     else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // measured in m/s and dashboard in m/s
                     {
                        sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_2[i].trim()); 
                     }
                  }
                  else if (main.wind_units.indexOf(main.KNOTS) != -1)            // wind_units 'as measured' set to knots by observer in Maintenance -> Station data
                  {
                     if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // measured in knots and dashboard in knots
                     {
                        sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_2[i].trim()); 
                     }
                     else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // measured in knots and dashboard in m/s
                     {
                        sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array_2[i].trim()) * main.KNOT_M_S_CONVERSION; 
                     }   
                  }      
                  
               
                  if ((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_DAY))
                  {
                     x_waarde_2 = links_onder_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
                  }
                  else if((RS232_view.mode_tijd_periode).equals(RS232_view.MODE_WEEK))
                  {
                     x_waarde_2 = links_onder_grafiek.getX() + (i / (double)aantal_5_minuten_perioden_tussen_2_markers * schaling_x_as);
                  }

                  y_waarde_2 = links_onder_grafiek.getY() - ((sensor_waarde - parameter_start_waarde) / (double)aantal_units_tussen_2_markers * schaling);
                  RS232_view.points[i] = new Point2D.Double(x_waarde_2, y_waarde_2);
               }
               catch (NumberFormatException e) { }
            }
         } // for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)


         GeneralPath path_2 = new GeneralPath();
         for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
         {
            if (RS232_view.points[i].getX() != 0)
            {
               path_2.moveTo((float)RS232_view.points[i].getX(), (float)RS232_view.points[i].getY());
               if (i + 1 < RS232_view.AANTAL_PLOT_POINTS)
               {
                  if (RS232_view.points[i + 1].getX() != 0)
                  {
                     path_2.lineTo((float)RS232_view.points[i + 1].getX(), (float)RS232_view.points[i + 1].getY());  // Resets the path to empty. The append position is set back to the beginning of the path and all coordinates and point types are forgotten
                  }
                  else
                  {
                     g2.draw(path_2);
                     path_2.reset();
                  }
               } // if (i + 1 < RS232_view.AANTAL_PLOT_POINTS)
               else
               {
                  g2.draw(path_2);
               } // else
            } // if (points[i].getX() != 0)
         } // for (int i = 0; i < RS232_view.AANTAL_PLOT_POINTS; i++)
      } // if (main.mode_grafiek.equals(main.MODE_WIND_SPEED))
      
      /////////////////////// END INPUT DATA ///////////////////////
   } // else (1 graph view)
}


   public String[] hulp_sensor_waarde_array                = new String[RS232_view.AANTAL_PLOT_POINTS];  
   public String[] hulp_datum_tijd_array                   = new String[RS232_view.AANTAL_PLOT_POINTS];

   //private final double M_S_KNOT_CONVERSION              = 1.94384449;
   //private final double HOOGTE_CORRECTIE = 0.0;
   private GregorianCalendar cal_grafiek_datum_tijd;   
   
   private int parameter_start_waarde;                   // lowest parameter value in graph
   private int aantal_parameter_markers_y_as;            // actually 11 markers (if source is counted also)
   private int aantal_units_tussen_2_markers;            // NB units = hPa or degrees or wind speed
 
    
}
