/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbowin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;







/**
 *
 * @author marti
 */
public class mylatestmeasurements extends javax.swing.JFrame {

/*   
   private class jsonfilefilter extends FileFilter
   {
 
      @Override
      public String getDescription() 
      {
         return "JSON files (*.json)";
      }
 
      @Override
      public boolean accept(File f) 
      {
         if (f.isDirectory()) 
         {
            return true;
         } 
         else 
         {
            return f.getName().toLowerCase().endsWith(".json");
         }
        
         
      } // public boolean accept(File f) 
   }
*/

   
   
   /**
    * Creates new form mylatestmeasurements
    */
   public mylatestmeasurements() {
      initComponents();
      //setLocation(main.x_pos_latestmeasurements_frame, main.y_pos_latestmeasurements_frame);
      setLocation(main.x_pos_main_frame, main.y_pos_main_frame);
      
      initComponents2();
   }

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                  initComponents2()                                          */
   /*                                                                                             */ 
   /***********************************************************************************************/
   private void initComponents2()
   {
      jTable1.setModel(new javax.swing.table.DefaultTableModel(
         new Object [AANTAL_AWS_MEASUREMENTS][AANTAL_AWS_PARAMETERS],
         new String [] {"Date [UTC]", "Time [UTC]", "Lat [°]", "Lon [°]", "SLP [hPa]", "Air temp [°C]", "RH [%]", "SST [°C]", "wind speed true [m/s]", "wind dir true [°]", "wind gust true [m/s]"}
      ) 
      {
         Class[] types = new Class [] 
         {
            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
         };
         boolean[] canEdit = new boolean [] 
         {
            false, false, false, false, false, false, false, false, false, false, false
         };

         @Override
         public Class getColumnClass(int columnIndex) 
         {
            return types [columnIndex];
         }

         @Override
         public boolean isCellEditable(int rowIndex, int columnIndex) 
         {
            return canEdit [columnIndex];
         }
      });
      
      
      // if required change column header wind speed and wind gust (from m.s. -> knots)
      if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)             // knots required
      {
         JTableHeader header = jTable1.getTableHeader();
         TableColumnModel colMod = header.getColumnModel();
         
         TableColumn wind_speed_Col = colMod.getColumn(8);                 // 8th column (start counting at 0)
         wind_speed_Col.setHeaderValue("wind speed true [kts]");
      
         TableColumn wind_gust_Col = colMod.getColumn(10);                 // 10th column (start counting at 0)
         wind_gust_Col.setHeaderValue("wind gust true [kts]");
      
         header.repaint();
      }
      
