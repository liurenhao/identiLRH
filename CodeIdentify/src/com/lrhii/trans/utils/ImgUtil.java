package com.lrhii.trans.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;

import com.lrhii.exception.IIException;

public class ImgUtil {
	/**
	 * btye数组转成图片并保存
	 * @param data
	 * @param path
	 * @throws IIException
	 */
	public static void byte2image(byte[] data,String path) throws IIException{
		 if(data.length<3||path.equals("")) return;
		 try{
			 FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
			 imageOutput.write(data, 0, data.length);
			 imageOutput.close();
		 } catch(Exception ex) {
			 ex.printStackTrace();
			 throw new IIException("0002", "byte[]转图片失败！");
		 }
	 }
	 
	/**
	 * 按路径读取图片并转成byte数组
	 * @param path
	 * @return
	 */
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
	
	/**
	 * byte数组转图像流
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	public static BufferedImage btyeToBI(byte[] data) throws Exception{
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(data));
		return bufferedImage;
	}
	
	/**
	 * 图像流转byte数组
	 * @param bufferedImage
	 * @return
	 * @throws Exception
	 */
	public static byte[] bIToByte(BufferedImage bufferedImage) throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "PNG", out);
		byte[] byteArray = out.toByteArray();
		return byteArray;
	}
	
}
