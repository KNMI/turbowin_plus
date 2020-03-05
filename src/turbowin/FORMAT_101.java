package turbowin;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hometrainer
 */
public class FORMAT_101 
{
   // for compression required: 
   //                             - dir: format_101 [with file: teste_hc_TW.exe (Windows) or teste_hc_TW (Linux)]
   //                             - sub dir: format_101/config [with fixed file: S-AWS-101_modl_pilote.csv]
   //                             - sub dir: format_101/temp [empty] 
   //                             - sub dir: format_101/log [empty]                    
   //
   
   // for decompression extra required: 
   //                             - dir: format_101 [with file: MAWSbin.exe (Windows) or MAWSbin (Linux)]
   //                             - sub dir: format_101/config [with fixed files:  S-AWS-101_modl_meta.csv and file: DataType.txt
   //                               note file: HC_ident.txt, also required in sub dir format_101/config, WILL ALWAYS BE RECREATED BY TURBOWIN+                                              
   //
  
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void compress_and_decompress_101_control_center()
   {
      int return_status = 0;
      boolean doorgaan = true;
      String compression_identifier = "unknown";
      
      
      //
      //////// determine OS (for selecting the correct compression and decompression modules)
      //
      main.OSType ostype = main.detect_OS();
      switch (ostype)
      {
         case LINUX:   compression_exe   = COMPRESSION_EXE_LIN; 
                       decompression_exe = DECOMPRESSION_EXE_LIN;
                       break;
         case WINDOWS: compression_exe   = COMPRESSION_EXE_WIN; 
                       decompression_exe = DECOMPRESSION_EXE_WIN;
                       break;
         default:      compression_exe   = COMPRESSION_EXE_WIN;
                       decompression_exe = DECOMPRESSION_EXE_WIN;
                       break;
      }
      
      
      //
      /////////////// for compression and decompression if necessary copy all format 101 related files from jar to destination ///////////
      //
      return_status = check_format_101_module();
      if (return_status != 0)                        // compression exe not found so now try to create dires and copy format 101 module files
      {
         return_status = copy_format_101_module();
         if (return_status != 0)                     // now if return_status = 0 compete format 101 module was copied sucessfully from jar to "logs_dir"
         {
            doorgaan = false;
         }
      }      
      
      
      //
      /////////////// for compression and decompression compression_identifier ///////////
      //
      if (doorgaan == true)
      {
         // NB if station ID or masked call sign (= masked station ID) iserted -> use this for obs else 'normal' call sign
         if ((main.station_ID != null) && (main.station_ID.length() > 0))
         {
            compression_identifier = main.station_ID;
         }
         else if ((main.masked_call_sign != null) && (main.masked_call_sign.trim().length() > 0))
         {   
            compression_identifier = main.masked_call_sign;
         }
         else if ((main.call_sign != null) && (main.call_sign.trim().length() > 0))
         {
            compression_identifier = main.call_sign;
         }   
         else
         {
            compression_identifier = "unknown";
         }
      } // if (doorgaan == true)
      
      
      //
      /////////////////////// compression /////////////////////
      //
      if (doorgaan == true)
      {
         return_status = check_and_clear_format_101_temp_folder();
         if (return_status != 0)
         {
            doorgaan = false;
         }
      }   
      
      if (doorgaan == true)
      {
         write_input_for_101_compression();
         compress_101(compression_identifier);
      }

      
      //
      /////////////////////// decompression /////////////////
      //
      if (doorgaan == true)
      {
         return_status = write_HC_identification_file(compression_identifier);
         if (return_status != 0)
         {
            doorgaan = false;
         }
      } // if (doorgaan == true)
      
      if (doorgaan == true)
      {
         decompress_101();
      } 
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private int check_format_101_module()
   {
      int format_101_module_status = 0;
      //String info = "";
      
   
      if (main.logs_dir.trim().equals("") == false && main.logs_dir.trim().length() >= 2)
      {
         // NB see function copy_format_101_module() for a message if "logs_dir" do not exist
         
         // NB e.g. exeFile = "C:\Program Files (x86)\TurboWin+\logs\format_101\teste_hc_TW.exe"
         final File exeFile = new File(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + compression_exe);
         if (exeFile.exists() == false)
         {   
            format_101_module_status = 1;
            
            //info = "--- did not find: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
            //System.out.println(info);
            main.log_turbowin_system_message("[FORMAT-101] did not find: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator +compression_exe );
         }   
         else
         {
            format_101_module_status = 0;
            
            //info = "--- found: format 101 compression executable (" + COMPRESSION_EXE + ")";
            //System.out.println(info);   
            main.log_turbowin_system_message("[FORMAT-101] found: format 101 compression executable (" + compression_exe + ")");
         }
      }
      
      
      return format_101_module_status;   
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private int copy_format_101_module()
   {
      int format_101_module_status = 0;
      boolean doorgaan = true;
      String info = "";
     
      
      // NB the complete instantiation of class format_101 is already done in a swingworker! so not necessary to use a swingworker here for file copying etc.
      
      // NB in offline_mode:  logs_dir = user_dir + java.io.File.separator + OFFLINE_LOGS_DIR;
      // NB in logs_dir always 'user_dir' already present (so a complete path)
      
      
      // check if logs folder exists (because this will be the root folder of all the format_101 dirs and files)
      if (main.logs_dir.trim().equals("") == true || main.logs_dir.trim().length() < 2)
      {
         doorgaan = false;
         info = "logs folder unknown, select: Maintenance -> Log files settings and retry";
         JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
      }
      
      
      // check if compression exe exists, if not create all format 101 dirs (again) and copy all the necessary format 101 fiies
      if (doorgaan == true)
      {
         // NB e.g. exeFile = "C:\Program Files (x86)\TurboWin+\logs\format_101\teste_hc_TW.exe"
         final File exeFile = new File(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + compression_exe);
         if (exeFile.exists() == false)
         {
            // create format 101 (sub)dirs and copy all the necessary files from the turbowin_jws.jar resource to the newly created dirs
            //
            //info = "--- start copying complete format 101 module from jar to: " + main.logs_dir;
            //System.out.println(info);   
            main.log_turbowin_system_message("[FORMAT-101] start copying complete format 101 module from jar to: " + main.logs_dir);
            
            // message (because it could be that virus scanner is complaining about compression exe (teste_hc_TW.exe))
            //String info_message = "format 101 module will be copied from source to destination\n note: in case you get a warning of your virus scanner, " + COMPRESSION_EXE + " and " + DECOMPRESSION_EXE + " are essential parts of " + main.APPLICATION_NAME + " !";
            //JOptionPane.showMessageDialog(null, info_message, main.APPLICATION_NAME + " info", JOptionPane.WARNING_MESSAGE);
            // note veplaatst naar obsformat.java (i.v.m messagebox niet goed in APR mode)
            
            // create dir "format_101" (e.g. "C:\Program Files (x86)\TurboWin+\logs\format_101")
            //
            String format_101_dir = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR;
            final File dir_format_101 = new File(format_101_dir);   
            dir_format_101.mkdir();
                    
            // create sub dir "format_101\config" (e.g. "C:\Program Files (x86)\TurboWin+\logs\format_101\config")
            //
            String format_101_config_dir = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR;
            final File dir_format_101_config = new File(format_101_config_dir);  
            dir_format_101_config.mkdir();
                    
            // create sub dir "format_101\log" (e.g. "C:\Program Files (x86)\TurboWin+\logs\format_101\log")  
            //
            String format_101_log_dir = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_LOG_DIR;
            final File dir_format_101_log = new File(format_101_log_dir);            
            dir_format_101_log.mkdir();
            
            // create sub dir "format_101\temp" (e.g. "C:\Program Files (x86)\TurboWin+\logs\format_101\temp")
            //
            String format_101_temp_dir = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + main.FORMAT_101_TEMP_DIR;
            final File dir_format_101_temp = new File(format_101_temp_dir);            
            dir_format_101_temp.mkdir();
                    

            // copy compression exe file (e.g teste_hc_TW.exe or teste_hc_TW_64)
            //
            try (InputStream is = getClass().getResourceAsStream(main.FORMAT_101_ROOT_DIR + "/" + compression_exe);  // NB in jar file: java.io.File.separator DO NOT WORK UNDER WINDOWS, MUST BE "/" !!!!!
                 OutputStream os = new FileOutputStream(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + compression_exe)   
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
               main.log_turbowin_system_message("[FORMAT-101] success when copying " + compression_exe + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + compression_exe);
            }
            catch (IOException ex) 
            {
               //info = "+++ error when copying " + COMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + COMPRESSION_EXE;
               //System.out.println(info);
               main.log_turbowin_system_message("[FORMAT-101] error when copying " + compression_exe + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + compression_exe);
            } 
            // on Linux systems set "allow executing file as program"
            main.OSType ostype = main.detect_OS();
            switch (ostype)
            {
               case LINUX: File f = new File(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + compression_exe); 
                           if (!f.canExecute())
                           {   
                              if (f.setExecutable(true, false))  // NB parameters: 1st=boolean executable, 2nd=boolean ownerOnly
                              {
                                 main.log_turbowin_system_message("[FORMAT-101] success when setting " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + compression_exe + " as executable");
                              }
                              else
                              {
                                 main.log_turbowin_system_message("[FORMAT-101] failed when setting " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + compression_exe + " as executable");
                              }
                           } // if (!f.canExecute())
                           break;
               default   : break;
            } // switch (ostype)
            
            
            // copy decompression exe file (e.g MAWSbin_TW.exe or MAWSbin_TW_64)
            //
            try (InputStream is = getClass().getResourceAsStream(main.FORMAT_101_ROOT_DIR + "/" + decompression_exe);  // NB in jar file: java.io.File.separator DO NOT WORK UNDER WINDOWS, MUST BE "/" !!!!!
                 OutputStream os = new FileOutputStream(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + decompression_exe)   
                ) 
            {
               // NB try-with-resource; resources (is and os) will be closed automatically when execution leaves the try block.
               
               int readBytes;
               byte[] buffer = new byte[4096];

               while ((readBytes = is.read(buffer)) > 0) 
               {
                  os.write(buffer, 0, readBytes);
               }
                     
               //info = "--- success when copying " + DECOMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + DECOMPRESSION_EXE;
               //System.out.println(info);
               main.log_turbowin_system_message("[FORMAT-101] success when copying " + decompression_exe + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + decompression_exe);
            }
            catch (IOException ex) 
            {
               //info = "+++ error when copying " + DECOMPRESSION_EXE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + DECOMPRESSION_EXE;
               //System.out.println(info);
               main.log_turbowin_system_message("[FORMAT-101] error when copying " + decompression_exe + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + decompression_exe);
            } 
            // on Linux systems set "allow executing file as program"
            switch (ostype)
            {
               case LINUX: File f = new File(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + decompression_exe); 
                           if (!f.canExecute())
                           {   
                              if (f.setExecutable(true, false))
                              {
                                 main.log_turbowin_system_message("[FORMAT-101] success when setting " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + decompression_exe + " as executable");
                              }
                              else
                              {
                                 main.log_turbowin_system_message("[FORMAT-101] failed when setting " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + decompression_exe + " as executable");
                              }
                           } // if (!f.canExecute())
                           break;
               default   : break;
            } //  switch (ostype)     
             
            
            // copy bufr table (S-AWS-101_modl_pilote.csv) [required for compression]
            //
            try (InputStream is = getClass().getResourceAsStream(main.FORMAT_101_ROOT_DIR + "/" + FORMAT_101_CONFIG_DIR + "/" + BUFR_TABLE);  // NB in jar file: java.io.File.separator DO NOT WORK UNDER WINDOWS, MUST BE "/" !!!!!
                 OutputStream os = new FileOutputStream(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + BUFR_TABLE)   
                ) 
            {
               // NB try-with-resource; resources (is and os) will be closed automatically when execution leaves the try block.
               
               int readBytes;
               byte[] buffer = new byte[4096];

               while ((readBytes = is.read(buffer)) > 0) 
               {
                  os.write(buffer, 0, readBytes);
               }
                     
               //info = "--- success when copying " + BUFR_TABLE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + BUFR_TABLE;
               //System.out.println(info);
               main.log_turbowin_system_message("[FORMAT-101] success when copying " + BUFR_TABLE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + BUFR_TABLE);
            }
            catch (IOException ex) 
            {
               //info = "+++ error when copying " + BUFR_TABLE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + BUFR_TABLE;
               //System.out.println(info);
               main.log_turbowin_system_message("[FORMAT-101] error when copying " + BUFR_TABLE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + BUFR_TABLE);
            } 
           
            // copy meta table (S-AWS-101_modl_meta.csv) [required for decompression]
            //
            try (InputStream is = getClass().getResourceAsStream(main.FORMAT_101_ROOT_DIR + "/" + FORMAT_101_CONFIG_DIR + "/" + META_TABLE);  // NB in jar file: java.io.File.separator DO NOT WORK UNDER WINDOWS, MUST BE "/" !!!!!
                 OutputStream os = new FileOutputStream(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + META_TABLE)   
                ) 
            {
               // NB try-with-resource; resources (is and os) will be closed automatically when execution leaves the try block.
               
               int readBytes;
               byte[] buffer = new byte[4096];

               while ((readBytes = is.read(buffer)) > 0) 
               {
                  os.write(buffer, 0, readBytes);
               }
                     
               //info = "--- success when copying " + META_TABLE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + META_TABLE;
               //System.out.println(info);
               main.log_turbowin_system_message("[FORMAT-101] success when copying " + META_TABLE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + META_TABLE);
            }
            catch (IOException ex) 
            {
               //info = "+++ error when copying " + META_TABLE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + META_TABLE;
               //System.out.println(info);
               main.log_turbowin_system_message("[FORMAT-101] error when copying " + META_TABLE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + META_TABLE);
            } 
            
            
            // copy data type (DataType.txt) [required for decompression]
            //
            try (InputStream is = getClass().getResourceAsStream(main.FORMAT_101_ROOT_DIR + "/" + FORMAT_101_CONFIG_DIR + "/" + DATA_TYPE);  // NB in jar file: java.io.File.separator DO NOT WORK UNDER WINDOWS, MUST BE "/" !!!!!
                 OutputStream os = new FileOutputStream(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + DATA_TYPE)   
                ) 
            {
               // NB try-with-resource; resources (is and os) will be closed automatically when execution leaves the try block.
               
               int readBytes;
               byte[] buffer = new byte[4096];

               while ((readBytes = is.read(buffer)) > 0) 
               {
                  os.write(buffer, 0, readBytes);
               }
                     
               //info = "--- success when copying " + DATA_TYPE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + DATA_TYPE;
               //System.out.println(info);
               main.log_turbowin_system_message("[FORMAT-101] success when copying " + DATA_TYPE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + DATA_TYPE);
            }
            catch (IOException ex) 
            {
               //info = "+++ error when copying " + DATA_TYPE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + DATA_TYPE;
               //System.out.println(info);
               main.log_turbowin_system_message("[FORMAT-101]error when copying " + DATA_TYPE + " from jar to: " + main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + DATA_TYPE); 
            } 
            
         } //  if (exeFile.exists() == false)
      } // if (doorgaan == true)
      
      
      return format_101_module_status;
   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private int write_HC_identification_file(String identifier)   
   {
      // NB this function is only necessary for decompression
      // NB the file (HC_IDENT_FILE) created by this function must be placed in the config dir (expected by the decompression C routines)
      
      // NB in offline_mode:  logs_dir = user_dir + java.io.File.separator + OFFLINE_LOGS_DIR;
      // NB in logs_dir always 'user_dir' already present (so a complete path)
      
      
      int exit_status = 0; 
      final String volledig_path_HC_ident_file = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + HC_IDENT_FILE;
      
      try
      {
         BufferedWriter out = new BufferedWriter(new FileWriter(volledig_path_HC_ident_file));
      
         // e.g. ;500;HC101;1;Test HC format 101;INMC_SC;S-AWS-101

         out.write(";");
         out.write(main.imo_number);     // Christophe Billon [email 11-04-2014]: MEMBER is a number for the ship to identify it - you can define it the way you want (normally it corresponds to the Inmarsat fleet member or Iridium IMEI).
         out.write(";");
         //out.write(main.call_sign); // or MASKED call sign ??????
         out.write(identifier);          // call sign or station ID or masked call sign (=masked station ID)
         out.write(";");
         out.write("1");
         out.write(";");
         out.write(main.ship_name);
         out.write(";");
         out.write("INMC_SC");
         out.write(";");
         out.write(TEMPLATE_NUMBER);    // TEMPLATE_NUMBER e.g. S-AWS-101
          
         out.newLine();   // newLine(): write a line separator. The line separator string is defined by the system property line.separator, and is not necessarily a single newline ('\n') character.
       
         out.close();
      } // try
      catch (Exception e)
      {
         //JOptionPane.showMessageDialog(null, "unable to write to: " + volledig_path_HC_ident_file, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         String info = "+++ unable to write to: " + volledig_path_HC_ident_file + " (function: write_HC_identification_file())";
         System.out.println(info);
         exit_status = 1;
      } // catch      
      
      return exit_status;
   }
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private int check_and_clear_format_101_temp_folder()
   {
      // NB in logs_dir always 'user_dir' already present (so a complete path)
      
      int exit_status = 0;
      
      
      // first check if sub dir temp present 
      final String temp_format_101_dir = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + main.FORMAT_101_TEMP_DIR;
      final File dirs = new File(temp_format_101_dir);

      if (dirs.exists() == false)
      {
         exit_status = 1;
         String info = "+++ clearing " + temp_format_101_dir + " not possible because dir not present (function: check_and_clear_format_101_temp_folder())";
         System.out.println(info);

      } // if (temp_logs_dir.trim().equals("") == true || temp_logs_dir.trim().length() < 2)
      else // temp sub dir present
      {
         // delete all the files in the temp dir (remains of a previous compress actions)
         final File file_logs_dir = new File(temp_format_101_dir + java.io.File.separator);
         String[] filenames = file_logs_dir.list(); // Returns an array of strings naming the files and directories in the directory denoted by this abstract pathname.

         for (int i = 0; i < filenames.length; i++)
         {
            File file_to_be_deleted = new File(temp_format_101_dir + java.io.File.separator + filenames[i]);
            if (file_to_be_deleted.delete() == false)
            {
               exit_status = 1;
               //JOptionPane.showMessageDialog(null, "delete error: " + filenames[i]  + " (compression temp files)"  , main.APPLICATION_NAME + " error", JOptionPane.ERROR_MESSAGE);
               String info = "+++ delete error: " + filenames[i]  + " (function: check_and_clear_format_101_temp_folder())";
               System.out.println(info);        
            }
         } // for (int i = 0; i < filenames.length; i++)
      } // else 
      
      
      return exit_status;
   }  
      
      
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void decompress_101()
   { 
      // e.g.: MAWSbin_TW.exe -n 1234567 -f temp/HPK_format_101.txt -i HC_ident.txt -h -r config -l -o temp/decompressed_FM13_message.txt > temp/decompressed.txt 
      //
      //
      // email Christophe Billon 9-4-2014:
      // MAWSbin_TW.exe -n [MEMBER] -f data/HPK_fic_data_test_hc_S-AWS-101_complete.txt -i HC_ident.txt -h -r config -l > data/decompressed.txt 
      // MEMBER is a number for the ship to identify it - you can define it the way you want (normally it corresponds to the Inmarsat fleet member or Iridium IMEI).
      //
      //
      // You also have to update  the HC_ident.txt file with a new line corresponding to the call sign PPPP and its  MEMBER number :
      //
      //; [MEMBER] ;PPPP;1; [ The name of the ship ]  ;INMC_SC;S-AWS-101
      
      // NB in logs_dir always 'user_dir' already present (so a complete path)
      
      
      final File exeFile        = new File(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + decompression_exe);
      final File log_outputFile = new File(String.format(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_LOG_DIR + java.io.File.separator + "log_decompression.txt"));
      
      final String volledig_path_format_101_compressed_file = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + main.FORMAT_101_TEMP_DIR + java.io.File.separator + "HPK_" + main.FORMAT_101_INPUT_FILE;// NB adding "HPK_" to the input file name is automatically done by the C-code compression functions
      final String volledig_path_decompressed_FM13_file = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + main.FORMAT_101_TEMP_DIR + java.io.File.separator + "do_not_send_decompressed_FM13_obs.txt";
      
      final List<String> args = new ArrayList<>();                
      args.add(exeFile.getAbsolutePath());
      args.add("-n");
      args.add(main.imo_number);
      args.add("-f");
      args.add(volledig_path_format_101_compressed_file);  
      args.add("-i");
      args.add(HC_IDENT_FILE);
      args.add("-r");
      args.add(FORMAT_101_CONFIG_DIR);
      args.add("-h");                                   // mandatory
      args.add("-l");
      args.add("-o");
      args.add(volledig_path_decompressed_FM13_file);
      
      // print the complete argument list (only for checking in output console)
      System.out.println();
      System.out.println("Decompression process argument list:");
      for (int i = 0; i < args.size(); i++) 
      {
         System.out.print("[" + i + "] = ");
         System.out.println(args.get(i));
      }
      System.out.println();
      
      final ProcessBuilder processBuilder = new ProcessBuilder(args);
      
      // Redirect any output (including error) to a file. This avoids deadlocks when the buffers get full. 
      processBuilder.redirectErrorStream(true);
      processBuilder.redirectOutput(log_outputFile);

      // Set the working directory. The exe file will run as if you are in this directory.
      processBuilder.directory(new File(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR));

      // Start the process and wait for it to finish. 
      Process process = null;
      try 
      {
         process = processBuilder.start();
         
         int exitStatus = 0;
         try 
         {
            // NB waitFor
            // Causes the current thread to wait, if necessary, until the process represented by this Process object has terminated. 
            // This method returns immediately if the subprocess has already terminated. If the subprocess has not yet terminated, 
            // the calling thread will be blocked until the subprocess exits.
            //
            //Returns:
            //    the exit value of the subprocess represented by this Process object. By convention, the value 0 indicates normal termination.
            exitStatus = process.waitFor();
         } 
         catch (InterruptedException ex) 
         {
            String info = "InterruptedException in function decompress (" + ex + ")";
            System.out.println(info);
            //JOptionPane.showMessageDialog(null, "format 101 decompress error; " + info, main.APPLICATION_NAME, JOptionPane.INFORMATION_MESSAGE);
         }
         System.out.println("Decompression process finished with status: " + exitStatus);   
      } // try
      catch (IOException ex) 
      {
         String info = "IOException in function decompress (" + ex + ")";
         System.out.println(info);
         //JOptionPane.showMessageDialog(null, "format 101 decompress error; " + info, main.APPLICATION_NAME, JOptionPane.INFORMATION_MESSAGE);

      } // catch
      
   }
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void compress_101(String identifier)
   {
      // NB in logs_dir always 'user_dir' already present (so a complete path)
      
      // The compression .exe file to execute
      final File exeFile = new File(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + compression_exe);

      // The output log file. All activity is written to this file
      //final File log_outputFile = new File(String.format(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_LOG_DIR + java.io.File.separator + "log_compression_%tY%<tm%<td_%<tH%<tM%<tS.txt", System.currentTimeMillis()));
      // NB System.currentTimeMillis() give the number of milliseconds since the unix epoch, which is a fixed point in time, not affected by your local time zone.
      final File log_outputFile = new File(String.format(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_LOG_DIR + java.io.File.separator + "log_compression.txt"));
 
      final String volledig_path_bufr_file = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_CONFIG_DIR + java.io.File.separator + BUFR_TABLE;
      
      final List<String> args = new ArrayList<>();                
      args.add(exeFile.getAbsolutePath());
      //args.add(FORMAT_101_CONFIG_DIR + java.io.File.separator + BUFR_TABLE);
      args.add(volledig_path_bufr_file);
      args.add(SOURCE_INPUT_COMPRESSION);        // nb doesn't work with a java separator (probably due to the working of the semi comression C program) !!
      args.add(identifier);                 // call sign or marked call sign; NB identifiers > 7 characters will be cut off by the compression program
      args.add(TEMPLATE_NUMBER);            // "S-AWS-101";
             
      // print the complete argument list (only for checking in output console)
      System.out.println();
      System.out.println("Compression process argument list:");
      for (int i = 0; i < args.size(); i++) 
      {
         System.out.print("[" + i + "] = ");
         System.out.println(args.get(i));
      }
      System.out.println();
      
      
      final ProcessBuilder processBuilder = new ProcessBuilder(args);
      
      // Redirect any output (including error) to a file. This avoids deadlocks
      // when the buffers get full. 
      processBuilder.redirectErrorStream(true);
      processBuilder.redirectOutput(log_outputFile);

      // Add a new environment variable
      //processBuilder.environment().put("message", "Example of process builder");

      // Set the working directory. The exe file will run as if you are in this directory.
      processBuilder.directory(new File(main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR));

      // Start the process and wait for it to finish. 
      Process process = null;
      try 
      {
         process = processBuilder.start();
         
         int exitStatus = 0;
         try 
         {
            // NB waitFor
            // Causes the current thread to wait, if necessary, until the process represented by this Process object has terminated. 
            // This method returns immediately if the subprocess has already terminated. If the subprocess has not yet terminated, 
            // the calling thread will be blocked until the subprocess exits.
            //
            //Returns:
            //    the exit value of the subprocess represented by this Process object. By convention, the value 0 indicates normal termination.
            exitStatus = process.waitFor();
         } 
         catch (InterruptedException ex) 
         {
            //"Logger.getLogger(FORMAT_101.class.getName()).log(Level.SEVERE, null, ex);
            String info = "InterruptedException in function compress (" + ex + ")";
            System.out.println(info);
            JOptionPane.showMessageDialog(null, "format 101 compress error; " + info, main.APPLICATION_NAME, JOptionPane.INFORMATION_MESSAGE);
         }
         System.out.println("Compression process finished with status: " + exitStatus);   
      } // try
      catch (IOException ex) 
      {
         //Logger.getLogger(FORMAT_101.class.getName()).log(Level.SEVERE, null, ex);
         String info = "IOException in function compress (" + ex + ")";
         System.out.println(info);
         JOptionPane.showMessageDialog(null, "format 101 compress error; " + info, main.APPLICATION_NAME, JOptionPane.INFORMATION_MESSAGE);

      } // catch
      
   } // public void compress()
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void write_input_for_101_compression()
   {   
      double compressed_format_identifier;                                  // new in format 101
      double compressed_call_sign_encryption_indicator;                     // new in format 101
      double compressed_ship_direction;
      double compressed_ship_speed;
      double compressed_year;                                               // new in format 101
      double compressed_month;                                              // new in format 101
      double compressed_day;
      double compressed_hour;
      double compressed_minute;                                             // new in format 101
      double compressed_latitude;
      double compressed_longitude;
      //double compressed_wind_speed_indicator;                             // not in format 101 anymore
      //double compressed_sst_method;                                       // not in format 101 anymore
      //double compressed_method_wet_bulb;                                  // not in format 101 anymore
      double compressed_cloud_base;
      double compressed_visibility;
      double compressed_cloud_cover;
      double compressed_wind_dir;
      double compressed_wind_speed;
      double compressed_air_temp;
      double compressed_dewpoint;
      double compressed_wet_bulb_temp;
      double compressed_rv;
      double compressed_pressure_msl;
      double compressed_pressure_characteristic;
      double compressed_pressure_change;
      double compressed_present_weather;
      double compressed_past_weather_1;
      double compressed_past_weather_2;
      double compressed_cloud_amount_low_medium;
      double compressed_Cl;
      double compressed_Cm;
      double compressed_Ch;
      double compressed_sst;
      double compressed_period_wind_waves_estimated;
      double compressed_height_wind_waves_estimated;
      double compressed_dir_swell_1_estimated;
      double compressed_dir_swell_2_estimated;
      double compressed_period_swell_1_estimated;
      double compressed_period_swell_2_estimated;
      double compressed_height_swell_1_estimated;
      double compressed_height_swell_2_estimated;
      double compressed_thickness_ice_accretion;
      double compressed_rate_ice_accretion;
      double compressed_cause_ice_accretion;
      double compressed_sea_ice_concentration;
      double compressed_ice_development;
      double compressed_amount_type_ice;
      double compressed_ice_edge_bearing;
      double compressed_ice_situation;
      double compressed_ice_obs_presence_indicator;                         // new in format 101
      double compressed_visual_obs_presence_indicator;                      // new in format 101
      double compressed_waves_obs_presence_indicator;                       // new in format 101
      double compressed_sll_wl_difference;                                  // new in format 101
      double compressed_true_heading;                                       // new in format 101
      double compressed_pressure_height;                                    // new in format 101
      double compressed_relative_wind_dir;                                  // new in format 101
      double compressed_relative_wind_speed;                                // new in format 101
      double compressed_max_wind_gust_speed;                                // new in format 101
      double compressed_max_wind_gust_dir;                                  // new in format 101
   
   
   
      //final int NBMAXELEM = 100;//1024;                                   // see compression.h [TurboWin]
      final int NBMAXELEM = 57;                                             // 0 - 56 incl. array places (fixed number in TurboWin+)
      final int COMPRESSED_UNDEF_VALUE = 0;                                 // see tmycompression.cpp [TurboWin]
   
      //int mode_oper;
      int[] present = new int[NBMAXELEM];   
      double[] val  = new double[NBMAXELEM];   
      double schalings_factor; 
      int pos_description;
      String[] description = new String[NBMAXELEM]; 
      final double omzet_kn_ms = 0.5144444;
      
      
      // if masked call sign (VOS ID) iserted -> use this for obs else 'normal' call sign
      //
//      if ((main.masked_call_sign != null) && (main.masked_call_sign.trim().length() > 0))
//      {   
//         coded_obs_call_sign = main.masked_call_sign;
//      }
//      else if ((main.call_sign != null) && (main.call_sign.trim().length() > 0))
//      {
//         coded_obs_call_sign = main.call_sign;
//      }   
//      else
//      {
//         coded_obs_call_sign = "unknown";
//     }
         
      
      // initialisation
      description[0]  = "format identifier";
      description[1]  = "call sign encryption indicator";
      description[2]  = "COG past 10 minutes [degrees]";
      description[3]  = "SOG past 10 minutes [m/s]";
      description[4]  = "heading [degrees]";
      description[5]  = "distance WL - SLL [metres]";
      description[6]  = "year";
      description[7]  = "month";
      description[8]  = "day";
      description[9]  = "hour";
      description[10] = "minute";
      description[11] = "latitude [degrees]";
      description[12] = "longitude [degrees]";
      description[13] = "pressure at barometer height [Pa]";
      description[14] = "pressure at MSL [Pa]";
      description[15] = "3 hour pressure change [Pa]";
      description[16] = "characteristic of pressure change [code]";
      description[17] = "true wind direction [degrees]";
      description[18] = "true wind speed [m/s]";
      description[19] = "relative wind direction [degrees]";
      description[20] = "relative wind speed [m/s]";
      description[21] = "maximum wind gust speed [m/s]";
      description[22] = "maximum wind gust direction [degrees]";
      description[23] = "air temperature [K]";
      description[24] = "wet bulb temperature [K]";
      description[25] = "dew point temperature [K]";
      description[26] = "relative humidity [%]";
      description[27] = "sea water temperature [K]";
      description[28] = "visual obs. precense indicator";
      description[29] = "visibility (VV) [code]";
      description[30] = "present weather (ww) [code]";
      description[31] = "past weather 1 (W1) [code]";
      description[32] = "past weather 2 (W2) [code]";
      description[33] = "total cloud cover [code]";
      description[34] = "cloud amount (low) [code]";
      description[35] = "cloud type low (Cl) [code]";
      description[36] = "cloud type middle (Cm) [code]";
      description[37] = "cloud type high (Ch) [code]";
      description[38] = "height of base of lowest clouds [code]";
      description[39] = "wave obs. presence indicator";
      description[40] = "period of wind waves [seconds]";
      description[41] = "height of wind waves [metres]";
      description[42] = "direction of 1st swell [degree])";
      description[43] = "period of 1st swell [seconds]";
      description[44] = "height of 1st swell [metres]";
      description[45] = "direction of 2nd swell [degrees]";
      description[46] = "period of 2nd swell [seconds]]";
      description[47] = "height of 2nd swell [metres]";
      description[48] = "ice obs. precence indicator";
      description[49] = "ice deposit/thickness (EsEs) [metres]";
      description[50] = "rate of ice accretion (Rs) [code]";
      description[51] = "cause of ice accretion (Is) [code]"; 
      description[52] = "sea ice concentration (ci) [code]";
      description[53] = "amount and type of ice (bi) [code]";
      description[54] = "ice situation (zi) [code]";
      description[55] = "ice development (Si) code]";
      description[56] = "bearing of ice edge (Di) [degrees]";
      
      
      // initialisation 
      for (int i = 0; i < NBMAXELEM; i++)
      {
         val[i] = COMPRESSED_UNDEF_VALUE;
         present[i] = 0;                                  // 0 = not present (1 = present)
      } // for (int i = 0; i < NBMAXELEM; i++)


      // initialisatie 
      //mode_oper = 0;                                      // see email email Christophe Billon 5-03-2014


      // NB units and scaling according: S-AWS-101_modl_pilote.csv 


      // [0]
      ////////////////////////////////////////// 0 01 198 (format identifier)  ///////////////////////////////////////
      //
      compressed_format_identifier = 101;
      present[0] = 1;


      // [1]
      ///////////////////////////////////// 0 01 199 (call sign encryption indicator)  ///////////////////////////////
      //

      //MessageBox(obs_encryption_identifier.c_str(), "obs_encryption_identifier", MB_OK);

      //  NB oorspronkelijk was het de bedoeling dat:  [zie email Pierre Blouch van 5-03-2014]    0 : No callsign encryption required   en  1 : Callsign encryption required
      //     echter de 1 wordt een ABSENT in de compressed message (dus 1 en "niet aanwezig" dan niet meer van elkaar te onderscheiden) ddarom nu:
      //     [zie email Christophe Billon 11-4-2014] Jean-Baptiste Cohuet asked Sterela to modify the call sign encryption definition, so that now it is :
      //         0 : encryption asked
      //         1 : no encryption
      //

      if (main.obs_101_encryption.equals(main.FORMAT_101_ENCRYPTION_YES))
      {
         compressed_call_sign_encryption_indicator = 0;
         present[1] = 1;
      }
      else
      {
         compressed_call_sign_encryption_indicator = 1;
         present[1] = 1;
      }


      // [2]
      ///////////////////////////////////////////// 0 01 012 (course made good; Ds)  ///////////////////////////////
      // NOTE in case format 101 past 10 minutes (BUFR: Descriptors 001012 and 001013 may relate to parameters of various meanings and the corresponding values may be integrated on different periods.
      //
      // unit = degrees true 
      schalings_factor = 1;                                        // scaling (scale factor)
      
      if (myposition.COG_APR >= 0.0 && myposition.COG_APR <= 360.0)
      {
         // if in APR mode the high resolution COG_APR is available (eg Mintaka Star) use then this value
         compressed_ship_direction = (double)myposition.COG_APR * schalings_factor;
         present[2] = 1;
      }
      else // (COG_APR not available)
      {
         if (myposition.Ds_code.equals("0") == true)
         {
            compressed_ship_direction = (double)0 * schalings_factor;
            present[2] = 1;
         }
         else if (myposition.Ds_code.equals("1") == true)
         {
            compressed_ship_direction = (double)45 * schalings_factor;
            present[2] = 1;
         }
         else if (myposition.Ds_code.equals("2") == true)   
         {
            compressed_ship_direction = (double)90 * schalings_factor;
            present[2] = 1;
         }
         else if (myposition.Ds_code.equals("3") == true)
         {
            compressed_ship_direction = (double)135 * schalings_factor;
            present[2] = 1;
         }
         else if (myposition.Ds_code.equals("4") == true)
         {
            compressed_ship_direction = (double)180 * schalings_factor;
            present[2] = 1;
         }
         else if (myposition.Ds_code.equals("5") == true)   
         {
            compressed_ship_direction = (double)225 * schalings_factor;
            present[2] = 1;
         }
         else if (myposition.Ds_code.equals("6") == true)
         {
            compressed_ship_direction = (double)270 * schalings_factor;
            present[2] = 1;
         }
         else if (myposition.Ds_code.equals("7") == true)   
         {
            compressed_ship_direction = (double)315 * schalings_factor;
            present[2] = 1;
         }
         else if (myposition.Ds_code.equals("8") == true)   
         {
            compressed_ship_direction = (double)360 * schalings_factor;
            present[2] = 1;
         }
         else if (myposition.Ds_code.equals("9") == true)                  
         {
            compressed_ship_direction = COMPRESSED_UNDEF_VALUE;
            present[2] = 0;
         }
         else
         {
            compressed_ship_direction = COMPRESSED_UNDEF_VALUE;
            present[2] = 0;
         }
      } // else (COG_APR not available)

      
      /* [3]
      ////////////////////////////////////////// 0 01 013 (speed made good; Vs) /////////////////////////////////////
      // NOTE in case format 101 past 10 minutes (BUFR: Descriptors 001012 and 001013 may relate to parameters of various meanings and the corresponding values may be integrated on different periods.)
      */
      /* unit = m/s */
      schalings_factor = 1;
      int gem_Vs = Integer.MAX_VALUE;
      
      if (myposition.SOG_APR >= 0.0 && myposition.SOG_APR <= 100.0) // NB SOG_APR in knots
      {
         // if in APR mode the high resolution SOG_APR is available (eg Mintaka Star) use then this value
         BigDecimal bd_SOG_APR = new BigDecimal((double)myposition.SOG_APR * omzet_kn_ms * schalings_factor). setScale(3, RoundingMode.HALF_UP); // three decimals // omzet_kn_ms = 0.5144444
         compressed_ship_speed = bd_SOG_APR.doubleValue();
         present[3] = 1;
      }
      else // SOG_APR not available
      {
         if (myposition.vs_code.equals("0"))
         {
            gem_Vs = 0;                // knots
         }
         else if (myposition.vs_code.equals("1"))
         {
            gem_Vs = 3;                // knots
         }
         else if (myposition.vs_code.equals("2"))
         {
            gem_Vs = 8;                // knots
         }
         else if (myposition.vs_code.equals("3"))
         {
            gem_Vs = 13;               // knots
         }
         else if (myposition.vs_code.equals("4"))
         {
            gem_Vs = 18;               // knots
         }
         else if (myposition.vs_code.equals("5"))
         {
            gem_Vs = 23;               // knots
         }
         else if (myposition.vs_code.equals("6"))
         {
            gem_Vs = 28;               // knots
         }
         else if (myposition.vs_code.equals("7"))
         {   
            gem_Vs = 33;               // knots
         }
         else if (myposition.vs_code.equals("8"))
         {   
            gem_Vs = 38;               // knots
         }
         else if (myposition.vs_code.equals("9"))    // tot versie 3.0.9: else if (myposition.vs_code.equals("2") == true) 
         {
            gem_Vs = 42;               // knots
         }
         else
         {
            gem_Vs = Integer.MAX_VALUE;
         }   

         if (gem_Vs != Integer.MAX_VALUE)
         {
            //compressed_ship_speed = (double)gem_Vs * omzet_kn_ms * schalings_factor;                // omzet_kn_ms = 0.5144444
            BigDecimal bd_vs = new BigDecimal((double)gem_Vs * omzet_kn_ms * schalings_factor). setScale(3, RoundingMode.HALF_UP); // three decimals // omzet_kn_ms = 0.5144444
            compressed_ship_speed = bd_vs.doubleValue();
            present[3] = 1;
         }
         else
         {
            compressed_ship_speed = COMPRESSED_UNDEF_VALUE;
            present[3] = 0;
         }
      } // else (SOG_APR not available)
      
      /* [4]
      ////////////////////////////////////////// 0 01 044 (true heading) /////////////////////////////////////
      */
      /* unit  = degrees */
      schalings_factor = 1;

      if (mywind.ship_heading.trim().length() > 0)
      {
         try
         {
            double double_ship_heading = Double.parseDouble(mywind.ship_heading.trim());
   
            if ( (double_ship_heading >= 1.0) && (double_ship_heading <= 360.0) )
            {
               compressed_true_heading = double_ship_heading * schalings_factor;              // HDG = heading
               present[4] = 1;
            }
            else
            {
               compressed_true_heading = COMPRESSED_UNDEF_VALUE;
               present[4] = 0;
            }
         } // try
         catch (NumberFormatException ex)
         {
            compressed_true_heading = COMPRESSED_UNDEF_VALUE;
            present[4] = 0;
            System.out.println("+++ write input file for format 101; true heading " + ex);
         } // catch
      }
      else
      {
         compressed_true_heading = COMPRESSED_UNDEF_VALUE;
         present[4] = 0;
      }
      

      /* [5]
      ////////////////////////////////////// 0 07 072 (sll - wl distance) ///////////////////////////////////
      */
      /* unit = metres */
      schalings_factor = 1;

      if ( (mywind.int_difference_sll_wl > -50) && (mywind.int_difference_sll_wl < 100) )      // very raw/save limits (for real limits see mywind.java)
      {
         compressed_sll_wl_difference = (double)mywind.int_difference_sll_wl * schalings_factor;
         present[5] = 1;
      }
      else
      {
         compressed_sll_wl_difference = COMPRESSED_UNDEF_VALUE;
         present[5] = 0;
      }

      
      /* [6]
      ////////////////////////////////////////////// 0 04 001 (year) /////////////////////////////////////////
      */
      /* unit = N/A */
      schalings_factor = 1;

      compressed_year = Double.parseDouble(mydatetime.year) * schalings_factor;
      present[6] = 1;
      
      
      /* [7]
      /////////////////////////////////////////////// 0 04 002 (months) ///////////////////////////////////////
      */
      /* unit = N/A */
      schalings_factor = 1;

      compressed_month = Double.parseDouble(mydatetime.MM_code) * schalings_factor;
      present[7] = 1;

   
      /* [8]
      ////////////////////////////////////////////////// 0 04 003 (day) ///////////////////////////////////////
      */
      /* unit  = N/A */
      schalings_factor = 1;

      compressed_day = Double.parseDouble(mydatetime.day) * schalings_factor;
      present[8] = 1;
   
   
      /* [9]
      //////////////////////////////////////////////// 0 04 004 (hour) ////////////////////////////////////////
      */
      /* unit  = N/A */
      schalings_factor = 1;

      compressed_hour = Double.parseDouble(mydatetime.hour) * schalings_factor;
      present[9] = 1;      
      
      
      /* [10]
      /////////////////////////////////////////////// 0 04 005 (minute) ////////////////////////////////////////
      */
      compressed_minute = 0.0;         // by default
      present[10] = 1;
      
      

      /* [11]
      //////////////////////////////////////////////// 0 05 002   latitude (coarse accurancy) ////////////////////////////////////////
      */
      /* unit  = degrees */
      schalings_factor = 1;  
      
      //compressed_latitude = ((double)myposition.int_latitude_degrees + (double)myposition.int_latitude_minutes / 60) * schalings_factor;   
      BigDecimal bd_lat = new BigDecimal(((double)myposition.int_latitude_degrees + (double)myposition.int_latitude_minutes / 60) * schalings_factor). setScale(3, RoundingMode.HALF_UP); // three decimals 
      compressed_latitude = bd_lat.doubleValue();
      
      if (myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true)
      {
         compressed_latitude *= -1;
      }
      present[11] = 1;
   
   
      /* [12]
      //////////////////////////////////////////////// 0 06 002   longitude (coarse accurancy) ////////////////////////////////////////
      */
      /* unit  = degrees */
      schalings_factor = 1;  
      
      //compressed_longitude = ((double)myposition.int_longitude_degrees + (double)myposition.int_longitude_minutes / 60) * schalings_factor;   
      BigDecimal bd_lon = new BigDecimal(((double)myposition.int_longitude_degrees + (double)myposition.int_longitude_minutes / 60) * schalings_factor). setScale(3, RoundingMode.HALF_UP); // three decimals // omzet_kn_ms = 0.5144444
      compressed_longitude = bd_lon.doubleValue();
      
      if (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true)
      {
         compressed_longitude *= -1;
      }
      present[12] = 1;
      
  
      /* [13]
      ////////////////////////////////////// 0 10 004 pressure barometer height  //////////////////////////////////
      */
      /* unit  = Pa */
      schalings_factor = 100;    
   
      // ONLY include pressure at baromter height if measured at this height (so not corrected by PMOs to MSL) !!
      if (main.pressure_reading_msl_yes_no.equals(main.PRESSURE_READING_MSL_NO))
      {
         if ((mybarometer.pressure_reading_corrected.trim()).length() > 0)
         {
            try
            {
               double double_pressure_reading_corrected = Double.parseDouble(mybarometer.pressure_reading_corrected);
               if ( (double_pressure_reading_corrected > 800.0) && (double_pressure_reading_corrected < 1100.0) )       // raw check (for real limits see mybarometer.java)
               {
                  compressed_pressure_height = double_pressure_reading_corrected * schalings_factor;
                  present[13] = 1;
               }
               else
               {
                  compressed_pressure_height = COMPRESSED_UNDEF_VALUE;
                  present[13] = 0;
               } 
            } // try
            catch (NumberFormatException ex)
            {
               compressed_pressure_height = COMPRESSED_UNDEF_VALUE;
               present[13] = 0;
               System.out.println("+++ write input file for format 101; pressure at barometer height " + ex);
            } // catch        
         }
         else
         {
            compressed_pressure_height = COMPRESSED_UNDEF_VALUE;
            present[13] = 0;
         }
      }
      else
      {
         compressed_pressure_height = COMPRESSED_UNDEF_VALUE;
         present[13] = 0;
      }      
      
      
      /* [14]
      //////////////////////////////////////////////// 0 10 051 pressure MSL  /////////////////////////////////////
      */
      /* unit  = Pa */
      schalings_factor = 100;
      
      if ((mybarometer.pressure_msl_corrected.trim()).length() > 0)
      {
         try
         {
            double double_pressure_msl_corrected = Double.parseDouble(mybarometer.pressure_msl_corrected);
            if ( (double_pressure_msl_corrected > 800.0) && (double_pressure_msl_corrected < 1100.0) )       // raw check (for real limits see mybarometer.java)
            {
               compressed_pressure_msl = double_pressure_msl_corrected * schalings_factor;
               present[14] = 1;
            }
            else
            {   
               compressed_pressure_msl = COMPRESSED_UNDEF_VALUE;
               present[14] = 0;
            }
         } // try
         catch (NumberFormatException ex)
         {
            compressed_pressure_msl = COMPRESSED_UNDEF_VALUE;
            present[14] = 0;
            System.out.println("+++ write input file for format 101; MSL pressure " + ex);
         } // catch   
      }   
      else
      {   
         compressed_pressure_msl = COMPRESSED_UNDEF_VALUE;
         present[14] = 0;
      }      
   
      
      /* [15]
      //////////////////////////////////////////////// 0 10 061 pressure change (ppp) ////////////////////////////////////
      */
      /* unit  = Pa */
      schalings_factor = 100;   

      if ((mybarograph.pressure_amount_tendency.trim()).length() > 0)   
      {
         if ((mybarograph.a_code.trim()).length() > 0) // we need 'a' because ppp has no sign on the input form (characteristic defines the sign)
         {
            try
            {   
               //double double_pressure_amount_tendency = Double.parseDouble(mybarograph.pressure_amount_tendency); 
               //compressed_pressure_change = double_pressure_amount_tendency * schalings_factor;
               BigDecimal bd_ppp = new BigDecimal(Double.parseDouble(mybarograph.pressure_amount_tendency) * schalings_factor). setScale(3, RoundingMode.HALF_UP); // three decimals 
               compressed_pressure_change = bd_ppp.doubleValue();              
               
               int int_a_code = Integer.parseInt(mybarograph.a_code);
               if (int_a_code == 5 || int_a_code == 6 || int_a_code == 7 || int_a_code == 8)
               {
                  compressed_pressure_change *= -1;
               }
               present[15] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_pressure_change = COMPRESSED_UNDEF_VALUE;
               present[15] = 0;
               System.out.println("+++ write input file for format 101; pressure change (ppp) " + ex);
            } // catch
         } // if ((mybarograph.a_code.trim()).length() > 0)
         else
         {
            compressed_pressure_change = COMPRESSED_UNDEF_VALUE;
            present[15] = 0;
         }
      }
      else
      {
         compressed_pressure_change = COMPRESSED_UNDEF_VALUE;
         present[15] = 0;
      }
      
   
      /* [16]
      //////////////////////////////////////////////// 0 10 063 pressure characteristic (a) //////////////////////////////
      */
      /* unit  = code (bufr table 010063) */
      schalings_factor = 1;
   
      if ((mybarograph.a_code.trim()).length() > 0)
      {
         try
         {   
            double double_a_code = Double.parseDouble(mybarograph.a_code);
            compressed_pressure_characteristic = double_a_code * schalings_factor;
            present[16] = 1;
         } // try
         catch (NumberFormatException ex)
         {
            compressed_pressure_characteristic = COMPRESSED_UNDEF_VALUE;
            present[16] = 0;
            System.out.println("+++ write input file for format 101; pressure characteristic (a) " + ex);
         } // catch
      }
      else
      {
         compressed_pressure_characteristic = COMPRESSED_UNDEF_VALUE;
         present[16] = 0;
      }

      
      /* [17]
      //////////////////////////////////////////////// 0 11 001 (true wind direction)   ///////////////////////////////////
      */
      /* unit  = degrees true */
      schalings_factor = 1;                    
      
      if (mywind.int_true_wind_dir >= 0 && mywind.int_true_wind_dir <= 360) 
      {
         compressed_wind_dir = (double)mywind.int_true_wind_dir * schalings_factor;
         present[17] = 1;
      }
      else if (mywind.int_true_wind_dir == mywind.WIND_DIR_VARIABLE)      // 0 agreed with Christophe Billon in email of 7-03-2014
      {
         compressed_wind_dir = 0.0;
         present[17] = 1;
      }
      else
      {
         compressed_wind_dir = COMPRESSED_UNDEF_VALUE;
         present[17] = 0;
      }
      
      
      /* [18]
      //////////////////////////////////////////////// 0 11 002 (true wind speed)   ///////////////////////////////////
      */
      /* unit  = m/s */
      schalings_factor = 1;
      
      if (mywind.int_true_wind_speed >= 0 && mywind.int_true_wind_speed <= 500)   
      {
         if (main.wind_units.trim().indexOf(main.M_S) != -1)                // so wind speed in m/s
         {
            compressed_wind_speed = (double)mywind.int_true_wind_speed * schalings_factor; 
            present[18] = 1;
         }
         else if (main.wind_units.trim().indexOf(main.KNOTS) != -1)         // so wind speed units knots
         {
            //compressed_wind_speed = (double)mywind.int_true_wind_speed * main.KNOT_M_S_CONVERSION * schalings_factor; // from knots -> m/s
            BigDecimal bd_wind = new BigDecimal((double)mywind.int_true_wind_speed * main.KNOT_M_S_CONVERSION * schalings_factor). setScale(3, RoundingMode.HALF_UP); // three decimals 
            compressed_wind_speed = bd_wind.doubleValue();
            present[18] = 1;
         }
         else
         {
            compressed_wind_speed = COMPRESSED_UNDEF_VALUE;
            present[18] = 0;
         }
      }
      else
      {
         compressed_wind_speed = COMPRESSED_UNDEF_VALUE;
         present[18] = 0;
      }
      
         
      /* [19]
      //////////////////////////////////////////// 0 11 099 (relative wind dir, RWD)   //////////////////////////////
      */
      /* unit  = degrees */
      schalings_factor = 1;                    // already in degrees 

      if ((mywind.RWD_code.trim()).length() > 0)  
      {
         if (mywind.RWD_code.equals("///"))
         {
            compressed_relative_wind_dir = COMPRESSED_UNDEF_VALUE;
            present[19] = 0;
         }   
         else if (mywind.RWD_code.equals("999"))     // variable for RWD, see mywind.java
         {
            compressed_relative_wind_dir = 0.0;      // in format 101 no option for variable so agreed to insert 0.0
            present[19] = 1;
         }
         else
         {
            try
            {
               compressed_relative_wind_dir = Double.parseDouble(mywind.RWD_code); 
               present[19] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_relative_wind_dir = COMPRESSED_UNDEF_VALUE;
               present[19] = 0;
               System.out.println("+++ write input file for format 101; relative wind dir " + ex);
            } // catch
         } // else
      } //  if ((mywind.RWD_code.trim()).length() > 0)
      else
      {
         compressed_relative_wind_dir = COMPRESSED_UNDEF_VALUE;
         present[19] = 0;
      } // else
   
   
      /* [20]
      ////////////////////////////////////////// 0 11 100 (relative wind speed, RWS)   //////////////////////////////
      */
      /* unit  = m/s */
      schalings_factor = 1;
      
      if ((mywind.RWS_code.trim()).length() > 0)  
      {
         if (mywind.RWS_code.equals("///"))
         {
            compressed_relative_wind_speed = COMPRESSED_UNDEF_VALUE;
            present[20] = 0;
         }   
         else
         {
            try
            {
               // NB if necessary first convert RWS to m/s!!
               if (main.wind_units.trim().indexOf(main.M_S) != -1)                // so wind speed in m/s
               {
                  compressed_relative_wind_speed = Double.parseDouble(mywind.RWS_code) * schalings_factor; 
                  present[20] = 1;
               }
               else if (main.wind_units.trim().indexOf(main.KNOTS) != -1)         // so wind speed units knots
               {
                  //compressed_wind_speed = (double)mywind.int_true_wind_speed * main.KNOT_M_S_CONVERSION * schalings_factor; // from knots -> m/s
                  BigDecimal bd_wind = new BigDecimal(Double.parseDouble(mywind.RWS_code) * main.KNOT_M_S_CONVERSION * schalings_factor). setScale(3, RoundingMode.HALF_UP); // three decimals 
                  compressed_relative_wind_speed = bd_wind.doubleValue();
                  present[20] = 1;
               }
               else
               {
                  compressed_relative_wind_speed = COMPRESSED_UNDEF_VALUE;
                  present[20] = 0;
               }               
            } // try
            catch (NumberFormatException ex)
            {
               compressed_relative_wind_speed = COMPRESSED_UNDEF_VALUE;
               present[20] = 0;
               System.out.println("+++ write input file for format 101; relative wind speed " + ex);
            } // catch
         } // else
      } //  if ((mywind.RWD_code.trim()).length() > 0)
      else
      {
         compressed_relative_wind_speed = COMPRESSED_UNDEF_VALUE;
         present[20] = 0;
      } // else
      
           
      /* [21]
      ////////////////////////////////////////// 0 11 041 (max wind gust speed)   ///////////////////////////////////
      */
      /* unit  = m/s */
      compressed_max_wind_gust_speed = COMPRESSED_UNDEF_VALUE;        // by default for VOS


      /* [22]
      ////////////////////////////////////////// 0 11 043 (max wind gust dir)   /////////////////////////////////////
      */
      /* unit  = degrees */
      compressed_max_wind_gust_dir = COMPRESSED_UNDEF_VALUE;        // by default for VOS
   
      
      /* [23]
      ////////////////////////////////////////// 0 12 101  air temp   //////////////////////////////////
      */
      /* unit  = Kelvin */
      schalings_factor = 1;      
      
      if ((mytemp.air_temp.trim()).length() > 0)  
      {
         try
         {
            //compressed_air_temp = Double.parseDouble(mytemp.air_temp) * schalings_factor + CELCIUS_TO_KELVIN_FACTOR;
            BigDecimal bd_air_temp = new BigDecimal(Double.parseDouble(mytemp.air_temp) * schalings_factor + CELCIUS_TO_KELVIN_FACTOR). setScale(3, RoundingMode.HALF_UP); // three decimals 
            compressed_air_temp = bd_air_temp.doubleValue();
            present[23] = 1;
         } // try
         catch (NumberFormatException ex)
         {
            compressed_air_temp = COMPRESSED_UNDEF_VALUE;
            present[23] = 0;
            System.out.println("+++ write input file for format 101; air temp " + ex);
         } // catch            
      } //  
      else
      {
         compressed_air_temp = COMPRESSED_UNDEF_VALUE;
         present[23] = 0;
      }
      
  
      /* [24]
      //////////////////////////////////////////////// 0 12 102  wet bulb    ///////////////////////////////
      */   
      /* unit  = Kelvin */
      schalings_factor = 1;
      
      if ((mytemp.wet_bulb_temp.trim()).length() > 0)  
      {
         try
         {
            //compressed_wet_bulb_temp = Double.parseDouble(mytemp.wet_bulb_temp) * schalings_factor + CELCIUS_TO_KELVIN_FACTOR;
            BigDecimal bd_wet_bulb = new BigDecimal(Double.parseDouble(mytemp.wet_bulb_temp) * schalings_factor + CELCIUS_TO_KELVIN_FACTOR). setScale(3, RoundingMode.HALF_UP); // three decimals 
            compressed_wet_bulb_temp = bd_wet_bulb.doubleValue();
            present[24] = 1;
         } // try
         catch (NumberFormatException ex)
         {
            compressed_wet_bulb_temp = COMPRESSED_UNDEF_VALUE;
            present[24] = 0;
            System.out.println("+++ write input file for format 101; wet bulb temp " + ex);
         } // catch            
      } //  
      else
      {
         compressed_wet_bulb_temp = COMPRESSED_UNDEF_VALUE;
         present[24] = 0;
      }
     
   
      /* [25]
      //////////////////////////////////////////////// 0 12 103 dewpoint    ///////////////////////////////
      */
      /* unit  = Kelvin */
      schalings_factor = 1;   
   
      if (mytemp.double_dew_point >= -70.0 && mytemp.double_dew_point <= 70.0)      // coarse limits, to be safe
      {
         //compressed_dewpoint = mytemp.double_dew_point * schalings_factor + CELCIUS_TO_KELVIN_FACTOR;
         BigDecimal bd_dewpoint = new BigDecimal(mytemp.double_dew_point * schalings_factor + CELCIUS_TO_KELVIN_FACTOR). setScale(3, RoundingMode.HALF_UP); // three decimals 
         compressed_dewpoint = bd_dewpoint.doubleValue();
         present[25] = 1;
      }
      else
      {
         compressed_dewpoint = COMPRESSED_UNDEF_VALUE;
         present[25] = 0;
      }
   
   
      /* [26]
      //////////////////////////////////////////////// 0 13 009     relative humidity   ////////////////////////////////
      */
      /* unit  = %; double_rv in range [0.0 .. 1.0] */
      schalings_factor = 100;
   
      if (mytemp.double_rv >= 0.01 && mytemp.double_rv <= 1.0)      // see also function: mytemp.Bereken_Dauwpunt_en_RV()
      {
         //compressed_rv = mytemp.double_rv * schalings_factor;
         //BigDecimal bd_rv = new BigDecimal(mytemp.double_rv * schalings_factor + CELCIUS_TO_KELVIN_FACTOR). setScale(3, RoundingMode.HALF_UP); // three decimals 
         BigDecimal bd_rv = new BigDecimal(mytemp.double_rv * schalings_factor). setScale(1, RoundingMode.HALF_UP); // one decimal
         compressed_rv = bd_rv.doubleValue();
         present[26] = 1;
      }
      else
      {
         compressed_rv = COMPRESSED_UNDEF_VALUE;
         present[26] = 0;
      }
   
   
      /* [27]
      //////////////////////////////////////////////// 0 22 043 SST   ////////////////////////////////////////
      */
      /* unit  = Kelvin */
      schalings_factor = 1;    
   
      if ((mytemp.sea_water_temp.trim()).length() > 0)  
      {
         try
         {
            //compressed_sst = Double.parseDouble(mytemp.sea_water_temp) * schalings_factor + CELCIUS_TO_KELVIN_FACTOR;
            BigDecimal bd_sst = new BigDecimal(Double.parseDouble(mytemp.sea_water_temp) * schalings_factor + CELCIUS_TO_KELVIN_FACTOR). setScale(3, RoundingMode.HALF_UP); // three decimals 
            compressed_sst = bd_sst.doubleValue();
            present[27] = 1;
         } // try
         catch (NumberFormatException ex)
         {
            compressed_sst = COMPRESSED_UNDEF_VALUE;
            present[27] = 0;
            System.out.println("+++ write input file for format 101; SST " + ex);
         } // catch            
      } //  
      else
      {
         compressed_sst = COMPRESSED_UNDEF_VALUE;
         present[27] = 0;
      }  
   
   
      /* [28]
      ///////////////////////////////////////// 1 11 000 visual obs presence indicator  ////////////////////////////////
      */
      /* unit  = n.a. */
      
      compressed_visual_obs_presence_indicator = 1;              // by default in TurboWin
      present[28] = 1;
   
   
      /* [29]
      /////////////////////////////////////////////// 0 20 001 (Visibility)  /////////////////////////////////////////////////
      */
      /* unit  = code (no bufr table -> manual on codes table 4377) */

      if ((myvisibility.VV_code.trim()).length() > 0)  
      {
         if (myvisibility.VV_code.equals("//"))
         {
            compressed_visibility = COMPRESSED_UNDEF_VALUE;
            present[29] = 0;
         }   
         else
         {
            try
            {
               compressed_visibility = Double.parseDouble(myvisibility.VV_code); 
               present[29] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_visibility = COMPRESSED_UNDEF_VALUE;
               present[29] = 0;
               System.out.println("+++ write input file for format 101; visibility (VV) " + ex);
            } // catch
         } // else
      } // if ((myvisibility.VV_code.trim()).length() > 0) 
      else
      {
         compressed_visibility = COMPRESSED_UNDEF_VALUE;
         present[29] = 0;
      } // else   
      
      
      /* [30]
      /////////////////////////////////////////// 0 20 003 (present weather)  ///////////////////////////////////////
      */
      /* unit  = (bufr code table 020003) */
      
      if ((mypresentweather.ww_code.trim()).length() > 0)  
      {
         if (mypresentweather.ww_code.equals("//"))
         {
            compressed_present_weather = COMPRESSED_UNDEF_VALUE;
            present[30] = 0;
         }   
         else
         {
            try
            {
               compressed_present_weather = Double.parseDouble(mypresentweather.ww_code); 
               present[30] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_present_weather = COMPRESSED_UNDEF_VALUE;
               present[30] = 0;
               System.out.println("+++ write input file for format 101; present weather (ww) " + ex);
            } // catch
         } // else
      } // if ((mypresentweather.ww_code.trim()).length() > 0) 
      else
      {
         compressed_present_weather = COMPRESSED_UNDEF_VALUE;
         present[30] = 0;
      } // else        
      
      
      /* [31]
      ////////////////////////////////////////////// 0 20 004 (past weather; W1) ////////////////////////////////////////
      */
      /* unit  = code (bufr table 020004/020005) */
   
      if ((mypastweather.W1_code.trim()).length() > 0)  
      {
         if (mypastweather.W1_code.equals("/"))
         {
            compressed_past_weather_1 = COMPRESSED_UNDEF_VALUE;
            present[31] = 0;
         }   
         else
         {
            try
            {
               compressed_past_weather_1 = Double.parseDouble(mypastweather.W1_code); 
               present[31] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_past_weather_1 = COMPRESSED_UNDEF_VALUE;
               present[31] = 0;
               System.out.println("+++ write input file for format 101; past weather 1 (W1) " + ex);
            } // catch
         } // else
      } // 
      else
      {
         compressed_past_weather_1 = COMPRESSED_UNDEF_VALUE;
         present[31] = 0;
      } // else   
   
   
      /* [32]
      ////////////////////////////////////////////// 0 20 005 (past weather; W2) ////////////////////////////////////////
      */
      /* unit  = code (bufr code table 020004/020005) */
   
      if ((mypastweather.W2_code.trim()).length() > 0)  
      {
         if (mypastweather.W2_code.equals("/"))
         {
            compressed_past_weather_2 = COMPRESSED_UNDEF_VALUE;
            present[32] = 0;
         }   
         else
         {
            try
            {
               compressed_past_weather_2 = Double.parseDouble(mypastweather.W2_code); 
               present[32] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_past_weather_2 = COMPRESSED_UNDEF_VALUE;
               present[32] = 0;
               System.out.println("+++ write input file for format 101; past weather 2 (W2) " + ex);
            } // catch
         } // else
      } // 
      else
      {
         compressed_past_weather_2 = COMPRESSED_UNDEF_VALUE;
         present[32] = 0;
      } // else      
   
   
      /* [33]
      //////////////////////////////////////////// 0 20 010 (total cloud cover; N) ////////////////////////////////////////
      */
      /* unit  = code (no bufr table -> manual on codes table 2700) */
   
      if ((mycloudcover.N_code.trim()).length() > 0)  
      {
         if (mycloudcover.N_code.equals("/"))
         {
            compressed_cloud_cover = COMPRESSED_UNDEF_VALUE;
            present[33] = 0;
         }   
         else
         {
            try
            {
               compressed_cloud_cover = Double.parseDouble(mycloudcover.N_code); 
               present[33] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_cloud_cover = COMPRESSED_UNDEF_VALUE;
               present[33] = 0;
               System.out.println("+++ write input file for format 101; total cloud cover (N) " + ex);
            } // catch
         } // else
      } // 
      else
      {
         compressed_cloud_cover = COMPRESSED_UNDEF_VALUE;
         present[33] = 0;
      } // else         
   
   
      /* [34]
      //////////////////////////////////////////// 0 20 011 (cloud amount low; Nh) ////////////////////////////////////////
      */
      /* unit  = code (bufr table 020011) */
      if ((mycloudcover.Nh_code.trim()).length() > 0)  
      {
         if (mycloudcover.Nh_code.equals("/"))
         {
            compressed_cloud_amount_low_medium = COMPRESSED_UNDEF_VALUE;
            present[34] = 0;
         }   
         else
         {
            try
            {
               compressed_cloud_amount_low_medium = Double.parseDouble(mycloudcover.Nh_code); 
               present[34] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_cloud_amount_low_medium = COMPRESSED_UNDEF_VALUE;
               present[34] = 0;
               System.out.println("+++ write input file for format 101; cloud amount low (Nh) " + ex);
            } // catch
         } // else
      } // 
      else
      {
         compressed_cloud_amount_low_medium = COMPRESSED_UNDEF_VALUE;
         present[34] = 0;
      } // else         
   
   
      /* [35]
      //////////////////////////////////////////// 0 20 012 (cloud type low; Cl) ////////////////////////////////////////
      */
      /* unit  = code (bufr table 020012) */
      
      if ((mycl.cl_code.trim()).length() > 0)  
      {
         if (mycl.cl_code.equals("/"))
         {
            compressed_Cl = COMPRESSED_UNDEF_VALUE;
            present[35] = 0;
         }   
         else
         {
            try
            {
               compressed_Cl = Double.parseDouble(mycl.cl_code) + 30.0;   
               present[35] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_Cl = COMPRESSED_UNDEF_VALUE;
               present[35] = 0;
               System.out.println("+++ write input file for format 101; cloud type low (Cl) " + ex);
            } // catch
         } // else
      } // 
      else
      {
         compressed_Cl = COMPRESSED_UNDEF_VALUE;
         present[35] = 0;
      } // else            
   
   
      /* [36]
      //////////////////////////////////////////// 0 20 012 (cloud type middle; Cm) ////////////////////////////////////////
      */
      /* unit  = code (bufr table 020012) */
      
      if ((mycm.cm_code.trim()).length() > 0)  
      {
         if (mycm.cm_code.equals("/"))
         {
            compressed_Cm = COMPRESSED_UNDEF_VALUE;
            present[36] = 0;
         }   
         else
         {
            try
            {
               String hulp_cm_code = "";
               
               // note: Cm7 is marked as 7a, 7b or 7c!
               if (mycm.cm_code.length() > 1)
               {
                  hulp_cm_code = mycm.cm_code.substring(0, 1);    // NB .substring(0, 1) --> because Cm_code in case of Cm7 an a, b, c could be sticked to the 7 (7a, 7b, 7c)
               }
               else
               {
                  hulp_cm_code = mycm.cm_code;
               }
               
               compressed_Cm = Double.parseDouble(hulp_cm_code) + 20.0; 
               present[36] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_Cm = COMPRESSED_UNDEF_VALUE;
               present[36] = 0;
               System.out.println("+++ write input file for format 101; cloud type middle (Cm) " + ex);
            } // catch
         } // else
      } // 
      else
      {
         compressed_Cm = COMPRESSED_UNDEF_VALUE;
         present[36] = 0;
      } // else               
   
   
      /* [37]
      //////////////////////////////////////////// 0 20 012 (cloud type high; Ch) ////////////////////////////////////////
      */
      /* unit  = code (bufr table 020012) */
      
      if ((mych.ch_code.trim()).length() > 0)  
      {
         if (mych.ch_code.equals("/"))
         {
            compressed_Ch = COMPRESSED_UNDEF_VALUE;
            present[37] = 0;
         }   
         else
         {
            try
            {
               compressed_Ch = Double.parseDouble(mych.ch_code) + 10.0; 
               present[37] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_Ch = COMPRESSED_UNDEF_VALUE;
               present[37] = 0;
               System.out.println("+++ write input file for format 101; cloud type high (Ch) " + ex);
            } // catch
         } // else
      } // 
      else
      {
         compressed_Ch = COMPRESSED_UNDEF_VALUE;
         present[37] = 0;
      } // else               
     
      
      /* [38]
      //////////////////////////////////////////////// 0 20 013 (height of base of cloud; h)///////////////////////
      */
      /* unit = code (no bufr table -> manual on codes table 1600) */
      
      if ((mycloudcover.h_code.trim()).length() > 0)  
      {
         if (mycloudcover.h_code.equals("/"))
         {
            compressed_cloud_base = COMPRESSED_UNDEF_VALUE;
            present[38] = 0;
         }   
         else
         {
            try
            {
               compressed_cloud_base = Double.parseDouble(mycloudcover.h_code); 
               present[38] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_cloud_base = COMPRESSED_UNDEF_VALUE;
               present[38] = 0;
               System.out.println("+++ write input file for format 101; height of base of lowest clouds (h) " + ex);
            } // catch
         } // else
      } // 
      else
      {
         compressed_cloud_base = COMPRESSED_UNDEF_VALUE;
         present[38] = 0;
      } // else                     
      
      
      /* [39]
      ///////////////////////////////////////// 1 08 000 waves obs presence indicator  ////////////////////////////////
      */
      /* unit  = N/A */
      compressed_waves_obs_presence_indicator = 1;              // by default inTurboWin
      present[39] = 1;
      
      
      /* [40]
      //////////////////////////////////// 0 22 012 (period wind waves)      ////////////////////////////////////////
      */
      /* unit  = sec */
      schalings_factor = 1;
      
      if ((mywaves.wind_waves_period.trim()).length() > 0)  
      {
         if (mywaves.wind_waves_period.equals("confused"))                     // there is no "confused" option in bufr 
         {
            compressed_period_wind_waves_estimated = COMPRESSED_UNDEF_VALUE;
            present[40] = 0;
         }
         else
         {
            try
            {
               compressed_period_wind_waves_estimated = Double.parseDouble(mywaves.wind_waves_period) * schalings_factor;
               present[40] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_period_wind_waves_estimated = COMPRESSED_UNDEF_VALUE;
               present[40] = 0;
               System.out.println("+++ write input file for format 101; wind waves period " + ex);
            } // catch 
         } // else
      } // if ((mywaves.wind_waves_period.trim()).length() > 0) 
      else
      {
         compressed_period_wind_waves_estimated = COMPRESSED_UNDEF_VALUE;
         present[40] = 0;
      } // else 
   
      
      /* [41]
      //////////////////////////////////// 0 22 022 (height wind waves)      ////////////////////////////////////////
      */
      /* unit  = metres */
      schalings_factor = 1;
      
      if ((mywaves.wind_waves_height.trim()).length() > 0)  
      {
         if (mywaves.wind_waves_height.equals("confused"))                     // there is no "confused" option in bufr 
         {
            compressed_height_wind_waves_estimated = COMPRESSED_UNDEF_VALUE;
            present[41] = 0;
         }
         else
         {
            try
            {
               compressed_height_wind_waves_estimated = Double.parseDouble(mywaves.wind_waves_height) * schalings_factor;
               present[41] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_height_wind_waves_estimated = COMPRESSED_UNDEF_VALUE;
               present[41] = 0;
               System.out.println("+++ write input file for format 101; wind waves height " + ex);
            } // catch 
         } // else
      } // if ((mywaves.wind_waves_height.trim()).length() > 0) 
      else
      {
         compressed_height_wind_waves_estimated = COMPRESSED_UNDEF_VALUE;
         present[41] = 0;
      } // else 
         
      
      /* [42]
      ////////////////////////////////////// 0 22 003 (dir swell 1)   ///////////////////////////////////////////
      */
      /* unit  =  degrees true */
      schalings_factor = 1;
      
      if ((mywaves.swell_1_dir.trim()).length() > 0)  
      {
         if (mywaves.swell_1_dir.equals("confused"))                     // there is no "confused" option in bufr 
         {
            compressed_dir_swell_1_estimated = COMPRESSED_UNDEF_VALUE;
            present[42] = 0;
         }
         else if (mywaves.swell_1_dir.equals("no swell"))                     
         {
            compressed_dir_swell_1_estimated = COMPRESSED_UNDEF_VALUE;
            present[42] = 0;
         }
         else
         {
            try
            {
               compressed_dir_swell_1_estimated = Double.parseDouble(mywaves.swell_1_dir) * schalings_factor;
               present[42] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_dir_swell_1_estimated = COMPRESSED_UNDEF_VALUE;
               present[42] = 0;
               System.out.println("+++ write input file for format 101; direction of 1st swell " + ex);
            } // catch 
         } // else
      } // if ((mywaves.swell_1_dir.trim()).length() > 0)
      else
      {
         compressed_dir_swell_1_estimated = COMPRESSED_UNDEF_VALUE;
         present[42] = 0;
      } // else       
      
      
      /* [43]
      //////////////////////////////////// 0 22 013 (period swell 1)   /////////////////////////////////////
      */
      /* unit  = sec */
      schalings_factor = 1;
      
      if ((mywaves.swell_1_period.trim()).length() > 0)  
      {
         if (mywaves.swell_1_period.equals("confused"))                     // there is no "confused" option in bufr 
         {
            compressed_period_swell_1_estimated = COMPRESSED_UNDEF_VALUE;
            present[43] = 0;
         }
         else
         {
            try
            {
               compressed_period_swell_1_estimated = Double.parseDouble(mywaves.swell_1_period) * schalings_factor;
               present[43] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_period_swell_1_estimated = COMPRESSED_UNDEF_VALUE;
               present[43] = 0;
               System.out.println("+++ write input file for format 101; period of 1st swell " + ex);
            } // catch 
         } // else
      } 
      else
      {
         compressed_period_swell_1_estimated = COMPRESSED_UNDEF_VALUE;
         present[43] = 0;
      } // else 
      
      
      /* [44]
      //////////////////////////////////// 0 22 023 (height swell 1)   /////////////////////////////////////
      */
      /* unit  = sec */
      schalings_factor = 1;
      
      if ((mywaves.swell_1_height.trim()).length() > 0)  
      {
         if (mywaves.swell_1_height.equals("confused"))                     // there is no "confused" option in bufr 
         {
            compressed_height_swell_1_estimated = COMPRESSED_UNDEF_VALUE;
            present[44] = 0;
         }
         else
         {
            try
            {
               compressed_height_swell_1_estimated = Double.parseDouble(mywaves.swell_1_height) * schalings_factor;
               present[44] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_height_swell_1_estimated = COMPRESSED_UNDEF_VALUE;
               present[44] = 0;
               System.out.println("+++ write input file for format 101; height of 1st swell " + ex);
            } // catch 
         } // else
      } 
      else
      {
         compressed_height_swell_1_estimated = COMPRESSED_UNDEF_VALUE;
         present[44] = 0;
      } // else       
      
      
      /* [45]
      ////////////////////////////////////// 0 22 003 (dir swell 2)   ///////////////////////////////////////////
      */
      /* unit  =  degrees true */
      schalings_factor = 1;
      
      if ((mywaves.swell_2_dir.trim()).length() > 0)  
      {
         if (mywaves.swell_2_dir.equals("confused"))                     // there is no "confused" option in bufr 
         {
            compressed_dir_swell_2_estimated = COMPRESSED_UNDEF_VALUE;
            present[45] = 0;
         }
         else if (mywaves.swell_2_dir.equals("no swell"))                     
         {
            compressed_dir_swell_2_estimated = COMPRESSED_UNDEF_VALUE;
            present[45] = 0;
         }
         else
         {
            try
            {
               compressed_dir_swell_2_estimated = Double.parseDouble(mywaves.swell_2_dir) * schalings_factor;
               present[45] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_dir_swell_2_estimated = COMPRESSED_UNDEF_VALUE;
               present[45] = 0;
               System.out.println("+++ write input file for format 101; direction of 2nd swell " + ex);
            } // catch 
         } // else
      } // if ((mywaves.swell_1_dir.trim()).length() > 0)
      else
      {
         compressed_dir_swell_2_estimated = COMPRESSED_UNDEF_VALUE;
         present[45] = 0;
      } // else       
      
      
      /* [46]
      //////////////////////////////////// 0 22 013 (period swell 2)   /////////////////////////////////////
      */
      /* unit  = sec */
      schalings_factor = 1;
      
      if ((mywaves.swell_2_period.trim()).length() > 0)  
      {
         if (mywaves.swell_2_period.equals("confused"))                     // there is no "confused" option in bufr 
         {
            compressed_period_swell_2_estimated = COMPRESSED_UNDEF_VALUE;
            present[46] = 0;
         }
         else
         {
            try
            {
               compressed_period_swell_2_estimated = Double.parseDouble(mywaves.swell_2_period) * schalings_factor;
               present[46] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_period_swell_2_estimated = COMPRESSED_UNDEF_VALUE;
               present[46] = 0;
               System.out.println("+++ write input file for format 101; period of 2nd swell " + ex);
            } // catch 
         } // else
      } 
      else
      {
         compressed_period_swell_2_estimated = COMPRESSED_UNDEF_VALUE;
         present[46] = 0;
      } // else 
            
      
      /* [47]
      //////////////////////////////////// 0 22 023 (height swell 2)   /////////////////////////////////////
      */
      /* unit  = sec */
      schalings_factor = 1;
      
      if ((mywaves.swell_2_height.trim()).length() > 0)  
      {
         if (mywaves.swell_2_height.equals("confused"))                     // there is no "confused" option in bufr 
         {
            compressed_height_swell_2_estimated = COMPRESSED_UNDEF_VALUE;
            present[47] = 0;
         }
         else
         {
            try
            {
               compressed_height_swell_2_estimated = Double.parseDouble(mywaves.swell_2_height) * schalings_factor;
               present[47] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_height_swell_2_estimated = COMPRESSED_UNDEF_VALUE;
               present[47] = 0;
               System.out.println("+++ write input file for format 101; height of 2nd swell " + ex);
            } // catch 
         } // else
      } 
      else
      {
         compressed_height_swell_2_estimated = COMPRESSED_UNDEF_VALUE;
         present[47] = 0;
      } // else       
      
      
      /* [48]
      ///////////////////////////////////////// 1 08 000 ice obs presence indicator  ////////////////////////////////
      */
      /* unit  = N/A */

      if (myicing.EsEs_code.equals("") && myicing.Rs_code.equals("") && myicing.Is_code.equals("") &&
          myice1.ci_code.equals("") && myice1.bi_code.equals("") && myice1.zi_code.equals("") && myice1.Si_code.equals("") && myice1.Di_code.equals(""))
      {
         compressed_ice_obs_presence_indicator = 0;        // observation do not contain ice groups
         present[48] = 1;
      }
      else
      {
         compressed_ice_obs_presence_indicator = 1;        // observation contains ice groups
         present[48] = 1;
      }
      
      
      /* [49]
      ///////////////////////////////////// 0 20 031 (EsEs- ice deposit/thickness)  //////////////////////////////////
      */
      /* unit  = meter */
      schalings_factor = 0.01;      /* NB num_EsEs in centimeters -> schalings_factor divide by 100 = meters */
   
      if ((myicing.EsEs_code.trim()).length() > 0)
      {
         if (myicing.EsEs_code.equals("//"))
         {
            compressed_thickness_ice_accretion = COMPRESSED_UNDEF_VALUE;
            present[49] = 0;
         }   
         else  
         {
            try
            {
               compressed_thickness_ice_accretion = Double.parseDouble(myicing.EsEs_code) * schalings_factor;
               present[49] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_thickness_ice_accretion = COMPRESSED_UNDEF_VALUE;
               present[49] = 0;
               System.out.println("+++ write input file for format 101; ice thickness (EsEs) " + ex);
            } // catch             
         } // else
      } // if ((myicing.EsEs_code.trim()).length() > 0)
      else
      {
         compressed_thickness_ice_accretion = COMPRESSED_UNDEF_VALUE;
         present[49] = 0;
      }   
      
      
      /* [50]
      ///////////////////////////////////// 0 20 032 (Rs - rate of ice accretion)   ///////////////////////////////////
      */
      /* unit  = code (bufr table 020032) */
      schalings_factor = 1;

      if ((myicing.Rs_code.trim()).length() > 0)
      {
         if (myicing.Rs_code.equals("/"))
         {
            compressed_rate_ice_accretion = COMPRESSED_UNDEF_VALUE;
            present[50] = 0;
         }   
         else  
         {
            try
            {
               compressed_rate_ice_accretion = Double.parseDouble(myicing.Rs_code) * schalings_factor;
               present[50] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_rate_ice_accretion = COMPRESSED_UNDEF_VALUE;
               present[50] = 0;
               System.out.println("+++ write input file for format 101; rate ice accretion (Rs) " + ex);
            } // catch             
         } // else
      } // if ((myicing.Rs_code.trim()).length() > 0)
      else
      {
         compressed_rate_ice_accretion = COMPRESSED_UNDEF_VALUE;
         present[50] = 0;
      }   
      

      /* [51]
      //////////////////////////////////////////// 0 20 033 (Is, cause of ice accretion) /////////////////////////////
      */
      /* unit  = code (table 020033-equivalent By Pierre Blouch)  */
      schalings_factor = 1;
      // NB see also: compile_obs_for_AWS() [main.java]
      // NB see also myicing.java
   
      if ((myicing.Is_code.trim()).length() > 0)
      {
         if (myicing.Is_code.equals("/"))
         {
            compressed_cause_ice_accretion = COMPRESSED_UNDEF_VALUE;
            present[51] = 0;
         }   
         else  
         {
            //try
            //{
               // NB Is code already according bufr 020033-equivalent if format 101 or AWS mode [see myicing.java]
               //compressed_cause_ice_accretion = Double.parseDouble(myicing.Is_code) * schalings_factor;
               //present[51] = 1;
               
               if (myicing.Is_code.equals("1"))               // icing from spray (FM13 code)
               {
                  compressed_cause_ice_accretion = 8.0;       // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
                  present[51] = 1;
               }
               else if (myicing.Is_code.equals("2"))          // icing from fog (FM13 code)
               {
                  compressed_cause_ice_accretion = 4.0;       // BUFR table 020033 equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
                  present[51] = 1;
               }
               else if (myicing.Is_code.equals("3"))          // icing from spray and fog (FM13 code)
              {
                  compressed_cause_ice_accretion = 12.0;      // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
                  present[51] = 1;
               }
               else if (myicing.Is_code.equals("4"))          // icing from rain (FM13 code)
               {
                  compressed_cause_ice_accretion = 2.0;       // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
                  present[51] = 1;
               }
               else if (myicing.Is_code.equals("5"))          // icing from spray and rain (FM13 code)
               {
                  compressed_cause_ice_accretion = 10.0;      // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
                  present[51] = 1;
               }
               else if (myicing.Is_code.equals("6"))          // icing from fog and rain (not present in FM13 code)
               {
                  compressed_cause_ice_accretion = 6.0;       // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
                  present[51] = 1;
               }
               else if (myicing.Is_code.equals("14"))         // icing from spray and fog and rain (not present in FM13 code)
               {
                  compressed_cause_ice_accretion = 14.0;      // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
                  present[51] = 1;
               }
               else
               {
                  compressed_cause_ice_accretion = COMPRESSED_UNDEF_VALUE;
                  present[51] = 0;
               }                                 
            //} // try
            //catch (NumberFormatException ex)
            //{
            //   compressed_cause_ice_accretion = COMPRESSED_UNDEF_VALUE;
            //   present[51] = 0;
            //   System.out.println("+++ write input file for format 101; cause ice accretion (Is) " + ex);
            //} // catch             
         } // else
      } // 
      else
      {
         compressed_cause_ice_accretion = COMPRESSED_UNDEF_VALUE;
         present[51] = 0;
      }   

      
      /* [52]
      /////////////////////////////////// 0 20 034 (ci; sea ice concentration)  //////////////////////////////////////
      */
      /* unit  = code (bufr table 020034 and FM13 table table 0639) */
      schalings_factor = 1;

      if ((myice1.ci_code.trim()).length() > 0)
      {
         if (myice1.ci_code.equals("/"))
         {
            compressed_sea_ice_concentration = COMPRESSED_UNDEF_VALUE;
            present[52] = 0;
         }   
         else  
         {
            try
            {
               compressed_sea_ice_concentration = Double.parseDouble(myice1.ci_code) * schalings_factor;
               present[52] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_sea_ice_concentration = COMPRESSED_UNDEF_VALUE;
               present[52] = 0;
               System.out.println("+++ write input file for format 101; sea ice concentration (ci) " + ex);
            } // catch             
         } // else
      } // 
      else
      {
         compressed_sea_ice_concentration = COMPRESSED_UNDEF_VALUE;
         present[52] = 0;
      }   
      
      
      /* [53]
      ///////////////////// 0 20 035 (bi, amount and type of ice, ice of land origin)    ///////////////////////////
      */
      /* unit  = code (bufr table 020035 and FM13 table table 0439) */
      schalings_factor = 1;

      if ((myice1.bi_code.trim()).length() > 0)
      {
         if (myice1.bi_code.equals("/"))
         {
            compressed_amount_type_ice = COMPRESSED_UNDEF_VALUE;
            present[53] = 0;
         }   
         else  
         {
            try
            {
               compressed_amount_type_ice = Double.parseDouble(myice1.bi_code) * schalings_factor;
               present[53] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_amount_type_ice = COMPRESSED_UNDEF_VALUE;
               present[53] = 0;
               System.out.println("+++ write input file for format 101; amount and type of ice (bi) " + ex);
            } // catch             
         } // else
      } // 
      else
      {
         compressed_amount_type_ice = COMPRESSED_UNDEF_VALUE;
         present[53] = 0;
      }         
      
      
      /* [54]
      //////////////////////////////////////////////// 0 20 036 (zi; ice situation) ////////////////////////////////
      */
      /* unit  = code (bufr table 020036 and FM13 table 5239) */
      schalings_factor = 1;

      if ((myice1.zi_code.trim()).length() > 0)
      {
         if (myice1.zi_code.equals("/"))
         {
            compressed_ice_situation = COMPRESSED_UNDEF_VALUE;
            present[54] = 0;
         }   
         else  
         {
            try
            {
               compressed_ice_situation = Double.parseDouble(myice1.zi_code) * schalings_factor;
               present[54] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_ice_situation = COMPRESSED_UNDEF_VALUE;
               present[54] = 0;
               System.out.println("+++ write input file for format 101; ice concentration (zi) " + ex);
            } // catch             
         } // else
      } // 
      else
      {
         compressed_ice_situation = COMPRESSED_UNDEF_VALUE;
         present[54] = 0;
      }         
       
   
      /* [55]
      //////////////////////////////////////////////// 0 20 037 (Si; ice development) ////////////////////////////////
      */
      /* unit  = code (bufr table 020037 and FM13 table 3739) */
      schalings_factor = 1;

      if ((myice1.Si_code.trim()).length() > 0)
      {
         if (myice1.Si_code.equals("/"))
         {
            compressed_ice_development = COMPRESSED_UNDEF_VALUE;
            present[55] = 0;
         }   
         else  
         {
            try
            {
               compressed_ice_development = Double.parseDouble(myice1.Si_code) * schalings_factor;
               present[55] = 1;
            } // try
            catch (NumberFormatException ex)
            {
               compressed_ice_development = COMPRESSED_UNDEF_VALUE;
               present[55] = 0;
               System.out.println("+++ write input file for format 101; ice development (Si) " + ex);
            } // catch             
         } // else
      } // 
      else
      {
         compressed_ice_development = COMPRESSED_UNDEF_VALUE;
         present[55] = 0;
      }           
   
   
      /* [56]
      //////////////////////////////////////// 0 20 038 (Di, bearing of ice edge)  ////////////////////////////////////
      */
      /* unit  = degrees (no bufr table) */
      schalings_factor = 1;
      
      if (myice1.Di_code.equals("0") == true)                        // ship in shore or flaw lead; not possible to translate this into a direction (as required in bufr) !!   
      {   
         compressed_ice_edge_bearing = COMPRESSED_UNDEF_VALUE;       // see doc "EUCAWS inputs/outputs Complementary information about codes", Pierre Blouch, 21 August 2013 draft V2
         present[56] = 0;
      }
      else if (myice1.Di_code.equals("1") == true)
      {
         compressed_ice_edge_bearing = (double)45 * schalings_factor;
         present[56] = 1;
      }
      else if (myice1.Di_code.equals("2") == true)
      {
         compressed_ice_edge_bearing = (double)90 * schalings_factor;
         present[56] = 1;
      }
      else if (myice1.Di_code.equals("3") == true)
      {
         compressed_ice_edge_bearing = (double)135 * schalings_factor;
         present[56] = 1;
      }
      else if (myice1.Di_code.equals("4") == true)
      {
         compressed_ice_edge_bearing = (double)180 * schalings_factor;
         present[56] = 1;
      }
      else if (myice1.Di_code.equals("5") == true)
      {
         compressed_ice_edge_bearing = (double)225 * schalings_factor;
         present[56] = 1;
      }
      else if (myice1.Di_code.equals("6") == true)
      {
         compressed_ice_edge_bearing = (double)270 * schalings_factor;
         present[56] = 1;
      }
      else if (myice1.Di_code.equals("7") == true)
      {
         compressed_ice_edge_bearing = (double)315 * schalings_factor;
         present[56] = 1;
      }
      else if (myice1.Di_code.equals("8") == true)
      {
         compressed_ice_edge_bearing = (double)360 * schalings_factor;
         present[56] = 1;
      }
      else if (myice1.Di_code.equals("9") == true)
      {
         compressed_ice_edge_bearing = COMPRESSED_UNDEF_VALUE;
         present[56] = 0;
      }
      else
      {
         compressed_ice_edge_bearing = COMPRESSED_UNDEF_VALUE;
         present[56] = 0;
      }  
   
    
   
      //
      /* fill array val (sequence according S-AWS-101_modl_pilote.csv -and format 101 dataformat description-) */
      //
      val[0]  = compressed_format_identifier;
      val[1]  = compressed_call_sign_encryption_indicator;
      val[2]  = compressed_ship_direction;
      val[3]  = compressed_ship_speed;
      val[4]  = compressed_true_heading;
      val[5]  = compressed_sll_wl_difference;
      val[6]  = compressed_year;
      val[7]  = compressed_month;
      val[8]  = compressed_day;
      val[9]  = compressed_hour;
      val[10] = compressed_minute;
      val[11] = compressed_latitude;
      val[12] = compressed_longitude;
      val[13] = compressed_pressure_height;
      val[14] = compressed_pressure_msl;
      val[15] = compressed_pressure_change;
      val[16] = compressed_pressure_characteristic;
      val[17] = compressed_wind_dir;
      val[18] = compressed_wind_speed;
      val[19] = compressed_relative_wind_dir;
      val[20] = compressed_relative_wind_speed;
      val[21] = compressed_max_wind_gust_speed;
      val[22] = compressed_max_wind_gust_dir;
      val[23] = compressed_air_temp;
      val[24] = compressed_wet_bulb_temp;
      val[25] = compressed_dewpoint;
      val[26] = compressed_rv;
      val[27] = compressed_sst;        
      val[28] = compressed_visual_obs_presence_indicator;       
      val[29] = compressed_visibility;       
      val[30] = compressed_present_weather;       
      val[31] = compressed_past_weather_1;
      val[32] = compressed_past_weather_2;
      val[33] = compressed_cloud_cover;
      val[34] = compressed_cloud_amount_low_medium;
      val[35] = compressed_Cl;
      val[36] = compressed_Cm;
      val[37] = compressed_Ch;
      val[38] = compressed_cloud_base;
      val[39] = compressed_waves_obs_presence_indicator;
      val[40] = compressed_period_wind_waves_estimated;
      val[41] = compressed_height_wind_waves_estimated;
      val[42] = compressed_dir_swell_1_estimated;
      val[43] = compressed_period_swell_1_estimated;
      val[44] = compressed_height_swell_1_estimated;
      val[45] = compressed_dir_swell_2_estimated;
      val[46] = compressed_period_swell_2_estimated;
      val[47] = compressed_height_swell_2_estimated;
      val[48] = compressed_ice_obs_presence_indicator;
      val[49] = compressed_thickness_ice_accretion;
      val[50] = compressed_rate_ice_accretion;
      val[51] = compressed_cause_ice_accretion;
      val[52] = compressed_sea_ice_concentration;
      val[53] = compressed_amount_type_ice;
      val[54] = compressed_ice_situation;
      val[55] = compressed_ice_development;
      val[56] = compressed_ice_edge_bearing;
      
      
      // write to sub dir temp
      //
      String volledig_path_format_101_input_file = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + main.FORMAT_101_TEMP_DIR + java.io.File.separator + main.FORMAT_101_INPUT_FILE;

      try
      {
          BufferedWriter out = new BufferedWriter(new FileWriter(volledig_path_format_101_input_file));
      
          // always 0 as first line
          out.write("0");                      // "mode_oper"
          out.newLine();
          
          for (int i = 0; i < NBMAXELEM; i++)
          {
             out.write(String.valueOf(present[i]));
             
             if (present[i] == 1)
             {
                out.write(" ");
                out.write(String.valueOf(val[i]));
                
                pos_description = 17 - (Double.toString(val[i]).length()) - 1;   // 17 than description on same pos as Meteo France examples
             }
             else // so no 'val' value present for this element
             {
                pos_description = 17;                                             
             } // else
             
             // add appropriate description
             for (int k = 0; k < pos_description; k++)
             {
                out.write(" ");
             }
             out.write(description[i]);
             
             out.newLine();   // newLine(): write a line separator. The line separator string is defined by the system property line.separator, and is not necessarily a single newline ('\n') character.
          } // for (int i = 0; i < NBMAXELEM; i++)
          
          out.close();
       } // try
       catch (Exception e)
       {
          String info = "unable to write to: " + volledig_path_format_101_input_file;
          System.out.println(info);
          JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
       } // catch      
      
   } // public void write_input_for_101_compression()
   
   public static String compression_exe;
   public static String decompression_exe;
   
   private final double CELCIUS_TO_KELVIN_FACTOR     = 273.15;                         // add to Celcius to obtain Kelvin
   private final String FORMAT_101_CONFIG_DIR        = "config";
   private final String FORMAT_101_LOG_DIR           = "log";
   // NB see main.java: public final String FORMAT_101_ROOT_DIR      = "format_101";
   // NB see main.java: public final String FORMAT_101_TEMP_DIR      = "temp";
   public static final String COMPRESSION_EXE_WIN    = "teste_hc_TW.exe";              // Windows version
   public static final String DECOMPRESSION_EXE_WIN  = "MAWSbin_TW.exe";               // Windows version
   public static final String COMPRESSION_EXE_LIN    = "teste_hc_TW_64";               // Linux version (only for 64 bit Linux OS)
   public static final String DECOMPRESSION_EXE_LIN  = "MAWSbin_TW_64";                // Linux version (only for 64 bit Linux OS)
   private final String BUFR_TABLE                   = "S-AWS-101_modl_pilote.csv";    // for compression
   private final String META_TABLE                   = "S-AWS-101_modl_meta.csv";      // for decompression
   private final String DATA_TYPE                    = "DataType.txt";                 // for decompression
   private final String SOURCE_INPUT_COMPRESSION     = "temp/format_101.txt";          // nb doesn't work with a java separator (probably due to the working of the semi comression C program) !!
   private final String TEMPLATE_NUMBER              = "S-AWS-101";
   private final String HC_IDENT_FILE                = "HC_ident.txt";
   
   
} // public class FORMAT_101