      // import the measurements 
      import_AWS_measurements();
   }
   
   

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                reset_all_table_cells()                                      */
   /*                                                                                             */ 
   /***********************************************************************************************/
   private void reset_all_table_cells()
   {
      for (int i = 0; i < AANTAL_AWS_MEASUREMENTS; i++)      // rows
      {
         for (int c = 0; c < AANTAL_AWS_PARAMETERS; c++)     // columns (date, time, lat etc.)
         {
            jTable1.setValueAt("", i, c);
         }
      }   
   }
   
   

   /***********************************************************************************************/
   /*                                                                                             */
   /*                               import_AWS_measurements()                                     */
   /*                                                                                             */
   /***********************************************************************************************/
   private void import_AWS_measurements()
   {
      // called from: - initComponents2()
      //              - Refresh_button_actionPerformed()
      
      new SwingWorker<Void, Void>()
      {
         @Override
         protected Void doInBackground() throws Exception
         {
            Read_Sensor_Data_Files_For_Latest_AWS_Measurements();
               
            return null;
         } // protected Void doInBackground() throws Exception

         @Override
         protected void done()
         {
            reset_all_table_cells();
            insert_AWS_values_in_table_fields();
         }
      }.execute(); // new SwingWorker<Void, Void>()
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                             insert_AWS_values_in_table_fields                               */
   /*                                                                                             */
   /***********************************************************************************************/
   private void insert_AWS_values_in_table_fields()
   {
      
      // NB AWS_array[i][c]: rows i -> highest i-index numbers = most recent in time; i = 0 -> record of 6 hours ago
      String table_insert = "";
      int row = -1;
      
      for (int i = AANTAL_AWS_MEASUREMENTS -1; i >= 0; i--)       // rows, newest/latest records on top in the table
      {
         if ( (!AWS_array[i][date_index].equals("")) && (!AWS_array[i][time_index].equals("")) )
         {
            // only insert the row (measurement record) if date and time are available
            row++;                 // so the first recent not empty record will be displayed on row 0 (top most)
         
            for (int c = 0; c < AANTAL_AWS_PARAMETERS; c++)     // columns
            {
               //System.out.println("+++ " +  "AWS_array[" + i + "][" + c + "]: " + AWS_array[i][c]);
               
               if (c == date_index)
               {
                  if (AWS_array[i][c].length() == 8)
                  {
                     // format e.g. 2019-02-21 (ISO date)
                     table_insert = AWS_array[i][c].substring(0, 4) + "-" + AWS_array[i][c].substring(4, 6) + "-" + AWS_array[i][c].substring(6, 8);
                  }
                  else
                  {
                     table_insert = "";
                  }
               }
               else if (c == time_index)
               {
                  if (AWS_array[i][c].length() == 6)
                  {
                     // format e.g. 09:20
                     table_insert = AWS_array[i][c].substring(0, 2) + ":" + AWS_array[i][c].substring(2, 4);
                  }
                  else
                  {
                     table_insert = "";
                  }
               }
               else if ((c == true_wind_speed_index) || (c == true_wind_gust_index))
               {
                  // convert m/s to knots if required
                  if (AWS_array[i][c].length() >= 1 && (!AWS_array[i][c].equals("")))
                  {
                     double conversion_factor = 1;
                     if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)             // knots required
                     {
                        conversion_factor = main.M_S_KNOT_CONVERSION;
                     }
                     else                                                                 // m/s
                     {
                        conversion_factor = 1;
                     }   
                        
                     double table_insert_double = Double.parseDouble(AWS_array[i][c]) * conversion_factor;
                     table_insert = String.format("%.2f", table_insert_double);
                     
                  } // if (AWS_array[i][c].length() >= 1 && (!AWS_array[i][c].equals("")))
                  else
                  {
                     table_insert = "";
                  }   
               }
               else
               {
                  table_insert = AWS_array[i][c];
               }
               
               jTable1.setValueAt(table_insert, row, c);
               
            } // for (int c = 0; c < AANTAL_AWS_PARAMETERS; c++) 
         } // if ( (!AWS_array[i][date_index].equals("")) && (!AWS_array[i][time_index].equals("")) )
      } // for (int i = AANTAL_AWS_MEASUREMENTS -1; i >= 0; i--)  
      
      
      // Date/Time select label
      jLabel2.setText(main.sdf_tsl_2.format(new Date()) + " UTC");   //  new Date() -> always in UTC
      
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */ 
   /*                          Read_Sensor_Data_Files_For_Latest_AWS_Measurements                 */
   /*                                                                                             */ 
   /***********************************************************************************************/
   public static void Read_Sensor_Data_Files_For_Latest_AWS_Measurements()
   {
      // called from: - import_AWS_measurements() [mylatestmeasurements.java]
      //              - OSM_AWS_Sensor_data_on_leaflet_map() [OSM.java]     
      
      // NB this function reads sensor data from an AWS (not from an individual Vaisala or Mintaka barometer) stored in files
      //
      // NB This function will be called from within a swingworker, 
      //    so not necessary to use a swingworker here (it is adviced to use a swingworker when file reading/writing)

   
      int number_read_commas                                 = 0;
      int aantal_intelezen_files                             = 0;
      int int_record_minuten                                 = 0;
      int pos                                                = 0;
      String sensor_data_file_name                           = "";
      String volledig_path_sensor_data                       = "";
      String record_parameter                                = "";
      String record_datum_tijd                               = "";
      String record_minuten                                  = "";
      boolean doorgaan_in_record                             = true;
   
   
      sdf3_local = new SimpleDateFormat("yyyyMMddHH");                                  // HH hour in day (0-23) note there is also hh (then also with am, pm)
      sdf3_local.setTimeZone(TimeZone.getTimeZone("UTC"));
   
   
      // initialisation
      for (int i = 0; i < AANTAL_AWS_MEASUREMENTS; i++)      // rows
      {
         for (int c = 0; c < AANTAL_AWS_PARAMETERS; c++)     // columns (date, time, lat etc.)
         {
            AWS_array[i][c] = "";
         }
      }   
   
      // initialisation
      //aantal_intelezen_files = 6;                            // max 6 hours back (= max duty watch on bridge
      aantal_intelezen_files = AANTAL_AWS_MEASUREMENTS -1;     // -1 !! otherwise outside array boundary)

      // message to console
      //SimpleDateFormat sdf2_local;
      //sdf2_local = new SimpleDateFormat("MMMM dd, yyyy");                            // e.g "MMMM dd, yyyy" -> februari 27, 2010
      //System.out.println("--- " + sdf2_local.format(new Date()) + " UTC " + "reading AWS sensor data from file for latest AWS measuremets");
      System.out.println("--- " + "reading AWS sensor data from files for latest AWS measuremets");
/*   
      for (int i = aantal_intelezen_files; i >= 0; i--)          // so oldest file read first
      {
         cal_file_datum_tijd = new GregorianCalendar();
         cal_file_datum_tijd.add(Calendar.HOUR_OF_DAY, -i);

         String sensor_data_file_naam_datum_tijd_deel = sdf3_local.format(cal_file_datum_tijd.getTime()); // -> eg 2013020308
         sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";
         
         //System.out.println("+++ " + "sensor_data_file_name = " +  sensor_data_file_name);
         
         
   
         // first check if there is a sensor data file present (and not empty)
         volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         // bv volledig_path_sensor_data = C:\Users\Martin\Documents\NetBeansProjects\RS232_AWS_1\data\sensor_data.aws

         File sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try (BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data)))  // try-with-resources: will always automatically closed the open files
            {
               String record                    = null;
               
               while ((record = in.readLine()) != null)                                 // null means that the end of the stream has been reached 
               {
                  //System.out.println("+++ ingelezen record: " + record);
               
                  // initialisation
                  doorgaan_in_record               = true;
                  
                  // Check the number of comma's in the record that must exactely match the fixed expected number (TOTAL_NUMBER_RECORD_COMMAS)
                  //
                  number_read_commas = 0;
                  pos = -1;
                        
                  do
                  {
                     pos = record.indexOf(",", pos + 1);
                     if (pos > 0)     // "," found
                     {
                        number_read_commas++;
                     }
                  } while (pos > 0); 
                        
                  if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS)
                  {
                     doorgaan_in_record = false;
                  }                
                  
                  if ( (doorgaan_in_record == true) && (record.length() > 18) && (record.substring(0, 6).equals("$PEUMB")) )   // NB > 18 is a little bit arbitrary number (YYYYMMDDHHmm + $PEUMB = 18 chars)
                  {
                     pos = record.lastIndexOf(",");                                       // searching for LAST appearance of ","
                     if ((pos > 0) && (pos + 1 + 12 == record.length()))     // so "," found and YYYYMMDDHHmm follows
                     {
                        record_datum_tijd             = record.substring(pos + 1, pos + 1 + 10);  // YYYYMMDDHH has length 10
                     
                        if (record_datum_tijd.equals(sensor_data_file_naam_datum_tijd_deel))
                        {
                           record_minuten = record.substring(pos + 1 + 10, pos + 1 + 12);         // mm from YYYYMMDDHHmm
                           
                           // initialisation
                           int_record_minuten = 9999;

                           try
                           {
                              int_record_minuten = Integer.parseInt(record_minuten.trim());
                           }
                           catch (NumberFormatException e) 
                           {
                              doorgaan_in_record = false;
                           }

                           // only parameter value (eg pressure) every 5 minutes (00, 05, 10, 15 etc minutes) !
                           //if (!(int_record_minuten >= 0 && int_record_minuten <= 59 && (int_record_minuten % 5 == 0)))
                           if (!(int_record_minuten == 0))
                           {
                              doorgaan_in_record = false;
                           }      
                        } // if (record_datum_tijd.equals(sensor_data_file_naam_datum_tijd_deel))
                        else
                        {
                           doorgaan_in_record = false;
                        }
                     } // if (pos > 0) -> so "," NOT found
                     else
                     {
                        doorgaan_in_record = false;
                     }
                        
                     
                     // If the by TurboWin+ added date and time are OK and number of commas in the record is OK then continue
                     //
                     if (doorgaan_in_record == true)
                     {   
                        // initialisation
                        number_read_commas = 0;
                        pos = -1;
                           
                        do
                        {
                           pos = record.indexOf(",", pos + 1);    // searching "," from position "pos + 1"
                           if (pos > 0)     // "," found
                           {
                              number_read_commas++;
                              
                              // Date
                              //
                              if (number_read_commas == main.DATE_COMMA_NUMBER) 
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //sensor_waarde_array[(aantal_intelezen_files - i) * 12 + int_minuten_5] = record_parameter;           // so every 5 minutes storage (=array position)
                                       //datum_tijd_array[(aantal_intelezen_files - i) * 12 + int_minuten_5] = record_datum_tijd_met_minuten; // so every 5 minutes storage (=array position)
                                    
                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][date_index] = record_parameter;          // so every 5 minutes
                                       
                                       //System.out.println("+++ [DATE]sensor_data_file_name = " +  sensor_data_file_name);
                                       //System.out.println("+++ [DATE]aantal_intelezen_files: " + aantal_intelezen_files);
                                       //System.out.println("+++ [DATE]i: " + i);
                                       
                                       
                                       AWS_array[(aantal_intelezen_files - i)][date_index] = record_parameter;          // so every 1 hour
                                       //AWS_array[200][date_index] = "test";
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.DATE_COMMA_NUMBER)
                              
                              
                              // Time
                              //
                              if (number_read_commas == main.TIME_COMMA_NUMBER) 
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][time_index] = record_parameter;          // so every 5 minutes
                                       AWS_array[(aantal_intelezen_files - i)][time_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.TIME_COMMA_NUMBER)
                              
                              
                              // Latitude
                              //
                              if (number_read_commas == main.LATITUDE_COMMA_NUMBER) 
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][lat_index] = record_parameter;          // so every 5 minutes
                                       AWS_array[(aantal_intelezen_files - i)][lat_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.LATITUDE_COMMA_NUMBER) 
                              
                              
                              // Longitude
                              //
                              if (number_read_commas == main.LONGITUDE_COMMA_NUMBER) 
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][lon_index] = record_parameter;          // so every 5 minutes
                                       AWS_array[(aantal_intelezen_files - i)][lon_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.LONGITUDE_COMMA_NUMBER) 
                              
                             
                              // COG
                              //
                              if (number_read_commas == main.COG_COMMA_NUMBER) 
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       int int_minuten_5 = int_record_minuten / 5;

                                       AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][cog_index] = record_parameter;          // so every 5 minutes
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.COG_COMMA_NUMBER) 
                              
                              
                              // SOG
                              //
                              if (number_read_commas == main.SOG_COMMA_NUMBER) 
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       int int_minuten_5 = int_record_minuten / 5;

                                       AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][sog_index] = record_parameter;          // so every 5 minutes
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.SOG_COMMA_NUMBER) 
                             
                              
                              // SLP (Sea Level Pressure)
                              //
                              if (number_read_commas == main.PRESSURE_MSL_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][slp_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][slp_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.PRESSURE_MSL_COMMA_NUMBER) 
                              
                              
                              // Air temp
                              //
                              if (number_read_commas == main.AIR_TEMP_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][air_temp_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][air_temp_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.AIR_TEMP_COMMA_NUMBER)
                              
                              
                              // RH
                              //
                              if (number_read_commas == main.HUMIDITY_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][rh_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][rh_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.HUMIDITY_COMMA_NUMBER)
                              
                               
                              // SST
                              //
                              if (number_read_commas == main.SST_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][sst_index] = record_parameter;
                                       AWS_array[(aantal_intelezen_files - i)][sst_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.SST_COMMA_NUMBER)
                              
                               
                              // True Wind Speed (at sensor height)
                              //
                              if (number_read_commas == main.TRUE_WIND_SPEED_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][true_wind_speed_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][true_wind_speed_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.TRUE_WIND_SPEED_COMMA_NUMBER)
                              
                               
                              // True Wind Dir (at sensor height)
                              //
                              if (number_read_commas == main.TRUE_WIND_DIR_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][true_wind_dir_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][true_wind_dir_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.TRUE_WIND_DIR_COMMA_NUMBER)
                              
                              
                              // True Wind Gust (gust speed) (at sensor height)
                              //
                              if (number_read_commas == main.TRUE_WIND_GUST_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][true_wind_gust_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][true_wind_gust_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.TRUE_WIND_GUST_COMMA_NUMBER)
                              
                           } // if (pos > 0)
                        } while (pos > 0); 
                           
                     } // if (doorgaan_in_record)
                  } // if ( (record.length() > 18) etc.
               } // while ((record = in.readLine()) != null)
            } // try (BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data)))
            catch (FileNotFoundException ex) 
            {
               //Logger.getLogger(mylatestmeasurements.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (IOException ex) 
            {
               //Logger.getLogger(mylatestmeasurements.class.getName()).log(Level.SEVERE, null, ex);
            }
         
         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
      
         // clear memory
         cal_file_datum_tijd = null;
      
      } // for (int i = aantal_intelezen_files; i >= 0; i--)
*/
        
        

      for (int i = aantal_intelezen_files; i >= 0; i--)          // so oldest file read first
      {
         cal_file_datum_tijd = new GregorianCalendar();
         cal_file_datum_tijd.add(Calendar.HOUR_OF_DAY, -i);

         String sensor_data_file_naam_datum_tijd_deel = sdf3_local.format(cal_file_datum_tijd.getTime()); // -> eg 2013020308
         sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";
         
         //System.out.println("+++ " + "sensor_data_file_name = " +  sensor_data_file_name);
   
         // first check if there is a sensor data file present (and not empty)
         volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         // bv volledig_path_sensor_data = C:\Users\Martin\Documents\NetBeansProjects\RS232_AWS_1\data\sensor_data.aws

         File sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try (BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data)))  // try-with-resources: will always automatically closed the open files
            {
               String record                    = null;
               
               while ((record = in.readLine()) != null)                                 // null means that the end of the stream has been reached 
               {
                  //System.out.println("+++ ingelezen record: " + record);
               
                  // initialisation
                  doorgaan_in_record               = true;
                  
                  // Check the number of comma's in the record that must exactely match the fixed expected number (TOTAL_NUMBER_RECORD_COMMAS)
                  //
                  number_read_commas = 0;
                  pos = -1;
                        
                  do
                  {
                     pos = record.indexOf(",", pos + 1);
                     if (pos > 0)     // "," found
                     {
                        number_read_commas++;
                     }
                  } while (pos > 0); 
                        
                  if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS)
                  {
                     doorgaan_in_record = false;
                  }                
                  
                  if ( (doorgaan_in_record == true) && (record.length() > 18) && (record.substring(0, 6).equals("$PEUMB")) )   // NB > 18 is a little bit arbitrary number (YYYYMMDDHHmm + $PEUMB = 18 chars)
                  {
                     pos = record.lastIndexOf(",");                                       // searching for LAST appearance of ","
                     if ((pos > 0) && (pos + 1 + 12 == record.length()))                  // so "," found and YYYYMMDDHHmm follows
                     {
                        record_datum_tijd             = record.substring(pos + 1, pos + 1 + 10);  // YYYYMMDDHH has length 10
                     
                        if (record_datum_tijd.equals(sensor_data_file_naam_datum_tijd_deel))
                        {
                           //$PEUMB,20190201,150000,45.960,-2.124,128.3,10.8,131.6,997.4,1000.5,6.0,2,20.5,53.0,9.3,3.1,128.3,4.2,350.3,7.8,358.3,23.1,28.3,-10,,,,201902011500
                           
                           // initialisation
                           doorgaan_in_record = false;
                           pos = -1;
                           
                           if (record.length() > pos + 1 + 12)                                  // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                           {   
                              pos = record.indexOf(",", pos + 1);                              // searching first "," from position "pos + 1"
                              if (pos > 0)        // first "," found
                              {
                                 pos = record.indexOf(",", pos + 1);                           // searching second "," from position "pos + 1"
                                 if (pos > 0)     // second "," found
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    String record_time = record.substring(pos + 1, pos2);      // eg 150000 [HHMMSS]
                                    String record_minutes = record_time.substring(2, 4);       // eg 00 [MM]
                                    
                                    if (record_minutes.equals("00"))
                                    {
                                      doorgaan_in_record = true; 
                                    }
                                 } // if (pos > 0)
                              } // if (pos > 0) 
                           } // if (record.length() > pos + 1 + 12) 
                              
                              
                              
                           
                        //   record_minuten = record.substring(pos + 1 + 10, pos + 1 + 12);         // mm from YYYYMMDDHHmm
                        //   
                        //   // initialisation
                        //   int_record_minuten = 9999;
                        //
                        //   try
                        //   {
                        //      int_record_minuten = Integer.parseInt(record_minuten.trim());
                        //   }
                        //   catch (NumberFormatException e) 
                        //   {
                        //      doorgaan_in_record = false;
                        //   }
                        //
                        //   // only parameter value (eg pressure) every 5 minutes (00, 05, 10, 15 etc minutes) !
                        //   //if (!(int_record_minuten >= 0 && int_record_minuten <= 59 && (int_record_minuten % 5 == 0)))
                        //   if (!(int_record_minuten == 0))
                        //   {
                        //      doorgaan_in_record = false;
                        } // if (record_datum_tijd.equals(sensor_data_file_naam_datum_tijd_deel))
                        else
                        {
                           doorgaan_in_record = false;
                        }
                     } // if (pos > 0) -> so "," NOT found
                     else
                     {
                        doorgaan_in_record = false;
                     }
                        
                     
                     // If the by TurboWin+ added date and time are OK and number of commas in the record is OK then continue
                     //
                     if (doorgaan_in_record == true)
                     {   
                        // initialisation
                        number_read_commas = 0;
                        pos = -1;
                           
                        do
                        {
                           pos = record.indexOf(",", pos + 1);    // searching "," from position "pos + 1"
                       
                           if (pos > 0)     // "," found
                           {
                              number_read_commas++;
                              
                              // Date
                              //
                              if (number_read_commas == main.DATE_COMMA_NUMBER) 
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //sensor_waarde_array[(aantal_intelezen_files - i) * 12 + int_minuten_5] = record_parameter;           // so every 5 minutes storage (=array position)
                                       //datum_tijd_array[(aantal_intelezen_files - i) * 12 + int_minuten_5] = record_datum_tijd_met_minuten; // so every 5 minutes storage (=array position)
                                    
                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][date_index] = record_parameter;          // so every 5 minutes
                                       
                                       //System.out.println("+++ [DATE]sensor_data_file_name = " +  sensor_data_file_name);
                                       //System.out.println("+++ [DATE]aantal_intelezen_files: " + aantal_intelezen_files);
                                       //System.out.println("+++ [DATE]i: " + i);
                                       
                                       
                                       AWS_array[(aantal_intelezen_files - i)][date_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.DATE_COMMA_NUMBER)
                              
                              
                              // Time
                              //
                              if (number_read_commas == main.TIME_COMMA_NUMBER) 
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][time_index] = record_parameter;          // so every 5 minutes
                                       AWS_array[(aantal_intelezen_files - i)][time_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.TIME_COMMA_NUMBER)
                              
                              
                              // Latitude
                              //
                              if (number_read_commas == main.LATITUDE_COMMA_NUMBER) 
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][lat_index] = record_parameter;          // so every 5 minutes
                                       AWS_array[(aantal_intelezen_files - i)][lat_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.LATITUDE_COMMA_NUMBER) 
                              
                              
                              // Longitude
                              //
                              if (number_read_commas == main.LONGITUDE_COMMA_NUMBER) 
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][lon_index] = record_parameter;          // so every 5 minutes
                                       AWS_array[(aantal_intelezen_files - i)][lon_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.LONGITUDE_COMMA_NUMBER) 
                              
                              
                              // SLP (Sea Level Pressure)
                              //
                              if (number_read_commas == main.PRESSURE_MSL_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][slp_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][slp_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.PRESSURE_MSL_COMMA_NUMBER) 
                              
                              
                              // Air temp
                              //
                              if (number_read_commas == main.AIR_TEMP_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][air_temp_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][air_temp_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.AIR_TEMP_COMMA_NUMBER)
                              
                              
                              // RH
                              //
                              if (number_read_commas == main.HUMIDITY_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][rh_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][rh_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.HUMIDITY_COMMA_NUMBER)
                              
                               
                              // SST
                              //
                              if (number_read_commas == main.SST_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][sst_index] = record_parameter;
                                       AWS_array[(aantal_intelezen_files - i)][sst_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.SST_COMMA_NUMBER)
                              
                               
                              // True Wind Speed (at sensor height)
                              //
                              if (number_read_commas == main.TRUE_WIND_SPEED_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    //System.out.println("+++ ingelezen record bij number_read_commas == main.TRUE_WIND_SPEED_COMMA_NUMBER: " + record);
                                    
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][true_wind_speed_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][true_wind_speed_index] = record_parameter;          // so every 1 hour
                                       
                                       //System.out.println("+++ record_parameter (wind speed): " + record_parameter);
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.TRUE_WIND_SPEED_COMMA_NUMBER)
                              
                               
                              // True Wind Dir (at sensor height)
                              //
                              if (number_read_commas == main.TRUE_WIND_DIR_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][true_wind_dir_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][true_wind_dir_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.TRUE_WIND_DIR_COMMA_NUMBER)
                              
                              
                              // True Wind Gust (gust speed) (at sensor height)
                              //
                              if (number_read_commas == main.TRUE_WIND_GUST_COMMA_NUMBER) // eg air temp value always after the 12th comma
                              {
                                 if (record.length() > pos + 1 + 12)    // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                                 {
                                    int pos2 = record.indexOf(",", pos + 1);
                                    if (pos2 - pos >= 2)            // between conmmas at least 1 char
                                    {
                                       record_parameter = record.substring(pos + 1, pos2);
                                       //int int_minuten_5 = int_record_minuten / 5;

                                       //AWS_array[(aantal_intelezen_files - i) * 12 + int_minuten_5][true_wind_gust_index] = record_parameter; 
                                       AWS_array[(aantal_intelezen_files - i)][true_wind_gust_index] = record_parameter;          // so every 1 hour
                                       
                                    } // if (pos2 - pos >= 2)
                                 } // if (record.length() > number_read_commas + 12)
                              } // if (number_read_commas == main.TRUE_WIND_GUST_COMMA_NUMBER)
                              
                           } // if (pos > 0)
                        } while (pos > 0); 
                           
                     } // if (doorgaan_in_record)
                  } // if ( (record.length() > 18) etc.
               } // while ((record = in.readLine()) != null)
            } // try (BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data)))
            catch (FileNotFoundException ex) 
            {
               //Logger.getLogger(mylatestmeasurements.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (IOException ex) 
            {
               //Logger.getLogger(mylatestmeasurements.class.getName()).log(Level.SEVERE, null, ex);
            }
         
         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
      
         // clear memory
         cal_file_datum_tijd = null;
      
      } // for (int i = aantal_intelezen_files; i >= 0; i--)
   }
   
   
   
   
   
   
   /**
    * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form
    * Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jScrollPane1 = new javax.swing.JScrollPane();
      jTable1 = new javax.swing.JTable();
      jPanel2 = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      jLabel2 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      jPanel1 = new javax.swing.JPanel();
      jButton1 = new javax.swing.JButton();
      jButton2 = new javax.swing.JButton();
      jButton3 = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle("Latest AWS measurements");
      setMinimumSize(new java.awt.Dimension(1000, 700));

      jTable1.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null}
         },
         new String [] {
            "Title 1", "Title 2", "Title 3", "null", "null", "null", "null", "null", "null", "null", "null"
         }
      ) {
         boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false, false, false, false, false
         };

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
         }
      });
      jScrollPane1.setViewportView(jTable1);

      getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

      jLabel1.setText("Date/Time select:");

      jLabel2.setText("no data");

      jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel3.setText("--- all measurements at sensor height, SLP excepted ---");

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addGroup(jPanel2Layout.createSequentialGroup()
                  .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(0, 753, Short.MAX_VALUE)))
            .addContainerGap())
      );
      jPanel2Layout.setVerticalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
            .addGap(15, 15, 15)
            .addComponent(jLabel3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel1)
               .addComponent(jLabel2))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

      jButton1.setText("Cancel");
      jButton1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Cancel_button_actionPerformed(evt);
         }
      });

      jButton2.setText("Refresh");
      jButton2.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Refresh_button_actionPerformed(evt);
         }
      });

      jButton3.setText("Export");
      jButton3.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Export_button_actionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap(416, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jButton2)
            .addGap(18, 18, 18)
            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(417, Short.MAX_VALUE))
      );

      jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jButton3});

      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(15, 15, 15)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jButton2)
               .addComponent(jButton1)
               .addComponent(jButton3))
            .addGap(15, 15, 15))
      );

      getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

      pack();
   }// </editor-fold>//GEN-END:initComponents

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/   
   private void Cancel_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_button_actionPerformed
      // TODO add your handling code here:
      
      setVisible(false);
      dispose();
   }//GEN-LAST:event_Cancel_button_actionPerformed

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/   
   private void Refresh_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Refresh_button_actionPerformed
      // TODO add your handling code here:
      
      import_AWS_measurements();
   }//GEN-LAST:event_Refresh_button_actionPerformed

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/   
   private void Export_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Export_button_actionPerformed
      // TODO add your handling code here:
      
      export_latest_AWS_measurements();
   }//GEN-LAST:event_Export_button_actionPerformed

   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/   
   private void export_latest_AWS_measurements()  
   {
      boolean doorgaan = true;
      final String latest_AWS_measurements_export_file;
      
      String export_dir = null;    // NB Passing in a null string causes the file chooser to point to the user's default directory (It is typically the "My Documents" folder on Windows)
      
      
      JFileChooser chooser = new JFileChooser(export_dir);
      
      //jsonChooser chooser = new jsonChooser(export_dir);
      chooser.setDialogTitle("Export latest AWS measurements");
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);                        // now the user can only select files (directories and files are still visible)
  
      //chooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON", "json"));
      
      
      //chooser.addChoosableFileFilter(new jsonfilefilter());
      
      FileNameExtensionFilter filter = new FileNameExtensionFilter ("JSON file", "json");
      chooser.setFileFilter((javax.swing.filechooser.FileFilter) filter);
      
      
      int result = chooser.showSaveDialog(mylatestmeasurements.this);

      if (result == JFileChooser.APPROVE_OPTION)
      {
         latest_AWS_measurements_export_file = chooser.getSelectedFile().getPath();                       // getSelectedFile() -> in this case returns not a file but a directory !

         // if the selected file doesn't ends with ".json" add the ".json" extension
         if (!latest_AWS_measurements_export_file.toLowerCase().endsWith(".json"))
         {
            JOptionPane.showMessageDialog(null, "inserted file name must ends with \".json\"", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
            doorgaan = false;
         }
         
         
         if (doorgaan == true)
         {
            if (chooser.getSelectedFile().exists())
            {
               int response = JOptionPane.showConfirmDialog (null, "Overwrite existing file?", main.APPLICATION_NAME + " confirm overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
               doorgaan = response != JOptionPane.CANCEL_OPTION;
            }
         } // if (doorgaan == true)
         

         if (doorgaan == true)
         {
            new SwingWorker<String, Void>()
            {
               @Override
               protected String doInBackground() throws Exception
               {
                  String export_ok = write_export_file(latest_AWS_measurements_export_file);
                  
                  
                  return export_ok;
                  
               } // protected Void doInBackground() throws Exception
               
               @Override
               protected void done()
               {
                  String result_export_ok = null;
                  try 
                  {
                     result_export_ok = get();
                  } 
                  catch (ExecutionException | InterruptedException ex) 
                  {
                      result_export_ok = "Error writing export file (" + ex + ")";
                  }
                     
                  
                  if (result_export_ok.contains("OK") != true)
                  {
                     // show error message
                     JOptionPane.showMessageDialog(null, result_export_ok,  main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                  }
                  else
                  {
                     // show the succesfully exported message
                     JOptionPane.showMessageDialog(null, result_export_ok, main.APPLICATION_NAME + " info", JOptionPane.INFORMATION_MESSAGE);
                  }
               } // protected void done()
               
            }.execute(); // new SwingWorker<Void, Void>()
         } // if (doorgaan == true)
      } // if (result == JFileChooser.APPROVE_OPTION
      else // Cancel button
      {

      }
      
   }
   

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/    
   private String write_export_file(final String latest_AWS_measurements_export_file) 
   {
      // called from: export_latest_AWS_measurements() [within swingworker]
      
      String[][] export_array        = new String[AANTAL_AWS_MEASUREMENTS][AANTAL_AWS_PARAMETERS];
      String export_ok               = "OK";
      
      
      // initialisation
      for (int r = 0; r < AANTAL_AWS_MEASUREMENTS; r++)
      {
         for (int c = 0; c < AANTAL_AWS_PARAMETERS; c++)
         {
            export_array[r][c] = "";
         }
      } // for (int r = 0; r < AANTAL_AWS_MEASUREMENTS; r++)
      
      
      // collect data from all table cells
      for (int r = 0; r < AANTAL_AWS_MEASUREMENTS; r++)
      {
         for (int c = 0; c < AANTAL_AWS_PARAMETERS; c++)
         {
            export_array[r][c] = (String)jTable1.getValueAt(r, c);
         }
      } // for (int r = 0; r < AANTAL_AWS_MEASUREMENTS; r++)
      
      
      // write to the user defined export file
      try (BufferedWriter out = new BufferedWriter(new FileWriter(latest_AWS_measurements_export_file)))   //try-with-resources
      {
         // write the json strings
         out.write("{");                           out.newLine(); 
         out.write("  \"ship name\": ");           out.write("\"" + main.ship_name + "\"");                              out.write(",");   out.newLine();
         out.write("  \"date/time export\": ");    out.write("\"" + main.sdf_tsl_2.format(new Date()) + " UTC" + "\"");  out.write(",");   out.newLine();                                         //  new Date() -> always in UTC
         out.write("  \"measurements\": ");        out.newLine();  
         out.write("   [");                        out.newLine(); 
         
         for (int r = 0; r < AANTAL_AWS_MEASUREMENTS; r++)
         {
            if ( (!export_array[r][date_index].equals("")) && (!export_array[r][time_index].equals("")) )
            {
               out.write("     {");  
               
               // e.g.: {"Date [UTC]": "2019-04-14", "Time [UTC]": "05:00", "Lat [°]": "05:00", "Lon [°]": "2019-04-14", "SLP [hPa]": "05:00", "Air temp [°C]": "05:00", "RH [%]": "2019-04-14", "SST [°C]": "05:00", "wind speed true [m/s]": "05:00", "wind dir true [°]": "05:00", "wind gust true [m/s]": "05:00"},

               out.write("\"" + jTable1.getColumnModel().getColumn(0).getHeaderValue() + "\": ");   out.write("\"" + export_array[r][date_index] + "\"");              out.write(", "); 
               out.write("\"" + jTable1.getColumnModel().getColumn(1).getHeaderValue() + "\": ");   out.write("\"" + export_array[r][time_index] + "\"");              out.write(", "); 
               out.write("\"" + jTable1.getColumnModel().getColumn(2).getHeaderValue() + "\": ");   out.write("\"" + export_array[r][lat_index] + "\"");               out.write(", ");     
               out.write("\"" + jTable1.getColumnModel().getColumn(3).getHeaderValue() + "\": ");   out.write("\"" + export_array[r][lon_index] + "\"");               out.write(", "); 
               out.write("\"" + jTable1.getColumnModel().getColumn(4).getHeaderValue() + "\": ");   out.write("\"" + export_array[r][slp_index] + "\"");               out.write(", "); 
               out.write("\"" + jTable1.getColumnModel().getColumn(5).getHeaderValue() + "\": ");   out.write("\"" + export_array[r][air_temp_index] + "\"");          out.write(", ");     
               out.write("\"" + jTable1.getColumnModel().getColumn(6).getHeaderValue() + "\": ");   out.write("\"" + export_array[r][rh_index] + "\"");                out.write(", "); 
               out.write("\"" + jTable1.getColumnModel().getColumn(7).getHeaderValue() + "\": ");   out.write("\"" + export_array[r][sst_index] + "\"");               out.write(", "); 
               out.write("\"" + jTable1.getColumnModel().getColumn(8).getHeaderValue() + "\": ");   out.write("\"" + export_array[r][true_wind_speed_index] + "\"");   out.write(", ");     
               out.write("\"" + jTable1.getColumnModel().getColumn(9).getHeaderValue() + "\": ");   out.write("\"" + export_array[r][true_wind_dir_index] + "\"");     out.write(", "); 
               out.write("\"" + jTable1.getColumnModel().getColumn(10).getHeaderValue() + "\": ");  out.write("\"" + export_array[r][true_wind_gust_index] + "\"");      
               
               if (r == AANTAL_AWS_MEASUREMENTS -1) // this is the last record
               {   
                  // this is the last record
                  out.write("}");                 out.newLine();    
               }
               else if ((export_array[r + 1][date_index].equals("")) || (export_array[r + 1][time_index].equals("")))
               {
                  // next record will be invalid/blank -> due to the break in the for loop: now this will be the last record also! (so not a "," fter the "}")
                  out.write("}");                 out.newLine();
               }
               else // more valid records will follow
               {
                  out.write("},");                out.newLine();     
               }
               
            } // if ( (!AWS_array[r][date_index].equals("")) && (!AWS_array[r][time_index].equals("")) )
            else
            {
               break;   // quit the for loop
            }
               
         } // for (int r = 0; r < AANTAL_AWS_MEASUREMENTS; r++)
         
         out.write("   ]");                       out.newLine();
         out.write("}"); out.newLine(); 
                             
         // user feedback string
         export_ok = "OK, latest AWS measurements written to: " + latest_AWS_measurements_export_file;        
      }
      catch(Exception e) 
      {
         export_ok = "Unable to write to: " + latest_AWS_measurements_export_file + " (" + e + ")";
      }
                  
                  
      return export_ok;
   }
   
   
   
   
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
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
         java.util.logging.Logger.getLogger(mylatestmeasurements.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      //</editor-fold>
      
      //</editor-fold>

      /* Create and display the form */
      java.awt.EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            new mylatestmeasurements().setVisible(true);
         }
      });
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton jButton1;
   private javax.swing.JButton jButton2;
   private javax.swing.JButton jButton3;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JTable jTable1;
   // End of variables declaration//GEN-END:variables

   public final static int AANTAL_AWS_MEASUREMENTS                          = 672; // 28 dagen * 24 hr = 672      192;   // 8 * 24 = 192      //84;   // 6 hours (every 5 minutes) e.g. from 9.00 UTC - 15.55 UTC = 6 * 12 + 12 = 84
   public final static int AANTAL_AWS_PARAMETERS                            = 11;    // date, time, lat, lon, SLP, air temp etc. 
   
   public final static String[][] AWS_array                                 = new String[AANTAL_AWS_MEASUREMENTS][AANTAL_AWS_PARAMETERS];// NB default Sring Array values: null
   public final static int date_index                                       = 0;
   public final static int time_index                                       = 1;
   public final static int lat_index                                        = 2;
   public final static int lon_index                                        = 3;
   //private final int cog_index                                       = 4;
   //private final int sog_index                                       = 5;
   public final static int slp_index                                        = 4;
   public final static int air_temp_index                                   = 5; 
   private final static int rh_index                                        = 6;
   public final static int sst_index                                        = 7;
   public final static int true_wind_speed_index                            = 8;
   public final static int true_wind_dir_index                              = 9;
   private final static int true_wind_gust_index                            = 10;
   
   private static GregorianCalendar cal_file_datum_tijd;
   private static SimpleDateFormat sdf3_local;
   
}
