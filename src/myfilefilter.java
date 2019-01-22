package turbowin;


import java.io.File;
import javax.swing.filechooser.FileFilter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Martin
 */

/* a Filter is e.g. used by FileChooserDemo2.java (see oracle/java web site) */
public class myfilefilter extends FileFilter
{
   //Accept all directories and fiel configuration.rxt
   @Override
   public boolean accept(File f)
   {
      if (f.isDirectory())
      {
         return true;
      }

      String filename = f.getName();
      if (filename.equals("configuration.txt") == true)
      {
          return true;
      }
      else
      {
         // so no dir and no file "configuration.txt"
         return false;
      }
   } // public boolean accept(File f)

   //The description of this filter
   @Override
   public String getDescription()
   {
      return "configuration.txt";
   }
}

