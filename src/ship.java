/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbowin;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author marti
 */
public class ship 
{
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public ship()
   {
      color_white                        = Color.WHITE;
      color_black                        = Color.BLACK;
      color_life_boat                    = new Color(255, 69, 0);                                // orange-red
      color_deck_passenger_ship          = new Color(222,184, 135);   
      color_acc_passenger_ship           = new Color(255, 218, 185);
      color_deck_general_cargo_ship      = new Color(210, 105, 30);
      color_deck_research_vessel         = new Color(36, 128, 55);    
      color_deck1_ferry                  = new Color(70, 130, 180);                              // main deck        // steel blue
      color_deck2_ferry                  = new Color(123, 163, 201);                             // bridge deck      // between steel blue and light steel blue
      color_deck3_ferry                  = new Color(176, 196, 222);                             // upper/top deck   // light steel blue
      color_funnel_ferry                 = new Color(173, 216, 220);//new Color(70, 130, 180);
      color_deck_ro_ro                   = Color.DARK_GRAY;
      color_lanes_ro_ro                  = new Color(255, 255, 0, 128);                          // yellow, semi transparent
      color_crane_research_vessel        = new Color(204, 69, 50, 255);                          // red-brown
      color_hoist_research_vessel        = new Color(0, 102, 204, 255);                          // blue
      color_cradle_research_vessel       = new Color(255, 165, 0, 255);                          // orange
      color_night_crane_research_vessel  = new Color(204, 69, 50, 64);                           // red-brown, semi transparent
      color_night_hoist_research_vessel  = new Color(0, 102, 204, 64);                           // blue, semi transparent
      color_night_cradle_research_vessel = new Color(255, 165, 0, 64);                           // orange, semi transparent
      color_hatches_general_cargo_ship   = Color.LIGHT_GRAY;
      color_pool_1                       = new Color(100, 149, 237);                             // starboard and aft swimming pool edge; color between inner swimming pool and outer pool; for depth effect
      color_pool_2                       = new Color(0, 0, 205);                                 // inner swimming pool (pool bottom)
      color_pool_3                       = new Color(0, 255, 255, 100);                          // outer swimming pool (covering pool bottom and edges (semi transparent)
      color_funnel_passenger_ship        = Color.LIGHT_GRAY;
      color_bridge_container_ship        = new Color(245,255,250); 
      color_bridge_oil_tanker            = Color.GRAY;  
      color_bridge_research_vessel       = new Color(60, 159, 113); 
      color_bridge_ro_ro_ship            = new Color(60, 159, 113);
      color_bulk_carrier                 = new Color(204, 69, 50, 255);                          // red-brown
      color_lng_tanks                    = Color.RED;
      color_deck_lng_tanker              = Color.LIGHT_GRAY; 
      color_deck_oil_tanker              = new Color(0, 100, 70);                                // green-blue
      color_pipes_oil_tanker             = Color.LIGHT_GRAY;  
      color_deck                         = new Color(176,196,222, 255);                          // light steel blue (NB alpha 255 = 100% opaque)
      color_medium_gray                  = new Color(128, 128, 128);
      int alpha_container                = 128;                                                  // semi transparent
      color_container_1                  = new Color(255, 51, 51, alpha_container);              // red      
      color_container_2                  = new Color(178, 34, 34, alpha_container);              // firebrick red
      color_container_3                  = new Color(240,128,128, alpha_container);              // light coral red
      color_container_4                  = new Color(70,130,180, alpha_container);               // steel blue
      color_container_5                  = new Color(30,144,255, alpha_container);               // dodger blue
      color_container_6                  = new Color(65,105,225, alpha_container);               // royal blue
      color_container_7                  = new Color(255,245,238, alpha_container);              // sea shell white
      color_container_8                  = new Color(245,255,250, alpha_container);              // mint crean white
      color_container_9                  = new Color(244,164,96, alpha_container);               // sandy brown
      color_container_10                 = new Color(255,127,80, alpha_container);               // coral red
      
   } 
 
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void draw_general_cargo_ship(Graphics2D g2d, double wind_rose_diameter, boolean night_mode) 
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
      //
      
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
      float[] dash = {2f, 0f, 2f};
      g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));
      g2d.draw(new Line2D.Double(0, wind_rose_diameter / 2 + ship_breadth, 0, -wind_rose_diameter / 2 - ship_breadth));

      //////////// bow ////////
      //
      x = 0;                                                                // control point quadTo; relative to origin (centre of wind rose)
      y = -(wind_rose_diameter / 2.0);                                      // control point quadTo; relative to origin (centre of wind rose)
      double bow_height_virtual = ship_breadth * 2;                         // NB factor 2.0 is indication of the bow curve; quadTo; quadTo; vertical distance from control point to start end end point       
      y = -(wind_rose_diameter / 2.0) + (bow_height_virtual * 0.1);         // NB factor 0.1 depends on factor used in bow_height_virtual
      
      ///////////// main ship section (rectangle shape) ///////////
      //
      x1 = -ship_breadth / 2.0; //x;
      y1 = y + bow_height_virtual;
      x2 = ship_breadth / 2.0;
      y2 = y1;
      double ship_length_main = Math.abs(y1) * 2.3;                       // so the length of the main mid section; NB factor 2.3 depends on the factor used at bow_height_virtual and y

      ///////////// aft ship (trapezium shape) ///////////
      //
      x3 = x1;
      x4 = x3 + ship_breadth;
      x5 = x4 - (ship_breadth / 7);
      x6 = x3 + (ship_breadth / 7);
      y3 = y1 + ship_length_main;
      y4 = y3;
      y5 = y3 + (ship_breadth * 0.5);
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
      
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {   
         g2d.setPaint(color_deck_general_cargo_ship);
      }
      g2d.fill(ship);

      
      /////////////// ship bridge /////////
      //
      double bridge_height = 7;                     
      //double acc_height = 20;                        
      double acc_height = (y6 - y3) / 2;                        
      double bridge_indention = ship_breadth / 5;
      double dist_origin_bridge = y3 - bridge_height;
      draw_ship_bridge(g2d, ship_breadth, bridge_height, acc_height, dist_origin_bridge, bridge_indention, night_mode);

      
      ////////////// hatches ////////////
      //
      double hatch_breath = ship_breadth * 0.8;
      double hatch_length = hatch_breath / 2;
      double hatch_intermediate = 2;   // distances between 2 hatches
      double y_hatch = y1;
      double x_hatch = -hatch_breath / 2;

      for (int i = 0; i < 50; i++) // max 50 hatches
      {
         if (y_hatch + (hatch_length + hatch_intermediate / 2) < (ship_length_main / 2 - bridge_height)) 
         {
            if (night_mode)
            {
               g2d.setPaint(color_medium_gray);
            }
            else
            {
               g2d.setPaint(color_hatches_general_cargo_ship);
            }
            
            g2d.fill(new Rectangle2D.Double(x_hatch, y_hatch, hatch_breath, hatch_length));
            
            // hatch sections
            g2d.setColor(Color.DARK_GRAY);
            double y_sub_hatch = y_hatch;
            for (int k = 0; k < 100; k++)
            {
               if (y_sub_hatch < (y_hatch + hatch_length))
               {
                  g2d.draw(new Line2D.Double(x_hatch, y_sub_hatch, (x_hatch + hatch_breath), y_sub_hatch));
                  
                  y_sub_hatch = y_sub_hatch + 10;
               }
               else
               {
                  break;
               }
            } //  for (int k = 0; k < 100; k++)  
            
            y_hatch += hatch_length + hatch_intermediate;
         } 
         else 
         {
            break;
         }

      } // for (int i = 0; i < 50; i++)      
      
      
      ////////////// crane pillars ////////////
      //
      //                          ___
      //                         /   \
      //       (x1_arm,y1_arm)  |     | (x2_arm,y2_arm)     <-- pillar crane
      //                        |\___/|
      //                        |     |
      //                        |     |
      //                        |     |
      //                        |     |                     <-- arm crane
      //                        |     |
      //                        |     |
      //                        |     |
      //                         -----
      //        (x3_arm,y3_arm)          (x4_arm,y4_arm)
      //
      //
      
      double dist_between_cranes = ship_length_main / 3;
      double width_crane_pillar = ship_breadth * 0.2;
      double height_crane_pillar = width_crane_pillar;
      double x_pillar_crane = x2 - width_crane_pillar;//0;
      double y_pillar_crane = 0;
      
      double x1_arm = x_pillar_crane;
      double x2_arm = x_pillar_crane + width_crane_pillar;
      double arm_slope = 4;
      double x3_arm = x1_arm + arm_slope;
      double x4_arm = x2_arm - arm_slope;
      
      // if the (ship) figure is very small
      if (x4_arm < x3_arm)
      {
         x3_arm = x1_arm;
         x4_arm = x2_arm;
      }
      
      double y1_first_arm = 0;                 // counting from the bow
      double y1_second_arm = 0;                // counting from the bow
      double y1_third_arm = 0;                 // counting from the bow
      
      for (int p = 0; p < 3; p++)
      {
         y_pillar_crane = y2 + (p * dist_between_cranes);
         
         Shape shape_crane_pillar = new Ellipse2D.Double(x_pillar_crane, y_pillar_crane, width_crane_pillar, height_crane_pillar);

         if (night_mode)
         {
            g2d.setPaint(Color.LIGHT_GRAY);
         }
         else
         {
            g2d.setPaint(color_white);
         }
         g2d.fill(shape_crane_pillar);
      
         g2d.setColor(Color.LIGHT_GRAY);
         g2d.setStroke(new BasicStroke(1.0f));
         g2d.draw(shape_crane_pillar);
         
         // collect data for the arms of the cranes
         if (p == 0)
         {
            y1_first_arm = y_pillar_crane + (height_crane_pillar / 2);
         }
         else if (p == 1)
         {
            y1_second_arm = y_pillar_crane + (height_crane_pillar / 2);
         }
         else if (p == 2)
         {
            y1_third_arm = y_pillar_crane + (height_crane_pillar / 2);
         }
      } // for (int p = 0; p < 3; p++)
      
      
      ///////////////// crane arms /////////////
      //
      g2d.setStroke(new BasicStroke(3.0f)); 
      if (night_mode)
      {
         g2d.setColor(Color.LIGHT_GRAY);
      }
      else
      {
         g2d.setColor(color_white);
      }
      
      double y3_first_arm = y1_second_arm - width_crane_pillar;   // NB y1_first_arm to y3_first_arm - 4 = arm length
      double y3_second_arm = y1_third_arm - width_crane_pillar;   // NB y1_second_arm to y3_second_arm - 4 = arm length
      double y3_third_arm = y4 - width_crane_pillar;              // NB y1_third_arm to y4 (start bridge) - 4 = arm length            
      
      for (int p = 0; p < 3; p++)
      {   
         double y1_arm = 0;
         double y2_arm = 0;
         double y3_arm = 0;
         double y4_arm = 0;
         
         if (p == 0)
         {
            y1_arm = y1_first_arm;
            y2_arm = y1_arm;
            y3_arm = y3_first_arm;
            y4_arm = y3_arm;
         }
         else if (p == 1)
         {
            y1_arm = y1_second_arm;
            y2_arm = y1_arm;
            y3_arm = y3_second_arm;
            y4_arm = y3_arm;
         }
         else if (p == 2)
         {
            y1_arm = y1_third_arm;
            y2_arm = y1_arm;
            y3_arm = y3_third_arm;
            y4_arm = y3_arm;
         }
         
         double xPoints[] = {x1_arm, x3_arm, x4_arm, x2_arm};
         double yPoints[] = {y1_arm, y3_arm, y4_arm, y2_arm};         // -values for y goes/points to the bow

         GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
         polygon.moveTo(xPoints[0], yPoints[0]);
         for (int index = 1; index < xPoints.length; index++) 
         {
            polygon.lineTo(xPoints[index], yPoints[index]);
         }
         polygon.closePath();
        
         g2d.draw(polygon);  
         
         // crane arm reinforcement part
         double x1_sub = x1_arm + (arm_slope / 2);
         double x2_sub = x2_arm - (arm_slope / 2);
         double y1_sub = (y1_arm + y3_arm) / 2;
         double y2_sub = y1_sub;    
         
         g2d.draw(new Line2D.Double(x1_sub, y1_sub, x2_sub, y2_sub));
         
      } // for (int p = 0; p < 3; p++)        
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void draw_neutral_ship(Graphics2D g2d, double wind_rose_diameter, boolean night_mode) 
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
      float[] dash = {2f, 0f, 2f};
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
      
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {   
         g2d.setPaint(color_deck);
      }
      g2d.fill(ship);

      //System.out.println("x = "  + x);
      //System.out.println("x1 = "  + x1);
      //System.out.println("x2 = "  + x2);
      //System.out.println("y = "  + y);
      //System.out.println("y1 = "  + y1);
      //System.out.println("y2 = "  + y2);
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void draw_research_vessel(Graphics2D g2d, double wind_rose_diameter, boolean night_mode) 
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
      float[] dash = {2f, 0f, 2f};
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

      // ship contour
      g2d.setStroke(new BasicStroke(2.0f));
      if (night_mode)
      {
         g2d.setColor(color_black);
      }
      else
      {
         g2d.setColor(color_white);
      }
      g2d.draw(ship);
      
      // ship fill
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {   
         g2d.setPaint(color_deck_research_vessel);
      }
      g2d.fill(ship);

      
      ///////// deck curve on fore ship /////////
      //
      double y_control = -(wind_rose_diameter / 2.0);                                   // control point quadTo; relative to origin (centre of wind rose)
      bow_height_virtual = ship_breadth * 2.5;                                   // quadTo; vertical distance from control point to start end end point       
      
      double x1_curve = x1;
      double y1_curve = y1;
      double ctrx_curve = 0;
      double ctry_curve = y_control + bow_height_virtual / 1.1;
      double x2_curve = x1_curve + ship_breadth;
      double y2_curve = y1_curve;
 
      QuadCurve2D quad = new QuadCurve2D.Double(x1_curve, y1_curve, ctrx_curve, ctry_curve, x2_curve, y2_curve);
      g2d.setStroke(new BasicStroke(2.0f));
      
      if (night_mode)
      {
         g2d.setColor(color_medium_gray);
      }
      else
      {
         g2d.setColor(color_white);
      }
      g2d.draw(quad);

      
      ///////////////////// ship bridge ///////////////////
      //
      //
      //
      //                   ------------
      //                  /            \
      //                 /              \
      //                /(x5,y5)         \(x6,y6)
      //  (x1,y1)---------------------------------(x2,y2)
      //         |                               |
      //         |                               |
      //  (x4,y4)---------------------------------(x3,y3)
      //                
      //
      //
      // NB
      //    bridge_height = distance y2 -> y3
      //    bridge_indention = x1 -> x5
      //
      // NB
      //    all relative to origin (0, 0)
      //
      // NB read in this bridge section "x1_bridge" for "x1" etc
      //
   
      
      double bridge_height = ship_breadth / 5;                    
      double bridge_indention = ship_breadth / 5;
      double dist_origin_bridge = 0 - bridge_height;  
      
      double bridge_overhang = 5;
      double x1_bridge = -ship_breadth / 2 - bridge_overhang;
      double x2_bridge = ship_breadth / 2 + bridge_overhang;
      double x3_bridge = x2_bridge;
      double x4_bridge = x1_bridge;
      double x5_bridge = x1_bridge + bridge_indention;
      double x6_bridge = x2_bridge - bridge_indention;
      double y1_bridge = dist_origin_bridge; //0;
      double y2_bridge = y1_bridge;
      double y3_bridge = y1_bridge + bridge_height;
      double y4_bridge = y3_bridge;
      double y5_bridge = y1_bridge;
      double y6_bridge = y1_bridge;
       

      GeneralPath bridge = new GeneralPath();
      bridge.moveTo(x1_bridge, y1_bridge);
      bridge.lineTo(x5_bridge, y5_bridge);
      bridge.quadTo(x, y1_bridge - (ship_breadth / 4), x6_bridge, y6_bridge);
      bridge.lineTo(x2_bridge, y2_bridge);
      bridge.lineTo(x3_bridge, y3_bridge);
      bridge.lineTo(x4_bridge, y4_bridge);
      bridge.closePath();

      // bridge contour
      g2d.setStroke(new BasicStroke(5.0f));
      if (night_mode)
      {
         g2d.setColor(color_medium_gray);
      }
      else
      {
         g2d.setColor(color_white);
      }
      g2d.draw(bridge);
      
      // bridge fill
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {   
         g2d.setPaint(color_bridge_research_vessel);
      }
      g2d.fill(bridge);
      
      
      //////// satellite dome (middle bridge) ///////
      //
      double dome_diameter = ship_breadth / 4;
      double x1_dome = 0 - dome_diameter / 2;                           // middle of the bridge
      double y1_dome = y4_bridge - dome_diameter;                 // middle of the bridge
      Shape shape_dome = new Ellipse2D.Double(x1_dome, y1_dome, dome_diameter, dome_diameter);
      
      // fill dome
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {
         g2d.setPaint(color_white);
      }
      g2d.fill(shape_dome);
      
      // contour dome
      g2d.setStroke(new BasicStroke(1.0f));
      g2d.setColor(color_black);
      g2d.draw(shape_dome);

      
     
      /////////////////// crane pillar /////////////////
      //
      //                          ___
      //                         /   \
      //       (x1_arm,y1_arm)  |     | (x2_arm,y2_arm)     <-- pillar crane
      //                        |\___/|
      //                        |     |
      //                        |     |
      //                        |     |
      //                        |     |                     <-- arm crane
      //                        |     |
      //                        |     |
      //                        |     |
      //                         -----
      //        (x3_arm,y3_arm)          (x4_arm,y4_arm)
      //
      //
      
      double width_crane_pillar = ship_breadth * 0.15;
      double height_crane_pillar = width_crane_pillar;
      double x_pillar_crane = x2 - width_crane_pillar;//0;
      double y_pillar_crane = (y4 - y3_bridge) / 2;                // crane position in the middle of bridge and where the indention/curve of the aft ship starts
      
      double x1_arm = x_pillar_crane;
      double x2_arm = x_pillar_crane + width_crane_pillar;
      double arm_slope = 4;
      double x3_arm = x1_arm + arm_slope;
      double x4_arm = x2_arm - arm_slope;
      
      // if the (ship) figure is very small
      if (x4_arm < x3_arm)
      {
         x3_arm = x1_arm;
         x4_arm = x2_arm;
      }
      
      Shape shape_crane_pillar = new Ellipse2D.Double(x_pillar_crane, y_pillar_crane, width_crane_pillar, height_crane_pillar);
      if (night_mode)
      {
         g2d.setPaint(color_night_crane_research_vessel);
      }
      else
      {
         g2d.setPaint(color_crane_research_vessel);
      }
      g2d.fill(shape_crane_pillar);
      
      if (night_mode)
      {
         g2d.setColor(color_night_crane_research_vessel); 
      }
      else
      {
         g2d.setColor(color_crane_research_vessel);
      }
      g2d.setStroke(new BasicStroke(1.0f));
      g2d.draw(shape_crane_pillar);
      
      
      ///////////////// crane arm /////////////
      //
      g2d.setStroke(new BasicStroke(3.0f)); 
      if (night_mode)
      {
         g2d.setColor(color_night_crane_research_vessel);
      }
      else
      {
         g2d.setColor(color_crane_research_vessel);
      }
      
      double y1_arm = y_pillar_crane + (height_crane_pillar / 2);
      double y2_arm = y1_arm;  
      double y3_arm = y1_arm - (y4 - y3_bridge) / 2;    // arm length = y4 - y3_bridge) / 2
      double y4_arm = y3_arm;
         
      double xPoints[] = {x1_arm, x3_arm, x4_arm, x2_arm};
      double yPoints[] = {y1_arm, y3_arm, y4_arm, y2_arm};         // -values for y goes/points to the bow

      GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
      polygon.moveTo(xPoints[0], yPoints[0]);
      for (int index = 1; index < xPoints.length; index++) 
      {
         polygon.lineTo(xPoints[index], yPoints[index]);
      }
      polygon.closePath();
      g2d.draw(polygon);  
         
      // crane arm reinforcement part
      double x1_sub = x1_arm + (arm_slope / 2);
      double x2_sub = x2_arm - (arm_slope / 2);
      double y1_sub = (y1_arm + y3_arm) / 2;
      double y2_sub = y1_sub;    
      g2d.draw(new Line2D.Double(x1_sub, y1_sub, x2_sub, y2_sub));
      
      
      ////////////////////// equipment containers //////////////////
      //
      //               x1                           x2
      //                |                           |
      //                |                           |
      //  (x4_bridge,y4_bridge)                    (x3_bridge,y3_bridge)
      //               -------------------------------                          rear side bridge   
      //                |                           |
      //   (x1_c,y1_c)  |---| (x2_c,y2_c)           |
      //                |   |                       |
      //                |   |                       |
      //                |   |                       |                                
      //    (x5_c,y5_c) |-- | (x6_c,y6_c)           |
      //                |   |                       |                                
      //                |   |                       |
      //                |   |                       |
      //   (x4_c,y4_c)  |---| (x3_c,y3_c)           |
      //                |                           |
      //                |                           |
      //        (x3,y3) |                           | (x4,y4)
      //                 \                         /
      //                  \                       /
      //                port                  starboard
      //
      //
      //
      double x1_container = x1;
      double x2_container = x1_container + (ship_breadth * 0.2); 
      double x3_container = x2_container;
      double x4_container = x1_container;       
      double y1_container = y4_bridge + (ship_breadth * 0.3);                             
      double y2_container = y1_container;
      double y3_container = y3 - (ship_breadth * 0.3);
      double y4_container = y3_container;
              
      double xPoints_container[] = {x1_container, x2_container, x3_container, x4_container};
      double yPoints_container[] = {y1_container, y2_container, y3_container, y4_container};                   // -values for y goes/points to the bow

      GeneralPath polygon_container = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints_container.length);
      polygon_container.moveTo(xPoints_container[0], yPoints_container[0]);
      for (int index = 1; index < xPoints_container.length; index++)            
      {
         polygon_container.lineTo(xPoints_container[index], yPoints_container[index]);
      }
      polygon_container.closePath();   
      
      // container fill
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {
         // NB these (reearch/equipment containers are always white!
         g2d.setPaint(color_white);
      }
      g2d.fill(polygon_container);  
      
      // container contour
      g2d.setStroke(new BasicStroke(1.0f)); 
      g2d.setColor(color_black);
      g2d.draw(polygon_container);
      
      // makes 2 containers
      double x5_container = x1_container;
      double x6_container = x2_container;     
      double y5_container = (y1_container + y4_container) / 2;
      double y6_container = y5_container;
      
      GeneralPath container_mid = new GeneralPath();
      container_mid.moveTo(x5_container, y5_container);
      container_mid.lineTo(x6_container, y6_container);
      container_mid.closePath();
      g2d.draw(container_mid);
      
      //////////////// container cradle /////////////////
      //
      //
      //                  (x7_c,y7_c)    
      //    (x5_c,y5_c) |-------| (x6_c,y6_c)           
      //                |   |   |                                                    
      //                |   |   |    
      //                |   |   |   
      //                |   |   |   
      //                |   |   |   
      //                |   |   |
      //                |   |   |                       
      //   (x4_c,y4_c)  |-------|  (x3_c,y3_c)          
      //                  (x8_c,y8_c)
      //
      //
      
      double x7_container = (x5_container + x6_container) / 2;
      double x8_container = x7_container;     
      double y7_container = y5_container;
      double y8_container = y4_container;

      
      // NB drawing the cradle lines via GeneralPath is also ok
      // 
      // GeneralPath container_cradle = new GeneralPath();
      // container_cradle.moveTo(x5_container, y5_container);
      // container_cradle.lineTo(x6_container, y6_container);
      // 
      // container_cradle.moveTo(x7_container, y7_container);
      // container_cradle.lineTo(x8_container, y8_container);
      //
      // container_cradle.moveTo(x4_container, y4_container);
      // container_cradle.lineTo(x3_container, y3_container);
      //
      // container_cradle.closePath();
      //
      // g2d.setStroke(new BasicStroke(2.0f)); 
      // g2d.setColor(Color.ORANGE);
      // g2d.draw(container_cradle);
      
      g2d.setStroke(new BasicStroke(2.0f)); 
      if (night_mode)
      {
         g2d.setColor(color_night_cradle_research_vessel);
      }
      else
      {
         g2d.setColor(color_cradle_research_vessel);
      }
      g2d.draw(new Line2D.Double(x5_container, y5_container, x6_container, y6_container));
      g2d.draw(new Line2D.Double(x7_container, y7_container, x8_container, y8_container));
      g2d.draw(new Line2D.Double(x4_container, y4_container, x3_container, y3_container));
     
      
      ///////////////////// aft hoist constuction /////////////////
      //
      //
      //             port                        starboard
      //               |                             |
      //               |                             |
      //               |                             |
      //               |                             |
      //  (x1_hoist,y1_hoist)                    (x2_hoist,y2_hoist)  
      //               |    ---------------------    |                        
      //               |   /                      \  |
      //               | /                          \|                            
      //               -------------------------------                        <--- stern
      //        (x6,y6)                                (x5,y5)  
      //
      //
      
      double x1_hoist = x6 + (ship_breadth / 10);
      double x2_hoist = x5 - (ship_breadth / 10);  
      double y1_hoist = y6 - (ship_length / 17);                               // NB ship_length = the length of the main mid section
      double y2_hoist = y1_hoist;  
      
      double xPoints_hoist[] = {x6, x1_hoist, x2_hoist, x5};
      double yPoints_hoist[] = {y6, y1_hoist, y2_hoist, y5};                   // -values for y goes/points to the bow
   
      g2d.setStroke(new BasicStroke(3.0f)); 
      if (night_mode)
      {
         g2d.setColor(color_night_hoist_research_vessel);
      }
      else
      {
         g2d.setColor(color_hoist_research_vessel);
      }
      
      GeneralPath polygon_hoist = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints_hoist.length);
      polygon_hoist.moveTo(xPoints_hoist[0], yPoints_hoist[0]);
      for (int index = 1; index < xPoints_hoist.length; index++)              // NB do not close the polygon!
      {
         polygon_hoist.lineTo(xPoints_hoist[index], yPoints_hoist[index]);
      }
      //polygon_hoist.closePath();                                            // NB do not close the polygon!
      g2d.draw(polygon_hoist);  
      
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void draw_ro_ro_ship(Graphics2D g2d, double wind_rose_diameter, boolean night_mode) 
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
      float[] dash = {2f, 0f, 2f};
      g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));
      g2d.draw(new Line2D.Double(0, wind_rose_diameter / 2 + ship_breadth, 0, -wind_rose_diameter / 2 - ship_breadth));

      //////////// bow ////////
      //
      x = 0;                                                             // control point quadTo; relative to origin (centre of wind rose)
      double bow_height_virtual = ship_breadth * 1.1;                    // NB factor 1.1 is indication of the bow curve; quadTo; vertical distance from control point to start end end point       
      y = -(wind_rose_diameter / 2.0) + (bow_height_virtual * 0.6);      // NB fator 0.6 depends on factor used in bow_height_virtual
      
      ///////////// main ship section (rectangle shape) ///////////
      //
      x1 = -ship_breadth / 2.0; 
      y1 = y + bow_height_virtual;  
      x2 = ship_breadth / 2.0;
      y2 = y1;
      double ship_length = Math.abs(y1) * 1.65;                          // so the length of the main mid section; nb factor 1.65 depends on the factor used at bow_height_virtual and y

      ///////////// aft ship (trapezium shape) ///////////
      //
      x3 = x1;
      x4 = x3 + ship_breadth;
      x5 = x4; // - (ship_breadth / 7);  // so in fact here not a trapezium but a simple square
      x6 = x3; // + (ship_breadth / 7);
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

      // fill total ship
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {   
         g2d.setPaint(color_deck_ro_ro);
      }
      g2d.fill(ship);
      
      // contour total ship
      g2d.setStroke(new BasicStroke(2.0f));
      if (night_mode)
      {
         g2d.setColor(color_medium_gray);
      }
      else
      {
         g2d.setColor(color_white);
      }
      g2d.draw(ship);
      
      
      //////////// fill the bow ////////
      GeneralPath bow = new GeneralPath();
      bow.moveTo(x1, y1);
      bow.quadTo(x, y, x2, y2);
      bow.lineTo(x1, y1);
      bow.closePath();
      
      if (night_mode)
      {
         g2d.setPaint(color_medium_gray);
      }
      else
      {   
         g2d.setPaint(color_white);
      }
      g2d.fill(bow);
      
      
      ////////////////// deck lanes ////////////
      //
      // NB drawing the deck lanes before drawing the bridge
      //
      
      g2d.setStroke(new BasicStroke(1.0f)); 
      g2d.setColor(Color.BLACK);
      
      // draw horizontal line as begin point deck lanes (where the bow begins)
      if (night_mode)
      {
         g2d.setColor(color_medium_gray);
      }
      else
      {
         g2d.setColor(color_white);
      }
      g2d.draw(new Line2D.Double(x1, y1, x2, y2));
      
      int number_lanes = 8;
      double lane_breadth = ship_breadth / number_lanes;
      g2d.setColor(color_lanes_ro_ro);
      for (int i = 1; i < number_lanes; i++)
      {
         // draw 7 lines (do not over-write the port and starboard ship contour line)
         double x1_lane = x1 + (i * lane_breadth);
         double x2_lane = x1_lane;                    // always vertical line
         double y1_lane = y1;
         double y2_lane = y6;
         g2d.draw(new Line2D.Double(x1_lane, y1_lane, x2_lane, y2_lane));
      }
      
      
      ///////////////////// ship bridge ////////////////////
      //
      //
      //
      //            (x3,y3)           (x4,y4)
      //                   ------------
      //                  /            \
      //                 /              \
      //                /(x2,y2)         \(x5,y5)
      //  (x1,y1)---------------------------------(x6,y6)            <---- dist_origin_bridge
      //         |                               |
      //         |                               |
      // (x12,y12)---------------------------------(x7,y7)
      //            | (x11,y11)         (x8,y8)|
      //            |                          |
      //            |                          |
      //            |                          |
      //            |                          |
      //  (x10,y10) ---------------------------- (x9,y9)            <--- y3 (aft of ship mid section)
      //
      //
      //
      //
      // NB
      //    bridge_height = distance y1 -> y12
      //    bridge_indention = x1 -> x2
      //
      //    bridge_height2 = distance y2 -> y3         // bridge expansion
      //    bridge_indention2 = x2 -> x3               // bridge expansion
      //
      //    acc_indention = x12 -> x11
      //    acc_height = y10 -> y12
      //
      // NB
      //    all relative to origin (0, 0)
      //
      // NB read in this bridge section "x1_bridge" for "x1" etc
      //
      //
      // NB drawing bridge after drawing the deck lanes
      //
   
      
      double bridge_height = ship_breadth / 9;                    
      double bridge_indention = ship_breadth / 3;
      double bridge_height2 = ship_breadth / 10;        // bridge expansion                   
      double bridge_indention2 = ship_breadth / 10;     // bridge expansion
      double dist_origin_bridge = 0 + (y3 / 3);         // NB y3 always positve (because always on aft ship)
      
      double bridge_overhang = 3;
      double acc_indention = bridge_overhang;//ship_breadth / 12;
      
      double x1_bridge = -ship_breadth / 2 - bridge_overhang;
      double x2_bridge = x1_bridge + bridge_indention;
      double x3_bridge = x2_bridge + bridge_indention2;
      
      double x12_bridge = x1_bridge;
      double x11_bridge = x12_bridge + acc_indention;
      double x10_bridge = x11_bridge;
      
      double x6_bridge = ship_breadth / 2 + bridge_overhang;
      double x5_bridge = x6_bridge - bridge_indention;
      double x4_bridge = x5_bridge - bridge_indention2;
      
      double x7_bridge = x6_bridge;
      double x8_bridge = x7_bridge - acc_indention;
      double x9_bridge = x8_bridge;
      
      double y1_bridge = dist_origin_bridge;        
      double y2_bridge = y1_bridge;       
      double y3_bridge = y2_bridge - bridge_height2;        
      double y4_bridge = y3_bridge ;      
      double y5_bridge = y1_bridge;       
      double y6_bridge = y1_bridge;     
      double y7_bridge = y6_bridge + bridge_height;   
      
      double acc_height = y3 - y7_bridge;
      
      double y8_bridge = y7_bridge;    
      double y9_bridge = y8_bridge + acc_height;
      double y10_bridge = y9_bridge;
      double y11_bridge = y8_bridge;
      double y12_bridge = y8_bridge;
      
      double xPoints_bridge[] = {x1_bridge, x2_bridge, x3_bridge, x4_bridge, x5_bridge, x6_bridge, x7_bridge, x8_bridge, x9_bridge, x10_bridge, x11_bridge, x12_bridge};
      double yPoints_bridge[] = {y1_bridge, y2_bridge, y3_bridge, y4_bridge, y5_bridge, y6_bridge, y7_bridge, y8_bridge, y9_bridge, y10_bridge, y11_bridge, y12_bridge};                   // -values for y goes/points to the bow

      GeneralPath polygon_bridge = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints_bridge.length);
      polygon_bridge.moveTo(xPoints_bridge[0], yPoints_bridge[0]);
      for (int index = 1; index < xPoints_bridge.length; index++)            
      {
         polygon_bridge.lineTo(xPoints_bridge[index], yPoints_bridge[index]);
      }
      polygon_bridge.closePath();   
      
      // bridge fill
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {
         g2d.setPaint(color_bridge_ro_ro_ship);
      }
      g2d.fill(polygon_bridge);  
      
      // bridge contour
      g2d.setStroke(new BasicStroke(2.0f)); 
      
      if (night_mode)
      {
         g2d.setColor(color_medium_gray);
      }
      else
      {
         g2d.setColor(color_white);
      }
      g2d.draw(polygon_bridge);
      
      // draw horizontal lines as indication of a 2nd deck aft accomodation
      double between_acc = 5;
      g2d.draw(new Line2D.Double(x10_bridge, y10_bridge - between_acc, x9_bridge, y9_bridge - between_acc));
      
      
      //////// satellite dome's (on bridge)  ///////
      //
      double dome_diameter = ship_breadth / 10;
      double x1_dome = 0 - dome_diameter / 2;                      // just on the centre line                               
      double y1_dome = y12_bridge;                                         
      //double x2_dome = x1_dome;                                    // 2nd dome behind the 1st one                           
      //double y2_dome = y1_dome + (dome_diameter * 2);
      
      Shape shape_dome_1 = new Ellipse2D.Double(x1_dome, y1_dome, dome_diameter, dome_diameter);
      //Shape shape_dome_2 = new Ellipse2D.Double(x2_dome, y2_dome, dome_diameter, dome_diameter);
      
      if (night_mode)
      {
         g2d.setPaint(color_medium_gray);
      }
      else
      {
         g2d.setPaint(color_white);
      }
      g2d.fill(shape_dome_1);
      //g2d.fill(shape_dome_2);
      
      // contour domes
      g2d.setStroke(new BasicStroke(1.0f)); 
      g2d.setPaint(color_black);
      g2d.draw(shape_dome_1);
      //g2d.draw(shape_dome_2);
      
      
      ///////////// stern ramp   ////////////
      //
      g2d.setStroke(new BasicStroke(6.0f)); 
      if (night_mode)
      {
         g2d.setColor(Color.DARK_GRAY);
      }
      else
      {
         g2d.setColor(color_bridge_ro_ro_ship);
      }
      double x1_ramp = x1 + lane_breadth;
      double x2_ramp = x2 - lane_breadth;
      g2d.draw(new Line2D.Double(x1_ramp, y6, x2_ramp, y5));
   }      
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void draw_ferry(Graphics2D g2d, double wind_rose_diameter, boolean night_mode) 
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
      float[] dash = {2f, 0f, 2f};
      g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));
      g2d.draw(new Line2D.Double(0, wind_rose_diameter / 2 + ship_breadth, 0, -wind_rose_diameter / 2 - ship_breadth));

      //////////// bow ////////
      //
      x = 0;                                                             // control point quadTo; relative to origin (centre of wind rose)
      double bow_height_virtual = ship_breadth * 1.2;                    // NB factor 1.2 indicates the bow curve; quadTo; vertical distance from control point to start end end point       
      y = -(wind_rose_diameter / 2.0) + (bow_height_virtual * 0.5);      // NB factor 0.5 depends on factor used in bow_height_virtual
      
      ///////////// main ship section (rectangle shape) ///////////
      //
      x1 = -ship_breadth / 2.0; 
      y1 = y + bow_height_virtual;  
      x2 = ship_breadth / 2.0;
      y2 = y1;
      double ship_length = Math.abs(y1) * 1.7;                          // so the length of the main mid section; nb factor 1.7 depends on the factor used at bow_height_virtual and y

      ///////////// aft ship (trapezium shape) ///////////
      //
      x3 = x1;
      x4 = x3 + ship_breadth;
      x5 = x4; // - (ship_breadth / 7);  // so in fact here not a trapezium but a simple square
      x6 = x3; // + (ship_breadth / 7);
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

      g2d.setStroke(new BasicStroke(2.0f));
      if (night_mode)
      {
         g2d.setPaint(color_black);
      }
      else
      {
         g2d.setPaint(color_white);
      }
      g2d.draw(ship);
      
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {   
         g2d.setPaint(color_deck1_ferry);
      }
      g2d.fill(ship);

      
      
      ///////////////////// ship bridge ////////////////////
      //
      //
      //
      //            (x3,y3)           (x4,y4)
      //                   ------------
      //                  /            \
      //                 /              \
      //                /(x2,y2)         \(x5,y5)
      //  (x1,y1)---------------------------------(x6,y6)            <---- dist_origin_bridge
      //         |                               |
      //         |                               |
      // (x12,y12)---------------------------------(x7,y7)
      //            | (x11,y11)         (x8,y8)|
      //            |                          |
      //            |                          |
      //            |                          |
      //            |                          |
      //  (x10,y10) ---------------------------- (x9,y9)            <--- y3 (aft of ship mid section)
      //
      //
      //
      //
      // NB
      //    bridge_height = distance y1 -> y12
      //    bridge_indention = x1 -> x2
      //
      //    bridge_height2 = distance y2 -> y3         // bridge expansion
      //    bridge_indention2 = x2 -> x3               // bridge expansion
      //
      //    acc_indention = x12 -> x11
      //    acc_height = y10 -> y12
      //
      // NB
      //    all relative to origin (0, 0)
      //
      // NB read in this bridge section "x1_bridge" for "x1" etc
      //
      //
      // NB drawing bridge after drawing the deck lanes
      //
   
      
      double bridge_height = ship_breadth / 9;                    
      double bridge_indention = ship_breadth / 3;
      double bridge_height2 = ship_breadth / 10;        // bridge expansion                   
      double bridge_indention2 = ship_breadth / 10;     // bridge expansion
      double dist_origin_bridge = y1; 
      
      double bridge_overhang = 3;
      double acc_indention = ship_breadth / 8;
      
      double x1_bridge = -ship_breadth / 2 - bridge_overhang;
      double x2_bridge = x1_bridge + bridge_indention;
      double x3_bridge = x2_bridge + bridge_indention2;
      
      double x12_bridge = x1_bridge;
      double x11_bridge = x12_bridge + acc_indention;
      double x10_bridge = x11_bridge;
      
      double x6_bridge = ship_breadth / 2 + bridge_overhang;
      double x5_bridge = x6_bridge - bridge_indention;
      double x4_bridge = x5_bridge - bridge_indention2;
      
      double x7_bridge = x6_bridge;
      double x8_bridge = x7_bridge - acc_indention;
      double x9_bridge = x8_bridge;
      
      double y1_bridge = dist_origin_bridge;        
      double y2_bridge = y1_bridge;       
      double y3_bridge = y2_bridge - bridge_height2;        
      double y4_bridge = y3_bridge ;      
      double y5_bridge = y1_bridge;       
      double y6_bridge = y1_bridge;     
      double y7_bridge = y6_bridge + bridge_height;   
      
      double acc_height = (y6 - ship_breadth / 2) - y7_bridge;
      
      double y8_bridge = y7_bridge;    
      double y9_bridge = y8_bridge + acc_height;
      double y10_bridge = y9_bridge;
      double y11_bridge = y8_bridge;
      double y12_bridge = y8_bridge;
      
      double xPoints_bridge[] = {x1_bridge, x2_bridge, x3_bridge, x4_bridge, x5_bridge, x6_bridge, x7_bridge, x8_bridge, x9_bridge, x10_bridge, x11_bridge, x12_bridge};
      double yPoints_bridge[] = {y1_bridge, y2_bridge, y3_bridge, y4_bridge, y5_bridge, y6_bridge, y7_bridge, y8_bridge, y9_bridge, y10_bridge, y11_bridge, y12_bridge};                   // -values for y goes/points to the bow

      GeneralPath polygon_bridge = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints_bridge.length);
      polygon_bridge.moveTo(xPoints_bridge[0], yPoints_bridge[0]);
      for (int index = 1; index < xPoints_bridge.length; index++)            
      {
         polygon_bridge.lineTo(xPoints_bridge[index], yPoints_bridge[index]);
      }
      polygon_bridge.closePath();   
      
      // bridge fill
      if (night_mode)
      {
         g2d.setPaint(color_medium_gray);
      }
      else
      {
         g2d.setPaint(color_deck2_ferry);
      }
      g2d.fill(polygon_bridge);  
      
      // bridge contour
      g2d.setStroke(new BasicStroke(2.0f)); 
      if (night_mode)
      {
         g2d.setColor(Color.LIGHT_GRAY);
      }
      else
      {
         g2d.setColor(color_white);
      }
      g2d.draw(polygon_bridge);
      
      
      /////// top acc deck (upper deck/top deck) //////
      //
      //
      //  (x13,y13)             (x14,y14)
      //           -------------
      //           |           |
      //           |           |
      //           |           |
      //           |           |
      //           |           |
      //           _____________
      //   (x16,y16)            (x15,y15)
      //
      //
      double between_acc = 5;
      double x13_bridge = x11_bridge + acc_indention;
      double x14_bridge = x8_bridge - acc_indention;
      double x15_bridge = x14_bridge;
      double x16_bridge = x13_bridge;
      double y13_bridge = y7_bridge + (between_acc * 4);
      double y14_bridge = y13_bridge;
      double y15_bridge = y9_bridge - (between_acc * 4);
      double y16_bridge = y15_bridge;      
       
      double xPoints_top_deck[] = {x13_bridge, x14_bridge, x15_bridge, x16_bridge};
      double yPoints_top_deck[] = {y13_bridge, y14_bridge, y15_bridge, y16_bridge};                   // -values for y goes/points to the bow
        
      GeneralPath polygon_top_deck = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints_top_deck.length);
      polygon_top_deck.moveTo(xPoints_top_deck[0], yPoints_top_deck[0]);
      for (int index = 1; index < xPoints_top_deck.length; index++)            
      {
         polygon_top_deck.lineTo(xPoints_top_deck[index], yPoints_top_deck[index]);
      }
      polygon_top_deck.closePath();        
      
      // top deck fill
      if (night_mode)
      {
         g2d.setPaint(color_medium_gray);
      }
      else
      {
         g2d.setPaint(color_deck3_ferry);
      }
      g2d.fill(polygon_top_deck);  
              
      // top deck contour
      g2d.setStroke(new BasicStroke(2.0f)); 
      if (night_mode)
      {
         g2d.setColor(color_medium_gray);
      }
      else
      {
         g2d.setColor(color_white);
      }
      g2d.draw(polygon_top_deck);        
      
       
      ////////////// funnel /////////////
      //
      double x1_funnel = x16_bridge / 1.8; 
      double width_funnel = Math.abs(x1_funnel) * 2;
      double length_funnel = width_funnel * 1.5;
      double y1_funnel = (y15_bridge / 2);

      // fill funnel
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {
         g2d.setPaint(color_funnel_ferry);
      }
      g2d.fill(new RoundRectangle2D.Double(x1_funnel, y1_funnel, width_funnel, length_funnel, 20, 20));
      
      // contour funnel
      if (night_mode)
      {
         g2d.setPaint(color_black);
      }
      else
      {
         g2d.setPaint(Color.BLUE);
      }
      g2d.draw(new RoundRectangle2D.Double(x1_funnel, y1_funnel, width_funnel, length_funnel, 20, 20));

      ///////////// pipes in funnel //////////////
      //
      g2d.setPaint(color_black);
      double diameter_pipe = width_funnel / 4;
      double x1_pipe;
      double y1_pipe = 0;

      for (int p = 0; p < 10; p++) 
      {
         x1_pipe = -(diameter_pipe / 2);
         y1_pipe = (y1_funnel + diameter_pipe) + (p * diameter_pipe);

         if ((diameter_pipe * (p + 1)) < (length_funnel - (diameter_pipe * 2))) 
         {
            Shape shape_pipe = new Ellipse2D.Double(x1_pipe, y1_pipe, diameter_pipe, diameter_pipe);
            g2d.fill(shape_pipe);
         } 
         else 
         {
            break;
         }
      } // for (int p = 0; p < 10; p++)
      
      
      //////// satellite dome's (on top deck)  ///////
      //
      double dome_diameter = ship_breadth / 8;
      
      double x1_dome = 0 - dome_diameter / 2;                                  // middle of the fore acc
      double y1_dome = (y1_funnel - length_funnel + y13_bridge) / 2;           // between bridge and funnel
      double x2_dome = 0 - dome_diameter / 2;                                  // middle of the fore acc
      double y2_dome = y1_dome - (dome_diameter * 2);
      
      Shape shape_dome_1 = new Ellipse2D.Double(x1_dome, y1_dome, dome_diameter, dome_diameter);
      Shape shape_dome_2 = new Ellipse2D.Double(x2_dome, y2_dome, dome_diameter, dome_diameter);
      
      if (night_mode)
      {
         g2d.setPaint(Color.LIGHT_GRAY);
      }
      else
      {
         g2d.setPaint(color_white);
      }
      g2d.fill(shape_dome_1);
      g2d.fill(shape_dome_2);
      
      // contour domes
      g2d.setStroke(new BasicStroke(1.0f)); 
      g2d.setPaint(color_black);
      g2d.draw(shape_dome_1);
      g2d.draw(shape_dome_2);
      
      
      ///////// stern ramp   ////////
      g2d.setStroke(new BasicStroke(6.0f)); 
      if (night_mode)
      {
         g2d.setColor(Color.DARK_GRAY);
      }
      else
      {
         g2d.setColor(Color.BLUE);
      }
      double x1_ramp = x1 + acc_indention;
      double x2_ramp = x2 - acc_indention;
      g2d.draw(new Line2D.Double(x1_ramp, y6, x2_ramp, y5));
      
   }      
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void draw_container_ship(Graphics2D g2d, double wind_rose_diameter, boolean night_mode)
   {
      // ship_breadth is leading for scaling
      //
      double ship_breadth = wind_rose_diameter / 8.0;

      ////////////// course line ///////////////
      //
      g2d.setColor(color_black);
      float[] dash = {2f, 0f, 2f};
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

      //g2d.setPaint(color_deck);
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {   
         g2d.setPaint(color_deck);
      }
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

      //g2d.setPaint(color_deck);
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {   
         g2d.setPaint(color_deck);
      }
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

      for (int m = 0; m < 2; m++) // two loops (fore and aft ship)
      {
         // NB m = 0: fore ship
         // NB m =1: aft ship

         if (m == 0) // fore ship
         {
            y_container = y1;                                                   // now y_container is negative value
         } 
         else // aft ship
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
            else // aft ship
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
               if (m == 0) // fore ship 
               {
                  y_container_offset = 0;                                             // because of the starting point (x,y) of the drawing of a rectangle
               } 
               else // aft ship
               {
                  y_container_offset = container_length;                             // because of the starting point (x,y) of the drawing of a rectangle
               }

               int start_in_row = 0;
               int end_in_row = 0;
               if (i == 0 && m == 0) // first row fore ship (20ft row on the bow)
               {
                  container_length = container_length_20;
                  x_container = x1 + container_breadth;
                  y_container = y1 - container_length;
                  start_in_row = 1;                                                  // because less container space available on the bow
                  end_in_row = number_containers_in_row - 1;                          // because less container space available on the bow
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

                  int random = (int)(Math.random() * 10 + 1);    // 1 - 10 return value

                  switch (random) 
                  {
                     case 1:  g2d.setPaint(color_container_1); break;
                     case 2:  g2d.setPaint(color_container_2); break;
                     case 3:  g2d.setPaint(color_container_3); break;
                     case 4:  g2d.setPaint(color_container_4); break;
                     case 5:  g2d.setPaint(color_container_5); break;
                     case 6:  g2d.setPaint(color_container_6); break;
                     case 7:  g2d.setPaint(color_container_7); break;
                     case 8:  g2d.setPaint(color_container_8); break;
                     case 9:  g2d.setPaint(color_container_9); break;
                     case 10: g2d.setPaint(color_container_10); break;
                     default: g2d.setPaint(color_container_1); break;
                  }

                  g2d.fill(shape_container);

                  g2d.setColor(color_black);
                  g2d.setStroke(new BasicStroke(1.0f));
                  g2d.draw(shape_container);

                  x_container = x_container + container_breadth;

                  //System.out.println("+++ i = " + i + " ;x_container: = " + x_container); 
               } // for (int k = 0; k < number_containers_row; k++)

               if (m == 0) // fore ship
               {
                  y_container = y_container + container_length;
               } 
               else // aft ship
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
      draw_ship_bridge(g2d, ship_breadth, bridge_height, acc_height, dist_origin_bridge, bridge_indention, night_mode);

   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void draw_oil_tanker(Graphics2D g2d, double wind_rose_diameter, boolean night_mode) 
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
      float[] dash = {2f, 0f, 2f};
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

      GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
      polygon.moveTo(xPoints[0], yPoints[0]);
      for (int index = 1; index < xPoints.length; index++) 
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
      double bridge_height = 7;                      
      double acc_height = (y5 - y4) / 2; //17;                       
      double bridge_indention = ship_breadth / 4;
      double dist_origin_bridge = y3 - bridge_height;
      draw_ship_bridge(g2d, ship_breadth, bridge_height, acc_height, dist_origin_bridge, bridge_indention, night_mode);

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
      x_pipe = -between_pipes;
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
         if (i == 0) // port deck line
         {
            x0_deck_line = x1;
            x1_deck_line = x1;
            x2_deck_line = -(ship_breadth / 2) + (2 * between_pipes);
            x3_deck_line = x2_deck_line;
         } 
         else // starboard deck line
         {
            x0_deck_line = x1 + ship_breadth;
            x1_deck_line = x1 + ship_breadth;
            x2_deck_line = (ship_breadth / 2) - (2 * between_pipes);
            x3_deck_line = x2_deck_line;
         }

         double xPoints_deck_line[] = {x0_deck_line, x1_deck_line, x2_deck_line, x3_deck_line};         // -values for x goes/points to port (relative to origin)
         double yPoints_deck_line[] = {y0_deck_line, y1_deck_line, y2_deck_line, y3_deck_line};         // -values for y goes/points to the bow( relative to origin)

         GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints_deck_line.length);
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
      g2d.setStroke(new BasicStroke((float) heli_circle_thickness));
      g2d.draw(shape_heli);

      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else
      {
         g2d.setPaint(Color.LIGHT_GRAY);
      }
      g2d.fill(shape_heli);
   /*           
      // H in the middle of the Heli circle
      FontMetrics fm = g2d.getFontMetrics();
      String msg = "H";
      int msgWidth = fm.stringWidth(msg);
      int msgAscent = fm.getAscent();
      g2d.setColor(Color.WHITE);
   
      //g2d.translate((float)msgX,(float)msgY);
      g2d.translate((float)x_heli + width_heli / 2, y_heli + height_heli / 2);
      g2d.rotate(Math.toRadians(90));                                 // H visible in direction perpendicular port side
      g2d.drawString(msg, -msgWidth / 2, (int)(msgAscent / 2));     // NB msgAscent / 2.5 for vertical fine tuning)
      //g2d.drawString(msg, -msgWidth / 2, 0);     // NB msgAscent / 2.5 for vertical fine tuning)
      g2d.rotate(Math.toRadians(-90));                                // rest coordinate rotation   
      g2d.translate(-((float)x_heli + width_heli / 2), -(y_heli + height_heli / 2));
   */

   }

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void draw_lng_tanker(Graphics2D g2d, double wind_rose_diameter, boolean night_mode) 
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
      float[] dash = {2f, 0f, 2f};
      g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));
      g2d.draw(new Line2D.Double(0, wind_rose_diameter / 2 + ship_breadth, 0, -wind_rose_diameter / 2 - ship_breadth));

      double x = -ship_breadth / 2.0;                                       // relative to origin (centre of wind rose)
      double y = -(wind_rose_diameter / 2.0 - ship_breadth);                // relative to origin (centre of wind rose)

      ////////////// Bow (ellipse shape) //////////////
      //
      double bow_width = ship_breadth;
      double bow_height = ship_breadth * 2.0;                               // total height of the ellipse, bur only the upper half will be visible as the bow        
      Shape shape_bow = new Ellipse2D.Double(x, y, bow_width, bow_height);

      g2d.setStroke(new BasicStroke(1.0f));
      g2d.draw(shape_bow);                                                  // point East = 0 degrees (normally you would call East 90 degrees...) -> to the left

      if (night_mode)
      {
         g2d.setPaint(color_medium_gray);
      }
      else
      {   
         g2d.setPaint(color_deck_lng_tanker);
      }
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

      if (night_mode)
      {
         g2d.setPaint(color_medium_gray);
      }
      else
      {
         g2d.setPaint(color_deck_lng_tanker);
      }
      g2d.fill(shape_midships);


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

      GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
      polygon.moveTo(xPoints[0], yPoints[0]);
      for (int index = 1; index < xPoints.length; index++) 
      {
         polygon.lineTo(xPoints[index], yPoints[index]);
      }
      polygon.closePath();

      // aft ship fill
      if (night_mode)
      {
         g2d.setPaint(color_medium_gray);
      }
      else
      {
         g2d.setPaint(color_deck_lng_tanker);
      }
      g2d.fill(polygon);

      // aft ship contour
      g2d.setColor(color_black);
      g2d.draw(polygon);

      /////////////// ship bridge /////////
      //
      double bridge_height = 7;                     
      double acc_height = (y5 - y4) / 2; //17;                        
      double bridge_indention = ship_breadth / 5;
      double dist_origin_bridge = y3 - bridge_height;
      draw_ship_bridge(g2d, ship_breadth, bridge_height, acc_height, dist_origin_bridge, bridge_indention, night_mode);

      ///////////// LNG tanks //////////
      //
      double tank_intermediate = ship_breadth / 10;   // distances between 2 tanks and between tank and railing
      double tank_width = ship_breadth - (2 * tank_intermediate);
      double tank_height = tank_width;

      double y_tank = y1;
      double x_tank = -tank_width / 2;

      for (int i = 0; i < 20; i++) // max 20 hatches
      {
         if (y_tank + (tank_height + tank_intermediate / 2) < (ship_length / 2 - bridge_height)) 
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

   

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void draw_bulk_carrier(Graphics2D g2d, double wind_rose_diameter, boolean night_mode) 
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
      float[] dash = {2f, 0f, 2f};
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

      GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
      polygon.moveTo(xPoints[0], yPoints[0]);
      for (int index = 1; index < xPoints.length; index++) 
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
      double bridge_height = 7;                      
      double acc_height = (y5 - y4) / 2;//17;                     
      double bridge_indention = ship_breadth / 5;
      double dist_origin_bridge = y3 - bridge_height;
      draw_ship_bridge(g2d, ship_breadth, bridge_height, acc_height, dist_origin_bridge, bridge_indention, night_mode);

      ////////////// hatches ////////////
      //
      double hatch_breath = ship_breadth / 2;
      double hatch_length = hatch_breath;
      double hatch_intermediate = hatch_breath / 2;   // distances between 2 hatches
      double y_hatch = y1;
      double x_hatch = -hatch_breath / 2;

      for (int i = 0; i < 20; i++) // max 20 hatches
      {
         if (y_hatch + (hatch_length + hatch_intermediate / 2) < (ship_length / 2 - bridge_height)) 
         {
            g2d.setPaint(color_bulk_carrier);
            g2d.fill3DRect((int) x_hatch, (int) y_hatch, (int) hatch_breath, (int) hatch_length, true);   // raised hatches
            y_hatch += hatch_length + hatch_intermediate;
         } 
         else 
         {
            break;
         }
      } // for (int i = 0; i < 20; i++)

   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void draw_ship_bridge(Graphics2D g2d, double ship_breadth, double bridge_height, double acc_height, double dist_origin_bridge, double bridge_indention, boolean night_mode) 
   {
      // NB ship bridge for a limited number od ships (not for all ship types)
      
      
      ///////////// ship bridge ////////////
      //
      //
      //
      //  (x1,y1)----------------------------- (x2,y2)          <---- dist_origin_bridge
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

      GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
      polygon.moveTo(xPoints[0], yPoints[0]);
      for (int index = 1; index < xPoints.length; index++) 
      {
         polygon.lineTo(xPoints[index], yPoints[index]);
      }
      polygon.closePath();

      // bridge fill
      if (night_mode)
      {
         g2d.setPaint(Color.DARK_GRAY);
      }
      else if (main.ship_type_dashboard.equals(main.CONTAINER_SHIP)) 
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
         if (night_mode)
         {
            g2d.setColor(color_medium_gray);
         }
         else
         {    
            g2d.setColor(color_white);
         }
      } 
      else 
      {
         g2d.setColor(color_black);
      }
      g2d.draw(polygon);

   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void draw_passenger_ship(Graphics2D g2d, double wind_rose_diameter, boolean night_mode) 
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
      float[] dash = {2f, 0f, 2f};
      g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 1.0f));
      g2d.draw(new Line2D.Double(0, wind_rose_diameter / 2 + ship_breadth, 0, -wind_rose_diameter / 2 - ship_breadth));

      ////////////// Bow //////////////
      //
      double bow_height = ship_breadth * 3.0;

      x_control = 0;                                                             // control point quadTo; relative to origin (centre of wind rose)
      y_control = -(wind_rose_diameter / 2.0);                                   // control point quadTo; relative to origin (centre of wind rose)
      bow_height_virtual = ship_breadth * 2.5;                                   // quadTo; vertical distance from control point to start end end point       
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
      
      if (night_mode)
      {
         g2d.setPaint(color_medium_gray);
      }
      else
      {
         g2d.setPaint(color_deck_passenger_ship);
      }
      g2d.fill(bow);

      ///////////// main ship section (rectangle shape) ///////////
      //
      double stern = bow_height / 2;
      double ship_length = (Math.abs(y1) * 2) + stern;    // length without the bow!
      Shape shape_midships = new Rectangle2D.Double(x1, y1, ship_breadth, ship_length);

      g2d.setColor(color_black);
      g2d.setStroke(new BasicStroke(1.0f));
      g2d.draw(shape_midships);

      if (night_mode)
      {
         g2d.setPaint(color_medium_gray);
      }
      else
      {
         g2d.setPaint(color_deck_passenger_ship);
      }
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
      //                 (x2,y2)           |             (x3,y3)
      //                       ____________|____________                 ^ x = + (to starboard)
      //                      | \ ____________________/ |                |
      //                      |  |         |          | |                |
      //                      |  |         |          | |                |
      //  <--- fore ship      |  |         |          | |                |                             ----> aft ship    
      //                      |  | ________|__________| |                |
      //                      |_/_____________________\_|                |
      //                 (x1,y1)           |              (x4,y4)        v x = - (to port)
      //                                   |
      //                                   |                   
      //                                   |   
      //
      //
      //
      //
      double pool_width = ship_breadth * 0.6;
      double pool_length = pool_width * 1.2;
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

      /*   
      // vortex pool slide (suisbuis)
      //
      // NB e.g. see: https://stackoverflow.com/questions/29638733/java-draw-a-circular-spiral-using-drawarc/29639168
      //
      g2d.setStroke(new BasicStroke(2.0f));
      g2d.setPaint(Color.ORANGE);
      int centerX = 0;
      int centerY = 0 - (int)((Math.abs(y1_pool) / 2)); //(int)(y1_pool + (2 * pool_edge));

      int numIterations = 2;
      int arcWidth = 2;      // start width circle (px)
      int arcGrowDelta = 4;  // growth every circle (px)

      for (int i = 0; i < numIterations; i++) 
      {
         if ((2 * arcWidth) < pool_width - (2 * pool_edge))
         {
            g2d.drawArc(centerX - arcWidth, centerY - arcWidth, 2 * arcWidth, 2 * arcWidth, 0, 180);
            arcWidth += arcGrowDelta;
            g2d.drawArc(centerX - arcWidth, centerY - arcWidth, 2 * arcWidth - arcGrowDelta, 2 * arcWidth, 180, 180);    
         
         } // if (2 * arcWidth < pool_width)
      } // for (int i = 0; i < numIterations; i++) 
      */
      
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

         GeneralPath polygon_dive = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
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
            if (night_mode)
            {
               g2d.setPaint(color_medium_gray); 
            }  
            else
            {
               g2d.setPaint(color_acc_passenger_ship);
            }
            g2d.fill(gp);
         }
      } // for (int k = 0; k < 3; k++)

      //////// satellite dome (after building "fore accomodation"!) ///////
      //
      double dome_diameter = ship_breadth / 4;
      double x1_dome = 0 - dome_diameter / 2;            // middle of the fore acc
      double y1_dome = (y1_gp + y4_gp) / 2 - (dome_diameter / 2);
      Shape shape_dome = new Ellipse2D.Double(x1_dome, y1_dome, dome_diameter, dome_diameter);
      
      if (night_mode)
      {
         g2d.setPaint(Color.LIGHT_GRAY);
      }
      else
      {
         g2d.setPaint(color_white);
      }
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
            if (night_mode)
            {
               g2d.setPaint(color_medium_gray); 
            } 
            else
            {   
               g2d.setPaint(color_acc_passenger_ship);
            }
            g2d.fill(gp2);
         }
      } // for (int k = 0; k < 3; k++)

      ////////// funnel /////////////
      //
      double x1_funnel = x1_gp2 / 1.8;
      double width_funnel = Math.abs(x1_funnel) * 2;
      double length_funnel = width_funnel * 1.5;
      double y1_funnel = y3_gp2 - length_funnel;

      g2d.setPaint(color_funnel_passenger_ship);
      g2d.fill(new RoundRectangle2D.Double(x1_funnel, y1_funnel, width_funnel, length_funnel, 20, 20));

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
            Shape shape_pipe = new Ellipse2D.Double(x1_pipe, y1_pipe, diameter_pipe, diameter_pipe);
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

      g2d.setStroke(new BasicStroke((float) life_boat_breadth));
      double x1_life_boat = 0;
      double y1_life_boat = 0;
      double x2_life_boat = 0;
      double y2_life_boat;

      for (int h = 0; h < 2; h++) // h = 0: port side; h = 1 : starboard side
      {
         if (h == 0) // port side
         {
            x1_life_boat = x1;
            y1_life_boat = y1 + life_boat_length;
            x2_life_boat = x1_life_boat;
         } 
         else if (h == 1) // starboard side
         {
            x1_life_boat = x1 + ship_breadth;
            y1_life_boat = y1 + life_boat_length;
            x2_life_boat = x1_life_boat;
         }

         for (int b = 0; b < 20; b++) 
         {
            if (y1_life_boat + (2 * life_boat_length) < y1_pipe) // from "bow" to the "pipes" 
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

   private final Color color_white;
   private final Color color_black;
   private final Color color_deck_passenger_ship;
   private final Color color_pool_1;
   private final Color color_pool_2;
   private final Color color_pool_3;
   private final Color color_acc_passenger_ship;
   private final Color color_life_boat;
   private final Color color_funnel_passenger_ship;
   private final Color color_funnel_ferry;
   private final Color color_bridge_oil_tanker;
   private final Color color_bridge_container_ship;
   private final Color color_bridge_ro_ro_ship;
   private final Color color_bulk_carrier;
   private final Color color_lng_tanks;
   private final Color color_deck_lng_tanker;
   private final Color color_deck_oil_tanker;
   private final Color color_pipes_oil_tanker;
   private final Color color_deck;
   private final Color color_deck_research_vessel;
   private final Color color_crane_research_vessel;
   private final Color color_hoist_research_vessel;
   private final Color color_cradle_research_vessel;
   private final Color color_bridge_research_vessel;
   private final Color color_night_crane_research_vessel;
   private final Color color_night_hoist_research_vessel;
   private final Color color_night_cradle_research_vessel;
   private final Color color_deck_general_cargo_ship; 
   private final Color color_deck1_ferry;
   private final Color color_deck2_ferry;
   private final Color color_deck3_ferry;
   private final Color color_deck_ro_ro;
   private final Color color_lanes_ro_ro;
   private final Color color_hatches_general_cargo_ship; 
   private final Color color_medium_gray;
   private final Color color_container_1;
   private final Color color_container_2;
   private final Color color_container_3;
   private final Color color_container_4;
   private final Color color_container_5;
   private final Color color_container_6;
   private final Color color_container_7;
   private final Color color_container_8;
   private final Color color_container_9;
   private final Color color_container_10;
   
   
}
