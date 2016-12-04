package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;

import cv.FacialRecognition;
import jna.JNAHelper;
import utils.ImageJPanel;

public class AutoTabSwitcher {

	public static void main(String[] args) {
		/*
		String work = "PowerPoint";
	    String play = "Eclipse";
	    String workPage = "";
	    String playPage = "";
	    
	    List<String> winNameList = JNAHelper.getAllWindowNames();
	      for (String winName : winNameList) {
	         if(winName.contains(work))
	            workPage = winName;
	            
	         if(winName.contains(play))
	            playPage = winName;
	      }
	      
	      switchToWindow(workPage);
	      */
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		loadLibraries();
		
		JFrame main = new JFrame("AutoTabSwitcher");
				
		ImageJPanel p = new ImageJPanel();
	    
	    main.setContentPane(p);
	    		
	    main.pack();
	    main.setVisible(true);
	    
	    new FacialRecognition(p);
	}
	
	
	private static void loadLibraries() {
		boolean is64bit = false;
		if (System.getProperty("os.name").contains("Windows")) {
		    is64bit = (System.getenv("ProgramFiles(x86)") != null);
		} else {
		    is64bit = (System.getProperty("os.arch").indexOf("64") != -1);
		}
		File dll = new File("res\\dll");
		for(File a : dll.listFiles()) {
			if(a.getName().contains("FreeImage")) {
				if(a.getName().contains("64")) {
					if(is64bit) {
						System.load(a.getAbsolutePath());
						System.out.println("Loaded FreeImage64");
					}
		
				} else {
					if(!is64bit) {
						System.load(a.getAbsolutePath());
						System.out.println("Loaded FreeImage32");
					}
				}
			
			} else {
				try {
					System.load(a.getAbsolutePath());
				} catch(NoSuchMethodError e) {
					e.printStackTrace();
					System.out.println(a.getAbsolutePath());
				}
			}
		}
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	
	private static void switchToWindow(String title) {
	      boolean result = JNAHelper.setForegroundWindowByName(title, true);
	      System.out.println("result: " + result);
	   }

}
