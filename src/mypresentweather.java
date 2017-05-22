import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
/*
 * mypresentweather.java
 *
 * Created on 10 mei 2007, 12:06
 */

/**
 *
 * @author  stam
 */
public class mypresentweather extends javax.swing.JFrame {
   
   /** Creates new form mypresentweather */
   public mypresentweather() {
      initComponents();
      initComponents2();
      initSynopparameters();
      setLocation(main.x_pos_frame, main.y_pos_frame);
   }



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void initComponents2()
   {
      // hide Back/Stop buttons if not in next_screen_mode
      if (main.in_next_sequence == false)
      {
         jButton4.setEnabled(false);                         // back button
         jButton5.setEnabled(false);                         // stop button
      }


      if (main.offline_mode == true)
      {
         jButton3.setText("Help");
      }
   }



  /***********************************************************************************************/
  /*                                                                                             */
  /*                                                                                             */
  /*                                                                                             */
  /***********************************************************************************************/
   private void initSynopparameters() 
   {

      // no precip. present weather
      jList1.setModel(new javax.swing.AbstractListModel() 
      {
         String[] strings = { "not determined",                                                           // index 0
	                           "thunder audible during the last 10 minutes",                               // index 1
                              "fog (or ice-fog) at time of obs",                                          // index 2
                              "duststorm, sandstorm, drifting or blowing snow",                           // index 3
                              "precipitation, fog or thunderstorm within last hour; not at time of obs",  // index 4
                              "squalls or funnel cloud(s) within last hour or at time of obs",            // index 5
                              "lightning or precipitation within sight but not at station",               // index 6 
                              "shallow fog or mist",                                                      // index 7
                              "haze, dust, sand, smoke or blowing spray",                                 // index 8 
                              "phenomena without significance" };                                         // index 9
            @Override
         public int getSize() { return strings.length; }
            @Override
         public Object getElementAt(int i) { return strings[i]; }
      });
      
      // precip. present weather
      jList2.setModel(new javax.swing.AbstractListModel() 
      {
         String[] strings = { "not determined",                                                           // index 0
                              "thunderstorm* at time of obs",                                             // index 1
                              "thunderstorm* during preceding hour but not at time of obs",               // index 2 
                              "showery precipitation, no thunder at time of obs or during preceding hr",  // index 3
                              "solid precipitation, not in showers",                                      // index 4
                              "rain",                                                                     // index 5 
                              "drizzle" };                                                                // index 6
            @Override
         public int getSize() { return strings.length; }
            @Override
         public Object getElementAt(int i) { return strings[i]; }
      });
 
      // specific present weather 
      model3 = new javax.swing.DefaultListModel();
      jList3.setModel(model3); 
      
      
      // put back earlier inserted values  
      //JOptionPane.showMessageDialog(null, present_weather , "present-weather", JOptionPane.INFORMATION_MESSAGE);
      if (ww_code.compareTo("") != 0)                             // nothing was selected before
      {
         if (ww_code.equals("//"))                                // "not determined" was selected before
         {
            int_ww_code = main.INVALID;
            
            jList1.clearSelection();                              // NO precip general weather
            jList2.clearSelection();                              // precip general weather
            jList3.clearSelection();                              // specific weather
      
            model3.removeAllElements();
            model3.addElement(ww_not_determined);
  
            jList3.setSelectedValue(present_weather, false);
         } // if (ww_code.equals("//"))
         else 
         {
            try 
            {
               int_ww_code = Integer.parseInt(ww_code.trim());
            } 
            catch (NumberFormatException e){/* ... */}
         } // else
         
         
         if (int_ww_code >= 0 && int_ww_code <= 49) 
         {
            if (int_ww_code >= 0 && int_ww_code <= 3)
               jList1.setSelectedIndex(9);                     // phenomena without significance
            else if (int_ww_code >= 4 && int_ww_code <= 9)
               jList1.setSelectedIndex(8);                     // haze, dust, sand, smoke or blowing spray
            else if (int_ww_code >= 10 && int_ww_code <= 12)
               jList1.setSelectedIndex(7);                     // shallow fog or mist
            else if (int_ww_code >= 13 && int_ww_code <= 16)
               jList1.setSelectedIndex(6);                     // lightning or precipitation within sight but not at station
            else if (int_ww_code == 17)
               jList1.setSelectedIndex(1);                     // thunder audible during the last 10 minutes
            else if (int_ww_code >= 18 && int_ww_code <= 19)   
               jList1.setSelectedIndex(5);                     // squalls or funnel cloud(s) within last hour or at time of obs
            else if (int_ww_code >= 20 && int_ww_code <= 29)     
               jList1.setSelectedIndex(4);                     // precipitation, fog or thunderstorm within last hour; not at time of obs
            else if (int_ww_code >= 30 && int_ww_code <= 39)      
               jList1.setSelectedIndex(3);                     // duststorm, sandstorm, drifting or blowing snow
            else if (int_ww_code >= 40 && int_ww_code <= 49)  
               jList1.setSelectedIndex(2);                     // fog (or ice-fog) at time of obs
                
            Fill_specific_weather_listbox_from_no_precip();
            jList3.setSelectedValue(present_weather, false);
         } // if (int_ww_code >= 0 && int_ww_code <= 49) 
         else if (int_ww_code >= 50 && int_ww_code <= 99) 
         {
            if (int_ww_code >= 50 && int_ww_code <= 59)
               jList2.setSelectedIndex(6);                     // drizzle
            else if (int_ww_code >= 60 && int_ww_code <= 69)
               jList2.setSelectedIndex(5);                     // rain
            else if (int_ww_code >= 70 && int_ww_code <= 79)
               jList2.setSelectedIndex(4);                     // solid precipitation, not in showers
            else if (int_ww_code >= 80 && int_ww_code <= 90)
               jList2.setSelectedIndex(3);                     // showery precipitation, no thunder at time of obs or during preceding hr
            else if (int_ww_code >= 91 && int_ww_code <= 94)
               jList2.setSelectedIndex(2);                     // thunderstorm* during preceding hour but not at time of obs
            else if (int_ww_code >= 95 && int_ww_code <= 99)
               jList2.setSelectedIndex(1);                     // thunderstorm* at time of obs
             
            Fill_specific_weather_listbox_from_precip();
            jList3.setSelectedValue(present_weather, false);
         } // else if (int_ww_code >= 50 && int_ww_code <= 99) 
         
      } // if (ww_code.compareTo("") != 0)
   }
   
   
   
   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jScrollPane1 = new javax.swing.JScrollPane();
      jList1 = new javax.swing.JList();
      jScrollPane2 = new javax.swing.JScrollPane();
      jList2 = new javax.swing.JList();
      jScrollPane3 = new javax.swing.JScrollPane();
      jList3 = new javax.swing.JList();
      jSeparator1 = new javax.swing.JSeparator();
      jButton1 = new javax.swing.JButton();
      jButton2 = new javax.swing.JButton();
      jButton3 = new javax.swing.JButton();
      jLabel1 = new javax.swing.JLabel();
      jLabel2 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      jLabel4 = new javax.swing.JLabel();
      jLabel5 = new javax.swing.JLabel();
      jLabel6 = new javax.swing.JLabel();
      jLabel7 = new javax.swing.JLabel();
      jLabel8 = new javax.swing.JLabel();
      jLabel9 = new javax.swing.JLabel();
      jLabel10 = new javax.swing.JLabel();
      jLabel11 = new javax.swing.JLabel();
      jLabel12 = new javax.swing.JLabel();
      jLabel13 = new javax.swing.JLabel();
      jLabel14 = new javax.swing.JLabel();
      jLabel15 = new javax.swing.JLabel();
      jLabel16 = new javax.swing.JLabel();
      jLabel17 = new javax.swing.JLabel();
      jLabel18 = new javax.swing.JLabel();
      jLabel19 = new javax.swing.JLabel();
      jLabel21 = new javax.swing.JLabel();
      jLabel20 = new javax.swing.JLabel();
      jLabel22 = new javax.swing.JLabel();
      jButton4 = new javax.swing.JButton();
      jButton5 = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle("Present weather");
      setResizable(false);

      jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
      jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
         public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            no_precip_valueChanged(evt);
         }
      });
      jScrollPane1.setViewportView(jList1);

      jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
      jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
         public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            precip_valueChanged(evt);
         }
      });
      jScrollPane2.setViewportView(jList2);

      jList3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
      jScrollPane3.setViewportView(jList3);

      jButton1.setText("OK");
      jButton1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            OK_button_actionPerformed(evt);
         }
      });

      jButton2.setText("Cancel");
      jButton2.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Cancel_button_actionPerformed(evt);
         }
      });

      jButton3.setText("Internet");
      jButton3.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Internet_button_actionPerformed(evt);
         }
      });

      jLabel1.setText("no precipitation (DRY) at station at time of obs");

      jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
      jLabel2.setText("precipitation (WET) at station at time of obs");

      jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel3.setText("specific weather condition");

      jLabel4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel4.setForeground(new java.awt.Color(0, 0, 255));
      jLabel4.setText("for all general and");

      jLabel5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel5.setForeground(new java.awt.Color(0, 0, 255));
      jLabel5.setText("specific weather");

      jLabel6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel6.setForeground(new java.awt.Color(0, 0, 255));
      jLabel6.setText("conditions: the ");

      jLabel7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel7.setForeground(new java.awt.Color(0, 0, 255));
      jLabel7.setText("topmost applicable");

      jLabel8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel8.setForeground(new java.awt.Color(0, 0, 255));
      jLabel8.setText("weather condition");

      jLabel9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel9.setForeground(new java.awt.Color(0, 0, 255));
      jLabel9.setText("shall be selected");

      jLabel10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel10.setForeground(new java.awt.Color(0, 0, 255));
      jLabel10.setText("NOTE");

      jLabel11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel11.setForeground(new java.awt.Color(0, 0, 255));
      jLabel11.setText("present weather");

      jLabel12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel12.setForeground(new java.awt.Color(0, 0, 255));
      jLabel12.setText("shall describe");

      jLabel13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel13.setForeground(new java.awt.Color(0, 0, 255));
      jLabel13.setText("the weather at");

      jLabel14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel14.setForeground(new java.awt.Color(0, 0, 255));
      jLabel14.setText("time of obs or ");

      jLabel15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel15.setForeground(new java.awt.Color(0, 0, 255));
      jLabel15.setText("(where specially");

      jLabel16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel16.setForeground(new java.awt.Color(0, 0, 255));
      jLabel16.setText("mentioned)");

      jLabel17.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel17.setForeground(new java.awt.Color(0, 0, 255));
      jLabel17.setText("during  the");

      jLabel18.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel18.setForeground(new java.awt.Color(0, 0, 255));
      jLabel18.setText("period of one");

      jLabel19.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel19.setForeground(new java.awt.Color(0, 0, 255));
      jLabel19.setText("hr immediately");

      jLabel21.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
      jLabel21.setForeground(new java.awt.Color(0, 0, 255));
      jLabel21.setText("preceding it");

      jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel20.setText("general weather condition");

      jLabel22.setText("*thunder heard; lightning may or may not be seen");

      jButton4.setText("Back");
      jButton4.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Back_button_actionPerformed(evt);
         }
      });

      jButton5.setText("Stop");
      jButton5.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Stop_button_actionPerformed(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addGap(160, 160, 160)
                  .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(jLabel1))
               .addGroup(layout.createSequentialGroup()
                  .addGap(333, 333, 333)
                  .addComponent(jLabel3))
               .addGroup(layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(jSeparator1)
                     .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                           .addGroup(layout.createSequentialGroup()
                              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                 .addComponent(jLabel9)
                                 .addComponent(jLabel8)
                                 .addComponent(jLabel7)
                                 .addComponent(jLabel6)
                                 .addComponent(jLabel5)
                                 .addComponent(jLabel4)
                                 .addComponent(jLabel10))
                              .addGap(17, 17, 17)
                              .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addGap(17, 17, 17)
                              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                 .addComponent(jLabel13)
                                 .addComponent(jLabel11)
                                 .addComponent(jLabel21)
                                 .addComponent(jLabel16)
                                 .addComponent(jLabel15)
                                 .addComponent(jLabel14)
                                 .addComponent(jLabel12)
                                 .addComponent(jLabel18)
                                 .addComponent(jLabel17)
                                 .addComponent(jLabel19)))
                           .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                              .addGroup(layout.createSequentialGroup()
                                 .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                 .addGap(18, 18, 18)
                                 .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                              .addGroup(layout.createSequentialGroup()
                                 .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                                 .addGap(34, 34, 34)
                                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 10, Short.MAX_VALUE)))))
            .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel1)
                     .addComponent(jLabel2)))
               .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
               .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
            .addGap(4, 4, 4)
            .addComponent(jLabel22)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                  .addComponent(jLabel10)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jLabel4)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jLabel5)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jLabel6)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jLabel7)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jLabel8)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jLabel9)
                  .addGap(53, 53, 53))
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(layout.createSequentialGroup()
                     .addComponent(jLabel11)
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                     .addComponent(jLabel12)
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                     .addComponent(jLabel13)
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                     .addComponent(jLabel14)
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                     .addComponent(jLabel15)
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                     .addComponent(jLabel16)
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                     .addComponent(jLabel17)
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                     .addComponent(jLabel18)
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                     .addComponent(jLabel19)
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                     .addComponent(jLabel21))
                  .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(15, 15, 15)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(15, 15, 15))
      );

      jLabel18.getAccessibleContext().setAccessibleName(" hour immed");

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

      if (main.in_next_sequence == true)
      {
         next_screen();
      }
   }//GEN-LAST:event_Cancel_button_actionPerformed


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Reset_All_PresentWeather_Vars()
   {
      // scope this module + myturbowin.java main module (all of type: static) 
      ww_code         = "";
      present_weather = "";
   
      // local (scope only this file, no static) 
      int_ww_code     = main.INVALID;
      checks_ok       = false;   
      
      // onderstaande DUS NIET resetten
      //general_weather_condition_selected = false;

      /* present weather velden op hoofdscherm overzicht updaten */
      main.present_weather_fields_update();
   }


  /***********************************************************************************************/
  /*                                                                                             */
  /*                                                                                             */
  /*                                                                                             */
  /***********************************************************************************************/
   private void OK_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_button_actionPerformed
