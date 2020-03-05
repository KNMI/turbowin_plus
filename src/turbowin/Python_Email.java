/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turbowin;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 *
 * @author marti
 */
public class Python_Email 
{
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public Python_Email()
   {

   }
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public boolean python_email_control_center()
   {
      // called from: Output_obs_by_email_Localhost_Gmail_Yahoo_FM13() [main.java]
      
      int return_status = 0;
      boolean doorgaan = true;
      
      
      
      //
      //////// determine OS (for selecting the correct pyton email executable)
      //
      main.OSType ostype = main.detect_OS();
      switch (ostype)
      {
         case LINUX:   python_email_exe   = PYTHON_EMAIL_EXE_LIN; 
                       break;
         case WINDOWS: python_email_exe   = PYTHON_EMAIL_EXE_WIN; 
                       break;
         default:      python_email_exe   = PYTHON_EMAIL_EXE_WIN;
                       break;
      }


      //
      /////////////// If necessary copy python email exe from internal jar to destination ///////////
      //
      return_status = check_python_email_module();
      if (return_status != 0)                        // python_email_exe not found so now try to copy python_email_exe
      {
         return_status = copy_python_email_module();
         if (return_status != 0)                     // now if return_status = 0 python_email_exe was copied sucessfully from jar to "PYTHON_ROOT_DIR"
         {
            doorgaan = false;
         }
      }  
      
      
      return doorgaan;

   } //  public boolean python_email_control_center() 
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private int check_python_email_module()
   {
      // called from python_email_control_center()[Python_Email.java]
      
      int python_email_module_status = 0;          // NB 0 = python_email_exe is present; 1 = python_email_exe is missing
      
   
      if (main.logs_dir.trim().equals("") == false && main.logs_dir.trim().length() >= 2)
      {
         // NB see function copy_python_email_exe_module() for a message if "PYTHON_ROOT_DIR" do not exist
         
         // NB e.g. python_email_exe_File = "C:\Program Files (x86)\TurboWin+\logs\python\email_tbw.exe"
         final File python_email_exe_File = new File(main.logs_dir + java.io.File.separator + PYTHON_ROOT_DIR + java.io.File.separator + python_email_exe);
         if (python_email_exe_File.exists() == false)
         {   
            python_email_module_status = 1;
            main.log_turbowin_system_message("[EMAIL] did not find: " + main.logs_dir + java.io.File.separator + PYTHON_ROOT_DIR + java.io.File.separator + python_email_exe);
         }   
         else
         {
            python_email_module_status = 0;
            main.log_turbowin_system_message("[EMAIL] found: " + python_email_exe);
         }
      } // if (main.logs_dir.trim().equals("") == false && main.logs_dir.trim().length() >= 2)
      
      
      return python_email_module_status;   
   } // private int check_python_email_module()
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private int copy_python_email_module()
   {
      // called from python_email_control_center()[Python_Email.java]
      
      int python_email_module_status;
      boolean doorgaan = true;
      String info = "";
      String resource = "";
      String output = "";
      
      
      // initialisation
      python_email_module_status = 0;           // NB 0 = OK; 1 = error
      
      // check if logs folder exists (because this will be the root folder of python email dir + file)
      if (main.logs_dir.trim().equals("") == true || main.logs_dir.trim().length() < 2)
      {
         doorgaan = false;
         info = "logs folder unknown, select: Maintenance -> Log files settings and retry";
         JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
      }
      
      
      if (doorgaan == true)
      {
         main.log_turbowin_system_message("[EMAIL] start copying complete Python Email module from jar to: " + main.logs_dir);
         
         // create dir "python" (e.g. "C:\Program Files (x86)\TurboWin+\logs\python")
         //
         String python_email_dir = main.logs_dir + java.io.File.separator + PYTHON_ROOT_DIR;
         final File dir_python_email = new File(python_email_dir);   
         dir_python_email.mkdir();
         
         // copying file python_email_exe to the just created "python" dir
         //
         resource = PYTHON_ROOT_DIR + "/" + python_email_exe;
         output = main.logs_dir + java.io.File.separator + PYTHON_ROOT_DIR + java.io.File.separator + python_email_exe;
         
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
                   
               main.log_turbowin_system_message("[EMAIL] success when copying " + resource + " from jar to: " + python_email_dir);
               
               // Set Execute Permission of just copied python email program but only for Linux
               // because after uzipping and unzipping the included exe file was set to not executable
               main.OSType ostype = main.detect_OS();
               switch (ostype)
               {
                  case LINUX:    File file = new File(output); 
                                 if (file.exists())
                                 {
                                    //Setting execute permission for all
                                    boolean result = file.setExecutable(true, false);   // Setting execute permission for all
             
                                    //System.out.println("Is execute permission for all set successfully? "+result);
                                    if (result)
                                    {
                                       main.log_turbowin_system_message("[EMAIL] success when setting execute permission of " + python_email_exe);
                                    }
                                    else
                                    {
                                       main.log_turbowin_system_message("[EMAIL] failed when setting execute permission of " + python_email_exe);
                                       python_email_module_status = 1;
                                    }
                                 }
                                 else
                                 {
                                    //System.out.println("Sorry...File doesn't exist.");
                                    main.log_turbowin_system_message("[EMAIL] failed (file not found) when setting execute permission of " + python_email_exe);
                                    python_email_module_status = 1;
                                 }
                                 break;
                  case WINDOWS:  break;
                  default:       break;
               }  // switch (ostype)     
               
            }
            catch (IOException ex) 
            {
               main.log_turbowin_system_message("[EMAIL] error when copying " + resource + " from jar to: " + python_email_dir);
               python_email_module_status = 1;
               
               // NB if the email app is not executable it comes with this (IO) error
            }                       
         } // if (getClass().getResourceAsStream(resource) != null)  
         else
         {
            main.log_turbowin_system_message("[EMAIL] error (resource file does not exist) when copying " + resource + " from jar to: " + python_email_dir);
            python_email_module_status = 1;
         }       
         
      } // if (doorgaan == true)
      
      
      return python_email_module_status;
   
   } // private int copy_python_email_module()
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public int send_python_email(final String smtp_mode, final String smtp_host_local, final String smtp_password_local, final String send_to, final String send_from, final String subject, final String body, final String send_cc, final String smtp_port_local, final String attachment)
   {
      // called from: Output_obs_by_email_Localhost_Gmail_Yahoo_FM13_format_101() [main.java]
      
      // NB in logs_dir always 'user_dir' already present (so a complete path)
      
      int exitStatus = 0;
      
      
      // The python email exe file to execute
      final File exeFile = new File(main.logs_dir + java.io.File.separator + PYTHON_ROOT_DIR + java.io.File.separator + python_email_exe);
      if (exeFile.exists() == false)
      {
         // NB this is an extra check. In theory if the python email exe is not present it should be already copied
         //    so probably this error message will never be triggered
         exitStatus = 100;
      }
      else  // exitStatus != 100;
      {
         // The output log file. All activity is written to this file
         final File log_outputFile = new File(String.format(main.logs_dir + java.io.File.separator + PYTHON_ROOT_DIR + java.io.File.separator + "log_python_email.txt"));

         final List<String> args = new ArrayList<>();
         args.add(exeFile.getAbsolutePath());
         args.add(smtp_mode);
         args.add(smtp_host_local);
         args.add(smtp_password_local);
         args.add(send_to);
         args.add(send_from);
         args.add(subject);
         args.add(body);
         args.add(send_cc);
         args.add(smtp_port_local);
         args.add(attachment);
         
         // print the complete argument list (only for checking in output console)
         System.out.println();
         System.out.println("python email process argument list:");
         for (int i = 0; i < args.size(); i++) 
         {
            if ((i == 3) && (smtp_mode.equals(main.SMTP_HOST_SHIP) == false))   // i = 3: password
            {
               // make password not readable (NB in SMTP_HOST_SHIP mode it could be "null")
               System.out.println("[" + i + "] = ******");
            }
            else
            {
               System.out.print("[" + i + "] = ");
               System.out.println(args.get(i));
            }
         }
         System.out.println();

         final ProcessBuilder processBuilder = new ProcessBuilder(args);

         // Redirect any output (including error) to a file. This avoids deadlocks when the buffers get full. 
         processBuilder.redirectErrorStream(true);
         processBuilder.redirectOutput(log_outputFile);

         // Set the working directory. The exe file will run as if you are in this directory.
         processBuilder.directory(new File(main.logs_dir + java.io.File.separator + PYTHON_ROOT_DIR));

         // Start the process and wait for it to finish. 
         Process process = null;
         try 
         {
            process = processBuilder.start();

            try 
            {
               // NB waitFor()
               // Causes the current thread to wait, if necessary, until the process represented by this Process object has terminated. 
               // This method returns immediately if the subprocess has already terminated. If the subprocess has not yet terminated, 
               // the calling thread will be blocked until the subprocess exits.
               //
               //Returns:
               //    the exit value of the subprocess represented by this Process object. By convention, the value 0 indicates normal termination.
               //exitStatus = process.waitFor();
               //
               // NB see above waitFor(): sometimes BLOCKING !!!! -> use waitFor(long timeout, TimeUnit unit) !!!
               
               // NB 'waitFor(long timeout, TimeUnit unit)' not available for Java < 8
               boolean boolean_exit_status = process.waitFor(20, TimeUnit.SECONDS);           // Java >= 8
               
               if (boolean_exit_status == true) // it will return a boolean value of true if the subprocess has exited before max waiting time elapsed
               {
                  //exitStatus = 0;
                  // if the subprocess has been successfully terminated then it will result in an exit value of the process.
                  exitStatus = process.exitValue();
               }
               else                             // a boolean value false if the wait time had elapsed before the subprocess exited.
               {
                  exitStatus = 103;
               }   
               
               // below to be 100% sure there will never be blocking of the main process (=calling thread) 
               process.destroy();
               if (process.isAlive())                           // Java >= 8
               {
                  process.destroyForcibly();                    // Java >= 8
               }
               //return process.exitValue();
               
               // NB https://www.baeldung.com/java-process-api
               // NB https://docs.oracle.com/javase/8/docs/api/java/lang/Process.html
               // NB https://docs.oracle.com/javase/7/docs/api/java/lang/Process.html
            } 
            catch (InterruptedException ex) 
            {
               exitStatus = 101;
            }
            //System.out.println("send email process finished with status: " + exitStatus);
         } // try
         catch (IOException ex) 
         {
            exitStatus = 102;
         } // catch
      } // else (exitStatus != 100)
      
      
      return exitStatus;
      
   } // public int send_python_email()
   
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public final String python_email_exe_return_status_to_text(int exit_status)
   {
      String exit_status_text = null;
       
      switch (exit_status)
      {
         case 0:   exit_status_text = "mail sent successfully"; break;
         
         // in python module defined exit_status codes
         case 1:   exit_status_text = "invalid number of arguments"; break;
         case 2:   exit_status_text = "invalid smtp_mode"; break;
         case 3:   exit_status_text = "This exception is raised when the server unexpectedly disconnects, or when an attempt is made to use the SMTP instance before connecting it to a server"; break;
         case 4:   exit_status_text = "Sender address refused. In addition to the attributes set by on all SMTPResponseException exceptions, this sets ‘sender’ to the string that the SMTP server refused"; break;
         case 5:   exit_status_text = "All recipient addresses refused. The errors for each recipient are accessible through the attribute recipients, which is a dictionary of exactly the same sort as SMTP.sendmail() returns"; break;
         case 6:   exit_status_text = "The SMTP server refused to accept the message data"; break;
         case 7:   exit_status_text = "Error occurred during establishment of a connection with the server"; break;
         case 8:   exit_status_text = "The server refused our HELO message"; break;
         case 9:   exit_status_text = "The command or option attempted is not supported by the server"; break;
         case 10:  exit_status_text = "SMTP authentication went wrong. Most probably the server didn’t accept the username/password combination provided"; break;
         case 11:  exit_status_text = "Base class for all exceptions that include an SMTP error code. These exceptions are generated in some instances when the SMTP server returns an error code. The error code is stored in the smtp_code attribute of the error, and the smtp_error attribute is set to the error message" ; break;     
         case 12:  exit_status_text = "Subclass of OSError that is the base exception class for all the other exceptions provided by this module"; break;     
         case 13:  exit_status_text = "not specified error; internet connection ok?; server reachable?; your email adress valid?; mail port open?"; break;  
         case 14:  exit_status_text = "Unable to add attachment format 101 obs (HPK_format_101.txt)"; break;
         
         // in this Java file defined exit_status codes
         case 100: exit_status_text = "Email exe not found"; break;
         case 101: exit_status_text = "InterruptedException in function send_python_email"; break;
         case 102: exit_status_text = "IOException in function send_python_email"; break; 
         case 103: exit_status_text = "unknown mail sent result because the mail process was terminated (took too long to complete)"; break;
            
         // NB reserved exit_status codes (see: Output_obs_by_email_Localhost_Gmail_Yahoo_FM13_format_101() [main.java])
         // NB exit_status 1000 reserved
         // NB exit_status 1001 reserved
         // NB exit_status 1002 reserved
         
         default: exit_status_text  = "unknown exit status (see also log_python_email.txt)"; break;
      }
      
      System.out.println("python_email_exe_return_status_to_text = " + exit_status_text);
      return exit_status_text;
   }

   
   
   // private constants
   private final String PYTHON_ROOT_DIR                         = "python";
   private final String PYTHON_EMAIL_EXE_LIN                    = "email_tbw";
   private final String PYTHON_EMAIL_EXE_WIN                    = "email_tbw.exe";
   
   // private var's
   private String python_email_exe                              = "";
   
}
