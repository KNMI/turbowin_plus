package turbowin;


import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
//import jssc.SerialPort;
//import jssc.SerialPortEvent;
//import jssc.SerialPortEventListener;
//import jssc.SerialPortException;
//import jssc.SerialPortList;






public class main_RS232_RS422 
{
   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/  
public void RS422_Check_Default_Serial_Port()   
{
   // called from: RS422_init_new_aws_data_received_check_timer() [main_RS232_RS422.java]
   //
   // AWS connection (not used if barometer connected)
   
   String info = "";
   
   if (main.serialPort.isOpen())
   {
      info = "[AWS] starting-com-port still open (" + main.serialPort.getDescriptivePortName() + ")";
      main.log_turbowin_system_message(info);
      
      main.serialPort.closePort();                     // will be opened again later
      // NB main.defaultPort and main.defaultPort_descriptive still preserve their start-up value
   }
   else
   {
      info = "[AWS] starting-com-port closed (" + main.serialPort.getDescriptivePortName() + ")";
      main.log_turbowin_system_message(info);
      
      main.defaultPort                                  = null;
      main.defaultPort_descriptive                      = null;
   }  
   
   
/* BELOW IS OK BUT UNNECESSATY   
   // initialisation
   main.defaultPort                                  = null;
   main.defaultPort_descriptive                      = null;

   // writing to default port
   //
   System.out.println("[AWS] Writing \"" + "?" + "\" to " + main.serialPort.getDescriptivePortName()); 
   String messageString_info = "?\r\n";                    // or "?\r" 
   byte[] bytes_message = messageString_info.getBytes(StandardCharsets.UTF_8); // Java 7+ only
   main.serialPort.writeBytes(bytes_message, bytes_message.length);            // Write data to port 
               
   try
   {
      Thread.sleep(2000);                                                      // 1000 seems too short on a few systems
   }
   catch (InterruptedException ex){ }     
                  
   // the response
   //
   String response = "";
   main.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);
   try 
   {
      byte[] readBuffer = new byte[64];
      main.serialPort.readBytes(readBuffer, readBuffer.length);
      response = new String(readBuffer, StandardCharsets.UTF_8);
      System.out.println(response);
   } 
   catch (Exception e) 
   { 
      System.out.println(e);
   }
                  
   if (response != null && response.indexOf("EUCAWS") >= 0)   
   {
      info = "[AWS] found AWS on port: " + main.serialPort.getDescriptivePortName();
      System.out.print("--- ");
      main.log_turbowin_system_message(info);
      System.out.println("");
                     
      main.serialPort.closePort();   
      main.defaultPort = main.serialPort.getSystemPortName();
      main.defaultPort_descriptive = main.serialPort.getDescriptivePortName();
   }
   else 
   {
      info = "[AWS] no AWS found on starting port (" + main.serialPort.getDescriptivePortName() + ")";
      System.out.println("--- " + info);
      System.out.println("");
      
      main.serialPort.closePort();   
      main.defaultPort = null;
      main.defaultPort_descriptive = null;
   }
*/
}   
   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void RS422_Check_Serial_Ports(int completed_checks_serial_ports)
{
   // called from - main_RS232_RS422.RS422_initComponents() [main_RS232_RS422.java]
   //             - RS422_init_new_aws_data_received_check_timer() [main_RS232_RS422.java]
   //
   // AWS connection (not used if barometer connected)
   
   int teller                                        = -1;
   //String[] serial_ports_portid_array                = new String[main.NUMBER_COM_PORTS];
   SerialPort[] serial_ports_portid_array            = new SerialPort[main.NUMBER_COM_PORTS];
   String info                                       = "[AWS] start up: no AWS found";
   String hulp_parity                                = "";                     // for message writing on java console

   
   // initialisation
   for (int i = 0; i < main.NUMBER_COM_PORTS; i++)
   {
      serial_ports_portid_array[i]                   = null;
   }

   // initialisation
   main.defaultPort                                  = null;
   main.defaultPort_descriptive                      = null;
   
   
   if (main.prefered_COM_port.equals("AUTOMATICALLY"))
   {
      // Temporary message box, (pop-up for only a short time) automatically disappears
      //
      if (completed_checks_serial_ports == 0)                  // only show this temporary meassagebox at first port-checking-round
      {
         final JOptionPane pane_begin = new JOptionPane("[AWS] checking serial com ports", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
         final JDialog checking_ports_begin_dialog = pane_begin.createDialog(main.APPLICATION_NAME);

         Timer timer_begin = new Timer(1000, new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               checking_ports_begin_dialog.dispose();
            }
         });
         timer_begin.setRepeats(false);
         timer_begin.start();
         checking_ports_begin_dialog.setVisible(true);
      }

      // Get sorted array of serial ports in the system
	   //main.portList = SerialPort.getCommPorts();//main.portList = SerialPortList.getPortNames();
      //main.serialPorts = SerialPort.getCommPorts();
      SerialPort[] serialPorts = SerialPort.getCommPorts();//main.portList = SerialPortList.getPortNames();

      for (int portNo = 0; portNo < serialPorts.length; portNo++)//for (int i = 0; i < main.portList.length && i < main.NUMBER_COM_PORTS; i++) 
      {
         teller++;
         SerialPort serialPort_test = SerialPort.getCommPort(serialPorts[portNo].getSystemPortName()); 
         serialPort_test.openPort();  
         
         if (serialPort_test.isOpen()) 
         { 
            serial_ports_portid_array[teller] = serialPorts[portNo];
            serialPort_test.closePort();
            String message = "[AWS] found serial port (ok): " + serialPorts[portNo].getDescriptivePortName();
            main.log_turbowin_system_message(message);
         } // if (serialPort_test.isOpen()) 
         else
         {
            serial_ports_portid_array[teller] = null;
            System.out.println("couldn't open: " + serialPorts[portNo].getDescriptivePortName());
         } // else
                 

         // NB opening a port cannot be done in SwingWorker, so must be done here !!

	   } //  for (int i = 0; i < main.portList.length && i < main.NUMBER_COM_PORTS; i++)


      for (int i = 0; i < main.NUMBER_COM_PORTS; i++)                      // max number ports to open
      {
         if (serial_ports_portid_array[i] != null)                         // so serial port present (and also not in use)
         {
            SerialPort serialPort_test = SerialPort.getCommPort(serial_ports_portid_array[i].getSystemPortName()); //SerialPort serialPort_test = new SerialPort(serial_ports_portid_array[i]);
            
            serialPort_test.openPort();  
            if (serialPort_test.isOpen()) 
            { 
               if (main.parity == 0)
               {
                  hulp_parity = "none";
               }
               else if (main.parity == 1)
               {
                  hulp_parity = "odd";
               }
               else if (main.parity == 2)
               {
                  hulp_parity = "even";
               }               
               
               System.out.println("[AWS] trying to open with " + String.valueOf(main.bits_per_second) + " " + hulp_parity + " " + String.valueOf(main.data_bits) + " " + String.valueOf(main.stop_bits) + " " + serial_ports_portid_array[i].getDescriptivePortName());
               serialPort_test.setComPortParameters(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
               serialPort_test.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
               
               if (main.RS232_connection_mode == 3)   // EUCAWS
               {
                  System.out.println("[AWS] Writing \"" + "?" + "\" to " + serial_ports_portid_array[i].getDescriptivePortName()); 
                  String messageString_info = "?\r\n";                    // or "?\r" 
                  byte[] bytes_message = messageString_info.getBytes(StandardCharsets.UTF_8); // Java 7+ only
                  serialPort_test.writeBytes(bytes_message, bytes_message.length);  ///Write data to port 
               
                  try
                  {
                     Thread.sleep(2000);                                                  // 1000 seems too short on a few systems
                  }
                  catch (InterruptedException ex){ }     
                  
                  // the response
                  //
                  //String response = serialPort_test.readString();   
                  //System.out.println(response);
                  
                  String response = "";
                  serialPort_test.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);
                  try 
                  {
                     byte[] readBuffer = new byte[64];
                     serialPort_test.readBytes(readBuffer, readBuffer.length);
                     response = new String(readBuffer, StandardCharsets.UTF_8);
                     System.out.println(response);
                  } 
                  catch (Exception e) 
                  { 
                     System.out.println(e);
                  }
                  
                  if (response != null && response.indexOf("EUCAWS") >= 0)   
                  {
                     info = "[AWS] found AWS on port: " + serial_ports_portid_array[i].getDescriptivePortName();
                     System.out.print("--- ");
                     main.log_turbowin_system_message(info);
                     System.out.println("");
                     
                     serialPort_test.closePort();   
                     main.defaultPort = serial_ports_portid_array[i].getSystemPortName();
                     main.defaultPort_descriptive = serial_ports_portid_array[i].getDescriptivePortName();
                     break;
                  }
                  else 
                  {
                     info = "[AWS] no AWS found";
                     System.out.println("--- " + info);
                     System.out.println("");
                     serialPort_test.closePort();   
                  }
               } // if (main.RS232_connection_mode == 3)   // EUCAWS serial
               else if (main.RS232_connection_mode == 9)   // OMC-140 serial
               {
                  try
                  {
                     Thread.sleep(2000);                                      // 1000 seems too short on a few systems
                  }
                  catch (InterruptedException ex){ }                 
               
                  //String response = serialPort_test.readString();   
                  //System.out.println(response);
 
                  // the response   
                  String response = "";
                  // NB The TIMEOUT_READ_SEMI_BLOCKING mode specifies that the corresponding call will block until either newReadTimeout milliseconds of inactivity have elapsed or at least 1 byte of data can be read.
                  //
                  // if testing OMC-140 with the EUCAWS simulator then set newReadTimeout >= 60000 because data send every minute
                  // in a real situation the OMC-140 will send several messages (not only PEUMB) every minute
                  // so newReadTimeout set to 20000 will be ok
                  //  
                  //serialPort_test.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 2000, 0); // till 11-06-2018
                  serialPort_test.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 20000, 0);   // from 11-06-2018
                  try 
                  {
                     //while (true) //NB while(true) doesn't work (idefinite loop, despite the newReadTimeout...) !!!!!!!!!
                     //{
                        byte[] readBuffer = new byte[1024];            // NB keep size 1024 because first part of incoming data is rubish
                        //int numRead = serialPort_test.readBytes(readBuffer, readBuffer.length);
                        serialPort_test.readBytes(readBuffer, readBuffer.length);
                        //System.out.println("Read " + numRead + " bytes.");
                        response = new String(readBuffer, StandardCharsets.UTF_8);
                        System.out.println(response);
                     //}
                  } 
                  catch (Exception e) 
                  { 
                     System.out.println(e);
                  }              
               
               
                  if (response != null && (response.indexOf("$") >= 0 || response.indexOf("$") >= 0)) // NB this is the response of only the last line of the readBuffer
                  {
                     info = "[AWS] found AWS on port: " + serial_ports_portid_array[i].getDescriptivePortName();
                     System.out.print("--- ");
                     main.log_turbowin_system_message(info);
                     System.out.println("");
                     
                     serialPort_test.closePort();   
                     main.defaultPort = serial_ports_portid_array[i].getSystemPortName();
                     main.defaultPort_descriptive = serial_ports_portid_array[i].getDescriptivePortName();
                     break;
                  }
                  else 
                  {
                     serialPort_test.closePort();
                     info = "[AWS] no AWS found";
                     System.out.println("--- " + info);
                     System.out.println("");
                  }    
               } // else if (main.RS232_connection_mode == 9)
               
            } // if (serialPort_test.isOpen()) 
            else
            {
               System.out.println("[AWS] couldn't open: " + serial_ports_portid_array[i]);
            }
            
         } // if (serial_ports_portid_array[i] != null)
      } // for (int i = 0; i < NUMBER_COM_PORTS; i++)

      if (main.defaultPort != null)
      {
         // temporary message box (AWS was found) 
         final JOptionPane pane_end = new JOptionPane(info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
         final JDialog checking_ports_end_dialog = pane_end.createDialog(main.APPLICATION_NAME);

         Timer timer_end = new Timer(1500, new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               //checking_ports_end_dialog.setVisible(false);
               // or maybe you'll need dialog.dispose() instead?
               checking_ports_end_dialog.dispose();
            }
         });
         timer_end.setRepeats(false);
         timer_end.start();
         checking_ports_end_dialog.setVisible(true);
      }
      else
      {
         // No com port with an AWS connected found
         
         // logging and blocking message box (no AWS found); but ony show message if it is the last port-checking-round 
         if (completed_checks_serial_ports == MAX_COMPLETED_AWS_PORT_CHECKS - 1)
         {
            main.log_turbowin_system_message(info);       // default info used here: "[AWS] start up: no AWS found";
            
            if (main.obsolate_data_flag == false) 
            {
               // NB so only show this blocking messagebox the first time at start up but not in retrying to establish a new connection mode
               //    see also: RS422_init_new_aws_data_received_check_timer() [main_RS232_RS422.java]
               JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
            }
         }
      } // else
 
   } // if (main.prefered_COM_port.equals("AUTOMATICALLY"))
   else // fixed COM port selected
   {
      SerialPort serialPort_test = SerialPort.getCommPort(main.prefered_COM_port);//SerialPort serialPort_test = new SerialPort(main.prefered_COM_port);
      serialPort_test.openPort();
         
      if (serialPort_test.isOpen())   
      {   
         serialPort_test.closePort();
         
         // OK, no problems encoutered
         main.defaultPort = main.prefered_COM_port;
         main.defaultPort_descriptive = main.prefered_COM_port;  // NB this is a work around this is not the exact descriptive term as obtained via the AUTOMTICALLY branch
         info = "[AWS] " + main.prefered_COM_port + " serial COM port available";
         main.log_turbowin_system_message(info);
         
         final JOptionPane pane_begin = new JOptionPane(info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
         final JDialog checking_ports_begin_dialog = pane_begin.createDialog(main.APPLICATION_NAME);

         Timer timer_begin = new Timer(1000, new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               checking_ports_begin_dialog.dispose();
            }
         });
         timer_begin.setRepeats(false);
         timer_begin.start();
         checking_ports_begin_dialog.setVisible(true); 
      }
      //catch (SerialPortException ex) 
      else
      {
         // error opening predefined (by the user)port, reset/warnings; logging and blocking messagebox
         //System.out.println(ex);
         info = "[AWS] " + main.prefered_COM_port + " serial COM port not available";
         main.log_turbowin_system_message(info);
         if (main.obsolate_data_flag == false) 
         {
            // NB so only show this blocking messagebox the first time at start up but not in retrying to establish a new connection mode
            //    see also: RS422_init_new_aws_data_received_check_timer() [main_RS232_RS422.java]
            JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         }
         //System.out.println(info);
         main.defaultPort = null;
         main.defaultPort_descriptive = null;
      }
   } // else (fixed COM port selected)

}
   


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void RS232_GPS_NMEA_0183_Check_Default_Serial_Port()
{
   // called from: RS232_GPS_NMEA_0183_init_new_data_received_check_timer() [main_RS232_RS422.java]
   //
   // NB after power break or temporarily device failure the (default/starting)serial port is still open but the listener must be restarted
   
   String info = "";
   
   
   if (main.GPS_serialPort.isOpen())
   {
      info = "[GPS] starting-com-port still open (" + main.GPS_serialPort.getDescriptivePortName() + ")";
      main.log_turbowin_system_message(info);
      
      main.GPS_serialPort.closePort();                     // will be opened again later
      // NB GPS_defaultPort and GPS_defaultPort_descriptive still preserve their start-up value
   }
   else
   {
      info = "[GPS] starting-com-port closed (" + main.GPS_serialPort.getDescriptivePortName() + ")";
      main.log_turbowin_system_message(info);
      
      GPS_defaultPort                                  = null;
      GPS_defaultPort_descriptive                      = null;
   }
}


   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void RS232_Check_Default_Serial_Port()
{
   // called from: RS232_And_WiFi_init_new_sensor_data_received_check_timer() [main_RS232_RS422.java]
   //
   // NB after power break or temporarily device failure the (default/starting)serial port is still open but the listener must be restarted
   
   String info = "";
   
   
   if (main.serialPort.isOpen())
   {
      info = "[BAROMETER] starting-com-port still open (" + main.serialPort.getDescriptivePortName() + ")";
      main.log_turbowin_system_message(info);
      
      main.serialPort.closePort();                     // will be opened again later
      // NB main.defaultPort and main.defaultPort_descriptive still preserve their start-up value
   }
   else
   {
      info = "[BAROMETER] starting-com-port closed (" + main.serialPort.getDescriptivePortName() + ")";
      main.log_turbowin_system_message(info);
      
      main.defaultPort                                  = null;
      main.defaultPort_descriptive                      = null;
   }

   
/* BELOW IS OK BUT UNNECESSATY   
   // initialisation
   main.defaultPort                                  = null;
   main.defaultPort_descriptive                      = null;

   
   if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)     // PTB220 or PTB330
   {
      // send data to the barometer
      //
      System.out.println("[BAROMETER] Writing \"" + "S" + "\" to " + main.serialPort.getDescriptivePortName());
      String messageString_stop = "S\r";
      byte[] bytes_message_stop = messageString_stop.getBytes(StandardCharsets.UTF_8); // Java 7+ only
      main.serialPort.writeBytes(bytes_message_stop, bytes_message_stop.length);       // Write data to port 

      try
      {
         Thread.sleep(2000);
      }
      catch (InterruptedException ex){ }     
                  
      System.out.println("[BAROMETER] Writing \"" + "VERS" + "\" to " + main.serialPort.getDescriptivePortName());
      String messageString_info = "VERS\r";
      byte[] bytes_message_info = messageString_info.getBytes(StandardCharsets.UTF_8); // Java 7+ only
      main.serialPort.writeBytes(bytes_message_info, bytes_message_info.length);       // Write data to port 
                  
      try
      {
         Thread.sleep(2000);                                                  // 1000 seems too short on a few systems
      }
      catch (InterruptedException ex){ }     
                  
                  
      String response = "";
      main.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);
      try 
      {
         byte[] readBuffer = new byte[64];
         main.serialPort.readBytes(readBuffer, readBuffer.length);
         response = new String(readBuffer, StandardCharsets.UTF_8);
         System.out.println(response);
      } 
      catch (Exception e) 
      { 
         System.out.println(e);
      }
                  
                  
      if (response != null && response.indexOf("PTB220") >= 0) 
      {
         info = "[BAROMETER] found PTB220 on port: " + main.serialPort.getDescriptivePortName();
         System.out.print("--- ");
         main.log_turbowin_system_message(info);
         System.out.println("");

         main.serialPort.closePort();
         main.defaultPort = main.serialPort.getSystemPortName();
         main.defaultPort_descriptive = main.serialPort.getDescriptivePortName();
         //break;
      } //else if (response.indexOf("PTB330") >= 0)
      else if (response != null && response.indexOf("PTB330") >= 0) 
      {
         // NB after command "?" a BTB330 barometer will always return 4800 bit rate (or bit rate set for user port)
         //    despite the PTB330 was opened via its service port (19200 bit rate)

         info = "[BAROMETER] found PTB330 on port: " + main.serialPort.getDescriptivePortName();
         System.out.print("--- ");
         main.log_turbowin_system_message(info);
         System.out.println("");

         main.serialPort.closePort();
         main.defaultPort = main.serialPort.getSystemPortName();
         main.defaultPort_descriptive = main.serialPort.getDescriptivePortName();
      } 
      else 
      {
         info = "[BAROMETER] no barometer found";
         System.out.println("--- " + info);
         System.out.println("");
         
         main.serialPort.closePort();
         main.defaultPort = null;
         main.defaultPort_descriptive = null;
      }
   
   } // if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2) 
   else if (main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5) // Mintaka Duo or MintakaStar USB
   {
      // send data to the barometer
      //
      System.out.println("[BAROMETER] Writing \"" + "asq" + "\" to " + main.serialPort.getDescriptivePortName());
      String messageString_stop = "asq\r";
      byte[] bytes_message_stop = messageString_stop.getBytes(StandardCharsets.UTF_8); // Java 7+ only
      main.serialPort.writeBytes(bytes_message_stop, bytes_message_stop.length);       // Write data to port 

      try 
      {
         Thread.sleep(2000);
      } 
      catch (InterruptedException ex) {  }

      System.out.println("[BAROMETER] Writing \"" + "dm ma" + "\" to " + main.serialPort.getDescriptivePortName());
      String messageString_info = "dm ma\r";
      byte[] bytes_message_info = messageString_info.getBytes(StandardCharsets.UTF_8); // Java 7+ only
      main.serialPort.writeBytes(bytes_message_info, bytes_message_info.length);       // Write data to port 

      try 
      {
         Thread.sleep(2000);                                                  // 1000 seems too short on a few systems
      } 
      catch (InterruptedException ex) { }

      // the response
      //
      String response = "";
      main.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);
      try 
      {
         byte[] readBuffer = new byte[1024];
         main.serialPort.readBytes(readBuffer, readBuffer.length);
         response = new String(readBuffer, StandardCharsets.UTF_8);
         System.out.println(response);
      } 
      catch (Exception e) 
      {
         System.out.println(e);
      }

      if (response != null && response.indexOf("MintakaDuo") >= 0) 
      {
         info = "[BAROMETER] found Mintaka Duo on port: " + main.serialPort.getDescriptivePortName();
         System.out.print("--- ");
         main.log_turbowin_system_message(info);
         System.out.println("");

         main.serialPort.closePort();
         main.defaultPort = main.serialPort.getSystemPortName();
         main.defaultPort_descriptive = main.serialPort.getDescriptivePortName();
      } 
      else if (response != null && response.indexOf("Mintaka Star") >= 0) 
      {
         info = "[BAROMETER] found Mintaka Star on port: " + main.serialPort.getDescriptivePortName();
         System.out.print("--- ");
         main.log_turbowin_system_message(info);
         System.out.println("");

         main.serialPort.closePort();
         main.defaultPort = main.serialPort.getSystemPortName();
         main.defaultPort_descriptive = main.serialPort.getDescriptivePortName();
      } 
      else 
      {
         info = "[BAROMETER] no barometer found on starting port (" + main.serialPort.getDescriptivePortName() + ")";
         System.out.println("--- " + info);
         System.out.println("");
         
         main.serialPort.closePort();
         main.defaultPort = null;
         main.defaultPort_descriptive = null;
      }

   } //  else if (main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5) 
*/   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void RS232_Check_Serial_Ports_8(int completed_checks_serial_ports)
{
   // called from: main_RS232_RS424.RS232_initComponents()
   //
      
   ///////// only used for PTB220/PTB330/Mintaka Duo/MintakaStar USB barometers (not for EUCAWS) ////////   
      
   //SerialPort serialPort_test                        = null;
   int teller                                        = -1;
   //String[] serial_ports_portid_array                = new String[main.NUMBER_COM_PORTS];
   SerialPort[] serial_ports_portid_array            = new SerialPort[main.NUMBER_COM_PORTS];
   String info                                       = "[BAROMETER] start up: no barometer found";
   String hulp_parity                                = "";                     // for message writing on java console

      
   // initialisation
   for (int i = 0; i < main.NUMBER_COM_PORTS; i++)
   {
      serial_ports_portid_array[i]                   = null;
   }

   // initialisation
   main.defaultPort                                  = null;
   main.defaultPort_descriptive                      = null;
   

   ////////// automatically detecting COM port ///////////
   //
   if (main.prefered_COM_port.equals("AUTOMATICALLY"))
   {
   
      // Temporary message box, (pop-up for only a short time) automatically disappears
      //
      if (completed_checks_serial_ports == 0)                  // only show this temporary meassagebox at first port-checking-round
      {
         final JOptionPane pane_begin = new JOptionPane("[BAROMETER] checking serial com ports", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
         final JDialog checking_ports_begin_dialog = pane_begin.createDialog(main.APPLICATION_NAME);

         Timer timer_begin = new Timer(1000, new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               checking_ports_begin_dialog.dispose();
            }
         });
         timer_begin.setRepeats(false);
         timer_begin.start();
         checking_ports_begin_dialog.setVisible(true);
      }

      // list all the available ports
	   SerialPort[] serialPorts = SerialPort.getCommPorts();//main.portList = SerialPortList.getPortNames();

      for (int portNo = 0; portNo < serialPorts.length; portNo++)//for (int i = 0; i < main.portList.length && i < main.NUMBER_COM_PORTS; i++) 
      {
         // NB opening a port cannot be done in SwingWorker, so must be done here !!
         
         teller++;
         //SerialPort serialPort_test = SerialPort.getCommPort(serialPorts[portNo].getDescriptivePortName()); 
         SerialPort serialPort_test = SerialPort.getCommPort(serialPorts[portNo].getSystemPortName()); 
         serialPort_test.openPort();  
         
         if (serialPort_test.isOpen()) 
         { 
            //serial_ports_portid_array[teller] = serialPorts[portNo].getSystemPortName();
            serial_ports_portid_array[teller] = serialPorts[portNo];
            serialPort_test.closePort();
            String message = "[BAROMETER] found serial port (ok): " + serialPorts[portNo].getDescriptivePortName();
            main.log_turbowin_system_message(message);
         } // if (serialPort_test.isOpen()) 
         else
         {
            serial_ports_portid_array[teller] = null;
            System.out.println("[BAROMETER] couldn't open: " + serialPorts[portNo].getDescriptivePortName());
         } // else      
	   } // for (int portNo = 0; portNo < serialPorts.length; portNo++)


      for (int i = 0; i < main.NUMBER_COM_PORTS; i++)                      // max aantal te scannen poorten
      {
         if (serial_ports_portid_array[i] != null)                         // dus serial port aanwezig (en ook niet in gebruik)
         {
            //SerialPort serialPort_test = SerialPort.getCommPort(serial_ports_portid_array[i]);//serialPort_test = new SerialPort(serial_ports_portid_array[i]);
            SerialPort serialPort_test = SerialPort.getCommPort(serial_ports_portid_array[i].getSystemPortName());
            serialPort_test.openPort();  
            if (serialPort_test.isOpen()) 
            {
               serialPort_test.openPort();//Open serial port

               if (main.parity == 0)
               {
                  hulp_parity = "none";
               }
               else if (main.parity == 1)
               {
                  hulp_parity = "odd";
               }
               else if (main.parity == 2)
               {
                  hulp_parity = "even";
               }               
               
               System.out.println("[BAROMETER] trying to open with " + String.valueOf(main.bits_per_second) + " " + hulp_parity + " " + String.valueOf(main.data_bits) + " " + String.valueOf(main.stop_bits) + " " + serial_ports_portid_array[i].getDescriptivePortName());
               serialPort_test.setComPortParameters(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
               serialPort_test.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
               
               if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)     // PTB220 or PTB330
               {
                  // send data to the barometer
                  //
                  //System.out.println("[BAROMETER] Writing \"" + "S" + "\" to " + serialPort_test.getDescriptivePortName());
                  System.out.println("[BAROMETER] Writing \"" + "S" + "\" to " + serial_ports_portid_array[i].getDescriptivePortName());
                  String messageString_stop = "S\r";
                  //serialPort_test.writeBytes(messageString_stop.getBytes());                     // Write data to port
                  byte[] bytes_message_stop = messageString_stop.getBytes(StandardCharsets.UTF_8); // Java 7+ only
                  serialPort_test.writeBytes(bytes_message_stop, bytes_message_stop.length);       // Write data to port 

                  try
                  {
                     Thread.sleep(2000);
                  }
                  catch (InterruptedException ex){ }     
                  
                  //System.out.println("[BAROMETER] Writing \"" + "VERS" + "\" to " + serialPort_test.getDescriptivePortName());
                  System.out.println("[BAROMETER] Writing \"" + "VERS" + "\" to " + serial_ports_portid_array[i].getDescriptivePortName());
                  String messageString_info = "VERS\r";
                  //serialPort_test.writeBytes(messageString_info.getBytes());                     // Write data to port
                  byte[] bytes_message_info = messageString_info.getBytes(StandardCharsets.UTF_8); // Java 7+ only
                  serialPort_test.writeBytes(bytes_message_info, bytes_message_info.length);       // Write data to port 
                  
                  try
                  {
                     Thread.sleep(2000);                                                  // 1000 seems too short on a few systems
                  }
                  catch (InterruptedException ex){ }     
                  
                  // the response
                  //
                  //String response = serialPort_test.readString();   
                  //System.out.println(response);
                  
                  String response = "";
                  serialPort_test.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);
                  try 
                  {
                     //while (true)
                     //{
                        byte[] readBuffer = new byte[64];
                        serialPort_test.readBytes(readBuffer, readBuffer.length);
                        response = new String(readBuffer, StandardCharsets.UTF_8);
                        System.out.println(response);
                     //}
                  } 
                  catch (Exception e) 
                  { 
                     System.out.println(e);
                  }
                 //serialPort_test.closePort();                  
                  
                  
                  //if (response.indexOf("PTB220") >= 0)
                  if (response != null && response.indexOf("PTB220") >= 0)   
                  {
                     info = "[BAROMETER] found PTB220 on port: " + serial_ports_portid_array[i].getDescriptivePortName();
                     System.out.print("--- ");
                     main.log_turbowin_system_message(info);
                     System.out.println("");
                     
                     serialPort_test.closePort();   
                     main.defaultPort = serial_ports_portid_array[i].getSystemPortName();
                     main.defaultPort_descriptive = serial_ports_portid_array[i].getDescriptivePortName();
                     break;
                  }
                  //else if (response.indexOf("PTB330") >= 0)
                  else if (response != null && response.indexOf("PTB330") >= 0)   
                  {
                     // NB after command "?" a BTB330 barometer will always return 4800 bit rate (or bit rate set for user port)
                     //    despite the PTB330 was opened via its service port (19200 bit rate)

                     info = "[BAROMETER] found PTB330 on port: " + serial_ports_portid_array[i].getDescriptivePortName();
                     System.out.print("--- ");
                     main.log_turbowin_system_message(info);
                     System.out.println("");
                     
                     serialPort_test.closePort();   
                     main.defaultPort = serial_ports_portid_array[i].getSystemPortName();
                     main.defaultPort_descriptive = serial_ports_portid_array[i].getDescriptivePortName();
                     //main.defaultPort = serial_ports_portid_array[i];
                     break;
                  }    
                  else 
                  {
                     info = "[BAROMETER] no barometer found";
                     System.out.println("--- " + info);
                     System.out.println("");
                     serialPort_test.closePort();   
                  }
                  
               } // if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2) 
               else if (main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5 || main.RS232_connection_mode == 7)   // Mintaka Duo or MintakaStar USB or Mintaka StarX USB
               {
                  // send data to the barometer
                  //
                  System.out.println("[BAROMETER] Writing \"" + "asq" + "\" to " + serial_ports_portid_array[i].getDescriptivePortName());
                  String messageString_stop = "asq\r";
                  //String messageString_stop = "asq\r\n";
                  //serialPort_test.writeBytes(messageString_stop.getBytes());                     // Write data to port
                  byte[] bytes_message_stop = messageString_stop.getBytes(StandardCharsets.UTF_8); // Java 7+ only
                  serialPort_test.writeBytes(bytes_message_stop, bytes_message_stop.length);       // Write data to port 

                  
                  try
                  {
                     Thread.sleep(2000);
                  }
                  catch (InterruptedException ex){ }                      
                  
                  System.out.println("[BAROMETER] Writing \"" + "dm ma" + "\" to " + serial_ports_portid_array[i].getDescriptivePortName());
                  String messageString_info = "dm ma\r";
                  //serialPort_test.writeBytes(messageString_info.getBytes());                     // Write data to port
                  byte[] bytes_message_info = messageString_info.getBytes(StandardCharsets.UTF_8); // Java 7+ only
                  serialPort_test.writeBytes(bytes_message_info, bytes_message_info.length);       // Write data to port 


                  try
                  {
                     Thread.sleep(2000);                                                  // 1000 seems too short on a few systems
                  }
                  catch (InterruptedException ex){ }                      
                  
                  
                  // the response
                  //
                  //String response = serialPort_test.readString();   
                  //System.out.println(response);    
                  String response = "";
                  serialPort_test.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);
                  try 
                  {
                     //while (true)
                     //{
                        byte[] readBuffer = new byte[1024];
                        //int numRead = serialPort_test.readBytes(readBuffer, readBuffer.length);
                        serialPort_test.readBytes(readBuffer, readBuffer.length);
                        //System.out.println("Read " + numRead + " bytes.");
                        response = new String(readBuffer, StandardCharsets.UTF_8);
                        System.out.println(response);
                     //}
                  } catch (Exception e) 
                  { 
                     System.out.println(e);
                  }
                  
                  if (response != null && response.indexOf("MintakaDuo") >= 0)
                  {
                     info = "[BAROMETER] found Mintaka Duo on port: " + serial_ports_portid_array[i].getDescriptivePortName();
                     System.out.print("--- ");
                     main.log_turbowin_system_message(info);
                     System.out.println("");
                     
                     serialPort_test.closePort();   
                     main.defaultPort = serial_ports_portid_array[i].getSystemPortName();
                     main.defaultPort_descriptive = serial_ports_portid_array[i].getDescriptivePortName();
                     //main.defaultPort = serial_ports_portid_array[i];
                     break;
                  }        
                  else if (response != null && response.indexOf("Mintaka Star") >= 0)
                  {
                     info = "[BAROMETER] found Mintaka Star on port: " + serial_ports_portid_array[i].getDescriptivePortName();
                     System.out.print("--- ");
                     main.log_turbowin_system_message(info);
                     System.out.println("");
                     
                     serialPort_test.closePort();   
                     main.defaultPort = serial_ports_portid_array[i].getSystemPortName();
                     main.defaultPort_descriptive = serial_ports_portid_array[i].getDescriptivePortName();
                     //main.defaultPort = serial_ports_portid_array[i];
                     break;
                  }    
                  else 
                  {
                     info = "[BAROMETER] no barometer found";
                     System.out.println("--- " + info);
                     System.out.println("");
                     serialPort_test.closePort();   
                  }                  
                  
               } //  else if (main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5) 
               
            } // if
            //catch (SerialPortException ex)
            else        
            {
               //System.out.println(ex);
               System.out.println("[BAROMETER] couldn't open: " + serial_ports_portid_array[i].getDescriptivePortName());
            }
      
         } // if (serial_ports_portid_array[i] != null)
      } // for (int i = 0; i < main.NUMBER_COM_PORTS; i++) 

      if (main.defaultPort != null)
      {
         // temporary message box (barometer was found) 
         final JOptionPane pane_end = new JOptionPane(info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
         final JDialog checking_ports_end_dialog = pane_end.createDialog(main.APPLICATION_NAME);

         Timer timer_end = new Timer(1500, new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               //checking_ports_end_dialog.setVisible(false);
               // or maybe you'll need dialog.dispose() instead?
               checking_ports_end_dialog.dispose();
            }
         });
         timer_end.setRepeats(false);
         timer_end.start();
         checking_ports_end_dialog.setVisible(true);
      }
      else
      {
         // No com port with barometer attached found
         
         // blocking message box + logging (no barometer found); but only show message if it is the last port-checking-round 
         if (completed_checks_serial_ports == MAX_COMPLETED_BAROMETER_PORT_CHECKS - 1)
         {
            main.log_turbowin_system_message(info);  // default info = "[BAROMETER] start up: no barometer found";
            if (main.obsolate_data_flag == false)  // eg at start up of this application
            {
               // NB no blocking messagebox if in "trying to restart the RS232 listener" mode (see Function: RS232_And_WiFi_init_new_sensor_data_received-check_timer() [main_RS232_RS422.java]
               JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
            }
         }
      } // else
   } // if (main.prefered_COM_port.equals("AUTOMATICALLY"))
   
   ///////// fixed COM port name or number ///////
   //
   else // fixed COM port selected
   {
      //serialPort_test = new SerialPort(main.prefered_COM_port);
      SerialPort serialPort_test = SerialPort.getCommPort(main.prefered_COM_port);//SerialPort serialPort_test = new SerialPort(main.prefered_COM_port);
      serialPort_test.openPort();
      if (serialPort_test.isOpen())   
      {
         serialPort_test.closePort();
         
         // OK, no problems
         main.defaultPort = main.prefered_COM_port;             // this construction is ok (prefered COM port is the same as getSystemPortName())
         main.defaultPort_descriptive = main.prefered_COM_port;  // NB this is a work around this is not the exact descriptive term as obtained via the AUTOMTICALLY branch
         info = "[BAROMETER] " + main.prefered_COM_port + ", serial com port available";
         
         main.log_turbowin_system_message(info);
         
         final JOptionPane pane_begin = new JOptionPane(info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
         final JDialog checking_ports_begin_dialog = pane_begin.createDialog(main.APPLICATION_NAME);

         Timer timer_begin = new Timer(1000, new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               checking_ports_begin_dialog.dispose();
            }
         });
         timer_begin.setRepeats(false);
         timer_begin.start();
         checking_ports_begin_dialog.setVisible(true);            
      }
      //catch (SerialPortException ex) 
      else        
      {
         // error opening predefined (by the user)port, reset/warnings; logging and blocking messagebox
         info = "[BAROMETER] " + main.prefered_COM_port + ", serial com port not available";
         main.log_turbowin_system_message(info);
         if (main.obsolate_data_flag == false)  // eg at start up of this application
         {
            // NB no blocking messagebox if in "trying to restart the RS232 listener" mode (see Function: RS232_And_WiFi_init_new_sensor_data_received-check_timer() [main_RS232_RS422.java]
            JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         }
         main.defaultPort = null;
         main.defaultPort_descriptive = null;
         //System.out.println(info);
      } // else    
      
   } // else (fixed COM port selected)
  
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
   public void RS232_Delete_Sensor_Data_Files()
   {
      // NB delete files older thean 200 hours till 1 year back
      
      // message to console
      if (main.RS232_connection_mode == 3 || main.RS232_connection_mode == 9)          // AWS connected
      {
         System.out.println("--- deleting old (> 1 year) sensor data files");
      }
      else
      {
         System.out.println("--- deleting old (> 200 hrs) sensor data files");
      }
      
 /////////////////////////////////////////////////////
/*
      // Het is nu 11.20 lokale tijd (winter) dus 10.20 UTC


       sdf2 = new SimpleDateFormat("yyyy-MM-dd HH.mm");                            // HH hour in day (0-23) let op er is ook een hh (dan hoort er ook a, pm bij)
       sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));                              // <----- DEZE = ESSENTIEEL


       cal_delete_datum_tijd = new GregorianCalendar();
       cal_delete_datum_tijd.add(Calendar.HOUR_OF_DAY, 0);
       System.out.println("--- getTime()" + sdf2.format(cal_delete_datum_tijd.getTime())); // geeft 10.20

       cal_delete_datum_tijd.add(Calendar.HOUR_OF_DAY, -2);
       System.out.println("--- getTime()" + sdf2.format(cal_delete_datum_tijd.getTime())); // geeft 8.20

       System.out.println("--- Date()" + sdf2.format(new Date()));                         // geeft 10.20

       cal_delete_datum_tijd = null;
       cal_delete_datum_tijd = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
       System.out.println("--- getTime()" + sdf2.format(cal_delete_datum_tijd.getTime()));  // geeft 10.20

       cal_delete_datum_tijd = null;
       cal_delete_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));
       System.out.println("--- getTime()" + sdf2.format(cal_delete_datum_tijd.getTime()));  // geeft 10.20

       cal_delete_datum_tijd = null;
       cal_delete_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));
       System.out.println("--- getTime()" + sdf2.format(cal_delete_datum_tijd.getTime()));  // geeft 10.20

*/
//////////////////////////////////////////////////////////


      new SwingWorker<Void, Void>()
      {
         @Override
         protected Void doInBackground() throws Exception
         {
            String file_naam;
            String volledig_path_sensor_data;
            int i_start;
            int i_end;
            
            if (main.RS232_connection_mode == 3 || main.RS232_connection_mode == 9)  // AWS connected
            {
               i_start = 8760;                            // 1 year (365 days * 24 hours)
               i_end = 17520;                             // 2 years
            }
            else
            {
               i_start = 200;                             // 200 hours
               i_end = 8760;                              // 1 year
            }

            //for (int i = 200; i < 8760 + 200; i++)   // 365 dagen van 24 uur = 8760 uur
            for (int i = i_start; i < i_end + i_start; i++)   // 365 dagen van 24 uur = 8760 uur
            {
               cal_delete_datum_tijd = new GregorianCalendar();
               cal_delete_datum_tijd.add(Calendar.HOUR_OF_DAY, -i);

               file_naam = "sensor_data_" + main.sdf3.format(cal_delete_datum_tijd.getTime()) + ".txt";    // is in UTC !!
               //volledig_path_sensor_data = main.logs_dir + java.io.File.separator + java.io.File.separator + file_naam;
               volledig_path_sensor_data = main.logs_dir + java.io.File.separator + file_naam;
               
               //System.out.println(volledig_path_sensor_data);
               File file_sensor_data = new File(volledig_path_sensor_data);
               if (file_sensor_data.exists())
               {
                  file_sensor_data.delete();
               }
            } // for (int i = 200; i < 8760 + 200; i++)

            return null;
         } // protected Void doInBackground() throws Exception
      }.execute(); // new SwingWorker<Void, Void>()
   }

   
 
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void RS232_write_sensor_data_to_file() 
{
      // called from: class RS232_Class_Receive_Sensor_Data() [main_RS232_RS422.java]
      // 
      // 
      // for: PTB220, PTB330, Mintaka Duo, Mintaka Star USB, Mintaka Star WiFi, Mintaka StarX USB, Mintaka StarX WiFi ///////
   
   
      // NB PTB220:
      // er wordt uiteindelijk geschreven bv:
      // $3  996.87 ****.** ****.** 996.87 0   3$
      //
      // maar eigelijk worden er bv 3 stukken string geschreven!!!! bv:
      // $3  996.87 ***
      // *.** ****.**
      // 996.87 0   3 #r #n$
      //
      // NB Dit is vrij normaal voor een serieele aansluiting 14 bytes per keer ingelezen
      //    "De veel gebruikte UART chip type 16550 bevat een buffer van 14 bytes voor ontvangen en
      //    16 bytes voor zenden, zodat de CPU van de PC niet voor iedere byte aandacht aan de poort hoeft te besteden."
      //    (http://nl.wikipedia.org/wiki/Seri%C3%ABle_poort)
      // NB via laptop virtuele seriele poort (USB) lijkt dat er veel minder bytes per keer wordt ingelezen!
      //
      // NB PTB330 via service port alleen een luchtdruk bv 1020.33
      //    PTB330 via user port kan verschillend zijn?? (maar er is wel een default??)
      //
      //
      // NB geen Swingworker hier gebruiken (worden dan alleen stukken geschreven)
      //
   
      BufferedWriter out                                        = null;
      boolean doorgaan                                          = false;
      

      String file_naam = "sensor_data_" + main.sdf3.format(new Date()) + ".txt";
      String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + file_naam;

      if ((main.logs_dir != null) && (main.logs_dir.compareTo("") != 0))
      {
         // NB in Function RS232_initComponents() there was already checked on dir logs_dir
         doorgaan = true;
      }
      

      if (doorgaan)
      {
         try
         {
            //BufferedWriter out = new BufferedWriter(new FileWriter(volledig_path_sensor_data, true));// true means append the specified data to the file i.e. the pre-exist data in a file is not overwritten and the new data is appended after the pre-exist data.
            out = new BufferedWriter(new FileWriter(volledig_path_sensor_data, true));// true means append the specified data to the file i.e. the pre-exist data in a file is not overwritten and the new data is appended after the pre-exist data.

            // NB Alleen onderstaande zou al voldoende zijn als er geen aparte datum/tijd toegevoegd zou hoeven te worden
            ///out.write(total_string);

            if (main.total_string.indexOf("\n") != -1)
            {
               // test-record = date time to be published on main screen to show the last update date-time
               test_record = main.sdf_tsl_2.format(new Date()) + " UTC";      // new Date() -> always in UTC  
               
               out.write(main.total_string.replaceAll("[\r\n]", ""));
               out.write(main.sdf4.format(new Date()));                       // new Date() -> always in UTC
               out.newLine();
            }
            else
            {
               out.write(main.total_string.replace("\r", ""));
            }

         } // try
         catch (Exception e)
         {
            // log
            String info = "unable to write to: " + volledig_path_sensor_data;
            main.log_turbowin_system_message("[barometer] " + e + "; " + info);
            
            // pop-up message
            JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         } // catch
         finally
         {
            try 
            {
               if (out != null)
               {   
                  out.close();
               }
            } 
            catch (IOException ex) 
            {
               String info = ex + "; unable to write to: " + volledig_path_sensor_data;
               main.log_turbowin_system_message("[barometer] " + info);
            }
         } // finally
      } // if (doorgaan)
   }



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void RS232_read_dasboard_values()
{
   // called from: init_dasboard_timer() [DASHBOARD_view.java]
   
   new SwingWorker<String, Void>()
   {
      @Override
      protected String doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record             = null;
         String laatste_record     = null;
         String last_record        = null;

         // initialisation
         //main.sensor_data_record_obs_pressure = "";

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // e.g. 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;
                  
                  
                  // retrieve the last record in the sensor data file
                  //
                  while ((record = in.readLine()) != null)
                  {
                     laatste_record = record;
                  } // while ((record = in.readLine()) != null)
                  
                  
                  
                  // last retrieved record ok
                  //
                  if (laatste_record != null)
                  {
                     //System.out.println("+++ last retrieved record = " + laatste_record);
                     
                     //String record_datum_tijd_minuten = laatste_record.substring(main.type_record_datum_tijd_begin_pos, main.type_record_datum_tijd_begin_pos + 12);  // bv 201302201345
                     
                     int pos = laatste_record.length() -12;                                               // pos is now start position of YYYYMMDDHHmm
                     String record_datum_tijd_minuten = laatste_record.substring(pos, pos + 12);  // YYYYMMDDHHmm has length 12
                     
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     ///System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min

                     if (timeDiff <= TIMEDIFF_SENSOR_DATA)      // max 5 minutes old
                     {
                        last_record = laatste_record;
                     } // 
                     else // if (timeDiff <= TIMEDIFF_SENSOR_DATA) 
                     {
                        //main.sensor_data_record_obs_pressure = "";
                        //System.out.println("--- automatically retrieved barometer reading obsolete");
                        last_record = "";
                     }
                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) {  }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
 
         // clear memory
         main.obs_file_datum_tijd      = null;

         
         return last_record;
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         
         String last_update_record = null;
         try 
         {
            last_update_record = get();
         } 
         catch (InterruptedException | ExecutionException ex) 
         {
            //Logger.getLogger(main_RS232_RS422.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("+++ Function RS232_read_dasboard_values(), when retrieving last sensor data record: " + ex);
         }
         
         String date_time_last_update = main.sdf_tsl_2.format(new Date()) + " UTC";      // new Date() -> always in UTC  
         RS232_compute_dasboard_values(date_time_last_update, last_update_record);
         
      } // protected void done()
   }.execute(); // new SwingWorker <Void, Void>()
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private static void RS232_compute_dasboard_values(final String date_time_last_update, final String last_update_record)
{
   // called from: RS232_read_dasboard_values() [main_RS232_RS422.java]
   //
   // NB only for single barometer on Dashboard
   //
   //
   // NB update the global dasboard var's if ok, otherwise do not reset them but leave the old values unchanged!
   // NB dashboard_double_last_update_record_pressure_ic -> glabal var, link between this function and dashboard dial
   // NB dashboard_string_last_update_record_date_time   -> glabal var, link between this function and dashboard dial   
   // NB dashboard_double_last_update_record_ppp         -> glabal var, link between this function and dashboard dial  
   
   
   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         boolean dashboard_double_last_update_record_pressure_ic_ok = false;
         boolean checksum_ok                                        = false;
         String dashboard_string_last_update_record_pressure        = "";
         String dashboard_string_last_update_record_ppp             = "";
         String local_obs_age = "";
         final int AGE_NOT_OK = 999999;
         int pos1 = 0;                       // position of the first "," in the last record
         int pos2 = 0;                       // position of the second "," in the last record
         int pos3 = 0;                       // position of the third "," in the last record
         int pos4 = 0;                                        
         int pos5 = 0;                                        
         int pos6 = 0;                                        
         int pos7 = 0;                                        
         int pos8 = 0;                                        
         int pos9 = 0;
         int pos10 = 0;                                        
         int pos11 = 0;                                        
         int pos12 = 0;
         int pos13 = 0;                                        
         int pos14 = 0;                       // pos of the "*" 

         //System.out.println("--- last_update_record = " + last_update_record);
   
         if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2 || main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6 || main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8)     // PTB220, PTB330, Mintaka Duo or Mintaka Star USB or WiFi or Mintaka StarX USB or WiFi  
         {
            if (last_update_record.length() > 10)        // NB > 10 is a little bit arbitrary number
            {
               //System.out.println("--- record_checksum = " + record_checksum);
               //System.out.println("--- computed_checksum = " + computed_checksum);
         
               if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6 || main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8)    // Mintaka Star USB or WiFi 0or Mintaka StarX USB or WiFi
               {
                  // Mintaka Star or StarX perform a cheksum check
                  String record_checksum = last_update_record.substring(last_update_record.length() - 14, last_update_record.length() - 12);  // eg "24" from last_update_record: "1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24"
                  String computed_checksum = Mintaka_Star_Checksum(last_update_record);
            
                  checksum_ok = computed_checksum.equals(record_checksum);
               }
               else if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2 || main.RS232_connection_mode == 4) // PTB220, PTB330 or Mintaka Duo
               { 
                  // no cheksum check (Sep 2017) for PTB220, PTB330 and Mintaka Duo. But Mintaka Duo checksum value is available so could be used in the future
                  checksum_ok = true;        
               }
         
               if (checksum_ok)   
               {
                  if (main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)   // Mintaka Duo, Mintaka Star USB, Mintaka Star WiFi
                  {
                     // NB pressure and ppp pos in the saved records is the same for Duo, Star and StarX
                     pos1 = last_update_record.indexOf(",", 0);                                            // position of the first "," in the record
                     pos2 = last_update_record.indexOf(",", pos1 + 1);                                     // position of the second "," in the last record
                     pos3 = last_update_record.indexOf(",", pos2 + 1);                                     // position of the third "," in the last record

                     dashboard_string_last_update_record_pressure = last_update_record.substring(0, pos1);
                     dashboard_string_last_update_record_ppp = last_update_record.substring(pos2 + 1, pos3);
                  }
                  else if (main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8)   // Mintaka StarX USB or Mintaka StarX WiFi
                  {
                     // NB pressure and ppp pos in the saved records is the same for Duo, Star and StarX
                     // StarX example: // 1009.73,1007.73,0.00,0, 52 41.9491N,  6 14.1802E,0,1,7,19.5,65,15.4,12.8,58*16
                     pos1 = last_update_record.indexOf(",", 0);                                              // ML hereafter; position of the first "," in the last record
                     pos2 = last_update_record.indexOf(",", pos1 +1);                                        // ppp hereafter; position of the second "," in the last record
                     pos3 = last_update_record.indexOf(",", pos2 +1);                                        // a hereafter; position of the third "," in the last record
                     pos4 = last_update_record.indexOf(",", pos3 +1);                                        // lat hereafter; position of the 4th "," in the last record
                     pos5 = last_update_record.indexOf(",", pos4 +1);                                        // lon hereafter; position n of the 5th "," in the last record
                     pos6 = last_update_record.indexOf(",", pos5 +1);                                        // course hereafter; position of the 6th "," in the last record
                     pos7 = last_update_record.indexOf(",", pos6 +1);                                        // speed hereafter                                
                     pos8 = last_update_record.indexOf(",", pos7 +1);                                        // elevation hereafter
                     pos9 = last_update_record.indexOf(",", pos8 +1);                                        // air temp
                     pos10 = last_update_record.indexOf(",", pos9 +1);                                       // RH
                     pos11 = last_update_record.indexOf(",", pos10 +1);                                      // wet bulb                                      
                     pos12 = last_update_record.indexOf(",", pos11 +1);                                      // dew point
                     pos13 = last_update_record.indexOf(",", pos12 +1);                                      // observation age  
                     pos14 = last_update_record.indexOf("*", pos13 +1);                                      // pos of the "*" 
                              
                     local_obs_age = last_update_record.substring(pos13 +1, pos14); 
                     main.sensor_data_record_obs_pressure = last_update_record.substring(0, pos1);
                     
                     
                     int int_local_obs_age = AGE_NOT_OK;                 // 999999 = random number but > 99.9
                     if ( (local_obs_age.compareTo("") != 0) && (local_obs_age != null) && (local_obs_age.indexOf("*") == -1) )
                     {
                        try
                        {
                           int_local_obs_age = Integer.parseInt(local_obs_age.trim());
                        }
                        catch (NumberFormatException e)
                        {
                           int_local_obs_age = AGE_NOT_OK; 
                           System.out.println("--- " + "RS232_compute_dasboard_values() " + e);
                        }
                     } // if ( (local_obs_age.compareTo("") != 0) etc.
            
                     if ((int_local_obs_age >= 0) && (int_local_obs_age <= MAX_AGE_STARX_OBS_DATA))
                     {
                        dashboard_string_last_update_record_pressure = last_update_record.substring(0, pos1);
                        dashboard_string_last_update_record_ppp = last_update_record.substring(pos2 + 1, pos3);
                     }
                     else
                     {
                        dashboard_string_last_update_record_pressure = "";
                        dashboard_string_last_update_record_ppp = "";
                     }
                  }
                  else if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)                 // PTB220 or PTB330
                  {
                     int type_record_pressure_begin_pos     = 0;
                     int type_record_ppp_begin_pos          = 0;
                     //int type_record_a_begin_pos            = 0;
               
                     if (main.RS232_connection_mode == 1)       // PTB220
                     {
                        type_record_pressure_begin_pos     = main.RECORD_P_BEGIN_POS_PTB220;
                        type_record_ppp_begin_pos          = main.RECORD_ppp_BEGIN_POS_PTB220;
                        //type_record_a_begin_pos          = main.RECORD_a_BEGIN_POS_PTB220;
                     }
                     else if (main.RS232_connection_mode == 2)  // PTB330   
                     {
                        type_record_pressure_begin_pos     = main.RECORD_P_BEGIN_POS_PTB330;
                        type_record_ppp_begin_pos          = main.RECORD_ppp_BEGIN_POS_PTB330;
                        //type_record_a_begin_pos            = main.RECORD_a_BEGIN_POS_PTB330;
                     }
              
                     dashboard_string_last_update_record_pressure = last_update_record.substring(type_record_pressure_begin_pos, type_record_pressure_begin_pos + 7);
                     dashboard_string_last_update_record_ppp      = last_update_record.substring(type_record_ppp_begin_pos, type_record_ppp_begin_pos + 5);
                    
                     //System.out.println("+++ dashboard_string_last_update_record_pressure = " + dashboard_string_last_update_record_pressure);
                    
                    //String sensor_data_record_obs_a              = last_update_record.substring(type_record_a_begin_pos, type_record_a_begin_pos + 1);
                  } // else if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)   
            
                  if ((dashboard_string_last_update_record_pressure.equals("") == false) && (dashboard_string_last_update_record_pressure != null))
                  {
                     // pressure at sensor height + apply ic
                     double dashboard_double_last_update_record_pressure;
                     try
                     {
                        dashboard_double_last_update_record_pressure = Double.parseDouble(dashboard_string_last_update_record_pressure);
                        dashboard_double_last_update_record_pressure = Math.round(dashboard_double_last_update_record_pressure * 10) / 10.0d;  // bv 998.19 -> 998.2
                     }
                     catch (NumberFormatException e)
                     {
                        System.out.println("--- " + "Function RS232_compute_dasboard_values() " + e);
                        dashboard_double_last_update_record_pressure = Double.MAX_VALUE;
                     }   
             
                     double dashboard_double_barometer_instrument_correction = 0.0;
                     if ((dashboard_double_last_update_record_pressure > 900.0) && (dashboard_double_last_update_record_pressure < 1100.0))
                     {
                        if (main.barometer_instrument_correction.equals("") || (main.barometer_instrument_correction == null))
                        {
                           // so in the case there was never an ic inserted
                           dashboard_double_barometer_instrument_correction = 0.0;        
                        }
                        else
                        {
                           dashboard_double_barometer_instrument_correction = Double.parseDouble(main.barometer_instrument_correction.trim());
                        }
                        
                        
                        if ((dashboard_double_barometer_instrument_correction > -4.0) && (dashboard_double_barometer_instrument_correction < 4.0))
                        {        
                           // 1 digit precision
                           // NB example (double)Math.round(value * 100000d) / 100000d -> rounding for 5 digits precision
                           dashboard_double_last_update_record_pressure_ic = Math.round((dashboard_double_last_update_record_pressure + dashboard_double_barometer_instrument_correction) * 10d) / 10d;
                           dashboard_double_last_update_record_pressure_ic_ok = true;
                        }
                        else // ic out of range -> consider ic = 0
                        {
                            //pressure_sensor_height = Double.toString(hulp_double_pressure_reading);
                           // 1 digit precision
                           dashboard_double_last_update_record_pressure_ic = Math.round(dashboard_double_last_update_record_pressure * 10d) / 10d;
                           dashboard_double_last_update_record_pressure_ic_ok = true;
                        }         
                     } 
                     else
                     {
                        dashboard_double_last_update_record_pressure_ic_ok = false;
                     }
         
                     // only continue if the retrieved pressure was ok
                     //
                     if (dashboard_double_last_update_record_pressure_ic_ok == true)
                     {
                        // date time last update (retrieved pressure was ok, now we can update the date-time of the last update var)
                        dashboard_string_last_update_record_date_time = date_time_last_update;            
                     }
               
                     // ppp: only continue if the retrieved pressure was ok + there was at least 1 char for the ppp 
                     //
                     if (dashboard_double_last_update_record_pressure_ic_ok == true && dashboard_string_last_update_record_ppp.length() >= 1 && dashboard_string_last_update_record_ppp.indexOf("*") == -1)
                     {   
                        // pressure change (ppp; pressure change during last 3 hours)
                        // so pressure was ok, if the ppp is not ok make it invalid, pressure is leading!
                        try
                        {
                           dashboard_double_last_update_record_ppp = Double.parseDouble(dashboard_string_last_update_record_ppp);
                           dashboard_double_last_update_record_ppp = Math.round(dashboard_double_last_update_record_ppp * 10) / 10.0d;  // bv 2.19 -> 2.2
                           
                           if (dashboard_double_last_update_record_ppp > 100.0)
                           {
                              dashboard_double_last_update_record_ppp = Double.MAX_VALUE;
                           }
                        }
                        catch (NumberFormatException e)
                        {
                           System.out.println("--- " + "Function RS232_compute_dasboard_values() " + e);
                           dashboard_double_last_update_record_ppp = Double.MAX_VALUE;
                        }   
                     } // if (dashboard_double_last_update_record_pressure_ic_ok == true)
                  } // if ((dashboard_string_last_update_record_pressure.equals("") == false) && (dashboard_string_last_update_record_pressure != null))
                  else
                  {
                     // do nothing!!
                     // the previuous "dashboard" values are still valid! (but will not updated by this "last_update_record") 
                     System.out.println("+++ Function RS232_compute_dasboard_values(): dashboard_string_last_update_record_pressure = null/empty");
                  }
               } // if (computed_checksum.equals(record_checksum))
               else
               {
                  // do nothing!!
                  // the previuous "dashboard" values are still valid! (but will not updated by this "last_update_record")  
                  System.out.println("+++ Function RS232_compute_dasboard_values(): cheksum not ok");
               }
            } // if (last_update_record.length() > 10) 
            else
            {
               // do nothing!!
               // the previuous "dashboard" values are still valid! (but will not be updated by this "last_update_record")
               System.out.println("+++ Function RS232_compute_dasboard_values(): last_update_record.length() <= 10 or not present");
            }
         } // if (main.RS232_connection_mode == 1 || etc
   
   
         return null;
      } // protected Void doInBackground() throws Exception
   }.execute(); // new SwingWorker<Void, Void>()
           
}





/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
//private static String Mintaka_Star_Checksum(String record
public static String Mintaka_Star_Checksum(String record)   
{
   // called from: - RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_GPS_For_Obs() [main_RS232_RS422.java]
   //              - RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR() [main_RS232_RS422.java]
   //              - RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_PPPP_For_Obs() [main_RS232_RS422.java]
   //              - RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_a_ppp_Data_Files_For_Obs() [main_RS232_RS422.java]
   //              - RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_GPS_For_Obs() [main_RS232_RS422.java]
   //              - RS232_compute_dasboard_values()[main_RS232_RS422.java]
   //              - Read_Sensor_Data_Files_For_Air_Temp_Mintaka_StarX() [RS232_view.java]
   //
   // NB used for string data checking of the Mintaka Star USB/Wifi and the Mintaka StarX USB/Wifi
   
   
   String computed_checksum = "";
   int checksum = 0;
   
   
   // cheksum computed between first pos and '*'
   int start_pos = 0;
   int end_pos   = record.indexOf('*');
   
   // cheksum ok? 
   //
   if (start_pos != -1 && end_pos != -1) 
   {
      for (int i = start_pos; i < end_pos; i++) 
      {
         checksum = checksum ^ record.charAt(i);
      }   
      
      computed_checksum = Integer.toHexString(checksum);
      if (computed_checksum.length() == 1)
      {
         computed_checksum = "0" + computed_checksum;
      }
   } // if (start_pos != -1 && end_pos != -1) 
   
   
   return computed_checksum = computed_checksum.toUpperCase();
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_GPS_For_Obs(final String mode, final boolean StarX)
{
   // called from: - initSynopparameters() [myposition.java]
   
   
   //
   // MINTAKA STAR has an integrated GPS
   // GPS data is part of th saved pressure string eg:
   // <station pressure in mb>,<sea level pressure in mb>,<3 hour pressure tendency>,
   // <WMO tendency characteristic code>,<lat>,<long>,<course>,<speed>,<elevation>*<checksum>
   // where <lat> = ddd mm.mmmm[N|S], <long> = ddd mm.mmmm[E|W], 
   // <course> is True, <speed> in knots, <elevation> in meters
   //
   // 1018.61,1018.61,1.90,2, 15 39.0161N, 89 00.1226W,0,0,0*03
   // 1011.20,1011.20,,, 47 37.5965N,122 31.1453W,0,0,0*31
   //
   //
   // STARX (NB first part of STARX is the same as the STAR)
   // <station pressure in mb>,<sea level pressure in mb>,<3 hour pressure tendency>,
   // <WMO tendency characteristic code>,<lat>,<long>,<course>,<speed>,<elevation>,
   // <temperature>,<relativeHumidity>,<wetBulbTemperature>,<dewPoint>,
   // <humiditySensor pressure in mb>,<observationAge>*<checksum>
   //
   // 1018.12,1018.13,0.00,0, 16 30.4429N, 88 21.9040W,0,2,7,29.4,85,27.4,26.7,1017.89,60*18
   
   
   
   // initialisation
   GPS_latitude             = "";
   GPS_longitude            = "";
   GPS_latitude_hemisphere  = "";
   GPS_longitude_hemisphere = "";
   GPS_latitude_earlier     = "";
   GPS_longitude_earlier    = "";
   
   

   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         // retrieve (almost) most recent recorded position
         //
         //
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record         = null;
         String laatste_record = null;
         String sensor_data_file_naam_datum_tijd_deel_start = null;   // for retrieving GPS position earlier (10 min or 3 hours) eg 2017092504 (file name eg sensor_data_2017092505.txt)
         String date_time_SOG_COG_start = null;                       // for retrieving GPS position earlier (10 min or 3 hours) eg 201709250406 (last 12 char of record)
                 

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // e.g. 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;

                  // retrieve the last record in the sensor data file
                  //
                  while ((record = in.readLine()) != null)
                  {
                     laatste_record = record;
                  } // while ((record = in.readLine()) != null)
                  
                  // check on minimum record length
                  //
                  if (laatste_record != null)
                  {
                     if (!(laatste_record.length() > 15))         // NB > 15 is a little bit arbitrary number (YYYYMMDDHHmm + 3 commas + at leat 2 char pressure value= 15 chars)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Star or StarX format (min. record length) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                     }
                  }
                  
                  // check on correct number of commas in the laatste_record
                  //
                  if (laatste_record != null)
                  {
                     int number_read_commas = 0;
                     int pos = -1;
                     
                     do
                     {
                        pos = laatste_record.indexOf(",", pos + 1);
                        if (pos != -1)     // "," found
                        {
                           number_read_commas++;
                           //System.out.println("+++ number_read_commas = " + number_read_commas);
                        }
                     } while (pos != -1); 
                        
                     if (StarX == false)
                     {
                        if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STAR)
                        {
                           laatste_record = null;
                           System.out.println("--- Mintaka Star format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                        }   
                     }
                     else if (StarX == true)
                     {
                         if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STARX)
                        {
                           laatste_record = null;
                           System.out.println("--- Mintaka StarX format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                        }   
                     }
                  } // if (laatste_record != null)
                  
                  
                  // last retrieved record ok
                  if (laatste_record != null)
                  {
                     int pos = laatste_record.length() -12;                                               // pos is now start position of YYYYMMDDHHmm
                     String record_datum_tijd_minuten = laatste_record.substring(pos, pos + 12);          // YYYYMMDDHHmm has length 12
                     
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     ///System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min
                     
                     
                     
                     // NB COG and SOG must be computed over last 10 minutes (format 101) or last 3 hours (FM13)
                     int COG_SOG_diff_min = 0;
                     if (main.obs_format.equals(main.FORMAT_101)) 
                     {
                        COG_SOG_diff_min = 10;                           // 10 minutes (between positions to compute SOG and COG)      
                     }
                     else if (main.obs_format.equals(main.FORMAT_FM13))
                     {
                        COG_SOG_diff_min = 180;                         // 180 minutes (3 hours, between positions to compute SOG and COG)     
                     }
                     // NB GregorianCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minute)
                     // NB Constructs a GregorianCalendar with the given date and time set for the default time zone with the default locale.
                     // NB month 0..11            // The first month of the year in the Gregorian and Julian calendars is JANUARY which is 0
                     // NB dayOfMonth 1..31       // The first day of the month has value 1
                     // NB hourOfDay: 0.. 23      // Field number for get and set indicating the hour of the day. HOUR_OF_DAY is used for the 24-hour clock. E.g., at 10:04:15.250 PM the HOUR_OF_DAY is 22.

                     String date_time_SOG_COG_end = record_datum_tijd_minuten;  // date time of last retrieved record eg 201709250400 (YYYYMMDDHHmm has length 12)

                     // record_datum_tijd_minuten format: YYYYMMDDHHmm
                     String year_end   = date_time_SOG_COG_end.substring(0, 4);
                     String month_end  = date_time_SOG_COG_end.substring(4, 6);         // January = 01
                     String day_end    = date_time_SOG_COG_end.substring(6, 8);
                     String hour_end   = date_time_SOG_COG_end.substring(8, 10);
                     String minute_end = date_time_SOG_COG_end.substring(10, 12);        
                     GregorianCalendar cal_SOG_COG_date = new GregorianCalendar
                                                                 ( Integer.parseInt(year_end), 
                                                                   Integer.parseInt(month_end) -1,     // convert to internal month representation
                                                                   Integer.parseInt(day_end),
                                                                   Integer.parseInt(hour_end),
                                                                   Integer.parseInt(minute_end)
                                                                  );   
                     cal_SOG_COG_date.setTimeZone(TimeZone.getTimeZone("UTC"));                        // !!!! (otherwise in local pc time zone)
                    
                     cal_SOG_COG_date.add(Calendar.MINUTE, - COG_SOG_diff_min);                    // substract 10 minutes (format 101) or 3 hours (FM13)
                     date_time_SOG_COG_start = main.sdf4.format(cal_SOG_COG_date.getTime()); // sdf4 : yyyyMMddHHmm eg 201701030812 // NB MM = 01 = January!! so SimpleDateformat automatically converts the internal  month representation [January = 0] to the human representation [January = 1]
                     
                     String year_start   = date_time_SOG_COG_start.substring(0, 4);
                     String month_start  = date_time_SOG_COG_start.substring(4, 6);         // January = 01
                     String day_start    = date_time_SOG_COG_start.substring(6, 8);
                     String hour_start   = date_time_SOG_COG_start.substring(8, 10);
                     
                     sensor_data_file_naam_datum_tijd_deel_start = year_start + month_start + day_start + hour_start;    // eg 2017092505
                     
                     // below for testing
                     //System.out.println("+++ sensor_data_file_naam_datum_tijd_deel_start: " + sensor_data_file_naam_datum_tijd_deel_start);
                     

                     if (timeDiff <= TIMEDIFF_SENSOR_DATA)      // eg max 5 minutes old 
                     {
                        
                        // cheksum check
                        //
                        // example Mintaka Star last_record : 1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211
                        // example Mintaka StarX last record: 1018.12,1018.13,0.00,0, 16 30.4429N, 88 21.9040W,0,2,7,29.4,85,27.4,26.7,1017.89,60*18
                        //
                        String record_checksum = laatste_record.substring(laatste_record.length() -14, laatste_record.length() -12);  // eg "24" from record "1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211"
                        String computed_checksum = Mintaka_Star_Checksum(laatste_record);
   
                        // below only for testing (uncomment when testing)
                        //computed_checksum = record_checksum;
                              
                        if (computed_checksum.equals(record_checksum))
                        {
                           //System.out.println("checksum ok"); 
                        
                           // retrieved (from file) record example Mintaka Star: 1029.97,1029.97,-0.90,7, 52 41.9535N,  6 14.1943E,0,0,19*1A201703152006
                           int pos1 = laatste_record.indexOf(",", 0);                                              // position of the first "," in the last record
                           int pos2 = laatste_record.indexOf(",", pos1 +1);                                        // position of the second "," in the last record
                           int pos3 = laatste_record.indexOf(",", pos2 +1);                                        // position of the third "," in the last record
                           int pos4 = laatste_record.indexOf(",", pos3 +1);                                        // position of the 4th "," in the last record
                           int pos5 = laatste_record.indexOf(",", pos4 +1);                                        // position of the 5th "," in the last record
                           int pos6 = laatste_record.indexOf(",", pos5 +1);                                        // position of the 6th "," in the last record
                           //int pos7 = laatste_record.indexOf(",", pos6 +1);                                        // position of the 7th "," in the last record
                        
                           GPS_latitude  = laatste_record.substring(pos4 +1, pos5);
                           GPS_longitude = laatste_record.substring(pos5 +1, pos6);
                           
                           System.out.println("--- sensor data record, GPS latitude: " + GPS_latitude + "(" + record_datum_tijd_minuten + ")");
                           System.out.println("--- sensor data record, GPS longitude: " + GPS_longitude + "(" + record_datum_tijd_minuten + ")");
                        }
                        else
                        {
                           //System.out.println("record checksum = " + record_checksum);
                           //System.out.println("computed checksum = " + computed_checksum);
                           System.out.println("--- checksum NOT ok " + "(" + laatste_record  + ")"); 
                           
                           GPS_latitude = "";
                           GPS_longitude = "";
                        } // else
                        
                     } // if (timeDiff <= 5L)
                     else
                     {
                        GPS_latitude = "";
                        GPS_longitude = "";
                     } // else

                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) {  }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
 
         // clear memory
         main.obs_file_datum_tijd      = null;

 
         
         // retrieve GPS position 10 minutes (format 101) or 3 hours (FM13) earlier
         //
         // NB there is no use to continue if a recent position was not found
         if ( (GPS_latitude.compareTo("") != 0) && (GPS_latitude != null) && (GPS_latitude.indexOf("*") == -1) )
         {
            record         = null;

            // determine sensor data file name of the file with the GPS position of 10 minutes or 3 hours ago
            sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel_start + ".txt";

            // first check if there is a sensor data file present (and not empty)
            volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
            //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

            sensor_data_file = new File(volledig_path_sensor_data);
            if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
            {
               try
               {
                  BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

                  try
                  {
                     record         = null;
                     while ((record = in.readLine()) != null)
                     {
                        if (record.length() > 15)         // NB > 15 is a little bit arbitrary number (YYYYMMDDHHmm + 3 commas + at leat 2 char pressure value= 15 chars)
                        {
                           int pos = record.length() -12;                                               // pos is now start position of YYYYMMDDHHmm
                           String record_datum_tijd_minuten = record.substring(pos, pos + 12);          // YYYYMMDDHHmm has length 12
                           
                           if (record_datum_tijd_minuten.equals(date_time_SOG_COG_start))               // found the corresponding record of 10 min or 3 hrs earlier
                           {
                              String record_checksum = record.substring(record.length() -14, record.length() -12);  // eg "24" from record "1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211"
                              String computed_checksum = Mintaka_Star_Checksum(record);
   
                              // below only for testing (uncomment when testing)
                              //computed_checksum = record_checksum;
                              
                              
                              if (computed_checksum.equals(record_checksum))
                              {
                                 //System.out.println("checksum ok"); 
                        
                                 // extra check on correct number of commas in the earlier (10 min or 3 hrs) record
                                 //
                                 int number_read_commas = 0;
                                 int pos_comma = -1;
                                 
                                 do
                                 {
                                    pos_comma = record.indexOf(",", pos_comma + 1);
                                    if (pos_comma != -1)     // "," found
                                    {
                                       number_read_commas++;
                                       //System.out.println("+++ number_read_commas = " + number_read_commas);
                                    }
                                 } while (pos_comma != -1); 
                        
                                 boolean format_ok = true;
                                 if (StarX == false)
                                 {   
                                    if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STAR)
                                    {
                                       format_ok = false;
                                       System.out.println("--- Mintaka Star format (number commas) rertieved record earlier (for SOG and COG) NOT ok (file: " + volledig_path_sensor_data + "; record: " + record + ")"); 
                                    }   
                                 }
                                 else if (StarX == true)
                                 {
                                    if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STARX)
                                    {
                                       format_ok = false;
                                       System.out.println("--- Mintaka StarX format (number commas) rertieved record earlier (for SOG and COG) NOT ok (file: " + volledig_path_sensor_data + "; record: " + record + ")"); 
                                    }   
                                 }
                                 
                                 if (format_ok)
                                 {   
                                    // retrieved (from file) record example Mintaka Star: 1029.97,1029.97,-0.90,7, 52 41.9535N,  6 14.1943E,0,0,19*1A201703152006
                                    int pos1 = record.indexOf(",", 0);                                              // position of the first "," in the last record
                                    int pos2 = record.indexOf(",", pos1 +1);                                        // position of the second "," in the last record
                                    int pos3 = record.indexOf(",", pos2 +1);                                        // position of the third "," in the last record
                                    int pos4 = record.indexOf(",", pos3 +1);                                        // position of the 4th "," in the last record
                                    int pos5 = record.indexOf(",", pos4 +1);                                        // position of the 5th "," in the last record
                                    int pos6 = record.indexOf(",", pos5 +1);                                        // position of the 6th "," in the last record
                                    //int pos7 = record.indexOf(",", pos6 +1);                                        // position of the 7th "," in the last record
                        
                                    GPS_latitude_earlier  = record.substring(pos4 +1, pos5);
                                    GPS_longitude_earlier = record.substring(pos5 +1, pos6);
                           
                                    System.out.println("--- sensor data record, GPS latitude earlier: " + GPS_latitude_earlier + "(" + record_datum_tijd_minuten + ")");
                                    System.out.println("--- sensor data record, GPS longitude earlier: " + GPS_longitude_earlier + "(" + record_datum_tijd_minuten + ")");
                                 } // if (format_ok)
                                 else
                                 {
                                    GPS_latitude_earlier = "";
                                    GPS_longitude_earlier = "";
                                 }   
                              } // if (computed_checksum.equals(record_checksum))
                              else
                              {
                                 //System.out.println("record checksum = " + record_checksum);
                                 //System.out.println("computed checksum = " + computed_checksum);
                                 System.out.println("--- checksum NOT ok " + "(" + record  + ")"); 
                           
                                 GPS_latitude_earlier = "";
                                 GPS_longitude_earlier = "";
                              } // else
                            
                           } // if (record_datum_tijd_minuten.equals(date_time_SOG_COG_start))
                        } // if (record.length() > 15)  
                        else
                        {
                           GPS_latitude_earlier = "";
                           GPS_longitude_earlier = "";
                        }
                     } // while ((record = in.readLine()) != null)
                  } // try
                  finally
                  {
                     in.close();
                  }

               } // try
               catch (IOException ex) {  }

            } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
         } // if ( (GPS_latitude.compareTo("") != 0) etc.
         
         
         return null;
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         // intialisation (in this inner class)
         boolean fix_ok              = true;
         String message              = "";
         String message_fix_position = "";
         
         //
         //////// latitude (eg " 52 04.7041N") /////////
         //
         if ( (GPS_latitude.compareTo("") != 0) && (GPS_latitude != null) && (GPS_latitude.indexOf("*") == -1) )
         {
            // fill the public string latitude values
            //
            // NB String hulp_GPS_latitude = GPS_latitude.replaceAll("\\s","");        // remove all the whitespaces in the string
            
            String fix_latitude = GPS_latitude.trim();                             // to remove leading and trailing whitespace in the string eg " 52 04.7041N" -> "52 04.7041N"
            int pos_space = fix_latitude.indexOf(" ", 0);                          // find first space (" ") in the string
            myposition.latitude_degrees = fix_latitude.substring(0, pos_space);    // eg "52 04.7041N" -> "52" and "2 04.7041N" -> "2"
            
            String fix_latitude_minutes = fix_latitude.substring(pos_space + 1, fix_latitude.length() -1);  // only the minutes of the latitude eg "52 04.7041N" -> "04.7041"
            BigDecimal bd_lat_min = new BigDecimal(fix_latitude_minutes).setScale(0, RoundingMode.HALF_UP); // 0 decimals rounding
            myposition.latitude_minutes = bd_lat_min.toString();                        // rounded minutes value  e.g. "04.7041N" -> "4" minutes
            if (myposition.latitude_minutes.length() == 1)
            {
               myposition.latitude_minutes =  "0" +  myposition.latitude_minutes;       // "4" -> "04"
            }
            
            GPS_latitude_hemisphere = fix_latitude.substring(fix_latitude.length() -1); // eg "52 04.7041N" -> "N" 
            if (GPS_latitude_hemisphere.toUpperCase().equals("N"))
            {
               myposition.latitude_hemisphere = myposition.HEMISPHERE_NORTH;
            }
            else if (GPS_latitude_hemisphere.toUpperCase().equals("S"))
            {
               myposition.latitude_hemisphere = myposition.HEMISPHERE_SOUTH;
            }
            else
            {
               myposition.latitude_hemisphere = "";
               fix_ok = false;
            }         
           
            // lat int values
            //
            if (fix_ok == true)
            {
               try 
               {
                  myposition.int_latitude_degrees = Integer.parseInt(myposition.latitude_degrees);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }
      
               try 
               {
                  myposition.int_latitude_minutes = Integer.parseInt(myposition.latitude_minutes);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }   
            } // if (fix_ok == true)   
         
            // for latitude (LaLaLa) for IMMT log
            //
            if (fix_ok == true) 
            {
               int int_latitude_minutes_6 =  bd_lat_min.intValue() / 6;                           // devide the minutes by six and disregard the remainder, now tenths of degrees [IMMT rounding!!!}
               String latitude_minutes_6 = Integer.toString(int_latitude_minutes_6);              // convert to String 
               //myposition.lalala_code = myposition.latitude_degrees.trim().replaceFirst("^0+(?!$)", "") + latitude_minutes_6;
               myposition.lalala_code = myposition.latitude_degrees + latitude_minutes_6;

               int len = 3;                                                           // always 3 chars
               if (myposition.lalala_code.length() < len) 
               {
                  // pad on left with zeros
                  myposition.lalala_code = "0000000000".substring(0, len - myposition.lalala_code.length()) + myposition.lalala_code;
               }         
               else if (myposition.lalala_code.length() > len)
               {
                  fix_ok = false;
                  //System.out.println("+++++++++++ lalala_code error: " + myposition.lalala_code);
               }       
            } // if (fix_ok == true)
         } // if ( (GPS_latitude.compareTo("") != 0) && (GPS_latitude != null) && (GPS_latitude.indexOf("*") == -1) )
         else
         {
            fix_ok = false;
         }
         
            
         //
         //////// longitude (eg "  4 14.7041W") /////////
         //
         if ( (fix_ok == true) && (GPS_longitude.compareTo("") != 0) && (GPS_longitude != null) && (GPS_longitude.indexOf("*") == -1) )
         {
            // fill the public string longitude values
            //
            String fix_longitude = GPS_longitude.trim();                           // to remove leading and trailing whitespace in the string eg "  6 14.7041N" -> "6 14.7041N"
            int pos_space = fix_longitude.indexOf(" ", 0);                         // find first space (" ") in the string
            myposition.longitude_degrees = fix_longitude.substring(0, pos_space);  // eg "6 14.7041W" -> "6" and "116 14.7041W" -> "116"
            
            String fix_longitude_minutes = fix_longitude.substring(pos_space + 1, fix_longitude.length() -1);  // only the minutes of the latitude eg "52 04.7041N" -> "04.7041"
            BigDecimal bd_lon_min = new BigDecimal(fix_longitude_minutes).setScale(0, RoundingMode.HALF_UP); // 0 decimals rounding
            myposition.longitude_minutes = bd_lon_min.toString();                 // rounded minutes value  e.g. "04.7041N" -> "5" minutes; "23.7041N" -> "24" minutes
            if (myposition.longitude_minutes.length() == 1)
            {
               myposition.longitude_minutes =  "0" +  myposition.longitude_minutes;        // "5" -> "05"
            }
            
            GPS_longitude_hemisphere = fix_longitude.substring(fix_longitude.length() -1); // eg "4 14.7041W" -> "W" 
            if (GPS_longitude_hemisphere.toUpperCase().equals("E"))
            {
               myposition.longitude_hemisphere = myposition.HEMISPHERE_EAST;
            }
            else if (GPS_longitude_hemisphere.toUpperCase().equals("W"))
            {
               myposition.longitude_hemisphere = myposition.HEMISPHERE_WEST;
            }
            else
            {
               myposition.longitude_hemisphere = "";
               fix_ok = false;
            }  
            
            // fill the public int longitude values
            //
            if (fix_ok == true)
            {
               try 
               {
                  myposition.int_longitude_degrees = Integer.parseInt(myposition.longitude_degrees);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }
      
               try 
               {
                  myposition.int_longitude_minutes = Integer.parseInt(myposition.longitude_minutes);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }
            } // if (fix_ok == true)
            
            
            // for longitude (LoLoLoLo) for IMMT log (NB see also myposition.java) In fact this IMMT preparation only necessary in APR mode
            //
            if (fix_ok == true)
            {
               int int_longitude_minutes_6 =  bd_lon_min.intValue() / 6;                           // devide the minutes by six and disregard the remainder, now tenths of degrees [IMMT rounding!!!}
               String longitude_minutes_6 = Integer.toString(int_longitude_minutes_6);              // convert to String 
               myposition.lolololo_code = myposition.longitude_degrees + longitude_minutes_6;

               int len2 = 4;                                                           // always 4 chars
               if (myposition.lolololo_code.length() < len2) 
               {
                  // pad on left with zeros
                  myposition.lolololo_code = "0000000000".substring(0, len2 - myposition.lolololo_code.length()) + myposition.lolololo_code;
               }  
               else if (myposition.lolololo_code.length() > len2)
               {
                  fix_ok = false;
                   //System.out.println("+++++++++++ lolololo_code error: " + myposition.lolololo_code);
               } 
            } // if (fix_ok == true)
            
         } //  if ( (GPS_longitude.compareTo("") != 0) && (GPS_longitude != null) && (GPS_longitude.indexOf("*") == -1) )
         else
         {
            fix_ok = false;
         }
         
         
         // quadrant of the globe
         //
         if (fix_ok == true)
         {
            // quadrant of the globe for IMMT
            //
            if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
            {
                myposition.Qc_code = "1";
            }
            else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
            {
               myposition.Qc_code = "3";
            }
            else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
            {
               myposition.Qc_code = "5";
            }
            else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
            {
               myposition.Qc_code = "7";
            }
            else
            {
               fix_ok = false;
               //System.out.println("+++++++++++ Qc_code error");
            }
         } // if (fix_ok == true)
         
         if ((fix_ok == true) && (mode.equals("MANUAL")))
         {
            // latitude
            if (myposition.latitude_degrees.compareTo("") != 0)
            {
               myposition.jTextField1.setText(myposition.latitude_degrees); 
            }
            
            if (myposition.latitude_minutes.compareTo("") != 0)
            {
               myposition.jTextField2.setText(myposition.latitude_minutes); 
            }
            
            if (myposition.latitude_hemisphere.compareTo("") != 0)
            {
               if (myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH))
               {
                  myposition.jRadioButton1.setSelected(true);
               }
               else if (myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH))
               {
                  myposition.jRadioButton2.setSelected(true);
               }
            } // if (latitude_hemisphere != "")              
            
            // longitude
            if (myposition.longitude_degrees.compareTo("") != 0)
            {
               myposition.jTextField3.setText(myposition.longitude_degrees); 
            }
      
            if (myposition.longitude_minutes.compareTo("") != 0)
            {
               myposition.jTextField4.setText(myposition.longitude_minutes);
            } 
     
            if (myposition.longitude_hemisphere.compareTo("") != 0)
            {
               if (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST))
               {
                  myposition.jRadioButton3.setSelected(true);
               }
               else if (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST))
               {
                  myposition.jRadioButton4.setSelected(true);
               }
            } // if (latitude_hemisphere != "")  
         } // if ((fix_ok == true) && (mode.equals("MANUAL")))
         
         
         // computing COG and SOG (position interval 10 minutes or 3 hours)
         //
         // 
         boolean GPS_earlier_ok = true;
         
         if ( (GPS_latitude_earlier.compareTo("") == 0) || (GPS_longitude_earlier.compareTo("") == 0) || 
              (GPS_latitude_earlier == null) || (GPS_longitude_earlier == null) || 
              (GPS_latitude_earlier.indexOf("*") != -1) || (GPS_longitude_earlier.indexOf("*") != -1) )
         {
            GPS_earlier_ok = false;
            
            if (main.obs_format.equals(main.FORMAT_101)) 
            {
               // 10 minutes based
               message = "[GPS] error; no 10 minutes earlier position data available for computing COG and SOG";
            }
            else if (main.obs_format.equals(main.FORMAT_FM13))
            {
               // 180 minutes (3 hours, between positions to compute SOG and COG) based   
               message = "[GPS] error; no 3 hours earlier position data available for computing COG and SOG";
            }
            
            main.log_turbowin_system_message(message);
         }
         
         
         if (fix_ok && GPS_earlier_ok)
         {   
            // http://www.movable-type.co.uk/scripts/latlong.html
            
            
            // MINTAKA STAR has an integrated GPS
            // GPS data is part of the saved pressure string eg:
            // <station pressure in mb>,<sea level pressure in mb>,<3 hour pressure tendency>,
            // <WMO tendency characteristic code>,<lat>,<long>,<course>,<speed>,<elevation>*<checksum>
            // where <lat> = ddd mm.mmmm[N|S], <long> = ddd mm.mmmm[E|W], 
            // <course> is True, <speed> in knots, <elevation> in meters
            //
            // 1018.61,1018.61,1.90,2, 15 39.0161N, 89 00.1226W,0,0,0*03
            // 1011.20,1011.20,,, 47 37.5965N,122 31.1453W,0,0,0*31
            //
            // STARX (NB first part of STARX is the same as the STAR)
            // <station pressure in mb>,<sea level pressure in mb>,<3 hour pressure tendency>,
            // <WMO tendency characteristic code>,<lat>,<long>,<course>,<speed>,<elevation>,
            // <temperature>,<relativeHumidity>,<wetBulbTemperature>,<dewPoint>,
            // <humiditySensor pressure in mb>*<checksum>
            //
            // 1018.12,1018.13,0.00,0, 16 30.4429N, 88 21.9040W,0,2,7,29.4,85,27.4,26.7,1017.89*18

            
            
            double double_lat_start;
            double double_lon_start;
            double double_lat_end;
            double double_lon_end;
            
            // latitude START position (latitude 10 min or 3 hrs earlier)
            //
            String fix_latitude_start = GPS_latitude_earlier.trim();                                   // to remove leading and trailing whitespace in the string eg " 52 04.7041N" -> "52 04.7041N"
            int pos_space = fix_latitude_start.indexOf(" ", 0);                                // find first space (" ") in the string
            String fix_latitude_degrees_start = fix_latitude_start.substring(0, pos_space);    // eg "52 04.7041N" -> "52" and "2 04.7041N" -> "2"
            String fix_latitude_minutes_start = fix_latitude_start.substring(pos_space + 1, fix_latitude_start.length() -1);  // only the minutes of the latitude eg "52 04.7041N" -> "04.7041"
            
            double double_latitude_degrees_start = Double.parseDouble(fix_latitude_degrees_start);
            double double_latitude_minutes_start = Double.parseDouble(fix_latitude_minutes_start);
            
            double_lat_start = double_latitude_degrees_start + double_latitude_minutes_start / 60.0;
            
            String fix_latitude_hemisphere_start = fix_latitude_start.substring(fix_latitude_start.length() -1); // eg "52 04.7041N" -> "N" 
            if (fix_latitude_hemisphere_start.toUpperCase().equals("N"))
            {
               // North is positive value
               double_lat_start *= 1;
            }
            else if (fix_latitude_hemisphere_start.toUpperCase().equals("S"))
            {
               // south = negative value
               double_lat_start *= -1;
            }
            else
            {
               double_lat_start = Double.MAX_VALUE;
            }
            
            
            // longitude START position (latitude 10 min or 3 hrs earlier)
            //
            String fix_longitude_start = GPS_longitude_earlier.trim();                                      // to remove leading and trailing whitespace in the string eg "152 04.7041W" -> "152 04.7041W"
            pos_space = fix_longitude_start.indexOf(" ", 0);                                        // find first space (" ") in the string
            String fix_longitude_degrees_start = fix_longitude_start.substring(0, pos_space);       // eg "152 04.7041W" -> "152" and "2 04.7041W" -> "2"
            String fix_longitude_minutes_start = fix_longitude_start.substring(pos_space + 1, fix_longitude_start.length() -1);  // only the minutes of the latitude eg "152 04.7041W" -> "04.7041"
            
            double double_longitude_degrees_start = Double.parseDouble(fix_longitude_degrees_start);
            double double_longitude_minutes_start = Double.parseDouble(fix_longitude_minutes_start);
            
            double_lon_start = double_longitude_degrees_start + double_longitude_minutes_start / 60.0;
            
            String fix_longitude_hemisphere_start = fix_longitude_start.substring(fix_longitude_start.length() -1); // eg "152 04.7041W" -> "W" 
            if (fix_longitude_hemisphere_start.toUpperCase().equals("E"))
            {
               // East is positive value
               double_lon_start *= 1;
            }
            else if (fix_longitude_hemisphere_start.toUpperCase().equals("W"))
            {
               // West = negative value
               double_lon_start *= -1;
            }
            else
            {
               double_lon_start = Double.MAX_VALUE;
            }
            
            // latitude END position (present pos)
            //
            String fix_latitude_end = GPS_latitude.trim();                             // to remove leading and trailing whitespace in the string eg " 52 04.7041N" -> "52 04.7041N"
            pos_space = fix_latitude_end.indexOf(" ", 0);                                      // find first space (" ") in the string
            String fix_latitude_degrees_end = fix_latitude_end.substring(0, pos_space);        // eg "52 04.7041N" -> "52" and "2 04.7041N" -> "2"
            String fix_latitude_minutes_end = fix_latitude_end.substring(pos_space + 1, fix_latitude_end.length() -1);  // only the minutes of the latitude eg "52 04.7041N" -> "04.7041"
            
            double double_latitude_degrees_end = Double.parseDouble(fix_latitude_degrees_end);
            double double_latitude_minutes_end = Double.parseDouble(fix_latitude_minutes_end);
            
            double_lat_end = double_latitude_degrees_end + double_latitude_minutes_end / 60.0;
            
            String fix_latitude_hemisphere_end = fix_latitude_end.substring(fix_latitude_end.length() -1); // eg "52 04.7041N" -> "N" 
            if (fix_latitude_hemisphere_end.toUpperCase().equals("N"))
            {
               // North is positive value
               double_lat_end *= 1;
            }
            else if (fix_latitude_hemisphere_end.toUpperCase().equals("S"))
            {
               // south = negative value
               double_lat_end *= -1;
            }
            else
            {
               //System.out.println("+++ double_lat_end = " + double_lat_end); 
               double_lat_end = Double.MAX_VALUE;
            }
            
            
            // longitude END position (present pos)
            //
            String fix_longitude_end = GPS_longitude.trim();                              // to remove leading and trailing whitespace in the string eg "152 04.7041W" -> "152 04.7041W"
            pos_space = fix_longitude_end.indexOf(" ", 0);                                        // find first space (" ") in the string
            String fix_longitude_degrees_end = fix_longitude_end.substring(0, pos_space);         // eg "152 04.7041W" -> "152" and "2 04.7041W" -> "2"
            String fix_longitude_minutes_end = fix_longitude_end.substring(pos_space + 1, fix_longitude_end.length() -1);  // only the minutes of the latitude eg "152 04.7041W" -> "04.7041"
            
            double double_longitude_degrees_end = Double.parseDouble(fix_longitude_degrees_end);
            double double_longitude_minutes_end = Double.parseDouble(fix_longitude_minutes_end);
            
            double_lon_end = double_longitude_degrees_end + double_longitude_minutes_end / 60.0;
            
            String fix_longitude_hemisphere_end = fix_longitude_end.substring(fix_longitude_end.length() -1); // eg "152 04.7041W" -> "W" 
            if (fix_longitude_hemisphere_end.toUpperCase().equals("E"))
            {
               // East is positive value
               double_lon_end *= 1;
            }
            else if (fix_longitude_hemisphere_end.toUpperCase().equals("W"))
            {
               // West = negative value
               double_lon_end *= -1;
            }
            else
            {
               //System.out.println("+++ double_lon_end = " + double_lon_end); 
               double_lon_end = Double.MAX_VALUE;
            }
            
            
            //
            // Compute distance and bearing between the two positions (present and 10 min/ 3 hours earlier)
            //
            if (double_lat_start != Double.MAX_VALUE && double_lon_start != Double.MAX_VALUE && double_lat_end != Double.MAX_VALUE && double_lon_end != Double.MAX_VALUE)
            {
               // NB see: http://www.movable-type.co.uk/scripts/latlong.html
               //
               //var R = 6371e3; // metres
               //var ?1 = lat1.toRadians();
               //var ?2 = lat2.toRadians();
               //var ?? = (lat2-lat1).toRadians();
               //var ?? = (lon2-lon1).toRadians();
               
               //var ?? = Math.log(Math.tan(Math.PI/4+?2/2)/Math.tan(Math.PI/4+?1/2));    // the ‘projected’ latitude difference
               //var q = Math.abs(??) > 10e-12 ? ??/?? : Math.cos(?1); // E-W course becomes ill-conditioned with 0/0
               //
               //// if dLon over 180° take shorter rhumb line across the anti-meridian:
               //if (Math.abs(??) > Math.PI) ?? = ??>0 ? -(2*Math.PI-??) : (2*Math.PI+??);
               //
               //var dist = Math.sqrt(??*?? + q*q*??*??) * R;              
               //var brng = Math.atan2(??, ??).toDegrees(); 
               
               double R             = 6371e3;                                     // metres
               double lat1_rad      = Math.toRadians(double_lat_start);           // start latitude (latitude 10 min or 3 hours ago)     
               double lat2_rad      = Math.toRadians(double_lat_end);             // end latitude (most recent stored latitude)
               double delta_lat_rad = Math.toRadians(double_lat_end - double_lat_start);
               double delta_lon_rad = Math.toRadians(double_lon_end - double_lon_start);
               double SOG           = Double.MAX_VALUE;
               double COG           = Double.MAX_VALUE;
               
               double delta_projected_lat = Math.log(Math.tan(Math.PI / 4 + lat2_rad / 2) / Math.tan(Math.PI / 4 + lat1_rad / 2));
               
               double q = Math.abs(delta_projected_lat) > 10e-12 ? delta_lat_rad / delta_projected_lat : Math.cos(lat1_rad);
              
               //if (Math.abs(delta_lon_rad) > Math.PI) delta_lon_rad = delta_lon_rad > 0 ? -(2*Math.PI - delta_lon_rad) : (2*Math.PI + delta_lon_rad);
               // if dLon over 180° take shorter rhumb line across the anti-meridian:
               if (delta_lon_rad >  Math.PI) delta_lon_rad -= 2*Math.PI;
               if (delta_lon_rad < -Math.PI) delta_lon_rad += 2*Math.PI;
    
               double dist = Math.sqrt(delta_lat_rad * delta_lat_rad + q * q * delta_lon_rad * delta_lon_rad) * R; 
               
               if (main.obs_format.equals(main.FORMAT_101)) 
               {
                  // 10 minutes based
                  SOG = (dist / 1852) * 6;                   // now SOG in knots          
               }
               else if (main.obs_format.equals(main.FORMAT_FM13))
               {
                  // 180 minutes (3 hours, between positions to compute SOG and COG) based   
                  SOG = (dist / 1852) / 3;                   // now SOG in knots 
               }

               COG = Math.toDegrees(Math.atan2(delta_lon_rad, delta_projected_lat));
               if (COG < 0) COG += 360.0;
               
               
               // SOG
               //
               
               // NB WMO_NO_306.pdf
               // 
               // WMO table  4451
               //vs Ship’s average speed made good during the three hours preceding the time of observation
               //Code
               //figure
               //0 0 knot 0 km h–1
               //1 1– 5 knots 1–10 km h–1
               //2 6–10 knots 11–19 km h–1
               //3 11–15 knots 20–28 km h–1
               //4 16–20 knots 29–37 km h–1
               //5 21–25 knots 38–47 km h–1
               //6 26–30 knots 48–56 km h–1
               //7 31–35 knots 57–65 km h–1
               //8 36–40 knots 66–75 km h–1
               //9 Over 40 knots Over 75 km h–1
               /// Not applicable (report from a coastal land station) or not reported (see Regulation 12.3.1.2 (b))
               if (SOG < 1.0)
               {
                  myposition.jRadioButton14.setSelected(true);
               }
               else if (SOG >= 1.0 && SOG <= 5.0)
               {
                  myposition.jRadioButton15.setSelected(true);
               }           
               else if (SOG > 5.0 && SOG <= 10.0)
               {
                  myposition.jRadioButton16.setSelected(true);
               } 
               else if (SOG > 10.0 && SOG <= 15.0)
               {
                  myposition.jRadioButton17.setSelected(true);
               } 
               else if (SOG > 15.0 && SOG <= 20.0)
               {
                  myposition.jRadioButton18.setSelected(true);
               } 
               else if (SOG > 20.0 && SOG <= 25.0)
               {
                  myposition.jRadioButton19.setSelected(true);
               } 
               else if (SOG > 25.0 && SOG <= 30.0)
               {
                  myposition.jRadioButton20.setSelected(true);
               } 
               else if (SOG > 30.0 && SOG <= 35.0)
               {
                  myposition.jRadioButton21.setSelected(true);
               } 
               else if (SOG > 35.0 && SOG <= 40.0)
               {
                  myposition.jRadioButton22.setSelected(true);
               } 
               else if (SOG > 40)
               {
                  myposition.jRadioButton23.setSelected(true);
               } 
               
               
               // COG
               //
       
               // NB WMO_NO_306.pdf
               //
               //Ds True direction of resultant displacement of the ship during the three hours preceding
               //the time of observation
               //D1 True direction of the point position from the station
               //Code
               //figure
               //0 Calm (in D, DK), or stationary (in Ds), or at the station (in Da, D1), or stationary or no clouds (in DH,
               //DL, DM)
               //1 NE
               //2 E
               //3 SE
               //4 S
               //5 SW
               //6 W
               //7 NW
               //8 N
               //9 All directions (in Da, D1), or confused (in DK), or variable (in D(wind)), or unknown (in Ds), or unknown
               //or clouds invisible (in DH, DL, DM)
               // / Report from a coastal land station or displacement of ship not reported (in Ds only — see
               // Regulation 12.3.1.2 (b))               
               
               // NB see myposition.java  
               //
               //public static final String COURSE_STATIONARY = "stationary";
               //public static final String COURSE_023_067    = "023 - 067";
               //public static final String COURSE_068_112    = "068 - 112";
               //public static final String COURSE_113_157    = "113 - 157";
               //public static final String COURSE_158_202    = "158 - 202";
               //public static final String COURSE_203_247    = "203 - 247";
               //public static final String COURSE_248_292    = "248 - 292";
               //public static final String COURSE_293_337    = "293 - 337";
               //public static final String COURSE_338_022    = "338 - 022";
               
               if (SOG < 1.0) // stopped
               {
                  // by default: in case SOG is indicating stopped then SOG always stopped also!
                  myposition.jRadioButton5.setSelected(true);           // COG = stopped
               }
               //else if (SOG >= 1.0 && SOG <= 999.9) // not stopped (halverwege de test maar veranderd om toch een richting tijdens laatste testjes te krijgen)
               else if (SOG >= 1.0 && SOG <= 999999.9) // not stopped   
               {
                  if (COG > 23.0 && COG <= 67.0)
                  {
                     myposition.jRadioButton6.setSelected(true);
                  }
                  else if (COG > 67.0 && COG <= 112.0)
                  {
                     myposition.jRadioButton7.setSelected(true);
                  }
                  else if (COG > 112.0 && COG <= 157.0)
                  {
                     myposition.jRadioButton8.setSelected(true);
                  }
                  else if (COG > 157.0 && COG <= 202.0)
                  {
                     myposition.jRadioButton9.setSelected(true);
                  }
                  else if (COG > 202.0 && COG <= 247.0)
                  {
                     myposition.jRadioButton10.setSelected(true);
                  }
                  else if (COG > 247.0 && COG <= 292.0)
                  {
                     myposition.jRadioButton11.setSelected(true);
                  }
                  else if (COG > 292.0 && COG <= 337.0)
                  {
                     myposition.jRadioButton12.setSelected(true);
                  }
                  else if (COG > 337.0 && COG <= 360.0 || COG > 0.0 && COG <= 23.0)
                  {
                     myposition.jRadioButton13.setSelected(true);
                  }
               } // else if (SOG >= 1.0 && SOG <= 99.9)
               
               // below for testing
               //System.out.println("+++ double_lat_start = " + double_lat_start);
               //System.out.println("+++ double_lon_start = " + double_lon_start);
               //System.out.println("+++ double_lat_end = " + double_lat_end);
               //System.out.println("+++ double_lon_end = " + double_lon_end);
               //System.out.println("+++ dist [metres] = " + dist);
               System.out.println("--- SOG [knots] = " + SOG);
               System.out.println("--- COG [deg] = " + COG);
               
            } // if (double_lat_start != Double.MAX_VALUE etc.
         } // if ((fix_ok) etc.
         
         
         
      
         if (fix_ok == false)   
         {
            message = "[GPS] error; no sensor data file available / formatting error last saved record / last saved record > 5 minutes old / checksum not ok";
            
            // reset all the previous inserted values in the position inut screen
            myposition.jTextField1.setText("");                                     // lat deg
            myposition.jTextField2.setText("");                                     // lat min
            myposition.jTextField3.setText("");                                     // lon deg
            myposition.jTextField4.setText("");                                     // lon min
            myposition.buttonGroup1.clearSelection();                               // N/S
            myposition.buttonGroup2.clearSelection();                               // E/W
            myposition.buttonGroup3.clearSelection();                               // course
            myposition.buttonGroup4.clearSelection();                               // speed
         } // if (fix_ok == false) 
   
   
         if (fix_ok == false)
         {
            if (mode.equals("APR"))                                                 // e.g. every 1, 3 or 6 hours
            {
               // message (was set before)
               main.log_turbowin_system_message(message);
            }
            else if (mode.equals("MANUAL"))
            {
               // message (was set before)
               main.log_turbowin_system_message(message);
         
               // temporary message box (eg mismatch GPS date-time and computer date-time) 
               String info = "no reliable GPS position available";
         
               final JOptionPane pane_end = new JOptionPane(info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
               final JDialog temp_dialog = pane_end.createDialog(main.APPLICATION_NAME);

               Timer timer_end = new Timer(2500, new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent e)
                  {
                     temp_dialog.dispose();
                  }
               });
               timer_end.setRepeats(false);
               timer_end.start();
               temp_dialog.setVisible(true);               
            } // else if (mode.equals("MANUAL"))
         } // if (fix_ok == false)
         else // fix_ok == true
         {
            if (mode.equals("APR") || mode.equals("MANUAL"))    // only in APR mode (1, 3, 6 hours) or manual mode, otherwise every minute message lines in the log
            {
               // logging
               //
               message_fix_position = "GPS position (dd-mm [N/S] ddd-mm [E/W]): " + myposition.latitude_degrees + "-" + myposition.latitude_minutes + " " + myposition.latitude_hemisphere.substring(0, 1) +  " " + myposition.longitude_degrees + "-" + myposition.longitude_minutes + " " + myposition.longitude_hemisphere.substring(0, 1);  

               //main.log_turbowin_system_message("[GPS] date-time parsing ok; " + message_fix_date_time);
               main.log_turbowin_system_message("[GPS] last position parsing ok; " + message_fix_position);
            }
         } // else (fix_ok == true)
         
      } // protected void done()
      
   }.execute(); // new SwingWorker <Void, Void>()
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_a_ppp_Data_Files_For_Obs(final boolean StarX)
{
   // called from: - initSynopparameters() [mybarograph.java]
   
   // MINTAKA STAR has an integrated GPS
   // GPS data is part of th saved pressure string eg:
   // <station pressure in mb>,<sea level pressure in mb>,<3 hour pressure tendency>,
   // <WMO tendency characteristic code>,<lat>,<long>,<course>,<speed>,<elevation>*<checksum>
   // where <lat> = ddd mm.mmmm[N|S], <long> = ddd mm.mmmm[E|W], 
   // <course> is True, <speed> in knots, <elevation> in meters
   //
   // 1018.61,1018.61,1.90,2, 15 39.0161N, 89 00.1226W,0,0,0*03
   // 1011.20,1011.20,,, 47 37.5965N,122 31.1453W,0,0,0*31
   //
   //
   // STARX (NB first part of STARX is the same as the STAR)
   //       TurboWinH 
   //             <station pressure in mb>,
   //             <sea level pressure in mb>,
   //             <3 hour pressure tendency>,
   //             <WMO tendency characteristic code>,
   //             <lat>,
   //             <long>,
   //             <course>,
   //             <speed>,
   //             <elevation>,
   //             <temperature>,
   //             <relativeHumidity>,
   //             <wetBulbTemperature>,
   //             <dewPoint>,
   //             <observationAge>
   //             *<checksum>
   //
   //             <lat> = ddd mm.mmmm[N|S], <long> = ddd mm.mmmm[E|W], <course> is True,
   //             <speed> in knots, <elevation> in meters, <relativeHumidity> is 0-100,
   //             temperatures are in degrees celsius, <observatoinAge> is in seconds.
   //
   // 1009.73,1007.73,0.00,0, 52 41.9491N,  6 14.1802E,0,1,7,19.5,65,15.4,12.8,58*16

   
   
   // initialisation
   mybarograph.pressure_amount_tendency = "";
   mybarograph.a_code                   = "";
   

   new SwingWorker<String, Void>()
   {
      @Override
      protected String doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record                       = null;
         String laatste_record               = null;
         String local_obs_age                = "";
         int pos1 = 0;           // position of the first "," in the last record
         int pos2 = 0;           // position of the second "," in the last record
         int pos3 = 0;           // position of the third "," in the last record
         int pos4 = 0;                                        
         int pos5 = 0;                                        
         int pos6 = 0;                                        
         int pos7 = 0;                                        
         int pos8 = 0;                                        
         int pos9 = 0;
         int pos10 = 0;                                        
         int pos11 = 0;                                        
         int pos12 = 0;
         int pos13 = 0;                                        
         int pos14 = 0;         // pos of the "*" 


         // initialisation
         //main.sensor_data_record_obs_pressure = "";

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // e.g. 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;

                  // retrieve the last record in the sensor data file
                  //
                  while ((record = in.readLine()) != null)
                  {
                     laatste_record = record;
                  } // while ((record = in.readLine()) != null)
                  
                  // check on minimum record length
                  //
                  if (laatste_record != null)
                  {
                     if (!(laatste_record.length() > 15))         // NB > 15 is a little bit arbitrary number (YYYYMMDDHHmm + 3 commas + at leat 2 char pressure value= 15 chars)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Star or StarX format (min. record length) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                     }
                  }
                  
                  // check on correct number of commas in the laatste_record
                  //
                  if (laatste_record != null)
                  {
                     int number_read_commas = 0;
                     int pos = -1;
                     
                     do
                     {
                        pos = laatste_record.indexOf(",", pos + 1);
                        if (pos != -1)     // "," found
                        {
                           number_read_commas++;
                           //System.out.println("+++ number_read_commas = " + number_read_commas);
                        }
                     } while (pos != -1); 
                        
                     if (StarX == false)
                     {
                        if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STAR)
                        {
                           laatste_record = null;
                           System.out.println("--- Mintaka Star format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                        }      
                     } // if (StarX == false)
                     else if (StarX == true)
                     {
                        if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STARX)
                        {
                           laatste_record = null;
                           System.out.println("--- Mintaka StarX format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                        }      
                     } // else if (StarX == true)
                  } // if (laatste_record != null)
                  
                  
                  // last retrieved record ok
                  if (laatste_record != null)
                  {
                     int pos = laatste_record.length() -12;                                               // pos is now start position of YYYYMMDDHHmm
                     String record_datum_tijd_minuten = laatste_record.substring(pos, pos + 12);          // YYYYMMDDHHmm has length 12
                     
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     ///System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min

                     if (timeDiff <= TIMEDIFF_SENSOR_DATA)      // max ? minutes old
                     {
                        // cheksum check
                        //
                        // example Mintaka Star last_record:  1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211
                        // example Mintaka StarX last record: 1009.73,1007.73,0.00,0, 52 41.9491N,  6 14.1802E,0,1,7,19.5,65,15.4,12.8,58*16
                        //
                        String record_checksum = laatste_record.substring(laatste_record.length() -14, laatste_record.length() -12);  // eg "24" from record "1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211"
                        String computed_checksum = Mintaka_Star_Checksum(laatste_record);
   
                        /**** TEST BEGIN *****/
                        //computed_checksum = record_checksum;
                        /****** TEST END *********/
                        
                        if (computed_checksum.equals(record_checksum))
                        {
                           if (StarX == false)
                           {
                              pos1 = laatste_record.indexOf(",", 0);                 // position of the first "," in the last record
                              pos2 = laatste_record.indexOf(",", pos1 +1);           // position of the second "," in the last record
                              pos3 = laatste_record.indexOf(",", pos2 +1);           // position of the third "," in the last record
                              pos4 = laatste_record.indexOf(",", pos3 +1);  
                              
                              main.sensor_data_record_obs_ppp = laatste_record.substring(pos2 +1, pos3);
                              main.sensor_data_record_obs_a = laatste_record.substring(pos3 +1, pos4);  
                           
                              // if not StarX then there is no obs age to be checked
                              local_obs_age = "0";
                              
                              System.out.println("--- sensor data record, tendency for obs: " + main.sensor_data_record_obs_ppp);
                              System.out.println("--- sensor data record, characteristic for obs: " + main.sensor_data_record_obs_a);

                           } // if (StarX == false)
                           else if (StarX == true)
                           {
                              // StarX example: // 1009.73,1007.73,0.00,0, 52 41.9491N,  6 14.1802E,0,1,7,19.5,65,15.4,12.8,58*16
                                                                                     // sensor height pressure 
                              pos1 = laatste_record.indexOf(",", 0);                 // MSLP hereafter; position of the first "," in the last record
                              pos2 = laatste_record.indexOf(",", pos1 +1);           // ppp hereafter; position of the second "," in the last record
                              pos3 = laatste_record.indexOf(",", pos2 +1);           // a hereafter; position of the third "," in the last record
                              pos4 = laatste_record.indexOf(",", pos3 +1);           // lat hereafter; position of the 4th "," in the last record
                              pos5 = laatste_record.indexOf(",", pos4 +1);           // lon hereafter; position n of the 5th "," in the last record
                              pos6 = laatste_record.indexOf(",", pos5 +1);           // course hereafter; position of the 6th "," in the last record
                              pos7 = laatste_record.indexOf(",", pos6 +1);           // speed hereafter                                
                              pos8 = laatste_record.indexOf(",", pos7 +1);           // elevation hereafter
                              pos9 = laatste_record.indexOf(",", pos8 +1);           // air temp
                              pos10 = laatste_record.indexOf(",", pos9 +1);          // RH
                              pos11 = laatste_record.indexOf(",", pos10 +1);         // wet bulb                                      
                              pos12 = laatste_record.indexOf(",", pos11 +1);         // dew point
                              pos13 = laatste_record.indexOf(",", pos12 +1);         // observation age  
                              pos14 = laatste_record.indexOf("*", pos13 +1);         // pos of the "*" 
                              
                              main.sensor_data_record_obs_ppp = laatste_record.substring(pos2 +1, pos3);
                              main.sensor_data_record_obs_a = laatste_record.substring(pos3 +1, pos4);  
                              
                              local_obs_age = laatste_record.substring(pos13 +1, pos14);  
                              
                              System.out.println("--- sensor data record, tendency for obs: " + main.sensor_data_record_obs_ppp);
                              System.out.println("--- sensor data record, characteristic for obs: " + main.sensor_data_record_obs_a);
                              System.out.println("--- sensor data record, obs age (StarX): " + local_obs_age);
                           } // else if (StarX == true)
                           
                        } // if (computed_checksum.equals(record_checksum))
                        else // checksum not ok
                        {
                           main.sensor_data_record_obs_ppp = "";
                           main.sensor_data_record_obs_a = "";
                           local_obs_age = ""; 
                        } // else (checksum not ok)
                     } // if (timeDiff <= 5L)
                     else
                     {
                        main.sensor_data_record_obs_ppp = "";
                        main.sensor_data_record_obs_a = "";
                        local_obs_age = ""; 
                     }

                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) {  }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
 
         // clear memory
         main.obs_file_datum_tijd      = null;

         
         return local_obs_age;
         
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         int hulp_pressure_change       = 3;                // -1 negative; 1 positive; 0 same pressure; 3 is only start value
         final int AGE_NOT_OK           = 999999;
         String error_info              = "";
         double hulp_double_ppp_reading = Double.MAX_VALUE;
         int hulp_int_a_reading         = Integer.MAX_VALUE;
         
         try 
         {
            // obs age (in sec) of the last record
            //
            String return_obs_age = get();
            
            int int_return_obs_age = AGE_NOT_OK;                 // 999999 = random number but > 99.9
            if ( (return_obs_age.compareTo("") != 0) && (return_obs_age != null) && (return_obs_age.indexOf("*") == -1) )
            {
               try
               {
                  int_return_obs_age = Integer.parseInt(return_obs_age.trim());
               }
               catch (NumberFormatException e)
               {
                  int_return_obs_age = AGE_NOT_OK; 
                  System.out.println("--- " + "RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_a_ppp_Data_Files_For_Obs() " + e);
                  //return_obs_age = "";
               }
            } 
            
            if ((int_return_obs_age >= 0) && (int_return_obs_age <= MAX_AGE_STARX_OBS_DATA))
            {
               // ppp (tendency)
               //
               if ( (main.sensor_data_record_obs_ppp.compareTo("") != 0) && (main.sensor_data_record_obs_ppp != null) && (main.sensor_data_record_obs_ppp.indexOf("*") == -1) )
               {
                  //double hulp_double_ppp_reading;
                  try
                  {
                     hulp_double_ppp_reading = Double.parseDouble(main.sensor_data_record_obs_ppp.trim());
                     hulp_double_ppp_reading = Math.round(hulp_double_ppp_reading * 10) / 10.0d;  // e.g. 13.19 -> 13.2
               
                     //System.out.println("+++ hulp_double_ppp_reading = " + hulp_double_ppp_reading);
                  }
                  catch (NumberFormatException e)
                  {
                     System.out.println("--- " + "Function RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_a_ppp_For_Obs() " + e);
                     hulp_double_ppp_reading = Double.MAX_VALUE;
                  }   
             
                  if ((hulp_double_ppp_reading > -99.9) && (hulp_double_ppp_reading < 99.9))
                  {
                     // NB hulp_pressure_change: a help to determine a (pressure characteristic)
                     if (hulp_double_ppp_reading > 0.0)
                     {
                        hulp_pressure_change = 1; 
                     }
                     else if (hulp_double_ppp_reading < 0.0)
                     {
                        hulp_pressure_change = -1;
                     }
                     else if (hulp_double_ppp_reading == 0.0)
                     {
                        hulp_pressure_change = 0;
                     }
               
                     //System.out.println("+++ hulp_pressure_change = " + hulp_pressure_change);
        
                     // tendency
                     //mybarograph.pressure_amount_tendency = Double.toString(Math.abs(hulp_double_ppp_reading));  // only positive value in this field
                     //mybarograph.jTextField1.setText(mybarograph.pressure_amount_tendency); 
                  }
               } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
               else
               {
                  error_info = "pressure tendency and/or characteristic not available";
                  hulp_double_ppp_reading = Double.MAX_VALUE;
               }
               
               // a (characteristic)
               //
               if ((main.sensor_data_record_obs_a.compareTo("") != 0) && (main.sensor_data_record_obs_a != null) && (main.sensor_data_record_obs_a.indexOf("*") == -1) )
               {
                  //int hulp_int_a_reading;
                  try
                  {
                     hulp_int_a_reading = Integer.parseInt(main.sensor_data_record_obs_a.trim());
                  }
                  catch (NumberFormatException e)
                  {
                     System.out.println("--- " + "Function RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_a_ppp_For_Obs() " + e);
                     hulp_int_a_reading = Integer.MAX_VALUE;
                  }   
             
               } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
               else
               {
                  error_info = "pressure tendency and/or characteristic not available";
                  hulp_int_a_reading = Integer.MAX_VALUE; 
               }
               
            } // if ((hulp_return_obs_age >= 0) && (hulp_return_obs_age <= MAX_AGE_STARX_OBS_DATA))
            else
            {
               error_info = "pressure tendency and characteristic data obsolete or checksum not ok";
               hulp_double_ppp_reading = Double.MAX_VALUE;
               hulp_int_a_reading = Integer.MAX_VALUE;   
            }
            
         }  // try 
         catch (InterruptedException | ExecutionException ex) 
         {
            error_info = "error retrieving pressure tendency and characteristic data (" + ex + ")";
            hulp_double_ppp_reading = Double.MAX_VALUE;
            hulp_int_a_reading = Integer.MAX_VALUE;   
         } // catch
         
         
         // general error eg NumberFormatException when parsing hulp_int_a_reading = Integer.parseInt(main.sensor_data_record_obs_a.trim());
         if (error_info.equals("") && (hulp_double_ppp_reading > 999999.9 || hulp_int_a_reading > 999999))   
         {
            error_info = "automatically retrieved pressure tendency and characteristic general error or checksum not ok";
         }
         
         if (!error_info.equals(""))
         {
            System.out.println("--- " + error_info);
            
            final JOptionPane pane_end = new JOptionPane(error_info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            final JDialog dialog = pane_end.createDialog(main.APPLICATION_NAME);

            Timer timer_end = new Timer(2500, new ActionListener()
            {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  dialog.dispose();
               }
            });
            timer_end.setRepeats(false);
            timer_end.start();
            dialog.setVisible(true);   
         } // if (!error_info.equals(""))
         else // no error encountered
         {
            // set tendency textfield on a-ppp input page
            //
            mybarograph.pressure_amount_tendency = Double.toString(Math.abs(hulp_double_ppp_reading));  // only positive value in this field
            mybarograph.jTextField1.setText(mybarograph.pressure_amount_tendency); 
            
            // set characteristic on a-ppp input page
            //
            if ((hulp_int_a_reading >= 0) && (hulp_int_a_reading <= 8))
            {
               // initialisation
               mybarograph.jRadioButton1.setSelected(false);
               mybarograph.jRadioButton2.setSelected(false);
               mybarograph.jRadioButton3.setSelected(false);
               mybarograph.jRadioButton4.setSelected(false);
               mybarograph.jRadioButton5.setSelected(false);
               mybarograph.jRadioButton6.setSelected(false);
               mybarograph.jRadioButton7.setSelected(false);
               mybarograph.jRadioButton8.setSelected(false);
               mybarograph.jRadioButton9.setSelected(false);
               mybarograph.jRadioButton10.setSelected(false);
               mybarograph.jRadioButton11.setSelected(false);
               mybarograph.jRadioButton12.setSelected(false);
               
               // pressure higher than 3hrs ago
               if (hulp_pressure_change == 1)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_0))
                     mybarograph.jRadioButton1.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_1))
                     mybarograph.jRadioButton2.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_2))
                     mybarograph.jRadioButton3.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_3))
                     mybarograph.jRadioButton4.setSelected(true);
               }
               
               // pressure lower than 3hrs ago
               else if (hulp_pressure_change == -1)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_5))
                     mybarograph.jRadioButton5.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_6))
                     mybarograph.jRadioButton6.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_7))
                     mybarograph.jRadioButton7.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_8))
                     mybarograph.jRadioButton8.setSelected(true);
               }
            
               // pressure the same as 3hrs ago
               else if (hulp_pressure_change == 0)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_0_SAME))
                     mybarograph.jRadioButton9.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_4))                                
                     mybarograph.jRadioButton10.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_5_SAME))                                
                     mybarograph.jRadioButton11.setSelected(true);              
               }    
               else
               {
                  mybarograph.jRadioButton12.setSelected(true);   // not determined
               }
               
            } // if ((hulp_int_a_reading >= 0) && (hulp_int_a_reading <= 8))
            
         } // else ( no error encountered)
         
         
          
         
         
/*         
         // ppp
         //
         if ( (main.sensor_data_record_obs_ppp.compareTo("") != 0) && (main.sensor_data_record_obs_ppp != null) && (main.sensor_data_record_obs_ppp.indexOf("*") == -1) )
         {
            double hulp_double_ppp_reading;
            try
            {
               hulp_double_ppp_reading = Double.parseDouble(main.sensor_data_record_obs_ppp.trim());
               hulp_double_ppp_reading = Math.round(hulp_double_ppp_reading * 10) / 10.0d;  // e.g. 13.19 -> 13.2
               
               //System.out.println("+++ hulp_double_ppp_reading = " + hulp_double_ppp_reading);
               
            }
            catch (NumberFormatException e)
            {
               System.out.println("--- " + "Function RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_a_ppp_For_Obs() " + e);
               hulp_double_ppp_reading = Double.MAX_VALUE;
            }   
             
            if ((hulp_double_ppp_reading > -99.9) && (hulp_double_ppp_reading < 99.9))
            {
               // NB hulp_pressure_change: a help to determine a (pressure characteristic)
               if (hulp_double_ppp_reading > 0.0)
               {
                  hulp_pressure_change = 1; 
               }
               else if (hulp_double_ppp_reading < 0.0)
               {
                  hulp_pressure_change = -1;
               }
               else if (hulp_double_ppp_reading == 0.0)
               {
                  hulp_pressure_change = 0;
               }
               
               //System.out.println("+++ hulp_pressure_change = " + hulp_pressure_change);
        
               // tendency
               mybarograph.pressure_amount_tendency = Double.toString(Math.abs(hulp_double_ppp_reading));  // only positive value in this field
               mybarograph.jTextField1.setText(mybarograph.pressure_amount_tendency); 
            }
         } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
         
         
         // a
         //
         if ((main.sensor_data_record_obs_a.compareTo("") != 0) && (main.sensor_data_record_obs_a != null) && (main.sensor_data_record_obs_a.indexOf("*") == -1) )
         {
            int hulp_int_a_reading;
            
            try
            {
               hulp_int_a_reading = Integer.parseInt(main.sensor_data_record_obs_a.trim());
            }
            catch (NumberFormatException e)
            {
               System.out.println("--- " + "Function RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_a_ppp_For_Obs() " + e);
               hulp_int_a_reading = Integer.MAX_VALUE;
            }   
             
            if ((hulp_int_a_reading >= 0) && (hulp_int_a_reading <= 8))
            {
               // initialisation
               mybarograph.jRadioButton1.setSelected(false);
               mybarograph.jRadioButton2.setSelected(false);
               mybarograph.jRadioButton3.setSelected(false);
               mybarograph.jRadioButton4.setSelected(false);
               mybarograph.jRadioButton5.setSelected(false);
               mybarograph.jRadioButton6.setSelected(false);
               mybarograph.jRadioButton7.setSelected(false);
               mybarograph.jRadioButton8.setSelected(false);
               mybarograph.jRadioButton9.setSelected(false);
               mybarograph.jRadioButton10.setSelected(false);
               mybarograph.jRadioButton11.setSelected(false);
               mybarograph.jRadioButton12.setSelected(false);
               
               // pressure higher than 3hrs ago
               if (hulp_pressure_change == 1)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_0))
                     mybarograph.jRadioButton1.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_1))
                     mybarograph.jRadioButton2.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_2))
                     mybarograph.jRadioButton3.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_3))
                     mybarograph.jRadioButton4.setSelected(true);
               }
               
               // pressure lower than 3hrs ago
               else if (hulp_pressure_change == -1)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_5))
                     mybarograph.jRadioButton5.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_6))
                     mybarograph.jRadioButton6.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_7))
                     mybarograph.jRadioButton7.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_8))
                     mybarograph.jRadioButton8.setSelected(true);
               }
            
               // pressure the same as 3hrs ago
               else if (hulp_pressure_change == 0)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_0_SAME))
                     mybarograph.jRadioButton9.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_4))                                
                     mybarograph.jRadioButton10.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_5_SAME))                                
                     mybarograph.jRadioButton11.setSelected(true);              
               }    
               else
               {
                  mybarograph.jRadioButton12.setSelected(true);   // not determined
               }
               
            } // if ((hulp_int_a_reading >= 0) && (hulp_int_a_reading <= 8))
         } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
*/        
      } // protected void done()

   }.execute(); // new SwingWorker <Void, Void>()
  
   
}        
        
        

/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void RS232_Mintaka_StarX_Read_Sensor_Data_Air_Temp_et_al_For_Obs()
{
   // called from: - initSynopparameters() [mytemp.java]
   
   // MINTAKA STAR has an integrated GPS
   // GPS data is part of th saved pressure string eg:
   // <station pressure in mb>,<sea level pressure in mb>,<3 hour pressure tendency>,
   // <WMO tendency characteristic code>,<lat>,<long>,<course>,<speed>,<elevation>*<checksum>
   // where <lat> = ddd mm.mmmm[N|S], <long> = ddd mm.mmmm[E|W], 
   // <course> is True, <speed> in knots, <elevation> in meters
   //
   // 1018.61,1018.61,1.90,2, 15 39.0161N, 89 00.1226W,0,0,0*03
   // 1011.20,1011.20,,, 47 37.5965N,122 31.1453W,0,0,0*31
   //
   
   
   // STARX (NB first part of STARX is the same as the STAR)
   //       TurboWinH 
   //             <station pressure in mb>,
   //             <sea level pressure in mb>,
   //             <3 hour pressure tendency>,
   //             <WMO tendency characteristic code>,
   //             <lat>,
   //             <long>,
   //             <course>,
   //             <speed>,
   //             <elevation>,
   //             <temperature>,
   //             <relativeHumidity>,
   //             <wetBulbTemperature>,
   //             <dewPoint>,
   //             <observationAge>
   //             *<checksum>
   //
   //             <lat> = ddd mm.mmmm[N|S], <long> = ddd mm.mmmm[E|W], <course> is True,
   //             <speed> in knots, <elevation> in meters, <relativeHumidity> is 0-100,
   //             temperatures are in degrees celsius, <observatoinAge> is in seconds.
   //
   // 1009.73,1007.73,0.00,0, 52 41.9491N,  6 14.1802E,0,1,7,19.5,65,15.4,12.8,58*16
   //        ,       ,    , , 52 41.9490N,  6 14.1741E,0,0,2,    ,  ,    ,    ,*03201805230929

   
   // initialisation
   mytemp.air_temp                     = "";           // if not ="" -> nullpointerexception if update button clicked on main page
   mytemp.wet_bulb_temp                = "";
   mytemp.RH                           = "";           // range 0.0 - 100.0 %
   

   new SwingWorker<String, Void>()
   {
      @Override
      protected String doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record         = null;
         String laatste_record = null;
         
         String local_dew_point              = "";
         String local_obs_age                = "";


         // initialisation
         //main.sensor_data_record_obs_pressure = "";

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // e.g. 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;

                  // retrieve the last record in the sensor data file
                  //
                  while ((record = in.readLine()) != null)
                  {
                     laatste_record = record;
                  } // while ((record = in.readLine()) != null)
                  
                  // check on minimum record length
                  //
                  if (laatste_record != null)
                  {
                     if (!(laatste_record.length() > 15))         // NB > 15 is a little bit arbitrary number (YYYYMMDDHHmm + 3 commas + at leat 2 char pressure value= 15 chars)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka StarX format (min. record length) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                     }
                  }
                  
                  // check on correct number of commas in the laatste_record
                  //
                  if (laatste_record != null)
                  {
                     int number_read_commas = 0;
                     int pos = -1;
                     
                     do
                     {
                        pos = laatste_record.indexOf(",", pos + 1);
                        if (pos != -1)     // "," found
                        {
                           number_read_commas++;
                           //System.out.println("+++ number_read_commas = " + number_read_commas);
                        }
                     } while (pos != -1); 
                        
                     
                     if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STARX)
                     {
                        //System.out.println("number_read_commas = " + number_read_commas);
                        //System.out.println("main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STARX = " + main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STARX);
                        laatste_record = null;
                        System.out.println("--- Mintaka StarX format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                     }      
                  } // if (laatste_record != null)
                  
                  
                  // last retrieved record ok
                  if (laatste_record != null)
                  {
                     int pos = laatste_record.length() -12;                                               // pos is now start position of YYYYMMDDHHmm
                     String record_datum_tijd_minuten = laatste_record.substring(pos, pos + 12);          // YYYYMMDDHHmm has length 12
                     
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     ///System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min

                     if (timeDiff <= TIMEDIFF_SENSOR_DATA)      // max ? minutes old
                     {
                        // cheksum check
                        //
                        // example Mintaka StarX last record: 1009.73,1007.73,0.00,0, 52 41.9491N,  6 14.1802E,0,1,7,19.5,65,15.4,12.8,58*16
                        //
                        String record_checksum = laatste_record.substring(laatste_record.length() -14, laatste_record.length() -12);  // eg "24" from record "1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211"
                        String computed_checksum = Mintaka_Star_Checksum(laatste_record);
   
                        if (computed_checksum.equals(record_checksum))
                        {                                                                                          // sensor height pressure                                                                               
                           int pos1 = laatste_record.indexOf(",", 0);                                              // MSL hereafter; position of the first "," in the last record
                           int pos2 = laatste_record.indexOf(",", pos1 +1);                                        // ppp hereafter; position of the second "," in the last record
                           int pos3 = laatste_record.indexOf(",", pos2 +1);                                        // a hereafter; position of the third "," in the last record
                           int pos4 = laatste_record.indexOf(",", pos3 +1);                                        // lat hereafter 
                           int pos5 = laatste_record.indexOf(",", pos4 +1);                                        // lon hereafter
                           int pos6 = laatste_record.indexOf(",", pos5 +1);                                        // course hereafter                                    
                           int pos7 = laatste_record.indexOf(",", pos6 +1);                                        // speed hereafter                                
                           int pos8 = laatste_record.indexOf(",", pos7 +1);                                        // elevation hereafter
                           int pos9 = laatste_record.indexOf(",", pos8 +1);                                        // air temp
                           int pos10 = laatste_record.indexOf(",", pos9 +1);                                       // RH
                           int pos11 = laatste_record.indexOf(",", pos10 +1);                                      // wet bulb                                      
                           int pos12 = laatste_record.indexOf(",", pos11 +1);                                      // dew point
                           int pos13 = laatste_record.indexOf(",", pos12 +1);                                      // observation age  
                           int pos14 = laatste_record.indexOf("*", pos13 +1);                                      // pos of the "*" 
                           
                           // rounding eg: 998.19 -> 998.2
                           //        double digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim()) + HOOGTE_CORRECTIE;
                           //        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;  // bv 998.19 -> 998.2
                        
                           mytemp.air_temp      = laatste_record.substring(pos9 +1, pos10);
                           mytemp.RH            = laatste_record.substring(pos10 +1, pos11);
                           mytemp.wet_bulb_temp = laatste_record.substring(pos11 +1, pos12);
                           local_dew_point      = laatste_record.substring(pos12 +1, pos13);  
                           local_obs_age        = laatste_record.substring(pos13 +1, pos14);   
                           
                           System.out.println("--- sensor data record, air temp for obs: " + mytemp.air_temp);
                           System.out.println("--- sensor data record, RH for obs: " + mytemp.RH);
                           System.out.println("--- sensor data record, wet bulb temp for obs: " + mytemp.wet_bulb_temp);
                           System.out.println("--- sensor data record, dewpoint for obs: " + local_dew_point);
                           System.out.println("--- sensor data record, obs age for obs: " + local_obs_age);
                           
                        } // if (computed_checksum.equals(record_checksum))
                        else // checksum not ok
                        {
                           //System.out.println("cheksum not OK");
                           mytemp.air_temp       = "";
                           mytemp.RH             = "";
                           mytemp.wet_bulb_temp  = "";
                           local_dew_point       = "";  
                           local_obs_age         = "";   
                        } // else (checksum not ok)
                     } // if (timeDiff <= 5L)
                     else
                     {
                        mytemp.air_temp       = "";
                        mytemp.RH             = "";
                        mytemp.wet_bulb_temp  = "";
                        local_dew_point       = "";  
                        local_obs_age         = "";   
                     } // else

                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) {  }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
 
         // clear memory
         main.obs_file_datum_tijd      = null;

         
         // dewpoint
         //
         mytemp.double_dew_point = main.INVALID;                 
         if ( (local_dew_point.compareTo("") != 0) && (local_dew_point != null) && (local_dew_point.indexOf("*") == -1) )
         {
            try
            {
               mytemp.double_dew_point = Double.parseDouble(local_dew_point.trim());
            }
            catch (NumberFormatException e)
            {
               System.out.println("--- " + "RS232_Mintaka_StarX_Read_Sensor_Data_Air_Temp_et_al_For_Obs() " + e);
               //mytemp.air_temp = "";
               mytemp.double_dew_point = main.INVALID;
            }   
         }
         
         
         return local_obs_age;
         
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         String error_info = "";
         final int AGE_NOT_OK = 999999;
         double hulp_air_temp = 999999;                 // 999999 = random number but > 99.9
         double hulp_wet_bulb_temp = 999999;            // 999999 = random number but > 99.9
         
         try 
         {
            // obs age (in sec) of the temp et al part of the last record
            //
            String return_obs_age = get();
            
            int hulp_return_obs_age = AGE_NOT_OK;                 // 999999 = random number but > 99.9
            if ( (return_obs_age.compareTo("") != 0) && (return_obs_age != null) && (return_obs_age.indexOf("*") == -1) )
            {
               try
               {
                  hulp_return_obs_age = Integer.parseInt(return_obs_age.trim());
               }
               catch (NumberFormatException e)
               {
                  hulp_return_obs_age = AGE_NOT_OK;   
                  System.out.println("--- " + "RS232_Mintaka_StarX_Read_Sensor_Data_Air_Temp_et_al_For_Obs() " + e);
                  //return_obs_age = "";
               }
            }

            if ((hulp_return_obs_age >= 0) && (hulp_return_obs_age <= MAX_AGE_STARX_OBS_DATA))   // NB 60 * 10 = 600 sec = 10 minutes
            {
               // air temp
               //
               //double hulp_air_temp = 999999;                 // 999999 = random number but > 99.9
               if ( (mytemp.air_temp.compareTo("") != 0) && (mytemp.air_temp != null) && (mytemp.air_temp.indexOf("*") == -1) )
               {
                  try
                  {
                     hulp_air_temp = Double.parseDouble(mytemp.air_temp.trim());
                  }
                  catch (NumberFormatException e)
                  {
                     System.out.println("--- " + "RS232_Mintaka_StarX_Read_Sensor_Data_Air_Temp_et_al_For_Obs() " + e);
                     //hulp_air_temp = Double.MAX_VALUE;
                     mytemp.air_temp = "";
                  }
               }
               
               if (!(hulp_air_temp > -99.9) && (hulp_air_temp < 99.9))
               {
                  mytemp.air_temp = "";
               }
            
               
               // wet bulb temp
               //
               //double hulp_wet_bulb_temp = 999999;                 // 999999 = random number but > 99.9
               if ( (mytemp.wet_bulb_temp.compareTo("") != 0) && (mytemp.wet_bulb_temp != null) && (mytemp.wet_bulb_temp.indexOf("*") == -1) )
               {
                  try
                  {
                     hulp_wet_bulb_temp = Double.parseDouble(mytemp.wet_bulb_temp.trim());
                  }
                  catch (NumberFormatException e)
                  {
                     System.out.println("--- " + "RS232_Mintaka_StarX_Read_Sensor_Data_Air_Temp_et_al_For_Obs() " + e);
                     mytemp.wet_bulb_temp = "";
                  }
               }
               
               if (!(hulp_wet_bulb_temp > -99.9) && (hulp_wet_bulb_temp < 99.9))
               {
                  mytemp.wet_bulb_temp = "";
               }
               
               
               // RH
               //
               double hulp_RH = 999999;                 // 999999 = random number but > 99.9
               if ( (mytemp.RH.compareTo("") != 0) && (mytemp.RH != null) && (mytemp.RH.indexOf("*") == -1) )
               {
                  try
                  {
                     hulp_RH = Double.parseDouble(mytemp.RH.trim());
                  }
                  catch (NumberFormatException e)
                  {
                     System.out.println("--- " + "RS232_Mintaka_StarX_Read_Sensor_Data_Air_Temp_et_al_For_Obs() " + e);
                     mytemp.RH = "";
                  }
               }
               
               if (!(hulp_RH > 0.0) && (hulp_air_temp <= 100.0))
               {
                  mytemp.RH = "";
               }
            
            } // if ((hulp_return_obs_age > 0) && (hulp_return_obs_age <= 600)) 
            //else if (hulp_return_obs_age != AGE_NOT_OK)
            //{
            //   error_info = "sensor data reading ok but air temp and RH part obsolete";
            //   mytemp.air_temp         = "";
            //   mytemp.RH               = "";
            //   mytemp.wet_bulb_temp    = "";
            //   mytemp.double_dew_point = main.INVALID;
            //} // else if (hulp_return_obs_age != AGE_NOT_OK)
            else
            {
               error_info = "automatically retrieved sensor data reading obsolete or checksum not ok";
               mytemp.air_temp         = "";
               mytemp.RH               = "";
               mytemp.wet_bulb_temp    = "";
               mytemp.double_dew_point = main.INVALID;
            } // else
         } // try
         catch (InterruptedException | ExecutionException ex) 
         {
            error_info = "error retrieving air temp and RH data (" + ex + ")";
            mytemp.air_temp         = "";
            mytemp.RH               = "";
            mytemp.wet_bulb_temp    = "";
            mytemp.double_dew_point = main.INVALID;
         } // catch
         
         // general error (air temp and RH no value but error_info not yet set)
         if (mytemp.air_temp.equals("") && mytemp.RH.equals("") && error_info.equals(""))
         {
            error_info = "automatically retrieved air temp and RH data not available or checksum not ok";
            mytemp.double_dew_point = main.INVALID;
         }
         
         if (!error_info.equals(""))
         {
            System.out.println("--- " + error_info);
            
            final JOptionPane pane_end = new JOptionPane(error_info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            final JDialog checking_ports_end_dialog = pane_end.createDialog(main.APPLICATION_NAME);

            Timer timer_end = new Timer(2500, new ActionListener()
            {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  checking_ports_end_dialog.dispose();
               }
            });
            timer_end.setRepeats(false);
            timer_end.start();
            checking_ports_end_dialog.setVisible(true);   
         } // if (!error_info.equals(""))
         else // no error encountered
         {
            mytemp.jTextField1.setText(mytemp.air_temp);      // air temp
            mytemp.jTextField2.setText(mytemp.wet_bulb_temp); // wet-bulb temp
            
            // NB default is wet-bulb not frozen (so if no info available then by default the "wet-bulb not frozen" button will be checked) 
            if (hulp_wet_bulb_temp < 0.0)
            {
                mytemp.jRadioButton4.setSelected(true);       // wet-bulb frozen radio button
            }
            else
            {
                mytemp.jRadioButton3.setSelected(true);       // wet-bulb not frozen radio button
            }
            
            mytemp.jTextField4.setText(mytemp.RH);            // RH
         } // else
         
      } // protected void done()

   }.execute(); // new SwingWorker <Void, Void>()
  
   
}        
        


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_PPPP_For_Obs(boolean local_tray_icon_clicked, final boolean StarX) 
{
   // called from: - initSynopparameters() [mybarometer.java]
   //              - main_windowIconfied() [main.java]
   //
   //
   // MINTAKA STAR has an integrated GPS
   // GPS data is part of th saved pressure string eg:
   // <station pressure in mb>,<sea level pressure in mb>,<3 hour pressure tendency>,
   // <WMO tendency characteristic code>,<lat>,<long>,<course>,<speed>,<elevation>*<checksum>
   // where <lat> = ddd mm.mmmm[N|S], <long> = ddd mm.mmmm[E|W], 
   // <course> is True, <speed> in knots, <elevation> in meters
   //
   // 1018.61,1018.61,1.90,2, 15 39.0161N, 89 00.1226W,0,0,0*03
   // 1011.20,1011.20,,, 47 37.5965N,122 31.1453W,0,0,0*31
   //
   //
   // STARX (NB first part of STARX is the same as the STAR)
   //       TurboWinH 
   //             <station pressure in mb>,
   //             <sea level pressure in mb>,
   //             <3 hour pressure tendency>,
   //             <WMO tendency characteristic code>,
   //             <lat>,
   //             <long>,
   //             <course>,
   //             <speed>,
   //             <elevation>,
   //             <temperature>,
   //             <relativeHumidity>,
   //             <wetBulbTemperature>,
   //             <dewPoint>,
   //             <observationAge>
   //             *<checksum>
   //
   //             <lat> = ddd mm.mmmm[N|S], <long> = ddd mm.mmmm[E|W], <course> is True,
   //             <speed> in knots, <elevation> in meters, <relativeHumidity> is 0-100,
   //             temperatures are in degrees celsius, <observatoinAge> is in seconds.
   //
   // 1009.73,1007.73,0.00,0, 52 41.9491N,  6 14.1802E,0,1,7,19.5,65,15.4,12.8,58*16
   
   
   // initialisation
   mybarometer.pressure_reading = "";
   
   // initialisation
   main.tray_icon_clicked = local_tray_icon_clicked;
   

   new SwingWorker<String, Void>()
   {
      @Override
      protected String doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record                       = null;
         String laatste_record               = null;
         String local_obs_age                = "";
         int pos1 = 0;                       // position of the first "," in the last record
         int pos2 = 0;                       // position of the second "," in the last record
         int pos3 = 0;                       // position of the third "," in the last record
         int pos4 = 0;                                        
         int pos5 = 0;                                        
         int pos6 = 0;                                        
         int pos7 = 0;                                        
         int pos8 = 0;                                        
         int pos9 = 0;
         int pos10 = 0;                                        
         int pos11 = 0;                                        
         int pos12 = 0;
         int pos13 = 0;                                        
         int pos14 = 0;                       // pos of the "*" 


         // initialisation
         main.sensor_data_record_obs_pressure = "";

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // e.g. 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;
                  
                  
                  // retrieve the last record in the sensor data file
                  //
                  while ((record = in.readLine()) != null)
                  {
                     laatste_record = record;
                  } // while ((record = in.readLine()) != null)
                  
                  // check on minimum record length
                  //
                  if (laatste_record != null)
                  {
                     if (!(laatste_record.length() > 15))         // NB > 15 is a little bit arbitrary number (YYYYMMDDHHmm + 3 commas + at leat 2 char pressure value= 15 chars)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Star or StarX format (min. record length) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                     }
                  }
                  
                  // check on correct number of commas in the laatste_record (Star and StarX)
                  //
                  if (laatste_record != null)
                  {
                     int number_read_commas = 0;
                     int pos = -1;
                     
                     do
                     {
                        pos = laatste_record.indexOf(",", pos + 1);
                        if (pos != -1)     // "," found
                        {
                           number_read_commas++;
                           //System.out.println("+++ number_read_commas = " + number_read_commas);
                        }
                     } while (pos != -1); 
                        
                     if (StarX == false)
                     {
                        if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STAR)
                        {
                           laatste_record = null;
                           System.out.println("--- Mintaka Star format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                        }    
                     } // if (StarX == false)
                     else if (StarX == true)
                     {
                        if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STARX)
                        {
                           laatste_record = null;
                           System.out.println("--- Mintaka StarX format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")");
                           //System.out.println("--- Mintaka StarX format read commas = " + number_read_commas); 
                           //System.out.println("--- Mintaka StarX format format commas = " + main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STARX); 
                        }    
                     } // else if (StarX == true)
                  } // if (laatste_record != null)
                  
                  
                  // last retrieved record ok
                  //
                  if (laatste_record != null)
                  {
                     //System.out.println("+++ Mintaka Duo, last retrieved record = " + laatste_record);
                     
                     //String record_datum_tijd_minuten = laatste_record.substring(main.type_record_datum_tijd_begin_pos, main.type_record_datum_tijd_begin_pos + 12);  // bv 201302201345
                     
                     int pos = laatste_record.length() -12;                                               // pos is now start position of YYYYMMDDHHmm
                     String record_datum_tijd_minuten = laatste_record.substring(pos, pos + 12);  // YYYYMMDDHHmm has length 12
                     
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     ///System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min

                     if (timeDiff <= TIMEDIFF_SENSOR_DATA)      // max 5 minutes old
                     {
                        // cheksum check
                        //
                        // example Mintaka Star last_record : 1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211
                        // example Mintaka StarX last record: 1018.12,1018.13,0.00,0, 16 30.4429N, 88 21.9040W,0,2,7,29.4,85,27.4,26.7,1017.89,60*18
                        //
                        String record_checksum = laatste_record.substring(laatste_record.length() -14, laatste_record.length() -12);  // eg "24" from record "1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211"
                        String computed_checksum = Mintaka_Star_Checksum(laatste_record);
   
                        if (computed_checksum.equals(record_checksum))
                        {
                           if (StarX == false)
                           {
                              pos1 = laatste_record.indexOf(",", 0);                 // position of the first "," in the last record
                              
                              // not StarX then there is no obs age to be checked
                              local_obs_age = "0";
                              main.sensor_data_record_obs_pressure = laatste_record.substring(0, pos1);
                           }
                           else if (StarX == true)
                           {
                              // StarX example: // 1009.73,1007.73,0.00,0, 52 41.9491N,  6 14.1802E,0,1,7,19.5,65,15.4,12.8,58*16
                              pos1 = laatste_record.indexOf(",", 0);                                              // ML hereafter; position of the first "," in the last record
                              pos2 = laatste_record.indexOf(",", pos1 +1);                                        // ppp hereafter; position of the second "," in the last record
                              pos3 = laatste_record.indexOf(",", pos2 +1);                                        // a hereafter; position of the third "," in the last record
                              pos4 = laatste_record.indexOf(",", pos3 +1);                                        // lat hereafter; position of the 4th "," in the last record
                              pos5 = laatste_record.indexOf(",", pos4 +1);                                        // lon hereafter; position n of the 5th "," in the last record
                              pos6 = laatste_record.indexOf(",", pos5 +1);                                        // course hereafter; position of the 6th "," in the last record
                              pos7 = laatste_record.indexOf(",", pos6 +1);                                        // speed hereafter                                
                              pos8 = laatste_record.indexOf(",", pos7 +1);                                        // elevation hereafter
                              pos9 = laatste_record.indexOf(",", pos8 +1);                                        // air temp
                              pos10 = laatste_record.indexOf(",", pos9 +1);                                       // RH
                              pos11 = laatste_record.indexOf(",", pos10 +1);                                      // wet bulb                                      
                              pos12 = laatste_record.indexOf(",", pos11 +1);                                      // dew point
                              pos13 = laatste_record.indexOf(",", pos12 +1);                                      // observation age  
                              pos14 = laatste_record.indexOf("*", pos13 +1);                                      // pos of the "*" 
                              
                              local_obs_age = laatste_record.substring(pos13 +1, pos14); 
                              main.sensor_data_record_obs_pressure = laatste_record.substring(0, pos1);
                              
                           }
                          
                        
                           if (main.tray_icon_clicked == true)
                           {   
                              System.out.println("--- sensor data record, pressure for tray icon: " + main.sensor_data_record_obs_pressure);
                           }
                           else
                           {
                              System.out.println("--- sensor data record, pressure for barometer form: " + main.sensor_data_record_obs_pressure);
                           }  
                        } // if (computed_checksum.equals(record_checksum))
                        else // checksum not ok
                        {
                           local_obs_age = ""; 
                           main.sensor_data_record_obs_pressure = "";
                           System.out.println("--- automatically retrieved barometer reading checksum not ok");
                        } // else
                     } // if (timeDiff <= 5L)
                     else // timediff exceed limit
                     {
                        local_obs_age = ""; 
                        main.sensor_data_record_obs_pressure = "";
                        System.out.println("--- automatically retrieved barometer reading obsolete");
                     }
                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) {  }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
 
         // clear memory
         main.obs_file_datum_tijd      = null;

         
         return local_obs_age;
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         final int AGE_NOT_OK                = 999999;
         String error_info                   = "";
         double hulp_double_pressure_reading = Double.MAX_VALUE;
         
         try 
         {
            // obs age (in sec) of the last record
            //
            String return_obs_age = get();
            
            int int_return_obs_age = AGE_NOT_OK;                 // 999999 = random number but > 99.9
            if ( (return_obs_age.compareTo("") != 0) && (return_obs_age != null) && (return_obs_age.indexOf("*") == -1) )
            {
               try
               {
                  int_return_obs_age = Integer.parseInt(return_obs_age.trim());
               }
               catch (NumberFormatException e)
               {
                  int_return_obs_age = AGE_NOT_OK; 
                  System.out.println("--- " + "RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_a_ppp_Data_Files_For_Obs() " + e);
                  //return_obs_age = "";
               }
            } 
            
            if ((int_return_obs_age >= 0) && (int_return_obs_age <= MAX_AGE_STARX_OBS_DATA))
            {         
               if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) && (main.sensor_data_record_obs_pressure != null))
               {
                  //double hulp_double_pressure_reading;
                  try
                  {
                     hulp_double_pressure_reading = Double.parseDouble(main.sensor_data_record_obs_pressure.trim());
                     hulp_double_pressure_reading = Math.round(hulp_double_pressure_reading * 10) / 10.0d;  // bv 998.19 -> 998.2
                  }
                  catch (NumberFormatException e)
                  {
                     System.out.println("--- " + "Function RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_PPPP_For_Obs() " + e);
                     error_info = "air pressure data parsing error";
                     hulp_double_pressure_reading = Double.MAX_VALUE;
                  }   
             
                  if (!(hulp_double_pressure_reading > 900.0) && (hulp_double_pressure_reading < 1100.0))
                  {
                     error_info = "air pressure data outside boundary limits";
                     hulp_double_pressure_reading = Double.MAX_VALUE;
                  }
                     
               } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
               else
               {
                  error_info = "air pressure data not available";
                  hulp_double_pressure_reading = Double.MAX_VALUE;
               }
          
            } // if ((hulp_return_obs_age >= 0) && (hulp_return_obs_age <= MAX_AGE_STARX_OBS_DATA))
            else
            {
               error_info = "air pressure data obsolete";
               hulp_double_pressure_reading = Double.MAX_VALUE;
            }
            
         }  // try 
         catch (InterruptedException | ExecutionException ex) 
         {
            error_info = "error retrieving air pressure data (" + ex + ")";
            hulp_double_pressure_reading = Double.MAX_VALUE;
         } // catch         
         
         
         // general error eg NumberFormatException when parsing 
         if (error_info.equals("") && (hulp_double_pressure_reading > 999999.9))   
         {
            error_info = "automatically retrieved air pressure general error or checksum not ok";
         }
         
         if (!error_info.equals(""))
         {
            System.out.println("--- " + error_info);
            
            final JOptionPane pane_end = new JOptionPane(error_info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            final JDialog dialog = pane_end.createDialog(main.APPLICATION_NAME);

            Timer timer_end = new Timer(2500, new ActionListener()
            {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  dialog.dispose();
               }
            });
            timer_end.setRepeats(false);
            timer_end.start();
            dialog.setVisible(true);   
         } // if (!error_info.equals(""))
         else // no error encountered
         {
            if (main.tray_icon_clicked == true)
            {   
               String pressure_sensor_height = "";
                  
               // NB pressure at sensor height = pressure reading + ic
               double double_barometer_instrument_correction = Double.parseDouble(main.barometer_instrument_correction.trim());
               if ((double_barometer_instrument_correction > -4.0) && (double_barometer_instrument_correction < 4.0))
               {        
                  // 1 digit precision
                  // NB example (double)Math.round(value * 100000d) / 100000d -> rounding for 5 digits precision
                  pressure_sensor_height = Double.toString(Math.round((hulp_double_pressure_reading + double_barometer_instrument_correction) * 10d) / 10d);
               }
               else
               {
                  // 1 digit precision
                  pressure_sensor_height = Double.toString(Math.round(hulp_double_pressure_reading * 10d) / 10d);
               }
                  
               cal_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // geeft systeem datum tijd in UTC van dit moment
               cal_system_date_time.add(Calendar.MINUTE, -1);                              // averaged the data is 1 minute old
               String date_time = sdf8.format(cal_system_date_time.getTime());  
                  
               String info = "";
               info = date_time + " UTC " + "\n" +
                                  "\n" +
                                  "pressure at sensor height: " + pressure_sensor_height + " hPa" + "\n";
               //info = date_time + " UTC " + main.newline +
               //       main.newline +
               //       "pressure at sensor height: " + mybarometer.pressure_reading + " hPa" + main.newline;

               main.trayIcon.displayMessage(main.APPLICATION_NAME, info, MessageType.INFO);
            } // if (main.tray_icon_clicked == true)
            else // barometer input screen opened
            {        
               mybarometer.pressure_reading = Double.toString(hulp_double_pressure_reading);
                  
               // check if barometer input page is opened
               if (mybarometer.jTextField1 != null) 
               {
                  // http://stackoverflow.com/questions/17397442/how-to-check-if-a-jframe-is-opened
               }
                  
               mybarometer.jTextField1.setText(mybarometer.pressure_reading); 
               mybarometer.jTextField2.requestFocus();      // focus on "insert draft" textfield
            } // else   
            
         } // else (no error encountered)
         
      } // protected void done()

   }.execute(); // new SwingWorker<String, Void>()
  
}

  

/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void RS422_write_sensor_data_to_file() 
{
      ///// EUCAWS ONLY //////
   
      // called from class RS422_V1_Class_Receive_Sensor_Data
      //
   
   
      // NB PTB220:
      // er wordt uiteindelijk geschreven bv:
      // $3  996.87 ****.** ****.** 996.87 0   3$
      //
      // maar eigelijk worden er bv 3 stukken string geschreven!!!! bv:
      // $3  996.87 ***
      // *.** ****.**
      // 996.87 0   3 #r #n$
      //
      // NB Dit is vrij normaal voor een serieele aansluiting 14 bytes per keer ingelezen
      //    "De veel gebruikte UART chip type 16550 bevat een buffer van 14 bytes voor ontvangen en
      //    16 bytes voor zenden, zodat de CPU van de PC niet voor iedere byte aandacht aan de poort hoeft te besteden."
      //    (http://nl.wikipedia.org/wiki/Seri%C3%ABle_poort)
      // NB via laptop virtuele seriele poort (USB) lijkt dat er veel minder bytes per keer wordt ingelezen!
      //
      // NB PTB330 via service port alleen een luchtdruk bv 1020.33
      //    PTB330 via user port kan verschillend zijn?? (maar er is wel een default??)
      //
      //
      // NB geen Swingworker hier gebruiken (worden dan alleen stukken geschreven)
      //
   
      BufferedWriter out                                        = null;
      boolean doorgaan                                          = false;
      
      // if time is 57, 58 or 59 sec round to next whole minute
      //
      // to prevent such situations:
      //
      // $PEUMB,130815,121900,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815121859
      // $PEUMB,130815,122000,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815121959
      // $PEUMB,130815,122100,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122100
      // $PEUMB,130815,122200,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122200
      // $PEUMB,130815,122300,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122259
      // $PEUMB,130815,122400,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122400
      // $PEUMB,130815,122500,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122459
      // $PEUMB,130815,122600,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122559
      // $PEUMB,130815,122700,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122700
      // $PEUMB,130815,122800,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122800
      // $PEUMB,130815,122900,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122900
      // $PEUMB,130815,123000,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815123000
      // $PEUMB,130815,123100,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815123100
      // $PEUMB,130815,123200,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815123200
      // $PEUMB,130815,123300,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815123300
      // $PEUMB,130815,123400,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815123359
      //
      // NB seconds display at the end of every record only during testing  (eg 20130815122900, operational this will be 01308151229)
      
      cal_sensor_datum_tijd = new GregorianCalendar();
      String number_sec = sdf7.format(cal_sensor_datum_tijd.getTime());  // format sdf7 = "ss" (show only seconds)
      switch (number_sec)
      {   
         case "59" : cal_sensor_datum_tijd.add(Calendar.SECOND, + 1);
         break;
         case "58" : cal_sensor_datum_tijd.add(Calendar.SECOND, + 2);
         break;
         case "57" : cal_sensor_datum_tijd.add(Calendar.SECOND, + 3);
         break;
      }        

      //String file_naam = "sensor_data_" + main.sdf3.format(new Date()) + ".txt";
      String file_naam = "sensor_data_" + main.sdf3.format(cal_sensor_datum_tijd.getTime()) + ".txt";
      
      //String volledig_path_sensor_data = logs_dir + java.io.File.separator + SENSOR_DATA_SUB_DIR + java.io.File.separator + file_naam;
      String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + java.io.File.separator + file_naam;

      if ((main.logs_dir != null) && (main.logs_dir.compareTo("") != 0))
      {
         // NB in Function RS422_initComponents() there was already checked on dir logs_dir
         doorgaan = true;
      }
      

      if (doorgaan)
      {
         try
         {
            //BufferedWriter out = new BufferedWriter(new FileWriter(volledig_path_sensor_data, true));// true means append the specified data to the file i.e. the pre-exist data in a file is not overwritten and the new data is appended after the pre-exist data.
            out = new BufferedWriter(new FileWriter(volledig_path_sensor_data, true));// true means append the specified data to the file i.e. the pre-exist data in a file is not overwritten and the new data is appended after the pre-exist data.

            // NB Alleen onderstaande zou al voldoende zijn als er geen aparte datum/tijd toegevoegd zou hoeven te worden
            ///out.write(total_string);

            if (main.total_string.indexOf("\n") != -1)
            {
               // test-record can be called by pressing the "Test" button on the instrument connections settings page
               test_record = main.sdf_tsl_2.format(new Date()) + " UTC";   // // new Date() -> always in UTC 
               
               
               out.write(main.total_string.replaceAll("[\r\n]", ""));
               out.write(",");                                                // NB "," : typically for AWS connection (record data separated by commas)
               
               //out.write(main.sdf4.format(new Date()));                       // new Date() -> altijd in UTC
               out.write(main.sdf4.format(cal_sensor_datum_tijd.getTime()));
               out.newLine();
               
               // only if the last piece of a record string was written check for presence parameters (if parameter present disable the input on TurboWin+ input form)
               //RS422_Read_AWS_Sensor_Data(); // and also if a parameter is present in the saved sensor data file then disable this parameter on the TurboWin+ input form
            }
            else
            {
               out.write(main.total_string.replace("\r", ""));
            }

            //out.close();

         } // try
         catch (Exception e)
         {
            JOptionPane.showMessageDialog(null, "unable to write to: " + volledig_path_sensor_data, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         } // catch
         finally
         {
            try 
            {
               if (out != null)
               {   
                  out.close();
               }
            } 
            catch (IOException ex) 
            {
               // Logger.getLogger(RS232_AWS_1View.class.getName()).log(Level.SEVERE, null, ex);
            }

         } // finally
      } // if (doorgaan)

   }
     


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/

public void RS422_V3_write_sensor_data_to_file() 
{
      ///// EUCAWS serial and OMC-140 serial and OMC-140 LAN  //////
   
      // called from - class RS422_V3_Class_Receive_Sensor_Data
      //             - class Ethernet_Class_Receive_UDP
   
   
      // NB PTB220:
      // er wordt uiteindelijk geschreven bv:
      // $3  996.87 ****.** ****.** 996.87 0   3$
      //
      // maar eigelijk worden er bv 3 stukken string geschreven!!!! bv:
      // $3  996.87 ***
      // *.** ****.**
      // 996.87 0   3 #r #n$
      //
      // NB Dit is vrij normaal voor een serieele aansluiting 14 bytes per keer ingelezen
      //    "De veel gebruikte UART chip type 16550 bevat een buffer van 14 bytes voor ontvangen en
      //    16 bytes voor zenden, zodat de CPU van de PC niet voor iedere byte aandacht aan de poort hoeft te besteden."
      //    (http://nl.wikipedia.org/wiki/Seri%C3%ABle_poort)
      // NB via laptop virtuele seriele poort (USB) lijkt dat er veel minder bytes per keer wordt ingelezen!
      //
      // NB PTB330 via service port alleen een luchtdruk bv 1020.33
      //    PTB330 via user port kan verschillend zijn?? (maar er is wel een default??)
      //
      //
      // NB geen Swingworker hier gebruiken (worden dan alleen stukken geschreven)
      //
   
      BufferedWriter out         = null;
      boolean doorgaan           = false;
      boolean peumb_update       = false;
      boolean retry              = false;
      
      // if time is 57, 58 or 59 sec round to next whole minute
      //
      // to prevent such situations:
      //
      // $PEUMB,130815,121900,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815121859
      // $PEUMB,130815,122000,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815121959
      // $PEUMB,130815,122100,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122100
      // $PEUMB,130815,122200,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122200
      // $PEUMB,130815,122300,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122259
      // $PEUMB,130815,122400,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122400
      // $PEUMB,130815,122500,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122459
      // $PEUMB,130815,122600,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122559
      // $PEUMB,130815,122700,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122700
      // $PEUMB,130815,122800,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122800
      // $PEUMB,130815,122900,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815122900
      // $PEUMB,130815,123000,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815123000
      // $PEUMB,130815,123100,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815123100
      // $PEUMB,130815,123200,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815123200
      // $PEUMB,130815,123300,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815123300
      // $PEUMB,130815,123400,...........................4.2,350.3,7.8,358.3,23.1,28.3,-38,,,,20130815123359
      //
      // NB seconds display at the end of every record only during testing  (eg 20130815122900, operational this will be 01308151229)
      
      cal_sensor_datum_tijd = new GregorianCalendar();
      String number_sec = sdf7.format(cal_sensor_datum_tijd.getTime());  // format sdf7 = "ss" (show only seconds)
      switch (number_sec)
      {   
         case "59" : cal_sensor_datum_tijd.add(Calendar.SECOND, + 1);
         break;
         case "58" : cal_sensor_datum_tijd.add(Calendar.SECOND, + 2);
         break;
         case "57" : cal_sensor_datum_tijd.add(Calendar.SECOND, + 3);
         break;
      }        

      //String file_naam = "sensor_data_" + main.sdf3.format(new Date()) + ".txt";
      String file_naam = "sensor_data_" + main.sdf3.format(cal_sensor_datum_tijd.getTime()) + ".txt";
      
      //String volledig_path_sensor_data = logs_dir + java.io.File.separator + SENSOR_DATA_SUB_DIR + java.io.File.separator + file_naam;
      String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + java.io.File.separator + file_naam;

      if ((main.logs_dir != null) && (main.logs_dir.compareTo("") != 0))
      {
         // NB in Function RS422_initComponents() there was already checked on dir logs_dir
         doorgaan = true;
      }
      

      if (doorgaan)
      {
         try
         {
            //BufferedWriter out = new BufferedWriter(new FileWriter(volledig_path_sensor_data, true));// true means append the specified data to the file i.e. the pre-exist data in a file is not overwritten and the new data is appended after the pre-exist data.
            out = new BufferedWriter(new FileWriter(volledig_path_sensor_data, true));// true means append the specified data to the file i.e. the pre-exist data in a file is not overwritten and the new data is appended after the pre-exist data.

            for (int b = 0; b < main.total_string.length(); b++)
            {
               if (main.total_string.charAt(b) == '\r' || main.total_string.charAt(b) == '\n') 
               {
                  //if (message_peumb.toString().contains("$PEUMB"))
                  
                  // NB in case of OMC-140 LAN:
                  //               UdPbC\0\s:D263G4,s:WI4263*31\$PEUMB,20180411,060028,52.5,4.9,257.2,4.0,259.2,1006.6,991.5,0.7,4,19.6,28.9,3.9,24.4,131.8,28.6,139.9,34.6,73.3,,,,,,*57
                  //    stared as: $PEUMB,20180411,060028,52.5,4.9,257.2,4.0,259.2,1006.6,991.5,0.7,4,19.6,28.9,3.9,24.4,131.8,28.6,139.9,34.6,73.3,,,,,,*57
                  //
                  // but will have no effect on OMC-140 serial and EUCAWS serial 
                  //
                  int start = message_peumb.toString().indexOf("$PEUMB");
                  if (start != -1)   
                  {
                     // test_record: for dispaying on main screen date/ime of this last received data update
                     test_record = main.sdf_tsl_2.format(new Date()) + " UTC";   // // new Date() -> always in UTC  
                      
                     //out.write(message_peumb.toString().replaceAll("[\r\n]", ""));
                     out.write(message_peumb.toString().substring(start).replaceAll("[\r\n]", ""));
                     out.write(",");                                                // NB "," : typically for AWS connection (record data separated by commas)
               
                     out.write(main.sdf4.format(cal_sensor_datum_tijd.getTime()));
                     out.newLine();
                     
                     peumb_update = true;
                  }
                  message_peumb.setLength(0);
               }   
               else
               {
                  message_peumb.append(main.total_string.charAt(b));
               }
                  
            } // for (int b = 0; b < main.total_string.length(); b++)
            
            // NB Alleen onderstaande zou al voldoende zijn als er geen aparte datum/tijd toegevoegd zou hoeven te worden
            //out.write(main.total_string);

            //out.close();

         } // try
         catch (Exception e)
         {
            JOptionPane.showMessageDialog(null, "unable to write to: " + volledig_path_sensor_data, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         } // catch
         finally
         {
            try 
            {
               if (out != null)
               {   
                  out.close();
               }
            } 
            catch (IOException ex) 
            {
               // Logger.getLogger(RS232_AWS_1View.class.getName()).log(Level.SEVERE, null, ex);
            }

         } // finally
      } // if (doorgaan)
      
      
      if (peumb_update)
      {
         // read the AWS data which was saved before by TurboWin+ from a sensor data file
         //
         // NB DIT ZOU BETER KUNNEN DOOR GELIJK UIT DE message_peumb string te lezen !!!!!!!
         //
         RS422_Read_AWS_Sensor_Data_For_Display();  
                  
         // update main screen with application name + mode and date time last received data (single info line botton screen)
         String info = "";
         if ((main_RS232_RS422.test_record.equals("")) || (main_RS232_RS422.test_record == null)) // test_rcord was set in RS422_write_sensor_data_to_file()
         {
            info = "no data";
         } 
         else 
         {
            info = main_RS232_RS422.test_record;
         }     
         main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received AWS data: " + info);
                     
         // for checking that the displayed data is not obsolete (i.e. not updated last 5 minutes)
         last_new_data_received_TimeMillis = System.currentTimeMillis(); // see also Function: RS422_init_new_aws_data_received_check_timer() [main_RS232_422.java]
      
         
         // AWSR (Automatic Weather Station Reporting)
         if (main.AWSR == true)
         {
            cal_AWSR_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));   // system date time in UTC of this moment
            cal_AWSR_system_date_time.getTime();                                               // now effective
            int AWSR_system_hour_local = cal_AWSR_system_date_time.get(Calendar.HOUR_OF_DAY);   // HOUR_OF_DAY is used for the 24-hour clock. E.g., at 10:04:15.250 PM the HOUR_OF_DAY is 22.
            int AWSR_system_minute_local = cal_AWSR_system_date_time.get(Calendar.MINUTE);
                  
            if ((AWSR_system_minute_local == 0) && (AWSR_system_hour_local % Integer.valueOf(main.AWSR_reporting_interval) == 0))   // e.g. every 1 hour (AWSR_reporting_interval);               
            {
               retry = false;
                        
               // logging
               String message = "[AWSR] scheduled upload"; 
               main.log_turbowin_system_message(message);
               
               // checking
               boolean AWSR_settings_ok = RS422_check_AWSR_settings();
               boolean GPS_date_time_ok = true;
      
               GPS_date_time_ok = true;
               // NB in function RS422_Read_And_Send_Sensor_Data_For_AWSR() the date time of the last saved record will be checked, see below
                        
               if (AWSR_settings_ok && GPS_date_time_ok) // GPS date-time was compared with system date-time                    
               {
                  // NB in this stage we are sure the date-time is correct! + we are sure position is available!
                             
                  // read the AWS data which was saved before by TurboWin+ from a sensor data file
                  RS422_Read_And_Send_Sensor_Data_For_AWSR(retry); // nB in this function also the date-time of the last saved record will be checked
                  
               } // if (AWSR_settings_ok && GPS_date_time_ok) 
            } // if ((AWSR_system_minute_local == 0) etc.
            else if ((AWSR_server_response_code == main.RESPONSE_NO_INTERNET) && (AWSR_system_minute_local == AWSR_RETRY_MINUTES) && (AWSR_system_hour_local % Integer.valueOf(main.AWSR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
            {
               retry = true;
                        
               // logging
               String message = "[AWSR] retry scheduled upload"; 
               main.log_turbowin_system_message(message);
                        
               // checking
               boolean AWSR_settings_ok = RS422_check_AWSR_settings();
               boolean GPS_date_time_ok = true;
                        
               // NB Mintaka StarX saved data do not contain date/time information so it is not possible to check this
               //    so it is made always true
               GPS_date_time_ok = true;
                     
               if (AWSR_settings_ok && GPS_date_time_ok) // GPS date-time was compared with system date-time                    
               {
                  // NB in this stage we are sure the date-time is correct! + we are sure position is available!
                  RS422_Read_And_Send_Sensor_Data_For_AWSR(retry);           
                                             
               } // if (APR_settings_ok && GPS_date_time_ok) 
                        
               // reset (to be sure)
               AWSR_server_response_code = 0;
                        
            } // else if ((APR_server_response_code = main.RESPONSE_NO_INTERNET) etc.
            else if ((AWSR_server_response_code == main.RESPONSE_NO_INTERNET) && (AWSR_system_minute_local < AWSR_RETRY_MINUTES) && (AWSR_system_hour_local % Integer.valueOf(main.AWSR_reporting_interval) == 0))   // e.g. every 1 hour (AWSR_reporting_interval);               
            {
               // text will be visible only a few minutes (till the retry upload)
               main.jTextField4.setText("[AWSR] retry AWS report upload within a few minutes");
            }
            else
            {
               // text with next AWSR update bottom status line TurboWin+ main screen (updated every minute)
               for (int i = (AWSR_system_hour_local + 1); i < (AWSR_system_hour_local + 1 + Integer.valueOf(main.AWSR_reporting_interval)); i++)
               {
                  // 24 hours or higher makes no difference for this computation
                  if (i % Integer.valueOf(main.AWSR_reporting_interval) == 0)
                  {
                     cal_AWSR_system_date_time.add(Calendar.HOUR_OF_DAY, i - AWSR_system_hour_local);   // add 1 - 5 hours
                     cal_AWSR_system_date_time.getTime();
                                 
                     String next_AWSR_string = "[AWSR] next automated AWS report upload: " + cal_AWSR_system_date_time.get(Calendar.DAY_OF_MONTH) + " " + main.convert_month(cal_AWSR_system_date_time.get(Calendar.MONTH)) + " " + cal_AWSR_system_date_time.get(Calendar.YEAR) + " " + cal_AWSR_system_date_time.get(Calendar.HOUR_OF_DAY) + ".00 UTC"; 
                                 
                     main.jTextField4.setText(next_AWSR_string);
                     break;
                  }
               } // for (int i = (APR_system_hour_local + 1); etc.
            } // else
                        
            cal_AWSR_system_date_time = null; 
         
         } // if (main.AWSR == true)
      } // if (peumb_update)

   }



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void RS232_Format_Barometer_Output_3()
  {
    
      // NB Indien Vaisala barometer in POLL mode dan moeilijk deze hier uit te halen (zie manual aantekeningen)
      //    (open 0 <cr> -> smode run <cr> -> reset <cr>)
      //    Dit moet dan met putty of hyperterminal o.i.d. (in principe zal een barometer nooit in poll mode staan)

      SerialPort serialPort_form                     = null;
      String hulp_parity                             = "";          // only for message writing in java console


      if (main.defaultPort == null)
      {
	      String info = "[BAROMETER] selected serial port [function: Format_Barometer_Output_3()] " + main.defaultPort_descriptive + " not found";
         System.out.println(info);
         // JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
	   }


      if (main.defaultPort != null)
      {
         serialPort_form = SerialPort.getCommPort(main.defaultPort);//serialPort_form = new SerialPort(main.defaultPort);
         serialPort_form.openPort(); 
         if (serialPort_form.isOpen())
         {
            if (main.parity == 0)
            {
               hulp_parity = "none";
            }
            else if (main.parity == 1)
            {
               hulp_parity = "odd";
            }
            else if (main.parity == 2)
            {
               hulp_parity = "even";
            }               
              
            System.out.println("[BAROMETER] opening with " + String.valueOf(main.bits_per_second) + " " + hulp_parity + " " + String.valueOf(main.data_bits) + " " + String.valueOf(main.stop_bits) + " " + main.defaultPort_descriptive);
            serialPort_form.setComPortParameters(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
            serialPort_form.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

             
            if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)     // PTB220 or PRB330
            {   
               //System.out.println("[BAROMETER] Writing \"" + "S" + "\" to " + serialPort_form.getDescriptivePortName());
               System.out.println("[BAROMETER] Writing \"" + "S" + "\" to " + main.defaultPort_descriptive);
               String messageString_stop = "S\r";
               byte[] bytes_message_stop = messageString_stop.getBytes(StandardCharsets.UTF_8); // Java 7+ only
               serialPort_form.writeBytes(bytes_message_stop, bytes_message_stop.length);       // Write data to port 
                  
               // NB reset niet nodig (heeft geen effect)

            } // if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2) 
            else if (main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5)   // Mintaka Duo or Mintaka Star USB
            {
               System.out.println("[BAROMETER] Writing \"" + "asq" + "\" to " + main.defaultPort_descriptive);
               String messageString_stop = "asq\r";                                    // asq = terminate autosampling
               byte[] bytes_message_stop = messageString_stop.getBytes(StandardCharsets.UTF_8); // Java 7+ only
               serialPort_form.writeBytes(bytes_message_stop, bytes_message_stop.length);       // Write data to port 
            } // else if (main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5)            
            
            try
            {
               Thread.sleep(2000);       // gaat eigenlijk ook altijd goed bij 1000
            }
            catch (InterruptedException ex)
            {
               System.out.println("[BAROMETER] InterruptedException [function: RS232_Format_Barometer_Output_3()] " + ex);
            }            
            
            // flush ???
            
            if ((main.RS232_connection_mode == 1) || (main.RS232_connection_mode == 2))    // PTB220 or PTB330
            {
               System.out.println("[BAROMETER] Writing \"" + "ECHO OFF" + "\" to " + main.defaultPort_descriptive);
               String messageString_echo = "ECHO OFF\r";
               byte[] bytes_message_echo = messageString_echo.getBytes(StandardCharsets.UTF_8); // Java 7+ only
               serialPort_form.writeBytes(bytes_message_echo, bytes_message_echo.length);       // Write data to port 
            }           
            
            if (main.RS232_connection_mode == 1)             // PTB220
            {
               // NB LET OP DE TE VERZENDEN FORMAT STRING NAAR PTB220 MAG NIET TE LANG ZIJN (ongeveer 50 char -100 bytes-)!!!!
               System.out.println("[BAROMETER] Writing \"" + "form 4.2 P \" \" 4.2 HCP \" \" 3.1 TREND \" \" A \" \" #r #n" + "\" to " + main.defaultPort_descriptive);
               String messageString_format2 = "form 4.2 P \" \" 4.2 HCP \" \" 3.1 TREND \" \" A \" \" #r #n\r";
               //String messageString_format2 = "form P \" \" P \" \" P \" \" P \" \" P \" \" P \" \" P \" \" #r #n\r";
               //String messageString_format2 = "form \"0123456789 0123456789 01234567 \" P \" \" #r #n\r"; // deze gaat noog net goed
               byte[] bytes_message_format2 = messageString_format2.getBytes(StandardCharsets.UTF_8); // Java 7+ only
               serialPort_form.writeBytes(bytes_message_format2, bytes_message_format2.length);       // Write data to port 
            } // if (barometer_type.equals(PTB220))
            else if (main.RS232_connection_mode == 2)       // PTB330
            {
               // test voor bxpm171 - bxpm80
               //String messageString_format = "form 4.2 P \" \" 4.2 HCP \" \" 4.2 QNH \" \" 3.1 P3H \" \" 1.1 A3H \" \" DIT IS EEN TEST 1234567890 1234567890 1234567890 wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ X\r\n";
               //String messageString_format = "123456789";
               System.out.println("[BAROMETER] Writing \"" + "form 4.2 P \" \" 4.2 HCP \" \" 4.2 QNH \" \" 3.1 P3H \" \" 1.1 A3H \" \" #r #n" + "\" to " + main.defaultPort_descriptive);
               String messageString_format = "form 4.2 P \" \" 4.2 HCP \" \" 4.2 QNH \" \" 3.1 P3H \" \" 1.1 A3H \" \" #r #n\r";
               byte[] bytes_message_format = messageString_format.getBytes(StandardCharsets.UTF_8); // Java 7+ only
               serialPort_form.writeBytes(bytes_message_format, bytes_message_format.length);       // Write data to port 
               
            } //  else if (barometer_type.equals(PTB330_USER_PORT) || etc
            else if (main.RS232_connection_mode == 4)       // Mintaka Duo
            {            
               System.out.println("[BAROMETER] Writing \"" + "as 60 TurboWin" + "\" to " + main.defaultPort_descriptive);
               String messageString_format = "as 60 TurboWin\r\n";
               //serialPort_form.writeBytes(messageString_format.getBytes());                    // Write data to port  
               byte[] bytes_message_format = messageString_format.getBytes(StandardCharsets.UTF_8); // Java 7+ only
               serialPort_form.writeBytes(bytes_message_format, bytes_message_format.length);       // Write data to port 

            } // else if (main.RS232_connection_mode == 4) 
            
            else if (main.RS232_connection_mode == 5)       // Mintaka Star USB
            {            
               System.out.println("[BAROMETER] Writing \"" + "as 60 TurboWinG" + "\" to " + main.defaultPort_descriptive);
               String messageString_format = "as 60 TurboWinG\r\n";                            // note: turbowing !!
               //serialPort_form.writeBytes(messageString_format.getBytes());                    // Write data to port  
               byte[] bytes_message_format = messageString_format.getBytes(StandardCharsets.UTF_8); // Java 7+ only
               serialPort_form.writeBytes(bytes_message_format, bytes_message_format.length);       // Write data to port 

            } // else if (main.RS232_connection_mode == 5) 
            else if (main.RS232_connection_mode == 7)       // Mintaka Star + StarX USB
            {            
               System.out.println("[BAROMETER] Writing \"" + "as 60 TURBOWINH" + "\" to " + main.defaultPort_descriptive);
               String messageString_format = "as 60 TURBOWINH\r\n";                            // note: turbowinH !!
               //serialPort_form.writeBytes(messageString_format.getBytes());                    // Write data to port  
               byte[] bytes_message_format = messageString_format.getBytes(StandardCharsets.UTF_8); // Java 7+ only
               serialPort_form.writeBytes(bytes_message_format, bytes_message_format.length);       // Write data to port 

            } // else if (main.RS232_connection_mode == 7) 
            
            else
            {
               System.out.println("[BAROMETER] barometer type unknown [function: RS232_Format_Barometer_Output()_3]");
            }
            
            if ((main.RS232_connection_mode == 1) || (main.RS232_connection_mode == 2))         // PTB220 or PTB330
            {   
               // NB INTV 60 s niet voor het form commando uitvoeren!!!!!!!
               System.out.println("[BAROMETER] Writing \"" + "INTV 60 s" + "\" to " + main.defaultPort_descriptive);
               String messageString_int = "INTV 60 s\r";
               byte[] bytes_message_int = messageString_int.getBytes(StandardCharsets.UTF_8); // Java 7+ only
               serialPort_form.writeBytes(bytes_message_int, bytes_message_int.length);       // Write data to port 

               //flush??
            } // if ((main.RS232_connection_mode == 1) || (main.RS232_connection_mode == 2))    // PTB220 or PTB330
            
            if ((main.RS232_connection_mode == 1) || (main.RS232_connection_mode == 2))    // PTB220 or PTB330
            {   
               System.out.println("[BAROMETER] Writing \"" + "R" + "\" to " + main.defaultPort_descriptive);
               String messageString_cont_R = "R\r";
               byte[] bytes_message_cont_R = messageString_cont_R.getBytes(StandardCharsets.UTF_8); // Java 7+ only
               serialPort_form.writeBytes(bytes_message_cont_R, bytes_message_cont_R.length);       // Write data to port 

               // flush ??
            } // if ((main.RS232_connection_mode == 1) || (main.RS232_connection_mode == 2))     // PTB220 or PTB330
            
            try
            {
               Thread.sleep(1000);       // gaat eigenlijk ook altijd goed bij 1000
            }
            catch (InterruptedException ex)
            {
               System.out.println("[BAROMETER] InterruptedException [function: Format_Barometer_Output()] " + ex);
            }            
            
            // close the serial port
            serialPort_form.closePort();
            
         } // if (serialPort_form.isOpen())
         else        
         {
            //System.out.println("[BAROMETER] Function: RS232_Format_Barometer_Output_3() " + ex);
            System.out.println("[BAROMETER] Couldn't open (for formatting) serial port:  " + main.defaultPort_descriptive);
         }         
         
      } // if (defaultPort != null)

   }

 
    
/***********************************************************************************************/
/*                                                                                             */
/*                            RS232_Read_Sensor_Data_PPPP_For_Obs                              */
/*                                                                                             */
/***********************************************************************************************/
public static void RS232_Read_Sensor_Data_PPPP_For_Obs(boolean local_tray_icon_clicked)
{
   // Vaisala PTB220 or PTB330 (NOT AWS and NOT Mintaka Duo and NOT Mintaka Star)
   
   
   // called from: - initSynopparameters() [mybarometer.java]
   //              - main_windowIconfied() [main.java]
   //
   
   
   //if (barometer_type.equals(PTB220))
   if (main.RS232_connection_mode == 1)       // PTB220
   {
      main.type_record_lengte                 = main.RECORD_LENGTE_PTB220;
      main.type_record_datum_tijd_begin_pos   = main.RECORD_DATUM_TIJD_BEGIN_POS_PTB220;
      //type_record_minuten_begin_pos      = RECORD_MINUTEN_BEGIN_POS_PTB220;
      main.type_record_pressure_begin_pos     = main.RECORD_P_BEGIN_POS_PTB220;
   }
   //else if (barometer_type.equals(PTB330_SERVICE_PORT))
   //else if (barometer_type.equals(PTB330))   
   else if (main.RS232_connection_mode == 2)  // PTB330   
   {
      main.type_record_lengte                 = main.RECORD_LENGTE_PTB330;
      main.type_record_datum_tijd_begin_pos   = main.RECORD_DATUM_TIJD_BEGIN_POS_PTB330;
      //type_record_minuten_begin_pos      = RECORD_MINUTEN_BEGIN_POS_PTB330;
      main.type_record_pressure_begin_pos     = main.RECORD_P_BEGIN_POS_PTB330;
   }
   
   // initialisation
   mybarometer.pressure_reading = "";
   
   // initialisation
   main.tray_icon_clicked = local_tray_icon_clicked;
   

   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record         = null;
         String laatste_record = null;

         // initialisation
         main.sensor_data_record_obs_pressure = "";

         // present obs value
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // geeft bv 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;

                  while ((record = in.readLine()) != null)
                  {
                     if (record.length() == main.type_record_lengte)
                     {
                        laatste_record = record;
                     } 
                  } // while ((record = in.readLine()) != null)

                  if (laatste_record != null)
                  {
                     String record_datum_tijd_minuten = laatste_record.substring(main.type_record_datum_tijd_begin_pos, main.type_record_datum_tijd_begin_pos + 12);  // bv 201302201345
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     ///System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min

                     //String record_pressure_present = null;
                     if (timeDiff <= 3L)      // max 3 minutes old
                     {
                        main.sensor_data_record_obs_pressure = laatste_record.substring(main.type_record_pressure_begin_pos, main.type_record_pressure_begin_pos + 7);
                  
                        // rounding eg: 998.19 -> 998.2
                        //        double digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim()) + HOOGTE_CORRECTIE;
                        //        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;  // bv 998.19 -> 998.2
                        
                        if (main.tray_icon_clicked == true)
                        {   
                           System.out.println("--- sensor data record, pressure for tray icon: " + main.sensor_data_record_obs_pressure);
                        }
                        else
                        {
                           System.out.println("--- sensor data record, pressure for barometer form: " + main.sensor_data_record_obs_pressure);
                        }   
                     } // if (timeDiff <= 3L)
                     else
                     {
                        main.sensor_data_record_obs_pressure = "";
                        System.out.println("--- automatically retrieved barometer reading obsolete");
                     }

                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) {  }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
 
         // clear memory
         main.obs_file_datum_tijd      = null;

         return null;
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) && (main.sensor_data_record_obs_pressure != null))
         {
            double hulp_double_pressure_reading;
            try
            {
               hulp_double_pressure_reading = Double.parseDouble(main.sensor_data_record_obs_pressure.trim());
               hulp_double_pressure_reading = Math.round(hulp_double_pressure_reading * 10) / 10.0d;  // bv 998.19 -> 998.2
            }
            catch (NumberFormatException e)
            {
               System.out.println("--- " + "Function RS232_Read_Sensor_Data_PPPP_For_Obs() " + e);
               hulp_double_pressure_reading = Double.MAX_VALUE;
            }   
             
            if ((hulp_double_pressure_reading > 900.0) && (hulp_double_pressure_reading < 1100.0))
            {
               if (main.tray_icon_clicked == true)
               {   
                  String pressure_sensor_height = "";
                  
                  // NB pressure at sensor height = pressure reading + ic
                  double double_barometer_instrument_correction = Double.parseDouble(main.barometer_instrument_correction.trim());
                  if ((double_barometer_instrument_correction > -4.0) && (double_barometer_instrument_correction < 4.0))
                  {        
                     //pressure_sensor_height = Double.toString(hulp_double_pressure_reading + double_barometer_instrument_correction);
                     // 1 digit precision
                     pressure_sensor_height = Double.toString(Math.round((hulp_double_pressure_reading + double_barometer_instrument_correction) * 10d) / 10d);
                  }
                  else
                  {
                     //pressure_sensor_height = Double.toString(hulp_double_pressure_reading);
                     // 1 digit precision
                     pressure_sensor_height = Double.toString(Math.round(hulp_double_pressure_reading * 10d) / 10d);
                  }                     
                  
                  cal_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // geeft systeem datum tijd in UTC van dit moment
                  cal_system_date_time.add(Calendar.MINUTE, -1);                              // averaged the data is 1 minute old
                  String date_time = sdf8.format(cal_system_date_time.getTime());  
                  
                  String info = "";
                  info = date_time + " UTC " + "\n" +
                                    "\n" +
                                    "pressure at sensor height: " + pressure_sensor_height + " hPa" + "\n";
                  //info = date_time + " UTC " + main.newline +
                  //       main.newline +
                  //       "pressure at sensor height: " + mybarometer.pressure_reading + " hPa" + main.newline;

                  main.trayIcon.displayMessage(main.APPLICATION_NAME, info, MessageType.INFO);
               }
               else // barometer input screen opened
               {      
                  mybarometer.pressure_reading = Double.toString(hulp_double_pressure_reading);
                  
                  if (mybarometer.jTextField1 != null)
                  {
                     mybarometer.jTextField1.setText(mybarometer.pressure_reading); 
                     mybarometer.jTextField2.requestFocus();      // focus on "insert draft" textfield
                  }
               }    
            } // if ((hulp_double_pressure_reading > 900.0) && (hulp_double_pressure_reading < 1100.0))
         } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
         else // so (still) no sensor data available
         {
            if (main.tray_icon_clicked == true)
            {
               String info = "no sensor data available";
               main.trayIcon.displayMessage(main.APPLICATION_NAME, info, MessageType.INFO);
            } // if (main.tray_icon_clicked == true)
            else
            {
               // temporary message box (barometer data obsolete) 
               final JOptionPane pane_end = new JOptionPane("automatically retrieved barometer reading obsolete", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
               final JDialog checking_ports_end_dialog = pane_end.createDialog(main.APPLICATION_NAME);

               Timer timer_end = new Timer(2500, new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent e)
                  {
                     checking_ports_end_dialog.dispose();
                  }
               });
               timer_end.setRepeats(false);
               timer_end.start();
               checking_ports_end_dialog.setVisible(true);               
            }            
         } // else so (still) no sensor data available
      } // protected void done()

   }.execute(); // new SwingWorker <Void, Void>()

}

 
   
/***********************************************************************************************/
/*                                                                                             */
/*                       RS232_Mintaka_Duo_Read_Sensor_Data_PPPP_For_Obs                       */
/*                                                                                             */
/***********************************************************************************************/
public static void RS232_Mintaka_Duo_Read_Sensor_Data_PPPP_For_Obs(boolean local_tray_icon_clicked)
{
   // called from: - initSynopparameters() [mybarometer.java]
   //              - main_windowIconfied() [main.java]
   //
   
   //System.out.println("+++ Mintaka Duo test"); 
   
   // initialisation
   mybarometer.pressure_reading = "";
   
   // initialisation
   main.tray_icon_clicked = local_tray_icon_clicked;
   

   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record         = null;
         String laatste_record = null;

         // initialisation
         main.sensor_data_record_obs_pressure = "";

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // e.g. 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;
                  
                  
                  // retrieve the last record in the sensor data file
                  //
                  while ((record = in.readLine()) != null)
                  {
                     laatste_record = record;
                  } // while ((record = in.readLine()) != null)
                  
                  // check on minimum record length
                  //
                  if (laatste_record != null)
                  {
                     if (!(laatste_record.length() > 15))         // NB > 15 is a little bit arbitrary number (YYYYMMDDHHmm + 3 commas + at leat 2 char pressure value= 15 chars)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Duo, format (min. record length) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                     }
                  }
                  
                  // check on correct number of commas in the laatste_record
                  //
                  if (laatste_record != null)
                  {
                     int number_read_commas = 0;
                     int pos = 0;
                     
                     do
                     {
                        pos = laatste_record.indexOf(",", pos + 1);
                        if (pos > 0)     // "," found
                        {
                           number_read_commas++;
                           //System.out.println("+++ number_read_commas = " + number_read_commas);
                        }
                     } while (pos > 0); 
                        
                     if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Duo, format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                     }                                  
                  } // if (laatste_record != null)
                  
                  
                  // last retrieved record ok
                  //
                  if (laatste_record != null)
                  {
                     //System.out.println("+++ Mintaka Duo, last retrieved record = " + laatste_record);
                     
                     //String record_datum_tijd_minuten = laatste_record.substring(main.type_record_datum_tijd_begin_pos, main.type_record_datum_tijd_begin_pos + 12);  // bv 201302201345
                     
                     int pos = laatste_record.length() -12;                                               // pos is now start position of YYYYMMDDHHmm
                     String record_datum_tijd_minuten = laatste_record.substring(pos, pos + 12);  // YYYYMMDDHHmm has length 12
                     
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     ///System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min

                     if (timeDiff <= 3L)      // max 3 minutes old
                     {
                        // retrieved record example Mintaka Duo: 1021.89,1021.89,1.70,1*05 201502111405
                        int pos1 = laatste_record.indexOf(",", 0);                                           // position of the first "," in the record
                        main.sensor_data_record_obs_pressure = laatste_record.substring(0, pos1);
                        
                        // rounding eg: 998.19 -> 998.2
                        //        double digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim()) + HOOGTE_CORRECTIE;
                        //        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;  // bv 998.19 -> 998.2
                        
                        if (main.tray_icon_clicked == true)
                        {   
                           System.out.println("--- sensor data record, pressure for tray icon: " + main.sensor_data_record_obs_pressure);
                        }
                        else
                        {
                           System.out.println("--- sensor data record, pressure for barometer form: " + main.sensor_data_record_obs_pressure);
                        }   
                     } // if (timeDiff <= 3L)
                     else
                     {
                        main.sensor_data_record_obs_pressure = "";
                        System.out.println("--- automatically retrieved barometer reading obsolete");
                     }

                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) {  }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
 
         // clear memory
         main.obs_file_datum_tijd      = null;

         return null;
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) && (main.sensor_data_record_obs_pressure != null))
         {
            double hulp_double_pressure_reading;
            try
            {
               hulp_double_pressure_reading = Double.parseDouble(main.sensor_data_record_obs_pressure.trim());
               hulp_double_pressure_reading = Math.round(hulp_double_pressure_reading * 10) / 10.0d;  // bv 998.19 -> 998.2
            }
            catch (NumberFormatException e)
            {
               System.out.println("--- " + "Function RS232_MintakaDuo_Read_Sensor_Data_PPPP_For_Obs() " + e);
               hulp_double_pressure_reading = Double.MAX_VALUE;
            }   
             
            if ((hulp_double_pressure_reading > 900.0) && (hulp_double_pressure_reading < 1100.0))
            {
               if (main.tray_icon_clicked == true)
               {   
                  String pressure_sensor_height = "";
                  
                  // NB pressure at sensor height = pressure reading + ic
                  double double_barometer_instrument_correction = Double.parseDouble(main.barometer_instrument_correction.trim());
                  if ((double_barometer_instrument_correction > -4.0) && (double_barometer_instrument_correction < 4.0))
                  {        
                     //pressure_sensor_height = Double.toString(hulp_double_pressure_reading + double_barometer_instrument_correction);
                     // 1 digit precision
                     pressure_sensor_height = Double.toString(Math.round((hulp_double_pressure_reading + double_barometer_instrument_correction) * 10d) / 10d);
                  }
                  else
                  {
                     //pressure_sensor_height = Double.toString(hulp_double_pressure_reading);
                     // 1 digit preciion
                     pressure_sensor_height = Double.toString(Math.round(hulp_double_pressure_reading * 10d) / 10d);
                  }                  
                  
                  cal_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // geeft systeem datum tijd in UTC van dit moment
                  cal_system_date_time.add(Calendar.MINUTE, -1);                              // averaged the data is 1 minute old
                  String date_time = sdf8.format(cal_system_date_time.getTime());  
                  
                  String info = "";
                  info = date_time + " UTC " + "\n" +
                                    "\n" +
                                    "pressure at sensor height: " + pressure_sensor_height + " hPa" + "\n";
                  //info = date_time + " UTC " + main.newline +
                  //       main.newline +
                  //       "pressure at sensor height: " + mybarometer.pressure_reading + " hPa" + main.newline;

                  main.trayIcon.displayMessage(main.APPLICATION_NAME, info, MessageType.INFO);
               }
               else // barometer input screen opened
               {        
                  mybarometer.pressure_reading = Double.toString(hulp_double_pressure_reading);
                  
                  // savety check barometer input screen is opened
                  if (mybarometer.jTextField1 != null)
                  {
                     mybarometer.jTextField1.setText(mybarometer.pressure_reading); 
                     mybarometer.jTextField2.requestFocus();      // focus on "insert draft" textfield
                  }
               }    
            } // if ((hulp_double_pressure_reading > 900.0) && (hulp_double_pressure_reading < 1100.0))
         } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
         else // so (still) no sensor data available
         {
            if (main.tray_icon_clicked == true)
            {
               String info = "no sensor data available";
               main.trayIcon.displayMessage(main.APPLICATION_NAME, info, MessageType.INFO);
            } // if (main.tray_icon_clicked == true)
            else
            {
               // temporary message box (barometer data obsolete) 
               final JOptionPane pane_end = new JOptionPane("automatically retrieved barometer reading obsolete", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
               final JDialog checking_ports_end_dialog = pane_end.createDialog(main.APPLICATION_NAME);

               Timer timer_end = new Timer(2500, new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent e)
                  {
                     checking_ports_end_dialog.dispose();
                  }
               });
               timer_end.setRepeats(false);
               timer_end.start();
               checking_ports_end_dialog.setVisible(true);               
            }
         } // else so (still) no sensor data available
      } // protected void done()

   }.execute(); // new SwingWorker <Void, Void>()

}

 

/***********************************************************************************************/
/*                                                                                             */
/*                       RS232_Read_Sensor_Data_a_ppp_Data_Files_For_Obs                       */
/*                                                                                             */
/***********************************************************************************************/
public static void RS232_Read_Sensor_Data_a_ppp_For_Obs()
{
   // Vaisala PTB220 or PTB330 (NOT AWS and NOT Mintaka Duo)
   
   
   if (main.RS232_connection_mode == 1)       // PTB220
   {
      main.type_record_lengte                 = main.RECORD_LENGTE_PTB220;
      main.type_record_datum_tijd_begin_pos   = main.RECORD_DATUM_TIJD_BEGIN_POS_PTB220;
      //type_record_minuten_begin_pos      = RECORD_MINUTEN_BEGIN_POS_PTB220;
      //type_record_pressure_begin_pos     = RECORD_P_BEGIN_POS_PTB220;
      main.type_record_a_begin_pos            = main.RECORD_a_BEGIN_POS_PTB220;
      main.type_record_ppp_begin_pos          = main.RECORD_ppp_BEGIN_POS_PTB220;
   }
   else if (main.RS232_connection_mode == 2)  // PTB330   
   {
      main.type_record_lengte                 = main.RECORD_LENGTE_PTB330;
      main.type_record_datum_tijd_begin_pos   = main.RECORD_DATUM_TIJD_BEGIN_POS_PTB330;
      //type_record_minuten_begin_pos      = RECORD_MINUTEN_BEGIN_POS_PTB330;
      //type_record_pressure_begin_pos     = RECORD_P_BEGIN_POS_PTB330;
      main.type_record_a_begin_pos            = main.RECORD_a_BEGIN_POS_PTB330;
      main.type_record_ppp_begin_pos          = main.RECORD_ppp_BEGIN_POS_PTB330;
   }
   
   // initialisation
   mybarograph.pressure_amount_tendency = "";
   mybarograph.a_code                   = "";
   

   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record         = null;
         String laatste_record = null;

         // initialisation
         main.sensor_data_record_obs_ppp = "";
         main.sensor_data_record_obs_a   = "";

         // determine file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // geeft bv 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;

                  while ((record = in.readLine()) != null)
                  {
                     if (record.length() == main.type_record_lengte)
                     {
                        laatste_record = record;
                     } 
                  } // while ((record = in.readLine()) != null)

                  if (laatste_record != null)
                  {
                     String record_datum_tijd_minuten = laatste_record.substring(main.type_record_datum_tijd_begin_pos, main.type_record_datum_tijd_begin_pos + 12);  // bv 201302201345
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     ///System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min

                     //String record_pressure_present = null;
                     if (timeDiff <= 3L)      // max 3 minutes old
                     {
                        main.sensor_data_record_obs_ppp = laatste_record.substring(main.type_record_ppp_begin_pos, main.type_record_ppp_begin_pos + 5);
                        main.sensor_data_record_obs_a   = laatste_record.substring(main.type_record_a_begin_pos, main.type_record_a_begin_pos + 1);
                 
                        // rounding eg: 998.19 -> 998.2
                        //        double digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim()) + HOOGTE_CORRECTIE;
                        //        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;  // bv 998.19 -> 998.2
                        
                        System.out.println("--- sensor data record, tendency for obs: " + main.sensor_data_record_obs_ppp);
                        System.out.println("--- sensor data record, characteristic for obs: " + main.sensor_data_record_obs_a);
                     }
                     else
                     {
                        //main.sensor_data_record_obs_pressure = "";       // t.m versie 2.3.3. dit ten onrechte
                        main.sensor_data_record_obs_ppp = "";
                        main.sensor_data_record_obs_a = "";
                     }

                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) {  }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
 
         // clear memory
         main.obs_file_datum_tijd      = null;
         //hulp_obs_file_datum_tijd = null;

         return null;
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         int hulp_pressure_change = 3;                // -1 negative; 1 positive; 0 same pressure; 3 is only start value
         //Determine_Pressure_Characteristic();
         
         // ppp
         //
         if ( (main.sensor_data_record_obs_ppp.compareTo("") != 0) && (main.sensor_data_record_obs_ppp != null) && (main.sensor_data_record_obs_ppp.indexOf("*") == -1) )
         {
            double hulp_double_ppp_reading;
            try
            {
               hulp_double_ppp_reading = Double.parseDouble(main.sensor_data_record_obs_ppp.trim());
               hulp_double_ppp_reading = Math.round(hulp_double_ppp_reading * 10) / 10.0d;  // bv 13.19 -> 13.2
            }
            catch (NumberFormatException e)
            {
               System.out.println("--- " + "Function RS232_Read_Sensor_Data_a_ppp_For_Obs() " + e);
               hulp_double_ppp_reading = Double.MAX_VALUE;
            }   
             
            if ((hulp_double_ppp_reading > -99.9) && (hulp_double_ppp_reading < 99.9))
            {
               // NB hulp_pressure_change: for 'future' use to determine a (pressure characteristic)
               if (hulp_double_ppp_reading > 0.0)
               {
                  hulp_pressure_change = 1; 
               }
               else if (hulp_double_ppp_reading < 0.0)
               {
                  hulp_pressure_change = -1;
               }
               else if (hulp_double_ppp_reading == 0.0)
               {
                  hulp_pressure_change = 0;
               }
        
               // tendency
               mybarograph.pressure_amount_tendency = Double.toString(Math.abs(hulp_double_ppp_reading));  // only pos value in this field
               mybarograph.jTextField1.setText(mybarograph.pressure_amount_tendency); 
            }
         } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
         
         
         // a
         //
         if ((main.sensor_data_record_obs_a.compareTo("") != 0) && (main.sensor_data_record_obs_a != null) && (main.sensor_data_record_obs_a.indexOf("*") == -1) )
         {
            int hulp_int_a_reading;
            
            try
            {
               hulp_int_a_reading = Integer.parseInt(main.sensor_data_record_obs_a.trim());
            }
            catch (NumberFormatException e)
            {
               System.out.println("--- " + "Function RS232_Read_Sensor_Data_a_ppp_For_Obs() " + e);
               hulp_int_a_reading = Integer.MAX_VALUE;
            }   
             
            if ((hulp_int_a_reading >= 0) && (hulp_int_a_reading <= 8))
            {
               // initialisation
               mybarograph.jRadioButton1.setSelected(false);
               mybarograph.jRadioButton2.setSelected(false);
               mybarograph.jRadioButton3.setSelected(false);
               mybarograph.jRadioButton4.setSelected(false);
               mybarograph.jRadioButton5.setSelected(false);
               mybarograph.jRadioButton6.setSelected(false);
               mybarograph.jRadioButton7.setSelected(false);
               mybarograph.jRadioButton8.setSelected(false);
               mybarograph.jRadioButton9.setSelected(false);
               mybarograph.jRadioButton10.setSelected(false);
               mybarograph.jRadioButton11.setSelected(false);
               mybarograph.jRadioButton12.setSelected(false);
               
               // pressure higher than 3hrs ago
               if (hulp_pressure_change == 1)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_0))
                     mybarograph.jRadioButton1.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_1))
                     mybarograph.jRadioButton2.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_2))
                     mybarograph.jRadioButton3.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_3))
                     mybarograph.jRadioButton4.setSelected(true);
               }
               
               // pressure lower than 3hrs ago
               else if (hulp_pressure_change == -1)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_5))
                     mybarograph.jRadioButton5.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_6))
                     mybarograph.jRadioButton6.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_7))
                     mybarograph.jRadioButton7.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_8))
                     mybarograph.jRadioButton8.setSelected(true);
               }
            
               // pressure the same as 3hrs ago
               else if (hulp_pressure_change == 0)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_0_SAME))
                     mybarograph.jRadioButton9.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_4))                                
                     mybarograph.jRadioButton10.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_5_SAME))                                
                     mybarograph.jRadioButton11.setSelected(true);              
               }    
               else
               {
                  mybarograph.jRadioButton12.setSelected(true);   // not determined
               }
               
            } // if ((hulp_int_a_reading >= 0) && (hulp_int_a_reading <= 8))
         } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
      } // protected void done()

   }.execute(); // new SwingWorker <Void, Void>()

}


/***********************************************************************************************/
/*                                                                                             */
/*               RS232_Mintaka_Duo_Read_Sensor_Data_a_ppp_Data_Files_For_Obs                   */
/*                                                                                             */
/***********************************************************************************************/
public static void RS232_Mintaka_Duo_Read_Sensor_Data_a_ppp_Data_Files_For_Obs()        
{
   // called from: - initSynopparameters() [mybarograph.java]
   
   
   // initialisation
   mybarograph.pressure_amount_tendency = "";
   mybarograph.a_code                   = "";
   

   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record         = null;
         String laatste_record = null;

         // initialisation
         //main.sensor_data_record_obs_pressure = "";

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // e.g. 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;

                  // retrieve the last record in the sensor data file
                  //
                  while ((record = in.readLine()) != null)
                  {
                     laatste_record = record;
                  } // while ((record = in.readLine()) != null)
                  
                  // check on minimum record length
                  //
                  if (laatste_record != null)
                  {
                     if (!(laatste_record.length() > 15))         // NB > 15 is a little bit arbitrary number (YYYYMMDDHHmm + 3 commas + at leat 2 char pressure value= 15 chars)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Duo, format (min. record length) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                     }
                  }
                  
                  // check on correct number of commas in the laatste_record
                  //
                  if (laatste_record != null)
                  {
                     int number_read_commas = 0;
                     int pos = 0;
                     
                     do
                     {
                        pos = laatste_record.indexOf(",", pos + 1);
                        if (pos > 0)     // "," found
                        {
                           number_read_commas++;
                           //System.out.println("+++ number_read_commas = " + number_read_commas);
                        }
                     } while (pos > 0); 
                        
                     if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Duo, format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                     }                                  
                  } // if (laatste_record != null)
                  
                  
                  // last retrieved record ok
                  if (laatste_record != null)
                  {
                     int pos = laatste_record.length() -12;                                               // pos is now start position of YYYYMMDDHHmm
                     String record_datum_tijd_minuten = laatste_record.substring(pos, pos + 12);          // YYYYMMDDHHmm has length 12
                     
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     ///System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min

                     if (timeDiff <= 3L)      // max 3 minutes old
                     {
                        // retrieved record example Mintaka Duo: 1021.89,1021.89,1.70,1*05 201502041505
                        int pos1 = laatste_record.indexOf(",", 0);                                              // position of the first "," in the last record
                        int pos2 = laatste_record.indexOf(",", pos1 +1);                                        // position of the second "," in the last record
                        int pos3 = laatste_record.indexOf(",", pos2 +1);                                        // position of the third "," in the last record
                        
                        main.sensor_data_record_obs_ppp = laatste_record.substring(pos2 +1, pos3);
                        main.sensor_data_record_obs_a = laatste_record.substring(pos3 +1, pos3 + 2);             // a code is always 1 char (could be *, but this will be corrected, see next code lines)
                        
                        // rounding eg: 998.19 -> 998.2
                        //        double digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim()) + HOOGTE_CORRECTIE;
                        //        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;  // bv 998.19 -> 998.2
                        
                        System.out.println("--- sensor data record, tendency for obs: " + main.sensor_data_record_obs_ppp);
                        
                        // first char after third comma could be * (always 1 char after last comma will be read) if no "a" present -> correct this
                        if (main.sensor_data_record_obs_ppp.indexOf("*") == 1)
                        {
                           main.sensor_data_record_obs_a = "";
                        }
                        
                        System.out.println("--- sensor data record, characteristic for obs: " + main.sensor_data_record_obs_a);
                        
                     } // if (timeDiff <= 3L)
                     else
                     {
                        main.sensor_data_record_obs_ppp = "";
                        main.sensor_data_record_obs_a = "";
                     }

                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) {  }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
 
         // clear memory
         main.obs_file_datum_tijd      = null;

         return null;
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         int hulp_pressure_change = 3;                // -1 negative; 1 positive; 0 same pressure; 3 is only start value
         
         // ppp
         //
         if ( (main.sensor_data_record_obs_ppp.compareTo("") != 0) && (main.sensor_data_record_obs_ppp != null) && (main.sensor_data_record_obs_ppp.indexOf("*") == -1) )
         {
            double hulp_double_ppp_reading;
            try
            {
               hulp_double_ppp_reading = Double.parseDouble(main.sensor_data_record_obs_ppp.trim());
               hulp_double_ppp_reading = Math.round(hulp_double_ppp_reading * 10) / 10.0d;  // e.g. 13.19 -> 13.2
               
               //System.out.println("+++ hulp_double_ppp_reading = " + hulp_double_ppp_reading);
               
            }
            catch (NumberFormatException e)
            {
               System.out.println("--- " + "Function RS232_Mintaka_Duo_Read_Sensor_Data_a_ppp_For_Obs() " + e);
               hulp_double_ppp_reading = Double.MAX_VALUE;
            }   
             
            if ((hulp_double_ppp_reading > -99.9) && (hulp_double_ppp_reading < 99.9))
            {
               // NB hulp_pressure_change: a help to determine a (pressure characteristic)
               if (hulp_double_ppp_reading > 0.0)
               {
                  hulp_pressure_change = 1; 
               }
               else if (hulp_double_ppp_reading < 0.0)
               {
                  hulp_pressure_change = -1;
               }
               else if (hulp_double_ppp_reading == 0.0)
               {
                  hulp_pressure_change = 0;
               }
               
               //System.out.println("+++ hulp_pressure_change = " + hulp_pressure_change);
        
               // tendency
               mybarograph.pressure_amount_tendency = Double.toString(Math.abs(hulp_double_ppp_reading));  // only positive value in this field
               mybarograph.jTextField1.setText(mybarograph.pressure_amount_tendency); 
            }
         } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
         
         
         // a
         //
         if ((main.sensor_data_record_obs_a.compareTo("") != 0) && (main.sensor_data_record_obs_a != null) && (main.sensor_data_record_obs_a.indexOf("*") == -1) )
         {
            int hulp_int_a_reading;
            
            try
            {
               hulp_int_a_reading = Integer.parseInt(main.sensor_data_record_obs_a.trim());
            }
            catch (NumberFormatException e)
            {
               System.out.println("--- " + "Function RS232_Mintaka_Duo_Read_Sensor_Data_a_ppp_For_Obs() " + e);
               hulp_int_a_reading = Integer.MAX_VALUE;
            }   
             
            if ((hulp_int_a_reading >= 0) && (hulp_int_a_reading <= 8))
            {
               // initialisation
               mybarograph.jRadioButton1.setSelected(false);
               mybarograph.jRadioButton2.setSelected(false);
               mybarograph.jRadioButton3.setSelected(false);
               mybarograph.jRadioButton4.setSelected(false);
               mybarograph.jRadioButton5.setSelected(false);
               mybarograph.jRadioButton6.setSelected(false);
               mybarograph.jRadioButton7.setSelected(false);
               mybarograph.jRadioButton8.setSelected(false);
               mybarograph.jRadioButton9.setSelected(false);
               mybarograph.jRadioButton10.setSelected(false);
               mybarograph.jRadioButton11.setSelected(false);
               mybarograph.jRadioButton12.setSelected(false);
               
               // pressure higher than 3hrs ago
               if (hulp_pressure_change == 1)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_0))
                     mybarograph.jRadioButton1.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_1))
                     mybarograph.jRadioButton2.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_2))
                     mybarograph.jRadioButton3.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_3))
                     mybarograph.jRadioButton4.setSelected(true);
               }
               
               // pressure lower than 3hrs ago
               else if (hulp_pressure_change == -1)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_5))
                     mybarograph.jRadioButton5.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_6))
                     mybarograph.jRadioButton6.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_7))
                     mybarograph.jRadioButton7.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_8))
                     mybarograph.jRadioButton8.setSelected(true);
               }
            
               // pressure the same as 3hrs ago
               else if (hulp_pressure_change == 0)
               {
                  if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_0_SAME))
                     mybarograph.jRadioButton9.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_4))                                
                     mybarograph.jRadioButton10.setSelected(true);
                  else if (main.sensor_data_record_obs_a.trim().equals(mybarograph.A_5_SAME))                                
                     mybarograph.jRadioButton11.setSelected(true);              
               }    
               else
               {
                  mybarograph.jRadioButton12.setSelected(true);   // not determined
               }
               
            } // if ((hulp_int_a_reading >= 0) && (hulp_int_a_reading <= 8))
         } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
         
      } // protected void done()

   }.execute(); // new SwingWorker <Void, Void>()

}




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS422_init_new_aws_data_received_check_timer()
{
   /////// NB AWS only ////////
   //
   // called from: - RS422_initComponents() [main_RS232_RS422.java]
   //              - Ethernet_initComponents() [main_RS232_RS422.java]
   
   ActionListener check_new_data_action = new ActionListener()
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
			//System.out.println("+++ System.currentTimeMillis() = " + System.currentTimeMillis());
			//System.out.println("+++ last_new_data_received_TimeMillis = " + last_new_data_received_TimeMillis);
			//System.out.println("+++ System.currentTimeMillis() - last_new_data_received_TimeMillis = " + (System.currentTimeMillis() - last_new_data_received_TimeMillis));
			
			
         // NB currentTimeMillis(): returns the difference,in milliseconds, between the current system time and midnight, January 1, 1970 UTC
         // NB last_new_data_received_TimeMillis: returns the difference,in milliseconds between the last aws data receipt system time and midnight, January 1, 1970 UTC.
         if ((System.currentTimeMillis() - last_new_data_received_TimeMillis) > MAX_AGE_AWS_DATA)     // MAX_AGE_AWS_DATA e.g. 30000 msec (5 minutes)
         {
            // NB see function: RS422_initComponents() [main_RS232_RS422.java] for setting of var "last_new_data_received_TimeMillis"
            
            // gray meteo parameters data on main screen
				
				
				//System.out.println("+++ System.currentTimeMillis() - last_new_data_received_TimeMillis) > MAX_AGE_AWS_DATA");
            
            
            // TEST
            //jTextField3.setForeground(main.input_color_from_aws);
            
            // ONDERSTAANDE FUNCTIES AANPASSEN VOOT GRAY
            // display aws data values on main screen
            main.displayed_aws_data_obsolate = true;                              // for DASHBOARD
            
            main.date_time_fields_update();
            main.position_fields_update();
            main.barometer_fields_update();   
            main.barograph_fields_update();
            main.temperatures_fields_update();
            main.wind_fields_update();
            
            // OOK INDIVIDUELE meteo parameter (bv wind) INVUL SCHERMEN GRAY MAKEN ??????
            
              
            // VOT (display on on main screen)
            //main.jTextField4.setForeground(Color.GRAY);
            main.jTextField4.setForeground(main.obsolate_color_data_from_aws);
            main.jTextField4.setText("AWS data obsolete");
            main.jMenuItem46.setEnabled(false);                  // Obs to AWS
            
            // NIET VERGETEN WEER AANTE ZETTEN ALS DATA WEER WORDT ONTVANGEN
            // OF GAAT AUTOMATISCH WEER GOED? (via VOT check)
            // see: function: RS422_Read_AWS_Sensor_Data_For_Display()
            // main.jMenuItem46.setEnabled(true);                  // Obs to AWS
            
            // MOET DEZE ACTIONLISTNER EXPLICIET GESTOPT WORDEN ALS EEN EXIT DOOR GEBRUIKER WORDT GEGEVEN ????????? (check in console op vreemde foutmeldingen na afsluiten TurboWin+)
            
            
            // MOET DEZE ACTIONLISTNER EXPLICIET GESTOPT WORDEN ALS EEN EXIT DOOR GEBRUIKER WORDT GEGEVEN ????????? (check in console op vreemde foutmeldingen na afsluiten TurboWin+)
          
            // message obsolete data only the first time
            if (main.obsolate_data_flag == false)
            {
               String message = "[AWS] sensor data obsolete (lost connection?)";
               main.log_turbowin_system_message(message);
            } // if (main.obsolate_data_flag == false)
            else // data obsolete but after > 5 minutes
            {
               
               if (main.RS232_connection_mode == 10)   // OMC-140 ethernet
               {
                  // do nothing port 0.0.0.0:60004 will alway stay open as long as TurboWin+/Web is running (Windows power shell command "netstat -aon")
/*                  
                  // NB Ethernet listener restart every hour
                  cal_Ethernet_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));   // system date time in UTC of this moment
                  cal_Ethernet_system_date_time.getTime();                                               // now effective
                  int Ethernet_system_minute_local = cal_Ethernet_system_date_time.get(Calendar.MINUTE);
                  
                  // cancel Ethernet listener thread
                  if (Ethernet_system_minute_local == CANCEL_AWS_THREAD)           
                  {
                     try
                     {
                        // NB to prevent after every restart of the thread an extra UDP port will be opened 0.0.0.0:60004 (Windows power shell command "netstat -aon")
                        //    and after 6 - 10 times opening a new 60004 UDP port there will be no comm .at all
                        socket_udp.leaveGroup(group_udp);
                     }
                     catch (IOException ex)
                     {
                        // do nothing
                     }
                     socket_udp.close();
                     Ethernet_worker.cancel(true);
                     //Ethernet_worker = null;     // NB null do not prevent a Bindexception,; 
                     // NB2 a Bindexception is not an issue because it means TurboWin+ is still bind to (UDP)Ethernet, 
                     //     it would be an issue if it was bind to onother process/application
                     //
                     // NB3 with ip multicast it seems adifferent, cannot bind will not occure but every time a new UDP port will be opened
                     //     and after -8 times there is no more communication at all
                     //      therefor the socket_udp.leaveGroup(group_udp) and socket_udp.close()
                     //
                     String message = "[AWS] sensor data listener thread cancelling";
                     main.log_turbowin_system_message(message);
                  } 
                  
                  // restart Ethernet listener thread
                  if (Ethernet_system_minute_local == START_AWS_THREAD)            
                  {
                     Ethernet_worker = new Ethernet_Class_Receive_UDP();
                     Ethernet_worker.execute();
                     String message = "[AWS] sensor data listener thread restarting";
                     main.log_turbowin_system_message(message);
                  } // if (Ethernet_system_minute_local == START_AWS_THREAD)  
*/                  
               } //  if (main.RS232_connection_mode == 10) 
               else if ((main.RS232_connection_mode == 3) || (main.RS232_connection_mode == 9)) // EUCAWS serial or OMC-140 serial (but no OMC-140 ethernet)
               {
               
                  // NB RS422 listener restart every hour
                  cal_RS422_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));   // system date time in UTC of this moment
                  cal_RS422_system_date_time.getTime();                                                // now effective
                  int RS422_system_minute_local = cal_RS422_system_date_time.get(Calendar.MINUTE);
                  
                  // cancel RS422 listener thread
                  if (RS422_system_minute_local == CANCEL_AWS_THREAD)           
                  {
                     RS422_worker.cancel(true);
                     String message = "[AWS] sensor data listener thread cancelling";
                     main.log_turbowin_system_message(message);
                  } 
                  
                  // restart RS422 listener thread
                  if (RS422_system_minute_local == START_AWS_THREAD) 
                  {
                     // NB in case of power failure the com port of connected AWS is still ok (still open) but listener must be restarted
                     //    so we cannot use Function: RS422_Check_Serial_Ports() because then the present default port, which is ok, will be reported as occupied!
                     //    so first Function: RS422_Check_Default_Serial_Port(), see below
                     RS422_Check_Default_Serial_Port(); // in this function:: main.defaultPort will be set to null if present default comm port was not valid anymore
                     
                     if (main.defaultPort == null)  
                     {
                        // so default port not valid anymore (most likely there was no power break) now first checking/looking for the correct com port
                        // checking/looking for the correct com port
                        int completed_checks_serial_ports = 0;
                        main.defaultPort = null;                // reset !
                        while ((main.defaultPort == null) && (completed_checks_serial_ports < MAX_COMPLETED_AWS_PORT_CHECKS))
                        {
                           RS422_Check_Serial_Ports(completed_checks_serial_ports);
                           completed_checks_serial_ports++;
                        }
                  
                        if (main.defaultPort == null)
                        {
                           String message = "[AWS] no AWS found (defaultPort = null); listener thread not restarted";
                           main.log_turbowin_system_message(message);
                        }
                     } // if (main.defaultPort == null)
               
                      if (main.defaultPort != null)                     
                     {
                        main.serialPort  = SerialPort.getCommPort(main.defaultPort); 
                     
                        // restart RS422 listener thread
                        RS422_worker = new RS422_V3_Class_Receive_Sensor_Data();//RS422_worker = new RS422_Class_Receive_Sensor_Data();
                        RS422_worker.execute();
                        String message = "[AWS] sensor data listener thread restarting";
                        main.log_turbowin_system_message(message);
                     } // if (main.defaultPort != null) 
                  
                  } // if (RS422_system_minute_local == START_AWS_THREAD) 
                  
               } // else if ((main.RS232_connection_mode == 3) || (main.RS232_connection_mode == 9))
            } // data obsolete but after > 5 minutes
            
            main.obsolate_data_flag = true;            
            
         } // if ((System.currentTimeMillis() - last_new_data_received_TimeMillis) > MAX_AGE_AWS_DATA)
         else // so aws data NOT obsolate (data < 5 minutes old)
         {
            main.displayed_aws_data_obsolate = false;                          // for DASHBOARD
            
            // NB in this case: in function: RS422_Read_AWS_Sensor_Data_For_Display() the fields on the main screen will be updated
            // NB main.date_time_fields_update() etc. and VOT will be updated there
            
            // reset message if before was written data obsolate
            // NB probably the message below will never be written because disconnected serial connection must be reconnected by a program restart (contraru to WiFi connection)
            if (main.obsolate_data_flag)
            {
               String message = "[AWS] start receiving sensor data again";
               main.log_turbowin_system_message(message);
            } // if (main.obsolate_data_flag)
            
            main.obsolate_data_flag = false;
            
            // to depress "AWS data obsolete" on status line in the case of a connected AWS without VOT parameter (eg OMC-140)
            if (main.RS232_connection_mode == 9 || main.RS232_connection_mode == 10  && (main.AWSR == false))  // OMC-140 serial or internet but not if AWSR because then next upload is set to blank every time
            {
               main.jTextField4.setText("");
            }
         } //else
         
         // initialisation
         //last_new_data_received_TimeMillis = 0;
   
      } // public void actionPerformed(ActionEvent e)
   };
   
   /* main loop for checking when new AWS data arrives */
   check_new_data_timer_delay = DELAY_NEW_DATA_CHECK_LOOP;                              // delay between two new aws data checks (e.g. 1 minute)  
   check_new_data_timer = new Timer(check_new_data_timer_delay, check_new_data_action);
   check_new_data_timer.setRepeats(true);                                               // false = only one action
   check_new_data_timer.setInitialDelay(INITIAL_DELAY_NEW_DATA_CHECK_LOOP);             // time in millisec to wait after timer is started to fire first event
   check_new_data_timer.setCoalesce(true);                                              // to be sure
   check_new_data_timer.start();   
}        



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void RS422_initComponents()
{
   /////// NB EUCAWS serial or OMC-140 serial only ////////
   //
   // called from specific_connection_initComponents() [main.java]
   //
   
   
   
   // http://www.snip2code.com/Snippet/1044/Java--read-from-USB-using-RXTX-library
   // FOR LINUX:
   //#==================================================================#
   // WARNING! - DO NOT SET THE FOLLOWING PROPERTY WITH RXTX LIBRARY, IT
   // CAUSES A PROGRAM LOCK:
   // serialPort.notifyOnOutputEmpty(true);
   //#==================================================================#     
   
   
   
   // start timer for checking every 1 minute if there is new AWS meteo sensor data available (because if last data > 5 minutes old 'gray' data on main screen)
   //last_new_data_received_TimeMillis = 0;
   //RS422_init_new_aws_data_received_check_timer();
      
   // for dispaying VOT (Visual Observation Trigger) line in bottom text field on main screen
   color_dark_green = new Color(76, 153, 0);
   
   // for sensor data files (in the file name itself) (note also used in RS232_view.java)
   main.sdf3 = new SimpleDateFormat("yyyyMMddHH");                                  // HH hour in day (0-23) note there is also hh (then also with am, pm)
   main.sdf3.setTimeZone(TimeZone.getTimeZone("UTC"));
   
   // for date-time strings/remarks in the sensor data records (note also used in RS232_view.java)
   main.sdf4 = new SimpleDateFormat("yyyyMMddHHmm");                                // HH hour in day (0-23) note there is also hh (then also with am, pm)
   main.sdf4.setTimeZone(TimeZone.getTimeZone("UTC"));
    
   // for function: RS422_write_sensor_data_to_file() and RS422_V3_write_sensor_data_to_file()
   sdf7 = new SimpleDateFormat("ss");                                           
   sdf7.setTimeZone(TimeZone.getTimeZone("UTC"));
   
   
   // dir for storage sensor data
   //if (logs_dir.trim().equals("") == true || logs_dir.trim().length() < 2)
   if ((main.logs_dir == null) || (main.logs_dir.compareTo("") == 0))
   {
      String info = "logs folder (for sensor data storage) unknown, select: Maintenance -> Log files settings";
      JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
   }
      
   // checking/looking for the correct com port
   int completed_checks_serial_ports = 0;
   while ((main.defaultPort == null) && (completed_checks_serial_ports < MAX_COMPLETED_AWS_PORT_CHECKS))
   {
      RS422_Check_Serial_Ports(completed_checks_serial_ports);
      completed_checks_serial_ports++;
   }
   
   // delete old sensor data files
   main.log_turbowin_system_message("[GENERAL] deleting old (AWS) sensor data files");
   RS232_Delete_Sensor_Data_Files();                                        // also used by RS422 (AWS)
   

   if (main.defaultPort != null)
   {
      // info text on (bottom) main screen 
      main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode + ", last received AWS data via serial communication: no data"); // application mode was set in initComponents2() [main.java]
      
      main.serialPort  = SerialPort.getCommPort(main.defaultPort);
      
      RS422_worker = new RS422_V3_Class_Receive_Sensor_Data();//RS422_worker = new RS422_Class_Receive_Sensor_Data();
      RS422_worker.execute();
   
      String message = "[AWS] start listening";
      main.log_turbowin_system_message(message);
       
      // start timer for checking every 1 minute if there is new AWS sensor data available (because if last data > 5 minutes old 'gray' data on main screen)
      // NB invoking after: RS422_worker = new RS422_Class_Receive_Sensor_Data(); and RS422_worker.execute();
      last_new_data_received_TimeMillis = 0;
      RS422_init_new_aws_data_received_check_timer();
   
   } // if (main.defaultPort != null)
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private class RS422_Class_Receive_Sensor_Data extends SwingWorker<String, String>
{
   /////////////////// AWS only /////////////////
   //
   // called from: RS422_initComponents()
   
   //   new SwingWorker<String, String>()
   //   {
         @Override
         protected String doInBackground() throws Exception
         {
            main.serialPort.openPort(); 
            if (main.serialPort.isOpen())
            {
               //main.serialPort.openPort();               // NB will be closed in Function:  File_Exit_menu_actionPerformd() [main.java]
               //main.serialPort.setParams(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
               //main.serialPort.setFlowControlMode(main.flow_control);
               main.serialPort.setComPortParameters(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
               main.serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
             
               
               // Preparing a mask. In a mask, we need to specify the types of events that we want to track.
               // Well, for example, we need to know what came some data, thus in the mask must have the
               // following value: MASK_RXCHAR. If we, for example, still need to know about changes in states 
               // of lines CTS and DSR, the mask has to look like this: SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR
               //int mask = SerialPort.MASK_RXCHAR;
                  
               // Set the prepared mask
               //main.serialPort.setEventsMask(mask);                  
                  
		      } // if (serialPort.isOpen())
            else        
            {
               System.out.println("+++ " + "[AWS] Couldn't open " +  main.serialPort.getDescriptivePortName());
            }                       
        
            
            if (main.serialPort.isOpen())
            {
               main.serialPort.addDataListener(new SerialPortDataListener() 
               {
                  @Override
                  public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                  
                  @Override
                  public void serialEvent(SerialPortEvent event)
                  {
                     String ontvangen_SMD_string = "";
                     
                     if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                        return;
                     
                     //byte[] newData = new byte[main.serialPort.bytesAvailable()];
                     //int numRead = main.serialPort.readBytes(newData, newData.length);
                     int numRead;
                     byte[] newData = null;
                     try
                     {
                        newData = new byte[main.serialPort.bytesAvailable()];
                        numRead = main.serialPort.readBytes(newData, newData.length);                  
                     }
                     catch (NegativeArraySizeException ex) 
                     {
                        numRead = -1;
                        // NB don't modify the GUI anywhere except for on the event-dispatch-thread.!! (so not here in the function doinBackground())
                     }
                     
                     //ontvangen_SMD_string = main.serialPort.readString(event.getEventValue());
                     if (numRead > 0)
                     {
                        ontvangen_SMD_string = new String(newData, StandardCharsets.UTF_8);
                     
                        //System.out.println("Read " + numRead + " bytes.");
                     
                        // publish: Sends data chunks to the process(java.util.List) method. This method is to be used from inside the doInBackground method to deliver intermediate results for processing on the Event Dispatch Thread inside the process method.
                        //          Because the process method is invoked asynchronously on the Event Dispatch Thread  multiple invocations to the publish method might occur before the process method is executed. For performance purposes all these invocations are coalesced into one invocation with concatenated arguments.
                        //
                        // For example:
                        //
                        // publish("1");
                        // publish("2", "3");
                        // publish("4", "5", "6");
                        //
                        // might result in: process("1", "2", "3", "4", "5", "6")
                        //
                        publish(new String[] { ontvangen_SMD_string });
                     } // if (numRead > 0)
                     else if (numRead == -1)
                     {
                        publish(new String[] { "interruption or excecution error\n" });
                     }
                  } // public void serialEvent(SerialPortEvent event)
               });              
            } // if (serialPort.isOpen())
            
            return null;

         } // protected Void doInBackground() throws Exception

         @Override
         protected void process(List<String> data)
         {
            // process: Receives data chunks from the publish method asynchronously on the Event Dispatch Thread.
            for (String ontvangen_SMD_string: data)
            {
               // NB altijd in for loop omdat meerdere ontbvangen_SMD_string's verzameld kunnen zijn voordat het hier procesed wordt(inherent aan SwingWorker)
               //System.out.println("ontvangen_SMD_string = " + ontvangen_SMD_string);   // bv martin

               main.total_string = ontvangen_SMD_string;

               // NB Onderstaande om i.p.v. elke minuut de data per 5 minuten op te slaan
               //    werkt hier niet (door de 'brokken' record maar bij AWS zou het wel een mogelijkheid kunnen zijn
               //if (total_string.indexOf("3$") != -1)
               //{
               //   String minuten = sdf4.format(new Date()).substring(10, 10 + 2);  // sdf4: yyyyMMddHHmm
               //   int int_minuten = Integer.parseInt(minuten.trim());
               //
               //   if (int_minuten % 5 == 0   );
               //
               System.out.print(main.total_string);
               //System.out.println(total_string); // NB zo zie je ruwweg hoeveel bytes dat telkens beschikbaar zijn

               // error logging
               if (main.total_string.indexOf("error") != -1)
               {
                  String message = "[AWS] " + "interruption or execution exception"; 
                  main.log_turbowin_system_message(message);
                  
                  //main.obsolate_data_flag = true;  // see also Function RS422_init_new_aws_data_received_check_timer() [main_RS232_RS422.java]                     
                  
               } //  if (main.total_string.indexOf("error") != -1)              
               
               
               // write sensor data to sensor log file
               RS422_write_sensor_data_to_file();
                  
               // only if the last piece of a record string was received update TurboWin+ display and check for presence parameters (if parameter present disable the manual input on TurboWin+ input form)
               if (main.total_string.indexOf("\n") != -1)
               {
                  // read the AWS data which was saved before by TurboWin+ from a sensor data file
                  RS422_Read_AWS_Sensor_Data_For_Display();  
                  
                  // update main screen with application name + mode and date time last received data (single info line botton screen)
                  String info = "";
                  if ((main_RS232_RS422.test_record.equals("")) || (main_RS232_RS422.test_record == null)) // test_rcord was set in RS422_write_sensor_data_to_file()
                  {
                     info = "no data";
                  } 
                  else 
                  {
                     info = main_RS232_RS422.test_record;
                  }     
                  main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received AWS data via serial communication: " + info);
                     
                  // for checking that the displayed data is not obsolete (i.e. not updated last 5 minutes)
                  last_new_data_received_TimeMillis = System.currentTimeMillis(); // see also Function: RS422_init_new_aws_data_received_check_timer() [main_RS232_422.java]
               } 
               // update the date and time on the main screen (and the global date time var's)
               // NB if during start up the date and time displayed in the pupup message was not comfired by the user
               //    it is assumed that the pc is not running on the correct time so then do not update the date time automatically
            } // for (String ontvangen_string : data)
         }
     // }.execute(); // new SwingWorker<String, String>()
   
}
*/

/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private class RS422_V2_Class_Receive_Sensor_Data extends SwingWorker<String, String>
{
   /////////////////// AWS only /////////////////
   //
   // called from: RS422_initComponents()
   
   //   new SwingWorker<String, String>()
   //   {
         @Override
         protected String doInBackground() throws Exception
         {
            main.serialPort.openPort(); 
            if (main.serialPort.isOpen())
            {
               main.serialPort.setComPortParameters(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
               main.serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
                  
		      } // if (serialPort.isOpen())
            else        
            {
               System.out.println("+++ " + "[AWS] Couldn't open " +  main.serialPort.getDescriptivePortName());
            }                       
        
            
            if (main.serialPort.isOpen())
            {
               main.serialPort.addDataListener(new SerialPortDataListener() 
               {
                  @Override
                  public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                  
                  StringBuilder message = new StringBuilder();
                  
                  @Override
                  public void serialEvent(SerialPortEvent event)
                  {
                     String ontvangen_SMD_string = "";
                     
                     if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                     {
                        return;
                     }
                     
                     int numRead;
                     byte[] newData = null;
                     try
                     {
                        newData = new byte[main.serialPort.bytesAvailable()];
                        numRead = main.serialPort.readBytes(newData, newData.length);                  
                     }
                     catch (NegativeArraySizeException ex) 
                     {
                        numRead = -1;
                        // NB don't modify the GUI anywhere except for on the event-dispatch-thread.!! (so not here in the function doinBackground())
                     }
                     
                     //ontvangen_SMD_string = main.serialPort.readString(event.getEventValue());
                     if (numRead > 0)
                     {
                        for (byte b: newData)
                        {   
                           if ((b == '\r' || b == '\n') && message.length() > 0) 
                           //if ((b == 13 || b == 10) && message.length() > 0)    // https://gist.github.com/GermanCoding/d250ca5012ec25ceffcc
                           {   
                              ontvangen_SMD_string = message.toString();
                     
                              // publish: Sends data chunks to the process(java.util.List) method. This method is to be used from inside the doInBackground method to deliver intermediate results for processing on the Event Dispatch Thread inside the process method.
                              //          Because the process method is invoked asynchronously on the Event Dispatch Thread  multiple invocations to the publish method might occur before the process method is executed. For performance purposes all these invocations are coalesced into one invocation with concatenated arguments.
                              //
                              // For example:
                              //
                              // publish("1");
                              // publish("2", "3");
                              // publish("4", "5", "6");
                              //
                              // might result in: process("1", "2", "3", "4", "5", "6")
                              //
                              
                              if (ontvangen_SMD_string.indexOf("$PEUMB") != -1)
                              {   
                                 publish(new String[] { ontvangen_SMD_string + "\n" });
                                 message.setLength(0);
                              }
                              
                           } // if ((b == '\r' || b == '\n') && message.length() > 0)
                           else
                           {
                              message.append((char)b);
                           }
                        } // for (byte b: newData
                     } // if (numRead > 0)
                     else if (numRead == -1)
                     {
                        publish(new String[] { "interruption or excecution error\n" });
                     }
                  } // public void serialEvent(SerialPortEvent event)
               });              
            } // if (serialPort.isOpen())
            
            return null;

         } // protected Void doInBackground() throws Exception

         @Override
         protected void process(List<String> data)
         {
            // process: Receives data chunks from the publish method asynchronously on the Event Dispatch Thread.
            for (String ontvangen_SMD_string: data)
            {
               // NB altijd in for loop omdat meerdere ontbvangen_SMD_string's verzameld kunnen zijn voordat het hier procesed wordt(inherent aan SwingWorker)
               //System.out.println("ontvangen_SMD_string = " + ontvangen_SMD_string);   // bv martin

               main.total_string = ontvangen_SMD_string;

               // NB Onderstaande om i.p.v. elke minuut de data per 5 minuten op te slaan
               //    werkt hier niet (door de 'brokken' record maar bij AWS zou het wel een mogelijkheid kunnen zijn
               //if (total_string.indexOf("3$") != -1)
               //{
               //   String minuten = sdf4.format(new Date()).substring(10, 10 + 2);  // sdf4: yyyyMMddHHmm
               //   int int_minuten = Integer.parseInt(minuten.trim());
               //
               //   if (int_minuten % 5 == 0   );
               //
               System.out.print(main.total_string);
               //System.out.println(total_string); // NB zo zie je ruwweg hoeveel bytes dat telkens beschikbaar zijn

               // error logging
               if (main.total_string.indexOf("error") != -1)
               {
                  String message = "[AWS] " + "interruption or execution exception"; 
                  main.log_turbowin_system_message(message);
                  
                  //main.obsolate_data_flag = true;  // see also Function RS422_init_new_aws_data_received_check_timer() [main_RS232_RS422.java]                     
                  
               } //  if (main.total_string.indexOf("error") != -1)              
               
               
               // write sensor data to sensor log file
               RS422_write_sensor_data_to_file();
                  
               // only if the last piece of a record string was received update TurboWin+ display and check for presence parameters (if parameter present disable the manual input on TurboWin+ input form)
               if (main.total_string.indexOf("\n") != -1)
               {
                  // read the AWS data which was saved before by TurboWin+ from a sensor data file
                  RS422_Read_AWS_Sensor_Data_For_Display();  
                  
                  // update main screen with application name + mode and date time last received data (single info line botton screen)
                  String info = "";
                  if ((main_RS232_RS422.test_record.equals("")) || (main_RS232_RS422.test_record == null)) // test_rcord was set in RS422_write_sensor_data_to_file()
                  {
                     info = "no data";
                  } 
                  else 
                  {
                     info = main_RS232_RS422.test_record;
                  }     
                  main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received AWS data via serial communication: " + info);
                     
                  // for checking that the displayed data is not obsolete (i.e. not updated last 5 minutes)
                  last_new_data_received_TimeMillis = System.currentTimeMillis(); // see also Function: RS422_init_new_aws_data_received_check_timer() [main_RS232_422.java]
               } 
               // update the date and time on the main screen (and the global date time var's)
               // NB if during start up the date and time displayed in the pupup message was not comfired by the user
               //    it is assumed that the pc is not running on the correct time so then do not update the date time automatically
            } // for (String ontvangen_string : data)
         }
     // }.execute(); // new SwingWorker<String, String>()
   
}
*/


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private class RS422_V3_Class_Receive_Sensor_Data extends SwingWorker<String, String>
{
   /////////////////// AWS serial only /////////////////
   //
   // called from: RS422_initComponents()
   
   //   new SwingWorker<String, String>()
   //   {
         @Override
         protected String doInBackground() throws Exception
         {
            main.serialPort.openPort(); 
            if (main.serialPort.isOpen())
            {
               //main.serialPort.openPort();               // NB will be closed in Function:  File_Exit_menu_actionPerformd() [main.java]
               //main.serialPort.setParams(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
               //main.serialPort.setFlowControlMode(main.flow_control);
               main.serialPort.setComPortParameters(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
               main.serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
             
               
               // Preparing a mask. In a mask, we need to specify the types of events that we want to track.
               // Well, for example, we need to know what came some data, thus in the mask must have the
               // following value: MASK_RXCHAR. If we, for example, still need to know about changes in states 
               // of lines CTS and DSR, the mask has to look like this: SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR
               //int mask = SerialPort.MASK_RXCHAR;
                  
               // Set the prepared mask
               //main.serialPort.setEventsMask(mask);                  
                  
		      } // if (serialPort.isOpen())
            else        
            {
               System.out.println("+++ " + "[AWS] Couldn't open " +  main.serialPort.getDescriptivePortName());
            }                       
        
            
            if (main.serialPort.isOpen())
            {
               main.serialPort.addDataListener(new SerialPortDataListener() 
               {
                  @Override
                  public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                  
                  @Override
                  public void serialEvent(SerialPortEvent event)
                  {
                     String ontvangen_SMD_string = "";
                     
                     if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                        return;
                     
                     //byte[] newData = new byte[main.serialPort.bytesAvailable()];
                     //int numRead = main.serialPort.readBytes(newData, newData.length);
                     int numRead;
                     byte[] newData = null;
                     try
                     {
                        newData = new byte[main.serialPort.bytesAvailable()];
                        numRead = main.serialPort.readBytes(newData, newData.length);                  
                     }
                     catch (NegativeArraySizeException ex) 
                     {
                        numRead = -1;
                        // NB don't modify the GUI anywhere except for on the event-dispatch-thread.!! (so not here in the function doinBackground())
                     }
                     
                     //ontvangen_SMD_string = main.serialPort.readString(event.getEventValue());
                     if (numRead > 0)
                     {
                        ontvangen_SMD_string = new String(newData, StandardCharsets.UTF_8);
                     
                        //System.out.println("Read " + numRead + " bytes.");
                     
                        // publish: Sends data chunks to the process(java.util.List) method. This method is to be used from inside the doInBackground method to deliver intermediate results for processing on the Event Dispatch Thread inside the process method.
                        //          Because the process method is invoked asynchronously on the Event Dispatch Thread  multiple invocations to the publish method might occur before the process method is executed. For performance purposes all these invocations are coalesced into one invocation with concatenated arguments.
                        //
                        // For example:
                        //
                        // publish("1");
                        // publish("2", "3");
                        // publish("4", "5", "6");
                        //
                        // might result in: process("1", "2", "3", "4", "5", "6")
                        //
                        publish(new String[] { ontvangen_SMD_string });
                     } // if (numRead > 0)
                     else if (numRead == -1)
                     {
                        publish(new String[] { "interruption or excecution error\n" });
                     }
                  } // public void serialEvent(SerialPortEvent event)
               });              
            } // if (serialPort.isOpen())
            
            return null;

         } // protected Void doInBackground() throws Exception

         @Override
         protected void process(List<String> data)
         {
            // process: Receives data chunks from the publish method asynchronously on the Event Dispatch Thread.
            for (String ontvangen_SMD_string: data)
            {
               // NB altijd in for loop omdat meerdere ontbvangen_SMD_string's verzameld kunnen zijn voordat het hier procesed wordt(inherent aan SwingWorker)
               //System.out.println("ontvangen_SMD_string = " + ontvangen_SMD_string);   // bv martin

               main.total_string = ontvangen_SMD_string;

               // NB Onderstaande om i.p.v. elke minuut de data per 5 minuten op te slaan
               //    werkt hier niet (door de 'brokken' record maar bij AWS zou het wel een mogelijkheid kunnen zijn
               //if (total_string.indexOf("3$") != -1)
               //{
               //   String minuten = sdf4.format(new Date()).substring(10, 10 + 2);  // sdf4: yyyyMMddHHmm
               //   int int_minuten = Integer.parseInt(minuten.trim());
               //
               //   if (int_minuten % 5 == 0   );
               //
               System.out.print(main.total_string);
               //System.out.println(total_string); // NB zo zie je ruwweg hoeveel bytes dat telkens beschikbaar zijn

               // error logging
               if (main.total_string.indexOf("error") != -1)
               {
                  String message = "[AWS] " + "interruption or execution exception"; 
                  main.log_turbowin_system_message(message);
                  
                  //main.obsolate_data_flag = true;  // see also Function RS422_init_new_aws_data_received_check_timer() [main_RS232_RS422.java]                     
                  
               } //  if (main.total_string.indexOf("error") != -1)              
               
               
               // write sensor data to sensor log file
               RS422_V3_write_sensor_data_to_file();
/*                  
               // only if the last piece of a record string was received update TurboWin+ display and check for presence parameters (if parameter present disable the manual input on TurboWin+ input form)
               if (main.total_string.indexOf("\n") != -1)
               {
                  // read the AWS data which was saved before by TurboWin+ from a sensor data file
                  RS422_Read_AWS_Sensor_Data_For_Display();  
                  
                  // update main screen with application name + mode and date time last received data (single info line botton screen)
                  String info = "";
                  if ((main_RS232_RS422.test_record.equals("")) || (main_RS232_RS422.test_record == null)) // test_rcord was set in RS422_write_sensor_data_to_file()
                  {
                     info = "no data";
                  } 
                  else 
                  {
                     info = main_RS232_RS422.test_record;
                  }     
                  main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received AWS data via serial communication: " + info);
                     
                  // for checking that the displayed data is not obsolete (i.e. not updated last 5 minutes)
                  last_new_data_received_TimeMillis = System.currentTimeMillis(); // see also Function: RS422_init_new_aws_data_received_check_timer() [main_RS232_422.java]
               } 
*/             
               // update the date and time on the main screen (and the global date time var's)
               // NB if during start up the date and time displayed in the pupup message was not comfired by the user
               //    it is assumed that the pc is not running on the correct time so then do not update the date time automatically
               if (main.use_system_date_time_for_updating == true)
               {  
                  set_datetime_while_collecting_sensor_data();
               }
            } // for (String ontvangen_string : data)
         }
     // }.execute(); // new SwingWorker<String, String>()
   
}




  
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
public void RS422_initComponents()
{
   
   /////// NB EUCAWS only ////////
   
   // http://www.snip2code.com/Snippet/1044/Java--read-from-USB-using-RXTX-library
   // FOR LINUX:
   //#==================================================================#
   // WARNING! - DO NOT SET THE FOLLOWING PROPERTY WITH RXTX LIBRARY, IT
   // CAUSES A PROGRAM LOCK:
   // serialPort.notifyOnOutputEmpty(true);
   //#==================================================================#     
   
   
   // called from main.read_muffin() or main.lees_configuratie_regels()
   //
   
   // start timer for checking every 1 minute if there is new AWS meteo sensor data available (because if last data > 5 minutes old 'gray' data on main screen)
   last_new_data_received_TimeMillis = 0;
   RS422_init_new_aws_data_received_check_timer();
      
   // for dispaying VOT (Visual Observation Trigger) line in bottom text field on main screen
   color_dark_green = new Color(76, 153, 0);
   
   // for sensor data files (in the file name itself) (note also used in RS232_view.java)
   main.sdf3 = new SimpleDateFormat("yyyyMMddHH");                                  // HH hour in day (0-23) note there is also hh (then also with am, pm)
   main.sdf3.setTimeZone(TimeZone.getTimeZone("UTC"));
   
   // for date-time strings/remarks in the sensor data records (note also used in RS232_view.java)
   main.sdf4 = new SimpleDateFormat("yyyyMMddHHmm");                                // HH hour in day (0-23) note there is also hh (then also with am, pm)
   main.sdf4.setTimeZone(TimeZone.getTimeZone("UTC"));
    
   // for function: RS422_write_sensor_data_to_file
   sdf7 = new SimpleDateFormat("ss");                                           
   sdf7.setTimeZone(TimeZone.getTimeZone("UTC"));
   
   
   // dir for storage sensor data
   //if (logs_dir.trim().equals("") == true || logs_dir.trim().length() < 2)
   if ((main.logs_dir == null) || (main.logs_dir.compareTo("") == 0))
   {
      String info = "logs folder (for sensor data storage) unknown, select: Maintenance -> Log files settings";
      JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
   }
      
   // checking/looking for the correct com port
   int completed_checks_serial_ports = 0;
   while ((main.defaultPort == null) && (completed_checks_serial_ports < MAX_COMPLETED_AWS_PORT_CHECKS))
   {
      RS422_Check_Serial_Ports(completed_checks_serial_ports);
      completed_checks_serial_ports++;
   }
   
   // delete old sensor data files
   main.log_turbowin_system_message("[GENERAL] deleting old (AWS) sensor data files");
   RS232_Delete_Sensor_Data_Files();                                        // also used by RS422 (AWS)
   

   if (main.defaultPort != null)
   {
      // info text on (bottom) main screen 
      //main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode + ", receiving AWS sensor data via serial communication....."); // application mode was set in initComponents2() [main.java]
      main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode + ", last received AWS data via serial communication: no data"); // application mode was set in initComponents2() [main.java]
      
      //final SerialPort serialPort  = SerialPort.getCommPort(main.defaultPort);//main.serialPort = new SerialPort(main.defaultPort);
      main.serialPort  = SerialPort.getCommPort(main.defaultPort); 

      new SwingWorker<String, String>()
      {
         @Override
         protected String doInBackground() throws Exception
         {
            main.serialPort.openPort(); 
            if (main.serialPort.isOpen())
            {
               //main.serialPort.openPort();               // NB will be closed in Function:  File_Exit_menu_actionPerformd() [main.java]
               //main.serialPort.setParams(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
               //main.serialPort.setFlowControlMode(main.flow_control);
               main.serialPort.setComPortParameters(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
               main.serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
             
               
               // Preparing a mask. In a mask, we need to specify the types of events that we want to track.
               // Well, for example, we need to know what came some data, thus in the mask must have the
               // following value: MASK_RXCHAR. If we, for example, still need to know about changes in states 
               // of lines CTS and DSR, the mask has to look like this: SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR
               //int mask = SerialPort.MASK_RXCHAR;
                  
               // Set the prepared mask
               //main.serialPort.setEventsMask(mask);                  
                  
		      } // if (serialPort.isOpen())
            else        
            {
               System.out.println("+++ " + "[AWS] Couldn't open " +  main.serialPort.getDescriptivePortName());
            }                       
        
            
            if (main.serialPort.isOpen())
            {
               main.serialPort.addDataListener(new SerialPortDataListener() 
               {
                  @Override
                  public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                  
                  @Override
                  public void serialEvent(SerialPortEvent event)
                  {
                     String ontvangen_SMD_string = "";
                     
                     if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                        return;
                     
                     //byte[] newData = new byte[main.serialPort.bytesAvailable()];
                     //int numRead = main.serialPort.readBytes(newData, newData.length);
                     int numRead;
                     byte[] newData = null;
                     try
                     {
                        newData = new byte[main.serialPort.bytesAvailable()];
                        numRead = main.serialPort.readBytes(newData, newData.length);                  
                     }
                     catch (NegativeArraySizeException ex) 
                     {
                        numRead = -1;
                        // NB don't modify the GUI anywhere except for on the event-dispatch-thread.!! (so not here in the function doinBackground())
                     }
                     
                     //ontvangen_SMD_string = main.serialPort.readString(event.getEventValue());
                     if (numRead > 0)
                     {
                        ontvangen_SMD_string = new String(newData, StandardCharsets.UTF_8);
                     
                        //System.out.println("Read " + numRead + " bytes.");
                     
                        // publish: Sends data chunks to the process(java.util.List) method. This method is to be used from inside the doInBackground method to deliver intermediate results for processing on the Event Dispatch Thread inside the process method.
                        //          Because the process method is invoked asynchronously on the Event Dispatch Thread  multiple invocations to the publish method might occur before the process method is executed. For performance purposes all these invocations are coalesced into one invocation with concatenated arguments.
                        //
                        // For example:
                        //
                        // publish("1");
                        // publish("2", "3");
                        // publish("4", "5", "6");
                        //
                        // might result in: process("1", "2", "3", "4", "5", "6")
                        //
                        publish(new String[] { ontvangen_SMD_string });
                     } // if (numRead > 0)
                     else if (numRead == -1)
                     {
                        publish(new String[] { "interruption or excecution error\n" });
                     }
                  } // public void serialEvent(SerialPortEvent event)
               });              
            } // if (serialPort.isOpen())
            
            return null;

         } // protected Void doInBackground() throws Exception

         @Override
         protected void process(List<String> data)
         {
            // process: Receives data chunks from the publish method asynchronously on the Event Dispatch Thread.
            for (String ontvangen_SMD_string: data)
            {
               // NB altijd in for loop omdat meerdere ontbvangen_SMD_string's verzameld kunnen zijn voordat het hier procesed wordt(inherent aan SwingWorker)
               //System.out.println("ontvangen_SMD_string = " + ontvangen_SMD_string);   // bv martin

               main.total_string = ontvangen_SMD_string;

               // NB Onderstaande om i.p.v. elke minuut de data per 5 minuten op te slaan
               //    werkt hier niet (door de 'brokken' record maar bij AWS zou het wel een mogelijkheid kunnen zijn
               //if (total_string.indexOf("3$") != -1)
               //{
               //   String minuten = sdf4.format(new Date()).substring(10, 10 + 2);  // sdf4: yyyyMMddHHmm
               //   int int_minuten = Integer.parseInt(minuten.trim());
               //
               //   if (int_minuten % 5 == 0   );
               //
               System.out.print(main.total_string);
               //System.out.println(total_string); // NB zo zie je ruwweg hoeveel bytes dat telkens beschikbaar zijn

               // error logging
               if (main.total_string.indexOf("error") != -1)
               {
                  String message = "[AWS] " + "interruption or execution exception"; 
                  main.log_turbowin_system_message(message);
                  
                  //main.obsolate_data_flag = true;  // see also Function RS422_init_new_aws_data_received_check_timer() [main_RS232_RS422.java]                     
                  
               } //  if (main.total_string.indexOf("error") != -1)              
               
               
               // write sensor data to sensor log file
               RS422_write_sensor_data_to_file();
                  
               // only if the last piece of a record string was received update TurboWin+ display and check for presence parameters (if parameter present disable the manual input on TurboWin+ input form)
               if (main.total_string.indexOf("\n") != -1)
               {
                  // read the AWS data which was saved before by TurboWin+ from a sensor data file
                  RS422_Read_AWS_Sensor_Data_For_Display();  
                  
                  // update main screen with application name + mode and date time last received data (single info line botton screen)
                  String info = "";
                  if ((main_RS232_RS422.test_record.equals("")) || (main_RS232_RS422.test_record == null)) // test_rcord was set in RS422_write_sensor_data_to_file()
                  {
                     info = "no data";
                  } 
                  else 
                  {
                     info = main_RS232_RS422.test_record;
                  }     
                  main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received AWS data via serial communication: " + info);
                     
                  // for checking that the displayed data is not obsolete (i.e. not updated last 5 minutes)
                  last_new_data_received_TimeMillis = System.currentTimeMillis(); // see also Function: RS422_init_new_aws_data_received_check_timer() [main_RS232_422.java]
               } 
               // update the date and time on the main screen (and the global date time var's)
               // NB if during start up the date and time displayed in the pupup message was not comfired by the user
               //    it is assumed that the pc is not running on the correct time so then do not update the date time automatically
            } // for (String ontvangen_string : data)
         }
      }.execute(); // new SwingWorker<String, String>()

   } // if (defaultPort != null)
   
} 
*/   



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS232_GPS_NMEA_0183_Check_Serial_Ports(int completed_checks_serial_ports)
{
   int teller                                        = -1;
   //String[] serial_ports_portid_array                = new String[main.NUMBER_COM_PORTS];
   SerialPort[] serial_ports_portid_array            = new SerialPort[main.NUMBER_COM_PORTS];
   String info                                       = "[GPS] start up: no GPS found";
   String hulp_parity                                = "";                     // for message writing on java console
   

   // initialisation
   for (int i = 0; i < main.NUMBER_COM_PORTS; i++)
   {
      serial_ports_portid_array[i]                   = null;
   }

   // initialisation
   GPS_defaultPort                                   = null;
   GPS_defaultPort_descriptive                       = null;  
   
   
   if (main.prefered_GPS_COM_port.equals("AUTOMATICALLY"))
   {
      // Temporary message box, (pop-up for only a short time) automatically disappears
      //
      if (completed_checks_serial_ports == 0)                  // only show this temporary meassagebox at first port-checking-round
      {
         final JOptionPane pane_begin = new JOptionPane("[GPS] checking serial com ports", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
         final JDialog checking_ports_begin_dialog = pane_begin.createDialog(main.APPLICATION_NAME);

         Timer timer_begin = new Timer(1000, new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               checking_ports_begin_dialog.dispose();
            }
         });
         timer_begin.setRepeats(false);
         timer_begin.start();
         checking_ports_begin_dialog.setVisible(true);
      }

      // Get sorted array of serial ports in the system
	   //main.portList = SerialPortList.getPortNames();
      SerialPort[] serialPorts = SerialPort.getCommPorts();

      //for (int i = 0; i < main.portList.length && i < main.NUMBER_COM_PORTS; i++) 
      for (int portNo = 0; portNo < serialPorts.length; portNo++)   
      {
         teller++;
         SerialPort serialPort_test = SerialPort.getCommPort(serialPorts[portNo].getSystemPortName()); 
         serialPort_test.openPort();  
         
         if (serialPort_test.isOpen())
         { 
            serial_ports_portid_array[teller] = serialPorts[portNo];
            serialPort_test.closePort();
            String message = "[GPS] found serial com port (ok): " + serialPorts[portNo].getDescriptivePortName();
            main.log_turbowin_system_message(message);
         } // if (serialPort_test.isOpen()) 
         else
         {
            serial_ports_portid_array[teller] = null;
            System.out.println("[GPS] couldn't open: " + serialPorts[portNo].getDescriptivePortName());
         } // else         
         

         // NB opening a port cannot be done in SwingWorker, so must be done here !!

	   } //  for (int i = 0; i < main.portList.length && i < main.NUMBER_COM_PORTS; i++)


      for (int i = 0; i < main.NUMBER_COM_PORTS; i++)                      // max number ports to open
      {
         if (serial_ports_portid_array[i] != null)                         // so serial port present (and also not in use)
         {
            //SerialPort serialPort_test = new SerialPort(serial_ports_portid_array[i]);
            SerialPort serialPort_test = SerialPort.getCommPort(serial_ports_portid_array[i].getSystemPortName());
            serialPort_test.openPort();  
            if (serialPort_test.isOpen()) 
		      //try
            {
               serialPort_test.openPort();                                 // Open serial port
               
               if (GPS_NMEA_0183_parity == 0)
               {
                  hulp_parity = "none";
               }
               else if (GPS_NMEA_0183_parity == 1)
               {
                  hulp_parity = "odd";
               }
               else if (GPS_NMEA_0183_parity == 2)
               {
                  hulp_parity = "even";
               }               
               
               System.out.println("[GPS] trying to open with " + String.valueOf(main.GPS_bits_per_second) + " " + hulp_parity + " " + String.valueOf(GPS_NMEA_0183_data_bits) + " " + String.valueOf(GPS_NMEA_0183_stop_bits) + " " + serial_ports_portid_array[i].getDescriptivePortName() );
               //serialPort_test.setParams(main.GPS_bits_per_second, GPS_NMEA_0183_data_bits, GPS_NMEA_0183_stop_bits, GPS_NMEA_0183_parity);
               serialPort_test.setComPortParameters(main.GPS_bits_per_second, GPS_NMEA_0183_data_bits, GPS_NMEA_0183_stop_bits, GPS_NMEA_0183_parity);
               serialPort_test.setFlowControl(GPS_NMEA_0183_flow_control); 
               
                  
               try
               {
                  Thread.sleep(2000);                                      // 1000 seems too short on a few systems
               }
               catch (InterruptedException ex){ }                 
               
               //String response = serialPort_test.readString();   
               //System.out.println(response);
 
               // the response   
               String response = "";
               serialPort_test.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 2000, 0);
               try 
               {
                  //while (true)
                  //{
                     byte[] readBuffer = new byte[1024];            // NB keep size 1024 because first part of incoming data is rubish
                     //int numRead = serialPort_test.readBytes(readBuffer, readBuffer.length);
                     serialPort_test.readBytes(readBuffer, readBuffer.length);
                     //System.out.println("Read " + numRead + " bytes.");
                     response = new String(readBuffer, StandardCharsets.UTF_8);
                     System.out.println(response);
                  //}
               } 
               catch (Exception e) 
               { 
                  System.out.println(e);
               }              
               
               
               if (response != null && (response.indexOf("$GP") >= 0 || response.indexOf("$GL") >= 0))
               {
                  info = "[GPS] found GPS on port: " + serial_ports_portid_array[i].getDescriptivePortName();
                  System.out.print("--- ");
                  main.log_turbowin_system_message(info);
                  System.out.println("");
                  
                  serialPort_test.closePort();
                  GPS_defaultPort = serial_ports_portid_array[i].getSystemPortName();
                  GPS_defaultPort_descriptive = serial_ports_portid_array[i].getDescriptivePortName();
                  break;
               }
               else 
               {
                  serialPort_test.closePort();
                  info = "[GPS] no GPS found";
                  System.out.println("--- " + info);
                  System.out.println("");
               }    
		      } // if (serialPort_test.isOpen())
            //catch (SerialPortException ex)
            else
            {
               //System.out.println(ex);
               System.out.println("[GPS] couldn't open: " + serial_ports_portid_array[i].getDescriptivePortName());     
            }
         } // if (serial_ports_portid_array[i] != null)
      } // for (int i = 0; i < NUMBER_COM_PORTS; i++)

      if (GPS_defaultPort != null)
      {
         // temporary message box (GPS (NMEA 0183) was found) 
         final JOptionPane pane_end = new JOptionPane(info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
         final JDialog checking_ports_end_dialog = pane_end.createDialog(main.APPLICATION_NAME);

         Timer timer_end = new Timer(1500, new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               //checking_ports_end_dialog.setVisible(false);
               // or maybe you'll need dialog.dispose() instead?
               checking_ports_end_dialog.dispose();
            }
         });
         timer_end.setRepeats(false);
         timer_end.start();
         checking_ports_end_dialog.setVisible(true);
      }
      else
      {
         // No com port with an GPS (NMEA 0183) connected found
         
         // blocking message box (no GPS NMEA 0183 found); but only show message if it is the last port-checking-round 
         if (completed_checks_serial_ports == MAX_COMPLETED_GPS_PORT_CHECKS - 1)
         {
            main.log_turbowin_system_message(info);
            
            if (main.obsolate_GPS_data_flag == false)  // eg at start up of this application
            {
               JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
            }
         }
      } // else
 
   } // if (main.prefered_COM_port.equals("AUTOMATICALLY"))
   
   else // fixed GPS com port set by user
   {   
      //SerialPort serialPort_test = new SerialPort(main.prefered_GPS_COM_port);
      SerialPort serialPort_test = SerialPort.getCommPort(main.prefered_GPS_COM_port);
      serialPort_test.openPort();
         
      if (serialPort_test.isOpen())   
      {  
         serialPort_test.closePort();
         
         // OK, no port problems encoutered
         GPS_defaultPort = main.prefered_GPS_COM_port;
         GPS_defaultPort_descriptive = main.prefered_COM_port;  // NB this is a work around this is not the exact descriptive term as obtained via the AUTOMTICALLY branch
         info = "[GPS] " + main.prefered_GPS_COM_port + ", serial com port available";
         
         // file logging
         main.log_turbowin_system_message(info);
         
         final JOptionPane pane_begin = new JOptionPane(info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
         final JDialog checking_ports_begin_dialog = pane_begin.createDialog(main.APPLICATION_NAME);

         Timer timer_begin = new Timer(1000, new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               checking_ports_begin_dialog.dispose();
            }
         });
         timer_begin.setRepeats(false);
         timer_begin.start();
         checking_ports_begin_dialog.setVisible(true);            
      } // if (serialPort_test.isOpen())  
      else 
      {
         // error opening predefined (by the user)port, reset/warnings
         info = "[GPS] " + main.prefered_GPS_COM_port + ", serial com port not available";
         main.log_turbowin_system_message(info);
         
         if (main.obsolate_GPS_data_flag == false)  // eg at start up of this application
         {
            JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         }
         //System.out.println(info);
         GPS_defaultPort = null;
         GPS_defaultPort_descriptive = null;
      } // else   
   }
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static boolean RS232_GPS_NMEA_0183_Date_Position_Parsing(String mode)
{
   // GPS_date: 230394       Date - 23rd of March 1994
   // GPS_time: 092751.000   time 09:27:51                                             // nb fix_time could also be: 092751
   //
   // called from: - RS232_initComponents()[main_RS232_RS422.java] (for APR)
   //              - initSynopparameters() [myposition.java]
   //              - RS232_GPS_NMEA_0183_RMC(String ontvangen_GPS_string)[main_RS232_RS422.java]  only for updating position field main screen
   //              - RS232_GPS_NMEA_0183_GGA(String ontvangen_GPS_string)[main_RS232_RS422.java]  only for updating position field main screen
   //
   // 
   boolean GPS_date_time_ok = true;
   String message = "";
   String message_fix_date_time = "";
   String message_fix_position = "";
   
   
   if ((GPS_date.length() != 6) || (GPS_time.length() < 6))
   {
      GPS_date_time_ok = false;
      message = "[GPS] GPS date/time format not ok (GPS date = " + GPS_date + ", GPS time = " + GPS_time + ")";
   }   
      
   if (GPS_date_time_ok)
   {
      SimpleDateFormat sdf9 = new SimpleDateFormat("yyyyMMddHHmm");                      // HH hour in day (0-23) note there is also hh (then also with am, pm)
      sdf9.setTimeZone(TimeZone.getTimeZone("UTC"));
   
      String hulp_GPS_yyyyMMdd = "20" + GPS_date.substring(4) + GPS_date.substring(2,4) + GPS_date.substring(0,2);
      String hulp_GPS_HHmm = GPS_time.substring(0, 4); 
      String hulp_fix_datum_tijd_minuten = hulp_GPS_yyyyMMdd + hulp_GPS_HHmm;    // eg final result : 202303940927
      Date hulp_fix_date = null;
      try 
      {
         hulp_fix_date = sdf9.parse(hulp_fix_datum_tijd_minuten);
         
         long system_sec = System.currentTimeMillis();
         long timeDiff = Math.abs(hulp_fix_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

         //System.out.println("+++ system_sec: " + system_sec); 
         //System.out.println("+++ hulp_fix_date: " + hulp_fix_date); 
         //System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min
      
         //String record_pressure_present = null;

         if (timeDiff > TIMEDIFF_GPS_DATA)      // max 10 minutes difference GPS time <--> computer time
         {
            GPS_date_time_ok = false;
            message = "[GPS] GPS date/time obsolete; > " + (int)TIMEDIFF_GPS_DATA + " minutes old compared with computer date/time (time difference = " + timeDiff + " minutes)";
         }    
      } // try
      catch (ParseException ex) 
      {
         GPS_date_time_ok = false;
         message = "[GPS] GPS date/time format not ok (" + ex + ")";
      }
   } // if (GPS_date_time_ok)
   
   
   if (GPS_date_time_ok)
   {   
      //
      /////////// date ///////////////
      // 
         
      // year
      String fix_year = "20" + GPS_date.substring(4);
         
      // month
      String fix_month = GPS_date.substring(2,4);
         
      // day
      String fix_day = GPS_date.substring(0,2);      
         
         
      //
      ////////// Time (eg 092751) /////////
      // 
         
      // hour
      String fix_hour = GPS_time.substring(0, 2);
       
      // minutes
      String fix_minute = GPS_time.substring(2, 4);
      
      //System.out.println("GPS date/time (dd-mm-yyyy hh.mm UTC): " + fix_day + "-" + fix_month + "-" + fix_year + " " +  fix_hour + "." + fix_minute + " UTC");
      message_fix_date_time = "GPS date/time (dd-mm-yyyy hh.mm UTC): " + fix_day + "-" + fix_month + "-" + fix_year + " " +  fix_hour + "." + fix_minute + " UTC";   
         
      //
      //////// latitude (eg 4804.7041778) /////////
      //
       
      // fill the public string latitude values
      //
      myposition.latitude_degrees = GPS_latitude.substring(0, 2);    // eg 4804.7041778 -> 48
         
      String fix_latitude_minutes = GPS_latitude.substring(2);       // only the minutes of the latitude eg 4804.7041778 -> 04.7041778
      BigDecimal bd_lat_min = new BigDecimal(fix_latitude_minutes).setScale(0, RoundingMode.HALF_UP); // 0 decimals 
      myposition.latitude_minutes = bd_lat_min.toString();           // rounded minutes value  e.g. 41.9504 -> 42 minutes
         
      if (GPS_latitude_hemisphere.toUpperCase().equals("N"))
      {
         myposition.latitude_hemisphere = myposition.HEMISPHERE_NORTH;
      }
      else if (GPS_latitude_hemisphere.toUpperCase().equals("S"))
      {
         myposition.latitude_hemisphere = myposition.HEMISPHERE_SOUTH;
      }
      else
      {
         myposition.latitude_hemisphere = "";
         GPS_date_time_ok = false;
      }  
         
      // fill the public int latitude values
      //
      try 
      {
         myposition.int_latitude_degrees = Integer.parseInt(myposition.latitude_degrees);
      }
      catch (NumberFormatException e)
      {
         GPS_date_time_ok = false;
      }
      
      try 
      {
         myposition.int_latitude_minutes = Integer.parseInt(myposition.latitude_minutes);
      }
      catch (NumberFormatException e)
      {
         GPS_date_time_ok = false;
      }       
         
      // for latitude (LaLaLa) for IMMT log
      //
      int int_latitude_minutes_6 =  bd_lat_min.intValue() / 6;                           // devide the minutes by six and disregard the remainder, now tenths of degrees [IMMT rounding!!!}
      String latitude_minutes_6 = Integer.toString(int_latitude_minutes_6);              // convert to String 
      //myposition.lalala_code = myposition.latitude_degrees.trim().replaceFirst("^0+(?!$)", "") + latitude_minutes_6;
      myposition.lalala_code = myposition.latitude_degrees + latitude_minutes_6;

      int len = 3;                                                           // always 3 chars
      if (myposition.lalala_code.length() < len) 
      {
         // pad on left with zeros
         myposition.lalala_code = "0000000000".substring(0, len - myposition.lalala_code.length()) + myposition.lalala_code;
      }         
      else if (myposition.lalala_code.length() > len)
      {
         GPS_date_time_ok = false;
         //System.out.println("+++++++++++ lalala_code error: " + myposition.lalala_code);
      }       
         
         
      //
      //////// longitude (eg 01144.3966270) ////////
      //
         
      // fill the public string longitude values
      //
      myposition.longitude_degrees = GPS_longitude.substring(0, 3);   // eg 01144.3966270 -> 011
       
      String fix_longitude_minutes = GPS_longitude.substring(3);      // only the minutes of the longitude eg  01144.3966270 -> 44.3966270
      BigDecimal bd_lon_min = new BigDecimal(fix_longitude_minutes).setScale(0, RoundingMode.HALF_UP); // 0 decimals 
      myposition.longitude_minutes = bd_lon_min.toString();           // rounded minutes value  e.g. 41.9504 -> 42 minutes         
         
      if (GPS_longitude_hemisphere.toUpperCase().equals("E"))
      {
         myposition.longitude_hemisphere = myposition.HEMISPHERE_EAST;
      }
      else if (GPS_longitude_hemisphere.toUpperCase().equals("W"))
      {
         myposition.longitude_hemisphere = myposition.HEMISPHERE_WEST;
      }
      else
      {
         myposition.longitude_hemisphere = "";
         GPS_date_time_ok = false;
      }
         
      // fill the public int longitude values
      //
      try 
      {
         myposition.int_longitude_degrees = Integer.parseInt(myposition.longitude_degrees);
      }
      catch (NumberFormatException e)
      {
         GPS_date_time_ok = false;
      }
      
      try 
      {
         myposition.int_longitude_minutes = Integer.parseInt(myposition.longitude_minutes);
      }
      catch (NumberFormatException e)
      {
         GPS_date_time_ok = false;
      }
         
      // for longitude (LoLoLoLo) for IMMT log
      //
      int int_longitude_minutes_6 =  bd_lon_min.intValue() / 6;                           // devide the minutes by six and disregard the remainder, now tenths of degrees [IMMT rounding!!!}
      String longitude_minutes_6 = Integer.toString(int_longitude_minutes_6);              // convert to String 
      myposition.lolololo_code = myposition.longitude_degrees + longitude_minutes_6;

      int len2 = 4;                                                           // always 4 chars
      if (myposition.lolololo_code.length() < len2) 
      {
         // pad on left with zeros
         myposition.lolololo_code = "0000000000".substring(0, len2 - myposition.lolololo_code.length()) + myposition.lolololo_code;
      }  
      else if (myposition.lolololo_code.length() > len2)
      {
         GPS_date_time_ok = false;
         //System.out.println("+++++++++++ lolololo_code error: " + myposition.lolololo_code);
      } 
         
         
      // quadrant of the globe for IMMT
      //
      if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
      {
         myposition.Qc_code = "1";
      }
      else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
      {
         myposition.Qc_code = "3";
      }
      else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
      {
         myposition.Qc_code = "5";
      }
      else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
      {
         myposition.Qc_code = "7";
      }
      else
      {
         GPS_date_time_ok = false;
         //System.out.println("+++++++++++ Qc_code error");
         
      }
         
      // message_fix_position = "GPS position (dd-mm [N/S] ddd-mm [E/W]): " + myposition.latitude_degrees + "-" + myposition.latitude_minutes + " " + myposition.latitude_hemisphere.substring(0, 1) +  " " + myposition.longitude_degrees + "-" + myposition.longitude_minutes + " " + myposition.longitude_hemisphere.substring(0, 1);  
      
      
      if (GPS_date_time_ok == false)   
      {
         message = "[GPS] formatting error";
         message_fix_position = "";
      }
      else
      {
         message_fix_position = "GPS position (dd-mm [N/S] ddd-mm [E/W]): " + myposition.latitude_degrees + "-" + myposition.latitude_minutes + " " + myposition.latitude_hemisphere.substring(0, 1) +  " " + myposition.longitude_degrees + "-" + myposition.longitude_minutes + " " + myposition.longitude_hemisphere.substring(0, 1);  
      }
   } // if (GPS_date_time_ok)
   
   
   if (GPS_date_time_ok == false)
   {
      // NB if mode.equals("MINUTE_UPDATE") -> do nothing, otherwise every minute an extra line in the log in case of bad connection
      //...myposition.latitude_degrees = "";
      //myposition.latitude_minutes = "";
      //myposition.latitude_hemisphere = "";
      
      
      if (mode.equals("APR"))                                                 // e.g. every 1, 3 or 6 hours
      {
         // message (was set before)
         main.log_turbowin_system_message(message);
      }
      else if (mode.equals("MANUAL"))
      {
         // message (was set before)
         main.log_turbowin_system_message(message);
         
         // temporary message box (eg mismatch GPS date-time and computer date-time) 
         String info = "no reliable GPS position available";
         
         final JOptionPane pane_end = new JOptionPane(info, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
         final JDialog temp_dialog = pane_end.createDialog(main.APPLICATION_NAME);

         Timer timer_end = new Timer(2500, new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent e)
            {
               temp_dialog.dispose();
            }
         });
         timer_end.setRepeats(false);
         timer_end.start();
         temp_dialog.setVisible(true);               
      } // else if (mode.equals("MANUAL"))
   } //  if (GPS_date_time_ok == false)
   else // GPS_date_time_ok == true
   {
      // update the position fields on the main screen (every whole minute an update)
      //if (mode.equals("MINUTE_UPDATE")) 
      //{
      //   main.position_fields_update();
      //}
      
      if (mode.equals("APR") || mode.equals("MANUAL"))    // only in APR mode (1, 3, 6 hours) or manual mode, otherwise every minute message lines in the log
      {
         main.log_turbowin_system_message("[GPS] date-time parsing ok; " + message_fix_date_time);
         main.log_turbowin_system_message("[GPS] position parsing ok; " + message_fix_position);
      }
   } // else (GPS_date_time_ok == true)
   
   
   return GPS_date_time_ok;
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS232_GPS_NMEA_0183_GGA(String ontvangen_GPS_string)
{  
   // called from: RS232_GPS_Class_Receive_Data
   
   // NB if an AWS is connected a GPS cannot be connected [see RS232_settings.java]
   //
   //
   //
   // source: http://www.gpsinformation.org/dale/nmea.htm#RMC

   //GGA - essential fix data which provide 3D location and accuracy data.
   //
   // $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
   //
   // Where:
   //     GGA          Global Positioning System Fix Data
   //     123519       Fix taken at 12:35:19 UTC
   //     4807.038,N   Latitude 48 deg 07.038' N
   //     01131.000,E  Longitude 11 deg 31.000' E
   //     1            Fix quality: 0 = invalid
   //                               1 = GPS fix (SPS)
   //                               2 = DGPS fix
   //                               3 = PPS fix
   //			                        4 = Real Time Kinematic
   //		                           5 = Float RTK
   //                               6 = estimated (dead reckoning) (2.3 feature)
   //			                        7 = Manual input mode
   //			                        8 = Simulation mode
   //     08           Number of satellites being tracked
   //     0.9          Horizontal dilution of position
   //     545.4,M      Altitude, Meters, above mean sea level
   //     46.9,M       Height of geoid (mean sea level) above WGS84
   //                      ellipsoid
   //    (empty field) time in seconds since last DGPS update
   //    (empty field) DGPS station ID number
   //     *47          the checksum data, always begins with *
   //
   //
   // source: https://en.wikipedia.org/wiki/NMEA_0183
   // $GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76
   
   String record_checksum = "";
   String computed_checksum = "";
   boolean checksum_ok = false;
   int number_read_commas = 0;
   int pos_comma = 0;
   int start_fix_time = 0;
   int end_fix_time = 0;
   int start_fix_latitude = 0;
   int end_fix_latitude = 0;
   int start_fix_latitude_hemisphere = 0;
   int end_fix_latitude_hemisphere = 0;
   int start_fix_longitude = 0;
   int end_fix_longitude = 0;
   int start_fix_longitude_hemisphere = 0;
   int end_fix_longitude_hemisphere = 0;
   int start_fix_quality = 0;
   int end_fix_quality = 0;   
  
   
   // checksum check
   computed_checksum = RS232_GPS_NMEA_Checksum(ontvangen_GPS_string);
   record_checksum = ontvangen_GPS_string.substring(ontvangen_GPS_string.length() - 2);   
   
   if (computed_checksum.equals(record_checksum))
   {
      //System.out.println("checksum ok");
      checksum_ok = true;
   }
   else
   {
      //System.out.println("record checksum = " + record_checksum);
      //System.out.println("computed checksum = " + computed_checksum);
      System.out.println(ontvangen_GPS_string);
      System.out.println("checksum NOT ok"); 
   }   
   
   // eg $GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76
   if ((checksum_ok) && (ontvangen_GPS_string.length() > 40))           // for savety, 40 arbitrary number (but will cover latitude, longitude etc)
   {
      do
      {
         pos_comma = ontvangen_GPS_string.indexOf(",", pos_comma + 1);
         if (pos_comma > 0)     // "," found
         {
            //System.out.println("pos_comma = " + pos_comma);
            number_read_commas++;
            
            if (number_read_commas == 1)
            {
               start_fix_time = pos_comma + 1;
            }
            else if (number_read_commas == 2)
            {
               end_fix_time = pos_comma;
               start_fix_latitude = pos_comma + 1;
            }
            else if (number_read_commas == 3)
            {
               end_fix_latitude = pos_comma;
               start_fix_latitude_hemisphere = pos_comma + 1;
            }
            else if (number_read_commas == 4)
            {
               end_fix_latitude_hemisphere = pos_comma;
               start_fix_longitude = pos_comma + 1;
            }
            else if (number_read_commas == 5)
            {
               end_fix_longitude = pos_comma;
               start_fix_longitude_hemisphere = pos_comma + 1;
            }
            else if (number_read_commas == 6) 
            {
               end_fix_longitude_hemisphere = pos_comma; 
               start_fix_quality = pos_comma + 1; 
            }
            else if (number_read_commas == 7) 
            {
               end_fix_quality = pos_comma;
            }
                           
         } // if (pos_comma > 0)
      } while (pos_comma > 0);       
      
      
      // fix time
      String hulp_fix_time = ontvangen_GPS_string.substring(start_fix_time, end_fix_time);
      
      // fix quality (fix quality "0" = invalid)
      String fix_quality = ontvangen_GPS_string.substring(start_fix_quality, end_fix_quality);
      
      // continue if status ok en seconds = 00
      if ( (Integer.parseInt(fix_quality) != 0) && (hulp_fix_time.substring(4, 6).equals("00")) )    // quality=0 = invalid and seconds "00" (= every whole minute)
      {
         System.out.println(ontvangen_GPS_string);
         
         // date (eg 280515)
         //
         // NB Because there is no date in the GGA sentence we can only compare the system time of the GPS and the computer time
         //    For a generic approach the GPS_date is artificial set as computer date
         //    Theroretically an outdated GPS position can be unfairly marked as ok only a few minutes per day
         //    eg 18-12-2015 03.14 UTC last GPS date. Now current date-time 19-12-2015 02:00 UTC so GPS position set invalid by TurboWin+
         //    but if current date-time 19-12-2015 03:13 UTC -> TurboWin+ set it ok because only times (and not date) can be compared
         //
         GregorianCalendar cal_systeem_datum_tijd_NMEA_GGA = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));// gives system date time in UTC of this moment
         SimpleDateFormat sdf10;
         sdf10 = new SimpleDateFormat("ddMMyy");                            // e.g "MMMM dd, yyyy" -> februari 27, 2010
         GPS_date = sdf10.format(cal_systeem_datum_tijd_NMEA_GGA.getTime());
         
         // time (eg 181908.00)
         GPS_time = hulp_fix_time;                    // now GPS_time always holds the last valid value (passed the "A" status check) of the whole minute
         
         // update date and time on main screen (and date time input form)
         //main.date_time_fields_update();
         
         // latitude complete (eg 3404.7041778)
         GPS_latitude = ontvangen_GPS_string.substring(start_fix_latitude, end_fix_latitude);
         
         // latitude hemisphere
         GPS_latitude_hemisphere = ontvangen_GPS_string.substring(start_fix_latitude_hemisphere, end_fix_latitude_hemisphere);
         
         // longitude complete (eg 07044.3966270)
         GPS_longitude = ontvangen_GPS_string.substring(start_fix_longitude, end_fix_longitude); 
         
         // longitude hemisphere
         GPS_longitude_hemisphere = ontvangen_GPS_string.substring(start_fix_longitude_hemisphere, end_fix_longitude_hemisphere);         
         
         // for timer checking the GPS data is not obsolete (see Function: RS232_GPS_NMEA_0183_init_new_data_received_check_timer)
         last_new_GPS_data_received_TimeMillis = System.currentTimeMillis(); 
         
         // update the position fields on the main screen  (so every whole minute)
         //main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing("MINUTE_UPDATE");  // NB we do nothing with the return boolean of this function (this call was only for updating position on main screen)
          
      } // if ( (fix_status.toUpperCase().equals("A")) && (fix_time.substring(4, 6).equals("00")) )
   } // if ((checksum_ok) && (ontvangen_GPS_string.length() > 40))
   
   
   // NB in function RS232_GPS_NMEA_0183_Date_Position_Parsing() these GPS vars are inserted/compared with obs position values
   
}
   
  

/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private String RS232_GPS_NMEA_Checksum(String ontvangen_GPS_string)
{
   // called from: - RS232_GPS_NMEA_0183_RMC()
   //              - RS232_GPS_NMEA_0183_GGA()
   
   
   String computed_checksum = "";
   int checksum = 0;
   
   
   // cheksum computed between '$' and '*'
   int start_pos = ontvangen_GPS_string.indexOf('$');
   int end_pos = ontvangen_GPS_string.indexOf('*');
   
   // cheksum ok? (compulsory for RMA, RMB, and RMC messages)
   //
   if (start_pos != -1 && end_pos != -1) 
   {
      for (int i = start_pos + 1; i < end_pos; i++) 
      {
         checksum = checksum ^ ontvangen_GPS_string.charAt(i);
      }   
      
      computed_checksum = Integer.toHexString(checksum);
      if (computed_checksum.length() == 1)
      {
         computed_checksum = "0" + computed_checksum;
      }
   } // if (start_pos != -1 && end_pos != -1) 
   
   
   return computed_checksum = computed_checksum.toUpperCase();
}


   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS232_GPS_NMEA_0183_RMC(String ontvangen_GPS_string)
{  
   // called from: RS232_GPS_Class_Receive_Data
   
   // NB if an AWS is connected a GPS cannot be connected [see RS232_settings.java]
   //
   //
   //
   // source: http://www.gpsinformation.org/dale/nmea.htm#RMC
   //
   // RMC - NMEA has its own version of essential gps pvt (position, velocity, time) data. It is called RMC, The Recommended Minimum, which will look similar to:
   //
   // $GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
   //
   // Where:
   //     RMC          Recommended Minimum sentence C
   //     123519       Fix taken at 12:35:19 UTC
   //     A            Status A=active or V=Void.
   //     4807.038,N   Latitude 48 deg 07.038' N
   //     01131.000,E  Longitude 11 deg 31.000' E
   //     022.4        Speed over the ground in knots
   //     084.4        Track angle in degrees True
   //     230394       Date - 23rd of March 1994
   //     003.1,W      Magnetic Variation
   //     *6A          The checksum data, always begins with *
   // Note that, as of the 2.3 release of NMEA, there is a new field in the RMC sentence at the end just prior to the checksum. For more information on this field see here.
   //
   //
   //
   // source: https://en.wikipedia.org/wiki/NMEA_0183
   //
   // $GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45
   //   NB LET OP FIX TAKEN AT 092751.000 COULD ALSO 092751
   //
   ////
   // - Each message's starting character is a dollar sign.
   // - The next five characters identify the talker (two characters) and the type of message (three characters).
   // - All data fields that follow are comma-delimited.
   // - Where data is unavailable, the corresponding field remains blank (it contains no character before the next delimiter – see Sample file section below).
   // - The first character that immediately follows the last data field character is an asterisk, but it is only included if a checksum is supplied.
   // - The asterisk is immediately followed by a checksum represented as a two-digit hexadecimal number. The checksum is the bitwise exclusive OR of ASCII codes of all characters between the $ and *. 
   //        According to the official specification, the checksum is optional for most data sentences, but is compulsory for RMA, RMB, and RMC (among others).
   // - <CR><LF> ends the message. 
   //
   // Typical Baud rate	4800 (There is a variation of the standard called NMEA-0183HS that specifies a baud rate of 38,400. This is in general use by AIS devices.)
   // Data bits	      8
   // Parity	         None
   // Stop bits	      1
   // Handshake	      None
   //
   
   
   String record_checksum = "";
   String computed_checksum = "";
   int number_read_commas = 0;
   int pos_comma = 0;
   int start_fix_time = 0;
   int end_fix_time = 0;
   int start_fix_status = 0;
   int end_fix_status = 0;
   int start_fix_latitude = 0;
   int end_fix_latitude = 0;
   int start_fix_latitude_hemisphere = 0;
   int end_fix_latitude_hemisphere = 0;
   int start_fix_longitude = 0;
   int end_fix_longitude = 0;
   int start_fix_longitude_hemisphere = 0;
   int end_fix_longitude_hemisphere = 0;
   int start_fix_date = 0;
   int end_fix_date = 0;   
   boolean checksum_ok = false;

   
   // checksum check
   computed_checksum = RS232_GPS_NMEA_Checksum(ontvangen_GPS_string);
   record_checksum = ontvangen_GPS_string.substring(ontvangen_GPS_string.length() - 2);   
   
   if (computed_checksum.equals(record_checksum))
   {
      //System.out.println("checksum ok");
      checksum_ok = true;
   }
   else
   {
      //System.out.println("record checksum = " + record_checksum);
      //System.out.println("computed checksum = " + computed_checksum);
      System.out.println(ontvangen_GPS_string);
      System.out.println("checksum NOT ok"); 
   }
    
   // process the received GPS RMC string
   // eg $GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45 
   //
   if ((checksum_ok) && (ontvangen_GPS_string.length() > 40))           // for savety, 40 arbitrary number (but will cover latitude, longitude etc)
   {
      do
      {
         pos_comma = ontvangen_GPS_string.indexOf(",", pos_comma + 1);
         if (pos_comma > 0)     // "," found
         {
            //System.out.println("pos_comma = " + pos_comma);
            number_read_commas++;
            
            if (number_read_commas == 1)
            {
               start_fix_time = pos_comma + 1;
            }
            else if (number_read_commas == 2)
            {
               end_fix_time = pos_comma;
               start_fix_status = pos_comma + 1;
            }
            else if (number_read_commas == 3)
            {
               end_fix_status = pos_comma;
               start_fix_latitude = pos_comma + 1;
            }
            else if (number_read_commas == 4)
            {
               end_fix_latitude = pos_comma;
               start_fix_latitude_hemisphere = pos_comma + 1;
            }
            else if (number_read_commas == 5)
            {
               end_fix_latitude_hemisphere = pos_comma;
               start_fix_longitude = pos_comma + 1;
            }
            else if (number_read_commas == 6)
            {
               end_fix_longitude = pos_comma;
               start_fix_longitude_hemisphere = pos_comma + 1;
            }
            else if (number_read_commas == 7) 
            {
               end_fix_longitude_hemisphere = pos_comma; 
            }
            else if (number_read_commas == 9) 
            {
               start_fix_date = pos_comma + 1;
            }
            else if (number_read_commas == 10) 
            {
               end_fix_date = pos_comma;
            }
                           
         } // if (pos_comma > 0)
      } while (pos_comma > 0);       
      
      
      // http://gpsworld.com/what-exactly-is-gps-nmea-data/
      //      181908.00 is the time stamp: UTC time in hours, minutes and seconds.
      //      3404.7041778 is the latitude in the DDMM.MMMMM format. Decimal places are variable.
      //      07044.3966270 is the longitude in the DDDMM.MMMMM format. Decimal places are variable.
      
      
      // fix time
      String hulp_fix_time = ontvangen_GPS_string.substring(start_fix_time, end_fix_time);
      
      // fix status ("A" = active/ok; V = void/not ok)
      String fix_status = ontvangen_GPS_string.substring(start_fix_status, end_fix_status);
      
      // continue if status ok en seconds = 00
      if ( (fix_status.toUpperCase().equals("A")) && (hulp_fix_time.substring(4, 6).equals("00")) )    // status "A" and seconds "00" (= every whole minute)
      {
         System.out.println(ontvangen_GPS_string);
         
         // date (eg 280515)
         GPS_date = ontvangen_GPS_string.substring(start_fix_date, end_fix_date);
         
         // time (eg 181908.00)
         GPS_time = hulp_fix_time;                    // now GPS_time always holds the last valid value (passed the "A" status check) of the whole minute
         
         // update date and time on main screen (and date time input form)
         //main.date_time_fields_update();
         
         // latitude complete (eg 3404.7041778)
         GPS_latitude = ontvangen_GPS_string.substring(start_fix_latitude, end_fix_latitude);
         
         // latitude hemisphere
         GPS_latitude_hemisphere = ontvangen_GPS_string.substring(start_fix_latitude_hemisphere, end_fix_latitude_hemisphere);
         
         // longitude complete (eg 07044.3966270)
         GPS_longitude = ontvangen_GPS_string.substring(start_fix_longitude, end_fix_longitude); 
         
         // longitude hemisphere
         GPS_longitude_hemisphere = ontvangen_GPS_string.substring(start_fix_longitude_hemisphere, end_fix_longitude_hemisphere);         
         
         // for timer checking the GPS data is not obsolete (see Function: RS232_GPS_NMEA_0183_init_new_data_received_check_timer)
         last_new_GPS_data_received_TimeMillis = System.currentTimeMillis(); 
         
         // update the position fields on the main screen (so every whole minute)
         //main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing("MINUTE_UPDATE");  // NB we do nothing with the return boolean of this function (this call was only for updating position on main screen)
          
      } // if ( (fix_status.toUpperCase().equals("A")) && (fix_time.substring(4, 6).equals("00")) )
   } // if ((checksum_ok) && (ontvangen_GPS_string.length() > 40))
   
   // NB in function RS232_GPS_NMEA_0183_Date_Position_Parsing() these GPS vars are inserted/compared with obs position values
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void RS232_GPS_NMEA_0183_initComponents()
{
   // called from: - specific_connection_initComponents()
   
   // checking/looking for the correct com port
   //
   int completed_checks_serial_ports = 0;
   while ((GPS_defaultPort == null) && (completed_checks_serial_ports < MAX_COMPLETED_GPS_PORT_CHECKS))
   {
      RS232_GPS_NMEA_0183_Check_Serial_Ports(completed_checks_serial_ports);               // GPS
      completed_checks_serial_ports++;
   }  
   
   
   if (GPS_defaultPort != null)
   {   
      //System.out.println("+++ GPS_defaultPort = " + GPS_defaultPort);
      
      // info text on (bottom) main screen 
      main.jLabel6.setText(main.APPLICATION_NAME + " receiving GPS data via serial communication.....");
      
      //GPS_serialPort = new SerialPort(GPS_defaultPort);  // NB GPS_defaultPort was determined in Function: RS232_GPS_NMEA_0183_Check_Serial_Ports()
      //final SerialPort GPS_serialPort  = SerialPort.getCommPort(GPS_defaultPort); // // NB GPS_defaultPort was determined in Function: RS232_GPS_NMEA_0183_Check_Serial_Ports()
      main.GPS_serialPort  = SerialPort.getCommPort(GPS_defaultPort);

      
      RS232_GPS_worker = new RS232_GPS_Class_Receive_Data();
      RS232_GPS_worker.execute();
   
      String message = "[GPS] start listening";
      main.log_turbowin_system_message(message);
      
      // start timer for checking every 1 minute if there is new barometer sensor data available (because if last data > 5 minutes old 'gray' data on main screen)
      // NB invoking after: RS232_GPS_worker = new RS232_GPS_Class_Receive_Data(); and RS232_GPS_worker.execute();
      last_new_GPS_data_received_TimeMillis = 0;
      RS232_GPS_NMEA_0183_init_new_data_received_check_timer();
      
   } // if (GPS_defaultPort != null)
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private class RS232_GPS_Class_Receive_Data extends SwingWorker<String, String>
{
   // called from: - RS232_GPS_NMEA_0183_initComponents()
   //              - RS232_GPS_NMEA_0183_init_new_data_received_check_timer()
   
   // NB This class use doInBackground() and done() and not doInBackground(), publish() and process() (see outcommentd class RS232_GPS_Class_Receive_Data_2)
   //    This because here, for the GPS connection, there is a continous input streem and not one string every minute. So better to handle everthing in the background
   //    Disadvantage you cannot savely update the GUI in the doInbackground() eg to report the last updated date-time of the received data on label main screen
   
   
      //new SwingWorker<String, String>()
      //{
         @Override
         protected String doInBackground() throws Exception
         {
            main.GPS_serialPort.openPort();                  // NB will NOT be closed in Function:  File_Exit_menu_actionPerformd() [main.java] ????
            if (main.GPS_serialPort.isOpen())
            {
               String hulp_parity = "";
               if (GPS_NMEA_0183_parity == 0)
               {
                  hulp_parity = "none";
               }
               else if (GPS_NMEA_0183_parity == 1)
               {
                  hulp_parity = "odd";
               }
               else if (GPS_NMEA_0183_parity == 2)
               {
                  hulp_parity = "even";
               }          
               //System.out.println("[GPS] " + GPS_serialPort.getPortName() + " trying to open with " + String.valueOf(main.GPS_bits_per_second) + " " + hulp_parity + " " + String.valueOf(GPS_NMEA_0183_data_bits) + " " + String.valueOf(GPS_NMEA_0183_stop_bits));
               System.out.println("[GPS] " + GPS_defaultPort_descriptive + " trying to open with " + String.valueOf(main.GPS_bits_per_second) + " " + hulp_parity + " " + String.valueOf(GPS_NMEA_0183_data_bits) + " " + String.valueOf(GPS_NMEA_0183_stop_bits));
               main.GPS_serialPort.setComPortParameters(main.GPS_bits_per_second, GPS_NMEA_0183_data_bits, GPS_NMEA_0183_stop_bits, GPS_NMEA_0183_parity);
               main.GPS_serialPort.setFlowControl(GPS_NMEA_0183_flow_control);
                  
		      } // if (GPS_serialPort.isOpen())
            //catch (SerialPortException ex) 
            else        
            {
               System.out.println("[GPS] Couldn't open " +  GPS_defaultPort_descriptive);
            }               
   
            if (main.GPS_serialPort.isOpen())
            {
               main.GPS_serialPort.addDataListener(new SerialPortDataListener() 
               {
                  @Override
                  public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                  
                  
                  StringBuilder message = new StringBuilder();
                  
                  @Override
                  public void serialEvent(SerialPortEvent event)
                  {
                     String ontvangen_GPS_string = "";
                  
                     if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                     {
                        return;
                     }
                     
                     int numRead;
                     byte[] newData = null;
                     try
                     {
                        newData = new byte[main.GPS_serialPort.bytesAvailable()];
                        numRead = main.GPS_serialPort.readBytes(newData, newData.length);                  
                     }
                     catch (NegativeArraySizeException ex) 
                     {
                         numRead = -1;
                        // NB don't modify the GUI anywhere except for on the event-dispatch-thread.!! (so not here in the function doinBackground())
                     }                     
                     
                  
                     if (numRead > 0)
                     {
                        for (byte b: newData) 
                        {
                           if ((b == '\r' || b == '\n') && message.length() > 0) 
                           {
                              ontvangen_GPS_string = message.toString();
                            
                              // NB mail Henry Kleta 22-10-2015
                              //    ...And if you have the choice: Go for the NMEA RMC sentence.
                              //    One sentence, everyting in there and one of the view sentences that (nearly) all GPSes provide.
                              if (main.RS232_GPS_sentence == 1)         // RMC
                              {
                                 if ((ontvangen_GPS_string.indexOf("$GPRMC") != -1) || (ontvangen_GPS_string.indexOf("$GLRMC") != -1))  // (GP for a GPS unit, GL for a GLONASS)
                                 {
                                    RS232_GPS_NMEA_0183_RMC(ontvangen_GPS_string);
                                 }
                              } // if (main.RS232_GPS_sentence == 1)
                              else if (main.RS232_GPS_sentence == 2)     // GGA; could be an alternative in case of NMEA 2000 + converter
                              {
                                 if ((ontvangen_GPS_string.indexOf("$GPGGA") != -1) || (ontvangen_GPS_string.indexOf("$GLGGA") != -1))  // (GP for a GPS unit, GL for a GLONASS)
                                 {
                                    RS232_GPS_NMEA_0183_GGA(ontvangen_GPS_string);
                                 }
                              } // else if (main.RS232_GPS_sentence == 2)
                              
                              message.setLength(0);
                           } // if ((b == '\r' || b == '\n') && message.length() > 0) 
                           else 
                           {
                              message.append((char)b);
                           }
                        } // for (byte b: newData)                     
                     } // if (numRead > 0)  
                     else if (numRead == -1)
                     {
                        String message_a = "[GPS] " + "interruption or execution error (NegativeArraySizeException)"; 
                        main.log_turbowin_system_message(message_a);
                     }
                    
                  } // public void serialEvent(SerialPortEvent spe)
              });
            } // if (GPS_serialPort.isOpen())
            
            return null;               
               
         } // protected Void doInBackground() throws Exception
         
         
         //
         // NB url: https://stackoverflow.com/questions/6523623/graceful-exception-handling-in-swing-worker
         //         You should NOT try to catch exceptions in the background thread but rather let them pass through to the SwingWorker itself, 
         //         and then you can get them in the done() method by calling get()which normally returns the result of doInBackground() 
         //         (Voidin your situation). If an exceptionwas thrown in the background thread then get() will throw it, wrapped inside an ExecutionException.
         // NB see above: when testing it seems that the NegativeArraySizeException wasn't passed to the Done method!! 
         // NB not sure the program will ever execute the code below
         //
         // Executed in EDT
         //
         @Override
         protected void done() 
         {
            try 
            {
               //get();
               String message = get();
               
               if (message != null)
               { 
                  if (message.length() > 0)
                  {
                     main.log_turbowin_system_message(message);
                  }
               }
            } 
            catch (ExecutionException ex) 
            {
               String message = "[GPS] " + "execution excption"; 
               main.log_turbowin_system_message(message);
            } 
            catch (InterruptedException ex) 
            {
               String message = "[GPS] " + "interruption exception"; 
               main.log_turbowin_system_message(message);
            }
         } // protected void done()          
            
      //}.execute(); // new SwingWorker<String, String>()
   
}


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/* NB DEZE WERKT OOK GOED !!! [hier wordt niet alles in de achtergroond afgehandeld)
private class RS232_GPS_Class_Receive_Data_2 extends SwingWorker<String, String>
{
      //new SwingWorker<String, String>()
      //{
         @Override
         protected String doInBackground() throws Exception
         {
            main.GPS_serialPort.openPort();                  // NB will NOT be closed in Function:  File_Exit_menu_actionPerformd() [main.java] ????
            if (main.GPS_serialPort.isOpen())
            {
               String hulp_parity = "";
               if (GPS_NMEA_0183_parity == 0)
               {
                  hulp_parity = "none";
               }
               else if (GPS_NMEA_0183_parity == 1)
               {
                  hulp_parity = "odd";
               }
               else if (GPS_NMEA_0183_parity == 2)
               {
                  hulp_parity = "even";
               }          
               //System.out.println("[GPS] " + GPS_serialPort.getPortName() + " trying to open with " + String.valueOf(main.GPS_bits_per_second) + " " + hulp_parity + " " + String.valueOf(GPS_NMEA_0183_data_bits) + " " + String.valueOf(GPS_NMEA_0183_stop_bits));
               System.out.println("[GPS] " + GPS_defaultPort_descriptive + " trying to open with " + String.valueOf(main.GPS_bits_per_second) + " " + hulp_parity + " " + String.valueOf(GPS_NMEA_0183_data_bits) + " " + String.valueOf(GPS_NMEA_0183_stop_bits));
               main.GPS_serialPort.setComPortParameters(main.GPS_bits_per_second, GPS_NMEA_0183_data_bits, GPS_NMEA_0183_stop_bits, GPS_NMEA_0183_parity);
               main.GPS_serialPort.setFlowControl(GPS_NMEA_0183_flow_control);
                  
		      } // if (GPS_serialPort.isOpen())
            //catch (SerialPortException ex) 
            else        
            {
               System.out.println("[GPS] Couldn't open " +  GPS_defaultPort_descriptive);
            }               
   
            if (main.GPS_serialPort.isOpen())
            {
               main.GPS_serialPort.addDataListener(new SerialPortDataListener() 
               {
                  @Override
                  public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                  
                  
                  StringBuilder message = new StringBuilder();
                  
                  @Override
                  public void serialEvent(SerialPortEvent event)
                  {
                     String ontvangen_GPS_string = "";
                  
                     if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                     {
                        return;
                     }
                     
                     int numRead;
                     byte[] newData = null;
                     try
                     {
                        newData = new byte[main.GPS_serialPort.bytesAvailable()];
                        numRead = main.GPS_serialPort.readBytes(newData, newData.length);                  
                     }
                     catch (NegativeArraySizeException ex) 
                     {
                         numRead = -1;
                        // NB don't modify the GUI anywhere except for on the event-dispatch-thread.!! (so not here in the function doinBackground())
                     }                     
                     
                  
                     if (numRead > 0)
                     {
                        for (byte b: newData) 
                        {
                           if ((b == '\r' || b == '\n') && message.length() > 0) 
                           {
                              ontvangen_GPS_string = message.toString();
                              
                              // publish: Sends data chunks to the process(java.util.List) method. This method is to be used from inside the doInBackground method to deliver intermediate results for processing on the Event Dispatch Thread inside the process method.
                              //          Because the process method is invoked asynchronously on the Event Dispatch Thread  multiple invocations to the publish method might occur before the process method is executed. For performance purposes all these invocations are coalesced into one invocation with concatenated arguments.
                              //
                              // For example:
                              //
                              // publish("1");
                              // publish("2", "3");
                              // publish("4", "5", "6");
                              //
                              // might result in: process("1", "2", "3", "4", "5", "6")
  
                              publish(new String[] { ontvangen_GPS_string });
                              
                              message.setLength(0);
                           } // if ((b == '\r' || b == '\n') && message.length() > 0) 
                           else 
                           {
                              message.append((char)b);
                           }
                        } // for (byte b: newData)                     
                        
                        //ontvangen_GPS_string = new String(newData, StandardCharsets.UTF_8);
                     
                        //System.out.println("Read " + numRead + " bytes.");
                     
                        //publish(new String[] { ontvangen_GPS_string });
                     } // if (numRead > 0)       
                     else if (numRead == -1)
                     {
                        publish(new String[] { "interruption or excecution error\n" });
                        
                        // NB if publish error (see above) is not working (no message) the statemnts below do also not work (also no error message on screen or in the log file)
                        //String message = "[GPS] " + "interruption or execution exception (NegativeArraySizeException)"; 
                        //main.log_turbowin_system_message(message);
                     } // else if (numRead == -1)
                    
                  } // public void serialEvent(SerialPortEvent spe)
              });
            } // if (GPS_serialPort.isOpen())
            
            return null;               
               
         } // protected Void doInBackground() throws Exception
         
         
         //
         // NB url: https://stackoverflow.com/questions/6523623/graceful-exception-handling-in-swing-worker
         //         You should NOT try to catch exceptions in the background thread but rather let them pass through to the SwingWorker itself, 
         //         and then you can get them in the done() method by calling get()which normally returns the result of doInBackground() 
         //         (Voidin your situation). If an exceptionwas thrown in the background thread then get() will throw it, wrapped inside an ExecutionException.
       
         @Override
         protected void process(List<String> data)
         {
            String total_GPS_string;        
            
            // process: Receives data chunks from the publish method asynchronously on the Event Dispatch Thread.
            for (String ontvangen_GPS_string: data)
            {
               // NB altijd in for loop omdat meerdere ontbvangen_SMD_string's verzameld kunnen zijn voordat het hier procesed wordt(inherent aan SwingWorker)
               //System.out.println("ontvangen_SMD_string = " + ontvangen_SMD_string);   // bv martin

               total_GPS_string = ontvangen_GPS_string;

               //System.out.print(total_GPS_string);
               //System.out.println(total_string); // NB zo zie je ruwweg hoeveel bytes dat telkens beschikbaar zijn
                  
               // error logging
               if (total_GPS_string.indexOf("error") != -1)
               {
                  String message = "[GPS] " + "interruption or execution exception"; 
                  main.log_turbowin_system_message(message);
               }  
         
               // update main screen label
               // ...
               // ......
         
               
               if (main.RS232_GPS_sentence == 1)         // RMC
               {
                  if ((total_GPS_string.indexOf("$GPRMC") != -1) || (total_GPS_string.indexOf("$GLRMC") != -1))  // (GP for a GPS unit, GL for a GLONASS)
                  {
                     RS232_GPS_NMEA_0183_RMC(total_GPS_string);
                  }
               } // if (main.RS232_GPS_sentence == 1)
               else if (main.RS232_GPS_sentence == 2)     // GGA; could be an alternative in case of NMEA 2000 + converter
               {
                  if ((total_GPS_string.indexOf("$GPGGA") != -1) || (total_GPS_string.indexOf("$GLGGA") != -1))  // (GP for a GPS unit, GL for a GLONASS)
                  {
                     RS232_GPS_NMEA_0183_GGA(total_GPS_string);
                  }
               } // else if (main.RS232_GPS_sentence == 2)               
             
            } // for (String ontvangen_GPS_string: data)
         } //  protected void process(List<String> data)
      //}.execute(); // new SwingWorker<String, String>()
   
   
}
*/





/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
public void RS232_GPS_NMEA_0183_initComponents()
{
   // checking/looking for the correct com port
   //
   int completed_checks_serial_ports = 0;
   while ((GPS_defaultPort == null) && (completed_checks_serial_ports < MAX_COMPLETED_GPS_PORT_CHECKS))
   {
      RS232_GPS_NMEA_0183_Check_Serial_Ports(completed_checks_serial_ports);               // GPS
      completed_checks_serial_ports++;
   }  
   
   
   if (GPS_defaultPort != null)
   {   
      //System.out.println("+++ GPS_defaultPort = " + GPS_defaultPort);
      
      // info text on (bottom) main screen 
      main.jLabel6.setText(main.APPLICATION_NAME + " receiving GPS data via serial communication.....");
      
      //GPS_serialPort = new SerialPort(GPS_defaultPort);  // NB GPS_defaultPort was determined in Function: RS232_GPS_NMEA_0183_Check_Serial_Ports()
      //final SerialPort GPS_serialPort  = SerialPort.getCommPort(GPS_defaultPort); // // NB GPS_defaultPort was determined in Function: RS232_GPS_NMEA_0183_Check_Serial_Ports()
      main.GPS_serialPort  = SerialPort.getCommPort(GPS_defaultPort);
      
      new SwingWorker<String, String>()
      {
         @Override
         protected String doInBackground() throws Exception
         {
            main.GPS_serialPort.openPort();                  // NB will NOT be closed in Function:  File_Exit_menu_actionPerformd() [main.java] ????
            if (main.GPS_serialPort.isOpen())
            {
               String hulp_parity = "";
               if (GPS_NMEA_0183_parity == 0)
               {
                  hulp_parity = "none";
               }
               else if (GPS_NMEA_0183_parity == 1)
               {
                  hulp_parity = "odd";
               }
               else if (GPS_NMEA_0183_parity == 2)
               {
                  hulp_parity = "even";
               }          
               //System.out.println("[GPS] " + GPS_serialPort.getPortName() + " trying to open with " + String.valueOf(main.GPS_bits_per_second) + " " + hulp_parity + " " + String.valueOf(GPS_NMEA_0183_data_bits) + " " + String.valueOf(GPS_NMEA_0183_stop_bits));
               System.out.println("[GPS] " + GPS_defaultPort_descriptive + " trying to open with " + String.valueOf(main.GPS_bits_per_second) + " " + hulp_parity + " " + String.valueOf(GPS_NMEA_0183_data_bits) + " " + String.valueOf(GPS_NMEA_0183_stop_bits));
               main.GPS_serialPort.setComPortParameters(main.GPS_bits_per_second, GPS_NMEA_0183_data_bits, GPS_NMEA_0183_stop_bits, GPS_NMEA_0183_parity);
               main.GPS_serialPort.setFlowControl(GPS_NMEA_0183_flow_control);
                  
               // Preparing a mask. In a mask, we need to specify the types of events that we want to track.
               // Well, for example, we need to know what came some data, thus in the mask must have the
               // following value: MASK_RXCHAR. If we, for example, still need to know about changes in states 
               // of lines CTS and DSR, the mask has to look like this: SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR
               //int mask = SerialPort.MASK_RXCHAR;
                  
               //Set the prepared mask
               //GPS_serialPort.setEventsMask(mask);   
                  
               //System.out.println("+++ passed try block");
                  
		      } // if (GPS_serialPort.isOpen())
            //catch (SerialPortException ex) 
            else        
            {
               //System.out.println("[GPS] " + ex);
               //System.out.println("[GPS] Couldn't open " +  main.GPS_serialPort.getDescriptivePortName());
               System.out.println("[GPS] Couldn't open " +  GPS_defaultPort_descriptive);
            }               
   
            if (main.GPS_serialPort.isOpen())
            {
               main.GPS_serialPort.addDataListener(new SerialPortDataListener() 
               {
                  @Override
                  public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                  
                  
                  StringBuilder message = new StringBuilder();
                  
                  @Override
                  public void serialEvent(SerialPortEvent event)
                  {
                     String ontvangen_GPS_string = "";
                  
                     if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                     {
                        return;
                     }
                     
                     //byte[] newData = new byte[main.GPS_serialPort.bytesAvailable()];
                     //int numRead = main.GPS_serialPort.readBytes(newData, newData.length);
                     int numRead;
                     byte[] newData = null;
                     try
                     {
                        newData = new byte[main.GPS_serialPort.bytesAvailable()];
                        numRead = main.GPS_serialPort.readBytes(newData, newData.length);                  
                     }
                     catch (NegativeArraySizeException ex) 
                     {
                         numRead = -1;
                        // NB don't modify the GUI anywhere except for on the event-dispatch-thread.!! (so not here in the function doinBackground())
                     }                     
                     
                  
                     if (numRead > 0)
                     {
                        for (byte b: newData) 
                        {
                           if ((b == '\r' || b == '\n') && message.length() > 0) 
                           {
                              ontvangen_GPS_string = message.toString();
                            
                              // NB mail Henry Kleta 22-10-2015
                              //    ...And if you have the choice: Go for the NMEA RMC sentence.
                              //    One sentence, everyting in there and one of the view sentences that (nearly) all GPSes provide.
                              if (main.RS232_GPS_sentence == 1)         // RMC
                              {
                                 if ((ontvangen_GPS_string.indexOf("$GPRMC") != -1) || (ontvangen_GPS_string.indexOf("$GLRMC") != -1))  // (GP for a GPS unit, GL for a GLONASS)
                                 {
                                    RS232_GPS_NMEA_0183_RMC(ontvangen_GPS_string);
                                 
                                    // for checking that the displayed data is not obsolete (i.e. not updated last 5 minutes)
                                    //last_new_GPS_data_received_TimeMillis = System.currentTimeMillis(); // see also Function: .... [main_RS232_422.java]
                                 }
                              } // if (main.RS232_GPS_sentence == 1)
                              else if (main.RS232_GPS_sentence == 2)     // GGA; could be an alternative in case of NMEA 2000 + converter
                              {
                                 if ((ontvangen_GPS_string.indexOf("$GPGGA") != -1) || (ontvangen_GPS_string.indexOf("$GLGGA") != -1))  // (GP for a GPS unit, GL for a GLONASS)
                                 {
                                    RS232_GPS_NMEA_0183_GGA(ontvangen_GPS_string);
                                 }
                              } // else if (main.RS232_GPS_sentence == 2)
                              
                              message.setLength(0);
                           } // if ((b == '\r' || b == '\n') && message.length() > 0) 
                           else 
                           {
                              message.append((char)b);
                           }
                        } // for (byte b: buffer)                     
                     } // if (numRead > 0)  
                     else if (numRead == -1)
                     {
                        //publish(new String[] { "interruption or excecution error\n" });
                        String message_a = "[GPS] " + "NegativeArraySizeException (interruption or execution error)"; 
                        main.log_turbowin_system_message(message_a);
                     }
                    
                  } // public void serialEvent(SerialPortEvent spe)
              });
            } // if (GPS_serialPort.isOpen())
            
            return null;               
               
         } // protected Void doInBackground() throws Exception
         
         
         //
         // NB url: https://stackoverflow.com/questions/6523623/graceful-exception-handling-in-swing-worker
         //         You should NOT try to catch exceptions in the background thread but rather let them pass through to the SwingWorker itself, 
         //         and then you can get them in the done() method by calling get()which normally returns the result of doInBackground() 
         //         (Voidin your situation). If an exceptionwas thrown in the background thread then get() will throw it, wrapped inside an ExecutionException.
         // NB see above: when testing it seems that the NegativeArraySizeException wasn't passed to the Done method!! 
         // NB not sure the program will ever execute the code below
         //
         // Executed in EDT
         //
         @Override
         protected void done() 
         {
            try 
            {
               get();
            } 
            catch (ExecutionException ex) 
            {
               //String message = "[GPS] " + ex.getMessage(); 
               String message = "[GPS] " + "execution excption"; 
               main.log_turbowin_system_message(message);
               //message = "barometer connection execution exception (pc sleep mode?); restart this program";
               //JOptionPane.showMessageDialog(null, message, main.APPLICATION_NAME, JOptionPane.ERROR_MESSAGE);                 
            } 
            catch (InterruptedException ex) 
            {
               String message = "[GPS] " + "interruption exception"; 
               main.log_turbowin_system_message(message);
               //message = "barometer connection interrupted; restart this program";
               //JOptionPane.showMessageDialog(null, message, main.APPLICATION_NAME, JOptionPane.ERROR_MESSAGE);
            }
         } // protected void done()          
            
      }.execute(); // new SwingWorker<String, String>()
   } // if (GPS.defaultPort != null)
   
} // public void RS232_GPS_initComponents()
*/


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void WiFi_Check_Connection_b()
{
   System.out.println("start Wifi_Check_connection_b");
   
   
 String host = "192.168.178.18";               // ALLEEN VOOR TEST
         int port = 80;
         try (Socket socket = new Socket(host, port);
              InputStream is = socket.getInputStream();
              BufferedReader in = new BufferedReader(new InputStreamReader(is));
              OutputStream os = socket.getOutputStream();
              PrintWriter out = new PrintWriter(os);
            ) 
         {
            // test WiFiSystem.out.printf("Sending 'TurboWin' command to %s%n", host);
         //out.print("dm wifi HTTP/1.1\r\n\r\n");
         //out.flush();
         String in_line;

         //while ((in_line = in.readLine()).length() != 0) {}         // skip past the HTTP Headers

         while ((in_line = in.readLine()) != null) 
         {     
            // read the TurboWin information
            System.out.println(in_line);
         }
         socket.close();
            
         
         } 
         catch (UnknownHostException e) 
         {
            System.out.println("Don't know about host " + host);
            // System.exit(1);
         } 
         catch (IOException e) 
         {
            System.out.println("Couldn't get I/O for the connection to " + host);
            //System.exit(1);
         }            

}
*/



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void WiFi_Check_Connection_UDP_c()
{
   
   System.out.println("start Wifi_Check_connection_UDP_c");
      
}
*/


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void WiFi_Check_Connection_UDP_b()
{
   // https://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
   System.out.println("start Wifi_Check_connection_UDP_b");
   
DatagramSocket socket = null;
   try {
      try {
         socket = new DatagramSocket(10110, InetAddress.getByName("0.0.0.0"));
      } catch (UnknownHostException ex) {
         Logger.getLogger(main_RS232_RS422.class.getName()).log(Level.SEVERE, null, ex);
      }
   } catch (SocketException ex) {
      Logger.getLogger(main_RS232_RS422.class.getName()).log(Level.SEVERE, null, ex);
   }
   try {
      //DatagramSocket socket = new DatagramSocket(10110, InetAddress.getByName("192.168.178.13"));
      socket.setBroadcast(true);
   } catch (SocketException ex) {
      Logger.getLogger(main_RS232_RS422.class.getName()).log(Level.SEVERE, null, ex);
   }
   try {
      System.out.println("Listen on " + socket.getLocalAddress() + " from " + socket.getInetAddress() + " port " + socket.getBroadcast());
   } catch (SocketException ex) {
      Logger.getLogger(main_RS232_RS422.class.getName()).log(Level.SEVERE, null, ex);
   }
         byte[] buf = new byte[512];
         DatagramPacket packet = new DatagramPacket(buf, buf.length);
   
         while (true) 
         {
            System.out.println("Waiting for data");
      try {
         socket.receive(packet);
      } catch (IOException ex) {
         Logger.getLogger(main_RS232_RS422.class.getName()).log(Level.SEVERE, null, ex);
      }

            System.out.println("Data received: ");
            int endidx = 0;
            for (; endidx < buf.length; endidx++) if (buf[endidx] == '*') break;
            endidx += 3;
            int idx = 0;
            for (int i = 0; i<endidx; i++) System.out.print((char) buf[idx++]);
            System.out.println();
        }   

}
*/


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private class WiFi_Class_Receive_UDP extends SwingWorker<String, String>
{
   //new SwingWorker<String, String>()
      //String line;
      boolean retry = false;
      
      @Override
      protected String doInBackground() throws Exception
      {
         try (DatagramSocket socket = new DatagramSocket(10110, InetAddress.getByName("0.0.0.0"));)   
         {
            String ontvangen_SMD_string = "";
            
            
            socket.setBroadcast(true);
         
            //System.out.println("Listen on " + socket.getLocalAddress() + " from " + socket.getInetAddress() + " port " + socket.getBroadcast());
            byte[] buf = new byte[512];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (true) 
            {
               ontvangen_SMD_string = "";
               //System.out.println("Waiting for data");
               socket.receive(packet);

               //System.out.println("Data received: ");
               
               int endidx = 0;
               for (; endidx < buf.length; endidx++) 
               {
                  if (buf[endidx] == '*') break;
               }
               
               endidx += 3;
               int idx = 0;
               for (int i = 0; i < endidx; i++) 
               {
                  ontvangen_SMD_string += (char)buf[idx];
                  //System.out.print((char) buf[idx]);
                  idx++;
               }
               ontvangen_SMD_string += "\n";
               //System.out.println();
               
               
               publish(new String[] { ontvangen_SMD_string });
               
            } //  while (true)
         } // try
         catch (SocketException e) 
         {
            String message = "[WIFI] connection error: " + e;
            main.log_turbowin_system_message(message);
         } 
         catch (IOException e)
         {
            //System.out.println("Timeout. Client is closing.");
            String message = "[WIFI] IO error: " + e;
            main.log_turbowin_system_message(message);            
         }
         
         
         return null;
         
      } // protected String doInBackground() throws Exception
    
      @Override
      protected void process(List<String> data)
      {
         // process: Receives data chunks from the publish method asynchronously on the Event Dispatch Thread.
         for (String ontvangen_SMD_string: data)
         {
            // NB altijd in for loop omdat meerdere ontbvangen_SMD_string's verzameld kunnen zijn voordat het hier procesed wordt(inherent aan SwingWorker)
            //System.out.println("ontvangen_SMD_string = " + ontvangen_SMD_string);   // bv martin

            main.total_string = ontvangen_SMD_string;
            
            System.out.print(main.total_string);
            //System.out.println(total_string); // NB zo zie je ruwweg hoeveel bytes dat telkens beschikbaar zijn

            RS232_write_sensor_data_to_file();                             // PTB220, PTB330, MintakaDuo, Mintaka Star USB, Mintaka Star WiFi
           
            // update main screen with application name + mode and date time last received data
            String info = "";
            if ((main_RS232_RS422.test_record.equals("")) || (main_RS232_RS422.test_record == null)) 
            {
              info = "no data";
            } 
            else 
            {
               info = main_RS232_RS422.test_record;
            }     
            main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received barometer data via WiFi: " + info);

            // for timer checking the data is not obsolate (see Function: RS232_WiFi_init_new_barometer_received_check_timer)
            last_new_data_received_TimeMillis = System.currentTimeMillis(); 
            
            // update the date and time on the main screen (and the global date time var's)
            // NB if during start up the date and time displayed in the pupup message was not comfirmed by the user
            //    it is assumed that the pc is not running on the correct time so then do not update the date time automatically
            if (main.use_system_date_time_for_updating == true)
            {  
               set_datetime_while_collecting_sensor_data();
            }
                  
            // WOW enabled (and not 'only publish if manual obs was send') and a complete sensor data string was received
            if ((main.WOW == true) && (!main.WOW_reporting_interval.equals(main.WOW_REPORTING_INTERVAL_MANUAL)) && (main.total_string.indexOf("\n") != -1))
            {
               cal_WOW_systeem_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // system date time in UTC of this moment
               cal_WOW_systeem_datum_tijd.getTime();                                             // now effective
               int WOW_system_minute_local = cal_WOW_systeem_datum_tijd.get(Calendar.MINUTE);
               cal_WOW_systeem_datum_tijd = null; 
                  
               //if (WOW_system_minute_local % Integer.valueOf(main.WOW_reporting_interval) == 0)      // e.g. every 10 minutes (WOW_reporting_interval);  
               // NB avoid: retrieving sensor data exactly the same time as APR!!!! -> issue at whole hours
               if ((WOW_system_minute_local -1) % Integer.valueOf(main.WOW_reporting_interval) == 0)      // e.g. every 10 minutes (WOW_reporting_interval);  e.g: 1, 11, 21, 31 etc min past every hour    
               {
                  // logging
                  String message = "[WOW] scheduled upload"; 
                  main.log_turbowin_system_message(message);
                        
                  // checking
                  boolean WOW_settings_ok = RS232_check_WOW_settings();
                         
                  if (WOW_settings_ok)
                  {   
                     // read the barometer data which was saved before by TurboWin+ from a sensor data file
                     if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)       // PTB220 or PTB330
                     {
                        RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                     }
                     else if (main.RS232_connection_mode == 4)                                     // MintakaDuo
                     {
                        RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                     }
                     else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                     {
                        //RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                        boolean StarX = false;
                        RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry, StarX);
                     } 
                     else if (main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8)  // Mintaka StarX (USB or WiFi)
                     {
                        boolean StarX = true;
                        RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry, StarX);
                     } 
                  } // if (WOW_settings_ok)
               }   
            } // if ((main.WOW == true) && (!main.WOW_reporting_interval.equals(main.WOW_REPORTING_INTERVAL_MANUAL)) && (main.total_string.indexOf("\n") != -1))

            // APR (Automated Pressure Reporting)
            if ((main.APR == true) && (main.total_string.indexOf("\n") != -1))
            {
               cal_APR_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));   // system date time in UTC of this moment
               cal_APR_system_date_time.getTime();                                               // now effective
               int APR_system_hour_local = cal_APR_system_date_time.get(Calendar.HOUR_OF_DAY);   // HOUR_OF_DAY is used for the 24-hour clock. E.g., at 10:04:15.250 PM the HOUR_OF_DAY is 22.
               int APR_system_minute_local = cal_APR_system_date_time.get(Calendar.MINUTE);
                  
               if ((APR_system_minute_local == 0) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
               {
                  retry = false;
                        
                  // logging
                  String message = "[APR] scheduled upload"; 
                  main.log_turbowin_system_message(message);
                        
                  // checking
                  boolean APR_settings_ok = RS232_check_APR_settings();
                  boolean GPS_date_time_ok = true;
                        
                  if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6 || main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8) // Mintaka Star or StarX  (USB or WiFi)
                  {
                     // NB Mintaka Star and StarX saved data do not contain date/time information so it is not possible to check this
                     //    so it is made always true
                     GPS_date_time_ok = true;
                  }
                  else
                  {
                     GPS_date_time_ok = main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing("APR");
                  }
                        
                  if (APR_settings_ok && GPS_date_time_ok) // GPS date-time was compared with system date-time                    
                  {
                     // NB in this stage we are sure the date-time is correct! + we are sure position is available!
                             
                     // read the barometer data which was saved before by TurboWin+ from a sensor data file
                     if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)       // PTB220 or PTB330
                     {
                        RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                     }
                     else if (main.RS232_connection_mode == 4)                                     // MintakaDuo
                     {
                        RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                     }
                     else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                     {
                        //RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                        boolean StarX = false;
                        RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry, StarX);
                     }      
                     else if (main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8)  // Mintaka StarX (USB or WiFi)
                     {
                        boolean StarX = true;
                        RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry, StarX);
                     } 
                  } // if (APR_settings_ok && GPS_date_time_ok) 
               } // if ((APR_system_minute_local == 0) etc.
               else if ((APR_server_response_code == main.RESPONSE_NO_INTERNET) && (APR_system_minute_local == APR_RETRY_MINUTES) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
               {
                  retry = true;
                        
                  // logging
                  String message = "[APR] retry scheduled upload"; 
                  main.log_turbowin_system_message(message);
                        
                  // checking
                  boolean APR_settings_ok = RS232_check_APR_settings();
                  boolean GPS_date_time_ok = true;
                        
                  if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6 || main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8) // Mintaka Star or StarX (USB or WiFi)
                  {
                     // NB Mintaka Star and StarX saved data do not contain date/time information so it is not possible to check this
                     //    so it is made always true
                     GPS_date_time_ok = true;
                  }
                  else
                  {
                     GPS_date_time_ok = main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing("APR");
                  }
                     
                  if (APR_settings_ok && GPS_date_time_ok) // GPS date-time was compared with system date-time                    
                  {
                     // NB in this stage we are sure the date-time is correct! + we are sure position is available!
                             
                     // read the barometer data which was saved before by TurboWin+ from a sensor data file
                     if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)       // PTB220 or PTB330
                     {
                        RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                     }
                     else if (main.RS232_connection_mode == 4)                                     // MintakaDuo
                     {
                        RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                     }
                     else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                     {
                        //RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                        boolean StarX = false;
                        RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry, StarX);
                     } 
                     else if (main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8)  // Mintaka StarX (USB or WiFi)
                     {
                        boolean StarX = true;
                        RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry, StarX);
                     } 
                  } // if (APR_settings_ok && GPS_date_time_ok) 
                        
                  // reset (to be sure)
                  APR_server_response_code = 0;
                        
               } // else if ((APR_server_response_code = main.RESPONSE_NO_INTERNET) etc.
               else if ((APR_server_response_code == main.RESPONSE_NO_INTERNET) && (APR_system_minute_local < APR_RETRY_MINUTES) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
               {
                  // text will be visible only a few minutes (till the retry upload)
                  main.jTextField4.setText("[APR] retry automated pressure report upload within a few minutes");
               }
               else
               {
                  // text with next APR update bottom status line TurboWin+ main screen (updated every minute)
                  for (int i = (APR_system_hour_local + 1); i < (APR_system_hour_local + 1 + Integer.valueOf(main.APR_reporting_interval)); i++)
                  {
                     // 24 hours or higher makes no difference for this computation
                     if (i % Integer.valueOf(main.APR_reporting_interval) == 0)
                     {
                        cal_APR_system_date_time.add(Calendar.HOUR_OF_DAY, i - APR_system_hour_local);   // add 1 - 5 hours
                        cal_APR_system_date_time.getTime();
                                 
                        String next_APR_string = "[APR] next automated air pressure report upload: " + cal_APR_system_date_time.get(Calendar.DAY_OF_MONTH) + " " + main.convert_month(cal_APR_system_date_time.get(Calendar.MONTH)) + " " + cal_APR_system_date_time.get(Calendar.YEAR) + " " + cal_APR_system_date_time.get(Calendar.HOUR_OF_DAY) + ".00 UTC"; 
                                 
                        main.jTextField4.setText(next_APR_string);
                        break;
                     }
                  } // for (int i = (APR_system_hour_local + 1); etc.
                           
               } // else
                        
               cal_APR_system_date_time = null; 
                        
            } // if ( (main.APR == true) && (main.total_string.indexOf("\n") != -1))
         } //  for (String ontvangen_SMD_string: data)
         
      } // protected void process(List<String> data)
 

}




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private class Ethernet_Class_Receive_UDP extends SwingWorker<Void, String>
{     
      // AWS ethernet (LAN) only
      //
      // called from: - Ethernet_initComponents()
      //              - RS422_init_new_aws_data_received_check_timer()
      //
      //
      // NB OMC-140 LAN uses port 60004 and multicast ip adress 239.192.0.4   
      // eg https://examples.javacodegeeks.com/core-java/net/multicastsocket-net/java-net-multicastsocket-example/
      //    http://www.baeldung.com/java-broadcast-multicast
      //    https://en.wikipedia.org/wiki/Multicast_address
      //
   
      @Override
      protected Void doInBackground() throws Exception
      {
         final String INET_ADDR = "239.192.0.4";              // multicast IP group
         final int PORT         = 60004;
         InetAddress group_udp = InetAddress.getByName(INET_ADDR);
         //group_udp = InetAddress.getByName(INET_ADDR);

         //try (MulticastSocket socket_udp = new MulticastSocket(PORT)) 
         try (MulticastSocket socket_udp = new MulticastSocket(PORT))  
         {
            //socket.setBroadcast(true);
            socket_udp.joinGroup(group_udp);
         
            //byte[] buf = new byte[512
            byte[] buf = new byte[2048];                                 // 512 is not enough!! must cover the length of the whole $PEUMB string
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (true) 
            {
               socket_udp.receive(packet);
               String ontvangen_SMD_string = new String(packet.getData(), 0, packet.getLength());
               
               publish(new String[] { ontvangen_SMD_string });
               
            } // while (true)
         } // try
         catch (SocketException e) 
         {
            String message = "[AWS] connection error: " + e;
            main.log_turbowin_system_message(message);
         } 
         catch (IOException e)
         {
            String message = "[AWS] IO error: " + e;
            main.log_turbowin_system_message(message);            
         }
         
         
         return null;
      } // protected Void doInBackground() throws Exception
      
      
      @Override
      protected void process(List<String> data)
      {
         // process: Receives data chunks from the publish method asynchronously on the Event Dispatch Thread.
         for (String ontvangen_SMD_string: data)
         {
            // NB altijd in for loop omdat meerdere ontbvangen_SMD_string's verzameld kunnen zijn voordat het hier procesed wordt(inherent aan SwingWorker)
            //System.out.println("ontvangen_SMD_string = " + ontvangen_SMD_string);   // bv martin

            main.total_string = ontvangen_SMD_string;
            
            System.out.print(main.total_string);

            RS422_V3_write_sensor_data_to_file();     // NB also main.jLabel4 will be updated in this function and also last_new_data_received_TimeMillis
            
            // update main screen with application name + mode and date time last received data
            //String info = "";
            //if ((main_RS232_RS422.test_record.equals("")) || (main_RS232_RS422.test_record == null)) 
            //{
            //  info = "no data";
            //} 
            //else 
            //{
            //   info = main_RS232_RS422.test_record;
            //}     
            //main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received sensor data: " + info);

            // for timer checking the data is not obsolate (see Function: RS232_WiFi_init_new_barometer_received_check_timer)
            //last_new_data_received_TimeMillis = System.currentTimeMillis(); 
            
            // update the date and time on the main screen (and the global date time var's)
            // NB if during start up the date and time displayed in the pupup message was not comfirmed by the user
            //    it is assumed that the pc is not running on the correct time so then do not update the date time automatically
            if (main.use_system_date_time_for_updating == true)
            {  
               set_datetime_while_collecting_sensor_data();
            }
         } // for (String ontvangen_SMD_string: data)
      } // protected void process(List<String> data)
      
      
      
      @Override
      protected void done()
      {
         
      } // protected void done()
      
}








/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void WiFi_Receive_UDP()
{
   //System.out.println("+++ " + "WiFi_receiveUDP()");
   // info text on (bottom) main screen 
   //main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", receiving barometer sensor data via WiFi communication.....");

   // called from: WiFi_initComponents() [main_RS232_RS422.java]
   // NB to kill the swingworker see: https://stackoverflow.com/questions/8083768/stop-cancel-swingworker-thread
   //     0r https://stackoverflow.com/questions/20427697/trying-to-stop-swingworker
   //     etc.
   
   
   
   new SwingWorker<String, String>()
   //WiFi_worker = new SwingWorker<String, String>()
   {
      //String line;
      boolean retry = false;
      
      @Override
      protected String doInBackground() throws Exception
      {
         try (DatagramSocket socket = new DatagramSocket(10110, InetAddress.getByName("0.0.0.0"));)   
         {
            String ontvangen_SMD_string = "";
            
            
            socket.setBroadcast(true);
         
            //System.out.println("Listen on " + socket.getLocalAddress() + " from " + socket.getInetAddress() + " port " + socket.getBroadcast());
            byte[] buf = new byte[512];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (true) 
            {
               ontvangen_SMD_string = "";
               //System.out.println("Waiting for data");
               socket.receive(packet);

               //System.out.println("Data received: ");
               
               int endidx = 0;
               for (; endidx < buf.length; endidx++) 
               {
                  if (buf[endidx] == '*') break;
               }
               
               endidx += 3;
               int idx = 0;
               for (int i = 0; i < endidx; i++) 
               {
                  ontvangen_SMD_string += (char)buf[idx];
                  //System.out.print((char) buf[idx]);
                  idx++;
               }
               ontvangen_SMD_string += "\n";
               //System.out.println();
               
               
               publish(new String[] { ontvangen_SMD_string });
               
            } //  while (true)
         } // try
         catch (SocketException e) 
         {
            String message = "[WIFI] connection error: " + e;
            main.log_turbowin_system_message(message);
         } 
         catch (IOException e)
         {
            //System.out.println("Timeout. Client is closing.");
            String message = "[WIFI] IO error: " + e;
            main.log_turbowin_system_message(message);            
         }
         
         
         return null;
         
      } // protected String doInBackground() throws Exception
    
      @Override
      protected void process(List<String> data)
      {
         // process: Receives data chunks from the publish method asynchronously on the Event Dispatch Thread.
         for (String ontvangen_SMD_string: data)
         {
            // NB altijd in for loop omdat meerdere ontbvangen_SMD_string's verzameld kunnen zijn voordat het hier procesed wordt(inherent aan SwingWorker)
            //System.out.println("ontvangen_SMD_string = " + ontvangen_SMD_string);   // bv martin

            main.total_string = ontvangen_SMD_string;
            
            System.out.print(main.total_string);
            //System.out.println(total_string); // NB zo zie je ruwweg hoeveel bytes dat telkens beschikbaar zijn

            RS232_write_sensor_data_to_file();                             // PTB220, PTB330, MintakaDuo, Mintaka Star USB, Mintaka Star WiFi
           
            // update main screen with application name + mode and date time last received data
            String info = "";
            if ((main_RS232_RS422.test_record.equals("")) || (main_RS232_RS422.test_record == null)) 
            {
              info = "no data";
            } 
            else 
            {
               info = main_RS232_RS422.test_record;
            }     
            main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received barometer data via WiFi: " + info);

            // for timer checking the data is not obsolate (see Function: RS232_WiFi_init_new_barometer_received_check_timer)
            last_new_data_received_TimeMillis = System.currentTimeMillis(); 
            
            // update the date and time on the main screen (and the global date time var's)
            // NB if during start up the date and time displayed in the pupup message was not comfirmed by the user
            //    it is assumed that the pc is not running on the correct time so then do not update the date time automatically
            if (main.use_system_date_time_for_updating == true)
            {  
               set_datetime_while_collecting_sensor_data();
            }
                  
            // WOW enabled (and not 'only publish if manual obs was send') and a complete sensor data string was received
            if ((main.WOW == true) && (!main.WOW_reporting_interval.equals(main.WOW_REPORTING_INTERVAL_MANUAL)) && (main.total_string.indexOf("\n") != -1))
            {
               cal_WOW_systeem_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // system date time in UTC of this moment
               cal_WOW_systeem_datum_tijd.getTime();                                             // now effective
               int WOW_system_minute_local = cal_WOW_systeem_datum_tijd.get(Calendar.MINUTE);
               cal_WOW_systeem_datum_tijd = null; 
                  
               //if (WOW_system_minute_local % Integer.valueOf(main.WOW_reporting_interval) == 0)      // e.g. every 10 minutes (WOW_reporting_interval);  
               // NB avoid: retrieving sensor data exactly the same time as APR!!!! -> issue at whole hours
               if ((WOW_system_minute_local -1) % Integer.valueOf(main.WOW_reporting_interval) == 0)      // e.g. every 10 minutes (WOW_reporting_interval);  e.g: 1, 11, 21, 31 etc min past every hour    
               {
                  // logging
                  String message = "[WOW] scheduled upload"; 
                  main.log_turbowin_system_message(message);
                        
                  // checking
                  boolean WOW_settings_ok = RS232_check_WOW_settings();
                         
                  if (WOW_settings_ok)
                  {   
                     // read the barometer data which was saved before by TurboWin+ from a sensor data file
                     if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)  // PTB220 or PTB330
                     {
                        RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                     }
                     else if (main.RS232_connection_mode == 4)                                // MintakaDuo
                     {
                        RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                     }
                     else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                     {
                        RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                     } 
                  } // if (WOW_settings_ok)
               }   
            } // if ((main.WOW == true) && (!main.WOW_reporting_interval.equals(main.WOW_REPORTING_INTERVAL_MANUAL)) && (main.total_string.indexOf("\n") != -1))

            // APR (Automated Pressure Reporting)
            if ((main.APR == true) && (main.total_string.indexOf("\n") != -1))
            {
               cal_APR_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));   // system date time in UTC of this moment
               cal_APR_system_date_time.getTime();                                               // now effective
               int APR_system_hour_local = cal_APR_system_date_time.get(Calendar.HOUR_OF_DAY);   // HOUR_OF_DAY is used for the 24-hour clock. E.g., at 10:04:15.250 PM the HOUR_OF_DAY is 22.
               int APR_system_minute_local = cal_APR_system_date_time.get(Calendar.MINUTE);
                  
               if ((APR_system_minute_local == 0) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
               {
                  retry = false;
                        
                  // logging
                  String message = "[APR] scheduled upload"; 
                  main.log_turbowin_system_message(message);
                        
                  // checking
                  boolean APR_settings_ok = RS232_check_APR_settings();
                  boolean GPS_date_time_ok = true;
                        
                  if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6) // Mintaka Star (USB or WiFi)
                  {
                     // NB Mintaka Star saved data do not contain date/time information so it is not possible to check this
                     //    so it is made always true
                     GPS_date_time_ok = true;
                  }
                  else
                  {
                     GPS_date_time_ok = main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing("APR");
                  }
                        
                  if (APR_settings_ok && GPS_date_time_ok) // GPS date-time was compared with system date-time                    
                  {
                     // NB in this stage we are sure the date-time is correct! + we are sure position is available!
                             
                     // read the barometer data which was saved before by TurboWin+ from a sensor data file
                     if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)  // PTB220 or PTB330
                     {
                        RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                     }
                     else if (main.RS232_connection_mode == 4)                                // MintakaDuo
                     {
                        RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                     }
                     else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                     {
                        RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                     }                           
                  } // if (APR_settings_ok && GPS_date_time_ok) 
               } // if ((APR_system_minute_local == 0) etc.
               else if ((APR_server_response_code == main.RESPONSE_NO_INTERNET) && (APR_system_minute_local == APR_RETRY_MINUTES) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
               {
                  retry = true;
                        
                  // logging
                  String message = "[APR] retry scheduled upload"; 
                  main.log_turbowin_system_message(message);
                        
                  // checking
                  boolean APR_settings_ok = RS232_check_APR_settings();
                  boolean GPS_date_time_ok = true;
                        
                  if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6) // Mintaka Star (USB or WiFi)
                  {
                     // NB Mintaka Star saved data do not contain date/time information so it is not possible to check this
                     //    so it is made always true
                     GPS_date_time_ok = true;
                  }
                  else
                  {
                     GPS_date_time_ok = main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing("APR");
                  }
                     
                  if (APR_settings_ok && GPS_date_time_ok) // GPS date-time was compared with system date-time                    
                  {
                     // NB in this stage we are sure the date-time is correct! + we are sure position is available!
                             
                     // read the barometer data which was saved before by TurboWin+ from a sensor data file
                     if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)  // PTB220 or PTB330
                     {
                        RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                     }
                     else if (main.RS232_connection_mode == 4)                                // MintakaDuo
                     {
                        RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                     }
                     else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                     {
                        RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                     } 
                  } // if (APR_settings_ok && GPS_date_time_ok) 
                        
                  // reset (to be sure)
                  APR_server_response_code = 0;
                        
               } // else if ((APR_server_response_code = main.RESPONSE_NO_INTERNET) etc.
               else if ((APR_server_response_code == main.RESPONSE_NO_INTERNET) && (APR_system_minute_local < APR_RETRY_MINUTES) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
               {
                  // text will be visible only a few minutes (till the retry upload)
                  main.jTextField4.setText("[APR] retry automated pressure report upload within a few minutes");
               }
               else
               {
                  // text with next APR update bottom status line TurboWin+ main screen (updated every minute)
                  for (int i = (APR_system_hour_local + 1); i < (APR_system_hour_local + 1 + Integer.valueOf(main.APR_reporting_interval)); i++)
                  {
                     // 24 hours or higher makes no difference for this computation
                     if (i % Integer.valueOf(main.APR_reporting_interval) == 0)
                     {
                        cal_APR_system_date_time.add(Calendar.HOUR_OF_DAY, i - APR_system_hour_local);   // add 1 - 5 hours
                        cal_APR_system_date_time.getTime();
                                 
                        String next_APR_string = "[APR] next automated air pressure report upload: " + cal_APR_system_date_time.get(Calendar.DAY_OF_MONTH) + " " + main.convert_month(cal_APR_system_date_time.get(Calendar.MONTH)) + " " + cal_APR_system_date_time.get(Calendar.YEAR) + " " + cal_APR_system_date_time.get(Calendar.HOUR_OF_DAY) + ".00 UTC"; 
                                 
                        main.jTextField4.setText(next_APR_string);
                        break;
                     }
                  } // for (int i = (APR_system_hour_local + 1); etc.
                           
               } // else
                        
               cal_APR_system_date_time = null; 
                        
            } // if ( (main.APR == true) && (main.total_string.indexOf("\n") != -1))
         } //  for (String ontvangen_SMD_string: data)
         
      } // protected void process(List<String> data)
 
   }.execute(); // new SwingWorker <Void, Void>()  
   //}
   //};
           //WiFi_worker.execute(); // new SwingWorker <Void, Void>() 
   
}
*/



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void WiFi_Check_Connection_UDP()
{
   // https://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
   System.out.println("start Wifi_Check_connection_UDP");
   
   main.total_string = "";
   
   new SwingWorker<String, Void>()
   {
      //String line;
            
      @Override
      protected String doInBackground() throws Exception
      {
         try (DatagramSocket socket = new DatagramSocket(10110, InetAddress.getByName("0.0.0.0"));)   
         {
            socket.setBroadcast(true);
         
            System.out.println("Listen on " + socket.getLocalAddress() + " from " + socket.getInetAddress() + " port " + socket.getBroadcast());
            byte[] buf = new byte[512];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (true) 
            {
               System.out.println("Waiting for data");
               socket.receive(packet);

               System.out.println("Data received: ");
               
               int endidx = 0;
               for (; endidx < buf.length; endidx++) 
               {
                  if (buf[endidx] == '*') break;
               }
               
               endidx += 3;
               int idx = 0;
               for (int i = 0; i<endidx; i++) 
               {
                  System.out.print((char) buf[idx++]);
               }
               
               System.out.println();
            }
            
         
         
         
   //    DatagramSocket socket = new DatagramSocket(10110, InetAddress.getByName("0.0.0.0"));
   //    socket.setBroadcast(true);
   //    System.out.println("Listen on " + socket.getLocalAddress() + " from " + socket.getInetAddress() + " port " + socket.getBroadcast());
   //    byte[] buf = new byte[512];
   //    DatagramPacket packet = new DatagramPacket(buf, buf.length);
   //    while (true) {
  ///         System.out.println("Waiting for data");
   //        socket.receive(packet);
//
   //        System.out.println("Data received: ");
   //        int endidx = 0;
   //        for (; endidx < buf.length; endidx++) if (buf[endidx] == '*') break;
   //        endidx += 3;
   //        int idx = 0;
   //        for (int i = 0; i<endidx; i++) System.out.print((char) buf[idx++]);
    //       System.out.println();
       }
    

         
         
///////////////////////////////////////////////////         
         
         
         

         //String in_line = "";
         
         
         //return in_line;
         
   
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
   //      try 
  //       {
  //          main.total_string = get();
  //       }
  //       catch (InterruptedException | ExecutionException ex) 
   //      {
  //          String message = "[WIFI] " + ex.toString();
   //         main.log_turbowin_system_message(message);
    //        main.total_string = "";
   //      } // catch
         
      } // protected void done()

   }.execute(); // new SwingWorker <Void, Void>()   
   
   
   
}
*/





/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void WiFi_Check_Connection()
{
   // https://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
   
   //System.out.println("start Wifi_Check_connection");
   
   main.total_string = "";
   
   new SwingWorker<String, Void>()
   {
      //String line;
            
      @Override
      protected String doInBackground() throws Exception
      {
         
///////////////////////////////////////         
 //InetAddress localhost = InetAddress.getLocalHost();
//        System.out.println(localhost);
//        byte[] ip = localhost.getAddress();/
// 
//        for (int i = 1; i <= 254; i++) {
//            ip[3] = (byte) i;
//            InetAddress address = InetAddress.getByAddress(ip);
//            if (address.isReachable(1000)) {
//                System.out.println(address .getHostAddress()+ " - Active");
//                System.out.println(address .getHostName());
//            } else if (!address.getHostAddress().equals(address.getHostName())) {
//                System.out.println(address.getHostAddress() + " - DNS lookup known..");
//            } else {
//                System.out.println(address.getHostAddress() + " - Not Active");
//            }
//        }         
         
         
///////////////////////////////////         
         
         
         
         
///////////////////////////////////////////////////         
         
         String host = "192.168.178.18";               // ALLEEN VOOR TEST
         int port = 80;
         try (Socket socket = new Socket(host, port);
              InputStream is = socket.getInputStream();
              BufferedReader in = new BufferedReader(new InputStreamReader(is));
              OutputStream os = socket.getOutputStream();
              PrintWriter out = new PrintWriter(os);
            ) 
         {
            // test WiFiSystem.out.printf("Sending 'TurboWin' command to %s%n", host);
         //out.print("dm wifi HTTP/1.1\r\n\r\n");
         //out.flush();
         String in_line;

         //while ((in_line = in.readLine()).length() != 0) {}         // skip past the HTTP Headers

         while ((in_line = in.readLine()) != null) 
         {     
            // read the TurboWin information
            System.out.println(in_line);
         }
         socket.close();
            
            
            
         //   System.out.println(response);
//
         //         info = "[GPS] found GPS on port: " + serial_ports_portid_array[i];
         //         System.out.print("--- ");
         //         main.log_turbowin_system_message(info);
         //         System.out.println("");
            
            
            
            //String userInput;
            //while ((userInput = stdIn.readLine()) != null) 
            //{
            //   out.println(userInput);
            //   System.out.println("echo: " + in.readLine());
            //}
         
         
            // while ((in_line = in.readLine()) != null) 
            //{     
            //   // read the TurboWin information
            //   System.out.println(in_line);
            //}
         } 
         catch (UnknownHostException e) 
         {
            System.out.println("Don't know about host " + host);
            // System.exit(1);
         } 
         catch (IOException e) 
         {
            System.out.println("Couldn't get I/O for the connection to " + host);
            //System.exit(1);
         }          
         
         //..........
         // socket connection 
         //String host_wifi = "192.168.178.27";               // ALLEEN VOOR 
         String host_wifi = "MintakaStar-292efe";               // ALLEEN VOOR TEST
         Socket socket = new Socket(host_wifi, 80);
         InputStream is = socket.getInputStream();
         BufferedReader in = new BufferedReader(new InputStreamReader(is));
         OutputStream os = socket.getOutputStream();
         PrintWriter out = new PrintWriter(os);
         
         System.out.printf("Sending 'TurboWin' command to %s%n", host);
         out.print("GET /turbowin HTTP/1.1\r\n\r\n");
         out.flush();
         String in_line;

         //while ((in_line = in.readLine()).length() != 0) {}         // skip past the HTTP Headers

         while ((in_line = in.readLine()) != null) 
         {     
            // read the TurboWin information
            System.out.println(in_line);
         }
         socket.close();
         System.out.println("Done");
         
         
         return in_line;
   
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         try 
         {
            main.total_string = get();
         }
         catch (InterruptedException | ExecutionException ex) 
         {
            String message = "[WIFI] " + ex.toString();
            main.log_turbowin_system_message(message);
            main.total_string = "";
         } // catch
         
      } // protected void done()

   }.execute(); // new SwingWorker <Void, Void>()
}
*/
 


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS232_GPS_NMEA_0183_init_new_data_received_check_timer()
{
   // called from: RS232_GPS_NMEA_0183_initComponents() [main_RS232_RS422.java]
   
   
   ActionListener check_new_data_action = new ActionListener()
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         // NB currentTimeMillis(): returns the difference,in milliseconds, between the current system time and midnight, January 1, 1970 UTC
         // NB last_new_GPS_data_received_TimeMillis: returns the difference,in milliseconds between the last GPS data receipt system time and midnight, January 1, 1970 UTC.
         if ((System.currentTimeMillis() - last_new_GPS_data_received_TimeMillis) > MAX_AGE_GPS_DATA)     // MAX_AGE_GPS_DATA e.g. 30000 msec (5 minutes)
         {
            // NB see Class: RS232_GPS_Class_Receive_Data [main_RS232_RS422.java] for setting of var "last_new_GPS_data_received_TimeMillis"
            
            //..................
            // message obsolete GPS data only the first time
            if (main.obsolate_GPS_data_flag == false)
            {
               String message = "[GPS] data obsolete (lost connection?)";
               main.log_turbowin_system_message(message);
               
               // info text on (bottom) main screen 
               main.jLabel6.setText(main.APPLICATION_NAME + " receiving GPS data via serial communication: stopped");
               
            } // if (main.obsolate_GPS_data_flag == false)
            else
            {
               // NB RS232_GPS listener restart every hour
               cal_RS232_GPS_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));   // system date time in UTC of this moment
               cal_RS232_GPS_system_date_time.getTime();                                                // now effective
               int RS232_GPS_system_minute_local = cal_RS232_GPS_system_date_time.get(Calendar.MINUTE);
                  
               // cancel RS232_GPS listener thread
               if (RS232_GPS_system_minute_local == CANCEL_GPS_THREAD)           
               {
                  RS232_GPS_worker.cancel(true);
                  String message = "[GPS] data listener thread cancelling";
                  main.log_turbowin_system_message(message);
               } // if (WiFi_system_minute_local == 30)
                  
               // restart RS232_GPS listener thread
               if (RS232_GPS_system_minute_local == START_GPS_THREAD)
               {
                  // NB in case of power failure GPS com port is still ok (still open) but listener must be restarted
                  //    so we cannot use Function: RS232_GPS_NMEA_0183_Check_Serial_Ports(), bbecause then the present GPS default port, which is ok, will be reported as occupied!
                  //    so first Function: RS232_GPS_NMEA_0183_Check_Default_Serial_Port(), see below
                  RS232_GPS_NMEA_0183_Check_Default_Serial_Port(); // in this function:: GPS_defaultPort will be set to null if present default GPS com port was not valid anymore
                  
                  if (GPS_defaultPort == null) 
                  {
                     // checking/looking for the correct GPS com port
                     int completed_checks_serial_ports = 0;
                     GPS_defaultPort = null;                // reset !
                     while ((GPS_defaultPort == null) && (completed_checks_serial_ports < MAX_COMPLETED_GPS_PORT_CHECKS))
                     {
                        RS232_GPS_NMEA_0183_Check_Serial_Ports(completed_checks_serial_ports);               // GPS
                        completed_checks_serial_ports++;
                     }  
                  
                     if (GPS_defaultPort == null)
                     {
                        String message = "[GPS] no GPS found (GPS_defaultPort = null); listener thread not restarted";
                        main.log_turbowin_system_message(message);
                     }
                  } // if (GPS_defaultPort == null) 
               
                  if (GPS_defaultPort != null)                     
                  {
                     main.GPS_serialPort  = SerialPort.getCommPort(GPS_defaultPort);
                     
                     // restart RS232_GPS listener thread
                     RS232_GPS_worker = new RS232_GPS_Class_Receive_Data();
                     RS232_GPS_worker.execute();
                     String message = "[GPS] data listener thread restarting";
                     main.log_turbowin_system_message(message);
                  } // if (GPS_defaultPort != null) 
                     
               } // if (RS232_gPS_system_minute_local == 33) 
            }
            
            main.obsolate_GPS_data_flag = true;            
            
         } // if ((System.currentTimeMillis() - last_new_GPS_data_received_TimeMillis) > MAX_AGE_GPS_DATA)
         else // so GPS data NOT obsolate (data < ? minutes old)
         {
            // reset message if before was written data obsolete
            if (main.obsolate_GPS_data_flag)
            {
               String message = "[GPS] start receiving data again";
               main.log_turbowin_system_message(message);
               
               // info text on (bottom) main screen 
               main.jLabel6.setText(main.APPLICATION_NAME + " receiving GPS data via serial communication.....");
               
            } // if (main.obsolate_GPS_data_flag)
            
            main.obsolate_GPS_data_flag = false;
            
         } //else (so GPS data NOT obsolate)
         
      } // public void actionPerformed(ActionEvent e)
   };
   
   // main loop for checking when new GPS data arrives 
   check_new_GPS_data_timer_delay = DELAY_NEW_DATA_CHECK_LOOP;                              // delay between two new GPS data checks (e.g. 1 minute)  
   check_new_GPS_data_timer = new Timer(check_new_GPS_data_timer_delay, check_new_data_action);
   check_new_GPS_data_timer.setRepeats(true);                                               // false = only one action
   check_new_GPS_data_timer.setInitialDelay(INITIAL_DELAY_NEW_DATA_CHECK_LOOP);             // time in millisec to wait after timer is started to fire first event
   check_new_GPS_data_timer.setCoalesce(true);                                              // to be sure
   check_new_GPS_data_timer.start();      
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS232_And_WiFi_init_new_sensor_data_received_check_timer()
{
   /////// for serial link and WiFi connected barometers (NB not EUCAWS) ////////
   
   // called from: RS232_initComponents()
   //              WiFi_initComponents()
   
   
   ActionListener check_new_data_action = new ActionListener()
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
			//System.out.println("+++ System.currentTimeMillis() = " + System.currentTimeMillis());
			//System.out.println("+++ last_new_data_received_TimeMillis = " + last_new_data_received_TimeMillis);
			//System.out.println("+++ System.currentTimeMillis() - last_new_data_received_TimeMillis = " + (System.currentTimeMillis() - last_new_data_received_TimeMillis));
			
			
         // NB currentTimeMillis(): returns the difference,in milliseconds, between the current system time and midnight, January 1, 1970 UTC
         // NB last_new_data_received_TimeMillis: returns the difference,in milliseconds between the last aws data receipt system time and midnight, January 1, 1970 UTC.
         if ((System.currentTimeMillis() - last_new_data_received_TimeMillis) > MAX_AGE_AWS_DATA)     // MAX_AGE_AWS_DATA e.g. 30000 msec (5 minutes), same for barometer data
         {
            main.displayed_barometer_data_obsolate = true;             // for DASHBOARD
            
            // MOET DEZE ACTIONLISTNER EXPLICIET GESTOPT WORDEN ALS EEN EXIT DOOR GEBRUIKER WORDT GEGEVEN ????????? (check in console op vreemde foutmeldingen na afsluiten TurboWin+)
          
            // message obsolete data only the first time
            if (main.obsolate_data_flag == false)
            {
               if (main.RS232_connection_mode == 6)   // Mintaka Star WiFi
               {
                  String message = "[WIFI] sensor data obsolete (lost connection?)";
                  main.log_turbowin_system_message(message);
               }
               else if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2 || main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5) // PTB220, PTB330, Mintaka Duo, Mintaka Star USB
               {
                  String message = "[BAROMETER] sensor data obsolete (lost connection?)";
                  main.log_turbowin_system_message(message);
               }
            } // if (main.obsolate_data_flag == false)
            else // data obsolete but after > 5 minutes
            {
               if (main.RS232_connection_mode == 6 || main.RS232_connection_mode == 8)   // Mintaka Star WiFi or Mintaka StarX Wifi
               {
                  // do nothing port 0.0.0.0:60004 will alway stay open as long as TurboWin+/Web is running (Windows power shell command "netstat -aon")
/*            
                  // NB WiFi listener restart every hour
                  cal_WiFi_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));   // system date time in UTC of this moment
                  cal_WiFi_system_date_time.getTime();                                               // now effective
                  int WiFi_system_minute_local = cal_WiFi_system_date_time.get(Calendar.MINUTE);
                  
                  // cancel WiFi listener thread
                  if (WiFi_system_minute_local == CANCEL_BAROMETER_THREAD)           
                  {
                     WiFi_worker.cancel(true);
                     //WiFi_worker = null;     // NB null do not prevent a Bindexception,; 
                     // NB2 a Bindexception is not an issue because it means TurboWin+ is still bind to (UDP)WiFi, 
                     //     it would be an issue if it was bind to onother process/application
                     String message = "[WIFI] sensor data listener thread cancelling";
                     main.log_turbowin_system_message(message);
                  } 
                  
                  // restart WiFi listener thread
                  if (WiFi_system_minute_local == START_BAROMETER_THREAD)            
                  {
                     WiFi_worker = new WiFi_Class_Receive_UDP();
                     WiFi_worker.execute();
                     String message = "[WIFI] sensor data listener thread restarting";
                     main.log_turbowin_system_message(message);
                  } 
*/                  
               } //  if (main.RS232_connection_mode == 6) 
               else if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2 || main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5 || main.RS232_connection_mode == 7) // PTB220, PTB330, Mintaka Duo, Mintaka Star USB, Mintaka StarX USB
               {
                  // NB RS232 listener restart every hour
                  cal_RS232_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));   // system date time in UTC of this moment
                  cal_RS232_system_date_time.getTime();                                                // now effective
                  int RS232_system_minute_local = cal_RS232_system_date_time.get(Calendar.MINUTE);
                  
                  // cancel RS232 listener thread
                  if (RS232_system_minute_local == CANCEL_BAROMETER_THREAD)           
                  {
                     RS232_worker.cancel(true);                      // Returns: false if the task could not be cancelled, typically because it has already completed normally; true otherwise
                     String message = "[BAROMETER] sensor data listener thread cancelling";
                     main.log_turbowin_system_message(message);
                  } 
                  
                  // restart RS232 listener thread
                  if (RS232_system_minute_local == START_BAROMETER_THREAD)            
                  {
                     // in case of power failure com port of connected barometer is still ok (still open) but listener must be restarted
                     // NB so we cannot use Function: RS232_Check_Serial_Ports_8(because then the present default port, which is ok, will be reported as occupied!
                     //    so first Function: RS232_Check_Default_Serial_Port(), see below
                     RS232_Check_Default_Serial_Port(); // in this function:: main.defaultPort will be set to null if present default comm port was not valid anymore
                  
                     if (main.defaultPort != null)  
                     {
                        // NB expierenced during testing: at least for the PTB330 this (Function: RS232_Format_Barometer_Output_3()) is essential
                        RS232_Format_Barometer_Output_3();    // to be sure, send format and start string to barometer   
                     }
                     
                     if (main.defaultPort == null)  
                     {
                        // so default port not valid anymore (most likely there was no power break) now first checking/looking for the correct com port
                        int completed_checks_serial_ports = 0;
                        while ((main.defaultPort == null) && (completed_checks_serial_ports < MAX_COMPLETED_BAROMETER_PORT_CHECKS))
                        {
                           RS232_Check_Serial_Ports_8(completed_checks_serial_ports);               // PTB220, PTB330, MintakaDuo, MintakaStar USB
                           completed_checks_serial_ports++;
                        }

                        // send Vaisala/Mintaka serial commands to the barometer 
                        if (main.defaultPort != null)
                        {
                           RS232_Format_Barometer_Output_3();                                       // PTB220, PTB330, MintakaDuo, MintakaStar USB
                        }
                        else
                        {
                           String message = "[BAROMETER] no barometer found (defaultPort = null); listener thread not restarted";
                           main.log_turbowin_system_message(message);
                        }
                     } // if (main.defaultPort == null) 

                     if (main.defaultPort != null)                     
                     {
                        main.serialPort  = SerialPort.getCommPort(main.defaultPort); 
                        
                        // restart RS232 listener thread
                        RS232_worker = new RS232_Class_Receive_Sensor_Data();
                        RS232_worker.execute();
                        String message = "[BAROMETER] sensor data listener thread restarting";
                        main.log_turbowin_system_message(message);
                     } // if (main.defaultPort != null) 
                     
                  } // if (RS232_system_minute_local == START_BAROMETER_THREAD)
               } // else if (main.RS232_connection_mode == 1 etc.
            } // else (data obsolate but after > 5 minutes)
            
            main.obsolate_data_flag = true;
         }
         else // so sensor data NOT obsolete (data < 5 minutes old)
         {
            main.displayed_barometer_data_obsolate = false;                         // for DASHBOARD
            
            // reset message if before was written data obsolete
            if (main.obsolate_data_flag)
            {
               if (main.RS232_connection_mode == 6)   // Mintaka Star WiFi
               {
                  String message = "[WIFI] sensor data receiving again";
                  main.log_turbowin_system_message(message);
               }
               else if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2 || main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5) // PTB220, PTB330, Mintaka Duo, Mintaka Star USB
               {
                  String message = "[BAROMETER] start receiving sensor data again";
                  main.log_turbowin_system_message(message);
                  
                  // NB in the case of a PTB330 this could be a false message ("[BAROMETER] start receiving sensor data again";)
                  //    because if the power was off and on the PTB330 reports "PTB330 / 1.02" and the listener eports this as receiving data again
               }               
            } // if (main.obsolate_data_flag)
            
            main.obsolate_data_flag = false;
            
            // NB in this case: in function: RS422_Read_AWS_Sensor_Data_For_Display() the fields on the main screen will be updated
            // NB main.date_time_fields_update() etc. and VOT will be updated there
         }
         
         // initialisation
         //last_new_data_received_TimeMillis = 0;
   
      } // public void actionPerformed(ActionEvent e)
   };
   
   /* main loop for checking when new AWS data arrives */
   check_new_data_timer_delay = DELAY_NEW_DATA_CHECK_LOOP;                              // delay between two new aws data checks (e.g. 1 minute)  
   check_new_data_timer = new Timer(check_new_data_timer_delay, check_new_data_action);
   check_new_data_timer.setRepeats(true);                                               // false = only one action
   check_new_data_timer.setInitialDelay(INITIAL_DELAY_NEW_DATA_CHECK_LOOP);             // time in millisec to wait after timer is started to fire first event
   check_new_data_timer.setCoalesce(true);                                              // to be sure
   check_new_data_timer.start();   
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void WiFi_initComponents()
{
   // called from specific_connection_initComponents() [main.java]
   
   
   // for sensor data files (in the file name itself) (note also used in RS232_view.java)
   main.sdf3 = new SimpleDateFormat("yyyyMMddHH");                                  // HH hour in day (0-23) note there is also hh (then also with am, pm)
   main.sdf3.setTimeZone(TimeZone.getTimeZone("UTC"));

   // for date-time strings/remarks in the sensor data records (note also used in RS232_view.java)
   main.sdf4 = new SimpleDateFormat("yyyyMMddHHmm");                                // HH hour in day (0-23) note there is also hh (then also with am, pm)
   main.sdf4.setTimeZone(TimeZone.getTimeZone("UTC"));
    
   // for pop-up air pressure when clicking tray icon (TurboWin+ minimized/iconfied)
   sdf8 = new SimpleDateFormat("dd MMM yyyy HH.mm");
   sdf8.setTimeZone(TimeZone.getTimeZone("UTC"));                                   // <----- DEZE = ESSENTIEEL

      
   // dir for storage sensor data
   if ((main.logs_dir == null) || (main.logs_dir.compareTo("") == 0))
   {
      String info = "logs folder (for sensor data storage) unknown, select: Maintenance -> Log files settings";
      JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
   }
   
   // delete old sensor data files (whether or not a barometer was found)
   main.log_turbowin_system_message("[GENERAL] deleting old sensor data files");
   RS232_Delete_Sensor_Data_Files();

   
   // check wifi connection
  //WiFi_Check_Connection_b();
   
   //WiFi_Receive_UDP();
   //WiFi_Class_Receive_UDP.execute();
   WiFi_worker = new WiFi_Class_Receive_UDP();
   WiFi_worker.execute();
   
   String message = "[WIFI] start listening";
   main.log_turbowin_system_message(message);
   
   // start timer for checking every 1 minute if there is new barometer sensor data available (because if last data > 5 minutes old 'gray' data on main screen)
   // NB invoking after: WiFi_worker = new WiFi_Class_Receive_UDP(); and WiFi_worker.execute();
   last_new_data_received_TimeMillis = 0;
   RS232_And_WiFi_init_new_sensor_data_received_check_timer();

}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void Ethernet_initComponents()
{
   // called from specific_connection_initComponents() [main.java]
   
   
   // for sensor data files (in the file name itself) (note also used in RS232_view.java)
   main.sdf3 = new SimpleDateFormat("yyyyMMddHH");                                  // HH hour in day (0-23) note there is also hh (then also with am, pm)
   main.sdf3.setTimeZone(TimeZone.getTimeZone("UTC"));

   // for date-time strings/remarks in the sensor data records (note also used in RS232_view.java)
   main.sdf4 = new SimpleDateFormat("yyyyMMddHHmm");                                // HH hour in day (0-23) note there is also hh (then also with am, pm)
   main.sdf4.setTimeZone(TimeZone.getTimeZone("UTC"));
    
   // for function: RS422_write_sensor_data_to_file() and RS422_V3_write_sensor_data_to_file()
   sdf7 = new SimpleDateFormat("ss");                                           
   sdf7.setTimeZone(TimeZone.getTimeZone("UTC"));

      
   // dir for storage sensor data
   if ((main.logs_dir == null) || (main.logs_dir.compareTo("") == 0))
   {
      String info = "logs folder (for sensor data storage) unknown, select: Maintenance -> Log files settings";
      JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
   }
   
   // delete old sensor data files (whether or not a barometer was found)
   main.log_turbowin_system_message("[GENERAL] deleting old sensor data files");
   RS232_Delete_Sensor_Data_Files();

   Ethernet_worker = new Ethernet_Class_Receive_UDP();
   Ethernet_worker.execute();
   
   String message = "[AWS] start listening";
   main.log_turbowin_system_message(message);
   
   // start timer for checking every 1 minute if there is new AWS data available (because if last data > 5 minutes old 'gray' data on main screen)
   // NB invoking after: Ethernet_worker = new Ethernet_Class_Receive_UDP(); and Ethernet_worker.execute();
   last_new_data_received_TimeMillis = 0;
   RS422_init_new_aws_data_received_check_timer();
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private class RS232_Class_Receive_Sensor_Data extends SwingWorker<String, String>
{
   // called from RS232_initComponents()
   
    //  new SwingWorker<String, String>()
    //     {
            boolean retry = false;
            
            
            @Override
            protected String doInBackground() throws Exception
            {
               main.serialPort.openPort(); 
               if (main.serialPort.isOpen())
               {
                  //main.serialPort.openPort();               // NB will be closed in Function:  File_Exit_menu_actionPerformd() [main.java]
                  //main.serialPort.setParams(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
                  //main.serialPort.setFlowControlMode(main.flow_control);
                  main.serialPort.setComPortParameters(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
                  main.serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

                  
                  // Preparing a mask. In a mask, we need to specify the types of events that we want to track.
                  // Well, for example, we need to know what came some data, thus in the mask must have the
                  // following value: MASK_RXCHAR. If we, for example, still need to know about changes in states 
                  // of lines CTS and DSR, the mask has to look like this: SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR
                  //int mask = SerialPort.MASK_RXCHAR;
                  
                  //Set the prepared mask
                  //main.serialPort.setEventsMask(mask);                  
                  
		         } // if (serialPort.isOpen())
               else        
               {
                  //System.out.println("+++ " + ex);
                  System.out.println("+++ " + "[BAROMETER] Couldn't open " +  main.serialPort.getDescriptivePortName());
               }               
	            
               
               if (main.serialPort.isOpen())
               {
                  main.serialPort.addDataListener(new SerialPortDataListener() 
                  {
                     @Override
                     public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                  
                     @Override
                     public void serialEvent(SerialPortEvent event)
                     {                  
                        String ontvangen_SMD_string = "";
                     
                        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                        {
                           return;
                        }
                     
                        //byte[] newData = new byte[main.serialPort.bytesAvailable()];
                        //int numRead = main.serialPort.readBytes(newData, newData.length);  
                        int numRead;
                        byte[] newData = null;
                        try
                        {
                           newData = new byte[main.serialPort.bytesAvailable()];
                           numRead = main.serialPort.readBytes(newData, newData.length);                  
                        }
                        catch (NegativeArraySizeException ex) 
                        {
                           numRead = -1;
                           // NB don't modify the GUI anywhere except for on the event-dispatch-thread.!! (so not here in the function doinBackground())
                        }
                  
                        if (numRead > 0)
                        {
                           ontvangen_SMD_string = new String(newData, StandardCharsets.UTF_8);
                     
                           //System.out.println("Read " + numRead + " bytes.");
                     
                           // publish: Sends data chunks to the process(java.util.List) method. This method is to be used from inside the doInBackground method to deliver intermediate results for processing on the Event Dispatch Thread inside the process method.
                           //          Because the process method is invoked asynchronously on the Event Dispatch Thread  multiple invocations to the publish method might occur before the process method is executed. For performance purposes all these invocations are coalesced into one invocation with concatenated arguments.
                           //
                           // For example:
                           //
                           // publish("1");
                           // publish("2", "3");
                           // publish("4", "5", "6");
                           //
                           // might result in: process("1", "2", "3", "4", "5", "6")
                           //
                           publish(new String[] { ontvangen_SMD_string });
                        } // if (numRead > 0)       
                        else if (numRead == -1)
                        {
                           publish(new String[] { "interruption or excecution error\n" });
                        }
                     } // public void serialEvent(SerialPortEvent event)
                  });                   
               } // if (serialPort.isOpen())
               
               
               return null;

            } // protected Void doInBackground() throws Exception
            
            // NB https://stackoverflow.com/questions/6523623/graceful-exception-handling-in-swing-worker
            //    You should NOT try to catch exceptions in the background thread but rather let them pass through to the SwingWorker itself, 
            //    and then you can get them in the done() method by calling get()which normally returns the result of doInBackground() 
            //    (Void in your situation). If an exceptionwas thrown in the background thread then get() will throw it, wrapped inside an ExecutionException.
            //
            // NB Martin: but in this case it goes via the process() function, done() method has no effect!
            //
/*            
            // Executed in EDT
            @Override
            protected void done() 
            {
               try 
               {
                  //System.out.println("Done");
                  get();
               } 
               catch (ExecutionException ex) 
               {
                  //e.getCause().printStackTrace();
                  //String msg = String.format("Unexpected problem: %s", 
                  //            e.getCause().toString());
                  //JOptionPane.showMessageDialog(Utils.getActiveFrame(),
                  // msg, "Error", JOptionPane.ERROR_MESSAGE, errorIcon);
                  String message = "[BAROMETER] " + ex.getMessage(); 
                  main.log_turbowin_system_message(message);
                  message = "barometer connection execution exception (pc sleep mode?); restart this program";
                  JOptionPane.showMessageDialog(null, message, main.APPLICATION_NAME, JOptionPane.ERROR_MESSAGE);                 
               } 
               catch (InterruptedException ex) 
               {
                  // Process e here
                  //String message = "[BAROMETER] " + ex.getMessage(); 
                  String message = "[BAROMETER] " + "interruption exception"; 
                  main.log_turbowin_system_message(message);
                  message = "barometer connection interrupted; restart this program";
                  JOptionPane.showMessageDialog(null, message, main.APPLICATION_NAME, JOptionPane.ERROR_MESSAGE);
               }
            } // protected void done() 
*/           

            @Override
            protected void process(List<String> data)
            {
               // process: Receives data chunks from the publish method asynchronously on the Event Dispatch Thread.
               for (String ontvangen_SMD_string: data)
               {
                  // NB altijd in for loop omdat meerdere ontbvangen_SMD_string's verzameld kunnen zijn voordat het hier procesed wordt(inherent aan SwingWorker)
                  //System.out.println("ontvangen_SMD_string = " + ontvangen_SMD_string);   // bv martin

                  main.total_string = ontvangen_SMD_string;


                  // NB Onderstaande om i.p.v. elke minuut de data per 5 minuten op te slaan
                  //    werkt hier niet (door de 'brokken' record maar bij AWS zou het wel een mogelijkheid kunnen zijn
                  //if (total_string.indexOf("3$") != -1)
                  //{
                  //   String minuten = sdf4.format(new Date()).substring(10, 10 + 2);  // sdf4: yyyyMMddHHmm
                  //   int int_minuten = Integer.parseInt(minuten.trim());
                  //
                  //   if (int_minuten % 5 == 0   );
                  //
                  System.out.print(main.total_string);
                  //System.out.println(total_string); // NB zo zie je ruwweg hoeveel bytes dat telkens beschikbaar zijn
                  
                  // error logging
                  if (main.total_string.indexOf("error") != -1)
                  {
/*                     
                     // NB code below is OK but maybe dangerous if messagebox may blocking application? (eg after a 1 time hick-upp)
                     if (comm_error_messagebox_shown == 0 )
                     {
                        String message = "[BAROMETER] " + "interruption or execution exception"; 
                        main.log_turbowin_system_message(message);
                        message = "barometer connection interrupted; check pc sleep mode and/or connection cable; if obsolete data restart this application";
                     
                        comm_error_messagebox_shown = 1;
                        //int return = JOptionPane.showMessageDialog(null, message, main.APPLICATION_NAME, JOptionPane.ERROR_MESSAGE);
                        Object[] options = { "OK" };
                        int reply = JOptionPane.showOptionDialog(null, message, main.APPLICATION_NAME, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                        if (reply == JOptionPane.OK_OPTION) 
                        {
                           comm_error_messagebox_shown = 0;
                        }
                     } //  if (comm_error_messagebox_shown == 0)   
*/                     
                     String message = "[BAROMETER] " + "interruption or execution exception"; 
                     main.log_turbowin_system_message(message);
                  } //  if (main.total_string.indexOf("error") != -1)
                  
                  

                  // update main screen with application name + mode and date time last received data
                  String info = "";
                  if ((main_RS232_RS422.test_record.equals("")) || (main_RS232_RS422.test_record == null)) 
                  {
                     info = "no data";
                  } 
                  else 
                  {
                     info = main_RS232_RS422.test_record;
                  }   
                  
                  if (main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5 || main.RS232_connection_mode == 7)  // Mintaka Duo or Mintaka Star USB or StarX USB 
                  {
                     main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received barometer data via USB: " + info);
                  }
                  else
                  {   
                     main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received barometer data via serial communication: " + info);
                  }
                  
                  RS232_write_sensor_data_to_file();                             // PTB220, PTB330, MintakaDuo, Mintaka Star USB, Mintaka StarX USB
                  
                  // for timer checking the data is not obsolete; see Function: RS232_WiFi_init_new_barometer_received_check_timer()[main_RS232_RS422.java]
                  last_new_data_received_TimeMillis = System.currentTimeMillis(); 
                  
                  // update the date and time on the main screen (and the global date time var's)
                  // NB if during start up the date and time displayed in the pupup message was not comfirmed by the user
                  //    it is assumed that the pc is not running on the correct time so then do not update the date time automatically
                  if (main.use_system_date_time_for_updating == true)
                  {  
                     set_datetime_while_collecting_sensor_data();
                  }
                  
                  // WOW enabled (and not 'only publish if manual obs was send') and a complete sensor data string was received
                  if ((main.WOW == true) && (!main.WOW_reporting_interval.equals(main.WOW_REPORTING_INTERVAL_MANUAL)) && (main.total_string.indexOf("\n") != -1))
                  {
                     cal_WOW_systeem_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // system date time in UTC of this moment
                     cal_WOW_systeem_datum_tijd.getTime();                                             // now effective
                     int WOW_system_minute_local = cal_WOW_systeem_datum_tijd.get(Calendar.MINUTE);
                     cal_WOW_systeem_datum_tijd = null; 
                  
                     if ((WOW_system_minute_local -1) % Integer.valueOf(main.WOW_reporting_interval) == 0)      // e.g. every 10 minutes (WOW_reporting_interval);   but no interference with APR!           
                     {
                        // logging
                        String message = "[WOW] scheduled upload"; 
                        main.log_turbowin_system_message(message);
                        
                        // checking
                        boolean WOW_settings_ok = RS232_check_WOW_settings();
                           
                        if (WOW_settings_ok)
                        {   
                           // read the barometer data which was saved before by TurboWin+ from a sensor data file
                           if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)       // PTB220 or PTB330
                           {
                              RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                           }
                           else if (main.RS232_connection_mode == 4)                                     // MintakaDuo
                           {
                              RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                           }
                           else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                           {
                              //RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                              boolean StarX = false;
                              RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry, StarX);
                           } 
                           else if (main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8)  // Mintaka StarX (USB or WiFi)
                           {
                              boolean StarX = true;
                              RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry, StarX);
                           } 
                        } // if (WOW_settings_ok)
                     }   
                  } // if ((main.WOW == true) && (!main.WOW_reporting_interval.equals(main.WOW_REPORTING_INTERVAL_MANUAL)) && (main.total_string.indexOf("\n") != -1))

                  // APR (Automated Pressure Reporting)
                  if ((main.APR == true) && (main.total_string.indexOf("\n") != -1))
                  {
                     cal_APR_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));   // system date time in UTC of this moment
                     cal_APR_system_date_time.getTime();                                               // now effective
                     int APR_system_hour_local = cal_APR_system_date_time.get(Calendar.HOUR_OF_DAY);   // HOUR_OF_DAY is used for the 24-hour clock. E.g., at 10:04:15.250 PM the HOUR_OF_DAY is 22.
                     int APR_system_minute_local = cal_APR_system_date_time.get(Calendar.MINUTE);
                  
                     if ((APR_system_minute_local == 0) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
                     {
                        retry = false;
                        
                        // logging
                        String message = "[APR] scheduled upload"; 
                        main.log_turbowin_system_message(message);
                        
                        // checking
                        boolean APR_settings_ok = RS232_check_APR_settings();
                        boolean GPS_date_time_ok = true;
                        
                        if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6 || main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8) // Mintaka Star (USB or WiFi) or StarX (USB or WiFi)
                        {
                           // NB Mintaka Star saved data do not contain date/time information so it is not possible to check this
                           //    so it is made always true
                           GPS_date_time_ok = true;
                        }
                        else
                        {
                           GPS_date_time_ok = main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing("APR");
                        }
                        
                        if (APR_settings_ok && GPS_date_time_ok) // GPS date-time was compared with system date-time                    
                        {
                           // NB in this stage we are sure the date-time is correct! + we are sure position is available!
                             
                           // read the barometer data which was saved before by TurboWin+ from a sensor data file
                           if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)  // PTB220 or PTB330
                           {
                              RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                           }
                           else if (main.RS232_connection_mode == 4)                                // MintakaDuo
                           {
                              RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                           }
                           else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                           {
                              //RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                              boolean StarX = false;
                              RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry, StarX);
                           }   
                           else if (main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8)  // Mintaka StarX (USB or WiFi)
                           {
                              boolean StarX = true;
                              RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry, StarX);
                           } 
                        } // if (APR_settings_ok && GPS_date_time_ok) 
                     } // if ((APR_system_minute_local == 0) etc.
                     else if ((APR_server_response_code == main.RESPONSE_NO_INTERNET) && (APR_system_minute_local == APR_RETRY_MINUTES) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
                     {
                        retry = true;
                        
                        // logging
                        String message = "[APR] retry scheduled upload"; 
                        main.log_turbowin_system_message(message);
                        
                        // checking
                        boolean APR_settings_ok = RS232_check_APR_settings();
                        boolean GPS_date_time_ok = true;
                        
                        if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6 || main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8) // Mintaka Star (USB or WiFi) or StarX (USB or WiFi)
                        {
                           // NB Mintaka Star saved data do not contain date/time information so it is not possible to check this
                           //    so it is made always true
                           GPS_date_time_ok = true;
                        }
                        else
                        {
                           GPS_date_time_ok = main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing("APR");
                        }
                     
                        if (APR_settings_ok && GPS_date_time_ok) // GPS date-time was compared with system date-time                    
                        {
                           // NB in this stage we are sure the date-time is correct! + we are sure position is available!
                             
                           // read the barometer data which was saved before by TurboWin+ from a sensor data file
                           if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)  // PTB220 or PTB330
                           {
                              RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                           }
                           else if (main.RS232_connection_mode == 4)                                // MintakaDuo
                           {
                              RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                           }
                           else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                           {
                              //RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                              boolean StarX = false;
                              RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry, StarX);
                           } 
                           else if (main.RS232_connection_mode == 7 || main.RS232_connection_mode == 8)  // Mintaka StarX (USB or WiFi)
                           {
                              boolean StarX = true;
                              RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry, StarX);
                           }                            
                        } // if (APR_settings_ok && GPS_date_time_ok) 
                        
                        // reset (to be sure)
                        APR_server_response_code = 0;
                        
                     } // else if ((APR_server_response_code = main.RESPONSE_NO_INTERNET) etc.
                     else if ((APR_server_response_code == main.RESPONSE_NO_INTERNET) && (APR_system_minute_local < APR_RETRY_MINUTES) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
                     {
                        // text will be visible only a few minutes (till the retry upload)
                        main.jTextField4.setText("[APR] retry automated pressure report upload within a few minutes");
                     }
                     else
                     {
                        // text with next APR update bottom status line TurboWin+ main screen (updated every minute)
                        for (int i = (APR_system_hour_local + 1); i < (APR_system_hour_local + 1 + Integer.valueOf(main.APR_reporting_interval)); i++)
                        {
                           // 24 hours or higher makes no difference for this computation
                           if (i % Integer.valueOf(main.APR_reporting_interval) == 0)
                           {
                              cal_APR_system_date_time.add(Calendar.HOUR_OF_DAY, i - APR_system_hour_local);   // add 1 - 5 hours
                              cal_APR_system_date_time.getTime();
                                 
                              String next_APR_string = "[APR] next automated air pressure report upload: " + cal_APR_system_date_time.get(Calendar.DAY_OF_MONTH) + " " + main.convert_month(cal_APR_system_date_time.get(Calendar.MONTH)) + " " + cal_APR_system_date_time.get(Calendar.YEAR) + " " + cal_APR_system_date_time.get(Calendar.HOUR_OF_DAY) + ".00 UTC"; 
                                 
                              main.jTextField4.setText(next_APR_string);
                              break;
                           }
                        } // for (int i = (APR_system_hour_local + 1); etc.
                           
                     } // else
                        
                     cal_APR_system_date_time = null; 
                        
                  } // if ( (main.APR == true) && (main.total_string.indexOf("\n") != -1))
                  
               } // for (String ontvangen_string : data)
            } // protected void process(List<String> data)
   //      }.execute(); // new SwingWorker<String, String>()
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public void RS232_initComponents()
{
      // called from specific_connection_initComponents() [main.java] which is called from  read_muffin() [main.java] or lees_configuratie_regels() [main.java]
      
      ///////// only used for PTB220/PTB330/Mintaka Duo/Mintaka Star USB barometers (not for EUCAWS and not for Mintaka Star WiFi) ////////
      
      // http://www.snip2code.com/Snippet/1044/Java--read-from-USB-using-RXTX-library
      // FOR LINUX:
      //#==================================================================#
      // WARNING! - DO NOT SET THE FOLLOWING PROPERTY WITH RXTX LIBRARY, IT
      // CAUSES A PROGRAM LOCK:
      // serialPort.notifyOnOutputEmpty(true);
      //#==================================================================#     
      
      // start timer for checking every 1 minute if there is newbarometer sensor data available (because if last data > 5 minutes old 'gray' data on main screen)
      //last_new_data_received_TimeMillis = 0;
      //RS232_And_WiFi_init_new_sensor_data_received_check_timer();
      
      
      //System.loadLibrary("C:\\Users\\hometrainer\\Documents\\NetBeansProjects\\turbowin_jws\\rxtxSerial.dll"); // gaat fout omdat het geen path mag zijn (alleen de library name)
      
      
      // NB load dll via applet or java web start
      //    You can definitely accomplish this. I have a working applet in production that does exactly this. 
      //    Even if your applet is signed, you still need to use the Access Controller to access the dll, you cannot just call "loadlibrary". 
      //    You can add this to the Java policy file however this is not recommended due to 1. You probably do not have access to the users java configuration. 2. 
      //    Even if this is for your own company use, managing the policy file is a pain as users will download some JRE and your policy file is either overwritten or ignored.
      //    You best bet is to sign your jar, making sure to wrap your load library code in a privileged block of code like this. 
      //
      //    You can Also use System.loadlibrary(mydll.dll) but you have to have the dll folder on the path in windows so the applet can find it. 
      
      // for writing info data to status line (bottom of screen)
      //sdf2 = new SimpleDateFormat("yyyy-MM-dd HH.mm");                            // HH hour in day (0-23) note there is also hh (with am, pm)
      //sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
      
      // for sensor data files (in the file name itself) (note also used in RS232_view.java)
      main.sdf3 = new SimpleDateFormat("yyyyMMddHH");                                  // HH hour in day (0-23) note there is also hh (then also with am, pm)
      main.sdf3.setTimeZone(TimeZone.getTimeZone("UTC"));

      // for date-time strings/remarks in the sensor data records (note also used in RS232_view.java)
      main.sdf4 = new SimpleDateFormat("yyyyMMddHHmm");                                // HH hour in day (0-23) note there is also hh (then also with am, pm)
      main.sdf4.setTimeZone(TimeZone.getTimeZone("UTC"));
    
      // for pop-up air pressure when clicking tray icon (TurboWin+ minimized/iconfied)
      sdf8 = new SimpleDateFormat("dd MMM yyyy HH.mm");
      sdf8.setTimeZone(TimeZone.getTimeZone("UTC"));                                   // <----- DEZE = ESSENTIEEL

      
      // dir for storage sensor data
      //if (logs_dir.trim().equals("") == true || logs_dir.trim().length() < 2)
      if ((main.logs_dir == null) || (main.logs_dir.compareTo("") == 0))
      {
         String info = "logs folder (for sensor data storage) unknown, select: Maintenance -> Log files settings";
         JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
      }
      
      // checking/looking for the correct com port
      //
      int completed_checks_serial_ports = 0;
      while ((main.defaultPort == null) && (completed_checks_serial_ports < MAX_COMPLETED_BAROMETER_PORT_CHECKS))
      {
         RS232_Check_Serial_Ports_8(completed_checks_serial_ports);               // PTB220, PTB330, MintakaDuo, MintakaStar USB
         completed_checks_serial_ports++;
      }

      // send Vaisala/Mintaka serial commands to the barometer 
      if (main.defaultPort != null)
      {
         RS232_Format_Barometer_Output_3();                                       // PTB220, PTB330, MintakaDuo, MintakaStar USB
      }
      else
      {
         String info = "no barometer found (defaultPort = null)";
         System.out.println(info);
      }
      
      // delete old sensor data files (whether or not a barometer was found)
      main.log_turbowin_system_message("[GENERAL] deleting old sensor data files");
      RS232_Delete_Sensor_Data_Files();
      

      if (main.defaultPort != null)
      {
         // info text on (bottom) main screen 
         //main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", receiving barometer sensor data via serial communication.....");
         if (main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5  || main.RS232_connection_mode == 7)            // Mintaka Duo or Mintaka Star USB or StarX USB
         {
            main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode + ", last received barometer data via USB: no data");
         }
         else
         {
            main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode + ", last received barometer data via serial communication: no data");
         }
         
         //main.serialPort = new SerialPort(main.defaultPort);  // NB main.defaultPort was determined in Function: RS232_Check_Serial_Ports_8()
         //final SerialPort serialPort  = SerialPort.getCommPort(main.defaultPort
         main.serialPort  = SerialPort.getCommPort(main.defaultPort);        
      
      
         RS232_worker = new RS232_Class_Receive_Sensor_Data();
         RS232_worker.execute();
   
         String message = "[BAROMETER] start listening";
         main.log_turbowin_system_message(message);
   
         // start timer for checking every 1 minute if there is new barometer sensor data available (because if last data > 5 minutes old 'gray' data on main screen)
         // NB invoking after: RS232_worker = new RS232_Class_Receive_Sensor_Data(); and RS232_worker.execute();
         last_new_data_received_TimeMillis = 0;
         RS232_And_WiFi_init_new_sensor_data_received_check_timer();
      } // if (main.defaultPort != null)
      
}





/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
   public void RS232_initComponents()
   {
      // called from read_muffin() [main.java] or lees_configuratie_regels() [main.java]
      
      ///////// only used for PTB220/PTB330/Mintaka Duo/Mintaka Star USB barometers (not for EUCAWS and not for Mintaka Star WiFi) ////////
      
      // http://www.snip2code.com/Snippet/1044/Java--read-from-USB-using-RXTX-library
      // FOR LINUX:
      //#==================================================================#
      // WARNING! - DO NOT SET THE FOLLOWING PROPERTY WITH RXTX LIBRARY, IT
      // CAUSES A PROGRAM LOCK:
      // serialPort.notifyOnOutputEmpty(true);
      //#==================================================================#     
      
      // start timer for checking every 1 minute if there is newbarometer sensor data available (because if last data > 5 minutes old 'gray' data on main screen)
      last_new_data_received_TimeMillis = 0;
      RS232_And_WiFi_init_new_sensor_data_received_check_timer();
      
      
      //System.loadLibrary("C:\\Users\\hometrainer\\Documents\\NetBeansProjects\\turbowin_jws\\rxtxSerial.dll"); // gaat fout omdat het geen path mag zijn (alleen de library name)
      
      
      // NB load dll via applet or java web start
      //    You can definitely accomplish this. I have a working applet in production that does exactly this. 
      //    Even if your applet is signed, you still need to use the Access Controller to access the dll, you cannot just call "loadlibrary". 
      //    You can add this to the Java policy file however this is not recommended due to 1. You probably do not have access to the users java configuration. 2. 
      //    Even if this is for your own company use, managing the policy file is a pain as users will download some JRE and your policy file is either overwritten or ignored.
      //    You best bet is to sign your jar, making sure to wrap your load library code in a privileged block of code like this. 
      //
      //    You can Also use System.loadlibrary(mydll.dll) but you have to have the dll folder on the path in windows so the applet can find it. 
      
      // for writing info data to status line (bottom of screen)
      //sdf2 = new SimpleDateFormat("yyyy-MM-dd HH.mm");                            // HH hour in day (0-23) note there is also hh (with am, pm)
      //sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
      
      // for sensor data files (in the file name itself) (note also used in RS232_view.java)
      main.sdf3 = new SimpleDateFormat("yyyyMMddHH");                                  // HH hour in day (0-23) note there is also hh (then also with am, pm)
      main.sdf3.setTimeZone(TimeZone.getTimeZone("UTC"));

      // for date-time strings/remarks in the sensor data records (note also used in RS232_view.java)
      main.sdf4 = new SimpleDateFormat("yyyyMMddHHmm");                                // HH hour in day (0-23) note there is also hh (then also with am, pm)
      main.sdf4.setTimeZone(TimeZone.getTimeZone("UTC"));
    
      // for pop-up air pressure when clicking tray icon (TurboWin+ minimized/iconfied)
      sdf8 = new SimpleDateFormat("dd MMM yyyy HH.mm");
      sdf8.setTimeZone(TimeZone.getTimeZone("UTC"));                                   // <----- DEZE = ESSENTIEEL

      
      // dir for storage sensor data
      //if (logs_dir.trim().equals("") == true || logs_dir.trim().length() < 2)
      if ((main.logs_dir == null) || (main.logs_dir.compareTo("") == 0))
      {
         String info = "logs folder (for sensor data storage) unknown, select: Maintenance -> Log files settings";
         JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
      }
      
      // checking/looking for the correct com port
      //
      int completed_checks_serial_ports = 0;
      while ((main.defaultPort == null) && (completed_checks_serial_ports < MAX_COMPLETED_BAROMETER_PORT_CHECKS))
      {
         RS232_Check_Serial_Ports_8(completed_checks_serial_ports);               // PTB220, PTB330, MintakaDuo, MintakaStar USB
         completed_checks_serial_ports++;
      }

      // send Vaisala/Mintaka serial commands to the barometer 
      if (main.defaultPort != null)
      {
         RS232_Format_Barometer_Output_3();                                       // PTB220, PTB330, MintakaDuo, MintakaStar USB
      }
      else
      {
         String info = "no barometer found (defaultPort = null)";
         System.out.println(info);
      }
      
      // delete old sensor data files (whether or not a barometer was found)
      main.log_turbowin_system_message("[GENERAL] deleting old sensor data files");
      RS232_Delete_Sensor_Data_Files();
      

      if (main.defaultPort != null)
      {
         // info text on (bottom) main screen 
         //main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", receiving barometer sensor data via serial communication.....");
         if (main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5)            // Mintaka Duo or Mintaka Star USB
         {
            main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode + ", last received barometer data via USB: no data");
         }
         else
         {
            main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode + ", last received barometer data via serial communication: no data");
         }
         
         //main.serialPort = new SerialPort(main.defaultPort);  // NB main.defaultPort was determined in Function: RS232_Check_Serial_Ports_8()
         //final SerialPort serialPort  = SerialPort.getCommPort(main.defaultPort
         main.serialPort  = SerialPort.getCommPort(main.defaultPort);        
         
         new SwingWorker<String, String>()
         {
            boolean retry = false;
            
            
            @Override
            protected String doInBackground() throws Exception
            {
               main.serialPort.openPort(); 
               if (main.serialPort.isOpen())
               {
                  //main.serialPort.openPort();               // NB will be closed in Function:  File_Exit_menu_actionPerformd() [main.java]
                  //main.serialPort.setParams(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
                  //main.serialPort.setFlowControlMode(main.flow_control);
                  main.serialPort.setComPortParameters(main.bits_per_second, main.data_bits, main.stop_bits, main.parity);
                  main.serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

                  
                  // Preparing a mask. In a mask, we need to specify the types of events that we want to track.
                  // Well, for example, we need to know what came some data, thus in the mask must have the
                  // following value: MASK_RXCHAR. If we, for example, still need to know about changes in states 
                  // of lines CTS and DSR, the mask has to look like this: SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR
                  //int mask = SerialPort.MASK_RXCHAR;
                  
                  //Set the prepared mask
                  //main.serialPort.setEventsMask(mask);                  
                  
		         } // if (serialPort.isOpen())
               else        
               {
                  //System.out.println("+++ " + ex);
                  System.out.println("+++ " + "[BAROMETER] Couldn't open " +  main.serialPort.getDescriptivePortName());
               }               
	            
               
               if (main.serialPort.isOpen())
               {
                  main.serialPort.addDataListener(new SerialPortDataListener() 
                  {
                     @Override
                     public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                  
                     @Override
                     public void serialEvent(SerialPortEvent event)
                     {                  
                        String ontvangen_SMD_string = "";
                     
                        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                        {
                           return;
                        }
                     
                        //byte[] newData = new byte[main.serialPort.bytesAvailable()];
                        //int numRead = main.serialPort.readBytes(newData, newData.length);  
                        int numRead;
                        byte[] newData = null;
                        try
                        {
                           newData = new byte[main.serialPort.bytesAvailable()];
                           numRead = main.serialPort.readBytes(newData, newData.length);                  
                        }
                        catch (NegativeArraySizeException ex) 
                        {
                           numRead = -1;
                           // NB don't modify the GUI anywhere except for on the event-dispatch-thread.!! (so not here in the function doinBackground())
                        }
                  
                        if (numRead > 0)
                        {
                           ontvangen_SMD_string = new String(newData, StandardCharsets.UTF_8);
                     
                           //System.out.println("Read " + numRead + " bytes.");
                     
                           // publish: Sends data chunks to the process(java.util.List) method. This method is to be used from inside the doInBackground method to deliver intermediate results for processing on the Event Dispatch Thread inside the process method.
                           //          Because the process method is invoked asynchronously on the Event Dispatch Thread  multiple invocations to the publish method might occur before the process method is executed. For performance purposes all these invocations are coalesced into one invocation with concatenated arguments.
                           //
                           // For example:
                           //
                           // publish("1");
                           // publish("2", "3");
                           // publish("4", "5", "6");
                           //
                           // might result in: process("1", "2", "3", "4", "5", "6")
                           //
                           publish(new String[] { ontvangen_SMD_string });
                        } // if (numRead > 0)       
                        else if (numRead == -1)
                        {
                           publish(new String[] { "interruption or excecution error\n" });
                        }
                     } // public void serialEvent(SerialPortEvent event)
                  });                   
               } // if (serialPort.isOpen())
               
               
               return null;

            } // protected Void doInBackground() throws Exception
            
            // NB https://stackoverflow.com/questions/6523623/graceful-exception-handling-in-swing-worker
            //    You should NOT try to catch exceptions in the background thread but rather let them pass through to the SwingWorker itself, 
            //    and then you can get them in the done() method by calling get()which normally returns the result of doInBackground() 
            //    (Void in your situation). If an exceptionwas thrown in the background thread then get() will throw it, wrapped inside an ExecutionException.
            //
            // NB Martin: but in this case it goes via the process() function, done() method has no effect!
            //
        

            @Override
            protected void process(List<String> data)
            {
               // process: Receives data chunks from the publish method asynchronously on the Event Dispatch Thread.
               for (String ontvangen_SMD_string: data)
               {
                  // NB altijd in for loop omdat meerdere ontbvangen_SMD_string's verzameld kunnen zijn voordat het hier procesed wordt(inherent aan SwingWorker)
                  //System.out.println("ontvangen_SMD_string = " + ontvangen_SMD_string);   // bv martin

                  main.total_string = ontvangen_SMD_string;


                  // NB Onderstaande om i.p.v. elke minuut de data per 5 minuten op te slaan
                  //    werkt hier niet (door de 'brokken' record maar bij AWS zou het wel een mogelijkheid kunnen zijn
                  //if (total_string.indexOf("3$") != -1)
                  //{
                  //   String minuten = sdf4.format(new Date()).substring(10, 10 + 2);  // sdf4: yyyyMMddHHmm
                  //   int int_minuten = Integer.parseInt(minuten.trim());
                  //
                  //   if (int_minuten % 5 == 0   );
                  //
                  System.out.print(main.total_string);
                  //System.out.println(total_string); // NB zo zie je ruwweg hoeveel bytes dat telkens beschikbaar zijn
                  
                  // error logging
                  if (main.total_string.indexOf("error") != -1)
                  {
                     
                     // NB code below is OK but maybe dangerous if messagebox may blocking application? (eg after a 1 time hick-upp)
                  //   if (comm_error_messagebox_shown == 0 )
                  //   {
                  //      String message = "[BAROMETER] " + "interruption or execution exception"; 
                  //      main.log_turbowin_system_message(message);
                  //      message = "barometer connection interrupted; check pc sleep mode and/or connection cable; if obsolete data restart this application";
                  //   
                  //      comm_error_messagebox_shown = 1;
                  //      //int return = JOptionPane.showMessageDialog(null, message, main.APPLICATION_NAME, JOptionPane.ERROR_MESSAGE);
                  //      Object[] options = { "OK" };
                  //      int reply = JOptionPane.showOptionDialog(null, message, main.APPLICATION_NAME, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                  //      if (reply == JOptionPane.OK_OPTION) 
                  //      {
                  //         comm_error_messagebox_shown = 0;
                  //      }
                  //   } //  if (comm_error_messagebox_shown == 0)   
                    
                     String message = "[BAROMETER] " + "interruption or execution exception"; 
                     main.log_turbowin_system_message(message);
                  } //  if (main.total_string.indexOf("error") != -1)
                  
                  

                  // update main screen with application name + mode and date time last received data
                  String info = "";
                  if ((main_RS232_RS422.test_record.equals("")) || (main_RS232_RS422.test_record == null)) 
                  {
                     info = "no data";
                  } 
                  else 
                  {
                     info = main_RS232_RS422.test_record;
                  }   
                  
                  if (main.RS232_connection_mode == 4 || main.RS232_connection_mode == 5)            // Mintaka Duo or Mintaka Star USB
                  {
                     main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received barometer data via USB: " + info);
                  }
                  else
                  {   
                     main.jLabel4.setText(main.APPLICATION_NAME + " " + main.application_mode  + ", last received barometer data via serial communication: " + info);
                  }
                  
                  RS232_write_sensor_data_to_file();                             // PTB220, PTB330, MintakaDuo, Mintaka Star USB
                  
                  // for timer checking the data is not obsolate (see Function: RS232_WiFi_init_new_barometer_received_check_timer)
                  last_new_data_received_TimeMillis = System.currentTimeMillis(); 
                  
                  // update the date and time on the main screen (and the global date time var's)
                  // NB if during start up the date and time displayed in the pupup message was not comfirmed by the user
                  //    it is assumed that the pc is not running on the correct time so then do not update the date time automatically
                  if (main.use_system_date_time_for_updating == true)
                  {  
                     set_datetime_while_collecting_sensor_data();
                  }
                  
                  // WOW enabled (and not 'only publish if manual obs was send') and a complete sensor data string was received
                  if ((main.WOW == true) && (!main.WOW_reporting_interval.equals(main.WOW_REPORTING_INTERVAL_MANUAL)) && (main.total_string.indexOf("\n") != -1))
                  {
                     cal_WOW_systeem_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // system date time in UTC of this moment
                     cal_WOW_systeem_datum_tijd.getTime();                                             // now effective
                     int WOW_system_minute_local = cal_WOW_systeem_datum_tijd.get(Calendar.MINUTE);
                     cal_WOW_systeem_datum_tijd = null; 
                  
                     if ((WOW_system_minute_local -1) % Integer.valueOf(main.WOW_reporting_interval) == 0)      // e.g. every 10 minutes (WOW_reporting_interval);   but no interference with APR!           
                     {
                        // logging
                        String message = "[WOW] scheduled upload"; 
                        main.log_turbowin_system_message(message);
                        
                        // checking
                        boolean WOW_settings_ok = RS232_check_WOW_settings();
                           
                        if (WOW_settings_ok)
                        {   
                           // read the barometer data which was saved before by TurboWin+ from a sensor data file
                           if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)  // PTB220 or PTB330
                           {
                              RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                           }
                           else if (main.RS232_connection_mode == 4)                                // MintakaDuo
                           {
                              RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                           }
                           else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                           {
                              RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("WOW", retry);
                           } 
                        } // if (WOW_settings_ok)
                     }   
                  } // if ((main.WOW == true) && (!main.WOW_reporting_interval.equals(main.WOW_REPORTING_INTERVAL_MANUAL)) && (main.total_string.indexOf("\n") != -1))

                  // APR (Automated Pressure Reporting)
                  if ((main.APR == true) && (main.total_string.indexOf("\n") != -1))
                  {
                     cal_APR_system_date_time = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));   // system date time in UTC of this moment
                     cal_APR_system_date_time.getTime();                                               // now effective
                     int APR_system_hour_local = cal_APR_system_date_time.get(Calendar.HOUR_OF_DAY);   // HOUR_OF_DAY is used for the 24-hour clock. E.g., at 10:04:15.250 PM the HOUR_OF_DAY is 22.
                     int APR_system_minute_local = cal_APR_system_date_time.get(Calendar.MINUTE);
                  
                     if ((APR_system_minute_local == 0) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
                     {
                        retry = false;
                        
                        // logging
                        String message = "[APR] scheduled upload"; 
                        main.log_turbowin_system_message(message);
                        
                        // checking
                        boolean APR_settings_ok = RS232_check_APR_settings();
                        boolean GPS_date_time_ok = true;
                        
                        if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6) // Mintaka Star (USB or WiFi)
                        {
                           // NB Mintaka Star saved data do not contain date/time information so it is not possible to check this
                           //    so it is made always true
                           GPS_date_time_ok = true;
                        }
                        else
                        {
                           GPS_date_time_ok = main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing("APR");
                        }
                        
                        if (APR_settings_ok && GPS_date_time_ok) // GPS date-time was compared with system date-time                    
                        {
                           // NB in this stage we are sure the date-time is correct! + we are sure position is available!
                             
                           // read the barometer data which was saved before by TurboWin+ from a sensor data file
                           if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)  // PTB220 or PTB330
                           {
                              RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                           }
                           else if (main.RS232_connection_mode == 4)                                // MintakaDuo
                           {
                              RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                           }
                           else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                           {
                              RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                           }                           
                        } // if (APR_settings_ok && GPS_date_time_ok) 
                     } // if ((APR_system_minute_local == 0) etc.
                     else if ((APR_server_response_code == main.RESPONSE_NO_INTERNET) && (APR_system_minute_local == APR_RETRY_MINUTES) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
                     {
                        retry = true;
                        
                        // logging
                        String message = "[APR] retry scheduled upload"; 
                        main.log_turbowin_system_message(message);
                        
                        // checking
                        boolean APR_settings_ok = RS232_check_APR_settings();
                        boolean GPS_date_time_ok = true;
                        
                        if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6) // Mintaka Star (USB or WiFi)
                        {
                           // NB Mintaka Star saved data do not contain date/time information so it is not possible to check this
                           //    so it is made always true
                           GPS_date_time_ok = true;
                        }
                        else
                        {
                           GPS_date_time_ok = main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing("APR");
                        }
                     
                        if (APR_settings_ok && GPS_date_time_ok) // GPS date-time was compared with system date-time                    
                        {
                           // NB in this stage we are sure the date-time is correct! + we are sure position is available!
                             
                           // read the barometer data which was saved before by TurboWin+ from a sensor data file
                           if (main.RS232_connection_mode == 1 || main.RS232_connection_mode == 2)  // PTB220 or PTB330
                           {
                              RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                           }
                           else if (main.RS232_connection_mode == 4)                                // MintakaDuo
                           {
                              RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                           }
                           else if (main.RS232_connection_mode == 5 || main.RS232_connection_mode == 6)  // Mintaka Star (USB or WiFi)
                           {
                              RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR("APR", retry);
                           } 
                        } // if (APR_settings_ok && GPS_date_time_ok) 
                        
                        // reset (to be sure)
                        APR_server_response_code = 0;
                        
                     } // else if ((APR_server_response_code = main.RESPONSE_NO_INTERNET) etc.
                     else if ((APR_server_response_code == main.RESPONSE_NO_INTERNET) && (APR_system_minute_local < APR_RETRY_MINUTES) && (APR_system_hour_local % Integer.valueOf(main.APR_reporting_interval) == 0))   // e.g. every 1 hour (APR_reporting_interval);               
                     {
                        // text will be visible only a few minutes (till the retry upload)
                        main.jTextField4.setText("[APR] retry automated pressure report upload within a few minutes");
                     }
                     else
                     {
                        // text with next APR update bottom status line TurboWin+ main screen (updated every minute)
                        for (int i = (APR_system_hour_local + 1); i < (APR_system_hour_local + 1 + Integer.valueOf(main.APR_reporting_interval)); i++)
                        {
                           // 24 hours or higher makes no difference for this computation
                           if (i % Integer.valueOf(main.APR_reporting_interval) == 0)
                           {
                              cal_APR_system_date_time.add(Calendar.HOUR_OF_DAY, i - APR_system_hour_local);   // add 1 - 5 hours
                              cal_APR_system_date_time.getTime();
                                 
                              String next_APR_string = "[APR] next automated air pressure report upload: " + cal_APR_system_date_time.get(Calendar.DAY_OF_MONTH) + " " + main.convert_month(cal_APR_system_date_time.get(Calendar.MONTH)) + " " + cal_APR_system_date_time.get(Calendar.YEAR) + " " + cal_APR_system_date_time.get(Calendar.HOUR_OF_DAY) + ".00 UTC"; 
                                 
                              main.jTextField4.setText(next_APR_string);
                              break;
                           }
                        } // for (int i = (APR_system_hour_local + 1); etc.
                           
                     } // else
                        
                     cal_APR_system_date_time = null; 
                        
                  } // if ( (main.APR == true) && (main.total_string.indexOf("\n") != -1))
                  
               } // for (String ontvangen_string : data)
            } // protected void process(List<String> data)
         }.execute(); // new SwingWorker<String, String>()

      } // if (defaultPort != null)

   } // private void initComponents2()
*/
   
   

/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/  
private boolean RS422_check_AWSR_settings()   
{
   // called from: .....
   //
   // NB see also: AWSR_additional_requirements_checks() [WOW_APR_settings.java]
   
   String message = "";
   
   
   // AWS connected? (not for EUCAWS)
   if (main.RS232_connection_mode != 9 && main.RS232_connection_mode != 10)  // 9 or 10 = OMC 140 AWS connected
   {
     message = "[AWSR] AWS connection unknown (select: Maintenance -> Serial/USB/LAN connection settings)"; 
     main.log_turbowin_system_message(message);
   }   
   
   // OMC 140 AWS serial connected but not working
   if (main.RS232_connection_mode == 9 && main.defaultPort == null)        // 9 = OMC 140 AWS serial connected; 
   {
     message = "[AWSR] OMC 140 AWS connection error (comm port == null)"; 
     main.log_turbowin_system_message(message);
   }   
           
   // GPS connected?      
   //if (main.RS232_GPS_connection_mode == 0)                                                              // 0 no GPS connected
   //{
   //   message = "[AWSR] GPS connection unknown (select: Maintenance -> Serial/USB/WiFi connection settings)";
   //   main.log_turbowin_system_message(message);
   //}
   
   // GPS connected but not working (does not aplly if also a Mintaka Star is connected) 
   //if (main.RS232_GPS_connection_mode != 3)                                                             // in case Mintaka Star there will be no GPS_defaultPort
   //{
   //   if (main.RS232_GPS_connection_mode != 0 && main_RS232_RS422.GPS_defaultPort == null)              // 0 no GPS connected
   //   {
   //      message = "[AWSR] GPS connection error (comm port == null)";
   //      main.log_turbowin_system_message(message);
   //   }
   //}
   
   // call sign
   if (main.call_sign.trim().equals("") == true || main.call_sign.trim().length() < 2)
   {
      message = "[AWSR] Call sign unknown (select: Maintenance -> Station data)";
      main.log_turbowin_system_message(message);
   }
   
   // height barometer
   if (main.barometer_above_sll.trim().equals("") == true || main.barometer_above_sll.trim().length() < 1)
   {
      message = "[AWSR] Height of the barometer above SLL unknown (select: Maintenance -> Station data)";
      main.log_turbowin_system_message(message);
   }
   if (main.keel_sll.trim().equals("") == true || main.keel_sll.trim().length() < 1)
   {
      message = "[AWSR] Distance of bottom of the keel to SLL unknown (select: Maintenance -> Station data)";
      main.log_turbowin_system_message(message);
   }

   // logs folder        
   if (main.logs_dir.trim().equals("") == true || main.logs_dir.trim().length() < 2)
   {
      message = "[AWSR] Logs folder unknown (select: Maintenance -> Log files settings)";
      main.log_turbowin_system_message(message);
   }
   
   // obs format
   //if (main.obs_format.equals(main.FORMAT_101) == false)
   //{
   //   message = "[AWSR] In AWSR mode the obs format must be '101' (select: Maintenance -> Obs format setting)";  
   //   main.log_turbowin_system_message(message);
   //}   
   if (main.obs_format.equals(main.FORMAT_AWS) == false)
   {
      // NB "AWS connected" mode always reports in format101 !
      message = "[AWSR] In AWSR mode the obs format must be \"AWS connected\" (select: Maintenance -> Obs format setting)";  
      main.log_turbowin_system_message(message);
   } 
   
   // AWSR reporting interval
   //if (main.APR_reporting_interval.equals(""))
   if (main.AWSR_reporting_interval.equals(""))   
   {
      message = "[AWSR] AWSR reporting interval unknown (select: Maintenance -> WOW/APR/AWSR settings)";
      main.log_turbowin_system_message(message);
   }
   
   // draught
   if (main.WOW_APR_average_draught.equals("")) 
   {
      message = "[AWSR] Draught unknown (select: Maintenance -> WOW/APR/AWSR settings)";
      main.log_turbowin_system_message(message);
   }
   
   // IC barometer
   if (main.barometer_instrument_correction.equals(""))
   {
      message = "[AWSR] Barometer instrument correction unknown (select: Maintenance -> WOW/APR/AWSR settings)";
      main.log_turbowin_system_message(message);
   }
   
   // upload URL 
   if (main.upload_URL.equals(""))
   {
      message = "[AWSR] upload URL unknown (select: Maintenance -> Server settings)";
      main.log_turbowin_system_message(message);
   }
   
   // NB not necessary see also: specific_connection_initComponents() [main.java]
   // update of date and time automatically
   //if (main.use_system_date_time_for_updating == false)
   //{
   //   message = "[AWSR] obs and system date/time not comfirmed by the user at startup";
   //   main.log_turbowin_system_message(message);
   //}
   
   
   return message.equals(""); 

}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/  
private boolean RS232_check_APR_settings()   
{
   // called from: - class RS232_Class_Receive_Sensor_Data()
   //              - class WiFi_Class_Receive_UDP()
   //
   // NB see also: APR_additional_requirements_checks() [WOW_APR_settings.java]
   
   String message = "";
   
   
   // barometer connected? 
   if (main.RS232_connection_mode == 0 || main.RS232_connection_mode == 3 || main.RS232_connection_mode == 9 || main.RS232_connection_mode == 10)  // 0 = no instrument connected, 3 or 9/10 = AWS connected
   {
     message = "[APR] Barometer connection unknown (select: Maintenance -> Serial/USB/LAN connection settings)"; 
     main.log_turbowin_system_message(message);
   }   
   
   // barometer serial connected but not working
   if (main.RS232_connection_mode != 0 && main.RS232_connection_mode != 3 && main.RS232_connection_mode != 6 && main.RS232_connection_mode != 8 &&
      main.RS232_connection_mode != 9 && main.RS232_connection_mode != 10 && main.defaultPort == null)   // 0 = no instrument connected, 3 or 9 = AWS connected; 6 = Mintaka Star WiFi
   {
     message = "[APR] Barometer connection error (comm port == null)"; 
     main.log_turbowin_system_message(message);
   }   
           
   // GPS connected?      
   if (main.RS232_GPS_connection_mode == 0)                                                              // 0 no GPS connected
   {
      message = "[APR] GPS connection unknown (select: Maintenance -> Serial/USB/LAN connection settings)";
      main.log_turbowin_system_message(message);
   }
   
   // GPS connected but not working (does not aplly if also a Mintaka Star or StarX is connected because than integrated GPS) 
   if (main.RS232_GPS_connection_mode != 3 &&main.RS232_GPS_connection_mode != 4)                       // in case Mintaka Star or StarX there will be no GPS_defaultPort
   {
      if (main.RS232_GPS_connection_mode != 0 && main_RS232_RS422.GPS_defaultPort == null)              // 0 no GPS connected
      {
         message = "[APR] GPS connection error (comm port == null)";
         main.log_turbowin_system_message(message);
      }
   }
   
   // call sign
   if (main.call_sign.trim().equals("") == true || main.call_sign.trim().length() < 2)
   {
      message = "[APR] Call sign unknown (select: Maintenance -> Station data)";
      main.log_turbowin_system_message(message);
   }
   
   // height barometer
   if (main.barometer_above_sll.trim().equals("") == true || main.barometer_above_sll.trim().length() < 1)
   {
      message = "[APR] Height of the barometer above SLL unknown (select: Maintenance -> Station data)";
      main.log_turbowin_system_message(message);
   }
   if (main.keel_sll.trim().equals("") == true || main.keel_sll.trim().length() < 1)
   {
      message = "[APR] Distance of bottom of the keel to SLL unknown (select: Maintenance -> Station data)";
      main.log_turbowin_system_message(message);
   }

   // logs folder        
   if (main.logs_dir.trim().equals("") == true || main.logs_dir.trim().length() < 2)
   {
      message = "[APR] Logs folder unknown (select: Maintenance -> Log files settings)";
      main.log_turbowin_system_message(message);
   }
   
   // obs format
   if (main.obs_format.equals(main.FORMAT_101) == false)
   {
      message = "[APR] In APR mode the obs format must be '101' (select: Maintenance -> Obs format setting)";  
      main.log_turbowin_system_message(message);
   }            
   
   // APR reporting interval
   if (main.APR_reporting_interval.equals(""))
   {
      message = "[APR] APR reporting interval unknown (select: Maintenance -> WOW/APR/AWSR settings)";
      main.log_turbowin_system_message(message);
   }
   
   // draught
   if (main.WOW_APR_average_draught.equals("")) 
   {
      message = "[APR] Draught unknown (select: Maintenance -> WOW/APR/AWSR settings)";
      main.log_turbowin_system_message(message);
   }
   
   // IC barometer
   if (main.barometer_instrument_correction.equals(""))
   {
      message = "[APR] Barometer instrument correction unknown (select: Maintenance -> WOW/APR/AWSR settings)";
      main.log_turbowin_system_message(message);
   }
   
   // upload URL 
   if (main.upload_URL.equals(""))
   {
      message = "[APR] upload URL unknown (select: Maintenance -> Server settings)";
      main.log_turbowin_system_message(message);
   }
   
   // update of date and time automatically
   if (main.use_system_date_time_for_updating == false)
   {
      message = "[APR] obs and system date/time not comfirmed by the user at startup";
      main.log_turbowin_system_message(message);
   }
   
   
   return message.equals(""); 
}   
   
   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/  
private boolean RS232_check_WOW_settings()
{
   String message = "";
   
           
   // Barometer connected?
   if (main.RS232_connection_mode == 0 || main.RS232_connection_mode == 3 || main.RS232_connection_mode == 9)   
   {
      message = "[WOW] Barometer connection unknown (select: Maintenance -> Serial/USB/LAN connection settings)"; 
      main.log_turbowin_system_message(message);
   }
   
   // MintakeDuo connected but not working
   if (main.RS232_connection_mode == 4 && main.defaultPort == null)
   {
      message = "[WOW] MintakaDuo connection error (comm port == null)"; 
      main.log_turbowin_system_message(message);
   }
               
   // WOW site id
   else if ((main.WOW_site_id.length() < 2 || main.WOW_site_id.length() > 20))
   {
      message = "[WOW] site ID not ok (select: Maintenance -> WOW/APR settings)"; 
      main.log_turbowin_system_message(message);
   }          
                  
   // site pin
   if ((main.WOW_site_pin.length() < 2 || main.WOW_site_pin.length() > 20))
   {
      message = "[WOW] pin not ok (select: Maintenance -> WOW/APR settings)";
      main.log_turbowin_system_message(message);
   }
                  
   // reporting interval
   if ((main.WOW_reporting_interval.equals("") || main.WOW_reporting_interval.equals(main.WOW_REPORTING_INTERVAL_MANUAL)))   
   {
      message = "[WOW] reporting interval not ok (select: Maintenance -> WOW/APR settings)";
      main.log_turbowin_system_message(message);
   }
               
   // draught
   if (main.WOW_APR_average_draught.equals("")) 
   {
      message = "[WOW] Draught unknown (select: Maintenance -> WOW/APR settings)";
      main.log_turbowin_system_message(message);
   }
   
   // IC barometer
   if (main.barometer_instrument_correction.equals(""))
   {
      message = "[WOW] Barometer instrument correction unknown (select: Maintenance -> WOW/APR settings)";
      main.log_turbowin_system_message(message);
   }
   
   // update of date and time automatically
   if (main.use_system_date_time_for_updating == false)
   {
      message = "[WOW] obs and system date/time not comfirmed by the user at startup";
      main.log_turbowin_system_message(message);
   }
   
   
   return message.equals(""); 
}   
   

   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/  
private void RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR(final String destination, final boolean retry)
{
   // PTB220 or PTB330
   
   
   if (main.RS232_connection_mode == 1)       // PTB220
   {
      main.type_record_lengte                 = main.RECORD_LENGTE_PTB220;
      main.type_record_datum_tijd_begin_pos   = main.RECORD_DATUM_TIJD_BEGIN_POS_PTB220;
      main.type_record_pressure_begin_pos     = main.RECORD_P_BEGIN_POS_PTB220;
   }
   else if (main.RS232_connection_mode == 2)  // PTB330   
   {
      main.type_record_lengte                 = main.RECORD_LENGTE_PTB330;
      main.type_record_datum_tijd_begin_pos   = main.RECORD_DATUM_TIJD_BEGIN_POS_PTB330;
      main.type_record_pressure_begin_pos     = main.RECORD_P_BEGIN_POS_PTB330;
   }
   
   // initialisation
   //mybarometer.pressure_reading = "";
   
 
   new SwingWorker<String, Void>()
   {
      @Override
      protected String doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record         = null;
         String laatste_record = null;
         

         // initialisation
         //main.sensor_data_record_obs_pressure = "";
         
         // initialisation
         String sensor_data_record_WOW_APR_pressure_hpa = "";
         

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // geeft bv 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;

                  while ((record = in.readLine()) != null)
                  {
                     if (record.length() == main.type_record_lengte)
                     {
                        laatste_record = record;
                     } 
                  } // while ((record = in.readLine()) != null)

                  if (laatste_record != null)
                  {
                     String record_datum_tijd_minuten = laatste_record.substring(main.type_record_datum_tijd_begin_pos, main.type_record_datum_tijd_begin_pos + 12);  // bv 201302201345
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     if (timeDiff <= TIMEDIFF_SENSOR_DATA)      // max 5 minutes old
                     {
                        sensor_data_record_WOW_APR_pressure_hpa = laatste_record.substring(main.type_record_pressure_begin_pos, main.type_record_pressure_begin_pos + 7);
                  
                        // rounding eg: 998.19 -> 998.2
                        //        double digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim()) + HOOGTE_CORRECTIE;
                        //        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;  // bv 998.19 -> 998.2
                        
                        //System.out.println("--- sensor data record, pressure for barometer form: " + main.sensor_data_record_obs_pressure);
                        System.out.println("--- sensor data record, raw uncorrected pressure: " + sensor_data_record_WOW_APR_pressure_hpa);
                        
                     } // if (timeDiff <= TIMEDIFF_SENSOR_DATA)
                     else
                     {
                        sensor_data_record_WOW_APR_pressure_hpa = "";
                        System.out.println("--- automatically retrieved barometer reading obsolete");
                        
                        String message = "[WOW/APR] automatically retrieved barometer reading for WOW/APR obsolete (" + timeDiff + " minutes old)";
                        main.log_turbowin_system_message(message);
                     }
                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) {  }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
 
         // clear memory
         main.obs_file_datum_tijd      = null;

         return sensor_data_record_WOW_APR_pressure_hpa;
      } // protected String doInBackground() throws Exception
   
      @Override
      protected void done()
      {
         if (destination.equals("WOW"))
         {   
            String sensor_data_record_WOW_pressure_MSL_inhg = "";
            String sensor_data_record_WOW_pressure_hpa = "";
            try 
            {
               sensor_data_record_WOW_pressure_hpa = get();    // get the return value of the doInBackground()
            } 
            catch (InterruptedException | ExecutionException ex) 
            {
               sensor_data_record_WOW_pressure_hpa = "";
               String message = "[WOW] " + ex.toString();
               main.log_turbowin_system_message(message);
            }
         
            if ((sensor_data_record_WOW_pressure_hpa.compareTo("") != 0))
            {
               double hulp_double_pressure_reading;
               try
               {
                  hulp_double_pressure_reading = Double.parseDouble(sensor_data_record_WOW_pressure_hpa.trim());
                  //hulp_double_pressure_reading = Math.round(hulp_double_pressure_reading * 10) / 10.0d;  // bv 998.19 -> 998.2
               }
               catch (NumberFormatException e)
               {
                  System.out.println("--- " + "Function RS232_Vaisala_Read_Sensor_Data_For_WOW_APR() " + e);
                  hulp_double_pressure_reading = Double.MAX_VALUE;
               }   
             
               if ((hulp_double_pressure_reading > 900.0) && (hulp_double_pressure_reading < 1100.0))
               {
                  DecimalFormat df = new DecimalFormat("0.000");           // rounding only 3 decimals
               
                  // CONVERT TO MSL BECAUSE PRESSURE AT STATION HEIGHT NOT VISIBLE IN WOW!!! (+ apply barometer instrument correction)
                  double WOW_height_correction_pressure = RS232_WOW_APR_compute_air_pressure_height_correction(hulp_double_pressure_reading);
                  
                  String message_b = "[WOW] air pressure at sensor height = " + sensor_data_record_WOW_pressure_hpa + " hPa";
                  main.log_turbowin_system_message(message_b);
                  String message_hc = "[WOW] air pressure height corection = " + Double.toString(WOW_height_correction_pressure) + " hPa";
                  main.log_turbowin_system_message(message_hc);
                  String message_ic = "[WOW] air pressure instrument corection = " + main.barometer_instrument_correction + " hPa";
                  main.log_turbowin_system_message(message_ic);
               
                  if (WOW_height_correction_pressure > -50.0 && WOW_height_correction_pressure < 50.0)
                  {   
                     sensor_data_record_WOW_pressure_MSL_inhg = df.format((hulp_double_pressure_reading + WOW_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)) * HPA_TO_INHG); 
                     RS232_Send_Sensor_Data_to_WOW(sensor_data_record_WOW_pressure_MSL_inhg);
                  }
                  else
                  {
                     String message_hce = "[WOW] computed height correction pressure not ok (" + WOW_height_correction_pressure + ")";
                     main.log_turbowin_system_message(message_hce);
                     sensor_data_record_WOW_pressure_MSL_inhg = "";
                  }
               } // if ((hulp_double_pressure_reading > 900.0) && (hulp_double_pressure_reading < 1100.0))
               else
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading outside range");
                  sensor_data_record_WOW_pressure_MSL_inhg = "";
               }
            } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
            else // so (still) no sensor data available
            {
               main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading obsolete or error during retrieving data");
               sensor_data_record_WOW_pressure_MSL_inhg = "";
            }
         } // if (destination.equals("WOW"))
         else if (destination.equals("APR"))      // APR = Automated Pressure Reporting
         {
            String sensor_data_record_APR_pressure_MSL_hpa = "";
            String sensor_data_record_APR_pressure_hpa = "";
         
            try 
            {
               sensor_data_record_APR_pressure_hpa = get();    // get the return value of the doInBackground()
            } 
            catch (InterruptedException | ExecutionException ex) 
            {
               sensor_data_record_APR_pressure_hpa = "";
               String message = "[APR] " + ex.toString();
               main.log_turbowin_system_message(message);
            }
            
            if ((sensor_data_record_APR_pressure_hpa.compareTo("") != 0))
            {
               double hulp_double_APR_pressure_reading;
               try
               {
                  hulp_double_APR_pressure_reading = Double.parseDouble(sensor_data_record_APR_pressure_hpa.trim());
                  //hulp_double_pressure_reading = Math.round(hulp_double_pressure_reading * 10) / 10.0d;  // bv 998.19 -> 998.2
               }
               catch (NumberFormatException e)
               {
                  System.out.println("--- " + "Function RS232_Vaisala_Read_Sensor_Data_For_WOW_APR() " + e);
                  hulp_double_APR_pressure_reading = Double.MAX_VALUE;
               }   
             
               if ((hulp_double_APR_pressure_reading > 900.0) && (hulp_double_APR_pressure_reading < 1100.0))
               {
                  DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
               
                  // CONVERT TO MSL (+ apply barometer instrument correction)
                  double APR_height_correction_pressure = RS232_WOW_APR_compute_air_pressure_height_correction(hulp_double_APR_pressure_reading);
                  
                  String message_b = "[APR] air pressure at sensor height = " + sensor_data_record_APR_pressure_hpa + " hPa";
                  main.log_turbowin_system_message(message_b);
                  String message_hc = "[APR] air pressure height corection = " + Double.toString(APR_height_correction_pressure) + " hPa";
                  main.log_turbowin_system_message(message_hc);
                  String message_ic = "[APR] air pressure instrument corection = " + main.barometer_instrument_correction + " hPa";
                  main.log_turbowin_system_message(message_ic);
               
                  if (APR_height_correction_pressure > -50.0 && APR_height_correction_pressure < 50.0)
                  {   
                                         // ic correction (make it 0.0 if outside the range)
                     double double_barometer_instrument_correction = 0.0;
                     
                     if (main.barometer_instrument_correction.equals("") == false)
                     {
                        try
                        {
                           double_barometer_instrument_correction = Double.parseDouble(main.barometer_instrument_correction);
               
                           if (!(double_barometer_instrument_correction >= -4.0 && double_barometer_instrument_correction  <= 4.0))
                           {
                              double_barometer_instrument_correction = 0.0;
                           }
                        }
                        catch (NumberFormatException e) 
                        {
                           double_barometer_instrument_correction = 0.0;
                        }               
                     }
   
                     //DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
                     
                     ////////////////// barometer reading (pressure at sensor height + ic) ////////////////
                     //
                     String sensor_data_record_APR_pressure_reading_hpa_corrected = df.format(hulp_double_APR_pressure_reading + double_barometer_instrument_correction); 
                     mybarometer.pressure_reading_corrected = sensor_data_record_APR_pressure_reading_hpa_corrected;   // now pressure at sensor height corrected for ic
   
   
                     ///////////////// pressure at MSL (+ ic) ///////////
                     //
                     //sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)); 
                     sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + double_barometer_instrument_correction); 
                     mybarometer.pressure_msl_corrected = sensor_data_record_APR_pressure_MSL_hpa;   // sensor_data_record_APR_pressure_MSL_hpa the baromter instrument correction is included
              
                     String message_msl = "[APR] air pressure MSL = " + mybarometer.pressure_msl_corrected + " hPa";
                     main.log_turbowin_system_message(message_msl);

                     //sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)); 
                     RS232_Send_Sensor_Data_to_APR(/*sensor_data_record_APR_pressure_MSL_hpa, hulp_double_APR_pressure_reading,*/ retry);
                     
                     // for updating main screen (dit heeft hier geen zin omdat hij hier maar eens in de 1, 3 of 6 uur komt)
                     //mybarometer.pressure_reading_corrected = df.format(hulp_double_APR_pressure_reading + Double.parseDouble(main.barometer_instrument_correction));
                     //mybarometer.pressure_msl_corrected = sensor_data_record_APR_pressure_MSL_hpa;        
                  }
                  else
                  {
                     String message_hce = "[APR] computed height correction pressure not ok (" + APR_height_correction_pressure + ")";
                     main.log_turbowin_system_message(message_hce);
                     sensor_data_record_APR_pressure_MSL_hpa = "";
                  }            
               } // if ((hulp_double_APR_pressure_reading > 900.0) && (hulp_double_APR_pressure_reading < 1100.0))
               else
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading outside range");
                  sensor_data_record_APR_pressure_MSL_hpa = "";
               }
            } //  if ((sensor_data_record_APR_pressure_hpa.compareTo("") != 0))
            else
            {
               main.log_turbowin_system_message("[APR] automatically retrieved barometer reading obsolete or error during retrieving data");
               sensor_data_record_APR_pressure_MSL_hpa = "";
            }
         } // else if (destination.equals("APR"))
      } // protected void done()

   }.execute(); // new SwingWorker <String, Void>()

}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/  
/*
private void RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR(final String destination, final boolean retry)
{
   // called from: - class RS232_Class_Receive_Sensor_Data() [main_RS232_RS422.java]
   
   // Mintaka Star has an integrated GPS
   // GPS data is part of th saved pressure string eg:
   // <station pressure in mb>,<sea level pressure in mb>,<3 hour pressure tendency>,<WMO tendency characteristic code>,<lat>,<long>,<course>,<speed>,<elevation>*<checksum>
   // where <lat> = ddd mm.mmmm[N|S], <long> = ddd mm.mmmm[E|W], 
   // <course> is True, <speed> in knots, <elevation> in meters
   //
   // 1018.61,1018.61,1.90,2, 15 39.0161N, 89 00.1226W,0,0,0*03
   // 1011.20,1011.20,,, 47 37.5965N,122 31.1453W,0,0,0*31
   
   // initialisation
   GPS_latitude                         = "";
   GPS_longitude                        = "";
   GPS_latitude_hemisphere              = "";
   GPS_longitude_hemisphere             = "";
   main.sensor_data_record_obs_pressure = "";
   

   new SwingWorker<String, Void>()
   {
      @Override
      protected String doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record                                = null;
         String laatste_record                        = null;
         String local_sensor_data_record_obs_pressure = "";

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // e.g. 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor APR obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;

                  // retrieve the last record in the sensor data file
                  //
                  while ((record = in.readLine()) != null)
                  {
                     laatste_record = record;
                  } // while ((record = in.readLine()) != null)
                  
                  // check on minimum record length
                  //
                  if (laatste_record != null)
                  {
                     if (!(laatste_record.length() > 15))         // NB > 15 is a little bit arbitrary number (YYYYMMDDHHmm + 3 commas + at leat 2 char pressure value= 15 chars)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Star, format (min. record length) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                        local_sensor_data_record_obs_pressure = STRING_PRESSURE_RECORD_FORMAT_ERROR;
                     }
                  }
                  
                  // check on correct number of commas in the laatste_record
                  //
                  if (laatste_record != null)
                  {
                     int number_read_commas = 0;
                     int pos = 0;
                     
                     do
                     {
                        pos = laatste_record.indexOf(",", pos + 1);
                        if (pos > 0)     // "," found
                        {
                           number_read_commas++;
                           //System.out.println("+++ number_read_commas = " + number_read_commas);
                        }
                     } while (pos > 0); 
                        
                     if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STAR)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Star format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                        local_sensor_data_record_obs_pressure = STRING_PRESSURE_RECORD_FORMAT_ERROR;
                     }                                  
                  } // if (laatste_record != null)
                  
                  
                  // last retrieved record ok
                  if (laatste_record != null)
                  {
                     int pos = laatste_record.length() -12;                                               // pos is now start position of YYYYMMDDHHmm
                     String record_datum_tijd_minuten = laatste_record.substring(pos, pos + 12);          // YYYYMMDDHHmm has length 12
                     
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     //System.out.println("+++ difference [min]: " + timeDiff); //differencs in min
                     //System.out.println("+++ file last record date time [min, since January 1, 1970, 00:00:00 GMT]: " + file_date.getTime() / (60 * 1000)); // in min
                     //System.out.println("+++ system date time [min, since January 1, 1970, 00:00:00 GMT]: " + system_sec / (60 * 1000)); // in min
                     //System.out.println("+++ file last record date time [YYYYMMDDUUMM]: " + record_datum_tijd_minuten); 
                     //System.out.println("+++ file: " + volledig_path_sensor_data);

                     if (timeDiff <= TIMEDIFF_SENSOR_DATA)      // max ? minutes old 
                     {
                        
                        // cheksum check
                        //
                        // example Mintaka Star last_record: 1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211
                        //
                        String record_checksum = laatste_record.substring(laatste_record.length() -14, laatste_record.length() -12);  // eg "24" from record "1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211"
                        String computed_checksum = Mintaka_Star_Checksum(laatste_record);
   
                        if (computed_checksum.equals(record_checksum))
                        {
                           //System.out.println("checksum ok"); 
                        
                           // retrieved (from file) record example Mintaka Star: 1029.97,1029.97,-0.90,7, 52 41.9535N,  6 14.1943E,0,0,19*1A201703152006
                           int pos1 = laatste_record.indexOf(",", 0);                                              // position of the first "," in the last record
                           int pos2 = laatste_record.indexOf(",", pos1 +1);                                        // position of the second "," in the last record
                           int pos3 = laatste_record.indexOf(",", pos2 +1);                                        // position of the third "," in the last record
                           int pos4 = laatste_record.indexOf(",", pos3 +1);                                        // position of the 4th "," in the last record
                           int pos5 = laatste_record.indexOf(",", pos4 +1);                                        // position of the 5th "," in the last record
                           int pos6 = laatste_record.indexOf(",", pos5 +1);                                        // position of the 6th "," in the last record
                           int pos7 = laatste_record.indexOf(",", pos6 +1);                                        // position of the 7th "," in the last record
                           //int pos8 = laatste_record.indexOf(",", pos7 +1);                                        // position of the 8th "," in the last record (last , in the record)
                        
                           // pressure (uncorrected at sensor height)
                           //
                           local_sensor_data_record_obs_pressure = laatste_record.substring(0, pos1);
                        
                           System.out.println("--- sensor data record, raw uncorrected pressure: " + local_sensor_data_record_obs_pressure);
                        
                           // rounding eg: 998.19 -> 998.2
                           //        double digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim()) + HOOGTE_CORRECTIE;
                           //        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;  // bv 998.19 -> 998.2
                           
                           
                           // GPS
                           //
                           GPS_latitude  = laatste_record.substring(pos4 +1, pos5);
                           GPS_longitude = laatste_record.substring(pos5 +1, pos6);
                           
                           System.out.println("--- sensor data record, GPS latitude: " + GPS_latitude);
                           System.out.println("--- sensor data record, GPS longitude: " + GPS_longitude);
                        } // if (computed_checksum.equals(record_checksum))
                        else // checksum not ok
                        {
                           //System.out.println("record checksum = " + record_checksum);
                           //System.out.println("computed checksum = " + computed_checksum);
                           System.out.println("--- checksum NOT ok " + "(" + laatste_record  + ")"); 
                           
                           GPS_latitude = "";
                           GPS_longitude = "";
                           local_sensor_data_record_obs_pressure = STRING_PRESSURE_CHECKSUM_ERROR;
                        } // else
                        
                     } // if (timeDiff <= 5L)
                     else // timeDiff not ok
                     {
                        //System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min
                        //System.out.println("+++ file last record date time: " + file_date.getTime() / (60 * 1000)); // in min
                        //System.out.println("+++ system date time: " + system_sec / (60 * 1000)); // in min
                        
                        GPS_latitude = "";
                        GPS_longitude = "";
                        local_sensor_data_record_obs_pressure = STRING_PRESSURE_TIMEDIFF_ERROR;
                     } // else

                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) 
            { 
               GPS_latitude = "";
               GPS_longitude = "";
               local_sensor_data_record_obs_pressure = STRING_PRESSURE_IO_FILE_ERROR;
            }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
         else // no file
         {
            GPS_latitude = "";
            GPS_longitude = "";
            local_sensor_data_record_obs_pressure = STRING_PRESSURE_NO_FILE_ERROR;
         }
 
         // clear memory
         main.obs_file_datum_tijd      = null;

         
         return local_sensor_data_record_obs_pressure;
         
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         // intialisation (in this inner class)
         boolean fix_ok                                  = true;
         String message                                  = "";
         boolean pressure_ok                             = false;
         String sensor_data_record_WOW_pressure_MSL_inhg = "";
         String sensor_data_record_WOW_pressure_hpa      = "";
         String sensor_data_record_APR_pressure_MSL_hpa  = "";
         String sensor_data_record_APR_pressure_hpa      = "";
         double hulp_double_WOW_pressure_reading         = 0.0;
         double hulp_double_APR_pressure_reading         = 0.0;
         double WOW_height_correction_pressure           = Double.MAX_VALUE;
         double APR_height_correction_pressure           = Double.MAX_VALUE;
 
         
         
         //
         // pressure (with logging)
         //
         if (destination.equals("WOW"))
         {   
            pressure_ok = false;
            
            try 
            {
               sensor_data_record_WOW_pressure_hpa = get();
               main.sensor_data_record_obs_pressure = sensor_data_record_WOW_pressure_hpa;  // nodig?
            } 
            catch (InterruptedException | ExecutionException ex) 
            {
               sensor_data_record_WOW_pressure_hpa = "";
            }
            
         
            if ((sensor_data_record_WOW_pressure_hpa.compareTo("") != 0))
            {
               try
               {
                  hulp_double_WOW_pressure_reading = Double.parseDouble(sensor_data_record_WOW_pressure_hpa.trim());
               }
               catch (NumberFormatException e)
               {
                  System.out.println("--- " + "Function RS232_Mintaka_Star_Read_Sensor_Data_For_WOW_APR() " + e);
                  hulp_double_WOW_pressure_reading = Double.MAX_VALUE;
               }   
             
               if ((hulp_double_WOW_pressure_reading > 900.0) && (hulp_double_WOW_pressure_reading < 1100.0))
               {
                  //DecimalFormat df = new DecimalFormat("0.000");           // rounding only 3 decimals
               
                  // COMPUTE HEIGHT CORRECTIO AND CONVERT TO MSL (further below in this function) BECAUSE PRESSURE AT STATION HEIGHT NOT VISIBLE IN WOW!!! (+ apply barometer instrument correction)
                  WOW_height_correction_pressure = RS232_WOW_APR_compute_air_pressure_height_correction(hulp_double_WOW_pressure_reading);
                  
                  String message_b = "[WOW] pressure at sensor height = " + sensor_data_record_WOW_pressure_hpa + " hPa";
                  main.log_turbowin_system_message(message_b);
                  String message_hc = "[WOW] air pressure height corection = " + Double.toString(WOW_height_correction_pressure) + " hPa";
                  main.log_turbowin_system_message(message_hc);
                  String message_ic = "[WOW] air pressure instrument corection = " + main.barometer_instrument_correction + " hPa";
                  main.log_turbowin_system_message(message_ic);
               
                  if (WOW_height_correction_pressure > -50.0 && WOW_height_correction_pressure < 50.0)
                  {   
                     pressure_ok = true;
                  }
                  else
                  {
                     String message_hce = "[WOW] computed height correction pressure not ok (" + WOW_height_correction_pressure + ")";
                     main.log_turbowin_system_message(message_hce);
                     pressure_ok = false;
                  }
               } // if ((hulp_double_pressure_reading > 900.0) && (hulp_double_pressure_reading < 1100.0))
               else if (hulp_double_WOW_pressure_reading == INT_PRESSURE_CHECKSUM_ERROR)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: cheksum error ");
                  //sensor_data_record_WOW_pressure_MSL_inhg = "";
                  pressure_ok = false;
               }
               else if (hulp_double_WOW_pressure_reading == INT_PRESSURE_TIMEDIFF_ERROR)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: obsolete");
                  pressure_ok = false;
               }
               else if (hulp_double_WOW_pressure_reading == INT_PRESSURE_RECORD_FORMAT_ERROR)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: error record format");
                  pressure_ok = false;
               }
               else if (hulp_double_WOW_pressure_reading == INT_PRESSURE_IO_FILE_ERROR)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: IO error opening sensor data file");
                  pressure_ok = false;
               }
               else if (hulp_double_WOW_pressure_reading == INT_PRESSURE_NO_FILE_ERROR)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: error, no sensor data file or file empty");
                  pressure_ok = false;
               }
               else if (hulp_double_WOW_pressure_reading > Double.MAX_VALUE -1)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: error NumberFormatException");
                  pressure_ok = false;
               }
               else
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: outside range");
                  pressure_ok = false;
               }
            } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
            else // so (still) no sensor data available
            {
               main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: error during retrieving data");
               //sensor_data_record_WOW_pressure_MSL_inhg = "";
               pressure_ok = false;
            }
         } // if (destination.equals("WOW"))
         else if (destination.equals("APR"))      // APR = Automated Pressure Reporting
         {
            pressure_ok = false;
            
            try 
            {
               sensor_data_record_APR_pressure_hpa = get();
               main.sensor_data_record_obs_pressure = sensor_data_record_APR_pressure_hpa;  // nodig?
            } 
            catch (InterruptedException | ExecutionException ex) 
            {
               sensor_data_record_APR_pressure_hpa = "";
            }
            
            
            if ((sensor_data_record_APR_pressure_hpa.compareTo("") != 0))
            {
               try
               {
                  hulp_double_APR_pressure_reading = Double.parseDouble(sensor_data_record_APR_pressure_hpa.trim());
                  //hulp_double_pressure_reading = Math.round(hulp_double_pressure_reading * 10) / 10.0d;  // bv 998.19 -> 998.2
               }
               catch (NumberFormatException e)
               {
                  System.out.println("--- " + "Function RS232_Mintaka_Star_Read_Sensor_Data_For_WOW_APR() " + e);
                  hulp_double_APR_pressure_reading = Double.MAX_VALUE;
               }   
             
               if ((hulp_double_APR_pressure_reading > 900.0) && (hulp_double_APR_pressure_reading < 1100.0))
               {
                  //DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
               
                  // CONVERT TO MSL (+ apply barometer instrument correction)
                  APR_height_correction_pressure = RS232_WOW_APR_compute_air_pressure_height_correction(hulp_double_APR_pressure_reading);
                  
                  String message_b = "[APR] air pressure at sensor height = " + sensor_data_record_APR_pressure_hpa + " hPa";
                  main.log_turbowin_system_message(message_b);
                  String message_hc = "[APR] air pressure height corection = " + Double.toString(APR_height_correction_pressure) + " hPa";
                  main.log_turbowin_system_message(message_hc);
                  String message_ic = "[APR] air pressure instrument corection = " + main.barometer_instrument_correction + " hPa";
                  main.log_turbowin_system_message(message_ic);
               
                  if (APR_height_correction_pressure > -50.0 && APR_height_correction_pressure < 50.0)
                  {   
//////////////////////////////////
                     
                     // ic correction (make it 0.0 if outside the range)
                     double double_barometer_instrument_correction = 0.0;
                     
                     if (main.barometer_instrument_correction.equals("") == false)
                     {
                        try
                        {
                           double_barometer_instrument_correction = Double.parseDouble(main.barometer_instrument_correction);
               
                           if (!(double_barometer_instrument_correction >= -4.0 && double_barometer_instrument_correction  <= 4.0))
                           {
                              double_barometer_instrument_correction = 0.0;
                           }
                        }
                        catch (NumberFormatException e) 
                        {
                           double_barometer_instrument_correction = 0.0;
                        }               
                     }
   
                     DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
                     
                     ////////////////// barometer reading (pressure at sensor height + ic) ////////////////
                     //
                     String sensor_data_record_APR_pressure_reading_hpa_corrected = df.format(hulp_double_APR_pressure_reading + double_barometer_instrument_correction); 
                     mybarometer.pressure_reading_corrected = sensor_data_record_APR_pressure_reading_hpa_corrected;   // now pressure at sensor height corrected for ic
   
   
                     ///////////////// pressure at MSL (+ ic) ///////////
                     //
                     //sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)); 
                     sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + double_barometer_instrument_correction); 
                     mybarometer.pressure_msl_corrected = sensor_data_record_APR_pressure_MSL_hpa;   // sensor_data_record_APR_pressure_MSL_hpa the baromter instrument correction is included
              
                     String message_msl = "[APR] air pressure MSL = " + mybarometer.pressure_msl_corrected + " hPa";
                     main.log_turbowin_system_message(message_msl);
                    
                     
//////////////////////////////////                     
                     
                     pressure_ok = true;
                  }
                  else
                  {
                     String message_hce = "[APR] computed height correction pressure not ok (" + APR_height_correction_pressure + ")";
                     main.log_turbowin_system_message(message_hce);
                     pressure_ok = false;
                  }            
               } // if ((hulp_double_APR_pressure_reading > 900.0) && (hulp_double_APR_pressure_reading < 1100.0))
               else if (hulp_double_APR_pressure_reading == INT_PRESSURE_CHECKSUM_ERROR)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: cheksum error ");
                  pressure_ok = false;
               }
               else if (hulp_double_APR_pressure_reading == INT_PRESSURE_TIMEDIFF_ERROR)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: obsolete");
                  pressure_ok = false;
               }
               else if (hulp_double_APR_pressure_reading == INT_PRESSURE_RECORD_FORMAT_ERROR)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: error record format");
                  pressure_ok = false;
               }    
               else if (hulp_double_APR_pressure_reading == INT_PRESSURE_IO_FILE_ERROR)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: IO error opening sensor data file");
                  pressure_ok = false;
               }
               else if (hulp_double_APR_pressure_reading == INT_PRESSURE_NO_FILE_ERROR)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: error, no sensor data file or file empty");
                  pressure_ok = false;
               }               
               else if (hulp_double_APR_pressure_reading > Double.MAX_VALUE -1)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: error NumberFormatException");
                  pressure_ok = false;
               }
               else
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: outside range");
                  pressure_ok = false;
               }
            } //  if ((sensor_data_record_APR_pressure_hpa.compareTo("") != 0))
            else
            {
               main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: error during retrieving data");
               pressure_ok = false;
            }
         } // else if (destination.equals("APR"))
         
         
         //
         // GPS
         //
         
         //////// latitude (eg " 52 04.7041N") /////////
         //
         if ( (GPS_latitude.compareTo("") != 0) && (GPS_latitude != null) && (GPS_latitude.indexOf("*") == -1) )
         {
            // fill the public string latitude values
            //
            // NB String hulp_GPS_latitude = GPS_latitude.replaceAll("\\s","");        // remove all the whitespaces in the string
            
            String fix_latitude = GPS_latitude.trim();                             // to remove leading and trailing whitespace in the string eg " 52 04.7041N" -> "52 04.7041N"
            int pos_space = fix_latitude.indexOf(" ", 0);                          // find first space (" ") in the string
            myposition.latitude_degrees = fix_latitude.substring(0, pos_space);    // eg "52 04.7041N" -> "52" and "2 04.7041N" -> "2"
            
            String fix_latitude_minutes = fix_latitude.substring(pos_space + 1, fix_latitude.length() -1);  // only the minutes of the latitude eg "52 04.7041N" -> "04.7041"
            BigDecimal bd_lat_min = new BigDecimal(fix_latitude_minutes).setScale(0, RoundingMode.HALF_UP); // 0 decimals rounding
            myposition.latitude_minutes = bd_lat_min.toString();                        // rounded minutes value  e.g. "04.7041N" -> "4" minutes
            if (myposition.latitude_minutes.length() == 1)
            {
               myposition.latitude_minutes =  "0" +  myposition.latitude_minutes;       // "4" -> "04"
            }
            
            GPS_latitude_hemisphere = fix_latitude.substring(fix_latitude.length() -1); // eg "52 04.7041N" -> "N" 
            if (GPS_latitude_hemisphere.toUpperCase().equals("N"))
            {
               myposition.latitude_hemisphere = myposition.HEMISPHERE_NORTH;
            }
            else if (GPS_latitude_hemisphere.toUpperCase().equals("S"))
            {
               myposition.latitude_hemisphere = myposition.HEMISPHERE_SOUTH;
            }
            else
            {
               myposition.latitude_hemisphere = "";
               fix_ok = false;
            }         
           
            // lat int values
            //
            if (fix_ok == true)
            {
               try 
               {
                  myposition.int_latitude_degrees = Integer.parseInt(myposition.latitude_degrees);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }
      
               try 
               {
                  myposition.int_latitude_minutes = Integer.parseInt(myposition.latitude_minutes);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }   
            } // if (fix_ok == true)   
            
            // for latitude (LaLaLa) for IMMT log
            //
            if (fix_ok == true) 
            {
               int int_latitude_minutes_6 =  bd_lat_min.intValue() / 6;                           // devide the minutes by six and disregard the remainder, now tenths of degrees [IMMT rounding!!!}
               String latitude_minutes_6 = Integer.toString(int_latitude_minutes_6);              // convert to String 
               //myposition.lalala_code = myposition.latitude_degrees.trim().replaceFirst("^0+(?!$)", "") + latitude_minutes_6;
               myposition.lalala_code = myposition.latitude_degrees + latitude_minutes_6;

               int len = 3;                                                           // always 3 chars
               if (myposition.lalala_code.length() < len) 
               {
                  // pad on left with zeros
                  myposition.lalala_code = "0000000000".substring(0, len - myposition.lalala_code.length()) + myposition.lalala_code;
               }         
               else if (myposition.lalala_code.length() > len)
               {
                  fix_ok = false;
                  //System.out.println("+++++++++++ lalala_code error: " + myposition.lalala_code);
               }       
            } // if (fix_ok == true)            
         
         } // if ( (GPS_latitude.compareTo("") != 0) && (GPS_latitude != null) && (GPS_latitude.indexOf("*") == -1) )
         else
         {
            fix_ok = false;
         }
         
            
         //////// longitude (eg "  4 14.7041W") /////////
         //
         if ( (fix_ok == true) && (GPS_longitude.compareTo("") != 0) && (GPS_longitude != null) && (GPS_longitude.indexOf("*") == -1) )
         {
            // fill the public string longitude values
            //
            String fix_longitude = GPS_longitude.trim();                           // to remove leading and trailing whitespace in the string eg "  6 14.7041N" -> "6 14.7041N"
            int pos_space = fix_longitude.indexOf(" ", 0);                         // find first space (" ") in the string
            myposition.longitude_degrees = fix_longitude.substring(0, pos_space);  // eg "6 14.7041W" -> "6" and "116 14.7041W" -> "116"
            
            String fix_longitude_minutes = fix_longitude.substring(pos_space + 1, fix_longitude.length() -1);  // only the minutes of the latitude eg "52 04.7041N" -> "04.7041"
            BigDecimal bd_lon_min = new BigDecimal(fix_longitude_minutes).setScale(0, RoundingMode.HALF_UP); // 0 decimals rounding
            myposition.longitude_minutes = bd_lon_min.toString();                 // rounded minutes value  e.g. "04.7041N" -> "5" minutes; "23.7041N" -> "24" minutes
            if (myposition.longitude_minutes.length() == 1)
            {
               myposition.longitude_minutes =  "0" +  myposition.longitude_minutes;        // "5" -> "05"
            }
            
            GPS_longitude_hemisphere = fix_longitude.substring(fix_longitude.length() -1); // eg "4 14.7041W" -> "W" 
            if (GPS_longitude_hemisphere.toUpperCase().equals("E"))
            {
               myposition.longitude_hemisphere = myposition.HEMISPHERE_EAST;
            }
            else if (GPS_longitude_hemisphere.toUpperCase().equals("W"))
            {
               myposition.longitude_hemisphere = myposition.HEMISPHERE_WEST;
            }
            else
            {
               myposition.longitude_hemisphere = "";
               fix_ok = false;
            }  
            
            // fill the public int longitude values
            //
            if (fix_ok == true)
            {
               try 
               {
                  myposition.int_longitude_degrees = Integer.parseInt(myposition.longitude_degrees);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }
      
               try 
               {
                  myposition.int_longitude_minutes = Integer.parseInt(myposition.longitude_minutes);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }
            } // if (fix_ok == true)
            
            // for longitude (LoLoLoLo) for IMMT log (NB see also myposition.java) In fact this IMMT preparation only necessary in APR mode
            //
            if (fix_ok == true)
            {
               int int_longitude_minutes_6 =  bd_lon_min.intValue() / 6;                           // devide the minutes by six and disregard the remainder, now tenths of degrees [IMMT rounding!!!}
               String longitude_minutes_6 = Integer.toString(int_longitude_minutes_6);              // convert to String 
               myposition.lolololo_code = myposition.longitude_degrees + longitude_minutes_6;

               int len2 = 4;                                                           // always 4 chars
               if (myposition.lolololo_code.length() < len2) 
               {
                  // pad on left with zeros
                  myposition.lolololo_code = "0000000000".substring(0, len2 - myposition.lolololo_code.length()) + myposition.lolololo_code;
               }  
               else if (myposition.lolololo_code.length() > len2)
               {
                  fix_ok = false;
                   //System.out.println("+++++++++++ lolololo_code error: " + myposition.lolololo_code);
               } 
            } // if (fix_ok == true)           
            
            
         } //  if ( (GPS_longitude.compareTo("") != 0) && (GPS_longitude != null) && (GPS_longitude.indexOf("*") == -1) )
         else
         {
            fix_ok = false;
         }
         
         
         // quadrant of the globe
         //
         if (fix_ok == true)
         {
            // quadrant of the globe for IMMT
            //
            if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
            {
                myposition.Qc_code = "1";
            }
            else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
            {
               myposition.Qc_code = "3";
            }
            else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
            {
               myposition.Qc_code = "5";
            }
            else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
            {
               myposition.Qc_code = "7";
            }
            else
            {
               fix_ok = false;
               //System.out.println("+++++++++++ Qc_code error");
            }
         } // if (fix_ok == true)
         
         
         
         // GPS logging
         //
         if (fix_ok == false)   
         {
            if (destination.equals("WOW"))
            {
               message = "[WOW] GPS error; no sensor data file available / formatting error last saved record / last saved record > 5 minutes old / checksum not ok";
            }
            else if (destination.equals("APR"))
            {
               message = "[APR] GPS error; no sensor data file available / formatting error last saved record / last saved record > 5 minutes old / checksum not ok";
            }
            main.log_turbowin_system_message(message);
         } // if (fix_ok == false) 
         else // fix_ok == true
         {
            message = "GPS position (dd-mm [N/S] ddd-mm [E/W]): " + myposition.latitude_degrees + "-" + myposition.latitude_minutes + " " + myposition.latitude_hemisphere.substring(0, 1) +  " " + myposition.longitude_degrees + "-" + myposition.longitude_minutes + " " + myposition.longitude_hemisphere.substring(0, 1);  
            if (destination.equals("WOW"))
            {
               main.log_turbowin_system_message("[WOW] position parsing ok; " + message);
            }
            else if (destination.equals("APR"))
            {
               main.log_turbowin_system_message("[APR] position parsing ok; " + message);
            }
         } // else (fix_ok == true)
         
         
         // send the data
         //
         if (pressure_ok && fix_ok)
         {
            if (destination.equals("WOW"))
            {
               // NB 
               // VOOR WOW MAAKT HET OP DIT MOMENT (APRIL 2017) NOG NIET UIT OF DE GPS POSITIE OK IS OMDAT ER NOG NIET VOOR MOBIELE STATIONS INGEVOERD KAN WORDEN
               // MAAR ALS MOBIELE STATIONS WEL KUNNEN DAN IS DEZE GPS CHECK WEL VAN TOEPASSING
               // VOOR DE MINTAKA STAR HIER MAAR VAST EEN VOORSCHOT OP GENOMEN (GPS MOET OK ZIJN)
               //
               
               DecimalFormat df = new DecimalFormat("0.000");           // rounding only 3 decimals
               sensor_data_record_WOW_pressure_MSL_inhg = df.format((hulp_double_WOW_pressure_reading + WOW_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)) * HPA_TO_INHG); 
               RS232_Send_Sensor_Data_to_WOW(sensor_data_record_WOW_pressure_MSL_inhg);
            }
            else if (destination.equals("APR"))
            {
               //DecimalFormat df = new DecimalFormat("0.0");            // rounding only 1 decimal
               //sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)); 
               RS232_Send_Sensor_Data_to_APR(retry);
            }
         } // if (pressure_ok && fix_ok)
         
      } // protected void done()
      
   }.execute(); // new SwingWorker <Void, Void>()
   
   
}       
*/


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/  
private void RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR(final String destination, final boolean retry, final boolean StarX)
{
   // called from: - class RS232_Class_Receive_Sensor_Data() [main_RS232_RS422.java]
   //              - class WiFi_Class_Receive_UDP() [main_RS232_RS422.java]
   //
   // NB a STARX is always physically (WiFi) connected to a STAR, for simplicity I use the word STARX as it would be a total new stand-alone device
   //
   // Mintaka Star and StarX has an integrated GPS
   // GPS data is part of the saved pressure string eg:
   //
   //
   //
   // STAR
   // <station pressure in mb>,<sea level pressure in mb>,<3 hour pressure tendency>,
   // <WMO tendency characteristic code>,<lat>,<long>,<course>,<speed>,<elevation>*<checksum>
   // where <lat> = ddd mm.mmmm[N|S], <long> = ddd mm.mmmm[E|W], 
   // <course> is True, <speed> in knots, <elevation> in meters
   //
   // 1018.61,1018.61,1.90,2, 15 39.0161N, 89 00.1226W,0,0,0*03
   // 1011.20,1011.20,,, 47 37.5965N,122 31.1453W,0,0,0*31
   //
   //
   // STARX (NB first part of STARX is the same as the STAR)
   //       TurboWinH 
   //             <station pressure in mb>,
   //             <sea level pressure in mb>,
   //             <3 hour pressure tendency>,
   //             <WMO tendency characteristic code>,
   //             <lat>,
   //             <long>,
   //             <course>,
   //             <speed>,
   //             <elevation>,
   //             <temperature>,
   //             <relativeHumidity>,
   //             <wetBulbTemperature>,
   //             <dewPoint>,
   //             <observationAge>
   //             *<checksum>
   //
   //             <lat> = ddd mm.mmmm[N|S], <long> = ddd mm.mmmm[E|W], <course> is True,
   //             <speed> in knots, <elevation> in meters, <relativeHumidity> is 0-100,
   //             temperatures are in degrees celsius, <observatoinAge> is in seconds.
   //
   // 1009.73,1007.73,0.00,0, 52 41.9491N,  6 14.1802E,0,1,7,19.5,65,15.4,12.8,58*16

   
   // initialisation
   GPS_latitude                         = "";
   GPS_longitude                        = "";
   GPS_latitude_hemisphere              = "";
   GPS_longitude_hemisphere             = "";
   main.sensor_data_record_obs_pressure = "";
   

   new SwingWorker<String, Void>()
   {
      @Override
      protected String doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record                                = null;
         String laatste_record                        = null;
         String local_sensor_data_record_obs_pressure = "";
         String local_sensor_data_record_obs_temp     = "";
         String local_obs_age                         = "";
         final int AGE_NOT_OK                         = 99999;

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // e.g. 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor APR obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;

                  // retrieve the last record in the sensor data file
                  //
                  while ((record = in.readLine()) != null)
                  {
                     laatste_record = record;
                  } // while ((record = in.readLine()) != null)
                  
                  // check on minimum record length
                  //
                  if (laatste_record != null)
                  {
                     if (!(laatste_record.length() > 15))         // NB > 15 is a little bit arbitrary number (YYYYMMDDHHmm + 3 commas + at leat 2 char pressure value= 15 chars)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Star or StarX format (min. record length) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                        local_sensor_data_record_obs_pressure = STRING_PRESSURE_RECORD_FORMAT_ERROR;
                        if (StarX == true)
                        {
                           local_sensor_data_record_obs_temp = STRING_AIR_TEMP_RECORD_FORMAT_ERROR;
                        }   
                     }
                  } // if (laatste_record != null)
                  
                  // check on correct number of commas in the laatste_record
                  //
                  if (laatste_record != null)
                  {
                     int number_read_commas = 0;
                     int pos = -1;
                     
                     do
                     {
                        pos = laatste_record.indexOf(",", pos + 1);
                        if (pos != -1)     // "," found
                        {
                           number_read_commas++;
                           //System.out.println("+++ number_read_commas = " + number_read_commas);
                        }
                     } while (pos != -1); 
                        
                     
                     if (StarX == true)
                     {
                        if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STARX)
                        {
                           laatste_record = null;
                           System.out.println("--- Mintaka StarX format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                           local_sensor_data_record_obs_pressure = STRING_PRESSURE_RECORD_FORMAT_ERROR;
                           local_sensor_data_record_obs_temp = STRING_AIR_TEMP_RECORD_FORMAT_ERROR;   
                        }                                  
                     } //  if (StarX == true)
                     else
                     {
                        if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STAR)
                        {
                           laatste_record = null;
                           System.out.println("--- Mintaka Star format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                           local_sensor_data_record_obs_pressure = STRING_PRESSURE_RECORD_FORMAT_ERROR;
                        }                                  
                     } // else 
                  } // if (laatste_record != null)
                  
                  
                  // last retrieved record ok
                  if (laatste_record != null)
                  {
                     int pos = laatste_record.length() -12;                                               // pos is now start position of YYYYMMDDHHmm
                     String record_datum_tijd_minuten = laatste_record.substring(pos, pos + 12);          // YYYYMMDDHHmm has length 12
                     
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();

                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     //System.out.println("+++ difference [min]: " + timeDiff); //differencs in min
                     //System.out.println("+++ file last record date time [min, since January 1, 1970, 00:00:00 GMT]: " + file_date.getTime() / (60 * 1000)); // in min
                     //System.out.println("+++ system date time [min, since January 1, 1970, 00:00:00 GMT]: " + system_sec / (60 * 1000)); // in min
                     //System.out.println("+++ file last record date time [YYYYMMDDUUMM]: " + record_datum_tijd_minuten); 
                     //System.out.println("+++ file: " + volledig_path_sensor_data);

                     if (timeDiff <= TIMEDIFF_SENSOR_DATA)      // max ? minutes old 
                     {
                        // cheksum check
                        //
                        // example Mintaka Star last_record: 1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211
                        //
                        String record_checksum = laatste_record.substring(laatste_record.length() -14, laatste_record.length() -12);  // eg "24" from record "1022.20,1022.20,0.80,2, 52 41.9497N,  6 14.1848E,0,0,-1*24201703291211"
                        String computed_checksum = Mintaka_Star_Checksum(laatste_record); // used by the Star and StarX !
   
                        if (computed_checksum.equals(record_checksum))
                        {
                           //System.out.println("checksum ok"); 
                        
                           // retrieved (from file) record example Mintaka Star: 1029.97,1029.97,-0.90,7, 52 41.9535N,  6 14.1943E,0,0,19*1A201703152006
                                                                                                                   // pressure at sensor height
                           int pos1 = laatste_record.indexOf(",", 0);                                              // ML hereafter; position of the first "," in the last record
                           int pos2 = laatste_record.indexOf(",", pos1 +1);                                        // ppp hereafter; position of the second "," in the last record
                           int pos3 = laatste_record.indexOf(",", pos2 +1);                                        // a hereafter; position of the third "," in the last record
                           int pos4 = laatste_record.indexOf(",", pos3 +1);                                        // lat hereafter; position of the 4th "," in the last record
                           int pos5 = laatste_record.indexOf(",", pos4 +1);                                        // lon hereafter; position n of the 5th "," in the last record
                           int pos6 = laatste_record.indexOf(",", pos5 +1);                                        // course hereafter; position of the 6th "," in the last record
                           
                           // pressure (uncorrected at sensor height)
                           //
                           local_sensor_data_record_obs_pressure = laatste_record.substring(0, pos1);
                           System.out.println("--- sensor data record, raw uncorrected pressure: " + local_sensor_data_record_obs_pressure);
                        
                           // rounding eg: 998.19 -> 998.2
                           //        double digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim()) + HOOGTE_CORRECTIE;
                           //        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;  // bv 998.19 -> 998.2

                           // GPS
                           //
                           GPS_latitude  = laatste_record.substring(pos4 +1, pos5);
                           GPS_longitude = laatste_record.substring(pos5 +1, pos6);
                           
                           System.out.println("--- sensor data record, GPS latitude: " + GPS_latitude);
                           System.out.println("--- sensor data record, GPS longitude: " + GPS_longitude);
                           
                           
                           if (StarX == true)
                           {   
                              // StarX example: // 1009.73,1007.73,0.00,0, 52 41.9491N,  6 14.1802E,0,1,7,19.5,65,15.4,12.8,58*16
                              int pos7 = laatste_record.indexOf(",", pos6 +1);                                        // speed hereafter                                
                              int pos8 = laatste_record.indexOf(",", pos7 +1);                                        // elevation hereafter
                              int pos9 = laatste_record.indexOf(",", pos8 +1);                                        // air temp
                              int pos10 = laatste_record.indexOf(",", pos9 +1);                                       // RH
                              int pos11 = laatste_record.indexOf(",", pos10 +1);                                      // wet bulb                                      
                              int pos12 = laatste_record.indexOf(",", pos11 +1);                                      // dew point
                              int pos13 = laatste_record.indexOf(",", pos12 +1);                                      // observation age  
                              int pos14 = laatste_record.indexOf("*", pos13 +1);                                      // pos of the "*" 
                              
                              
                              // air temperature (only available in case of StarX)
                              //
                              local_sensor_data_record_obs_temp = laatste_record.substring(pos9, pos10);
                              System.out.println("--- sensor data record, air temperature: " + local_sensor_data_record_obs_temp);
                              
                              // // observation age (only available in case of StarX)
                              local_obs_age = laatste_record.substring(pos13 +1, pos14); 
                              System.out.println("--- sensor data record, obs age: " + local_obs_age);
                              
                              int int_local_obs_age = AGE_NOT_OK;                 // 999999 = random number but > 99.9
                              if ( (local_obs_age.compareTo("") != 0) && (local_obs_age != null) && (local_obs_age.indexOf("*") == -1) )
                              {
                                 try
                                 {
                                    int_local_obs_age = Integer.parseInt(local_obs_age.trim());
                                 }
                                 catch (NumberFormatException e)
                                 {
                                    int_local_obs_age = AGE_NOT_OK;   
                                    System.out.println("--- " + "RS232_Mintaka_Star_And_StarX_Read_And_Send_Sensor_Data_For_WOW_APR() " + e);
                                    //return_obs_age = "";
                                 }
                              }

                              if ((int_local_obs_age < 0) || (int_local_obs_age > MAX_AGE_STARX_OBS_DATA))   // NB 60 * 10 = 600 sec = 10 minutes
                              {
                                 GPS_latitude = "";
                                 GPS_longitude = "";
                                 local_sensor_data_record_obs_pressure = STRING_PRESSURE_OBS_AGE_ERROR;
                                 local_sensor_data_record_obs_temp     = STRING_AIR_TEMP_OBS_AGE_ERROR;
                              
                              } // if ((hulp_return_obs_age >= 0) && (hulp_return_obs_age <= MAX_AGE_STARX_OBS_DATA)) 
                              
                           } // if (StarX == true)
                           
                           
                        } // if (computed_checksum.equals(record_checksum))
                        else // checksum not ok
                        {
                           //System.out.println("record checksum = " + record_checksum);
                           //System.out.println("computed checksum = " + computed_checksum);
                           System.out.println("--- checksum NOT ok " + "(" + laatste_record  + ")"); 
                           
                           GPS_latitude = "";
                           GPS_longitude = "";
                           local_sensor_data_record_obs_pressure = STRING_PRESSURE_CHECKSUM_ERROR;
                           local_sensor_data_record_obs_temp     = STRING_AIR_TEMP_CHECKSUM_ERROR;
                        } // else
                        
                     } // if (timeDiff <= 5L)
                     else // timeDiff not ok
                     {
                        //System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min
                        //System.out.println("+++ file last record date time: " + file_date.getTime() / (60 * 1000)); // in min
                        //System.out.println("+++ system date time: " + system_sec / (60 * 1000)); // in min
                        
                        GPS_latitude = "";
                        GPS_longitude = "";
                        local_sensor_data_record_obs_pressure = STRING_PRESSURE_TIMEDIFF_ERROR;
                        local_sensor_data_record_obs_temp     = STRING_AIR_TEMP_TIMEDIFF_ERROR;
                     } // else

                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) 
            { 
               GPS_latitude = "";
               GPS_longitude = "";
               local_sensor_data_record_obs_pressure = STRING_PRESSURE_IO_FILE_ERROR;
               local_sensor_data_record_obs_temp     = STRING_AIR_TEMP_IO_FILE_ERROR;
            }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
         else // no file
         {
            GPS_latitude = "";
            GPS_longitude = "";
            local_sensor_data_record_obs_pressure = STRING_PRESSURE_NO_FILE_ERROR;
            local_sensor_data_record_obs_temp     = STRING_AIR_TEMP_NO_FILE_ERROR;
         }
 
         // clear memory
         main.obs_file_datum_tijd      = null;

         
         //return local_sensor_data_record_obs_pressure;
         
         // e.g. 1018.12,23.4   [StarX]
         //      1018.12,       [Star]
         //
         return local_sensor_data_record_obs_pressure + "," + local_sensor_data_record_obs_temp;  // NB in case not StarX local_sensor_data_record_obs_temp = ""
         
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         // intialisation (in this inner class)
         boolean fix_ok                                  = true;
         boolean pressure_ok                             = false;
         boolean air_temp_ok                             = false;
         String message                                  = "";
         String sensor_data_record_WOW_pressure_MSL_inhg = "";
         String sensor_data_record_WOW_pressure_hpa      = "";
         String sensor_data_record_APR_pressure_MSL_hpa  = "";
         String sensor_data_record_APR_pressure_hpa      = "";
         String sensor_data_record_WOW_air_temp          = "";
         double hulp_double_WOW_pressure_reading         = 0.0;
         double hulp_double_WOW_air_temp                 = 999.9;
         double hulp_double_APR_pressure_reading         = 0.0;
         double WOW_height_correction_pressure           = Double.MAX_VALUE;
         double APR_height_correction_pressure           = Double.MAX_VALUE;
         
         
         //
         // pressure [Star] and air temperature [StarX] (with logging)
         //
         if (destination.equals("WOW"))
         {   
            pressure_ok = false;
            
            try 
            {
               //sensor_data_record_WOW_pressure_hpa = get();
               String return_string = get();                   // e.g. 1018.12,23.4   [StarX]  or 1018.12,       [Star]
               int pos_comma = return_string.indexOf(",");
               if (pos_comma != -1)
               {
                  sensor_data_record_WOW_pressure_hpa = return_string.substring(0, pos_comma);
               }
               else
               {
                 sensor_data_record_WOW_pressure_hpa = ""; 
               }
               main.sensor_data_record_obs_pressure = sensor_data_record_WOW_pressure_hpa;  // nodig?
               
               if ((return_string.length() > pos_comma +1) && (pos_comma != -1))
               {
                  sensor_data_record_WOW_air_temp = return_string.substring(pos_comma +1);
               }
            } 
            catch (InterruptedException | ExecutionException ex) 
            {
               sensor_data_record_WOW_pressure_hpa = "";
               sensor_data_record_WOW_air_temp = "";
            }
            
         
            // pressure
            //
            if ((sensor_data_record_WOW_pressure_hpa.compareTo("") != 0))
            {
               try
               {
                  hulp_double_WOW_pressure_reading = Double.parseDouble(sensor_data_record_WOW_pressure_hpa.trim());
               }
               catch (NumberFormatException e)
               {
                  System.out.println("--- " + "Function RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_For_WOW_APR() " + e);
                  hulp_double_WOW_pressure_reading = Double.MAX_VALUE;
               }   
             
               if ((hulp_double_WOW_pressure_reading > 900.0) && (hulp_double_WOW_pressure_reading < 1100.0))
               {
                  //DecimalFormat df = new DecimalFormat("0.000");           // rounding only 3 decimals
               
                  // COMPUTE HEIGHT CORRECTION AND CONVERT TO MSL (further below in this function) BECAUSE PRESSURE AT STATION HEIGHT NOT VISIBLE IN WOW!!! (+ apply barometer instrument correction)
                  WOW_height_correction_pressure = RS232_WOW_APR_compute_air_pressure_height_correction(hulp_double_WOW_pressure_reading);
                  
                  String message_b = "[WOW] pressure at sensor height = " + sensor_data_record_WOW_pressure_hpa + " hPa";
                  main.log_turbowin_system_message(message_b);
                  String message_hc = "[WOW] air pressure height corection = " + Double.toString(WOW_height_correction_pressure) + " hPa";
                  main.log_turbowin_system_message(message_hc);
                  String message_ic = "[WOW] air pressure instrument corection = " + main.barometer_instrument_correction + " hPa";
                  main.log_turbowin_system_message(message_ic);
               
                  if (WOW_height_correction_pressure > -50.0 && WOW_height_correction_pressure < 50.0)
                  {   
                     pressure_ok = true;
                  }
                  else
                  {
                     String message_hce = "[WOW] computed height correction pressure not ok (" + WOW_height_correction_pressure + ")";
                     main.log_turbowin_system_message(message_hce);
                     pressure_ok = false;
                  }
               } // if ((hulp_double_pressure_reading > 900.0) && (hulp_double_pressure_reading < 1100.0))
               else if (hulp_double_WOW_pressure_reading == INT_PRESSURE_CHECKSUM_ERROR)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: cheksum error ");
                  //sensor_data_record_WOW_pressure_MSL_inhg = "";
                  pressure_ok = false;
               }
               else if (hulp_double_WOW_pressure_reading == INT_PRESSURE_TIMEDIFF_ERROR)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: complete reading obsolete");
                  pressure_ok = false;
               }
               else if (hulp_double_WOW_pressure_reading == INT_PRESSURE_OBS_AGE_ERROR)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: StarX part obsolete");
                  pressure_ok = false;
               }
               else if (hulp_double_WOW_pressure_reading == INT_PRESSURE_RECORD_FORMAT_ERROR)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: error record format");
                  pressure_ok = false;
               }
               else if (hulp_double_WOW_pressure_reading == INT_PRESSURE_IO_FILE_ERROR)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: IO error opening sensor data file");
                  pressure_ok = false;
               }
               else if (hulp_double_WOW_pressure_reading == INT_PRESSURE_NO_FILE_ERROR)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: error, no sensor data file or file empty");
                  pressure_ok = false;
               }
               else if (hulp_double_WOW_pressure_reading > Double.MAX_VALUE -1)
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: error NumberFormatException");
                  pressure_ok = false;
               }
               else
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: outside range");
                  pressure_ok = false;
               }
            } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
            else // so (still) no sensor data available
            {
               main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading: error during retrieving data");
               //sensor_data_record_WOW_pressure_MSL_inhg = "";
               pressure_ok = false;
            }
            
            
            // air temp (only if StarX)
            //
            if (StarX == true)
            {  
               if ((sensor_data_record_WOW_air_temp.compareTo("") != 0))
               {
                  try
                  {
                     hulp_double_WOW_air_temp = Double.parseDouble(sensor_data_record_WOW_air_temp.trim());
                  }
                  catch (NumberFormatException e)
                  {
                     System.out.println("--- " + "Function RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_For_WOW_APR() " + e);
                     hulp_double_WOW_air_temp = Double.MAX_VALUE;
                  }   
             
                  if ((hulp_double_WOW_air_temp > -70.0) && (hulp_double_WOW_air_temp < 70.0))
                  {
                     //DecimalFormat df = new DecimalFormat("0.000");           // rounding only 3 decimals
               
                    air_temp_ok = true;
                  } 
                  else if (hulp_double_WOW_air_temp == INT_AIR_TEMP_CHECKSUM_ERROR)
                  {
                     main.log_turbowin_system_message("[WOW] automatically retrieved air temp: cheksum error ");
                     air_temp_ok = false;
                  }
                  else if (hulp_double_WOW_air_temp == INT_AIR_TEMP_TIMEDIFF_ERROR)
                  {
                     main.log_turbowin_system_message("[WOW] automatically retrieved air temp: complete reading obsolete");
                     air_temp_ok = false;
                  }
                  else if (hulp_double_WOW_air_temp == INT_AIR_TEMP_OBS_AGE_ERROR)
                  {
                     main.log_turbowin_system_message("[WOW] automatically retrieved air temp: StarX part obsolete");
                     air_temp_ok = false;
                  }
                  else if (hulp_double_WOW_air_temp == INT_AIR_TEMP_RECORD_FORMAT_ERROR)
                  {
                     main.log_turbowin_system_message("[WOW] automatically retrieved air temp: error record format");
                     air_temp_ok = false;
                  }
                  else if (hulp_double_WOW_air_temp == INT_AIR_TEMP_IO_FILE_ERROR)
                  {
                     main.log_turbowin_system_message("[WOW] automatically retrieved air temp: IO error opening sensor data file");
                     air_temp_ok = false;
                  }
                  else if (hulp_double_WOW_air_temp == INT_AIR_TEMP_NO_FILE_ERROR)
                  {
                     main.log_turbowin_system_message("[WOW] automatically retrieved air temp: error, no sensor data file or file empty");
                     air_temp_ok = false;
                  }
                  else if (hulp_double_WOW_air_temp > Double.MAX_VALUE -1)
                  {
                     main.log_turbowin_system_message("[WOW] automatically retrieved air temp: error NumberFormatException");
                     air_temp_ok = false;
                  }
                  else
                  {
                     main.log_turbowin_system_message("[WOW] automatically retrieved air temp: outside range");
                     air_temp_ok = false;
                  }
               } // if ((sensor_data_record_WOW_air_temp.compareTo("") != 0))
               else // so (still) no sensor data available
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved air temp: error during retrieving data");
                  air_temp_ok = false;
               }
            } // if (STarX == true)
            
         } // if (destination.equals("WOW"))
         else if (destination.equals("APR"))      // APR = Automated Pressure Reporting
         {
            pressure_ok = false;
            
            try 
            {
               //sensor_data_record_APR_pressure_hpa = get();
               //main.sensor_data_record_obs_pressure = sensor_data_record_APR_pressure_hpa;  // nodig?
               String return_string = get();                   // e.g. 1018.12,23.4   [StarX]  or 1018.12,       [Star]
               int pos_comma = return_string.indexOf(",");
               if (pos_comma != -1)
               {
                  sensor_data_record_APR_pressure_hpa = return_string.substring(0, pos_comma);
               }
               else
               {
                  sensor_data_record_APR_pressure_hpa = "";
               }
               
               main.sensor_data_record_obs_pressure = sensor_data_record_APR_pressure_hpa;  // nodig?
               // NB in APR mode no action on air temp if available (StarX)
            } 
            catch (InterruptedException | ExecutionException ex) 
            {
               sensor_data_record_APR_pressure_hpa = "";
            }
            
            
            if ((sensor_data_record_APR_pressure_hpa.compareTo("") != 0))
            {
               try
               {
                  hulp_double_APR_pressure_reading = Double.parseDouble(sensor_data_record_APR_pressure_hpa.trim());
                  //hulp_double_pressure_reading = Math.round(hulp_double_pressure_reading * 10) / 10.0d;  // bv 998.19 -> 998.2
               }
               catch (NumberFormatException e)
               {
                  System.out.println("--- " + "Function RS232_Mintaka_Star_And_StarX_Read_Sensor_Data_For_WOW_APR() " + e);
                  hulp_double_APR_pressure_reading = Double.MAX_VALUE;
               }   
             
               if ((hulp_double_APR_pressure_reading > 900.0) && (hulp_double_APR_pressure_reading < 1100.0))
               {
                  //DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
               
                  // CONVERT TO MSL (+ apply barometer instrument correction)
                  APR_height_correction_pressure = RS232_WOW_APR_compute_air_pressure_height_correction(hulp_double_APR_pressure_reading);
                  
                  String message_b = "[APR] air pressure at sensor height = " + sensor_data_record_APR_pressure_hpa + " hPa";
                  main.log_turbowin_system_message(message_b);
                  String message_hc = "[APR] air pressure height corection = " + Double.toString(APR_height_correction_pressure) + " hPa";
                  main.log_turbowin_system_message(message_hc);
                  String message_ic = "[APR] air pressure instrument corection = " + main.barometer_instrument_correction + " hPa";
                  main.log_turbowin_system_message(message_ic);
               
                  if (APR_height_correction_pressure > -50.0 && APR_height_correction_pressure < 50.0)
                  {   
                     // ic correction (make it 0.0 if outside the range)
                     double double_barometer_instrument_correction = 0.0;
                     
                     if (main.barometer_instrument_correction.equals("") == false)
                     {
                        try
                        {
                           double_barometer_instrument_correction = Double.parseDouble(main.barometer_instrument_correction);
               
                           if (!(double_barometer_instrument_correction >= -4.0 && double_barometer_instrument_correction  <= 4.0))
                           {
                              double_barometer_instrument_correction = 0.0;
                           }
                        }
                        catch (NumberFormatException e) 
                        {
                           double_barometer_instrument_correction = 0.0;
                        }               
                     }
   
                     DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
                     
                     ////////////////// barometer reading (pressure at sensor height + ic) ////////////////
                     //
                     String sensor_data_record_APR_pressure_reading_hpa_corrected = df.format(hulp_double_APR_pressure_reading + double_barometer_instrument_correction); 
                     mybarometer.pressure_reading_corrected = sensor_data_record_APR_pressure_reading_hpa_corrected;   // now pressure at sensor height corrected for ic
   
   
                     ///////////////// pressure at MSL (+ ic) ///////////
                     //
                     //sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)); 
                     sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + double_barometer_instrument_correction); 
                     mybarometer.pressure_msl_corrected = sensor_data_record_APR_pressure_MSL_hpa;   // sensor_data_record_APR_pressure_MSL_hpa the baromter instrument correction is included
              
                     String message_msl = "[APR] air pressure MSL = " + mybarometer.pressure_msl_corrected + " hPa";
                     main.log_turbowin_system_message(message_msl);
                    
                     
                     pressure_ok = true;
                  }
                  else
                  {
                     String message_hce = "[APR] computed height correction pressure not ok (" + APR_height_correction_pressure + ")";
                     main.log_turbowin_system_message(message_hce);
                     pressure_ok = false;
                  }            
               } // if ((hulp_double_APR_pressure_reading > 900.0) && (hulp_double_APR_pressure_reading < 1100.0))
               else if (hulp_double_APR_pressure_reading == INT_PRESSURE_CHECKSUM_ERROR)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: cheksum error ");
                  pressure_ok = false;
               }
               else if (hulp_double_APR_pressure_reading == INT_PRESSURE_TIMEDIFF_ERROR)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: complete reading obsolete");
                  pressure_ok = false;
               }
               else if (hulp_double_APR_pressure_reading == INT_PRESSURE_OBS_AGE_ERROR)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: StarX part obsolete");
                  pressure_ok = false;
               }
               else if (hulp_double_APR_pressure_reading == INT_PRESSURE_RECORD_FORMAT_ERROR)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: error record format");
                  pressure_ok = false;
               }    
               else if (hulp_double_APR_pressure_reading == INT_PRESSURE_IO_FILE_ERROR)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: IO error opening sensor data file");
                  pressure_ok = false;
               }
               else if (hulp_double_APR_pressure_reading == INT_PRESSURE_NO_FILE_ERROR)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: error, no sensor data file or file empty");
                  pressure_ok = false;
               }               
               else if (hulp_double_APR_pressure_reading > Double.MAX_VALUE -1)
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: error NumberFormatException");
                  pressure_ok = false;
               }
               else
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: outside range");
                  pressure_ok = false;
               }
            } //  if ((sensor_data_record_APR_pressure_hpa.compareTo("") != 0))
            else
            {
               main.log_turbowin_system_message("[APR] automatically retrieved barometer reading: error during retrieving data");
               pressure_ok = false;
            }
         } // else if (destination.equals("APR"))
         
         
         //
         // GPS
         //
         
         //////// latitude (eg " 52 04.7041N") /////////
         //
         if ( (GPS_latitude.compareTo("") != 0) && (GPS_latitude != null) && (GPS_latitude.indexOf("*") == -1) )
         {
            // fill the public string latitude values
            //
            // NB String hulp_GPS_latitude = GPS_latitude.replaceAll("\\s","");        // remove all the whitespaces in the string
            
            String fix_latitude = GPS_latitude.trim();                             // to remove leading and trailing whitespace in the string eg " 52 04.7041N" -> "52 04.7041N"
            int pos_space = fix_latitude.indexOf(" ", 0);                          // find first space (" ") in the string
            myposition.latitude_degrees = fix_latitude.substring(0, pos_space);    // eg "52 04.7041N" -> "52" and "2 04.7041N" -> "2"
            
            String fix_latitude_minutes = fix_latitude.substring(pos_space + 1, fix_latitude.length() -1);  // only the minutes of the latitude eg "52 04.7041N" -> "04.7041"
            BigDecimal bd_lat_min = new BigDecimal(fix_latitude_minutes).setScale(0, RoundingMode.HALF_UP); // 0 decimals rounding
            myposition.latitude_minutes = bd_lat_min.toString();                        // rounded minutes value  e.g. "04.7041N" -> "4" minutes
            if (myposition.latitude_minutes.length() == 1)
            {
               myposition.latitude_minutes =  "0" +  myposition.latitude_minutes;       // "4" -> "04"
            }
            
            GPS_latitude_hemisphere = fix_latitude.substring(fix_latitude.length() -1); // eg "52 04.7041N" -> "N" 
            if (GPS_latitude_hemisphere.toUpperCase().equals("N"))
            {
               myposition.latitude_hemisphere = myposition.HEMISPHERE_NORTH;
            }
            else if (GPS_latitude_hemisphere.toUpperCase().equals("S"))
            {
               myposition.latitude_hemisphere = myposition.HEMISPHERE_SOUTH;
            }
            else
            {
               myposition.latitude_hemisphere = "";
               fix_ok = false;
            }         
           
            // lat int values
            //
            if (fix_ok == true)
            {
               try 
               {
                  myposition.int_latitude_degrees = Integer.parseInt(myposition.latitude_degrees);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }
      
               try 
               {
                  myposition.int_latitude_minutes = Integer.parseInt(myposition.latitude_minutes);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }   
            } // if (fix_ok == true)   
            
            // for latitude (LaLaLa) for IMMT log
            //
            if (fix_ok == true) 
            {
               int int_latitude_minutes_6 =  bd_lat_min.intValue() / 6;                           // devide the minutes by six and disregard the remainder, now tenths of degrees [IMMT rounding!!!}
               String latitude_minutes_6 = Integer.toString(int_latitude_minutes_6);              // convert to String 
               //myposition.lalala_code = myposition.latitude_degrees.trim().replaceFirst("^0+(?!$)", "") + latitude_minutes_6;
               myposition.lalala_code = myposition.latitude_degrees + latitude_minutes_6;

               int len = 3;                                                           // always 3 chars
               if (myposition.lalala_code.length() < len) 
               {
                  // pad on left with zeros
                  myposition.lalala_code = "0000000000".substring(0, len - myposition.lalala_code.length()) + myposition.lalala_code;
               }         
               else if (myposition.lalala_code.length() > len)
               {
                  fix_ok = false;
                  //System.out.println("+++++++++++ lalala_code error: " + myposition.lalala_code);
               }       
            } // if (fix_ok == true)            
         
         } // if ( (GPS_latitude.compareTo("") != 0) && (GPS_latitude != null) && (GPS_latitude.indexOf("*") == -1) )
         else
         {
            fix_ok = false;
         }
         
            
         //////// longitude (eg "  4 14.7041W") /////////
         //
         if ( (fix_ok == true) && (GPS_longitude.compareTo("") != 0) && (GPS_longitude != null) && (GPS_longitude.indexOf("*") == -1) )
         {
            // fill the public string longitude values
            //
            String fix_longitude = GPS_longitude.trim();                           // to remove leading and trailing whitespace in the string eg "  6 14.7041N" -> "6 14.7041N"
            int pos_space = fix_longitude.indexOf(" ", 0);                         // find first space (" ") in the string
            myposition.longitude_degrees = fix_longitude.substring(0, pos_space);  // eg "6 14.7041W" -> "6" and "116 14.7041W" -> "116"
            
            String fix_longitude_minutes = fix_longitude.substring(pos_space + 1, fix_longitude.length() -1);  // only the minutes of the latitude eg "52 04.7041N" -> "04.7041"
            BigDecimal bd_lon_min = new BigDecimal(fix_longitude_minutes).setScale(0, RoundingMode.HALF_UP); // 0 decimals rounding
            myposition.longitude_minutes = bd_lon_min.toString();                 // rounded minutes value  e.g. "04.7041N" -> "5" minutes; "23.7041N" -> "24" minutes
            if (myposition.longitude_minutes.length() == 1)
            {
               myposition.longitude_minutes =  "0" +  myposition.longitude_minutes;        // "5" -> "05"
            }
            
            GPS_longitude_hemisphere = fix_longitude.substring(fix_longitude.length() -1); // eg "4 14.7041W" -> "W" 
            if (GPS_longitude_hemisphere.toUpperCase().equals("E"))
            {
               myposition.longitude_hemisphere = myposition.HEMISPHERE_EAST;
            }
            else if (GPS_longitude_hemisphere.toUpperCase().equals("W"))
            {
               myposition.longitude_hemisphere = myposition.HEMISPHERE_WEST;
            }
            else
            {
               myposition.longitude_hemisphere = "";
               fix_ok = false;
            }  
            
            // fill the public int longitude values
            //
            if (fix_ok == true)
            {
               try 
               {
                  myposition.int_longitude_degrees = Integer.parseInt(myposition.longitude_degrees);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }
      
               try 
               {
                  myposition.int_longitude_minutes = Integer.parseInt(myposition.longitude_minutes);
               }
               catch (NumberFormatException e)
               {
                  fix_ok = false;
               }
            } // if (fix_ok == true)
            
            // for longitude (LoLoLoLo) for IMMT log (NB see also myposition.java) In fact this IMMT preparation only necessary in APR mode
            //
            if (fix_ok == true)
            {
               int int_longitude_minutes_6 =  bd_lon_min.intValue() / 6;                           // devide the minutes by six and disregard the remainder, now tenths of degrees [IMMT rounding!!!}
               String longitude_minutes_6 = Integer.toString(int_longitude_minutes_6);              // convert to String 
               myposition.lolololo_code = myposition.longitude_degrees + longitude_minutes_6;

               int len2 = 4;                                                           // always 4 chars
               if (myposition.lolololo_code.length() < len2) 
               {
                  // pad on left with zeros
                  myposition.lolololo_code = "0000000000".substring(0, len2 - myposition.lolololo_code.length()) + myposition.lolololo_code;
               }  
               else if (myposition.lolololo_code.length() > len2)
               {
                  fix_ok = false;
                   //System.out.println("+++++++++++ lolololo_code error: " + myposition.lolololo_code);
               } 
            } // if (fix_ok == true)           
            
            
         } //  if ( (GPS_longitude.compareTo("") != 0) && (GPS_longitude != null) && (GPS_longitude.indexOf("*") == -1) )
         else
         {
            fix_ok = false;
         }
         
         
         // quadrant of the globe
         //
         if (fix_ok == true)
         {
            // quadrant of the globe for IMMT
            //
            if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
            {
                myposition.Qc_code = "1";
            }
            else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
            {
               myposition.Qc_code = "3";
            }
            else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
            {
               myposition.Qc_code = "5";
            }
            else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
            {
               myposition.Qc_code = "7";
            }
            else
            {
               fix_ok = false;
               //System.out.println("+++++++++++ Qc_code error");
            }
         } // if (fix_ok == true)
         
         
         
         // GPS logging
         //
         if (fix_ok == false)   
         {
            if (destination.equals("WOW"))
            {
               message = "[WOW] GPS error; no sensor data file available / formatting error last saved record / last saved record > 5 minutes old / checksum not ok";
            }
            else if (destination.equals("APR"))
            {
               message = "[APR] GPS error; no sensor data file available / formatting error last saved record / last saved record > 5 minutes old / checksum not ok";
            }
            main.log_turbowin_system_message(message);
         } // if (fix_ok == false) 
         else // fix_ok == true
         {
            message = "GPS position (dd-mm [N/S] ddd-mm [E/W]): " + myposition.latitude_degrees + "-" + myposition.latitude_minutes + " " + myposition.latitude_hemisphere.substring(0, 1) +  " " + myposition.longitude_degrees + "-" + myposition.longitude_minutes + " " + myposition.longitude_hemisphere.substring(0, 1);  
            if (destination.equals("WOW"))
            {
               main.log_turbowin_system_message("[WOW] position parsing ok; " + message);
            }
            else if (destination.equals("APR"))
            {
               main.log_turbowin_system_message("[APR] position parsing ok; " + message);
            }
         } // else (fix_ok == true)
         
         
         // send the data
         //
         if (pressure_ok && fix_ok)
         {
            if (destination.equals("WOW"))
            {
               // NB 1
               // VOOR WOW MAAKT HET OP DIT MOMENT (APRIL 2017) NOG NIET UIT OF DE GPS POSITIE OK IS OMDAT ER NOG NIET VOOR MOBIELE STATIONS INGEVOERD KAN WORDEN
               // MAAR ALS MOBIELE STATIONS WEL KUNNEN DAN IS DEZE GPS CHECK WEL VAN TOEPASSING
               // VOOR DE MINTAKA STAR HIER MAAR VAST EEN VOORSCHOT OP GENOMEN (GPS MOET OK ZIJN)
               //
               
               // NB 2
               //
               // Februari 2018: zodra een StarX echte data verstuurd onderstaande aanpassen voor de extra buiten temp. die dan beschikbaar is (var: sensor_data_record_WOW_air_temp)
               //
               
               DecimalFormat df = new DecimalFormat("0.000");           // rounding only 3 decimals
               sensor_data_record_WOW_pressure_MSL_inhg = df.format((hulp_double_WOW_pressure_reading + WOW_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)) * HPA_TO_INHG); 
               RS232_Send_Sensor_Data_to_WOW(sensor_data_record_WOW_pressure_MSL_inhg);
            }
            else if (destination.equals("APR"))
            {
               //DecimalFormat df = new DecimalFormat("0.0");            // rounding only 1 decimal
               //sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)); 
               RS232_Send_Sensor_Data_to_APR(retry);
            }
         } // if (pressure_ok && fix_ok)
         
      } // protected void done()
      
   }.execute(); // new SwingWorker <Void, Void>()
   
   
}       



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/  
private void RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR(final String destination, final boolean retry)
{
   new SwingWorker<String, Void>()
   {
      @Override
      protected String doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record         = null;
         String laatste_record = null;

         // initialisation
         String sensor_data_record_WOW_APR_pressure_hpa = "";

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // e.g. 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  record         = null;
                  laatste_record = null;
                  
                  
                  // retrieve the last record in the sensor data file
                  //
                  while ((record = in.readLine()) != null)
                  {
                     laatste_record = record;
                  } // while ((record = in.readLine()) != null)
                  
                  // check on minimum record length
                  //
                  if (laatste_record != null)
                  {
                     if (!(laatste_record.length() > 15))         // NB > 15 is a little bit arbitrary number (YYYYMMDDHHmm + 3 commas + at leat 2 char pressure value= 15 chars)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Duo reading data for WOW/APR, format (min. record length) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                     }
                  }
                  
                  // check on correct number of commas in the laatste_record
                  //
                  if (laatste_record != null)
                  {
                     int number_read_commas = 0;
                     int pos = 0;
                     
                     do
                     {
                        pos = laatste_record.indexOf(",", pos + 1);
                        if (pos > 0)     // "," found
                        {
                           number_read_commas++;
                           //System.out.println("+++ number_read_commas = " + number_read_commas);
                        }
                     } while (pos > 0); 
                        
                     if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS_MINTAKA)
                     {
                        laatste_record = null;
                        System.out.println("--- Mintaka Duo reading data for WOW/APR, format (number commas) last retrieved record NOT ok (file: " + volledig_path_sensor_data + ")"); 
                     }                                  
                  } // if (laatste_record != null)
                  
                  
                  // last retrieved record ok
                  //
                  if (laatste_record != null)
                  {
                     //System.out.println("+++ Mintaka Duo, last retrieved record = " + laatste_record);
                     
                     //String record_datum_tijd_minuten = laatste_record.substring(main.type_record_datum_tijd_begin_pos, main.type_record_datum_tijd_begin_pos + 12);  // bv 201302201345
                     
                     int pos = laatste_record.length() -12;                                               // pos is now start position of YYYYMMDDHHmm
                     String record_datum_tijd_minuten = laatste_record.substring(pos, pos + 12);  // YYYYMMDDHHmm has length 12
                     
                     Date file_date = main.sdf4.parse(record_datum_tijd_minuten);
                     long system_sec = System.currentTimeMillis();
                     long timeDiff = Math.abs(file_date.getTime() - system_sec) / (60 * 1000); // timeDiff in minutes

                     if (timeDiff <= TIMEDIFF_SENSOR_DATA)      // max 5 minutes old
                     {
                        // retrieved record example Mintaka Duo: 1021.89,1021.89,1.70,1*05 201502111405
                        int pos1 = laatste_record.indexOf(",", 0);                                           // position of the first "," in the record
                        sensor_data_record_WOW_APR_pressure_hpa = laatste_record.substring(0, pos1);
                        
                        // rounding eg: 998.19 -> 998.2
                        //        double digitale_sensor_waarde = Double.parseDouble(RS232_view.sensor_waarde_array[i].trim()) + HOOGTE_CORRECTIE;
                        //        digitale_sensor_waarde = Math.round(digitale_sensor_waarde * 10) / 10.0d;  // bv 998.19 -> 998.2
                        
                        System.out.println("--- sensor data record, raw uncorrected pressure: " + sensor_data_record_WOW_APR_pressure_hpa);
                        
                     } // if (timeDiff <= TIMEDIFF_SENSOR_DATA)
                     else
                     {
                        sensor_data_record_WOW_APR_pressure_hpa = "";
                        System.out.println("--- automatically retrieved barometer reading for WOW/APR obsolete");
                        
                        String message = "[WOW/APR] automatically retrieved barometer reading for WOW/APR obsolete (" + timeDiff + " minutes old)";
                        main.log_turbowin_system_message(message);
                     }

                  } // if (laatste_record != null)

               } // try
               finally
               {
                  in.close();
               }

            } // try
            catch (IOException ex) {  }

         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
 
         // clear memory
         main.obs_file_datum_tijd      = null;

         return sensor_data_record_WOW_APR_pressure_hpa;
   
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         if (destination.equals("WOW"))
         {   
            String sensor_data_record_WOW_pressure_MSL_inhg = "";
            String sensor_data_record_WOW_pressure_hpa = "";
            try 
            {
               sensor_data_record_WOW_pressure_hpa = get();    // get the return value of the doInBackground()
            } 
            catch (InterruptedException | ExecutionException ex) 
            {
               String message = "[WOW] " + ex.toString();
               main.log_turbowin_system_message(message);
               sensor_data_record_WOW_pressure_hpa = "";
            }
         
            if ((sensor_data_record_WOW_pressure_hpa.compareTo("") != 0))
            {
               double hulp_double_pressure_reading;
               try
               {
                  hulp_double_pressure_reading = Double.parseDouble(sensor_data_record_WOW_pressure_hpa.trim());
                  //hulp_double_pressure_reading = Math.round(hulp_double_pressure_reading * 10) / 10.0d;  // bv 998.19 -> 998.2
               }
               catch (NumberFormatException e)
               {
                  System.out.println("--- " + "Function RS232_MintakaDuo_Read_Sensor_Data_For_WOW_APR() " + e);
                  hulp_double_pressure_reading = Double.MAX_VALUE;
               }   
             
               if ((hulp_double_pressure_reading > 900.0) && (hulp_double_pressure_reading < 1100.0))
               {
                  DecimalFormat df = new DecimalFormat("0.000");           // rounding only 3 decimals
               
                  // CONVERT TO MSL BECAUSE PRESSURE AT STATION HEIGHT NOT VISIBLE IN WOW!!! (+ apply barometer instrument correction)
                  double WOW_height_correction_pressure = RS232_WOW_APR_compute_air_pressure_height_correction(hulp_double_pressure_reading);
                  
                  String message_b = "[WOW] pressure at sensor height = " + sensor_data_record_WOW_pressure_hpa + " hPa";
                  main.log_turbowin_system_message(message_b);
                  String message_hc = "[WOW] air pressure height corection = " + Double.toString(WOW_height_correction_pressure) + " hPa";
                  main.log_turbowin_system_message(message_hc);
                  String message_ic = "[WOW] air pressure instrument corection = " + main.barometer_instrument_correction + " hPa";
                  main.log_turbowin_system_message(message_ic);
               
                  if (WOW_height_correction_pressure > -50.0 && WOW_height_correction_pressure < 50.0)
                  {   
                     sensor_data_record_WOW_pressure_MSL_inhg = df.format((hulp_double_pressure_reading + WOW_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)) * HPA_TO_INHG); 
                     RS232_Send_Sensor_Data_to_WOW(sensor_data_record_WOW_pressure_MSL_inhg);
                  }
                  else
                  {
                     String message_hce = "[WOW] computed height correction pressure not ok (" + WOW_height_correction_pressure + ")";
                     main.log_turbowin_system_message(message_hce);
                     sensor_data_record_WOW_pressure_MSL_inhg = "";
                  }
               } // if ((hulp_double_pressure_reading > 900.0) && (hulp_double_pressure_reading < 1100.0))
               else
               {
                  main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading outside range");
                  sensor_data_record_WOW_pressure_MSL_inhg = "";
               }
            } // if ((main.sensor_data_record_obs_pressure.compareTo("") != 0) etc.
            else // so (still) no sensor data available
            {
               main.log_turbowin_system_message("[WOW] automatically retrieved barometer reading obsolete or error during retrieving data");
               sensor_data_record_WOW_pressure_MSL_inhg = "";
            }
         } // if (destination.equals("WOW"))
         else if (destination.equals("APR"))      // APR = Automated Pressure Reporting
         {
            String sensor_data_record_APR_pressure_MSL_hpa = "";
            String sensor_data_record_APR_pressure_hpa = "";
         
            try 
            {
               sensor_data_record_APR_pressure_hpa = get();    // get the return value of the doInBackground()
            } 
            catch (InterruptedException | ExecutionException ex) 
            {
               String message = "[APR] " + ex.toString();
               main.log_turbowin_system_message(message);
               sensor_data_record_APR_pressure_hpa = "";
            }
            
            if ((sensor_data_record_APR_pressure_hpa.compareTo("") != 0))
            {
               double hulp_double_APR_pressure_reading;
               try
               {
                  hulp_double_APR_pressure_reading = Double.parseDouble(sensor_data_record_APR_pressure_hpa.trim());
                  //hulp_double_pressure_reading = Math.round(hulp_double_pressure_reading * 10) / 10.0d;  // bv 998.19 -> 998.2
               }
               catch (NumberFormatException e)
               {
                  System.out.println("--- " + "Function RS232_MintakaDuo_Read_Sensor_Data_For_WOW_APR() " + e);
                  hulp_double_APR_pressure_reading = Double.MAX_VALUE;
               }   
             
               if ((hulp_double_APR_pressure_reading > 900.0) && (hulp_double_APR_pressure_reading < 1100.0))
               {
                  DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
               
                  // CONVERT TO MSL (+ apply barometer instrument correction)
                  double APR_height_correction_pressure = RS232_WOW_APR_compute_air_pressure_height_correction(hulp_double_APR_pressure_reading);
                  
                  String message_b = "[APR] air pressure at sensor height = " + sensor_data_record_APR_pressure_hpa + " hPa";
                  main.log_turbowin_system_message(message_b);
                  String message_hc = "[APR] air pressure height corection = " + Double.toString(APR_height_correction_pressure) + " hPa";
                  main.log_turbowin_system_message(message_hc);
                  String message_ic = "[APR] air pressure instrument corection = " + main.barometer_instrument_correction + " hPa";
                  main.log_turbowin_system_message(message_ic);
               
                  if (APR_height_correction_pressure > -50.0 && APR_height_correction_pressure < 50.0)
                  {   
////////////////////////
                    // ic correction (make it 0.0 if outside the range)
                     double double_barometer_instrument_correction = 0.0;
                     
                     if (main.barometer_instrument_correction.equals("") == false)
                     {
                        try
                        {
                           double_barometer_instrument_correction = Double.parseDouble(main.barometer_instrument_correction);
               
                           if (!(double_barometer_instrument_correction >= -4.0 && double_barometer_instrument_correction  <= 4.0))
                           {
                              double_barometer_instrument_correction = 0.0;
                           }
                        }
                        catch (NumberFormatException e) 
                        {
                           double_barometer_instrument_correction = 0.0;
                        }               
                     }
   
                     //DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
                     
                     ////////////////// barometer reading (pressure at sensor height + ic) ////////////////
                     //
                     String sensor_data_record_APR_pressure_reading_hpa_corrected = df.format(hulp_double_APR_pressure_reading + double_barometer_instrument_correction); 
                     mybarometer.pressure_reading_corrected = sensor_data_record_APR_pressure_reading_hpa_corrected;   // now pressure at sensor height corrected for ic
   
   
                     ///////////////// pressure at MSL (+ ic) ///////////
                     //
                     //sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)); 
                     sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + double_barometer_instrument_correction); 
                     mybarometer.pressure_msl_corrected = sensor_data_record_APR_pressure_MSL_hpa;   // sensor_data_record_APR_pressure_MSL_hpa the baromter instrument correction is included
              
                     String message_msl = "[APR] air pressure MSL = " + mybarometer.pressure_msl_corrected + " hPa";
                     main.log_turbowin_system_message(message_msl);
                     
////////////////////////////////////////////////////////                     
                     //sensor_data_record_APR_pressure_MSL_hpa = df.format(hulp_double_APR_pressure_reading + APR_height_correction_pressure + Double.parseDouble(main.barometer_instrument_correction)); 
                     RS232_Send_Sensor_Data_to_APR(/*sensor_data_record_APR_pressure_MSL_hpa, hulp_double_APR_pressure_reading,*/ retry);
                  }
                  else
                  {
                     String message_hce = "[APR] computed height correction pressure not ok (" + APR_height_correction_pressure + ")";
                     main.log_turbowin_system_message(message_hce);
                     sensor_data_record_APR_pressure_MSL_hpa = "";
                  }            
               } // if ((hulp_double_APR_pressure_reading > 900.0) && (hulp_double_APR_pressure_reading < 1100.0))
               else
               {
                  main.log_turbowin_system_message("[APR] automatically retrieved barometer reading outside range");
                  sensor_data_record_APR_pressure_MSL_hpa = "";
               }
            } //  if ((sensor_data_record_APR_pressure_hpa.compareTo("") != 0))
            else
            {
               main.log_turbowin_system_message("[APR] automatically retrieved barometer reading obsolete or error during retrieving data");
               sensor_data_record_APR_pressure_MSL_hpa = "";
            }
         } // else if (destination.equals("APR"))
      } // protected void done()

   }.execute(); // new SwingWorker <Void, Void>()
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void RS232_Send_Sensor_Data_to_APR_OLD(String sensor_data_record_APR_pressure_MSL_hpa, double hulp_double_APR_pressure_reading, final boolean retry)  
{
   // NB see also: Output_obs_to_server_FM13() [main.java]
   
   //final Integer INVALID_RESPONSE_FORMAT_101 = 710;                // see also: Function: http_respons_code_to_text() [main.java]
   //final Integer OK_RESPONSE_FORMAT_101      = 713;                // see also: Function: http_respons_code_to_text() [main.java]
   
   double double_barometer_instrument_correction = 0.0;
   //String server_format_101_line = "";
   
   
   // NB in function main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing(); the obs vars like myposition.longitude_degrees etc were already set
   //
   //
   
   
   ////////////////// barometer reading (pressure at sensor height, no ic included) ////////////////
   // (not a nice solution but because the ic was not applied/available the reading corrected for instrument error must be done again)
   //
   
   // ic correction
   if (main.barometer_instrument_correction.equals("") == false)
   {
      try
      {
         double_barometer_instrument_correction = Double.parseDouble(main.barometer_instrument_correction);
               
         if (!(double_barometer_instrument_correction >= -4.0 && double_barometer_instrument_correction  <= 4.0))
         {
            double_barometer_instrument_correction = 0.0;
         }
      }
      catch (NumberFormatException e) 
      {
         double_barometer_instrument_correction = 0.0;
      }               
   }
   
   DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
   String sensor_data_record_APR_pressure_reading_hpa_corrected = df.format(hulp_double_APR_pressure_reading + double_barometer_instrument_correction); 
   mybarometer.pressure_reading_corrected = sensor_data_record_APR_pressure_reading_hpa_corrected;   // now pressure at sensor height corrected for ic
   
   
   ///////////////// pressure at MSL (ic is ready included in sensor_data_record_APR_pressure_MSL_hpa) ///////////
   //
   mybarometer.pressure_msl_corrected = sensor_data_record_APR_pressure_MSL_hpa;   // sensor_data_record_APR_pressure_MSL_hpa the baromter instrument correction is included

   
   ///////////////// compress the data end send it //////////////////////
   //
   new SwingWorker<Integer, Void>()
   {
      @Override
      protected Integer doInBackground() throws Exception
      {
         Integer responseCode          = main.OK_RESPONSE_FORMAT_101;            // OK 
         
         // NB in this phase you kow: - date-time ok
         //                          - position ok
         //                          - call sign ok (RS232_check_APR_settings())
         //                          = logs ok (RS232_check_APR_settings())
         //
             
         // compress the data
         //
         main.format_101_class = new FORMAT_101();
         main.format_101_class.compress_and_decompress_101_control_center();
            
            
         // read the compressed obs (format 101) which is the only line in file HPK_format_101.txt
         //
         String format_101_obs = "";
         
         final String volledig_path_format_101_compressed_file = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + main.FORMAT_101_TEMP_DIR + java.io.File.separator + "HPK_" + main.FORMAT_101_INPUT_FILE;// NB adding "HPK_" to the input file name is automatically done by the C-code compression functions

         try
         {
            BufferedReader in = new BufferedReader(new FileReader(volledig_path_format_101_compressed_file));
                  
            if ((format_101_obs = in.readLine()) == null)
            {
               String message = "[APR] when retrieveing format 101 data empty file: " + volledig_path_format_101_compressed_file;
               main.log_turbowin_system_message(message);
                  
               format_101_obs = "";                // the function which invoke get_format_101_obs_from_file() will check this value
               responseCode = main.INVALID_RESPONSE_FORMAT_101;
            } // else
                     
            in.close();
         } // try
         catch (IOException | HeadlessException e)
         {
            String message = "[APR] when retrieving format 101 data error opening file: " + volledig_path_format_101_compressed_file;
            main.log_turbowin_system_message(message);
               
            format_101_obs = "";                   // the function which invoke get_format_101_obs_from_file() will check this value
            responseCode = main.INVALID_RESPONSE_FORMAT_101;
         } // catch         
      
            
         // send the data
         //
         if (Objects.equals(responseCode, main.OK_RESPONSE_FORMAT_101))       // null save comparison
         {   
            // NB encoding:
            // Translates a string into application/x-www-form-urlencoded format using a specific encoding scheme. This method uses the supplied encoding scheme to obtain 
            // the bytes for unsafe characters.
            // Note: The World Wide Web Consortium Recommendation states that UTF-8 should be used. Not doing so may introduce incompatibilites.
            // 
            // http://stackoverflow.com/questions/10786042/java-url-encoding-of-query-string-parameters:
            // You only need to keep in mind to encode only the individual query string parameter name and/or value, not the entire URL, 
            // for sure not the query string parameter separator character & nor the parameter name-value separator character =.
            // String q = "random word £500 bank $";
            // String url = "http://example.com/query?q=" + URLEncoder.encode(q, "UTF-8");
            //
            // Encode all 'not alloud' ASCII chars if not java.net.URISyntaxException (with index number in the URL string)
            String encoded_format_101_obs = URLEncoder.encode(format_101_obs, "UTF-8");               
               
            //String url = "http://www.knmi.nl/samenw/turbowin/webstart101/index_webstart_101.php?obs=" + encoded_format_101_obs;  
            String url = main.upload_URL + "obs=" + encoded_format_101_obs; 
               
            URL obj = null;
            try 
            {
               obj = new URL(url);
               HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            
               // optional (default is GET)
               con.setRequestMethod("GET");  
                  
               String message = "[APR] sending 'GET' request to URL: " + url;
               main.log_turbowin_system_message(message);
     
               responseCode = con.getResponseCode();                     // if internet available the default OK_RESPONSE_FORMAT_101 will be overwritten here
                  
            } // try
            catch (MalformedURLException ex)
            {
               //String message = "[APR] send obs failed; MalformedURLException (function: RS232_Send_Sensor_Data_to_APR)"; 
               //main.log_turbowin_system_message(message);
               //main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message);
               
               responseCode = main.RESPONSE_MALFORMED_URL;
            }
            catch (IOException ex) 
            {
               //String message = "[APR] send obs failed; IOException; most probably no internet connection available; (function: RS232_Send_Sensor_Data_to_APR)"; 
               //main.log_turbowin_system_message(message);
               //main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message);
               
               responseCode = main.RESPONSE_NO_INTERNET;
            } // catch            
            
         } // if (Objects.equals(responseCode, OK_RESPONSE_FORMAT_101)) 


         return responseCode;
               
      } // protected Integer doInBackground() throws Exception

      @Override
      protected void done()
      {
         try
         {
            Integer response_code = get();

            if (response_code == 200)          // OK
            {
               String message_a = "[APR] send obs success"; 
               main.log_turbowin_system_message(message_a);
               main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message_a); // update status field (bottom line main -progress- window) 
               
               // save data to immt log and clear main screen
               //
               // NB in some rare occasions it is possible that within 1 second the obs will be send twice
               //    (somethimes if the wifi/ethernet connection is bridged) and in that case a misformed record will be writkten to the IMMT log
               //    to prevent this check the date/time and position elements (because after the first upload they were reset)
               //
               if ( (!mydatetime.year.equals("")) && (!mydatetime.MM_code.equals("")) && (!mydatetime.YY_code.equals("")) && (!mydatetime.GG_code.equals("")) &&                     
	                 (!myposition.Qc_code.equals("")) && (!myposition.lalala_code.equals("")) && (!myposition.lolololo_code.equals("")) )
               {          
                  RS232_make_APR_pressure_IMMT_ready();                        // pressure in IMMT will only be recorded as "mybarometer.PPPP_code" (not mybarometer.pressure_msl_corrected)
                  main.IMMT_log();
               }
               main.Reset_all_meteo_parameters();
            }  
            else // send obs NOT ok
            {
               // NB besides the response code there is also a corresponding response text, but unfortunately with html tags, 
               //    and only with the standard response codes, self defined response codes are not returned?. Not suitable for direct using it into a popup message box
               //    so only using the reponse code and locally (in this program) determined the corresponding return http message text
               
               // NB If no internet connection available the responseCode will be RESPONSE_NO_INTERNET
               //    (if internet avaialble the response code will be overwritten with eg 200)
                     
		         String message_b = "[APR] send obs failed; " + main.http_respons_code_to_text(response_code).replace("<br>", " ");
                  
               // file logging
               main.log_turbowin_system_message(message_b);
                  
               // bottom main screen
               main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message_b); 
               
               // so even no success after a retry, but now save the data to IMMT because no further attempts will follow (and the data was not saved before)
               if (retry)
               {
                  // save data to immt log and clear main screen
                  //
                  // NB in some rare occasions it is possible that within 1 second the obs will be send twice
                  //    (somethimes if the wifi/ethernet connection is bridged) and in that case a misformed record will be writkten to the IMMT log
                  //    to prevent this check the date/time and position elements (because after the first upload they were reset)
                  //
                  if ( (!mydatetime.year.equals("")) && (!mydatetime.MM_code.equals("")) && (!mydatetime.YY_code.equals("")) && (!mydatetime.GG_code.equals("")) &&                     
	                    (!myposition.Qc_code.equals("")) && (!myposition.lalala_code.equals("")) && (!myposition.lolololo_code.equals("")) )
                  {
                     RS232_make_APR_pressure_IMMT_ready();                     // pressure in IMMT will only be recorded as "mybarometer.PPPP_code" (not mybarometer.pressure_msl_corrected)
                     main.IMMT_log();
                  }   
                  main.Reset_all_meteo_parameters();
               } // if retry
            } // else (send obs NOT ok
            
            // for checking if a upload retry will be necessary [ see unction ...
            APR_server_response_code = response_code;
               
         } // try
         catch (InterruptedException | ExecutionException ex) 
         {   
            String message = "[APR] error in Function: RS232_Send_Sensor_Data_to_APR(); no internet connection available?; " + ex.toString(); 
            main.log_turbowin_system_message(message);
            main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message);
         }
      } // protected void done()
   }.execute(); // new SwingWorker<Void, Void>()
}
*/   

/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS232_Send_Sensor_Data_to_APR(/*String sensor_data_record_APR_pressure_MSL_hpa, double hulp_double_APR_pressure_reading,*/ final boolean retry)  
{
   // NB see also: Output_obs_to_server_FM13() [main.java]
   
   // called from: - RS232_Mintaka_Star_Read_And_Send_Sensor_Data_For_WOW_APR()
   //              - RS232_Mintaka_Duo_Read_And_Send_Sensor_Data_For_WOW_APR()
   //              - RS232_Vaisala_Read_And_Send_Sensor_Data_For_WOW_APR()
   //
   
/*   
   double double_barometer_instrument_correction = 0.0;
   
   
   // NB in function main_RS232_RS422.RS232_GPS_NMEA_0183_Date_Position_Parsing(); the obs vars like myposition.longitude_degrees etc were already set
   //
   //
   
   
   ////////////////// barometer reading (pressure at sensor height, no ic included) ////////////////
   // (not a nice solution but because the ic was not applied/available the reading corrected for instrument error must be done again)
   //
   
   // ic correction
   if (main.barometer_instrument_correction.equals("") == false)
   {
      try
      {
         double_barometer_instrument_correction = Double.parseDouble(main.barometer_instrument_correction);
               
         if (!(double_barometer_instrument_correction >= -4.0 && double_barometer_instrument_correction  <= 4.0))
         {
            double_barometer_instrument_correction = 0.0;
         }
      }
      catch (NumberFormatException e) 
      {
         double_barometer_instrument_correction = 0.0;
      }               
   }
   
   DecimalFormat df = new DecimalFormat("0.0");           // rounding only 1 decimal
   String sensor_data_record_APR_pressure_reading_hpa_corrected = df.format(hulp_double_APR_pressure_reading + double_barometer_instrument_correction); 
   mybarometer.pressure_reading_corrected = sensor_data_record_APR_pressure_reading_hpa_corrected;   // now pressure at sensor height corrected for ic
   
   
   ///////////////// pressure at MSL (ic is ready included in sensor_data_record_APR_pressure_MSL_hpa) ///////////
   //
   mybarometer.pressure_msl_corrected = sensor_data_record_APR_pressure_MSL_hpa;   // sensor_data_record_APR_pressure_MSL_hpa the baromter instrument correction is included
*/
   
   ///////////////// compress the data end send it //////////////////////
   //
   new SwingWorker<Integer, Void>()
   {
      @Override
      protected Integer doInBackground() throws Exception
      {
         Integer responseCode          = main.OK_RESPONSE_FORMAT_101;            // OK 
         
         // NB in this phase you kow: - date-time ok
         //                           - position ok
         //                           - call sign ok (RS232_check_APR_settings())
         //                           - logs ok (RS232_check_APR_settings())
         //
             
         // compress the data
         //
         main.format_101_class = new FORMAT_101();
         main.format_101_class.compress_and_decompress_101_control_center();
            
            
         // read the compressed obs (format 101) which is the only line in file HPK_format_101.txt
         //
         format_101_obs = "";
         
         final String volledig_path_format_101_compressed_file = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + main.FORMAT_101_TEMP_DIR + java.io.File.separator + "HPK_" + main.FORMAT_101_INPUT_FILE;// NB adding "HPK_" to the input file name is automatically done by the C-code compression functions

         try
         {
            BufferedReader in = new BufferedReader(new FileReader(volledig_path_format_101_compressed_file));
                  
            if ((format_101_obs = in.readLine()) == null)
            {
               String message = "[APR] when retrieveing format 101 data empty file: " + volledig_path_format_101_compressed_file;
               main.log_turbowin_system_message(message);
                  
               format_101_obs = "";                // the function which invoke get_format_101_obs_from_file() will check this value
               responseCode = main.INVALID_RESPONSE_FORMAT_101;
            } // else
                     
            in.close();
         } // try
         catch (IOException | HeadlessException e)
         {
            String message = "[APR] when retrieving format 101 data error opening file: " + volledig_path_format_101_compressed_file;
            main.log_turbowin_system_message(message);
               
            format_101_obs = "";                   // the function which invoke get_format_101_obs_from_file() will check this value
            responseCode = main.INVALID_RESPONSE_FORMAT_101;
         } // catch         
      

         return responseCode;
               
      } // protected Integer doInBackground() throws Exception

      @Override
      protected void done()
      {
         try
         {
            Integer response_code = get();
            
            // continue if compressing went ok
            //
            if (Objects.equals(response_code, main.OK_RESPONSE_FORMAT_101))       // null save comparising
            {   
               // NB encoding:
               // Translates a string into application/x-www-form-urlencoded format using a specific encoding scheme. This method uses the supplied encoding scheme to obtain 
               // the bytes for unsafe characters.
               // Note: The World Wide Web Consortium Recommendation states that UTF-8 should be used. Not doing so may introduce incompatibilites.
               // 
               // http://stackoverflow.com/questions/10786042/java-url-encoding-of-query-string-parameters:
               // You only need to keep in mind to encode only the individual query string parameter name and/or value, not the entire URL, 
               // for sure not the query string parameter separator character & nor the parameter name-value separator character =.
               // String q = "random word £500 bank $";
               // String url = "http://example.com/query?q=" + URLEncoder.encode(q, "UTF-8");
               //
               // Encode all 'not alloud' ASCII chars if not java.net.URISyntaxException (with index number in the URL string)
               String encoded_format_101_obs = null;               
               try 
               {
                  encoded_format_101_obs = URLEncoder.encode(format_101_obs, "UTF-8");
               } 
               catch (UnsupportedEncodingException ex) 
               {
                  response_code = main.RESPONSE_UNSUPPORTED_ENCODING;
               }
               
               // continue if the encoding was ok
               if (!Objects.equals(response_code, main.RESPONSE_UNSUPPORTED_ENCODING))       // null save comparising
               {   
                  //String url = "http://www.knmi.nl/samenw/turbowin/webstart101/index_webstart_101.php?obs=" + encoded_format_101_obs;  
                  String url = main.upload_URL + "obs=" + encoded_format_101_obs; 
               
                  URL obj = null;
                  try 
                  {
                     obj = new URL(url);
                     HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            
                     // optional (default is GET)
                     con.setRequestMethod("GET");  
                  
                     String message = "[APR] sending 'GET' request to URL: " + url;
                     main.log_turbowin_system_message(message);
     
                     response_code = con.getResponseCode();                     // if internet available the default OK_RESPONSE_FORMAT_101 will be overwritten here
                  
                  } // try
                  catch (MalformedURLException ex)
                  {
                     response_code = main.RESPONSE_MALFORMED_URL;
                  }
                  catch (IOException ex) 
                  {
                     response_code = main.RESPONSE_NO_INTERNET;
                  } // catch            
               } // if (!Objects.equals(response_code, main.RESPONSE_UNSUPPORTED_ENCODING))
            } // if (Objects.equals(responseCode, OK_RESPONSE_FORMAT_101)) 
               
               
            // how to continue depends on response_code            
            //
            if (response_code == 200)          // OK
            {
               String message_a = "[APR] send obs success"; 
               main.log_turbowin_system_message(message_a);
               main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message_a); // update status field (bottom line main -progress- window) 
               
               // save data to immt log and clear main screen
               //
               // NB in some rare occasions it is possible that within 1 second the obs will be send twice
               //    (somethimes if the wifi/ethernet connection is bridged) and in that case a misformed record will be writkten to the IMMT log
               //    to prevent this check the date/time and position elements (because after the first upload they were reset)
               //
               if ( (!mydatetime.year.equals("")) && (!mydatetime.MM_code.equals("")) && (!mydatetime.YY_code.equals("")) && (!mydatetime.GG_code.equals("")) &&                     
	                 (!myposition.Qc_code.equals("")) && (!myposition.lalala_code.equals("")) && (!myposition.lolololo_code.equals("")) )
               {          
                  RS232_make_APR_pressure_IMMT_ready();                        // pressure in IMMT will only be recorded as "mybarometer.PPPP_code" (not mybarometer.pressure_msl_corrected)
                  main.IMMT_log();
               }
               main.Reset_all_meteo_parameters();
            }  
            else // send obs NOT ok or compresing NOT ok or encoding NOT ok
            {
               // NB besides the response code there is also a corresponding response text, but unfortunately with html tags, 
               //    and only with the standard response codes, self defined response codes are not returned?. Not suitable for direct using it into a popup message box
               //    so only using the reponse code and locally (in this program) determined the corresponding return http message text
               
               // NB If no internet connection available the responseCode will be RESPONSE_NO_INTERNET
               //    (if internet avaialble the response code will be overwritten with eg 200)
                     
		         String message_b = "[APR] send obs failed; " + main.http_respons_code_to_text(response_code).replace("<br>", " ");
                  
               // file logging
               main.log_turbowin_system_message(message_b);
                  
               // bottom main screen
               main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message_b); 
               
               // so even no success after a retry, but now save the data to IMMT because no further attempts will follow (and the data was not saved before)
               if (retry)
               {
                  // save data to immt log and clear main screen
                  //
                  // NB in some rare occasions it is possible that within 1 second the obs will be send twice
                  //    (somethimes if the wifi/ethernet connection is bridged) and in that case a misformed record will be writkten to the IMMT log
                  //    to prevent this check the date/time and position elements (because after the first upload they were reset)
                  //
                  if ( (!mydatetime.year.equals("")) && (!mydatetime.MM_code.equals("")) && (!mydatetime.YY_code.equals("")) && (!mydatetime.GG_code.equals("")) &&                     
	                    (!myposition.Qc_code.equals("")) && (!myposition.lalala_code.equals("")) && (!myposition.lolololo_code.equals("")) )
                  {
                     RS232_make_APR_pressure_IMMT_ready();                     // pressure in IMMT will only be recorded as "mybarometer.PPPP_code" (not mybarometer.pressure_msl_corrected)
                     main.IMMT_log();
                  }   
                  main.Reset_all_meteo_parameters();
               } // if retry
            } // else (send obs NOT ok
            
            // for checking if a upload retry will be necessary [ see unction ...
            APR_server_response_code = response_code;
               
         } // try
         catch (InterruptedException | ExecutionException ex) 
         {   
            String message = "[APR] error in Function: RS232_Send_Sensor_Data_to_APR(); no internet connection available?; " + ex.toString(); 
            main.log_turbowin_system_message(message);
            main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message);
         }
      } // protected void done()
   }.execute(); // new SwingWorker<Void, Void>()
}
   





/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS232_make_APR_pressure_IMMT_ready()
{
   // NB pressure in IMMT will only be recorded as "mybarometer.PPPP_code" (not mybarometer.pressure_msl_corrected)
   // NB see also OK_button_actionPerformed [mybarometer.java]
   
   if (mybarometer.pressure_msl_corrected.compareTo("") != 0)
   {
      try
      {
         // string to double
         double hulp_double_pressure_msl_corrected = Double.parseDouble(mybarometer.pressure_msl_corrected);
      
         int num_PPPP_code = (int)Math.floor(hulp_double_pressure_msl_corrected * 10 + 0.5);

         // remove the "thousands"
         if (num_PPPP_code >= 10000)
         {
            num_PPPP_code -= 10000;
         }
             
         mybarometer.PPPP_code = Integer.toString(num_PPPP_code);     // convert to string

         // NB PPPP_code always 4 characters width e.g. 0007 (accomplish via construction below)
         int len = 4;
         if (mybarometer.PPPP_code.length() < len)                                            // pad on left with zeros
         {
            mybarometer.PPPP_code = "0000000000".substring(0, len - mybarometer.PPPP_code.length()) + mybarometer.PPPP_code;
         }
      }
      catch (NumberFormatException ex)
      {
         mybarometer.PPPP_code = "";
      }
   } // if (mybarometer.pressure_msl_corrected.compareTo("") != 0)  
   
   
   // NB in case of Mintaka Star the position data was not yet made IMMT ready
   // NB for the NMEA connected GPS's this was already done, see: RS232_GPS_NMEA_0183_Date_Position_Parsing() [main_RS232_RS422.java]
   //
   if (main.RS232_GPS_connection_mode == 3)                                          // Mintaka Star GPS (USB or station mode or acess point)          
   {   
      // determine (IMMT)code figures (latitude)
      //
      int int_latitude_minutes_6 = myposition.int_latitude_minutes / 6;               // devide the minutes by six and disregard the remainder
      String latitude_minutes_6 = Integer.toString(int_latitude_minutes_6);           // convert to String 
         
      if (latitude_minutes_6.length() != 1)                                           // debug check
      {
         myposition.lalala_code = "   ";                                              // 3x space
      }
      else
      {
         myposition.lalala_code = myposition.latitude_degrees.trim().replaceFirst("^0+(?!$)", "") + latitude_minutes_6;

         int len = 3;                                                                  // always 3 chars
         if (myposition.lalala_code.length() < len) 
         {
            // pad on left with zeros
            myposition.lalala_code = "0000000000".substring(0, len - myposition.lalala_code.length()) + myposition.lalala_code;
         }
      } // else
      
      // determine (IMMT)code figures (longitude)
      //
      int int_longitude_minutes_6 = myposition.int_longitude_minutes / 6;               // devide the minutes by six and disregard the remainder
      String longitude_minutes_6 = Integer.toString(int_longitude_minutes_6);           // convert to String 
         
      if (longitude_minutes_6.length() != 1)                                            // debug check
      {
         myposition.lolololo_code = "    ";                                             // 4 x space
      }
      else
      {
         myposition.lolololo_code = myposition.longitude_degrees.trim().replaceFirst("^0+(?!$)", "") + longitude_minutes_6;
        
         int len = 4;                                                                    // always 4 chars
         if (myposition.lolololo_code.length() < len)                                    // pad on left with zeros
         {
            myposition.lolololo_code = "0000000000".substring(0, len - myposition.lolololo_code.length()) + myposition.lolololo_code;
         }
      } // else
      
      
      // quadrant of the globe for IMMT
      //
      if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
      {
          myposition.Qc_code = "1";
      }
      else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
      {
         myposition.Qc_code = "3";
      }
      else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
      {
         myposition.Qc_code = "5";
      }
      else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
      {
         myposition.Qc_code = "7";
      }
      else
      {
         myposition.Qc_code = " ";
         //System.out.println("+++++++++++ Qc_code error");
      }
     
   } // if (maiin.RS232_GPS_connection_mode == 3) 
   
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private double RS232_WOW_APR_compute_air_pressure_height_correction(double double_pressure_reading)
{
   // NB this function is used for height air pressure conversion from station level to mean sea level for the WOW site and/or APR
   // NB before this function is called the WOW/APR settings were checked 
   
   double height_correction_pressure = Double.MAX_VALUE;
   double temp;
   double term_1;
   double term_2;
   double double_barometer_instrument_correction = 0;
   double double_barometer_above_sll = 0;
   double double_keel_sll = 0;
   double double_draught = 0;
   double double_hoogte_barometer;
   double double_pressure_reading_corrected;
   boolean checks_ok = true;
   
  
      
   /*
   /////////// checks 
   */
   if ( (main.barometer_above_sll.equals("")) || (main.barometer_above_sll.trim().length() == 0) )
   {
      //JOptionPane.showMessageDialog(null, "barometer height above MSL not available (Maintenance -> Station data)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
      System.out.println("+++ [WOW/APR] barometer height above MSL not available (Maintenance -> Station data)");
      checks_ok = false;
   }
   if ( (main.keel_sll.equals("")) || (main.keel_sll.trim().length() == 0) )
   {
      //JOptionPane.showMessageDialog(null, "distance keel - MSL not available (Maintenance -> Station data)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
      System.out.println("+++ [WOW/APR] distance keel - MSL not available (Maintenance -> Station data)");
      checks_ok = false;
   }
   if ( (main.barometer_instrument_correction.equals("")) || (main.barometer_instrument_correction.trim().length() == 0) )   
   {
      System.out.println("+++ [WOW/APR] barometer instrument correction not available (Maintenance -> WOW/APR settings)");
      checks_ok = false;
   }
   if ( (main.WOW_APR_average_draught.equals("")) || (main.WOW_APR_average_draught.trim().length() == 0) )   
   {
      System.out.println("+++ [WOW/APR] average draught not available (Maintenance -> WOW/APR settings)");
      checks_ok = false;
   }
   
   
   // compute the doubles
   //
   if (checks_ok)
   {     
      // convert to double: distance barometer - keel
      try 
      {
         double_barometer_above_sll = Double.parseDouble(main.barometer_above_sll.trim());
      } 
      catch (NumberFormatException e) 
      {  
         //JOptionPane.showMessageDialog(null, "Internal error (converting distance barometer - SLL to double)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         System.out.println("+++ [WOW/APR] Internal error (converting distance barometer - SLL to double)");
         checks_ok = false;
      }

      // convert to double: distance keel - sll
      try 
      {
         double_keel_sll = Double.parseDouble(main.keel_sll.trim());
      } 
      catch (NumberFormatException e) 
      {
         //JOptionPane.showMessageDialog(null, "Internal error (converting distance keel - SLL to double)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         System.out.println("+++ [WOW/APR] Internal error (converting distance keel - SLL to double)");
         checks_ok = false;
      }
      
      try
      {
         double_barometer_instrument_correction = Double.parseDouble(main.barometer_instrument_correction);
      }
      catch (NumberFormatException e) 
      {
         System.out.println("+++ [WOW/APR] Internal error (converting distance barometer ic to double)");
         checks_ok = false;
      }               
      
      try
      {
         double_draught = Double.parseDouble(main.WOW_APR_average_draught);
      }
      catch (NumberFormatException e) 
      {
         System.out.println("+++ [WOW/APR] Internal error (converting average draught to double)");
         checks_ok = false;
      }               
      
   } // if checks_ok
   
   
   // the 'real' computation
   //
   if (checks_ok)
   {
      double_pressure_reading_corrected = double_pressure_reading + double_barometer_instrument_correction;
      
      double_hoogte_barometer = double_keel_sll + double_barometer_above_sll - double_draught;
   
      temp = 15.0 + CELCIUS_TO_KELVIN_FACTOR;                  // so calculated with a default air temperature (15.0 C) !!!!
      term_1 = double_pressure_reading_corrected * double_hoogte_barometer;
      term_2 = 29.27 * temp;

      if (term_2 != 0) // prevent exception (in principle will never be 0))
      {
         height_correction_pressure = term_1 / term_2;
      } 
      else 
      {
         height_correction_pressure = 0.0;
      }      
      
      // round to two digits behind the "."
      height_correction_pressure = Math.round(height_correction_pressure * 100.0 ) / 100.0;
      
      // at Great lakes? (for extra height correction value)
      double great_lakes_corr = check_at_Great_Lakes();
      if (great_lakes_corr < 50.0)                                        // so at Great Lakes
      {
         height_correction_pressure += great_lakes_corr;
      }
      else if ((great_lakes_corr > AT_GREAT_LAKES_OUTSIDE_MAIN_AREAS - 1) && (great_lakes_corr < AT_GREAT_LAKES_OUTSIDE_MAIN_AREAS + 1))     // so at Great Lakes but in transition between two lakes (locks)
      {
         height_correction_pressure = AT_GREAT_LAKES_OUTSIDE_MAIN_AREAS;
         String message = "[APR/WOW] air pressure will not be reported because outside Great Lakes main areas"; 
         main.log_turbowin_system_message(message);
      }
   
   } // if (checks_ok)
   
   //System.out.println("--- WOW/APR height_correction_pressure : " + height_correction_pressure);  
   
   
   return height_correction_pressure;
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static double check_at_Great_Lakes()
{
   double north_limit_great_lakes     = 49;
   double south_limit_great_lakes     = 41;
   double west_limit_great_lakes      = -93;
   double east_limit_great_lakes      = -73;
   
   double north_limit_lake_ontario    = 44;
   double south_limit_lake_ontario    = 43.3;
   double west_limit_lake_ontario     = -80;
   double east_limit_lake_ontario     = -76.5;
           
   double north_limit_lake_erie_upper = 42.8;
   double south_limit_lake_erie_upper = 41.5;
   double west_limit_lake_erie_upper  = -82;
   double east_limit_lake_erie_upper  = -79;
   
   double north_limit_lake_erie_lower = 42; 
   double south_limit_lake_erie_lower = 41.5;
   double west_limit_lake_erie_lower  = -83.4;
   double east_limit_lake_erie_lower  = -82;
           
   double north_limit_lake_clair      = 42.5;
   double south_limit_lake_clair      = 42.35;
   double west_limit_lake_clair       = -82.9;
   double east_limit_lake_clair       = -82.7;
           
   double north_limit_lake_huron      = 46;
   double south_limit_lake_huron      = 43;
   double west_limit_lake_huron       = -84.5;
   double east_limit_lake_huron       = -80;
           
   double north_limit_lake_michagan   = 45.5;
   double south_limit_lake_michagan   = 42;
   double west_limit_lake_michagan    = -87.5;
   double east_limit_lake_michagan    = -86.0;
           
   double north_limit_lake_superior   = 48; 
   double south_limit_lake_superior   = 46.5;
   double west_limit_lake_superior    = -92;
   double east_limit_lake_superior    = -85;
           
   double extra_great_lakes_pressure_height_correction = Double.MAX_VALUE;        
   double num_huidige_obs_breedte = Double.MAX_VALUE;
   double num_huidige_obs_lengte  = Double.MAX_VALUE;
   boolean doorgaan = true;
   
   
   try
   {
      num_huidige_obs_breedte = (double)myposition.int_latitude_degrees +  ((double)myposition.int_latitude_minutes / 60);
      num_huidige_obs_lengte =  (double)myposition.int_longitude_degrees + ((double)myposition.int_longitude_minutes / 60);
   
      if (myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true)
      {
         num_huidige_obs_breedte *= -1;
      }

      if (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true)
      {
         num_huidige_obs_lengte *= -1;
      }   
   } // try
   catch(NumberFormatException ex)
   {
      String message = "[APR/WOW] Great Lakes position check failed; NumberFormatException (function: check_at_Great_Lakes)"; 
      main.log_turbowin_system_message(message);
      doorgaan = false;
   }
   
   
   if (doorgaan == true)
   {   
      // at the Great Lakes ?
      if (num_huidige_obs_breedte > south_limit_great_lakes && num_huidige_obs_breedte < north_limit_great_lakes && num_huidige_obs_lengte > west_limit_great_lakes && num_huidige_obs_lengte < east_limit_great_lakes)
      {
         // Lake Ontario ?
         if (num_huidige_obs_breedte >= south_limit_lake_ontario && num_huidige_obs_breedte <= north_limit_lake_ontario && num_huidige_obs_lengte >= west_limit_lake_ontario && num_huidige_obs_lengte <= east_limit_lake_ontario)
         {
            extra_great_lakes_pressure_height_correction = 8.99;
         }
         
         // Lake Erie upper part ?
         else if (num_huidige_obs_breedte >= south_limit_lake_erie_upper && num_huidige_obs_breedte <= north_limit_lake_erie_upper && num_huidige_obs_lengte >= west_limit_lake_erie_upper && num_huidige_obs_lengte <= east_limit_lake_erie_upper)
         {
            extra_great_lakes_pressure_height_correction = 21.05;
         }
        
         // Lake Erie lower part ?
         else if (num_huidige_obs_breedte >= south_limit_lake_erie_lower && num_huidige_obs_breedte <= north_limit_lake_erie_lower && num_huidige_obs_lengte >= west_limit_lake_erie_lower && num_huidige_obs_lengte <= east_limit_lake_erie_lower)
         {
            extra_great_lakes_pressure_height_correction = 21.05;
         }
         
         // Lake St Clair ?
         else if (num_huidige_obs_breedte >= south_limit_lake_clair && num_huidige_obs_breedte <= north_limit_lake_clair && num_huidige_obs_lengte >= west_limit_lake_clair && num_huidige_obs_lengte <= east_limit_lake_clair)
         {
            extra_great_lakes_pressure_height_correction = 21.16;
         }
         
         // Lake Huron ?
         else if (num_huidige_obs_breedte >= south_limit_lake_huron && num_huidige_obs_breedte <= north_limit_lake_huron && num_huidige_obs_lengte >= west_limit_lake_huron && num_huidige_obs_lengte <= east_limit_lake_huron)
         {
            extra_great_lakes_pressure_height_correction = 21.35;
         }
         
         // Lake Michagan ?
         else if (num_huidige_obs_breedte >= south_limit_lake_michagan && num_huidige_obs_breedte <= north_limit_lake_michagan && num_huidige_obs_lengte >= west_limit_lake_michagan && num_huidige_obs_lengte <= east_limit_lake_michagan)
         {
            extra_great_lakes_pressure_height_correction = 21.35;
         }
         
         // Lake Superior ?
         else if (num_huidige_obs_breedte >= south_limit_lake_superior && num_huidige_obs_breedte <= north_limit_lake_superior && num_huidige_obs_lengte >= west_limit_lake_superior && num_huidige_obs_lengte <= east_limit_lake_superior)
         {
            extra_great_lakes_pressure_height_correction = 22.24;
         }
         
         else
         {
            // so at the Great lakes but between two Lakes (e.g. in the locks!) or outside a 'main' area
            extra_great_lakes_pressure_height_correction = AT_GREAT_LAKES_OUTSIDE_MAIN_AREAS;
         }
         
      } // if (num_huidige_obs_breedte > south_limit_great_lakes etc.
      else
      {
         // not at the Great Lakes
         extra_great_lakes_pressure_height_correction = Double.MAX_VALUE;
      }
   } // if (doorgaan == true)    
   
   
   return extra_great_lakes_pressure_height_correction;
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS232_Send_Sensor_Data_to_WOW_Azure(String sensor_data_record_WOW_pressure_MSL_inhg)  
{
   
/*   
 using (var httpClient = new HttpClient())
    {
        httpClient.BaseAddress = new Uri("https://apimgmt.www.wow.metoffice.gov.uk");
        httpClient.DefaultRequestHeaders.Add("Ocp-Apim-Subscription-Key", subscriptionKey);

        var observation = new
        {
            SiteId = "a27e78e7-2b39-e611-9cd2-28d244e1ce6c", // Site Id
            SiteAuthenticationKey = "123123", // When Site Id is present, required.
            Latitude = 52.702, // Required. Latitude of the observation
            Longitude = 5.887, // Required. Longitude of the observation
            ReportStartDateTime = new DateTime(2016, 12, 19, 13, 25, 00), // Required. Start date of this observation.
            ReportEndDateTime = new DateTime(2016, 12, 19, 13, 25, 00), // Required. End date of this observation.
            DryBulbTemperature_Celsius = 12 // The dry bulb temperature value
        };

        using (var response = await httpClient.PostAsync("/api/observations", new StringContent(JsonConvert.SerializeObject(observation), Encoding.UTF8, "application/json")))
        {
            response.EnsureSuccessStatusCode();
            return JObject.Parse(await response.Content.ReadAsStringAsync());
        }
    }   
*/   
   
/*   
   // https://mowowprod.portal.azure-api.net/docs/services/57a0bf29e4a58413dcc1131f/operations/57a0bf2ce4a58413f4360cea
   // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://apimgmt.www.wow.metoffice.gov.uk/api/Observations");


            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", "{subscription key}");
            request.setHeader("Authorization", "Bearer access_token"); // For operations that require authentication
            
            // Request body
            StringEntity reqEntity = new StringEntity("{body}");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) 
            {
                System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

*/   
   
   
   
   
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS232_Send_Sensor_Data_to_WOW(String sensor_data_record_WOW_pressure_MSL_inhg)  
{
   // format YYYY-mm-DD HH:mm:ss (where : = coded as %3A and space as %20)
   
   cal_WOW_systeem_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // system date time in UTC of this moment
   cal_WOW_systeem_datum_tijd.getTime();                                             // now effective
   
   int int_WOW_system_year   = cal_WOW_systeem_datum_tijd.get(Calendar.YEAR); 
   int int_WOW_system_month  = cal_WOW_systeem_datum_tijd.get(Calendar.MONTH);          // Jan = 0, not 1
   int int_WOW_system_day    = cal_WOW_systeem_datum_tijd.get(Calendar.DAY_OF_MONTH);
   int int_WOW_system_hour   = cal_WOW_systeem_datum_tijd.get(Calendar.HOUR_OF_DAY);    // hOUR_OF_DAY for 24 hour format (HOUR is in 12 hour format [am/pm])
   int int_WOW_system_minute = cal_WOW_systeem_datum_tijd.get(Calendar.MINUTE);   
   
   String string_WOW_system_year = Integer.toString(int_WOW_system_year);
   String string_WOW_system_month  = String.format("%02d", int_WOW_system_month + 1);
   String string_WOW_system_day    = String.format("%02d", int_WOW_system_day);
   String string_WOW_system_hour   = String.format("%02d", int_WOW_system_hour);
   String string_WOW_system_minute = String.format("%02d", int_WOW_system_minute);
   
   String WOW_date_time = string_WOW_system_year + "-" + string_WOW_system_month + "-" + string_WOW_system_day + "+" +
                          string_WOW_system_hour + "%3A" + string_WOW_system_minute + "%3A" + "00";
           
           
   //System.out.println("--- pressure (inHg) for WOW: " + WOW_date_time + " " + sensor_data_record_WOW_pressure_inhg);        
           
   String url = "http://wow.metoffice.gov.uk/automaticreading" + "?" + 
                                                   "siteid=" + main.WOW_site_id + "&" +
                                                   "siteAuthenticationKey=" + main.WOW_site_pin + "&" +
                                                   "dateutc=" + WOW_date_time + "&" +
                                                   "baromin=" + sensor_data_record_WOW_pressure_MSL_inhg + "&" +
                                                   "softwaretype=" + main.APPLICATION_NAME;
  
   URL obj = null;
   try 
   {
      obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      
      // optional (default is GET)
	   con.setRequestMethod("GET");  

      String message = "[WOW] sending 'GET' request to URL: " + url;
      main.log_turbowin_system_message(message);
      
      //add request header
      //String USER_AGENT = "Mozilla/5.0";
	   //con.setRequestProperty("User-Agent", USER_AGENT);
     
      int responseCode = con.getResponseCode();
		//System.out.println("--- WOW Response Code: " + responseCode);
      
      /* update status field (bottom line main -progress- window) */
      if (responseCode == 200)  // ok
      {
         // main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + "send data to WOW successesfully"); 
         String message_a = "[WOW] send data success"; 
         main.log_turbowin_system_message(message_a);
         main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message_a); // update status field (bottom line main -progress- window) 
      }
      else // not ok
      {
         BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		   String inputLine;
		   StringBuilder response = new StringBuilder();

		   while ((inputLine = in.readLine()) != null) 
         {
			   response.append(inputLine);
		   }
		   in.close();

		   String message_b = "[WOW] send data failed; " + response.toString(); 
         main.log_turbowin_system_message(message_b);
         main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message_b); 
      } // else (not ok)
   } 
   catch (MalformedURLException ex)
   {
      //String message = "[WOW] send data failed; MalformedURLException (function: RS232_Send_Sensor_Data_to_WOW)"; 
      String message = "[WOW] send data failed; MalformedURLException"; 
      main.log_turbowin_system_message(message);
      main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message);
   }
   catch (IOException ex) 
   {
      //String message = "[WOW] send data failed; most probably no internet connection available or firewall/scanner is blocking; IOException (function: RS232_Send_Sensor_Data_to_WOW)"; 
      String message = "[WOW] send data failed; most probably no internet connection available or firewall/scanner is blocking; IOException"; 
      main.log_turbowin_system_message(message);
      main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message);
   }
   
}   
 


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void set_datetime_while_collecting_sensor_data()
{
   // called from: class RS232_Class_Receive_Sensor_Data [file main_RS232_RS422.java]
   //              class Ethernet_Class_Receive_UDP [file main_RS232_RS422.java]
   //              class class WiFi_Class_Receive_UDP 
   //
   //
   //
   //
   // Never mind the computer is set to UTC or not. SimpleTimeZone with argument UTC convert system date time
   // always to UTC !!
   //

   String system_month_volledig = "";
   int len;

   main.cal_systeem_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // system date time in UTC of this moment
   main.cal_systeem_datum_tijd.getTime();                                             // now effective
   int system_minute_local = main.cal_systeem_datum_tijd.get(Calendar.MINUTE);
      
   if (system_minute_local % 5 == 0)
   {   
      if (system_minute_local > 30)
      {
         main.cal_systeem_datum_tijd.add(Calendar.HOUR_OF_DAY, 1);   // add 1 hour
         main.cal_systeem_datum_tijd.getTime();
      }

      int system_year          = main.cal_systeem_datum_tijd.get(Calendar.YEAR);
      int system_month         = main.cal_systeem_datum_tijd.get(Calendar.MONTH);        // The first month of the year is JANUARY which is 0
      int system_day_of_month  = main.cal_systeem_datum_tijd.get(Calendar.DAY_OF_MONTH); // The first day of the month has value 1
      int system_hour_of_day   = main.cal_systeem_datum_tijd.get(Calendar.HOUR_OF_DAY);


      if (system_month == 0)
      {
         system_month_volledig = "January";
      }
      else if (system_month == 1)
      {
         system_month_volledig = "February";
      }
      if (system_month == 2)
      {
         system_month_volledig = "March";
      }
      if (system_month == 3)
      {
         system_month_volledig = "April";
      }
      if (system_month == 4)
      {
         system_month_volledig = "May";
      }
      if (system_month == 5)
      {
         system_month_volledig = "June";
      }
      if (system_month == 6)
      {
         system_month_volledig = "July";
      }
      if (system_month == 7)
      {
         system_month_volledig = "August";
      }
      if (system_month == 8)
      {
         system_month_volledig = "September";
      }
      if (system_month == 9)
      {
         system_month_volledig = "October";
      }
      if (system_month == 10)
      {
         system_month_volledig = "November";
      }
      if (system_month == 11)
      {
         system_month_volledig = "December";
      }

      mydatetime.year  = Integer.toString(system_year);                     // for progress main screen and IMMT
      mydatetime.month = system_month_volledig;                             // for progress main screen
      mydatetime.day   = Integer.toString(system_day_of_month);             // for progress main screen
      mydatetime.hour  = Integer.toString(system_hour_of_day);              // for progress main screen

      /* update date and time on TurboWin+ main screen */
      main.date_time_fields_update();

      // determine code figures for month (not for operational obs only for IMMT storage)
      //
      mydatetime.MM_code = Integer.toString(system_month +1);               // +1 because system month started with 0 (January)

      len = 2;                                                               // MM_code always 2 characters width e.g. 03
      if (mydatetime.MM_code.length() < len)                                 // pad on left with zeros
      {
         mydatetime.MM_code = "0000000000".substring(0, len - mydatetime.MM_code.length()) + mydatetime.MM_code;
      }
         
      // determine code figures for day of the month (for obs and IMMT)
      //
      mydatetime.YY_code = Integer.toString(system_day_of_month);            // system_day_of_month e.g. 1,2,11,23,29

      len = 2;                                                               // YY_code always 2 characters width e.g. 03
      if (mydatetime.YY_code.length() < len)                                 // pad on left with zeros
      {
         mydatetime.YY_code = "0000000000".substring(0, len - mydatetime.YY_code.length()) + mydatetime.YY_code;
      }

      // determine code figures for hour of day (for obs and IMMT)
      //
      mydatetime.GG_code = Integer.toString(system_hour_of_day);             // system_hour_of_day e.g. 1,2,11,23

      len = 2;                                                               // GG_code always 2 characters width e.g. 03
      if (mydatetime.GG_code.length() < len)                                 // pad on left with zeros
      {
         mydatetime.GG_code = "0000000000".substring(0, len - mydatetime.GG_code.length()) + mydatetime.GG_code;
      }
   } // if (system_minute_local % 5 == 0)
}   
   


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void RS422_initialise_AWS_Sensor_Data_For_Display()
{
   // initialisation
   main.date_from_AWS_present                     = false;
   main.time_from_AWS_present                     = false;
   main.latitude_from_AWS_present                 = false;
   main.longitude_from_AWS_present                = false;
   main.COG_from_AWS_present                      = false;
   main.SOG_from_AWS_present                      = false;
   main.true_heading_from_AWS_present             = false;
   main.pressure_sensor_level_from_AWS_present    = false;
   main.pressure_MSL_from_AWS_present             = false;
   main.pressure_tendency_from_AWS_present        = false;
   main.pressure_characteristic_from_AWS_present  = false;
       //    NB in AWS connected mode the date and time parameters by default only from AWS!
       //    NB in AWS connected mode the GPS parameters (lat, lon, SOG, COG, true heading) by default only from AWS!
       //    NB in AWS connected mode the air pressure parameters by default only from AWS! (if not present than still observer cannot insert them manually)
   main.air_temp_from_AWS_present                 = false;
   main.rh_from_AWS_present                       = false;  
   main.SST_from_AWS_present                      = false;
   main.relative_wind_speed_from_AWS_present      = false;
   main.relative_wind_dir_from_AWS_present        = false;
   main.true_wind_speed_from_AWS_present          = false;
   main.true_wind_dir_from_AWS_present            = false;
   main.true_wind_gust_from_AWS_present           = false;
   
   mydatetime.year                                = "";
   mydatetime.month                               = "";
   mydatetime.day                                 = "";
   mydatetime.hour                                = "";
   myposition.latitude_hemisphere                 = "";
   myposition.latitude_degrees                    = "";
   myposition.latitude_minutes                    = "";
   myposition.longitude_hemisphere                = "";
   myposition.longitude_degrees                   = "";
   myposition.longitude_minutes                   = "";
   myposition.course                              = "";
   myposition.speed                               = "";
   mywind.ship_ground_course                      = "";
   mywind.ship_ground_speed                       = "";
   mywind.ship_heading                            = "";
   mybarometer.pressure_reading                   = "";
   mybarometer.pressure_reading_corrected         = "";
   mybarometer.pressure_msl_corrected             = "";
   mybarograph.pressure_amount_tendency           = "";
   mybarograph.a_code                             = "";
   
   if (main.jTextField36.getForeground().getRGB() != main.input_color_from_observer.getRGB())
   {
      mytemp.air_temp                             = "";
   }
   
   if (main.jTextField37.getForeground().getRGB() != main.input_color_from_observer.getRGB())
   {
      mytemp.wet_bulb_temp                        = "";
   }
   
   if (main.jTextField38.getForeground().getRGB() != main.input_color_from_observer.getRGB())
   {
      mytemp.RH                                   = "";
      mytemp.double_rv                            = main.INVALID;      // double
      mytemp.double_dew_point                     = main.INVALID;      // double
   }
   
   if (main.jTextField40.getForeground().getRGB() != main.input_color_from_observer.getRGB())
   {
      mytemp.sea_water_temp                       = "";
   }
   
   //if (main.jTextField16.getForeground().getRGB() != main.input_color_from_observer.getRGB())
   //{
   //   mywind.int_true_wind_speed                  = main.INVALID;      // integer
   //   mywind.wind_speed                           = "";
   //}
   //
   //if (main.jTextField17.getForeground().getRGB() != main.input_color_from_observer.getRGB())
   //{
   //   mywind.int_true_wind_dir                    = main.INVALID;      // integer
   //   mywind.wind_dir                             = "";
   //}
   if (main.jTextField16.getForeground().getRGB() != main.input_color_from_observer.getRGB())  // apparent wind
   {
      mywind.int_relative_wind_dir                = main.INVALID;
      mywind.int_relative_wind_speed              = main.INVALID;
      mywind.wind_speed                           = "";
      mywind.wind_dir                             = "";
   }
   
   if (main.jTextField17.getForeground().getRGB() != main.input_color_from_observer.getRGB())  // true wind
   {
      mywind.int_true_wind_speed                  = main.INVALID;      // integer
      mywind.int_true_wind_dir                    = main.INVALID;      // integer
   }

   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS422_Read_AWS_Sensor_Data_For_Display()
{
   // check the presence of the various meteo parameters in the data received from AWS (data stored by TurboWin+ in sensor data files)
   
   // called from: Function RS422_V3_write_sensor_data_to_file() [file main_RS232_RS422.java]
   //
   
   
   //check_presence_parameter_system_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // system date time in UTC of this moment
   check_presence_parameter_system_datum_tijd = new GregorianCalendar();                             // UTC at this momnet not important (only retrieving minutes)
   check_presence_parameter_system_datum_tijd.getTime();                                             // now effective
   
   //int system_minute = check_presence_parameter_system_datum_tijd.get(Calendar.MINUTE);
   system_minute = check_presence_parameter_system_datum_tijd.get(Calendar.MINUTE);
      
   // initialisation
   RS422_initialise_AWS_Sensor_Data_For_Display();
   
   
   // only check every 1 or 5 minutes? [eg if (system_minute % 5 == 0)]
   if (system_minute % 1 == 0)   
   {
      //System.out.println("+++ checking parameter presence in sensor data file");
      
      new SwingWorker<String, Void>()
      {
         @Override
         protected String doInBackground() throws Exception
         {
            File sensor_data_file;
            String record;
            String last_record = null;
            
            
            if (system_minute != 0)
            {           
               // otherwise at 00 minutes the system is looking in an 'old' file because of the -1 MINUTE (without the 00 minutes sensor obs)
               check_presence_parameter_system_datum_tijd.add(Calendar.MINUTE, -1);
            }
            
            String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(check_presence_parameter_system_datum_tijd.getTime()); // -> eg 2013020308 // sdf3 was set in RS422_initComponents() to UTC
            String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

            // first check if there is a sensor data file present (and not empty)
            String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;         

            //System.out.println("+++ checking file: " + volledig_path_sensor_data);
            
            sensor_data_file = new File(volledig_path_sensor_data);
            if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
            {
               try
               {
                  BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

                  try
                  {
                     // initialisation
                     record         = "";
                     last_record    = "";

                     while ((record = in.readLine()) != null)
                     {
                        //pos = record.indexOf("\n");    
                        //if (pos > 0)     // "\n" found
                        
                        if (record.length() > 43)               // arbitrary but > $PEUMB + yyyymmdduu + TOTAL_NUMBER_RECORD_COMMAS
                        {
                           if ( (record.substring(0, 6).equals("$PEUMB")) && (record.indexOf(sensor_data_file_naam_datum_tijd_deel) != -1) )
                           {
                              last_record = record;
                           }
                        } // if (record.length() > 43)  
                     } // while ((record = in.readLine()) != null)

                     //System.out.println("+++ gepasseerd 1");
/*  
                     // block deleted
*/                     
                  } // try
                  finally
                  {
                     in.close();
                  }
               } // try
               catch (IOException ex) 
               {  
                  last_record = "";
               }
            } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
            else // no sensor_data_file present
            {
               last_record = null;
            }
            
            return last_record;
            
         } // protected Void doInBackground() throws Exception

         @Override
         protected void done()
         {
            boolean doorgaan_in_record = true;
            int number_read_commas;
            int pos;

            
            String laatste_record = null;
            try 
            {
               laatste_record = get();
            } 
            catch (InterruptedException | ExecutionException ex) 
            {
               String message = "[AWS] error retrieving AWS data from sensor data file; " + ex.toString();
               main.log_turbowin_system_message(message);
            }
            
            if (laatste_record == null)
            {
               String message = "[AWS] no sensor data file exists or file empty";
               main.log_turbowin_system_message(message);    
               doorgaan_in_record = false;
            }
            else if (laatste_record.equals(""))
            {
               String message = "[AWS] sensor data file found but no records found or misformed records";
               main.log_turbowin_system_message(message); 
               doorgaan_in_record = false;
            }
            
            
            if (doorgaan_in_record == true && laatste_record != null) 
            {
               // Check the number of comma's in the record that must exactely match the fixed expected number (TOTAL_NUMBER_RECORD_COMMAS)
               number_read_commas = 0;
               pos = -1;

               do 
               {
                  pos = laatste_record.indexOf(",", pos + 1);
                  if (pos > 0) // "," found
                  {
                     number_read_commas++;
                  }
               } while (pos > 0);

               //System.out.println("+++ record = " + laatste_record);
               //System.out.println("+++ number read commas = " + number_read_commas);
               if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS) {
                  doorgaan_in_record = false;
               }
            } // if (doorgaan_in_record == true)

            if (doorgaan_in_record == true && laatste_record != null) 
            {
               number_read_commas = 0;
               pos = -1;

               do 
               {
                  pos = laatste_record.indexOf(",", pos + 1);    // searching "," from position "pos + 1"
                  if (pos > 0) // "," found
                  {
                     number_read_commas++;

                     // Date
                     //
                     if (number_read_commas == main.DATE_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between conmmas at least 1 char
                           {
                              String record_aws_date = laatste_record.substring(pos + 1, pos2); // YYYYMMDD or YYMMDD

                              //mydatetime.day   = record_aws_date.substring(record_aws_date.length() -2) + " -";   // DD
                              //mydatetime.month = record_aws_date.substring(record_aws_date.length() -4, record_aws_date.length() -2) + " -"; // MM
                              mydatetime.day = record_aws_date.substring(record_aws_date.length() - 2);   // DD

                              if (mydatetime.day.substring(0, 1).equals("0")) 
                              {
                                 // otherwise will not match in the mydatetime input form
                                 mydatetime.day = mydatetime.day.substring(1, 2);
                              }

                              //System.out.println("+++ day = " + mydatetime.day);
                              String str_month = record_aws_date.substring(record_aws_date.length() - 4, record_aws_date.length() - 2); // MM

                                       //System.out.println("+++ month = " + str_month);
                              switch (str_month) 
                              {
                                 case "01":
                                    mydatetime.month = "January";
                                    break;
                                 case "02":
                                    mydatetime.month = "February";
                                    break;
                                 case "03":
                                    mydatetime.month = "March";
                                    break;
                                 case "04":
                                    mydatetime.month = "April";
                                    break;
                                 case "05":
                                    mydatetime.month = "May";
                                    break;
                                 case "06":
                                    mydatetime.month = "June";
                                    break;
                                 case "07":
                                    mydatetime.month = "July";
                                    break;
                                 case "08":
                                    mydatetime.month = "August";
                                    break;
                                 case "09":
                                    mydatetime.month = "September";
                                    break;
                                 case "10":
                                    mydatetime.month = "October";
                                    break;
                                 case "11":
                                    mydatetime.month = "November";
                                    break;
                                 case "12":
                                    mydatetime.month = "December";
                                    break;
                                 default:
                                    mydatetime.month = "unknown";
                                    break;
                              }

                              mydatetime.year = record_aws_date.substring(0, record_aws_date.length() - 4);       // YYYY (or YY)

                              main.date_from_AWS_present = true;
                              //System.out.println("+++ parameter date is aanwezig");

                           } // if (pos2 - pos >= 2)
                        }

                     } // if (number_read_commas == etc.                             
                     // Time
                     //
                     else if (number_read_commas == main.TIME_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between conmmas at least 1 char
                           {
                              String record_aws_time = laatste_record.substring(pos + 1, pos2);
                              if (record_aws_time.length() == 6) // HHMMSS
                              {
                                 mydatetime.hour = record_aws_time.substring(0, 2);   // HH from HHMMSS
                                 mydatetime.minute = record_aws_time.substring(2, 4); // MM from HHMMSS
                              }

                              if (mydatetime.hour.substring(0, 1).equals("0")) 
                              {
                                 // otherwise will not match in the mydatetime input form
                                 mydatetime.hour = mydatetime.hour.substring(1, 2);
                              }

                              main.time_from_AWS_present = true;
                              //System.out.println("+++ parameter time is aanwezig");

                           } // if (pos2 - pos >= 2)
                        }
                     } // else if (number_read_commas == etc.                             
                     // Latitude [format sDD.ddd; s = "-" if South; + always omitted]
                     //
                     else if (number_read_commas == main.LATITUDE_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              String record_aws_latitude = laatste_record.substring(pos + 1, pos2);

                              if (record_aws_latitude.indexOf("-") != -1) // found "-" in string
                              {
                                 myposition.latitude_hemisphere = "South";
                                 myposition.latitude_degrees = record_aws_latitude.substring(1, record_aws_latitude.indexOf("."));   // -12.344 -> 12
                              } else 
                              {
                                 myposition.latitude_hemisphere = "North";
                                 myposition.latitude_degrees = record_aws_latitude.substring(0, record_aws_latitude.indexOf("."));   // 12.344 -> 12
                              }

                              double decimal_degrees_lat = Double.parseDouble(record_aws_latitude);
                                       //double double_minutes_lat = (decimal_degrees_lat - Math.floor(decimal_degrees_lat)) * 60;
                              //
                              //BigDecimal bd = new BigDecimal(double_minutes_lat).setScale(1, RoundingMode.HALF_UP);  // one decimal, rounded e.g. 2.12939 -> 2.1
                              //double_minutes_lat = bd.doubleValue();
                              //
                              //if (double_minutes_lat < 10)
                              //{   
                              //   myposition.latitude_minutes = "0" + Double.toString(double_minutes_lat);
                              //}
                              //else
                              //{
                              //   myposition.latitude_minutes = Double.toString(double_minutes_lat);
                              //} 
                              int int_minutes_lat = (int) Math.round((decimal_degrees_lat - Math.floor(decimal_degrees_lat)) * 60);
                              if (int_minutes_lat < 10) 
                              {
                                 myposition.latitude_minutes = "0" + Integer.toString(int_minutes_lat);
                              } 
                              else 
                              {
                                 myposition.latitude_minutes = Integer.toString(int_minutes_lat);
                              }

                              main.latitude_from_AWS_present = true;
                              //System.out.println("+++ parameter latitude is aanwezig");

                           } // if (pos2 - pos >= 2)
                        } // if (laatste_record.length() etc.
                     } // else if (number_read_commas == etc.                                     
                     // Longitude [format sDDD.ddd; s = "-" if West; + always omitted]
                     //
                     else if (number_read_commas == main.LONGITUDE_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              String record_aws_longitude = laatste_record.substring(pos + 1, pos2);

                              if (record_aws_longitude.indexOf("-") != -1) // found "-" in string
                              {
                                 myposition.longitude_hemisphere = "West";
                                 myposition.longitude_degrees = record_aws_longitude.substring(1, record_aws_longitude.indexOf("."));   // -12.344 -> 12
                              } 
                              else 
                              {
                                 myposition.longitude_hemisphere = "East";
                                 myposition.longitude_degrees = record_aws_longitude.substring(0, record_aws_longitude.indexOf("."));   // 12.344 -> 12
                              }

                              double decimal_degrees_lon = Math.abs(Double.parseDouble(record_aws_longitude));
                                       //double double_minutes_lon = (decimal_degrees_lon - Math.floor(decimal_degrees_lon)) * 60;
                              //
                              //BigDecimal bd = new BigDecimal(double_minutes_lon).setScale(1, RoundingMode.HALF_UP);  // one decimal, rounded e.g. 2.12939 -> 2.1
                              //double_minutes_lon = bd.doubleValue();
                              //
                              //if (double_minutes_lon < 10)
                              //{
                              //   myposition.longitude_minutes = "0" + Double.toString(double_minutes_lon);
                              //}  
                              //else
                              //{   
                              //   myposition.longitude_minutes = Double.toString(double_minutes_lon);
                              //}
                              int int_minutes_lon = (int) Math.round((decimal_degrees_lon - Math.floor(decimal_degrees_lon)) * 60);
                              if (int_minutes_lon < 10) 
                              {
                                 myposition.longitude_minutes = "0" + Integer.toString(int_minutes_lon);
                              } 
                              else 
                              {
                                 myposition.longitude_minutes = Integer.toString(int_minutes_lon);
                              }

                              main.longitude_from_AWS_present = true;
                              //System.out.println("+++ parameter longitude is aanwezig");

                           } // if (pos2 - pos >= 2)
                        } // 
                     } // else if (number_read_commas == etc.                                     
                     // Course Over Ground (COG) [format CCC.c]
                     //
                     else if (number_read_commas == main.COG_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              String record_aws_COG = laatste_record.substring(pos + 1, pos2);

                              int int_COG = (int) Math.round(Double.parseDouble(record_aws_COG));

                              if (int_COG == 0) 
                              {
                                 int_COG = 360;
                              }

                              // NB if the ship is stationary then COG retrieved from the the AWS will be blanco -> unfortunately no option to indicate stationary
                              //if (int_COG == 0)
                              //{
                              //   myposition.course = myposition.COURSE_STATIONARY;
                              //}
                              if (int_COG >= 23 && int_COG <= 67) {
                                 myposition.course = myposition.COURSE_023_067;
                              } else if (int_COG >= 68 && int_COG <= 112) {
                                 myposition.course = myposition.COURSE_068_112;
                              } else if (int_COG >= 113 && int_COG <= 157) {
                                 myposition.course = myposition.COURSE_113_157;
                              } else if (int_COG >= 158 && int_COG <= 202) {
                                 myposition.course = myposition.COURSE_158_202;
                              } else if (int_COG >= 203 && int_COG <= 247) {
                                 myposition.course = myposition.COURSE_203_247;
                              } else if (int_COG >= 248 && int_COG <= 292) {
                                 myposition.course = myposition.COURSE_248_292;
                              } else if (int_COG >= 293 && int_COG <= 337) {
                                 myposition.course = myposition.COURSE_293_337;
                              } else if (int_COG >= 338 && int_COG <= 360) {
                                 myposition.course = myposition.COURSE_338_022;
                              } else if (int_COG > 0 && int_COG <= 22) {
                                 myposition.course = myposition.COURSE_338_022;
                              }

                              // COG = 10 min average so can also used for wind input page
                              //mywind.ship_ground_course = record_aws_COG;
                              //
                              //if (mywind.ship_ground_course.equals("0.0"))
                              //{
                              //   mywind.ship_ground_course = "360.0";
                              //}
                              mywind.ship_ground_course = Integer.toString(int_COG);

                              main.COG_from_AWS_present = true;
                              //System.out.println("+++ parameter COG is aanwezig");

                           } // if (pos2 - pos >= 2)
                        } // if (record.length() > number_read_commas + 12)
                     } // else if (number_read_commas == etc.
                     // Speed Over Ground (SOG) [format SS.s]
                     //
                     else if (number_read_commas == main.SOG_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between conmmas at least 1 char
                           {
                              String record_aws_SOG = laatste_record.substring(pos + 1, pos2);  // in m/s
                              double double_SOG = Double.parseDouble(record_aws_SOG);           // in m/s
                              int int_SOG = (int) Math.round(double_SOG * M_S_TO_KNOTS);         // in knots rounded

                              if (int_SOG == 0) {
                                 myposition.speed = myposition.SPEED_0;
                              } else if (int_SOG >= 1 && int_SOG <= 5) {
                                 myposition.speed = myposition.SPEED_1_5;
                              } else if (int_SOG >= 6 && int_SOG <= 10) {
                                 myposition.speed = myposition.SPEED_6_10;
                              } else if (int_SOG >= 11 && int_SOG <= 15) {
                                 myposition.speed = myposition.SPEED_11_15;
                              } else if (int_SOG >= 16 && int_SOG <= 20) {
                                 myposition.speed = myposition.SPEED_16_20;
                              } else if (int_SOG >= 21 && int_SOG <= 25) {
                                 myposition.speed = myposition.SPEED_21_25;
                              } else if (int_SOG >= 26 && int_SOG <= 30) {
                                 myposition.speed = myposition.SPEED_26_30;
                              } else if (int_SOG >= 31 && int_SOG <= 35) {
                                 myposition.speed = myposition.SPEED_31_35;
                              } else if (int_SOG >= 36 && int_SOG <= 40) {
                                 myposition.speed = myposition.SPEED_36_40;
                              } else if (int_SOG >= 41) {
                                 myposition.speed = myposition.SPEED_MORE_40;
                              }

                              // SOG 10 min average so can also used for wind input page
                              double_SOG = Math.round(double_SOG * main.M_S_KNOT_CONVERSION * 10) / 10.0; // always round to one decimal
                              mywind.ship_ground_speed = Double.toString(double_SOG);

                              main.SOG_from_AWS_present = true;
                              //System.out.println("+++ parameter SOG is aanwezig");

                           } // if (pos2 - pos >= 2)
                        } // if (record.length() > number_read_commas + 12)
                     } // else if (number_read_commas == etc.
                     // True heading [format HHH.h]
                     //
                     else if (number_read_commas == main.HEADING_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              String record_aws_ship_heading = laatste_record.substring(pos + 1, pos2);
                              double double_ship_heading = Double.parseDouble(record_aws_ship_heading);

                              int int_ship_heading = (int) Math.round(double_ship_heading);

                              if (int_ship_heading == 0) 
                              {
                                 int_ship_heading = 360;
                              }

                              mywind.ship_heading = Integer.toString(int_ship_heading);

                              main.true_heading_from_AWS_present = true;
                              // NB heading is not displayed on TurboWin+ main screen (only on wind input form)

                              //System.out.println("+++ parameter Heading is aanwezig");
                           } // if (pos2 - pos >= 2)
                        } // if (record.length() > number_read_commas + 12)
                     } // else if (number_read_commas == etc.
                     // air pressure at sensor height (read) [format PPPP.p]
                     //
                     else if (number_read_commas == main.PRESSURE_SENSOR_HEIGHT_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between conmmas at least 1 char
                           {
                              mybarometer.pressure_reading_corrected = laatste_record.substring(pos + 1, pos2);

                              // by default for pressure value obtained from AWS!
                              mybarometer.pressure_reading = mybarometer.pressure_reading_corrected;

                              // ic = 0.0 by default for pressure value obtained from AWS!
                              main.barometer_instrument_correction = "0.0";

                                       // for barometer input form
                              //if (main.pressure_reading_msl_yes_no.equals(main.PRESSURE_READING_MSL_NO))
                              //{
                              //   mybarometer.pressure_reading = mybarometer.pressure_reading_corrected;
                              //    
                              //   // ic = 0.0 by default for pressure value obtained from AWS!
                              //   main.barometer_instrument_correction = "0.0";
                              //}
                              main.pressure_sensor_level_from_AWS_present = true;
                              //System.out.println("+++ parameter air pressure read is aanwezig");

                           } // if (pos2 - pos >= 2)
                        } // if (record.length() > number_read_commas + 12)
                     } // else if (number_read_commas == etc.
                     // air pressure MSL [format PPPP.p]
                     //
                     else if (number_read_commas == main.PRESSURE_MSL_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between conmmas at least 1 char
                           {
                              mybarometer.pressure_msl_corrected = laatste_record.substring(pos + 1, pos2);

                              // for barometer input form
                              // NB if AWS connected, barometer input form will always indicate barometer reading at sensor level (never MSL)
                              main.pressure_MSL_from_AWS_present = true;
                              //System.out.println("+++ parameter air pressure MSL is aanwezig");

                           } // if (pos2 - pos >= 2)
                        } // if (record.length() > number_read_commas + 12)
                     } // else if (number_read_commas == etc.
                     // air pressure tendency over three three hours [format sPP.p]
                     //
                     else if (number_read_commas == main.PRESSURE_TENDENCY_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between conmmas at least 1 char
                           {
                              mybarograph.pressure_amount_tendency = laatste_record.substring(pos + 1, pos2);

                              main.pressure_tendency_from_AWS_present = true;
                              //System.out.println("+++ parameter air pressure tendency is aanwezig");

                           } // if (pos2 - pos >= 2)
                        } // if (record.length() > number_read_commas + 12)
                     } // else if (number_read_commas == etc.                              
                     // air pressure characteristic [format A]
                     //
                     else if (number_read_commas == main.PRESSURE_CHARACTERISTIC_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between conmmas at least 1 char
                           {
                              mybarograph.a_code = laatste_record.substring(pos + 1, pos2);

                              main.pressure_characteristic_from_AWS_present = true;
                              //System.out.println("+++ parameter air pressure characteristic (a code) is aanwezig");

                           } // if (pos2 - pos >= 2)
                        } // if (record.length() > number_read_commas + 12)
                     } // else if (number_read_commas == etc.    
                     // air temp [format sTA.a]
                     //
                     else if (number_read_commas == main.AIR_TEMP_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between conmmas at least 1 char
                           {
                              mytemp.air_temp = laatste_record.substring(pos + 1, pos2);

                              main.air_temp_from_AWS_present = true;

                                       //System.out.println("+++ parameter air temp is aanwezig");
                           } // if (pos2 - pos >= 2)

                           //System.out.println("+++++++= pos = " + pos);
                           //System.out.println("+++++++= pos2 = " + pos2);
                           //System.out.println("+++++++=  main.air_temp_from_AWS_present = " +  main.air_temp_from_AWS_present);
                        } // 
                     } // else if (number_read_commas == etc.
                     // relative humidity [format UUU.u]
                     //
                     else if (number_read_commas == main.HUMIDITY_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between conmmas at least 1 char
                           {
                              mytemp.double_rv = Double.parseDouble(laatste_record.substring(pos + 1, pos2)) / 100;
                              // NB "/100" for consistency with not AWS computed rh (see also main.temperatures_fields_update() and mytemp.OK_button_actioPerformed())

                              mytemp.RH = laatste_record.substring(pos + 1, pos2);
                              
                              main.rh_from_AWS_present = true;

                              //System.out.println("+++ parameter relative humidity is aanwezig");
                           } // if (pos2 - pos >= 2)
                        } // 
                     } // else if (number_read_commas == etc.
                     // SST [format sTW.w]
                     //
                     else if (number_read_commas == main.SST_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between conmmas at least 1 char
                           {
                              mytemp.sea_water_temp = laatste_record.substring(pos + 1, pos2);

                              main.SST_from_AWS_present = true;

                              //System.out.println("+++ parameter sst is aanwezig");
                           } // if (pos2 - pos >= 2)
                        } // 
                     } // else if (number_read_commas == etc.
                     // relative wind speed [format WS.r]
                     //
                     else if (number_read_commas == main.RELATIVE_WIND_SPEED_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              int int_relative_wind_speed;
                              String record_aws_relative_wind_speed = laatste_record.substring(pos + 1, pos2);
                              Double double_relative_wind_speed = Double.parseDouble(record_aws_relative_wind_speed);

                              // convert if necessary and round to integer
                              if (main.wind_units.indexOf(main.M_S) == -1) //  not m/s so convert to knots (see TurboWin+ menu:
                              {
                                 int_relative_wind_speed = (int) Math.round(double_relative_wind_speed * main.M_S_KNOT_CONVERSION); // NB int cast necessary for conversion from long (result from Math.round) to int
                                 double_relative_wind_speed = double_relative_wind_speed * main.M_S_KNOT_CONVERSION;
                              } 
                              else 
                              {
                                 int_relative_wind_speed = (int) Math.round(double_relative_wind_speed);
                                 // NB double_relative_wind_speed in m/s
                              }

                              mywind.wind_speed = Integer.toString(int_relative_wind_speed);        // for wind input screen
                              mywind.int_relative_wind_speed = int_relative_wind_speed;
                              mywind.double_relative_wind_speed = double_relative_wind_speed;              // only for dashboard
                              
                              main.relative_wind_speed_from_AWS_present = true;

                              //System.out.println("+++ parameter relative wind speed is aanwezig");
                           } // if (pos2 - pos >= 2)
                        } // 
                     } // else if (number_read_commas == etc.
                     // relative wind direction [format WDR.r]
                     //
                     else if (number_read_commas == main.RELATIVE_WIND_DIR_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              String record_aws_relative_wind_dir = laatste_record.substring(pos + 1, pos2);
                              double double_relative_wind_dir = Double.parseDouble(record_aws_relative_wind_dir);

                              //int int_relative_wind_dir = (int) Math.round(double_relative_wind_dir);
                              //mywind.int_relative_wind_dir = (int) Math.round(double_relative_wind_dir);
                              int int_relative_wind_dir = (int) Math.round(double_relative_wind_dir);

                              //if (int_relative_wind_dir == 0)
                              //{
                              //   int_relative_wind_dir = 360;
                              //}
                              //mywind.wind_dir = Integer.toString(mywind.int_relative_wind_dir);
                              mywind.wind_dir = Integer.toString(int_relative_wind_dir);           // for wind input screen
                              mywind.int_relative_wind_dir = int_relative_wind_dir;
                              main.relative_wind_dir_from_AWS_present = true;

                              //System.out.println("+++ parameter relative wind dir is aanwezig");
                           } // if (pos2 - pos >= 2)
                        } // 
                     } // else if (number_read_commas == etc.          
                     // true wind speed [format WS.t]
                     //
                     else if (number_read_commas == main.TRUE_WIND_SPEED_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              int int_true_wind_speed;
                              String record_aws_true_wind_speed = laatste_record.substring(pos + 1, pos2);
                              Double double_true_wind_speed = Double.parseDouble(record_aws_true_wind_speed); 

                              if (main.wind_units.indexOf(main.M_S) == -1) //  not m/s so convert to knots (see TurboWin+ menu:
                              {
                                 int_true_wind_speed = (int) Math.round(double_true_wind_speed * main.M_S_KNOT_CONVERSION); // NB int cast necessary for conversion from long (result from Math.round) to int
                                 double_true_wind_speed = double_true_wind_speed * main.M_S_KNOT_CONVERSION; // in knots
                              } 
                              else 
                              {
                                 int_true_wind_speed = (int) Math.round(double_true_wind_speed);
                                 //NB double_true_wind_speed; in m/s
                              }

                              mywind.int_true_wind_speed = int_true_wind_speed;
                              mywind.double_true_wind_speed = double_true_wind_speed;                         // only for dashboard

                              main.true_wind_speed_from_AWS_present = true;

                              //System.out.println("+++ parameter true wind speed is aanwezig");
                           } // if (pos2 - pos >= 2)
                        } // 
                     } // else if (number_read_commas == etc.
                     // true wind direction [format WDT.t]
                     //
                     else if (number_read_commas == main.TRUE_WIND_DIR_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              String record_aws_true_wind_dir = laatste_record.substring(pos + 1, pos2);
                              double double_true_wind_dir = Double.parseDouble(record_aws_true_wind_dir);

                              int int_true_wind_dir = (int) Math.round(double_true_wind_dir);

                              if (int_true_wind_dir == 0) 
                              {
                                 int_true_wind_dir = 360;
                              }

                              mywind.int_true_wind_dir = int_true_wind_dir;

                              main.true_wind_dir_from_AWS_present = true;

                              //System.out.println("+++ parameter true wind dir is aanwezig");
                           } // if (pos2 - pos >= 2)
                        } // if (laatste_record.length() > pos + 1 + 12)
                     } // else if (number_read_commas == etc.                              
                     // max wind gust speed from 10min [format WSX.x]
                     //
                     else if (number_read_commas == main.TRUE_WIND_GUST_COMMA_NUMBER)
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              int int_true_wind_gust_speed;
                              String record_aws_true_wind_gust_speed = laatste_record.substring(pos + 1, pos2);
                              Double double_true_wind_gust_speed = Double.parseDouble(record_aws_true_wind_gust_speed);

                              // convert if necessary and round to integer
                              if (main.wind_units.indexOf(main.M_S) == -1) //  not originally measured/observed in m/s so convert to knots (see TurboWin+ menu: Maintenance -> Station data)
                              {
                                 int_true_wind_gust_speed = (int) Math.round(double_true_wind_gust_speed * main.M_S_KNOT_CONVERSION); // NB int cast necessary for conversion from long (result from Math.round) to int
                                 double_true_wind_gust_speed = double_true_wind_gust_speed * main.M_S_KNOT_CONVERSION;      // knots   // only for dashboard
                              } 
                              else 
                              {
                                 int_true_wind_gust_speed = (int) Math.round(double_true_wind_gust_speed);
                                 // NB double_true_wind_gust_speed in knots
                              }

                              mywind.int_true_wind_gust_speed = int_true_wind_gust_speed;
                              mywind.double_true_wind_gust_speed = double_true_wind_gust_speed;    // only for dashboard

                              main.true_wind_gust_from_AWS_present = true;
                           } // if (pos2 - pos >= 2)
                        } // if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     } // else if (number_read_commas == main.TRUE_WIND_GUST_COMMA_NUMBER)
                     //
                     // max wind gust direction from 10min [format WDX.x]
                     //
                     // -- not for displaying in TurboWin+
                     //
                     // supply voltage [format VV.v]
                     //
                     // -- not for displaying in TurboWin+
                     //
                     // internal temperature [format sTT.t]
                     //
                     // -- not for displaying in TurboWin+
                     //
                     // VOT (visual observation acquisition trigger) [format sNNN]
                     //
                     else if (number_read_commas == main.VOT_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              //String record_aws_VOT = laatste_record.substring(pos + 1, pos2);
                              int int_VOT = Integer.parseInt(laatste_record.substring(pos + 1, pos2));
                              //String hulp_VOT = Integer.toString(Math.abs(int_VOT));

                              if (int_VOT < 0) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 //main.jTextField4.setText(hulp_VOT + " minutes to go before you can insert obervation parameters");
                                 int_VOT = Math.abs(int_VOT) + 20;
                                 String str_VOT = Integer.toString(int_VOT);
                                 main.jTextField4.setText(str_VOT + " minutes to go before the obs will be sent by the AWS, please do not insert observation parameters");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else if (int_VOT == 100) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 main.jTextField4.setText("ship is in port (no obs will be sent)");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else if (int_VOT == 101) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 main.jTextField4.setText("observations are not required in this area (no obs will be sent)");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else if (int_VOT == 102) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 main.jTextField4.setText("the AWS transmitter is out of order (no obs will be sent)");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else if (int_VOT == 103) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 main.jTextField4.setText("the AWS GPS is out of order (no obs will be sent)");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else if (int_VOT == 104) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 main.jTextField4.setText("AWS transmission is deactivated (no obs will be sent)");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else 
                              {
                                 main.jTextField4.setForeground(color_dark_green);
                                 String str_VOT = Integer.toString(int_VOT);
                                 main.jTextField4.setText(str_VOT + " minutes to go before the obs will be sent by the AWS, you can now insert observation parameters and select \"Output -> Obs to AWS\" ");

                                 main.jMenuItem46.setEnabled(true);                  // Obs to AWS
                              }

                              //System.out.println("+++ parameter VOT is aanwezig");
                           } // if (pos2 - pos >= 2)
                        } // 
                     } // else if (number_read_commas == etc.   

                     // SPARE
                     //
                     // -- not for displaying in TurboWin+
                     // 
                              //
                     // SPARE
                     // -- not for displaying in TurboWin+
                     //
                     // SPARE
                     //
                     // -- not for displaying in TurboWin+
                     //
                  } // if (pos > 0)
               } while (pos > 0);

               
               // update data text fields on main screen
               //
               main.date_time_fields_update();
               main.position_fields_update();
               main.barometer_fields_update();
               main.barograph_fields_update();
               main.temperatures_fields_update();
               main.wind_fields_update();
               
               // update AWS dasboard values
               RS422_update_AWS_dasboard_values();

            } // if (doorgaan_in_record)                       
                     
 
            // clear memory
            check_presence_parameter_system_datum_tijd    = null;
            
         } // protected void done()

      }.execute(); // new SwingWorker <Void, Void>()
      
   } // if (system_minute % 1 == 0)  
} // private void RS422_Read_AWS_Sensor_Data_For_Display()



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private static void RS422_initialise_AWS_Sensor_Data_For_AWSR()
{
   // called from: RS422_Read_And_Send_Sensor_Data_For_AWSR() [main_RS232_RS422.java]
   
   
   // Date and Time   
   mydatetime.year                                 = "";
   mydatetime.month                                = "";
   mydatetime.day                                  = "";
   mydatetime.hour                                 = "";
   mydatetime.MM_code                              = "";                            // month of year (only for IMMT)
   mydatetime.YY_code                              = "";                            // day of the month
   mydatetime.GG_code                              = "";                            // hour of obs

   // position
   myposition.Qc_code                              = "";
   myposition.int_latitude_degrees                 = main.INVALID;
   myposition.int_latitude_minutes                 = main.INVALID;
   myposition.latitude_degrees                     = "";
   myposition.latitude_hemisphere                  = "";
   myposition.lalala_code                          = "";
   myposition.int_longitude_degrees                = main.INVALID;
   myposition.int_longitude_minutes                = main.INVALID;
   myposition.longitude_degrees                    = "";
   myposition.longitude_hemisphere                 = "";
   myposition.lolololo_code                        = ""; 
   
   // Course and speed
   myposition.Ds_code                              = "";
   myposition.vs_code                              = "";
   mywind.ship_heading                             = "";
   
  // Air pressure
   mybarometer.pressure_reading_corrected          = "";
   mybarometer.pressure_msl_corrected              = "";
   mybarometer.PPPP_code                           = "";
   mybarograph.pressure_amount_tendency            = "";
   mybarograph.a_code                              = "";
   mybarograph.ppp_code                            = "";
   
   // Temperatures
   mytemp.air_temp                                 = "";
   mytemp.sn_TTT_code                              = "";
   mytemp.TTT_code                                 = "";
   mytemp.double_rv                                = main.INVALID;      // double
   mytemp.sea_water_temp                           = "";
   mytemp.ss_TsTsTs_code                           = "";
   mytemp.immt_sst_indicator                       = "";
   mytemp.TsTsTs_code                              = "";
   
   // wind
   mywind.RWS_code                                 = "";
   mywind.RWD_code                                 = "";
   mywind.int_true_wind_speed                      = main.INVALID;      // integer
   mywind.ff_code                                  = "";
   mywind.int_true_wind_dir                        = main.INVALID;      // integer
   mywind.dd_code                                  = "";
   mywind.HDG_code                                 = "";
   mywind.COG_code                                 = "";
   mywind.SOG_code                                 = "";
   
   // wind related (IMMT only)
   mywind.SLL_code                                 = "";
   mywind.sl_code                                  = "";
   mywind.hh_code                                  = "";
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private boolean RS422_Check_Date_Time_Last_Saved_Record_For_AWSR(final String record_aws_date, final String record_aws_time)
{
   // called from: RS422_Read_And_Send_Sensor_Data_For_AWSR() [main_RS232_RS422.java]
   //
   // NB record_aws_date format: yyyyMMdd
   // NB record_aws_time format: HHMmmss
   
   
   boolean record_date_time_ok = true;
   
   
   if ((record_aws_date.length() != 8) || (record_aws_time.length() != 6))
   {
      record_date_time_ok = false;  
   }
   
   if (record_date_time_ok)
   {
      SimpleDateFormat sdf9 = new SimpleDateFormat("yyyyMMddHHmm");                      // HH hour in day (0-23) note there is also hh (then also with am, pm); July = 07 !!
      sdf9.setTimeZone(TimeZone.getTimeZone("UTC"));
   
      //String hulp_GPS_yyyyMMdd = "20" + GPS_date.substring(4) + GPS_date.substring(2,4) + GPS_date.substring(0,2);
      //String hulp_GPS_HHmm = GPS_time.substring(0, 4); 
      //String hulp_fix_datum_tijd_minuten = hulp_GPS_yyyyMMdd + hulp_GPS_HHmm;    // eg final result : 202303940927
      String hulp_record_datum_tijd_minuten = record_aws_date + record_aws_time.substring(0, 4);    // eg final result : 202303940927
      Date hulp_record_date = null;
      try 
      {
         hulp_record_date = sdf9.parse(hulp_record_datum_tijd_minuten);
         
         long system_sec = System.currentTimeMillis();
         long timeDiff = Math.abs(hulp_record_date.getTime() - system_sec) / (60 * 1000);           // timeDiff in minutes

         //System.out.println("+++ system_sec: " + system_sec); 
         //System.out.println("+++ hulp_record_date: " + hulp_record_date); 
         //System.out.println("+++ difference [minuten]: " + timeDiff); //differencs in min
      
         //String record_pressure_present = null;

         if (timeDiff > TIMEDIFF_AWSR_DATA)      // max 10 minutes difference AWS time <--> computer time
         {
            record_date_time_ok = false;
            String message = "[AWSR] AWS last saved record date/time obsolete; > " + (int)TIMEDIFF_AWSR_DATA + " minutes old compared with computer date/time (time difference = " + timeDiff + " minutes)";
            main.log_turbowin_system_message(message);
         }    
      } // try
      catch (ParseException ex) 
      {
         record_date_time_ok = false;
         String message = "[AWSR] AWS date/time format not ok (" + ex + ")";
         main.log_turbowin_system_message(message);
      }
   } // if (record_date_time_ok)   
   
   
   return record_date_time_ok;
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS422_Send_Sensor_Data_To_AWSR(final boolean retry)
{
   // called from: RS422_Read_And_Send_Sensor_Data_For_AWSR() [main_RS232_RS422.java]
   //
   
   ///////////////// compress the data end send it //////////////////////
   //
   new SwingWorker<Integer, Void>()
   {
      @Override
      protected Integer doInBackground() throws Exception
      {
         Integer responseCode          = main.OK_RESPONSE_FORMAT_101;            // OK 
         
         // NB in this phase you kow: - date-time ok
         //                           - position ok
         //                           - call sign ok (RS422_check_AWSR_settings()) [main_RS232_Rs422.java]
         //                           - logs ok (RS422_check_AWSR_settings()) [main_RS232_Rs422.java]
         //
             
         // compress the data
         //
         main.format_101_class = new FORMAT_101();
         main.format_101_class.compress_and_decompress_101_control_center();
            
            
         // read the compressed obs (format 101) which is the only line in file HPK_format_101.txt
         //
         format_101_obs = "";
         
         final String volledig_path_format_101_compressed_file = main.logs_dir + java.io.File.separator + main.FORMAT_101_ROOT_DIR + java.io.File.separator + main.FORMAT_101_TEMP_DIR + java.io.File.separator + "HPK_" + main.FORMAT_101_INPUT_FILE;// NB adding "HPK_" to the input file name is automatically done by the C-code compression functions

         try
         {
            BufferedReader in = new BufferedReader(new FileReader(volledig_path_format_101_compressed_file));
                  
            if ((format_101_obs = in.readLine()) == null)
            {
               String message = "[AWSR] when retrieveing format 101 data empty file: " + volledig_path_format_101_compressed_file;
               main.log_turbowin_system_message(message);
                  
               format_101_obs = "";                // the function which invoke get_format_101_obs_from_file() will check this value
               responseCode = main.INVALID_RESPONSE_FORMAT_101;
            } // else
                     
            in.close();
         } // try
         catch (IOException | HeadlessException e)
         {
            String message = "[AWSR] when retrieving format 101 data error opening file: " + volledig_path_format_101_compressed_file;
            main.log_turbowin_system_message(message);
               
            format_101_obs = "";                   // the function which invoke get_format_101_obs_from_file() will check this value
            responseCode = main.INVALID_RESPONSE_FORMAT_101;
         } // catch         
      

         return responseCode;
               
      } // protected Integer doInBackground() throws Exception

      @Override
      protected void done()
      {
         try
         {
            Integer response_code = get();
            
            // continue if compressing went ok
            //
            if (Objects.equals(response_code, main.OK_RESPONSE_FORMAT_101))       // null save comparising
            {   
               // NB encoding:
               // Translates a string into application/x-www-form-urlencoded format using a specific encoding scheme. This method uses the supplied encoding scheme to obtain 
               // the bytes for unsafe characters.
               // Note: The World Wide Web Consortium Recommendation states that UTF-8 should be used. Not doing so may introduce incompatibilites.
               // 
               // http://stackoverflow.com/questions/10786042/java-url-encoding-of-query-string-parameters:
               // You only need to keep in mind to encode only the individual query string parameter name and/or value, not the entire URL, 
               // for sure not the query string parameter separator character & nor the parameter name-value separator character =.
               // String q = "random word £500 bank $";
               // String url = "http://example.com/query?q=" + URLEncoder.encode(q, "UTF-8");
               //
               // Encode all 'not alloud' ASCII chars if not java.net.URISyntaxException (with index number in the URL string)
               String encoded_format_101_obs = null;               
               try 
               {
                  encoded_format_101_obs = URLEncoder.encode(format_101_obs, "UTF-8");
               } 
               catch (UnsupportedEncodingException ex) 
               {
                  response_code = main.RESPONSE_UNSUPPORTED_ENCODING;
               }
               
               // continue if the encoding was ok
               if (!Objects.equals(response_code, main.RESPONSE_UNSUPPORTED_ENCODING))       // null save comparising
               {   
                  //String url = "http://www.knmi.nl/samenw/turbowin/webstart101/index_webstart_101.php?obs=" + encoded_format_101_obs;  
                  String url = main.upload_URL + "obs=" + encoded_format_101_obs; 
               
                  URL obj = null;
                  try 
                  {
                     obj = new URL(url);
                     HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            
                     // optional (default is GET)
                     con.setRequestMethod("GET");  
                  
                     String message = "[AWSR] sending 'GET' request to URL: " + url;
                     main.log_turbowin_system_message(message);
     
                     response_code = con.getResponseCode();                     // if internet available the default OK_RESPONSE_FORMAT_101 will be overwritten here
                  
                  } // try
                  catch (MalformedURLException ex)
                  {
                     response_code = main.RESPONSE_MALFORMED_URL;
                  }
                  catch (IOException ex) 
                  {
                     response_code = main.RESPONSE_NO_INTERNET;
                  } // catch            
               } // if (!Objects.equals(response_code, main.RESPONSE_UNSUPPORTED_ENCODING))
            } // if (Objects.equals(responseCode, OK_RESPONSE_FORMAT_101)) 
               
               
            // how to continue depends on response_code            
            //
            if (response_code == 200)          // OK
            {
               String message_a = "[AWSR] send obs success"; 
               main.log_turbowin_system_message(message_a);
               main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message_a); // update status field (bottom line main -progress- window) 
               
               // save data to immt log and clear main screen
               //
               // NB in some rare occasions it is possible that within 1 second the obs will be send twice
               //    (somethimes if the wifi/ethernet connection is bridged) and in that case a misformed record will be writkten to the IMMT log
               //    to prevent this check the date/time and position elements (because after the first upload they were reset)
               //
               if ( (!mydatetime.year.equals("")) && (!mydatetime.MM_code.equals("")) && (!mydatetime.YY_code.equals("")) && (!mydatetime.GG_code.equals("")) &&                     
	                 (!myposition.Qc_code.equals("")) && (!myposition.lalala_code.equals("")) && (!myposition.lolololo_code.equals("")) )
               {          
                  //....RS232_make_APR_pressure_IMMT_ready();                        // pressure in IMMT will only be recorded as "mybarometer.PPPP_code" (not mybarometer.pressure_msl_corrected)
                  main.IMMT_log();
               }
               main.Reset_all_meteo_parameters();
            }  
            else // send obs NOT ok or compresing NOT ok or encoding NOT ok
            {
               // NB besides the response code there is also a corresponding response text, but unfortunately with html tags, 
               //    and only with the standard response codes, self defined response codes are not returned?. Not suitable for direct using it into a popup message box
               //    so only using the reponse code and locally (in this program) determined the corresponding return http message text
               
               // NB If no internet connection available the responseCode will be RESPONSE_NO_INTERNET
               //    (if internet avaialble the response code will be overwritten with eg 200)
                     
		         String message_b = "[AWSR] send obs failed; " + main.http_respons_code_to_text(response_code).replace("<br>", " ");
                  
               // file logging
               main.log_turbowin_system_message(message_b);
                  
               // bottom main screen
               main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message_b); 
               
               // so even no success after a retry, but now save the data to IMMT because no further attempts will follow (and the data was not saved before)
               if (retry)
               {
                  // save data to immt log and clear main screen
                  //
                  // NB in some rare occasions it is possible that within 1 second the obs will be send twice
                  //    (somethimes if the wifi/ethernet connection is bridged) and in that case a misformed record will be writkten to the IMMT log
                  //    to prevent this check the date/time and position elements (because after the first upload they were reset)
                  //
                  if ( (!mydatetime.year.equals("")) && (!mydatetime.MM_code.equals("")) && (!mydatetime.YY_code.equals("")) && (!mydatetime.GG_code.equals("")) &&                     
	                    (!myposition.Qc_code.equals("")) && (!myposition.lalala_code.equals("")) && (!myposition.lolololo_code.equals("")) )
                  {
                     //....RS232_make_APR_pressure_IMMT_ready();                     // pressure in IMMT will only be recorded as "mybarometer.PPPP_code" (not mybarometer.pressure_msl_corrected)
                     main.IMMT_log();
                  }   
                  main.Reset_all_meteo_parameters();
               } // if retry
            } // else (send obs NOT ok
            
            // for checking if a upload retry will be necessary [ see unction ...
            APR_server_response_code = response_code;
               
         } // try
         catch (InterruptedException | ExecutionException ex) 
         {   
            String message = "[AWSR] error in Function: RS422_Send_Sensor_Data_To_AWSR(); no internet connection available?; " + ex.toString(); 
            main.log_turbowin_system_message(message);
            main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message);
         }
      } // protected void done()
   }.execute(); // new SwingWorker<Void, Void>()
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private boolean RS422_Set_Date_Time_Obs_And_IMMT_For_AWSR(String record_aws_date, String record_aws_time)
{
   // called from: RS422_Read_And_Send_Sensor_Data_For_AWSR() [main_RS232_RS422.java]
   //
   // NB record_aws_date format: yyyyMMdd
   // NB record_aws_time format: HHmmss
   
   int len = 0;
   boolean record_date_time_ok = true;
   
   
   if ((record_aws_date.length() != 8) || (record_aws_time.length() != 6))
   {
      record_date_time_ok = false;  
      String message = "[AWSR] AWS date/time last saved record format not ok";
      main.log_turbowin_system_message(message);
   }
   
   if (record_date_time_ok)
   {   
      SimpleDateFormat sdf9 = new SimpleDateFormat("yyyyMMddHHmm");                      // HH hour in day (0-23) note there is also hh (then also with am, pm); July = 07 !!
      sdf9.setTimeZone(TimeZone.getTimeZone("UTC"));
   
      String hulp_record_datum_tijd_minuten = record_aws_date + record_aws_time.substring(0, 4);    // eg final result : 202303940927
      Date hulp_record_date = null;
      try 
      {
         hulp_record_date = sdf9.parse(hulp_record_datum_tijd_minuten);
         
         Calendar hulp_cal = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));          // UTC
         hulp_cal.setTime(hulp_record_date);
         hulp_cal.getTime();
         
         int hulp_cal_minute_local = hulp_cal.get(Calendar.MINUTE);
         
         if (hulp_cal_minute_local > 30)
         {
            hulp_cal.add(Calendar.HOUR_OF_DAY, 1);   // add 1 hour
            hulp_cal.getTime();
         }
         
         int hulp_year          = hulp_cal.get(Calendar.YEAR);         // e.g. 2023
         int hulp_month         = hulp_cal.get(Calendar.MONTH);        // The first month of the year is JANUARY which is 0
         int hulp_day_of_month  = hulp_cal.get(Calendar.DAY_OF_MONTH); // The first day of the month has value 1
         int hulp_hour_of_day   = hulp_cal.get(Calendar.HOUR_OF_DAY);
         
         mydatetime.year  = Integer.toString(hulp_year);                // e.g. 2023    
         mydatetime.month = Integer.toString(hulp_month + 1);           // now  mydatetime.month [1 .. 12]                        
         mydatetime.day   = Integer.toString(hulp_day_of_month);        // [1 .. 31]     
         mydatetime.hour  = Integer.toString(hulp_hour_of_day);         // [0 .. 23]       

         // determine code figures for month (for obs and IMMT)
         //
         mydatetime.MM_code = mydatetime.month;                                 // mydatetime.month [1 .. 12]            

         len = 2;                                                               // MM_code always 2 characters width e.g. 03
         if (mydatetime.MM_code.length() < len)                                 // pad on left with zeros
         {
            mydatetime.MM_code = "0000000000".substring(0, len - mydatetime.MM_code.length()) + mydatetime.MM_code;
         }
         
         // determine code figures for day of the month (for obs and IMMT)
         //
         mydatetime.YY_code = mydatetime.day;                                   // mydatetime.day e.g. 1,2,11,23,29

         len = 2;                                                               // YY_code always 2 characters width e.g. 03
         if (mydatetime.YY_code.length() < len)                                 // pad on left with zeros
         {
            mydatetime.YY_code = "0000000000".substring(0, len - mydatetime.YY_code.length()) + mydatetime.YY_code;
         }

         // determine code figures for hour of day (for obs and IMMT)
         //
         mydatetime.GG_code = mydatetime.hour;                                  // mydatetime.hour e.g. 0,1,2,11,23

         len = 2;                                                               // GG_code always 2 characters width e.g. 03
         if (mydatetime.GG_code.length() < len)                                 // pad on left with zeros
         {
            mydatetime.GG_code = "0000000000".substring(0, len - mydatetime.GG_code.length()) + mydatetime.GG_code;
         }         
         
      } // try
      catch (ParseException ex) 
      {
         record_date_time_ok = false;
         String message = "[AWSR] AWS date/time last saved record format not ok (" + ex + ")";
         main.log_turbowin_system_message(message);
      }
   } //  if (record_date_time_ok)
   
   
   return record_date_time_ok;
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void RS422_Read_And_Send_Sensor_Data_For_AWSR(final boolean retry)
{
   // check the presence of the various meteo parameters in the data received from an AWS (data stored by TurboWin+ in sensor data files)
   
   // called from: Function RS422_V3_write_sensor_data_to_file() [file main_RS232_RS422.java]
   //
   
   
   // 3201312130055180596059842742000955055999202112502009000              44PDZS   NL 314    0075300722           A411111111119999111111 06907106000002080151111 11100764 8951932 BLOKZYL RR
   // 32018061308745900210                                  0              44TESTNL UK 314        2   35           A599999999999999191191                    9999 9990530001234567 
   // 32018061312745900210                                  0              44TESTNL US 314        2   35           A599999999999999191191              12839999 9110530001234567 
   // 32018061313745900210                                  0              44TESTNL US 314        2   35           A599999999999999191191              12839999 9110530001234567 
   // 32018061314745900210                                  0              44TESTNL US 314        2   35           A599999999999999191191              12839999 9110530001234567 

   
   // initialisation
   RS422_initialise_AWS_Sensor_Data_For_AWSR();
   
  
   new SwingWorker<String, Void>()
   {
      @Override
      protected String doInBackground() throws Exception
      {
         main.obs_file_datum_tijd = new GregorianCalendar();
         main.obs_file_datum_tijd.add(Calendar.MINUTE, -2);     // of is -1 ook goed????? : to be sure there was all time that it was written to the file
         File sensor_data_file;
         String record         = null;
         String last_record    = null;

         // determine sensor data file name
         String sensor_data_file_naam_datum_tijd_deel = main.sdf3.format(main.obs_file_datum_tijd.getTime()); // e.g. 2013020308
         String sensor_data_file_name = "sensor_data_" + sensor_data_file_naam_datum_tijd_deel + ".txt";

         // first check if there is a sensor data file present (and not empty)
         String volledig_path_sensor_data = main.logs_dir + java.io.File.separator + sensor_data_file_name;
         //System.out.println("+++ te openen file voor obs: "+ volledig_path_sensor_data);

         sensor_data_file = new File(volledig_path_sensor_data);
         if (sensor_data_file.exists() && sensor_data_file.length() > 0)     // length() in bytes
         {
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path_sensor_data));

               try
               {
                  // initialisation
                  record         = "";
                  last_record    = "";

                  while ((record = in.readLine()) != null)
                  {
                     if (record.length() > 43)               // arbitrary but > $PEUMB + yyyymmdduu + TOTAL_NUMBER_RECORD_COMMAS
                     {
                        if ( (record.substring(0, 6).equals("$PEUMB")) && (record.indexOf(sensor_data_file_naam_datum_tijd_deel) != -1) )
                        {
                           last_record = record;
                        }
                     } // if (record.length() > 43)  
                  } // while ((record = in.readLine()) != null)
               } // try
               finally
               {
                  in.close();
               }
            } // try
            catch (IOException ex) 
            {  
               last_record = "";
            }
         } // if (sensor_data_file.exists() && sensor_data_file.length() > 0)
         else // no sensor_data_file present
         {
            last_record = null;
         }
           
         return last_record;
            
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         boolean doorgaan_in_record = true;
         int number_read_commas;
         int pos;
         String record_aws_date = ""; // will be passed to function: RS422_Check_Date_Time_Last_Saved_Record_For_AWSR()
         String record_aws_time = ""; // will be passed to function: RS422_Check_Date_Time_Last_Saved_Record_For_AWSR()
         boolean set_date_time_record_ok = false;
         boolean check_date_time_record_ok = false;

            
         String laatste_record = null;
         try 
         {
            laatste_record = get();
         } 
         catch (InterruptedException | ExecutionException ex) 
         {
            String message = "[AWSR] error retrieving AWS data from sensor data file; " + ex.toString();
            main.log_turbowin_system_message(message);
         }
            
         if (laatste_record == null)
         {
            String message = "[AWSR] no sensor data file exists or file empty";
            main.log_turbowin_system_message(message);    
            doorgaan_in_record = false;
         }
         else if (laatste_record.equals(""))
         {
            String message = "[AWSR] sensor data file found but no records found or misformed records";
            main.log_turbowin_system_message(message); 
            doorgaan_in_record = false;
         }
            
            
         if (doorgaan_in_record == true && laatste_record != null) 
         {
            // Check the number of comma's in the record that must exactely match the fixed expected number (TOTAL_NUMBER_RECORD_COMMAS)
            number_read_commas = 0;
            pos = -1;

            do 
            {
               pos = laatste_record.indexOf(",", pos + 1);
               if (pos > 0) // "," found
               {
                  number_read_commas++;
               }
            } while (pos > 0);

            //System.out.println("+++ record = " + laatste_record);
            //System.out.println("+++ number read commas = " + number_read_commas);
            if (number_read_commas != main.TOTAL_NUMBER_RECORD_COMMAS) 
            {
               doorgaan_in_record = false;
            }
         } // if (doorgaan_in_record == true)

         if (doorgaan_in_record == true && laatste_record != null) 
         {
            number_read_commas = 0;
            pos = -1;

            do 
            {
               // NB eg:
               // $PEUMB,20120715,090227,52.405, 5.338,192.0,5.2,184.0, 997.4,998.6,2.3,2,16.0,87.5, ,9.4,98.5,12.4,55.2,15.2,44.0,24.2,18.8,-38,,,201207150902
               pos = laatste_record.indexOf(",", pos + 1);    // searching "," from position "pos + 1"
               if (pos > 0) // "," found
               {
                  number_read_commas++;

                  //////////////////// Date /////////////////
                  //
                  // format 101:
                  //   mydatetime.year
                  //   mydatetime.MM_code
                  //   mydatetime.day 
                  //
                  // extra for IMMT:
                  //   mydatetime.YY_code
                  //
                  if (number_read_commas == main.DATE_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between conmmas at least 1 char
                        {
                           record_aws_date = laatste_record.substring(pos + 1, pos2);                                                  // YYYYMMDD
                           //mydatetime.day = record_aws_date.substring(record_aws_date.length() - 2);                                   // DD eg "12" or "02"
                           //mydatetime.MM_code = record_aws_date.substring(record_aws_date.length() - 4, record_aws_date.length() - 2); // MM
                           //mydatetime.year = record_aws_date.substring(0, record_aws_date.length() - 4);                               // YYYY 
                           //mydatetime.YY_code = mydatetime.day; // in case this AWS record it is always the same! (not in case manual input)
                        } // if (pos2 - pos >= 2)
                     }
                  } // if (number_read_commas == etc.     
                  
                  
                  ////////////////// Time /////////////////
                  //
                  // see also:  Function RS422_Set_Date_Time_Obs_And_IMMT_For_AWSR()
                  //            Function RS422_Check_Date_Time_Last_Saved_Record_For_AWSR()
                  //            (called from within this function just for sending)
                  //
                  //
                  // format 101: see: 'see also' above
                  //
                  // extra for IMMT: see:  'see also' above
                  //
                  if (number_read_commas == main.TIME_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between conmmas at least 1 char
                        {
                           record_aws_time = laatste_record.substring(pos + 1, pos2);
                           //if (record_aws_time.length() == 6) // HHMMSS
                           //{
                           //   mydatetime.hour = record_aws_time.substring(0, 2);      // HH from HHMMSS
                           //   mydatetime.GG_code = mydatetime.hour; // in case this AWS record it is always the same! (not in case manual input)
                           //}
                        } // if (pos2 - pos >= 2)
                     }
                  } // if (number_read_commas == etc.  
                  

                  
                  ////////////////// Latitude [SMD output format sDD.ddd; s = "-" if South; + always omitted] //////////////////
                  //
                  // for format101:
                  //   myposition.int_latitude_degrees
                  //   myposition.int_latitude_minutes
                  //   myposition.latitude_hemisphere
                  //
                  // extra for IMMT:
                  //   (myposition.Qc_code) 
                  //   myposition.lalala_code
                  //
                  if (number_read_commas == main.LATITUDE_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between commas at least 1 char
                        {
                           String record_aws_latitude = laatste_record.substring(pos + 1, pos2);

                           if (record_aws_latitude.indexOf("-") != -1) // found "-" in string
                           {
                              myposition.latitude_hemisphere = myposition.HEMISPHERE_SOUTH; //"South";
                              myposition.latitude_degrees = record_aws_latitude.substring(1, record_aws_latitude.indexOf("."));   // -12.344 -> 12
                           } 
                           else 
                           {
                              myposition.latitude_hemisphere = myposition.HEMISPHERE_NORTH; //"North";
                              myposition.latitude_degrees = record_aws_latitude.substring(0, record_aws_latitude.indexOf("."));   // 12.344 -> 12
                           }
                           
                           // String latitude convert to integer
                           try 
                           {
                              myposition.int_latitude_degrees = Integer.parseInt(myposition.latitude_degrees.trim());
                           }
                           catch (NumberFormatException e){ }   

                           double decimal_degrees_lat = Double.parseDouble(record_aws_latitude);
                           myposition.int_latitude_minutes = (int) Math.round((decimal_degrees_lat - Math.floor(decimal_degrees_lat)) * 60);
                           
                           
                           // for IMMT
                           //
                           //
                           // NB myposition.Qc_code will be determined in the Longitude section (see below)
                           
                           int int_latitude_minutes_6 = myposition.int_latitude_minutes / 6;               // devide the minutes by six and disregard the remainder
                           String latitude_minutes_6 = Integer.toString(int_latitude_minutes_6);           // convert to String 
         
                           myposition.lalala_code = myposition.latitude_degrees.trim().replaceFirst("^0+(?!$)", "") + latitude_minutes_6;

                           int len = 3;                                                                    // always 3 chars
                           if (myposition.lalala_code.length() < len) 
                           { 
                              // pad on left with zeros
                              myposition.lalala_code = "0000000000".substring(0, len - myposition.lalala_code.length()) + myposition.lalala_code;
                           }                           
                           
                        } // if (pos2 - pos >= 2)
                     } // if (laatste_record.length() etc.
                  } // if (number_read_commas == etc.    
                  
                  
                  ////////////// Longitude [SMD output format sDDD.ddd; s = "-" if West; + always omitted] ////////////////
                  //
                  // for format101:
                  //   myposition.int_longitude_degrees
                  //   myposition.int_longitude_minutes
                  //   myposition.longitude_hemisphere
                  //
                  // extra for IMMT:
                  //   myposition.Qc_code
                  //   myposition.lolololo_code
                  //
                  if (number_read_commas == main.LONGITUDE_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between commas at least 1 char
                        {
                           String record_aws_longitude = laatste_record.substring(pos + 1, pos2);

                           if (record_aws_longitude.indexOf("-") != -1) // found "-" in string
                           {
                              myposition.longitude_hemisphere = myposition.HEMISPHERE_WEST; //"West";
                              myposition.longitude_degrees = record_aws_longitude.substring(1, record_aws_longitude.indexOf("."));   // -12.344 -> 12
                           } 
                           else 
                           {
                              myposition.longitude_hemisphere = myposition.HEMISPHERE_EAST; //"East";
                              myposition.longitude_degrees = record_aws_longitude.substring(0, record_aws_longitude.indexOf("."));   // 12.344 -> 12
                           }
                           
                           // String longitude omzetten naar integer
                           try 
                           {
                              myposition.int_longitude_degrees = Integer.parseInt(myposition.longitude_degrees.trim());
                           }
                           catch (NumberFormatException e){ }

                           double decimal_degrees_lon = Math.abs(Double.parseDouble(record_aws_longitude));
                           myposition.int_longitude_minutes = (int) Math.round((decimal_degrees_lon - Math.floor(decimal_degrees_lon)) * 60);
                           
                           // for IMMT
                           //
                           int int_longitude_minutes_6 = myposition.int_longitude_minutes / 6;               // devide the minutes by six and disregard the remainder
                           String longitude_minutes_6 = Integer.toString(int_longitude_minutes_6);           // convert to String 
         
                           myposition.lolololo_code = myposition.longitude_degrees.trim().replaceFirst("^0+(?!$)", "") + longitude_minutes_6;
         
                           int len = 4;                                                                          // always 4 chars
                           if (myposition.lolololo_code.length() < len)                                      // pad on left with zeros
                           {
                              myposition.lolololo_code = "0000000000".substring(0, len - myposition.lolololo_code.length()) + myposition.lolololo_code;
                           }    
                           
                           // myposition.Qc_code (must come after latitude_hemisphere and longitude_hemisphere is determined)
                           if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
                           {
                              myposition.Qc_code = "1";
                           }
                           else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true))
                           {
                              myposition.Qc_code = "3";
                           }
                           else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
                           {
                              myposition.Qc_code = "5";
                           }
                           else if ((myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true))
                           {
                              myposition.Qc_code = "7";
                           }

                        } // if (pos2 - pos >= 2)
                     }  
                  } // if (number_read_commas == etc.                                     

                  
                  //////////////// Course Over Ground (COG) [SMD output format CCC.c] ////////////////
                  //
                  // for format101:
                  //   myposition.Ds_code
                  //
                  if (number_read_commas == main.COG_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between commas at least 1 char
                        {
                           String record_aws_COG = laatste_record.substring(pos + 1, pos2);

                           int int_COG = (int) Math.round(Double.parseDouble(record_aws_COG));

                           if (int_COG == 0) 
                           {
                              int_COG = 360;
                           }

                           
                           // determine code figure course
                           //
                           // NB "stationary" not possible with AWS COG reporting (so no myposition.Ds_code = "0";)
                           if (int_COG >= 23 && int_COG <= 67)                                              // NE
                           {
                              myposition.Ds_code = "1";
                           }
                           else if (int_COG >= 68 && int_COG <= 112)                                        // E 
                           {
                              myposition.Ds_code = "2";
                           }
                           else if (int_COG >= 113 && int_COG <= 157)                                       // SE
                           {
                              myposition.Ds_code = "3";
                           }
                           else if (int_COG >= 158 && int_COG <= 202)                                       // S
                           {
                              myposition.Ds_code = "4";
                           }
                           else if (int_COG >= 203 && int_COG <= 247)                                       // SW
                           {
                              myposition.Ds_code = "5";
                           }
                           else if (int_COG >= 248 && int_COG <= 292)                                       // W
                           {
                              myposition.Ds_code = "6";
                           }
                           else if (int_COG >= 293 && int_COG <= 337)                                       // NW
                           {
                              myposition.Ds_code = "7";
                           }
                           else if ((int_COG >= 338 && int_COG <= 360) || (int_COG >= 0 && int_COG <= 022)) // N
                           {
                              myposition.Ds_code = "8";
                           }
                           else
                           {
                              myposition.Ds_code = "9";                                                    // no information (could be Ds_code = "/" also, because two options to code Ds undefined)
                           }
                           
                           // IMMT
                           //
                           // NB there is no 'stationary' in case of COG reporting in SMD output format
                           if (int_COG <= 9)
                           {
                              mywind.COG_code = "00" +  Integer.toString(int_COG);         // 3 char
                           }
                           else if (int_COG >= 10 && int_COG <= 99)
                           {
                              mywind.COG_code = "0" +  Integer.toString(int_COG);          // 3 char
                           }
                           else if (int_COG >= 100 && int_COG <= 360)
                           {
                              mywind.COG_code = Integer.toString(int_COG);                 // 3 char
                           }
                           else
                           {
                              mywind.COG_code = "///";                                     // 3 char
                           }
                        } // if (pos2 - pos >= 2)
                     } // if (record.length() > number_read_commas + 12)
                  } // if (number_read_commas == etc.
                  
                  
                  ///////////////// Speed Over Ground (SOG) [SMD output format SS.s] ////////////////
                  //
                  // for format101:
                  //   myposition.vs_code
                  //
                  if (number_read_commas == main.SOG_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between conmmas at least 1 char
                        {
                           String record_aws_SOG = laatste_record.substring(pos + 1, pos2);  // in m/s
                           double double_SOG = Double.parseDouble(record_aws_SOG);           // in m/s
                           int int_SOG = (int) Math.round(double_SOG * M_S_TO_KNOTS);        // in knots rounded

                           // determine code figure speed
                           if (int_SOG < 1)
                           {
                              myposition.vs_code = "0";
                           }
                           else if (int_SOG >= 1 && int_SOG <= 5)
                           {
                              myposition.vs_code = "1";
                           }
                           else if (int_SOG >= 6 && int_SOG <= 10)
                           {
                              myposition.vs_code = "2";
                           }
                           else if (int_SOG >= 11 && int_SOG <= 15)
                           {
                              myposition.vs_code = "3";
                           }
                           else if (int_SOG >= 16 && int_SOG <= 20)
                           {
                              myposition.vs_code = "4";
                           }
                           else if (int_SOG >= 21 && int_SOG <= 25)
                           {
                              myposition.vs_code = "5";
                           }
                           else if (int_SOG >= 26 && int_SOG <= 30)
                           {
                              myposition.vs_code = "6";
                           }
                           else if (int_SOG >= 31 && int_SOG <= 35)
                           {
                              myposition.vs_code = "7";
                           }
                           else if (int_SOG >= 36 && int_SOG <= 40)
                           {
                              myposition.vs_code = "8";
                           }
                           else if (int_SOG > 40)
                           {
                              myposition.vs_code = "9";
                           }
                           else
                           {
                              myposition.vs_code = "/";
                           }
                           
                           // IMMT
                           //
                           if (int_SOG <= 9)
                           {
                              mywind.SOG_code = "0" + Integer.toString(int_SOG);           // 2 char
                           }
                           else if (int_SOG >= 10 && int_SOG <= 99)
                           {
                              mywind.SOG_code = Integer.toString(int_SOG);                 // 2 char
                           }
                           else
                           {
                             mywind.SOG_code = "//";                                       // 2 char
                           }
                           
                        } // if (pos2 - pos >= 2)
                     } // if (record.length() > number_read_commas + 12)
                  } // if (number_read_commas == etc.
                  
                  
                  ///////////////// max height deck cargo (SLL only for IMMT) ///////////////////
                  //
                  //
                  //
                  //
                  int int_max_height_deck_cargo = main.INVALID;
                  try 
                  {
                     int_max_height_deck_cargo = Integer.parseInt(main.max_height_deck_cargo.trim());
                  }
                  catch (NumberFormatException e)
                  {
                     int_max_height_deck_cargo = main.INVALID;
                  }
                  
                  if (int_max_height_deck_cargo <= 9)
                  {
                     mywind.SLL_code = "0" + Integer.toString(int_max_height_deck_cargo);      // 2 char
                  }
                  else if (int_max_height_deck_cargo >= 10 && int_max_height_deck_cargo <= 99)
                  {
                     mywind.SLL_code = Integer.toString(int_max_height_deck_cargo);            // 2 char
                  }
                  else
                  {
                     mywind.SLL_code = "//";                                                   // 2 char
                  }
                  
                  
       
                  /////////////// departure of reference level (sl and hh, format 101 and IMMT) ///////////////
                  //
                  //
                  //
                  //
                  //
                  
                  //int int_difference_sll_wl = main.INVALID;  // local var (NB there is also a global var mywind.int_difference_sll_wl)
                  //try 
                  //{
                  //   int_difference_sll_wl = Integer.parseInt(main.diff_sll_wl.trim());
                  //}
                  //catch (NumberFormatException e)
                  //{
                  //   int_difference_sll_wl = main.INVALID;
                  //}
                  //
                  //if (int_difference_sll_wl >= -10 && int_difference_sll_wl < 0)
                  //{
                  //   mywind.sl_code = "1";                   // negative                          // 1 char
                  //
                  //   if (int_difference_sll_wl <= -10)
                  //   {
                  //      mywind.hh_code = Integer.toString(Math.abs(int_difference_sll_wl));       // 2 char
                  //   }
                  //   else // range -1 - -9
                  //   {
                  //      mywind.hh_code = "0" + Integer.toString(Math.abs(int_difference_sll_wl)); // 2 char
                  //   }
                  //} // if (int_difference_sll_wl >= -10 && int_difference_sll_wl < 0)
                  //else if (int_difference_sll_wl >= 0 && int_difference_sll_wl < 50)
                  //{
                  //   mywind.sl_code = "0";                   // positive                          // 1 char
                  //
                  //   if (int_difference_sll_wl >= 0 && int_difference_sll_wl <= 9)
                  //   {
                  //      mywind.hh_code = "0" + Integer.toString(int_difference_sll_wl);           // 2 char
                  //   }
                  //   else // range 10 - 49
                  //   {
                  //      mywind.hh_code = Integer.toString(int_difference_sll_wl);                 // 2 char
                  //   }
                  //} // else if (int_difference_sll_wl > 0 && int_difference_sll_wl < 50)
                  //else
                  //{
                  //   mywind.sl_code = "/";                                                        // 1 char
                  //   mywind.hh_code = "//";                                                       // 2 char
                  //} // else

                  // NB
                  // because in a automated situation (AWSR) most probable the diff. sll-wl will never be verified/updated (on the wind input page)
                  // the sl_code(IMMT) and hh_code (IMMT) and mywind.int_difference_sll_wl (format101) will be set to 'not preset'
                  
                  // forma101:
                  mywind.int_difference_sll_wl = main.INVALID;
                  
                  // IMMT
                  // 
                  mywind.sl_code = "/";                                                        // 1 char
                  mywind.hh_code = "//";                                                       // 2 char
                  
                  
                  
                  //////////////////// True heading [SMD output format HHH.h] ///////////////////
                  //
                  // for format101:
                  //   mywind.ship_heading
                  //
                  if (number_read_commas == main.HEADING_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between commas at least 1 char
                        {
                           String record_aws_ship_heading = laatste_record.substring(pos + 1, pos2);
                           double double_ship_heading = Double.parseDouble(record_aws_ship_heading);

                           int int_ship_heading = (int) Math.round(double_ship_heading);

                           if (int_ship_heading == 0) 
                           {
                              int_ship_heading = 360;
                           }

                           mywind.ship_heading = Integer.toString(int_ship_heading);
                           
                           // IMMT
                           // 
                           if (int_ship_heading <= 9)
                           {
                              mywind.HDG_code = "00" + Integer.toString(int_ship_heading);               // 3 char
                           }
                           else if (int_ship_heading >= 10 && int_ship_heading <= 99)
                           {
                              mywind.HDG_code = "0" + Integer.toString(int_ship_heading);                // 3 char
                           }
                           else if (int_ship_heading >= 100 && int_ship_heading <= 360)
                           {
                              mywind.HDG_code = Integer.toString(int_ship_heading);                       // 3 char
                           }
                           else
                           {
                              mywind.HDG_code = "///";                                                    // 3 char
                           }

                        } // if (pos2 - pos >= 2)
                     } // if (record.length() > number_read_commas + 12)
                  } // if (number_read_commas == etc.

                  
                  /////////////////////// air pressure at sensor height (read) [SMD output format PPPP.p] ////////////////////
                  //
                  // for format101:
                  //   (main.pressure_reading_msl_yes_no)
                  //   mybarometer.pressure_reading_corrected
                  //
                  //
                  //
                  if (number_read_commas == main.PRESSURE_SENSOR_HEIGHT_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between conmmas at least 1 char
                        {
                           mybarometer.pressure_reading_corrected = laatste_record.substring(pos + 1, pos2);

                           // by default for pressure value obtained from AWS!
                           //mybarometer.pressure_reading = mybarometer.pressure_reading_corrected;

                           // NB ic = 0.0 by default for pressure value obtained from AWS!
                           
                           //main.barometer_instrument_correction = "0.0";
                        } // if (pos2 - pos >= 2)
                     } // if (record.length() > number_read_commas + 12)
                  } // if (number_read_commas == etc.

                  
                  /////////////////// air pressure MSL [SMD output format PPPP.p] /////////////////////
                  //
                  // for format101:
                  //   mybarometer.pressure_msl_corrected
                  //
                  // IMMT:
                  //   mybarometer.PPPP_code
                  //
                  if (number_read_commas == main.PRESSURE_MSL_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between conmmas at least 1 char
                        {
                           double double_pressure_msl_corrected = main.INVALID;
                           
                           mybarometer.pressure_msl_corrected = laatste_record.substring(pos + 1, pos2);
                           
                           // NB ic = 0.0 by default for pressure value obtained from AWS!
                           
                           // IMMT
                           try
                           {
                              double_pressure_msl_corrected = Double.parseDouble(mybarometer.pressure_msl_corrected.trim());
                           }
                           catch (NumberFormatException e) 
                           { 
                              double_pressure_msl_corrected = main.INVALID;
                           }
                           
                           if (double_pressure_msl_corrected != main.INVALID)   
                           {
                              int num_PPPP_code = (int)Math.floor(double_pressure_msl_corrected * 10 + 0.5);

                              // thousands of the air pressure value not in IMMT air pressure code
                              if (num_PPPP_code >= 10000)
                              {
                                 num_PPPP_code -= 10000;
                              }
             
                              mybarometer.PPPP_code = Integer.toString(num_PPPP_code);     // convert to string

                              // NB PPPP_code always 4 characters width e.g. 0007 (accomplish via construction below)
                              int len = 4;
                              if (mybarometer.PPPP_code.length() < len)                                            // pad on left with zeros
                              {
                                 mybarometer.PPPP_code = "0000000000".substring(0, len - mybarometer.PPPP_code.length()) + mybarometer.PPPP_code;
                              }
                           } // if (double_pressure_msl != INVALID)
                           
                        } // if (pos2 - pos >= 2)
                     } // if (record.length() > number_read_commas + 12)
                  } // if (number_read_commas == etc.

                   
                  ///////////////// air pressure tendency over three three hours [SMD output format sPP.p] ///////////////////////
                  //
                  // for format101:
                  //   mybarograph.pressure_amount_tendency
                  //
                  if (number_read_commas == main.PRESSURE_TENDENCY_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between conmmas at least 1 char
                        {
                           double double_pressure_amount_tendency = main.INVALID;
                           
                           mybarograph.pressure_amount_tendency = laatste_record.substring(pos + 1, pos2);

                           // IMMT
                           //
                           try
                           {
                              double_pressure_amount_tendency = Double.parseDouble(mybarograph.pressure_amount_tendency.trim());
                           }
                           catch (NumberFormatException e) 
                           { 
                              double_pressure_amount_tendency = main.INVALID;
                           }
                           
                           
                           if (double_pressure_amount_tendency != main.INVALID)
                           {
                              int num_ppp_code = (int)Math.floor(double_pressure_amount_tendency * 10 + 0.5);
             
                               mybarograph.ppp_code = Integer.toString(num_ppp_code);     // convert to string
             
                              // NB ppp_code always 3 characters width e.g. 017 (accomplish via construction below)
                              int len = 3;
                              if ( mybarograph.ppp_code.length() < len)                                            // pad on left with zeros
                              {   
                                 mybarograph.ppp_code = "0000000000".substring(0, len -  mybarograph.ppp_code.length()) +  mybarograph.ppp_code;
                              }  
                           } // if (double_pressure_amount_tendency != INVALID)
                        } // if (pos2 - pos >= 2)
                     } // if (record.length() > number_read_commas + 12)
                  } // if (number_read_commas == etc.                              


                  ////////////////// air pressure characteristic [SMD output format A] ///////////////
                  //
                  // for format101:
                  //   mybarograph.a_code
                  //
                  //
                  if (number_read_commas == main.PRESSURE_CHARACTERISTIC_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between conmmas at least 1 char
                        {
                           mybarograph.a_code = laatste_record.substring(pos + 1, pos2);
                        } // if (pos2 - pos >= 2)
                     } // if (record.length() > number_read_commas + 12)
                  } // if (number_read_commas == etc.    
                
                  
                  //////////////////////// air temp [SMD output format sTA.a] /////////////
                  //
                  // for format101:
                  //   mytemp.air_temp
                  //
                  // extra for IMMT:
                  //   mytemp.sn_TTT_code (1 char)
                  //   mytemp.TTT_code  (3 chr)
                  //
                  if (number_read_commas == main.AIR_TEMP_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between conmmas at least 1 char
                        {
                           mytemp.air_temp = laatste_record.substring(pos + 1, pos2);
                           
                           // for IMMT
                           //
                           double double_air_temp = main.INVALID;
                           try 
                           {
                              double_air_temp = Double.parseDouble(mytemp.air_temp.trim());
                           }
                           catch (NumberFormatException e)
                           {
                              double_air_temp = main.INVALID;
                           }
                        
                           if (double_air_temp != main.INVALID)
                           {
                              // Sn of TTT (only for IMMT)
                              if (double_air_temp < 0)
                              {
                                 mytemp.sn_TTT_code = "1";
                              }
                              else
                              {
                                 mytemp.sn_TTT_code = "0";
                              }
           
                              // TTT (only for IMMT)
                              int num_TTT_code = (int)Math.floor(Math.abs(double_air_temp) * 10 + 0.5);
                              mytemp.TTT_code = Integer.toString(num_TTT_code);     // convert to string      // or (alternative): Hw_code = String.valueOf(num_Hw_code);

                              // NB TTT_code always 3 characters width e.g. 027 (accomplished via construction below)
                              int len = 3;
                              if (mytemp.TTT_code.length() < len)                                            // pad on left with zeros
                              {   
                                 mytemp.TTT_code = "0000000000".substring(0, len - mytemp.TTT_code.length()) + mytemp.TTT_code;
                              }   
  
                              //JOptionPane.showMessageDialog(null, TTT_code, main.APPLICATION_NAME + " TTT code", JOptionPane.WARNING_MESSAGE);
                           } // if (float_air_temp != INVALID)     
                        } // if (pos2 - pos >= 2)
                     } // if (laatste_record.length() > pos + 1 + 12)
                  } // if (number_read_commas == etc.
                  
                  
                  //////////////////////// relative humidity [SMD output format UUU.u] ///////////////
                  //
                  // for format101:
                  //   mytemp.double_rv
                  //
                  // fot IMMT:
                  //   RH (4 char)
                  //   RHi (1 char)
                  //
                  if (number_read_commas == main.HUMIDITY_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between conmmas at least 1 char (e.g. "87.5")
                        {
                           mytemp.double_rv = Double.parseDouble(laatste_record.substring(pos + 1, pos2)) / 100;
                           // NB "/100" for consistency with not AWS computed rh (see also main.temperatures_fields_update() and mytemp.OK_button_actioPerformed())

                           
                           // IMMT
                           //
                           // NB computing RH and RHi will be done in IMMT_log() [main.java]
                           
                        } // if (pos2 - pos >= 2)
                     } // if (laatste_record.length() > pos + 1 + 12)
                  } // if (number_read_commas == etc.
                     

                  /////////////////////////// SST [SMD output format sTW.w] ////////////////////
                  //
                  // for format101:
                  //    mytemp.sea_water_temp
                  //
                  // for IMMT:
                  //    ss_TsTsTs_code (1 char)
                  //    TstsTs_code (3 char)
                  //    immt_ss_indicator (1 char)
                  //
                  if (number_read_commas == main.SST_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between conmmas at least 1 char (e.g. 15.4)
                        {
                           mytemp.sea_water_temp = laatste_record.substring(pos + 1, pos2);
                           
                           // IMMT
                           //
                           double double_sea_water_temp = main.INVALID;
                           try 
                           {
                              double_sea_water_temp = Double.parseDouble(mytemp.sea_water_temp.trim());
                           }
                           catch (NumberFormatException e)
                           {
                              double_sea_water_temp = main.INVALID;
                           }
                           
                           
                           if (double_sea_water_temp != main.INVALID)
                           {
                              // ss of TsTsTs and sst indicator (IMMT only)
                              if (double_sea_water_temp < 0.0)
                              {
                                 if (main.sst_exposure.equals(main.INTAKE) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "1";                        // for obs and indirect also for immt (Sn calculation)
                                    mytemp.immt_sst_indicator = "1";                    // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.BUCKET) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "3";                         // for obs and indirect also for immt
                                    mytemp.immt_sst_indicator = "0";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.HULL_CONTACT_SENSOR) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "5";                         // for obs and indirect also for immt
                                    mytemp.immt_sst_indicator = "3";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.TRAILING_THERMISTOR) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "7";                         // trailing thermistor not for obs but for IMMT
                                    mytemp.immt_sst_indicator = "2";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.THROUGH_HULL_SENSOR) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "7";                         // through hull sensor not for obs but for IMMT
                                    mytemp.immt_sst_indicator = "4";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.RADIATION_THERMOMETER) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "7";                         // radiation thermometer not for obs but for IMMT
                                    mytemp.immt_sst_indicator = "5";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.BAIT_TANKS_THERMOMETER) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "7";                         // bait tanks thermometer not for obs but for IMMT
                                    mytemp.immt_sst_indicator = "6";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.OTHER) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "7";                         // for obs and indirect also for immt
                                    mytemp.immt_sst_indicator = "7";                     // only for immt log not for operational obs
                                 }
                              } // if (double_sea_water_temp < 0.0
                              else // sst >= 0
                              {
                                 if (main.sst_exposure.equals(main.INTAKE) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "0";
                                    mytemp.immt_sst_indicator = "1";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.BUCKET) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "2";
                                    mytemp.immt_sst_indicator = "0";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.HULL_CONTACT_SENSOR) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "4";
                                    mytemp.immt_sst_indicator = "3";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.TRAILING_THERMISTOR) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "6";                         
                                    mytemp.immt_sst_indicator = "2";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.THROUGH_HULL_SENSOR) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "6";                         
                                    mytemp.immt_sst_indicator = "4";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.RADIATION_THERMOMETER) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "6";                         
                                    mytemp.immt_sst_indicator = "5";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.BAIT_TANKS_THERMOMETER) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "6";                         
                                    mytemp.immt_sst_indicator = "6";                     // only for immt log not for operational obs
                                 }
                                 else if (main.sst_exposure.equals(main.OTHER) == true)
                                 {
                                    mytemp.ss_TsTsTs_code = "6";
                                    mytemp.immt_sst_indicator = "7";                     // only for immt log not for operational obs
                                 }
                              } // else (sst >= 0)
                 
                              // TsTsTs (IMMT only)
                              int num_TsTsTs_code = (int)Math.floor(Math.abs(double_sea_water_temp) * 10 + 0.5);
                              mytemp.TsTsTs_code = Integer.toString(num_TsTsTs_code);     // convert to string      // or (alternative): Hw_code = String.valueOf(num_Hw_code);
              
                              // NB TsTsTs_code always 3 characters width e.g. 127 (accomplish via construction below)
                              int len = 3;
                              if ( mytemp.TsTsTs_code.length() < len)   
                              {
                                 // pad on left with zeros
                                 mytemp.TsTsTs_code = "0000000000".substring(0, len - mytemp.TsTsTs_code.length()) +  mytemp.TsTsTs_code;
                              }
                           } // if (float_sea_water != INVALID)   
                        } // if (pos2 - pos >= 2)
                     } // if (laatste_record.length() > pos + 1 + 12)
                  } // if (number_read_commas == etc.
                     

                  /////////////////////// relative wind speed [SMD output format WS.r] //////////////////
                  //
                  // for format101:
                  //   mywind.RWS_code
                  //   (main.wind_units)
                  //
                  if (number_read_commas == main.RELATIVE_WIND_SPEED_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between commas at least 1 char
                        {
                           String record_aws_relative_wind_speed = laatste_record.substring(pos + 1, pos2);
                           Double double_relative_wind_speed = Double.parseDouble(record_aws_relative_wind_speed);

                           // round to integer (NB already in m/s)

                           // NB in this stage always m/s (by default in case of AWS SMD ouput data)
                           mywind.int_relative_wind_speed = (int) Math.round(double_relative_wind_speed);
                           
                           if (mywind.int_relative_wind_speed <= 9)
                           {
                              mywind.RWS_code = "00" + Integer.toString(mywind.int_relative_wind_speed);              // 3 char
                           }
                           else if (mywind.int_relative_wind_speed >= 10 && mywind.int_relative_wind_speed <= 99)
                           {
                              mywind.RWS_code = "0" + Integer.toString(mywind.int_relative_wind_speed);               // 3 char
                           }
                           else if (mywind.int_relative_wind_speed >= 100 && mywind.int_relative_wind_speed <= 300)
                           {
                              mywind.RWS_code = Integer.toString(mywind.int_relative_wind_speed);                     // 3 char // so now in m/s or knots depending var. "main.wind_units"
                           }
                           else
                           {
                              mywind.RWS_code = "///";                                                                // 3 char
                           }
                        } // if (pos2 - pos >= 2)
                     } // 
                  } // if (number_read_commas == etc.
                     
                     
                  ///////////////////// relative wind direction [SMD output format WDR.r] //////////////////
                  //
                  // for format101:
                  //   mywind.RWD_code
                  //
                  if (number_read_commas == main.RELATIVE_WIND_DIR_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between commas at least 1 char
                        {
                           String record_aws_relative_wind_dir = laatste_record.substring(pos + 1, pos2);
                           double double_relative_wind_dir = Double.parseDouble(record_aws_relative_wind_dir);
                           mywind.int_relative_wind_dir = (int) Math.round(double_relative_wind_dir);

                           if (mywind.int_relative_wind_dir == mywind.WIND_DIR_VARIABLE)                             // variable (I think this will never met, but it makes no difference)
                           {
                              mywind.RWD_code = "999";                                               // not in IMMT description but possible in MQCS (in IMMT no entry for variable mmeasured wind)
                           }
                           else if (mywind.int_relative_wind_dir <= 9)
                           {
                              mywind.RWD_code = "00" + Integer.toString(mywind.int_relative_wind_dir);               // 3 char
                           }
                           else if (mywind.int_relative_wind_dir >= 10 && mywind.int_relative_wind_dir <= 99)
                           {
                              mywind.RWD_code = "0" + Integer.toString(mywind.int_relative_wind_dir);                // 3 char
                           }
                           else if (mywind.int_relative_wind_dir >= 100 && mywind.int_relative_wind_dir <= 360)
                           {
                              mywind.RWD_code = Integer.toString(mywind.int_relative_wind_dir);                      // 3 char
                           }
                           else
                           {
                              mywind.RWD_code = "///";                                                               // 3 char
                           }                           
                           
                        } // if (pos2 - pos >= 2)
                     }  
                  } // if (number_read_commas == etc.          
                     
                  
                  ///////////////////// true wind speed [SMD output format WS.t in m/s] /////////////////////
                  //
                  // for format101:
                  //   mywind.int_true_wind_speed
                  //   (main.wind_units)
                  //
                  // IMMT:
                  //   mywind.ff_code
                  //   mywind.iw_code
                  //
                  if (number_read_commas == main.TRUE_WIND_SPEED_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between commas at least 1 char
                        {
                           String record_aws_true_wind_speed = laatste_record.substring(pos + 1, pos2);
                           Double double_true_wind_speed = Double.parseDouble(record_aws_true_wind_speed);

                           // NB in this stage it is always m/s
                           mywind.int_true_wind_speed = (int) Math.round(double_true_wind_speed);
                           
                           // IMMT
                           //
                           // NB in case of AWS: by default wind speed units indicator = m/s was set and checked [mystationdata.java] (iw = 1)
                           mywind.iw_code = "1";       // by defaut for AWS saved sensor data (see SMD output format)
                           
                           if (mywind.int_true_wind_speed <= 9)
                           {
                              mywind.ff_code = "0" + Integer.toString(mywind.int_true_wind_speed);
                           }
                           else if ((mywind.int_true_wind_speed >= 10) && (mywind.int_true_wind_speed <= 99))
                           {
                              mywind.ff_code = Integer.toString(mywind.int_true_wind_speed);
                           }
                        } // if (pos2 - pos >= 2)
                     } // 
                  } // if (number_read_commas == etc.
                     

                  /////////////////// true wind direction [SMD output format WDT.t] /////////////////
                  //
                  // for format101:
                  //   mywind.int_true_wind_dir
                  // 
                  // IMMT:
                  //   mywind.dd_code
                  //
                  if (number_read_commas == main.TRUE_WIND_DIR_COMMA_NUMBER) 
                  {
                     if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     {
                        int pos2 = laatste_record.indexOf(",", pos + 1);
                        if (pos2 - pos >= 2) // between commas at least 1 char
                        {
                           String record_aws_true_wind_dir = laatste_record.substring(pos + 1, pos2);
                           double double_true_wind_dir = Double.parseDouble(record_aws_true_wind_dir);

                           int int_true_wind_dir = (int) Math.round(double_true_wind_dir);

                           if (int_true_wind_dir == 0) 
                           {
                              int_true_wind_dir = 360;
                           }

                           mywind.int_true_wind_dir = int_true_wind_dir;
                           
                           // IMMT
                           //
                           int int_hulp_true_wind_dir;
                           int int_hulp_dd_code;
                 
                           // 1 - 4 degrees -> 360 deg 
	                        if (int_true_wind_dir >= 1 && int_true_wind_dir <= 4)
                           {
	                           int_hulp_true_wind_dir = 360;
                           }
	                        else
                           {
	                           int_hulp_true_wind_dir = int_true_wind_dir;
                           }
            
                           // rounding to tens
                           int_hulp_dd_code = (int)Math.round((float)int_hulp_true_wind_dir / 10);
                 
                           // coding
                           if (int_hulp_dd_code <= 9)
                           {
                              mywind.dd_code = "0" + Integer.toString(int_hulp_dd_code);
                           }
                           else if ((int_hulp_dd_code >= 10) && (int_hulp_dd_code <= 36))
                           {
                              mywind.dd_code = Integer.toString(int_hulp_dd_code);
                           }

                        } // if (pos2 - pos >= 2)
                     } // if (laatste_record.length() > pos + 1 + 12)
                  } // if (number_read_commas == etc.                              
                  
                     
                  /*
                     // max wind gust speed from 10min [format WSX.x]
                     //
                     else if (number_read_commas == main.TRUE_WIND_GUST_COMMA_NUMBER)
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              int int_true_wind_gust_speed;
                              String record_aws_true_wind_gust_speed = laatste_record.substring(pos + 1, pos2);
                              Double double_true_wind_gust_speed = Double.parseDouble(record_aws_true_wind_gust_speed);

                              // convert if necessary and round to integer
                              if (main.wind_units.indexOf(main.M_S) == -1) //  not m/s so convert to knots (see TurboWin+ menu:
                              {
                                 int_true_wind_gust_speed = (int) Math.round(double_true_wind_gust_speed * main.M_S_KNOT_CONVERSION); // NB int cast necessary for conversion from long (result from Math.round) to int
                              } 
                              else 
                              {
                                 int_true_wind_gust_speed = (int) Math.round(double_true_wind_gust_speed);
                              }

                              mywind.int_true_wind_gust_speed = int_true_wind_gust_speed;

                              main.true_wind_gust_from_AWS_present = true;
                           } // if (pos2 - pos >= 2)
                        } // if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                     } // else if (number_read_commas == main.TRUE_WIND_GUST_COMMA_NUMBER)
                     //
                     // max wind gust direction from 10min [format WDX.x]
                     //
                     // -- not for displaying in TurboWin+
                     //
                     // supply voltage [format VV.v]
                     //
                     // -- not for displaying in TurboWin+
                     //
                     // internal temperature [format sTT.t]
                     //
                     // -- not for displaying in TurboWin+
                     //
                     // VOT (visual observation acquisition trigger) [format sNNN]
                     //
                     else if (number_read_commas == main.VOT_COMMA_NUMBER) 
                     {
                        if (laatste_record.length() > pos + 1 + 12) // for safety; 12 = YYYYMMDDHHmm (always at the end of every record)
                        {
                           int pos2 = laatste_record.indexOf(",", pos + 1);
                           if (pos2 - pos >= 2) // between commas at least 1 char
                           {
                              //String record_aws_VOT = laatste_record.substring(pos + 1, pos2);
                              int int_VOT = Integer.parseInt(laatste_record.substring(pos + 1, pos2));
                              //String hulp_VOT = Integer.toString(Math.abs(int_VOT));

                              if (int_VOT < 0) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 //main.jTextField4.setText(hulp_VOT + " minutes to go before you can insert obervation parameters");
                                 int_VOT = Math.abs(int_VOT) + 20;
                                 String str_VOT = Integer.toString(int_VOT);
                                 main.jTextField4.setText(str_VOT + " minutes to go before the obs will be sent by the AWS, please do not insert observation parameters");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else if (int_VOT == 100) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 main.jTextField4.setText("ship is in port (no obs will be sent)");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else if (int_VOT == 101) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 main.jTextField4.setText("observations are not required in this area (no obs will be sent)");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else if (int_VOT == 102) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 main.jTextField4.setText("the AWS transmitter is out of order (no obs will be sent)");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else if (int_VOT == 103) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 main.jTextField4.setText("the AWS GPS is out of order (no obs will be sent)");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else if (int_VOT == 104) 
                              {
                                 main.jTextField4.setForeground(Color.RED);
                                 main.jTextField4.setText("AWS transmission is deactivated (no obs will be sent)");

                                 main.jMenuItem46.setEnabled(false);                  // Obs to AWS
                              } 
                              else 
                              {
                                 main.jTextField4.setForeground(color_dark_green);
                                 String str_VOT = Integer.toString(int_VOT);
                                 main.jTextField4.setText(str_VOT + " minutes to go before the obs will be sent by the AWS, you can now insert observation parameters and select \"Output -> Obs to AWS\" ");

                                 main.jMenuItem46.setEnabled(true);                  // Obs to AWS
                              }

                                       //System.out.println("+++ parameter VOT is aanwezig");
                           } // if (pos2 - pos >= 2)
                        } // 
                     } // else if (number_read_commas == etc.   

                     // SPARE
                     //
                     // -- not for displaying in TurboWin+
                     // 
                              //
                     // SPARE
                     // -- not for displaying in TurboWin+
                     //
                     // SPARE
                     //
                     // -- not for displaying in TurboWin+
                     //
                  */
                  
               } // if (pos > 0)
            } while (pos > 0);

         } //  if (doorgaan_in_record == true && laatste_record != null)    
 
         // clear memory
         //check_presence_parameter_system_datum_tijd    = null;
         
         // set and check the date time parameters for format101 obs and IMMT
         set_date_time_record_ok = RS422_Set_Date_Time_Obs_And_IMMT_For_AWSR(record_aws_date, record_aws_time);
         check_date_time_record_ok = RS422_Check_Date_Time_Last_Saved_Record_For_AWSR(record_aws_date, record_aws_time);
         
         // send the data
         if (check_date_time_record_ok && set_date_time_record_ok)
         {
            RS422_Send_Sensor_Data_To_AWSR(retry);
         }   
      } // protected void done()

   }.execute(); // new SwingWorker <Void, Void>()
      
} // private void RS422_Read_AWS_Sensor_Data_For_Display()



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/  
private void RS422_update_AWS_dasboard_values()
{
   // called from: RS422_Read_AWS_Sensor_Data_For_Display() [main_RS232_RS422.java]
   
   
   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         // last update date time
         //
         //
         dashboard_string_last_update_record_date_time = main.sdf_tsl_2.format(new Date()) + " UTC";   // // new Date() -> always in UTC 
   
   
         // air pressure sensor_height
         //
         //
         // NB if AWS connected always pressure with ic intergrated (contrary to only a barometer connected)
         //
         if (main.pressure_sensor_level_from_AWS_present)
         {
            // nb in case EUCAWS always ic integrated in barometer reading!
            dashboard_double_last_update_record_sensor_level_pressure_ic = Double.parseDouble(mybarometer.pressure_reading);
         } 
         else
         {
            dashboard_double_last_update_record_sensor_level_pressure_ic = Double.MAX_VALUE;
         }
   
   
         // air pressure MSL
         //
         //
         // NB if AWS connected always pressure with ic intergrated (contrary to only a barometer connected)
         //
         if (main.pressure_MSL_from_AWS_present)
         {
            dashboard_double_last_update_record_MSL_pressure_ic = Double.parseDouble(mybarometer.pressure_msl_corrected);
         } 
         else
         {
            dashboard_double_last_update_record_MSL_pressure_ic = Double.MAX_VALUE;
         }
   
   
         // air temperature
         //
         //
         if (main.air_temp_from_AWS_present)
         {
            dashboard_double_last_update_record_air_temp = Double.parseDouble(mytemp.air_temp);
         } 
         else
         {
            dashboard_double_last_update_record_air_temp = Double.MAX_VALUE;
         }
   
         // humidity
         //
         //
         if (main.rh_from_AWS_present)
         {
            double hulp_double_rv = Math.round(mytemp.double_rv * 10 * 100) / 10.0; // nb Math.round(xxx) - > geeft long terug  // NB * 100 for getting %
           dashboard_double_last_update_record_humidity = hulp_double_rv;
         } 
         else
         {
            dashboard_double_last_update_record_humidity = Double.MAX_VALUE;
         }        
     
   
         // true wind dir
         //
         //
         if (main.true_wind_dir_from_AWS_present)
         {
            dashboard_int_last_update_record_true_wind_dir = mywind.int_true_wind_dir;
         }
         else
         {
            dashboard_int_last_update_record_true_wind_dir = Integer.MAX_VALUE;        
         }
   
   
         // relative wind dir
         //
         //
         if (main.relative_wind_dir_from_AWS_present) 
         {
            //dashboard_int_last_update_record_relative_wind_dir = Integer.parseInt(mywind.wind_dir);
            dashboard_int_last_update_record_relative_wind_dir = mywind.int_relative_wind_dir;
         } 
         else 
         {
            dashboard_int_last_update_record_relative_wind_dir = Integer.MAX_VALUE;
         }
   
   
         // true wind speed
         //
         //
         if (main.true_wind_speed_from_AWS_present) 
         {
            // NB 4 options:
            // 1) measured/observed in m/s   -> dashboard and graph in knots
            // 2) measured/observed in m/s   -> dashboard and graph in m/s
            // 3) measured/observed in knots -> dashboard and graph in knots
            // 4) measured/observed in knots -> dashboard and graph in m/s
            
            //if (main.wind_units.indexOf(main.M_S) == 0)                          // wind_units set to m/s by observer in Maintenance -> Station data
            //{
            //   dashboard_int_last_update_record_true_wind_speed = (int) Math.round((double)mywind.int_true_wind_speed * main.M_S_KNOT_CONVERSION);
            //}
            //else
            //{
            //   dashboard_int_last_update_record_true_wind_speed = mywind.int_true_wind_speed;
            //}
            
            if (main.wind_units.indexOf(main.M_S) != -1)                   // wind_units 'as measured' set to m/s by observer in Maintenance -> Station data
            {
               if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // dashboard in knots
               {
                  //dashboard_int_last_update_record_true_wind_speed = (int) Math.round((double)mywind.int_true_wind_speed * main.M_S_KNOT_CONVERSION);
                  dashboard_int_last_update_record_true_wind_speed = (int) Math.round(mywind.double_true_wind_speed * main.M_S_KNOT_CONVERSION);
               }
               else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // dashboard in m/s
               {
                  dashboard_int_last_update_record_true_wind_speed = mywind.int_true_wind_speed;    
               }
            }
            else if (main.wind_units.indexOf(main.KNOTS) != -1)            // wind_units 'as measured' set to knots by observer in Maintenance -> Station data
            {
               if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // dashboard in knots
               {
                  dashboard_int_last_update_record_true_wind_speed = mywind.int_true_wind_speed; 
               }
               else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // dashboard in m/s
               {
                  //dashboard_int_last_update_record_true_wind_speed = (int) Math.round((double)mywind.int_true_wind_speed * main.KNOT_M_S_CONVERSION); 
                  dashboard_int_last_update_record_true_wind_speed = (int) Math.round(mywind.double_true_wind_speed * main.KNOT_M_S_CONVERSION);  
               }   
            }
         } 
         else 
         {
            dashboard_int_last_update_record_true_wind_speed = Integer.MAX_VALUE;
         }
   
   
         // relative wind speed
         //
         //
         if (main.relative_wind_speed_from_AWS_present) 
         {
            // NB 4 options:
            // 1) measured/observed in m/s   -> dashboard and graph in knots
            // 2) measured/observed in m/s   -> dashboard and graph in m/s
            // 3) measured/observed in knots -> dashboard and graph in knots
            // 4) measured/observed in knots -> dashboard and graph in m/s
            
            //if (main.wind_units.indexOf(main.M_S) == 0)   
            //{
            //   dashboard_int_last_update_record_relative_wind_speed = (int) Math.round((double)(Integer.parseInt(mywind.wind_speed) * main.M_S_KNOT_CONVERSION));
            //}
            //else
            //{
            //?   dashboard_int_last_update_record_relative_wind_speed = Integer.parseInt(mywind.wind_speed);
            //}
            
            if (main.wind_units.indexOf(main.M_S) != -1)                   // wind_units 'as measured' set to m/s by observer in Maintenance -> Station data
            {
               if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // dashboard in knots
               {
                  //dashboard_int_last_update_record_relative_wind_speed = (int) Math.round((double)mywind.int_relative_wind_speed * main.M_S_KNOT_CONVERSION);
                  dashboard_int_last_update_record_relative_wind_speed = (int) Math.round(mywind.double_relative_wind_speed * main.M_S_KNOT_CONVERSION);
               }
               else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // dashboard in m/s
               {
                  dashboard_int_last_update_record_relative_wind_speed = mywind.int_relative_wind_speed;    
               }
            }
            else if (main.wind_units.indexOf(main.KNOTS) != -1)             // wind_units 'as measured' set to knots by observer in Maintenance -> Station data
            {
               if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // dashboard in knots
               {
                  dashboard_int_last_update_record_relative_wind_speed = mywind.int_relative_wind_speed; 
               }
               else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // dashboard in m/s
               {
                  //dashboard_int_last_update_record_relative_wind_speed = (int) Math.round((double)mywind.int_relative_wind_speed * main.KNOT_M_S_CONVERSION); 
                  dashboard_int_last_update_record_relative_wind_speed = (int) Math.round(mywind.double_relative_wind_speed * main.KNOT_M_S_CONVERSION);
               }   
            }
         } 
         else 
         {
            dashboard_int_last_update_record_relative_wind_speed = Integer.MAX_VALUE;
         }
   
   
         // true wind gust speed
         //
         //
         if (main.true_wind_gust_from_AWS_present) 
         {
            // NB 4 options:
            // 1) measured/observed in m/s   -> dashboard and graph in knots
            // 2) measured/observed in m/s   -> dashboard and graph in m/s
            // 3) measured/observed in knots -> dashboard and graph in knots
            // 4) measured/observed in knots -> dashboard and graph in m/s
            
            //if (main.wind_units.indexOf(main.M_S) == 0)   
            //{
            //   dashboard_int_last_update_record_true_wind_gust = (int) Math.round((double)mywind.int_true_wind_gust_speed * main.M_S_KNOT_CONVERSION);
            //}
            //else
            //{
            //   dashboard_int_last_update_record_true_wind_gust = mywind.int_true_wind_gust_speed;
            //}
            
            if (main.wind_units.indexOf(main.M_S) != -1)                   // wind_units 'as measured' set to m/s by observer in Maintenance -> Station data
            {
               if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // dashboard in knots
               {
                  //dashboard_int_last_update_record_true_wind_gust = (int) Math.round((double)mywind.int_true_wind_gust_speed * main.M_S_KNOT_CONVERSION);
                  dashboard_int_last_update_record_true_wind_gust = (int) Math.round(mywind.double_true_wind_gust_speed * main.M_S_KNOT_CONVERSION);
               }
               else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // dashboard in m/s
               {
                  dashboard_int_last_update_record_true_wind_gust = mywind.int_true_wind_gust_speed;    
               }
            }
            else if (main.wind_units.indexOf(main.KNOTS) != -1)            // wind_units 'as measured' set to knots by observer in Maintenance -> Station data
            {
               if (main.wind_units_dashboard.indexOf(main.KNOTS) != -1)    // dashboard in knots
               {
                  dashboard_int_last_update_record_true_wind_gust = mywind.int_true_wind_gust_speed; 
               }
               else if (main.wind_units_dashboard.indexOf(main.M_S) != -1) // dashboard in m/s
               {
                  //dashboard_int_last_update_record_true_wind_gust = (int) Math.round((double)mywind.int_true_wind_gust_speed * main.KNOT_M_S_CONVERSION); 
                  dashboard_int_last_update_record_true_wind_gust = (int) Math.round(mywind.double_true_wind_gust_speed * main.KNOT_M_S_CONVERSION);  
               }   
            }
         } 
         else 
         {
            dashboard_int_last_update_record_true_wind_gust = Integer.MAX_VALUE;
         }

   
         // SST
         //
         //
         if (main.SST_from_AWS_present)
         {
            dashboard_double_last_update_record_sst = Double.parseDouble((mytemp.sea_water_temp));
         }
         else
         {
            dashboard_double_last_update_record_sst = Double.MAX_VALUE;        
         }
  
         return null;
      } // protected Void doInBackground() throws Exception
   }.execute(); // new SwingWorker<Void, Void>()

}




private final String STRING_PRESSURE_IO_FILE_ERROR                              = "4444444"; // number without significance (random)
private final String STRING_AIR_TEMP_IO_FILE_ERROR                              = "4444444"; // number without significance (random)
private final String STRING_PRESSURE_NO_FILE_ERROR                              = "5555555"; // number without significance (random)
private final String STRING_AIR_TEMP_NO_FILE_ERROR                              = "5555555"; // number without significance (random)
private final String STRING_PRESSURE_RECORD_FORMAT_ERROR                        = "6666666"; // number without significance (random)
private final String STRING_AIR_TEMP_RECORD_FORMAT_ERROR                        = "6666666"; // number without significance (random)
private final String STRING_PRESSURE_CHECKSUM_ERROR                             = "7777777"; // number without significance (random)
private final String STRING_AIR_TEMP_CHECKSUM_ERROR                             = "7777777"; // number without significance (random)
private final String STRING_PRESSURE_TIMEDIFF_ERROR                             = "8888888"; // number without significance (random)
private final String STRING_AIR_TEMP_TIMEDIFF_ERROR                             = "8888888"; // number without significance (random)
private final String STRING_PRESSURE_OBS_AGE_ERROR                              = "3333333"; // number without significance (random)
private final String STRING_AIR_TEMP_OBS_AGE_ERROR                              = "3333333"; // number without significance (random)
private final int INT_PRESSURE_IO_FILE_ERROR                                    = 4444444;   // number without significance (see STRING_PRESSURE_IO_FILE_ERROR)
private final int INT_AIR_TEMP_IO_FILE_ERROR                                    = 4444444;   // number without significance (see STRING_AIR_TEMPP_IO_FILE_ERROR)
private final int INT_PRESSURE_NO_FILE_ERROR                                    = 5555555;   // number without significance (see STRING_PRESSURE_NO_DFILE_ERROR)
private final int INT_AIR_TEMP_NO_FILE_ERROR                                    = 5555555;   // number without significance (see STRING_AIR_TEMP_NO_DFILE_ERROR)
private final int INT_PRESSURE_RECORD_FORMAT_ERROR                              = 6666666;   // number without significance (see STRING_PRESSURE_RECORD_FORMAT_ERROR)
private final int INT_AIR_TEMP_RECORD_FORMAT_ERROR                              = 6666666;   // number without significance (see STRING_AIR_TEMP_RECORD_FORMAT_ERROR)
private final int INT_PRESSURE_CHECKSUM_ERROR                                   = 7777777;   // number without significance (see STRING_PRESSURE_CHECKSUM_ERROR)
private final int INT_AIR_TEMP_CHECKSUM_ERROR                                   = 7777777;   // number without significance (see STRING_AIR_TEMP_CHECKSUM_ERROR)
private final int INT_PRESSURE_TIMEDIFF_ERROR                                   = 8888888;   // number without significance (see STRING_PRESSURE_TIMEDIFF_ERROR)
private final int INT_AIR_TEMP_TIMEDIFF_ERROR                                   = 8888888;   // number without significance (see STRING_AIR_TEMP_TIMEDIFF_ERROR)
private final int INT_PRESSURE_OBS_AGE_ERROR                                    = 3333333;   // number without significance (see STRING_PRESSURE_OBS_AGE_ERROR)
private final int INT_AIR_TEMP_OBS_AGE_ERROR                                    = 3333333;   // number without significance (see STRING_AIR_TEMP_OBS_AGE_ERROR)
private static final long TIMEDIFF_SENSOR_DATA                                  = 5L;        // retrieved sensor data (pressure) max ?minutes old, after this the data is considered not valid anymore
private static final long TIMEDIFF_GPS_DATA                                     = 10L;       // retrieved data (GPS) max ?minutes old, after this the data is considered not valid anymore
private static final long TIMEDIFF_AWSR_DATA                                    = 10L;       // retrieved sensor data aWSR mode max ?minutes old, after this the data is considered not valid anymore
private final double HPA_TO_INHG                                                = 0.02952998751;      // WOW
private final int DELAY_NEW_DATA_CHECK_LOOP                                     = 60000;     // time between checks new aws data in msec (e.g. 1 min = 1 * 60 * 1000 = 60000)
private final int INITIAL_DELAY_NEW_DATA_CHECK_LOOP                             = 60000;     // time to wait after start up in msec (e.g. 2 min = 2 * 60 * 1000 = 120000)
private final long MAX_AGE_AWS_DATA                                             = 300000;    // time in msec (e.g. 5 min = 5 * 60 * 1000 = 300000) // before listening thread cancelling/restarting
private final int APR_RETRY_MINUTES                                             = 3;         // APR
private final int AWSR_RETRY_MINUTES                                            = 3;         // AWSR
public static final int AT_GREAT_LAKES_OUTSIDE_MAIN_AREAS                       = 777;       // APR; symbolic value, only to indicate the ship is at the great lakes but mabe in a lock (not possible to determine height corr)
private final int CANCEL_GPS_THREAD                                             = 40;        // minutes, for reconnecting lost connection
private final int START_GPS_THREAD                                              = 43;        // minutes, for reconnecting lost connection
private final int CANCEL_BAROMETER_THREAD                                       = 30;        // minutes, for reconnecting lost connection
private final int START_BAROMETER_THREAD                                        = 33;        // minutes, for reconnecting lost connection
private final int CANCEL_AWS_THREAD                                             = 30;        // minutes, for reconnecting lost connection
private final int START_AWS_THREAD                                              = 33;        // minutes, for reconnecting lost connection
private final long MAX_AGE_GPS_DATA                                             = 600000;    // time in msec (e.g. 10 min = 10 * 60 * 1000 = 600000) before listening thread cancelling/restarting        
public final static int MAX_AGE_STARX_OBS_DATA                                  = 600;       // time in sec (e.g. 10 min = 10 * 60 = 600)

//private int comm_error_messagebox_shown                                         = 0;
private Color color_dark_green;
private GregorianCalendar cal_RS232_GPS_system_date_time;                       // for restarting RS232 GPS listener if no data data was received
private GregorianCalendar cal_RS422_system_date_time;                           // for restarting RS422 (AWS) listener if no data data was received
private GregorianCalendar cal_RS232_system_date_time;                           // for restarting RS232 (Barometer) listener if no data data was received
//private GregorianCalendar cal_WiFi_system_date_time;                            // for restarting WiFi listener if no Wifi data was received
//private GregorianCalendar cal_Ethernet_system_date_time;                        // for restarting WiFi listener if no Ethernet data was received
private GregorianCalendar cal_WOW_systeem_datum_tijd;                           // format: yyyyMMddHHmm
private GregorianCalendar cal_APR_system_date_time;
private GregorianCalendar cal_AWSR_system_date_time;
private GregorianCalendar cal_delete_datum_tijd;   
private GregorianCalendar cal_sensor_datum_tijd; 
private GregorianCalendar check_presence_parameter_system_datum_tijd;
private static GregorianCalendar cal_system_date_time;
private SimpleDateFormat sdf7;
private static SimpleDateFormat sdf8;                                                       // for pressure reading pop-up in system tray
//private SimpleDateFormat sdf9;                                                            // for GPS check gps time vz sustem time
private final int MAX_COMPLETED_BAROMETER_PORT_CHECKS                           = 2;
private final int MAX_COMPLETED_AWS_PORT_CHECKS                                 = 2;
private int system_minute;
private long last_new_data_received_TimeMillis;                                 // returns the time of last aws or barometer data receipt in milliseconds since midnight, January 1, 1970 UTC.
private long last_new_GPS_data_received_TimeMillis;                             // returns the time of last gPS data receipt in milliseconds since midnight, January 1, 1970 UTC.
private int check_new_data_timer_delay;
private Timer check_new_data_timer;                                             // for checking sensor data not obsolete (AWS or barometer)
private Timer check_new_GPS_data_timer;                                         // for checking sensor data not obsolete (GPS)
private int check_new_GPS_data_timer_delay;   

//private SwingWorker WiFi_worker;
private WiFi_Class_Receive_UDP WiFi_worker;                                     // SwingWorker with reference (for WiFi connection)
private Ethernet_Class_Receive_UDP Ethernet_worker;                             // SwingWorker with reference (for Ethernet connection)
private RS232_Class_Receive_Sensor_Data RS232_worker;                           // SwingWorker with reference (for RS232 connection
//private RS422_Class_Receive_Sensor_Data RS422_worker;                           // SwingWorker with reference (for RS422 connection)
private RS422_V3_Class_Receive_Sensor_Data RS422_worker;                        // SwingWorker with reference (for RS422 connection)
private RS232_GPS_Class_Receive_Data RS232_GPS_worker;                          // SwingWorker with reference (for RS232 GPS connection)


private final StringBuilder message_peumb = new StringBuilder();

private final double CELCIUS_TO_KELVIN_FACTOR                                   = 273.15; 
private final double M_S_TO_KNOTS                                               = 1.94384;

// mintaka star GPS
public static String test_record                                                = "";

// RS232 GPS
public static String GPS_defaultPort                                            = null;  // port info for the system (to open/close etc)
public static String GPS_defaultPort_descriptive                                = null;  // port info for messages and logging
private final int MAX_COMPLETED_GPS_PORT_CHECKS                                 = 2;

private final int GPS_NMEA_0183_parity                                          = 0;   // 0 = none parity
private final int GPS_NMEA_0183_data_bits                                       = 8;
private final int GPS_NMEA_0183_stop_bits                                       = 1;
private final int GPS_NMEA_0183_flow_control                                    = SerialPort.FLOW_CONTROL_DISABLED;//SerialPort.FLOWCONTROL_NONE;

public static String GPS_date                                                   = "";
public static String GPS_time                                                   = "";
public static String GPS_latitude                                               = "";
public static String GPS_latitude_hemisphere                                    = "";
public static String GPS_longitude                                              = "";
public static String GPS_longitude_hemisphere                                   = "";
public static String GPS_latitude_earlier                                       = "";                     // for computing COG and SOG (Ds, Vs)
public static String GPS_longitude_earlier                                      = "";                     // for vomputing COG and SOG (Ds, Vs)       

// APR and AWSR
public static int APR_server_response_code                                      = 0;
public static int AWSR_server_response_code                                     = 0;
private String format_101_obs                                                   = "";

// Dashboard
public static double dashboard_double_last_update_record_pressure_ic            = Double.MAX_VALUE;
public static double dashboard_double_last_update_record_ppp                    = Double.MAX_VALUE;
public static String dashboard_string_last_update_record_date_time              = "";
public static double dashboard_double_last_update_record_MSL_pressure_ic        = Double.MAX_VALUE;
public static double dashboard_double_last_update_record_sensor_level_pressure_ic = Double.MAX_VALUE;
public static double dashboard_double_last_update_record_air_temp               = Double.MAX_VALUE;
public static int dashboard_int_last_update_record_true_wind_dir                = Integer.MAX_VALUE;
public static int dashboard_int_last_update_record_true_wind_speed              = Integer.MAX_VALUE;
public static int dashboard_int_last_update_record_true_wind_gust               = Integer.MAX_VALUE;
public static int dashboard_int_last_update_record_relative_wind_dir            = Integer.MAX_VALUE;
public static int dashboard_int_last_update_record_relative_wind_speed          = Integer.MAX_VALUE;
public static double dashboard_double_last_update_record_sst                    = Double.MAX_VALUE;
public static double dashboard_double_last_update_record_humidity               = Double.MAX_VALUE;
        

} // public class main_RS232_RS422