// TODO add your handling code here:
      
//      present_weather = (String)jList3.getSelectedValue();
//      //JOptionPane.showMessageDialog(null, ww , "ww", JOptionPane.INFORMATION_MESSAGE);
//      
//      
//      if ((present_weather == null) && (general_weather_condition_selected == true))
//      {
//          // if a GENERAL weather condition was selected but not a specific wether condition -> warning
//          JOptionPane.showMessageDialog(null, "specific weather condition not selected",  main.APPLICATION_NAME + " warning", JOptionPane.WARNING_MESSAGE);
//          Reset_All_PresentWeather_Vars();
//      }
//     else if ((present_weather == null) && (general_weather_condition_selected == false))
//      {
//          // nothing at all selected -> no warning
//          Reset_All_PresentWeather_Vars();
//      }        
//      else
//      {
//          checks_ok = true;
//      }
      
      
//////////////     
      /* initialisation */
      Reset_All_PresentWeather_Vars();
      
      /* initialisation */                                  // NB checks_ok was set to false in Reset_All_PresentWeather_Vars();
      checks_ok = true;
      
      present_weather = (String)jList3.getSelectedValue();  // if nothing selected -> returns null

      
      if ((present_weather == null) && (general_weather_condition_selected == true))
      {
          // if a GENERAL weather condition was selected but not a specific wether condition -> warning
          JOptionPane.showMessageDialog(null, "specific weather condition not selected",  main.APPLICATION_NAME + " warning", JOptionPane.WARNING_MESSAGE);
          Reset_All_PresentWeather_Vars();
      }
      
      
      
      
