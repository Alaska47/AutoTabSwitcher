package jna;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JNAHelper {

   public static List<String> getAllWindowNames() {
      JNAHelper m = new JNAHelper();
      final List<String> inflList = new ArrayList<String>();
      final List<Integer> order = new ArrayList<Integer>();
      int top = User32.instance.GetTopWindow(0);
      while (top!=0) {
         order.add(top);
         top = User32.instance.GetWindow(top, User32.GW_HWNDNEXT);
      }
      User32.instance.EnumWindows(
         new WndEnumProc()
         {
            public boolean callback(int hWnd, int lParam)
            {
               if (User32.instance.IsWindowVisible(hWnd)) {
                  byte[] buffer = new byte[1024];
                  User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
                  String title = Native.toString(buffer);
                  if(title.split(" ").length > 1)
                     inflList.add(title);
               }
               return true;
            }
         }, 0);
      return inflList;
   }

   public static interface WndEnumProc extends StdCallLibrary.StdCallCallback {
      boolean callback (int hWnd, int lParam);
   }
   
   public static boolean setForegroundWindowByName(final String windowName,
         final boolean starting) {
      final User32 user32 = User32.INSTANCE;
      return user32.EnumWindows(
         new User32.WNDENUMPROC() {
         
            @Override
            public boolean callback(Pointer hWnd, Pointer arg) {
               byte[] windowText = new byte[512];
               user32.GetWindowTextA(hWnd, windowText, 512);
               String wText = Native.toString(windowText);
            // if (wText.contains(WINDOW_TEXT_TO_FIND)) {
               if (starting) {
                  if (wText.startsWith(windowName)) {
                     user32.SetForegroundWindow(hWnd);
                     return false;
                  }
               } 
               else {
                  if (wText.contains(windowName)) {
                     user32.SetForegroundWindow(hWnd);
                     return false;
                  }
               }
               return true;
            }
         }, null);
   }

   public static interface User32 extends StdCallLibrary
   {
      final User32 instance = (User32) Native.loadLibrary ("user32", User32.class);
      boolean EnumWindows (WndEnumProc wndenumproc, int lParam);
      boolean IsWindowVisible(int hWnd);
      void GetWindowTextA(int hWnd, byte[] buffer, int buflen);
      int GetTopWindow(int hWnd);
      int GetWindow(int hWnd, int flag);
      final int GW_HWNDNEXT = 2;
      
      User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
   
      interface WNDENUMPROC extends StdCallCallback {
         boolean callback(Pointer hWnd, Pointer arg);
      }
   
      boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer arg);
   
      int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);
   
      int SetForegroundWindow(Pointer hWnd);
   
      Pointer GetForegroundWindow();
   }
}