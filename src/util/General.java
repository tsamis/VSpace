package util;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import main.BZEclipse;

import processing.core.PApplet;
import processing.core.PImage;

public class General {
	public static int wrapIndex(int i,int limit){
		int res = (i+limit)%limit;
		if(res<0){
			res = limit+res;
		}
		return res;
	}
	public static PImage loadImageFile(BZEclipse parent){
		 
		  // set system look and feel

		  try {
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		  }
		  catch (Exception e) {
		    e.printStackTrace(); 
		  }


		//Create a file chooser
		final JFileChooser fc = new JFileChooser();

		//In response to a button click:
		int returnVal = fc.showOpenDialog(parent);
		PImage img;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		  File file = fc.getSelectedFile();
		  // see if it's an image
		  // (better to write a function and check for all supported extensions)
		  if (file.getName().endsWith("jpg")||file.getName().endsWith("png")) {
		    img = parent.loadImage(file.getPath());
		    
		  }else{
		    img=null;
		    System.out.println("We do not support this format Yet");
		  }
		} 
		  else {
		    img=null;
		    System.out.println("Open command cancelled by user.");
		  }
		  return img;
		}
}
