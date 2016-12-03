package main;

import java.io.File;
import java.util.List;

import jna.JNAHelper;

public class AutoTabSwitcher {

	public static void main(String[] args) {
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
	}
	
	private static void loadLibraries() {
		File dll = new File("res\\dll");
		for(File a : dll.listFiles()) {
			System.load(a.getAbsolutePath());
		}
	}
	
	private static void switchToWindow(String title) {
	      boolean result = JNAHelper.setForegroundWindowByName(title, true);
	      System.out.println("result: " + result);
	   }

}
