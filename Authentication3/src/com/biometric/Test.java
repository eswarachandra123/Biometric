package com.biometric;

import java.util.Scanner;

public class Test {
  public static void main(String args[]) {
	  System.out.println("Enter message");
	  Scanner scr = new Scanner(System.in);
	  String message = scr.nextLine();
	  MyResult result = AES.AESencrypt(message);
	  System.out.println(result.getskey());
	  System.out.println(result.getmessage());
	  String data = AES.AESdecrypt(result.getskey(),result.getmessage());
	  System.out.println(data);
	  
  }
}
