import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;
import javax.jnlp.BasicService;
import javax.jnlp.FileContents;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.jnlp.SingleInstanceListener;
import javax.jnlp.SingleInstanceService;
import javax.jnlp.UnavailableServiceException;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import jssc.SerialPort;
import jssc.SerialPortException;

//import netscape.javascript.JSObject;



/*
 * main.java
 *
 * Created on 26 maart 2008, 8:31
 */


/* TO DO
 *
 * - geselecteerde waarnemer weer selecteren bij weer openen waarnemers input pagina
 * - station data als binaire data wegschrijven
 * - cheken t.o.v. voorgaande waarneming
 * - checken op bestaant (10-graads) vak
 * - hoogte wolkenbasis advies
 * - move log files by E-mail
 *
 * 
*/

/*
* ---------------------------------------------------------------------------------------------------------------------
* I’ve installed previous versions of jdk1.6.0_17, and then after installing netbeans 7, I upgrade jdk to version jdk1.6.0_25.
* 
* Now, everytime I start Netbeans, it always show me this message :
*
* Cannot locate java installation in specified jdkhome:
* C:\Program Files\Java\jdk1.6.0_17
* Do you want to try to use default version?
*
* If I click “Yes”, the next time Netbeans started, the same message appear.
*
* So to remove this, I change “netbeans.conf” in “C:\Program Files\NetBeans 7.0\etc”.
* 
* Change “netbeans_jdkhome” like this :
* netbeans_jdkhome=”C:\Program Files\Java\jdk1.6.0_25?
* ---------------------------------------------------------------------------------------------------------------------
* Wanneer .jar file direct wordt opgestart heb je geen invloed op de java heap memory
* (wel via java web start in de jnlp file of via cmd batch file)
* 
* dus wanneer direct via jar file aanpassen in java control panel:
* http://www.wikihow.com/Increase-Java-Memory-in-Windows-7
* 
* ---------------------------------------------------------------------------------------------------------------------
*
* na een copy van de ene PC naar een andere:
*         - indien reference problems, resolve door te verwijzen naar:
*         - ../backup/jnlp_jar/jnlp.jar
*         - ../backup/jssc_jar/jssc.jar
*
* IDE: hierna after a clean build:
*         - copy icons dir from \backup\icons\build_classes\icons to \build\classes\
*         - copy format_101 dir from \backup\format_101\build_classes\format_101 to \build\classes\  
*         - recreate dir dist\logs
*         - copy the file  \backup\jnlp\turbowin_jws_offline.jnlp to \dist\
*         - copy the file  \backup\cmd\turbowin_plus_offline.cmd to \dist\
* 
* ---------------------------------------------------------------------------------------------------------------------
* IDE: Bij properties -> Build -> Compiling: Compile on Save uitzetten!!
* IDE: Bij Properties -> Build -> Compiling: -Xlint:unchecked
* 
* IDE: Tools -> Options -> Editor -> Formatting: number of spaces per indent: 3
*                                                                  Tab size : 3
* 
* IDE: Tools -> Options -> Editor -> Hints:  probable bugs     : unused assignment                     : uitgezet
*                                            suggestions       : split declaration                     : uitgezet
*                                            JDK 1.5 and later : use switch over strings where possible: uitgezet
*                                                              : convert to try-with-resources         : uitgezet
* ---------------------------------------------------------------------------------------------------------------------
* om JNLP API (b.v.voor BasicService) ter beschikking te hebben 
* met rechtermuisknop klikken op "turbowin_jws" -> properties -> libraries -> compile -> add jar/folder 
* C:\Program Files\Java\jdk1.6.0\sample\jnlp\servlet
* in dist komt dan (automatisch) een sub-dir lib  met daarin jnlp.jar
* 
* Als compiler alsnog jnlp.jar niet kan vinden dan verwijzen naar jnlp.jar in de dist/lib folder
* ( "turbowin_jws" -> properties -> libraries -> compile -> add jar/folder ->
* C:\Program Files\NetBeans 6.5.1\projects\turbowin_jws\dist\lib)
* 
* ook deze jnlp.jar moet gesigned worden + aangemeld in de turbowin_jws.jnlp file
* 
* vanaf JDK7: jnlp.jar via download:  "Java SE Development Kit 7u21 Demos and Samples Downloads" (ergens unzippen en dan verwijzen naar ...\samples\jnlp\servlet\jnlp.jar)
* voor het gemak ook onder \...\backup\jnlp_jar gezet
* 
* ---------------------------------------------------------------------------------------------------------------------
* 
* SELF SIGNED [niet meer geldig/geaccepteerd bij nieuwere JREs]
* java web start applicaties moeten "getekend" zijn
* 1. key genereren bv: "c:\program files\java\jdk1.6.0_10\bin\keytool" -genkey -keystore myKeys -alias keyalias -keypass Martin
* 2. signen bv       : "C:\program files\Java\jdk1.6.0_10\bin\jarsigner" -verbose -keystore myKeys "C:\program files\netbeans-6.5\projects\turbowin_jws\dist\turbowin_jws.jar" keyalias
* (zie de specifieke .cmd files in "C:\Program Files\NetBeans 6.5\projects\turbowin_jws\backup\java_web_start" met hierin deze commando's)
*
* 
* CERTIFICATE
* - jnlp.jar, jssc.jar en turbowin_jws.jar moeten gesigned worden
* - NB vanaf versie 1.7 update 25 moet de manifest in de jars gewijzigd worden voor permisions en codebase (anders warning in console)
* - NB vanaf versie 1.7 update 45 moet de manifest in de jars gewijzigd worden voor application-name (anders warning in console)
*
*
* onderstaande voor officieel certificaat
* - certificaat is file cert-stam.p12 van COMODO (toegestuurd door Jeroen van der Reijden) [password: stam]
* - geldig tot 29-10-2016 (zie jarsigner_veriy.cmd)
*
*  >>>>>>>>>>>>>>>> dit is de uitvoer van keytool naar file (vanaf de bxpm171, let op waar de alias ["comodo ca limited-id van knmi"] staat!!) ///////////////////
* D:\stam\Mijn Documenten\NetBeansProjects\turbowin_jws\backup\java_web_start_BXPM171>"c:\program files\java\jdk1.7.0_25\bin\keytool" -list -storetype pkcs12 -storepass stam -keystore cert-stam.p12 
*
* Keystore type: PKCS12
* Keystore provider: SunJSSE
*
* Your keystore contains 1 entry
*
* comodo ca limited-id van knmi, 29-Oct-2013, PrivateKeyEntry, 
* Certificate fingerprint (SHA1): E1:20:C2:11:20:A6:A0:58:E1:E8:E1:33:26:5B:48:6D:15:61:A2:18
*   >>>>>>>>>>>>>>
*
*
* ---------------------------------------------------------------------------------------------------------------------
* - muffin: // Windows 7        bv : C:\Users\hometrainer\AppData\LocalLow\Sun\Java\Deployment\cache\6.0\muffin
*           // XP               bv : C:\Documents and Settings\stam\Local Settings\Application Data\Sun\Java\Deployment\cache\6.0\muffin
* ---------------------------------------------------------------------------------------------------------------------
* - Bij overgang JDK 6 naar JDK 7:  - bij project properties (rechtermuisklik turbowin_jws -> properties -> sources) source/binary format evetueel veranderen van JDK 6 naar JDK 7 (bij JDK 6 wordt er gecheckt tegen JDK6 eigenschappen; bij JDK7 wordt er -ook- gecheckt tegen java 1.7 -nieuwe- eigenschappen)                                  
*                                   - bij project properties (rechtermuisklik turbowin_jws -> properties -> libraries) java platform: JDK 1.7 
*                                   - bij project properties (rechtermuisklik turbowin_jws -> properties -> libraries) compile time libraries: ...\jdk1_7_0_17_samples\sample\jnlp\servlet\jnlp.jar
*                                   - ook jarsigner_jnlp.cmd, keytool_turbowin_jws.cmd en jarsigner_turbowin_jws.cmd aanpassen
* 
* ----------------------------------------------------------------------------------------------------------------------
*
* wellicht interessant : https://blogs.oracle.com/jtc/entry/serial_port_communication_for_java
*
* NB VANAF VERSIE 2.4.0 JSSC ALS SERIAL COMMUNICATIE LIBRARY (BEHOEFT GEEN APARTE OS AFHANKELIJKE DRIVER INSTALLATIE)
*
*-----------------------------------------------------------------------------------------------------------------------
* directory structuur in offline mode
*              - main dir - turbowin_jws.jar
*              - main dir - turbowin_plus_offline.cmd (indien deze file aanwezig dan wordt turbowin_jws_offline.jnlp genegeerd)
*              - main dir - turbowin_jws_offline.jnlp (wanneer turbowin_plus_offline.cmd afwezig dan via de jnlp methode)
*              - sub dir lib (met files jnlp.jar, jssc.jar en ??AbsoluteLayout.jar??)
*              - sub dir help (met zelfde files als zoals op knmi turbowin internet server)
*              - sub dir logs (deze is echter niet noodzakelijk, indien niet aanwezig automatisch aagemaakt door TurboWin+)
*              - sub dir docs met ........
*              - sub dir amver (deze is echter niet noodzakelijk, indien niet aanwezig automatisch aagemaakt door TurboWin+)
*              - zie FORMAT_101.java voor extra dirs en files voor semi compressed berichten versturen
*              - NB voor volledig overzicht zie doc: "turbowin+_folder_structuur.docx"
*
* NB WHEN STARTED WITH turbowin_jws_offline.jnlp AND turbowin_plus_offline.cmd IS IN THE SAME DIR
*    THEN THE META DATA WILL ONLY BE STORED AND RETRIEVED FROM CONFIGURATION FILES AND NOT FROM/TO MUFFINS!
*    DELETE/RENAME turbowin_plus_offline.cmd TO USE MUFFINS
* 
* ----------------------------------------------------------------------------------------------------------------------
* global var: RS232_connection_mode: 0 = no instrument; serial connection or WiFi (default) 
*             (instrument type)      1 = barometer PTB220 
*                                    2 = barometer BTB330 
*                                    3 = EUCOS_AWS (EUCAWS)
*                                    4 = barometer Mintaka Duo
*                                    5 = barometer Mintaka Star (USB serial)
*                                    6 = barometer Mintaka Star (WiFi; access point mode or station mode)
*                                    
*
*
* global var: RS232_GPS_connection_mode: 0 = no GPS serial connection (default) 
*                                        1 = GPS NMEA 0183 
*                                        2 = GPS NMEA 2000  [for future use]
*                                        3 = GPS in Mintaka Star (USB, staion mode, access point)
*
* global var: RS232_GPS_sentence : 0 = no sentence
*                                  1 = RMC
*                                  2 = GGA
* 
* ----------------------------------------------------------------------------------------------------------------------
* global var obs_format:  - FORMAT_FM13
*                         - FORMAT_101
*                         - FORMAT_AWS
* ----------------------------------------------------------------------------------------------------------------------
* configuration data (bv call sign, imo nummer, hoogte deklading etc) wordt op 3 plaatsen weggeschreven: !!!
*   - 1x in muffin (java cache)
*   - 2x in configuration.txt - logs_dir (user defined in online mode sub dir in offline mode -dir waar immt.log staat-)
*                             - user_dir (system defined -dir waar turbowin_jws.jar staat- NB kan zijn dat user-dir write protected is bv bij 
*                                         installatie in de Program Files !! -> komt dan verder geen melding, dit is bij installatie script
*                                         aan te passen zie [DIRS] section, permissions etc. bij inno setup)
*
* 
* main.configuratie_regels[0 - 14]                              -> station data 
* main.configuratie_regels[15 - 16]                             -> email settings
* main.configuratie_regels[17]                                  -> logs_dir
* main.configuratie_regels[18]                                  -> email settings
* main.configuratie_regels[19]                                  -> station data (wind units)
* main.configuratie_regels[20]                                  -> INSTRUMENT instrument connection mode; serial communication settings
* main.configuratie_regels[21]                                  -> INSTRUMENT bps; serial communication settings
* main.configuratie_regels[22]                                  -> INSTRUMENT data bits; serial communication settings
* main.configuratie_regels[23]                                  -> INSTRUMENT parity serial; communication settings
* main.configuratie_regels[24]                                  -> INSTRUMENT stop bits; serial communication settings
* main.configuratie_regels[25]                                  -> INSTRUMENT prefered COM port (Windows and Linux); serial communication settings
* main.configuratie_regels[26]                                  -> barometer instrument correction
* main.configuratie_regels[27]                                  -> obs format (101 or FM13)
* main.configuratie_regels[28]                                  -> obs format 101 call sign encryption (yes or no)
* main.configuratie_regels[29]                                  -> obs format 101 email (body or attachement)
* main.configuratie_regels[30]                                  -> INSTRUMENT prefered COM port name (OS X); serial communication settings
* main.configuratie_regels[31]                                  -> WOW (true/false publish on WOW -WeatherObservationsWebsite-)
* main.configuratie_regels[32]                                  -> WOW_site_id                       
* main.configuratie_regels[33]                                  -> WOW_site_pin      
* main.configuratie_regels[34]                                  -> WOW_reporting_interval 
* main.configuratie_regels[35]                                  -> WOW/APR average draught            
* main.configuratie_regels[36]                                  -> default E-mail program on this computer is AMOS Mail [true/false]
* main.configuratie_regels[37]                                  -> GPS (NMEA 0183) connection mode; serial communication settings
* main.configuratie_regels[38]                                  -> GPS (NMEA 0183) bits per second; serial communication settings
* main.configuratie_regels[39]                                  -> GPS (NMEA 0183) prefered COM port (Windows and Linux); serial communication settings
* main.configuratie_regels[40]                                  -> GPS (NMEA 0183) prefered COM port name (OS X); serial communication settings
* main.configuratie_regels[41]                                  -> GPS (NMEA 0183) RS232_GPS_sentence to use: serial communication settings
* main.configuratie_regels[42]                                  -> APR (Automated Pressure Reports) [true/false]
* main.configuratie_regels[43]                                  -> APR reporting interval
* main.configuratie_regels[44]                                  -> upload URL (Output -> Obs to server)
*
* 
* lezen via: - read_muffin()
*            - lees_configuratie_regels()
* 
* schrijven via: - set_muffin()
*                - schrijf_configuratie_regels()
* 
* maar zie ook: - meta_data_from_configuration_regels_into_global_vars() [main.java]
*          - OK_button_actionPerformed [mystationdata.java]
*          - OK_button_actionPerformed [mylogfiles.java]
*          - OK_button_actionPerformed [myemailsettings.java]
*          - OK_button_actionPerformed [RS232_settings.java]
*          - OK_button_actionPerformed [mywind.java] 
*          - OK_button_actionPerformed [mybarometer.java] 
*          - OK_button_actionPerformed [myobsformat.java]
*          - OK_button_actionPerformed [WOW_settings.java]
*
*
* ->->->->-> -- IF NEW ENTRY, IT IS ONLY NECESSARY TO APPEND THESE TWO FUNCTIONS:--- <-<-<-<-
*                              - fill_configuratie_array()
*                              - meta_data_from_configuration_regels_into_global_vars()
*
* 
* ---------------------------------------------------------------------------------------------------------------------
* 
* NB input/output in een GUI altijd via een SwingWorker (Core Java Volume 1 bld 795 e.v.; Volume 2 bld 37, 215) 
* 
* NB java.io.File.separator gebruiken i.p.v. "/" of "\"
*
* 
* ---------------------------------------------------------------------------------------------------------------------
* CHECK ONLY SINGLE INSTANCE OF TURBOWIN+ IS RUNNING:
* 
* only jnlp mode can use the single instance running check 
* 
* in offline mode: So by removing the file turbowin_plus_offline.cmd and invoking turbowin+ via turbowin_jws_offline.jnlp there will be a single instance running check
* online mode: is always started via the jnlp file -> always single instance running check
*  
* ---------------------------------------------------------------------------------------------------------------------
* UPDATE IN/TERVAL PARAMETERS
* update date-time field main screen every minute if GPS is connected (both, APR and MANUAL mode)
*
* 
**/


/**
 *
 * @author  Martin
 */
public class main extends javax.swing.JFrame {
   
   /* inner class popupListener */
   class PopupListener_input extends MouseAdapter 
   {
      @Override
      public void mousePressed(MouseEvent e) 
      {
         ShowPopup(e);
         //System.out.println("Popup menu will be visible!");
      }

      @Override
      public void mouseReleased(MouseEvent e) 
      {
         ShowPopup(e);
      }

      private void ShowPopup(MouseEvent e) 
      {
         if (e.isPopupTrigger()) 
         {
            popup_input.show(e.getComponent(), e.getX(), e.getY());
         }
      }
   } // class PopupListener_input extends MouseAdapter
   
   
   /* Creates new form main */
   public main() {

      try
      {
         for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
         {
            if ("Nimbus".equals(info.getName()))
            {
               UIManager.setLookAndFeel(info.getClassName());

               // NB onderstaande hier nog niet nodig omdat de componenten nog niet aangemaakt zijn (initComponents();)
               //SwingUtilities.updateComponentTreeUI(main.this);
               
               // NB onderstaande kan niet omdat jTextField4 nog niet gecreeerd
               //jTextField4.setBackground(new java.awt.Color(204, 255, 255));
               
               theme_mode = THEME_NIMBUS_DAY;

               break;
            }
         }
      }
      catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
      {
         // If Metal is not available, you can set the GUI to another look and feel.
         //JOptionPane.showMessageDialog(null, "Metal color schemenot supported on this computer", main.APPLICATION_NAME + " message", JOptionPane.WARNING_MESSAGE);
         try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }  // java default look and feel (voor Java 1.6 het zelde als:  UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");)
         catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) { }
      }

      initComponents();
      initComponents2();
      bepaal_frame_location();
      initImages();
       //read_muffin();                             // read configuration(station) data (in read_muffin: ook check op grootte immt log); NB if muffin not available reads meta data from confuguration file */
   }
    
   
    
   // Implement the SingleInstanceListener for your application
   class SISListener implements SingleInstanceListener 
   {
      @Override
      public void newActivation(String[] params) 
      {
            
         // your code to handle the new arguments here
         JOptionPane.showMessageDialog(null, "TurboWin+ is already running", main.APPLICATION_NAME, JOptionPane.ERROR_MESSAGE);   
      }
   }  // class SISListener implements SingleInstanceListener 
   
   
   



   /** This method is called from within the init() method to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jPanel1 = new javax.swing.JPanel();
      jToolBar1 = new javax.swing.JToolBar();
      jButton1 = new javax.swing.JButton();
      jButton2 = new javax.swing.JButton();
      jButton3 = new javax.swing.JButton();
      jButton6 = new javax.swing.JButton();
      jButton7 = new javax.swing.JButton();
      jButton8 = new javax.swing.JButton();
      jButton4 = new javax.swing.JButton();
      jButton5 = new javax.swing.JButton();
      jButton11 = new javax.swing.JButton();
      jButton9 = new javax.swing.JButton();
      jButton10 = new javax.swing.JButton();
      jButton12 = new javax.swing.JButton();
      jButton13 = new javax.swing.JButton();
      jButton14 = new javax.swing.JButton();
      jButton15 = new javax.swing.JButton();
      jButton16 = new javax.swing.JButton();
      jButton17 = new javax.swing.JButton();
      jButton18 = new javax.swing.JButton();
      jButton19 = new javax.swing.JButton();
      jButton20 = new javax.swing.JButton();
      jPanel2 = new javax.swing.JPanel();
      jLabel33 = new javax.swing.JLabel();
      jTextField33 = new javax.swing.JTextField();
      jLabel34 = new javax.swing.JLabel();
      jTextField34 = new javax.swing.JTextField();
      jLabel36 = new javax.swing.JLabel();
      jTextField35 = new javax.swing.JTextField();
      jLabel13 = new javax.swing.JLabel();
      jTextField13 = new javax.swing.JTextField();
      jLabel14 = new javax.swing.JLabel();
      jTextField14 = new javax.swing.JTextField();
      jLabel15 = new javax.swing.JLabel();
      jTextField15 = new javax.swing.JTextField();
      jLabel19 = new javax.swing.JLabel();
      jTextField21 = new javax.swing.JTextField();
      jLabel21 = new javax.swing.JLabel();
      jTextField19 = new javax.swing.JTextField();
      jLabel30 = new javax.swing.JLabel();
      jTextField30 = new javax.swing.JTextField();
      jLabel31 = new javax.swing.JLabel();
      jTextField31 = new javax.swing.JTextField();
      jLabel32 = new javax.swing.JLabel();
      jTextField32 = new javax.swing.JTextField();
      jLabel20 = new javax.swing.JLabel();
      jTextField20 = new javax.swing.JTextField();
      jPanel3 = new javax.swing.JPanel();
      jLabel38 = new javax.swing.JLabel();
      jTextField40 = new javax.swing.JTextField();
      jLabel16 = new javax.swing.JLabel();
      jTextField16 = new javax.swing.JTextField();
      jLabel22 = new javax.swing.JLabel();
      jTextField22 = new javax.swing.JTextField();
      jLabel24 = new javax.swing.JLabel();
      jTextField24 = new javax.swing.JTextField();
      jLabel25 = new javax.swing.JLabel();
      jTextField25 = new javax.swing.JTextField();
      jLabel27 = new javax.swing.JLabel();
      jTextField27 = new javax.swing.JTextField();
      jLabel29 = new javax.swing.JLabel();
      jTextField29 = new javax.swing.JTextField();
      jLabel17 = new javax.swing.JLabel();
      jTextField17 = new javax.swing.JTextField();
      jLabel23 = new javax.swing.JLabel();
      jTextField23 = new javax.swing.JTextField();
      jLabel26 = new javax.swing.JLabel();
      jTextField26 = new javax.swing.JTextField();
      jLabel28 = new javax.swing.JLabel();
      jTextField28 = new javax.swing.JTextField();
      jLabel18 = new javax.swing.JLabel();
      jTextField18 = new javax.swing.JTextField();
      jPanel4 = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      jTextField1 = new javax.swing.JTextField();
      jLabel2 = new javax.swing.JLabel();
      jTextField2 = new javax.swing.JTextField();
      jLabel3 = new javax.swing.JLabel();
      jTextField3 = new javax.swing.JTextField();
      jLabel5 = new javax.swing.JLabel();
      jTextField5 = new javax.swing.JTextField();
      jLabel7 = new javax.swing.JLabel();
      jTextField7 = new javax.swing.JTextField();
      jLabel9 = new javax.swing.JLabel();
      jTextField9 = new javax.swing.JTextField();
      jLabel10 = new javax.swing.JLabel();
      jTextField10 = new javax.swing.JTextField();
      jLabel11 = new javax.swing.JLabel();
      jTextField11 = new javax.swing.JTextField();
      jLabel12 = new javax.swing.JLabel();
      jTextField12 = new javax.swing.JTextField();
      jLabel35 = new javax.swing.JLabel();
      jTextField36 = new javax.swing.JTextField();
      jLabel37 = new javax.swing.JLabel();
      jTextField37 = new javax.swing.JTextField();
      jLabel40 = new javax.swing.JLabel();
      jTextField38 = new javax.swing.JTextField();
      jPanel5 = new javax.swing.JPanel();
      jSeparator1 = new javax.swing.JSeparator();
      jTextField4 = new javax.swing.JTextField();
      jLabel4 = new javax.swing.JLabel();
      jLabel8 = new javax.swing.JLabel();
      jLabel39 = new javax.swing.JLabel();
      jLabel6 = new javax.swing.JLabel();
      jMenuBar1 = new javax.swing.JMenuBar();
      jMenu1 = new javax.swing.JMenu();
      jMenuItem1 = new javax.swing.JMenuItem();
      jMenu2 = new javax.swing.JMenu();
      jMenuItem30 = new javax.swing.JMenuItem();
      jSeparator4 = new javax.swing.JSeparator();
      jMenuItem3 = new javax.swing.JMenuItem();
      jMenuItem4 = new javax.swing.JMenuItem();
      jMenuItem7 = new javax.swing.JMenuItem();
      jMenuItem8 = new javax.swing.JMenuItem();
      jMenuItem9 = new javax.swing.JMenuItem();
      jMenuItem5 = new javax.swing.JMenuItem();
      jMenuItem6 = new javax.swing.JMenuItem();
      jMenuItem12 = new javax.swing.JMenuItem();
      jMenuItem10 = new javax.swing.JMenuItem();
      jMenuItem11 = new javax.swing.JMenuItem();
      jMenuItem13 = new javax.swing.JMenuItem();
      jMenuItem14 = new javax.swing.JMenuItem();
      jMenuItem15 = new javax.swing.JMenuItem();
      jMenuItem16 = new javax.swing.JMenuItem();
      jMenuItem17 = new javax.swing.JMenuItem();
      jMenuItem18 = new javax.swing.JMenuItem();
      jMenuItem19 = new javax.swing.JMenuItem();
      jMenu3 = new javax.swing.JMenu();
      jMenuItem20 = new javax.swing.JMenuItem();
      jMenuItem23 = new javax.swing.JMenuItem();
      jMenuItem24 = new javax.swing.JMenuItem();
      jMenuItem46 = new javax.swing.JMenuItem();
      jMenuItem48 = new javax.swing.JMenuItem();
      jMenu4 = new javax.swing.JMenu();
      jMenuItem21 = new javax.swing.JMenuItem();
      jMenuItem25 = new javax.swing.JMenuItem();
      jMenuItem26 = new javax.swing.JMenuItem();
      jMenuItem50 = new javax.swing.JMenuItem();
      jMenuItem42 = new javax.swing.JMenuItem();
      jMenuItem52 = new javax.swing.JMenuItem();
      jMenuItem54 = new javax.swing.JMenuItem();
      jSeparator2 = new javax.swing.JSeparator();
      jMenuItem27 = new javax.swing.JMenuItem();
      jMenuItem28 = new javax.swing.JMenuItem();
      jSeparator3 = new javax.swing.JSeparator();
      jMenuItem2 = new javax.swing.JMenuItem();
      jMenuItem29 = new javax.swing.JMenuItem();
      jMenu5 = new javax.swing.JMenu();
      jMenuItem31 = new javax.swing.JMenuItem();
      jMenuItem32 = new javax.swing.JMenuItem();
      jMenuItem34 = new javax.swing.JMenuItem();
      jMenuItem22 = new javax.swing.JMenuItem();
      jMenu6 = new javax.swing.JMenu();
      jMenuItem33 = new javax.swing.JMenuItem();
      jMenuItem38 = new javax.swing.JMenuItem();
      jMenuItem39 = new javax.swing.JMenuItem();
      jMenuItem40 = new javax.swing.JMenuItem();
      jMenu8 = new javax.swing.JMenu();
      jMenuItem41 = new javax.swing.JMenuItem();
      jMenuItem43 = new javax.swing.JMenuItem();
      jMenuItem44 = new javax.swing.JMenuItem();
      jMenuItem45 = new javax.swing.JMenuItem();
      jMenuItem47 = new javax.swing.JMenuItem();
      jMenuItem51 = new javax.swing.JMenuItem();
      jMenu7 = new javax.swing.JMenu();
      jMenuItem36 = new javax.swing.JMenuItem();
      jMenuItem49 = new javax.swing.JMenuItem();
      jSeparator5 = new javax.swing.JPopupMenu.Separator();
      jMenuItem53 = new javax.swing.JMenuItem();
      jMenuItem35 = new javax.swing.JMenuItem();
      jSeparator6 = new javax.swing.JPopupMenu.Separator();
      jMenuItem37 = new javax.swing.JMenuItem();

      setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
      setFocusable(false);
      setMinimumSize(new java.awt.Dimension(1050, 740));
      setResizable(false);
      addWindowListener(new java.awt.event.WindowAdapter() {
         public void windowClosing(java.awt.event.WindowEvent evt) {
            main_windowClosing(evt);
         }
         public void windowIconified(java.awt.event.WindowEvent evt) {
            main_windowIconfied(evt);
         }
      });

      jToolBar1.setFloatable(false);
      jToolBar1.setRollover(true);

      jButton1.setToolTipText("Call sign / VOS ID");
      jButton1.setFocusable(false);
      jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton1.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            call_sign_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton1);

      jButton2.setToolTipText("Date and Time");
      jButton2.setFocusable(false);
      jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton2.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            date_time_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton2);

      jButton3.setToolTipText("Position, course and speed");
      jButton3.setFocusable(false);
      jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton3.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            position_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton3);

      jButton6.setToolTipText("Barometer reading");
      jButton6.setFocusable(false);
      jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton6.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            barometer_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton6);

      jButton7.setToolTipText("Barograph reading");
      jButton7.setFocusable(false);
      jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton7.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            barograph_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton7);

      jButton8.setToolTipText("Temperatures");
      jButton8.setFocusable(false);
      jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton8.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            temperatures_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton8);

      jButton4.setToolTipText("Wind");
      jButton4.setFocusable(false);
      jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton4.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            wind_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton4);

      jButton5.setToolTipText("Waves");
      jButton5.setFocusable(false);
      jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton5.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            waves_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton5);

      jButton11.setToolTipText("Visibility");
      jButton11.setFocusable(false);
      jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton11.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton11.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            visibility_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton11);

      jButton9.setToolTipText("Present weather");
      jButton9.setFocusable(false);
      jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton9.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            present_weather_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton9);

      jButton10.setToolTipText("Past weather");
      jButton10.setFocusable(false);
      jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton10.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            past_weather_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton10);

      jButton12.setToolTipText("Clouds low");
      jButton12.setFocusable(false);
      jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton12.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            cl_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton12);

      jButton13.setToolTipText("Clouds middle");
      jButton13.setFocusable(false);
      jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton13.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton13.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            cm_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton13);

      jButton14.setToolTipText("Clouds high");
      jButton14.setFocusable(false);
      jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton14.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton14.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            ch_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton14);

      jButton15.setToolTipText("Clouds cover and height");
      jButton15.setFocusable(false);
      jButton15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton15.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton15.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            height_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton15);

      jButton16.setToolTipText("Icing");
      jButton16.setFocusable(false);
      jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton16.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton16.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            icing_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton16);

      jButton17.setToolTipText("Ice");
      jButton17.setFocusable(false);
      jButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton17.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton17.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton17.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            ice_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton17);

      jButton18.setToolTipText("Observer");
      jButton18.setFocusable(false);
      jButton18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton18.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton18.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton18.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            observer_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton18);

      jButton19.setToolTipText("Captains");
      jButton19.setFocusable(false);
      jButton19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton19.setPreferredSize(new java.awt.Dimension(22, 22));
      jButton19.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton19.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            captain_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton19);

      jButton20.setToolTipText("next form automation");
      jButton20.setFocusable(false);
      jButton20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      jButton20.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
      jButton20.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            next_screen_toolbar_mouseClicked(evt);
         }
      });
      jToolBar1.add(jButton20);

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
      );

      jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

      jLabel33.setForeground(new java.awt.Color(0, 0, 255));
      jLabel33.setText("Cl");
      jLabel33.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            cl_mainscreen_mouseClicked(evt);
         }
      });

      jTextField33.setEditable(false);
      jTextField33.setFocusable(false);
      jTextField33.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            cl_mainscreen_mouseClicked(evt);
         }
      });

      jLabel34.setForeground(new java.awt.Color(0, 0, 255));
      jLabel34.setText("Cm");
      jLabel34.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            cm_mainscreen_mouseClicked(evt);
         }
      });

      jTextField34.setEditable(false);
      jTextField34.setFocusable(false);
      jTextField34.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            cm_mainscreen_mouseClicked(evt);
         }
      });

      jLabel36.setForeground(new java.awt.Color(0, 0, 255));
      jLabel36.setText("Ch");
      jLabel36.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            ch_mainscreen_mouseClicked(evt);
         }
      });

      jTextField35.setEditable(false);
      jTextField35.setFocusable(false);
      jTextField35.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            ch_mainscreen_mouseClicked(evt);
         }
      });

      jLabel13.setForeground(new java.awt.Color(0, 0, 255));
      jLabel13.setText("Present weather");
      jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            present_weather_mainscreen_mouseClicked(evt);
         }
      });

      jTextField13.setEditable(false);
      jTextField13.setFocusable(false);
      jTextField13.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            present_weather_mainscreen_mouseClicked(evt);
         }
      });

      jLabel14.setForeground(new java.awt.Color(0, 0, 255));
      jLabel14.setText("Past weath. 1st");
      jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            past_weather_1_mainscreen_mouseClicked(evt);
         }
      });

      jTextField14.setEditable(false);
      jTextField14.setFocusable(false);
      jTextField14.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            past_weather_1_mainscreen_mouseClicked(evt);
         }
      });

      jLabel15.setForeground(new java.awt.Color(0, 0, 255));
      jLabel15.setText("Past weath. 2nd");
      jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            past_weather_2_mainscreen_mouseClicked(evt);
         }
      });

      jTextField15.setEditable(false);
      jTextField15.setFocusable(false);
      jTextField15.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            past_weather_2_mainscreen_mouseClicked(evt);
         }
      });

      jLabel19.setForeground(new java.awt.Color(0, 0, 255));
      jLabel19.setText("Icing");
      jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            icing_mainscreen_mouseClicked(evt);
         }
      });

      jTextField21.setEditable(false);
      jTextField21.setFocusable(false);
      jTextField21.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            icing_mainscreen_mouseClicked(evt);
         }
      });

      jLabel21.setText("Ice");
      jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            ice_mainscreen_mouseClicked(evt);
         }
      });

      jTextField19.setEditable(false);
      jTextField19.setFocusable(false);
      jTextField19.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            ice_mainscreen_mouseClicked(evt);
         }
      });

      jLabel30.setForeground(new java.awt.Color(0, 0, 255));
      jLabel30.setText("Total cloud cov");
      jLabel30.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            total_cloud_cover_mainscreen_mouseClicked(evt);
         }
      });

      jTextField30.setEditable(false);
      jTextField30.setFocusable(false);
      jTextField30.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            total_cloud_cover_mainscreen_mouseClicked(evt);
         }
      });

      jLabel31.setForeground(java.awt.Color.blue);
      jLabel31.setText("Amount Cl (Cm)");
      jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            amount_cl_mainscreen_mouseClicked(evt);
         }
      });

      jTextField31.setEditable(false);
      jTextField31.setFocusable(false);
      jTextField31.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            amount_cl_mainscreen_mouseClicked(evt);
         }
      });

      jLabel32.setForeground(new java.awt.Color(0, 0, 255));
      jLabel32.setText("h lowest cloud");
      jLabel32.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            height_lowest_cloud_mainscreen_mouseClicked(evt);
         }
      });

      jTextField32.setEditable(false);
      jTextField32.setFocusable(false);
      jTextField32.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            height_lowest_cloud_mainscreen_mouseClicked(evt);
         }
      });

      jLabel20.setForeground(new java.awt.Color(0, 0, 255));
      jLabel20.setText("Observer");
      jLabel20.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            observer_mainscreen_mouseClicked(evt);
         }
      });

      jTextField20.setEditable(false);
      jTextField20.setFocusable(false);
      jTextField20.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            observer_mainscreen_mouseClicked(evt);
         }
      });

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(jLabel34)
                  .addComponent(jLabel19)
                  .addComponent(jLabel36)
                  .addComponent(jLabel33)
                  .addComponent(jLabel21)
                  .addComponent(jLabel14)
                  .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jLabel15)
                  .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
               .addComponent(jLabel20))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
      );

      jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextField13, jTextField14, jTextField15, jTextField19, jTextField20, jTextField21, jTextField30, jTextField31, jTextField32, jTextField33, jTextField34, jTextField35});

      jPanel2Layout.setVerticalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel13)
               .addComponent(jTextField13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel14)
               .addComponent(jTextField14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel15)
               .addComponent(jTextField15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel33))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel34)
               .addComponent(jTextField34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel36))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel30)
               .addComponent(jTextField30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel31)
               .addComponent(jTextField31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel32))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel19)
               .addComponent(jTextField21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel21))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel20))
            .addContainerGap())
      );

      jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

      jLabel38.setText("Seawater temp");
      jLabel38.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            seawater_temp_mainscreen_mouseClicked(evt);
         }
      });

      jTextField40.setEditable(false);
      jTextField40.setFocusable(false);
      jTextField40.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            seawater_temp_mainscreen_mouseClicked(evt);
         }
      });

      jLabel16.setText("True wind speed");
      jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            true_wind_speed_mainscreen_mouseClicked(evt);
         }
      });

      jTextField16.setEditable(false);
      jTextField16.setFocusable(false);
      jTextField16.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            true_wind_speed_mainscreen_mouseClicked(evt);
         }
      });

      jLabel22.setForeground(java.awt.Color.blue);
      jLabel22.setText("(Wind) wave ht");
      jLabel22.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            wind_wave_height_mainscreen_mouseClicked(evt);
         }
      });

      jTextField22.setEditable(false);
      jTextField22.setFocusable(false);
      jTextField22.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            wind_wave_height_mainscreen_mouseClicked(evt);
         }
      });

      jLabel24.setForeground(new java.awt.Color(0, 0, 255));
      jLabel24.setText("1st swell dir");
      jLabel24.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_1_dir_mainscreen_mouseClicked(evt);
         }
      });

      jTextField24.setEditable(false);
      jTextField24.setFocusable(false);
      jTextField24.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_1_dir_mainscreen_mouseClicked(evt);
         }
      });

      jLabel25.setForeground(new java.awt.Color(0, 0, 255));
      jLabel25.setText("1st swell height");
      jLabel25.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_1_height_mainscreen_mouseClicked(evt);
         }
      });

      jTextField25.setEditable(false);
      jTextField25.setFocusable(false);
      jTextField25.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_1_height_mainscreen_mouseClicked(evt);
         }
      });

      jLabel27.setForeground(new java.awt.Color(0, 0, 255));
      jLabel27.setText("2nd swell dir");
      jLabel27.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_2_dir_mainscreen_mouseClicked(evt);
         }
      });

      jTextField27.setEditable(false);
      jTextField27.setFocusable(false);
      jTextField27.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_2_dir_mainscreen_mouseClicked(evt);
         }
      });

      jLabel29.setForeground(new java.awt.Color(0, 0, 255));
      jLabel29.setText("2nd swell period");
      jLabel29.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_2_period_mainscreen_mouseClicked(evt);
         }
      });

      jTextField29.setEditable(false);
      jTextField29.setFocusable(false);
      jTextField29.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_2_period_mainscreen_mouseClicked(evt);
         }
      });

      jLabel17.setText("True wind dir");
      jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            true_wind_dir_mainscreen_mouseClicked(evt);
         }
      });

      jTextField17.setEditable(false);
      jTextField17.setFocusable(false);
      jTextField17.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            true_wind_dir_mainscreen_mouseClicked(evt);
         }
      });

      jLabel23.setForeground(new java.awt.Color(0, 0, 255));
      jLabel23.setText("(Wind) wave per");
      jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            wind_wave_period_mainscreen_mouseClicked(evt);
         }
      });

      jTextField23.setEditable(false);
      jTextField23.setFocusable(false);
      jTextField23.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            wind_wave_period_mainscreen_mouseClicked(evt);
         }
      });

      jLabel26.setForeground(new java.awt.Color(0, 0, 255));
      jLabel26.setText("1st swell period");
      jLabel26.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_1_period_mainscreen_mouseClicked(evt);
         }
      });

      jTextField26.setEditable(false);
      jTextField26.setFocusable(false);
      jTextField26.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_1_period_mainscreen_mouseClicked(evt);
         }
      });

      jLabel28.setForeground(new java.awt.Color(0, 0, 255));
      jLabel28.setText("2nd swell height");
      jLabel28.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_2_height_mainscreen_mouseClicked(evt);
         }
      });

      jTextField28.setEditable(false);
      jTextField28.setFocusable(false);
      jTextField28.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            swell_2_height_mainscreen_mouseClicked(evt);
         }
      });

      jLabel18.setForeground(java.awt.Color.blue);
      jLabel18.setText("Visibility");
      jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            visibility_mainscreen_mouseClicked(evt);
         }
      });

      jTextField18.setEditable(false);
      jTextField18.setFocusable(false);
      jTextField18.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            visibility_mainscreen_mouseClicked(evt);
         }
      });

      javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
      jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jLabel17)
               .addComponent(jLabel23)
               .addComponent(jLabel25)
               .addComponent(jLabel16)
               .addComponent(jLabel24)
               .addComponent(jLabel27)
               .addComponent(jLabel38)
               .addComponent(jLabel26)
               .addComponent(jLabel29)
               .addComponent(jLabel28)
               .addComponent(jLabel22)
               .addComponent(jLabel18))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jTextField40, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextField16, jTextField17, jTextField18, jTextField22, jTextField23, jTextField24, jTextField25, jTextField26, jTextField27, jTextField28, jTextField29, jTextField40});

      jPanel3Layout.setVerticalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel38))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel17))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel16))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel23)
               .addComponent(jTextField23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel22))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel24)
               .addComponent(jTextField24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel26)
               .addComponent(jTextField26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel25)
               .addComponent(jTextField25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel27)
               .addComponent(jTextField27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel29)
               .addComponent(jTextField29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel28)
               .addComponent(jTextField28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel18)
               .addComponent(jTextField18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

      jLabel1.setText("Call sign");
      jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            call_sign_mainscreen_mouseClicked(evt);
         }
      });

      jTextField1.setEditable(false);
      jTextField1.setFocusable(false);
      jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            call_sign_mainscreen_mouseClicked(evt);
         }
      });

      jLabel2.setText("Masked call sign");
      jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            masked_call_sign_mainscreen_mouseClicked(evt);
         }
      });

      jTextField2.setEditable(false);
      jTextField2.setFocusable(false);
      jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            masked_call_sign_mainscreen_mouseClicked(evt);
         }
      });

      jLabel3.setText("Date & Time obs");
      jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            date_time_mainscreen_mouseClicked(evt);
         }
      });

      jTextField3.setEditable(false);
      jTextField3.setFocusable(false);
      jTextField3.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            date_time_mainscreen_mouseClicked(evt);
         }
      });

      jLabel5.setText("Position");
      jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            position_mainscreen_mouseClicked(evt);
         }
      });

      jTextField5.setEditable(false);
      jTextField5.setFocusable(false);
      jTextField5.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            position_mainscreen_mouseClicked(evt);
         }
      });

      jLabel7.setText("Course & Speed");
      jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            course_speed_mainscreen_mouseClicked(evt);
         }
      });

      jTextField7.setEditable(false);
      jTextField7.setFocusable(false);
      jTextField7.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            course_speed_mainscreen_mouseClicked(evt);
         }
      });

      jLabel9.setText("Pressure (read+ic)");
      jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            pressure_read_mainscreen_mouseClicked(evt);
         }
      });

      jTextField9.setEditable(false);
      jTextField9.setFocusable(false);
      jTextField9.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            pressure_read_mainscreen_mouseClicked(evt);
         }
      });

      jLabel10.setText("Pressure (MSL)");
      jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            pressure_msl_mainscreen_mouseClicked(evt);
         }
      });

      jTextField10.setEditable(false);
      jTextField10.setFocusable(false);
      jTextField10.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            pressure_msl_mainscreen_mouseClicked(evt);
         }
      });

      jLabel11.setText("Pressure tendency");
      jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            amount_pressure_tendency_mainscreen_mouseClicked(evt);
         }
      });

      jTextField11.setEditable(false);
      jTextField11.setFocusable(false);
      jTextField11.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            amount_pressure_tendency_mainscreen_mouseClicked(evt);
         }
      });

      jLabel12.setText("Char. press. tend.");
      jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            char_pressure_tendency_mainscreen_mouseClicked(evt);
         }
      });

      jTextField12.setEditable(false);
      jTextField12.setFocusable(false);
      jTextField12.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            char_pressure_tendency_mainscreen_mouseClicked(evt);
         }
      });

      jLabel35.setText("Air temp");
      jLabel35.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            air_temp_mainscreen_mouseClicked(evt);
         }
      });

      jTextField36.setEditable(false);
      jTextField36.setFocusable(false);
      jTextField36.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            air_temp_mainscreen_mouseClicked(evt);
         }
      });

      jLabel37.setText("Wet-bulb temp");
      jLabel37.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            wet_bulb_temp_mainscreen_mouseClicked(evt);
         }
      });

      jTextField37.setEditable(false);
      jTextField37.setHorizontalAlignment(javax.swing.JTextField.LEFT);
      jTextField37.setFocusable(false);
      jTextField37.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            wet_bulb_temp_mainscreen_mouseClicked(evt);
         }
      });

      jLabel40.setText("Dew point");
      jLabel40.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            dew_point_mainscreen_mouseClicked(evt);
         }
      });

      jTextField38.setEditable(false);
      jTextField38.setFocusable(false);
      jTextField38.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            dew_point_mainscreen_mouseClicked(evt);
         }
      });

      javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
      jPanel4.setLayout(jPanel4Layout);
      jPanel4Layout.setHorizontalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jLabel1)
               .addComponent(jLabel7)
               .addComponent(jLabel5)
               .addComponent(jLabel9)
               .addComponent(jLabel12)
               .addComponent(jLabel10)
               .addComponent(jLabel3)
               .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
               .addComponent(jLabel35)
               .addComponent(jLabel37)
               .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
      );

      jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jTextField1, jTextField10, jTextField11, jTextField12, jTextField2, jTextField3, jTextField36, jTextField37, jTextField38, jTextField5, jTextField7, jTextField9});

      jPanel4Layout.setVerticalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel1)
               .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel2))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel3))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel5))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel7))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel9))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel11))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel12))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel35)
               .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel37)
               .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel40))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
      jPanel5.setOpaque(false);

      jTextField4.setEditable(false);
      jTextField4.setFocusable(false);

      jLabel4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel4.setText("Turbo+");

      jLabel8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel8.setText("--- when minimised see system tray ---");

      jLabel39.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel39.setText("--- adding data: input menu, popup menu, toolbar icons or click on the text labels or fields ---");

      jLabel6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel6.setText("-");

      javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
      jPanel5.setLayout(jPanel5Layout);
      jPanel5Layout.setHorizontalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel5Layout.createSequentialGroup()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel5Layout.createSequentialGroup()
                  .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                     .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 931, Short.MAX_VALUE)
                     .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 931, Short.MAX_VALUE)
                     .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 931, Short.MAX_VALUE)
                     .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                  .addGap(0, 10, Short.MAX_VALUE))
               .addGroup(jPanel5Layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap())
      );

      jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel4, jSeparator1, jTextField4});

      jPanel5Layout.setVerticalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel5Layout.createSequentialGroup()
            .addComponent(jLabel8)
            .addGap(6, 6, 6)
            .addComponent(jLabel39)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jLabel4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel6)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jMenuBar1.setMaximumSize(new java.awt.Dimension(200, 21));
      jMenuBar1.setPreferredSize(new java.awt.Dimension(200, 21));

      jMenu1.setText("File");

      jMenuItem1.setText("Exit");
      jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            File_Exit_menu_actionPerformd(evt);
         }
      });
      jMenu1.add(jMenuItem1);

      jMenuBar1.add(jMenu1);

      jMenu2.setText("Input");

      jMenuItem30.setText("Next form automation");
      jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Next_form_automation_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem30);
      jMenu2.add(jSeparator4);

      jMenuItem3.setText("Date & Time...");
      jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_DateTime_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem3);

      jMenuItem4.setText("Position, Course & Speed...");
      jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Position_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem4);

      jMenuItem7.setText("Barometer reading...");
      jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Barometer_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem7);

      jMenuItem8.setText("Barograph reading...");
      jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Barograph_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem8);

      jMenuItem9.setText("Temperatures...");
      jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Temperatures_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem9);

      jMenuItem5.setText("Wind...");
      jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Wind_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem5);

      jMenuItem6.setText("Waves...");
      jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_waves_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem6);

      jMenuItem12.setText("Visibility...");
      jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Visibility_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem12);

      jMenuItem10.setText("Present weather...");
      jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Presentweather_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem10);

      jMenuItem11.setText("Past weather...");
      jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Pastweather_menu_actionperformed(evt);
         }
      });
      jMenu2.add(jMenuItem11);

      jMenuItem13.setText("Clouds low...");
      jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Cloudslow_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem13);

      jMenuItem14.setText("Clouds middle...");
      jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Cloudsmiddle_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem14);

      jMenuItem15.setText("Clouds high...");
      jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Cloudshigh_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem15);

      jMenuItem16.setText("Cloud cover & height...");
      jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Cloudcover_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem16);

      jMenuItem17.setText("Icing...");
      jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Icing_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem17);

      jMenuItem18.setText("Ice...");
      jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Ice_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem18);

      jMenuItem19.setText("Observer...");
      jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Input_Observer_menu_actionPerformed(evt);
         }
      });
      jMenu2.add(jMenuItem19);

      jMenuBar1.add(jMenu2);

      jMenu3.setText("Output");
      jMenu3.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Output_obs_by_email_actionPerformed(evt);
         }
      });

      jMenuItem20.setText("Obs to server (internet)");
      jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Output_Obs_to_server_menu_actionPerformed(evt);
         }
      });
      jMenu3.add(jMenuItem20);

      jMenuItem23.setText("Obs by E-mail (internet)...");
      jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Output_obs_by_email_actionPerformed(evt);
         }
      });
      jMenu3.add(jMenuItem23);

      jMenuItem24.setText("Obs to file...");
      jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Output_obs_to_file_actionPerformed(evt);
         }
      });
      jMenu3.add(jMenuItem24);

      jMenuItem46.setText("Obs to AWS");
      jMenuItem46.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Output_obs_to_AWS_actionPerformed(evt);
         }
      });
      jMenu3.add(jMenuItem46);

      jMenuItem48.setText("Obs to clipboard");
      jMenuItem48.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Output_obs_to_clipboard_actionPerformed(evt);
         }
      });
      jMenu3.add(jMenuItem48);

      jMenuBar1.add(jMenu3);

      jMenu4.setText("Maintenance");
      jMenu4.setPreferredSize(new java.awt.Dimension(80, 19));

      jMenuItem21.setText("Station data...");
      jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Maintenance_Stationdata_actionPerformed(evt);
         }
      });
      jMenu4.add(jMenuItem21);

      jMenuItem25.setText("E-mail settings...");
      jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Maintenance_Email_settings_actionPerformed(evt);
         }
      });
      jMenu4.add(jMenuItem25);

      jMenuItem26.setText("Log files settings...");
      jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Maintenance_Log_files_actionPerformed(evt);
         }
      });
      jMenu4.add(jMenuItem26);

      jMenuItem50.setText("Obs format setting...");
      jMenuItem50.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Maintenance_obs_format_actionPerformed(evt);
         }
      });
      jMenu4.add(jMenuItem50);

      jMenuItem42.setText("Serial/USB/WiFi connecton settings...");
      jMenuItem42.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Maintenance_Serial_actionPerformed(evt);
         }
      });
      jMenu4.add(jMenuItem42);

      jMenuItem52.setText("WOW/APR settings...");
      jMenuItem52.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Maintenance_WOW_settings_actionPerformed(evt);
         }
      });
      jMenu4.add(jMenuItem52);

      jMenuItem54.setText("Server settings...");
      jMenuItem54.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Maintenance_server_settings_actionperformed(evt);
         }
      });
      jMenu4.add(jMenuItem54);
      jMenu4.add(jSeparator2);

      jMenuItem27.setText("Observers...");
      jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Maintenance_Observer_menu_actionPerformed(evt);
         }
      });
      jMenu4.add(jMenuItem27);

      jMenuItem28.setText("Captains...");
      jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Maintenance_Captains_Menu_actionPerformed(evt);
         }
      });
      jMenu4.add(jMenuItem28);
      jMenu4.add(jSeparator3);

      jMenuItem2.setText("Move log files to (floppy/USB) disk...");
      jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Maintenance_Move_log_files_to_disk_actionPerformed(evt);
         }
      });
      jMenu4.add(jMenuItem2);

      jMenuItem29.setText("Move log files by E-mail");
      jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Maintenance_Move_log_files_by_email_actionPerformed(evt);
         }
      });
      jMenu4.add(jMenuItem29);

      jMenuBar1.add(jMenu4);

      jMenu5.setText("Themes");

      jMenuItem31.setText("Day");
      jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Themes_1_actionPerformed(evt);
         }
      });
      jMenu5.add(jMenuItem31);

      jMenuItem32.setText("Night");
      jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Themes_2_actionPerformed(evt);
         }
      });
      jMenu5.add(jMenuItem32);

      jMenuItem34.setText("Sunrise");
      jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Themes_3_actionPerformed(evt);
         }
      });
      jMenu5.add(jMenuItem34);

      jMenuItem22.setText("Sunset");
      jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Themes_4_actionPerformed(evt);
         }
      });
      jMenu5.add(jMenuItem22);

      jMenuBar1.add(jMenu5);

      jMenu6.setText("Amver");

      jMenuItem33.setText("Sailing Plan...");
      jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Amver_SailingPlan_actionPerformed(evt);
         }
      });
      jMenu6.add(jMenuItem33);

      jMenuItem38.setText("Deviation Report...");
      jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Amver_DeviationReport_actionPerformed(evt);
         }
      });
      jMenu6.add(jMenuItem38);

      jMenuItem39.setText("Arrival Report...");
      jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Amver_ArrivalReport_actionPerformed(evt);
         }
      });
      jMenu6.add(jMenuItem39);

      jMenuItem40.setText("Position Report...");
      jMenuItem40.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Amver_PositionReport_actionPerformed(evt);
         }
      });
      jMenu6.add(jMenuItem40);

      jMenuBar1.add(jMenu6);

      jMenu8.setText("Graphs");

      jMenuItem41.setText("Sensor data pressure");
      jMenuItem41.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Graphs_Pressure_Sensor_Data_actionPerformed(evt);
         }
      });
      jMenu8.add(jMenuItem41);

      jMenuItem43.setText("Sensor data air temp");
      jMenuItem43.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Graphs_Airtemp_Sensor_Data_actionPerformed(evt);
         }
      });
      jMenu8.add(jMenuItem43);

      jMenuItem44.setText("Sensor data SST");
      jMenuItem44.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Graphs_SST_Sensor_data_actionPerformed(evt);
         }
      });
      jMenu8.add(jMenuItem44);

      jMenuItem45.setText("Sensor data wind speed");
      jMenuItem45.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Graphs_Wind_Speed_Sensor_Data_actionPerformed(evt);
         }
      });
      jMenu8.add(jMenuItem45);

      jMenuItem47.setText("Sensor data wind dir");
      jMenuItem47.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Graph_Wind_Dir_Sensor_Data_actionPerformed(evt);
         }
      });
      jMenu8.add(jMenuItem47);

      jMenuItem51.setText("Sensor data all");
      jMenuItem51.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Graph_All_Sensor_Data_actionPerformed(evt);
         }
      });
      jMenu8.add(jMenuItem51);

      jMenuBar1.add(jMenu8);

      jMenu7.setText("Info");

      jMenuItem36.setText("Statistics (internet)");
      jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Info_Statistics_menu_actionPerformed(evt);
         }
      });
      jMenu7.add(jMenuItem36);

      jMenuItem49.setText("Calculator...");
      jMenuItem49.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Info_Calculator_menu_actionPerformed(evt);
         }
      });
      jMenu7.add(jMenuItem49);
      jMenu7.add(jSeparator5);

      jMenuItem53.setText("System log");
      jMenuItem53.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Info_System_Log_menu_actionPerformed(evt);
         }
      });
      jMenu7.add(jMenuItem53);

      jMenuItem35.setText("send System logs");
      jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Info_send_System_log_menu_actionperformed(evt);
         }
      });
      jMenu7.add(jMenuItem35);
      jMenu7.add(jSeparator6);

      jMenuItem37.setText("About...");
      jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Info_About_menu_actionPerformed(evt);
         }
      });
      jMenu7.add(jMenuItem37);

      jMenuBar1.add(jMenu7);

      setJMenuBar(jMenuBar1);

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addGroup(layout.createSequentialGroup()
            .addGap(33, 33, 33)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addGroup(layout.createSequentialGroup()
                  .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(72, 72, 72))
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
               .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
      );
   }// </editor-fold>//GEN-END:initComponents



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public ImageIcon createImageIcon(String path_and_file)
   {
      URL url = null;

      try
      {
         url = getClass().getResource(path_and_file);
      }
      catch (Exception e) { /* ... */}

      ImageIcon icon_glyph = new javax.swing.ImageIcon(url);

      return icon_glyph;
   }



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/

   // The doInBackground method, which creates the image icon for the photograph, is invoked by the background thread.
   // After the image icon is fully loaded, the done method is invoked on the event-dispatching thread.
   // This updates the GUI to display the photograph

   // SwingWorker is only designed to be executed once. Executing a SwingWorker more than once will not result in invoking the doInBackground method twice.
   // see: http://java.sun.com/javase/6/docs/api/javax/swing/SwingWorker.html
   private void loadImage(final String imagePath)
   {
      new SwingWorker<ImageIcon, Object>()
      {
         @Override
         public ImageIcon doInBackground()
         {
             return createImageIcon(imagePath);
         }

         @Override
         public void done()
         {
            try
            {
               // toolbar icons
               //
               //if (imagePath.equals(main.ICONS_DIRECTORY_R + java.io.File.separator + "call_sign.png"))
               if (imagePath.equals(main.ICONS_DIRECTORY + "call_sign.png"))
               {
                  ImageIcon toolbar_img_call_sign = get();
                  jButton1.setIcon(toolbar_img_call_sign);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "date_time.png"))
               {
                  ImageIcon toolbar_img_date_time = get();
                  jButton2.setIcon(toolbar_img_date_time);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "position.png"))
               {
                  ImageIcon toolbar_img_position = get();
                  jButton3.setIcon(toolbar_img_position);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "wind.png"))
               {
                  ImageIcon toolbar_img_wind = get();
                  jButton4.setIcon(toolbar_img_wind);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "waves.png"))
               {
                  ImageIcon toolbar_img_waves = get();
                  jButton5.setIcon(toolbar_img_waves);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "barometer.png"))
               {
                  ImageIcon toolbar_img_barometer = get();
                  jButton6.setIcon(toolbar_img_barometer);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "barograph.png"))
               {
                  ImageIcon toolbar_img_barograph = get();
                  jButton7.setIcon(toolbar_img_barograph);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "temperatures.png"))
               {
                  ImageIcon toolbar_img_temperatures = get();
                  jButton8.setIcon(toolbar_img_temperatures);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "present_weather.png"))
               {
                  ImageIcon toolbar_img_present_weather = get();
                  jButton9.setIcon(toolbar_img_present_weather);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "past_weather.png"))
               {
                  ImageIcon toolbar_img_past_weather = get();
                  jButton10.setIcon(toolbar_img_past_weather);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "visibility.png"))
               {
                  ImageIcon toolbar_img_visibility = get();
                  jButton11.setIcon(toolbar_img_visibility);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "cl.png"))
               {
                  ImageIcon toolbar_img_cl = get();
                  jButton12.setIcon(toolbar_img_cl);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "cm.png"))
               {
                  ImageIcon toolbar_img_cm = get();
                  jButton13.setIcon(toolbar_img_cm);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "ch.png"))
               {
                  ImageIcon toolbar_img_ch = get();
                  jButton14.setIcon(toolbar_img_ch);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "height.png"))
               {
                  ImageIcon toolbar_img_clouds_height = get();
                  jButton15.setIcon(toolbar_img_clouds_height);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "icing.png"))
               {
                  ImageIcon toolbar_img_icing = get();
                  jButton16.setIcon(toolbar_img_icing);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "ice.png"))
               {
                  ImageIcon toolbar_img_ice = get();
                  jButton17.setIcon(toolbar_img_ice);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "observers.png"))
               {
                  ImageIcon toolbar_img_observers = get();
                  jButton18.setIcon(toolbar_img_observers);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "captains.png"))
               {
                  ImageIcon toolbar_img_captains = get();
                  jButton19.setIcon(toolbar_img_captains);
               }
               else if (imagePath.equals(main.ICONS_DIRECTORY + "next_screen.png"))
               {
                  ImageIcon toolbar_img_next_screen = get();
                  jButton20.setIcon(toolbar_img_next_screen);
               }

            } // try
            catch (InterruptedException ignore) { }
            catch (java.util.concurrent.ExecutionException e)
            {
                String why = null;
                Throwable cause = e.getCause();
                if (cause != null)
                {
                   why = cause.getMessage();
                }
                else
                {
                   why = e.getMessage();
                }
                //System.err.println("Error retrieving file: " + why);
                JOptionPane.showMessageDialog(null, "Error retrieving toolbar icon file: " + why, main.APPLICATION_NAME, JOptionPane.ERROR_MESSAGE);
            } // catch
         } //  public void done()
      }.execute();
   } // private void loadImage(final String imagePath, final int index)




   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void initImages()
   {
      //loadImage(main.ICONS_DIRECTORY_R + java.io.File.separator + "call_sign.png");
      loadImage(main.ICONS_DIRECTORY + "call_sign.png");
      loadImage(main.ICONS_DIRECTORY + "date_time.png");
      loadImage(main.ICONS_DIRECTORY + "position.png");
      loadImage(main.ICONS_DIRECTORY + "wind.png");
      loadImage(main.ICONS_DIRECTORY + "waves.png");
      loadImage(main.ICONS_DIRECTORY + "barometer.png");
      loadImage(main.ICONS_DIRECTORY + "barograph.png");
      loadImage(main.ICONS_DIRECTORY + "temperatures.png");
      loadImage(main.ICONS_DIRECTORY + "present_weather.png");
      loadImage(main.ICONS_DIRECTORY + "past_weather.png");
      loadImage(main.ICONS_DIRECTORY + "visibility.png");
      loadImage(main.ICONS_DIRECTORY + "cl.png");
      loadImage(main.ICONS_DIRECTORY + "cm.png");
      loadImage(main.ICONS_DIRECTORY + "ch.png");
      loadImage(main.ICONS_DIRECTORY + "height.png");
      loadImage(main.ICONS_DIRECTORY + "icing.png");
      loadImage(main.ICONS_DIRECTORY + "ice.png");
      loadImage(main.ICONS_DIRECTORY + "observers.png");
      loadImage(main.ICONS_DIRECTORY + "captains.png");
      loadImage(main.ICONS_DIRECTORY + "next_screen.png");

   }


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
public void set_datetime_while_collecting_sensor_data()
{
   // called from: Function RS232_initComponents() [file main.java]
   //
   // was called in the every-5-minutes-collecting sensor data timer loop (Function init_sensor_data_uit_file_ophalen_timer() [RS232_view.java])
   // Never mind the computer is set to UTC or not. SimpleTimeZone with argument UTC convert system date time
   // always to UTC !!
   //

   String system_month_volledig = "";
   int len;

   cal_systeem_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // system date time in UTC of this moment
   cal_systeem_datum_tijd.getTime();                                             // now effective
   int system_minute = cal_systeem_datum_tijd.get(Calendar.MINUTE);
      
   if (system_minute % 5 == 0)
   {   
      if (system_minute > 30)
      {
         cal_systeem_datum_tijd.add(Calendar.HOUR, 1);   // add 1 hour
         cal_systeem_datum_tijd.getTime();
      }

      int system_year          = cal_systeem_datum_tijd.get(Calendar.YEAR);
      int system_month         = cal_systeem_datum_tijd.get(Calendar.MONTH);        // The first month of the year is JANUARY which is 0
      int system_day_of_month  = cal_systeem_datum_tijd.get(Calendar.DAY_OF_MONTH); // The first day of the month has value 1
      int system_hour_of_day   = cal_systeem_datum_tijd.get(Calendar.HOUR_OF_DAY);


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

      // update date and time on TurboWin+ main screen 
      date_time_fields_update();

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
   } // if (system_minute % 5 == 0)
}   
*/   
   

/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*  
public static void check_and_set_datetime()
{
   // Never mind the computer is set to UTC or not. SimpleTimeZone with argument UTC convert system date time
   // always to UTC !!
   //

   String system_month_volledig = "";
   int len;

   //if (time_zone_computer.trim().equals(TIME_ZONE_COMPUTER_UTC) == true)
   //{
      //JOptionPane.showMessageDialog(null, "time zone = UTC",  main.APPLICATION_NAME + " test", JOptionPane.WARNING_MESSAGE);
      //GregorianCalendar cal_systeem_datum_tijd;
      cal_systeem_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // gives system date and time in UTC of this moment
      cal_systeem_datum_tijd.getTime();                                 // effectueren

      int system_minute        = cal_systeem_datum_tijd.get(Calendar.MINUTE);

      
      
      if (system_minute > 30)
      {
         cal_systeem_datum_tijd.add(Calendar.HOUR_OF_DAY, 1);   // add 1 hour
         cal_systeem_datum_tijd.getTime();
      }

      int system_year          = cal_systeem_datum_tijd.get(Calendar.YEAR);
      int system_month         = cal_systeem_datum_tijd.get(Calendar.MONTH);        // The first month of the year is JANUARY which is 0
      int system_day_of_month  = cal_systeem_datum_tijd.get(Calendar.DAY_OF_MONTH); // The first day of the month has value 1
      int system_hour_of_day   = cal_systeem_datum_tijd.get(Calendar.HOUR_OF_DAY);  // HOUR_OF_DAY: 24 hour clock; HOUR : 12 hour clock


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

      String datum_tijd_string = system_month_volledig + " " +
                                 system_day_of_month + ", " +
                                 system_year + " " +
                                 system_hour_of_day + ".00 UTC";
      
      

      if (JOptionPane.showConfirmDialog(null, datum_tijd_string, "Date and time of observation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION)
      {
         // So date time of the confirmation message confirmed by the user

         mydatetime.year  = Integer.toString(system_year);                     // for progress main screen and IMMT
         mydatetime.month = system_month_volledig;                             // for progress main screen
         mydatetime.day   = Integer.toString(system_day_of_month);             // for progress main screen
         mydatetime.hour  = Integer.toString(system_hour_of_day);              // for progress main screen

         // update date and time on TurboWin+ main screen 
         date_time_fields_update();


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
         
         use_system_date_time_for_updating = true;
         
      } // if (JOptionPane.showConfirmDialog(null, datum_tijd_string, "Date and time of observation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION)
      else
      {
         use_system_date_time_for_updating = false;   // see Function: main_RS232_RS422.set_datetime_while_collecting_sensor_data()
      }
      


      // clear memory as soon as possible 
      //cal_systeem_datum_tijd         = null;

   //}
   //else
   //{
   //   JOptionPane.showMessageDialog(null, "time zone = NOT UTC: " + time_zone_computer,  main.APPLICATION_NAME + " test", JOptionPane.WARNING_MESSAGE);
   //
   //}

}
*/

   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static String convert_month(int month_number)   
{
   String month_name = "";
   
   
   if (month_number == 0)
   {
      month_name = "January";
   }
   else if (month_number == 1)
   {
      month_name = "February";
   }
   if (month_number == 2)
   {
      month_name = "March";
   }
   if (month_number == 3)
   {
      month_name = "April";
   }
   if (month_number == 4)
   {
      month_name = "May";
   }
   if (month_number == 5)
   {
      month_name = "June";
   }
   if (month_number == 6)
   {
      month_name = "July";
   }
   if (month_number == 7)
   {
      month_name = "August";
   }
   if (month_number == 8)
   {
      month_name = "September";
   }
   if (month_number == 9)
   {
      month_name = "October";
   }
   if (month_number == 10)
   {
      month_name = "November";
   }
   if (month_number == 11)
   {
      month_name = "December";
   }
   
   
   return month_name;
}
   
   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void check_and_set_datetime_v2()
{
   // NB Never mind the computer is set to UTC or not. SimpleTimeZone with argument UTC convert system date time
   //    always to UTC !!
   //
   String hulp_system_minute_LT = "";
   String hulp_system_minute_UTC = "";
   int len;

   
   ///////// local computer time (LT) (eg http://www.egmdss.com/gmdss-courses/mod/resource/view.php?id=2231)
   //
   cal_systeem_datum_tijd_LT = new GregorianCalendar();                                // Constructs a default GregorianCalendar using the current time in the default time zone with the default locale.
   cal_systeem_datum_tijd_LT.getTime();                                                // effectueren

   int system_year_LT          = cal_systeem_datum_tijd_LT.get(Calendar.YEAR);
   int system_month_LT         = cal_systeem_datum_tijd_LT.get(Calendar.MONTH);        // The first month of the year is JANUARY which is 0
   int system_day_of_month_LT  = cal_systeem_datum_tijd_LT.get(Calendar.DAY_OF_MONTH); // The first day of the month has value 1
   int system_hour_of_day_LT   = cal_systeem_datum_tijd_LT.get(Calendar.HOUR_OF_DAY);  // HOUR_OF_DAY: 24 hour clock; HOUR : 12 hour clock
   int system_minute_LT        = cal_systeem_datum_tijd_LT.get(Calendar.MINUTE);
   
   if (system_minute_LT <= 9)
   {
      hulp_system_minute_LT = "0" + Integer.toString(system_minute_LT);
   }
   else
   {
      hulp_system_minute_LT = Integer.toString(system_minute_LT);
   }

   String system_LT_string = "system: " + convert_month(system_month_LT) + " " + system_day_of_month_LT + ", " + system_year_LT + " " + system_hour_of_day_LT + "." + hulp_system_minute_LT + " LT";
      
   
   ///////// UTC computer time
   //
   cal_systeem_datum_tijd_UTC = new GregorianCalendar(new SimpleTimeZone(0, "UTC")); // gives system date and time in UTC of this moment
   cal_systeem_datum_tijd_UTC.getTime();                                 // effectueren

   int system_year_UTC          = cal_systeem_datum_tijd_UTC.get(Calendar.YEAR);
   int system_month_UTC         = cal_systeem_datum_tijd_UTC.get(Calendar.MONTH);        // The first month of the year is JANUARY which is 0
   int system_day_of_month_UTC  = cal_systeem_datum_tijd_UTC.get(Calendar.DAY_OF_MONTH); // The first day of the month has value 1
   int system_hour_of_day_UTC   = cal_systeem_datum_tijd_UTC.get(Calendar.HOUR_OF_DAY);  // HOUR_OF_DAY: 24 hour clock; HOUR : 12 hour clock
   int system_minute_UTC        = cal_systeem_datum_tijd_UTC.get(Calendar.MINUTE);

   if (system_minute_UTC <= 9)
   {
      hulp_system_minute_UTC = "0" + Integer.toString(system_minute_UTC);
   }
   else
   {
      hulp_system_minute_UTC = Integer.toString(system_minute_UTC);
   }   
   
   String system_UTC_string = "system: " + convert_month(system_month_UTC) + " " + system_day_of_month_UTC + ", " + system_year_UTC + " " + system_hour_of_day_UTC + "." + hulp_system_minute_UTC + " UTC";
      
   
   ///////// obs time
   //
   if (system_minute_UTC > 30)
   {
      cal_systeem_datum_tijd_UTC.add(Calendar.HOUR_OF_DAY, 1);   // add 1 hour
      cal_systeem_datum_tijd_UTC.getTime();
   }

   int obs_year          = cal_systeem_datum_tijd_UTC.get(Calendar.YEAR);
   int obs_month         = cal_systeem_datum_tijd_UTC.get(Calendar.MONTH);        // The first month of the year is JANUARY which is 0
   int obs_day_of_month  = cal_systeem_datum_tijd_UTC.get(Calendar.DAY_OF_MONTH); // The first day of the month has value 1
   int obs_hour_of_day   = cal_systeem_datum_tijd_UTC.get(Calendar.HOUR_OF_DAY);  // HOUR_OF_DAY: 24 hour clock; HOUR : 12 hour clock

   String obs_UTC_string = "obs: " + convert_month(obs_month) + " " + obs_day_of_month + ", " + obs_year + " " + obs_hour_of_day + ".00 UTC";   

   //if (JOptionPane.showConfirmDialog(null, obs_UTC_string, "Date and time of observation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION)
   if (JOptionPane.showConfirmDialog(null, obs_UTC_string + "\n\n" + "(" + system_UTC_string + ")" + "\n" + "(" + system_LT_string + ")", "Date and Time", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION)
   {
      // So date time of the confirmation message confirmed by the user

      mydatetime.year  = Integer.toString(obs_year);                     // for progress main screen and IMMT
      mydatetime.month = convert_month(obs_month);                       // for progress main screen
      mydatetime.day   = Integer.toString(obs_day_of_month);             // for progress main screen
      mydatetime.hour  = Integer.toString(obs_hour_of_day);              // for progress main screen

      /* update date and time on TurboWin+ main screen */
      date_time_fields_update();


      // determine code figures for month (not for operational obs only for IMMT storage)
      //
      mydatetime.MM_code = Integer.toString(obs_month +1);                   // +1 because obs_month started with 0 (January)

      len = 2;                                                               // MM_code always 2 characters width e.g. 03
      if (mydatetime.MM_code.length() < len)                                 // pad on left with zeros
      {
         mydatetime.MM_code = "0000000000".substring(0, len - mydatetime.MM_code.length()) + mydatetime.MM_code;
      }
         
      // determine code figures for day of the month (for obs and IMMT)
      //
      mydatetime.YY_code = Integer.toString(obs_day_of_month);               // obs_day_of_month e.g. 1,2,11,23,29

      len = 2;                                                               // YY_code always 2 characters width e.g. 03
      if (mydatetime.YY_code.length() < len)                                 // pad on left with zeros
      {
         mydatetime.YY_code = "0000000000".substring(0, len - mydatetime.YY_code.length()) + mydatetime.YY_code;
      }

      // determine code figures for hour of day (for obs and IMMT)
      //
      mydatetime.GG_code = Integer.toString(obs_hour_of_day);                // obs_hour_of_day e.g. 1,2,11,23

      len = 2;                                                               // GG_code always 2 characters width e.g. 03
      if (mydatetime.GG_code.length() < len)                                 // pad on left with zeros
      {
         mydatetime.GG_code = "0000000000".substring(0, len - mydatetime.GG_code.length()) + mydatetime.GG_code;
      }
         
      use_system_date_time_for_updating = true;
         
   } // if (JOptionPane.showConfirmDialog(null, datum_tijd_string, "Date and time of observation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION)
   else
   {
      use_system_date_time_for_updating = false;   // see Function: main_RS232_RS422.set_datetime_while_collecting_sensor_data()
   }


   /* clear memory as soon as possible */
   //cal_systeem_datum_tijd         = null;
}




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void read_muffin()
{
   // MUFFIN FOLDER VISTA: C:\Users\Martin\AppData\LocalLow\Sun\Java\Deployment\cache\6.0\muffin
   //                      C:\Gebruikers\Martin\AppData\LocalLow\Sun\Java\Deployment\cache\6.0\muffin
   // Windows 7          : C:\Users\hometrainer\AppData\LocalLow\Sun\Java\Deployment\cache\6.0\muffin
   

   // MORE MUFFINS INFO:
   // http://java.sun.com/javase/6/docs/technotes/guides/javaws/developersguide/examples.html
   // http://www.coderanch.com/t/200564/JNLP-Web-Start/java/Muffin-example-re-visited-getting
   // http://java.sun.com/javase/6/docs/jre/api/javaws/jnlp/javax/jnlp/PersistenceService.html


   //  http://mindprod.com/jgloss/lineseparator.html:
   //
   //  aka line terminator, newline, carriage return. Different operating systems use different conventions to mark the ends
   //   of lines in text files. For example, Unix/Linux uses newline, nl, 10, 0x0a, '\n'. DOS/Win 3.1/W95/W98/Me/NT/W2K/XP/W2K3/Vista
   //   use carriage-return:line-feed, CrLf, 13:10, 0x0d 0x0a "\r\n" OSX uses carriage-return, Cr, 10, 0x0a '\r'.
   //   If you use the readLine and println methods, Java automatically deals with the platform differences for you.
   //
   //   You can discover the magic line ending String with:
   //
   //   /** get the platform's String to mark the end of each line */
   //   static final String lineSeparator = System.getProperty ( "line.separator" );
   //
   //   The last line in a file may or may not have a line separator.
   //
   //
   // NB omdat PersistenceService met bytes uitgelezen MOET worden en daarna omgezet wordt naar String
   //    gaat de gewone eol soms fout daarom bij muffins met een zelfgedefineerde line seperator werken

   // called from: initComponents2()
   //
   
   System.out.println("--- reading muffin");

   new SwingWorker<Boolean, Void>()
   {
      @Override
      protected Boolean doInBackground() throws Exception
      {
         boolean muffin_lezen_gelukt = true;
         //String newline = System.getProperty("line.separator"); // not possible via newLine()

         // initialisation
         for (int i = 0; i < MAX_AANTAL_CONFIGURATIEREGELS; i++)
         {
            configuratie_regels[i] = "";
         }

         PersistenceService persistanceService = null;
         BasicService basicService = null;

         try
         {
            persistanceService = (PersistenceService)ServiceManager.lookup("javax.jnlp.PersistenceService");
            basicService = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
         }
         catch (UnavailableServiceException e)
         {
            muffin_lezen_gelukt = false;
            JOptionPane.showMessageDialog(null, "Internal error [read muffin()]; persistanceService and or basicService not available", APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         }

         if (persistanceService != null && basicService != null)
         {
            //JOptionPane.showMessageDialog(null, "persistanceService != null && basicService != null", APPLICATION_NAME + " test", JOptionPane.INFORMATION_MESSAGE);
            
            try
            {
               // NB from JRE 1.7_0_25 " URL codebase = basicService.getCodeBase()"fails in offline mode (online mode still ok)
               //    ONDERSTAANDE GEEFT BLANCO BIJ JRE 1.7_0_25 bij offline jnlp (niet bij online)
               //    JOptionPane.showMessageDialog(null, basicService.getCodeBase(), APPLICATION_NAME + " test", JOptionPane.INFORMATION_MESSAGE);
               //    JOptionPane.showMessageDialog(null, user_dir, APPLICATION_NAME + " test", JOptionPane.INFORMATION_MESSAGE);

               
               // find all the muffins for our URL
               URL codebase;
               if (offline_mode == true)
               {
                  File my_user_dir = new File(user_dir);
                  codebase = my_user_dir.toURI().toURL();
               }
               else // online (web) mode
               {
                  codebase = basicService.getCodeBase();
               }
               
               //URL codebase = basicService.getCodeBase();
               String[] muffins = persistanceService.getNames(codebase);

               int numberOfMuffins = ((muffins == null) ? 0 : muffins.length);

               if (numberOfMuffins > 0 && muffins != null)
               {
                  //JOptionPane.showMessageDialog(null, "numberOfMuffins > 0 && muffins != null", APPLICATION_NAME + " test", JOptionPane.INFORMATION_MESSAGE);

                  
                  URL[] muffinURLs = new URL[muffins.length];
                  for (int i = 0; i < muffins.length; i++)
                  {
                     muffinURLs[i] = new URL(codebase.toString() + muffins[i]);
                  } // for (int i = 0; i < muffins.length; i++)

                  // read in the contents of a muffin (for this program: always only 1 muffin -with index 0-)
                  FileContents fc = persistanceService.get(muffinURLs[0]);
                  byte[] buf = new byte[(int)fc.getLength()];
                  InputStream is = fc.getInputStream();
                  long pos = 0;
                  while ((pos = is.read(buf, (int)pos, (int)(buf.length - pos))) > 0)
                  {
                     // just loop
                  }

                  is.close();

                  String muffin_inhoud = new String(buf);            // byte array to a String

                  int teller = 0;
                  int from_index = 0;
                  int line_separator_index = -999;
                  while (line_separator_index != -1)
                  {
                     line_separator_index = muffin_inhoud.indexOf(MUFFIN_LINE_SEPARATOR, from_index); // indexOf : if no such value of MUFFIN_LINE_SEPARATOR exists, then -1 is returned.

                     if (line_separator_index != -1)
                     {
                        configuratie_regels[teller] = muffin_inhoud.substring(from_index, line_separator_index);// e.g. "hamburger".substring(4, 8) returns "urge"
                        from_index = line_separator_index + 1;
                     } // if (newline_index != -1)

                     teller++;
                  } // while (from_index < MAX_AANTAL_CONFIGURATIEREGELS && newline_index != -1)

                  meta_data_from_configuration_regels_into_global_vars();

               } // if (numberOfMuffins > 0
               else
               {
                  muffin_lezen_gelukt = false;                       // no muffin found
               }

            } // try
            catch (IOException e)
            {
               //e.printStackTrace();
               muffin_lezen_gelukt = false;
               // no warning, it is possible (first time use) that the file was never created
            }
         } // if (persistanceService != null && basicService != null)

         return muffin_lezen_gelukt;
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         try 
         {
            boolean muffin_lezen_gelukt = get(); // retrieve return value from doInBackground()
            if (muffin_lezen_gelukt == false)
            {
               //JOptionPane.showMessageDialog(null, "muffin gelezen mislukt", APPLICATION_NAME + " test", JOptionPane.INFORMATION_MESSAGE);

               /* alternative if read muffin failed (note: call_sign_fields_update() etc also invoked via lees_configuratie_regels()) */
               lees_configuratie_regels();       // muffin read failed now try file configuration.txt from logs dir (first) or user dir (second)
            }
            else // muffin read succeeded 
            {
               //JOptionPane.showMessageDialog(null, "muffin gelezen gelukt", APPLICATION_NAME + " test", JOptionPane.INFORMATION_MESSAGE);

               // "TurboWIn+ started" message to log (logs dir must be known!)
               log_turbowin_system_message("[GENERAL] started " + APPLICATION_NAME + " " + application_mode + " " + APPLICATION_VERSION);
               
               // deleting old (> 3 months) turbowin system logs
               log_turbowin_system_message("[GENERAL] deleting old (> 3 months) " + APPLICATION_NAME + " system logs");
               delete_logs_turbowin_system();
               
               // NB !!!!!!!!!!!!!! see also alternative lees_configuratie_regels() if muffin read fails !!!!!!!!!
               
               /* call sign and masked call sign update on main (progress) screen" */
               call_sign_fields_update();
 
               /* meta data (mystationdata.java), eg IMO number and call sign, must be present */
               check_meta_data();

               /* check immt size (main.java) */
               check_immt_size();
               
               /* RS232/RS422/WiFi */
               if (RS232_connection_mode == 1 || RS232_connection_mode == 2 || RS232_connection_mode == 4 || RS232_connection_mode == 5) // PTB220 or PTB330 or Mintaka Duo or Mintaka Star USB
               {   
                  RS232_RS422.RS232_initComponents();                                    // for Vaisala/Mintaka barometers (not Mintaka Star Wifi)
               }
               else if (RS232_connection_mode == 3)                                      // EUCOS AWS 
               {
                  /* in AWS mode input items like call sign, position, date/time etc. are not asked */ 
                  //disable_aws_input_menu_items();
                  
                  /* in AWS mode Output -> obs to file etc. disable */
                  disable_aws_output_menu_items();
                  
                  /* in AWS mode no dew point but relative humidity */
                  jLabel40.setText("relative humidity");
                  
                  /* in AWS mode update info */
                  jLabel8.setText("--- sensor data updated every minute ---");
                  
                  RS232_RS422.RS422_initComponents(); 
               }
               else if (RS232_connection_mode == 6)                                       // Mintaka Star WiFi
               {
                  RS232_RS422.WiFi_initComponents();
               }
               
               /* check date and time (and ask observer for confirmation) */
               if (RS232_connection_mode != 3)                             // not AWS connected mode
               {
                  check_and_set_datetime_v2();
               }
               else
               {
                  use_system_date_time_for_updating = false;               // in AWS connected mode the date time is updated when reading the incoming AWS measured data
               }
               
               /* gray (disable) the not appropriate graph menu selection options */
               disable_graph_menu_items();
               
               /* if not AWS connected -> disable the "Obs to AWS" menu item */
               if (RS232_connection_mode != 3)                            // not AWS connected mode
               {
                  jMenuItem46.setEnabled(false);                          // Obs to AWS
               }
               
               /* GPS connected ? */
               if (RS232_GPS_connection_mode == 1)                        // 0 = no GPS; 1 = GPS (NMEA 1083)
               {
                  RS232_RS422.RS232_GPS_NMEA_0183_initComponents();
               }

               // NB !!!!!!!!!!!!!! see also alternative lees_configuratie_regels() if muffin read fails !!!!!!!!!
               
            } // else (muffin read succeeded)
         } // try
         catch (InterruptedException | ExecutionException ex) 
         {
            //JOptionPane.showMessageDialog(null, "internal error; muffin read failed " + ex, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            System.out.println("+++ internal error; muffin read failed " + ex);
            
            /* alternative if read muffin failed (note: call_sign_fields_update() etc also invoked via lees_configuratie_regels())*/
            lees_configuratie_regels();       // muffin read failed now try file configuration.txt from logs dir (first) or user dir (second)
         }
          
      } // protected void done()
   }.execute(); // new SwingWorker<Boolean, Void>()
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
private void disable_aws_input_menu_items()
{
   // call sign and masked call sign (VOS ID)
   //
   jLabel1.setEnabled(false);                  // call sign                       // see also function
   jLabel2.setEnabled(false);                  // masked call sign                // see also function
   jButton1.setEnabled(false);                 // call sign and masked call sign  // see also function
   jTextField1.setEnabled(false);              // call sign
   jTextField2.setEnabled(false);              // masked call sign
   
   // Date & Time
   // 
   jMenuItem3.setEnabled(false);               // menu item Date and Time
   //NB Disabling a lightweight component does not prevent it from receiving MouseEvents
   //   Therefore a small trick via also prevent further invoking of other functions (see below comment)
   jLabel3.setEnabled(false);                  // see also date_time_mainscreen_moudeClicked()
   jButton2.setEnabled(false);                 // see also date_time_toolbar_mouseClicked()
   jTextField3.setEnabled(false);              // date and time
   
   // position course & speed
   //
   jMenuItem4.setEnabled(false);               // position course & speed
   jLabel5.setEnabled(false);                  // position                  // see also function
   jLabel7.setEnabled(false);                  // course & speed            // see also function
   jButton3.setEnabled(false);                 // position course & speed   // see also function
   jTextField5.setEnabled(false);              // position
   jTextField7.setEnabled(false);              // course and speed
   
   // pressure reading/sensor height and pressur MSL (barometer)
   //
   jMenuItem7.setEnabled(false);               // pressure reading and MSL 
   jLabel9.setEnabled(false);                  // pressure read             // see also function
   jLabel10.setEnabled(false);                 // pressure MSL              // see also function
   jButton6.setEnabled(false);                 // pressure reading and MSL  // see also function
   jTextField9.setEnabled(false);              // pressure read
   jTextField10.setEnabled(false);             // pressure MSL
   
   // pressure amount tendency and characteristic (barograph)
   //
   jMenuItem8.setEnabled(false);               // pressure amount tendency and characteristic
   jLabel11.setEnabled(false);                 // pressure amount                             // see also function
   jLabel12.setEnabled(false);                 // pressure characteristic                     // see also function
   jButton7.setEnabled(false);                 // pressure amount tendency and characteristic // see also function
   jTextField11.setEnabled(false);             // pressure amount
   jTextField12.setEnabled(false);             // pressure characteristic
     
}
*/


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void disable_aws_output_menu_items()
{
   jMenuItem20.setEnabled(false);                  // Obs to server
   jMenuItem23.setEnabled(false);                  // Obs by E-mail
   jMenuItem24.setEnabled(false);                  // Obs to file
   jMenuItem48.setEnabled(false);                  // Obs to clipboard
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void set_muffin()
{
   // FOR WINDOWS VISTA e.g.: C:\Users\Martin\AppData\LocalLow\Sun\Java\Deployment\cache\6.0\muffin
   //                         C:\Gebruikers\Martin\AppData\LocalLow\Sun\Java\Deployment\cache\6.0\muffin
   // WINDOWS 7/8       e.g.: C:\Users\hometrainer\AppData\LocalLow\Sun\Java\Deployment\cache\6.0\muffin
   //
   // more info about muffins:
   // http://java.sun.com/javase/6/docs/technotes/guides/javaws/developersguide/examples.html
   // http://www.coderanch.com/t/200564/JNLP-Web-Start/java/Muffin-example-re-visited-getting
   // http://java.sun.com/javase/6/docs/jre/api/javaws/jnlp/javax/jnlp/PersistenceService.html

   System.out.println("--- writing muffin");
   
   // fill array
   fill_configuratie_array();
   
   // extra check for writing to muffins
   for (int i = 0; i < main.MAX_AANTAL_CONFIGURATIEREGELS; i++)
   {
      if (main.configuratie_regels[i].indexOf(main.MUFFIN_LINE_SEPARATOR, 0) != -1) // indexOf : if no such value of MUFFIN_LINE_SEPARATOR exists, then -1 is returned.
      {
         String info = "Do not use character \"" + main.MUFFIN_LINE_SEPARATOR + "\" in inserted text string " + main.configuratie_regels[i].substring(main.CONFIGURATION_FILE_POS_INHOUD);
         JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " error", JOptionPane.INFORMATION_MESSAGE);

         break;
      } // if (main.configuratie_regels[i].indexOf(main.MUFFIN_LINE_SEPARATOR, 0) != -1)
   } // for (int i = 0; i < main.MAX_AANTAL_CONFIGURATIEREGELS; i++)

   
   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {

         //String newline = System.getProperty("line.separator"); // not possible via newLine()
/*
         //String urlName = "http://localhost:8888/servlets-examples/servlet/CookieServlet";
         //String urlName = "http://localhost";

         //String urlName = "http://www.knmi.nl/turbowin/webstart";
         // ONDER XP:String urlName = "file:/C:/Program%20Files/NetBeans%206.5/projects/turbowin_jws/dist/";
         String urlName = "file:/C:/Program%20Files/NetBeans%206.5.1/projects/turbowin_jws/dist/";

         URL url;
         try
         {
            url = new URL(urlName);
         }
         catch (MalformedURLException mfe)
         {
            JOptionPane.showMessageDialog(null, "internal error; url not valid", APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            mfe.printStackTrace();
            url = null;
            return null;
         }
*/
         PersistenceService persistanceService = null;
         BasicService basicService = null;

         try
         {
            persistanceService = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
            basicService = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
         }
         catch (UnavailableServiceException e)
         {
            JOptionPane.showMessageDialog(null, "internal error [set_muffin()]; persistanceService and/or basicService not available", APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         }

         if (persistanceService != null && basicService != null)
         {
            try
            {
               // find all the muffins for our URL and delete them
               
               //URL codebase = basicService.getCodeBase();
               // From JRE 1.7_0_25 "URL codebase = basicService.getCodeBase();" fails for offline mode!!!
               URL codebase;
               if (offline_mode == true)
               {
                  File my_user_dir = new File(user_dir);
                  codebase = my_user_dir.toURI().toURL();
               }
               else // online (web) mode
               {
                  codebase = basicService.getCodeBase();
               }
               
               String[] muffins = persistanceService.getNames(codebase);
               int numberOfMuffins = ((muffins == null) ? 0 : muffins.length);

               if (numberOfMuffins > 0 && muffins != null)
               {
                  URL[] muffinURLs = new URL[muffins.length];
                  for (int i = 0; i < muffins.length; i++)
                  {
                     muffinURLs[i] = new URL(codebase.toString() + muffins[i]);
                     persistanceService.delete(muffinURLs[i]);
                  } // for (int i = 0; i < muffins.length; i++)

               } // if (numberOfMuffins > 0)


               // well, just try and create the muffin and repopulate its data
/*
               persistanceService.create(url, 1024);                    // second parameter: maxsize - maximum size of storage that can be written to this entry
               FileContents fc = persistanceService.get(url);           // Returns a FileContents object representing the contents of this file
*/
               persistanceService.create(codebase, 2048);                    // second parameter: maxsize - maximum size of storage that can be written to this entry
               FileContents fc = persistanceService.get(codebase);           // Returns a FileContents object representing the contents of this file

               // don't append
               //OutputStream os = fc.getOutputStream(false);           // Gets an OutputStream to the file (method of FileContents)
               //os.write("1234567890test1234567890test00".getBytes()); // getBytes():  Encodes this String into a sequence of bytes using the named charset, storing the result into a new byte array.


               OutputStream os = fc.getOutputStream(false);             // Gets an OutputStream to the file (method of FileContents)
               //String newline = System.getProperty("line.separator"); // not possible via newLine()
               for (int i = 0; i < MAX_AANTAL_CONFIGURATIEREGELS; i++)
               {
                  if ((configuratie_regels[i] != null) && (configuratie_regels[i].compareTo("") != 0))
                  {
                     os.write(configuratie_regels[i].getBytes());
                     //os.newLine();   // newLine(): write a line separator. The line separator string is defined by the system property line.separator, and is not necessarily a single newline ('\n') character.
                     //os.write(newline.getBytes());
                     os.write(MUFFIN_LINE_SEPARATOR.getBytes());
                     //os.println(configuratie_regels[i]);
                  }
               } // for (int i = 0; i < MAX_AANTAL_CONFIGURATIEREGELS; i++)

               os.close();

            } // for (int i = 0; i < MAX_AANTAL_CONFIGURATIEREGELS; i++)
            catch (IOException e)
            {
               JOptionPane.showMessageDialog(null, "internal error [set_muffin()]; I/O persistanceService and/or basicService", APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            }
         } // if (persistanceService != null && basicService != null)

         return null;
      } // protected Void doInBackground() throws Exception

   }.execute(); // new SwingWorker<Void, Void>()

}

 /*
 void doUpdateServer(URL url)
 {
    PersistenceService ps = null;
      try {
         ps = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
      } catch (UnavailableServiceException ex) {
         Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
      }
      try {
         // update the server's copy of the persistent data
         // represented by the given URL
         //...
         ps.setTag(url, PersistenceService.CACHED);
      } catch (MalformedURLException ex) {
         Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
      }
 }
*/





   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void coded_obs_update()
   {
      //if (RS232_connection_mode != 3)                        // not AWS connected mode
      if (!main.obs_format.equals(main.FORMAT_AWS))            // not AWS connected mode
      {
         // NB in case format 101 (compressed) this coded obs is only a kind of decompressed FM13 obs
         //    especially Is (icing) has a specially coding/meaning 
         //    (see also Function private void initSynopparameters() in icing.java)
         //     So it is possible that the icing group contains 6 characters!
         
         /* update coded obs status field (bottom line main -progress- window) */
         String SPATIE = SPATIE_OBS_VIEW;                                          // dan gewoon " " als spatie grbruiken 
         jTextField4.setText(compose_coded_obs(SPATIE)); 
      }
   }           

   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void bepaal_frame_location()
   {
      Toolkit kit = Toolkit.getDefaultToolkit();
      Dimension screenSize = kit.getScreenSize();
      screenWidth = screenSize.width;
      screenHeight = screenSize.height;

      // compute x-y location start screen + set Location to this position
      x_pos_start_frame = screenWidth / 2 - (1050 / 2);
      y_pos_start_frame = screenHeight / 2 - (740 / 2);
      setLocation(x_pos_start_frame, y_pos_start_frame);

      // compute x-y location main screen (Maintenance etc)
      x_pos_main_frame = screenWidth / 2 - (1000 / 2);
      y_pos_main_frame = screenHeight / 2 - (700 / 2);
      
      // compute x-y location main screen (AMVER forms)
      x_pos_amver_frame = screenWidth / 2 - (1000 / 2);
      y_pos_amver_frame = screenHeight / 2 - (750 / 2);
      
      // compute position of other (parameter/element like waves) screens(frames)
      x_pos_frame = screenWidth / 2 - (800 / 2);
      y_pos_frame = screenHeight / 2 - (600 / 2);
    
      // compute position of info-about screen
      x_pos_small_frame = screenWidth / 2 - (400 / 2);
      y_pos_small_frame = screenHeight / 2 - (300 / 2);
      
     // compute position of calculator screen
      x_pos_calculator_frame = screenWidth / 2 - (350 / 2);
      y_pos_calculator_frame = screenHeight / 2 - (550 / 2);
       
   }      


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static String compose_coded_obs(String SPATIE)
   {
      String coded_obs_call_sign;
      String coded_obs_YY;
      String coded_obs_GG;
      String coded_obs_iw;
      String coded_obs_lalala;
      String coded_obs_lolololo;
      String coded_obs_Qc;
      String coded_obs_Ds;
      String coded_obs_vs;
      String coded_obs_N;
      String coded_obs_Nh;
      String coded_obs_h;
      String coded_obs_VV;
      String coded_obs_dd;
      String coded_obs_ff;
      String coded_obs_snTTT;
      String coded_obs_snTdTdTd;
      String coded_obs_PPPP;   
      String coded_obs_a;   
      String coded_obs_ppp;   
      String coded_obs_ww;
      String coded_obs_W1;
      String coded_obs_W2;
      String coded_obs_Cl;
      String coded_obs_Cm;
      String coded_obs_Ch;
      String coded_obs_ssTsTsTs;
      String coded_obs_Pw;
      String coded_obs_Hw;
      String coded_obs_Dw1;
      String coded_obs_Hw1;
      String coded_obs_Pw1;
      String coded_obs_Dw2;
      String coded_obs_Hw2;
      String coded_obs_Pw2;
      String coded_obs_Is;
      String coded_obs_EsEs;
      String coded_obs_Rs;
      String coded_obs_snTbTbTb;
      String coded_obs_ci;
      String coded_obs_Si;
      String coded_obs_bi;
      String coded_obs_Di;
      String coded_obs_zi;
              
      
      // if masked call sign (VOS ID) iserted -> use this for obs else 'normal' call sign
      //
      if ((masked_call_sign != null) && (masked_call_sign.trim().length() > 0))
         coded_obs_call_sign = masked_call_sign;
      else if ((call_sign != null) && (call_sign.trim().length() > 0))
         coded_obs_call_sign = call_sign;
      else
         coded_obs_call_sign = "unknown";
         
      //JOptionPane.showMessageDialog(null, obs_call_sign, "mycallsign.call_sign", JOptionPane.INFORMATION_MESSAGE);

      // date/time (null value if YY_code never activated /null pointer, "" if page visited but nothing done)"
      //
      if ((mydatetime.YY_code != null) && (mydatetime.YY_code.compareTo("") != 0))     // day
         coded_obs_YY = mydatetime.YY_code;
      else
         coded_obs_YY = "//";
         
      if ((mydatetime.GG_code != null) && (mydatetime.GG_code.compareTo("") != 0))     // hour
         coded_obs_GG = mydatetime.GG_code;
      else
         coded_obs_GG = "//";
         
      
      // position
      //
      if ((myposition.Qc_code != null) && (myposition.Qc_code.compareTo("") != 0))
         coded_obs_Qc = myposition.Qc_code;
      else
         coded_obs_Qc = "/";
         
      if ((myposition.lalala_code != null) && (myposition.lalala_code.compareTo("") != 0))
         coded_obs_lalala = myposition.lalala_code;
      else
         coded_obs_lalala = "///";
         
      if ((myposition.lolololo_code != null) && (myposition.lolololo_code.compareTo("") != 0))
         coded_obs_lolololo = myposition.lolololo_code;
      else
         coded_obs_lolololo = "////";
         
      
      // ship course
      //
      if ((myposition.Ds_code != null) && (myposition.Ds_code.compareTo("") != 0))
         coded_obs_Ds = myposition.Ds_code;
      else
         coded_obs_Ds = "/";
         
      
      // ship speed
      //
      if ((myposition.vs_code != null) && (myposition.vs_code.compareTo("") != 0))
         coded_obs_vs = myposition.vs_code;
      else
         coded_obs_vs = "/";
         
         
      // total cloud cover (N) 
      //
      if ((mycloudcover.N_code != null) && (mycloudcover.N_code.compareTo("") != 0))
         coded_obs_N = mycloudcover.N_code;
      else
         coded_obs_N = "/"; 
         
      
      // cover Cl/Cm (Nh)
      //
      if ((mycloudcover.Nh_code != null) && (mycloudcover.Nh_code.compareTo("") != 0))
         coded_obs_Nh = mycloudcover.Nh_code;
      else
         coded_obs_Nh = "/"; 
        
      
      // height lowest cloud in the sky (h)
      //
      if ((mycloudcover.h_code != null) && (mycloudcover.h_code.compareTo("") != 0))
         coded_obs_h = mycloudcover.h_code;
      else
         coded_obs_h = "/"; 
         
         
      // visibility (VV)
      //
      if ((myvisibility.VV_code != null) && (myvisibility.VV_code.compareTo("") != 0))
         coded_obs_VV = myvisibility.VV_code;
      else
         coded_obs_VV = "//"; 
         

      // winds source
      if ((mywind.iw_code != null) && (mywind.iw_code.compareTo("") != 0))
         coded_obs_iw = mywind.iw_code;
      else
         coded_obs_iw = "/";


      // wind direction (dd)
      //
      if ((mywind.dd_code != null) && (mywind.dd_code.compareTo("") != 0))
         coded_obs_dd = mywind.dd_code;
      else
         coded_obs_dd = "//"; 

      
      // wind speed (ff)
      //
      if ((mywind.ff_code != null) && (mywind.ff_code.compareTo("") != 0))
      {
         coded_obs_ff = mywind.ff_code;
         
         // extra 00fff group only added if wind speed >= 100 units (so only if fff00_code != "")
         if ((mywind.fff00_code != null) && (mywind.fff00_code.compareTo("") != 0))
         {
            coded_obs_ff += SPATIE + "00" + mywind.fff00_code;
         }
      }
      else
      {
         coded_obs_ff = "//";
      } 
         
         
      // air temperature 
      //
      if ((mytemp.sn_TTT_code != null) && (mytemp.sn_TTT_code.compareTo("") != 0) &&
          (mytemp.TTT_code != null)    && (mytemp.TTT_code.compareTo("") != 0) )
         coded_obs_snTTT = mytemp.sn_TTT_code + mytemp.TTT_code;
      else
         coded_obs_snTTT = "////"; 
         
      
      // dew point
      //
      if ((mytemp.sn_TdTdTd_code != null) && (mytemp.sn_TdTdTd_code.compareTo("") != 0) &&
          (mytemp.TdTdTd_code != null)    && (mytemp.TdTdTd_code.compareTo("") != 0) )
         coded_obs_snTdTdTd = mytemp.sn_TdTdTd_code + mytemp.TdTdTd_code;
      else
         coded_obs_snTdTdTd = "////"; 
         
         
      // air pressure (at MSL)
      //
      if ((mybarometer.PPPP_code != null) && (mybarometer.PPPP_code.compareTo("") != 0))
         coded_obs_PPPP = mybarometer.PPPP_code;
      else
         coded_obs_PPPP = "////"; 
         
      
      // air pressure tendency characteristic
      //
      if ((mybarograph.a_code != null) && (mybarograph.a_code.compareTo("") != 0))
         coded_obs_a = mybarograph.a_code;
      else
         coded_obs_a = "/"; 

      
      // air pressure tendency amount
      //
      if ((mybarograph.ppp_code != null) && (mybarograph.ppp_code.compareTo("") != 0))
         coded_obs_ppp = mybarograph.ppp_code;
      else
         coded_obs_ppp = "///"; 
      
      
      // present weather
      //
      if ((mypresentweather.ww_code != null) && (mypresentweather.ww_code.compareTo("") != 0))
         coded_obs_ww = mypresentweather.ww_code;
      else
         coded_obs_ww = "//"; 
      
      
      // past weather 1
      //
      if ((mypastweather.W1_code != null) && (mypastweather.W1_code.compareTo("") != 0))
         coded_obs_W1 = mypastweather.W1_code;
      else
         coded_obs_W1 = "/"; 
     
      
      // past weather 2
      //
      if ((mypastweather.W2_code != null) && (mypastweather.W2_code.compareTo("") != 0))
         coded_obs_W2 = mypastweather.W2_code;
      else
         coded_obs_W2 = "/"; 
     
         
      // clouds low (Cl)
      //
      if ((mycl.cl_code != null) && (mycl.cl_code.compareTo("") != 0))
         coded_obs_Cl = mycl.cl_code;
      else
         coded_obs_Cl = "/"; 
      
      
      // clouds middle (Cm)
      //
      if ((mycm.cm_code != null) && (mycm.cm_code.compareTo("") != 0))
         coded_obs_Cm = mycm.cm_code.substring(0, 1);    // omdat bij cm_code in geval Cm7 een a, b, c er achter staat (dus 7a, 7b, 7c)
      else
         coded_obs_Cm = "/"; 
      
      
      // clouds high (Ch)
      //
      if ((mych.ch_code != null) && (mych.ch_code.compareTo("") != 0))
         coded_obs_Ch = mych.ch_code;
      else
         coded_obs_Ch = "/"; 
      
      
      // Tsea
      //
      if ((mytemp.ss_TsTsTs_code != null) && (mytemp.ss_TsTsTs_code.compareTo("") != 0) &&
          (mytemp.TsTsTs_code != null)    && (mytemp.TsTsTs_code.compareTo("") != 0) )
         coded_obs_ssTsTsTs = mytemp.ss_TsTsTs_code + mytemp.TsTsTs_code;
      else
         coded_obs_ssTsTsTs = "////"; 
      
      
      // wind waves period
      //
      if ((mywaves.Pw_code != null) && (mywaves.Pw_code.compareTo("") != 0))
         coded_obs_Pw = mywaves.Pw_code;
      else
         coded_obs_Pw = "//"; 
      
      
      // wind waves height
      //
      if ((mywaves.Hw_code != null) && (mywaves.Hw_code.compareTo("") != 0))
         coded_obs_Hw = mywaves.Hw_code;
      else
         coded_obs_Hw = "//"; 
      
      
      // swell 1 direction
      //

      //JOptionPane.showMessageDialog(null, mywaves.Dw1_code, "mywaves.Dw1_code", JOptionPane.WARNING_MESSAGE);
      if ((mywaves.Dw1_code != null) && (mywaves.Dw1_code.compareTo("") != 0))
         coded_obs_Dw1 = mywaves.Dw1_code;
      else
         coded_obs_Dw1 = "//"; 
     
      
      // swell 1 period
      //
      if ((mywaves.Pw1_code != null) && (mywaves.Pw1_code.compareTo("") != 0))
         coded_obs_Pw1 = mywaves.Pw1_code;
      else
         coded_obs_Pw1 = "//"; 
     
      
      // swell 1 height
      //
      if ((mywaves.Hw1_code != null) && (mywaves.Hw1_code.compareTo("") != 0))
         coded_obs_Hw1 = mywaves.Hw1_code;
      else
         coded_obs_Hw1 = "//"; 
     
      
      // swell 2 direction
      //
      if ((mywaves.Dw2_code != null) && (mywaves.Dw2_code.compareTo("") != 0))
         coded_obs_Dw2 = mywaves.Dw2_code;
      else
         coded_obs_Dw2 = "//"; 
     
      
      // swell 2 period
      //
      if ((mywaves.Pw2_code != null) && (mywaves.Pw2_code.compareTo("") != 0))
         coded_obs_Pw2 = mywaves.Pw2_code;
      else
         coded_obs_Pw2 = "//"; 
     
      
      // swell 2 height
      //
      if ((mywaves.Hw2_code != null) && (mywaves.Hw2_code.compareTo("") != 0))
         coded_obs_Hw2 = mywaves.Hw2_code;
      else
         coded_obs_Hw2 = "//"; 


      // icing cause (Is)
      //
      if ((myicing.Is_code != null) && (myicing.Is_code.compareTo("") != 0))
      {
         coded_obs_Is = myicing.Is_code;
      }
      else
      {
         coded_obs_Is = "/";
      }


      // icing thickness (EsEs)
      //
      if ((myicing.EsEs_code != null) && (myicing.EsEs_code.compareTo("") != 0))
      {
         coded_obs_EsEs = myicing.EsEs_code;
      }
      else
      {
         coded_obs_EsEs = "//";
      }


      // icing rate (Rs)
      //
      if ((myicing.Rs_code != null) && (myicing.Rs_code.compareTo("") != 0))
      {
         coded_obs_Rs = myicing.Rs_code;
      }
      else
      {
         coded_obs_Rs = "/";
      }
      
      
      // wet bulb temperature (TbTbTb)
      //
      if ((mytemp.sn_TbTbTb_code != null) && (mytemp.sn_TbTbTb_code.compareTo("") != 0) &&
          (mytemp.TbTbTb_code != null)    && (mytemp.TbTbTb_code.compareTo("") != 0) )
         coded_obs_snTbTbTb = mytemp.sn_TbTbTb_code + mytemp.TbTbTb_code;
      else
         coded_obs_snTbTbTb = "////"; 


      // concentration or arrangement of sea ice (ci)
      //
      if ((myice1.ci_code != null) && (myice1.ci_code.compareTo("") != 0))
      {
         if (myice1.ci_code.trim().equals("u"))              // unable to report etc.
         {
            coded_obs_ci = "/";
         }
         else
         {
            coded_obs_ci = myice1.ci_code;
         }
      }
      else
      {
         coded_obs_ci = "/";
      }


      // stage of development Si)
      //
      if ((myice1.Si_code != null) && (myice1.Si_code.compareTo("") != 0))
      {
         if (myice1.Si_code.trim().equals("u"))              // unable to report etc.
         {
            coded_obs_Si = "/";
         }
         else
         {
            coded_obs_Si = myice1.Si_code;
         }
      }
      else
      {
         coded_obs_Si = "/";
      }


      // Ice of land origin (bi)
      //
      if ((myice1.bi_code != null) && (myice1.bi_code.compareTo("") != 0))
      {
         if (myice1.bi_code.trim().equals("u"))              // unable to report etc.
         {
            coded_obs_bi = "/";
         }
         else
         {
            coded_obs_bi = myice1.bi_code;
         }
      }
      else
      {
         coded_obs_bi = "/";
      }


      // Bearing of principal ice edge (Di)
      //
      if ((myice1.Di_code != null) && (myice1.Di_code.compareTo("") != 0))
      {
         if (myice1.Di_code.trim().equals("u"))              // unable to report etc.
         {
            coded_obs_Di = "/";
         }
         else
         {
            coded_obs_Di = myice1.Di_code;
         }
      }
      else
      {
         coded_obs_Di = "/";
      }

     
      // Ice situation and trend over preceding three hours (zi)
      //
      if ((myice1.zi_code != null) && (myice1.zi_code.compareTo("") != 0))
      {
         if (myice1.zi_code.trim().equals("u"))              // unable to report etc.
         {
            coded_obs_zi = "/";
         }
         else
         {
            coded_obs_zi = myice1.zi_code;
         }
      }
      else
      {
         coded_obs_zi = "/";
      }


      
      if ( (coded_obs_call_sign.compareTo("unknown") != 0) &&
           (coded_obs_YY.compareTo("//") != 0) && (coded_obs_GG.compareTo("//") != 0) &&
           (coded_obs_Qc.compareTo("/") != 0) && (coded_obs_lalala.compareTo("///") != 0) && (coded_obs_lolololo.compareTo("////") != 0) )
      {   
         // LET OP
         // NB met IE7 gaat als je voor spatie een " " neemt het wel goed
         //    met FireFox gaat als je voor spatie " " neemt het NIET goed

         coded_obs_total =
                 "BBXX" +                                                                         // BBXX
                 SPATIE +
                 coded_obs_call_sign +                                                            // D..D
                 SPATIE +
                 coded_obs_YY + coded_obs_GG + coded_obs_iw +                                     // YYGGiw
                 SPATIE +
                 "99" + coded_obs_lalala +                                                        // 99LaLaLa
                 SPATIE +
                 coded_obs_Qc + coded_obs_lolololo +                                              // QcLoLoLoLo
                 SPATIE +
                 "41" + coded_obs_h + coded_obs_VV +                                              // irihhVV
                 SPATIE +
                 coded_obs_N + coded_obs_dd + coded_obs_ff +                                      // Nddff
                 SPATIE +
                 "1" + coded_obs_snTTT +                                                          // 1snTTT
                 SPATIE +
                 "2" + coded_obs_snTdTdTd +                                                       // 2snTdTdTd
                 SPATIE +
                 "4" + coded_obs_PPPP +                                                           // 4PPPP
                 SPATIE +
                 "5" + coded_obs_a + coded_obs_ppp +                                              // 5appp
                 SPATIE +
                 "7" + coded_obs_ww + coded_obs_W1 + coded_obs_W2 +                               // 7wwW1W2                                                         // 4PPPP
                 SPATIE +
                 "8" + coded_obs_Nh + coded_obs_Cl + coded_obs_Cm + coded_obs_Ch +                // 8NhClCmCh                                                         // 4PPPP
                 SPATIE +
                 "222" + coded_obs_Ds + coded_obs_vs +                                            // 222Dsvs                                                         // 4PPPP
                 SPATIE +
                 "0" + coded_obs_ssTsTsTs +                                                       // 0ssTwTwTw
                 SPATIE +
                 "2" + coded_obs_Pw + coded_obs_Hw;                                              // 2PwPwHwHw
                 //SPATIE +
                 //"3" + coded_obs_Dw1 + coded_obs_Dw2 +                                            // 3dw1dw1dw2dw2
                 //SPATIE +
                 //"4" + coded_obs_Pw1 + coded_obs_Hw1;                                             // 4Pw1Pw1Hw1Hw1

         if (((coded_obs_Dw1 + coded_obs_Dw2).equals("////") == false))
         {
            // swell dir group only if relevant data available (not ////)
            coded_obs_total +=
                 SPATIE +
                 "3" + coded_obs_Dw1 + coded_obs_Dw2;                                             // 5Pw2Pw2Hw2Hw2
         } // if (((coded_obs_Dw1 + coded_obs_Dw2).equals("////") == false)) 

         if ( ((coded_obs_Pw1 + coded_obs_Hw1).equals("////") == false) || (coded_obs_Dw1.equals("99") == true) )
         {
            // 1st swell group only if relevant data available (not ////) or if confused swell (Dw1 = 99)
            coded_obs_total +=
                 SPATIE +
                 "4" + coded_obs_Pw1 + coded_obs_Hw1;                                             // 5Pw2Pw2Hw2Hw2
         } // if ( ((coded_obs_Pw1 + coded_obs_Hw1).equals("////") == false) )

         if (((coded_obs_Pw2 + coded_obs_Hw2).equals("////") == false)) 
         {
            // 2nd swell group only if relevant data available (not ////)
            coded_obs_total +=
                 SPATIE +
                 "5" + coded_obs_Pw2 + coded_obs_Hw2;                                             // 5Pw2Pw2Hw2Hw2
         } // if ( ((coded_obs_Pw2 + coded_obs_Hw2).equals("////") == false) )

         if (((coded_obs_Is + coded_obs_EsEs + coded_obs_Rs).equals("////") == false))
         {
            // icing group only if relevant data available (not ////)
            coded_obs_total +=
                 SPATIE +
                 "6" + coded_obs_Is + coded_obs_EsEs + coded_obs_Rs;                             // 6IsEsEsRs
         } // if (((coded_obs_Is + coded_obs_EsEs + coded_obs_Rs).equals("////") == false))

         if ((coded_obs_snTbTbTb.equals("////") == false)) 
         {
            // wet bulb group only if relevant data available (not ////)
            coded_obs_total +=
                 SPATIE +
                 "8" + coded_obs_snTbTbTb;                                                        // 8swTbTbTb
         } // if ( (coded_obs_snTbTbTb.equals("////") == false) )

         if (((coded_obs_ci + coded_obs_Si + coded_obs_bi + coded_obs_Di + coded_obs_zi).equals("/////") == false))
         {
            // ice group only if relevant data available (not /////)
            coded_obs_total +=
                 SPATIE +
                 "ICE" +
                 SPATIE + 
                 coded_obs_ci + coded_obs_Si + coded_obs_bi + coded_obs_Di + coded_obs_zi;                                     // ICE ciSibiDizi
         } // 


         coded_obs_total +=
                 "=";
         
      } // if ( (coded_obs_call_sign.compareTo("unknown") != 0) etc.
      else // call sign, dat/time or position not inserted
      {
         coded_obs_total = UNDEFINED;
      } // else
       
         
      return coded_obs_total;   
   }
  
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void fill_configuratie_array()
   { 
      configuratie_regels[0] =  main.SHIP_NAME_TXT + main.ship_name.trim();                              
      configuratie_regels[1] =  main.IMO_NUMBER_TXT + main.imo_number.trim();                            
      configuratie_regels[2] =  main.CALL_SIGN_TXT + main.call_sign.trim();                              
      configuratie_regels[3] =  main.MASKED_CALL_SIGN_TXT + main.masked_call_sign.trim();                
      configuratie_regels[4] =  main.TIME_ZONE_COMPUTER_TXT + main.time_zone_computer.trim();            
      configuratie_regels[5] =  main.RECRUITING_COUNTRY_TXT + main.recruiting_country.trim();            
      configuratie_regels[6] =  main.METHOD_WAVES_TXT + main.method_waves.trim();                        
      configuratie_regels[7] =  main.WIND_SOURCE_TXT + main.wind_source.trim();                          
      configuratie_regels[8] =  main.BAROMETER_ABOVE_SLL_TXT + main.barometer_above_sll.trim();          
      configuratie_regels[9] =  main.BAROMETER_KEEL_TO_SLL_TXT + main.keel_sll.trim();                   
      configuratie_regels[10] = main.PRESSURE_READING_MSL_TXT + main.pressure_reading_msl_yes_no.trim(); 
      configuratie_regels[11] = main.AIR_TEMP_EXPOSURE_TXT + main.air_temp_exposure.trim();              
      configuratie_regels[12] = main.SST_EXPOSURE_TXT + main.sst_exposure.trim();                        
      configuratie_regels[13] = main.MAX_HEIGHT_DECK_CARGO_TXT + main.max_height_deck_cargo.trim();       
      configuratie_regels[14] = main.DIFF_SLL_WL_TXT + main.diff_sll_wl.trim();                          
      configuratie_regels[15] = main.OBS_EMAIL_RECIPIENT_TXT + main.obs_email_recipient;                 
      configuratie_regels[16] = main.OBS_EMAIL_SUBJECT_TXT + main.obs_email_subject;                     
      configuratie_regels[17] = main.LOGS_DIR_TXT + main.logs_dir;                         
      configuratie_regels[18] = main.LOGS_EMAIL_RECIPIENT_TXT + main.logs_email_recipient;               
      configuratie_regels[19] = main.WIND_UNITS_TXT + main.wind_units.trim();                            
      configuratie_regels[20] = main.RS232_INSTRUMENT_TYPE_TXT + main.RS232_connection_mode;             
      configuratie_regels[21] = main.RS232_BITS_PER_SEC_TXT + main.bits_per_second;                      
      configuratie_regels[22] = main.RS232_DATA_BITS_TXT + main.data_bits;                               
      configuratie_regels[23] = main.RS232_PARITY_TXT + main.parity;                                     
      configuratie_regels[24] = main.RS232_STOP_BITS_TXT + main.stop_bits;                               
      configuratie_regels[25] = main.RS232_PREFERED_COM_PORT_TXT + main.prefered_COM_port_number;        
      configuratie_regels[26] = main.IC_BAROMETER_TXT + main.barometer_instrument_correction;           
      configuratie_regels[27] = main.OBS_FORMAT_TXT + main.obs_format.trim();                            
      configuratie_regels[28] = main.FORMAT_101_ENCRYPTION_TXT + main.obs_101_encryption.trim();        
      configuratie_regels[29] = main.FORMAT_101_EMAIL_TXT + main.obs_101_email.trim();                   
      configuratie_regels[30] = main.RS232_PREF_COM_PORT_NAME_TXT + main.prefered_COM_port_name;   
      configuratie_regels[31] = main.WOW_PUBLISH_TXT + String.valueOf(main.WOW);                        // boolean
      configuratie_regels[32] = main.WOW_SITE_ID_TXT + main.WOW_site_id;
      configuratie_regels[33] = main.WOW_PIN_TXT + main.WOW_site_pin;
      configuratie_regels[34] = main.WOW_REPORTING_INTERVAL_TXT + main.WOW_reporting_interval;
      //configuratie_regels[35] = main.WOW_AVERAGE_BARO_HEIGHT_TXT + main.WOW_average_height_barometer;
      configuratie_regels[35] = main.WOW_APR_AVERAGE_DRAUGHT_TXT + main.WOW_APR_average_draught;
      configuratie_regels[36] = main.AMOS_MAIL_TXT + String.valueOf(main.amos_mail);                    // boolean
      configuratie_regels[37] = main.RS232_GPS_TYPE_TXT + main.RS232_GPS_connection_mode;
      configuratie_regels[38] = main.RS232_GPS_BITS_PER_SEC_TXT + main.GPS_bits_per_second;
      configuratie_regels[39] = main.RS232_GPS_COM_PORT_TXT + main.prefered_GPS_COM_port_number;
      configuratie_regels[40] = main.RS232_GPS_COM_PORT_NAME_TXT + main.prefered_GPS_COM_port_name;
      configuratie_regels[41] = main.RS232_GPS_SENTENCE_TXT + main.RS232_GPS_sentence;                  // integer
      configuratie_regels[42] = main.APR_TXT + String.valueOf(main.APR);                                // boolean
      configuratie_regels[43] = main.APR_REPORTING_INTERVAL_TXT + main.APR_reporting_interval;
      configuratie_regels[44] = main.UPLOAD_URL_TXT + main.upload_URL;   
      
   }
   
    
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void schrijf_configuratie_regels()
   {       
      /* NB input/output in een GUI altijd via een SwingWorker (Core Java Volume 1 bld 795 e.v.; Volume 2 bld 37, 215) */

      /* This is a backup for writting to muffin !!! */
      /* backup (file configuration.txt) in: user dir (system defined) AND logs dir (user defined) */

      /* NB i.v.m. Swingworker backgroud proces kan het niet in 1 lus gebeuren */


      // fill array
      fill_configuratie_array();
      
      
      /*
      // to user dir
      */
      if ((user_dir != null) && (user_dir.compareTo("") != 0))
      {
         new SwingWorker<Void, Void>()
         {
            @Override
            protected Void doInBackground() throws Exception
            {
               // NB bv configuratie_regels[2]  = "wind source        : estimated; true speed and true direction"
               String volledig_path = user_dir + java.io.File.separator + CONFIGURATION_FILE;
      
                //JOptionPane.showMessageDialog(null, hulp_dir, APPLICATION_NAME + " hulp_dir", JOptionPane.WARNING_MESSAGE);
               try
               {
                  BufferedWriter out = new BufferedWriter(new FileWriter(volledig_path, false));          // false no appending
                  
                  for (int i = 0; i < MAX_AANTAL_CONFIGURATIEREGELS; i++)
                  {
                     if ((configuratie_regels[i] != null) && (configuratie_regels[i].compareTo("") != 0))
                     {
                        //System.out.println("+++ configuratie_regels[" + i + "] = " + configuratie_regels[i]);
                        
                        out.write(configuratie_regels[i]);
                        out.newLine();   // newLine(): write a line separator. The line separator string is defined by the system property line.separator, and is not necessarily a single newline ('\n') character.
                     }
                  } // for (int i = 0; i < MAX_AANTAL_CONFIGURATIEREGELS; i++)
      
                  out.close();
               } // try
               catch (Exception e)
               {
                  // No message if not writable, in the case of internet invoking (webstart) the user dir will be
                  // windows\ystem32 (or something like that) and under Vista or Windows7 write protected
                  //JOptionPane.showMessageDialog(null, "unable to write to: " + volledig_path, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               } // catch
      
               return null;
      
            } // protected Void doInBackground() throws Exception
         }.execute(); // new SwingWorker<Void, Void>()
      } // if ((user_dir != null) && (user_dir.compareTo("") != 0))


      /*
      // to logs dir
      */
      if ((logs_dir != null) && (logs_dir.compareTo("") != 0))
      {
         new SwingWorker<Void, Void>()
         {
            @Override
            protected Void doInBackground() throws Exception
            {
               // NB bv configuratie_regels[2]  = "wind source        : estimated; true speed and true direction"
               String volledig_path = logs_dir + java.io.File.separator + CONFIGURATION_FILE;

               //JOptionPane.showMessageDialog(null, hulp_dir, APPLICATION_NAME + " hulp_dir", JOptionPane.WARNING_MESSAGE);
               try
               {
                  BufferedWriter out = new BufferedWriter(new FileWriter(volledig_path, false));   // no append

                  for (int i = 0; i < MAX_AANTAL_CONFIGURATIEREGELS; i++)
                  {
                     if ((configuratie_regels[i] != null) && (configuratie_regels[i].compareTo("") != 0))
                     {
                        out.write(configuratie_regels[i]);
                        out.newLine();   // newLine(): write a line separator. The line separator string is defined by the system property line.separator, and is not necessarily a single newline ('\n') character.
                     }
                  } // for (int i = 0; i < MAX_AANTAL_CONFIGURATIEREGELS; i++)

                  out.close();
               } // try
               catch (Exception e)
               {
                  JOptionPane.showMessageDialog(null, "unable to write to: " + volledig_path, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               } // catch

               return null;

            } // protected Void doInBackground() throws Exception
         }.execute(); // new SwingWorker<Void, Void>()
      } // if ((logs_dir != null) && (logs_dir.compareTo("") != 0))
   } 


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void lees_configuratie_regels()
   {
      // called from: initComponents2()
      // 
      // or
      //
      // could also be:
      // An alternative if function read_muffin() failed (geen persistentService of geen muffin aanwezig) !!! 
      // on 2 locations configuration.txt present: - logs_dir (user defined in online mode or fixed subdir in offline mode)
      //                                           - user_dir (system defined)
      //
      // NB input/output GUI always via a SwingWorker (Core Java Volume 1 bld 795 e.v.; Volume 2 bld 37, 215) 

       
      
      // initialisation
      hulp_dir = "";

      if ((logs_dir != null) && (logs_dir.compareTo("") != 0))
      {
         hulp_dir = logs_dir;
      }
      else if ((user_dir != null) && (user_dir.compareTo("") != 0))
      {
         hulp_dir = user_dir;
      }


      if ((hulp_dir != null) && (hulp_dir.compareTo("") != 0))
      {
         new SwingWorker<Void, Void>()
         {
            @Override
            protected Void doInBackground() throws Exception
            {
               // NB e.g. configuratie_regels[2]  = "wind source        : estimated; true speed and true direction"
               int teller;
               String file_line;
               String volledig_path = hulp_dir + java.io.File.separator + CONFIGURATION_FILE;
      
               for (teller = 0; teller < MAX_AANTAL_CONFIGURATIEREGELS; teller++)
               {
                  configuratie_regels[teller] = "";
               }
      
               /* read all lines from configuration file */
               try
               {
                  BufferedReader in = new BufferedReader(new FileReader(volledig_path));
         
                  teller = 0;
                  while((file_line = in.readLine()) != null)
                  {
                     configuratie_regels[teller] = file_line;
                     teller++;
            
                     /* for safety */
                     if (teller >= MAX_AANTAL_CONFIGURATIEREGELS)
                     {
                        break;
                     }
            
                  } // while((file_line = in.readLine()) != null)
                  in.close();
               } // try
               catch (Exception e)
               {
                  // do nothing, it is possible (at first use) that the file was never created
               } // catch

               /* put collected meta data from configuration file into appropriate global vars */
               meta_data_from_configuration_regels_into_global_vars();

               return null;

            } // protected Void doInBackground() throws Exception

            @Override
            protected void done()
            {
               // "TurboWin+ started" message to log (logs dir must be known!)
               log_turbowin_system_message("[GENERAL] started " + APPLICATION_NAME + " " + application_mode + " " + APPLICATION_VERSION);
               
               // deleting old (> 3 months) turbowin system logs
               log_turbowin_system_message("[GENERAL] deleting old (> 3 months) " + APPLICATION_NAME + " system logs");
               delete_logs_turbowin_system();
               
               /* call sign and masked call sign update on main (progress) screen" */
               call_sign_fields_update();
 
               /* meta data (mystationdata.java), eg IMO number and call sign, must be present */
               check_meta_data();

               /* check immt size (main.java) */
               check_immt_size();
               
 ////////////////////////////////////////// TEST //////////////////////////////////////////
 //              RS232_connection_mode = 6;     // mintakaStar WiFi
 ////////////////////////////////////////// TEST //////////////////////////////////////////               
               
               
               /* RS232/RS422/Wifi */
               if (RS232_connection_mode == 1 || RS232_connection_mode == 2 || RS232_connection_mode == 4 || RS232_connection_mode == 5)  // PTB220 or PTB330 or Mintaka Duo or Mintaka Star USB
               {   
                  RS232_RS422.RS232_initComponents();
               }
               else if (RS232_connection_mode == 3)                          // EUCOS AWS conncted
               {
                  /* in AWS mode Output -> obs to file etc. disable */
                  disable_aws_output_menu_items();
                  
                  /* in AWS mode no dew point but relative humidity */
                  jLabel40.setText("relative humidity");
                  
                  /* in AWS mode update info */
                  jLabel8.setText("--- sensor data updated every minute ---");
                  
                  RS232_RS422.RS422_initComponents();
               }
               else if (RS232_connection_mode == 6)                        // Mintaka Star WiFi
               {
                  RS232_RS422.WiFi_initComponents();
               }
               
               /* check date and time (and ask observer for confirmation) */
               if (RS232_connection_mode != 3)                             // not AWS connected mode
               {
                  check_and_set_datetime_v2();
               }
               else
               {
                  use_system_date_time_for_updating = false;               // in AWS connected mode the date time is updated when reading the incoming AWS measured data
               }
               
               /* gray (disable) the not appropriate graph menu selection options */
               disable_graph_menu_items();
               
               /* if not AWS connected -> disable the "Obs to AWS" menu item */
               if (RS232_connection_mode != 3)                            // not AWS connected mode
               {
                  jMenuItem46.setEnabled(false);                          // Obs to AWS
               }
               
               /* GPS connected ? */
               if (RS232_GPS_connection_mode == 1)   // 0 = no GPS; 1 = GPS (NMEA 1083)
               {
                  RS232_RS422.RS232_GPS_NMEA_0183_initComponents();
               }
               
            }
         }.execute(); // new SwingWorker<Void, Void>()

         /* Hij komt alleen in deze functie als het lezen van de muffin niet gelukt was */
         /* het kan zijn dat de muffin er niet was omdat de temporary internet files net verwijderd waren */
         /* (via java control panel) */
         /* Daarom dan nu proberen weer naar muffin te schrijven */
         //set_muffin();
         if (offline_mode_via_cmd == false)   // so offline_via_jnlp mode or online (webstart) mode
         {
            set_muffin();
         }   

      } // if ((hulp_dir != null) && (hulp_dir.compareTo("") != 0))
   }

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void disable_graph_menu_items()
{
   // NOTE
   // Even in the case of the settings indicate a barometer or AWS connected but the program couldn't find one (defaultPort == null)
   // you can still open the graphs for checking 'old' values via the main menu bar
   //
   // NOTE 
   // in that case with the right mouse button clicking on the minimised icon (iconfied) than in case of defaultPort == null
   // the graph options are disabled
   
   
   /* gray (disable) graph menu selection options */
   if (RS232_connection_mode == 0)                                                                  // no instrument connected
   {
      jMenuItem41.setEnabled(false);               // menu item graph air pressure
      jMenuItem43.setEnabled(false);               // menu item graph air temp
      jMenuItem44.setEnabled(false);               // menu item graph SST
      jMenuItem45.setEnabled(false);               // menu item graph wind speed
      jMenuItem47.setEnabled(false);               // menu item graph wind dir
      jMenuItem51.setEnabled(false);               // menu item graph total (pressure, air temp, wind dir, wind speed)
   }
   else if (RS232_connection_mode == 1 || RS232_connection_mode == 2 || RS232_connection_mode == 4 || RS232_connection_mode == 5 || RS232_connection_mode == 6) // PTB220 or PTB330 or Mintaka Duo or Mintaka Star USB or Mintaka Star WiFiconnected
   {
      jMenuItem41.setEnabled(true);                // menu item graph air pressure
      jMenuItem43.setEnabled(false);               // menu item graph air temp
      jMenuItem44.setEnabled(false);               // menu item SST
      jMenuItem45.setEnabled(false);               // menu item graph wind speed
      jMenuItem47.setEnabled(false);               // menu item graph wind dir
      jMenuItem51.setEnabled(false);               // menu item graph total (pressure, air temp, wind dir, wind speed)
   }
   else if (RS232_connection_mode == 3)                                                             // AWS connected
   {
      jMenuItem41.setEnabled(true);                // menu item graph air pressure
      jMenuItem43.setEnabled(true);                // menu item graph air temp
      jMenuItem44.setEnabled(true);                // menu item graph SST
      jMenuItem45.setEnabled(true);                // menu item graph wind speed (wind speed gust included as a second line)
      jMenuItem47.setEnabled(true);                // menu item graph wind dir
      jMenuItem51.setEnabled(true);                // menu item graph total (pressure, air temp, wind dir, wind speed)
   }         
            
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void meta_data_from_configuration_regels_into_global_vars()
{
   /* put collected meta data from muffin (or configuration file if read muffin failed) into appropriate global vars */
   for (int teller = 0; teller < MAX_AANTAL_CONFIGURATIEREGELS; teller++)
   {
      if ((configuratie_regels[teller] != null) && (configuratie_regels[teller].compareTo("") != 0))
      {
         // ship name
         if (configuratie_regels[teller].indexOf(SHIP_NAME_TXT) != -1)
         {
            // zo ja, dan staat op een bepaalde pos (achter de : ) de inhoud
            ship_name = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // imo number
         if (configuratie_regels[teller].indexOf(IMO_NUMBER_TXT) != -1)
         {
            imo_number = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // call sign
         if (configuratie_regels[teller].indexOf(CALL_SIGN_TXT) != -1)
         {
            call_sign = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // masked call sign
         if (configuratie_regels[teller].indexOf(MASKED_CALL_SIGN_TXT) != -1)
         {
            masked_call_sign = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // time zone computer
         if (configuratie_regels[teller].indexOf(TIME_ZONE_COMPUTER_TXT) != -1)
         {
            time_zone_computer = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // recruiting country
         if (configuratie_regels[teller].indexOf(RECRUITING_COUNTRY_TXT) != -1)
         {
            recruiting_country = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // method determining waves
         if (configuratie_regels[teller].indexOf(METHOD_WAVES_TXT) != -1)
         {
            method_waves = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // wind meta data
         if (configuratie_regels[teller].indexOf(WIND_SOURCE_TXT) != -1)
         {
            wind_source = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         if (configuratie_regels[teller].indexOf(MAX_HEIGHT_DECK_CARGO_TXT) != -1)
         {
            max_height_deck_cargo = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         if (configuratie_regels[teller].indexOf(DIFF_SLL_WL_TXT) != -1)
         {
            diff_sll_wl = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // air pressure meta data
         if (configuratie_regels[teller].indexOf(BAROMETER_ABOVE_SLL_TXT) != -1)
         {
            barometer_above_sll = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         if (configuratie_regels[teller].indexOf(BAROMETER_KEEL_TO_SLL_TXT) != -1)
         {
            keel_sll = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         if (configuratie_regels[teller].indexOf(PRESSURE_READING_MSL_TXT) != -1)
         {
            pressure_reading_msl_yes_no = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // air temp exposure
         if (configuratie_regels[teller].indexOf(AIR_TEMP_EXPOSURE_TXT) != -1)
         {
            air_temp_exposure = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // sst exposure
         if (configuratie_regels[teller].indexOf(SST_EXPOSURE_TXT) != -1)
         {
            sst_exposure = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // Obs E-mail recipient
         if (configuratie_regels[teller].indexOf(OBS_EMAIL_RECIPIENT_TXT) != -1)
         {
            obs_email_recipient = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // Obs E-mail subject
         if (configuratie_regels[teller].indexOf(OBS_EMAIL_SUBJECT_TXT) != -1)
         {
            obs_email_subject = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // logs dir (for immt.log etc.)
         if (configuratie_regels[teller].indexOf(LOGS_DIR_TXT) != -1)
         {
            logs_dir = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // logs E-mail recipient
         if (configuratie_regels[teller].indexOf(LOGS_EMAIL_RECIPIENT_TXT) != -1)
         {
            logs_email_recipient = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }

         // wind units (knots or m/s)
         if (configuratie_regels[teller].indexOf(WIND_UNITS_TXT) != -1)
         {
            wind_units = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }
           

         // RS232 INSTRUMENT connection mode (= RS232 connected instrument type -none, PTB220, PTB330, SAWS-)
         if (configuratie_regels[teller].indexOf(RS232_INSTRUMENT_TYPE_TXT) != -1)
         {
            RS232_connection_mode = Integer.parseInt(configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD));
         }
        
         // RS232 INSTRUMENT bits per sec
         if (configuratie_regels[teller].indexOf(RS232_BITS_PER_SEC_TXT) != -1)
         {
            bits_per_second = Integer.parseInt(configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD));
         }
         
         // RS232 INSTRUMENT data bits
         if (configuratie_regels[teller].indexOf(RS232_DATA_BITS_TXT) != -1)
         {
            String hulp_data_bits = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
            
            switch (hulp_data_bits)  
            {
               case "7"  : data_bits = SerialPort.DATABITS_7;
                                       break;
               case "8"  : data_bits = SerialPort.DATABITS_8;
                                       break;
               default   : data_bits = 0;                                // non existing (data bits) value
                                       break;
            } // switch (hulp_data_bits)
         }

         // RS232 INSTRUMENT parity
         if (configuratie_regels[teller].indexOf(RS232_PARITY_TXT) != -1)
         {
            String hulp_parity = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
            
            switch (hulp_parity)
            {
               case "0" : parity = SerialPort.PARITY_NONE;
                          break;
               case "1" : parity = SerialPort.PARITY_ODD;
                          break;
               case "2" : parity = SerialPort.PARITY_EVEN;
                          break;
               default  : parity = 99;                         // non existing (parity) value
                          break;
            } // switch (hulp_parity)            
         }
         
         // RS232 INSTRUMENT stop bits
         if (configuratie_regels[teller].indexOf(RS232_STOP_BITS_TXT) != -1)
         {
            String hulp_stop_bits = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
            
            switch (hulp_stop_bits)  
            {
               case "1"  : stop_bits = SerialPort.STOPBITS_1;
                                       break;
               case "2"  : stop_bits = SerialPort.STOPBITS_2;
                                       break;
               default   : stop_bits = 0;                       // non existing (stop bits) value
                                       break;
            } // switch (hulp_stop_bits)            
         }
         
         // RS232 INSTRUMENT prefered COM port (Windows and Linux)
         if (configuratie_regels[teller].indexOf(RS232_PREFERED_COM_PORT_TXT) != -1)
         {
            prefered_COM_port_number = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }   
         
         // ic (instrument correction) barometer
         if (configuratie_regels[teller].indexOf(IC_BAROMETER_TXT) != -1)
         {
            barometer_instrument_correction = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }
         
         // obs format (FM13 or format 101)
         if (configuratie_regels[teller].indexOf(OBS_FORMAT_TXT) != -1)
         {
            obs_format = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }
         
         // format 101 call sign encryption (yes or no)
         if (configuratie_regels[teller].indexOf(FORMAT_101_ENCRYPTION_TXT) != -1)
         {
            obs_101_encryption = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }
         
         // format 101 email (body or attachement)
         if (configuratie_regels[teller].indexOf(FORMAT_101_EMAIL_TXT) != -1)
         {
            obs_101_email = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }
         
         // RS232 INSTRUMENT prefered COM port name (OS X)
         if (configuratie_regels[teller].indexOf(RS232_PREF_COM_PORT_NAME_TXT) != -1)
         {
            prefered_COM_port_name = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }         
         
         // WOW publish?
         if (configuratie_regels[teller].indexOf(WOW_PUBLISH_TXT) != -1)
         {
            WOW = Boolean.valueOf(configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD));
            // NB You have to be carefull when using Boolean.valueOf(string) or Boolean.parseBoolean(string). 
            //    The reason for this is that the methods will always return false if the String is not equal to "true" (the case is ignored).
            //    For example: Boolean.valueOf("YES") -> false
            //    BUT no problem here because automatically genenerated in WOW settings
         }  
         
         // WOW site id
         if (configuratie_regels[teller].indexOf(WOW_SITE_ID_TXT) != -1)
         {
            WOW_site_id = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }           
         
         // WOW pin
         if (configuratie_regels[teller].indexOf(WOW_PIN_TXT) != -1)
         {
            WOW_site_pin = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }           
         
         // WOW reporting(upload) interval
         if (configuratie_regels[teller].indexOf(WOW_REPORTING_INTERVAL_TXT) != -1)
         {
            WOW_reporting_interval = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }           
         
         // WOW/APR average draught
         if (configuratie_regels[teller].indexOf(WOW_APR_AVERAGE_DRAUGHT_TXT) != -1)
         {
            WOW_APR_average_draught = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }         
         
         // default E-mail program on this computer is AMOS Mail?
         if (configuratie_regels[teller].indexOf(AMOS_MAIL_TXT) != -1)
         {
            amos_mail = Boolean.valueOf(configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD));
            // NB You have to be carefull when using Boolean.valueOf(string) or Boolean.parseBoolean(string). 
            //    The reason for this is that the methods will always return false if the String is not equal to "true" (the case is ignored).
            //    For example: Boolean.valueOf("YES") -> false
            //    BUT no problem here because automatically genenerated in email settings
         }   
         
         // RS232 GPS connection mode
         if (configuratie_regels[teller].indexOf(RS232_GPS_TYPE_TXT) != -1)
         {
            RS232_GPS_connection_mode = Integer.parseInt(configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD));
         }
         
         // RS232 GPS bits per second
         if (configuratie_regels[teller].indexOf(RS232_GPS_BITS_PER_SEC_TXT) != -1)
         {
            GPS_bits_per_second = Integer.parseInt(configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD));
         }         
        
         // RS232 prefered GPS COM port (Windows and Linux)
         if (configuratie_regels[teller].indexOf(RS232_GPS_COM_PORT_TXT) != -1)
         {
            prefered_GPS_COM_port_number = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }          
         
         // RS232 prefered GPS COM port (Windows and Linux)
         if (configuratie_regels[teller].indexOf(RS232_GPS_COM_PORT_NAME_TXT) != -1)
         {
            prefered_GPS_COM_port_name = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }   
         
         // RS232 GPS sentence (RMC or GGA)
         if (configuratie_regels[teller].indexOf(RS232_GPS_SENTENCE_TXT) != -1)
         {
            RS232_GPS_sentence = Integer.parseInt(configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD));
         }
         
         // APR (Automated Pressure Reports)?
         if (configuratie_regels[teller].indexOf(APR_TXT) != -1)
         {
            APR = Boolean.valueOf(configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD));
            // NB You have to be carefull when using Boolean.valueOf(string) or Boolean.parseBoolean(string). 
            //    The reason for this is that the methods will always return false if the String is not equal to "true" (the case is ignored).
            //    For example: Boolean.valueOf("YES") -> false
            //    BUT no problem here because automatically genenerated in APR settings
         }  
         
         // APR reporting(upload) interval
         if (configuratie_regels[teller].indexOf(APR_REPORTING_INTERVAL_TXT) != -1)
         {
            APR_reporting_interval = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }   
         
         // upload URL
         if (configuratie_regels[teller].indexOf(UPLOAD_URL_TXT) != -1)
         {
            upload_URL = configuratie_regels[teller].substring(CONFIGURATION_FILE_POS_INHOUD);
         }   
         
         
      } // if ((configuratie_regels[teller] != null) etc.
   } // for (teller = 0; teller < MAX_AANTAL_CONFIGURATIEREGELS; teller++)

   
   // generic name "prefered_COM_port" will be used [main_RS232_RS422.java]
   //
   if (prefered_COM_port_number.trim().equals(""))                    // So no Windows or Linux com port number selected
   {
      prefered_COM_port = prefered_COM_port_name;                     // OS X
   }
   else
   {
      prefered_COM_port = prefered_COM_port_number;                   // Windows and Linux
   }   
   
   // generic name "prefered_GPS_COM_port" will be used [main_RS232_RS422.java]
   //
   if (prefered_GPS_COM_port_number.trim().equals(""))                // So no Windows or Linux GPS com port number selected
   {
      prefered_GPS_COM_port = prefered_GPS_COM_port_name;             // OS X
   }
   else
   {
      prefered_GPS_COM_port = prefered_GPS_COM_port_number;           // Windows and Linux
   }   
   


}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private static void check_meta_data()
{
   String info = "";

   
   // on start up
   if (ship_name.trim().equals("") == true || ship_name.trim().length() < 2)
   {
      info = "Ship name: unknown (select: Maintenance -> Station data)";
   }
   else if (imo_number.trim().equals("") == true || imo_number.trim().length() < 2)
   {
      info = "IMO number: unknown (select: Maintenance -> Station data)";
   }
   else if (call_sign.trim().equals("") == true || call_sign.trim().length() < 2)
   {
      info = "Call sign: unknown (select: Maintenance -> Station data)";
   }
   //else if (time_zone_computer.trim().equals("") == true || time_zone_computer.trim().length() < 2)
   //{
   //   info = "Time zone computer: unknown (select: Maintenance -> Station data)";
   //}
   else if (recruiting_country.trim().equals("") == true || recruiting_country.trim().length() < 2)
   {
      info = "Recruiting country: unknown (select: Maintenance -> Station data)";
   }
   else if (wind_source.trim().equals("") == true || wind_source.trim().length() < 2)
   {
      info = "Wind source (measured/estimated): unknown (select: Maintenance -> Station data)";
   }
   else if (max_height_deck_cargo.trim().equals("") == true || max_height_deck_cargo.trim().length() < 1)
   {
      info = "Maximum height deck cargo: unknown (select: Maintenance -> Station data)";
   }
   else if (diff_sll_wl.trim().equals("") == true || diff_sll_wl.trim().length() < 1)
   {
      info = "Difference SLL and water line: unknown (select: Maintenance -> Station data)";
   }
   else if (pressure_reading_msl_yes_no.trim().equals("") == true || pressure_reading_msl_yes_no.trim().length() < 2)
   {
      info = "Air pressure reading indication: unknown (select: Maintenance -> Station data)";
   }
   else if (barometer_above_sll.trim().equals("") == true || barometer_above_sll.trim().length() < 1)
   {
      info = "Height of the barometer above SLL: unknown (select: Maintenance -> Station data)";
   }
   else if (keel_sll.trim().equals("") == true || keel_sll.trim().length() < 1)
   {
      info = "Distance of bottom of the keel to SLL: unknown (select: Maintenance -> Station data)";
   }
   else if (air_temp_exposure.trim().equals("") == true || air_temp_exposure.trim().length() < 2)
   {
      info = "Air temp exposure: unknown (select: Maintenance -> Station data)";
   }
   else if (sst_exposure.trim().equals("") == true || sst_exposure.trim().length() < 2)
   {
      info = "Sea water temp exposure: unknown (select: Maintenance -> Station data)";
   }
   else if (logs_dir.trim().equals("") == true || logs_dir.trim().length() < 2)
   {
      info = "Logs folder unknown (select: Maintenance -> Log files settings)";
   }
   else if (obs_format.trim().equals("") == true || obs_format.trim().length() < 2)
   {
      info = "obs format unknown (select: Maintenance -> Obs format setting)";
   }
   //
   // NB no warning message if obs_email_recipient, obs_email_subject or logs_email_recipient is unknown

   
   // extra check on combination format AWS and AWS connected
   if ( (main.obs_format.equals(main.FORMAT_AWS) == true) && (main.RS232_connection_mode != 3))  // RS232_connection_mode = 3 = AWS connected
   {
       info = "if obs format = \"AWS connected\" (see Maintenance -> Obs format setting) then set also the AWS connection (select: Maintenance -> Serial/USB connection settings)";      
   }
       
       
   // if info/warning available -> pop-up message
   if (info.compareTo("") != 0)
   {
      JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " warning", JOptionPane.WARNING_MESSAGE);
   }
}



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
    private void Output_Obs_to_server_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Output_Obs_to_server_menu_actionPerformed

       new SwingWorker<Boolean, Void>()
       {
          @Override
          protected Boolean doInBackground() throws Exception
          {
             boolean doorgaan = false;

             // compose coded obs
             String SPATIE = SPATIE_OBS_SERVER;                                     // use "_" as marker between obs groups
             obs_write = compose_coded_obs(SPATIE);

             if (obs_write.compareTo(UNDEFINED) != 0)
             {
                doorgaan = true;
             }
             else
             {
                doorgaan = false;
                String info = "Call sign, date/time or position not inserted";
                JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
             } // else


             // check if logs dir was entered
             if (doorgaan == true)
             {
                if (logs_dir.trim().equals("") == true || logs_dir.trim().length() < 2)
                {
                   doorgaan = false;
                   String info = "logs folder unknown, select: Maintenance -> Log files settings and retry";
                   JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                }
             } // if (doorgaan == true)

             
             // check if upload URL was entered (only for format 101)
             if (doorgaan == true)
             {
                if ( (obs_format.equals(FORMAT_101)) && (upload_URL.equals("") || upload_URL == null) )
                {
                   doorgaan = false;
                   String info = "upload URL unknown, select: Maintenance -> Server settings and retry";
                   JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                }
             } // if (doorgaan == true)
             

             // position + time sequence checks
             if (doorgaan == true)
             {
                bepaal_last_record_uit_immt();
                doorgaan = position_sequence_check();

                if (doorgaan)
                {
                   doorgaan = Check_Land_Sea_Mask();
                }
             } // if (doorgaan == true)
             
             
             // if appropriate make a format 101 obs
             if (doorgaan == true)
             {
                if (obs_format.equals(FORMAT_101))
                {
                   // NB although "obs_write = compose_coded_obs(SPATIE);" (makes FM13 record)  see above is not necessary for the compressed obs 
                   //    it is useful because parts of it will be used for checking position etc.
               
                   format_101_class = new FORMAT_101();
                   format_101_class.compress_and_decompress_101_control_center();
                } // if (obs_format.equals(FORMAT_101))
             } // if (doorgaan == true)


             return doorgaan;
               
          } // protected Void doInBackground() throws Exception

          @Override
          protected void done()
          {
             try
             {
                boolean doorgaan = get();
                //if (doorgaan == true)
                //{
                //   Output_Obs_to_server();                // in this fuction also IMMT_log() (immt log storage)
                //}
                if (doorgaan == true)
                {
                   if (obs_format.equals(FORMAT_FM13))
                   {
                      Output_obs_to_server_FM13();
                   }
                   else if (obs_format.equals(FORMAT_101))
                   {
                      Output_obs_to_server_format_101();
                   }
                   else
                   {
                      String info = "obs format unknown (select: Maintenance -> Obs format setting)";
                      JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " warning", JOptionPane.WARNING_MESSAGE);                      
                      System.out.println("+++ Not supported obs format in Function: Output_Obs_to_server_menu_actionPerformed()");
                   }  // else    
                } // if (doorgaan == true)  
             } // try
             catch (InterruptedException | ExecutionException ex) 
             {   
                System.out.println("+++ Error in Function: Output_Obs_to_server_menu_actionPerformed(). " + ex); 
             }
             
          } // protected void done()
       }.execute(); // new SwingWorker<Void, Void>()
               
   }//GEN-LAST:event_Output_Obs_to_server_menu_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
    private void Input_Wind_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Wind_menu_actionPerformed
      // TODO add your handling code here:
      //if (wind_form == null)
      //{   
      //   wind_form = new mywind();               
      //   wind_form.setSize(800, 600);
      //}
      //wind_form.setVisible(true); 
      
      mywind form = new mywind();               
      form.setSize(800, 600);
      form.setVisible(true);  
    }//GEN-LAST:event_Input_Wind_menu_actionPerformed



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_Cloudcover_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Cloudcover_menu_actionPerformed
// TODO add your handling code here:
      //if (cloudcover_form == null)
      //{   
      //   cloudcover_form = new mycloudcover();               
      //   cloudcover_form.setSize(800, 600);
      //}
      //cloudcover_form.setVisible(true); 
      
      mycloudcover form = new mycloudcover();               
      form.setSize(800, 600);
      form.setVisible(true); 

   }//GEN-LAST:event_Input_Cloudcover_menu_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_Presentweather_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Presentweather_menu_actionPerformed
// TODO add your handling code here:
      //if (presentweather_form == null)
      //{   
      //   presentweather_form = new mypresentweather();               
      //   presentweather_form.setSize(800, 600);
      //}
      //presentweather_form.setVisible(true); 
      
      mypresentweather form = new mypresentweather();               
      form.setSize(800, 600);
      form.setVisible(true); 

   }//GEN-LAST:event_Input_Presentweather_menu_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_waves_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_waves_menu_actionPerformed
// TODO add your handling code here:
      //if (waves_form == null)
      //{
      //   waves_form = new mywaves();              
      //   waves_form.setSize(800, 600);
      //}
      //waves_form.setVisible(true); 
      
      mywaves form = new mywaves();               
      form.setSize(800, 600);
      form.setVisible(true); 
   }//GEN-LAST:event_Input_waves_menu_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void observer_field_update()
   {
      //JOptionPane.showMessageDialog(null, myobserver.selected_observer, main.APPLICATION_NAME + " test", JOptionPane.WARNING_MESSAGE);
      if (myobserver.selected_observer.compareTo("") != 0)
      {
         jTextField20.setText(myobserver.selected_observer);
      }
      else
      {
         jTextField20.setText("");
      }
   }

   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void temperatures_fields_update()
   {
      ////// air temp
      // 
      //
      if (main.RS232_connection_mode == 3)                        // AWS connected mode
      {
         if (main.air_temp_from_AWS_present == true)              // NB was set in function: RS422_Read_AWS_Sensor_Data_For_Display() [file: main_RS232_RS424.java]
         {
            if (displayed_aws_data_obsolate)
            {
               jTextField36.setForeground(obsolate_color_data_from_aws);   // gray
            }
            else
            {   
               jTextField36.setForeground(main.input_color_from_aws);
            }
         }
         else
         {
            //System.out.println("+++ jTextField36.getForeground() = " + jTextField36.getForeground());
            //System.out.println("+++ main.input_color_from_aws = " + main.input_color_from_aws);
            if ((jTextField36.getForeground().getRGB() == main.input_color_from_aws.getRGB()) || (jTextField36.getForeground().getRGB() == main.obsolate_color_data_from_aws.getRGB()))
            {
               // last value was measured by AWS (color was red/gray) but now no new temp in last received aws string -> clear text field
               jTextField36.setText("");
               //System.out.println("+++ kleuren zijn het zelde");
               mytemp.air_temp = "";
            }
            
            jTextField36.setForeground(main.input_color_from_observer);
         } // else
      } // if (main.RS232_connection_mode == 3)
      else // not AWS connected
      {
         jTextField36.setForeground(main.input_color_from_observer);
      }
      
      if ((mytemp.air_temp.compareTo("") != 0) && (mytemp.air_temp != null))    
      {
         // het kan zijn dat er bv alleen staat 25 dit moet dan worden 25.0 C 
         int pos = mytemp.air_temp.indexOf(".");
         if (pos == -1)                                                        // dus geen "." in de air temp string
         {
            jTextField36.setText(mytemp.air_temp + ".0 °C"); 
         }    
         else
         { 
            jTextField36.setText(mytemp.air_temp + " °C");
         }   
      }  
      else
      {
         jTextField36.setText(""); 
      }

      
      /////// wet bulb temp
      // 
      //
      if (main.RS232_connection_mode == 3)                        // AWS connected mode
      {
         if (rh_from_AWS_present == true)
         {
            if (displayed_aws_data_obsolate)
            {
               // NB to show "NA" (see below) in color gray
               jTextField37.setForeground(obsolate_color_data_from_aws);   // gray
            }
            else
            {
              // NB to show "NA" (see below) in color red
              jTextField37.setForeground(main.input_color_from_aws);
            }
         }
         else
         {
            jTextField37.setForeground(main.input_color_from_observer);
         }
      }
      else // not AWS connected
      {
         jTextField37.setForeground(main.input_color_from_observer);
      }
      
      if (rh_from_AWS_present == true)     
      {
         jTextField37.setText("NA");        // Not Applicable
         mytemp.wet_bulb_temp = "";
      }
      else 
      {   
         if ((mytemp.wet_bulb_temp.compareTo("") != 0) && (mytemp.wet_bulb_temp != null))    
         {
            // het kan zijn dat er bv alleen staat 25 dit moet dan worden 25.0 C 
            int pos = mytemp.wet_bulb_temp.indexOf(".");
            if (pos == -1)                                                        // dus geen "." in de air temp string
            {
               jTextField37.setText(mytemp.wet_bulb_temp + ".0 °C"); 
            }    
            else
            { 
               jTextField37.setText(mytemp.wet_bulb_temp + " °C");
            }   
         }  
         else
         {   
            jTextField37.setText(""); 
         }   
      }
      
      
      /////// dew point or relative humidity
      //
      // (NB in AWS mode the AWS measured relative humidity (no dew point available) is displayed and must also be send to the AWS if measured manually) 
      //
      //
      if (main.RS232_connection_mode == 3)                  // AWS connected mode
      {
         // relative humidity (only if in AWS connected mode!)
         //
         if (rh_from_AWS_present == true)
         {
            if (displayed_aws_data_obsolate)
            {
               jTextField38.setForeground(obsolate_color_data_from_aws);   // gray
            }
            else
            {   
               jTextField38.setForeground(main.input_color_from_aws);
            }
         }
         else
         {
            if ((jTextField38.getForeground().getRGB() == main.input_color_from_aws.getRGB()) || (jTextField38.getForeground().getRGB() == main.obsolate_color_data_from_aws.getRGB()))
            {
               // last value was measured by AWS (color was red/gray ) but now no rh in last received aws string -> clear text field
               jTextField38.setText("");
               mytemp.double_rv = INVALID;
            }
            jTextField38.setForeground(main.input_color_from_observer);
         }
         
         // (first a trick for relative humidity rounding to 1 figure behind the decimal)
         double hulp_double_rv = Math.round(mytemp.double_rv * 10 * 100) / 10.0; // nb Math.round(xxx) - > geeft long terug  // NB * 100 for getting %
         if ((mytemp.double_rv != INVALID))    
            jTextField38.setText(Double.toString(hulp_double_rv) + " %");
         else
            jTextField38.setText("");
         
      } // if (main.RS232_connection_mode == 3)
      else // not AWS connected
      {   
         // dewpoint (only if in not AWS connected mode!)
         //  
         jTextField38.setForeground(main.input_color_from_observer);
         
         // (first a trick for dew point rounding to 1 figure behind the decimal)
         double hulp_double_dew_point = Math.round(mytemp.double_dew_point * 10) / 10.0; // nb Math.round(xxx) - > geeft long terug
         if ((mytemp.double_dew_point != INVALID))    
            jTextField38.setText(Double.toString(hulp_double_dew_point) + " °C");
         else
            jTextField38.setText("");
      } // else (not AWS connected)
      
      
      //////// sst temp
      // 
      //
      if (main.RS232_connection_mode == 3)                        // AWS connected mode
      {
         if (main.SST_from_AWS_present == true)
         {
            if (displayed_aws_data_obsolate)
            {
               jTextField40.setForeground(obsolate_color_data_from_aws);   // gray
            }
            else
            {
               jTextField40.setForeground(main.input_color_from_aws);
            }
         }
         else
         {
            if ((jTextField40.getForeground().getRGB() == main.input_color_from_aws.getRGB()) || (jTextField40.getForeground().getRGB() == main.obsolate_color_data_from_aws.getRGB()))
            {
               // last value was measured by AWS (color was red/gray) but now no sst in last received aws string -> clear text field
               jTextField40.setText("");
               mytemp.sea_water_temp = "";
            }
            jTextField40.setForeground(main.input_color_from_observer);
         }
      } // if (main.RS232_connection_mode == 3)
      else // not aws connected
      {
         jTextField40.setForeground(main.input_color_from_observer);
      }
      
      if ((mytemp.sea_water_temp.compareTo("") != 0) && (mytemp.sea_water_temp != null))    
      {
         // het kan zijn dat er bv alleen staat 25 dit moet dan worden 25.0 C 
         int pos = mytemp.sea_water_temp.indexOf(".");
         if (pos == -1)                                                        // dus geen "." in de air temp string
         {
            jTextField40.setText(mytemp.sea_water_temp + ".0 °C"); 
         }    
         else
         { 
            jTextField40.setText(mytemp.sea_water_temp + " °C");
         }   
      }  
      else
      {
         jTextField40.setText(""); 
      }   
      
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void wind_fields_update()
   {
      //////// true wind direction (main screen)
      // 
      //
      if (main.RS232_connection_mode == 3)                                  // AWS connected mode
      {
         if (main.true_wind_dir_from_AWS_present == true)
         {
            if (displayed_aws_data_obsolate)
            {
               jTextField17.setForeground(obsolate_color_data_from_aws);   // gray
            }
            else
            {
               jTextField17.setForeground(main.input_color_from_aws);
            }
         }
         else
         {
            if ((jTextField17.getForeground().getRGB() == main.input_color_from_aws.getRGB()) || (jTextField17.getForeground().getRGB() == main.obsolate_color_data_from_aws.getRGB()))
            {
               // last value was measured by AWS (color was red/gray) but now no new wind dir in last received aws string -> clear text field
               jTextField17.setText("");
               mywind.int_true_wind_dir = INVALID;
            }
            jTextField17.setForeground(main.input_color_from_observer);
         }
      } // if (main.RS232_connection_mode == 3) 
      else
      {
         jTextField17.setForeground(main.input_color_from_observer);
      }
      
      
      if (mywind.int_true_wind_dir == mywind.WIND_DIR_VARIABLE)
      {
         jTextField17.setText("variable");  
      }   
      else if ((mywind.int_true_wind_dir != INVALID) && (mywind.int_true_wind_dir != mywind.WIND_DIR_VARIABLE))
      {
         jTextField17.setText(Integer.toString(mywind.int_true_wind_dir) + " degr");
      }
      else
      {
         jTextField17.setText(""); 
      }
      
      
      
      //////// true wind speed (main screen)
      //
      //
      if (main.RS232_connection_mode == 3)                                 // AWS connected mode
      {
         if (main.true_wind_speed_from_AWS_present == true)
         {
            if (displayed_aws_data_obsolate)
            {
               jTextField16.setForeground(obsolate_color_data_from_aws);   // gray
            }
            else
            {
               jTextField16.setForeground(main.input_color_from_aws);
            }
         }
         else
         {
            if ((jTextField16.getForeground().getRGB() == main.input_color_from_aws.getRGB()) || (jTextField16.getForeground().getRGB() == main.obsolate_color_data_from_aws.getRGB()))
            {
               // last value was measured by AWS (color was red/gray) but now no new wind speed in last received aws string -> clear text field
               jTextField16.setText("");
               mywind.int_true_wind_speed = INVALID;
            }
            jTextField16.setForeground(main.input_color_from_observer);
         }
      } // if (main.RS232_connection_mode == 3) 
      else
      {
         jTextField16.setForeground(main.input_color_from_observer);
      }
      
      
      if (mywind.int_true_wind_speed != INVALID)
      {
         if (main.wind_units.trim().indexOf(main.M_S) != -1)
         {
            jTextField16.setText(Integer.toString(mywind.int_true_wind_speed) + " m/s");
         }
         else // thus if wind speed units knots or wind speed units unknown
         {
            jTextField16.setText(Integer.toString(mywind.int_true_wind_speed) + " knots");
         }
      }
      else
      {
         jTextField16.setText("");
      }
      
      
      // relative wind direction (not on main screen, only on wind input form)
      //
      if (main.RS232_connection_mode == 3)                        // AWS connected mode
      {
         if (main.relative_wind_dir_from_AWS_present == false)
         {
            mywind.wind_dir = "";
         }
      }
       
      
      // relative wind speed (not on main screen, only on wind input form)
      //
      if (main.RS232_connection_mode == 3)                        // AWS connected mode
      {
         if (main.relative_wind_speed_from_AWS_present == false)
         {
            mywind.wind_speed = "";
         }
      }
      
      
      // ship ground cource  (not on main screen, only on wind input form)
      //
      if (main.RS232_connection_mode == 3)                        // AWS connected mode
      {
         if (main.COG_from_AWS_present == false)
         {
            mywind.ship_ground_course = "";
         }
      }
      
      
      // ship ground speed  (not on main screen, only on wind input form)
      //
      if (main.RS232_connection_mode == 3)                        // AWS connected mode
      {
         if (main.SOG_from_AWS_present == false)
         {
            mywind.ship_ground_speed = "";
         }
      }      
      
      
      // true heading  (not on main screen, only on wind input form)
      //
      if (main.RS232_connection_mode == 3)                        // AWS connected mode
      {
         if (main.true_heading_from_AWS_present == false)
         {
            mywind.ship_heading = "";
         }
      }
      
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }

   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void clouds_low_fields_update()
   {
      if ((mycl.cl_code.compareTo("") != 0) && (mycl.cl_code != null))
         jTextField33.setText(mycl.cl_code + " (code)");
      else
         jTextField33.setText("");
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void clouds_middle_fields_update()
   {
      if ((mycm.cm_code.compareTo("") != 0) && (mycm.cm_code != null))
      {
         /* take only first char (i.c.w. special cases Cm 7a, 7b, 7c -> 7) */
         jTextField34.setText(mycm.cm_code.substring(0, 1) + " (code)");
      }
      else
         jTextField34.setText("");
  
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }
  

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void clouds_high_fields_update()
   {
      if ((mych.ch_code.compareTo("") != 0) && (mych.ch_code != null))
         jTextField35.setText(mych.ch_code + " (code)");
      else
         jTextField35.setText("");
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }
  

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void cloud_cover_fields_update()
   {
      if ((mycloudcover.N.compareTo("") != 0) && (mycloudcover.N != null))
         jTextField30.setText(mycloudcover.N);  
      else
         jTextField30.setText(""); 
      
      if ((mycloudcover.Nh.compareTo("") != 0) && (mycloudcover.Nh != null))
         jTextField31.setText(mycloudcover.Nh);  
      else
         jTextField31.setText(""); 
       
      if ((mycloudcover.h.compareTo("") != 0) && (mycloudcover.h != null))
         jTextField32.setText(mycloudcover.h);  
      else
         jTextField32.setText(""); 
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }       


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void visibility_fields_update()
   {
      // visibility
      //
      if ((myvisibility.VV.compareTo("") != 0) && (myvisibility.VV != null))
         jTextField18.setText(myvisibility.VV);  
      else
         jTextField18.setText("");   
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }
   
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void waves_fields_update()
   {
      if ((mywaves.wind_waves_period.compareTo("") != 0) && (mywaves.wind_waves_period != null)) // NB null waarde heeft het als waves input pagina nooit geopend is
         jTextField23.setText(mywaves.wind_waves_period + " sec");  
      else
         jTextField23.setText(""); 
          
      if ((mywaves.wind_waves_height.compareTo("") != 0) && (mywaves.wind_waves_height != null))
         jTextField22.setText(mywaves.wind_waves_height + " metres"); 
      else
         jTextField22.setText(""); 

      if (mywaves.swell_1_period.equals("confused"))
         jTextField26.setText(mywaves.swell_1_period);
      else if (mywaves.swell_1_period.equals("no swell"))
         jTextField26.setText(mywaves.swell_1_period);       
      else if ((mywaves.swell_1_period.compareTo("") != 0) && (mywaves.swell_1_period != null))
         jTextField26.setText(mywaves.swell_1_period + " sec");  
      else
         jTextField26.setText("");

      if (mywaves.swell_1_height.equals("confused"))
         jTextField25.setText(mywaves.swell_1_height);
      else if (mywaves.swell_1_height.equals("no swell"))
         jTextField25.setText(mywaves.swell_1_height);
      else if ((mywaves.swell_1_height.compareTo("") != 0) && (mywaves.swell_1_height != null))
         jTextField25.setText(mywaves.swell_1_height + " metres");  
      else
         jTextField25.setText("");
      
      if (mywaves.swell_1_dir.equals("confused"))
         jTextField24.setText(mywaves.swell_1_dir); 
      else if (mywaves.swell_1_dir.equals("no swell"))
         jTextField24.setText(mywaves.swell_1_dir); 
      else if ((mywaves.swell_1_dir.compareTo("") != 0) && (mywaves.swell_1_dir != null))
         jTextField24.setText(mywaves.swell_1_dir + " degr");  
      else
         jTextField24.setText("");

      if ((mywaves.swell_2_period.compareTo("") != 0) && (mywaves.swell_2_period != null))
         jTextField29.setText(mywaves.swell_2_period + " sec"); 
      else
         jTextField29.setText(""); 
      
      if ((mywaves.swell_2_height.compareTo("") != 0) && (mywaves.swell_2_height != null))
         jTextField28.setText(mywaves.swell_2_height + " metres");  
      else
         jTextField28.setText("");
      
      if ((mywaves.swell_2_dir.compareTo("") != 0) && (mywaves.swell_2_dir != null))
         jTextField27.setText(mywaves.swell_2_dir + " degr");  
      else
         jTextField27.setText(""); 
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void call_sign_fields_update()
   {
      if ((call_sign.compareTo("") != 0) && (call_sign != null))
         jTextField1.setText(call_sign);
      else
         jTextField1.setText("");
      
      if ((masked_call_sign.compareTo("") != 0) && (masked_call_sign != null))
         jTextField2.setText(masked_call_sign);
      else
         jTextField2.setText("");
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }
   

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void date_time_fields_update()
   {
      
      if (RS232_connection_mode == 3)                                 // AWS connected mode
      {
         if (displayed_aws_data_obsolate)
         {
            jTextField3.setForeground(obsolate_color_data_from_aws);   // gray
         }
         else
         {   
            jTextField3.setForeground(input_color_from_aws);
         }
      }
      else
      {
         jTextField3.setForeground(input_color_from_observer);
      }
      
      
      if ((mydatetime.day.compareTo("") != 0 && mydatetime.month.compareTo("") != 0 && mydatetime.year.compareTo("") != 0 && mydatetime.hour.compareTo("") != 0 && mydatetime.minute.compareTo("") != 0) &&
          (mydatetime.day != null && mydatetime.month != null && mydatetime.year != null && mydatetime.hour != null && mydatetime.minute != null))    
      {   
         jTextField3.setText(mydatetime.day + " " + mydatetime.month + " " + mydatetime.year + "  " + mydatetime.hour + "." + mydatetime.minute + " UTC");
      }   
      else
      {
         jTextField3.setText("");
      } 

      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();

   }  
   

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void position_fields_update()
   {
      // position
      //
      if (RS232_connection_mode == 3)                       // AWS 
      {
         if (displayed_aws_data_obsolate)
         {
            jTextField5.setForeground(obsolate_color_data_from_aws);   // gray
         }
         else
         {
            jTextField5.setForeground(main.input_color_from_aws);
         }
      }
      else
      {
         jTextField5.setForeground(main.input_color_from_observer);
      }
      
      if ((myposition.latitude_degrees.compareTo("") != 0 && myposition.latitude_minutes.compareTo("") != 0 && myposition.latitude_hemisphere.compareTo("") != 0 &&
           myposition.longitude_degrees.compareTo("") != 0 && myposition.longitude_minutes.compareTo("") != 0 && myposition.longitude_hemisphere.compareTo("") != 0) &&
          (myposition.latitude_degrees != null && myposition.latitude_minutes != null && myposition.latitude_hemisphere != null &&
           myposition.longitude_degrees != null && myposition.longitude_minutes != null && myposition.longitude_hemisphere != null))
      {
         jTextField5.setText(myposition.latitude_degrees + "° - " + myposition.latitude_minutes + "' " + myposition.latitude_hemisphere.substring(0, 0 + 1) + "  " +
                             myposition.longitude_degrees + "° - " + myposition.longitude_minutes + "' " + myposition.longitude_hemisphere.substring(0, 0 + 1));
      }
      else
      {
         jTextField5.setText(""); 
      }
      
      
      // course and speed
      //
      if (RS232_connection_mode == 3)                       // AWS 
      {
         if (displayed_aws_data_obsolate)
         {
            jTextField7.setForeground(obsolate_color_data_from_aws); // gray
         }
         else
         {   
            jTextField7.setForeground(main.input_color_from_aws);
         }
      }
      else
      {
         jTextField7.setForeground(main.input_color_from_observer);
      }
      
      if ((myposition.course.compareTo("") != 0 && myposition.speed.compareTo("") != 0) && (myposition.course != null && myposition.speed != null))
         jTextField7.setText(myposition.course + " degr" + "  " + myposition.speed + " knots");
      else
         jTextField7.setText("");  
      
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }
   

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void present_weather_fields_update()
   {
      if ((mypresentweather.present_weather.compareTo("") != 0) && (mypresentweather.present_weather != null))
         jTextField13.setText(mypresentweather.present_weather);
      else
         jTextField13.setText(""); 
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }
   

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void past_weather_fields_update()
   {
      //past weather (prim. phenomena) 
      //
      if ((mypastweather.past_weather_1.compareTo("") != 0) && (mypastweather.past_weather_1 != null))
         jTextField14.setText(mypastweather.past_weather_1);
      else
         jTextField14.setText(""); 
      
      // past weather (sec. phenomena)
      //
      if ((mypastweather.past_weather_2.compareTo("") != 0) && (mypastweather.past_weather_2 != null))
         jTextField15.setText(mypastweather.past_weather_2);
      else
         jTextField15.setText(""); 
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }
                    

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void barometer_fields_update()
   {
      // input text color setting 'air pressure reading' and 'air pressure MSL'
      // 
      if (RS232_connection_mode == 3)                       // AWS   
      {
         if (displayed_aws_data_obsolate)
         {
            jTextField9.setForeground(obsolate_color_data_from_aws);    // gray
            jTextField10.setForeground(obsolate_color_data_from_aws);   // gray
         }
         else
         {
            jTextField9.setForeground(main.input_color_from_aws);
            jTextField10.setForeground(main.input_color_from_aws);
         }
      }
      else
      {
         jTextField9.setForeground(main.input_color_from_observer);
         jTextField10.setForeground(main.input_color_from_observer);
      }
      
      
      // air pressure reading
      //
      if ( (mybarometer.pressure_reading_corrected.compareTo("") != 0) && (mybarometer.pressure_reading_corrected != null) )
          
      {
         jTextField9.setText(mybarometer.pressure_reading_corrected + " hPa"); 
      }
      else
      {
         jTextField9.setText("");   
      }
       
      // air pressure MSL
      //
      if ( (mybarometer.pressure_msl_corrected.compareTo("") != 0) && (mybarometer.pressure_msl_corrected != null) )
      {
         jTextField10.setText(mybarometer.pressure_msl_corrected + " hPa");
      }
      else
      {
         jTextField10.setText("");   
      }
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }
        
   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void barograph_fields_update()
   {
      // input text color setting 'amount of pressure tendency' and 'characteristic pressure tendency'
      // 
      if (RS232_connection_mode == 3)                                    // AWS connected
      {
         if (displayed_aws_data_obsolate)
         {
            jTextField11.setForeground(obsolate_color_data_from_aws);    // gray
            jTextField12.setForeground(obsolate_color_data_from_aws);    // gray
         }
         else
         {
            jTextField11.setForeground(main.input_color_from_aws);
            jTextField12.setForeground(main.input_color_from_aws);
         }
      }
      else
      {
         jTextField11.setForeground(main.input_color_from_observer);
         jTextField12.setForeground(main.input_color_from_observer);
      }      
      
      
      // amount of pressure tendency
      //
      if ((mybarograph.pressure_amount_tendency.compareTo("") != 0) && (mybarograph.pressure_amount_tendency != null))
      {
         jTextField11.setText(mybarograph.pressure_amount_tendency + " hPa");  
      }   
      else
      {
         jTextField11.setText("");   
      }
      
       
      // characteristic pressure tendency (a code)
      //
      if ((mybarograph.a_code.compareTo("") != 0) && (mybarograph.a_code != null))
      {
         jTextField12.setText(mybarograph.a_code + " (code)");
      }   
      else
      {
         jTextField12.setText("");   
      }
      
      
      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void icing_fields_update()
   {
      if ( ((myicing.Is_code.compareTo("") != 0)   && (myicing.Is_code != null)) ||
           ((myicing.EsEs_code.compareTo("") != 0) && (myicing.EsEs_code != null)) ||
           ((myicing.Rs_code.compareTo("") != 0)   && (myicing.Rs_code != null)) )
      {
         jTextField21.setText("present");
      }
      else
      {
         jTextField21.setText("");
      }

      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }




   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void ice_fields_update()
   {
      if ( ((myice1.ci_code.compareTo("") != 0) && (myice1.ci_code != null)) ||
           ((myice1.Si_code.compareTo("") != 0) && (myice1.Si_code != null)) ||
           ((myice1.bi_code.compareTo("") != 0) && (myice1.bi_code != null)) ||
           ((myice1.Di_code.compareTo("") != 0) && (myice1.Di_code != null)) ||
           ((myice1.zi_code.compareTo("") != 0) && (myice1.zi_code != null))
         )
      {
         // if all ice parameters "u" (unable to report) -> do not set present
         if ( (myice1.ci_code.trim().compareTo("u") != 0) ||
              (myice1.Si_code.trim().compareTo("u") != 0) ||
              (myice1.bi_code.trim().compareTo("u") != 0) ||
              (myice1.Di_code.trim().compareTo("u") != 0) ||
              (myice1.zi_code.trim().compareTo("u") != 0)
            )
         {
            jTextField19.setText("present");
         }
         else
         {
            jTextField19.setText("");
         }
      }
      else
      {
         jTextField19.setText("");
      }

      /* update of the coded obs representation (bottom line main screen) */
      coded_obs_update();
   }



   
   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_Cloudshigh_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Cloudshigh_menu_actionPerformed
// TODO add your handling code here:
      //if (ch_form == null)
      //{   
      //   ch_form = new mych();               
      //   ch_form.setSize(800, 600);
      //}
      //ch_form.setVisible(true); 
      
      mych form = new mych();               
      form.setSize(800, 600);
      form.setVisible(true); 
   }//GEN-LAST:event_Input_Cloudshigh_menu_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_Position_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Position_menu_actionPerformed
// TODO add your handling code here:
      
      // date time for Google Maps plot
      google_maps_obs_day   = mydatetime.day;         // for date-time on google maps
      google_maps_obs_month = mydatetime.month;       // for date-time on google maps
      google_maps_obs_year  = mydatetime.year;        // for date-time on google maps
      google_maps_obs_hour  = mydatetime.hour;        // for date-time on google maps
      
      // wind dir for Google Maps plot
      if (mywind.int_true_wind_dir == mywind.WIND_DIR_VARIABLE)
      {
         google_maps_obs_wind_dir = "variable";  
      }
      else if ((mywind.int_true_wind_dir != INVALID) && (mywind.int_true_wind_dir != mywind.WIND_DIR_VARIABLE))
      {
         google_maps_obs_wind_dir = Integer.toString(mywind.int_true_wind_dir) + " degr";
      }
      else
      {
         google_maps_obs_wind_dir = "";
      } 
      
      // wind speed for Google Maps plot
      if (mywind.int_true_wind_speed != INVALID)
      {
         if (main.wind_units.trim().indexOf(main.M_S) != -1)
         {
            google_maps_obs_wind_speed = Integer.toString(mywind.int_true_wind_speed) + " m/s";
         }
         else // thus if wind speed units knots or wind speed units unknown
         {
            google_maps_obs_wind_speed = Integer.toString(mywind.int_true_wind_speed) + " knots";
         }
      }
      else
      {
         google_maps_obs_wind_speed = "";
      }
      
      // air temp for Google Maps plot
      if ((mytemp.air_temp.compareTo("") != 0) && (mytemp.air_temp != null))    
      {
         // it is possible that there is only the figures eg 25 -> change to 25.0 C 
         int pos = mytemp.air_temp.indexOf(".");
         if (pos == -1)                                                        // dus geen "." in de air temp string
         {
            google_maps_obs_air_temp = mytemp.air_temp + ".0" + " &#176" + "C"; 
         }    
         else
         { 
            google_maps_obs_air_temp = mytemp.air_temp + " &#176" + "C";
         }   
      }  
      else
      {
         google_maps_obs_air_temp = ""; 
      }
      
      // SST for Google Maps plot
      if ((mytemp.sea_water_temp.compareTo("") != 0) && (mytemp.sea_water_temp != null))    
      {
         // it is possible that there is only the figures eg 25 -> change to 25.0 C  
         int pos = mytemp.sea_water_temp.indexOf(".");
         if (pos == -1)                                                        // dus geen "." in de air temp string
         {
            google_maps_obs_sst = mytemp.sea_water_temp + ".0" + " &#176" + "C";
         }    
         else
         { 
            google_maps_obs_sst =  mytemp.sea_water_temp + " &#176" + "C";
         }   
      }  
      else
      {
         google_maps_obs_sst = ""; 
      }
      
      // MSl pressure for Google Maps plot
      if ( (mybarometer.pressure_msl_corrected.compareTo("") != 0) && (mybarometer.pressure_msl_corrected != null) )
      {
         google_maps_obs_msl_pressure = mybarometer.pressure_msl_corrected + " hPa";
      }
      else
      {
         google_maps_obs_msl_pressure = "";   
      }
      

      //if (position_form == null)
      //{   
      //   position_form = new myposition();
      //   position_form.setSize(800, 600);
      //}
      //position_form.setVisible(true); 
      
      myposition form = new myposition();               
      form.setSize(800, 600);
      form.setVisible(true); 
   }//GEN-LAST:event_Input_Position_menu_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_DateTime_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_DateTime_menu_actionPerformed
// TODO add your handling code here:
      
      // NB in serial connection mose (AWS or barometer connected) after an obs was send all parameters will be set to blank, 
      //    the "date & time obs" will be automatically updated/shown again (in case AWS every minute and in case barometer every 5 minutes) 
      //    but in "no serial connection mode" we have to ask the observer again
      
      if (main.RS232_connection_mode == 0)             // no serial connection (no AWS or barometer connected)
      {
         // ask the observer if this is the correct UTC date and time of the observation (and if yes: set accordingly)
         main.check_and_set_datetime_v2();    // use_system_date_time_for_updating will be set 
      
         if (use_system_date_time_for_updating == false)
         {   
            mydatetime form = new mydatetime();               
            form.setSize(800, 600);
            form.setVisible(true); 
         }
      } // if (main.RS232_connection_mode == 0)
      else
      {
         mydatetime form = new mydatetime();               
         form.setSize(800, 600);
         form.setVisible(true); 
      } // else
      
   }//GEN-LAST:event_Input_DateTime_menu_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_Visibility_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Visibility_menu_actionPerformed
      // TODO add your handling code here:
      //if (visibility_form == null)
      //{   
      //   visibility_form = new myvisibility();               
      //   visibility_form.setSize(800, 600);
      //}
      //visibility_form.setVisible(true); 
      myvisibility form = new myvisibility();               
      form.setSize(800, 600);
      form.setVisible(true); 
   }//GEN-LAST:event_Input_Visibility_menu_actionPerformed

   

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void File_Exit_menu_actionPerformd(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_File_Exit_menu_actionPerformd
      // TODO add your handling code here:
      
      main_windowClosing(null);
      
   }//GEN-LAST:event_File_Exit_menu_actionPerformd

   

   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_Pastweather_menu_actionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Pastweather_menu_actionperformed
      // TODO add your handling code here:
      //if (pastweather_form == null)
      //{   
      //   pastweather_form = new mypastweather();               
      //   pastweather_form.setSize(800, 600);
      //}
      //pastweather_form.setVisible(true); 
      
      mypastweather form = new mypastweather();               
      form.setSize(800, 600);
      form.setVisible(true); 
   }//GEN-LAST:event_Input_Pastweather_menu_actionperformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_Cloudslow_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Cloudslow_menu_actionPerformed
       // TODO add your handling code here:
      //if (cl_form == null)
      //{   
      //   cl_form = new mycl();               
      //   cl_form.setSize(800, 600);
      //}
      //cl_form.setVisible(true); 
      
      mycl form = new mycl();               
      form.setSize(800, 600);
      form.setVisible(true); 
   }//GEN-LAST:event_Input_Cloudslow_menu_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_Cloudsmiddle_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Cloudsmiddle_menu_actionPerformed
       // TODO add your handling code here:
      //if (cm_form == null)
      //{   
      //   cm_form = new mycm();               
      //   cm_form.setSize(800, 600);
      //}
      //cm_form.setVisible(true); 
      
      mycm form = new mycm();               
      form.setSize(800, 600);
      form.setVisible(true); 
   }//GEN-LAST:event_Input_Cloudsmiddle_menu_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_Temperatures_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Temperatures_menu_actionPerformed
       // TODO add your handling code here:
      //if (temp_form == null)
      //{   
      //   temp_form = new mytemp();               
      //   temp_form.setSize(800, 600);
      //}
      //temp_form.setVisible(true); 
       
      
      mytemp form = new mytemp();               
      form.setSize(800, 600);
      form.setVisible(true); 
        
   }//GEN-LAST:event_Input_Temperatures_menu_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Maintenance_Stationdata_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Maintenance_Stationdata_actionPerformed
       // TODO add your handling code here:
      //mystationdata form = new mystationdata();
      //form.setSize(800, 600);

      mode = STATION_DATA;

      mypassword form = new mypassword();
      form.setSize(400, 300);
      form.setVisible(true);
   }//GEN-LAST:event_Maintenance_Stationdata_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_Barometer_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Barometer_menu_actionPerformed
       // TODO add your handling code here:
      //if (barometer_form == null)
      //{   
      //   barometer_form = new mybarometer();
      //   barometer_form.setSize(800, 600);
      //}
      //barometer_form.setVisible(true);
      
      mybarometer form = new mybarometer();               
      form.setSize(800, 600);
      form.setVisible(true); 

   }//GEN-LAST:event_Input_Barometer_menu_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Input_Barograph_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Barograph_menu_actionPerformed
      // TODO add your handling code here:
      //if (barograph_form == null)
      //{   
      //   barograph_form = new mybarograph();
      //   barograph_form.setSize(800, 600);
      //}
      //barograph_form.setVisible(true); 
      
      mybarograph form = new mybarograph();               
      form.setSize(800, 600);
      form.setVisible(true); 
   }//GEN-LAST:event_Input_Barograph_menu_actionPerformed


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Info_About_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Info_About_menu_actionPerformed
// TODO add your handling code here:
   about form = new about();               
   form.setSize(400, 300);
   form.setVisible(true); 
   
}//GEN-LAST:event_Info_About_menu_actionPerformed


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Output_obs_by_email_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Output_obs_by_email_actionPerformed
// TODO add your handling code here:

   // NB !!!!! email attachements zijn standaard NIET mogelijk binnen java !!!!!

   new SwingWorker<Boolean, Void>()
   {
      @Override
      protected Boolean doInBackground() throws Exception
      {
         boolean doorgaan = false;
        
         // compose coded obs
         String SPATIE = SPATIE_OBS_VIEW;                                     // use " " as marker between obs groeps
         obs_write = compose_coded_obs(SPATIE);                        // returns UNDEFINED if call sign, position or date/tome not inserted
         
         // check Obs E-mail recipient address was added
         if (obs_email_recipient.compareTo("") != 0)
         {
            doorgaan = true;
         }
         else
         {
            doorgaan = false;
      
            String info = "E-mail address recipient not inserted (Select: Maintenance -> E-mail settings)";
            JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         } // else


         // check obs must contain at least call sign, date/time and position
         if (doorgaan == true)
         {
            if (obs_write.compareTo(UNDEFINED) != 0)
            {
               doorgaan = true;
            }
            else
            {
               doorgaan = false;
               String info = "Call sign, date/time or position not inserted";
               JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            } // else
         } // if (doorgaan == true)


         // check log dir known
         if (doorgaan == true)
         {
            if (logs_dir.trim().equals("") == true || logs_dir.trim().length() < 2)
            {
               doorgaan = false;
               String info = "logs folder unknown, select: Maintenance -> Log files settings and retry";
               JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            }
         } // if (doorgaan == true)


         // position + time sequence checks
         if (doorgaan == true)
         {
            bepaal_last_record_uit_immt();
            doorgaan = position_sequence_check();

            if (doorgaan)
            {
               doorgaan = Check_Land_Sea_Mask();
            }
         } // if (doorgaan == true)

         
         // if appropriate make a format 101 obs
         if (doorgaan == true)
         {
            if (obs_format.equals(FORMAT_101))
            {
               // NB although "obs_write = compose_coded_obs(SPATIE);" (makes FM13 record)  see above is not necessary for the compressed obs 
               //    it is useful because parts of it will be used for checking position etc.
               
               format_101_class = new FORMAT_101();
               format_101_class.compress_and_decompress_101_control_center();
            } // if (obs_format.equals(FORMAT_101))
         } // if (doorgaan == true)
         

         return doorgaan;

      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         try
         {
            boolean doorgaan = get();
            if (doorgaan == true)
            {
               if (obs_format.equals(FORMAT_FM13))
               {
                  Output_obs_by_email_FM13();
               }
               else if (obs_format.equals(FORMAT_101))
               {
                  Output_obs_by_email_format_101();
               }
               else
               {
                  String info = "obs format unknown (select: Maintenance -> Obs format setting)";
                  JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " warning", JOptionPane.WARNING_MESSAGE);                  
                  System.out.println("+++ Not supported obs format in Function: Output_obs_by_email_actionPerformed()");
               }  // else 
            } // if (doorgaan == true)
         } // try
         catch (InterruptedException | ExecutionException ex) 
         { 
            System.out.println("+++ Error in Function: Output_obs_by_email_actionPerformed(). " + ex);
         }

      } // protected void done()
   }.execute(); // new SwingWorker<Void, Void>()
        

}//GEN-LAST:event_Output_obs_by_email_actionPerformed


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Output_obs_to_file_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Output_obs_to_file_actionPerformed
// TODO add your handling code here:

   new SwingWorker<Boolean, Void>()
   {
      @Override
      protected Boolean doInBackground() throws Exception
      {
         boolean doorgaan = false;

         // compose coded obs
         String SPATIE = SPATIE_OBS_VIEW;                                     // marker between obs groups
         obs_write = compose_coded_obs(SPATIE);


         // check call sign and date time entered
         if (obs_write.compareTo(UNDEFINED) != 0)
         {
            doorgaan = true;
         }
         else
         {
            doorgaan = false;
            String info = "Call sign, date/time or position not inserted";
            JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         } // else


         // check log dir known
         if (doorgaan == true)
         {
            if (logs_dir.trim().equals("") == true || logs_dir.trim().length() < 2)
            {
               doorgaan = false;
               String info = "logs folder unknown, select: Maintenance -> Log files settings and retry";
               JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            }
         } // if (doorgaan == true)

         // position + time sequence checks
         if (doorgaan == true)
         {
            bepaal_last_record_uit_immt();
            
            doorgaan = position_sequence_check();

            if (doorgaan)
            {
               doorgaan = Check_Land_Sea_Mask();
            }
         } // if (doorgaan == true)
         
         
         // if appropriate make a format 101 obs
         if (doorgaan == true)
         {
            if (obs_format.equals(FORMAT_101))
            {
               // NB although "obs_write = compose_coded_obs(SPATIE);" (makes FM13 record)  see above is not necessary for the compressed obs 
               //    it is useful because parts of it will be used for checking position etc.
               
               format_101_class = new FORMAT_101();
               format_101_class.compress_and_decompress_101_control_center();
            } // if (obs_format.equals(FORMAT_101))
         } // if (doorgaan == true)


         return doorgaan;

      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         try
         {
            boolean doorgaan = get();
            //if (doorgaan == true)
            //{
            //   Output_obs_to_file();
            //}
            if (doorgaan == true)
            {
               if (obs_format.equals(FORMAT_FM13))
               {
                  Output_obs_to_file_FM13();
               }
               else if (obs_format.equals(FORMAT_101))
               {
                  Output_obs_to_file_format_101();
               }
               else
               {
                  String info = "obs format unknown (select: Maintenance -> Obs format setting)";
                  JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " warning", JOptionPane.WARNING_MESSAGE);                  
                  System.out.println("+++ Not supported obs format in Function: Output_obs_to_file_actionPerformed()");
               }  // else 
            } // if (doorgaan == true)
         } // try
         catch (InterruptedException | ExecutionException ex) 
         { 
            System.out.println("+++ Error in Function: Output_obs_to_file_actionPerformed(). " + ex);
         }

      } // protected void done()
   }.execute(); // new SwingWorker<Void, Void>()
  
}//GEN-LAST:event_Output_obs_to_file_actionPerformed


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Maintenance_Email_settings_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Maintenance_Email_settings_actionPerformed
// TODO add your handling code here:
   //myemailsettings form = new myemailsettings();
   //form.setSize(800, 600);
   //form.setVisible(true);

   mode = EMAIL_SETTINGS;

   mypassword form = new mypassword();
   form.setSize(400, 300);
   form.setVisible(true);
}//GEN-LAST:event_Maintenance_Email_settings_actionPerformed



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Maintenance_Log_files_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Maintenance_Log_files_actionPerformed
// TODO add your handling code here:
  // mylogfiles form = new mylogfiles();
  // form.setSize(800, 600);
  // form.setVisible(true);
   mode = LOG_FILES;

   mypassword form = new mypassword();
   form.setSize(400, 300);
   form.setVisible(true);

}//GEN-LAST:event_Maintenance_Log_files_actionPerformed



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Input_Observer_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Observer_menu_actionPerformed
    // TODO add your handling code here:
   //if (observer_form == null)
   //{   
   //   observer_form = new myobserver();
   //   observer_form.setSize(800, 600);
   //} 
   //observer_form.setVisible(true);
   
   myobserver form = new myobserver();               
   form.setSize(800, 600);
   form.setVisible(true); 
}//GEN-LAST:event_Input_Observer_menu_actionPerformed





/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Kopieeren_Waarnemers_En_Aantallen()
{

   /*
   // checken welke jaartallen er in de immt.txt staan bv 2003, 2004 en 2005
   */
   //new SwingWorker<Void, Void>()
   //{
   //   @Override
   //   protected Void doInBackground() throws Exception
  //    {
         int posi;
         int w;
         int immt_position_observer;
 	      int[][] aantal_waarnemer                      = new int [MAX_AANTAL_JAREN_IN_IMMT][MAX_AANTAL_WAARNEMERS];
         boolean jaar_substring_al_aanwezig;
         String record;
         String volledig_path_immt                     = logs_dir + java.io.File.separator + IMMT_LOG;
         //String moved_observername_file                = output_dir + java.io.File.separator + OBSERVER_LOG;
         String moved_observername_file                = output_dir + java.io.File.separator + call_sign + "_" + OBSERVER_LOG;
         String[] backup_moved_observername_file_array = new String[MAX_AANTAL_JAREN_IN_IMMT];
         String[] moved_observername_file_array        = new String[MAX_AANTAL_JAREN_IN_IMMT];
         String jaar_substring;
         String waarnemer_substring                    = "";
         String observername_office;
         String immt_version;
         BufferedWriter out_0                          = null;
         BufferedWriter out_0_backup                   = null;
         BufferedWriter out_1                          = null;
         BufferedWriter out_1_backup                   = null;
         BufferedWriter out_2                          = null;
         BufferedWriter out_2_backup                   = null;
         BufferedWriter out_3                          = null;
         BufferedWriter out_3_backup                   = null;
         BufferedWriter out_4                          = null;
         BufferedWriter out_4_backup                   = null;



         /* initialisation */
         for (int p = 0; p < MAX_AANTAL_JAREN_IN_IMMT; p++)
         {
            jaar_substring_array[p] = "";
         }                                   // lege array plaats maken

         /* read all lines/records from immt log */
         try
         {
            BufferedReader in = new BufferedReader(new FileReader(volledig_path_immt));

            while((record = in.readLine()) != null)
            {
               if (record.length() > IMMT_POSITION_IMMT_VERSION)                // NB at least number greater than year in IMMT
               {
			         jaar_substring = record.substring(1, 5);                      // eg 2006
                  jaar_substring_al_aanwezig = false;

                  for (int k = 0; k < MAX_AANTAL_JAREN_IN_IMMT; k++)
                  {
                     if (jaar_substring_array[k].equals(jaar_substring))      // this year was stored before
                     {
                        jaar_substring_al_aanwezig = true;
                        break;
                     }
                  } // for (int j = 0; j < MAX_AANTAL_JAREN_IN_IMMT; j++)


                  if (jaar_substring_al_aanwezig == false)
                  {
                     for (int m = 0; m < MAX_AANTAL_JAREN_IN_IMMT; m++)
                     {
                        if (jaar_substring_array[m].equals(""))                     // empty array place
                        {
                           jaar_substring_array[m] = jaar_substring;
                           break;
                        }
                     } // for (int j = 0; j < MAX_AANTAL_JAREN_IN_IMMT; j++)
                  } // if (jaar_substring_al_aanwezig == false)

               } // if (obs_immt.length() > IMMT_POSITION_IMMT_VERSION)
            } // while((record = in.readLine()) != null)
            in.close();


         } // try
         catch (Exception e)
         {
            // do nothing, possible file was never created
         } // catch



         /*
         // download (en backup) file namen bepalen m.b.v argument "moved_observername_file" deze is bv A:\PGDE_observer.log
         //
         // dit uitbreiden met jaartallen eerder gelezen uit immt log
         // bv A:\PGDE_observer.log -> A:\PGDE_observer_2004.log
         //                         -> A:\PGDE_observer_2005.log
         //                         -> A:\PGDE_observer_2006.log
         //
         // dus nu meerdere download file namen
         */


         /* initialisation */
         for (int j = 0; j < MAX_AANTAL_JAREN_IN_IMMT; j++)
         {
            moved_observername_file_array[j] = "";
            backup_moved_observername_file_array[j] = "";
         }

         /* positie bepalen waar jaartal tussengevoegd moet worden (bv A:\observer.log -> A:\observer_2006.log) */
         posi = moved_observername_file.indexOf(".log");              // nb variable pos wordt ook gebruikt in TPoint

         /* (absolute) filenamen van de download (en download backup) observer files bepalen */

         cal_systeem_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));// geeft systeem datum tijd in UTC van dit moment
         SimpleDateFormat sdf2;
         sdf2 = new SimpleDateFormat("MMMM dd, yyyy");                            // e.g "MMMM dd, yyyy" -> februari 27, 2010
         String systeem_date_time = sdf2.format(cal_systeem_datum_tijd.getTime());

            //volledig_path_backup_srcFilename_immt = logs_dir + java.io.File.separator + "IMMT_BACKUP " + systeem_date_time + ".TXT";


         for (int j = 0; j < MAX_AANTAL_JAREN_IN_IMMT; j++)
         {
            if (jaar_substring_array[j].compareTo("") != 0)
            {
               /* eg A:\PGDE_observer_2006.log + A:\PGDE_observer_2007.log) */
               moved_observername_file_array[j] = moved_observername_file.substring(0, posi) + "_" + jaar_substring_array[j] + ".log";
               /* bepalen naam van de observername backup file (altijd op een vaste plaats) */

               /* eg PGDE_OBSERVER_2006_BACKUP November 22, 2009.txt + PGDE_OBSERVER_2007_BACKUP November 22, 2009.txt */
               //backup_moved_observername_file_array[j] = logs_dir + java.io.File.separator + "OBSERVER_" + jaar_substring_array[j] + "_BACKUP " + systeem_date_time + ".TXT";
               backup_moved_observername_file_array[j] = logs_dir + java.io.File.separator + call_sign + "_" + "OBSERVER_" + jaar_substring_array[j] + "_BACKUP " + systeem_date_time + ".TXT";
               
            } // if (jaar_substring_array[k] != "")
         } // for (int j = 0; j < MAX_AANTAL_JAREN_IN_IMMT; j++)



         
         /*
         // NB je weet hier dat de observer name file in deze fase aanwezig is
         */
      
         /* initialisation */
         for (int b = 0; b < MAX_AANTAL_WAARNEMERS; b++)
		   {
			   observername_array[b] = "";
		   }

         String volledig_path_observer = main.logs_dir + java.io.File.separator + main.OBSERVER_LOG;

         /* read all lines/records from observer log */
         try
         {
            BufferedReader in = new BufferedReader(new FileReader(volledig_path_observer));

            w = 0;
            while((record = in.readLine()) != null)
            {
		         if (w < MAX_AANTAL_WAARNEMERS)               // extra check
		         {
                  observername_array[w++] = record;
		         }
            } // while((record = in.readLine()) != null)

            in.close();
         } // try
         catch (Exception e)
         {
            // do nothing, possible file was never created
         } // catch



	      /*
	      // uitlezen van immt log file om het aantal waarnemingen per waarnemer te tellen
	      // NB je weet zeker dat de immt.log in deze fase aanwezig is
	      */

         for (int m = 0; m < MAX_AANTAL_JAREN_IN_IMMT; m++)
         {
            for (int i = 0; i < MAX_AANTAL_WAARNEMERS; i++)
            {
               aantal_waarnemer[m][i] = 0;
            }
         }

         /* read all lines/records from immt log */
         try
         {
            BufferedReader in = new BufferedReader(new FileReader(volledig_path_immt));

            while ((record = in.readLine()) != null)
            {
               /* eerst immt version bepalen want dan is pas bekend wat de positie van de waarnemer is */
               if (record.length() > IMMT_POSITION_IMMT_VERSION)
               {
                  immt_version = record.substring(IMMT_POSITION_IMMT_VERSION, IMMT_POSITION_IMMT_VERSION + 1);

                  if (immt_version.equals("3") == true)
                  {
                     immt_position_observer = IMMT_3_POSITION_OBSERVER;
                  }
                  else if (immt_version.equals("4") == true)
                  {
                     immt_position_observer = IMMT_4_POSITION_OBSERVER;
                  }
                  else if (immt_version.equals("5") == true)
                  {
                     immt_position_observer = IMMT_5_POSITION_OBSERVER;
                  }
                  else
                  {
                     immt_position_observer = INVALID;                                 //  dan zal verderop geen waarnemer uit de record worden gelezen
                  }

                  //if (record.length() > IMMT_POSITION_OBSERVER - 1)
                  if (record.length() > immt_position_observer - 1)
                  {
                     waarnemer_substring = record.substring(immt_position_observer);    //record.substring(IMMT_POSITION_OBSERVER);
                     jaar_substring      = record.substring(1, 5);                      // eg 2010

                     for (int i = 0; i < MAX_AANTAL_WAARNEMERS; i++)
                     {
                        if (observername_array[i].compareTo("") != 0)
                        {
                           /* NB waarnemer_substring: surname - space - initials */
                           /* NB observername_array[i]: surname - tab - initials - tab - rank - tab - discharge book number */
 
                           if (waarnemer_substring.equals(observername_array[i]) == true)
                           {
                              for (int m = 0; m < MAX_AANTAL_JAREN_IN_IMMT; m++)
                              {
                                 if (jaar_substring.equals(jaar_substring_array[m]))
                                 {
                                    aantal_waarnemer[m][i]++;
                                 }
                              } // for (int m = 0; m < MAX_AANTAL_JAREN_IN_IMMT; m++)
                           } // if (waarnemer_substring.equals(hulp_observername))
                        } // if (observername_array[i].compareTo("") != 0)
                     } // for (i = 0; i < MAX_AANTAL_WAARNEMERS: i++)
                  } // if (obs_immt.length() > IMMT_POSITION_OBSERVER -1)
               } // if (record.length() > IMMT_POSITION_IMMT_VERSION)
            } // while((record = in.readLine()) != null)
            in.close();
         } // try
         catch (Exception e)
         {
           // do nothing, possible file was never created
         } // catch




	      /*
	      // observers and number of made observations to download and backup file(s)
	      */

         /* open the moved(download) and backup observer files */
         if (moved_observername_file_array[0].compareTo("") != 0)
         {
            try
            {
               out_0        = new BufferedWriter(new FileWriter(moved_observername_file_array[0]));
               out_0_backup = new BufferedWriter(new FileWriter(backup_moved_observername_file_array[0]));
            } // try
            catch (Exception e) { /* ... */}
         } // if (moved_observername_file_array[0].compareTo("") != 0)
         if (moved_observername_file_array[1].compareTo("") != 0)
         {
            try
            {
               out_1        = new BufferedWriter(new FileWriter(moved_observername_file_array[1]));
               out_1_backup = new BufferedWriter(new FileWriter(backup_moved_observername_file_array[1]));
            } // try
            catch (Exception e) { /* ... */}
         } // if (moved_observername_file_array[2].compareTo("") != 0)
         if (moved_observername_file_array[2].compareTo("") != 0)
         {
            try
            {
               out_2        = new BufferedWriter(new FileWriter(moved_observername_file_array[2]));
               out_2_backup = new BufferedWriter(new FileWriter(backup_moved_observername_file_array[2]));
            } // try
            catch (Exception e) { /* ... */}
         } // if (moved_observername_file_array[2].compareTo("") != 0)
         if (moved_observername_file_array[3].compareTo("") != 0)
         {
            try
            {
               out_3        = new BufferedWriter(new FileWriter(moved_observername_file_array[3]));
               out_3_backup = new BufferedWriter(new FileWriter(backup_moved_observername_file_array[3]));
            } // try
            catch (Exception e) { /* ... */}
         } // if (moved_observername_file_array[3].compareTo("") != 0)
         if (moved_observername_file_array[4].compareTo("") != 0)
         {
            try
            {
               out_4        = new BufferedWriter(new FileWriter(moved_observername_file_array[4]));
               out_4_backup = new BufferedWriter(new FileWriter(backup_moved_observername_file_array[4]));
            } // try
            catch (Exception e) { /* ... */}
         } // if (moved_observername_file_array[4].compareTo("") != 0)




         /* write to the moved and backup files */
         for (int m = 0; m < MAX_AANTAL_JAREN_IN_IMMT; m++)
         {
            for (int i = 0; i < MAX_AANTAL_WAARNEMERS; i++)
            {
               observername_office = observername_array[i];

               if (observername_office.compareTo("") != 0)
               {
                  //observername_office += "\t";                // tab
                  observername_office += Integer.toString(aantal_waarnemer[m][i]);      // number of observations waarnemingen

                  if (m == 0 && aantal_waarnemer[m][i] != 0)
                  {
                     try
                     {
                        if (out_0 != null)
                        {   
                           out_0.write(observername_office);
                           out_0.newLine();
                        }
                     }
                     catch (IOException ex) { }
                     try
                     {
                        if (out_0_backup != null)
                        {   
                           out_0_backup.write(observername_office);
                           out_0_backup.newLine();
                        }
                     }
                     catch (IOException ex) { }
                  } // if (m == 0 && aantal_waarnemer[m][i] != 0)
                  else if (m == 1 && aantal_waarnemer[m][i] != 0)
                  {
                     try
                     { 
                        if (out_1 != null)
                        {   
                           out_1.write(observername_office);
                           out_1.newLine();
                        }
                     }
                     catch (IOException ex) { }
                     try
                     {
                        if (out_1_backup != null)
                        {   
                           out_1_backup.write(observername_office);
                           out_1_backup.newLine();
                        }
                     }
                     catch (IOException ex) {  }
                  } // else if (m == 2 && aantal_waarnemer[j][i] != 0)
                  else if (m == 2 && aantal_waarnemer[m][i] != 0)
                  {
                     try
                     {
                        if (out_2 != null)
                        {   
                           out_2.write(observername_office);
                           out_2.newLine();
                        }
                     }
                     catch (IOException ex) { }
                     try
                     {
                        if (out_2_backup != null)
                        {   
                           out_2_backup.write(observername_office);
                           out_2_backup.newLine();
                        }
                     } catch (IOException ex) { }
                  } // else if (m == 2 && aantal_waarnemer[m][i] != 0)
                  else if (m == 3 && aantal_waarnemer[m][i] != 0)
                  {
                     try
                     {
                        if (out_3 != null)
                        {   
                           out_3.write(observername_office);
                           out_3.newLine();
                        }
                     }
                     catch (IOException ex) {  }
                     try
                     {
                        if (out_3_backup != null)
                        {   
                           out_3_backup.write(observername_office);
                           out_3_backup.newLine();
                        }
                     }
                     catch (IOException ex) {  }
                  } // else if (m == 3 && aantal_waarnemer[m][i] != 0)
                  else if (m == 4 && aantal_waarnemer[m][i] != 0)
                  {
                     try
                     {
                        if (out_4 != null)
                        {   
                           out_4.write(observername_office);
                           out_4.newLine();
                        }
                     }
                     catch (IOException ex) {  }
                     try
                     {
                        if (out_4_backup != null)
                        {   
                           out_4_backup.write(observername_office);
                           out_4_backup.newLine();
                        }
                     }
                     catch (IOException ex) {  }
                  } // else if (m == 4 && aantal_waarnemer[m][i] != 0)
               } // if (observername_office != "")
            } // for (i = 0; i < MAX_AANTAL_WAARNEMERS; i++)
         } // for (int m = 0; m < MAX_AANTAL_JAREN_IN_IMMT; m++)




         /* close all the (moved and backup) observer files */
         if (moved_observername_file_array[0].compareTo("") != 0)
         {
            try
            {
               if (out_0 != null)
               {
                  out_0.close();
               }   
               if (out_0_backup != null)
               {
                  out_0_backup.close();
               }   
            } // try
            catch (Exception e) { /* ... */}
         } // if (moved_observername_file_array[0].compareTo("") != 0)
         if (moved_observername_file_array[1].compareTo("") != 0)
         {
            try
            {
               if (out_1 != null)
               {   
                  out_1.close();
               }
               if (out_1_backup != null)
               {   
                  out_1_backup.close();
               }   
            } // try
            catch (Exception e) { /* ... */}
         } // if (moved_observername_file_array[2].compareTo("") != 0)
         if (moved_observername_file_array[2].compareTo("") != 0)
         {
            try
            {
               if (out_2 != null)
               {   
                  out_2.close();
               }
               if (out_2_backup != null)
               {   
                  out_2_backup.close();
               }
            } // try
            catch (Exception e) { /* ... */}
         } // if (moved_observername_file_array[2].compareTo("") != 0)
         if (moved_observername_file_array[3].compareTo("") != 0)
         {
            try
            {
               if (out_3 != null)
               {   
                  out_3.close();
               }
               if (out_3_backup != null)
               {   
                  out_3_backup.close();
               }   
            } // try
            catch (Exception e) { /* ... */}
         } // if (moved_observername_file_array[3].compareTo("") != 0)
         if (moved_observername_file_array[4].compareTo("") != 0)
         {
            try
            {
               if (out_4 != null)
               {
                  out_4.close();
               }
               if (out_4_backup != null)
               {   
                  out_4_backup.close();
               }   
            } // try
            catch (Exception e) { /* ... */}
         } // if (moved_observername_file_array[4].compareTo("") != 0)


    //     return null;

   //    } // protected Void doInBackground() throws Exception
      
   //}.execute(); // new SwingWorker<Void, Void>()

}




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private boolean Move_log_files(final String move_mode_logs)
{
   /*
   // Note: temp_dir only for move_mode_logs = MOVE_TO_EMAIL; in move_mode_logs = MOVE_TO_DISK temp_dir is a dummy
   */
   
   /*
   // Note:  If you intend to distribute your program as an unsigned Java™ Web Start application,
   //      then instead of using the JFileChooser API you should use the file services provided
   //      by the JNLP API. These services — FileOpenService and FileSaveService — not only
   //      provide support for choosing files in a restricted environment, but also take care of
   //      actually opening and saving them. An example of using these services is in JWSFileChooserDemo.
   //      Documentation for using the JNLP API can be found in the Java Web Start lesson.
   //     (http://java.sun.com/docs/books/tutorial/uiswing/components/filechooser.html)
   */

   boolean doorgaan         = false;
   boolean doorgaan_captain = true;


   /* first check if there is an immt log source file present (and not empty) */
   volledig_path_srcFilename_immt = logs_dir + java.io.File.separator + IMMT_LOG;
   File immt_source_file = new File(volledig_path_srcFilename_immt);
   if (immt_source_file.exists() && immt_source_file.length() > 10)
   {
      doorgaan = true;
   }
   else
   {
      JOptionPane.showMessageDialog(null, "Move log files cancelled, reason: nothing to move; IMMT log (file with all stored observations for climatological use) empty ", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
      doorgaan = false;
   }


   /* OK immt log present, so continue */

   if (doorgaan == true)
   {
      /* in MOVE_TO_DISK mode filechooser dialog popup */
      if (move_mode_logs.equals(MOVE_TO_DISK) == true)
      {
         // pop-up the file/directory chooser dialog box
         //JFileChooser chooser = new JFileChooser(".");                                                         // present dir
         //JFileChooser chooser = new JFileChooser(javax.swing.filechooser.FileSystemView.getFileSystemView() ); // Constructs a JFileChooser using the given FileSystemView
         JFileChooser chooser = new JFileChooser();                                                              // Constructs a JFileChooser pointing to the user's default directory. This default depends on the operating system. It is typically the "My Documents" folder on Windows, and the user's home directory on Unix.
         chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);                                // now the user can only select directories
         int result = chooser.showSaveDialog(main.this);

         if (result == JFileChooser.APPROVE_OPTION)
         {
            output_dir = chooser.getSelectedFile().getPath();                                        // getSelectedFile() -> in this case returns not a file but a directory !

            final File dirs = new File(output_dir);

            if (dirs.exists() == false)                   // output_dir not exists
            {
               final boolean success = dirs.mkdirs();
               if (success == false)
               {
                  JOptionPane.showMessageDialog(null, "Could not create " + output_dir, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                  doorgaan = false;
               }
            } // else destination path do not exist


            // output_dir and log_dir must be different !
            if (doorgaan == true)
            {
               if (output_dir.equals(logs_dir) == true)
               {
                  JOptionPane.showMessageDialog(null, "Download folder the same as log files folder, LOG FILES NOT MOVED ", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);

                  output_dir = null;
                  doorgaan = false;
               } // if (output_dir.equals(logs_dir) == true)
            } // if (doorgaan == true)

         } // if (result == JFileChooser.APPROVE_OPTION
         else // Cancel buttom / cross
         {
            doorgaan = false;
         } // else
 
      } // if (move_mode_logs.equals(MOVE_TO_DISK) == true)
      else if (move_mode_logs.equals(MOVE_TO_EMAIL) == true)
      {
         output_dir = temp_logs_dir;
      } // else if (move_mode_logs.equals(MOVE_TO_EMAIL) == true)

   } // if doorgaan == true




   /*
   // move captain source file to captain destination file (if captain source file is present)
   */
   if (doorgaan == true)
   {
      /* captain distination file (eg PGDE_captain.log) */
      //volledig_path_dstFilename_captain = output_dir + java.io.File.separator + CAPTAIN_LOG;
      volledig_path_dstFilename_captain = output_dir + java.io.File.separator + call_sign + "_" + CAPTAIN_LOG;

      /* captain source file (captain.log) */
      volledig_path_srcFilename_captain = logs_dir + java.io.File.separator + CAPTAIN_LOG;

      /* captain backup file (eg PGDE_CAPTAIN_BACKUP May 08, 2015.TXT) */
      cal_systeem_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));// gives system date and time (UTC) of this moment
      SimpleDateFormat sdf2;
      sdf2 = new SimpleDateFormat("MMMM dd, yyyy");                            // e.g "MMMM dd, yyyy" -> februari 27, 2010
      String systeem_date_time = sdf2.format(cal_systeem_datum_tijd.getTime());
      //volledig_path_backup_srcFilename_captain = logs_dir + java.io.File.separator + "CAPTAIN_BACKUP " + systeem_date_time + ".TXT";
      volledig_path_backup_srcFilename_captain = logs_dir + java.io.File.separator + call_sign + "_" + "CAPTAIN_BACKUP " + systeem_date_time + ".TXT";

      File captain_source_file = new File(volledig_path_srcFilename_captain);
      if (captain_source_file.exists())
      {
         doorgaan_captain = captain_source_file.length() > 5;
      }
      else // no captain source file present
      {
         // NB no message necessary, because captain data (file) not mandatory (contrary to immt log)
         doorgaan_captain = false;
      }
   } // if (doorgaan == true)


   if (doorgaan == true && doorgaan_captain == true)
   {
      /* copy captain source file to destination captain file */
      new SwingWorker<String, Void>()
      {
         @Override
         protected String doInBackground() throws Exception
         {
            String result = null;

            try
            {
               // Create channel on the source
               FileChannel srcChannel = new FileInputStream(volledig_path_srcFilename_captain).getChannel();

               // Create channel on the destination
               FileChannel dstChannel = new FileOutputStream(volledig_path_dstFilename_captain).getChannel();

               // Copy file contents from source to destination
               dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

               // Close the channels
               srcChannel.close();
               dstChannel.close();

               result = "OK";
            }
            catch (IOException e)
            {
               JOptionPane.showMessageDialog(null, "Unable to move " + volledig_path_srcFilename_captain + " to " + volledig_path_dstFilename_captain, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               result = "NOT_OK";
            }

            return result;

         } // protected Void doInBackground() throws Exception

         @Override
         protected void done()
         {
            String result_opgehaald = null;

            try
            {
               result_opgehaald = get();
            }
            catch (InterruptedException | ExecutionException ex) {}

            if ( (result_opgehaald != null) && (result_opgehaald.equals("OK") == true) )
            {
               // rename sourcefile to backup file (after it was copied, see doInBackground())
               File source_file = new File(volledig_path_srcFilename_captain);
               File renamed_file = new File(volledig_path_backup_srcFilename_captain);

               if (source_file.renameTo(renamed_file) == false)
               {
                  // failed: most of the time because backup file of the same name already exist (2nd backup same day)

                  if (move_mode_logs.equals(MOVE_TO_DISK) == true)
                  {
                     // LET OP
                     // deze melding NIET geven als de files gezipped worden er daarna per email moeten worden verstuurd
                     // want dan wordt met deze melding de aanmaak van het zip bestand opgehouden, maar het email programma
                     // met een verwijzing naar dit zip bestand is echter al wel geopend !!
                     JOptionPane.showMessageDialog(null, "Backing up log file " + volledig_path_srcFilename_captain + " failed (2nd move/backup same day?)", main.APPLICATION_NAME + " info", JOptionPane.INFORMATION_MESSAGE);
                  }
                  source_file.delete();        // because backup failed, immt.log still present to avoid confusing with log files, simply delete the immt.log
               }
            } // if (result_opgehaald.equals("OK") == true)
         } // protected void done()
      }.execute(); // new SwingWorker<Void, Void>()

   } // if (doorgaan == true)




   /*
   // copy immt source file to destination and make a backup of the immt file
   //
   // + copy observer data plus number of observations (via function Kopieeren_Waarnemers_En_Aantallen())
   */
   if (doorgaan == true)
   {
      /* immt distination file (eg PGDE_immt.log) */
      //volledig_path_dstFilename_immt = output_dir + java.io.File.separator + IMMT_LOG;
      volledig_path_dstFilename_immt = output_dir + java.io.File.separator + call_sign + "_" + IMMT_LOG;

      /* immt source file (immt.log) */
      volledig_path_srcFilename_immt = logs_dir + java.io.File.separator + IMMT_LOG;

      /* immt backup file (eg PGDE_IMMT_BACKUP May 08, 2015.TXT) */
      cal_systeem_datum_tijd = new GregorianCalendar(new SimpleTimeZone(0, "UTC"));// gives system date and time (UTC) of this moment
      SimpleDateFormat sdf2;
      sdf2 = new SimpleDateFormat("MMMM dd, yyyy");                            // e.g "MMMM dd, yyyy" -> februari 27, 2010
      String systeem_date_time = sdf2.format(cal_systeem_datum_tijd.getTime());
      //volledig_path_backup_srcFilename_immt = logs_dir + java.io.File.separator + "IMMT_BACKUP " + systeem_date_time + ".TXT";
      volledig_path_backup_srcFilename_immt = logs_dir + java.io.File.separator + call_sign + "_" + "IMMT_BACKUP " + systeem_date_time + ".TXT";

      new SwingWorker<String, Void>()
      {
         @Override
         protected String doInBackground() throws Exception
         {
            String result = null;

            /*
            // count number of obs per observer per year
            */
            Kopieeren_Waarnemers_En_Aantallen();


            /*
            // copy immt
            */
            try
            {
               // Create channel on the source
               FileChannel srcChannel = new FileInputStream(volledig_path_srcFilename_immt).getChannel();

               // Create channel on the destination
               FileChannel dstChannel = new FileOutputStream(volledig_path_dstFilename_immt).getChannel();

               // Copy file contents from source to destination
               dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

               // Close the channels
               srcChannel.close();
               dstChannel.close();

               result = "OK";
            }
            catch (IOException e)
            {
               JOptionPane.showMessageDialog(null, "Unable to move " + volledig_path_srcFilename_immt + " to " + volledig_path_dstFilename_immt, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               result = "NOT_OK";
            }

            return result;

         } // protected Void doInBackground() throws Exception

         @Override
         protected void done()
         {
            String result_opgehaald = null;

            try
            {
               result_opgehaald = get();
            }
            catch (InterruptedException | ExecutionException ex) {}

            if ( (result_opgehaald != null) && (result_opgehaald.equals("OK") == true) )
            {
               if (move_mode_logs.equals(MOVE_TO_DISK) == true)
               {
                  // LET OP
                  // deze melding NIET geven als de files gezipped worden er daarna per email moeten worden verstuurd
                  // want dan wordt met deze melding de aanmaak van het zip bestand opgehouden, maar het email programma
                  // met een verwijzing naar dit zip bestand is echter al wel geopend !!

                  // Note: not possible to show this message at the end of this function (outsite the swingworker)
                  //       because Swingworker not finished
                  JOptionPane.showMessageDialog(null, "Log files moved to folder: " + output_dir, main.APPLICATION_NAME + " info", JOptionPane.INFORMATION_MESSAGE);
               }

               // rename sourcefile to backup file (after it was copied, see doInBackground())
               File source_file = new File(volledig_path_srcFilename_immt);
               File renamed_file = new File(volledig_path_backup_srcFilename_immt);

               if (source_file.renameTo(renamed_file) == false)
               {
                  // rename failed: most of the time because a backup file of the same name already exist (2nd backup same day)

                  if (move_mode_logs.equals(MOVE_TO_DISK) == true)
                  {        
                     // LET OP
                     // deze melding NIET geven als de files gezipped worden er daarna per email moeten worden verstuurd
                     // want dan wordt met deze melding de aanmaak van het zip bestand opgehouden, maar het email programma
                     // met een verwijzing naar dit zip bestand is echter al wel geopend !!
                        
                     JOptionPane.showMessageDialog(null, "Backing up log file " + volledig_path_srcFilename_immt + " failed (2nd move/backup same day?)", main.APPLICATION_NAME + " info", JOptionPane.INFORMATION_MESSAGE);
                  }
                  source_file.delete();        // because backup failed, immt.log still present to avoid confusing with log files, simply delete the immt.log
               }

               // NB
               // omdat zippen afhankelijk is van de verplaatste log files, moet dit hier in het done() deel gebeuren
               // want als je dat anders doet kan het zijn dat de swingworker die de log files veplaatst
               // nog bezig is zodat dan een i/0 zip error optreed
               //
               if (move_mode_logs.equals(MOVE_TO_EMAIL) == true)
               {
                  zip_log_files();
               }
               
            } // if (result_opgehaald.equals("OK") == true)
         } // protected void done()
      }.execute(); // new SwingWorker<Void, Void>()

   } // if (doorgaan == true)


   return doorgaan;
}




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Maintenance_Move_log_files_to_disk_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Maintenance_Move_log_files_to_disk_actionPerformed
    // TODO add your handling code here:

   String move_mode_logs = MOVE_TO_DISK;
   String info           = "";
   boolean doorgaan        = true;


   /* are you sure? */
   info = "Uploading log files should be undertaken when it is intended to return the stored log files" +
          " to the National Meteorological Service.\nDo you wish to proceed";

   if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + " message", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
   {
      doorgaan = true;
   }
   else
   {
      JOptionPane.showMessageDialog(null, "moving log files process cancelled", APPLICATION_NAME + " message", JOptionPane.INFORMATION_MESSAGE);
      doorgaan = false;
   }
   
   
   if (doorgaan == true)
   {   
      Move_log_files(move_mode_logs);   // return value of Move_log_files not from interest in this function
   }
}//GEN-LAST:event_Maintenance_Move_log_files_to_disk_actionPerformed



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Maintenance_Observer_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Maintenance_Observer_menu_actionPerformed
   // TODO add your handling code here:
   Input_Observer_menu_actionPerformed(evt);
}//GEN-LAST:event_Maintenance_Observer_menu_actionPerformed



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Maintenance_Captains_Menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Maintenance_Captains_Menu_actionPerformed
   // TODO add your handling code here:
   //if (captain_form == null)
   //{   
   //   captain_form = new mycaptain();
   //   captain_form.setSize(800, 600);
   //}
   //captain_form.setVisible(true);
   
   mycaptain form = new mycaptain();               
   form.setSize(800, 600);
   form.setVisible(true); 
}//GEN-LAST:event_Maintenance_Captains_Menu_actionPerformed



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Maintenance_Move_log_files_by_email_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Maintenance_Move_log_files_by_email_actionPerformed
   // TODO add your handling code here:
   boolean doorgaan        = true;
   String info             = "";


   /* are you sure? */
   info = "Uploading log files should be undertaken when it is intended to return the stored log files" +
          " to the National Meteorological Service.\nDo you wish to proceed";

   if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + " message", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
   {
      doorgaan = true;
   }
   else
   {
      JOptionPane.showMessageDialog(null, "moving log files process cancelled", APPLICATION_NAME + " message", JOptionPane.INFORMATION_MESSAGE);
      doorgaan = false;
   }



   /* logs dir must be set before (e.g. C:/Users/Martin/Downloads/logs) */
   if (logs_dir.trim().equals("") == true || logs_dir.trim().length() < 2)
   {
      doorgaan = false;
      info = "Logs folder unknown, select: Maintenance -> Log files settings and retry";
      JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
   }

   if (doorgaan)
   {
      /* check sub dir temp already present, if not -> create */
      temp_logs_dir = logs_dir + java.io.File.separator + "temp";
      final File dirs = new File(temp_logs_dir);

      if (dirs.exists() == false)
      {
         ////* create subdir 'temp' (e.g. C:\Users\Martin\Downloads\logs/temp) */
         ///temp_logs_dir = logs_dir + java.io.File.separator + "temp";

         final boolean success = dirs.mkdirs();
         if (success == false)
         {
            doorgaan = false;
            JOptionPane.showMessageDialog(null, "Could not create " + temp_logs_dir + ", operation cancelled", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         }
         else
         {
            doorgaan = true;
         }
      } // if (temp_logs_dir.trim().equals("") == true || temp_logs_dir.trim().length() < 2)
      else // temp sub dir already present
      {
         // delete all the files in the temp dir (remains of a previous zip action)
         final File file_logs_dir = new File(temp_logs_dir + java.io.File.separator);
         String[] filenames = file_logs_dir.list(); // Returns an array of strings naming the files and directories in the directory denoted by this abstract pathname.

         for (int i = 0; i < filenames.length; i++)
         {
            File file_to_be_deleted = new File(temp_logs_dir + java.io.File.separator + filenames[i]);
            if (file_to_be_deleted.delete() == false)
            {
               JOptionPane.showMessageDialog(null, "delete error: " + filenames[i]  + " (Maintenance_Move_log_files_by_email_actionPerformed)"  , APPLICATION_NAME + " error", JOptionPane.ERROR_MESSAGE);
            }
         } // for (int i = 0; i < filenames.length; i++)

         doorgaan = true;
      } // else

   } // if (doorgaan)


   /* move log files from logs_dir (e.g. C:\Users\Martin\Downloads\logs) to temp_logs_dir (e.g. C:\Users\Martin\Downloads\logs/temp) */
   if (doorgaan)
   {
      String move_mode_logs = MOVE_TO_EMAIL;
      doorgaan = Move_log_files(move_mode_logs);  // and zip the moved log files
   } // if (doorgaan)




   // obs and E-mail address OK -> proceed
   if (doorgaan == true)
   {
      new SwingWorker<Void, Void>()
      {
          @Override
         protected Void doInBackground() throws Exception
         {
            /*
            // Version 6 of the Java Platform, Standard Edition (Java SE), continues to narrow the gap with
            // new system tray functionality, better  print support for JTable, and now the Desktop API
            //(java.awt.Desktop API).
            //
            // Use the Desktop.isDesktopSupported() method to determine whether the Desktop API is available.
            // On the Solaris Operating System and the Linux platform, this API is dependent on Gnome libraries.
            // If those libraries are unavailable, this method will return false. After determining that the API is
            // supported, that is, the isDesktopSupported() returns true, the application can retrieve a Desktop
            // instance using the static method getDesktop().
            //
            */
            Desktop desktop = null;

            // Before more Desktop API is used, first check
            // whether the API is supported by this particular
            // virtual machine (VM) on this particular host.
            if (Desktop.isDesktopSupported())
            {
               desktop = Desktop.getDesktop();
               try
               {
                  String body_txt = "please attach manually the file: " + temp_logs_dir + java.io.File.separator + ship_name + " " + LOGS_ZIP;
                  String mail_txt = logs_email_recipient + "?subject=" + "meteo logs " + ship_name + "&body=" + body_txt;

                  URI uriMailTo = null;
                  try
                  {
                     uriMailTo = new URI("mailto", mail_txt, null);
                  }
                  catch (URISyntaxException ex)
                  {
                     JOptionPane.showMessageDialog(null, "Error invoking default Email program (URISyntaxException)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                  }

                  desktop.mail(uriMailTo);

               } // try
               catch (IOException ex)
               {
                  JOptionPane.showMessageDialog(null, "Error invoking default Email program", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               }
            } // if (Desktop.isDesktopSupported())
            else
            {
               JOptionPane.showMessageDialog(null, "Error invoking default Email program (method not supported on this computer system)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            } // else

            return null;

         } // protected Void doInBackground() throws Exception

         //@Override
         //protected void done()
         //{
         //
         //} // protected void done()

      }.execute(); // new SwingWorker<Void, Void>()
   } // if (doorgaan == true)

}//GEN-LAST:event_Maintenance_Move_log_files_by_email_actionPerformed


private void Next_form_automation_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Next_form_automation_menu_actionPerformed
   // TODO add your handling code here:

   /* initialisation */
   in_next_sequence = true;

   /* starting with the position data input screen */
   Input_Position_menu_actionPerformed(evt);

   //int seq_no_input_screen =5;

   /*
   // sequence_no_input_screen: 1  = CmShipDatetime()
   //                           2  = CmShipPosition()
   //                           3  = CmShipWind()
   //                           4  = CmShipWaves()
   //                           5  = CmShipBarometer()
   //                           6  = CmShipBarograph()
   //                           7  = CmShipTemperatures()
   //                           8  = CmShipPresentWeather()
   //                           9  = CmShipPastWeather()
   //                           10 = CmShipVisibility()
   //                           11 = CmShipCloudslow()
   //                           12 = CmShipCloudsmedium()
   //                           13 = CmShipCloudshigh()
   //                           14 = CmShipCloudsheight()
   //                           15 = CmShipObserver()
   */

   //while (/*stop_in_next_sequence == false &&*/ (seq_no_input_screen <= 15) && (seq_no_input_screen >= 1))
   //{
      //if (seq_no_input_screen == 5)
      //{
       //  Input_Barometer_menu_actionPerformed(evt);
       //  seq_no_input_screen++;
      //}

      //if ((seq_no_input_screen == 6) /*&& (barometer_form_active == false)*/)
      //{
      //   Input_Barograph_menu_actionPerformed(evt);

      //}

      //seq_no_input_screen++;
   //}


}//GEN-LAST:event_Next_form_automation_menu_actionPerformed


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void date_time_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_date_time_mainscreen_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)                          // NOT EUCOS AWS 
   //{
      Input_DateTime_menu_actionPerformed(null);
   //}   
}//GEN-LAST:event_date_time_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void position_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_position_mainscreen_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Input_Position_menu_actionPerformed(null);
   //}
   // NB zoals onderstaande kan het ook
   //   myposition form = new myposition();
   //   form.setSize(800, 600);
   //   form.setVisible(true);
}//GEN-LAST:event_position_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void course_speed_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_course_speed_mainscreen_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Input_Position_menu_actionPerformed(null);
   //}
}//GEN-LAST:event_course_speed_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void pressure_read_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pressure_read_mainscreen_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Input_Barometer_menu_actionPerformed(null);
   //}
}//GEN-LAST:event_pressure_read_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void pressure_msl_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pressure_msl_mainscreen_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Input_Barometer_menu_actionPerformed(null);
   //}
}//GEN-LAST:event_pressure_msl_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void amount_pressure_tendency_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_amount_pressure_tendency_mainscreen_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Input_Barograph_menu_actionPerformed(null);
   //}
}//GEN-LAST:event_amount_pressure_tendency_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void char_pressure_tendency_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_char_pressure_tendency_mainscreen_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Input_Barograph_menu_actionPerformed(null);
   //}
}//GEN-LAST:event_char_pressure_tendency_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void present_weather_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_present_weather_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Presentweather_menu_actionPerformed(null);
}//GEN-LAST:event_present_weather_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void past_weather_1_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_past_weather_1_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Pastweather_menu_actionperformed(null);
}//GEN-LAST:event_past_weather_1_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void past_weather_2_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_past_weather_2_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Pastweather_menu_actionperformed(null);
}//GEN-LAST:event_past_weather_2_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void true_wind_speed_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_true_wind_speed_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Wind_menu_actionPerformed(null);
}//GEN-LAST:event_true_wind_speed_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void true_wind_dir_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_true_wind_dir_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Wind_menu_actionPerformed(null);
}//GEN-LAST:event_true_wind_dir_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void visibility_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visibility_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Visibility_menu_actionPerformed(null);
}//GEN-LAST:event_visibility_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void observer_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_observer_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Observer_menu_actionPerformed(null);
}//GEN-LAST:event_observer_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void wind_wave_height_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wind_wave_height_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_waves_menu_actionPerformed(null);
}//GEN-LAST:event_wind_wave_height_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void wind_wave_period_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wind_wave_period_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_waves_menu_actionPerformed(null);
}//GEN-LAST:event_wind_wave_period_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void swell_1_dir_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_swell_1_dir_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_waves_menu_actionPerformed(null);
}//GEN-LAST:event_swell_1_dir_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void swell_1_height_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_swell_1_height_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_waves_menu_actionPerformed(null);
}//GEN-LAST:event_swell_1_height_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void swell_1_period_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_swell_1_period_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_waves_menu_actionPerformed(null);
}//GEN-LAST:event_swell_1_period_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void swell_2_dir_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_swell_2_dir_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_waves_menu_actionPerformed(null);
}//GEN-LAST:event_swell_2_dir_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void swell_2_height_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_swell_2_height_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_waves_menu_actionPerformed(null);
}//GEN-LAST:event_swell_2_height_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void swell_2_period_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_swell_2_period_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_waves_menu_actionPerformed(null);
}//GEN-LAST:event_swell_2_period_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void total_cloud_cover_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_total_cloud_cover_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Cloudcover_menu_actionPerformed(null);
}//GEN-LAST:event_total_cloud_cover_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void amount_cl_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_amount_cl_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Cloudcover_menu_actionPerformed(null);
}//GEN-LAST:event_amount_cl_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void height_lowest_cloud_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_height_lowest_cloud_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Cloudcover_menu_actionPerformed(null);
}//GEN-LAST:event_height_lowest_cloud_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void cl_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cl_mainscreen_mouseClicked
   // TODO add your handling code here:
  Input_Cloudslow_menu_actionPerformed(null);
}//GEN-LAST:event_cl_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void cm_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cm_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Cloudsmiddle_menu_actionPerformed(null);
}//GEN-LAST:event_cm_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void ch_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ch_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Cloudshigh_menu_actionPerformed(null);
}//GEN-LAST:event_ch_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void air_temp_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_air_temp_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Temperatures_menu_actionPerformed(null);
}//GEN-LAST:event_air_temp_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void wet_bulb_temp_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wet_bulb_temp_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Temperatures_menu_actionPerformed(null);
}//GEN-LAST:event_wet_bulb_temp_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void dew_point_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dew_point_mainscreen_mouseClicked
   // TODO add your handling code here:
   Input_Temperatures_menu_actionPerformed(null);
}//GEN-LAST:event_dew_point_mainscreen_mouseClicked



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void call_sign_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_call_sign_mainscreen_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Maintenance_Stationdata_actionPerformed(null);
   //}  
}//GEN-LAST:event_call_sign_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void masked_call_sign_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_masked_call_sign_mainscreen_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Maintenance_Stationdata_actionPerformed(null);
   //}
}//GEN-LAST:event_masked_call_sign_mainscreen_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void call_sign_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_call_sign_toolbar_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Maintenance_Stationdata_actionPerformed(null);
   //}
}//GEN-LAST:event_call_sign_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void date_time_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_date_time_toolbar_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)                          // NOT EUCOS AWS 
   //{
      Input_DateTime_menu_actionPerformed(null);
   //}   
}//GEN-LAST:event_date_time_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void position_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_position_toolbar_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Input_Position_menu_actionPerformed(null);
   //}
}//GEN-LAST:event_position_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void wind_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wind_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_Wind_menu_actionPerformed(null);
}//GEN-LAST:event_wind_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void waves_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_waves_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_waves_menu_actionPerformed(null);
}//GEN-LAST:event_waves_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void barometer_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_barometer_toolbar_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Input_Barometer_menu_actionPerformed(null);
   //}    
}//GEN-LAST:event_barometer_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void barograph_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_barograph_toolbar_mouseClicked
   // TODO add your handling code here:
   
   //if (RS232_connection_mode != 3)
   //{
      Input_Barograph_menu_actionPerformed(null);
   //}
}//GEN-LAST:event_barograph_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void temperatures_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_temperatures_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_Temperatures_menu_actionPerformed(null);
}//GEN-LAST:event_temperatures_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void present_weather_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_present_weather_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_Presentweather_menu_actionPerformed(null);
}//GEN-LAST:event_present_weather_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void past_weather_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_past_weather_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_Pastweather_menu_actionperformed(null);
}//GEN-LAST:event_past_weather_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void visibility_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visibility_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_Visibility_menu_actionPerformed(null);
}//GEN-LAST:event_visibility_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void cl_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cl_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_Cloudslow_menu_actionPerformed(null);

}//GEN-LAST:event_cl_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void cm_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cm_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_Cloudsmiddle_menu_actionPerformed(null);
}//GEN-LAST:event_cm_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void ch_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ch_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_Cloudshigh_menu_actionPerformed(null);
}//GEN-LAST:event_ch_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void height_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_height_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_Cloudcover_menu_actionPerformed(null);

}//GEN-LAST:event_height_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void icing_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icing_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_Icing_menu_actionPerformed(null);
}//GEN-LAST:event_icing_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void ice_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ice_toolbar_mouseClicked
   // TODO add your handling code here:
   Input_Ice_menu_actionPerformed(null);
}//GEN-LAST:event_ice_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void observer_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_observer_toolbar_mouseClicked
   // TODO add your handling code here:

  Input_Observer_menu_actionPerformed(null);
}//GEN-LAST:event_observer_toolbar_mouseClicked


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Themes_1_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Themes_1_actionPerformed
   // TODO add your handling code here:
   
   
   //
   //////// Day colors (Nimbus (vanaf Java 1.6.10) based)
   //
   
   try
   {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
      {
         if ("Nimbus".equals(info.getName()))
         {
            UIManager.setLookAndFeel(info.getClassName());
            
            // Nimbus default color values: https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/_nimbusDefaults.html
            
            UIManager.put("control", new Color(214,217,223));                          // Nimbus default
            UIManager.put("nimbusBase", new Color(51,98,140));                         // Nimbus default
            UIManager.put("nimbusFocus", new Color(115,164,209));                      // Nimbus default
            UIManager.put("nimbusLightBackground", new Color(255,255,255));            // Nimbus default
            //UIManager.put("nimbusSelectionBackground", new Color(57,105,138));       // Nimbus default, selected/highlighted text eg to copy, NOT IMPORTANT
            UIManager.put("text", new Color(0,0,0));                                   // Nimbus default
            UIManager.put("nimbusBlueGrey", new Color(169,176,190));                   // Nimbus default

            SwingUtilities.updateComponentTreeUI(main.this);                           // moet komen na setLookAndFeel !!!
            
            jTextField4.setBackground(new java.awt.Color(204, 255, 255));              // status bar (for system messages)
            
            break;
         }
      }
      theme_mode = THEME_NIMBUS_DAY;
   }
   catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
   {
      String info = "Nimbus related Themes not supported on this computer";
      JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " message", JOptionPane.WARNING_MESSAGE);
      main.log_turbowin_system_message("[GENERAL] " + info);
   }   

}//GEN-LAST:event_Themes_1_actionPerformed


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Themes_2_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Themes_2_actionPerformed
   // TODO add your handling code here:

   // Night colors (Nimbus based)
   //
   
   try
   {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
      {
         if ("Nimbus".equals(info.getName()))
         {
            UIManager.setLookAndFeel(info.getClassName());
            
            UIManager.put("control", new Color(114,114,114)); 
            UIManager.put("nimbusBase", new Color(64,64,64)); 
            UIManager.put("nimbusFocus", new Color(191,191,191)); 
            UIManager.put("nimbusLightBackground", new Color(176,176,176)); 
            //UIManager.put("nimbusSelectionBackground", new Color(90,130,195)); 
            UIManager.put("text", new Color(0,0,0));   
            UIManager.put("nimbusBlueGrey", new Color(169,176,190));

            SwingUtilities.updateComponentTreeUI(main.this);                                   // moet komen na setLookAndFeel !!!
            
            jTextField4.setBackground(new java.awt.Color(192, 192, 192));                      // status bar (for system messages)
            
            break;
         }
      }
      theme_mode = THEME_NIMBUS_NIGHT;
   }
   catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
   {
      String info = "Nimbus related Themes not supported on this computer";
      JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " message", JOptionPane.WARNING_MESSAGE);
      main.log_turbowin_system_message("[GENERAL] " + info);
   }
   
   
}//GEN-LAST:event_Themes_2_actionPerformed




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Themes_3_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Themes_3_actionPerformed
   // TODO add your handling code here:

   //
   //////// Sunrise, Nimbus (vanaf Java 1.6.10) based
   //
   // NB dit is eigenlijk geen Theme maar compleet ander color scheme
   try
   {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
      {
         if ("Nimbus".equals(info.getName()))
         {
            UIManager.setLookAndFeel(info.getClassName());
            
            // eg for the color values see: http://www.rapidtables.com/web/color/RGB_Color.htm
             
            //UIManager.put("control", new Color(214,217,223));                          // panels, frame
            UIManager.put("control", new Color(255,178,102)); 
            
            UIManager.put("nimbusBase", new Color(51,98,140));                           // menu background unfolded items, radio buttons, yes/no buttons
            UIManager.put("nimbusFocus", new Color(115,164,209));                        // border color selected control (text field, button, radio button etc.)
            
            //UIManager.put("nimbusLightBackground", new Color(255,255,255));            // Nimbus default    // controls wit
            UIManager.put("nimbusLightBackground", new Color(255,204,153));              //  controls 
            
            //UIManager.put("nimbusSelectionBackground", new Color(57,105,138));         // Nimbus default    // selected text  ONBELANGRIJK
            UIManager.put("text", new Color(0,0,0));                                     // black texten
            
            UIManager.put("nimbusBlueGrey", new Color(169,176,190));                     // menu bar, text field, button, radio button etc.

            SwingUtilities.updateComponentTreeUI(main.this);                             // moet komen na setLookAndFeel !!!
            
            jTextField4.setBackground(new java.awt.Color(255,255,132));                  // status bar (for system messages)
            //jTextField4.setBackground(new java.awt.Color(255,153,153));
            
            break;
         }
      }
      theme_mode = THEME_NIMBUS_SUNRISE;
   }
   catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
   {
      String info = "Nimbus related Themes not supported on this computer";
      JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " message", JOptionPane.WARNING_MESSAGE);
      main.log_turbowin_system_message("[GENERAL] " + info);
   }


    
   /*
   // http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/nimbus.html
   //
   // Version Note: Do not set the Nimbus look and feel explicitly by invoking the UIManager.setLookAndFeel
   // method because not all versions or implementations of Java SE 6 support Nimbus. Additionally,
   // the location of the Nimbus package changed between the 6u10 and JDK7 releases. Iterating through
   // all installed look and feel implementations is a more robust approach because if Nimbus is not available,
   // the default look and feel is used. For the Java SE 6 Update 10 release, the Nimbus package is
   // located at com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel.
   */

}//GEN-LAST:event_Themes_3_actionPerformed




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Input_Icing_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Icing_menu_actionPerformed
   // TODO add your handling code here:
   //if (icing_form == null)
   //{   
   //   icing_form = new myicing();
   //   icing_form.setSize(800, 600);
   //}
   //icing_form.setVisible(true);
   
   myicing form = new myicing();               
   form.setSize(800, 600);
   form.setVisible(true); 
}//GEN-LAST:event_Input_Icing_menu_actionPerformed



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void icing_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_icing_mainscreen_mouseClicked
   // TODO add your handling code here:

   //myicing form = new myicing();
   //form.setSize(800, 600);
   //form.setVisible(true);
   
   Input_Icing_menu_actionPerformed(null);
}//GEN-LAST:event_icing_mainscreen_mouseClicked



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Input_Ice_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Input_Ice_menu_actionPerformed
   // TODO add your handling code here:
   //if (ice_form == null)
   //{   
   //   ice_form = new myice1();
   //   ice_form.setSize(800, 600);
   //}
   //ice_form.setVisible(true);
   
   myice1 form = new myice1();               
   form.setSize(800, 600);
   form.setVisible(true); 
}//GEN-LAST:event_Input_Ice_menu_actionPerformed



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void ice_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ice_mainscreen_mouseClicked
   // TODO add your handling code here:

   //myice1 form = new myice1();
   //form.setSize(800, 600);
   //form.setVisible(true);
   
   Input_Ice_menu_actionPerformed(null);
}//GEN-LAST:event_ice_mainscreen_mouseClicked




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void captain_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_captain_toolbar_mouseClicked
   // TODO add your handling code here:
   
   Maintenance_Captains_Menu_actionPerformed(null);
}//GEN-LAST:event_captain_toolbar_mouseClicked



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void next_screen_toolbar_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_next_screen_toolbar_mouseClicked
   // TODO add your handling code here:

   /* initialisation */
   in_next_sequence = true;

   /* starting with the position data input screen */
   Input_Position_menu_actionPerformed(null);
 
}//GEN-LAST:event_next_screen_toolbar_mouseClicked





/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Info_Statistics_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Info_Statistics_menu_actionPerformed
   // TODO add your handling code here:
   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         Desktop desktop = null;

         // Before more Desktop API is used, first check
         // whether the API is supported by this particular
         // virtual machine (VM) on this particular host.
         if (Desktop.isDesktopSupported())
         {
            desktop = Desktop.getDesktop();
            URI uri = null;
            String http_adres = "";
            try
            {
               http_adres = "http://www.meteo2.shom.fr/cgi-bin/meteo/display_vos_ext.cgi?callchx=";
               http_adres += call_sign;
               uri = new URI(http_adres);
               desktop.browse(uri);
            }
            catch(IOException | URISyntaxException ioe) { }
         } // if (Desktop.isDesktopSupported())
         else
         {
            JOptionPane.showMessageDialog(null, "Error invoking default web browser (-Desktop-method not supported on this computer system)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         } // else

         return null;

      } // protected Void doInBackground() throws Exception
   }.execute(); // new SwingWorker<Void, Void>()


}//GEN-LAST:event_Info_Statistics_menu_actionPerformed






/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Amver_SailingPlan_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Amver_SailingPlan_actionPerformed
   // TODO add your handling code here:

   if (amver_report.compareTo("") != 0)
   {
      JOptionPane.showMessageDialog(null, "Please close first a previously opened AMVER form", main.APPLICATION_NAME + " message", JOptionPane.WARNING_MESSAGE);
   }
   else
   {
      amver_report = AMVER_SP;              // AMVER sailing plan

      myamversailingplan form = new myamversailingplan();
      form.setSize(1000, 700);
      form.setVisible(true);
   }
}//GEN-LAST:event_Amver_SailingPlan_actionPerformed


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Amver_DeviationReport_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Amver_DeviationReport_actionPerformed
   // TODO add your handling code here:

   if (amver_report.compareTo("") != 0)
   {
      JOptionPane.showMessageDialog(null, "Please close first a previously opened AMVER form", main.APPLICATION_NAME + " message", JOptionPane.WARNING_MESSAGE);
   }
   else
   {
      amver_report = AMVER_DR;             // AMVER deviation report

      myamversailingplan form = new myamversailingplan();
      form.setSize(1000, 700);
      form.setVisible(true);
   }
}//GEN-LAST:event_Amver_DeviationReport_actionPerformed


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Amver_ArrivalReport_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Amver_ArrivalReport_actionPerformed
   // TODO add your handling code here:
   if (amver_report.compareTo("") != 0)
   {
      JOptionPane.showMessageDialog(null, "Please close first a previously opened AMVER form", main.APPLICATION_NAME + " message", JOptionPane.WARNING_MESSAGE);
   }
   else
   {
      amver_report = AMVER_FR;             // AMVER arrival(final) report

      myamversailingplan form = new myamversailingplan();
      form.setSize(1000, 700);
      form.setVisible(true);
   }
}//GEN-LAST:event_Amver_ArrivalReport_actionPerformed


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Amver_PositionReport_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Amver_PositionReport_actionPerformed
   // TODO add your handling code here:
   if (amver_report.compareTo("") != 0)
   {
      JOptionPane.showMessageDialog(null, "Please close first a previously opened AMVER form", main.APPLICATION_NAME + " message", JOptionPane.WARNING_MESSAGE);
   }
   else
   {
      amver_report = AMVER_PR;             // AMVER position report

      myamversailingplan form = new myamversailingplan();
      form.setSize(1000, 700);
      form.setVisible(true);
   }
}//GEN-LAST:event_Amver_PositionReport_actionPerformed



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
    private void Graphs_Pressure_Sensor_Data_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Graphs_Pressure_Sensor_Data_actionPerformed
        // TODO add your handling code here:
       
       if (graph_form != null)
       {
          if (sensor_data_file_ophalen_timer_is_gecreeerd == true)  // 15-05-2013
          {
             if (RS232_view.sensor_data_file_ophalen_timer.isRunning())
             {
                RS232_view.sensor_data_file_ophalen_timer.stop();
             }
          }
          RS232_view.sensor_data_file_ophalen_timer = null;
      
          sensor_data_file_ophalen_timer_is_gecreeerd = false;
          
          //graph_form.dispose();
          graph_form.setVisible(false);
       }
       
       mode_grafiek = MODE_PRESSURE;
       
       graph_form = new RS232_view();
       //graph_form.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());       // full screen
       graph_form.setExtendedState(MAXIMIZED_BOTH); 
       graph_form.setVisible(true);  
             
    }//GEN-LAST:event_Graphs_Pressure_Sensor_Data_actionPerformed

    
    
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
   private void Maintenance_Serial_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Maintenance_Serial_actionPerformed
      // TODO add your handling code here:
      
      mode = SERIAL_CONNECTION;

      mypassword form = new mypassword();
      form.setSize(400, 300);
      form.setVisible(true);
   }//GEN-LAST:event_Maintenance_Serial_actionPerformed

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
   private void main_windowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_main_windowClosing
      // TODO add your handling code here:
     
      
      String info = "Are you sure you want to exit this application?";
      //if ( ((RS232_connection_mode == 1) || (RS232_connection_mode == 2) || (RS232_connection_mode == 3) || (RS232_connection_mode == 4))  && (defaultPort != null) )
      if ( ((RS232_connection_mode != 0) && (defaultPort != null)) || (RS232_connection_mode == 6) )   
      {
         // PTB220, PTB330, EUCAWS, MintakaDuo, Mintaka Star USB (all via serial comm) or Mintaka Star WiFi connected
         info += "\n\n (" + APPLICATION_NAME + " will stop with monitoring and collecting of the sensor data)";
         if (main.APR == true || main.WOW == true)
         {
            info += "\n (" + APPLICATION_NAME + " will stop with automated reports upload)";
            info += "\n (minimise " + APPLICATION_NAME + " instead of closing)";
         }
      }   
      int result = JOptionPane.showConfirmDialog(main.this, info, "Exit " + APPLICATION_NAME, JOptionPane.YES_NO_OPTION);

      if (result == JOptionPane.YES_OPTION)
      {
         // Remember to remove the listener (for checking only once instance running) before your application exits (see Function initComponents2()) [nb only in jnlp mode]
         if (sisL != null)
         {
            sis.removeSingleInstanceListener(sisL);
         }
         
         // serial communication barometer (not neccessary for WiFi barometer)
         //if ( ((RS232_connection_mode == 1) || (RS232_connection_mode == 2) || (RS232_connection_mode == 3) || (RS232_connection_mode == 4)) && (defaultPort != null) )
         if ((RS232_connection_mode != 0) && (defaultPort != null))  
         {
            //
            // NB RxTx Serial ?
            // close() for Linux for a proper clean up of a port stale lock file (e.g. /var/lock/LCK...ttyUSB0), not appropiate to Windows
            // unfortunately it didn't help (see also: http://www.raspberrypi.org/forums/viewtopic.php?f=81&t=32186)
            //
            // The problem with file lock is likely due to the lock-file being created with root permissions (sudo), 
            // however the IDE is being ran under user mode. Either you will have to make the lock file create in user-mode,
            // change the ownership of the lock, or run the IDE in root. Running it in root shouldn't be a big issue. 
            //
            //
            // NB in case of WiFi: defaultport == null
            //
            //
            if (main.serialPort != null)
            {
               try 
               {
                  main.serialPort.removeEventListener();
                  main.serialPort.closePort();
                  main.serialPort = null;
               } 
               catch (SerialPortException ex) 
               {
                  System.out.println(ex);
               }
            } // if (main.serialPort != null)
            
         } // if ((RS232_connection_mode != 0) && (defaultPort != null))
         
         // serial communication GPS
         if ((RS232_GPS_connection_mode != 0) && (main_RS232_RS422.GPS_defaultPort != null))
         {
            // only necessary in case of RxTx serial?
            if (main_RS232_RS422.GPS_serialPort != null)
            {
               try 
               {
                  main_RS232_RS422.GPS_serialPort.removeEventListener();
                  main_RS232_RS422.GPS_serialPort.closePort();
                  main_RS232_RS422.GPS_serialPort = null;
               } 
               catch (SerialPortException ex) 
               {
                  System.out.println(ex);
               }
            } // if (main_RS232_RS422.GPS_serialPort != null)
         } // if ((RS232_GPS_connection_mode != 0) && (main_RS232_RS422.GPS_defaultPort != null))
         
         // if the program was minimized remove the trayIcon
         if ((main.ICONIFIED & this.getExtendedState()) == main.ICONIFIED)
         {
            tray.remove(trayIcon);
         }
         
         // TurboWin+ stopped message to log
         log_turbowin_system_message("[GENERAL] stopped " + APPLICATION_NAME + " " + application_mode + " " + APPLICATION_VERSION);
         
         // exit
         System.exit(0);     
      }      
   }//GEN-LAST:event_main_windowClosing

   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
   private void Graphs_Airtemp_Sensor_Data_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Graphs_Airtemp_Sensor_Data_actionPerformed
      // TODO add your handling code here:
      if (graph_form != null)
      {
         if (sensor_data_file_ophalen_timer_is_gecreeerd == true)  // 15-05-2013
         {
            if (RS232_view.sensor_data_file_ophalen_timer.isRunning())
            {
               RS232_view.sensor_data_file_ophalen_timer.stop();
            }
         }
         RS232_view.sensor_data_file_ophalen_timer = null;
      
         sensor_data_file_ophalen_timer_is_gecreeerd = false;
          
         //graph_form.dispose();
         graph_form.setVisible(false);
      }  
      
      mode_grafiek = MODE_AIRTEMP;
       
      graph_form = new RS232_view();
      //graph_form.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());       // full screen
      graph_form.setExtendedState(MAXIMIZED_BOTH); 
      graph_form.setVisible(true);     
     
      
   }//GEN-LAST:event_Graphs_Airtemp_Sensor_Data_actionPerformed

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
   private void Graphs_SST_Sensor_data_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Graphs_SST_Sensor_data_actionPerformed
      // TODO add your handling code here:
      if (graph_form != null)
      {
         if (sensor_data_file_ophalen_timer_is_gecreeerd == true)  // 15-05-2013
         {
            if (RS232_view.sensor_data_file_ophalen_timer.isRunning())
            {
               RS232_view.sensor_data_file_ophalen_timer.stop();
            }
         }
         RS232_view.sensor_data_file_ophalen_timer = null;
      
         sensor_data_file_ophalen_timer_is_gecreeerd = false;
          
         //graph_form.dispose();
         graph_form.setVisible(false);
      }        
      
      mode_grafiek = MODE_SST;
       
      graph_form = new RS232_view();
      //graph_form.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());       // full screen
      graph_form.setExtendedState(MAXIMIZED_BOTH); 
      graph_form.setVisible(true);     
      
   }//GEN-LAST:event_Graphs_SST_Sensor_data_actionPerformed

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
   private void Graphs_Wind_Speed_Sensor_Data_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Graphs_Wind_Speed_Sensor_Data_actionPerformed
      // TODO add your handling code here:
      if (graph_form != null)
      {
         if (sensor_data_file_ophalen_timer_is_gecreeerd == true)  // 15-05-2013
         {
            if (RS232_view.sensor_data_file_ophalen_timer.isRunning())
            {
               RS232_view.sensor_data_file_ophalen_timer.stop();
            }
         }
         RS232_view.sensor_data_file_ophalen_timer = null;
      
         sensor_data_file_ophalen_timer_is_gecreeerd = false;
          
         //graph_form.dispose();
         graph_form.setVisible(false);
      }        
      mode_grafiek = MODE_WIND_SPEED;
       
      graph_form = new RS232_view();
      //graph_form.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());       // full screen
      graph_form.setExtendedState(MAXIMIZED_BOTH); 
      graph_form.setVisible(true);     

   }//GEN-LAST:event_Graphs_Wind_Speed_Sensor_Data_actionPerformed

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
/*
   private void Output_obs_to_AWS_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Output_obs_to_AWS_actionPerformed
      // TODO add your handling code here:
      
      OutputStream outputStream_obs_to_aws = null;
      
/////////// TEST START ///////////
//      
//      String test_obs_for_AWS_string = compile_obs_for_AWS();
//      System.out.println("Writing " + test_obs_for_AWS_string + " to " + defaultPort);      
//      
/////////// TEST END /////////////      
      
      
      
      if (defaultPort != null)
      {
         try
         {
		      if (serialPort != null)
            {
               outputStream_obs_to_aws = serialPort.getOutputStream();
            }
               
            if (outputStream_obs_to_aws != null)
            {
               // temporary message box "Obs to AWS" 
               // NB de Thread.sleep(2000); is waarschijnlijk niet nodig bij versuren obs naar AWS, dan kan ook deze eerste temporary message box vervallen 
//
//               final JOptionPane pane_start = new JOptionPane("obs to AWS", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
//               final JDialog start_dialog = pane_start.createDialog(APPLICATION_NAME);
//
//               Timer timer_start = new Timer(1500, new ActionListener()
//               {
//                  @Override
//                  public void actionPerformed(ActionEvent e)
//                  {
//                     start_dialog.dispose();
//                  }
//               });
//               timer_start.setRepeats(false);
//               timer_start.start();
//               start_dialog.setVisible(true);
// 
               

               
               String obs_for_AWS_string = compile_obs_for_AWS();
               
               System.out.println("Writing " + obs_for_AWS_string + " to " + defaultPort);
               
               obs_for_AWS_string += "\r\n";                  // <CR><LF>  required for EUCAWS          
               
               outputStream_obs_to_aws.write(obs_for_AWS_string.getBytes());
               outputStream_obs_to_aws.flush();
        

               
               
              
//////////// TEST START /////////////////
//              
//               String messageString_stop = "S\r";
//               outputStream_obs_to_aws.write(messageString_stop.getBytes());
//               outputStream_obs_to_aws.flush();
//               
//               try 
//               {
//                  Thread.sleep(2000);
//               } 
//               catch (InterruptedException ex) { }
//               
//               String messageString_info = "VERS\r";
//               outputStream_obs_to_aws.write(messageString_info.getBytes());
//               outputStream_obs_to_aws.flush();
//               
 //////////////////// TEST STOP ////////////     
               
               // temporary message box "obs sent to AWS" 
               final JOptionPane pane_end = new JOptionPane("obs sent to AWS", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
               final JDialog end_dialog = pane_end.createDialog(APPLICATION_NAME);

               Timer timer_end = new Timer(1500, new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent e)
                  {
                     end_dialog.dispose();
                  }
               });
               timer_end.setRepeats(false);
               timer_end.start();
               end_dialog.setVisible(true);
               
            } // if (outputStream_obs_to_aws != null)
         
		   }
         catch (IOException ex)
         {
            System.out.print("+++ IOException:");
			   System.out.println(ex.toString());
            
            JOptionPane.showMessageDialog(null, "Failed to send obs to AWS because no serial connection available (IOException)"  , APPLICATION_NAME + " error", JOptionPane.ERROR_MESSAGE);
         }    
      } // if (defaultPort != null)
      else
      {
         JOptionPane.showMessageDialog(null, "Failed to send obs to AWS because no serial connection available (defaultPort = null)"  , APPLICATION_NAME + " error", JOptionPane.ERROR_MESSAGE);
      }
      
      //reset alll meteo parameters 
      main_RS232_RS422.RS422_initialise_AWS_Sensor_Data_For_Display(); // must be called before: Reset_all_meteo_parameters();
      Reset_all_meteo_parameters();
      
      
   }//GEN-LAST:event_Output_obs_to_AWS_actionPerformed
*/
   

   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
   private void Output_obs_to_AWS_actionPerformed(java.awt.event.ActionEvent evt) {                                                   
      // TODO add your handling code here:
      
      
/////////// TEST START ///////////
//      
//      String test_obs_for_AWS_string = compile_obs_for_AWS();
//      System.out.println("Writing " + test_obs_for_AWS_string + " to " + defaultPort);      
//      
/////////// TEST END /////////////      
      
      
      if (defaultPort != null)
      {
         String obs_for_AWS_string = compile_obs_for_AWS();
         System.out.println("Writing " + obs_for_AWS_string + " to " + defaultPort);
         obs_for_AWS_string += "\r\n";                  // <CR><LF>  required for EUCAWS          
         try 
         {
            serialPort.writeBytes(obs_for_AWS_string.getBytes());              // Write data to port
            
            // temporary message box "obs sent to AWS" 
            final JOptionPane pane_end = new JOptionPane("obs sent to AWS", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            final JDialog end_dialog = pane_end.createDialog(APPLICATION_NAME);

            Timer timer_end = new Timer(1500, new ActionListener()
            {
               @Override
               public void actionPerformed(ActionEvent e)
               {
                  end_dialog.dispose();
               }
            });
            timer_end.setRepeats(false);
            timer_end.start();
            end_dialog.setVisible(true);
         } // try
         catch (SerialPortException ex) 
         {
            System.out.println(ex);
         }
               
              
//////////// TEST START /////////////////
//              
//               String messageString_stop = "S\r";
//               outputStream_obs_to_aws.write(messageString_stop.getBytes());
//               outputStream_obs_to_aws.flush();
//               
//               try 
//               {
//                  Thread.sleep(2000);
//               } 
//               catch (InterruptedException ex) { }
//               
//               String messageString_info = "VERS\r";
//               outputStream_obs_to_aws.write(messageString_info.getBytes());
//               outputStream_obs_to_aws.flush();
//               
 //////////////////// TEST STOP ////////////     
               
                
         
      } // if (defaultPort != null)
      else
      {
         JOptionPane.showMessageDialog(null, "Failed to send obs to AWS because no serial connection available (defaultPort = null)"  , APPLICATION_NAME + " error", JOptionPane.ERROR_MESSAGE);
      }
      
      /* reset alll meteo parameters */
      main_RS232_RS422.RS422_initialise_AWS_Sensor_Data_For_Display(); // must be called before: Reset_all_meteo_parameters();
      Reset_all_meteo_parameters();
      
   }                                                  


   
   
   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
   private void Graph_Wind_Dir_Sensor_Data_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Graph_Wind_Dir_Sensor_Data_actionPerformed
      // TODO add your handling code here:
      if (graph_form != null)
      {
         if (sensor_data_file_ophalen_timer_is_gecreeerd == true)  
         {
            if (RS232_view.sensor_data_file_ophalen_timer.isRunning())
            {
               RS232_view.sensor_data_file_ophalen_timer.stop();
            }
         }
         RS232_view.sensor_data_file_ophalen_timer = null;
      
         sensor_data_file_ophalen_timer_is_gecreeerd = false;
          
         //graph_form.dispose();
         graph_form.setVisible(false);
      }        
      mode_grafiek = MODE_WIND_DIR;
       
      graph_form = new RS232_view();
      //graph_form.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());       // full screen
      graph_form.setExtendedState(MAXIMIZED_BOTH); 
      graph_form.setVisible(true);           
   }//GEN-LAST:event_Graph_Wind_Dir_Sensor_Data_actionPerformed


   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
   private void Output_obs_to_clipboard_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Output_obs_to_clipboard_actionPerformed
      // TODO add your handling code here:
      
      new SwingWorker<Boolean, Void>() 
      {
         @Override
         protected Boolean doInBackground() throws Exception
         {
            boolean doorgaan = false;

            // compose coded obs
            String SPATIE = SPATIE_OBS_VIEW;                                     // marker between obs groups
            obs_write = compose_coded_obs(SPATIE);


            // check call sign and date time entered (level 1b checks)
            if (obs_write.compareTo(UNDEFINED) != 0)
            {
               doorgaan = true;
            }
            else
            {
               doorgaan = false;
               String info = "Call sign, date/time or position not inserted";
               JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            } // else
            
            
            // level-2 checks
            //
            //
            if (doorgaan == true)
            {
               doorgaan = checking_level_2() == true;
            }
            
            
            // level-3 checks
            //
            //
            if (doorgaan == true)
            {
               doorgaan = checking_level_3() == true;
            }
            
            
            // check log dir known
            if (doorgaan == true)
            {
               if (logs_dir.trim().equals("") == true || logs_dir.trim().length() < 2)
               {
                  doorgaan = false;
                  String info = "logs folder unknown, select: Maintenance -> Log files settings and retry";
                  JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               }
            } // if (doorgaan == true)


            // position + time sequence checks
            if (doorgaan == true)
            {
               bepaal_last_record_uit_immt();
               doorgaan = position_sequence_check();

               if (doorgaan)
               {
                  doorgaan = Check_Land_Sea_Mask();
               }
            } // if (doorgaan == true)
         
         
            // if appropriate make a format 101 obs
            if (doorgaan == true)
            {
               if (obs_format.equals(FORMAT_101))
               {
                  // NB although "obs_write = compose_coded_obs(SPATIE);" (makes FM13 record)  see above is not necessary for the compressed obs 
                  //    it is useful because parts of it will be used for checking position etc.
               
                  format_101_class = new FORMAT_101();
                  format_101_class.compress_and_decompress_101_control_center();
               } // if (obs_format.equals(FORMAT_101))
            } // if (doorgaan == true)


            return doorgaan;

         } // protected Void doInBackground() throws Exception

         @Override
         protected void done()
         {
            try
            {
               boolean doorgaan = get();
               //if (doorgaan == true)
               //{
               //   Output_obs_to_clipboard();
               //}
               if (doorgaan == true)
               {
                  if (obs_format.equals(FORMAT_FM13))
                  {
                     Output_obs_to_clipboard_FM13();
                  }
                  else if (obs_format.equals(FORMAT_101))
                  {
                     Output_obs_to_clipboard_format_101();
                  }
                  else
                  {
                     String info = "obs format unknown (select: Maintenance -> Obs format setting)";
                     JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " warning", JOptionPane.WARNING_MESSAGE);
                     System.out.println("+++ Not supported obs format in Function: Output_obs_to_clipboard_actionPerformed()");
                  }  // else 
               } // if (doorgaan == true)
            } // try
            catch (InterruptedException | ExecutionException ex) 
            { 
               System.out.println("+++ Error in Function: Output_obs_to_clipboard_actionPerformed(). " + ex);
            }

         } // protected void done()
      }.execute(); // new SwingWorker<Void, Void>()
  

   }//GEN-LAST:event_Output_obs_to_clipboard_actionPerformed

   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
private boolean checking_level_2()
{
   boolean doorgaan                               = true;
   boolean level_2_ok                             = true;
   boolean wind_waves_period_conversion_ok        = true;
   boolean wind_waves_height_conversion_ok        = true;
   boolean pressure_amount_tendency_conversion_ok = true;
   boolean cl_code_conversion_ok                  = true;
   boolean cm_code_conversion_ok                  = true;
   boolean ch_code_conversion_ok                  = true;
   boolean ww_code_conversion_ok                  = true;
   boolean VV_code_conversion_ok                  = true;
   boolean air_temp_conversion_ok                 = true;
   float float_wind_waves_period                  = main.INVALID;
   float float_wind_waves_height                  = main.INVALID; 
   float float_pressure_amount_tendency           = main.INVALID;
   float float_air_temp                           = main.INVALID;
   int int_cl_code                                = main.INVALID;
   int int_cm_code                                = main.INVALID;
   int int_ch_code                                = main.INVALID;
   int int_ww_code                                = main.INVALID;
   int int_VV_code                                = main.INVALID;
   Integer sky_not_discernible_array[]            = {43, 45, 47, 49};
   Integer drizzle_rain_array[]                   = {50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69};
   Integer present_weather_36_39_array[]          = {36, 37, 38, 39};
   Integer present_weather_48_49_array[]          = {48, 49};
   Integer present_weather_56_57_array[]          = {56, 57};
   Integer present_weather_66_67_array[]          = {66, 67};
   Integer present_weather_68_69_array[]          = {68, 69};
   Integer present_weather_70_75_array[]          = {70, 71, 72, 73, 74, 75};
   Integer present_weather_76_79_array[]          = {76, 77, 78, 79};
   Integer present_weather_83_86_array[]          = {83, 84, 85, 86};
   Integer cl_1_9_array[]                         = {1, 2, 3, 4, 5, 6, 7, 8, 9};
   Integer ch_1_9_array[]                         = {1, 2, 3, 4, 5, 6, 7, 8, 9};
   Integer fog_array[]                            = {42, 43, 44, 45, 46, 47, 48, 49};
   Integer visibility_95_99_array[]               = {95, 96, 97, 98, 99};
   Integer visibility_90_93_array[]               = {90, 91, 92, 93};
   
   
   //
   ///////////////////////////// conversions //////////////////////////
   //
   
   // wind_waves_period conversion
   try
   {
      if (mywaves.wind_waves_period.equals("") == false && mywaves.wind_waves_period != null)
      {
         float_wind_waves_period = Float.parseFloat(mywaves.wind_waves_period);
         wind_waves_period_conversion_ok = true;
      }
      else
      {
         wind_waves_period_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] wind waves period conversion error; Function: checking_level_2()");
      wind_waves_period_conversion_ok = false;
   }   
   
   // wind_waves_height conversion
   try
   {
      if (mywaves.wind_waves_height.equals("") == false && mywaves.wind_waves_height != null)
      {
        float_wind_waves_height = Float.parseFloat(mywaves.wind_waves_height);
        wind_waves_height_conversion_ok = true;
      }
      else
      {
         wind_waves_height_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] wind waves height conversion error; Function: checking_level_2()");
      wind_waves_height_conversion_ok = false;
   }   
   
   // pressure amount tendency conversion
   try
   {
      if (mybarograph.pressure_amount_tendency.equals("") == false && mybarograph.pressure_amount_tendency != null)
      {
         float_pressure_amount_tendency = Float.parseFloat(mybarograph.pressure_amount_tendency);
         pressure_amount_tendency_conversion_ok = true;
      }
      else
      {
         pressure_amount_tendency_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] pressure amount tendency conversion error; Function: checking_level_2()");
      pressure_amount_tendency_conversion_ok = false;
   }
   
    // string Cl code conversion to int
   try
   {
      if (mycl.cl_code.equals("") == false && mycl.cl_code != null)
      {
         int_cl_code = Integer.parseInt(mycl.cl_code);
         cl_code_conversion_ok = true;
      }
      else
      {
         cl_code_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] Cl conversion error; Function: checking_level_2()");
      cl_code_conversion_ok = false;
   }   
  
   // string Cm code conversion to int
   try
   {
      if (mycm.cm_code.equals("") == false && mycm.cm_code != null)
      {
         int_cm_code = Integer.parseInt(mycm.cm_code.substring(0, 1));   // to eliminate the a,b or c in 7a, 7b, 7c Cm code (see mycm.java)
         cm_code_conversion_ok = true;
      }
      else
      {
         cm_code_conversion_ok = false; 
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] Cm conversion error; Function: checking_level_2()");
      cm_code_conversion_ok = false;
   }   
   
   // string Ch code conversion to int
   try
   {
      if (mych.ch_code.equals("") == false && mych.ch_code != null)
      {
         int_ch_code = Integer.parseInt(mych.ch_code);
         ch_code_conversion_ok = true;
      }
      else
      {
         ch_code_conversion_ok = false;
      }   
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] Ch conversion error; Function: checking_level_2()");
      ch_code_conversion_ok = false;
   }   
   
   // string ww code conversion to int
   try
   {
      if (mypresentweather.ww_code.equals("") == false && mypresentweather.ww_code != null)
      {
         int_ww_code = Integer.parseInt(mypresentweather.ww_code);
         ww_code_conversion_ok = true;
      }
      else
      {
         ww_code_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] ww conversion error; Function: checking_level_2()");
      ww_code_conversion_ok = false;
   }      
   
   // string VV code conversion to int
   try
   {
      if (myvisibility.VV_code.equals("") == false && myvisibility.VV_code != null)
      {
         int_VV_code = Integer.parseInt(myvisibility.VV_code);
         VV_code_conversion_ok = true;
      }
      else
      {
         VV_code_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] VV conversion error; Function: checking_level_2()");
      VV_code_conversion_ok = false;
   }         
   
   // string air temp conversion to float
   try
   {
      if (mytemp.air_temp.equals("") == false && mytemp.air_temp != null)
      {
         float_air_temp = Float.parseFloat(mytemp.air_temp);
         air_temp_conversion_ok = true;
      }
      else
      {
         air_temp_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] air temp conversion error; Function: checking_level_2()");
      air_temp_conversion_ok = false;
   }   
   
   
   
   //
   ///////////////////////////// checks //////////////////////////
   //
   
   //
   ////////// wind - waves checks /////
   //
   
   // wind speed <-> sea period
   if ((doorgaan == true) && wind_waves_period_conversion_ok == true)
   {
      if ( (mywind.int_true_wind_speed == 0) && (float_wind_waves_period > 0.01 && float_wind_waves_period < 99.9)            )
      {
         JOptionPane.showMessageDialog(null, "if (true) wind speed is 0, wind waves period must be 0 or blank", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         level_2_ok = false;
         doorgaan = false;
      }
   }
   
   // wind speed <-> sea height
   if ( (doorgaan == true) && (wind_waves_height_conversion_ok == true) ) 
   {
      if ( (mywind.int_true_wind_speed == 0) && (float_wind_waves_height > 0.01 && float_wind_waves_height < 99.9) )
      {
         JOptionPane.showMessageDialog(null, "if (true) wind speed is 0, wind waves height must be 0 or blank", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         level_2_ok = false;
         doorgaan = false;
      }
   }
   
   // wind speed <-> sea height
   if ( (doorgaan == true) && (wind_waves_height_conversion_ok == true) ) 
   {
      if ( (main.wind_units.trim().indexOf(main.M_S) != -1) && (mywind.int_true_wind_speed >= 0 && mywind.int_true_wind_speed <= 3) && (float_wind_waves_height > 9.7 && float_wind_waves_height < 99.9) )
      {
         // wind speed in m/s
         JOptionPane.showMessageDialog(null, "if (true) wind speed in range 0 - 3 m/s, wind waves height must be < 9.8 m or blank", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         level_2_ok = false;
         doorgaan = false;
      }
      else if ( (main.wind_units.trim().indexOf(main.KNOTS) != -1) && (mywind.int_true_wind_speed >= 0 && mywind.int_true_wind_speed <= 6) && (float_wind_waves_height > 9.7 && float_wind_waves_height < 99.9) )
      {
         // wind speed in knots
         JOptionPane.showMessageDialog(null, "if (true) wind speed in range 0 - 6 knots, wind waves height must be < 9.8 m or blank", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         level_2_ok = false;
         doorgaan = false;
      }
   }
   
   
   //
   ////////// air pressure /////
   //
   
   // pressure characteristic <-> pressure tendency
   if ( (doorgaan == true) && (pressure_amount_tendency_conversion_ok == true) )
   {      
      if (mybarograph.a_code.equals("4") && mybarograph.pressure_amount_tendency.equals(""))
      {
         JOptionPane.showMessageDialog(null, "if air pressure characteristic is steady (a = 0), amount of pressure tendency must be 0", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         level_2_ok = false;
         doorgaan = false;
      }
      else if ( (mybarograph.a_code.equals("4") && float_pressure_amount_tendency > 0.01 && float_pressure_amount_tendency < 50.0) )
      {
         JOptionPane.showMessageDialog(null, "if air pressure characteristic is steady (a = 0), amount of pressure tendency must be 0", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         level_2_ok = false;
         doorgaan = false;
      }
   }
   
   
   //
   ////////// cloud cover <-> cloud types /////
   //
   if ((doorgaan == true) && (mycloudcover.N.equals(mycloudcover.N_CLOUDLESS)) && mycl.cl_code.equals("0") == false)
   {
      JOptionPane.showMessageDialog(null, "if total cloud cover is 'cloudless', Cl must be 'no clouds Cl'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;
   }
   
   if ((doorgaan == true) && (mycloudcover.N.equals(mycloudcover.N_CLOUDLESS)) && mycm.cm_code.equals("0") == false)
   {
      JOptionPane.showMessageDialog(null, "if total cloud cover is 'cloudless', Cm must be 'no clouds Cm'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;
   }
   
   if ((doorgaan == true) && (mycloudcover.N.equals(mycloudcover.N_CLOUDLESS)) && mych.ch_code.equals("0") == false)
   {
      JOptionPane.showMessageDialog(null, "if total cloud cover is 'cloudless', Ch must be 'no clouds Ch'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;
   }
   
   if ((doorgaan == true) && (mycl.cl_code.equals("0") && mycm.cm_code.equals("0") && mych.ch_code.equals("0") && mycloudcover.N.equals(mycloudcover.N_CLOUDLESS) == false))
   {
      JOptionPane.showMessageDialog(null, "if Cl and Cm and Ch is 'no clouds', total cloud cover must be 'cloudless'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;
   }
   
   if ((doorgaan == true) && (mycloudcover.Nh.equals(mycloudcover.NH_0_8) && mycl.cl_code.equals("0") == false))
   {
      JOptionPane.showMessageDialog(null, "if 'amount of Cl (or Cm if Cl not present)' is '0/8', Cl must be 'no clouds Cl'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }
   
   if ((doorgaan == true) && (mycloudcover.Nh.equals(mycloudcover.NH_0_8) && mycl.cl_code.equals("0") && mycm.cm_code.equals("0") == false))
   {
      JOptionPane.showMessageDialog(null, "if 'amount of Cl (or Cm if Cl not present)' is '0/8' and Cl is 'no clouds Cl', Cm must be 'no clouds Cm'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }   
   
   if ((doorgaan == true) && mycl.cl_code.equals("0") && mycm.cm_code.equals("0") && mycloudcover.Nh.equals(mycloudcover.NH_0_8) == false)
   {
      JOptionPane.showMessageDialog(null, "if Cl and Cm is 'no clouds', amount of Cl (or Cm if Cl not present)' must be '0/8'  ", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }
   
   if ((doorgaan == true) && ch_code_conversion_ok && (mycloudcover.Nh.equals(mycloudcover.NH_8_8)) && (int_ch_code >= 0 && int_ch_code <= 9))
   {
      JOptionPane.showMessageDialog(null, "if 'amount of Cl (or Cm if Cl not present)' is '8/8', Ch must be 'not determined'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }
   
   if ((doorgaan == true) && cl_code_conversion_ok && cm_code_conversion_ok && (mycloudcover.Nh.equals(mycloudcover.NH_8_8)) && (int_cl_code >= 1 && int_cl_code <= 9) && (int_cm_code >= 0 && int_cm_code <= 9))
   {
      JOptionPane.showMessageDialog(null, "if 'amount of Cl (or Cm if Cl not present)' is '8/8' and Cl was determined (in range 1 - 9 or 'no clouds Cl'), Cm must be 'not determined'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }
   
   
   //
   ////////// cloud cover <-> present weather /////
   //
   if ((doorgaan == true) && ww_code_conversion_ok && (Arrays.asList(sky_not_discernible_array).indexOf(int_ww_code) != -1) && 
       (mycloudcover.N.equals(mycloudcover.N_NOT_DETERMINED) == false && mycloudcover.N.equals(mycloudcover.N_OBSCURED) == false))
   {
      JOptionPane.showMessageDialog(null, "if 'present weather' is 'fog with sky not discernable', 'total cloud cover' must be 'obscured' or 'not determined'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }
   
   if ((doorgaan == true) && ww_code_conversion_ok && (Arrays.asList(sky_not_discernible_array).indexOf(int_ww_code) != -1) && (mycl.cl_code.equals("") == false))
   {
      JOptionPane.showMessageDialog(null, "if 'present weather' is 'fog with sky not discernable', Cl must be 'not determined'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }
   
   if ((doorgaan == true) && ww_code_conversion_ok && (Arrays.asList(sky_not_discernible_array).indexOf(int_ww_code) != -1) && (mycm.cm_code.equals("") == false))
   {
      JOptionPane.showMessageDialog(null, "if 'present weather' is 'fog with sky not discernable', Cm must be 'not determined'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }
   
   if ((doorgaan == true) && ww_code_conversion_ok && (Arrays.asList(sky_not_discernible_array).indexOf(int_ww_code) != -1) && (mych.ch_code.equals("") == false))
   {
      JOptionPane.showMessageDialog(null, "if 'present weather' is 'fog with sky not discernable', Ch must be 'not determined'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }
   
   if ((doorgaan == true) && (mycloudcover.N.equals(mycloudcover.N_CLOUDLESS)) && ww_code_conversion_ok && (Arrays.asList(drizzle_rain_array).indexOf(int_ww_code) != -1))
   {
      JOptionPane.showMessageDialog(null, "if 'present weather' is drizzle or rain, 'total cloud cover' cannot be 'cloudless'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }
   
   
   //
   ////////// cloud type <-> cloud height /////
   //
   if ((doorgaan == true) && cl_code_conversion_ok && (Arrays.asList(cl_1_9_array).indexOf(int_cl_code) != -1) && 
       (mycloudcover.h.equals(mycloudcover.H_GROTER_2500) || mycloudcover.h.equals(mycloudcover.H_CLOUDLESS)))
   {
      JOptionPane.showMessageDialog(null, "if type Cl cloud is observed, 'height of base of lowest cloud' cannot be '>=2500m (8000 ft)' and not 'cloudless'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }
   
   if ((doorgaan == true) && ch_code_conversion_ok && mycl.cl_code.equals("0") && mycm.cm_code.equals("0") && (Arrays.asList(ch_1_9_array).indexOf(int_ch_code) != -1) &&
       (mycloudcover.h.equals(mycloudcover.H_0_50) || mycloudcover.h.equals(mycloudcover.H_50_100) || mycloudcover.h.equals(mycloudcover.H_100_200)))
   {
      JOptionPane.showMessageDialog(null, "if Cl = 'no clouds Cl' and Cm = 'no clouds Cm' and Ch in range 1 - 9, 'height of base of lowest cloud in the sky' cannot be < 200 m (< 600 ft)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false; 
   }
   
   
   //
   ////////// visibilty <-> present weather /////
   //   
   if ((doorgaan == true) && ww_code_conversion_ok && (Arrays.asList(fog_array).indexOf(int_ww_code) != -1) &&
                             VV_code_conversion_ok && (Arrays.asList(visibility_95_99_array).indexOf(int_VV_code) != -1))
   {
      JOptionPane.showMessageDialog(null, "if present weather = 'fog', visibility cannot be > 0.5 nm (an exception is made for 'fog banks', 'fog in patches' and 'shallow fog')", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }   
   
   if ((doorgaan == true) && ww_code_conversion_ok && (int_ww_code == 40) &&
                             VV_code_conversion_ok && (Arrays.asList(visibility_90_93_array).indexOf(int_VV_code) != -1))
   {
      String info = "if present weather = " + "\"" + mypresentweather.ww_40  +  "\"" + ", reported visibility cannot be < 0.5 nm";  // to put "\"" diect in JOptionPane.showMessageDialog not allowed
      JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }
      
      
   //
   ////////// present weather <-> air temperature /////
   //    
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 20.0 && float_air_temp < 99.9) &&
                            (Arrays.asList(present_weather_36_39_array).indexOf(int_ww_code) != -1))
   {
      JOptionPane.showMessageDialog(null, "If 'air temperature' > 20.0 °C then 'present weather' can not indicate 'drifting snow' or 'blowing snow'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }
   
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 20.0 && float_air_temp < 99.9) &&
                            (Arrays.asList(present_weather_48_49_array).indexOf(int_ww_code) != -1))
   {
      JOptionPane.showMessageDialog(null, "If 'air temperature' > 20.0 °C then 'present weather' can not indicate 'depositing rime'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }
   
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 20.0 && float_air_temp < 99.9) &&
                            (Arrays.asList(present_weather_56_57_array).indexOf(int_ww_code) != -1))
   {
      JOptionPane.showMessageDialog(null, "If 'air temperature' > 20.0 °C then 'present weather' can not indicate 'freezing drizzle'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }
   
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 20.0 && float_air_temp < 99.9) &&
                            (Arrays.asList(present_weather_66_67_array).indexOf(int_ww_code) != -1))
   {
      JOptionPane.showMessageDialog(null, "If 'air temperature' > 20.0 °C then 'present weather' can not indicate 'freezing rain'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }
    
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 20.0 && float_air_temp < 99.9) &&
                            (Arrays.asList(present_weather_68_69_array).indexOf(int_ww_code) != -1))
   {
      JOptionPane.showMessageDialog(null, "If 'air temperature' > 20.0 °C then 'present weather' can not indicate 'snow'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }
      
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 20.0 && float_air_temp < 99.9) &&
                            (Arrays.asList(present_weather_70_75_array).indexOf(int_ww_code) != -1))
   {
      JOptionPane.showMessageDialog(null, "If 'air temperature' > 20.0 °C then 'present weather' can not indicate 'snow flakes'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }
   
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 20.0 && float_air_temp < 99.9) &&
                            (Arrays.asList(present_weather_76_79_array).indexOf(int_ww_code) != -1))
   {
      JOptionPane.showMessageDialog(null, "If 'air temperature' > 20.0 °C then 'present weather' can not indicate 'snow grains/crystals' or 'ice prisms/pellets'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }
       
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 20.0 && float_air_temp < 99.9) &&
                            (Arrays.asList(present_weather_83_86_array).indexOf(int_ww_code) != -1))
   {
      JOptionPane.showMessageDialog(null, "If 'air temperature' > 20.0 °C then 'present weather' can not indicate 'snow shower(s)'", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }
       
   
   //
   ////////// icing <-> air temperature /////
   // 
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 20.0 && float_air_temp < 99.9) && 
                             (!myicing.Is_code.equals("") || !myicing.EsEs_code.equals("") || !myicing.Rs_code.equals("")))
   {
      JOptionPane.showMessageDialog(null, "If 'air temperature' > 20.0 °C then 'Icing (Ice accretion)' is not possible", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }
   

   //
   ////////// ice <-> air temperature /////
   // 
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 25.0 && float_air_temp < 99.9) &&
                             (!myice1.ci_code.equals("") || !myice1.Si_code.equals("") || !myice1.bi_code.equals("") || !myice1.Di_code.equals("") || !myice1.zi_code.equals("")))
   {
      JOptionPane.showMessageDialog(null, "If 'air temperature' > 25.0 °C then 'Ice' is not possible", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
      level_2_ok = false;
      doorgaan = false;       
   }
   
   
   return level_2_ok;
}
   


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
private boolean checking_level_3()
{
   boolean doorgaan                               = true;
   boolean level_3_ok                             = true;   
   boolean wind_waves_period_conversion_ok        = true;
   boolean wind_waves_height_conversion_ok        = true;
   boolean first_swell_period_conversion_ok       = true;
   boolean first_swell_height_conversion_ok       = true;
   boolean second_swell_period_conversion_ok      = true;
   boolean second_swell_height_conversion_ok      = true;
   boolean air_temp_conversion_ok                 = true;
   boolean air_pressure_conversion_ok             = true;
   boolean amount_pressure_tendency_conversion_ok = true;
   boolean sea_water_temp_conversion_ok           = true;
   boolean ice_thickness_conversion_ok            = true;
   boolean ww_code_conversion_ok                  = true;
   float float_wind_waves_period                  = main.INVALID;
   float float_wind_waves_height                  = main.INVALID; 
   float float_first_swell_period                 = main.INVALID;
   float float_first_swell_height                 = main.INVALID;
   float float_second_swell_period                = main.INVALID;
   float float_second_swell_height                = main.INVALID;
   float float_air_temp                           = main.INVALID;
   float float_air_pressure_msl_corrected         = main.INVALID;
   float float_amount_pressure_tendency           = main.INVALID;
   float float_sea_water_temp                     = main.INVALID;
   float float_ice_thickness                      = main.INVALID;
   int int_ww_code                                = main.INVALID;
   Integer present_weather_48_49_array[]          = {48, 49};
   Integer present_weather_56_57_array[]          = {56, 57};
   Integer present_weather_66_67_array[]          = {66, 67};

   
   //
   ///////////////////////////// conversions //////////////////////////
   //
   
   // wind_waves_period conversion
   try
   {
      if (mywaves.wind_waves_period.equals("") == false && mywaves.wind_waves_period != null)
      {
         float_wind_waves_period = Float.parseFloat(mywaves.wind_waves_period);
         wind_waves_period_conversion_ok = true;
      }
      else
      {
         wind_waves_period_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] wind waves period conversion error; Function: checking_level_3()");
      wind_waves_period_conversion_ok = false;
   }   
   
   // wind_waves_height conversion
   try
   {
      if (mywaves.wind_waves_height.equals("") == false && mywaves.wind_waves_height != null)
      {
         float_wind_waves_height = Float.parseFloat(mywaves.wind_waves_height);
         wind_waves_height_conversion_ok = true;
      }
      else
      {
         wind_waves_period_conversion_ok = false;
      }         
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] wind waves height conversion error; Function: checking_level_3()");
      wind_waves_height_conversion_ok = false;
   }   
   
   // first swell system period conversion
   try
   {
      if (mywaves.swell_1_period.equals("") == false && mywaves.swell_1_period != null)
      {
         float_first_swell_period = Float.parseFloat(mywaves.swell_1_period);
         first_swell_period_conversion_ok = true;
      }
      else
      {
         first_swell_period_conversion_ok = false;
      }         
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] first swell period conversion error; Function: checking_level_3()");
      first_swell_period_conversion_ok = false;
   }   
   
   // first swell system height conversion
   try
   {
      if (mywaves.swell_1_height.equals("") == false && mywaves.swell_1_height != null)
      {
         float_first_swell_height = Float.parseFloat(mywaves.swell_1_height);
         first_swell_height_conversion_ok = true;
      }
      else
      {
         first_swell_height_conversion_ok = false;
      }         
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] first swell height conversion error; Function: checking_level_3()");
      first_swell_height_conversion_ok = false;
   }   
   
   // second swell system period conversion
   try
   {
      if (mywaves.swell_2_period.equals("") == false && mywaves.swell_2_period != null)
      {
         float_second_swell_period = Float.parseFloat(mywaves.swell_2_period);
         second_swell_period_conversion_ok = true;
      }
      else
      {
         second_swell_period_conversion_ok = false;
      }         
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] second swell period conversion error; Function: checking_level_3()");
      second_swell_period_conversion_ok = false;
   }   
   
   // second swell system height conversion
   try
   {
      if (mywaves.swell_2_height.equals("") == false && mywaves.swell_2_height != null)
      {
         float_second_swell_height = Float.parseFloat(mywaves.swell_2_height);
         second_swell_height_conversion_ok = true;
      }
      else
      {
         second_swell_height_conversion_ok = false;
      }         
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] second swell height conversion error; Function: checking_level_3()");
      second_swell_height_conversion_ok = false;
   }   
   
   // string air_temp conversion to float
   try
   {
      if (mytemp.air_temp.equals("") == false && mytemp.air_temp != null)
      {
         float_air_temp = Float.parseFloat(mytemp.air_temp);
         air_temp_conversion_ok = true;
      }
      else
      {
         air_temp_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] air temp conversion error; Function: checking_level_3()");
      air_temp_conversion_ok = false;
   }
   
   // string pressure_msl_corrected to float
   try
   {   
      if (mybarometer.pressure_msl_corrected.equals("") == false && mybarometer.pressure_msl_corrected != null)
      {
         float_air_pressure_msl_corrected = Float.parseFloat(mybarometer.pressure_msl_corrected);
         air_pressure_conversion_ok = true;
      }
      else
      {
         air_pressure_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] air pressure conversion error; Function: checking_level_3()");
      air_pressure_conversion_ok = false;
   }              
   
   // string amount pressure tendency to float
   try
   {   
      if (mybarograph.pressure_amount_tendency.equals("") == false && mybarograph.pressure_amount_tendency != null)
      {
         float_amount_pressure_tendency = Float.parseFloat(mybarograph.pressure_amount_tendency);
         amount_pressure_tendency_conversion_ok = true;
      }
      else
      {
         amount_pressure_tendency_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] amount air pressure tendency conversion error; Function: checking_level_3()");
      amount_pressure_tendency_conversion_ok = false;
   }              
   
   // string SST to float
   try
   {
      if (mytemp.sea_water_temp.equals("") == false && mytemp.sea_water_temp != null)
      {
         float_sea_water_temp = Float.parseFloat(mytemp.sea_water_temp);
         sea_water_temp_conversion_ok = true;
      }
      else
      {
         sea_water_temp_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] SST conversion error; Function: checking_level_3()");
      sea_water_temp_conversion_ok = false;
   }
   
   // string thickness ice accretion (EsEs) to float
   try
   {
      if (myicing.EsEs_code.equals("") == false && myicing.EsEs_code != null)
      {
         float_ice_thickness = Float.parseFloat(myicing.EsEs_code);                      // EsEs_code = ice thickness in centimetres
         ice_thickness_conversion_ok = true;
      }
      else
      {
         ice_thickness_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      main.log_turbowin_system_message("[GENERAL] ice thickness (EsEs) conversion error; Function: checking_level_3()");
      ice_thickness_conversion_ok = false;
   }
   
   // string ww code conversion to int
   try
   {
      if (mypresentweather.ww_code.equals("") == false && mypresentweather.ww_code != null)
      {
         int_ww_code = Integer.parseInt(mypresentweather.ww_code);
         ww_code_conversion_ok = true;
      }
      else
      {
         ww_code_conversion_ok = false;
      }
   }
   catch (NumberFormatException ex)
   {
      //doorgaan = true;
      main.log_turbowin_system_message("[GENERAL] ww conversion error; Function: checking_level_3()");
      ww_code_conversion_ok = false;
   }      
   
   
   
   //
   ///////////////////////////// checks //////////////////////////
   //
   
   
   // speed ship
   //
   if ((doorgaan == true) && (myposition.vs_code.equals("8") || myposition.vs_code.equals("9")))
   {
      String info = "Ship's speed > 35 knots \n Press the NO button if it was a typing error, press the YES button if this average speed is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
   
   
   // wind speed
   //
   if ( (doorgaan == true) && (main.wind_units.trim().indexOf(main.M_S) != -1) && (mywind.int_true_wind_speed > 28 && mywind.int_true_wind_speed < 500.0) )
   {
      // wind speed in m/s
      String info = "Wind speed > 28 m/s (> 55 knots) \n Press the NO button if it was a typing error, press the YES button if this wind speed is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }

   if ( (doorgaan == true) && (main.wind_units.trim().indexOf(main.KNOTS) != -1) && (mywind.int_true_wind_speed > 55 && mywind.int_true_wind_speed < 500.0) )
   {
      // wind speed in m/s
      String info = "Wind speed > 55 knots \n Press the NO button if it was a typing error, press the YES button if this wind speed is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }

   
   // wind waves period
   //
   if ((doorgaan == true) && wind_waves_period_conversion_ok == true)
   {
      if (float_wind_waves_period > 25.0 && float_wind_waves_period < 99.9)
      {
         String info = "Wind waves period > 25 seconds \n Press the NO button if it was a typing error, press the YES button if this wind waves period is ok";
         if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
         {
            JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
            doorgaan = false;
            level_3_ok = false;
         }
      }
   }
   
   
   // wind waves height
   //
   if ((doorgaan == true) && wind_waves_height_conversion_ok == true)
   {
      if (float_wind_waves_height > 12.2 && float_wind_waves_height < 99.9)
      {
         String info = "Wind waves height > 12.2 metres \n Press the NO button if it was a typing error, press the YES button if this wind waves height is ok";
         if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
         {
            JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
            doorgaan = false;
            level_3_ok = false;
         }
      }
   }   
   
   
   // first swell period
   //
   if ((doorgaan == true) && first_swell_period_conversion_ok == true)
   {
      if (float_first_swell_period > 25.0 && float_first_swell_period < 99.9)
      {
         String info = "First swell system waves period > 25 seconds \n Press the NO button if it was a typing error, press the YES button if this first swell system waves period is ok";
         if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
         {
            JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
            doorgaan = false;
            level_3_ok = false;
         }
      }
   }
   
   
   // first swell height
   //
   if ((doorgaan == true) && first_swell_height_conversion_ok == true)
   {
      if (float_first_swell_height > 12.2 && float_first_swell_height < 99.9)
      {
         String info = "First swell system waves height > 12.2 metres \n Press the NO button if it was a typing error, press the YES button if this first swell system height waves is ok";
         if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
         {
            JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
            doorgaan = false;
            level_3_ok = false;
         }
      }
   }
   
   
   // second swell period
   //
   if ((doorgaan == true) && second_swell_period_conversion_ok == true)
   {
      if (float_second_swell_period > 25.0 && float_second_swell_period < 99.9)
      {
         String info = "Second swell system waves period > 25 seconds \n Press the NO button if it was a typing error, press the YES button if this second swell system waves period is ok";
         if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
         {
            JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
            doorgaan = false;
            level_3_ok = false;
         }
      }
   }
   
   
   // second swell height
   //
   if ((doorgaan == true) && second_swell_height_conversion_ok == true)
   {
      if (float_second_swell_height > 12.2 && float_second_swell_height < 99.9)
      {
         String info = "Second swell system waves height > 12.2 metres \n Press the NO button if it was a typing error, press the YES button if this second swell system height waves is ok";
         if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
         {
            JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
            doorgaan = false;
            level_3_ok = false;
         }
      }
   }
   
   
   // wind speed <--> wand waves height
   //
   if ( (doorgaan == true) && (main.wind_units.trim().indexOf(main.M_S) != -1) && (mywind.int_true_wind_speed > 5 && mywind.int_true_wind_speed < 500.0) && 
                              (wind_waves_height_conversion_ok && float_wind_waves_height <= 0.01) )
   {
      // wind [m/s]
      String info = "Wind speed > 5 m/s (> 10 kts) and wind waves height 0 metres \n Press the NO button if it was a typing error, press the YES button if this observation is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
   
   if ( (doorgaan == true) && (main.wind_units.trim().indexOf(main.KNOTS) != -1) && (mywind.int_true_wind_speed > 10 && mywind.int_true_wind_speed < 500.0) && 
                              (wind_waves_height_conversion_ok && float_wind_waves_height <= 0.01) )
   {
      // wind [knots]
      String info = "Wind speed > 10 kts and wind waves height 0 metres \n Press the NO button if it was a typing error, press the YES button if this observation is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
   
   if ( (doorgaan == true) && (main.wind_units.trim().indexOf(main.M_S) != -1) && (mywind.int_true_wind_speed > 21 && mywind.int_true_wind_speed < 500.0) && 
                              (wind_waves_height_conversion_ok && float_wind_waves_height < 2.3) )
   {
      // wind [m/s]
      String info = "Wind speed > 21 m/s (> 41 kts) and wind waves height < 2.3 metres \n Press the NO button if it was a typing error, press the YES button if this observation is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
   
  if ( (doorgaan == true) && (main.wind_units.trim().indexOf(main.KNOTS) != -1) && (mywind.int_true_wind_speed > 41 && mywind.int_true_wind_speed < 500.0) && 
                             (wind_waves_height_conversion_ok && float_wind_waves_height < 2.3) )
   {
      // wind [knots]
      String info = "Wind speed > 41 kts and wind waves height < 2.3 metres \n Press the NO button if it was a typing error, press the YES button if this observation is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
   
  
   // air temp
   //
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 50.0 && float_air_temp < 99.9))
   {
      String info = "Air temperature > 50.0 °C \n Press the NO button if it was a typing error, press the YES button if this air temp is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
           
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp < -20.0 && float_air_temp > -99.9))
   {
      String info = "Air temperature < -20.0 °C \n Press the NO button if it was a typing error, press the YES button if this air temp is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }           
           
           
   // icing <-> air temperature 
   // 
   if ((doorgaan == true) && air_temp_conversion_ok && (float_air_temp > 4.0 && float_air_temp < 99.9) && 
                             (!myicing.Is_code.equals("") || !myicing.EsEs_code.equals("") || !myicing.Rs_code.equals("")))
   {
      String info = "Air temperature > 4.0 °C and Icing (Ice accretion)\n Press the NO button if it was a typing error, press the YES button if this observation is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }  
      
   
   // air pressure (MSL)
   //
   if ((doorgaan == true) && air_pressure_conversion_ok && !(float_air_pressure_msl_corrected >= 910.0 && float_air_pressure_msl_corrected <= 1050.0))
   {
      String info = "Air pressure (MSL) < 950.0 hPa or > 1050.0 hPa\n Press the NO button if it was a typing error, press the YES button if this air pressure (MSL) is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
     
   
   // amount pressure tendency
   //
   if ((doorgaan == true) && amount_pressure_tendency_conversion_ok && (float_amount_pressure_tendency > 30.0 && float_amount_pressure_tendency <= 99.9))
   {
      String info = "pressure tendency amount, last 3 hours > 30.0 hPa\n Press the NO button if it was a typing error, press the YES button if this amount of pressure tendency is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }

   
   // amount pressure tendency <-> characteristic pressure tendency (a)
   //
   if ((doorgaan == true) && amount_pressure_tendency_conversion_ok && (float_amount_pressure_tendency >= 0.0 && float_amount_pressure_tendency <= 99.9) && 
                                                                       (mybarograph.a_code.equals("") == true)  )
   {
      String info = "Amount of pressure tendency available and characteristic of tendency not available?\n Press the NO button if it was a typing error, press the YES button if this observation is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
    
   
   // SST
   //
   if ((doorgaan == true) && sea_water_temp_conversion_ok && (float_sea_water_temp > 35.0))
   {
      String info = "Seawater temperature > 35.0 °C?\n Press the NO button if it was a typing error, press the YES button if this sewater temp is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
   
   
   // thickness ice accretion (EsEs)
   //
   if ((doorgaan == true) && ice_thickness_conversion_ok && (float_ice_thickness > 20.0)) 
   {
      String info = "Ice thickness > 20.0 centimetres?\n Press the NO button if it was a typing error, press the YES button if this ice thickness is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }        
  
   
   // present weather <-> icing
   //  
   if ( (doorgaan == true) && ww_code_conversion_ok && (Arrays.asList(present_weather_48_49_array).indexOf(int_ww_code) != -1) && 
                                        (myicing.Is_code.equals("") && myicing.EsEs_code.equals("") && myicing.Rs_code.equals("")) )
   {
      String info = "Fog, depositing rime (present weather) and no ICING?\n Press the NO button if it was a typing error, press the YES button if this observation is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
   
   if ( (doorgaan == true) && ww_code_conversion_ok && (Arrays.asList(present_weather_56_57_array).indexOf(int_ww_code) != -1) && 
                                        (myicing.Is_code.equals("") && myicing.EsEs_code.equals("") && myicing.Rs_code.equals("")) )
   {
      String info = "Freezing drizzle (present weather) and no ICING?\n Press the NO button if it was a typing error, press the YES button if this observation is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
   
   if ( (doorgaan == true) && ww_code_conversion_ok && (Arrays.asList(present_weather_66_67_array).indexOf(int_ww_code) != -1) && 
                                        (myicing.Is_code.equals("") && myicing.EsEs_code.equals("") && myicing.Rs_code.equals("")) )
   {
      String info = "Freezing rain (present weather) and no ICING?\n Press the NO button if it was a typing error, press the YES button if this observation is ok";
      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + ", please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME, JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
         level_3_ok = false;
      }
   }
   
   
   
   
   return level_3_ok;
}


   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
   private void main_windowIconfied(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_main_windowIconfied
      // TODO add your handling code here:
      
      //Check the SystemTray is supported
      if (!SystemTray.isSupported()) 
      {
         System.out.println("+++ SystemTray is not supported");
      }
      else
      {
         setVisible(false);
         
         final PopupMenu popup = new PopupMenu();
         trayIcon = new TrayIcon(createImage(main.ICONS_DIRECTORY + "tray.png"));
         //final SystemTray tray = SystemTray.getSystemTray();
         
      
         // set tool tip text
         trayIcon.setToolTip("TurboWin+ weather observations");
      
         
         // message pop-up (left mouse click on TurboWin+ system tray icon)
         //     NB The only way to display a "dynamic" tooltip that pops up when the mouse cursor hovers over the tray icon
         // 
         MouseListener ml = new MouseListener() 
         {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
               if (e.getButton() == MouseEvent.BUTTON1)           // left mouse button
               {
                  String info = "no sensor data available";        // eg just after start-up (takes 1 minute to start collecting) 
                   
                  if (RS232_connection_mode == 4)                  // Mintaka Duo
                  {
                     // check if we receive valid data (parameter day is arbitrary, could eg also be month etc.)
                     try
                     {
                        // NB tray icon message will be set in: main_RS232_RS422.RS232_Mintaka_Duo_Read_Sensor_Data_PPPP_For_Obs()
                        boolean local_tray_icon_clicked = true;
                        main_RS232_RS422.RS232_Mintaka_Duo_Read_Sensor_Data_PPPP_For_Obs(local_tray_icon_clicked); // retrieve mybarometer.pressure_reading
                     } // try
                     catch (NumberFormatException en)
                     {
                        info = "no sensor data available";   // eg just after start-up (takes 1 minute to start collecting) 
                     } // catch       
                  } // if (RS232_connection_mode == 4)
                  
                  else if (RS232_connection_mode == 5 || RS232_connection_mode == 6)       // Mintaka Star USB or Mintaka Star WiFi
                  {
                     // check if we receive valid data (parameter day is arbitrary, could eg also be month etc.)
                     try
                     {
                        // NB tray icon message will be set in: main_RS232_RS422.RS232_Mintaka_Star_Read_Sensor_Data_PPPP_And_GPS_For_Obs()
                        boolean local_tray_icon_clicked = true;
                        main_RS232_RS422.RS232_Mintaka_Star_Read_Sensor_Data_PPPP_For_Obs(local_tray_icon_clicked); // retrieve mybarometer.pressure_reading
                     } // try
                     catch (NumberFormatException en)
                     {
                        info = "no sensor data available";   // eg just after start-up (takes 1 minute to start collecting) 
                     } // catch       
                  } // if (RS232_connection_mode == 5 || RS232_connection_mode == 6)                  
                  
                  else if ( (RS232_connection_mode == 1) || (RS232_connection_mode == 2) ) // PTB220 or PTB330
                  {
                     // check if we receive valid data (parameter day is arbitrary, could eg also be month etc.)
                     try
                     {
                        // NB tray icon message will be set in: main_RS232_RS422.RS232_Read_Sensor_Data_PPPP_For_Obs()
                        boolean local_tray_icon_clicked = true;
                        main_RS232_RS422.RS232_Read_Sensor_Data_PPPP_For_Obs(local_tray_icon_clicked); // retrieve mybarometer.pressure_reading
                     } // try
                     catch (NumberFormatException en)
                     {
                        info = "no sensor data available";   // eg just after start-up (takes 1 minute to start collecting) 
                     } // catch       
                  } // if ( (RS232_connection_mode == 1) || (RS232_connection_mode == 2) )
                  
                  else if ( (RS232_connection_mode == 3) && (defaultPort != null) )    // AWS
                  {
                     // check if we receive valid data (parameter day is arbitrary, could eg also be month etc.)
                     try
                     {
                        int int_day = Integer.parseInt(mydatetime.day);
                        
                        if ( (int_day >= 1) && (int_day <= 31) )
                        {   
                           String wind_speed_units = "";
                     
                           if (main.wind_units.indexOf(main.M_S) == -1)  // so knots
                           {
                              wind_speed_units = "kn";
                           }   
                           else
                           {
                              wind_speed_units = "m/s";
                           }   
                           
                           info = "";
                           info = mydatetime.day + " " + mydatetime.month + " " + mydatetime.year + "  " + mydatetime.hour + "." + mydatetime.minute + " UTC" + "\n" +
                                  "\n" +
                                  "pressure at sensor height: " + mybarometer.pressure_reading + " hPa" + "\n" +
                                  "pressure at MSL: " + mybarometer.pressure_msl_corrected + " hPa" + "\n" +
                                  "\n" +
                                  "air temp at sensor height: " + mytemp.air_temp + " °C" + "\n" +
                                  "SST at sensor depth: " + mytemp.sea_water_temp + " °C" + "\n" +
                                  "\n" +
                                  "true wind speed at sensor height: " + mywind.int_true_wind_speed + " " + wind_speed_units + "\n" +
                                  "true wind dir at sensor height: " + mywind.int_true_wind_dir + " degr";
                        } // if ( (int_day >= 1) && (int_day <= 31) )
                        
                        trayIcon.displayMessage("TurboWin+", info, MessageType.INFO);
                        
                     } // try
                     catch (NumberFormatException en)
                     {
                        info = "no sensor data available";   // eg just after start-up (takes 1 minute to start collecting) 
                        trayIcon.displayMessage("TurboWin+", info, MessageType.INFO);
                     } // catch       
                     
                  } // else if ( (RS232_connection_mode == 3) && (defaultPort != null) ) 
                  else // TurboWin+ stand-alone mode (no serial connection to barometer or AWS and no WiFi)
                  {
                     info = "right click icon to maximize or exit TurboWin+";
                     trayIcon.displayMessage("TurboWin+", info, MessageType.INFO);
                  } // else 
                  
               } // if (e.getButton() == MouseEvent.BUTTON1) 
            } // public void mouseClicked(MouseEvent e) 

            @Override
            public void mousePressed(MouseEvent e) {
               //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
               //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
               //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
               //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
         }; // MouseListener ml = new MouseListener()
         trayIcon.addMouseListener(ml); 
      

         // Create a pop-up menu components
         //
         
         if ( (RS232_connection_mode == 3) && (defaultPort != null) )    // AWS
         {
            MenuItem pressure_graph_Item = new MenuItem("pressure graph");
            MenuItem wind_dir_graph_Item = new MenuItem("wind direction graph");
            MenuItem wind_speed_graph_Item = new MenuItem("wind speed graph");
            MenuItem sst_graph_Item = new MenuItem("SST graph");
            MenuItem air_temp_graph_Item = new MenuItem("air temp graph");
            MenuItem total_graph_Item = new MenuItem("total graph");
            
            popup.add(pressure_graph_Item);
            popup.add(wind_dir_graph_Item);
            popup.add(wind_speed_graph_Item);
            popup.add(sst_graph_Item);
            popup.add(air_temp_graph_Item);
            popup.add(total_graph_Item);
            
            popup.addSeparator();
            
            // air pressure graph
            //
            pressure_graph_Item.addActionListener(new ActionListener() 
            {
               @Override
               public void actionPerformed(ActionEvent e) 
               {
                  Graphs_Pressure_Sensor_Data_actionPerformed(null);  
               } // public void actionPerformed(ActionEvent e)
            });            
            
            // wind dir graph
            //
            wind_dir_graph_Item.addActionListener(new ActionListener() 
            {
               @Override
               public void actionPerformed(ActionEvent e) 
               {
                  Graph_Wind_Dir_Sensor_Data_actionPerformed(null);  
               } // public void actionPerformed(ActionEvent e)
            });
            
            // wind speed graph
            //
            wind_speed_graph_Item.addActionListener(new ActionListener() 
            {
               @Override
               public void actionPerformed(ActionEvent e) 
               {
                  Graphs_Wind_Speed_Sensor_Data_actionPerformed(null);  
               } // public void actionPerformed(ActionEvent e)
            });
            
            // SST graph
            //
            sst_graph_Item.addActionListener(new ActionListener() 
            {
               @Override
               public void actionPerformed(ActionEvent e) 
               {
                  Graphs_SST_Sensor_data_actionPerformed(null);  
               } // public void actionPerformed(ActionEvent e)
            });
            
            // air temp graph
            //
            air_temp_graph_Item.addActionListener(new ActionListener() 
            {
               @Override
               public void actionPerformed(ActionEvent e) 
               {
                  Graphs_Airtemp_Sensor_Data_actionPerformed(null);  
               } // public void actionPerformed(ActionEvent e)
            });
            
            // total graph (pressure, air temp, wind dir and wind speed in one total graph)
            //
            total_graph_Item.addActionListener(new ActionListener() 
            {
               @Override
               public void actionPerformed(ActionEvent e) 
               {
                  Graph_All_Sensor_Data_actionPerformed(null);  
               } // public void actionPerformed(ActionEvent e)
            });
            
         } // if ( (RS232_connection_mode == 3) && (defaultPort != null) )  
         

         if ( ((RS232_connection_mode == 1) || (RS232_connection_mode == 2) || (RS232_connection_mode == 4) || (RS232_connection_mode == 5)) && (defaultPort != null) ) // PTB220 or PTB330 or Mintaka Duo or Mintaka Star USB
         {
            MenuItem pressure_graph_Item = new MenuItem("pressure graph");
            popup.add(pressure_graph_Item);
            
            popup.addSeparator();
            
            // air pressure graph
            //
            pressure_graph_Item.addActionListener(new ActionListener() 
            {
               @Override
               public void actionPerformed(ActionEvent e) 
               {
                  Graphs_Pressure_Sensor_Data_actionPerformed(null);  
               } // public void actionPerformed(ActionEvent e)
            });
         } // if ( ((RS232_connection_mode == 1) || (RS232_connection_mode == 2) || (RS232_connection_mode == 4) || (RS232_connection_mode == 5)) && (defaultPort != null) ) // PTB220 or PTB330 or Mintaka Duo


         if (RS232_connection_mode == 6) // Mintaka Star WiFi
         {
            MenuItem pressure_graph_Item = new MenuItem("pressure graph");
            popup.add(pressure_graph_Item);
            
            popup.addSeparator();
            
            // air pressure graph
            //
            pressure_graph_Item.addActionListener(new ActionListener() 
            {
               @Override
               public void actionPerformed(ActionEvent e) 
               {
                  Graphs_Pressure_Sensor_Data_actionPerformed(null);  
               } // public void actionPerformed(ActionEvent e)
            });
         } // if (RS232_connection_mode == 6)

         
         
         // exit and restore (maximize) always present!
         //
         MenuItem restoreItem = new MenuItem("Maximize TurboWin+");
         popup.add(restoreItem);
         MenuItem exitItem = new MenuItem("Exit TurboWin+");
         popup.add(exitItem);
       
        
         // exit program
         //
         exitItem.addActionListener(new ActionListener() 
         {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
/*               
               String info = "Are you sure you want to exit this application?";
               if ( ((RS232_connection_mode == 1) || (RS232_connection_mode == 2) || (RS232_connection_mode == 3)) && (defaultPort != null) )
               {
                  info += "\n (TurboWin+ will stop with monitoring and collecting of the sensor data)";
               }   

               int result = JOptionPane.showConfirmDialog(main.this, info, "Exit " + APPLICATION_NAME, JOptionPane.YES_NO_OPTION);

               if (result == JOptionPane.YES_OPTION)
               {
                  tray.remove(trayIcon);
                  System.exit(0);     
               }     
*/
               main_windowClosing(null);
            } // public void actionPerformed(ActionEvent e)
         });
        
        
         // restore (maximize main screen)
         //
         restoreItem.addActionListener(new ActionListener() 
         {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
               setExtendedState(NORMAL); 
               setVisible(true); 
               tray.remove(trayIcon);
            } // public void actionPerformed(ActionEvent e)
         });
        
         
         trayIcon.setPopupMenu(popup);
       
         try 
         {
            tray.add(trayIcon);
         } 
         catch (AWTException e) 
         {
            System.out.println("+++ TrayIcon could not be added.");
         }      
         
      } // else (so system tray available)
   }//GEN-LAST:event_main_windowIconfied

   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/     
   private void Info_Calculator_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Info_Calculator_menu_actionPerformed
      // TODO add your handling code here:
      
      calculator form = new calculator();               
      form.setSize(350, 600);
      form.setVisible(true); 
   }//GEN-LAST:event_Info_Calculator_menu_actionPerformed

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/     
   private void Maintenance_obs_format_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Maintenance_obs_format_actionPerformed
      // TODO add your handling code here:
      mode = SET_OBS_FORMAT;

      mypassword form = new mypassword();
      form.setSize(400, 300);
      form.setVisible(true);
   }//GEN-LAST:event_Maintenance_obs_format_actionPerformed

	
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/     
   private void seawater_temp_mainscreen_mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_seawater_temp_mainscreen_mouseClicked
      // TODO add your handling code here:
      Input_Temperatures_menu_actionPerformed(null);
   }//GEN-LAST:event_seawater_temp_mainscreen_mouseClicked

	
	
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/     
   private void Graph_All_Sensor_Data_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Graph_All_Sensor_Data_actionPerformed
      // TODO add your handling code here:
       
      if (graph_form != null)
      {
         if (sensor_data_file_ophalen_timer_is_gecreeerd == true)  // 15-05-2013
         {
            if (RS232_view.sensor_data_file_ophalen_timer.isRunning())
            {
               RS232_view.sensor_data_file_ophalen_timer.stop();
            }
         }
         RS232_view.sensor_data_file_ophalen_timer = null;
      
         sensor_data_file_ophalen_timer_is_gecreeerd = false;
          
         //graph_form.dispose();
         graph_form.setVisible(false);
      }
       
      mode_grafiek = MODE_ALL_PARAMETERS;
      
      graph_form = new RS232_view();
      //graph_form.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());       // full screen
      graph_form.setExtendedState(MAXIMIZED_BOTH); 
      graph_form.setVisible(true);  		
		
   }//GEN-LAST:event_Graph_All_Sensor_Data_actionPerformed

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
   private void Maintenance_WOW_settings_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Maintenance_WOW_settings_actionPerformed
      // TODO add your handling code here:
      
      mode = SET_WOW_APR_SETTINGS;

      mypassword form = new mypassword();
      form.setSize(400, 300);
      form.setVisible(true);      
   }//GEN-LAST:event_Maintenance_WOW_settings_actionPerformed

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
   private void Info_System_Log_menu_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Info_System_Log_menu_actionPerformed
      // TODO add your handling code here:
      
      mysystem_log form = new mysystem_log();
      form.setExtendedState(MAXIMIZED_BOTH);                      // full screen
      form.setVisible(true);           
   }//GEN-LAST:event_Info_System_Log_menu_actionPerformed

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
   private void Maintenance_server_settings_actionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Maintenance_server_settings_actionperformed
      // TODO add your handling code here:
      
      mode = SET_SERVER_SETTINGS;

      mypassword form = new mypassword();
      form.setSize(400, 300);
      form.setVisible(true);      
      
   }//GEN-LAST:event_Maintenance_server_settings_actionperformed

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
   private void Themes_4_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Themes_4_actionPerformed
      
      // TODO add your handling code here:
      
      //
      //////// Sunset, Nimbus (vanaf Java 1.6.10) based
      //
      // NB dit is eigenlijk geen Theme maar compleet ander color scheme
      try
      {
         for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
         {
            if ("Nimbus".equals(info.getName()))
            {
               UIManager.setLookAndFeel(info.getClassName());
            
               // eg for the color values see: http://www.rapidtables.com/web/color/RGB_Color.htm
             
               //UIManager.put("control", new Color(214,217,223));                         // Nimbus default
               UIManager.put("control", new Color(255,110,110));                           // panels, frame
            
               UIManager.put("nimbusBase", new Color(51,98,140));                          // menu background unfolded items, radio buttons, yes/no buttons
               UIManager.put("nimbusFocus", new Color(115,164,209));                       // border color selected control (text field, button, radio button etc.)
            
               //UIManager.put("nimbusLightBackground", new Color(255,255,255));           // Nimbus default    // controls wit
               UIManager.put("nimbusLightBackground", new Color(255,204,204));             // controls 
            
               //UIManager.put("nimbusSelectionBackground", new Color(57,105,138));        // Nimbus default    // selected text  ONBELANGRIJK
               UIManager.put("text", new Color(0,0,0));                                    // Nimbus default, black texten
            
               UIManager.put("nimbusBlueGrey", new Color(169,176,190));                    // Nimbus default, menu bar, text field, button, radio button etc.

               SwingUtilities.updateComponentTreeUI(main.this);                            // moet komen na setLookAndFeel !!!
            
               jTextField4.setBackground(new java.awt.Color(255,51,51));                   // status bar (for system messages)
               //jTextField4.setBackground(new java.awt.Color(255,153,153));
            
               break;
            }
         }
         theme_mode = THEME_NIMBUS_SUNSET;
      }
      catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
      {
         String info = "Nimbus related Themes not supported on this computer";
         JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " message", JOptionPane.WARNING_MESSAGE);
         main.log_turbowin_system_message("[GENERAL] " + info);
      }
   }//GEN-LAST:event_Themes_4_actionPerformed

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
   
   private void Info_send_System_log_menu_actionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Info_send_System_log_menu_actionperformed
      // TODO add your handling code here:
      

   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         /*
         // Version 6 of the Java Platform, Standard Edition (Java SE), continues to narrow the gap with
         // new system tray functionality, better  print support for JTable, and now the Desktop API
         // (java.awt.Desktop API).
         //
         // Use the Desktop.isDesktopSupported() method to determine whether the Desktop API is available.
         // On the Solaris Operating System and the Linux platform, this API is dependent on Gnome libraries.
         // If those libraries are unavailable, this method will return false. After determining that the API is
         // supported, that is, the isDesktopSupported() returns true, the application can retrieve a Desktop
         // instance using the static method getDesktop().
         //
         */
         Desktop desktop          = null;
         String email_body_line   = "";
         String email_body_line_1 = "";
         String email_body_line_2 = "";

         // Before more Desktop API is used, first check
         // whether the API is supported by this particular
         // virtual machine (VM) on this particular host.
         if (Desktop.isDesktopSupported())
         {
            desktop = Desktop.getDesktop();
            try
            {
               TimeZone timeZone = TimeZone.getTimeZone("UTC");
               Calendar cal = Calendar.getInstance(timeZone);
               
               String file_naam_1 = "turbowin_system_" + sdf_tsl_1.format(cal.getTime()) + ".txt";
               
               
               cal.add(Calendar.MONTH, -1);
               String file_naam_2 = "turbowin_system_" + sdf_tsl_1.format(cal.getTime()) + ".txt";
               
               // NB OK but not possible to go to previuos month [deprecated] String file_naam = "turbowin_system_" + sdf_tsl_1.format(new Date()) + ".txt";     
               
               String volledig_path_turbowin_system_logs_1 = main.logs_dir + java.io.File.separator + main.TURBOWIN_SYSTEM_LOGS_DIR + java.io.File.separator + file_naam_1;
               String volledig_path_turbowin_system_logs_2 = main.logs_dir + java.io.File.separator + main.TURBOWIN_SYSTEM_LOGS_DIR + java.io.File.separator + file_naam_2;
               
               //
               // NB write every system log line in the email body fails !!
               //
               
               
               // check if System log 1 file exists (current month)
               final File system_file_1 = new File(volledig_path_turbowin_system_logs_1);
               if (system_file_1.exists() == true)
               {
                  email_body_line_1 = "Please attach manually the file: " + volledig_path_turbowin_system_logs_1;
                  email_body_line = email_body_line_1;
               } // if (system_file_1.exists() == true)
               
               // check if System log 2 file exists (previous month)
               final File system_file_2 = new File(volledig_path_turbowin_system_logs_2);
               if (system_file_2.exists() == true)
               {
                  email_body_line_2 = "Please attach manually the file: " + volledig_path_turbowin_system_logs_2;
                  
                  if (system_file_1.exists() == true)
                  {
                     email_body_line += "\nand\n\n";
                     email_body_line +=  email_body_line_2;
                  }
                  else
                  {
                     email_body_line = email_body_line_2;
                  }
               } // if (system_file_2.exists() == true)
               
               
               if (system_file_1.exists() == false && system_file_2.exists() == false)
               {
                  JOptionPane.showMessageDialog(null, "No " + APPLICATION_NAME + " system log files found", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);        
               }
               else
               {
                  /////// invoke email program //////
                  //
                  String email_recipient = "";
                  String email_subject = APPLICATION_NAME + " System logs " + main.ship_name;
                  String mail_txt = email_recipient + "?subject=" + email_subject  + "&body=" + email_body_line;
                  //String mail_txt = obs_email_recipient + "?subject=" + obs_email_subject_new  + "&body=test";
                  URI uriMailTo = null;
                  try
                  {
                     uriMailTo = new URI("mailto", mail_txt, null);
                  }
                  catch (URISyntaxException ex)
                  {
                     JOptionPane.showMessageDialog(null, "Error invoking default Email program" + " (" + ex + ")", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                  }

                  desktop.mail(uriMailTo);
               } // else
            } // try
            catch (IOException ex)
            {
               JOptionPane.showMessageDialog(null, "Error invoking default Email program" + " (" + ex + ")", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            }
         } // if (Desktop.isDesktopSupported())
         else
         {
            JOptionPane.showMessageDialog(null, "Error invoking default Email program (-Desktop- method not supported on this computer system)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         } // else

         return null;
      } // protected Void doInBackground() throws Exception


   }.execute(); // new SwingWorker<Void, Void>()
      
   }//GEN-LAST:event_Info_send_System_log_menu_actionperformed

   
   
   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
protected static Image createImage(String path) 
   {
      //URL imageURL = TrayIconDemo.class.getResource(path);
      URL imageURL = main.class.getResource(path);
         
      if (imageURL == null) 
      {
         System.out.println("+++ tray icon image resource not found: " + path);
         return null;
      } 
      else 
      {
         return (new ImageIcon(imageURL)).getImage();
      }
   }
      
   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
private void Output_obs_to_clipboard_FM13()
{
   Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
   StringSelection selection = new StringSelection(obs_write);
   clipboard.setContents(selection, null);
   
   IMMT_log();
         
   Reset_all_meteo_parameters();
}
   


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/   
private void Output_obs_to_clipboard_format_101()
{
   boolean doorgaan                 = true;
   String clipboard_format_101_line = "";
   
   
   // read the compressed obs (format 101) which is the only line in file HPK_format_101.txt
   clipboard_format_101_line = get_format_101_obs_from_file();
   if (clipboard_format_101_line.equals("") == true)
   {
      doorgaan = false;
   }
   
   if (doorgaan == true)
   {   
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      StringSelection selection = new StringSelection(clipboard_format_101_line);
      clipboard.setContents(selection, null);
   }
   
   IMMT_log();
         
   Reset_all_meteo_parameters();
}

   
   
/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private String compile_obs_for_AWS()  
{
   String AWS_obs                       = "";
   String AWS_id                        = "";
   String AWS_diff_SLL_WL               = "";
   String AWS_dd                        = "";
   String AWS_ff                        = "";
   String AWS_TTT                       = "";
   String AWS_rh                        = "";
   String AWS_sst                       = "";
   String AWS_VV                        = "";
   String AWS_ww                        = "";
   String AWS_W1                        = "";
   String AWS_W2                        = "";
   String AWS_N                         = "";
   String AWS_Nh                        = "";
   String AWS_Cl                        = "";
   String AWS_Cm                        = "";
   String AWS_Ch                        = "";    
   String AWS_h                         = "";
   String AWS_Pw                        = "";
   String AWS_Hw                        = "";
   String AWS_Dw1                       = "";
   String AWS_Pw1                       = "";
   String AWS_Hw1                       = "";
   String AWS_Dw2                       = "";
   String AWS_Pw2                       = "";
   String AWS_Hw2                       = "";
   String AWS_EsEs                      = "";
   String AWS_Rs                        = "";
   String AWS_Is                        = "";
   String AWS_ci                        = "";
   String AWS_bi                        = "";
   String AWS_zi                        = "";
   String AWS_Si                        = "";
   String AWS_Di                        = "";
  
   
   double double_wind_speed;
   double double_sst;
   double double_air_temp;
   
   
   // See docs: - "EUCAWS inputs/outputs Complementary information about codes by Pierre Blouch"
   //           - "SMD & PSO formats by jean-Baptiste Cohuet" 
   
   
   // AWS NMEA identifier
   //
   AWS_id                      = "$PTBWP";
   
   
   // departure of SLL from the actual sea level (diff SLL - WL) [format sHH; range -10..20; resolution 1; units m]
   //
   AWS_diff_SLL_WL             = diff_sll_wl;                  // global var (set in "Maintenance -> Station data" and "Input -> Wind")
   
   
   // wind direction [format WST; range 10..360; resolution 10; units deg]
   //
   if (true_wind_dir_from_AWS_present == false)               // if parameter is measured by AWS than must be not a part of the string send to the AWS
   {
      if (mywind.int_true_wind_dir == mywind.WIND_DIR_VARIABLE)
      {
         AWS_dd = "0";                                       // NB wind dir = variable -> 0 : special for EUCAWS!! 
      }  
      else if (mywind.int_true_wind_dir != INVALID)
      {
         // NB So wind_dir = 0 (calm) also included here
         AWS_dd = Integer.toString(mywind.int_true_wind_dir);
      }
      else
      {
         AWS_dd = "";
      } 
   } // if (true_wind_dir_from_AWS_present == false)
   
   
   // wind speed [format WS.s; range 0..75; resolution 0.1; units m/s]
   //
   if (true_wind_speed_from_AWS_present == false)
   {
      if (mywind.int_true_wind_speed != INVALID) 
      {
         if (main.wind_units.trim().indexOf(main.M_S) != -1) // so wind speed in m/s
         {
            double_wind_speed = mywind.int_true_wind_speed * 1.0;                     // double_wind_speed: units m/s
         } 
         else // so wind speed units knots or wind speed units unknown
         {
            double_wind_speed = mywind.int_true_wind_speed * KNOT_M_S_CONVERSION;     // double_wind_speed: units m/s
         }

         // rounded one digit
         BigDecimal bd = new BigDecimal(double_wind_speed).setScale(1, RoundingMode.HALF_UP);  // one decimal, rounded e.g. 2.12939 -> 2.1
         double_wind_speed = bd.doubleValue();

         AWS_ff = Double.toString(double_wind_speed);
      } 
      else 
      {
         AWS_ff = "";
      }
   } // if (true_wind_speed_from_AWS_present == false)

   
   // air  temperature [format sTA.w; range -60..+60; resolution 0.1; unit C] 
   //
   if (air_temp_from_AWS_present == false)
   {
      if ((mytemp.air_temp.compareTo("") != 0) && (mytemp.air_temp != null))    
      {
        double_air_temp = Double.parseDouble(mytemp.air_temp);   
      
         BigDecimal bd = new BigDecimal(double_air_temp).setScale(1, RoundingMode.HALF_UP);  // one decimal, rounded e.g. 2.12939 -> 2.1
         double_air_temp = bd.doubleValue();
         
         AWS_TTT = Double.toString(double_air_temp);   
      }  
      else
      {
         AWS_TTT = "";
      }
   } // if (air_temp_from_AWS_present == false)
   
   
   // relative humidity [format UUU; range 0..100; resolution 1; unit %]
   //
   if (rh_from_AWS_present == false)
   {
      if ((mytemp.double_rv != main.INVALID))  
      {
         int int_rh = (int)Math.round(mytemp.double_rv * 100);    // rounding and in % (and eg not 100.0 but 100)
      
         if ((int_rh >= 0) && (int_rh <= 100))   
         { 
            AWS_rh = Integer.toString(int_rh);
         }
         else
         {
            AWS_rh = "";
         }
      } // if ((mytemp.double_rv != main.INVALID)) 
      else
      {
         AWS_rh = "";
      }
   } // if (rh_from_AWS_present == false)
   
   
   // sea water temperature [format sTW.w; range -5..45; resolution 0.1; unit C] 
   //
   if (SST_from_AWS_present == false)
   {
      if ((mytemp.sea_water_temp.compareTo("") != 0) && (mytemp.sea_water_temp != null))    
      {
         double_sst = Double.parseDouble(mytemp.sea_water_temp);   
      
         BigDecimal bd = new BigDecimal(double_sst).setScale(1, RoundingMode.HALF_UP);  // one decimal, rounded e.g. 2.12939 -> 2.1
         double_sst = bd.doubleValue();
         
         AWS_sst = Double.toString(double_sst);   
      }  
      else
      {
         AWS_sst = "";
      }
   } // if (SST_from_AWS_present == false)
   
   
   // visibility [format VV; range 0..99; resolution -; units: code]
   //
   if (myvisibility.VV_code.equals("//"))
   {
      AWS_VV = "";
   }   
   else if ((myvisibility.VV_code != null) && (myvisibility.VV_code.compareTo("") != 0))
   {
      AWS_VV = myvisibility.VV_code;
   }
   else
   {
      AWS_VV = "";  
   }
   
   
   // Present Weather [format WW; range 0..99; resolution -; units: bufr code table 020003]
   //
   if (mypresentweather.ww_code.equals("//"))
   {
     AWS_ww = ""; 
   }
   else if ((mypresentweather.ww_code != null) && (mypresentweather.ww_code.compareTo("") != 0))
   {
      AWS_ww = mypresentweather.ww_code;
   }
   else
   {
      AWS_ww = "";
   }
      
   
   // Past weather 1 (W1; bufr table 020004)
   //
   if (mypastweather.W1_code.equals("/"))
   {
      AWS_W1 = "";
   }
   else if ((mypastweather.W1_code != null) && (mypastweather.W1_code.compareTo("") != 0))
   {
      AWS_W1 = mypastweather.W1_code;
   }
   else
   {
      AWS_W1 = "";
   } 
     
      
   // past weather 2 (W2; bufr table 020004)
   //
   if (mypastweather.W2_code.equals("/"))
   {
      AWS_W2 = "";
   }
   else if ((mypastweather.W2_code != null) && (mypastweather.W2_code.compareTo("") != 0))
   {
      AWS_W2 = mypastweather.W2_code;
   }
   else
   {
      AWS_W2 = "";
   } 
    
   
   // total cloud cover (N)
   //
   if (mycloudcover.N_code.equals("/"))
   {
      AWS_N = "";
   }
   else if ((mycloudcover.N_code != null) && (mycloudcover.N_code.compareTo("") != 0))
   {
      AWS_N = mycloudcover.N_code;
   }
   else
   {
      AWS_N = ""; 
   }
   
   
   // Cloud amount Cl/Cm (Nh) [bufr table 020011]
   //
   if (mycloudcover.Nh_code.equals("/"))
   {
      AWS_Nh = "";
   }
   else if ((mycloudcover.Nh_code != null) && (mycloudcover.Nh_code.compareTo("") != 0))
   {
      AWS_Nh = mycloudcover.Nh_code;
   }
   else
   {
      AWS_Nh = ""; 
   }
         
   
   // clouds low (Cl) [bufr table 020012]
   //
   if (mycl.cl_code.equals("/"))
   {
      AWS_Cl = "";
   }
   else if ((mycl.cl_code != null) && (mycl.cl_code.compareTo("") != 0))
   {
      //AWS_Cl = mycl.cl_code;
      try
      {
         int hulp_cl = Integer.parseInt(mycl.cl_code) + 30;    // add 30, see bufr table 020012 
         AWS_Cl = Integer.toString(hulp_cl);
      }
      catch (NumberFormatException ex)
      {
         AWS_Cl = "";
         System.out.println("+++ Error compile obs for AWS; cloud type low (Cl) " + ex);
      } // catch      
   }   
   else
   {
      AWS_Cl = "";
   } 
      
      
   // clouds middle (Cm) [bufr table 020012]
   //
   if (mycm.cm_code.equals("/"))
   {
      AWS_Cm = "";
   }
   else if ((mycm.cm_code != null) && (mycm.cm_code.compareTo("") != 0))
   {
      //AWS_Cm = mycm.cm_code.substring(0, 1);// omdat bij cm_code in geval Cm7 een a, b, c er achter staat (dus 7a, 7b, 7c)
      try
      {
         int hulp_cm = Integer.parseInt(mycm.cm_code.substring(0, 1)) + 20;    // add 20, see bufr table 020012 
         AWS_Cm = Integer.toString(hulp_cm);
         // NB because if Cm code = Cm7 there is an addition a, b, c (so 7a, 7b, 7c)
      }
      catch (NumberFormatException ex)
      {
         AWS_Cm = "";
         System.out.println("+++ Error compile obs for AWS; cloud type middle (Cm) " + ex);
      } // catch    
   }    
   else
   {
      AWS_Cm = "";
   } 
      
      
   // clouds high (Ch) [bufr table 020012]
   //
   if (mych.ch_code.equals("/"))
   {
      AWS_Ch = "";
   }
   else if ((mych.ch_code != null) && (mych.ch_code.compareTo("") != 0))
   {
      //AWS_Ch = mych.ch_code;
      try
      {
         int hulp_ch = Integer.parseInt(mych.ch_code) + 10;    // add 10, see bufr table 020012 
         AWS_Ch = Integer.toString(hulp_ch);
      }
      catch (NumberFormatException ex)
      {
         AWS_Ch = "";
         System.out.println("+++ Error compile obs for AWS; cloud type high (Ch) " + ex);
      } // catch     
   }
   else
   {
      AWS_Ch = "";
   }   

   
   // height of base of lowest clouds (h)
   //
   if (mycloudcover.h_code.equals("/"))
   {
      AWS_h = "";
   }
   else if ((mycloudcover.h_code != null) && (mycloudcover.h_code.compareTo("") != 0))
   {
      AWS_h = mycloudcover.h_code;
   }
   else
   {
      AWS_h = "";
   } 
   
   
   // Pw (period wind waves)
   //
   if (mywaves.Pw_code.equals("//"))
   {
      AWS_Pw = "";
   }
   else if (mywaves.Pw_code.equals("99"))
   {
      AWS_Pw = "";                                 // EUCAWS (Bufr) cannot handle 99 so agreed this will become ""
   }
   else if ( (mywaves.Pw_code != null) && (mywaves.Pw_code.compareTo("") != 0) )
   {   
      // period < 10 sec than skip leading 0
      //if (Integer.parseInt(mywaves.Pw_code) >= 10)
      //{
      //   AWS_Pw = mywaves.Pw_code.substring(1, 1);
      //}
      //else
      //{
      //   AWS_Pw = mywaves.Pw_code;
      //}
      // skip if present the leading zero
      int int_Pw = Integer.parseInt(mywaves.Pw_code);
      AWS_Pw = Integer.toString(int_Pw);
   }
   else
   {
      AWS_Pw = "";
   } 
   
   
   // Hw (height of wind waves)
   //
   if (mywaves.Hw_code.equals("//"))
   {
      AWS_Hw = "";
   }
   else if (mywaves.Hw_code.equals("99"))
   {
      AWS_Hw = "";
   }
   else if ( (mywaves.Hw_code != null) && (mywaves.Hw_code.compareTo("") != 0) )
   {   
      //AWS_Hw = mywaves.Hw_code;
      
      double double_Hw = Double.parseDouble(mywaves.Hw_code) / 2;   // eg 03 in FM13 code -> 1.5 m
      
      BigDecimal bd = new BigDecimal(double_Hw).setScale(1, RoundingMode.HALF_UP);  // one decimals, rounded e.g. 0.50000 -> 0.5
      double_Hw = bd.doubleValue();
         
      AWS_Hw = Double.toString(double_Hw);   
   }
   else
   {
      AWS_Hw = "";
   } 
  
   
   // dw1 (direction of first swell)
   // 
   if (mywaves.Dw1_code.equals("//"))
   {
      AWS_Dw1 = "";
   }
   else if (mywaves.Dw1_code.equals("99"))
   {
      AWS_Dw1 = "";
   }
   else if ( (mywaves.Dw1_code != null) && (mywaves.Dw1_code.compareTo("") != 0) )
   {   
      AWS_Dw1 = mywaves.Dw1_code + "0";
   }
   else
   {
      AWS_Dw1 = "";
   } 
   
   
   // Pw1 (period of first swell)
   //
   if (mywaves.Pw1_code.equals("//"))
   {
      AWS_Pw1 = "";
   }
   else if (mywaves.Pw1_code.equals("99"))
   {
      AWS_Pw1 = "";
   }
   else if ( (mywaves.Pw1_code != null) && (mywaves.Pw1_code.compareTo("") != 0) )
   {   
      // period < 10 sec? than skip leading 0
      //if (Integer.parseInt(mywaves.Pw1_code) >= 10)
      //{
       //  AWS_Pw1 = mywaves.Pw1_code.substring(1, 1); 
      //}
      //else
      //{
      //   AWS_Pw1 = mywaves.Pw1_code;
      //}
      int int_Pw1 = Integer.parseInt(mywaves.Pw1_code);
      AWS_Pw1 = Integer.toString(int_Pw1);
   }
   else
   {
      AWS_Pw1 = "";
   } 
   
   
   // Hw1 (height of first swell)
   //
   if (mywaves.Hw1_code.equals("//"))
   {
      AWS_Hw1 = "";
   }
   else if (mywaves.Hw1_code.equals("99"))
   {
      AWS_Hw1 = "";
   }
   else if ( (mywaves.Hw1_code != null) && (mywaves.Hw1_code.compareTo("") != 0) )
   {   
      //AWS_Hw1 = mywaves.Hw1_code;
      
      double double_Hw1 = Double.parseDouble(mywaves.Hw1_code) / 2;   // eg 03 in FM13 code -> 1.5 m
      
      BigDecimal bd = new BigDecimal(double_Hw1).setScale(1, RoundingMode.HALF_UP);  // one decimals, rounded e.g. 0.50000 -> 0.5
      double_Hw1 = bd.doubleValue();
         
      AWS_Hw1 = Double.toString(double_Hw1);   
   }
   else
   {
      AWS_Hw1 = "";
   } 
   
   
   // Dw2 (direction of second swell)
   //
   if (mywaves.Dw2_code.equals("//"))
   {
      AWS_Dw2 = "";
   }
   else if (mywaves.Dw2_code.equals("99"))
   {
      AWS_Dw2 = "";
   }
   else if ( (mywaves.Dw2_code != null) && (mywaves.Dw2_code.compareTo("") != 0) )
   {   
      AWS_Dw2 = mywaves.Dw2_code + "0";
   }
   else
   {
      AWS_Dw2 = "";
   }    
   
   
   // Pw2 (period of second swell)
   // 
   if (mywaves.Pw2_code.equals("//"))
   {
      AWS_Pw2 = "";
   }
   else if (mywaves.Pw2_code.equals("99"))
   {
      AWS_Pw2 = "";
   }
   else if ( (mywaves.Pw2_code != null) && (mywaves.Pw2_code.compareTo("") != 0) )
   {   
      // period < 10 sec than skip leading 0
      //if (Integer.parseInt(mywaves.Pw2_code) >= 10)
      //{
      //  AWS_Pw2 = mywaves.Pw2_code.substring(1, 1); 
      //}
      //else
      //{
      //   AWS_Pw2 = mywaves.Pw2_code;
      //}
      int int_Pw2 = Integer.parseInt(mywaves.Pw2_code);
      AWS_Pw2 = Integer.toString(int_Pw2);
   }
   else
   {
      AWS_Pw2 = "";
   } 
   
   
   // Hw2 (height of second swell)
   //
   if (mywaves.Hw2_code.equals("//"))
   {
      AWS_Hw2 = "";
   }
   else if (mywaves.Hw2_code.equals("99"))
   {
      AWS_Hw2 = "";
   }
   else if ( (mywaves.Hw2_code != null) && (mywaves.Hw2_code.compareTo("") != 0) )
   {   
      double double_Hw2 = Double.parseDouble(mywaves.Hw2_code) / 2;   // eg 03 in FM13 code -> 1.5 m
      
      BigDecimal bd = new BigDecimal(double_Hw2).setScale(1, RoundingMode.HALF_UP);  // one decimals, rounded e.g. 0.50000 -> 0.5
      double_Hw2 = bd.doubleValue();
         
      AWS_Hw2 = Double.toString(double_Hw2);   
   }
   else
   {
      AWS_Hw2 = "";
   } 

   
   // ice deposit (thickness)
   //
   if (myicing.EsEs_code.equals("//"))
   {
      AWS_EsEs = "";
   }
   else if ( (myicing.EsEs_code != null) && (myicing.EsEs_code.compareTo("") != 0) )
   {
      double double_EsEs = Double.parseDouble(myicing.EsEs_code) / 100;   // eg 04 in FM13 code (4 cm) -> 0.04 m
      
      BigDecimal bd = new BigDecimal(double_EsEs).setScale(2, RoundingMode.HALF_UP);  // two decimals, rounded e.g. 0.12939 -> 0.13
      double_EsEs = bd.doubleValue();
         
      AWS_EsEs = Double.toString(double_EsEs);   
   }
   else
   {
      AWS_EsEs = "";
   }
   
   
   // rate of ice accretion (Rs) [bufr table 020032]
   //
   if (myicing.Rs_code.equals("/"))
   {
      AWS_Rs = "";
   }
   else if ( (myicing.Rs_code != null) && (myicing.Rs_code.compareTo("") != 0) )
   {
      AWS_Rs = myicing.Rs_code;
   }  
   else
   {
      AWS_Rs = "";
   }   
   
   
   // cause of ice accretion (Is) [bufr table 020033]
   //
   if (myicing.Is_code.equals("/"))
   {
      AWS_Is = "";
   }
   else if ( (myicing.Is_code != null) && (myicing.Is_code.compareTo("") != 0) )
   {
      if (myicing.Is_code.equals("1"))               // icing from spray (FM13 code)
      {
         AWS_Is = "8";                               // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
      }
      else if (myicing.Is_code.equals("2"))          // icing from fog (FM13 code)
      {
         AWS_Is = "4";                               // BUFR table 020033 equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
      }
      else if (myicing.Is_code.equals("3"))          // icing from spray and fog (FM13 code)
      {
         AWS_Is = "12";                              // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
      }
       else if (myicing.Is_code.equals("4"))         // icing from rain (FM13 code)
      {
         AWS_Is = "2";                               // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
      }
        else if (myicing.Is_code.equals("5"))        // icing from spray and rain (FM13 code)
      {
         AWS_Is = "10";                              // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
      }
      else if (myicing.Is_code.equals("6"))          // icing from fog and rain (not present in FM13 code)
      {
         AWS_Is = "6";                               // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
      }
      else if (myicing.Is_code.equals("14"))         // icing from spray and fog and rain (not present in FM13 code)
      {
         AWS_Is = "14";                              // BUFR table 020033-equivalent (see "EUCAWS inputs/outputs complementary information about codes", Pierre Blouch)
      }
      else
      {
         AWS_Is = "";
      }
   }  
   else
   {
      AWS_Is = "";
   }      
   
   
   // sea ice concentration (ci) [bufr table 020034]
   //
   if (myice1.ci_code.equals("/"))
   {
      AWS_ci = "";
   }
   else if (myice1.ci_code.equals("u"))                       // internal code used by TurboWin+ ("unable to report because of ......")
   {
      AWS_ci = "14";
   }
   else if ( (myice1.ci_code != null) && (myice1.ci_code.compareTo("") != 0) )
   {
      AWS_ci = myice1.ci_code;
   }
   else
   {
      AWS_ci = "";
   }
   
   
   // amount and type of ice (bi) [bufr table 020035]
   //
   if (myice1.bi_code.equals("/"))
   {
      AWS_bi = "";
   }
   else if (myice1.bi_code.equals("u"))                       // internal code used by TurboWin+ ("unable to report because of ......")
   {
      AWS_bi = "14";
   }
   else if ( (myice1.bi_code != null) && (myice1.bi_code.compareTo("") != 0) )
   {
      AWS_bi = myice1.bi_code;
   }
   else
   {
      AWS_bi = "";
   }   
   
   
   // ice situation (zi) [bufr table 020036]
   //
   if (myice1.zi_code.equals("/"))
   {
      AWS_zi = "";
   }
   else if (myice1.zi_code.equals("u"))                       // internal code used by TurboWin+ ("unable to report because of ......")
   {
      AWS_zi = "30";
   }
   else if ( (myice1.zi_code != null) && (myice1.zi_code.compareTo("") != 0) )
   {
      AWS_zi = myice1.zi_code;
   }
   else
   {
      AWS_zi = "";
   }   
   
   
   // ice development (Si) [bufr table 020037]
   //
   if (myice1.Si_code.equals("/"))
   {
      AWS_Si = "";
   }
   else if (myice1.Si_code.equals("u"))                       // internal code used by TurboWin+ ("unable to report because of ......")
   {
      AWS_Si = "30";
   }
   else if ( (myice1.Si_code != null) && (myice1.Si_code.compareTo("") != 0) )
   {
      AWS_Si = myice1.Si_code;
   }
   else
   {
      AWS_Si = "";
   }   
   
   
   // bearing of ice edge (Di) [bufr id 020038 NO TABLE]
   //
   if (myice1.Di_code.equals("/"))
   {
      AWS_Di = "";
   }
   else if (myice1.Di_code.equals("u"))                       // internal code used by TurboWin+ ("unable to report because of ......")
   {
      AWS_Di = "";                                            // there is no code table, only direction, no support for "unable to report...etc"
   }
   else if ( (myice1.Di_code != null) && (myice1.Di_code.compareTo("") != 0) )
   {
      if (myice1.Di_code.equals("0"))               // ship in shore or flaw lead (FM13 code)
      {
         AWS_Di = "";                                // see EUCAWS inputs/outputs Complementary information about codes", Pierre Blouch)
      }
      else if (myice1.Di_code.equals("1")) 
      {
         AWS_Di = "45";
      }
      else if (myice1.Di_code.equals("2")) 
      {
         AWS_Di = "90";
      }
      else if (myice1.Di_code.equals("3")) 
      {
         AWS_Di = "135";
      }
      else if (myice1.Di_code.equals("4")) 
      {
         AWS_Di = "180";
      }
      else if (myice1.Di_code.equals("5")) 
      {
         AWS_Di = "225";
      }
      else if (myice1.Di_code.equals("6")) 
      {
         AWS_Di = "270";
      }
      else if (myice1.Di_code.equals("7")) 
      {
         AWS_Di = "315";
      }
      else if (myice1.Di_code.equals("8")) 
      {
         AWS_Di = "360";
      }
      else
      {
         AWS_Di = "";
      }
   }
   else
   {
      AWS_Di = "";
   }   
   
   
   // compose AWS string
   //
   AWS_obs = AWS_id + "," +                   
             AWS_diff_SLL_WL + "," +                
             AWS_dd + "," +                  
             AWS_ff + "," +    
             AWS_TTT + "," + 
             AWS_rh + "," + 
             AWS_sst + "," +                       
             AWS_VV + "," +                
             AWS_ww + "," +                        
             AWS_W1 + "," +                       
             AWS_W2 + "," +                        
             AWS_N + "," +                         
             AWS_Nh + "," +                        
             AWS_Cl + "," +                        
             AWS_Cm + "," +                        
             AWS_Ch + "," +                            
             AWS_h + "," +                         
             AWS_Pw + "," +                        
             AWS_Hw + "," +                        
             AWS_Dw1 + "," +                       
             AWS_Pw1 + "," +                       
             AWS_Hw1 + "," +                       
             AWS_Dw2 + "," +                       
             AWS_Pw2 + "," +                       
             AWS_Hw2 + "," +                       
             AWS_EsEs + "," +                      
             AWS_Rs + "," +                        
             AWS_Is + "," +                        
             AWS_ci + "," +                        
             AWS_bi + "," +                        
             AWS_zi + "," +                        
             AWS_Si + "," +                        
             AWS_Di;                       
   
   
        
  // voor testen
  //JOptionPane.showMessageDialog(null, AWS_obs  , APPLICATION_NAME + " AWS_obs", JOptionPane.INFORMATION_MESSAGE);

   
   
   return AWS_obs;
}   
   


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void zip_log_files()
{
   File file_logs_dir = new File(temp_logs_dir /*+ java.io.File.separator*/);
   String[] filenames = file_logs_dir.list(); // Returns an array of strings naming the files and directories in the directory denoted by this abstract pathname.

   for (int i = 0; i < filenames.length; i++)
   {
      filenames[i] = temp_logs_dir + java.io.File.separator + filenames[i];
   }

   // Create a buffer for reading the files
   byte[] buf = new byte[1024];

   try
   {
      // Create the ZIP file
      //String outFilename = "C:/Users/Martin/Downloads/logs/temp/logs.zip";
      //String outFilename = "C:/Users/Martin/Documents/logs.zip";
      String outFilename = temp_logs_dir + java.io.File.separator + ship_name + " " + LOGS_ZIP; // e.g. "C:/Users/Martin/Downloads/logs/temp/happy sailor logs.zip";
      ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

      // Compress the files
      for (int i = 0; i < filenames.length; i++)
      {
         //JOptionPane.showMessageDialog(null, filenames[i] , APPLICATION_NAME + " test", JOptionPane.WARNING_MESSAGE);
         File te_zippen_file = new File(filenames[i]);
         if (te_zippen_file.isDirectory() == false)     // ABSOLUUT GEEN DIRECTORIES, WANT DAN INVALID ARCHIEF
         {
            FileInputStream in = new FileInputStream(filenames[i]);

            // Add ZIP entry to output stream.
            try
            {
               //out.putNextEntry(new ZipEntry(filenames[i]));
               //out.putNextEntry(new ZipEntry(Path.GetFileName(filenames[i]))); // Path.GetFileName -> to prevent full paths are included
               out.putNextEntry(new ZipEntry(te_zippen_file.getName())); // getName -> to prevent full paths are included


               ////out.setLevel(Deflater.DEFAULT_COMPRESSION);
            }
            catch (ZipException ex)
            {
              JOptionPane.showMessageDialog(null, "zip error (Maintenance_Move_log_files_by_email_actionPerformed)"  , APPLICATION_NAME + " error", JOptionPane.ERROR_MESSAGE);
            }


            // Transfer bytes from the input file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0)
            {
               out.write(buf, 0, len);
            }

            // Complete the entry
            out.closeEntry();
            in.close();

         } //if (test.isDirectory() == false)
      } // for (int i = 0; i < filenames.length; i++)

      // Complete the ZIP file
      out.close();

   } // try
   catch (IOException e)
   {
      JOptionPane.showMessageDialog(null, "i/o zip error (Maintenance_Move_log_files_by_email_actionPerformed)"  , APPLICATION_NAME + " error", JOptionPane.ERROR_MESSAGE);
   }

}


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static OSType detect_OS()
{
   // source: http://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
   OSType detectedOS;
   
   
   String OS_property_name = System.getProperty("os.name", "generic").toLowerCase();
   //String OS_property_arch = System.getProperty("os.arch", "generic").toLowerCase();  // zal bij RaspBerry en Linux veelal ARM geven (kan niet gebruikt worden om te onderscheiden)
      
   if ((OS_property_name.indexOf("mac") >= 0) || (OS_property_name.indexOf("darwin") >= 0)) 
   {
      detectedOS = OSType.MACOS;
   } 
   else if (OS_property_name.indexOf("win") >= 0) 
   {
      detectedOS = OSType.WINDOWS;
   } 
   else if (OS_property_name.indexOf("nux") >= 0) 
   {
      // NB could also be on a RaspberryPi (unfortunately not possible to distinguish)
      detectedOS = OSType.LINUX;
   } 
   else 
   {
      detectedOS = OSType.OTHER;
   }
     
   
   return detectedOS;
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void initComponents2()
{
   /* determine the OS this program is running on */
/*   
   OSType ostype = detect_OS();
   switch (ostype)
   {
      case WINDOWS: APPLICATION_NAME = "TurboWin+"; 
                    break;
      case MACOS:   APPLICATION_NAME = "TurboWin+";//"TurboMac+"; 
                    break;
      case LINUX:   APPLICATION_NAME = "TurboWin+";//"TurboPi"; 
                    break;
      case OTHER:   APPLICATION_NAME = "TurboWin+"; 
                    break;
      default:      APPLICATION_NAME = "TurboWin+"; 
                    break;
   }
*/
   
   /* title of main screen */
   setTitle(APPLICATION_NAME);                   // fixed
   
   /* fixed text bottom screen (e.g. Turboin+ stand-alone mode...) */
   jLabel4.setText(APPLICATION_NAME);
   
   /* set main application icon (top-left in title bar) */
   setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(main.ICONS_DIRECTORY + "tray.png")));

   /* status field (NB can be over written with different Themes) */
   jTextField4.setBackground(new java.awt.Color(204, 255, 255));   // Cyan 
   
   /* create pop-up menu (right mouse button) */
   popup_input = new JPopupMenu();
      
   JMenuItem menuItem1 = new JMenuItem("Date & Time...");
   menuItem1.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_DateTime_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem1);    
      
   JMenuItem menuItem2 = new JMenuItem("Position, Course & Speed...");
   menuItem2.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Position_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem2);    
   
   JMenuItem menuItem3 = new JMenuItem("Barometer reading...");
   menuItem3.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Barometer_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem3);    
   
   JMenuItem menuItem4 = new JMenuItem("Barograph reading...");
   menuItem4.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Barograph_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem4);    
   
   JMenuItem menuItem5 = new JMenuItem("Temperatures...");
   menuItem5.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Temperatures_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem5);    
   
   JMenuItem menuItem6 = new JMenuItem("Wind...");
   menuItem6.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Wind_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem6);    
   
   JMenuItem menuItem7 = new JMenuItem("Waves...");
   menuItem7.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_waves_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem7);    
   
   JMenuItem menuItem8 = new JMenuItem("Visibility...");
   menuItem8.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Visibility_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem8); 

   JMenuItem menuItem9 = new JMenuItem("Present weather...");
   menuItem9.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Presentweather_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem9);   
   
   JMenuItem menuItem10 = new JMenuItem("Past weather...");
   menuItem10.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Pastweather_menu_actionperformed(null);
      }
   });
   popup_input.add(menuItem10);    
   
   JMenuItem menuItem11 = new JMenuItem("Clouds low...");
   menuItem11.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Cloudslow_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem11);    
   
   JMenuItem menuItem12 = new JMenuItem("Clouds middle...");
   menuItem12.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Cloudsmiddle_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem12);    
   
   JMenuItem menuItem13 = new JMenuItem("Clouds high...");
   menuItem13.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Cloudshigh_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem13);    
   
   JMenuItem menuItem14 = new JMenuItem("Cloud cover & height...");
   menuItem14.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Cloudcover_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem14);    
   
   JMenuItem menuItem15 = new JMenuItem("Icing...");
   menuItem15.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Icing_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem15);    
   
   JMenuItem menuItem16 = new JMenuItem("Ice...");
   menuItem16.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Ice_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem16);    
   
   JMenuItem menuItem17 = new JMenuItem("Observer...");
   menuItem17.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Input_Observer_menu_actionPerformed(null);
      }
   });
   popup_input.add(menuItem17);  
   
   popup_input.addSeparator();
   
   JMenuItem menuItem18 = new JMenuItem("Day colours");
   menuItem18.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Themes_1_actionPerformed(null);
      }
   });
   popup_input.add(menuItem18);  
   
   JMenuItem menuItem19 = new JMenuItem("Night colours");
   menuItem19.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Themes_2_actionPerformed(null);
      }
   });
   popup_input.add(menuItem19);  
   
   JMenuItem menuItem20 = new JMenuItem("Sunrise colours");
   menuItem20.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Themes_3_actionPerformed(null);
      }
   });
   popup_input.add(menuItem20);  
   
   JMenuItem menuItem21 = new JMenuItem("Sunset colours");
   menuItem21.addActionListener(new java.awt.event.ActionListener() 
   {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         Themes_4_actionPerformed(null);
      }
   });
   popup_input.add(menuItem21);  
   
   MouseListener popupListener_input = new PopupListener_input();
   addMouseListener(popupListener_input);                              // connect to jFrame otherwise eg: jTextField1.addMouseListener(popupListener);
   jToolBar1.addMouseListener(popupListener_input);                    // also connected to Toolbar now
     
   
   /* user directory */
   user_dir = System.getProperty("user.dir");
   //log_turbowin_system_message("[GENERAL] user dir:" + user_dir);
   //JOptionPane.showMessageDialog(null, user_dir, "user_dir", JOptionPane.INFORMATION_MESSAGE);
   System.out.println("user dir = " + user_dir);  

   // for turbowin system logs
   sdf_tsl_1 = new SimpleDateFormat("MMM_yyyy");                                // e.g. JAN_2016 (part of the file name)
   sdf_tsl_1.setTimeZone(TimeZone.getTimeZone("UTC"));
   
   sdf_tsl_2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");                    // e.g. 09-Jan-2016 12:23:33 (time stamp of the recoreded messages)
   sdf_tsl_2.setTimeZone(TimeZone.getTimeZone("UTC"));
   
   
   // initialisation
   Reset_all_meteo_parameters();
    
   /* check if the application is online or offline (different help file handling)  */
   //
   // NB werken via bs = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
   //               en hierna if (bs.isOffline() == true) gaat niet goed
   // deze methode kan niet goed online of offline bepalen (staat ook in API documentatie)
   // API: "The return value is does not have to be guaranteed to be reliable, as it is sometimes difficult to ascertain the true online / offline state of a client system."
   //

   // initialisation
   offline_mode = false;
   offline_mode_via_jnlp = false;
   offline_mode_via_cmd = false;
   
   // turbowin jnlp offline file present? (turbowin_jws_offline.jnlp)
   String volledig_path_jnlp_offline_file = user_dir + java.io.File.separator + JNLP_OFFLINE_FILE;
   File jnlp_offline_file = new File(volledig_path_jnlp_offline_file);
   if (jnlp_offline_file.exists())
   {
      // So file turbowin_jws_offline.jnlp exists (TurboWeb online version: than this file wil not be present) 
      offline_mode = true;
      offline_mode_via_jnlp = true;
   }
   
   // turbowin cmd file present? (turbowin_plus_offline.cmd)
   String volledig_path_cmd_offline_file = user_dir + java.io.File.separator + CMD_OFFLINE_FILE;
   File cmd_offline_file = new File(volledig_path_cmd_offline_file);
   if (cmd_offline_file.exists())
   {
      // So file turbowin_plus_offline.cmd exists (TurboWeb online version: than this file wil not be present) 
      offline_mode = true;
      offline_mode_via_cmd = true;
   }
   
   
   /* always for offline mode !!! fixed sub dir logs and sub dir amver(not user configurable) */
   if (offline_mode == true)
   {
      // logs sub dir
      //
      // NB logs dir fixed for offline mode (sub dir of main dir -main dir is the dir where jar file is located-)
      logs_dir = user_dir + java.io.File.separator + OFFLINE_LOGS_DIR;
      //JOptionPane.showMessageDialog(null, logs_dir, main.APPLICATION_NAME + " logs_dir test", JOptionPane.WARNING_MESSAGE);

      /* check sub dir logs already present, if not -> create */
      final File dirs = new File(logs_dir);
      if (dirs.exists() == false)
      {
         final boolean success = dirs.mkdirs();
         if (success == false)
         {
            JOptionPane.showMessageDialog(null, "Could not create " + logs_dir + ", disk write protected or no permission to write", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         }
      } // if (dirs.exists() == false)
      
      // if not done before, create sub-sub dir "turbowin_system" (logs\turbowin_system) (NB in case of online(web) mode this will be done in OK_button_actionPerformed() [mylogfiles.java])
      //
      if (dirs.isDirectory()) 
      {
         // create sub dir TURBOWIN_SYSTEM_LOGS (turbowin_system)
         String turbowin_system_logs_dir = main.logs_dir + java.io.File.separator + main.TURBOWIN_SYSTEM_LOGS_DIR;
         final File dir_turbowin_system_logs = new File(turbowin_system_logs_dir);   
         if (dir_turbowin_system_logs.exists() == false)
         {
            dir_turbowin_system_logs.mkdir();   
            log_turbowin_system_message("[GENERAL] created dir " + turbowin_system_logs_dir);
         }
      } //  if (dirs.isDirectory()) 
      
      
      // amver sub dir
      //
      // NB amver dir fixed for offline mode (sub dir of main dir -main dir is the dir where jar file is located-)
      String amver_dir = user_dir + java.io.File.separator + OFFLINE_AMVER_DIR;

      /* check sub dir amver already present, if not -> create */
      final File dirs_amver = new File(amver_dir);
      if (dirs_amver.exists() == false)
      {
         final boolean success = dirs_amver.mkdirs();
         if (success == false)
         {
            JOptionPane.showMessageDialog(null, "Could not create " + amver_dir + ", disk write protected or no permission to write", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         }
      } // if (dirs.exists() == false)

   } // if (offline_mode == true)
   
   
   if (offline_mode == true)
   {
      /* So file turbowin_jws_offline.jnlp and/or turbowin_plus_offline.cmd exists (TurboWeb online version: than these files wil not be present) */
      //offline_mode = true;

      /* gray (disable) the "output -> obs to server (internet)" menu selection option */   // see below now it is also an option for offline mode
      //jMenuItem20.setEnabled(false);

      /* gray (disable) the "Info -> Statistics(internet)" menu selection option */  
      //jMenuItem36.setEnabled(false);
      
      /* set label on bottom main screen */
      application_mode =  "stand-alone mode";
      jLabel4.setText(APPLICATION_NAME + " " + application_mode); // NB can later in this start up process be overwritten if a barometer or AWS is coupled

      /* NB logs dir fixed for offline mode (sub dir of main dir -main dir is the dir where the jar file is located-) */
      /* see function: meta_data_from_configuration_regels_into_global_vars() */
   } 
   else
   {
      /* set label on bottom main screen */
      application_mode =  "web mode";
      jLabel4.setText(APPLICATION_NAME + " " + application_mode);
   }
   
   // Obs to server
   if ((obs_format.equals(FORMAT_FM13)) && (offline_mode == true))
   {
      // NB FM13 only "obs to server" in online mode, in case of format 101 "obs to server" is an output option in both modes (online/web and offline)
      // gray (disable) the "output -> obs to server (internet)" menu selection option
      jMenuItem20.setEnabled(false);
   }
   
   // initialisation
   mode_grafiek = MODE_PRESSURE;
   
   // initialisation graph form (for barometer or AWS connected)
   graph_form = null;
   
   // all specific RS232 and RS422 functions
   RS232_RS422 = new main_RS232_RS422();
   
   /* read stored meta (station) data from muffins or from configuration files */
   if (offline_mode_via_cmd == true) // offline mode
   {
      lees_configuratie_regels();          
   }
   else // so offline_via_jnlp mode or online (webstart) mode
   {
      // only jnlp mode can use the single instance running check 
      //
      // in offline mode: So by removing the file turbowin_plus_offline.cmd and invoking turbowin+ via turbowin_jws_offline.jnlp there will be only a single instance running check
      // online mode: is always started via the jnlp file -> always single instance running check
      //
      //
      
      // check only one instance running 
      try 
      { 
         sis = (SingleInstanceService)ServiceManager.lookup("javax.jnlp.SingleInstanceService");
      } 
      catch (UnavailableServiceException e) { sis = null; } 
      
      // Register the single instance listener at the start of the application
      if (sis != null)
      {
         sisL = new SISListener();
         sis.addSingleInstanceListener(sisL);       
      }

      // read stored station data
      //
      read_muffin();
   }   
   
   
   // get the systemTrays instance
   if (!SystemTray.isSupported()) 
   {
      log_turbowin_system_message("[GENERAL] SystemTray is not supported");
   }
   else
   {   
      tray = SystemTray.getSystemTray();
   }   
   

   
}        



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
//private void IMMT_log()
public static void IMMT_log()        
{
   final String SPATIE_1 = " ";
   final String SPATIE_2 = "  ";
   final String SPATIE_3 = "   ";
   final String SPATIE_4 = "    ";
   final String SPATIE_5 = "     ";
   final String SPATIE_6 = "      ";
   final String SPATIE_7 = "       ";


	String immt_rec = "";                                   
	String Qc_1  = "9";                                      // 9 = the value of the element is missing
	String Qc_2  = "9";
	String Qc_3  = "9";
	String Qc_4  = "9";
	String Qc_5  = "9";
	String Qc_6  = "9";
	String Qc_7  = "9";
	String Qc_8  = "9";
	String Qc_9  = "9";
	String Qc_10 = "9";
	String Qc_11 = "9";
	String Qc_12 = "9";
	String Qc_13 = "9";
	String Qc_14 = "9";
	String Qc_15 = "9";
	String Qc_16 = "9";
	String Qc_17 = "9";
	String Qc_18 = "9";
	String Qc_19 = "9";
	String Qc_20 = "9";
	String Qc_21 = SPATIE_1;                                 // this one always blank (1 space) (MQCS version) MQCS version numbers not suitable for checking done by this program
	String Qc_22 = "9";                                      // only for vosclim -> this program always vosclim
	String Qc_23 = "9";                                      // only for vosclim -> this program always vosclim
	String Qc_24 = "9";                                      // only for vosclim -> this program always vosclim
	String Qc_25 = "9";                                      // only for vosclim -> this program always vosclim
	//String Qc_26 = "9";                                      // only for vosclim -> this program always vosclim
	String Qc_27 = "9";                                      // only for vosclim -> this program always vosclim
	String Qc_28 = "9";                                      // only for vosclim -> this program always vosclim
	String Qc_29 = "9";                                      // only for vosclim -> this program always vosclim

   boolean HDG_ok     = false;
   boolean sl_code_ok = false;


	//
	// immt record velden vullen (volgens WMO version IMMT-5)
	//
	immt_rec = "3";                                      // char number 1 (3=temp. in tenths of degrees C)

	immt_rec += mydatetime.year;                         // char number 2-5
	immt_rec += mydatetime.MM_code;                      // char number 6-7
	immt_rec += mydatetime.YY_code;                      // char number 8-9
	immt_rec += mydatetime.GG_code;                      // char number 10-11

   immt_rec += myposition.Qc_code;                      // char number 12

	immt_rec += myposition.lalala_code;                  // char number 13-15
	immt_rec += myposition.lolololo_code;                // char number 16-19
	Qc_20 = "1";

   immt_rec += "0";                                     // char number 20


   try
   {
      int num_h = Integer.valueOf(mycloudcover.h_code);
      if (num_h >= 0 && num_h <= 9)
      {
		   immt_rec += mycloudcover.h_code;               // char number 21
		   Qc_1 = "1";
      }
	   else
	   {
         immt_rec += SPATIE_1;                          // char number 21
	   }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 21 
   } // catch        
   
   
   try
   {
      int num_VV = Integer.valueOf(myvisibility.VV_code);
      if (num_VV >= 90 && num_VV <= 99)
      {
		   immt_rec += myvisibility.VV_code;              // char number 22-23
		   Qc_2 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 22-23
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 22-23
   } // catch


   try
   {
      int num_N = Integer.valueOf(mycloudcover.N_code);
      if (num_N >= 0 && num_N <= 9)
      {
         immt_rec += mycloudcover.N_code;               // char number 24
         Qc_3 = "1";
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 24
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 24
   } // catch


   try
   {
      int num_dd = Integer.valueOf(mywind.dd_code);
      if ((num_dd >= 0 && num_dd <= 36) || (num_dd == 99))
      {
		   immt_rec += mywind.dd_code;                    // char number 25-26
		   Qc_4 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 25-26
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 25-26
   } // catch


   try
   {
      int num_iw = Integer.valueOf(mywind.iw_code);
      if (num_iw == 0 || num_iw == 1 || num_iw == 3 || num_iw == 4)
      {
         immt_rec += mywind.iw_code;                    // char number 27
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 27
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 27
   } // catch


   try
   {
      // max ff 99 units for IMMT 
      int num_ff = Integer.valueOf(mywind.ff_code);
      if (num_ff >= 0 && num_ff <= 99)
      {
		   immt_rec += mywind.ff_code;                    // char number 28-29
		   Qc_5 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 28-29
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 28-29
   } // catch


   try
   {
      int num_sn_TTT = Integer.valueOf(mytemp.sn_TTT_code);
      if (num_sn_TTT >= 0 && num_sn_TTT <= 1)
      {
         immt_rec += mytemp.sn_TTT_code;                // char number 30
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 30
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 30
   } // catch


   try
   {
      int num_TTT = Integer.valueOf(mytemp.TTT_code);
      if (num_TTT >= 0 && num_TTT <= 999)
      {
		   immt_rec += mytemp.TTT_code;                    // char number 31-33
		   Qc_6 = "1";
      }
      else
      {
         immt_rec += SPATIE_3;                           // char number 31-33
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_3;                              // char number 31-33
   } // catch



   try
   {
   	// sign of dewpoint must be converted to 'computed immt code'
   	// also Sn_TdTdTd = 7(ice) is possible contrary to the operational branch

      int num_sn_TdTdTd = Integer.valueOf(mytemp.sn_TdTdTd_code);
      int num_sn_TbTbTb = Integer.valueOf(mytemp.sn_TbTbTb_code);

      if (num_sn_TdTdTd == 0)
      {
         immt_rec += "5";                               // char number 34
      }
      else if (num_sn_TdTdTd == 1 && num_sn_TbTbTb != 2 && num_sn_TbTbTb != 7)
      {
         immt_rec += "6";                               // char number 34
      }
      else if (num_sn_TdTdTd == 1 && (num_sn_TbTbTb == 2 || num_sn_TbTbTb == 7))
      {
         immt_rec += "7";                               // char number 34
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 34
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 34
   } // catch



   try
   {
      int num_TdTdTd = Integer.valueOf(mytemp.TdTdTd_code);
      if (num_TdTdTd >= 0 && num_TdTdTd <= 999)
      {
		   immt_rec += mytemp.TdTdTd_code;                // char number 35-37
		   Qc_7 = "1";
      }
      else
      {
         immt_rec += SPATIE_3;                          // char number 35-37
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_3;                             // char number 35-37
   } // catch


   try
   {
      int num_PPPP = Integer.valueOf(mybarometer.PPPP_code);
      if (num_PPPP >= 0 && num_PPPP <= 9999)
      {
		   immt_rec += mybarometer.PPPP_code;             // char number 38-41
		   Qc_8 = "1";
      }
      else
      {
         immt_rec += SPATIE_4;                          // char number 38-41
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_4;                             // char number 38-41
   } // catch


   try
   {
      int num_ww = Integer.valueOf(mypresentweather.ww_code);
      if (num_ww >= 0 && num_ww <= 99)
      {
		   immt_rec += mypresentweather.ww_code;          // char number 42-43
		   Qc_9 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 42-43
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 42-43
   } // catch

        

   try
   {
      int num_W1 = Integer.valueOf(mypastweather.W1_code);
      if (num_W1 >= 0 && num_W1 <= 9)
      {
         immt_rec += mypastweather.W1_code;             // char number 44
         Qc_9 = "1";
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 44
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 44
   } // catch



   try
   {
      int num_W2 = Integer.valueOf(mypastweather.W2_code);
      if (num_W2 >= 0 && num_W2 <= 9)
      {
         immt_rec += mypastweather.W2_code;             // char number 45
         Qc_9 = "1";
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 45
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 45
   } // catch


   try
   {
      int num_Nh = Integer.valueOf(mycloudcover.Nh_code);
      if (num_Nh >= 0 && num_Nh <= 9)
      {
         immt_rec += mycloudcover.Nh_code;             // char number 46
         Qc_3 = "1";
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 46
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 46
   } // catch


   try
   {
      int num_cl = Integer.valueOf(mycl.cl_code);
      if (num_cl >= 0 && num_cl <= 9)
      {
         immt_rec += mycl.cl_code;                     // char number 47
         Qc_3 = "1";
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 47
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 47
   } // catch


   try
   {
      String hulp_cm_code = "";

      if (mycm.cm_code.length() > 1)
      {
         hulp_cm_code = mycm.cm_code.substring(0, 1);    // NB .substring(0, 1) --> because Cm_code in case of Cm7 an a, b, c could be sticked to the 7 (7a, 7b, 7c)
      }
      else
      {
         hulp_cm_code = mycm.cm_code;
      }
      
      int num_cm = Integer.valueOf(hulp_cm_code);           
      if (num_cm >= 0 && num_cm <= 9)
      {
         immt_rec += hulp_cm_code;                      // char number 48
         Qc_3 = "1";
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 48
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 48
   } // catch



   try
   {
      int num_ch = Integer.valueOf(mych.ch_code);
      if (num_ch >= 0 && num_ch <= 9)
      {
         immt_rec += mych.ch_code;                     // char number 49
         Qc_3 = "1";
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 49
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 49
   } // catch



   try
   {
      int num_ss_TsTsTs = Integer.valueOf(mytemp.ss_TsTsTs_code);
      if (num_ss_TsTsTs >= 0 && num_ss_TsTsTs <= 7)
      {
         if (num_ss_TsTsTs == 0 || num_ss_TsTsTs == 2 || num_ss_TsTsTs == 4 || num_ss_TsTsTs == 6)
         {
            immt_rec += "0";                            // char number 50
         }
         else if (num_ss_TsTsTs == 1 || num_ss_TsTsTs == 3 || num_ss_TsTsTs == 5  || num_ss_TsTsTs == 7)
         {
            immt_rec += "1";                            // char number 50
         }
         else
         {
            immt_rec += SPATIE_1;                       // char number 50
         }
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 50
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 50
   } // catch



   try
   {
      int num_TsTsTs = Integer.valueOf(mytemp.TsTsTs_code);
      if (num_TsTsTs >= 0 && num_TsTsTs <= 999)
      {
		   immt_rec += mytemp.TsTsTs_code;                // char number 51-53
		   Qc_10 = "1";
      }
      else
      {
         immt_rec += SPATIE_3;                          // char number 51-53
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_3;                             // char number 51-53
   } // catch


   try
   {
      int num_immt_sst_indicator = Integer.valueOf(mytemp.immt_sst_indicator);
      if (num_immt_sst_indicator >= 0 && num_immt_sst_indicator <= 7)
      {
         immt_rec += mytemp.immt_sst_indicator;         // char number 54
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 54
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 54
   } // catch


   if (method_waves.equals(SEA_AND_SWELL_ESTIMATED) == true)
   {
      immt_rec += "0";                                  // char number 55
   }
   else if (method_waves.equals(WAVES_MEASURED_SHIPBORNE) == true)
   {
      immt_rec += "1";                                  // char number 55
   }
   else if (method_waves.equals(WAVES_MEASURED_BUOY) == true)
   {
      immt_rec += "4";                                  // char number 55
   }
   else if (method_waves.equals(WAVES_MEASURED_OTHER) == true)
   {
      immt_rec += "7";                                   // char number 55
   }
   else
   {
      // arbitrary, it could be " " also, but I choose "0" (estimated)
      immt_rec += "0";                                    // char number 55
   }



   try
   {
      int num_Pw = Integer.valueOf(mywaves.Pw_code);
      if (num_Pw >= 0 && num_Pw <= 99)
      {
		   immt_rec += mywaves.Pw_code;                  // char number 56-57
		   Qc_11 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 56-57
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 56-57
   } // catch


   try
   {
      int num_Hw = Integer.valueOf(mywaves.Hw_code);
      if (num_Hw >= 0 && num_Hw <= 99)
      {
		   immt_rec += mywaves.Hw_code;                  // char number 58-59
		   Qc_12 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 58-59
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 58-59
   } // catch



   try
   {
      int num_Dw1 = Integer.valueOf(mywaves.Dw1_code);
      if (num_Dw1 >= 0 && num_Dw1 <= 99)
      {
		   immt_rec += mywaves.Dw1_code;                  // char number 60-61
		   Qc_13 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 60-61
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 60-61
   } // catch


   try
   {
      int num_Pw1 = Integer.valueOf(mywaves.Pw1_code);
      if (num_Pw1 >= 0 && num_Pw1 <= 99)
      {
		   immt_rec += mywaves.Pw1_code;                  // char number 62-63
		   Qc_13 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 62-63
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 62-63
   } // catch


   try
   {
      int num_Hw1 = Integer.valueOf(mywaves.Hw1_code);
      if (num_Hw1 >= 0 && num_Hw1 <= 99)
      {
		   immt_rec += mywaves.Hw1_code;                  // char number 64-65
		   Qc_13 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 64-65
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 64-65
   } // catch


   if (main.obs_format.equals(main.FORMAT_FM13))
   {
      try
      {
         int num_Is = Integer.valueOf(myicing.Is_code);
         if (num_Is >= 1 && num_Is <= 5)
         {
            immt_rec += myicing.Is_code;                // char number 66
         }
         else
         {
            immt_rec += SPATIE_1;                       // char number 66
         }
      }
      catch (NumberFormatException e)
      {
         immt_rec += SPATIE_1;                          // char number 66
      }
   } // if (main.obs_format.equals(main.FORMAT_FM13))
   else // (eg format 101)
   {
      // NB format 101 is not according WMO code table 1751 as required for Is
      immt_rec += SPATIE_1;                             // char number 66
   } // else

   
   try
   {
      int num_EsEs = Integer.valueOf(myicing.EsEs_code);
      if (num_EsEs >= 0 && num_EsEs <= 99)
      {
         immt_rec += myicing.EsEs_code;                 // char number 67-68
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 67-68
      }
   }
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 67-68
   }

   
   try
   {
      int num_Rs = Integer.valueOf(myicing.Rs_code);
      if (num_Rs >= 0 && num_Rs <= 4)
      {
         immt_rec += myicing.Rs_code;                   // char number 69
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 69
      }
   }
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 69
   }

   immt_rec += "4";                                     // char number 70      // source of obs [4=electronic logbook]

   immt_rec += "4";                                     // char number 71      // obs platform [4=VOSClim]


	int lengte = call_sign.trim().toUpperCase().length();

   if (lengte == 7)
   {
      immt_rec += call_sign;                             // char number 72-78
   }
   else if (lengte == 6)
   {
      immt_rec += call_sign;                             // char number 72-77
      immt_rec += SPATIE_1;                              // char number 78
   }
   else if (lengte == 5)
   {
      immt_rec += call_sign;                             // char number 72-76
      immt_rec += SPATIE_2;                              // char number 77-78
   }
   else if (lengte == 4)
   {
      immt_rec += call_sign;                             // char number 72-75
      immt_rec += SPATIE_3;                              // char number 76-78
   }
   else if (lengte == 3)
   {
      immt_rec += call_sign;                             // char number 72-74
      immt_rec += SPATIE_4;                              // char number 75-78
   }
   else // only call sign length of 3,4,5,6,7 char allowed (see IMMT description)
   {
     // note: in TurboWin "unknown"
     immt_rec += SPATIE_7;                               // char number 72-78
   }

   if (recruiting_country.length() > 2)
   {
      immt_rec += recruiting_country.substring(recruiting_country.length() - 2);  // char number 79 - 80 (e.g. Netherlands NL)
   }
   else
   {
      immt_rec += SPATIE_2;                              // char number 79 - 80
   }

   immt_rec += SPATIE_1;                                 // char number 81

   immt_rec += "3";                                      // char number 82     NB 3 = automated QC only(inc time-seq checks)

   immt_rec += "1";                                      // char number 83    // 1 = weather indicator: manual

   immt_rec += "4";                                      // char number 84    //ir

   immt_rec += SPATIE_3;                                 // char number 85-87 // RRR

   immt_rec += SPATIE_1;                                 // char number 88    // tr

   try
   {
      int num_sn_TbTbTb_code = Integer.valueOf(mytemp.sn_TbTbTb_code);
      if (num_sn_TbTbTb_code >= 0 && num_sn_TbTbTb_code <= 2)
      {
		   immt_rec += mytemp.sn_TbTbTb_code;              // char number 89
      }
      else
      {
         immt_rec += SPATIE_1;                           // char number 89
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                              // char number 89
   } // catch


   try
   {
      int num_TbTbTb_code = Integer.valueOf(mytemp.TbTbTb_code);
      if (num_TbTbTb_code >= 0 && num_TbTbTb_code <= 999)
      {
		   immt_rec += mytemp.TbTbTb_code;                 // char number 90-92
         Qc_19 = "1";
      }
      else
      {
         immt_rec += SPATIE_3;                           // char number 90-92
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_3;                              // char number 90-92
   } // catch


   try
   {
      int num_a_code = Integer.valueOf(mybarograph.a_code);
      if (num_a_code >= 0 && num_a_code <= 8)
      {
		   immt_rec += mybarograph.a_code;                 // char number 93
         Qc_15 = "1";
      }
      else
      {
         immt_rec += SPATIE_1;                           // char number 93
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                              // char number 93
   } // catch


   try
   {
      int num_ppp_code = Integer.valueOf(mybarograph.ppp_code);
      if (num_ppp_code >= 0 && num_ppp_code <= 999)
      {
		   immt_rec += mybarograph.ppp_code;              // char number 94-96
         Qc_16 = "1";
      }
      else
      {
         immt_rec += SPATIE_3;                           // char number 94-96
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_3;                              // char number 94-96
   } // catch


   try
   {
      int num_Ds_code = Integer.valueOf(myposition.Ds_code);
      if (num_Ds_code >= 0 && num_Ds_code <= 9)
      {
		   immt_rec += myposition.Ds_code;                // char number 97
         Qc_17 = "1";
      }
      else
      {
         immt_rec += SPATIE_1;                           // char number 97
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                              // char number 97
   } // catch


   try
   {
      int num_vs_code = Integer.valueOf(myposition.vs_code);
      if (num_vs_code >= 0 && num_vs_code <= 9)
      {
		   immt_rec += myposition.vs_code;                // char number 98
         Qc_18 = "1";
      }
      else
      {
         immt_rec += SPATIE_1;                           // char number 98
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                              // char number 98
   } // catch

   try
   {
      int num_Dw2 = Integer.valueOf(mywaves.Dw2_code);
      if (num_Dw2 >= 0 && num_Dw2 <= 99)
      {
		   immt_rec += mywaves.Dw2_code;                  // char number 99-100
		   Qc_13 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 99-100
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 99-100
   } // catch


   try
   {
      int num_Pw2 = Integer.valueOf(mywaves.Pw2_code);
      if (num_Pw2 >= 0 && num_Pw2 <= 99)
      {
		   immt_rec += mywaves.Pw2_code;                  // char number 101-102
		   Qc_13 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 101-102
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 101-102
   } // catch

   
   try
   {
      int num_Hw2 = Integer.valueOf(mywaves.Hw2_code);
      if (num_Hw2 >= 0 && num_Hw2 <= 99)
      {
		   immt_rec += mywaves.Hw2_code;                  // char number 103-104
		   Qc_13 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 103-104
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 103-104
   } // catch

   
   try
   {
      int num_ci = Integer.valueOf(myice1.ci_code);
      if (num_ci >= 0 && num_ci <= 9)
      {
         immt_rec += myice1.ci_code;                    // char number 105
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 105
      }
   }
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 105
   }

   
   try
   {
      int num_Si = Integer.valueOf(myice1.Si_code);
      if (num_Si >= 0 && num_Si <= 9)
      {
         immt_rec += myice1.Si_code;                    // char number 106
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 106
      }
   }
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 106
   }

   
   try
   {
      int num_bi = Integer.valueOf(myice1.bi_code);
      if (num_bi >= 0 && num_bi <= 9)
      {
         immt_rec += myice1.bi_code;                    // char number 107
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 107
      }
   }
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 107
   }

   
   try
   {
      int num_Di = Integer.valueOf(myice1.Di_code);
      if (num_Di >= 0 && num_Di <= 9)
      {
         immt_rec += myice1.Di_code;                    // char number 108
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 108
      }
   }
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 108
   }

   
   try
   {
      int num_zi = Integer.valueOf(myice1.zi_code);
      if (num_zi >= 0 && num_zi <= 9)
      {
         immt_rec += myice1.zi_code;                    // char number 109
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 109
      }
   }
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 109
   }

   immt_rec += "A";                                     // char number 110 (FM-13 code version)

   immt_rec += "5";                                     // char number 111 (IMMT version)

	immt_rec += Qc_1;                                    // char number 112
	immt_rec += Qc_2;                                    // char number 113
	immt_rec += Qc_3;                                    // char number 114
	immt_rec += Qc_4;                                    // char number 115
	immt_rec += Qc_5;                                    // char number 116
	immt_rec += Qc_6;                                    // char number 117
	immt_rec += Qc_7;                                    // char number 118
	immt_rec += Qc_8;                                    // char number 119
	immt_rec += Qc_9;                                    // char number 120
	immt_rec += Qc_10;                                   // char number 121
	immt_rec += Qc_11;                                   // char number 122
	immt_rec += Qc_12;                                   // char number 123
	immt_rec += Qc_13;                                   // char number 124
	immt_rec += Qc_14;                                   // char number 125
	immt_rec += Qc_15;                                   // char number 126
	immt_rec += Qc_16;                                   // char number 127
	immt_rec += Qc_17;                                   // char number 128
	immt_rec += Qc_18;                                   // char number 129
	immt_rec += Qc_19;                                   // char number 130
	immt_rec += Qc_20;                                   // char number 131
   immt_rec += Qc_21;                                   // char number 132  (MQCS)


   try
   {
      int num_HDG = Integer.valueOf(mywind.HDG_code);
      if (num_HDG >= 1 && num_HDG <= 360)
      {
		   immt_rec += mywind.HDG_code;                   // char number 133-135
		   Qc_22 = "1";
         HDG_ok = true;
      }
      else
      {
         // heading (HDG) is blank if heading was the same as COG (see wind input screen)
         HDG_ok = false;
      }
   } // try
   catch (NumberFormatException e)
   {
      HDG_ok = false;
   } // catch



   // So HDG (heading) blank -> use COG
   if (HDG_ok == false)
   {
      try
      {
         int num_COG = Integer.valueOf(mywind.COG_code);   // use COG for HDG (see wind input screen) will be inserted into IMMT on the position of HDG
         if (num_COG >= 1 && num_COG <= 360)
         {
		      immt_rec += mywind.COG_code;                   // char number 133-135
		      Qc_22 = "1";
         }
         else
         {
            immt_rec += SPATIE_3;                          // char number 133-135
         }
      } // try
      catch (NumberFormatException e)
      {
         immt_rec += SPATIE_3;                             // char number 133-135
      } // catch
   } // if (HDG_ok == false)



   try
   {
      int num_COG = Integer.valueOf(mywind.COG_code);
      if (num_COG >= 0 && num_COG <= 360)
      {
		   immt_rec += mywind.COG_code;                   // char number 136-138
		   Qc_23 = "1";
      }
      else
      {
         immt_rec += SPATIE_3;                          // char number 136-138
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_3;                             // char number 136-138
   } // catch


   try
   {
      int num_SOG = Integer.valueOf(mywind.SOG_code);
      if (num_SOG >= 0 && num_SOG <= 99)
      {
		   immt_rec += mywind.SOG_code;                   // char number 139-140
		   Qc_24 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 139-140
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 139-140
   } // catch


   try
   {
      int num_SLL = Integer.valueOf(mywind.SLL_code);
      if (num_SLL >= 0 && num_SLL <= 99)
      {
		   immt_rec += mywind.SLL_code;                   // char number 141-142
		   Qc_25 = "1";
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 141-142
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 141-142
   } // catch


   try
   {
      int num_sl = Integer.valueOf(mywind.sl_code);
      if (num_sl >= 0 && num_sl <= 1)
      {
		   immt_rec += mywind.sl_code;                   // char number 143
         sl_code_ok = true;
      }
      else
      {
         immt_rec += SPATIE_1;                          // char number 143
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_1;                             // char number 143
   } // catch


   try
   {
      int num_hh = Integer.valueOf(mywind.hh_code);
      if (num_hh >= 0 && num_hh <= 99)
      {
		   immt_rec += mywind.hh_code;                   // char number 144-145

         // Qc27 serves as the indicator for both sl and hh (from IMMT-4)
         if (sl_code_ok == true)
         {
            // So both, sl and hh, are now ok
            Qc_27 = "1";
         }
      }
      else
      {
         immt_rec += SPATIE_2;                          // char number 144-145
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_2;                             // char number 144-145
   } // catch


   try
   {
      int num_RWD = Integer.valueOf(mywind.RWD_code);
      if (num_RWD >= 0 && num_RWD <= 360)
      {
		   immt_rec += mywind.RWD_code;                   // char number 146-148
		   Qc_28 = "1";
      }
      else
      {
         immt_rec += SPATIE_3;                          // char number 146-148
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_3;                             // char number 146-148
   } // catch


   try
   {
      int num_RWS = Integer.valueOf(mywind.RWS_code);
      if (num_RWS >= 0 && num_RWS <= 999)
      {
		   immt_rec += mywind.RWS_code;                   // char number 149-151
		   Qc_29 = "1";
      }
      else
      {
         immt_rec += SPATIE_3;                          // char number 149-151
      }
   } // try
   catch (NumberFormatException e)
   {
      immt_rec += SPATIE_3;                             // char number 149-151
   } // catch

   immt_rec += Qc_22;                                   // char number 152
   immt_rec += Qc_23;                                   // char number 153
   immt_rec += Qc_24;                                   // char number 154
   immt_rec += Qc_25;                                   // char number 155
   immt_rec += SPATIE_1;                                // char number 156      // NB former Qc_26;
   immt_rec += Qc_27;                                   // char number 157
   immt_rec += Qc_28;                                   // char number 158
   immt_rec += Qc_29;                                   // char number 159

   immt_rec += SPATIE_4;                                // char number 160-163  // relative humidity
   immt_rec += SPATIE_1;                                // char number 164      // relative humidity indicator
   
   //immt_rec += SPATIE_1;                                // char number 165
   immt_rec += "0";                                     // char number 165      // 0 = no AWS
   
   int lengte_imo = imo_number.trim().length();

   if (lengte_imo == 7)
   {
      immt_rec += imo_number;                           // char number 166-172
   }
   else if (lengte_imo == 6)
   {
      immt_rec += imo_number;                           // char number 166-171
      immt_rec += SPATIE_1;                             // char number 172
   }
   else if (lengte_imo == 5)
   {
      immt_rec += imo_number;                           // char number 166-170
      immt_rec += SPATIE_2;                             // char number 171-172
   }
   else if (lengte_imo == 4)
   {
      immt_rec += imo_number;                           // char number 166-169
      immt_rec += SPATIE_3;                             // char number 170-172
   }
   else if (lengte_imo == 3)
   {
      immt_rec += imo_number;                           // char number 166-168
      immt_rec += SPATIE_4;                             // char number 169-172
   }
   else if (lengte_imo == 2)
   {
      immt_rec += imo_number;                           // char number 166-167
      immt_rec += SPATIE_5;                             // char number 168-172
   }
   else if (lengte_imo == 1)
   {
      immt_rec += imo_number;                           // char number 166
      immt_rec += SPATIE_6;                             // char number 167-172
   }
   else // 
   {
     immt_rec += SPATIE_7;                              // char number 166-172
   }


   /* BEGIN: THE FOLLOWING SECTION IS NOT PART OF THE OFFICIAL IMMT FORMAT */
   immt_rec += SPATIE_1;                                // char number 173


   if (myobserver.selected_observer != null && myobserver.selected_observer.compareTo("") != 0)
   {
      immt_rec += myobserver.selected_observer;         // char number 174 - ?
   }

   /* END: THE FOLLOWING SECTION IS NOT PART OF THE OFFICIAL IMMT FORMAT */


   schrijven_IMMT_log(immt_rec);

}



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   //private void schrijven_IMMT_log(final String immt_rec)
   private static void schrijven_IMMT_log(final String immt_rec)        
   {
      /* NB input/output in a GUI always via a SwingWorker (Core Java Volume 1 bld 795 e.v.; Volume 2 bld 37, 215) */

      boolean doorgaan = true;


      // first test logs dir was defined
      if (main.logs_dir.equals("") == true)
      {
         JOptionPane.showMessageDialog(null, "logs folder unknown, please select: Maintenance -> Log files settings", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
      }


      if (doorgaan == true)
      {
         new SwingWorker<Void, Void>()
         {
            @Override
            protected Void doInBackground() throws Exception
            {
               String datum_tijd_positie_last_record = "";
               String datum_tijd_positie_huidige_record = "";

               /* preventing observation will be stored twice or more times */
               if (last_record.length() >= 19)
               {
                  datum_tijd_positie_last_record = last_record.substring(1, 19);
               }

               if (immt_rec.length() >= 19)
               {
                  datum_tijd_positie_huidige_record = immt_rec.substring(1, 19);
               }

               if (datum_tijd_positie_last_record.compareTo(datum_tijd_positie_huidige_record) != 0)
               {
                  String volledig_path = main.logs_dir + java.io.File.separator + IMMT_LOG;

                  try
                  {
                     BufferedWriter out = new BufferedWriter(new FileWriter(volledig_path, true));// true means append the specified data to the file i.e. the pre-exist data in a file is not overwritten and the new data is appended after the pre-exist data.

                     out.write(immt_rec);
                     out.newLine();   // newLine(): write a line separator. The line separator string is defined by the system property line.separator, and is not necessarily a single newline ('\n') character.
           
                     out.close();

                  } // try
                  catch (Exception e)
                  {
                     JOptionPane.showMessageDialog(null, "unable to write to: " + volledig_path, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                  } // catch
               }


               return null;

            } // protected Void doInBackground() throws Exception
         }.execute(); // new SwingWorker<Void, Void>()
      } // if (doorgaan == true)
   }




   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public static void internet_mouseClicked(final String help_dir) {
      // TODO add your handling code here:

      new SwingWorker<Void, Void>()
      {
         @Override
         protected Void doInBackground() throws Exception
         {
             Desktop desktop = null;

            // Before more Desktop API is used, first check
            // whether the API is supported by this particular
            // virtual machine (VM) on this particular host.
            if (Desktop.isDesktopSupported())
            {
               desktop = Desktop.getDesktop();
               URI uri = null;
               try
               {
                  if (offline_mode == false)
                  {
                     String http_adres = main.URL_INTERNET_HELP + help_dir; // help_dir set in e.g. ch1_image_mouseClicked()
                     uri = new URI(http_adres);
                     desktop.browse(uri);
                  }
                  else if (offline_mode == true)
                  {
                     String help_file_path = user_dir + java.io.File.separator + OFFLINE_HELP_DIR + java.io.File.separator + help_dir; // nb help_dir is pecifiek per parameter bv wind, waves etc.
                     desktop.open(new File(help_file_path));
                  }
               } // try
               catch (IOException | URISyntaxException ioe) { }
            } // if (Desktop.isDesktopSupported())
            else // Destop method not supported trying alternatives
            {
               // Desktop method failed
               // now trying to open with system-specific commands
               //
               // KDE:     kde-open
               // GNOME:   gnome-open
               // Any X-server system: xdg-open
               // MAC:     open
               // Windows: explorer               
               //
               // e.g. see: http://stackoverflow.com/questions/18004150/desktop-api-is-not-supported-on-the-current-platform
               
               URI uri = null;
               String te_open_help_file = null;
               Runtime runtime;
                  
               if (offline_mode == false)
               {
                  String http_adres = main.URL_INTERNET_HELP + help_dir; 
                  uri = new URI(http_adres);
                  te_open_help_file = uri.toString();
                  // NB Maybe uri.toString() is maybe(?)not necassary in case "xdg-open" this should be tested (see also manual on "xdg-open")
               }
               else if (offline_mode == true)
               {
                  String help_file_path = user_dir + java.io.File.separator + OFFLINE_HELP_DIR + java.io.File.separator + help_dir; // nb help_dir is pecifiek per parameter bv wind, waves etc.
                  te_open_help_file = help_file_path;
               }
                  
               runtime = Runtime.getRuntime();
                  
               // Microsoft Windows
               // --- NB Desktop method will always succeed
               
               // Linux (Gnome)
               // --- NB Desktop method will succeed (Desktop is based on Gnome) so not necessary to try a customised open command ---
                  
               // Linux (KDE)
               try 
               {
                  runtime.exec("kde-open " + te_open_help_file);
               } 
               catch (IOException e) 
               {
                  // Linux (RaspBerry) [14-11-2014: tested on stand-alone RaspBerry succesfully]
                  try 
                  {
                     runtime.exec("xdg-open " + te_open_help_file);
                  } 
                  catch (IOException e2) 
                  {
                     // Mac
                     try 
                     {
                        runtime.exec("open " + te_open_help_file);
                     } 
                     catch (IOException e3) 
                     {
                        JOptionPane.showMessageDialog(null, "Error invoking default web browser (-Desktop-method not supported on this computer system and Runtime alternatives failed)" , main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                     } // catch                 
                  } // catch                 
               } // catch                
            } // else (// Destop method not supported)

            return null;

         } // protected Void doInBackground() throws Exception
      }.execute(); // new SwingWorker<Void, Void>()
   }




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void check_immt_size()
{
   // TODO add your handling code here:

   new SwingWorker<Boolean, Void>()
   {
      @Override
      protected Boolean doInBackground() throws Exception
      {
         boolean immt_size_limit_exceeded = false;

         /* first check if there is an immt log source file present (and not empty) */
         String volledig_path_immt = logs_dir + java.io.File.separator + IMMT_LOG;
         File immt_file = new File(volledig_path_immt);
         if (immt_file.exists() && immt_file.length() > IMMT_LIMIT)     // length() in bytes
         {
            immt_size_limit_exceeded = true;
         }

         return immt_size_limit_exceeded;
      }
      @Override
      protected void done()
      {
         try
         {
            boolean immt_size_limit_exceeded = get();   // retrieve return value from doInBackground()
            if (immt_size_limit_exceeded)
            {
               String info = "immt log (file with all stored observations for research and climatological use) exceeds ";
               info += IMMT_LIMIT / 1024;
               info += " Kb.\nPlease select one of the coming days: Maintenance -> Move log files ";
               JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " info", JOptionPane.INFORMATION_MESSAGE);

            } // if (immt_size_limit_exceeded)

         } // try
         catch (InterruptedException | ExecutionException ex) { }

      } // protected void done()

   }.execute();
}




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private boolean position_sequence_check()
{
   // initialisation
   boolean time_sequence_checks_ok  = true;
   boolean doorgaan                 = true;
   boolean string_num_converions_ok = true;
   int num_vorige_obs_jaar          = 0;
   int num_vorige_obs_maand         = 0;
   int num_vorige_obs_dag           = 0;
   int num_vorige_obs_uur           = 0;
   int num_quadrant                 = 0;
   float num_latitude               = 0;
   float num_longitude              = 0;
   
      
   //
   ////////////////// first determine the substrings and convert them to numerical values               
   //
   if (last_record.length() >= 19)
   {
      /* string date time from previous obs (last stored record) */
      String jaar      = last_record.substring(1, 5);              // cpp: jaar  = last_record.substr(1, 4);
      String maand     = last_record.substring(5, 7);              // cpp: maand = last_record.substr(5, 2);
      String dag       = last_record.substring(7, 9);              // cpp: dag   = last_record.substr(7, 2);
      String uur       = last_record.substring(9, 11);             // cpp: uur   = last_record.substr(9, 2);

      /* obs code position from previous obs (last stored record) */
      String quadrant  = last_record.substring(11, 12);            // cpp: quadrant  = last_record.substr(11, 1);                       // WMO code table 3333
      String latitude  = last_record.substring(12, 15);            // cpp: latitude  = last_record.substr(12, 3);                       // tenths of degrees
      String longitude = last_record.substring(15, 19);            // cpp: longitude = last_record.substr(15, 4);                       // tenths of degrees

      /* date/time of last stored record convertion to numerical values */
      try
      {
         num_vorige_obs_jaar  = Integer.valueOf(jaar.trim());  // cpp: num_vorige_obs_jaar = atoi(jaar.c_str());
      }
      catch (NumberFormatException ex)
      {
         System.out.println("+++ Error function: position_sequence_check(). " + ex.toString());
         string_num_converions_ok = false;
      }
      try
      {  
         num_vorige_obs_maand = Integer.valueOf(maand.trim()); // cpp: num_vorige_obs_maand = atoi(maand.c_str());
      }
      catch (NumberFormatException ex)
      {
         System.out.println("+++ Error function: position_sequence_check(). " + ex.toString());
         string_num_converions_ok = false;
      }      
      try
      {   
         num_vorige_obs_dag   = Integer.valueOf(dag.trim());   // cpp: num_vorige_obs_dag   = atoi(dag.c_str());
      }
      catch (NumberFormatException ex)
      {
         System.out.println("+++ Error function: position_sequence_check(). " + ex.toString());
         string_num_converions_ok = false;
      }  
      try
      {
         num_vorige_obs_uur   = Integer.valueOf(uur.trim());   // cpp: num_vorige_obs_uur   = atoi(uur.c_str());
      }
      catch (NumberFormatException ex)
      {
         System.out.println("+++ Error function: position_sequence_check(). " + ex.toString());
         string_num_converions_ok = false;
      }     
      
      /* positie uit laatste record omzetten naar num_waarden + afronden + "+/-" (afh. quadrant) maken */
      try
      {
         num_quadrant = Integer.valueOf(quadrant.trim());    // cpp: num_quadrant  = atoi(quadrant.c_str());
      }
      catch (NumberFormatException ex)
      {
         System.out.println("+++ Error function: position_sequence_check(). " + ex.toString());
         string_num_converions_ok = false;
      }        
      try
      {
         num_latitude = Float.valueOf(latitude.trim());      // cpp: num_latitude  = atoi(latitude.c_str());
      }
      catch (NumberFormatException ex)
      {
         System.out.println("+++ Error function: position_sequence_check(). " + ex.toString());
         string_num_converions_ok = false;
      }        
      try
      {
         num_longitude = Float.valueOf(longitude.trim());     // cpp: num_longitude = atoi(longitude.c_str());
      }
      catch (NumberFormatException ex)
      {
         System.out.println("+++ Error function: position_sequence_check(). " + ex.toString());
         string_num_converions_ok = false;
      }  
   } // if (last_record.length() >= 19) 
   else // record too short for obtaining substrings
   {
      string_num_converions_ok = false;
   }
      
   
   //
   //////////////// conversion from substring to ints or floats successful
   //
   if (string_num_converions_ok == true)   
   {   
      float num_vorige_obs_breedte = num_latitude / 10;           // nu in graden en tienden
      float num_vorige_obs_lengte  = num_longitude / 10;          // nu in graden en tienden

      if (num_quadrant == 3 || num_quadrant == 5)                 // Zuiderbreedte
      {
         num_vorige_obs_breedte *= -1;
      }
      if (num_quadrant == 5 || num_quadrant == 7)                 // Westerlengte
      {
         num_vorige_obs_lengte *= -1;
      }


      /* 
      // compare date/time with the date/time of the previous obs
      */
      
      System.out.println("--- comparing date/time of entered obs with date/time of last saved obs");

      /* date-time previous saved obs */
      Calendar calendar_vorige_obs = new GregorianCalendar(num_vorige_obs_jaar, num_vorige_obs_maand -1, num_vorige_obs_dag, num_vorige_obs_uur, 0);// Month value is 0-based. e.g., 0 for January.

      /* date-time present obs */
      int num_huidige_obs_jaar = Integer.valueOf(mydatetime.year.trim());
      int num_huidige_obs_maand = Integer.valueOf(mydatetime.MM_code.trim());// NB do not use mydatetime.month because = February etc.
      int num_huidige_obs_dag = Integer.valueOf(mydatetime.day.trim());
      int num_huidige_obs_uur = Integer.valueOf(mydatetime.hour.trim());

      Calendar calendar_huidige_obs = new GregorianCalendar(num_huidige_obs_jaar, num_huidige_obs_maand - 1, num_huidige_obs_dag, num_huidige_obs_uur, 0);// Month value is 0-based. e.g., 0 for January.


      if (calendar_huidige_obs.compareTo(calendar_vorige_obs) <= 0)
      {
         SimpleDateFormat sdf2;
         sdf2 = new SimpleDateFormat("MMMM dd, yyyy HH");                            // e.g "MMMM dd, yyyy" -> februari 27, 2010

         String string_calendar_huidige_obs = sdf2.format(calendar_huidige_obs.getTime());

         String info = "";
         info = "-time sequence check-\n";
         info += "obs date/time";
         info += " (";
         info += string_calendar_huidige_obs;
         info += ".00 UTC";
         info += ")";

         if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + " please confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
         {
            //MessageBox("Please correct the error (no final obs was coded)", "TurboWin message", MB_OK);
            JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME + " warning", JOptionPane.WARNING_MESSAGE);
            doorgaan = false;
            time_sequence_checks_ok = false;
         }
      } // if (calendar_huidige_obs.compareTo(calendar_vorige_obs) < 0)


      /* present obs position compared to the position of the previous obs */
      if (doorgaan)
      {
         System.out.println("--- comparing position of entered obs with position of last saved obs");
         
         /* first determine the time diff between present and previous obs */
         long obs_verschil_uur = (calendar_huidige_obs.getTimeInMillis() - calendar_vorige_obs.getTimeInMillis()) / 3600000;    // 3600000 = 1000 * 60 * 60 = 1 uur

         if (obs_verschil_uur >= 0)                   // alleen verdere checks als huidige obs datum/tijd is later dan vorige obs datum/tijd
         {
            /* NB er wordt als grens genomen dat er max 30 mijl per uur afgelegd kan zijn */
            /* NB for testing see e.g.: http://williams.best.vwh.net/gccalc.htm */
            int afstand_vorige_huidige_obs = bepaal_afstand_huidige_obs_pos_tot_vorige_obs_pos(num_vorige_obs_breedte, num_vorige_obs_lengte);

            // JOptionPane.showMessageDialog(null, afstand_vorige_huidige_obs,  main.APPLICATION_NAME + " afstand tot vorige obs", JOptionPane.WARNING_MESSAGE);

            if (((obs_verschil_uur >= 0 && obs_verschil_uur <= 6) && (afstand_vorige_huidige_obs > 180)) ||
                    ((obs_verschil_uur > 6 && obs_verschil_uur <= 12) && (afstand_vorige_huidige_obs > 360)) ||
                    ((obs_verschil_uur > 12 && obs_verschil_uur <= 18) && (afstand_vorige_huidige_obs > 540)) ||
                    ((obs_verschil_uur > 18 && obs_verschil_uur <= 24) && (afstand_vorige_huidige_obs > 720)))
            {
      		   String info = "";
               info = "-position sequence check-\n";
               info += "obs position:\n";

               info += myposition.latitude_degrees;
               info += "° ";
               info += myposition.latitude_minutes;
               info += "' ";
               info += myposition.latitude_hemisphere;
               info += "  ";
               info += myposition.longitude_degrees;
               info += "° ";
               info += myposition.longitude_minutes;
               info += "' ";
               info += myposition.longitude_hemisphere;
               info += "\n";

               if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + " please confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
               {
                  JOptionPane.showMessageDialog(null, "Please correct the obs position (no final obs was coded)", main.APPLICATION_NAME + " message", JOptionPane.WARNING_MESSAGE);
                  doorgaan = false;
                  time_sequence_checks_ok = false;
		         }
            } // if ( ((obs_verschil_uur > 0 && obs_verschil <= 6) etc.
         } // if (obs_verschil_uur > 0)
      } // if (doorgaan)
   } // if (string_num_converions_ok == true)


   return time_sequence_checks_ok;
}




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private int bepaal_afstand_huidige_obs_pos_tot_vorige_obs_pos(double num_vorige_obs_breedte, double num_vorige_obs_lengte)
{
	/* used formula :                                                  */
	/* cos_AB = sin_bA * sin_bB + cos_bA * cos_bB * cos_delta_l_AB     */
	/*                                                                 */
	/* distance = 60 arccos(cos_AB)                                    */
	/*                                                                 */
	/* from degrees angle to radians angle : graden * 60 * boogminuut  */


   // constants
   final int westgrens_POR     = 90;
   final int oostgrens_POR     = -90;
   final double boogminuut     = 0.0002908882; 
   final double h180pi         = 3437.746771;  

   // var's
	int afstand = Integer.MAX_VALUE;
	double delta_l_ab;
	double cos_delta_l_ab;
	double sin_breedte_a;
	double sin_breedte_b;
	double cos_breedte_a;
	double cos_breedte_b;
	double num_huidige_obs_breedte;
	double num_huidige_obs_lengte;
   double acos_argument;
	boolean huidige_obs_in_por;



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

   /* due to 180 degrees passage in the POR */
       //if ((num_huidige_obs_lengte >= westgrens_POR) && (num_huidige_obs_lengte <= oostgrens_POR)) // komt hier nooit in ????
   
       //if ( (num_huidige_obs_lengte >= westgrens_POR && num_huidige_obs_lengte <= 180) ||      // westgrens_por +90
       //     (num_huidige_obs_lengte >= -180 && num_huidige_obs_lengte <= oostgrens_POR) )      // oostgrens_por -90
   huidige_obs_in_por = (num_huidige_obs_lengte >= -180 && num_huidige_obs_lengte <= oostgrens_POR) || (num_huidige_obs_lengte >= westgrens_POR && num_huidige_obs_lengte <= 180); // westgrens_por +90 // oostgrens_por -90

	if (huidige_obs_in_por == true)
	{
		if (num_huidige_obs_lengte < 0) num_huidige_obs_lengte += 360;
		if (num_vorige_obs_lengte < 0) num_vorige_obs_lengte += 360;
	} // if (huidige_obs_in_por == true)


	/* determine longitude difference */
	delta_l_ab = num_huidige_obs_lengte - num_vorige_obs_lengte;


	/* longitude difference > 180: do not compute but give MAX_VALUE (> 180 gives issues in formula) */
	if (Math.abs(delta_l_ab) > 180)
   {
      afstand = Integer.MAX_VALUE;
   }
	else // longitude difference < 180
	{
		/* the (greatcircle) computation */
		sin_breedte_a  = Math.sin(num_vorige_obs_breedte  * 60 * boogminuut);
		sin_breedte_b  = Math.sin(num_huidige_obs_breedte * 60 * boogminuut);
		cos_breedte_a  = Math.cos(num_vorige_obs_breedte  * 60 * boogminuut);
		cos_breedte_b  = Math.cos(num_huidige_obs_breedte * 60 * boogminuut);
		cos_delta_l_ab = Math.cos(delta_l_ab * 60 * boogminuut);

      /* first test acos argument to prevent ACOS domain error (if 2x exact the same position + obs to screen)*/
      acos_argument = sin_breedte_a * sin_breedte_b + cos_breedte_a * cos_breedte_b * cos_delta_l_ab;
      if (acos_argument <= -1 || acos_argument >= 1)
      {
         afstand = 0;
      }
      else
      {
      	afstand = (int)Math.round(h180pi *  Math.acos(acos_argument)); //Returns the closest int to the argument (zelde als (int)Math.floor(a + 0.5f)
      }
	} // else (longitude difference < 180)


	return afstand;
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Output_obs_to_file_FM13()
{
   //JOptionPane.showMessageDialog(null, "test output obs to file", main.APPLICATION_NAME + " info", JOptionPane.INFORMATION_MESSAGE);
   
   
   // pop-up the file chooser dialog box
   JFileChooser chooser = new JFileChooser();
   int result = chooser.showSaveDialog(main.this);
   if (result == JFileChooser.APPROVE_OPTION)
   {
      output_file = chooser.getSelectedFile().getPath();

      new SwingWorker<Void, Void>()
      {
         @Override
         protected Void doInBackground() throws Exception
         {
            // write to selected file
            try
            {
               BufferedWriter out = new BufferedWriter(new FileWriter(output_file));

               out.write(obs_write);
               //out.newLine();   // newLine(): write a line separator. The line separator string is defined by the system property line.separator, and is not necessarily a single newline ('\n') character.
               out.close();

               // user feedback
               String info = "obs written to: " + output_file;
               JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " info", JOptionPane.INFORMATION_MESSAGE);

            } // try
            catch (IOException | HeadlessException e)
            {
               JOptionPane.showMessageDialog(null, "unable to write to: " + output_file, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            } // catch

            return null;

         } // protected Void doInBackground() throws Exception

         @Override
         protected void done()
         {
            IMMT_log();
            
            Reset_all_meteo_parameters();
         } // protected void done()

      }.execute(); // new SwingWorker<Void, Void>()
   } // if (result == JFileChooser.APPROVE_OPTION

}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Output_obs_to_file_format_101()
{
   // pop-up the file chooser dialog box
   JFileChooser chooser = new JFileChooser();
   int result = chooser.showSaveDialog(main.this);
   if (result == JFileChooser.APPROVE_OPTION)
   {
      output_file = chooser.getSelectedFile().getPath();

      new SwingWorker<Void, Void>()
      {
         @Override
         protected Void doInBackground() throws Exception
         {
            boolean doorgaan            = true;
            String file_format_101_line = "";     
           
            
            file_format_101_line = get_format_101_obs_from_file();
            if (file_format_101_line.equals("") == true)
            {
               doorgaan = false;
            }
            
            if (doorgaan == true)
            {
               // write to selected file
               try
               {
                  BufferedWriter out = new BufferedWriter(new FileWriter(output_file));

                  out.write(file_format_101_line);
                  //out.newLine();   // newLine(): write a line separator. The line separator string is defined by the system property line.separator, and is not necessarily a single newline ('\n') character.
                  out.close();

                  // user feedback
                  String info = "obs written to: " + output_file;
                  JOptionPane.showMessageDialog(null, info, main.APPLICATION_NAME + " info", JOptionPane.INFORMATION_MESSAGE);

               } // try
               catch (IOException | HeadlessException e)
               {
                  JOptionPane.showMessageDialog(null, "unable to write to: " + output_file, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               } // catch
            } // if (doorgaan == true)
            
            return null;

         } // protected Void doInBackground() throws Exception

         @Override
         protected void done()
         {
            IMMT_log();
            
            Reset_all_meteo_parameters();
         } // protected void done()

      }.execute(); // new SwingWorker<Void, Void>()
   } // if (result == JFileChooser.APPROVE_OPTION

}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Output_obs_by_email_FM13()
{
   // This function called by: Output_obs_by_email_actionPerformed()

   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         /*
         // Version 6 of the Java Platform, Standard Edition (Java SE), continues to narrow the gap with
         // new system tray functionality, better  print support for JTable, and now the Desktop API
         // (java.awt.Desktop API).
         //
         // Use the Desktop.isDesktopSupported() method to determine whether the Desktop API is available.
         // On the Solaris Operating System and the Linux platform, this API is dependent on Gnome libraries.
         // If those libraries are unavailable, this method will return false. After determining that the API is
         // supported, that is, the isDesktopSupported() returns true, the application can retrieve a Desktop
         // instance using the static method getDesktop().
         //
         */
         Desktop desktop = null;

         // Before more Desktop API is used, first check
         // whether the API is supported by this particular
         // virtual machine (VM) on this particular host.
         if (Desktop.isDesktopSupported())
         {
            desktop = Desktop.getDesktop();
            try
            {
               // if ddmmyyyy in subject field -> replace by actual utc date of observation 
         
               String obs_email_subject_new = obs_email_subject.replaceAll("ddhhmm", mydatetime.YY_code + mydatetime.GG_code + "00");

               String mail_txt = obs_email_recipient + "?subject=" + obs_email_subject_new  + "&body=" + obs_write;
               URI uriMailTo = null;
               try
               {
                  uriMailTo = new URI("mailto", mail_txt, null);
               }
               catch (URISyntaxException ex)
               {
                  JOptionPane.showMessageDialog(null, "Error invoking default Email program (URISyntaxException)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               }

               desktop.mail(uriMailTo);

            } // try
            catch (IOException ex)
            {
               JOptionPane.showMessageDialog(null, "Error invoking default Email program", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            }
         } // if (Desktop.isDesktopSupported())
         else // Desktop method not supported
         {
            // Now try to open with mailto
            //
            // Now (mailto)workarounds follow with 'best shots'
            //
            //
            // NB The main problem with using mailto is with breaking lines. Use %0A for carriage returns, %20 for spaces.
            // NB http://stackoverflow.com/questions/17373/how-do-i-open-the-default-mail-program-with-a-subject-and-body-in-a-cross-platfo
            // NB http://www.wsoftware.de/practices/proc-execs.html
            
            /* determine the OS this program is running on */
            OSType ostype = detect_OS();
            String os = "";
            switch (ostype)
            {
               case WINDOWS: os = "WINDOWS"; 
                             break;
               case MACOS:   os = "MACOS"; 
                             break;
               case LINUX:   os = "LINUX"; 
                             break;
               case OTHER:   os = "OTHER"; 
                             break;
               default:      os = "OTHER"; 
                             break;
            }
            
            
            //JOptionPane.showMessageDialog(null, "Error invoking default Email program (method not supported on this computer system)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            Runtime runtime = Runtime.getRuntime();
            
            // NB Use %0A for carriage returns, %20 for spaces [see "urlEncode(obs_write)"]
            // if ddmmyyyy in subject field -> replace by actual utc date of observation 
            String obs_email_subject_new = urlEncode(obs_email_subject.replaceAll("ddhhmm", mydatetime.YY_code + mydatetime.GG_code + "00"));
            String mail_txt = obs_email_recipient + "?subject=" + obs_email_subject_new  + "&body=" + urlEncode(obs_write);
            
            //if (amos_mail) // DO NOT USE %0A for carriage returns and %20 for spaces [so NOT "urlEncode(obs_write)"] NB DIDN'T WORK EITHER SO FROM THIS VERSION NO PARTICULAR CODE FOR AMOS MAIL
            if (os.equals("WINDOWS"))   
            {
               try
               {
                  runtime.exec("cmd /c " + "start " + "mailto:" + mail_txt);
                  
                  // NB during a test on 25-05-2016 on Windows 10 Runtime.getRuntime(); worked only partially (the body contents wasn't copied to the amial client)
               }
               catch (IOException e)
               {
                  JOptionPane.showMessageDialog(null, "Error invoking default email client (-Desktop-method not supported on this computer system and Runtime alternatives failed)" , main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               }
            } // if (os.equals("WINDOWS")) 
            else if (os.equals("MACOS")) 
            {
               try
               {
                  runtime.exec("open " + "mailto:" + mail_txt);
               }
               catch (IOException e)
               {
                  JOptionPane.showMessageDialog(null, "Error invoking default email client (-Desktop-method not supported on this computer system and Runtime alternatives failed)" , main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               }
            } // else if (os.equals("MACOS")) 
            else // most probably a Linux;
            {
               try 
               {
                  runtime.exec("xdg-open " + "mailto:" + mail_txt);
               } 
               catch (IOException e2) 
               {
                  try
                  {
                     runtime.exec("kde-open " + "mailto:" + mail_txt);
                  }
                  catch (IOException e3)
                  {
                     JOptionPane.showMessageDialog(null, "Error invoking default email client (-Desktop-method not supported on this computer system and Runtime alternatives failed)" , main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                  }
               }
            } // else
         } // else (Desktop method not supported)
         
         
         return null;

      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         IMMT_log();
         
         Reset_all_meteo_parameters();
      } // protected void done()

   }.execute(); // new SwingWorker<Void, Void>()
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private static String urlEncode(String s) 
{
   // NB http://stackoverflow.com/questions/17373/how-do-i-open-the-default-mail-program-with-a-subject-and-body-in-a-cross-platfo
   
   StringBuilder sb = new StringBuilder();
   for (int i = 0; i < s.length(); i++) 
   {
       char ch = s.charAt(i);
       if (Character.isLetterOrDigit(ch)) 
       {
          sb.append(ch);
       }
       else 
       {
          sb.append(String.format("%%%02X", (int)ch));
       }
   }
   
   return sb.toString();
}




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Output_obs_by_email_format_101()
{
   // This function called by: Output_obs_by_email_actionPerformed()
   

   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         /*
         // Version 6 of the Java Platform, Standard Edition (Java SE), continues to narrow the gap with
         // new system tray functionality, better  print support for JTable, and now the Desktop API
         // (java.awt.Desktop API).
         //
         // Use the Desktop.isDesktopSupported() method to determine whether the Desktop API is available.
         // On the Solaris Operating System and the Linux platform, this API is dependent on Gnome libraries.
         // If those libraries are unavailable, this method will return false. After determining that the API is
         // supported, that is, the isDesktopSupported() returns true, the application can retrieve a Desktop
         // instance using the static method getDesktop().
         //
         */
         Desktop desktop        = null;
         String email_body_line = "";
         boolean doorgaan       = true;

         // Before more Desktop API is used, first check
         // whether the API is supported by this particular
         // virtual machine (VM) on this particular host.
         if (Desktop.isDesktopSupported())
         {
            desktop = Desktop.getDesktop();
            try
            {
               final String volledig_path_format_101_compressed_file = main.logs_dir + java.io.File.separator + FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_TEMP_DIR + java.io.File.separator + "HPK_" + FORMAT_101_INPUT_FILE;// NB adding "HPK_" to the input file name is automatically done by the C-code compression functions
               
               //////// format 101 message in the e-mail body //////
               //
               if (main.obs_101_email.equals(FORMAT_101_BODY) == true)
               {
                  // read the compressed obs (format 101) which is the only line in file HPK_format_101.txt
                  email_body_line = get_format_101_obs_from_file();
                  if (email_body_line.equals("") == true)
                  {
                     doorgaan = false;
                  }
               } // if (main.obs_101_email.equals(FORMAT_101_BODY) == true);
               
               /////// format 101 message as attachment //////
               //
               else if (main.obs_101_email.equals(FORMAT_101_ATTACHEMENT) == true)
               {
                  // first check if compressed file exists
                  final File compressed_file = new File(volledig_path_format_101_compressed_file);

                  if (compressed_file.exists() == true)
                  {
                     email_body_line = "Please attach manually the file: " + volledig_path_format_101_compressed_file;
                     doorgaan = true;
                  }
                  else
                  {
                     JOptionPane.showMessageDialog(null, "No format 101 obs available. The following file does not exist: " + volledig_path_format_101_compressed_file, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                     doorgaan = false;
                  } // else
               } // else if (main.obs_101_email.equals(FORMAT_101_ATTACHEMENT) == true)
               
               
               /////// invoke email program //////
               //
               if (doorgaan == true)
               {
                  /* if ddmmyyyy in subject field -> replace by actual utc date of observation */
                  String obs_email_subject_new = obs_email_subject.replaceAll("ddhhmm", mydatetime.YY_code + mydatetime.GG_code + "00");
                  
                  String mail_txt = obs_email_recipient + "?subject=" + obs_email_subject_new  + "&body=" + email_body_line;
                  URI uriMailTo = null;
                  try
                  {
                     uriMailTo = new URI("mailto", mail_txt, null);
                  }
                  catch (URISyntaxException ex)
                  {
                     JOptionPane.showMessageDialog(null, "Error invoking default Email program" + " (" + ex + ")", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                  }

                  desktop.mail(uriMailTo);
               } // if (doorgaan == true)
            } // try
            catch (IOException ex)
            {
               JOptionPane.showMessageDialog(null, "Error invoking default Email program" + " (" + ex + ")", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            }
         } // if (Desktop.isDesktopSupported())
         else
         {
            JOptionPane.showMessageDialog(null, "Error invoking default Email program (-Desktop- method not supported on this computer system)", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         } // else

         return null;
      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         IMMT_log();
         
         Reset_all_meteo_parameters();
      } // protected void done()

   }.execute(); // new SwingWorker<Void, Void>()
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private String get_format_101_obs_from_file()
{
   String format_101_obs = "";
   
         
   final String volledig_path_format_101_compressed_file = main.logs_dir + java.io.File.separator + FORMAT_101_ROOT_DIR + java.io.File.separator + FORMAT_101_TEMP_DIR + java.io.File.separator + "HPK_" + FORMAT_101_INPUT_FILE;// NB adding "HPK_" to the input file name is automatically done by the C-code compression functions

   try
   {
      BufferedReader in = new BufferedReader(new FileReader(volledig_path_format_101_compressed_file));
                  
      if ((format_101_obs = in.readLine()) == null)
      {
         JOptionPane.showMessageDialog(null, "When retrieveing format 101 data empty file: " + volledig_path_format_101_compressed_file, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         format_101_obs = "";                // the function which invoke get_format_101_obs_from_file() will check this value
      } // else
                     
     in.close();
   } // try
   catch (IOException | HeadlessException e)
   {
      JOptionPane.showMessageDialog(null, "When retrieving format 101 data error opening file: " + volledig_path_format_101_compressed_file + " (" + e + ")", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
      format_101_obs = "";                   // the function which invoke get_format_101_obs_from_file() will check this value
   } // catch         
      
   //  JOptionPane.showMessageDialog(null, format_101_obs, "test inhoud format_101_obs" + " error", JOptionPane.WARNING_MESSAGE);

   return format_101_obs;
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Output_obs_to_server_FM13()
{
   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         BasicService bs = null;
         try
         {
            bs = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
         }
         catch (UnavailableServiceException ex) { }

         if (bs != null)
         {   
            URL url_basis = bs.getCodeBase();
            try
            {
               //String SPATIE = SPATIE_OBS_SERVER;                                     // dan "%20" als spatie grbruiken
               //url_php = new URL(url_basis + (String)compose_coded_obs(SPATIE));
               url_php = new URL(url_basis + "index_webstart.php?obs=" + obs_write);    // obs_write is a global var (set via: obs_write = compose_coded_obs(SPATIE);)

               // LET OP
               // NB met IE7 gaat als je voor spatie een " " neemt het wel goed
               //    met FireFox gaat als je voor spatie " " neemt het NIET goed

            } // try
            catch (MalformedURLException ex) { /*ex.printStackTrace();*/ }

            //this.getAppletContext().showDocument(url);
            bs.showDocument(url_php);
         }
         
         return null;

      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         IMMT_log();
         
         Reset_all_meteo_parameters();
      } // protected void done()
   }.execute(); // new SwingWorker<Void, Void>()

}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void Output_obs_to_server_format_101()
{
   // http://stackoverflow.com/questions/2793150/using-java-net-urlconnection-to-fire-and-handle-http-requests
   //
   // NB see also: RS232_Send_Sensor_Data_to_APR() [main_RS232_RS422.java]
   
   // called from: Output_Obs_to_server_menu_actionPerformed() [main.java]
   
   
   new SwingWorker<Integer, Void>()
   {
      @Override
      protected Integer doInBackground() throws Exception
      {
         String server_format_101_line = "";
         Integer responseCode          = OK_RESPONSE_FORMAT_101;            // OK
         
         
         // read the compressed obs (format 101) which is the only line in file HPK_format_101.txt
         server_format_101_line = get_format_101_obs_from_file();
         if (server_format_101_line.equals("") == true)
         {
            responseCode = INVALID_RESPONSE_FORMAT_101;                        // self defined
         }
      
/*         
         if (doorgaan == true)
         {
            BasicService bs = null;
            try
            {
               bs = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
            }
            catch (UnavailableServiceException ex) 
            { 
               System.out.println("+++ " + ex.toString() + " Function Output_obs_to_server_format_101()");
            }

            if (bs != null)
            {   
               URL url_basis = bs.getCodeBase();
               try
               {
                  // ??String SPATIE = SPATIE_OBS_SERVER;                                     // dan "%20" als spatie grbruiken
                  // ??url_php = new URL(url_basis + (String)compose_coded_obs(SPATIE));
                  
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
                  server_format_101_line = URLEncoder.encode(server_format_101_line, "UTF-8");
                  
                  url_php = new URL(url_basis + "index_webstart_101.php?obs=" + server_format_101_line);    // obs_write is a global var (set via: obs_write = compose_coded_obs(SPATIE);)

                  // LET OP
                  // ?? NB met IE7 gaat als je voor spatie een " " neemt het wel goed
                  // ??   met FireFox gaat als je voor spatie " " neemt het NIET goed

               } // try
               catch (MalformedURLException ex) 
               { 
                  System.out.println("+++ " + ex.toString() + " Function Output_obs_to_server_format_101()");
               }

               //this.getAppletContext().showDocument(url);
               //bs.showDocument(url_php);
               
               if (!(bs.showDocument(url_php)))
               {
                  System.out.println("+++ " + "showDocument (web browser) request failed; Function Output_obs_to_server_format_101()");         
               }
               else
               {
                  System.out.println("--- obs format 101 via web browser"); 
               }
               
            } // if (bs != null)
         } // if (doorgaan == true)
*/       
         
         
         if (Objects.equals(responseCode, OK_RESPONSE_FORMAT_101))   
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
            String encoded_server_format_101_obs = URLEncoder.encode(server_format_101_line, "UTF-8");               
               
            //String url = "http://www.knmi.nl/samenw/turbowin/webstart101/index_webstart_101.php?obs=" + encoded_server_format_101_obs; 
            String url = upload_URL + "obs=" + encoded_server_format_101_obs;       // eg upload U?rL = http://www.knmi.nl/samenw/turbowin/webstart101/index_webstart_101.php?
               
            URL obj = null;
            try 
            {
               obj = new URL(url);
               HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            
               // optional (default is GET)
	            con.setRequestMethod("GET");  
               //con.setDoOutput(true);        //  To be clear: setting URLConnection#setDoOutput(true) to true implicitly sets the request method to POST
                  
               String message = "[MANUAL] sending 'GET' request to URL: " + url;
               main.log_turbowin_system_message(message);
     
               responseCode = con.getResponseCode();
      
               // NB besides the response code there is also a corresponding response text, but unfortunately with html tags, 
               //    and only with the standard response codes, self defined response codes are not returned?. Not suitable for direct using it into a popup message box
               //    so only using the reponse code and locally (in this program) determined the corresponding return http message text
               //
/*                  
                  BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		            String inputLine;
		            StringBuilder response = new StringBuilder();
                  //
                  
                  //System.out.println(responseCode);
                  
                  // NB below lists all the details
                      Map<String, List<String>> map = con.getHeaderFields();                  
                      for (Map.Entry<String, List<String>> entry : map.entrySet())
                      {
                         System.out.println(entry.getKey() + " : " + entry.getValue());
                      }
                  //    eg
                  //    Keep-Alive : [timeout=5, max=100]
                  //    null : [HTTP/1.1 495 your call sign is not on the whitelist]
                  //    Server : [Apache]
                  //    Connection : [Keep-Alive]
                  //    Content-Length : [32]
                  //    Date : [Mon, 01 Feb 2016 07:43:44 GMT]
                  //    Content-Type : [text/html]

                  //
                  
		            while ((inputLine = in.readLine()) != null) 
                  {
			            response.append(inputLine);
                     
                     System.out.println(inputLine);
		            }
		            in.close();
                     
                   String info = "<html>" + response.toString() + "</html>";   
                  JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
*/                     
                     
            } // try
            catch (MalformedURLException ex)
            {
               //String message = "[MANUAL] send obs failed; MalformedURLException (function: Output_obs_to_server_format_101())"; 
               //main.log_turbowin_system_message(message);
               //main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message);
               
               responseCode = RESPONSE_MALFORMED_URL;
            }
            catch (IOException ex) 
            {
               //String message = "[MANUAL] send obs failed; IOException; most probably no internet connection available; (function: Output_obs_to_server_format_101())"; 
               //main.log_turbowin_system_message(message);
               //main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message);
               
               responseCode = RESPONSE_NO_INTERNET;
            } // catch            
            
         } // if (Objects.equals(responseCode, OK_RESPONSE_FORMAT_101))
         
         
         return responseCode;

      } // protected Void doInBackground() throws Exception

      @Override
      protected void done()
      {
         try 
         {
            Integer response_code = get();
            
            if (response_code == 200)          // OK
            {
               String message_a = "[MANUAL] send obs success"; 
                  
               // file logging
               main.log_turbowin_system_message(message_a);
                  
               // bottom line main screen
               main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message_a); // update status field (bottom line main -progress- window) 
               
               // pop-up message in manual mode
               String info = "<html>" + 
                             main.sdf_tsl_2.format(new Date()) + " UTC " + "send obs success" + "<br>" +
                             "Many thanks for your cooperation" + 
                             "</html>";
               JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " info", JOptionPane.INFORMATION_MESSAGE);
               
               IMMT_log();
               Reset_all_meteo_parameters();
            }
            else // send obs NOT ok
            {
               // NB besides the response code there is also a corresponding response text (from the apache server, but unfortunately with html tags, 
               //    and only with the standard response codes, self defined response codes are not returned (from the apache server)?. 
               //    Not suitable for direct using it into a popup message box
               //    so only using the reponse code and locally (in this program) determined the corresponding return http message text
               //
               String message_b = "[MANUAL] send obs failed; " + http_respons_code_to_text(response_code).replace("<br>", " ");
                  
               // file logging
               main.log_turbowin_system_message(message_b);
                  
               // bottom main screen
               main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message_b); 
               
               // pop-up message in manual mode
               String info = "<html>" + 
                             main.sdf_tsl_2.format(new Date()) + " UTC " + "send obs failed; " + "<br>" +
                             http_respons_code_to_text(response_code) + 
                             "</html>";
               JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            
               // "try again?" pop-up message
               if (JOptionPane.showConfirmDialog(null, "try again (Obs to server)", APPLICATION_NAME + " ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
               {
                  // YES button pressed (= try again)
                  Output_obs_to_server_format_101();
               }
               else // NO or CANCEL clicked by the user
               {
                  IMMT_log();
                  Reset_all_meteo_parameters();
               }   
            } // else (send obs NOT ok)
         } // protected void done()
         catch (InterruptedException | ExecutionException ex) 
         {
            String message = "[MANUAL] error in Function: Output_obs_to_server_format_101;" + ex.toString(); 
            
            // fille logging 
            main.log_turbowin_system_message(message);
            
            // bottom main screen
            main.jTextField4.setText(main.sdf_tsl_2.format(new Date()) + " UTC " + message);
            
            // pop-up message in manual mode
            String info = "<html>" + 
                          main.sdf_tsl_2.format(new Date()) + " UTC " + "send obs failed; " + "<br>" +
                          http_respons_code_to_text(RESPONSE_INTERRUPTION) + 
                          "</html>";
            JOptionPane.showMessageDialog(null, info, APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
            
            // "try again?" pop-up message
            if (JOptionPane.showConfirmDialog(null, "try again (Obs to server)", APPLICATION_NAME + " ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            {
               // YES button pressed (= try again)
               Output_obs_to_server_format_101();
            }
            else // NO or CANCEL clicked by the user
            {
               IMMT_log();
               Reset_all_meteo_parameters();
            }   
         } // catch (InterruptedException | ExecutionException ex) 
      } // protected void done()
   }.execute(); // new SwingWorker<Void, Void>()

}


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static String http_respons_code_to_text(int responseCode)
{
   // called from:
   //  - RS232_Send_Sensor_Data_to_APR() [main_RS232_RS422.java]
   // 
   
   
   String text = "Unknown error";          
   switch (responseCode) 
   {
      // 1xx: Information
      case 100: text = "Continue (HTTP code 100)"; break;
      case 101: text = "Switching Protocols (HTTP code 101)"; break;
      case 103: text = "Checkpoint (HTTP code 103)"; break;
      
      // 2xx: Successful
      case 200: text = "OK"; break;
      case 201: text = "Created"; break;
      case 202: text = "Accepted"; break;
      case 203: text = "Non-Authoritative Information"; break;
      case 204: text = "No Content"; break;
      case 205: text = "Reset Content"; break;
      case 206: text = "Partial Content"; break;
      
      // 3xx: Redirection
      case 300: text = "Multiple Choices (HTTP code 300)"; break;
      case 301: text = "Moved Permanently (HTTP code 301)"; break;
      case 302: text = "Moved Temporarily (HTTP code 302)"; break;
      case 303: text = "See Other (HTTP code 303)"; break;
      case 304: text = "Not Modified (HTTP code 304)"; break;
      case 305: text = "Use Proxy (HTTP code 305)"; break;
      case 306: text = "Switch Proxy (HTTP code 306)"; break;
      case 307: text = "Temporary Redirect (HTTP code 307)"; break;
      case 308: text = "Resume Incomplete (HTTP code 308)"; break;   
      
      // 4xx: Client Error
      case 400: text = "Bad Request (HTTP code 400)"; break;
      case 401: text = "Unauthorized (HTTP code 401)"; break;
      case 402: text = "Payment Required (HTTP code 402)"; break;
      case 403: text = "Forbidden (HTTP code 403)"; break;
      case 404: text = "Not Found (upload URL unknown) (HTTP code 404)"; break;
      case 405: text = "Method Not Allowed (HTTP code 405)"; break;
      case 406: text = "Not Acceptable (HTTP code 406)"; break;
      case 407: text = "Proxy Authentication Required (HTTP code 407)"; break;
      case 408: text = "Request Time-out (HTTP code 408)"; break;
      case 409: text = "Conflict (HTTP code 409)"; break;
      case 410: text = "Gone (HTTP code 410)"; break;
      case 411: text = "Length Required (HTTP code 411)"; break;
      case 412: text = "Precondition Failed (HTTP code 412)"; break;
      case 413: text = "Request Entity Too Large (HTTP code 413)"; break;
      case 414: text = "Request-URI Too Large (HTTP code 414)"; break;
      case 415: text = "Unsupported Media Type (HTTP code 415)"; break;
      case 416: text = "Requested Range Not Satisfiable (HTTP code 416)"; break;
      case 417: text = "Expectation Failed (HTTP code 417)"; break;        
            
      // 5xx: Server Error   
      case 500: text = "Internal Server Error (HTTP code 500)"; break;                
      case 501: text = "Not Implemented (HTTP code 501)"; break;
      case 502: text = "Bad Gateway (HTTP code 502)"; break;
      case 503: text = "Service Unavailable (HTTP code 503)"; break;
      case 504: text = "Gateway Time-out (HTTP code 504)"; break;
      case 505: text = "HTTP Version not supported (HTTP code 505)"; break;
      case 511: text = "Authentication Required (HTTP code 511)"; break;
         
      // 7xx: TurboWin+/server Error (custom/sef defined errors) 
      //// start self defined section (must be coordinated with server [index_webstart_101.php]) //////   
      case 700: text = "obs invalid format"; break;                                                                       // self defined
      case 701: text = "(masked)call sign in the obs not on the email whitelist of this server." + "<br>" +               // self defined
                       "Please send an email with your (masked)call sign to your PMO or National Meteorological Service"; break;
      case 702: text = "obs routing from server to Meteorological Centre failed"; break;                                  // self defined
      
      //// start self defined section (no coordination with server [index_webstart_101.php]) //////      
      case 710: text = "internal error when generating format 101 obs"; break;                                            // self defined 
      case 711: text = "most probably no internet connection available"; break;                                           // self defined (actually IOexception)
      case 712: text = "internal error, malformed URL"; break;                                                            // self defined
      case 713: text = "most probably no internet connection available (format 101 obs ok)"; break;                       // self defined
      case 714: text = "InterruptedException or ExecutionException"; break;                                               // self defined
      //// end self defined section //////         
         
      // default   
      default:  text = "Unknown error"; break;                  
   } // switch   
       
   
   return text;     
}                  






/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private void bepaal_last_record_uit_immt()
{
   // NB This function will be called from within a swingworker e.g. see Output_obs_by_email_actionPerformed()
   //    so not necessary to use a swingworker here (it is adviced to use a swingworker when file reading/writing)

   String record                   = "";

   /* initialisatie */
   last_record                     = "";


   /* first check if there is an immt log source file present (and not empty) */
   String volledig_path_immt = logs_dir + java.io.File.separator + IMMT_LOG;

   File immt_file = new File(volledig_path_immt);
   if (immt_file.exists() && immt_file.length() > 0)     // length() in bytes
   {
      BufferedReader in = null;

      try
      {
         in = new BufferedReader(new FileReader(volledig_path_immt));

         while ((record = in.readLine()) != null)
         {
            last_record = record; 
         }
         in.close();
      }
      catch (IOException ex) { }

   } // if (immt_file.exists() && immt_file.length() > 0)
}




/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
private boolean Check_Land_Sea_Mask()
{
   /* NB om de grootte van de jar te beperken wordt er alleen gecheckt op 1 graads vak niveau (zgf_sam) */
   /*    en (i.t.t. TurboWin) niet op 1/10 graads vak niveau (zgf_lkw) */

   int Octant = INVALID;
   int la_vak_10;
   int la_vak_1;
   int lo_vak_10;
   int lo_vak_1;
   int vak_10;
   int sam_index;
   int land_zee_cijfer_sam;            // gevonden cijfer in ZGF_SAM (10 graads vakken file)
   int int_record_sam_vak_10;
   boolean zee_vak_ok = true;
   String record_sam;


   System.out.println("--- Checking entered position against a land-sea mask");
   
   try
   {
      // reading file 1 degree squares
      InputStream is = getClass().getResourceAsStream(main.ICONS_DIRECTORY + "zgf_sam");
      BufferedReader in_1 = new BufferedReader(new InputStreamReader(is));

      /* deze aanzetten om de 1/10 graads niveau file mee te nemen                          */
      /* InputStream is = getClass().getResourceAsStream(main.ICONS_DIRECTORY + "zgf_lkw"); */
      /* BufferedReader in_2 = new BufferedReader(new InputStreamReader(is));               */



      //
      // Octant bepalen (WMO code table 0371)
      //
      //if ((obs_North_or_South == "N") && (obs_East_or_West == "W"))
      if ( (myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true) )
      {
         //if (num_LoLoLoLo >= 0 && num_LoLoLoLo < 900)                      // n.b. 900 = 90.0 gr
         if (myposition.int_longitude_degrees >= 0 && myposition.int_longitude_degrees < 90)
            Octant = 0;
         else if (myposition.int_longitude_degrees >= 90 && myposition.int_longitude_degrees <= 180)             // n.b. 1800 = 180.0 gr
            Octant = 1;
      } 

      //else if ((obs_North_or_South == "N") && (obs_East_or_West == "E"))
      else if ( (myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_NORTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true) )
      {
         if (myposition.int_longitude_degrees >= 90 && myposition.int_longitude_degrees <= 180)
            Octant = 2;
         else if (myposition.int_longitude_degrees >= 0 && myposition.int_longitude_degrees < 90)
            Octant = 3;
      } 

      //else if ((obs_North_or_South == "S") && (obs_East_or_West == "W"))
      else if ( (myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_WEST) == true) )
      {
         if (myposition.int_longitude_degrees >= 0 && myposition.int_longitude_degrees < 90)                      // n.b. 900 = 90.0 gr
            Octant = 5;
         else if (myposition.int_longitude_degrees >= 90 && myposition.int_longitude_degrees <= 180)             // n.b. 1800 = 180.0 gr
            Octant = 6;
      } 

      //else if ((obs_North_or_South == "S") && (obs_East_or_West == "E"))
      else if ( (myposition.latitude_hemisphere.equals(myposition.HEMISPHERE_SOUTH) == true) && (myposition.longitude_hemisphere.equals(myposition.HEMISPHERE_EAST) == true) )
      {
         if (myposition.int_longitude_degrees >= 90 && myposition.int_longitude_degrees <= 180)
            Octant = 7;
         else if (myposition.int_longitude_degrees >= 0 && myposition.int_longitude_degrees < 90)
            Octant = 8;
      } 


      //
      // La-Vak bepalen
      //
      //la_vak_10    = num_LaLaLa / 100;                        // 123 -> 1
      //la_vak_1     = (num_LaLaLa / 10) % 10;                  // 123 -> 2
      la_vak_10 = myposition.int_latitude_degrees / 10;         // 12 -> 1
      la_vak_1  = myposition.int_latitude_degrees % 10;         // 12 -> 2
      //la_vak_1_10  = num_LaLaLa % 10;                         // 123 -> 3
      //la_voor_lkw  = num_LaLaLa / 10;                         // 123 -> 12

      //
      // Lo-Vak bepalen
      //
      //lo_vak_10    = (num_LoLoLoLo / 100) % 10;               // 1234 -> 2
      //lo_vak_1     = (num_LoLoLoLo / 10) % 10;                // 1234 -> 3
      lo_vak_10 = (myposition.int_longitude_degrees / 10) % 10; // 123 -> 2
      lo_vak_1  = (myposition.int_longitude_degrees) % 10;      // 123 -> 3
      //lo_vak_1_10  = num_LoLoLoLo % 10;                       // 1234 -> 4
      //lo_voor_lkw  = (num_LoLoLoLo / 10) % 100;               // 1234 -> 23

      //
      // 10 graads vak bepalen (WMO code tabel 0371)
      //
      vak_10 = (Octant * 100) + (la_vak_10 * 10) + lo_vak_10;

      while( ((record_sam = in_1.readLine()) != null) && (Octant != INVALID) )
      {
         //record_sam.read_line(in_1);
         if (record_sam.length() == 127)                   // zijn allemaal 127 char. lang
         {
            //if (atoi(record_sam.substring(0, 3)) == vak_10)
            try
            {
               int_record_sam_vak_10 = Integer.valueOf(record_sam.substring(0, 3));

               if (int_record_sam_vak_10 == vak_10)
               {
                  sam_index = 5 + (11 * la_vak_1) + lo_vak_1; // start op positie 0

                  //land_zee_cijfer_sam = atoi(record_sam.substring(sam_index, sam_index + 1));
                  land_zee_cijfer_sam = Integer.valueOf(record_sam.substring(sam_index, sam_index + 1));

                  if (land_zee_cijfer_sam == 0)               // sea position
                     zee_vak_ok = true;
                  else if (land_zee_cijfer_sam == 4)          // not yet but in mask/file
                     zee_vak_ok = true;
                  else if (land_zee_cijfer_sam == 2)          // wrong position
                     zee_vak_ok = false;
                  else if (land_zee_cijfer_sam == 3)          // land position
                     zee_vak_ok = false;
                  else if (land_zee_cijfer_sam == 1)          // coast position
                  {
                     /* code if testing on 1/10 degree squares */
                     zee_vak_ok = true;
                  } // else if (land_zee_cijfer == 1)

                  break;                                           // ok, gevonden verlaten do-while sam

               } // if (atoi(record.SubString(0, 3)) == vak_10)
            } // try
            catch (NumberFormatException e) { }

         } // if (record.length() == 127)
         else // invalid line length
         {
            break;
         }
      } // while((record_sam = in_1.readLine()) != null)

      in_1.close();
   }
   catch (Exception e)
   {
      JOptionPane.showMessageDialog(null, "Reading error 'sea-land mask' file", APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
   } // catch


   if (zee_vak_ok == false)
   {
      String info = "";
      info = "-land mask check-\n";
      info += "obs position:\n";
      info += myposition.latitude_degrees;
      info += "° ";
      info += myposition.latitude_minutes;
      info += "' ";
      info += myposition.latitude_hemisphere;
      info += "  ";
      info += myposition.longitude_degrees;
      info += "° ";
      info += myposition.longitude_minutes;
      info += "' ";
      info += myposition.longitude_hemisphere;
      info += "\n";

      if (JOptionPane.showConfirmDialog(null, info, main.APPLICATION_NAME + " please confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
      {
         JOptionPane.showMessageDialog(null, "Please correct the error (no final obs was coded)", main.APPLICATION_NAME + " warning", JOptionPane.WARNING_MESSAGE);
         zee_vak_ok = false;
      }
      else
      {
         zee_vak_ok = true;
      }
   } // if (zee_vak_ok == false)


   return zee_vak_ok;
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void Reset_all_meteo_parameters()        
{
   // global var's
   //
   
   // past weather
   mypastweather.W1_code                          = "";
   mypastweather. W2_code                         = "";
   mypastweather.past_weather_1                   = "";
   mypastweather.past_weather_2                   = "";
   
   // present weather
   mypresentweather.ww_code                       = "";
   mypresentweather.present_weather               = "";
   
   // visibility
   myvisibility.VV                               = "";
   myvisibility.VV_code                          = "";
   
   // barograph
   mybarograph.a_code                            = "";      
   mybarograph.ppp_code                          = "";
   mybarograph.pressure_amount_tendency          = "";
   
   // barometer
   mybarometer.pressure_reading                  = "";
   mybarometer.pressure_msl                      = "";
   mybarometer.PPPP_code                         = "";
   mybarometer.deepest_draft                     = "";
   //mybarometer.barometer_instrument_correction_new = "";
   mybarometer.pressure_reading_corrected        = "";
   mybarometer.pressure_msl_corrected            = "";
   
   // Cl
   mycl.cl_code                                  = "";
   
   // Cm
   mycm.cm_code                                  = "";
   
   // Ch
   mych.ch_code                                  = ""; 
   
   // cloud cover and height
   mycloudcover.N                                = "";
   mycloudcover.Nh                               = "";
   mycloudcover.h                                = "";
   mycloudcover.N_code                           = "";                             
   mycloudcover.Nh_code                          = "";                             
   mycloudcover.h_code                           = "";                             
   
   // Temperatures
   mytemp.air_temp                              = "";           
   mytemp.wet_bulb_temp                         = "";
   mytemp.sea_water_temp                        = "";
   mytemp.sn_TTT_code                           = "";
   mytemp.TTT_code                              = "";     
   mytemp.sn_TbTbTb_code                        = "";           // Tb = Twet
   mytemp.TbTbTb_code                           = "";     
   mytemp.ss_TsTsTs_code                        = "";           // Ts = Tsea
   mytemp.TsTsTs_code                           = "";     
   mytemp.sn_TdTdTd_code                        = "";
   mytemp.TdTdTd_code                           = "";
   mytemp.immt_sst_indicator                    = "";
   mytemp.double_dew_point                      = INVALID;
   mytemp.wet_bulb_frozen                       = false;
   mytemp.double_rv                             = INVALID;
   
   // Wind
   mywind.wind_dir                              = "";
   mywind.wind_speed                            = "";
   mywind.ship_ground_course                    = "";    
   mywind.ship_ground_speed                     = "";
   mywind.ship_heading                          = "";
   mywind.dd_code                               = "";
   mywind.ff_code                               = "";
   mywind.fff00_code                            = "";
   mywind.iw_code                               = "";
   mywind.HDG_code                              = "";
   mywind.COG_code                              = "";
   mywind.SOG_code                              = "";
   mywind.SLL_code                              = "";
   mywind.sl_code                               = "";
   mywind.hh_code                               = "";
   mywind.RWD_code                              = "";
   mywind.RWS_code                              = "";
   mywind.int_true_wind_dir                     = INVALID;
   mywind.int_true_wind_speed                   = INVALID;
   
   // Waves
   mywaves.wind_waves_period                    = "";   
   mywaves.wind_waves_height                    = "";
   mywaves.swell_1_dir                          = "";
   mywaves.swell_1_period                       = "";
   mywaves.swell_1_height                       = "";
   mywaves.swell_2_dir                          = "";
   mywaves.swell_2_period                       = "";
   mywaves.swell_2_height                       = "";
   mywaves.Hw_code                              = "";
   mywaves.Pw_code                              = "";
   mywaves.Dw1_code                             = "";
   mywaves.Pw1_code                             = "";
   mywaves.Hw1_code                             = "";
   mywaves.Dw2_code                             = "";
   mywaves.Pw2_code                             = "";
   mywaves.Hw2_code                             = "";
  
   // Captain
   for (int r = 0; r < mycaptain.CAPTAIN_ROWS; r++)
   {
     for (int c = 0; c < mycaptain.CAPTAIN_COLUMNS; c++)
     {
        mycaptain.captain_data[r][c]             = "";
     }
   }
   
   // Observer
   for (int r = 0; r < myobserver.OBSERVER_ROWS; r++)
   {
      for (int c = 0; c < myobserver.OBSERVER_COLUMNS; c++)
      {
         myobserver.observer_data[r][c]          = "";
      }
   }
   myobserver.selected_observer                  = "";
   
   // two swell systems
   //---
   
   // one swell system
   //---
   
   // confused swell
   //---
   
   // Icing
   myicing.Is_code                               = "";      
   myicing.EsEs_code                             = "";      
   myicing.Rs_code                               = "";       
   
   // Ice
   myice1.ci_code                                = "";       
   myice1.Si_code                                = "";            
   myice1.bi_code                                = "";            
   myice1.Di_code                                = "";            
   myice1.zi_code                                = "";            
   
   // position
   myposition.latitude_degrees                   = "";
   myposition.latitude_minutes                   = "";
   myposition.latitude_hemisphere                = "";
   myposition.longitude_degrees                  = "";
   myposition.longitude_minutes                  = "";
   myposition.longitude_hemisphere               = "";
   myposition.course                             = "";
   myposition.speed                              = "";
   myposition.lalala_code                        = "";         
   myposition.lolololo_code                      = "";         
   myposition.Qc_code                            = "";         
   myposition.Ds_code                            = "";
   myposition.vs_code                            = "";
   myposition.int_latitude_degrees               = INVALID;    // also used for position sequence check and cloud height advice computation
   myposition.int_latitude_minutes               = INVALID;    // also used for position sequence check
   myposition.int_longitude_degrees              = INVALID;    // also used for position sequence check and cloud height advice computation
   myposition.int_longitude_minutes              = INVALID;    // also used for position sequence check
   
   // date time
   mydatetime.year                               = "";                           
   mydatetime.month                              = "";
   mydatetime.day                                = "";
   mydatetime.hour                               = "";
   mydatetime.MM_code                            = "";                           // month of year 
   mydatetime.YY_code                            = "";                           // day of the month 
   mydatetime.GG_code                            = "";                           // hour of obs       
   
   
   // call sign
   //--
   
   
   // update of the fields on the main screen (and obs line on bottom main screen)
   //
   date_time_fields_update();
   visibility_fields_update();
   barometer_fields_update();
   barograph_fields_update();
   cloud_cover_fields_update();
   clouds_high_fields_update();
   clouds_low_fields_update();
   clouds_middle_fields_update();
   ice_fields_update();
   icing_fields_update();
   observer_field_update();
   past_weather_fields_update();
   position_fields_update();
   present_weather_fields_update();
   temperatures_fields_update();
   waves_fields_update();
   wind_fields_update();
   
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void delete_logs_turbowin_system()
{
   // NB    sdf_tsl_1 = new SimpleDateFormat("MMM_yyyy");                                // e.g. JAN_2016 (part of the file name)
   //       sdf_tsl_1.setTimeZone(TimeZone.getTimeZone("UTC"));
   //
   
   // delete the log files of 3 months and older 
   new SwingWorker<Void, Void>()
   {
      GregorianCalendar cal_delete_datum;
      
      @Override
      protected Void doInBackground() throws Exception
      {
         for (int i = 3; i <= 12; i++)   
         {
            cal_delete_datum = new GregorianCalendar();
            cal_delete_datum.add(Calendar.MONTH, -i);
            
            String file_naam = "turbowin_system_" + main.sdf_tsl_1.format(cal_delete_datum.getTime()) + ".txt";
            String volledig_path_turbowin_system_logs = main.logs_dir + java.io.File.separator + main.TURBOWIN_SYSTEM_LOGS_DIR + java.io.File.separator + file_naam;
            
            File file_log_data = new File(volledig_path_turbowin_system_logs);
            if (file_log_data.exists())
            {
               file_log_data.delete();
            }            
         } // for (int i = 3; i < 12; i++) 

         return null;
      } // protected Void doInBackground() throws Exception
   }.execute(); // new SwingWorker<Void, Void>()
}



/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
public static void log_turbowin_system_message(final String message)
{
   // NB for logging path e.g. .../logs/turbowin_system_Jan_2016.txt
   //    message e.g. : [WOW] barometer height above MSL not available (Maintenance -> Station data)
   //
   //
   //    line on screen e.g. [WOW] barometer height above MSL not available (Maintenance -> Station data)
   //    line in file log e.g. 12-Jan-2016 13:17:33 [WOW] barometer height above MSL not available (Maintenance -> Station data)
   
   
   // to screen
   //
   System.out.println(message);
   
   // to file
   //
   new SwingWorker<Void, Void>()
   {
      @Override
      protected Void doInBackground() throws Exception
      {
         String file_naam = "turbowin_system_" + main.sdf_tsl_1.format(new Date()) + ".txt";     
         String volledig_path_turbowin_system_logs = main.logs_dir + java.io.File.separator + main.TURBOWIN_SYSTEM_LOGS_DIR + java.io.File.separator + file_naam;
  
         try (BufferedWriter out = new BufferedWriter(new FileWriter(volledig_path_turbowin_system_logs, true)))  // true means append the specified data to the file i.e. the pre-exist data in a file is not overwritten and the new data is appended after the pre-exist data. 
         {
            // NB try-with-resource; resources (is and os) will be closed automatically when execution leaves the try block.
      
            out.write(main.sdf_tsl_2.format(new Date()) + " UTC ");                       // new Date() -> always in UTC (because of sdf set in UTC, sdf_tsl_1.setTimeZone(TimeZone.getTimeZone("UTC"));)
            out.write(message);
            out.newLine();      
         }
         catch (IOException ex) 
         { 
            System.out.println("+++ " + ex + "; trying to create the logs folder"); 
      
            // if not done before/deleted, create sub dir TURBOWIN_SYSTEM_LOGS (turbowin_system)
            if ((logs_dir != null) && (logs_dir.compareTo("") != 0))   
            {
               File f = new File(logs_dir);
               if (f.exists() && f.isDirectory()) 
               {         
                  String turbowin_system_logs_dir = main.logs_dir + java.io.File.separator + main.TURBOWIN_SYSTEM_LOGS_DIR;
                  final File dir_turbowin_system_logs = new File(turbowin_system_logs_dir);  
      
                  if (dir_turbowin_system_logs.exists() == false)
                  {
                     dir_turbowin_system_logs.mkdir();  
                     //main.log_turbowin_system_message("[GENERAL] created dir " + turbowin_system_logs_dir);
                  }
               } // if (f.exists() && f.isDirectory())
            } //  if ((logs_dir != null) && (logs_dir.compareTo("") != 0))   
         } // catch
   
         return null;
      } // protected Void doInBackground() throws Exception
   }.execute(); // new SwingWorker<Void, Void>()

}


/***********************************************************************************************/
/*                                                                                             */
/*                                                                                             */
/*                                                                                             */
/***********************************************************************************************/
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
         @Override
            public void run() {
                new main().setVisible(true);
            }
        });
    }
    
 
 
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton jButton1;
   private javax.swing.JButton jButton10;
   private javax.swing.JButton jButton11;
   private javax.swing.JButton jButton12;
   private javax.swing.JButton jButton13;
   private javax.swing.JButton jButton14;
   private javax.swing.JButton jButton15;
   private javax.swing.JButton jButton16;
   private javax.swing.JButton jButton17;
   private javax.swing.JButton jButton18;
   private javax.swing.JButton jButton19;
   private javax.swing.JButton jButton2;
   private javax.swing.JButton jButton20;
   private javax.swing.JButton jButton3;
   private javax.swing.JButton jButton4;
   private javax.swing.JButton jButton5;
   private javax.swing.JButton jButton6;
   private javax.swing.JButton jButton7;
   private javax.swing.JButton jButton8;
   private javax.swing.JButton jButton9;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel10;
   private javax.swing.JLabel jLabel11;
   private javax.swing.JLabel jLabel12;
   private javax.swing.JLabel jLabel13;
   private javax.swing.JLabel jLabel14;
   private javax.swing.JLabel jLabel15;
   private javax.swing.JLabel jLabel16;
   private javax.swing.JLabel jLabel17;
   private javax.swing.JLabel jLabel18;
   private javax.swing.JLabel jLabel19;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel20;
   private javax.swing.JLabel jLabel21;
   private javax.swing.JLabel jLabel22;
   private javax.swing.JLabel jLabel23;
   private javax.swing.JLabel jLabel24;
   private javax.swing.JLabel jLabel25;
   private javax.swing.JLabel jLabel26;
   private javax.swing.JLabel jLabel27;
   private javax.swing.JLabel jLabel28;
   private javax.swing.JLabel jLabel29;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel30;
   private javax.swing.JLabel jLabel31;
   private javax.swing.JLabel jLabel32;
   private javax.swing.JLabel jLabel33;
   private javax.swing.JLabel jLabel34;
   private javax.swing.JLabel jLabel35;
   private javax.swing.JLabel jLabel36;
   private javax.swing.JLabel jLabel37;
   private javax.swing.JLabel jLabel38;
   private javax.swing.JLabel jLabel39;
   public static javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel40;
   private javax.swing.JLabel jLabel5;
   public static javax.swing.JLabel jLabel6;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JLabel jLabel8;
   private javax.swing.JLabel jLabel9;
   private javax.swing.JMenu jMenu1;
   private javax.swing.JMenu jMenu2;
   private javax.swing.JMenu jMenu3;
   private javax.swing.JMenu jMenu4;
   private javax.swing.JMenu jMenu5;
   private javax.swing.JMenu jMenu6;
   private javax.swing.JMenu jMenu7;
   private javax.swing.JMenu jMenu8;
   private javax.swing.JMenuBar jMenuBar1;
   private javax.swing.JMenuItem jMenuItem1;
   private javax.swing.JMenuItem jMenuItem10;
   private javax.swing.JMenuItem jMenuItem11;
   private javax.swing.JMenuItem jMenuItem12;
   private javax.swing.JMenuItem jMenuItem13;
   private javax.swing.JMenuItem jMenuItem14;
   private javax.swing.JMenuItem jMenuItem15;
   private javax.swing.JMenuItem jMenuItem16;
   private javax.swing.JMenuItem jMenuItem17;
   private javax.swing.JMenuItem jMenuItem18;
   private javax.swing.JMenuItem jMenuItem19;
   private javax.swing.JMenuItem jMenuItem2;
   private javax.swing.JMenuItem jMenuItem20;
   private javax.swing.JMenuItem jMenuItem21;
   private javax.swing.JMenuItem jMenuItem22;
   private javax.swing.JMenuItem jMenuItem23;
   private javax.swing.JMenuItem jMenuItem24;
   private javax.swing.JMenuItem jMenuItem25;
   private javax.swing.JMenuItem jMenuItem26;
   private javax.swing.JMenuItem jMenuItem27;
   private javax.swing.JMenuItem jMenuItem28;
   private javax.swing.JMenuItem jMenuItem29;
   private javax.swing.JMenuItem jMenuItem3;
   private javax.swing.JMenuItem jMenuItem30;
   private javax.swing.JMenuItem jMenuItem31;
   private javax.swing.JMenuItem jMenuItem32;
   private javax.swing.JMenuItem jMenuItem33;
   private javax.swing.JMenuItem jMenuItem34;
   private javax.swing.JMenuItem jMenuItem35;
   private javax.swing.JMenuItem jMenuItem36;
   private javax.swing.JMenuItem jMenuItem37;
   private javax.swing.JMenuItem jMenuItem38;
   private javax.swing.JMenuItem jMenuItem39;
   private javax.swing.JMenuItem jMenuItem4;
   private javax.swing.JMenuItem jMenuItem40;
   private javax.swing.JMenuItem jMenuItem41;
   private javax.swing.JMenuItem jMenuItem42;
   private javax.swing.JMenuItem jMenuItem43;
   private javax.swing.JMenuItem jMenuItem44;
   private javax.swing.JMenuItem jMenuItem45;
   public static javax.swing.JMenuItem jMenuItem46;
   private javax.swing.JMenuItem jMenuItem47;
   private javax.swing.JMenuItem jMenuItem48;
   private javax.swing.JMenuItem jMenuItem49;
   private javax.swing.JMenuItem jMenuItem5;
   private javax.swing.JMenuItem jMenuItem50;
   private javax.swing.JMenuItem jMenuItem51;
   private javax.swing.JMenuItem jMenuItem52;
   private javax.swing.JMenuItem jMenuItem53;
   private javax.swing.JMenuItem jMenuItem54;
   private javax.swing.JMenuItem jMenuItem6;
   private javax.swing.JMenuItem jMenuItem7;
   private javax.swing.JMenuItem jMenuItem8;
   private javax.swing.JMenuItem jMenuItem9;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JPanel jPanel3;
   private javax.swing.JPanel jPanel4;
   private javax.swing.JPanel jPanel5;
   private javax.swing.JSeparator jSeparator1;
   private javax.swing.JSeparator jSeparator2;
   private javax.swing.JSeparator jSeparator3;
   private javax.swing.JSeparator jSeparator4;
   private javax.swing.JPopupMenu.Separator jSeparator5;
   private javax.swing.JPopupMenu.Separator jSeparator6;
   private static javax.swing.JTextField jTextField1;
   private static javax.swing.JTextField jTextField10;
   private static javax.swing.JTextField jTextField11;
   private static javax.swing.JTextField jTextField12;
   private static javax.swing.JTextField jTextField13;
   private static javax.swing.JTextField jTextField14;
   private static javax.swing.JTextField jTextField15;
   public static javax.swing.JTextField jTextField16;
   public static javax.swing.JTextField jTextField17;
   private static javax.swing.JTextField jTextField18;
   private static javax.swing.JTextField jTextField19;
   private static javax.swing.JTextField jTextField2;
   private static javax.swing.JTextField jTextField20;
   private static javax.swing.JTextField jTextField21;
   private static javax.swing.JTextField jTextField22;
   private static javax.swing.JTextField jTextField23;
   private static javax.swing.JTextField jTextField24;
   private static javax.swing.JTextField jTextField25;
   private static javax.swing.JTextField jTextField26;
   private static javax.swing.JTextField jTextField27;
   private static javax.swing.JTextField jTextField28;
   private static javax.swing.JTextField jTextField29;
   private static javax.swing.JTextField jTextField3;
   private static javax.swing.JTextField jTextField30;
   private static javax.swing.JTextField jTextField31;
   private static javax.swing.JTextField jTextField32;
   private static javax.swing.JTextField jTextField33;
   private static javax.swing.JTextField jTextField34;
   private static javax.swing.JTextField jTextField35;
   public static javax.swing.JTextField jTextField36;
   public static javax.swing.JTextField jTextField37;
   public static javax.swing.JTextField jTextField38;
   public static javax.swing.JTextField jTextField4;
   public static javax.swing.JTextField jTextField40;
   private static javax.swing.JTextField jTextField5;
   private static javax.swing.JTextField jTextField7;
   private static javax.swing.JTextField jTextField9;
   private javax.swing.JToolBar jToolBar1;
   // End of variables declaration//GEN-END:variables

   
   
   // private constants
   private static final String SPATIE_OBS_SERVER            = "_";//"%20";// must be replaced by receiving php program with " "this is to avoid problems by browsers handling spaces
   private static final String SPATIE_OBS_VIEW              = " ";
   private static final String UNDEFINED                    = "undefined";
   private static final int MAX_AANTAL_JAREN_IN_IMMT        = 5;
   private static final int MAX_AANTAL_WAARNEMERS           = 20;          // zie ook OBSERVER_ROWS in myobserver.java
   private static final int IMMT_3_POSITION_OBSERVER        = 160;         // in IMMT-3 records begin position observer name
   private static final int IMMT_4_POSITION_OBSERVER        = 173;         // in IMMT-4 records begin position observer name
   private static final int IMMT_5_POSITION_OBSERVER        = 173;         // in IMMT-5 records begin position observer name
   private static final int IMMT_POSITION_IMMT_VERSION      = 110;         // immt version voor zowel IMMT-3/IMMT-4/IMMT-5 staat deze op dezelfde pos, zal in de toekomst ook wel zo blijven, maar wel controleren voor volgende IMMT versies
   private static final int IMMT_LIMIT                      = 512000;      // 500 bytes x 1024 = 500 kB
   private final String MOVE_TO_EMAIL                       = "move_to_email";
   private final String MOVE_TO_DISK                        = "move_to_disk";
   private final String LOGS_ZIP                            = "logs.zip";
   private final String JNLP_OFFLINE_FILE                   = "turbowin_jws_offline.jnlp"; // deze file zal aanwezig zijn als alleen in offline mode gewerkt wordt (wordt dan door KNMI er bij geleverd)
   private final String CMD_OFFLINE_FILE                    = "turbowin_plus_offline.cmd"; // deze file zal aanwezig zijn als alleen in offline mode gewerkt wordt (wordt dan door KNMI er bij geleverd)
   
   
   // public constants
   public static final double KNOT_M_S_CONVERSION           = 0.51444444444;
   public static final double M_S_KNOT_CONVERSION           = 1.94384449;
   public static final String OFFLINE_LOGS_DIR              = "logs";           // only used inoffline_mode
   public static final String OFFLINE_AMVER_DIR             = "amver";          // only used in offline_mode
   public static final String TURBOWIN_SYSTEM_LOGS_DIR      = "turbowin_system";// online(web) and offline mode
   public static final int INVALID                          = 9999999;
   public static final int CONFIGURATION_FILE_POS_INHOUD    = 21;               // eg "van wind source        : estimated; true speed and true direction"de pos waar estimated begint
   public static final int MAX_AANTAL_CONFIGURATIEREGELS    = 50;               // in configuratie file (for wind source e.d.)
   public static final String ICONS_DIRECTORY               = "icons/";
   //public static final String ICONS_DIRECTORY_R             = "icons";        // _R van revised "icons" i.p.v. "icons/" WERKT HELAAS NIET BIJ TOOLBAR ICONS, REDEN ONBEKEND
   public static final String CONFIGURATION_FILE            = "configuration.txt";
   public static final String OBSERVER_LOG                  = "observer.log";
   public static final String CAPTAIN_LOG                   = "captain.log";
   public static final String IMMT_LOG                      = "immt.log";
   public static final String SHIP_NAME_TXT                 = "ship name          : ";   // t/m : is 20 characters
   public static final String IMO_NUMBER_TXT                = "imo number         : ";   // t/m : is 20 characters
   public static final String CALL_SIGN_TXT                 = "call sign          : ";   // t/m : is 20 characters
   public static final String MASKED_CALL_SIGN_TXT          = "masked call sign   : ";   // t/m : is 20 characters
   public static final String TIME_ZONE_COMPUTER_TXT        = "time zone computer : ";   // t/m : is 20 characters
   public static final String RECRUITING_COUNTRY_TXT        = "recruiting country : ";   // t/m : is 20 characters
   public static final String METHOD_WAVES_TXT              = "method waves       : ";   // t/m : is 20 characters
   public static final String WIND_SOURCE_TXT               = "wind source        : ";   // t/m : is 20 characters
   public static final String BAROMETER_ABOVE_SLL_TXT       = "barometer above sll: ";   // t/m : is 20 characters
   public static final String BAROMETER_KEEL_TO_SLL_TXT     = "barometer keel-sll : ";   // t/m : is 20 characters
   public static final String PRESSURE_READING_MSL_TXT      = "pressure read msl  : ";   // t/m : is 20 characters
   public static final String AIR_TEMP_EXPOSURE_TXT         = "air temp exposure  : ";   // t/m : is 20 characters
   public static final String SST_EXPOSURE_TXT              = "sst exposure       : ";   // t/m : is 20 characters
   public static final String MAX_HEIGHT_DECK_CARGO_TXT     = "max. height cargo  : ";   // t/m : is 20 characters
   public static final String DIFF_SLL_WL_TXT               = "diff. sll-wl       : ";   // t/m : is 20 characters
   public static final String OBS_EMAIL_RECIPIENT_TXT       = "obs email recipient: ";   // t/m : is 20 characters
   public static final String OBS_EMAIL_SUBJECT_TXT         = "obs email subject  : ";   // t/m : is 20 characters
   public static final String LOGS_DIR_TXT                  = "logs folder        : ";   // t/m : is 20 characters
   public static final String LOGS_EMAIL_RECIPIENT_TXT      = "log email recipient: ";   // t/m : is 20 characters
   public static final String WIND_UNITS_TXT                = "wind units         : ";   // t/m : is 20 characters
   public static final String RS232_INSTRUMENT_TYPE_TXT     = "RS232 instrument   : ";   // t/m : is 20 characters
   public static final String RS232_BITS_PER_SEC_TXT        = "RS232 bps          : ";   // t/m : is 20 characters
   public static final String RS232_DATA_BITS_TXT           = "RS232 data bits    : ";   // t/m : is 20 characters
   public static final String RS232_PARITY_TXT              = "RS232 parity       : ";   // t/m : is 20 characters
   public static final String RS232_STOP_BITS_TXT           = "RS232 stop bits    : ";   // t/m : is 20 characters
   public static final String RS232_PREFERED_COM_PORT_TXT   = "RS232 prefered COM : ";   // t/m : is 20 characters (Windows and Linux)
   public static final String IC_BAROMETER_TXT              = "ic barometer       : ";   // t/m : is 20 characters
   public static final String OBS_FORMAT_TXT                = "obs format         : ";   // t/m : is 20 characters
   public static final String FORMAT_101_ENCRYPTION_TXT     = "format 101 encrypt : ";   // t/m : is 20 characters
   public static final String FORMAT_101_EMAIL_TXT          = "format 101 email   : ";   // t/m : is 20 characters
   public static final String RS232_PREF_COM_PORT_NAME_TXT  = "RS232 pref COM name: ";   // t/m : is 20 characters (OS X)
   public static final String WOW_PUBLISH_TXT               = "WOW publish        : ";   // t/m : is 20 characters 
   public static final String WOW_SITE_ID_TXT               = "WOW site ID        : ";   // t/m : is 20 characters 
   public static final String WOW_PIN_TXT                   = "WOW pin            : ";   // t/m : is 20 characters 
   public static final String WOW_REPORTING_INTERVAL_TXT    = "WOW rep. interval  : ";   // t/m : is 20 characters 
   //public static final String WOW_AVERAGE_BARO_HEIGHT_TXT   = "WOW barom. height  : ";   // t/m : is 20 characters 
   public static final String WOW_APR_AVERAGE_DRAUGHT_TXT   = "WOW/APR draught    : ";   // t/m : is 20 characters (average draught last years)
   public static final String AMOS_MAIL_TXT                 = "AMOS Mail          : ";   // t/m : is 20 characters
   public static final String RS232_GPS_TYPE_TXT            = "RS232 GPS type     : ";   // t/m : is 20 characters
   public static final String RS232_GPS_BITS_PER_SEC_TXT    = "RS232 GPS bps      : ";   // t/m : is 20 characters
   public static final String RS232_GPS_COM_PORT_TXT        = "RS232 GPS COM      : ";   // t/m : is 20 characters (Windows and Linux)
   public static final String RS232_GPS_COM_PORT_NAME_TXT   = "RS232 GPS COM name : ";   // t/m : is 20 characters (OS X)
   public static final String RS232_GPS_SENTENCE_TXT        = "RS232 GPS sentence : ";   // t/m : is 20 characters (RMC or GGA)
   public static final String APR_TXT                       = "APR                : ";   // t/m : is 20 characters
   public static final String APR_REPORTING_INTERVAL_TXT    = "APR rep. interval  : ";   // t/m : is 20 characters
   public static final String UPLOAD_URL_TXT                = "upload URL         : ";   // t/m : is 20 characters
   
   public static final String ESTIMATED_TRUE                = "estimated; true speed and true direction";
   public static final String MEASURED_OFF_BOW              = "measured; apparent speed and apparent direction (OFF THE BOW, clockwise)";
   public static final String MEASURED_TRUE                 = "measured; true speed and true direction";
   public static final String SLING_PSYCHROMETER            = "sling psychrometer";
   public static final String MARINE_SCREEN                 = "marine screen";
   public static final String INTAKE                        = "intake";
   public static final String BUCKET                        = "bucket" ;
   public static final String HULL_CONTACT_SENSOR           = "hull contact sensor";
   public static final String TRAILING_THERMISTOR           = "trailing thermistor";
   public static final String THROUGH_HULL_SENSOR           = "through hull sensor";
   public static final String RADIATION_THERMOMETER         = "radiation thermometer";
   public static final String BAIT_TANKS_THERMOMETER        = "bait tanks thermometer";
   public static final String OTHER                         = "other";
   public static final String TIME_ZONE_COMPUTER_UTC        = "UTC/GMT";
   public static final String TIME_ZONE_COMPUTER_OTHER      = "other";
   public static final String PRESSURE_READING_MSL_YES      = "yes";
   public static final String PRESSURE_READING_MSL_NO       = "no";
   public static final String STATION_DATA                  = "station data";      // see 'mode' in password form
   public static final String EMAIL_SETTINGS                = "email settings";    // see 'mode' in password form
   public static final String LOG_FILES                     = "log files";         // see 'mode' in password form
   public static final String SET_OBS_FORMAT                = "set obs format";    // see 'mode' in password form
   public static final String SET_WOW_APR_SETTINGS          = "set WOW APR settings";  // see 'mode' in password form
   public static final String SET_SERVER_SETTINGS           = "set server settings";   // see 'mode' in password form
   public static final String SEA_AND_SWELL_ESTIMATED       = "wind sea and swell estimated";
   public static final String WAVES_MEASURED_SHIPBORNE      = "waves measured (shipborne wave recorder)";
   public static final String WAVES_MEASURED_BUOY           = "waves measured (buoy)";
   public static final String WAVES_MEASURED_OTHER          = "waves measured (other measurement system)";
   public static final String MUFFIN_LINE_SEPARATOR         = "%";                    // i.p.v. eol deze geeft problemen bij bytes -> String
   public static final String UK_OBS_EMAIL_SUBJECT          = "SXVX88 EGRR ddhhmm";
   public static final String GENERAL_OBS_EMAIL_SUBJECT     = "weather observation";
   public static final String URL_INTERNET_HELP             = "http://projects.knmi.nl/turbowin/webstart/help/";      //"www.knmi.nl/samenw/turbowin/webstart/help/";
   public static final String OFFLINE_HELP_DIR              = "help";                 // wordt alleeen gebruikt indien in offline_mode
   public static final String KNOTS                         = "knots";
   public static final String M_S                           = "m/s";
   public static final String AMVER_SP                      = "amver_sp";             // AMVER sailing plan
   public static final String AMVER_DR                      = "amver_dr";             // AMVER deviation report
   public static final String AMVER_FR                      = "amver_fr";             // AMVER arrival report
   public static final String AMVER_PR                      = "amver_pr";             // AMVER position report
   public static final String FORMAT_FM13                   = "format_fm13";          // obs format
   public static final String FORMAT_AWS                    = "format_aws";           // obs format (if AWS connected)
   public static final String FORMAT_101                    = "format_101";           // obs format
   public static final String FORMAT_101_ENCRYPTION_YES     = "101_encrypt_yes";      // related to obs format
   public static final String FORMAT_101_ENCRYPTION_NO      = "101_encrypt_no";       // related to obs format
   public static final String FORMAT_101_BODY               = "101_email_body";       // related to obs format
   public static final String FORMAT_101_ATTACHEMENT        = "101_email_attachement";// related to obs format
   public static final String FORMAT_101_ROOT_DIR           = "format_101";           // directory for format 101
   public static final String FORMAT_101_TEMP_DIR           = "temp";                 // directory for format 101
   public static final String FORMAT_101_INPUT_FILE         = "format_101.txt";       // file for format 101 (NB outputfile is automatically "HPK_" + input file)
   public static final String THEME_NIMBUS_DAY              = "theme_nimbus_day";
   public static final String THEME_NIMBUS_NIGHT            = "theme_nimbus_night";
   public static final String THEME_NIMBUS_SUNRISE          = "theme_nimbus_sunrise";
   public static final String THEME_NIMBUS_SUNSET           = "theme_nimbus_sunset";
   public enum OSType {WINDOWS, MACOS, LINUX, OTHER};
   public static final String KNMI_UPLOAD_URL               = "http://www.knmi.nl/samenw/turbowin/webstart101/index_webstart_101.php?";
   public static final Integer INVALID_RESPONSE_FORMAT_101  = 710;                    // self defined http response code
   public static final Integer RESPONSE_NO_INTERNET         = 711;                    // self defined http response code (IOException)
   public static final Integer RESPONSE_MALFORMED_URL       = 712;                    // self defined http response code
   public static final Integer OK_RESPONSE_FORMAT_101       = 713;                    // self defined http response code 
   public static final Integer RESPONSE_INTERRUPTION        = 714;                    // self defined http response code (InterruptedException | ExecutionException)
   
   // public var's
   public static final String APPLICATION_NAME              = "TurboWin+";                       // NB DO NOT FORGET TO BUILD ALL AFTER A CHANGE OF THIS STRING
   public static final String APPLICATION_VERSION           = "2.6.5 (build 18-May-2017)"; // NB DO NOT FORGET TO COMPILE ABOUT.JAVA AFTER A CHANGE OF THIS STRING
   public static String application_mode                    = "";                     // e.g. web mode (set in initComponents2 [main.java] and [main_RS232_RS422.java]
   public static String amver_report                        = "";                     // AMVER
   public static String user_dir;
   public static String[] configuratie_regels               = new String[MAX_AANTAL_CONFIGURATIEREGELS];// default values: null
   public static String ship_name                           = "";                     // meta data (mystationdata.java)
   public static String imo_number                          = "";                     // meta data
   public static String call_sign                           = "";                     // meta data
   public static String masked_call_sign                    = "";                     // meta data
   public static String time_zone_computer                  = "";                     // meta data
   public static String recruiting_country                  = "";                     // meta data
   public static String method_waves                        = "";                     // meta data
   public static String wind_source                         = "";                     // meta data
   public static String barometer_above_sll                 = "";                     // meta data
   public static String keel_sll                            = "";                     // meta data
   public static String air_temp_exposure                   = "";                     // meta data
   public static String sst_exposure                        = "";                     // meta data
   public static String max_height_deck_cargo               = "";                     // meta data
   public static String diff_sll_wl                         = "";                     // meta data
   public static String pressure_reading_msl_yes_no         = "";                     // meta data
   public static String logs_dir                            = "";                     // meta data (in this folder e.g. immt.log)
   public static String coded_obs_total                     = "";
   public static String obs_email_recipient                 = "";                     // meta data (myemailsettings.java)
   public static String obs_email_subject                   = "";                     // meta data (myemailsettings.java)
   public static String logs_email_recipient                = "";                     // meta data (myemailsettings.java)
   public static String barometer_instrument_correction     = "";                     // meta data (mybarometer.java)
   public static String mode                                = "";
   public static String wind_units                          = "";
   public static String google_maps_obs_year                = "";                     // for date time in infowindow on google maps
   public static String google_maps_obs_month               = "";                     // for date time in infowindow on google maps
   public static String google_maps_obs_day                 = "";                     // for date time in infowindow on google maps
   public static String google_maps_obs_hour                = "";                     // for date time in infowindow on google maps
   public static String google_maps_obs_wind_dir            = "";                     // for infowindow on google maps
   public static String google_maps_obs_wind_speed          = "";                     // for infowindow on google maps
   public static String google_maps_obs_air_temp            = "";                     // for infowindow on google maps
   public static String google_maps_obs_sst                 = "";                     // for infowindow on google maps
   public static String google_maps_obs_msl_pressure        = "";                     // for infowindow on google maps
   public static String obs_format                          = "";                     // meta data for obs format settings (e.g. FORMAT_101 or FORMAT_FM13)
   public static String obs_101_encryption                  = "";                     // meta data for obs format settings 
   public static String obs_101_email                       = "";                     // meta data for obs format settings 
   public static String upload_URL                          = "";                     // meta data for server settings (used by Output -> Obs to server)
   public static boolean amos_mail                          = false;                  // meta data (myemailsettings.java)
   public static String newline                             = System.getProperty("line.separator");
   public static String theme_mode                          = "";
   public static int x_pos_frame;
   public static int y_pos_frame;
   public static int x_pos_small_frame;
   public static int y_pos_small_frame;
   public static int x_pos_main_frame;
   public static int y_pos_main_frame;
   public static int x_pos_start_frame;
   public static int y_pos_start_frame;
   public static int x_pos_amver_frame;
   public static int y_pos_amver_frame;
   public static int x_pos_calculator_frame;
   public static int y_pos_calculator_frame;
   public static int screenWidth;
   public static int screenHeight;
   public static boolean in_next_sequence                   = false;
   public static boolean offline_mode;
   public static boolean offline_mode_via_jnlp;
   public static boolean offline_mode_via_cmd;
   public static boolean tray_icon_clicked;
   public static boolean use_system_date_time_for_updating  = false;      // NB if you start with true then if the data/comfirmation box pop-ups and the user disagree the time is still inserted by the timer loop
   public static TrayIcon trayIcon;
   
   
   // private var's
   private SingleInstanceService sis                        = null;        // for checking only one instance is running
   private SISListener sisL                                 = null;        // for checking only one instance is running
   private static String output_dir                         = null;
   private static String hulp_dir                           = "";          // for writing configuration.txt file in user_dir (system defined) AND logs_dir (user defined) (backup for muffin)
   private URL url_php;
   private String obs_write                                 = "";
   private String output_file                               = "";
   private String volledig_path_dstFilename_immt            = "";
   private String volledig_path_srcFilename_immt            = "";
   private String volledig_path_backup_srcFilename_immt     = "";
   private String volledig_path_dstFilename_captain         = "";
   private String volledig_path_srcFilename_captain         = "";
   private String volledig_path_backup_srcFilename_captain  = "";
   private String temp_logs_dir                             = "";
   //private String last_record                               = "";
   private static String last_record                               = "";
   private String[] jaar_substring_array                    = new String [MAX_AANTAL_JAREN_IN_IMMT];
   private String[] observername_array                      = new String [MAX_AANTAL_WAARNEMERS];
   public static GregorianCalendar cal_systeem_datum_tijd;             
   public static GregorianCalendar cal_systeem_datum_tijd_UTC;
   public static GregorianCalendar cal_systeem_datum_tijd_LT;
   public static FORMAT_101 format_101_class;
   
   // RS232-RS422
   //
   private main_RS232_RS422 RS232_RS422;
   
   public static final String SERIAL_CONNECTION                    = "serial connection";  // see mode in password form
   //public static final String WIFI_CONNECTION                      = "WiFi connection";  // see mode in password form
   public static final String MODE_PRESSURE                        = "mode_pressure";
   public static final String MODE_AIRTEMP                         = "mode_airtemp";
   public static final String MODE_SST                             = "mode_sst";
   public static final String MODE_WIND_SPEED                      = "mode_windspeed";
   public static final String MODE_WIND_DIR                        = "mode_winddir";
	public static final String MODE_ALL_PARAMETERS                  = "mode_all_parameters";
   
   public static final int RECORD_LENGTE_PTB330                    = 46;
   public static final int RECORD_DATUM_TIJD_BEGIN_POS_PTB330      = 34; 
   public static final int RECORD_MINUTEN_BEGIN_POS_PTB330         = RECORD_DATUM_TIJD_BEGIN_POS_PTB330 + 10;
   public static final int RECORD_P_BEGIN_POS_PTB330               = 0;
   public static final int RECORD_a_BEGIN_POS_PTB330               = 30; 
   public static final int RECORD_ppp_BEGIN_POS_PTB330             = 24; 
   
   public static final int RECORD_LENGTE_PTB220                    = 36;
   public static final int RECORD_DATUM_TIJD_BEGIN_POS_PTB220      = 24;
   public static final int RECORD_MINUTEN_BEGIN_POS_PTB220         = RECORD_DATUM_TIJD_BEGIN_POS_PTB220 + 10;
   public static final int RECORD_P_BEGIN_POS_PTB220               = 0;
   public static final int RECORD_a_BEGIN_POS_PTB220               = 22;
   public static final int RECORD_ppp_BEGIN_POS_PTB220             = 16;
   
   public static int RS232_GPS_sentence                            = 0;   //  0 = no existing value; 1 = RMC ; 2 = GGA
   public static int RS232_GPS_connection_mode                     = 0;   //  0 = default = no GPS connected via RS232
   public static int RS232_connection_mode                         = 0;   //  0 = default = no meteorological instrument connected via RS232
   public static int bits_per_second                               = 0;   //  0 = meteorological instrument no existing value
   public static int GPS_bits_per_second                           = 0;   //  0 = GPS no existing value
   public static int data_bits                                     = 0;   //  0 = meteorological instrument no existing value
   public static int parity                                        = 99;  // 99 = meteorological instrument no existing value
   public static int stop_bits                                     = 0;   //  0 = meteorological instrument no existing value 
   public static int flow_control                                  = SerialPort.FLOWCONTROL_NONE;
   public static String prefered_COM_port_number                   = "";  // meteorological instrument Windows and Linux
   public static String prefered_GPS_COM_port_number               = "";  // GPS Windows and Linux
   public static String prefered_COM_port_name                     = "";  // meteorological instrument OS X
   public static String prefered_GPS_COM_port_name                 = "";  // GPS OS X
   public static String prefered_COM_port                          = "";  // meteorological instrument generic (Windows/Linux/OS X)
   public static String prefered_GPS_COM_port                      = "";  // GPS generic (Windows/Linux/OS X)
   public static String defaultPort                                = null;
   public static String sensor_data_record_obs_pressure            = "";
   public static String sensor_data_record_obs_ppp                 = "";
   public static String sensor_data_record_obs_a                   = "";
   public static String mode_grafiek;                              // first initialisation in initComponents2() later in Functions: Graphs_Airtemp_Sensor_Data_actionPerformed() etc.
   public static SimpleDateFormat sdf3;
   public static SimpleDateFormat sdf4;
   public static SimpleDateFormat sdf_tsl_1;                              // TurboWin system logs
   public static SimpleDateFormat sdf_tsl_2;                              // TurboWin system logs
   public static boolean sensor_data_file_ophalen_timer_is_gecreeerd = false;              // static!
   
   
   
   // NB parameters like position, date time and air pressure etc. are always not editable by the observer in AWS mode (so not necessary to keep a boolean record of these parameters)
   public static boolean date_from_AWS_present                     = false;
   public static boolean time_from_AWS_present                     = false;
   public static boolean latitude_from_AWS_present                 = false;
   public static boolean longitude_from_AWS_present                = false;
   public static boolean COG_from_AWS_present                      = false;
   public static boolean SOG_from_AWS_present                      = false;
   public static boolean true_heading_from_AWS_present             = false;
   public static boolean pressure_sensor_level_from_AWS_present    = false;
   public static boolean pressure_MSL_from_AWS_present             = false;
   public static boolean pressure_tendency_from_AWS_present        = false;
   public static boolean pressure_characteristic_from_AWS_present  = false;
   public static boolean air_temp_from_AWS_present                 = false;
   public static boolean rh_from_AWS_present                       = false;
   public static boolean SST_from_AWS_present                      = false;
   public static boolean relative_wind_speed_from_AWS_present      = false;
   public static boolean relative_wind_dir_from_AWS_present        = false;
   public static boolean true_wind_speed_from_AWS_present          = false;
   public static boolean true_wind_dir_from_AWS_present            = false;
   public static boolean displayed_aws_data_obsolate               = false;
   
   public static final int NUMBER_COM_PORTS                        = 20;     // used by checking COM ports meteorological instrument (barometer, EUCAWS) and also for GPS
   public static final int LENGTE_SMD_STRING                       = 14;//14;//1024;//20;  // 20 is willekeurig, moet nog precies bepaald worden
   public static final int TOTAL_NUMBER_RECORD_COMMAS              = 27;
   public static final int TOTAL_NUMBER_RECORD_COMMAS_MINTAKA      = 3;
   public static final int TOTAL_NUMBER_RECORD_COMMAS_MINTAKA_STAR = 8;
   public static final int DATE_COMMA_NUMBER                       = 1;   // reading from EUCAWS sensor data file
   public static final int TIME_COMMA_NUMBER                       = 2;   //             --"--
   public static final int LATITUDE_COMMA_NUMBER                   = 3;   //             --"--
   public static final int LONGITUDE_COMMA_NUMBER                  = 4;   //             --"-- 
   public static final int COG_COMMA_NUMBER                        = 5;   //             --"--
   public static final int SOG_COMMA_NUMBER                        = 6;   //             --"--
   public static final int HEADING_COMMA_NUMBER                    = 7;   //             --"--
   public static final int PRESSURE_SENSOR_HEIGHT_COMMA_NUMBER     = 8;   //             --"--
   public static final int PRESSURE_MSL_COMMA_NUMBER               = 9;   //             --"--
   public static final int PRESSURE_TENDENCY_COMMA_NUMBER          = 10;  //             --"--
   public static final int PRESSURE_CHARACTERISTIC_COMMA_NUMBER    = 11;  //             --"--
   public static final int AIR_TEMP_COMMA_NUMBER                   = 12;  //             --"--
   public static final int HUMIDITY_COMMA_NUMBER                   = 13;  //             --"--
   public static final int SST_COMMA_NUMBER                        = 14;  //             --"--
   public static final int RELATIVE_WIND_SPEED_COMMA_NUMBER        = 15;  //             --"--
   public static final int RELATIVE_WIND_DIR_COMMA_NUMBER          = 16;  //             --"--
   public static final int TRUE_WIND_SPEED_COMMA_NUMBER            = 17;  //             --"--
   public static final int TRUE_WIND_DIR_COMMA_NUMBER              = 18;  //             --"--
   public static final int TRUE_WIND_GUST_COMMA_NUMBER             = 19;  //             --"--
   // NB --- max wind gust dir                                       20
   // NB --- supply voltage                                          21
   // NB --- internal temperature                                    22
   public static final int VOT_COMMA_NUMBER                        = 23; //              --"--
   // NB --- spare                                                   24
   // NB --- spare                                                   25
   // NB --- spare                                                   26
   
   
   public static int type_record_lengte                             = 0;
   public static int type_record_datum_tijd_begin_pos               = 0;
   public static int type_record_pressure_begin_pos                 = 0;
   public static int type_record_a_begin_pos                        = 0;
   public static int type_record_ppp_begin_pos                      = 0;
  
   public static String[] portList;
   public static GregorianCalendar obs_file_datum_tijd;
   public static SerialPort serialPort;
   public static String total_string;     
   
   private RS232_view graph_form;
   
   public static final Color input_color_from_aws                   = Color.RED;  // color for text fields if input was measured by AWS (manually input of that text field disabled)
   public static final Color input_color_from_observer              = Color.BLACK;
   public static final Color obsolate_color_data_from_aws           = Color.GRAY;
   
   // WOW
   public static boolean WOW                                        = false;      // meta data  yes or no publish on WOW (WeatherObservationsWebsite)
   public static String WOW_site_id                                 = "";         // meta data
   public static String WOW_site_pin                                = "";         // meya data
   public static String WOW_reporting_interval                      = "";         // meta data
   //public static String WOW_average_height_barometer                = "";         // meta data
   public final static String WOW_REPORTING_INTERVAL_MANUAL         = "44444";    // meta data (44444 is just a number)
   
   // APR (Automated Pressure Reports)
   public static boolean APR                                        = false;      // meta data
   public static String APR_reporting_interval                      = "";         // meta data
   
   // WOW and APR
   public static String WOW_APR_average_draught                     = "";         // meta data
 
   
   SystemTray tray = null;
   JPopupMenu popup_input;
   
}




