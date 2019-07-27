package com.biometric;

import java.io.IOException;

import com.mintutiae.image.MintutiaeAlgo;
public class demo {
    public static void main(String args[]) throws SecurityException, IOException {
    	String file1 ="C:/Users/priyanka.bammidi/biometric/repo/minutiae/istockphoto-899732676-1024x1024.jpg";
    	String file2 = "C:/Users/priyanka.bammidi/biometric/repo/minutiae/istockphoto-899732676-1024x1024.jpg";
    	
    	boolean status = MintutiaeAlgo.test(file1, file2);
    	System.out.println(status);
    }
}
