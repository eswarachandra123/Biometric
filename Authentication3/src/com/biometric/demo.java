package com.biometric;

import java.io.File;
import java.io.IOException;

import com.mintutiae.image.MintutiaeAlgo;
public class demo {
    public static void main(String args[]) throws SecurityException, IOException {
    	String file1 ="C:/Users/malla kavya/Desktop/20.jpg";
//    	File file = new File(file1);
//    	System.out.println(var.replace('','/'));
//    	String file2 = "E:\\images\\istockphoto-182188504-1024x1024.jpg";
//    	File file = new File(file1.replaceAll("C:\\Users\\malla kavya\\Desktop\\","E:\\images\\"));
  //  	System.out.println(file.toString());
//    	System.out.println("File Path = " + file.getPath());
//        System.out.println("Absolute Path = " + file.getAbsolutePath());
//        System.out.println("Canonical Path = " + file.getCanonicalPath());
//    	
//    	System.out.println(file.toString());
//        String var = file.toString();
////    	System.out.println(var.replace('\\','/'));
//////    	System.out.println(var.replaceAll("\\s",""));
    	boolean status1 = MintutiaeAlgo.checkimage(file1);
   	System.out.println(status1);
//   	System.out.println(name);
//    	System.out.println(var);
    	
//    	boolean status = MintutiaeAlgo.test(file1,file1);
//    	System.out.println(status);
    }
    
}
