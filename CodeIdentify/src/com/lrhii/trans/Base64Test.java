package com.lrhii.trans;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;

public class Base64Test {
	/*** 
     * encode by Base64 
     */  
    public static String encodeBase64(byte[]input) throws Exception{  
        Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");  
        Method mainMethod= clazz.getMethod("encode", byte[].class);  
        mainMethod.setAccessible(true);  
         Object retObj=mainMethod.invoke(null, new Object[]{input});  
         return (String)retObj;  
    }  
    /*** 
     * decode by Base64 
     */  
    public static byte[] decodeBase64(String input) throws Exception{  
        Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");  
        Method mainMethod= clazz.getMethod("decode", String.class);  
        mainMethod.setAccessible(true);  
         Object retObj=mainMethod.invoke(null, input);  
         return (byte[])retObj;  
    } 
    
    public static void byte2image(byte[] data,String path){
	    if(data.length<3||path.equals("")) return;
	    try{
	    FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
	    imageOutput.write(data, 0, data.length);
	    imageOutput.close();
	    System.out.println("Make Picture success,Please find image in " + path);
	    } catch(Exception ex) {
	      System.out.println("Exception: " + ex);
	      ex.printStackTrace();
	    }
  }
    
    public static byte[] image2byte(String path){
        byte[] data = null;
        FileImageInputStream input = null;
        try {
          input = new FileImageInputStream(new File(path));
          ByteArrayOutputStream output = new ByteArrayOutputStream();
          byte[] buf = new byte[1024];
          int numBytesRead = 0;
          while ((numBytesRead = input.read(buf)) != -1) {
          output.write(buf, 0, numBytesRead);
          }
          data = output.toByteArray();
          output.close();
          input.close();
        }
        catch (FileNotFoundException ex1) {
          ex1.printStackTrace();
        }
        catch (IOException ex1) {
          ex1.printStackTrace();
        }
        return data;
      }
    
    public static void main(String[] args) {
    	try {
    		byte[] bytes = Base64Test.decodeBase64("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");
    		 //byte数组到图片
    		Base64Test.byte2image(bytes, "d://check.png");
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public static void baseToImage(String checkCode,String fileName){
    	try {
    		byte[] bytes = Base64Test.decodeBase64(checkCode);
    		 //byte数组到图片
    		Base64Test.byte2image(bytes, "E://source//"+fileName+".png");
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