/////////////      
      
      if (checks_ok == true)
      {    
         if (present_weather != null)
         {
         // search for the corresponding ww code
         if (present_weather.equals(ww_0))
            ww_code = "00";
         else if (present_weather.equals(ww_1))
            ww_code = "01";
         else if (present_weather.equals(ww_2))
            ww_code = "02";
         else if (present_weather.equals(ww_3))
            ww_code = "03";
         else if (present_weather.equals(ww_4))
            ww_code = "04";
         else if (present_weather.equals(ww_5))
            ww_code = "05";
         else if (present_weather.equals(ww_6))
            ww_code = "06";
         else if (present_weather.equals(ww_7))
            ww_code = "07";
         else if (present_weather.equals(ww_9))
            ww_code = "09";
         else if (present_weather.equals(ww_10))
            ww_code = "10";
          
         else if (present_weather.equals(ww_11))
            ww_code = "11";
         else if (present_weather.equals(ww_12))
            ww_code = "12";
         else if (present_weather.equals(ww_13))
            ww_code = "13";
         else if (present_weather.equals(ww_14))
            ww_code = "14";
         else if (present_weather.equals(ww_15))
            ww_code = "15";
         else if (present_weather.equals(ww_16))
            ww_code = "16";
         else if (present_weather.equals(ww_17))
            ww_code = "17";
         else if (present_weather.equals(ww_18))
            ww_code = "18";
         else if (present_weather.equals(ww_19))
            ww_code = "19";
         else if (present_weather.equals(ww_20))
            ww_code = "20";
          
         else if (present_weather.equals(ww_21))
            ww_code = "21";
         else if (present_weather.equals(ww_22))
            ww_code = "22";
         else if (present_weather.equals(ww_23))
            ww_code = "23";
         else if (present_weather.equals(ww_24))
            ww_code = "24";
         else if (present_weather.equals(ww_25))
            ww_code = "25";
         else if (present_weather.equals(ww_26))
            ww_code = "26";
         else if (present_weather.equals(ww_27))
            ww_code = "27";
         else if (present_weather.equals(ww_28))
            ww_code = "28";
         else if (present_weather.equals(ww_29))
            ww_code = "29";
         else if (present_weather.equals(ww_30))
            ww_code = "30";
          
         else if (present_weather.equals(ww_31))
            ww_code = "31";
         else if (present_weather.equals(ww_32))
            ww_code = "32";
         else if (present_weather.equals(ww_33))
            ww_code = "33";
         else if (present_weather.equals(ww_34))
            ww_code = "34";
         else if (present_weather.equals(ww_35))
            ww_code = "35";
         else if (present_weather.equals(ww_36))
            ww_code = "36";
         else if (present_weather.equals(ww_37))
            ww_code = "37";
         else if (present_weather.equals(ww_38))
            ww_code = "38";
         else if (present_weather.equals(ww_39))
            ww_code = "39";
         else if (present_weather.equals(ww_40))
            ww_code = "40";
          
         else if (present_weather.equals(ww_41))
            ww_code = "41";
         else if (present_weather.equals(ww_42))
            ww_code = "42";
         else if (present_weather.equals(ww_43))
            ww_code = "43";
         else if (present_weather.equals(ww_44))
            ww_code = "44";
         else if (present_weather.equals(ww_45))
            ww_code = "45";
         else if (present_weather.equals(ww_46))
            ww_code = "46";
         else if (present_weather.equals(ww_47))
            ww_code = "47";
         else if (present_weather.equals(ww_48))
            ww_code = "48";
         else if (present_weather.equals(ww_49))
            ww_code = "49";
         else if (present_weather.equals(ww_50))
            ww_code = "50";
          
         else if (present_weather.equals(ww_51))
            ww_code = "51";
         else if (present_weather.equals(ww_52))
            ww_code = "52";
         else if (present_weather.equals(ww_53))
            ww_code = "53";
         else if (present_weather.equals(ww_54))
            ww_code = "54";
         else if (present_weather.equals(ww_55))
            ww_code = "55";
         else if (present_weather.equals(ww_56))
            ww_code = "56";
         else if (present_weather.equals(ww_57))
            ww_code = "57";
         else if (present_weather.equals(ww_58))
            ww_code = "58";
         else if (present_weather.equals(ww_59))
            ww_code = "59";
         else if (present_weather.equals(ww_60))
            ww_code = "60";
          
         else if (present_weather.equals(ww_61))
            ww_code = "61";
         else if (present_weather.equals(ww_62))
            ww_code = "62";
         else if (present_weather.equals(ww_63))
            ww_code = "63";
         else if (present_weather.equals(ww_64))
            ww_code = "64";
         else if (present_weather.equals(ww_65))
            ww_code = "65";
         else if (present_weather.equals(ww_66))
            ww_code = "66";
         else if (present_weather.equals(ww_67))
            ww_code = "67";
         else if (present_weather.equals(ww_68))
            ww_code = "68";
         else if (present_weather.equals(ww_69))
            ww_code = "69";
         else if (present_weather.equals(ww_70))
            ww_code = "70";
          
         else if (present_weather.equals(ww_71))
            ww_code = "71";
         else if (present_weather.equals(ww_72))
            ww_code = "72";
         else if (present_weather.equals(ww_73))
            ww_code = "73";
         else if (present_weather.equals(ww_74))
            ww_code = "74";
         else if (present_weather.equals(ww_75))
            ww_code = "75";
         else if (present_weather.equals(ww_76))
            ww_code = "76";
         else if (present_weather.equals(ww_77))
            ww_code = "77";
         else if (present_weather.equals(ww_78))
            ww_code = "78";
         else if (present_weather.equals(ww_79))
            ww_code = "79";
         else if (present_weather.equals(ww_80))
            ww_code = "80";
          
         else if (present_weather.equals(ww_81))
            ww_code = "81";
         else if (present_weather.equals(ww_82))
            ww_code = "82";
         else if (present_weather.equals(ww_83))
            ww_code = "83";
         else if (present_weather.equals(ww_84))
            ww_code = "84";
         else if (present_weather.equals(ww_85))
            ww_code = "85";
         else if (present_weather.equals(ww_86))
            ww_code = "86";
         else if (present_weather.equals(ww_87))
            ww_code = "87";
         else if (present_weather.equals(ww_88))
            ww_code = "88";
         else if (present_weather.equals(ww_89))
            ww_code = "89";
         else if (present_weather.equals(ww_90))
            ww_code = "90";
          
         else if (present_weather.equals(ww_91))
            ww_code = "91";
         else if (present_weather.equals(ww_92))
            ww_code = "92";
         else if (present_weather.equals(ww_93))
            ww_code = "93";
         else if (present_weather.equals(ww_94))
            ww_code = "94";
         else if (present_weather.equals(ww_95))
            ww_code = "95";
         else if (present_weather.equals(ww_96))
            ww_code = "96";
         else if (present_weather.equals(ww_97))
            ww_code = "97";
         else if (present_weather.equals(ww_98))
            ww_code = "98";
         else if (present_weather.equals(ww_99))
            ww_code = "99";
          
         else if (present_weather.equals(ww_not_determined))
            ww_code = "//";
         
         else
            ww_code = "";
         }
      } // if (checks_ok == true)

      
      // close if checks ok or if nothing at all was selected
      //if ( (checks_ok == true) || ((present_weather == null) && (general_weather_condition_selected == false)) )
      if (checks_ok == true)   
      {
         /* update present weather fields on main screen */
         if (present_weather != null)
         {
            main.present_weather_fields_update();
         }      
          
         /* close input page */
         setVisible(false);
         dispose();

         /* next screen if in_next_screen mode */
         if (main.in_next_sequence == true)
         {
            next_screen();
         }
         
      } //   if (checks_ok == true) 
   }//GEN-LAST:event_OK_button_actionPerformed


  /***********************************************************************************************/
  /*                                                                                             */
  /*                                                                                             */
  /*                                                                                             */
  /***********************************************************************************************/
   private void precip_valueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_precip_valueChanged
       // TODO add your handling code here:
      // NB at listbox use valueChanged (and not mouseClicked or keyReleased)

      Fill_specific_weather_listbox_from_precip();
      general_weather_condition_selected = true;

   }//GEN-LAST:event_precip_valueChanged


  /***********************************************************************************************/
  /*                                                                                             */
  /*                                                                                             */
  /*                                                                                             */
  /***********************************************************************************************/
   private void no_precip_valueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_no_precip_valueChanged
       // TODO add your handling code here:
      // NB at listbox use valueChanged (and not mouseClicked or keyReleased)

      Fill_specific_weather_listbox_from_no_precip();
      general_weather_condition_selected = true;

   }//GEN-LAST:event_no_precip_valueChanged


  /***********************************************************************************************/
  /*                                                                                             */
  /*                                                                                             */
  /*                                                                                             */
  /***********************************************************************************************/
   private void Internet_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Internet_button_actionPerformed
      // TODO add your handling code here:
      main.internet_mouseClicked(PRESENTWEATHER_HELP_DIR);
   }//GEN-LAST:event_Internet_button_actionPerformed


  /***********************************************************************************************/
  /*                                                                                             */
  /*                                                                                             */
  /*                                                                                             */
  /***********************************************************************************************/
   private void Back_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Back_button_actionPerformed
      // TODO add your handling code here:
      setVisible(false);
      dispose();

      previous_screen();
   }//GEN-LAST:event_Back_button_actionPerformed


  /***********************************************************************************************/
  /*                                                                                             */
  /*                                                                                             */
  /*                                                                                             */
  /***********************************************************************************************/
   private void Stop_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Stop_button_actionPerformed
      // TODO add your handling code here:
      setVisible(false);
      dispose();

      main.in_next_sequence = false;
   }//GEN-LAST:event_Stop_button_actionPerformed


  /***********************************************************************************************/
  /*                                                                                             */
  /*                                                                                             */
  /*                                                                                             */
  /***********************************************************************************************/
  private void previous_screen()
  {
     myvisibility form = new myvisibility();
     form.setSize(800, 600);
     form.setVisible(true);
  }


  /***********************************************************************************************/
  /*                                                                                             */
  /*                                                                                             */
  /*                                                                                             */
  /***********************************************************************************************/
  private void next_screen()
  {
     mypastweather form = new mypastweather();
     form.setSize(800, 600);
     form.setVisible(true);
  }


  /***********************************************************************************************/
  /*                                                                                             */
  /*                                                                                             */
  /*                                                                                             */
  /***********************************************************************************************/
  private void Fill_specific_weather_listbox_from_precip()
  {
      // first remove all -not suitable for this obs- selections
      jList1.clearSelection();                              // no precip general weather
      jList3.clearSelection();                              // specific weather
      
      if (jList2.isSelectedIndex(0))
      {
         model3.removeAllElements();
         model3.addElement(ww_not_determined);
      } // if (jList2.isSelectedIndex(0))
      else if (jList2.isSelectedIndex(1))
      {
         model3.removeAllElements();
         model3.addElement(ww_99);
         model3.addElement(ww_98);
         model3.addElement(ww_97);
         model3.addElement(ww_96);
         model3.addElement(ww_95);
      } // else if (jList2.isSelectedIndex(1))
      else if (jList2.isSelectedIndex(2))
      {
         model3.removeAllElements();
         model3.addElement(ww_94);
         model3.addElement(ww_93);
         model3.addElement(ww_92);
         model3.addElement(ww_91);
      } // else if (jList2.isSelectedIndex(2))
      else if (jList2.isSelectedIndex(3))
      {
         model3.removeAllElements();
         model3.addElement(ww_90);
         model3.addElement(ww_89);
         model3.addElement(ww_88);
         model3.addElement(ww_87);
         model3.addElement(ww_86);
         model3.addElement(ww_85);
         model3.addElement(ww_84);
         model3.addElement(ww_83);
         model3.addElement(ww_82);
         model3.addElement(ww_81);
         model3.addElement(ww_80);
      } // else if (jList2.isSelectedIndex(3))
      else if (jList2.isSelectedIndex(4))
      {
         model3.removeAllElements();
         model3.addElement(ww_79);
         model3.addElement(ww_78);
         model3.addElement(ww_77);
         model3.addElement(ww_76);
         model3.addElement(ww_75);
         model3.addElement(ww_74);
         model3.addElement(ww_73);
         model3.addElement(ww_72);
         model3.addElement(ww_71);
         model3.addElement(ww_70);
      } // else if (jList2.isSelectedIndex(4))
      else if (jList2.isSelectedIndex(5))
      {
         model3.removeAllElements();
         model3.addElement(ww_69);
         model3.addElement(ww_68);
         model3.addElement(ww_67);
         model3.addElement(ww_66);
         model3.addElement(ww_65);
         model3.addElement(ww_64);
         model3.addElement(ww_63);
         model3.addElement(ww_62);
         model3.addElement(ww_60);
      } // else if (jList2.isSelectedIndex(5))
      else if (jList2.isSelectedIndex(6))
      {
         model3.removeAllElements();
         model3.addElement(ww_59);
         model3.addElement(ww_58);
         model3.addElement(ww_57);
         model3.addElement(ww_56);
         model3.addElement(ww_55);
         model3.addElement(ww_54);
         model3.addElement(ww_53);
         model3.addElement(ww_52);
         model3.addElement(ww_51);
         model3.addElement(ww_50);
      } // else if (jList2.isSelectedIndex(6)) 
  }  


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void Fill_specific_weather_listbox_from_no_precip()
   {
      // first remove all -not suitable for this obs- selections
      jList2.clearSelection();                              // precip general weather
      jList3.clearSelection();                              // specific weather
      
      if (jList1.isSelectedIndex(0))
      {   
         model3.removeAllElements();
         model3.addElement(ww_not_determined);
      } // if (jList1.isSelectedIndex(0))
      else if (jList1.isSelectedIndex(1))
      {
         model3.removeAllElements();
         model3.addElement(ww_17);
      } // else if (jList1.isSelectedIndex(1))   
      else if (jList1.isSelectedIndex(2))
      {
         model3.removeAllElements();
         model3.addElement(ww_49);
         model3.addElement(ww_48);
         model3.addElement(ww_47);
         model3.addElement(ww_46);
         model3.addElement(ww_45);
         model3.addElement(ww_44);
         model3.addElement(ww_43);
         model3.addElement(ww_42);
         model3.addElement(ww_41);
         model3.addElement(ww_40);
      } // else if (jList1.isSelectedIndex(2))    
      else if (jList1.isSelectedIndex(3))
      {
         model3.removeAllElements();
         model3.addElement(ww_39);
         model3.addElement(ww_38);
         model3.addElement(ww_37);
         model3.addElement(ww_36);
         model3.addElement(ww_35);
         model3.addElement(ww_34);
         model3.addElement(ww_33);
         model3.addElement(ww_32);
         model3.addElement(ww_31);
         model3.addElement(ww_30);
      } // else if (jList1.isSelectedIndex(3))    
      else if (jList1.isSelectedIndex(4))
      {
         model3.removeAllElements();
         model3.addElement(ww_29);
         model3.addElement(ww_28);
         model3.addElement(ww_27);
         model3.addElement(ww_26);
         model3.addElement(ww_25);
         model3.addElement(ww_24);
         model3.addElement(ww_23);
         model3.addElement(ww_22);
         model3.addElement(ww_21);
         model3.addElement(ww_20);
      } // else if (jList1.isSelectedIndex(4))    
      else if (jList1.isSelectedIndex(5))
      {
         model3.removeAllElements();
         model3.addElement(ww_19);
         model3.addElement(ww_18);
      } // else if (jList1.isSelectedIndex(5))
      else if (jList1.isSelectedIndex(6))
      {
         model3.removeAllElements();
         model3.addElement(ww_16);
         model3.addElement(ww_15);
         model3.addElement(ww_14);
         model3.addElement(ww_13);
      } // else if (jList1.isSelectedIndex(6))
      else if (jList1.isSelectedIndex(7))
      {
         model3.removeAllElements();
         model3.addElement(ww_12);
         model3.addElement(ww_11);
         model3.addElement(ww_10);
      } // else if (jList1.isSelectedIndex(7))
      else if (jList1.isSelectedIndex(8))
      {
         model3.removeAllElements();
         model3.addElement(ww_9);
         // model3.addElement(ww_8); NOT FOR MARINE USE
         model3.addElement(ww_7);
         model3.addElement(ww_6);
         model3.addElement(ww_5);
         model3.addElement(ww_4);
      } // else if (jList1.isSelectedIndex(8))    
      else if (jList1.isSelectedIndex(9))
      {
         model3.removeAllElements();
         model3.addElement(ww_3);
         model3.addElement(ww_2);
         model3.addElement(ww_1);
         model3.addElement(ww_0);
      } // else if (jList1.isSelectedIndex(9))    
   }
   
   
   /**
    * @param args the command line arguments
    */
   public static void main(String args[]) {
      java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
         public void run() {
            new mypresentweather().setVisible(true);
         }
      });
   }
   
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton jButton1;
   private javax.swing.JButton jButton2;
   private javax.swing.JButton jButton3;
   private javax.swing.JButton jButton4;
   private javax.swing.JButton jButton5;
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
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JLabel jLabel6;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JLabel jLabel8;
   private javax.swing.JLabel jLabel9;
   private javax.swing.JList jList1;
   private javax.swing.JList jList2;
   private javax.swing.JList jList3;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JScrollPane jScrollPane3;
   private javax.swing.JSeparator jSeparator1;
   // End of variables declaration//GEN-END:variables
   
   
   // constants global
   public static final String ww_0 = "cloud development not observed or not observable";
   public static final String ww_1 = "clouds generally dissolving or becoming less developed";
   public static final String ww_2 = "state of the sky on the whole unchanged";
   public static final String ww_3 = "clouds generally forming or developing";
   public static final String ww_4 = "visibility reduced by smoke, e.g. industrial smoke, volcanic ash, forest fire";
   public static final String ww_5 = "haze";
   public static final String ww_6 = "widespread dust in suspension in the air, not raised by wind, at time of observation";
   public static final String ww_7 = "blowing spray at the station";
   public static final String ww_9 = "duststorm or sandstorm within sight at time of observation or during the preceding hour";
   public static final String ww_10 = "mist (visibility > 0.5 nm)";
   public static final String ww_11 = "shallow fog in patches (not deeper than 10 metres)";
   public static final String ww_12 = "shallow fog, more or less continuous (not deeper than 10 metres)";
   public static final String ww_13 = "lightning visible, no thunder heard";
   public static final String ww_14 = "precipitation, not reaching the surface of sea";
   public static final String ww_15 = "precipitation, beyond 2.7 nm, reaching the surface of sea";
   public static final String ww_16 = "precipitation, within 2.7 nm, reaching the surface of sea";
   public static final String ww_17 = "thunder audible during the last 10 minutes";
   public static final String ww_18 = "squalls, at or within sight of station";
   public static final String ww_19 = "funnel cloud(s) (tornado cloud or waterspout), at or within sight of station";
   public static final String ww_20 = "drizzle (not freezing) or snow grains";
   public static final String ww_21 = "rain (not freezing), not in showers";
   public static final String ww_22 = "snow, not in showers";
   public static final String ww_23 = "rain and snow or ice pellets, not in showers";
   public static final String ww_24 = "freezing drizzle or freezing rain, not in showers";
   public static final String ww_25 = "shower(s) of rain";
   public static final String ww_26 = "shower(s) of snow, or of rain and snow";
   public static final String ww_27 = "shower(s) of hail, or of hail and rain";
   public static final String ww_28 = "fog or ice fog";
   public static final String ww_29 = "thunderstorm, with or without precipitation";
   public static final String ww_30 = "duststorm or sandstorm, decreasing last hour, slight or moderate";
   public static final String ww_31 = "duststorm or sandstorm, unchanging last hour, slight or moderate";
   public static final String ww_32 = "duststorm or sandstorm, increasing last hour, slight or moderate";
   public static final String ww_33 = "duststorm or sandstorm, decreasing last hour, severe";
   public static final String ww_34 = "duststorm or sandstorm, unchanging last hour, severe";
   public static final String ww_35 = "duststorm or sandstorm, increasing last hour, severe";
   public static final String ww_36 = "drifting snow, below eye level, slight or moderate";
   public static final String ww_37 = "drifting snow, below eye level, heavy";
   public static final String ww_38 = "blowing snow, above eye level, slight or moderate";
   public static final String ww_39 = "blowing snow, above eye level, heavy";
   public static final String ww_40 = "fogbank (app. vis. in bank < 0.5 nm) at a distance;not at stat. during last hr";
   public static final String ww_41 = "fog in patches (apparent vis. in patches < 0.5 nm)";
   public static final String ww_42 = "fog (vis. < 0.5 nm), thinning in last hour, sky discernible";
   public static final String ww_43 = "fog (vis. < 0.5 nm), thinning in last hour, sky not discernible";
   public static final String ww_44 = "fog (vis. < 0.5 nm), unchanging in last hour, sky discernible";
   public static final String ww_45 = "fog (vis. < 0.5 nm), unchanging in last hour, sky not discernible";
   public static final String ww_46 = "fog (vis. < 0.5 nm), beginning or thickening in last hour, sky discernible";
   public static final String ww_47 = "fog (vis. < 0.5 nm), beginning or thickening in last hour, sky not discernible";
   public static final String ww_48 = "fog (vis. < 0.5 nm), depositing rime, sky discernible";
   public static final String ww_49 = "fog (vis. < 0.5 nm), depositing rime, sky not discernible";
   public static final String ww_50 = "slight drizzle, not freezing, intermittent";
   public static final String ww_51 = "slight drizzle, not freezing, continous";
   public static final String ww_52 = "moderate drizzle, not freezing, intermittent";
   public static final String ww_53 = "moderate drizzle, not freezing, continous";
   public static final String ww_54 = "dense drizzle, not freezing, intermittent";
   public static final String ww_55 = "dense drizzle, not freezing, continous";
   public static final String ww_56 = "drizzle, freezing, slight";
   public static final String ww_57 = "drizzle, freezing, moderate or dense";
   public static final String ww_58 = "drizzle and rain, slight";
   public static final String ww_59 = "drizzle and rain, moderate or dense";
   public static final String ww_60 = "slight rain, not freezing, intermittent";
   public static final String ww_61 = "slight rain, not freezing, continous";
   public static final String ww_62 = "moderate rain, not freezing, intermittent";
   public static final String ww_63 = "moderate rain, not freezing, continous";
   public static final String ww_64 = "heavy rain, not freezing, intermittent";
   public static final String ww_65 = "heavy rain, not freezing, continous";
   public static final String ww_66 = "rain, freezing, slight";
   public static final String ww_67 = "rain, freezing, moderate or heavy";
   public static final String ww_68 = "rain and snow or drizzle and snow, slight";
   public static final String ww_69 = "rain and snow or drizzle and snow, moderate or heavy";
   public static final String ww_70 = "slight fall of snow flakes, intermittent";
   public static final String ww_71 = "slight fall of snow flakes, continous";
   public static final String ww_72 = "moderate fall of snow flakes, intermittent";
   public static final String ww_73 = "moderate fall of snow flakes, continous";
   public static final String ww_74 = "heavy fall of snow flakes, intermittent";
   public static final String ww_75 = "heavy fall of snow flakes, continous";
   public static final String ww_76 = "ice prisms (with or without fog)";
   public static final String ww_77 = "snow grains (with or without fog)";
   public static final String ww_78 = "isolated star-like snow crystals (with or without fog)";
   public static final String ww_79 = "ice pellets";
   public static final String ww_80 = "rain shower(s), slight";
   public static final String ww_81 = "rain shower(s), moderate or heavy";
   public static final String ww_82 = "rain shower(s), violent";
   public static final String ww_83 = "shower(s) of rain and snow mixed, slight";
   public static final String ww_84 = "shower(s) of rain and snow mixed, moderate or heavy";
   public static final String ww_85 = "snow shower(s), slight";
   public static final String ww_86 = "snow shower(s), moderate or heavy";
   public static final String ww_87 = "slight shower(s) of soft or small hail (with or without rain/snow)";
   public static final String ww_88 = "moderate or heavy showers of soft or small hail (with or without rain/snow)";
   public static final String ww_89 = "slight showers of hail (with or without rain/snow)";
   public static final String ww_90 = "moderate or heavy showers of hail (with or without rain/snow)";
   public static final String ww_91 = "slight rain; thunder heard";
   public static final String ww_92 = "moderate or heavy rain; thunder heard";
   public static final String ww_93 = "slight snow, or rain and snow mixed, or hail; thunder heard";
   public static final String ww_94 = "moderate or heavy snow, or rain and snow mixed, or hail; thunder heard";
   public static final String ww_95 = "thunderstorm (thunder heard), slight or moderate, without hail";
   public static final String ww_96 = "thunderstorm (thunder heard), slight or moderate, with hail";
   public static final String ww_97 = "thunderstorm (thunder heard), heavy, without hail";
   public static final String ww_98 = "thunderstorm (thunder heard), combined with duststorm or sandstorm";
   public static final String ww_99 = "thunderstorm (thunder heard), heavy, with hail";
   public static final String ww_not_determined = "not determined";
   
   // local constants
   //private final int INVALID                          = 9999999;
   private final String PRESENTWEATHER_HELP_DIR       = "weather.html";

   // scope this module + myturbowin.java main module (all of type: static) 
   public static String ww_code                       = "";
   public static String present_weather               = "";
   
   // local (scope only this file, no static) 
   private int int_ww_code                            = main.INVALID;
   private boolean checks_ok                          = false;
   private boolean general_weather_condition_selected = false;
   private DefaultListModel model3;                           // for specific weather

}