/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbowin;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;


/**
 *
 * @author marti
 */
public class OSM 
{
   // NB https://stackoverflow.com/questions/1441984/how-can-i-know-whether-an-instance-of-a-class-already-exists-in-memory
   // NB https://stackoverflow.com/questions/43608919/html-offline-map-with-local-tiles-via-leaflet
   
    /**
    * This class attribute will be the only "instance" of this class
    * It is private so none can reach it directly. 
    * And is "static" so it does not need "instances" 
    */        
   //private static OSM instance;

   /** 
     * Constructor make private, to enforce the non-instantiation of the 
     * class. So an invocation to: new OSM() outside of this class
     * won't be allowed.
     */
   //private OSM(){} // avoid instantiation.

   /**
    * This class method returns the "only" instance available for this class
    * If the instance is still null, it gets instantiated. 
    * Being a class method you can call it from anywhere and it will 
    * always return the same instance.
    */
   //public static OSM getInstance() 
   //{
   //   if (instance == null) 
   //   {
   //      instance = new OSM();
   //   }
   //   return instance;
   //}
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public OSM()
   {

   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public boolean OSM_control_center()
   {
      // called from: Maps_OSM() [main.java]
      
      int return_status = 0;
      boolean doorgaan = true;


      //
      /////////////// for Maps, if necessary copy all OSM related files from internal jar to destination ///////////
      //
      return_status = check_OSM_module();
      if (return_status != 0)                        // leaflet.js not found so now try to create dires and copy all OSM module files
      {
         return_status = copy_OSM_module();
         if (return_status != 0)                     // now if return_status = 0 complete OSM module was copied sucessfully from jar to "logs_dir"
         {
            doorgaan = false;
         }
      }  
      
      
      return doorgaan;

   } // public void OSM_control_center()
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private int check_OSM_module()
   {
      // called from OSM_control_center()[OSM.java]
      
      int OSM_module_status = 0;          // NB 0 = leaflet.js is present; 1 = leaflet.js is missing
      
   
      if (main.logs_dir.trim().equals("") == false && main.logs_dir.trim().length() >= 2)
      {
         // NB see function copy_OSM_module() for a message if "logs_dir" do not exist
         
         // NB e.g. leaflet_js_File = "C:\Program Files (x86)\TurboWin+\logs\OSM\leaflet.js"
         final File leaflet_js_File = new File(main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + LEAFLET_JS);
         if (leaflet_js_File.exists() == false)
         {   
            OSM_module_status = 1;
            main.log_turbowin_system_message("[OSM] did not find: " + main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + LEAFLET_JS);
         }   
         else
         {
            OSM_module_status = 0;
            main.log_turbowin_system_message("[OSM] found: " + LEAFLET_JS);
         }
      } // if (main.logs_dir.trim().equals("") == false && main.logs_dir.trim().length() >= 2)
      
      
      return OSM_module_status;   
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private int copy_OSM_module()
   {
      // called from OSM_control_center()[OSM.java]
      
      int OSM_module_status;
      boolean doorgaan = true;
      String info = "";
      String resource = "";
      String output = "";
     
      
      // NB still in a swingworker! (Maps_OSM()[main.java] -> osm.OSM_control_center()). So not necessary to use a swingworker here for file copying etc.
      
      // NB in offline_mode:  logs_dir = user_dir + java.io.File.separator + OFFLINE_LOGS_DIR;
      // NB in logs_dir always 'user_dir' already present (so a complete path)
      
      
      // initialisation
      OSM_module_status = 0;           // NB 0 = OK; 1 = error
      
      // check if logs folder exists (because this will be the root folder of all the format_101 dirs and files)
      if (main.logs_dir.trim().equals("") == true || main.logs_dir.trim().length() < 2)
      {
         doorgaan = false;
         info = "logs folder unknown, select: Maintenance -> Log files settings and retry";
         JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
      }
      
      
      // check if leaflet.js exists, if not create all OSM dirs (again) and copy all the necessary OSM fiies
      if (doorgaan == true)
      {
         // NB e.g. leaflet_js_File = "C:\Program Files (x86)\TurboWin+\logs\OSM\leaflet_js"
         final File leaflet_js_File = new File(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + LEAFLET_JS);
         if (leaflet_js_File.exists() == false)
         {
            // create OSM (sub)dirs and copy all the necessary files from the turbowin_jws.jar resource to the newly created dirs
            //
            //info = "--- start copying complete format 101 module from jar to: " + main.logs_dir;
            //System.out.println(info);   
            main.log_turbowin_system_message("[OSM] start copying complete format OSM module from jar to: " + main.logs_dir);
            
            // message (because it could be that virus scanner is complaining about compression exe (teste_hc_TW.exe))
            //String info_message = "format 101 module will be copied from source to destination\n note: in case you get a warning of your virus scanner, " + COMPRESSION_EXE + " and " + DECOMPRESSION_EXE + " are essential parts of " + main.APPLICATION_NAME + " !";
            //JOptionPane.showMessageDialog(null, info_message, main.APPLICATION_NAME + " info", JOptionPane.WARNING_MESSAGE);
            // note veplaatst naar obsformat.java (i.v.m messagebox niet goed in APR mode)
            
            // create dir "OSM" (e.g. "C:\Program Files (x86)\TurboWin+\logs\OSM")
            //
            String OSM_dir = main.logs_dir + java.io.File.separator + OSM_ROOT_DIR;
            final File dir_OSM = new File(OSM_dir);   
            dir_OSM.mkdir();
                    
            // create sub dir "OSM\OSMPublicTransport" (e.g. "C:\Program Files (x86)\TurboWin+\logs\OSM\OSMPublicTransport")
            //
            String OSMPublicTranspor_dir = OSM_dir + java.io.File.separator + OSM_PUBLIC_TRANSPORT_DIR;
            final File dir_OSMPublicTranspor = new File(OSMPublicTranspor_dir);  
            dir_OSMPublicTranspor.mkdir();
               
            
            //
            // create sub sub dir "OSM\OSMPublicTransport\2" (e.g. "C:\Program Files (x86)\TurboWin+\logs\OSM\OSMPublicTransport\2")  
            //
            String OSMPublicTransport_2_dir = OSMPublicTranspor_dir + java.io.File.separator + "2";
            final File dir_OSMPublicTransport_2 = new File(OSMPublicTransport_2_dir);            
            dir_OSMPublicTransport_2.mkdir();
            
            // create sub dir's "OSM\OSMPublicTransport\2\0" etc. (e.g. "C:\Program Files (x86)\TurboWin+\logs\OSM\OSMPublicTransport\2\0")  
            //
            for (int i = 0; i <= 3; i++)   // sub-sub-sub dir's 0,1,2,3
            {
               String OSMPublicTransport_2_sub_dir = OSMPublicTransport_2_dir + java.io.File.separator + String.valueOf(i);
               final File dir_OSMPublicTransport_2_sub = new File(OSMPublicTransport_2_sub_dir);            
               dir_OSMPublicTransport_2_sub.mkdir();  
               
               // copying files to "OSM\OSMPublicTransport\2\0" etc. (0.png, 1.png, 2.png)
               //
               for (int k = 0; k <= 3; k++)   // 0.png, 1.png, 2.png, 3.png
               {
                  resource = OSM_ROOT_DIR + "/" + OSM_PUBLIC_TRANSPORT_DIR + "/" + "2" + "/" + String.valueOf(i) + "/" + String.valueOf(k) + ".png";
                  output = OSMPublicTransport_2_sub_dir + java.io.File.separator + String.valueOf(k) + ".png";
                  if (getClass().getResourceAsStream(resource) != null)  
                  {
                     try (InputStream is = getClass().getResourceAsStream(resource);  // NB in jar file: java.io.File.separator DO NOT WORK UNDER WINDOWS, MUST BE "/" !!!!!
                          OutputStream os = new FileOutputStream(output)   
                         ) 
                     {
                        // NB try-with-resource; resources (is and os) will be closed automatically when execution leaves the try block.
               
                        int readBytes;
                        byte[] buffer = new byte[4096];

                        while ((readBytes = is.read(buffer)) > 0) 
                        {
                           os.write(buffer, 0, readBytes);
                        }
                    
                        //info = "--- success when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                        //System.out.println(info);
                        main.log_turbowin_system_message("[OSM] success when copying " + resource + " from jar to: " + OSMPublicTransport_2_sub_dir);
                     }
                     catch (IOException ex) 
                     {
                        //info = "+++ error when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                        //System.out.println(info);
                        main.log_turbowin_system_message("[OSM] error when copying " + resource + " from jar to: " + OSMPublicTransport_2_sub_dir);
                        OSM_module_status = 1;
                     }  
                  } // if (getClass().getResourceAsStream(resource) != null)  
                  else
                  {
                     main.log_turbowin_system_message("[OSM] error when copying (resource file does not exist) " + resource + " from jar to: " + OSMPublicTransport_2_sub_dir);
                     OSM_module_status = 1;
                  }
               } // for (int k = 0; k <= 2; k++)
            } // for (int i = 0; i <= 3; i++)
            
            
           
            //
            // create sub sub dir "OSM\OSMPublicTransport\3" (e.g. "C:\Program Files (x86)\TurboWin+\logs\OSM\OSMPublicTransport\3")  
            //
            String OSMPublicTransport_3_dir = OSMPublicTranspor_dir + java.io.File.separator + "3";
            final File dir_OSMPublicTransport_3 = new File(OSMPublicTransport_3_dir);            
            dir_OSMPublicTransport_3.mkdir();
            
            // create sub dir's "OSM\OSMPublicTransport\3\0" etc. (e.g. "C:\Program Files (x86)\TurboWin+\logs\OSM\OSMPublicTransport\3\0")  
            //
            for (int i = 0; i <= 7; i++)   // sub-sub-sub dir's 0,1,2,3,4,5,6,7
            {
               String OSMPublicTransport_3_sub_dir = OSMPublicTransport_3_dir + java.io.File.separator + String.valueOf(i);
               final File dir_OSMPublicTransport_3_sub = new File(OSMPublicTransport_3_sub_dir);            
               dir_OSMPublicTransport_3_sub.mkdir();  
               
               // copying files to "OSM\OSMPublicTransport\3\0" etc. (0.png, 1.png, 2.png etc.)
               //
               for (int k = 0; k <= 6; k++)   // 0.png, 1.png, 2.png, 3.png, 4.png, 5.png, 6.png
               {
                  resource = OSM_ROOT_DIR + "/" + OSM_PUBLIC_TRANSPORT_DIR + "/" + "3" + "/" + String.valueOf(i) + "/" + String.valueOf(k) + ".png";
                  output = OSMPublicTransport_3_sub_dir + java.io.File.separator + String.valueOf(k) + ".png";
                  if (getClass().getResourceAsStream(resource) != null)  
                  {
                     try (InputStream is = getClass().getResourceAsStream(resource);  // NB in jar file: java.io.File.separator DO NOT WORK UNDER WINDOWS, MUST BE "/" !!!!!
                          OutputStream os = new FileOutputStream(output)   
                         ) 
                     {
                        // NB try-with-resource; resources (is and os) will be closed automatically when execution leaves the try block.
               
                        int readBytes;
                        byte[] buffer = new byte[4096];

                        while ((readBytes = is.read(buffer)) > 0) 
                        {
                           os.write(buffer, 0, readBytes);
                        }
                    
                        //info = "--- success when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                        //System.out.println(info);
                        main.log_turbowin_system_message("[OSM] success when copying " + resource + " from jar to: " + OSMPublicTransport_3_sub_dir);
                     }
                     catch (IOException ex) 
                     {
                        //info = "+++ error when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                        //System.out.println(info);
                        main.log_turbowin_system_message("[OSM] error when copying " + resource + " from jar to: " + OSMPublicTransport_3_sub_dir);
                        OSM_module_status = 1;
                     }  
                  } // if (getClass().getResourceAsStream(resource) != null)  
                  else
                  {
                     main.log_turbowin_system_message("[OSM] error when copying (resource does not exist) " + resource + " from jar to: " + OSMPublicTransport_3_sub_dir);
                     OSM_module_status = 1;
                  }
               } // for (int k = 0; k <= 5; k++)
               
            } // for (int i = 0; i <= 7; i++) 
            
            
            //
            // create sub sub dir "OSM\OSMPublicTransport\4" (e.g. "C:\Program Files (x86)\TurboWin+\logs\OSM\OSMPublicTransport\4")  
            //
            String OSMPublicTransport_4_dir = OSMPublicTranspor_dir + java.io.File.separator + "4";
            final File dir_OSMPublicTransport_4 = new File(OSMPublicTransport_4_dir);            
            dir_OSMPublicTransport_4.mkdir();
            
            // create sub dir's "OSM\OSMPublicTransport\4\0" etc. (e.g. "C:\Program Files (x86)\TurboWin+\logs\OSM\OSMPublicTransport\4\0")  
            //
            for (int i = 0; i <= 15; i++)   // sub-sub-sub dir's 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
            {
               String OSMPublicTransport_4_sub_dir = OSMPublicTransport_4_dir + java.io.File.separator + String.valueOf(i);
               final File dir_OSMPublicTransport_4_sub = new File(OSMPublicTransport_4_sub_dir);            
               dir_OSMPublicTransport_4_sub.mkdir();  
               
               // copying files to "OSM\OSMPublicTransport\4\0" etc. (0.png, 1.png, 2.png etc.)
               //
               for (int k = 0; k <= 13; k++)   // 0.png, 1.png, 2.png, 3.png, 4.png, 5.png, 6.png, 7.png, 8.png, 9.png, 10.png, 11., 12.png, 13.png
               {
                  resource = OSM_ROOT_DIR + "/" + OSM_PUBLIC_TRANSPORT_DIR + "/" + "4" + "/" + String.valueOf(i) + "/" + String.valueOf(k) + ".png";
                  output = OSMPublicTransport_4_sub_dir + java.io.File.separator + String.valueOf(k) + ".png";
                  if (getClass().getResourceAsStream(resource) != null)  
                  {
                     try (InputStream is = getClass().getResourceAsStream(resource);  // NB in jar file: java.io.File.separator DO NOT WORK UNDER WINDOWS, MUST BE "/" !!!!!
                          OutputStream os = new FileOutputStream(output)   
                         ) 
                     {
                        // NB try-with-resource; resources (is and os) will be closed automatically when execution leaves the try block.
               
                        int readBytes;
                        byte[] buffer = new byte[4096];

                        while ((readBytes = is.read(buffer)) > 0) 
                        {
                           os.write(buffer, 0, readBytes);
                        }
                    
                        //info = "--- success when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                        //System.out.println(info);
                        main.log_turbowin_system_message("[OSM] success when copying " + resource + " from jar to: " + OSMPublicTransport_4_sub_dir);
                     }
                     catch (IOException ex) 
                     {
                        //info = "+++ error when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                        //System.out.println(info);
                        main.log_turbowin_system_message("[OSM] error when copying " + resource + " from jar to: " + OSMPublicTransport_4_sub_dir);
                        OSM_module_status = 1;
                     }  
                  } // if (getClass().getResourceAsStream(resource) != null)  
                  else
                  {
                     main.log_turbowin_system_message("[OSM] error when copying (resource does not exist) " + resource + " from jar to: " + OSMPublicTransport_4_sub_dir);
                     OSM_module_status = 1;
                  }
               } // for (int k = 0; k <= 11; k++)
            } // for (int i = 0; i <= 15; i++)
            
            
            
            // copy leaflet.js file 
            //
            resource = OSM_ROOT_DIR + "/" + LEAFLET_JS;
            output = main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + LEAFLET_JS;
            if (getClass().getResourceAsStream(resource) != null)   
            {
               try (InputStream is = getClass().getResourceAsStream(resource);  // NB in jar file: java.io.File.separator DO NOT WORK UNDER WINDOWS, MUST BE "/" !!!!!
                 OutputStream os = new FileOutputStream(output)   
                   ) 
               {
                  // NB try-with-resource; resources (is and os) will be closed automatically when execution leaves the try block.
               
                  int readBytes;
                  byte[] buffer = new byte[4096];

                  while ((readBytes = is.read(buffer)) > 0) 
                  {
                     os.write(buffer, 0, readBytes);
                  }
                    
                  //info = "--- success when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                  //System.out.println(info);
                  main.log_turbowin_system_message("[OSM] success when copying " + resource + " from jar to: " + main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + LEAFLET_JS);
               }
               catch (IOException ex) 
               {
                  //info = "+++ error when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                  //System.out.println(info);
                  main.log_turbowin_system_message("[OSM] error when copying " + resource + " from jar to: " + main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + LEAFLET_JS);
                  OSM_module_status = 1;
               }               
            }  // if (getClass().getResourceAsStream(resource) != null) 
            else
            {
               main.log_turbowin_system_message("[OSM] error when copying (resource does not exist) " + resource + " from jar to: " + main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + LEAFLET_JS);
               OSM_module_status = 1;
            } // else
            
            
            // copy leaflet.css file 
            //
            resource = OSM_ROOT_DIR + "/" + LEAFLET_CSS;
            output = main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + LEAFLET_CSS;
            if (getClass().getResourceAsStream(resource) != null)   
            {
               try (InputStream is = getClass().getResourceAsStream(OSM_ROOT_DIR + "/" + LEAFLET_CSS);  // NB in jar file: java.io.File.separator DO NOT WORK UNDER WINDOWS, MUST BE "/" !!!!!
                    OutputStream os = new FileOutputStream(output)   
                   ) 
               {
                  // NB try-with-resource; resources (is and os) will be closed automatically when execution leaves the try block.
               
                  int readBytes;
                  byte[] buffer = new byte[4096];

                  while ((readBytes = is.read(buffer)) > 0) 
                  {
                     os.write(buffer, 0, readBytes);
                  }
                    
                  //info = "--- success when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                  //System.out.println(info);
                  main.log_turbowin_system_message("[OSM] success when copying " + LEAFLET_CSS + " from jar to: " + main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + LEAFLET_CSS);
               }
               catch (IOException ex) 
               {
                  //info = "+++ error when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                  //System.out.println(info);
                  main.log_turbowin_system_message("[OSM] error when copying " + LEAFLET_CSS + " from jar to: " + main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + LEAFLET_CSS);
                  OSM_module_status = 1;
               } 
            } // if (getClass().getResourceAsStream(resource) != null)  
            else
            {
               main.log_turbowin_system_message("[OSM] error when copying (resource do not exist) " + resource + " from jar to: " + main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + LEAFLET_CSS);
               OSM_module_status = 1;
            }
            
            
            // copy "marker" file 
            //
            resource = OSM_ROOT_DIR + "/" + MARKER;
            output = main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + MARKER;
            if (getClass().getResourceAsStream(resource) != null)   
            {
               try (InputStream is = getClass().getResourceAsStream(OSM_ROOT_DIR + "/" + MARKER);  // NB in jar file: java.io.File.separator DO NOT WORK UNDER WINDOWS, MUST BE "/" !!!!!
                    OutputStream os = new FileOutputStream(output)   
                   ) 
               {
                  // NB try-with-resource; resources (is and os) will be closed automatically when execution leaves the try block.
               
                  int readBytes;
                  byte[] buffer = new byte[4096];

                  while ((readBytes = is.read(buffer)) > 0) 
                  {
                     os.write(buffer, 0, readBytes);
                  }
                    
                  //info = "--- success when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                  //System.out.println(info);
                  main.log_turbowin_system_message("[OSM] success when copying " + MARKER + " from jar to: " + main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + MARKER);
               }
               catch (IOException ex) 
               {
                  //info = "+++ error when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
                  //System.out.println(info);
                  main.log_turbowin_system_message("[OSM] error when copying " + MARKER + " from jar to: " + main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + MARKER);
                  OSM_module_status = 1;
               } 
            } // if (getClass().getResourceAsStream(resource) != null)  
            else
            {
               main.log_turbowin_system_message("[OSM] error when copying (resource do not exist) " + resource + " from jar to: " + main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + MARKER);
               OSM_module_status = 1;
            }
         } // if (leaflet_js_File.exists() == false)
      } // if (doorgaan == true)
      
      
      return OSM_module_status;
      
   } // copy_OSM_module()
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void OSM_IMMT_on_leaflet_map()
   {
      // called from: Maps_OSM() [main.java]
      
      Boolean doorgaan = true;
      
      // Check logs dir
      if (main.logs_dir.trim().equals("") == true || main.logs_dir.trim().length() < 2)
      {
         String info = "logs folder unknown, select: Maintenance -> Log files settings";
         JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
      }
      
      // chck immt log
      if (doorgaan)
      {
         String volledig_path_immt = main.logs_dir + java.io.File.separator + main.IMMT_LOG;

         File immt_file = new File(volledig_path_immt);
         if (immt_file.exists() && immt_file.length() > 0)    // length() in bytes
         {
            doorgaan = true;
         }  
         else
         {
            String info = "No stored observations (immt.log) found on this computer";
            JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            doorgaan = false;
         } // else      
      }
      
      // logs dir ok and immt log ok
      if (doorgaan)
      {
         // temporary (modaless) dialog box "Processing, Please wait"
         final processing processing_dialog = new processing();     
         processing_dialog.setSize(400, 300);
         processing_dialog.setVisible(true); 
         
         new SwingWorker<List<String>, Void>()
         {
            @Override
            protected List<String> doInBackground() throws Exception
            {
               // NB  first pass: determine the number of immt records in the immt file
               //     because if > 1000 immt records the constructed html file will be cause problems when opened with some browsers (eg Edge)
               
               boolean doorgaan2 = true;
               List<String> immt_list = new ArrayList<>();           // size is now dynamically
               String record = "";
               int teller1 = 0;                                     // for counting total number of records in immt (first loop)
               int teller2 = -1;                                    // for counting numer of records in immt_list (second loop)

               
               // first check if there is an immt log source file present (and not empty) 
               //String record = "";
               String volledig_path_immt = main.logs_dir + java.io.File.separator + main.IMMT_LOG;

               ///File immt_file = new File(volledig_path_immt);
               //if (immt_file.exists() && immt_file.length() > 0)     // length() in bytes
               //{
                  
                  
                  //BufferedReader in = null;
                  //
                  //try
                  //{
                  //   in = new BufferedReader(new FileReader(volledig_path_immt));
                  //   
                  //   try (BufferedReader br = new BufferedReader(new FileReader(volledig_path_immt))) {
//
                  //   int teller = 0;
                  //   while ((record = in.readLine()) != null)
                  //   {
                  //      teller++;
                  //   }
                  //   in.close();
                  //}
                     
               try (BufferedReader in = new BufferedReader(new FileReader(volledig_path_immt))) // try with resources
               {   
                  //int teller = 0;
                  //while ((record = in.readLine()) != null)
                  while ((in.readLine()) != null)   
                  {
                     teller1++;
                  }
               }  
               catch (IOException ex) 
               {
                  String info = "Error when opening immt.log "  + "(" + ex + ")";
                  JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                  doorgaan2 = false;
               }
               //} // if (immt_file.exists() && immt_file.length() > 0)     
               //else
               ///{
               //   String info = "No stored observations (immt.log) found on this computer";
               //   JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                //  doorgaan2 = false;
               //} // else
               
               System.out.println("--- IMMT log number of records: " + teller1);
               
               
             
               if (doorgaan2)
               {
                  // 2nd pass:
                  // - immt file exits and is not empty!
                  
                  
                  //List<String> immt_list = new ArrayList<>();           // size is now dynamically
                  //String record = "";
      
                  // first check if there is an immt log source file present (and not empty) 
                  //String volledig_path_immt = main.logs_dir + java.io.File.separator + main.IMMT_LOG;

                  //File immt_file = new File(volledig_path_immt);
                  //if (immt_file.exists() && immt_file.length() > 0)     // length() in bytes
                  //{
                     //BufferedReader in2 = null;

                     //try
                     //{
                        //in = new BufferedReader(new FileReader(volledig_path_immt));
                  
                    //int teller2 = 0;
                  
                  // trick to display always max the last 1000 records 
                  if (teller1 > 1000)
                  {
                     teller2 = (teller1 -1000) * -1;   // e.g. teller1 = 1700 -> teller2 = -700;
                  }
                  else
                  {
                     teller2 = -1;
                  }
                  System.out.println("--- IMMT log displaying from record number: " + Math.abs(teller2 + 1));
                  
                  
                  try (BufferedReader in2 = new BufferedReader(new FileReader(volledig_path_immt))) // try with resources
                  {
                     while ((record = in2.readLine()) != null)
                     {
                        teller2++;
                        if (teller2 >= 0)
                        {
                           immt_list.add(teller2, record);
                        }
                     }
                     
                     // display the online/offline map via a web browser
                     if (main.OSM_mode.equals(main.OSM_ONLINE_MANUAL) || main.OSM_mode.equals(main.OSM_ONLINE_AWS_VISUAL))        // NB OSM_ONLINE_MANUAL is APR inclusive
                     {
                        OSM_display_IMMT_on_online_map(immt_list);
                     }
                     else if (main.OSM_mode.equals(main.OSM_OFFLINE_MANUAL) || main.OSM_mode.equals(main.OSM_OFFLINE_AWS_VISUAL)) // NB OSM_ONLINE_MANUAL is APR inclusive
                     {
                        OSM_display_IMMT_on_offline_map(immt_list);
                     }
                  }
                  catch (IOException ex) 
                  {
                     String info = "Error when opening immt.log "  + "(" + ex + ")";
                     JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                  }
            
               } // if (doorgaan2)
            
               
               return immt_list;   // no use, but maybe for future use

            } // protected Void doInBackground() throws Exception
         
            @Override
            protected void done()
            {
               //try
               //{
               //   // http://www.codejava.net/java-core/collections/java-list-collection-tutorial-and-examples
               //   List<String> immt_list = get();
               //   
               //   for (String element : immt_list) 
               //   {
               //      System.out.println(element);
               //   }
               //   
               //   //set_latest_obs_values(latest_dashboard_obs);
               //   
               //} // try
               //catch (InterruptedException | ExecutionException ex) 
               //{ 
               //   System.out.println("+++ Error in Function: main_IMMT_on_leaflet_map() [main.java] " + ex);
               //}
               
               processing_dialog.dispose(); // NB actually it closes much too early, but also putting the closing statement in another functions (eg in display_IMMT_on_leaflet_map()) , the result is the same (too early)
               
            } // protected void done()
         
         }.execute(); // new SwingWorker<Void, Void>()      
      } //  if (doorgaan)
      
   }
    
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void OSM_AWS_Sensor_data_on_leaflet_map()
   {
      // called from: Maps_OSM() [main.java]
      
      
      new SwingWorker<Void, Void>()
      {
         @Override
         protected Void doInBackground() throws Exception
         {
            mylatestmeasurements.Read_Sensor_Data_Files_For_Latest_AWS_Measurements(); // now AWS_array[][] is filled (see myleatestmeasurements.java)
               
            // display the online/offline map via a web browser
            if (main.OSM_mode.equals(main.OSM_ONLINE_AWS_SENSOR))
            {
               OSM_display_AWS_Sensor_on_online_map();
            }
            else if (main.OSM_mode.equals(main.OSM_OFFLINE_AWS_SENSOR))
            {
               OSM_display_AWS_Sensor_on_offline_map();
            } 
            
            return null;
         } // protected Void doInBackground() throws Exception

         @Override
         protected void done()
         {
            ;
         }
      }.execute(); // new SwingWorker<Void, Void>()   
   }
 
    
     
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void OSM_display_IMMT_on_online_map(List<String> immt_list)
   {
      // called from: OSM_IMMT_on_leaflet_map() [OSM.java]
      //
      // NB this function is still in background thread
      
      
      Desktop desktop = null;

      // Before more Desktop API is used, first check
      // whether the API is supported by this particular
      // virtual machine (VM) on this particular host.

      if ( (Desktop.isDesktopSupported()) && ((main.logs_dir != null) && (main.logs_dir.compareTo("") != 0)))
      {
         desktop = Desktop.getDesktop();
                  
         try
         {
            // check internet connection available
            URL url = new URL("http://www.google.com");              // arbitray address could also take www.knmi.nl or etc.
            URLConnection con = url.openConnection();
            con.connect();
            

            // it is possible that the OSM dir do not exist, if not create the OSM dir first
            String str_OSM_dir = main.logs_dir + java.io.File.separator + OSM_ROOT_DIR;
            final File OSM_dir = new File(str_OSM_dir);
            if (OSM_dir.exists() == false)
            {
               try
               {
                  OSM_dir.mkdir();
                  main.log_turbowin_system_message("[OSM] created OSM dir for obs online map in " + main.logs_dir);
               }
               catch (Exception ex)
               {
                  // If a security manager exists and its SecurityManager.checkWrite(java.lang.String) method denies access to create the named directory.
                  main.log_turbowin_system_message("[OSM] could NOT create OSM dir for obs online map in " + main.logs_dir + "; reason: " + ex);
               }
            } // if (OSM_dir.exists() == false)
           

            String full_path_leaflet_maps_html_file = main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + OBS_ONLINE_MAP_HTML_FILE;
            OSM_IMMT_Obsen_on_Map(immt_list, full_path_leaflet_maps_html_file);
                     
            // open the just created leaflet maps html with the default web browser
            try
            {
               File leaflet_maps_file = new File(full_path_leaflet_maps_html_file);  // String omzetten naar File

               //if (leaflet_maps_file.exists()) // is niet nodig door het try/catch blok
               desktop.open(leaflet_maps_file);
            }
            catch (NullPointerException | IllegalArgumentException | IOException ex1) 
            { 
               // if file is null  
               System.out.println("+++ unable to create dynamic (online) html file for leaflet Maps plot [function: OSM_display_IMMT_on_online_map()] (" + ex1 + ")");
               main.log_turbowin_system_message("[OSM] error creating online Obs's Map html file");
            }

         } // try
         catch (MalformedURLException ex) 
         { 
            System.out.println("+++ unable to create dynamic (online) html file for leaflet Maps plot [function: OSM_display_IMMT_on_online_map()] (" + ex + ")");
            main.log_turbowin_system_message("[OSM] error creating online Obs's Map html file");
         }
         catch (IOException ioe) 
         { 
            System.out.println("+++ unable to create dynamic (online) html file for leaflet Maps plot [function: OSM_display_IMMT_on_online_map()] (" + ioe + ")");
            main.log_turbowin_system_message("[OSM] error creating online Obs's Map html file");
         }
      } // if ((Desktop.isDesktopSupported()) && etc.  
      
   }
   
   

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void OSM_IMMT_Obsen_on_Map(List<String> immt_list, String full_path_leaflet_maps_html_file)
   {
      // called from: OSM_display_IMMT_on_online_map() [OSM.java]
      //              OSM_display_IMMT_on_offline_map() [OSM.java]
      // NB this function is still in background thread
      //
      // NB see also: Input_Position_menu_actionPerformed() [main.java]
      // NB see also: set_latest_obs_values() [DASHBOARD_latest_obs.java]
      
      
      // make a dynamic html file
      // NB Before invoking this function the existence of the output dir (the logs dir) is already checked positive
      //
      // voor AIS info see: http://www.marinetraffic.com/ais/nl/addyourarea.aspx?level1=150 (English version: http://www.marinetraffic.com/ais/addyourarea.aspx?level1=150)
      
         
      
      //
      // write HTML/JS code
      //
      try
      {
         BufferedWriter out = new BufferedWriter(new FileWriter(full_path_leaflet_maps_html_file));
 
         if (main.OSM_mode.equals(main.OSM_ONLINE_MANUAL) || main.OSM_mode.equals(main.OSM_ONLINE_AWS_VISUAL))
         {
            out.write("<html>");out.newLine();
            out.write("<head>");out.newLine();
            out.write("  <meta charset=utf-8 />");out.newLine();
            out.newLine();
            out.write("  <title>TurboWin+ Obs's Map (internet)</title>");out.newLine();
            out.write("  <meta name='viewport' content='initial-scale=1,maximum-scale=1,user-scalable=no' />");out.newLine();
            out.newLine();
            out.write("  <!-- Load Leaflet from CDN -->");out.newLine();
            out.write(main.LEAFLET_CSS_URL);out.newLine();
            out.write(main.LEAFLET_CSS_INTEGRITY);out.newLine();
            out.write("  crossorigin=\"\"/>");out.newLine();
            out.write(main.LEAFLET_JS_URL);out.newLine();
            out.write(main.LEAFLET_JS_INTEGRITY);out.newLine();
            out.write("  crossorigin=\"\"></script>");out.newLine();
            out.newLine();
            out.write("  <!-- Load Esri Leaflet from CDN -->");out.newLine();
            out.write(main.LEAFLET_ESRI_URL);out.newLine();
            out.write(main.LEAFLET_ESRI_INTEGRITY);out.newLine();
            out.write("  crossorigin=\"\"></script>");out.newLine();
            out.newLine();
            out.write("  <style>");out.newLine();
            out.write("    body { margin:0; padding:0; }");out.newLine();
            out.write("    #map { position: absolute; top:0; bottom:0; right:0; left:0; }");out.newLine();
            out.write("  </style>");out.newLine();
            out.newLine();
            out.write("</head>");out.newLine();
            out.newLine();
            out.write("<body>");out.newLine();
            out.write("<div id=\"map\"></div>");out.newLine();
            out.newLine();
            out.write("<script>");out.newLine();
            out.write("  var map = L.map(\"map\").setView([" + "0.0" + "," + "0.0" + "], 3);");out.newLine();   // origin of the map at 0.0 N and 0.0 E
            out.write("  L.esri.basemapLayer(\"Topographic\").addTo(map);");out.newLine();
            out.write("  var markerOptions = { };");out.newLine();
            out.newLine();
         } // if (main.OSM_mode.equals(main.OSM_ONLINE_MANUAL) || main.OSM_mode.equals(main.OSM_ONLINE_AWS_VISUAL))
         else if (main.OSM_mode.equals(main.OSM_OFFLINE_MANUAL) || main.OSM_mode.equals(main.OSM_OFFLINE_AWS_VISUAL))// offline
         {
            out.write("<html>");out.newLine();
            out.write("<head>");out.newLine();
            //out.write("  <meta charset=utf-8 />");out.newLine();
            out.newLine();
            out.write("  <title>TurboWin+ Obs's Map (offline)</title>");out.newLine();
            out.write("  <meta name='viewport' content='initial-scale=1,maximum-scale=1,user-scalable=no' />");out.newLine();
            out.newLine();
            out.write("  <link rel=\"stylesheet\" charset=\"utf-8\" href=\"leaflet.css\" />");out.newLine();
            out.write("  <script type=\"text/javascript\" charset=\"utf-8\" src=\"leaflet.js\"></script>");out.newLine();
            out.newLine();
            out.write("  <style>");out.newLine();
            out.write("    body { margin:0; padding:0; }");out.newLine();
            out.write("    #map { position: absolute; top:0; bottom:0; right:0; left:0; }");out.newLine();
            out.write("  </style>");out.newLine();
            out.newLine();
            out.write("<body>");out.newLine();
            out.write("<div id=\"map\"></div>");out.newLine();
            out.newLine();
            out.write("<script>");out.newLine();
            out.write(   "var map = L.map('map').setView([0.0,0.0], 3);");out.newLine();
            out.write(   "L.tileLayer('OSMPublicTransport/{z}/{x}/{y}.png',{ maxZoom: 4, minZoom:2 }).addTo(map);");out.newLine();
            out.write(   "var iconOptions = { iconUrl: 'marker_red_meet.png', iconAnchor: [10, 2] };");out.newLine();
            out.write(   "var markerOptions = { icon: L.icon(iconOptions) };");out.newLine();
            out.newLine();  
         } // else if (main.OSM_mode.equals(main.OSM_OFFLINE_MANUAL) || main.OSM_mode.equals(main.OSM_OFFLINE_AWS_VISUAL))
         
         
         for (String obs : immt_list) 
         {
            System.out.println(obs);
                
            String month_full           = "";
            String latitude_hemisphere  = "";
            String longitude_hemisphere = "";
            String true_wind_dir_hulp   = "";
            String lat_prefix           = "";
            String lon_prefix           = "";
            String year                 = "";              
            String month                = "";              
            String day                  = "";              
            String hour                 = "";            
            String quadrant             = "";            // WMO code table 3333
            String latitude             = "";            // tenths of degrees
            String longitude            = "";            // tenths of degrees
            String pressure_MSL         = ""; 
            String air_temp_sign        = "";            // WMO code table 3845
            String air_temp             = "";
            String sst_sign             = "";            // WMO code table 3845
            String sst                  = "";
            String wind_units_ind       = "";            // WMO code table 1855
            String true_wind_dir        = "";            // WMO code table 0877
            String true_wind_speed      = "";
            String local_call_sign      = "";            // local_call_sign to prevent field hiding (global call-sign)
      
      
            if (obs.length() > 80)
            {
               String infowindow_total = "";
               
               // see IMMT description
               // eg IMMT record: 32018051112152700620         0129                     0              44TESTNL US 314            23           A599999199999999991191                    9999 999     01234567 
               year              = obs.substring(1, 5);              
               month             = obs.substring(5, 7);              
               day               = obs.substring(7, 9);              
               hour              = obs.substring(9, 11);            
      
               quadrant          = obs.substring(11, 12);            // WMO code table 3333
               latitude          = obs.substring(12, 15);            // tenths of degrees
               longitude         = obs.substring(15, 19);            // tenths of degrees
         
               pressure_MSL      = obs.substring(37, 41);
               air_temp_sign     = obs.substring(29, 30);            // WMO code table 3845
               air_temp          = obs.substring(30, 33);
               sst_sign          = obs.substring(49, 50);            // WMO code table 3845
               sst               = obs.substring(50, 53);
               wind_units_ind    = obs.substring(26, 27);            // WMO code table 1855
               true_wind_dir     = obs.substring(24, 26);            // WMO code table 0877
               true_wind_speed   = obs.substring(27, 29);
               local_call_sign   = obs.substring(71, 78);
               //wind_waves_period = obs.substring(55, 57);
               //wind_waves_height = obs.substring(57, 59);
               //swell_1_dir       = obs.substring(59, 61);
               //swell_1_period    = obs.substring(61, 63);
               //swell_1_height    = obs.substring(63, 65);
         
               ///////// position quadrant of the globe
               //
               switch (quadrant)
               {
                  case "1" : lat_prefix = "";  
                             latitude_hemisphere = "N";
                             lon_prefix = ""; 
                             longitude_hemisphere = "E";
                             break;
                  case "3" : lat_prefix = "-";
                             latitude_hemisphere = "S";
                             lon_prefix = "";
                             longitude_hemisphere = "E";
                             break;
                  case "5" : lat_prefix = "-";
                             latitude_hemisphere = "S";
                             lon_prefix = "-";
                             longitude_hemisphere = "W";
                             break;
                  case "7" : lat_prefix = "";
                             latitude_hemisphere = "N";
                             lon_prefix = "-";
                             longitude_hemisphere = "W";
                             break;
                  default :  lat_prefix = "";
                             latitude_hemisphere = "?";
                             lon_prefix = "";
                             longitude_hemisphere = "?";
                             break;
               } // switch (quadrant)
         
               ///////// latitude and longitude in degrees but here still without the hemisphere!
               //
               String lat_degrees = latitude.substring(0, 2) + "." + latitude.substring(2, latitude.length());
               String lon_degrees = longitude.substring(0, 3) + "." + longitude.substring(3, longitude.length());
            
               // position marker; NB add hemisphere (+ or -) and leading zeros is not allowed in a position in a leaflet map
               //
               
               // test the validity of the marker position (other wise with one single marker position error nothing will be dispayed
               Boolean marker_position_ok = false;
               if ((lat_degrees.length() == 4 && lon_degrees.length() == 5))        // e.g. "52.4" and "023.7"
               {
                  char lat_one  = lat_degrees.charAt(0);
                  char lat_two  = lat_degrees.charAt(1);
                  char lat_four = lat_degrees.charAt(3);
 
                  char lon_one   = lon_degrees.charAt(0);
                  char lon_two   = lon_degrees.charAt(1);
                  char lon_three = lon_degrees.charAt(2);   
                  char lon_five  = lon_degrees.charAt(4);
                  
                  if (Character.isDigit(lat_one) && Character.isDigit(lat_two) && Character.isDigit(lat_four) &&
                      Character.isDigit(lon_one) && Character.isDigit(lon_two) && Character.isDigit(lon_three) && Character.isDigit(lon_five))   
                  {
                     marker_position_ok = true;       
                  }
                  
               } // if ((lat_degrees.length() == 4 && lon_degrees.length() == 5))
               
               String lat_marker = lat_prefix + lat_degrees.replaceFirst("^0+(?!$)", "");
               String lon_marker = lon_prefix + lon_degrees.replaceFirst("^0+(?!$)", "");
               
               
               // position for the pop-up (different from the marker position, see hemisphere)
               //
               String infowindow_lat = lat_degrees + "&#176 " + latitude_hemisphere;
               String infowindow_lon = lon_degrees + "&#176 " + longitude_hemisphere;

               // date time of the IMMT obs
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

               String infowindow_date_time = day + " " + month_full + " " + year + "  " + hour + ".00 UTC";
      
               if (main.OSM_mode.contains(main.OSM_ONLINE_MANUAL) || main.OSM_mode.contains(main.OSM_OFFLINE_MANUAL))
               {
                  // (true) wind dir and speed
                  //
                  //System.out.println("immt wind dir = " + true_wind_dir);
                  //System.out.println("immt wind speed = " + true_wind_speed);
               
                  String infowindow_wind = "wind: ";
                  if ( (true_wind_dir.trim().length() > 0) && (true_wind_speed.trim().length() > 0) )
                  {
                     switch (wind_units_ind) // (WMO code table 1855)
                     {
                        case "0" : main.wind_units = "m/s"; break; 
                        case "1" : main.wind_units = "m/s"; break; 
                        case "3" : main.wind_units = "kts"; break;  
                        case "4" : main.wind_units = "kts"; break;     
                        default:   main.wind_units = ""; break;   
                     } // switch 
                  
                     if (true_wind_dir.equals("99"))
                     {
                        true_wind_dir_hulp = "var";
                     }
                     else
                     {
                        true_wind_dir_hulp = true_wind_dir + "0";           // eg 34 immt-code -> 340°
                     }
            
                     infowindow_wind = "wind: " + true_wind_dir_hulp + "&#176" + " / " + true_wind_speed + " " + main.wind_units;
                  } // if (!true_wind_dir.equals("") && !true_wind_speed.equals(""))
               
                  // air temp
                  //
                  //System.out.println("immt air temp = " + air_temp);
               
                  String infowindow_air_temp = "air temp: ";
                  //System.out.println("air tmp length =  " + air_temp.length()  );
                  if (air_temp.trim().length() > 0)
                  {
                     air_temp = air_temp.substring(0, 2) + "." + air_temp.substring(2, air_temp.length());
                     air_temp = air_temp.replaceFirst("^0+(?!$)", "");   // remove leading zeros but leaves one if necessary (i.e. it wouldn't just turn "0" to a blank string)
                  
                     if (air_temp_sign.equals("1"))
                     {
                        infowindow_air_temp = "air temp: -" + air_temp + " &#176 C";
                     }
                     else
                     {   
                        infowindow_air_temp = "air temp: " + air_temp + " &#176 C";
                     }
                  } 
      
                  // sst
                  //
                  String infowindow_sst = "sst: ";
                  if (sst.trim().length() > 0)
                  {
                     sst = sst.substring(0, 2) + "." + sst.substring(2, sst.length());
                     sst = air_temp.replaceFirst("^0+(?!$)", "");   // remove leading zeros but leaves one if necessary (i.e. it wouldn't just turn "0" to a blank string)
                  
                     if (sst_sign.equals("1"))
                     {
                        infowindow_sst = "sst: -" + sst + " &#176 C";
                     }
                     else
                     {   
                        infowindow_sst = "sst: " + sst + " &#176 C";
                     }
                  }
      
                  // MSL air pressure
                  //
                  String infowindow_msl_pressure = "slp: ";
                  if (pressure_MSL.trim().length() > 0)           // there is something present
                  {
                     try
                     {
                        int int_pressure_MSL = Integer.parseInt(pressure_MSL);
                        if (int_pressure_MSL > 8000)
                        {
                           infowindow_msl_pressure = "slp: " + pressure_MSL.substring(0, 3) + "." + pressure_MSL.substring(3, pressure_MSL.length()) + " hPa";
                        }
                        else
                        {
                           infowindow_msl_pressure = "slp: " + "1" + pressure_MSL.substring(0, 3) + "." + pressure_MSL.substring(3, pressure_MSL.length()) + " hPa";
                        }
                     }
                     catch (NumberFormatException e)
                     {
                        infowindow_msl_pressure = "slp: ";
                     }
                  } // if (!pressure_MSL.equals(""))
               
               
                  // pop-up text
                  //
                  infowindow_total = "\"" + "<h3>" + local_call_sign.trim() + " weather observation" + "</h3>"
                                 + "<p>"
                                 + " position: " +  infowindow_lat + ", " +  infowindow_lon + "<br>"
                                 + " date time: " + infowindow_date_time + "<br>"
                                 + infowindow_wind  + "<br>"
                                 //+ infowindow_wind_speed + "<br>"
                                 + infowindow_air_temp + "<br>"
                                 + infowindow_sst + "<br>"
                                 + infowindow_msl_pressure + "<br>"
                                 + "</p>" + "\"";  
               } // if (main.OSM_mode.contains(main.OSM_ONLINE_MANUAL) || main.OSM_mode.contains(main.OSM_OFFLINE_MANUAL))
               else if (main.OSM_mode.contains(main.OSM_ONLINE_AWS_VISUAL) || main.OSM_mode.contains(main.OSM_OFFLINE_AWS_VISUAL))
               {
                  // pop-up text
                  //
                  infowindow_total = "\"" + "<h3>" + local_call_sign.trim() + " weather observation" + "</h3>"
                                 + "<p>"
                                 + " position: " +  infowindow_lat + ", " +  infowindow_lon + "<br>"
                                 + " date time: " + infowindow_date_time + "<br>"
                                 + "<br>"
                                 + "visual data uploaded" + "<br>"
                                 + "</p>" + "\"";  
               } // else if etc.
               
               if (marker_position_ok)
               {
                  out.write("  L.marker([" + lat_marker + "," + lon_marker + "], markerOptions).addTo(map).bindPopup(" + infowindow_total + ").openPopup();");out.newLine();
               }
               
            } // if (obs.length() > 70)
         
         } //  for (String obs : immt_list) 
         out.newLine();
         
         out.write("</script>");out.newLine();
         out.newLine();
         out.write("</body>");out.newLine();
         out.write("</html>");out.newLine();    
         
         out.close();

      } // try
      catch (HeadlessException | IOException e)
      {
         String info = "[OSM] error creating Obs's Map html file (" + e + ")";
         main.log_turbowin_system_message(info);
      } // catch
      
   }
  
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void OSM_display_IMMT_on_offline_map(List<String> immt_list)
   {
      // called from: OSM_IMMT_on_leaflet_map() [OSM.java]
      //
      // NB this function is still in background thread
      
      
      Desktop desktop = null;

      // Before more Desktop API is used, first check
      // whether the API is supported by this particular
      // virtual machine (VM) on this particular host.

      if ( (Desktop.isDesktopSupported()) && ((main.logs_dir != null) && (main.logs_dir.compareTo("") != 0)))
      {
         desktop = Desktop.getDesktop();
                  
         String full_path_leaflet_maps_html_file = main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + OBS_OFFLINE_MAP_HTML_FILE;
         OSM_IMMT_Obsen_on_Map(immt_list, full_path_leaflet_maps_html_file);
                     
         // open the just created leaflet maps html with the default web browser
         try
         {
            File leaflet_maps_file = new File(full_path_leaflet_maps_html_file);  // String omzetten naar File

            //if (leaflet_maps_file.exists()) // is niet nodig door het try/catch blok
            desktop.open(leaflet_maps_file);
         }
         catch (NullPointerException | IllegalArgumentException | IOException ex1) 
         { 
            // if file is null  
            System.out.println("+++ unable to create dynamic (offline) html file for leaflet Maps plot [function: OSM_display_IMMT_on_offline_map()] (" + ex1 + ")");
            main.log_turbowin_system_message("[OSM] error creating offline AWS sensor Obs's Map html file");
         }

      } // if ((Desktop.isDesktopSupported()) && etc.  
      
   } // private void OSM_display_IMMT_on_offline_map(List<String> immt_list)
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void OSM_display_AWS_Sensor_on_online_map()
   {
      // called from: OSM_AWS_Sensor_data_on_leaflet_map() [OSM.java]
      //
      // NB this function is still in background thread
      
      
      Desktop desktop = null;

      // Before more Desktop API is used, first check
      // whether the API is supported by this particular
      // virtual machine (VM) on this particular host.

      if ( (Desktop.isDesktopSupported()) && ((main.logs_dir != null) && (main.logs_dir.compareTo("") != 0)))
      {
         desktop = Desktop.getDesktop();
                  
         try
         {
            // check internet connection available
            URL url = new URL("http://www.google.com");              // arbitray address could also take www.knmi.nl or etc.
            URLConnection con = url.openConnection();
            con.connect();

            String full_path_leaflet_maps_html_file = main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + OBS_ONLINE_MAP_HTML_FILE;
            OSM_AWS_Sensor_Obsen_on_Map(full_path_leaflet_maps_html_file);
                     
            // open the just created leaflet maps html with the default web browser
            try
            {
               File leaflet_maps_file = new File(full_path_leaflet_maps_html_file);  // String omzetten naar File

               //if (leaflet_maps_file.exists()) // is niet nodig door het try/catch blok
               desktop.open(leaflet_maps_file);
            }
            catch (NullPointerException | IllegalArgumentException | IOException ex1) 
            { 
               // if file is null  
               System.out.println("+++ unable to create dynamic (online) html file for leaflet Maps plot [function: OSM_display_AWS_Sensor_on_online_map()] (" + ex1 + ")");
               main.log_turbowin_system_message("[OSM] error creating online AWS sensor Obs's Map html file");
            }

         } // try
         catch (MalformedURLException ex) 
         { 
            System.out.println("+++ unable to create dynamic (online) html file for leaflet Maps plot [function: OSM_display_AWS_Sensor_on_online_map()] (" + ex + ")");
            main.log_turbowin_system_message("[OSM] error creating online AWS sensor Obs's Map html file");
         }
         catch (IOException ioe) 
         { 
            System.out.println("+++ unable to create dynamic (online) html file for leaflet Maps plot [function: OSM_display_AWS_Sensor_on_online_map()] (" + ioe + ")");
            main.log_turbowin_system_message("[OSM] error creating online AWS sensor Obs's Map html file");
         }
      } // if ((Desktop.isDesktopSupported()) && etc.  
   
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void OSM_display_AWS_Sensor_on_offline_map()
   {
      // called from: called from: OSM_AWS_Sensor_data_on_leaflet_map() [OSM.java]
      //
      // NB this function is still in background thread
      
      
      Desktop desktop = null;

      // Before more Desktop API is used, first check
      // whether the API is supported by this particular
      // virtual machine (VM) on this particular host.

      if ( (Desktop.isDesktopSupported()) && ((main.logs_dir != null) && (main.logs_dir.compareTo("") != 0)))
      {
         desktop = Desktop.getDesktop();
                  
         String full_path_leaflet_maps_html_file = main.logs_dir + java.io.File.separator + OSM_ROOT_DIR + java.io.File.separator + OBS_OFFLINE_MAP_HTML_FILE;
         OSM_AWS_Sensor_Obsen_on_Map(full_path_leaflet_maps_html_file);
                     
         // open the just created leaflet maps html with the default web browser
         try
         {
            File leaflet_maps_file = new File(full_path_leaflet_maps_html_file);  // String omzetten naar File

            //if (leaflet_maps_file.exists()) // is niet nodig door het try/catch blok
            desktop.open(leaflet_maps_file);
         }
         catch (NullPointerException | IllegalArgumentException | IOException ex1) 
         { 
            // if file is null  
            System.out.println("+++ unable to create dynamic (offline) html file for leaflet Maps plot [function: OSM_display_AWS_Sensor_on_offline_map()] (" + ex1 + ")");
            main.log_turbowin_system_message("[OSM] error creating offline AWS sensor Obs's Map html file");
         }

      } // if ((Desktop.isDesktopSupported()) && etc.  

   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void OSM_AWS_Sensor_Obsen_on_Map(String full_path_leaflet_maps_html_file)
   {
      // called from: - OSM_display_AWS_Sensor_on_offline_map()
      //              - OSM_display_AWS_Sensor_on_online_map()
      
      // NB this function is still in background thread
      //
      // NB see also: Input_Position_menu_actionPerformed() [main.java]
      // NB see also: set_latest_obs_values() [DASHBOARD_latest_obs.java]
      
      
      // make a dynamic html file
      // NB Before invoking this function the existence of the output dir (the logs dir) is already checked positive
      //
      // voor AIS info see: http://www.marinetraffic.com/ais/nl/addyourarea.aspx?level1=150 (English version: http://www.marinetraffic.com/ais/addyourarea.aspx?level1=150)
      
         
      
      //
      // write HTML/JS code
      //
      try
      {
         BufferedWriter out = new BufferedWriter(new FileWriter(full_path_leaflet_maps_html_file));
 
         if (main.OSM_mode.equals(main.OSM_ONLINE_AWS_SENSOR))
         {
            out.write("<html>");out.newLine();
            out.write("<head>");out.newLine();
            out.write("  <meta charset=utf-8 />");out.newLine();
            out.newLine();
            out.write("  <title>TurboWin+ AWS sensor Map (internet)</title>");out.newLine();
            out.write("  <meta name='viewport' content='initial-scale=1,maximum-scale=1,user-scalable=no' />");out.newLine();
            out.newLine();
            out.write("  <!-- Load Leaflet from CDN -->");out.newLine();
            out.write(main.LEAFLET_CSS_URL);out.newLine();
            out.write(main.LEAFLET_CSS_INTEGRITY);out.newLine();
            out.write("  crossorigin=\"\"/>");out.newLine();
            out.write(main.LEAFLET_JS_URL);out.newLine();
            out.write(main.LEAFLET_JS_INTEGRITY);out.newLine();
            out.write("  crossorigin=\"\"></script>");out.newLine();
            out.newLine();
            out.write("  <!-- Load Esri Leaflet from CDN -->");out.newLine();
            out.write(main.LEAFLET_ESRI_URL);out.newLine();
            out.write(main.LEAFLET_ESRI_INTEGRITY);out.newLine();
            out.write("  crossorigin=\"\"></script>");out.newLine();
            out.newLine();
            out.write("  <style>");out.newLine();
            out.write("    body { margin:0; padding:0; }");out.newLine();
            out.write("    #map { position: absolute; top:0; bottom:0; right:0; left:0; }");out.newLine();
            out.write("  </style>");out.newLine();
            out.newLine();
            out.write("</head>");out.newLine();
            out.newLine();
            out.write("<body>");out.newLine();
            out.write("<div id=\"map\"></div>");out.newLine();
            out.newLine();
            out.write("<script>");out.newLine();
            out.write("  var map = L.map(\"map\").setView([" + "0.0" + "," + "0.0" + "], 3);");out.newLine();   // origin of the map at 0.0 N and 0.0 E
            out.write("  L.esri.basemapLayer(\"Topographic\").addTo(map);");out.newLine();
            out.write("  var markerOptions = { };");out.newLine();
            out.newLine();
         } // if (main.OSM_mode.equals(main.OSM_ONLINE_AWS_SENSOR))
         else if (main.OSM_mode.equals(main.OSM_OFFLINE_AWS_SENSOR))
         {
            out.write("<html>");out.newLine();
            out.write("<head>");out.newLine();
            //out.write("  <meta charset=utf-8 />");out.newLine();
            out.newLine();
            out.write("  <title>TurboWin+ AWS sensor Map (offline)</title>");out.newLine();
            out.write("  <meta name='viewport' content='initial-scale=1,maximum-scale=1,user-scalable=no' />");out.newLine();
            out.newLine();
            out.write("  <link rel=\"stylesheet\" charset=\"utf-8\" href=\"leaflet.css\" />");out.newLine();
            out.write("  <script type=\"text/javascript\" charset=\"utf-8\" src=\"leaflet.js\"></script>");out.newLine();
            out.newLine();
            out.write("  <style>");out.newLine();
            out.write("    body { margin:0; padding:0; }");out.newLine();
            out.write("    #map { position: absolute; top:0; bottom:0; right:0; left:0; }");out.newLine();
            out.write("  </style>");out.newLine();
            out.newLine();
            out.write("<body>");out.newLine();
            out.write("<div id=\"map\"></div>");out.newLine();
            out.newLine();
            out.write("<script>");out.newLine();
            out.write(   "var map = L.map('map').setView([0.0,0.0], 3);");out.newLine();
            out.write(   "L.tileLayer('OSMPublicTransport/{z}/{x}/{y}.png',{ maxZoom: 4, minZoom:2 }).addTo(map);");out.newLine();
            out.write(   "var iconOptions = { iconUrl: 'marker_red_meet.png', iconAnchor: [10, 2] };");out.newLine();
            out.write(   "var markerOptions = { icon: L.icon(iconOptions) };");out.newLine();
            out.newLine();  
         } // else if (main.OSM_mode.equals(main.OSM_OFFLINE_AWS_SENSOR))
         
         
         for (int i = 0; i < mylatestmeasurements.AANTAL_AWS_MEASUREMENTS; i++) 
         {
            //System.out.println(obs);
      
      
            if ( (!mylatestmeasurements.AWS_array[i][mylatestmeasurements.date_index].equals("")) && (!mylatestmeasurements.AWS_array[i][mylatestmeasurements.time_index].equals("")) )
            {
               String infowindow_total        = "";
               String infowindow_date_time    = "";   
               String infowindow_lat          = "";  
               String infowindow_lon          = "";   
               String infowindow_wind         = "wind: ";
               String infowindow_wind_speed   = "";
               String infowindow_wind_dir     = "";
               String infowindow_air_temp     = "air temp: ";    
               String infowindow_sst          = "sst: ";
               String infowindow_msl_pressure = "slp: ";
               
               String lon_marker           = "";
               String lat_marker           = "";
               String lat_degrees          = "";
               String lat_degrees_info     = "";             // for pop-up info box (without the"-" sign)
               String lon_degrees          = "";
               String lon_degrees_info     = "";             // for popup info box (without the"-" sign)            
               String month_full           = "";
               String latitude_hemisphere  = "";
               String longitude_hemisphere = "";
               //String true_wind_dir_hulp   = "";
               //String lat_prefix           = "";
               //String lon_prefix           = "";
               String year                 = "";              
               String month                = "";              
               String day                  = "";              
               String time                 = "";            
               //String latitude             = "";            // tenths of degrees
               //String longitude            = "";            // tenths of degrees
               String pressure_MSL         = ""; 
               //String air_temp_sign        = "";            // WMO code table 3845
               String air_temp             = "";
               //String sst_sign             = "";            // WMO code table 3845
               String sst                  = "";
               //String wind_units_ind       = "";            // WMO code table 1855
               String true_wind_dir        = "";            // WMO code table 0877
               String true_wind_speed      = "";
               //String local_call_sign      = "";            // local_call_sign to prevent field hiding (global call-sign)

               boolean marker_position_ok = true;
               
               for (int c = 0; c < mylatestmeasurements.AANTAL_AWS_PARAMETERS; c++)     // columns
               {    
                  if (c == mylatestmeasurements.date_index)
                  {
                     if (mylatestmeasurements.AWS_array[i][c].length() == 8)
                     {
                        // format e.g. 20190221 
                        year  = mylatestmeasurements.AWS_array[i][c].substring(0, 4);
                        month = mylatestmeasurements.AWS_array[i][c].substring(4, 6); 
                        day   = mylatestmeasurements.AWS_array[i][c].substring(6, 8);
                        
                        
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
                     } // if (mylatestmeasurements.AWS_array[i][c].length() == 8)
                  } // if (c == mylatestmeasurements.date_index
                  else if (c == mylatestmeasurements.time_index)
                  {
                     if (mylatestmeasurements.AWS_array[i][c].length() == 6)
                     {
                        // format e.g. 0920
                        time = mylatestmeasurements.AWS_array[i][c].substring(0, 2) + "." + mylatestmeasurements.AWS_array[i][c].substring(2, 4);
                     }
                     
                     infowindow_date_time = day + " " + month_full + " " + year + "  " + time + " UTC";
                  }
                  else if (c == mylatestmeasurements.lat_index)
                  {
                     double double_lat_degrees = 0;
                     
                     // eg 52.405
                     lat_degrees = mylatestmeasurements.AWS_array[i][c];
                     
                     try
                     {
                        double_lat_degrees = Double.parseDouble(lat_degrees);
                        
                        if (double_lat_degrees < 0)
                        {
                           latitude_hemisphere = "S";
                           lat_degrees_info = lat_degrees.substring(1);   // 'delete' the "-" sign
                        }
                        else
                        {
                           latitude_hemisphere = "N";
                           lat_degrees_info = lat_degrees;
                        }
                     
                        infowindow_lat = lat_degrees_info + "&#176 " + latitude_hemisphere;
                        
                        // position marker; NB leading zeros is not allowed in a position in a leaflet map
                        //
                        lat_marker = lat_degrees.replaceFirst("^0+(?!$)", "");
                        
                     } // try
                     catch (NumberFormatException e)
                     {
                        marker_position_ok = false; 
                     }
                  } // else if (c == mylatestmeasurements.lat_index)
                  else if (c == mylatestmeasurements.lon_index)
                  {
                     double double_lon_degrees = 0;
                     
                     // eg -4.405
                     lon_degrees = mylatestmeasurements.AWS_array[i][c];
                     
                     try
                     {
                        double_lon_degrees = Double.parseDouble(lon_degrees);
                        
                        if (double_lon_degrees < 0)
                        {
                           longitude_hemisphere = "W";
                           lon_degrees_info = lon_degrees.substring(1);   // delete the "-" sign
                        }
                        else
                        {
                           longitude_hemisphere = "E";
                           lon_degrees_info = lon_degrees;
                        }
                     
                        infowindow_lon = lon_degrees_info + "&#176 " + longitude_hemisphere;
                     
                        // position marker; NB leading zeros is not allowed in a position in a leaflet map
                        //
                        lon_marker = lon_degrees.replaceFirst("^0+(?!$)", "");
                    
                     }
                     catch (NumberFormatException e)
                     {
                        marker_position_ok = false; 
                     }
                     
                  } // else if (c == mylatestmeasurements.lat_index)
                  else if (c == mylatestmeasurements.true_wind_speed_index)
                  {
                     // NB true wind speed wil be read before true wind dir!!
                     
                     true_wind_speed = mylatestmeasurements.AWS_array[i][c];
                     if (true_wind_speed.length() >= 1)
                     {
                        String hulp_true_wind_speed = "";
                        if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)               // units is knots
                        {
                           double wind_speed_knots_double = Double.parseDouble(true_wind_speed) * main.M_S_KNOT_CONVERSION;
                           hulp_true_wind_speed = String.format("%.2f", wind_speed_knots_double);
                        }
                        else
                        {
                           hulp_true_wind_speed = true_wind_speed;
                        }
                        
                        infowindow_wind_speed = hulp_true_wind_speed + " " + main.wind_units_dashboard; 
                     }
                     else
                     {
                        infowindow_wind_speed = "-";
                     }
                  } // else if (c == mylatestmeasurements.true_wind_dir_index)   
                  else if (c == mylatestmeasurements.true_wind_dir_index)
                  {
                     // NB true wind dir will be read after true wind speed !!
                     
                     true_wind_dir = mylatestmeasurements.AWS_array[i][c];
                     if (true_wind_dir.length() >=1)
                     {
                        infowindow_wind_dir = true_wind_dir + "&#176" + " / "; 
                     }
                     else
                     {
                        infowindow_wind_dir = "-" + " / "; 
                     }
                     
                     infowindow_wind += infowindow_wind_dir + infowindow_wind_speed;
                     
                  } // else if (c == mylatestmeasurements.true_wind_dir_index)
                  else if (c == mylatestmeasurements.air_temp_index)   
                  {
                     air_temp = mylatestmeasurements.AWS_array[i][c];
                     
                     if (air_temp.length() >= 1)
                     {
                        infowindow_air_temp += air_temp + " &#176 C";
                     }
                     else
                     {   
                        infowindow_air_temp += "-";
                     }
                  } // else if (c == mylatestmeasurements.air_temp_index) 
                  else if (c == mylatestmeasurements.sst_index)   
                  {
                     sst = mylatestmeasurements.AWS_array[i][c];
                     
                     if (sst.length() >= 1)
                     {
                        infowindow_sst += sst + " &#176 C";
                     }
                     else
                     {   
                        infowindow_sst += "-";
                     }
                  } // else if (c == mylatestmeasurements.sst_index)    
                  else if (c == mylatestmeasurements.slp_index)   
                  {
                     pressure_MSL = mylatestmeasurements.AWS_array[i][c];
                     
                     if (pressure_MSL.length() >= 1)
                     {
                        infowindow_msl_pressure += pressure_MSL +  " hPa";
                     }
                     else
                     {   
                        infowindow_msl_pressure += "-";
                     }
                  } // else if (c == mylatestmeasurements.sst_index)      
               
               } // for (int c = 0; c < mylatestmeasurements.AANTAL_AWS_PARAMETERS; c++) 
               
               
               
               // pop-up text
               //
               infowindow_total = "\"" + "<h3>" + main.call_sign.trim() + " AWS sensor" + "</h3>"
                              + "<p>"
                              + " position: " +  infowindow_lat + ", " +  infowindow_lon + "<br>"
                              + " date time: " + infowindow_date_time + "<br>"
                              + infowindow_wind  + "<br>"
                              //+ infowindow_wind_speed + "<br>"
                              + infowindow_air_temp + "<br>"
                              + infowindow_sst + "<br>"
                              + infowindow_msl_pressure + "<br>"
                              + "</p>" + "\"";  
               //} // if (main.OSM_mode.contains(main.OSM_ONLINE_MANUAL) || main.OSM_mode.contains(main.OSM_OFFLINE_MANUAL))
               
/*
               // test the validity of the marker position (other wise even with one single marker position error nothing will be dispayed)
               Boolean marker_position_ok = false;
               if (lat_degrees.length() >= 3 && lon_degrees.length() >= 3)   // so at least something like "3.8"
               {
                  // check the first char before and after the found "."
                  int pos_point_lat = lat_degrees.indexOf(".");
                  char lat_a  = lat_degrees.charAt(pos_point_lat - 1);
                  char lat_b  = lat_degrees.charAt(pos_point_lat + 1);
                  
                  int pos_point_lon = lon_degrees.indexOf(".");
                  char lon_a  = lon_degrees.charAt(pos_point_lon - 1);
                  char lon_b  = lon_degrees.charAt(pos_point_lon + 1);
                  
                  if ( Character.isDigit(lat_a) && Character.isDigit(lat_b) &&
                       Character.isDigit(lon_a) && Character.isDigit(lon_b) )   
                  {
                     marker_position_ok = true;       
                  }
               } // if (lat_degrees.length() >= 3 && lon_degrees.length() >= 3)
*/

               if (marker_position_ok)
               {   
                  out.write("  L.marker([" + lat_marker + "," + lon_marker + "], markerOptions).addTo(map).bindPopup(" + infowindow_total + ").openPopup();");out.newLine();
               }
            } // if ( (!mylatestmeasurements.AWS_array[i][mylatestmeasurements.date_index].equals("")) && etc.
         
         } // for (int i = 0; i < AANTAL_AWS_MEASUREMENTS; i++) 
         out.newLine();
         
         out.write("</script>");out.newLine();
         out.newLine();
         out.write("</body>");out.newLine();
         out.write("</html>");out.newLine();    
         
         out.close();

      } // try
      catch (HeadlessException | IOException e)
      {
         String info = "[OSM] error creating Obs's Map html file (" + e + ")";
         main.log_turbowin_system_message(info);
      } // catch
      
 
   }
   
   
   
   
   
   
   
   //public static final String LEAFLET_CSS_URL                = "  <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.3.1/dist/leaflet.css\"";
   //public static final String LEAFLET_JS_URL                 = "  <script src=\"https://unpkg.com/leaflet@1.3.1/dist/leaflet.js\"";
   //public static final String LEAFLET_ESRI_URL               = "  <script src=\"https://unpkg.com/esri-leaflet@2.1.4/dist/esri-leaflet.js\"";
   //public static final String LEAFLET_CSS_INTEGRITY          = "  integrity=\"sha512-Rksm5RenBEKSKFjgI3a41vrjkw4EVPlJ3+OiI65vTjIdo9brlAacEuKOiQ5OFh7cOI1bkDwLqdLw3Zg0cRJAAQ==\"";
   //public static final String LEAFLET_JS_INTEGRITY           = "  integrity=\"sha512-/Nsx9X4HebavoBvEBuyp3I7od5tA0UzAxs+j83KgC8PU0kgB4XiK4Lfe4y4cgBtaRJQEIFCW+oC506aPT2L1zw==\"";
   //public static final String LEAFLET_ESRI_INTEGRITY         = "  integrity=\"sha512-m+BZ3OSlzGdYLqUBZt3u6eA0sH+Txdmq7cqA1u8/B2aTXviGMMLOfrKyiIW7181jbzZAY0u+3jWoiL61iLcTKQ==\"";
   //public static final String LEAFLET_MAPS_HTML_FILE         = "position_leaflet_maps.html";     // leaflet maps file

   public static final String OBS_OFFLINE_MAP_HTML_FILE      = "obs_offline_map.html";     // leaflet maps file
   public static final String OBS_ONLINE_MAP_HTML_FILE       = "obs_online_map.html";      // leaflet maps file

   private final String MARKER                               = "marker_red_meet.png";
   private final String LEAFLET_CSS                          = "leaflet.css";
   private final String LEAFLET_JS                           = "leaflet.js";
   private final String OSM_ROOT_DIR                         = "OSM";
   private final String OSM_PUBLIC_TRANSPORT_DIR             = "OSMPublicTransport";
   
} // public class OSM 










