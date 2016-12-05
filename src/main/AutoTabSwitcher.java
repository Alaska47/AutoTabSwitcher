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

		File dll = new File("res\\dll");
		for(File a : dll.listFiles()) {
			System.load(a.getAbsolutePath());
		}
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}


	private static void switchToWindow(String title) {
		boolean result = JNAHelper.setForegroundWindowByName(title, true);
		System.out.println("result: " + result);
	}

}
