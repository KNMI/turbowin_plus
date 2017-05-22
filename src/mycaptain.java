
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * mycaptain.java
 *
 * Created on 22-apr-2009, 9:52:07
 */

/**
 *
 * @author Martin
 */
public class mycaptain extends javax.swing.JFrame {

    /** Creates new form mycaptain */
    public mycaptain() {
        initComponents();
        initComponents2();
        setLocation(main.x_pos_frame, main.y_pos_frame);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jSeparator1 = new javax.swing.JSeparator();
      jButton1 = new javax.swing.JButton();
      jButton2 = new javax.swing.JButton();
      jScrollPane1 = new javax.swing.JScrollPane();
      jTable1 = new javax.swing.JTable();
      jLabel1 = new javax.swing.JLabel();
      jLabel2 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      jButton3 = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle("Captains");
      setMinimumSize(new java.awt.Dimension(800, 600));
      setResizable(false);

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

      jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 322));

      jTable1.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null}
         },
         new String [] {
            "surname", "full initials*", "date of joining**", "date of leaving**", "discharge book***"
         }
      ) {
         Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
         };

         public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
         }
      });
      jTable1.getTableHeader().setReorderingAllowed(false);
      jScrollPane1.setViewportView(jTable1);

      jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel1.setText("*e.g. M.F.C.M.D.                **format DD/MM/YYYY                 ***discharge book or seaman's card number if applicable");

      jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel2.setText("--- double click cell to insert ---");

      jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      jLabel3.setText("--- to clear this table select: Maintenance --> Move log files  (do not delete data in individual cells) ---");

      jButton3.setText("Internet");
      jButton3.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            Internet_button_actionPerformed(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
            .addContainerGap())
         .addGroup(layout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
               .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
               .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
               .addGroup(layout.createSequentialGroup()
                  .addGap(50, 50, 50)
                  .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(18, 18, 18))
         .addGroup(layout.createSequentialGroup()
            .addGap(260, 260, 260)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(268, Short.MAX_VALUE))
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addContainerGap(147, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel1)
            .addGap(117, 117, 117)
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel3)
            .addGap(34, 34, 34)
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(15, 15, 15)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(15, 15, 15))
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents






   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void lees_captain_log_en_vul_table()
   {
      /* NB input/output GUI always via a SwingWorker (Core Java Volume 1 bld 795 e.v.; Volume 2 bld 37, 215) */

      new SwingWorker<Void, Void>()
      {
         @Override
         protected Void doInBackground() throws Exception
         {
            String record;
            int pos_begin;
            int pos_eind;
            int r;

            /* initialisation */
            clear_captain_data_array();


            String volledig_path = main.logs_dir + java.io.File.separator + main.CAPTAIN_LOG;//"java.io.File.separator" os independent

            /* read all lines from captain log */
            try
            {
               BufferedReader in = new BufferedReader(new FileReader(volledig_path));

               r = 0;
               while((record = in.readLine()) != null)
               {
                  pos_begin = 0;
                  pos_eind = 0;
                  for (int c = 0; c < CAPTAIN_COLUMNS; c++)
                  {
                     pos_eind = record.indexOf(";", pos_begin);                                   // Returns the index within this string of the first occurrence of the specified substring, starting at the specified index.

                     if (pos_eind != -1)
                     {
                        captain_data[r][c] = record.substring(pos_begin, pos_eind);
                        pos_begin = pos_eind +1;
                     }
                     else
                     {
                        break;
                     }
                  } // for (int c = 0; c < OBSERVER_COLUMNS; c++)

                  r++;

                  /* safety */
                  if (r >= CAPTAIN_ROWS)
                  {
                     break;
                  }
               } // while((file_line = in.readLine()) != null)
               in.close();
            } // try
            catch (Exception e)
            {
               // do nothing, possible file was never created
            } // catch

            return null;
         } // protected Void doInBackground() throws Exception

         @Override
         protected void done()
         {
            // collect data from all table cells
            for (int r = 0; r < CAPTAIN_ROWS; r++)
            {
               for (int c = 0; c < CAPTAIN_COLUMNS; c++)
               {
                  jTable1.setValueAt(captain_data[r][c], r, c);
               }
            }

         }
      }.execute(); // new SwingWorker<Void, Void>()
   }



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void initComponents2()
   {




      
      if (main.offline_mode == true)
      {
         jButton3.setText("Help");
      }
      

      /* for Germany recruited ships different column heading (christian name instead of full initials) */
      if (main.recruiting_country.indexOf("GERMANY") != -1)
      {
         DefaultTableModel model = new DefaultTableModel
         (
            new Object [][]
            {
               {null, null, null, null, null},
               {null, null, null, null, null},
               {null, null, null, null, null},
               {null, null, null, null, null},
               {null, null, null, null, null},
               {null, null, null, null, null},
               {null, null, null, null, null},
               {null, null, null, null, null},
               {null, null, null, null, null},
               {null, null, null, null, null}
            },
            new String []
            {
               "surname", "full christian name*", "date of joining", "date of leaving", "discharge book**"
            }
         );

         jTable1.setModel(model);

         jLabel1.setText("*e.g. Maximilian Louis Leon                                        **discharge book or seaman's card number if applicable");

      } // if (main.recruiting_country.indexOf("Germany") != -1)

      lees_captain_log_en_vul_table();
   }



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   public void stopCellEditing()
   {
      TableCellEditor editor = jTable1.getCellEditor();
      if (editor != null )
      {
         editor.stopCellEditing();
      }
   }


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void clear_captain_data_array()
   {
      for (int r = 0; r < CAPTAIN_ROWS; r++)
      {
         for (int c = 0; c < CAPTAIN_COLUMNS; c++)
         {
            captain_data[r][c] = "";
         }
      } // for (int r = 0; r < CAPTAIN_ROWS; r++)
   }


   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
    private void OK_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OK_button_actionPerformed
       // TODO add your handling code here:

       boolean doorgaan = true;
       String hulp_date_of_joining = "";
       String hulp_date_of_leaving = "";

       /* initialisation */
       clear_captain_data_array();


       // NOTE: stopCellEditing() is called automatically when you hit [Enter] or [Tab] on a cell indicating the end of editing
       //       But we want it to stop if the user click the OK button, if fuction stopCellEditing() is not called
       //       it is possible that the last changes/inserts of a cell, if this cell has still the focus, are ignored
       //       when pressing the OK button
       stopCellEditing();


       // check correct format date of joining
       for (int r = 0; r < CAPTAIN_ROWS; r++)
       {
          hulp_date_of_joining = (String)jTable1.getValueAt(r, COLUMN_NUMBER_DATE_JOINING);

          if ((hulp_date_of_joining.length() > 0) && (hulp_date_of_joining.compareTo("-") != 0) && (hulp_date_of_joining.length() != 10))
          {
             JOptionPane.showMessageDialog(null, "format date of joining must be DD/MM/YYYY, updated data not saved", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
             doorgaan = false;
          } // if ((hulp_date_of_joining.length() > 0

          if (doorgaan && hulp_date_of_joining.length() == 10)
          {
             if ((hulp_date_of_joining.substring(2,3).compareTo("/") != 0) || (hulp_date_of_joining.substring(5,6).compareTo("/") != 0))
             {
                JOptionPane.showMessageDialog(null, "format date of joining must be DD/MM/YYYY, updated data not saved", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                doorgaan = false;
             }
          } // if ((hulp_date_of_joining.length() > 0
       } // for (int r = 0; r < CAPTAIN_ROWS; r++)


       if (doorgaan)
       {
          // check correct format date of leaving
          for (int r = 0; r < CAPTAIN_ROWS; r++)
          {
             hulp_date_of_leaving = (String)jTable1.getValueAt(r, COLUMN_NUMBER_DATE_LEAVING);

             if ((hulp_date_of_leaving.length() > 0) && (hulp_date_of_leaving.compareTo("-") != 0) && (hulp_date_of_leaving.length() != 10))
             {
                JOptionPane.showMessageDialog(null, "format date of leaving must be DD/MM/YYYY, updated data not saved", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                doorgaan = false;
             }

             if (doorgaan && hulp_date_of_leaving.length() == 10)
             {
                if ((hulp_date_of_leaving.substring(2,3).compareTo("/") != 0) || (hulp_date_of_leaving.substring(5,6).compareTo("/") != 0))
                {
                   JOptionPane.showMessageDialog(null, "format date of leaving must be DD/MM/YYYY, updated data not saved", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
                   doorgaan = false;
                }
             } // if ((hulp_date_of_joining.length() > 0
          } // for (int r = 0; r < CAPTAIN_ROWS; r++)
       } // if (doorgaan)



       if (doorgaan)
       {
          // collect data from all table cells
          for (int r = 0; r < CAPTAIN_ROWS; r++)
          {
             for (int c = 0; c < CAPTAIN_COLUMNS; c++)
             {
                captain_data[r][c] = (String)jTable1.getValueAt(r, c);
             }
          }

          /* save observer data to file */
          schrijf_captain_log();



          /* update observer field on main (progress) screen */
          //main.captain_field_update();


          /* close input page */
          setVisible(false);
          dispose();


       } // if (doorgaan)


    }//GEN-LAST:event_OK_button_actionPerformed



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
   private void schrijf_captain_log()
   {
      /* NB input/output in a GUI always via a SwingWorker (Core Java Volume 1 bld 795 e.v.; Volume 2 bld 37, 215) */
      boolean doorgaan = true;


      // first test if logs dir was defined
      if (main.logs_dir.equals("") == true)
      {
         JOptionPane.showMessageDialog(null, "Logs folder unknown, please select: Maintenance -> Log files settings", main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
         doorgaan = false;
      }

      // ok, logs dir is known
      if (doorgaan == true)
      {
         new SwingWorker<Void, Void>()
         {
            @Override
            protected Void doInBackground() throws Exception
            {
               String volledig_path = main.logs_dir + java.io.File.separator + main.CAPTAIN_LOG;

               try
               {
                  BufferedWriter out = new BufferedWriter(new FileWriter(volledig_path));

                  for (int r = 0; r < CAPTAIN_ROWS; r++)
                  {
                     // at least surname must be present (c = 0)
                     if ((captain_data[r][0] != null) && (captain_data[r][0].compareTo("") != 0))
                     {
                        for (int c = 0; c < CAPTAIN_COLUMNS; c++)
                        {
                           if ((captain_data[r][c] != null) && (captain_data[r][c].compareTo("") != 0))
                           {
                              out.write(captain_data[r][c]);
                           }
                           else // empty field/cell
                           {
                              out.write("-");
                           }

                           out.write(";");                     // semi-column seperated
                        } // for (int c = 0; c < CAPTAIN_COLUMNS; c++)

                        out.newLine();   // newLine(): write a line separator. The line separator string is defined by the system property line.separator, and is not necessarily a single newline ('\n') character.

                     } // if ((captain_data[r][0] != null)
                  } // for (int r = 0; r < CAPTAIN_ROWS; r++)

                  out.close();

               } // try
               catch (Exception e)
               {
                  JOptionPane.showMessageDialog(null, "unable to write to: " + volledig_path, main.APPLICATION_NAME + " error", JOptionPane.WARNING_MESSAGE);
               } // catch

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
    private void Cancel_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel_button_actionPerformed
       // TODO add your handling code here:

       /* close input page */
       setVisible(false);
       dispose();
    }//GEN-LAST:event_Cancel_button_actionPerformed



   /***********************************************************************************************/
   /*                                                                                             */
   /*                                                                                             */
   /*                                                                                             */
   /***********************************************************************************************/
    private void Internet_button_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Internet_button_actionPerformed
       // TODO add your handling code here:
       main.internet_mouseClicked(CAPTAIN_HELP_DIR);
    }//GEN-LAST:event_Internet_button_actionPerformed

    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
         @Override
            public void run() {
                new mycaptain().setVisible(true);
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
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JSeparator jSeparator1;
   private javax.swing.JTable jTable1;
   // End of variables declaration//GEN-END:variables



   // constants
   public static final int CAPTAIN_ROWS                = 10; // static !!
   public static final int CAPTAIN_COLUMNS             = 5;  // static !!
   private static final int COLUMN_NUMBER_DATE_JOINING = 2;
   private static final int COLUMN_NUMBER_DATE_LEAVING = 3;
   private final String CAPTAIN_HELP_DIR               = "captain.html";

   // scope this module + myturbowin.java main module (all of type: static)
   public static String[][] captain_data               = new String[CAPTAIN_ROWS][CAPTAIN_COLUMNS];// default values: null

}
